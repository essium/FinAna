package org.dm.fin.ana.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CsiInfo extends Tick {
    public Set<String> tier1;
    public Set<String> tier2;
    public Set<String> tier3;
    public Set<String> tier4;
    public Map<String, Data> industries;
    public Map<String, Stock> stocks;
    public CsiInfo() {
        this.code = "csi";
        this.tier1 = new HashSet<>();
        this.tier2 = new HashSet<>();
        this.tier3 = new HashSet<>();
        this.tier4 = new HashSet<>();
        this.industries = new HashMap<>();
        this.stocks = new HashMap<>();
    }

    public static class Data {
        public Double pe;
        public Double pb;
        public Double dr;
    }

    public static class Stock {
        public String tier1;
        public String tier2;
        public String tier3;
        public String tier4;
        public Data data;

        public Stock() {
            this.data = new Data();
        }
    }
}
