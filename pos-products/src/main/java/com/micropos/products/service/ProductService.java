package com.micropos.products.service;

import com.micropos.products.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> products();

    Optional<Product> getProduct(String id);

    Product randomProduct();
}
