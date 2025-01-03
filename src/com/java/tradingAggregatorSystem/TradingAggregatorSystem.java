package com.java.tradingAggregatorSystem;

import com.java.tradingAggregatorSystem.buildingblocks.MarketSide;
import com.java.tradingAggregatorSystem.buildingblocks.MarketData;
import com.java.tradingAggregatorSystem.buildingblocks.OrderMessage;
import com.java.tradingAggregatorSystem.buildingblocks.PriceBook;
import com.java.tradingAggregatorSystem.utils.PriceBookUtility;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradingAggregatorSystem {
    private static Logger LOGGER = Logger.getLogger(TradingAggregatorSystem.class.getName());

    public static void main(String[] args) {
        System.out.println("Driver");
        OrderMessage orderMessageBuy = new OrderMessage(MarketSide.BUY,new BigDecimal(80),10);
        OrderMessage orderMessageSell = new OrderMessage(MarketSide.SELL,new BigDecimal(80),10);
        MarketData marketData = new MarketData("LP1","USDINR",List.of(orderMessageBuy), List.of(orderMessageBuy));
       //validation of correct pricebook
        PriceBook priceBookUSDINR = new PriceBook("USDINR");
        priceBookUSDINR.update(marketData);
        PriceBookUtility priceBookUtility = new PriceBookUtility(priceBookUSDINR);
        LOGGER.log(Level.FINE,"Val is {0}" , priceBookUtility.getTotalQuantityForPriceAndSide(new BigDecimal(80),MarketSide.BUY));
        //System.out.println(priceBookUtility.getVwapForQuantityAndSide(10L,MarketSide.BUY));
    }
}
