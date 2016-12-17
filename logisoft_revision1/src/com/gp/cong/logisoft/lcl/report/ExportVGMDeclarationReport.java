package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.logiware.common.dao.PropertyDAO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

public class ExportVGMDeclarationReport extends LclReportFormatMethods {

    private LclUnitSs lclUnitSs;
    private PdfPTable table = null;

    public ExportVGMDeclarationReport(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;
    }

    public void createPdf(String realPath, String outputFileName)
            throws IOException, DocumentException, SQLException, Exception {
        document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        document.setMargins(5, 5, 8, 8);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(getContent(realPath));
        document.close();
    }

    private PdfPTable getContent(String realPath) throws Exception {
        table = new PdfPTable(2);
        table.setWidths(new float[]{2f, 5f});

        LclSsHeader lclSsHeader = this.lclUnitSs.getLclSsHeader();
        LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclSsHeader.getId(), "V");
        lclSsDetail = lclSsDetail == null ? lclSsHeader.getLclSsDetailList().get(0) : lclSsDetail;

        String carrierName = lclSsDetail != null ? lclSsDetail.getSpAcctNo().getAccountName() : "";
        String bookingNo = null != lclUnitSs.getSpBookingNo() ? lclUnitSs.getSpBookingNo() : "";
        String verififedDate = this.lclUnitSs.getSolasVerificationDate() != null
                ? DateUtils.formatDate(this.lclUnitSs.getSolasVerificationDate(), "dd-MMM-yyyy") : "";
        TradingPartner agent = null != lclSsHeader.getLclSsExports() ? lclSsHeader.getLclSsExports().getExportAgentAcctoNo() : null;
        String agentBrand = null != agent ? agent.getBrandPreference() : "";

        String companyName = new PropertyDAO().getProperty(agentBrand.equalsIgnoreCase("Econo")
                ? "application.Econo.companyname" : "application.ECU.companyname");
        String companyLogo = new PropertyDAO().getProperty(agentBrand.equalsIgnoreCase("Econo")
                ? "application.image.econo.logo" : "application.image.logo");

        PdfPCell cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingTop(25f);
        Paragraph p = new Paragraph(5f, "VERIFIED GROSS MASS DECLARATION", blackBoldFont20);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        Image img = Image.getInstance(realPath + companyLogo);
        img.scalePercent(60);
        img.setAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(10f);
        cell.setColspan(2);
        cell.setBorder(0);
        cell.addElement(img);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingTop(15f);
        p = new Paragraph(5f, "" + companyName.toUpperCase(), blackBoldFont14);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(15f);
        p = new Paragraph(15f, "Carrier    :", arialFontSize10Normal);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPaddingTop(15f);
        cell.setPaddingLeft(18f);
        cell.setBorder(0);
        p = new Paragraph(15f, "" + carrierName, arialFontSize10Normal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Booking#    :", arialFontSize10Normal);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, "" + bookingNo, arialFontSize10Normal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Unit#    :", arialFontSize10Normal);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, "" + this.lclUnitSs.getLclUnit().getUnitNo(), arialFontSize10Normal);
        cell.addElement(p);
        table.addCell(cell);

        BigDecimal dunnageWeight = lclUnitSs.getSolasDunnageWeightKGS() != null ? lclUnitSs.getSolasDunnageWeightKGS() : new BigDecimal("0.00");
        BigDecimal tareWeight = lclUnitSs.getSolasTareWeightKGS() != null ? lclUnitSs.getSolasTareWeightKGS() : new BigDecimal("0.00");
        BigDecimal cargoWeight = lclUnitSs.getSolasCargoWeightKGS() != null ? lclUnitSs.getSolasCargoWeightKGS() : new BigDecimal("0.00");
        BigDecimal totalGrossMass = dunnageWeight.add(tareWeight).add(cargoWeight);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Verified Gross Mass    :", arialFontSize10Normal);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, "" + totalGrossMass.doubleValue() + " KGS", arialFontSize10Normal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Verification Signature    :", arialFontSize10Normal);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, null != this.lclUnitSs.getSolasVerificationSign()
                ? this.lclUnitSs.getSolasVerificationSign() : "", arialFontSize10Normal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Verification Date    :", arialFontSize10Normal);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, "" + verififedDate, arialFontSize10Normal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        p = new Paragraph(5f, "Printed On : " + DateUtils.formatDate(new Date(), "dd-MMM-yyyy hh:mm"), arialFontSize10Normal);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }
}
