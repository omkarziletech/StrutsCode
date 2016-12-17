/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cvst.logisoft.struts.form.lcl.LclExportStopOffForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author aravindhan.v
 */
public class LclExportStopOffAction extends LogiwareDispatchAction {

    public ActionForward displayStopOff(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclExportStopOffForm lclExportStopOffForm = (LclExportStopOffForm) form;
        request.setAttribute("lclExportStopOffForm", lclExportStopOffForm);
        return mapping.findForward("displayStopOff");
    }
}
