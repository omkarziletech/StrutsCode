package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import org.apache.struts.util.MessageResources;
import org.apache.log4j.Logger;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;
import com.lowagie.text.pdf.BaseFont;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AuthorityToMakeEntryPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(AuthorityToMakeEntryPdfCreator.class);
    Document document = null;
    PdfWriter pdfWriter = null;
    public static MessageResources messageResources = null;
    private FclBl fclBl = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(cell);
        } else if (null != bl && bl.getBrand().equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
            Image img = Image.getInstance(realPath + econoPath);
            pdfPTable.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            cell.addElement(new Chunk(img, 180, -20));
            img.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(img);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(cell);
        } else if (null != bl && bl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            Image img = Image.getInstance(realPath + path);
            pdfPTable.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            cell.addElement(new Chunk(img, 180, -20));
            img.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(img);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(cell);
        }
        String companyName = "";

        if (null != bl && bl.getBrand().equalsIgnoreCase("Econo") && ("03").equals(companyCode)) {
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");

        } else if (null != bl && bl.getBrand().equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");

        } else if (null != bl && bl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");

        }

        cell = makeCellCenterWithBoldFontUnderline("AUTHORITY TO MAKE ENTRY");
        cell.setBorder(0);
        cell.setPaddingTop(30f);
        cell.setPaddingBottom(20f);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        PdfPTable pdfInnerTable = makeTable(4);
        pdfInnerTable.setWidths(new float[]{40, 5, 8, 25});
        pdfInnerTable.setWidthPercentage(90);
        PdfPCell pdfInnerCell = new PdfPCell();
        Date date = new Date();
        fclBl = new FclBlDAO().getOriginalBl(bl.getFileNo());
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Customs Collection District no.4909");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("DATE:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(dateFormat.format(date), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Port of San Juan, Puerto Rico");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerCell.setColspan(3);
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(3);
        pdfInnerTable.setWidths(new float[]{30, 25, 30});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Imported at San Juan,Puerto Rico on");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(dateFormat.format(fclBl.getEta()), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(4);
        pdfInnerTable.setWidths(new float[]{8, 30, 5, 25});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Vessel:");
        pdfInnerTable.addCell(pdfInnerCell);
        String vessel = fclBl.getVessel() != null ? fclBl.getVessel().getCodedesc() : fclBl.getManualVesselName();
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(vessel + " V. " + fclBl.getVoyages(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        BillOfLaddingPdfCreator cityName = new BillOfLaddingPdfCreator();
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(cityName.removeUnlocCodeAppendCountryName(fclBl.getTerminal()), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(3);
        pdfInnerTable.setWidths(new float[]{8, 30, 30});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Shipped By:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(fclBl.getShipperName(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont(", consigned to "+ " " +companyName);
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(3);
        pdfInnerTable.setWidths(new float[]{9, 30, 30});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Endorsed To:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(fclBl.getConsigneeName(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(3);
        pdfInnerTable.setWidths(new float[]{9, 30, 30});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Master B/L #:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(fclBl.getNewMasterBL(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(3);
        pdfInnerTable.setWidths(new float[]{9, 30, 30});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("House B/L #:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(fclBl.getImportAMSHouseBl(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(3);
        pdfInnerTable.setWidths(new float[]{5, 10, 30});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Dated:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(dateFormat.format(fclBl.getSailDate()), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("at origin on file with the District Director of the US Customs");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Services at San Juan, Puerto Rico.");
        pdfInnerCell.setColspan(3);
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(3);
        pdfInnerTable.setWidths(new float[]{30, 30, 30});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Marks and Numbers");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Weight");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Description");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(5);
        pdfInnerTable.setWidths(new float[]{15, 5, 15, 5, 40});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List<FclBlContainer> fclBlContainer = new FclBlContainerDAO().getAllContainers(fclBl.getBol().toString());
        if (CommonUtils.isNotEmpty(fclBlContainer)) {
            for (FclBlContainer fclblCon : fclBlContainer) {
                List<FclBlMarks> fclPackage = new FclBlContainerDAO().getPakagesDetails(fclblCon.getTrailerNoId());
                for (FclBlMarks marks : fclPackage) {
                    String code = genericCodeDAO.getDescFromCode(marks.getUom());
                    pdfInnerCell = makeCellCenterNoBorderPalatinoWithUnderline(marks.getNoOfPkgs().toString() + " " + code, palatinoRomanLargeFont);
                    pdfInnerTable.addCell(pdfInnerCell);
                    pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
                    pdfInnerTable.addCell(pdfInnerCell);
                    pdfInnerCell = makeCellCenterNoBorderPalatinoWithUnderline(marks.getNetweightKgs().toString() + "  KGS", palatinoRomanLargeFont);
                    pdfInnerTable.addCell(pdfInnerCell);
                    pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
                    pdfInnerTable.addCell(pdfInnerCell);
                    pdfInnerCell = makeCellCenterNoBorderPalatinoWithUnderline(marks.getDescPckgs(), palatinoRomanLargeFont);
                    pdfInnerTable.addCell(pdfInnerCell);
                }
            }
        }
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(1);
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
     
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("We," +companyName+ "The consignee in the above mentioned document covering");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("Merchandise for the ultimate consignee, here by authorize");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(2);
        pdfInnerTable.setWidths(new float[]{40, 40});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline(fclBl.getConsigneeName(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("to make Customs entry for the");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("above described merchandise.");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(5);
        pdfInnerTable.setWidths(new float[]{8, 6, 14, 5, 25});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("ECI REF #");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline("04-" + fclBl.getFileNo(), palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("BY:");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        cell.setBorder(0);
        cell.addElement(pdfInnerTable);
        pdfPTable.addCell(cell);

        cell = new PdfPCell();
        pdfInnerTable = makeTable(2);
        pdfInnerTable.setWidths(new float[]{24, 25});
        pdfInnerTable.setWidthPercentage(90);
        pdfInnerCell = new PdfPCell();
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellLeftNoBorderPalatinoWithUnderline("", palatinoRomanLargeFont);
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont("");
        pdfInnerTable.addCell(pdfInnerCell);
        pdfInnerCell = makeCellleftNoBorderWithBoldFont(companyName);
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

    public String createAuthorityToMakeEntryReport(FclBl bl, String fileName, String contextPath, MessageResources messageResources) throws Exception {
        try {
            AuthorityToMakeEntryPdfCreator.messageResources = messageResources;
            this.initialize(fileName, bl);
            this.createBody(bl, fileName, contextPath, messageResources);
            this.destroy();
        } catch (Exception e) {
            log.info("CreateAuthorityToMakeEntryReport failed on" + new Date(), e);
            throw new ExceptionConverter(e);
        }
        return "fileName";
    }
}
