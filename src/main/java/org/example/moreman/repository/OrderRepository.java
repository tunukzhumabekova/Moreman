package org.example.moreman.repository;

import com.agro.public_.tables.Orders;
import com.agro.public_.tables.records.OrdersRecord;
import org.example.moreman.model.request.OrderRecord;
import org.example.moreman.model.response.OrderResponse;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final DSLContext dslContext;

    public OrderRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<OrderResponse> createOrders(List<OrderRecord> orders) {
        return orders.stream()
                .map(order -> {
                    OrdersRecord record = dslContext.insertInto(Orders.ORDERS)
                            .set(Orders.ORDERS.PRODUCT_ID, order.productId())
                            .set(Orders.ORDERS.QUANTITY, order.quantity())
                            .set(Orders.ORDERS.DATE, LocalDateTime.now())
                            .returning()
                            .fetchOne();

                    if (record == null) {
                        throw new RuntimeException("Failed to create order");
                    }

                    return new OrderResponse(
                            record.getId(),
                            record.getProductId(),
                            record.getQuantity(),
                            record.getDate()
                    );
                })
                .collect(Collectors.toList());
    }
    public List<OrderResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        List<OrderResponse> ordersRecords = dslContext.selectFrom(Orders.ORDERS)
                .where(Orders.ORDERS.DATE.between(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()))
                .fetchInto(OrderResponse.class);

        return ordersRecords.stream()
                .map(record -> new OrderResponse(
                        record.orderId(),
                        record.productId(),
                        record.quantity(),
                        record.localDateTime()
                ))
                .collect(Collectors.toList());
    }
}
