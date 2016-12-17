package com.logiware.excel;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.logiware.utils.LineItemComparator;
import java.io.File;
import java.util.Date;
import java.util.TreeSet;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class JournalEntryExcelCreator extends BaseExcelGenerator {

    private WritableCellFormat mainHeaderCell = null;
    private WritableCellFormat grayHeaderCell = null;

    private void writeContents(JournalEntry journalEntry) throws Exception {
        this.writableSheet = this.writableWorkbook.createSheet(journalEntry.getJournalEntryId(), 0);
        int row = 0;
        this.writableSheet.setColumnView(0, 20);
        this.writableSheet.setColumnView(1, 20);
        this.writableSheet.setColumnView(2, 20);
        this.writableSheet.setColumnView(3, 20);
        this.writableSheet.setColumnView(4, 20);
        this.writableSheet.setColumnView(5, 20);
        this.writableSheet.addCell(new Label(0, row, "Journal Entry ", this.mainHeaderCell));
        this.writableSheet.addCell(new Label(1, row, journalEntry.getJournalEntryId(), this.noBorderCellBlackLeft));
        this.writableSheet.addCell(new Label(2, row, "Description ", this.mainHeaderCell));
        this.writableSheet.mergeCells(3, row, 5, row);
        this.writableSheet.addCell(new Label(3, row, journalEntry.getJournalEntryDesc(), this.noBorderCellBlackLeft));
        row++;
        this.writableSheet.addCell(new Label(0, row, "Period ", this.mainHeaderCell));
        this.writableSheet.addCell(new Number(1, row, Integer.parseInt(journalEntry.getPeriod()), this.noBorderCellBlackLeft));
        this.writableSheet.addCell(new Label(2, row, "Subledger ", this.mainHeaderCell));
        this.writableSheet.addCell(new Label(3, row, journalEntry.getSourceCode().getCode(), this.noBorderCellBlackLeft));
        this.writableSheet.addCell(new Label(4, row, "Memo ", this.mainHeaderCell));
        this.writableSheet.addCell(new Label(5, row, journalEntry.getMemo(), this.noBorderCellBlackLeft));
        row++;
        this.writableSheet.addCell(new Label(0, row, "Debit ", this.mainHeaderCell));
        this.writableSheet.addCell(new Number(1, row, journalEntry.getDebit().doubleValue(), this.noBorderNumberCellBlackLeft));
        this.writableSheet.addCell(new Label(2, row, "Credit ", this.mainHeaderCell));
        this.writableSheet.addCell(new Number(3, row, journalEntry.getCredit().doubleValue(), this.noBorderNumberCellBlackLeft));
        this.writableSheet.addCell(new Label(4, row, "Created Date ", this.mainHeaderCell));
        this.writableSheet.addCell(new Label(5, row, DateUtils.formatDate(new Date(), "MM/dd/yyyy"), this.noBorderCellBlackLeft));
        row++;
        this.writableSheet.addCell(new Label(0, row, "Line Item Number", this.grayHeaderCell));
        this.writableSheet.addCell(new Label(1, row, "GL Account", this.grayHeaderCell));
        this.writableSheet.addCell(new Label(2, row, "Debit", this.grayHeaderCell));
        this.writableSheet.addCell(new Label(3, row, "Credit", this.grayHeaderCell));
        this.writableSheet.addCell(new Label(4, row, "Currency", this.grayHeaderCell));
        this.writableSheet.addCell(new Label(5, row, "", this.grayHeaderCell));
        if ((null != journalEntry.getLineItemSet()) && (!journalEntry.getLineItemSet().isEmpty())) {
            TreeSet<LineItem> lineItems = new TreeSet(new LineItemComparator());
            lineItems.addAll(journalEntry.getLineItemSet());
            for (LineItem lineItem : lineItems) {
                row++;
                this.writableSheet.addCell(new Label(0, row, lineItem.getLineItemId(), this.noBorderCellBlackCenter));
                this.writableSheet.addCell(new Label(1, row, lineItem.getAccount(), this.noBorderCellBlackCenter));
                this.writableSheet.addCell(new Number(2, row, lineItem.getDebit().doubleValue(), this.noBorderNumberCellBlackRight));
                this.writableSheet.addCell(new Number(3, row, lineItem.getCredit().doubleValue(), this.noBorderNumberCellBlackRight));
                this.writableSheet.addCell(new Label(4, row, lineItem.getCurrency(), this.noBorderCellBlackCenter));
                this.writableSheet.addCell(new Label(5, row, "", this.noBorderCellBlackCenter));
            }
        }
    }

    public String exportToExcel(JournalEntry journalEntry) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        fileName.append("/Documents/JournalEntry/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("Journal_Entry_").append(journalEntry.getJournalEntryId()).append(".xls");
        init(fileName.toString());
        WritableFont font12 = createFont(new WritableFont(WritableFont.ARIAL), 12, "BOLD", false, UnderlineStyle.NO_UNDERLINE, Colour.AUTOMATIC);
        this.mainHeaderCell = createCellFormat(font12, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.LEFT);
        this.grayHeaderCell = createCellFormat(this.wfBoldForColumns, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
        this.grayHeaderCell.setBackground(Colour.GRAY_25);
        writeContents(journalEntry);
        write();
        close();
        return fileName.toString();
    }
}