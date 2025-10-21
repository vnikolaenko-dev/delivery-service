package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.AddressDTO;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.OrderItemDto;
import ru.don_polesie.back_end.enums.OrderStatus;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.AddressMapper;
import ru.don_polesie.back_end.mapper.OrderMapper;
import ru.don_polesie.back_end.model.*;
import ru.don_polesie.back_end.repository.*;
import ru.don_polesie.back_end.service.UserOrderService;
import ru.don_polesie.back_end.service.YooKassaService;
import ru.don_polesie.back_end.service.impl.order.PriceService;

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
    private final PriceService orderService;
    private final PriceService priceService;

    @Override
    public OrderDtoRR findById(Long id) {
        return orderMapper
                .toOrderDtoRR(orderRepository
                        .findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("")));
    }


    @Override
    public Page<OrderDtoRR> findUserOrdersPage(Integer pageNumber, String username) {
        var pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Order> orderPage = orderRepository.findByUserUsername(username, pageable);
        return orderPage.map(orderMapper::toOrderDtoRR);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }


    @Override
    @Transactional
    public OrderCreateResponse save(OrderDtoRR orderDtoRR, User user) {
        Address address = processAddress(orderDtoRR.getAddress());
        Order order = createOrder(orderDtoRR, user, address);
        processOrderItems(orderDtoRR, order);
        try {
            return new OrderCreateResponse(
                    orderMapper.toOrderDtoRR(order),
                    yooKassaServiceImpl.createPayment(String.valueOf(order.getId()))
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteProductFromOrder(Long orderId, Long productId) {
        var order = getOrderById(orderId);
        var product = getProductById(productId);
        var orderProduct = getOrderProduct(orderId, productId);

        BigDecimal productPrice = priceService.calculateProductCost(product, orderProduct.getQuantity());

        order.setTotalAmount(order.getTotalAmount().subtract(productPrice));
        order.removeProduct(orderProduct);
    }

    // ========== ПРИВАТНЫЕ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private Address findExistingAddressOrSaveNew(Address address) {
        // Если у адреса нет ID, значит это новый адрес - сохраняем
        if (address.getId() == null) {
            return addressRepository.save(address);
        }

        // Если ID есть, проверяем существует ли такой адрес
        return addressRepository.findById(address.getId())
                .orElseGet(() -> addressRepository.save(address));
    }

    private void processOrderItems(OrderDtoRR orderDtoRR, Order order) {
        BigDecimal totalAmount = orderDtoRR.getItems().stream()
                .map(orderItem -> processOrderItem(orderItem, order))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PAYING);
        orderRepository.save(order);
    }

    private BigDecimal processOrderItem(OrderItemDto orderItem, Order order) {
        Product product = getProductById(orderItem.getProductId());
        OrderProduct orderProduct = new OrderProduct(order, product, orderItem.getQuantity());

        BigDecimal itemCost = priceService.calculateProductCost(product, orderItem.getQuantity());
        order.addProduct(orderProduct);

        return itemCost;
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Order not found with id: " + orderId));
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + productId));
    }

    private OrderProduct getOrderProduct(Long orderId, Long productId) {
        OrderProductId orderProductId = new OrderProductId(orderId, productId);
        return orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Order product not found for orderId: " + orderId + " and productId: " + productId));
    }

    private Address processAddress(AddressDTO addressDto) {
        Address address = addressMapper.toEntity(addressDto);
        return findExistingAddressOrSaveNew(address);
    }

    private Order createOrder(OrderDtoRR orderDtoRR, User user, Address address) {
        Order order = orderMapper.toOrder(orderDtoRR);
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.NEW);
        order.setTotalAmount(BigDecimal.ZERO);
        return orderRepository.save(order);
    }

}
