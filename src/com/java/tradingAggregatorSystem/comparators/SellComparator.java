package com.java.tradingAggregatorSystem.comparators;

import com.java.tradingAggregatorSystem.PriceLevel;

import java.util.Comparator;

public class SellComparator implements Comparator<PriceLevel> {
    @Override
    public int compare(PriceLevel m1, PriceLevel m2) {
        int priceComp = m1.getPrice().compareTo(m2.getPrice());
        if(priceComp != 0) return priceComp;
        if(m1.getQuantity() != m2.getQuantity())
            return Long.compare(m1.getQuantity(), m2.getQuantity());
        return m1.getLpName().compareTo(m2.getLpName());
    }
}
