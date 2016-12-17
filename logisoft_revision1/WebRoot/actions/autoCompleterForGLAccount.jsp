<%@ page language="java" import="java.util.List"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@page import="com.gp.cvst.logisoft.domain.AccountDetails"%>
<%

	String glAccountNo = "";
	String functionName = null;

	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("BANKACCOUNT") && null!=request.getParameter("glAccountNumber")) {
		glAccountNo = request.getParameter("glAccountNumber");
	}else if (functionName.equals("SUBLEDGER") && null!=request.getParameter("index")) {
		String index = request.getParameter("index");
		if(null!=request.getParameter("glAccountNo"+index)){
			glAccountNo = request.getParameter("glAccountNo"+index);
		}
	}else if (functionName.equals("GLMAPPING")) {
		if(null!=request.getParameter("glAccount")){
			glAccountNo = request.getParameter("glAccount");
		}else if(null!=request.getParameter("endglAccount")){
			glAccountNo = request.getParameter("endglAccount");
		}
	}else if (functionName.equals("APINVOICE")){
		if(null!=request.getParameter("glAccount")){
			glAccountNo = request.getParameter("glAccount");
		}
	}
	//this is to add '-' to the account no format : ##-####-##
	String output = glAccountNo;
	glAccountNo = glAccountNo.replaceAll("-", "");
	if(StringUtils.isNumeric(glAccountNo)){
		if (glAccountNo.length() < 3) {
			output = glAccountNo;
		} else if (glAccountNo.length() < 7) {
			output = glAccountNo.substring(0, 2) + "-"
					+ glAccountNo.substring(2, glAccountNo.length());
		} else if (glAccountNo.length() < 9) {
			output = glAccountNo.substring(0, 2) + "-" + glAccountNo.substring(2, 6)
					+ "-" + glAccountNo.substring(6, glAccountNo.length());
		}
	glAccountNo = output;
	}
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append("<ul>");
	
	if (glAccountNo != null && !glAccountNo.trim().equals("")) {
		DBUtil dbUtil = new DBUtil();
		List<AccountDetails> accountList = dbUtil.getAccountNumber(glAccountNo);
		if(null!=accountList){
			if (!functionName.equals("GLMAPPING")){
				for(AccountDetails accountDetails : accountList) {
					stringBuffer.append("<li id='"+ accountDetails.getAccount() + "'>"
						+ "<b>"+accountDetails.getAccount()
						+ " <--> " + accountDetails.getAcctDesc()
						+ "</b></li>");
				}
			}else if (functionName.equals("GLMAPPING")) {
				for(AccountDetails accountDetails : accountList) {
					String glAcct = accountDetails.getAccount();
					if(accountDetails.getAccount().length()>4 && accountDetails.getAccount().contains("-")){
						glAcct = glAcct.substring(glAcct.indexOf("-")+1,glAcct.lastIndexOf("-"));
					}
					stringBuffer.append("<li id='"+ glAcct + "'>"
						+ "<b>"+accountDetails.getAccount()
						+ " <--> " + accountDetails.getAcctDesc()
						+ "</b></li>");
				}
			}	
		}
	}
	stringBuffer.append("</ul>");
	out.println(stringBuffer.toString());
%>
