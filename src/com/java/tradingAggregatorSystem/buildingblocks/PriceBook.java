package com.java.tradingAggregatorSystem.buildingblocks;

import com.java.tradingAggregatorSystem.comparators.BuyComparator;
import com.java.tradingAggregatorSystem.comparators.SellComparator;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PriceBook {
    private static Logger LOGGER = Logger.getLogger(PriceBook.class.getName());

    private String priceBookInstruement;
    private Set<PriceLevel> buyPriceLevelSet;
    private Set<PriceLevel> sellPriceLevelSet ;
    private Map<String,Set<PriceLevel>> lpToBids;
    private Map<String,Set<PriceLevel>> lpToOffers;
    private Map<BigDecimal,Long> bidPriceToTotalQuantity;
    private Map<BigDecimal,Long> offerPriceToTotalQuantity;

    ReentrantLock lock;

    public PriceBook(String priceBookInstruement) {
        this.priceBookInstruement = priceBookInstruement;
        this.buyPriceLevelSet = new TreeSet<>(new BuyComparator());
        this.sellPriceLevelSet = new TreeSet<>(new SellComparator());
        this.lpToBids = new HashMap<>();
        this.lpToOffers = new HashMap<>();
        this.bidPriceToTotalQuantity = new HashMap<>();
        this.offerPriceToTotalQuantity = new HashMap<>();

        this.lock = new ReentrantLock();
    }

    public void reset() {
        try {
            lock.lock();
            //reset everything
            buyPriceLevelSet.clear();
            sellPriceLevelSet.clear();
            lpToBids.clear();
            lpToOffers.clear();
            bidPriceToTotalQuantity.clear();
            offerPriceToTotalQuantity.clear();
        } finally {
            lock.unlock();
        }
    }

    public void update(MarketData marketData) {
        try {
            lock.lock();
            //reset bids and Offers of marketData.getSource()
            removePreviousPriceLevelDataOfLP(marketData, lpToBids, buyPriceLevelSet, bidPriceToTotalQuantity);
            removePreviousPriceLevelDataOfLP(marketData, lpToOffers, sellPriceLevelSet, offerPriceToTotalQuantity);
            //add new data
            addNewPriceLevelData(marketData.getSource(), marketData.getBuyOrderMessageList(), MarketSide.BUY,
                    lpToBids, buyPriceLevelSet, bidPriceToTotalQuantity);
            addNewPriceLevelData(marketData.getSource(), marketData.getSellOrderMessageList(), MarketSide.SELL,
                    lpToOffers, sellPriceLevelSet, offerPriceToTotalQuantity);
        } finally {
            lock.unlock();
        }
    }


    private void addNewPriceLevelData(String source, List<OrderMessage> orderMessageList, MarketSide marketSide,
                                      Map<String, Set<PriceLevel>> lpToPriceLevel, Set<PriceLevel> priceLevelSet, Map<BigDecimal, Long> priceToTotalQuantity) {
        for(OrderMessage orderMessage : orderMessageList) {
            BigDecimal currentOrderMessagePrice = orderMessage.getPrice();
            PriceLevel priceLevel = new PriceLevel(marketSide,currentOrderMessagePrice,orderMessage.getQuantity(),source);
            priceLevelSet.add(priceLevel);
            priceToTotalQuantity.put(currentOrderMessagePrice, priceToTotalQuantity.getOrDefault(currentOrderMessagePrice, 0L)+ orderMessage.getQuantity());
            lpToPriceLevel.computeIfAbsent(source, key -> new HashSet<>()).add(priceLevel);
        }
    }

    private void removePreviousPriceLevelDataOfLP(MarketData marketData, Map<String, Set<PriceLevel>> lpToPriceLevel, Set<PriceLevel> priceLevelSet, Map<BigDecimal, Long> priceToTotalQuantity) {
        Set<PriceLevel> prevPriceLevelData = lpToPriceLevel.getOrDefault(marketData.getSource(), new HashSet<>());
        for(PriceLevel singleDataPoint : prevPriceLevelData) {
            priceLevelSet.remove(singleDataPoint);
            updatePriceToTotalQuantityMapOnRemoval(priceToTotalQuantity,singleDataPoint);
        }
        lpToPriceLevel.remove(marketData.getSource());
    }

    private void updatePriceToTotalQuantityMapOnRemoval(Map<BigDecimal, Long> priceToTotalQuantity, PriceLevel singleDataPoint) {
        BigDecimal currentDataPointPrice = singleDataPoint.getPrice();
        Long currentTotalQ = priceToTotalQuantity.get(currentDataPointPrice);
        if(currentTotalQ != null) {
            if(currentTotalQ > singleDataPoint.getQuantity()) {
                currentTotalQ -= singleDataPoint.getQuantity();
                priceToTotalQuantity.put(currentDataPointPrice,currentTotalQ);
            }
            else
                priceToTotalQuantity.remove(currentDataPointPrice);
        }
    }


    public Map<BigDecimal, Long> getBidPriceToTotalQuantity() {
        return bidPriceToTotalQuantity;
    }

    public Map<BigDecimal, Long> getOfferPriceToTotalQuantity() {
        return offerPriceToTotalQuantity;
    }

    public Set<PriceLevel> getSellPriceLevelSet() {
        Set<PriceLevel> copyOfSellPriceLevelSet = sellPriceLevelSet.stream()
                .map(pL -> new PriceLevel(pL.getSide(),pL.getPrice(),pL.getQuantity(),pL.getLpName()))
                .collect(Collectors.toCollection(TreeSet::new));
        return copyOfSellPriceLevelSet;
    }

    public Set<PriceLevel> getBuyPriceLevelSet() {
        Set<PriceLevel> copyOfBuyPriceLevelSet = buyPriceLevelSet.stream()
                .map(pL -> new PriceLevel(pL.getSide(),pL.getPrice(),pL.getQuantity(),pL.getLpName()))
                .collect(Collectors.toCollection(TreeSet::new));
        return copyOfBuyPriceLevelSet;
    }
}
