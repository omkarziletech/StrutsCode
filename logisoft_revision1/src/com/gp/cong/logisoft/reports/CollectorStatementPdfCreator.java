package com.gp.cong.logisoft.reports;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.reports.dto.AgingReportPeriodDTO;
import com.gp.cvst.logisoft.struts.form.CustomerStatementForm;
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
public class CollectorStatementPdfCreator {
	Font blackBoldFont=new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
	Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
	Font headingFont1 = new Font(Font.BOLD, 14,0, Color.BLACK);
	Font blackBoldHeadingFont=new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK);
	Font blackFont=new Font(Font.HELVETICA, 9, 0, Color.BLACK);
	float[] twoColumnDefinitionSize = { 50F, 50F};
	float[] threeColumnDefinitionSize = { 33.33F, 33.33F, 33.33F };
	float[] fourColumnDefinitionSize = { 26F, 29F,15F, 30F};
	Document document=null;
	PdfWriter pdfWriter=null;
 	public void initialize(String fileName,String contextPath,List liginList,String collectorName,String footerName)throws DocumentException, MalformedURLException, IOException{
		document = new Document(PageSize.A4);
		document.setMargins(10,10,10,10);
		pdfWriter=PdfWriter.getInstance(document, new FileOutputStream(fileName));
		String city = "";
		String state = "";
		String phoneNo = "";
		String email = "";
		if (liginList != null) {
			city = (String) liginList.get(2) + ",";
			state = (String) liginList.get(3) + ",";
			phoneNo = (String) liginList.get(4) + ",";
			email = (String) liginList.get(5);
		}
		String colName="CollectorName :"+collectorName;
		String footerPhone="  Phone No :"+phoneNo;
		String footerEmail="  Email :"+email;
		
		String footerList=colName+""+footerPhone+""+footerEmail+"\n"+footerName+"\nPage ";
		String totalPages="";
 		Phrase headingPhrase=new Phrase(footerList,headingFont);
		Phrase headingPhrase1=new Phrase(totalPages,headingFont);
		HeaderFooter footer = new HeaderFooter(headingPhrase,headingPhrase1);
	    footer.setAlignment(Element.ALIGN_CENTER);
	    
	    document.setFooter(footer);
		document.open();
		
	}
	public void createBody(List<AgingReportPeriodDTO> searchCollectorStatementList,String contextPath,CustomerStatementForm customerStatementForm,String collectorName)throws DocumentException, MalformedURLException, IOException, Exception{
		//start of image
		String ageingzeero=customerStatementForm.getAgingzeero();
		String ageingthirty=customerStatementForm.getAgingthirty();
		String greaterthanthirty=customerStatementForm.getGreaterthanthirty();
		String agingsixty=customerStatementForm.getAgingsixty();
		String greaterthansixty=customerStatementForm.getGreaterthansixty();
		String agingninty=customerStatementForm.getAgingninty();
		String greaterthanninty=customerStatementForm.getGreaterthanninty();
		String overdue=customerStatementForm.getOverdue();
		String minamt=customerStatementForm.getMinamt();
		String terminal=customerStatementForm.getTerminal();
		String collector=customerStatementForm.getCollector();
		String company=customerStatementForm.getCompany();
		String subject=customerStatementForm.getSubject();
		String textArea=customerStatementForm.getTextArea();
		
		//GENERATING IMAGE
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image img = Image.getInstance(contextPath + imagePath);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(15);
        PdfPCell celL = new PdfPCell();
        celL.addElement(new Chunk(img, 0,-22));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(celL);
        document.add(table);
        
        PdfPCell cell;
		PdfPTable bookTable = new PdfPTable(2);
		bookTable.setWidthPercentage(100);
		bookTable.setWidths(new float[]{50,50});
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
		//HEADER WITH BACKGROUND COLOUR
		String heading="Collector Statement";
		Phrase headingPhrase=new Phrase(heading,headingFont1);
		PdfPCell headingCell=new PdfPCell(headingPhrase);
		headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headingCell.setVerticalAlignment(Element.ALIGN_TOP);
		headingCell.setPaddingTop(-2);
		headingCell.setPaddingBottom(2);
		headingCell.setBorder(0);
		headingCell.setBackgroundColor(Color.LIGHT_GRAY);
		headingCell.setColspan(2);
        bookTable.addCell(headingCell);
        //CREATE DATE FIELD AND INSERT DATE
        PdfPTable dateCell = makeTable(1);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        PdfPCell date= makeCellRightNoBorder(simpleDateFormat.format(new Date()));
        dateCell.setWidthPercentage(100);
        dateCell.addCell(date);
    //CREATE EMPTY ROW
    PdfPTable emptyRow = makeTable(1);
    PdfPCell emptyCell= makeCellRightNoBorder("");
    emptyRow.addCell(emptyCell);

    //THIS IS FOR FIELD HEADINGS
	PdfPTable agingTable=new PdfPTable(3);
	agingTable.setWidthPercentage(100);
	agingTable.setWidths(new float[]{33.33f,33.33f,33.33f});
	AgingReportPeriodDTO agingReportPeriodDTO = new AgingReportPeriodDTO();
	if(!searchCollectorStatementList.isEmpty()){
	agingReportPeriodDTO=searchCollectorStatementList.get(0);
	//THIS IS FOR FIRST ROW
	agingTable.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERNO+"  "+agingReportPeriodDTO.getCustNo()));
	agingTable.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERNAME+"  "+agingReportPeriodDTO.getCustName()));
	agingTable.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERADDRESS+"  "+agingReportPeriodDTO.getCustAddress()));
	agingTable.addCell(makeCellleftNoBorder(ReportConstants.TERMINAL+" "+terminal));
	agingTable.addCell(makeCellleftNoBorder(ReportConstants.COMPANY+" "+company));
	agingTable.addCell(makeCellleftNoBorder(ReportConstants.COLLECTOR+" "+collectorName));
	agingTable.addCell(makeCellleftNoBorder(" "));
	agingTable.addCell(makeCellleftNoBorder(" "));
	agingTable.addCell(makeCellCenter(ReportConstants.AGERANGES+" "));
	}

	PdfPTable agingFielsTableHeading=new PdfPTable(7);
	agingFielsTableHeading.setWidthPercentage(100);
	agingFielsTableHeading.setWidths(new float[]{15,15,15,15,13,13,14});
	agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.INVOICENO));
	agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.INVOICEDATE));
	agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading(ReportConstants.BALANCE));
	agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading("<"+ageingzeero+"-"+ageingthirty+">"));
	agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading("<"+greaterthanthirty+"-"+agingsixty+">"));
	agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading("<"+greaterthansixty+"-"+agingninty+">"));
	agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading("<"+greaterthanninty+">"));
	
	document.add(bookTable);
	document.add(dateCell);
	document.add(agingTable);
	document.add(agingFielsTableHeading);
	//THIS IS FOR FIELD LIST ITERATOR
	int i=0;
	
	PdfPTable agingFielsTable=new PdfPTable(7);
	agingFielsTable.setWidthPercentage(100);
	agingFielsTable.setWidths(new float[]{15,15,15,15,13,13,14});
	int size=searchCollectorStatementList.size();
	String AcctName="";
	while(size>0){
		agingReportPeriodDTO=searchCollectorStatementList.get(i);
		if(AcctName!=null && !AcctName.equals("") && !AcctName.equals(agingReportPeriodDTO.getCustName())){
			document.add(agingFielsTable);
			agingFielsTable.deleteBodyRows();
			document.newPage();
			document.add(table);
			document.add(bookTable);
			document.add(dateCell);
			
			PdfPTable agingTableHeading=new PdfPTable(3);
			agingTableHeading.setWidthPercentage(100);
			agingTableHeading.setWidths(new float[]{33.33f,33.33f,33.33f});
			agingReportPeriodDTO=searchCollectorStatementList.get(i);
			agingTableHeading.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERNO+"  "+agingReportPeriodDTO.getCustNo()));
			agingTableHeading.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERNAME+"  "+agingReportPeriodDTO.getCustName()));
			agingTableHeading.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERADDRESS+"  "+agingReportPeriodDTO.getCustAddress()));
			agingTableHeading.addCell(makeCellleftNoBorder(ReportConstants.TERMINAL+" "+terminal));
			agingTableHeading.addCell(makeCellleftNoBorder(ReportConstants.COMPANY+" "+company));
			agingTableHeading.addCell(makeCellleftNoBorder(ReportConstants.COLLECTOR+" "+collector));
			agingTableHeading.addCell(makeCellleftNoBorder(" "));
			agingTableHeading.addCell(makeCellleftNoBorder(" "));
			agingTableHeading.addCell(makeCellCenter(ReportConstants.AGERANGES+" "));

			document.add(agingTableHeading);
			document.add(emptyRow);
			document.add(agingFielsTableHeading);
		}
		
		AcctName=agingReportPeriodDTO.getCustName();
		agingFielsTable.addCell(makeCellleftNoBorder(agingReportPeriodDTO.getInvoiceNo()));
		agingFielsTable.addCell(makeCellleftNoBorder(agingReportPeriodDTO.getSailingDate()));
		agingFielsTable.addCell(makeCellCenterForDouble(agingReportPeriodDTO.getBalance()));
		agingFielsTable.addCell(makeCellCenterForDouble(agingReportPeriodDTO.getAgerangeone()));
		agingFielsTable.addCell(makeCellCenterForDouble(agingReportPeriodDTO.getAgerangetwo()));
		agingFielsTable.addCell(makeCellCenterForDouble(agingReportPeriodDTO.getAgerangethree()));
		agingFielsTable.addCell(makeCellCenterForDouble(agingReportPeriodDTO.getAgerangefour()));
		i++;
		size--;
		if(size==0){
			document.add(agingFielsTable);
		}
	}
	
}
	public void destroy(){
		document.close();
	}
	private PdfPTable makeTable(int rows){
		PdfPTable table = new PdfPTable(rows);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setBorderWidth(0);
		return table;
	}
	private PdfPCell makeCell(Phrase phrase, int alignment){
		PdfPCell cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(0);
		return cell;
	}
    private PdfPCell makeCellLeftNoBorder(String text){
		Phrase phrase = new Phrase(text, blackBoldFont);
		PdfPCell cell = makeCell(phrase,Element.ALIGN_LEFT);
		cell.setBorderWidthRight(0.0f);
		return cell;
	}
    private PdfPCell makeCellRightNoBorder(String text){
		Phrase phrase = new Phrase(text, blackBoldFont);
		PdfPCell cell = makeCell(phrase,Element.ALIGN_RIGHT);
		return cell;
	}
    private PdfPCell makeCellCenter(String text){
		Phrase phrase = new Phrase(text, blackBoldFont);
		PdfPCell cell = makeCell(phrase,Element.ALIGN_CENTER);
		cell.setBorderWidthLeft(0.0f);
		return cell;
	}
	private PdfPCell makeCellleftNoBorder(String text){
		Phrase phrase = new Phrase(text, blackBoldFont);
		PdfPCell cell = makeCell(phrase,Element.ALIGN_LEFT);
		//cell.height('2');
		cell.setBorderWidthLeft(0f);
		return cell;
	}
	private PdfPCell makeCellCenterNoBorder(String text){
		PdfPCell cell = makeCellCenter(text);
		cell.setBorderWidthLeft(0f);
		return cell;
	}
	//THIS IS FOR LEFT BORDER WITH HEADING FONT
	private PdfPCell makeCellleftwithBorder(String text){
		Phrase phrase = new Phrase(text, headingFont);
		PdfPCell cell = makeCell(phrase,Element.ALIGN_LEFT);
		cell.setBorderWidthRight(0.0f);
		cell.setBorderWidthBottom(0.1f);
		return cell;
	}
	private PdfPCell makeCellCenterForDoubleHeading(String text){
		Phrase phrase = new Phrase(text,headingFont);
		PdfPCell cell = makeCell(phrase,Element.ALIGN_RIGHT);
		cell.setBorderWidthRight(0.0f);
		cell.setBorderWidthBottom(0.1f);
		return cell;
	}
	private PdfPCell makeCellCenterForDouble(String text){
		Phrase phrase = new Phrase(text,blackFont);
		PdfPCell cell = makeCell(phrase,Element.ALIGN_RIGHT);
		return cell;
	}
	private PdfPCell makeCellCenterForDouble(Double value){
		String value1=String.valueOf(value);
		Phrase phrase = new Phrase(value1,blackFont);
		PdfPCell cell = makeCell(phrase,Element.ALIGN_RIGHT);
		cell.setBorderWidthRight(0.1f);
		cell.setBorderWidthBottom(0.1f);
		return cell;
	}
	public String createReport(List<AgingReportPeriodDTO> searchCollectorStatementList,String fileName,String contextPath,CustomerStatementForm customerStatementForm,List liginList,String username)
                throws DocumentException, MalformedURLException, IOException, Exception{
			String collectorName="";
		   String collector=customerStatementForm.getCollector();
		   String footerName=customerStatementForm.getFooterName();
		   if(!collector.equals("0") && collector!=null){
				GenericCodeDAO genericCodeDAO=new GenericCodeDAO();
				GenericCode genericCode=new GenericCode();
				genericCode=genericCodeDAO.findById(Integer.parseInt(collector));
				collectorName=genericCode.getCodedesc();
		   }
			this.initialize(fileName,contextPath,liginList,collectorName,footerName);
			this.createBody(searchCollectorStatementList,contextPath,customerStatementForm,collectorName);
		    this.destroy();
		return "fileName";
	}
}
