package ru.don_polesie.back_end.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.admin.RevenueDtoResponse;
import ru.don_polesie.back_end.dto.order.response.PopularProductDtoResponse;
import ru.don_polesie.back_end.service.admin.AdminStatService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminStatController {

    private final AdminStatService adminStatService;

    @Operation(
            summary = "Дневная выручка"
    )
    @GetMapping("/daily-revenue")
    public ResponseEntity<RevenueDtoResponse> dailyRevenue(@RequestParam @Min(2025) int year,
                                                           @RequestParam @Min(1) @Max(12) int month,
                                                           @RequestParam @Min(1) @Max(31) int day) {
        RevenueDtoResponse revenue = adminStatService.getDailyRevenue(year, month, day);
        return ResponseEntity.ok(revenue);
    }

    @Operation(
            summary = "Месячная выручка"
    )
    @GetMapping("/monthly-revenue")
    public ResponseEntity<RevenueDtoResponse> monthlyRevenue(@RequestParam @Min(2025) int year,
                                                             @RequestParam @Min(1) @Max(12) int month) {
        RevenueDtoResponse revenue = adminStatService.getMonthlyRevenue(year, month);
        return ResponseEntity.ok(revenue);
    }

    @Operation(
            summary = "Товары по популярности"
    )
    @GetMapping("/most-popular-products")
    public ResponseEntity<Page<PopularProductDtoResponse>> mostPopularProducts(@RequestParam @Min(2025) int year,
                                                                               @RequestParam @Min(1) @Max(12) int month,
                                                                               @RequestParam int pageNumber) {
        Page<PopularProductDtoResponse> products = adminStatService.getMostPopularProducts(year, month, pageNumber);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Число заказов за день"
    )
    @GetMapping("/order-count/per-day")
    public ResponseEntity<Long> orderCountPerDay(@RequestParam @Min(2025) int year,
                                                 @RequestParam @Min(1) @Max(12) int month,
                                                 @RequestParam @Min(1) @Max(31) int day) {
        Long count = adminStatService.getOrderCountPerDay(year, month, day);
        return ResponseEntity.ok(count);
    }

    @Operation(
            summary = "Число заказов за месяц"
    )
    @GetMapping("/order-count/per-month")
    public ResponseEntity<Long> orderCountPerMonth(@RequestParam @Min(2025) int year,
                                                   @RequestParam @Min(1) @Max(12) int month) {
        Long count = adminStatService.getOrderCountPerMonth(year, month);
        return ResponseEntity.ok(count);
    }
}
