package com.gp.cong.logisoft.reports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gp.cong.common.CommonConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.struts.form.APPaymentForm;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ApPaymentBatchReportPdfCreator extends ReportFormatMethods {

    Document document = null;

    public void initialize(String fileName) throws FileNotFoundException,
            DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(15, 15, 10, 10);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        String footerList = "Page No";
        String totalPages = "";
        Phrase headingPhrase = new Phrase(footerList, headingFont);
        Phrase headingPhrase1 = new Phrase(totalPages, headingFont);
        HeaderFooter footer = new HeaderFooter(headingPhrase, headingPhrase1);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.open();
    }

    public void createBody(Integer batchId, List<TransactionBean> batchList, APPaymentForm apPaymentForm, String realPath) throws DocumentException, IOException, IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        NumberFormat number = new DecimalFormat("###,###,##0.00");


        // table for company details and logo
        Image img = Image.getInstance(realPath + "/img/logo.jpg");
        PdfPTable imagetable = new PdfPTable(1);
        imagetable.setWidthPercentage(100);
        img.scalePercent(17);
        PdfPCell imageCell = new PdfPCell();
        imageCell.addElement(new Chunk(img, 200, -12));
        imageCell.setBorder(0);
        imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        imageCell.setVerticalAlignment(Element.ALIGN_CENTER);
        imagetable.addCell(imageCell);
        document.add(imagetable);

        PdfPTable emptyTable = new PdfPTable(2);
        emptyTable.setWidthPercentage(100);
        emptyTable.setWidths(new float[]{50, 50});
        emptyTable.getDefaultCell().setPadding(0);
        emptyTable.getDefaultCell().setBorderWidth(0.5f);
        emptyTable.getDefaultCell().setBorderWidthLeft(0.0f);
        emptyTable.getDefaultCell().setBorderWidthRight(0.0f);

        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));
        emptyTable.addCell(makeCellCenterNoBorder(""));

        document.add(emptyTable);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        float colWith = 100 / 8;
        table.setWidths(new float[]{colWith, colWith, colWith, colWith, colWith, colWith, colWith, colWith});

        Phrase titlePhrase = new Phrase("Batch Details Report", headingFont);
        PdfPCell titleCell = makeCellForBatch(titlePhrase, Element.ALIGN_CENTER);
        titleCell.setColspan(6);
        table.addCell(titleCell);

        Date date = new Date();
        Phrase datePhrase = new Phrase("Date: " + simpleDateFormat.format(date), blackBoldHeadingFont);
        PdfPCell dateCell = makeCellForBatch(datePhrase, Element.ALIGN_RIGHT);
        dateCell.setColspan(2);
        table.addCell(dateCell);



        Phrase bankPhrase = new Phrase("Bank Name:  " + apPaymentForm.getBankName(), textFontForBatch);
        PdfPCell bankCell = makeCellForBatch(bankPhrase, Element.ALIGN_CENTER);
        bankCell.setColspan(3);
        table.addCell(bankCell);

        Phrase bankAccountPhrase = new Phrase("Bank Account: " + apPaymentForm.getBankAccountNumber(), textFontForBatch);
        PdfPCell bankAccountCell = makeCellForBatch(bankAccountPhrase, Element.ALIGN_CENTER);
        bankAccountCell.setColspan(3);
        table.addCell(bankAccountCell);

        Phrase batchPhrase = new Phrase("Batch:  " + batchId, textFontForBatch);
        PdfPCell batchCell = makeCellForBatch(batchPhrase, Element.ALIGN_CENTER);
        batchCell.setColspan(2);
        table.addCell(batchCell);


        //add headers
        table.addCell(makeCellForBatch(new Phrase("Vendor Name", headingFont), Element.ALIGN_CENTER));
        table.addCell(makeCellForBatch(new Phrase("Vendor Account", headingFont), Element.ALIGN_CENTER));
        table.addCell(makeCellForBatch(new Phrase("Invoice Number", headingFont), Element.ALIGN_CENTER));
        table.addCell(makeCellForBatch(new Phrase("Amount", headingFont), Element.ALIGN_CENTER));
        table.addCell(makeCellForBatch(new Phrase("Payment Date", headingFont), Element.ALIGN_CENTER));
        table.addCell(makeCellForBatch(new Phrase("Payment Method", headingFont), Element.ALIGN_CENTER));
        table.addCell(makeCellForBatch(new Phrase("Cheque Number", headingFont), Element.ALIGN_CENTER));
        table.addCell(makeCellForBatch(new Phrase("GL Account", headingFont), Element.ALIGN_CENTER));

        Double totalAmount = 0d;
        for (TransactionBean transactionBean : batchList) {
            table.addCell(makeCellWithBorder(transactionBean.getCustomer(), Element.ALIGN_LEFT));
            table.addCell(makeCellWithBorder(transactionBean.getCustomerNo(), Element.ALIGN_LEFT));
            table.addCell(makeCellWithBorder(transactionBean.getInvoiceOrBl(), Element.ALIGN_RIGHT));
            table.addCell(makeCellWithBorder(transactionBean.getAmount(), Element.ALIGN_RIGHT));
            table.addCell(makeCellWithBorder(transactionBean.getTransDate(), Element.ALIGN_RIGHT));
            table.addCell(makeCellWithBorder(transactionBean.getPaymentMethod(), Element.ALIGN_CENTER));
            if (null != transactionBean.getPaymentMethod() && transactionBean.getPaymentMethod().trim().equalsIgnoreCase(CommonConstants.PAYMENT_METHOD_CHECK)) {
                table.addCell(makeCellWithBorder(transactionBean.getChequenumber(), Element.ALIGN_CENTER));
            } else {
                table.addCell(makeCellWithBorder("", Element.ALIGN_CENTER));
            }
            table.addCell(makeCellWithBorder(transactionBean.getGlAcctNo(), Element.ALIGN_CENTER));
            totalAmount += Double.parseDouble(transactionBean.getAmount().replaceAll(",", ""));
        }

        Phrase totalAmountPhrase = new Phrase("Total Amount: $" + number.format(totalAmount), redBoldFont);
        PdfPCell totalAmountCell = makeCell(totalAmountPhrase, Element.ALIGN_CENTER);
        totalAmountCell.setColspan(8);
        table.addCell(totalAmountCell);

        document.add(table);
    }

    public void destroy() {
        document.close();
    }

    public String createBatchReport(Integer batchId, String fileName, List<TransactionBean> batchList, APPaymentForm apPaymentForm, String realPath) throws Exception {
        this.initialize(fileName);
        this.createBody(batchId, batchList, apPaymentForm, realPath);
        this.destroy();
        return fileName;
    }
}
