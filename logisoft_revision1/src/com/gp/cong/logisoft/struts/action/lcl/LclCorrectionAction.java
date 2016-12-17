/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cvst.logisoft.struts.form.lcl.LCLCorrectionForm;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.logiware.accounting.dao.LclManifestDAO;
import com.gp.cong.lcl.dwr.LclPrintUtil;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.lcl.report.LclCorrectionDebitCreditPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.beans.LCLCorrectionChargeBean;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import java.math.BigDecimal;
import org.apache.struts.util.MessageResources;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Owner
 */
public class LclCorrectionAction extends LogiwareDispatchAction implements ConstantsInterface, LclCommonConstant, LclReportConstants, ReportConstants {

    private static final String LCL_BL_VIEW_CORRECTION = "lclBlViewCorrection";
    private static final String LCL_BL_ADD_CORRECTION = "lclBlAddCorrection";
    private static final String LCL_BL_ADD_CORRECTION_POPUP = "lclBlAddCorrectionPopup";
    private static final String LCL_BL_SEARCH_CORRECTION = "lclBlSearchCorrection";
    private static final String LCL_BL_VIEW_SEARCH_CORRECTION = "lclBlViewSearchCorrection";
    private static final String LCL_BL_VIEW_AUTHENTICATE_POPUP = "lclViewAuthenticatePopup";
    private static final String LCL_BL_VIEW_VOID_CORRECTION = "lclBlViewVoidCorrection";
    private static final String LCL_BL_CORRECTION_CHARGE_DESC = "correctionChargeDesc";

    public ActionForward viewLclBlCorrections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        String forwardName = LCL_BL_VIEW_CORRECTION;
        saveBookingValues(lclCorrectionForm, request);
        setViewCorrections(lclCorrectionForm, null, request);
        LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getHeaderId())) {
            LclSsHeader lclssheader = lclSsHeaderDAO.findById(Long.parseLong(lclCorrectionForm.getHeaderId()));
            request.setAttribute("auditOrClosedBy", lclssheader);
        }
        if (lclCorrectionForm.getScreenName() != null && lclCorrectionForm.getScreenName().equalsIgnoreCase("Search")) {
            viewSearchCorrection(mapping, form, request, response);
            forwardName = LCL_BL_VIEW_SEARCH_CORRECTION;
        }
        return mapping.findForward(forwardName);
    }

    public ActionForward addLclBlCorrections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        lclCorrectionForm.setCorrectionId(null);
        saveBookingValues(lclCorrectionForm, request);
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFileId())) {
            setCorrections(lclCorrectionForm, new LCLCorrectionDAO(), request);
        }
        return mapping.findForward(LCL_BL_ADD_CORRECTION);
    }

    private void saveBookingValues(LCLCorrectionForm lclCorrectionForm, HttpServletRequest request) throws Exception {
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports") && request.getParameter("updateBillToParty") != null
                && request.getParameter("updateBillToParty").toString().equalsIgnoreCase("Y")) {
            String cfsWarehouseNo = request.getParameter("cfsDevWarhsNo");
            if (CommonUtils.isNotEmpty(cfsWarehouseNo)) {
                String cfsVendor[] = new WarehouseDAO().getWarehouseAccountNo(cfsWarehouseNo);
                if (CommonUtils.isNotEmpty(cfsVendor)) {
                    lclCorrectionForm.setCfsDevAcctName(cfsVendor[0]);
                    lclCorrectionForm.setCfsDevAcctNo(cfsVendor[1]);
                }
            }
            Date d = new Date();
            User user = getCurrentUser(request);
            LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
            TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
            LclBooking lclBooking = lclBookingDAO.findById(lclCorrectionForm.getFileId());
            lclBookingDAO.getCurrentSession().evict(lclBooking);
            lclBooking.setBillToParty(lclCorrectionForm.getBillToParty());
            lclBooking.setBillingType(lclCorrectionForm.getBillingType());
            lclBooking.setModifiedDatetime(d);
            lclBooking.setModifiedBy(getCurrentUser(request));
            if (CommonUtils.isNotEmpty(lclCorrectionForm.getThirdPartyAcctNo())) {
                lclBooking.setThirdPartyAcct(tradingPartnerDAO.findById(lclCorrectionForm.getThirdPartyAcctNo()));
                //lclBooking.getThirdPartyContact().setCompanyName(lclCorrectionForm.getCustomerAcctName());
            } else if (lclCorrectionForm.getBillToParty().equalsIgnoreCase("C")) {
                lclBooking.setConsAcct(tradingPartnerDAO.findById(lclCorrectionForm.getCustomerAcctNo()));
                lclBooking.getConsContact().setCompanyName(lclCorrectionForm.getCustomerAcctName());
                lclBooking.setConsContact(new LclContact(null, "", lclCorrectionForm.getCustomerAcctName(),
                        d, d, user, user, lclBooking.getLclFileNumber()));
            } else if (lclCorrectionForm.getBillToParty().equalsIgnoreCase("N")) {
                lclBooking.setNotyAcct(tradingPartnerDAO.findById(lclCorrectionForm.getCustomerAcctNo()));
                lclBooking.getNotyContact().setCompanyName(lclCorrectionForm.getCustomerAcctName());
                lclBooking.setNotyContact(new LclContact(null, "", lclCorrectionForm.getCustomerAcctName(),
                        d, d, user, user, lclBooking.getLclFileNumber()));
            } else {
                lclBooking.setSupAcct(tradingPartnerDAO.findById(lclCorrectionForm.getCustomerAcctNo()));
                lclBooking.getSupContact().setCompanyName(lclCorrectionForm.getCustomerAcctName());
            }
            lclBookingDAO.update(lclBooking);
        }
    }

    public ActionForward viewLclBlVoidCorrections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
        request.setAttribute("lclVoidBlCorrectionList", lclCorrectionDAO.getAllVoidCorrectionsByFileId(lclCorrectionForm));
        return mapping.findForward(LCL_BL_VIEW_VOID_CORRECTION);
    }

    public ActionForward editLclBlCorrections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFileId())) {
            setCorrections(lclCorrectionForm, new LCLCorrectionDAO(), request);
        }
        return mapping.findForward(LCL_BL_ADD_CORRECTION);
    }

    public ActionForward deleteCorrectionCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getCorrectionChargeId())) {
            LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
            lclCorrectionChargeDAO.deleteCorrectionCharge(lclCorrectionForm.getCorrectionChargeId());
            setCorrections(lclCorrectionForm, new LCLCorrectionDAO(), request);
        }
        return mapping.findForward(LCL_BL_ADD_CORRECTION);
    }

    public ActionForward viewLclAddBlCorrections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        lclCorrectionForm.setViewMode("view");
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFileId())) {
            setCorrections(lclCorrectionForm, new LCLCorrectionDAO(), request);
        }
        return mapping.findForward(LCL_BL_ADD_CORRECTION);
    }

    public ActionForward deleteLclBlCorrections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        LclUtils lclUtils = new LclUtils();
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        lclCorrectionDAO.deleteCorrections(request.getParameter("correctionId"), getCurrentUser(request).getUserId());
        setViewCorrections(lclCorrectionForm, null, request);
        String concatenatedBlNo = "(" + lclCorrectionForm.getBlNo() + "-C-" + lclCorrectionForm.getNoticeNo() + ")";
        lclUtils.insertLCLCorrectionRemarks(REMARKS_TYPE_LCL_CORRECTIONS, lclCorrectionForm.getFileId(), concatenatedBlNo, "deleted", getCurrentUser(request));
        return mapping.findForward(LCL_BL_VIEW_CORRECTION);
    }

    public ActionForward editCorrectionCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        setBillToVendorList(request, lclCorrectionForm);
        lclCorrectionForm.setChargeStatus("edit");
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
        return mapping.findForward(LCL_BL_ADD_CORRECTION_POPUP);
    }

    private void setBillToVendorList(HttpServletRequest request, LCLCorrectionForm lclCorrectionForm) {
        com.gp.cvst.logisoft.util.DBUtil dbUtil = new com.gp.cvst.logisoft.util.DBUtil();
        boolean importFlag = false;
        if (lclCorrectionForm.getSelectedMenu() != null && lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            importFlag = true;
        }
        request.setAttribute("billToVendor", dbUtil.getBilltypeLcl(importFlag));
    }

    public ActionForward addCorrectionCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        setBillToVendorList(request, lclCorrectionForm);
        lclCorrectionForm.setChargeStatus("add");
        if (lclCorrectionForm.getBillToParty().equalsIgnoreCase("F")) {
            lclCorrectionForm.setBillToParty("Forwarder");
        } else if (lclCorrectionForm.getBillToParty().equalsIgnoreCase("S")) {
            lclCorrectionForm.setBillToParty("Shipper");
        } else if (lclCorrectionForm.getBillToParty().equalsIgnoreCase("T")) {
            lclCorrectionForm.setBillToParty("ThirdParty");
        } else if (lclCorrectionForm.getBillToParty().equalsIgnoreCase("A")) {
            lclCorrectionForm.setBillToParty("Agent");
        } else if (lclCorrectionForm.getBillToParty().equalsIgnoreCase("N")) {
            lclCorrectionForm.setBillToParty("Notify");
        } else if (lclCorrectionForm.getBillToParty().equalsIgnoreCase("C")) {
            lclCorrectionForm.setBillToParty("Consignee");
        }
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
        return mapping.findForward(LCL_BL_ADD_CORRECTION_POPUP);
    }

    public ActionForward saveLclCorrectionCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFileId())) {
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
            if (CommonUtils.isNotEmpty(lclCorrectionForm.getNewAmount())) {
                lclCorrectionForm.setNewAmount(String.valueOf(Math.abs(Double.parseDouble(
                        lclCorrectionForm.getNewAmount()))));
            }
            if (CommonUtils.isNotEmpty(lclCorrectionForm.getBillToParty())) {
                lclCorrectionForm.setBillToParty(lclCorrectionForm.getBillToParty().substring(0, 1).toUpperCase());
            }
            if (lclCorrectionForm.getButtonValue().equalsIgnoreCase("quickcn")) {
                lclCorrectionForm.setCorrectionType(lclCorrectionForm.getCorrectionTypeIdA());
            }
            lclCorrectionDAO.saveCorrection(lclCorrectionForm, getCurrentUser(request));//saveCorrection
            if (CommonUtils.isEmpty(lclCorrectionForm.getCorrectionId())) {
                lclCorrectionForm.setCorrectionId(lclCorrectionDAO.getIntegerDescByFileIdWithVoid(lclCorrectionForm.getFileId(), "id").longValue());
                insertCorrectionCharges(lclCorrectionForm, lclCorrectionChargeDAO, getCurrentUser(request));
            } else {
                // updateCorrections(lclCorrectionForm, lclCorrectionDAO, getCurrentUser(request));
                if (CommonUtils.isNotEmpty(lclCorrectionForm.getCorrectionChargeId())) {
                    lclCorrectionChargeDAO.updateCorrectionCharge(lclCorrectionForm.getCorrectionChargeId(), lclCorrectionForm.getNewAmount(),
                            lclCorrectionForm.getBillToParty(), getCurrentUser(request).getUserId());
                } else {
                    insertCorrectionCharges(lclCorrectionForm, lclCorrectionChargeDAO, getCurrentUser(request));
                }
            }
            setCorrections(lclCorrectionForm, lclCorrectionDAO, request);
        }
        return mapping.findForward(LCL_BL_ADD_CORRECTION);
    }

    public ActionForward saveLclCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        String oldBillToCode = "";
        String forwardName = LCL_BL_VIEW_CORRECTION;
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFileId()) && CommonUtils.isEmpty(lclCorrectionForm.getCorrectionId())) {
            oldBillToCode = new LCLBookingDAO().getOldBillToParty(lclCorrectionForm.getFileId());
            if (CommonUtils.isNotEmpty(lclCorrectionForm.getBillToCode()) && !lclCorrectionForm.getBillToCode().equalsIgnoreCase(oldBillToCode) && CommonUtils.isEmpty(lclCorrectionForm.getCorrectionId())) {
                lclCorrectionDAO.saveCorrection(lclCorrectionForm, getCurrentUser(request));//saveCorrection
                updateBillToCode(mapping, lclCorrectionForm, request, response);
            }
        } else {
            lclCorrectionDAO.saveCorrection(lclCorrectionForm, getCurrentUser(request));//saveCorrection
        }
        saveBookingValues(lclCorrectionForm, request);
        setViewCorrections(lclCorrectionForm, lclCorrectionDAO, request);
        if (lclCorrectionForm.getScreenName().equalsIgnoreCase("Search")) {
            viewSearchCorrection(mapping, form, request, response);
            forwardName = LCL_BL_VIEW_SEARCH_CORRECTION;
        }
        return mapping.findForward(forwardName);
    }

    private void insertCorrections(LCLCorrectionForm lclCorrectionForm, LCLCorrectionDAO lclCorrectionDAO, User user) throws Exception {
        lclCorrectionDAO.saveCorrection(lclCorrectionForm, user);
    }

    private void updateCorrections(LCLCorrectionForm lclCorrectionForm, LCLCorrectionDAO lclCorrectionDAO, User user) throws Exception {
        lclCorrectionDAO.updateCorrections(lclCorrectionForm.getCorrectionId(), lclCorrectionForm.getCorrectionType(), lclCorrectionForm.getCorrectionCode(),
                lclCorrectionForm.getCustomerAcctNo(), lclCorrectionForm.getComments(), user.getUserId(),
                lclCorrectionForm.getDebitMemoEmail(), lclCorrectionForm.getCreditMemoEmail());
    }

    private void insertCorrectionCharges(LCLCorrectionForm lclCorrectionForm, LCLCorrectionChargeDAO lclCorrectionChargeDAO, User user) throws Exception {
        lclCorrectionChargeDAO.insertCorrectionChargeWithBookingAC(lclCorrectionForm, user.getUserId());
    }

    private void setCorrections(LCLCorrectionForm lclCorrectionForm, LCLCorrectionDAO lclCorrectionDAO, HttpServletRequest request) throws Exception {
        DBUtil dbUtil = new DBUtil();
        LclUtils lclUtils = new LclUtils();
        request.setAttribute("correctionCodeList", dbUtil.getGenericCodeListWithDesc(52, "no", "yes", "Select Correction Code"));
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        String strCorrectionTypeIdA = genericCodeDAO.getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION, GENERIC_CODE_A_BL_CORRECTION, "id");
        String strCorrectionTypeIdY = genericCodeDAO.getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION, GENERIC_CODE_Y_BL_CORRECTION, "id");
        String strCorrectionTypeIdS = genericCodeDAO.getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION, GENERIC_CODE_S_BL_CORRECTION, "id");
        LclCorrection lclCorrection = null;
        LCLCorrectionNoticeBean lclCorrectionNoticeBean = lclCorrectionDAO.getAllCorrectionByFileId(lclCorrectionForm.getFileId().toString(),
                lclCorrectionForm.getCorrectionId(), lclCorrectionForm.getSelectedMenu());
        if (CommonUtils.isNotEmpty(strCorrectionTypeIdA)) {
            lclCorrectionForm.setCorrectionTypeIdA(Integer.valueOf(strCorrectionTypeIdA));
        }
        if (CommonUtils.isNotEmpty(strCorrectionTypeIdY)) {
            lclCorrectionForm.setCorrectionTypeIdY(Integer.valueOf(strCorrectionTypeIdY));
        }
        if (CommonUtils.isNotEmpty(strCorrectionTypeIdS)) {
            lclCorrectionForm.setCorrectionTypeIdS(Integer.valueOf(strCorrectionTypeIdS));
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getCorrectionId())) {
            lclCorrection = lclCorrectionDAO.findById(lclCorrectionForm.getCorrectionId());
            if (CommonUtils.isNotEmpty(lclCorrection.getBillToParty())) {
                lclCorrectionNoticeBean.setBillToCode(lclCorrection.getBillToParty());
                lclCorrectionNoticeBean.setBillingType(lclCorrection.getBillingType());
            }
            request.setAttribute("lclCorrection", lclCorrection);
        }
        if (lclCorrection == null && CommonUtils.isNotEmpty(lclCorrectionForm.getBillToCode())) {
            lclCorrectionNoticeBean.setBillToCode(lclCorrectionForm.getBillToCode());
            lclCorrectionNoticeBean.setBillingType(lclCorrectionForm.getBillingType());
        }
        lclCorrectionNoticeBean.setFileNo(lclCorrectionForm.getFileNo());
        Object lastApprovedCode = lclCorrectionDAO.getLastApprovedCodeByFileId(lclCorrectionForm.getFileId());
        if (lastApprovedCode != null) {
            changeBTAndBTP(lastApprovedCode.toString(), lclCorrectionNoticeBean);
        }
        setOriginalCustomer(lclCorrectionNoticeBean, lclCorrectionForm);
        setCorrectionCodeList(lclCorrectionNoticeBean, request, lclCorrectionForm);
        BigInteger pieceCount = null;
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
            if (lclCorrectionForm.getButtonValue().equalsIgnoreCase("quickcn")) {
                if (CommonUtils.isNotEmpty(strCorrectionTypeIdA)) {
                    lclCorrectionNoticeBean.setCorrectionType(Integer.valueOf(strCorrectionTypeIdA));
                }
                if (CommonUtils.isEmpty(lclCorrectionNoticeBean.getComments())) {
                    lclCorrectionNoticeBean.setComments("QUICK CN");
                }
            }
            if (CommonUtils.isEmpty(lclCorrectionNoticeBean.getCorrectionCode())) {
                String id = genericCodeDAO.getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION_CODE, GENERIC_CODE_A_CORRECTION_IMPORTS, "id");
                lclCorrectionNoticeBean.setCorrectionCode(Integer.parseInt(id));
            }
            Integer.parseInt(genericCodeDAO.getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_INVOICENO, GENERIC_CODE_INVOICENO, "Field1"));
            pieceCount = lclBookingPieceDAO.getPieceCountByFileId(lclCorrectionForm.getFileId());
            List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.getLclChargeCorrectionList(lclCorrectionForm.getFileId(), lclCorrectionForm.getSelectedMenu(), lclCorrectionForm.getViewMode());
            request.setAttribute("lclCorrectionChargesList", lclUtils.getFormattedCorrectionChargesImports(lclBookingAcList, lclCorrectionForm.getCorrectionId(),
                    lclCorrectionForm.getFileId(), pieceCount.intValue(), lclCorrectionNoticeBean, lclCorrectionForm.getViewMode(), lclCorrectionForm.getCurrentExitBillToCode()));
        } else {
            LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
            LclBLPieceDAO lclBlPieceDAO = new LclBLPieceDAO();
            pieceCount = lclBlPieceDAO.getPieceCountByFileId(lclCorrectionForm.getFileId());
            List<LclBlAc> lclBlAcList = lclBlAcDAO.getLclChargesByFileNumberAsc(lclCorrectionForm.getFileId());
//            request.setAttribute("lclCorrectionChargesList", lclUtils.getFormattedCorrectionCharges(lclBlAcList, lclCorrectionForm.getCorrectionId(),
//                    lclCorrectionForm.getFileId(), pieceCount.intValue(), lclCorrectionNoticeBean.getBillToParty(), lclCorrectionForm.getViewMode()));
        }
        request.setAttribute("lclCorrectionNoticeBean", lclCorrectionNoticeBean);
        lclCorrectionForm.setShipperNo(lclCorrectionNoticeBean.getShipperNo());
        lclCorrectionForm.setForwarderNo(lclCorrectionNoticeBean.getForwarderNo());
        lclCorrectionForm.setAgentNo(lclCorrectionNoticeBean.getAgentNo());
        lclCorrectionForm.setCustomerAcctNo(lclCorrectionNoticeBean.getCustomerAcctNo());
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
    }

    private void setViewCorrections(LCLCorrectionForm lclCorrectionForm, LCLCorrectionDAO lclCorrectionDAO, HttpServletRequest request) throws Exception {
        CustomerAccountingDAO customerAccountingDAO = new CustomerAccountingDAO();
        String customerAcctNo = null;
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
            customerAcctNo = lclBookingDAO.getCustomerByFileIdImports(lclCorrectionForm.getFileId().toString());
        } else {
            LCLBlDAO lclBlDAO = new LCLBlDAO();
            customerAcctNo = lclBlDAO.getCustomerByFileIdImports(lclCorrectionForm.getFileId().toString());
        }
        if (lclCorrectionDAO == null) {
            lclCorrectionDAO = new LCLCorrectionDAO();
        }
        Object lastApprovedCorrectionNo = lclCorrectionDAO.getLastApprovedFieldsByFileId(lclCorrectionForm.getFileId(), "correction_no");
        Object lastCorrectionNo = lclCorrectionDAO.getFieldDescByFileId(lclCorrectionForm.getFileId(), "correction_no");
        if (lastCorrectionNo != null) {
            lclCorrectionForm.setLastCorrectionNo(lastCorrectionNo.toString());
        }
        if (lastApprovedCorrectionNo != null) {
            lclCorrectionForm.setLastApprovedCorrectionNo(lastApprovedCorrectionNo.toString());
        }
        setVoidClassName(lclCorrectionDAO, lclCorrectionForm);
        lclCorrectionForm.setCorrectionCount(lclCorrectionDAO.getCorrectionCountByFileId(lclCorrectionForm.getFileId()));
        request.setAttribute("lclBlCorrectionList", lclCorrectionDAO.getAllAddedCorrectionsByFileId(lclCorrectionForm));
        lclCorrectionForm.setThirdPartyName(lclCorrectionForm.getThirdPartyName());
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Exports")) {
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            lclCorrectionForm.setCostAmount(lclCostChargeDAO.getTotalLclCostAmountByFileNumber(lclCorrectionForm.getFileId()));
        }
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
    }

    private void setVoidClassName(LCLCorrectionDAO lclCorrectionDAO, LCLCorrectionForm lclCorrectionForm) throws Exception {
        if (lclCorrectionDAO.getVoidedCorrectionCountByFileId(lclCorrectionForm.getFileId(), "1") == 0) {
            lclCorrectionForm.setVoidedClassName("button-style1");
        } else {
            lclCorrectionForm.setVoidedClassName("green-background");
        }
    }

    private void changeBTAndBTP(String lastApprovedCode, LCLCorrectionNoticeBean lclCorrectionNoticeBean) {
        if (lastApprovedCode.equalsIgnoreCase("B")) {
            lclCorrectionNoticeBean.setBillToParty("A");
            lclCorrectionNoticeBean.setBillingType("C");
        } else if (lastApprovedCode.equalsIgnoreCase("F") || lastApprovedCode.equalsIgnoreCase("H") || lastApprovedCode.equalsIgnoreCase("D")) {
            lclCorrectionNoticeBean.setBillToParty("F");
            lclCorrectionNoticeBean.setBillingType("P");
        } else if (lastApprovedCode.equalsIgnoreCase("G") || lastApprovedCode.equalsIgnoreCase("I") || lastApprovedCode.equalsIgnoreCase("C")) {
            lclCorrectionNoticeBean.setBillToParty("S");
            lclCorrectionNoticeBean.setBillingType("P");
        } else if (lastApprovedCode.equalsIgnoreCase("E")) {
            lclCorrectionNoticeBean.setBillToParty("T");
            lclCorrectionNoticeBean.setBillingType("P");
        }
    }

    private void setOriginalCustomer(LCLCorrectionNoticeBean lclCorrectionNoticeBean, LCLCorrectionForm lclCorrectionForm) {
        String partyNo = "";
        if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("F")) {
            lclCorrectionNoticeBean.setCustomerAcctNo(lclCorrectionNoticeBean.getForwarderNo());
            lclCorrectionNoticeBean.setCustomerLabel("FORWARDER");
            lclCorrectionNoticeBean.setCustomer(lclCorrectionNoticeBean.getForwarderName() + " " + lclCorrectionNoticeBean.getForwarderNo());
            partyNo = lclCorrectionNoticeBean.getForwarderNo();
        } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("S")) {
            lclCorrectionNoticeBean.setCustomerLabel("SHIPPER");
            lclCorrectionNoticeBean.setCustomerAcctNo(lclCorrectionNoticeBean.getShipperNo());
            lclCorrectionNoticeBean.setCustomer(lclCorrectionNoticeBean.getShipperName() + " " + lclCorrectionNoticeBean.getShipperNo());
            partyNo = lclCorrectionNoticeBean.getShipperNo();
        } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("T")) {
            lclCorrectionNoticeBean.setCustomerAcctNo(lclCorrectionNoticeBean.getThirdPartyNo());
            lclCorrectionNoticeBean.setCustomerLabel("THIRD PARTY");
            lclCorrectionNoticeBean.setCustomer(lclCorrectionNoticeBean.getThirdPartyName() + " " + lclCorrectionNoticeBean.getThirdPartyNo());
            partyNo = lclCorrectionNoticeBean.getThirdPartyNo();
        } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("A")) {
            lclCorrectionNoticeBean.setCustomerAcctNo(lclCorrectionNoticeBean.getAgentNo());
            lclCorrectionNoticeBean.setCustomerLabel("AGENT");
            lclCorrectionNoticeBean.setCustomer(lclCorrectionNoticeBean.getAgentName() + " " + lclCorrectionNoticeBean.getAgentNo());
            partyNo = lclCorrectionNoticeBean.getAgentNo();
        } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("C")) {
            lclCorrectionNoticeBean.setCustomerAcctNo(lclCorrectionNoticeBean.getConsigneeNo());
            lclCorrectionNoticeBean.setCustomerLabel("CONSIGNEE");
            lclCorrectionNoticeBean.setCustomer(lclCorrectionNoticeBean.getConsigneeName() + " " + lclCorrectionNoticeBean.getConsigneeNo());
            partyNo = lclCorrectionNoticeBean.getConsigneeNo();
        } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("N")) {
            lclCorrectionNoticeBean.setCustomerAcctNo(lclCorrectionNoticeBean.getNotifyNo());
            lclCorrectionNoticeBean.setCustomerLabel("NOTIFY");
            lclCorrectionNoticeBean.setCustomer(lclCorrectionNoticeBean.getNotifyName() + " " + lclCorrectionNoticeBean.getNotifyNo());
            partyNo = lclCorrectionNoticeBean.getNotifyNo();
        }
        if (lclCorrectionNoticeBean.getBillingType().equalsIgnoreCase("P")) {
            lclCorrectionNoticeBean.setBillingType("Prepaid");
            lclCorrectionNoticeBean.setBillingTypeLabel("P-Prepaid");
        } else if (lclCorrectionNoticeBean.getBillingType().equalsIgnoreCase("C")) {
            lclCorrectionNoticeBean.setBillingType("Collect");
            lclCorrectionNoticeBean.setBillingTypeLabel("C-Collect");
        } else {
            lclCorrectionNoticeBean.setBillingType("Both");
            lclCorrectionNoticeBean.setBillingTypeLabel("B-Both");
        }
        if (lclCorrectionNoticeBean.getCorrectionTypeValue() != null && lclCorrectionNoticeBean.getCorrectionTypeValue().equals("S")) {
            lclCorrectionForm.setPartyNo(lclCorrectionNoticeBean.getThirdPartyNo());
        } else {
            lclCorrectionForm.setPartyNo(partyNo);
        }
    }

    private void setCorrectionCodeList(LCLCorrectionNoticeBean lclCorrectionNoticeBean, HttpServletRequest request, LCLCorrectionForm lclCorrectionForm) throws Exception {
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List<LabelValueBean> selectList = new ArrayList<LabelValueBean>();
        List<GenericCode> correctionTypeList = genericCodeDAO.findByCodeNameByInOpreator(51, "'A'", "");
        if (CommonUtils.isEmpty(selectList)) {
            for (GenericCode genericCode : correctionTypeList) {
                selectList.add(new LabelValueBean(genericCode.getCode() + "-" + genericCode.getCodedesc(), genericCode.getId().toString()));
            }
        }
        request.setAttribute("correctionTypeList", selectList);

    }

    private String getPreparedValue(String forwarder, String forwarderForCollect, MessageResources messageResources) throws Exception {
        String returnValue = null;
        if (forwarder != null && !forwarder.trim().equals(messageResources.getMessage("notForwarder"))
                && !forwarder.trim().equals(messageResources.getMessage("notForwarderSecond"))) {
            returnValue = messageResources.getMessage("notPrepaidForwarder");
        } else if (forwarderForCollect != null
                && !forwarderForCollect.trim().equals(messageResources.getMessage("notForwarder"))
                && !forwarderForCollect.trim().equals(messageResources.getMessage("notForwarderSecond"))) {
            returnValue = messageResources.getMessage("notColltecForwarder");
        }
        return returnValue;
    }

    public ActionForward searchCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("loginName", getCurrentUser(request).getLoginName());
        request.setAttribute("userId", getCurrentUser(request).getUserId());
        DBUtil dbUtil = new DBUtil();
        request.setAttribute("correctionCodeList", dbUtil.getGenericCodeList(52, "no", "Select Correction Code"));
        return mapping.findForward(LCL_BL_SEARCH_CORRECTION);
    }

    public ActionForward viewSearchCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            request.setAttribute("lclBlCorrectionSearchList", lclCorrectionDAO.getSearchCorrectionsImports(lclCorrectionForm));
        } else {
            request.setAttribute("lclBlCorrectionSearchList", lclCorrectionDAO.getSearchCorrections(lclCorrectionForm));
        }
        lclCorrectionForm.setUserId(getCurrentUser(request).getUserId());
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
        request.setAttribute("disabledFilter", lclCorrectionForm.getFilterBy().equalsIgnoreCase("1") ? "D" : "");
        request.setAttribute("showAllFilter", lclCorrectionForm.getFilterBy());
        return mapping.findForward(LCL_BL_VIEW_SEARCH_CORRECTION);
    }

    public ActionForward viewAuthenticationScreen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        lclCorrectionForm.setUserPassword(getCurrentUser(request).getPassword());
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
        return mapping.findForward(LCL_BL_VIEW_AUTHENTICATE_POPUP);
    }

    public ActionForward approveCorrections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
            LclManifestDAO lclManifestDAO = new LclManifestDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            String redirectUrl = null;
            List<Object[]> lclCorrectionChargeList = null;
            LclUtils lclUtils = new LclUtils();
            LclPrintUtil lclPrintUtil = new LclPrintUtil();
            String realPath = request.getSession().getServletContext().getRealPath("/");
            String fileLocation = null;
            BigInteger pieceCount = null;
            LclContact lclContact = null;
            User user = getCurrentUser(request);
            List<LclBlAc> lclBlAcList = null;
            LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
            Date d = new Date();
            String correctionNo = "";
            //********Post Quick CN*************************************************
            LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", lclCorrectionForm.getFileId());
            if (lclCorrectionForm.getButtonValue().equalsIgnoreCase("Q")) {
                String strCorrectionTypeIdA = genericCodeDAO.getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION, GENERIC_CODE_A_BL_CORRECTION, "id");
                if (CommonUtils.isNotEmpty(strCorrectionTypeIdA)) {
                    lclCorrectionForm.setCorrectionType(Integer.valueOf(strCorrectionTypeIdA));
                }
                if (CommonUtils.isEmpty(lclCorrectionForm.getComments())) {
                    lclCorrectionForm.setComments("QUICK CN");
                }
                if (CommonUtils.isNotEmpty(lclCorrectionForm.getFileId())) {
                    lclCorrectionDAO.saveCorrection(lclCorrectionForm, getCurrentUser(request));//saveCorrection
                    setViewCorrections(lclCorrectionForm, lclCorrectionDAO, request);
                    if (CommonUtils.isNotEmpty(lclCorrectionForm.getBillToCode()) && !lclBooking.getBillToParty().equalsIgnoreCase(lclCorrectionForm.getBillToCode())) {
                        updateBillToCode(mapping, lclCorrectionForm, request, response);
                    }
                }
                if (CommonUtils.isEmpty(lclCorrectionForm.getCorrectionId())) {
                    lclCorrectionForm.setCorrectionId(lclCorrectionDAO.getCorrectionIdByFileId(lclCorrectionForm.getFileId()).longValue());
                }
            }
            //**********************************************************************
            LclCorrection lclCorrection = lclCorrectionDAO.findById(lclCorrectionForm.getCorrectionId());
            correctionNo = "(" + lclCorrection.getLclFileNumber().getFileNumber() + "-C-" + lclCorrection.getCorrectionNo() + ")";
            if (CommonUtils.notIn(lclCorrection.getStatus(), "A", "Q")) {
                lclBookingDAO.getCurrentSession().evict(lclBooking);
                if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
                    lclBooking = lclManifestDAO.postCorrection(lclBooking, lclCorrection, user);
                } else {
                    lclBlAcList = lclBlAcDAO.getLclChargesByFileNumberAsc(lclCorrectionForm.getFileId());
                    int approvedCorrectionCount = lclCorrectionDAO.getApprovedCorrectionCountByFileId(lclCorrectionForm.getFileId());
                    if (approvedCorrectionCount == 0) {
                        for (LclBlAc lclBlAc : lclBlAcList) {
                            lclBlAc.setArAmount(lclBlAc.getArAmount().add(lclBlAc.getAdjustmentAmount()));
                            lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
                            lclBlAcDAO.update(lclBlAc);
                        }
                    }
                    lclManifestDAO.createLclCorrections(lclCorrectionForm.getSelectedMenu(), user, true, lclCorrection, true, lclBooking);
                    lclCorrectionChargeList = lclCorrectionChargeDAO.getAllCorrectionChargesForApprove(lclCorrectionForm.getCorrectionId());
                    for (Object[] lclCorrectionCharge : lclCorrectionChargeList) {
                        if (lclBlAcDAO.getCountbyFileNumberAndChargeId(lclCorrectionForm.getFileId(), Integer.parseInt(lclCorrectionCharge[0].toString())) > 0) {
                            lclBlAcDAO.updateChargesForApproval(lclCorrectionForm.getFileId(), getCurrentUser(request).getUserId(),
                                    lclCorrectionCharge);
                        } else {
                            lclBlAcDAO.insertChargesForApproval(lclCorrectionForm.getFileId(), lclCorrectionCharge[0].toString(),
                                    getCurrentUser(request).getUserId(), lclCorrectionCharge[1].toString(),
                                    lclCorrectionCharge[2].toString());
                        }
                    }
                }
                if (lclCorrection.getConsAcct() != null) {
                    lclBooking.setConsAcct(lclCorrection.getConsAcct());
                    lclBooking.setConsContact(new LclContact(null, "", lclCorrection.getConsAcct().getAccountName(),
                            d, d, user, user, lclBooking.getLclFileNumber()));
                    lclCorrectionForm.setConstAcctNo(lclCorrection.getConsAcct().getAccountno());
                    lclCorrectionForm.setConstAcctName(lclCorrection.getConsAcct().getAccountName());
                }
                if (lclCorrection.getNotyAcct() != null) {
                    lclBooking.setNotyAcct(lclCorrection.getNotyAcct());
                    lclBooking.setNotyContact(new LclContact(null, "", lclCorrection.getNotyAcct().getAccountName(),
                            d, d, user, user, lclBooking.getLclFileNumber()));
                    lclCorrectionForm.setNotyAcctNo(lclCorrection.getNotyAcct().getAccountno());
                    lclCorrectionForm.setNotyAcctName(lclCorrection.getNotyAcct().getAccountName());
                }
                if (lclCorrection.getThirdPartyAcct() != null) {
                    lclBooking.setThirdPartyAcct(lclCorrection.getThirdPartyAcct());
                    lclCorrectionForm.setThirdPartyAcctNo(lclCorrection.getThirdPartyAcct().getAccountno());
                    lclCorrectionForm.setThirdPartyName(lclCorrection.getThirdPartyAcct().getAccountName());
                }
                lclBooking.setBillingType(lclCorrection.getBillingType());
                lclBookingDAO.getCurrentSession().clear();
                lclBookingDAO.update(lclBooking);
                if (lclCorrectionForm.getButtonValue().equalsIgnoreCase("Q")) {
                    lclCorrectionDAO.approveCorrections(lclCorrectionForm.getCorrectionId(), user.getUserId(), "Q");
                } else {
                    lclCorrectionDAO.approveCorrections(lclCorrectionForm.getCorrectionId(), user.getUserId(), "A");
                }
                if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Exports") || lclCorrectionForm.getButtonValue().equalsIgnoreCase("SA")) {
                    request.setAttribute("lclBlCorrectionSearchList", lclCorrectionDAO.getSearchCorrections(lclCorrectionForm));
                    request.setAttribute("message", "Correction Notice " + lclCorrectionForm.getConcatenatedBlNo() + " is Approved and Posted");
                    viewSearchCorrection(mapping, form, request, response);
                    redirectUrl = LCL_BL_VIEW_SEARCH_CORRECTION;
                    String creditDebitEMail = null;
                    if (CommonUtils.isNotEmpty(lclCorrection.getDebitEmail())) {
                        creditDebitEMail = lclCorrection.getDebitEmail();
                    } else if (CommonUtils.isNotEmpty(lclCorrection.getCreditEmail())) {
                        creditDebitEMail = lclCorrection.getCreditEmail();
                    }
                    if (CommonUtils.isNotEmpty(creditDebitEMail)) {
                        String debitOrCreditNote = "CREDIT NOTE";
                        boolean isCreditDebitNote = true;//accountingDAO.isCreditDebitNote(lclCorrection.getPartyAcctNo().getAccountno());
                        if (isCreditDebitNote) {
                            LCLCorrectionNoticeBean lclCorrectionNoticeBean = lclCorrectionDAO.getAllCorrectionByFileIdReports(
                                    lclCorrectionForm.getFileId().toString(), lclCorrectionForm.getCorrectionId(),
                                    lclCorrectionForm.getSelectedMenu());
                            BigDecimal totalAmount = lclCorrectionChargeDAO.getCorrectionChargesDifferenceForCreditDebit(lclCorrectionForm.getCorrectionId());
                            if (!(CommonUtils.isEmpty(totalAmount))) {
                                if (totalAmount.doubleValue() > 0) {
                                    debitOrCreditNote = "DEBIT NOTE";
                                }
                            }
                            lclCorrectionNoticeBean.setDebitOrCreditNote(debitOrCreditNote);
                            lclCorrectionNoticeBean.setCorrectionNo(String.valueOf(lclCorrection.getCorrectionNo()));
                            if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
                                LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
                                pieceCount = lclBookingPieceDAO.getPieceCountByFileId(lclCorrectionForm.getFileId());
                                List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.getLclChargeByFileNumberAsc(lclCorrectionForm.getFileId());
                                LclBookingAc lclBookingAc = lclBookingAcList.get(0);
                                if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("C")) {
                                    lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getConsContact();
                                } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("A")) {
                                    lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getAgentContact();
                                } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("N")) {
                                    lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getNotyContact();
                                } else {
                                    lclContact = lclBookingAc.getLclFileNumber().getLclBooking().getThirdPartyContact();
                                }
                                lclCorrectionNoticeBean.setBillToPartyAddress(lclUtils.getConcatenatedLclContactAddress(lclContact));
                                request.setAttribute("lclCorrectionChargesList", lclUtils.getFormattedCorrectionChargesPdf(lclCorrectionForm.getCorrectionId()));
                                String outputFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/" + BILLOFLADINGFILENAME + "/" + "LCL-CN-"
                                        + lclCorrection.getLclFileNumber().getFileNumber() + "-" + lclCorrection.getCorrectionNo() + "-"
                                        + debitOrCreditNote + ".pdf";//"("+customerNumber+")-"
                                LclCorrectionDebitCreditPdfCreator lclCorrectionDebitORCreditNoteReportPdfCreator = new LclCorrectionDebitCreditPdfCreator();
                                fileLocation = lclCorrectionDebitORCreditNoteReportPdfCreator.createReport(lclCorrectionNoticeBean, outputFileName, realPath, request, lclCorrectionForm.getFileId(), lclCorrectionForm.getSelectedMenu());
                                lclUtils.sendMailWithoutPrintConfig(fileLocation, SCREENNAMELCLIMPORTBOOKINGREPORT,
                                        DOCUMENTLCLIMPORTSARRIVALNOTICE + "For File#" + lclCorrection.getLclFileNumber().getFileNumber(), "Email", "Pending",
                                        creditDebitEMail, lclCorrection.getLclFileNumber().getFileNumber(), SCREENNAMELCLIMPORTBOOKINGREPORT,
                                        "", "", user);
                            } else {
                                LclBlAc lclBlAc = lclBlAcList.get(0);
                                if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("S")) {
                                    lclContact = lclBlAc.getLclFileNumber().getLclBl().getShipContact();
                                } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("A")) {
                                    lclContact = lclBlAc.getLclFileNumber().getLclBl().getAgentContact();
                                } else if (lclCorrectionNoticeBean.getBillToParty().equalsIgnoreCase("F")) {
                                    lclContact = lclBlAc.getLclFileNumber().getLclBl().getFwdContact();
                                } else {
                                    lclContact = lclBlAc.getLclFileNumber().getLclBl().getThirdPartyContact();
                                }
                                lclCorrectionNoticeBean.setBillToPartyAddress(lclUtils.getConcatenatedLclContactAddress(lclContact));
                                request.setAttribute("lclCorrectionChargesList", lclUtils.getFormattedCorrectionChargesPdf(lclCorrection.getId()));
                                String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
                                outputFileName = outputFileName + "/" + LCL_BOOKING + "/" + "LCL-CN-"
                                        + lclCorrectionNoticeBean.getBlNo() + "-" + lclCorrection.getCorrectionNo() + "-"
                                        + debitOrCreditNote + ".pdf";//"("+customerNumber+")-"
                                LclCorrectionDebitCreditPdfCreator lclCorrectionDebitORCreditNoteReportPdfCreator = new LclCorrectionDebitCreditPdfCreator();
                                fileLocation = lclCorrectionDebitORCreditNoteReportPdfCreator.createReport(lclCorrectionNoticeBean, outputFileName, realPath, request, lclCorrectionForm.getFileId(), lclCorrectionForm.getSelectedMenu());
                                String subject = DOCUMENTLCLEXPORTS + "-" + lclCorrectionNoticeBean.getBlNo() + "-" + lclCorrectionNoticeBean.getDebitOrCreditNote() + " For "
                                        + lclCorrectionNoticeBean.getCustomer() + "(" + lclCorrectionNoticeBean.getCustomerAcctNo() + ")";
                                lclUtils.sendMailWithoutPrintConfig(fileLocation, LCL_BOOKING, subject, "Email", "Pending", creditDebitEMail,
                                        lclCorrection.getLclFileNumber().getFileNumber(), LCL_BOOKING, "", "", user);
                            }
                        } else {
                            if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
                                String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
                                fileLocation = lclPrintUtil.createImportBkgReport(outputFileName, lclCorrectionForm.getFileId().toString(), lclCorrection.getLclFileNumber().getFileNumber(), DOCUMENTLCLIMPORTSARRIVALNOTICE, realPath, null, request);
                                lclUtils.sendMailWithoutPrintConfig(fileLocation, SCREENNAMELCLIMPORTBOOKINGREPORT,
                                        DOCUMENTLCLIMPORTSARRIVALNOTICE + "For File#" + lclCorrection.getLclFileNumber().getFileNumber(), "Email", "Pending",
                                        creditDebitEMail, lclCorrection.getLclFileNumber().getFileNumber(), SCREENNAMELCLIMPORTBOOKINGREPORT,
                                        "", "", user);
                            } else {
                            }
                        }
                    }

                } else {
                    request.setAttribute("message", "Correction Notice " + correctionNo + " is Approved and Posted");
                    redirectUrl = LCL_BL_VIEW_CORRECTION;
                }
                setVoidClassName(lclCorrectionDAO, lclCorrectionForm);
                lclUtils.insertLCLCorrectionRemarks(REMARKS_TYPE_LCL_CORRECTIONS, lclCorrectionForm.getFileId(), correctionNo, "is Approved and Posted", user);
            }
            request.setAttribute("lclCorrectionForm", lclCorrectionForm);
            return mapping.findForward(redirectUrl);
        }
    }

    public ActionForward updateBillToCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        LCLCorrectionChargeDAO correctionChargeDAO = new LCLCorrectionChargeDAO();
        List<LCLCorrectionChargeBean> correctionChargeBean = null;
        lclCorrectionForm.setCurrentExitBillToCode(lclCorrectionForm.getBillToCode());
        setCorrections(lclCorrectionForm, new LCLCorrectionDAO(), request);
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getCorrectionId())) {
            correctionChargeBean = (List<LCLCorrectionChargeBean>) request.getAttribute("lclCorrectionChargesList");
            List correctIdList = correctionChargeDAO.getCorrectionChargeId(lclCorrectionForm.getCorrectionId());
            if (correctIdList.isEmpty()) {
                LclCorrection lclCorrection = new LCLCorrectionDAO().findById(lclCorrectionForm.getCorrectionId());
                saveLclBookingAcCharge(correctionChargeBean, lclCorrection, lclCorrectionForm.getBillToCode(), loginUser);
            } else {
                correctionChargeDAO.updateCorrectionBillToCode(lclCorrectionForm.getBillToCode(), loginUser.getUserId(), lclCorrectionForm.getCorrectionId());
            }
            request.setAttribute("lclCorrectionChargesList", correctionChargeBean);
        }
        return mapping.findForward(LCL_BL_CORRECTION_CHARGE_DESC);
    }

    public List<LCLCorrectionChargeBean> saveLclBookingAcCharge(List<LCLCorrectionChargeBean> correctionChargeBean, LclCorrection lclCorrection, String billToCode, User user) throws Exception {
        LclCorrectionCharge correctionCharge = new LclCorrectionCharge();
        Date date = new Date();
        if (!correctionChargeBean.isEmpty()) {
            for (LCLCorrectionChargeBean lclCorrectionChargeBean : correctionChargeBean) {
                if (CommonUtils.isNotEmpty(lclCorrectionChargeBean.getOldAmount().doubleValue()) && lclCorrectionChargeBean.getOldAmount().doubleValue() != 0.00 && CommonUtils.isEmpty(lclCorrectionChargeBean.getNewAmount()) || lclCorrectionChargeBean.getNewAmount().doubleValue() == 0.00) {
                    if (!billToCode.equalsIgnoreCase(lclCorrectionChargeBean.getOldBillToCode())) {
                        GlMapping arGlmapping = new GlMappingDAO().findById(lclCorrectionChargeBean.getChargeId());
                        correctionCharge.setLclCorrection(lclCorrection);
                        correctionCharge.setLclBookingAc(null);
                        correctionCharge.setBillToParty(billToCode);
                        correctionCharge.setGlMapping(arGlmapping);
                        correctionCharge.setOldAmount(lclCorrectionChargeBean.getOldAmount());
                        correctionCharge.setNewAmount(BigDecimal.ZERO);
                        correctionCharge.setEnteredBy(user);
                        correctionCharge.setEnteredDatetime(date);
                        correctionCharge.setModifiedBy(user);
                        correctionCharge.setModifiedDatetime(date);
                        this.save(correctionCharge);
                    }
                }

            }
        }
        return correctionChargeBean;
    }
}
