package dev.ted.towarddomain.service;

import dev.ted.towarddomain.domain.Order;

public interface ShipmentService {
    void ship(Order order);
}
