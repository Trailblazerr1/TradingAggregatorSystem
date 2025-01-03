package com.java.tradingAggregatorSystem.utils;

import com.java.tradingAggregatorSystem.buildingblocks.MarketSide;
import com.java.tradingAggregatorSystem.buildingblocks.PriceBook;
import com.java.tradingAggregatorSystem.buildingblocks.CustomPriceLevel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.logging.Logger;

public class PriceBookUtility {
    private static final Logger LOGGER = Logger.getLogger(PriceBookUtility.class.getName());
    PriceBook priceBook;

    public PriceBookUtility(PriceBook priceBook) {
        this.priceBook = priceBook;
    }

    public Long getTotalQuantityForPriceAndSide(BigDecimal price, MarketSide side) {
        if(price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid price given in parameter");
        if(side == MarketSide.BUY)
            return priceBook.getBidPriceToTotalQuantity().getOrDefault(price,0L);
        else if(side == MarketSide.SELL)
            return priceBook.getOfferPriceToTotalQuantity().getOrDefault(price,0L);
        else
            throw new IllegalArgumentException("Invalid side given in parameter");
    }

    public BigDecimal getVwapForQuantityAndSide(Long quantity, MarketSide side) {
        Set<CustomPriceLevel> priceLevelSet = side == MarketSide.BUY ? priceBook.getBuyCPriceLevelSet():
                priceBook.getSellCPriceLevelSet();
        long remainingQuantity =  quantity;
        BigDecimal weightedSum = BigDecimal.ZERO;
        long currentQuantity = 0L;

        for(CustomPriceLevel priceLevel : priceLevelSet) {
            long quantityToUse = Math.min(priceLevel.getQuantity(),remainingQuantity);
            if(quantityToUse <=0 ) break;
            weightedSum = weightedSum.add(
                    priceLevel.getPrice().multiply(BigDecimal.valueOf(quantityToUse)));
            currentQuantity+= quantityToUse;
            remainingQuantity-= quantityToUse;
        }
        if(currentQuantity < quantity)
            throw new IllegalArgumentException("Illiquid quantity provided in parameter");
        return weightedSum.divide(BigDecimal.valueOf(currentQuantity), RoundingMode.HALF_UP);

    }
}
