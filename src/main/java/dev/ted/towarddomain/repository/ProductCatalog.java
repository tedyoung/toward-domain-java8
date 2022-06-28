package dev.ted.towarddomain.repository;

import dev.ted.towarddomain.domain.Product;

public interface ProductCatalog {
    Product getByName(String name);
}
