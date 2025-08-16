package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.OrderItemDto;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.enums.OrderStatus;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.mapper.OrderMapper;
import ru.don_polesie.back_end.model.Order;
import ru.don_polesie.back_end.model.OrderProduct;
import ru.don_polesie.back_end.model.OrderProductId;
import ru.don_polesie.back_end.model.Product;
import ru.don_polesie.back_end.repository.OrderProductRepository;
import ru.don_polesie.back_end.repository.OrderRepository;
import ru.don_polesie.back_end.repository.ProductRepository;
import ru.don_polesie.back_end.security.guest.GuestPrincipal;
import ru.don_polesie.back_end.security.guest.GuestTokenProvider;
import ru.don_polesie.back_end.service.OrderService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final GuestTokenProvider guestTokenProvider;

    @Override
    public Page<OrderDtoRR> findOrdersPage(Integer pageNumber) {
        var pageable =
                PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage
                .map(orderMapper::toOrderDtoRR);
    }

    @Override
    @Transactional
    public OrderCreateResponse save(OrderDtoRR orderDtoRR) {
        // 1) берём guestId из SecurityContext, если гость уже аутентифицирован
        String guestId = extractGuestIdOrNew();

        // 2) маппим заказ, проставляем guestId и статус
        var order = orderRepository.save(orderMapper.toOrder(orderDtoRR));
        order.setGuestId(guestId);



        // 3) добавляем позиции
        orderDtoRR.getItems().forEach(item -> {
            var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ObjectNotFoundException("product not found"));
            var op = new OrderProduct(order, product, item.getQuantity());
            order.addProduct(op);
        });


        // 4) переиздаём гостевой токен (обновляем TTL) и возвращаем в ответе
        String guestToken = guestTokenProvider.createToken(guestId);

        return new OrderCreateResponse(orderMapper.toOrderDtoRR(order), guestToken);
    }

    private String extractGuestIdOrNew() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof GuestPrincipal gp) {
            return gp.getGuestId();
        }
        return UUID.randomUUID().toString();
    }

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
                    Double.parseDouble(orderProduct.getQuantity().split(" ")[0]) / 1000);
        } else {
            productPrice = BigDecimal.valueOf(product.getPrice() *
                    Double.parseDouble(orderProduct.getQuantity().split(" ")[0]));
        }


        order.setTotalAmount(order.getTotalAmount().subtract(productPrice));

        order.removeProduct(orderProduct);
    }

    @Override
    @Transactional
    public void markShipped(Long id) {
        var order = orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(""));
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    @Override
    public OrderDtoRR findById(Long id) {
        return orderMapper
                .toOrderDtoRR(orderRepository
                        .findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException(""))
                );
    }

    @Override
    @Transactional
    public void processOrder(Long id, ProcessWeightsRequest req) {
        var order = orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ не найден"));


        Map<Long, ProcessWeightsRequest.WeightDto> weightMap = req.getWeights().stream()
                .collect(Collectors.toMap(ProcessWeightsRequest.WeightDto::getProductId, w -> w));

        var additional = BigDecimal.ZERO;

        for (OrderProduct op : order.getOrderProducts()) {
            var product = op.getProduct();
            if (Boolean.TRUE.equals(product.getIsWeighted())) {
                ProcessWeightsRequest.WeightDto w = weightMap.get(product.getId());
                if (w == null) {
                    throw new IllegalArgumentException("Не передан вес для взвешиваемого товара: " + product.getId());
                }
                double weightGrams = w.getWeight();

                op.setQuantity(w.getWeight() + " г.");

                double pricePerKg = product.getPrice();
                double cost = (weightGrams / 1000) * pricePerKg;


                additional = additional.add(BigDecimal.valueOf(cost));
            }
        }

        order.setTotalAmount(order.getTotalAmount().add(additional));
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);
    }
}
