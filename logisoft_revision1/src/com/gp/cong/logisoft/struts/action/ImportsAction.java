/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.ImportPortConfiguration;
import com.gp.cong.logisoft.struts.form.ImportsForm;

/** 
 * MyEclipse Struts
 * Creation date: 01-05-2008
 * 
 * XDoclet definition:
 * @struts.action path="/imports" name="importsForm" input="/jsps/datareference/imports.jsp" scope="request" validate="true"
 */
public class ImportsAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response) {
        ImportsForm importsForm = (ImportsForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();

        ImportPortConfiguration importObj = null;
        if (session.getAttribute("importPortObj") != null) {
            importObj = (ImportPortConfiguration) session.getAttribute("importPortObj");
        } else {
            importObj = new ImportPortConfiguration();
        }
        importObj.setLineManager(importsForm.getLineManager());
        importObj.setImportsService(importsForm.getImportsService());
        importObj.setImportAgentNumber(importsForm.getImportAgentNumber());
        importObj.setImportAgentWarehouse(importsForm.getImportAgentWarehouse());
        session.setAttribute("importPortObj", importObj);


        return mapping.findForward("importConfig");
    }
}