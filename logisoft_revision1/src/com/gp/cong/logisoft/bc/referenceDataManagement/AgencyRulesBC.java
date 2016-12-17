package com.gp.cong.logisoft.bc.referenceDataManagement;

import com.gp.cong.logisoft.domain.AgencyRules;
import com.gp.cong.logisoft.struts.form.AgencyRulesForm;

public class AgencyRulesBC {
	public AgencyRules getSaveInformation(AgencyRulesForm agencyRulesForm) {
		AgencyRules agencyRules = new AgencyRules();
		// setting amount of routed and non routed agent and commn
		agencyRules.setNotRouteAgtAdminAmt((agencyRulesForm
				.getAmountRouteNotAgentAdmin() != null && !agencyRulesForm
				.getAmountRouteNotAgentAdmin().equals("")) ? Double
				.parseDouble(agencyRulesForm.getAmountRouteNotAgentAdmin())
				: null);
		agencyRules.setNotRouteAgtCommnAmt((agencyRulesForm
				.getAmountRouteNotAgentCommn() != null && !agencyRulesForm
				.getAmountRouteNotAgentCommn().equals("")) ? Double
				.parseDouble(agencyRulesForm.getAmountRouteNotAgentCommn())
				: null);
		agencyRules.setRouteAgtAdminAmt((agencyRulesForm
				.getAmountRouteByAgentAdmin() != null && !agencyRulesForm
				.getAmountRouteByAgentAdmin().equals("")) ? Double
				.parseDouble(agencyRulesForm.getAmountRouteByAgentAdmin())
				: null);
		agencyRules.setRouteAgtCommnAmt((agencyRulesForm
				.getAmountRouteByAgentCommn() != null && !agencyRulesForm
				.getAmountRouteByAgentCommn().equals("")) ? Double
				.parseDouble(agencyRulesForm.getAmountRouteByAgentCommn())
				: null);
		agencyRules.setNotRouteAgtAdminTieramt((agencyRulesForm
				.getTierAmountRouteNotAgentAdmin() != null && !agencyRulesForm
				.getTierAmountRouteNotAgentAdmin().equals("")) ? Double
				.parseDouble(agencyRulesForm.getTierAmountRouteNotAgentAdmin())
				: null);

		agencyRules.setNotRouteAgtCommnTieramt((agencyRulesForm
				.getTierAmountRouteNotAgentCommn() != null && !agencyRulesForm
				.getTierAmountRouteNotAgentCommn().equals("")) ? Double
				.parseDouble(agencyRulesForm.getTierAmountRouteNotAgentCommn())
				: null);
		agencyRules.setRouteAgtAdminTieramt((agencyRulesForm
				.getTierAmountRouteByAgentAdmin() != null && !agencyRulesForm
				.getTierAmountRouteByAgentAdmin().equals("")) ? Double
				.parseDouble(agencyRulesForm.getTierAmountRouteByAgentAdmin())
				: null);
		agencyRules.setRouteAgtCommnTieramt((agencyRulesForm
				.getTierAmountRouteByAgentCommn() != null && !agencyRulesForm
				.getTierAmountRouteByAgentCommn().equals("")) ? Double
				.parseDouble(agencyRulesForm.getTierAmountRouteByAgentCommn())
				: null);
		// setting rules
		agencyRules.setNotRouteAgtAdminRule(agencyRulesForm
				.getRuleRouteNotAgentAdmin());
		agencyRules.setNotRouteAgtCommnRule(agencyRulesForm
				.getRuleRouteNotAgentCommn());
		agencyRules.setRouteAgtCommnRule(agencyRulesForm
				.getRuleRouteByAgentCommn());
		agencyRules.setRouteAgtAdminRule(agencyRulesForm
				.getRuleRouteByAgentAdmin());
		if(agencyRulesForm.getAgencyRulesId()!=null && agencyRulesForm.getAgencyRulesId()!=0){
			agencyRules.setId(agencyRulesForm.getAgencyRulesId());
		}
		
		return agencyRules;

	}
}
