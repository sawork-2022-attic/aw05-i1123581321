package com.micropos.cart.rest;

import com.micropos.cart.api.CartApi;
import com.micropos.cart.dto.ItemDto;
import com.micropos.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class CartController implements CartApi {

    private final CartService service;

    @Autowired
    public CartController(CartService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<List<ItemDto>> listCart() {
        return ResponseEntity.ok(service.content());
    }

    @Override
    public ResponseEntity<List<ItemDto>> addItem(ItemDto itemDto) {
        service.modifyCart(itemDto.getId(), itemDto.getNumber());
        return ResponseEntity.ok(service.content());
    }

    @Override
    public ResponseEntity<Void> removeItem(String productId) {
        service.removeItem(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
