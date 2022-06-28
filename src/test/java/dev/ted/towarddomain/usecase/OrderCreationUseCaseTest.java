package dev.ted.towarddomain.usecase;

import dev.ted.towarddomain.domain.Category;
import dev.ted.towarddomain.domain.Order;
import dev.ted.towarddomain.domain.OrderStatus;
import dev.ted.towarddomain.domain.Product;
import dev.ted.towarddomain.doubles.InMemoryProductCatalog;
import dev.ted.towarddomain.doubles.TestOrderRepository;
import dev.ted.towarddomain.repository.ProductCatalog;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class OrderCreationUseCaseTest {

    @Test
    public void sellMultipleItems() throws Exception {
        TestOrderRepository orderRepository = new TestOrderRepository();
        ProductCatalog productCatalog = createProductCatalogWithSaladAndTomato();
        OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

        SellItemRequest saladRequest = new SellItemRequest();
        saladRequest.setProductName("salad");
        saladRequest.setQuantity(2);

        SellItemRequest tomatoRequest = new SellItemRequest();
        tomatoRequest.setProductName("tomato");
        tomatoRequest.setQuantity(3);

        final SellItemsRequest request = new SellItemsRequest();
        request.setRequests(new ArrayList<>());
        request.getRequests().add(saladRequest);
        request.getRequests().add(tomatoRequest);

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();
        assertThat(insertedOrder.getStatus())
            .isEqualByComparingTo(OrderStatus.CREATED);
        assertThat(insertedOrder.getTotal())
            .isEqualByComparingTo(new BigDecimal("23.20"));
        assertThat(insertedOrder.getTax())
            .isEqualByComparingTo(new BigDecimal("2.13"));
        assertThat(insertedOrder.getCurrency())
            .isEqualTo("USD");
        assertThat(insertedOrder.getItems())
            .hasSize(2);
        assertThat(insertedOrder.getItems().get(0).getProduct().getName())
            .isEqualTo(("salad"));
        assertThat(insertedOrder.getItems().get(0).getProduct().getPrice())
            .isEqualByComparingTo(new BigDecimal("3.56"));
        assertThat(insertedOrder.getItems().get(0).getQuantity())
            .isEqualTo((2));
        assertThat(insertedOrder.getItems().get(0).getTaxedAmount())
            .isEqualByComparingTo(new BigDecimal("7.84"));
        assertThat(insertedOrder.getItems().get(0).getTax())
            .isEqualByComparingTo(new BigDecimal("0.72"));
        assertThat(insertedOrder.getItems().get(1).getProduct().getName())
            .isEqualTo(("tomato"));
        assertThat(insertedOrder.getItems().get(1).getProduct().getPrice())
            .isEqualByComparingTo(new BigDecimal("4.65"));
        assertThat(insertedOrder.getItems().get(1).getQuantity())
            .isEqualTo((3));
        assertThat(insertedOrder.getItems().get(1).getTaxedAmount())
            .isEqualByComparingTo(new BigDecimal("15.36"));
        assertThat(insertedOrder.getItems().get(1).getTax())
            .isEqualByComparingTo(new BigDecimal("1.41"));
    }

    private ProductCatalog createProductCatalogWithSaladAndTomato() {
        Category food = new Category();
        food.setName("food");
        food.setTaxPercentage(new BigDecimal("10"));

        List<Product> products = new ArrayList<>();
        Product salad = new Product();
        salad.setName("salad");
        salad.setPrice(new BigDecimal("3.56"));
        salad.setCategory(food);
        products.add(salad);

        Product tomato = new Product();
        tomato.setName("tomato");
        tomato.setPrice(new BigDecimal("4.65"));
        tomato.setCategory(food);
        products.add(tomato);

        return new InMemoryProductCatalog(products);
    }

    @Test
    public void unknownProduct() throws Exception {
        TestOrderRepository orderRepository = new TestOrderRepository();
        ProductCatalog productCatalog = createProductCatalogWithSaladAndTomato();
        OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

        SellItemsRequest request = new SellItemsRequest();
        request.setRequests(new ArrayList<>());
        SellItemRequest unknownProductRequest = new SellItemRequest();
        unknownProductRequest.setProductName("unknown product");
        request.getRequests().add(unknownProductRequest);

        assertThatThrownBy(() -> {
            useCase.run(request);
        }).isExactlyInstanceOf(UnknownProductException.class);
    }
}
