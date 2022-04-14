package com.micropos.cart.service;

import com.micropos.cart.dto.ItemDto;
import com.micropos.cart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private static final String PRODUCT = "http://product-service/api/products";
    private static final ParameterizedTypeReference<List<Product>> type = new ParameterizedTypeReference<>() {
    };
    private static final List<Product> fallback = new ArrayList<>();

    static {
        fallback.add(new Product("12991976", "Java 11官方 参考手册（第11版） Java语言专家、畅销书作者HerbertSchildt新书",
                188.1, "https://img12.360buyimg.com/n1/s200x200_jfs/t1/103759/9/19668/133910/5ea007f7E82d73d22/c7c36a4e35d9e3b4.jpg"));
        fallback.add(new Product("13543582", "Java高并发与集合框架：JCF和JUC源码分析与实现(博文视点出品) 掌握Java集合框架和Java并发工具包",
                117.8, "https://img12.360buyimg.com/n1/s200x200_jfs/t1/208611/22/12064/282488/61b22ce7E08894c60/be5539046b79c2d4.jpg"));
    }


    private final Map<String, Integer> items = new HashMap<>();

    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory<?, ?> factory;

    @Autowired
    public CartServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory<?, ?> factory) {
        this.restTemplate = restTemplate;
        this.factory = factory;
    }

    private List<Product> getProduct(){
//        return restTemplate.exchange(PRODUCT, HttpMethod.GET, null, type).getBody();
        var cb = factory.create("product");
        return cb.run(() -> restTemplate.exchange(PRODUCT, HttpMethod.GET, null, type).getBody(), t ->fallback);
    }


    @Override
    public List<ItemDto> content() {
        var result = new ArrayList<ItemDto>();
        for (var e: items.entrySet()){
            var item = new ItemDto();
            item.setId(e.getKey());
            item.setNumber(e.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public void modifyCart(String id, int amount) {
        if (getProduct().stream().noneMatch(p -> p.getId().equals(id))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PRODUCT ID NOT VALID!");
        }
        items.merge(id, amount, Integer::sum);
        if (items.get(id) <= 0){
            items.remove(id);
        }
    }

    @Override
    public void removeItem(String id) {
        items.remove(id);
    }
}
