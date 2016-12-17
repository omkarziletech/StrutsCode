<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CustomerDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%
	String account = "";
	String name = "";
	String type = "mb";
	String functionName = null;
	String acctName="";
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("AR_INVOICE")
			|| functionName.equals("AP_INVOICE")||functionName.equals("ACCRUALS")) {
		if (request.getParameter("cusName") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			name = request.getParameter("cusName");

		}else if(request.getParameter("custname") != null && request.getParameter("from") != null
				&& request.getParameter("from").equals("1") ){
			name = request.getParameter("custname");	
		}
	}else if (functionName.equals("FCL_BL_CORRECTION")
			&& request.getParameter("from")!=null 
			&& request.getParameter("from").equalsIgnoreCase("0")) {
			acctName = request.getParameter("accountName");	
	}
	JSONArray accountNoArray = new JSONArray();
	if (name != null && !name.trim().equals("")) {
		CustomerDAO customerDAO = new CustomerDAO();
		List customerList = customerDAO.findForManagement(account,
				name, null, type);

		Iterator iter = customerList.iterator();
		try {
			while (iter.hasNext()) {
				CustomerTemp accountDetails = (CustomerTemp) iter
						.next();
			if (accountDetails != null) {
			if (functionName.equals("AP_INVOICE") ||functionName.equals("ACCRUALS")){
				String city = accountDetails.getCity2();
				String state = accountDetails.getState();
				String addressLine = accountDetails.getAddress1();
				if (null != city && !city.trim().equals("")
					&& null != state
					&& !state.trim().equals("")
					&& null != type
					&& !type.trim().equals("")
					&& null != addressLine
					&& !addressLine.trim().equals("") ) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ ":-   "
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getAddress1()
					+ ",  "
					+ accountDetails.getCity2()
					+ ",  "
					+ accountDetails.getState()
					+ ":-   "
					+ accountDetails.getType());
				}else if (null != city
					&& !city.trim().equals("")
					&& null != state
					&& !state.trim().equals("")
					&& null != type
					&& !type.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ ":-   "
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getCity2()
					+ ",  "
					+ accountDetails.getState()
					+ ":-   "
					+ accountDetails.getType());
			  }  
				else if (null != city
					&& !city.trim().equals("")
					&& null != state
					&& !state.trim().equals("")
					&& null != addressLine
					&& !addressLine.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ ":-   "
					+ accountDetails.getAccountNo()
					+ ":-  "
					+ accountDetails.getAddress1()
					+ ",  "
					+ accountDetails.getCity2()
					+ ",  "
					+ accountDetails.getState());
				} else if (null != city
					&& !city.trim().equals("")
					&& null != addressLine
					&& !addressLine.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ ":-   "
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getAddress1()
					+ ",  "
					+ accountDetails.getCity2());
				} else if (null != city
					&& !city.trim().equals("")
					&& null != state
					&& !state.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ ":-   "
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getCity2()
					+ ",  "
					+ accountDetails.getState());
				}else if (null != city
					&& !city.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ ":-   "
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getCity2());
				}
				else {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ ":-   "
					+ accountDetails.getAccountNo());
				}

			}else{
								accountNoArray.put(accountDetails.getAccountName());
			
			     }
			} else {
				accountNoArray.put(accountDetails
				.getAccountName());
			}

		}
			
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if(acctName != null && !acctName.trim().equals("")){
		CustomerDAO customerDAO = new CustomerDAO();
		List customerList = customerDAO.findForManagement(account,
				acctName, null, type);
		Iterator iter = customerList.iterator();
		try {
			while (iter.hasNext()) {
				CustomerTemp accountDetails = (CustomerTemp) iter
						.next();
				accountNoArray.put(accountDetails
				.getAccountName()+" // "+accountDetails.getAccountNo());
			}
		}catch(Exception e){
				e.printStackTrace();
		}
						
	}

	out.println(accountNoArray.toString());
%>

