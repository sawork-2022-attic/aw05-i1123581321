package com.micropos.products.repository;


import com.micropos.products.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> allProducts();

    Optional<Product> findProduct(String productId);

}