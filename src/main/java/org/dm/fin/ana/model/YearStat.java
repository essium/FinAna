package org.dm.fin.ana.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

public class YearStat implements Serializable {
    public int year;
    public Report rq1;
    public Report rh;
    public Report rq3;
    public Report ry;
    public Dividend dh;
    public Dividend dy;
    public HashMap<Long, Pay> payMap;

    public YearStat() {
        rq1 = null;
        rh = null;
        rq3 = null;
        ry = null;
        dh = null;
        dy = null;
        payMap = new HashMap<>();
    }

    public static class CompareYear implements Comparator<YearStat> {
        @Override
        public int compare(YearStat left, YearStat right) {
            return Integer.compare(left.year, right.year);
        }
    }

    public static class Report implements Serializable {
        public double capital;
        public double equity;
        public double income;
    }

    public static class Dividend implements Serializable {
        public long date;
        public double capital;
        public double reserve;
        public double base;
        public double cash;
        public double share;
    }

    public static class Pay implements Serializable {
        public long date;
        public double outcome;
    }
}
