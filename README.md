#Trading Aggregator System

TradingAggregatorSystem.java contains the main method to start the program.

Main Components
    MarketData: Represents data received from LPs, containing buy and sell price levels.
    PriceLevel: Abstract class for price levels, with subclasses for messages and custom levels.
    PriceBook: Manages aggregated buy and sell orders, sorted by price.
    PriceBookUtility: Provides utility functions for market calculations.

Prerequisites 
    Java 17

Input:
A single message from LP would have below attibutes:
LP, Side, List of Buy orders, List of Sell orders

Example Buy orders list:
    [{price,quantity,side}] i.e. [{84.100,1000000,BUY}]

Example input:
"LP1", "BUY",[{84.100,1000000,"BUY"}], [{84.100,5000000,"SELL"}]
