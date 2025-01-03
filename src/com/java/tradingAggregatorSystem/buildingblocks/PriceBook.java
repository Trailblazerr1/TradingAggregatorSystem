package com.java.tradingAggregatorSystem.buildingblocks;

import com.java.tradingAggregatorSystem.comparators.BuyComparator;
import com.java.tradingAggregatorSystem.comparators.SellComparator;
import com.java.tradingAggregatorSystem.exceptions.InvalidMarketDataException;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class PriceBook {
    private static Logger LOGGER = Logger.getLogger(PriceBook.class.getName());

    private final String priceBookInstruement;
    private Set<CustomPriceLevel> buyCPriceLevelSet;
    private Set<CustomPriceLevel> sellCPriceLevelSet;
    private Map<String,Set<CustomPriceLevel>> lpToBids;
    private Map<String,Set<CustomPriceLevel>> lpToOffers;
    private Map<BigDecimal,Long> bidPriceToTotalQuantity;
    private Map<BigDecimal,Long> offerPriceToTotalQuantity;

    private final ReentrantReadWriteLock lock;
    //To support other insturments, we can have a PriceBookFactory to generate singleton
    //classes
    public PriceBook(String priceBookInstruement) {
        this.priceBookInstruement = priceBookInstruement;
        this.buyCPriceLevelSet = new TreeSet<>(new BuyComparator());
        this.sellCPriceLevelSet = new TreeSet<>(new SellComparator());
        this.lpToBids = new HashMap<>();
        this.lpToOffers = new HashMap<>();
        this.bidPriceToTotalQuantity = new HashMap<>();
        this.offerPriceToTotalQuantity = new HashMap<>();

        this.lock = new ReentrantReadWriteLock();
    }

    public void reset() {
        try {
            lock.writeLock().lock();
            buyCPriceLevelSet.clear();
            sellCPriceLevelSet.clear();
            lpToBids.clear();
            lpToOffers.clear();
            bidPriceToTotalQuantity.clear();
            offerPriceToTotalQuantity.clear();
            LOGGER.info("PriceBook data reset");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void update(MarketData marketData) throws InvalidMarketDataException {
        try {
            lock.writeLock().lock();
            if(!marketData.getInstrument().equals(priceBookInstruement)) {
                throw new InvalidMarketDataException("Invalid Instrument found in Market Data");
            }
            removePreviousPriceLevelDataOfLP(marketData, lpToBids, buyCPriceLevelSet, bidPriceToTotalQuantity);
            removePreviousPriceLevelDataOfLP(marketData, lpToOffers, sellCPriceLevelSet, offerPriceToTotalQuantity);
            //add new data
            addNewPriceLevelData(marketData.getSource(), marketData.getBuyMessagePriceLevelList(), MarketSide.BUY,
                    lpToBids, buyCPriceLevelSet, bidPriceToTotalQuantity);
            addNewPriceLevelData(marketData.getSource(), marketData.getSellMessagePriceLevelList(), MarketSide.SELL,
                    lpToOffers, sellCPriceLevelSet, offerPriceToTotalQuantity);
        } finally {
            lock.writeLock().unlock();
        }
    }


    private void addNewPriceLevelData(String source, List<MessagePriceLevel> messagePriceLevelList, MarketSide marketSide,
                                      Map<String, Set<CustomPriceLevel>> lpToPriceLevel, Set<CustomPriceLevel> priceLevelSet, Map<BigDecimal, Long> priceToTotalQuantity) throws InvalidMarketDataException {
        for(MessagePriceLevel messagePriceLevel : messagePriceLevelList) {
            BigDecimal currentOrderMessagePrice = messagePriceLevel.getPrice();
            CustomPriceLevel priceLevel = new CustomPriceLevel(marketSide,currentOrderMessagePrice, messagePriceLevel.getQuantity(),source);
            priceLevelSet.add(priceLevel);
            priceToTotalQuantity.put(currentOrderMessagePrice, priceToTotalQuantity.getOrDefault(currentOrderMessagePrice, 0L)+ messagePriceLevel.getQuantity());
            //lpToPriceLevel.getOrDefault(source, new HashSet<>());
            lpToPriceLevel.computeIfAbsent(source, key -> new HashSet<>()).add(priceLevel);
        }
    }

    private void removePreviousPriceLevelDataOfLP(MarketData marketData, Map<String, Set<CustomPriceLevel>> lpToPriceLevel,
                                                  Set<CustomPriceLevel> priceLevelSet, Map<BigDecimal, Long> priceToTotalQuantity) {
        Set<CustomPriceLevel> prevPriceLevelData = lpToPriceLevel.getOrDefault(marketData.getSource(), new HashSet<>());
        for(CustomPriceLevel singleDataPoint : prevPriceLevelData) {
            priceLevelSet.remove(singleDataPoint);
            updatePriceToTotalQuantityMapOnRemoval(priceToTotalQuantity,singleDataPoint);
        }
        lpToPriceLevel.remove(marketData.getSource());
    }

    private void updatePriceToTotalQuantityMapOnRemoval(Map<BigDecimal, Long> priceToTotalQuantity, CustomPriceLevel singleDataPoint) {
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
        try {
            lock.readLock().lock();
            return new HashMap<>(bidPriceToTotalQuantity);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<BigDecimal, Long> getOfferPriceToTotalQuantity() {
        try {
            lock.readLock().lock();
            return new HashMap<>(offerPriceToTotalQuantity);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<CustomPriceLevel> getSellCPriceLevelSet() {
        try {
            lock.readLock().lock();
            TreeSet<CustomPriceLevel> sellCPriceLevelSetCopy = new TreeSet<>(new BuyComparator());
            sellCPriceLevelSetCopy.addAll(sellCPriceLevelSet);
            return Collections.unmodifiableSet(sellCPriceLevelSetCopy);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<CustomPriceLevel> getBuyCPriceLevelSet() {
        try {
            lock.readLock().lock();
            TreeSet<CustomPriceLevel> buyCPriceLevelSetCopy = new TreeSet<>(new SellComparator());
            buyCPriceLevelSetCopy.addAll(buyCPriceLevelSet);
            return Collections.unmodifiableSet(buyCPriceLevelSetCopy);
        } finally {
            lock.readLock().unlock();
        }
    }
}
