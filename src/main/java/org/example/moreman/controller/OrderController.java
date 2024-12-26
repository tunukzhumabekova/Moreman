package org.example.moreman.controller;

import org.example.moreman.model.request.OrderRecord;
import org.example.moreman.model.response.OrderResponse;
import org.example.moreman.model.response.OrderResponseToGet;
import org.example.moreman.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a new order
    @PostMapping("/createOrder")
    public ResponseEntity<List<OrderResponse>> createOrder(@RequestBody List<OrderRecord> orderRecord) {
        List<OrderResponse> createdOrder = orderService.createOrder(orderRecord);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseToGet>> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate); // Parse the start date
        LocalDate end = LocalDate.parse(endDate);     // Parse the end date
        List<OrderResponseToGet> orders = orderService.getOrdersByDateRange(start, end);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/deleteById")
    public void deleteById(@RequestParam Integer id) {
        orderService.deleteOrderById(id);
    }

    @DeleteMapping("/deleteAll")
   public void deleteAll() {
        orderService.deleteAllOrders();
    }
}
