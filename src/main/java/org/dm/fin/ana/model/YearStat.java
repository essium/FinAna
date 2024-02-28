package org.dm.fin.ana.model;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class YearStat implements Serializable {
    public int year;
    public Report rq1;
    public Report rh;
    public Report rq3;
    public Report ry;
    public Dividend dh;
    public Dividend dy;
    public HashMap<Long, Pay> payMap;

    public YearStat(int year) {
        this.year = year;
        rq1 = null;
        rh = null;
        rq3 = null;
        ry = null;
        dh = null;
        dy = null;
        payMap = new HashMap<>();
    }

    public String echartsData() {
        double[] outcome = new double[4];
        double payOutcome = payMap.values().stream().mapToDouble(item -> item.outcome).sum() / 4e8;
        if (dh != null) {
            outcome[0] = dh.outcome() / 2e8 + payOutcome;
            outcome[1] = outcome[0];
            if (dy != null) {
                outcome[2] = dy.outcome() / 2e8 + payOutcome;
                outcome[3] = outcome[2];
            }
        } else if (dy != null) {
            outcome[0] = dy.outcome() / 4e8 + payOutcome;
            outcome[1] = outcome[0];
            outcome[2] = outcome[0];
            outcome[3] = outcome[0];
        }
        String template = "['%s',%f,%f,%f]";
        int yShort = year % 100;
        List<String> quarters = new ArrayList<>();
        if (rq1 != null) {
            quarters.add(String.format(template, yShort + "Q1", rq1.equity / 1e8, rq1.income / 1e8, outcome[0]));
            if (rh != null) {
                quarters.add(String.format(template, yShort + "Q2", rh.equity / 1e8, (rh.income - rq1.income) / 1e8, outcome[1]));
            }
            if (rq3 != null) {
                quarters.add(String.format(template, yShort + "Q3", rq3.equity / 1e8, (rq3.income - rh.income) / 1e8, outcome[2]));
            }
            if (ry != null) {
                quarters.add(String.format(template, yShort + "Q4", ry.equity / 1e8, (ry.income - rq3.income) / 1e8, outcome[3]));
            }
        } else if (rh != null) {
            quarters.add(String.format(template, yShort + "Q1", rh.equity / 1e8, rh.income / 2e8, outcome[0]));
            quarters.add(String.format(template, yShort + "Q2", rh.equity / 1e8, rh.income / 2e8, outcome[1]));
            if (rq3 != null) {
                quarters.add(String.format(template, yShort + "Q3", rq3.equity / 1e8, (rq3.income - rh.income) / 1e8, outcome[2]));
            }
            if (ry != null) {
                quarters.add(String.format(template, yShort + "Q4", ry.equity / 1e8, (ry.income - rq3.income) / 1e8, outcome[3]));
            }
        } else if (rq3 != null) {
            quarters.add(String.format(template, yShort + "Q1", rq3.equity / 1e8, rq3.income / 3e8, outcome[0]));
            quarters.add(String.format(template, yShort + "Q2", rq3.equity / 1e8, rq3.income / 3e8, outcome[1]));
            quarters.add(String.format(template, yShort + "Q3", rq3.equity / 1e8, rq3.income / 3e8, outcome[2]));
            if (ry != null) {
                quarters.add(String.format(template, yShort + "Q4", ry.equity / 1e8, (ry.income - rq3.income) / 1e8, outcome[3]));
            }
        } else if (ry != null) {
            quarters.add(String.format(template, yShort + "Q1", ry.equity / 1e8, ry.income / 4e8, outcome[0]));
            quarters.add(String.format(template, yShort + "Q2", ry.equity / 1e8, ry.income / 4e8, outcome[1]));
            quarters.add(String.format(template, yShort + "Q3", ry.equity / 1e8, ry.income / 4e8, outcome[2]));
            quarters.add(String.format(template, yShort + "Q4", ry.equity / 1e8, ry.income / 4e8, outcome[3]));
        }
        return String.join(",", quarters);
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

        public double outcome() {
            return (capital - reserve) / base * cash;
        }
    }

    public static class Pay implements Serializable {
        public long date;
        public double outcome;
    }
}
