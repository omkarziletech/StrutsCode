package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingSegregationDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.accounting.model.CompanyModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Nambu
 */
public class FreightInvoiceLclExcelCreator extends BaseExcelCreator implements LclReportConstants {

    public String filePath;
    public String fileId;
    public String realPath;
    private String companyOriginal = "";
    private String webSite = "";
    private LclUtils lclUtils = new LclUtils();
    private CompanyModel company;

    public FreightInvoiceLclExcelCreator() {
    }

    public FreightInvoiceLclExcelCreator(String filePath, String fileId, String realPath) {
        this.filePath = filePath;
        this.fileId = fileId;
        this.realPath = realPath;
    }

    public void createReport() throws Exception{
        try {
            init(filePath, FreightInvoiceWorksheet);
            headingDetails();
            writeIntoFile();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }

    public void headingDetails() throws Exception {

        String shipName = "";
        String consName = "";
        String notyName = "";
        String forwName = "";
        String billToParty = "";
        String billToPartyAc = "";
        String customerPo = "";
        StringBuilder consAddress = new StringBuilder();
        Date pickUpDate = null;
        Date vesselEtd = null;
        String subHouseBl = "";
        String unitNo = "";
        String masterBl = "";
        String trmname = "";
        String trmAddress = "";
        String trmZip = "";
        String originValues = "";
        String destinationValues = "";
        String amsHouseBl = "";
        String billToPartyAcctName ="";

        LclBooking lclBooking = new LCLBookingDAO().findById(Long.valueOf(fileId));
        shipName = null != lclBooking.getShipAcct() ? lclBooking.getShipContact().getCompanyName() : "";
        consName = null != lclBooking.getConsAcct() ? lclBooking.getConsContact().getCompanyName() : "";
        notyName = null != lclBooking.getNotyAcct() ? lclBooking.getNotyContact().getCompanyName() : "";
        forwName = null != lclBooking.getFwdAcct() ? lclBooking.getFwdAcct().getAccountName() : "";

        billToParty = null != lclBooking.getBillToParty() ? lclBooking.getBillToParty() : "";
        if (billToParty.equalsIgnoreCase("C")) {
            billToPartyAc = null != lclBooking.getConsAcct() ? lclBooking.getConsAcct().getAccountno() : "";
             billToPartyAcctName =  null != lclBooking.getConsAcct() ? lclBooking.getConsContact().getCompanyName() : "";
        } else if (billToParty.equalsIgnoreCase("A")) {
            billToPartyAc = null != lclBooking.getSupAcct() ? lclBooking.getSupAcct().getAccountno() : "";
            billToPartyAcctName =  null != lclBooking.getSupAcct() ? lclBooking.getSupAcct().getAccountName() : "";
        } else if (billToParty.equalsIgnoreCase("N")) {
            billToPartyAc = null != lclBooking.getNotyAcct() ? lclBooking.getNotyAcct().getAccountno() : "";
            billToPartyAcctName =  null != lclBooking.getNotyAcct() ? lclBooking.getNotyContact().getCompanyName() : "";
        } else if (billToParty.equalsIgnoreCase("T")) {
            billToPartyAc = null != lclBooking.getThirdPartyAcct() ? lclBooking.getThirdPartyAcct().getAccountno() : "";
             billToPartyAcctName = null != lclBooking.getThirdPartyAcct() ? lclBooking.getThirdPartyAcct().getAccountName() : "";
        }

        originValues = lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfLoading());
        destinationValues = lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfDestination());

        if (lclBooking.getTerminal() != null) {
            RefTerminal terminal = new TerminalDAO().findByTerminalNo(String.valueOf(lclBooking.getTerminal().getTrmnum()));
            trmname = null != terminal ? terminal.getTerminalLocation() : "";
            if (trmname.equalsIgnoreCase("IMPRTS LOS ANGELES")) {
                trmname = "Los Angeles";
            }
            trmAddress = null != terminal ? terminal.getAddres1() : "";
            trmZip = null != terminal ? terminal.getZipcde() : "";
        }
        customerPo = new Lcl3pRefNoDAO().getCustomerPo(fileId);
        CustAddress custAddress = new CustAddressDAO().findByAccountNo(billToPartyAc);
        if (custAddress != null) {
            consAddress.append(billToPartyAcctName).append("\n");
            consAddress.append(custAddress.getAddress1()).append("\n");
            consAddress.append(custAddress.getCity1()).append(" ").append(custAddress.getState()).append(" ").append(custAddress.getZip());
        }

        LclFileNumber lclFileNumber = new LclFileNumberDAO().getByProperty("id", Long.parseLong(fileId));
        List<LclBookingPiece> lclBookingPiece = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        if (lclBookingPiece != null && !lclBookingPiece.isEmpty() && CommonUtils.isNotEmpty(lclBookingPiece.get(0).getLclBookingPieceUnitList())) {
            unitNo = lclBookingPiece.get(0).getLclBookingPieceUnitList().get(0).getLclUnitSs().getLclUnit().getUnitNo();
            masterBl = lclBookingPiece.get(0).getLclBookingPieceUnitList().get(0).getLclUnitSs().getLclUnit().getLclUnitSsManifestList().get(0).getMasterbl();
            vesselEtd = lclBookingPiece.get(0).getLclBookingPieceUnitList().get(0).getLclUnitSs().getLclSsHeader().getLclSsDetailList().get(0).getSta();
        }

        Boolean isSegregationFlag = new LclBookingSegregationDao().isCheckedSegregationDr(Long.parseLong(fileId));
        if (isSegregationFlag) {
            amsHouseBl = new LclBookingImportAmsDAO().getAmsNo(fileId);
        } else {
            amsHouseBl = new LclBookingImportAmsDAO().getAmsNoGroup(fileId);
        }
        if (lclFileNumber.getLclBookingImport() != null) {
            subHouseBl = lclFileNumber.getLclBookingImport().getSubHouseBl();
            pickUpDate = lclFileNumber.getLclBookingImport().getPickupDateTime();
        }
        String acctNumber = checkPayment(billToPartyAc);

        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
//        String companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
        String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
        String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
        String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");

        this.setBrand(fileId);
        createRow();
        createRow();
        createRow();
        createHeaderCell(companyOriginal, subHeaderOneCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, columnIndex, 11);
        createRow();
        resetColumnIndex();
        StringBuilder addressess = new StringBuilder();
        addressess.append(companyAddress).append(". PHONE: ").append(companyPhone).append(". FAX: ").append(companyFax);
        createHeaderCell(addressess.toString(), subHeaderOneCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 11);
        createRow();
        resetColumnIndex();

        createHeaderCell("BILL TO ACCOUNT NO:", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 3);
        createHeaderCell(billToPartyAc, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 4, 5);
        createHeaderCell("INVOICE NO", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 6, 7);
        createHeaderCell("DATE", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 8, 9);
        createHeaderCell("BILLING TM", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 10, 11);
        createRow();
        resetColumnIndex();

        createTextCellWithRow(consAddress.toString(), cellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, columnIndex, 5);
// INVOICE NO       
        createTextCellWithRow(lclFileNumber.getFileNumber(), cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 6, 7);

// DATE
        if (!acctNumber.equals("noCredit")) {
            if (CommonFunctions.isNotNull(acctNumber) && !acctNumber.equals("") && pickUpDate != null) {
                createTextCellWithRow(DateUtils.formatStringDateToAppFormatMMM(pickUpDate), cellStyleCenterBold);
                mergeCells(rowIndex, rowIndex, 8, 9);
            } else if (pickUpDate == null && vesselEtd != null) {
                createTextCellWithRow(DateUtils.formatStringDateToAppFormatMMM(vesselEtd), cellStyleCenterBold);
                mergeCells(rowIndex, rowIndex, 8, 9);
            } else {
                createTextCellWithRow("", cellStyleCenterBold);
                mergeCells(rowIndex, rowIndex, 8, 9);
            }
        } else if (acctNumber.equals("noCredit") && vesselEtd != null) {
            createTextCellWithRow(DateUtils.formatStringDateToAppFormatMMM(vesselEtd), cellStyleCenterBold);
            mergeCells(rowIndex, rowIndex, 8, 9);
        } else {
            createTextCellWithRow("", cellStyleCenterBold);
            mergeCells(rowIndex, rowIndex, 8, 9);
        }
//BILLING TM
        createTextCellWithRow(trmname, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 10, 11);
        createRow();
        resetColumnIndex();

        createCell();
        createCell();
        createCell();
        createCell();
        createCell();
        createCell();
        createTextCell("CUSTOMER REF NO.", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 6, 11);
        createRow();
        resetColumnIndex();
        createCell();
        createCell();
        createCell();
        createCell();
        createCell();
        createCell();
        createTextCell(customerPo, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 6, 11);
        createRow();
        resetColumnIndex();

        createTextCell("CONTAINER NO.", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 2);
        createTextCell("ECI SHIPMENT FILE NO.", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 3, 5);
        createTextCell("ORIGIN", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 6, 8);
        createTextCell("DESTINATION", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 9, 11);
        createRow();
        resetColumnIndex();
//CONTAINER NO
        createTextCell(unitNo, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, columnIndex, 2);
//ECI SHIPMENT FILE NO        
        createTextCell("IMP-" + lclFileNumber.getFileNumber(), cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 3, 5);
//ORIGIN        
        createTextCell(originValues, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 6, 8);
//DESTINATION        
        createTextCell(destinationValues, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 9, 11);
        createRow();
        resetColumnIndex();

        createTextCell("MBL / AWB NUMBER", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 5);
        createTextCell("AMS HOUSE BL", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 6, 8);
        createTextCell("SUB HOUSE BL", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 9, 11);
        createRow();
        resetColumnIndex();

        createTextCell(masterBl, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, columnIndex, 5);
        createTextCell(amsHouseBl, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 6, 8);
        createTextCell(subHouseBl, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 9, 11);
        createRow();
        resetColumnIndex();

        createTextCell("SHIPPER", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 5);
        createTextCell("FORWARDER", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 6, 11);
        createRow();
        resetColumnIndex();
        createTextCell(shipName, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, columnIndex, 5);
        createTextCell(forwName, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 6, 11);
        createRow();
        resetColumnIndex();

        createTextCell("CONSIGNEE", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 5);
        createTextCell("NOTIFY PARTY", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 6, 11);
        createRow();
        resetColumnIndex();
        createTextCell(consName, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, columnIndex, 5);
        createTextCell(notyName, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 6, 11);
        createRow();
        resetColumnIndex();

        createTextCell("MARKS AND NUMBERS", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 2);
        createTextCell("NO.PKGS", subHeaderTwoCellStyleLeft10);
        mergeCells(rowIndex, rowIndex, 3, 3);
        createTextCell("DESCRIPTION OF PACKAGES AND GOODS", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 4, 7);
        createTextCell("GROSS WEIGHT", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 8, 9);
        createTextCell("MEASURE", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 10, 11);
        createRow();
        resetColumnIndex();

        String marksAndNumbers = "";
        String noOfPKGS = "";
        String desCription = "";
        StringBuilder grossWeight = new StringBuilder();
        StringBuilder measure = new StringBuilder();

        for (LclBookingPiece lclBookingPieces : lclBookingPiece) {
            if (lclBookingPieces != null && lclBookingPieces.getMarkNoDesc() != null && !lclBookingPieces.getMarkNoDesc().equals("")) {
                marksAndNumbers = lclBookingPieces.getMarkNoDesc().toUpperCase();
            }
            if (lclBookingPieces != null && lclBookingPieces.getBookedPieceCount() != null && lclBookingPieces.getPackageType().getAbbr01() != null) {
                noOfPKGS = lclBookingPieces.getBookedPieceCount() + " " + lclBookingPieces.getPackageType().getAbbr01();
            }
            if (lclBookingPieces != null && lclBookingPieces.getPieceDesc() != null && !lclBookingPieces.getPieceDesc().equals("")) {
                desCription = lclBookingPieces.getPieceDesc().toUpperCase();
            }
            if (lclBookingPieces != null && lclBookingPieces.getBookedWeightMetric() != null) {
                grossWeight.append(lclBookingPieces.getBookedWeightMetric()).append(" KGS").append("\n").append("\n")
                        .append(lclBookingPieces.getBookedWeightImperial()).append(" LBS");
            }
            if (lclBookingPieces != null && lclBookingPieces.getBookedVolumeMetric() != null) {
                measure.append(lclBookingPieces.getBookedVolumeMetric()).append(" CBM").append("\n").append("\n")
                        .append(lclBookingPieces.getBookedVolumeImperial()).append(" CFT");
            }
        }

        createTextCellWithRow(marksAndNumbers, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, columnIndex, 2);
        createTextCellWithRow(noOfPKGS, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 3, 3);
        createTextCellWithRow(desCription, cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 4, 7);
        createTextCellWithRow(grossWeight.toString(), cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 8, 9);
        createTextCellWithRow(measure.toString(), cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 10, 11);
        createRow();
        resetColumnIndex();

        createHeaderCell("DESCRIPTION", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 7);
        createHeaderCell("CHARGES", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 8, 11);
        createRow();
        resetColumnIndex();

        double total = 0.00;
        String[] billToPartyA;
        billToPartyA = new String[]{"C", "N", "T"};
        List<String> billtoPartyList = Arrays.asList(billToPartyA);
        List<BookingChargesBean> lclBookingAcList = null;
        String code = "";
        String codeDesc = "";

        lclBookingAcList = new LclCostChargeDAO().findBybookingAcId(fileId, billtoPartyList);
        for (int j = 0; j < lclBookingAcList.size(); j++) {

            BookingChargesBean lclBookingAc = (BookingChargesBean) lclBookingAcList.get(j);
            code = CommonUtils.isNotEmpty(lclBookingAc.getChargeCode()) ? lclBookingAc.getChargeCode() : "";
            codeDesc = new GenericCodeDAO().getGenericCodeDesc(code);
            if (CommonUtils.isNotEmpty(codeDesc)) {
                createTextCell(codeDesc, cellStyleRightBold);
                mergeCells(rowIndex, rowIndex, columnIndex, 7);
            } else {
                createTextCell(code, cellStyleRightBold);
                mergeCells(rowIndex, rowIndex, columnIndex, 7);
            }
            createDollarCell((lclBookingAc.getTotalAmt().doubleValue()), cellStyleRightBold);
            mergeCells(rowIndex, rowIndex, 8, 11);
            createRow();
            resetColumnIndex();

            total = total + lclBookingAc.getTotalAmt().doubleValue();

        }
        createCell();
        createCell();
        createCell();
        createCell();
        createTextCell("INVOICE TOTAL", cellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 4, 7);
        createDollarCell(total, cellStyleRightBold);
        mergeCells(rowIndex, rowIndex, 8, 11);
        createRow();
        resetColumnIndex();
        createRow();
        resetColumnIndex();

        boolean lateFeeFlag = false;
        double lateFee = 0.00;
        double payAmount = 0.00;

        if (CommonFunctions.isNotNull(acctNumber) && !acctNumber.equals("") && !acctNumber.equals("noCredit")) {
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(acctNumber);
            if (CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getAccounting())) {
                for (Iterator accountingList = tradingPartner.getAccounting().iterator(); accountingList.hasNext();) {
                    CustomerAccounting customerAccounting = (CustomerAccounting) accountingList.next();
                    if (null != customerAccounting.getLclApplyLateFee() && customerAccounting.getLclApplyLateFee().equals("on")) {
                        lateFeeFlag = true;
                    }
                    break;
                }
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String outDate = "";
        String crtDate = "";

        if (vesselEtd != null) { //vessel arrivalDate         
            outDate = sdf.format(vesselEtd);
        }

        CustomerAccounting customerAccounting = new CustomerAccountingDAO().findByProperty("accountNo", billToPartyAc);

        if (customerAccounting != null && (customerAccounting.getCreditRate() != null && (CommonUtils.isNotEmpty(outDate)) && !acctNumber.equals("noCredit"))) {
            Calendar c = Calendar.getInstance();
//            c.setTime(new Date(outDate));  // Removed Deprecated Warning
             c.setTime(sdf.parse(outDate));// Now use previous date.
            if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 7 Days")) {
                c.add(Calendar.DATE, 7);
                crtDate = sdf.format(c.getTime());// Adding 7 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 15 Days")) {
                c.add(Calendar.DATE, 15);
                crtDate = sdf.format(c.getTime());// Adding 15 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("NET 21 DAYS")) {
                c.add(Calendar.DATE, 21);
                crtDate = sdf.format(c.getTime());// Adding 21 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 30 Days")) {
                c.add(Calendar.DATE, 30);
                crtDate = sdf.format(c.getTime());// Adding 30 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 45 Days")) {
                c.add(Calendar.DATE, 45);
                crtDate = sdf.format(c.getTime());// Adding 45 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 60 Days")) {
                c.add(Calendar.DATE, 60);
                crtDate = sdf.format(c.getTime());// Adding 60 days
            }
        }
        createTextCell("ARRIVAL DATE", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 2);

        if (lateFeeFlag) {
            lateFee = total * 0.015; // 1.5percent calculate
            payAmount = total + lateFee;

            createCell();
            createTextCell("LATE FEE IF NOT PAID BY - " + crtDate, cellStyleCenterBold);
            mergeCells(rowIndex, rowIndex, 4, 8);
            createDollarCell(lateFee, cellStyleRightBold);
            mergeCells(rowIndex, rowIndex, 9, 11);
            createRow();
            resetColumnIndex();

            createTextCell(outDate, cellStyleCenterBold);
            mergeCells(rowIndex, rowIndex, columnIndex, 2);
            createCell();
            createTextCell("PAY THIS AMOUNT IF NOT PAID BY DUE DATE ", cellStyleCenterBold);
            mergeCells(rowIndex, rowIndex, 4, 8);

            createDollarCell(payAmount, cellStyleRightBold);
            mergeCells(rowIndex, rowIndex, 9, 11);
            createRow();
            resetColumnIndex();
            createRow();

        } else {
            createRow();
            resetColumnIndex();
            createTextCell(outDate, cellStyleCenterBold);
            mergeCells(rowIndex, rowIndex, columnIndex, 2);
            createCell();

            createTextCell("PLEASE PAY THIS AMOUNT - " + crtDate, cellStyleCenterBold);
            mergeCells(rowIndex, rowIndex, 4, 8);

            createDollarCell(total, cellStyleRightBold);
            mergeCells(rowIndex, rowIndex, 9, 11);
            createRow();
            resetColumnIndex();

        }

        if (customerAccounting != null && (customerAccounting.getCreditRate() != null && (CommonUtils.isNotEmpty(outDate)))) {
            if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Due Upon Receipt")) {
                createTextCell("INVOICE IS PAYABLE UPON RECEIPT", cellStyleCenterBoldYellow);
                mergeCells(rowIndex, rowIndex, columnIndex, 11);
            } else {
                createTextCell("INVOICE PAYABLE ON OR BEFORE  " + (crtDate), cellStyleCenterBold);
                mergeCells(rowIndex, rowIndex, columnIndex, 11);
            }
        } else {
            createTextCell("INVOICE IS PAYABLE UPON RECEIPT", cellStyleCenterBoldYellow);
            mergeCells(rowIndex, rowIndex, columnIndex, 11);
        }
        createRow();
        resetColumnIndex();
        createRow();
        createRow();
        createHeaderCell("PAYMENT METHODS", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 2);
        createRow();
        resetColumnIndex();

        createHeaderCell("Via Check", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, columnIndex, 3);
        createHeaderCell("Via ACH or Wire Transfer", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 4, 8);
        createHeaderCell("Credit Card Payments", subHeaderTwoCellStyleCenter10);
        mergeCells(rowIndex, rowIndex, 9, 11);
        createRow();
        resetColumnIndex();

        StringBuilder viaCheck = new StringBuilder();
        StringBuilder viaACH = new StringBuilder();
        StringBuilder creditCard = new StringBuilder();
//Via ACH or Wire Transfer
        company = new SystemRulesDAO().getCompanyDetails();
        viaACH.append("Bank: ").append(company.getBankName()).append("\n").append(company.getBankAddress()).append("\n");
        viaACH.append("ABA: ").append(company.getBankAbaNumber()).append("\n");
        viaACH.append("ACCT: ").append(companyOriginal).append("\n");
        viaACH.append("ACCOUNT NO: ").append(company.getBankAccountNumber());
//Via Check
        viaCheck.append("PLEASE REMIT PAYMENT TO").append("\n");
        String creditStatusDomain = "";
        if (null != customerAccounting && null != customerAccounting.getCreditStatus()) {
            creditStatusDomain = customerAccounting.getCreditStatus().getCodedesc();
            if (!("No Credit").equalsIgnoreCase(creditStatusDomain)) {
                viaCheck.append(companyOriginal).append("\n");
                viaCheck.append("2401 N.W. 69TH STREET").append("\n");
                viaCheck.append("Miami, FL  33147");
            } else {
                viaCheck.append(trmAddress).append("\n");
                viaCheck.append(trmname).append(" ").append(trmZip);
            }
        }
//Credit Card Payments
        creditCard.append("If paying via Credit card").append("\n");
        creditCard.append("Please go to:").append("\n");
        creditCard.append(webSite);

        createTextCellWithRowHgt(viaCheck.toString(), cellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, columnIndex, 3);
        createTextCellWithRowHgt(viaACH.toString(), cellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 4, 8);
        createTextCellWithRowHgt(creditCard.toString(), cellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 9, 11);
    }

    public String checkPayment(String billToPartyAc) throws Exception {
        String returnString = "noCredit";
        String crditWarning = "";
        FclBlBC blBC = new FclBlBC();
        String creditDetail = blBC.validateCustomer(billToPartyAc, "");
        if (creditDetail != null && !creditDetail.isEmpty()) {
            String[] chargecode = creditDetail.split("==");
            if (!chargecode[0].equals("")) {
                crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
            }
            if (crditWarning.equals("In Good Standing ") || crditWarning.equals("Credit Hold ")) {
                returnString = billToPartyAc;
            } else {
                returnString = "noCredit";
            }
        }
        return returnString;
    }

    public String setBrand(String fileId) throws Exception {
        String brand = new LclFileNumberDAO().getBusinessUnit(fileId);
        if ("ECI".equalsIgnoreCase(brand)) {
            webSite = LoadLogisoftProperties.getProperty("application.Econo.website");
            companyOriginal = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if ("OTI".equalsIgnoreCase(brand)) {
            webSite = LoadLogisoftProperties.getProperty("application.OTI.website");
            companyOriginal = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        } else {
            webSite = LoadLogisoftProperties.getProperty("application.ECU.website");
            companyOriginal = LoadLogisoftProperties.getProperty("application.ECU.companyname");
        }
        return brand;
    }

    protected void createTextCellWithRowHgt(String value, CellStyle cellStyle) throws IOException {
        createCell();
        cell.setCellValue(value);
        cell.getRow().setHeight((short) 1500);
        cell.setCellStyle(cellStyle);
    }

}
