package ru.don_polesie.back_end.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;

import java.util.List;

@RequestMapping("/api/products")
public interface ProductController {

    @Operation(
            summary = "Найти все товары постранично)",
            description = "Возвращает указанную страницу с товарами в базе данных, отсортированные по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Все товары успешно найдены",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = ProductDtoRR.class)))),
    })
    @GetMapping("")
    ResponseEntity<Page<ProductDtoRR>> findProductsPage(@RequestParam @Min(value = 1) Integer pageNumber);


    @PutMapping("/{id}")
    ResponseEntity<ProductDtoRR> update(@RequestBody @Valid ProductDtoRR productDtoRR,
                              @PathVariable @Min(value = 1) Long id);

    @PostMapping("")
    ResponseEntity<ProductDtoRR> save(@RequestBody @Valid ProductDtoRR productDtoRR);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable @Min(value = 1) Long id);

    @Operation(
            summary = "Поиск товара по ID",
            description = "Возвращает один товар по заданному ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар по заданному ID успешно найден",
                    content = @Content(schema = @Schema(implementation = ProductDtoRR.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<ProductDtoRR> findById(@PathVariable @Min(value = 1) Long id);


    @Operation(
            summary = "Поис товаров по фильтрам",
            description = "Возвращает список товаров по заданным фильтрам"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товары по заданным фильтрам успешно найдены",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = ProductDtoRR.class)))),
            @ApiResponse(responseCode = "400", description = "Фильтры указаны неверно",
                    content = @Content(schema = @Schema(implementation = HttpClientErrorException.BadRequest.class)))
    })
    @GetMapping("/find-all-by-params")
    ResponseEntity<List<ProductDtoRR>> findAllByParams(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String name);

    @Operation(
            summary = "Поис товаров по текстовому запросу",
            description = "Возвращает список товаров по заданному запросу"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товары по заданным фильтрам успешно найдены",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = ProductDtoRR.class)))),
            @ApiResponse(responseCode = "400", description = "Данные введены в неверном формате",
                    content = @Content(schema = @Schema(implementation = HttpClientErrorException.BadRequest.class)))
    })
    @GetMapping("/find-products-by-query")
    ResponseEntity<Page<ProductDtoRR>> findProductsByQuery(@RequestParam String query,
                                           @RequestParam Integer pageNumber);


}
