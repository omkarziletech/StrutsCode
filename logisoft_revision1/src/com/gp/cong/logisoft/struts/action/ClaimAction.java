/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.struts.form.ClaimForm;

/** 
 * MyEclipse Struts
 * Creation date: 03-17-2008
 * 
 * XDoclet definition:
 * @struts.action path="/claim" name="claimForm" input="/jsps/datareference/Claim.jsp" scope="request" validate="true"
 */
public class ClaimAction extends Action {
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
		ClaimForm claimForm = (ClaimForm) form;// TODO Auto-generated method stub
		return null;
	}
}