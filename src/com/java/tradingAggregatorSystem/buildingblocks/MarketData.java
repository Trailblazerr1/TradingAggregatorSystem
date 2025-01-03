package com.java.tradingAggregatorSystem.buildingblocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketData {
    private final String source;
    private final String instrument;
    private final List<MessagePriceLevel> buyMessagePriceLevelList;
    private final List<MessagePriceLevel> sellMessagePriceLevelList;

    public MarketData(String source, String instrument, List<MessagePriceLevel> buyMessagePriceLevelList,
                      List<MessagePriceLevel> sellMessagePriceLevelList) {
        //verify if buy contains correct msg? Maybe make a mode DR site and all
        //verify is source is among LP1 to LP 100 . Will take O(n) time
        this.source = source;
        this.instrument = instrument;
        this.buyMessagePriceLevelList = new ArrayList<>(buyMessagePriceLevelList);
        this.sellMessagePriceLevelList = new ArrayList<>(sellMessagePriceLevelList);
    }

    public String getSource() {
        return source;
    }

    public String getInstrument() {
        return instrument;
    }

    public List<MessagePriceLevel> getBuyMessagePriceLevelList() {
        return Collections.unmodifiableList(buyMessagePriceLevelList);
    }

    public List<MessagePriceLevel> getSellMessagePriceLevelList() {
        return Collections.unmodifiableList(sellMessagePriceLevelList);
    }



}
