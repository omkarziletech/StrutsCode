/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.kn.bc;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.lcl.kn.BookingDetail;
import com.gp.cong.logisoft.domain.lcl.kn.BookingEnvelope;
import com.gp.cong.logisoft.domain.lcl.kn.CargoDetail;
import com.gp.cong.logisoft.domain.lcl.kn.PickupDetail;
import com.gp.cong.logisoft.domain.lcl.kn.SailingDetail;
import com.gp.cong.logisoft.hibernate.dao.lcl.kn.BookingEnvelopeDao;
import com.gp.cong.logisoft.lcl.report.LclReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfCell;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author palraj.p
 */
public class PDFUtils extends LclReportFormatMethods {

    public void createPDF(HttpServletRequest request, HttpServletResponse response, String id) {
        try {
            String realPath = request.getSession().getServletContext().getRealPath("/");
            BookingEnvelopeDao bookingEnvelopeDao = new BookingEnvelopeDao();
            String companyLogo = LoadLogisoftProperties.getProperty("application.image.logo");
            String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            //document.setMargins(4, 0, 8, 8);
            BookingEnvelope bkgEnvelope = bookingEnvelopeDao.searchById(Long.parseLong(id));
            PdfPTable table = null;
            PdfPCell cell = null;
            Paragraph nbvalue = null;
            response.setContentType("application/pdf");
            PdfWriter.getInstance(document, new FileOutputStream(reportLocation + "/booking_request.pdf"));
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            table = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell();
            cell1.setBorder(0);
            cell1.setColspan(4);
            Image image = Image.getInstance(realPath + companyLogo);
            image.setAlignment(Element.ALIGN_CENTER);
            image.setAlignment(Element.ALIGN_TOP);
            image.scalePercent(60);
            cell1.addElement(image);
            table.addCell(cell1);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{6f, 6f});
            Font head = FontFactory.getFont("helvetica-bold", 14, BaseColor.WHITE);
            table.addCell(createEmptyCell(0, 2.5f, 2));

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(0);
            nbvalue = new Paragraph(10f, "BOOKING REQUEST", blueBoldFont15);
            nbvalue.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(nbvalue);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(20f, "Created On :", headingblackBoldFont);
            nbvalue.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(nbvalue);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(20f, "" + DateUtils.formatStringDateToAppFormatMMM(bkgEnvelope.getCreatedOn()), blackNormalCourierFont10f);
            nbvalue.setAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(nbvalue);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(20f, "Time :", headingblackBoldFont);
            nbvalue.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(nbvalue);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(20f, "" + DateUtils.formatStringDateToTimeTT(bkgEnvelope.getCreatedOn()), blackNormalCourierFont10f);
            nbvalue.setAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(nbvalue);
            table.addCell(cell);
            table.addCell(createEmptyCell(0, 3f, 2));

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(2);
            cell.setBackgroundColor(new BaseColor(00, 51, 153));
            nbvalue = new Paragraph(9.5f, "Booking Envelope", head);
            nbvalue.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(nbvalue);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph("SenderID", headingblackBoldFont);
            cell.addElement(nbvalue);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(bkgEnvelope.getSenderId(), blackNormalCourierFont10f);
            cell.addElement(nbvalue);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph("ReceiverID", headingblackBoldFont);
            cell.addElement(nbvalue);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(bkgEnvelope.getReceiverId(), blackNormalCourierFont10f);
            cell.addElement(nbvalue);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph("Password", headingblackBoldFont);
            cell.addElement(nbvalue);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(bkgEnvelope.getPassword(), blackNormalCourierFont10f);
            cell.addElement(nbvalue);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph("Type", headingblackBoldFont);
            cell.addElement(nbvalue);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(bkgEnvelope.getType(), blackNormalCourierFont10f);
            cell.addElement(nbvalue);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph("Version", headingblackBoldFont);
            cell.addElement(nbvalue);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(bkgEnvelope.getVersion(), blackNormalCourierFont10f);
            cell.addElement(nbvalue);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph("EnvelopeID", headingblackBoldFont);
            cell.addElement(nbvalue);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            nbvalue = new Paragraph(bkgEnvelope.getEnvelopeId(), blackNormalCourierFont10f);
            cell.addElement(nbvalue);
            table.addCell(cell);
            table.addCell(createEmptyCell(0, 4f, 2));

            for (BookingDetail bkg : bkgEnvelope.getBookingDetailList()) {

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setColspan(2);
                cell.setBackgroundColor(new BaseColor(00, 51, 153));
                nbvalue = new Paragraph(9.5f, "Booking Details", head);
                nbvalue.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("BookingType", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getBkgType(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("BookingDate", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(DateUtils.formatStringDateToAppFormatMMM(bkg.getBkgDate()), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("RequestType", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getRequestType(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("CustomerControlCode", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCustomerControlCode(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("CommunicationReference", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCommunicationReference(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("CustomerReference", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCustomerReference(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("CustomerContact", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCustomerContact(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("CustomerPhone", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCustomerPhone(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("CustomerEmail", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCustomerEmail(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("CFSOrigin", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCfsOrigin(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("CFSDestination", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCfsDestination(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("AmsFlag", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getAmsFlag(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("AesFlag", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getAesFlag(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("ColoadCommodity", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getCoLoadCommodity(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("Remarks", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph((bkg.getRemarks1() != null ? bkg.getRemarks1() : "") + " " + (bkg.getRemarks2() != null ? bkg.getRemarks2() : "") + " "
                        + (bkg.getRemarks3() != null ? bkg.getRemarks3() : "") + " " + (bkg.getRemarks4() != null ? bkg.getRemarks4() : "") + " "
                        + (bkg.getRemarks5() != null ? bkg.getRemarks5() : ""), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("PickupFlag", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getPickupFlag(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph("OncarriageFlag", headingblackBoldFont);
                cell.addElement(nbvalue);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                nbvalue = new Paragraph(bkg.getOncarriageFlag(), blackNormalCourierFont10f);
                cell.addElement(nbvalue);
                table.addCell(cell);
                boolean pickupFalg = true;
                for (PickupDetail pick : bkg.getPickupDetailList()) {
                    table.addCell(createEmptyCell(0, 22f, 2));
                    pickupFalg = false;
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setColspan(2);
                    cell.setBackgroundColor(new BaseColor(00, 51, 153));
                    nbvalue = new Paragraph(9.5f, "PickupDetails", head);
                    nbvalue.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("NameAndAddressLine", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getCombinedAddress(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("CompanyName", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getCompanyName(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Address", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getAddress(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("City", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getCity(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("PostalCode", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getZip(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("StateProvince", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getState(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Country", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getCountry(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Contact", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getContact(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Phone", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getPhone(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Email", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(pick.getEmail(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("PickupDate", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(DateUtils.formatStringDateToAppFormatMMM(pick.getPickupDate()), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("PickupTime", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(DateUtils.formatStringDateToTimeTT(pick.getPickupDate()), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    table.addCell(createEmptyCell(0, 3f, 2));

                }

                for (SailingDetail sail : bkg.getSailingDetailList()) {
                    if (pickupFalg) {
                        table.addCell(createEmptyCell(0, 27.5f, 2));
                    }
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setColspan(2);
                    cell.setBackgroundColor(new BaseColor(00, 51, 153));
                    nbvalue = new Paragraph(9.5f, "Sailing Details", head);
                    nbvalue.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("VesselVoyageID", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(sail.getVesselVoyageId(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("VesselName", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(sail.getVesselName(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("IMONumber", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(sail.getImoNumber(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Voyage", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(sail.getVoyage(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("ETDCFS", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(DateUtils.formatStringDateToAppFormatMMM(sail.getEtd()).toString(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("ETACFS", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(DateUtils.formatStringDateToAppFormatMMM(sail.getEta()).toString(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    table.addCell(createEmptyCell(0, 3f, 2));
                }


                for (CargoDetail cargoDetail : bkg.getCargoDetailList()) {
                    cargoDetail.getHazFlag();
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setColspan(2);
                    cell.setBackgroundColor(new BaseColor(00, 51, 153));
                    nbvalue = new Paragraph(9.5f, "CargoDetails", head);
                    nbvalue.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("CargoID", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(cargoDetail.getCargoId(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("ShippingMarks", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph((cargoDetail.getShippingMarks1() != null ? cargoDetail.getShippingMarks1() : "") + " " + (cargoDetail.getShippingMarks2() != null ? cargoDetail.getShippingMarks2() : "") + " "
                            + (cargoDetail.getShippingMarks3() != null ? cargoDetail.getShippingMarks3() : "") + " " + (cargoDetail.getShippingMarks4() != null ? cargoDetail.getShippingMarks4() : "") + " "
                            + (cargoDetail.getShippingMarks5() != null ? cargoDetail.getShippingMarks5() : ""), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Pieces", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(cargoDetail.getPieces(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Packaging", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(cargoDetail.getPackage1(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Commodity", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph((cargoDetail.getCm1() !=null ? cargoDetail.getCm1() : "")+ " "+ (cargoDetail.getCm2() !=null ? cargoDetail.getCm2() : "")
                            +" "+ (cargoDetail.getCm3() !=null ? cargoDetail.getCm3(): "") +" "+ (cargoDetail.getCm4() !=null ? cargoDetail.getCm4() :""), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Weight", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(cargoDetail.getWeight().toString(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("Volume", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(cargoDetail.getVolume().toString(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("UOM", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(cargoDetail.getUom(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph("HazardousFlag", headingblackBoldFont);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    nbvalue = new Paragraph(cargoDetail.getHazFlag(), blackNormalCourierFont10f);
                    cell.addElement(nbvalue);
                    table.addCell(cell);
                }
                document.add(table);
                document.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(PDFUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
