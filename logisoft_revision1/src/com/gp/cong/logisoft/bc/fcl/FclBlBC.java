package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.GenerateFileNumber;
import com.gp.cong.hibernate.FclBlNew;
import com.gp.cong.lcl.dwr.LclPrintUtil;
import com.gp.cong.logisoft.bc.accounting.ArRedInvoiceBC;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.bc.ratemanagement.GenericCodeBC;
import com.gp.cong.logisoft.bc.scan.ScanBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.domain.AgencyInfo;
import com.gp.cong.logisoft.domain.AgencyRules;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.CustomerTemp;
import com.gp.cong.logisoft.domain.FCLPortConfiguration;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.FCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.ScanDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.reports.ArRedInvoicePdfCreator;
import com.gp.cong.logisoft.reports.AuthorityToMakeEntryPdfCreator;
import com.gp.cong.logisoft.reports.BLPdfConfirmOnBoardNotice;
import com.gp.cong.logisoft.reports.BillOfLaddingPdfCreator;
import com.gp.cong.logisoft.reports.ContainerResponsibilityWaiverPdfCreater;
import com.gp.cong.logisoft.reports.DeliveryOrderPdfCreator;
import com.gp.cong.logisoft.reports.ManifestBLPdfCreator;
import com.gp.cong.logisoft.reports.SteamShipBLPdfCreator;
import com.gp.cong.logisoft.reports.VGMDeclarationReport;
import com.gp.cong.logisoft.reports.freightInvoiceBLPDFCreator;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.logisoft.web.HibernateSessionRequestFilter;
import com.gp.cong.struts.LoadApplicationProperties;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.Comparator;
import com.gp.cvst.logisoft.beans.FclBlChargeBean;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BlVoidDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.FCLHazMatForm;
import com.gp.cvst.logisoft.struts.form.FclBLForm;
import com.gp.cvst.logisoft.struts.form.FclBillLaddingForm;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.dao.FclManifestDAO;
import com.logiware.accounting.model.CostModel;
import com.logiware.action.ARRedInvoiceAction;
import com.logiware.bc.EventsBC;
import com.logiware.form.ARRedInvoiceForm;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.dao.FclDAO;
import com.logiware.hibernate.dao.PaymentReleaseDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.struts.util.MessageResources;
import org.directwebremoting.WebContextFactory;

public class FclBlBC {

    FclBillLaddingForm fclBillLaddingForm = new FclBillLaddingForm();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    FclBlDAO fclBlDAO = new FclBlDAO();
    BookingFclDAO bookingFclDAO = new BookingFclDAO();
    QuotationDAO quotationDAO = new QuotationDAO();
    FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
    TransactionDAO transactionDAO = new TransactionDAO();
    GlMappingDAO glMappingDAO = new GlMappingDAO();
    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
    FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
    FclBlUtil fclBlUtil = new FclBlUtil();
    CustomerDAO custAddressDAO = new CustomerDAO();
    HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
    FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
    SedFilingsDAO sedFilingsDAO = new SedFilingsDAO();
    NotesBC notesBC = new NotesBC();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
    GenericCodeBC genericCodeBC = new GenericCodeBC();
    Quotation quotation = new Quotation();
    BookingFcl bookingFcl = new BookingFcl();
    FclBlContainer fclBlContainer = new FclBlContainer();
    SedFilings sedFilings = new SedFilings();
    QuotationBC quotationBC = new QuotationBC();
    NotesDAO notesDAO = new NotesDAO();
    FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
    ScanBC scanBC = new ScanBC();
    DBUtil dbUtil = new DBUtil();
    PortsDAO portsDAO = new PortsDAO();
    ManifestBC manifestBc = new ManifestBC();
    NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");

    public FclBillLaddingForm setFclBlintoFclBillLaddingform(FclBl fclBl) throws Exception {
        fclBillLaddingForm.setAccountName(fclBl.getShipperName());
        fclBillLaddingForm.setHouseName(fclBl.getHouseShipperName());
        fclBillLaddingForm.setShipper(fclBl.getShipperNo());
        fclBillLaddingForm.setHouseShipper(fclBl.getHouseShipper());
        fclBillLaddingForm.setStreamShip(fclBl.getShipperAddress());
        fclBillLaddingForm.setHouseShipper1(fclBl.getHouseShipperAddress());
        fclBillLaddingForm.setConsigneeName(fclBl.getConsigneeName());
        fclBillLaddingForm.setHouseConsigneeName(fclBl.getHouseConsigneeName());
        fclBillLaddingForm.setConsignee(fclBl.getConsigneeNo());
        fclBillLaddingForm.setHouseConsignee(fclBl.getHouseConsignee());
        fclBillLaddingForm.setStreamShipConsignee(fclBl.getConsigneeAddress());
        fclBillLaddingForm.setHouseConsignee1(fclBl.getHouseConsigneeAddress());
        fclBillLaddingForm.setNotifyPartyName(fclBl.getNotifyPartyName());
        fclBillLaddingForm.setHouseNotifyPartyName(fclBl.getHouseNotifyPartyName());
        fclBillLaddingForm.setNotifyParty(fclBl.getNotifyParty());
        fclBillLaddingForm.setHouseNotifyParty(fclBl.getHouseNotifyPartyNo());
        fclBillLaddingForm.setStreamshipNotifyParty(fclBl.getStreamshipNotifyParty());
        fclBillLaddingForm.setHouseNotifyPartyaddress(fclBl.getHouseNotifyParty());
        if (fclBl.getStreamShipBl().equals("P-Prepaid")) {
            fclBillLaddingForm.setStreamShipBL("P");
        } else if (fclBl.getStreamShipBl().equals("C-Collect")) {
            fclBillLaddingForm.setStreamShipBL("C");
        } else if (fclBl.getStreamShipBl().equals("T-Third Party")) {
            fclBillLaddingForm.setStreamShipBL("T");
        }
        if (fclBl.getHouseBl().equals("P-Prepaid")) {
            fclBillLaddingForm.setHouseBL("P");
        } else if (fclBl.getHouseBl().equals("C-Collect")) {
            fclBillLaddingForm.setHouseBL("C");
        } else if (fclBl.getHouseBl().equals("T-Third Party")) {
            fclBillLaddingForm.setHouseBL("T");
        }

        if (fclBl.getDestinationChargesPreCol().equals("P-Prepaid")) {
            fclBillLaddingForm.setDestinationChargesPreCol("P");
        } else if (fclBl.getDestinationChargesPreCol().equals("C-Collect")) {
            fclBillLaddingForm.setDestinationChargesPreCol("C");
        }
        fclBillLaddingForm.setBkgNo(fclBl.getBkgNo());
        fclBillLaddingForm.setTerminalName(fclBl.getTerminal());
        fclBillLaddingForm.setPortofladding(fclBl.getPortOfLoading());
        fclBillLaddingForm.setPortofdischarge(fclBl.getPortofDischarge());
        fclBillLaddingForm.setFinalDestination(fclBl.getFinalDestination());
        fclBillLaddingForm.setOPrinting(fclBl.getOPrinting());
        fclBillLaddingForm.setNPrinting(fclBl.getNPrinting());
        fclBillLaddingForm.setBLPrinting(fclBl.getBLPrinting());
        fclBillLaddingForm.setNoOfOriginals(fclBl.getNoOfOriginals());
        fclBillLaddingForm.setBillofladding(fclBl.getBolId());
        fclBillLaddingForm.setBillofdate(simpleDateFormat.format(fclBl.getBolDate()));
        fclBillLaddingForm.setBookingId(fclBl.getBookingNo());
        if (fclBl.getQuuoteNo() != null && !fclBl.getQuuoteNo().equals("")) {
            fclBillLaddingForm.setQuote(Integer.parseInt(fclBl.getQuuoteNo()));
        }
        fclBillLaddingForm.setStreamShipName(fclBl.getSslineName());
        fclBillLaddingForm.setSslinenumber(fclBl.getSslineNo());
        if (fclBl.getVessel() != null && fclBl.getVessel().getCodedesc() != null) {
            fclBillLaddingForm.setVesselname(fclBl.getVessel().getCodedesc());
            fclBillLaddingForm.setVessel(fclBl.getVessel().getCode());
        }
        fclBillLaddingForm.setVoyage(fclBl.getVoyages());
        fclBillLaddingForm.setImporigBL(fclBl.getImportOrginBlno());
        if (fclBl.getSailDate() != null && !fclBl.getSailDate().equals("")) {
            String date2 = DateUtils.formatDate(fclBl.getSailDate(), "MM/dd/yyyy");
            fclBillLaddingForm.setSailDate(date2);
        }
        if (fclBl.getPortCutOff() != null && !fclBl.getPortCutOff().equals("")) {
            String date3 = DateUtils.formatDate(fclBl.getPortCutOff(), "MM/dd/yyyy HH:mm:ss a");
            fclBillLaddingForm.setPortCutOff(date3);
        }
        if (fclBl.getDocCutOff() != null && !fclBl.getDocCutOff().equals("")) {
            String date4 = DateUtils.formatDate(fclBl.getDocCutOff(), "MM/dd/yyyy HH:mm:ss a");
            fclBillLaddingForm.setDocCutOff(date4);
        }

        if (fclBl.getEta() != null && !fclBl.getEta().equals("")) {
            String date3 = DateUtils.formatDate(fclBl.getEta(), "MM/dd/yyyy");
            fclBillLaddingForm.setSailDate(date3);
        }
        if (fclBl.getEarlierPickUpDate() != null && !fclBl.getEarlierPickUpDate().equals("")) {
            String date4 = DateUtils.formatDate(fclBl.getEarlierPickUpDate(), "MM/dd/yyyy HH:mm:ss a");
            fclBillLaddingForm.setEarlierPickUpDate(date4);
        }
        fclBillLaddingForm.setImportsFreightRelease(fclBl.getImportsFreightRelease());
        fclBillLaddingForm.setInsurance(fclBl.getInsurance());
        fclBillLaddingForm.setOriginalBlRequired(fclBl.getOriginalBlRequired());
        fclBillLaddingForm.setFileType(fclBl.getFileType());
        fclBillLaddingForm.setExportReference(fclBl.getExportReference());
        fclBillLaddingForm.setDomesticRouting(fclBl.getDomesticRouting());
        fclBillLaddingForm.setOnwardInlandRouting(fclBl.getOnwardInlandRouting());
        fclBillLaddingForm.setForwardingAgentName(fclBl.getForwardingAgentName());
        fclBillLaddingForm.setForwardingAgent1(fclBl.getForwardAgentNo());
        fclBillLaddingForm.setForwardingAgentno(fclBl.getForwardingAgent());
        fclBillLaddingForm.setPortofCountryandOrigin(fclBl.getPointOfCountryAndOrigin());
        fclBillLaddingForm.setReadyToPost(fclBl.getReadyToPost());
        fclBillLaddingForm.setShipmentType(fclBl.getShipmentType());
        fclBillLaddingForm.setOriginalTerminal(fclBl.getOriginalTerminal());
        fclBillLaddingForm.setBillingTerminal(fclBl.getBillingTerminal());
        fclBillLaddingForm.setPreCarriage(fclBl.getPreCarriage());
        fclBillLaddingForm.setLoadingPier(fclBl.getLoadingPier());
        fclBillLaddingForm.setFclInttgra(fclBl.getFclInttgra());
        fclBillLaddingForm.setEdiCheckBox(fclBl.getReadyToEDI());
        fclBillLaddingForm.setBlColsed(fclBl.getBlClosed());
        fclBillLaddingForm.setAES(fclBl.getAES());
        fclBillLaddingForm.setMasterCheckBox(fclBl.getMaster());
        fclBillLaddingForm.setAuditCheckBox(fclBl.getBlAudit());
        fclBillLaddingForm.setFileNo(fclBl.getFileNo().toString());
        fclBillLaddingForm.setBol(fclBl.getBol().toString());
        fclBillLaddingForm.setDirectConsignment(fclBl.getDirectConsignment());
        fclBillLaddingForm.setImportWareHouseName(fclBl.getImportWareHouseName());
        fclBillLaddingForm.setImportWareHouseCode(fclBl.getImportWareHouseCode());
        fclBillLaddingForm.setImportWareHouseAddress(fclBl.getImportWareHouseAddress());
        fclBillLaddingForm.setImportPickUpRemarks(fclBl.getImportPickUpRemarks());
        return fclBillLaddingForm;
    }

    public void save(FclBillLaddingForm fclBillLaddingForm, FclBl fclBl) throws Exception {
        Date date = null;
        if (fclBillLaddingForm.getBillofdate() != null) {
            date = simpleDateFormat.parse(fclBillLaddingForm.getBillofdate());
        } else {
            date = new Date();
        }
        if (CommonFunctions.isNotNullOrNotEmpty(date)) {
            fclBl.setBolDate(date);
        } else {
            fclBl.setBolDate(null);
        }
        if (fclBillLaddingForm.getEta() != null && !fclBillLaddingForm.getEta().equals("")) {
            fclBl.setEta(DateUtils.parseToDate(fclBillLaddingForm.getEta()));
        } else {
            fclBl.setEta(null);
        }
        if (fclBillLaddingForm.getEtaFd() != null && !fclBillLaddingForm.getEtaFd().equals("")) {
            fclBl.setEtaFd(DateUtils.parseToDate(fclBillLaddingForm.getEtaFd()));
        } else {
            fclBl.setEtaFd(null);
        }
        if (CommonFunctions.isNotNullOrNotEmpty(fclBillLaddingForm.getDateInYard())) {
            fclBl.setDateInYard(DateUtils.parseToDate(fclBillLaddingForm.getDateInYard()));
        } else {
            fclBl.setDateInYard(null);
        }
        fclBl.setReplaceArrival(fclBillLaddingForm.getReplaceArrival());
        if (CommonFunctions.isNotNullOrNotEmpty(fclBillLaddingForm.getDateOutYard())) {
            fclBl.setDateoutYard(DateUtils.parseToDate(fclBillLaddingForm.getDateOutYard()));
        }
        fclBl.setBlBy(fclBillLaddingForm.getBlBy());
        fclBl.setRatesRemarks(fclBillLaddingForm.getRatesRemarks());
        fclBl.setBookingNo(fclBillLaddingForm.getBooking());
        fclBl.setNewMasterBL(fclBillLaddingForm.getNewMasterBL());
        fclBl.setDestRemarks(fclBillLaddingForm.getPortremarks());
        fclBl.setShipperName(fclBillLaddingForm.getAccountName());
        fclBl.setHouseShipperName(fclBillLaddingForm.getHouseName());
        fclBl.setShipperNo(fclBillLaddingForm.getShipper());
        fclBl.setDirectConsignment(fclBillLaddingForm.getDirectConsignment());
        fclBl.setHouseShipper(fclBillLaddingForm.getHouseShipper());
        fclBl.setShipperAddress(fclBillLaddingForm.getStreamShip());
        fclBl.setBillToCode(fclBillLaddingForm.getBillToCode());
        fclBl.setHouseShipperAddress(fclBillLaddingForm.getHouseShipper1());
        fclBl.setConsigneeName(fclBillLaddingForm.getConsigneeName());
        fclBl.setHouseConsigneeName(fclBillLaddingForm.getHouseConsigneeName());
        fclBl.setConsigneeNo(fclBillLaddingForm.getConsignee());
        fclBl.setHouseConsignee(fclBillLaddingForm.getHouseConsignee());
        fclBl.setConsigneeAddress(fclBillLaddingForm.getStreamShipConsignee());
        fclBl.setHouseConsigneeAddress(fclBillLaddingForm.getHouseConsignee1());
        fclBl.setNotifyPartyName(fclBillLaddingForm.getNotifyPartyName());
        fclBl.setHouseNotifyPartyName(fclBillLaddingForm.getHouseNotifyPartyName());
        fclBl.setNotifyParty(fclBillLaddingForm.getNotifyParty());
        fclBl.setHouseNotifyPartyNo(fclBillLaddingForm.getHouseNotifyParty());
        fclBl.setStreamshipNotifyParty(fclBillLaddingForm.getStreamshipNotifyParty());
        fclBl.setSsBldestinationChargesPreCol(fclBillLaddingForm.getSsBldestinationChargesPreCol());
        fclBl.setHouseNotifyParty(fclBillLaddingForm.getHouseNotifyPartyaddress());
        fclBl.setBillToParty(fclBillLaddingForm.getBillToParty());
        fclBl.setAlternatePort(fclBillLaddingForm.getAlternatePort());
        fclBl.setHblPOL(fclBillLaddingForm.getHblPOL());
        fclBl.setHblPOD(fclBillLaddingForm.getHblPOD());
        fclBl.setHblFD(fclBillLaddingForm.getHblFD());
        fclBl.setInternalRemark(fclBillLaddingForm.getInternalRemark());
        //fclBl.setMoveType(fclBillLaddingForm.getMoveType());
        //fclBl.setZip(fclBillLaddingForm.getZip());
        fclBl.setRampCity(fclBillLaddingForm.getRampCity());
        fclBl.setNoOfDays(fclBillLaddingForm.getNoOfDays());
        fclBl.setRoutedByAgent(fclBillLaddingForm.getRoutedByAgent());
        fclBl.setRoutedByAgentCountry(fclBillLaddingForm.getCountry());

        if (fclBillLaddingForm.getHouseBL().equals("P")) {
            fclBl.setHouseBl("P-Prepaid");
        } else if (fclBillLaddingForm.getHouseBL().equals("C")) {
            fclBl.setHouseBl("C-Collect");
        } else if (fclBillLaddingForm.getHouseBL().equals("B")) {
            fclBl.setHouseBl("B-Both");
        } else if (fclBillLaddingForm.getHouseBL().equals("T")) {
            fclBl.setHouseBl("T-Third Party");
        }
        if (fclBillLaddingForm.getStreamShipBL().equals("P")) {
            fclBl.setStreamShipBl("P-Prepaid");
        } else if (fclBillLaddingForm.getStreamShipBL().equals("C")) {
            fclBl.setStreamShipBl("C-Collect");
        } else if (fclBillLaddingForm.getStreamShipBL().equals("T")) {
            fclBl.setStreamShipBl("T-Third Party");
        }
        if (fclBillLaddingForm.getDestinationChargesPreCol() != null && fclBillLaddingForm.getDestinationChargesPreCol().equals("P")) {
            fclBl.setDestinationChargesPreCol("P-Prepaid");
        } else if (fclBillLaddingForm.getDestinationChargesPreCol() != null && fclBillLaddingForm.getDestinationChargesPreCol().equals("C")) {
            fclBl.setDestinationChargesPreCol("C-Collect");
        }
        if (fclBl.getDestinationChargesPreCol() != null && fclBl.getDestinationChargesPreCol().equals("P-Prepaid")) {
            fclBillLaddingForm.setDestinationChargesPreCol("P");
        } else if (fclBl.getDestinationChargesPreCol() != null && fclBl.getDestinationChargesPreCol().equals("C-Collect")) {
            fclBillLaddingForm.setDestinationChargesPreCol("C");
        }
        fclBl.setBkgNo(fclBillLaddingForm.getBkgNo());
        fclBl.setTerminal(fclBillLaddingForm.getTerminalName());
        fclBl.setPortOfLoading(fclBillLaddingForm.getPortofladding());
        fclBl.setPortofDischarge(fclBillLaddingForm.getPortofdischarge());
        fclBl.setFinalDestination(fclBillLaddingForm.getFinalDestination());
        fclBl.setOPrinting(fclBillLaddingForm.getOriginalBL());
        fclBl.setNPrinting(fclBillLaddingForm.getNPrinting());
        fclBl.setBLPrinting(fclBillLaddingForm.getBLPrinting());
        fclBl.setNoOfOriginals(fclBillLaddingForm.getNoOfOriginals());
        fclBl.setBolId(fclBillLaddingForm.getBillofladding());
        if (CommonFunctions.isNotNull(fclBillLaddingForm.getBillofdate())) {
            fclBl.setBolDate(DateUtils.parseToDate(fclBillLaddingForm.getBillofdate()));
        }
        //fclBl.setBookingNo(fclBillLaddingForm.getBookingId());
        if (fclBillLaddingForm.getQuote() != null && !fclBillLaddingForm.getQuote().equals("")) {
            fclBl.setQuuoteNo(fclBillLaddingForm.getQuote().toString());
        }
        fclBl.setSslineName(fclBillLaddingForm.getStreamShipName());
        fclBl.setSslineNo(fclBillLaddingForm.getSslinenumber());
        if (null == fclBillLaddingForm.getVesselNameCheck()) {
            if (fclBillLaddingForm.getVesselname() != null && !fclBillLaddingForm.getVesselname().equals("")) {
                List genList = (List) genericCodeDAO.findForGenericAction(14, null, fclBillLaddingForm.getVesselname());
                if (genList != null && genList.size() > 0) {
                    GenericCode gen = (GenericCode) genList.get(0);
                    fclBl.setVessel(gen);
                    fclBl.setManualVesselName(fclBillLaddingForm.getManualVesselName());
                    fclBl.setVesselNameCheck(fclBillLaddingForm.getVesselNameCheck());
                }
            } else {
                fclBl.setManualVesselName(fclBillLaddingForm.getManualVesselName());
                fclBl.setVesselNameCheck(fclBillLaddingForm.getVesselNameCheck());
                fclBl.setVessel(null);// need to save Null incase Vessel is not entered
            }
        } else {
            if (fclBillLaddingForm.getManualVesselName() != null && !fclBillLaddingForm.getManualVesselName().equals("")) {
                fclBl.setManualVesselName(fclBillLaddingForm.getManualVesselName());
                fclBl.setVesselNameCheck(fclBillLaddingForm.getVesselNameCheck());
                fclBl.setVessel(null);
            } else {
                fclBl.setManualVesselName(fclBillLaddingForm.getManualVesselName());
                fclBl.setVesselNameCheck(fclBillLaddingForm.getVesselNameCheck());
                fclBl.setVessel(null);
            }
        }
        fclBl.setVoyages(fclBillLaddingForm.getVoyage());
        fclBl.setImportOrginBlno(fclBillLaddingForm.getImporigBL());
        if (fclBillLaddingForm.getSailDate() != null && !fclBillLaddingForm.getSailDate().equals("")) {
            fclBl.setSailDate(DateUtils.parseDate(fclBillLaddingForm.getSailDate(), "MM/dd/yyyy"));
        } else {
            fclBl.setSailDate(null);
        }
        if (fclBillLaddingForm.getPortCutOff() != null && !fclBillLaddingForm.getPortCutOff().equals("")) {
            fclBl.setPortCutOff(DateUtils.parseDate(fclBillLaddingForm.getPortCutOff(), "MM/dd/yyyy HH:mm a"));
        } else {
            fclBl.setPortCutOff(null);
        }
        if (fclBillLaddingForm.getDocCutOff() != null && !fclBillLaddingForm.getDocCutOff().equals("")) {
            fclBl.setDocCutOff(DateUtils.parseDate(fclBillLaddingForm.getDocCutOff(), "MM/dd/yyyy HH:mm a"));
        } else {
            fclBl.setDocCutOff(null);
        }
        if (fclBillLaddingForm.getEarlierPickUpDate() != null && !fclBillLaddingForm.getEarlierPickUpDate().equals("")) {
            fclBl.setEarlierPickUpDate(DateUtils.parseDate(fclBillLaddingForm.getEarlierPickUpDate(), "MM/dd/yyyy HH:mm a"));
        } else {
            fclBl.setEarlierPickUpDate(null);
        }
        fclBl.setInsurance(fclBillLaddingForm.getInsurance());
        fclBl.setOriginalBlRequired(fclBillLaddingForm.getOriginalBlRequired());
        fclBl.setImportsFreightRelease(fclBillLaddingForm.getImportsFreightRelease());
        fclBl.setFileType(fclBillLaddingForm.getFileType());
        fclBl.setExportReference(fclBillLaddingForm.getExportReference());
        fclBl.setDomesticRouting(fclBillLaddingForm.getDomesticRouting());
        fclBl.setOnwardInlandRouting(fclBillLaddingForm.getOnwardInlandRouting());
        fclBl.setForwardingAgentName(fclBillLaddingForm.getForwardingAgentName());
        fclBl.setForwardAgentNo(fclBillLaddingForm.getForwardingAgent1());
        fclBl.setForwardingAgent(fclBillLaddingForm.getForwardingAgentno());
        fclBl.setPointOfCountryAndOrigin(fclBillLaddingForm.getPortofCountryandOrigin());
        fclBl.setShipmentType(fclBillLaddingForm.getShipmentType());
        fclBl.setOriginalTerminal(fclBillLaddingForm.getOriginalTerminal());
        fclBl.setBillingTerminal(fclBillLaddingForm.getBillingTerminal());
        fclBl.setPreCarriage(fclBillLaddingForm.getPreCarriage());
        fclBl.setLoadingPier(fclBillLaddingForm.getLoadingPier());
        if (CommonUtils.isNotEmpty(fclBillLaddingForm.getSslinenumber())) {
            GeneralInformationDAO generalInformationDAO = new GeneralInformationDAO();
            GeneralInformation generalInformation = generalInformationDAO.getGeneralInformationByAccountNumber(fclBillLaddingForm.getSslinenumber());
            if (generalInformation != null && generalInformation.getShippingCode() != null && !generalInformation.getShippingCode().equalsIgnoreCase("N")) {
                fclBl.setFclInttgra(generalInformation.getShippingCode());
            } else {
                fclBl.setFclInttgra("");
            }
            fclBl.setFclInttgra(fclBl.getFclInttgra());
        }
        fclBl.setReadyToEDI(fclBillLaddingForm.getEdiCheckBox());
        fclBl.setBlClosed(fclBillLaddingForm.getBlColsed());
        fclBl.setAES(fclBillLaddingForm.getAES());
        fclBl.setDoorOfDestination(fclBillLaddingForm.getDoorOfDestination());
        fclBl.setDoorOfOrigin(fclBillLaddingForm.getDoorOfOrigin());
        if (!"Yes".equalsIgnoreCase(fclBl.getMaster())) {
            fclBl.setMaster("No");
            new ScanDAO().updateMasterReceived(fclBl.getFileNo(), "No");
        }
        fclBl.setBlAudit(fclBillLaddingForm.getAuditCheckBox());
        fclBl.setFclBLClause(fclBillLaddingForm.getBlClause());
        fclBl.setClauseDescription(fclBillLaddingForm.getClauseDescription());
        fclBl.setPrintPhrase(fclBillLaddingForm.getPrintPhrase());
        fclBl.setAlternatePOL(fclBillLaddingForm.getAlternatePOL());
        fclBl.setManifestPrintReport(fclBillLaddingForm.getManifestPrintReport());
        fclBl.setMoveType(fclBillLaddingForm.getMoveType());
        fclBl.setLineMove(fclBillLaddingForm.getLineMove());
        fclBl.setZip(fclBillLaddingForm.getZip());
        fclBl.setAgent(fclBillLaddingForm.getAgent());
        fclBl.setAgentNo(fclBillLaddingForm.getAgentNo());
        fclBl.setDefaultAgent(fclBillLaddingForm.getDefaultAgent());
        fclBl.setVaoyageInternational(fclBillLaddingForm.getVaoyageInternational());
        fclBl.setCommodityCode(fclBillLaddingForm.getCommodityCode());
        fclBl.setCommodityDesc(fclBillLaddingForm.getCommodityDesc());
        fclBl.setRoutedAgentCheck(fclBillLaddingForm.getRoutedAgentCheck());
        //PRINT OPTIONS......
        fclBl.setAgentsForCarrier(fclBillLaddingForm.getAgentsForCarrier());
        fclBl.setShipperLoadsAndCounts(fclBillLaddingForm.getShipperLoadsAndCounts());
        fclBl.setPrintContainersOnBL(fclBillLaddingForm.getPrintContainersOnBL());
        fclBl.setNoOfPackages(fclBillLaddingForm.getNoOfPackages());
        fclBl.setAlternateNoOfPackages(fclBillLaddingForm.getAlternateNoOfPackages());
        fclBl.setCountryOfOrigin(fclBillLaddingForm.getConturyOfOrigin());
        fclBl.setTotalContainers(fclBillLaddingForm.getTotalContainers());
        fclBl.setProof(fclBillLaddingForm.getProof());
        fclBl.setPreAlert(fclBillLaddingForm.getPreAlert());
        fclBl.setNonNegotiable(fclBillLaddingForm.getNonNegotiable());
        fclBl.setPrintRev(fclBillLaddingForm.getPrintRev());
        fclBl.setDoorOriginAsPlor(fclBillLaddingForm.getDoorOriginAsPlor());
        fclBl.setDoorOriginAsPlorHouse(null != fclBillLaddingForm.getDoorOriginAsPlorHouse() ? fclBillLaddingForm.getDoorOriginAsPlorHouse() : "Yes");
        fclBl.setPrintAlternatePort(fclBillLaddingForm.getPrintAlternatePort());
        fclBl.setHblFDOverride(fclBillLaddingForm.getHblFDOverride());
        fclBl.setHblPODOverride(fclBillLaddingForm.getHblPODOverride());
        fclBl.setHblPOLOverride(fclBillLaddingForm.getHblPOLOverride());
        fclBl.setDoorDestinationAsFinalDeliveryToMaster(fclBillLaddingForm.getDoorDestinationAsFinalDeliveryToMaster());
        fclBl.setDoorDestinationAsFinalDeliveryToHouse(fclBillLaddingForm.getDoorDestinationAsFinalDeliveryToHouse());
        fclBl.setCollectThirdParty(fclBillLaddingForm.getCollectThirdParty());
        fclBl.setTrimTrailingZerosForQty(fclBillLaddingForm.getTrimTrailingZerosForQty());
        fclBl.setCertifiedTrueCopy(fclBillLaddingForm.getCertifiedTrueCopy());
        fclBl.setDockReceipt(fclBillLaddingForm.getDockReceipt());
        fclBl.setOmitTermAndPort(fclBillLaddingForm.getOmitTermAndPort());
        fclBl.setServiceContractNo(fclBillLaddingForm.getServiceContractNo());
        fclBl.setImportAMSHouseBl(fclBillLaddingForm.getImportAMSHosueBlNumber());
        //Check boxes that disables dojo......
        fclBl.setMasterConsigneeCheck(fclBillLaddingForm.getMasterConsigneeCheck());
        fclBl.setMasterNotifyCheck(fclBillLaddingForm.getMasterNotifyCheck());
        fclBl.setEditAgentNameCheck(fclBillLaddingForm.getEditAgentNameCheck());
        fclBl.setConsigneeCheck(fclBillLaddingForm.getConsigneeCheck());
        fclBl.setNotifyCheck(fclBillLaddingForm.getNotifyCheck());
        fclBl.setHouseShipperCheck(fclBillLaddingForm.getHouseShipperCheck());
        fclBl.setEditHouseShipperCheck(fclBillLaddingForm.getEditHouseShipperCheck());
        fclBl.setEditHouseNotifyCheck(fclBillLaddingForm.getEditHouseNotifyCheck());
        fclBl.setEditHouseConsigneeCheck(fclBillLaddingForm.getEditHouseConsigneeCheck());
        fclBl.setEdiConsigneeCheck(fclBillLaddingForm.getEdiConsigneeCheck());
        fclBl.setEdiNotifyPartyCheck(fclBillLaddingForm.getEdiNotifyPartyCheck());
        fclBl.setEdiShipperCheck(fclBillLaddingForm.getEdiShipperCheck());
        fclBl.setEditAgentNameCheck(fclBillLaddingForm.getEditAgentNameCheck());
        fclBl.setCheckNumber(fclBillLaddingForm.getCheckNumber());
        fclBl.setPaymentAmount(CommonUtils.isNotEmpty(fclBillLaddingForm.getPaymentAmount()) ? Double.parseDouble(fclBillLaddingForm.getPaymentAmount().replaceAll(",", "")) : 0.00);
        fclBl.setImportWareHouseName(fclBillLaddingForm.getImportWareHouseName());
        fclBl.setImportWareHouseCode(fclBillLaddingForm.getImportWareHouseCode());
        fclBl.setImportWareHouseAddress(fclBillLaddingForm.getImportWareHouseAddress());
        fclBl.setImportPickUpRemarks(fclBillLaddingForm.getImportPickUpRemarks());
        fclBl.setResendCostToBlue(fclBillLaddingForm.getResendCostToBlue());
        fclBl.setBrand(fclBillLaddingForm.getBrand());
        String thirdPartyName = fclBillLaddingForm.getBillThirdPartyName();
        String billThirdPartyNo = fclBillLaddingForm.getBillTrePty();
        if (null != fclBl.getBol()) {
            fclBlDAO.update(fclBl);
            saveFclBlInFclBlCharges(fclBl, null, null, billThirdPartyNo, thirdPartyName, fclBillLaddingForm.getBillThirdParty());
        } else {
            GenerateFileNumber generateFileNumber = new GenerateFileNumber();// wil generate file number
            generateFileNumber.join();// it wil force thread to complete the task before move to next step
            fclBl.setFileNo(null != generateFileNumber.getFileNumber() ? generateFileNumber.getFileNumber().toString() : null);
            String billNo = new StringFormatter().getBolid(fclBillLaddingForm.getBillingTerminal(), fclBillLaddingForm.getFinalDestination(), fclBl.getFileNo());
            fclBl.setImportFlag(ImportBc.IMPORTFLAG);
            fclBl.setBolId(billNo);
            fclBlDAO.save(fclBl);
        }
    }
    //.........THIS IS FOR BL REPORT.............

    public void createFclBillLadingReport(String id, String outputFileName, String realPath, MessageResources messageResources,
            User user, String documentName) throws Exception {
        BillOfLaddingPdfCreator billOfLaddingPdfCreator = new BillOfLaddingPdfCreator();
        freightInvoiceBLPDFCreator invoiceBLPDFCreator = new freightInvoiceBLPDFCreator();
        BLPdfConfirmOnBoardNotice blPdfConfirmOnBoardNotice = new BLPdfConfirmOnBoardNotice();
        SteamShipBLPdfCreator steamShipBLPdfCreator = new SteamShipBLPdfCreator();
        ManifestBLPdfCreator manifestBLPdfCreator = new ManifestBLPdfCreator();
        ContainerResponsibilityWaiverPdfCreater containerResponsibilityWaiverPdfCreater = new ContainerResponsibilityWaiverPdfCreater();
        AuthorityToMakeEntryPdfCreator authorityToMakeEntryPdfCreator = new AuthorityToMakeEntryPdfCreator();
        VGMDeclarationReport vGMDeclarationReport = new VGMDeclarationReport();
        GenericCodeDAO genericcodeDAO = new GenericCodeDAO();
        StringBuilder valueString = new StringBuilder();
        DeliveryOrderPdfCreator deliveryOrderPdfCreator = new DeliveryOrderPdfCreator();

        //---getting FCL BL Object---
        FclBl fclBl;
        if (null != id && id.indexOf("==") != -1) {
            fclBl = fclBlDAO.findById(id);
        } else if (null != id && id.indexOf("-") != -1) {
            fclBl = fclBlDAO.findById(id);
        } else {
            fclBl = fclBlDAO.findById(Integer.parseInt((null == id || id.trim().equals("")) ? "0" : id.trim()));
        }
        boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
        List commentList = genericcodeDAO.getCommentListForFclBlReport();

        String result = valueString.toString();
        if (documentName.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_SHIPPER)
                || documentName.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_FORWARDER)
                || documentName.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_AGENT)
                || documentName.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY)
                || documentName.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_CONSIGNEE)
                || documentName.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_NOTIFYPARTY)) {
            // all parties freight invoice list
            invoiceBLPDFCreator.createBillOfLaddingReport(fclBl, outputFileName, realPath, messageResources, commentList, user, documentName);
        } else if (documentName.equalsIgnoreCase(PrintReportsConstants.FCL_CONFIRM_ONBOARD_NOTICE)) {
            blPdfConfirmOnBoardNotice.createBillOfLaddingReport(fclBl, outputFileName, realPath, messageResources, user, result);
        } else if (documentName.equalsIgnoreCase(PrintReportsConstants.STEAMSHIPBL)) {
            steamShipBLPdfCreator.createSteamShipBlReport(fclBl, outputFileName, realPath, messageResources, user, commentList);
        } else if (documentName.equalsIgnoreCase(PrintReportsConstants.MANIFESTBL)) {
            manifestBLPdfCreator.createBillOfLaddingReport(fclBl, outputFileName, realPath, messageResources, commentList, user, documentName);
        } else if (documentName.equalsIgnoreCase(PrintReportsConstants.UNMARKED_HOUSE_BILLOFLADDING)) {
            billOfLaddingPdfCreator.createBillOfLaddingReport(fclBl, outputFileName, realPath, messageResources, commentList, user, documentName, importFlag);
        } else if (documentName.equalsIgnoreCase(PrintReportsConstants.CONTAINER_RESPONSIBILITY_WAIVER)) {
            containerResponsibilityWaiverPdfCreater.createContainerResponseReport(fclBl, outputFileName, realPath, messageResources);
        } else if (documentName.equalsIgnoreCase(PrintReportsConstants.AUTHORITY_TO_MAKE_ENTRY)) {
            authorityToMakeEntryPdfCreator.createAuthorityToMakeEntryReport(fclBl, outputFileName, realPath, messageResources);
        } else if (documentName.equalsIgnoreCase(PrintReportsConstants.DELIVERY_ORDER)) {
            deliveryOrderPdfCreator.createDeliveryOrderReport(fclBl, outputFileName, realPath, messageResources);
        } else if (documentName.equalsIgnoreCase(PrintReportsConstants.VGM_DECLARATION)) {
            vGMDeclarationReport.createVgmDeclarationReport(fclBl, outputFileName, realPath);
        } else {
            // All house Bl and master bl prints
            billOfLaddingPdfCreator.createBillOfLaddingReport(fclBl, outputFileName, realPath, messageResources, commentList, user, documentName, importFlag);
        }
    }

    public void saveAATransaction(FclBl fclBl) throws Exception {
        String fclreadyTopOst = "";
        if (fclBl.getReadyToPost() != null) {
            fclreadyTopOst = fclBl.getReadyToPost();
        }
        if (!fclreadyTopOst.equals("M")) {
            List addList = fclBlChargesDAO.findAATransactions(fclBl.getBol());
            for (int i = 0; i < addList.size(); i++) {
                FclBlCharges fclBlCharges = (FclBlCharges) addList.get(i);
                Transaction transaction = new Transaction();
                transaction.setBillLaddingNo(fclBl.getBolId());
                transaction.setTransactionAmt(fclBlCharges.getAmount());
                transaction.setCurrencyCode(fclBlCharges.getCurrencyCode());
                if (fclBlCharges.getBillTo() != null && fclBlCharges.getBillTo().equalsIgnoreCase("Forwarder")) {
                    transaction.setCustName(fclBl.getForwardingAgentName());
                    transaction.setCustNo(fclBl.getForwardAgentNo());
                } else if (fclBlCharges.getBillTo() != null && fclBlCharges.getBillTo().equalsIgnoreCase("Shipper")) {
                    transaction.setCustName(fclBl.getShipperName());
                    transaction.setCustNo(fclBl.getShipperNo());
                } else if (fclBlCharges.getBillTo() != null && fclBlCharges.getBillTo().equalsIgnoreCase("ThirdParty")) {
                    transaction.setCustName(fclBl.getThirdPartyName());
                    transaction.setCustNo(fclBl.getBillTrdPrty());

                } else if (fclBlCharges.getBillTo() != null && fclBlCharges.getBillTo().equalsIgnoreCase("Agent")) {
                    transaction.setCustName(fclBl.getAgentName());
                    transaction.setCustNo(fclBl.getAlternateAgentNo());

                } else {
                    transaction.setCustName(" ");
                    transaction.setCustNo(" ");
                }
                transaction.setBalance(fclBlCharges.getAmount());
                transaction.setBalanceInProcess(fclBlCharges.getAmount());
                transaction.setTransactionType("AA");
                transaction.setStatus("Open");
                transaction.setSubledgerSourceCode("AR-FCLE");
                if (fclBl.getVoyages() != null) {
                    transaction.setVoyageNo(fclBl.getVoyages());
                }
                if (fclBl.getVessel() != null) {
                    transaction.setVesselNo(fclBl.getVessel().getCode());
                }
                transaction.setOrgTerminal(fclBl.getTerminal());
                transactionDAO.save(transaction);
            }
        }
    }

    public void unmanifest(FclBl bl, User user) throws Exception {
        new FclManifestDAO(bl, user).unmanifest();
        setFlagforCostandCharges(bl, null);
        fclBlDAO.update(bl);
    }

    public void manifest(FclBl bl, User user) throws Exception {
        new FclManifestDAO(bl, user).manifest();
        setFlagforCostandCharges(bl, "M");
        fclBlDAO.update(bl);
    }

    public void setFlagforCostandCharges(FclBl bl, String readyToPost) {
        if (bl.getFclcharge() != null) {
            Iterator iter = bl.getFclcharge().iterator();
            while (iter.hasNext()) {
                FclBlCharges charge = (FclBlCharges) iter.next();
                charge.setReadyToPost(readyToPost);
            }
        }
        if (bl.getFclblcostcodes() != null) {
            Iterator iter = bl.getFclblcostcodes().iterator();
            while (iter.hasNext()) {
                FclBlCostCodes cost = (FclBlCostCodes) iter.next();
                cost.setReadyToPost(readyToPost);
                if (CommonUtils.isEmpty(cost.getTransactionType()) || CommonUtils.isEqualIgnoreCase(cost.getTransactionType(), "AC") && CommonUtils.isEmpty(cost.getbookingId())) {
                    cost.setTransactionType("M".equals(readyToPost) ? "AC" : "");
                } else if ((CommonUtils.isEmpty(cost.getTransactionType()) || CommonUtils.isEqualIgnoreCase(cost.getTransactionType(), "AC")) && CommonUtils.isNotEmpty(cost.getbookingId())) {
                    cost.setTransactionType("M".equals(readyToPost) ? "AC" : "AC");
                } else if ((CommonUtils.isEmpty(cost.getTransactionType()) || CommonUtils.isEqualIgnoreCase(cost.getTransactionType(), "AP")) && CommonUtils.isNotEmpty(cost.getbookingId())) {
                    cost.setTransactionType("M".equals(readyToPost) ? "AP" : "AP");
                } else if ((CommonUtils.isEmpty(cost.getTransactionType()) || CommonUtils.isEqualIgnoreCase(cost.getTransactionType(), "DS")) && CommonUtils.isNotEmpty(cost.getbookingId())) {
                    cost.setTransactionType("M".equals(readyToPost) ? "DS" : "DS");
                } else if ((CommonUtils.isEmpty(cost.getTransactionType()) || CommonUtils.isEqualIgnoreCase(cost.getTransactionType(), "IP")) && CommonUtils.isNotEmpty(cost.getbookingId())) {
                    cost.setTransactionType("M".equals(readyToPost) ? "IP" : "IP");
                }
            }
        }
        fclBlCostCodesDAO.getCurrentSession().flush();
        fclBlCostCodesDAO.getCurrentSession().clear();
    }

    public FclBl copy(FclBl fclBl, String bol) throws Exception {
        FclBl copyFclBl = new FclBl();
        PropertyUtils.copyProperties(copyFclBl, fclBl);
        copyFclBl.setBol(null);
        String fileNumber = fclBlDAO.getBOLId(copyFclBl.getFileNo());
        if (copyFclBl.getQuuoteNo() != null && copyFclBl.getQuuoteNo().equals("0")) {
            copyFclBl.setQuuoteNo(null);
        }
        copyFclBl.setLocalDrayage("N");
        copyFclBl.setBolDate(new Date());
        if (fileNumber != null && fileNumber.contains(FclBlConstants.EQUALDELIMITER)) {
            Character ca = fileNumber.charAt(fileNumber.length() - 1);
            ++ca;
            copyFclBl.setFileNo(copyFclBl.getFileNo() + FclBlConstants.EQUALDELIMITER + ca.toString());
            copyFclBl.setBolId(copyFclBl.getBolId() + FclBlConstants.EQUALDELIMITER + ca.toString());
        } else {
            copyFclBl.setFileNo(copyFclBl.getFileNo() + FclBlConstants.EQUALDELIMITER + "A");
            copyFclBl.setBolId(copyFclBl.getBolId() + FclBlConstants.EQUALDELIMITER + "A");
        }
        Set<FclBlContainer> fclContainerSet = new HashSet<FclBlContainer>();
        if (CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclcontainer())) {
            Iterator iter = fclBl.getFclcontainer().iterator();
            while (iter.hasNext()) {
                FclBlContainer originalContainer = (FclBlContainer) iter.next();
                FclBlContainer copyContainer = new FclBlContainer();
                if (originalContainer != null) {
                    PropertyUtils.copyProperties(copyContainer, originalContainer);
                    copyContainer.setBolId(null);
                    copyContainer.setTrailerNoId(null);
                    copyContainer.setFclBlMarks(null);
                    fclContainerSet.add(copyContainer);
                }
            }
        }
        copyFclBl.setFclAesDetails(null);
        copyFclBl.setFclInbondDetails(null);
        copyFclBl.setImportPaymentRelease(null);
        copyFclBl.setFclblcostcodes(null);
        copyFclBl.setFclcharge(null);
        copyFclBl.setFclBlClauses(null);
        copyFclBl.setFclcontainer(fclContainerSet);
        copyFclBl.setReadyToPost(null);
        copyFclBl.setBlAudit(null);
        copyFclBl.setAuditedBy(null);
        copyFclBl.setAuditedDate(null);
        copyFclBl.setBlClosed(null);
        copyFclBl.setClosedBy(null);
        copyFclBl.setClosedDate(null);
        fclBlDAO.save(copyFclBl);
        return copyFclBl;
    }

    public Set setFclContainerValues(FclBillLaddingForm fclcontainerForm, List containerList, HttpServletRequest request) throws Exception {
        Set fclContainerSet = new LinkedHashSet<FclBlContainer>();
        if (containerList.size() > 0) {
            for (Iterator iterator = containerList.iterator(); iterator.hasNext();) {
                FclBlContainer fclBlContainer = (FclBlContainer) iterator.next();
                if (null != fclcontainerForm.getTrailerNo()) {
                    if (fclBlContainer.getTrailerNoId().toString().equals(request.getParameter("id"))) {
                        fclBlContainer.setTrailerNo(fclcontainerForm.getTrailerNo());
                        fclBlContainer.setTrailerNoOld(fclcontainerForm.getTrailerNoOld());
                        fclBlContainer.setSealNo(fclcontainerForm.getSealNo());
                        fclBlContainer.setMarks(fclcontainerForm.getMarksNo());
                        fclBlContainer.setLastUpdate(new Date());
                        if (fclcontainerForm.getSizeLegend() != null && !fclcontainerForm.getSizeLegend().equals("0")) {
                            GenericCode gen = (GenericCode) genericCodeDAO.findById(Integer.parseInt(fclcontainerForm.getSizeLegend()));
                            fclBlContainer.setSizeLegend(gen);
                        }
                        break;
                    }
                }
                fclContainerSet.add(fclBlContainer);
            }
        } else {
            if (null != fclcontainerForm.getTrailerNo()) {
                FclBlContainer fclBlContainer = new FclBlContainer();
                fclBlContainer.setTrailerNo(fclcontainerForm.getTrailerNo());
                fclBlContainer.setTrailerNoOld(fclcontainerForm.getTrailerNoOld());
                fclBlContainer.setSealNo(fclcontainerForm.getSealNo());
                fclBlContainer.setMarks(fclcontainerForm.getMarksNo());
                fclBlContainer.setLastUpdate(new Date());
                if (fclcontainerForm.getSizeLegend() != null && !fclcontainerForm.getSizeLegend().equals("0")) {
                    GenericCode gen = (GenericCode) genericCodeDAO.findById(Integer.parseInt(fclcontainerForm.getSizeLegend()));
                    fclBlContainer.setSizeLegend(gen);
                }
                fclContainerSet.add(fclBlContainer);
            }
        }
        return fclContainerSet;
    }

    public Set setContainerValuesToUpdate(FclBillLaddingForm fclcontainerForm, List containerList, HttpServletRequest request, String userName) throws Exception {
        Set fclContainerSet = new LinkedHashSet<FclBlContainer>();
        String index = null;
        if (null != request.getParameter("index")) {
            index = request.getParameter("index");
        }
        int j = Integer.parseInt(index);

        if (containerList.size() > 0) {
            for (Iterator iterator = containerList.iterator(); iterator.hasNext();) {
                FclBlContainer fclBlContainer = (FclBlContainer) iterator.next();
                if (fclBlContainer.getTrailerNoId().toString().equals(request.getParameter("id" + j))) {
                    if (null != fclcontainerForm) {
                        if (null != fclcontainerForm.getTrailerNo()) {
                            if (null != fclBlContainer.getTrailerNo() && !(fclBlContainer.getTrailerNo()).equalsIgnoreCase(fclcontainerForm.getTrailerNo())) {
                                fclBlContainer.setTrailerNoOld(fclBlContainer.getTrailerNo());//--0(zero) bcz length of properties in popup is always 1
                            }
                            fclBlContainer.setTrailerNo(fclcontainerForm.getTrailerNo());//--0(zero) bcz length of properties in popup is always 1
                        }
                        fclBlContainer.setSealNo(fclcontainerForm.getSealNo());
                        fclBlContainer.setMarks(fclcontainerForm.getMarksNo());
                        fclBlContainer.setUserName(userName);
                        fclBlContainer.setLastUpdate(new Date());
                        if (fclcontainerForm.getSizeLegend() != null && !fclcontainerForm.getSizeLegend().equals("0")) {
                            GenericCode gen = (GenericCode) genericCodeDAO.findById(Integer.parseInt(fclcontainerForm.getSizeLegend()));
                            fclBlContainer.setSizeLegend(gen);
                        }
                    }
                }
                fclContainerSet.add(fclBlContainer);
            }
        }
        return fclContainerSet;
    }

    public List addFclBlCharges(List addchargeslist, FclBl tempFclBl) {

        List newaddList = new ArrayList();
        Map getUniqueCostCode = new HashMap();
        for (Iterator iterator = addchargeslist.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlChargesAdd = (FclBlCharges) iterator.next();
            if (getUniqueCostCode.containsKey(fclBlChargesAdd.getChargeCode())) {
                FclBlCharges fclBlChargesFrmMap = (FclBlCharges) getUniqueCostCode.get(fclBlChargesAdd.getChargeCode());
                Double addedamt = fclBlChargesFrmMap.getAmount() + fclBlChargesAdd.getAmount();
                fclBlChargesFrmMap.setAmount(addedamt);
                getUniqueCostCode.put(fclBlChargesAdd.getChargeCode(), fclBlChargesFrmMap);
            } else {
                getUniqueCostCode.put(fclBlChargesAdd.getChargeCode(), fclBlChargesAdd);
            }
        }
        Set set = getUniqueCostCode.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String currentKey = (String) iter.next();
            newaddList.add(getUniqueCostCode.get(currentKey));
        }
        newaddList.add(new FclBlCharges());
        return newaddList;
    }

    public List addFclCostCodes(List addcostcodeslist, FclBl tempFclBl) {
        List newaddList = new ArrayList();

        Map getUniqueCostCode = new HashMap();
        for (Iterator iterator = addcostcodeslist.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (getUniqueCostCode.containsKey(fclBlCostCodes.getCostCode() + fclBlCostCodes.getAccName())) {
                FclBlCostCodes fclBlCostCodesFrmMap = (FclBlCostCodes) getUniqueCostCode.get(fclBlCostCodes.getCostCode() + fclBlCostCodes.getAccName());
                Double addedamt = fclBlCostCodesFrmMap.getAmount() + fclBlCostCodes.getAmount();
                fclBlCostCodesFrmMap.setAmount(addedamt);
                getUniqueCostCode.put(fclBlCostCodes.getCostCode() + fclBlCostCodes.getAccName(), fclBlCostCodesFrmMap);
            } else {
                getUniqueCostCode.put(fclBlCostCodes.getCostCode() + fclBlCostCodes.getAccName(), fclBlCostCodes);
            }
        }
        Set set = getUniqueCostCode.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String currentKey = (String) iter.next();
            newaddList.add(getUniqueCostCode.get(currentKey));
        }
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        fclBlCostCodes.setCostCode("");
        fclBlCostCodes.setCostCodeDesc("");
        fclBlCostCodes.setAccNo("");
        fclBlCostCodes.setAccName("");
        fclBlCostCodes.setAmount(0.00);
        fclBlCostCodes.setDatePaid(null);
        fclBlCostCodes.setComments("");
        newaddList.add(fclBlCostCodes);

        return newaddList;
    }

    public void saveFclBlInFclBlCharges(FclBl fclBl, Double total, Double totalCostCode, String billTrePty, String thirdPartyName, String billThirdParty) throws Exception {
        String thirdParty = "";
        if (total != null) {
            fclBl.setTotal(total);
        }
        if (totalCostCode != null) {
            fclBl.setTotalCostCode(totalCostCode);
        }
        fclBl.setBillTrdPrty(billTrePty);
        if (null != billTrePty && !billTrePty.equals("") && !billTrePty.equals("null")) {
            List houseConsigneeList = custAddressDAO.findForAgenttNo(billTrePty, null);
            if (houseConsigneeList != null && houseConsigneeList.size() > 0) {
                CustomerTemp cust = (CustomerTemp) houseConsigneeList.get(0);
                fclBl.setBillThirdPartyAddress(cust.getAddress1());
                fclBl.setThirdPartyName(cust.getAccountName());
            }
        } else {
            thirdParty = null != billTrePty && !billTrePty.equals("null") ? billTrePty : "";
            fclBl.setThirdPartyName(thirdParty);
        }
        fclBlDAO.update(fclBl);
    }

    public FclBl getFclBLObject(Integer bol) throws Exception {
        FclBlDAO fclbldao = new FclBlDAO();
        return fclbldao.findById(bol);
    }

    public FclBl getFclBLObjectByBolid(String bol) throws Exception {
        FclBlDAO fclbldao = new FclBlDAO();
        return fclbldao.findById(bol);
    }

    public String getFileNoObjectForMultipleBl(String fileno) throws Exception {
        return fclBlDAO.getBOLId(fileno);
    }

    public BookingFcl getBookingByFileNo(String fileno) throws Exception {
        return bookingFclDAO.getFileNoObject(fileno);
    }

    public Quotation getQuoteByFileNo(String fileno) throws Exception {
        return quotationDAO.getFileNoObject(fileno);
    }

    public List getHazmatByBolId(int bolId) throws Exception {
        List hazmatMaterial = hazmatMaterialDAO.getHazmatByBolId(bolId);
        return hazmatMaterial;
    }

    public List getHazmatForBlPrint(int bolId, String screenName) throws Exception {
        List hazmatMaterial = hazmatMaterialDAO.getHazmatForBlPrint(bolId, screenName);
        return hazmatMaterial;
    }

    public String finLatestBl(String bolId) throws Exception {
        List list = fclBlDAO.findLatestBolId(bolId);
        String bolid = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            FclBl fclBl = (FclBl) iter.next();
            if (fclBl.getBolId().indexOf(bolId + FclBlConstants.DELIMITER) > -1) {
                bolid = String.valueOf(fclBl.getBol());
                break;
            }
        }
        return bolid;
    }

    public List getCorrectionList(String bolNo) throws Exception {
        List correctionList = null != bolNo ? fclBlDAO.getListOfApprovedFclBlCorrections(bolNo) : null;
        return correctionList;

    }

    public List getCorrectionListForApprovedBl(String bolNo) throws Exception {
        List correctionList = fclBlDAO.getAllBlNumbers(bolNo);
        return correctionList;

    }

    public FclBl updateFclBl(FclBl fclBl, String user, FclBillLaddingForm fclBillLaddingForm) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String newDate = sdf.format(date);
        String action = fclBillLaddingForm.getAction();
        if (action != null && action.equalsIgnoreCase("blClosed")) {
            fclBl.setBlClosed("Y");
            user = user.toUpperCase();
            fclBl.setClosedBy(user);
            fclBl.setClosedDate(date);
        } else if (action != null && action.equalsIgnoreCase("blAudit")) {
            fclBl.setBlAudit("Y");
            user = user.toUpperCase();
            fclBl.setAuditedBy(user);
            fclBl.setAuditedDate(date);
        } else if (action != null && action.equalsIgnoreCase("blAuditCancel")) {
            fclBl.setBlAudit(null);
            fclBl.setAuditedBy(null);
            fclBl.setAuditedDate(null);
        } else if (action != null && action.equalsIgnoreCase("blOpned")) {
            fclBl.setBlClosed(null);
            fclBl.setClosedBy(null);
            fclBl.setClosedDate(null);
        } else if (action != null && action.equalsIgnoreCase("voidBl")) {
            fclBl.setBlVoid("Y");
            user = user.toUpperCase();
            fclBl.setVoidBy(user);
            fclBl.setVoidDate(date);
        } else if (action != null && action.equalsIgnoreCase("UnVoidBl")) {
            fclBl.setBlVoid(null);
            fclBl.setVoidBy(null);
            fclBl.setVoidDate(null);
        }
        //---setting date to temporary String property for display purpose-----
        fclBl.setTempDisplayDate(newDate);
//        fclBl.setUpdateBy(user);
        fclBlDAO.save(fclBl);
        return fclBl;
    }

    public String updateFclBlForConfirmOnBoard(String bolNo, String verifyETA, String confirmOnBoard,
            String user, String comments, String fromImportRelease, HttpServletRequest request) throws Exception {
        FclBl fclBl = new FclBl();
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        if (bolNo != null) {
            fclBl = getFclBLObject(new Integer(bolNo));
            comments = (CommonFunctions.isNotNull(comments)) ? comments.toUpperCase() : comments;
            //----THIS IS FOR IMPORT RELEASE----
            if (!fromImportRelease.equals("") && fromImportRelease.equals("fromImportRelease")) {
                fclBl.setImportReleaseComments(comments);
                if (verifyETA != null && !verifyETA.equals("")) {
                    fclBl.setImportVerifiedEta(DateUtils.parseDate(verifyETA, "MM/dd/yyyy"));
                } else {
                    fclBl.setImportVerifiedEta(null);
                }

                fclBl.setImportRelease(confirmOnBoard);
                //----THIS IS FOR CONFIRM ON BOARD----
            } else {
                if (confirmOnBoard.equalsIgnoreCase("Y") && null != fclBl.getConfirmBy() && !fclBl.getConfirmBy().equalsIgnoreCase(user)) {
                    fclBl.setConfirmBy(user);
                } else {
                    fclBl.setConfirmBy(null);
                    fclBl.setConfirmOn(null);
                    fclBl.setTempConfirmOnToDisplay(null);
                }
                fclBl.setConfOnBoardComments(comments);
                if (verifyETA != null && !verifyETA.equals("")) {
                    fclBl.setVerifyETA(DateUtils.parseDate(verifyETA, "MM/dd/yyyy"));
                } else {
                    fclBl.setVerifyETA(null);
                }
                fclBl.setConfirmOnBorad(confirmOnBoard);
            }
            fclBl.setUpdateBy(loginUser.getLoginName());
            fclBl.setUpdatedDate(new Date());
//            fclBlDAO.update(fclBl);
            List correctedfclBlList = new FclBlDAO().findLatestBolId(fclBl.getBolId() + FclBlConstants.DELIMITER);
            if (CommonFunctions.isNotNullOrNotEmpty(correctedfclBlList)) {
                for (Iterator it = correctedfclBlList.iterator(); it.hasNext();) {
                    FclBl fclCorrectionBl = (FclBl) it.next();
                    if (!fromImportRelease.equals("") && fromImportRelease.equals("fromImportRelease")) {
                        fclCorrectionBl.setImportReleaseComments(comments);
                        if (verifyETA != null && !verifyETA.equals("")) {
                            fclCorrectionBl.setImportVerifiedEta(DateUtils.parseDate(verifyETA, "MM/dd/yyyy"));
                        } else {
                            fclCorrectionBl.setImportVerifiedEta(null);
                        }

                        fclCorrectionBl.setImportRelease(confirmOnBoard);
                        //----THIS IS FOR CONFIRM ON BOARD----
                    } else {
                        fclCorrectionBl.setConfirmBy(user);
                        fclCorrectionBl.setConfOnBoardComments(comments);
                        if (verifyETA != null && !verifyETA.equals("")) {
                            fclCorrectionBl.setVerifyETA(DateUtils.parseDate(verifyETA, "MM/dd/yyyy"));
                        } else {
                            fclCorrectionBl.setVerifyETA(null);
                        }
                        fclCorrectionBl.setConfirmOnBorad(confirmOnBoard);
                    }
                    fclBlDAO.update(fclCorrectionBl);
                }
            }

        }
        return DateUtils.formatDate(new Date(), "MM/dd/yyyy HH:ss");
    }

    public FclBl displayManifestedClosedAuditedDetails(String bolNo) throws Exception {
        FclBl fclBl = new FclBl();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        if (bolNo != null) {
            fclBl = getFclBLObject(new Integer(bolNo));
            //---setting date to temporary String property for display purpose-----
            if (null != fclBl.getClosedDate()) {
                fclBl.setTempDisplayDate(sdf.format(fclBl.getClosedDate()));
            }
            if (null != fclBl.getAuditedDate()) {
                fclBl.setTempDisplayDate(sdf.format(fclBl.getAuditedDate()));
            }
        }
        return fclBl;
    }

    public List<HazmatMaterial> listOfHasMateridalFromBooking(String bol, List list) throws Exception {
        List<HazmatMaterial> listOfmaterial = null;
        HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
        if (CommonFunctions.isNotNull(bol)) {
            FclBl fclBl = fclBlDAO.findById(new Integer(bol));
            String filenumber = fclBl.getFileNo();
            String fileno = null;
            if (filenumber != null) {
                fileno = getFileNumber(filenumber);
            }
            BookingFcl bookingFcl = bookingFclDAO.getFileNoObject("" + fileno);
            if (bookingFcl != null) {
                listOfmaterial = hazmatMaterialDAO.findbydoctypeid1(FclBlConstants.HAZMATQUOTE, bookingFcl.getBookingId());
            }

        }

        List<HazmatMaterial> newList = new ArrayList();
        if (listOfmaterial != null) {
            newList.addAll(listOfmaterial);
        }
        if (CommonFunctions.isNotNullOrNotEmpty(list)) {
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                HazmatMaterial hazmatMaterial2 = (HazmatMaterial) iter.next();
                for (Iterator iter2 = newList.iterator(); iter2.hasNext();) {
                    HazmatMaterial hazmatMaterial = (HazmatMaterial) iter2.next();
                    if ((hazmatMaterial2.getFlag() != null && hazmatMaterial.getId() != null
                            && hazmatMaterial.getId().equals(new Integer(hazmatMaterial2.getFlag())))
                            || (hazmatMaterial.getDeletedFlag() != null && hazmatMaterial.getDeletedFlag().equalsIgnoreCase("Yes"))) {
                        listOfmaterial.remove(hazmatMaterial);
                    }
                }
            }
        } else {
            if (CommonFunctions.isNotNullOrNotEmpty(newList)) {
                for (HazmatMaterial newHazmatMaterial : newList) {
                    if (null != newHazmatMaterial.getDeletedFlag()) {
                        listOfmaterial.remove(newHazmatMaterial);
                    }
                }
            }
        }
        return listOfmaterial;
    }

    public void convertHazMatTOBl(FCLHazMatForm fCLHazMatForm, HttpServletRequest request) throws Exception {
        //String arg[] = fCLHazMatForm.getCheckBox();
        String hazmatId = fCLHazMatForm.getUnAssignedHazMatId();
        String booking = fCLHazMatForm.getBooking();
        String containerId = fCLHazMatForm.getNumber();
        String bolid = fCLHazMatForm.getBolId();
        List listOfmaterial = null;
        if (booking != null) {
            listOfmaterial = getHazmatByBolId(new Integer(booking));
        }
        HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
        List<HazmatMaterial> hazmatList = new ArrayList<HazmatMaterial>();
        List<Integer> temphazmatList = new ArrayList<Integer>();
        List<HazmatMaterial> hazmatListFromBooking = new ArrayList<HazmatMaterial>();
        List<HazmatMaterial> bookingHazmat = new ArrayList<HazmatMaterial>();
        if (CommonFunctions.isNotNull(hazmatId)) {
            if (listOfmaterial != null) {
                for (Iterator iter = listOfmaterial.iterator(); iter.hasNext();) {
                    HazmatMaterial hazmatMaterial = (HazmatMaterial) iter.next();
                    if (hazmatMaterial.getId().equals(new Integer(hazmatId))) {
                        HazmatMaterial hazmatMaterial2 = new HazmatMaterial();
                        PropertyUtils.copyProperties(hazmatMaterial2, hazmatMaterial);
                        if (containerId != null && !containerId.equals("")) {
                            hazmatMaterial2.setId(null);
                            hazmatMaterial2.setDocTypeCode(FclBlConstants.HAZMATQUOTEFORBL);
                            hazmatMaterial2.setDocTypeId(containerId);
                            hazmatMaterial2.setBolId(new Integer(containerId));
                            hazmatMaterial2.setFlag(hazmatMaterial.getId().toString());
                            hazmatMaterialDAO.save(hazmatMaterial2);
                        }
                        temphazmatList.add(hazmatMaterial.getId());
                    }
                }
            }
        }
        if (containerId != null) {
            listOfmaterial = getHazmatByBolId(new Integer(containerId));
        }
        if (listOfmaterial != null) {
            for (Iterator iter = listOfmaterial.iterator(); iter.hasNext();) {
                HazmatMaterial hazmatMaterial = (HazmatMaterial) iter.next();
                hazmatList.add(hazmatMaterial);
            }
        }
        List checkingBookingList = quotationBC.getHazmatList(request.getParameter("name"), null);
        hazmatListFromBooking = listOfHasMateridalFromBooking(fCLHazMatForm.getBolId(), checkingBookingList);
        request.setAttribute(QuotationConstants.HAZMAT, hazmatList);
        for (Object object : hazmatListFromBooking) {
            HazmatMaterial hazmatMaterial = (HazmatMaterial) object;
            if (!"Y".equalsIgnoreCase(hazmatMaterial.getFreeFormat())) {
                String mandatory = getMandatoryFields(hazmatMaterial);
                if (CommonUtils.isNotEmpty(mandatory)) {
                    mandatory = "Mandatory Fields Needed<br>" + mandatory;
                    hazmatMaterial.setMandatory(mandatory);
                }
            }
            bookingHazmat.add(hazmatMaterial);
        }
        request.setAttribute("bookingHazmatList", bookingHazmat);
        request.setAttribute(QuotationConstants.FCLBLNO, bolid);
    }

    public void deleteUnassignedHazmat(FCLHazMatForm fCLHazMatForm, HttpServletRequest request) throws Exception {
        String hazmatId = fCLHazMatForm.getUnAssignedHazMatId();
        HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();

        //---To INDICATE THE DELETION, SET THE DELETEFLAG TO 'YES'---
        HazmatMaterial hazmatMaterial = hazmatMaterialDAO.findById(Integer.parseInt(hazmatId));
        hazmatMaterial.setDeletedFlag("YES");
        hazmatMaterialDAO.update(hazmatMaterial);

        //--FETCH THE RESPECTIVE QUOTE AND BOOKING HAZMATS i.e UNASSIGNED HAZMAT LIST----
        List unassignedHazmatList = new ArrayList();
        if (fCLHazMatForm.getName() != null && fCLHazMatForm.getName().equalsIgnoreCase(FclBlConstants.HAZMATQUOTEFORBL)) {
            List quoteHazmatList = quotationBC.getHazmatList(request.getParameter("name"), null);
            List hazmatListForBl = listOfHasMateridalFromBooking(fCLHazMatForm.getBolId(), quoteHazmatList);
            //----CHECKING FOR DELETED HAZMAT------
            for (Iterator iter = hazmatListForBl.iterator(); iter.hasNext();) {
                HazmatMaterial newHazmatMaterial = (HazmatMaterial) iter.next();
                if (null != newHazmatMaterial.getDeletedFlag() && newHazmatMaterial.getDeletedFlag().equals("YES")) {
                    //do nothing---:)
                } else {
                    unassignedHazmatList.add(newHazmatMaterial);
                }
            }
            request.setAttribute("bookingHazmatList", unassignedHazmatList);
            request.setAttribute(QuotationConstants.FCLBLNO, fCLHazMatForm.getBolId());
        }

        //-----RETURN THE LIST OF HAZMAT ADDED IN BL-------
        List hazmatListOfBl = null;
        if (fCLHazMatForm.getNumber() != null) {
            hazmatListOfBl = getHazmatByBolId(new Integer(fCLHazMatForm.getNumber()));
        }
        request.setAttribute(QuotationConstants.HAZMAT, hazmatListOfBl);
    }

    public void deleteHazMat(FCLHazMatForm fCLHazMatForm, HttpServletRequest request) throws Exception {
        String hazMatId = fCLHazMatForm.getUnAssignedHazMatId();
        if (CommonFunctions.isNotNull(hazMatId)) {
            deleted(hazMatId);
        }
        List listOfmaterialForBooking = null;
        List listOfmaterialForBl = null;
        List<HazmatMaterial> bookingHazmat = new ArrayList<HazmatMaterial>();
        if (fCLHazMatForm.getNumber() != null) {
            listOfmaterialForBl = getHazmatByBolId(new Integer(fCLHazMatForm.getNumber()));
        }
        if (fCLHazMatForm.getName() != null && fCLHazMatForm.getName().equalsIgnoreCase(FclBlConstants.HAZMATQUOTEFORBL)) {
            List checkingBookingList = quotationBC.getHazmatList(request.getParameter("name"), null);
            listOfmaterialForBooking = listOfHasMateridalFromBooking(fCLHazMatForm.getBolId(), checkingBookingList);
            for (Object object : listOfmaterialForBooking) {
                HazmatMaterial hazmatMaterial = (HazmatMaterial) object;
                if (!"Y".equalsIgnoreCase(hazmatMaterial.getFreeFormat())) {
                    String mandatory = getMandatoryFields(hazmatMaterial);
                    if (CommonUtils.isNotEmpty(mandatory)) {
                        mandatory = "Mandatory Fields Needed<br>" + mandatory;
                        hazmatMaterial.setMandatory(mandatory);
                    }
                }
                bookingHazmat.add(hazmatMaterial);
            }
            request.setAttribute("bookingHazmatList", bookingHazmat);
            request.setAttribute(QuotationConstants.FCLBLNO, fCLHazMatForm.getBolId());
        }
        request.setAttribute(QuotationConstants.HAZMAT, listOfmaterialForBl);
    }

    public List getPrimaryClassForHazmatList() throws Exception {
        com.gp.cvst.logisoft.util.DBUtil dUtil = new com.gp.cvst.logisoft.util.DBUtil();
        return dUtil.scanScreenName(59);
    }

    public void deleteHazMatFroQuoteAndBooking(FCLHazMatForm fCLHazMatForm, HttpServletRequest request) throws Exception {
        HazmatMaterial hazmatMaterial = hazmatMaterialDAO.findById(new Integer(fCLHazMatForm.getIndex()));
        if (hazmatMaterial != null) {
            hazmatMaterialDAO.delete(hazmatMaterial);
        }
        List hazmatList = null;
        if (fCLHazMatForm.getNumber() != null) {
            hazmatList = getHazmatByBolId(new Integer(fCLHazMatForm.getNumber()));
        }
        request.setAttribute(QuotationConstants.HAZMAT, hazmatList);

        //--FETCH THE RESPECTIVE QUOTE AND BOOKING HAZMATS i.e UNASSIGNED HAZMAT LIST----
        List unassignedHazmatList = new ArrayList();
        if (fCLHazMatForm.getName() != null && fCLHazMatForm.getName().equalsIgnoreCase(FclBlConstants.HAZMATQUOTEFORBL)) {
            List quoteHazmatList = quotationBC.getHazmatList(request.getParameter("name"), null);
            List hazmatListForBl = listOfHasMateridalFromBooking(fCLHazMatForm.getBolId(), quoteHazmatList);
            //----CHECKING FOR DELETED HAZMAT------
            for (Iterator iter = hazmatListForBl.iterator(); iter.hasNext();) {
                HazmatMaterial newHazmatMaterial = (HazmatMaterial) iter.next();
                if (null != newHazmatMaterial.getDeletedFlag() && newHazmatMaterial.getDeletedFlag().equals("YES")) {
                    //do nothing---:)
                } else {
                    unassignedHazmatList.add(newHazmatMaterial);
                }
            }
            request.setAttribute("bookingHazmatList", unassignedHazmatList);
        }
    }

    public void deleted(String hazMatId) throws Exception {
        HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
        if (CommonFunctions.isNotNull(hazMatId)) {
            HazmatMaterial hazmatMaterial = hazmatMaterialDAO.findById(new Integer(hazMatId));
            if (hazmatMaterial != null) {
                hazmatMaterialDAO.delete(hazmatMaterial);
            }
        }
    }

    public String getFileNumber(String fileNo) {
        String fileNumber = "";
        if (fileNo != null) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(fileNo);
            if (matcher != null && matcher.find()) {
                fileNumber += matcher.group();
            }
        }
        return fileNumber;
    }

    public Set<HazmatMaterial> saveHazmatMaterial(Integer trailerNoId) throws Exception {
        Set<HazmatMaterial> hazSet = new HashSet<HazmatMaterial>();
        QuotationBC quotationBC = new QuotationBC();
        List hazmatList = quotationBC.getHazmatList("FclBl", String.valueOf(trailerNoId));
        Set<HazmatMaterial> hazmatMaterialList = new HashSet<HazmatMaterial>();
        for (Iterator iterator = hazmatList.iterator(); iterator.hasNext();) {
            HazmatMaterial hazmatMaterialObject = (HazmatMaterial) iterator.next();
            HazmatMaterial hazmatMaterial = new HazmatMaterial();
            PropertyUtils.copyProperties(hazmatMaterial, hazmatMaterialObject);
            hazmatMaterial.setId(null);
            hazmatMaterialList.add(hazmatMaterial);
        }
        // adding HazmatMaterail object to fclBlContainer set(this is temprory set....)
        hazSet.addAll(hazmatMaterialList);
        return hazSet;
    }

    /**
     * @param fclBillLaddingForm
     * @param buttonValue
     * @param request
     * @param session this method copy from fclblContainerAction............
     */
    public void fclBlContainerProcess(FclBillLaddingForm fclBillLaddingForm, String buttonValue, HttpServletRequest request, HttpSession session, String userName, MessageResources messageResources) throws Exception {
        FclBl fclBl = fclBlDAO.findById(Integer.parseInt(fclBillLaddingForm.getBol()));
        Set fclContainerSet = new HashSet();
        List containerList = getFclBlContrainerList(fclBl);
        if (!buttonValue.equals("getUpdatedContainerDetails")) {
            //-----UPDATING EACH CONTAINER FROM POPUP--------
            if (buttonValue.equals("updateContainer") || buttonValue.equals("updateContainerWithCharges")) {
                fclContainerSet = setContainerValuesToUpdate(fclBillLaddingForm, containerList, request, userName);
            } else {
                fclContainerSet = setFclContainerValues(fclBillLaddingForm, containerList, request);
            }
            fclBl.setFclcontainer(fclContainerSet);
            fclBlDAO.update(fclBl);
        }
        if (buttonValue.equals("add")) {
            fclContainerSet.add(new FclBlContainer());
        }
        //---TO SET QUOTEBY AND BOOKINGBY AND THEIR CREATION DATE FOR DISPLAY----
        quotation = getQuoteByFileNo(getFileNumber(fclBl.getFileNo()));
        if (quotation != null) {
            fclBl.setQuoteBy(quotation.getQuoteBy());
            fclBl.setQuoteDate(quotation.getQuoteDate());
            if (quotation.getHazmat() != null && quotation.getHazmat().equalsIgnoreCase("Y")) {
                fclBl.setHazmat("Y");
            }
        }
        bookingFcl = getBookingByFileNo(getFileNumber(fclBl.getFileNo()));
        if (bookingFcl != null) {
            fclBl.setBookingBy(bookingFcl.getBookedBy());
            fclBl.setBookingDate(bookingFcl.getBookingDate());
        }
        //---ends---
        request.setAttribute(FclBlConstants.FCLBL, fclBl);
        SetRequestForFclChargesandCostCode(request, fclBl, buttonValue, messageResources, false);
        request.setAttribute("selectedTab", "fclBlContainer");
        if (session.getAttribute("bookinghazmat") != null) {
            session.removeAttribute("bookinghazmat");
        }
        if (fclBl.getFclcontainer() != null) {
            Iterator iter = fclBl.getFclcontainer().iterator();
            while (iter.hasNext()) {
                FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                List hazmatMaterialList = hazmatMaterialDAO.findbydoctypeid1("FclBl", fclBlContainer.getTrailerNoId());
                if (hazmatMaterialList.size() > 0) {
                    request.setAttribute("message", "HAZARDOUS CARGO");
                    break;
                }
            }
        }
    }

    public void editContainerDetails(FclBillLaddingForm fclBillLaddingForm, HttpServletRequest request) throws Exception {
        FclBl fclBl = fclBlDAO.findById(Integer.parseInt(fclBillLaddingForm.getBol()));
        String sizeLegend = "";
        if (fclBl.getFclcontainer() != null) {
            Iterator iter = fclBl.getFclcontainer().iterator();
            while (iter.hasNext()) {
                fclBlContainer = (FclBlContainer) iter.next();
                if (fclBlContainer.getTrailerNoId().toString().equalsIgnoreCase(request.getParameter("id"))) {
                    if (null != fclBlContainer.getSizeLegend()) {
                        sizeLegend = fclBlContainer.getSizeLegend().getId().toString();
                    }
                    request.setAttribute("sizeLegend", sizeLegend);
                    request.setAttribute(FclBlConstants.FCL_BL_CONTAINER, fclBlContainer);
                }
            }
        }
        //---Request set to disable the UnitNo while editing container details for manifested Bl---
        if (null != fclBl.getReadyToPost() && fclBl.getReadyToPost().equalsIgnoreCase("M")) {
            request.setAttribute("ManifestedBl", "ManifestedBl");
        }
    }

    public List getUpdatedContainerList(String bol) throws Exception {
        return fclBlContainerDAO.getAllContainers(bol);
    }

    /**
     * @param fclBl
     * @return
     */
    public List getFclBlContrainerList(FclBl fclBl) throws Exception {
        List fclBlContainerList = new ArrayList();
        if (fclBl.getFclcontainer() != null) {
            Iterator iter = fclBl.getFclcontainer().iterator();
            while (iter.hasNext()) {
                FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                fclBlContainerList.add(fclBlContainer);
            }
        }
        Collections.sort(fclBlContainerList, new Comparator());
        return fclBlContainerList;
    }

    /**
     * @param fclBl
     * @return
     */
    public List getFclBlChargesList(FclBl tempFclBl) throws Exception {

        List addchargeslist = new ArrayList();
        HashMap hashMap = new HashMap();
        if (tempFclBl.getFclcharge() != null) {
            Iterator iter = tempFclBl.getFclcharge().iterator();
            while (iter.hasNext()) {
                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
                hashMap.put(fclBlCharges.getChargesId(), fclBlCharges);
                addchargeslist.add(fclBlCharges.getChargesId());
            }
        }
        return addchargeslist;
    }

    /**
     * @param request
     * @param fclBl this method wil set request for fclBlCharges and
     * FclBlCostCode........
     */
    public void SetRequestForFclChargesandCostCode(HttpServletRequest request, FclBl fclBl, String buttonValue, MessageResources messageResources, boolean hasMasterBlChanged) throws Exception {
        List fclCostCodeList = new ArrayList();
        List fclChargesList = new ArrayList();
        List containerList = new ArrayList();

        if (fclBl.getFclcharge() != null) {
            fclChargesList = fclBlChargesDAO.findByProperty("bolId", fclBl.getBol());
            Collections.sort(fclChargesList, new Comparator());
        }
        if (buttonValue != null && buttonValue.equals("addCharges")) {
            fclChargesList.add(new FclBlCharges());
        }
        if (fclBl.getFclblcostcodes() != null) {
            fclCostCodeList.addAll(fclBl.getFclblcostcodes());
        }
        Collections.sort(fclCostCodeList, new Comparator());
        if (buttonValue != null && buttonValue.equals("costCodeAdd")) {
            fclCostCodeList.add(new FclBlCostCodes());
        }
        if (null != buttonValue && !buttonValue.equals("getUpdatedContainerDetails")) {
            if (fclBl.getFclcontainer() != null) {
                containerList.addAll(fclBl.getFclcontainer());
                Collections.sort(containerList, new Comparator());
                request.setAttribute("fclBlContainerList", containerList);
            }
        }
        int j = 0;
        int k = 0;
        boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        for (Iterator iterator = fclChargesList.iterator(); iterator.hasNext();) {
            FclBlCharges charges = (FclBlCharges) iterator.next();
            if (charges.getChargeCode() != null && (charges.getChargeCode().equals("OCNFRT") || charges.getChargeCode().equals("OFIMP"))) {
                linkedList.add(k, charges);
            } else {
                linkedList.add(charges);
            }
            j++;
        }
        newList.addAll(linkedList);
        request.setAttribute("addchargeslist", newList);
        //-------COLLAPSE CHARGES LIST....
        List consolidatorList = consolidateRates(newList, messageResources, importFlag);
        request.setAttribute("consolidatorList", consolidatorList);
        //-------EXPAND OR DETAILED COST LIST..
        //newList = shortCostCodeList(fclCostCodeList);
        List costList = fclBlCostCodesDAO.findByParenId(fclBl.getBol());
        j = 0;
        k = 0;
        linkedList = new LinkedList();
        newList = new ArrayList();
        for (Iterator iterator = costList.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (fclBlCostCodes.getCostCode() != null && (fclBlCostCodes.getCostCode().equals("OCNFRT") || fclBlCostCodes.getCostCode().equals("OFIMP"))) {
                linkedList.add(k, fclBlCostCodes);
            } else {
                linkedList.add(fclBlCostCodes);
            }
            j++;
        }
        newList.addAll(linkedList);
        request.setAttribute(FclBlConstants.FCLBL_COSTS_LIST, newList);
        //------COLLAPSE OR SUMMARY COST LIST...
        List consolidatorCostList = consolidateRatesForCosts(newList, messageResources, fclBl, hasMasterBlChanged, importFlag);

        request.setAttribute("consolidatorCostList", consolidatorCostList);
        fclBlUtil.calculateChargesAndProfit(fclBl, request, messageResources);
        //-----MANIFESTED COST LIST......
        if (null != fclBl.getBol() && CommonFunctions.isNotNull(fclBl.getFclblcostcodes())) {
            List manifestedCostList = fclBlCostCodesDAO.getAllFclCosts(fclBl.getBol().toString());
            List newManifestList = new ArrayList();
            //---getting summary or collapsed manifested list---
            manifestedCostList = shortManifestedCostCodeList(manifestedCostList);
            newManifestList = consolidateRatesForCosts(manifestedCostList, messageResources, fclBl, hasMasterBlChanged, importFlag);
            request.setAttribute("ManifestedCostList", newManifestList);
        }
        //----GET LIST OF CONTACTCONFIG DETAILS WHERE CODE C IS E1 OR E2------
        // if (!"I".equalsIgnoreCase(fclBl.getImportFlag())) {
        getContactConfigDetailsForCodeC(fclBl, request);
        //  }
    }

    public void getContactConfigDetailsForCodeC(FclBl fclBl, HttpServletRequest request) throws Exception {
        // seeting message if there is no changes in charges and bill to partygetCustomerAccountNo
        Set chargesSet = fclBl.getFclcharge();
        if (CommonUtils.isNotEmpty(chargesSet)) {
            List li = new FclBlChargesDAO().getDistinctBillToParty(fclBl.getBol().toString());
            if (fclBl.getManifestRev() != null && !fclBl.getManifestRev().equals("0") && !CommonFunctions.isNotNullOrNotEmpty(li)) {
                request.setAttribute("invoiceMessage", "Freight Invoice will NOT be sent because of no changes to charges");
            } else {
                List billToList = new FclBlChargesDAO().getDistinctBillTo(fclBl.getBol().toString());
                List<CustomerContact> returnList = new ArrayList();
                if (billToList.size() > 0) {
                    for (Iterator iter = billToList.iterator(); iter.hasNext();) {
                        String Object = (String) iter.next();
                        if (null != Object) {
                            if (Object.equalsIgnoreCase(CommonConstants.FORWARDER)) {
                                returnList.addAll(checkForE1andE2OfCodek(fclBl.getForwardAgentNo(), fclBl.getImportFlag()));
                            } else if (Object.equalsIgnoreCase(CommonConstants.SHIPPER)) {
                                String customerNumber = "I".equalsIgnoreCase(fclBl.getImportFlag()) ? fclBl.getHouseShipper() : fclBl.getShipperNo();
                                returnList.addAll(checkForE1andE2OfCodek(customerNumber, fclBl.getImportFlag()));
                            } else if (Object.equalsIgnoreCase(CommonConstants.THIRDPARTY)) {
                                returnList.addAll(checkForE1andE2OfCodek(fclBl.getBillTrdPrty(), fclBl.getImportFlag()));
                            } else if (Object.equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
                                String customerNumber = fclBl.getConsigneeNo();
                                returnList.addAll(checkForE1andE2OfCodek(customerNumber, fclBl.getImportFlag()));
                            } else if (Object.equalsIgnoreCase(CommonConstants.AGENT)) {
                                String customerNumber = fclBl.getAgentNo();
                                returnList.addAll(checkForE1andE2OfCodek(customerNumber, fclBl.getImportFlag()));
                            }
                        }
                    }
                }
                // getting email from booking and displaying
                if (CommonFunctions.isNotNull(fclBl.getSendCopyTo())) {
                    CustomerContact customerContact = new CustomerContact();
                    customerContact.setAccountName(fclBl.getSendCopyTo());
                    returnList.add(customerContact);
                }
                if (CommonUtils.isNotEmpty(returnList)) {
                    request.setAttribute("ContactConfigE1andE2", returnList);
                }
            }

        } else {
            request.setAttribute("manifestWithoutCharges", "yes");
        }
    }

    public List<CustomerContact> checkForE1andE2OfCodeC(String accountNo) throws Exception {
        List contactConfigList = new ArrayList();
        List resultList = new ArrayList();
        TradingPartner tradingPartner = new TradingPartner();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        if (null != accountNo) {
            tradingPartner = tradingPartnerDAO.findById(accountNo);
            String accountingEmail = tradingPartnerDAO.getAccountingEmail(accountNo);
            String accountingFax = tradingPartnerDAO.getAccountingFax(accountNo);
            if (tradingPartner != null) {
                contactConfigList.addAll(tradingPartner.getCustomerContact());
                for (Iterator iter = contactConfigList.iterator(); iter.hasNext();) {
                    CustomerContact customerContact = (CustomerContact) iter.next();
                    customerContact.setAccountName(tradingPartner.getAccountName());
                    customerContact.setAccountNo(tradingPartner.getAccountno());
                    customerContact.setAccountType(tradingPartner.getAcctType());
                    customerContact.setSubType(tradingPartner.getSubType());
                    String code = (null != customerContact.getCodec()) ? customerContact.getCodec().getCode() : null;
                    if (null != code && (code.equalsIgnoreCase(TradingPartnerConstants.E1)
                            || code.equalsIgnoreCase(TradingPartnerConstants.E3) || code.equalsIgnoreCase(TradingPartnerConstants.F1)
                            || code.equalsIgnoreCase(TradingPartnerConstants.F3))) {
                        if (CommonUtils.isEqualIgnoreCase(customerContact.getEmail(), accountingEmail)
                                || CommonUtils.isEqualIgnoreCase(customerContact.getFax(), accountingFax)) {
                            customerContact.setAccountingSelected(true);
                        }
                        resultList.add(customerContact);
                    }
                }
            }
        }
        return resultList;
    }

    public List shortCostCodeList(List<FclBlCostCodes> fclCostCodeList) {
        int j = 0;
        int k = 0;
        List newList = new ArrayList();
        LinkedList linkedList = new LinkedList();
        for (FclBlCostCodes fclBlCostCodes : fclCostCodeList) {
            if (!"AP".equalsIgnoreCase(fclBlCostCodes.getTransactionType()) && !"IP".equalsIgnoreCase(fclBlCostCodes.getTransactionType()) && !"DS".equalsIgnoreCase(fclBlCostCodes.getTransactionType())) {
                if (fclBlCostCodes.getCostCode() != null && (fclBlCostCodes.getCostCode().equals("OCNFRT") || fclBlCostCodes.getCostCode().equals("OFIMP"))) {
                    linkedList.add(k, fclBlCostCodes);
                } else {
                    linkedList.add(fclBlCostCodes);
                }
            }
            j++;
        }
        newList.addAll(linkedList);
        return newList;
    }

    public List shortManifestedCostCodeList(List<FclBlCostCodes> fclCostCodeList) {
        int j = 0;
        int k = 0;
        List newList = new ArrayList();
        LinkedList linkedList = new LinkedList();
        for (FclBlCostCodes fclBlCostCodes : fclCostCodeList) {
            if (fclBlCostCodes.getCostCode() != null && (fclBlCostCodes.getCostCode().equals("OCNFRT") || fclBlCostCodes.getCostCode().equals("OFIMP"))) {
                linkedList.add(k, fclBlCostCodes);
            } else {
                linkedList.add(fclBlCostCodes);
            }
            j++;
        }
        newList.addAll(linkedList);
        return newList;
    }

    public String checkAesDetailsForThisContainer(String containerId) throws Exception {

        String[] idArray = containerId.split(",");
        StringBuilder returnString = new StringBuilder();
        String length = idArray.length != 0 ? String.valueOf(idArray.length) : "";
        for (int i = 0; i < idArray.length; i++) {
            FclBlContainer fclBlContainer = fclBlContainerDAO.findById(Integer.parseInt(idArray[i]));
            List hazmatMaterialList = hazmatMaterialDAO.findbydoctypeid1("FclBl", Integer.parseInt(idArray[i]));
            if (fclBlContainer.getFclBlMarks() != null
                    && fclBlContainer.getFclBlMarks().size() > 0 && null != hazmatMaterialList
                    && hazmatMaterialList.size() > 0) {
                returnString.append("AesMarksHazmat");
            } else if (null != fclBlContainer.getFclBlMarks() && fclBlContainer.getFclBlMarks().size() > 0) {
                returnString.append("AesMarks");
            } else if (null != fclBlContainer.getFclBlMarks() && fclBlContainer.getFclBlMarks().size() > 0 && null != hazmatMaterialList && hazmatMaterialList.size() > 0) {
                returnString.append("MarksHazmat");
            } else if (null != hazmatMaterialList && hazmatMaterialList.size() > 0) {
                returnString.append("AesHazmat");
            } else if (fclBlContainer.getFclBlMarks() != null && fclBlContainer.getFclBlMarks().size() > 0) {
                returnString.append("Marks");
            } else if (null != hazmatMaterialList && hazmatMaterialList.size() > 0) {
                returnString.append("Hazmat");
            } else {
                returnString.append("no");
            }
            if (i < Integer.parseInt(length) - 1) {
                returnString.append(",");
            }
        }
        return returnString.toString();
    }

    public String checkAesDetailsForRefNum(String refnum) throws Exception {
        String[] idArray = refnum.split(",");
        StringBuilder returnString = new StringBuilder();
        String length = idArray.length != 0 ? String.valueOf(idArray.length) : "";
        for (int i = 0; i < idArray.length; i++) {
            if (CommonUtils.isNotEmpty(new SedSchedulebDetailsDAO().findByTrnref(idArray[i]))) {
                returnString.append("Aes");
            }
            if (i < Integer.parseInt(length) - 1) {
                returnString.append(",");
            }
        }
        return returnString.toString();
    }

    //----------DWR METHODS FOR DOJO--------------
    public CustAddress getShipperDetails(String accountName) throws Exception {
        String customerType = null;
        String clientName = accountName.replace("amp;", "").trim();
        CustAddressBC custAddressBC = new CustAddressBC();
        CustAddress custAddress = new CustAddress();
        custAddress = custAddressBC.getCustInfo(clientName, customerType);
        if (null == custAddress.getAcctName()) {
            //--in case of account number is passed...
            custAddress = custAddressBC.getCustInfoForCustNo(clientName);
        }
        return custAddress;
    }

    public CustAddress getCustomerAccountNo(String accountNo) throws Exception {
        CustAddressBC custAddressBC = new CustAddressBC();
        CustAddress custAddress = custAddressBC.getCustInfoForCustNo(accountNo);
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        tradingPartnerBC.setSubType(custAddress);
        return custAddress;
    }

    public String getChargeCode(String commodityCode) throws Exception {
        String codeTypeId = "36";
        GenericCode genericCode = genericCodeBC.getChargeCodeDescription(commodityCode, codeTypeId);
        return genericCode.getCodedesc();
    }

    public String getChargeCodeDesc(String commodityCodeDesc) throws Exception {
        String codeTypeId = "36";
        GenericCode genericCode = genericCodeBC.getChargeCode(commodityCodeDesc, codeTypeId);
        return genericCode.getCode();
    }

    public GenericCode getVesselDetails(String vesselNo, String vesselName) throws Exception {
        GenericCode genericCode = new GenericCode();
        if (vesselName.equals("")) {
            genericCode = genericCodeBC.getCommodityForFclBl(vesselNo, "");
        } else {
            genericCode = genericCodeBC.getCommodityForFclBl("", vesselName);
        }
        return genericCode;
    }

    public GenericCode getReleaseClauseDetails(String code) throws Exception {
        String codeType = "7";
        GenericCode genericCode = genericCodeBC.getCommodityDetails(codeType, code, "");
        return genericCode;
    }
    //---DWR METHODS ENDS---

    public String addContainerDetails(FclBillLaddingForm fclBillLaddingForm, MessageResources messageResources) throws Exception {
        FclBlContainer fclBlContainer = new FclBlContainer(fclBillLaddingForm);
        //--to indicate these containers are added in bl----
        fclBlContainer.setManuallyAddedFlag("M");
        fclBlDAO.saveFcl(fclBlContainer);
        if (fclBlContainer.getSizeLegend() != null) {
            updateFFCommisionForCostCode(fclBillLaddingForm, messageResources, fclBlContainer.getSizeLegend().getId());
        }

        return "Saved Successfully";
    }

    public void updateFFCommisionForCostCode(FclBillLaddingForm fclBillLaddingForm,
            MessageResources messageResources, Integer unitTypeId) throws Exception {
        String uniType[] = messageResources.getMessage("unittype").split(",");
        String ffCommissionRates[] = LoadLogisoftProperties.getProperty("ffcommissionrates").split(",");
        if (fclBillLaddingForm.getBol() != null && !fclBillLaddingForm.getBol().equals("")) {
            List<FclBlCostCodes> costCodesList = fclBlCostCodesDAO.findByParentIdAndCostCode(new Integer(fclBillLaddingForm.getBol()), FclBlConstants.FFCODE);
            double amount = 0.0;
            for (FclBlCostCodes fclBlContainer : costCodesList) {
                amount = fclBlContainer.getAmount();
                if (unitTypeId != null && unitTypeId.toString().equals(uniType[0])) {
                    amount += Double.parseDouble(ffCommissionRates[0]);
                } else {
                    amount += Double.parseDouble(ffCommissionRates[1]);
                }
                fclBlContainer.setAmount(amount);
            }
        }
    }

    public void addCostAndChargesDetails(FclBillLaddingForm fclBillLaddingForm, HttpServletRequest request, String userName, FclBl fclBl) throws Exception {
        FclBlCharges fclBlCharges = new FclBlCharges();
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        if (CommonUtils.isNotEmpty(fclBillLaddingForm.getChargeAmount())) {
            if (fclBillLaddingForm.getChargeAmount().contains(",")) {
                fclBillLaddingForm.setChargeAmount(fclBillLaddingForm.getChargeAmount().replace(",", ""));
            }
            fclBlCharges.setAmount(Double.parseDouble(fclBillLaddingForm.getChargeAmount()));
            fclBlCharges.setOldAmount(Double.parseDouble(fclBillLaddingForm.getChargeAmount()));
        }
        fclBlCharges.setBillTo(fclBillLaddingForm.getChargeBillTo());
        fclBlCharges.setCharges(fclBillLaddingForm.getChargeCodeDesc());
        fclBlCharges.setChargeCode(fclBillLaddingForm.getChargeCode());
        fclBlCharges.setCurrencyCode(fclBillLaddingForm.getChargeCurrency());
        fclBlCharges.setBolId(null != fclBillLaddingForm.getBol() && !"".equals(fclBillLaddingForm.getBol()) ? Integer.parseInt(fclBillLaddingForm.getBol()) : 0);
        fclBlCharges.setPrintOnBl(fclBillLaddingForm.getChargePrintBl());
        fclBlCharges.setChargesRemarks(fclBillLaddingForm.getComment());
        if (null != fclBl && "M".equals(fclBl.getReadyToPost())) {
            fclBl.setCorrectedAfterManifest("Y");
            fclBlCharges.setReadyToPost("M");
        }
        fclBlChargesDAO.save(fclBlCharges);
        notesBC.saveNotesWhileAddingFclBlCharges(fclBlCharges, userName);

        if (CommonUtils.isNotEmpty(fclBillLaddingForm.getCostAmount()) && !"0.0".equals(fclBillLaddingForm.getCostAmount())
                && !"0.00".equals(fclBillLaddingForm.getCostAmount()) && !"0".equals(fclBillLaddingForm.getCostAmount())) {
            if (fclBillLaddingForm.getCostAmount().contains(",")) {
                fclBillLaddingForm.setCostAmount(fclBillLaddingForm.getCostAmount().replace(",", ""));
            }
            fclBlCostCodes.setAmount(Double.parseDouble(fclBillLaddingForm.getCostAmount()));
            fclBlCostCodes.setCostCode(fclBillLaddingForm.getChargeCode());
            fclBlCostCodes.setCostCodeDesc(fclBillLaddingForm.getChargeCodeDesc());
            fclBlCostCodes.setAccName(fclBillLaddingForm.getVendorName());
            fclBlCostCodes.setAccNo(fclBillLaddingForm.getVendorNumber());
            fclBlCostCodes.setCostComments(fclBillLaddingForm.getComment());
            fclBlCostCodes.setCurrencyCode(fclBillLaddingForm.getChargeCurrency());
            fclBlCostCodes.setInvoiceNumber(fclBillLaddingForm.getInvoiceNumber());
            fclBlCostCodes.setBolId(null != fclBillLaddingForm.getBol() && !"".equals(fclBillLaddingForm.getBol()) ? Integer.parseInt(fclBillLaddingForm.getBol()) : 0);
            fclBlCostCodes.setAccrualsCreatedDate(new Date());
            fclBlCostCodes.setAccrualsCreatedBy(userName);
            fclBlCostCodes.setTransactionType("AC");
            if (null != fclBl && "M".equals(fclBl.getReadyToPost())) {
                fclBlCostCodes.setReadyToPost("M");
            }
            fclBlCostCodesDAO.save(fclBlCostCodes);
            notesBC.saveNotesWhileAddingFclBlCostCodes(fclBlCostCodes, userName);
            List fclBlCostCodeSummeryList = new ArrayList();
            fclBlCostCodeSummeryList.add(fclBlCostCodes);
            manifestBc.getTransactionObject(fclBl, getFclBlCostBeanobject(null, fclBlCostCodeSummeryList, null, null, null), request);
        }
//        if (null != fclBl && "M".equals(fclBl.getReadyToPost())) {
        List fclBlChargesSummeryList = new ArrayList();
        fclBlChargesSummeryList.add(fclBlCharges);
        if (null != fclBl && "M".equals(fclBl.getReadyToPost())) {
            manifestBc.getTransactionObject(fclBl, getFclBlCostBeanobject(fclBlChargesSummeryList, null, null, null, null), request);
            TransactionLedger transactionLedger = transactionLedgerDAO.getArTaransactionByChargeId(fclBlCharges.getChargesId());
            if (null != transactionLedger) {
                AccrualsDAO accrualsDAO = new AccrualsDAO();
                Date postedDate = accrualsDAO.getPostedDate("I".equalsIgnoreCase(fclBl.getImportFlag()) ? fclBl.getEta() : fclBl.getSailDate());
                List<TransactionLedger> transactionLedgerList = new ArrayList<TransactionLedger>();
                transactionLedgerList.add(transactionLedger);
                Transaction transaction = transactionDAO.findByTransactionByBillNoAndCustomer(fclBl.getBolId(), transactionLedger.getCustNo());
                if (null != transaction) {
                    transaction.setTransactionAmt(transaction.getTransactionAmt() + fclBlCharges.getAmount());
                    transaction.setBalance(transaction.getBalance() + fclBlCharges.getAmount());
                    transaction.setBalanceInProcess(transaction.getBalanceInProcess() + fclBlCharges.getAmount());
                    if (fclBlCharges.getAmount() != 0d) {
                        ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                        arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
                        arTransactionHistory.setPostedDate(postedDate);
                        arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
                        arTransactionHistory.setTransactionAmount(fclBlCharges.getAmount());
                        arTransactionHistory.setBlNumber(fclBl.getBolId());
                        arTransactionHistory.setCustomerNumber(transaction.getCustNo());
                        arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
                        arTransactionHistory.setCreatedBy(userName);
                        arTransactionHistory.setCreatedDate(new Date());
                        arTransactionHistory.setTransactionType("FCL BL");
                        new ArTransactionHistoryDAO().save(arTransactionHistory);
                    }

                } else {
                    manifestBc.createTransactionAfterManifest(transactionLedgerList, fclBl, userName);
                }
            }
        }
//        }
    }

    public String addChargesDetails(FclBillLaddingForm fclBillLaddingForm, HttpServletRequest request) throws Exception {
        FclBlCharges fclBlCharges = new FclBlCharges(fclBillLaddingForm);
        fclBlCharges.setOldAmount(fclBlCharges.getAmount());
        if (CommonFunctions.isNotNull(fclBillLaddingForm.getAdjustmentAmount())) {
            fclBlCharges.setAdjustment(new Double(fclBillLaddingForm.getAdjustmentAmount()));
            fclBlCharges.setAmount(fclBlCharges.getAmount() + fclBlCharges.getAdjustment());
        }
        fclBlDAO.saveFclCharges(fclBlCharges);
        if (null != fclBlCharges) {
            String userName = "";
            HttpSession session = request.getSession(true);
            User user = new User();
            if (session.getAttribute("loginuser") != null) {
                user = (User) session.getAttribute("loginuser");
                userName = user.getLoginName();
            }
            notesBC.saveNotesWhileAddingFclBlCharges(fclBlCharges, userName);
        }
        if (fclBlCharges.getBolId() != null) {
            FclBl fclBl = fclBlDAO.findById(fclBlCharges.getBolId());
            fclBl.getFclcharge().add(fclBlCharges);
            if (fclBlCharges.getChargeCode() != null && (fclBlCharges.getChargeCode().trim().equalsIgnoreCase(
                    FclBlConstants.ADVANCEFFCODE) || fclBlCharges.getChargeCode().trim().equalsIgnoreCase(
                            FclBlConstants.ADVANCESHIPPERCODE))) {
                calculatePBAADVSHPWhileManifest(fclBl, request);
            }
        }
        return "Saved Successfully";
    }

    public boolean updateChargesDetails(FclBillLaddingForm fclBillLaddingForm, String userName) throws Exception {
        Double amount = 0.0;
        boolean hasUpdated = false;
        FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
        StringBuilder message = new StringBuilder();
        FclBlCharges fclBlCharges = fclBlChargesDAO.findById(Integer.parseInt(fclBillLaddingForm.getChargeId()));
        double newAmount = 0.00;
        double adjustment = 0.00;
        double oldAmount = 0.00;
        double transactionAmount = 0.00;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        formatter.setTimeZone(TimeZone.getDefault());
        formatter.setLenient(false);

        if ("manualCharge".equals(fclBillLaddingForm.getEditFlag())) {
            fclBlCharges.setAdjustment(adjustment);
            if (null != fclBillLaddingForm.getManualChargeAmount()) {
                fclBlCharges.setOldAmount(Double.parseDouble(dbUtil.removeComma(fclBillLaddingForm.getManualChargeAmount())));
                oldAmount = Double.parseDouble(dbUtil.removeComma(fclBillLaddingForm.getManualChargeAmount()));
            }
            if (null != fclBillLaddingForm.getChargeAmount()) {
                newAmount = Double.parseDouble(dbUtil.removeComma(fclBillLaddingForm.getChargeAmount()));
                transactionAmount = newAmount - oldAmount;
                fclBlCharges.setAmount(fclBlCharges.getAmount() + newAmount - oldAmount);
            }

        } else if (CommonFunctions.isNotNull(fclBillLaddingForm.getAdjustmentAmount())) {
            if (null != fclBlCharges.getAmount()) {
                amount = fclBlCharges.getAmount();
            }
            if (null != fclBlCharges.getAdjustment()) {
                amount -= fclBlCharges.getAdjustment();
            }
            fclBlCharges.setOldAmount(amount);
            fclBlCharges.setAdjustment(Double.parseDouble(dbUtil.removeComma(fclBillLaddingForm.getAdjustmentAmount())));

            if (null != fclBillLaddingForm.getAdjustmentChargesRemarks() && !("".equals(fclBillLaddingForm.getAdjustmentChargesRemarks()))) {
                String comments = fclBillLaddingForm.getAdjustmentChargesRemarks() + "(" + userName + "-" + formatter.format(new Date()) + ").";
                fclBlCharges.setAdjustmentChargesRemarks(comments);
            }
            if (null != fclBillLaddingForm.getManualChargeAmount()) {
                oldAmount = Double.parseDouble(dbUtil.removeComma(fclBillLaddingForm.getManualChargeAmount()));
            }

            if (null != fclBlCharges.getOldAmount()) {
                fclBlCharges.setAmount(fclBlCharges.getAdjustment() + fclBlCharges.getOldAmount());
            }
            newAmount = (null != fclBillLaddingForm.getOldAmount() ? Double.parseDouble(dbUtil.removeComma(fclBillLaddingForm.getOldAmount())) : 0d) + fclBlCharges.getAdjustment();
        }
        String oldBillTo = fclBlCharges.getBillTo();
        String newBillTo = fclBillLaddingForm.getChargeBillTo();
        if (fclBillLaddingForm.getChargeBillTo() != null) {
            fclBlCharges.setBillTo(fclBillLaddingForm.getChargeBillTo());
        }
        if (fclBillLaddingForm.getChargeCodeDesc() != null) {
            fclBlCharges.setCharges(fclBillLaddingForm.getChargeCodeDesc());
        }
        if (fclBillLaddingForm.getChargeCode() != null) {
            fclBlCharges.setChargeCode(fclBillLaddingForm.getChargeCode());
        }
        if (fclBillLaddingForm.getChargeCurrency() != null) {
            fclBlCharges.setCurrencyCode(fclBillLaddingForm.getChargeCurrency());
        }
        if (CommonUtils.isNotEmpty(fclBillLaddingForm.getChargePrintBl())) {
            fclBlCharges.setPrintOnBl(fclBillLaddingForm.getChargePrintBl());
        } else {
            fclBlCharges.setPrintOnBl("Yes");
        }
        fclBlCharges.setBundleIntoOfr(fclBillLaddingForm.getBundleIntoOfr());
        fclBlCharges.setChargesRemarks(CommonFunctions.isNotNull(fclBillLaddingForm.getChargesRemarks())
                ? fclBillLaddingForm.getChargesRemarks().toUpperCase() : fclBillLaddingForm.getChargesRemarks());
        FclBl bl = fclBlDAO.findById(fclBlCharges.getBolId());
        String noteDesc = "";
        Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
        if (null != bl && CommonUtils.isEqualIgnoreCase(bl.getReadyToPost(), "M")
                && "I".equalsIgnoreCase(bl.getImportFlag()) && CommonUtils.isNotEqualIgnoreCase(oldBillTo, newBillTo)) {
            hasUpdated = true;
            noteDesc = " Bill To Party - " + oldBillTo + " to " + newBillTo;
            String billingTerminal = "";
            if (CommonUtils.isNotEmpty(bl.getBillingTerminal()) && bl.getBillingTerminal().lastIndexOf("-") > -1) {
                billingTerminal = bl.getBillingTerminal().substring(bl.getBillingTerminal().lastIndexOf("-") + 1);
            }
            bl.setCorrectedAfterManifest("Y");
            TransactionLedger transactionLedger = transactionLedgerDAO.findByChargeId(fclBlCharges.getChargesId());
            String oldCustNo = transactionLedger.getCustNo();
            String newCustNo = null;
            if (CommonUtils.isEqualIgnoreCase(newBillTo, "Shipper")) {
                newCustNo = bl.getHouseShipper();
            } else if (CommonUtils.isEqualIgnoreCase(newBillTo, "Forwarder")) {
                newCustNo = bl.getForwardAgentNo();
            } else if (CommonUtils.isEqualIgnoreCase(newBillTo, "Consignee")) {
                newCustNo = bl.getConsigneeNo();
            } else if (CommonUtils.isEqualIgnoreCase(newBillTo, "ThirdParty")) {
                newCustNo = bl.getBillTrdPrty();
            } else if (CommonUtils.isEqualIgnoreCase(newBillTo, "Agent")) {
                newCustNo = bl.getAgentNo();
            } else if (CommonUtils.isEqualIgnoreCase(newBillTo, "Notify")) {
                newCustNo = bl.getNotifyParty();
            }
            String newCustName = new TradingPartnerDAO().getAccountName(newCustNo);
            if (null != transactionLedger) {
                TransactionLedger oldBillToSubledger = new TransactionLedger();
                PropertyUtils.copyProperties(oldBillToSubledger, transactionLedger);
                oldBillToSubledger.setTransactionId(null);
                oldBillToSubledger.setTransactionAmt(-fclBlCharges.getAmount());
                oldBillToSubledger.setBalance(-fclBlCharges.getAmount());
                oldBillToSubledger.setBalanceInProcess(-fclBlCharges.getAmount());
                oldBillToSubledger.setChargeCode(fclBlCharges.getChargeCode());
                oldBillToSubledger.setStatus("Open");
                oldBillToSubledger.setJournalEntryNumber(null);
                oldBillToSubledger.setLineItemNumber(null);
                oldBillToSubledger.setPostedToGlDate(null);
                oldBillToSubledger.setTransactionDate(new Date());
                oldBillToSubledger.setSubledgerSourceCode("AR-CN");
                oldBillToSubledger.setPostedDate(postedDate);
                String bluescreenChargeCode = glMappingDAO.getBlueScreenChargeCode("FCLI", fclBlCharges.getChargeCode(), "AR", "R");
                String glAccountNumber = glMappingDAO.dervieGlAccount(fclBlCharges.getChargeCode(), "FCLI", billingTerminal, "R");
                oldBillToSubledger.setBlueScreenChargeCode(bluescreenChargeCode);
                oldBillToSubledger.setGlAccountNumber(glAccountNumber);
                transactionLedgerDAO.save(oldBillToSubledger);

                Transaction oldBillToAr = transactionDAO.findByTransactionByBillNoAndCustomer(bl.getBolId(), oldCustNo);
                if (null != oldBillToAr) {
                    oldBillToAr.setTransactionAmt(oldBillToAr.getTransactionAmt() - fclBlCharges.getAmount());
                    oldBillToAr.setBalance(oldBillToAr.getBalance() - fclBlCharges.getAmount());
                    oldBillToAr.setBalanceInProcess(oldBillToAr.getBalanceInProcess() - fclBlCharges.getAmount());
                    ArTransactionHistory oldBillToHistory = new ArTransactionHistory();
                    oldBillToHistory.setInvoiceDate(oldBillToAr.getTransactionDate());
                    oldBillToHistory.setPostedDate(postedDate);
                    oldBillToHistory.setTransactionDate(new Date());
                    oldBillToHistory.setTransactionAmount(-fclBlCharges.getAmount());
                    oldBillToHistory.setBlNumber(bl.getBolId());
                    oldBillToHistory.setCustomerNumber(oldBillToAr.getCustNo());
                    oldBillToHistory.setVoyageNumber(oldBillToAr.getVoyageNo());
                    oldBillToHistory.setCreatedBy(userName);
                    oldBillToHistory.setCreatedDate(new Date());
                    oldBillToHistory.setTransactionType("FCL CN");
                    new ArTransactionHistoryDAO().save(oldBillToHistory);
                }

                TransactionLedger newBillToSubledger = new TransactionLedger();
                PropertyUtils.copyProperties(newBillToSubledger, oldBillToSubledger);
                newBillToSubledger.setTransactionId(null);
                newBillToSubledger.setTransactionAmt(fclBlCharges.getAmount());
                newBillToSubledger.setBalance(fclBlCharges.getAmount());
                newBillToSubledger.setBalanceInProcess(fclBlCharges.getAmount());
                newBillToSubledger.setChargeCode(fclBlCharges.getChargeCode());
                newBillToSubledger.setStatus("Open");
                newBillToSubledger.setCustName(newCustName);
                newBillToSubledger.setCustNo(newCustNo);
                newBillToSubledger.setJournalEntryNumber(null);
                newBillToSubledger.setLineItemNumber(null);
                newBillToSubledger.setPostedToGlDate(null);
                newBillToSubledger.setTransactionDate(new Date());
                newBillToSubledger.setPostedDate(postedDate);
                newBillToSubledger.setBlueScreenChargeCode(bluescreenChargeCode);
                newBillToSubledger.setGlAccountNumber(glAccountNumber);
                transactionLedgerDAO.save(newBillToSubledger);

                Transaction newBillToAr = transactionDAO.findByTransactionByBillNoAndCustomer(bl.getBolId(), newCustNo);
                if (null != newBillToAr) {
                    newBillToAr.setTransactionAmt(newBillToAr.getTransactionAmt() + fclBlCharges.getAmount());
                    newBillToAr.setBalance(newBillToAr.getBalance() + fclBlCharges.getAmount());
                    newBillToAr.setBalanceInProcess(newBillToAr.getBalanceInProcess() + fclBlCharges.getAmount());
                } else {
                    newBillToAr = new Transaction();
                    PropertyUtils.copyProperties(newBillToAr, oldBillToAr);
                    newBillToAr.setTransactionAmt(fclBlCharges.getAmount());
                    newBillToAr.setBalance(fclBlCharges.getAmount());
                    newBillToAr.setBalanceInProcess(fclBlCharges.getAmount());
                    newBillToAr.setTransactionDate(bl.getEta());
                    newBillToAr.setPostedDate(postedDate);
                    newBillToAr.setStatus("Open");
                    newBillToAr.setCustName(newCustName);
                    newBillToAr.setCustNo(newCustNo);
                    transactionDAO.save(newBillToAr);
                }
                ArTransactionHistory newBillToHistory = new ArTransactionHistory();
                newBillToHistory.setInvoiceDate(newBillToAr.getTransactionDate());
                newBillToHistory.setPostedDate(postedDate);
                newBillToHistory.setTransactionDate(new Date());
                newBillToHistory.setTransactionAmount(fclBlCharges.getAmount());
                newBillToHistory.setBlNumber(bl.getBolId());
                newBillToHistory.setCustomerNumber(newBillToAr.getCustNo());
                newBillToHistory.setVoyageNumber(newBillToAr.getVoyageNo());
                newBillToHistory.setCreatedBy(userName);
                newBillToHistory.setCreatedDate(new Date());
                newBillToHistory.setTransactionType("FCL CN");
                new ArTransactionHistoryDAO().save(newBillToHistory);
            }

        } else if (null != bl && transactionAmount != 0d) {
            //Update AR Records
            String billingTerminal = "";
            if (CommonUtils.isNotEmpty(bl.getBillingTerminal()) && bl.getBillingTerminal().lastIndexOf("-") > -1) {
                billingTerminal = bl.getBillingTerminal().substring(bl.getBillingTerminal().lastIndexOf("-") + 1);
            }
            if ("M".equalsIgnoreCase(fclBlCharges.getReadyToPost()) && "I".equalsIgnoreCase(bl.getImportFlag())) {
                bl.setCorrectedAfterManifest("Y");
                TransactionLedger transactionLedger = transactionLedgerDAO.getArTaransactionByChargeId(fclBlCharges.getChargesId());
                TransactionLedger newLedger = new TransactionLedger();
                if (null != transactionLedger) {
                    if ("manualCharge".equals(fclBillLaddingForm.getEditFlag())) {
                        PropertyUtils.copyProperties(newLedger, transactionLedger);
                        newLedger.setTransactionAmt(transactionAmount);
                        newLedger.setBalance(transactionAmount);
                        newLedger.setBalanceInProcess(transactionAmount);
                        newLedger.setChargeCode(fclBlCharges.getChargeCode());
                        newLedger.setTransactionDate(new Date());
                        newLedger.setStatus("Open");
                        newLedger.setJournalEntryNumber(null);
                        newLedger.setLineItemNumber(null);
                        newLedger.setBlueScreenChargeCode(glMappingDAO.getBlueScreenChargeCode("FCLI", fclBlCharges.getChargeCode(), "AR", "R"));
                        newLedger.setGlAccountNumber(glMappingDAO.dervieGlAccount(fclBlCharges.getChargeCode(), "FCLI", billingTerminal, "R"));
                        newLedger.setPostedDate(postedDate);
                        transactionLedgerDAO.save(newLedger);
                    } else {
                        transactionLedger.setTransactionAmt(fclBlCharges.getAmount());
                        transactionLedger.setBalance(fclBlCharges.getAmount());
                        transactionLedger.setBalanceInProcess(fclBlCharges.getAmount());
                        transactionLedger.setChargeCode(fclBlCharges.getChargeCode());
                        transactionLedger.setTransactionDate(new Date());
                        transactionLedger.setBlueScreenChargeCode(glMappingDAO.getBlueScreenChargeCode("FCLI", fclBlCharges.getChargeCode(), "AR", "R"));
                        transactionLedger.setGlAccountNumber(glMappingDAO.dervieGlAccount(fclBlCharges.getChargeCode(), "FCLI", billingTerminal, "R"));
                    }
                    //Update transaction object
                    Transaction transaction = transactionDAO.findByTransactionByBillNoAndCustomer(transactionLedger.getBillLaddingNo(), transactionLedger.getCustNo());
                    if (null != transaction) {
                        transaction.setTransactionAmt(transaction.getTransactionAmt() + transactionAmount);
                        transaction.setBalance(transaction.getTransactionAmt());
                        transaction.setBalanceInProcess(transaction.getTransactionAmt());
                        transaction.setChargeCode(transaction.getChargeCode());
                        transaction.setTransactionDate(transactionLedger.getTransactionDate());
                        transaction.setGlAccountNumber(transactionLedger.getGlAccountNumber());
                        if (transactionAmount != 0d) {
                            ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                            arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
                            arTransactionHistory.setPostedDate(postedDate);
                            arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
                            arTransactionHistory.setTransactionAmount(transactionAmount);
                            arTransactionHistory.setBlNumber(transactionLedger.getBillLaddingNo());
                            arTransactionHistory.setCustomerNumber(transaction.getCustNo());
                            arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
                            arTransactionHistory.setCreatedBy(userName);
                            arTransactionHistory.setCreatedDate(new Date());
                            arTransactionHistory.setTransactionType("FCL BL");
                            new ArTransactionHistoryDAO().save(arTransactionHistory);
                        }
                    }
                }
            }
        }
        if (null != fclBlCharges) {
            if (CommonFunctions.isNotNull(fclBillLaddingForm.getManualChargeAmount())) {
                if (oldAmount != newAmount) {
                    hasUpdated = true;
                    noteDesc = " Cost - " + numberFormat.format(oldAmount) + " to " + numberFormat.format(newAmount);
                }
            }
            if (hasUpdated) {
                message.append("UPDATED ->Charge Code - ").append(fclBlCharges.getChargeCode());
                message.append(" Currency - ").append(fclBlCharges.getCurrencyCode());
                noteDesc = message.toString() + " " + noteDesc;
                if (CommonUtils.isNotEmpty(fclBlCharges.getChargesRemarks())) {
                    noteDesc += " Comment -" + fclBlCharges.getChargesRemarks();
                }
                Notes note = new Notes();
                note.setModuleId(NotesConstants.FILE);
                String fileNo = "";
                if (null != fclBlCharges.getBolId()) {
                    String bolId = fclBlCharges.getBolId().toString();
                    fileNo = fclBlDAO.getFileNo(bolId);
                }
                note.setModuleRefId(fileNo);
                note.setUpdateDate(new Date());
                note.setUpdatedBy(userName);
                note.setNoteType(NotesConstants.AUTO);
                note.setNoteDesc(noteDesc);
                notesBC.saveNotes(note);
            }
        }
        //updating bill to party for ocean freight groups
        List fclBlChargesList = fclBlChargesDAO.findByParentId(fclBlCharges.getBolId());
        for (Iterator iterator = fclBlChargesList.iterator(); iterator.hasNext();) {
            FclBlCharges tempFclBlCharges = (FclBlCharges) iterator.next();
            if (fclBlCharges.getChargeCode().equalsIgnoreCase("OCNFRT")) {
                if (tempFclBlCharges.getReadOnlyFlag() != null && tempFclBlCharges.getReadOnlyFlag().equalsIgnoreCase("on")
                        && tempFclBlCharges.getBookingFlag() == null) {
                    if (!tempFclBlCharges.getChargeCode().equalsIgnoreCase("BKRSUR") && !tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTMDL")
                            && !tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTFS") && !tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTRAMP")
                            && !tempFclBlCharges.getChargeCode().equalsIgnoreCase("HAZFEE")) {
                        tempFclBlCharges.setBillTo(fclBlCharges.getBillTo());
                    }
                }
            } else if (fclBlCharges.getChargeCode().equalsIgnoreCase("INTMDL") && tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTFS")
                    && tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTRAMP")) {
                tempFclBlCharges.setBillTo(fclBlCharges.getBillTo());
            } else if (fclBlCharges.getChargeCode().equalsIgnoreCase("INTFS") && tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTMDL")
                    && tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTRAMP")) {
                tempFclBlCharges.setBillTo(fclBlCharges.getBillTo());
            } else if (fclBlCharges.getChargeCode().equalsIgnoreCase("INTRAMP") && tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTMDL")
                    && tempFclBlCharges.getChargeCode().equalsIgnoreCase("INTFS")) {
                tempFclBlCharges.setBillTo(fclBlCharges.getBillTo());
            }
        }
        return hasUpdated;
    }

    public String deleteChargesDetails(String chargeId, String userName, MessageResources messageResources, HttpServletRequest request) throws Exception {
        FclBlCharges fclBlCharges = fclBlChargesDAO.findById(Integer.parseInt(chargeId));
        String charges = "";
        if (null != fclBlCharges) {
            charges = fclBlCharges.getChargeCode();
            StringBuilder message = new StringBuilder();
            message.append("DELETED ->Charge Code - ").append(fclBlCharges.getChargeCode()).append(" Cost - ");
            message.append(numberFormat.format(fclBlCharges.getAmount())).append(" Currency - ").append(fclBlCharges.getCurrencyCode());
            Notes note = new Notes();
            note.setModuleId(NotesConstants.FILE);
            String fileNo = "";
            String bolId = "";
            FclBl fclBl = new FclBl();
            boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
            if (null != fclBlCharges.getBolId()) {
                bolId = fclBlCharges.getBolId().toString();
                fileNo = fclBlDAO.getFileNo(bolId);
                fclBl = fclBlDAO.findById(fclBlCharges.getBolId());
            }
            note.setModuleRefId(fileNo);
            note.setUpdateDate(new Date());
            note.setUpdatedBy(userName);
            note.setNoteType(NotesConstants.AUTO);
            note.setNoteDesc(message.toString());
            notesBC.saveNotes(note);
            if ((fclBlCharges.getChargeCode().equals("OCNFRT") || fclBlCharges.getChargeCode().equals("OFIMP"))
                    && CommonUtils.isNotEmpty(fclBlCharges.getReadOnlyFlag()) && !bolId.isEmpty()) {
                List<FclBlCharges> fclBlChargesList = fclBlChargesDAO.findByParentId(Integer.parseInt(bolId));
                List<FclBlCharges> collapseList = consolidateRates(fclBlChargesList, messageResources, importFlag);
                StringBuilder r = new StringBuilder();
                for (FclBlCharges fclBlCollapsCharges : collapseList) {
                    if ((!fclBlCollapsCharges.getChargeCode().equals("OCNFRT") || !fclBlCollapsCharges.getChargeCode().equals("OFIMP"))) {
                        r.append(",'").append(fclBlCollapsCharges.getChargeCode()).append("'");
                    }
                }
                String collapsechargeCodes = r.toString().replaceFirst(",", "");
                if (!collapsechargeCodes.isEmpty()) {
                    fclBlChargesDAO.deleteNonCollapseChargesForOcfr(Integer.parseInt(bolId), collapsechargeCodes);
                }
            } else {
                fclBl.getFclcharge().remove(fclBlCharges);
                fclBlChargesDAO.delete(fclBlCharges);
            }
            String importFlag1 = null != fclBl.getImportFlag() ? fclBl.getImportFlag() : "";
            if (fclBlDAO.isFaeReCalculaionRequiredWhileDelete(fclBlCharges.getBolId(), "cost", fclBlCharges.getChargeCode()) && !"I".equals(importFlag1)) {
                FaeReCalculationWhileDelete(fclBlCharges.getBolId(), request);
            }
            fclBlDAO.update(fclBl);
        }
        return charges;
    }

    public CustomerTemp getAccountNumberForThirdParty(String accoutName) throws Exception {
        return (CustomerTemp) custAddressDAO.findAccountName(accoutName).get(0);
    }

    public void addCostDetails(FclBillLaddingForm fclBillLaddingForm, HttpServletRequest request, String userName) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes(fclBillLaddingForm);

        fclBlCostCodes.setAmount(Double.parseDouble(dbUtil.removeComma(fclBillLaddingForm.getCostAmount())));
        Date date = null;
        if (null != fclBillLaddingForm.getDatePaid() && !fclBillLaddingForm.getDatePaid().trim().equals("")) {
            date = sdf.parse(fclBillLaddingForm.getDatePaid());
            fclBlCostCodes.setDatePaid(date);
        }
        fclBlCostCodes.setAccrualsCreatedDate(new Date());
        fclBlCostCodes.setAccrualsCreatedBy(userName);
        if ("FAECOMM".equalsIgnoreCase(fclBlCostCodes.getCostCode())) {
            fclBlCostCodes.setBookingFlag("FAE");
        }
        fclBlCostCodesDAO.save(fclBlCostCodes);
        if (null != fclBlCostCodes) {
            notesBC.saveNotesWhileAddingFclBlCostCodes(fclBlCostCodes, userName);
        }
        FclBl fclBl = fclBlDAO.findById(fclBlCostCodes.getBolId());
        fclBl.getFclblcostcodes().add(fclBlCostCodes);
        String costCode = fclBlCostCodes.getCostCode();

        if (costCode != null && (costCode.trim().equals(FclBlConstants.ADVANCEFFCODE) || costCode.trim().equals(FclBlConstants.PBACODE))) {
            fclBlCostCodes.setAccName(fclBl.getForwardingAgentName());
            fclBlCostCodes.setAccNo(fclBl.getForwardAgentNo());
        } else if (costCode != null && costCode.trim().equals(FclBlConstants.ADVANCESHIPPERCODE)) {
            fclBlCostCodes.setAccName(fclBl.getShipperName());
            fclBlCostCodes.setAccNo(fclBl.getShipperNo());
        }
        addCostCodeToAccruals(fclBlCostCodes, fclBl, request);
    }

    public void addCostCodeToAccruals(FclBlCostCodes fclCost, FclBl bl, HttpServletRequest request) throws Exception {
        fclCost.setTransactionType("AC");
        fclCost.setReadyToPost("M");
        fclCost.setManifestModifyFlag("yes");
        fclBlCostCodesDAO.update(fclCost);
        List<CostModel> costs = new ArrayList<CostModel>();
        CostModel cost = new CostModel();
        cost.setId(fclCost.getCodeId());
        cost.setVendorName(fclCost.getAccName());
        cost.setVendorNumber(fclCost.getAccNo());
        cost.setCostCode(fclCost.getCostCode());
        cost.setAmount(fclCost.getAmount());
        cost.setInvoiceNumber(fclCost.getInvoiceNumber());
        cost.setCurrency(fclCost.getCurrencyCode());
        cost.setComments(fclCost.getComments());
        costs.add(cost);
        User user = (User) request.getSession().getAttribute("loginuser");
        new FclManifestDAO(bl, user).postAccruals(costs);
    }

    public boolean updateCostDetails(FclBillLaddingForm fclBillLaddingForm, String userName) throws Exception {
        FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(Integer.parseInt(fclBillLaddingForm.getCostCodeId()));
        double oldCostCodeAmount = fclBlCostCodes.getAmount();
        Date date = null;
        boolean hasChanged = false;
        boolean hasUpdated = false;
        StringBuilder message = new StringBuilder();
        if (null != fclBlCostCodes) {
            // getting  amount and deducting from rollup charges
            if (!fclBillLaddingForm.getCostAmount().trim().equalsIgnoreCase(fclBillLaddingForm.getRollUpAmount().trim())) {
                Double amount = new Double(fclBillLaddingForm.getCostAmount().replace(",", ""));
                Double rollUpAmount = new Double(fclBillLaddingForm.getRollUpAmount().replace(",", ""));
                rollUpAmount = amount - rollUpAmount;
                Double AmountToassign = fclBlCostCodes.getAmount() + rollUpAmount;
                if (AmountToassign != fclBlCostCodes.getAmount()) {
                    hasChanged = true;
                }
                fclBlCostCodes.setAmount(AmountToassign);

            }
            if ((oldCostCodeAmount != fclBlCostCodes.getAmount()) && "M".equalsIgnoreCase(fclBlCostCodes.getReadyToPost())) {
                fclBlCostCodesDAO.updateOldAmount(fclBlCostCodes.getCodeId(), oldCostCodeAmount);

            }
            fclBlCostCodes.setAccrualsUpdatedBy(userName);
            fclBlCostCodes.setManifestModifyFlag("yes");
            fclBlCostCodes.setProcessedStatus("");
            fclBlCostCodes.setAccrualsUpdatedDate(new Date());
            if (null != fclBlCostCodes.getCostCode() && !fclBlCostCodes.getCostCode().equals(fclBillLaddingForm.getCostCode())) {
                hasChanged = true;
            }
            fclBlCostCodes.setCostCode(null != fclBillLaddingForm.getCostCode() ? fclBillLaddingForm.getCostCode() : "");
            fclBlCostCodes.setCostCodeDesc(null != fclBillLaddingForm.getCostCodeDesc() ? fclBillLaddingForm.getCostCodeDesc() : "");
            if (null != fclBlCostCodes.getAccNo() && !fclBlCostCodes.getAccNo().equals(fclBillLaddingForm.getAccountNo())) {
                hasChanged = true;
            }
            fclBlCostCodes.setAccName(null != fclBillLaddingForm.getAccountName() ? fclBillLaddingForm.getAccountName() : "");
            fclBlCostCodes.setAccNo(null != fclBillLaddingForm.getAccountNo() ? fclBillLaddingForm.getAccountNo() : "");
            if (null != fclBlCostCodes.getInvoiceNumber() && !fclBlCostCodes.getInvoiceNumber().equals(fclBillLaddingForm.getInvoiceNumber())) {
                hasChanged = true;
            }
            fclBlCostCodes.setInvoiceNumber(fclBillLaddingForm.getInvoiceNumber());
            fclBlCostCodes.setCurrencyCode(null != fclBillLaddingForm.getCostCurrency() ? fclBillLaddingForm.getCostCurrency() : "");
            if (null != fclBillLaddingForm.getDatePaid() && !fclBillLaddingForm.getDatePaid().equals("")) {
                date = sdf.parse(fclBillLaddingForm.getDatePaid());
                fclBlCostCodes.setDatePaid(date);
            }
            fclBlCostCodes.setCheckNo(null != fclBillLaddingForm.getChequeNumber() ? fclBillLaddingForm.getChequeNumber() : "");
            if (null != fclBlCostCodes.getCostComments() && !fclBlCostCodes.getCostComments().equals(fclBillLaddingForm.getCostComments())) {
                hasChanged = true;
            }
            fclBlCostCodes.setCostComments(CommonFunctions.isNotNull(fclBillLaddingForm.getCostComments())
                    ? fclBillLaddingForm.getCostComments().toUpperCase() : fclBillLaddingForm.getCostComments());
            fclBlCostCodesDAO.update(fclBlCostCodes);
            // code to update OCNFRT bundled cost codes
            if (null != fclBillLaddingForm.getCostCode() && "OCNFRT".equalsIgnoreCase(fclBillLaddingForm.getCostCode())) {
                List<FclBlCostCodes> ofrCostCodeList = fclBlCostCodesDAO.getOFRCostCodes(fclBlCostCodes.getBolId());
                for (FclBlCostCodes ofrCostCode : ofrCostCodeList) {
                    if (null != ofrCostCode.getCostCode() && !"OCNFRT".equalsIgnoreCase(ofrCostCode.getCostCode())) {
                        ofrCostCode.setAccNo(null != fclBlCostCodes.getAccNo()
                                ? fclBlCostCodes.getAccNo() : "");
                        ofrCostCode.setAccName(null != fclBlCostCodes.getAccName()
                                ? fclBlCostCodes.getAccName() : "");
                        ofrCostCode.setInvoiceNumber(null != fclBlCostCodes.getInvoiceNumber()
                                ? fclBlCostCodes.getInvoiceNumber() : "");
                        ofrCostCode.setAccrualsUpdatedBy(userName);
                        ofrCostCode.setAccrualsUpdatedDate(new Date());
                        fclBlCostCodesDAO.update(ofrCostCode);
                    }
                }
            }

            FclBl fclBl = fclBlDAO.findById(fclBlCostCodes.getBolId());
            if (hasChanged) {
                fclBl.getFclblcostcodes().add(fclBlCostCodes);
                message.append("UPDATED ->Cost Code - ").append(fclBlCostCodes.getCostCodeDesc());
                if (!fclBillLaddingForm.getCostAmount().trim().equalsIgnoreCase(fclBillLaddingForm.getRollUpAmount().trim())) {
                    Double newAmount = new Double(fclBillLaddingForm.getCostAmount().replace(",", ""));
                    Double rollAmount = new Double(fclBillLaddingForm.getRollUpAmount().replace(",", ""));
                    hasUpdated = true;
                    message.append(" Cost - ").append(numberFormat.format(rollAmount)).append(" to ").append(numberFormat.format(newAmount));
                }
                message.append(" Vendor Name - ").append(fclBlCostCodes.getAccName());
                message.append(" Vendor Number - ").append(fclBlCostCodes.getAccNo());
                message.append(" Invoice Number - ").append(fclBlCostCodes.getInvoiceNumber());
                if (CommonUtils.isNotEmpty(fclBlCostCodes.getCostComments())) {
                    message.append(" Comment -").append(fclBlCostCodes.getCostComments());
                }
                Notes note = new Notes();
                note.setModuleId(NotesConstants.FILE);
                note.setUniqueId("" + fclBlCostCodes.getCodeId());
                String fileNo = null != fclBl ? fclBl.getFileNo() : "";
                note.setModuleRefId(fileNo);
                note.setUpdateDate(new Date());
                note.setUpdatedBy(userName);
                note.setNoteType(NotesConstants.AUTO);
                note.setNoteDesc(message.toString());
                notesBC.saveNotes(note);
            }
            TransactionLedger tarLedger = (TransactionLedger) transactionLedgerDAO.findByCostId(fclBlCostCodes.getCodeId(), fclBl.getBolId());
            if (tarLedger != null && !"Assign".equalsIgnoreCase(tarLedger.getStatus()) && !"I".equalsIgnoreCase(tarLedger.getStatus())) {
                double transactionAmount = new Double(fclBillLaddingForm.getCostAmount().replace(",", ""));
                tarLedger.setTransactionAmt(transactionAmount);
                tarLedger.setBalance(transactionAmount);
                tarLedger.setBalanceInProcess(transactionAmount);
                tarLedger.setCustName(fclBlCostCodes.getAccName());
                tarLedger.setCustNo(fclBlCostCodes.getAccNo());
                tarLedger.setChargeCode(fclBlCostCodes.getCostCode());
                tarLedger.setInvoiceNumber(fclBlCostCodes.getInvoiceNumber());
            }
        }
        return hasUpdated;
//      commented on 4/2/10 gayatri
        // manifestBc.updateTransactionAmount(fclBl.getBolId(), CommonConstants.TRANSACTION_TYPE_ACCRUALS);
    }

    public void updateManifestModifyFlag(String costId, User user) throws Exception {
        FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(Integer.parseInt(costId));
        fclBlCostCodes.setManifestModifyFlag("yes");
        fclBlCostCodes.setProcessedStatus("");
        fclBlCostCodes.setAccrualsUpdatedBy(user.getLoginName());
        fclBlCostCodes.setAccrualsUpdatedDate(new Date());
        fclBlCostCodesDAO.update(fclBlCostCodes);
    }

    public void deleteCostDetails(String costId, String billLaddingNumber, String userName, String ratesNonRates, HttpServletRequest request) throws Exception {
        FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(Integer.parseInt(costId));
        if (null != fclBlCostCodes) {
            TransactionLedger tarLedger = (TransactionLedger) transactionLedgerDAO.findByCostId(fclBlCostCodes.getCodeId(), billLaddingNumber);
            StringBuilder message = new StringBuilder();
            message.append("DELETED ->Cost Code - ").append(fclBlCostCodes.getCostCode()).append(" Cost - ");
            message.append(numberFormat.format(fclBlCostCodes.getAmount())).append(" Currency - ").append(fclBlCostCodes.getCurrencyCode());
            Notes note = new Notes();
            note.setModuleId(NotesConstants.FILE);
            String fileNo = "";
            String bolId = "";
            if (null != fclBlCostCodes.getBolId()) {
                bolId = fclBlCostCodes.getBolId().toString();
                fileNo = fclBlDAO.getFileNo(bolId);
            }
            fclBlCostCodes.setDeleteFlag("yes");
            fclBlCostCodes.setAccrualsUpdatedBy(userName);
            fclBlCostCodes.setAccrualsUpdatedDate(new Date());
            fclBlCostCodes.setManifestModifyFlag("yes");
            fclBlCostCodes.setProcessedStatus("");
            FclBl fclBl = fclBlDAO.findById(fclBlCostCodes.getBolId());
            fclBl.getFclblcostcodes().add(fclBlCostCodes);
            String importFlag = null != fclBl.getImportFlag() ? fclBl.getImportFlag() : "";
            note.setModuleRefId(fileNo);
            note.setUpdateDate(new Date());
            note.setNoteType(NotesConstants.AUTO);
            note.setNoteDesc(message.toString());
            note.setUpdatedBy(userName);
            notesBC.saveNotes(note);
            if (fclBlDAO.isFaeReCalculaionRequiredWhileDelete(fclBlCostCodes.getBolId(), "cost", fclBlCostCodes.getCostCode()) && !"I".equals(importFlag)) {
                FaeReCalculationWhileDelete(fclBlCostCodes.getBolId(), request);
            }
            fclBlDAO.update(fclBl);
            request.setAttribute("hasTransactionType", fclBlCostCodesDAO.hasTransactionType(fclBlCostCodes.getBolId().toString()));
            if ("OCNFRT".equalsIgnoreCase(fclBlCostCodes.getCostCode()) && "on".equalsIgnoreCase(fclBlCostCodes.getReadOnlyFlag()) && "R".equalsIgnoreCase(ratesNonRates)) {
                fclBlCostCodesDAO.updateOFRDeleteFlag(fclBlCostCodes.getBolId());
            } else {
                fclBlCostCodesDAO.updateDeleteFlag(fclBlCostCodes.getCodeId());
            }
            if (null != tarLedger) {
                transactionLedgerDAO.delete(tarLedger);
            }

        }
    }

    public void deleteFlagDetails(FclBillLaddingForm fclBillLaddingForm) throws Exception {
        FclBl fclBl = fclBlDAO.findById(Integer.parseInt(fclBillLaddingForm.getBol()));
        List costList = fclBlCostCodesDAO.findByParentIdforManifest(fclBl.getBol());
        for (Iterator iterator = costList.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (fclBlCostCodes != null) {
                fclBlCostCodesDAO.deleteUpdatedFlag(fclBlCostCodes.getCodeId());
            }
        }
    }

    public void reSetOldAmount(FclBillLaddingForm fclBillLaddingForm) throws Exception {
        double resetAmount = 0.0;
        FclBl fclBl = fclBlDAO.findById(Integer.parseInt(fclBillLaddingForm.getBol()));
        List costList = fclBlCostCodesDAO.findByParentIdforManifest(fclBl.getBol());
        for (Iterator iterator = costList.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (fclBlCostCodes != null) {
                fclBlCostCodesDAO.resetOldAmount(fclBlCostCodes.getCodeId(), resetAmount);
            }
        }
    }

    public Double setBillToParty(String bol, String billToParty) throws Exception {
        Double amount = 0.00;
        if (CommonFunctions.isNotNull(bol)) {
            List chargesList = fclBlChargesDAO.findByParentId(Integer.parseInt(bol));
            for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
                fclBlCharges.setBillTo(billToParty);
                amount += fclBlCharges.getAmount();
                fclBlChargesDAO.update(fclBlCharges);
            }
            // updating billto code
            FclBl fclBl = fclBlDAO.findById(Integer.parseInt(bol));
            if (billToParty != null) {
                if (billToParty.equalsIgnoreCase("Agent")) {
                    fclBl.setBillToCode("A");
                } else if (billToParty.equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
                    fclBl.setBillToCode("C");
                } else if (billToParty.equalsIgnoreCase("Shipper")) {
                    fclBl.setBillToCode("S");
                } else if (billToParty.equalsIgnoreCase("Forwarder")) {
                    fclBl.setBillToCode("F");
                } else if (billToParty.equalsIgnoreCase("ThirdParty")) {
                    fclBl.setBillToCode("T");
                } else if (billToParty.equalsIgnoreCase("NotifyParty")) {
                    fclBl.setBillToCode("N");
                }
            }
        }
        return amount;
    }

    public void getUpdatedBLDetails(FclBillLaddingForm fclBillLaddingForm, HttpServletRequest request, MessageResources messageResources) throws Exception {
        FclBl fclBl = fclBlDAO.findById(Integer.parseInt(fclBillLaddingForm.getBol()));
        //-----GETTING CONTAINER DETAILS--------
        List containerList = getUpdatedContainerList(fclBl.getBol().toString());
        request.setAttribute("fclBlContainerList", containerList);

        //----GETTING CHARGES DETAILS-----
        List chargesList = fclBlChargesDAO.findByParentId(fclBl.getBol());
        String temp = "";
        int j = 0;
        int k = 0;
        boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
            FclBlCharges charges = (FclBlCharges) iterator.next();
            if (charges.getChargeCode() != null && (charges.getChargeCode().equals("OCNFRT") || charges.getChargeCode().equals("OFIMP"))) {
                linkedList.add(k, charges);
            } else {
                linkedList.add(charges);
            }
            j++;
        }
        newList.addAll(linkedList);
        request.setAttribute("addchargeslist", newList);
        List collapseList = consolidateRates(newList, messageResources, importFlag);
        request.setAttribute("consolidatorList", collapseList);

        //----GETTING COSTS DETAILS-----
        List costList = fclBlCostCodesDAO.findByParenId(fclBl.getBol());
        temp = "";
        j = 0;
        k = 0;
        linkedList = new LinkedList();
        newList = new ArrayList();
        for (Iterator iterator = costList.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (fclBlCostCodes.getCostCode() != null && (fclBlCostCodes.getCostCode().equals("OCNFRT") || fclBlCostCodes.getCostCode().equals("OFIMP"))) {
                linkedList.add(k, fclBlCostCodes);
            } else {
                linkedList.add(fclBlCostCodes);
            }
            j++;
        }
        newList.addAll(linkedList);
        request.setAttribute(FclBlConstants.FCLBL_COSTS_LIST, newList);
        List consolidatorCostList = consolidateRatesForCosts(newList, messageResources, fclBl, false, importFlag);
        request.setAttribute("consolidatorCostList", consolidatorCostList);

        //-----ALL FCL COST LIST......
        List manifestedCostList = fclBlCostCodesDAO.getAllFclCosts(fclBl.getBol().toString());
        List newManifestList = new ArrayList();
        //---getting summary or collapsed manifested list---
        newManifestList = consolidateRatesForCosts(manifestedCostList, messageResources, fclBl, false, importFlag);
        request.setAttribute("ManifestedCostList", newManifestList);
        fclBlUtil.calculateChargesAndProfit(fclBl, request, messageResources);
        //---TO SET QUOTEBY AND BOOKINGBY AND THEIR CREATION DATE FOR DISPLAY----
        quotation = getQuoteByFileNo(getFileNumber(fclBl.getFileNo()));
        if (quotation != null) {
            fclBl.setQuoteBy(quotation.getQuoteBy());
            fclBl.setQuoteDate(quotation.getQuoteDate());
        }
        bookingFcl = getBookingByFileNo(getFileNumber(fclBl.getFileNo()));
        if (bookingFcl != null) {
            fclBl.setBookingBy(bookingFcl.getBookedBy());
            fclBl.setBookingDate(bookingFcl.getBookingDate());
        }//---ends---

        //---REQUEST FOR BL OBJECT---
        overPaidStatusforImports(fclBl);
        request.setAttribute(FclBlConstants.FCLBL, fclBl);
    }

    public String updateUserForCommisionOrAccrual(String bolId, String type) throws Exception {
        String returnValue = "";
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (null == loginUser) {
            returnValue = "your session got expired. Please login again";
        } else {
            FclBlDAO fclBlDAO = new FclBlDAO();

            FclBl fclBl = fclBlDAO.findById(Integer.parseInt((null == bolId || bolId.trim().equals("")) ? "0" : bolId.trim()));

            if (null != fclBl) {
                if (null != type && type.trim().equals("commission")) {
                    fclBl.setCommissionsAddedBy(loginUser.getLoginName());
                    fclBl.setCommissionsAddedDate(new Date());
                } else if (null != type && type.trim().equals("accrual")) {
                    fclBl.setAccrualConvertedBy(loginUser.getLoginName());
                    fclBl.setAccrualConvertedDate(new Date());
                }
            }
            Notes notes = new Notes();
            notes.setModuleId("FILE");
            notes.setUpdateDate(new Date());
            notes.setNoteTpye("auto");
            if (null != type && type.trim().equals("commission")) {
                notes.setNoteDesc("Commission added for FclBl " + fclBl.getFileNo());
            } else if (null != type && type.trim().equals("accrual")) {
                notes.setNoteDesc("Accruals added for FclBl " + fclBl.getFileNo());
            }
            notes.setUpdatedBy(loginUser.getLoginName());
            notes.setModuleRefId(fclBl.getFileNo().toString());
            notesDAO.save(notes);
            returnValue = "updated successfully";
        }
        return returnValue;
    }

    public FclBl displayUserForCommisionOrAccrual(String bolId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        FclBlDAO fclBlDAO = new FclBlDAO();
        FclBl fclBl = fclBlDAO.findById(Integer.parseInt((null == bolId || bolId.trim().equals("")) ? "0" : bolId.trim()));
        if (null != fclBl) {
            if (null != fclBl.getCommissionsAddedDate()) {
                fclBl.setCommissionDisplayDate(sdf.format(fclBl.getCommissionsAddedDate()));
            }
            if (null != fclBl.getAccrualConvertedDate()) {
                fclBl.setAccrualDisplayDate(sdf.format(fclBl.getAccrualConvertedDate()));
            }
            return fclBl;
        } else {
            return null;
        }
    }

    public List getSortedList(List tempList) {
        int j = 0;
        int k = 0;
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
            FclBlCharges charges = (FclBlCharges) iterator.next();
            if (charges.getChargeCode() != null && (charges.getChargeCode().equals("OCNFRT") || charges.getChargeCode().equals("OFIMP")) && CommonUtils.isNotEmpty(charges.getReadOnlyFlag())) {
                linkedList.add(k, charges);
            } else {
                linkedList.add(charges);
            }
            j++;
        }
        newList.addAll(linkedList);
        return newList;
    }

    public List consolidateRates(List fclRates, MessageResources messageResources, boolean importFlag) throws Exception {
        List newList = new ArrayList();
        List finalList = new ArrayList();
        LinkedList sortedList = new LinkedList();
        int k = 0;
        boolean noAutoOfr = true;
        String consolidator = "";
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            if ((fclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                    || fclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge")))
                    && CommonUtils.isNotEmpty(fclBlCharges.getReadOnlyFlag())) {
                noAutoOfr = false;
            }
            if (fclBlCharges.getChargeCode() != null && (fclBlCharges.getChargeCode().equals("OCNFRT") || fclBlCharges.getChargeCode().equals("OFIMP"))) {
                sortedList.add(k, fclBlCharges);
                k++;
            } else {
                sortedList.add(fclBlCharges);
            }
        }
        for (Iterator iterator = sortedList.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            if (importFlag == false) {
                consolidator = messageResources.getMessage("OceanFreight");
            } else {
                consolidator = messageResources.getMessage("OceanFreightImports");
            }
            String colsolidatorRates[] = new String[5];
            if (consolidator.indexOf(",") != -1) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = messageResources.getMessage("IntermodelAccrual");
            String interModelRates[] = new String[10];
            if (interModel.indexOf(",") != -1) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            for (int i = 0; i < colsolidatorRates.length; i++) {
                if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().equalsIgnoreCase(colsolidatorRates[i])) {
                    if (fclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge")) || fclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge"))) {
                        FclBlCharges charges = new FclBlCharges();
                        PropertyUtils.copyProperties(charges, fclBlCharges);
                        newList.add(charges);
                        flag = true;
                    } else {
                        interModelFlag = false;
                        for (int j = 0; j < interModelRates.length; j++) {
                            if (fclBlCharges.getReadOnlyFlag() != null
                                    && fclBlCharges.getChargeCode().equalsIgnoreCase(interModelRates[j])) {
                                interModelFlag = true;
                                break;
                            }
                        }
                        if (interModelFlag) {
                            newList.add(fclBlCharges);
                        }
                        if (!interModelFlag) {
                            newList.add(fclBlCharges);
                            flag = true;
                        }
                    }
                    break;
                }
            }
            if (!flag && !interModelFlag) {
                if (importFlag == false) {
                    if (null != fclBlCharges.getChargeCode() && (fclBlCharges.getChargeCode().equals("DRAY")
                            || fclBlCharges.getChargeCode().equals("INSURE")
                            || fclBlCharges.getChargeCode().equals("PIERPA")
                            || fclBlCharges.getChargeCode().equals("CHASFEE")
                            || (fclBlCharges.getBookingFlag() != null
                            && fclBlCharges.getBookingFlag().equals("new")
                            && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODE)
                            && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODETWO)
                            && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODETHREE))
                            || (fclBlCharges.getReadOnlyFlag() == null
                            || fclBlCharges.getReadOnlyFlag().equals("")))) {
                        newList.add(fclBlCharges);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCharges tempFclBlCharges = (FclBlCharges) itr.next();
                            if ((tempFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                                    || tempFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge")))
                                    && (CommonUtils.isNotEmpty(tempFclBlCharges.getReadOnlyFlag()) || noAutoOfr)) {
                                tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + fclBlCharges.getAmount());
                                tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + fclBlCharges.getOldAmount());
                                tempFclBlCharges.setAdjustment((null != tempFclBlCharges.getAdjustment() ? tempFclBlCharges.getAdjustment() : 0d) + (null != fclBlCharges.getAdjustment() ? fclBlCharges.getAdjustment() : 0d));
                                break;
                            }
                        }
                    }
                } else if (importFlag == true) {
                    if (null != fclBlCharges.getChargeCode() && (fclBlCharges.getChargeCode().equals("INSURE")) || (fclBlCharges.getBookingFlag() != null && fclBlCharges.getBookingFlag().equals("new")) || (fclBlCharges.getReadOnlyFlag() == null
                            || fclBlCharges.getReadOnlyFlag().equals(""))) {
                        newList.add(fclBlCharges);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCharges tempFclBlCharges = (FclBlCharges) itr.next();
                            if ((tempFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                                    || tempFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge")))
                                    && (CommonUtils.isNotEmpty(tempFclBlCharges.getReadOnlyFlag()) || noAutoOfr)) {
                                tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + fclBlCharges.getAmount());
                                tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + fclBlCharges.getOldAmount());
                                tempFclBlCharges.setAdjustment((null != tempFclBlCharges.getAdjustment() ? tempFclBlCharges.getAdjustment() : 0d) + (null != fclBlCharges.getAdjustment() ? fclBlCharges.getAdjustment() : 0d));
                                break;
                            }
                        }
                    }
                }
            }
        }
        k = 0;
        LinkedList linkedList = new LinkedList();
        for (Iterator iter = newList.iterator(); iter.hasNext();) {
            FclBlCharges chargeCode = (FclBlCharges) iter.next();
            if (chargeCode.getChargeCode() != null && (chargeCode.getChargeCode().equals("OCNFRT") || chargeCode.getChargeCode().equals("OFIMP"))) {
                linkedList.add(k, chargeCode);
                k++;
            } else {
                linkedList.add(chargeCode);
            }
        }
        finalList.addAll(linkedList);
        return finalList;
    }

    public List consolidateRatesForCosts(List fclRates, MessageResources messageResources, FclBl fclBl, boolean hasMasterBlChanged, boolean importFlag) throws Exception {
        List list = new ArrayList();
        List newList = new ArrayList();
        List finalList = new ArrayList();
        String consolidator = "";
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            list.add(fclBlCostCodes);
        }

        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (importFlag == false) {
                consolidator = messageResources.getMessage("OceanFreight");
            } else if (importFlag == true) {
                consolidator = messageResources.getMessage("OceanFreightImports");
            }
            String colsolidatorRates[] = new String[5];
            if (consolidator.indexOf(",") != -1) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = messageResources.getMessage("IntermodelAccrual");
            String interModelRates[] = new String[10];
            if (interModel.indexOf(",") != -1) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            for (int i = 0; i < colsolidatorRates.length; i++) {
                if (fclBlCostCodes.getCostCode().equalsIgnoreCase(colsolidatorRates[i])) {
                    if (fclBlCostCodes.getCostCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge")) || fclBlCostCodes.getCostCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge"))) {
                        FclBlCostCodes costcodes = new FclBlCostCodes();
                        PropertyUtils.copyProperties(costcodes, fclBlCostCodes);
                        newList.add(costcodes);
                        flag = true;
                    } else {
                        newList.add(fclBlCostCodes);
                        flag = true;
                    }
                    break;
                }
            }
            if (!flag && !interModelFlag) {
                if (importFlag == false) {
                    if ((fclBlCostCodes.getCostCode().equals("DRAY")
                            || fclBlCostCodes.getCostCode().equals("INSURE")
                            || fclBlCostCodes.getCostCode().equals("PIERPA")
                            || fclBlCostCodes.getCostCode().equals("CHASFEE")
                            || fclBlCostCodes.getCostCode().equals("005")
                            || fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODE)
                            || fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODETWO)
                            || fclBlCostCodes.getCostCode().equals("DEFER")
                            || fclBlCostCodes.getCostCode().startsWith("FAE")
                            || (fclBlCostCodes.getBookingFlag() != null && fclBlCostCodes.getBookingFlag().equals("new"))
                            || (fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals("")))) {
                        if ("DEFER".equals(fclBlCostCodes.getCostCode())
                                && CommonUtils.isNotEmpty(fclBl.getNewMasterBL()) && "M".equalsIgnoreCase(fclBl.getReadyToPost()) && hasMasterBlChanged) {
                            fclBlCostCodes.setInvoiceNumber(fclBl.getNewMasterBL());
                            TransactionLedger tarLedger = (TransactionLedger) transactionLedgerDAO.findByCostId(fclBlCostCodes.getCodeId(), fclBl.getBolId());
                            if (tarLedger != null && tarLedger.getStatus() != null && tarLedger.getStatus().equalsIgnoreCase("open")) {
                                tarLedger.setInvoiceNumber(fclBl.getNewMasterBL());
                            }
                        }
                        newList.add(fclBlCostCodes);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCostCodes tempFclBlCostCodes = (FclBlCostCodes) itr.next();
                            if ((tempFclBlCostCodes.getCostCode().equals(messageResources.getMessage("oceanfreightcharge"))
                                    || tempFclBlCostCodes.getCostCode().equals(messageResources.getMessage("oceanfreightImpcharge")))
                                    && null != tempFclBlCostCodes.getReadOnlyFlag() && tempFclBlCostCodes.getReadOnlyFlag().equals("on")) {
                                tempFclBlCostCodes.setAmount(tempFclBlCostCodes.getAmount() + fclBlCostCodes.getAmount());
                                break;
                            }
                        }
                    }
                } else if (importFlag == true) {
                    if ((fclBlCostCodes.getBookingFlag() != null && fclBlCostCodes.getBookingFlag().equals("new"))
                            || (fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals("")) || (fclBlCostCodes.getCostCode().equals("INSURE"))) {
                        if ("DEFER".equals(fclBlCostCodes.getCostCode())
                                && CommonUtils.isNotEmpty(fclBl.getNewMasterBL()) && "M".equalsIgnoreCase(fclBl.getReadyToPost()) && hasMasterBlChanged) {
                            fclBlCostCodes.setInvoiceNumber(fclBl.getNewMasterBL());
                            TransactionLedger tarLedger = (TransactionLedger) transactionLedgerDAO.findByCostId(fclBlCostCodes.getCodeId(), fclBl.getBolId());
                            if (tarLedger != null && tarLedger.getStatus() != null && tarLedger.getStatus().equalsIgnoreCase("open")) {
                                tarLedger.setInvoiceNumber(fclBl.getNewMasterBL());
                            }
                        }
                        newList.add(fclBlCostCodes);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCostCodes tempFclBlCostCodes = (FclBlCostCodes) itr.next();
                            if ((tempFclBlCostCodes.getCostCode().equals(messageResources.getMessage("oceanfreightcharge"))
                                    || tempFclBlCostCodes.getCostCode().equals(messageResources.getMessage("oceanfreightImpcharge")))
                                    && null != tempFclBlCostCodes.getReadOnlyFlag() && tempFclBlCostCodes.getReadOnlyFlag().equals("on")) {
                                tempFclBlCostCodes.setAmount(tempFclBlCostCodes.getAmount() + fclBlCostCodes.getAmount());
                                break;
                            }
                        }
                    }
                }
            }
        }

        int k = 0;
        LinkedList linkedList = new LinkedList();
        for (Iterator iter = newList.iterator(); iter.hasNext();) {
            FclBlCostCodes costCodes = (FclBlCostCodes) iter.next();
            if (costCodes.getCostCode() != null && (costCodes.getCostCode().equals("OCNFRT") || costCodes.getCostCode().equals("OFIMP"))) {
                linkedList.add(k, costCodes);
                k++;
            } else {
                linkedList.add(costCodes);
            }
        }
        finalList.addAll(linkedList);
        return finalList;
    }

    public FclBl getShipperNameAndAdressFromSystemRuleTableForBL(MessageResources messageResources, FclBl fclBl) throws Exception {
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        fclBl.setHouseShipperName(systemRulesDAO.getSystemRulesByCode(messageResources.getMessage("shipperName")));
        fclBl.setHouseShipper("");
        fclBl.setHouseShipperAddress(systemRulesDAO.getSystemRulesByCode(messageResources.getMessage("shipperAddress")));
        return fclBl;
    }

    public void disableAes(String fileNo, String id, String comment, User user, HttpServletRequest request) throws Exception {
        sedFilings = new SedFilingsDAO().findById(Integer.parseInt(id));
        if (null != sedFilings) {
            sedFilings.setAesDisabledFlag("D");
            Date date = new Date();
            String changedDate = sdf1.format(date);
            int k = comment.indexOf("(");
            if (k != -1) {
                comment = comment.substring(0, k);
            }
            createFlatFile(sedFilings.getTrnref(), fileNo, "disableAes", request);
            sedFilings.setAesComment(comment + "(" + user.getLoginName() + "," + changedDate + ").");
            sedFilingsDAO.update(sedFilings);
        }
    }

    public void enableAes(String fileNo, String id, String comment, User user, HttpServletRequest request) throws Exception {
        sedFilings = new SedFilingsDAO().findById(Integer.parseInt(id));
        if (null != sedFilings) {
            sedFilings.setAesDisabledFlag("E");
            Date date = new Date();
            String changedDate = sdf1.format(date);
            int k = comment.indexOf("(");
            if (k != -1) {
                comment = comment.substring(0, k);
            }
            createFlatFile(sedFilings.getTrnref(), fileNo, "enableAes", request);
            sedFilings.setAesComment(comment + "(" + user.getLoginName() + "," + changedDate + ").");
            sedFilingsDAO.update(sedFilings);
        }
    }

    public void deductChargesAndCostAmountOnContainerDisabled(String fileNo, String bolId, String containerId,
            String sizeOfContainer, String comment, User user) throws Exception {
        int i = fileNo.indexOf("-");
        if (i != -1) {
            fileNo = fileNo.substring(0, i);
        }

        BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
        BookingfclUnits bookingfclUnits = new BookingfclUnits();
        Double amount = 0.0;
        Double oldAmount = 0.0;
        Integer conatinerNumber = 0;
        HashMap bookingMap = new HashMap();
        Date date = new Date();
        //--get container object to set the disable flag-----
        FclBlContainer fclBlContainer = fclBlContainerDAO.findById(Integer.parseInt(containerId));
        if (fclBlContainer.getSizeLegend() != null && fclBlContainer.getSizeLegend().getCodedesc().equalsIgnoreCase("A=20")) {
            conatinerNumber = 75;
        } else {
            conatinerNumber = 100;
        }
        if (null != fclBlContainer) {
            fclBlContainer.setDisabledFlag("D");

            String changedDate = sdf1.format(date);
            int k = comment.indexOf("(");
            if (k != -1) {
                comment = comment.substring(0, k);
            } else {
                comment = comment;
            }
            fclBlContainer.setContainerComments(comment + "(" + user.getLoginName() + "," + changedDate + ").");
            fclBlContainerDAO.update(fclBlContainer);
        }
        Notes notes = new Notes();
        StringBuilder message = new StringBuilder();
        message.append(fclBlContainer.getTrailerNo()).append(" Container is Disabled ");
        notes.setModuleId(NotesConstants.FILE);
        notes.setModuleRefId(fileNo);
        notes.setNoteType(NotesConstants.AUTO);
        notes.setUpdateDate(date);
        notes.setUpdatedBy(user.getLoginName());
        notes.setNoteDesc(message.toString());
        new NotesBC().saveNotes(notes);
        //----getting charges from Bookingfclunits based on container size----
//        BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(fileNo);
//        if (null != bookingFcl) {
//            List bookingChargesList = bookingfclUnitsDAO.getChargesBasedOnContainerSize(bookingFcl.getBookingNumber(), sizeOfContainer);
//            if (bookingChargesList.size() > 0) {
//                Iterator iter = bookingChargesList.iterator();
//                while (iter.hasNext()) {
//                    bookingfclUnits = (BookingfclUnits) iter.next();
//                    bookingMap.put(bookingfclUnits.getChgCode(), bookingfclUnits);
//                }
//            }
//        }
//        //-----get charges from fclblcharges----------
//        List blChargesList = fclBlChargesDAO.findByParentId(Integer.parseInt(bolId));
//        if (blChargesList.size() > 0) {
//            Iterator iter = blChargesList.iterator();
//            while (iter.hasNext()) {
//                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
//                String chargeCode = fclBlCharges.getCharges();
//                //---deduct booking amount from bl amount if the charge code exist for same container size-----
//                if (bookingMap.containsKey(chargeCode)) {
//                    BookingfclUnits bookingUnitsForCharges = (BookingfclUnits) bookingMap.get(chargeCode);
//                    //--amount is not deducted if the container is added in bl i.e manually added container---
//                    if (null != fclBlContainer.getManuallyAddedFlag() && fclBlContainer.getManuallyAddedFlag().equalsIgnoreCase("M")) {
//                        amount = fclBlCharges.getAmount();
//                        oldAmount = fclBlCharges.getOldAmount();
//                    } else {
//                        if (!"new".equals(bookingUnitsForCharges.getNewFlag())) {
//                            amount = fclBlCharges.getAmount() - (bookingUnitsForCharges.getAmount() + bookingUnitsForCharges.getMarkUp()
//                                    + bookingUnitsForCharges.getAdjustment());
//                            oldAmount = fclBlCharges.getOldAmount() - (bookingUnitsForCharges.getAmount() + bookingUnitsForCharges.getMarkUp()
//                                    + bookingUnitsForCharges.getAdjustment());
//                        } else {
//                            amount = fclBlCharges.getAmount() - (bookingUnitsForCharges.getMarkUp()
//                                    + bookingUnitsForCharges.getAdjustment());
//                            oldAmount = fclBlCharges.getOldAmount() - (bookingUnitsForCharges.getMarkUp()
//                                    + bookingUnitsForCharges.getAdjustment());
//                        }
//                    }
//                    fclBlCharges.setAmount(amount);
//                    fclBlCharges.setOldAmount(oldAmount);
//                }
//                fclBlChargesDAO.update(fclBlCharges);
//            }
//        }
        //-----get costs from fclblcosts-----------
//        List blcostsList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolId));
//        if (blcostsList.size() > 0) {
//            Iterator iter = blcostsList.iterator();
//            while (iter.hasNext()) {
//                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iter.next();
//                String costCode = fclBlCostCodes.getCostCodeDesc();
//                //---deduct booking amount from bl amount if the cost code exist for same container size-----
//                if (bookingMap.containsKey(costCode)) {
//                    BookingfclUnits bookingUnitsForCost = (BookingfclUnits) bookingMap.get(costCode);
//                    //--amount is not deducted if the container is added in bl i.e manually added container---
//                    if (null != fclBlContainer.getManuallyAddedFlag() && fclBlContainer.getManuallyAddedFlag().equalsIgnoreCase("M")) {
//                        amount = fclBlCostCodes.getAmount();
//                    } else if ((fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals("")) && fclBlCostCodes.getBookingFlag() == null) {
//                        amount = fclBlCostCodes.getAmount();
//                    } else {
//                        amount = fclBlCostCodes.getAmount() - bookingUnitsForCost.getAmount();
//                    }
//                    fclBlCostCodes.setAmount(amount);
//                } else {
//                    if (costCode.equalsIgnoreCase(FclBlConstants.FFCODEDESC)) {
//                        if (fclBlCostCodes.getAmount() != null) {
//                            fclBlCostCodes.setAmount(fclBlCostCodes.getAmount() - conatinerNumber);
//                        }
//                    }
//                }
//                fclBlCostCodes.setTransactionType("");
//                fclBlCostCodes.setManifestModifyFlag("yes");
//                fclBlCostCodes.setProcessedStatus("");
//                fclBlCostCodes.setAccrualsUpdatedBy(user.getLoginName());
//                fclBlCostCodes.setAccrualsUpdatedDate(new Date());
//                fclBlCostCodesDAO.update(fclBlCostCodes);
//            }
//        }
        //delete the transaction_ledger entries while disable the container
//        FclBl fclBl = fclBlDAO.getFileNoObject(fileNo);
//        List transactionLedgerList = transactionLedgerDAO.findByBolId(fclBl.getBolId());
//        if (transactionLedgerList.size() > 0) {
//            Iterator iterator = transactionLedgerList.iterator();
//            while (iterator.hasNext()) {
//                TransactionLedger transactionLedger = (TransactionLedger) iterator.next();
//                transactionLedgerDAO.delete(transactionLedger);
//            }
//        }
    }

    public void addChargesAndCostAmountOnContainerEnabled(String fileNo, String bolId, String containerId,
            String sizeOfContainer, String comment, User user) throws Exception {

        BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
        BookingfclUnits bookingfclUnits = new BookingfclUnits();
        Double amount = 0.0;
        Integer conatinerNumber = null;
        Double oldAmount = 0.0;
        HashMap bookingMap = new HashMap();
        Date date = new Date();

        int i = fileNo.indexOf("-");
        if (i != -1) {
            fileNo = fileNo.substring(0, i);
        } else {
            fileNo = fileNo;
        }

        //--get container object to set the disable flag-----
        FclBlContainer fclBlContainer = fclBlContainerDAO.findById(Integer.parseInt(containerId));
        if (fclBlContainer.getSizeLegend() != null && fclBlContainer.getSizeLegend().getCodedesc().equalsIgnoreCase("A=20")) {
            conatinerNumber = 75;
        } else {
            conatinerNumber = 100;
        }
        if (null != fclBlContainer) {
            fclBlContainer.setDisabledFlag("E");
            //--append last modified login username ,date and time to comments---

            String changedDate = sdf1.format(date);
            int k = comment.indexOf("(");
            if (k != -1) {
                comment = comment.substring(0, k);
            } else {
                comment = comment;
            }
            fclBlContainer.setContainerComments(comment + "(" + user.getLoginName() + "," + changedDate + ").");
            fclBlContainerDAO.update(fclBlContainer);
        }
        Notes notes = new Notes();
        StringBuilder message = new StringBuilder();
        message.append(fclBlContainer.getTrailerNo()).append(" Container is Enabled ");
        notes.setModuleId(NotesConstants.FILE);
        notes.setModuleRefId(fileNo);
        notes.setNoteType(NotesConstants.AUTO);
        notes.setUpdateDate(date);
        notes.setUpdatedBy(user.getLoginName());
        notes.setNoteDesc(message.toString());
        new NotesBC().saveNotes(notes);
        //----getting charges from Bookingfclunits based on container size----
//        BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(fileNo);
//        if (null != bookingFcl) {
//            List bookingChargesList = bookingfclUnitsDAO.getChargesBasedOnContainerSize(bookingFcl.getBookingNumber(), sizeOfContainer);
//            if (bookingChargesList.size() > 0) {
//                Iterator iter = bookingChargesList.iterator();
//                while (iter.hasNext()) {
//                    bookingfclUnits = (BookingfclUnits) iter.next();
//                    bookingMap.put(bookingfclUnits.getChgCode(), bookingfclUnits);
//                }
//            }
//        }
//        //-----get charges from fclblcharges----------
//        List blChargesList = fclBlChargesDAO.findByParentId(Integer.parseInt(bolId));
//        if (blChargesList.size() > 0) {
//            Iterator iter = blChargesList.iterator();
//            while (iter.hasNext()) {
//                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
//                String chargeCode = fclBlCharges.getCharges();
//                //---add booking amount to bl amount if the charge code exist for same container size-----
//                if (bookingMap.containsKey(chargeCode)) {
//                    BookingfclUnits bookingUnitsForCharges = (BookingfclUnits) bookingMap.get(chargeCode);
//
//                    //---amount is not added if the container is manually added in bl----
//                    if (null != fclBlContainer.getManuallyAddedFlag() && fclBlContainer.getManuallyAddedFlag().equalsIgnoreCase("M")) {
//                        amount = fclBlCharges.getAmount();
//                        oldAmount = fclBlCharges.getOldAmount();
//                    } else {
//                        if (!"new".equals(bookingUnitsForCharges.getNewFlag())) {
//                            amount = fclBlCharges.getAmount() + (bookingUnitsForCharges.getAmount() + bookingUnitsForCharges.getMarkUp()
//                                    + bookingUnitsForCharges.getAdjustment());
//                            oldAmount = fclBlCharges.getOldAmount() + (bookingUnitsForCharges.getAmount() + bookingUnitsForCharges.getMarkUp()
//                                    + bookingUnitsForCharges.getAdjustment());
//                        } else {
//                            amount = fclBlCharges.getAmount() + (bookingUnitsForCharges.getMarkUp()
//                                    + bookingUnitsForCharges.getAdjustment());
//                            oldAmount = fclBlCharges.getOldAmount() + (bookingUnitsForCharges.getMarkUp()
//                                    + bookingUnitsForCharges.getAdjustment());
//                        }
//                    }
//                    fclBlCharges.setAmount(amount);
//                    fclBlCharges.setOldAmount(oldAmount);
//                }
//                fclBlChargesDAO.update(fclBlCharges);
//            }
//        }
//        //-----get costs from fclblcosts-----------
//        List blcostsList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolId));
//        if (blcostsList.size() > 0) {
//            Iterator iter = blcostsList.iterator();
//            while (iter.hasNext()) {
//                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iter.next();
//                String costCode = fclBlCostCodes.getCostCodeDesc();
//                //---add booking amount to bl amount if the cost code exist for same container size-----
//                if (bookingMap.containsKey(costCode)) {
//                    BookingfclUnits bookingUnitsForCost = (BookingfclUnits) bookingMap.get(costCode);
//                    //---amount is not added if the container is manually added in bl----
//                    if (null != fclBlContainer.getManuallyAddedFlag() && fclBlContainer.getManuallyAddedFlag().equalsIgnoreCase("M")) {
//                        amount = fclBlCostCodes.getAmount();
//                    } else if ((fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals("")) && fclBlCostCodes.getBookingFlag() == null) {
//                        amount = fclBlCostCodes.getAmount();
//                    } else {
//                        amount = fclBlCostCodes.getAmount() + bookingUnitsForCost.getAmount();
//                    }
//                    fclBlCostCodes.setAmount(amount);
//                } else {
//                    if (costCode.equalsIgnoreCase(FclBlConstants.FFCODEDESC)) {
//                        if (fclBlCostCodes.getAmount() != null) {
//                            fclBlCostCodes.setAmount(fclBlCostCodes.getAmount() + conatinerNumber);
//                        }
//                    }
//                }
//                fclBlCostCodesDAO.update(fclBlCostCodes);
//            }
//        }
    }

    public String getCorrectionsForThisBL(String blNumber, String fclBlId) throws Exception {
        String returnValue = "";
        List correctionList = fclBlCorrectionsDAO.getFclBlCorrectionForTheBLNumbertoDisplay(blNumber);
        if (CommonFunctions.isNotNullOrNotEmpty(correctionList)) {
            returnValue = "Corrections Exist";
        }
        Integer bol = (CommonFunctions.isNotNull(fclBlId)) ? new Integer(fclBlId) : 0;
        FclBl fclBl = getFclBLObject(bol);
        if (null != fclBl && CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclAesDetails())) {
            returnValue += ",AES";
        } else {
            returnValue += ",";
        }
        return returnValue;
    }

    public String getInbondForThisBL(String fclBlId) throws Exception {
        String returnValue = "";
        Integer bol = (CommonFunctions.isNotNull(fclBlId)) ? new Integer(fclBlId) : 0;
        FclBl fclBl = getFclBLObject(bol);
        if (null != fclBl && CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclInbondDetails())) {
            returnValue += "INBOND";
        }
        return returnValue;
    }

    public String getARInvoiceForThisBL(String fileNo, String voyageInternal) throws Exception {
        String returnValue = "";
        ARRedInvoiceForm arRedInvoiceForm = new ARRedInvoiceForm();
        List<Integer> arInvoiceIdList = new ArrayList<Integer>();
        List cfclArinvoiceList = new ArrayList();
        ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
        String groupFileNo = "";
        List invoiceNumberList = arRedInvoiceDAO.findInvoiceNumberByDrNumber(fileNo);
        List<ArRedInvoice> redInvoiceList = new ArrayList<ArRedInvoice>();
        if (CommonUtils.isNotEmpty(voyageInternal)) {
            groupFileNo = new ARRedInvoiceAction().getCfclLinkedDr(voyageInternal);
            arInvoiceIdList = arRedInvoiceDAO.getArInvoiceId(groupFileNo);
            if (!arInvoiceIdList.isEmpty()) {
                cfclArinvoiceList = arRedInvoiceDAO.findLclInvoiceNumberByDrNumber(arInvoiceIdList);
            }
        }
        invoiceNumberList.addAll(cfclArinvoiceList);
        for (int i = 0; i < invoiceNumberList.size(); i++) {
            String invoiceNumber = (String) invoiceNumberList.get(i);
            if (CommonUtils.isNotEmpty(invoiceNumber)) {
                arRedInvoiceForm.setInvoiceNumber(invoiceNumber);
                redInvoiceList.add(new ArRedInvoiceBC().getInvoiceForEditByInvoiceNumber(invoiceNumber));
            }
        }
        for (int i = 0; i < redInvoiceList.size(); i++) {
            ArRedInvoice arRedInvoice = (ArRedInvoice) redInvoiceList.get(i);
            if (null != arRedInvoice.getStatus() && "AR".equals(arRedInvoice.getStatus())) {
                returnValue = "ARINVOICE";
                break;
            }
        }
        return returnValue;
    }

    public String getCorrectionsForThisBLToDisplayColor(String blNumber) throws Exception {
        Integer noticeNo = fclBlCorrectionsDAO.getNoticeNumber(blNumber);
        return (null != noticeNo) ? noticeNo.toString() : null;
    }

    public String getCommentsOfEachContainer(String containerId) throws Exception {
        String comments = null;
        fclBlContainer = fclBlContainerDAO.findById(Integer.parseInt(containerId));
        comments = (null != fclBlContainer) ? fclBlContainer.getContainerComments() : "";
        return comments;
    }

    public String getCommentsOfAes(String Id) throws Exception {
        String comments = null;
        comments = sedFilingsDAO.getCommentsOfAes(Integer.parseInt(Id));
        return comments;
    }

    public String checkForUnitNoInContainerDetailsToManifest(String bol) throws Exception {
        String returnString = "";
        Integer fclblId = (CommonFunctions.isNotNull(bol)) ? new Integer(bol) : 0;
        FclBl fclBl = getFclBLObject(fclblId);
        String importFlag = null != fclBl.getImportFlag() ? fclBl.getImportFlag() : "";
        List containerList = fclBlContainerDAO.getAllContainers(bol);
        if (!"N".equalsIgnoreCase(fclBl.getBreakBulk()) || !"N".equalsIgnoreCase(fclBl.getRatesNonRates())) {
            boolean containerFlag = false;
            if (CommonFunctions.isNotNullOrNotEmpty(containerList)) {
                Iterator iter = containerList.iterator();
                while (iter.hasNext()) {
                    fclBlContainer = (FclBlContainer) iter.next();
                    if (fclBlContainer.getDisabledFlag() == null || !fclBlContainer.getDisabledFlag().equalsIgnoreCase("D")) {
                        containerFlag = true;
                        if (!CommonFunctions.isNotNull(fclBlContainer.getTrailerNo())) {
                            returnString = returnString + "-->" + "Please Enter Unit Number for all containers" + "<br>";
                            break;
                        }
                        if (!CommonFunctions.isNotNull(fclBlContainer.getSealNo())) {
                            returnString = returnString + "-->" + "Please Enter Seal Number for all containers" + "<br>";
                            break;
                        }
                        if (!CommonFunctions.isNotNullOrNotEmpty(fclBlContainer.getFclBlMarks())) {
                            returnString = returnString + "-->" + "Please Enter Pkg info for container " + fclBlContainer.getTrailerNo() + "<br>";
                            break;
                        } else {
                            List<FclBlMarks> list = new ArrayList(fclBlContainer.getFclBlMarks());
                            for (FclBlMarks fclBlMarks : list) {
                                if (!CommonFunctions.isNotNull(fclBlMarks.getDescPckgs())) {
                                    returnString = returnString + "-->" + "Please Enter BL Description in PKG Info for container " + fclBlContainer.getTrailerNo() + "<br>";
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                returnString = returnString + "-->" + "Please Enter Containers details" + "<br>";
                containerFlag = true;
            }
            if (!containerFlag) {
                returnString = returnString + "-->" + "All Containers Disabled" + "<br>";
            }
        }
        if ((fclBl != null && !fclBl.getFileNo().contains(FclBlConstants.EQUALDELIMITER))
                && !CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclAesDetails()) && !"I".equals(importFlag)) {
            returnString = returnString + "-->" + "Please Enter AES/ITN " + "<br>";
        }
        if (CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclblcostcodes())) {
            List<FclBlCostCodes> list = new ArrayList(fclBl.getFclblcostcodes());
            for (FclBlCostCodes fclBlCostCodes : list) {
                if (!CommonFunctions.isNotNull(fclBlCostCodes.getAccNo()) && !"Yes".equalsIgnoreCase(fclBlCostCodes.getDeleteFlag())) {
                    returnString = returnString + "-->" + "Please Enter Vendor" + "<br>";
                }
            }
        }
        /*else{
         returnString = returnString+"-->"+"Please enter at least one cost code"+"<br>";// for import
         }*/
        if ((fclBl != null && !fclBl.getFileNo().contains(FclBlConstants.EQUALDELIMITER) && !"I".equals(importFlag))
                && !CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclcharge())) {
            returnString = returnString + "-->" + "Please Enter at least one Charge Code" + "<br>";// for import
        }
        String hazMat = getUnassignedHazmatList(fclBl.getBol().toString());
        if (CommonFunctions.isNotNull(hazMat)) {
            returnString = returnString + "-->" + "Cannot Proceed until all Hazardous Cargo is assigned to Container" + "<br>";
        }

        return returnString;
    }

    public String checkForUnitNoInContainerDetailsToMultipleBl(String bol) throws Exception {
        String returnString = "";
        Integer fclblId = (CommonFunctions.isNotNull(bol)) ? new Integer(bol) : 0;
        FclBl fclBl = getFclBLObject(fclblId);
        List containerList = fclBlContainerDAO.getAllContainers(bol);
        String breakBulk = new BookingFclDAO().getBreakBulk(fclBl.getFileNo());
        if (!"Y".equalsIgnoreCase(breakBulk)) {
            boolean containerFlag = false;
            if (CommonFunctions.isNotNullOrNotEmpty(containerList)) {
                Iterator iter = containerList.iterator();
                while (iter.hasNext()) {
                    fclBlContainer = (FclBlContainer) iter.next();
                    if (fclBlContainer.getDisabledFlag() == null || !fclBlContainer.getDisabledFlag().equalsIgnoreCase("D")) {
                        containerFlag = true;
                        if (!CommonFunctions.isNotNull(fclBlContainer.getTrailerNo())) {
                            returnString = returnString + "-->" + "Please Enter Unit Number for all containers" + "<br>";
                            break;
                        }
                    }
                }
            } else {
                returnString = returnString + "-->" + "Please Enter Containers details" + "<br>";
                containerFlag = true;
            }
            if (!containerFlag) {
                returnString = returnString + "-->" + "All Containers Disabled" + "<br>";
            }
        }
        return returnString;
    }

    public boolean checkIfCostCodeAlreadyExists(String bol, String codeId, String costCode, String vendorAccNo) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        boolean returnString = false;
        String interModel = "INTMDL,INTFS,INTRAMP";
        String interModelRates[] = new String[10];
        String flag = "false";
        if (interModel.indexOf(",") != -1) {
            interModelRates = interModel.split(",");
        } else {
            interModelRates[0] = interModel;
        }
        for (int j = 0; j < interModelRates.length; j++) {
            if (costCode.equalsIgnoreCase(interModelRates[j])) {
                flag = "true";
            }
        }
        if (CommonUtils.isNotEmpty(fclBlCostCodesDAO.getRecordWithThisCostCode(bol, costCode, vendorAccNo, codeId))) {
            returnString = true;
        }
        return returnString;
    }

    public boolean checkIfInvoiceAlreadyExists(String vendor, String costCode, String invoiceNumber) throws Exception {
        List recordList = fclBlCostCodesDAO.checkInvoiceNumberDuplicate(vendor, costCode, invoiceNumber);
        if (CommonUtils.isNotEmpty(recordList)) {
            return true;
        }
        return false;
    }

    /**
     * @param fbl
     * @param fclBlChargeOrCostCodeList adding into TransactionLedgerr
     */
    public List<FclBlChargeBean> getFclBlCostBeanobject(List<FclBlCharges> fclBlChargesList,
            List<FclBlCostCodes> fclBlCostCodeList, List<FclBlCorrections> blCorrectionsList, Date date, String UserName) throws Exception {
        List<FclBlChargeBean> returnList = new ArrayList<FclBlChargeBean>();
        if (fclBlChargesList != null) {
            for (FclBlCharges fclBlCharges : fclBlChargesList) {
                FclBlChargeBean fclbean = new FclBlChargeBean();
                if (fclBlCharges.getBolId() != null) {
                    fclbean.setBillofLaddingNo(fclBlCharges.getBolId().toString());
                }
                fclbean.setBillTo(fclBlCharges.getBillTo());
                fclbean.setChargeAmt(String.valueOf(fclBlCharges.getAmount()));
                fclbean.setChargeCode(fclBlCharges.getChargeCode());
                fclbean.setCurrencyCode(fclBlCharges.getCurrencyCode());
                fclbean.setThirdPartyName(fclBlCharges.getThirdPartyName());
                fclbean.setThirdPartyNo(fclBlCharges.getBillTrdPrty());
                fclbean.setReadyToPost(fclBlCharges.getReadyToPost());
                fclbean.setReadyToPost(fclBlCharges.getReadyToPost());
                fclbean.setComment(fclBlCharges.getChargesRemarks());
                fclbean.setChargeId(fclBlCharges.getChargesId());
                returnList.add(fclbean);

            }
        } else if (fclBlCostCodeList != null) {
            for (FclBlCostCodes fclBlCostCodes : fclBlCostCodeList) {
                FclBlChargeBean fclbean = new FclBlChargeBean();
                if (fclBlCostCodes.getBolId() != null) {
                    fclbean.setBillofLaddingNo(fclBlCostCodes.getBolId().toString());
                }
                fclbean.setChargeAmt(String.valueOf(fclBlCostCodes.getAmount()));
                fclbean.setChargeCode(fclBlCostCodes.getCostCode());
                fclbean.setCurrencyCode(fclBlCostCodes.getCurrencyCode());
                fclbean.setAcctName(fclBlCostCodes.getAccName());
                fclbean.setAcctNo(fclBlCostCodes.getAccNo());
                fclbean.setReadyToPost(fclBlCostCodes.getReadyToPost());
                fclbean.setChargeId(fclBlCostCodes.getCodeId());
                fclbean.setBillTo(null);
                fclbean.setCostId(fclBlCostCodes.getCodeId());
                fclbean.setInvoiceNumber(fclBlCostCodes.getInvoiceNumber());
                fclbean.setComment(fclBlCostCodes.getCostComments());
                returnList.add(fclbean);
            }
        } else if (blCorrectionsList != null) {
            FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
            for (FclBlCorrections fclBlCorrections : blCorrectionsList) {
                FclBlChargeBean fclbean = new FclBlChargeBean();
                fclbean.setBillofLaddingNo(fclBlCorrections.getBlNumber());
                fclbean.setChargeAmt(String.valueOf(fclBlCorrections.getDiffereceAmount()));
                fclbean.setChargeCode(fclBlCorrections.getChargeCode());
                fclbean.setCurrencyCode("USD");
                fclbean.setReadyToPost("M");
                fclbean.setThirdPartyName(fclBlCorrections.getThirdParty());
                // set to set third bllto party name
                fclbean.setBillTo(fclBlCorrections.getBillToParty());
                fclbean.setChargeId(fclBlCorrections.getId());
                fclbean.setAcctName(fclBlCorrections.getAccountName());
                fclbean.setAcctNo(fclBlCorrections.getAccountNumber());
                returnList.add(fclbean);
                if ("Y".equalsIgnoreCase(fclBlCorrections.getCorrectionType().getCode())
                        && CommonUtils.isNotEmpty(fclBlCorrections.getOriginalAmountCorrectionTypeY())
                        && !fclBlCorrections.getBillToParty().equalsIgnoreCase(fclBlCorrections.getOriginalBillToPartyCorrectionTypeY())) {
                    fclbean = new FclBlChargeBean();
                    fclbean.setBillofLaddingNo(fclBlCorrections.getBlNumber());
                    fclbean.setChargeAmt(String.valueOf(fclBlCorrections.getOriginalAmountCorrectionTypeY()));
                    fclbean.setChargeCode(fclBlCorrections.getChargeCode());
                    fclbean.setCurrencyCode("USD");
                    fclbean.setReadyToPost("M");
                    fclbean.setThirdPartyName(fclBlCorrections.getThirdParty());
                    // set to set third bllto party name
                    fclbean.setBillTo(fclBlCorrections.getBillToParty());
                    fclbean.setChargeId(fclBlCorrections.getId());
                    fclbean.setAcctName(fclBlCorrections.getAccountName());
                    fclbean.setAcctNo(fclBlCorrections.getAccountNumber());
                    returnList.add(fclbean);
                    // Add Minus amount for original party
                    fclbean = new FclBlChargeBean();
                    fclbean.setBillofLaddingNo(fclBlCorrections.getBlNumber());
                    fclbean.setChargeAmt(String.valueOf(0 - fclBlCorrections.getOriginalAmountCorrectionTypeY()));
                    fclbean.setChargeCode(fclBlCorrections.getChargeCode());
                    fclbean.setCurrencyCode("USD");
                    fclbean.setReadyToPost("M");
                    fclbean.setThirdPartyName(fclBlCorrections.getThirdParty());
                    // set to set third bllto party name
                    fclbean.setBillTo(fclBlCorrections.getOriginalBillToPartyCorrectionTypeY());
                    fclbean.setChargeId(fclBlCorrections.getId());
                    fclbean.setAcctName(fclBlCorrections.getOriginalCustomerNameCorrectionTypeY());
                    fclbean.setAcctNo(fclBlCorrections.getOriginalCustomerNumberCorrectionTypeY());
                    returnList.add(fclbean);
                }
                fclBlCorrections.setManifest("M");
                fclBlCorrections.setWhoPosted(UserName);
                fclBlCorrections.setPostedDate(date);
                fclBlCorrectionsDAO.save(fclBlCorrections);
            }
        }
        return returnList;
    }
    // getting list of void Bl from bl_void table

    public void getListVoidBlCost(String billLaddingNo, HttpServletRequest request) throws Exception {
        BlVoidDAO blVoidDAO = new BlVoidDAO();
        request.setAttribute(FclBlConstants.BLVOIDLIST, blVoidDAO.findByProperty("billLaddingNo",
                billLaddingNo));
    }

    public double multipleConatinerByInputValue(List<FclBlContainer> costCodesList, Double firstAmount, Double secondAmount) throws Exception {
        double amount = 0;
        if (CommonFunctions.isNotNullOrNotEmpty(costCodesList)) {
            for (FclBlContainer fclBlContainer : costCodesList) {
                if (fclBlContainer.getSizeLegend() != null && fclBlContainer.getSizeLegend().getId().equals(FclBlConstants.FIRSTUNITTYPEID)) {
                    amount += firstAmount;
                } else {
                    amount += secondAmount;
                }
            }
        }

        return amount;

    }
    // calculateFAE......

    public void calculateFAE(FclBillLaddingForm fclBillLaddingform, MessageResources messageResources, HttpServletRequest request) throws Exception {
        FclBl fclBl = null;
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        PortsDAO portsDAO = new PortsDAO();
        Integer portId = 0;
        FclBlCharges fclBlCharges = new FclBlCharges();
        if (fclBillLaddingform.getBol() != null && !fclBillLaddingform.getBol().equals("")) {
            fclBl = fclBlDAO.findById(new Integer(fclBillLaddingform.getBol()));
            if (CommonUtils.notMatches(fclBl.getFileNo(), "(\\d+)-([a-zA-Z])")) {
                //NOW FEA calculation...
                // if(fclBillLaddingform.getTempFAECostCOde()==null || fclBillLaddingform.getTempFAECostCOde().trim().equals("")){
                String cityName = null;
                if (fclBl.getPortofDischarge() != null) {
                    StringFormatter stringFormatter = new StringFormatter();
                    cityName = stringFormatter.getBreketValue(fclBl.getFinalDestination());
                }
                List<Ports> portList = portsDAO.findPortUsingUnlocaCode(cityName);
                if (portList != null && !portList.isEmpty()) {
                    Ports ports = portList.get(0);
                    portId = ports.getId();
                }
                if (!calculateValueUsingAgentRules(fclBl, fclBillLaddingform, portId, request)) {
                    calculateValueUsingPortsRules(portList, fclBl, cityName, fclBillLaddingform, request);
                }
            }
        }
    }

    public boolean calculateValueUsingAgentRules(FclBl fclBl, FclBillLaddingForm fclBillLaddingForm, Integer portId, HttpServletRequest request) throws Exception {
        boolean returnFlag = false;
        PortsDAO portsDAO = new PortsDAO();
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        Double calculateAmount = null;
        Double calculateAdminAmount = null;
        String accountDetail = fclBillLaddingForm.getRoutedByAgent();
        if (fclBillLaddingForm.getRoutedByAgent() != null && !fclBillLaddingForm.getRoutedByAgent().equals("")) {
            TradingPartner tradingPartner = tradingPartnerBC.findAccountNumberByPassingAccountName(accountDetail);
            accountDetail = (tradingPartner != null) ? tradingPartner.getAccountno() : accountDetail;
        } else if (fclBillLaddingForm.getAgentNo() != null && !fclBillLaddingForm.getAgentNo().equals("")) {
            accountDetail = fclBillLaddingForm.getAgentNo();
        }
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List<AgencyInfo> agentList = portsDAO.getAgentInfo(accountDetail, portId);

        if (agentList != null && !agentList.isEmpty()) {
            AgencyInfo agencyInfo = (AgencyInfo) agentList.get(0);
            if (agencyInfo.getAgencyRules() != null) {
                List<AgencyRules> agencyRulesList = (agencyInfo.getAgencyRules() != null) ? new ArrayList(agencyInfo.getAgencyRules())
                        : Collections.EMPTY_LIST;
                for (AgencyRules agencyRules : agencyRulesList) {
                    boolean flag = false;
                    if (fclBillLaddingForm.getRoutedByAgent() != null && !fclBillLaddingForm.getRoutedByAgent().equals("")) {
                        if (agencyRules.getRouteAgtAdminRule() != null && !agencyRules.getRouteAgtAdminRule().equals("0")) {
                            calculateAdminAmount = getNumberValue(genericCodeDAO.findById(new Integer(agencyRules.getRouteAgtAdminRule().trim())), fclBl, agencyRules.getRouteAgtAdminAmt(), agencyRules.getRouteAgtAdminTieramt(), null);
                            returnFlag = true;
                        }
                        if (agencyRules.getRouteAgtCommnRule() != null && !agencyRules.getRouteAgtCommnRule().equals("0")) {
                            Double differenceOfCostAndCharges = (fclBillLaddingForm.getDifferenceOfCostAndCharges() != null && !fclBillLaddingForm.getDifferenceOfCostAndCharges().equals(""))
                                    ? new Double(fclBillLaddingForm.getDifferenceOfCostAndCharges().replace(",", "")) : 0.0d;
                            Double fAECost = (fclBillLaddingForm.getAdminCost() != null && !fclBillLaddingForm.getAdminCost().equals(""))
                                    ? new Double(fclBillLaddingForm.getAdminCost().replace(",", "")) : 0.0d;
                            differenceOfCostAndCharges = differenceOfCostAndCharges + fAECost;
                            if (calculateAdminAmount != null) {
                                differenceOfCostAndCharges = differenceOfCostAndCharges - calculateAdminAmount;
                            }

                            GenericCode genericCode = genericCodeDAO.findById(new Integer(agencyRules.getRouteAgtCommnRule().trim()));
                            flag = (genericCode != null && genericCode.getCodedesc().equalsIgnoreCase(FclBlConstants.PERCENTAGERUEL)) ? true : false;// to cheack percentage rule

                            calculateAmount = getNumberValue(genericCode, fclBl, agencyRules.getRouteAgtCommnAmt(), agencyRules.getRouteAgtCommnTieramt(), differenceOfCostAndCharges);
                            if (calculateAmount != null) {
                                calculateAmount = (!flag && null != calculateAdminAmount) ? calculateAmount - calculateAdminAmount
                                        : calculateAmount;
                                getAdminAndCommCostToFclBlCost(calculateAmount, fclBillLaddingForm, fclBl, FclBlConstants.ROUTEDFORCOMM, request);
                            }
                            returnFlag = true;
                        }
                    } else {
                        if (agencyRules.getNotRouteAgtAdminRule() != null && !agencyRules.getNotRouteAgtAdminRule().equals("0")) {
                            calculateAdminAmount = getNumberValue(genericCodeDAO.findById(new Integer(agencyRules.getNotRouteAgtAdminRule())), fclBl, agencyRules.getNotRouteAgtAdminAmt(), agencyRules.getNotRouteAgtAdminTieramt(), null);
                            returnFlag = true;
                        }
                        if (agencyRules.getNotRouteAgtCommnRule() != null && !agencyRules.getNotRouteAgtCommnRule().equals("0")) {
                            Double differenceOfCostAndCharges = (fclBillLaddingForm.getDifferenceOfCostAndCharges() != null && !fclBillLaddingForm.getDifferenceOfCostAndCharges().equals(""))
                                    ? new Double(fclBillLaddingForm.getDifferenceOfCostAndCharges().replace(",", "")) : 0.0d;
                            Double fAECost = (fclBillLaddingForm.getAdminCost() != null && !fclBillLaddingForm.getAdminCost().equals(""))
                                    ? new Double(fclBillLaddingForm.getAdminCost().replace(",", "")) : 0.0d;
                            differenceOfCostAndCharges = differenceOfCostAndCharges + fAECost;
                            if (calculateAdminAmount != null) {
                                differenceOfCostAndCharges = differenceOfCostAndCharges - calculateAdminAmount;
                            }
                            GenericCode genericCode = genericCodeDAO.findById(new Integer(agencyRules.getNotRouteAgtCommnRule().trim()));
                            flag = (genericCode != null && genericCode.getCodedesc().equalsIgnoreCase(FclBlConstants.PERCENTAGERUEL)) ? true : false;// to cheack percentage rule
                            calculateAmount = getNumberValue(genericCode,
                                    fclBl, agencyRules.getNotRouteAgtCommnAmt(), agencyRules.getNotRouteAgtCommnTieramt(), differenceOfCostAndCharges);

                            if (calculateAmount != null) {
                                calculateAmount = (!flag && null != calculateAdminAmount) ? calculateAmount - calculateAdminAmount : calculateAmount;
                                getAdminAndCommCostToFclBlCost(calculateAmount, fclBillLaddingForm, fclBl, FclBlConstants.AGENTFORCOMM, request);
                            }
                            returnFlag = true;
                        }
                    }
                }
            }
        }
        return returnFlag;
    }

    public Double getNumberValue(GenericCode genericCode, FclBl fclBl, Double percentageCost, Double tiearAmount,
            Double otherAmount) throws Exception {
        percentageCost = (percentageCost == null) ? 0.0d : percentageCost;
        tiearAmount = (tiearAmount == null) ? 0.0d : tiearAmount;
        int numberOfContainer = (fclBl.getFclcontainer() != null) ? fclBl.getFclcontainer().size() : 0;
        Double calculateValue = 0.0;
        if (genericCode != null && genericCode.getCodedesc().equalsIgnoreCase(FclBlConstants.CONTAINERRUEL)) {
            calculateValue = new Double(numberOfContainer * percentageCost);

        } else if (genericCode != null && genericCode.getCodedesc().equalsIgnoreCase(FclBlConstants.PERBLRUEL)) {
            calculateValue = percentageCost;

        } else if (genericCode != null && genericCode.getCodedesc().equalsIgnoreCase(FclBlConstants.MULTICONTAINERRUEL)) {
            List<FclBlContainer> costCodesList = (fclBl.getFclcontainer() != null) ? new ArrayList(fclBl.getFclcontainer()) : Collections.EMPTY_LIST;
            calculateValue = multipleConatinerByInputValue(costCodesList, percentageCost, tiearAmount);
        } else if ((genericCode != null && genericCode.getCodedesc().equalsIgnoreCase(FclBlConstants.PERCENTAGERUEL))
                && otherAmount != null) {
            //calculateValue =  new CommonFunctions().getPercentOf(otherAmount, percentageCost);
            calculateValue = otherAmount * percentageCost;
            calculateValue = +Math.abs(calculateValue);

        }
        return calculateValue;
    }

    public void calculateValueUsingPortsRules(List<Ports> portList, FclBl fclBl, String cityName, FclBillLaddingForm fclBillLaddingForm, HttpServletRequest request) throws Exception {
        Double calculateValue = null;
        Double calculateAdminAmount = null;
        for (Ports ports : portList) {
            List<FCLPortConfiguration> postConfigSetList = (ports.getFclPortConfigSet() != null) ? new ArrayList(ports.getFclPortConfigSet())
                    : Collections.EMPTY_LIST;
            //boolean flag=false;
            Boolean flag = false;
            for (FCLPortConfiguration fConfiguration : postConfigSetList) {

                if (fclBillLaddingForm.getRoutedByAgent() != null && !fclBillLaddingForm.getRoutedByAgent().equals("")) {
                    if (fConfiguration.getRadmRule() != null && !fConfiguration.getRadmRule().equals("0")) {
                        calculateAdminAmount = getNumberValue(fConfiguration.getRadmRule(), fclBl,
                                fConfiguration.getRadmAm(), fConfiguration.getRadmTierAmt(), null);

                    }
                    if (fConfiguration.getRcomRule() != null && !fConfiguration.getRcomRule().equals("0")) {
                        Double differenceOfCostAndCharges = (fclBillLaddingForm.getDifferenceOfCostAndCharges() != null && !fclBillLaddingForm.getDifferenceOfCostAndCharges().equals(""))
                                ? new Double(fclBillLaddingForm.getDifferenceOfCostAndCharges().replace(",", "")) : 0.0d;

                        Double fAECost = (fclBillLaddingForm.getAdminCost() != null && !fclBillLaddingForm.getAdminCost().equals(""))
                                ? new Double(fclBillLaddingForm.getAdminCost().replace(",", "")) : 0.0d;

                        differenceOfCostAndCharges = differenceOfCostAndCharges + fAECost;

                        if (calculateAdminAmount != null) {
                            differenceOfCostAndCharges = differenceOfCostAndCharges - calculateAdminAmount;
                        }
                        calculateValue = getNumberValue(fConfiguration.getRcomRule(), fclBl,
                                fConfiguration.getRcomAm(), fConfiguration.getRcomTierAmt(), differenceOfCostAndCharges);
                        flag = (fConfiguration.getRcomRule().getCodedesc().equalsIgnoreCase(FclBlConstants.PERCENTAGERUEL)) ? true : false;// to cheack percentage rule

                        if (calculateValue != null) {

                            calculateValue = (!flag && calculateAdminAmount != null) ? calculateValue - calculateAdminAmount : calculateValue;
                            getAdminAndCommCostToFclBlCost(calculateValue, fclBillLaddingForm, fclBl, FclBlConstants.ROUTEDFORCOMM, request);
                        }
                    }
                } else {
                    if (fConfiguration.getNadmRule() != null && !fConfiguration.getNadmRule().equals("0")) {
                        flag = (fConfiguration.getRadmRule().getCodedesc().equalsIgnoreCase(FclBlConstants.PERCENTAGERUEL)) ? true : false;// to cheack percentage rule

                        calculateAdminAmount = getNumberValue(fConfiguration.getNadmRule(), fclBl,
                                fConfiguration.getNadmAm(), fConfiguration.getNadmTierAmt(), null);
                    }
                    if (fConfiguration.getNcomRule() != null && !fConfiguration.getNcomRule().equals("0")) {
                        Double differenceOfCostAndCharges = (fclBillLaddingForm.getDifferenceOfCostAndCharges() != null && !fclBillLaddingForm.getDifferenceOfCostAndCharges().equals(""))
                                ? new Double(fclBillLaddingForm.getDifferenceOfCostAndCharges().replace(",", "")) : 0.0d;

                        Double fAECost = (fclBillLaddingForm.getAdminCost() != null && !fclBillLaddingForm.getAdminCost().equals(""))
                                ? new Double(fclBillLaddingForm.getAdminCost().replace(",", "")) : 0.0d;

                        differenceOfCostAndCharges = differenceOfCostAndCharges + fAECost;

                        if (calculateAdminAmount != null) {
                            differenceOfCostAndCharges = differenceOfCostAndCharges - calculateAdminAmount;
                        }
                        calculateValue = getNumberValue(fConfiguration.getNcomRule(), fclBl,
                                fConfiguration.getNcomAm(), fConfiguration.getNcomTierAmt(), differenceOfCostAndCharges);

                        if (calculateValue != null) {
                            calculateValue = (!flag && calculateAdminAmount != null) ? calculateValue - calculateAdminAmount : calculateValue;
                            getAdminAndCommCostToFclBlCost(calculateValue, fclBillLaddingForm, fclBl, FclBlConstants.AGENTFORCOMM, request);
                        }
                    }
                }
            }

        }
    }

    public void getAdminAndCommCostToFclBlCost(Double amount, FclBillLaddingForm fclBillLaddingform,
            FclBl fclBl, String routedOrAgent, HttpServletRequest request) throws Exception {
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        fclBlCostCodes.setAmount(amount);
        fclBlCostCodes.setAccName(fclBillLaddingform.getAgent());
        fclBlCostCodes.setAccNo(fclBillLaddingform.getAgentNo());
        fclBlCostCodes.setCurrencyCode("USD");
        fclBlCostCodes.setCostCode(FclBlConstants.FAECODE);
        fclBlCostCodes.setCostCodeDesc(FclBlConstants.FAECODEDESC);
        if ("M".equalsIgnoreCase(fclBl.getReadyToPost())) {
            fclBlCostCodes.setReadyToPost("M");
            fclBlCostCodes.setManifestModifyFlag("yes");
            fclBlCostCodes.setTransactionType("AC");
            User user = (User) request.getSession().getAttribute("loginuser");
            fclBlCostCodes.setAccrualsUpdatedBy(user.getLoginName());
            fclBlCostCodes.setAccrualsUpdatedDate(new Date());
        }
        if (routedOrAgent != null && routedOrAgent.equalsIgnoreCase(FclBlConstants.ROUTEDFORCOMM)
                || routedOrAgent.equalsIgnoreCase(FclBlConstants.ROUTEDFORADMIN)) {
            if (fclBillLaddingform.getRoutedByAgent() != null && fclBillLaddingform.getRoutedByAgent().trim().equalsIgnoreCase(
                    fclBillLaddingform.getAgentNo().trim())) {
            } else {
                TradingPartner tradingPartner = tradingPartnerBC.findAccountNumberByPassingAccountName(fclBillLaddingform.getRoutedByAgent());
                if (tradingPartner != null) {
                    fclBlCostCodes.setAccName(tradingPartner.getAccountName());
                    fclBlCostCodes.setAccNo(tradingPartner.getAccountno());
                }
            }
        }
        fclBlCostCodes.setBolId(fclBl.getBol());
        fclBlCostCodesDAO.save(fclBlCostCodes);
        notesFAECal(fclBl, amount, fclBlCostCodes, request, "INSERTED");
        findCostCode(fclBl, amount, fclBlCostCodes);

    }

    public void notesFAECal(FclBl fclbl, Double amount, FclBlCostCodes fclBlCostCodes, HttpServletRequest request, String action) throws Exception {
        HttpSession session = request.getSession();
        StringBuilder message = new StringBuilder();
        Date date = new Date();
        User loginUser = (User) session.getAttribute("loginuser");
        String noteDesc = "";
        FclBl fclBl = fclBlDAO.findById(fclBlCostCodes.getBolId());
        if (null != fclBlCostCodes) {
            message.append(action).append(" -> Cost Code - " + FclBlConstants.FAECODEDESC);
            message.append(" Cost - ").append(numberFormat.format(amount));
            message.append(" Vendor Name - ").append(fclBlCostCodes.getAccName());
            message.append(" Vendor Number - ").append(fclBlCostCodes.getAccNo());
            if (CommonUtils.isNotEmpty(fclBlCostCodes.getCostComments())) {
                message.append(" Comment -").append(fclBlCostCodes.getCostComments());
            }
            Notes notes = new Notes();
            notes.setModuleId(NotesConstants.FILE);
            String fileNo = null != fclBl ? fclBl.getFileNo() : "";
            notes.setModuleRefId(fileNo);
            notes.setNoteType(NotesConstants.AUTO);
            notes.setUpdateDate(date);
            notes.setUpdatedBy(loginUser.getLoginName());
            notes.setNoteDesc(message.toString());
            new NotesBC().saveNotes(notes);
        }
    }

    public void notesFAECalForOldamount(FclBl fclbl, Double amount, Double oldAmount, FclBlCostCodes fclBlCostCodes, HttpServletRequest request, String action) throws Exception {
        HttpSession session = request.getSession();
        StringBuilder message = new StringBuilder();
        Date date = new Date();
        User loginUser = (User) session.getAttribute("loginuser");
        FclBl fclBl = fclBlDAO.findById(fclBlCostCodes.getBolId());
        if (null != fclBlCostCodes) {
            message.append(action).append(" -> Cost Code - " + FclBlConstants.FAECODEDESC);
            message.append(" Cost - ").append(numberFormat.format(oldAmount));
            message.append(" to ").append(numberFormat.format(amount));
            message.append(" Vendor Name - ").append(fclBlCostCodes.getAccName());
            message.append(" Vendor Number - ").append(fclBlCostCodes.getAccNo());
            if (CommonUtils.isNotEmpty(fclBlCostCodes.getCostComments())) {
                message.append(" Comment -").append(fclBlCostCodes.getCostComments());
            }
            Notes notes = new Notes();
            notes.setModuleId(NotesConstants.FILE);
            String fileNo = null != fclBl ? fclBl.getFileNo() : "";
            notes.setModuleRefId(fileNo);
            notes.setNoteType(NotesConstants.AUTO);
            notes.setUpdateDate(date);
            notes.setUpdatedBy(loginUser.getLoginName());
            notes.setNoteDesc(message.toString());
            new NotesBC().saveNotes(notes);
        }
    }

    public void findCostCode(FclBl fclBl, double amount, FclBlCostCodes fclBlCostCodes) throws Exception {
        List<FclBlCostCodes> costCodeList = (fclBl.getFclblcostcodes() != null) ? new ArrayList(fclBl.getFclblcostcodes())
                : Collections.EMPTY_LIST;
        boolean flag = true;
        for (FclBlCostCodes fclBlCostCodes2 : costCodeList) {
            if (fclBlCostCodes2.getCostCode() != null && fclBlCostCodes.getCostCode() != null
                    && fclBlCostCodes2.getCostCode().equalsIgnoreCase(fclBlCostCodes.getCostCode())) {
                if (amount == 0.0) {
                    fclBl.getFclblcostcodes().remove(fclBlCostCodes2);
                } else {
                    fclBlCostCodes2.setAmount(amount);
                    if ("M".equalsIgnoreCase(fclBl.getReadyToPost())) {
                        TransactionLedger tarLedger = (TransactionLedger) transactionLedgerDAO.findByCostId(fclBlCostCodes.getCodeId(), fclBl.getBolId());
                        if (tarLedger != null && tarLedger.getStatus() != null && tarLedger.getStatus().equalsIgnoreCase("open") && fclBl.getReadyToPost() != null && fclBl.getReadyToPost().equalsIgnoreCase("M")) {
                            double transactionAmount = tarLedger.getTransactionAmt();
                            transactionAmount = fclBlCostCodes.getAmount();
                            tarLedger.setTransactionAmt(transactionAmount);
                            tarLedger.setBalance(transactionAmount);
                            tarLedger.setBalanceInProcess(transactionAmount);
                        }
                    }
                }
                flag = false;
                break;
            }
        }
        if (flag && "M".equalsIgnoreCase(fclBl.getReadyToPost())) {
//            fclBl.getFclblcostcodes().add(fclBlCostCodes);
            List<FclBlCostCodes> list = new ArrayList<FclBlCostCodes>();
            list.add(fclBlCostCodes);
            manifestBc.getTransactionObject(fclBl, getFclBlCostBeanobject(null, list, null, null, null), null);
        }
    }

    public String disAproveCorrectedBL(String notice, String blNumber) throws Exception {
        FclBl fclbl = null;
        FclBlCorrectionsDAO blCorrectionsDAO = new FclBlCorrectionsDAO();
        blCorrectionsDAO.setDisApprove(notice, blNumber);
        String correctedBLNo = blNumber + FclBlConstants.DELIMITER + notice;
        FclBl fclBl = fclBlDAO.findById(correctedBLNo);
        if (null != fclBl && null != fclBl.getFileNo()) {
            fclbl = new FclBlDAO().getOriginalBl(fclBl.getFileNo());
        }
        Integer correctionCount = 1;
        if (null != fclbl && null != fclbl.getCorrectionCount()) {
            correctionCount = fclbl.getCorrectionCount() + correctionCount;
        }
        if (null != blNumber) {
            fclBlDAO.saveCorrectionCount(correctionCount, blNumber);
        }
        if (null != fclBl) {
            fclBlDAO.delete(fclBl);
        }
        return "";
    }

    public void deleteFCLBLCreatedByCorrection(String blNumber) throws Exception {
        List<FclBl> fclblList = fclBlDAO.getAllBlNumbers(blNumber + FclBlConstants.DELIMITER);
        for (FclBl fclBl : fclblList) {
            fclBlDAO.delete(fclBl);
        }
    }

    public List getCorrectedList(List fclChargesList) throws Exception {
        /*Map<Integer,FclBlCharges> fclBlMap = new HashMap<Integer,FclBlCharges>();
         List fclChargeList = new ArrayList();
         for(int i=0;i<fclChargesList.size();i++){
         FclBlCharges fclBlCharges = (FclBlCharges)fclChargesList.get(i);
         fclBlMap.put(i, fclBlCharges);
         }
         for(int i=0;i<fclChargesList.size();i++){
         FclBlCharges fclBlCharges = (FclBlCharges)fclChargesList.get(i);
         for(int j=i+1;j<fclChargesList.size();j++){
         FclBlCharges fclBlChargesDup  = (FclBlCharges)fclChargesList.get(j);
         if(fclBlChargesDup.getChargeCode().trim().equalsIgnoreCase(fclBlCharges.getChargeCode().trim())){
         if(null != fclBlChargesDup.getAmount() && null != fclBlCharges.getAmount()
         && fclBlChargesDup.getAmount()>fclBlCharges.getAmount()){
         fclBlMap.remove(i);
         }else{
         fclBlMap.remove(j);
         }
         }
         }
         }
         Set<Integer> keySet = fclBlMap.keySet();
         for (Integer key : keySet) {
         fclChargeList.add(fclBlMap.get(key));
         }*/
        //DONT WORRY ABT THIS FUNCTION
        return fclChargesList;
    }

    public BookingFcl reverseToBook(String billNumber) throws Exception {
        List<FclBl> fclBList = fclBlDAO.findBolId(billNumber);
        if (fclBList.size() > 0) {
            FclBl fclBl = (FclBl) fclBList.get(0);
            new AccrualsDAO().deleteAccruals(fclBl.getBol());
            String fileNumber = fclBl.getFileNo();
            String screenName = null != fclBl.getFileType() && fclBl.getFileType().equalsIgnoreCase("I") ? "IMPORT FILE" : "FCLFILE";
            new DocumentStoreLogDAO().deleteDocuments(screenName, CommonConstants.SS_MASTER_BL, fileNumber);
            deleteFclBl(fclBl);
            bookingFcl = bookingFclDAO.getFileNoObject(fileNumber);
            String bookingId = (bookingFcl.getBookingId() != null) ? bookingFcl.getBookingId().toString() : null;
            List<BookingfclUnits> bookingFclUnitsList = new BookingfclUnitsDAO().getbookingfcl2(bookingId);
            for (BookingfclUnits bookingfclUnits : bookingFclUnitsList) {
                bookingfclUnits.setRateChangeAmount(null);
                bookingfclUnits.setRateChangeMarkup(null);
            }
            bookingFcl.setBlFlag("off");
            bookingFcl.setBlBy(null);
            bookingFcl.setBlDate(null);
        }
        return bookingFcl;
    }

    public void deleteFclBl(FclBl fclBl) throws Exception {
        fclBlDAO.delete(fclBl);
    }

    public Quotation reverseToQuote(String billNumber) throws Exception {
        FclBlDAO fclBlDAO = new FclBlDAO();
        List<FclBl> fclBList = fclBlDAO.findBolId(billNumber);
        if (!fclBList.isEmpty()) {
            FclBl fclBl = fclBList.get(0);
            new AccrualsDAO().deleteAccruals(fclBl.getBol());
            String fileNumber = fclBl.getFileNo();
            new DocumentStoreLogDAO().deleteDocuments("IMPORT FILE", CommonConstants.SS_MASTER_BL, fileNumber);
            deleteFclBl(fclBl);
            quotation = quotationDAO.getFileNoObject(fileNumber);
            quotation.setBlBy(null);
            quotation.setBlDate(null);
            quotation.setFinalized(null);
        }
        return quotation;
    }

    public String checkForAgentsAndRules(String destination) throws Exception {
        String unLocCode = "", portname = "", returnString = "";
        int i = destination.indexOf("/");
        if (i != -1) {
            portname = destination.substring(0, i);
        }
        int j = destination.lastIndexOf("(");
        if (j != -1) {
            unLocCode = destination.substring(j + 1, destination.length() - 1);
        }
        List<Ports> portList = portsDAO.findForUnlocCodeAndPortName(unLocCode, portname);
        if (CommonFunctions.isNotNullOrNotEmpty(portList)) {
            Ports port = portList.get(0);
            List<AgencyInfo> agentRulesList = portsDAO.getListOfFclAgents(port.getId());
            if (CommonFunctions.isNotNullOrNotEmpty(agentRulesList)) {
                Integer portId = 0;
                boolean flag = true;
                for (AgencyInfo agencyInfo : agentRulesList) {
                    portId = agencyInfo.getSchnum();
                    if (agencyInfo.getAgencyRules() != null && !agencyInfo.getAgencyRules().isEmpty()) {
                        returnString = "rules";// previous message was  Agents and Rules present
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    FCLPortConfigurationDAO fConfigurationDAO = new FCLPortConfigurationDAO();
                    List portRulelist = fConfigurationDAO.getPortRules(portId);
                    returnString = CommonFunctions.isNotNullOrNotEmpty(portRulelist) ? "rules" : returnString; //previous was Ports Rules present
                }
            } /*else {
             returnString = "No Agents";
             }*/

        }
        return returnString;
    }

    public String deleteChargesForPBA(String bol) throws Exception {
        if (CommonFunctions.isNotNull(bol)) {
            FclBl fclBl = fclBlDAO.findById(new Integer(bol));
            List<FclBlCharges> chargesList = (CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclcharge())) ? new ArrayList(fclBl.getFclcharge()) : Collections.EMPTY_LIST;
            List<FclBlCostCodes> costCodeList = (CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclblcostcodes())) ? new ArrayList(fclBl.getFclblcostcodes()) : Collections.EMPTY_LIST;
            for (FclBlCostCodes fclBlCostCodes : costCodeList) {
                String costCode = fclBlCostCodes.getCostCode();
                if (costCode != null && (costCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE) || costCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE)
                        || costCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE))) {
                    manifestBc.deleteTransationRecord(fclBl, fclBlCostCodes.getCostCode());
                    fclBl.getFclblcostcodes().remove(fclBlCostCodes);
                }
            }
            for (FclBlCharges fclBlCharges : chargesList) {
                String chargeCode = fclBlCharges.getChargeCode();
                if (chargeCode != null && (chargeCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE) || chargeCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE)
                        || chargeCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE))) {
                    fclBl.getFclcharge().remove(fclBlCharges);
                }
            }
        }
        return null;
    }

    public String testCodeFromFclBlCharges(String bolId) throws Exception {
        String returnString = "";
        if (CommonFunctions.isNotNull(bolId)) {
            List<FclBlCharges> list = fclBlChargesDAO.findByPropertyAndBlNumber("chargeCode",
                    FclBlConstants.ADVANCESHIPPERCODE, new Integer(bolId));
            if (CommonFunctions.isNotNullOrNotEmpty(list)) {
                returnString = "shipper";
            }
            List<FclBlCharges> list3 = fclBlChargesDAO.findByPropertyAndBlNumber("chargeCode",
                    FclBlConstants.ADVANCEFFCODE, new Integer(bolId));
            if (CommonFunctions.isNotNullOrNotEmpty(list3)) {
                returnString = returnString + "," + "forwarder";
            }
            List<FclBlCharges> list2 = fclBlChargesDAO.findByPropertyAndBlNumber("chargeCode",
                    FclBlConstants.ADVANCESURCHARGECODE, new Integer(bolId));
            if (CommonFunctions.isNotNullOrNotEmpty(list2)) {
                returnString = returnString + "," + "forwarder";
            }

        }
        return returnString;
    }

    public boolean deleteCostCode(String CostCode, List<FclBlCostCodes> list, FclBl fclBl) throws Exception {
        boolean flag = false;
        for (FclBlCostCodes fclBlCostCodes : list) {
            if (fclBlCostCodes.getCostCode() != null && CostCode != null
                    && fclBlCostCodes.getCostCode().equalsIgnoreCase(CostCode)
                    && (CommonUtils.isEmpty(fclBlCostCodes.getTransactionType())
                    || CommonUtils.isEqualIgnoreCase(fclBlCostCodes.getTransactionType(), ConstantsInterface.TRANSACTION_TYPE_ACCRUALS))) {
                fclBl.getFclblcostcodes().remove(fclBlCostCodes);
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean deleteChargeCode(String CostCode, List<FclBlCharges> list, FclBl fclBl) throws Exception {
        boolean flag = false;
        for (FclBlCharges fclBlCharges : list) {
            if (fclBlCharges.getChargeCode() != null && CostCode != null
                    && fclBlCharges.getChargeCode().equalsIgnoreCase(CostCode)) {
                fclBl.getFclcharge().remove(fclBlCharges);
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void updateAdjustmentAmount(String[] adjustment, String[] chargeCode, String BolNo) throws Exception {
        FclBlChargesDAO chargesDAO = new FclBlChargesDAO();
        for (int i = 0; i < chargeCode.length; i++) {
            chargeCode[i] = chargeCode[i].replaceAll("\"", "").trim();
        }
        for (int i = 0; i < adjustment.length; i++) {
            adjustment[i] = adjustment[i].replaceAll(",", "").trim();
        }
        for (int i = 0; i < chargeCode.length; i++) {
            String chargCode = chargeCode[i];
            if (chargCode.equalsIgnoreCase("INTMDL")) {
                chargCode = "INT";
            }
            FclBlCharges fclBLCharges = chargesDAO.getPerticularCharge(chargCode, BolNo);
            if (null != adjustment[i] && !adjustment[i].equalsIgnoreCase("")
                    && StringFormatter.isNumeric(adjustment[i])) {
                double amount = fclBLCharges.getOldAmount() + Double.parseDouble(adjustment[i]);
                fclBLCharges.setAmount(amount);
                fclBLCharges.setAdjustment(Double.parseDouble(adjustment[i]));
                chargesDAO.update(fclBLCharges);
            }
        }
    }

    public String getUnassignedHazmatList(String bolId) throws Exception {
        String returnString = "";
        List quoteHazmatList = quotationBC.getHazmatList("FclBl", null);
        List quoteBookingHazmatList = listOfHasMateridalFromBooking(bolId, quoteHazmatList);
        List unassignedHazmatList = new ArrayList();
        //---CHECKING FOR DELETED HAZMAT----
        if (CommonFunctions.isNotNullOrNotEmpty(quoteBookingHazmatList)) {
            for (Iterator iter = quoteBookingHazmatList.iterator(); iter.hasNext();) {
                HazmatMaterial hazmatMaterial = (HazmatMaterial) iter.next();
                if (null != hazmatMaterial.getDeletedFlag() && hazmatMaterial.getDeletedFlag().equals("YES")) {
                    //--- do nothing--:)
                } else {
                    unassignedHazmatList.add(hazmatMaterial);
                }
            }//---
        }

        if (CommonFunctions.isNotNullOrNotEmpty(unassignedHazmatList)) {
            returnString = "unassigned Hazmats present";
        }
        return returnString;
    }

    public String checkForAssignedHazmats(String bol) throws Exception {
        String returnString = "";
        List containerList = (List) new FclBlContainerDAO().getAllContainers(bol);
        if (containerList.size() > 0) {
            Iterator iter = containerList.iterator();
            while (iter.hasNext()) {
                FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                List hazmatList = new HazmatMaterialDAO().getAssignedHazMat(fclBlContainer.getTrailerNoId());
                if (hazmatList.size() > 0) {
                    returnString = "assigned hazmats are present";
                }
            }
        }
        return returnString;
    }

    public String checkForDisable(String accountNo) throws Exception {
        return new TradingPartnerDAO().chekForDisable(accountNo);
    }

    public String blIdForCorrections(String bolNo) throws Exception {
        FclBlDAO blDAO = new FclBlDAO();
        return blDAO.getBOLId1(bolNo);
    }

    public void clearTradingPartnerSession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("tradingPartnerId") != null) {
            session.removeAttribute("tradingPartnerId");
        }
        if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER) != null) {
            session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER);
        }
        if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM) != null) {
            session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM);
        }
        if (session.getAttribute(TradingPartnerConstants.VIEW) != null) {
            session.removeAttribute(TradingPartnerConstants.VIEW);
        }
        if (session.getAttribute("tradingPartnerSearchList") != null) {
            session.removeAttribute("tradingPartnerSearchList");
        }
    }

    public void calculatePBAADVSHPWhileManifest(FclBl fclBl, HttpServletRequest request) throws Exception {
        Double returnValue;
        Double amount = 0.0;
        returnValue = createPBACharges(fclBl, FclBlConstants.ADVANCESHIPPERCODE, request);
        amount += (null != returnValue) ? returnValue : 0.0;
        returnValue = createPBACharges(fclBl, FclBlConstants.ADVANCEFFCODE, request);
        amount += (null != returnValue) ? returnValue : 0.0;
        if (amount != 0.0 && !LoadLogisoftProperties.getProperty("advanceSurchargePercentage").equals("")) {
            amount = CommonFunctions.getPercentOf(amount, new Integer(LoadLogisoftProperties.getProperty("advanceSurchargePercentage")));
            createPBASURCHARGE(amount, fclBl);
        }
    }

    public Double createPBACharges(FclBl fclBl, String chargCode, HttpServletRequest request) throws Exception {
        Double amount = null;
        if (CommonFunctions.isNotNull(fclBl)) {
            List<FclBlCharges> list = fclBlChargesDAO.findByPropertyAndBlNumber("chargeCode",
                    chargCode, new Integer(fclBl.getBol()));
            if (CommonFunctions.isNotNullOrNotEmpty(list)) {
                FclBlCharges fclBlCharges = list.get(0);
                List costCodeList = fclBlCostCodesDAO.findByPropertyAndBlNumber("costCode",
                        chargCode, new Integer(fclBl.getBol()));
                if (!CommonFunctions.isNotNullOrNotEmpty(costCodeList)) {
                    FclBlCostCodes fclBlCostCodes = addCostCodes(fclBlCharges.getAmount(), chargCode, fclBl);
                    amount = fclBlCostCodes.getAmount();
                    fclBl = fclBlDAO.findById(fclBl.getBol());
                    addCostCodeToAccruals(fclBlCostCodes, fclBl, request);
                }
            }
        }
        return amount;
    }

    public FclBlCostCodes addCostCodes(Double amount, String chargCode, FclBl fclBl) throws Exception {
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        fclBlCostCodes.setBolId(fclBl.getBol());
        fclBlCostCodes.setAmount(amount);
        fclBlCostCodes.setBookingFlag("new");
        fclBlCostCodes.setReadyToPost("M");
        fclBlCostCodes.setCurrencyCode("USD");
        if (chargCode != null && chargCode.equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE)) {
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                fclBlCostCodes.setAccName(fclBl.getHouseShipperName());
                fclBlCostCodes.setAccNo(fclBl.getHouseShipper());
            } else {
                fclBlCostCodes.setAccName(fclBl.getShipperName());
                fclBlCostCodes.setAccNo(fclBl.getShipperNo());
            }
            fclBlCostCodes.setCostCode(FclBlConstants.ADVANCESHIPPERCODE);
            fclBlCostCodes.setCostCodeDesc(FclBlConstants.ADVANCESHIPPERDESC);
        } else if (chargCode != null && (chargCode.equalsIgnoreCase(FclBlConstants.PBACODE)
                || chargCode.equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)
                || chargCode.equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE))) {
            fclBlCostCodes.setAccName(fclBl.getForwardingAgentName());
            fclBlCostCodes.setAccNo(fclBl.getForwardAgentNo());
            fclBlCostCodes.setCostCode(FclBlConstants.PBACODE);
            fclBlCostCodes.setCostCodeDesc(FclBlConstants.PBADESC);
            if (chargCode.equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)) {
                fclBlCostCodes.setCostCode(FclBlConstants.ADVANCEFFCODE);
                fclBlCostCodes.setCostCodeDesc(FclBlConstants.ADVANCEFFDESC);
            } else if (chargCode.equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE)) {
                fclBlCostCodes.setCostCode(FclBlConstants.ADVANCESURCHARGECODE);
                fclBlCostCodes.setCostCodeDesc(FclBlConstants.ADVANCESURCHARGEDESC);
            }
        }
        fclBlCostCodesDAO.save(fclBlCostCodes);
        return fclBlCostCodes;
    }

    /**
     * @param amount
     * @param fclBl
     */
    public void createPBASURCHARGE(Double amount, FclBl fclBl) throws Exception {
        boolean flag = true;
        List<FclBlCharges> fclBlChargeList = (null != fclBl.getFclcharge()) ? new ArrayList(fclBl.getFclcharge()) : Collections.EMPTY_LIST;
        for (FclBlCharges fclBlCharges : fclBlChargeList) {
            if (FclBlConstants.ADVANCESURCHARGECODE.equalsIgnoreCase(fclBlCharges.getChargeCode())) {
                Object object = fclBlChargesDAO.sumOfADVFFandADVSHP(fclBl.getBol());
                if (object != null) {
                    Double sumAmount = (Double) object;
                    Double pbaAmount = CommonFunctions.getPercentOf(sumAmount, new Integer(LoadLogisoftProperties.getProperty("advanceSurchargePercentage")));
                    fclBlCharges.setAmount(pbaAmount);
                    fclBlCharges.setBolId(fclBl.getBol());
                    fclBlCharges.setOldAmount(pbaAmount);
                }
                flag = false;
                break;
            }
        }
        if (flag && null != amount) {
            FclBlCharges newFclBlCharges = new FclBlCharges();
            newFclBlCharges.setChargesId(null);
            newFclBlCharges.setBolId(fclBl.getBol());
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                newFclBlCharges.setBillTo(FclBlConstants.CONSIGNEE);
            } else {
                newFclBlCharges.setBillTo("Agent");
            }
            newFclBlCharges.setPrintOnBl("Yes");
            newFclBlCharges.setCurrencyCode("USD");
            newFclBlCharges.setAmount(amount);
            newFclBlCharges.setOldAmount(amount);
            newFclBlCharges.setReadyToPost("M");
            newFclBlCharges.setChargeCode(FclBlConstants.ADVANCESURCHARGECODE);
            newFclBlCharges.setCharges(FclBlConstants.ADVANCESURCHARGEDESC);
            fclBl.getFclcharge().add(newFclBlCharges);
            new FclBlDAO().save(fclBl);
        }

    }

    public String getFinalDeliveryForBlPrint(FclBl bl, String houseOrMaster) throws Exception {
        String pod = "";
        String destination = "";
        boolean checkEquls = false;
        String finalDelivery = "";
        String doorDestinationAsFinalDelivery = "";
        if ("master".equalsIgnoreCase(houseOrMaster)) {
            doorDestinationAsFinalDelivery = bl.getDoorDestinationAsFinalDeliveryToMaster();
        } else {
            doorDestinationAsFinalDelivery = bl.getDoorDestinationAsFinalDeliveryToHouse();
        }
        if ("Yes".equalsIgnoreCase(doorDestinationAsFinalDelivery)) {
            finalDelivery = bl.getDoorOfDestination();
        } else {
            if (CommonUtils.isNotEmpty(bl.getFinalDestination()) && bl.getFinalDestination().lastIndexOf("(") != -1 && bl.getFinalDestination().lastIndexOf(")") != -1) {
                destination = bl.getFinalDestination().substring(bl.getFinalDestination().lastIndexOf("(") + 1, bl.getFinalDestination().lastIndexOf(")"));
                if (CommonUtils.isNotEmpty(bl.getPortofDischarge()) && bl.getPortofDischarge().lastIndexOf("(") != -1 && bl.getPortofDischarge().lastIndexOf(")") != -1) {
                    pod = bl.getPortofDischarge().substring(bl.getPortofDischarge().lastIndexOf("(") + 1, bl.getPortofDischarge().lastIndexOf(")"));
                }
                if (destination.equalsIgnoreCase(pod)) {
                    checkEquls = true;
                }
            }
            if (checkEquls) {
                finalDelivery = "";
            } else {
                finalDelivery = bl.getFinalDestination();
            }
        }
        return finalDelivery.toUpperCase();
    }

    public String getDoorOriginForHouseBlPrint(FclBl bl) throws Exception {
        String pol = "";
        String origin = "";
        boolean checkEquls = false;
        String doorOrigin = "";
        if (CommonUtils.isNotEmpty(bl.getDoorOfOrigin())) {
            doorOrigin = bl.getDoorOfOrigin();
        } else {
            if (CommonUtils.isNotEmpty(bl.getTerminal()) && bl.getTerminal().lastIndexOf("(") != -1 && bl.getTerminal().lastIndexOf(")") != -1) {
                origin = bl.getTerminal().substring(bl.getTerminal().lastIndexOf("(") + 1, bl.getTerminal().lastIndexOf(")"));
                if (CommonUtils.isNotEmpty(bl.getPortOfLoading()) && bl.getPortOfLoading().lastIndexOf("(") != -1 && bl.getPortOfLoading().lastIndexOf(")") != -1) {
                    pol = bl.getPortOfLoading().substring(bl.getPortOfLoading().lastIndexOf("(") + 1, bl.getPortOfLoading().lastIndexOf(")"));
                }
                if (origin.equalsIgnoreCase(pol)) {
                    checkEquls = true;
                }
            }
            if (checkEquls && CommonUtils.isEmpty(bl.getDoorOfOrigin())) {
                doorOrigin = "";
            } else {
                doorOrigin = bl.getTerminal();
            }
        }
        return doorOrigin.toUpperCase();
    }

    public String getDoorOriginForMasterBlPrint(FclBl bl) throws Exception {
        String doorOrigin = "";
        if ("no".equalsIgnoreCase(bl.getDoorOriginAsPlor())) {
            doorOrigin = bl.getTerminal();
        } else {
            doorOrigin = getDoorOriginForHouseBlPrint(bl);
        }
        return doorOrigin.toUpperCase();
    }
    //delete all chargeCode..

    public void deletecodeCodes(FclBl saveFclBl, String chargeCode) throws Exception {
        List<FclBlCharges> list2 = (null != saveFclBl.getFclcharge()) ? new ArrayList(saveFclBl.getFclcharge()) : Collections.EMPTY_LIST;
        List<FclBlCostCodes> costList = (null != saveFclBl.getFclblcostcodes()) ? new ArrayList(saveFclBl.getFclblcostcodes()) : Collections.EMPTY_LIST;
        if (chargeCode != null && chargeCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)) {
            deleteCostCode(FclBlConstants.ADVANCEFFCODE, costList, saveFclBl);
            manifestBc.deleteTransationRecord(saveFclBl, chargeCode);
        }
        if (chargeCode != null && chargeCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE)) {
            deleteCostCode(FclBlConstants.ADVANCESHIPPERCODE, costList, saveFclBl);
            manifestBc.deleteTransationRecord(saveFclBl, chargeCode);
        }
        if (fclBlChargesDAO.sumOfADVFFandADVSHP(saveFclBl.getBol()) != null) {
            createPBASURCHARGE(null, saveFclBl);
        } else {
            deleteChargeCode(FclBlConstants.ADVANCESURCHARGECODE, list2, saveFclBl);
        }
    }

    public String validateCustomer(String accountNumber, String importFlag) throws Exception {
        String returnString = "", addContacts = "";
        if (CommonFunctions.isNotNull(accountNumber)) {
            TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
            boolean flag = false;
            String creditHold = TradingPartnerConstants.NOCREDIT;
            TradingPartner tradingPartner = tradingPartnerBC.findTradingPartnerById(accountNumber);
            String autosCredit = new CustomerAccountingDAO().getAutosCredit(accountNumber);
            if (null != tradingPartner && CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getCustomerContact())) {
                for (Iterator iterator = tradingPartner.getCustomerContact().iterator(); iterator.hasNext();) {
                    CustomerContact contact = (CustomerContact) iterator.next();

                    String code = null != contact.getCodek() ? contact.getCodek().getCode() : null;
                    if ("I".equalsIgnoreCase(importFlag)) {
                        if (code != null && CommonUtils.in(code, "E", "F") && contact.isFclImports()) {
                            flag = true;
                            break;
                        }
                    } else {
                        if (code != null && CommonUtils.in(code, "E", "F") && contact.isFclExports()) {
                            flag = true;
                            break;
                        }

                    }
                }
                if (null != tradingPartner && CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getAccounting())) {
                    for (CustomerAccounting customerAccounting : tradingPartner.getAccounting()) {
                        if (null != customerAccounting.getCreditStatus() && null != customerAccounting.getCreditStatus().getCodedesc()
                                && (customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.CREDITHOLD)
                                || customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.NOCREDIT)
                                || customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.SUSPENDED_SEE_ACCOUNTING)
                                || customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.LEGAL_SEE_ACCOUNTING)
                                || customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.IN_GOOD_STANDING))) {

                            if (null != importFlag && importFlag.equals("I") && customerAccounting.getImportCredit().equalsIgnoreCase("N")) {
                                creditHold = TradingPartnerConstants.NOCREDIT;
                            } else if ("Y".equalsIgnoreCase(customerAccounting.getExemptCreditProcess())) {
                                creditHold = TradingPartnerConstants.IN_GOOD_STANDING;
                            } else {
                                creditHold = customerAccounting.getCreditStatus().getCodedesc();
                            }
                            break;
                        }
                        if (null != customerAccounting.getOverLimit()
                                && !customerAccounting.getOverLimit().equalsIgnoreCase("off")) {
                            creditHold = "Over Limit";
                            break;
                        }
                    }
                }
                addContacts = (!flag) ? "-->Freight Invoice Auto Notification Contact does NOT exist,"
                        + " For the Party " + tradingPartner.getAccountName() + "(" + tradingPartner.getAccountno() + ")<br><br> Please Enter Contacts in TP " : "";
                returnString += "-->" + creditHold + " For the Party " + tradingPartner.getAccountName() + "(" + tradingPartner.getAccountno() + ") ";
                if (null != autosCredit && autosCredit.equalsIgnoreCase("Yes")) {
                    returnString += "==" + addContacts + "===" + "HHG/PE/AUTOS CREDIT";
                } else {
                    returnString += "==" + addContacts + "===" + "NO CREDIT FOR HHG/PE/AUTOS";
                }
            }

        }
        return returnString;
    }

    public String getCreditWarning(String accountNumber) throws Exception {
        String creditHold = "";
        if (CommonFunctions.isNotNull(accountNumber)) {
            creditHold = TradingPartnerConstants.NOCREDIT;
            TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
            boolean flag = false;
            TradingPartner tradingPartner = tradingPartnerBC.findTradingPartnerById(accountNumber);
            if (CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getCustomerContact())) {
                for (Iterator iterator = tradingPartner.getCustomerContact().iterator(); iterator.hasNext();) {
                    CustomerContact contact = (CustomerContact) iterator.next();
                    String code = null != contact.getCodec() ? contact.getCodec().getCode() : null;
                    if (code != null && (code.equalsIgnoreCase(TradingPartnerConstants.E1) || code.equalsIgnoreCase(TradingPartnerConstants.E3)
                            || code.equalsIgnoreCase(TradingPartnerConstants.F1) || code.equalsIgnoreCase(TradingPartnerConstants.F3))) {
                        flag = true;
                        break;
                    }
                }
            }
            if (CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getAccounting())) {
                for (Iterator accountingList = tradingPartner.getAccounting().iterator(); accountingList.hasNext();) {
                    CustomerAccounting customerAccounting = (CustomerAccounting) accountingList.next();
                    if (null != customerAccounting.getCreditStatus() && null != customerAccounting.getCreditStatus().getCodedesc()
                            && (customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.CREDITHOLD)
                            || customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.NOCREDIT)
                            || customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.SUSPENDED_SEE_ACCOUNTING)
                            || customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.LEGAL_SEE_ACCOUNTING)
                            || customerAccounting.getCreditStatus().getCodedesc().equalsIgnoreCase(TradingPartnerConstants.IN_GOOD_STANDING))) {
                        creditHold = customerAccounting.getCreditStatus().getCodedesc();
                        break;
                    }
                    if (null != customerAccounting.getOverLimit()
                            && !customerAccounting.getOverLimit().equalsIgnoreCase("off")) {
                        creditHold = "Over Limit";
                        break;
                    }
                }
            }
        }//if
        return creditHold;
    }

    public String redInvoiceValidateCustomer(String accountNumber, String importFlag) throws Exception {
        String returnString = "";
        if (CommonFunctions.isNotNull(accountNumber)) {
            TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
            boolean flag = false;
            TradingPartner tradingPartner = tradingPartnerBC.findTradingPartnerById(accountNumber);
            if (CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getCustomerContact())) {
                for (Iterator iterator = tradingPartner.getCustomerContact().iterator(); iterator.hasNext();) {
                    CustomerContact contact = (CustomerContact) iterator.next();
                    String code = null != contact.getCodek() ? contact.getCodek().getCode() : null;
                    if ("I".equalsIgnoreCase(importFlag)) {
                        if (code != null && CommonUtils.in(code, "E", "F") && contact.isFclImports()) {
                            flag = true;
                            break;
                        }
                    } else {
                        if (code != null && CommonUtils.in(code, "E", "F") && contact.isFclExports()) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
            returnString = (!flag) ? "-->AR Invoice Auto Notification Contact does NOT exist,"
                    + " For the Party " + tradingPartner.getAccountName() + "(" + tradingPartner.getAccountno() + ")<br><br> Please Enter Contacts in TP " : "";
        }//if
        return returnString;
    }

    /**
     * ----This method is to get the list of all the Disputed Bl------
     *
     * @param fclBLForm
     * @param status
     * @return
     */
    public List getDisputedBL(FclBLForm fclBLForm, String status) throws Exception {
        HashMap fieldsMap = new HashMap();
        StringFormatter stringFormatter = new StringFormatter();
        if (CommonFunctions.isNotNull(fclBLForm.getFileNo())) {
            fieldsMap.put(CommonConstants.FILENO, fclBLForm.getFileNo().trim().replace("'", "'+'"));
        }
        if (CommonFunctions.isNotNull(fclBLForm.getTerminal())) {
            String origin = fclBLForm.getTerminal().trim().replace("'", "''");
            origin = stringFormatter.getDestinationCodeWithBracket(origin);
            origin = "%" + origin + "%";
            fieldsMap.put(CommonConstants.ORIGIN, origin);
        }
        if (CommonFunctions.isNotNull(fclBLForm.getFinalDestination())) {
            fieldsMap.put(CommonConstants.DESTINATION, fclBLForm.getFinalDestination().replace("'", "'+'"));
        }
        if (CommonFunctions.isNotNull(fclBLForm.getPortofladding())) {
            fieldsMap.put(CommonConstants.POL, fclBLForm.getPortofladding().replace("'", "'+'"));
        }
        if (CommonFunctions.isNotNull(fclBLForm.getPortofdischarge())) {
            fieldsMap.put(CommonConstants.POD, fclBLForm.getPortofdischarge().replace("'", "'+'"));
        }
        if (CommonFunctions.isNotNull(fclBLForm.getSslineName())) {
            fieldsMap.put(CommonConstants.SSL, fclBLForm.getSslineName());
        }
        if (CommonUtils.isNotEmpty(fclBLForm.getEta())) {
            Date date = new Date(fclBLForm.getEta());
            String etaDate = DateUtils.formatDate(date, "MM/dd/yyyy");
            fieldsMap.put(CommonConstants.ETA, etaDate);
        }
        if (CommonFunctions.isNotNullOrNotEmpty(fclBLForm.getEtd())) {
            Date date = new Date(fclBLForm.getEtd());
            String etdDate = DateUtils.formatDate(date, "MM/dd/yyyy");
            fieldsMap.put(CommonConstants.ETD, etdDate);
        }
        return new DocumentStoreLogDAO().getDisputedDocList(fieldsMap, status);
    }

    public List<CustomerContact> getCodeCContactList(String bol) throws Exception {
        List<CustomerContact> list = getListOfPartyDetails(bol);
        List<CustomerContact> contactList = new ArrayList<CustomerContact>();
        for (CustomerContact contact : list) {
            if (null != contact.getCodec() && CommonUtils.isNotEmpty(contact.getCodec().getCode())) {
                contactList.add(contact);
            }
        }
        return contactList;
    }

    public String getCodeCContactMail(String bol) throws Exception {
        StringBuilder builder = new StringBuilder("");
        if (CommonFunctions.isNotNull(bol)) {
            FclBl fclBl = fclBlDAO.findById(bol);
            if (CommonFunctions.isNotNull(fclBl.getAgentNo())) {
                boolean isFirst = true;
                TradingPartner tradingPartner = new TradingPartnerBC().findTradingPartnerById(fclBl.getAgentNo());
                if (tradingPartner != null && tradingPartner.getCustomerContact() != null) {
                    for (Iterator iterator = tradingPartner.getCustomerContact().iterator(); iterator.hasNext();) {
                        CustomerContact contact = (CustomerContact) iterator.next();
                        if (null != contact.getCodec() && CommonUtils.isNotEmpty(contact.getCodec().getCode())) {
                            if (null != contact.getEmail() && !contact.getEmail().equals("")) {
                                if (!isFirst) {
                                    builder.append(", ");
                                }
                                builder.append(contact.getEmail());
                                isFirst = false;
                            }
                        }
                    }
                }
            }
        }
        return builder.toString();
    }

    public List<CustomerContact> getListOfPartyDetails(String bol) throws Exception {
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        List<CustomerContact> custContactList = new ArrayList<CustomerContact>();
        Map<String, CustomerContact> custContactMap = new HashMap<String, CustomerContact>();
        if (CommonFunctions.isNotNull(bol)) {
            FclBl fclBl = fclBlDAO.findById(bol);
            if (CommonFunctions.isNotNull(fclBl.getAgentNo())) {
                //agent
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getAgentNo());
            }
            if (CommonFunctions.isNotNull(fclBl.getForwardAgentNo())) {
                //Forwarder
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getForwardAgentNo());
            }
            if (CommonFunctions.isNotNull(fclBl.getShipperNo())) {
                //HouseBl Shipper
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getShipperNo());
            }
            if (CommonFunctions.isNotNull(fclBl.getBillTrdPrty())) {
                //ThirdParty
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getBillTrdPrty());
            }
            if (CommonFunctions.isNotNull(fclBl.getConsigneeNo())) {
                //houseBL consignee
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getConsigneeNo());
            }
            if (CommonFunctions.isNotNull(fclBl.getHouseConsignee())) {
                //MasterBl consignee
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getHouseConsignee());
            }
            if (CommonFunctions.isNotNull(fclBl.getHouseNotifyPartyNo())) {
                // MasterBl NotifyParty
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getHouseNotifyPartyNo());
            }
            if (CommonFunctions.isNotNull(fclBl.getNotifyParty())) {
                //houseBl  NotifyParty
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getNotifyParty());
            }
            if (CommonFunctions.isNotNull(fclBl.getHouseShipper())) {
                //MasterBl Shipper
                tradingPartnerBC.getCustomerContactList(custContactMap, fclBl.getHouseShipper());
            }
            Set<String> set = custContactMap.keySet();
            for (String keyValue : set) {
                custContactList.add(custContactMap.get(keyValue));
            }
            //Order contact by First name
            custContactMap = new HashMap<String, CustomerContact>();
            for (CustomerContact contact : custContactList) {
                custContactMap.put(contact.getAccountName() + "-" + contact.getFirstName() + "-" + contact.getEmail(), contact);
            }
            TreeSet<String> keys = new TreeSet<String>(custContactMap.keySet());
            custContactList = new ArrayList<CustomerContact>();
            for (String key : keys) {
                custContactList.add(custContactMap.get(key));
            }
        }
        return custContactList;
    }

    public String findPropertyName(String chargeCode, String bolId) throws Exception {
        if (null != chargeCode && null != bolId) {
            return CommonFunctions.isNotNullOrNotEmpty(fclBlChargesDAO.findByPropertyAndBlNumber("chargeCode", chargeCode, new Integer(bolId))) ? "true" : "false";
        } else {
            return "false";
        }
    }

    public void updateCorrectedBl(FclBillLaddingForm fclBillLaddingForm, List correctedFclblList) throws Exception {
        for (Iterator iterator = correctedFclblList.iterator(); iterator.hasNext();) {
            FclBl fclBl = (FclBl) iterator.next();
            if (fclBillLaddingForm.getHouseBL().equals("P")) {
                fclBl.setHouseBl("P-Prepaid");
            } else if (fclBillLaddingForm.getHouseBL().equals("C")) {
                fclBl.setHouseBl("C-Collect");
            } else if (fclBillLaddingForm.getHouseBL().equals("T")) {
                fclBl.setHouseBl("T-Third Party");
            }
            if (fclBillLaddingForm.getDestinationChargesPreCol() != null && fclBillLaddingForm.getDestinationChargesPreCol().equals("P")) {
                fclBl.setDestinationChargesPreCol("P-Prepaid");
            } else if (fclBillLaddingForm.getDestinationChargesPreCol() != null && fclBillLaddingForm.getDestinationChargesPreCol().equals("C")) {
                fclBl.setDestinationChargesPreCol("C-Collect");
            }
            fclBl.setVoyages(fclBillLaddingForm.getVoyage());
            if (fclBillLaddingForm.getVesselname() != null && !fclBillLaddingForm.getVesselname().equals("")) {
                List genList = (List) genericCodeDAO.findForGenericAction(14, null, fclBillLaddingForm.getVesselname());
                if (genList != null && genList.size() > 0) {
                    GenericCode gen = (GenericCode) genList.get(0);
                    fclBl.setVessel(gen);
                }
            }
            fclBl.setVaoyageInternational(fclBillLaddingForm.getVaoyageInternational());
            fclBl.setNewMasterBL(fclBillLaddingForm.getNewMasterBL());
            fclBl.setHouseShipperName(fclBillLaddingForm.getHouseName());
            fclBl.setHouseShipper(fclBillLaddingForm.getHouseShipper());
            fclBl.setHouseShipperAddress(fclBillLaddingForm.getHouseShipper1());
            fclBl.setHouseConsigneeName(fclBillLaddingForm.getHouseConsigneeName());
            fclBl.setHouseConsignee(fclBillLaddingForm.getHouseConsignee());
            fclBl.setHouseConsigneeAddress(fclBillLaddingForm.getHouseConsignee1());
            fclBl.setHouseNotifyPartyName(fclBillLaddingForm.getHouseNotifyPartyName());
            fclBl.setHouseNotifyPartyNo(fclBillLaddingForm.getHouseNotifyParty());
            fclBl.setHouseNotifyParty(fclBillLaddingForm.getHouseNotifyPartyaddress());
            fclBl.setConsigneeName(fclBillLaddingForm.getConsigneeName());
            fclBl.setConsigneeNo(fclBillLaddingForm.getConsignee());
            fclBl.setConsigneeAddress(fclBillLaddingForm.getStreamShipConsignee());
            fclBl.setNotifyPartyName(fclBillLaddingForm.getNotifyPartyName());
            fclBl.setNotifyParty(fclBillLaddingForm.getNotifyParty());
            fclBl.setStreamshipNotifyParty(fclBillLaddingForm.getStreamshipNotifyParty());
            fclBl.setRoutedByAgentCountry(fclBillLaddingForm.getCountry());
            fclBl.setClauseDescription(fclBillLaddingForm.getClauseDescription());
            fclBl.setImportOrginBlno(fclBillLaddingForm.getImporigBL());
            fclBl.setInsurance(fclBillLaddingForm.getInsurance());
            fclBl.setOriginalBlRequired(fclBillLaddingForm.getOriginalBlRequired());
            fclBl.setExportReference(fclBillLaddingForm.getExportReference());
            fclBl.setDomesticRouting(fclBillLaddingForm.getDomesticRouting());
            fclBl.setOnwardInlandRouting(fclBillLaddingForm.getOnwardInlandRouting());
            //PRINT OPTIONS......
            fclBl.setAgentsForCarrier(fclBillLaddingForm.getAgentsForCarrier());
            fclBl.setShipperLoadsAndCounts(fclBillLaddingForm.getShipperLoadsAndCounts());
            fclBl.setPrintContainersOnBL(fclBillLaddingForm.getPrintContainersOnBL());
            fclBl.setNoOfPackages(fclBillLaddingForm.getNoOfPackages());
            fclBl.setAlternateNoOfPackages(fclBillLaddingForm.getAlternateNoOfPackages());
            fclBl.setCountryOfOrigin(fclBillLaddingForm.getConturyOfOrigin());
            fclBl.setTotalContainers(fclBillLaddingForm.getTotalContainers());
            fclBl.setProof(fclBillLaddingForm.getProof());
            fclBl.setPreAlert(fclBillLaddingForm.getPreAlert());
            fclBl.setNonNegotiable(fclBillLaddingForm.getNonNegotiable());
            fclBl.setCollectThirdParty(fclBillLaddingForm.getCollectThirdParty());
            fclBl.setDoorOriginAsPlor(fclBillLaddingForm.getDoorOriginAsPlor());
            fclBl.setDoorOriginAsPlorHouse(null != fclBillLaddingForm.getDoorOriginAsPlorHouse() ? fclBillLaddingForm.getDoorOriginAsPlorHouse() : "Yes");
            fclBl.setDoorDestinationAsFinalDeliveryToMaster(fclBillLaddingForm.getDoorDestinationAsFinalDeliveryToMaster());
            fclBl.setDoorDestinationAsFinalDeliveryToHouse(fclBillLaddingForm.getDoorDestinationAsFinalDeliveryToHouse());
            fclBl.setTrimTrailingZerosForQty(fclBillLaddingForm.getTrimTrailingZerosForQty());
            fclBl.setCertifiedTrueCopy(fclBillLaddingForm.getCertifiedTrueCopy());
            fclBl.setDockReceipt(fclBillLaddingForm.getDockReceipt());
            fclBl.setOmitTermAndPort(fclBillLaddingForm.getOmitTermAndPort());
            fclBl.setServiceContractNo(fclBillLaddingForm.getServiceContractNo());
            fclBl.setPrintAlternatePort(fclBillLaddingForm.getPrintAlternatePort());
            fclBl.setHblFDOverride(fclBillLaddingForm.getHblFDOverride());
            fclBl.setHblPODOverride(fclBillLaddingForm.getHblPODOverride());
            fclBl.setHblPOLOverride(fclBillLaddingForm.getHblPOLOverride());
            fclBl.setPrintRev(fclBillLaddingForm.getPrintRev());
            fclBl.setResendCostToBlue(fclBillLaddingForm.getResendCostToBlue());
            fclBlDAO.update(fclBl);
        }
    }

    public void updateCorrectedBlPrintOptions(FclBlNew fclBlForm, List<FclBl> correctedFclBlList) throws Exception {
        for (Iterator iterator = correctedFclBlList.iterator(); iterator.hasNext();) {
            FclBl fclBl = (FclBl) iterator.next();
            fclBl.setAgentsForCarrier(fclBlForm.getAgentsForCarrier());
            fclBl.setShipperLoadsAndCounts(fclBlForm.getShipperLoadsAndCounts());
            fclBl.setPrintContainersOnBL(fclBlForm.getPrintContainersOnBl());
            fclBl.setCountryOfOrigin(fclBlForm.getCountryOfOrigin());
            fclBl.setNoOfPackages(fclBlForm.getNoOfPackages());
            fclBl.setAlternateNoOfPackages(fclBlForm.getAlternateNoOfPackages());
            fclBl.setTotalContainers(fclBlForm.getTotalContainers());
            fclBl.setProof(fclBlForm.getProof());
            fclBl.setPreAlert(fclBlForm.getPreAlert());
            fclBl.setNonNegotiable(fclBlForm.getNonNegotiable());
            fclBl.setPrintRev(fclBlForm.getPrintRev());
            fclBl.setPrintAlternatePort(fclBlForm.getPrintAlternatePort());
            fclBl.setHblPOLOverride(fclBlForm.getHblPOLOverride());
            fclBl.setHblPODOverride(fclBlForm.getHblPODOverride());
            fclBl.setHblFDOverride(fclBlForm.getHblFDOverride());
            fclBl.setHblPlaceReceiptOverride(fclBlForm.getHblPlaceReceiptOverride());
            fclBl.setDoorOriginAsPlor(fclBlForm.getDoorOriginAsPlor());
            fclBl.setDoorOriginAsPlorHouse(fclBlForm.getDoorOriginAsPlorHouse());
            fclBl.setDoorDestinationAsFinalDeliveryToHouse(fclBlForm.getDoorDestinationAsFinalDeliveryHouse());
            fclBl.setDoorDestinationAsFinalDeliveryToMaster(fclBlForm.getDoorDestinationAsFinalDeliveryMaster());
            fclBl.setCollectThirdParty(fclBlForm.getCollectThirdParty());
            fclBl.setTrimTrailingZerosForQty(fclBlForm.getTrimTrailingZerosForQty());
            fclBl.setOmitTermAndPort(fclBlForm.getOmitTermAndPort());
            fclBl.setServiceContractNo(fclBlForm.getServiceContractNo());
            fclBl.setCertifiedTrueCopy(fclBlForm.getCertifiedTrueCopy());
            fclBl.setRatedManifest(fclBlForm.getRatedManifest());
            fclBl.setOmit2LetterCountryCode(fclBlForm.getOmit2LetterCountryCode());
            fclBl.setDockReceipt(fclBlForm.getDockReceipt());
            fclBlDAO.update(fclBl);
        }
    }

    public void calculateDateOfYard(FclBl fclBl) throws Exception {
        if (CommonFunctions.isNotNull(fclBl.getDateInYard()) && CommonFunctions.isNotNull(fclBl.getDateoutYard())) {
            Integer days = new Integer(LoadLogisoftProperties.getProperty("minimumDays"));// minum days....
            Double perdayAmount = new Double(LoadLogisoftProperties.getProperty("perDayRate"));// minum rate per day
            Double amountForChargeCode = 0.0;
            long diffenceDays = new DBUtil().getDaysBetweenTwoDays(fclBl.getDateInYard(), fclBl.getDateoutYard());
            if (diffenceDays > 5) {
                diffenceDays = diffenceDays - 5;
                diffenceDays = (diffenceDays < 0) ? 0 - diffenceDays : diffenceDays;
                amountForChargeCode = diffenceDays * perdayAmount;
                List<FclBlCharges> chargeList = (null != fclBl.getFclcharge() ? new ArrayList(fclBl.getFclcharge()) : Collections.EMPTY_LIST);
                if (CommonFunctions.isNotNullOrNotEmpty(chargeList)) {
                    FclBlCharges fclBlCharges = chargeList.get(0);
                    FclBlCharges fclBlChargesForPerDM = new FclBlCharges();
                    PropertyUtils.copyProperties(fclBlChargesForPerDM, fclBlCharges);
                    fclBlChargesForPerDM.setChargesId(null);
                    fclBlChargesForPerDM.setOldAmount(amountForChargeCode);
                    fclBlChargesForPerDM.setAmount(amountForChargeCode);
                    fclBlChargesForPerDM.setChargeCode(FclBlConstants.PERDMCODE);
                    fclBlChargesForPerDM.setCharges(FclBlConstants.PERDMCODEDESC);
                    fclBlChargesForPerDM.setReadOnlyFlag(null);
                    fclBl.getFclcharge().add(fclBlChargesForPerDM);
                }
            }
        }

    }

    public Double sumOfCharges(FclBl saveFclBl) throws Exception {
        Double currentSumOfCharges = 0.0;
        Object totalAmount = fclBlChargesDAO.sumOFCharges(saveFclBl.getBol());
        if (null != totalAmount) {
            currentSumOfCharges = (Double) totalAmount;
        }
        return currentSumOfCharges;
    }

    public String setRevisionFlagAndSendFreightPDF(FclBillLaddingForm fclBillLaddingForm, HttpServletRequest request, FclBl saveFclBl,
            User user, String realPath, String action) throws Exception {
        if (CommonFunctions.isNotNull(saveFclBl.getManifestRev())) {
            if (action != null && action.equalsIgnoreCase("manifest")) {
                //  if (!"I".equalsIgnoreCase(saveFclBl.getImportFlag())) {
                new EventsBC().sendManifestEmail(fclBillLaddingForm.getAction(), saveFclBl, user, realPath, request, fclBillLaddingForm.getFreightInvoiceContacts());
                //  }

            } else if (action != null && action.equalsIgnoreCase("unManifest")) {
                if (CommonFunctions.isNotNull(saveFclBl.getManifestRev())) {
                    Integer revsionNumber = new Integer(saveFclBl.getManifestRev());
                    revsionNumber = (revsionNumber != null) ? revsionNumber + 1 : revsionNumber;
                    saveFclBl.setManifestRev(revsionNumber != null ? revsionNumber.toString() : null);
                }
            }
        } else {
            saveFclBl.setManifestRev("0");
        }
        return saveFclBl.getManifestRev();
    }

    public String getTerminalAddress(String terminal) throws Exception {
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        String terminalNo = terminal.substring(terminal.indexOf("-") + 1, terminal.length());
        List<RefTerminal> terminalList = refTerminalDAO.findTermNumber(terminalNo);
        String address = "";
        if (CommonUtils.isNotEmpty(terminalList)) {
            RefTerminal refTerminal = terminalList.get(0);
            address = CommonUtils.isNotEmpty(refTerminal.getAddres1()) ? refTerminal.getAddres1() : "";
            address += CommonUtils.isNotEmpty(refTerminal.getAddres2()) ? ", " + refTerminal.getAddres2() : "";

            address += CommonUtils.isNotEmpty(refTerminal.getTerminalLocation()) ? "\n" + refTerminal.getTerminalLocation() : "";
            address += CommonUtils.isNotEmpty(refTerminal.getZipcde()) ? " " + refTerminal.getZipcde() : "";
            address += CommonUtils.isNotEmpty(refTerminal.getPhnnum1()) ? "\nPhone " + refTerminal.getPhnnum1() : "";

        }

        return address;
    }

    public String updateEdi(String bol, String userName) throws Exception {
        if (CommonFunctions.isNotNull(bol)) {
            Date date = new Date();
            FclBl fclbl = new FclBlDAO().findById(Integer.parseInt(bol));
            fclbl.setEdiCreatedBy(userName);
            fclbl.setEdiCreatedOn(date);
            Notes notes = new Notes();
            notes.setModuleId(NotesConstants.FILE);
            notes.setModuleRefId(fclbl.getFileNo());
            notes.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
            notes.setUpdateDate(date);
            notes.setUpdatedBy(userName);
            notes.setItemName("");
            notes.setNoteDesc("EDI Master Sent");
            new NotesBC().saveNotes(notes);
            return DateUtils.formatDate(date, "dd-MMM-yyyy HH:mm");
        }
        return null;
    }

    public boolean checkUnitNoAvailabilty(String unitNo, String bol) throws Exception {
        return new FclBlContainerDAO().checkTrailerNoAvailability(unitNo, bol);
    }

    public boolean checkSchedBAvailability(String trnref) throws Exception {
        List schedList = new SedSchedulebDetailsDAO().findByTrnref(trnref);
        if (schedList.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean checkHazmatAvailability(String containerId) throws Exception {
        List hazmatList = new QuotationBC().getHazmatList("FclBl", containerId);
        if (hazmatList.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean checkBlDrNumber(String trnref) throws Exception {
        List liist = new FclBlDAO().findByDrNo(trnref);
        if (!liist.isEmpty()) {
            return false;
        }
        return true;
    }

    public String validateAesData(String trnref) throws Exception {
        return new SedFilingBC().validateAes(new SedFilingsDAO().findByTrnref(trnref));
    }

    public String createFlatFile(String trnref, String fileNo, String isAes, HttpServletRequest request) throws Exception {
        sedFilings = new SedFilingsDAO().findByTrnref(trnref);
        String fileLocation = new SedFilingBC().createFlatFile(sedFilings);
        List<String> contents = FileUtils.readLines(new File(fileLocation));
        if (CommonUtils.isNotEmpty(contents)) {
            if ("N".equals(sedFilings.getStatus()) || "C".equals(sedFilings.getStatus()) || "E".equals(sedFilings.getStatus())) {
                sedFilings.setStatus("S");
            }
            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("loginuser");
            if (isAes.equalsIgnoreCase("aes")) {
                notesBC.saveNotes(fileNo, loginUser.getLoginName(), "AES is submitted for Transaction REF# : " + trnref);
            } else if (isAes.equalsIgnoreCase("disableAes")) {
                notesBC.saveNotes(fileNo, loginUser.getLoginName(), "AES with Transaction REF# : " + trnref + " Disabled Successfully");
            } else if (isAes.equalsIgnoreCase("enableAes")) {
                notesBC.saveNotes(fileNo, loginUser.getLoginName(), "AES with Transaction REF# : " + trnref + " Enabled Successfully");
            }
            return "Success";
        }
        return "";
    }

    public String getMandatoryFields(HazmatMaterial hazmatMaterial) throws Exception {
        String mandatory = "";
        int i = 1;
        if (CommonUtils.isEmpty(hazmatMaterial.getPropShipingNumber())) {
            mandatory = mandatory + i + ") Proper Shipping Name<br>";
            i++;
        }
        if (CommonUtils.isEmpty(hazmatMaterial.getImoClssCode())) {
            mandatory = mandatory + i + ") IMO ClassCode(Primary)<br>";
            i++;
        }
        if (CommonUtils.isEmpty(hazmatMaterial.getOuterPackingPieces())) {
            mandatory = mandatory + i + ") Outer Packing Pieces<br>";
            i++;
        }
        if (CommonUtils.isEmpty(hazmatMaterial.getOuterPackComposition())) {
            mandatory = mandatory + i + ") Outer Pack Composition<br>";
            i++;
        }
        if (CommonUtils.isEmpty(hazmatMaterial.getOuterPackagingType())) {
            mandatory = mandatory + i + ") Outer Packaging Type<br>";
            i++;
        }
        if (CommonUtils.isEmpty(hazmatMaterial.getGrossWeight())) {
            mandatory = mandatory + i + ") Gross Weight<br>";
            i++;
        }
        return mandatory;
    }

    public String[] wordWrap(String str) {
        Pattern wrapRE = Pattern.compile(".{0,33}(?:\\S(?:-| |$)|$)");
        List list = new LinkedList();
        Matcher m = wrapRE.matcher(str);
        while (m.find()) {
            list.add(m.group());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public String previewArInvoice(String searchValue, String searchBy, HttpServletRequest request) throws Exception {
        String fileName = "";
        HttpSession session = request.getSession();
        MessageResources messageResources = CommonConstants.loadMessageResources();
        ArRedInvoice arRedInvoice = new ArRedInvoice();
        User user = new User();
        if (CommonUtils.isNotEmpty(searchValue)) {
            String fileLocation = LoadLogisoftProperties.getProperty("reportLocation");
            if ("number".equalsIgnoreCase(searchBy)) {
                arRedInvoice = new ArRedInvoiceBC().getInvoiceForEdit(searchValue, "");
            } else {
                arRedInvoice = new ArRedInvoiceBC().getAPInvoice(searchValue);
            }
            if (arRedInvoice.getFileType() != null && arRedInvoice.getFileType().equalsIgnoreCase("LCLE")) {
                LclPrintUtil lclPrintUtil = new LclPrintUtil();
                return lclPrintUtil.lclArInvoiceReport(fileLocation, "", String.valueOf(arRedInvoice.getId()), "", "No", request);
            }
            fileLocation = fileLocation + "//Documents//RedInvoice//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
            File dir = new File(fileLocation);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (session.getAttribute("loginuser") != null) {
                user = (com.gp.cong.logisoft.domain.User) session.getAttribute("loginuser");
                if (null != user) {
                    user = new UserDAO().findById(user.getUserId());
                }
            }
            fileName = fileLocation + "//Invoice_" + arRedInvoice.getInvoiceNumber() + ".pdf";
            String realPath = HibernateSessionRequestFilter.servletContext.getRealPath("/");
            ArRedInvoicePdfCreator arRedInvoicePdfCreator = new ArRedInvoicePdfCreator();
            arRedInvoicePdfCreator.createReport(arRedInvoice, fileName, realPath, messageResources, user);
        }
        return fileName;
    }

    public String updateImportRelease(String bolId, String importRelease, String importReleaseOn, String importReleaseComment, String paymentRelease, String paymentReleaseOn, String paymentReleaseComment,
           String expressRelease,String expressReleaseOn,String expressReleaseComment,String deliveryOrder,
           String deliveryOrderOn,String deliveryOrderComment,String customsClearance,String customsClearanceOn,
           String customsClearanceComment,HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(bolId)) {
            HttpSession session = request.getSession();
            NotesBC notesBC = new NotesBC();
            User loginUser = (User) session.getAttribute("loginuser");
            FclBl fclBl = new FclBlDAO().findById(Integer.parseInt(bolId));
            if (null != fclBl) {
                boolean releaseFlag = false;
                StringBuffer sb = new StringBuffer("");
                if (CommonUtils.isEmpty(fclBl.getPaymentRelease()) && "Y".equalsIgnoreCase(paymentRelease)) {
                    releaseFlag = true;
                    sb.append("(Payment Release -> N to Y)");
                }
                if (CommonUtils.isEmpty(fclBl.getImportRelease()) && "Y".equalsIgnoreCase(importRelease)) {
                    if (releaseFlag) {
                        sb.append(",");
                    }
                    sb.append("(Doc Release -> N to Y)");
                }
                if (CommonUtils.isEmpty(fclBl.getExpressRelease()) && "Y".equalsIgnoreCase(expressRelease)) {
                    sb.append("(Express Release -> N to Y)");
                }
                if(CommonUtils.isEmpty(fclBl.getDeliveryOrder()) && "Y".equalsIgnoreCase(deliveryOrder)){
                  sb.append("(Delivery Order -> N to Y)");  
                }
                if(CommonUtils.isEmpty(fclBl.getCustomsClearance()) && "Y".equalsIgnoreCase(customsClearance)){
                   sb.append("(Customs Clearance -> N to Y)");   
                }
                if (CommonUtils.isNotEmpty(sb.toString())) {
                    notesBC.saveNotesWhileAddingPaymentRelease(fclBl.getFileNo(), loginUser.getLoginName(), sb.toString());
                }
                fclBl.setDeliveryOrderComment(deliveryOrderComment);
                fclBl.setCustomsClearanceComment(customsClearanceComment);
                fclBl.setImportReleaseComments(importReleaseComment);
                fclBl.setImportRelease(importRelease);
                fclBl.setPaymentRelease(paymentRelease);
                fclBl.setPaymentReleaseComments(paymentReleaseComment);
                fclBl.setExpressRelease(expressRelease);
                fclBl.setExpressReleaseComment(expressReleaseComment);
                fclBl.setDeliveryOrder(deliveryOrder);
                fclBl.setCustomsClearance(customsClearance);
                
                if (CommonUtils.isNotEmpty(expressReleaseOn)) {
                   Date expressReleaseOnDate = DateUtils.parseDate(expressReleaseOn, "MM/dd/yyyy hh:mm a");
                   fclBl.setExpressReleasedOn(expressReleaseOnDate);
                } else {
                   fclBl.setExpressReleasedOn(null);
                }
                if(CommonUtils.isNotEmpty(deliveryOrderOn)){
                    Date deliveryOrderDate = DateUtils.parseDate(deliveryOrderOn, "MM/dd/yyyy hh:mm a");
                    fclBl.setDeliveryOrderOn(deliveryOrderDate);
                } else {
                    fclBl.setDeliveryOrderOn(null);
                }
                if(CommonUtils.isNotEmpty(customsClearanceOn)){
                  Date customsClearanceOnDate = DateUtils.parseDate(customsClearanceOn, "MM/dd/yyyy hh:mm a");
                  fclBl.setCustomsClearanceOn(customsClearanceOnDate);
                } else {
                   fclBl.setCustomsClearanceOn(null); 
                }
                
                if (CommonUtils.isNotEmpty(importReleaseOn)) {
                    Date importReleaseOnDate = DateUtils.parseDate(importReleaseOn, "MM/dd/yyyy hh:mm a");
                    fclBl.setImportVerifiedEta(importReleaseOnDate);
                } else {
                    fclBl.setImportVerifiedEta(null);
                }
                if (CommonUtils.isNotEmpty(paymentReleaseOn)) {
                    Date paymentReleaseOnDate = DateUtils.parseDate(paymentReleaseOn, "MM/dd/yyyy hh:mm a");
                    fclBl.setPaymentReleasedOn(paymentReleaseOnDate);
                } else {
                    fclBl.setPaymentReleasedOn(null);
                }
                if ("Y".equalsIgnoreCase(paymentRelease)) {
                    if (null != loginUser) {
                        fclBl.setAmountAndPaidBy(loginUser.getLoginName());
                        return loginUser.getLoginName();
                    }
                } else {
                    fclBl.setAmountAndPaidBy("");
                }
            }
        }
        return "Updated";
    }

    public boolean checkCarrierForCost(String bolId, String carrierName) throws Exception {
        return new FclBlCostCodesDAO().checkCarrierForCost(bolId, carrierName);
    }

    public boolean getCostTransactionType(String bolId) throws Exception {
        return new FclBlCostCodesDAO().getCostTransactionType(bolId);
    }

    public String[] checkAPStatus(String bolId) throws Exception {
        List list = new ArrayList();
        String[] costDetails = new String[2];
        FclBlCostCodesDAO blCostCodesDAO = new FclBlCostCodesDAO();
        list = blCostCodesDAO.checkAPStatus(bolId);
        String apStatus = "";
        String assignedStatus = "";
        for (Iterator it = list.iterator(); it.hasNext();) {
            Object[] obj = (Object[]) it.next();
            if (obj[1] != null && obj[1] != "") {
                apStatus += obj[0].toString() + ",";
            } else {
                assignedStatus += obj[0].toString() + ",";
            }
        }
        costDetails[0] = assignedStatus.trim();
        costDetails[1] = apStatus.trim();
        return costDetails;
    }

    public String getUnlocDetail(String agentNo) throws Exception {
        String returnString = "";
        if (null != agentNo && !agentNo.equals("")) {
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(agentNo);
            if (tradingPartner != null) {
                Set tradingPartnerAddrSet = tradingPartner.getCustomerAddressSet();
                for (Object object : tradingPartnerAddrSet) {
                    CustomerAddress customerAddress = (CustomerAddress) object;
                    if (null != customerAddress.getUnLocCode() && !customerAddress.getUnLocCode().equals("")) {
                        returnString = "true";
                    } else {
                        returnString = "false";
                    }
                }
            }
        }
        return returnString;
    }

    public void FaeReCalculation(FclBillLaddingForm fclBillLaddingForm, MessageResources messageResources, HttpServletRequest request) throws Exception {
        Integer bol = Integer.parseInt(fclBillLaddingForm.getBol());
        String buttonValue = fclBillLaddingForm.getButtonValue();
        String costOrCharge = "";
        String code = "";
        if (buttonValue.equals("addChargesInfo") || buttonValue.equals("updateBlChargesInfo")) {
            FclBlCharges fclBlCharges = new FclBlCharges(fclBillLaddingForm);
            code = fclBlCharges.getChargeCode();
            costOrCharge = "charge";
        } else if (buttonValue.equals("addBlCost") || buttonValue.equals("updateBlCost")) {
            FclBlCostCodes fclBlCostCodes = new FclBlCostCodes(fclBillLaddingForm);
            code = fclBlCostCodes.getCostCode();
            costOrCharge = "cost";
        }
        List l = fclBlCostCodesDAO.findByPropertyAndBlNumber("costCode", "FAECOMM", bol);
        if (!l.isEmpty() && fclBlDAO.isFaeReCalculaionRequiredWhileDelete(bol, costOrCharge, code)) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) l.get(0);
            if (!"FAE".equalsIgnoreCase(fclBlCostCodes.getBookingFlag())) {
                FaeCalculation(bol, request);
            }
        }
    }

    public void FaeReCalculationWhileDelete(Integer bol, HttpServletRequest request) throws Exception {
        List l = fclBlCostCodesDAO.findByPropertyAndBlNumber("costCode", "FAECOMM", bol);
        if (!l.isEmpty()) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) l.get(0);
            if (!"FAE".equalsIgnoreCase(fclBlCostCodes.getBookingFlag())) {
                FaeCalculation(bol, request);
            }
        }
    }

    public void FaeCalculation(Integer bol, HttpServletRequest request) throws Exception {
        if (bol != null) {
            FclBl fclBl = fclBlDAO.findById(bol);
            if (null != fclBl && fclBl.getFinalDestination() != null) {
                StringFormatter stringFormatter = new StringFormatter();
                String unlocaCode = stringFormatter.getBreketValue(fclBl.getFinalDestination());
                List<Ports> portList = portsDAO.findPortUsingUnlocaCode(unlocaCode);
                if (portList != null && !portList.isEmpty()) {
                    Ports ports = portList.get(0);
                    List<FCLPortConfiguration> postConfigSetList = (ports.getFclPortConfigSet() != null) ? new ArrayList(ports.getFclPortConfigSet()) : Collections.EMPTY_LIST;
                    for (FCLPortConfiguration fclPortConfiguration : postConfigSetList) {
                        new FclBlUtil().calculateFAE(fclBl, request, fclPortConfiguration);
                        break;
                    }
                }
            }
        }
    }

    public String getHouseBlStatusOrOrginStatus(String documentName, String fileNo) throws Exception {
        return new DocumentStoreLogDAO().getHouseBlStatusOrOrginStatus(fileNo,
                "IMPORT FILE", documentName);
    }

    public void overPaidStatusforImports(FclBl fclbl) throws Exception {
        Double collectAmount;
        Double paidAmount;
        Object collectObject = new FclBlChargesDAO().sumOfCollectCharges(fclbl.getBol());
        collectAmount = null != collectObject ? (Double) collectObject : 0.0;
        Object paidObject = new PaymentReleaseDAO().sumOfPaidCharges(fclbl.getBol());
        paidAmount = null != paidObject ? (Double) paidObject : 0.0;
        boolean overPaidResult = null != collectObject && paidAmount > collectAmount; // only for collect(cosing) will check ovverpaid status
        fclBlDAO.setOverPaidStatusForImports(overPaidResult, fclbl.getFileNo());
    }

    public String getAcctName(String acctNo) throws Exception {
        String acctName = "";
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        if (null != acctNo && !acctNo.equals("")) {
            acctName = tradingPartnerDAO.getTradingpatnerAccName(acctNo);
        }
        return acctName;
    }

    public String validateMasterAccount(String accountNumber) {
        String result = "";
        String[] account = accountNumber.split(",");
        for (String string : account) {
            if (new TradingPartnerDAO().isMaster(string)) {
                if (!"".equals(result)) {
                    result = result + "," + string;
                } else {
                    result = string;
                }
            }
        }
        return result;
    }

    public List getsendCreditMemoList(String bolId) throws Exception {
        FclDAO fclDAO = new FclDAO();
        FclBlNew fclBl = fclDAO.findById((Integer.parseInt(bolId)));
        List billToList = new FclBlChargesDAO().getDistinctBillTo(bolId);
        List<CustomerContact> returnList = new ArrayList();
        if (billToList.size() > 0) {
            for (Iterator iter = billToList.iterator(); iter.hasNext();) {
                String Object = (String) iter.next();
                if (null != Object) {
                    if (Object.equalsIgnoreCase(CommonConstants.FORWARDER)) {
                        returnList.addAll(checkForE1andE2OfCodeC(fclBl.getForwardagentNo()));
                    } else if (Object.equalsIgnoreCase(CommonConstants.SHIPPER)) {
                        String customerNumber = ("I".equalsIgnoreCase(fclBl.getImportFlag())) ? fclBl.getShipperNo()
                                : fclBl.getHouseShipperNo();
                        returnList.addAll(checkForE1andE2OfCodeC(customerNumber));
                    } else if (Object.equalsIgnoreCase(CommonConstants.THIRDPARTY)) {
                        returnList.addAll(checkForE1andE2OfCodeC(fclBl.getBillTrdPrty()));
                    } else if (Object.equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
                        String customerNumber = fclBl.getHouseConsignee();
                        returnList.addAll(checkForE1andE2OfCodeC(customerNumber));
                    } else if (Object.equalsIgnoreCase(CommonConstants.AGENT)) {
                        String customerNumber = fclBl.getAgentNo();
                        returnList.addAll(checkForE1andE2OfCodeC(customerNumber));
                    }
                }
            }

        }
        if (CommonFunctions.isNotNull(fclBl.getSendCopyTo())) {
            CustomerContact customerContact = new CustomerContact();
            customerContact.setAccountName(fclBl.getSendCopyTo());
            returnList.add(customerContact);
        }
        return returnList;
    }

    public String getPortName(String port) throws Exception {
        // add country if any and remove UN Locaion code.
        StringBuilder portName = new StringBuilder();
        UnLocation location = null;
        if (null != port) {
            if (port.contains("(") && port.contains(")")) {
                String unLocCode = port.substring(port.lastIndexOf("(") + 1, port.lastIndexOf(")"));
                if (CommonUtils.isNotEmpty(unLocCode)) {
                    UnLocationDAO locationDAO = new UnLocationDAO();
                    location = locationDAO.getUnlocation(unLocCode);
                    if (null != location) {
                        portName.append(location.getUnLocationName());
                        if (null != location.getCountryId() && null != location.getCountryId().getCodedesc()) {
                            portName.append("/").append(location.getCountryId().getCodedesc());
                        }
                    }
                }
            }
        }
        return null != location ? portName.toString() : "";
    }

    public void deleteChargesAndCostAmountOnContainer(String fileNo, String bolId, String containerId,
            String sizeOfContainer, User user) throws Exception {
        int i = fileNo.indexOf("-");
        if (i != -1) {
            fileNo = fileNo.substring(0, i);
        }
        BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
        BookingfclUnits bookingfclUnits = new BookingfclUnits();
        Double amount = 0.0;
        Double oldAmount = 0.0;
        Integer conatinerNumber = 0;
        HashMap bookingMap = new HashMap();
        BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(fileNo);
        if (null != bookingFcl) {
            List bookingChargesList = bookingfclUnitsDAO.getChargesBasedOnContainerSize(bookingFcl.getBookingNumber(), sizeOfContainer);
            if (bookingChargesList.size() > 0) {
                Iterator iter = bookingChargesList.iterator();
                while (iter.hasNext()) {
                    bookingfclUnits = (BookingfclUnits) iter.next();
                    bookingMap.put(bookingfclUnits.getChgCode(), bookingfclUnits);
                }
            }
        }
        List blChargesList = fclBlChargesDAO.findByParentId(Integer.parseInt(bolId));
        if (blChargesList.size() > 0) {
            Iterator iter = blChargesList.iterator();
            while (iter.hasNext()) {
                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
                String chargeCode = fclBlCharges.getCharges();
                //---deduct booking amount from bl amount if the charge code exist for same container size-----
                if (bookingMap.containsKey(chargeCode)) {
                    BookingfclUnits bookingUnitsForCharges = (BookingfclUnits) bookingMap.get(chargeCode);
                    //--amount is not deducted if the container is added in bl i.e manually added container---
                    if (null != fclBlContainer.getManuallyAddedFlag() && fclBlContainer.getManuallyAddedFlag().equalsIgnoreCase("M")) {
                        amount = fclBlCharges.getAmount();
                        oldAmount = fclBlCharges.getOldAmount();
                    } else {
                        if (!"new".equals(bookingUnitsForCharges.getNewFlag())) {
                            amount = fclBlCharges.getAmount() - (bookingUnitsForCharges.getAmount() + bookingUnitsForCharges.getMarkUp()
                                    + bookingUnitsForCharges.getAdjustment());
                            oldAmount = fclBlCharges.getOldAmount() - (bookingUnitsForCharges.getAmount() + bookingUnitsForCharges.getMarkUp()
                                    + bookingUnitsForCharges.getAdjustment());
                        } else {
                            amount = fclBlCharges.getAmount() - (bookingUnitsForCharges.getMarkUp()
                                    + bookingUnitsForCharges.getAdjustment());
                            oldAmount = fclBlCharges.getOldAmount() - (bookingUnitsForCharges.getMarkUp()
                                    + bookingUnitsForCharges.getAdjustment());
                        }
                    }
                    fclBlCharges.setAmount(amount);
                    fclBlCharges.setOldAmount(oldAmount);
                }
                fclBlChargesDAO.update(fclBlCharges);
            }
        }
        //-----get costs from fclblcosts-----------
        List blcostsList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolId));
        if (blcostsList.size() > 0) {
            Iterator iter = blcostsList.iterator();
            while (iter.hasNext()) {
                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iter.next();
                String costCode = fclBlCostCodes.getCostCodeDesc();
                //---deduct booking amount from bl amount if the cost code exist for same container size-----
                if (bookingMap.containsKey(costCode)) {
                    BookingfclUnits bookingUnitsForCost = (BookingfclUnits) bookingMap.get(costCode);
                    //--amount is not deducted if the container is added in bl i.e manually added container---
                    if (null != fclBlContainer.getManuallyAddedFlag() && fclBlContainer.getManuallyAddedFlag().equalsIgnoreCase("M")) {
                        amount = fclBlCostCodes.getAmount();
                    } else if ((fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals("")) && fclBlCostCodes.getBookingFlag() == null) {
                        amount = fclBlCostCodes.getAmount();
                    } else {
                        amount = fclBlCostCodes.getAmount() - bookingUnitsForCost.getAmount();
                    }
                    fclBlCostCodes.setAmount(amount);
                } else {
                    if (costCode.equalsIgnoreCase(FclBlConstants.FFCODEDESC)) {
                        if (fclBlCostCodes.getAmount() != null) {
                            fclBlCostCodes.setAmount(fclBlCostCodes.getAmount() - conatinerNumber);
                        }
                    }
                }
                fclBlCostCodes.setTransactionType("");
                fclBlCostCodes.setManifestModifyFlag("yes");
                fclBlCostCodes.setProcessedStatus("");
                fclBlCostCodes.setAccrualsUpdatedBy(user.getLoginName());
                fclBlCostCodes.setAccrualsUpdatedDate(new Date());
                fclBlCostCodesDAO.update(fclBlCostCodes);
            }
        }
        //delete the transaction_ledger entries while delete the container
        FclBl fclBl = fclBlDAO.getFileNoObject(fileNo);
        List transactionLedgerList = transactionLedgerDAO.findByBolId(fclBl.getBolId());
        if (transactionLedgerList.size() > 0) {
            Iterator iterator = transactionLedgerList.iterator();
            while (iterator.hasNext()) {
                TransactionLedger transactionLedger = (TransactionLedger) iterator.next();
                transactionLedgerDAO.delete(transactionLedger);
            }
        }
    }

    public void notesForIncentCharge(Integer bol, HttpServletRequest request, String action, String chargeCode) throws Exception {
        HttpSession session = request.getSession();
        StringBuilder message = new StringBuilder();
        Date date = new Date();
        User loginUser = (User) session.getAttribute("loginuser");
        FclBl fclBl = fclBlDAO.findById(bol);
        FclBlChargesDAO chageDao = new FclBlChargesDAO();
        if (null != fclBl) {
            double amount = chargeCode.equals("INCENT") ? chageDao.getIncentAmount(bol) : chageDao.getAmountBychargeCode(bol, chargeCode);
            message.append(action).append(" -> Charge Code - ").append(chargeCode);
            message.append(" Charge - ").append(numberFormat.format(amount)).append(" USD ");
            Notes notes = new Notes();
            notes.setModuleId(NotesConstants.FILE);
            String fileNo = null != fclBl.getFileNo() ? fclBl.getFileNo() : "";
            notes.setModuleRefId(fileNo);
            notes.setNoteType(NotesConstants.AUTO);
            notes.setUpdateDate(date);
            notes.setUpdatedBy(loginUser.getLoginName());
            notes.setNoteDesc(message.toString());
            new NotesBC().saveNotes(notes);
        }
    }

    public String checkChargeMappingWithGL(String fileNumber, String transactionType, String importFlag) throws Exception {
        String chargeCode = "";
        String bundleChargeCode = "";
        List account = null;
        if ("true".equalsIgnoreCase(importFlag)) {
            bundleChargeCode = LoadApplicationProperties.getProperty("OceanFreightImports");
        } else {
            bundleChargeCode = LoadApplicationProperties.getProperty("OceanFreight");
        }
        if ("AR".equalsIgnoreCase(transactionType)) {
            account = fclBlChargesDAO.getAccountForChargeCodeFromGlAndTerminal(fileNumber);
        } else {
            account = fclBlChargesDAO.getAccountForCostCodeFromGlAndTerminal(fileNumber);
        }
        if (CommonUtils.isNotEmpty(account)) {
            for (Object obj1 : account) {
                Object[] obj = (Object[]) obj1;
                if (!(Boolean) obj[1]) {
                    if (chargeCode.equalsIgnoreCase("") && (bundleChargeCode.contains((String) obj[0]) || obj[2].equals("manual"))) {
                        chargeCode = (String) obj[0];
                    } else if (!chargeCode.contains((String) obj[0]) && (bundleChargeCode.contains((String) obj[0]) || obj[2].equals("manual"))) {
                        chargeCode = chargeCode + "," + (String) obj[0];
                    }
                }
            }
        }
        return chargeCode;
    }

    public String checkChargeAndCostMappingWithGL(String chargesCode, String fileNo, String transactionType, String screenName) throws Exception {
        boolean glAccountFlag = Boolean.TRUE;
        String chargeCode = "";
        if (screenName.equalsIgnoreCase("BL")) {
            glAccountFlag = bookingFclDAO.getAccountForCostAndChargeCodeBlFromGlAndTerminal(chargesCode, fileNo, transactionType);
        } else if (screenName.equalsIgnoreCase("BOOKING")) {
            glAccountFlag = bookingFclDAO.getAccountForCostAndChargeCodeBookingFromGlAndTerminal(chargesCode, fileNo, transactionType);
        } else if (screenName.equalsIgnoreCase("QUOTE")) {
            glAccountFlag = bookingFclDAO.getAccountForCostAndChargeCodeQtFromGlAndTerminal(chargesCode, fileNo, transactionType);
        }
        if (!glAccountFlag) {
            chargeCode = chargesCode;
        }
        return chargeCode;
    }

    public List<CustomerContact> checkForE1andE2OfCodek(String accountNo, String importFlag) throws Exception {
        List contactConfigList = new ArrayList();
        List resultList = new ArrayList();
        TradingPartner tradingPartner = new TradingPartner();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        if (null != accountNo) {
            tradingPartner = tradingPartnerDAO.findById(accountNo);
            String accountingEmail = tradingPartnerDAO.getAccountingEmail(accountNo);
            String accountingFax = tradingPartnerDAO.getAccountingFax(accountNo);
            if (tradingPartner != null) {
                contactConfigList.addAll(tradingPartner.getCustomerContact());
                for (Iterator iter = contactConfigList.iterator(); iter.hasNext();) {
                    CustomerContact customerContact = (CustomerContact) iter.next();
                    customerContact.setAccountName(tradingPartner.getAccountName());
                    customerContact.setAccountNo(tradingPartner.getAccountno());
                    customerContact.setAccountType(tradingPartner.getAcctType());
                    customerContact.setSubType(tradingPartner.getSubType());
                    String code = (null != customerContact.getCodek()) ? customerContact.getCodek().getCode() : null;
                    if ("I".equalsIgnoreCase(importFlag)) {
                        if (null != code && CommonUtils.in(code, "E", "F") && customerContact.isFclImports()) {
                            if (CommonUtils.isEqualIgnoreCase(customerContact.getEmail(), accountingEmail)
                                    || CommonUtils.isEqualIgnoreCase(customerContact.getFax(), accountingFax)) {
                                customerContact.setAccountingSelected(true);
                            }
                            resultList.add(customerContact);
                        }
                    } else {
                        if (null != code && CommonUtils.in(code, "E", "F") && customerContact.isFclExports()) {
                            if (CommonUtils.isEqualIgnoreCase(customerContact.getEmail(), accountingEmail)
                                    || CommonUtils.isEqualIgnoreCase(customerContact.getFax(), accountingFax)) {
                                customerContact.setAccountingSelected(true);
                            }
                            resultList.add(customerContact);
                        }
                    }
                }
            }
        }
        return resultList;
    }
}
