<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@ page language="java"
         import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%@page import="com.gp.cvst.logisoft.AccountingConstants"%>
<%@page import="com.gp.cong.common.CommonUtils"%>
<%
            try {
                String accountName = null;
                String functionName = null;
                String accountType = null;
                String clientType = null;
                String agent = null;
                String pod = null;
                String destination = null;
                String disableDojo = null;
                String sslSubType = "";
                String consigneeCheck = "";
                String tradingPartnerAccount = "";
                boolean sslName = false;
                boolean disabledAccount = false;
                boolean flag = (null != session.getAttribute(ImportBc.sessionName)) ? true : false;
                if (request.getParameter("tabName") != null) {
                    functionName = request.getParameter("tabName");
                }
                if (null != request.getParameter("acctTyp")) {
                    accountType = request.getParameter("acctTyp");
                }
                if ("TRADING_PARTNER".equals(functionName)) {
                    if (request.getParameter("name") != null
                            && request.getParameter("from") != null
                            && "0".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("name");
                    } else if (request.getParameter("forwardAccountName") != null
                            && request.getParameter("from") != null
                            && "0".equals(request.getParameter("from"))) {
                        disabledAccount = true;
                        accountName = request.getParameter("forwardAccountName");
                        tradingPartnerAccount = request.getParameter("tradingPartnerAccount");
                    }
                } else if ("CONTACTPOPUP".equals(functionName)) {
                    if (request.getParameter("custName") != null
                            && request.getParameter("from") != null
                            && "0".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("custName");
                    }
                } else if ("TERMINAL".equals(functionName)) {
                    if (request.getParameter("zaccount") != null
                            && request.getParameter("from") != null && "0".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("zaccount");
                    }
                } else if ("QUOTE".equals(functionName)) {
                    if (request.getParameter("customerName") != null
                            && request.getParameter("from") != null
                            && "0".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("customerName");
                        consigneeCheck = request.getParameter("consigneeCheck");
                    } else if (request.getParameter("customerName") != null
                            && request.getParameter("from") != null
                            && "2".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("customerName");
                    } else if (request.getParameter("sslDescription") != null
                            && request.getParameter("from") != null
                            && "1".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("sslDescription");
                        accountType = "V";
                        sslSubType = "Steamship Line";
                    } else if (request.getParameter("sslineName") != null
                            && request.getParameter("from") != null
                            && "7".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("sslineName");
                        accountType = "V";
                        sslSubType = "Steamship Line";
                    } else if (request.getParameter("routedbymsg") != null
                            && request.getParameter("from") != null
                            && "2".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("routedbymsg");
                        //accountType = "E";
                    } else if (request.getParameter("agent") != null
                            && request.getParameter("from") != null
                            && "3".equals(request.getParameter("from"))) {
                        agent = request.getParameter("agent");
                        pod = request.getParameter("portOfDischarge");
                        destination = request.getParameter("destination");
                        accountName = "dummy";
                        if (null != destination && !destination.equals("")) {
                            if (destination.lastIndexOf("(") != -1 && destination.lastIndexOf(")") != -1) {
                                destination = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
                            }
                        }
                        if (null != pod && !pod.equals("")) {
                            if (pod.lastIndexOf("(") != -1 && pod.lastIndexOf(")") != -1) {
                                pod = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
                            }
                        }
                    } else if (request.getParameter("carrier") != null
                            && request.getParameter("from") != null
                            && "4".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("carrier");
                        accountType = "V";
                    } else if (request.getParameter("vendorName") != null
                            && request.getParameter("from") != null
                            && "5".equals(request.getParameter("from"))) {
                        if ("booking".equals(request.getParameter("nvo"))) {
                            accountType = "Z";
                            accountName = request.getParameter("vendorName");
                        } else {
                            accountName = request.getParameter("vendorName");
                        }
                    }
                } else if ("BOOKING".equals(functionName)) {
                    if (request.getParameter("accountName") != null
                            && request.getParameter("from") != null
                            && "0".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("accountName");
                    } else if (request.getParameter("routedByAgent") != null
                            && request.getParameter("from") != null
                            && "1".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("routedByAgent");
                    } else if (request.getParameter("agent") != null
                            && request.getParameter("from") != null
                            && "2".equals(request.getParameter("from"))) {
                        agent = request.getParameter("agent");
                        destination = request.getParameter("portOfDischarge");
                        pod = request.getParameter("destination");
                        accountName = "dummy";
                        if (null != pod && !pod.equals("")) {
                            if (pod.lastIndexOf("(") != -1 && pod.lastIndexOf(")") != -1) {
                                pod = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
                            }
                        }
                        if (null != destination && !destination.equals("")) {
                            if (destination.lastIndexOf("(") != -1 && destination.lastIndexOf(")") != -1) {
                                destination = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
                            }
                        }
                    } else if (request.getParameter("shipperName") != null
                            && request.getParameter("from") != null
                            && "3".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("shipperName");
                        disableDojo = request.getParameter("disableShipperDojo");
                    } else if (request.getParameter("fowardername") != null
                            && request.getParameter("from") != null
                            && "4".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("fowardername");
                    } else if (request.getParameter("consigneename") != null
                            && request.getParameter("from") != null
                            && "5".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("consigneename");
                        disableDojo = request.getParameter("disableConsigneeDojo");
                    } else if (request.getParameter("spottingAccountName") != null
                            && request.getParameter("from") != null
                            && "6".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("spottingAccountName");
                        disableDojo = request.getParameter("disableSpottAddrDojo");
                    } else if (request.getParameter("truckerName") != null
                            && request.getParameter("from") != null
                            && "7".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("truckerName");
                        disableDojo = request.getParameter("disableTruckerDojo");
                    } else if (request.getParameter("vendorName") != null
                            && request.getParameter("from") != null
                            && "8".equals(request.getParameter("from"))) {
                        if ("booking".equals(request.getParameter("nvo"))) {
                            accountType = "Z";
                            accountName = request.getParameter("vendorName");
                        } else {
                            accountName = request.getParameter("vendorName");
                        }
                    }
                } else if ("FCL_BL".equals(functionName)) {
                    if (request.getParameter("billThirdPartyName") != null
                            && request.getParameter("from") != null
                            && "0".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("billThirdPartyName");
                    } else if (request.getParameter("agent") != null
                            && request.getParameter("from") != null
                            && "1".equals(request.getParameter("from"))) {
                        agent = request.getParameter("agent");
                        destination = request.getParameter("destination");
                        pod = request.getParameter("portOfDischarge");
                        accountName = "dummy";
                        if (null != pod && !pod.equals("")) {
                            if (pod.lastIndexOf("(") != -1 && pod.lastIndexOf(")") != -1) {
                                pod = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
                            }
                        }
                        if (null != destination && !destination.equals("")) {
                            if (destination.lastIndexOf("(") != -1 && destination.lastIndexOf(")") != -1) {
                                destination = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
                            }
                        }
                    } else if (request.getParameter("routedByAgent") != null
                            && request.getParameter("from") != null
                            && "2".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("routedByAgent");
                    } else if (request.getParameter("houseName") != null
                            && request.getParameter("from") != null && "3".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("houseName");
                        disableDojo = request.getParameter("disableHouseShipperDojo");
                    } else if (request.getParameter("houseConsigneeName") != null
                            && request.getParameter("from") != null && "4".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("houseConsigneeName");
                        disableDojo = request.getParameter("disableMasterConsigneeDojo");
                    } else if (request.getParameter("houseNotifyPartyName") != null
                            && request.getParameter("from") != null
                            && "5".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("houseNotifyPartyName");
                        disableDojo = request.getParameter("disableMasterNotifyDojo");
                    } else if (request.getParameter("accountName") != null
                            && request.getParameter("from") != null && "6".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("accountName");
                        disableDojo = request.getParameter("disableHouseShipperDojo");
                    } else if (request.getParameter("consigneeName") != null
                            && request.getParameter("from") != null && "7".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("consigneeName");
                        disableDojo = request.getParameter("disableDojo");
                    } else if (request.getParameter("notifyPartyName") != null
                            && request.getParameter("from") != null && "8".equals(request.getParameter("from"))) {
                        disableDojo = request.getParameter("disableAutocomplete");
                        accountName = request.getParameter("notifyPartyName");
                    } else if (request.getParameter("routedByAgent") != null
                            && request.getParameter("from") != null
                            && "9".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("routedByAgent");
                    } else if (request.getParameter("forwardingAgentName") != null
                            && request.getParameter("from") != null
                            && "10".equals(request.getParameter("from"))) {
                        disableDojo = request.getParameter("disableForwarderDojo");
                        accountName = request.getParameter("forwardingAgentName");
                    } else if (request.getParameter("accountName") != null
                            && request.getParameter("from") != null
                            && "11".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("accountName");
                    } else if (request.getParameter("accountName") != null
                            && request.getParameter("from") != null
                            && "12".equalsIgnoreCase(request.getParameter("from"))) {
                        accountName = request.getParameter("accountName");
                    } else if (request.getParameter("streamShipName") != null
                            && request.getParameter("from") != null
                            && "15".equalsIgnoreCase(request.getParameter("from"))) {
                        accountName = request.getParameter("streamShipName");
                        accountType = "V";
                        sslSubType = "Steamship Line";
                        sslName = true;
                    } else if (request.getParameter("sslDescription") != null
                            && request.getParameter("from") != null
                            && "15".equalsIgnoreCase(request.getParameter("from"))) {
                        accountName = request.getParameter("sslDescription");
                        accountType = "V";
                        sslSubType = "Steamship Line";
                        sslName = true;
                    } else if (request.getParameter("expnam") != null
                            && request.getParameter("from") != null
                            && "3".equalsIgnoreCase(request.getParameter("from"))) {
                        accountName = request.getParameter("expnam");
                        disableDojo = request.getParameter("disableShipperDojo");
                    } else if (request.getParameter("connam") != null
                            && request.getParameter("from") != null
                            && "4".equalsIgnoreCase(request.getParameter("from"))) {
                        accountName = request.getParameter("connam");
                        disableDojo = request.getParameter("disableConsigneeDojo");
                    } else if (request.getParameter("frtnam") != null
                            && request.getParameter("from") != null
                            && "10".equalsIgnoreCase(request.getParameter("from"))) {
                        accountName = request.getParameter("frtnam");
                        disableDojo = request.getParameter("disableForwarderDojo");
                    }
                } else if ("USER".equals(functionName)) {
                    if (request.getParameter("ctsAccount") != null) {
                        accountName = request.getParameter("ctsAccount");
                    }
                }else if("doorDelivery".equals(functionName)){
                 if (request.getParameter("notify") != null
                            && request.getParameter("from") != null
                            && "0".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("notify");
                        disableDojo = request.getParameter("disableNotifyDojo");
                    }    
                 if (request.getParameter("deliveryTo") != null
                            && request.getParameter("from") != null
                            && "1".equals(request.getParameter("from"))) {
                        accountName = request.getParameter("deliveryTo");
                        disableDojo = request.getParameter("disableDeliveryToDojo");
                    }    
                }
                StringBuffer results = new StringBuffer();
                results.append("<ul>");
                if (accountName != null && !accountName.trim().equals("")) {
                    accountName = accountName.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
                    TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
                    List list = null;
                    String customerState = request.getParameter("customerStateId");
                    String customerCountry = request.getParameter("customerCountryId");
                    if ("TRADING_PARTNER".equals(functionName)) {
                        User user = (User) request.getSession().getAttribute("loginuser");
                        if (disabledAccount) {
                            list = tradingPartnerDAO.getForwardAccounts(accountName.replace("'", "''").replace(" ", "").replace("\"", ""), user, tradingPartnerAccount);
                        } else {
                            list = tradingPartnerDAO.getTPListForSearchCriteria(accountName.replace("'", "''").replace(" ", "").replace("\"", ""), user, customerState, customerCountry);
                        }
                    } else if ("TERMINAL".equals(functionName)) {
                        if (request.getParameter("zaccount") != null
                                && request.getParameter("from") != null && "0".equals(request.getParameter("from"))) {
                            list = tradingPartnerDAO.getZAccounts(accountName.replace("'", "''").replace(" ", "").replace("\"", ""), accountType);
                        }
                    } else if (null != agent && !agent.equals("")) {
                        list = tradingPartnerDAO.getTPAGentListForQuotBookBL(destination, agent, flag);
                    } else if (null != sslSubType && !sslSubType.equals("")) {
                        if (sslName) {
                            list = tradingPartnerDAO.getTPForSSL(accountName.replace("'", "''").replace(" ", "").replace("\"", ""), accountType);
                        } else {
                            list = tradingPartnerDAO.getTPForQuotBookBL(accountName.replace("'", "''").replace(" ", "").replace("\"", ""), accountType);
                        }
                    } else {
                        if ("false".equals(consigneeCheck)) {
                            list = tradingPartnerDAO.getTPForClientOtherThanConsignee(accountName.replace("'", "''").replace(" ", "").replace("\"", ""), "C");
                        } else {
                            list = tradingPartnerDAO.getTPForQuotBookBL(accountName.replace("'", "''").replace(" ", "").replace("\"", ""), accountType);
                        }
                    }
                    Iterator iter = list.iterator();
                    while (iter.hasNext()) {
                        String acctName, acctNumber, acctType, address, city, state, zip, disabled, sslineNo, subType, salesCode, email1, email2, fax, salesCod = "";
                        String eciAcctNo = "", ecifwno = "", bluescreenAccount = "";
                        Object[] obj = (Object[]) iter.next();
                        acctNumber = null != obj[0] ? obj[0].toString() : "";
                        acctName = null != obj[1] ? obj[1].toString() : "";
                        acctType = null != obj[2] ? obj[2].toString() : "";
                        address = null != obj[3] ? obj[3].toString() : "";
                        city = null != obj[4] ? obj[4].toString() : "";
                        state = null != obj[5] ? obj[5].toString() : "";
                        zip = null != obj[6] ? obj[6].toString() : "";
                        disabled = null != obj[7] ? obj[7].toString() : "";
                        subType = null != obj[8] ? obj[8].toString() : "";
                        sslineNo = null != obj[9] ? obj[9].toString() : "";
                        fax = (obj.length == 16 && null != obj[10]) ? obj[10].toString() : "";
                        email1 = (obj.length == 16 && null != obj[11]) ? obj[11].toString() : "";
                        email2 = (obj.length == 16 && null != obj[12]) ? obj[12].toString() : "";
                        eciAcctNo = (obj.length == 16 && null != obj[13]) ? obj[13].toString() : "";
                        ecifwno = (obj.length == 16 && null != obj[14]) ? obj[14].toString() : "";
                        salesCode = (obj.length == 16 && null != obj[15]) ? obj[15].toString() : "";
                        if (null != acctType && !acctType.trim().equals("") && (acctType.trim().equalsIgnoreCase("S") || acctType.trim().equalsIgnoreCase("V"))) {
                            bluescreenAccount = eciAcctNo;
                        }
                        if (null != acctType && !acctType.trim().equals("") && (acctType.trim().equalsIgnoreCase("C"))) {
                            bluescreenAccount = ecifwno;
                        }
                        bluescreenAccount = "".equals(bluescreenAccount) ? (null != eciAcctNo ? eciAcctNo : ecifwno) : bluescreenAccount;
                        if (subType.equals("0")) {
                            subType = "";
                        }
                        results.append("<li id='").append(acctNumber).append("'>");
                        results.append("<div class='bold'>");
                        results.append("<span class='blue-70'>").append(acctName).append("</span>");
                        results.append("<span class='red-90'> <--> ").append(acctNumber).append("</span>");
                        if (CommonUtils.isNotEmpty(acctType)) {
                            results.append("<span class='red'>, ").append(acctType).append("</span>");
                        }
                        if (CommonUtils.isEqualIgnoreCase(subType, "Steamship Line") && CommonUtils.isNotEmpty(sslineNo)) {
                            results.append("<span class='green'> - (").append(subType).append(": ").append(sslineNo).append(")</span>");
                        } else if (CommonUtils.isNotEmpty(subType)) {
                            results.append("<span class='green'> - (").append(subType).append(")</span>");
                        }
                        if (CommonUtils.isNotEmpty(salesCode)) {
                            results.append("<span class='magenta'>, SP=").append(salesCode).append("</span>");
                        }
                        if (CommonUtils.isNotEmpty(bluescreenAccount)) {
                            results.append("<span class='green'>, ").append(bluescreenAccount).append("</span>");
                        }
                        if (flag && tradingPartnerDAO.isImportCreadit(acctNumber)) {
                            results.append(",<font class='green' size='2' style='font-weight: bold;'> $ </font>");
                        }
                        if ("Y".equalsIgnoreCase(disabled)) {
                            results.append("<span class='red'>, DISABLED</span>");
                        }
                        results.append("</div>");
                        if (CommonUtils.isAtLeastOneNotEmpty(address, city, state, zip)) {
                            results.append("<div>");
                            results.append("<span class='grey font-8px'>");
                            if (CommonUtils.isNotEmpty(address)) {
                                results.append(address);
                            }
                            if (CommonUtils.isNotEmpty(city)) {
                                results.append(", ").append(city);
                            }
                            if (CommonUtils.isNotEmpty(state)) {
                                results.append(", ").append(state);
                            }
                            if (CommonUtils.isNotEmpty(zip)) {
                                results.append(", ").append(zip);
                            }
                            results.append("</div>");
                        }
                        results.append("</li>");
                    }
                }
                results.append("</ul>");
                if (null != disableDojo) {//-----This is to disable the dojo in a textBox when a checkBox is clicked.----
                    out.println("");
                } else {
                    out.println(results.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
%>
