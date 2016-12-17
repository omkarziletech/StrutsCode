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
import com.gp.cong.logisoft.struts.form.TopFrameForm;

/** 
 * MyEclipse Struts
 * Creation date: 11-05-2007
 * 
 * XDoclet definition:
 * @struts.action path="/topFrame" name="topFrameForm" input="/jsps/topFrame.jsp" scope="request" validate="true"
 */
public class TopFrameAction extends Action {
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
		TopFrameForm topFrameForm = (TopFrameForm) form;// TODO Auto-generated method stub
		
		String buttonValue=topFrameForm.getButtonValue();
		String forwardName="";
		if(buttonValue.equals("logout"))
		{	
		String msg="Item has been deleted";
		request.setAttribute("message",msg);	
		forwardName="logout";
		}
		request.setAttribute("buttonValue",buttonValue);
		return mapping.findForward(forwardName);
	}
}