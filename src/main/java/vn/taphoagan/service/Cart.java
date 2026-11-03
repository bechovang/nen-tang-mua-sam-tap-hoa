package vn.taphoagan.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cart implements Serializable {

    private Long storeId;
    private String storeName;
    private String storeSlug;
    private List<Item> items = new ArrayList<>();

    public long subtotal() {
        return items.stream().mapToLong(i -> i.getPrice() * i.getQuantity()).sum();
    }

    public int count() {
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        storeId = null;
        storeName = null;
        storeSlug = null;
        items.clear();
    }

    @Getter
    @Setter
    public static class Item implements Serializable {
        private Long productId;
        private String name;
        private String unit;
        private long price;
        private int quantity;
    }
}
