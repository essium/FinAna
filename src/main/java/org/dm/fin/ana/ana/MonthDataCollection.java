package org.dm.fin.ana.ana;

import org.dm.fin.ana.model.CsiInfo;
import org.dm.fin.ana.utils.DateUtil;

import java.util.*;

public class MonthDataCollection {
    public Map<Integer, MonthData> mapData;
    public List<MonthData> listData;
    public MonthDataCollection() {
        mapData = new HashMap<>();
    }

    public void add(long dateLong, CsiInfo.Data d) {
        int month = DateUtil.long2Month(dateLong);
        mapData.putIfAbsent(month, new MonthData(month));
        mapData.get(month).add(d);
    }

    public void compute() {
        for (MonthData d : mapData.values()) {
            d.compute();
        }
        listData = new ArrayList<>(mapData.values());
    }

    public void sortPb() {
        listData.sort(new MonthData.ComparePb());
    }

    public void sortMonth() {
        listData.sort(new MonthData.CompareMonth());
    }

    public double orderPb(Double currentPb) {
        if (Double.isInfinite(currentPb)) {
            return 1d;
        }
        int left = -1;
        int right = listData.size();
        int mid = (left + right) / 2;
        while (right - left > 1) {
            if (currentPb > listData.get(mid).pb) {
                left = mid;
            } else {
                right = mid;
            }
            mid = (left + right) / 2;
        }
        if (left == -1) {
            return 0d;
        }
        if (right == listData.size()) {
            return 1d;
        }
        if (listData.get(right).pb.isInfinite()) {
            return 1d * left / (listData.size() - 1);
        }
        double r = (currentPb - listData.get(left).pb) / (listData.get(right).pb - listData.get(left).pb);
        return (left + r * (right - left)) / (listData.size() - 1);
    }

    public Double quantilePb(double q) {
        double position = q * (listData.size() - 1);
        int left = (int) Math.floor(position);
        if (Double.isInfinite(listData.get(left + 1).pb)) {
            return Double.POSITIVE_INFINITY;
        }
        return (position - left) * listData.get(left + 1).pb + (1d + left - position) * listData.get(left).pb;
    }
}
