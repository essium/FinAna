package org.dm.fin.ana.ana;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dm.fin.ana.data.CsiRepo;
import org.dm.fin.ana.model.CsiInfo;
import org.dm.fin.ana.plot.EchartsPlot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analysis {
    public static void Tie4Industry(CsiRepo repo, CsiInfo info, Long start, String outPrefix) {
        Map<String, MonthDataCollection> tie4industry = new HashMap<>();
        String code;
        for (String indName : info.name2code.get(4).keySet()) {
            tie4industry.putIfAbsent(indName, new MonthDataCollection());
            for (Map.Entry<Long, CsiInfo> entry : repo.data.tree.entrySet()) {
                if (entry.getKey() < start) {
                    continue;
                }
                CsiInfo infoHistory = entry.getValue();
                if (!infoHistory.name2code.get(4).containsKey(indName)) {
                    continue;
                }
                code = infoHistory.name2code.get(4).get(indName);
                tie4industry.get(indName).add(entry.getKey(), infoHistory.industries.get(code));
            }
        }
        for (MonthDataCollection collection : tie4industry.values()) {
            collection.compute();
            collection.sortPb();
        }
        try (FileOutputStream fos = new FileOutputStream(outPrefix + ".xlsx");
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("历史市净率统计");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("四级行业");
            row.createCell(1).setCellValue("最新市净率");
            row.createCell(2).setCellValue("市净率分位数");
            row.createCell(3).setCellValue("历史市净率数量");
            row.createCell(4).setCellValue("历史中值");
            row.createCell(5).setCellValue("历史第一四分位数");
            row.createCell(6).setCellValue("历史第三四分位数");
            row.createCell(7).setCellValue("最新市盈率");
            row.createCell(8).setCellValue("最新股息率");
            int idx = 1;
            for (Map.Entry<String, MonthDataCollection> entry : tie4industry.entrySet()) {
                String name = entry.getKey();
                code = info.name2code.get(4).get(name);
                CsiInfo.Data current = info.industries.get(code);
                MonthDataCollection history = entry.getValue();
                int size = history.listData.size();
                if (size < 12 || Double.isInfinite(history.quantilePb(0.75))) {
                    continue;
                }
                row = sheet.createRow(idx);
                row.createCell(0).setCellValue(name);
                row.createCell(1).setCellValue(current.pb);
                row.createCell(2).setCellValue(history.orderPb(current.pb));
                row.createCell(3).setCellValue(history.listData.size());
                row.createCell(4).setCellValue(history.quantilePb(0.5));
                row.createCell(5).setCellValue(history.quantilePb(0.25));
                row.createCell(6).setCellValue(history.quantilePb(0.75));
                row.createCell(7).setCellValue(current.pe);
                row.createCell(8).setCellValue(current.dr);
                idx++;
            }
            workbook.write(fos);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }
        EchartsPlot.plot(outPrefix + ".html", tie4industry.entrySet().stream()
                .filter(entry -> {
                    List<MonthData> list = entry.getValue().listData;
                    return !Double.isInfinite(list.getLast().pb);
                })
                .map(entry -> {
                    String key = entry.getKey();
                    MonthDataCollection value = entry.getValue();
                    return value.echartsPlot(key);
                }).toList());
    }

    public static void stock(CsiRepo repo, List<String> stocks, Long start, String output) {
        Map<String, MonthDataCollection> stockCollection = new HashMap<>();
        for (String code: stocks) {
            stockCollection.putIfAbsent(code, new MonthDataCollection());
            for (Map.Entry<Long, CsiInfo> entry : repo.data.tree.entrySet()) {
                if (entry.getKey() < start) {
                    continue;
                }
                CsiInfo info = entry.getValue();
                if (info.stocks.containsKey(code)) {
                    stockCollection.get(code).add(entry.getKey(), info.stocks.get(code).data);
                }
            }
        }
        for (MonthDataCollection collection : stockCollection.values()) {
            collection.compute();
            collection.sortPb();
        }
        EchartsPlot.plot(output, stockCollection.entrySet().stream()
                .filter(entry -> {
                    List<MonthData> list = entry.getValue().listData;
                    return !Double.isInfinite(list.getLast().pb);
                })
                .map(entry -> {
                    String key = entry.getKey();
                    MonthDataCollection value = entry.getValue();
                    return value.echartsPlot(key);
                }).toList());
    }
}
