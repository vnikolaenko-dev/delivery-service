package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.OrderItemDto;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.enums.OrderStatus;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.AddressMapper;
import ru.don_polesie.back_end.mapper.OrderMapper;
import ru.don_polesie.back_end.model.*;
import ru.don_polesie.back_end.repository.*;
import ru.don_polesie.back_end.service.UserOrderService;
import ru.don_polesie.back_end.service.YooKassaService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserOrderServiceImpl implements UserOrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final YooKassaService yooKassaServiceImpl;
    private final OrderService orderService;



    @Override
    public Page<OrderDtoRR> findUserOrdersPage(Integer pageNumber, String username) {
        var pageable =
                PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Order> orderPage = orderRepository.findByUserUsername(username, pageable);
        return orderPage
                .map(orderMapper::toOrderDtoRR);
    }

    @Override
    @Transactional
    public OrderCreateResponse save(OrderDtoRR orderDtoRR, User user) {
        Address address = addressMapper.toEntity(orderDtoRR.getAddress());
        Address existingAddress = findExistingAddressOrSaveNew(address);

        Order order = orderMapper.toOrder(orderDtoRR);
        order.setUser(user);
        order.setAddress(existingAddress);
        order.setStatus(OrderStatus.NEW);
        order.setTotalAmount(BigDecimal.ZERO);
        order = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemDto od : orderDtoRR.getItems()) {
            var product = productRepository.findById(od.getProductId()).orElseThrow();
            var op = new OrderProduct(order, product, od.getQuantity());

            totalAmount = totalAmount.add(orderService.calculateProductCost(product, od.getQuantity()));

            order.addProduct(op);
        }

        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PAYING);
        order = orderRepository.save(order);

        try {
            return new OrderCreateResponse(
                    orderMapper.toOrderDtoRR(order),
                    yooKassaServiceImpl.createPayment(String.valueOf(order.getId()))
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Address findExistingAddressOrSaveNew(Address address) {
        // Если у адреса нет ID, значит это новый адрес - сохраняем
        if (address.getId() == null) {
            return addressRepository.save(address);
        }

        // Если ID есть, проверяем существует ли такой адрес
        return addressRepository.findById(address.getId())
                .orElseGet(() -> addressRepository.save(address));
    }


    /*
    private String extractGuestIdOrNew() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof GuestPrincipal gp) {
            return gp.getGuestId();
        }
        return UUID.randomUUID().toString();
    }

     */

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public void deleteProductFromOrder(Long orderId, Long productId) {
        var order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException(""));

        var product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException(""));

        var orderProduct = orderProductRepository
                .findById(new OrderProductId(orderId, productId))
                .orElseThrow(() -> new ObjectNotFoundException(""));

        BigDecimal productPrice;
        if (product.getIsWeighted()) {
            productPrice = BigDecimal.valueOf(product.getPrice() *
                    orderProduct.getQuantity() / 1000);
        } else {
            productPrice = BigDecimal.valueOf(product.getPrice() *
                    orderProduct.getQuantity());
        }

        order.setTotalAmount(order.getTotalAmount().subtract(productPrice));
        order.removeProduct(orderProduct);
    }

    @Override
    public OrderDtoRR findById(Long id) {
        return orderMapper
                .toOrderDtoRR(orderRepository
                        .findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException(""))
                );
    }

}
