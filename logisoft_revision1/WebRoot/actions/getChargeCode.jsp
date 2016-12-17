<%@ page language="java"
         import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.GenericCode,org.apache.commons.lang3.StringUtils"%>

<%
    String code = "";
    String codeDesc = "";
    String codeType = "";
    String codeOfApplyPayments = "";
    String Field7 = "";
    String index = "";
    String codeCodeDesc = null;
    String allChargeCodes = "";
    String functionName = null;
    String codeForGlobalRates = "";
    boolean commodityCode = false;
    boolean isBulletRates = "true".equalsIgnoreCase(request.getParameter("bulletRates"));
    if (request.getParameter("tabName") != null) {
        functionName = request.getParameter("tabName");
    }
    if (functionName == null) {
        return;
    }
    if (functionName.equals("EDIT_TERMINAL")
            || functionName.equals("ADD_TERMINAL")) {
        if (request.getParameter("chargeCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            codeType = "36";
            codeCodeDesc = "yes";
            code = request.getParameter("chargeCode");
        } else if (request.getParameter("brlChargeCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("2")) {
            codeType = "36";
            codeCodeDesc = "yes";
            code = request.getParameter("brlChargeCode");
        } else if (request.getParameter("ovr10kChgCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("3")) {
            codeType = "36";
            codeCodeDesc = "yes";
            code = request.getParameter("ovr10kChgCode");
        } else if (request.getParameter("ovr20kChgCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("4")) {
            codeType = "36";
            codeCodeDesc = "yes";
            code = request.getParameter("ovr20kChgCode");
        } else if (request.getParameter("docChargeCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("5")) {
            codeType = "36";
            codeCodeDesc = "yes";
            code = request.getParameter("docChargeCode");
        }
    } else if (functionName.equals("AIR_RATES")
            || functionName.equals("FLIGHT_SHEDULE")
            || functionName.equals("MANAGE_AIR_RATES")) {
        if (request.getParameter("terminalNumber") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            codeType = "1";
            code = request.getParameter("terminalNumber");
        } else if (request.getParameter("destSheduleNumber") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            codeType = "1";
            code = request.getParameter("destSheduleNumber");
        } else if (request.getParameter("destAirportname") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("2")) {
            codeDesc = request.getParameter("destAirportname");
            codeType = "1";
        } else if (request.getParameter("comCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("3")) {
            code = request.getParameter("comCode");
            codeType = "4";

        }
    } else if (functionName.equals("FTF")
            || functionName.equals("RETAIL_CSSC")
            || functionName.equals("UNIVERSAL_COMMODITY")
            || functionName.equals("RETAIL_AGSC")
            || functionName.equals("FCL_AIR_BUY")
            || functionName.equals("AGSC_FTF")
            || functionName.equals("AGSS")
            || functionName.equals("CO_AGSS")
            || functionName.equals("CSSS")) {
        if (request.getParameter("charge") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            code = request.getParameter("charge");
            codeType = "36";
        } else if (request.getParameter("destSheduleNumber") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            codeType = "1";
            code = request.getParameter("destSheduleNumber");
        }
    } else if (functionName.equals("AR_APPLY_PAYMENTS")) {
        if (request.getParameter("index") != null) {
            index = request.getParameter("index");
            codeOfApplyPayments = request.getParameter("chargeCode"
                    + index);
            Field7 = "A";
            codeType = "36";
        } else if (request.getParameter("chargeCodeIndex") != null) {
            index = request.getParameter("chargeCodeIndex");
            codeOfApplyPayments = request
                    .getParameter("chargecodeForOnCharge" + index);
            Field7 = "A";
            codeType = "36";
        }
    } else if (functionName.equals("DOCUMENT_CHARGES")
            || functionName.equals("DOCUMENT_CHARGES_FTF")
            || functionName.equals("LCL_CODOCUMENT_CHARGES")
            || functionName.equals("RETAIL_DOCUMENT_CHARGES")) {
        if (request.getParameter("charge") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {

            code = request.getParameter("charge");
            codeType = "35";
        } else if (request.getParameter("desc") != null
                & request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            codeDesc = request.getParameter("desc");
            codeType = "35";
            codeCodeDesc = "yes";
        } else if (request.getParameter("destAirportname") != null
                & request.getParameter("from") != null
                && request.getParameter("from").equals("2")) {
            codeDesc = request.getParameter("destAirportname");
            codeType = "1";
        } else if (request.getParameter("destSheduleNumber") != null) {
            codeType = "1";
            code = request.getParameter("destSheduleNumber");
        }
    } else if (functionName.equals("FCL_BL_CHARGES")) {

        if (request.getParameter("index") != null
                && request.getParameter("from").equals("0")) {
            index = request.getParameter("index");
            if (request.getParameter("chargeCode" + index) != null) {
                codeType = "36";
                codeCodeDesc = "yes";
                code = request.getParameter("chargeCode" + index);
            }
        } else if (request.getParameter("index") != null
                && request.getParameter("from").equals("1")) {
            index = request.getParameter("index");
            if (request.getParameter("chargeCode" + index) != null) {
                codeType = "36";
                codeCodeDesc = "yes";
                code = request.getParameter("chargeCode" + index);
            }
        } else if (request.getParameter("index") != null
                && request.getParameter("from").equals("2")) {
            index = request.getParameter("index");

            if (request.getParameter("chargeCode" + index) != null) {
                codeType = "36";
                codeCodeDesc = "yes";
                code = request.getParameter("chargeCode" + index);
            }
        } else if (request.getParameter("index") != null
                && request.getParameter("from").equals("3")) {
            index = request.getParameter("index");
            if (request.getParameter("costCode" + index) != null) {
                codeType = "36";
                codeCodeDesc = "yes";
                code = request.getParameter("costCode" + index);
            }
        } else if (request.getParameter("index") != null
                && !request.getParameter("from").equals("4")) {
            index = request.getParameter("index");
            codeCodeDesc = "yes";
            codeDesc = request.getParameter("costCodeDesc" + index);
            codeType = "36";
        } else if (request.getParameter("index") != null
                && !request.getParameter("from").equals("5")) {
            index = request.getParameter("index");
            codeCodeDesc = "yes";
            codeDesc = request.getParameter("costCodeDesc" + index);
            codeType = "36";
        } else if (request.getParameter("from") != null
                && request.getParameter("from").equals("6")) {
            if (request.getParameter("chargeCode") != null) {
                codeType = "36";
                codeCodeDesc = "yes";
                code = request.getParameter("chargeCode");
            }
        } else if (request.getParameter("from") != null
                && request.getParameter("from").equals("6")) {
            if (request.getParameter("chargeCode") != null) {
                codeType = "36";
                codeCodeDesc = "yes";
                code = request.getParameter("chargeCode");
            }
        }
    } else if (functionName.equals("FCL_SELL_RATES")) {
        if (request.getParameter("destRegion") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {
            code = request.getParameter("destRegion");
            codeType = "19";
        } else if (request.getParameter("comCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("1")) {
            code = request.getParameter("comCode");
            codeType = "4";
        }
    } else if (functionName.equals("WAREHOUSE")) {
        codeType = "4";
        if (request.getParameter("ipiCommodity") != null) {

            code = request.getParameter("ipiCommodity");
        }
    } else if (functionName.equals("GENERIC_CODE_MAINTENANCE")) {
        if (request.getParameter("codeValue") != null) {
            codeType = "";
            codeCodeDesc = "yes";
            code = request.getParameter("codeValue");
        }
    } else if (functionName.equals("BOOKING")) {
        if (request.getParameter("commcode") != null) {
            codeType = "4";
            code = request.getParameter("commcode");
        }
    } else if (functionName.equals("QUOTE")) {
        if (request.getParameter("commcode") != null) {
            commodityCode = true;
            codeType = "4";
            code = request.getParameter("commcode");
        } else if (request.getParameter("ipiCommodity") != null) {
            commodityCode = true;
            codeType = "4";
            code = request.getParameter("ipiCommodity");
        }
    } else if (functionName.equals("FCLBL")) {
        if (request.getParameter("commodityCode") != null) {
            codeType = "4";
            code = request.getParameter("commodityCode");
        }
    } else if (functionName.equals("GLOBAL_RATES")) {
        if (request.getParameter("commodityCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equalsIgnoreCase("0")) {
            codeType = "4";
            codeForGlobalRates = request.getParameter("commodityCode");
        }
    } else if (functionName.equals("ADD_FCL")
            || functionName.equals("ADD_FTF_POPUP")
            || functionName.equals("ADD_FUTURE_FCL_POPUP")
            || functionName.equals("ADD_LCL_COLOAD_POPUP")
            || functionName.equals("ADD_LCL_COLOAD_POPUP")
            || functionName.equals("MANAGE_RETAIL_RATES")
            || functionName.equals("PRAC_MANAGE_RETAIL_RATES")
            || functionName.equals("SEARCH_FCL_FUTURE")
            || functionName.equals("SEARCH_FCL")
            || functionName.equals("SEARCH_FTF")
            || functionName.equals("SEARCH_LCL_COLOAD")
            || functionName.equals("SEARCH_UNIVERSAL")
            || functionName.equals("UNIVERSAL_ADD_POPUP")
            || functionName.equals("RETAIL_ADD_AIR_RATES_POPUP")) {
        if (request.getParameter("comCode") != null) {

            codeType = "4";
            code = request.getParameter("comCode");
        }
    } else if (functionName.equals("GENERAL_INFORMATION")) {
        String fieldName = request.getParameter("fieldName");
        code = request.getParameter(fieldName);
        codeType = "4";
        if (fieldName.equalsIgnoreCase("salesCode") || fieldName.equalsIgnoreCase("consSalesCode")) {
            codeType = "23";
        }
    } else if (functionName.equals("MASTER_GENERAL_INFORMATION")) {
        if (request.getParameter("commodity") != null) {
            code = request.getParameter("commodity");
            codeType = "4";

        }
    } else if (functionName.equals("AR_INVOICE")) {
        if (request.getParameter("chargeCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {//-------for ARInvoiceGenerator.jsp-------
            allChargeCodes = request.getParameter("chargeCode");
        }
    } else if (functionName.equals("ACCRUALS")) {
        if (request.getParameter("costCode") != null) {
            codeType = "36";
            codeCodeDesc = "yes";
            code = request.getParameter("costCode");
        }
    } else if (functionName.equals("AP_INVOICE")) {
        if (request.getParameter("chargeCode") != null) {
            codeType = "36";
            codeCodeDesc = "yes";
            code = request.getParameter("chargeCode");
        }
    } else if (functionName.equals("GLMAPPING")) {
        if (request.getParameter("chargeCode") != null) {
            codeType = "36";
            codeCodeDesc = "yes";
            code = request.getParameter("chargeCode");
        }
    } else if (functionName.equals("ADJUSTMENT")) {
        if (request.getParameter("chargeCode1") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {//-------for ARInvoiceGenerator.jsp-------
            codeOfApplyPayments = request.getParameter("chargeCode1");
            Field7 = "A";
            codeType = "36";
        }
    } else if (functionName.equals("EXPORT_VOYAGE")) {
        if (request.getParameter("reasonCode") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {//-------for ARInvoiceGenerator.jsp-------
            code = request.getParameter("reasonCode");
            codeType = "15";
        }
    } else if (functionName.equals("FCL_BILL_LADDING")) {
        if (request.getParameter("blClause") != null
                && request.getParameter("from") != null
                && request.getParameter("from").equals("0")) {//-------for ARInvoiceGenerator.jsp-------
            code = request.getParameter("blClause");
            codeType = "7";
        }
    } else if (functionName.equals("FCLBLCORRECTIONS")) {
        if (request.getParameter("index") != null && request.getParameter("from").equals("1")) {
            index = request.getParameter("index");
            if (request.getParameter("chargeCode" + index) != null) {
                codeType = "36";
                codeCodeDesc = "yes";
                code = request.getParameter("chargeCode" + index);
            }
        } else if (request.getParameter("index") != null && request.getParameter("from").equals("2")) {
            index = request.getParameter("index");
            if (request.getParameter("chargeCodeDescription" + index) != null) {
                codeType = "36";
                codeCodeDesc = "yes";
                codeDesc = request.getParameter("chargeCodeDescription" + index);
            }
        }
    }
        //if (request.getParameter("portofDischarge") != null) {
    //		codeType = "1";
    //	code = request.getParameter("portofDischarge");
    //}
    // Ar Apply Payments Screen by Pradeep
    JSONArray accountNoArray = new JSONArray();

    if (code != null && !code.trim().equals("")) {
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        List codeList = new ArrayList();
        if (codeType != null && !codeType.equals("")) {
            if (commodityCode) {
                codeList = genericDAO.findForCommodityCode(code, codeType, isBulletRates);
            } else {
                codeList = genericDAO.findForChargeCodesForAirRates(code, codeDesc, codeType);
            }
        } else {
            codeList = genericDAO.findForAirRatesDup(code, codeDesc);
        }

        Iterator iter = codeList.iterator();

        while (iter.hasNext()) {
            GenericCode accountDetails = (GenericCode) iter.next();
            if (codeCodeDesc != null && codeCodeDesc.equals("yes")) {
                if (functionName.equals("EDIT_TERMINAL")
                        || functionName.equals("ADD_TERMINAL")) {
                    accountNoArray.put("<font class='red-90'>" + accountDetails.getCode().toString()
                            + "</font><font class='blue-70'> <--> " + accountDetails.getCodedesc() + "</font>");
                } else {
                    accountNoArray.put("<font class='red-90'>" + accountDetails.getCode().toString()
                            + "</font><font class='blue-70'> :- " + accountDetails.getCodedesc() + "</font>");

                }
            } else {
                if (functionName.equals("GENERAL_INFORMATION")) {
                    if (!accountDetails.getCode().toString().trim().equals("000000")) {
                        accountNoArray.put("<font class='red-90'>" + accountDetails.getCode().toString() + "</font><font class=blue-70> <--> " + accountDetails.getCodedesc().toString() + "</font>");
                    }
                } else {
                    accountNoArray.put("<font class='red-90'>" + accountDetails.getCode().toString() + "</font><font class=blue-70> <--> " + accountDetails.getCodedesc().toString() + "</font>");
                }
            }
        }
    } else if (codeForGlobalRates != null && !codeForGlobalRates.trim().equals("")) {
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        List codeList = new ArrayList();
        if (codeType != null && !codeType.equals("")) {
            codeList = genericDAO.findForChargeCodesForAirRates(codeForGlobalRates,
                    codeDesc, codeType);
        }
        Iterator iter = codeList.iterator();
        while (iter.hasNext()) {
            GenericCode accountDetails = (GenericCode) iter.next();
            accountNoArray.put("<font class='red-90'>" + accountDetails.getCode().toString()
                    + "</font><font class='blue-70'> / "
                    + accountDetails.getCodedesc().toString() + "</font>");
        }
    } else if (codeDesc != null && !codeDesc.trim().equals("")) {
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        List codeList = genericDAO.findForChargeCodesForAirRates(code,
                codeDesc, codeType);
        Iterator iter = codeList.iterator();
        while (iter.hasNext()) {
            GenericCode accountDetails = (GenericCode) iter.next();
            accountNoArray.put("<font class='blue-70'>" + accountDetails.getCodedesc().toString() + "</font>");
        }
    }

    if (allChargeCodes != null && !allChargeCodes.trim().equals("")) {
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        String code1 = "";
        String codeDesc1 = "";
        String codeType1 = "2";
        List codeList = genericDAO.findForChargeCodesForAirRates(
                allChargeCodes, codeDesc1, codeType1);
        Iterator iter = codeList.iterator();
        while (iter.hasNext()) {

            GenericCode accountDetails = (GenericCode) iter.next();
            accountNoArray.put("<font class='red-90'>" + accountDetails.getCode() + "</font><font class='blue-70'> ; "
                    + accountDetails.getCodedesc() + "</font>");

        }
    }
    if (codeOfApplyPayments != null
            && !codeOfApplyPayments.trim().equals("")) {
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        List codeList = new ArrayList();
        if (codeType != null && !codeType.equals("")) {
            codeList = genericDAO.findChargeCodesForApplyPayments(
                    codeOfApplyPayments, codeDesc, codeType, Field7);
        } else {
            codeList = genericDAO.findForAirRatesDup(
                    codeOfApplyPayments, codeDesc);
        }

        Iterator iter = codeList.iterator();
        while (iter.hasNext()) {
            GenericCode accountDetails = (GenericCode) iter.next();
            accountNoArray.put("<font class='red-90'>" + accountDetails.getCode().toString()
                    + "</font><font class='blue-70'> :- " + accountDetails.getCodedesc().toString() + "</font>");

        }
    }

    if ("false".equals(request.getParameter("isDojo"))) {
        StringBuilder buffer = new StringBuilder("<UL>");
        for (int i = 0; i < accountNoArray.length(); i++) {
            String[] values = accountNoArray.get(i).toString().split("<-->");
            if (values.length > 1) {
                buffer.append("<li id='").append(values[1].replace("</font>", "")).append("'>");
            } else {
                buffer.append("<li id='").append("").append("'>");
            }
            buffer.append(accountNoArray.get(i));
            buffer.append("</li>");
        }
        buffer.append("</UL>");
        out.println(buffer.toString());

    } else {
        out.println(accountNoArray.toString());
    }
%>
