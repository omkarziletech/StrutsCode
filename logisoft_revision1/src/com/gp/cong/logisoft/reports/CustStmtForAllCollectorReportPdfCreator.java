package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cvst.logisoft.reports.dto.AgingReportPeriodDTO;
import com.gp.cvst.logisoft.struts.action.customerStatementVO;
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

/**
 * @author Pradeep P
 *
 */
public class CustStmtForAllCollectorReportPdfCreator extends ReportFormatMethods {

    Document document = null;
    PdfWriter pdfWriter = null;

    public void initialize(String fileName, List liginList, customerStatementVO statementVO) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(20, 20, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        //Login Details
        String footerName = statementVO.getFooterName();
        String colName = statementVO.getCollector();
        String city = "";
        String state = "";
        String phoneNo = "";
        String email = "";
        if (liginList != null && !liginList.isEmpty() && liginList.size() >= 6) {
            city = (String) liginList.get(2) + ",";
            state = (String) liginList.get(3) + ",";
            phoneNo = (String) liginList.get(4) + ",";
            email = (String) liginList.get(5);
        }
        String collectorName = "CollectorName :" + colName;
        String footerPhone = "  Phone No :" + phoneNo;
        String footerEmail = "  Email :" + email;
        String footerList = collectorName + "" + footerPhone + "" + footerEmail + "\n" + footerName + "\nPage ";
        //RtfTotalPageNumber tot=new RtfTotalPageNumber();
        String totalPages = "";//+pdfWriter.getPageNumber();
        Phrase headingPhrase = new Phrase(footerList, headingFont);
        Phrase headingPhrase1 = new Phrase(totalPages, headingFont);
        HeaderFooter footer = new HeaderFooter(headingPhrase, headingPhrase1);
        footer.setAlignment(Element.ALIGN_CENTER);

        document.setFooter(footer);
        document.open();

    }

    public void createBody(String custNum, String contextPath, List<AgingReportPeriodDTO> searchAllCustomerFieldList, customerStatementVO statementVO, List liginList, String username) throws DocumentException, MalformedURLException, IOException {
        String ageingzeero = statementVO.getAgingzeero();
        String ageingthirty = statementVO.getAgingthirty();
        String greaterthanthirty = statementVO.getGreaterthanthirty();
        String agingsixty = statementVO.getAgingsixty();
        String greaterthansixty = statementVO.getGreaterthansixty();
        String agingninty = statementVO.getAgingninty();
        String greaterthanninty = statementVO.getGreaterthanninty();
        String overdue = statementVO.getOverdue();
        String minamt = statementVO.getMinamt();
        String terminal = statementVO.getTerminal();
        String collector = statementVO.getCollector();
        String company = statementVO.getCompany();
        String subject = statementVO.getSubject();
        String textArea = statementVO.getTextArea();
        String allcollectors = statementVO.getAllcollectors();
        AgingReportPeriodDTO agingReportDTO = new AgingReportPeriodDTO();
        agingReportDTO = searchAllCustomerFieldList.get(0);
        String addr1 = "";
        String addr2 = "";
        if (liginList != null && !liginList.isEmpty() && liginList.size() >= 6) {
            addr1 = (String) liginList.get(0) + ",";
        }
        if (liginList != null && !liginList.isEmpty() && liginList.size() >= 6) {
            addr2 = (String) liginList.get(1) + ",";
        }
        AgingReportPeriodDTO agingReportPeriodDTO = new AgingReportPeriodDTO();
        agingReportPeriodDTO = searchAllCustomerFieldList.get(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        //PLACING LINE SPACE
        PdfPTable emptyRow = makeTable(1);
        PdfPCell emptyCell = makeCellRightNoBorder("");
        emptyRow.addCell(emptyCell);

        Image img = Image.getInstance(contextPath + "/img/logo.jpg");
        //GENERATING IMAGE
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(15);
        PdfPCell celL = new PdfPCell();
        celL.addElement(new Chunk(img, 1, -22));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(celL);
        PdfPCell cell;
        PdfPTable bookTable = new PdfPTable(2);
        bookTable.setWidthPercentage(100);
        bookTable.setWidths(new float[]{50, 50});
        bookTable.getDefaultCell().setPadding(0);
        bookTable.getDefaultCell().setBorderWidth(0.5f);
        bookTable.getDefaultCell().setBorderWidthLeft(0.0f);
        bookTable.getDefaultCell().setBorderWidthRight(0.0f);
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        String heading = "";
        //HEADING WITH BACKGROUND COLOR
        if (allcollectors != null && allcollectors.equals("yes")) {
            heading = "AllCollector Statement";
        } else {
            heading = "Collector Statement";
        }
        Phrase headingPhrase = new Phrase(heading, headingFont1);
        PdfPCell headingCell = new PdfPCell(headingPhrase);
        headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingCell.setVerticalAlignment(Element.ALIGN_TOP);
        headingCell.setPaddingTop(-2);
        headingCell.setPaddingBottom(2);
        headingCell.setBorder(0);
        headingCell.setBackgroundColor(Color.LIGHT_GRAY);
        headingCell.setColspan(2);
        bookTable.addCell(headingCell);


        PdfPTable agingTableRowOne = new PdfPTable(3);
        agingTableRowOne.setWidthPercentage(100);
        agingTableRowOne.setWidths(new float[]{45, 10, 45});
        agingTableRowOne.addCell(makeCellleftNoBorder(username + "\n" + addr1 + "\n" + addr2));
        agingTableRowOne.addCell(makeCellleftNoBorder(""));
        agingTableRowOne.addCell(makeCellleftNoBorder(ReportConstants.ACOUNTNUMBER + "\n" + agingReportDTO.getCustNo()));

        PdfPTable agingTableRowTwo = new PdfPTable(3);
        agingTableRowTwo.setWidthPercentage(100);
        agingTableRowTwo.setWidths(new float[]{45, 10, 45});
        String acctName = agingReportDTO.getCustName();
        if (acctName == null) {
            acctName = "";
        }
        String acctAdd = agingReportDTO.getCustAddress();
        if (acctAdd == null) {
            acctAdd = "";
        }
        agingTableRowTwo.addCell(makeCellleftNoBorder(acctName + "\n" + acctAdd));
        agingTableRowTwo.addCell(makeCellleftNoBorder(""));
        agingTableRowTwo.addCell(makeCellleftNoBorder(ReportConstants.STATEMENTDATE + " " + simpleDateFormat.format(new Date())));

        PdfPTable agingTableRowThree = new PdfPTable(2);
        agingTableRowThree.setWidthPercentage(100);
        agingTableRowThree.setWidths(new float[]{65, 35});
        if (subject == null) {
            subject = "";
        }
        agingTableRowThree.addCell(makeCellleftNoBorder("Subject : " + subject));
        agingTableRowThree.addCell(makeCellleftNoBorder(""));
        if (textArea == null) {
            textArea = "";
        }
        PdfPTable textAreaBox = new PdfPTable(2);
        textAreaBox.setWidthPercentage(100);
        textAreaBox.setWidths(new float[]{95, 5});
        textAreaBox.addCell(makeCellleftNoBorder("Text Area : " + textArea));
        textAreaBox.addCell(makeCellleftNoBorder(""));

        document.add(table);
        document.add(bookTable);
        document.add(emptyRow);
        document.add(agingTableRowOne);
        document.add(emptyRow);
        document.add(agingTableRowTwo);
        document.add(emptyRow);
        document.add(agingTableRowThree);
        document.add(emptyRow);
        document.add(textAreaBox);
        document.newPage();

        PdfPTable agingFielsTableHeading = new PdfPTable(8);
        agingFielsTableHeading.setWidthPercentage(100);
        agingFielsTableHeading.setWidths(new float[]{10, 17, 18, 10, 10, 10, 10, 15});
        agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.INVOICEDATE));
        agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.INVOICENO));
        agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.CUSTOMERREFERENCE));
        agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading(ReportConstants.INVOICEAMOUNT));
        agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading(ReportConstants.PAYMENTORADDJUSTMENT));
        agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading(ReportConstants.BALANCE));
        agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading(ReportConstants.AGING));
        agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.BOOKINGORQUOTENO));

        document.add(table);
        document.add(bookTable);
        document.add(emptyRow);
        document.add(agingTableRowOne);
        document.add(emptyRow);
        document.add(agingTableRowTwo);
        document.add(emptyRow);
        document.add(agingFielsTableHeading);

        PdfPTable agingRangeAmtHeading = new PdfPTable(6);
        agingRangeAmtHeading.setWidthPercentage(100);
        agingRangeAmtHeading.setWidths(new float[]{15, 15, 15, 15, 15, 25});
        agingRangeAmtHeading.addCell(makeCellCenterForDoubleHeading("<" + ageingzeero + "-" + ageingthirty + " Days>"));
        agingRangeAmtHeading.addCell(makeCellCenterForDoubleHeading("<" + greaterthanthirty + "-" + agingsixty + " Days>"));
        agingRangeAmtHeading.addCell(makeCellCenterForDoubleHeading("<" + greaterthansixty + "-" + agingninty + " Days>"));
        agingRangeAmtHeading.addCell(makeCellCenterForDoubleHeading("<" + greaterthanninty + " Days>"));
        agingRangeAmtHeading.addCell(makeCellCenterForDoubleHeading("TOTAL"));
        agingRangeAmtHeading.addCell(makeCellCenterForDouble(""));

        PdfPTable agingFielsTable = new PdfPTable(8);
        agingFielsTable.setWidthPercentage(100);
        agingFielsTable.setWidths(new float[]{10, 17, 18, 10, 10, 10, 10, 15});
        int i = 0;
        String custNo = "";
        int size = searchAllCustomerFieldList.size();
        while (size > 0) {
            agingReportPeriodDTO = searchAllCustomerFieldList.get(i);
            if (!custNo.equals("") && !custNo.equals(agingReportPeriodDTO.getCustNo())) {
                AgingReportPeriodDTO agingReportPeriodDTOForAmount = new AgingReportPeriodDTO();
                agingReportPeriodDTOForAmount = searchAllCustomerFieldList.get(i - 1);
                PdfPTable agingRangeAmt = new PdfPTable(6);
                agingRangeAmt.setWidthPercentage(100);
                agingRangeAmt.setWidths(new float[]{15, 15, 15, 15, 15, 25});
                if (agingReportPeriodDTOForAmount.getAgerangeone() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getAgerangeone()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                if (agingReportPeriodDTOForAmount.getAgerangetwo() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getAgerangetwo()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                if (agingReportPeriodDTOForAmount.getAgerangethree() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getAgerangethree()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                if (agingReportPeriodDTOForAmount.getAgerangefour() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getAgerangefour()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                if (agingReportPeriodDTOForAmount.getTotal() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getTotal()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                agingRangeAmt.addCell(makeCellCenterForDouble(""));
                document.add(agingRangeAmtHeading);
                document.add(agingRangeAmt);
                agingRangeAmt.deleteBodyRows();
                document.newPage();

                PdfPTable agingTableRowOneForNew = new PdfPTable(3);
                agingTableRowOneForNew.setWidthPercentage(100);
                agingTableRowOneForNew.setWidths(new float[]{45, 10, 45});
                agingTableRowOneForNew.addCell(makeCellleftNoBorder(username + "\n" + addr1 + "\n" + addr2));
                agingTableRowOneForNew.addCell(makeCellleftNoBorder(""));
                agingTableRowOneForNew.addCell(makeCellleftNoBorder(ReportConstants.ACOUNTNUMBER + "\n" + agingReportPeriodDTO.getCustNo()));

                PdfPTable agingTableRowTwoForNew = new PdfPTable(3);
                agingTableRowTwoForNew.setWidthPercentage(100);
                agingTableRowTwoForNew.setWidths(new float[]{45, 10, 45});
                String accountName = agingReportPeriodDTO.getCustName();
                if (accountName == null) {
                    accountName = "";
                }
                String acctAddress = agingReportPeriodDTO.getCustAddress();
                if (acctAddress == null) {
                    acctAddress = "";
                }
                agingTableRowTwoForNew.addCell(makeCellleftNoBorder(accountName + "\n" + acctAddress));
                agingTableRowTwoForNew.addCell(makeCellleftNoBorder(""));
                agingTableRowTwoForNew.addCell(makeCellleftNoBorder(ReportConstants.STATEMENTDATE + " " + simpleDateFormat.format(new Date())));
                document.add(table);
                document.add(bookTable);
                document.add(emptyRow);
                document.add(agingTableRowOneForNew);
                document.add(emptyRow);
                document.add(agingTableRowTwoForNew);
                document.add(emptyRow);
                document.add(agingTableRowThree);
                document.add(emptyRow);
                document.add(textAreaBox);
                document.newPage();

                document.add(table);
                document.add(bookTable);
                document.add(emptyRow);
                document.add(agingTableRowOneForNew);
                document.add(emptyRow);
                document.add(agingTableRowTwoForNew);
                document.add(emptyRow);
                document.add(agingFielsTableHeading);
            } //else{
            agingFielsTable.addCell(makeCellleftNoBorder(agingReportPeriodDTO.getDate()));
            agingFielsTable.addCell(makeCellleftNoBorder(agingReportPeriodDTO.getInvoiceNo()));
            agingFielsTable.addCell(makeCellleftNoBorder(agingReportPeriodDTO.getCustRefer()));
            if (agingReportPeriodDTO.getInvoiceamt() != null && !agingReportPeriodDTO.getInvoiceamt().equals("")) {
                agingFielsTable.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTO.getInvoiceamt()));
            } else {
                agingFielsTable.addCell(makeCellCenterForDouble(""));
            }
            if (agingReportPeriodDTO.getPaymentoradjustment() != null && !agingReportPeriodDTO.getPaymentoradjustment().equals("")) {
                agingFielsTable.addCell(makeCellCenterForDouble("$ (" + agingReportPeriodDTO.getPaymentoradjustment() + ")"));
            } else {
                agingFielsTable.addCell(makeCellCenterForDouble(""));
            }
            if (agingReportPeriodDTO.getInvoicebalance() != null && !agingReportPeriodDTO.getInvoicebalance().equals("")) {
                agingFielsTable.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTO.getInvoicebalance()));
            } else {
                agingFielsTable.addCell(makeCellCenterForDouble(""));
            }
            agingFielsTable.addCell(makeCellCenterForDouble(agingReportPeriodDTO.getAgent() + " "));
            agingFielsTable.addCell(makeCellleftNoBorder(agingReportPeriodDTO.getVoyageno()));
            document.add(agingFielsTable);
            agingFielsTable.deleteBodyRows();
            //}
            if (size == 1) {
                AgingReportPeriodDTO agingReportPeriodDTOForAmount = new AgingReportPeriodDTO();
                agingReportPeriodDTOForAmount = searchAllCustomerFieldList.get(i);
                PdfPTable agingRangeAmt = new PdfPTable(6);
                agingRangeAmt.setWidthPercentage(100);
                agingRangeAmt.setWidths(new float[]{15, 15, 15, 15, 15, 25});
                if (agingReportPeriodDTOForAmount.getAgerangeone() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getAgerangeone()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                if (agingReportPeriodDTOForAmount.getAgerangetwo() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getAgerangetwo()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                if (agingReportPeriodDTOForAmount.getAgerangethree() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getAgerangethree()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                if (agingReportPeriodDTOForAmount.getAgerangefour() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getAgerangefour()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                if (agingReportPeriodDTOForAmount.getTotal() != null) {
                    agingRangeAmt.addCell(makeCellCenterForDouble("$ " + agingReportPeriodDTOForAmount.getTotal()));
                } else {
                    agingRangeAmt.addCell(makeCellCenterForDouble(""));
                }
                agingRangeAmt.addCell(makeCellCenterForDouble(""));
                document.add(agingRangeAmtHeading);
                document.add(agingRangeAmt);
                agingRangeAmt.deleteBodyRows();
            }
            custNo = agingReportPeriodDTO.getCustNo();
            i++;
            size--;
        }
    }

    public void destroy() {
        document.close();
    }

    public String createReport(String custNo, String fileName, String contextPath, List<AgingReportPeriodDTO> searchAllCustomerFieldList, List liginList, customerStatementVO statementVO, String username)throws Exception {
            this.initialize(fileName, liginList, statementVO);
            this.createBody(custNo, contextPath, searchAllCustomerFieldList, statementVO, liginList, username);
            this.destroy();
        return "fileName";
    }
}
