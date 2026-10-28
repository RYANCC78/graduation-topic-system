package com.buptsse.topic.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtil {

    public static void export(HttpServletResponse response, String fileName, String[] headers, List<String[]> rows) throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("选题统计");
            Row head = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = head.createCell(i);
                c.setCellValue(headers[i]);
            }
            for (int i = 0; i < rows.size(); i++) {
                Row r = sheet.createRow(i + 1);
                String[] cells = rows.get(i);
                for (int j = 0; j < cells.length; j++) {
                    r.createCell(j).setCellValue(cells[j] == null ? "" : cells[j]);
                }
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            try (OutputStream out = response.getOutputStream()) {
                wb.write(out);
                out.flush();
            }
        }
    }
}
