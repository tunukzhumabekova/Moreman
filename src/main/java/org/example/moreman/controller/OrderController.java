package org.example.moreman.controller;

import com.agro.public_.tables.records.OrdersRecord;
import org.example.moreman.model.request.OrderRecord;
import org.example.moreman.model.response.OrderResponse;
import org.example.moreman.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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


}
