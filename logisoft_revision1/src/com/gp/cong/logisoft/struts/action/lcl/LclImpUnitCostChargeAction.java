/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.lcl.model.LCLFAECostModel;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitCostChargeForm;
import com.logiware.accounting.dao.LclManifestDAO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Logiware
 */
public class LclImpUnitCostChargeAction extends LogiwareDispatchAction implements LclCommonConstant, ConstantsInterface {

    public ActionForward displayCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (groupByInvoiceFlag == null) {
            groupByInvoiceFlag = "false";
        }
        List<LclSsAc> lclUnitSSAcList = lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag);
        lclUnitCostChargeForm.setCostAmount(lclSSAcDAO.getTotalLclUnitCostByUnitSSId(lclUnitCostChargeForm.getUnitSsId()).toString());
        lclUnitCostChargeForm.setUnitStatus(new LclUnitSsDAO().getStatusById(lclUnitCostChargeForm.getUnitSsId().toString()).toString());
        request.setAttribute("lclUnitSSAcList", lclUnitSSAcList);
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        return mapping.findForward("displayCharge");
    }

    public ActionForward displayCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        if ("Exports".equalsIgnoreCase(lclUnitCostChargeForm.getModuleName())) {
            lclUnitCostChargeForm.setAutoCostFlag(lclSSAcDAO.isCheckedRates(lclUnitCostChargeForm.getHeaderId(),
                    lclUnitCostChargeForm.getUnitSsId(), false, false));
        } else {
            Long[] lclUnitSsImports = new LclUnitSsImportsDAO().getWarehouseAndUnitTypeId(lclUnitCostChargeForm.getUnitId(), lclUnitCostChargeForm.getHeaderId());
            lclUnitCostChargeForm.setUnitTypeId(lclUnitSsImports[1]);
            if (lclUnitSsImports[0] > 0l) {
                lclUnitCostChargeForm.setCfsWarehouseId(Integer.parseInt(lclUnitSsImports[0].toString()));
            }
        }
        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (groupByInvoiceFlag == null) {
            groupByInvoiceFlag = "false";
        }
        lclUnitCostChargeForm.setSaveDrCostFlag("false");
        request.setAttribute("drCostList", lclSSAcDAO.getAllDrCost(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("lclUnitSSAcList", lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        return mapping.findForward("displayCost");
    }

    public ActionForward deleteCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getUnitSSAcId())) {
            User loginUser = (User) request.getSession().getAttribute("loginuser");
            LclSsAc lclSSAc = lclSSAcDAO.findById(lclUnitCostChargeForm.getUnitSSAcId());
            // Delete Cost Code Remarks
            String remarks = "DELETED -> Cost Code -> " + lclSSAc.getApGlMappingId().getChargeCode() + ", Cost Amount -> " + lclSSAc.getApAmount();
            new LclUnitSsRemarksDAO().insertLclunitRemarks(lclSSAc.getLclSsHeader().getId(), lclSSAc.getLclUnitSs().getLclUnit().getId(), "auto", remarks, loginUser.getUserId());
            String concatenatedVoyageNumber = "";
            if (!"I".equalsIgnoreCase(lclSSAc.getLclSsHeader().getServiceType())) {
                concatenatedVoyageNumber = lclSSAc.getLclSsHeader().getOrigin().getUnLocationCode() + "-"
                        + lclSSAc.getLclSsHeader().getDestination().getUnLocationCode() + "-"
                        + lclSSAc.getLclSsHeader().getScheduleNo();
            } else {
                concatenatedVoyageNumber = lclSSAc.getLclSsHeader().getBillingTerminal().getTrmnum() + "-"
                        + lclSSAc.getLclSsHeader().getOrigin().getUnLocationCode() + "-"
                        + lclSSAc.getLclSsHeader().getDestination().getUnLocationCode() + "-"
                        + lclSSAc.getLclSsHeader().getScheduleNo();
            }
            String containerNo = lclSSAc.getLclUnitSs().getLclUnit().getUnitNo();
            lclSSAc.setDeleted(true);
            lclSSAc.setApAmount(BigDecimal.ZERO);
            lclSSAc.setModifiedDatetime(new Date());
            lclSSAcDAO.update(lclSSAc);
            lclManifestDAO.deleteLclUnitAccruals(lclUnitCostChargeForm.getUnitSSAcId().intValue(),
                    concatenatedVoyageNumber, containerNo);
        }
        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (groupByInvoiceFlag == null) {
            groupByInvoiceFlag = "false";
        }
        request.setAttribute("drCostList", lclSSAcDAO.getAllDrCost(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("lclUnitSSAcList", lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        lclUnitCostChargeForm.setUnitSSAcId(null);
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        return mapping.findForward("displayCost");
    }

    public ActionForward editCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getUnitSSAcId())) {
            LclSsAc lclSSAc = lclSSAcDAO.findById(lclUnitCostChargeForm.getUnitSSAcId());
            setFormValues(lclUnitCostChargeForm, lclSSAc);
        }
        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (groupByInvoiceFlag == null) {
            groupByInvoiceFlag = "false";
        }
        lclUnitCostChargeForm.setSaveDrCostFlag("false");
        request.setAttribute("lclUnitSSAcList", lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("drCostList", lclSSAcDAO.getAllDrCost(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        return mapping.findForward("displayCost");
    }

    public ActionForward saveCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LclSsAc lclSSAc = null;
        List<LclSsAc> lclSsAcList = new ArrayList<LclSsAc>();
        Date d = new Date();
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getUnitSSAcId())) {
            lclSSAc = lclSSAcDAO.findById(lclUnitCostChargeForm.getUnitSSAcId());
        } else {
            lclSSAc = new LclSsAc();
            LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
            LclUnitSs lclUnitSs = lclUnitSsDAO.findById(lclUnitCostChargeForm.getUnitSsId());
            lclSSAc.setLclSsHeader(lclUnitSs.getLclSsHeader());
            lclSSAc.setLclUnitSs(lclUnitSs);
            lclSSAc.setEnteredByUserId(getCurrentUser(request));
            lclSSAc.setEnteredDatetime(d);
            lclSSAc.setTransDatetime(d);
            lclSSAc.setApTransType(TRANSACTION_TYPE_ACCRUALS);
            lclSSAc.setManualEntry(true);
            lclSSAc.setArAmount(BigDecimal.ZERO);
        }
        GlMapping glmapping = glMappingDAO.findById(lclUnitCostChargeForm.getChargesCodeId());
        lclSSAc.setArGlMappingId(glmapping);
        lclSSAc.setApGlMappingId(glmapping);
        lclSSAc.setApAcctNo(tradingPartnerDAO.findById(lclUnitCostChargeForm.getThirdpartyaccountNo()));
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getInvoiceNumber())) {
            lclSSAc.setApReferenceNo(lclUnitCostChargeForm.getInvoiceNumber().toUpperCase());
        } else {
            lclSSAc.setApReferenceNo("");
        }
        lclSSAc.setApAmount(new BigDecimal(lclUnitCostChargeForm.getCostAmount()));
        lclSSAc.setModifiedByUserId(getCurrentUser(request));
        lclSSAc.setModifiedDatetime(d);
        lclSSAcDAO.saveOrUpdate(lclSSAc);
        lclSsAcList.add(lclSSAc);
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getUnitSSAcId())) {
            lclManifestDAO.updateLclUnitAccruals(lclSSAc);
        } else {
            lclManifestDAO.createLclAccrualsforAutoCosting(lclSsAcList);
        }
        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (groupByInvoiceFlag == null) {
            groupByInvoiceFlag = "false";
        }
        lclUnitCostChargeForm.setSaveDrCostFlag("false");
        request.setAttribute("lclUnitSSAcList", lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("drCostList", lclSSAcDAO.getAllDrCost(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        clearFormValues(lclUnitCostChargeForm);
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        return mapping.findForward("displayCost");
    }

    public ActionForward editDrCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclCostChargeDAO lclBookingAcDAO = new LclCostChargeDAO();
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getBookingAcId())) {
            LclBookingAc lclBookingAc = lclBookingAcDAO.findById(lclUnitCostChargeForm.getBookingAcId());
            setDrCostFormValues(lclUnitCostChargeForm, lclBookingAc);
        }
        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (groupByInvoiceFlag == null) {
            groupByInvoiceFlag = "false";
        }
        lclUnitCostChargeForm.setSaveDrCostFlag("true");
        request.setAttribute("lclUnitSSAcList", lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("drCostList", lclSSAcDAO.getAllDrCost(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        return mapping.findForward("editDrCost");
    }

    public ActionForward saveDrCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        LclCostChargeDAO lclBookingAcDAO = new LclCostChargeDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LclBookingAc lclBookingAc = null;
        Date d = new Date();
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getBookingAcId())) {
            lclBookingAc = lclBookingAcDAO.findById(lclUnitCostChargeForm.getBookingAcId());
        }
        lclBookingAc.setApAmount(new BigDecimal(lclUnitCostChargeForm.getCostAmount()));
        lclBookingAc.setModifiedBy(getCurrentUser(request));
        lclBookingAc.setModifiedDatetime(d);
        lclBookingAc.setSupAcct(tradingPartnerDAO.findById(lclUnitCostChargeForm.getThirdpartyaccountNo()));
        lclBookingAcDAO.saveOrUpdate(lclBookingAc);
//        lclManifestDAO.updateLclAccruals(lclBookingAc, lclBookingAc.getModifiedBy(), "LCLI");
        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (groupByInvoiceFlag == null || groupByInvoiceFlag.equals("")) {
            groupByInvoiceFlag = "false";
        }
        lclUnitCostChargeForm.setSaveDrCostFlag("false");
        request.setAttribute("lclUnitSSAcList", lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("drCostList", lclSSAcDAO.getAllDrCost(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        clearFormValues(lclUnitCostChargeForm);
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        return mapping.findForward("displayCost");
    }

    public ActionForward distributeDr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /* DONT REMOVE THE FOLLOWING COMMENTED CODE */

        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        List<Object[]> fileList = lclFileNumberDAO.getAllFileIdsByUnitSSId(lclUnitCostChargeForm.getUnitSsId());
        if (CommonUtils.isNotEmpty(fileList)) {
            BigDecimal totalCbmKgs = BigDecimal.ZERO;
            BigDecimal zeroBigDecimal = BigDecimal.ZERO;
            //BigDecimal kgsBigDecimal = new BigDecimal(1000);
            BigDecimal cbm = null;
            for (int i = 0; i < fileList.size(); i++) {
                Object[] obj = fileList.get(i);
                if (obj[2] != null && CommonUtils.isNotEmpty(obj[2].toString())) {
                    cbm = new BigDecimal(obj[2].toString());
                } else {
                    cbm = zeroBigDecimal;
                }
                /*
                 if (obj[3] != null && CommonUtils.isNotEmpty(obj[3].toString())) {
                 kgs = new BigDecimal(obj[3].toString());
                 kgs = kgs.divide(kgsBigDecimal);
                 } else {
                 kgs = zeroBigDecimal;
                 }
                 if (kgs.doubleValue() < cbm.doubleValue()) {*/
                totalCbmKgs = totalCbmKgs.add(cbm);
                /*} else {
                 totalCbmKgs = totalCbmKgs.add(kgs);
                 }*/
            }
            if (lclUnitCostChargeForm.getFinalAmount() != null && !lclUnitCostChargeForm.getFinalAmount().trim().equals("")) {
                Double flatRateAmount = Double.parseDouble(lclUnitCostChargeForm.getFinalAmount());
                Double calculatedCbmCft = flatRateAmount / totalCbmKgs.doubleValue();
                List<ImportsManifestBean> drList = new ArrayList<ImportsManifestBean>();
                Double totalAmount = 0.00, totalBasisAmount = 0.00, percentageAmount = 0.00, totalPercentageAmount = 0.00;
                Double totalChargesAmount = 0.00;
                for (int i = 0; i < fileList.size(); i++) {
                    Object[] obj = fileList.get(i);
                    ImportsManifestBean importsManifestBean = new ImportsManifestBean();
                    if (obj[2] != null && CommonUtils.isNotEmpty(obj[2].toString())) {
                        cbm = new BigDecimal(obj[2].toString());
                    } else {
                        cbm = zeroBigDecimal;
                    }
                    importsManifestBean.setTotalVolumeImperial(cbm);
                    /*
                     if (obj[3] != null && CommonUtils.isNotEmpty(obj[3].toString())) {
                     kgs = new BigDecimal(obj[3].toString());
                     kgs = kgs.divide(kgsBigDecimal);
                     } else {
                     kgs = zeroBigDecimal;
                     }
                     importsManifestBean.setTotalWeightImperial(kgs);*/
                    if (i != fileList.size() - 1) {
                        //if (kgs.doubleValue() < cbm.doubleValue()) {
                        importsManifestBean.setStrTotalCharges(NumberUtils.convertToTwoDecimal(cbm.doubleValue() * calculatedCbmCft));
                        importsManifestBean.setBasis(cbm.toString() + "(CBM)");
                        totalBasisAmount += cbm.doubleValue();
                        /*} else {
                         importsManifestBean.setStrTotalCharges(NumberUtils.convertToTwoDecimal(kgs.doubleValue() * calculatedCbmCft));
                         importsManifestBean.setBasis(kgs.toString() + "(KGS)");
                         totalBasisAmount += kgs.doubleValue();
                         }*/
                        totalAmount += Double.parseDouble(importsManifestBean.getStrTotalCharges());
                        percentageAmount = (100 / flatRateAmount) * Double.parseDouble(importsManifestBean.getStrTotalCharges());
                        totalPercentageAmount += percentageAmount;
                        importsManifestBean.setBasis(importsManifestBean.getBasis() + "    " + NumberUtils.convertToTwoDecimal(percentageAmount) + "%");
                        if ("USNYC".equalsIgnoreCase(lclUnitCostChargeForm.getPodUnlocCode()) && Double.parseDouble(importsManifestBean.getStrTotalCharges()) < 50.00) {
                            importsManifestBean.setStrTotalCharges("50.00");
                            importsManifestBean.setMinimumAmount("(MIN)");
                        }
                        totalChargesAmount += Double.parseDouble(importsManifestBean.getStrTotalCharges());
                    } else {
                        //if (kgs.doubleValue() < cbm.doubleValue()) {
                        importsManifestBean.setBasis(cbm.toString() + "(CBM)");
                        totalBasisAmount += cbm.doubleValue();
                        /*} else {
                         importsManifestBean.setBasis(kgs.toString() + "(KGS)");
                         totalBasisAmount += kgs.doubleValue();
                         }*/
                        importsManifestBean.setStrTotalCharges(NumberUtils.convertToTwoDecimal(flatRateAmount - totalAmount));
                        if ("USNYC".equalsIgnoreCase(lclUnitCostChargeForm.getPodUnlocCode()) && Double.parseDouble(importsManifestBean.getStrTotalCharges()) < 50.00) {
                            importsManifestBean.setStrTotalCharges("50.00");
                            importsManifestBean.setMinimumAmount("(MIN)");
                        }
                        totalChargesAmount += Double.parseDouble(importsManifestBean.getStrTotalCharges());
                        importsManifestBean.setBasis(importsManifestBean.getBasis() + "    " + NumberUtils.convertToTwoDecimalRoundDown(100 - totalPercentageAmount) + "%");
                    }
                    importsManifestBean.setFileId(Long.parseLong(obj[0].toString()));
                    importsManifestBean.setFileNo(obj[1].toString());
                    importsManifestBean.setFinalDestination(obj[3].toString());
                    if (obj[4] != null) {
                        importsManifestBean.setBillToParty(obj[4].toString());
                    } else {
                        importsManifestBean.setBillToParty("");
                    }
                    importsManifestBean.setChargeCode(lclUnitCostChargeForm.getChargesCode());
                    drList.add(importsManifestBean);
                }
                lclUnitCostChargeForm.setCostAmount(NumberUtils.convertToThreeDecimal(totalBasisAmount));
                lclUnitCostChargeForm.setChargesAmount(NumberUtils.convertToTwoDecimal(totalChargesAmount));
                lclUnitCostChargeForm.setFinalAmount(NumberUtils.convertToTwoDecimal(totalChargesAmount));
                request.setAttribute("drList", drList);
            }
        }
        lclUnitCostChargeForm.setUnitStatus(lclUnitSsDAO.getStatusById(lclUnitCostChargeForm.getUnitSsId().toString()).toString());
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("lclSsAcId", request.getParameter("lclSsAcId"));
        request.setAttribute("orginalCostAmount", request.getParameter("orginalCostAmount"));
        return mapping.findForward("displayDistributePopup");
    }

    public ActionForward saveDistributedDr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getFileId()) && CommonUtils.isNotEmpty(lclUnitCostChargeForm.getChargesAmount())) {
            String fileId[] = lclUnitCostChargeForm.getFileId().split(",");
            String chargeAmt[] = lclUnitCostChargeForm.getChargesAmount().split(",");
            LclUtils lclUtils = new LclUtils();
            LclUnitSsRemarksDAO lclUnitSSRemarksDAO = new LclUnitSsRemarksDAO();
            LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
            LclFileNumber lclFileNumber = null;
            GlMapping glmapping = new GlMappingDAO().findByChargeCode(lclUnitCostChargeForm.getChargesCode(), LCL_SHIPMENT_TYPE_IMPORT, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            String remarks = "Distributed " + lclUnitCostChargeForm.getChargesCode() + " charge from unit# " + lclUnitCostChargeForm.getUnitNo();
            for (int i = 0; i < fileId.length; i++) {
                if (Double.parseDouble(chargeAmt[i]) > 0.00) {
                    lclFileNumber = lclFileNumberDAO.findById(Long.parseLong(fileId[i]));
                    LclBookingAc lclBookingAc = new LclBookingAc();
                    BigDecimal decimalFinalAmount = new BigDecimal(chargeAmt[i]).setScale(2, BigDecimal.ROUND_HALF_UP);
                    lclBookingAc.setArAmount(decimalFinalAmount);
                    lclBookingAc.setArBillToParty("C");
                    lclUtils.insertLclBookingAc(lclBookingAc, Long.parseLong(fileId[i]), glmapping, decimalFinalAmount, getCurrentUser(request), request, "FL", "M", true);
                    lclUtils.insertLCLRemarks(Long.parseLong(fileId[i]), remarks, REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
                    if ("M".equalsIgnoreCase(lclFileNumber.getStatus()) && !lclBookingAc.getArBillToParty().equalsIgnoreCase("A")) {
                        postedCorrectionDR(lclFileNumber, glmapping, decimalFinalAmount, getCurrentUser(request));
                    }
                }
            }
            remarks = "Distributed (Charge Code -> " + glmapping.getChargeCode() + "),(Charge Amount --> $" + lclUnitCostChargeForm.getFinalAmount() + ")";
            lclUnitSSRemarksDAO.insertLclunitRemarks(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitId(), "auto", remarks,
                    getCurrentUser(request).getUserId());
            lclUnitCostChargeForm.setMessage(remarks);
            LclSsAcDAO lclSsAcDAO = new LclSsAcDAO();
            if (lclUnitCostChargeForm.getLclSsAcId() != null) {
                lclSsAcDAO.updateLclSsAc(lclUnitCostChargeForm.getLclSsAcId());
            }
            request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        }
        return mapping.findForward("displayDistributePopup");
    }

    private void clearFormValues(LclUnitCostChargeForm lclUnitCostChargeForm) {
        lclUnitCostChargeForm.setChargesCode("");
        lclUnitCostChargeForm.setChargesCodeId(0);
        lclUnitCostChargeForm.setInvoiceNumber("");
        lclUnitCostChargeForm.setCostAmount("");
        lclUnitCostChargeForm.setThirdPartyname("");
        lclUnitCostChargeForm.setThirdpartyaccountNo("");
        lclUnitCostChargeForm.setUnitSSAcId(null);
    }

    private void setFormValues(LclUnitCostChargeForm lclUnitCostChargeForm, LclSsAc lclSSAc) {
        lclUnitCostChargeForm.setChargesCode(lclSSAc.getApGlMappingId().getChargeCode());
        lclUnitCostChargeForm.setChargesCodeId(lclSSAc.getApGlMappingId().getId());
        lclUnitCostChargeForm.setInvoiceNumber(lclSSAc.getApReferenceNo());
        lclUnitCostChargeForm.setCostAmount(lclSSAc.getApAmount().toString());
        lclUnitCostChargeForm.setThirdPartyname(lclSSAc.getApAcctNo().getAccountName());
        lclUnitCostChargeForm.setThirdpartyaccountNo(lclSSAc.getApAcctNo().getAccountno());
    }

    private void setDrCostFormValues(LclUnitCostChargeForm lclUnitCostChargeForm, LclBookingAc lclBookingAc) {
        lclUnitCostChargeForm.setChargesCode(lclBookingAc.getApglMapping().getChargeCode());
        lclUnitCostChargeForm.setChargesCodeId(lclBookingAc.getApglMapping().getId());
        lclUnitCostChargeForm.setInvoiceNumber(lclBookingAc.getInvoiceNumber());
        lclUnitCostChargeForm.setCostAmount(lclBookingAc.getApAmount().toString());
        if (lclBookingAc.getSupAcct() != null) {
            lclUnitCostChargeForm.setThirdPartyname(lclBookingAc.getSupAcct().getAccountName());
            lclUnitCostChargeForm.setThirdpartyaccountNo(lclBookingAc.getSupAcct().getAccountno());
        }
    }

    public ActionForward saveUnitAutoCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        LclSsHeader lclssheader = new LclSsHeaderDAO().findById(lclUnitCostChargeForm.getHeaderId());
        LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(lclUnitCostChargeForm.getUnitSsId());
        //Delete Acctounting Cost
        new LclManifestDAO().deleteLclAccrualsByUnit(lclssheader.getScheduleNo(), lclUnitCostChargeForm.getUnitNo(), lclssheader.getId(), lclUnitSs.getId(), false);
        //Insert New Cost Values
        lclSSAcDAO.calculateUnitRates(lclUnitSs.getLclSsHeader(), lclUnitSs, lclssheader.getOrigin().getId(), lclssheader.getDestination().getId(), lclUnitCostChargeForm.getCfsWarehouseId(), lclUnitCostChargeForm.getUnitTypeId(), new Date(), request);
        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (groupByInvoiceFlag == null) {
            groupByInvoiceFlag = "false";
        }
        request.setAttribute("lclUnitSSAcList", lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        clearFormValues(lclUnitCostChargeForm);
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        return mapping.findForward("displayCost");
    }

    public void postedCorrectionDR(LclFileNumber lclFileNumber, GlMapping glmapping, BigDecimal amt, User user) throws Exception {
//      String customerNo = lclFileNumber.getLclBooking().getConsAcct().getAccountno();
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        Integer codeTypeId = codeTypeDAO.getCodeTypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION);
        Integer codeId = codeTypeDAO.getCodeTypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION_CODE);
        String cnType = genericCodeDAO.getByCodeAndCodetypeId(String.valueOf(codeTypeId), GENERIC_CODE_A_BL_CORRECTION, "id");
        String cnCode = genericCodeDAO.getByCodeAndCodetypeId(String.valueOf(codeId), GENERIC_CODE_A_CORRECTION_IMPORTS, "id");
        BigInteger lastCorrectionNo = lclCorrectionDAO.getIntegerDescByFileIdWithoutVoid(lclFileNumber.getId(), "correction_no");
        if (lastCorrectionNo == null) {
            lastCorrectionNo = new BigInteger("0");
        }
        String cnFileNo = "(" + lclFileNumber.getFileNumber() + "-C-" + String.valueOf(lastCorrectionNo.intValue() + 1) + ")";
        lclCorrectionDAO.saveCorrections(lclFileNumber.getId(), Integer.parseInt(cnType), Integer.parseInt(cnCode),
                lastCorrectionNo.intValue() + 1, "QUICK CN", "A", null, null, user.getUserId(), 0, lclFileNumber);
        String correction = CommonConstants.getEventMap().get(GENERIC_EVENT_CODE_CORRECTION_NOTES);
        StringBuilder savedCnNotes = new StringBuilder();
        savedCnNotes.append(correction).append(" ").append(cnFileNo).append(" ").append("Saved");
        lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_TYPE_LCL_CORRECTIONS, savedCnNotes.toString(), user.getUserId());
        BigInteger cnId = lclCorrectionDAO.getIntegerDescByFileIdWithoutVoid(lclFileNumber.getId(), "id");
        lclCorrectionChargeDAO.insertCorrectionCharge(cnId.longValue(), "0.00",
                String.valueOf(amt), glmapping.getId(), "C", user.getUserId());
        lclCorrectionDAO.approveCorrections(cnId.longValue(), user.getUserId(), "A");
        LclCorrection lclCorrection = lclCorrectionDAO.findById(cnId.longValue());
        lclManifestDAO.createLclCorrections(LCL_IMPORT, user, true, lclCorrection, true, lclFileNumber.getLclBooking());
        StringBuilder postCnNotes = new StringBuilder();
        postCnNotes.append(correction).append(" ").append(cnFileNo).append(" ").append("is got Approved and Post");
        lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_TYPE_LCL_CORRECTIONS, postCnNotes.toString(), user.getUserId());
    }

    public ActionForward calculateAutoCostByLCLE(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        if (CommonUtils.isNotEmpty(lclUnitCostChargeForm.getUnitSsId())) {
            User user = getCurrentUser(request);
            LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
            LclSsDetailDAO lclSsDetailDAO = new LclSsDetailDAO();
            LclUnitSs lclUnitSs = lclUnitSsDAO.findById(lclUnitCostChargeForm.getUnitSsId());
            LclSsDetail lclSsDetail = lclSsDetailDAO.findByTransMode(lclUnitSs.getLclSsHeader().getId(), "V");
            lclSSAcDAO.calculateUnitCostByLCLE(lclUnitSs.getLclSsHeader(), lclUnitSs,
                    lclSsDetail.getSpAcctNo().getAccountno(),
                    lclSsDetail.getDeparture().getId(), user, new Date(), lclUnitCostChargeForm.getHazFlag());
        }

        String groupByInvoiceFlag = lclUnitCostChargeForm.getGroupByInvoiceFlag();
        if (CommonUtils.isEmpty(groupByInvoiceFlag)) {
            groupByInvoiceFlag = "false";
        }
        request.setAttribute("groupByInvoiceFlag", groupByInvoiceFlag);
        lclUnitCostChargeForm.setAutoCostFlag(lclSSAcDAO.isCheckedRates(lclUnitCostChargeForm.getHeaderId(),
                lclUnitCostChargeForm.getUnitSsId(), false, false));
        lclUnitCostChargeForm.setSaveDrCostFlag("false");
        request.setAttribute("drCostList", lclSSAcDAO.getAllDrCost(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("lclUnitSSAcList", lclSSAcDAO.getAllLclUnitCostAsc(lclUnitCostChargeForm.getHeaderId(), lclUnitCostChargeForm.getUnitSsId(), groupByInvoiceFlag));
        request.setAttribute("lclUnitCostChargeForm", lclUnitCostChargeForm);
        return mapping.findForward("displayCost");
    }

    public ActionForward showFAECost(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm lclUnitCostChargeForm = (LclUnitCostChargeForm) form;
        LCLFAECostModel faeCostModel = new LCLFAECostModel();
        LCLFAECostModel model = faeCostModel.getBlData(lclUnitCostChargeForm.getUnitSsId(),
                lclUnitCostChargeForm.getHeaderId());
        request.setAttribute("model", model);
        request.setAttribute("faeCostList", model.getFaeCostList());
        request.setAttribute("costChargeForm", lclUnitCostChargeForm);
        return mapping.findForward("lclFAECost");
    }

    public ActionForward saveFAECost(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitCostChargeForm chargeForm = (LclUnitCostChargeForm) form;
        LclSsAcDAO lclSSAcDAO = new LclSsAcDAO();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LclSsAc lclSSAc = null;
        List<LclSsAc> lclSsAcList = new ArrayList<LclSsAc>();
        Date d = new Date();
        String chargeAndCost = request.getParameter("chargeAndCost");
        String[] costValue = chargeAndCost.split(",");
        for (String costData : costValue) {
            String[] costAmount = costData.split("_");
            LclSsAc lclSsAc = new LclSsAcDAO().getCost(chargeForm.getUnitSsId(), chargeForm.getHeaderId(), Integer.parseInt(costAmount[0]), true, "AC");
            if (lclSsAc == null) {
                lclSSAc = new LclSsAc();
                LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
                LclUnitSs lclUnitSs = lclUnitSsDAO.findById(chargeForm.getUnitSsId());
                lclSSAc.setLclSsHeader(lclUnitSs.getLclSsHeader());
                lclSSAc.setLclUnitSs(lclUnitSs);
                lclSSAc.setEnteredByUserId(getCurrentUser(request));
                lclSSAc.setEnteredDatetime(d);
                lclSSAc.setTransDatetime(d);
                lclSSAc.setApTransType(TRANSACTION_TYPE_ACCRUALS);
                lclSSAc.setManualEntry(false);
                lclSSAc.setArAmount(BigDecimal.ZERO);
            }
            GlMapping glmapping = glMappingDAO.findById(Integer.parseInt(costAmount[0]));
            lclSSAc.setArGlMappingId(glmapping);
            lclSSAc.setApGlMappingId(glmapping);
            lclSSAc.setApAcctNo(tradingPartnerDAO.findById(chargeForm.getThirdpartyaccountNo()));
            lclSSAc.setApAmount(new BigDecimal(costAmount[1]));
            lclSSAc.setModifiedByUserId(getCurrentUser(request));
            lclSSAc.setModifiedDatetime(d);
            lclSSAcDAO.saveOrUpdate(lclSSAc);
            lclSsAcList.add(lclSSAc);
        }
        if (CommonUtils.isNotEmpty(chargeForm.getUnitSSAcId())) {
            lclManifestDAO.updateLclUnitAccruals(lclSSAc);
        } else {
            lclManifestDAO.createLclAccrualsforAutoCosting(lclSsAcList);
        }
        return mapping.findForward("displayCharge");
    }
}
