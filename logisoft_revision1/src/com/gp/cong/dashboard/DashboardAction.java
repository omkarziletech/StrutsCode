package com.gp.cong.dashboard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.bc.dashboard.DashboardBC;

public class DashboardAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DashboardBC dashboardBC = new DashboardBC();
        request.setAttribute("data", dashboardBC.getAllOperationDataWarehouseCountForLatestMonthXML(12));
        return mapping.findForward("success");
    }
}
