package org.example.moreman.repository;

import com.agro.public_.tables.Orders;
import com.agro.public_.tables.records.OrdersRecord;
import org.example.moreman.model.request.OrderRecord;
import org.example.moreman.model.response.OrderResponse;
import org.example.moreman.model.response.OrderResponseToGet;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.agro.public_.Tables.ORDERS;
import static com.agro.public_.Tables.PRODUCTS;

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

    public List<OrderResponseToGet> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        List<OrderResponseToGet> ordersRecords = dslContext.select(
                        ORDERS.ID.as("orderId"),
                        PRODUCTS.NAME.as("productName"),
                        PRODUCTS.PRICE,
                        ORDERS.QUANTITY,
                        PRODUCTS.DESCRIPTION,
                        ORDERS.DATE.as("localDateTime")
                )
                .from(ORDERS)
                .join(PRODUCTS).on(ORDERS.PRODUCT_ID.eq(PRODUCTS.ID))
                .where(ORDERS.DATE.between(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()))
                .fetchInto(OrderResponseToGet.class);

        return ordersRecords.stream()
                .map(record -> new OrderResponseToGet(
                        record.orderId(),
                        record.productName(),
                        record.price(),
                        record.quantity(),
                        record.description(),
                        record.localDateTime()
                ))
                .collect(Collectors.toList());
    }
    public void deleteOrderById(Integer orderId) {
        dslContext.deleteFrom(ORDERS).where(ORDERS.ID.eq(orderId)).execute();
    }

    public void deleteAllOrders(){
        dslContext.deleteFrom(ORDERS).execute();
    }
}
