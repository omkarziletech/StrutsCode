package com.gp.cong.logisoft.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.logiware.common.dao.PropertyDAO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

public class VGMDeclarationReport extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(VGMDeclarationReport.class);
    Document document = null;
    PdfWriter pdfWriter = null;
    private PdfPTable table = null;
    public static MessageResources messageResources = null;
    public FclBl bl = null;

    public VGMDeclarationReport(FclBl bl) throws Exception {
        this.bl = bl;
    }

    public VGMDeclarationReport() {
    }

    public void initialize(String fileName, FclBl bl) throws FileNotFoundException, DocumentException, Exception {
        document = new Document(PageSize.A4);
        document.setMargins(4, 4, 4, 4);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }

    public PdfPTable createBody(FclBl bl, FclBlContainer fclBlContainer, String fileName, String contextPath) throws MalformedURLException, IOException, DocumentException, Exception {
        FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
        String carrierName = bl.getSslineName();
        String bookingNo = bl.getBookingNo();
        String verificationSignature = "";
        String verificationDate = "";

        table = new PdfPTable(2);
        table.setWidths(new float[]{2f, 5f});
        PdfPCell cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingTop(25f);
        Paragraph p = new Paragraph(5f, "VERIFIED GROSS MASS DECLARATION", blackBoldFontForHeadingFcLBL);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        String brand = null != bl.getBrand() ? bl.getBrand() : "";
        String companyLogo = new PropertyDAO().getProperty(brand.equalsIgnoreCase("Econo") ? "application.image.econo.logo" : "application.image.logo");

        cell = new PdfPCell();
        Image img = Image.getInstance(contextPath + companyLogo);
        img.scalePercent(60);
        img.setAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(10f);
        cell.setColspan(2);
        cell.setBorder(0);
        cell.addElement(img);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(15f);
        p = new Paragraph(15f, "Carrier    :", blackBoldFont1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPaddingTop(15f);
        cell.setPaddingLeft(18f);
        cell.setBorder(0);
        p = new Paragraph(15f, "" + carrierName, blackBoldFont1);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Booking#    :", blackBoldFont1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, "" + bookingNo, blackBoldFont1);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Unit#    :", blackBoldFont1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, "" + fclBlContainer.getTrailerNo(), blackBoldFont1);
        cell.addElement(p);
        table.addCell(cell);

        Double totalVGMWeight = 0d;
        List<FclBlMarks> fclBlMarksList = fclBlContainerDAO.getPakagesDetails(fclBlContainer.getTrailerNoId());
        for (Object object : fclBlMarksList) {
            FclBlMarks fclBlMarks = (FclBlMarks) object;
            totalVGMWeight = totalVGMWeight + fclBlMarks.getBottomLineVgmWeightKgs();
        }

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Verified Gross Mass    :", blackBoldFont1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, "" + totalVGMWeight + " KGS", blackBoldFont1);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Verification Signature    :", blackBoldFont1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        if (!fclBlMarksList.isEmpty() && fclBlMarksList.size() > 0) {
            verificationSignature = fclBlMarksList.get(0).getVerificationSignature() != null ? fclBlMarksList.get(0).getVerificationSignature() : "";
            verificationDate = fclBlMarksList.get(0).getVerificationDate() != null ? DateUtils.formatDate(fclBlMarksList.get(0).getVerificationDate(), "dd-MMM-yyyy") : "";
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, verificationSignature, blackBoldFont1);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Verification Date    :", blackBoldFont1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(18f);
        p = new Paragraph(15f, "" + verificationDate, blackBoldFont1);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        p = new Paragraph(5f, "Printed On : " + DateUtils.formatDate(new Date(), "dd-MMM-yyyy HH:mm a"), blackBoldFont1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public void destroy() {
        document.close();
    }

    public String createVgmDeclarationReport(FclBl bl, String fileName, String contextPath) throws Exception {
        try {
            this.initialize(fileName, bl);
            if (bl.getFclcontainer() != null) {
                List fclBlContainerList = new FclBlContainerDAO().getAllContainers(bl.getBol().toString());
                for (Object object : fclBlContainerList) {
                    FclBlContainer fclBlContainer = (FclBlContainer) object;
                    if (null == fclBlContainer.getDisabledFlag() || !fclBlContainer.getDisabledFlag().equalsIgnoreCase("D")) {
                        document.add(this.createBody(bl, fclBlContainer, fileName, contextPath));
                        document.newPage();
                    }
                }
            }
            this.destroy();
        } catch (Exception e) {
            log.info("createVgmDeclarationReport failed on" + new Date(), e);
            throw new ExceptionConverter(e);
        }
        return "fileName";
    }
}
