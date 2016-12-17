/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclBookingSegregation;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingSegregationDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclSegregationForm;
import com.logiware.thread.LclFileNumberThread;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mohanapriya
 */
public class LclSegregationAction extends LogiwareDispatchAction {

    public ActionForward displaySegDr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("amsHblId", request.getParameter("amsHblId"));
        request.setAttribute("amsHbl", request.getParameter("amsNo"));
        request.setAttribute("pcs", Integer.parseInt(request.getParameter("pieces")));
        request.setAttribute("segDestination", request.getParameter("finalDestination"));
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        return mapping.findForward("segregation");
    }

    public ActionForward createSegregationDr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        synchronized (LclBookingAction.class) {
            LclSegregationForm lclSegregationForm = (LclSegregationForm) form;
            LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
            LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
            LclBookingImportDAO lclBookingImportDAO = new LclBookingImportDAO();
            LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
            LclBookingPieceUnitDAO lclBookingPieceUnitDAO = new LclBookingPieceUnitDAO();
            LclFileNumber orgLclFileNumber = lclFileNumberDAO.findById(lclSegregationForm.getFileId());
            LclFileNumber segLclFileNumber = null;
            User loginUser = getCurrentUser(request);
            Date date = new Date();

            // creating new Lcl File Number
            LclFileNumberThread thread = new LclFileNumberThread();
            String fileNumber = thread.getFileNumber();
            Long newFileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNumber);
            if (newFileNumberId == 0) {
                segLclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
            } else {
                thread = new LclFileNumberThread();
                fileNumber = thread.getFileNumber();
                segLclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
            }
            if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
                lclFileNumberDAO.getSession().getTransaction().begin();
            }

            // setting LclBookingSegregation values
            LclBookingSegregation lclBookingSegregation = new LclBookingSegregation();
            lclBookingSegregation.setChildLclFileNumber(segLclFileNumber);
            lclBookingSegregation.setParentLclFileNumber(orgLclFileNumber);
            lclBookingSegregation.setEnteredBy(loginUser);
            lclBookingSegregation.setModifiedBy(loginUser);
            lclBookingSegregation.setEnteredDatetime(date);
            lclBookingSegregation.setModifiedDatetime(date);
            LclBookingImportAms orgLclBookingImportAms = new LclBookingImportAmsDAO().findById(lclSegregationForm.getAmsHblId());
            StringBuilder referenceNo = new StringBuilder();
            referenceNo.append(orgLclBookingImportAms.getScac()).append(",").append(orgLclBookingImportAms.getAmsNo()).append(",").append(orgLclBookingImportAms.getPieces());
            lclBookingSegregation.setReferenceNo(referenceNo.toString());
            new LclBookingSegregationDao().save(lclBookingSegregation);

            // inserting notes
            StringBuilder remarks = new StringBuilder();
            remarks.append("Segregation DR was ").append(" Created from the Original DR #");
            remarks.append(lclBookingSegregation.getParentLclFileNumber().getFileNumber());
            new LclRemarksDAO().insertLclRemarks(lclBookingSegregation.getChildLclFileNumber().getId(),
                    LclCommonConstant.REMARKS_DR_AUTO_NOTES, remarks.toString(), loginUser.getUserId());

            // updating the lclBookingImportAms values
            new LclBookingImportAmsDAO().updateSegregationFileNumber(lclSegregationForm.getAmsHblId(), segLclFileNumber.getId(), loginUser.getUserId());

            // copying the original BookingPiece values to new Segregation BookingPiece values
            LclBookingPiece orgLclBookingPiece = lclBookingPieceDAO.getByProperty("lclFileNumber.id", orgLclFileNumber.getId());
            LclBookingPiece segLclBookingPiece = (LclBookingPiece) BeanUtils.cloneBean(orgLclBookingPiece);
            segLclBookingPiece.setBookedPieceCount(lclSegregationForm.getPieces());
            segLclBookingPiece.setBookedVolumeMetric(lclSegregationForm.getCbm());
            segLclBookingPiece.setBookedVolumeImperial(lclSegregationForm.getCft());
            segLclBookingPiece.setBookedWeightMetric(lclSegregationForm.getKgs());
            segLclBookingPiece.setBookedWeightImperial(lclSegregationForm.getLbs());
            segLclBookingPiece.setLclFileNumber(segLclFileNumber);
            segLclBookingPiece.setModifiedBy(loginUser);
            segLclBookingPiece.setEnteredBy(loginUser);
            segLclBookingPiece.setModifiedDatetime(date);
            segLclBookingPiece.setEnteredDatetime(date);
            segLclBookingPiece.setLclBookingAcList(null);
            segLclBookingPiece.setLclBookingHazmatList(null);
            segLclBookingPiece.setLclBookingHsCodeList(null);
            segLclBookingPiece.setLclBookingPieceDetailList(null);
            segLclBookingPiece.setLclBookingPieceUnitList(null);
            segLclBookingPiece.setLclBookingPieceWhseList(null);
            lclBookingPieceDAO.save(segLclBookingPiece);

            //copying the other original BookingImport values to new Segregation BookingImport values
            LclBookingImport orgLclBookingImport = lclBookingImportDAO.getByProperty("lclFileNumber.id", orgLclFileNumber.getId());
            LclBookingImport segLclBookingImport = (LclBookingImport) BeanUtils.cloneBean(orgLclBookingImport);
            segLclBookingImport.setFileNumberId(segLclFileNumber.getId());
            segLclBookingImport.setLclFileNumber(segLclFileNumber);
            segLclBookingImport.setTransShipment(lclSegregationForm.getTransshipment());
            segLclBookingImport.setModifiedBy(loginUser);
            segLclBookingImport.setEnteredBy(loginUser);
            segLclBookingImport.setModifiedDatetime(date);
            segLclBookingImport.setEnteredDatetime(date);
            lclBookingImportDAO.save(segLclBookingImport);

            // copying the original Booking values to new Segregation Booking values
            LclBooking orgLclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", orgLclFileNumber.getId());
            lclBookingDAO.getCurrentSession().evict(orgLclBooking);
            LclBooking segLclBooking = (LclBooking) BeanUtils.cloneBean(orgLclBooking);
            segLclBooking.setFileNumberId(segLclFileNumber.getId());
            segLclBooking.setLclFileNumber(segLclFileNumber);
            segLclBooking.setFinalDestination(CommonUtils.isNotEmpty(lclSegregationForm.getSegDestId())
                    ? new UnLocationDAO().findById(lclSegregationForm.getSegDestId()) : orgLclBooking.getFinalDestination());
//            if (orgLclBooking.getRtAgentContact() == null) {
//                orgLclBooking.setAgentContact(new LclContact(null, "", "", date, date, loginUser, loginUser, orgLclFileNumber));
//            }
//            if (orgLclBooking.getRtAgentContact().getEnteredBy() == null) {
//                orgLclBooking.setRtAgentContact(new LclContact(null, "", "", date, date, loginUser, loginUser, orgLclFileNumber));
//            }
            if (orgLclBooking.getPooWhseContact().getEnteredBy() == null) {
                orgLclBooking.setPooWhseContact(new LclContact(null, "", "", date, date, loginUser, loginUser, orgLclFileNumber));
            }
            // segLclBooking.setAgentContact(new LclContact(null, "", "", date, date, loginUser, loginUser, segLclFileNumber));
            //  segLclBooking.setRtAgentContact(new LclContact(null, "", "", date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setFwdContact(new LclContact(null, "", "", date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setClientContact(new LclContact(null, "", orgLclBooking.getClientContact() != null
                    && CommonUtils.isNotEmpty(orgLclBooking.getClientContact().getCompanyName()) ? orgLclBooking.getClientContact().getCompanyName() : "",
                    date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setPooWhseContact(new LclContact(null, "", orgLclBooking.getPooWhseContact() != null
                    && CommonUtils.isNotEmpty(orgLclBooking.getPooWhseContact().getCompanyName()) ? orgLclBooking.getPooWhseContact().getCompanyName() : "",
                    date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setConsContact(new LclContact(null, "", orgLclBooking.getConsContact() != null
                    && CommonUtils.isNotEmpty(orgLclBooking.getConsContact().getCompanyName()) ? orgLclBooking.getConsContact().getCompanyName() : "",
                    date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setShipContact(new LclContact(null, "", orgLclBooking.getShipContact() != null
                    && CommonUtils.isNotEmpty(orgLclBooking.getShipContact().getCompanyName()) ? orgLclBooking.getShipContact().getCompanyName() : "",
                    date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setSupContact(new LclContact(null, "", orgLclBooking.getSupContact() != null
                    && CommonUtils.isNotEmpty(orgLclBooking.getSupContact().getCompanyName()) ? orgLclBooking.getSupContact().getCompanyName() : "",
                    date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setNotyContact(new LclContact(null, "", orgLclBooking.getNotyContact() != null
                    && CommonUtils.isNotEmpty(orgLclBooking.getNotyContact().getCompanyName()) ? orgLclBooking.getNotyContact().getCompanyName() : "",
                    date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setNotify2Contact(new LclContact(null, "", orgLclBooking.getNotify2Contact() != null
                    && CommonUtils.isNotEmpty(orgLclBooking.getNotify2Contact().getCompanyName()) ? orgLclBooking.getNotify2Contact().getCompanyName() : "",
                    date, date, loginUser, loginUser, segLclFileNumber));
//            segLclBooking.setThirdPartyContact(new LclContact(null, "", orgLclBooking.getThirdPartyContact() != null
//                    && CommonUtils.isNotEmpty(orgLclBooking.getThirdPartyContact().getCompanyName()) ? orgLclBooking.getThirdPartyContact().getCompanyName() : "",
//                    date, date, loginUser, loginUser, segLclFileNumber));
            segLclBooking.setModifiedBy(loginUser);
            segLclBooking.setEnteredBy(loginUser);
            segLclBooking.setModifiedDatetime(date);
            segLclBooking.setEnteredDatetime(date);
            segLclBooking.setBookingType(segLclBookingImport.getTransShipment() == true ? "T" : "I");
            lclBookingDAO.save(segLclBooking);

            if (segLclBooking.getSupAcct() != null && segLclBooking.getSupAcct().getAccountno() != null) {
                String newBrand = new TradingPartnerDAO().getBusinessUnit(segLclBooking.getSupAcct().getAccountno());
                new LclFileNumberDAO().updateEconoEculine(newBrand, String.valueOf(segLclBooking.getFileNumberId()));
            }

            //copying the other original BookingPad values to new Segregation BookingPad values
            LclBookingPad orgLclBookingPad = lclBookingPadDAO.getByProperty("lclFileNumber.id", orgLclFileNumber.getId());
            if (orgLclBookingPad != null) {
                LclBookingPad segLclBookingPad = (LclBookingPad) BeanUtils.cloneBean(orgLclBookingPad);
                if (null != orgLclBookingPad.getLclBookingAc() && !NumberUtils.isNotZero(orgLclBookingPad.getLclBookingAc().getId())) {
                    orgLclBookingPad.setLclBookingAc(null);
                }
                segLclBookingPad.setLclFileNumber(segLclFileNumber);
                segLclBookingPad.setLclBookingAc(null);
                segLclBookingPad.setModifiedBy(loginUser);
                segLclBookingPad.setEnteredBy(loginUser);
                segLclBookingPad.setModifiedDatetime(date);
                segLclBookingPad.setEnteredDatetime(date);
                lclBookingPadDAO.save(segLclBookingPad);
            }

            //copying the other original BookingPieceUnit values to new Segregation BookingPieceUnit values
            LclBookingPieceUnit orgLclBookingPieceUnit = lclBookingPieceUnitDAO.getByProperty("lclBookingPiece.id", orgLclBookingPiece.getId());
            LclBookingPieceUnit segLclBookingPieceUnit = (LclBookingPieceUnit) BeanUtils.cloneBean(orgLclBookingPieceUnit);
            segLclBookingPieceUnit.setLclBookingPiece(segLclBookingPiece);
            segLclBookingPieceUnit.setLclUnitSs(orgLclBookingPieceUnit.getLclUnitSs());
            segLclBookingPieceUnit.setModifiedBy(loginUser);
            segLclBookingPieceUnit.setEnteredBy(loginUser);
            segLclBookingPieceUnit.setModifiedDatetime(date);
            segLclBookingPieceUnit.setEnteredDatetime(date);
            lclBookingPieceUnitDAO.save(segLclBookingPieceUnit);
            lclFileNumberDAO.getSession().getTransaction().commit();
        }
        if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
            lclFileNumberDAO.getSession().getTransaction().begin();
        }
        return null;
    }
}
