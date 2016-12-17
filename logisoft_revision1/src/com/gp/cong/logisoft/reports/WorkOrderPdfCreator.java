package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.reports.dto.SearchBookingReportDTO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class WorkOrderPdfCreator extends ReportFormatMethods {

    Document document = null;
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 18, 0, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);

    // public void initialize(String fileName) throws FileNotFoundException,
    // DocumentException {
    public void initialize(SearchBookingReportDTO searchBookingReportDTO) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(30, 30, 10, 5);
        String fileName = searchBookingReportDTO.getFileName();
        // PdfWriter.getInstance(document, new FileOutputStream(fileName));
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        String footerList = "Page No- ";
        String totalPages = "";
        Phrase headingPhrase = new Phrase(footerList, headingFont);
        Phrase headingPhrase1 = new Phrase(totalPages, headingFont);
        HeaderFooter footer = new HeaderFooter(headingPhrase, headingPhrase1);
        footer.setAlignment(Element.ALIGN_CENTER);

        document.setFooter(footer);
        document.open();
    }

    // public void createBody(ArInvoice arInvoice, String contextPath)
    // throws DocumentException, MalformedURLException, IOException {
    public void createBody(SearchBookingReportDTO searchBookingReportDTO) throws DocumentException, MalformedURLException, IOException {
        BookingFcl bookingFcl = new BookingFcl();
        String contextPath = searchBookingReportDTO.getContextPath();
        bookingFcl = searchBookingReportDTO.getBookingflFcl();
        List workOrderFieldList = searchBookingReportDTO.getObjectList();
        PdfPCell cell;
        SearchBookingReportDTO searchbookingDTO = new SearchBookingReportDTO();
        Image img = Image.getInstance(contextPath + "/img/logo.jpg");
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new float[]{76, 25});
        table.setWidthPercentage(100);
        img.scalePercent(20);
        PdfPCell celL = new PdfPCell();
        celL.addElement(new Chunk(img, 2, -10));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);

        table.addCell(celL);

        PdfPTable dateTable = makeTable(2);
        cell = makeCellRightNoBorder("Date   :");
        dateTable.addCell(cell);
        cell = makeCellLeftForDateNoBorder(new Date());
        dateTable.addCell(cell);
        cell = makeCellRightNoBorder("Time   :");
        dateTable.addCell(cell);
        cell = makeCellLeftNoBorder(getCurrentTime());
        dateTable.addCell(cell);
        cell.addElement(dateTable);
        table.addCell(cell);

        // table for heading
        PdfPTable heading1 = makeTable(1);
        heading1.setWidthPercentage(100);
        heading1.addCell(makeCellCenterForDoubleHeading("WORK ORDER"));
        // table for booking details
        PdfPTable bookingDetails = makeTable(5);
        bookingDetails.setWidthPercentage(100);
        bookingDetails.setWidths(new float[]{18, 30, 4, 18, 30});

        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);

        /*bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.ATTNNAME));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getAttenName()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.WORKORDERPHONE));
        bookingDetails.addCell(makeCellLeftNoBorderValue(""));

        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.WORKORDERFAX));
        bookingDetails.addCell(makeCellLeftNoBorderValue(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));*/

        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.SHIPPERNAME));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getShipper()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.SHIPPERADDRESS));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddressforShipper()));

        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.SHIPPERPHONE));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getShipperPhone()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.FORWARDERNAME));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getForward()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.FORWARDERADDRESS));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddressforForwarder()));

        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.FORWARDERPHONE));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getForwarderPhone()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.CONSIGNEENAME));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getConsignee()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.CONSIGNEEADDRESS));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddressforConsingee()));

        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.CONSIGNEEPHONE));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getConsingeePhone()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.TRUCKER));
        cell = makeCellLeftNoBorderValue(bookingFcl.getName());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder("File #"));
        cell = makeCellLeftNoBorderValue(bookingFcl.getFileNo().toString());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.BOOKINGNO));
        cell = makeCellLeftNoBorderValue(bookingFcl.getBookingNumber());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.LOADDATEORTIME));
        cell = makeCellLeftNoBorderValue(bookingFcl.getLoadDate());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        if (!workOrderFieldList.isEmpty()) {
            int i = 0;
            SearchBookingReportDTO searchbookingFieldDTO = new SearchBookingReportDTO();
            int size = workOrderFieldList.size();
            while (size > 0) {
                searchbookingFieldDTO = (SearchBookingReportDTO) workOrderFieldList.get(i);
                if (i == 0) {
                    bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.EQUIPMENT));
                } else {
                    bookingDetails.addCell(makeCellleftNoBorder(""));
                }
                bookingDetails.addCell(makeCellLeftNoBorderValue(searchbookingFieldDTO.getEquipment()));
                bookingDetails.addCell(makeCellleftNoBorder(""));
                if (i == 0) {
                    bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.AMT));
                } else {
                    bookingDetails.addCell(makeCellleftNoBorder(""));
                }
                bookingDetails.addCell(makeCellLeftNoBorderValue(searchbookingFieldDTO.getAmount1()));
                i++;
                size--;
            }
        } else {
            bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.EQUIPMENT));
            bookingDetails.addCell(makeCellleftNoBorder(":"));
            bookingDetails.addCell(makeCellleftNoBorder(""));
            bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.AMT));
            bookingDetails.addCell(makeCellleftNoBorder(":"));
        }

        bookingDetails.addCell(makeCellleftNoBorder("Commodity"));
        String com = bookingFcl.getComcode() + " / " + bookingFcl.getComdesc();
        cell = makeCellLeftNoBorderValue(com);
        cell.setColspan(4);
        bookingDetails.addCell(cell);

        bookingDetails.addCell(makeCellleftNoBorder("SS Line"));
        int index = 0;
        String sslName = "";
        if (bookingFcl.getSslname() != null) {
            index = bookingFcl.getSslname().indexOf("//");
            sslName = bookingFcl.getSslname().substring(0, index);
        }
        String ssl = bookingFcl.getSSLine() + " / " + sslName;
        cell = makeCellLeftNoBorderValue(ssl);
        cell.setColspan(4);
        bookingDetails.addCell(cell);

        /*bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.SSL));
        bookingDetails.addCell(makeCellLeftNoBorderValue(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.SSLNAME));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getSslname()));

        bookingDetails.addCell(makeCellleftwithBottomBorder(ReportConstants.COMMODITY));
        bookingDetails.addCell(makeCellleftwithBottomBorderValue(bookingFcl.getComcode()));
        bookingDetails.addCell(makeCellleftwithBottomBorder(""));
        bookingDetails.addCell(makeCellleftwithBottomBorder(ReportConstants.COMMODITYDESC));
        bookingDetails.addCell(makeCellleftwithBottomBorderValue(bookingFcl.getComdesc()));*/

        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.PUEQUIPMENT));
        cell = makeCellLeftNoBorderValue(bookingFcl.getEmptyPickUpDate());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftwithBottomBorder(ReportConstants.EARLIESTPU));
        cell = makeCellLeftNoBorderValue(bookingFcl.getEarliestPickUpDate());
        cell.setBorderWidthBottom(1);
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftwithBottomBorder(ReportConstants.RETURN));
        cell = makeCellLeftForDateWithColon(bookingFcl.getLoadDate());
        cell.setBorderWidthBottom(1);
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        /*	bookingDetails.addCell(makeCellleftwithBottomBorder(ReportConstants.EARLIESTRETURN));
        cell = makeCellleftwithBottomBorderValue(bookingFcl.getEmptyEarliestReturn());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);*/
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.VESSEL));
        cell = makeCellLeftNoBorderValue(bookingFcl.getVessel());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.DESTINATION));
        cell = makeCellLeftNoBorderValue(bookingFcl.getDestination());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.CUTOFF));
        cell = makeCellLeftNoBorderValue(bookingFcl.getPortCutOff());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftwithBottomBorder(ReportConstants.RATEBREAKDOWN));
        cell = makeCellleftwithBottomBorderValue("");
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.LOADINGADDRESS));
        cell = makeCellLeftNoBorderValue(bookingFcl.getAddress());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.RATE));
        cell = makeCellLeftNoBorderValue("");
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.CONTACT));
        cell = makeCellLeftNoBorderValue(bookingFcl.getContractReference());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftwithBottomBorder(ReportConstants.WORKORDERPHONE));
        cell = makeCellleftwithBottomBorderValue("");
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);

        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.INVOICE));
        cell = makeCellLeftNoBorderValue("pending");
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder(ReportConstants.WORKORDERPHONE));
        cell = makeCellLeftNoBorderValue("");
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);

        bookingDetails.addCell(makeCellleftwithBottomBorder(ReportConstants.WORKORDERFAX));
        cell = makeCellleftwithBottomBorderValue(bookingFcl.getShipperFax());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);

        document.add(table);
        document.add(heading1);
        document.add(bookingDetails);

    }

    public void destroy() {
        document.close();
    }

    public String createReport(SearchBookingReportDTO searchBookingReportDTO)throws Exception {
            this.initialize(searchBookingReportDTO);
            this.createBody(searchBookingReportDTO);
            this.destroy();
        return "fileName";
    }
}
