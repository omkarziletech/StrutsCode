package com.gp.cong.logisoft.reports;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.domestic.DomesticPurchaseOrder;
import com.logiware.domestic.DomesticRateCarrier;
import com.logiware.domestic.DomesticRateQuote;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
import java.util.*;

public class CTSQuotePdfCreator {

    Document document = null;
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 12, 0, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);

    public void initialize(String fileName) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        PdfAction action = PdfAction.gotoLocalPage(1, new
        PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
        pdfWriter.setOpenAction(action);
    }

    public void createBody(DomesticRateQuote quote,List<DomesticRateCarrier> carrierList, List<DomesticPurchaseOrder> orderList,User loginUser, String contextPath,String carrierName) throws DocumentException, MalformedURLException, IOException, Exception {
        PdfPCell cell;
        String imagePath = "/img/companyLogo/primaryFrieght.jpg";
        Image img = Image.getInstance(contextPath + imagePath);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(60);
        PdfPCell celL = new PdfPCell();
        celL.addElement(new Chunk(img, 220, -30));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(celL);
        PdfPTable emptyTable = new PdfPTable(1);
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        PdfPTable quoteTable = makeTable(1);
        quoteTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorder("Shipe Date : "+quote.getShipDate());
        quoteTable.addCell(cell);
        PdfPTable addressTable = makeTable(1);
        addressTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorder(loginUser.getFirstName());
        addressTable.addCell(cell);
        cell = makeCellLeftNoBorder(loginUser.getAddress1());
        addressTable.addCell(cell);
        cell = makeCellLeftNoBorder(loginUser.getCity()+","+loginUser.getState()+","+loginUser.getZipCode());
        addressTable.addCell(cell);
        PdfPTable lineItem = makeTable(5);
        lineItem.setWidthPercentage(100);
        lineItem.setWidths(new float[]{20, 20, 20, 20, 20});
        lineItem.addCell(makeCellLeftNoBorder("Cubic Feet"));
        lineItem.addCell(makeCellLeftNoBorder("Class"));
        lineItem.addCell(makeCellLeftNoBorder("Pieces"));
        lineItem.addCell(makeCellLeftNoBorder("Pallet"));
        lineItem.addCell(makeCellLeftNoBorder("Weight"));
        if(null != orderList){
            for (DomesticPurchaseOrder domesticPurchaseOrder : orderList) {
                lineItem.addCell(makeCellLeftNoBorder(""+domesticPurchaseOrder.getCube()));
                lineItem.addCell(makeCellLeftNoBorder(domesticPurchaseOrder.getClasses()));
                lineItem.addCell(makeCellLeftNoBorder(""+domesticPurchaseOrder.getPackageQuantity()));
                lineItem.addCell(makeCellLeftNoBorder(""+domesticPurchaseOrder.getHandlingUnitQuantity()));
                lineItem.addCell(makeCellLeftNoBorder(""+domesticPurchaseOrder.getWeight()));
            }
        }
        PdfPTable quoteDetailTable = makeTable(6);
        quoteDetailTable.setWidthPercentage(100);
        cell = makeCellLeftBottomBorder("Quote Details");
        cell.setBorderWidthLeft(0.1f);
        cell.setBorderWidthTop(0.1f);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.0f);
        cell.setColspan(6);
        quoteDetailTable.addCell(cell);

        cell = makeCellLeftNoBorder("Origin :");
        cell.setBorderWidthLeft(0.1f);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        quoteDetailTable.addCell(cell);
        quoteDetailTable.addCell(makeCellLeftNoBorder(quote.getOriginCity()));
        quoteDetailTable.addCell(makeCellLeftNoBorder("Origin Zip :"));
        quoteDetailTable.addCell(makeCellLeftNoBorder(quote.getOriginZip()));
        quoteDetailTable.addCell(makeCellLeftNoBorder("Origin State :"));
        cell = makeCellLeftRightBorder(quote.getOriginState());
        cell.setBorderWidthBottom(0.0f);
        quoteDetailTable.addCell(cell);

        cell = makeCellLeftNoBorder("Destination :");
        cell.setBorderWidthLeft(0.1f);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        quoteDetailTable.addCell(cell);
        quoteDetailTable.addCell(makeCellLeftNoBorder(quote.getDestinationCity()));
        quoteDetailTable.addCell(makeCellLeftNoBorder("Destination Zip :"));
        quoteDetailTable.addCell(makeCellLeftNoBorder(quote.getDestinationZip()));
        quoteDetailTable.addCell(makeCellLeftNoBorder("Destination State :"));
        cell = makeCellLeftRightBorder(quote.getDestinationState());
        cell.setBorderWidthBottom(0.0f);
        quoteDetailTable.addCell(cell);


        PdfPTable carrierTable = makeTable(8);
        carrierTable.setWidthPercentage(100);
        carrierTable.setWidths(new float[]{30, 12, 8, 6, 9, 11, 12, 12});
        cell = makeCellLeftBottomBorder("List of Carrier");
        cell.setBorderWidthLeft(0.1f);
        cell.setBorderWidthTop(0.1f);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.f);
        cell.setColspan(9);
//        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Carrier Name");
        cell.setBorderWidthLeft(0.1f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthBottom(0.1f);
        carrierTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Direct/Interline");
        carrierTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Est Date");
        carrierTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Type");
        carrierTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Line Haul");
        carrierTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Fuel Charge");
        carrierTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Extra Charges");
        carrierTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Final Charge");
        cell.setBorderWidthLeft(0.0f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        carrierTable.addCell(cell);
        for (DomesticRateCarrier carrier :carrierList) {
            cell = makeCellLeft(carrier.getCarrierName());
            cell.setBorderWidthLeft(0.1f);
            if(null != carrierName && carrierName.equals(carrier.getCarrierName())){
                cell.setBackgroundColor(Color.yellow);
            }
            carrierTable.addCell(cell);
            cell = makeCellLeft(carrier.getDirectInterline());
            if(null != carrierName && carrierName.equals(carrier.getCarrierName())){
                cell.setBackgroundColor(Color.yellow);
            }
            carrierTable.addCell(cell);
            cell = makeCellLeft(""+carrier.getEstimatedDays());
            if(null != carrierName && carrierName.equals(carrier.getCarrierName())){
                cell.setBackgroundColor(Color.yellow);
            }
            carrierTable.addCell(cell);
            cell = makeCellLeft(carrier.getType());
            if(null != carrierName && carrierName.equals(carrier.getCarrierName())){
                cell.setBackgroundColor(Color.yellow);
            }
            carrierTable.addCell(cell);
            cell = makeCellLeft(null != carrier.getLineHual() ?carrier.getLineHual().toString():"");
            if(null != carrierName && carrierName.equals(carrier.getCarrierName())){
                cell.setBackgroundColor(Color.yellow);
            }
            carrierTable.addCell(cell);
            cell = makeCellLeft(null != carrier.getFuelCharge() ?carrier.getFuelCharge().toString():"");
            if(null != carrierName && carrierName.equals(carrier.getCarrierName())){
                cell.setBackgroundColor(Color.yellow);
            }
            carrierTable.addCell(cell);
            cell = makeCellLeft(null != carrier.getExtraCharge() ?carrier.getExtraCharge().toString():"");
            if(null != carrierName && carrierName.equals(carrier.getCarrierName())){
                cell.setBackgroundColor(Color.yellow);
            }
            carrierTable.addCell(cell);
            cell = makeCellLeft(null != carrier.getFinalCharge() ?carrier.getFinalCharge().toString():"");
            cell.setBorderWidthLeft(0.0f);
            cell.setBorderWidthRight(0.1f);
            if(null != carrierName && carrierName.equals(carrier.getCarrierName())){
                cell.setBackgroundColor(Color.yellow);
            }
            carrierTable.addCell(cell);
        }
        PdfPTable descTable = makeTable(1);
        descTable.setWidthPercentage(100);
        Iterator iter = new GenericCodeDAO().getCommentsListForCTS();
        while (iter.hasNext()) {
             Object[] row = (Object[]) iter.next();
	    String code = (String) row[0];
	    String codeDesc = (String) row[1];
            if (code != null) {
                if ("CTS01".equals(code)) {
                    descTable.addCell(makeCellLeftNoBorderBoldFont(codeDesc));
                }else{
                    descTable.addCell(makeCellLeftNoBorder(codeDesc));
                }
                descTable.addCell(makeCellLeftNoBorderBoldFont("\n"));
            }
        }
//        descTable.addCell(makeCellLeftNoBorderBoldFont(""));
//        descTable.addCell(makeCellLeftNoBorderBoldFont(""));
//        descTable.addCell(makeCellLeftNoBorderBoldFont(""));
        descTable.addCell(makeCellLeftNoBorderBoldFont("\n"));
        document.add(table);
        document.add(emptyTable);
        document.add(quoteTable);
        document.add(emptyTable);
        document.add(addressTable);
        document.add(emptyTable);
        document.add(lineItem);
        document.add(emptyTable);
        document.add(quoteDetailTable);
        document.add(carrierTable);
        document.add(emptyTable);
        document.add(descTable);
    }

    public void destroy() {
        document.close();
    }

    private PdfPCell makeCellCenter(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBorderWidthLeft(0.0f);
        return cell;
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

    private PdfPCell makeCellLeftRightBorder(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.1f);
        return cell;
    }

  
    private PdfPCell makeCellLeftBottomBorder(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }

  

    private PdfPCell makeCellLeft(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }
    private PdfPCell makeCellLeftNoBorder(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.0f);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }
    private PdfPCell makeCellLeftNoBorderBoldFont(String text) {
        Phrase phrase = new Phrase(text, headingFont1);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.0f);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }

    public String createReport(DomesticRateQuote quote,List<DomesticRateCarrier> carrierList, List<DomesticPurchaseOrder> orderList,User loginUser, String fileName, String contextPath,String carrierName)throws Exception {
            this.initialize(fileName);
            this.createBody(quote,carrierList,orderList,loginUser,contextPath,carrierName);
            this.destroy();
        return "fileName";
    }
}
