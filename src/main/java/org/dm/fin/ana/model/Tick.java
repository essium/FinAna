package org.dm.fin.ana.model;

import java.io.Serializable;
import java.util.Comparator;

public class Tick implements Serializable {
    public String code;
    public long date;

    public static TickComparator COMPARATOR_INSTANCE = new TickComparator();

    public static class TickComparator implements Comparator<Tick> {
        @Override
        public int compare(Tick left, Tick right) {
            return Long.compare(left.date, right.date);
        }
    }
}
