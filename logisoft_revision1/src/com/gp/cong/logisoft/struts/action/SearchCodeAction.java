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

import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.form.SearchCodeForm;

/** 
 * MyEclipse Struts
 * Creation date: 08-04-2008
 * 
 * XDoclet definition:
 * @struts.action path="/searchCode" name="searchCodeForm" input="/jsps/datareference/searchCode.jsp" scope="request" validate="true"
 */
public class SearchCodeAction extends Action {
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
		SearchCodeForm searchCodeForm = (SearchCodeForm) form;// TODO Auto-generated method stub
		String code= searchCodeForm.getCode();
		String codeDesc = searchCodeForm.getCodeDesc();
		
		GenericCodeDAO gcdao = new GenericCodeDAO();
		
		return mapping.findForward("searchCode");
	}
}