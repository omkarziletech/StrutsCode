<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@page import="com.gp.cvst.logisoft.domain.BankDetails"%>
<%
	
	String bankAccountNumber = request.getParameter("account");
	String functionName="";
	
	 if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
    if(functionName.equals("AP_RECONCILE") && request.getParameter("bankAccount")!= null){
       bankAccountNumber=request.getParameter("bankAccount");
      
    }else     if(functionName.equals("CHECK_REGISTER") && request.getParameter("bankAccountNo")!= null){
       bankAccountNumber=request.getParameter("bankAccountNo");      
    }
    
	JSONArray accountNoArray = new JSONArray();

	BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
	List customerList = bankDetailsDAO.findByBankAcctNo(bankAccountNumber+"%");
	
	if(null != customerList && !customerList.isEmpty()) {
		Iterator iter = customerList.iterator();
		try {
			while (iter.hasNext()) {
				BankDetails bankDetails = (BankDetails)iter.next();
				if (null != bankDetails && null != bankDetails.getAcctNo() 
				&& !bankDetails.getAcctNo().trim().equals("")) {
					String accountNumber = bankDetails.getBankAcctNo();
					String accountName = bankDetails.getAcctName();
					String bankName = bankDetails.getBankName();
					String bankAddress = bankDetails.getBankAddress();
					if(null != accountName && !accountName.trim().equals("") && 
					null != bankName && !bankName.trim().equals("") &&
					null != bankAddress && bankAddress.trim().equals("")){
						accountNoArray.put(accountNumber+":- "+accountName+" "+bankName+" "+bankAddress);
					}else if(null != accountName && !accountName.trim().equals("") && 
					null != bankName && !bankName.trim().equals("")) {
						accountNoArray.put(accountNumber+":- "+accountName+" "+bankName);
					}else if(null != accountName && !accountName.trim().equals("") && 
					null != bankAddress && bankAddress.trim().equals("")) {
						accountNoArray.put(accountNumber+":- "+accountName+" "+bankAddress);
					}else if(null != bankName && !bankName.trim().equals("") &&
					null != bankAddress && bankAddress.trim().equals("")) {
						accountNoArray.put(accountNumber+":- "+bankName+" "+bankAddress);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	out.println(accountNoArray.toString());
%>

