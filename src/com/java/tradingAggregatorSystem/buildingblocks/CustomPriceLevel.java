package com.java.tradingAggregatorSystem.buildingblocks;

import com.java.tradingAggregatorSystem.exceptions.InvalidMarketDataException;

import java.math.BigDecimal;

public class CustomPriceLevel extends PriceLevel {
    private final MarketSide side;
    private final String lpName;

    public CustomPriceLevel(MarketSide side, BigDecimal price, long quantity, String lpName) throws InvalidMarketDataException {
        super(price,quantity);
        this.side = side;
        this.lpName = lpName;
    }

    public MarketSide getSide() {
        return side;
    }

    public String getLpName() {
        return lpName;
    }
}
