package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import com.gp.cong.logisoft.reports.dto.TrialReportDTO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.util.Date;

import org.apache.log4j.Logger;

public class TrialBalancePdfCreator {

    private static final Logger log = Logger.getLogger(TrialBalancePdfCreator.class);
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 18, 0, Color.BLACK);
    Font blackBoldHeadingFont = new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);
    float[] twoColumnDefinitionSize = {50F, 50F};
    float[] threeColumnDefinitionSize = {33.33F, 33.33F, 33.33F};
    float[] fourColumnDefinitionSize = {26F, 29F, 15F, 30F};
    Document document = null;

    //BL Report Object
    //BLReportDTO  blReportDTO = null;
    public void initialize(String fileName) throws FileNotFoundException, DocumentException {

	document = new Document(PageSize.A4);
	document.setMargins(10, 10, 10, 10);
	PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	document.open();
	PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
	pdfWriter.setOpenAction(action);
    }

    public void createBody(List<TrialReportDTO> trialReportDTOList, String contextPath) throws DocumentException, MalformedURLException, IOException, Exception {
	//start of image
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image img = Image.getInstance(contextPath + imagePath);
	PdfPTable table = new PdfPTable(1);
	table.setWidthPercentage(100);
	img.scalePercent(50);
	PdfPCell celL = new PdfPCell();
	celL.addElement(new Chunk(img, 230, -50));
	celL.setBorder(0);
	celL.setHorizontalAlignment(Element.ALIGN_CENTER);
	celL.setVerticalAlignment(Element.ALIGN_CENTER);
	table.addCell(celL);
	//image
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


	//Heading

	String heading = "Trial Balance Report";
	Phrase headingPhrase = new Phrase(heading, headingFont1);
	PdfPCell headingCell = new PdfPCell(headingPhrase);
	headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	headingCell.setVerticalAlignment(Element.ALIGN_TOP);
	//  headingCell.setPadding(100);
	headingCell.setPaddingTop(-2);
	headingCell.setPaddingBottom(2);
	headingCell.setBorder(0);
	headingCell.setBackgroundColor(Color.LIGHT_GRAY);
	headingCell.setColspan(2);

	bookTable.addCell(headingCell);

	//end of heading


	//empty


	PdfPTable Empty = makeTable(1);
	PdfPCell Emptycell = makeCellCenterNoBorder("");
	Empty.addCell(Emptycell);

	//empty


	PdfPTable emptytable = makeTable(6);
	PdfPCell emptycell = makeCellCenterNoBorder("");
	Empty.addCell(emptycell);

	PdfPTable periodtable = new PdfPTable(6);
	periodtable.setWidthPercentage(100);
	periodtable.setWidths(new float[]{20, 15, 5, 10, 10, 20});
	TrialReportDTO trialReportDTO = new TrialReportDTO();
	trialReportDTO = trialReportDTOList.get(0);



	periodtable.addCell(makeCellCenterNoBorder(" "));
	periodtable.addCell(makeCellCenterNoBorder("Period :" + trialReportDTO.getPeriod()));
	periodtable.addCell(makeCellCenterNoBorder(""));
	periodtable.addCell(makeCellCenterNoBorder("Year :" + trialReportDTO.getYear()));
	periodtable.addCell(makeCellCenterNoBorder(""));
	periodtable.addCell(makeCellCenterNoBorder(""));





	PdfPTable accountTable = makeTable(4);
	accountTable.setWidthPercentage(100);
	accountTable.setWidths(new float[]{20, 45, 12, 13});

	accountTable.addCell(makeCellCenter3("Account"));
	accountTable.addCell(makeCellCenter3("Description"));
	accountTable.addCell(makeCellCenter3("Debit"));
	accountTable.addCell(makeCellCenter3("Credit"));
	//accountTable.addCell(makeCellCenter3("Balance"));

	double totalCredit = 0;
	double totalDebit = 0;
	double totalBalance = 0;
	NumberFormat numformat = new DecimalFormat("##,###,##0.00");

	for (Iterator iterator = trialReportDTOList.iterator(); iterator.hasNext();) {
	    trialReportDTO = (TrialReportDTO) iterator.next();

	    accountTable.addCell(makeCellCenter1(trialReportDTO.getAccountNumber()));
	    accountTable.addCell(makeCellCenter1(trialReportDTO.getAccountDescription()));

	    if (trialReportDTO.getTotalDebit() == 0.00) {
		accountTable.addCell(makeCellRight(("")));
	    } else {
		accountTable.addCell(makeCellRight(numformat.format(trialReportDTO.getTotalDebit())));
	    }
	    if (trialReportDTO.getTotalCredit() == 0.00) {
		accountTable.addCell(makeCellRightBottomBorder(("")));
	    } else {
		accountTable.addCell(makeCellRightBottomBorder(numformat.format(trialReportDTO.getTotalCredit())));
	    }
	    /* if(trialReportDTO.getBalance()==0.00){
	     accountTable.addCell( makeCellRight(("")));
	     }
	     else{
	     accountTable.addCell(makeCellRight(numformat.format(trialReportDTO.getBalance())));
	     }*/

	    totalBalance += trialReportDTO.getBalance();
	    totalCredit += trialReportDTO.getTotalCredit();
	    totalDebit += trialReportDTO.getTotalDebit();

	}

	PdfPTable totalTable = makeTable(3);
	totalTable.setWidthPercentage(100);
	totalTable.setWidths(new float[]{72, 14, 14});

	totalTable.addCell(makeCellLeft("Total"));
	totalTable.addCell(makeCellLeft(numformat.format(totalDebit)));
	totalTable.addCell(makeCellLeft(numformat.format(totalCredit)));
//		totalTable.addCell(makeCellLeft(numformat.format(totalBalance)));

	document.add(table);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);

	document.add(bookTable);

	document.add(Empty);

	document.add(periodtable);

	document.add(Empty);
	document.add(Empty);



	document.add(accountTable);
	document.add(Empty);
	document.add(Empty);
	document.add(totalTable);
    }

    public void destroy() {
	document.close();
    }

    private PdfPTable makeTable(int rows) {
	PdfPTable table = new PdfPTable(rows);
	table.getDefaultCell().setBorder(0);
	table.getDefaultCell().setBorderWidth(0);
	return table;
    }

    private PdfPCell makeCell(Phrase phrase, int alignment) {
	PdfPCell cell = new PdfPCell(phrase);
	cell.setHorizontalAlignment(alignment);
	cell.setBorder(0);
	return cell;
    }

    private PdfPCell makeTableCell(PdfPTable table) {

	PdfPCell cell = new PdfPCell(table);
	cell.setBorderWidth(0f);
	cell.setBorderWidthBottom(0.5f);
	cell.setPadding(0f);
	return cell;
    }

    private PdfPCell makeCellCenter4(String text) {
	Phrase phrase = new Phrase(text, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	cell.setBorderWidthRight(0.1f);
	//cell.setBorderWidthLeft(1.0f);
	//cell.setBorderWidthTop(1.0f);

	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    private PdfPCell makeCellLeft(String text) {
	Phrase phrase = new Phrase(text, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
	cell.setBorderWidthRight(0.5f);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	return cell;
    }

    private PdfPCell makeCellLeftForDouble(Double value) {
	String value1 = String.valueOf(value);
	Phrase phrase = new Phrase(value1, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBorderWidthRight(0.5f);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	return cell;
    }

    private PdfPCell makeCellLeftNoBorder(String text) {
	Phrase phrase = new Phrase(text, blackBoldFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
	cell.setBorderWidthRight(0.0f);
	return cell;
    }

    private PdfPCell makeCellCenter1(String text) {
	Phrase phrase = new Phrase(text, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	//cell.setBorderWidthLeft(1.0f);
	//cell.setBorderWidthTop(1.0f);
	return cell;
    }

    private PdfPCell makeCellRight(String text) {
	Phrase phrase = new Phrase(text, blackBoldFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    private PdfPCell makeCellRightBottomBorder(String text) {
	Phrase phrase = new Phrase(text, blackBoldFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBorderWidthRight(0.0f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    private PdfPCell makeCellCenter(String text) {
	Phrase phrase = new Phrase(text, blackBoldFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
	cell.setBorderWidthLeft(0.5f);
	return cell;
    }

    private PdfPCell makeCellleftNoBorder(String text) {
	Phrase phrase = new Phrase(text, blackBoldFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
	cell.setBorderWidthLeft(0f);
	return cell;
    }

    private PdfPCell makeCellCenterNoBorder(String text) {
	PdfPCell cell = makeCellCenter(text);
	cell.setBorderWidthLeft(0f);
	return cell;
    }

    private PdfPCell makeCellCenter3(String text) {
	Phrase phrase = new Phrase(text, headingFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    private PdfPCell makeCellCenterForDouble(Double value) {
	String value1 = String.valueOf(value);
	Phrase phrase = new Phrase(value1, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	//cell.setBackgroundColor(Color.LIGHT_GRAY);
	cell.setBorderWidthRight(0.1f);
	//cell.setBorderWidthLeft(1.0f);
	//cell.setBorderWidthTop(1.0f);

	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    public String createReport(List<TrialReportDTO> trialReportDTOList, String fileName, String contextPath) {
	try {
	    this.initialize(fileName);
	    this.createBody(trialReportDTOList, contextPath);
	    this.destroy();
	} catch (Exception e) {
	    log.info("createReport failed on " + new Date(),e);
	}
	return "fileName";
    }
}
