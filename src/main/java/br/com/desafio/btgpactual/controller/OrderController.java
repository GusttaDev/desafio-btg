package br.com.desafio.btgpactual.controller;

import br.com.desafio.btgpactual.model.Order;
import br.com.desafio.btgpactual.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderCode}/totalValue")
    @Operation(summary = "Obtém o valor total de um pedido")
    public ResponseEntity<BigDecimal> getTotalOrderValue(@PathVariable Long orderCode) {
        return ResponseEntity.ok(orderService.searchTotalOrderValue(orderCode));
    }

    @GetMapping("/customer/{customerCode}/quantityOrder")
    @Operation(summary = "Obtém a quantitdade de pedidos por cliente")
    public ResponseEntity<Long> quantityOrderByCustomer(@PathVariable Long customerCode) {
        return ResponseEntity.ok(orderService.quantityOrderByCustomer(customerCode));
    }

    @GetMapping("/customer/{customerCode}/totalOrders")
    @Operation(summary = "Lista de todos os pedidos referente a um cliente")
    public ResponseEntity<List<Order>> getAllOrdersByCustomer(@PathVariable Long customerCode) {
        return ResponseEntity.ok(orderService.getAllOrdersByCustomer(customerCode));
    }

}
