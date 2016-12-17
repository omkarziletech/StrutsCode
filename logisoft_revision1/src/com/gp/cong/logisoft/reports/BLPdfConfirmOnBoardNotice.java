package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.MessageResources;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.log4j.Logger;

public class BLPdfConfirmOnBoardNotice extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(BLPdfConfirmOnBoardNotice.class);
    Document document = null;
    PdfWriter pdfWriter = null;
    private String manifestRev = "";
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
    protected PdfTemplate total;
    protected BaseFont helv;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    public static String manifested = "";
    public static String proof = "";

    public BLPdfConfirmOnBoardNotice() {
    }

    public BLPdfConfirmOnBoardNotice(FclBl bl) throws Exception {
	FclBl fclBl = new FclBlDAO().getOriginalBl(bl.getFileNo());
	if (bl.getBolId() != null && bl.getBolId().indexOf("=") != -1) {
	    if (CommonFunctions.isNotNull(fclBl.getCorrectionCount()) && "Yes".equalsIgnoreCase(fclBl.getPrintRev())) {
		if (CommonFunctions.isNotNull(fclBl.getManifestRev())) {
		    int count = fclBl.getCorrectionCount() + Integer.parseInt(fclBl.getManifestRev());
		    manifestRev = "" + count;
		} else {
		    manifestRev = "" + fclBl.getCorrectionCount();
		}
	    }
	} else {
	    if (CommonFunctions.isNotNull(fclBl.getManifestRev()) && "Yes".equalsIgnoreCase(fclBl.getPrintRev())) {
		manifestRev = fclBl.getManifestRev();
	    }
	}
    }

    public void initialize(String fileName, FclBl bl) throws FileNotFoundException, DocumentException, Exception {
	document = new Document(PageSize.A4);
	document.setMargins(10, 10, 30, 30);
	pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	String footerList = "Page No";
	String totalPages = "";
	Phrase headingPhrase = new Phrase(footerList, headingFont);
	Phrase headingPhrase1 = new Phrase(totalPages, headingFont);
	HeaderFooter footer = new HeaderFooter(headingPhrase, headingPhrase1);
	footer.setAlignment(Element.ALIGN_CENTER);
	pdfWriter.setPageEvent(new BLPdfConfirmOnBoardNotice(bl));
	document.open();
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
	total = writer.getDirectContent().createTemplate(100, 100);
	total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
	try {
	    helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
		    BaseFont.NOT_EMBEDDED);
	} catch (Exception e) {
	    throw new ExceptionConverter(e);
	}
    }

    public void createBody(FclBl fclBl, String contextPath, MessageResources messageResources, User user, String result)
	    throws MalformedURLException, IOException, DocumentException, Exception {

	//.....EMPTY Row with top Border...
	PdfPTable emptyRowWithBottomBorder = makeTable(1);
	emptyRowWithBottomBorder.setWidthPercentage(100);
	PdfPCell emptyRowWithBorder = makeCellBorderTopAlignRightBL("");
	emptyRowWithBottomBorder.addCell(emptyRowWithBorder);
	PdfPCell cell = new PdfPCell();
	PdfPCell cell1 = new PdfPCell();
	//PdfPTable pdfPTable = null;

	//...EMPTY TABLE....
	PdfPTable emptyTable = makeTable(1);
	cell = makeCellleftNoBorder("");
	emptyTable.addCell(cell);


	BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
	Font palatinoRomanBigFont = new Font(palationRomanBase, 12, Font.NORMAL, Color.BLACK);
	Font helvSmallFont = new Font(Font.HELVETICA, 7, Font.NORMAL, Color.BLACK);
	//...HEADING TABLE...
	PdfPTable headingTable = makeTable(1);
	headingTable.setWidthPercentage(100);
	/*  if(fclBl.getBolId()!=null && fclBl.getBolId().indexOf("=")!=-1){
	 StringBuilder correctedBl =new StringBuilder();
	 correctedBl.append(fclBl.getBolId()!=null?fclBl.getBolId():"");
	 correctedBl.append(" - This is a corrected BL");
	 cell = makeCellLeftNoBorderPalatinoFclBl(correctedBl.toString(),palatinoRomanSmallFont);
	 cell.setColspan(2);
	 headingTable.addCell(cell);
	 cell = makeCellLeftNoBorderBold("");
	 cell.setColspan(3);
	 headingTable.addCell(cell);
	 }*/

	LoadLogisoftProperties loadLogisoftProperties1 = new LoadLogisoftProperties();
	String path = loadLogisoftProperties1.getProperty("application.image.logo");
        String econoPath = loadLogisoftProperties1.getProperty("application.image.econo.logo");
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        PdfPCell celL = new PdfPCell();
	PdfPTable table = new PdfPTable(1);
        if (null != fclBl && fclBl.getBrand().equals("Econo") && ("03").equals(companyCode)) {
            Image img = Image.getInstance(contextPath + econoPath);
            table.setWidthPercentage(100);
            img.scalePercent(60);
            //img.scaleAbsoluteWidth(200);
            img.setAlignment(Element.ALIGN_CENTER);
            celL.addElement(img);
            celL.setBorder(0);
            celL.setHorizontalAlignment(Element.ALIGN_CENTER);
            celL.setVerticalAlignment(Element.ALIGN_TOP);
            headingTable.addCell(celL);
        } else if (null !=fclBl && fclBl.getBrand().equals("OTI") && ("02").equals(companyCode)) {
            Image img = Image.getInstance(contextPath + econoPath);
            table.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            celL.addElement(new Chunk(img, 185, -5));
            img.setAlignment(Element.ALIGN_CENTER);
            celL.addElement(img);
            celL.setBorder(0);
            celL.setHorizontalAlignment(Element.ALIGN_CENTER);
            celL.setVerticalAlignment(Element.ALIGN_TOP);
            headingTable.addCell(celL);
        } else if (null != fclBl && fclBl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            Image img = Image.getInstance(contextPath + path);
            table.setWidthPercentage(100);
            img.scalePercent(60);
            // img.scaleAbsoluteWidth(200);
            img.setAlignment(Element.ALIGN_CENTER);
            celL.addElement(img);
            //celL.addElement(new Chunk(img, 185, -5));
            celL.setBorder(0);
            celL.setHorizontalAlignment(Element.ALIGN_CENTER);
            celL.setVerticalAlignment(Element.ALIGN_TOP);
            headingTable.addCell(celL);
        }
        
	document.add(headingTable);

	document.add(emptyTable);
	document.add(emptyTable);

	PdfPTable headingTable1 = makeTable(1);
	headingTable1.setWidthPercentage(100);
	String companyPhone = new SystemRulesDAO().getSystemRuleNameByRuleCode("CompanyPhone");
	String companyFax = new SystemRulesDAO().getSystemRuleNameByRuleCode("CompanyFax");
	String heading1 = companyPhone + " " + companyFax;
	Phrase headingPhrase1 = new Phrase(heading1, blackBoldFont);
	PdfPCell headingCell1 = new PdfPCell(headingPhrase1);
	headingCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	headingCell1.setVerticalAlignment(Element.ALIGN_TOP);
	headingCell1.setPaddingTop(1);
	headingCell1.setPaddingBottom(2);
	headingCell1.setBorder(0);
	headingTable1.addCell(headingCell1);

	document.add(headingTable1);

	PdfPTable headingTable2 = makeTable(1);
	headingTable2.setWidthPercentage(100);
        String companyWebsite="";
        if (null != fclBl && fclBl.getBrand().equals("Econo") && ("03").equals(companyCode)) {
            companyWebsite=loadLogisoftProperties1.getProperty("application.Econo.website");
        }else if (null != fclBl && fclBl.getBrand().equals("OTI") && ("02").equals(companyCode)){
            companyWebsite=loadLogisoftProperties1.getProperty("application.Oti.website");
        } else if (null != fclBl && fclBl.getBrand().equalsIgnoreCase("Ecu Worldwide")){
            companyWebsite=loadLogisoftProperties1.getProperty("application.ECU.website");
        }
	headingPhrase1 = new Phrase(companyWebsite, blackBoldFont);
	headingCell1 = new PdfPCell(headingPhrase1);
	headingCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	headingCell1.setVerticalAlignment(Element.ALIGN_TOP);
	headingCell1.setPaddingTop(1);
	headingCell1.setPaddingBottom(2);
	headingCell1.setBorder(0);
	headingTable2.addCell(headingCell1);
	/* if(fclBl.getBolId()!=null && fclBl.getBolId().indexOf("=")!=-1){
	 cell = makeCellCenterNoBorderPalatinoFclBl("CORRECTED",palatinoRomanBigFont);
	 cell.setColspan(3);
	 headingTable2.addCell(cell);
	 }*/
	document.add(headingTable2);


	document.add(emptyTable);
	document.add(emptyTable);

	//..TITLE TABLE...
	PdfPTable titleTable = makeTable(1);
	titleTable.setWidthPercentage(100);
//        if(!CommonFunctions.isNotNull(fclBl.getReadyToPost()) &&
//        		fclBl.getPreAlert()!=null && fclBl.getPreAlert().equalsIgnoreCase("yes")){
//        	 cell= makeCellleftNoBorderForHeadingFclBL(PrintReportsConstants.PREALTER);
//        	 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        	 titleTable.addCell(cell);
//        }
	titleTable.addCell(makeCellleftwithBottomBorderHeadingFontFclBlConrimOnBoard("F C L  C a r g o  Confirmed On-Board Notification"));
	titleTable.addCell(makeCellRightNoBorderFclBL(""));
	document.add(titleTable);
	document.add(emptyRowWithBottomBorder);


	//..DETAILS TABLE...
	String fileNumber = fclBl.getFileNo();
	if (null != fclBl && null != fileNumber && fileNumber.indexOf("-") != -1) {
	    fileNumber = fileNumber.substring(0, fileNumber.indexOf("-"));
	}
	Quotation quotation = new QuotationDAO().getFileNoObject(fileNumber);
	PdfPTable detailsTable = makeTable(4);
	detailsTable.setWidthPercentage(100);
	detailsTable.setWidths(new float[]{12, 56, 12, 20});
	
	if ("02".equals(companyCode)) {
	    companyCode = "OTI";
	} else {
	    companyCode = "ECI";
	}
	detailsTable.addCell(makeCellLeftNoBorderConfirmeOnBoard("To Name:", false, "left"));
	detailsTable.addCell(makeCellLeftNoBorderConfirmeOnBoard(quotation.getContactname(), false, "left"));//value
	detailsTable.addCell(makeCellLeftNoBorderConfirmeOnBoard(companyCode + " REF.#", true, "left"));
	detailsTable.addCell(makeCellLeftNoBorderConfirmeOnBoard(" " + messageResources.getMessage("fileNumberPrefix") + (fclBl.getFileNo() != null ? fclBl.getFileNo() : ""), true, "left"));
	detailsTable.addCell(makeCellLeftNoBorderConfirmeOnBoard("Company:", false, "left"));
	detailsTable.addCell(makeCellLeftNoBorderConfirmeOnBoard(quotation.getClientname(), false, "left"));// compnay value
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));

	detailsTable.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable.addCell(makeCellRightNoBorderFclBL(""));


	document.add(detailsTable);

	document.add(emptyTable);

	//..2ND DETAILS TABLE...
	PdfPTable detailsTable2 = makeTable(2);
	detailsTable2.setWidthPercentage(100);
	detailsTable2.setWidths(new float[]{14, 87});
	detailsTable2.setHorizontalAlignment(0);
	detailsTable2.addCell(makeCellLeftNoBorderConfirmeOnBoard("CARRIER:", false, "right"));
	detailsTable2.addCell(makeCellLeftNoBorderConfirmeOnBoard((fclBl.getSslineName() != null ? fclBl.getSslineName() : ""), false, "left"));
	detailsTable2.addCell(makeCellLeftNoBorderConfirmeOnBoard("VESSEL:", false, "right"));
	String vessel = fclBl.getVessel() != null ? (fclBl.getVessel().getCodedesc() != null ? fclBl.getVessel().getCodedesc() : "") : "";
	String voyage = fclBl.getVoyages() != null ? fclBl.getVoyages() : "";
	detailsTable2.addCell(makeCellLeftNoBorderConfirmeOnBoard(vessel + "  V." + voyage + "", false, "left"));
	detailsTable2.addCell(makeCellLeftNoBorderConfirmeOnBoard("CONSIGNEE:", false, "right"));
	detailsTable2.addCell(makeCellLeftNoBorderConfirmeOnBoard((fclBl.getConsigneeName() != null ? fclBl.getConsigneeName() : ""), false, "left"));
	detailsTable2.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable2.addCell(makeCellRightNoBorderFclBL(""));
	document.add(detailsTable2);
	document.add(emptyRowWithBottomBorder);

	//...3RD DETAILS TABLE...
	PdfPTable detailsTable3 = makeTable(4);
	detailsTable3.setWidthPercentage(100);
	detailsTable3.setWidths(new float[]{22, 46, 17, 15});
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard("YOUR BOOKING #:", true, "right"));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard(null != fclBl.getBookingNo() ? fclBl.getBookingNo() : "", true, "left", 11));
	detailsTable3.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable3.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard("PORT OF SAILING:", false, "right"));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard(null != fclBl.getPortOfLoading() ? removeUnlocCode(fclBl.getPortOfLoading()) : "", false, "left"));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard("Sailing Date:", false, "left"));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard(" " + sdf.format(null != fclBl.getSailDate() ? fclBl.getSailDate() : new Date()), false, "left"));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard("DESTINATION PORT:", false, "right"));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard(null != fclBl.getFinalDestination() ? removeUnlocCode(fclBl.getFinalDestination()) : "", false, "left"));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard("Arrival Date:", true, "left", 11));
	detailsTable3.addCell(makeCellLeftNoBorderConfirmeOnBoard(sdf.format(null != fclBl.getVerifyETA() ? fclBl.getVerifyETA() : new Date()), true, "left", 11));
	detailsTable3.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable3.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable3.addCell(makeCellRightNoBorderFclBL(""));
	detailsTable3.addCell(makeCellRightNoBorderFclBL(""));
	document.add(detailsTable3);
	document.add(emptyRowWithBottomBorder);

	//...TRAILER & SEAL DETAILS....

	PdfPTable trailerSealTable2 = makeTable(2);
	trailerSealTable2.setWidthPercentage(100);
	trailerSealTable2.setWidths(new float[]{50, 50});
	trailerSealTable2.setHorizontalAlignment(0);

	// new table;
	Integer traileTotal = 0;
	if (null != fclBl.getPrintContainersOnBL() && fclBl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
	    PdfPTable trailerSealFirstTable = null;
	    PdfPTable trailerSealTable = makeTable(4);
	    trailerSealTable.setWidthPercentage(100);
	    trailerSealTable.setWidths(new float[]{30, 7, 25, 38});
	    trailerSealTable.setHorizontalAlignment(0);
	    if (null != fclBl.getPrintContainersOnBL() && fclBl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
		trailerSealTable.addCell(makeCellLeftNoBorderConfirmeOnBoard("Trailer", false, "left", "bottom"));
		trailerSealTable.addCell(makeCellRightNoBorderFclBL(" "));
		trailerSealTable.addCell(makeCellLeftNoBorderConfirmeOnBoard("Seal", false, "left", "bottom"));
		trailerSealTable.addCell(makeCellRightNoBorderFclBL(" "));
	    } else {
		trailerSealTable.addCell(makeCellRightNoBorderFclBL(" "));
		trailerSealTable.addCell(makeCellRightNoBorderFclBL(" "));
		trailerSealTable.addCell(makeCellRightNoBorderFclBL(" "));
		trailerSealTable.addCell(makeCellRightNoBorderFclBL(" "));

	    }

	    FclBlBC fclBlBC = new FclBlBC();
	    List containerList = fclBlBC.getUpdatedContainerList(fclBl.getBol().toString());

	    if (containerList.size() > 0) {
		Iterator iter = (Iterator) containerList.iterator();
		while (iter.hasNext()) {
		    FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
		    if (!"D".equalsIgnoreCase(fclBlContainer.getDisabledFlag())) {
			if (null != fclBlContainer.getTrailerNo()) {
			    if (null != fclBl.getPrintContainersOnBL() && fclBl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
				trailerSealTable.addCell(makeCellLeftNoBorderConfirmeOnBoard(fclBlContainer.getTrailerNo(), true, "left", 8));
			    } else {
				trailerSealTable.addCell(makeCellRightNoBorderFclBL(""));
			    }
			    traileTotal++;
			}
			// empty cell
			trailerSealTable.addCell(makeCellLeftNoBorderPalatinoFclBl(" ", palatinoRomanSmallFont));
			if (null != fclBlContainer.getSealNo()) {
			    if (null != fclBl.getPrintContainersOnBL() && fclBl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
				trailerSealTable.addCell(makeCellLeftNoBorderConfirmeOnBoard(fclBlContainer.getSealNo(), true, "left", 8));
			    } else {
				trailerSealTable.addCell(makeCellRightNoBorderFclBL(""));
			    }
			}
			trailerSealTable.addCell(makeCellLeftNoBorderPalatinoFclBl(" ", palatinoRomanSmallFont));
			if (traileTotal == 10) {
			    trailerSealFirstTable = trailerSealTable;
			    trailerSealTable = makeTable(4);
			    trailerSealTable.setWidthPercentage(100);
			    trailerSealTable.setWidths(new float[]{30, 7, 25, 38});
			    trailerSealTable.setHorizontalAlignment(0);
			    if (null != fclBl.getPrintContainersOnBL() && fclBl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
				trailerSealTable.addCell(makeCellLeftNoBorderConfirmeOnBoard("Trailer", false, "left", "bottom"));
				trailerSealTable.addCell(makeCellRightNoBorderFclBL(" "));
				trailerSealTable.addCell(makeCellLeftNoBorderConfirmeOnBoard("Seal", false, "left", "bottom"));
				trailerSealTable.addCell(makeCellRightNoBorderFclBL(" "));
			    }
			}
		    }
		}
	    }
	    if (trailerSealFirstTable != null) {
		PdfPCell cell2 = new PdfPCell();
		createRemeaningEmptyRows(trailerSealFirstTable, 4, 10);
		cell2.addElement(trailerSealFirstTable);
		cell2.setBorder(0);
		trailerSealTable2.addCell(cell2);

		cell2 = new PdfPCell();
		cell2.setBorder(0);
		createRemeaningEmptyRows(trailerSealTable, 4, 10);
		cell2.addElement(trailerSealTable);
		trailerSealTable2.addCell(cell2);
	    } else {
		PdfPCell cell2 = new PdfPCell();
		cell2.setBorder(0);
		createRemeaningEmptyRows(trailerSealTable, 4, 10);
		cell2.addElement(trailerSealTable);
		trailerSealTable2.addCell(cell2);
		trailerSealTable2.addCell(makeCellRightNoBorderFclBL(""));
	    }
	    document.add(trailerSealTable2);
	}
	document.add(emptyTable);
	document.add(emptyTable);
	document.add(emptyTable);

	//..END OF DOC....
	PdfPTable endDocTable = makeTable(4);
	endDocTable.setWidthPercentage(100);
	endDocTable.setWidths(new float[]{20, 20, 50, 10});

	Date date = new Date();
	Calendar cal = Calendar.getInstance();
	String time = simpleDateFormat.format(cal.getTime());
	int index = 11;
	time = time.substring(index);

	endDocTable.addCell(makeCellleftwithBottomBorderSmallFontCorier("Date " + (currentDate.format(date))));
	endDocTable.addCell(makeCellleftwithBottomBorderSmallFontCorier(traileTotal + " Total Trailers"));
	endDocTable.addCell(makeCellleftwithBottomBorderSmallFontCorier("End Of Report"));
	endDocTable.addCell(makeCellleftwithBottomBorderSmallFontCorier("Time  " + time));
	cell = makeCellLeftNoBorderPalatinoFclBl("", helvSmallFont);
	cell.setColspan(4);
	endDocTable.addCell(cell);
	document.add(endDocTable);

    }

    public void onEndPage(PdfWriter writer, Document document) {

	try {
	    //this for print page number at the bottom in the format x of y
	    PdfContentByte cb = writer.getDirectContent();
	    cb.saveState();
	    String text = "Page " + writer.getPageNumber() + " of ";
	    float textBase = document.bottom() - (document.bottomMargin() - 20);
	    //float textBase = document.bottom() - 20;
	    float textSize = helv.getWidthPoint(text, 12);
	    cb.beginText();
	    cb.setFontAndSize(helv, 7);
	    cb.setTextMatrix(document.left() + 280, textBase);
	    cb.showText(text);
	    cb.endText();
	    cb.addTemplate(total, document.left() + 260 + textSize, textBase);
	    cb.restoreState();


	    ///this for the water mark..........................
	    BaseFont helv;
	    PdfGState gstate;
	    Font hellv;
	    String waterMark = "";
	    waterMark = PrintReportsConstants.CONFIRM_ONBOARD_NOTICE;

//                        else if(null!=proof && proof.equalsIgnoreCase("Yes")){
//				waterMark=PrintReportsConstants.PROOF;
//			}else{
//				waterMark="";
//			}
	    try {
		helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI,
			BaseFont.NOT_EMBEDDED);

	    } catch (Exception e) {
		throw new ExceptionConverter(e);
	    }
	    gstate = new PdfGState();
	    gstate.setFillOpacity(0.15f);
	    gstate.setStrokeOpacity(0.3f);
	    PdfContentByte contentunder = writer.getDirectContentUnder();
	    contentunder.saveState();
	    contentunder.setGState(gstate);
	    contentunder.beginText();
	    contentunder.setFontAndSize(helv, 50);
	    contentunder.showTextAligned(Element.ALIGN_CENTER, waterMark,
		    document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
	    contentunder.endText();
	    contentunder.restoreState();
	} catch (Exception e) {
	    log.info("onEndPage failed on " + new Date(),e);
            throw new ExceptionConverter(e);
	}
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
	total.beginText();
	total.setFontAndSize(helv, 7);
	total.setTextMatrix(0, 0);
	if (CommonFunctions.isNotNull(manifestRev)) {
	    total.showText(String.valueOf(writer.getPageNumber() - 1) + "        (REV: " + manifestRev + ")");
	} else {
	    total.showText(String.valueOf(writer.getPageNumber() - 1));
	}
	total.endText();
    }

    public void destroy() {
	document.close();
    }

    public String removeUnlocCode(String port) {
	String portName = "";
	if (null != port) {
	    if (port.lastIndexOf("(") != -1) {
		portName = port.substring(0, port.lastIndexOf("("));
	    } else {
		portName = port;
	    }
	    int length = portName.length();
	    if (CommonUtils.isNotEmpty(portName) && portName.charAt(length - 1) == '/') {
		portName = portName.substring(0, length - 1);
	    }
	}
	return portName;
    }

    public String createBillOfLaddingReport(FclBl fclBl, String fileName, String contextPath,
	    MessageResources messageResources, User user, String result) throws Exception{
	try {
	    ManifestBLPdfCreator.messageResources = messageResources;
	    BLPdfConfirmOnBoardNotice.manifested = fclBl.getReadyToPost();
	    BLPdfConfirmOnBoardNotice.proof = fclBl.getProof();
	    this.initialize(fileName, fclBl);
	    this.createBody(fclBl, contextPath, messageResources, user, result);
	    this.destroy();
	} catch (Exception e) {
	    log.info("createBillOfLaddingReport failed on " + new Date(),e);
            throw new ExceptionConverter(e);
	}
	return "fileName";
    }
}
