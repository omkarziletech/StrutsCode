package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.bc.print.PrintConfigBC;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cvst.logisoft.struts.form.lcl.LclPrintForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LclPrintAction extends LogiwareDispatchAction {

    User user = null;

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        String screenName = null;
        user = (User) session.getAttribute("loginuser");
        LclPrintForm lclPrintForm = (LclPrintForm) form;
        PrintConfigBC printConfigBC = new PrintConfigBC();
        //  request.setAttribute("lclPrintList", new LclRemarksDAO().executeQuery("from LclRemarks where lclFileNumber.id= " + lclPrintForm.getFileNumberId()));
        request.setAttribute("fileNumber", lclPrintForm.getFileNumber());
        request.setAttribute("fileID", request.getParameter("fileNumberId"));
        // request.setAttribute("lclPrintForm", lclPrintForm);
        if (!lclSession.getSelectedMenu().equals("Imports")) {

            if (lclPrintForm.getModuleId().equalsIgnoreCase(LclReportConstants.SCREENNAME_QUOTEREPORT)) {
                screenName = lclPrintForm.getModuleId();
            }
            if (lclPrintForm.getModuleId().equalsIgnoreCase(LclReportConstants.SCREENNAME_BOOKINGREPORT)) {
                screenName = lclPrintForm.getModuleId();
            }
            if (lclPrintForm.getModuleId().equalsIgnoreCase(LclReportConstants.SCREENNAME_BLREPORT)) {
                screenName = lclPrintForm.getModuleId();
            }
            if (lclPrintForm.getModuleId().equalsIgnoreCase(LclReportConstants.LCL_UNITS)) {
                screenName = lclPrintForm.getModuleId();
            }
            request.setAttribute(CommonConstants.LCL_PRINT_LIST, printConfigBC.findPrintConfigByScreenNameForExport(screenName, null, null, user.getUserId()));
        } else {
            if (lclPrintForm.getModuleId().equalsIgnoreCase(LclReportConstants.SCREENNAME_BLREPORT)) {
                screenName = "LCLIMPORTSBL";
            }
            request.setAttribute(CommonConstants.LCL_PRINT_LIST, printConfigBC.findPrintConfigByScreenNameForExport(screenName, null, null, user.getUserId()));
        }
        return mapping.findForward("lclPrintFax");
    }
}
