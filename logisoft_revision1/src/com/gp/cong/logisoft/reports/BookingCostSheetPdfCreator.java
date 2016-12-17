package com.gp.cong.logisoft.reports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.reports.dto.SearchBookingReportDTO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class BookingCostSheetPdfCreator extends ReportFormatMethods {

    Document document = null;

    private void initialize(SearchBookingReportDTO searchBookingReportDTO) throws FileNotFoundException, DocumentException {
        String fileName = searchBookingReportDTO.getFileName();
        document = new Document(PageSize.A4);
        document.setMargins(30, 30, 10, 5);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }

    private void createBody(SearchBookingReportDTO searchBookingReportDTO) throws DocumentException, MalformedURLException, IOException {
        BookingFcl bookingFcl = new BookingFcl();
        String contextPath = searchBookingReportDTO.getContextPath();
        bookingFcl = searchBookingReportDTO.getBookingflFcl();
        List costSheetFieldList = searchBookingReportDTO.getObjectList();
        List costEquipmentList = searchBookingReportDTO.getObjectList1();
        //image table
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
        cell = makeCellLeftNoBorder(getCurrentTime());
        dateTable.addCell(cell);
        cell.addElement(dateTable);
        table.addCell(cell);

        // table for heading
        PdfPTable heading1 = makeTable(1);
        heading1.setWidthPercentage(100);
        heading1.addCell(makeCellCenterForDoubleHeading("COST SHEET"));
        //document.add(table);
        //document.add(heading1);
        // table for booking details
        PdfPTable bookingDetails = makeTable(5);
        bookingDetails.setWidthPercentage(100);
        bookingDetails.setWidths(new float[]{18, 28, 8, 18, 28});

        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);
        bookingDetails.addCell(makeCellleftNoBorder("File #"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getFileNo().toString()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));

        bookingDetails.addCell(makeCellleftNoBorder("Bkg Date"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getBookingDate()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Contract/ref"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getContractReference()));

        bookingDetails.addCell(makeCellleftwithBottomBorder("Booked By"));
        bookingDetails.addCell(makeCellleftwithBottomBorderValue(bookingFcl.getUsername()));
        bookingDetails.addCell(makeCellleftwithBottomBorder(""));
        bookingDetails.addCell(makeCellleftwithBottomBorder("Booking Rep"));
        bookingDetails.addCell(makeCellleftwithBottomBorderValue(bookingFcl.getSSLineBookingRepresentative()));




        bookingDetails.addCell(makeCellleftNoBorder("Shipper"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getShipper()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Booking#"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getBookingNumber()));

        bookingDetails.addCell(makeCellleftNoBorder("Contact"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("SS LINE"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getSslname()));

        bookingDetails.addCell(makeCellleftNoBorder("Address"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddress()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Tel"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getTelNo()));

        bookingDetails.addCell(makeCellleftNoBorder("Phone"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getshipperPhone()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Load Date"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getLoadDate()));

        bookingDetails.addCell(makeCellleftNoBorder("Forwarder"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getForward()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Trucker"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getName()));

        bookingDetails.addCell(makeCellleftNoBorder("Contact"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Phone"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getForwarderPhone()));

        bookingDetails.addCell(makeCellleftNoBorder("Address"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getAddressforForwarder()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));

        bookingDetails.addCell(makeCellleftNoBorder("Phone"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getForwarderPhone()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));

        bookingDetails.addCell(makeCellleftNoBorder("Pick-Up Empty"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getEmptyPickUpDate()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));

        bookingDetails.addCell(makeCellleftNoBorder("Email"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getTruckerEmail()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));

        bookingDetails.addCell(makeCellleftNoBorder("Origin"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getOriginTerminal()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Earliest Pick-Up"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getEarliestPickUpDate()));

        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Return"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getLoadDate()));

        if (bookingFcl.getPortofOrgin() != null && bookingFcl.getRampCity() != null && bookingFcl.getRampCity().replace("/ ", "/").trim().equals(bookingFcl.getPortofOrgin().
                replace("/ ", "/").trim())) {
            bookingDetails.addCell(makeCellleftNoBorder("POL"));
            bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getPortofOrgin()));
        } else {
            if (bookingFcl.getRampCity() != null && !bookingFcl.getRampCity().equalsIgnoreCase("")) {
                bookingDetails.addCell(makeCellleftNoBorder("Ramp City"));
                bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getRampCity()));
            } else {
                bookingDetails.addCell(makeCellleftNoBorder(""));
                bookingDetails.addCell(makeCellleftNoBorder(""));
            }
        }
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Earliest Return"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getEarliestPickUpDate()));

        if (bookingFcl.getPortofOrgin() != null && bookingFcl.getRampCity() != null && !bookingFcl.getRampCity().replace("/ ", "/").trim().equals(bookingFcl.getPortofOrgin().
                replace("/ ", "/").trim())) {
            bookingDetails.addCell(makeCellleftNoBorder("POL"));
            bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getPortofOrgin()));
        } else {
            bookingDetails.addCell(makeCellleftNoBorder(""));
            bookingDetails.addCell(makeCellleftNoBorder(""));
        }
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Vessel"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getVessel()));

        if (bookingFcl.getPortofDischarge() != null && bookingFcl.getDestination() != null && bookingFcl.getDestination().replace("/ ", "/").trim().equalsIgnoreCase(bookingFcl.getPortofDischarge().replace("/ ", "/").trim())) {

            bookingDetails.addCell(makeCellleftNoBorder("Dest"));
            bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getDestination()));
        } else {
            bookingDetails.addCell(makeCellleftNoBorder("POD"));
            bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getPortofDischarge()));
        }
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Doc Cut"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getVoyDocCutOff()));

        if (bookingFcl.getPortofDischarge() != null && bookingFcl.getDestination() != null && !bookingFcl.getDestination().replace("/ ", "/").trim().equalsIgnoreCase(bookingFcl.getPortofDischarge().replace("/ ", "/").trim())) {
            bookingDetails.addCell(makeCellleftNoBorder("Dest"));
            bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getDestination()));
        } else {
            bookingDetails.addCell(makeCellleftNoBorder(""));
            bookingDetails.addCell(makeCellleftNoBorder(""));
        }
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("Cut Off"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getPortCutOff()));

        if (!costEquipmentList.isEmpty()) {
            int i = 0;
            SearchBookingReportDTO searchbookingFieldDTO = new SearchBookingReportDTO();
            int size = costEquipmentList.size();
            while (size > 0) {
                searchbookingFieldDTO = (SearchBookingReportDTO) costEquipmentList.get(i);
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
            bookingDetails.addCell(makeCellleftNoBorder("Equipment"));
            bookingDetails.addCell(makeCellLeftNoBorderValue(""));
            bookingDetails.addCell(makeCellleftNoBorder(""));
            bookingDetails.addCell(makeCellleftNoBorder("Amount"));
            bookingDetails.addCell(makeCellLeftNoBorderValue(""));
        }
        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        bookingDetails.addCell(cell);

        bookingDetails.addCell(makeCellleftNoBorder("Commodity"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getComdesc()));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("ETD"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getEtd()));

        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder(""));
        bookingDetails.addCell(makeCellleftNoBorder("ETA"));
        bookingDetails.addCell(makeCellLeftNoBorderValue(bookingFcl.getEta()));

        //charges table
        PdfPTable chargesTable = makeTable(3);
        chargesTable.setWidthPercentage(100);
        chargesTable.setWidths(new float[]{40, 30, 30});
        chargesTable.addCell(makeCellLeftWithGreyBackgroundBold("Charges"));
        chargesTable.addCell(makeCellCenterForDoubleHeadingWithBacgGround("Sell Rate"));
        chargesTable.addCell(makeCellCenterForDoubleHeadingWithBacgGround("Buy Rate"));
        /*	 int count=0;
        SearchBookingReportDTO searchbookingFieldDTO = new SearchBookingReportDTO();
        if(!costSheetFieldList.isEmpty()){
        int size=costSheetFieldList.size();
        while(size>0){
        searchbookingFieldDTO=(SearchBookingReportDTO)costSheetFieldList.get(count);
        chargesTable.addCell(makeCellleftNoBorder(searchbookingFieldDTO.getFieldName()));
        chargesTable.addCell(makeCellRightNoBorder(searchbookingFieldDTO.getSellRate()));
        chargesTable.addCell(makeCellRightNoBorder(searchbookingFieldDTO.getBuyRate()));
        if(size==1){
        chargesTable.addCell(makeCellLeftWithGreyBackgroundBold("TOTAL"));
        chargesTable.addCell(makeCellCenterForDoubleHeadingWithBacgGround(searchbookingFieldDTO.getSellRateTotal()));
        chargesTable.addCell(makeCellCenterForDoubleHeadingWithBacgGround(searchbookingFieldDTO.getBuyRateTotal()));
        }
        count++;
        size--;
        }
        }
         */
        NumberFormat numformat = new DecimalFormat("##,###,##0.00");
        List chargesList = new ArrayList();
        chargesList = searchBookingReportDTO.getChargesList();
        Double totalSellRate = 0.00;
        Double totalBuyRate = 0.00;
        for (Iterator iter = chargesList.iterator(); iter.hasNext();) {
            BookingfclUnits element = (BookingfclUnits) iter.next();
            if (element.getApproveBl() != null && element.getApproveBl().equalsIgnoreCase("Yes")) {
                chargesTable.addCell(makeCellleftNoBorder(element.getChgCode()));
                Double tempSellRate = element.getAmount() + element.getMarkUp();
                chargesTable.addCell(makeCellRightNoBorder(numformat.format(tempSellRate)));
                totalSellRate = totalSellRate + (null != tempSellRate ? tempSellRate : 0.00);
                chargesTable.addCell(makeCellRightNoBorder(numformat.format(null != element.getAmount() ? element.getAmount() : 0.00)));
                totalBuyRate = totalBuyRate + (null != element.getAmount() ? element.getAmount() : 0.00);
            }
        }
        List otherChargesList = new ArrayList();
        otherChargesList = searchBookingReportDTO.getOtherChargesList();
        for (Iterator iter = otherChargesList.iterator(); iter.hasNext();) {
            BookingfclUnits element = (BookingfclUnits) iter.next();

            chargesTable.addCell(makeCellleftNoBorder(element.getChgCode()));
            chargesTable.addCell(makeCellRightNoBorder(numformat.format(null != element.getSellRate() ? element.getSellRate() : 0.00)));
            totalSellRate = totalSellRate + (null != element.getSellRate() ? element.getSellRate() : 0.00);
            chargesTable.addCell(makeCellRightNoBorder(numformat.format(null != element.getAmount() ? element.getAmount() : 0.00)));
            totalBuyRate = totalBuyRate + (null != element.getAmount() ? element.getAmount() : 0.00);


        }
        chargesTable.addCell(makeCellLeftWithGreyBackgroundBold("TOTAL"));
        chargesTable.addCell(makeCellCenterForDoubleHeadingWithBacgGround(numformat.format(null != totalSellRate ? totalSellRate : 0.00)));
        chargesTable.addCell(makeCellCenterForDoubleHeadingWithBacgGround(numformat.format(null != totalBuyRate ? totalBuyRate : 0.00)));
        //Profit Table
        PdfPTable profitTable = makeTable(5);
        profitTable.setWidthPercentage(100);
        profitTable.setWidths(new float[]{18, 28, 8, 18, 28});
        profitTable.addCell(makeCellleftNoBorder("PROFIT"));
        profitTable.addCell(makeCellLeftNoBorderValue(""));
        profitTable.addCell(makeCellleftNoBorder(""));
        profitTable.addCell(makeCellleftNoBorder("Trucker"));
        profitTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getName()));

        profitTable.addCell(makeCellleftNoBorder(""));
        profitTable.addCell(makeCellleftNoBorder(""));
        profitTable.addCell(makeCellleftNoBorder(""));
        profitTable.addCell(makeCellleftNoBorder("Contract Holder"));
        profitTable.addCell(makeCellLeftNoBorderValue(""));

        profitTable.addCell(makeCellleftNoBorder(""));
        profitTable.addCell(makeCellleftNoBorder(""));
        profitTable.addCell(makeCellleftNoBorder(""));
        profitTable.addCell(makeCellleftNoBorder("Brokerage"));
        profitTable.addCell(makeCellLeftNoBorderValue(""));

        document.add(table);
        document.add(heading1);
        document.add(bookingDetails);
        document.add(chargesTable);
        document.add(profitTable);
    }

    public void destroy() {
        document.close();
    }

    public String createReport(SearchBookingReportDTO searchBookingReportDTO)throws Exception {
            this.initialize(searchBookingReportDTO);
            this.createBody(searchBookingReportDTO);
            this.destroy();
        return searchBookingReportDTO.getFileName();
    }
}
