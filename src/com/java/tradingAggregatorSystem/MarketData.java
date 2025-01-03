package com.java.tradingAggregatorSystem;

import java.util.ArrayList;
import java.util.List;

public class MarketData {
    private final String source;
    private final String instrument;
    private List<OrderMessage> buyOrderMessageList;
    private List<OrderMessage> sellOrderMessageList;

    public MarketData(String source, String instrument, List<OrderMessage> buyOrderMessageList,
                      List<OrderMessage> sellOrderMessageList) {
        this.source = source;
        this.instrument = instrument;
        //verify if buy contains correct msg? Maybe make a mode DR site and all
        this.buyOrderMessageList = new ArrayList<>(buyOrderMessageList);
        this.sellOrderMessageList = new ArrayList<>(sellOrderMessageList);
    }

    public String getSource() {
        return source;
    }

    public String getInstrument() {
        return instrument;
    }

    public List<OrderMessage> getBuyOrderMessageList() {
//        return Collections.unmodifiableList(buyOrderMessageList);
        return buyOrderMessageList;
    }

    public void setBuyOrderMessageList(List<OrderMessage> buyOrderMessageList) {
        this.buyOrderMessageList = buyOrderMessageList;
    }

    public List<OrderMessage> getSellOrderMessageList() {
        return sellOrderMessageList;
    }

    public void setSellOrderMessageList(List<OrderMessage> sellOrderMessageList) {
        this.sellOrderMessageList = sellOrderMessageList;
    }


}
