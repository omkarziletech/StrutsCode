package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import java.awt.Color;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import org.apache.struts.util.MessageResources;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.apache.log4j.Logger;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.Image;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ContainerResponsibilityWaiverPdfCreater extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(ContainerResponsibilityWaiverPdfCreater.class);
    Document document = null;
    PdfWriter pdfWriter = null;
    public static MessageResources messageResources = null;
    private FclBl fclBl = null;

    public void initialize(String fileName, FclBl bl) throws FileNotFoundException, DocumentException, Exception {
        document = new Document(PageSize.A4);
        document.setMargins(4, 4, 4, 4);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }

    public void createBody(FclBl bl, String fileName, String contextPath, MessageResources messageResources) throws MalformedURLException, IOException, DocumentException, Exception {
        document.add(lineTable(bl, contextPath));
    }

    public PdfPTable lineTable(FclBl bl, String realPath) throws Exception {
        String path = LoadLogisoftProperties.getProperty("application.image.logo");
        String econoPath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
        String companyCode= new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        BaseFont palationRomanBase = BaseFont.createFont(realPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanLargeFont = new Font(palationRomanBase, 10, Font.NORMAL, Color.BLACK);
        PdfPTable pdfPTable = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        if (null != bl && bl.getBrand().equalsIgnoreCase("Econo") && ("03").equals(companyCode)) {
            Image img = Image.getInstance(realPath + econoPath);
            pdfPTable.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            cell.addElement(new Chunk(img, 180, -20));
            img.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(img);
        } else if (null != bl && bl.getBrand().equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
            Image img = Image.getInstance(realPath + econoPath);
            pdfPTable.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            cell.addElement(new Chunk(img, 180, -20));
            img.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(img);
        } else if (null != bl && bl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            Image img = Image.getInstance(realPath + path);
            pdfPTable.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            cell.addElement(new Chunk(img, 180, -20));
            img.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(img);
        }
       
        
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        pdfPTable.addCell(cell);

        cell = makeCellCenterWithBoldFontUnderline("CONTAINER RESPONSIBILITY WAIVER");
        cell.setBorder(0);
        cell.setPaddingTop(30f);
        cell.setPaddingBottom(20f);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        PdfPTable pdfInnerTable = makeTable(4);
        pdfInnerTable.setWidths(new float[]{5, 15, 5, 15});
        pdfInnerTable.setWidthPercentage(90);
        PdfPCell pdfInnerCell = new PdfPCell();
        DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
        Date date = new Date();
        fclBl = new FclBlDAO().getOriginalBl(bl.getFileNo());
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("DATE:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(df7.format(date), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("ECI REF #:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline("04-" + fclBl.getFileNo(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(30f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(4);
        pdfInnerTable.setWidths(new float[]{5, 15, 5, 15});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("MBL: ");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(fclBl.getNewMasterBL().toUpperCase(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellCenterNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellCenterNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(4);
        pdfInnerTable.setWidths(new float[]{5, 15, 5, 15});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("UNIT(S):");
        pdfInnerTable.addCell(pdfInnerCell);
        List<FclBlContainer> fclBlContainer = new FclBlContainerDAO().getAllContainers(fclBl.getBol().toString());
        if (CommonUtils.isNotEmpty(fclBlContainer)) {
            for (FclBlContainer fclblCon : fclBlContainer) {
                if (!"D".equalsIgnoreCase(fclblCon.getDisabledFlag())) {
                    pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(fclblCon.getTrailerNo(), palatinoRomanLargeFont);
                    pdfInnerTable.addCell(pdfInnerCell);
                    pdfInnerCell = makeCellCenterNoBorderWithBoldFont(" ");
                    pdfInnerTable.addCell(pdfInnerCell);
                }
            }
        } else {
            pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(" ", palatinoRomanLargeFont);
            pdfInnerTable.addCell(pdfInnerCell);
            pdfInnerCell = makeCellCenterNoBorderWithBoldFont(" ");
            pdfInnerTable.addCell(pdfInnerCell);
        }
        pdfInnerCell = makeCellCenterNoBorderWithBoldFont(" ");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        String comments = "Demurrage and Detention charges associated with above mentioned container(s) will be the sole responsibility of the final consignee after the free time expires.\n";
        pdfInnerTable = makeTable(1);
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellLeftNoBorderBold(comments);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("\n");
        pdfInnerTable.addCell(pdfInnerCell);

        String customerContactTel = "Please contact your Customer Service Representative if you have any questions Tel # 787-620-3085";
        pdfInnerCell = makeCellLeftNoBorderBold(customerContactTel);
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(2);
        pdfInnerTable.setWidths(new float[]{15, 70});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Final Consignee:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(fclBl.getConsigneeName(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont(" ");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont(" ");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Name: ");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(" ", palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(3);
        pdfInnerTable.setWidths(new float[]{40, 10, 40});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(" ", palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(" ", palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Signature");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont(" ");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Date");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        return pdfPTable;
    }

    public void destroy() {
        document.close();
    }

    public String createContainerResponseReport(FclBl bl, String fileName, String contextPath, MessageResources messageResources) throws Exception {
        try {
            ContainerResponsibilityWaiverPdfCreater.messageResources = messageResources;
            this.initialize(fileName, bl);
            this.createBody(bl, fileName, contextPath, messageResources);
            this.destroy();
        } catch (Exception e) {
            log.info("CreateContainerResponseReport failed on" + new Date(), e);
            throw new ExceptionConverter(e);
        }
        return "fileName";
    }
}
