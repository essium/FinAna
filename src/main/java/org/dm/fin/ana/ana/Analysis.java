package org.dm.fin.ana.ana;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dm.fin.ana.data.CsiRepo;
import org.dm.fin.ana.model.CsiInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Analysis {
    public static void Tie4Industry(CsiRepo repo, CsiInfo info, Long start, String output) {
        Map<String, List<Double>> tie4ind = new HashMap<>();
        String code;
        for (String indName : info.name2code.get(4).keySet()) {
            tie4ind.putIfAbsent(indName, new ArrayList<>());
            for (Map.Entry<Long, CsiInfo> entry : repo.data.tree.entrySet()) {
                if (entry.getKey() < start) {
                    continue;
                }
                CsiInfo infoHistory = entry.getValue();
                if (!infoHistory.name2code.get(4).containsKey(indName)) {
                    continue;
                }
                code = infoHistory.name2code.get(4).get(indName);
                Double pb = infoHistory.industries.get(code).pb;
                tie4ind.get(indName).add(pb);
            }
        }
        for (List<Double> list : tie4ind.values()) {
            Collections.sort(list);
        }
        try (FileOutputStream fos = new FileOutputStream(output);
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
            for (Map.Entry<String, List<Double>> entry : tie4ind.entrySet()) {
                String name = entry.getKey();
                code = info.name2code.get(4).get(name);
                CsiInfo.Data current = info.industries.get(code);
                List<Double> history = entry.getValue();
                int size = history.size();
                if (size < 5 || Double.isInfinite(history.get(4))) {
                    continue;
                }
                row = sheet.createRow(idx);
                row.createCell(0).setCellValue(name);
                row.createCell(1).setCellValue(current.pb);
                row.createCell(2).setCellValue(order(current.pb, history));
                row.createCell(3).setCellValue(history.size());
                row.createCell(4).setCellValue(quantile(0.5, history));
                row.createCell(5).setCellValue(quantile(0.25, history));
                row.createCell(6).setCellValue(quantile(0.75, history));
                row.createCell(7).setCellValue(current.pe);
                row.createCell(8).setCellValue(current.dr);
                idx++;
            }
            workbook.write(fos);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }
    }

    private static double order(Double current, List<Double> history) {
        if (Double.isInfinite(current)) {
            return 1d;
        }
        int left = -1;
        int right = history.size();
        int mid = (left + right) / 2;
        while (right - left > 1) {
            if (current > history.get(mid)) {
                left = mid;
            } else {
                right = mid;
            }
            mid = (left + right) / 2;
        }
        if (left == -1) {
            return 0d;
        }
        if (right == history.size()) {
            return 1d;
        }
        if (history.get(right).isInfinite()) {
            return 1d * left / (history.size() - 1);
        }
        double r = (current - history.get(left)) / (history.get(right) - history.get(left));
        return (left + r * (right - left)) / (history.size() - 1);
    }

    private static Double quantile(double q, List<Double> history) {
        double position = q * (history.size() - 1);
        int left = (int) Math.floor(position);
        if (Double.isInfinite(history.get(left + 1))) {
            return Double.POSITIVE_INFINITY;
        }
        return (position - left) * history.get(left + 1) + (1d + left - position) * history.get(left);
    }
}
