package org.dm.fin.ana.plot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

public class EchartsPlot {
    private static final String PREFIX = "<!DOCTYPE html>" +
            "<html><head><meta charset=\"utf-8\" />" +
            "<script src=\"echarts.js\"></script></head><body>\n";
    private static final String SUFFIX = "</body></html>";

    public static void plot(String file, List<Supplier<String>> divs) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(PREFIX.getBytes(StandardCharsets.UTF_8));
            for (Supplier<String> div : divs) {
                fos.write(div.get().getBytes(StandardCharsets.UTF_8));
            }
            fos.write(SUFFIX.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }
    }

    public static String createDiv(String id, int width, int height, Supplier<String> option) {
        return "<div id=\"" + id + "\" style=\"width:" + width + "px;height:" + height + "px;\"></div>" +
                "<script type=\"text/javascript\">var myChart = echarts.init(document.getElementById('" +
                id + "'));var option = {" + option.get() + "};myChart.setOption(option);</script>\n";
    }

    public static final String OPTION_TEMPLATE = "title:{text:'%s'}," +
            "tooltip: {trigger: 'axis',axisPointer: { type: 'cross' }}," +
            "legend: {},dataset:{source:[['time','市净率','净资产收益率'],%s]},xAxis: [{type: 'category',axisTick: {" +
            "alignWithLabel: true}}],yAxis: [{type: 'value',name: '市净率',position: 'left'},{type: 'value'," +
            "name: '净资产收益率',position: 'right'}],series: [{name: '市净率',type: 'line'," +
            "smooth:true,yAxisIndex: 0},{name: '净资产收益率',type: 'bar',yAxisIndex: 1}]";
}
