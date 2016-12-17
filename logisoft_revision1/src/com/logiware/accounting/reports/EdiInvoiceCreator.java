package com.logiware.accounting.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.constants.Company;
import com.logiware.accounting.domain.EdiInvoice;
import com.logiware.accounting.domain.EdiInvoiceBank;
import com.logiware.accounting.domain.EdiInvoiceContainer;
import com.logiware.accounting.domain.EdiInvoiceDetail;
import com.logiware.accounting.domain.EdiInvoiceParty;
import com.logiware.reports.BaseReportCreator;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;
/**
 *
 * @author Lakshmi Naryanan
 */
public class EdiInvoiceCreator extends BaseReportCreator {
private static final Logger log = Logger.getLogger(EdiInvoiceCreator.class);
    private EdiInvoice ediInvoice;

    public EdiInvoiceCreator() {
    }

    public EdiInvoiceCreator(EdiInvoice ediInvoice, String contextPath) {
	this.ediInvoice = ediInvoice;
	this.contextPath = contextPath;
    }

    private void init(String fileName) throws DocumentException, FileNotFoundException {
	document = new Document(PageSize.A4);
	document.setMargins(15, 15, 15, 30);
	writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	writer.setUserunit(1f);
	writer.setPageEvent(new EdiInvoiceCreator(ediInvoice, contextPath));
	document.open();
	writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), writer));
    }

    private void writeEcuLineHeader() throws Exception {
	headerTable = new PdfPTable(1);
	headerTable.setWidthPercentage(100);
	String imagePath = LoadLogisoftProperties.getProperty("edi.invoice.ecu.logo");
	Image image = Image.getInstance(contextPath + imagePath);
	image.scalePercent(75);
	headerTable.addCell(createImageCell(image));
	PdfPTable partiesTable = new PdfPTable(2);
	partiesTable.setWidthPercentage(80);
	partiesTable.setWidths(new float[]{50, 50});
	PdfPTable vendorTable = new PdfPTable(1);
	vendorTable.setWidthPercentage(100);
	vendorTable.setWidths(new float[]{100});
	PdfPTable companyTable = new PdfPTable(1);
	companyTable.setWidthPercentage(100);
	companyTable.setWidths(new float[]{100});
	for (EdiInvoiceParty party : ediInvoice.getEdiInvoiceParties()) {
	    if (CommonUtils.isEqualIgnoreCase(party.getType(), "Vendor")) {
		vendorTable.addCell(createTextCell(party.getName(), headerBoldFont11, Rectangle.NO_BORDER));
		vendorTable.addCell(createTextCell(party.getStreet(), blackNormalFont10, Rectangle.NO_BORDER));
		vendorTable.addCell(createTextCell(party.getZip(), blackNormalFont10, Rectangle.NO_BORDER));
		vendorTable.addCell(createTextCell(party.getCity(), blackNormalFont10, Rectangle.NO_BORDER));
		vendorTable.addCell(createTextCell(party.getCountry(), blackNormalFont10, Rectangle.NO_BORDER));
	    } else {
		companyTable.addCell(createTextCell(party.getName(), headerBoldFont11, Rectangle.NO_BORDER));
		companyTable.addCell(createTextCell(party.getStreet(), blackNormalFont10, Rectangle.NO_BORDER));
		companyTable.addCell(createTextCell(party.getZip(), blackNormalFont10, Rectangle.NO_BORDER));
		companyTable.addCell(createTextCell(party.getCity(), blackNormalFont10, Rectangle.NO_BORDER));
		String country = null != party.getCountry() ? party.getCountry() : "U.S.A.";
		companyTable.addCell(createTextCell(country, blackNormalFont10, Rectangle.NO_BORDER));
	    }
	}
	PdfPCell vendorCell = createEmptyCell(Rectangle.NO_BORDER);
	vendorCell.addElement(vendorTable);
	partiesTable.addCell(vendorCell);
	PdfPCell companyCell = createEmptyCell(Rectangle.NO_BORDER);
	companyCell.addElement(companyTable);
	partiesTable.addCell(companyCell);
	PdfPCell partiesCell = createEmptyCell(Rectangle.NO_BORDER);
	partiesCell.addElement(partiesTable);
	partiesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	headerTable.addCell(partiesCell);
    }

    private void writeMaerskLineHeader() throws Exception {
	headerTable = new PdfPTable(2);
	headerTable.setWidths(new int[]{75, 25});
	headerTable.setWidthPercentage(100);
	String imagePath = LoadLogisoftProperties.getProperty("edi.invoice.maersk.logo");
	Image image = Image.getInstance(contextPath + imagePath);
	image.scalePercent(100);
	headerTable.addCell(createImageCell(image, Element.ALIGN_LEFT));
	StringBuilder address = new StringBuilder();
	address.append("Maersk Agency U.S.A., Inc.\n");
	address.append("9300 Arrowpoint Boulevard\n");
	address.append("Charlotte NC 28273-8136");
	headerTable.addCell(createTextCell(address.toString(), blackNormalFont9, Rectangle.NO_BORDER));
    }

    private void writeHeader() throws Exception {
	if (Company.MAERSK_LINE.equals(ediInvoice.getCompany())) {
	    writeMaerskLineHeader();
	} else {
	    writeEcuLineHeader();
	}
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
	pageTemplate = writer.getDirectContent().createTemplate(20, 10);
	pageTemplate.setBoundingBox(new Rectangle(-20, -20, 20, 50));
	try {
	    helvFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	} catch (Exception e) {
	    log.info("onOpenDocument()in EdiInvoiceCreator failed on " + new Date(),e);
	}
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
	try {
	    writeHeader();
	    document.add(headerTable);
	} catch (Exception e) {
	    log.info("onStartPage()in EdiInvoiceCreator failed on " + new Date(),e);
	}
    }

    private void writeEcuLineContent() throws DocumentException, Exception {
	contentTable = new PdfPTable(1);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{100});
	for (int count = 0; count < 10; count++) {
	    contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	}
	PdfPTable invoiceTable = new PdfPTable(4);
	invoiceTable.setWidthPercentage(100);
	invoiceTable.setWidths(new float[]{10, 40, 10, 40});
	invoiceTable.addCell(createTextCell("Invoice :", blackBoldFont8, Rectangle.NO_BORDER));
	invoiceTable.addCell(createTextCell(ediInvoice.getInvoiceNumber(), blackNormalFont8, Rectangle.NO_BORDER));
	invoiceTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	invoiceTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	invoiceTable.addCell(createTextCell("VVH :", blackBoldFont8, Rectangle.NO_BORDER));
	StringBuilder vvh = new StringBuilder();
	vvh.append(ediInvoice.getEdiInvoiceShippingDetails().getVessel()).append("  ");
	vvh.append(DateUtils.formatDate(ediInvoice.getEdiInvoiceShippingDetails().getDate(), "MM/dd/yyyy"));
	PdfPCell vvhCell = createTextCell(vvh.toString(), blackNormalFont8, Rectangle.NO_BORDER);
	vvhCell.setColspan(3);
	invoiceTable.addCell(vvhCell);
	invoiceTable.addCell(createTextCell("EDI Ref :", blackBoldFont8, Rectangle.NO_BORDER));
	invoiceTable.addCell(createTextCell(ediInvoice.getEdiReference(), blackNormalFont8, Rectangle.NO_BORDER));
	invoiceTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	invoiceTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	invoiceTable.addCell(createTextCell("VAT :", blackBoldFont8, Rectangle.NO_BORDER));
	String vatNumber = "";
	for (EdiInvoiceParty party : ediInvoice.getEdiInvoiceParties()) {
	    if (CommonUtils.isEqualIgnoreCase(party.getType(), "Company")) {
		vatNumber = party.getVatNumber();
		break;
	    }
	}
	invoiceTable.addCell(createTextCell("US  " + vatNumber, blackNormalFont8, Rectangle.NO_BORDER));
	invoiceTable.addCell(createTextCell("Date :", blackBoldFont8, Rectangle.NO_BORDER));
	String date = DateUtils.formatDate(ediInvoice.getInvoiceDate(), "MM/dd/yyyy");
	invoiceTable.addCell(createTextCell(date, blackNormalFont8, Rectangle.NO_BORDER));
	PdfPCell invoiceCell = createEmptyCell(Rectangle.BOTTOM);
	invoiceCell.setBorderWidth(1f);
	invoiceCell.addElement(invoiceTable);
	contentTable.addCell(invoiceCell);
	PdfPTable referenceTable = new PdfPTable(4);
	referenceTable.setWidthPercentage(100);
	referenceTable.setWidths(new float[]{10, 40, 10, 40});
	referenceTable.addCell(createTextCell("Our Ref. :", blackBoldFont8, Rectangle.NO_BORDER));
	referenceTable.addCell(createTextCell(ediInvoice.getOurReference(), blackNormalFont8, Rectangle.NO_BORDER));
	referenceTable.addCell(createTextCell("BL :", blackBoldFont8, Rectangle.NO_BORDER));
	referenceTable.addCell(createTextCell(ediInvoice.getBlNumber(), blackNormalFont8, Rectangle.NO_BORDER));
	referenceTable.addCell(createTextCell("Your Ref. :", blackBoldFont8, Rectangle.NO_BORDER));
	referenceTable.addCell(createTextCell(ediInvoice.getYourReference1(), blackNormalFont8, Rectangle.NO_BORDER));
	referenceTable.addCell(createTextCell("", blackBoldFont8, Rectangle.NO_BORDER));
	referenceTable.addCell(createTextCell(ediInvoice.getYourReference2(), blackNormalFont8, Rectangle.NO_BORDER));
	referenceTable.addCell(createTextCell("Vessel :", blackBoldFont8, Rectangle.NO_BORDER));
	StringBuilder vessel = new StringBuilder();
	vessel.append(ediInvoice.getEdiInvoiceShippingDetails().getVessel()).append("  ");
	vessel.append(DateUtils.formatDate(ediInvoice.getEdiInvoiceShippingDetails().getDate(), "MM-dd-yyyy"));
	referenceTable.addCell(createTextCell(vessel.toString(), blackNormalFont8, Rectangle.NO_BORDER));
	PdfPCell routingCell = createTextCell(ediInvoice.getEdiInvoiceShippingDetails().getRouting(), blackNormalFont8, Rectangle.NO_BORDER);
	routingCell.setColspan(2);
	referenceTable.addCell(routingCell);
	PdfPTable packageTable = new PdfPTable(6);
	packageTable.setWidthPercentage(100);
	packageTable.setWidths(new float[]{10, 20, 8, 12, 9, 41});
	packageTable.addCell(createTextCell("Quantity :", blackBoldFont8, Rectangle.NO_BORDER));
	StringBuilder packageDetails = new StringBuilder();
	if (null != ediInvoice.getEdiInvoiceShippingDetails().getPackageQuantity()) {
	    packageDetails.append(ediInvoice.getEdiInvoiceShippingDetails().getPackageQuantity());
	    packageDetails.append(" ");
	    packageDetails.append(ediInvoice.getEdiInvoiceShippingDetails().getPackageDescription());
	}
	packageTable.addCell(createTextCell(packageDetails.toString(), blackNormalFont8, Rectangle.NO_BORDER));
	packageTable.addCell(createTextCell("Weight :", blackBoldFont8, Rectangle.NO_BORDER));
	packageTable.addCell(createTextCell(ediInvoice.getEdiInvoiceShippingDetails().getWeight(), blackNormalFont8, Rectangle.NO_BORDER));
	packageTable.addCell(createTextCell("Volume :", blackBoldFont8, Rectangle.NO_BORDER));
	packageTable.addCell(createTextCell(ediInvoice.getEdiInvoiceShippingDetails().getVolume(), blackNormalFont8, Rectangle.NO_BORDER));
	PdfPCell packageCell = createEmptyCell(Rectangle.NO_BORDER);
	packageCell.setColspan(4);
	packageCell.addElement(packageTable);
	referenceTable.addCell(packageCell);
	PdfPCell referenceCell = createEmptyCell(Rectangle.BOTTOM);
	referenceCell.setBorderWidth(1f);
	referenceCell.addElement(referenceTable);
	contentTable.addCell(referenceCell);
	PdfPTable detailsTable = new PdfPTable(10);
	detailsTable.setWidthPercentage(100);
	detailsTable.setWidths(new float[]{30, 8, 8, 7, 7, 7, 7, 9, 8, 9});
	detailsTable.getDefaultCell().setLeading(0.25f, 0.25f);
	detailsTable.addCell(createTextCell("Description", blackBoldFont8, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("Qty", blackBoldFont8, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("Code", blackBoldFont8, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("Price", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("Curr.", blackBoldFont8, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("VAT", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("Tax Code", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("Total", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("Rate", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	detailsTable.addCell(createTextCell("Total", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	PdfPCell lineCell = createEmptyCell(Rectangle.TOP);
	lineCell.setColspan(10);
	lineCell.setBorderWidth(1f);
	detailsTable.addCell(lineCell);
	double totalVatExclAmount = 0d;
	double totalVatInclAmount = 0d;
	for (EdiInvoiceDetail detail : ediInvoice.getEdiInvoiceDetails()) {
	    StringBuilder description = new StringBuilder();
	    description.append(detail.getDescription());
	    description.append("  ");
	    description.append(detail.getBlReference());
	    detailsTable.addCell(createTextCell(description.toString(), blackNormalFont8, Rectangle.NO_BORDER));
	    detailsTable.addCell(createTextCell(detail.getQuantity(), blackNormalFont8, Rectangle.NO_BORDER));
	    detailsTable.addCell(createTextCell(detail.getCalculationCode(), blackNormalFont8, Rectangle.NO_BORDER));
	    detailsTable.addCell(createAmountCell(detail.getPrice(), blackNormalFont8, redNormalFont8, Rectangle.NO_BORDER));
	    detailsTable.addCell(createTextCell(detail.getCurrency(), blackNormalFont8, Rectangle.NO_BORDER));
	    detailsTable.addCell(createAmountCell(detail.getVatAmount(), blackNormalFont8, redNormalFont8, Rectangle.NO_BORDER));
	    detailsTable.addCell(createTextCell("", blackNormalFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	    detailsTable.addCell(createAmountCell(detail.getVatIncludedAmount(), blackNormalFont8, redNormalFont8, Rectangle.NO_BORDER));
	    detailsTable.addCell(createAmountCell(detail.getRate(), blackNormalFont8, redNormalFont8, Rectangle.NO_BORDER));
	    double amount = Double.parseDouble(detail.getVatIncludedAmount().replace(",", ""));
	    double rate = Double.parseDouble(detail.getRate().replace(",", ""));
	    String totalAmount = NumberUtils.formatNumber(amount * rate);
	    totalVatExclAmount += (amount * rate) - Double.parseDouble(detail.getVatAmount().replace(",", ""));
	    totalVatInclAmount += (amount * rate);
	    detailsTable.addCell(createAmountCell(totalAmount, blackNormalFont8, redNormalFont8, Rectangle.NO_BORDER));
	}
	PdfPCell emptyCell = createEmptyCell(Rectangle.NO_BORDER);
	emptyCell.setColspan(7);
	detailsTable.addCell(emptyCell);
	PdfPCell totalCell = createTextCell("USD", blackBoldFont8, Rectangle.TOP);
	totalCell.setColspan(2);
	totalCell.setBorderWidth(1f);
	detailsTable.addCell(totalCell);
	PdfPCell invoiceAmountCell = createAmountCell(totalVatInclAmount, blackNormalFont8, redNormalFont8, Rectangle.TOP);
	invoiceAmountCell.setColspan(2);
	invoiceAmountCell.setBorderWidth(1f);
	detailsTable.addCell(invoiceAmountCell);
	PdfPCell detailsCell = createEmptyCell(Rectangle.NO_BORDER);
	detailsCell.addElement(detailsTable);
	contentTable.addCell(detailsCell);
	PdfPTable vatMainTable = new PdfPTable(2);
	vatMainTable.setWidthPercentage(100);
	vatMainTable.setWidths(new float[]{50, 50});
	PdfPTable vatTable = new PdfPTable(3);
	vatTable.setWidthPercentage(100);
	vatTable.setWidths(new float[]{15, 15, 20});
	vatTable.addCell(createTextCell("VAT %", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	vatTable.addCell(createTextCell("VAT", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	vatTable.addCell(createTextCell("Total excl.", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	vatTable.addCell(createAmountCell(ediInvoice.getVatPercentage(), blackNormalFont8, Rectangle.NO_BORDER));
	vatTable.addCell(createAmountCell(ediInvoice.getVatAmount(), blackNormalFont8, redNormalFont8, Rectangle.NO_BORDER));
	vatTable.addCell(createAmountCell(totalVatExclAmount, blackNormalFont8, redNormalFont8, Rectangle.NO_BORDER));
	PdfPCell vatCell = createEmptyCell(Rectangle.BOX);
	vatCell.setBorderWidth(1f);
	vatCell.addElement(vatTable);
	vatMainTable.addCell(vatCell);
	vatMainTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	PdfPCell vatMainCell = createEmptyCell(Rectangle.NO_BORDER);
	vatMainCell.addElement(vatMainTable);
	contentTable.addCell(vatMainCell);
	for (int count = 0; count < 5; count++) {
	    contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	}
	StringBuilder paymentTerms = new StringBuilder();
	paymentTerms.append("Payment Conditions :  ");
	paymentTerms.append(ediInvoice.getPaymentTerms());
	contentTable.addCell(createTextCell(paymentTerms.toString(), blackNormalFont8, Rectangle.NO_BORDER));
	for (int count = 0; count < 10; count++) {
	    contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	}
	PdfPTable banksTable = new PdfPTable(3);
	banksTable.setWidthPercentage(100);
	banksTable.setWidths(new float[]{30, 30, 40});
	for (EdiInvoiceBank bank : ediInvoice.getEdiInvoiceBanks()) {
	    PdfPTable bankTable = new PdfPTable(1);
	    bankTable.setWidthPercentage(100);
	    bankTable.setWidths(new float[]{100});
	    bankTable.addCell(createTextCell(bank.getName(), blackBoldFont8, Rectangle.NO_BORDER));
	    bankTable.addCell(createTextCell(bank.getStreet1(), blackNormalFont8, Rectangle.NO_BORDER));
	    bankTable.addCell(createTextCell(bank.getStreet2(), blackNormalFont8, Rectangle.NO_BORDER));
	    bankTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	    bankTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	    bankTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	    bankTable.addCell(createTextCell("account : " + bank.getAccount(), blackNormalFont8, Rectangle.NO_BORDER));
	    bankTable.addCell(createTextCell("iban : " + bank.getIban(), blackNormalFont8, Rectangle.NO_BORDER));
	    bankTable.addCell(createTextCell("bic : " + bank.getBic(), blackNormalFont8, Rectangle.NO_BORDER));
	    PdfPCell bankCell = createEmptyCell(Rectangle.NO_BORDER);
	    bankCell.addElement(bankTable);
	    banksTable.addCell(bankCell);
	}
	PdfPCell banksCell = createEmptyCell(Rectangle.NO_BORDER);
	banksCell.addElement(banksTable);
	contentTable.addCell(banksCell);
	document.add(contentTable);
    }

    private void writeMaerskLineContent() throws DocumentException, Exception {
	contentTable = new PdfPTable(1);
	contentTable.setWidthPercentage(100);
	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell("Original", headerBoldFont15, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	String invoiceNumber = "EXPORT INVOICE Number: " + ediInvoice.getInvoiceNumber();
	contentTable.addCell(createTextCell(invoiceNumber, headerBoldFont15, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 2f));

	PdfPTable invoiceTable = new PdfPTable(2);
	invoiceTable.setWidthPercentage(100);
	invoiceTable.setWidths(new int[]{60, 40});

	PdfPTable billToPartyTable = new PdfPTable(2);
	billToPartyTable.setWidthPercentage(100);
	billToPartyTable.setWidths(new int[]{30, 70});
	billToPartyTable.addCell(createTextCell("Bill-to Party :", blackNormalFont10, Rectangle.NO_BORDER));
	billToPartyTable.addCell(createTextCell(ediInvoice.getBillToParty(), blackNormalFont10, Rectangle.NO_BORDER));
	PdfPCell billToPartyCell = createEmptyCell(Rectangle.NO_BORDER);
	billToPartyCell.addElement(billToPartyTable);
	invoiceTable.addCell(billToPartyCell);

	PdfPTable dateTable = new PdfPTable(2);
	dateTable.setWidthPercentage(100);
	dateTable.setWidths(new int[]{50, 50});
	dateTable.addCell(createTextCell("Invoice Date:", blackNormalFont10, Rectangle.NO_BORDER));
	dateTable.addCell(createTextCell(DateUtils.formatDate(ediInvoice.getInvoiceDate(), "MM-dd-yyyy"), blackNormalFont10, Rectangle.NO_BORDER));
	dateTable.addCell(createTextCell("Due Date:", blackNormalFont10, Rectangle.NO_BORDER));
	dateTable.addCell(createTextCell(DateUtils.formatDate(ediInvoice.getDueDate(), "MM-dd-yyyy"), blackNormalFont10, Rectangle.NO_BORDER));
	dateTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	dateTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	dateTable.addCell(createTextCell("Payment terms:", blackNormalFont10, Rectangle.NO_BORDER));
	dateTable.addCell(createTextCell(ediInvoice.getPaymentTerms(), blackNormalFont10, Rectangle.NO_BORDER));
	PdfPCell dateCell = createEmptyCell(Rectangle.NO_BORDER);
	dateCell.addElement(dateTable);
	invoiceTable.addCell(dateCell);

	PdfPCell invoiceCell = createEmptyCell(Rectangle.NO_BORDER);
	invoiceCell.addElement(invoiceTable);
	contentTable.addCell(invoiceCell);

	PdfPTable amountTable = new PdfPTable(4);
	amountTable.setWidthPercentage(100);
	amountTable.setWidths(new int[]{40, 20, 20, 20});
	amountTable.addCell(createTextCell("Total Amount Due", headerBoldFont13, Rectangle.BOTTOM, 3f, 4));
	amountTable.addCell(createTextCell("Condition", headerBoldFont13, Rectangle.BOTTOM, 1.5f));
	amountTable.addCell(createTextCell("Rate", headerBoldFont13, Rectangle.BOTTOM, 1.5f));
	amountTable.addCell(createTextCell("Base Value", headerBoldFont13, Rectangle.BOTTOM, 1.5f));
	String currency = ediInvoice.getCurrency();
	amountTable.addCell(createTextCell("Total(" + currency + ")", headerBoldFont13, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 1.5f));
	amountTable.addCell(createTextCell("Net value", blackNormalFont10, Rectangle.NO_BORDER, 1f, 3));
	String invoiceAmount = NumberUtils.formatNumber(ediInvoice.getInvoiceAmount());
	amountTable.addCell(createTextCell(invoiceAmount, blackNormalFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	amountTable.addCell(createTextCell("0% Non EU services VAT", blackNormalFont10, Rectangle.BOTTOM, 3f));
	amountTable.addCell(createTextCell("0.00 %", blackNormalFont10, Rectangle.BOTTOM, 3f));
	amountTable.addCell(createTextCell(invoiceAmount, blackNormalFont10, Element.ALIGN_CENTER, Rectangle.BOTTOM, 3f));
	amountTable.addCell(createTextCell("0.00", blackNormalFont10, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 3f));
	amountTable.addCell(createTextCell("Amount Due", blackNormalFont10, Rectangle.BOTTOM, 3f, 3));
	amountTable.addCell(createTextCell(invoiceAmount, blackNormalFont10, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 3f));
	PdfPCell amountCell = createEmptyCell(Rectangle.NO_BORDER);
	amountCell.addElement(amountTable);
	contentTable.addCell(amountCell);

	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

	PdfPTable blTable = new PdfPTable(2);
	blTable.setWidthPercentage(100);
	blTable.setWidths(new int[]{60, 40});
	blTable.addCell(createTextCell("Bill of Lading Number: " + ediInvoice.getBlNumber(), headerBoldFont13, Rectangle.NO_BORDER));
	blTable.addCell(createTextCell("Your Reference: " + ediInvoice.getYourReference1(), headerBoldFont13, Rectangle.NO_BORDER));
	PdfPCell blCell = createEmptyCell(Rectangle.NO_BORDER);
	blCell.addElement(blTable);
	contentTable.addCell(blCell);

	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

	PdfPTable tradeRouteTable = new PdfPTable(3);
	tradeRouteTable.setWidthPercentage(100);
	tradeRouteTable.setWidths(new int[]{30, 30, 40});
	tradeRouteTable.addCell(createEmptyCell(Rectangle.BOTTOM, 1.5f, 3));
	tradeRouteTable.addCell(createTextCell("POL: " + ediInvoice.getPortOfLoading(), blackNormalFont10, Rectangle.NO_BORDER));
	tradeRouteTable.addCell(createTextCell("Place of Receipt: " + ediInvoice.getPlaceOfReceipt(), blackNormalFont10, Rectangle.NO_BORDER));
	String vesselAndVoayge = "Vessel/Voyage: " + ediInvoice.getVesselName() + "/" + ediInvoice.getVoyageNumber();
	tradeRouteTable.addCell(createTextCell(vesselAndVoayge, blackNormalFont10, Rectangle.NO_BORDER));
	tradeRouteTable.addCell(createTextCell("POD: " + ediInvoice.getPortOfDischarge(), blackNormalFont10, Rectangle.NO_BORDER));
	tradeRouteTable.addCell(createTextCell("Place of Delivery: " + ediInvoice.getPlaceOfDelivery(), blackNormalFont10, Rectangle.NO_BORDER));
	tradeRouteTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	tradeRouteTable.addCell(createTextCell("ETD: " + DateUtils.formatDate(ediInvoice.getEtd(), "dd-MMM-yyyy"), blackNormalFont10, Rectangle.NO_BORDER));
	tradeRouteTable.addCell(createTextCell("ETA: " + DateUtils.formatDate(ediInvoice.getEta(), "dd-MMM-yyyy"), blackNormalFont10, Rectangle.NO_BORDER));
	tradeRouteTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	PdfPCell routeCell = createEmptyCell(Rectangle.NO_BORDER);
	routeCell.addElement(tradeRouteTable);
	contentTable.addCell(routeCell);

	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

	PdfPTable chargesTable = new PdfPTable(5);
	chargesTable.setWidthPercentage(100);
	chargesTable.setWidths(new int[]{35, 10, 15, 20, 20});
	chargesTable.addCell(createTextCell("Description of Charges", headerBoldFont13, Rectangle.BOTTOM, 1.5f));
	chargesTable.addCell(createTextCell("Qty", headerBoldFont13, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 1.5f));
	chargesTable.addCell(createTextCell("UoM", headerBoldFont13, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 1.5f));
	chargesTable.addCell(createTextCell("Unit Price", headerBoldFont13, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 1.5f));
	chargesTable.addCell(createTextCell("Total(" + currency + ")", headerBoldFont13, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 1.5f));
	for (EdiInvoiceDetail charge : ediInvoice.getEdiInvoiceDetails()) {
	    chargesTable.addCell(createTextCell(charge.getDescription(), blackNormalFont10, Rectangle.NO_BORDER));
	    chargesTable.addCell(createTextCell(charge.getQuantity(), blackNormalFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	    chargesTable.addCell(createTextCell(charge.getUom(), blackNormalFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	    chargesTable.addCell(createTextCell(charge.getPrice(), blackNormalFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	    String total = NumberUtils.formatNumber(NumberUtils.parseNumber(charge.getQuantity()) * NumberUtils.parseNumber(charge.getPrice()));
	    chargesTable.addCell(createTextCell(total, blackNormalFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	}
	chargesTable.addCell(createTextCell("Total Due:", headerBoldFont13, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 1.5f, 4));
	chargesTable.addCell(createTextCell(invoiceAmount, headerBoldFont13, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 1.5f));
	PdfPCell chargesCell = createEmptyCell(Rectangle.NO_BORDER);
	chargesCell.addElement(chargesTable);
	contentTable.addCell(chargesCell);

	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	contentTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

	PdfPTable containerTable = new PdfPTable(3);
	containerTable.setWidthPercentage(100);
	containerTable.setWidths(new int[]{5, 35, 60});
	containerTable.addCell(createTextCell("No", headerBoldFont13, Rectangle.BOTTOM, 1.5f));
	containerTable.addCell(createTextCell("Container No", headerBoldFont13, Rectangle.BOTTOM, 1.5f));
	containerTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	int count = 1;
	for (EdiInvoiceContainer container : ediInvoice.getEdiInvoiceContainers()) {
	    containerTable.addCell(createTextCell("" + count, blackNormalFont10, Rectangle.NO_BORDER));
	    containerTable.addCell(createTextCell(container.getContainerNumber(), blackNormalFont10, Rectangle.NO_BORDER));
	    containerTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	    count++;
	}
	PdfPCell containerCell = createEmptyCell(Rectangle.NO_BORDER);
	containerCell.addElement(containerTable);
	contentTable.addCell(containerCell);

	document.add(contentTable);
    }

    private void writeContent() throws DocumentException, Exception {
	if (Company.MAERSK_LINE.equals(ediInvoice.getCompany())) {
	    writeMaerskLineContent();
	} else {
	    writeEcuLineContent();
	}
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
	PdfContentByte cb = writer.getDirectContent();
	cb.saveState();
	String text = "Page " + writer.getPageNumber() + " of ";
	float textBase = document.bottom() - 20;
	float textSize = helvFont.getWidthPoint(text, 12);
	cb.beginText();
	cb.setFontAndSize(helvFont, 12);
	cb.setTextMatrix((document.right() / 2) - (textSize / 2), textBase);
	cb.showText(text);
	cb.endText();
	cb.addTemplate(pageTemplate, (document.right() / 2) + (textSize / 2), textBase);
	cb.restoreState();
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
	pageTemplate.beginText();
	pageTemplate.setFontAndSize(helvFont, 12);
	pageTemplate.setTextMatrix(0, 0);
	pageTemplate.showText(String.valueOf(writer.getPageNumber() - 1));
	pageTemplate.endText();
    }

    private void exit() {
	document.close();
    }

    public String create() throws DocumentException, FileNotFoundException, Exception {
	StringBuilder fileName = new StringBuilder();
	fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/EdiInvoice/");
	fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd/"));
	File file = new File(fileName.toString());
	if (!file.exists()) {
	    file.mkdirs();
	}
	fileName.append(ediInvoice.getInvoiceNumber().replaceAll("[^a-zA-Z0-9]+", ""));
	fileName.append(".pdf");
	init(fileName.toString());
	writeContent();
	exit();
	return fileName.toString();
    }
}
