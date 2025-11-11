package ru.don_polesie.back_end.controller.product.publish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.dto.product.ProductDtoSearch;
import ru.don_polesie.back_end.service.product.WorkerProductService;


@Tag(
        name = "Каталог товаров",
        description = "Публичный API для поиска и просмотра товаров"
)
@RequestMapping("/api/product")
@RestController
@RequiredArgsConstructor
public class ProductSearchController {

    private final WorkerProductService productServiceImpl;

    @Operation(
            summary = "Получить товары с пагинацией",
            description = "Возвращает страницу с товарами, отсортированными по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список товаров успешно получен")
    })
    @GetMapping
    public ResponseEntity<Page<ProductDtoRR>> findProductsPage(@RequestParam @Min(value = 0) Integer pageNumber) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(productServiceImpl.findProductsPage(pageNumber));
    }

    @Operation(
            summary = "Найти товар по ID",
            description = "Возвращает полную информацию о товаре по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар найден"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDtoRR> findById(@PathVariable @Min(value = 1) Long id) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(productServiceImpl.findById(id));
    }

    @Operation(
            summary = "Расширенный поиск товаров",
            description = "Поиск товаров по различным фильтрам с пагинацией. Все параметры необязательные."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Поиск выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры фильтрации")
    })
    @GetMapping("/find")
    public ResponseEntity<Page<ProductDtoRR>> findAllByParams(@RequestParam(required = false) Long id,
                                                              @RequestParam(required = false) String brand,
                                                              @RequestParam(required = false) String name,
                                                              @RequestParam(required = false) Integer pageNumber) {
        ProductDtoSearch productDtoSearch = ProductDtoSearch
                .builder()
                .id(id)
                .brand(brand)
                .name(name)
                .build();

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(productServiceImpl.findAllByParams(productDtoSearch, pageNumber));
    }

    @Operation(
            summary = "Поиск товаров по текстовому запросу",
            description = "Полнотекстовый поиск товаров по названию, бренду и другим текстовым полям"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Поиск выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Пустой или неверный поисковый запрос")
    })
    @GetMapping("/find/query")
    public ResponseEntity<Page<ProductDtoRR>> findProductsByQuery(@RequestParam String query,
                                                                  @RequestParam Integer pageNumber) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(productServiceImpl.findProductByQuery(query, pageNumber));
    }

}
