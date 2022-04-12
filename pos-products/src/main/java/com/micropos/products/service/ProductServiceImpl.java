package com.micropos.products.service;

import com.micropos.products.model.Product;
import com.micropos.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Random random = new Random();


    @Autowired
    public ProductServiceImpl( ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> products() {
        return productRepository.allProducts();
    }

    @Override
    public Optional<Product> getProduct(String id) {
        return productRepository.findProduct(id);
    }

    @Override
    public Product randomProduct() {
        var list = products();
        return list.get(random.nextInt(list.size()));
    }
}
