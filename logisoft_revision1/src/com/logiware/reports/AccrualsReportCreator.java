package com.logiware.reports;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccrualsReportCreator extends ReportFormatMethods implements java.io.Serializable {

    private static final long serialVersionUID = 90521669795762686L;
    private Document document = null;
    private PdfWriter pdfWriter = null;
    private String contextPath = null;
    private AccrualsForm accrualsForm = null;
    protected PdfTemplate totalNoOfPages = null;
    protected BaseFont fontForFooter = null;
    private static final Logger log = Logger.getLogger(AccrualsReportCreator.class);
    public AccrualsReportCreator() {
    }

    public AccrualsReportCreator(AccrualsForm accrualsForm, String contextPath) {
        this.accrualsForm = accrualsForm;
        this.contextPath = contextPath;
    }

    private void init(AccrualsForm accrualsForm, String contextPath, String fileName) throws Exception {
        this.accrualsForm = accrualsForm;
        this.contextPath = contextPath;
        document = new Document(PageSize.A4.rotate());
        document.setMargins(10, 10, 10, 50);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new AccrualsReportCreator(accrualsForm, contextPath));
        document.open();
        pdfWriter.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter));
    }

    private PdfPTable createHeaderTable() throws Exception {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        PdfPCell logoCell = new PdfPCell(image);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);
        headerTable.addCell(logoCell);
        PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        headerTable.addCell(emptyCell);
        headerTable.addCell(emptyCell);
        String title = "Accruals Report";
        PdfPCell titleCell = makeCell(title, Element.ALIGN_CENTER, Element.ALIGN_TOP, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable cutOffTable = makeTable(1);
        cutOffTable.setWidthPercentage(100);
        cutOffTable.addCell(makeCell("Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell cutOffCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        cutOffCell.addElement(cutOffTable);
        headerTable.addCell(cutOffCell);
        PdfPTable accountDetailsTable = new PdfPTable(2);
        accountDetailsTable.setWidthPercentage(100);
        accountDetailsTable.setWidths(new float[]{50f, 50f});
        if (CommonUtils.isNotEqual(accrualsForm.getCategory(), "0") && CommonUtils.isNotEmpty(accrualsForm.getDocNumber())) {
            PdfPTable searchTable = new PdfPTable(2);
            searchTable.setWidthPercentage(100);
            searchTable.setWidths(new float[]{25f, 75f});
            searchTable.addCell(makeCell("Search by : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
            searchTable.addCell(makeCell(accrualsForm.getCategory(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
            searchTable.addCell(makeCell("Search Value : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
            searchTable.addCell(makeCell(accrualsForm.getDocNumber(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
            PdfPCell searchCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
            searchCell.addElement(searchTable);
            accountDetailsTable.addCell(searchCell);
            accountDetailsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
        } else {
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(accrualsForm.getVendornumber());
            if (null != tradingPartner) {
                PdfPTable customerTable = new PdfPTable(2);
                customerTable.setWidthPercentage(100);
                customerTable.setWidths(new float[]{25f, 75f});
                customerTable.addCell(makeCell("Vendor Name : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
                customerTable.addCell(makeCell(tradingPartner.getAccountName(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
                customerTable.addCell(makeCell("Vendor Number : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
                customerTable.addCell(makeCell(tradingPartner.getAccountno(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
                PdfPCell customerCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
                customerCell.addElement(customerTable);
                accountDetailsTable.addCell(customerCell);
                accountDetailsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
            }
        }
        PdfPCell accountDetailsCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        accountDetailsCell.addElement(accountDetailsTable);
        headerTable.addCell(accountDetailsCell);
        headerTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
        PdfPTable listHeadingTable = new PdfPTable(13);
        listHeadingTable.setWidthPercentage(100);
        listHeadingTable.setSpacingAfter(0.1f);
        listHeadingTable.setSpacingBefore(0.1f);
        listHeadingTable.setWidths(new float[]{19, 11, 10, 11, 7, 8, 10, 7, 16, 10, 10, 10, 6});
        listHeadingTable.addCell(makeCell("Vendor Name", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Vendor#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Invoice#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("BL#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Cost Code", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Reporting Date", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Accrued Amount", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Check Number", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Container#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Dock Receipt", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Voyage#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Operations Contact", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Status", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell listHeadingCell = makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
        listHeadingCell.setBorderWidthTop(.5f);
        listHeadingCell.setBorderWidthBottom(.5f);
        listHeadingCell.setLeading(.5f, .5f);
        listHeadingCell.addElement(listHeadingTable);
        headerTable.addCell(listHeadingCell);
        return headerTable;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        totalNoOfPages = writer.getDirectContent().createTemplate(100, 100);
        totalNoOfPages.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            fontForFooter = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(),e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            document.add(this.createHeaderTable());
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(),e);
        }
    }

    private void writeContents(List<TransactionBean> accruals) throws Exception {
        if (CommonUtils.isNotEmpty(accruals)) {
            PdfPTable listContentsTable = new PdfPTable(13);
            listContentsTable.setWidths(new float[]{19, 11, 10, 11, 7, 8, 10, 7, 16, 10, 10, 10, 6});
            listContentsTable.setSpacingAfter(0.1f);
            listContentsTable.setSpacingBefore(0.1f);
            listContentsTable.setWidthPercentage(100);
            for (TransactionBean accrual : accruals) {
                listContentsTable.addCell(makeCell(StringUtils.left(accrual.getCustomer(), 30), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(accrual.getCustomerNo(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(accrual.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(accrual.getBillofLadding(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(accrual.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(DateUtils.formatDate(accrual.getSailingDate(), "MM/dd/yyyy"), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                Double amount = accrual.getTransactionAmount();
                if (amount < 0) {
                    listContentsTable.addCell(makeCell("(" + NumberUtils.formatNumber(amount).replace("-", "") + ")", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, smallRedFont, Rectangle.BOTTOM));
                } else {
                    listContentsTable.addCell(makeCell(NumberUtils.formatNumber(amount), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                }
                listContentsTable.addCell(makeCell(accrual.getChequenumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(accrual.getContainerNo(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(accrual.getDocReceipt(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(accrual.getVoyage(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(accrual.getContact(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
                String status = accrual.getStatus();
                if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_ASSIGN)) {
                    status = "Assign";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_INACTIVE)) {
                    status = "Inactive";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_IN_PROGRESS)) {
                    status = "In Progress";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_REJECT)) {
                    status = "Reject";
                }
                listContentsTable.addCell(makeCell(status, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, smallBlackFont, Rectangle.BOTTOM));
            }
            document.add(listContentsTable);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        String text = "Page " + writer.getPageNumber() + " of ";
        float textBase = document.bottom() - 20;
        float textSize = fontForFooter.getWidthPoint(text, 12);
        cb.beginText();
        cb.setFontAndSize(fontForFooter, 12);
        cb.setTextMatrix(document.right() / 2, textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(totalNoOfPages, document.right() / 2 + textSize, textBase);
        cb.restoreState();
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        totalNoOfPages.beginText();
        totalNoOfPages.setFontAndSize(fontForFooter, 12);
        totalNoOfPages.setTextMatrix(0, 0);
        totalNoOfPages.showText(String.valueOf(writer.getPageNumber() - 1));
        totalNoOfPages.endText();
    }

    public void destroy() {
        document.close();
    }

    public String createReport(AccrualsForm accrualsForm, List<TransactionBean> accruals, String contextPath) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/AccountPayable/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("Accruals.pdf");
        this.init(accrualsForm, contextPath, fileName.toString());
        this.writeContents(accruals);
        this.destroy();
        return fileName.toString();
    }
}
