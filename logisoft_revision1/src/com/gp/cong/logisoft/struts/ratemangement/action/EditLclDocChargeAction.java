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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.LCLColoadDocumentCharges;
import com.gp.cong.logisoft.domain.LCLColoadMaster;
import com.gp.cong.logisoft.struts.ratemangement.form.DocChargesEditForm;
import com.gp.cong.logisoft.struts.ratemangement.form.EditLclDocChargeForm;

/** 
 * MyEclipse Struts
 * Creation date: 06-04-2008
 * 
 * XDoclet definition:
 * @struts.action path="/editLclDocCharge" name="editLclDocChargeForm" input="jsps/ratemangement/editLclDocCharge.jsp" scope="request" validate="true"
 */
public class EditLclDocChargeAction extends Action {
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
		EditLclDocChargeForm docChargesEditForm = (EditLclDocChargeForm) form;// TODO Auto-generated method stub
		HttpSession session = ((HttpServletRequest)request).getSession();
		String forwardName="";
		String buttonValue=docChargesEditForm.getButtonValue();
		LCLColoadDocumentCharges documentCharges=null;
	
		LCLColoadDocumentCharges docChargesObj=null;
		List docChargesList = new ArrayList();
		int index=0;
		LCLColoadMaster lCLColoadMaster=new LCLColoadMaster();

			if(session.getAttribute("addlclColoadMaster")==null)
			{
				lCLColoadMaster=new LCLColoadMaster();
			}
			else
			{
				lCLColoadMaster=(LCLColoadMaster)session.getAttribute("addlclColoadMaster");
			}
			if(docChargesEditForm.getMaxDocCharge()!=null && !docChargesEditForm.getMaxDocCharge().equals(""))
			{
				lCLColoadMaster.setMaxDocCharge(new Double(docChargesEditForm.getMaxDocCharge()));
			}
			else
			{
				lCLColoadMaster.setMaxDocCharge(0.0);
			}
			if(docChargesEditForm.getFfCommision()!=null && !docChargesEditForm.getFfCommision().equals(""))
			{
				lCLColoadMaster.setFfCommission(new Double(docChargesEditForm.getFfCommision()));
			}
			else
			{
				lCLColoadMaster.setFfCommission(0.0);
			}
			if(docChargesEditForm.getBlBottomLine()!=null && !docChargesEditForm.getBlBottomLine().equals(""))
			{
				lCLColoadMaster.setBlBottomLine(new Double(docChargesEditForm.getBlBottomLine()));
			}
			else
			{
				lCLColoadMaster.setBlBottomLine(0.0);
			}
			
			session.setAttribute("addlclColoadMaster",lCLColoadMaster);
			if(session.getAttribute("lcldocumentCharges")!=null )
			{
				documentCharges=(LCLColoadDocumentCharges)session.getAttribute("lcldocumentCharges");
			}
			else
			{
				documentCharges=new LCLColoadDocumentCharges();
			}
			if(docChargesEditForm.getAmount()!=null && !docChargesEditForm.getAmount().equals(""))
			{
				documentCharges.setAmount(new Double(docChargesEditForm.getAmount()));
			}
			
			forwardName="lcldocChargesEdit";
			if(buttonValue.equals("add"))
			{
				if(session.getAttribute("lcldocChargesAdd") != null)
				{
					docChargesList=(List)session.getAttribute("lcldocChargesAdd");
					for(int i=0;i<docChargesList.size();i++)
					{
						
						LCLColoadDocumentCharges coMaster=(LCLColoadDocumentCharges)docChargesList.get(i);
							if(coMaster.getIndex()==documentCharges.getIndex())
							{
								documentCharges.setIndex(coMaster.getIndex());
								docChargesList.remove(documentCharges);
							}
					}
				}
				
				docChargesList.add(documentCharges);
				session.setAttribute("lcldocChargesAdd", docChargesList);
				if(session.getAttribute("lcldocumentCharges")!=null)
				{
					session.removeAttribute("lcldocumentCharges");
				}
				forwardName="lcldocChargesAdd";
			}
			else if(buttonValue.equals("cancel"))
			{
				if(session.getAttribute("lcldocumentCharges")!=null)
				{
					session.removeAttribute("lcldocumentCharges");
				}
				forwardName="lcldocChargesAdd";
			}
			else if(buttonValue.equals("delete"))
			{
				if(session.getAttribute("lcldocChargesAdd") != null)
				{
					docChargesList=(List)session.getAttribute("lcldocChargesAdd");
				}
				for(int i=0;i<docChargesList.size();i++)
				{
					LCLColoadDocumentCharges coMaster=(LCLColoadDocumentCharges)docChargesList.get(i);
					if(coMaster.getIndex()==documentCharges.getIndex())
					{
						docChargesList.remove(coMaster);
					}
					session.setAttribute("lcldocChargesAdd", docChargesList);
				}
				if(session.getAttribute("lcldocumentCharges")!=null)
				{
					session.removeAttribute("lcldocumentCharges");
				}
				forwardName="lcldocChargesAdd";
			}
		request.setAttribute("buttonValue",buttonValue);
			return mapping.findForward(forwardName);
	}	
		
}