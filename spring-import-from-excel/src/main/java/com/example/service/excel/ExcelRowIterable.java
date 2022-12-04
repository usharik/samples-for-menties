package com.example.service.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

class ExcelRowIterable implements Iterable<Map<String, Object>> {

    private int rowIndex;

    private final XSSFSheet sheet;

    private final ColumnMapper columnMapper;

    public ExcelRowIterable(InputStream is, int sheetNum, int rowToStart, ColumnMapper columnMapper) throws IOException {
        this.columnMapper = columnMapper;
        XSSFWorkbook workBook = new XSSFWorkbook(is);
        FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();

        this.sheet = workBook.getSheetAt(sheetNum);
        this.rowIndex = rowToStart;
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
                return columnMapper.convert(sheet.getRow(rowIndex++));
            }
        };
    }
}
