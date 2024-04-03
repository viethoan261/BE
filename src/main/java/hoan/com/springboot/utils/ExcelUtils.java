package hoan.com.springboot.utils;

import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author: Admin
 * @date: 7/9/2023
 **/
public class ExcelUtils {

    public static final int FONT_SIZE = 12;
    public static final String NAME_CELL = "nameCell";

    private ExcelUtils() {

    }

    public static String readCellContent(Cell cell) {
        String content;

        switch (cell.getCellType()) {

            case STRING:
                content = StrUtil.getString(cell.getStringCellValue());

                break;
            case NUMERIC:
                content = BigDecimal.valueOf(cell.getNumericCellValue()).toString();

                break;
            case BOOLEAN:
                content = cell.getBooleanCellValue() + StringPool.BLANK;

                break;
            case FORMULA:
                content = cell.getCellFormula() + StringPool.BLANK;

                break;
            default:
                content = StringPool.BLANK;
        }

        return content;
    }

    public static void createCell(Row row, int index, CellStyle style, String message) {
        Cell cell = row.createCell(index, CellType.STRING);
        cell.setCellValue(message);

        cell.setCellStyle(style);

        row.setRowStyle(style);
    }

    public static void modifyValue(Cell cell, String message) {
        CellStyle cellStyle = cell.getCellStyle();
        cell.setCellValue(message.toUpperCase());
        cell.setCellStyle(cellStyle);
    }

    public static void createRotateCell(Row row, int index, CellStyle style, String message) {
        Cell cell = row.createCell(index, CellType.STRING);
        cell.setCellValue(message);

        cell.setCellStyle(style);

        row.setRowStyle(style);
    }

    public static CellStyle createBoldBlackWithRotateCellStyle(Workbook workbook) {
        CellStyle cellStyle = createCellStyle(workbook, IndexedColors.BLACK.getIndex(), NAME_CELL);
        cellStyle.setRotation((short) 90);
        return cellStyle;
    }

    public static CellStyle createRedCellStyle(Workbook workbook, Boolean isBold) {
        String type = Boolean.TRUE.equals(isBold) ? NAME_CELL : null;
        return createCellStyle(workbook, IndexedColors.RED.getIndex(), type);
    }

    public static CellStyle createBoldBlackCellStyle(Workbook workbook) {
        return createCellStyle(workbook, IndexedColors.BLACK.getIndex(), NAME_CELL);
    }

    public static CellStyle createValueCellStyle(Workbook workbook) {
        return createCellStyle(workbook, IndexedColors.BLACK1.getIndex(), null);
    }

    public static CellStyle createSuccessCellStyle(Workbook workbook) {
        return createCellStyle(workbook, IndexedColors.BLUE.getIndex(), null);
    }

    public static CellStyle createCellStyle(Workbook workbook, short fontIndex, String type) {
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();

        font.setColor(fontIndex);

        // set font bold neu la ten cot
        if (Objects.nonNull(type) && type.equals(NAME_CELL)) {
            font.setBold(true);
        } else {
            font.setItalic(true);
        }
        font.setFontHeight((short) (FONT_SIZE * 20));

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setFontName("Times New Roman");
        style.setFont(font);
        style.setWrapText(true);

        return style;
    }

    /**
     * xoa row empty khong co data
     *
     * @param sheet: Sheet
     */
    private void removeEmptyRows(Sheet sheet) {
        boolean isRowEmpty = Boolean.FALSE;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i) == null) {
                isRowEmpty = true;
                sheet.shiftRows(i + 1, sheet.getLastRowNum() + 1, -1);
                i--;
                continue;
            }
            for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++) {
                if (sheet.getRow(i).getCell(j) == null ||
                        sheet.getRow(i).getCell(j).toString().trim().equals("")) {
                    isRowEmpty = true;
                } else {
                    isRowEmpty = false;
                    break;
                }
            }
            if (isRowEmpty) {
                sheet.shiftRows(i + 1, sheet.getLastRowNum() + 1, -1);
                i--;
            }
        }
    }
    public static CellStyle createTitleCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 15);

        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // Thiết lập các thuộc tính cell style khác nếu cần thiết

        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

}