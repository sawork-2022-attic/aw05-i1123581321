package com.micropos.cart.service;

import com.micropos.cart.dto.ItemDto;

import java.util.List;

public interface CartService {
    List<ItemDto> content();
    void modifyCart(String id, int amount);
    void removeItem(String id);
}
