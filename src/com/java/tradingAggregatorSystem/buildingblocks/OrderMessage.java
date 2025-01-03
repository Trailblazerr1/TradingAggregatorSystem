package com.java.tradingAggregatorSystem.buildingblocks;

import java.math.BigDecimal;

public class OrderMessage {
    private final MarketSide side;
    private final BigDecimal price;
    private final long quantity;

    public OrderMessage(MarketSide side, BigDecimal price, long quantity) {
        this.side = side;
        this.price = price;
        this.quantity = quantity;
    }

    public MarketSide getSide() {
        return side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
