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

public class ReferenceRequestPdfCreator extends ReportFormatMethods {

    Document document = null;
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 18, 0, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);

    public void initialize(SearchBookingReportDTO searchBookingReportDTO) throws FileNotFoundException,
            DocumentException {
        String fileName = searchBookingReportDTO.getFileName();
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        PdfWriter.getInstance(document,
                new FileOutputStream("c:/reference.pdf"));
        String footerList = "Page No";
        String totalPages = "";
        Phrase headingPhrase = new Phrase(footerList, headingFont);
        Phrase headingPhrase1 = new Phrase(totalPages, headingFont);
        HeaderFooter footer = new HeaderFooter(headingPhrase, headingPhrase1);
        footer.setAlignment(Element.ALIGN_CENTER);

        document.setFooter(footer);
        document.open();
    }

    public void createBody(SearchBookingReportDTO searchBookingReportDTO) throws DocumentException, MalformedURLException, IOException {
        BookingFcl bookingFcl = new BookingFcl();
        String contextPath = searchBookingReportDTO.getContextPath();
        bookingFcl = searchBookingReportDTO.getBookingflFcl();
        List ReferenceFieldList = searchBookingReportDTO.getObjectList();
        // table for company details and logo
        PdfPCell cell;
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
        cell = makeCellRightNoBorder(getCurrentTime());
        dateTable.addCell(cell);
        cell = makeCellleftNoBorder("");
        cell.addElement(dateTable);
        table.addCell(cell);

        // table for heading

        PdfPTable heading = makeTable(1);
        heading.setWidthPercentage(100);
        heading.addCell(makeCellCenterForDoubleHeading("REFERENCE # REQUEST"));

        // reference table
        PdfPTable referenceTable = makeTable(5);
        referenceTable.setWidthPercentage(100);
        referenceTable.setWidths(new float[]{18, 30, 4, 18, 30});
        referenceTable.addCell(makeCellleftNoBorder("File No"));
        cell = makeCellleftNoBorder(":" + bookingFcl.getFileNo().toString());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        referenceTable.addCell(cell);
        referenceTable.addCell(makeCellleftNoBorder("Phone"));
        cell = makeCellLeftNoBorderValue(bookingFcl.getshipperPhone());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        referenceTable.addCell(cell);
        referenceTable.addCell(makeCellleftwithBottomBorder("Fax"));
        cell = makeCellleftwithBottomBorderValue(bookingFcl.getShipperFax());
        cell.setColspan(4);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        referenceTable.addCell(cell);

        referenceTable.addCell(makeCellleftNoBorder("Date"));
        referenceTable.addCell(makeCellLeftNoBorderValue(new Date()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        referenceTable.addCell(makeCellleftNoBorder("ShipperName"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getShipper()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder("shipperAddress"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddressforShipper()));

        referenceTable.addCell(makeCellleftNoBorder("ShipperPhone"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getShipperPhone()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        referenceTable.addCell(cell);

        referenceTable.addCell(makeCellleftNoBorder("ForwarderName"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getForward()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder("ForwarderAddress"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddressforForwarder()));

        referenceTable.addCell(makeCellleftNoBorder("ForwarderPhone"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getForwarderPhone()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        referenceTable.addCell(cell);

        referenceTable.addCell(makeCellleftNoBorder("ConsigneeName"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getConsignee()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder("ConsigneeAddress"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddressforConsingee()));

        referenceTable.addCell(makeCellleftNoBorder("ConsigneePhone"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getConsingeePhone()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        referenceTable.addCell(cell);

        referenceTable.addCell(makeCellleftwithBottomBorder("Company"));
        referenceTable.addCell(makeCellleftwithBottomBorderValue(""));
        referenceTable.addCell(makeCellleftwithBottomBorder(""));
        referenceTable.addCell(makeCellleftwithBottomBorder(""));
        referenceTable.addCell(makeCellleftwithBottomBorder(""));

        referenceTable.addCell(makeCellleftNoBorder("Booking#"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getBookingNumber()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        referenceTable.addCell(makeCellleftNoBorder("Origin"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getOriginTerminal()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        if (bookingFcl.getPortofOrgin() != null && bookingFcl.getRampCity() != null && bookingFcl.getRampCity().replace("/ ", "/").trim().equals(bookingFcl.getPortofOrgin().
                replace("/ ", "/").trim())) {
            referenceTable.addCell(makeCellleftNoBorder("POL"));
            referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getPortofOrgin()));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
        } else {
            if (bookingFcl.getRampCity() != null && !bookingFcl.getRampCity().equalsIgnoreCase("")) {
                referenceTable.addCell(makeCellleftNoBorder("Ramp City"));
                referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getRampCity()));
            } else {
                referenceTable.addCell(makeCellleftNoBorder(""));
                referenceTable.addCell(makeCellleftNoBorder(""));
            }

            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));

            referenceTable.addCell(makeCellleftNoBorder("POL"));
            referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getPortofOrgin()));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
        }

        if (bookingFcl.getPortofDischarge() != null && bookingFcl.getDestination() != null && bookingFcl.getDestination().replace("/ ", "/").trim().equalsIgnoreCase(bookingFcl.getPortofDischarge().replace("/ ", "/").trim())) {
            referenceTable.addCell(makeCellleftNoBorder("Destination"));
            referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getDestination()));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
        } else {
            referenceTable.addCell(makeCellleftNoBorder("POD"));
            referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getPortofDischarge()));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));

            referenceTable.addCell(makeCellleftNoBorder("Destination"));
            referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getDestination()));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
        }


        /*	referenceTable.addCell(makeCellleftNoBorder("Origin"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getOriginTerminal()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        referenceTable.addCell(makeCellleftNoBorder("Address"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddress()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));*/

        /*	referenceTable.addCell(makeCellleftNoBorder("Destination"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getDestination()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));*/

        if (!ReferenceFieldList.isEmpty()) {
            int i = 0;
            SearchBookingReportDTO searchbookingFieldDTO = new SearchBookingReportDTO();
            int size = ReferenceFieldList.size();
            while (size > 0) {
                searchbookingFieldDTO = (SearchBookingReportDTO) ReferenceFieldList.get(i);
                if (i == 0) {
                    referenceTable.addCell(makeCellleftNoBorder(ReportConstants.EQUIPMENT));
                } else {
                    referenceTable.addCell(makeCellleftNoBorder(""));
                }
                referenceTable.addCell(makeCellLeftNoBorderValue(searchbookingFieldDTO.getEquipment()));
                referenceTable.addCell(makeCellleftNoBorder(""));
                if (i == 0) {
                    referenceTable.addCell(makeCellleftNoBorder(ReportConstants.AMT));
                } else {
                    referenceTable.addCell(makeCellleftNoBorder(""));
                }
                referenceTable.addCell(makeCellLeftNoBorderValue(searchbookingFieldDTO.getAmount1()));
                i++;
                size--;
            }
        } else {
            referenceTable.addCell(makeCellleftNoBorder("Equipment"));
            referenceTable.addCell(makeCellLeftNoBorderValue(""));
            referenceTable.addCell(makeCellleftNoBorder(""));
            referenceTable.addCell(makeCellleftNoBorder("Amount"));
            referenceTable.addCell(makeCellLeftNoBorderValue(""));
        }
        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        referenceTable.addCell(cell);

        referenceTable.addCell(makeCellleftNoBorder("Commodity"));
        String com = bookingFcl.getComcode() + " / " + bookingFcl.getComdesc();
        cell = makeCellLeftNoBorderValue(com);
        cell.setColspan(4);
        referenceTable.addCell(cell);


        referenceTable.addCell(makeCellleftNoBorder("SS Line"));
        int index = 0;
        String sslName = "";
        if (bookingFcl.getSslname() != null) {
            index = bookingFcl.getSslname().indexOf("//");
            sslName = bookingFcl.getSslname().substring(0, index);
        }
        String ssl = bookingFcl.getSSLine() + " / " + sslName;
        cell = makeCellLeftNoBorderValue(ssl);
        cell.setColspan(4);
        referenceTable.addCell(cell);



        referenceTable.addCell(makeCellleftNoBorder("Vessel"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getVessel()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        referenceTable.addCell(makeCellleftNoBorder("Port cut"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getPortCutOff()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));


        referenceTable.addCell(makeCellleftNoBorder("Doc cut"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getCutofDate()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder("P/U Equipment"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getEmptyPickUpDate()));

        referenceTable.addCell(makeCellleftNoBorder("ETD"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getEtd()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder("Earliest P/U"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getEarliestPickUpDate()));

        referenceTable.addCell(makeCellleftNoBorder("ETA"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getEta()));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder("Return"));
        referenceTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getLoadDate()));

        cell = makeCellleftwithBottomBorder("");
        cell.setColspan(5);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        referenceTable.addCell(cell);

        /*referenceTable.addCell(makeCellleftwithBottomBorder(""));
        referenceTable.addCell(makeCellleftwithBottomBorder(""));
        referenceTable.addCell(makeCellleftwithBottomBorder(""));
        referenceTable.addCell(makeCellleftwithBottomBorder("Earliest Return"));
        referenceTable.addCell(makeCellleftwithBottomBorderValue(bookingFcl.getEmptyEarliestReturn()));*/

        referenceTable.addCell(makeCellleftNoBorder("Rate Confirmation"));
        referenceTable.addCell(makeCellLeftNoBorderValue(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        referenceTable.addCell(makeCellleftNoBorder("Rate breakdown"));
        referenceTable.addCell(makeCellLeftNoBorderValue(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));
        referenceTable.addCell(makeCellleftNoBorder(""));

        document.add(table);
        document.add(heading);
        document.add(referenceTable);
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
