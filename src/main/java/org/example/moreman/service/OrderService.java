package org.example.moreman.service;

import org.example.moreman.model.request.OrderRecord;
import org.example.moreman.model.response.OrderResponse;

import java.util.List;

public interface OrderService {
     List<OrderResponse> createOrder(List<OrderRecord> orderRecord) ;

    }
