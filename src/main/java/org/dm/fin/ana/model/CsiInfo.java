package org.dm.fin.ana.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CsiInfo extends Tick {
    public Map<String, Data> industries;
    public Map<String, Stock> stocks;
    public Map<Integer, Map<String, String>> name2code;

    public CsiInfo() {
        this.code = "csi";
        this.industries = new HashMap<>();
        this.stocks = new HashMap<>();
        this.name2code = new HashMap<>();
        this.name2code.put(1, new HashMap<>());
        this.name2code.put(2, new HashMap<>());
        this.name2code.put(3, new HashMap<>());
        this.name2code.put(4, new HashMap<>());
    }

    public static class Data implements Serializable {
        public Double pe;
        public Double pb;
        public Double dr;
    }

    public static class Stock implements Serializable {
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
