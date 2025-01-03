package com.java.tradingAggregatorSystem;

import com.java.tradingAggregatorSystem.constants.MarketSide;

import java.math.BigDecimal;

public class PriceLevel {
    private final MarketSide side;
    private final BigDecimal price;
    private final long quantity;
    private final String lpName;

    public PriceLevel(MarketSide side, BigDecimal price, long quantity, String lpName) {
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.lpName = lpName;
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

    public String getLpName() {
        return lpName;
    }
}
