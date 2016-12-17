/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



import com.gp.cvst.logisoft.beans.AccountStructureBean;
import com.gp.cvst.logisoft.domain.Segment;
import com.gp.cvst.logisoft.hibernate.dao.SegmentDAO;
import com.gp.cvst.logisoft.struts.form.EditPopupForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-07-2008
 * 
 * XDoclet definition:
 * @struts.action path="/editpopup" name="EditPopupForm" input="/jsps/Accounting/EditPopUp.jsp" scope="request"
 */
public class EditPopupAction extends Action {
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
	EditPopupForm EditPopUpForm = (EditPopupForm) form;// TODO Auto-generated method stub
	HttpSession session = ((HttpServletRequest)request).getSession();
	   
	String acctStruct = EditPopUpForm.getEditsegcode();
	String forwardName="";
    String buttonValue=EditPopUpForm.getButtonValue();
	String index=EditPopUpForm.getIndex();
	String  editId= String.valueOf(EditPopUpForm.getEditId());
	String segCodeBean="";
	String editdesc=EditPopUpForm.getEditsegdescription();
	String editcode=EditPopUpForm.getEditsegcode();
	String accountStructure=EditPopUpForm.getAccountStructure();
	int editlength=EditPopUpForm.getEditseglen();
	int acctid1=EditPopUpForm.getEditId();
	int editlen=EditPopUpForm.getEditseglen();
	  
	AccountStructureBean coab=new AccountStructureBean();
	SegmentDAO segDAO = new SegmentDAO();
	Segment segment = new   Segment();
		 
if(buttonValue.equals("editpopup"))
	 {  
	  segment.setSegmentDesc(editdesc);
	  segment.setId(acctid1);
	  segDAO.segdescupdate(acctid1,editdesc);
	  List AcctStruct = (List)segDAO.findForShow(accountStructure);
      session.setAttribute("AcctSturct",AcctStruct);
	  request.setAttribute("buttonValue","completed");
      forwardName="success";	
	 }

  return mapping.findForward("success");
	}
    }
 
 