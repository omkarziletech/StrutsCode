/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.job;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.bc.fcl.BookingDwrBC;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Shaji.S
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class webToolsToLogiware implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(webToolsToLogiware.class);
    private static final BookingFclDAO dao = new BookingFclDAO();
    private Transaction transaction = null;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            log.info("Fetching Fcl Exports from WebTools Fcl Exports on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = new JobDAO().findByClassName(webToolsToLogiware.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            new JobDAO().getCurrentSession().update(job);
            transaction.commit();
            log.info("Fetching Fcl Exports from WebTools Fcl Exports on " + new Date());
        } catch (Exception e) {
            log.info("Fetching Fcl Exports from WebTools Fcl Exports Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("Fetching Fcl Exports from WebTools Fcl Exports Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = new JobDAO().findByClassName(webToolsToLogiware.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("Fetching Fcl Exports from WebTools Fcl Exports Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("Fetching Fcl Exports from WebTools Fcl Exports Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }

    public Double getDoubleValue(Object targetobj) {
        return ((null != targetobj && !targetobj.toString().isEmpty()) ? Double.parseDouble(targetobj.toString()) : 0.0);
    }

    public void run() throws Exception {
        try {
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            List bookingList = new BookingFclDAO().getWebToolsBooking();
            if (bookingList != null) {
                for (Object o : bookingList) {
                    Object[] bobj = (Object[]) o;
                    String bFileNo = null != bobj[0] ? bobj[0].toString() : "";
                    String bEmail = null != bobj[1] ? bobj[1].toString() : "";
                    String bConName = null != bobj[2] ? bobj[2].toString() : "";
                    String bConNum = null != bobj[3] ? bobj[3].toString() : "";
                    String bWareHouHrs = null != bobj[4] ? bobj[4].toString() : "";
                    String bWareHouAdd = null != bobj[5] ? bobj[5].toString() : "";
                    Date bCargoDate = (Date) bobj[6];
                    String bWareHouName = null != bobj[7] ? bobj[7].toString() : "";
                    String bNotes = null != bobj[8] ? bobj[8].toString() : "";
                    Object quoteObj = new BookingFclDAO().getWebToolsQuote(bFileNo);
                    Object[] qobj = (Object[]) quoteObj;
                    Date qDate = (Date) qobj[0];
                    String qBookSSL = null != qobj[6] ? qobj[6].toString() : "";
                    String qSslAccNo = null != qobj[18] ? qobj[18].toString() : "";
                    String qInland = null != qobj[28] ? qobj[28].toString() : "";
                    String qCommCode = null != qobj[29] ? qobj[29].toString() : "";
                    String qNvoMove = null != qobj[30] ? qobj[30].toString() : "";
                    Integer commodity = 0;
                    Date qBookingDate = (Date) qobj[31];
                    String qQuoteNo = null != qobj[32] ? qobj[32].toString() : "";
                    if (qNvoMove.equalsIgnoreCase("DTD")) {
                        qNvoMove = "DOOR TO DOOR";
                    } else if (qNvoMove.equalsIgnoreCase("DTP")) {
                        qNvoMove = "DOOR TO PORT";
                    } else if (qNvoMove.equalsIgnoreCase("DTR")) {
                        qNvoMove = "DOOR TO RAIL";
                    } else if (qNvoMove.equalsIgnoreCase("PTD")) {
                        qNvoMove = "PORT TO DOOR";
                    } else if (qNvoMove.equalsIgnoreCase("PTP")) {
                        qNvoMove = "PORT TO PORT";
                    } else if (qNvoMove.equalsIgnoreCase("PTR")) {
                        qNvoMove = "PORT TO RAIL";
                    } else if (qNvoMove.equalsIgnoreCase("RTD")) {
                        qNvoMove = "RAIL TO DOOR";
                    } else if (qNvoMove.equalsIgnoreCase("RTP")) {
                        qNvoMove = "RAIL TO PORT";
                    } else if (qNvoMove.equalsIgnoreCase("RTR")) {
                        qNvoMove = "RAIL TO RAIL";
                    }
                    Quotation quote = new Quotation();
                    quote.setSpecialequipment("N");
                    quote.setLocaldryage("N");
                    quote.setAmount(0.00);
                    quote.setIntermodel("N");
                    quote.setAmount1(0.00);
                    quote.setCustomertoprovideSed("N");
                    quote.setDeductFfcomm("N");
                    quote.setOutofgage("N");
                    quote.setRoutedAgentCheck("no");
                    quote.setBaht(0.00);
                    quote.setBdt(0.00);
                    quote.setCyp(0.00);
                    quote.setEur(0.00);
                    quote.setHkd(0.00);
                    quote.setLkr(0.00);
                    quote.setNt(0.00);
                    quote.setPrs(0.00);
                    quote.setRmb(0.00);
                    quote.setWon(0.00);
                    quote.setYen(0.00);
                    quote.setCarrierPrint("off");
                    quote.setDefaultAgent("Y");
                    quote.setInsuranceCharge(0.00);
                    quote.setNewClient("off");
                    quote.setDrayageMarkUp(0.00);
                    quote.setIntermodalMarkUp(0.00);
                    quote.setInsuranceMarkUp(0.00);
                    quote.setBreakBulk("N");
                    quote.setRatesNonRates("R");
                    quote.setDocCharge("N");
                    quote.setClientConsigneeCheck("off");
                    quote.setImportantDisclosures("off");
                    quote.setDocsInquiries("off");
                    quote.setDocumentAmount(0.00);
                    UnLocationDAO unlocationDao = new UnLocationDAO();
                    String dest = null != qobj[4] ? qobj[4].toString() : "";
                    String[] destination = dest.split("/");
                    quote.setRemarks(unlocationDao.getDestinationTempRemarks(destination[0]));
                    quote.setFclGRIRemarks("");
                    quote.setFclTempRemarks("");
                    quote.setOnCarriageRemarks("");
                    quote.setPrintPortRemarks("");
                    quote.setPrintRemarks("");
                    quote.setRatesRemarks("");
                    quote.setRegionRemarks("");
                    quote.setQuoteBy("System");
                    quote.setUpdateBy("System");
                    quote.setBookedBy("System");
                    quote.setQuoteDate(qDate);
                    quote.setOrigin_terminal(null != qobj[1] ? qobj[1].toString() : "");
                    quote.setDestination_port(null != qobj[2] ? qobj[2].toString() : "");
                    String agent = new PortsDAO().getPortsForAgentInfoWithDefault2(qobj[2].toString());
                    String agentNo = new PortsDAO().getPortsForAgentInfoWithDefault1(qobj[2].toString());
                    quote.setAgent(agent);
                    quote.setAgentNo(agentNo);
                    quote.setPlor(null != qobj[3] ? qobj[3].toString() : "");
                    quote.setFinaldestination(null != qobj[4] ? qobj[4].toString() : "");
                    quote.setContactname(null != qobj[5] ? qobj[5].toString() : "");
                    quote.setCarrier(qBookSSL);
                    quote.setPhone(null != qobj[7] ? qobj[7].toString() : "");
                    quote.setFax(null != qobj[8] ? qobj[8].toString() : "");
                    quote.setEmail1(null != qobj[9] ? qobj[9].toString() : "");
                    quote.setComment1(null != qobj[10] ? qobj[10].toString() : "");
                    quote.setZip(null != qobj[11] ? qobj[11].toString() : "");
                    quote.setTypeofMove(qNvoMove);
                    List comList = new GenericCodeDAO().findForGenericCode(qCommCode);
                    if (comList != null && comList.size() > 0) {
                        GenericCode genericObject = (GenericCode) comList.get(0);
                        commodity = genericObject.getId();
                        quote.setCommcode(genericObject);
                        quote.setDescription(genericObject.getCodedesc());
                    }
                    quote.setClientname(null != qobj[12] ? qobj[12].toString() : "");
                    quote.setClientnumber(null != qobj[13] ? qobj[13].toString() : "");
                    quote.setGoodsdesc(null != qobj[14] ? qobj[14].toString() : "");
                    quote.setHazmat(null != qobj[15] ? qobj[15].toString() : "");
                    quote.setInsurance(null != qobj[16] ? qobj[16].toString() : "");
                    Double costofgoods = getDoubleValue(qobj[17]);
                    quote.setCostofgoods(costofgoods);
                    quote.setInsurancamt(null != qobj[16] && qobj[16].toString().equalsIgnoreCase("Y") ? 0.80 : 0.00);
                    quote.setSsline(qSslAccNo);
                    quote.setSslname(qBookSSL);
                    quote.setNoOfDays(null != qobj[19] ? qobj[19].toString() : "");
                    quote.setIssuingTerminal(null != qobj[20] ? qobj[20].toString() : "");
                    quote.setFileNo(bFileNo);
                    quote.setCarrierContact(null != qobj[21] ? qobj[21].toString() : "");
                    quote.setCarrierPhone(null != qobj[22] ? qobj[22].toString() : "");
                    quote.setCarrierFax(null != qobj[23] ? qobj[23].toString() : "");
                    quote.setCarrierEmail(null != qobj[24] ? qobj[24].toString() : "");
                    quote.setDoorOrigin(null != qobj[25] ? qobj[25].toString() : "");
                    quote.setDoorDestination(null != qobj[26] ? qobj[26].toString() : "");
                    quote.setFileType(null != qobj[27] ? qobj[27].toString() : "");
                    quote.setInland(qInland);
                    quote.setBrand(null != qobj[33] ? qobj[33].toString() : "Ecu Worldwide");
                    BookingFcl booking = new BookingFcl();
                    String clientType = "";
                    CustAddress custAddress = new CustAddressDAO().findByAccountNoAndPrime(qobj[13].toString());
                    String accType = new BookingFclDAO().getClientType(Integer.parseInt(qobj[34].toString()));
                    String poa = new BookingDwrBC().setPoa(qobj[13].toString());
                    poa = !poa.isEmpty() && poa.equalsIgnoreCase("Y") ? "Yes" : "No";
                    if (accType.equalsIgnoreCase("S")) {
                        clientType = "Shipper";
                        booking.setShipper(qobj[12].toString());
                        booking.setShipNo(qobj[13].toString());
                        if (null != custAddress) {
                            booking.setAddressforShipper(custAddress.getAddress1());
                            booking.setShipperCity(custAddress.getCity1());
                            booking.setShipperState(custAddress.getState());
                            booking.setShipperZip(custAddress.getZip());
                            booking.setShipperEmail(custAddress.getEmail1());
                            booking.setShipperFax(custAddress.getFax());
                            booking.setShipperPhone(custAddress.getPhone());
                            booking.setShippercheck("on");
                            booking.setShipperPoa(poa);
                            if (custAddress.getCuntry() != null) {
                                booking.setShipperCountry(custAddress.getCuntry().getCodedesc());
                            }
                        }
                    } else if (accType.equalsIgnoreCase("C")) {
                        clientType = "Consignee";
                        booking.setConsignee(qobj[12].toString());
                        booking.setConsNo(qobj[13].toString());
                        if (null != custAddress) {
                            booking.setAddressforConsingee(custAddress.getAddress1());
                            booking.setConsigneeCity(custAddress.getCity1());
                            booking.setConsigneeState(custAddress.getState());
                            booking.setConsigneeZip(custAddress.getZip());
                            booking.setConsingeeEmail(custAddress.getEmail1());
                            booking.setConsigneeFax(custAddress.getFax());
                            booking.setConsingeePhone(custAddress.getPhone());
                            booking.setConsigneecheck("on");
                            booking.setConsigneePoa(poa);
                            if (custAddress.getCuntry() != null) {
                                booking.setConsigneeCountry(custAddress.getCuntry().getCodedesc());
                            }
                        }
                    } else {
                        clientType = "Vendor";
                        booking.setForward(qobj[12].toString());
                        booking.setForwNo(qobj[13].toString());
                        if (null != custAddress) {
                            booking.setAddressforForwarder(custAddress.getAddress1());
                            booking.setForwarderCity(custAddress.getCity1());
                            booking.setForwarderState(custAddress.getState());
                            booking.setForwarderZip(custAddress.getZip());
                            booking.setForwarderEmail(custAddress.getEmail1());
                            booking.setForwarderFax(custAddress.getFax());
                            booking.setForwarderPhone(custAddress.getPhone());
                            booking.setForwardercheck("on");
                            booking.setForwarderPoa(poa);
                            if (custAddress.getCuntry() != null) {
                                booking.setForwarderCountry(custAddress.getCuntry().getCodedesc());
                            }
                        }
                    }
                    quote.setClienttype(clientType);
                    new BookingFclDAO().update(quote);
                    booking.setBrand(null != qobj[33] ? qobj[33].toString() : "Ecu Worldwide");
                    booking.setBookingComplete("N");
                    booking.setSpecialequipment("N");
                    booking.setOutofgage("N");
                    booking.setLocaldryage("N");
                    booking.setAmount(0.00);
                    booking.setIntermodel("N");
                    booking.setAmount1(0.00);
                    booking.setBaht(0.00);
                    booking.setBdt(0.00);
                    booking.setCyp(0.00);
                    booking.setEur(0.00);
                    booking.setHkd(0.00);
                    booking.setLkr(0.00);
                    booking.setNt(0.00);
                    booking.setPrs(0.00);
                    booking.setRmb(0.00);
                    booking.setWon(0.00);
                    booking.setYen(0.00);
                    booking.setAlternateAgent("Y");
                    booking.setCustomertoprovideSED("N");
                    booking.setDeductFFcomm("N");
                    booking.setDrayMarkUp(0.00);
                    booking.setInterMarkUp(0.00);
                    booking.setInsureMarkUp(0.00);
                    booking.setRatesNonRates("R");
                    booking.setBreakBulk("N");
                    booking.setBlFlag("off");
                    booking.setDocCharge("N");
                    booking.setCarrierPrint("off");
                    booking.setSpotRate("N");
                    booking.setRoutedAgentCheck("no");
                    booking.setDirectConsignmntCheck("off");
                    booking.setPrepaidCollect("P");
                    booking.setBilltoCode("F");
                    booking.setImportFlag(null != qobj[27] ? qobj[27].toString() : "");
                    booking.setUpdateBy("System");
                    booking.setBookedBy("System");
                    booking.setUsername("System");
                    booking.setAgent(agent);
                    booking.setAgentNo(agentNo);
                    booking.setMoveType(qNvoMove);
                    if (qInland.equalsIgnoreCase("Y")) {
                        booking.setPositioningDate(bCargoDate);
                        booking.setLoadRemarks(bNotes + " , Warehouse Hours: " + bWareHouHrs);
                        booking.setSpottAddrTpCheck("on");
                        booking.setPickUpRemarks("");
                    } else {
                        booking.setEarliestPickUpDate(bCargoDate);
                        booking.setPickUpRemarks(bNotes);
                        booking.setLoadRemarks("");
                    }
                    booking.setBookingDate(qBookingDate);
                    booking.setPortofDischarge(null != qobj[2] ? qobj[2].toString() : "");
                    booking.setPortofOrgin(null != qobj[3] ? qobj[3].toString() : "");
                    booking.setOriginTerminal(null != qobj[1] ? qobj[1].toString() : "");
                    booking.setDestination(null != qobj[4] ? qobj[4].toString() : "");
                    booking.setGoodsDescription(null != qobj[14] ? qobj[14].toString() : "");
                    booking.setRemarks(null != qobj[10] ? qobj[10].toString() : "");
                    booking.setSSLine(null != qobj[18] ? qobj[18].toString() : "");
                    booking.setHazmat(null != qobj[15] ? qobj[15].toString() : "");
                    booking.setInsurance(null != qobj[16] ? qobj[16].toString() : "");
                    Double costofgood = getDoubleValue(qobj[17]);
                    booking.setCostofgoods(costofgood);
                    booking.setInsurancamt(null != qobj[16] && qobj[16].toString().equalsIgnoreCase("Y") ? 0.80 : 0.00);
                    booking.setSslname(null != qobj[6] ? qobj[6].toString() + "//" + qobj[18].toString() : "");
                    booking.setComcode(null != qobj[29] ? qobj[29].toString() : "");
                    booking.setFileNo(bFileNo);
                    booking.setIssuingTerminal(null != qobj[20] ? qobj[20].toString() : "");
                    booking.setNoOfDays(null != qobj[19] ? qobj[19].toString() : "");
                    booking.setZip(null != qobj[11] ? qobj[11].toString() : "");
                    booking.setRatesRemarks("");
                    booking.setDestRemarks(unlocationDao.getDestinationTempRemarks(destination[0]));
                    booking.setOnCarriageRemarks("");
                    booking.setReturnRemarks("");
                    booking.setDoorDestination(null != qobj[26] ? qobj[26].toString() : "");
                    booking.setDoorOrigin(null != qobj[25] ? qobj[25].toString() : "");
                    booking.setInland(qInland);
                    booking.setFileType(null != qobj[27] ? qobj[27].toString() : "");
                    booking.setBookingContact(null != qobj[9] ? qobj[9].toString() : "");
                    booking.setLoadcontact(bConName + ", " + bEmail);
                    booking.setLoadphone(bConNum);
                    booking.setAddressForExpPositioning(bWareHouAdd);
                    booking.setCargoReadyDate(bCargoDate);
                    booking.setSpottingAccountName(bWareHouName);
                    new BookingFclDAO().update(booking);
                    Quotation quotation = new BookingFclDAO().findQuoteByFileNo(bFileNo);
                    BookingFcl bookingFcl = new BookingFclDAO().findBookingByFileNo(bFileNo);
                    String ratesRemarks = "";
                    List ratesList = new BookingFclDAO().getWebToolsRates(qQuoteNo);
                    for (Object ratesObj : ratesList) {
                        Object[] rObj = (Object[]) ratesObj;
                        if (ratesRemarks == "") {
                            ratesRemarks = null != rObj[7] ? rObj[7].toString() : "";
                        }
                        BookingfclUnits rates = new BookingfclUnits();
                        Charges charge = new Charges();
                        rates.setBookingNumber(bookingFcl.getBookingId().toString());
                        rates.setRates(0.00);
                        rates.setMinimum(0.00);
                        rates.setCtc(0.00);
                        rates.setQouteId(quotation.getQuoteId());
                        rates.setCommcode(commodity);
                        rates.setCostType("PER CONTAINER SIZE");
                        rates.setPrint("off");
                        rates.setCurrency("USD");
                        rates.setBuyRate(0.00);
                        rates.setAdjustment(0.00);
                        rates.setOutOfGauge("N");
                        rates.setStandardCharge("Y");
                        rates.setStandardChk("off");
                        GenericCode genericCode = new GenericCodeDAO().findById(Integer.parseInt(rObj[0].toString()));
                        rates.setUnitType(genericCode);
                        rates.setNumbers(null != rObj[1] ? rObj[1].toString() : "");
                        rates.setChgCode(null != rObj[2] ? rObj[2].toString() : "");
                        Double amount = getDoubleValue(rObj[3]);
                        Double markup = getDoubleValue(rObj[4]);
                        Double sellrate = getDoubleValue(rObj[6]);
                        rates.setChargeCodeDesc(null != rObj[5] ? rObj[5].toString() : "");
                        if (null != rObj[2] && rObj[2].toString().equalsIgnoreCase("INLAND")) {
                            rates.setManualCharges("M");
                            rates.setNewFlag("new");
                            rates.setAmount(amount);
                            rates.setMarkUp(sellrate);
                            rates.setProfit(amount + sellrate);
                            rates.setSellRate(amount + sellrate);
                            charge.setAmount(amount);
                            charge.setMarkUp(sellrate);
                            charge.setChargeFlag("M");
                        } else {
                            rates.setAmount(amount);
                            rates.setMarkUp(markup);
                            rates.setProfit(sellrate);
                            rates.setSellRate(sellrate);
                            charge.setAmount(amount);
                            charge.setMarkUp(markup);
                        }
                        rates.setAccountNo(null != rObj[9] ? rObj[9].toString() : "");
                        rates.setAccountName(null != rObj[8] ? rObj[8].toString() : "");
                        rates.setComment(null != rObj[10] ? rObj[10].toString() : null);
                        new BookingFclDAO().update(rates);
                        charge.setQouteId(quotation.getQuoteId());
                        charge.setCommcode(commodity);
                        charge.setCostType("PER CONTAINER SIZE");
                        charge.setPrint("off");
                        charge.setRetail(0.00);
                        charge.setFtf(0.00);
                        charge.setCtc(0.00);
                        charge.setMinimum(0.00);
                        charge.setCurrecny("USD");
                        charge.setFutureRate(0.00);
                        charge.setAdjestment(0.00);
                        charge.setStandardCharge("Y");
                        charge.setSpotRateChk("off");
                        charge.setStandardChk("off");
                        charge.setUnitType(genericCode.getId().toString());
                        charge.setNumber(null != rObj[1] ? rObj[1].toString() : "");
                        charge.setChgCode(null != rObj[2] ? rObj[2].toString() : "");
                        charge.setChargeCodeDesc(null != rObj[5] ? rObj[5].toString() : "");
                        charge.setAccountNo(null != rObj[9] ? rObj[9].toString() : "");
                        charge.setAccountName(null != rObj[8] ? rObj[8].toString() : "");
                        charge.setComment(null != rObj[10] ? rObj[10].toString() : null);
                        new BookingFclDAO().update(charge);

                    }
                    quotation.setRatesRemarks(ratesRemarks);
                    quotation.setQuoteNo(quotation.getQuoteId().toString());
                    quotation.setBookingNo(bookingFcl.getBookingId());
                    bookingFcl.setBookingNumber(bookingFcl.getBookingId().toString());
                    bookingFcl.setQuoteNo(quotation.getQuoteId().toString());
                    bookingFcl.setRatesRemarks(ratesRemarks);
                    new BookingFclDAO().update(bookingFcl);
                    new BookingFclDAO().update(quotation);
                    List hazmatValues = new BookingFclDAO().getWebToolsHazmat(qQuoteNo);
                    for (int i = 1; i <= 2; i++) {
                        if (hazmatValues != null) {
                            for (Object hazardous : hazmatValues) {
                                Object[] hObj = (Object[]) hazardous;
                                HazmatMaterial hazmat = new HazmatMaterial();
                                if (i == 1) {
                                    hazmat.setDocTypeCode("Quote");
                                    hazmat.setBolId(quotation.getQuoteId());
                                } else {
                                    hazmat.setDocTypeCode("Booking");
                                    hazmat.setBolId(bookingFcl.getBookingId());
                                }
                                hazmat.setDocTypeId(quotation.getQuoteId().toString());
                                hazmat.setDate(quotation.getQuoteDate());
                                hazmat.setUnNumber(null != hObj[0] ? hObj[0].toString() : "");
                                hazmat.setPropShipingNumber(null != hObj[1] ? hObj[1].toString() : "");
                                hazmat.setEmerreprsNum(null != hObj[2] ? hObj[2].toString() : "");
                                hazmat.setTechnicalName(null != hObj[3] ? hObj[3].toString() : "");
                                hazmat.setImoClssCode(null != hObj[4] ? hObj[4].toString() : "");
                                hazmat.setImoSubsidiaryClassCode(null != hObj[5] ? hObj[5].toString() : "");
                                hazmat.setImoSecondarySubClass(null != hObj[6] ? hObj[6].toString() : "");
                                hazmat.setPackingGroupCode(null != hObj[7] ? hObj[7].toString() : "");
                                hazmat.setExceptedQuantity(null != hObj[8] ? hObj[8].toString() : "");
                                hazmat.setMarinePollutant(null != hObj[9] ? hObj[9].toString() : "");
                                hazmat.setLimitedQuantity(null != hObj[10] ? hObj[10].toString() : "");
                                hazmat.setInnerPackingPieces(null != hObj[11] ? hObj[11].toString() : "");
                                hazmat.setOuterPackingPieces(null != hObj[12] ? hObj[12].toString() : "");
                                hazmat.setInnerPackComposition(null != hObj[13] ? hObj[13].toString() : "");
                                hazmat.setOuterPackComposition(null != hObj[14] ? hObj[14].toString() : "");
                                hazmat.setInnerPackagingType(null != hObj[15] ? hObj[15].toString() : "");
                                hazmat.setOuterPackagingType(null != hObj[16] ? hObj[16].toString() : "");
                                Double netweight = getDoubleValue(hObj[17]);
                                Double grossweight = getDoubleValue(hObj[18]);
                                Double volume = getDoubleValue(hObj[19]);
                                hazmat.setNetWeight(netweight);
                                hazmat.setGrossWeight(grossweight);
                                hazmat.setVolume(volume);
                                hazmat.setReportableQuantity(null != hObj[20] ? hObj[20].toString() : "");
                                hazmat.setInhalationHazard(null != hObj[21] ? hObj[21].toString() : "");
                                hazmat.setResidue(null != hObj[22] ? hObj[22].toString() : "");
                                hazmat.setEmsCode(null != hObj[23] ? hObj[23].toString() : "");
                                hazmat.setContactName(null != hObj[24] ? hObj[24].toString() : "");
                                hazmat.setNetWeightUMO(null != hObj[25] ? hObj[25].toString() : "");
                                hazmat.setFlashPointUMO(null != hObj[26] ? hObj[26].toString() : "");
                                hazmat.setFreeFormat(null != hObj[27] ? hObj[27].toString() : "");
                                hazmat.setLine1(null != hObj[28] ? hObj[28].toString() : "");
                                hazmat.setLine2(null != hObj[29] ? hObj[29].toString() : "");
                                hazmat.setLine3(null != hObj[30] ? hObj[30].toString() : "");
                                hazmat.setLine4(null != hObj[31] ? hObj[31].toString() : "");
                                hazmat.setLine5(null != hObj[32] ? hObj[32].toString() : "");
                                hazmat.setLine6(null != hObj[33] ? hObj[33].toString() : "");
                                hazmat.setLine7(null != hObj[34] ? hObj[34].toString() : "");
                                Double totalnetweight = getDoubleValue(hObj[35]);
                                hazmat.setTotalNetWeight(totalnetweight);
                                new BookingFclDAO().update(hazmat);
                            }
                        }
                    }
                    new BookingFclDAO().updateStatus(bFileNo);
                    transaction.commit();
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                }
            }
        } catch (HibernateException e) {
            transaction.rollback();
            throw e;
        }
    }
}
