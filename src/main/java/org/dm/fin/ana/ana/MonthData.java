package org.dm.fin.ana.ana;

import org.dm.fin.ana.model.CsiInfo;
import org.dm.fin.ana.utils.DateUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MonthData {
    public int month;
    public Double pb;
    public Double pe;
    public Double dr;
    public List<CsiInfo.Data> data;

    public MonthData(int month) {
        this.month = month;
        data = new ArrayList<>();
    }

    public void compute() {
        Double pbSum = 0d;
        Double peSum = 0d;
        Double drSum = 0d;
        for (CsiInfo.Data d : data) {
            pbSum += d.pb;
            peSum += d.pe;
            drSum += d.dr;
        }
        pb = pbSum / data.size();
        pe = peSum / data.size();
        dr = drSum / data.size();
    }

    public void add(CsiInfo.Data d) {
        data.add(d);
    }

    public String toEcharts() {
        StringBuilder builder = new StringBuilder();
        builder.append("['");
        builder.append(DateUtil.month2Date(month));
        builder.append("',");
        if (pb.isInfinite()) {
            builder.append("0,0");
        } else {
            builder.append(pb);
            builder.append(",");
            if (pe.isInfinite()) {
                builder.append(0);
            } else {
                builder.append(pb / pe);
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public static class ComparePb implements Comparator<MonthData> {
        @Override
        public int compare(MonthData left, MonthData right) {
            return Double.compare(left.pb, right.pb);
        }
    }

    public static class CompareMonth implements Comparator<MonthData> {
        @Override
        public int compare(MonthData left, MonthData right) {
            return Integer.compare(left.month, right.month);
        }
    }
}
