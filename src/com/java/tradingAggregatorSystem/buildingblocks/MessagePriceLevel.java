package com.java.tradingAggregatorSystem.buildingblocks;

import java.math.BigDecimal;

//Immutable, no concurrency
public class MessagePriceLevel extends PriceLevel{
    private final MarketSide side;

    public MessagePriceLevel(MarketSide side, BigDecimal price, long quantity) {
        super(price,quantity);
        this.side = side;
    }

    public MarketSide getSide() {
        return side;
    }
}
