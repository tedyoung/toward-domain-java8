package dev.ted.towarddomain.usecase;

import dev.ted.towarddomain.repository.OrderRepository;
import dev.ted.towarddomain.domain.Order;
import dev.ted.towarddomain.domain.OrderStatus;
import dev.ted.towarddomain.service.ShipmentService;

import static dev.ted.towarddomain.domain.OrderStatus.CREATED;
import static dev.ted.towarddomain.domain.OrderStatus.REJECTED;
import static dev.ted.towarddomain.domain.OrderStatus.SHIPPED;

public class OrderShipmentUseCase {
    private final OrderRepository orderRepository;
    private final ShipmentService shipmentService;

    public OrderShipmentUseCase(OrderRepository orderRepository, ShipmentService shipmentService) {
        this.orderRepository = orderRepository;
        this.shipmentService = shipmentService;
    }

    public void run(OrderShipmentRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        if (order.getStatus().equals(CREATED) || order.getStatus().equals(REJECTED)) {
            throw new OrderCannotBeShippedException();
        }

        if (order.getStatus().equals(SHIPPED)) {
            throw new OrderCannotBeShippedTwiceException();
        }

        shipmentService.ship(order);

        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }
}
