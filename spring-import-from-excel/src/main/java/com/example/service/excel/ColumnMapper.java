package com.example.service.excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class ColumnMapper {

    private final List<ColumnInfo> columnInfos = new ArrayList<>();

    private ColumnMapper() {
    }

    public Map<String, Object> convert(XSSFRow row) {
        Map<String, Object> result = new HashMap<>();
        for (ColumnInfo columnInfo : columnInfos) {
            result.put(columnInfo.getColumnName(), columnInfo.convert(row));
        }
        return result;
    }

    public static class ColumnMapperBuilder {

        private final ColumnMapper columnMapper = new ColumnMapper();

        public ColumnMapperBuilder withStringColumn(String name, int index, boolean isObligatory) {
            this.columnMapper.columnInfos.add(new ColumnInfo(index, name, isObligatory, cell ->
                    Optional.ofNullable(cell)
                            .map(XSSFCell::getStringCellValue)));
            return this;
        }

        public ColumnMapperBuilder withDateColumn(String name, int index, boolean isObligatory) {
            this.columnMapper.columnInfos.add(new ColumnInfo(index, name, isObligatory, cell ->
                    Optional.ofNullable(cell)
                            .map(XSSFCell::getLocalDateTimeCellValue)
                            .map(LocalDateTime::toLocalDate)));
            return this;
        }

        public ColumnMapperBuilder withIntegerColumn(String name, int index, boolean isObligatory) {
            this.columnMapper.columnInfos.add(new ColumnInfo(index, name, isObligatory, cell ->
                    Optional.ofNullable(cell)
                            .map(XSSFCell::getNumericCellValue)
                            .map(val -> (int) Math.ceil(val))));
            return this;
        }

        public ColumnMapperBuilder withBigDecimalColumn(String name, int index, boolean isObligatory) {
            this.columnMapper.columnInfos.add(new ColumnInfo(index, name, isObligatory, cell ->
                    Optional.ofNullable(cell)
                            .map(XSSFCell::getNumericCellValue)
                            .map(BigDecimal::new)));
            return this;
        }

        public ColumnMapper build() {
            return this.columnMapper;
        }
    }

    private static class ColumnInfo {

        private final int columnIndex;

        private final String columnName;

        private final boolean isObligatory;
        private final Function<XSSFCell, Optional<Object>> mapper;

        public ColumnInfo(int columnIndex, String columnName, boolean isObligatory, Function<XSSFCell, Optional<Object>> mapper) {
            this.columnIndex = columnIndex;
            this.columnName = columnName;
            this.isObligatory = isObligatory;
            this.mapper = mapper;
        }

        public String getColumnName() {
            return columnName;
        }

        public Object convert(XSSFRow row) {
            Optional<Object> opt = mapper
                    .apply(row.getCell(columnIndex));
            if (isObligatory) {
                return opt.orElseThrow(() -> new IllegalArgumentException(String.format("No value for column %s at row %d", columnName, row.getRowNum())));
            }
            return opt.orElse(null);
        }
    }
}
