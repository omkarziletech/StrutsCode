<%@ page language="java"
         import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO,com.gp.cong.logisoft.domain.TradingPartner"%>
<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%
            String account = "";
            String name = "";
            String type = "mb";
            String functionName = null;
            String bankAccount = "";

            JSONArray accountNoArray = new JSONArray();

            if (request.getParameter("tabName") != null) {
                functionName = request.getParameter("tabName");
            }

            if (functionName == null) {
                return;
            }

            if (functionName.equals("TERMINAL")
                    && request.getParameter("from") != null
                    && request.getParameter("from").equalsIgnoreCase("0")) {
                account = request.getParameter("acctno");
            }
            StringBuffer accountBuilder = new StringBuffer();
            accountBuilder.append("<ul class='bold' style='width:500px; height:200px'>");
            if (account != null && !account.trim().equals("")) {
                String acctName, acctNumber, acctType, eciAcctType, address, city, state, zip, salescode, subType, sslineNo;
                TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
                List<TradingPartner> accountList = tradingPartnerDAO.getAccoutNumber(account);
                Iterator iter = accountList.iterator();
                while (iter.hasNext()) {
                    Object[] obj = (Object[]) iter.next();
                    acctNumber = null != obj[1] ? obj[1].toString() : "";
                    acctName = null != obj[0] ? obj[0].toString() : "";
                    acctType = null != obj[2] ? obj[2].toString() : "";
                    address = null != obj[3] ? obj[3].toString() : "";
                    city = null != obj[4] ? obj[4].toString() : "";
                    state = null != obj[5] ? obj[5].toString() : "";
                    zip = null != obj[6] ? obj[6].toString() : "";
                    subType = null != obj[8] ? obj[8].toString() : "";
                    sslineNo = null != obj[9] ? obj[9].toString() : "";
                    eciAcctType = null != obj[13] ? obj[13].toString() : "";
                    salescode = null != obj[15] ? obj[15].toString() : "";
                    accountBuilder.append("<li style='font-size:10px;'><span class='blue'>").append(acctName).append("</span>");
                    accountBuilder.append("<---><span  class='magenta'>").append(acctNumber).append("</span>");
                    if (CommonUtils.isNotEmpty(acctType)) {
                        accountBuilder.append("<span class='red'>, ").append(acctType).append("</span>");
                    }
                    if (CommonUtils.isEqualIgnoreCase(subType, "Steamship Line") && CommonUtils.isNotEmpty(sslineNo)) {
                        accountBuilder.append("<span class='green'> - (").append(subType).append(": ").append(sslineNo).append(")</span>");
                    } else if (CommonUtils.isNotEmpty(subType)) {
                        accountBuilder.append("<span class='green'> - (").append(subType).append(")</span>");
                    }
                    if (CommonUtils.isNotEmpty(salescode)) {
                        accountBuilder.append("</span><span class='magenta'>, SP=").append(salescode);
                    }
                    accountBuilder.append("</span><span class='green'>,").append(eciAcctType).append("</span>").append("<br>");
                    if (CommonUtils.isAtLeastOneNotEmpty(address, city, state, zip)) {
                        accountBuilder.append("<span class='grey font-8px'>");
                        if (CommonUtils.isNotEmpty(address)) {
                            accountBuilder.append(address);
                        }
                         if (CommonUtils.isNotEmpty(city)) {
                            accountBuilder.append(",").append(city);
                        }
                         if (CommonUtils.isNotEmpty(state)) {
                            accountBuilder.append(",").append(state);
                        }
                        if (CommonUtils.isNotEmpty(zip)) {
                            accountBuilder.append(",").append(zip);
                        }
                         accountBuilder.append("</span>");
                    }
                    accountBuilder.append("</li><hr/>");
                }
            }
            accountBuilder.append("</ul>");
            out.println(accountBuilder.toString());
%>