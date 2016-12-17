package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.directwebremoting.WebContextFactory;

import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.struts.form.FclBlCorrectionsForm;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class FclBlCorrectionsReportPdfCreator extends ReportFormatMethods {
    Document document = null;
    PdfWriter pdfWriter = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
   public void initialize(String fileName) throws FileNotFoundException,
            DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 30, 30);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(
                fileName));
        document.open();
    }
    public void createBody(String contextPath,FclBlCorrections blCorrections,HttpServletRequest request)
            throws MalformedURLException, IOException, DocumentException, Exception {
    	LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();
    	BaseFont palationRomanBase = BaseFont.createFont(contextPath+"/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    	Font palatinoRomanSmallFont = new Font(palationRomanBase,8, Font.NORMAL, Color.BLACK);
    	
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy ");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
      
        
        PdfPCell cell = null;
        
        Calendar calendar = new GregorianCalendar();
        String am_pm;
        String currentTime;
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if(calendar.get(Calendar.AM_PM) == 0)
          am_pm = "AM";
        else
          am_pm = "PM";
        
        currentTime = hour + ":" + minute + " " + am_pm;
        FclBlCorrectionsForm fclBlCorrectionsForm = null;
        FclBl fclBl=null;
        if(null != request.getAttribute("FclBlCorrectionForm")){
        	fclBlCorrectionsForm =(FclBlCorrectionsForm) request.getAttribute("FclBlCorrectionForm");
        }
        if(null !=  request.getAttribute("fclBl")){
        	fclBl =(FclBl) request.getAttribute("fclBl");
        }
        PdfPTable headingTable = makeTable(5);
        headingTable.setWidthPercentage(100);
        
        
    	LoadLogisoftProperties loadLogisoftProperties1 = new LoadLogisoftProperties();
        String path = loadLogisoftProperties1.getProperty("application.image.logo");
        PdfPCell celL = new PdfPCell();
        PdfPTable table = new PdfPTable(1);
        String realPath = request.getRealPath("/");
        Image img = Image.getInstance(realPath + path);
    	table.setWidthPercentage(100);
        img.scalePercent(60);
        img.scaleAbsoluteWidth(200);
        celL.addElement(new Chunk(img, 185, -5));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setColspan(5);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);
        
        headingTable.addCell(celL);
        
        headingTable.setWidths(new float[]{8,28,46,8,10});
        headingTable.addCell( makeCellLeftNoBorderFclBL("Date :"));
        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl(sdf1.format(new Date()),palatinoRomanSmallFont));
        cell = makeCellleftNoBorderForHeadingFclBL("");
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        headingTable.addCell(cell);
        headingTable.addCell( makeCellLeftNoBorderFclBL(""));
        headingTable.addCell( makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont));
        
        
        headingTable.addCell( makeCellLeftNoBorderFclBL(""));
        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont));
        if(null!=blCorrections.getStatus() && blCorrections.getStatus().equalsIgnoreCase(FclBlConstants.DISABLE)){
        	cell=makeCellleftNoBorderForHeadingWithRedFont(FclBlConstants.VOIDED);
            headingTable.addCell(cell);
        }else{
        	 headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("                        "+"Correction Notices",palatinoRomanSmallFont));
        }
        
       
        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont));
        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont));
        
        cell = makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont);
        cell.setColspan(5);
        headingTable.addCell(cell);
        headingTable.addCell(cell);
        
//        cell = makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont);
//        cell.setColspan(2);
//        headingTable.addCell(cell);
//        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl((null!=blCorrections.getOrigin()?blCorrections.getOrigin():"")+
//        		"  ,  "+(null!=blCorrections.getDestination()?blCorrections.getDestination():""),palatinoRomanSmallFont));
        
        headingTable.addCell(cell);
        
        cell = makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont);
        cell.setColspan(5);
        headingTable.addCell(cell);
        headingTable.addCell(cell);
        
        PdfPTable middleTable = makeTable(4);
        middleTable.setWidthPercentage(100);
        middleTable.setWidths(new float[]{20,30,5,45});
        
        
        cell = makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont);
        cell.setColspan(4);
        middleTable.addCell(cell);
        middleTable.addCell(cell);
        //FOR LABEL cell = makeCellLeftNoBorderFclBL("SHIPPER/EXPORTER");
        //FOR DATA cell = makeCellLeftNoBorderPalatinoFclBl("SHIPPER/EXPORTER",palatinoRomanSmallFont);
        PdfPTable detailsTable = makeTable(2);
        detailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        detailsTable.setWidthPercentage(100);
        
        detailsTable.setWidths(new float[]{17,83});
         PdfPTable destinationOriginTable = makeTable(4);
        destinationOriginTable.setWidths(new float[]{17,35,15,33});
        destinationOriginTable.setWidthPercentage(100);
        destinationOriginTable.addCell(makeCellRightNoBorderFclBL("Correction Notice Date :"));
        destinationOriginTable.addCell(makeCellLeftNoBorderPalatinoFclBl((null != blCorrections.getDate()?sdf.format(blCorrections.getDate()):""),palatinoRomanSmallFont));
        destinationOriginTable.addCell(makeCellRightNoBorderFclBL("Origin :"));
        destinationOriginTable.addCell(makeCellLeftNoBorderPalatinoFclBl((null!=blCorrections.getOrigin()?blCorrections.getOrigin():""),palatinoRomanSmallFont));

        destinationOriginTable.addCell(makeCellRightNoBorderFclBL("Bill of Lading Number :    "));
        destinationOriginTable.addCell(makeCellLeftNoBorderPalatinoFclBl( blCorrections.getBlNumber()+"  "+"CN# "+blCorrections.getNoticeNo()+
        		 (null!=blCorrections.getPostedDate()?"  (Posted)":"  (Not Posted)"),palatinoRomanSmallFont));
        destinationOriginTable.addCell(makeCellRightNoBorderFclBL("Destination :"));
        destinationOriginTable.addCell(makeCellLeftNoBorderPalatinoFclBl((null!=blCorrections.getDestination()?blCorrections.getDestination():""),palatinoRomanSmallFont));
        cell.addElement(destinationOriginTable);
        cell.setColspan(2);
        detailsTable.addCell(cell);

        detailsTable.addCell(makeCellRightNoBorderFclBL("Sailing Date :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl((null!= blCorrections.getSailDate()?sdf.format(blCorrections.getSailDate()):""),palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Vessel :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(blCorrections.getVoyages(),palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Shipper :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(blCorrections.getShipper(),palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Freight Forwarder :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(blCorrections.getForwarder(),palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Consignee :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl((null!= fclBl?(null != fclBl.getConsigneeName()?fclBl.getConsigneeName():""):""),palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Bill To :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(blCorrections.getBillToParty(),palatinoRomanSmallFont));
        
        cell= makeCellleftNoBorder("");
        cell.setColspan(2);
        detailsTable.addCell(cell);
        detailsTable.addCell(cell);
        // adding pdfptable to document
        //charges table
        double totalAmount =0d;
        double newAmount =0d;
        double diffAmount =0d;
        PdfPTable chargesTable = makeTable(6);
        chargesTable.setWidthPercentage(100);
        chargesTable.setWidths(new float[]{10,17,20,13,20,20});
        chargesTable.addCell(makeCellLeftNoBorderFclBLUnderLined("Code"));
        chargesTable.addCell(makeCellLeftNoBorderFclBLUnderLined("Billed As"));
        chargesTable.addCell(makeCellRightNoBorderFclBLUnderLined("Current Charges"));
        chargesTable.addCell(makeCellRightNoBorderFclBLUnderLined("Bill To Party"));
        if(null != blCorrections.getCorrectionCode() && blCorrections.getCorrectionType().getCode().equalsIgnoreCase("A")){
	        chargesTable.addCell(makeCellRightNoBorderFclBLUnderLined("New Amount"));
	        chargesTable.addCell(makeCellRightNoBorderFclBLUnderLined("Net Difference"));
        }else{
        	chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
	        chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
        }
        cell= makeCellleftNoBorder("");
        cell.setColspan(6);
        chargesTable.addCell(cell);
        List<FclBlCorrections> chargesList =(List) request.getAttribute("FclBlChargesList");
        if(null!=chargesList){
        for (FclBlCorrections fclBlCorrections:chargesList) {
                if( null !=fclBlCorrections && null != fclBlCorrections.getAmount()){
                totalAmount = totalAmount+fclBlCorrections.getAmount();
                }
                if( null !=fclBlCorrections && null != fclBlCorrections.getNewAmount()){
                    newAmount = newAmount+fclBlCorrections.getNewAmount();
                }
                if(null!=fclBlCorrections && null != fclBlCorrections.getNewAmount()){
                    diffAmount = diffAmount+fclBlCorrections.getDiffereceAmount();
                }
        	chargesTable.addCell(makeCellLeftNoBorderPalatinoFclBl(fclBlCorrections.getChargeCode(),palatinoRomanSmallFont));
        	chargesTable.addCell(makeCellLeftNoBorderPalatinoFclBl(fclBlCorrections.getChargeCodeDescription(),palatinoRomanSmallFont));
        	chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("$"+(null != fclBlCorrections.getAmount()?numberFormat.format(fclBlCorrections.getAmount()):""),palatinoRomanSmallFont));
        	chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl(fclBlCorrections.getBillToParty(),palatinoRomanSmallFont));
        	chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl((null != fclBlCorrections.getNewAmount()?"$"+numberFormat.format(fclBlCorrections.getNewAmount()):"") ,palatinoRomanSmallFont));
        	chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl((null != fclBlCorrections.getDiffereceAmount()?"$"+numberFormat.format(fclBlCorrections.getDiffereceAmount()):"") ,palatinoRomanSmallFont));

        }
        }
        if(totalAmount != 0d){
             cell= makeCellleftNoBorder("");
        cell.setColspan(6);
        chargesTable.addCell(cell);
                chargesTable.addCell(makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont));
        	chargesTable.addCell(makeCellLeftNoBorderPalatinoFclBl("Total",palatinoRomanSmallFont));
        	chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("$"+numberFormat.format(totalAmount),palatinoRomanSmallFont));
        	chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("",palatinoRomanSmallFont));
                if(null != blCorrections.getCorrectionCode() && blCorrections.getCorrectionType().getCode().equalsIgnoreCase("A")){
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("$"+numberFormat.format(newAmount) ,palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("$"+numberFormat.format(diffAmount),palatinoRomanSmallFont));
                }else{
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("" ,palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("",palatinoRomanSmallFont));
                }
        }
        cell= makeCellleftNoBorder("");
        cell.setColspan(6);
        chargesTable.addCell(cell);
        chargesTable.addCell(cell);
        //end of charges table--------
        PdfPTable correctionTypeTable = makeTable(2);
        correctionTypeTable.setWidthPercentage(100);
        correctionTypeTable.setWidths(new float[]{10,90});
        correctionTypeTable.addCell(makeCellLeftNoBorderFclBL("C/N Code :    "));
        correctionTypeTable.addCell(makeCellLeftNoBorderPalatinoFclBl( blCorrections.getCorrectionCode().getCode()+" "+blCorrections.getCorrectionCode().getCodedesc(),palatinoRomanSmallFont));
        cell = makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont);
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl( blCorrections.getCorrectionType().getCode()+"-"+blCorrections.getCorrectionType().getCodedesc(),palatinoRomanSmallFont);
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);
        if(null != blCorrections.getCorrectionCode() && !blCorrections.getCorrectionType().getCode().equalsIgnoreCase("A")){
        	correctionTypeTable.addCell(makeCellLeftNoBorderFclBL("New BillTo :    "));
        	correctionTypeTable.addCell(makeCellLeftNoBorderPalatinoFclBl(blCorrections.getAccountName()+"/"+blCorrections.getAccountNumber(),palatinoRomanSmallFont));
        }
        
        cell = makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont);
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);
        correctionTypeTable.addCell(cell);
        
        cell =makeCellLeftNoBorderFclBL("Comments:");
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);
        
        cell = makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont);
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);
        
        
        PdfPTable commentsTable = makeTable(2);
        commentsTable.setWidthPercentage(100);
        commentsTable.setWidths(new float[]{3,97});
        commentsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont));
        commentsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(blCorrections.getComments(),palatinoRomanSmallFont));
      
      document.add(headingTable);
      document.add(middleTable);
      document.add(detailsTable);
      document.add(chargesTable);
      document.add(correctionTypeTable);
      document.add(commentsTable);
        
     }
    public void destroy() {
        document.close();
    }

    public String createFclBlCorrectionsReport(FclBlCorrections blCorrections, String fileName,
            String contextPath, MessageResources messageResources,HttpServletRequest request)throws Exception {
        	this.initialize(fileName);
            this.createBody(contextPath,blCorrections,request);
            this.destroy();
        return "fileName";
    }
}
