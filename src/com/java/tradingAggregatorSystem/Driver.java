package com.java.tradingAggregatorSystem;

import com.java.tradingAggregatorSystem.constants.MarketSide;

import java.math.BigDecimal;
import java.util.List;

public class Driver {
    public static void main(String[] args) {
        System.out.println("Driver");
        OrderMessage orderMessageBuy = new OrderMessage(MarketSide.BUY,new BigDecimal(80),10);
        OrderMessage orderMessageSell = new OrderMessage(MarketSide.SELL,new BigDecimal(80),10);
        MarketData marketData = new MarketData("LP1","USDINR",List.of(orderMessageBuy), List.of(orderMessageBuy));
        PriceBook priceBook = new PriceBook();
        priceBook.update(marketData);
        PriceBookUtility priceBookUtility = new PriceBookUtility(priceBook);
        System.out.println("Val1 is" + priceBookUtility.getTotalQuantityForPriceAndSide(new BigDecimal(80),MarketSide.BUY));
        //System.out.println(priceBookUtility.getVwapForQuantityAndSide(10L,MarketSide.BUY));
    }
}
