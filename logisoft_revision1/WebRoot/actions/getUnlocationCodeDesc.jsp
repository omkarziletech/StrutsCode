<%@page import="com.logiware.bean.TradeRouteBean"%>
<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.UserDAO"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO"%>
<%@ page
    import="org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.hibernate.dao.PortsDAO,com.gp.cong.logisoft.domain.UnLocation,com.gp.cvst.logisoft.beans.ChartOfAccountBean,com.gp.cong.logisoft.hibernate.dao.FclBuyDAO"%>
    <%@ page import="java.util.*"%>
    <jsp:directive.page import="com.gp.cong.logisoft.bc.ratemanagement.PortsBC"/>

    <%
        String orgRegion = "";
        String orgRegionDesc = "";
        String cityForZipCode = "";
        String terminalName = "";
        String functionName = "";
        String orgTrm = "";
        String originService = "";
        String portCity = "";
        String destinationService = "";
        String typeOfMove = "";
        String destination = "";
        String country = "", flag = "", nonRated = "";
        String fclRatesOriginService = "";
        String fclRatesDestinationService = "";
        String searchBy = "";
        String userAgent = "";
        boolean wareHouse = false;
        String importFlag = "false";
        String countryName = "";

        if (request.getParameter("typeOfmove") != null) {
            typeOfMove = request.getParameter("typeOfmove");
        }
        if (request.getParameter("tabName") != null) {
            functionName = request.getParameter("tabName");
        }
        if (functionName == null) {
            return;
        }
        if (functionName.equals("PORTS")) {
            if (request.getParameter("defaultPortOfDischarge") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("defaultPortOfDischarge");
                destinationService = "Y";
            }
            else if (request.getParameter("transhipment") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("transhipment");
                destinationService = "Y";
            }
        }
        else if (functionName.equals("PORTSAGENT")) {
            if (request.getParameter("podAgent") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                portCity = request.getParameter("podAgent");
                destinationService = "pod";
            }
        } else if (functionName.equals("BOOKING")) {
            if (request.getParameter("exportToDelivery") != null && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
                orgRegionDesc = request.getParameter("exportToDelivery");
            } else if (request.getParameter("portOfOrigin") != null && request.getParameter("from") != null && request.getParameter("from").equals("4")) {
                orgRegionDesc = request.getParameter("portOfOrigin");
            } else if (request.getParameter("pol") != null && request.getParameter("from") != null && request.getParameter("from").equals("2")) {
                orgRegionDesc = request.getParameter("pol");
            } else if (request.getParameter("destination") != null && request.getParameter("from") != null && request.getParameter("from").equals("3")) {
                orgRegionDesc = request.getParameter("destination");
            } else if (request.getParameter("plod") != null && request.getParameter("from") != null && request.getParameter("from").equals("4")) {
                orgRegionDesc = request.getParameter("plod");
            } else if (request.getParameter("pod") != null && request.getParameter("from") != null && request.getParameter("from").equals("5")) {
                orgRegionDesc = request.getParameter("pod");
            } else if (request.getParameter("portOfDischarge") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("portOfDischarge");
            } else if (request.getParameter("originTerminal") != null && request.getParameter("from") != null && request.getParameter("from").equals("2")) {
                importFlag = request.getParameter("importFlag");
                orgRegionDesc = request.getParameter("originTerminal");
            } else if (request.getParameter("doorOrigin") != null && request.getParameter("from") != null && request.getParameter("from").equals("5")) {
                orgRegionDesc = request.getParameter("doorOrigin");
            } else if (request.getParameter("doorDestination") != null && request.getParameter("from") != null && request.getParameter("from").equals("6")) {
                orgRegionDesc = request.getParameter("doorDestination");
            } else if (request.getParameter("doorOfDestination") != null && request.getParameter("from") != null && request.getParameter("from").equals("6")) {
                orgRegionDesc = request.getParameter("doorOfDestination");
            }
        } else if (functionName.equals("QUOTE")) {
            if (request.getParameter("poe") != null && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
                orgRegionDesc = request.getParameter("poe");
            } else if (request.getParameter("pod") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("pod");
                destinationService = "Y";
            } else if (request.getParameter("destination") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("destination");
                destinationService = "Y";
            } else if (request.getParameter("placeofReceipt") != null && request.getParameter("from") != null && request.getParameter("from").equals("2")) {
                orgRegionDesc = request.getParameter("placeofReceipt");
                portCity = "Y";
            } else if (request.getParameter("pol") != null && request.getParameter("from") != null && request.getParameter("from").equals("3")) {
                orgRegionDesc = request.getParameter("pol");
            } else if (request.getParameter("finalDestination") != null && request.getParameter("from") != null && request.getParameter("from").equals("4")) {
                orgRegionDesc = request.getParameter("finalDestination");
                portCity = "Y";
            } else if (request.getParameter("plod") != null && request.getParameter("from") != null && request.getParameter("from").equals("5")) {
                orgRegionDesc = request.getParameter("plod");
            } else if (request.getParameter("isTerminal") != null && request.getParameter("from") != null && request.getParameter("from").equals("6")) {

                //-----THIS IS FOR RATED QUOTE CONDITION----
                if (request.getParameter("destination") != null && !request.getParameter("destination").equals("")) {
                    fclRatesOriginService = request.getParameter("isTerminal");
                    destination = request.getParameter("destination");
                    if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("country")) {
                        importFlag = request.getParameter("importFlag");
                        searchBy = "country";
                    } else {
                        importFlag = request.getParameter("importFlag");
                        searchBy = "city";
                    }
                } else {
                    if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("country")) {
                        searchBy = "country";
                        importFlag = request.getParameter("importFlag");
                        fclRatesOriginService = request.getParameter("isTerminal");
                        //originService = "Y";
                    } else if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("city")) {
                        importFlag = request.getParameter("importFlag");
                        fclRatesOriginService = request.getParameter("isTerminal");
                        searchBy = "city";
                    }
                }
                //------THIS IS FOR NONRATED QUfOTE CONDITION----
                if (request.getParameter("nonRated") != null && request.getParameter("nonRated").equals("yes")) {
                    nonRated = "true";
                    if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("country")) {
                        country = "getCitiesBasedOnCountries";
                        flag = "Origin";
                        orgRegionDesc = request.getParameter("isTerminal");
                        //originService = "Y";
                    } else if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("city")) {
                        orgRegionDesc = request.getParameter("isTerminal");
                        // originService = "Y";
                    }
                }
                if (typeOfMove.equals("")) {
                    typeOfMove = "0";
                }
                if (typeOfMove.equals("0") || typeOfMove.equals("1") || typeOfMove.equals("2")) {
                } else {
                    //originService = "Y";
                }

            } else if (request.getParameter("rampCity") != null && request.getParameter("from") != null && request.getParameter("from").equals("8")) {
                orgRegionDesc = request.getParameter("rampCity");
                // originService = "Y";
            } else if (request.getParameter("portofDischarge") != null && request.getParameter("from") != null && request.getParameter("from").equals("7")) {

                //------THIS IS FOR RATED QUOTE---------
                if (request.getParameter("origin") != null && !request.getParameter("origin").equals("")) {
                    fclRatesDestinationService = request.getParameter("portofDischarge");
                    destination = request.getParameter("origin");
                    if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("country")) {
                        searchBy = "country";
                    } else {
                        searchBy = "city";
                    }
                } else {//----THIS IS FOR RATED BUT WHEN ORIGIN IS EMPTY-------
                    if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("country")) {
                        fclRatesDestinationService = request.getParameter("portofDischarge");
                        searchBy = "country";
                    } else if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("city")) {
                        fclRatesDestinationService = request.getParameter("portofDischarge");
                        searchBy = "city";
                    }
                }
                //------THIS IS FOR NONRATED QUOTE CONDITION----
                if (request.getParameter("nonRated") != null && request.getParameter("nonRated").equals("yes")) {
                    nonRated = "true";//--checking this flag value below----
                    if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("country")) {
                        country = "getCitiesBasedOnCountries";
                        flag = "Destination";
                        orgRegionDesc = request.getParameter("portofDischarge");
                        destinationService = "Y";
                    } else if (request.getParameter("radio") != null && request.getParameter("radio").equalsIgnoreCase("city")) {
                        orgRegionDesc = request.getParameter("portofDischarge");
                        destinationService = "Y";
                    }
                }
            } else if (request.getParameter("plod") != null && request.getParameter("from") != null && request.getParameter("from").equals("9")) {
                orgRegionDesc = request.getParameter("plod");
                portCity = "Y";
            } else if (request.getParameter("pod") != null && request.getParameter("from") != null && request.getParameter("from").equals("9")) {
                orgRegionDesc = request.getParameter("pod");
                portCity = "Y";
            } else if (request.getParameter("doorDestination") != null && request.getParameter("from") != null && request.getParameter("from").equals("10")) {
                if (null != request.getParameter("checkDoor") && request.getParameter("checkDoor").equalsIgnoreCase("false")) {
                    orgRegionDesc = request.getParameter("doorDestination");
                }
            }
        } else if (functionName.equals("WAREHOUSE")) {
            if (request.getParameter("city") != null) {
                orgRegionDesc = request.getParameter("city");
                wareHouse = true;
            } else if (request.getParameter("acCity") != null) {
                orgRegionDesc = request.getParameter("acCity");
                wareHouse = true;
            }
        } else if (functionName.equals("FCL_BILL_LADDING")) {
            if (request.getParameter("portofdischarge") != null && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
                orgRegionDesc = request.getParameter("portofdischarge");
            } else if (request.getParameter("portofladding") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("portofladding");
            } else if (request.getParameter("finalDestination") != null && request.getParameter("from") != null && request.getParameter("from").equals("2")) {
                orgRegionDesc = request.getParameter("finalDestination");

            } else if (request.getParameter("alternatePort") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("alternatePort");
            }
        } else if (functionName.equals("ADD_FCL") || functionName.equals("ADD_FTF_POPUP") || functionName.equals("ADD_LCL_COLOAD_POPUP") || functionName.equals("FCL_SELL_RATES") || functionName.equals("MANAGE_RETAIL_RATES") || functionName.equals("RETAIL_ADD_AIR_RATES_POPUP") || functionName.equals("SEARCH_FCL_FUTURE") || functionName.equals("SEARCH_FCL") || functionName.equals("SEARCH_FTF") || functionName.equals("SEARCH_LCL_COLOAD") || functionName.equals("SEARCH_UNIVERSAL")) {
            if (request.getParameter("terminalName") != null && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
                orgTrm = request.getParameter("terminalName");
            } else if (request.getParameter("destAirportname") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgTrm = request.getParameter("destAirportname");
            }
        } else if (functionName.equals("FCL_BL")) {
            if (request.getParameter("portofladding") != null && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
                orgRegionDesc = request.getParameter("portofladding");
            } else if (request.getParameter("finalDestination") != null
                    && request.getParameter("from") != null
                    && request.getParameter("from").equals("2")) {
                orgRegionDesc = request.getParameter("finalDestination");
                destinationService = "Y";
            } else if (request.getParameter("portofdischarge") != null
                    && request.getParameter("from") != null
                    && request.getParameter("from").equals("3")) {
                orgRegionDesc = request.getParameter("portofdischarge");
            } else if (request.getParameter("terminal") != null
                    && request.getParameter("from") != null
                    && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("terminal");
                //originService = "Y";
            } else if (request.getParameter("doorOfOrigin") != null
                    && request.getParameter("from") != null
                    && request.getParameter("from").equals("5")) {
                orgRegionDesc = request.getParameter("doorOfOrigin");
                //originService = "Y";
            } else if (request.getParameter("doorOfDestination") != null
                    && request.getParameter("from") != null
                    && request.getParameter("from").equals("6")) {
                orgRegionDesc = request.getParameter("doorOfDestination");
                //originService = "Y";
            }
        } else if (functionName.equals("GLOBAL_RATES")) {
            if (request.getParameter("origin") != null && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
                orgRegionDesc = request.getParameter("origin");
            } else if (request.getParameter("origin") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("destination");
            }
        } else if (functionName.equals("BL_CORRECTIONS")) {
            if (request.getParameter("origin") != null && request.getParameter("from") != null && request.getParameter("from").equals("0")) {
                orgRegionDesc = request.getParameter("origin");
            } else if (request.getParameter("pol") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                orgRegionDesc = request.getParameter("pol");
            } else if (request.getParameter("pod") != null && request.getParameter("from") != null && request.getParameter("from").equals("2")) {
                orgRegionDesc = request.getParameter("pod");
            } else if (request.getParameter("destination") != null && request.getParameter("from") != null && request.getParameter("from").equals("3")) {
                orgRegionDesc = request.getParameter("destination");
            } else if (request.getParameter("rampCity") != null && request.getParameter("from") != null && request.getParameter("from").equals("4")) {
                orgRegionDesc = request.getParameter("rampCity");
            }
        } else if (functionName.equals("ZIPCODE")) {
            if (request.getParameter("city") != null && request.getParameter("from") != null && request.getParameter("from").equals("1")) {
                cityForZipCode = request.getParameter("city");
            }
        } else if (functionName.equals("USERAGENT")) {
            userAgent = request.getParameter("pod");
        } else if (functionName.equals("TRADING_PARTNER")) {
            countryName = request.getParameter("customerCountry");
        }
        JSONArray accountNoArray = new JSONArray();
        List accountList = null;
        if (orgRegion != null && !orgRegion.equals("")) {
            PortsDAO portsDAO = new PortsDAO();
            List portslist = portsDAO.findForUnlocCodeAndPortName(orgRegion.replace("'", "''").replace(" ", "").replace("\"", ""), orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""));
            Iterator iter = portslist.iterator();
            while (iter.hasNext()) {
                Ports accountDetails = (Ports) iter.next();
                accountNoArray.put(accountDetails.getUnLocationCode() + "-" + accountDetails.getPortname().trim());
            }
        } else if (orgRegionDesc != null && !orgRegionDesc.trim().equals("")) {
            PortsDAO portsDAO = new PortsDAO();
            PortsBC portsBC = new PortsBC();
            List<TradeRouteBean> pierList = new ArrayList();
            String fromFlag = "";
            if (country != "" && country.equalsIgnoreCase("getCitiesBasedOnCountries")) {
                pierList = portsDAO.getAllTheCitiesBasedOnCountry(orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""));
            } else if (portCity == "Y") {
                pierList = portsDAO.searchForUnlocCodeAndPortNameForDestinationServiceBYPortCity(orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""));
            } else {
                pierList = portsDAO.searchForUnlocCodeAndPortNameforPortsTemp(orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""), importFlag);
            }

            if (originService == "Y") {
                if (nonRated.equals("true")) {
                    fromFlag = "Origin";
                }
            } else if (destinationService == "Y") {
                if (nonRated.equals("true")) {
                    fromFlag = "Destination";
                }
            }
            Iterator iter = pierList.iterator();
            while (iter.hasNext()) {
                TradeRouteBean accountDetails = (TradeRouteBean) iter.next();
                if (destinationService == "Y") {
                    if (accountDetails.getStateCode() != null) {
                        accountNoArray.put(accountDetails.getPortName()
                                + "/" + accountDetails.getStateCode()
                                + "/" + accountDetails.getCountryName()
                                + "(" + accountDetails.getUnLocationCode() + ")");
                    } else if (accountDetails.getUnLocationCode() != null) {
                        accountNoArray.put(accountDetails.getPortName()
                                + "/" + accountDetails.getCountryName()
                                + "(" + accountDetails.getUnLocationCode() + ")");
                    } else {
                        accountNoArray.put(accountDetails.getPortName()
                                + "/" + accountDetails.getCountryName());
                    }
                } else if (wareHouse) {
                    if (accountDetails.getStateCode() != null) {
                        accountNoArray.put(accountDetails.getPortName() + "/" + accountDetails.getCountryName() + "/" + accountDetails.getStateCode());
                    } else if (accountDetails.getUnLocationCode() != null) {
                        accountNoArray.put(accountDetails.getPortName() + "/" + accountDetails.getCountryName());
                    }
                } else {
                    if (accountDetails.getStateCode() != null && accountDetails.getCountryName() != null) {
                        accountNoArray.put("<font class='blue-70'>" + accountDetails.getPortName() + "</font><font class='green'>/" + accountDetails.getStateCode() + "</font><font class='red'>/" + accountDetails.getCountryName()
                                + "</font><font class='red-90'>(" + accountDetails.getUnLocationCode() + ")</font>");
                    } else if (accountDetails.getStateCode() != null) {
                        accountNoArray.put("<font class='blue-70'>" + accountDetails.getPortName() + "</font><font class='green'>/" + accountDetails.getStateCode()
                                + "</font><font class='red-90'>(" + accountDetails.getUnLocationCode() + ")</font>");
                    } else if (accountDetails.getCountryName() != null) {
                        accountNoArray.put("<font class='blue-70'>" + accountDetails.getPortName() + "</font><font class='green'>/" + accountDetails.getCountryName()
                                + "</font><font class='red-90'>(" + accountDetails.getUnLocationCode() + ")</font>");
                    } else {
                        accountNoArray.put("<font class='blue-70'>" + accountDetails.getPortName() + "</font><font class='red-90'>/(" + accountDetails.getUnLocationCode() + ")</font>");
                    }
                }
            }
        } else if (terminalName != null && !terminalName.trim().equals("")) {
            PortsDAO portsDAO = new PortsDAO();
            List pierList = portsDAO.findPierCode(orgRegion, terminalName);
            Iterator iter = pierList.iterator();
            while (iter.hasNext()) {
                Ports accountDetails = (Ports) iter.next();
                accountNoArray.put(accountDetails.getUnLocationCode() + "-" + accountDetails.getPortname().toString());
            }
        } else if (orgTrm != null && !orgTrm.trim().equals("")) {
            PortsDAO portsDAO = new PortsDAO();
            List pierList = portsDAO.findForUnlocCodeAndPortName(orgRegion.replace("'", "''").replace(" ", "").replace("\"", ""), orgTrm.replace("'", "''").replace(" ", "").replace("\"", ""));
            Iterator iter = pierList.iterator();
            while (iter.hasNext()) {
                Ports accountDetails = (Ports) iter.next();
                accountNoArray.put(accountDetails.getPortname().toString() + "-" + accountDetails.getUnLocationCode());
            }
        } else if (fclRatesOriginService != null && !fclRatesOriginService.trim().equals("")) {
            FclBuyDAO fclBuyDAO = new FclBuyDAO();
            String code = "";
            if (destination.indexOf("/") != -1) {
                if (destination.lastIndexOf("(") != -1) {
                    code = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
                }
            }
            List ratesList = fclBuyDAO.getOriginsForDestination(code.replace("'", "''").replace(" ", "").replace("\"", ""), fclRatesOriginService.replace("'", "''").replace(" ", "").replace("\"", ""), searchBy, importFlag);
            Iterator iter = ratesList.iterator();
            while (iter.hasNext()) {
                String object = (String) iter.next();
                accountNoArray.put(object);
            }
        } else if (fclRatesDestinationService != null && !fclRatesDestinationService.trim().equals("")) {
            FclBuyDAO fclBuyDAO = new FclBuyDAO();
            String code = "";
            if (destination.indexOf("/") != -1) {
                if (destination.lastIndexOf("(") != -1) {
                    code = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
                }
            }
            List ratesList = fclBuyDAO.getDestinationsForOrigin(code.replace("'", "''").replace(" ", "").replace("\"", ""), fclRatesDestinationService.replace("'", "''").replace(" ", "").replace("\"", ""), searchBy);
            Iterator iter = ratesList.iterator();
            while (iter.hasNext()) {
                String object = (String) iter.next();
                accountNoArray.put(object);
            }
        } else if (null != cityForZipCode && !cityForZipCode.trim().equals("")) {
            PortsDAO portsDAO = new PortsDAO();
            List pierList = portsDAO.findports1(cityForZipCode.replace("'", "''").replace(" ", "").replace("\"", ""));
            Iterator iter = pierList.iterator();
            while (iter.hasNext()) {
                PortsTemp accountDetails = (PortsTemp) iter.next();
                if (accountDetails.getStateCode() != null) {
                    accountNoArray.put(accountDetails.getPortname().toString() + "/" + accountDetails.getStateCode());
                } else {
                    accountNoArray.put(accountDetails.getPortname().toString());
                }
            }
        } else if (null != userAgent && !userAgent.trim().equals("")) {
            if (session.getAttribute("loginuser") != null) {
                User user = (User) session.getAttribute("loginuser");
                List userAgentList = new UserDAO().findByUserAgentProperty("userId.userId", user.getUserId());
                for (Object object : userAgentList) {
                    UserAgentInformation agent = (UserAgentInformation) object;
                    if (null != agent.getPortId()) {
                        if (CommonUtils.isNotEmpty(agent.getPortId().getStateCode())) {
                            accountNoArray.put(agent.getPortId().getPortname() + "/" + agent.getPortId().getStateCode() + "/" + agent.getPortId().getCountryName() + "(" + agent.getPortId().getUnLocationCode() + ")");
                        } else {
                            accountNoArray.put(agent.getPortId().getPortname() + "/" + agent.getPortId().getCountryName() + "(" + agent.getPortId().getUnLocationCode() + ")");
                        }
                    }
                }
            }
        } else if (null != countryName && !countryName.trim().equals("")) {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            List pierList = genericCodeDAO.getCountryList(countryName.replace("'", "''").replace(" ", "").replace("\"", ""));
            Iterator iter = pierList.iterator();
            while (iter.hasNext()) {
                GenericCode genericCode = (GenericCode) iter.next();
                accountNoArray.put(genericCode.getCodedesc() + "/" + genericCode.getCode());
            }
        } else if (destinationService.equals("pod")) {
            List podList = new PortsDAO().findPodForAgent(portCity);
            for (Object pod : podList) {
                accountNoArray.put(pod);
            }
        }

        //accountNoArray.g
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
