<%-- 
    Document   : getClientResults
    Created on : Jul 23, 2012, 4:03:09 PM
    Author     : Lakshmi Naryanan
--%>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@page import="com.logiware.accounting.model.TradingPartnerModel"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO"%>
<%@page import="java.util.List"%>
<%@page import="com.gp.cong.common.CommonUtils"%>
<%
    try {
	String input = request.getParameter("input");
        TradingPartnerDAO tradingPartnerDAO=new TradingPartnerDAO();
	if (CommonUtils.isEqualIgnoreCase(input, request.getParameter("enabled"))) {
	    String field = request.getParameter("field");
	    String value = request.getParameter(field);
	    if (CommonUtils.isNotEmpty(value)) {
		boolean isConsignee = CommonUtils.isEqualIgnoreCase(request.getParameter("consignee"), "true");
		boolean isDisplayOneLine = CommonUtils.isEqualIgnoreCase(request.getParameter("displayOneLine"), "true");
		String state = request.getParameter("state");
		String zipCode = request.getParameter("zipCode");
		String salesCode = request.getParameter("salesCode");
		boolean importFlag = importFlag=(null!=session.getAttribute(ImportBc.sessionName))?true:false;
		String blueScrAcctNo = "";
		List<TradingPartnerModel> results = tradingPartnerDAO.getClientResults(input, value, isConsignee, state, zipCode, salesCode,importFlag);
		if (CommonUtils.isNotEmpty(results)) {
		    StringBuilder resultBuilder = new StringBuilder();
		    resultBuilder.append("<ul class='autocompleter-ul-").append(isDisplayOneLine ? "single" : "broken").append("'>");
		    for (TradingPartnerModel model : results) {
			resultBuilder.append("<li id='").append(model.getName().replace("'", "&#39;"));
			resultBuilder.append("^").append(model.getNumber().replace("'", "&#39;"));
			if (CommonUtils.isEqualIgnoreCase(input, "Contact") || CommonUtils.isEqualIgnoreCase(input, "Email")) {
			    resultBuilder.append("^").append(null != model.getContact() ? model.getContact().replace("'", "&#39;") : "");
			    resultBuilder.append("^").append(null != model.getEmail() ? model.getEmail().replace("'", "&#39;") : "");
			}
			resultBuilder.append("'>");
			resultBuilder.append("<font class='blue-70'> ").append(model.getName().replace("'", "&#39;")).append("</font>");
                        resultBuilder.append("</font>");
			resultBuilder.append("<font class='red-90'> <--> ").append(model.getNumber().replace("'", "&#39;")).append("</font>");
			resultBuilder.append(" <font class='red'> , ").append(model.getType().replace("'", "&#39;")).append("</font>");
			if (CommonUtils.isNotEmpty(model.getSalesCode())) {
			    resultBuilder.append("<font color='#FF00FF'> , SP=").append(model.getSalesCode().replace("'", "&#39;")).append("</font>");
			}
                        if(null!=model.getType() && model.getType().equalsIgnoreCase("V") && null!=model.getSubType() &&  (model.getSubType().equalsIgnoreCase("Steamship Line") || model.getSubType().equalsIgnoreCase("Air Line") && (CommonUtils.isNotEmpty(model.getType())))){
                            blueScrAcctNo=model.getType();
                        }else if(null!=model.getType() && model.getType().equalsIgnoreCase("V") && null!=model.getSubType() && (model.getSubType().equalsIgnoreCase("Forwarder") && CommonUtils.isNotEmpty(model.getBs_ship_fwd_no()))){
                            blueScrAcctNo=model.getBs_ship_fwd_no();
                        }else if(null!=model.getType() && model.getType().equalsIgnoreCase("S") && CommonUtils.isNotEmpty(model.getBs_ship_fwd_no())){
                            blueScrAcctNo=model.getBs_ship_fwd_no();
                        }else if(null!=model.getType() && model.getType().equalsIgnoreCase("C") && CommonUtils.isNotEmpty(model.getBs_cons_no())){
                            blueScrAcctNo=model.getBs_cons_no();
                        }else if(CommonUtils.isNotEmpty(model.getBs_ship_fwd_no())){
                            blueScrAcctNo=model.getBs_ship_fwd_no();
                        }
                        if(CommonUtils.isNotEmpty(blueScrAcctNo)){
                            resultBuilder.append("<font class='green'> , ").append(blueScrAcctNo).append("</font>");
                        }
                        if(importFlag && tradingPartnerDAO.isImportCreadit(model.getNumber())){
                            resultBuilder.append(",<font class='green' size='2' style='font-weight: bold;'> $ </font>");
                        }
			if (CommonUtils.isNotEmpty(model.getAddress())) {
			    resultBuilder.append(isDisplayOneLine ? " , " : "<br/>");
			    resultBuilder.append("<font style='font-weight: normal;'>").append(model.getAddress().replace("'", "&#39;")).append("</font>");
			}
			if (model.isDisabled()) {
			    resultBuilder.append("<font class='red' style='font-weight: bold;'> , DISABLED</font>");
			}
			resultBuilder.append("</li>");
		    }
		    resultBuilder.append("</ul>");
		    out.println(resultBuilder.toString());
		}
	    }
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
%>