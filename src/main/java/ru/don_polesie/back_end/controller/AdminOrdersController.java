package ru.don_polesie.back_end.controller;


import io.swagger.v3.oas.annotations.Operation;
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
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;

@RequestMapping("/api/admin/orders")
public interface AdminOrdersController {

    @GetMapping("")
    ResponseEntity<Page<OrderDtoRR>> findOrdersPage(@RequestParam @Min(value = 1) Integer pageNumber);

    @PostMapping("/{id}/process")
    ResponseEntity<Void> processOrder(@PathVariable Long id,
                                      @RequestBody @Valid ProcessWeightsRequest req);

    @PutMapping("/{id}/ship")
    ResponseEntity<Void> markShipped(@PathVariable Long id);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteOrder(@PathVariable Long id);
}
