package dev.ted.towarddomain.doubles;

import dev.ted.towarddomain.domain.Order;
import dev.ted.towarddomain.service.ShipmentService;

public class TestShipmentService implements ShipmentService {
    private Order shippedOrder = null;

    public Order getShippedOrder() {
        return shippedOrder;
    }

    @Override
    public void ship(Order order) {
        this.shippedOrder = order;
    }
}
