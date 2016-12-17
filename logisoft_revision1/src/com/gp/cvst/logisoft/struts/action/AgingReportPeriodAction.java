/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.struts.form.AgingReportPeriodsForm;

/** 
 * MyEclipse Struts
 * Creation date: 08-30-2008
 * 
 * XDoclet definition:
 * @struts.action path="agingreportPeriod" name="agingreportPeriod" input="/jsps/AgingReports/AgingReport.jsp" scope="request"
 * @struts.action-forward name="success" path="/jsps/AgingReports/AgingReport.jsp"
 */
public class AgingReportPeriodAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)throws Exception {
        AgingReportPeriodsForm agingreportPeriodform = (AgingReportPeriodsForm) form;// TODO Auto-generated method stub
        String buttonValue = agingreportPeriodform.getButtonValue();
        HttpSession session = request.getSession(true);
        String forwardName = "";
        String custName = agingreportPeriodform.getCustomerName();
        String custNo = agingreportPeriodform.getCustomerNumber();
        String custAddress = agingreportPeriodform.getCustomerAddress();
        String terminal = agingreportPeriodform.getTerminal();
        String collector = agingreportPeriodform.getCollector();
        String company = agingreportPeriodform.getCompany();
        String ageingzeero = agingreportPeriodform.getAgingzeero();
        String ageingthirty = agingreportPeriodform.getAgingthirty();
        String greaterthanthirty = agingreportPeriodform.getGreaterthanthirty();
        String agingsixty = agingreportPeriodform.getAgingsixty();
        String greaterthansixty = agingreportPeriodform.getGreaterthansixty();
        String agingninty = agingreportPeriodform.getAgingninty();
        String greaterthanninty = agingreportPeriodform.getGreaterthanninty();
        String nintyplus = agingreportPeriodform.getNintyplus();
        String allCust = agingreportPeriodform.getAllcustomers();
        String overdue = agingreportPeriodform.getOverdue();
        String minamt = agingreportPeriodform.getMinamt();
        return mapping.findForward(forwardName);
    }

    public void redirectToReport(HttpServletRequest request, HttpServletResponse response)throws Exception {
            response.sendRedirect(request.getContextPath() + "/report");
    }
}