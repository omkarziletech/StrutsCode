    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPad;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclBlPickupInfoForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclBlPickupInfoAction extends LogiwareDispatchAction {

    private static final String CARRIER = "carrier";
    private static final String PICKUPCHARGE = "pickupCharge";
    private LclBlPadDAO lclBlPadDAO = new LclBlPadDAO();
    private LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlPickupInfoForm lclBlPickupInfoForm = (LclBlPickupInfoForm) form;
        request.setAttribute("doorOriginCityZip", request.getParameter("doorOriginCityZip"));
        request.setAttribute("pickupCharge", lclBlPickupInfoForm.getLclBlPad().getChargeAmount());
        request.setAttribute("scac", lclBlPickupInfoForm.getLclBlPad().getScac());
        String fileNumberId = request.getParameter("fileNumberId");
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclBlAc lclBlAc = lclBlAcDAO.getLclCostByChargeCode(Long.parseLong(fileNumberId), "0012");
            request.setAttribute("lclBlAc", lclBlAc);
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlPickupInfoForm lclBlPickupInfoForm = (LclBlPickupInfoForm) form;
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(lclBlPickupInfoForm.getFileNumberId());
        LclBlPad lclBlPad = lclBlPickupInfoForm.getLclBlPad();
        LclBlAc lclBlAc = lclBlAcDAO.getLclCostByChargeCode(lclBlPickupInfoForm.getFileNumberId(), "0012");
        if (lclBlAc != null && lclBlPad != null) {
            if (!lclBlAc.getArAmount().equals(lclBlPad.getChargeAmount())) {
                lclBlAc.setManualEntry(true);
                lclBlAc.setArAmount(lclBlPad.getChargeAmount());
            }
            lclBlAc.setBundleIntoOf(false);
            lclBlAc.setPrintOnBl(true);
            lclBlAcDAO.update(lclBlAc);
        }
        lclBlPad.setLclFileNumber(lclFileNumber);
        lclBlPad.setEnteredBy(getCurrentUser(request));
        lclBlPad.setModifiedBy(getCurrentUser(request));
        lclBlPad.setEnteredDatetime(new Date());
        lclBlPad.setModifiedDatetime(new Date());
        lclBlPad.getDeliveryContact().setLclFileNumber(lclFileNumber);
        this.setUserAndDateTime(lclBlPickupInfoForm.getLclBlPad().getDeliveryContact(), request);
        lclBlPickupInfoForm.getLclBlPad().getPickupContact().setLclFileNumber(lclFileNumber);
        this.setUserAndDateTime(lclBlPickupInfoForm.getLclBlPad().getPickupContact(), request);
        lclBlPadDAO.saveOrUpdate(lclBlPad);
        request.setAttribute("lclBlPad", lclBlPad);
        request.setAttribute("fileNumberId", lclBlPickupInfoForm.getLclBlPad().getLclFileNumber().getId());
        request.setAttribute("fileNumber", lclBlPickupInfoForm.getLclBlPad().getLclFileNumber().getFileNumber());
        request.setAttribute("pickupCharge", lclBlPickupInfoForm.getChargeAmount());
        return mapping.findForward(SUCCESS);
    }

    public void setUserAndDateTime(LclContact lclContact, HttpServletRequest request) {
        if (lclContact.getEnteredBy() == null || lclContact.getEnteredDatetime() == null) {
            lclContact.setEnteredBy(getCurrentUser(request));
            lclContact.setEnteredDatetime(new Date());
        }
        lclContact.setModifiedBy(getCurrentUser(request));
        lclContact.setModifiedDatetime(new Date());
    }

    public ActionForward carrier(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlPickupInfoForm lclBlPickupInfoForm = (LclBlPickupInfoForm) form;
        HttpSession session = request.getSession();
        String toZip = "";
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setXmlObjMap(null);
        lclSession.setCarrierList(null);
        session.setAttribute("lclSession", lclSession);
        String fromZip = request.getParameter("fromZip");
        String sailDate = request.getParameter("sailDate");
        String fileNumberId = request.getParameter("fileNumberId");
        String originUnLocation = request.getParameter("originUnLocation");
        String rateType = request.getParameter("rateType");
        if (rateType != null && rateType.equalsIgnoreCase("R")) {
            rateType = "Y";
        }
        RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(originUnLocation, rateType);
        if (terminal != null) {
            toZip = terminal.getZipcde();
        }
        BigDecimal weight = new BigDecimal(0.000);
        BigDecimal measure = new BigDecimal(0.000);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            List<LclBlPiece> commList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            for (LclBlPiece lbp : commList) {
                if (lbp.getActualWeightImperial() != null) {
                    weight = weight.add(lbp.getActualWeightImperial());
                } else if (lbp.getBookedWeightImperial() != null) {
                    weight = weight.add(lbp.getBookedWeightImperial());
                }
                if (lbp.getActualVolumeImperial() != null) {
                    measure = measure.add(lbp.getActualVolumeImperial());
                } else if (lbp.getBookedVolumeImperial() != null) {
                    measure = measure.add(lbp.getBookedVolumeImperial());
                }
            }
        }
        String realPath = session.getServletContext().getRealPath("/xml/");
        String fileName = "ctsresponse" + session.getId() + ".xml";
        CallCTSWebServices ctsweb = new CallCTSWebServices();
        lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip, toZip, sailDate,
                "" + weight, "" + measure, null, "CARRIER_COST", "Exports");
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("fileNumberId", fileNumberId);
        return mapping.findForward(CARRIER);
    }

    /*
    public ActionForward addPickupCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    LclPickupInfoForm lclPickupInfoForm = (LclPickupInfoForm) form;
    String fileNumberId = request.getParameter("fileNumberId");
    String scac = request.getParameter("scac");
    String pickupCharge = request.getParameter("pickupCharge");
    if (CommonUtils.isNotEmpty(fileNumberId)) {
    List<LclBookingPad> padList = lclBookingPadDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
    if (CommonUtils.isNotEmpty(padList)) {
    LclBookingPad lclBookingPad = padList.get(0);
    if (CommonUtils.isNotEmpty(pickupCharge)) {
    lclBookingPad.setChargeAmount(new BigDecimal(pickupCharge));
    lclBookingPad.setPickupAcct(new TradingPartnerDAO().findById("CAPTRA0002"));
    lclBookingPadDAO.update(lclBookingPad);
    }
    }
    }
    request.setAttribute("scac", scac);
    request.setAttribute("pickupCharge", pickupCharge);
    return mapping.findForward(PICKUPCHARGE);

    }*/
    public ActionForward addPickupChargeToAcct(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        String scac = request.getParameter("scac");
        String pickupCharge = request.getParameter("pickupCharge");
        if (CommonUtils.isNotEmpty(fileNumberId) && CommonUtils.isNotEmpty(pickupCharge)) {
            TradingPartner spRefNumber = new TradingPartnerDAO().findById("CTSFRE0004");
            LclBlAc lclBlAc = null != lclBlAcDAO.getLclCostByChargeCode(Long.parseLong(fileNumberId), "0012") ? lclBlAcDAO.getLclCostByChargeCode(Long.parseLong(fileNumberId), "0012") : new LclBlAc();
            lclBlAc.setLclFileNumber(new LclFileNumber(Long.parseLong(fileNumberId)));
            lclBlAc.setArAmount(new BigDecimal(pickupCharge));
            GlMapping gp = new GlMappingDAO().findByBlueScreenChargeCode("0012", "LCLE", "AR");
            lclBlAc.setArglMapping(gp);
            lclBlAc.setSupAcct(spRefNumber);
            lclBlAc.setTransDatetime(new Date());
            lclBlAc.setManualEntry(false);
            lclBlAc.setRatePerUnitUom("FL");
            lclBlAc.setRateUom("I");
            lclBlAc.setAdjustmentAmount(new BigDecimal(0.00));
            lclBlAc.setEnteredBy(getCurrentUser(request));
            lclBlAc.setModifiedBy(getCurrentUser(request));
            lclBlAc.setEnteredDatetime(new Date());
            lclBlAc.setModifiedDatetime(new Date());
            lclBlAc.setBundleIntoOf(false);
            lclBlAc.setPrintOnBl(true);
            lclBlAcDAO.saveOrUpdate(lclBlAc);
            LclBlPad lclBlPad = new LclBlPadDAO().executeUniqueQuery("from LclBlPad where lclFileNumber.id=" + Long.parseLong(fileNumberId));
            request.setAttribute("lclBlPad", lclBlPad);
            request.setAttribute("lclBlAc", lclBlAc);
        }
        request.setAttribute("scac", scac);
        request.setAttribute("pickupCharge", pickupCharge);
        return mapping.findForward(PICKUPCHARGE);
    }
}
