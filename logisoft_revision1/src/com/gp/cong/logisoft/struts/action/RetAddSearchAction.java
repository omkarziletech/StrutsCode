/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.struts.form.RetAddSearchForm;
import com.gp.cvst.logisoft.hibernate.dao.RetAddDAO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Vinay
 */
public class RetAddSearchAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        RetAddSearchForm retAddSearchForm = (RetAddSearchForm) form;
        RetAddDAO retAddDAO = new RetAddDAO();
        String forward = "retAddSearch";
        if (retAddSearchForm.getAction().equals("get")) {
            request.setAttribute("currentRetAdd", retAddDAO.getRetAdd(retAddSearchForm.getRetAddId()));
            forward = "retAddEdit";
        } else if (retAddSearchForm.getAction().equals("edit")) {
            retAddDAO.updateRetAdd(retAddSearchForm);
        }
        if (!retAddSearchForm.getAction().equals("get")) {
            request.setAttribute("searchResults", retAddDAO.search(retAddSearchForm));
        }
        return mapping.findForward(forward);
    }
}
