package org.dm.fin.ana.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    public static DateTimeFormatter FMT_HYPHEN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter FMT_STANDARD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static LocalDate date_orig = LocalDate.of(1990, 1, 1);

    public static long date2Long(String dateStr, DateTimeFormatter fmt) {
        LocalDate date = LocalDate.from(fmt.parse(dateStr));
        return ChronoUnit.DAYS.between(date_orig, date);
    }

    public static String long2Date (long dateLong, DateTimeFormatter fmt) {
        return date_orig.plusDays(dateLong).format(fmt);
    }

    public static int long2Month(long dateLong) {
        LocalDate date = date_orig.plusDays(dateLong);
        return 12 * (date.getYear() - 1990) + date.getMonthValue() - 1;
    }
}
