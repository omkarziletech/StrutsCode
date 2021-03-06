/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.domain.ArAgeing;
import com.gp.cvst.logisoft.hibernate.dao.ArAgeingDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.reports.dto.AgingReportPeriodDTO;
import com.gp.cvst.logisoft.struts.form.ArManifestAgeingForm;

/** 
 * MyEclipse Struts
 * Creation date: 10-22-2008
 * 
 * XDoclet definition:
 * @struts.action path="/arManifestAgeing" name="arManifestAgeingForm" input="/jsps/arManifestAgeing.jsp" scope="request"
 * @struts.action-forward name="success" path="/jsps/arManifestAgeing.jsp"
 */
public class ArManifestAgeingAction extends Action {
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
	//dummy class
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("");
	}
}