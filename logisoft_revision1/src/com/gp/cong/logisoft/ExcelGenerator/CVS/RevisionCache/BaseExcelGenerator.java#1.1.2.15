package com.gp.cong.logisoft.ExcelGenerator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.log4j.Logger;

public class BaseExcelGenerator {

    private static final Logger log = Logger.getLogger(BaseExcelGenerator.class);
    protected SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    protected java.text.NumberFormat amountFormat = new DecimalFormat("###,###,##0.00");
    protected WritableWorkbook writableWorkbook = null;
    protected WritableSheet writableSheet = null;
    protected WritableFont wfNoBold = createFont(new WritableFont(WritableFont.ARIAL), 10, "NO_BOLD", false, UnderlineStyle.NO_UNDERLINE, Colour.AUTOMATIC);
    protected WritableFont wfBold = createFont(new WritableFont(WritableFont.ARIAL), 12, "BOLD", false, UnderlineStyle.NO_UNDERLINE, Colour.AUTOMATIC);
    protected WritableFont wfBoldForColumns = createFont(new WritableFont(WritableFont.ARIAL), 10, "BOLD", false, UnderlineStyle.NO_UNDERLINE, Colour.AUTOMATIC);
    protected WritableFont wfNoBoldRed = createFont(new WritableFont(WritableFont.ARIAL), 10, "NO_BOLD", false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
    protected WritableCellFormat cfBold = new WritableCellFormat(this.wfBold);
    protected WritableCellFormat thinBorderCell = createCellFormat(this.wfNoBold, null, Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat thinBorderCellAlignRight = createCellFormat(this.wfNoBold, null, Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.RIGHT);
    protected WritableCellFormat thinBorderCellAlignRightWithRedFont = createCellFormat(this.wfNoBoldRed, null, Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.RIGHT);
    protected WritableCellFormat numberCellFormat = createCellFormat(this.wfNoBold, new jxl.write.NumberFormat("0.00"), Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.RIGHT);
    protected WritableCellFormat headerCell = createCellFormat(this.wfBold, null, Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat headerNoBorderCell = createCellFormat(this.wfBold, null, Border.NONE, BorderLineStyle.NONE, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat headerCellAlignCenter = createCellFormat(this.wfBold, null, Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.CENTRE);
    protected WritableCellFormat columnCellAlignCenter = createCellFormat(this.wfNoBold, null, Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.CENTRE);
    protected WritableCellFormat columnCellAlignCenterWithBorder = createCellFormat(this.wfBold, null, Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.CENTRE);
    protected WritableCellFormat columnHeaderCell = createCellFormat(this.wfBoldForColumns, null, Border.ALL, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.CENTRE);
    protected WritableCellFormat fullBorderCell = createCellFormat(this.wfBoldForColumns, null, Border.ALL, BorderLineStyle.MEDIUM, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat topBorderCell = createCellFormat(this.wfBoldForColumns, null, Border.ALL, BorderLineStyle.MEDIUM, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat bottomBorderCell = createCellFormat(this.wfBoldForColumns, null, Border.BOTTOM, BorderLineStyle.MEDIUM, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat leftBorderCell = createCellFormat(this.wfBoldForColumns, null, Border.LEFT, BorderLineStyle.MEDIUM, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat rightBorderCell = createCellFormat(this.wfBoldForColumns, null, Border.RIGHT, BorderLineStyle.MEDIUM, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat noBorderCell = createCellFormat(this.wfBoldForColumns, null, Border.NONE, BorderLineStyle.NONE, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat noBorderCellBlackLeft = createCellFormat(this.wfNoBold, null, Border.NONE, BorderLineStyle.NONE, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat noBorderCellBlackCenter = createCellFormat(this.wfNoBold, null, Border.NONE, BorderLineStyle.NONE, Colour.AUTOMATIC, true, Alignment.CENTRE);
    protected WritableCellFormat noBorderCellBlackRight = createCellFormat(this.wfNoBold, null, Border.NONE, BorderLineStyle.NONE, Colour.AUTOMATIC, true, Alignment.RIGHT);
    protected WritableCellFormat noBorderCellRedLeft = createCellFormat(this.wfNoBoldRed, null, Border.NONE, BorderLineStyle.NONE, Colour.RED, true, Alignment.LEFT);
    protected WritableCellFormat noBorderCellRedRight = createCellFormat(this.wfNoBoldRed, null, Border.NONE, BorderLineStyle.NONE, Colour.RED, true, Alignment.RIGHT);
    protected WritableCellFormat noBorderNumberCellBlackLeft = createCellFormat(this.wfNoBold, new jxl.write.NumberFormat("0.00"), Border.NONE, BorderLineStyle.NONE, Colour.AUTOMATIC, true, Alignment.LEFT);
    protected WritableCellFormat noBorderNumberCellBlackRight = createCellFormat(this.wfNoBold, new jxl.write.NumberFormat("0.00"), Border.NONE, BorderLineStyle.NONE, Colour.AUTOMATIC, true, Alignment.RIGHT);
    protected WritableCellFormat noBorderNumberCellRedLeft = createCellFormat(this.wfNoBoldRed, new jxl.write.NumberFormat("0.00"), Border.NONE, BorderLineStyle.NONE, Colour.RED, true, Alignment.LEFT);
    protected WritableCellFormat noBorderNumberCellRedRight = createCellFormat(this.wfNoBoldRed, new jxl.write.NumberFormat("0.00"), Border.NONE, BorderLineStyle.NONE, Colour.RED, true, Alignment.RIGHT);
    protected WritableFont font16 = createFont(new WritableFont(WritableFont.ARIAL), 16, BOLD_STYLE_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.AUTOMATIC);
    protected WritableCellFormat noBorderHeaderCell = createCellFormat(font16, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
    protected WritableCellFormat noBoldCellCenter = createCellFormat(wfNoBold, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
    protected WritableCellFormat noBordernoBoldCell = createCellFormat(wfNoBold, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.LEFT);
    protected WritableCellFormat noBorderBoldAlignRight = createCellFormat(wfBoldForColumns, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.RIGHT);
    protected WritableCellFormat noBorderBoldAlignLeft = createCellFormat(wfBoldForColumns, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.LEFT);
    protected WritableCellFormat noBorderNumberCellBoldBlackRight = createCellFormat(wfBoldForColumns, new jxl.write.NumberFormat("0.00"), Border.NONE, BorderLineStyle.NONE, Colour.AUTOMATIC, true, Alignment.RIGHT);
    protected static final String BOLD_STYLE_NO_BOLD = "NO_BOLD";
    protected static final String BOLD_STYLE_BOLD = "BOLD";

    protected void init(String excelFilePath) {
        try {
            File file = new File(excelFilePath);
            this.writableWorkbook = Workbook.createWorkbook(file);
        } catch (IOException e) {
            log.info("init failed on " + new Date(),e);
        }
    }

    protected void write() {
        try {
            this.writableWorkbook.write();
        } catch (IOException e) {
            log.info("write failed on " + new Date(),e);
        }
    }

    protected void close() {
        try {
            this.writableWorkbook.close();
        } catch (WriteException e) {
            log.info("close failed on " + new Date(),e);
        } catch (IOException e) {
            log.info("close failed on " + new Date(),e);
        }
    }

    protected WritableCellFormat createCellFormat(WritableFont writableFont, DisplayFormat displayFormat, Border border, BorderLineStyle borderLineStyle, Colour colour, boolean wrap, Alignment alignment) {
        WritableCellFormat cellFormat = null;
        try {
        if ((null != writableFont) && (null != displayFormat)) {
            cellFormat = new WritableCellFormat(writableFont, displayFormat);
        } else if (null != writableFont) {
            cellFormat = new WritableCellFormat(writableFont);
        } else if (null != displayFormat) {
            cellFormat = new WritableCellFormat(displayFormat);
        } else {
            cellFormat = new WritableCellFormat();
        }
        cellFormat.setBorder(border, borderLineStyle, colour);
        cellFormat.setWrap(wrap);
        cellFormat.setAlignment(alignment);
        cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
        } catch (Exception e) {
            log.info("createCellFormat failed on " + new Date(),e);
        }
        return cellFormat;
    }

    protected WritableFont createFont(Font font, int pointSize, String boldStyle, boolean italic, UnderlineStyle us, Colour colour) {
        WritableFont writableFont = new WritableFont(font);
        try {
        writableFont.setPointSize(pointSize);
        if ((null != boldStyle) && (boldStyle.trim().equals("BOLD"))) {
            writableFont.setBoldStyle(WritableFont.BOLD);
        } else {
            writableFont.setBoldStyle(WritableFont.NO_BOLD);
        }
        writableFont.setItalic(italic);
        writableFont.setUnderlineStyle(us);
        writableFont.setColour(colour);
        } catch (Exception e) {
            log.info("createFont failed on " + new Date(),e);
        }
        return writableFont;
    }
}
