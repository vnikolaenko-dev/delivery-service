package ru.don_polesie.back_end.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.RevenueDto;
import ru.don_polesie.back_end.dto.order.PopularProductDto;
import ru.don_polesie.back_end.repository.OrderProductRepository;
import ru.don_polesie.back_end.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminStatService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    private ZoneId zone = ZoneId.of("Europe/Moscow");


    public RevenueDto getDailyRevenue(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        Instant startOfDay = date.atStartOfDay(ZoneOffset.of("Europe/Moscow")).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();


        String period = String.format("%d-%02d-%02d", year, month, day);
        BigDecimal revenue = orderRepository.calculateRevenueByPeriod(startOfDay, endOfDay);
        return new RevenueDto(revenue != null ? revenue.longValue() : 0, period);
    }


    public RevenueDto getMonthlyRevenue(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        Instant startOfMonth = yearMonth.atDay(1).atStartOfDay(zone).toInstant();
        Instant endOfMonth = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay(zone).toInstant();

        String period = String.format("%d-%02d", year, month);
        BigDecimal revenue = orderRepository.calculateRevenueByPeriod(startOfMonth, endOfMonth);
        return new RevenueDto(revenue != null ? revenue.longValue() : 0, period);
    }


    public Page<PopularProductDto> getMostPopularProducts(int year, int month, int pageNumber) {
        YearMonth yearMonth = YearMonth.of(year, month);
        Instant startOfMonth = yearMonth.atDay(1).atStartOfDay(zone).toInstant();
        Instant endOfMonth = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay(zone).toInstant();

        Pageable pageable = PageRequest.of(pageNumber, 10);

        return orderProductRepository.findPopularProductsByPeriod(startOfMonth, endOfMonth, pageable);
    }


    public Long getOrderCountPerDay(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        Instant startOfDay = date.atStartOfDay(zone).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(zone).toInstant();

        return orderRepository.countByCreatedAtBetween(startOfDay, endOfDay);
    }


    public Long getOrderCountPerMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        Instant startOfMonth = yearMonth.atDay(1).atStartOfDay(zone).toInstant();
        Instant endOfMonth = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay(zone).toInstant();

        return orderRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
    }
}
