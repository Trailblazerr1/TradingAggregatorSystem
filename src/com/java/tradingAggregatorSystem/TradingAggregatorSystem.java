package com.java.tradingAggregatorSystem;

import com.java.tradingAggregatorSystem.buildingblocks.MarketSide;
import com.java.tradingAggregatorSystem.buildingblocks.MarketData;
import com.java.tradingAggregatorSystem.buildingblocks.MessagePriceLevel;
import com.java.tradingAggregatorSystem.buildingblocks.PriceBook;
import com.java.tradingAggregatorSystem.exceptions.InvalidMarketDataException;
import com.java.tradingAggregatorSystem.utils.PriceBookUtility;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradingAggregatorSystem {
    private static Logger LOGGER = Logger.getLogger(TradingAggregatorSystem.class.getName());

    public static void main(String[] args) throws InvalidMarketDataException {
        LOGGER.info("Trading Aggregator System started");
        MessagePriceLevel messagePriceLevelBuy = new MessagePriceLevel(MarketSide.BUY,new BigDecimal(80),10);
        MessagePriceLevel messagePriceLevelSell = new MessagePriceLevel(MarketSide.SELL,new BigDecimal(80),10);
        MarketData marketData = new MarketData("LP1","USDINR",List.of(messagePriceLevelBuy), List.of(messagePriceLevelBuy));
        PriceBook priceBookUSDINR = new PriceBook("USDINR");
        priceBookUSDINR.update(marketData);
        PriceBookUtility priceBookUtility = new PriceBookUtility(priceBookUSDINR);
        LOGGER.info("Val of totalQuant "+ priceBookUtility.getTotalQuantityForPriceAndSide(new BigDecimal(80),MarketSide.BUY));
        LOGGER.info("Val of vwap " + priceBookUtility.getVwapForQuantityAndSide(10L,MarketSide.BUY));
    }
}
