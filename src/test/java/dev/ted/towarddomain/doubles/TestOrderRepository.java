package dev.ted.towarddomain.doubles;

import dev.ted.towarddomain.repository.OrderRepository;
import dev.ted.towarddomain.domain.Order;

import java.util.ArrayList;
import java.util.List;

public class TestOrderRepository implements OrderRepository {
    private Order insertedOrder;
    private List<Order> orders = new ArrayList<>();

    public Order getSavedOrder() {
        return insertedOrder;
    }

    public void save(Order order) {
        this.insertedOrder = order;
    }

    @Override
    public Order getById(int orderId) {
        return orders.stream()
                     .filter(order -> order.getId() == orderId)
                     .findFirst()
                     .get();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
