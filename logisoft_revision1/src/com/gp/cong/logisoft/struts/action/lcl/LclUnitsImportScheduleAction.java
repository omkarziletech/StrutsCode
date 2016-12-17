/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitsScheduleForm;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Logiware
 */
public class LclUnitsImportScheduleAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception, SQLException {
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        String module = lclSession.getSelectedMenu();
        LclUnitsScheduleForm lclUnitsScheduleForm = (LclUnitsScheduleForm) form;
        request.setAttribute("filterValues", lclUnitsScheduleForm.getFilterByChanges());
        return mapping.findForward("displaySchedule");
    }
}
