package org.example.moreman.service.impl;

import org.example.moreman.model.request.OrderRecord;
import org.example.moreman.model.response.OrderResponse;
import org.example.moreman.repository.OrderRepository;
import org.example.moreman.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderResponse> createOrder(List<OrderRecord> orderRecord) {
        return orderRepository.createOrders(orderRecord);
    }
}
