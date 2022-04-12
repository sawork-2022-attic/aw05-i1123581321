package com.micropos.cart.service;

import com.micropos.cart.dto.ItemDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private final Map<String, Integer> items = new HashMap<>();


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
