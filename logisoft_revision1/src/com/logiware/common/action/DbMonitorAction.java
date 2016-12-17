package com.logiware.common.action;

import com.logiware.common.dao.DbMonitorDAO;
import com.logiware.common.form.DbMonitorForm;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class DbMonitorAction extends BaseAction {

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	DbMonitorForm dbMonitorForm = (DbMonitorForm) form;
	new DbMonitorDAO().search(dbMonitorForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward killProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    DbMonitorForm dbMonitorForm = (DbMonitorForm) form;
	    new DbMonitorDAO().killProcess(dbMonitorForm);
	    request.setAttribute("message", "Process with Id - " + dbMonitorForm.getId() + " is killed successfully");
	} catch (SQLException e) {
	    request.setAttribute("error", e.getMessage());
	}
	return refresh(mapping, form, request, response);
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	return refresh(mapping, form, request, response);
    }
}
