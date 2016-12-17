package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.bc.referenceDataManagement.AgencyRulesBC;
import com.gp.cong.logisoft.domain.AgencyInfo;
import com.gp.cong.logisoft.domain.AgencyRules;
import com.gp.cong.logisoft.struts.form.AgencyRulesForm;
import com.gp.cong.logisoft.util.CommonFunctions;


public class AgencyRulesAction extends Action {
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession();
		AgencyRulesForm agencyRulesForm = (AgencyRulesForm) form;
		String forward = "";
		String buttonValue = agencyRulesForm.getButtonValue();
		String index = req.getParameter("index");
		req.setAttribute("index", index);
		if (req.getParameter("search") != null && req.getParameter("search").equalsIgnoreCase("get")) {
			List agencyInfoListForFCL = new ArrayList();
			AgencyInfo agencyInfoObj = null;
			if (index != null) {
				agencyInfoListForFCL = (List) session.getAttribute("agencyInfoListForFCL");
				if(CommonFunctions.isNotNullOrNotEmpty(agencyInfoListForFCL)){
					agencyInfoObj = (AgencyInfo) agencyInfoListForFCL.get(Integer.parseInt(index));
					Set agencyRulesSet = agencyInfoObj.getAgencyRules();
					if (agencyRulesSet != null) {
						AgencyRules agencyRules = null;
						for (Iterator iter = agencyRulesSet.iterator(); iter.hasNext();) {
							agencyRules = (AgencyRules) iter.next();
						}
						req.setAttribute("AgencyRules", agencyRules);
					} else {
						req.setAttribute("AgencyRules", null);
					}
				}
				
				
			}
			forward = "agencyRules";
		}
		if (buttonValue != null && buttonValue.equalsIgnoreCase("save")) {
			AgencyRulesBC agencyRulesBC = new AgencyRulesBC();
			List agencyInfoListForFCL = new ArrayList();
			AgencyRules rules = agencyRulesBC.getSaveInformation(agencyRulesForm);
			AgencyInfo agencyInfoObj = null;
			if (index != null && session.getAttribute("agencyInfoListForFCL")!=null) {
				agencyInfoListForFCL = (List) session.getAttribute("agencyInfoListForFCL");
				if(CommonFunctions.isNotNullOrNotEmpty(agencyInfoListForFCL)){
					agencyInfoObj = (AgencyInfo) agencyInfoListForFCL.get(Integer.parseInt(index));
					Set<AgencyRules> agencyRulesSet= new HashSet();
					agencyRulesSet.add(rules);
					agencyInfoObj.setAgencyRules(agencyRulesSet);
					agencyInfoListForFCL.set(Integer.parseInt(index), agencyInfoObj);	
				}
					
			}
			session.setAttribute("agencyInfoListForFCL",agencyInfoListForFCL);
			forward = "agencyRules";
			req.setAttribute("AgencyRules", rules);
			req.setAttribute("close", "close");
		}
		return mapping.findForward(forward);
	}

}
