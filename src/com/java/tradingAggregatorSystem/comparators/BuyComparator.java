package com.java.tradingAggregatorSystem.comparators;

import com.java.tradingAggregatorSystem.buildingblocks.CustomPriceLevel;

import java.util.Comparator;

public class BuyComparator implements Comparator<CustomPriceLevel> {

    @Override
    public int compare(CustomPriceLevel m1, CustomPriceLevel m2) {
        int priceComp = m1.getPrice().compareTo(m2.getPrice());
        if(priceComp != 0) return -priceComp;
        if(m1.getQuantity() != m2.getQuantity())
            return -Long.compare(m1.getQuantity(), m2.getQuantity());
        return m1.getLpName().compareTo(m2.getLpName());
    }
}
