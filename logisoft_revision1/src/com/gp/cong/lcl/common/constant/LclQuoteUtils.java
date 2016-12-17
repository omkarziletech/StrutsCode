/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.QuoteCommodityDetailsDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLQuoteForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author Mei
 */
public class LclQuoteUtils implements LclCommonConstant {

    public void saveLclContact(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, Date now, User loginUser) throws Exception {
        lclQuoteForm.getLclQuote().getClientContact().setLclFileNumber(lclFileNumber);
        this.setUserAndDateTime(lclQuoteForm.getLclQuote().getClientContact(), now, loginUser);
        lclQuoteForm.getLclQuote().getShipContact().setLclFileNumber(lclFileNumber);
        this.setUserAndDateTime(lclQuoteForm.getLclQuote().getShipContact(), now, loginUser);
        lclQuoteForm.getLclQuote().getConsContact().setLclFileNumber(lclFileNumber);
        this.setUserAndDateTime(lclQuoteForm.getLclQuote().getConsContact(), now, loginUser);
        lclQuoteForm.getLclQuote().getFwdContact().setLclFileNumber(lclFileNumber);
        this.setUserAndDateTime(lclQuoteForm.getLclQuote().getFwdContact(), now, loginUser);
        lclQuoteForm.getLclQuote().getNotyContact().setLclFileNumber(lclFileNumber);
        this.setUserAndDateTime(lclQuoteForm.getLclQuote().getNotyContact(), now, loginUser);
        lclQuoteForm.getLclQuote().getPooWhseContact().setLclFileNumber(lclFileNumber);
        this.setUserAndDateTime(lclQuoteForm.getLclQuote().getPooWhseContact(), now, loginUser);
    }

    public void setUserAndDateTime(LclContact lclContact, Date now, User loginUser) throws Exception {
        if (lclContact.getEnteredBy() == null) {
            lclContact.setEnteredBy(loginUser);
            lclContact.setEnteredDatetime(now);
        }
        lclContact.setModifiedBy(loginUser);
        lclContact.setModifiedDatetime(now);
    }

    public void setRemarks(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            User loginUser, Date todayDate, LclRemarksDAO lclRemarksDAO) throws Exception {
        //quoteStatus(lclQuoteForm, lclFileNumber, loginUser, lclRemarksDAO);
        //add Port Remarks
        addPortRemarks(lclQuoteForm, lclFileNumber, loginUser, todayDate, lclRemarksDAO);
        //OSD,Remarks Loading and External Comment
        saveOrUpdateRemarks(lclQuoteForm, lclFileNumber, loginUser, todayDate, lclRemarksDAO);
        //replicate Quote fileNumber
        addReplicateNote(lclQuoteForm, lclFileNumber, loginUser, todayDate, lclRemarksDAO);
    }

    public void quoteStatus(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, LclRemarksDAO lclRemarksDAO) throws Exception {
        LclRemarks lclRemarks = lclRemarksDAO.validateQuoteRemarks(lclFileNumber.getId());
        if (lclRemarks == null) {
            String remarks = "";
            if (CommonUtils.isNotEmpty(lclQuoteForm.getOriginalFileNo())) {
                remarks = "Quote Created - Copy from QT# " + lclQuoteForm.getOriginalFileNo();
                lclQuoteForm.setOriginalFileNo(null);
            } else {
                remarks = REMARKS_QUOTE_CREATED;
            }
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_QT_AUTO_NOTES, remarks, loginUser);
        }
    }

    public void addPortRemarks(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, Date todayDate, LclRemarksDAO lclRemarksDAO) throws Exception {
        addPortSpecialRemarks(lclQuoteForm, lclFileNumber, loginUser, todayDate, lclRemarksDAO);
        addPortGriRemarks(lclQuoteForm, lclFileNumber, loginUser, todayDate, lclRemarksDAO);
        addPortInternalRemarks(lclQuoteForm, lclFileNumber, loginUser, todayDate, lclRemarksDAO);
    }

    public void saveOrUpdateRemarks(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, Date todayDate, LclRemarksDAO lclRemarksDAO) throws Exception {
        addRemarks(lclQuoteForm, lclFileNumber, loginUser);
//        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getOsdRemarks(), REMARKS_TYPE_OSD,
//                lclFileNumber, loginUser, todayDate);//save OSD Remarks
        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getExternalComment(), REMARKS_TYPE_EXTERNAL_COMMENT,
                lclFileNumber, loginUser, todayDate);//save External Comments
//        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getRemarksForLoading(), REMARKS_TYPE_LOADING_REMARKS,
//                lclFileNumber, loginUser, todayDate);//save remarks Loading
    }

    public void addPortSpecialRemarks(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, Date todayDate, LclRemarksDAO lclRemarksDAO) throws Exception {
        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getSpecialRemarks(), REMARKS_TYPE_SPECIAL_REMARKS,
                lclFileNumber, loginUser, todayDate);//add port Special Remarks
        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getSpecialRemarksPod(), SPECIAL_REMARKS_FD,
                lclFileNumber, loginUser, todayDate);//add port Special Remarks
    }

    public void addPortGriRemarks(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, Date todayDate, LclRemarksDAO lclRemarksDAO) throws Exception {
        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getPortGriRemarks(), REMARKS_TYPE_GRI,
                lclFileNumber, loginUser, todayDate);//add port GRI Remarks
        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getPortGriRemarksPod(), GRI_REMARKS_FD,
                lclFileNumber, loginUser, todayDate);//add port POD GRI Remarks
    }

    public void addPortInternalRemarks(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, Date todayDate, LclRemarksDAO lclRemarksDAO) throws Exception {
        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getInternalRemarks(), REMARKS_TYPE_INTERNAL_REMARKS,
                lclFileNumber, loginUser, todayDate);//add port Internal Remarks
        lclRemarksDAO.saveOrUpdateRemarks(lclQuoteForm.getInternalRemarksPod(), INTERNAL_REMARKS_FD,
                lclFileNumber, loginUser, todayDate);//add port POD Internal Remarks
    }

    public void addReplicateNote(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, Date todayDate, LclRemarksDAO lclRemarksDAO) throws Exception {
        if (CommonUtils.isNotEmpty(lclQuoteForm.getReplicateFileNumber())) {
            String remarks = "Quote Copied from " + lclQuoteForm.getReplicateFileNumber();
            lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
            lclQuoteForm.setReplicateFileNumber(null);
        }
    }

    public void getRemarks(LCLQuoteForm lclQuoteForm, HttpServletRequest request, Long fileId, String remarkTypes[],
            LclRemarksDAO lclRemarksDAO) throws Exception {
        List remarks = lclRemarksDAO.getRemarksByTypes(fileId, remarkTypes);
        Map<String, String> remarksMap = new HashMap<String, String>();
        for (Object row : remarks) {
            String remarksStr = "";
            Object[] col = (Object[]) row;
            if (remarksMap.containsKey(col[1].toString())) {
                remarksStr = remarksMap.get(col[1].toString()) + " , " + col[0].toString();
            } else {
                remarksStr = col[0].toString();
            }
            remarksMap.put(col[1].toString(), remarksStr);

        }
        for (Map.Entry<String, String> entry : remarksMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("Priority View")) {
                request.setAttribute("lclRemarksPriority", entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("AutoRates")) {
                request.setAttribute("highVolumeMessage", entry.getValue());
            }

            if (entry.getKey().equalsIgnoreCase("Special Remarks")) {
                lclQuoteForm.setSpecialRemarks(entry.getValue());
            }

            if (entry.getKey().equalsIgnoreCase("Special Remarks Pod")) {
                lclQuoteForm.setSpecialRemarksPod(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Internal Remarks")) {
                lclQuoteForm.setInternalRemarks(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Internal Remarks Pod")) {
                lclQuoteForm.setInternalRemarksPod(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("G")) {
                lclQuoteForm.setPortGriRemarks(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Gri Remarks Pod")) {
                lclQuoteForm.setPortGriRemarksPod(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Loading Remarks")) {
                lclQuoteForm.setRemarksForLoading(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("OSD")) {
                lclQuoteForm.setOsdRemarks(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("E")) {
                lclQuoteForm.setExternalComment(entry.getValue());
            }

        }
    }

    public void setRequestValues(LCLQuoteForm lclQuoteForm, LclQuote lclQuote, LclUtils lclUtils,
            List<LclQuotePiece> lclQuotePieceList, HttpServletRequest request) throws Exception {
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        setPolPodValues(lclQuote, request);//set Pol and Pod Values
        request.setAttribute("lclHotCodeList", new LclQuoteHotCodeDAO().getHotCodeList(lclQuote.getFileNumberId()));
        request.setAttribute("lcl3PList", lcl3pRefNoDAO.get3PRefList(lclQuote.getFileNumberId(), ""));//3rdParty List
        request.setAttribute("lclQuoteHsCodeList", new LclQuoteHsCodeDAO().getHsCodeList(lclQuote.getFileNumberId()));// hsCodeList
        request.setAttribute("aesCount", lcl3pRefNoDAO.isValidateAes(lclQuote.getFileNumberId()));
        exportQuoteUtils.setPoaandCreditStatusValues(lclQuoteForm, lclQuote, lclQuoteForm.getModuleName(), request);//set POA and Credit Status for all Vendors
        PortsDAO portsDAO = new PortsDAO();
        String engmet = portsDAO.getPortValue(PORTS_ENGMET, lclQuote.getFinalDestination().getUnLocationCode());
        request.setAttribute("engmet", engmet);
        new LclInbondsDAO().inbondDetails(lclQuote.getFileNumberId(), request);
        setSportRateValues(lclQuote, engmet, request);
        String[] remarkTypes = new String[]{REMARK_TYPE_MANUAL, REMARKS_QT_MANUAL_NOTES};
        request.setAttribute("manualNotesFlag", lclRemarksDAO.isRemarks(lclQuote.getLclFileNumber().getId(), remarkTypes));
        if (LCL_IMPORT.equalsIgnoreCase(lclQuoteForm.getModuleName())) {
            new ImportQuoteUtils().setRequestValues(lclQuoteForm, lclQuote, lclQuotePieceList, request);
        } else {
            lclQuoteForm.setWhsCode((null != lclQuote.getPooWhseContact() && null != lclQuote.getPooWhseContact().getWarehouse())
                    ? lclQuote.getPooWhseContact().getWarehouse().getWarehouseNo() : "");
            LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();
            LclBookingExport lclBookingExport = lclBookingExportDAO.getByProperty("lclFileNumber.id", lclQuote.getFileNumberId());
            if (lclBookingExport == null) {
                lclBookingExport = lclBookingExportDAO.save(lclBookingExport, lclQuote.getLclFileNumber(), lclQuote.getEnteredBy());
            }
            if (null != lclBookingExport.getStorageDatetime()) {
                lclQuoteForm.setStorageDate(DateUtils.formatDate(lclBookingExport.getStorageDatetime(), "dd-MMM-yyyy hh:mm:ss a"));
            }
            request.setAttribute("lclBookingExport", lclBookingExport);
        }
    }

    public void setSportRateValues(LclQuote lclQuote, String engmet, HttpServletRequest request) {
        request.setAttribute("ofspotrate", lclQuote.getSpotRate());//set ofrate flag in commodity Tab display Ofrate
        if (null != engmet && !"".equalsIgnoreCase(engmet) && lclQuote.getSpotRate()) {
            if (engmet.equalsIgnoreCase("E")) {
                request.setAttribute("spotratelabel", (lclQuote.getSpotWmRate() == null ? 0 : lclQuote.getSpotWmRate())
                        + "/CFT" + "," + (lclQuote.getSpotRateMeasure() == null ? 0 : lclQuote.getSpotRateMeasure()) + "/100 LBS");
            } else {
                request.setAttribute("spotratelabel", (lclQuote.getSpotWmRate() == null ? 0 : lclQuote.getSpotWmRate())
                        + "/CBM" + "," + (lclQuote.getSpotRateMeasure() == null ? 0 : lclQuote.getSpotRateMeasure()) + "/1000 KGS");
            }
        }
    }

    public List<LclQuotePiece> setCommodityList(Long fileId, HttpServletRequest request) throws Exception {
        List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileId);
        for (LclQuotePiece lbp : lclQuotePiecesList) {
            // lbp.getLclQuoteAcList();
            lbp.setLclQuoteHazmatList(new LclQuoteHazmatDAO().findByFileAndCommodityList(fileId, lbp.getId()));
            lbp.setLclQuoteAcList(new LclQuoteAcDAO().findByFileAndCommodityList(fileId, lbp.getId()));
            lbp.setLclQuotePieceWhseList(new LclQuotePieceWhseDAO().findByProperty("lclQuotePiece.id", lbp.getId()));
        }
        request.setAttribute("lclCommodityList", lclQuotePiecesList);
        return lclQuotePiecesList;
    }

    public void saveCommodity(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, LclSession lclSession, Date todayDate) throws Exception {
        List QuotecommodityList = null != lclSession.getQuoteCommodityList() ? lclSession.getQuoteCommodityList() : new ArrayList();
        for (Object obj : QuotecommodityList) {
            LclQuotePiece lqp = (LclQuotePiece) obj;
            if (CommonUtils.isNotEmpty(lclQuoteForm.getCopyFnVal()) && lclQuoteForm.getCopyFnVal().equalsIgnoreCase("Y")) {
                LclQuotePiece lbpNew = new LclQuotePiece();
                List<LclQuotePieceDetail> detailList = lqp.getLclQuotePieceDetailList();
                PropertyUtils.copyProperties(lbpNew, lqp);
                lbpNew.setLclFileNumber(lclFileNumber);
                lbpNew.setEnteredBy(loginUser);
                lbpNew.setModifiedBy(loginUser);
                lbpNew.setEnteredDatetime(todayDate);
                lbpNew.setModifiedDatetime(todayDate);
                lbpNew.setId(null);
                lbpNew.setLclQuotePieceUnitList(null);
                lbpNew.setLclQuoteHazmatList(null);
                lbpNew.setLclQuotePieceWhseList(null);
                lbpNew.setLclQuotePieceDetailList(null);
                new LclQuotePieceDAO().save(lbpNew);
                if (CommonUtils.isNotEmpty(detailList)) {
                    for (Object detailObj : detailList) {
                        LclQuotePieceDetail lbpd = (LclQuotePieceDetail) detailObj;
                        LclQuotePieceDetail lbpdNew = new LclQuotePieceDetail();
                        PropertyUtils.copyProperties(lbpdNew, lbpd);
                        lbpdNew.setQuotePiece(lbpNew);
                        lbpdNew.setId(null);
                        new QuoteCommodityDetailsDAO().save(lbpdNew);
                    }
                }
            } else {
                lqp.setLclFileNumber(lclFileNumber);
                List<LclQuotePieceDetail> detailList = lqp.getLclQuotePieceDetailList();
                lqp.setLclQuotePieceDetailList(null);
                lqp.setEnteredBy(loginUser);
                lqp.setModifiedBy(loginUser);
                lqp.setEnteredDatetime(todayDate);
                lqp.setModifiedDatetime(todayDate);
                if (lqp != null) {
                    new LclQuotePieceDAO().save(lqp);
                }
                if (CommonUtils.isNotEmpty(detailList)) {
                    for (int i = 0; i < detailList.size(); i++) {
                        LclQuotePieceDetail detail = (LclQuotePieceDetail) detailList.get(i);
                        detail.setQuotePiece(lqp);
                        new QuoteCommodityDetailsDAO().save(detail);
                    }
                }
            }
        }
    }

    public void setPolPodValues(LclQuote lclQuote, HttpServletRequest request) throws Exception {
        if (lclQuote.getPortOfLoading() != null) {
            StringBuilder sb = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationName()) && null != lclQuote.getPortOfLoading().getStateId()
                    && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getStateId().getCode()) && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationCode())) {
                sb.append(lclQuote.getPortOfLoading().getUnLocationName()).append("/").append(lclQuote.getPortOfLoading().getStateId().getCode()).append("(").append(lclQuote.getPortOfLoading().getUnLocationCode()).append(")");
            } else if (CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationCode())
                    && null != lclQuote.getPortOfLoading().getCountryId() && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationCode())) {
                sb.append(lclQuote.getPortOfLoading().getUnLocationName()).append("/").append(lclQuote.getPortOfLoading().getCountryId().getCodedesc()).append("(").append(lclQuote.getPortOfLoading().getUnLocationCode()).append(")");
            } else if (CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationCode())) {
                sb.append(lclQuote.getPortOfLoading().getUnLocationName()).append("(").append(lclQuote.getPortOfLoading().getUnLocationCode()).append(")");
            }
            request.setAttribute("pol", sb.toString());
            request.setAttribute("polCode", lclQuote.getPortOfLoading().getUnLocationCode());
            request.setAttribute("polUnlocationcode", lclQuote.getPortOfLoading().getUnLocationCode());
            request.setAttribute("portOfLoadingId", lclQuote.getPortOfLoading().getId());
        }
        if (lclQuote.getPortOfDestination() != null) {
            StringBuilder sb = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationName()) && null != lclQuote.getPortOfDestination().getStateId()
                    && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getStateId().getCode()) && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationCode())) {
                sb.append(lclQuote.getPortOfDestination().getUnLocationName()).append("/").append(lclQuote.getPortOfDestination().getStateId().getCode()).append("(").append(lclQuote.getPortOfDestination().getUnLocationCode()).append(")");
            } else if (CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationCode())
                    && null != lclQuote.getPortOfDestination().getCountryId() && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationCode())) {
                sb.append(lclQuote.getPortOfDestination().getUnLocationName()).append("/").append(lclQuote.getPortOfDestination().getCountryId().getCodedesc()).append("(").append(lclQuote.getPortOfDestination().getUnLocationCode()).append(")");
            } else if (CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationCode())) {
                sb.append(lclQuote.getPortOfDestination().getUnLocationName()).append("(").append(lclQuote.getPortOfDestination().getUnLocationCode()).append(")");
            }
            request.setAttribute("pod", sb.toString());
            request.setAttribute("podUnlocationcode", lclQuote.getPortOfDestination().getUnLocationCode());
            request.setAttribute("podCode", lclQuote.getPortOfDestination().getUnLocationCode());
            request.setAttribute("portOfDestinationId", lclQuote.getPortOfDestination().getId());
        }
    }

    public String setDeleteChargeTriggerValues(LclQuoteAc lclQuoteAc) throws Exception {
        StringBuilder chargeRemarks = new StringBuilder();
        chargeRemarks.append("DELETED -> ");
        if (lclQuoteAc != null && lclQuoteAc.getArglMapping() != null && lclQuoteAc.getArglMapping().getChargeCode() != null) {
            chargeRemarks.append(" Code -> ").append(lclQuoteAc.getArglMapping().getChargeCode());
        }
        if (lclQuoteAc != null && lclQuoteAc.getArAmount() != null && !"0.00".equalsIgnoreCase(lclQuoteAc.getArAmount().toString())) {
            chargeRemarks.append(" Charge Amount -> ").append(lclQuoteAc.getArAmount());
        }
        if (lclQuoteAc != null && lclQuoteAc.getApAmount() != null && !"0.00".equalsIgnoreCase(lclQuoteAc.getApAmount().toString())) {
            chargeRemarks.append(" Cost Amount -> ").append(lclQuoteAc.getApAmount());
        }
        return chargeRemarks.toString();
    }

    public LclContact getContactForBooking(LclContact bookingContact, User thisUser, LclFileNumber lclFileNumber, String remarksType) throws Exception {
        LclContact newContact = new LCLContactDAO().getContact(lclFileNumber.getId(), remarksType);
        if (newContact == null) {
            newContact = new LclContact(null, "", new Date(), new Date(), thisUser, thisUser, lclFileNumber);
        }
        if (null != bookingContact) {
            newContact.setCompanyName(bookingContact.getCompanyName());
            newContact.setContactName(bookingContact.getContactName());
            newContact.setEmail1(bookingContact.getEmail1());
            newContact.setEmail2(bookingContact.getEmail2());
            newContact.setCountry(bookingContact.getCountry());
            newContact.setFax1(bookingContact.getFax1());
            newContact.setAddress(bookingContact.getAddress());
            newContact.setCity(bookingContact.getCity());
            newContact.setPhone1(bookingContact.getPhone1());
            newContact.setPhone2(bookingContact.getPhone1());
            newContact.setZip(bookingContact.getZip());
            newContact.setRemarks(remarksType);
            newContact.setModifiedBy(thisUser);
            newContact.setEnteredBy(thisUser);
            newContact.setModifiedDatetime(new Date());
            newContact.setEnteredDatetime(new Date());
        }
        return newContact;
    }

    public LclContact setContactDataForQuote(LclContact quoteContact, LclContact existingContact, User loginUser, LclFileNumber lclFileNumber, String remarksType) throws Exception {
        quoteContact.setAddress(null != existingContact.getAddress() ? existingContact.getAddress().toUpperCase() : "");
        quoteContact.setCity(null != existingContact.getCity() ? existingContact.getCity().toUpperCase() : "");
        quoteContact.setState(null != existingContact.getState() ? existingContact.getState().toUpperCase() : "");
        quoteContact.setPhone1(null != existingContact.getPhone1() ? existingContact.getPhone1().toUpperCase() : "");
        quoteContact.setFax1(null != existingContact.getFax1() ? existingContact.getFax1().toUpperCase() : "");
        quoteContact.setZip(null != existingContact.getZip() ? existingContact.getZip().toUpperCase() : "");
        quoteContact.setEmail1(existingContact.getEmail1());
        quoteContact.setLclFileNumber(lclFileNumber);
        quoteContact.setRemarks(remarksType);
        quoteContact.setEnteredBy(loginUser);
        quoteContact.setModifiedBy(loginUser);
        quoteContact.setModifiedDatetime(new Date());
        quoteContact.setEnteredDatetime(new Date());
        return quoteContact;
    }

    public void addRemarks(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User user) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), REMARKS_TYPE_OSD);
        //save OSD Remarks
        if (lclRemarks == null && CommonUtils.isNotEmpty(lclQuoteForm.getOsdRemarks())) {
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_QT_AUTO_NOTES, "INSERTED-> OSD -> " + lclQuoteForm.getOsdRemarks().toUpperCase(), user);
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_TYPE_OSD, lclQuoteForm.getOsdRemarks().toUpperCase(), user);
        } else if (lclRemarks != null && !lclQuoteForm.getOsdRemarks().equalsIgnoreCase(lclRemarks.getRemarks())) {
            String remarks = "UPDATED-> OSD -> " + lclRemarks.getRemarks().toUpperCase() + " to " + lclQuoteForm.getOsdRemarks().toUpperCase();
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_QT_AUTO_NOTES, remarks, user);
            lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_OSD, lclQuoteForm.getOsdRemarks().toUpperCase(), user);
        }
        //save remarks Loading
        lclRemarks = lclRemarksDAO.getLclRemarksByType(lclFileNumber.getId().toString(), REMARKS_TYPE_LOADING_REMARKS);
        if (lclRemarks == null && CommonUtils.isNotEmpty(lclQuoteForm.getRemarksForLoading())) {
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_QT_AUTO_NOTES, "INSERTED-> Remarks Loading -> " + lclQuoteForm.getRemarksForLoading().toUpperCase(), user);
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_TYPE_LOADING_REMARKS, lclQuoteForm.getRemarksForLoading().toUpperCase(), user);
        } else if (lclRemarks != null && !lclQuoteForm.getRemarksForLoading().equalsIgnoreCase(lclRemarks.getRemarks())) {
            String remarks = "UPDATED-> Remarks Loading -> " + lclRemarks.getRemarks().toUpperCase() + " to " + lclQuoteForm.getRemarksForLoading().toUpperCase();
            lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_QT_AUTO_NOTES, remarks, user);
            lclRemarksDAO.insertLclRemarks(lclRemarks, lclFileNumber, REMARKS_TYPE_LOADING_REMARKS, lclQuoteForm.getRemarksForLoading().toUpperCase(), user);
        }
    }
}
