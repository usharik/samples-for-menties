package com.example.service.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

class ExcelRowIterable implements Iterable<Map<String, Object>> {

    private int rowIndex;

    private final XSSFSheet sheet;

    private final ColumnMapper columnMapper;

    private final boolean forcedToString;

    private final FormulaEvaluator evaluator;

    private final DataFormatter dataFormatter = new DataFormatter(Locale.ROOT);

    public ExcelRowIterable(InputStream is, int sheetNum, int rowToStart, ColumnMapper columnMapper) throws IOException {
        this(is, sheetNum, rowToStart, columnMapper, false);
    }

    public ExcelRowIterable(InputStream is, int sheetNum, int rowToStart, ColumnMapper columnMapper, boolean forcedToString) throws IOException {
        this.columnMapper = columnMapper;
        this.forcedToString = forcedToString;
        this.rowIndex = rowToStart;

        XSSFWorkbook workBook = new XSSFWorkbook(is);
        this.evaluator = workBook.getCreationHelper().createFormulaEvaluator();
        this.sheet = workBook.getSheetAt(sheetNum);
    }

    private Map<String, Object> forcedToStringConverter(XSSFRow row) {
        Map<String, Object> result = new HashMap<>();
        for (ColumnMapper.ColumnInfo columnInfo : columnMapper.getColumnInfos()) {
            Optional<String> opt = Optional.ofNullable(row.getCell(columnInfo.getColumnIndex()))
                    .map(cell -> dataFormatter.formatCellValue(cell, evaluator));
            String str = null;
            if (columnInfo.isObligatory()) {
               str = opt.orElseThrow(() -> new IllegalArgumentException(String.format("No value for column %s at row %d", columnInfo.getColumnName(), row.getRowNum())));
            }
            result.put(columnInfo.getColumnName(), str);
        }
        return result;
    }

    @Override
    public Iterator<Map<String, Object>> iterator() {

        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return Optional.ofNullable(sheet.getRow(rowIndex))
                        .map(row -> row.getCell(0))
                        .filter(cell -> cell.getCellType() != CellType.BLANK)
                        .isPresent();
            }

            @Override
            public Map<String, Object> next() {
                if (forcedToString) {
                    return forcedToStringConverter(sheet.getRow(rowIndex++));
                }
                return columnMapper.convert(sheet.getRow(rowIndex++));
            }
        };
    }
}
