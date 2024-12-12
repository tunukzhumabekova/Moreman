package org.example.moreman.service;

import org.example.moreman.model.request.OrderRecord;
import org.example.moreman.model.response.OrderResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
     List<OrderResponse> createOrder(List<OrderRecord> orderRecord) ;
     List<OrderResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) ;

    }
