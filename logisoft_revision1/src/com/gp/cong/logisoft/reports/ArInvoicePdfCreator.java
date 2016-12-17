package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.ArInvoice;
import com.gp.cvst.logisoft.domain.ArinvoiceCharges;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ArInvoicePdfCreator extends ReportFormatMethods {

    Document document = null;
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 18, 0, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);

    public void initialize(String fileName) throws FileNotFoundException,
            DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }

    public void createBody(ArInvoice arInvoice, String contextPath)
            throws DocumentException, MalformedURLException, IOException, Exception {

        // tabel for company details and invoice heading

        PdfPTable invoice = makeTable(2);
        invoice.setWidthPercentage(100);
        invoice.setWidths(new float[]{65, 35});
        // table for company details and logo

        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image img = Image.getInstance(contextPath + imagePath);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(15);
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Chunk(img, -12, 3));
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_TOP);
        cell.setVerticalAlignment(Element.ALIGN_LEFT);

        PdfPTable companyTable = makeTable(1);
        companyTable.getDefaultCell().setBorderWidth(0.0f);
        companyTable.setWidthPercentage(100);
        cell.setColspan(1);
        companyTable.addCell(cell);

        companyTable.addCell(makeCellleftNoBorder("Company Address : " + arInvoice.getCompanyAddress()));
        companyTable.addCell(makeCellleftNoBorder("Company City : " + arInvoice.getCompanyCity() + " State :" + arInvoice.getCompanyState() + " Zip :" + arInvoice.getCompanyZip()));
        companyTable.addCell(makeCellleftNoBorder("Phone : " + arInvoice.getCompanyPhone() + " Fax : " + arInvoice.getCompanyFax()));

        // adding to invoice table

        invoice.addCell(companyTable);

        // table for heading and invoice no and invoice date
        PdfPTable invoiceHeading = makeTable(1);

        PdfPTable invoiceHeading1 = makeTable(1);
        invoiceHeading.addCell(makeCellRightForHeadingLineBottom("INVOICE"));

        PdfPTable invoicedeatils = makeTable(3);
        invoicedeatils.setWidthPercentage(100);
        invoicedeatils.setWidths(new float[]{30, 36, 33});
        cell = makeCellRightNoBorder("");
        cell.setColspan(3);
        cell.setPaddingTop(5);
        invoicedeatils.addCell(cell);
        invoicedeatils.addCell(makeCellRightNoBorder(""));
        invoicedeatils.addCell(makeCellRightNoBorderWithGreyBackground("INVOICE NO.:"));
        invoicedeatils.addCell(makeCellleftNoBorder(arInvoice.getInvoiceNumber()));
        cell = makeCellRightNoBorder("");
        cell.setColspan(3);
        invoicedeatils.addCell(cell);
        invoicedeatils.addCell(makeCellRightNoBorder(""));

        invoicedeatils.addCell(makeCellRightNoBorderWithGreyBackground("INVOICE DATE:"));
        if (arInvoice.getDate() != null) {
            invoicedeatils.addCell(makeCellLeftForDate(arInvoice.getDate()));
        } else {

            invoicedeatils.addCell(makeCellleftNoBorder(""));
        }

        invoiceHeading.addCell(invoiceHeading1);
        invoiceHeading.addCell(invoicedeatils);

        invoice.addCell(invoiceHeading);

        // account number cell
        PdfPTable accountNumberTable = makeTable(2);
        accountNumberTable.setWidthPercentage(100);
        accountNumberTable.setWidths(new float[]{18, 82});
        cell = makeCellLeftNoBorder("");
        cell.setColspan(2);
        cell.setPaddingTop(2f);
        accountNumberTable.addCell(cell);
        cell = makeCellLeftNoBorder("ACCOUNT NUMBER : ");
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        accountNumberTable.addCell(cell);
        cell = makeCellLeftNoBorder(arInvoice.getAccountNumber());
        accountNumberTable.addCell(cell);
        cell = makeCellLeftNoBorder("");
        cell.setColspan(2);
        cell.setPaddingTop(2f);
        accountNumberTable.addCell(cell);

        // table for sold to and For information
        PdfPTable soldTable = makeTable(3);
        soldTable.setWidthPercentage(100);
        soldTable.setWidths(new float[]{48, 4, 48});

        // table for sold to
        PdfPTable soldTo = makeTable(1);
        soldTo.getDefaultCell().setBorder(2);

        soldTo.addCell(makeCellLeftNoBorderWithGreyBackground("SOLD TO:"));
        soldTo.addCell(makeCellLeftWithRightLeftBorder("AR Contact Name :" + arInvoice.getContactName()));
        soldTo.addCell(makeCellLeftWithRightLeftBorder("Customer Name :" + arInvoice.getCustomerName()));
        soldTo.addCell(makeCellLeftWithRightLeftBorder("Customer Invoice Address :" + arInvoice.getAddress()));
        soldTo.addCell(makeCellLeftWithRightLeftBorder("City :" + arInvoice.getInvoiceCity() + " ST :" + arInvoice.getState() + " Zip : " + arInvoice.getZip()));
        soldTo.addCell(makeCellLeftWithRightLeftBottomBorder("Phone :" + arInvoice.getPhoneNumber()));

        // empty table
        PdfPTable empty = makeTable(1);
        empty.addCell(makeCellLeftNoBorder(""));

        // table for Sold For

        PdfPTable forTable = makeTable(1);

        forTable.addCell(makeCellLeftNoBorderWithGreyBackground("For:"));
        forTable.addCell(makeCellLeftWithRightLeftBorder(""));
        forTable.addCell(makeCellLeftWithRightLeftBorder("Bill of Ladings :" + arInvoice.getBlDrNumber()));
        forTable.addCell(makeCellLeftWithRightLeftBottomBorder("Dock Receipts :" + arInvoice.getBlDrNumber()));

        soldTable.addCell(soldTo);
        soldTable.addCell(empty);
        soldTable.addCell(forTable);

        // table for description, rate, quantity and amount
        PdfPCell emptyCell = makeCellLeftNoBorder("");
        emptyCell.setColspan(4);
        emptyCell.setPaddingTop(3);

        PdfPTable arInvoiceChargesDetailsTable = makeTable(4);
        arInvoiceChargesDetailsTable.setWidthPercentage(100);
        arInvoiceChargesDetailsTable.setWidths(new float[]{55, 15, 15, 15});

        arInvoiceChargesDetailsTable.addCell(emptyCell);
        arInvoiceChargesDetailsTable.addCell(makeCellCenter3("CHARGE DESCRIPTION"));
        arInvoiceChargesDetailsTable.addCell(makeCellCenter3("RATE"));
        arInvoiceChargesDetailsTable.addCell(makeCellCenter3("QUANTITY"));
        arInvoiceChargesDetailsTable.addCell(makeCellCenter3("AMOUNT"));

        Set<ArinvoiceCharges> arinvoiceChargesSet = new HashSet<ArinvoiceCharges>();
        arinvoiceChargesSet.addAll(arInvoice.getArinvoiceCharges());
        double amount = 0.0;
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        int lineCount = 0;
        for (Iterator<ArinvoiceCharges> iter = arinvoiceChargesSet.iterator(); iter.hasNext();) {
            ArinvoiceCharges arinvoiceCharges = iter.next();
            arInvoiceChargesDetailsTable.addCell(makeCellleftwithBorderGray(arinvoiceCharges.getChargesCodeDesc()));
            if (arinvoiceCharges.getRate() == null) {
                arinvoiceCharges.setRate(0.0);
            }
            arInvoiceChargesDetailsTable.addCell(makeCellleftwithBorderGray(number.format(arinvoiceCharges.getRate())));
            arInvoiceChargesDetailsTable.addCell(makeCellleftwithBorderGray(arinvoiceCharges.getQuantity()));
            if (arinvoiceCharges.getAmount() == null) {
                arinvoiceCharges.setAmount(0.0);
            }
            arInvoiceChargesDetailsTable.addCell(makeCellrightwithBorderGray(number.format(arinvoiceCharges.getAmount())));
            amount += arinvoiceCharges.getAmount().doubleValue();
            lineCount++;
            if (!iter.hasNext() && lineCount <= 20) {
                while (lineCount < ReportConstants.lINE_COUNT) {

                    arInvoiceChargesDetailsTable.addCell(makeCellleftwithBorderGray(" "));
                    arInvoiceChargesDetailsTable.addCell(makeCellrightwithBorderGray(" "));
                    arInvoiceChargesDetailsTable.addCell(makeCellleftwithBorderGray(" "));
                    arInvoiceChargesDetailsTable.addCell(makeCellrightwithBorderGray(" "));
                    lineCount++;
                }
            }

        }
        PdfPCell cellCol2 = makeCellleftwithBorderGray("");
        cellCol2.setColspan(2);
        arInvoiceChargesDetailsTable.addCell(cellCol2);
        arInvoiceChargesDetailsTable.addCell(makeCellleftwithBorderGray("TOTAL:"));
        arInvoiceChargesDetailsTable.addCell(makeCellrightwithBorderGray(number.format(amount)));

        //
        PdfPTable disclaimer = makeTable(1);
        disclaimer.setWidthPercentage(100);

        PdfPTable disclaimer1 = makeTable(1);
        disclaimer1.setWidthPercentage(100);
        disclaimer1.addCell(makeCellleftNoBorder("Make all checks payable to " + arInvoice.getCompanyName()));

        PdfPTable disclaimer2 = makeTable(2);
        disclaimer2.setWidthPercentage(100);
        disclaimer2.setWidths(new float[]{10, 90});
        disclaimer2.addCell(makeCellLeftWithGreyBackground("TERMS"));
        disclaimer2.addCell(makeCellLeftNoBorder(arInvoice.getTermForPrint()));

        PdfPTable disclaimer3 = makeTable(1);
        disclaimer3.setWidthPercentage(100);
        cell = makeCellCenterWithBoldFont("");
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        disclaimer3.addCell(cell);
        disclaimer3.addCell(makeCellCenterWithBoldFont("Thank you for your business!"));

        disclaimer.addCell(disclaimer1);
        disclaimer.addCell(disclaimer2);
        disclaimer.addCell(disclaimer3);

        document.add(invoice);
        document.add(accountNumberTable);
        document.add(soldTable);
        document.add(arInvoiceChargesDetailsTable);
        document.add(disclaimer);


    }

    public void destroy() {
        document.close();
    }

    private PdfPCell makeCellLeft1(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0.0f);
        return cell;
    }

    private PdfPCell makeCellCenter3(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthTop(0.1f);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthLeft(0.1f);

        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    private PdfPCell makeCellCenter1(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        return cell;
    }

    private PdfPCell makeCellCenter4(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthLeft(0.1f);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        return cell;
    }

    /**
     * @param arInvoice
     * @param fileName
     * @param contextPath
     * @return
     */
    public String createReport(ArInvoice arInvoice, String fileName,
            String contextPath)throws Exception {
            this.initialize(fileName);
            this.createBody(arInvoice, contextPath);
            this.destroy();
        return "fileName";
    }
}
