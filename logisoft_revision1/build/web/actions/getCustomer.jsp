<%@ page language="java"
         import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.domain.CustomerTemp,com.gp.cong.logisoft.domain.Customer"
         pageEncoding="ISO-8859-1"%>
<jsp:directive.page
    import="com.gp.cong.logisoft.hibernate.dao.CustomerDAO" />
<%

    String acctno = "";
    String accountNumber = "";
    String acctname = "";
    String acctType = null;
    String index = "";
    String functionName = null;
    if (request.getParameter("tabName") != null) {
        functionName = request.getParameter("tabName");
    }

    if (functionName == null) {
        return;
    }
    if (functionName.equals("BOOKING")
            || functionName.equals("FCL_BL_CHARGES")
            || functionName.equals("FCL_BILL_LADDING")
            || functionName.equals("FCL_BL")
            || functionName.equals("QUOTE")) {
        if (request.getParameter("index") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            index = request.getParameter("index");
            acctType = "V";
            acctno = request.getParameter("accountno" + index);
        } else if (request.getParameter("shipper") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("3")) {
            acctno = request.getParameter("shipper");
        } else if (request.getParameter("billTrePty") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            acctno = request.getParameter("billTrePty");
        } else if (request.getParameter("consignee") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("2")) {
            acctno = request.getParameter("consignee");
        } else if (request.getParameter("houseConsignee") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("4")) {
            acctno = request.getParameter("houseConsignee");
        } else if (request.getParameter("index") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("5")) {
            index = request.getParameter("index");
            acctType = "V";
            acctno = request.getParameter("otheraccountno" + index);
        } else if (request.getParameter("houseShipper") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("6")) {
            acctno = request.getParameter("houseShipper");
        } else if (request.getParameter("alternateAgent") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("7")) {
            acctno = request.getParameter("alternateAgent");
        } else if (request.getParameter("houseNotifyParty") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("8")) {
            acctno = request.getParameter("houseNotifyParty");
        } else if (request.getParameter("notifyParty") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("9")) {
            acctno = request.getParameter("notifyParty");
        } else if (request.getParameter("forwardingAgent1") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("10")) {
            acctno = request.getParameter("forwardingAgent1");
        } else if (request.getParameter("truckerCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("11")) {
            acctno = request.getParameter("truckerCode");
        } else if (request.getParameter("index1") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("12")) {
            index = request.getParameter("index1");
            acctType = "V";
            acctno = request.getParameter("collapseaccountno" + index);
        } else if (request.getParameter("forwarder") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("13")) {
            acctno = request.getParameter("forwarder");
        }
    } else if (functionName.equals("WAREHOUSE")) {
        if (request.getParameter("ipiVendor") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            acctno = request.getParameter("ipiVendor");
        }

    } else if (functionName.equals("AP_INQUIRY")) {
        if (request.getParameter("vendor") != null) {
            accountNumber = request.getParameter("vendor");
        }
    } else if (functionName.equals("ACCOUNTS_RECIEVABLE_INQUIRY")) {
        if (request.getParameter("customerName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            accountNumber = request.getParameter("customerName");
        }
    } else if (functionName.equalsIgnoreCase("SEARCH_FCL")
            || functionName.equalsIgnoreCase("FCL_SELL_RATE")
            || functionName.equalsIgnoreCase("ADD_FCL_POPUP")) {
        if (request.getParameter("sslinenumber") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            acctno = request.getParameter("sslinenumber");
        }
    } else if (functionName.equalsIgnoreCase("PORTS")) {
        if (request.getParameter("agentAcountNo") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            acctno = request.getParameter("agentAcountNo");
        } else if (request.getParameter("agentName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            acctname = request.getParameter("agentName");
        } else if (request.getParameter("consigneeAcctNo") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("2")) {
            acctno = request.getParameter("consigneeAcctNo");
            acctType = "C";
        } else if (request.getParameter("consigneeName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("3")) {
            acctname = request.getParameter("consigneeName");
            acctType = "C";
        } else if (request.getParameter("consigneeAcctNo") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("4")) {
            acctno = request.getParameter("consigneeAcctNo");
        } else if (request.getParameter("consigneeName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("5")) {
            acctname = request.getParameter("consigneeName");
        } else if (request.getParameter("acAccountPickup") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            acctno = request.getParameter("acAccountPickup");
        } else if (request.getParameter("asetupAcct") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            acctno = request.getParameter("asetupAcct");
        }
    }

    JSONArray accountNoArray = new JSONArray();

    if ((acctno != null && !acctno.trim().equals("")) || (acctname != null && !acctname.trim().equals(""))) {
        CustomerDAO customerDAO = new CustomerDAO();
        List list = null;
        if (acctType == null) {
            list = customerDAO.findForAgentNumber(acctno, acctname);
        } else {
            list = customerDAO.findForConsigneetNumber(acctno, acctname, acctType);
        }
        Iterator iter = list.iterator();
        String disabled = "";
        while (iter.hasNext()) {
            CustomerTemp accountDetails = (CustomerTemp) iter.next();
            disabled = customerDAO.isAccountDisabled(accountDetails.getAccountNo()) ? "<font color=red>,DISABLED</font>" : "";
            if (functionName.equals("PORTS")) {
                accountNoArray.put("<font class='blue-70'>" + accountDetails.getAccountNo() + ":- </font>"
                        + "<font class='green'>" + accountDetails.getAccountName() + "</font>" + (accountDetails.getAccountType() != null ? "-" + "<font class='red-90'><b>" + accountDetails.getAccountType() + "</b></font>" + disabled : ""));
            } else {
                accountNoArray.put("<font class='blue-70'>" + accountDetails.getAccountNo() + ":- </font><font class='green'> "
                        + accountDetails.getAccountName() + "</font>" + disabled);
            }

        }
    } else if (accountNumber != null && !accountNumber.equals("")) {
        List list = null;
        CustomerDAO customerDAO = new CustomerDAO();
        if (acctType == null) {
            list = customerDAO.findForAgentNumber(acctno, acctname);
        } else {
            list = customerDAO.findForConsigneetNumber(accountNumber, acctname,
                    acctType);
        }
        Iterator iter = list.iterator();
        try {
            while (iter.hasNext()) {
                CustomerTemp accountDetails = (CustomerTemp) iter.next();
                if (accountDetails != null) {
                    if (functionName
                            .equals("ACCOUNTS_RECIEVABLE_INQUIRY")
                            || functionName
                            .equals("AP_INQUIRY")) {
                        String city = accountDetails.getCity2();
                        String state = accountDetails.getState();
                        String type = accountDetails.getType();
                        String addressLine = accountDetails.getAddress1();

                        if (null != city && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")
                                && null != type
                                && !type.trim().equals("")
                                && null != addressLine
                                && !addressLine.trim().equals("")) {
                            accountNoArray.put(accountDetails
                                    .getAccountNo()
                                    + ":-   "
                                    + accountDetails.getAccountName()
                                    + ":-   "
                                    + accountDetails.getAddress1()
                                    + ",  "
                                    + accountDetails.getCity2()
                                    + ",  "
                                    + accountDetails.getState()
                                    + ":-  "
                                    + accountDetails.getType());
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")
                                && null != type
                                && !type.trim().equals("")) {
                            accountNoArray.put(accountDetails
                                    .getAccountNo()
                                    + ":-   "
                                    + accountDetails.getAccountName()
                                    + ":-   "
                                    + accountDetails.getCity2()
                                    + ",  "
                                    + accountDetails.getState()
                                    + ":-   "
                                    + accountDetails.getType());
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")
                                && null != addressLine
                                && !addressLine.trim().equals("")) {
                            accountNoArray.put(accountDetails
                                    .getAccountNo()
                                    + ":-   "
                                    + accountDetails.getAccountName()
                                    + ":-;   "
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
                                    .getAccountNo()
                                    + ":-    "
                                    + accountDetails.getAccountName()
                                    + ":-    "
                                    + accountDetails.getAddress1()
                                    + ",  "
                                    + accountDetails.getCity2());
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")) {
                            accountNoArray.put(accountDetails
                                    .getAccountNo()
                                    + ":-   "
                                    + accountDetails.getAccountName()
                                    + ":-   "
                                    + accountDetails.getCity2()
                                    + ",  "
                                    + accountDetails.getState());
                        } else if (null != city
                                && !city.trim().equals("")) {
                            accountNoArray.put(accountDetails
                                    .getAccountNo()
                                    + ":-   "
                                    + accountDetails.getAccountName()
                                    + ":-   "
                                    + accountDetails.getCity2());
                        } else {
                            accountNoArray.put(accountDetails.getAccountNo()
                                    + ":-   "
                                    + accountDetails.getAccountName());
                        }

                    } else {
                        accountNoArray.put(accountDetails
                                .getAccountNo());
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    if ("false".equals(request.getParameter("isDojo"))) {
        StringBuilder buffer = new StringBuilder("<UL>");
        for (int i = 0; i < accountNoArray.length(); i++) {
            buffer.append("<li>");
            buffer.append(accountNoArray.get(i));
            buffer.append("</li>");
        }
        buffer.append("</UL>");
        out.println(buffer.toString());
    } else {
        out.println(accountNoArray.toString());
    }
%>
