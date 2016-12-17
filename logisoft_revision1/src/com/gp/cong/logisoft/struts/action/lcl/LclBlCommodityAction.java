/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.google.gson.Gson;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.BlUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.dwr.LclBlChargesCalculation;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.BlCommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlPieceDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclBlCommodityForm;
import com.logiware.common.filter.JspWrapper;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Owner
 */
public class LclBlCommodityAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static String COMMODITY_DESC = "commodityDesc";
    private static String SHOW_CARGO = "showCargo";
    private static String SUCCESS = "success";

    /**
     * This method is used to set the commodity List in the session ,we will
     * save the commodity while saving the booking
     *
     * @param mapping
     * @return
     * @throws Exception
     */
    public ActionForward saveOrUpdateCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            Map<String, String> result = new HashMap<String, String>();
            response.setContentType("application/json");
            out = response.getWriter();
            Date d = new Date();
            LclBlCommodityForm lclBlCommodityForm = (LclBlCommodityForm) form;
            LclBl lclBl = new LCLBlDAO().getByProperty("lclFileNumber.id", lclBlCommodityForm.getFileNumberId());
            LclBLPieceDAO lclBLPieceDAO = new LclBLPieceDAO();
            User loginUser = getCurrentUser(request);
            LclBlPiece lclBlPiece = lclBlCommodityForm.getLclBlPiece();
            lclBlPiece.setModifiedBy(loginUser);
            lclBlPiece.setModifiedDatetime(d);
            lclBLPieceDAO.saveOrUpdate(lclBlPiece);
            new LCLBlDAO().updateModifiedDate(lclBlCommodityForm.getFileNumberId(), loginUser.getUserId());
            List<LclBlPiece> lclBlPieceList = lclBLPieceDAO.findByProperty("lclFileNumber.id", lclBlCommodityForm.getFileNumberId());
            request.setAttribute("lclCommodityList", lclBlPieceList);
            JspWrapper jspWrapper = new JspWrapper(response);
            List consolidateList = new LclConsolidateDAO().getConsolidatesFiles(lclBlPiece.getLclFileNumber().getId());
            consolidateList.add(lclBlPiece.getLclFileNumber().getId());
            request.setAttribute("hazmatFlag", new LclBlHazmatDAO().isCheckedHazmat(consolidateList));
            request.getRequestDispatcher("/jsps/LCL/commodityBlDesc.jsp").include(request, jspWrapper);
            result.put("commodityDesc", jspWrapper.getOutput());
            if (lclBlCommodityForm.isRatesRecalFlag()) {
                calculateRates(lclBlCommodityForm, lclBlPieceList, loginUser, request);
                jspWrapper = new JspWrapper(response);
                request.getRequestDispatcher("/jsps/LCL/ajaxload/chargeBlDesc.jsp").include(request, jspWrapper);
                result.put("chargeDesc", jspWrapper.getOutput());
            }
            Gson gson = new Gson();
            out.print(gson.toJson(result));
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    public void calculateRates(LclBlCommodityForm lclBlCommodityForm, List<LclBlPiece> lclBlPieceList,
            User loginUser, HttpServletRequest request) throws Exception {
        LclBl lclBl = new LCLBlDAO().getByProperty("lclFileNumber.id", lclBlCommodityForm.getFileNumberId());
        if (!lclBl.isFreeBL()) {
            String insurance = lclBlCommodityForm.getInsurance();
            BigDecimal valueOfGoods = new BigDecimal(0.00);
            if (CommonUtils.isNotEmpty(lclBlCommodityForm.getValueOfGoods())) {
                valueOfGoods = new BigDecimal(lclBlCommodityForm.getValueOfGoods());
            }
            String pcBoth = request.getParameter("pcBoth");
            boolean calcHeavy = false;
            if (CommonUtils.isNotEmpty(lclBlCommodityForm.getCalcHeavy()) && lclBlCommodityForm.getCalcHeavy().equalsIgnoreCase("Y")) {
                calcHeavy = true;
            }
            BlUtils blUtils = new BlUtils();
            String rateType = "R".equalsIgnoreCase(lclBlCommodityForm.getRateType()) ? "Y" : lclBlCommodityForm.getRateType();
            LclBlChargesCalculation lclBlChargesCalculation = new LclBlChargesCalculation();
            // Calculating imports rates.
            List<LclBlAc> blAcList = blUtils.getTransshipmentRaterForBl(lclBl, loginUser, request);
            if (CommonUtils.isEmpty(blAcList)) {
                lclBlChargesCalculation.calculateRates(lclBlCommodityForm.getPooUnlocCode(), lclBlCommodityForm.getFdUnlocCode(),
                        lclBlCommodityForm.getPolUnlocCode(), lclBlCommodityForm.getPodUnlocCode(), lclBlCommodityForm.getFileNumberId(),
                        lclBlPieceList, loginUser, null, insurance, valueOfGoods, rateType,
                        "C", null, null, null, null, calcHeavy, lclBl.getDeliveryMetro(), pcBoth, null, lclBl.getBillToParty(), request, false, false);
            }
            if (lclBl.getSpotRate() && lclBlPieceList.size() == 1) {
                String billingType = lclBl.getBillingType();
                String CFT = null != lclBl.getSpotWmRate() ? lclBl.getSpotWmRate().toString() : "";
                String CBM = null != lclBl.getSpotRateMeasure() ? lclBl.getSpotRateMeasure().toString() : "";
                Boolean spotCheckBottom = lclBl.isSpotRateBottom();
                Boolean isOnlyOcnfrt = lclBl.isSpotOfRate();
                String spotComment = lclBl.getSpotComment();
                MessageResources messageResources = getResources(request);
                String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
                blUtils.calculateSpotRate(lclBlCommodityForm.getFileNumberId(), lclBl, billingType, CBM, CFT, rateType, isOnlyOcnfrt,
                        spotCheckBottom, spotComment, spotRateCommodity, request);
            } else {
                List<LclBlAc> lclBlAcList = new LclBlAcDAO().getLclCostByFileNumberAsc(lclBlCommodityForm.getFileNumberId());
                String engmet = new PortsDAO().getPortValue(PORTS_ENGMET, lclBlCommodityForm.getFdUnlocCode());
                blUtils.setWeighMeasureForBl(request, lclBlPieceList, engmet);
                blUtils.setRolledUpChargesForBl(lclBlAcList, request, lclBlCommodityForm.getFileNumberId(),
                        new LclBlAcDAO(), lclBlPieceList, pcBoth, engmet, lclBl);
            }
        }
        if (!lclBl.getBillingType().equals("P")) {
            request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking()); //for Freight Agent Acct
        }
        request.setAttribute("lclBl", lclBl);
        setWeightMeasureDetails(lclBlPieceList, lclBl, request);
    }

    public void setWeightMeasureDetails(List<LclBlPiece> lclCommodityList, LclBl lclBl,
            HttpServletRequest request) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        Ports ports = null;
        String ofratebasis = "";
        String rateType = "R".equalsIgnoreCase(lclBl.getRateType()) ? "Y" : lclBl.getRateType();
        RefTerminal refterminal = new RefTerminalDAO().getTerminalByUnLocation(lclBl.getPortOfOrigin().getUnLocationCode(), rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            ofratebasis = refterminal.getTrmnum() + "-";
        }
        if (lclBl.getFinalDestination() != null) {
            ports = portsDAO.getByProperty("unLocationCode", lclBl.getFinalDestination().getUnLocationCode());
        } else {
            ports = portsDAO.getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
        }
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            ofratebasis += ports.getEciportcode() + "-";
        }
        if (lclCommodityList != null && lclCommodityList.size() > 0) {
            LclBlPiece lclBlPiece = lclCommodityList.get(0);
            ofratebasis += lclBlPiece.getCommodityType().getCode();
            if (lclBlPiece.getStdchgRateBasis() != null && !lclBlPiece.getStdchgRateBasis().trim().equals("")) {
                request.setAttribute("stdchgratebasis", lclBlPiece.getStdchgRateBasis());
            }
            request.setAttribute("ofratebasis", ofratebasis);
        }
    }

    public ActionForward addCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCommodityForm lclBlCommodityForm = (LclBlCommodityForm) form;
        boolean editDimFlag = false;
        String dimFlag = request.getParameter("editDimFlag");
        if (request.getParameter("editDimFlag") != null && dimFlag.equals("true")) {
            editDimFlag = true;
        }
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List<LclBlPiece> blcommodityList = null != lclSession.getBlCommodityList() ? lclSession.getBlCommodityList() : new ArrayList<LclBlPiece>();
        LclBlPiece lclBlPiece = lclBlCommodityForm.getLclBlPiece();
        if (lclBlPiece == null) {
            lclBlPiece = new LclBlPiece();
        }
        if (lclBlPiece.getEnteredBy() == null || lclBlPiece.getEnteredDatetime() == null) {
            lclBlPiece.setEnteredBy(getCurrentUser(request));
            lclBlPiece.setEnteredDatetime(new Date());
        }
        if (editDimFlag) {
            blcommodityList.set(Integer.parseInt(request.getParameter("id")), lclBlPiece);
        } else {
            blcommodityList.add(lclBlPiece);
        }
        lclBlPiece.setModifiedBy(getCurrentUser(request));
        lclBlPiece.setModifiedDatetime(new Date());
        lclSession.setBlCommodityList(blcommodityList);
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("lclCommodityList", blcommodityList);
        return mapping.findForward(COMMODITY_DESC);
    }

    public ActionForward addLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCommodityForm lclBlCommodityForm = (LclBlCommodityForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setCommodityList(null);
        session.setAttribute("lclSession", lclSession);
        LclBlPiece lclBlPiece = lclBlCommodityForm.getLclBlPiece();
        if (lclBlPiece == null) {
            lclBlPiece = new LclBlPiece();
        }
        if (lclBlPiece.getEnteredBy() == null || lclBlPiece.getEnteredDatetime() == null) {
            lclBlPiece.setEnteredBy(getCurrentUser(request));
            lclBlPiece.setEnteredDatetime(new Date());
        }
        lclBlPiece.setModifiedBy(getCurrentUser(request));
        lclBlPiece.setModifiedDatetime(new Date());
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(lclBlCommodityForm.getFileNumberId());
        if (lclFileNumber.getStatus().equalsIgnoreCase("B")) {
            if (CommonUtils.isEmpty(lclBlPiece.getBookedPieceCount())) {
                lclBlPiece.setBookedPieceCount(lclBlPiece.getActualPieceCount());
            }
            if (lclBlPiece.getBookedVolumeImperial() == null) {
                lclBlPiece.setBookedVolumeImperial(lclBlPiece.getActualVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (lclBlPiece.getBookedVolumeMetric() == null) {
                lclBlPiece.setBookedVolumeMetric(lclBlPiece.getActualVolumeMetric());
            }
            if (lclBlPiece.getBookedWeightImperial() == null) {
                lclBlPiece.setBookedWeightImperial(lclBlPiece.getActualWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (lclBlPiece.getBookedWeightMetric() == null) {
                lclBlPiece.setBookedWeightMetric(lclBlPiece.getActualWeightMetric());
            }
        }
        new LclBLPieceDAO().saveAndReturn(lclBlPiece);
        //save the lcl_booking_piece_details
        List deleteList = new LclBlPieceDetailDAO().findByProperty("lclBlPiece.id", lclBlPiece.getId());
        for (Object obj : deleteList) {
            LclBlPieceDetail lbpd = (LclBlPieceDetail) obj;
            new LclBlPieceDetailDAO().delete(lbpd);
        }
        List dimList = null != lclSession.getBlDetailList() ? lclSession.getBlDetailList() : new LinkedList();
        for (Object obj : dimList) {
            LclBlPieceDetail lbpd = (LclBlPieceDetail) obj;
            lbpd.setLclBlPiece(lclBlPiece);
            new LclBlPieceDetailDAO().save(lbpd);
        }
        List<LclBlPiece> lclBlPieceList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBlCommodityForm.getFileNumberId());
        request.setAttribute("lclCommodityList", lclBlPieceList);
        return mapping.findForward(COMMODITY_DESC);
    }

    public ActionForward modifyMinimumRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCommodityForm lclBlCommodityForm = (LclBlCommodityForm) form;
        BlUtils blUtils = new BlUtils();
        LCLBlDAO lCLBlDAO = new LCLBlDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        String deliveryMetro = request.getParameter("deliveryMetro");
        String insurance = lclBlCommodityForm.getInsurance();
        BigDecimal valueOfGoods = new BigDecimal(0.00);
        if (CommonUtils.isNotEmpty(lclBlCommodityForm.getValueOfGoods())) {
            valueOfGoods = new BigDecimal(lclBlCommodityForm.getValueOfGoods());
        }
        String pcBoth = request.getParameter("pcBoth");
        List<LclBlPiece> lclBlPieceList = null;
        LclBlPiece lclBlPiece = lclBlCommodityForm.getLclBlPiece();
        LclBlChargesCalculation lclBlChargesCalculation = new LclBlChargesCalculation();
        String rateType = lclBlCommodityForm.getRateType();
        if (lclBlCommodityForm.getRateType() != null && !lclBlCommodityForm.getRateType().trim().equals("")) {
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
        }
        String fileId = request.getParameter("fileNumberId");
        Long fileNumberId = Long.parseLong(fileId);
        boolean calcHeavy = false;
        if (CommonUtils.isNotEmpty(lclBlCommodityForm.getCalcHeavy()) && lclBlCommodityForm.getCalcHeavy().equalsIgnoreCase("Y")) {
            calcHeavy = true;
        }
        lclBlPieceList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBlCommodityForm.getFileNumberId());
        LclBl lclBl = lCLBlDAO.getByProperty("lclFileNumber.id", fileNumberId);
        lclBlChargesCalculation.calculateRates(lclBlCommodityForm.getPooUnlocCode(), lclBlCommodityForm.getFdUnlocCode(),
                lclBlCommodityForm.getPolUnlocCode(), lclBlCommodityForm.getPodUnlocCode(), fileNumberId,
                lclBlPieceList, user, null, insurance, valueOfGoods, rateType,
                "C", null, null, null, null, calcHeavy, lclBl.getDeliveryMetro(), pcBoth, null, null, request, false, false);
        List<LclBlAc> lclBlAcList = new LclBlAcDAO().getLclCostByFileNumberAsc(lclBlCommodityForm.getFileNumberId());
        blUtils.setWeighMeasureForBl(request, lclBlPieceList, "");
        blUtils.setRolledUpChargesForBl(lclBlAcList, request, fileNumberId, new LclBlAcDAO(), lclBlPieceList, pcBoth, lclBlChargesCalculation.getPorts().getEngmet(), lclBl);
        lclBlPiece.setModifiedBy(getCurrentUser(request));
        lclBlPiece.setModifiedDatetime(new Date());
        request.setAttribute("lclCommodityList", lclBlPieceList);
        return mapping.findForward("chargeBlDesc");
    }

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCommodityForm lclBlCommodityForm = (LclBlCommodityForm) form;
        String tabFlag = request.getParameter("tabFlag");
        String fileNumberId = request.getParameter("fileNumberId");
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setBlDetailList(null);
        session.setAttribute("lclSession", lclSession);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            if (!tabFlag.equals("false")) {
                request.setAttribute("lclCommodityList", new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId)));
            }
        }
        request.setAttribute("editDimFlag", request.getParameter("editDimFlag"));
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            request.setAttribute("bookingExport", bookingExport);
        }
        lclBlCommodityForm.setBlPieceId("");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward showCargo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCommodityForm lclBlCommodityForm = (LclBlCommodityForm) form;
        return mapping.findForward(SHOW_CARGO);
    }

    public ActionForward deleteLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCommodityForm lclBlCommodityForm = (LclBlCommodityForm) form;
        String fileNumberId = request.getParameter("filenumberId");
        String commodityId = request.getParameter("commId");
        Long id = Long.parseLong(commodityId);
        LclBlPiece lclBlPiece = new LclBLPieceDAO().findById(id);
        new LclBLPieceDAO().deleteNotesForCommodity(Long.parseLong(fileNumberId), getCurrentUser(request).getUserId());
        new LclBLPieceDAO().delete(lclBlPiece);
        request.setAttribute("lclBlCommodityForm", lclBlCommodityForm);
        request.setAttribute("lclCommodityList", new LclBLPieceDAO().findByProperty("lclFileNumber.id", new Long(fileNumberId)));
        return mapping.findForward(COMMODITY_DESC);

    }

    public ActionForward editLclCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCommodityForm lclBlCommodityForm = (LclBlCommodityForm) form;
        LclBlPiece lclBlPiece = lclBlCommodityForm.getLclBlPiece();
        LclBookingExport bookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclBlPiece.getLclFileNumber().getId());
        if (CommonUtils.isNotEmpty(lclBlPiece.getActualPieceCount()) && lclBlPiece.getActualPieceCount() > 1) {
            lclBlCommodityForm.setPackageType(lclBlPiece.getPackageType().getDescription() + "" + lclBlPiece.getPackageType().getPlural().toLowerCase());
        } else {
            lclBlCommodityForm.setPackageType(lclBlPiece.getPackageType().getDescription());
        }
        request.setAttribute("commCode", lclBlPiece.getCommodityType().getCode());
        request.setAttribute("lclBlCommodityForm", lclBlCommodityForm);
        request.setAttribute("editDimFlag", request.getParameter("editDimFlag"));
        request.setAttribute("actualPieceCount", lclBlPiece.getActualPieceCount());
        request.setAttribute("actualWeightImp", lclBlPiece.getActualWeightImperial() != null ? lclBlPiece.getActualWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBlPiece.getActualWeightImperial());
        request.setAttribute("actualWeightMet", lclBlPiece.getActualWeightMetric());
        request.setAttribute("actualMeasureImp", lclBlPiece.getActualVolumeImperial() != null ? lclBlPiece.getActualVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : lclBlPiece.getActualVolumeImperial());
        request.setAttribute("actualMeasureMet", lclBlPiece.getActualVolumeMetric());
        String data[] = new BlCommodityDetailsDAO().displayBlDimsDetails(lclBlCommodityForm.getBlPieceId());
        lclBlCommodityForm.setDimsToolTip(data[0]);
        request.setAttribute("bookingExport", bookingExport);
        request.setAttribute("lclBlPiece", lclBlPiece);
        return mapping.findForward(SUCCESS);
    }
}
