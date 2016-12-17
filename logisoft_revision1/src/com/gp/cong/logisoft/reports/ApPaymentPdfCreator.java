package com.gp.cong.logisoft.reports;

import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.util.EnglishNumberToWords;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.util.Collections;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;

import org.apache.log4j.Logger;

public class ApPaymentPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(ApPaymentPdfCreator.class);
    String checkFileName = null;
    String overflowFileName = null;
    Document checkDocument = null;
    FileOutputStream fileOutputStream = null;

    private void init() throws FileNotFoundException, DocumentException {
	checkDocument = new Document(PageSize.LETTER);
	checkDocument.setMargins(15, 15, 10, 10);
	fileOutputStream = new FileOutputStream(checkFileName);
	PdfWriter.getInstance(checkDocument, fileOutputStream);
	checkDocument.open();
    }

    private void createBody(String checkNumber, String vendorNumber, String paymentDate, List<TransactionBean> invoiceList)
	    throws DocumentException, MalformedURLException, IOException, Exception {
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	PdfPTable emptyTable = new PdfPTable(1);
	PdfPCell emptyCell = checkPrintingCell("");
	emptyCell.setBorder(0);
	emptyCell.setColspan(1);
	for (int i = 0; i < 12; i++) {
	    emptyTable.addCell(emptyCell);
	}
	checkDocument.add(emptyTable);

	PdfPTable checkAndDate = makeTable(2);
	checkAndDate.setWidthPercentage(100);
	checkAndDate.setWidths(new float[]{75, 25});
	checkAndDate.addCell(checkPrintingCell(""));
	checkAndDate.addCell(checkPrintingCell(StringUtils.leftPad(checkNumber + "  ", 20, "")));
	for (int i = 0; i < 6; i++) {
	    checkAndDate.addCell(checkPrintingCell(""));
	}
	checkAndDate.addCell(checkPrintingCell(""));
	checkAndDate.addCell(checkPrintingCell(StringUtils.leftPad(paymentDate + "    ", 20, "")));
	checkDocument.add(checkAndDate);

	TradingPartner tradingPartner = new TradingPartnerDAO().findById(vendorNumber);
	String vendorName = tradingPartner.getAccountName();

	emptyTable = new PdfPTable(1);
	for (int i = 0; i < 2; i++) {
	    emptyTable.addCell(emptyCell);
	}
	checkDocument.add(emptyTable);
	Double totalDollars = 0d;
	if (null != invoiceList && !invoiceList.isEmpty()) {
	    for (TransactionBean invoiceBean : invoiceList) {
		totalDollars += Double.parseDouble(invoiceBean.getAmount().replaceAll(",", ""));
	    }
	}
	String dollars = "";
	String cents = "";
	String checkAmountText = "";
	if (totalDollars > 0d) {
	    String data = String.valueOf(number.format(totalDollars));
	    String[] amount = StringUtils.split(data, ".");
	    if (null != amount && amount.length == 2) {
		dollars = amount[0].replaceAll(",", "");
		cents = amount[1].replaceAll(",", "");
		if (Integer.parseInt(dollars) > 0) {
		    dollars = EnglishNumberToWords.convert(Integer.parseInt(dollars));
		    checkAmountText = dollars + " DOLLARS ";
		}
		if (Integer.parseInt(cents) > 0) {
		    if (!checkAmountText.equals("")) {
			checkAmountText = checkAmountText + " AND " + cents + " CENTS";
		    } else {
			checkAmountText = cents + " CENTS";
		    }
		}
	    }
	}
	if (checkAmountText.equals("")) {
	    int k = totalDollars.intValue();
	    checkAmountText = EnglishNumberToWords.convert(k);
	}
	PdfPTable paymentDetails = makeTable(4);
	paymentDetails.setWidthPercentage(100);
	paymentDetails.setWidths(new float[]{5, 65, 10, 20});
	paymentDetails.addCell(checkPrintingCell(""));
	paymentDetails.addCell(checkPrintingCell(checkAmountText));
	paymentDetails.addCell(checkPrintingCell(""));
	paymentDetails.addCell(checkPrintingCell(""));
	paymentDetails.addCell(checkPrintingCell(""));
	paymentDetails.addCell(checkPrintingCell(""));
	paymentDetails.addCell(checkPrintingCell(""));
	paymentDetails.addCell(checkPrintingCell(StringUtils.leftPad("$" + number.format(totalDollars) + "    ", 20, "")));

	PdfPCell spaceCell = checkPrintingCell("");
	spaceCell.setBorder(0);
	spaceCell.setColspan(3);
	paymentDetails.addCell(spaceCell);
	paymentDetails.addCell(spaceCell);
	CustAddress custAddress = new CustAddressDAO().getCustAddressForCheck(vendorNumber);
	StringBuilder customerAddress = new StringBuilder(vendorName + "\n");
	if (null != custAddress) {
	    customerAddress.append(custAddress.getAddress1() != null ? custAddress.getAddress1() + "\n" : "");
	    customerAddress.append(custAddress.getCity1() != null ? custAddress.getCity1() + ", " : "");
	    customerAddress.append(custAddress.getState() != null ? custAddress.getState() + " " : "");
	    customerAddress.append(custAddress.getZip() != null ? custAddress.getZip() : "");
	}
	Phrase phrase = new Phrase(customerAddress.toString(), addressFont);
	PdfPCell addressCell = makeCell(phrase, Element.ALIGN_LEFT);
	paymentDetails.addCell(checkPrintingCell(""));
	paymentDetails.addCell(addressCell);
	paymentDetails.addCell(checkPrintingCell(""));
	paymentDetails.addCell(checkPrintingCell(""));
	checkDocument.add(paymentDetails);
	emptyTable = new PdfPTable(1);
	for (int i = 0; i < 25; i++) {
	    emptyTable.addCell(emptyCell);
	}
	checkDocument.add(emptyTable);


	PdfPTable vendorDetails = makeTable(3);
	vendorDetails.setWidthPercentage(100);
	vendorDetails.setWidths(new float[]{50, 30, 20});
	vendorDetails.addCell(makeCell("Vendor:(" + tradingPartner.getAccountno() + ")" + tradingPartner.getAccountName(),
		Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
	vendorDetails.addCell(makeCell("Check #: " + checkNumber, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
	vendorDetails.addCell(makeCell(paymentDate, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));

	PdfPTable invoicesTable = makeTable(3);
	invoicesTable.setWidthPercentage(100);
	invoicesTable.setWidths(new float[]{40, 40, 20});
	emptyCell.setColspan(3);
	invoicesTable.addCell(emptyCell);
	PdfPTable invoicesTable1 = makeTable(3);
	invoicesTable1.setWidthPercentage(100);
	invoicesTable1.setWidths(new float[]{50, 30, 20});
	PdfPTable invoicesTable2 = makeTable(3);
	invoicesTable2.setWidthPercentage(100);
	invoicesTable2.setWidths(new float[]{50, 30, 20});
	int i = 0;
	boolean overflow = false;
	String invoice = "";
	if (invoiceList.size() > 30) {
	    int limit = invoiceList.size() % 2 == 0 ? invoiceList.size() / 2 : invoiceList.size() / 2 + 1;
	    overflow = true;
	    for (TransactionBean invoiceBean : invoiceList) {
		invoice += ",'" + invoiceBean.getInvoiceOrBl() + "'";
		if (i < limit) {
		    invoicesTable1.addCell(makeCell(invoiceBean.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		    invoicesTable1.addCell(makeCell(invoiceBean.getInvoiceDate(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		    invoicesTable1.addCell(makeCell(invoiceBean.getAmount(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		} else {
		    invoicesTable2.addCell(makeCell(invoiceBean.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		    invoicesTable2.addCell(makeCell(invoiceBean.getInvoiceDate(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		    invoicesTable2.addCell(makeCell(invoiceBean.getAmount(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		}
		i++;
	    }
	} else {
	    for (TransactionBean invoiceBean : invoiceList) {
		invoice += ",'" + invoiceBean.getInvoiceOrBl() + "'";
		if (i < 15) {
		    invoicesTable1.addCell(makeCell(invoiceBean.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		    invoicesTable1.addCell(makeCell(invoiceBean.getInvoiceDate(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		    invoicesTable1.addCell(makeCell(invoiceBean.getAmount(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		} else {
		    invoicesTable2.addCell(makeCell(invoiceBean.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		    invoicesTable2.addCell(makeCell(invoiceBean.getInvoiceDate(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		    invoicesTable2.addCell(makeCell(invoiceBean.getAmount(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER));
		}
		i++;
	    }
	    for (int l = i; l < 15; l++) {
		PdfPCell pdfPCell = makeCell(" ", Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, voucherFont, Rectangle.NO_BORDER);
		pdfPCell.setColspan(3);
		invoicesTable1.addCell(pdfPCell);
	    }
	}
	PdfPCell invoicesTable1Cell = checkPrintingCell("");
	invoicesTable1Cell.addElement(invoicesTable1);
	invoicesTable.addCell(invoicesTable1Cell);
	PdfPCell invoicesTable2Cell = checkPrintingCell("");
	invoicesTable2Cell.addElement(invoicesTable2);
	invoicesTable.addCell(invoicesTable2Cell);
	emptyCell.setColspan(1);
	invoicesTable.addCell(emptyCell);
	invoicesTable.addCell(emptyCell);
	invoicesTable.addCell(emptyCell);
	invoicesTable.addCell(checkPrintingCell(StringUtils.leftPad("$" + number.format(totalDollars) + "  ", 20, "")));
	invoice = StringUtils.removeStart(invoice, ",");
	List<TransactionBean> chargesList = new TransactionLedgerDAO().getChargeCodeForInvoice(invoice, tradingPartner.getAccountno());
	PdfPTable chargesTable = makeTable(3);
	chargesTable.setWidthPercentage(100);
	chargesTable.setWidths(new float[]{40, 40, 20});
	emptyCell.setColspan(3);
	chargesTable.addCell(emptyCell);
	PdfPTable chargesTable1 = makeTable(4);
	chargesTable1.setWidthPercentage(100);
	chargesTable1.setWidths(new float[]{40, 20, 20, 20});
	PdfPTable chargesTable2 = makeTable(4);
	chargesTable2.setWidthPercentage(100);
	chargesTable2.setWidths(new float[]{40, 20, 20, 20});
	i = 0;
	if (chargesList.size() > 30) {
	    int limit = chargesList.size() % 2 == 0 ? chargesList.size() / 2 : chargesList.size() / 2 + 1;
	    overflow = true;
	    for (TransactionBean chargeBean : chargesList) {
		if (i < limit) {
		    chargesTable1.addCell(makeCell(chargeBean.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable1.addCell(makeCell(chargeBean.getInvoiceDate(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable1.addCell(makeCell(chargeBean.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable1.addCell(makeCell(chargeBean.getAmount(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		} else {
		    chargesTable2.addCell(makeCell(chargeBean.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable2.addCell(makeCell(chargeBean.getInvoiceDate(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable2.addCell(makeCell(chargeBean.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable2.addCell(makeCell(chargeBean.getAmount(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		}
		i++;
	    }
	} else {
	    for (TransactionBean chargeBean : chargesList) {
		if (i < 15) {
		    chargesTable1.addCell(makeCell(chargeBean.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable1.addCell(makeCell(chargeBean.getInvoiceDate(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable1.addCell(makeCell(chargeBean.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable1.addCell(makeCell(chargeBean.getAmount(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		} else {
		    chargesTable2.addCell(makeCell(chargeBean.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable2.addCell(makeCell(chargeBean.getInvoiceDate(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable2.addCell(makeCell(chargeBean.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		    chargesTable2.addCell(makeCell(chargeBean.getAmount(), Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER));
		}
		i++;
	    }
	    for (int l = i; l < 15; l++) {
		PdfPCell pdfPCell = makeCell(" ", Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, chargesFont, Rectangle.NO_BORDER);
		pdfPCell.setColspan(4);
		chargesTable1.addCell(pdfPCell);
	    }
	}
	PdfPCell chargesTabl1Cell = checkPrintingCell("");
	chargesTabl1Cell.addElement(chargesTable1);
	chargesTable.addCell(chargesTabl1Cell);
	PdfPCell chargesTable2Cell = checkPrintingCell("");
	chargesTable2Cell.addElement(chargesTable2);
	chargesTable.addCell(chargesTable2Cell);
	emptyCell.setColspan(1);
	chargesTable.addCell(emptyCell);
	chargesTable.addCell(emptyCell);
	chargesTable.addCell(emptyCell);
	chargesTable.addCell(checkPrintingCell(StringUtils.leftPad("$" + number.format(totalDollars) + "  ", 20, "")));
	if (overflow) {
	    checkDocument.add(vendorDetails);
	    PdfPTable overflowList = makeTable(1);
	    overflowList.setWidthPercentage(100);
	    spaceCell.setColspan(1);
	    for (int l = 0; l < 10; l++) {
		overflowList.addCell(spaceCell);
	    }
	    PdfPCell centerCell = checkPrintingCellWithFont11("See Attached....");
	    centerCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	    overflowList.addCell(centerCell);
	    for (int l = 0; l < 10; l++) {
		overflowList.addCell(spaceCell);
	    }
	    for (int l = 0; l < 6; l++) {
		overflowList.addCell(checkPrintingCell(""));
	    }
	    checkDocument.add(overflowList);
	    checkDocument.close();
	    overflowFileName = FilenameUtils.getFullPath(checkFileName) + "Overflow_" + FilenameUtils.getBaseName(checkFileName) + ".pdf";
	    checkDocument = new Document(PageSize.LETTER);
	    checkDocument.setMargins(15, 15, 10, 10);
	    PdfWriter.getInstance(checkDocument, new FileOutputStream(overflowFileName));
	    checkDocument.open();
	    checkDocument.add(vendorDetails);
	    checkDocument.add(invoicesTable);
	    emptyTable = new PdfPTable(1);
	    for (int l = 0; l < 10; l++) {
		emptyTable.addCell(emptyCell);
	    }
	    checkDocument.add(emptyTable);
	    checkDocument.add(vendorDetails);
	    checkDocument.add(chargesTable);
	} else {
	    checkDocument.add(vendorDetails);
	    checkDocument.add(invoicesTable);
	    emptyTable = new PdfPTable(1);
	    for (int l = 0; l < 23; l++) {
		emptyTable.addCell(emptyCell);
	    }
	    checkDocument.add(emptyTable);
	    checkDocument.add(vendorDetails);
	    checkDocument.add(chargesTable);
	}
    }
    protected Font voucherFont = new Font(Font.HELVETICA, 7, 0, Color.BLACK);
    protected Font chargesFont = new Font(Font.HELVETICA, 5, 0, Color.BLACK);

    private PdfPCell checkPrintingCell(String text) {
	return makeCell(text, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, checkPrintingFont, 0);
    }

    private PdfPCell checkPrintingCellWithFont11(String text) {
	return makeCell(text, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, checkPrintingFont11, 0);
    }

    private void destroy() {
	checkDocument.close();
    }

    public String createReport(String checkNumber, String vendorNumber, String fileName, String paymentDate, List invoiceList) throws IOException {
	try {
	    checkFileName = fileName;
	    overflowFileName = null;
	    init();
	    Collections.sort(invoiceList, new Comparator());
	    createBody(checkNumber, vendorNumber, paymentDate, invoiceList);
	    destroy();
	} catch (Exception e) {
	    log.info("createReport failed on " + new Date(),e);
            throw new ExceptionConverter(e);
	} finally {
	    if (null != checkDocument && checkDocument.isOpen()) {
		destroy();
	    }
	    if (null != fileOutputStream) {
		fileOutputStream.close();
	    }
	}
	if (null != overflowFileName) {
	    checkFileName += ":-" + overflowFileName;
	}
	return checkFileName;
    }

    public class Comparator implements java.util.Comparator {

	public int compare(Object obj1, Object obj2) {
	    if (obj1 instanceof TransactionBean && obj2 instanceof TransactionBean) {
		String invoice1 = ((TransactionBean) obj1).getInvoiceOrBl();
		String invoice2 = ((TransactionBean) obj2).getInvoiceOrBl();
		return invoice1.compareTo(invoice2);
	    }
	    return 0;
	}
    }
}
