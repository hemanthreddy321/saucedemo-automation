package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelDataReader - reads test data from Excel (.xlsx) files
 * for data-driven testing using Apache POI.
 *
 * Usage:
 *   List<Map<String,String>> data = ExcelDataReader.readSheet("src/test/resources/testdata/LoginData.xlsx", "Login");
 *   for (Map<String,String> row : data) {
 *       String username = row.get("username");
 *       String password = row.get("password");
 *   }
 */
public class ExcelDataReader {

    private ExcelDataReader() {}

    /**
     * Read all rows from a given sheet.
     * First row is treated as header (column names).
     *
     * @param filePath  path to .xlsx file
     * @param sheetName sheet name to read
     * @return list of rows, each row is a Map of columnName -> cellValue
     */
    public static List<Map<String, String>> readSheet(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return data;

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                boolean isEmptyRow = true;

                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = getCellValue(cell);
                    rowData.put(headers.get(j), value);
                    if (!value.isEmpty()) isEmptyRow = false;
                }

                if (!isEmptyRow) {
                    data.add(rowData);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return data;
    }

    /** Convert any cell type to String */
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                double val = cell.getNumericCellValue();
                if (val == Math.floor(val)) return String.valueOf((long) val);
                return String.valueOf(val);
            }
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default:      return "";
        }
    }

    /**
     * Read a specific column from a sheet as a list of strings.
     */
    public static List<String> readColumn(String filePath, String sheetName, String columnName) {
        List<String> column = new ArrayList<>();
        for (Map<String, String> row : readSheet(filePath, sheetName)) {
            String val = row.get(columnName);
            if (val != null) column.add(val);
        }
        return column;
    }
}
