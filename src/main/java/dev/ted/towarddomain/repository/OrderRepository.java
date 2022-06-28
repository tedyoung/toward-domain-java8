package dev.ted.towarddomain.repository;

import dev.ted.towarddomain.domain.Order;

public interface OrderRepository {
    void save(Order order);

    Order getById(int orderId);
}
