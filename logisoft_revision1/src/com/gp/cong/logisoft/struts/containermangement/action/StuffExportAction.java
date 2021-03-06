 /*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.containermangement.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.struts.containermangement.form.StuffExportForm;
import com.gp.cvst.logisoft.domain.Dockreceipt;
import com.gp.cvst.logisoft.hibernate.dao.DockreceiptDAO;

/** 
 * MyEclipse Struts
 * Creation date: 09-22-2008
 * 
 * XDoclet definition:
 * @struts.action path="/stuffexport" name="stuffexportform" input="/jsps/Containermanagement/StuffExport.jsp" scope="request"
 * @struts.action-forward name="success" path="/jsps/Containermanagement/StuffExport.jsp"
 */
public class StuffExportAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        StuffExportForm stuffexportform = (StuffExportForm) form;// TODO Auto-generated method stub
        String forwardName = "";
        HttpSession session = request.getSession();
        String buttonValue = stuffexportform.getButtonValue();
        int flagvalue = stuffexportform.getFlagvalue();
        Integer docreceipt = stuffexportform.getDocreceipt();
        String unitNumber = stuffexportform.getUnitNumber();
        String ParamId = "";
        String UnitNum = (String) session.getAttribute("unitnumber");
        if (request.getParameter("paramid") != null) {
            String FlagStatus = "loaded";

            ParamId = (String) request.getParameter("paramid");
            Dockreceipt docr = new Dockreceipt();
            DockreceiptDAO dockrecDAO = new DockreceiptDAO();

            if (ParamId != null && !ParamId.equals("0")) {
                dockrecDAO.updateStatus(ParamId, FlagStatus, UnitNum);
            }
            forwardName = "success";
        } else if (request.getParameter("paramid1") != null) {
            String FlagStatus = "released";

            ParamId = (String) request.getParameter("paramid1");
            DockreceiptDAO dockrecDAO = new DockreceiptDAO();
            UnitNum = "";
            if (ParamId != null && !ParamId.equals("0")) {
                dockrecDAO.updateStatus(ParamId, FlagStatus, UnitNum);
            }
            forwardName = "success";
        } else if (buttonValue.equals("save")) {
            forwardName = "success";
        }
        return mapping.findForward(forwardName);
    }
}