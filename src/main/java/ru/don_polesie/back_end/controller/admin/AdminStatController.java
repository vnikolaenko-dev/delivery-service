package ru.don_polesie.back_end.controller.admin;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.admin.RevenueDto;
import ru.don_polesie.back_end.dto.order.PopularProductDto;
import ru.don_polesie.back_end.service.admin.AdminStatService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminStatController {

    private final AdminStatService adminStatService;

    @GetMapping("/daily-revenue")
    public ResponseEntity<RevenueDto> dailyRevenue(@RequestParam @Min(2025) int year,
                                                   @RequestParam @Min(1) @Max(12) int month,
                                                   @RequestParam @Min(1) @Max(31) int day) {
        RevenueDto revenue = adminStatService.getDailyRevenue(year, month, day);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<RevenueDto> monthlyRevenue(@RequestParam @Min(2025) int year,
                                                     @RequestParam @Min(1) @Max(12) int month) {
        RevenueDto revenue = adminStatService.getMonthlyRevenue(year, month);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/most-popular-products")
    public ResponseEntity<Page<PopularProductDto>> mostPopularProducts(@RequestParam @Min(2025) int year,
                                                                       @RequestParam @Min(1) @Max(12) int month,
                                                                       @RequestParam int pageNumber) {
        Page<PopularProductDto> products = adminStatService.getMostPopularProducts(year, month, pageNumber);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/order-count/per-day")
    public ResponseEntity<Long> orderCountPerDay(@RequestParam @Min(2025) int year,
                                                 @RequestParam @Min(1) @Max(12) int month,
                                                 @RequestParam @Min(1) @Max(31) int day) {
        Long count = adminStatService.getOrderCountPerDay(year, month, day);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/order-count/per-month")
    public ResponseEntity<Long> orderCountPerMonth(@RequestParam @Min(2025) int year,
                                                   @RequestParam @Min(1) @Max(12) int month) {
        Long count = adminStatService.getOrderCountPerMonth(year, month);
        return ResponseEntity.ok(count);
    }
}
