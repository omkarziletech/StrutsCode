/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.struts.form.lcl.LclBookingPlanForm;
import java.util.Date;

/**
 *
 * @author Owner
 */
public class LclBookingPlanAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBookingPlanForm lclPlanForm = (LclBookingPlanForm) form;
        Long fileNumberId=new Long(request.getParameter("fileNumberId"));
        request.setAttribute("bookingPlanList", new LclBookingPlanDAO().findByProperty("lclFileNumber.id", fileNumberId));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward movePlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBookingPlanForm lclPlanForm = (LclBookingPlanForm) form;
        lclPlanForm.getLclBookingPlan().setEnteredBy(getCurrentUser(request));
        lclPlanForm.getLclBookingPlan().setModifiedBy(getCurrentUser(request));
        lclPlanForm.getLclBookingPlan().setEnteredDatetime(new Date());
        lclPlanForm.getLclBookingPlan().setModifiedDatetime(new Date());
        lclPlanForm.getLclBookingPlan().setFromId(new UnLocation(lclPlanForm.getFromId()));
        lclPlanForm.getLclBookingPlan().setToId(new UnLocation(lclPlanForm.getToId()));
        request.setAttribute("lclCommodityList", new LclBookingPlanDAO().findByProperty("lclFileNumber.id", lclPlanForm.getFileNumberId()));
        return mapping.findForward(SUCCESS);
    }
}
