package com.gp.cong.logisoft.servlet.AutoCompleter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gp.cong.common.CommonConstants;
import java.util.Date;

import org.apache.log4j.Logger;

public class AutoCompleterServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(AutoCompleterServlet.class);
    private static final long serialVersionUID = 5702532979476010374L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	String action = request.getParameter("action");
	try {
	    if (null != action) {
		if (action.trim().equals(CommonConstants.AUTOCOMPLETER_VENDOR_NAME)) {
		    new AutoCompleterForVendor().setVendorDetails(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_CUSTOMER_NAME)) {
		    new AutoCompleterForCustomer().setCustomerDetails(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_CHARGE_CODE)) {
		    new AutoCompleterForChargeCode().setChargeCodes(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_GL_ACCOUNT)) {
		    new AutoCompleterForGLAccount().setGLAccounts(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_USER_NAME)) {
		    new AutoCompleterForUser().setUser(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_COLLECTOR)) {
		    new AutoCompleterForUser().setCollectors(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_BANK)) {
		    new AutoCompleterForBank().setBankDetails(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_MODULE)) {
		    new AutoCompleterForModules().setModuleNames(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_SUBLEDGER)) {
		    new AutoCompleterForSubLedger().setSubLedgerCodes(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_FISCAL_PERIOD)) {
		    new AutoCompleterForFiscalPeriod().setFiscalPeriods(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_ORIG_TERMINAL)) {
		    new AutoCompleterForPorts().setOriginTerminals(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_DEST_PORT)) {
		    new AutoCompleterForPorts().setDestinationPorts(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_SSL_NAME)) {
		    new AutoCompleterForSSLine().setSSLines(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_DESTIN)) {
		    new AutoCompleterForPorts().setDPorts(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_USRNAME)) {
		    new AutoCompleterForUser().setUserByName(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_TERMIN)) {
		    new AutoCompleterForTerminals().getTerminals(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_CITY)) {
		    new AutoCompleterForCity().setCity(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_GENERICCODE)) {
		    new AutoCompleterForGenericCode().setCountry(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_EMAIL)) {
		    new AutoCompleterForUser().setEmailAddress(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_BILLIING_TERMINAL)) {
		    new AutoCompleterForTerminals().setBillingTerminals(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_DESTINATION)) {
		    new AutoCompleterForPorts().setDestination(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_STATE)) {
		    new AutoCompleterForGenericCode().setCountry(request, response);
		} else if (action.trim().equals(CommonConstants.AUTOCOMPLETER_BL)) {
		    new AutoCompleterForBL().setBlAutoCompleteResults(request, response);
		}else if  (action.trim().equals(CommonConstants.AUTOCOMPLETER_PORTNAME)){
                    new AutoCompleterForPortName().getPortName(request, response);
                }else if  (action.trim().equals(CommonConstants.AUTOCOMPLETER_MASTER_NAME)){
                    new AutoCompleterForMaster().setMasterDetails(request, response);
                }
	    }
	} catch (Exception e) {
	    log.info("doPost() in AutoCompleterServlet failed on " + new Date(),e);
	}
    }
}
