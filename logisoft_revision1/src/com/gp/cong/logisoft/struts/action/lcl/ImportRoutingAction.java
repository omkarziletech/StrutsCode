package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cvst.logisoft.struts.form.lcl.ImportRoutingForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author meiyazhakan.r
 */
public class ImportRoutingAction extends LogiwareDispatchAction {

    private LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportRoutingForm importRoutingForm = (ImportRoutingForm) form;
        LclBookingPad lclBookingPad = lclBookingPadDAO.getByProperty("lclFileNumber.id", Long.parseLong(importRoutingForm.getFileId()));
        if(lclBookingPad!=null && lclBookingPad.getPickupCutoffDate()!=null){
            importRoutingForm.setCutOff(DateUtils.formatDate(lclBookingPad.getPickupCutoffDate(), "dd-MMM-yyyy"));
        }
         if(lclBookingPad!=null && lclBookingPad.getPickupReadyDate()!=null){
            importRoutingForm.setReadyDate(DateUtils.formatDate(lclBookingPad.getPickupReadyDate(), "dd-MMM-yyyy"));
        }
        request.setAttribute("lclBookingPad", lclBookingPad);
        request.setAttribute("importRoutingForm", importRoutingForm);
        return mapping.findForward("routingInstruction");
    }

    public ActionForward savePickUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportRoutingForm importRoutingForm = (ImportRoutingForm) form;
        Date now = new Date();
        LclBookingPad lclBookingPad = lclBookingPadDAO.getByProperty("lclFileNumber.id", Long.parseLong(importRoutingForm.getFileId()));
        if (lclBookingPad == null) {
            lclBookingPad = new LclBookingPad();
            lclBookingPad.setIssuingTerminal(getCurrentUser(request).getTerminal());
            lclBookingPad.setEnteredBy(getCurrentUser(request));
            lclBookingPad.setEnteredDatetime(now);
            lclBookingPad.setDeliveryContact(null);
            lclBookingPad.setPickupContact(null);
        }
        lclBookingPad.setLclFileNumber(new LclFileNumber(Long.parseLong(importRoutingForm.getFileId())));
        lclBookingPad.setDeliveryHours(CommonUtils.isNotEmpty(importRoutingForm.getShipperHours()) ? importRoutingForm.getShipperHours() : null);
        lclBookingPad.setPickupReadyNote(CommonUtils.isNotEmpty(importRoutingForm.getReadyNote()) ? importRoutingForm.getReadyNote() : null);
        lclBookingPad.setPickupCutoffDate(CommonUtils.isNotEmpty(importRoutingForm.getCutOff()) ? DateUtils.parseDate(importRoutingForm.getCutOff(), "dd-MMM-yyyy") : null);
        lclBookingPad.setPickUpTo(CommonUtils.isNotEmpty(importRoutingForm.getSpecialInstructions()) ? importRoutingForm.getSpecialInstructions() : null);
        lclBookingPad.setPickupReadyDate(CommonUtils.isNotEmpty(importRoutingForm.getReadyDate()) ? DateUtils.parseDate(importRoutingForm.getReadyDate(), "dd-MMM-yyyy") : null);
        lclBookingPad.setTermsOfService(CommonUtils.isNotEmpty(importRoutingForm.getTermsOfSale())? importRoutingForm.getTermsOfSale() : null);
        lclBookingPad.setModifiedBy(getCurrentUser(request));
        lclBookingPad.setModifiedDatetime(now);
        lclBookingPadDAO.saveOrUpdate(lclBookingPad);
        request.setAttribute("importRoutingForm", importRoutingForm);
        request.setAttribute("lclBookingPad", lclBookingPadDAO.getByProperty("lclFileNumber.id", Long.parseLong(importRoutingForm.getFileId())));
        return mapping.findForward("routingInstruction");
    }
}
