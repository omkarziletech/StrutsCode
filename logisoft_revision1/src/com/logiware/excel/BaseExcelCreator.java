package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PaperSize;
import org.apache.poi.ss.usermodel.PrintOrientation;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;

/**
 *
 * @author Lakshmi Naryanan
 */
public class BaseExcelCreator extends IBaseExcelCreator {

    DecimalFormat df = new DecimalFormat("########0.00");

    protected void createWorkBook() {
        workbook = new SXSSFWorkbook(-1);
        workbook.setCompressTempFiles(true);
    }

    protected void createHelper() {
        helper = workbook.getCreationHelper();
    }

    protected void createSheet() {
        sheet = workbook.createSheet(sheetName);
    }

    protected void createRow() throws IOException {
        if (rowIndex != 0 && rowIndex % 100 == 0) {
            ((SXSSFSheet) sheet).flushRows();
        }
        rowIndex++;
        row = sheet.createRow(rowIndex);
    }

    protected void removeRow() {
        row = sheet.createRow(rowIndex);
        sheet.removeRow(row);
        rowIndex--;
    }

    protected XSSFColor createColor(String color) {
        return new XSSFColor(java.awt.Color.decode(color));
    }

    protected XSSFColor createColor(int r, int g, int b) {
        return new XSSFColor(new java.awt.Color(r, g, b));
    }

    protected XSSFFont createFont(String fontName, int fontSize, XSSFColor color) {
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints((short) fontSize);
        font.setColor(color);
        return font;
    }

    protected void setLclPrintSize() {
        XSSFPrintSetup printSetup = (XSSFPrintSetup) sheet.getPrintSetup();
        printSetup.setHeaderMargin(0.3D);
        printSetup.setFooterMargin(0.3D);
        printSetup.setOrientation(PrintOrientation.LANDSCAPE);
        printSetup.setPaperSize(PaperSize.A4_PAPER);
    }

    protected XSSFFont createFont(String fontName, int fontSize, XSSFColor color, int boldWeight) {
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints((short) fontSize);
        font.setColor(color);
        font.setBoldweight((short) boldWeight);
        return font;
    }

    protected CellStyle createCellStyle(XSSFColor backgroundColor, XSSFFont font, short alignment) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(backgroundColor);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);
        style.setAlignment(alignment);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        return (CellStyle) style;
    }

    protected CellStyle createLclCellStyle(XSSFColor backgroundColor, Font font, short alignment) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setFont(font);
        style.setLocked(true);
        style.setWrapText(false);
        style.setFillForegroundColor(backgroundColor);
        style.setAlignment(alignment);
        style.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
        style.setBorderTop(CellStyle.BORDER_NONE);
        style.setBorderRight(CellStyle.BORDER_NONE);
        style.setBorderBottom(CellStyle.BORDER_NONE);
        style.setBorderLeft(CellStyle.BORDER_NONE);
        return (CellStyle) style;
    }

    protected void setCellBorder(CellStyle cellStyle, short borderTop, short boderRight, short borderBottom, short borderLeft) {
        cellStyle.setBorderTop(borderTop);
        cellStyle.setBorderRight(boderRight);
        cellStyle.setBorderBottom(borderBottom);
        cellStyle.setBorderLeft(borderLeft);
    }

    protected void createCell() throws IOException {
        columnIndex++;
        if (null == row) {
            createRow();
        }
        cell = row.createCell(columnIndex);
    }

    protected void createEmptyCell(CellStyle cellStyle) throws IOException {
        createCell();
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
    }

    protected void createHeaderCell(String value, CellStyle cellStyle) throws IOException {
        createCell();
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    protected void createHeaderCell(String value, CellStyle cellStyle, int columnWidth) throws IOException {
        createCell();
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
        sheet.setColumnWidth(columnIndex, 256 * columnWidth);
    }

    protected void createTextCell(String value, CellStyle cellStyle) throws IOException {
        createCell();
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }
    protected void createTextCell(boolean value, CellStyle cellStyle) throws IOException {
        createCell();
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
        
    }
    protected void createTextCell(double value, CellStyle cellStyle) throws IOException {
        createCell();
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
        
    }
    protected void createTextCellWithRow(String value, CellStyle cellStyle) throws IOException {
        createCell();
        cell.setCellValue(value);
        cell.getRow().setHeight((short)1000);
        cell.setCellStyle(cellStyle);
    }
   
    protected void createIntegerCell(Integer value, CellStyle cellStyle) throws IOException {
        createCell();
        if (null != value) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
        cellStyle.setDataFormat(helper.createDataFormat().getFormat("#,##0_);[Red](#,##0)"));
        cell.setCellStyle(cellStyle);
    }

    protected void createIntegerCell(String value, CellStyle cellStyle) throws IOException {
        createIntegerCell(CommonUtils.isNotEmpty(value) ? Integer.parseInt(value.replace(",", "")) : null, cellStyle);
    }

    protected void createPercentageCell(Double value, CellStyle cellStyle) throws IOException {
        createCell();
        if (null != value) {
            cell.setCellValue(value / 100);
        } else {
            cell.setCellValue("");
        }
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("##%"));
        cell.setCellStyle(cellStyle);
    }

    protected void createPercentageCell(String value, CellStyle cellStyle) throws IOException {
        createPercentageCell(CommonUtils.isNotEmpty(value) ? Double.parseDouble(value.replaceAll("[^0-9.]", "")) : null, cellStyle);
    }

    protected void createDollarCell(Double value, CellStyle cellStyle) throws IOException {
        createCell();
        if (null != value) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
        cellStyle.setDataFormat(helper.createDataFormat().getFormat("$#,##0.00"));
        cell.setCellStyle(cellStyle);
    }

    protected void createDollarCell(String value, CellStyle cellStyle) throws IOException {
        createDollarCell(CommonUtils.isNotEmpty(value) ? Double.parseDouble(value.replaceAll("[^0-9.]", "")) : null, cellStyle);
    }

    protected void createAmountCell(Double value, CellStyle cellStyle) throws IOException {
        createCell();
        if (null != value) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
        cellStyle.setDataFormat(helper.createDataFormat().getFormat("#,##0.00"));
        cell.setCellStyle(cellStyle);
    }

    protected void createAmountCell(String value, CellStyle cellStyle) throws IOException {
        createAmountCell(CommonUtils.isNotEmpty(value) ? Double.parseDouble(value.replace(",", "")) : null, cellStyle);
    }

    protected void createLongCell(Long value, CellStyle cellStyle) throws IOException {
        createCell();
        if (null != value) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
        cellStyle.setDataFormat(helper.createDataFormat().getFormat("#,##0"));
        cell.setCellStyle(cellStyle);
    }

    protected void createLongCell(String value, CellStyle cellStyle) throws IOException {
        createLongCell(CommonUtils.isNotEmpty(value) ? Long.parseLong(value.replace(",", "")) : null, cellStyle);
    }

    protected void createDoubleCell(Double value, CellStyle cellStyle) throws IOException {
        createCell();
        if (null != value) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
        cellStyle.setDataFormat(doubleDataFormat);
        cell.setCellStyle(cellStyle);
    }

    protected void createDoubleCell(String value, CellStyle cellStyle) throws IOException {
        createDoubleCell(CommonUtils.isNotEmpty(value) ? Double.parseDouble(value.replace(",", "")) : null, cellStyle);
    }

    protected void resetColumnIndex() {
        columnIndex = -1;
    }

    protected void setColumnAutoSize() {
//      this method won't work anymore since SXSSFSheet has been used now in the place of XSSFSheet.        
//        while (columnIndex >= 0) {
//            sheet.autoSizeColumn(columnIndex);
//            columnIndex--;
//        }
    }

    protected void setColumnAutoSize(Integer startColumnIndex, Integer endColumnIndex) {
//      this method won't work anymore since SXSSFSheet has been used now in the place of XSSFSheet.        
//        for (int colIndex = startColumnIndex; colIndex <= endColumnIndex; colIndex++) {
//            sheet.autoSizeColumn(colIndex);
//        }
    }

    protected void mergeCells(Integer fromRow, Integer toRow, Integer fromColumn, Integer toColumn) throws IOException {
        row = sheet.getRow(fromRow);
        CellStyle firstCellStyle = row.getCell(fromColumn).getCellStyle();
        for (int colCount = fromColumn + 1; colCount <= toColumn; colCount++) {
            createCell();
            cell.setCellValue("");
            cell.setCellStyle(firstCellStyle);
        }
        if (CommonUtils.isNotEqual(fromRow, toRow)) {
            for (int rowCount = fromRow; rowCount <= toRow; rowCount++) {
                row = sheet.getRow(rowCount);
                if (null == row) {
                    createRow();
                }
                columnIndex = fromColumn;
                for (int colCount = fromColumn; colCount <= toColumn; colCount++) {
                    createCell();
                    cell.setCellValue("");
                    cell.setCellStyle(firstCellStyle);
                }
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(fromRow, toRow, fromColumn, toColumn));
    }

    protected void createColors() {
        BLACK = createColor(255, 255, 255);
        RED = createColor(255, 0, 0);
        WHITE = createColor(0, 0, 0);
        BG_BLACK = createColor(0, 0, 0);
        BG_WHITE = createColor(255, 255, 255);
        DARK_BLUE = createColor(75, 172, 198);
        LIGHT_BLUE = createColor(219, 238, 243);
        ORANGE_RED = createColor(255, 69, 0);
        LIGHT_ORANGE = createColor("#FFAD8F");
        SKY_BLUE = createColor("#CBDADE");
        DARK_ASH = createColor("#C3FDB8");
        LIGHT_ASH = createColor("#DCDCDC");
        LAVENDAR = createColor("#EBDDE2");
        LIGHT_GRAY = createColor("#808080");
        LIGHTER_GRAY = createColor("#CCCCCC");
        LIGHT_GREEN = createColor("#90EE90");
        BG_YELLOW = createColor("#FFFF00");
    }

    protected void createFonts() {
        blackBoldFont16 = createFont("Arial", 16, BLACK, Font.BOLDWEIGHT_BOLD);
        blackBoldFont15 = createFont("Arial", 15, BLACK, Font.BOLDWEIGHT_BOLD);
        blackBoldFont12 = createFont("Arial", 12, BLACK, Font.BOLDWEIGHT_BOLD);
        blackNormalFont12 = createFont("Arial", 12, BLACK, Font.BOLDWEIGHT_NORMAL);
        blackBoldFont10 = createFont("Arial", 10, BLACK, Font.BOLDWEIGHT_BOLD);
        blackBoldFont11 = createFont("Arial", 11, BLACK, Font.BOLDWEIGHT_BOLD);
        redBoldFont10 = createFont("Arial", 10, RED, Font.BOLDWEIGHT_BOLD);
        blackNormalFont10 = createFont("Arial", 10, BLACK, Font.BOLDWEIGHT_NORMAL);
        redNormalFont10 = createFont("Arial", 10, RED, Font.BOLDWEIGHT_NORMAL);
        blackNormalFont11 = createFont("Calibri", 11, BLACK, Font.BOLDWEIGHT_NORMAL);
        whiteNormalFont11 = createFont("Calibri", 11, WHITE, Font.BOLDWEIGHT_NORMAL);
    }

    protected void createCellStyles() {
        //Base Header Cell Style
        baseHeaderCellStyleLeftBold = createCellStyle(BG_WHITE, blackBoldFont16, CellStyle.ALIGN_LEFT);
        baseHeaderCellStyleCenterBold = createCellStyle(BG_WHITE, blackBoldFont16, CellStyle.ALIGN_CENTER);
        baseHeaderCellStyleRightBold = createCellStyle(BG_WHITE, blackBoldFont16, CellStyle.ALIGN_RIGHT);
        //Secondary Header Cell Style
        secondaryHeaderCellStyleLeftNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_LEFT);
        secondaryHeaderCellStyleCenterNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_CENTER);
        secondaryHeaderCellStyleRightNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_RIGHT);
        //Header Cell Style
        headerCellStyleLeftBold = createCellStyle(ORANGE_RED, blackBoldFont15, CellStyle.ALIGN_LEFT);
        headerCellStyleCenterBold = createCellStyle(ORANGE_RED, blackBoldFont15, CellStyle.ALIGN_CENTER);
        headerCellStyleRightBold = createCellStyle(ORANGE_RED, blackBoldFont15, CellStyle.ALIGN_RIGHT);
        //Light Gray Header Cell Style
        lightGrayHeaderCellStyleLeftBold = createCellStyle(LIGHT_GRAY, blackBoldFont16, CellStyle.ALIGN_LEFT);
        lightGrayHeaderCellStyleCenterBold = createCellStyle(LIGHT_GRAY, blackBoldFont16, CellStyle.ALIGN_CENTER);
        lightGrayHeaderCellStyleRightBold = createCellStyle(LIGHT_GRAY, blackBoldFont16, CellStyle.ALIGN_RIGHT);
        //Sub Header One Cell Style
        subHeaderOneCellStyleLeftBold = createCellStyle(LIGHT_ORANGE, blackBoldFont12, CellStyle.ALIGN_LEFT);
        subHeaderOneCellStyleLeftNormal = createCellStyle(LIGHT_ORANGE, blackNormalFont12, CellStyle.ALIGN_LEFT);
        subHeaderOneCellStyleCenterBold = createCellStyle(LIGHT_ORANGE, blackBoldFont12, CellStyle.ALIGN_CENTER);
        subHeaderOneCellStyleCenterNormal = createCellStyle(LIGHT_ORANGE, blackNormalFont12, CellStyle.ALIGN_CENTER);
        subHeaderOneCellStyleCenter10 = createCellStyle(LIGHT_ORANGE, blackBoldFont10, CellStyle.ALIGN_CENTER);
        subHeaderOneCellStyleRightBold = createCellStyle(LIGHT_ORANGE, blackBoldFont12, CellStyle.ALIGN_RIGHT);
        subHeaderOneCellStyleRightNormal = createCellStyle(LIGHT_ORANGE, blackNormalFont12, CellStyle.ALIGN_RIGHT);
        subHeaderOneCellStyleGray = createCellStyle(LIGHTER_GRAY, blackBoldFont11, CellStyle.ALIGN_LEFT);
        subHeaderOneCellStyleGreen = createCellStyle(LIGHT_GREEN, blackBoldFont11, CellStyle.ALIGN_LEFT);

        //Sub Header Two Cell Style
        subHeaderTwoCellStyleLeftBold = createCellStyle(SKY_BLUE, blackBoldFont12, CellStyle.ALIGN_LEFT);
        subHeaderTwoCellStyleLeftNormal = createCellStyle(SKY_BLUE, blackNormalFont12, CellStyle.ALIGN_LEFT);
        subHeaderTwoCellStyleCenterBold = createCellStyle(SKY_BLUE, blackBoldFont12, CellStyle.ALIGN_CENTER);
        subHeaderTwoCellStyleCenterNormal = createCellStyle(SKY_BLUE, blackNormalFont12, CellStyle.ALIGN_CENTER);
        subHeaderTwoCellStyleCenter10 = createCellStyle(SKY_BLUE, blackBoldFont10, CellStyle.ALIGN_CENTER);
        subHeaderTwoCellStyleLeft10 = createCellStyle(SKY_BLUE, blackBoldFont10, CellStyle.ALIGN_LEFT);
        subHeaderTwoCellStyleRightBold = createCellStyle(SKY_BLUE, blackBoldFont12, CellStyle.ALIGN_RIGHT);
        subHeaderTwoCellStyleRightNormal = createCellStyle(SKY_BLUE, blackNormalFont12, CellStyle.ALIGN_RIGHT);
        //Table Header Cell Style
        tableHeaderCellStyleLeftBold = createCellStyle(DARK_BLUE, blackBoldFont10, CellStyle.ALIGN_LEFT);
        tableHeaderCellStyleCenterBold = createCellStyle(DARK_BLUE, blackBoldFont10, CellStyle.ALIGN_CENTER);
        tableHeaderCellStyleRightBold = createCellStyle(DARK_BLUE, blackBoldFont10, CellStyle.ALIGN_RIGHT);
        //Table Row Odd Cell Style
        tableOddRowCellStyleLeftBold = createCellStyle(BG_WHITE, blackBoldFont10, CellStyle.ALIGN_LEFT);
        tableOddRowCellStyleLeftNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_LEFT);
        tableOddRowCellStyleCenterBold = createCellStyle(BG_WHITE, blackBoldFont10, CellStyle.ALIGN_CENTER);
        tableOddRowCellStyleCenterNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_CENTER);
        tableOddRowCellStyleRightBold = createCellStyle(BG_WHITE, blackBoldFont10, CellStyle.ALIGN_RIGHT);
        tableOddRowCellStyleRightNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_RIGHT);
        //Table Row Even Cell Style
        tableEvenRowCellStyleLeftBold = createCellStyle(LIGHT_BLUE, blackBoldFont10, CellStyle.ALIGN_LEFT);
        tableEvenRowCellStyleLeftNormal = createCellStyle(LIGHT_BLUE, blackNormalFont10, CellStyle.ALIGN_LEFT);
        tableEvenRowCellStyleCenterBold = createCellStyle(LIGHT_BLUE, blackBoldFont10, CellStyle.ALIGN_CENTER);
        tableEvenRowCellStyleCenterNormal = createCellStyle(LIGHT_BLUE, blackNormalFont10, CellStyle.ALIGN_CENTER);
        tableEvenRowCellStyleRightBold = createCellStyle(LIGHT_BLUE, blackBoldFont10, CellStyle.ALIGN_RIGHT);
        tableEvenRowCellStyleRightNormal = createCellStyle(LIGHT_BLUE, blackNormalFont10, CellStyle.ALIGN_RIGHT);
        //Light Ash Background Cell Style
        lightAshCellStyleLeftBold = createCellStyle(LIGHT_ASH, blackBoldFont10, CellStyle.ALIGN_LEFT);
        lightAshCellStyleLeftNormal = createCellStyle(LIGHT_ASH, blackNormalFont10, CellStyle.ALIGN_LEFT);
        lightAshCellStyleCenterBold = createCellStyle(LIGHT_ASH, blackBoldFont10, CellStyle.ALIGN_CENTER);
        lightAshCellStyleCenterNormal = createCellStyle(LIGHT_ASH, blackNormalFont10, CellStyle.ALIGN_CENTER);
        lightAshCellStyleRightBold = createCellStyle(LIGHT_ASH, blackBoldFont10, CellStyle.ALIGN_RIGHT);
        lightAshCellStyleRightNormal = createCellStyle(LIGHT_ASH, blackNormalFont10, CellStyle.ALIGN_RIGHT);
        //Dark Ash Background Cell Style
        darkAshCellStyleLeftBold = createCellStyle(DARK_ASH, blackBoldFont10, CellStyle.ALIGN_LEFT);
        darkAshCellStyleLeftNormal = createCellStyle(DARK_ASH, blackNormalFont10, CellStyle.ALIGN_LEFT);
        darkAshCellStyleCenterBold = createCellStyle(DARK_ASH, blackBoldFont10, CellStyle.ALIGN_CENTER);
        darkAshCellStyleCenterNormal = createCellStyle(DARK_ASH, blackNormalFont10, CellStyle.ALIGN_CENTER);
        darkAshCellStyleRightBold = createCellStyle(DARK_ASH, blackBoldFont10, CellStyle.ALIGN_RIGHT);
        darkAshCellStyleRightNormal = createCellStyle(DARK_ASH, blackNormalFont10, CellStyle.ALIGN_RIGHT);
        //Lavendar Background Cell Style
        lavendarCellStyleLeftBold = createCellStyle(LAVENDAR, blackBoldFont10, CellStyle.ALIGN_LEFT);
        lavendarCellStyleLeftNormal = createCellStyle(LAVENDAR, blackNormalFont10, CellStyle.ALIGN_LEFT);
        lavendarCellStyleCenterBold = createCellStyle(LAVENDAR, blackBoldFont10, CellStyle.ALIGN_CENTER);
        lavendarCellStyleCenterNormal = createCellStyle(LAVENDAR, blackNormalFont10, CellStyle.ALIGN_CENTER);
        lavendarCellStyleRightBold = createCellStyle(LAVENDAR, blackBoldFont10, CellStyle.ALIGN_RIGHT);
        lavendarCellStyleRightNormal = createCellStyle(LAVENDAR, blackNormalFont10, CellStyle.ALIGN_RIGHT);
        //Normal Cell Style
        cellStyleLeftBold = createCellStyle(BG_WHITE, blackBoldFont10, CellStyle.ALIGN_LEFT);
        cellStyleLeftNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_LEFT);
        cellStyleCenterBold = createCellStyle(BG_WHITE, blackBoldFont10, CellStyle.ALIGN_CENTER);
        cellStyleCenterBoldYellow = createCellStyle(BG_YELLOW, blackBoldFont10, CellStyle.ALIGN_CENTER);
        cellStyleCenterNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_CENTER);
        cellStyleRightBold = createCellStyle(BG_WHITE, blackBoldFont10, CellStyle.ALIGN_RIGHT);
        cellStyleRightNormal = createCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_RIGHT);
        cellStyleLeftNormalForTruker = createCellStyle(BG_WHITE, blackNormalFont11, CellStyle.ALIGN_LEFT);
        cellStyleCenterNormalForTruker = createCellStyle(BG_WHITE, blackNormalFont11, CellStyle.ALIGN_CENTER);

        redCellStyleLeftBold = createCellStyle(RED, blackNormalFont11, CellStyle.ALIGN_LEFT);
        redCellStyleLeftNormal = createCellStyle(RED, blackNormalFont11, CellStyle.ALIGN_LEFT);
        redCellStyleCenterBold = createCellStyle(RED, blackNormalFont11, CellStyle.ALIGN_CENTER);
        redCellStyleCenterNormal = createCellStyle(RED, blackNormalFont11, CellStyle.ALIGN_CENTER);
        redCellStyleRightBold = createCellStyle(RED, blackNormalFont11, CellStyle.ALIGN_RIGHT);
        redCellStyleRightNormal = createCellStyle(RED, blackNormalFont11, CellStyle.ALIGN_RIGHT);
        //lcl
        lclCellStyleLeftNormal = createLclCellStyle(BG_WHITE, blackNormalFont11, CellStyle.ALIGN_LEFT);
        lclCellStyleLeftNormalArial10 = createLclCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_LEFT);
        redLclCellStyleRightNormalArial10 = createLclCellStyle(BG_WHITE, redNormalFont10, CellStyle.ALIGN_RIGHT);
        lclCellStyleLeftBoldArial15 = createLclCellStyle(BG_WHITE, blackBoldFont15, CellStyle.ALIGN_LEFT);
        lclCellStyleLeftBoldArial12 = createLclCellStyle(BG_WHITE, blackBoldFont12, CellStyle.ALIGN_LEFT);
        lclCellStyleBoldArialBorder = createLclCellStyle(BG_BLACK, blackBoldFont10, CellStyle.BORDER_THICK);
        lclCellStyleBoldArialLine = createLclCellStyle(BG_BLACK, blackBoldFont10, CellStyle.BORDER_THICK);
        lclCellStyleBoldArialBottomLine = createLclCellStyle(BG_BLACK, blackBoldFont10, CellStyle.BORDER_THICK);
        lclCellStyleBoldArialBottomRightLine = createLclCellStyle(BG_BLACK, blackBoldFont10, CellStyle.BORDER_THICK);
        lclCellStyleLeftBoldArial11 = createLclCellStyle(BG_WHITE, blackBoldFont11, CellStyle.ALIGN_LEFT);
        lclCellStyleLeftBoldArial10 = createLclCellStyle(BG_WHITE, blackBoldFont10, CellStyle.ALIGN_LEFT);
        lclCellStyleNormalArial10 = createLclCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_LEFT);
        cellStyleRightNormalArial10 = createLclCellStyle(BG_WHITE, blackNormalFont10, CellStyle.ALIGN_RIGHT);
        cellStyleRightBoldArial10 = createLclCellStyle(BG_WHITE, blackBoldFont10, CellStyle.ALIGN_RIGHT);
        blackCellBackground = createCellStyle(BG_BLACK, whiteNormalFont11, CellStyle.ALIGN_LEFT);
    }

    protected void createDataFormats() {
        integerDataFormat = helper.createDataFormat().getFormat("#,##0_);[Red](#,##0)");
        doubleDataFormat = helper.createDataFormat().getFormat("#,##0.00_);[Red](#,##0.00)");
    }

    protected void init(String fileName, String sheetName) {
        this.fileName = fileName;
        this.sheetName = sheetName;
        createWorkBook();
        createHelper();
        createSheet();
        createColors();
        createFonts();
        createCellStyles();
    }

    protected void writeIntoFile() throws Exception {
        outputStream = FileUtils.openOutputStream(new File(fileName));
        workbook.write(outputStream);
    }

    protected void exit() {
        IOUtils.closeQuietly(outputStream);
        if (null != workbook) {
            workbook.dispose();
        }
    }

    protected void customizedLclAlaramCell(String value, double revanueAmmount, double costAmmount) throws IOException {
        createRow();
        resetColumnIndex();
        createHeaderCell(value, cellStyleRightNormalArial10);
        mergeCells(rowIndex, rowIndex, 0, 7);
        createCell();
        createHeaderCell("Revenue : " + "$" + df.format(revanueAmmount), lclCellStyleLeftBoldArial10);
        createCell();
        createCell();
        createHeaderCell("Cost : " + "$" + df.format(costAmmount), lclCellStyleLeftBoldArial10);
    }
        }
