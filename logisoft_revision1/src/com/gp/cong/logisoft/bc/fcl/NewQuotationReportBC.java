package com.gp.cong.logisoft.bc.fcl;

import java.util.List;

import org.apache.struts.util.MessageResources;

import com.gp.cong.logisoft.beans.CostBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.reports.dto.QuotationDTO;

public class NewQuotationReportBC{
	public void createJournalEntryReport(String fileNo,String fileName,User user,String contextPath,MessageResources messageResources) throws Exception{
	    QuotationDAO quotationDAO=new QuotationDAO();
		Quotation  quotation  = quotationDAO.getFileNoObject(fileNo);
		QuotationReportBC quotationReportBC=new QuotationReportBC();
		String ruleName = getruleName();
		quotation.setRuleName(ruleName);
		quotation = setUserDetails(quotation,user);
	}
	private String getruleName() throws Exception{
	   SystemRulesDAO SystemRulesDAO=new SystemRulesDAO();
	   String ruleName = SystemRulesDAO.getSystemRulesByCode("CompanyName") ;  
	   return ruleName;
	}
	private Quotation setUserDetails(Quotation quotation,User user) throws Exception{
		if(user.getFirstName()!=null && !user.getFirstName().equalsIgnoreCase("")){
			quotation.setUserName(user.getFirstName());
		}else{
			quotation.setUserName("");
		}
		if(user.getLastName()!=null && !user.getLastName().equalsIgnoreCase("")){
			quotation.setUserName(user.getFirstName().concat(" "+user.getLastName()));
		}else{
			quotation.setUserName("");
		}
		if(user.getTelephone()!=null && !user.getTelephone().equalsIgnoreCase("")){
			quotation.setUserPhone(user.getTelephone());
		}else{
			quotation.setUserPhone("");
		}
		if(user.getFax()!=null && !user.getFax().equalsIgnoreCase("")){
			quotation.setUserFax(user.getFax());
		}else{
			quotation.setUserFax("");
		}
		return quotation;
	}
	private List getDisclaimerList() throws Exception {
		GenericCodeDAO genericCodeDAO=new GenericCodeDAO();
		List disclaimerList = genericCodeDAO.getAllCommentCodesForReports();
		return disclaimerList;
	}
	private List<QuotationDTO> getChargesListWithUnitType(Quotation quotation) throws Exception{
		 QuotationReportBC quotationReportBC=new QuotationReportBC();
		 List<QuotationDTO> QtFieldsList = quotationReportBC.getChargesList(quotation.getQuoteNo());
		 return QtFieldsList;
	}
	private List<CostBean> getOtherChargeList(Quotation quotation,MessageResources messageResources) throws Exception{
		QuotationReportBC quotationReportBC = new QuotationReportBC();
		List<CostBean> otherChargesLIst = quotationReportBC.getOtherChargesList(quotation.getQuoteNo(),
				messageResources);
		return otherChargesLIst;
	}
//	private List getChargesListWithoutUnitType(Quotation quotation){
//		ChargesDAO chargesDAO=new ChargesDAO();
//		List chargeList2=chargesDAO.getChargesforQuotation8(quotation.getQuoteId());
//		return chargeList2;
//		
//	}
	
}