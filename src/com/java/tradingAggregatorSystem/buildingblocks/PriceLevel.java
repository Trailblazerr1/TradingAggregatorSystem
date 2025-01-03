package com.java.tradingAggregatorSystem.buildingblocks;

import com.java.tradingAggregatorSystem.exceptions.InvalidMarketDataException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class PriceLevel {
    private final BigDecimal price;
    private final long quantity;

    public PriceLevel(BigDecimal price, long quantity) throws InvalidMarketDataException {
        if(price.compareTo(BigDecimal.ZERO) >= 0)
            this.price = price;
        else
            throw new InvalidMarketDataException("Invalid price found in Market Data at "+ LocalDateTime.now());

        if(quantity >= 0)
            this.quantity = quantity;
        else
            throw new InvalidMarketDataException("Invalid quantity found in Market Data at "+ LocalDateTime.now());
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
