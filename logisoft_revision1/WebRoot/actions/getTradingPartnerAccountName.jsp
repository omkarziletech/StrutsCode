<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.domain.CustomerTemp"%>
<jsp:directive.page
	import="com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO" />
<jsp:directive.page import="com.gp.cong.logisoft.domain.TradingPartner" />
<%
     	String account = "";
	String name = "";
        String importNumber = "";
	String type = "mb";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("MASTER_SEARCH_CUSTOMER")
	||functionName.equals("SEARCH_CUSTOMER")) {
		if (request.getParameter("name") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			name = request.getParameter("name");
			type="mb";
		}else if (request.getParameter("name") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			name = request.getParameter("name");
			type="master";
		}

	}
        if(functionName.equals("IMPORT_AGENT_NUMBER")){
            if(request.getParameter("importAgentNumber") !=  null){                
                importNumber = request.getParameter("importAgentNumber");
                type="mb";
            }
       }

	JSONArray accountNoArray = new JSONArray();

	if (name != null && !name.trim().equals("")) {           
		TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
		List customerList = tradingPartnerDAO.getAccountName(account,
				name, null, type);               
		try {
			for (Object object : customerList) {
				accountNoArray.put(object.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        if(importNumber != null && !importNumber.trim().equals("")){
            TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();            
		List<TradingPartner> customerList = tradingPartnerDAO.getTradinPartnerAccountName(account,
				importNumber, null, type);               
                try{
                    Iterator iter = customerList.iterator();                    
                    while (iter.hasNext()) {                      
			TradingPartner tradingPartner = (TradingPartner) iter.next();                        
			accountNoArray.put(tradingPartner.getAccountno().toString() + "<-->"
					+ tradingPartner.getAccountName().trim());                       
		}                   
                }catch (Exception e) {
			e.printStackTrace();
		}

        }
        if("false".equals(request.getParameter("isDojo"))){            
            StringBuilder buffer = new StringBuilder();
            buffer.append("<UL>");
            for(int i =0; i < accountNoArray.length(); i++){
                buffer.append("<li>");
                buffer.append(accountNoArray.get(i));
                buffer.append("</li>");
       }
       buffer.append("</UL>");       
       out.println(buffer.toString());
       }else{
           out.println(accountNoArray.toString());
       }

	
%>

