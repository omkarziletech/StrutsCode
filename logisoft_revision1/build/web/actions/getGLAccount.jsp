<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@page import="com.gp.cvst.logisoft.domain.BankDetails"%>
<%
	
	String glAccount = "";
	String functionName="";
	
	 if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
    if(functionName.equals("CHECK_REGISTER") && request.getParameter("glAccountNo")!= null){
       glAccount=request.getParameter("glAccountNo");      
    }
    
	StringBuffer accountNoArray = new StringBuffer();
	accountNoArray.append("<ul>");
	BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
	List customerList = bankDetailsDAO.findByGLAccountNo("%"+glAccount+"%");
	
	if(null != customerList && !customerList.isEmpty()) {
		Iterator iter = customerList.iterator();
		try {
			while (iter.hasNext()) {
				BankDetails bankDetails = (BankDetails)iter.next();
				if (null != bankDetails && null != bankDetails.getGlAccountno() 
				&& !bankDetails.getGlAccountno().trim().equals("")) {
						String delimiter=" <--> ";
					String accountNumber = bankDetails.getBankAcctNo();
					String accountName = bankDetails.getAcctName();
					String bankName = bankDetails.getBankName();
					String bankAddress = bankDetails.getBankAddress();
					String glAccountNo = bankDetails.getGlAccountno();
					if(null != accountNumber && !accountNumber.trim().equals("")
						&& null != accountName && !accountName.trim().equals("") 
						&& null != bankName && !bankName.trim().equals("") 
						&& null != bankAddress && bankAddress.trim().equals("")){
							accountNoArray.append("<li id='"+accountNumber+"'><b>"
							+" <font color="+"red"+">"+glAccountNo+"</font>"
							+delimiter
							+bankName
							+ delimiter
							+ accountNumber
							+"<br/>"
							+accountName+"</b>"
							+delimiter
							+ bankAddress
							+ ";   "
							+ "</li>");
					}else if(null != accountNumber && !accountNumber.trim().equals("")
						&& null != accountName && !accountName.trim().equals("") 
						&& null != bankName && !bankName.trim().equals("")){
							accountNoArray.append("<li id='"+accountNumber+"'><b>"
							+" <font color="+"red"+">"+glAccountNo+"</font>"
							+delimiter
							+bankName
							+ delimiter
							+ accountNumber
							+"<br/>"
							+ accountName+"</b>"
							+ "</li>");
					}else if(null != accountNumber && !accountNumber.trim().equals("")
						&& null != bankName && !bankName.trim().equals("")){
							accountNoArray.append("<li id='"+accountNumber+"'><b>"
							+" <font color="+"red"+">"+glAccountNo+"</font>"
							+delimiter
							+bankName
							+ delimiter
							+ accountNumber
							+ "</b><br/>"
							+ "</li>");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    accountNoArray.append("</ul>");
	out.println(accountNoArray.toString());
%>

