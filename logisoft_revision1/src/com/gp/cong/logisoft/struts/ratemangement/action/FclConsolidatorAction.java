/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.gp.cong.logisoft.bc.ratemanagement.FclSellRatesBC;
import com.gp.cong.logisoft.domain.FclConsolidator;
import com.gp.cong.logisoft.struts.ratemangement.form.FclConsolidatorForm;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchFCLForm;

/** 
 * MyEclipse Struts
 * Creation date: 12-23-2008
 * 
 * XDoclet definition:
 * @struts.action path="/fclConsolidator" name="fclConsolidatorForm" input="/jsps/ratemanagement/fclConsolidator.jsp" scope="request" validate="true"
 */
public class FclConsolidatorAction extends Action {
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
		SearchFCLForm fclConsolidatorForm = (SearchFCLForm) form;// TODO Auto-generated method stub
		List fclConsolidatorList=new ArrayList();
		HttpSession session = ((HttpServletRequest)request).getSession();
		FclSellRatesBC fclSellRatesBC=new FclSellRatesBC();
		String buttonValue=fclConsolidatorForm.getButtonValue();
		String charge=fclConsolidatorForm.getHiddencharge();
		String rollToCharge=fclConsolidatorForm.getHiddenrollToCharge();
		String display=fclConsolidatorForm.getHiddendisplay();
		String excludeFromTotal=fclConsolidatorForm.getHiddenexcludeFromTotal();
		String originTerminal=fclConsolidatorForm.getHiddenTerminalNumber();
		String destinationport=fclConsolidatorForm.getHiddendestSheduleNumber();
		String index=fclConsolidatorForm.getIndex();
		String originalTerminal="";
		String destinationPort="";
		MessageResources messageResources = getResources(request);
		if(buttonValue==null || buttonValue.equals(""))
		{
			if(session.getAttribute("fclconsolidator")!=null)
			{
				fclConsolidatorList=(List)session.getAttribute("fclconsolidator");
			}
			else
			{
				fclConsolidatorList=fclSellRatesBC.getConsolidatorList(messageResources);
				session.setAttribute("fclconsolidator", fclConsolidatorList);
			}
			if(request.getParameter("originalTerminal")!=null)
			{
				originalTerminal=(String)request.getParameter("originalTerminal");
				request.setAttribute("originalTerminal", originalTerminal);
			}
			if(request.getParameter("destinationPort")!=null)
			{
				destinationPort=(String)request.getParameter("destinationPort");
				request.setAttribute("destinationPort",destinationPort);
			}
		}
		else if(buttonValue.equals("save") || buttonValue.equals("add"))
		{
			fclConsolidatorList=fclSellRatesBC.getConsolidatorList(messageResources);
			request.setAttribute("fclconsolidator", fclConsolidatorList);
			if(session.getAttribute("fclconsolidator")!=null)
			{
				fclConsolidatorList=(List)session.getAttribute("fclconsolidator");
			}
			
			String hiddencharge[] = StringUtils.splitPreserveAllTokens(charge, ',');
			String hiddenrolltocharge[]=StringUtils.splitPreserveAllTokens(rollToCharge, ',');
			String hiddendisplay[]=StringUtils.splitPreserveAllTokens(display,',');
			String hiddenexcludefromtotal[]=StringUtils.splitPreserveAllTokens(excludeFromTotal,',');
			for(int i=0;i<fclConsolidatorList.size();i++)
			{
				FclConsolidator fclConsolidator=(FclConsolidator)fclConsolidatorList.get(i);
				fclConsolidator.setCharge(hiddencharge[i]);
				fclConsolidator.setRollToCharge(hiddenrolltocharge[i]);
				fclConsolidator.setDisplay(hiddendisplay[i]);
				fclConsolidator.setExcludeFromTotal(hiddenexcludefromtotal[i]);
			}
			if(buttonValue.equals("add")){
			fclConsolidatorList.add(new FclConsolidator("","","Y","N"));
			}
			session.setAttribute("fclconsolidator", fclConsolidatorList);
			FclConsolidator setfclConsolidator = new FclConsolidator();
			setfclConsolidator.setFclConsolidatorList(fclConsolidatorList);
			if(buttonValue.equals("save")){
			request.setAttribute("completed", "completed");
			}
		}else if(buttonValue.equals("delete")){
			if(session.getAttribute("fclconsolidator")!=null){
				fclConsolidatorList=(List)session.getAttribute("fclconsolidator");
				fclConsolidatorList.remove(fclConsolidatorList.get(Integer.parseInt(index)));
			}
		}else if(buttonValue.equals("reset")){
			fclConsolidatorList=fclSellRatesBC.getConsolidatorList(messageResources);
			session.setAttribute("fclconsolidator", fclConsolidatorList);
		}
		return mapping.findForward("consol");
	}
}