/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.struts.form.FclcontainerForm;

/** 
 * MyEclipse Struts
 * Creation date: 09-07-2008
 * 
 * XDoclet definition:
 * @struts.action path="/fclcontainer" name="fclcontainerForm" input="/jsps/fclQuotes/fclcontainer.jsp" scope="request" validate="true"
 */
public class FclcontainerAction extends Action {
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
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		FclcontainerForm fclcontainerForm = (FclcontainerForm) form;// TODO Auto-generated method stub
		HttpSession session = ((HttpServletRequest)request).getSession();
		String buttonValue = fclcontainerForm.getButtonValue();
		 List fclBlContainerList=new ArrayList();
		 String bol=fclcontainerForm.getBol();
		 request.setAttribute("bol", bol);
		 FclBlDAO fclBlDAO=new FclBlDAO();
		 FclBlBC fclBlBC=new FclBlBC();
		 HashMap hashMap=new HashMap();
		 if(request.getParameter("button")!=null && request.getParameter("button").equals("NewFCLBL")){
			 String bol1=null;
			 if(request.getParameter("bol")!=null && !request.getParameter("bol").equals("")){
				 bol1=request.getParameter("bol");
			  }
			 request.setAttribute("bol", bol1);
			 FclBl fclBl=fclBlDAO.findById(Integer.parseInt(bol1));
			 List containerList=new ArrayList();
			 if(fclBl.getFclcontainer()!=null){
				 Iterator iter=fclBl.getFclcontainer().iterator();
				 while(iter.hasNext()){
					 FclBlContainer fclBlContainer=(FclBlContainer)iter.next();
					 hashMap.put(fclBlContainer.getTrailerNoId(), fclBlContainer);
					 fclBlContainerList.add(fclBlContainer.getTrailerNoId());
				 }
				 Collections.sort(fclBlContainerList);
				 for (int i = 0; i < fclBlContainerList.size(); i++) {
					FclBlContainer fclBlContainer=(FclBlContainer)hashMap.get(fclBlContainerList.get(i));
					containerList.add(fclBlContainer);
				}
			 }
			 request.setAttribute("fclBlContainerList", containerList);
		}
		if(buttonValue!=null && (buttonValue.equals("add") || buttonValue.equals("save") || buttonValue.equals("popup")||
				buttonValue.equals("hazmat") || buttonValue.equals("addAES")|| buttonValue.equals("addBookingHazmat")
				|| buttonValue.equals("getContainerData"))){
			 FclBl fclBl=fclBlDAO.findById(Integer.parseInt(bol));
			 hashMap=new HashMap();
			 List containerList=new ArrayList();
			if(fclBl.getFclcontainer()!=null){
				 Iterator iter=fclBl.getFclcontainer().iterator();
				 while(iter.hasNext()){
					 FclBlContainer fclBlContainer=(FclBlContainer)iter.next();
					 hashMap.put(fclBlContainer.getTrailerNoId(), fclBlContainer);
					 fclBlContainerList.add(fclBlContainer.getTrailerNoId());
				 }
			 }
			Collections.sort(fclBlContainerList);
			 for (int i = 0; i < fclBlContainerList.size(); i++) {
				FclBlContainer fclBlContainer = (FclBlContainer) hashMap.get(fclBlContainerList.get(i));
				containerList.add(fclBlContainer);
			}
			/*Set fclContainerSet=fclBlBC.setFclContainerValues(containerList, fclBl, fclcontainerForm);
			if(buttonValue.equals("add")){
				fclBlContainerList.add(new FclBlContainer());
				fclContainerSet.add(new FclBlContainer());
				}*/
			fclBlContainerList=new ArrayList();
			containerList=new ArrayList();
				//fclBl.setFclcontainer(fclContainerSet);
				if(buttonValue.equals("add") || buttonValue.equals("save")){
				fclBlDAO.update(fclBl);
				}
				if(fclBl.getFclcontainer()!=null){
					Iterator iter=fclBl.getFclcontainer().iterator();
					while(iter.hasNext()){
						FclBlContainer fclBlContainer=(FclBlContainer)iter.next();
						 hashMap.put(fclBlContainer.getTrailerNoId(), fclBlContainer);
						fclBlContainerList.add(fclBlContainer.getTrailerNoId());
					}
					Collections.sort(fclBlContainerList);
					 for (int i = 0; i < fclBlContainerList.size(); i++) {
						FclBlContainer fclBlContainer = (FclBlContainer) hashMap.get(fclBlContainerList.get(i));
						containerList.add(fclBlContainer);
					}
				}
				
				
				request.setAttribute("fclBlContainerList", containerList);
				if(buttonValue.equals("save")){
	    		 request.setAttribute("close", "close");
	    		 if(session.getAttribute("bookinghazmat")!=null){
	    			 session.removeAttribute("bookinghazmat");
	    		 }
				}
		}
		return mapping.findForward("container");
	}
}