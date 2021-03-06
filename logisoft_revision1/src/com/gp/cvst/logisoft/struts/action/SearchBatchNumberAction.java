/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.Iterator;
import java.util.List;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.WarehouseTemp;
import com.gp.cvst.logisoft.domain.ArBatch;
import com.gp.cvst.logisoft.hibernate.dao.ArBatchDAO;
import com.gp.cvst.logisoft.struts.form.SearchBatchNumberForm;

/** 
 * MyEclipse Struts
 * Creation date: 08-13-2008
 * 
 * XDoclet definition:
 * @struts.action path="/searchBatchNumber" name="searchBatchNumberForm" input="/jsps/AccountsRecievable/searchBatchNumber.jsp" scope="request" validate="true"
 */
public class SearchBatchNumberAction extends Action {
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
		SearchBatchNumberForm searchBatchNumberForm = (SearchBatchNumberForm) form;// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		Integer batchNo = searchBatchNumberForm.getBatchNo();
		String index = searchBatchNumberForm.getIndex();
		String sdate = "";
		String edate = "";
		ArBatchDAO arbatchDAO=new ArBatchDAO();
		ArBatch batchObj = new ArBatch(); 
	
		
		  if((request.getParameter("index")!=null && !request.getParameter("index").equals(""))|| (index != null && !index.equals("")))
		  {
			  int ind = Integer.parseInt(request.getParameter("index"));
				if(index!= null)
				{
					ind =Integer.parseInt(index); 
				}
				List batchList = (List)session.getAttribute("batchlist");
				batchObj = (ArBatch)batchList.get(ind);
				
				session.setAttribute("batchObj", batchObj);
				request.setAttribute("checked", "checked");
				
				return mapping.findForward("searchBatchNumber");
				
				
	    	}
		  else{
				List batchlist = arbatchDAO.findByBatchNo(batchNo);
				session.setAttribute("batchlist", batchlist);
		  }
		  
		 response.sendRedirect(request.getContextPath() +"/jsps/AccountsRecievable/searchBatchNumber.jsp");
		return null;
	}
}