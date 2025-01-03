package com.java.tradingAggregatorSystem;

import com.java.tradingAggregatorSystem.constants.MarketSide;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class PriceBookUtility {
    PriceBook priceBook;

    public PriceBookUtility(PriceBook priceBook) {
        this.priceBook = priceBook;
    }

    public Long getTotalQuantityForPriceAndSide(BigDecimal price, MarketSide side) {
        if(price.compareTo(BigDecimal.valueOf(0)) <= 0)
            throw new IllegalArgumentException("Invalid price given in parameter");
        if(side == MarketSide.BUY)
            return priceBook.getBidPriceToTotalQuantity().getOrDefault(price,0L);
        else if(side == MarketSide.SELL)
            return priceBook.getOfferPriceToTotalQuantity().getOrDefault(price,0L);
        else
            throw new IllegalArgumentException("Invalid side given in parameter");
    }

    public BigDecimal getVwapForQuantityAndSide(Long quantity, MarketSide side) {
        Set<PriceLevel> priceLevelSet = side == MarketSide.BUY ? priceBook.getBuyPriceLevelSet():
                priceBook.getSellPriceLevelSet();
        long remainingQuantity =  quantity;
        BigDecimal weightedSum = BigDecimal.ZERO;
        long currentQuantity = 0L;

        for(PriceLevel priceLevel : priceLevelSet) {
//            long priceLevelQuantity = priceLevel.getQuantity();
            long quantityToUse = Math.min(priceLevel.getQuantity(),remainingQuantity);
            if(quantityToUse <=0 ) break;
            weightedSum = weightedSum.add(
                    priceLevel.getPrice().multiply(BigDecimal.valueOf(quantityToUse)));
            currentQuantity+= quantityToUse;
            remainingQuantity-= quantityToUse;
        }
        if(currentQuantity < quantity)
            throw new IllegalArgumentException("Illiquid quantity");
        //make rounding mode configurable
        return weightedSum.divide(BigDecimal.valueOf(currentQuantity), RoundingMode.HALF_UP);

    }
}
