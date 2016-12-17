<%@ page language="java"
         import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CustomerDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%@page import="com.gp.cvst.logisoft.AccountingConstants"%>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.PortsDAO"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.AgencyInfo"/>
<%
    String account = "";
    String name = "";
    String type = "mb";
    String functionName = null;
    String acctName = "";
    String acctType = null;
    String agentName = null, destination = null, portOfDischarge = null;

    if (request.getParameter("tabName") != null) {
        functionName = request.getParameter("tabName");
    }
    if (functionName == null) {
        return;
    }

    if (functionName.equals("AR_INVOICE")
            || functionName.equals("AP_INVOICE")) {

        if (request.getParameter("cusName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            name = request.getParameter("cusName");

        } else if (request.getParameter("custname") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            name = request.getParameter("custname");
        }
    } else if (functionName.equals("FCL_BL_CORRECTION")
            && request.getParameter("from") != null
            && request.getParameter("from").equalsIgnoreCase("0")) {
        acctName = request.getParameter("accountName");
    } else if (functionName.equals("ACCRUALS")) {
        if (request.getParameter("custname") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            name = request.getParameter("custname");
        } else if (request.getParameter("vendor") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            name = request.getParameter("vendor");
        }
    } else if (functionName.equals("APINQUIRY")) {
        if (request.getParameter("vendor") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            name = request.getParameter("vendor");
        }
    } else if (functionName.equals("CHECK_REGISTER")) {
        if (request.getParameter("vendorName") != null) {
            name = request.getParameter("vendorName");
        }
    } else if (functionName.equals("APREPORTS")) {
        if (request.getParameter("vendorName") != null) {
            name = request.getParameter("vendorName");
        }
    } else if (functionName.equals("ARINQUIRY")) {
        if (request.getParameter("customer") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            name = request.getParameter("customer");

        }
    } else if (functionName.equals("AR_APPLY_PAYMENTS")) {
        if (request.getParameter("customerName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("2")) {
            name = request.getParameter("customerName");

        } else if (request.getParameter("otherCustomerName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            name = request.getParameter("otherCustomerName");
        } else if (request.getParameter("otherCustomer") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("3")) {
            name = request.getParameter("otherCustomer");
        }
    } else if (functionName.equals("AGING_REPORTS")) {
        if (request.getParameter("customerName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            name = request.getParameter("customerName");
        }
    } else if (functionName.equals("FILE_NUMBER")) {
        if (request.getParameter("carrier") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            acctName = request.getParameter("carrier");
            acctType = "V";
        }
    } else if (functionName.equals("QUOTE")) {
        if (request.getParameter("customerName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            acctName = request.getParameter("customerName");
        } else if (request.getParameter("vendorName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("2")) {
            if (request.getParameter("nvo").equals("booking")) {
                acctType = "Z";
                acctName = request.getParameter("vendorName");
            } else {
                acctName = request.getParameter("vendorName");
            }
        } else if (request.getParameter("sslDescription") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("3")) {
            acctName = request.getParameter("sslDescription");
            acctType = "V%SS";
        } else if (request.getParameter("routedbymsg") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("4")) {
            acctName = request.getParameter("routedbymsg");
            acctType = "E";
        } else if (request.getParameter("client") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("5")) {
            if (request.getParameter("check") != null && request.getParameter("check").equals("true")) {
                acctName = "";
            } else {
                acctName = request.getParameter("client");
            }
        } else if (request.getParameter("shipper") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("6")) {
            acctName = request.getParameter("shipper");
        } else if (request.getParameter("forwarder") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("7")) {
            acctName = request.getParameter("forwarder");
        } else if (request.getParameter("conginee") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("8")) {
            acctName = request.getParameter("conginee");
        } else if (request.getParameter("agent") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("9")) {
            agentName = request.getParameter("agent");
            if (null != request.getParameter("portOfDischarge") && !request.getParameter("portOfDischarge").equals("")) {
                portOfDischarge = request.getParameter("portOfDischarge");
            }
            if (null != request.getParameter("destination") && !request.getParameter("destination").equals("")) {
                destination = request.getParameter("destination");
            }
        }
    } else if (functionName.equals("TRADING_PARTNER")) {
        if (request.getParameter("name") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            acctName = request.getParameter("name");
        }
    } else if (functionName.equals("BOOKING")) {
        if (request.getParameter("accountName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            acctName = request.getParameter("accountName");
        } else if (request.getParameter("shipperName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            acctName = request.getParameter("shipperName");
        } else if (request.getParameter("fowardername") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("2")) {
            acctName = request.getParameter("fowardername");
            //acctType="F";
        } else if (request.getParameter("consigneename") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("3")) {
            acctName = request.getParameter("consigneename");
            //acctType="C";
        } else if (request.getParameter("truckerName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("4")) {
            acctName = request.getParameter("truckerName");
        } else if (request.getParameter("routedByAgent") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("5")) {
            acctName = request.getParameter("routedByAgent");
        } else if (request.getParameter("spottingAccountName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("6")) {
            acctName = request.getParameter("spottingAccountName");
            //acctType="L";
        } else if (request.getParameter("agentName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("7")) {
            agentName = request.getParameter("agentName");
            if (null != request.getParameter("portOfDischarge") && !request.getParameter("portOfDischarge").equals("")) {
                portOfDischarge = request.getParameter("portOfDischarge");
            }
            if (null != request.getParameter("destination") && !request.getParameter("destination").equals("")) {
                destination = request.getParameter("destination");
            }
        }
    } else if (functionName.equals("ACCOUNTS_PAYABLE")) {
        if (request.getParameter("vendor") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            name = request.getParameter("vendor");

        }
    } else if (functionName.equals("BANKACCOUNT")) {
        if (request.getParameter("accountName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            name = request.getParameter("accountName");
        }
    } else if (functionName.equals("AR_CREDIT_HOLD")) {
        if (request.getParameter("customer") != null) {
            name = request.getParameter("customer");
            if (request.getParameter("customerType") != null && !request.getParameter("customerType").equals(AccountingConstants.AR_CREDIT_HOLD_SELECT)) {
                acctType = request.getParameter("customerType");
            }
        }
    } else if (functionName.equals("FCL_BILL_LADING")) {
        if (request.getParameter("accountName") != null
                && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
            acctName = request.getParameter("accountName");
            acctType = "V";
        } else if (request.getParameter("houseName") != null
                && request.getParameter("from") != null && request.getParameter("from").equals("4")) {
            acctName = request.getParameter("houseName");
        } else if (request.getParameter("consigneeName") != null
                && request.getParameter("from") != null && request.getParameter("from").equals("3")) {
            acctName = request.getParameter("consigneeName");
        } else if (request.getParameter("houseConsigneeName") != null
                && request.getParameter("from") != null && request.getParameter("from").equals("6")) {
            acctName = request.getParameter("houseConsigneeName");
        } else if (request.getParameter("notifyPartyName") != null
                && request.getParameter("from") != null && request.getParameter("from").equals("8")) {
            //acctType = "N";
            acctName = request.getParameter("notifyPartyName");
        } else if (request.getParameter("houseNotifyPartyName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("7")) {
            acctName = request.getParameter("houseNotifyPartyName");
        } else if (request.getParameter("forwardingAgentName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            acctName = request.getParameter("forwardingAgentName");
        } else if (request.getParameter("routedByAgent") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("9")) {
            acctName = request.getParameter("routedByAgent");
        } else if (request.getParameter("agentName") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("10")) {
            agentName = request.getParameter("agentName");
            if (null != request.getParameter("portOfDischarge") && !request.getParameter("portOfDischarge").equals("")) {
                portOfDischarge = request.getParameter("portOfDischarge");
            }
            if (null != request.getParameter("destination") && !request.getParameter("destination").equals("")) {
                destination = request.getParameter("destination");
            }
        }
    } else if (functionName.equals("FCL_BL")) {
        if (request.getParameter("billThirdPartyName") != null
                && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
            //acctType = "T";
            acctName = request.getParameter("billThirdPartyName");
        }
    }

    //JSONArray accountNoArray = new JSONArray();
    StringBuffer accountNoArray = new StringBuffer();

    accountNoArray.append("<ul>");
    if (name != null && !name.trim().equals("")) {
        CustomerDAO customerDAO = new CustomerDAO();
        List customerList = customerDAO.findForManagement(name,
                name, acctType, type);
        Iterator iter = customerList.iterator();
        try {
            while (iter.hasNext()) {
                CustomerTemp accountDetails = (CustomerTemp) iter.next();
                if (accountDetails != null) {
                    if (functionName.equals("AP_INVOICE")
                            || functionName.equals("ACCRUALS")
                            || functionName.equals("APINQUIRY")
                            || functionName.equals("CHECK_REGISTER")
                            || functionName.equals("APREPORTS")
                            || functionName.equals("ARINQUIRY")
                            || functionName.equals("ACCOUNTS_PAYABLE")
                            || functionName.equals("BANKACCOUNT")
                            || functionName.equals("AR_APPLY_PAYMENTS")
                            || functionName.equals("AGING_REPORTS")
                            || functionName.equals("AR_CREDIT_HOLD")) {
                        String city = accountDetails.getCity2();
                        String state = accountDetails.getState();
                        String addressLine = accountDetails.getAddress1();
                        if (null != city && !city.trim().equals("")
                                && null != state && !state.trim().equals("")
                                && null != type && !type.trim().equals("")
                                && null != addressLine && !addressLine.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + accountDetails.getAccountNo()
                                    + "'><b>"
                                    + accountDetails.getAccountName()
                                    + " <--> "
                                    + accountDetails.getAccountNo()
                                    + "  <font color='red'>"
                                    + accountDetails.getAccountType()
                                    + "</font></b><br/>"
                                    + accountDetails.getAddress1()
                                    + ",  " + accountDetails.getCity2()
                                    + ",  " + accountDetails.getState()
                                    + ";   " + accountDetails.getType()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")
                                && null != type
                                && !type.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + accountDetails.getAccountNo()
                                    + "'><b>"
                                    + accountDetails.getAccountName()
                                    + " <--> "
                                    + accountDetails.getAccountNo()
                                    + "  <font color='red'>"
                                    + accountDetails.getAccountType()
                                    + "</font></b><br/>"
                                    + accountDetails.getCity2() + ",  "
                                    + accountDetails.getState()
                                    + ";   " + accountDetails.getType()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")
                                && null != addressLine
                                && !addressLine.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + accountDetails.getAccountNo()
                                    + "'><b>"
                                    + accountDetails.getAccountName()
                                    + " <--> "
                                    + accountDetails.getAccountNo()
                                    + "  <font color='red'>"
                                    + accountDetails.getAccountType()
                                    + "</font></b><br/>"
                                    + accountDetails.getAddress1()
                                    + ",  " + accountDetails.getCity2()
                                    + ",  " + accountDetails.getState()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != addressLine
                                && !addressLine.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + accountDetails.getAccountNo()
                                    + "'><b>"
                                    + accountDetails.getAccountName()
                                    + " <--> "
                                    + accountDetails.getAccountNo()
                                    + "  <font color='red'>"
                                    + accountDetails.getAccountType()
                                    + "</font></b><br/>"
                                    + accountDetails.getAddress1()
                                    + ",  " + accountDetails.getCity2()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + accountDetails.getAccountNo()
                                    + "'><b>"
                                    + accountDetails.getAccountName()
                                    + " <--> "
                                    + accountDetails.getAccountNo()
                                    + "  <font color='red'>"
                                    + accountDetails.getAccountType()
                                    + "</font></b><br/>"
                                    + accountDetails.getCity2() + ",  "
                                    + accountDetails.getState()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + accountDetails.getAccountNo()
                                    + "'><b>"
                                    + accountDetails.getAccountName()
                                    + " <--> "
                                    + accountDetails.getAccountNo()
                                    + "  <font color='red'>"
                                    + accountDetails.getAccountType()
                                    + "</font></b><br/>"
                                    + accountDetails.getCity2()
                                    + "</li>");
                        } else {
                            accountNoArray.append("<li id='"
                                    + accountDetails.getAccountNo()
                                    + "'><b>"
                                    + accountDetails.getAccountName()
                                    + " <--> "
                                    + accountDetails.getAccountNo()
                                    + "  <font color='red'>"
                                    + accountDetails.getAccountType()
                                    + "</font></b></li>");
                        }

                    } else {
                        accountNoArray.append("<li id='"
                                + accountDetails.getAccountNo() + "'>"
                                + accountDetails.getAccountName()
                                + "</b></li>");
                    }
                } else {
                    accountNoArray.append("<li id='"
                            + accountDetails.getAccountNo() + "'><b>"
                            + accountDetails.getAccountName()
                            + "<font color='red'>"
                            + accountDetails.getAccountType()
                            + "</font></b></li>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    } else if (acctName != null && !acctName.trim().equals("")) {
        CustomerDAO customerDAO = new CustomerDAO();
        List customerList = customerDAO.findForManagement(acctName, acctName, acctType, type);

        if (request.getParameter("vendorName") != null && request.getParameter("from") != null
                && request.getParameter("from").equals("2") && request.getParameter("tabName") != null
                && request.getParameter("tabName").equalsIgnoreCase("QUOTE")) {
            if (request.getParameter("nvo") != null && request.getParameter("nvo").equalsIgnoreCase("booking")) {
                customerList = customerDAO.findForManagement1(acctName, acctName, acctType, type);
            } else {
                customerList = customerDAO.findForManagement1(acctName, acctName, null, type);
            }
        }
        if (request.getParameter("shipper") != null && request.getParameter("from") != null
                && request.getParameter("from").equals("6") && request.getParameter("tabName") != null
                && request.getParameter("tabName").equalsIgnoreCase("QUOTE")) {
            customerList = customerDAO.findForShipOrForOrCon(acctName, acctName, acctType, type, "shipper");
        }
        if (request.getParameter("forwarder") != null && request.getParameter("from") != null
                && request.getParameter("from").equals("7") && request.getParameter("tabName") != null
                && request.getParameter("tabName").equalsIgnoreCase("QUOTE")) {
            customerList = customerDAO.findForShipOrForOrCon(acctName, acctName, acctType, type, "forwarder");
        }
        if (request.getParameter("conginee") != null && request.getParameter("from") != null
                && request.getParameter("from").equals("8") && request.getParameter("tabName") != null
                && request.getParameter("tabName").equalsIgnoreCase("QUOTE")) {
            customerList = customerDAO.findForShipOrForOrCon(acctName, acctName, acctType, type, "consignee");
        }
        if (null != functionName && (functionName.equals("FCL_BILL_LADING") || functionName.equals("BOOKING"))) {
            customerList = customerDAO.findForManagement2(acctName, acctType, type);
        }
        Iterator iter = customerList.iterator();

        try {
            while (iter.hasNext()) {
                CustomerTemp accountDetails = (CustomerTemp) iter.next();
                if (functionName.equals("QUOTE")
                        || functionName.equals("BOOKING")
                        || functionName.equals("FCL_BILL_LADING")
                        || functionName.equals("FILE_NUMBER")
                        || functionName.equals("TRADING_PARTNER") || functionName.equals("FCL_BL_CORRECTION")
                        || functionName.equals("FCL_BL_CHARGES")) {
                    String city = accountDetails.getCity2();
                    String state = accountDetails.getState();
                    String addressLine = accountDetails.getAddress1();
                    String zip = null != accountDetails.getZip() ? ", " + accountDetails.getZip() : "";
                    if (null != city && !city.trim().equals("")
                            && null != state
                            && !state.trim().equals("") && null != type
                            && !type.trim().equals("")
                            && null != addressLine
                            && !addressLine.trim().equals("")) {
                        accountNoArray.append("<li id='"
                                + accountDetails.getAccountNo()
                                + "'><b>"
                                + accountDetails.getAccountName()
                                + " <--> <font color='blue'>"
                                + accountDetails.getAccountNo()
                                + "  </font><font color='red'>"
                                + accountDetails.getAccountType()
                                + (accountDetails.getAccountType().contains("V")
                                && null != accountDetails.getSubType()
                                && !accountDetails.getSubType().trim().isEmpty()
                                ? (" - " + accountDetails.getSubType()) : "")
                                + "</font></b><br/><font style='font-weight: normal;'>"
                                + accountDetails.getAddress1() + "</font>,  "
                                + accountDetails.getCity2() + ",  "
                                + accountDetails.getState()
                                + zip + ";"
                                + "</li>");
                    } else if (null != city && !city.trim().equals("")
                            && null != state
                            && !state.trim().equals("") && null != type
                            && !type.trim().equals("")) {
                        accountNoArray.append("<li id='"
                                + accountDetails.getAccountNo()
                                + "'><b>"
                                + accountDetails.getAccountName()
                                + " <--> <font color='blue'>"
                                + accountDetails.getAccountNo()
                                + " </font> <font color='red'>"
                                + accountDetails.getAccountType()
                                + (accountDetails.getAccountType().contains("V")
                                && null != accountDetails.getSubType()
                                && !accountDetails.getSubType().trim().isEmpty()
                                ? (" - " + accountDetails.getSubType()) : "")
                                + "</font></b><br/>"
                                + accountDetails.getCity2() + ",  "
                                + accountDetails.getState()
                                + zip + ";   "
                                + "</li>");
                    } else if (null != city && !city.trim().equals("")
                            && null != state
                            && !state.trim().equals("")
                            && null != addressLine
                            && !addressLine.trim().equals("")) {
                        accountNoArray.append("<li id='"
                                + accountDetails.getAccountNo()
                                + "'><b>"
                                + accountDetails.getAccountName()
                                + " <--> <font color='blue'>"
                                + accountDetails.getAccountNo()
                                + " </font> <font color='red'>"
                                + accountDetails.getAccountType()
                                + (accountDetails.getAccountType().contains("V")
                                && null != accountDetails.getSubType()
                                && !accountDetails.getSubType().trim().isEmpty()
                                ? (" - " + accountDetails.getSubType()) : "")
                                + "</font></b><br/><font style='font-weight: normal;'>"
                                + accountDetails.getAddress1() + "</font>,  "
                                + accountDetails.getCity2() + ",  "
                                + accountDetails.getState()
                                + zip + "</li>");
                    } else if (null != city && !city.trim().equals("")
                            && null != addressLine
                            && !addressLine.trim().equals("")) {
                        accountNoArray.append("<li id='"
                                + accountDetails.getAccountNo()
                                + "'><b>"
                                + accountDetails.getAccountName()
                                + " <--> <font color='blue'>"
                                + accountDetails.getAccountNo()
                                + " </font> <font color='red'>"
                                + accountDetails.getAccountType()
                                + (accountDetails.getAccountType().contains("V")
                                && null != accountDetails.getSubType()
                                && !accountDetails.getSubType().trim().isEmpty()
                                ? (" - " + accountDetails.getSubType()) : "")
                                + "</font></b><br/><font style='font-weight: normal;'>"
                                + accountDetails.getAddress1() + "</font>,  "
                                + accountDetails.getCity2()
                                + zip + "</li>");
                    } else if (null != city && !city.trim().equals("")
                            && null != state
                            && !state.trim().equals("")) {
                        accountNoArray.append("<li id='"
                                + accountDetails.getAccountNo()
                                + "'><b>"
                                + accountDetails.getAccountName()
                                + " <--> <font color='blue'>"
                                + accountDetails.getAccountNo()
                                + " </font> <font color='red'>"
                                + accountDetails.getAccountType()
                                + (accountDetails.getAccountType().contains("V")
                                && null != accountDetails.getSubType()
                                && !accountDetails.getSubType().trim().isEmpty()
                                ? (" - " + accountDetails.getSubType()) : "")
                                + "</font></b><br/>"
                                + accountDetails.getCity2() + ",  "
                                + accountDetails.getState()
                                + zip + "</li>");
                    } else if (null != city && !city.trim().equals("")) {
                        accountNoArray.append("<li id='"
                                + accountDetails.getAccountNo()
                                + "'><b>"
                                + accountDetails.getAccountName()
                                + " <--> <font color='blue'>"
                                + accountDetails.getAccountNo()
                                + " </font> <font color='red'>"
                                + accountDetails.getAccountType()
                                + (accountDetails.getAccountType().contains("V")
                                && null != accountDetails.getSubType()
                                && !accountDetails.getSubType().trim().isEmpty()
                                ? (" - " + accountDetails.getSubType()) : "")
                                + "</font></b><br/>"
                                + accountDetails.getCity2()
                                + zip + "</li>");
                    } else {
                        accountNoArray.append("<li id='"
                                + accountDetails.getAccountNo()
                                + "'><b>"
                                + accountDetails.getAccountName()
                                + "</b> <-->  <font color='blue'>"
                                + accountDetails.getAccountNo()
                                + " </font> <font color='red'>"
                                + accountDetails.getAccountType()
                                + (accountDetails.getAccountType().contains("V")
                                && null != accountDetails.getSubType()
                                && !accountDetails.getSubType().trim().isEmpty()
                                ? (" - " + accountDetails.getSubType()) : "")
                                + "</font></li>");
                    }

                } else {
                    accountNoArray.append("<li id='"
                            + accountDetails.getAccountNo() + "'><b>"
                            + accountDetails.getAccountName()
                            + " <--> " + accountDetails.getAccountNo()
                            + "</b></li>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//----THIS IS TO FETCH AGENT NAME BASED ON DESTINATION OR PORT OF DESTINATION----
    else if (agentName != null && !agentName.trim().equals("")) {
        List agentList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();
        String portName = "";
        if (null != portOfDischarge) {
            portName = portOfDischarge;
            int i = portName.indexOf("/");
            if (i != -1) {
                portName = portName.substring(0, i);
            }
            agentList = portsDAO.getPortsForAgentInfo(portName);
        }
        if (agentList == null || agentList.size() == 0) {
            if (null != destination) {
                portName = destination;
                int i = portName.indexOf("/");
                if (i != -1) {
                    portName = portName.substring(0, i);
                }
                agentList = portsDAO.getPortsForAgentInfo(portName);
            }
        }

        Iterator iter = agentList.iterator();
        try {
            while (iter.hasNext()) {
                CustomerTemp customerTemp = (CustomerTemp) iter.next();
                if (customerTemp != null) {
                    if (functionName.equals("BOOKING")
                            || functionName.equals("FCL_BILL_LADING") || functionName.equals("QUOTE")) {
                        String city = customerTemp.getCity2();
                        String state = customerTemp.getState();
                        String addressLine = customerTemp.getAddress1();
                        if (null != city && !city.trim().equals("")
                                && null != state && !state.trim().equals("")
                                && null != type && !type.trim().equals("")
                                && null != addressLine && !addressLine.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + customerTemp.getAccountNo()
                                    + "'><b>"
                                    + customerTemp.getAccountName()
                                    + " <--> "
                                    + customerTemp.getAccountNo()
                                    + "  <font color='red'>"
                                    + customerTemp.getAccountType()
                                    + "</font></b><br/>"
                                    + customerTemp.getAddress1()
                                    + ",  " + customerTemp.getCity2()
                                    + ",  " + customerTemp.getState()
                                    + ";   " + customerTemp.getType()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")
                                && null != type
                                && !type.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + customerTemp.getAccountNo()
                                    + "'><b>"
                                    + customerTemp.getAccountName()
                                    + " <--> "
                                    + customerTemp.getAccountNo()
                                    + "  <font color='red'>"
                                    + customerTemp.getAccountType()
                                    + "</font></b><br/>"
                                    + customerTemp.getCity2() + ",  "
                                    + customerTemp.getState()
                                    + ";   " + customerTemp.getType()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")
                                && null != addressLine
                                && !addressLine.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + customerTemp.getAccountNo()
                                    + "'><b>"
                                    + customerTemp.getAccountName()
                                    + " <--> "
                                    + customerTemp.getAccountNo()
                                    + "  <font color='red'>"
                                    + customerTemp.getAccountType()
                                    + "</font></b><br/>"
                                    + customerTemp.getAddress1()
                                    + ",  " + customerTemp.getCity2()
                                    + ",  " + customerTemp.getState()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != addressLine
                                && !addressLine.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + customerTemp.getAccountNo()
                                    + "'><b>"
                                    + customerTemp.getAccountName()
                                    + " <--> "
                                    + customerTemp.getAccountNo()
                                    + "  <font color='red'>"
                                    + customerTemp.getAccountType()
                                    + "</font></b><br/>"
                                    + customerTemp.getAddress1()
                                    + ",  " + customerTemp.getCity2()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")
                                && null != state
                                && !state.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + customerTemp.getAccountNo()
                                    + "'><b>"
                                    + customerTemp.getAccountName()
                                    + " <--> "
                                    + customerTemp.getAccountNo()
                                    + "  <font color='red'>"
                                    + customerTemp.getAccountType()
                                    + "</font></b><br/>"
                                    + customerTemp.getCity2() + ",  "
                                    + customerTemp.getState()
                                    + "</li>");
                        } else if (null != city
                                && !city.trim().equals("")) {
                            accountNoArray.append("<li id='"
                                    + customerTemp.getAccountNo()
                                    + "'><b>"
                                    + customerTemp.getAccountName()
                                    + " <--> "
                                    + customerTemp.getAccountNo()
                                    + "  <font color='red'>"
                                    + customerTemp.getAccountType()
                                    + "</font></b><br/>"
                                    + customerTemp.getCity2()
                                    + "</li>");
                        } else {
                            accountNoArray.append("<li id='"
                                    + customerTemp.getAccountNo()
                                    + "'><b>"
                                    + customerTemp.getAccountName()
                                    + " <--> "
                                    + customerTemp.getAccountNo()
                                    + "  <font color='red'>"
                                    + customerTemp.getAccountType()
                                    + "</font></b></li>");
                        }

                    } else {
                        accountNoArray.append("<li id='"
                                + customerTemp.getAccountNo() + "'>"
                                + customerTemp.getAccountName()
                                + "</b></li>");
                    }
                } else {
                    accountNoArray.append("<li id='"
                            + customerTemp.getAccountNo() + "'><b>"
                            + customerTemp.getAccountName()
                            + "<font color='red'>"
                            + customerTemp.getAccountType()
                            + "</font></b></li>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    accountNoArray.append("</ul>");
    out.println(accountNoArray.toString());
    //out.println("<ul><li id='1'>val 1</li><li id='2'>val 2</li><li id='3'>val 3</li><li id='4'>val 4</li><li id='5'>val 5</li></ul>");
%>

