package org.dm.fin.ana.model;

import org.dm.fin.ana.utils.DateUtil;

public enum ReportPeriod {
    Q1,
    H,
    Q3,
    Y;
    public long getDate(int year) {
        StringBuffer buffer = new StringBuffer(Integer.toString(year));
        switch (this) {
            case Q1:
                buffer.append("0331");
                break;
            case H:
                buffer.append("0630");
                break;
            case Q3:
                buffer.append("0930");
                break;
            case Y:
                buffer.append("1231");
                break;
            default:
                break;
        }
        return DateUtil.date2Long(buffer.toString(), DateUtil.FMT_STANDARD);
    }
}
