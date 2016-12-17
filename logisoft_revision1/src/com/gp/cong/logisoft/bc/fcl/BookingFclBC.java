package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.GenerateFileNumber;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.domain.BookingInbondDetails;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.FCLPortConfiguration;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.FclOrgDestMiscData;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.FCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.FclOrgDestMiscDataDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.reports.BookingCostSheetPdfCreator;
import com.gp.cong.logisoft.reports.BookingCoverSheetPdfCreater;
import com.gp.cong.logisoft.reports.BookingPdfCreator;
import com.gp.cong.logisoft.reports.ReferenceRequestPdfCreator;
import com.gp.cong.logisoft.reports.WorkOrderPdfCreator;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.FileNumberForQuotaionBLBooking;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.reports.dto.SearchBookingReportDTO;
import com.gp.cvst.logisoft.struts.form.EditBookingsForm;
import com.gp.cvst.logisoft.struts.form.NewBookingsForm;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.accounting.model.ManifestModel;
import com.logiware.common.dao.PropertyDAO;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.MessageResources;
import org.hibernate.SQLQuery;

public class BookingFclBC {

    BookingFclDAO bookingFclDAO = new BookingFclDAO();
    BookingfclUnitsDAO bookingFclUnitsDAO = new BookingfclUnitsDAO();
    BookingFcl bookingFcl = null;
    QuotationDAO quotationDAO = new QuotationDAO();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
    NumberFormat numb = new DecimalFormat("###,###,##0.00");
    DBUtil dbUtil = new DBUtil();
    CustAddress custAddress = new CustAddress();
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    CustAddressDAO custAddressDAO = new CustAddressDAO();
    UnLocationDAO unLocationDAO = new UnLocationDAO();
    CustomerDAO customerDAO = new CustomerDAO();
    FclBlDAO fclBlDAO = new FclBlDAO();
    FclBuyDAO fclBuyDAO = new FclBuyDAO();
    HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
    GenericCode gen = null;
    FclOrgDestMiscDataDAO fclOrgDestMiscDataDAO = new FclOrgDestMiscDataDAO();
    QuotationBC quotationBC = new QuotationBC();
    PortsDAO portsDAO = new PortsDAO();
    EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
    GeneralInformationDAO generalInformationDAO = new GeneralInformationDAO();
    NotesDAO notesDAO = new NotesDAO();
    FCLPortConfigurationDAO fCLPortConfigurationDAO = new FCLPortConfigurationDAO();
    StringFormatter stringFormatter = new StringFormatter();

    public void save(EmailSchedulerVO emailSchedulerVO) throws Exception {
        emailschedulerDAO.save(emailSchedulerVO);
    }

    public NewBookingsForm getDefaultValues(NewBookingsForm newBookingForm) {
        if (newBookingForm.getTotalCharges() == null || newBookingForm.getTotalCharges().equals("")) {
            newBookingForm.setTotalCharges("0.00");
        }
        if (newBookingForm.getBaht() == null || newBookingForm.getBaht().equals("")) {
            newBookingForm.setBaht("0.00");
        }
        if (newBookingForm.getBdt() == null || newBookingForm.getBdt().equals("")) {
            newBookingForm.setBdt("0.00");
        }
        if (newBookingForm.getCyp() == null || newBookingForm.getCyp().equals("")) {
            newBookingForm.setCyp("0.00");
        }
        if (newBookingForm.getEur() == null || newBookingForm.getEur().equals("")) {
            newBookingForm.setEur("0.00");
        }
        if (newBookingForm.getHkd() == null || newBookingForm.getHkd().equals("")) {
            newBookingForm.setHkd("0.00");
        }
        if (newBookingForm.getLkr() == null || newBookingForm.getLkr().equals("")) {
            newBookingForm.setLkr("0.00");
        }
        if (newBookingForm.getNt() == null || newBookingForm.getNt().equals("")) {
            newBookingForm.setNt("0.00");
        }
        if (newBookingForm.getPrs() == null || newBookingForm.getPrs().equals("")) {
            newBookingForm.setPrs("0.00");
        }
        if (newBookingForm.getRmb() == null || newBookingForm.getRmb().equals("")) {
            newBookingForm.setRmb("0.00");
        }
        if (newBookingForm.getWon() == null || newBookingForm.getWon().equals("")) {
            newBookingForm.setWon("0.00");
        }
        if (newBookingForm.getYen() == null || newBookingForm.getYen().equals("")) {
            newBookingForm.setYen("0.00");
        }
        if (newBookingForm.getMyr() == null || newBookingForm.getMyr().equals("")) {
            newBookingForm.setMyr("0.00");
        }
        if (newBookingForm.getNht() == null || newBookingForm.getNht().equals("")) {
            newBookingForm.setNht("0.00");
        }
        if (newBookingForm.getPkr() == null || newBookingForm.getPkr().equals("")) {
            newBookingForm.setPkr("0.00");
        }
        if (newBookingForm.getRm() == null || newBookingForm.getRm().equals("")) {
            newBookingForm.setRm("0.00");
        }
        if (newBookingForm.getSpo() == null || newBookingForm.getSpo().equals("")) {
            newBookingForm.setSpo("0.00");
        }
        if (newBookingForm.getVnd() == null || newBookingForm.getVnd().equals("")) {
            newBookingForm.setVnd("0.00");
        }
        if (newBookingForm.getInr() == null || newBookingForm.getInr().equals("")) {
            newBookingForm.setInr("0.00");
        }
        return newBookingForm;
    }

    public BookingFcl getBookingfcl(EditBookingsForm editBookingsForm) throws Exception {
        if (editBookingsForm.getBookingId() != null && !editBookingsForm.getBookingId().equals("")) {
            bookingFcl = bookingFclDAO.findById(Integer.parseInt(editBookingsForm.getBookingId()));
            if (bookingFcl == null) {
                bookingFcl = new BookingFcl();
            }
        } else {
            bookingFcl = new BookingFcl();
        }
        return bookingFcl;
    }

    public List getHazmatByBolId(int bolId) throws Exception {
        List hazmatMaterial = hazmatMaterialDAO.getHazmatByBolId(bolId);
        return hazmatMaterial;
    }

    public List getHazmatByBolId1(String code, int bolId) throws Exception {
        List hazmatMaterial = hazmatMaterialDAO.findbydoctypeid1(code, bolId);
        return hazmatMaterial;
    }

    public BookingFcl save(NewBookingsForm newBookingForm) throws Exception {
        if (newBookingForm.getBookingId() != null && !newBookingForm.getBookingId().equals("")) {
            bookingFcl = bookingFclDAO.findById(Integer.parseInt(newBookingForm.getBookingId()));
            if (bookingFcl == null) {
                bookingFcl = new BookingFcl();
            }
        } else {
            bookingFcl = new BookingFcl();
        }
        Date bookDate = null;
        if (newBookingForm.getBookingDate() != null && !newBookingForm.getBookingDate().trim().equals("")) {
            bookDate = simpleDateFormat.parse(newBookingForm.getBookingDate());
        } else {
            bookDate = new Date();
        }
        bookingFcl.setBookingDate(bookDate);
        bookingFcl.setRoutedByAgent(newBookingForm.getRoutedByAgent());
        bookingFcl.setAlternateAgent(newBookingForm.getAlternateAgent());
        bookingFcl.setDirectConsignmntCheck(newBookingForm.getDirectConsignmntCheck());
        bookingFcl.setShippercheck(newBookingForm.getShippercheck());
        bookingFcl.setForwardercheck(newBookingForm.getForwardercheck());
        bookingFcl.setConsigneecheck(newBookingForm.getConsigneecheck());
        bookingFcl.setSSBookingNo(newBookingForm.getSSBooking());
        bookingFcl.setContractReference(newBookingForm.getContractReference());
        bookingFcl.setQuoteNo(newBookingForm.getQuoteId());
        bookingFcl.setSalesRepCode(newBookingForm.getSlaesRepCode());
        bookingFcl.setSSLineBookingRepresentative(newBookingForm.getSSLineBookingRep());
        bookingFcl.setTelNo(newBookingForm.getTelePho());
        bookingFcl.setBookingemail(newBookingForm.getBookingemail());
        bookingFcl.setAttenName(newBookingForm.getAttenName());
        bookingFcl.setAgent(newBookingForm.getAgent());
        bookingFcl.setBookedBy(newBookingForm.getUserName());
        bookingFcl.setSslname(newBookingForm.getSslDescription());
        bookingFcl.setRampCheck(newBookingForm.getRampcheck());
        bookingFcl.setComcode(newBookingForm.getCommcode());
        bookingFcl.setComdesc(newBookingForm.getComdesc());
        bookingFcl.setVoyageCarrier(newBookingForm.getVoyage());
        bookingFcl.setVoyageInternal(newBookingForm.getVaoyageInternational());
        bookingFcl.setAccountName(newBookingForm.getAccountName());
        bookingFcl.setAccountNumber(newBookingForm.getAccountNumber());
        if (null == newBookingForm.getVesselNameCheck()) {
            bookingFcl.setVessel(newBookingForm.getVessel());
        } else {
            bookingFcl.setManualVesselName(newBookingForm.getManualVesselName());
        }
        Date etd = null;
        if (newBookingForm.getEstimatedDate() != null && !newBookingForm.getEstimatedDate().trim().equals("")) {
            etd = DateUtils.parseToDateForMonthMMM(newBookingForm.getEstimatedDate());
        } else {
            etd = null;
        }
        bookingFcl.setEtd(etd);
        Date eta = null;
        if (newBookingForm.getEstimatedAtten() != null && !newBookingForm.getEstimatedAtten().trim().equals("")) {
            eta = DateUtils.parseToDateForMonthMMM(newBookingForm.getEstimatedAtten());
        } else {
            eta = null;
        }
        bookingFcl.setEta(eta);
        Date railCutOff = null;
        if (newBookingForm.getRailCutOff() != null && !newBookingForm.getRailCutOff().trim().equals("")) {
            railCutOff = simpleDateFormat1.parse(newBookingForm.getRailCutOff());
        } else {
            railCutOff = null;
        }
        bookingFcl.setRailCutOff(railCutOff);
        Date portOfCutOff = null;
        if (newBookingForm.getPortCutOff() != null && !newBookingForm.getPortCutOff().trim().equals("")) {
            portOfCutOff = DateUtils.parseStringToDate(newBookingForm.getPortCutOff());
        } else {
            portOfCutOff = null;
        }
        bookingFcl.setPortCutOff(portOfCutOff);
        Date docCutOff = null;
        if (newBookingForm.getDocCutOff() != null && !newBookingForm.getDocCutOff().trim().equals("")) {
            docCutOff = simpleDateFormat1.parse(newBookingForm.getDocCutOff());
        } else {
            docCutOff = null;
        }
        bookingFcl.setDocCutOff(docCutOff);
        Date bargeCutOff = null;
        if (newBookingForm.getVoyageDocCutOff() != null && !newBookingForm.getVoyageDocCutOff().trim().equals("")) {
            bargeCutOff = simpleDateFormat1.parse(newBookingForm.getVoyageDocCutOff());
        } else {
            bargeCutOff = null;
        }
        bookingFcl.setVoyDocCutOff(bargeCutOff);
        Date autoCutOff = null;
        if (newBookingForm.getCutoffDate() != null && !newBookingForm.getCutoffDate().trim().equals("")) {
            autoCutOff = simpleDateFormat1.parse(newBookingForm.getCutoffDate());
        } else {
            autoCutOff = null;
        }
        Date vgmofCutOff = null;
        if (newBookingForm.getVgmCuttOff()!= null && !newBookingForm.getVgmCuttOff().trim().equals("")) {
            vgmofCutOff = DateUtils.parseStringToDate(newBookingForm.getVgmCuttOff());
        } else {
            vgmofCutOff = null;
        }
        bookingFcl.setVgmCuttOff(vgmofCutOff);
        bookingFcl.setCutofDate(autoCutOff);
        bookingFcl.setBilltoCode(newBookingForm.getBillToCode());
        bookingFcl.setBookingComplete(newBookingForm.getBookingComplete());
        bookingFcl.setPrepaidCollect(newBookingForm.getPrepaidToCollect());
        bookingFcl.setOriginTerminal(newBookingForm.getOriginTerminal());
        bookingFcl.setPortofOrgin(newBookingForm.getPortOfOrigin());
        bookingFcl.setExportDevliery(newBookingForm.getExportToDelivery());
        bookingFcl.setDestination(newBookingForm.getDestination());
        bookingFcl.setPortofDischarge(newBookingForm.getPortOfDischarge());
        bookingFcl.setMoveType(newBookingForm.getMoveType());
        bookingFcl.setShipNo(newBookingForm.getShipper());
        bookingFcl.setShipper(newBookingForm.getShipperName());
        bookingFcl.setAddressforShipper(newBookingForm.getShipperAddress());
        bookingFcl.setShipperCity(newBookingForm.getShipperCity());
        bookingFcl.setShipperState(newBookingForm.getShipperState());
        bookingFcl.setShipperZip(newBookingForm.getShipperZip());
        bookingFcl.setShipperCountry(newBookingForm.getShipperCountry());
        bookingFcl.setshipperPhone(newBookingForm.getShipPho());
        bookingFcl.setShipperEmail(newBookingForm.getShipEmai());
        bookingFcl.setShipperFax(newBookingForm.getShipperFax());
        bookingFcl.setShippercheck(newBookingForm.getShippercheck());
        bookingFcl.setForwNo(newBookingForm.getForwarder());
        bookingFcl.setForward(newBookingForm.getFowardername());
        bookingFcl.setAddressforForwarder(newBookingForm.getForwarderAddress());
        bookingFcl.setForwarderCity(newBookingForm.getForwarderCity());
        bookingFcl.setForwarderState(newBookingForm.getForwarderState());
        bookingFcl.setForwarderZip(newBookingForm.getForwarderZip());
        bookingFcl.setForwarderCountry(newBookingForm.getForwarderCountry());
        bookingFcl.setForwarderPhone(newBookingForm.getForwarderPhone());
        bookingFcl.setForwarderEmail(newBookingForm.getForwarderEmail());
        bookingFcl.setForwarderFax(newBookingForm.getForwarderFax());
        bookingFcl.setForwardercheck(newBookingForm.getForwardercheck());
        bookingFcl.setConsNo(newBookingForm.getConsignee());
        bookingFcl.setConsignee(newBookingForm.getConsigneename());
        bookingFcl.setAddressforConsingee(newBookingForm.getConsigneeAddress());
        bookingFcl.setConsigneeCity(newBookingForm.getConsigneeCity());
        bookingFcl.setConsigneeState(newBookingForm.getConsigneeState());
        bookingFcl.setConsigneeZip(newBookingForm.getConsigneeZip());
        bookingFcl.setConsigneeCountry(newBookingForm.getConsigneeCountry());
        bookingFcl.setConsingeePhone(newBookingForm.getconsigneephone());
        bookingFcl.setConsingeeEmail(newBookingForm.getConsigneeEmail());
        bookingFcl.setConsigneeFax(newBookingForm.getConsigneeFax());
        bookingFcl.setConsigneecheck(newBookingForm.getConsigneecheck());
        bookingFcl.setName(newBookingForm.getTruckerName());
        bookingFcl.setTruckerCode(newBookingForm.getTruckerCode());
        bookingFcl.setAddress(newBookingForm.getAddressoftrucker());
        bookingFcl.setTruckerPhone(newBookingForm.getTruckerPhone());
        bookingFcl.setTruckerEmail(newBookingForm.getTruckerEmail());
        if (newBookingForm.getCargoReadyDate() != null && !newBookingForm.getCargoReadyDate().trim().equals("")) {
            bookingFcl.setCargoReadyDate(sdf.parse(newBookingForm.getCargoReadyDate()));
        } else {
            bookingFcl.setCargoReadyDate(null);
        }
        if (newBookingForm.getEmpPickupDate() != null && !newBookingForm.getEmpPickupDate().trim().equals("")) {
            bookingFcl.setEmptyPickUpDate(DateUtils.parseDate(newBookingForm.getEmpPickupDate(), "MM/dd/yyyy"));
        } else {
            bookingFcl.setEmptyPickUpDate(null);
        }
        if (newBookingForm.getEarlierPickUpDate() != null && !newBookingForm.getEarlierPickUpDate().trim().equals("")) {
            bookingFcl.setEarliestPickUpDate(DateUtils.parseDate(newBookingForm.getEarlierPickUpDate(), "MM/dd/yyyy"));
        } else {
            bookingFcl.setEarliestPickUpDate(null);
        }
        bookingFcl.setExportPositoningPickup(newBookingForm.getExportPositioning());
        bookingFcl.setEmptypickupaddress(newBookingForm.getEmptypickupaddress());
        if (newBookingForm.getPostioningDate() != null && !newBookingForm.getPostioningDate().trim().equals("")) {
            bookingFcl.setPositioningDate(DateUtils.parseDate(newBookingForm.getPostioningDate(), "MM/dd/yyyy"));
        } else {
            bookingFcl.setPositioningDate(null);
        }
        bookingFcl.setLoadcontact(newBookingForm.getLoadcontact());
        bookingFcl.setPositionlocation(newBookingForm.getPositionlocation());
        bookingFcl.setLoadphone(newBookingForm.getLoadphone());
        bookingFcl.setAddressForExpPositioning(newBookingForm.getAddressofExpPosition());

        if (newBookingForm.getLoaddate() != null && !newBookingForm.getLoaddate().equals("")) {
            bookingFcl.setLoadDate(DateUtils.parseToDateForMonthMMM(newBookingForm.getLoaddate()));
        } else {
            bookingFcl.setLoadDate(null);
        }

        if (newBookingForm.getDateOutYard() != null && !newBookingForm.getDateOutYard().trim().equals("")) {
            bookingFcl.setDateoutYard(DateUtils.parseDate(newBookingForm.getDateOutYard(), "MM/dd/yyyy"));
        } else {
            bookingFcl.setDateoutYard(null);
        }

        if (newBookingForm.getDateInYard() != null && !newBookingForm.getDateInYard().trim().equals("")) {
            bookingFcl.setDateInYard(DateUtils.parseDate(newBookingForm.getDateInYard(), "MM/dd/yyyy"));
        } else {
            bookingFcl.setDateInYard(null);
        }

        bookingFcl.setLoadLocation(newBookingForm.getLoadLocation());
        bookingFcl.setAddessForExpDelivery(newBookingForm.getAddressofDelivery());
        bookingFcl.setSpecialequipment(newBookingForm.getSpecialequipment());
        bookingFcl.setHazmat(newBookingForm.getHazmat());
        bookingFcl.setOutofgage(newBookingForm.getOutofgate());
//        bookingFcl.setLocaldryage(newBookingForm.getLocaldryage());
        if (newBookingForm.getAmount() == null || newBookingForm.getAmount().equals("")) {
            newBookingForm.setAmount("0.00");
        }
        bookingFcl.setAmount(Double.parseDouble(dbUtil.removeComma(newBookingForm.getAmount())));
        bookingFcl.setIntermodel(newBookingForm.getIntermodel());
        if (newBookingForm.getAmount1() == null || newBookingForm.getAmount1().equals("")) {
            newBookingForm.setAmount1("0.00");
        }
        bookingFcl.setAmount1(Double.parseDouble(dbUtil.removeComma(newBookingForm.getAmount1())));
        bookingFcl.setInsurance(newBookingForm.getInsurance());
        if (newBookingForm.getCostofgoods1() == null || newBookingForm.getCostofgoods1().equals("")) {
            newBookingForm.setCostofgoods1("0.00");
        }
        bookingFcl.setCostofgoods(Double.parseDouble(dbUtil.removeComma(newBookingForm.getCostofgoods1())));
        if (newBookingForm.getInsurancamt() == null || newBookingForm.getInsurancamt().equals("")) {
            newBookingForm.setInsurancamt("0.00");
        }
        bookingFcl.setInsurancamt(Double.parseDouble(dbUtil.removeComma(newBookingForm.getInsurancamt())));
        bookingFcl.setGoodsDescription(newBookingForm.getGoodsDescription());
        bookingFcl.setRemarks(newBookingForm.getRemarks());
        if (bookingFcl.getFileNo() == null) {
            List fileNumberList = genericCodeDAO.findByCodeTypeid(46);
            if (fileNumberList != null && !fileNumberList.isEmpty()) {
                gen = (GenericCode) fileNumberList.get(0);
                if (gen != null) {
                    int fileNo = Integer.parseInt(gen.getCode());
                    fileNo++;
                    bookingFcl.setFileNo("" + fileNo);
                    gen.setCode(String.valueOf(fileNo));
                }

            }
        }
        return bookingFcl;
    }

    public void createBookingConfirmationReport(String id, String fileName,
            String contextPath, MessageResources messageResources, String simpleRequest, String regionRemarks, String fileToPrint,
            String documentName, String fromEmailAddress, String fromName, HttpServletRequest request) throws Exception {
        BookingFcl bookingfcl = null;
        bookingfcl = bookingFclDAO.findById(Integer.parseInt(id));
        BookingPdfCreator bookingPdfCreator = new BookingPdfCreator();
        SearchBookingReportDTO searchBookingReportDTO = new SearchBookingReportDTO();
        searchBookingReportDTO.setBookingflFcl(bookingfcl);
        searchBookingReportDTO.setFileName(fileName);
        searchBookingReportDTO.setContextPath(contextPath);
        searchBookingReportDTO.setRegionRemarks(regionRemarks);
        List containerList = null;
        containerList = bookingFclDAO.getBKGConfFieldsList1(id);
        searchBookingReportDTO.setObjectList(containerList);
        // Need to call PDF Creator.
        searchBookingReportDTO = setRatesList(searchBookingReportDTO, messageResources, bookingfcl);
        if ("Booking Cover Sheet".equalsIgnoreCase(documentName)) {
            new BookingCoverSheetPdfCreater().createReport(searchBookingReportDTO, simpleRequest,
                    messageResources, fileName, documentName);
        } else {
            String pdffileName = bookingPdfCreator.createReport(searchBookingReportDTO, simpleRequest,
                    messageResources, fileToPrint, documentName, fromEmailAddress, fromName, request);
        }
    }

    public void createBookingCostSheetReport(EditBookingsForm editBookingsForm, String fileName, String contextPath) throws Exception {
        BookingFcl bookingfcl = null;
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        bookingfcl = bookingFclDAO.findById(Integer.parseInt(editBookingsForm.getBookingNo()));
        SearchBookingReportDTO searchBookingReportDTO = new SearchBookingReportDTO();
        searchBookingReportDTO.setBookingflFcl(bookingfcl);
        searchBookingReportDTO.setFileName(fileName);
        searchBookingReportDTO.setContextPath(contextPath);
        List costSheetFieldlist = null;
        costSheetFieldlist = bookingFclDAO.getcostSheetFieldList(editBookingsForm.getBookingNo());
        /*if (costSheetFieldlist != null) {
        int i = costSheetFieldlist.size();
        SearchBookingReportDTO bookingReportDTO = (SearchBookingReportDTO) costSheetFieldlist.get(i - 1);
        String SRTotal = bookingReportDTO.getSellRateTotal();
        String BRTotal = bookingReportDTO.getBuyRateTotal();
        Double srtTotal = Double.valueOf(SRTotal);
        Double brtTotal = Double.valueOf(BRTotal);
        Double Profit = srtTotal - brtTotal;
        //setting profit.
        bookingReportDTO.setAmount1(number.format(Profit));
        }*/
        searchBookingReportDTO.setObjectList(costSheetFieldlist);
        BookingCostSheetPdfCreator costSheetPdfCreator = new BookingCostSheetPdfCreator();
        String costSheetfilename = costSheetPdfCreator.createReport(searchBookingReportDTO);
    }

    public BookingFcl update(EditBookingsForm editBookingForm, HttpSession session) throws Exception {
        FclBl fclBl = new FclBl();
        if (editBookingForm.getBookingId() != null && !editBookingForm.getBookingId().equals("")) {
            bookingFcl = bookingFclDAO.findById(Integer.parseInt(editBookingForm.getBookingId()));
            if (bookingFcl == null) {
                bookingFcl = new BookingFcl();
            }
        } else {
            bookingFcl = new BookingFcl();
            GenerateFileNumber generateFileNumber = new GenerateFileNumber();// wil generate file number
            generateFileNumber.join();// it wil force thread to complete the task before move to next step
            bookingFcl.setFileNo("" + generateFileNumber.getFileNumber());
            bookingFcl.setImportFlag(ImportBc.IMPORTFLAG);

        }
        if (editBookingForm.getBookingDate() != null && !editBookingForm.getBookingDate().trim().equals("")) {
            bookingFcl.setBookingDate(simpleDateFormat.parse(editBookingForm.getBookingDate()));
        } else {
            bookingFcl.setBookingDate(null);
        }
        if (editBookingForm.getCargoReadyDate() != null && !editBookingForm.getCargoReadyDate().trim().equals("")) {
            bookingFcl.setCargoReadyDate(sdf.parse(editBookingForm.getCargoReadyDate()));
        } else {
            bookingFcl.setCargoReadyDate(null);
        }
        bookingFcl.setBookingComments(editBookingForm.getBookingComments());
        if (CommonUtils.isNotEmpty(editBookingForm.getMoveType())) {
            bookingFcl.setMoveType(editBookingForm.getMoveType());
        } else {
            bookingFcl.setMoveType(bookingFcl.getMoveType());
        }
        bookingFcl.setRatesRemarks(editBookingForm.getRatesRemarks());
        bookingFcl.setFileType(editBookingForm.getFileType());
        bookingFcl.setSSBookingNo(editBookingForm.getSSBooking());
        bookingFcl.setZip(editBookingForm.getZip());
        bookingFcl.setRoutedbyAgentsCountry(editBookingForm.getRoutedAgentCountry());
        bookingFcl.setRampCity(editBookingForm.getRampCity());
        bookingFcl.setNoOfDays(editBookingForm.getNoOfDays());
        bookingFcl.setVoyageInternal(editBookingForm.getVaoyageInternational());
        bookingFcl.setDestRemarks(editBookingForm.getPortremarks());
        bookingFcl.setAlternateAgent(editBookingForm.getAlternateAgent());
        bookingFcl.setAgent(editBookingForm.getAgent());
        bookingFcl.setAgentNo(editBookingForm.getAgentNo());
        bookingFcl.setBookingContact(editBookingForm.getBookingContact());
        //--checking for manually added agent---
        if (null == editBookingForm.getAgentNo() || editBookingForm.getAgentNo().equals("")) {
            bookingFcl.setAgent("");
        }
        bookingFcl.setRoutedByAgent(editBookingForm.getRoutedByAgent());
        bookingFcl.setAccountName(editBookingForm.getAccountName());
        bookingFcl.setAccountNumber(editBookingForm.getAccountNumber());
        bookingFcl.setSSBookingNo(editBookingForm.getSSBooking());
        bookingFcl.setContractReference(editBookingForm.getContractReference());
        bookingFcl.setQuoteNo(editBookingForm.getQuoteId());
        bookingFcl.setSalesRepCode(editBookingForm.getSlaesRepCode());
        bookingFcl.setSSLineBookingRepresentative(editBookingForm.getSSLineBookingRep());
        bookingFcl.setTelNo(editBookingForm.getTelePho());
        bookingFcl.setBookingemail(editBookingForm.getBookingemail());
        bookingFcl.setAttenName(editBookingForm.getAttenName());
        bookingFcl.setBookedBy(editBookingForm.getUserName());
        bookingFcl.setSslname(editBookingForm.getSslDescription());
        bookingFcl.setComcode(editBookingForm.getCommcode());
        bookingFcl.setComdesc(editBookingForm.getComdesc());
        bookingFcl.setVoyageCarrier(editBookingForm.getVoyage());
        if(null !=editBookingForm.getButtonValue() && !"removedOldCFCLVoyageDetails".equalsIgnoreCase(editBookingForm.getButtonValue())) {
        bookingFcl.setVoyageInternal(editBookingForm.getVaoyageInternational());
        } else {
        bookingFcl.setVoyageInternal(null);
        }
        bookingFcl.setShippercheck(editBookingForm.getShippercheck());
        bookingFcl.setForwardercheck(editBookingForm.getForwardercheck());
        bookingFcl.setConsigneecheck(editBookingForm.getConsigneecheck());
        bookingFcl.setPrintGoodsDescription(editBookingForm.getPrintGoodsDescription());
        bookingFcl.setRoutedAgentCheck(editBookingForm.getRoutedAgentCheck());
        bookingFcl.setTimeCheckBox(editBookingForm.getTimeCheckBox());
//        bookingFcl.setSpecialEqpmtSelectBox(editBookingForm.getSpecialEqpmtSelectBox());
        bookingFcl.setCustomertoprovideSED(editBookingForm.getCustomertoprovideSED());
        bookingFcl.setDeductFFcomm(editBookingForm.getDeductFFcomm());
        if (null == editBookingForm.getVesselNameCheck()) {
            bookingFcl.setVessel(editBookingForm.getVessel());
            bookingFcl.setVesselNameCheck(editBookingForm.getVesselNameCheck());
            bookingFcl.setManualVesselName(null);
        } else {
            bookingFcl.setVesselNameCheck(editBookingForm.getVesselNameCheck());
            bookingFcl.setManualVesselName(editBookingForm.getManualVesselName());
            bookingFcl.setVessel(editBookingForm.getVessel());
        }
        if (editBookingForm.getEstimatedDate() != null && !editBookingForm.getEstimatedDate().equals("")) {
            bookingFcl.setEtd(DateUtils.parseToDate(editBookingForm.getEstimatedDate()));
        } else {
            bookingFcl.setEtd(null);
        }
        if (editBookingForm.getEstimatedAtten() != null && !editBookingForm.getEstimatedAtten().trim().equals("")) {
            bookingFcl.setEta(DateUtils.parseToDate(editBookingForm.getEstimatedAtten()));
        } else {
            bookingFcl.setEta(null);
        }

        if (editBookingForm.getRailCutOff() != null && !editBookingForm.getRailCutOff().trim().equals("")) {
            bookingFcl.setRailCutOff(simpleDateFormat.parse(editBookingForm.getRailCutOff()));
        } else {
            bookingFcl.setRailCutOff(null);
        }
        if (editBookingForm.getPortCutOff() != null && !editBookingForm.getPortCutOff().trim().equals("")) {
            bookingFcl.setPortCutOff(DateUtils.parseDate(editBookingForm.getPortCutOff(), "MM/dd/yyyy HH:mm a"));
        } else {
            bookingFcl.setPortCutOff(null);
        }

        if (editBookingForm.getDocCutOff() != null && !editBookingForm.getDocCutOff().trim().equals("")) {
            bookingFcl.setDocCutOff(DateUtils.parseDate(editBookingForm.getDocCutOff(), "MM/dd/yyyy HH:mm a"));
        } else {
            bookingFcl.setDocCutOff(null);
        }
        if (editBookingForm.getCarrierDocCut() != null && !editBookingForm.getCarrierDocCut().trim().equals("")) {
            bookingFcl.setCarrierDocCut(DateUtils.parseDate(editBookingForm.getCarrierDocCut(), "MM/dd/yyyy HH:mm a"));
        } else {
            bookingFcl.setCarrierDocCut(null);
        }
        if (editBookingForm.getVoyageDocCutOff() != null && !editBookingForm.getVoyageDocCutOff().trim().equals("")) {
            bookingFcl.setVoyDocCutOff(DateUtils.parseToDate(editBookingForm.getVoyageDocCutOff()));
        } else {
            bookingFcl.setVoyDocCutOff(null);
        }
        if (editBookingForm.getCutoffDate() != null && !editBookingForm.getCutoffDate().trim().equals("")) {
            bookingFcl.setCutofDate(DateUtils.parseToDate(editBookingForm.getCutoffDate()));
        } else {
            bookingFcl.setCutofDate(null);
        }
        if (editBookingForm.getVgmCuttOff()!= null && !editBookingForm.getVgmCuttOff().trim().equals("")) {
            bookingFcl.setVgmCuttOff(DateUtils.parseDate(editBookingForm.getVgmCuttOff(), "MM/dd/yyyy HH:mm a"));
        } else {
            bookingFcl.setVgmCuttOff(null);
        }
        bookingFcl.setBilltoCode(editBookingForm.getBillToCode());
        bookingFcl.setBookingComplete(editBookingForm.getBookingComplete());
        bookingFcl.setPrepaidCollect(editBookingForm.getPrepaidToCollect());
        bookingFcl.setOriginTerminal(editBookingForm.getOriginTerminal());
        bookingFcl.setPortofOrgin(editBookingForm.getPortOfOrigin());
        bookingFcl.setExportDevliery(editBookingForm.getExportToDelivery());
        bookingFcl.setDestination(editBookingForm.getDestination());
        bookingFcl.setPortofDischarge(editBookingForm.getPortOfDischarge());
        bookingFcl.setDirectConsignmntCheck(editBookingForm.getDirectConsignmntCheck());
        //bookingFcl.setMoveType(editBookingForm.getMoveType());
        bookingFcl.setShipNo(editBookingForm.getShipper());
        bookingFcl.setShipper(editBookingForm.getShipperName());
        bookingFcl.setAddressforShipper(editBookingForm.getShipperAddress());
        bookingFcl.setShipperCity(editBookingForm.getShipperCity());
        bookingFcl.setShipperState(editBookingForm.getShipperState());
        bookingFcl.setShipperZip(editBookingForm.getShipperZip());
        bookingFcl.setShipperCountry(editBookingForm.getShipperCountry());
        bookingFcl.setshipperPhone(editBookingForm.getShipPho());
        bookingFcl.setShipperEmail(editBookingForm.getShipEmai());
        bookingFcl.setShipperFax(editBookingForm.getShipperFax());
        bookingFcl.setShippercheck(editBookingForm.getShippercheck());
        bookingFcl.setForwNo(editBookingForm.getForwarder());
        bookingFcl.setForward(editBookingForm.getFowardername());
        bookingFcl.setAddressforForwarder(editBookingForm.getForwarderAddress());
        bookingFcl.setForwarderCity(editBookingForm.getForwarderCity());
        bookingFcl.setForwarderState(editBookingForm.getForwarderState());
        bookingFcl.setForwarderZip(editBookingForm.getForwarderZip());
        bookingFcl.setForwarderCountry(editBookingForm.getForwarderCountry());
        bookingFcl.setForwarderPhone(editBookingForm.getForwarderPhone());
        bookingFcl.setForwarderEmail(editBookingForm.getForwarderEmail());
        bookingFcl.setForwarderFax(editBookingForm.getForwarderFax());
        bookingFcl.setForwardercheck(editBookingForm.getForwardercheck());
        bookingFcl.setConsNo(editBookingForm.getConsignee());
        bookingFcl.setConsignee(editBookingForm.getConsigneename());
        bookingFcl.setAddressforConsingee(editBookingForm.getConsigneeAddress());
        bookingFcl.setConsigneeCity(editBookingForm.getConsigneeCity());
        bookingFcl.setConsigneeState(editBookingForm.getConsigneeState());
        bookingFcl.setConsigneeZip(editBookingForm.getConsigneeZip());
        bookingFcl.setConsigneeCountry(editBookingForm.getConsigneeCountry());
        bookingFcl.setConsingeePhone(editBookingForm.getConsigneePhone());
        bookingFcl.setConsingeeEmail(editBookingForm.getConsigneeEmail());
        bookingFcl.setConsigneeFax(editBookingForm.getConsigneeFax());
        bookingFcl.setConsigneecheck(editBookingForm.getConsigneecheck());
        bookingFcl.setTruckerCheckbox(editBookingForm.getTruckerCheckbox());
        bookingFcl.setName(editBookingForm.getTruckerName());
        bookingFcl.setTruckerCode(editBookingForm.getTruckerCode());
        bookingFcl.setAddress(editBookingForm.getAddressoftrucker());
        bookingFcl.setTruckerPhone(editBookingForm.getTruckerPhone());
        bookingFcl.setTruckerEmail(editBookingForm.getTruckerEmail());
        bookingFcl.setShipperClientReference(editBookingForm.getShipperClientReference());
        bookingFcl.setForwarderClientReference(editBookingForm.getForwarderClientReference());
        bookingFcl.setConsigneeClientReference(editBookingForm.getConsigneeClientReference());
        bookingFcl.setTruckerClientReference(editBookingForm.getTruckerClientReference());
        bookingFcl.setPickUpRemarks(editBookingForm.getPickUpRemarks());
        bookingFcl.setLoadRemarks(editBookingForm.getLoadRemarks());
        bookingFcl.setReturnRemarks(editBookingForm.getReturnRemarks());
        bookingFcl.setCarrierPrint(null == editBookingForm.getCarrierPrint() ? "off" : editBookingForm.getCarrierPrint());
        String code = "";
        String desc = "";
        User user = new User();
        String userName = null;
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userName = user.getLoginName();
        }
        bookingFcl.setUpdateBy(userName);
        bookingFcl.setUpdatedDate(new Date());
        if (editBookingForm.getBookingComplete() != null && editBookingForm.getBookingComplete().equalsIgnoreCase("y")) {
            bookingFcl.setBookingCompletedBy(userName);
        }

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date bookingCompletedDate = new Date();
        dateFormat.format(bookingCompletedDate);
        //---set only when BKG Complete is Yes----
        if (editBookingForm.getBookingComplete() != null && editBookingForm.getBookingComplete().equals("Y")) {
            bookingFcl.setBookingCompletedDate(bookingCompletedDate);
        }

        if (editBookingForm.getEmpPickupDate() != null && !editBookingForm.getEmpPickupDate().trim().equals("")) {
            bookingFcl.setEmptyPickUpDate(DateUtils.parseToDate(editBookingForm.getEmpPickupDate()));
        } else {
            bookingFcl.setEmptyPickUpDate(null);
        }
        if (editBookingForm.getEarlierPickUpDate() != null && !editBookingForm.getEarlierPickUpDate().trim().equals("")) {
            bookingFcl.setEarliestPickUpDate(DateUtils.parseToDate(editBookingForm.getEarlierPickUpDate()));
        } else {
            bookingFcl.setEarliestPickUpDate(null);
        }
        bookingFcl.setExportPositoningPickup(editBookingForm.getExportPositioning());
        bookingFcl.setEmptypickupaddress(editBookingForm.getEmptypickupaddress());
        Date positioningDate = null;
        if (editBookingForm.getPostioningDate() != null && !editBookingForm.getPostioningDate().equals("")) {
            if (null != editBookingForm.getTimeCheckBox() && !editBookingForm.getTimeCheckBox().equals("")) {
                positioningDate = DateUtils.parseToDate(editBookingForm.getPostioningDate());
            } else {
                positioningDate = DateUtils.parseDate(editBookingForm.getPostioningDate(), "MM/dd/yyyy HH:mm a");
            }
        } else {
            positioningDate = null;
        }
        bookingFcl.setPositioningDate(positioningDate);
        bookingFcl.setLoadcontact(editBookingForm.getLoadcontact());
        bookingFcl.setPositionlocation(editBookingForm.getPositionlocation());
        bookingFcl.setLoadphone(editBookingForm.getLoadphone());
        bookingFcl.setAddressForExpPositioning(editBookingForm.getAddressofExpPosition());
        bookingFcl.setSpotAddrCity(editBookingForm.getSpotAddrCity());
        bookingFcl.setSpotAddrState(editBookingForm.getSpotAddrState());
        bookingFcl.setSpotAddrZip(editBookingForm.getSpotAddrZip());
        Date loadDate = null;
        if (editBookingForm.getLoaddate() != null && !editBookingForm.getLoaddate().trim().equals("")) {
            loadDate = DateUtils.parseToDateForMonthMMM(editBookingForm.getLoaddate());
        } else {
            loadDate = null;
        }
        Date outOfYardDate = null;
        if (editBookingForm.getDateOutYard() != null && !editBookingForm.getDateOutYard().trim().equals("")) {
            outOfYardDate = DateUtils.parseToDate(editBookingForm.getDateOutYard());
        } else {
            outOfYardDate = null;
        }

        Date dateIntoYard = null;
        if (editBookingForm.getDateInYard() != null && !editBookingForm.getDateInYard().trim().equals("")) {
            dateIntoYard = DateUtils.parseToDate(editBookingForm.getDateInYard());
        } else {
            dateIntoYard = null;
        }
        bookingFcl.setLoadDate(loadDate);
        bookingFcl.setDateInYard(dateIntoYard);
        bookingFcl.setDateoutYard(outOfYardDate);
        bookingFcl.setLoadLocation(editBookingForm.getLoadLocation());
        bookingFcl.setAddessForExpDelivery(editBookingForm.getAddressofDelivery());
        bookingFcl.setSpecialequipment(editBookingForm.getSpecialequipment());
        if (CommonFunctions.isNotNull(editBookingForm.getHazmat())) {
            bookingFcl.setHazmat(editBookingForm.getHazmat());
        } else {
            bookingFcl.setHazmat(bookingFcl.getHazmat());
        }

        bookingFcl.setOutofgage(editBookingForm.getOutofgate());
//        bookingFcl.setLocaldryage(editBookingForm.getLocaldryage());
        if (editBookingForm.getAmount() == null || editBookingForm.getAmount().equals("")) {
            editBookingForm.setAmount("0.00");
        }
        bookingFcl.setAmount(Double.parseDouble(dbUtil.removeComma(editBookingForm.getAmount())));
        bookingFcl.setIntermodel(editBookingForm.getIntermodel());
        if (editBookingForm.getAmount1() == null || editBookingForm.getAmount1().equals("")) {
            editBookingForm.setAmount1("0.00");
        }
        bookingFcl.setAmount1(Double.parseDouble(dbUtil.removeComma(editBookingForm.getAmount1())));
        bookingFcl.setInsurance(editBookingForm.getInsurance());
        if (editBookingForm.getCostofgoods() == null || editBookingForm.getCostofgoods().equals("")) {
            editBookingForm.setCostofgoods("0.00");
        }
        bookingFcl.setCostofgoods(Double.parseDouble(dbUtil.removeComma(editBookingForm.getCostofgoods())));
        if (editBookingForm.getInsurancamt() == null || editBookingForm.getInsurancamt().equals("")) {
            editBookingForm.setInsurancamt("0.00");
        }
        bookingFcl.setInland(editBookingForm.getInland());
        bookingFcl.setDocCharge(editBookingForm.getDocCharge());
        bookingFcl.setInsurancamt(Double.parseDouble(dbUtil.removeComma(editBookingForm.getInsurancamt())));
        bookingFcl.setGoodsDescription(editBookingForm.getGoodsDescription());
        bookingFcl.setRemarks(editBookingForm.getRemarks());
        bookingFcl.setIssuingTerminal(editBookingForm.getIssuingTerminal());
        bookingFcl.setDoorOrigin(editBookingForm.getDoorOrigin());
        bookingFcl.setDoorDestination(editBookingForm.getDoorDestination());
        if (null != editBookingForm.getRatesNonRates()) {
            bookingFcl.setRatesNonRates(editBookingForm.getRatesNonRates());
        }
        if (null != editBookingForm.getBreakBulk()) {
            bookingFcl.setBreakBulk(editBookingForm.getBreakBulk());
        }
        bookingFcl.setSpotRate(editBookingForm.getSpotRate());
        bookingFcl.setLineMove(editBookingForm.getLineMove());
        bookingFcl.setTruckerCity(editBookingForm.getTruckerCity());
        bookingFcl.setTruckerState(editBookingForm.getTruckerState());
        bookingFcl.setTruckerZip(editBookingForm.getTruckerZip());
        bookingFcl.setDocumentsReceived(editBookingForm.getDocumentsReceived());
        bookingFcl.setSpottingAccountName(editBookingForm.getSpottingAccountName());
        bookingFcl.setSpottingAccountNo(editBookingForm.getSpottingAccountNo());
        bookingFcl.setWareHouseCode(editBookingForm.getWareHouseTemp());
        bookingFcl.setEquipmentReturnName(editBookingForm.getEquipmentReturnName());
        bookingFcl.setEquipmentReturnCode(editBookingForm.getEquipmentReturnTemp());
        bookingFcl.setRampCheck(editBookingForm.getRampCheck());

        //--CHECKBOXES THAT DISABLES THE DOJO---
        bookingFcl.setShipperTpCheck(editBookingForm.getShipperTpCheck());
        bookingFcl.setTruckerTpCheck(editBookingForm.getTruckerTpCheck());
        bookingFcl.setSpottAddrTpCheck(editBookingForm.getSpottAddrTpCheck());
        bookingFcl.setConsigneeTpCheck(editBookingForm.getConsigneeTpCheck());
        bookingFcl.setContactNameCheck(editBookingForm.getContactNameCheck());
        //--for Checked Container details
//        if (editBookingForm.getCollapseid() != null && editBookingForm.getCollapseid().equals("expand") && editBookingForm.getExpandCheck() != null) {
//           bookingFcl.setContainerSize(editBookingForm.getExpandCheck().length);
//        }else if (editBookingForm.getCollapseid() != null && editBookingForm.getCollapseid().equals("collapse") && editBookingForm.getCollapseCheck() != null) {
//           bookingFcl.setContainerSize(editBookingForm.getCollapseCheck().length);
//        }

        bookingFcl.setShipperPoa(editBookingForm.getShipperPoa());
        bookingFcl.setConsigneePoa(editBookingForm.getConsigneePoa());
        bookingFcl.setForwarderPoa(editBookingForm.getForwarderPoa());
        bookingFcl.setBrand(editBookingForm.getBrand());

        //------MARK UP's---------
        if (editBookingForm.getDrayMarkUp() == null || editBookingForm.getDrayMarkUp().equals("")) {
            editBookingForm.setDrayMarkUp("0.0");
        }
        bookingFcl.setDrayMarkUp(Double.parseDouble(dbUtil.removeComma(editBookingForm.getDrayMarkUp())));
        if (editBookingForm.getInterMarkUp() == null || editBookingForm.getInterMarkUp().equals("")) {
            editBookingForm.setInterMarkUp("0.0");
        }
        bookingFcl.setInterMarkUp(Double.parseDouble(dbUtil.removeComma(editBookingForm.getInterMarkUp())));
        if (editBookingForm.getInsureMarkUp() == null || editBookingForm.getInsureMarkUp().equals("")) {
            editBookingForm.setInsureMarkUp("0.0");
        }
        bookingFcl.setInsureMarkUp(Double.parseDouble(dbUtil.removeComma(editBookingForm.getInsureMarkUp())));
        if (bookingFcl.getFileNo() == null) {
            List fileNumberList = genericCodeDAO.findByCodeTypeid(46);
            if (fileNumberList != null && !fileNumberList.isEmpty()) {
                gen = (GenericCode) fileNumberList.get(0);
                if (gen != null) {
                    int fileNo = Integer.parseInt(gen.getCode());
                    fileNo++;
                    bookingFcl.setFileNo("" + fileNo);
                    gen.setCode(String.valueOf(fileNo));
                }
            }
        }
        // to update voyage and third party fields in Bl with voyageInternal field of Booking
        fclBl = fclBlDAO.getFileNoObject(bookingFcl.getFileNo().toString());
        List addressList = null;
        if (fclBl != null && fclBl.getFileNo() != null && fclBl.getFileNo().equals(bookingFcl.getFileNo())) {

            fclBl.setVoyages(bookingFcl.getVoyageCarrier());
            fclBl.setThirdPartyName(bookingFcl.getAccountName());
            fclBl.setBillTrdPrty(bookingFcl.getAccountNumber());
            fclBl.setVaoyageInternational(bookingFcl.getVoyageInternal());
            fclBl.setBookingNo(bookingFcl.getSSBookingNo());
            addressList = custAddressDAO.getPrimaryCustomerAddress(bookingFcl.getAccountName());
            for (Iterator iter = addressList.iterator(); iter.hasNext();) {
                custAddress = (CustAddress) iter.next();
                fclBl.setBillThirdPartyAddress(custAddress.getAddress1());
            }
            fclBlDAO.update(fclBl);
        }
        if (CommonFunctions.isNotNull(editBookingForm.getFileNo())) {
            Quotation quotation = quotationDAO.getFileNoObject(editBookingForm.getFileNo());
            if (null != quotation) {
                quotation.setRoutedbymsg(editBookingForm.getRoutedByAgent());
                quotationDAO.update(quotation);
            }

        }
        //Update Inland vendor as per trucker code
//        if(CommonUtils.isNotEmpty(editBookingForm.getTruckerCode()) && CommonUtils.isNotEmpty(editBookingForm.getBookingId())){
//            bookingFclUnitsDAO.updateInlandVendor(editBookingForm.getBookingId(), editBookingForm.getTruckerCode(), editBookingForm.getTruckerName());
//        }
        return bookingFcl;
    }

    public BookingfclUnits addCharges(NewBookingsForm newBookingsForm) throws Exception {
        BookingfclUnits bookingfclUnits = new BookingfclUnits();
        GenericCode gen = genericCodeDAO.findById(Integer.parseInt(newBookingsForm.getUnitSelect()));
        bookingfclUnits.setUnitType(gen);
        bookingfclUnits.setNumbers(newBookingsForm.getNumber());
        bookingfclUnits.setAmount(Double.parseDouble(dbUtil.removeComma(newBookingsForm.getChargeAmt())));
        bookingfclUnits.setMinimum(Double.parseDouble(dbUtil.removeComma(newBookingsForm.getMinimumAmt())));
        if (newBookingsForm.getCurrency1() != null && !newBookingsForm.getCurrency1().equals("0")) {

            GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(newBookingsForm.getCurrency1()));
            bookingfclUnits.setCurrency(genericCode.getCode());
            bookingfclUnits.setCurrency1(genericCode);
        }
        if (newBookingsForm.getChargeCodeDesc() != null && !newBookingsForm.getChargeCodeDesc().equals("0")) {
            GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(newBookingsForm.getChargeCodeDesc()));
            bookingfclUnits.setChgCode(genericCode.getCodedesc());
            bookingfclUnits.setChargeCodeDesc(genericCode.getCode());
            bookingfclUnits.setChargeCode(genericCode);
        }
        if (newBookingsForm.getCostSelect() != null && !newBookingsForm.getCostSelect().equals("0")) {
            GenericCode gen1 = genericCodeDAO.findById(Integer.parseInt(newBookingsForm.getCostSelect()));
            bookingfclUnits.setCostType(gen1.getCodedesc());
            bookingfclUnits.setCosttype(gen1);
        }
        bookingfclUnits.setMarkUp(0.00);
        bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
        bookingfclUnits.setBuyRate(0.00);
        bookingfclUnits.setProfit(bookingfclUnits.getSellRate() - bookingfclUnits.getBuyRate());
        bookingfclUnits.setManualCharges("Y");
        return bookingfclUnits;
    }

    public BookingfclUnits addChargesForEditBookingForm(EditBookingsForm editBookingsForm) throws Exception {
        BookingfclUnits bookingfclUnits = new BookingfclUnits();
        GenericCode gen = genericCodeDAO.findById(Integer.parseInt(editBookingsForm.getUnitSelect()));
        bookingfclUnits.setUnitType(gen);
        bookingfclUnits.setNumbers(editBookingsForm.getNumber());
        bookingfclUnits.setAmount(Double.parseDouble(dbUtil.removeComma(editBookingsForm.getChargeAmt())));
        bookingfclUnits.setMinimum(Double.parseDouble(dbUtil.removeComma(editBookingsForm.getMinimumAmt())));
        if (editBookingsForm.getCurrency1() != null && !editBookingsForm.getCurrency1().equals("0")) {
            GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(editBookingsForm.getCurrency1()));
            bookingfclUnits.setCurrency(genericCode.getCode());
            bookingfclUnits.setCurrency1(genericCode);
        }
        if (editBookingsForm.getChargeCodeDesc() != null && !editBookingsForm.getChargeCodeDesc().equals("0")) {
            GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(editBookingsForm.getChargeCodeDesc()));
            bookingfclUnits.setChgCode(genericCode.getCodedesc());
            bookingfclUnits.setChargeCodeDesc(genericCode.getCode());
            bookingfclUnits.setChargeCode(genericCode);
        }
        if (editBookingsForm.getCostSelect() != null && !editBookingsForm.getCostSelect().equals("0")) {
            GenericCode gen1 = genericCodeDAO.findById(Integer.parseInt(editBookingsForm.getCostSelect()));
            bookingfclUnits.setCostType(gen1.getCodedesc());
            bookingfclUnits.setCosttype(gen1);
        }
        bookingfclUnits.setMarkUp(0.00);
        bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
        bookingfclUnits.setBuyRate(0.00);
        bookingfclUnits.setProfit(bookingfclUnits.getSellRate() - bookingfclUnits.getBuyRate());
        bookingfclUnits.setManualCharges("Y");
        return bookingfclUnits;
    }

    public void chargesSave(List chargesList, BookingFcl bookingFcl, HttpServletRequest request) throws Exception {
        List deletedList = bookingFclUnitsDAO.getbookingfcl2(bookingFcl.getBookingId().toString());
        for (Iterator iterator = deletedList.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
            bookingFclUnitsDAO.delete(bookingfclUnits);
        }
        boolean falg = false;
        for (Iterator iter = chargesList.iterator(); iter.hasNext();) {
            String outOfGage = "";
            BookingfclUnits bookingfclUnits = new BookingfclUnits();
            bookingfclUnits = (BookingfclUnits) iter.next();
            BookingfclUnits saveBookingfclUnits = new BookingfclUnits();
            PropertyUtils.copyProperties(saveBookingfclUnits, bookingfclUnits);
            saveBookingfclUnits.setId(null);
            if (null == bookingfclUnits.getSpecialEquipmentUnit()) {
                bookingfclUnits.setSpecialEquipmentUnit("");
            }
            if (null == bookingfclUnits.getStandardCharge()) {
                bookingfclUnits.setStandardCharge("");
            }
            if ("R".equals(bookingFcl.getRatesNonRates()) && "OCNFRT".equalsIgnoreCase(bookingfclUnits.getChargeCodeDesc()) && null != bookingfclUnits.getUnitType()) {
                if (null != bookingfclUnits.getUnitType()) {
                    outOfGage = request.getParameter("outOfGageOCNFRT" + bookingfclUnits.getUnitType().getCodedesc() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge());
                    if (CommonUtils.isEmpty(outOfGage)) {
                        outOfGage = request.getParameter("outOfGage1OCNFRT" + bookingfclUnits.getUnitType().getCodedesc() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge());
                    }
                }
                if (CommonUtils.isNotEmpty(outOfGage)) {
                    saveBookingfclUnits.setOutOfGauge(outOfGage);
                } else {
                    saveBookingfclUnits.setOutOfGauge("N");
                }
                List l = bookingFclUnitsDAO.getChargeByEquipmentUnit("" + bookingFcl.getBookingId(), bookingfclUnits.getUnitType().getId(), bookingfclUnits.getStandardCharge());
                if (!l.isEmpty()) {
                    for (Iterator it = l.iterator(); it.hasNext();) {
                        BookingfclUnits b = (BookingfclUnits) it.next();
                        b.setOutOfGauge(bookingfclUnits.getOutOfGauge());
                    }
                }
            } else {
                if (null != bookingfclUnits.getUnitType()) {
                    outOfGage = request.getParameter("outOfGage" + bookingfclUnits.getChargeCodeDesc() + bookingfclUnits.getUnitType().getCodedesc() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge());
                    if (CommonUtils.isEmpty(outOfGage)) {
                        outOfGage = request.getParameter("outOfGage1" + bookingfclUnits.getChargeCodeDesc() + bookingfclUnits.getUnitType().getCodedesc() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge());
                    }
                }
                if (CommonUtils.isNotEmpty(outOfGage)) {
                    saveBookingfclUnits.setOutOfGauge(outOfGage);
                    List l = bookingFclUnitsDAO.getChargeByEquipmentUnit("" + bookingFcl.getBookingId(), bookingfclUnits.getUnitType().getId(), bookingfclUnits.getStandardCharge());
                    if (!l.isEmpty()) {
                        for (Iterator it = l.iterator(); it.hasNext();) {
                            BookingfclUnits b = (BookingfclUnits) it.next();
                            b.setOutOfGauge(bookingfclUnits.getOutOfGauge());
                        }
                    }
                } else {
                    saveBookingfclUnits.setOutOfGauge("N");
                }
            }
            saveBookingfclUnits.setBookingNumber(String.valueOf(bookingFcl.getBookingId()));
            if (saveBookingfclUnits.getAmount().equals("Infinity")) {
                saveBookingfclUnits.setAmount(0.00);
            }
            bookingFclUnitsDAO.save(saveBookingfclUnits);

        }

    }

    public void otherChargesSave(List chargesList, BookingFcl bookingFcl) throws Exception {
        for (Iterator iter = chargesList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            BookingfclUnits saveBookingfclUnits = new BookingfclUnits();
            PropertyUtils.copyProperties(saveBookingfclUnits, bookingfclUnits);
            saveBookingfclUnits.setBookingNumber(String.valueOf(bookingFcl.getBookingId()));
            saveBookingfclUnits.setId(null);
            bookingFclUnitsDAO.save(saveBookingfclUnits);
        }
    }

    public NewBookingsForm saveCurrenciesIntoNewBookingForm(List chargesList, NewBookingsForm newBookingsForm) throws Exception {
        Double tCharges = 0.00;
        Double baht = 0.00;
        Double bdt = 0.00;
        Double cyp = 0.00;
        Double eur = 0.00;
        Double hkd = 0.00;
        Double lkr = 0.00;
        Double nt = 0.00;
        Double prs = 0.00;
        Double rmb = 0.00;
        Double won = 0.00;
        Double yen = 0.00;
        Double myr = 0.00;
        Double nht = 0.00;
        Double pkr = 0.00;
        Double rm = 0.00;
        Double spo = 0.00;
        Double vnd = 0.00;
        Double inr = 0.00;
        for (Iterator iter = chargesList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("USD")) {
                tCharges = tCharges + bookingfclUnits.getAmount();
                tCharges = tCharges + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("BAHT")) {
                baht = baht + bookingfclUnits.getAmount();
                baht = baht + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("BDT")) {
                bdt = bdt + bookingfclUnits.getAmount();
                bdt = bdt + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("CYP")) {
                cyp = cyp + bookingfclUnits.getAmount();
                cyp = cyp + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("EUR")) {
                eur = eur + bookingfclUnits.getAmount();
                eur = eur + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("HKD")) {
                hkd = hkd + bookingfclUnits.getAmount();
                hkd = hkd + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("LKR")) {
                lkr = lkr + bookingfclUnits.getAmount();
                lkr = lkr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("NT")) {
                nt = nt + bookingfclUnits.getAmount();
                nt = nt + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("PRS")) {
                prs = prs + bookingfclUnits.getAmount();
                prs = prs + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("RMB")) {
                rmb = rmb + bookingfclUnits.getAmount();
                rmb = rmb + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("WON")) {
                won = won + bookingfclUnits.getAmount();
                won = won + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("YEN")) {
                yen = yen + bookingfclUnits.getAmount();
                yen = yen + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("MYR")) {
                myr = myr + bookingfclUnits.getAmount();
                myr = myr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("NHT")) {
                nht = nht + bookingfclUnits.getAmount();
                nht = nht + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("PKR")) {
                pkr = pkr + bookingfclUnits.getAmount();
                pkr = pkr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("RM")) {
                rm = rm + bookingfclUnits.getAmount();
                rm = rm + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("SPO")) {
                spo = spo + bookingfclUnits.getAmount();
                spo = spo + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("VND")) {
                vnd = vnd + bookingfclUnits.getAmount();
                vnd = vnd + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("INR")) {
                inr = inr + bookingfclUnits.getAmount();
                inr = inr + bookingfclUnits.getMarkUp();
            }
        }
        newBookingsForm.setTotalCharges(numb.format(tCharges));
        newBookingsForm.setBaht(numb.format(baht));
        newBookingsForm.setBdt(numb.format(bdt));
        newBookingsForm.setCyp(numb.format(cyp));
        newBookingsForm.setEur(numb.format(eur));
        newBookingsForm.setHkd(numb.format(hkd));
        newBookingsForm.setLkr(numb.format(lkr));
        newBookingsForm.setNt(numb.format(nt));
        newBookingsForm.setPrs(numb.format(prs));
        newBookingsForm.setRmb(numb.format(rmb));
        newBookingsForm.setWon(numb.format(won));
        newBookingsForm.setYen(numb.format(yen));
        newBookingsForm.setMyr(numb.format(myr));
        newBookingsForm.setNht(numb.format(nht));
        newBookingsForm.setPkr(numb.format(pkr));
        newBookingsForm.setRm(numb.format(rm));
        newBookingsForm.setSpo(numb.format(spo));
        newBookingsForm.setVnd(numb.format(vnd));
        newBookingsForm.setInr(numb.format(inr));
        return newBookingsForm;
    }

    public BookingFcl saveCurrenciesIntoNewBookingFcl(List chargesList, BookingFcl bookingFcl) throws Exception {
        Double tCharges = 0.00;
        Double baht = 0.00;
        Double bdt = 0.00;
        Double cyp = 0.00;
        Double eur = 0.00;
        Double hkd = 0.00;
        Double lkr = 0.00;
        Double nt = 0.00;
        Double prs = 0.00;
        Double rmb = 0.00;
        Double won = 0.00;
        Double yen = 0.00;
        Double myr = 0.00;
        Double nht = 0.00;
        Double pkr = 0.00;
        Double rm = 0.00;
        Double spo = 0.00;
        Double vnd = 0.00;
        Double inr = 0.00;
        for (Iterator iter = chargesList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getMarkUp() == null) {
                bookingfclUnits.setMarkUp(0.00);
            }
            if (bookingfclUnits.getAmount() == null) {
                bookingfclUnits.setAmount(0.00);
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("USD")) {
                tCharges = tCharges + bookingfclUnits.getAmount();
                tCharges = tCharges + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("BAHT")) {
                baht = baht + bookingfclUnits.getAmount();
                baht = baht + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("BDT")) {
                bdt = bdt + bookingfclUnits.getAmount();
                bdt = bdt + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("CYP")) {
                cyp = cyp + bookingfclUnits.getAmount();
                cyp = cyp + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("EUR")) {
                eur = eur + bookingfclUnits.getAmount();
                eur = eur + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("HKD")) {
                hkd = hkd + bookingfclUnits.getAmount();
                hkd = hkd + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("LKR")) {
                lkr = lkr + bookingfclUnits.getAmount();
                lkr = lkr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("NT")) {
                nt = nt + bookingfclUnits.getAmount();
                nt = nt + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("PRS")) {
                prs = prs + bookingfclUnits.getAmount();
                prs = prs + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("RMB")) {
                rmb = rmb + bookingfclUnits.getAmount();
                rmb = rmb + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("WON")) {
                won = won + bookingfclUnits.getAmount();
                won = won + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("YEN")) {
                yen = yen + bookingfclUnits.getAmount();
                yen = yen + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("MYR")) {
                myr = myr + bookingfclUnits.getAmount();
                myr = myr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("NHT")) {
                nht = nht + bookingfclUnits.getAmount();
                nht = nht + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("PKR")) {
                pkr = pkr + bookingfclUnits.getAmount();
                pkr = pkr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("RM")) {
                rm = rm + bookingfclUnits.getAmount();
                rm = rm + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("SPO")) {
                spo = spo + bookingfclUnits.getAmount();
                spo = spo + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("VND")) {
                vnd = vnd + bookingfclUnits.getAmount();
                vnd = vnd + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("INR")) {
                inr = inr + bookingfclUnits.getAmount();
                inr = inr + bookingfclUnits.getMarkUp();
            }
        }
        bookingFcl.setTotalCharges(tCharges);
        bookingFcl.setBaht(baht);
        bookingFcl.setBdt(bdt);
        bookingFcl.setCyp(cyp);
        bookingFcl.setEur(eur);
        bookingFcl.setHkd(hkd);
        bookingFcl.setLkr(lkr);
        bookingFcl.setNt(nt);
        bookingFcl.setPrs(prs);
        bookingFcl.setRmb(rmb);
        bookingFcl.setWon(won);
        bookingFcl.setYen(yen);
        bookingFcl.setMyr(myr);
        bookingFcl.setNht(nht);
        bookingFcl.setPkr(pkr);
        bookingFcl.setRm(rm);
        bookingFcl.setSpo(spo);
        bookingFcl.setVnd(vnd);
        bookingFcl.setInr(inr);
        return bookingFcl;
    }

    public NewBookingsForm saveCurrenciesofOtherChargesIntoNewBookingForm(List chargesList, NewBookingsForm newBookingsForm) throws Exception {
        newBookingsForm = getDefaultValues(newBookingsForm);
        Double tCharges = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getTotalCharges()));
        Double baht = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getBaht()));
        Double bdt = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getBdt()));
        Double cyp = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getCyp()));
        Double eur = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getEur()));
        Double hkd = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getHkd()));
        Double lkr = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getLkr()));
        Double nt = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getNt()));
        Double prs = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getPrs()));
        Double rmb = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getRmb()));
        Double won = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getWon()));
        Double yen = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getYen()));
        Double myr = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getMyr()));
        Double nht = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getNht()));
        Double pkr = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getPkr()));
        Double rm = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getRm()));
        Double spo = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getSpo()));
        Double vnd = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getVnd()));
        Double inr = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getInr()));
        for (Iterator iter = chargesList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("USD")) {
                tCharges = tCharges + bookingfclUnits.getAmount();
                tCharges = tCharges + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("BAHT")) {
                baht = baht + bookingfclUnits.getAmount();
                baht = baht + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("BDT")) {
                bdt = bdt + bookingfclUnits.getAmount();
                bdt = bdt + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("CYP")) {
                cyp = cyp + bookingfclUnits.getAmount();
                cyp = cyp + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("EUR")) {
                eur = eur + bookingfclUnits.getAmount();
                eur = eur + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("HKD")) {
                hkd = hkd + bookingfclUnits.getAmount();
                hkd = hkd + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("LKR")) {
                lkr = lkr + bookingfclUnits.getAmount();
                lkr = lkr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("NT")) {
                nt = nt + bookingfclUnits.getAmount();
                nt = nt + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("PRS")) {
                prs = prs + bookingfclUnits.getAmount();
                prs = prs + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("RMB")) {
                rmb = rmb + bookingfclUnits.getAmount();
                rmb = rmb + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("WON")) {
                won = won + bookingfclUnits.getAmount();
                won = won + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("YEN")) {
                yen = yen + bookingfclUnits.getAmount();
                yen = yen + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("MYR")) {
                myr = myr + bookingfclUnits.getAmount();
                myr = myr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("NHT")) {
                nht = nht + bookingfclUnits.getAmount();
                nht = nht + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("PKR")) {
                pkr = pkr + bookingfclUnits.getAmount();
                pkr = pkr + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("RM")) {
                rm = rm + bookingfclUnits.getAmount();
                rm = rm + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("SPO")) {
                spo = spo + bookingfclUnits.getAmount();
                spo = spo + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("VND")) {
                vnd = vnd + bookingfclUnits.getAmount();
                vnd = vnd + bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("INR")) {
                inr = inr + bookingfclUnits.getAmount();
                inr = inr + bookingfclUnits.getMarkUp();
            }
        }
        newBookingsForm.setTotalCharges(numb.format(tCharges));
        newBookingsForm.setBaht(numb.format(baht));
        newBookingsForm.setBdt(numb.format(bdt));
        newBookingsForm.setCyp(numb.format(cyp));
        newBookingsForm.setEur(numb.format(eur));
        newBookingsForm.setHkd(numb.format(hkd));
        newBookingsForm.setLkr(numb.format(lkr));
        newBookingsForm.setNt(numb.format(nt));
        newBookingsForm.setPrs(numb.format(prs));
        newBookingsForm.setRmb(numb.format(rmb));
        newBookingsForm.setWon(numb.format(won));
        newBookingsForm.setYen(numb.format(yen));
        newBookingsForm.setMyr(numb.format(myr));
        newBookingsForm.setNht(numb.format(nht));
        newBookingsForm.setPkr(numb.format(pkr));
        newBookingsForm.setRm(numb.format(rm));
        newBookingsForm.setSpo(numb.format(spo));
        newBookingsForm.setVnd(numb.format(vnd));
        newBookingsForm.setInr(numb.format(inr));
        return newBookingsForm;
    }

    public BookingFcl saveCurrenciesofOtherChargesIntoBookignFcl(List chargesList, BookingFcl bookingFcl) throws Exception {
        Double tCharges = bookingFcl.getTotalCharges();
        Double baht = bookingFcl.getBaht();
        Double bdt = bookingFcl.getBdt();
        Double cyp = bookingFcl.getCyp();
        Double eur = bookingFcl.getEur();
        Double hkd = bookingFcl.getHkd();
        Double lkr = bookingFcl.getLkr();
        Double nt = bookingFcl.getNt();
        Double prs = bookingFcl.getPrs();
        Double rmb = bookingFcl.getRmb();
        Double won = bookingFcl.getWon();
        Double yen = bookingFcl.getYen();
        Double myr = bookingFcl.getMyr();
        Double nht = bookingFcl.getNht();
        Double pkr = bookingFcl.getPkr();
        Double rm = bookingFcl.getRm();
        Double spo = bookingFcl.getSpo();
        Double vnd = bookingFcl.getVnd();
        Double inr = bookingFcl.getInr();
        for (Iterator iter = chargesList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("USD")) {
                if (bookingfclUnits.getAmount() != null) {
                    tCharges = tCharges + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    tCharges = tCharges + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("BAHT")) {
                if (bookingfclUnits.getAmount() != null) {
                    baht = baht + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    baht = baht + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("BDT")) {
                if (bookingfclUnits.getAmount() != null) {
                    bdt = bdt + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    bdt = bdt + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("CYP")) {
                if (bookingfclUnits.getAmount() != null) {
                    cyp = cyp + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    cyp = cyp + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("EUR")) {
                if (bookingfclUnits.getAmount() != null) {
                    eur = eur + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    eur = eur + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("HKD")) {
                if (bookingfclUnits.getAmount() != null) {
                    hkd = hkd + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    hkd = hkd + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("LKR")) {
                if (bookingfclUnits.getAmount() != null) {
                    lkr = lkr + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    lkr = lkr + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("NT")) {
                if (bookingfclUnits.getAmount() != null) {
                    nt = nt + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    nt = nt + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("PRS")) {
                if (bookingfclUnits.getAmount() != null) {
                    prs = prs + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    prs = prs + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("RMB")) {
                if (bookingfclUnits.getAmount() != null) {
                    rmb = rmb + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    rmb = rmb + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("WON")) {
                if (bookingfclUnits.getAmount() != null) {
                    won = won + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    won = won + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("YEN")) {
                if (bookingfclUnits.getAmount() != null) {
                    yen = yen + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    yen = yen + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("MYR")) {
                if (bookingfclUnits.getAmount() != null) {
                    myr = myr + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    myr = myr + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("NHT")) {
                if (bookingfclUnits.getAmount() != null) {
                    nht = nht + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    nht = nht + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("PKR")) {
                if (bookingfclUnits.getAmount() != null) {
                    pkr = pkr + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    pkr = pkr + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("RM")) {
                if (bookingfclUnits.getAmount() != null) {
                    rm = rm + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    rm = rm + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("SPO")) {
                if (bookingfclUnits.getAmount() != null) {
                    spo = spo + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    spo = spo + bookingfclUnits.getMarkUp();
                }

            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("VND")) {
                if (bookingfclUnits.getAmount() != null) {
                    vnd = vnd + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    vnd = vnd + bookingfclUnits.getMarkUp();
                }
            }
            if (bookingfclUnits.getCurrency() != null && bookingfclUnits.getCurrency().trim().equals("INR")) {
                if (inr == null) {
                    inr = 0.00;
                }
                if (bookingfclUnits.getAmount() != null) {
                    inr = inr + bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    inr = inr + bookingfclUnits.getMarkUp();
                }
            }
        }
        bookingFcl.setTotalCharges(tCharges);
        bookingFcl.setBaht(baht);
        bookingFcl.setBdt(bdt);
        bookingFcl.setCyp(cyp);
        bookingFcl.setEur(eur);
        bookingFcl.setHkd(hkd);
        bookingFcl.setLkr(lkr);
        bookingFcl.setNt(nt);
        bookingFcl.setPrs(prs);
        bookingFcl.setRmb(rmb);
        bookingFcl.setWon(won);
        bookingFcl.setYen(yen);
        bookingFcl.setMyr(myr);
        bookingFcl.setNht(nht);
        bookingFcl.setPkr(pkr);
        bookingFcl.setRm(rm);
        bookingFcl.setSpo(spo);
        bookingFcl.setVnd(vnd);
        bookingFcl.setInr(inr);
        return bookingFcl;
    }

    public List setMarkupValuesIntoCharges(List chargesList, NewBookingsForm newBookingsForm) throws Exception {
        for (int i = 0; i < chargesList.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) chargesList.get(i);
            if (newBookingsForm.getChargeMarkUp()[i] != null) {
                bookingfclUnits.setMarkUp(Double.parseDouble(dbUtil.removeComma((newBookingsForm.getChargeMarkUp()[i]))));
                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                bookingfclUnits.setBuyRate(0.00);
                bookingfclUnits.setProfit(bookingfclUnits.getSellRate() - bookingfclUnits.getBuyRate());
            }
        }
        return chargesList;
    }

    public List setMarkupValuesIntoChargesEditBookingForm(List chargesList, EditBookingsForm editBookingsForm, String userName) throws Exception {
        String unitType = "";
        String containerNo = "";
        String tempUnitType = "";
        String specialEquip = "";
        String standardCharge = "";
        if (!editBookingsForm.getCollapseid().equals("")) {
            for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
                bookingfclUnits.setApproveBl("No");
            }
        }
        if (editBookingsForm.getCollapseid() != null && editBookingsForm.getCollapseid().equals("collapse")) {
            for (int i = 0; i < editBookingsForm.getHiddennumbers().length; i++) {
                if (null != editBookingsForm.getSplEqpUnitsCollapse()[i]) {
                    specialEquip = editBookingsForm.getSplEqpUnitsCollapse()[i];
                }
                if (null != editBookingsForm.getStandardChargeCollapse()[i]) {
                    standardCharge = editBookingsForm.getStandardChargeCollapse()[i];
                }
                if (!unitType.equalsIgnoreCase(editBookingsForm.getHiddenunitType()[i] + "-" + specialEquip + "-" + standardCharge)) {
                    containerNo = editBookingsForm.getHiddennumbers()[i];
                    tempUnitType = editBookingsForm.getHiddenunitType()[i] + "-" + specialEquip + "-" + standardCharge;
                    for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                        BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
                        if (null == bookingfclUnits.getSpecialEquipmentUnit()) {
                            bookingfclUnits.setSpecialEquipmentUnit("");
                        }
                        if (null == bookingfclUnits.getStandardCharge()) {
                            bookingfclUnits.setStandardCharge("");
                        }
                        if (null != bookingfclUnits.getUnitType() && tempUnitType.equalsIgnoreCase(bookingfclUnits.getUnitType().getCodedesc() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge())) {
                            bookingfclUnits.setNumbers(containerNo);
                        }
                    }
                }
                unitType = editBookingsForm.getHiddenunitType()[i] + "-" + specialEquip + "-" + standardCharge;
            }
        } else if (editBookingsForm.getCollapseid() != null && editBookingsForm.getCollapseid().equals("expand")) {
            if (editBookingsForm.getNumbers() != null) {
                for (int i = 0; i < editBookingsForm.getNumbers().length; i++) {
                    if (null != editBookingsForm.getSplEqpUnits()[i]) {
                        specialEquip = editBookingsForm.getSplEqpUnits()[i];
                    }
                    if (null != editBookingsForm.getStandardCharge()[i]) {
                        standardCharge = editBookingsForm.getStandardCharge()[i];
                    }
                    if (!unitType.equalsIgnoreCase(editBookingsForm.getUnitType()[i] + "-" + specialEquip + "-" + standardCharge)) {
                        containerNo = editBookingsForm.getNumbers()[i];
                        tempUnitType = editBookingsForm.getUnitType()[i] + "-" + specialEquip + "-" + standardCharge;
                        for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
                            if (null == bookingfclUnits.getSpecialEquipmentUnit()) {
                                bookingfclUnits.setSpecialEquipmentUnit("");
                            }
                            if (null == bookingfclUnits.getStandardCharge()) {
                                bookingfclUnits.setStandardCharge("");
                            }
                            if (null != bookingfclUnits.getUnitType() && tempUnitType.equalsIgnoreCase(bookingfclUnits.getUnitType().getCodedesc() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge())) {
                                bookingfclUnits.setNumbers(containerNo);
                            }
                        }
                    }
                    unitType = editBookingsForm.getUnitType()[i] + "-" + specialEquip + "-" + standardCharge;
                }
            }
        }
        if (editBookingsForm.getCollapseid() != null && editBookingsForm.getCollapseid().equals("expand")) {

            if (editBookingsForm.getExpandCheck() != null) {
                for (int i = 0; i < editBookingsForm.getExpandCheck().length; i++) {
                    String id = editBookingsForm.getExpandCheck()[i];
                    String specialEquipment = "";
                    if (null != editBookingsForm.getCheckSplEqpUnits() && editBookingsForm.getCheckSplEqpUnits().length > i && null != editBookingsForm.getCheckSplEqpUnits()[i]) {
                        specialEquipment = editBookingsForm.getCheckSplEqpUnits()[i];
                    }
                    String standardCheck = "";
                    if (null != editBookingsForm.getCheckStandardCharge() && editBookingsForm.getCheckStandardCharge().length > i && null != editBookingsForm.getCheckStandardCharge()[i]) {
                        standardCheck = editBookingsForm.getCheckStandardCharge()[i];
                    }
                    BookingfclUnits bookingfclUnits = bookingFclUnitsDAO.findById(Integer.parseInt(id));
                    if (bookingfclUnits != null) {
                        String unitName = bookingfclUnits.getUnitType().getId().toString();
                        for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                            BookingfclUnits tempBookingfclUnits = (BookingfclUnits) iterator.next();
                            String eqpUnit = "";
                            if (null != tempBookingfclUnits.getSpecialEquipmentUnit()) {
                                eqpUnit = tempBookingfclUnits.getSpecialEquipmentUnit();
                            }
                            String standard = "";
                            if (null != tempBookingfclUnits.getStandardCharge()) {
                                standard = tempBookingfclUnits.getStandardCharge();
                            }
                            if (tempBookingfclUnits.getUnitType() != null && tempBookingfclUnits.getUnitType().getId().toString().equals(unitName)
                                    && specialEquipment.equals(eqpUnit) && standard.equalsIgnoreCase(standardCheck)) {
                                tempBookingfclUnits.setApproveBl("Yes");
                            }
                        }
                    }
                }
            } else {
                for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                    BookingfclUnits tempBookingfclUnits = (BookingfclUnits) iterator.next();
                    tempBookingfclUnits.setApproveBl("No");
                }
            }
        } else if (editBookingsForm.getCollapseid() != null && editBookingsForm.getCollapseid().equals("collapse")) {
            if (editBookingsForm.getCollapseCheck() != null) {
                for (int i = 0; i < editBookingsForm.getCollapseCheck().length; i++) {
                    String id = editBookingsForm.getCollapseCheck()[i];
                    String specialEquipment = "";
                    if (null != editBookingsForm.getCheckSplEqpUnitsCollapse() && editBookingsForm.getCheckSplEqpUnitsCollapse().length > i && null != editBookingsForm.getCheckSplEqpUnitsCollapse()[i]) {
                        specialEquipment = editBookingsForm.getCheckSplEqpUnitsCollapse()[i];
                    }
                    String standardCheck = "";
                    if (null != editBookingsForm.getCheckStandardChargeCollapse() && editBookingsForm.getCheckStandardChargeCollapse().length > i && null != editBookingsForm.getCheckStandardChargeCollapse()[i]) {
                        standardCheck = editBookingsForm.getCheckStandardChargeCollapse()[i];
                    }
                    BookingfclUnits bookingfclUnits = bookingFclUnitsDAO.findById(Integer.parseInt(id));
                    if (bookingfclUnits != null) {
                        String unitName = bookingfclUnits.getUnitType().getId().toString();
                        for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                            BookingfclUnits tempBookingfclUnits = (BookingfclUnits) iterator.next();
                            String eqpUnit = "";
                            if (null != tempBookingfclUnits.getSpecialEquipmentUnit()) {
                                eqpUnit = tempBookingfclUnits.getSpecialEquipmentUnit();
                            }
                            String standard = "";
                            if (null != tempBookingfclUnits.getStandardCharge()) {
                                standard = tempBookingfclUnits.getStandardCharge();
                            }
                            if (tempBookingfclUnits.getUnitType() != null && tempBookingfclUnits.getUnitType().getId().toString().equals(unitName)
                                    && specialEquipment.equals(eqpUnit) && standard.equalsIgnoreCase(standardCheck)) {
                                tempBookingfclUnits.setApproveBl("Yes");
                            }
                        }
                    }
                }
            } else {
                for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                    BookingfclUnits tempBookingfclUnits = (BookingfclUnits) iterator.next();
                    tempBookingfclUnits.setApproveBl("No");
                }
            }
        }
        if (editBookingsForm.getCollapseid() != null && editBookingsForm.getCollapseid().equals("collapse")) {
            if (editBookingsForm.getHiddenchargeCddesc() != null) {
                for (int i = 0; i < editBookingsForm.getHiddenchargeCddesc().length; i++) {
                    String chargeCode = editBookingsForm.getHiddenchargeCddesc()[i];
                    String unittype = editBookingsForm.getHiddenunitType()[i];
                    String specialEquipment = "";
                    if (null != editBookingsForm.getSplEqpUnitsCollapse()[i]) {
                        specialEquipment = editBookingsForm.getSplEqpUnitsCollapse()[i];
                    }
                    String standardIndex = "";
                    if (null != editBookingsForm.getStandardChargeCollapse()[i]) {
                        standardIndex = editBookingsForm.getStandardChargeCollapse()[i];
                    }
                    for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                        BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
                        String eqpUnit = "";
                        String standard = "";
                        if (null != bookingfclUnits.getSpecialEquipmentUnit()) {
                            eqpUnit = bookingfclUnits.getSpecialEquipmentUnit();
                        }
                        if (null != bookingfclUnits.getStandardCharge()) {
                            standard = bookingfclUnits.getStandardCharge();
                        }
                        if (bookingfclUnits.getUnitType() != null && bookingfclUnits.getChargeCodeDesc().equals(chargeCode)
                                && bookingfclUnits.getUnitType().getCodedesc().equals(unittype)
                                && specialEquipment.equals(eqpUnit) && standard.equals(standardIndex)) {
                            if (bookingfclUnits.getNewFlag() != null && bookingfclUnits.getNewFlag().equals("D")) {
                                bookingfclUnits.setAmount(bookingfclUnits.getAmount());
                            } else {
                                bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp());
                                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                            }
                            bookingfclUnits.setBuyRate(0.00);
                            bookingfclUnits.setProfit(bookingfclUnits.getSellRate() - bookingfclUnits.getBuyRate());
                            String buttonValue = (editBookingsForm.getButtonValue() != null ? editBookingsForm.getButtonValue() : "");
                            if (editBookingsForm.getHiddenAdjustment() != null) {
                                if (!("adjustmentChargeComments".equals(buttonValue))) {
                                    bookingfclUnits.setAdjustment(Double.parseDouble(dbUtil.removeComma(editBookingsForm.getHiddenAdjustment()[i])));
                                }
                                bookingfclUnits.setUpdateBy(userName);
                                bookingfclUnits.setUpdateOn(new Date());
                            }
                            //for saving bundle into OFR-----
                            String bundle = editBookingsForm.getBundleOfr();
                            if (!bundle.equals("")) {
                                String[] print = bundle.split(",");
                                if (print.length > i && print[i].equalsIgnoreCase("1") && (!bookingfclUnits.getChargeCodeDesc().equals("OCNFRT") || !bookingfclUnits.getChargeCodeDesc().equals("OFIMP"))) {
                                    bookingfclUnits.setPrint("on");
                                } else {
                                    bookingfclUnits.setPrint("off");
                                }
                            }
                        }
                    }
                }
            }

        } else if (editBookingsForm.getCollapseid() != null && editBookingsForm.getCollapseid().equals("expand")) {
            if (editBookingsForm.getChargeCddesc() != null) {
                for (int i = 0; i < editBookingsForm.getChargeCddesc().length; i++) {
                    String chargeCode = editBookingsForm.getChargeCddesc()[i];
                    String unittype = editBookingsForm.getUnitType()[i];
                    String specialEquipment = "";
                    if (null != editBookingsForm.getSplEqpUnits()[i]) {
                        specialEquipment = editBookingsForm.getSplEqpUnits()[i];
                    }
                    String standardIndex = "";
                    if (null != editBookingsForm.getStandardCharge()[i]) {
                        standardIndex = editBookingsForm.getStandardCharge()[i];
                    }
                    for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                        BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
                        String eqpUnit = "";
                        if (null != bookingfclUnits.getSpecialEquipmentUnit()) {
                            eqpUnit = bookingfclUnits.getSpecialEquipmentUnit();
                        }
                        String standard = "";
                        if (null != bookingfclUnits.getStandardCharge()) {
                            standard = bookingfclUnits.getStandardCharge();
                        }
                        if (bookingfclUnits.getUnitType() != null && bookingfclUnits.getChargeCodeDesc().equals(chargeCode) && bookingfclUnits.getUnitType().getCodedesc().equals(unittype)
                                && specialEquipment.equals(eqpUnit) && standard.equals(standardIndex)) {

                            if (bookingfclUnits.getChgCode() != null && !bookingfclUnits.getChgCode().equals("INSURANCE") && bookingfclUnits.getNewFlag() != null && bookingfclUnits.getNewFlag().equals("new")) {
                                bookingfclUnits.setMarkUp(Double.parseDouble(dbUtil.removeComma((editBookingsForm.getSellRate()[i]))));
                            } else {
                                if (bookingfclUnits.getChgCode() != null && !bookingfclUnits.getChgCode().equals("INSURANCE") && bookingfclUnits.getNewFlag() != null && (bookingfclUnits.getNewFlag().equals("FF") || bookingfclUnits.getNewFlag().equals("IN"))) {
                                    bookingfclUnits.setAmount(Double.parseDouble(dbUtil.removeComma((editBookingsForm.getChargeMarkUp()[i]))));
                                    bookingfclUnits.setMarkUp(Double.parseDouble(dbUtil.removeComma((editBookingsForm.getChargeAmount()[i]))));
                                } else if (bookingfclUnits.getChgCode() != null && !bookingfclUnits.getChgCode().equals("INSURANCE") && bookingfclUnits.getNewFlag() != null && bookingfclUnits.getNewFlag().equals("D")) {
                                    bookingfclUnits.setAmount(Double.parseDouble(dbUtil.removeComma((editBookingsForm.getChargeMarkUp()[i]))));
                                }
                            }
                            if (editBookingsForm.getSpotRate().equals("N")) {
                                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                                bookingfclUnits.setBuyRate(0.00);
                                bookingfclUnits.setProfit(bookingfclUnits.getSellRate() - bookingfclUnits.getBuyRate());
                            }
                            String buttonValue = (editBookingsForm.getButtonValue() != null ? editBookingsForm.getButtonValue() : "");
                            if (!("adjustmentChargeComments".equals(buttonValue))) {
                                bookingfclUnits.setAdjustment(Double.parseDouble(dbUtil.removeComma(editBookingsForm.getAdjustment()[i])));
                            }
                            bookingfclUnits.setUpdateBy(userName);
                            bookingfclUnits.setUpdateOn(new Date());
                            //for saving bundle into OFR-----
                            String bundle = editBookingsForm.getBundleOfr();
                            if (!bundle.equals("")) {
                                String[] print = bundle.split(",");
                                if (print.length > i && print[i].equalsIgnoreCase("1") && (!bookingfclUnits.getChargeCodeDesc().equals("OCNFRT") || !bookingfclUnits.getChargeCodeDesc().equals("OFIMP"))) {
                                    bookingfclUnits.setPrint("on");
                                } else {
                                    bookingfclUnits.setPrint("off");
                                }
                            }
                        }
                    }
                }
            }
        }

        return chargesList;
    }

    public List setOtherMarkupValuesIntoCharges(List chargesList, NewBookingsForm newBookingsForm) throws Exception {
        for (int i = 0; i < chargesList.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) chargesList.get(i);
            if (newBookingsForm.getOthermarkUp()[i] != null) {
                bookingfclUnits.setMarkUp(Double.parseDouble(dbUtil.removeComma((newBookingsForm.getOthermarkUp()[i]))));
                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                bookingfclUnits.setBuyRate(0.00);
                bookingfclUnits.setProfit(bookingfclUnits.getSellRate() - bookingfclUnits.getBuyRate());
            }
        }
        return chargesList;
    }

    public List setOtherMarkupValuesIntoChargesEditBookingForm(List chargesList, EditBookingsForm editBookingsForm) throws Exception {
        for (int i = 0; i < chargesList.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) chargesList.get(i);
            //for saving bundle into OFR-----
            String bundle = editBookingsForm.getOtherChargesBundleOfr();
            if (bundle != null && !bundle.equals("")) {
                String[] print = bundle.split(",");
                if (print[i].equalsIgnoreCase("1")) {
                    bookingfclUnits.setPrint("on");
                } else {
                    bookingfclUnits.setPrint("off");
                }
            }
            /*if(editBookingsForm.getOthermarkUp()!=null && editBookingsForm.getOthermarkUp()[i]!=null){
            bookingfclUnits.setMarkUp(Double.parseDouble(dbUtil.removeComma((editBookingsForm.getOthermarkUp()[i]))));
            if(bookingfclUnits.getAmount()!=null && bookingfclUnits.getMarkUp()!=null){
            bookingfclUnits.setSellRate(bookingfclUnits.getAmount()+bookingfclUnits.getMarkUp());
            bookingfclUnits.setBuyRate(0.00);
            bookingfclUnits.setProfit(bookingfclUnits.getSellRate()-bookingfclUnits.getBuyRate());
            }
            }*/
        }
        return chargesList;
    }

    public List doContainersChanged(List chargesList, NewBookingsForm newBookingsForm) throws Exception {
        BookingfclUnits b2 = (BookingfclUnits) chargesList.get(Integer.parseInt(newBookingsForm.getNumbIdx()));
        for (int i = 0; i < chargesList.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) chargesList.get(i);
            if (b2.getUnitType() != null && bookingfclUnits.getUnitType() != null && b2.getUnitType().getId().equals(bookingfclUnits.getUnitType().getId())) {
                Double a1 = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getChargeAmount()[i])) / Double.parseDouble(bookingfclUnits.getNumbers());
                Double a2 = a1 * Double.parseDouble(newBookingsForm.getNumbers1());
                bookingfclUnits.setAmount(a2);
                Double m1 = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getChargeMarkUp()[i])) / Double.parseDouble(bookingfclUnits.getNumbers());
                Double m2 = m1 * Double.parseDouble(newBookingsForm.getNumbers1());
                bookingfclUnits.setMarkUp(m2);
                if (!newBookingsForm.getNewRate()[i].trim().equals("")) {
                    Double n1 = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getNewRate()[i])) / Double.parseDouble(bookingfclUnits.getNumbers());
                    Double n2 = n1 * Double.parseDouble(newBookingsForm.getNumbers1());
                    bookingfclUnits.setFutureRate(n2);
                }
                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                if (newBookingsForm.getBuyRate() != null) {
                    Double n1 = Double.parseDouble(dbUtil.removeComma(newBookingsForm.getBuyRate()[i])) / Double.parseDouble(bookingfclUnits.getNumbers());
                    Double n2 = n1 * Double.parseDouble(newBookingsForm.getNumbers1());
                    bookingfclUnits.setBuyRate(n2);
                }
                bookingfclUnits.setProfit(bookingfclUnits.getSellRate() - bookingfclUnits.getBuyRate());
                bookingfclUnits.setNumbers(newBookingsForm.getNumbers1());
            }
        }
        return chargesList;
    }

    public List doContainersChangedIntoEditBookignForm(List chargesList, EditBookingsForm editBookingsForm) throws Exception {
        BookingfclUnits b2 = (BookingfclUnits) chargesList.get(Integer.parseInt(editBookingsForm.getNumbIdx()));
        for (int i = 0; i < chargesList.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) chargesList.get(i);
            if (b2.getUnitType() != null && bookingfclUnits.getUnitType() != null && b2.getUnitType().getId().equals(bookingfclUnits.getUnitType().getId())) {
                if (bookingfclUnits.getNumbers() == null) {
                    bookingfclUnits.setNumbers("1");
                }
                Double a1 = Double.parseDouble(dbUtil.removeComma(editBookingsForm.getChargeAmount()[i])) / Double.parseDouble(bookingfclUnits.getNumbers());
                Double a2 = a1 * Double.parseDouble(editBookingsForm.getNumbers1());
                bookingfclUnits.setAmount(a2);
                Double m1 = Double.parseDouble(dbUtil.removeComma(editBookingsForm.getChargeMarkUp()[i])) / Double.parseDouble(bookingfclUnits.getNumbers());
                Double m2 = m1 * Double.parseDouble(editBookingsForm.getNumbers1());
                bookingfclUnits.setMarkUp(m2);
                if (!editBookingsForm.getNewRate()[i].trim().equals("")) {
                    Double n1 = Double.parseDouble(dbUtil.removeComma(editBookingsForm.getNewRate()[i])) / Double.parseDouble(bookingfclUnits.getNumbers());
                    Double n2 = n1 * Double.parseDouble(editBookingsForm.getNumbers1());
                    bookingfclUnits.setFutureRate(n2);
                }
                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                if (editBookingsForm.getBuyRate() != null) {
                    Double n1 = Double.parseDouble(dbUtil.removeComma(editBookingsForm.getBuyRate()[i])) / Double.parseDouble(bookingfclUnits.getNumbers());
                    Double n2 = n1 * Double.parseDouble(editBookingsForm.getNumbers1());
                    bookingfclUnits.setBuyRate(n2);
                }
                bookingfclUnits.setProfit(bookingfclUnits.getSellRate() - bookingfclUnits.getBuyRate());
                bookingfclUnits.setNumbers(editBookingsForm.getNumbers1());
            }
        }
        return chargesList;
    }

    public FclBl getConvertToBl(BookingFcl bookingFcl, EditBookingsForm editBookingForm, User user) throws Exception {
        FclBl fclBl = new FclBl();
        CustAddressBC custAddressBC = new CustAddressBC();
        List addressList = null;
        fclBl.setBlBy(user.getLoginName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        Date date = new Date();
        String agent = "";
        String bolDate = dateFormat.format(date);
        fclBl.setBolDate(dateFormat.parse(bolDate));
        fclBl.setDateoutYard(bookingFcl.getDateoutYard());
        fclBl.setRampCheck(bookingFcl.getRampCheck());
        fclBl.setDateInYard(bookingFcl.getDateInYard());
        fclBl.setAutoDeductFFCom(bookingFcl.getDeductFFcomm());
        fclBl.setSendCopyTo(bookingFcl.getBookingemail());
        fclBl.setRatesRemarks(bookingFcl.getRatesRemarks());
        fclBl.setBillingTerminal(bookingFcl.getIssuingTerminal());
        fclBl.setDomesticRouting(getIssuingTM(bookingFcl.getIssuingTerminal()));
        fclBl.setBkgNo(bookingFcl.getBookingId());
        fclBl.setBookingNo(bookingFcl.getSSBookingNo());
        fclBl.setEta(bookingFcl.getEta());
        fclBl.setAgent(bookingFcl.getAgent());
        fclBl.setSpotRate(bookingFcl.getSpotRate());
        fclBl.setDoorOfOrigin(bookingFcl.getDoorOrigin());
        fclBl.setDoorOfDestination(bookingFcl.getDoorDestination());
        fclBl.setAgentNo(bookingFcl.getAgentNo());
        fclBl.setZip(bookingFcl.getZip());
        fclBl.setFileType(bookingFcl.getFileType());
        fclBl.setLineMove(bookingFcl.getLineMove());
        fclBl.setDefaultAgent(bookingFcl.getAlternateAgent());
        fclBl.setBookingContact(bookingFcl.getBookingContact());
        fclBl.setDirectconsignCheck(bookingFcl.getDirectConsignmntCheck());

        if (bookingFcl.getPrepaidCollect() != null && bookingFcl.getPrepaidCollect().equalsIgnoreCase("P")) {
            fclBl.setHouseBl("P-Prepaid");
        } else {
            fclBl.setHouseBl("C-Collect");
        }
        fclBl.setBillToCode(bookingFcl.getBilltoCode());
        fclBl.setStreamShipBl(null);
        fclBl.setSsBldestinationChargesPreCol(null);
        String ssl = bookingFcl.getSslname();

        int index = 0;
        if (ssl != null && !ssl.equalsIgnoreCase("")) {
            index = ssl.indexOf("//");
        }
        if (index != -1 && index != 0) {
            fclBl.setSslineName(ssl.substring(0, index));
            fclBl.setSslineNo(ssl.substring(index + 2, ssl.length()));
        }
        fclBl.setFileNo(bookingFcl.getFileNo());
        fclBl.setFileNumber(bookingFcl.getFileNo());
        fclBl.setVaoyageInternational(bookingFcl.getVoyageInternal());
        fclBl.setBookNo(bookingFcl.getBookingNumber());
        fclBl.setQuuoteNo(bookingFcl.getQuoteNo());
        fclBl.setShipperNo(bookingFcl.getShipNo());
        fclBl.setShipperName(bookingFcl.getShipper());
        //  shipper address checking.....................................
        String address = custAddressBC.getCompleteShipperAddress(bookingFcl.getShipNo());
        fclBl.setShipperAddress(address);
        fclBl.setMaster("No");
        fclBl.setConsigneeNo(bookingFcl.getConsNo());
        fclBl.setConsigneeName(bookingFcl.getConsignee());
        //  consignee address checking.....................................
        address = custAddressBC.getCompleteAddress(bookingFcl.getConsNo());
        fclBl.setConsigneeAddress(address);
        if (CommonUtils.isNotEmpty(bookingFcl.getConsNo())) {
            TradingPartner tradingPartner = new TradingPartnerBC().findTradingPartnerById(bookingFcl.getConsNo());
            if (null != tradingPartner) {
                if (null != tradingPartner.getNotifyParty()) {
                    fclBl.setNotifyPartyName(tradingPartner.getNotifyParty());
                    fclBl.setNotifyCheck("on");
                    fclBl.setStreamshipNotifyParty(custAddressBC.concatNotifyPartyAddress(tradingPartner));
                }
            }
        }

        fclBl.setForwardAgentNo(bookingFcl.getForwNo());
        fclBl.setForwardingAgentName(bookingFcl.getForward());
        //  forwarder address checking.....................................
        address = custAddressBC.getCompleteAddress(bookingFcl.getForwNo());
        fclBl.setForwardingAgent(address);

        fclBl.setPortOfLoading(bookingFcl.getPortofOrgin());
        fclBl.setSailDate(bookingFcl.getEtd());
        fclBl.setPortCutOff(bookingFcl.getPortCutOff());
        fclBl.setDocCutOff(bookingFcl.getDocCutOff());
        fclBl.setEarlierPickUpDate(bookingFcl.getEarliestPickUpDate());
        fclBl.setVoyages(bookingFcl.getVoyageInternal());
        fclBl.setThirdPartyName(bookingFcl.getAccountName());
        fclBl.setBillTrdPrty(bookingFcl.getAccountNumber());
        fclBl.setMoveType(bookingFcl.getMoveType());
        fclBl.setRampCity(bookingFcl.getRampCity());
        fclBl.setNoOfDays(bookingFcl.getNoOfDays());
        fclBl.setRoutedByAgent(bookingFcl.getRoutedByAgent());
        fclBl.setRoutedByAgentCountry(bookingFcl.getRoutedbyAgentsCountry());
        fclBl.setDestRemarks(editBookingForm.getPortremarks());
        fclBl.setCommodityCode(editBookingForm.getCommcode());
        fclBl.setCommodityDesc(editBookingForm.getComdesc());
        fclBl.setZip(bookingFcl.getZip());
        fclBl.setRoutedAgentCheck(bookingFcl.getRoutedAgentCheck());
        fclBl.setBreakBulk(bookingFcl.getBreakBulk());
        fclBl.setHazmat(bookingFcl.getHazmat());
        fclBl.setRatesNonRates(bookingFcl.getRatesNonRates());
        fclBl.setCostOfGoods(bookingFcl.getCostofgoods());
        fclBl.setInsurance(bookingFcl.getInsurance());
        fclBl.setInsuranceRate(bookingFcl.getInsurancamt());
        fclBl.setLocalDrayage(bookingFcl.getLocaldryage());
        fclBl.setBrand(bookingFcl.getBrand());
        if (bookingFcl.getBilltoCode() != null && bookingFcl.getBilltoCode().equals("F")) {
            fclBl.setBillToParty("Forwarder");
        } else if (bookingFcl.getBilltoCode() != null && bookingFcl.getBilltoCode().equals("S")) {
            fclBl.setBillToParty("Shipper");
        } else if (bookingFcl.getBilltoCode() != null && bookingFcl.getBilltoCode().equals("T")) {
            fclBl.setBillToParty("ThirdParty");
        }
        //-----TO GET RELEASECLAUSE BASED ON DESTINATION------
        String destination = bookingFcl.getPortofDischarge();
        if (null != destination && destination.lastIndexOf("(") != -1 && destination.lastIndexOf(")") != -1) {
            destination = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
        }
        GenericCode genericCode = fCLPortConfigurationDAO.getReleaseClause(destination);
        if (null != genericCode) {
            fclBl.setFclBLClause(genericCode.getCode());
            fclBl.setClauseDescription(genericCode.getCodedesc());
        }//--ends--
        String defaultAgentValues = bookingFcl.getAlternateAgent();
        String agentNumber = bookingFcl.getAgentNo();
        TradingPartner tradingPartner = null;
        List portsList = portsDAO.getPortsofDefaultValues(destination, agentNumber);
        String defaultConsignee = "";
        defaultConsignee = portsDAO.getPortsofDefaultConsignee(destination, agentNumber);
        boolean flag = false;
        if (!defaultConsignee.equalsIgnoreCase("")) {
            fclBl.setHouseConsignee(defaultConsignee);
            flag = true;
        }
        String destinationSchdNo = portsDAO.getShedulenumber(destination);
        Integer id = portsDAO.findId(destinationSchdNo);
        String defaultMasterSettings = portsDAO.getDefaultMasterSettings(id);
        String MasterConsigneeName = "Master.Consignee.Name";
        String MasterConsigneeNameValue = new PropertyDAO().getProperty(MasterConsigneeName);
        String MasterConsigneeAddress = "Master.Consignee.Address";
        String MasterConsigneeAddressValue = new PropertyDAO().getProperty(MasterConsigneeAddress);
        if (portsList.size() > 0) {
            tradingPartner = (TradingPartner) portsList.get(0);
        }
        if (("I".equalsIgnoreCase(bookingFcl.getFileType()))) {
            if (null != tradingPartner && CommonUtils.isNotEmpty(tradingPartner.getAccountno())) {
                CustomerAddress customerAddress = custAddressDAO.findByAgentName(tradingPartner.getAccountno());
                if (customerAddress != null) {
                    if (customerAddress.getAcctname() != null) {
                        fclBl.setHouseConsigneeName(customerAddress.getAcctname());
                    }
                    if (customerAddress.getAccountNo() != null) {
                        fclBl.setHouseConsignee(customerAddress.getAccountNo());
                        tradingPartner = new TradingPartnerBC().findTradingPartnerById(customerAddress.getAccountNo());
                        if (null != tradingPartner) {
                            if (null != tradingPartner.getNotifyParty()) {
                                fclBl.setHouseNotifyPartyName(tradingPartner.getNotifyParty());
                                fclBl.setMasterNotifyCheck("on");
                                fclBl.setHouseNotifyParty(custAddressBC.concatNotifyPartyAddress(tradingPartner));
                            }
                        }
                    }
                    // house conginee address checking.....................................
                    address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                    fclBl.setHouseConsigneeAddress(address);
                }
            } else if (bookingFcl.getAgentNo() != null && !bookingFcl.getAgentNo().equals("")) {
                CustomerAddress customerAddress = custAddressDAO.findByAgentName(bookingFcl.getAgentNo());
                if (customerAddress != null) {
                    if (customerAddress.getAcctname() != null) {
                        fclBl.setHouseConsigneeName(customerAddress.getAcctname());
                    }
                    if (customerAddress.getAccountNo() != null) {
                        fclBl.setHouseConsignee(customerAddress.getAccountNo());
                        tradingPartner = new TradingPartnerBC().findTradingPartnerById(customerAddress.getAccountNo());
                        if (null != tradingPartner) {
                            if (null != tradingPartner.getNotifyParty()) {
                                fclBl.setHouseNotifyPartyName(tradingPartner.getNotifyParty());
                                fclBl.setMasterNotifyCheck("on");
                                fclBl.setHouseNotifyParty(custAddressBC.concatNotifyPartyAddress(tradingPartner));
                            }
                        }
                    }
                    // house conginee address checking.....................................
                    address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                    fclBl.setHouseConsigneeAddress(address);
                }
            }
        } else {
            if (null != tradingPartner && CommonUtils.isNotEmpty(tradingPartner.getAccountno())) {
                CustomerAddress customerAddress = custAddressDAO.findByAgentName(tradingPartner.getAccountno());
                CustomerAddress consigneeAddress = custAddressDAO.findByAgentName(defaultConsignee);
                if (defaultMasterSettings.equalsIgnoreCase("Y")) {
                    fclBl.setHouseConsigneeName(MasterConsigneeNameValue);
                    fclBl.setMasterConsigneeCheck("on");
                } else if (null != customerAddress && customerAddress.getAcctname() != null && defaultMasterSettings.equalsIgnoreCase("N")) {
                    if (flag == true) {
                        if (null != consigneeAddress && consigneeAddress.getAcctname() != null) {
                            fclBl.setHouseConsigneeName(consigneeAddress.getAcctname());
                        }
                    } else {
                        fclBl.setHouseConsigneeName(customerAddress.getAcctname());
                    }
                }
                if (null != customerAddress && customerAddress.getAccountNo() != null) {
                    if (defaultMasterSettings.equalsIgnoreCase("N") && !flag) {
                        fclBl.setHouseConsignee(customerAddress.getAccountNo());
                    }
                    tradingPartner = new TradingPartnerBC().findTradingPartnerById(customerAddress.getAccountNo());
                    if (null != tradingPartner) {
                        if (defaultMasterSettings.equalsIgnoreCase("Y")) {
                            fclBl.setHouseNotifyPartyName(customerAddress.getAcctname());
                            fclBl.setHouseNotifyPartyNo(customerAddress.getAccountNo());
                            address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                            fclBl.setHouseNotifyParty(address);
                        } else if (null != tradingPartner.getNotifyParty() && defaultMasterSettings.equalsIgnoreCase("N")) {
                            fclBl.setHouseNotifyPartyName(tradingPartner.getNotifyParty());
                            fclBl.setMasterNotifyCheck("on");
                            fclBl.setHouseNotifyParty(custAddressBC.concatNotifyPartyAddress(tradingPartner));
                        }
                    }
                }
                // house conginee address checking.....................................
                if (defaultMasterSettings.equalsIgnoreCase("Y")) {
                    fclBl.setHouseConsigneeAddress(MasterConsigneeAddressValue);
                } else if (null != customerAddress && defaultMasterSettings.equalsIgnoreCase("N")) {
                    if (flag == true) {
                        address = custAddressBC.getCompleteAddress(defaultConsignee);
                        fclBl.setHouseConsigneeAddress(address);
                    } else {
                        address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                        fclBl.setHouseConsigneeAddress(address);
                    }
                }
            } else if (bookingFcl.getAgentNo() != null && !bookingFcl.getAgentNo().equals("")) {
                CustomerAddress customerAddress = custAddressDAO.findByAgentName(bookingFcl.getAgentNo());
                CustomerAddress consigneeAddress = custAddressDAO.findByAgentName(defaultConsignee);
                if (customerAddress != null) {
                    if (customerAddress.getAcctname() != null && defaultMasterSettings.equalsIgnoreCase("N")) {
                        if (flag == true) {
                            if (null != consigneeAddress && consigneeAddress.getAcctname() != null) {
                                fclBl.setHouseConsigneeName(consigneeAddress.getAcctname());
                            }
                        } else {
                            fclBl.setHouseConsigneeName(customerAddress.getAcctname());
                        }
                    } else if (defaultMasterSettings.equalsIgnoreCase("Y")) {
                        fclBl.setHouseConsigneeName(MasterConsigneeNameValue);
                        fclBl.setMasterConsigneeCheck("on");
                    }
                    if (null != customerAddress && customerAddress.getAccountNo() != null) {
                        if (defaultMasterSettings.equalsIgnoreCase("N") && !flag) {
                            fclBl.setHouseConsignee(customerAddress.getAccountNo());
                        }
                        tradingPartner = new TradingPartnerBC().findTradingPartnerById(customerAddress.getAccountNo());
                        if (null != tradingPartner) {
                            if (null != tradingPartner.getNotifyParty() && defaultMasterSettings.equalsIgnoreCase("N")) {
                                fclBl.setHouseNotifyPartyName(tradingPartner.getNotifyParty());
                                fclBl.setMasterNotifyCheck("on");
                                fclBl.setHouseNotifyParty(custAddressBC.concatNotifyPartyAddress(tradingPartner));
                            } else if (defaultMasterSettings.equalsIgnoreCase("Y")) {
                                fclBl.setHouseNotifyPartyName(customerAddress.getAcctname());
                                fclBl.setHouseNotifyPartyNo(customerAddress.getAccountNo());
                                address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                                fclBl.setHouseNotifyParty(address);
                            }
                        }
                    }
                    // house conginee address checking.....................................
                    if (defaultMasterSettings.equalsIgnoreCase("Y")) {
                        fclBl.setHouseConsigneeAddress(MasterConsigneeAddressValue);
                    } else if (null != customerAddress && defaultMasterSettings.equalsIgnoreCase("N")) {
                        if (flag == true) {
                            address = custAddressBC.getCompleteAddress(defaultConsignee);
                            fclBl.setHouseConsigneeAddress(address);
                        } else {
                            address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                            fclBl.setHouseConsigneeAddress(address);
                        }
                    }
                } else {
                    if (defaultMasterSettings.equalsIgnoreCase("Y")) {
                        fclBl.setHouseConsigneeName(MasterConsigneeNameValue);
                        fclBl.setMasterConsigneeCheck("on");
                        fclBl.setHouseConsigneeAddress(MasterConsigneeAddressValue);
                    }
                }
            }
        }

        if (defaultMasterSettings.equalsIgnoreCase("N")) {
            if (flag == true) {
                CustomerAddress consigneeAddress = custAddressDAO.findByAgentName(defaultConsignee);
                if (null != consigneeAddress && consigneeAddress.getAcctname() != null) {
                    fclBl.setHouseConsigneeName(consigneeAddress.getAcctname());
                    address = custAddressBC.getCompleteAddress(defaultConsignee);
                    fclBl.setHouseConsigneeAddress(address);
                }
            }
        }
        if (bookingFcl.getOriginTerminal() != null && !bookingFcl.getOriginTerminal().trim().equals("")) {
            fclBl.setTerminal(bookingFcl.getOriginTerminal());
        }
        if (bookingFcl.getPortofDischarge() != null && !bookingFcl.getPortofDischarge().trim().equals("")) {
            fclBl.setFinalDestination(bookingFcl.getPortofDischarge());
        }
        String omit2LetterCountryCodeStatus = bookingFclDAO.getomit2LetterCountryCodeStatus(destination);
        if (CommonUtils.isNotEmpty(omit2LetterCountryCodeStatus) && omit2LetterCountryCodeStatus.equals("Y")) {
            fclBl.setOmit2LetterCountryCode("Yes");
        } else {
            fclBl.setOmit2LetterCountryCode("No");
        }
        fclBl.setPortofDischarge(bookingFcl.getDestination());

        //--TO SET GT-NEXUS OR INTRA BASED ON SSLNAME----------
        if (bookingFcl.getSSLine() != null && !bookingFcl.getSSLine().trim().equals("")) {
            String accountNumber = bookingFcl.getSSLine();
            List carrier = custAddressDAO.findBy1(null, accountNumber, null, null);
            if (carrier != null && carrier.size() > 0) {
                CustAddress c1 = (CustAddress) carrier.get(0);
                fclBl.setSslineName(c1.getAcctName());
                fclBl.setSslineNo(c1.getAcctNo());
                GeneralInformation generalInformation = generalInformationDAO.getGeneralInformationByAccountNumber(c1.getAcctNo());
                if (generalInformation != null && generalInformation.getShippingCode() != null && !generalInformation.getShippingCode().equalsIgnoreCase("N")) {
                    fclBl.setFclInttgra(generalInformation.getShippingCode());
                } else {
                    fclBl.setFclInttgra("");
                }
            }
        }
        if ("I".equalsIgnoreCase(bookingFcl.getImportFlag()) && null != bookingFcl.getVesselNameCheck()) {
            fclBl.setManualVesselName(bookingFcl.getManualVesselName());
            fclBl.setVesselNameCheck(bookingFcl.getVesselNameCheck());
            fclBl.setVessel(null);
        } else {
            if (CommonUtils.isNotEmpty(bookingFcl.getVessel())) {
                GenericCode vessel = genericCodeDAO.getGenericCode(bookingFcl.getVessel().trim(), 14);
                if (null != vessel) {
                    fclBl.setVessel(vessel);
                }
            }
        }
        fclBl.setVoyages(bookingFcl.getVoyageCarrier());
        //fclBl.setStreamShipBl("P-Prepaid");
        addressList = custAddressDAO.getPrimaryCustomerAddress(bookingFcl.getAccountName());
        for (Iterator iter = addressList.iterator(); iter.hasNext();) {
            custAddress = (CustAddress) iter.next();
            fclBl.setBillThirdPartyAddress(custAddress.getAddress1());
        }
        fclBl.setConsigneeCheck(bookingFcl.getConsigneeTpCheck());
        Notes notes = new Notes();
        notes.setModuleId("FILE");
        notes.setUpdateDate(new Date());
        notes.setNoteTpye("auto");
        notes.setNoteDesc("Booking is converted to FCLBL");
        notes.setUpdatedBy(user.getLoginName());
        notes.setModuleRefId(fclBl.getFileNo());
        notesDAO.save(notes);
        return fclBl;
    }

    public List getConvertToInbondList(BookingFcl bookingFcl) throws Exception {
        List fclInbondDetailsList = new ArrayList();
        if (CommonFunctions.isNotNullOrNotEmpty(bookingFcl.getBookingInbondDetails())) {
            List inbondDetailsList = new ArrayList(bookingFcl.getBookingInbondDetails());
            for (Iterator it = inbondDetailsList.iterator(); it.hasNext();) {
                BookingInbondDetails bookingInbondDetails = (BookingInbondDetails) it.next();
                FclInbondDetails fclInbondDetails = new FclInbondDetails();
                fclInbondDetails.setInbondNumber(bookingInbondDetails.getInbondNumber());
                fclInbondDetails.setInbondPort(bookingInbondDetails.getInbondPort());
                fclInbondDetails.setInbondType(bookingInbondDetails.getInbondType());
                fclInbondDetails.setInbondDate(bookingInbondDetails.getInbondDate());
                fclInbondDetails.setFileNumber(null != bookingFcl.getFileNo() ? bookingFcl.getFileNo().toString() : "");
                fclInbondDetailsList.add(fclInbondDetails);
            }
        }
        return fclInbondDetailsList;
    }

    public List getConvertToContainerList(BookingFcl bookignFcl) throws Exception {
        FclBlContainer fclBlContainer = new FclBlContainer();
        List fclcontainerList = new ArrayList();
        List bookingFclUnitList = bookingFclUnitsDAO.getbookingfcl2(String.valueOf(bookignFcl.getBookingId()));
        List groupFclUnitList = bookingFclUnitsDAO.getGroupByCharges(String.valueOf(bookignFcl.getBookingId()));
        if ("R".equalsIgnoreCase(bookignFcl.getRatesNonRates()) || "N".equalsIgnoreCase(bookignFcl.getRatesNonRates())) {
            for (Iterator iterator = groupFclUnitList.iterator(); iterator.hasNext();) {
                BookingfclUnits bookingFclUnits = (BookingfclUnits) iterator.next();
                if ("yes".equalsIgnoreCase(bookingFclUnits.getApproveBl())) {
                    int id = 0;
                    if (bookingFclUnits.getNumbers() != null) {
                        id = Integer.parseInt(bookingFclUnits.getNumbers());
                    }
                    while (id >= 1) {
                        if (bookingFclUnits.getUnitType() != null) {
                            fclBlContainer = new FclBlContainer();
                            fclBlContainer.setSizeLegend(bookingFclUnits.getUnitType());
                            fclBlContainer.setSpecialEquipment(bookingFclUnits.getSpecialEquipment());
                            fclcontainerList.add(fclBlContainer);
                            id--;
                        }
                    }
                }
            }
        } else {
            for (Iterator iterator = bookingFclUnitList.iterator(); iterator.hasNext();) {
                BookingfclUnits bookingFclUnits = (BookingfclUnits) iterator.next();
                int id = 0;
                if (bookingFclUnits.getNumbers() != null) {
                    id = Integer.parseInt(bookingFclUnits.getNumbers());
                }
                if (bookignFcl.getRatesNonRates() != null && bookignFcl.getRatesNonRates().equals("N")) {
                    bookingFclUnits.setApproveBl("Yes");
                }
                boolean flag = false;
                if (bookingFclUnits.getUnitType() != null && bookingFclUnits.getApproveBl() != null && bookingFclUnits.getApproveBl().equalsIgnoreCase("Yes")) {
                    if (fclcontainerList.size() > 0) {
                        for (int i = 0; i < fclcontainerList.size(); i++) {
                            fclBlContainer = (FclBlContainer) fclcontainerList.get(i);
                            if (bookingFclUnits.getUnitType() != null && fclBlContainer.getSizeLegend() != null) {
                                if (fclBlContainer.getSizeLegend().getCode().equals(bookingFclUnits.getUnitType().getCode())) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if (!flag) {
                            while (id >= 1) {
                                if (bookingFclUnits.getUnitType().getCode() != null) {
                                    fclBlContainer = new FclBlContainer();
                                    fclBlContainer.setSizeLegend(bookingFclUnits.getUnitType());
                                    fclcontainerList.add(fclBlContainer);
                                    id--;
                                }
                            }
                        }
                    } else {
                        while (id >= 1) {
                            if (bookingFclUnits.getUnitType() != null) {
                                fclBlContainer = new FclBlContainer();
                                fclBlContainer.setSizeLegend(bookingFclUnits.getUnitType());
                                fclcontainerList.add(fclBlContainer);
                            }
                            id--;
                        }
                    }
                }
            }
        }
        return fclcontainerList;
    }

    public List getConvertToChargesList(BookingFcl bookingFcl, String buttonValue, List bookingFclUnitList) throws Exception {
        FclBlCharges fclBlCharges = new FclBlCharges();
        List addchargeslist = new ArrayList();
        List newaddList = new ArrayList();
        Map getUniqueCostCode = new HashMap();
        if (buttonValue.equals("converttoblnew")) {
        } else {
            bookingFclUnitList = bookingFclUnitsDAO.getbookingfcl2(String.valueOf(bookingFcl.getBookingId()));
        }
        List tempBookingFclList = new ArrayList(bookingFclUnitList);
        if (!tempBookingFclList.isEmpty()) {
            boolean rateChange = false;
            for (Iterator iterator = tempBookingFclList.iterator(); iterator.hasNext();) {
                BookingfclUnits bookingFclUnits = (BookingfclUnits) iterator.next();
                BookingfclUnits tempBookingfclUnits = new BookingfclUnits();
                PropertyUtils.copyProperties(tempBookingfclUnits, bookingFclUnits);
                //------SETTING THE FLAG TO MAKE THESE  BOOKING CHARGES READONLY IN BL--------
                fclBlCharges.setReadOnlyFlag("on");
                if (bookingFcl.getRatesNonRates() != null && bookingFcl.getRatesNonRates().equals("N") && "Y".equalsIgnoreCase(bookingFcl.getBreakBulk())) {
                    tempBookingfclUnits.setApproveBl("Yes");
                }
                if (null != tempBookingfclUnits && tempBookingfclUnits.getApproveBl() != null
                        && tempBookingfclUnits.getApproveBl().equalsIgnoreCase("Yes")) {
                    if (getUniqueCostCode.containsKey(tempBookingfclUnits.getChargeCodeDesc())) {
                        fclBlCharges = (FclBlCharges) getUniqueCostCode.get(tempBookingfclUnits.getChargeCodeDesc());
                        double amount = fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00;
                        Integer number = tempBookingfclUnits.getNumbers() != null ? new Integer(
                                tempBookingfclUnits.getNumbers()) : 1;
                        if (!"new".equals(tempBookingfclUnits.getNewFlag())) {
                            if (tempBookingfclUnits.getRateChangeAmount() != null
                                    && !tempBookingfclUnits.getRateChangeAmount().equals(0.00)) {
                                rateChange = true;
                                amount += tempBookingfclUnits.getRateChangeAmount() * number;
                            } else if (bookingFcl.getSpotRate().equals("Y") && null != tempBookingfclUnits.getSpotRateAmt()) {
                                amount += (tempBookingfclUnits.getSpotRateAmt() != null ? tempBookingfclUnits.getSpotRateAmt() : 0.00) * number;
                            } else {
                                amount += (tempBookingfclUnits.getAmount() != null ? tempBookingfclUnits.getAmount() : 0.00) * number;
                            }
                        }
                        if (tempBookingfclUnits.getRateChangeMarkup() != null
                                && !tempBookingfclUnits.getRateChangeMarkup().equals(0.00)) {
                            amount += tempBookingfclUnits.getRateChangeMarkup() * number;
                        } else if (null != tempBookingfclUnits.getSpotRateMarkUp()) {
                            amount += (tempBookingfclUnits.getSpotRateMarkUp() != null ? tempBookingfclUnits.getSpotRateMarkUp() : 0.00) * number;
                        } else {
                            amount += (tempBookingfclUnits.getMarkUp() != null ? tempBookingfclUnits.getMarkUp() : 0.00) * number;
                        }
                        amount += (tempBookingfclUnits.getAdjustment() != null ? tempBookingfclUnits.getAdjustment() : 0.00) * number;
                        fclBlCharges.setAmount(amount);
                        fclBlCharges.setOldAmount(amount);
                        if (fclBlCharges.getChargesRemarks() != null && !fclBlCharges.getChargesRemarks().equalsIgnoreCase("")) {
                            fclBlCharges.setChargesRemarks(fclBlCharges.getChargesRemarks());
                        } else {
                            fclBlCharges.setChargesRemarks(tempBookingfclUnits.getComment());
                        }
                        //booking flag is for manually added charges in booking-------
                        fclBlCharges.setBookingFlag("D".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                        fclBlCharges.setBookingFlag("PP".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                       // fclBlCharges.setBookingFlag("CHH".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                        fclBlCharges.setBundleIntoOfr("on".equalsIgnoreCase(tempBookingfclUnits.getPrint()) ? "Yes" : "No");
                    } else {
                        fclBlCharges.setChargeCode(tempBookingfclUnits.getChargeCodeDesc());
                        fclBlCharges.setCharges(tempBookingfclUnits.getChgCode());
                        if (tempBookingfclUnits.getAmount() != null) {
                            Integer number = tempBookingfclUnits.getNumbers() != null ? new Integer(
                                    tempBookingfclUnits.getNumbers()) : 1;
                            double amount = 0.0;
                            if (!"new".equals(tempBookingfclUnits.getNewFlag())) {
                                if (tempBookingfclUnits.getRateChangeAmount() != null
                                        && !tempBookingfclUnits.getRateChangeAmount().equals(0.00)) {
                                    amount += tempBookingfclUnits.getRateChangeAmount() * number;
                                    rateChange = true;
                                } else if (bookingFcl.getSpotRate().equals("Y") && null != tempBookingfclUnits.getSpotRateAmt()) {
                                    amount += (tempBookingfclUnits.getSpotRateAmt() != null ? tempBookingfclUnits.getSpotRateAmt() : 0.00) * number;
                                } else {
                                    amount += (tempBookingfclUnits.getAmount() != null ? tempBookingfclUnits.getAmount() : 0.00) * number;
                                }
                            }
                            if (tempBookingfclUnits.getRateChangeMarkup() != null
                                    && !tempBookingfclUnits.getRateChangeMarkup().equals(0.00)) {
                                amount += tempBookingfclUnits.getRateChangeMarkup() * number;
                            } else if (null != tempBookingfclUnits.getSpotRateMarkUp()) {
                                amount += (tempBookingfclUnits.getSpotRateMarkUp() != null ? tempBookingfclUnits.getSpotRateMarkUp() : 0.00) * number;
                            } else {
                                amount += (tempBookingfclUnits.getMarkUp() != null ? tempBookingfclUnits.getMarkUp() : 0.00) * number;
                            }
                            amount += (tempBookingfclUnits.getAdjustment() != null ? tempBookingfclUnits.getAdjustment() : 0.00) * number;
                            fclBlCharges.setAmount(amount);
                            fclBlCharges.setOldAmount(amount);
                        }
                        if (tempBookingfclUnits.getCurrency() != null) {
                            fclBlCharges.setCurrencyCode(tempBookingfclUnits.getCurrency());
                        }
                        if (fclBlCharges.getChargesRemarks() != null && !fclBlCharges.getChargesRemarks().equalsIgnoreCase("")) {
                            fclBlCharges.setChargesRemarks(fclBlCharges.getChargesRemarks());
                        } else {
                            fclBlCharges.setChargesRemarks(tempBookingfclUnits.getComment());
                        }
                        if (bookingFcl.getPrepaidCollect().equals("C")) {
                            if ("I".equalsIgnoreCase(bookingFcl.getImportFlag())) {
                                fclBlCharges.setBillTo("Consignee");
                            } else {
                                fclBlCharges.setBillTo("Agent");
                            }
                        } else if (bookingFcl.getPrepaidCollect().equals("P")) {
                            if (bookingFcl.getBilltoCode().equals("F")) {
                                fclBlCharges.setBillTo("Forwarder");
                            } else if (bookingFcl.getBilltoCode().equals("S")) {
                                fclBlCharges.setBillTo("Shipper");
                            } else if (bookingFcl.getBilltoCode().equals("T")) {
                                fclBlCharges.setBillTo("ThirdParty");
                            }
                        }

                        fclBlCharges.setPrintOnBl("Yes");
                        fclBlCharges.setBundleIntoOfr("on".equalsIgnoreCase(tempBookingfclUnits.getPrint()) ? "Yes" : "No");
                        fclBlCharges.setPcollect("prepaid");
                        fclBlCharges.setBookingFlag("D".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                        fclBlCharges.setBookingFlag("PP".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                        //fclBlCharges.setBookingFlag("CHH".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                       getUniqueCostCode.put(fclBlCharges.getChargeCode(), fclBlCharges);
                    }
                    fclBlCharges = new FclBlCharges();
                } else if (tempBookingfclUnits != null && tempBookingfclUnits.getCostType().equalsIgnoreCase("PER BL CHARGES")) {
                    if (getUniqueCostCode.containsKey(tempBookingfclUnits.getChargeCodeDesc())) {
                        fclBlCharges = (FclBlCharges) getUniqueCostCode.get(tempBookingfclUnits.getChargeCodeDesc());
                        double amount = fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00;
                        fclBlCharges.setChargeCode(tempBookingfclUnits.getChargeCodeDesc());
                        fclBlCharges.setCharges(tempBookingfclUnits.getChgCode());
                        if (fclBlCharges.getChargesRemarks() != null && !fclBlCharges.getChargesRemarks().equalsIgnoreCase("")) {
                            fclBlCharges.setChargesRemarks(fclBlCharges.getChargesRemarks());
                        } else {
                            fclBlCharges.setChargesRemarks(tempBookingfclUnits.getComment());
                        }
                        if (tempBookingfclUnits.getCurrency() != null) {
                            fclBlCharges.setCurrencyCode(tempBookingfclUnits.getCurrency());
                        }
                        if (bookingFcl.getPrepaidCollect().equals("C")) {
                            if ("I".equalsIgnoreCase(bookingFcl.getImportFlag())) {
                                fclBlCharges.setBillTo("Consignee");
                            } else {
                                fclBlCharges.setBillTo("Agent");
                            }
                        } else if (bookingFcl.getPrepaidCollect().equals("P")) {
                            if (bookingFcl.getBilltoCode().equals("F")) {
                                fclBlCharges.setBillTo("Forwarder");
                            } else if (bookingFcl.getBilltoCode().equals("S")) {
                                fclBlCharges.setBillTo("Shipper");
                            } else if (bookingFcl.getBilltoCode().equals("T")) {
                                fclBlCharges.setBillTo("ThirdParty");
                            }

                        }
                        fclBlCharges.setPrintOnBl("Yes");
                        fclBlCharges.setBundleIntoOfr("on".equalsIgnoreCase(tempBookingfclUnits.getPrint()) ? "Yes" : "No");
                        fclBlCharges.setPcollect("prepaid");
                        if (!"new".equals(tempBookingfclUnits.getNewFlag())) {
                            if (tempBookingfclUnits.getRateChangeAmount() != null
                                    && !tempBookingfclUnits.getRateChangeAmount().equals(0.00)) {
                                amount += tempBookingfclUnits.getRateChangeAmount();
                                rateChange = true;
                            } else if (bookingFcl.getSpotRate().equals("Y") && null != tempBookingfclUnits.getSpotRateAmt()) {
                                amount += (tempBookingfclUnits.getSpotRateAmt() != null ? tempBookingfclUnits.getSpotRateAmt() : 0.00);
                            } else {
                                amount += (tempBookingfclUnits.getAmount() != null ? tempBookingfclUnits.getAmount() : 0.00);
                            }
                        }
                        if (tempBookingfclUnits.getRateChangeMarkup() != null
                                && !tempBookingfclUnits.getRateChangeMarkup().equals(0.00)) {
                            amount += tempBookingfclUnits.getRateChangeMarkup();
                        } else if (null != tempBookingfclUnits.getSpotRateMarkUp()) {
                            amount += (tempBookingfclUnits.getSpotRateMarkUp() != null ? tempBookingfclUnits.getSpotRateMarkUp() : 0.00);
                        } else {
                            amount += (tempBookingfclUnits.getMarkUp() != null ? tempBookingfclUnits.getMarkUp() : 0.00);
                        }
                        amount += (tempBookingfclUnits.getAdjustment() != null ? tempBookingfclUnits.getAdjustment() : 0.00);
                        fclBlCharges.setAmount(amount);
                        if (CommonUtils.isEqual(tempBookingfclUnits.getNewFlag(), "new")) {
                            fclBlCharges.setOldAmount(tempBookingfclUnits.getMarkUp());
                        }
                        if (null != tempBookingfclUnits.getSpotRateMarkUp()) {
                            fclBlCharges.setOldAmount(tempBookingfclUnits.getAmount() + tempBookingfclUnits.getSpotRateMarkUp());
                        } else {
                            fclBlCharges.setOldAmount(tempBookingfclUnits.getAmount() + tempBookingfclUnits.getMarkUp());
                        }
                        fclBlCharges.setBookingFlag("D".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                        fclBlCharges.setBookingFlag("PP".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                       // fclBlCharges.setBookingFlag("CHH".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                       
                    } else {
                        fclBlCharges = new FclBlCharges();
                        fclBlCharges.setChargeCode(tempBookingfclUnits.getChargeCodeDesc());
                        fclBlCharges.setCharges(tempBookingfclUnits.getChgCode());
                        if (fclBlCharges.getChargesRemarks() != null && !fclBlCharges.getChargesRemarks().equalsIgnoreCase("")) {
                            fclBlCharges.setChargesRemarks(fclBlCharges.getChargesRemarks());
                        } else {
                            fclBlCharges.setChargesRemarks(tempBookingfclUnits.getComment());
                        }
                        if (tempBookingfclUnits.getCurrency() != null) {
                            fclBlCharges.setCurrencyCode(tempBookingfclUnits.getCurrency());
                        }
                        if (bookingFcl.getPrepaidCollect().equals("C")) {
                            if ("I".equalsIgnoreCase(bookingFcl.getImportFlag())) {
                                fclBlCharges.setBillTo("Consignee");
                            } else {
                                fclBlCharges.setBillTo("Agent");
                            }
                        } else if (bookingFcl.getPrepaidCollect().equals("P")) {
                            if (bookingFcl.getBilltoCode().equals("F")) {
                                fclBlCharges.setBillTo("Forwarder");
                            } else if (bookingFcl.getBilltoCode().equals("S")) {
                                fclBlCharges.setBillTo("Shipper");
                            } else if (bookingFcl.getBilltoCode().equals("T")) {
                                fclBlCharges.setBillTo("ThirdParty");
                            }

                        }
                        fclBlCharges.setPrintOnBl("Yes");
                        fclBlCharges.setBundleIntoOfr("on".equalsIgnoreCase(tempBookingfclUnits.getPrint()) ? "Yes" : "No");
                        fclBlCharges.setPcollect("prepaid");
                        double amount = 0.0;
                        if (!"new".equals(tempBookingfclUnits.getNewFlag())) {
                            if (tempBookingfclUnits.getRateChangeAmount() != null
                                    && !tempBookingfclUnits.getRateChangeAmount().equals(0.00)) {
                                amount += tempBookingfclUnits.getRateChangeAmount();
                                rateChange = true;
                            } else {
                                amount += (tempBookingfclUnits.getAmount() != null ? tempBookingfclUnits.getAmount() : 0.00);
                            }
                        }
                        if (tempBookingfclUnits.getRateChangeMarkup() != null
                                && !tempBookingfclUnits.getRateChangeMarkup().equals(0.00)) {
                            amount += tempBookingfclUnits.getRateChangeMarkup();
                            rateChange = true;
                        } else {
                            if (null != tempBookingfclUnits.getSpotRateMarkUp()) {
                                amount += (tempBookingfclUnits.getSpotRateMarkUp() != null ? tempBookingfclUnits.getSpotRateMarkUp() : 0.00);
                            } else {
                                amount += (tempBookingfclUnits.getMarkUp() != null ? tempBookingfclUnits.getMarkUp() : 0.00);
                            }
                        }
                        amount += (tempBookingfclUnits.getAdjustment() != null ? tempBookingfclUnits.getAdjustment() : 0.00);
                        fclBlCharges.setAmount(amount);
                        if (CommonUtils.isEqual(tempBookingfclUnits.getNewFlag(), "new")) {
                            fclBlCharges.setOldAmount(tempBookingfclUnits.getMarkUp());
                        }
                        if (null != tempBookingfclUnits.getSpotRateMarkUp()) {
                            fclBlCharges.setOldAmount(tempBookingfclUnits.getAmount() + tempBookingfclUnits.getSpotRateMarkUp());
                        } else {
                            fclBlCharges.setOldAmount(tempBookingfclUnits.getAmount() + tempBookingfclUnits.getMarkUp());
                        }
                        fclBlCharges.setBookingFlag("D".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                        fclBlCharges.setBookingFlag("PP".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                       // fclBlCharges.setBookingFlag("CHH".equals(tempBookingfclUnits.getNewFlag()) ? "new" : tempBookingfclUnits.getNewFlag());
                      getUniqueCostCode.put(fclBlCharges.getChargeCode(), fclBlCharges);
                    }
                }
            }
            if (rateChange) {
                new BookingFclDAO().clearSpotCost(bookingFcl.getFileNo());
            }
        }
        Set set = getUniqueCostCode.keySet();
        Iterator iter2 = set.iterator();
        while (iter2.hasNext()) {
            String currentKey = (String) iter2.next();
            newaddList.add(getUniqueCostCode.get(currentKey));
        }
        return newaddList;
    }

    public List getConvertToCostCodesList(BookingFcl bookingFcl, String buttonValue, List bookingFclUnitList, MessageResources messageResources) throws Exception {
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        List newaddList1 = new ArrayList();
        Map getUniqueCostCode1 = new HashMap();
        if (buttonValue.equals("converttoblnew")) {
        } else {
            bookingFclUnitList = bookingFclUnitsDAO.getbookingfcl2(String.valueOf(bookingFcl.getBookingId()));
        }
        if (!bookingFclUnitList.isEmpty()) {
            boolean rateChange = false;
            GlMappingDAO glMappingDAO = new GlMappingDAO();
            for (Iterator iter = bookingFclUnitList.iterator(); iter.hasNext();) {
                BookingfclUnits tempBookingfclUnits = (BookingfclUnits) iter.next();
                if (CommonFunctions.isNotNull(tempBookingfclUnits.getAccountNo())) {
                    BookingfclUnits bookingFclUnits = new BookingfclUnits();
                    Integer number = 1;

                    double amount = 0.0;
                    PropertyUtils.copyProperties(bookingFclUnits, tempBookingfclUnits);
                    boolean isBlLevelCost = glMappingDAO.isBlLevelCost(bookingFclUnits.getChargeCodeDesc());
                    if (!isBlLevelCost) {
                        number = bookingFclUnits.getNumbers() != null ? new Integer(bookingFclUnits.getNumbers()) : 1;
                    }
                    //----SETTING THE FLAG TO MAKE THESE COSTCODES READONLY IN BL--------
                    fclBlCostCodes.setReadOnlyFlag("on");
                    fclBlCostCodes.setInvoiceNumber(bookingFclUnits.getInvoiceNumber());

                    if (bookingFcl.getRatesNonRates() != null && bookingFcl.getRatesNonRates().equals("N") && "Y".equalsIgnoreCase(bookingFcl.getBreakBulk())) {
                        bookingFclUnits.setApproveBl("Yes");
                    }
                    if (bookingFclUnits != null && bookingFclUnits.getApproveBl() != null && bookingFclUnits.getApproveBl().equalsIgnoreCase("Yes")) {
                        if (getUniqueCostCode1.containsKey(bookingFclUnits.getChargeCodeDesc() + "-" + bookingFclUnits.getAccountName())) {
                            fclBlCostCodes = (FclBlCostCodes) getUniqueCostCode1.get(bookingFclUnits.getChargeCodeDesc() + "-" + bookingFclUnits.getAccountName());
                            if (bookingFclUnits.getRateChangeAmount() != null
                                    && !bookingFclUnits.getRateChangeAmount().equals(0.00)) {
                                amount += bookingFclUnits.getRateChangeAmount() * number;
                                rateChange = true;
                            } else if (bookingFcl.getSpotRate().equals("Y") && null != bookingFclUnits.getSpotRateAmt()) {
                                amount += (bookingFclUnits.getSpotRateAmt() != null) ? bookingFclUnits.getSpotRateAmt() * number : 0.0 * number;
                            } else {
                                amount += (bookingFclUnits.getAmount() != null) ? bookingFclUnits.getAmount() * number : 0.0 * number;
                            }

                            if (fclBlCostCodes.getAmount() != null) {
                                fclBlCostCodes.setAmount(fclBlCostCodes.getAmount() + amount);
                            }
                            fclBlCostCodes.setCostComments(bookingFclUnits.getComment());
                            fclBlCostCodes.setBookingFlag("D".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                            fclBlCostCodes.setBookingFlag("PP".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                          //  fclBlCostCodes.setBookingFlag("CHH".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                       
                        } else {
                            if (bookingFclUnits.getChargeCodeDesc() != null) {
                                fclBlCostCodes.setCostCode(bookingFclUnits.getChargeCodeDesc());
                            }
                            if (bookingFclUnits.getChgCode() != null) {
                                fclBlCostCodes.setCostCodeDesc(bookingFclUnits.getChgCode());
                            }
                            if (bookingFclUnits.getRateChangeAmount() != null
                                    && !bookingFclUnits.getRateChangeAmount().equals(0.00)) {
                                amount += bookingFclUnits.getRateChangeAmount() * number;
                                rateChange = true;
                            } else if (bookingFcl.getSpotRate().equals("Y") && null != bookingFclUnits.getSpotRateAmt()) {
                                amount += (bookingFclUnits.getSpotRateAmt() != null) ? bookingFclUnits.getSpotRateAmt() * number : 0.0 * number;
                            } else {
                                amount += (bookingFclUnits.getAmount() != null) ? bookingFclUnits.getAmount() * number : 0.0 * number;
                            }
                            fclBlCostCodes.setAmount(amount);
                            if (bookingFclUnits.getAccountNo() != null) {
                                fclBlCostCodes.setAccNo(bookingFclUnits.getAccountNo());
                            }
                            if (bookingFclUnits.getAccountName() != null) {
                                fclBlCostCodes.setAccName(bookingFclUnits.getAccountName());
                            }
                            if (bookingFclUnits.getCurrency() != null) {
                                fclBlCostCodes.setCurrencyCode(bookingFclUnits.getCurrency());
                            }
                            if (bookingFclUnits.getComment() != null) {
                                fclBlCostCodes.setCostComments(bookingFclUnits.getComment());
                            }
                            fclBlCostCodes.setBookingFlag("D".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                            fclBlCostCodes.setBookingFlag("PP".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                         //   fclBlCostCodes.setBookingFlag("CHH".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                            if (bookingFclUnits.getChgCode() != null) {
                                String chargeCode = bookingFclUnits.getChargeCodeDesc();
                                if (chargeCode.equals(messageResources.getMessage("chargeCodeAsPBA"))) {
                                    fclBlCostCodes.setAccNo(bookingFcl.getAgentNo());
                                    fclBlCostCodes.setAccName(bookingFcl.getAgent());
                                } else if (chargeCode.equals(messageResources.getMessage("chargeCodeAsADVSHP"))) {
                                    fclBlCostCodes.setAccNo(bookingFcl.getShipNo());
                                    fclBlCostCodes.setAccName(bookingFcl.getShipper());
                                }
                            }
                            //--- FFCOMMISSION and PBA CHARGES NOT TO BE ADDED TO THE COSTCODE LIST------
                            if (fclBlCostCodes.getCostCodeDesc().equals(FclBlConstants.ADVANCESURCHARGEDESC) || (fclBlCostCodes.getCostCode().equals("INSURE"))
                                    || (fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODE) && fclBlCostCodes.getCostCodeDesc().equalsIgnoreCase("FF COMMISSION"))) {
                                //do nothing.
                            } else {
                                getUniqueCostCode1.put(fclBlCostCodes.getCostCode() + "-" + fclBlCostCodes.getAccName(), fclBlCostCodes);
                            }
                        }
                        fclBlCostCodes = new FclBlCostCodes();
                    } else if (bookingFclUnits != null && bookingFclUnits.getCostType().equalsIgnoreCase("PER BL CHARGES")) {
                        fclBlCostCodes = new FclBlCostCodes();
                        if (bookingFclUnits.getChargeCodeDesc() != null) {
                            fclBlCostCodes.setCostCode(bookingFclUnits.getChargeCodeDesc());
                        }
                        if (bookingFclUnits.getChgCode() != null) {
                            fclBlCostCodes.setCostCodeDesc(bookingFclUnits.getChgCode());
                        }
                        if (bookingFclUnits.getCurrency() != null) {
                            fclBlCostCodes.setCurrencyCode(bookingFclUnits.getCurrency());
                        }
                        if (bookingFclUnits.getAccountNo() != null) {
                            fclBlCostCodes.setAccNo(bookingFclUnits.getAccountNo());
                        }
                        if (bookingFclUnits.getAccountName() != null) {
                            fclBlCostCodes.setAccName(bookingFclUnits.getAccountName());
                        }
                        if (bookingFclUnits.getComment() != null) {
                            fclBlCostCodes.setCostComments(bookingFclUnits.getComment());
                        }
                        if (bookingFclUnits.getChgCode() != null) {
                            String chargeCode = bookingFclUnits.getChgCode();
                            if (chargeCode.equals(messageResources.getMessage("chargeCodeAsPBA"))) {
                                fclBlCostCodes.setAccNo(bookingFcl.getAgentNo());
                                fclBlCostCodes.setAccName(bookingFcl.getAgent());
                            } else if (chargeCode.equals(messageResources.getMessage("chargeCodeAsADVSHP"))) {
                                fclBlCostCodes.setAccNo(bookingFcl.getShipNo());
                                fclBlCostCodes.setAccName(bookingFcl.getShipper());
                            }
                        }
                        fclBlCostCodes.setBookingFlag("D".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                        fclBlCostCodes.setBookingFlag("PP".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                   //     fclBlCostCodes.setBookingFlag("CHH".equals(bookingFclUnits.getNewFlag()) ? "new" : bookingFclUnits.getNewFlag());
                        if (bookingFclUnits.getRateChangeAmount() != null
                                && !bookingFclUnits.getRateChangeAmount().equals(0.00)) {
                            amount += bookingFclUnits.getRateChangeAmount() * number;
                            rateChange = true;
                        } else if (bookingFcl.getSpotRate().equals("Y") && null != bookingFclUnits.getSpotRateAmt()) {
                            amount += (bookingFclUnits.getSpotRateAmt() != null) ? bookingFclUnits.getSpotRateAmt() : 0.0 * number;
                        } else {
                            amount += (bookingFclUnits.getAmount() != null) ? bookingFclUnits.getAmount() : 0.0 * number;
                        }
                        fclBlCostCodes.setAmount(amount);
                        if (fclBlCostCodes.getAmount().equals(0.0)) {
                            //--if amount is zero then it should not be added to costcode list--
                        } else if (fclBlCostCodes.getCostCodeDesc().equals(FclBlConstants.ADVANCESURCHARGEDESC)) {
                            //--do nothing i.e PBA charge should not be added to cost code list--
                        } else {
                            getUniqueCostCode1.put(fclBlCostCodes.getCostCode() + "-" + fclBlCostCodes.getAccName(), fclBlCostCodes);
                        }
                    }
                }// if checking for account number
            }
            if (rateChange) {
                new BookingFclDAO().clearSpotCost(bookingFcl.getFileNo());
            }
        }
        Set set2 = getUniqueCostCode1.keySet();
        Iterator iter1 = set2.iterator();
        while (iter1.hasNext()) {
            String currentKey = (String) iter1.next();
            newaddList1.add(getUniqueCostCode1.get(currentKey));
        }
        return newaddList1;
    }

    public FclBlCostCodes multipleConatinerByInputValue(MessageResources messageResources, List<BookingfclUnits> bookingFclUnitList, BookingFcl bookingFcl) throws Exception {
        String ffCommissionRates[] = LoadLogisoftProperties.getProperty("ffcommissionrates").split(",");
        String uniType[] = messageResources.getMessage("unittype").split(",");
        Integer amount = 0;
        String tempString = "";
        if (bookingFclUnitList != null) {
            for (BookingfclUnits bookingFclUnit : bookingFclUnitList) {
                if (bookingFclUnit.getUnitType() != null && bookingFclUnit.getApproveBl() != null
                        && bookingFclUnit.getApproveBl().equalsIgnoreCase("Yes")) {
                    if (!tempString.equals(bookingFclUnit.getUnitType().getId1().toString())) {
                        if (bookingFclUnit.getUnitType().getId().toString().equals(uniType[0])) {
                            Integer value = new Integer(ffCommissionRates[0].replace("-", ""));
                            Integer number = null != bookingFclUnit.getNumbers() ? new Integer(bookingFclUnit.getNumbers()) : 1;
                            amount += (value * number);
                        } else {
                            Integer value = new Integer(ffCommissionRates[1].replace("-", ""));
                            Integer number = null != bookingFclUnit.getNumbers() ? new Integer(bookingFclUnit.getNumbers()) : 1;
                            amount += (value * number);
                        }
                    }
                    tempString = bookingFclUnit.getUnitType().getId1().toString();
                }
            }
        }
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        fclBlCostCodes.setCostCode(FclBlConstants.FFCODE);
        fclBlCostCodes.setCostCodeDesc(FclBlConstants.FFCODEDESC);
        fclBlCostCodes.setAmount(amount.doubleValue());
        fclBlCostCodes.setCurrencyCode("USD");
        fclBlCostCodes.setBookingFlag("M");
        fclBlCostCodes.setReadOnlyFlag("new");
        fclBlCostCodes.setAccName(bookingFcl.getForward());
        fclBlCostCodes.setAccNo(bookingFcl.getForwNo());
        return fclBlCostCodes;

    }

    public List<Charges> cheackChargeCodeforBooking(String columnName, String columnValue, String secondColumnName, String bookingId) throws Exception {
        return bookingFclUnitsDAO.findByPropertyUsingBookingId(columnName, columnValue, secondColumnName, bookingId);
    }

    public Set copyCharges(List chargesList, Set bookingFclSet) throws Exception {
        for (int i = 0; i < chargesList.size(); i++) {
            BookingfclUnits bookingFclUnits = (BookingfclUnits) chargesList.get(i);
            BookingfclUnits B1 = new BookingfclUnits();
            if (bookingFclUnits != null) {
                PropertyUtils.copyProperties(B1, bookingFclUnits);
                B1.setBookingNumber(null);
                B1.setId(null);
                bookingFclSet.add(B1);
            }
        }
        return bookingFclSet;
    }
    NumberFormat number = new DecimalFormat("###,###,##0.00");

    public void createWorkOrderReport(EditBookingsForm form, String fileName, String contextPath) throws Exception {
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        SearchBookingReportDTO searchBookingReportDTO = new SearchBookingReportDTO();
        //BookingFcl bookingFcl=new BookingFcl();
        bookingFcl = bookingFclDAO.findById(Integer.parseInt(form.getBookingId()));
        List workOrderFieldList = bookingFclDAO.getworkOrderFieldsList(form.getBookingId());
        WorkOrderPdfCreator workOrderPdfCreator = new WorkOrderPdfCreator();
        searchBookingReportDTO.setBookingflFcl(bookingFcl);
        searchBookingReportDTO.setObjectList(workOrderFieldList);
        searchBookingReportDTO.setFileName(fileName);
        searchBookingReportDTO.setContextPath(contextPath);
        workOrderPdfCreator.createReport(searchBookingReportDTO);
    }

    public void createReferenceReport(EditBookingsForm form, String fileName, String contextPath) throws Exception {
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        SearchBookingReportDTO searchBookingReportDTO = new SearchBookingReportDTO();
        //BookingFcl bookingFcl=new BookingFcl();
        bookingFcl = bookingFclDAO.findById(Integer.parseInt(form.getBookingId()));
        List workOrderFieldList = bookingFclDAO.getworkOrderFieldsList(form.getBookingId());
        ReferenceRequestPdfCreator referenceRequestPdfCreator = new ReferenceRequestPdfCreator();
        searchBookingReportDTO.setBookingflFcl(bookingFcl);
        searchBookingReportDTO.setObjectList(workOrderFieldList);
        searchBookingReportDTO.setFileName(fileName);
        searchBookingReportDTO.setContextPath(contextPath);
        referenceRequestPdfCreator.createReport(searchBookingReportDTO);
    }

    public void createCostSheetReport(EditBookingsForm form, String fileName, String contextPath, MessageResources messageResources) throws Exception {
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        BookingCostSheetPdfCreator bookingCostSheetPdfCreator = new BookingCostSheetPdfCreator();
        SearchBookingReportDTO searchBookingReportDTO = new SearchBookingReportDTO();
        //BookingFcl bookingFcl=new BookingFcl();
        bookingFcl = bookingFclDAO.findById(Integer.parseInt(form.getBookingId()));
        List costSheetFieldlist = null;
        costSheetFieldlist = bookingFclDAO.getworkOrderFieldsList(form.getBookingId());
        List costSheetFieldList = bookingFclDAO.getcostSheetFieldList(form.getBookingId());

        searchBookingReportDTO.setBookingflFcl(bookingFcl);
        searchBookingReportDTO.setObjectList1(costSheetFieldlist);
        searchBookingReportDTO.setObjectList(costSheetFieldList);
        searchBookingReportDTO.setFileName(fileName);
        searchBookingReportDTO.setContextPath(contextPath);
        searchBookingReportDTO = setRatesList(searchBookingReportDTO, messageResources, bookingFcl);
        bookingCostSheetPdfCreator.createReport(searchBookingReportDTO);
    }

    public BookingFcl calculateInsurance(BookingFcl bookingFcl, String insuranceAmount) throws Exception {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        Double a = bookingFcl.getCostofgoods();
        if (bookingFcl.getInsurancamt() == null || bookingFcl.getInsurancamt().equals("")) {
            bookingFcl.setInsurancamt(0.00);
        }
        if (insuranceAmount == null || insuranceAmount.equals("") || insuranceAmount.equals("0.0")) {
            insuranceAmount = "0.80";
        }
        Double insureAmt = Double.parseDouble(dbUtil.removeComma(insuranceAmount));
        if (bookingFcl.getTotalCharges() == null || bookingFcl.getTotalCharges().equals("")) {
            bookingFcl.setTotalCharges(0.00);
        }
        Double b = bookingFcl.getTotalCharges();

        Double c = ((a + b) * insureAmt) / 100;
        Double d = ((a + b + c) * 10) / 100;
        Double cif = a + b + c + d;
        Double insuranceCharge = Double.parseDouble(formatter.format((cif * insureAmt) / 100));
        bookingFcl.setInsureMarkUp(insuranceCharge);
        bookingFcl.setTotalCharges(b + insuranceCharge);
        return bookingFcl;
    }

    public Quotation getQuoteByFileNo(String fileno) throws Exception {
        return quotationDAO.getFileNoObject(fileno);
    }

    public FclBl getBlByFileNo(String fileno) throws Exception {
        return fclBlDAO.getFileNoObject(fileno.toString());
    }

    public List deleteLocalDrayage(List fclRates, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("DRAY")) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List deleteInsurance(List fclRates, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("INSURE")) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List deletePBACharges(List fclRates, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)
                    || bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE)
                    || bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE)) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List deleteFFCommission(List fclRates, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(FclBlConstants.FFCODE)) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List addLocalDrayage(List fclRates, BookingFcl bookingFcl) throws Exception {
        boolean flag = false;
        String[] unitType = new String[fclRates.size()];
        String[] number = new String[fclRates.size()];
        List newList = new ArrayList(fclRates);
        List chargesList = new ArrayList();
        for (Iterator iter = fclRates.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getChgCode().equalsIgnoreCase("Drayage")) {
                flag = true;
                bookingFcl.setAmount(bookingfclUnits.getAmount());
                bookingFcl.setDrayMarkUp(bookingfclUnits.getMarkUp());
                newList.remove(bookingfclUnits);
            } else if (bookingfclUnits.getChgCode().equalsIgnoreCase("Intermodal")) {
                flag = true;
                bookingFcl.setAmount1(bookingfclUnits.getAmount());
                bookingFcl.setInterMarkUp(bookingfclUnits.getMarkUp());
                newList.remove(bookingfclUnits);
            } else if (bookingfclUnits.getChgCode().equalsIgnoreCase("FF COMMISSION")) {
                newList.remove(bookingfclUnits);
            } else if (bookingfclUnits.getChgCode().equalsIgnoreCase("INSURANCE")) {
                newList.remove(bookingfclUnits);
            }
        }
        if (flag) {
            //if local drayage is not present then add local drayage and set all its properties
            int i = 0;
            for (Iterator iter = newList.iterator(); iter.hasNext();) {
                BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
                if (bookingfclUnits.getUnitType() != null) {
                    unitType[i] = bookingfclUnits.getUnitType().getCodedesc();
                }
                number[i] = bookingfclUnits.getNumbers();
                chargesList.add(bookingfclUnits);
                i++;
            }
            String unitLast = "";
            String numberLast = "";
            int temp = 0;
            for (int j = 0; j < unitType.length; j++) {
                unitLast = unitType[j];
                numberLast = number[j];
                if (j + 1 < unitType.length) {
                    if (unitType[j + 1] != unitType[j]) {
                        //adding localdrayage for a perticular unittype
                        BookingfclUnits newBookingfclUnits = new BookingfclUnits();
                        newBookingfclUnits.setCostType("Flat Rate Per Container");
                        newBookingfclUnits.setChgCode("Drayage");
                        newBookingfclUnits.setChargeCodeDesc("DRAY");
                        if (number[j] == null) {
                            number[j] = "1";
                        }
                        int num = Integer.parseInt(number[j]);
                        if (bookingFcl.getAmount() != null) {
                            newBookingfclUnits.setAmount(bookingFcl.getAmount());
                        }
                        Double double1 = 0.00;
                        newBookingfclUnits.setMarkUp(bookingFcl.getDrayMarkUp());
                        newBookingfclUnits.setSellRate(newBookingfclUnits.getAmount() + newBookingfclUnits.getMarkUp());
                        newBookingfclUnits.setEfectiveDate(new Date());
                        newBookingfclUnits.setNumbers(String.valueOf(num));
                        newBookingfclUnits.setCurrency("USD");
                        newBookingfclUnits.setPrint("off");
                        newBookingfclUnits.setManualCharges("M");
                        newBookingfclUnits.setNewFlag("new");
                        if (bookingFcl.getSslname() != null) {
                            int k = bookingFcl.getSslname().indexOf("//");
                            if (k != -1) {
                                String ssname[] = bookingFcl.getSslname().split("//");
                                newBookingfclUnits.setAccountName(ssname[0]);
                                newBookingfclUnits.setAccountNo(ssname[1]);
                            }
                        }
                        newBookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
                        List list = genericCodeDAO.findByCodedesc(unitType[j]);
                        newBookingfclUnits.setUnitType((GenericCode) list.get(0));
                        chargesList.add(j + 1 + temp, newBookingfclUnits);
                        temp++;
                    }
                }

            }
            //adding localdrayage if only 1 unittype is present or else if more than 1 is present then adding to last unit type
            BookingfclUnits newBookingfclUnits1 = new BookingfclUnits();
            newBookingfclUnits1.setCostType("Flat Rate Per Container");
            newBookingfclUnits1.setChgCode("Drayage");
            if (numberLast == null || numberLast.equals("")) {
                numberLast = "1";
            }
            int num = Integer.parseInt(numberLast);
            newBookingfclUnits1.setAmount(bookingFcl.getAmount());
            newBookingfclUnits1.setNumbers(String.valueOf(num));
            newBookingfclUnits1.setChargeCodeDesc("DRAY");
            newBookingfclUnits1.setMarkUp(bookingFcl.getDrayMarkUp());
            newBookingfclUnits1.setSellRate(newBookingfclUnits1.getAmount() + newBookingfclUnits1.getMarkUp());
            newBookingfclUnits1.setEfectiveDate(new Date());
            newBookingfclUnits1.setCurrency("USD");
            newBookingfclUnits1.setPrint("off");
            newBookingfclUnits1.setManualCharges("M");
            newBookingfclUnits1.setNewFlag("new");
            if (bookingFcl.getSslname() != null) {
                int k = bookingFcl.getSslname().indexOf("//");
                if (k != -1) {
                    String ssname[] = bookingFcl.getSslname().split("//");
                    newBookingfclUnits1.setAccountName(ssname[0]);
                    newBookingfclUnits1.setAccountNo(ssname[1]);
                }
            }
            List list = genericCodeDAO.findByCodedesc(unitLast);
            if (list != null && !list.isEmpty()) {
                newBookingfclUnits1.setUnitType((GenericCode) list.get(0));
            }
            newBookingfclUnits1.setBookingNumber(bookingFcl.getBookingNumber());

            chargesList.add(newBookingfclUnits1);
        }
        if (flag) {
            return chargesList;
        } else {
            return fclRates;
        }
    }

    public List deleteIntermodal(List fclRates, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < newfclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) newfclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("INTMDL")) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List deleteInland(List fclRates, BookingFcl bookingFcl) throws Exception {
        for (Iterator<BookingfclUnits> iterator = fclRates.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = iterator.next();
            boolean importFlag = null != bookingFcl.getImportFlag() && bookingFcl.getImportFlag().equalsIgnoreCase("I");
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(importFlag ? "DELIV" : "INLAND")) {
                iterator.remove();
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return fclRates;
    }

    public List deleteIntermodalRamp(List fclRates, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < newfclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) newfclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("INTRAMP")) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List deleteDocCharge(List otherChargesList, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(otherChargesList);
        for (int i = 0; i < newfclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) newfclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("DOCUM")) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List deletePierPassCharge(List fclRates, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < newfclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) newfclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("PIERPA")) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List deleteSpecialEquipmentCharges(List fclRates) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(i);
            if (CommonUtils.isNotEmpty(bookingfclUnits.getSpecialEquipmentUnit())) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List deleteSpecialEquipmentUnitCharges(List fclRates, String unitCode, String index) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(i);
            if (CommonUtils.isNotEmpty(unitCode) && unitCode.equals(bookingfclUnits.getSpecialEquipmentUnit())
                    && CommonUtils.isNotEmpty(index) && index.equals(bookingfclUnits.getStandardCharge())) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }

    public List addIntermodal(List fclRates, BookingFcl bookingFcl) throws Exception {
        List chargesList = new ArrayList();
        boolean flag = false;

        List newList = new ArrayList(fclRates);

        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();

            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("INTMDL")) {
                bookingFcl.setAmount1(bookingfclUnits.getAmount());
                bookingFcl.setInterMarkUp(bookingfclUnits.getMarkUp());
                newList.remove(bookingfclUnits);
            }
        }
        String[] unitType = new String[newList.size()];
        String[] number = new String[newList.size()];
        //if Intermodal is not present then add Intermodal and set all its properties
        int i = 0;
        for (Iterator iter = newList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getUnitType() != null) {
                unitType[i] = bookingfclUnits.getUnitType().getCodedesc();
            }
            number[i] = bookingfclUnits.getNumbers();
            chargesList.add(bookingfclUnits);
            i++;
        }
        String unitLast = "";
        String numberLast = "";
        int temp = 0;
        for (int j = 0; j < unitType.length; j++) {
            if (unitType[j] != null) {
                unitLast = unitType[j];
            }
            numberLast = number[j];
            if (j + 1 < unitType.length) {
                if (unitType[j + 1] != unitType[j]) {
                    //adding Intermodal for a perticular unittype
                    BookingfclUnits newBookingfclUnits = new BookingfclUnits();
                    newBookingfclUnits.setCostType("Flat Rate Per Container");
                    newBookingfclUnits.setChgCode("INTERMODAL");
                    newBookingfclUnits.setChargeCodeDesc("INTMDL");
                    if (number[j] == null) {
                        number[j] = "1";
                    }
                    int num = Integer.parseInt(number[j]);
                    if (bookingFcl.getAmount1() != null) {
                        newBookingfclUnits.setAmount(bookingFcl.getAmount1());
                    }
                    Double double1 = 0.00;
                    newBookingfclUnits.setMarkUp(bookingFcl.getInterMarkUp());
                    newBookingfclUnits.setSellRate(newBookingfclUnits.getAmount() + newBookingfclUnits.getMarkUp());
                    newBookingfclUnits.setEfectiveDate(new Date());
                    newBookingfclUnits.setNumbers(String.valueOf(num));
                    newBookingfclUnits.setCurrency("USD");
                    newBookingfclUnits.setPrint("off");
                    newBookingfclUnits.setManualCharges("M");
                    newBookingfclUnits.setNewFlag("new");
                    if (bookingFcl.getSslname() != null) {
                        int k = bookingFcl.getSslname().indexOf("//");
                        if (k != -1) {
                            String ssname[] = bookingFcl.getSslname().split("//");
                            newBookingfclUnits.setAccountName(ssname[0]);
                            newBookingfclUnits.setAccountNo(ssname[1]);
                        }
                    }
                    newBookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
                    List list = genericCodeDAO.findByCodedesc(unitType[j]);
                    newBookingfclUnits.setUnitType((GenericCode) list.get(0));
                    flag = true;
                    chargesList.add(j + 1 + temp, newBookingfclUnits);
                    temp++;
                }
            }

        }
        //adding Intermodal if only 1 unittype is present or else if more than 1 is present then adding to last unit type
        if (fclRates.size() > 0) {
            BookingfclUnits newBookingfclUnits1 = new BookingfclUnits();
            newBookingfclUnits1.setCostType("Flat Rate Per Container");
            newBookingfclUnits1.setChgCode("INTERMODAL");
            if (numberLast == null || numberLast.equals("")) {
                numberLast = "1";
            }
            int num = Integer.parseInt(numberLast);
            newBookingfclUnits1.setAmount(bookingFcl.getAmount1());
            newBookingfclUnits1.setNumbers(String.valueOf(num));
            newBookingfclUnits1.setChargeCodeDesc("INTMDL");
            newBookingfclUnits1.setMarkUp(bookingFcl.getInterMarkUp());
            newBookingfclUnits1.setSellRate(newBookingfclUnits1.getAmount() + newBookingfclUnits1.getMarkUp());
            newBookingfclUnits1.setEfectiveDate(new Date());
            newBookingfclUnits1.setCurrency("USD");
            newBookingfclUnits1.setPrint("off");
            newBookingfclUnits1.setManualCharges("M");
            newBookingfclUnits1.setNewFlag("new");
            if (bookingFcl.getSslname() != null) {
                int k = bookingFcl.getSslname().indexOf("//");
                if (k != -1) {
                    String ssname[] = bookingFcl.getSslname().split("//");
                    newBookingfclUnits1.setAccountName(ssname[0]);
                    newBookingfclUnits1.setAccountNo(ssname[1]);
                }
            }
            List list = genericCodeDAO.findByCodedesc(unitLast);
            if (list.size() > 0) {
                newBookingfclUnits1.setUnitType((GenericCode) list.get(0));
            }
            newBookingfclUnits1.setBookingNumber(bookingFcl.getBookingNumber());
            flag = true;
            chargesList.add(newBookingfclUnits1);
        }
        if (!flag) {
            return fclRates;
        } else {
            return chargesList;
        }
    }

    public List addDocumentCharge(List otherChargesList, BookingFcl bookingFcl, String amount) throws Exception {
        List newList = new ArrayList(otherChargesList);
        for (Iterator iterator = otherChargesList.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();

            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("DOCUM")) {
                newList.remove(bookingfclUnits);
            }
        }
        BookingfclUnits bookingfclUnits = new BookingfclUnits();
        bookingfclUnits.setCostType("PER BL CHARGES");
        bookingfclUnits.setChgCode("DOCUMENT CHARGE");
        bookingfclUnits.setChargeCodeDesc("DOCUM");
        bookingfclUnits.setAccountName("");
        bookingfclUnits.setAccountNo("");
        bookingfclUnits.setAmount(0.00);
        bookingfclUnits.setMarkUp(Double.parseDouble(dbUtil.removeComma(amount)));
        bookingfclUnits.setEfectiveDate(new Date());
        bookingfclUnits.setCurrency("USD");
        bookingfclUnits.setPrint("off");
        bookingfclUnits.setUnitType(null);
        bookingfclUnits.setManualCharges("D");
        bookingfclUnits.setNewFlag("new");
        bookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
        newList.add(bookingfclUnits);
        return newList;
    }

    public List addPierPassCharge(List fclRates, BookingFcl bookingFcl) throws Exception {

        int no = 0;
        for (int a = 0; a < fclRates.size(); a++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(a);
            BookingfclUnits prevBookingfclUnits = null;
            if (a != 0) {
                prevBookingfclUnits = (BookingfclUnits) fclRates.get(a - 1);
                if (null != bookingfclUnits && null != prevBookingfclUnits && !bookingfclUnits.getUnitType().equals(prevBookingfclUnits.getUnitType())) {
                    if (prevBookingfclUnits.getNumbers() == null) {
                        prevBookingfclUnits.setNumbers("1");
                    }
                    if (prevBookingfclUnits.getNumbers() != null) {
                        no = no + Integer.parseInt(prevBookingfclUnits.getNumbers());
                    }
                }
            }
        }
        if (fclRates.size() > 0) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(fclRates.size() - 1);
            if (bookingfclUnits.getNumbers() != null) {
                no = no + Integer.parseInt(bookingfclUnits.getNumbers());
            }
        }
        List chargesList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        boolean flag = false;
        String insuranceComment = "";
        List tempList = new ArrayList(fclRates);
        String[] unitType = new String[fclRates.size()];
        String[] number = new String[fclRates.size()];
        Map<String, String> commentMap = new HashMap<String, String>();
        for (Iterator iter = tempList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("PIERPA")) {
                if (null != bookingfclUnits.getUnitType() && CommonUtils.isNotEmpty(bookingfclUnits.getComment())) {
                    commentMap.put(bookingfclUnits.getUnitType().getCodedesc(), bookingfclUnits.getComment());
                }
                flag = true;
                fclRates.remove(bookingfclUnits);
            }
        }

        Double totalCharges = 0.00;

        int i = 0;
        for (Iterator iter = fclRates.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getUnitType() != null) {
                unitType[i] = bookingfclUnits.getUnitType().getCodedesc();
                number[i] = bookingfclUnits.getNumbers();
                i++;
            }

        }
        String unitLast = "";
        String numberLast = "";
        int temp = 0;
        String prevUnitValue = "";
        String unitValue = "";
        fclRates = orderExpandList(fclRates);
        for (int j = 0; j < fclRates.size(); j++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(j);
            if (null == bookingfclUnits.getSpecialEquipmentUnit()) {
                bookingfclUnits.setSpecialEquipmentUnit("");
            }
            if (null == bookingfclUnits.getStandardCharge()) {
                bookingfclUnits.setStandardCharge("");
            }
            if (null != bookingfclUnits.getUnitType()) {
                unitValue = bookingfclUnits.getUnitType().getId() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge();
            }
            BookingfclUnits bookingfclUnitsPrev = null;
            if (j != 0) {
                bookingfclUnitsPrev = (BookingfclUnits) fclRates.get(j - 1);
                if (null == bookingfclUnitsPrev.getSpecialEquipmentUnit()) {
                    bookingfclUnitsPrev.setSpecialEquipmentUnit("");
                }
                if (null == bookingfclUnitsPrev.getStandardCharge()) {
                    bookingfclUnitsPrev.setStandardCharge("");
                }
                if (null != bookingfclUnitsPrev.getUnitType()) {
                    prevUnitValue = bookingfclUnitsPrev.getUnitType().getId() + "-" + bookingfclUnitsPrev.getSpecialEquipmentUnit() + "-" + bookingfclUnitsPrev.getStandardCharge();
                }
                if (bookingfclUnitsPrev.getUnitType() != null && bookingfclUnits.getUnitType() != null && !prevUnitValue.equals(unitValue)) {
                    BookingfclUnits newBookingfclUnits = new BookingfclUnits();
                    newBookingfclUnits.setCostType("Flat Rate Per Container");
                    newBookingfclUnits.setChgCode("PIER PASS");
                    newBookingfclUnits.setChargeCodeDesc("PIERPA");
                    if (number[j] == null) {
                        number[j] = "1";
                    }
                    int num = Integer.parseInt(number[j]);
                    if (bookingfclUnitsPrev.getAmount() != null && !"new".equalsIgnoreCase(bookingfclUnitsPrev.getNewFlag())) {
                        totalCharges += bookingfclUnitsPrev.getAmount();
                    }
                    if (bookingfclUnitsPrev.getMarkUp() != null) {
                        totalCharges += bookingfclUnitsPrev.getMarkUp();
                    }
                    if (bookingfclUnitsPrev.getAdjustment() != null) {
                        totalCharges += bookingfclUnitsPrev.getAdjustment();
                    }
//                    bookingFcl.setTotalCharges(totalCharges);
//                    bookingFcl = calculateInsurance(bookingFcl, bookingFcl.getInsurancamt().toString());
//                    if (bookingFcl.getInsureMarkUp() != null) {
//                        newBookingfclUnits.setMarkUp((bookingFcl.getInsureMarkUp()));
//                    }
                    Double double1 = Double.parseDouble(new PropertyDAO().getProperty("pier.pass.amount"));
                    newBookingfclUnits.setAmount(double1);
                    newBookingfclUnits.setAccountName(new PropertyDAO().getProperty("pier.pass.vendor"));
                    newBookingfclUnits.setAccountNo(new PropertyDAO().getProperty("pier.pass.vendor"));
                    newBookingfclUnits.setMarkUp(0.00);
                    newBookingfclUnits.setSpecialEquipment(bookingfclUnitsPrev.getSpecialEquipment());
                    newBookingfclUnits.setSpecialEquipmentUnit(bookingfclUnitsPrev.getSpecialEquipmentUnit());
                    newBookingfclUnits.setStandardCharge(bookingfclUnitsPrev.getStandardCharge());
                    newBookingfclUnits.setSellRate(newBookingfclUnits.getAmount() + newBookingfclUnits.getMarkUp());
                    newBookingfclUnits.setEfectiveDate(new Date());
                    newBookingfclUnits.setNumbers(String.valueOf(num));
                    newBookingfclUnits.setCurrency("USD");
                    newBookingfclUnits.setPrint("off");
                    newBookingfclUnits.setManualCharges("M");
                    newBookingfclUnits.setNewFlag("PP");
                    if (null != bookingfclUnitsPrev.getUnitType()) {
                        newBookingfclUnits.setComment(commentMap.get(bookingfclUnitsPrev.getUnitType().getCodedesc()));
                    }

                    newBookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
                    newBookingfclUnits.setUnitType(bookingfclUnitsPrev.getUnitType());
                    newBookingfclUnits.setUnitName(bookingfclUnitsPrev.getUnitType().getCodedesc());
                    if (!"A=20".equalsIgnoreCase(newBookingfclUnits.getUnitName())) {
                        double amount = newBookingfclUnits.getAmount();
                        double markup = newBookingfclUnits.getMarkUp();
                        if ((amount != 133.00) && (markup != 133.00)) {
                            newBookingfclUnits.setAmount(newBookingfclUnits.getAmount() * 2.00);
                            //newBookingfclUnits.setMarkUp(newBookingfclUnits.getMarkUp() * 2.00);
                        }

                    }
                    chargesList.add(newBookingfclUnits);
                    totalCharges = 0.00;
                    temp++;
                } else {
                    if (!"new".equalsIgnoreCase(bookingfclUnitsPrev.getNewFlag())) {
                        totalCharges += bookingfclUnitsPrev.getAmount();
                    }
                    if (bookingfclUnitsPrev.getMarkUp() != null) {
                        totalCharges += bookingfclUnitsPrev.getMarkUp();
                    }
                    if (bookingfclUnitsPrev.getAdjustment() != null) {
                        totalCharges += bookingfclUnitsPrev.getAdjustment();
                    }
                }
            }
            chargesList.add(bookingfclUnits);
        }
        if (fclRates.size() > 0) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(fclRates.size() - 1);
            BookingfclUnits newBookingfclUnits1 = new BookingfclUnits();
            newBookingfclUnits1.setCostType("Flat Rate Per Container");
            newBookingfclUnits1.setChgCode("PIER PASS");
            if (numberLast == null || numberLast.equals("")) {
                numberLast = "1";
            }
            int num = Integer.parseInt(numberLast);
            if (bookingfclUnits.getAmount() != null && !"new".equalsIgnoreCase(bookingfclUnits.getNewFlag())) {
                totalCharges += bookingfclUnits.getAmount();
            }
            if (bookingfclUnits.getMarkUp() != null) {
                totalCharges += bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getAdjustment() != null) {
                totalCharges += bookingfclUnits.getAdjustment();
            }
            bookingFcl.setTotalCharges(totalCharges);
            // bookingFcl = calculateInsurance(bookingFcl, bookingFcl.getInsurancamt().toString());
            newBookingfclUnits1.setAmount(Double.parseDouble(new PropertyDAO().getProperty("pier.pass.amount")));
            newBookingfclUnits1.setMarkUp(0.00);
            newBookingfclUnits1.setSpecialEquipment(bookingfclUnits.getSpecialEquipment());
            newBookingfclUnits1.setSpecialEquipmentUnit(bookingfclUnits.getSpecialEquipmentUnit());
            newBookingfclUnits1.setStandardCharge(bookingfclUnits.getStandardCharge());
            newBookingfclUnits1.setNumbers(String.valueOf(num));
            newBookingfclUnits1.setChargeCodeDesc("PIERPA");
            newBookingfclUnits1.setSellRate(newBookingfclUnits1.getAmount() + newBookingfclUnits1.getMarkUp());
            newBookingfclUnits1.setEfectiveDate(new Date());
            newBookingfclUnits1.setCurrency("USD");
            newBookingfclUnits1.setPrint("off");
            newBookingfclUnits1.setManualCharges("M");
            newBookingfclUnits1.setNewFlag("PP");
            newBookingfclUnits1.setAccountName(new PropertyDAO().getProperty("pier.pass.vendor"));
            newBookingfclUnits1.setAccountNo(new PropertyDAO().getProperty("pier.pass.vendor"));
            if (null != bookingfclUnits.getUnitType()) {
                newBookingfclUnits1.setComment(commentMap.get(bookingfclUnits.getUnitType().getCodedesc()));
            }
            newBookingfclUnits1.setUnitType(bookingfclUnits.getUnitType());
            if (null != bookingfclUnits && null != bookingfclUnits.getUnitType()) {
                newBookingfclUnits1.setUnitName(bookingfclUnits.getUnitType().getCodedesc());
            }
            newBookingfclUnits1.setBookingNumber(bookingFcl.getBookingNumber());
            if (!"A=20".equalsIgnoreCase(newBookingfclUnits1.getUnitName())) {
                double amount = newBookingfclUnits1.getAmount();
                double markup = newBookingfclUnits1.getMarkUp();
                if ((amount != 133.00) && (markup != 133.00)) {
                    newBookingfclUnits1.setAmount(newBookingfclUnits1.getAmount() * 2.00);
                    //newBookingfclUnits.setMarkUp(newBookingfclUnits.getMarkUp() * 2.00);
                }

            }
            chargesList.add(newBookingfclUnits1);
        }
        return chargesList;

//        List newList = new ArrayList(otherChargesList);
//        for (Iterator iterator = otherChargesList.iterator(); iterator.hasNext();) {
//            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
//
//            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("PIERPASS")) {
//                newList.remove(bookingfclUnits);
//            }
//        }
//        BookingfclUnits bookingfclUnits = new BookingfclUnits();
//        bookingfclUnits.setCostType("PER BL CHARGES");
//        bookingfclUnits.setChgCode("PIER PASS CHARGE");
//        bookingfclUnits.setChargeCodeDesc("PIERPASS");
//        bookingfclUnits.setAccountName(new PropertyDAO().getProperty("pier.pass.vendor"));
//        bookingfclUnits.setAccountNo(new PropertyDAO().getProperty("pier.pass.vendor"));
//        bookingfclUnits.setAmount(0.00);
//        bookingfclUnits.setMarkUp(Double.parseDouble(new PropertyDAO().getProperty("pier.pass.amount")));
//        bookingfclUnits.setEfectiveDate(new Date());
//        bookingfclUnits.setCurrency("USD");
//        bookingfclUnits.setPrint("off");
//        bookingfclUnits.setUnitType(null);
//        bookingfclUnits.setManualCharges("P");
//        bookingfclUnits.setNewFlag("new");
//        bookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
//        newList.add(bookingfclUnits);
//        return newList;
    }

    public List addLocalDrayageToBlManually(List ratesList, BookingFcl bookingFcl, String unitType, MessageResources messageResources, List fclRatesList1) throws Exception {
        BookingfclUnits bookingfclUnits = new BookingfclUnits();
        bookingfclUnits.setCostType("Flat Rate Per Container");
        bookingfclUnits.setChgCode("Percent Of Drayage");
        bookingfclUnits.setChargeCodeDesc("DRAY");
        bookingfclUnits.setAmount(bookingFcl.getAmount());
        bookingfclUnits.setMarkUp(0.00);
        bookingfclUnits.setEfectiveDate(new Date());
        bookingfclUnits.setCurrency("USD");
        bookingfclUnits.setNumbers("1");
        bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
        if (unitType != null && !unitType.equals("0")) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(unitType));
            bookingfclUnits.setUnitType(gen);
            bookingfclUnits.setUnitName(gen.getCodedesc());
        }
        if (bookingFcl.getSslname() != null) {
            int k = bookingFcl.getSslname().indexOf("//");
            if (k != -1) {
                String ssname[] = bookingFcl.getSslname().split("//");
                bookingfclUnits.setAccountName(ssname[0]);
                bookingfclUnits.setAccountNo(ssname[1]);
            }
        }
        boolean flag = false;
        for (Iterator iter = ratesList.iterator(); iter.hasNext();) {
            BookingfclUnits tempBookingfclUnits = (BookingfclUnits) iter.next();
            if (tempBookingfclUnits.getChargeCodeDesc().equals(bookingfclUnits.getChargeCodeDesc())
                    && tempBookingfclUnits.getUnitType().getId().toString().equals(bookingfclUnits.getUnitType().getId().toString())) {
                flag = true;
                break;
            }
        }
        if (!flag && bookingFcl.getLocaldryage() != null && bookingFcl.getLocaldryage().equals("Y")) {
            fclRatesList1.add(bookingfclUnits);
        }
        return fclRatesList1;
    }

    public List addIntermodelToBlManually(List ratesList, BookingFcl bookingFcl, String unitType, MessageResources messageResources, List fclRatesList1) throws Exception {
        BookingfclUnits bookingfclUnits = new BookingfclUnits();
        bookingfclUnits.setCostType("Flat Rate Per Container");
        bookingfclUnits.setChgCode("INTMDL");
        bookingfclUnits.setChargeCodeDesc("INTMDL");
        bookingfclUnits.setAmount(bookingFcl.getAmount1());
        bookingfclUnits.setMarkUp(0.00);
        bookingfclUnits.setEfectiveDate(new Date());
        bookingfclUnits.setCurrency("USD");
        bookingfclUnits.setNumbers("1");
        bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
        if (unitType != null && !unitType.equals("0")) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(unitType));
            bookingfclUnits.setUnitType(gen);
            bookingfclUnits.setUnitName(gen.getCodedesc());
        }
        if (bookingFcl.getSslname() != null) {
            int k = bookingFcl.getSslname().indexOf("//");
            if (k != -1) {
                String ssname[] = bookingFcl.getSslname().split("//");
                bookingfclUnits.setAccountName(ssname[0]);
                bookingfclUnits.setAccountNo(ssname[1]);
            }
        }
        boolean flag = false;
        for (Iterator iter = ratesList.iterator(); iter.hasNext();) {
            BookingfclUnits tempBookingfclUnits = (BookingfclUnits) iter.next();
            if (tempBookingfclUnits.getChargeCodeDesc().equals(bookingfclUnits.getChargeCodeDesc())
                    && tempBookingfclUnits.getUnitType().getId().toString().equals(bookingfclUnits.getUnitType().getId().toString())) {
                flag = true;
                break;
            }
        }
        if (!flag && bookingFcl.getIntermodel() != null && bookingFcl.getIntermodel().equals("Y")) {
            fclRatesList1.add(bookingfclUnits);
        }
        return fclRatesList1;
    }

    public List getInsuranceCostofGoodsForNonRated(List fclRates, BookingFcl bookingFcl) throws Exception {
        List chargesList = new ArrayList();
        Double totalCharges = 0.00;
        boolean insurance = false;
        for (int j = 0; j < fclRates.size(); j++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(j);
            if (!"INSURE".equalsIgnoreCase(bookingfclUnits.getChargeCodeDesc())) {
                if (bookingfclUnits.getAmount() != null && !"new".equalsIgnoreCase(bookingfclUnits.getNewFlag())) {
                    totalCharges += bookingfclUnits.getAmount();
                }
                if (bookingfclUnits.getMarkUp() != null) {
                    totalCharges += bookingfclUnits.getMarkUp();
                }
            }
            chargesList.add(bookingfclUnits);
        }
        for (int i = 0; i < chargesList.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) chargesList.get(i);
            if ("INSURE".equalsIgnoreCase(bookingfclUnits.getChargeCodeDesc())) {
                bookingFcl.setTotalCharges(totalCharges);
                bookingFcl = calculateInsurance(bookingFcl, bookingFcl.getInsurancamt().toString());
                if (bookingFcl.getInsureMarkUp() != null) {
                    bookingfclUnits.setMarkUp((bookingFcl.getInsureMarkUp()));
                }
                Double double1 = 0.00;
                bookingfclUnits.setAmount(double1);
                insurance = true;
            }
        }
        if (!insurance && chargesList.size() > 0) {
            BookingfclUnits bookingfclUnitsPrev = (BookingfclUnits) fclRates.get(fclRates.size() - 1);
            BookingfclUnits newBookingfclUnits = new BookingfclUnits();
            newBookingfclUnits.setCostType("PER BL CHARGES");
            newBookingfclUnits.setChgCode("INSURANCE");
            newBookingfclUnits.setChargeCodeDesc("INSURE");
            bookingFcl.setTotalCharges(totalCharges);
            bookingFcl = calculateInsurance(bookingFcl, bookingFcl.getInsurancamt().toString());
            if (bookingFcl.getInsureMarkUp() != null) {
                newBookingfclUnits.setMarkUp((bookingFcl.getInsureMarkUp()));
            }
            Double double1 = 0.00;
            newBookingfclUnits.setAmount(double1);
            newBookingfclUnits.setSpecialEquipment(bookingfclUnitsPrev.getSpecialEquipment());
            newBookingfclUnits.setSpecialEquipmentUnit(bookingfclUnitsPrev.getSpecialEquipmentUnit());
            newBookingfclUnits.setStandardCharge(bookingfclUnitsPrev.getStandardCharge());
            newBookingfclUnits.setSellRate(newBookingfclUnits.getAmount() + newBookingfclUnits.getMarkUp());
            newBookingfclUnits.setEfectiveDate(new Date());
            newBookingfclUnits.setCurrency("USD");
            newBookingfclUnits.setPrint("off");
            newBookingfclUnits.setManualCharges("M");
            newBookingfclUnits.setNewFlag("IN");
            newBookingfclUnits.setAccountName(null != bookingFcl.getSslname() && bookingFcl.getSslname().indexOf("//") != -1 ? bookingFcl.getSslname().substring(0, bookingFcl.getSslname().indexOf("//")) : bookingFcl.getSslname());
            newBookingfclUnits.setAccountNo(bookingFcl.getSSLine());
            newBookingfclUnits.setBookingNumber("" + bookingFcl.getBookingId());
            new BookingfclUnitsDAO().save(newBookingfclUnits);
            chargesList.add(newBookingfclUnits);
        }
        return chargesList;
    }

    public List getInsuranceCostofGoods(List fclRates, BookingFcl bookingFcl) throws Exception {

        int no = 0;
        for (int a = 0; a < fclRates.size(); a++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(a);
            BookingfclUnits prevBookingfclUnits = null;
            if (a != 0) {
                prevBookingfclUnits = (BookingfclUnits) fclRates.get(a - 1);
                if (bookingfclUnits.getUnitType() != null && prevBookingfclUnits.getUnitType() != null && !bookingfclUnits.getUnitType().getId().toString().equals(prevBookingfclUnits.getUnitType().getId().toString())) {
                    if (prevBookingfclUnits.getNumbers() == null) {
                        prevBookingfclUnits.setNumbers("1");
                    }
                    if (prevBookingfclUnits.getNumbers() != null) {
                        no = no + Integer.parseInt(prevBookingfclUnits.getNumbers());
                    }
                }
            }
        }
        if (fclRates.size() > 0) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(fclRates.size() - 1);
            if (bookingfclUnits.getNumbers() != null) {
                no = no + Integer.parseInt(bookingfclUnits.getNumbers());
            }
        }
        List chargesList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        boolean flag = false;
        String insuranceComment = "";
        List tempList = new ArrayList(fclRates);
        String[] unitType = new String[fclRates.size()];
        String[] number = new String[fclRates.size()];
        Map<String, String> commentMap = new HashMap<String, String>();
        for (Iterator iter = tempList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("INSURE")) {
                if (null != bookingfclUnits.getUnitType() && CommonUtils.isNotEmpty(bookingfclUnits.getComment())) {
                    commentMap.put(bookingfclUnits.getUnitType().getCodedesc(), bookingfclUnits.getComment());
                }
                flag = true;
                fclRates.remove(bookingfclUnits);
            }
        }

        Double totalCharges = 0.00;

        int i = 0;
        for (Iterator iter = fclRates.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getUnitType() != null) {
                unitType[i] = bookingfclUnits.getUnitType().getCodedesc();
                number[i] = bookingfclUnits.getNumbers();
                i++;
            }

        }
        String unitLast = "";
        String numberLast = "";
        int temp = 0;
        String prevUnitValue = "";
        String unitValue = "";
        fclRates = orderExpandList(fclRates);
        for (int j = 0; j < fclRates.size(); j++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(j);
            if (null == bookingfclUnits.getSpecialEquipmentUnit()) {
                bookingfclUnits.setSpecialEquipmentUnit("");
            }
            if (null == bookingfclUnits.getStandardCharge()) {
                bookingfclUnits.setStandardCharge("");
            }
            if (null != bookingfclUnits.getUnitType()) {
                unitValue = bookingfclUnits.getUnitType().getId() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge();
            }
            BookingfclUnits bookingfclUnitsPrev = null;
            if (j != 0) {
                bookingfclUnitsPrev = (BookingfclUnits) fclRates.get(j - 1);
                if (null == bookingfclUnitsPrev.getSpecialEquipmentUnit()) {
                    bookingfclUnitsPrev.setSpecialEquipmentUnit("");
                }
                if (null == bookingfclUnitsPrev.getStandardCharge()) {
                    bookingfclUnitsPrev.setStandardCharge("");
                }
                if (null != bookingfclUnitsPrev.getUnitType()) {
                    prevUnitValue = bookingfclUnitsPrev.getUnitType().getId() + "-" + bookingfclUnitsPrev.getSpecialEquipmentUnit() + "-" + bookingfclUnitsPrev.getStandardCharge();
                }
                if (bookingfclUnitsPrev.getUnitType() != null && bookingfclUnits.getUnitType() != null && !prevUnitValue.equals(unitValue)) {
                    BookingfclUnits newBookingfclUnits = new BookingfclUnits();
                    newBookingfclUnits.setCostType("Flat Rate Per Container");
                    newBookingfclUnits.setChgCode("INSURANCE");
                    newBookingfclUnits.setChargeCodeDesc("INSURE");
                    if (number[j] == null) {
                        number[j] = "1";
                    }
                    int num = Integer.parseInt(number[j]);
                    if (bookingfclUnitsPrev.getAmount() != null && !"new".equalsIgnoreCase(bookingfclUnitsPrev.getNewFlag())) {
                        totalCharges += bookingfclUnitsPrev.getAmount();
                    }
                    if (bookingfclUnitsPrev.getMarkUp() != null) {
                        totalCharges += bookingfclUnitsPrev.getMarkUp();
                    }
                    if (bookingfclUnitsPrev.getAdjustment() != null) {
                        totalCharges += bookingfclUnitsPrev.getAdjustment();
                    }
                    bookingFcl.setTotalCharges(totalCharges);
                    bookingFcl = calculateInsurance(bookingFcl, bookingFcl.getInsurancamt().toString());
                    if (bookingFcl.getInsureMarkUp() != null) {
                        newBookingfclUnits.setMarkUp((bookingFcl.getInsureMarkUp()));
                    }
                    Double double1 = 0.00;
                    newBookingfclUnits.setAmount(double1);
                    newBookingfclUnits.setSpecialEquipment(bookingfclUnitsPrev.getSpecialEquipment());
                    newBookingfclUnits.setSpecialEquipmentUnit(bookingfclUnitsPrev.getSpecialEquipmentUnit());
                    newBookingfclUnits.setStandardCharge(bookingfclUnitsPrev.getStandardCharge());
                    newBookingfclUnits.setSellRate(newBookingfclUnits.getAmount() + newBookingfclUnits.getMarkUp());
                    newBookingfclUnits.setEfectiveDate(new Date());
                    newBookingfclUnits.setNumbers(String.valueOf(num));
                    newBookingfclUnits.setCurrency("USD");
                    newBookingfclUnits.setPrint("off");
                    newBookingfclUnits.setManualCharges("M");
                    newBookingfclUnits.setNewFlag("IN");
                    if (null != bookingfclUnitsPrev.getUnitType()) {
                        newBookingfclUnits.setComment(commentMap.get(bookingfclUnitsPrev.getUnitType().getCodedesc()));
                    }

                    newBookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
                    newBookingfclUnits.setUnitType(bookingfclUnitsPrev.getUnitType());
                    newBookingfclUnits.setUnitName(bookingfclUnitsPrev.getUnitType().getCodedesc());
                    chargesList.add(newBookingfclUnits);
                    totalCharges = 0.00;
                    temp++;
                } else {
                    if (!"new".equalsIgnoreCase(bookingfclUnitsPrev.getNewFlag())) {
                        totalCharges += bookingfclUnitsPrev.getAmount();
                    }
                    if (bookingfclUnitsPrev.getMarkUp() != null) {
                        totalCharges += bookingfclUnitsPrev.getMarkUp();
                    }
                    if (bookingfclUnitsPrev.getAdjustment() != null) {
                        totalCharges += bookingfclUnitsPrev.getAdjustment();
                    }
                }
            }
            chargesList.add(bookingfclUnits);
        }
        if (fclRates.size() > 0) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(fclRates.size() - 1);
            BookingfclUnits newBookingfclUnits1 = new BookingfclUnits();
            newBookingfclUnits1.setCostType("Flat Rate Per Container");
            newBookingfclUnits1.setChgCode("INSURANCE");
            if (numberLast == null || numberLast.equals("")) {
                numberLast = "1";
            }
            int num = Integer.parseInt(numberLast);
            if (bookingfclUnits.getAmount() != null && !"new".equalsIgnoreCase(bookingfclUnits.getNewFlag())) {
                totalCharges += bookingfclUnits.getAmount();
            }
            if (bookingfclUnits.getMarkUp() != null) {
                totalCharges += bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getAdjustment() != null) {
                totalCharges += bookingfclUnits.getAdjustment();
            }
            bookingFcl.setTotalCharges(totalCharges);
            bookingFcl = calculateInsurance(bookingFcl, bookingFcl.getInsurancamt().toString());
            newBookingfclUnits1.setAmount(0.00);
            newBookingfclUnits1.setMarkUp(bookingFcl.getInsureMarkUp());
            newBookingfclUnits1.setSpecialEquipment(bookingfclUnits.getSpecialEquipment());
            newBookingfclUnits1.setSpecialEquipmentUnit(bookingfclUnits.getSpecialEquipmentUnit());
            newBookingfclUnits1.setStandardCharge(bookingfclUnits.getStandardCharge());
            newBookingfclUnits1.setNumbers(String.valueOf(num));
            newBookingfclUnits1.setChargeCodeDesc("INSURE");
            newBookingfclUnits1.setSellRate(newBookingfclUnits1.getAmount() + newBookingfclUnits1.getMarkUp());
            newBookingfclUnits1.setEfectiveDate(new Date());
            newBookingfclUnits1.setCurrency("USD");
            newBookingfclUnits1.setNewFlag("new");
            newBookingfclUnits1.setPrint("off");
            newBookingfclUnits1.setManualCharges("M");
            //newBookingfclUnits1.setNewFlag("new");
            newBookingfclUnits1.setNewFlag("IN");
            if (null != bookingfclUnits.getUnitType()) {
                newBookingfclUnits1.setComment(commentMap.get(bookingfclUnits.getUnitType().getCodedesc()));
            }
            newBookingfclUnits1.setUnitType(bookingfclUnits.getUnitType());
            if (null != bookingfclUnits && null != bookingfclUnits.getUnitType()) {
                newBookingfclUnits1.setUnitName(bookingfclUnits.getUnitType().getCodedesc());
            }
            newBookingfclUnits1.setBookingNumber(bookingFcl.getBookingNumber());
            chargesList.add(newBookingfclUnits1);
        }
        return chargesList;
    }

    public List addFFCommission(List fclRates, BookingFcl bookingFcl, MessageResources messageResources) throws Exception {
        boolean flag = false;
        String ffCommissionRates[] = LoadLogisoftProperties.getProperty("ffcommissionrates").split(",");
        String uniType[] = messageResources.getMessage("unittype").split(",");
        List newList = new ArrayList(fclRates);
        for (Iterator iter = fclRates.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(FclBlConstants.FFCODE)) {
                newList.remove(bookingfclUnits);
            }
        }
        List list = new BookingfclUnitsDAO().getGroupByCharges("" + bookingFcl.getBookingId());
        for (Object object : list) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) object;
            BookingfclUnits documentfclUnits = new BookingfclUnits();
            PropertyUtils.copyProperties(documentfclUnits, bookingfclUnits);
            documentfclUnits.setCostType("Flat Rate Per Container");
            documentfclUnits.setChgCode(FclBlConstants.FFCODEDESC);
            documentfclUnits.setChargeCodeDesc(FclBlConstants.FFCODE);
            String unitType = "" + documentfclUnits.getUnitType().getId();
            if (uniType[0].equals(unitType)) {
                documentfclUnits.setAmount(Double.parseDouble(ffCommissionRates[0]));
            } else {
                documentfclUnits.setAmount(Double.parseDouble(ffCommissionRates[1]));
            }
            Double double1 = 0.00;
            documentfclUnits.setMarkUp(double1);
            documentfclUnits.setSellRate(documentfclUnits.getAmount() + documentfclUnits.getMarkUp());
            documentfclUnits.setEfectiveDate(new Date());
            documentfclUnits.setCurrency("USD");
            documentfclUnits.setPrint("on");
            documentfclUnits.setManualCharges("M");
            documentfclUnits.setNewFlag("FF");
            if (bookingFcl.getSslname() != null) {
                int k = bookingFcl.getSslname().indexOf("//");
                if (k != -1) {
                    String ssname[] = bookingFcl.getSslname().split("//");
                    documentfclUnits.setAccountName(ssname[0]);
                    documentfclUnits.setAccountNo(ssname[1]);
                }
            }
            documentfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
            flag = true;
            newList.add(documentfclUnits);
        }
        if (!flag) {
            return fclRates;
        } else {
            return newList;
        }
    }

    public List deleteCharge(String index, List fclRates) throws Exception {
        StringBuffer message = new StringBuffer();
        if (index != null && !index.equals("")) {
            BookingfclUnits bookingfclUnits = bookingFclUnitsDAO.findById(new Integer(index));
            if (null != bookingfclUnits) {
                message.append("DELETED ->Charge Code - " + bookingfclUnits.getChargeCodeDesc() + " Cost Type - " + bookingfclUnits.getCostType() + " Cost - ");
                message.append(bookingfclUnits.getAmount() + " Sell -" + bookingfclUnits.getMarkUp() + " Currency - " + bookingfclUnits.getCurrency() + " Vendor Name - " + bookingfclUnits.getAccountName() + " Vendor Number -" + bookingfclUnits.getAccountNo() + " Comment -" + bookingfclUnits.getComment());
                if (bookingfclUnits.getUnitType() != null) {
                    message.append("Unit Type -" + bookingfclUnits.getUnitType().getCodedesc());
                }
                Notes note = new Notes();
                NotesBC notesBC = new NotesBC();
                note.setModuleId(NotesConstants.FILE);
                String fileNo = "";
                if (null != bookingfclUnits.getBookingNumber()) {
                    String bookingNo = bookingfclUnits.getBookingNumber().toString();
                    fileNo = bookingFclDAO.getFileNo(bookingNo);
                }
                note.setModuleRefId(fileNo);
                note.setUpdateDate(new Date());
                note.setNoteType(NotesConstants.AUTO);
                note.setNoteDesc(message.toString());
                notesBC.saveNotes(note);
                BookingfclUnits bookingfclUnitsToSendAsArugment = new BookingfclUnits();
                PropertyUtils.copyProperties(bookingfclUnitsToSendAsArugment, bookingfclUnits);
                bookingfclUnitsToSendAsArugment.setId(null);
                bookingFclUnitsDAO.delete(bookingfclUnits);
                deletePBASURCHARGE(bookingfclUnitsToSendAsArugment, fclRates);
            }
            deleteFromChargeList(fclRates, index);
        }

        return fclRates;
    }

    public void changeChargeToPerBl(String index, String chargeCode, String userName, String bookingNumber) throws Exception {
        StringBuffer message = new StringBuffer();
        if (index != null && !index.equals("")) {
            List bookingfclUnitsList = bookingFclUnitsDAO.getByBookingNumberAndChargeCode(bookingNumber, chargeCode);
            message.append("Changed ->Charge Code - " + chargeCode);
            message.append(" Cost Type - PER CONTAINER to PER BL CHARGES ");
            Notes note = new Notes();
            NotesBC notesBC = new NotesBC();
            note.setModuleId(NotesConstants.FILE);
            String fileNo = "";
            if (null != bookingNumber) {
                fileNo = bookingFclDAO.getFileNo(bookingNumber);
            }
            note.setModuleRefId(fileNo);
            note.setUpdateDate(new Date());
            note.setUpdatedBy(userName);
            note.setNoteType(NotesConstants.AUTO);
            note.setNoteDesc(message.toString());
            notesBC.saveNotes(note);
            if (CommonUtils.isNotEmpty(bookingfclUnitsList)) {
                for (Object object : bookingfclUnitsList) {
                    BookingfclUnits bookingfclUnits = (BookingfclUnits) object;
                    bookingFclUnitsDAO.delete(bookingfclUnits);
                }
            }
        }

    }

    public void deleteFromChargeList(List fclRates, String index) throws Exception {
        if (CommonFunctions.isNotNullOrNotEmpty(fclRates)) {
            for (Iterator iter = fclRates.iterator(); iter.hasNext();) {
                BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
                if (bookingfclUnits.getId().toString().equals(index)) {
                    fclRates.remove(bookingfclUnits);
                    break;
                }
            }
        }
    }

    public void deletePBASURCHARGE(BookingfclUnits bookingfclUnits, List otherChargeCList) throws Exception {
        if (bookingfclUnits.getChargeCodeDesc() != null && bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)) {
            Integer unitTypeid = (null != bookingfclUnits.getUnitType()) ? bookingfclUnits.getUnitType().getId() : null;
            List list = bookingFclUnitsDAO.checkChargeCode(bookingfclUnits.getBookingNumber(), FclBlConstants.ADVANCESHIPPERCODE, unitTypeid, null);
            if (CommonFunctions.isNotNullOrNotEmpty(list)) {
                calculatePBACharge(new Integer(bookingfclUnits.getBookingNumber()));
            } else {
                list = bookingFclUnitsDAO.checkChargeCode(bookingfclUnits.getBookingNumber(), FclBlConstants.ADVANCESURCHARGECODE, unitTypeid, null);
                if (CommonFunctions.isNotNullOrNotEmpty(list)) {
                    BookingfclUnits bookingfclUnitsNew = (BookingfclUnits) list.get(0);
                    deleteFromChargeList(otherChargeCList, bookingfclUnitsNew.getId().toString());
                    bookingFclUnitsDAO.delete(bookingfclUnitsNew);

                }
            }
        }
        if (bookingfclUnits.getChargeCodeDesc() != null && bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE)) {
            Integer unitTypeid = (null != bookingfclUnits.getUnitType()) ? bookingfclUnits.getUnitType().getId() : null;
            List list = bookingFclUnitsDAO.checkChargeCode(bookingfclUnits.getBookingNumber(), FclBlConstants.ADVANCEFFCODE, unitTypeid, null);
            if (CommonFunctions.isNotNullOrNotEmpty(list)) {
                calculatePBACharge(new Integer(bookingfclUnits.getBookingNumber()));
            } else {
                list = bookingFclUnitsDAO.checkChargeCode(bookingfclUnits.getBookingNumber(), FclBlConstants.ADVANCESURCHARGECODE, unitTypeid, null);
                if (CommonFunctions.isNotNullOrNotEmpty(list)) {
                    BookingfclUnits bookingfclUnitsNew = (BookingfclUnits) list.get(0);
                    deleteFromChargeList(otherChargeCList, bookingfclUnitsNew.getId().toString());// deleting from list
                    bookingFclUnitsDAO.delete(bookingfclUnitsNew);
                }
            }

        }
    }

    public void addHazmatRates(BookingFcl bookingFcl, HttpSession session) throws Exception {
        String originTerminal = "";
        String commodity = "";
        String destination = "";
        String ssline = "";
        String sscode = "";
        String unitType = bookingFcl.getSelectedUnits();
        User user = new User();
        String userName = null;
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userName = user.getLoginName();
        }
        if (bookingFcl.getOriginTerminal() != null) {
            originTerminal = stringFormatter.findForManagement(bookingFcl.getOriginTerminal(), null);
        }
        if (bookingFcl.getPortofDischarge() != null) {
            destination = stringFormatter.findForManagement(bookingFcl.getPortofDischarge(), null);
        }
        if (bookingFcl.getSslname() != null) {
            int i = bookingFcl.getSslname().indexOf("//");
            String code = "";
            String desc = "";
            if (i != -1) {
                String destinationPort[] = bookingFcl.getSslname().split("//");
                desc = destinationPort[0];
                code = destinationPort[1];
            }
            List list = customerDAO.findForAgenttNo1(code, desc);
            if (list != null && list.size() > 0) {
                TradingPartnerTemp customerTemp = (TradingPartnerTemp) list.get(0);
                ssline = customerTemp.getAccountName();
                sscode = customerTemp.getAccountno();
            }
        }
        // delete hazmat rates from `bookingfcl_units` table, before adding
        bookingFclUnitsDAO.deleteCharges(bookingFcl.getBookingNumber(), BookingConstants.HAZARDOUS_CODE_DESC);
        // Add hazmat rates to `bookingfcl_units` table
        GenericCode genericCode = quotationBC.findForGenericCode(bookingFcl.getComcode());
        commodity = genericCode.getId().toString();
        List<FclBuyCostTypeRates> ratesList = fclBuyDAO.getHazmatRates(originTerminal, destination, commodity, sscode);
        GenericCode hazmatGenericCodes = genericCodeDAO.findById(BookingConstants.HAZARDOUS_GENERICCODE_ID);
        GenericCode perContainerGenericCode = genericCodeDAO.findById(BookingConstants.PER_CONTAINER_SIZE_GENERICCODE_ID);
        BookingfclUnitsDAO bookingFclUnitDAO = new BookingfclUnitsDAO();
        if (CommonUtils.isEqualIgnoreCase(bookingFcl.getRatesNonRates(), "N") && CommonUtils.isEmpty(unitType)) {
            for (FclBuyCostTypeRates fclBuyCostTypeRates : ratesList) {
                BookingfclUnits bookingfclUnits = new BookingfclUnits();
                bookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
                bookingfclUnits.setRates(0d);
                bookingfclUnits.setMinimum(0d);
                bookingfclUnits.setCtc(0d);
                bookingfclUnits.setQouteId(0);
                bookingfclUnits.setChgCode(hazmatGenericCodes.getCodedesc());
                bookingfclUnits.setUnitName(null);
                bookingfclUnits.setNumbers(null);
                bookingfclUnits.setChargeCode(hazmatGenericCodes);
                bookingfclUnits.setChargeCodeDesc(hazmatGenericCodes.getCode());
                bookingfclUnits.setRetail(0d);
                bookingfclUnits.setPrint("off");
                bookingfclUnits.setFtf(0d);
                bookingfclUnits.setAmount(fclBuyCostTypeRates.getActiveAmt());
                bookingfclUnits.setMarkUp(fclBuyCostTypeRates.getMarkup());
                bookingfclUnits.setCommcode(genericCode.getId());
                bookingfclUnits.setCurrency(fclBuyCostTypeRates.getCurrency().getCodedesc());
                bookingfclUnits.setCurrency1(fclBuyCostTypeRates.getCurrency());
                bookingfclUnits.setEfectiveDate(fclBuyCostTypeRates.getEffectiveDate());
                bookingfclUnits.setUnitType(null);
                bookingfclUnits.setCostType(perContainerGenericCode.getCodedesc());
                bookingfclUnits.setProfit(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                bookingfclUnits.setBuyRate(0d);
                bookingfclUnits.setFutureRate(0d);
                bookingfclUnits.setAdjustment(0d);
                bookingfclUnits.setRateChangeAmount(0d);
                bookingfclUnits.setRateChangeMarkup(0d);
                bookingfclUnits.setUpdateBy(userName);
                bookingfclUnits.setUpdateOn(new Date());
                bookingfclUnits.setOutOfGauge("N");
                bookingfclUnits.setSpecialEquipmentUnit("");
                bookingfclUnits.setStandardCharge("Y");
                bookingfclUnits.setAccountNo(bookingFcl.getSSLine());
                String accountName = StringUtils.substringBefore(bookingFcl.getSslname(), "/");
                bookingfclUnits.setAccountName(accountName);
                bookingFclUnitDAO.save(bookingfclUnits);
                break;
            }
        } else {
            for (FclBuyCostTypeRates fclBuyCostTypeRates : ratesList) {
                BookingfclUnits bookingfclUnits = new BookingfclUnits();
                if (unitType.contains(fclBuyCostTypeRates.getUnitType().getCodedesc())) {
                    bookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
                    bookingfclUnits.setRates(0d);
                    bookingfclUnits.setMinimum(0d);
                    bookingfclUnits.setCtc(0d);
                    bookingfclUnits.setQouteId(0);
                    bookingfclUnits.setChgCode(hazmatGenericCodes.getCodedesc());
                    bookingfclUnits.setUnitName(fclBuyCostTypeRates.getUnitType().getCodedesc());
                    bookingfclUnits.setNumbers("1");
                    bookingfclUnits.setChargeCode(hazmatGenericCodes);
                    bookingfclUnits.setChargeCodeDesc(hazmatGenericCodes.getCode());
                    bookingfclUnits.setRetail(0d);
                    bookingfclUnits.setPrint("off");
                    bookingfclUnits.setFtf(0d);
                    bookingfclUnits.setAmount(fclBuyCostTypeRates.getActiveAmt());
                    bookingfclUnits.setMarkUp(fclBuyCostTypeRates.getMarkup());
                    bookingfclUnits.setCommcode(genericCode.getId());
                    bookingfclUnits.setCurrency(fclBuyCostTypeRates.getCurrency().getCodedesc());
                    bookingfclUnits.setCurrency1(fclBuyCostTypeRates.getCurrency());
                    bookingfclUnits.setEfectiveDate(fclBuyCostTypeRates.getEffectiveDate());
                    bookingfclUnits.setUnitType(fclBuyCostTypeRates.getUnitType());
                    bookingfclUnits.setCostType(perContainerGenericCode.getCodedesc());
                    bookingfclUnits.setProfit(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                    bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                    bookingfclUnits.setBuyRate(0d);
                    bookingfclUnits.setFutureRate(0d);
                    bookingfclUnits.setAdjustment(0d);
                    bookingfclUnits.setRateChangeAmount(0d);
                    bookingfclUnits.setRateChangeMarkup(0d);
                    bookingfclUnits.setUpdateBy(userName);
                    bookingfclUnits.setUpdateOn(new Date());
                    bookingfclUnits.setOutOfGauge("N");
                    bookingfclUnits.setSpecialEquipmentUnit("");
                    bookingfclUnits.setStandardCharge("Y");
                    bookingfclUnits.setAccountNo(bookingFcl.getSSLine());
                    String accountName = StringUtils.substringBefore(bookingFcl.getSslname(), "/");
                    bookingfclUnits.setAccountName(accountName);
                    bookingFclUnitDAO.save(bookingfclUnits);
                }
            }
        }
    }

    public void deleteHazmatRates(BookingFcl bookingFcl, HttpSession session) throws Exception {
        bookingFclUnitsDAO.deleteCharges(bookingFcl.getBookingNumber(), BookingConstants.HAZARDOUS_CODE_DESC);
    }

    public String fclAutoCostCalculation(BookingFcl bookingFcl, FclBl fclBl, List newaddList, List newaddList1) throws Exception {
        String originTerminal = "";
        String pol = "";
        String pod = "";
        String rampCity = "";
        String destination = "";
        String commodity = "";
        String ssline = "";
        String sscode = "";
        Double amt012 = 0.00;
        Double amt029 = 0.00;
        Double amt001 = 0.00;
        Double amt022 = 0.00;
        Double amt009 = 0.00;
        Double amt005 = 0.00;
        Double amt007 = 0.00;
        Double amt = 0.00;
        Double comamt1 = 0.00;
        Double comamt2 = 0.00;
        Double amt1 = 0.00;
        Double totamt = 0.00;
        Double profit = 0.00;
        String portDest = "";
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        if (bookingFcl.getOriginTerminal() != null) {
            originTerminal = stringFormatter.findForManagement(bookingFcl.getOriginTerminal(), null);
        }
        if (bookingFcl.getRampCity() != null) {
            rampCity = stringFormatter.findForManagement(bookingFcl.getRampCity(), null);
        }
        if (bookingFcl.getPortofOrgin() != null) {
            pol = stringFormatter.findForManagement(bookingFcl.getPortofOrgin(), null);
        }
        if (bookingFcl.getPortofDischarge() != null) {
            destination = stringFormatter.findForManagement(bookingFcl.getPortofDischarge(), null);
        }
        if (bookingFcl.getDestination() != null) {
            pod = stringFormatter.findForManagement(bookingFcl.getDestination(), null);
        }

        if (bookingFcl.getComcode() != null) {
            GenericCode genericCode = quotationBC.findForGenericCode(bookingFcl.getComcode());
            commodity = genericCode.getId().toString();
        }
        if (bookingFcl.getSslname() != null) {
            int i = bookingFcl.getSslname().indexOf("//");
            String code = "";
            String desc = "";
            if (i != -1) {
                String destinationPort[] = bookingFcl.getSslname().split("//");
                desc = destinationPort[0];
                code = destinationPort[1];
            }
            List list = customerDAO.findForAgenttNo1(code, desc);
            if (list != null && list.size() > 0) {
                TradingPartnerTemp customerTemp = (TradingPartnerTemp) list.get(0);
                ssline = customerTemp.getAccountName();
                sscode = customerTemp.getAccountno();
            }
        }
        List ratesList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(originTerminal, destination, commodity, sscode);
        if (ratesList.isEmpty()) {
            ratesList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(rampCity, destination, commodity, sscode);
            if (ratesList.isEmpty()) {
                ratesList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(pol, destination, commodity, sscode);
                if (ratesList.isEmpty()) {
                    ratesList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(originTerminal, pod, commodity, sscode);
                    portDest = pod;
                    if (ratesList.isEmpty()) {
                        ratesList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(rampCity, pod, commodity, sscode);
                        portDest = pod;
                        if (ratesList.isEmpty()) {
                            ratesList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(pol, pod, commodity, sscode);
                            portDest = pod;
                        }
                    }
                }
            }
        }
        if (ratesList.size() > 0) {
            for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                FclBuy fclBuy = (FclBuy) iterator.next();
                if (fclBuy.getEndDate() != null && fclBl.getSailDate() != null && fclBl.getSailDate().before(fclBuy.getEndDate())) {
                    if (fclBuy.getFclBuyCostsSet() != null) {
                        Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                        while (iter.hasNext()) {
                            FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                            if (fclBuyCost.getContType() != null && fclBuyCost.getContType().getCode().equals("A")) {
                                if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                                    Iterator iter1 = fclBuyCost.getFclBuyUnitTypesSet().iterator();
                                    if (iter1.hasNext()) {
                                        FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iter1.next();
                                        if (fclBuyCostTypeRates.getUnitType() != null
                                                && (fclBuyCostTypeRates.getUnitType().getCode().equals("A") || fclBuyCostTypeRates.getUnitType().getCode().equals("B")
                                                || fclBuyCostTypeRates.getUnitType().getCode().equals("C") || fclBuyCostTypeRates.getUnitType().getCode().equals("D")
                                                || fclBuyCostTypeRates.getUnitType().getCode().equals("E") || fclBuyCostTypeRates.getUnitType().getCode().equals("F"))) {
                                            if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equals("012")) {
                                                amt012 += fclBuyCostTypeRates.getActiveAmt();
                                            }
                                            if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equals("029")
                                                    && bookingFcl.getHazmat() != null && bookingFcl.getHazmat().equals("Y")) {
                                                amt029 += fclBuyCostTypeRates.getActiveAmt();
                                            }
                                            if (fclBuyCost.getCostId() != null && !fclBuyCost.getCostId().getCode().equals("022")
                                                    && !fclBuyCost.getCostId().getCode().equals("029") && !fclBuyCost.getCostId().getCode().equals("030")) {
                                                amt001 += fclBuyCostTypeRates.getActiveAmt();
                                            }
                                            if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equals("022")) {
                                                amt022 += fclBuyCostTypeRates.getActiveAmt();
                                            }
                                            if (fclBuyCost.getCostId() != null && (fclBuyCost.getCostId().getCode().equals("005")
                                                    || fclBuyCost.getCostId().getCode().equals("007") || fclBuyCost.getCostId().getCode().equals("009"))) {
                                                if (fclBl.getBillingTerminal() != null) {
                                                    int i = fclBl.getBillingTerminal().indexOf("-");
                                                    if (i != -1) {
                                                        String issueTerm[] = fclBl.getBillingTerminal().split("-");
                                                        String issue = issueTerm[1];
                                                        if (issue.equals("18")) {
                                                            amt009 += 30;
                                                        }
                                                    }
                                                }
                                                if (fclBuyCostTypeRates.getUnitType() != null && fclBuyCostTypeRates.getUnitType().getCode().equals("A")
                                                        && bookingFcl.getDeductFFcomm() != null && bookingFcl.getDeductFFcomm().equals("N")
                                                        && fclBl.getForwardingAgentName() != null && !fclBl.getForwardingAgentName().equals("")) {
                                                    amt005 += 75;
                                                }
                                                if (fclBuyCostTypeRates.getUnitType() != null && fclBuyCostTypeRates.getUnitType().getCode().equals("A")) {
                                                    if (fclBl.getBillingTerminal() != null) {
                                                        int i = fclBl.getBillingTerminal().indexOf("-");
                                                        if (i != -1) {
                                                            String issueTerm[] = fclBl.getBillingTerminal().split("-");
                                                            String issue = issueTerm[1];
                                                            if (!issue.equals("08") && !issue.equals("09") && !issue.equals("17")
                                                                    && !issue.equals("15") && !issue.equals("19") && !issue.equals("38")
                                                                    && !issue.equals("59") && !issue.equals("61") && !issue.equals("63")
                                                                    && !issue.equals("73") && !issue.equals("18")) {
                                                                amt009 += 50;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (fclBuyCostTypeRates.getUnitType() != null && !fclBuyCostTypeRates.getUnitType().getCode().equals("A")
                                                        && bookingFcl.getDeductFFcomm() != null && bookingFcl.getDeductFFcomm().equals("N")
                                                        && fclBl.getForwardingAgentName() != null && !fclBl.getForwardingAgentName().equals("")) {
                                                    amt005 += 100;
                                                }
                                                if (fclBuyCostTypeRates.getUnitType() != null && !fclBuyCostTypeRates.getUnitType().getCode().equals("A")) {
                                                    if (fclBl.getBillingTerminal() != null) {
                                                        int i = fclBl.getBillingTerminal().indexOf("-");
                                                        if (i != -1) {
                                                            String issueTerm[] = fclBl.getBillingTerminal().split("-");
                                                            String issue = issueTerm[1];
                                                            if (!issue.equals("08") && !issue.equals("09") && !issue.equals("17")
                                                                    && !issue.equals("15") && !issue.equals("19") && !issue.equals("38")
                                                                    && !issue.equals("59") && !issue.equals("61") && !issue.equals("63")
                                                                    && !issue.equals("73") && !issue.equals("18")) {
                                                                amt009 += 100;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equals("007")) {
                                                if (portDest != null) {
                                                    UnLocation unLocation = unLocationDAO.findById(Integer.parseInt(portDest));
                                                    List portsList = portsDAO.findForUnlocCodeAndPortName(unLocation.getUnLocationCode(), unLocation.getUnLocationName());
                                                    if (portsList.size() > 0) {
                                                        Ports ports = (Ports) portsList.get(0);
                                                        if (ports.getFclPortConfigSet() != null) {
                                                            Iterator iter2 = ports.getFclPortConfigSet().iterator();
                                                            while (iter2.hasNext()) {
                                                                FCLPortConfiguration fclPortConfiguration = (FCLPortConfiguration) iter2.next();
                                                                if (fclPortConfiguration.getRcomRule() != null && fclPortConfiguration.getRcomRule().getCode().equals("3")) {
                                                                    if (fclBl.getRoutedByAgent() != null && !fclBl.getRoutedByAgent().equals("")) {
                                                                        if (fclPortConfiguration.getRadmAm() != null) {
                                                                            amt += fclPortConfiguration.getRadmAm();
                                                                        }
                                                                        if (fclPortConfiguration.getRcomAm() != null) {
                                                                            comamt1 += fclPortConfiguration.getRcomAm();
                                                                        }
                                                                        if (fclPortConfiguration.getRcomTierAmt() != null) {
                                                                            comamt2 += fclPortConfiguration.getRcomTierAmt();
                                                                        }
                                                                    } else {
                                                                        if (fclPortConfiguration.getNadmAm() != null) {
                                                                            amt += fclPortConfiguration.getNadmAm();
                                                                        }
                                                                        if (fclPortConfiguration.getNadmTierAmt() != null) {
                                                                            amt1 += fclPortConfiguration.getNadmTierAmt();
                                                                        }
                                                                        if (fclPortConfiguration.getNcomAm() != null) {
                                                                            comamt1 += fclPortConfiguration.getNcomAm();
                                                                        }
                                                                        if (fclPortConfiguration.getNcomTierAmt() != null) {
                                                                            comamt2 += fclPortConfiguration.getNcomTierAmt();
                                                                        }
                                                                    }
                                                                    if (fclPortConfiguration.getNcomRule() != null && fclPortConfiguration.getNcomRule().getCode().equals("2")) {
                                                                        amt007 += comamt1;
                                                                    }
                                                                    if (fclPortConfiguration.getNcomRule() != null && fclPortConfiguration.getNcomRule().getCode().equals("1")) {
                                                                        comamt2 += comamt1;
                                                                    }
                                                                    if (fclPortConfiguration.getNadmRule() != null && fclPortConfiguration.getNadmRule().getCode().equals("1")
                                                                            && fclPortConfiguration.getNcomRule() != null && fclPortConfiguration.getNcomRule().getCode().equals("3")) {
                                                                        amt1 += amt;
                                                                    }
                                                                    if (fclPortConfiguration.getNcomRule() != null && fclPortConfiguration.getNcomRule().getCode().equals("3")
                                                                            && fclPortConfiguration.getNadmRule() != null && fclPortConfiguration.getNadmRule().getCode().equals("2")) {
                                                                        totamt += amt;
                                                                    }
                                                                    if (fclPortConfiguration.getNcomRule() != null && (fclPortConfiguration.getNcomRule().getCode().equals("1")
                                                                            || (fclPortConfiguration.getNcomRule().getCode().equals("4")))) {
                                                                        amt007 += comamt1;
                                                                        if (!flag) {
                                                                            flag = true;
                                                                            amt007 += comamt2;
                                                                            totamt += amt;
                                                                        }
                                                                        totamt += amt1;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (!flag1) {
                                                for (Iterator iterator2 = newaddList.iterator(); iterator2.hasNext();) {
                                                    flag1 = true;
                                                    FclBlCharges fclBlCharges = (FclBlCharges) iterator2.next();
                                                    if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().equals("139")) {
                                                        profit += fclBlCharges.getAmount();
                                                    }
                                                    if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().equals("149")) {
                                                        profit += fclBlCharges.getAmount();
                                                    }
                                                    if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().equals("159")) {
                                                        profit += fclBlCharges.getAmount();
                                                    }
                                                    if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().equals("250")) {
                                                        profit += fclBlCharges.getAmount();
                                                    }
                                                }
                                            }
                                            if (!flag2) {
                                                for (Iterator iterator2 = newaddList1.iterator(); iterator2.hasNext();) {
                                                    flag2 = true;
                                                    FclBlCostCodes fclBLCostCodes = (FclBlCostCodes) iterator2.next();
                                                    if (fclBLCostCodes.getCostCode() != null && fclBLCostCodes.getCostCode().equals("001")) {
                                                        profit -= fclBLCostCodes.getAmount();
                                                    }
                                                    if (fclBLCostCodes.getCostCode() != null && fclBLCostCodes.getCostCode().equals(FclBlConstants.FFCODE)) {
                                                        profit -= fclBLCostCodes.getAmount();
                                                    }
                                                    if (fclBLCostCodes.getCostCode() != null && fclBLCostCodes.getCostCode().equals("009")) {
                                                        profit -= fclBLCostCodes.getAmount();
                                                    }
                                                    if (fclBLCostCodes.getCostCode() != null && fclBLCostCodes.getCostCode().equals("012")) {
                                                        profit -= fclBLCostCodes.getAmount();
                                                    }
                                                    if (fclBLCostCodes.getCostCode() != null && fclBLCostCodes.getCostCode().equals("022")) {
                                                        profit -= fclBLCostCodes.getAmount();
                                                    }
                                                }
                                            }
                                            if (fclBuyCost.getCostId() != null && !fclBuyCost.getCostId().getCode().equals("001")) {
                                                profit -= amt001;
                                            }
                                            if (fclBuyCost.getCostId() != null && !fclBuyCost.getCostId().getCode().equals("005")) {
                                                profit -= amt005;
                                            }
                                            if (fclBuyCost.getCostId() != null && !fclBuyCost.getCostId().getCode().equals("009")) {
                                                profit -= amt009;
                                            }
                                            if (fclBuyCost.getCostId() != null && !fclBuyCost.getCostId().getCode().equals("012")) {
                                                profit -= amt012;
                                            }
                                            if (fclBuyCost.getCostId() != null && !fclBuyCost.getCostId().getCode().equals("029")) {
                                                profit -= amt029;
                                            }
                                            profit -= totamt;
                                            amt007 = profit * comamt1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        boolean autoFlag = false;
        String msg = "";
        for (Iterator iterator = newaddList1.iterator(); iterator.hasNext();) {

            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (fclBlCostCodes.getCostCode() != null && fclBlCostCodes.getCostCode().equals("001")) {
                if (!amt001.equals(0.0) && fclBlCostCodes.getAmount() != amt001) {
                    if (!autoFlag) {
                        autoFlag = true;
                        msg += "Fcl Auto Cost Calculation=";
                    }
                    msg += "Ocean Freight Amount Changed from " + fclBlCostCodes.getAmount() + " to " + amt001 + " = ";
                }
            }
            if (fclBlCostCodes.getCostCode() != null && fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODE)) {
                if (!amt005.equals(0.0) && fclBlCostCodes.getAmount() != amt005) {
                    if (!autoFlag) {
                        autoFlag = true;
                        msg += "Fcl Auto Cost Calculation=";
                    }
                    msg += "FF Commission Amount Changed from " + fclBlCostCodes.getAmount() + " to " + amt005 + " = ";
                }
            }
            if (fclBlCostCodes.getCostCode() != null && fclBlCostCodes.getCostCode().equals("009")) {
                if (!amt009.equals(0.0) && fclBlCostCodes.getAmount() != amt009) {
                    if (!autoFlag) {
                        autoFlag = true;
                        msg += "Fcl Auto Cost Calculation=";
                    }
                    msg += "FCL COM Terminal Amount Changed from " + fclBlCostCodes.getAmount() + " to " + amt009 + " = ";
                }
            }
            if (fclBlCostCodes.getCostCode() != null && fclBlCostCodes.getCostCode().equals("012")) {
                if (!amt012.equals(0.0) && fclBlCostCodes.getAmount() != amt012) {
                    if (!autoFlag) {
                        autoFlag = true;
                        msg += "Fcl Auto Cost Calculation=";
                    }
                    msg += "DEFERRAL Amount Changed from " + fclBlCostCodes.getAmount() + " to " + amt012 + " = ";
                }
            }
            if (fclBlCostCodes.getCostCode() != null && fclBlCostCodes.getCostCode().equals("029")) {
                if (!amt029.equals(0.0) && fclBlCostCodes.getAmount() != amt029) {
                    if (!autoFlag) {
                        autoFlag = true;
                        msg += "Fcl Auto Cost Calculation=";
                    }
                    msg += "Hazardous Changed from " + fclBlCostCodes.getAmount() + " to " + amt029 + " = ";
                }
            }
            if (fclBlCostCodes.getCostCode() != null && fclBlCostCodes.getCostCode().equals("022")) {
                if (!amt022.equals(0.0) && fclBlCostCodes.getAmount() != amt022) {
                    if (!autoFlag) {
                        autoFlag = true;
                        msg += "Fcl Auto Cost Calculation=";
                    }
                    msg += "INTERMODAL RAMP Changed from " + fclBlCostCodes.getAmount() + " to " + amt022 + " = ";
                }
            }
            if (fclBlCostCodes.getCostCode() != null && fclBlCostCodes.getCostCode().equals("007")) {
                if (!amt007.equals(0.0) && fclBlCostCodes.getAmount() != amt007) {
                    if (!autoFlag) {
                        autoFlag = true;
                        msg += "Fcl Auto Cost Calculation=";
                    }
                    msg += "FCL FAE-COMMISSION Changed from " + fclBlCostCodes.getAmount() + " to " + amt007 + " = ";
                }
            }
        }
        return msg;
    }

    public List consolidateRates(List fclRates, MessageResources messageResources, boolean importFlag) throws Exception {
        List consolidatorList = new ArrayList();
        Map hashMap = new HashMap();
        Map newHashMap = new HashMap();
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
            BookingfclUnits newBookingfclUnits = new BookingfclUnits();
            PropertyUtils.copyProperties(newBookingfclUnits, bookingfclUnits);
            if (null == newBookingfclUnits.getSpecialEquipmentUnit()) {
                newBookingfclUnits.setSpecialEquipmentUnit("");
            }
            if (null == newBookingfclUnits.getStandardCharge()) {
                newBookingfclUnits.setStandardCharge("");
            }
            if (null != newBookingfclUnits.getSpotRateAmt()) {
                newBookingfclUnits.setAmount(newBookingfclUnits.getSpotRateAmt());
            }
            if (newBookingfclUnits.getUnitType() != null) {
                newHashMap.put(newBookingfclUnits.getChargeCodeDesc() + "-" + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge(), newBookingfclUnits);
            } else {
                newHashMap.put(newBookingfclUnits.getChargeCodeDesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge(), newBookingfclUnits);
            }
        }
        String interModelRate = "";
        String interModelRate1 = "";
        boolean flag1 = false;
        String consolidator = "";

        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
            BookingfclUnits newBookingfclUnits = new BookingfclUnits();
            PropertyUtils.copyProperties(newBookingfclUnits, bookingfclUnits);
            if (null == newBookingfclUnits.getSpecialEquipmentUnit()) {
                newBookingfclUnits.setSpecialEquipmentUnit("");
            }
            if (null == newBookingfclUnits.getStandardCharge()) {
                newBookingfclUnits.setStandardCharge("");
            }
            if (importFlag == false) {
                consolidator = messageResources.getMessage("OceanFreight");
            } else {
                consolidator = messageResources.getMessage("OceanFreightImports");
            }
            String colsolidatorRates[] = new String[5];
            if (consolidator.contains(",")) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = messageResources.getMessage("Intermodel");
            String interModelRates[] = new String[10];
            if (interModel.contains(",")) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            for (String colsolidatorRate : colsolidatorRates) {
                if (newBookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(colsolidatorRate)) {
                    if (newBookingfclUnits.getUnitType() != null && newBookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge")) && newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                            + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge()) != null) {
                        BookingfclUnits tempBookingfclUnits = (BookingfclUnits) newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                                + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge());
                        hashMap.put(messageResources.getMessage("oceanfreightcharge") + "-" + tempBookingfclUnits.getUnitType().getCodedesc() + "-" + tempBookingfclUnits.getSpecialEquipmentUnit() + "-" + tempBookingfclUnits.getStandardCharge(), tempBookingfclUnits);
                        flag = true;
                    } else if (newBookingfclUnits.getUnitType() != null && newBookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge")) && newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                            + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge()) != null) {
                        BookingfclUnits tempBookingfclUnits = (BookingfclUnits) newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                                + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge());
                        hashMap.put(messageResources.getMessage("oceanfreightImpcharge") + "-" + tempBookingfclUnits.getUnitType().getCodedesc() + "-" + tempBookingfclUnits.getSpecialEquipmentUnit() + "-" + tempBookingfclUnits.getStandardCharge(), tempBookingfclUnits);
                        flag = true;
                    } else {
                        interModelFlag = false;
                        for (String interModelRate2 : interModelRates) {
                            if (newBookingfclUnits.getManualCharges() == null) {
                                newBookingfclUnits.setManualCharges("");
                            }
                            if (newBookingfclUnits.getManualCharges() != null && !newBookingfclUnits.getManualCharges().equals("M") && newBookingfclUnits.getChargeCodeDesc().equalsIgnoreCase(interModelRate2)) {
                                interModelFlag = true;
                                break;
                            }
                        }
                        if (interModelFlag && newBookingfclUnits.getUnitType() != null) {
                            interModelRate1 = interModelRate + "-" + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge();
                            if (hashMap.containsKey(interModelRate1)) {
                                BookingfclUnits tempBookingfclUnits = (BookingfclUnits) newHashMap.get(interModelRate1);
                                if (null == tempBookingfclUnits.getSpecialEquipmentUnit()) {
                                    tempBookingfclUnits.setSpecialEquipmentUnit("");
                                }
                                if (null == tempBookingfclUnits.getStandardCharge()) {
                                    tempBookingfclUnits.setStandardCharge("");
                                }
                                tempBookingfclUnits.setAmount(tempBookingfclUnits.getAmount() + (null != newBookingfclUnits.getSpotRateAmt() ? newBookingfclUnits.getSpotRateAmt() : newBookingfclUnits.getAmount()) + (null != newBookingfclUnits.getSpotRateMarkUp() ? newBookingfclUnits.getSpotRateMarkUp() : newBookingfclUnits.getMarkUp()));
                                if (null != tempBookingfclUnits.getSpotRateAmt() && null != newBookingfclUnits.getSpotRateAmt()) {
                                    tempBookingfclUnits.setSpotRateAmt(tempBookingfclUnits.getSpotRateAmt() + newBookingfclUnits.getSpotRateAmt());
                                } else if (null != newBookingfclUnits.getSpotRateAmt()) {
                                    tempBookingfclUnits.setSpotRateAmt(newBookingfclUnits.getSpotRateAmt());
                                }
                                tempBookingfclUnits.setAdjustment(tempBookingfclUnits.getAdjustment() + newBookingfclUnits.getAdjustment());
                                hashMap.put(tempBookingfclUnits.getChargeCodeDesc() + "-" + tempBookingfclUnits.getUnitType().getCodedesc() + "-" + tempBookingfclUnits.getSpecialEquipmentUnit() + "-" + tempBookingfclUnits.getStandardCharge(), tempBookingfclUnits);
                            } else {
                                if (null != newBookingfclUnits.getSpotRateAmt()) {
                                    newBookingfclUnits.setAmount(newBookingfclUnits.getSpotRateAmt());
                                }
                                hashMap.put(newBookingfclUnits.getChargeCodeDesc() + "-" + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge(), newBookingfclUnits);
                                interModelRate = newBookingfclUnits.getChargeCodeDesc();
                            }
                        } else if (interModelFlag && newBookingfclUnits.getUnitType() == null) {
                            interModelRate1 = interModelRate + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge();
                            if (hashMap.containsKey(interModelRate1)) {
                                BookingfclUnits tempBookingfclUnits = (BookingfclUnits) newHashMap.get(interModelRate1);
                                tempBookingfclUnits.setAmount(tempBookingfclUnits.getAmount() + (null != newBookingfclUnits.getSpotRateAmt() ? newBookingfclUnits.getSpotRateAmt() : newBookingfclUnits.getAmount()) + (null != newBookingfclUnits.getSpotRateMarkUp() ? newBookingfclUnits.getSpotRateMarkUp() : newBookingfclUnits.getMarkUp()));
                                if (null != tempBookingfclUnits.getSpotRateAmt() && null != newBookingfclUnits.getSpotRateAmt()) {
                                    tempBookingfclUnits.setSpotRateAmt(tempBookingfclUnits.getSpotRateAmt() + newBookingfclUnits.getSpotRateAmt());
                                } else if (null != newBookingfclUnits.getSpotRateAmt()) {
                                    tempBookingfclUnits.setSpotRateAmt(newBookingfclUnits.getSpotRateAmt());
                                }
                                tempBookingfclUnits.setAdjustment(tempBookingfclUnits.getAdjustment() + newBookingfclUnits.getAdjustment());
                                hashMap.put(tempBookingfclUnits.getChargeCodeDesc(), tempBookingfclUnits);
                            } else {
                                hashMap.put(newBookingfclUnits.getChargeCodeDesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge(), newBookingfclUnits);
                                interModelRate = newBookingfclUnits.getChargeCodeDesc();
                            }
                        }
                        if (!interModelFlag && newBookingfclUnits.getUnitType() != null) {
                            if (null != newBookingfclUnits.getSpotRateAmt()) {
                                newBookingfclUnits.setAmount(newBookingfclUnits.getSpotRateAmt());
                            }
                            if (null != newBookingfclUnits.getSpotRateMarkUp()) {
                                newBookingfclUnits.setSpotRateMarkUp(newBookingfclUnits.getSpotRateMarkUp());
                            }
                            hashMap.put(newBookingfclUnits.getChargeCodeDesc() + "-" + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge(), newBookingfclUnits);
                            flag = true;
                        }
                    }
                    break;
                }
            }
            if (!flag && !interModelFlag) {
                if (importFlag == false) {
                    if (newBookingfclUnits.getUnitType() != null && (newBookingfclUnits.getChargeCodeDesc().equals("DOCUM") || newBookingfclUnits.getChargeCodeDesc().equals("INSURE") || newBookingfclUnits.getChargeCodeDesc().equals(FclBlConstants.FFCODE) || newBookingfclUnits.getChargeCodeDesc().equals("CHASFEE") || (newBookingfclUnits.getManualCharges() != null && newBookingfclUnits.getManualCharges().equals("M")) || newBookingfclUnits.getChargeCodeDesc().equals("PIERPA"))) {
                        hashMap.put(newBookingfclUnits.getChargeCodeDesc() + "-" + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge(), newBookingfclUnits);
                    } else if (newBookingfclUnits.getUnitType() != null) {
                        if (newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                                + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge()) != null) {
                            BookingfclUnits tempBookingfclUnits = (BookingfclUnits) newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                                    + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge());
                            tempBookingfclUnits.setAmount(tempBookingfclUnits.getAmount() + (null != newBookingfclUnits.getSpotRateAmt() ? newBookingfclUnits.getSpotRateAmt() : newBookingfclUnits.getAmount()) + (null != newBookingfclUnits.getSpotRateMarkUp() ? newBookingfclUnits.getSpotRateMarkUp() : newBookingfclUnits.getMarkUp()));
                            if (null != tempBookingfclUnits.getSpotRateAmt() && null != newBookingfclUnits.getSpotRateAmt()) {
                                tempBookingfclUnits.setSpotRateAmt(tempBookingfclUnits.getSpotRateAmt() + newBookingfclUnits.getSpotRateAmt());
                            } else if (null != newBookingfclUnits.getSpotRateAmt()) {
                                tempBookingfclUnits.setSpotRateAmt(newBookingfclUnits.getSpotRateAmt());
                            }
                            tempBookingfclUnits.setAdjustment(tempBookingfclUnits.getAdjustment() + newBookingfclUnits.getAdjustment());
                            hashMap.put(messageResources.getMessage("oceanfreightcharge") + "-" + tempBookingfclUnits.getUnitType().getCodedesc(), tempBookingfclUnits);
                        }
                    } else {
                        hashMap.put(newBookingfclUnits.getChargeCodeDesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge(), newBookingfclUnits);
                    }
                } else if (importFlag == true) {
                    if (newBookingfclUnits.getUnitType() != null && (newBookingfclUnits.getChargeCodeDesc().equals("INSURE") || newBookingfclUnits.getChargeCodeDesc().equals(FclBlConstants.FFCODE) || newBookingfclUnits.getManualCharges() != null && newBookingfclUnits.getManualCharges().equals("M"))) {
                        hashMap.put(newBookingfclUnits.getChargeCodeDesc() + "-" + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge(), newBookingfclUnits);
                    } else {
                        if (newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                                + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge()) != null) {
                            BookingfclUnits tempBookingfclUnits = (BookingfclUnits) newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                                    + newBookingfclUnits.getUnitType().getCodedesc() + "-" + newBookingfclUnits.getSpecialEquipmentUnit() + "-" + newBookingfclUnits.getStandardCharge());
                            tempBookingfclUnits.setAmount(tempBookingfclUnits.getAmount() + newBookingfclUnits.getAmount() + newBookingfclUnits.getMarkUp());
                            tempBookingfclUnits.setAdjustment(tempBookingfclUnits.getAdjustment() + newBookingfclUnits.getAdjustment());
                            hashMap.put(messageResources.getMessage("oceanfreightImpcharge") + "-" + tempBookingfclUnits.getUnitType().getCodedesc(), tempBookingfclUnits);
                        }
                    }
                }
            }

        }
        Set hashSet = hashMap.keySet();
        List tempList = new ArrayList();
        HashMap tempMap = new HashMap();
        for (Iterator iterator = hashSet.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            BookingfclUnits bookingfclUnits = (BookingfclUnits) hashMap.get(key);
            tempMap.put(bookingfclUnits.getId().toString(), bookingfclUnits);
            tempList.add(bookingfclUnits.getId().toString());
        }
        Collections.sort(tempList);
        for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
            String unitName = (String) iterator.next();
            BookingfclUnits bookingfclUnits = (BookingfclUnits) tempMap.get(unitName);
            consolidatorList.add(bookingfclUnits);
        }
        //---to make Ocean freight charge to appear on top of the list---------
        String temp = "";
        int j = 0;
        int k = 0;
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();

        for (Iterator iterator = consolidatorList.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
            if (bookingfclUnits.getUnitType() != null && !temp.equals(bookingfclUnits.getUnitType().getCode())) {
                k = j;
            }
            if (bookingfclUnits.getChargeCodeDesc().equals("OCNFRT") || bookingfclUnits.getChargeCodeDesc().equals("OFIMP")) {
                linkedList.add(k, bookingfclUnits);
            } else {
                linkedList.add(bookingfclUnits);
            }
            if (bookingfclUnits.getUnitType() != null) {
                temp = bookingfclUnits.getUnitType().getCode();
            }
            j++;
        }
        newList.addAll(linkedList);
        return orderExpandList(newList);
    }

    public SearchBookingReportDTO setRatesList(SearchBookingReportDTO searchBookingReportDTO,
            MessageResources messageResources, BookingFcl bookingFcl) throws Exception {
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        BookingfclUnitsDAO bookingFclUnitsDAO = new BookingfclUnitsDAO();
        List fclRates = bookingFclUnitsDAO.getbookingfcl(bookingFcl.getBookingNumber());
        List otherChargesList = new ArrayList();
        List otherList = bookingFclUnitsDAO.getbookingfcl1(bookingFcl.getBookingNumber());
        List bookingfclUnitsList1 = new ArrayList();
        List perkglbsList = new ArrayList();
        boolean importFlag = null != bookingFcl.getImportFlag() && bookingFcl.getImportFlag().equalsIgnoreCase("I");
        boolean flag1 = false;
        for (Object otherList1 : otherList) {
            BookingfclUnits c1 = (BookingfclUnits) otherList1;
            if (c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per1000kg")) || c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                perkglbsList.add(c1);
            } else if (c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("perbl"))) {
                otherChargesList.add(c1);
            }
        }
        if ("N".equalsIgnoreCase(bookingFcl.getRatesNonRates())) {
            searchBookingReportDTO.setChargesList(orderNonRatedList(fclRates));
        } else {
            List consolidaorList = consolidateRates(fclRates, messageResources, importFlag);
            searchBookingReportDTO.setChargesList(consolidaorList);
        }
        searchBookingReportDTO.setOtherChargesList(otherChargesList);
        return searchBookingReportDTO;
    }

    public List getRates(EditBookingsForm NewBookingsForm, BookingFcl bookingFcl,
            MessageResources messageResources, List bookingFclRatesList, List otherChargesList) throws Exception {
        String code = "";
        String desc = "";
        String sscode = "";
        String origin = bookingFcl.getOriginTerminal();
        String destination = bookingFcl.getPortofDischarge();
        String commodity = bookingFcl.getComcode();
        String ssline = NewBookingsForm.getSslDescription();
        List tempRatesList = new ArrayList();
        tempRatesList.addAll(bookingFclRatesList);
        if (ssline != null) {
            int i = ssline.indexOf("//");
            if (i != -1) {
                String destinationPort[] = ssline.split("//");
                desc = destinationPort[0];
                code = destinationPort[1];
            }
            List list = customerDAO.findForAgenttNo1(code, desc);
            if (list != null && list.size() > 0) {
                TradingPartnerTemp customerTemp = (TradingPartnerTemp) list.get(0);
                ssline = customerTemp.getAccountName();
                sscode = customerTemp.getAccountno();

            }
        }
        HashMap hashMap = new HashMap();
        HashMap<String, BookingfclUnits> unitHashMap = new HashMap<String, BookingfclUnits>();
        for (Iterator iter = bookingFclRatesList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getUnitType() != null && unitHashMap.get(bookingfclUnits.getUnitType().getCodedesc()) == null) {
                unitHashMap.put(bookingfclUnits.getUnitType().getCodedesc(), bookingfclUnits);
            }
            if (bookingfclUnits.getUnitType() != null) {
                hashMap.put(bookingfclUnits.getChgCode() + "-" + bookingfclUnits.getUnitType().getCodedesc(), bookingfclUnits);
            } else {
                hashMap.put(bookingfclUnits.getChgCode(), bookingfclUnits);
            }
        }
        for (Iterator iterator = otherChargesList.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
            hashMap.put(bookingfclUnits.getChgCode(), bookingfclUnits);
        }
        if (origin != null) {
            origin = stringFormatter.findForManagement(origin);
        }
        if (destination != null) {
            destination = stringFormatter.findForManagement(destination);
        }
        String commodityCode = null;
        if (commodity != null && !commodity.equals("") && !commodity.equals("%")) {
            List comList = genericCodeDAO.findForGenericCode(commodity);
            if (comList != null && comList.size() > 0) {
                GenericCode genObj = (GenericCode) comList.get(0);
                commodityCode = genObj.getCode();
                commodity = genObj.getId().toString();
            }
        }
        bookingFclDAO.update(bookingFcl);
        Date date1 = null;
        if (NewBookingsForm.getQuoteDate() != null) {
            date1 = sdf.parse(NewBookingsForm.getQuoteDate());
        } else {
            date1 = new Date();
        }
        bookingFcl.setQuoteDate(date1);
        Ports destPort = new PortsDAO().findById(Integer.parseInt(destination));
        String region = null != destPort && null != destPort.getRegioncode() ? destPort.getRegioncode().getField3() : "";
        List otherCommodityList = new FclBuyCostDAO().getOtherCommodity(commodityCode, null);
        String baseCommodity = commodity;
        String addsub = null;
        Double markup1 = null;
        String costCode = null;
        Double markup2 = null;
        if (CommonUtils.isNotEmpty(otherCommodityList)) {
            for (Object row : otherCommodityList) {
                Object[] cols = (Object[]) row;
                addsub = null != cols[0] ? cols[0].toString() : "";
                markup1 = null != cols[1] ? Double.parseDouble(cols[1].toString()) : 0;
                costCode = null != cols[2] ? genericCodeDAO.getFieldByCodeAndCodetypeId("Cost Code", cols[2].toString(), "codedesc") : "";
                baseCommodity = null != cols[3] ? cols[3].toString() : "";
                markup2 = null != cols[4] ? Double.parseDouble(cols[4].toString()) : 0;
                break;
            }
        }
        List ratesList = fclBuyDAO.findRates2(origin, destination, baseCommodity, code, sdf.format(bookingFcl.getQuoteDate()), sdf.format(bookingFcl.getBookingDate()));
        for (Object ratesList1 : ratesList) {
            FclBuy fclBuy = (FclBuy) ratesList1;
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                while (iter.hasNext()) {
                    FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                    if (bookingFcl.getHazmat() != null && bookingFcl.getHazmat().equals("N") && (fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurcharge"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                            || fclBuyCost.getCostId().getCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
                    } else {
                        if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                            boolean flag = false;
                            for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                if (fclBuyCostTypeRates.getEffectiveDate() != null
                                        && bookingFcl.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate())
                                        && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                    FclBuyCostTypeRates newFclBuyCostTypeRates = new FclBuyCostTypeRates();
                                    PropertyUtils.copyProperties(newFclBuyCostTypeRates, fclBuyCostTypeRates);
                                    if (CommonUtils.isEqualIgnoreCase(fclBuyCost.getCostId().getCodedesc(), costCode) && CommonUtils.isNotEmpty(addsub)) {
                                        if ("A".equalsIgnoreCase(addsub)) {
                                            if ("Y".equalsIgnoreCase(region)) {
                                                newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup2);
                                            } else {
                                                newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup1);
                                            }
                                        } else {
                                            if ("Y".equalsIgnoreCase(region)) {
                                                newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup2);
                                            } else {
                                                newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup1);
                                            }
                                        }
                                    }
                                    if (newFclBuyCostTypeRates.getUnitType() != null) {
                                        if (hashMap.containsKey(fclBuyCost.getCostId().getCodedesc() + "-" + newFclBuyCostTypeRates.getUnitType().getCodedesc())) {
                                            flag = true;
                                            BookingfclUnits bookingfclUnits = (BookingfclUnits) hashMap.get(fclBuyCost.getCostId().getCodedesc() + "-" + newFclBuyCostTypeRates.getUnitType().getCodedesc());
                                            if (newFclBuyCostTypeRates.getActiveAmt().compareTo(bookingfclUnits.getAmount()) != 0) {
                                                bookingfclUnits.setRateChangeAmount(newFclBuyCostTypeRates.getActiveAmt());
                                            }
                                            if (newFclBuyCostTypeRates.getMarkup().compareTo(bookingfclUnits.getMarkUp()) != 0) {
                                                bookingfclUnits.setRateChangeMarkup(newFclBuyCostTypeRates.getMarkup());
                                            }
                                        }
                                    } else {
                                        if (hashMap.containsKey(fclBuyCost.getCostId().getCodedesc())) {
                                            BookingfclUnits bookingfclUnits = new BookingfclUnits();
                                            flag = true;
                                            if (newFclBuyCostTypeRates.getRatAmount().compareTo(bookingfclUnits.getAmount()) != 0) {
                                                bookingfclUnits.setRateChangeAmount(newFclBuyCostTypeRates.getActiveAmt());
                                            }
                                            if (newFclBuyCostTypeRates.getMarkup().compareTo(bookingfclUnits.getMarkUp()) != 0) {
                                                bookingfclUnits.setRateChangeMarkup(newFclBuyCostTypeRates.getMarkup());
                                            }
                                        } else {
                                            String temp = "";
                                            if (fclBuyCost.getContType() != null && fclBuyCost.getContType().getCodedesc().equals("Flat Rate Per Container")) {
                                                for (Iterator iterator = tempRatesList.iterator(); iterator.hasNext();) {
                                                    BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();

                                                    if (bookingfclUnits.getUnitType() != null && !temp.equals(bookingfclUnits.getUnitType().getCode())) {
                                                        BookingfclUnits tempBookingfclUnits = new BookingfclUnits();
                                                        tempBookingfclUnits.setApproveBl(bookingfclUnits.getApproveBl());
                                                        tempBookingfclUnits.setUnitType(bookingfclUnits.getUnitType());
                                                        tempBookingfclUnits.setNumbers(bookingfclUnits.getNumbers());
                                                        tempBookingfclUnits.setChgCode(fclBuyCost.getCostId().getCodedesc());
                                                        tempBookingfclUnits.setChargeCodeDesc(fclBuyCost.getCostId().getCode());
                                                        tempBookingfclUnits.setCostType(fclBuyCost.getContType().getCodedesc());
                                                        tempBookingfclUnits.setAmount(newFclBuyCostTypeRates.getRatAmount());
                                                        tempBookingfclUnits.setMarkUp(newFclBuyCostTypeRates.getMarkup());
                                                        tempBookingfclUnits.setSellRate(tempBookingfclUnits.getAmount() + tempBookingfclUnits.getMarkUp());
                                                        tempBookingfclUnits.setAdjustment(0.00);
                                                        tempBookingfclUnits.setStandardCharge(bookingfclUnits.getStandardCharge());
                                                        tempBookingfclUnits.setCurrency(bookingfclUnits.getCurrency());
                                                        tempBookingfclUnits.setCommcode(bookingfclUnits.getCommcode());
                                                        tempBookingfclUnits.setSpecialEquipment(bookingfclUnits.getSpecialEquipment());
                                                        tempBookingfclUnits.setSpecialEquipmentUnit(bookingfclUnits.getSpecialEquipmentUnit());
                                                        tempBookingfclUnits.setEfectiveDate(fclBuyCost.getEffectiveDate());
                                                        tempBookingfclUnits.setAccountName(ssline);
                                                        tempBookingfclUnits.setAccountNo(sscode);
                                                        bookingFclRatesList.add(tempBookingfclUnits);
                                                        flag = true;
                                                        temp = bookingfclUnits.getUnitType().getCode();
                                                    }
                                                }
                                            } else if (fclBuyCost.getContType() != null && fclBuyCost.getContType().getCodedesc().equals("PER BL CHARGES")) {
                                                BookingfclUnits tempBookingfclUnits = new BookingfclUnits();
                                                tempBookingfclUnits.setChgCode(fclBuyCost.getCostId().getCodedesc());
                                                tempBookingfclUnits.setChargeCodeDesc(fclBuyCost.getCostId().getCode());
                                                tempBookingfclUnits.setCostType(fclBuyCost.getContType().getCodedesc());
                                                tempBookingfclUnits.setAmount(newFclBuyCostTypeRates.getRatAmount());
                                                tempBookingfclUnits.setMarkUp(newFclBuyCostTypeRates.getMarkup());
                                                tempBookingfclUnits.setSellRate(tempBookingfclUnits.getAmount() + tempBookingfclUnits.getMarkUp());
                                                tempBookingfclUnits.setAdjustment(0.00);
                                                tempBookingfclUnits.setStandardCharge(newFclBuyCostTypeRates.getStandard());
                                                if (null != newFclBuyCostTypeRates.getCurrency() && CommonUtils.isNotEmpty(newFclBuyCostTypeRates.getCurrency().getCode())) {
                                                    tempBookingfclUnits.setCurrency(newFclBuyCostTypeRates.getCurrency().getCode());
                                                } else {
                                                    tempBookingfclUnits.setCurrency("USD");
                                                }
                                                tempBookingfclUnits.setCommcode(fclBuy.getComNum().getId());
                                                tempBookingfclUnits.setEfectiveDate(fclBuyCost.getEffectiveDate());
                                                tempBookingfclUnits.setAccountName(ssline);
                                                tempBookingfclUnits.setAccountNo(sscode);
                                                otherChargesList.add(tempBookingfclUnits);
                                                flag = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!flag) {
                                for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                    FclBuyCostTypeRates newFclBuyCostTypeRates = new FclBuyCostTypeRates();
                                    PropertyUtils.copyProperties(newFclBuyCostTypeRates, fclBuyCostTypeRates);
                                    if (CommonUtils.isEqualIgnoreCase(fclBuyCost.getCostId().getCodedesc(), costCode) && CommonUtils.isNotEmpty(addsub)) {
                                        if ("A".equalsIgnoreCase(addsub)) {
                                            if ("Y".equalsIgnoreCase(region)) {
                                                newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup2);
                                            } else {
                                                newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup1);
                                            }
                                        } else {
                                            if ("Y".equalsIgnoreCase(region)) {
                                                newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup2);
                                            } else {
                                                newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup1);
                                            }
                                        }
                                    }
                                    if (newFclBuyCostTypeRates.getEffectiveDate() != null && bookingFcl.getQuoteDate().before(newFclBuyCostTypeRates.getEffectiveDate()) && newFclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                        if (unitHashMap.containsKey(newFclBuyCostTypeRates.getUnitType().getCodedesc())) {
                                            BookingfclUnits orgBookingfclUnits = unitHashMap.get(newFclBuyCostTypeRates.getUnitType().getCodedesc());
                                            BookingfclUnits bookingfclUnits = new BookingfclUnits();
                                            bookingfclUnits.setApproveBl(orgBookingfclUnits.getApproveBl());
                                            bookingfclUnits.setUnitType(newFclBuyCostTypeRates.getUnitType());
                                            bookingfclUnits.setNumbers(orgBookingfclUnits.getNumbers());
                                            bookingfclUnits.setChgCode(fclBuyCost.getCostId().getCodedesc());
                                            bookingfclUnits.setChargeCodeDesc(fclBuyCost.getCostId().getCode());
                                            bookingfclUnits.setCostType(fclBuyCost.getContType().getCodedesc());
                                            bookingfclUnits.setAmount(newFclBuyCostTypeRates.getActiveAmt());
                                            bookingfclUnits.setMarkUp(newFclBuyCostTypeRates.getMarkup());
                                            bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                                            bookingfclUnits.setAdjustment(0.00);
                                            bookingfclUnits.setStandardCharge(orgBookingfclUnits.getStandardCharge());
                                            bookingfclUnits.setCurrency(orgBookingfclUnits.getCurrency());
                                            bookingfclUnits.setCommcode(orgBookingfclUnits.getCommcode());
                                            bookingfclUnits.setSpecialEquipment(orgBookingfclUnits.getSpecialEquipment());
                                            bookingfclUnits.setSpecialEquipmentUnit(orgBookingfclUnits.getSpecialEquipmentUnit());
                                            bookingfclUnits.setEfectiveDate(fclBuyCost.getEffectiveDate());
                                            bookingfclUnits.setAccountName(ssline);
                                            bookingfclUnits.setAccountNo(sscode);
                                            bookingFclRatesList.add(bookingfclUnits);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return bookingFclRatesList;
    }

    public void updateBookingInSession(FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking,
            BookingFcl bookingFcl) throws Exception {
        fileNumberForQuotaionBLBooking.setSsBkgNo(bookingFcl.getSSBookingNo());
        List hazmatList = quotationBC.getHazmatList("Booking", bookingFcl.getBookingId().toString());
        if (hazmatList.size() > 0) {
            fileNumberForQuotaionBLBooking.setHazmat("H");
        } else {
            fileNumberForQuotaionBLBooking.setHazmat("");
        }

        if (bookingFcl.getBlFlag() == null || bookingFcl.getBlFlag().equalsIgnoreCase("off")
                || (null != bookingFcl.getConvertedToBlStatusFlag() && bookingFcl.getConvertedToBlStatusFlag().equalsIgnoreCase("on"))) {
            //----Setting Status by Calculating the number of days from the current Date------
            String status = setStatus(bookingFcl);
            fileNumberForQuotaionBLBooking.setFclBlStatus(status);
        }

        if (bookingFcl.getDocumentsReceived() != null && bookingFcl.getDocumentsReceived().equals("Y")) {
            fileNumberForQuotaionBLBooking.setDocReceived(bookingFcl.getDocumentsReceived());
        } else {
            fileNumberForQuotaionBLBooking.setDocReceived("N");
        }
        //----To set green icon for booking once booking is completed-------
        if (bookingFcl.getBookingComplete() != null && bookingFcl.getBookingComplete().equalsIgnoreCase("Y")) {
            fileNumberForQuotaionBLBooking.setBookingComplete("Y");
        } else {
            fileNumberForQuotaionBLBooking.setBookingComplete("N");
        }
        FclBl fclBl = fclBlDAO.getFileNoObject(bookingFcl.getFileNo());
        if (fclBl != null) {
            fileNumberForQuotaionBLBooking.setFclBlId(fclBl.getBol());
        }
        BookingFcl bookingByFileNo = new BookingFclDAO().getFileNoObject(bookingFcl.getFileNo());
        if (null != bookingByFileNo) {
            if (bookingByFileNo.getQuoteNo() != null && !bookingByFileNo.getQuoteNo().equals("")) {
                fileNumberForQuotaionBLBooking.setQuotId(Integer.parseInt(bookingFcl.getQuoteNo()));
            }
        } else {
            if (bookingFcl.getQuoteNo() != null && !bookingFcl.getQuoteNo().equals("")) {
                fileNumberForQuotaionBLBooking.setQuotId(Integer.parseInt(bookingFcl.getQuoteNo()));
            }
        }
        fileNumberForQuotaionBLBooking.setBookingId(bookingFcl.getBookingId());
        fileNumberForQuotaionBLBooking.setDisplayColor("RED");
    }

    public String setStatus(BookingFcl bookingFcl) throws Exception {
        String status = "";
        if (bookingFcl.getDocCutOff() != null) {
            long k = dbUtil.getDaysBetweenTwoDays(DateUtils.formatDateAndParseTo(bookingFcl.getDocCutOff(), "MM/dd/yyyy"),
                    DateUtils.formatDateAndParseTo(new Date(), "MM/dd/yyyy"));
            status += (k >= 0) ? (k) + "D," : "";
        }
        if (bookingFcl.getPortCutOff() != null) {
            long k = dbUtil.getDaysBetweenTwoDays(DateUtils.formatDateAndParseTo(bookingFcl.getPortCutOff(), "MM/dd/yyyy"),
                    DateUtils.formatDateAndParseTo(new Date(), "MM/dd/yyyy"));
            status += (k >= 0) ? (k) + "C," : "";
        }
        if (bookingFcl.getEtd() != null) {
            long k = dbUtil.getDaysBetweenTwoDays(DateUtils.formatDateAndParseTo(bookingFcl.getEtd(), "MM/dd/yyyy"),
                    DateUtils.formatDateAndParseTo(new Date(), "MM/dd/yyyy"));
            status += (k >= 0) ? (k + 0) + "S," : "";
        }
        int unit = 0;
        if (null != bookingFcl.getBookingId()) {
            unit = new BookingfclUnitsDAO().getBookingUnits(bookingFcl.getBookingId().toString());
            if (unit > 0) {
                status += (unit) + "U" + ",";
            }
        }
        status = ("N".equals(bookingFcl.getRatesNonRates())) ? ((status.contains("NR")) ? status
                : status + "NR" + ",")
                : (status.contains("NR")) ? status.replace(",NR", ",") : status;
        return status;
    }

    public List getBookingInfo(int BookingId) throws Exception {
        String sql = "SELECT  "
                + " q.file_no , "
                + " q.Quote_ID,"
                + " b.BookingId,"
                + " f.Bol, "
                + " p.id AS processId, "
                + " q.quote_by, "
                + " q.Quote_Date, "
                + " f.bl_by, "
                + " f.Bol_date, "
                + " COUNT(d.Operation), "
                + " h.docType_code,  "
                + " u.first_name  "
                + " FROM quotation q  "
                + " LEFT JOIN booking_fcl b ON q.file_no = b.file_no "
                + " LEFT JOIN process_info p ON p.record_id = q.file_no "
                + " LEFT JOIN fcl_bl f ON q.file_no = f.file_no  "
                + " LEFT JOIN hazmat_material h ON h.BolId = q.quote_id  "
                + " LEFT JOIN document_store_log d ON d.document_ID = q.file_no AND d.screen_Name = 'FCLFILE' AND d.operation IN ('Scan','Attach') "
                + " LEFT JOIN user_details u ON u.user_id = p.user_id "
                + " WHERE b.BookingId = '" + BookingId + "' group by q.file_no";
        SQLQuery query = quotationDAO.getCurrentSession().createSQLQuery(sql);
        return query.list();
    }

    public List<FclBuyCost> getListOfCharges(List<FclBuy> fclBuyLIst,
            MessageResources messageResources, String hazmat, HttpServletRequest request, String fileNo, List otherCommodityList, String region) throws Exception {
        bookingFcl = new BookingFclDAO().findbyFileNo(fileNo);
        Integer bookingFclId = bookingFcl.getBookingId();
        HashMap<String, BookingfclUnits> hashMap = new HashMap<String, BookingfclUnits>();
        HashMap<Integer, Integer> unitHashMap = new HashMap<Integer, Integer>();
        Quotation quotation = quotationDAO.getFileNoObject(fileNo);
        List<BookingfclUnits> bookingFclUnitsList = new BookingfclUnitsDAO().getbookingfcl2(bookingFclId.toString());
        if (bookingFclUnitsList != null) {
            for (BookingfclUnits bookingfclUnits : bookingFclUnitsList) {
                if (bookingfclUnits.getUnitType() != null) {
                    if (unitHashMap.get(bookingfclUnits.getUnitType().getId()) == null) {
                        unitHashMap.put(bookingfclUnits.getUnitType().getId(), bookingfclUnits.getUnitType().getId());
                    }
                    hashMap.put(bookingfclUnits.getChgCode() + "-" + bookingfclUnits.getUnitType().getId(), bookingfclUnits);
                } else {
                    hashMap.put(bookingfclUnits.getChgCode(), bookingfclUnits);
                }
            }
        }
        List returnList = fclBuyDAO.getFclblChargesForBooking(fclBuyLIst, messageResources, hazmat, hashMap, unitHashMap, request, quotation, otherCommodityList, region);
        return returnList;
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

    public List<CustomerContact> getListOfPartyDetails(String bookingId) throws Exception {
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        List<CustomerContact> custContactList = new ArrayList<CustomerContact>();
        Map<String, CustomerContact> custContactMap = new HashMap<String, CustomerContact>();
        if (CommonFunctions.isNotNull(bookingId)) {
            bookingFcl = bookingFclDAO.findById(Integer.parseInt(bookingId));

            if (CommonFunctions.isNotNull(bookingFcl.getAgentNo())) {
                //agent
                tradingPartnerBC.getCustomerContactList(custContactMap, bookingFcl.getAgentNo());
            }
            if (CommonFunctions.isNotNull(bookingFcl.getForwNo())) {
                //Forwarder
                tradingPartnerBC.getCustomerContactList(custContactMap, bookingFcl.getForwNo());
            }
            if (CommonFunctions.isNotNull(bookingFcl.getShipNo())) {
                //Shipper
                tradingPartnerBC.getCustomerContactList(custContactMap, bookingFcl.getShipNo());
            }
            if (CommonFunctions.isNotNull(bookingFcl.getAccountNumber())) {
                //ThridParty
                tradingPartnerBC.getCustomerContactList(custContactMap, bookingFcl.getAccountNumber());
            }
            if (CommonFunctions.isNotNull(bookingFcl.getConsNo())) {
                //conginee
                tradingPartnerBC.getCustomerContactList(custContactMap, bookingFcl.getConsNo());
            }
            if (CommonFunctions.isNotNull(bookingFcl.getTruckerCode())) {
                //trucker
                tradingPartnerBC.getCustomerContactList(custContactMap, bookingFcl.getTruckerCode());
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

    public BookingfclUnits calculatePBACharge(Integer bookingId) throws Exception {
        BookingfclUnits bookingfclUnitsNew = new BookingfclUnits();
        boolean flag = false;
        List<Object[]> list = bookingFclUnitsDAO.getADVFFandADVSHPAmount(bookingId);
        if (list != null) {

            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                Object[] objects = (Object[]) iterator.next();
                Integer unitType = (Integer) objects[0];
                Double amount = (Double) objects[1];
                String chargeCode = (String) objects[2];
                Double amountMarkup = (Double) objects[3];
                if (amountMarkup != null && amountMarkup != 0d) {
                    amount = amountMarkup;
                    amount = CommonFunctions.getPercentOf(amount, new Integer(LoadLogisoftProperties.getProperty("advanceSurchargePercentage")));
                    List bookingUnitList = bookingFclUnitsDAO.checkChargeCode(bookingId.toString(), chargeCode, unitType, null);
                    if (CommonFunctions.isNotNullOrNotEmpty(bookingUnitList)) {
                        List PBAList = bookingFclUnitsDAO.checkChargeCode(bookingId.toString(), FclBlConstants.ADVANCESURCHARGECODE, unitType, null);

                        if (CommonFunctions.isNotNullOrNotEmpty(PBAList)) {
                            BookingfclUnits bookingfclUnits = (BookingfclUnits) PBAList.get(0);
                            bookingfclUnits.setAmount(0d);
                            bookingfclUnits.setMarkUp(amount);
                        } else {
                            BookingfclUnits bookingfclUnits = (BookingfclUnits) bookingUnitList.get(0);
                            PropertyUtils.copyProperties(bookingfclUnitsNew, bookingfclUnits);
                            bookingfclUnitsNew.setId(null);
                            bookingfclUnitsNew.setAmount(0d);
                            bookingfclUnitsNew.setMarkUp(amount);
                            bookingfclUnitsNew.setChargeCodeDesc(FclBlConstants.ADVANCESURCHARGECODE);
                            bookingfclUnitsNew.setChgCode(FclBlConstants.ADVANCESURCHARGEDESC);
                            bookingfclUnitsNew.setAccountName("");
                            bookingfclUnitsNew.setAccountNo("");
                            bookingFclUnitsDAO.save(bookingfclUnitsNew);
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag) {
            return bookingfclUnitsNew;
        } else {
            return null;
        }
    }

    public String getIssuingTM(String issuingTerminal) throws Exception {
        String domesticRoute = "";
        String issuingTerm = "";
        String codeNo = "";
        String code = "";
        String codeVal = "";
        String[] temp;
        String[] temp1;
        if (issuingTerminal != null) {
            issuingTerm = issuingTerminal;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Issued By ");
            stringBuilder.append("(");
            temp = issuingTerm.split(",");
            if (!Arrays.equals(temp, null) && temp.length == 2) {
                for (int i = 0; i < temp.length; i++) {
                    if (i == 0) {
                        codeVal = temp[i];
                    } else {
                        String codeandNo = temp[i];
                        temp1 = codeandNo.split("-");
                        for (int j = 0; j < temp1.length; j++) {
                            if (j == 0) {
                                code = temp1[j];
                            } else {
                                codeNo = temp1[j];
                            }
                        }
                    }
                }
            }
            stringBuilder.append(codeNo);
            stringBuilder.append(") ");
            stringBuilder.append(codeVal);
            stringBuilder.append(" -");
            stringBuilder.append(code);
            domesticRoute = stringBuilder.toString();
        }
        return domesticRoute;
    }

    public List orderExpandList(List ratesList) {
        TreeMap<String, BookingfclUnits> map = new TreeMap<String, BookingfclUnits>();
        List resultList = new ArrayList();
        for (Iterator it = ratesList.iterator(); it.hasNext();) {
            BookingfclUnits bookingFclUnits = (BookingfclUnits) it.next();
            String flag = "";
            String unitFlag = "";
            String standardFlag = "";
            GenericCode genericCode = bookingFclUnits.getUnitType();
            if ("Y".equalsIgnoreCase(bookingFclUnits.getStandardCharge())) {
                standardFlag = "0";
            } else {
                if (null != bookingFclUnits.getStandardCharge()) {
                    standardFlag = bookingFclUnits.getStandardCharge();
                } else {
                    standardFlag = "9";
                }
            }
            if (CommonUtils.isNotEmpty(bookingFclUnits.getSpecialEquipmentUnit())) {
                unitFlag = bookingFclUnits.getSpecialEquipmentUnit();
                if ("OCNFRT".equals(bookingFclUnits.getChargeCodeDesc()) || "OFIMP".equals(bookingFclUnits.getChargeCodeDesc())) {
                    flag = "BAAA";
                } else if (CommonUtils.isNotEmpty(bookingFclUnits.getManualCharges())) {
                    flag = "B" + bookingFclUnits.getManualCharges();
                } else if (bookingFclUnits.getChargeCodeDesc().startsWith("INT")) {
                    if (bookingFclUnits.getChargeCodeDesc().equals("INTRAMP")) {
                        flag = "BAZ";
                    } else if (bookingFclUnits.getChargeCodeDesc().startsWith("INT")) {
                        flag = "BAZZ";
                    }
                } else {
                    flag = "BA";
                }
            } else {
                if (null != genericCode) {
                    unitFlag = genericCode.getCode() + genericCode.getCode();
                }
                if ("OCNFRT".equals(bookingFclUnits.getChargeCodeDesc()) || "OFIMP".equals(bookingFclUnits.getChargeCodeDesc())) {
                    flag = "AAAA";
                } else if (CommonUtils.isNotEmpty(bookingFclUnits.getManualCharges())) {
                    flag = "A" + bookingFclUnits.getManualCharges();
                } else if (bookingFclUnits.getChargeCodeDesc().startsWith("INT")) {
                    if (bookingFclUnits.getChargeCodeDesc().equals("INTRAMP")) {
                        flag = "AAZ";
                    } else if (bookingFclUnits.getChargeCodeDesc().startsWith("INT")) {
                        flag = "AAZZ";
                    }
                } else {
                    flag = "AA";
                }
            }
            if (null != bookingFclUnits.getUnitType()) {
                map.put("" + bookingFclUnits.getUnitType().getId() + standardFlag + flag + unitFlag + bookingFclUnits.getChargeCodeDesc(), bookingFclUnits);
            } else {
                map.put(standardFlag + flag + unitFlag + bookingFclUnits.getChargeCodeDesc(), bookingFclUnits);
            }
        }
        Set keySet = map.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            resultList.add(map.get(key));
        }
        return resultList;
    }

    public List orderNonRatedList(List ratesList) {
        TreeMap<String, BookingfclUnits> map = new TreeMap<String, BookingfclUnits>();
        List resultList = new ArrayList();
        for (Iterator it = ratesList.iterator(); it.hasNext();) {
            BookingfclUnits bookingFclUnits = (BookingfclUnits) it.next();
            String flag = "";
            String unitFlag = "";
            String standardFlag = "";
            GenericCode genericCode = bookingFclUnits.getUnitType();
            if ("Y".equalsIgnoreCase(bookingFclUnits.getStandardCharge())) {
                standardFlag = "0";
            } else {
                if (null != bookingFclUnits.getStandardCharge()) {
                    standardFlag = bookingFclUnits.getStandardCharge();
                } else {
                    standardFlag = "9";
                }
            }
            if (CommonUtils.isNotEmpty(bookingFclUnits.getSpecialEquipmentUnit())) {
                unitFlag = bookingFclUnits.getSpecialEquipmentUnit();
                if ("OCNFRT".equals(bookingFclUnits.getChargeCodeDesc())) {
                    flag = "BAAA";
                } else if (CommonUtils.isNotEmpty(bookingFclUnits.getManualCharges())) {
                    flag = "B" + bookingFclUnits.getManualCharges();
                } else {
                    flag = "BA";
                }
            } else {
                if (null != genericCode) {
                    unitFlag = genericCode.getCode() + genericCode.getCode();
                }
                if ("OCNFRT".equals(bookingFclUnits.getChargeCodeDesc())) {
                    flag = "AAAA";
                } else if (CommonUtils.isNotEmpty(bookingFclUnits.getManualCharges())) {
                    flag = "A" + bookingFclUnits.getManualCharges();
                } else {
                    flag = "AA";
                }
            }
            if (null != bookingFclUnits.getUnitType()) {
                map.put("" + bookingFclUnits.getUnitType().getId() + standardFlag + flag + unitFlag + bookingFclUnits.getChargeCodeDesc() + "" + bookingFclUnits.getId(), bookingFclUnits);
            } else {
                map.put(standardFlag + flag + unitFlag + bookingFclUnits.getChargeCodeDesc() + "" + bookingFclUnits.getId(), bookingFclUnits);
            }
        }
        Set keySet = map.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            resultList.add(map.get(key));
        }
        return resultList;
    }

    public String getGRIRemarks(BookingFcl bookingFcl) throws Exception {
        String unlocationCode = "";
        String destination = "";
        String griRemarks = "";
        if (bookingFcl.getPortofDischarge() != null) {
            destination = bookingFcl.getPortofDischarge();
            int j = destination.indexOf("/");
            if (j != -1) {
                String a[] = destination.split("/");
                destination = a[0];
                if (bookingFcl.getPortofDischarge().lastIndexOf("(") != -1) {
                    unlocationCode = bookingFcl.getPortofDischarge().substring(bookingFcl.getPortofDischarge().lastIndexOf("(") + 1,
                            bookingFcl.getPortofDischarge().lastIndexOf(")"));
                } else {
                    unlocationCode = a[1];
                }
            }
            griRemarks = unLocationDAO.getDestinationGRIRemarks(destination, bookingFcl.getSSLine(), unlocationCode);
        }
        return null != griRemarks ? griRemarks : "";
    }

    public void saveNotes(BookingFcl booking, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        List<Charges> hazChargeList = bookingFclUnitsDAO.findByPropertyUsingBookingId("bookingNumber", String.valueOf(booking.getBookingId()), "ChargeCodeDesc", BookingConstants.HAZARDOUS_CODE_DESC);
        if (null != booking.getHazmat() && "Y".equalsIgnoreCase(booking.getHazmat()) && hazChargeList.isEmpty()) {
            Notes notes = new Notes();
            notes.setModuleId("FILE");
            notes.setUpdateDate(new Date());
            notes.setNoteTpye("auto");
            notes.setNoteDesc("File " + booking.getFileNo() + " is hazardous but does not have hazardous surcharges.");
            notes.setUpdatedBy(user.getLoginName());
            notes.setModuleRefId(booking.getFileNo());
            new NotesDAO().save(notes);
        }
    }

    public void saveAutoNotes(BookingFcl booking, HttpServletRequest request, String Notes) throws Exception {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        Notes notes = new Notes();
        notes.setModuleId("FILE");
        notes.setUpdateDate(new Date());
        notes.setNoteTpye("auto");
        notes.setNoteDesc(Notes);
        notes.setUpdatedBy(user.getLoginName());
        notes.setModuleRefId(booking.getFileNo().toString());
        new NotesDAO().save(notes);
    }

    public void addCostDetailsForBooking(EditBookingsForm editBookingForm, HttpServletRequest request, User user) throws Exception {
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes(editBookingForm);
        fclBlCostCodes.setAccName(editBookingForm.getVendorAccountName());
        fclBlCostCodes.setAccNo(editBookingForm.getVendorAccountNo());
        fclBlCostCodes.setAmount(Double.parseDouble(dbUtil.removeComma(editBookingForm.getCostAmount())));
        fclBlCostCodes.setAccrualsCreatedDate(new Date());
        fclBlCostCodes.setAccrualsCreatedBy(user.getUserId().toString());
        if (CommonUtils.isNotEmpty(editBookingForm.getBookingId())) {
            fclBlCostCodes.setbookingId(Integer.parseInt(editBookingForm.getBookingId()));
        }
        fclBlCostCodes.setCostComments(editBookingForm.getCostComments());
        fclBlCostCodes.setInvoiceNumber(editBookingForm.getInvoiceNumber());
        fclBlCostCodes.setCurrencyCode(editBookingForm.getCostCurrency());
        if (null != editBookingForm.getAccurlsDatePaid()) {
            fclBlCostCodes.setDatePaid(editBookingForm.getAccurlsDatePaid());
        }
        fclBlCostCodes.setTransactionType("AC");
        new FclBlCostCodesDAO().save(fclBlCostCodes);
        bookingFcl = bookingFclDAO.findById(Integer.parseInt(editBookingForm.getBookingId()));
        ManifestModel manifestModel = new ManifestModel();
        manifestModel.setFclBlCostCodes(fclBlCostCodes);
        manifestModel.setBookingFcl(bookingFcl);
        manifestModel.setUser(user);
        new LclManifestDAO().addAndUpdateFclBookingAccruals(manifestModel);
        new NotesBC().saveNotesWhileAddingFclBlCostCodes(fclBlCostCodes, user.getLoginName().toUpperCase());
    }

    public void updateBookingCostDetails(EditBookingsForm editBookingsForm, User user) throws Exception {
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodesDAO().findById(Integer.parseInt(editBookingsForm.getCostCodeId()));
        Date date = null;
        boolean hasChanged = false;
        StringBuilder message = new StringBuilder();
        if (null != fclBlCostCodes) {
            fclBlCostCodes.setAmount(Double.parseDouble(editBookingsForm.getCostAmount()));
            fclBlCostCodes.setAccrualsUpdatedBy(user.getUserId().toString());
            fclBlCostCodes.setAccrualsUpdatedDate(new Date());
            if (null != fclBlCostCodes.getCostCode() && !fclBlCostCodes.getCostCode().equals(editBookingsForm.getCostCode())) {
                hasChanged = true;
            }
            fclBlCostCodes.setCostCode(null != editBookingsForm.getCostCode() ? editBookingsForm.getCostCode() : "");
            fclBlCostCodes.setCostCodeDesc(null != editBookingsForm.getCostCodeDesc() ? editBookingsForm.getCostCodeDesc() : "");
            if (null != fclBlCostCodes.getAccNo() && !fclBlCostCodes.getAccNo().equals(editBookingsForm.getVendorAccountNo())) {
                hasChanged = true;
            }
            fclBlCostCodes.setAccName(null != editBookingsForm.getVendorAccountName() ? editBookingsForm.getVendorAccountName() : "");
            fclBlCostCodes.setAccNo(null != editBookingsForm.getVendorAccountNo() ? editBookingsForm.getVendorAccountNo() : "");
            if (null != fclBlCostCodes.getInvoiceNumber() && !fclBlCostCodes.getInvoiceNumber().equals(editBookingsForm.getInvoiceNumber())) {
                hasChanged = true;
            }
            fclBlCostCodes.setInvoiceNumber(editBookingsForm.getInvoiceNumber());
            fclBlCostCodes.setCurrencyCode(null != editBookingsForm.getCostCurrency() ? editBookingsForm.getCostCurrency() : "");
            if (null != editBookingsForm.getAccurlsDatePaid()) {
                date = editBookingsForm.getAccurlsDatePaid();
                fclBlCostCodes.setDatePaid(date);
            }
            fclBlCostCodes.setTransactionType("AC");
            fclBlCostCodes.setCostComments(CommonFunctions.isNotNull(editBookingsForm.getCostComments())
                    ? editBookingsForm.getCostComments().toUpperCase() : editBookingsForm.getCostComments());
            new FclBlCostCodesDAO().update(fclBlCostCodes);
            bookingFcl = bookingFclDAO.findById(Integer.parseInt(editBookingsForm.getBookingId()));
            ManifestModel manifestModel = new ManifestModel();
            manifestModel.setFclBlCostCodes(fclBlCostCodes);
            manifestModel.setBookingFcl(bookingFcl);
            manifestModel.setUser(user);
            new LclManifestDAO().addAndUpdateFclBookingAccruals(manifestModel);
            if (hasChanged) {
                message.append("UPDATED ->Cost Code - ").append(fclBlCostCodes.getCostCodeDesc());
                message.append(" Vendor Name - ").append(fclBlCostCodes.getAccName());
                message.append(" Vendor Number - ").append(fclBlCostCodes.getAccNo());
                message.append(" Invoice Number - ").append(fclBlCostCodes.getInvoiceNumber());
                if (CommonUtils.isNotEmpty(fclBlCostCodes.getCostComments())) {
                    message.append(" Comment -").append(fclBlCostCodes.getCostComments());
                }
                Notes note = new Notes();
                note.setModuleId(NotesConstants.FILE);
                note.setUniqueId("" + fclBlCostCodes.getCodeId());
                String fileNo = null != bookingFcl ? bookingFcl.getFileNo() : "";
                note.setModuleRefId(fileNo);
                note.setUpdateDate(new Date());
                note.setUpdatedBy(user.getLoginName().toUpperCase());
                note.setNoteType(NotesConstants.AUTO);
                note.setNoteDesc(message.toString());
                new NotesBC().saveNotes(note);
            }
        }
    }

    public void editCostDetailsForBooking(EditBookingsForm editBookingsForm, HttpServletRequest request) throws Exception {
        if (request.getParameter("costId") != null) {
            FclBlCostCodes fclBlCostCodes = new FclBlCostCodesDAO().findById(Integer.parseInt(request.getParameter("costId")));
            editBookingsForm.setCostAmount(fclBlCostCodes.getAmount().toString());
            editBookingsForm.setBookingAccrualsCreatedBy(fclBlCostCodes.getAccrualsCreatedBy());
            editBookingsForm.setBookingAccrualsCreatedDate(fclBlCostCodes.getAccrualsCreatedDate());
            editBookingsForm.setCostCode(fclBlCostCodes.getCostCode());
            editBookingsForm.setCostCodeDesc(fclBlCostCodes.getCostCodeDesc());
            editBookingsForm.setInvoiceNumber(fclBlCostCodes.getInvoiceNumber());
            editBookingsForm.setTransactionType(fclBlCostCodes.getTransactionType());
            editBookingsForm.setAccurlsDatePaid(fclBlCostCodes.getDatePaid());
            editBookingsForm.setVendorAccountName(fclBlCostCodes.getAccName());
            editBookingsForm.setVendorAccountNo(fclBlCostCodes.getAccNo());
            editBookingsForm.setCostComments(fclBlCostCodes.getCostComments());
            editBookingsForm.setCostCurrency(fclBlCostCodes.getCurrencyCode());
            editBookingsForm.setBookingId(fclBlCostCodes.getbookingId().toString());
            editBookingsForm.setCostCodeId(fclBlCostCodes.getCodeId().toString());
        }
    }

    public void deleteCostDetails(EditBookingsForm editBookingsForm, String userName) throws Exception {
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodesDAO().findById(Integer.parseInt(editBookingsForm.getCostCode()));
        if (null != fclBlCostCodes) {
            StringBuilder message = new StringBuilder();
            new LclManifestDAO().deleteFclBookingAccruals(editBookingsForm.getCostCode(), editBookingsForm.getFileNo());
            message.append("DELETED ->Cost Code - ").append(fclBlCostCodes.getCostCode()).append(" Cost - ");
            message.append(numb.format(fclBlCostCodes.getAmount())).append(" Currency - ").append(fclBlCostCodes.getCurrencyCode());
            new BookingFclDAO().deleteBookingCharges(editBookingsForm.getCostCode());
            Notes note = new Notes();
            note.setModuleId(NotesConstants.FILE);
            note.setModuleRefId(editBookingsForm.getFileNo());
            note.setUpdateDate(new Date());
            note.setNoteType(NotesConstants.AUTO);
            note.setNoteDesc(message.toString());
            note.setUpdatedBy(userName);
            new NotesBC().saveNotes(note);
        }
    }

    public void copyBooking(EditBookingsForm bookingForm, HttpSession session, MessageResources messageResources, String buttonValue) throws Exception {
        String originalBookingFileNo = bookingForm.getFileNo();
        if (null != originalBookingFileNo && !originalBookingFileNo.equals("")) {
            ChargesDAO chargesDAO = new ChargesDAO();
            User userDomain = new User();
            if (session.getAttribute("loginuser") != null) {
                userDomain = (User) session.getAttribute("loginuser");
            }
            String userName = userDomain.getLoginName();
            Date newDate = new Date();
            String newDate1 = simpleDateFormat.format(newDate);
            Date currentDate = simpleDateFormat.parse(newDate1);
            Quotation originalQuote = quotationDAO.findbyFileNo(originalBookingFileNo);
            GenerateFileNumber generateFileNumber = new GenerateFileNumber();// wil generate file number
            generateFileNumber.join();// it wil force thread to complete the task before move to next step
            String newFileNo = generateFileNumber.getFileNumber().toString();
            Integer newQuoteId = null;
            if (null != originalQuote) {
                Quotation newQuote = new Quotation();
                Integer originalQuoteId = originalQuote.getQuoteId();
                PropertyUtils.copyProperties(newQuote, originalQuote);
                newQuote.setQuoteId(null);
                newQuote.setQuoteNo(null);
                newQuote.setFileNo(newFileNo);
                newQuote.setQuoteBy(userName);
                newQuote.setQuoteDate(currentDate);
                newQuote.setQuoteFlag("Book");
                newQuote.setFinalized("on");
                newQuote.setBookedBy(userName);
                newQuote.setUserName(userName);
                newQuote.setUserEmail(userDomain.getEmail());
                newQuote.setPhone(userDomain.getTelephone());
//                newQuote.setRegionRemarks(null);
//                newQuote.setFclGRIRemarks(null);
//                newQuote.setRatesRemarks(null);
                Set hazmatSet = new LinkedHashSet<HazmatMaterial>();
                if (originalQuote.getHazmatSet() != null) {
                    Iterator iter = (Iterator) originalQuote.getHazmatSet().iterator();
                    while (iter.hasNext()) {
                        HazmatMaterial haz = (HazmatMaterial) iter.next();
                        if (haz.getDocTypeCode() != null && haz.getDocTypeCode().equals("Quote")) {
                            HazmatMaterial hazmat1 = new HazmatMaterial();
                            PropertyUtils.copyProperties(hazmat1, haz);
                            hazmat1.setBolId(null);
                            hazmat1.setId(null);
                            hazmatSet.add(hazmat1);
                        }
                    }
                }
                newQuote.setHazmatSet(hazmatSet);
                quotationDAO.save(newQuote);//Create New Quote For New Booking

                newQuoteId = newQuote.getQuoteId();

                newQuote.setQuoteNo(newQuoteId.toString());
                new QuotationBC().getRemarksandTransitDaysFromFclSellratesForEdit(newQuote);
                quotationDAO.save(newQuote);//Update Quote_no for new Quotation Based on Quote_id

                List<Charges> originalBookingsQuoteChargesList = chargesDAO.findByProperty("qouteId", originalQuoteId);
                for (Charges oldCharges : originalBookingsQuoteChargesList) {//Copying Original Quote Charges For New Quote.
                    Charges newCharges = new Charges();
                    PropertyUtils.copyProperties(newCharges, oldCharges);
                    newCharges.setQouteId(newQuoteId);
                    newCharges.setNumber("1");
                    newCharges.setUpdateBy(userName);
                    newCharges.setUpdateOn(currentDate);
                    chargesDAO.save(newCharges);
                }
            }

            BookingFcl originalBooking = bookingFclDAO.findbyFileNo(originalBookingFileNo);
            BookingFcl newBooking = new BookingFcl();
            PropertyUtils.copyProperties(newBooking, originalBooking);
            String originalBookingNo = originalBooking.getBookingNumber();
            newBooking.setFileNo(newFileNo);
            newBooking.setBookingNumber(null);
            newBooking.setBookingId(null);
            newBooking.setSpotRate(originalBooking.getSpotRate());
            newBooking.setVessel("");
            newBooking.setVesselNameCheck("");
            newBooking.setSSBookingNo("");
            newBooking.setPortCutOff(null);
            newBooking.setDocCutOff(null);
            newBooking.setEtd(null);
            newBooking.setEta(null);
            newBooking.setVgmCuttOff(null);
            newBooking.setEarliestPickUpDate(null);
            newBooking.setPositioningDate(null);
            newBooking.setDateoutYard(null);
            newBooking.setDateInYard(null);
            newBooking.setRailCutOff(null);
            newBooking.setCutofDate(null);
            newBooking.setVoyDocCutOff(null);
            newBooking.setEmptyPickUpDate(null);
            newBooking.setEmptyReturnDate(null);
            newBooking.setLoadDate(null);
            newBooking.setEdiCanceledBy(null);
            newBooking.setEdiCanceledOn(null);
            newBooking.setEdiCreatedBy(null);
            newBooking.setEdiCreatedOn(null);
            newBooking.setCargoReadyDate(null);
            newBooking.setBookingCompletedDate(null);
            newBooking.setBookingComplete("N");
            newBooking.setBlFlag("off");
            newBooking.setBookedBy(userName);
            newBooking.setUsername(userName);
            newBooking.setBookingDate(currentDate);
            newBooking.setQuoteBy(userName);
            newBooking.setQuoteDate(currentDate);
            newBooking.setDocumentsReceived("N");
            if (null != newQuoteId) {
                newBooking.setQuoteNo(newQuoteId.toString());
            } else {
                newBooking.setQuoteNo(null);
            }
            newBooking.setBlBy(null);
            newBooking.setBlDate(null);

            List<BookingInbondDetails> originalBookingInbondDetailsList = new ArrayList(originalBooking.getBookingInbondDetails());//InbondDetails
            Set<BookingInbondDetails> newBookingInbondDetailsSet = new HashSet<BookingInbondDetails>();
            for (BookingInbondDetails oldBookingInbondDetails : originalBookingInbondDetailsList) {
                BookingInbondDetails newBookingInbondDetails = new BookingInbondDetails();
                PropertyUtils.copyProperties(newBookingInbondDetails, oldBookingInbondDetails);
                newBookingInbondDetails.setId(null);
                newBookingInbondDetails.setBolId(null);
                newBookingInbondDetailsSet.add(newBookingInbondDetails);
            }

            newBooking.setBookingInbondDetails(newBookingInbondDetailsSet);

//        Set bookingHazmatSet = new LinkedHashSet<HazmatMaterial>();//hazmat
//        if (originalBooking.getHazmatSet() != null) {
//            Iterator iter = (Iterator) originalBooking.getHazmatSet().iterator();
//            while (iter.hasNext()) {
//                HazmatMaterial bookHaz = (HazmatMaterial) iter.next();
//                if (bookHaz.getDocTypeCode() != null && bookHaz.getDocTypeCode().equals("Booking")) {
//                    HazmatMaterial hazmat1 = new HazmatMaterial();
//                    PropertyUtils.copyProperties(hazmat1, bookHaz);
//                    hazmat1.setBolId(null);
//                    hazmat1.setId(null);
//                    bookingHazmatSet.add(hazmat1);
//                }
//            }
//        }
            newBooking.setHazmatSet(null);
            bookingFclDAO.save(newBooking);//Creating New Booking
            String newBookingNo = newBooking.getBookingId().toString();
            List<BookingfclUnits> originalBookingChargesList = bookingFclUnitsDAO.findByProperty("bookingNumber", originalBookingNo);
            if (buttonValue.equals("copyBooking")) {
                for (BookingfclUnits oldBookingcharges : originalBookingChargesList) {//Charges and Containers
                    BookingfclUnits newBookingcharges = new BookingfclUnits();
                    PropertyUtils.copyProperties(newBookingcharges, oldBookingcharges);
                    newBookingcharges.setBookingNumber(newBookingNo);
                    newBookingcharges.setNumbers("1");
                    newBookingcharges.setApproveBl("No");
                    newBookingcharges.setUpdateBy(userName);
                    newBookingcharges.setUpdateOn(currentDate);
                    new BookingfclUnitsDAO().save(newBookingcharges);
                }
            } else {
                //update RateChange Charges To New Booking Charges.
                List bookingFclRatesList = bookingFclUnitsDAO.getbookingfcl(originalBooking.getBookingNumber().toString());
                List otherChargesList = bookingFclUnitsDAO.getbookingfcl1(originalBooking.getBookingNumber().toString());
                List<BookingfclUnits> ratesChangeList = getRates(bookingForm, originalBooking, messageResources, bookingFclRatesList, otherChargesList);
                ratesChangeList.addAll(otherChargesList);
                for (BookingfclUnits ratesChangecharges : ratesChangeList) {//Charges and Containers
                    BookingfclUnits newBookingcharges = new BookingfclUnits();
                    PropertyUtils.copyProperties(newBookingcharges, ratesChangecharges);
                    newBookingcharges.setBookingNumber(newBookingNo);
                    if (null != ratesChangecharges.getRateChangeAmount() && ratesChangecharges.getRateChangeAmount() != 0d) {
                        newBookingcharges.setAmount(ratesChangecharges.getRateChangeAmount());
                    }
                    if (null != ratesChangecharges.getRateChangeMarkup() && ratesChangecharges.getRateChangeMarkup() != 0d) {
                        newBookingcharges.setMarkUp(ratesChangecharges.getRateChangeMarkup());
                    }
                    newBookingcharges.setSellRate(newBookingcharges.getAmount() + (null == newBookingcharges.getMarkUp() ? 0d : newBookingcharges.getMarkUp()));
                    newBookingcharges.setNumbers("1");
                    newBookingcharges.setApproveBl("No");
                    newBookingcharges.setUpdateBy(userName);
                    newBookingcharges.setUpdateOn(currentDate);
                    new BookingfclUnitsDAO().save(newBookingcharges);
                }
            }

            newBooking.setBookingNumber(newBookingNo);//set new BookingNumber
            newBooking = getRemarks(newBooking);//updating new Booking Remarks
            bookingFclDAO.update(newBooking);//update new Booking
//            Quotation originalQuote1 = quotationDAO.findbyFileNo(originalBookingFileNo);
//            Quotation newQuote = getRemarksForQuotationWhenCopyBooking(newBooking,originalQuote1);
//            quotationDAO.save(newQuote);

            if (buttonValue.equals("copyBookingWithNewRates")) { //update RateChange Charges To New Quotation Charges.
                List<BookingfclUnits> newBookingChargesList = bookingFclUnitsDAO.findByProperty("bookingNumber", newBooking.getBookingNumber());
                chargesDAO.updateNewChargesForQuote(newBookingChargesList, newQuoteId);
            }

            ProcessInfoBC processInfoBC = new ProcessInfoBC();//Releasing The Original File from record Lock
            if (CommonFunctions.isNotNull(originalBookingFileNo)) {
                processInfoBC.releaseLoack(messageResources.getMessage("lockQuoteModule"), originalBookingFileNo, userDomain.getUserId());
            }

            Notes notes = new Notes();//creating notes for copy booking
            notes.setModuleId("FILE");
            notes.setUpdateDate(currentDate);
            notes.setNoteTpye("auto");
            notes.setNoteDesc("Booking is copied from File No : " + originalBookingFileNo + ". ");
            notes.setUpdatedBy(userName);
            notes.setModuleRefId(newFileNo);
            notesDAO.save(notes);
            session.setAttribute("selectedFileNumber", newFileNo);
            if (null != originalBooking.getBlFlag() && originalBooking.getBlFlag().equals("on")) {
                session.setAttribute("screenName", "BL");
            } else {
                session.setAttribute("screenName", "Bookings");
            }
        }
    }

    public BookingFcl getRemarks(BookingFcl bookingFcl) throws Exception {
        UnLocation originTerminal = null;
        UnLocation rampcity = null;
        UnLocation destinationPort = null;
        UnLocation portOfloading = null;
        UnLocation portOfDischarge = null;
        String unlocationCode = "";
        String origin = "";
        String rampCity = "";
        List fclOrgDestMiscData = null;
        if (bookingFcl.getOriginTerminal() != null) {
            origin = bookingFcl.getOriginTerminal();
            int i = origin.indexOf("/");
            if (i != -1) {
                String a[] = origin.split("/");
                origin = a[0];
                unlocationCode = bookingFcl.getOriginTerminal().substring(bookingFcl.getOriginTerminal().lastIndexOf("(") + 1, bookingFcl.getOriginTerminal().lastIndexOf(")"));
            }
            List list = unLocationDAO.findForManagement(unlocationCode.trim(), origin.trim());
            if (list != null && list.size() > 0) {
                originTerminal = (UnLocation) list.get(0);
            }
        }
        if (bookingFcl.getRampCity() != null) {
            rampCity = bookingFcl.getRampCity();
            int i = rampCity.indexOf("/");
            if (i != -1) {
                String a[] = rampCity.split("/");
                rampCity = a[0];
                unlocationCode = a[1];
            }
            List list = unLocationDAO.findForManagement(unlocationCode.trim(), rampCity.trim());
            if (list != null && list.size() > 0) {
                rampcity = (UnLocation) list.get(0);
            }
        }
        unlocationCode = "";
        String pol = "";
        if (bookingFcl.getPortofOrgin() != null) {
            pol = bookingFcl.getPortofOrgin();
            int k = pol.indexOf("/");
            if (k != -1) {
                String a[] = pol.split("/");
                pol = a[0];
                unlocationCode = a[1];
            }

            List list2 = unLocationDAO.findForManagement(unlocationCode.trim(), pol.trim());
            if (list2 != null && list2.size() > 0) {
                portOfloading = (UnLocation) list2.get(0);
            }
        }
        unlocationCode = "";
        String pod = "";
        if (bookingFcl.getDestination() != null) {
            pod = bookingFcl.getDestination();
            int l = pod.indexOf("/");
            if (l != -1) {
                String a[] = pod.split("/");
                pod = a[0];
                unlocationCode = a[1];
            }
            List list3 = unLocationDAO.findForManagement(unlocationCode.trim(), pod.trim());
            if (list3 != null && list3.size() > 0) {
                portOfDischarge = (UnLocation) list3.get(0);
            }
        }
        unlocationCode = "";
        String destination = "";
        if (bookingFcl.getPortofDischarge() != null) {
            destination = bookingFcl.getPortofDischarge();
            int j = destination.indexOf("/");
            if (j != -1) {
                String a[] = destination.split("/");
                destination = a[0];
                unlocationCode = bookingFcl.getPortofDischarge().substring(bookingFcl.getPortofDischarge().lastIndexOf("(") + 1, bookingFcl.getPortofDischarge().lastIndexOf(")"));
            }
            List list1 = unLocationDAO.findForManagement(unlocationCode.trim(), destination.trim());
            if (list1 != null && list1.size() > 0) {
                destinationPort = (UnLocation) list1.get(0);
            }
        }
        List newTradingPartnerTempList = customerDAO.findAccountNo1(bookingFcl.getSSLine());
        TradingPartnerTemp newTradingPartnerTemp = new TradingPartnerTemp();
        if (newTradingPartnerTempList.size() > 0) {
            newTradingPartnerTemp = (TradingPartnerTemp) newTradingPartnerTempList.get(0);
        }
        if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(originTerminal) && CommonFunctions.isNotNull(destinationPort)) {
            fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(originTerminal, destinationPort, newTradingPartnerTemp);
        }

        if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
            FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);

            bookingFcl.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
            if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                bookingFcl.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
            }
        } else if (rampcity != null) {
            if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(rampcity) && CommonFunctions.isNotNull(destinationPort)) {
                fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(rampcity, destinationPort, newTradingPartnerTemp);
            }
            if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
                FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                bookingFcl.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                    bookingFcl.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                }
            } else {
                if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(portOfloading) && CommonFunctions.isNotNull(destinationPort)) {
                    fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(portOfloading, destinationPort, newTradingPartnerTemp);
                }
                if (null != newTradingPartnerTemp && !CommonUtils.isEmpty(fclOrgDestMiscData)) {
                    FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                    bookingFcl.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                    if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                        bookingFcl.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                    }
                } else {
                    if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(originTerminal) && CommonFunctions.isNotNull(portOfDischarge)) {
                        fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(originTerminal, portOfDischarge, newTradingPartnerTemp);
                    }
                    if (null != newTradingPartnerTemp && !CommonUtils.isEmpty(fclOrgDestMiscData)) {
                        FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                        bookingFcl.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                        if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                            bookingFcl.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                        }
                    } else {
                        if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(originTerminal) && CommonFunctions.isNotNull(portOfDischarge)) {
                            fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(rampcity, portOfDischarge, newTradingPartnerTemp);
                        }
                        if (null != newTradingPartnerTemp && !CommonUtils.isEmpty(fclOrgDestMiscData)) {
                            FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                            bookingFcl.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                            if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                                bookingFcl.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                            }
                        } else {
                            if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(portOfloading) && CommonFunctions.isNotNull(portOfDischarge)) {
                                fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(portOfloading, portOfDischarge, newTradingPartnerTemp);
                            }
                            if (null != newTradingPartnerTemp && !CommonUtils.isNotEmpty(fclOrgDestMiscData)) {
                                FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                                bookingFcl.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                                if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                                    bookingFcl.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return bookingFcl;
    }
    public List addChassisCharge(List fclRates, BookingFcl bookingFcl, String vendorName, String vendorAcct, String cost, String sell) throws Exception {

        int no = 0;
        for (int a = 0; a < fclRates.size(); a++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(a);
            BookingfclUnits prevBookingfclUnits = null;
            if (a != 0) {
                prevBookingfclUnits = (BookingfclUnits) fclRates.get(a - 1);
                if (null != bookingfclUnits && null != prevBookingfclUnits && !bookingfclUnits.getUnitType().equals(prevBookingfclUnits.getUnitType())) {
                    if (prevBookingfclUnits.getNumbers() == null) {
                        prevBookingfclUnits.setNumbers("1");
}
                    if (prevBookingfclUnits.getNumbers() != null) {
                        no = no + Integer.parseInt(prevBookingfclUnits.getNumbers());
                    }
                }
            }
        }
        if (fclRates.size() > 0) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(fclRates.size() - 1);
            if (bookingfclUnits.getNumbers() != null) {
                no = no + Integer.parseInt(bookingfclUnits.getNumbers());
            }
        }
        List chargesList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        boolean flag = false;
        String insuranceComment = "";
        List tempList = new ArrayList(fclRates);
        String[] unitType = new String[fclRates.size()];
        String[] number = new String[fclRates.size()];
        Map<String, String> commentMap = new HashMap<String, String>();
        for (Iterator iter = tempList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("CHASFEE")) {
                if (null != bookingfclUnits.getUnitType() && CommonUtils.isNotEmpty(bookingfclUnits.getComment())) {
                    commentMap.put(bookingfclUnits.getUnitType().getCodedesc(), bookingfclUnits.getComment());
                }
                flag = true;
                fclRates.remove(bookingfclUnits);
            }
        }

        Double totalCharges = 0.00;

        int i = 0;
        for (Iterator iter = fclRates.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getUnitType() != null) {
                unitType[i] = bookingfclUnits.getUnitType().getCodedesc();
                number[i] = bookingfclUnits.getNumbers();
                i++;
            }

        }
        String unitLast = "";
        String numberLast = "";
        int temp = 0;
        String prevUnitValue = "";
        String unitValue = "";
        fclRates = orderExpandList(fclRates);
        for (int j = 0; j < fclRates.size(); j++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(j);
            if (null == bookingfclUnits.getSpecialEquipmentUnit()) {
                bookingfclUnits.setSpecialEquipmentUnit("");
            }
            if (null == bookingfclUnits.getStandardCharge()) {
                bookingfclUnits.setStandardCharge("");
            }
            if (null != bookingfclUnits.getUnitType()) {
                unitValue = bookingfclUnits.getUnitType().getId() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge();
            }
            BookingfclUnits bookingfclUnitsPrev = null;
            if (j != 0) {
                bookingfclUnitsPrev = (BookingfclUnits) fclRates.get(j - 1);
                if (null == bookingfclUnitsPrev.getSpecialEquipmentUnit()) {
                    bookingfclUnitsPrev.setSpecialEquipmentUnit("");
                }
                if (null == bookingfclUnitsPrev.getStandardCharge()) {
                    bookingfclUnitsPrev.setStandardCharge("");
                }
                if (null != bookingfclUnitsPrev.getUnitType()) {
                    prevUnitValue = bookingfclUnitsPrev.getUnitType().getId() + "-" + bookingfclUnitsPrev.getSpecialEquipmentUnit() + "-" + bookingfclUnitsPrev.getStandardCharge();
                }
                if (bookingfclUnitsPrev.getUnitType() != null && bookingfclUnits.getUnitType() != null && !prevUnitValue.equals(unitValue)) {
                    BookingfclUnits newBookingfclUnits = new BookingfclUnits();
                    newBookingfclUnits.setCostType("Flat Rate Per Container");
                    newBookingfclUnits.setChgCode("CHASSIS FEE");
                    newBookingfclUnits.setChargeCodeDesc("CHASFEE");
                    if (number[j] == null) {
                        number[j] = "1";
                    }
                    int num = Integer.parseInt(number[j]);
                    if (bookingfclUnitsPrev.getAmount() != null && !"new".equalsIgnoreCase(bookingfclUnitsPrev.getNewFlag())) {
                        totalCharges += bookingfclUnitsPrev.getAmount();
                    }
                    if (bookingfclUnitsPrev.getMarkUp() != null) {
                        totalCharges += bookingfclUnitsPrev.getMarkUp();
                    }
                    if (bookingfclUnitsPrev.getAdjustment() != null) {
                        totalCharges += bookingfclUnitsPrev.getAdjustment();
                    }

                    
                    newBookingfclUnits.setMarkUp(Double.parseDouble(dbUtil.removeComma(sell)));
                    newBookingfclUnits.setAmount(Double.parseDouble(dbUtil.removeComma(cost)));
                    newBookingfclUnits.setSpecialEquipment(bookingfclUnitsPrev.getSpecialEquipment());
                    newBookingfclUnits.setSpecialEquipmentUnit(bookingfclUnitsPrev.getSpecialEquipmentUnit());
                    newBookingfclUnits.setStandardCharge(bookingfclUnitsPrev.getStandardCharge());
                    newBookingfclUnits.setSellRate(newBookingfclUnits.getAmount() + newBookingfclUnits.getMarkUp());
                    newBookingfclUnits.setEfectiveDate(new Date());
                    newBookingfclUnits.setNumbers(String.valueOf(num));
                    newBookingfclUnits.setCurrency("USD");
                    newBookingfclUnits.setPrint("off");
                    newBookingfclUnits.setManualCharges("M");
                    newBookingfclUnits.setNewFlag("new");
                    if (null != bookingfclUnitsPrev.getUnitType()) {
                        newBookingfclUnits.setComment(commentMap.get(bookingfclUnitsPrev.getUnitType().getCodedesc()));
                    }

                    newBookingfclUnits.setBookingNumber(bookingFcl.getBookingNumber());
                    newBookingfclUnits.setUnitType(bookingfclUnitsPrev.getUnitType());
                    newBookingfclUnits.setUnitName(bookingfclUnitsPrev.getUnitType().getCodedesc());
                    newBookingfclUnits.setAccountName(vendorName);
                    newBookingfclUnits.setAccountNo(vendorAcct);
                    chargesList.add(newBookingfclUnits);
                    totalCharges = 0.00;
                    temp++;
                } else {
                    if (!"new".equalsIgnoreCase(bookingfclUnitsPrev.getNewFlag())) {
                        totalCharges += bookingfclUnitsPrev.getAmount();
                    }
                    if (bookingfclUnitsPrev.getMarkUp() != null) {
                        totalCharges += bookingfclUnitsPrev.getMarkUp();
                    }
                    if (bookingfclUnitsPrev.getAdjustment() != null) {
                        totalCharges += bookingfclUnitsPrev.getAdjustment();
                    }
                }
            }
            chargesList.add(bookingfclUnits);
        }
        if (fclRates.size() > 0) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) fclRates.get(fclRates.size() - 1);
            BookingfclUnits newBookingfclUnits1 = new BookingfclUnits();
            newBookingfclUnits1.setCostType("Flat Rate Per Container");
            newBookingfclUnits1.setChgCode("CHASSIS FEE");
            if (numberLast == null || numberLast.equals("")) {
                numberLast = "1";
            }
            int num = Integer.parseInt(numberLast);
            if (bookingfclUnits.getAmount() != null && !"new".equalsIgnoreCase(bookingfclUnits.getNewFlag())) {
                totalCharges += bookingfclUnits.getAmount();
            }
            if (bookingfclUnits.getMarkUp() != null) {
                totalCharges += bookingfclUnits.getMarkUp();
            }
            if (bookingfclUnits.getAdjustment() != null) {
                totalCharges += bookingfclUnits.getAdjustment();
            }
            bookingFcl.setTotalCharges(totalCharges);
            // bookingFcl = calculateInsurance(bookingFcl, bookingFcl.getInsurancamt().toString());
            newBookingfclUnits1.setMarkUp(Double.parseDouble(dbUtil.removeComma(sell)));
            newBookingfclUnits1.setAmount(Double.parseDouble(dbUtil.removeComma(cost)));
            newBookingfclUnits1.setSpecialEquipment(bookingfclUnits.getSpecialEquipment());
            newBookingfclUnits1.setSpecialEquipmentUnit(bookingfclUnits.getSpecialEquipmentUnit());
            newBookingfclUnits1.setStandardCharge(bookingfclUnits.getStandardCharge());
            newBookingfclUnits1.setNumbers(String.valueOf(num));
            newBookingfclUnits1.setChargeCodeDesc("CHASFEE");
            newBookingfclUnits1.setSellRate(newBookingfclUnits1.getAmount() + newBookingfclUnits1.getMarkUp());
            newBookingfclUnits1.setEfectiveDate(new Date());
            newBookingfclUnits1.setCurrency("USD");
            newBookingfclUnits1.setPrint("off");
            newBookingfclUnits1.setManualCharges("M");
            newBookingfclUnits1.setNewFlag("new");
            newBookingfclUnits1.setAccountName(vendorName);
            newBookingfclUnits1.setAccountNo(vendorAcct);
            if (null != bookingfclUnits.getUnitType()) {
                newBookingfclUnits1.setComment(commentMap.get(bookingfclUnits.getUnitType().getCodedesc()));
            }
            newBookingfclUnits1.setUnitType(bookingfclUnits.getUnitType());
            if (null != bookingfclUnits && null != bookingfclUnits.getUnitType()) {
                newBookingfclUnits1.setUnitName(bookingfclUnits.getUnitType().getCodedesc());
            }
            newBookingfclUnits1.setBookingNumber(bookingFcl.getBookingNumber());
            if (!"A=20".equalsIgnoreCase(newBookingfclUnits1.getUnitName())) {
                
                   newBookingfclUnits1.setAmount(newBookingfclUnits1.getAmount());
                   newBookingfclUnits1.setMarkUp(newBookingfclUnits1.getMarkUp());
           
            }
            chargesList.add(newBookingfclUnits1);
        }
        return chargesList;


    }
       public List deleteChassisCharge(List fclRates, BookingFcl bookingFcl) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < newfclRates.size(); i++) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) newfclRates.get(i);
            if (bookingfclUnits.getChargeCodeDesc().equalsIgnoreCase("CHASFEE")) {
                newfclRates.remove(bookingfclUnits);
                bookingFclUnitsDAO.delete(bookingfclUnits);
            }
        }
        return newfclRates;
    }
}
