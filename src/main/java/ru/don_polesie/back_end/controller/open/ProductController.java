package ru.don_polesie.back_end.controller.open;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;

@Tag(
        name = "Каталог товаров",
        description = "Публичный API для поиска и просмотра товаров"
)
@RequestMapping("/api/products")
public interface ProductController {

    @Operation(
            summary = "Получить товары с пагинацией",
            description = "Возвращает страницу с товарами, отсортированными по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список товаров успешно получен")
    })
    @GetMapping("")
    ResponseEntity<Page<ProductDtoRR>> findProductsPage(@RequestParam @Min(value = 1) Integer pageNumber);


    @Operation(
            summary = "Найти товар по ID",
            description = "Возвращает полную информацию о товаре по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар найден"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @GetMapping("/{id}")
    ResponseEntity<ProductDtoRR> findById(@PathVariable @Min(value = 1) Long id);


    @Operation(
            summary = "Расширенный поиск товаров",
            description = "Поиск товаров по различным фильтрам с пагинацией. Все параметры необязательные."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Поиск выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры фильтрации")
    })
    @GetMapping("/find-all-by-params")
    ResponseEntity<Page<ProductDtoRR>> findAllByParams(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer pageNumber);

    @Operation(
            summary = "Поиск товаров по текстовому запросу",
            description = "Полнотекстовый поиск товаров по названию, бренду и другим текстовым полям"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Поиск выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Пустой или неверный поисковый запрос")
    })
    @GetMapping("/find-products-by-query")
    ResponseEntity<Page<ProductDtoRR>> findProductsByQuery(@RequestParam String query,
                                           @RequestParam Integer pageNumber);
}
