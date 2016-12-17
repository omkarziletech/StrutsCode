/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.struts.form.ProcessInfoForm;

/** 
 * MyEclipse Struts
 * Creation date: 03-09-2009
 * 
 * XDoclet definition:
 * @struts.action path="/processInfo" name="processInfoForm" input="/jsps/admin/ProcessInfo.jsp" scope="request" validate="true"
 */
public class ProcessInfoAction extends Action {
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
		ProcessInfoForm processInfoForm = (ProcessInfoForm) form;// TODO Auto-generated method stub
		String index=processInfoForm.getIndex();
		String buttonValue=processInfoForm.getButtonValue();
		ProcessInfoBC processInfoBc=new ProcessInfoBC();
		if(buttonValue==null || buttonValue.equals("")){
			List lockedList=processInfoBc.getLockedPages();
			request.setAttribute("lockedList", lockedList);
		}
		if(buttonValue!=null && buttonValue.equals("delete")){
			List lockedList=processInfoBc.getLockedPages();
			processInfoBc.delete(index);
			lockedList=new ArrayList();
			lockedList=processInfoBc.getLockedPages();
			request.setAttribute("lockedList", lockedList);
		}
		return mapping.findForward("processInfo");
	}
}