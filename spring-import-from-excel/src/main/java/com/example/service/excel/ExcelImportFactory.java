package com.example.service.excel;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class ExcelImportFactory {

    public Iterable<Map<String, Object>> createExcelRowIterable(InputStream is, int sheetNum, int rowToStart, ColumnMapper columnMapper) throws IOException {
        return new ExcelRowIterable(is, sheetNum, rowToStart, columnMapper);
    }
}
