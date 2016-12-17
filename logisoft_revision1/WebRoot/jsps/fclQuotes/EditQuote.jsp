<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.bc.notes.NotesConstants,
         com.gp.cong.logisoft.beans.*,com.gp.cvst.logisoft.beans.*,com.gp.cong.logisoft.hibernate.dao.*,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.bc.fcl.QuotationDTO,com.gp.cvst.logisoft.beans.TransactionBean,com.gp.cong.logisoft.bc.ratemanagement.*,com.gp.cong.logisoft.domain.*,com.gp.cvst.logisoft.struts.form.*,com.gp.cvst.logisoft.hibernate.dao.*"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO,com.gp.cong.logisoft.util.CommonFunctions"%>
<%@ page import="java.util.*,java.text.*,com.gp.cong.logisoft.bc.fcl.* "%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.QuotationConstants"/>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@include file="../includes/jspVariables.jsp" %>
<bean:define id="oceanfreightcharge" type="String">
    <bean:message key="oceanfreightcharge"/>
</bean:define>
<bean:define id="oceanfreightImpcharge" type="String">
    <bean:message key="oceanfreightImpcharge"/>
</bean:define>
<bean:define id="RoleQuote" type="String">
    <bean:message key="RoleQuote"/>
</bean:define>
<bean:define id="fileNumberPrefix" type="String">
    <bean:message key="fileNumberPrefix"/>
</bean:define>
<bean:define id="defaultAgent" type="String">
    <bean:message key="defaultAgent"/>
</bean:define>
<%    request.setAttribute("HAZARDOUS", BookingConstants.HAZARDOUS_CODE_DESC);
    String finalized = "";
    String quotesChargesDisable = "";
    Quotation quotdomain = new Quotation();
    PortsDAO portsDAO = new PortsDAO();
    String quotnum = "";
    String specialequipment = "";
    String hazmat = "";
    String localdryage = "";
    Double amount = 0.0;
    Double amount1 = 0.0;
    String insu = "";
    String deductFfcomm = "";
    String outgage = "";
    String sed = "";
    String intermodal = "", fileNo = "",fileNumber = "" ;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    NumberFormat numb = new DecimalFormat("###,###,##0.00");
    String quoteDate = "";
    String userEmail = "";
    String userName = "";
    User userid = null;
    String hiddenLocDrayage = "";
    String msg = "";
    String loackMessage = "";
    boolean check = false;
    String message = "";
    String selectedCheck = "", ssline = "", selectedComCode = "";
    Double totalCharegs = 0.00;
    String disNo = "";
    String view = "";
    String commId = "", destination = "", regionRemarks = "", orginRemarks ="";
    String terminalNumber = "";
    DBUtil dbUtil = new DBUtil();
    String path = request.getContextPath();
    String ocenFrightRollUpAmount = "";
    String oceanFrightForCollapse = "";
    String carrierPrint = "";
    String importantDisclosures = "";
    String printPortRemarks = "";
    String docsInquiries = "";
    Date date = new Date();
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
    String todaysDate = format.format(date);
    String mandatoryFieldForQuotes = "";
    List rateList = new ArrayList();
    boolean importFlag = false;
    boolean hasUserLevelAccess = roleDuty.isShowDetailedCharges();
    String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        request.setAttribute("companyCode", companyCode);
    request.setAttribute("hasUserLevelAccess", hasUserLevelAccess);
    if (session.getAttribute("loginuser") != null) {
        userid = (User) session.getAttribute("loginuser");
        userName = userid.getLoginName();
        request.setAttribute("userName", userName);
    }
    if (userid.getEmail() != null) {
        userEmail = userid.getEmail();
    }
    if (request.getAttribute("msg") != null) {
        msg = (String) request.getAttribute("msg");
    }
    if (request.getAttribute("loackRecord") != null) {
        msg = (String) request.getAttribute("loackRecord");
    }
    if (request.getAttribute("ratesbasicsmessage") != null) {
        message = (String) request.getAttribute("ratesbasicsmessage");
    } else if (request.getAttribute("message") != null) {
        message = (String) request.getAttribute("message");
    }
    QuotationDTO quotationDTO = new QuotationDTO();
    if (session.getAttribute("quotationDTO") != null) {
        quotationDTO = (QuotationDTO) session.getAttribute("quotationDTO");
        selectedCheck = quotationDTO.getSelectedCheck();
        selectedComCode = quotationDTO.getSelectedComCode();
        ssline = quotationDTO.getSsline();
    }

    TransactionBean transactionBean = new TransactionBean();
    com.gp.cvst.logisoft.util.DBUtil dbU = new com.gp.cvst.logisoft.util.DBUtil();
    session.setAttribute("clientlist", dbU.getVendortype());
    view = (String) session.getAttribute("modifyforquotation");

    if (session.getAttribute("view") != null) {
        view = (String) session.getAttribute("view");
        check = true;
        session.removeAttribute("view");
    }
    if (request.getAttribute(QuotationConstants.LOCK) != null) {
        loackMessage = (String) request.getAttribute(QuotationConstants.LOCK);
        view = "3";
    }
    if (request.getAttribute("transactionbean") != null) {
        transactionBean = (TransactionBean) request.getAttribute("transactionbean");
    }
    if (request.getAttribute("QuoteValues") != null) {
        quotdomain = (Quotation) request.getAttribute("QuoteValues");
        quotnum = quotdomain.getQuoteNo();
        request.setAttribute("ratesbasicsmessage", "Rates Basis----------->" + quotdomain.getOrigin_terminal() + " to " + quotdomain.getDestination_port());
        if (quotdomain.getQuoteDate() != null) {
            quoteDate = sdf.format(quotdomain.getQuoteDate());
        }
        deductFfcomm=quotdomain.getDeductFfcomm();
        if (quotdomain.getFileNo() != null) {
            fileNumber = quotdomain.getFileNo();
            if (fileNumber != null) {
                fileNo = fileNumberPrefix + quotdomain.getFileNo();
            } else {
                fileNo = "NEW";
            }
        }
        if (null != quotdomain.getFileNo()) {
            QuotationDAO qDao = new QuotationDAO();
            request.setAttribute("isBlFile", qDao.isBlFile(quotdomain.getFileNo()));
        } else {
            request.setAttribute("isBlFile", false);
        }
        if (quotdomain.getQuoteId() != null) {
            quotnum = quotdomain.getQuoteId().toString();
        }
        if (null != quotdomain.getDestination_port()) {
            destination = quotdomain.getDestination_port();
            regionRemarks = new QuotationBC().fetchRegionRemarks(destination, null);
        }
        if (null != regionRemarks) {
            request.setAttribute("regionRemarks", regionRemarks.replaceAll("(\\r\\n|\\n)", "<br/>"));
        }
        if(null != quotdomain.getOrigin_terminal()){
        String[] countryName = quotdomain.getOrigin_terminal().split("/");
        orginRemarks = new PortsDAO().fetchOrginRemarks(countryName[0].toString());
        }
        if(null != orginRemarks){
        request.setAttribute("orginRemarks", orginRemarks);
        }
        transactionBean.setLdprint("off");
        transactionBean.setIdinclude("off");
        transactionBean.setIdprint("off");
        transactionBean.setInsureinclude("off");
        transactionBean.setInsureprint("off");
        transactionBean.setHazmat("N");
        transactionBean.setSpecialequipment("N");
        transactionBean.setPrintDesc("off");
        transactionBean.setOriginCheck("off");
        transactionBean.setPolCheck("off");
        transactionBean.setPodCheck("off");
        transactionBean.setDestinationCheck("off");
        transactionBean.setOutofgate("N");
        transactionBean.setDeductFFcomm("N");
        transactionBean.setCustomertoprovideSED("N");
        transactionBean.setInland("N");
        transactionBean.setInsurance("N");
        transactionBean.setCommodityPrint("on");
        transactionBean.setCarrierPrint("on");
        transactionBean.setPrintPortRemarks("on");
        transactionBean.setImportantDisclosures("on");
        transactionBean.setDocsInquiries("off");
        transactionBean.setChangeIssuingTerminal("N");
        transactionBean.setFinalized("off");
        transactionBean.setDefaultAgent("N");
        transactionBean.setPrintRemarks("off");
        transactionBean.setRatesNonRates("R");
        transactionBean.setBreakBulk("N");
        transactionBean.setRoutedAgentCheck(quotdomain.getRoutedAgentCheck());
        transactionBean.setLdprint(quotdomain.getLdprint());
        transactionBean.setIdinclude(quotdomain.getIdinclude());
        transactionBean.setIdprint(quotdomain.getIdprint());
        transactionBean.setInsureinclude(quotdomain.getInsureinclude());
        transactionBean.setInsureprint(quotdomain.getInsureprint());
        transactionBean.setHazmat(quotdomain.getHazmat());
        transactionBean.setSpecialequipment(quotdomain.getSpecialequipment());
        transactionBean.setPrintDesc(quotdomain.getPrintDesc());
        transactionBean.setOutofgate(quotdomain.getOutofgage());
        transactionBean.setDeductFFcomm(quotdomain.getDeductFfcomm());
        transactionBean.setCustomertoprovideSED(quotdomain.getCustomertoprovideSed());
        transactionBean.setInland(quotdomain.getInland());
        transactionBean.setDocCharge(quotdomain.getDocCharge());
        transactionBean.setGreenDollarDocCharge(quotdomain.getDocCharge());
        transactionBean.setInsurance(quotdomain.getInsurance());
        transactionBean.setCommodityPrint(quotdomain.getCommodityPrint());
        transactionBean.setCarrierPrint(quotdomain.getCarrierPrint());
        transactionBean.setNewClient(quotdomain.getNewClient());
        transactionBean.setBulletRatesCheck(quotdomain.getBulletRatesCheck());
        transactionBean.setClientConsigneeCheck(quotdomain.getClientConsigneeCheck());
        transactionBean.setFinalized(quotdomain.getFinalized());
        transactionBean.setDefaultAgent(quotdomain.getDefaultAgent());
        request.setAttribute("checkDefaultAgent", quotdomain.getDefaultAgent());
        transactionBean.setOriginCheck(quotdomain.getOriginCheck());
        transactionBean.setPolCheck(quotdomain.getPolCheck());
        transactionBean.setPodCheck(quotdomain.getPodCheck());
        transactionBean.setDestinationCheck(quotdomain.getDestinationCheck());
        transactionBean.setPrintRemarks(quotdomain.getRemarksFlag());
        transactionBean.setCcEmail(quotdomain.getCcEmail());
        transactionBean.setRatesNonRates(quotdomain.getRatesNonRates());
        transactionBean.setBreakBulk(quotdomain.getBreakBulk());
        transactionBean.setFileType(quotdomain.getFileType());
        transactionBean.setDirectConsignmntCheck(quotdomain.getDirectConsignmntCheck());
        request.setAttribute("checkDirectConsign", quotdomain.getDirectConsignmntCheck());
        transactionBean.setRampCheck(quotdomain.getRampCheck());
        transactionBean.setSpotRate(quotdomain.getSpotRate());
        transactionBean.setBrand(quotdomain.getBrand());
        
        if (quotdomain.getImportantDisclosures() == null || quotdomain.getImportantDisclosures() == "") {
            transactionBean.setImportantDisclosures("off");
        } else {
            transactionBean.setImportantDisclosures(quotdomain.getImportantDisclosures());
        }
        if (quotdomain.getPrintPortRemarks() == null || quotdomain.getPrintPortRemarks() == "") {
            transactionBean.setPrintPortRemarks("off");
        } else {
            transactionBean.setPrintPortRemarks(quotdomain.getPrintPortRemarks());
        }
        if (quotdomain.getDocsInquiries() == null || quotdomain.getDocsInquiries() == "") {
            transactionBean.setDocsInquiries("off");
        } else {
            transactionBean.setDocsInquiries(quotdomain.getDocsInquiries());
        }
        if (quotdomain.getChangeIssuingTerminal() == null || quotdomain.getChangeIssuingTerminal() == "") {
            transactionBean.setChangeIssuingTerminal("N");
        } else {
            transactionBean.setChangeIssuingTerminal(quotdomain.getChangeIssuingTerminal());
        }
         if (quotdomain.getChassisCharge() == null || quotdomain.getChassisCharge() == "") {
            transactionBean.setChassisCharge("N");
        } else {
            transactionBean.setChassisCharge(quotdomain.getChassisCharge());
        }
           if (quotdomain.getPierPass() == null || quotdomain.getPierPass() == "") {
                   transactionBean.setPierPass("N");
               } else {
                   transactionBean.setPierPass(quotdomain.getPierPass());
               }
        carrierPrint = transactionBean.getCarrierPrint();
        importantDisclosures = transactionBean.getImportantDisclosures();
        printPortRemarks = transactionBean.getPrintPortRemarks();
        docsInquiries = transactionBean.getDocsInquiries();
        request.setAttribute("transactionbean", transactionBean);
        request.setAttribute("navigationCheck", quotdomain.getFileType());
        amount = (Double) quotdomain.getAmount();
        if (quotdomain.getFinalized() != null && quotdomain.getFinalized().equals("on")) {
            finalized = quotdomain.getFinalized();
            check = true;
        }
        if (amount == null) {
            amount = 0.0;
        }

        amount1 = (Double) quotdomain.getAmount1();
        if (quotdomain.getAmount1() != null) {
            totalCharegs = totalCharegs + Double.parseDouble(dbUtil.dayList1(quotdomain.getAmount1().toString()));
        }
        if (amount1 == null) {
            amount1 = 0.0;
        }
        sed = quotdomain.getCustomertoprovideSed();
        importFlag = quotdomain.getFileType().equalsIgnoreCase("I") ? true : false;
    } else {
        importFlag = ((null != session.getAttribute(ImportBc.sessionName)) ? true : false);
    }
    String star = (importFlag) ? "" : "*";
    request.setAttribute("importFlag", importFlag);
    request.setAttribute("insuranceAllowed",portsDAO.getInsuranceAllowed());
    request.setAttribute("spclList", dbUtil.getUnitListForFCLTest1(new Integer(41), "yes", "Select Special Equipments"));
    request.setAttribute("quotnum", quotnum);
    request.setAttribute("isSpotRated", new QuoteDwrBC().isNotSpotRate(quotdomain.getQuoteId().toString()));
    if (transactionBean.getRampCheck() != null && transactionBean.getRampCheck().equalsIgnoreCase("on")) {
        request.setAttribute("typeOfMoveList", new QuoteDwrBC().getrampNvoMoveList());
    } else {
        request.setAttribute("typeOfMoveList", new QuoteDwrBC().getNvoMoveList());
    }
    if (view != null && finalized != null && view.equals("3") || finalized.equals("on")) {
        quotesChargesDisable = "on";
    }
    String sslName = "";
    if (null != request.getAttribute("QuoteValues")) {
        Quotation quote = (Quotation) request.getAttribute("QuoteValues");
        sslName = (null != quote.getSslname()) ? quote.getSslname() : "";
        sslName = sslName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;");
    }

    DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
    if (quotdomain.getFileNo() != null) {
         if (importFlag) {
         request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(quotdomain.getFileNo().toString(), "IMPORT FILE", "", "Scan or Attach"));
                } else{
         request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(quotdomain.getFileNo().toString(), "FCLFILE", "", "Scan or Attach"));
         }
         request.setAttribute("ManualNotes", new NotesDAO().isManualNotes(quotdomain.getFileNo().toString()));
         if (quotdomain.getClientnumber() != null && !"".equalsIgnoreCase(quotdomain.getClientnumber())) {
            request.setAttribute("isClientBlueNotes", new NotesDAO().isCustomerNotes(quotdomain.getClientnumber().toString()));
        }
    }
    //----CHECKING WHETHER RATES ARE PRESENT OR NOT & TO ALLOW CHANGE IN ORIGIN & DESTINATION------.
    int rateListSize = 0;
    String chargeCodeValue = null;
    List expandList = new ArrayList();
    List otherList = new ArrayList();
    if (null != request.getAttribute("fclRates")) {
        expandList = (List) request.getAttribute("fclRates");
        rateList.addAll(expandList);
    }
    if (null != request.getAttribute("otherChargesList")) {
        otherList = (List) request.getAttribute("otherChargesList");
        rateList.addAll(otherList);
    }
    rateListSize = rateList.size();
    if (null != quotdomain && null != quotdomain.getBulletRatesCheck() && quotdomain.getBulletRatesCheck().equals("on")) {
        request.setAttribute("bulletRates", true);
    } else {
        request.setAttribute("bulletRates", false);
    }
    if (importFlag) {
        mandatoryFieldForQuotes = "Mandatory Fields Needed<br>1)Client<BR>2)Destination<br>3)Origin"
                + "<br>4)Rates button<BR>5)Type of Move<br>6)Goods Description";
    } else {
        mandatoryFieldForQuotes = "Mandatory Fields Needed<br>1)Client<BR>2)Destination<br>3)Origin"
                + "<br>4)Rates button<BR>5)Type of Move<br>6)ERT<br>7)Goods Description";
    }
%>
<html>
    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="quoteDate" value="${QuoteValues.quoteDate}"/>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <head>
            <title>Quotation</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script type='text/javascript' src="${path}/js/editQuote.js"></script>
        <script type='text/javascript' src="${path}/js/rates.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script language="javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript">
            start = function () {
                loadFunction();
                displayToolTipDiv();
                serializeForm();
                tabMoveAfterDeleteRates('${importFlag}');
                getAdjustValue('<%=fileNo%>');
                spotMsg('${QuoteValues.spotRate}', "");
                getTypeofMoveList(document.getElementById('typeofMoveSelect'), '${QuoteValues.quoteId}');
               showbrandValue('${QuoteValues.quoteId}');

                if (document.getElementById("nonRated").checked) {
                    checkBulkBreak();
                }
                if (document.searchQuotationform.defaultAgent[0].checked) {
                    document.searchQuotationform.defaultAgent[0].value = 'Y';
                } else {
                    document.searchQuotationform.defaultAgent[1].value = 'N';
                }

               if ((checkDefault != null && checkDefault != undefined) && (checkDirect != null && checkDirect != undefined)) {
                        if (checkDefault == 'Y' && checkDirect == 'off') {
                       // document.searchQuotationform.defaultAgent[0].checked = true;
                        document.getElementById("defaultAgentY").checked = true;
                        document.getElementById("directConsignmentN").checked = true;
                        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                        document.getElementById("agent").readOnly = true;                        
                        document.getElementById("routedAgentCheck").className = "dropdown_accounting";
                        document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";
                    }
                        else if (checkDefault == 'N' && checkDirect == 'on') {
                        document.getElementById("defaultAgentN").checked = true;
                        document.getElementById("directConsignmentY").checked = true;
                        jQuery("#agent").attr("disabled", true);
                        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";                        
                        document.getElementById("routedAgentCheck").className = "dropdown_accountingDisabled";
                        document.getElementById("routedbymsg").className = "textlabelsBoldForTextBoxDisabledLook";
                    }
                        else  if(checkDefault == 'N' && checkDirect == 'off') {
                        document.getElementById("defaultAgentN").checked = true;
                        document.getElementById("directConsignmentN").checked = true;
                        document.getElementById("agent").className = "textlabelsBoldForTextBox";
                        document.getElementById("agent").readOnly = true;
                        document.getElementById("routedAgentCheck").className = "dropdown_accounting";
                        document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";
                    }
               }
              enableOrDisablePierPass();
              

                if (document.getElementById('bulletRates') && ${bulletRates eq true}) {
                        document.getElementById('bulletRates').checked = true;
                    }
                    document.getElementById('save').focus();
                }
                window.onload = start;
        </script>

        <script language=vbscript>
                function goBackCallMsgBox2(strMsg)
                        callMsgBox2 = msgBox(strMsg, 4, "title for msgbox")
                end
                function
        </script>
        <script language="javascript" type="text/javascript">
                var HAZARDOUS = "${HAZARDOUS}";
                var importFlag = "${importFlag}";
                var destinationDbValue = "${QuoteValues.destination_port}";
                var originDbValue = "${QuoteValues.origin_terminal}";
                var insuranceAllowed = "${insuranceAllowed}";
                var isSpotRate = "${isSpotRated}";
                function loadFunction() {
                    checkCarrier();
                    checkDiscloser("${QuoteValues.ratesNonRates}", "<%=importantDisclosures%>");
                    checkprintPortRemark("${QuoteValues.ratesNonRates}");
                    makeARInvoiceButtonGreen("${QuoteValues.fileNo}","");
                    displayShortCutCharges();
                    allowInsurance();
                    enableOrDisableChassis();
                }

                var special = "<%= specialequipment%>"
                var hazmat = "<%=hazmat%>"
                var outgage = "<%=outgage%>"
                var sed = "<%=sed%>"
                var deductFfcomm = "<%=deductFfcomm%>"
                var insu = "<%=insu%>"
                var carrierPrint = "<%=carrierPrint%>";
                var importantDisclosures = "<%=importantDisclosures%>";
                var printPortRemarks = "<%=printPortRemarks%>";
                var docsInquiries = "<%=docsInquiries%>";
                var checkDefault = "${checkDefaultAgent}";
                var checkDirect = "${checkDirectConsign}";
                function getInland(ev, val1, val2) {
                    //--CHECKING WHETHER RATES ARE COMING FROM RATES TABLE I.E. GETRATES POP UP---
                    var val = checkIfRatesAreFromGetRates();
                    if (val == "true") {
                        if (document.getElementById("rampCheck").checked) {
                            alertNew("Intermodal Ramp Charges can be added only if rates are from getRates");
                        } else {
                            alertNew("Inland Charges can be added only if rates are from getRates");
                        }
                        document.searchQuotationform.inland[1].checked = true;
                        return;
                    } else if (document.searchQuotationform.zip.value == "") {
                        if (document.getElementById("rampCheck").checked) {
                            alertNew("Please Enter ZIp Code In Order to select Intermodal Ramp");
                        } else {
                            if (importFlag == 'true') {
                             alertNew("Please Enter ZIp Code In Order to select Delivery");
                            }else{
                               alertNew("Please Enter ZIp Code In Order to select Inland");
                            }
                        }
                        document.searchQuotationform.inland[0].checked = false;
                        document.searchQuotationform.inland[1].checked = true;
                        return;
                    } else if ("<%=fileNo%>" == "") {
                        quotupdation("${importFlag}", "inland");
                    } else {
                        addInland(ev);
                    }
                }
                
  function enableOrDisableChassis(){
               
                if(!${importFlag}){
                   jQuery.ajaxx({
                      data: {
                          className: "com.gp.cvst.logisoft.hibernate.dao.ChargesDAO",
                          methodName: "checkvendorForChassisChargeOnload",
                          param1: "${QuoteValues.quoteId}"       
                      },
                       success: function(data) {
                          
                       if(data === "" || data === null){
                           
                           jQuery("#chassisChargeY").attr("disabled",true);
                           jQuery("#chassisChargeN").attr("disabled",true);
                           
                        } else {
                            jQuery("#chassisChargeY").attr("disabled",false);
                           jQuery("#chassisChargeN").attr("disabled",false);  
                       }
                   }
                   }); 
                  }
                  }
                function enableOrDisablePierPass(){
                if(!${importFlag}){
               var origin = document.searchQuotationform.isTerminal.value;
               var pol = document.searchQuotationform.placeofReceipt.value;
               var nvoMove = document.searchQuotationform.typeofMove.value;                    
               if(origin === pol && (nvoMove === "DOOR TO DOOR" || nvoMove === "DOOR TO PORT" || nvoMove === "DOOR TO RAIL")){ 
                jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.UnLocationDAO",
                    methodName: "getPierPass",
                    param1: origin
                },
                success: function (data) {
                    if(data === "true"){
                     jQuery("#pierPassY").attr("disabled", false);
                     jQuery("#pierPassN").attr("disabled", false); 
                    }
                  }
                });                                
               }else{
                  jQuery("#pierPassY").attr("disabled", true);
                  jQuery("#pierPassN").attr("disabled", true);
                  document.searchQuotationform.pierPass ="N";
               }               
             }
                }
                function polChange() {                   
                   var placeofReceiptForPierPass = jQuery("#placeofReceiptForPierPass").val();
                   var pol = document.searchQuotationform.placeofReceipt.value;
                    if(pol !== placeofReceiptForPierPass && document.searchQuotationform.pierPass.value === "Y" && document.searchQuotationform.isTerminal.value !== ''){
                                        confirmNew("Pol value is chaged Pier pass charge will be removed, are you sure you want to continue? ", "polChangeDeletePierPassCharge");
               
                }
                enableOrDisablePierPass();
            }
                function originChange() {                   
                   var isTerminalForPierPass = jQuery("#isTerminalForPierPass").val();
                   var origin = document.searchQuotationform.isTerminal.value;
                    if(origin !== isTerminalForPierPass && document.searchQuotationform.pierPass.value === "Y" && document.searchQuotationform.placeofReceipt.value !== ''){
                     
                     confirmNew("Origin value is chaged Pier pass charge will be removed, are you sure you want to continue? ", "originChangeDeletePierPassCharge");             
                }
                enableOrDisablePierPass();
            }
                function addInland(ev) {
                    var hazmat = "";
                    if (document.searchQuotationform.hazmat[0].checked) {
                        hazmat = "Y";
                    } else {
                        hazmat = "N";
                    }
                    var spcleqpt = "";
                    if (document.searchQuotationform.specialequipment[0].checked) {
                        spcleqpt = "Y";
                    } else {
                        spcleqpt = "N";
                    }
                    var ratedOption = "";
                    if (undefined != document.getElementById("nonRated") && true == document.getElementById("nonRated").checked) {
                        ratedOption = "NonRated";
                    } else {
                        ratedOption = "rated";
                    }
                    if (document.getElementById("rampCheck").checked) {
                        GB_show("get Input Rates", rootPath +"quoteCharge.do?markup=" + "Intermodal Ramp" + "&hazmat=" + hazmat + "&quoteNo="
                                + ev + "&spcleqpmt=" + spcleqpt + "&button=" + "editquote" + "&fileNo=" + "<%=fileNo%>" + "&ratedOption=" + ratedOption + "&importFlag=" + "${importFlag}", 500, 950);
                    } else {
                        GB_show("get Input Rates", rootPath +"/quoteCharge.do?markup=" + "inland" + "&hazmat=" + hazmat + "&quoteNo="
                                + ev + "&spcleqpmt=" + spcleqpt + "&button=" + "editquote" + "&fileNo=" + "<%=fileNo%>" + "&ratedOption=" + ratedOption + "&importFlag=" + "${importFlag}", 500, 950);
                    }
                }
                function getHazmat(val) {
                    if (document.searchQuotationform.numbers == undefined && null == document.getElementById("otherCharges")) {
                        alertNew("Please Select Rates before selecting Hazmat");
                        jQuery("#charge").css("border-color", "red");
                        return;
                    } else {
                        if (collapseid == "") {
                            getcollapse();
                        }
                        document.searchQuotationform.check1.value = collapsePrint;
                        document.searchQuotationform.collapseid.value = collapseid;
                        checkPrintInclude();
                        if (document.searchQuotationform.hazmat[0].checked) {
                            GB_show("Hazmat", "/logisoft/fCLHazMat.do?buttonValue=editQuotation&name=Quote&number=" + "<%=quotnum%>" + "&fileNo=" + val,
                                    width = "500", height = "1200");
                        } else {
                            alertNew("Please Change Hazmat to Yes");
                            return;
                        }
                    }
                }
                function confirmMessageFunction(id1, id2) {
                    if (document.searchQuotationform.defaultAgent[0].checked) {
                        document.searchQuotationform.defaultAgent[0].value = "Y";
                    } else {
                        document.searchQuotationform.defaultAgent[1].value = "N";
                    }
                      if (id1 == "inland" && id2 == "ok") {
                        var inlandLabel = document.getElementById("inlandVal").innerHTML;
                        if (document.getElementById("rampCheck").checked) {
                            document.getElementById("rampCheck").checked = false;
                            document.getElementById("inlandVal").innerHTML = "Inland";
                        }
                        if (inlandLabel == "Inland" || inlandLabel == "Delivery") {
                            document.searchQuotationform.buttonValue.value = "deleteInlandToBl";
                        } else {
                            document.searchQuotationform.buttonValue.value = "deleteIntermodelRampToBl";
                        }
                        document.searchQuotationform.submit();
                    } else if (id1 == "inland" && id2 == "cancel") {
                        document.searchQuotationform.inland[0].checked = true;
                    } else if (id1 == "inland" && id2 == "cancel") {
                        document.searchQuotationform.inland[0].checked = true;
                    } else if (id1 == "inland" && id2 == "no") {
                        document.getElementById("scrollUp").scrollIntoView(true);
                        showPopUp();
                        document.getElementById("localDrayageCommentDiv").style.display = "block";
                        var IpopTop = (screen.height - document.getElementById("localDrayageCommentDiv").offsetHeight) / 2;
                        var IpopLeft = (screen.width - document.getElementById("localDrayageCommentDiv").offsetWidth) / 2;
                        document.getElementById("localDrayageCommentDiv").style.left = IpopLeft + document.body.scrollLeft - 70;
                        document.getElementById("localDrayageCommentDiv").style.top = IpopTop + document.body.scrollTop - 150;
                        document.getElementById("commentType").value = "inland";
                    } else if (id1 == "printFaxEmail" && id2 == "no") {
                        document.getElementById("scrollUp").scrollIntoView(true);
                        showPopUp();
                        document.getElementById("localDrayageCommentDiv").style.display = "block";
                        var IpopTop = (screen.height - document.getElementById("localDrayageCommentDiv").offsetHeight) / 2;
                        var IpopLeft = (screen.width - document.getElementById("localDrayageCommentDiv").offsetWidth) / 2;
                        document.getElementById("localDrayageCommentDiv").style.left = IpopLeft + document.body.scrollLeft - 70;
                        document.getElementById("localDrayageCommentDiv").style.top = IpopTop + document.body.scrollTop - 150;
                        document.getElementById("commentType").value = "inland";
                        overrideComment();
                    } else if (id1 == "printFaxEmail" && id2 == "yes") {
                        overrideComment();
                        //--allow to enter local drayage or inter modal---
                        //quotationPrintSubmit();
                    } else if (id1 == "deliveryCharge" && id2 == "no") {
                        document.getElementById("scrollUp").scrollIntoView(true);
                        showPopUp();
                        document.getElementById("deliveryChargeCommentDiv").style.display = "block";
                        var IpopTop = (screen.height - document.getElementById("deliveryChargeCommentDiv").offsetHeight) / 2;
                        var IpopLeft = (screen.width - document.getElementById("deliveryChargeCommentDiv").offsetWidth) / 2;
                        document.getElementById("deliveryChargeCommentDiv").style.left = IpopLeft + document.body.scrollLeft - 70;
                        document.getElementById("deliveryChargeCommentDiv").style.top = IpopTop + document.body.scrollTop - 150;
                        document.getElementById("commentType").value = "delivery";
                    } else if (id1 == "deliveryCharge" && id2 == "yes") {
                        getDeliveryCharge("${QuoteValues.quoteId}");
                    } else if (id1 == "onCarriage" && id2 == "no") {
                        onCarriageOverride();
                    } else if (id1 == "onCarriage" && id2 == "yes") {
                        openRates();
                    } else if (id1 == "hazCharge" && id2 == "no") {
                        //convertToBkg();
                    } else if (id1 == "hazCharge" && id2 == "yes") {
                        convertToBkg();
                    } else if (id1 == "hazChargeArrivalNotice" && id2 == "no") {
                        //convertToBkg();
                    } else if (id1 == "hazChargeArrivalNotice" && id2 == "yes") {
                        proceedConvertToArrivalNotice();
                    } else if (id1 == "copyQuote" && id2 == "yes") {
                        document.searchQuotationform.copyOrigin.value = document.searchQuotationform.isTerminal.value;
                        if (document.searchQuotationform.ratesNonRates[0].checked) {
                            document.searchQuotationform.buttonValue.value = "copyQuoteWithoutRate";
                            document.searchQuotationform.submit();
                        } else {
                            popupAddRates("windows", "<%=fileNo%>", "copyQuote",'${importFlag}');
                        }
                    } else if (id1 == "acceptVid" && id2 == "yes") {
                        if (collapseid == "") {
                            getcollapse();
                        }
                        document.searchQuotationform.check1.value = collapsePrint;
                        document.searchQuotationform.collapseid.value = collapseid;
                        checkPrintInclude();

                        document.searchQuotationform.spotRate.value = "Y";
                        document.searchQuotationform.buttonValue.value = "acceptVidOnSpotRate";
                        document.searchQuotationform.submit();
                    } else if (id1 == "acceptVid" && id2 == "no") {
                        jQuery("#spotRateN").attr('checked', 'checked');
                    } else if (id1 == "convertToBookings" && id2 == "ok") {
                        document.getElementById("eventCode").value = "100001";
                        if (document.searchQuotationform.ratesNonRates[0].checked) {
                            getConverttoBook("oldgetRatesBKG", "");
                        } else {
                            var d = new Date();
                            var haz;
                            if (document.searchQuotationform.hazmat[0].checked) {
                                haz = "Y";
                            } else {
                                haz = "N";
                            }
                            var car = document.searchQuotationform.sslDescription.value + "//" + document.searchQuotationform.sslcode.value;
                            var career = escape(car);
                            var origin = document.searchQuotationform.isTerminal.value;
                            var pol = document.searchQuotationform.placeofReceipt.value;
                            var pod = document.searchQuotationform.finalDestination.value;
                            var dest = document.searchQuotationform.portofDischarge.value;
                            var comid = document.searchQuotationform.commcode.value;
                            var carrier = career;
                            var quoteDate = "<%=quoteDate%>";
                            var mm = (d.getMonth() < 9 ? "0" : "") + (d.getMonth() + 1);
                            var dd = (d.getDate() < 10 ? "0" : "") + d.getDate();
                            var yyyy = d.getFullYear();
                            var bookingDate = mm + "/" + dd + "/" + yyyy;
                            var fileNo = "<%=quotdomain.getFileNo()%>";
                            var hazmat = haz;
                            jQuery.ajaxx({
                                dataType: "json",
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName: "rateChangeAlert",
                                    param1: origin,
                                    param2: pol,
                                    param3: pod,
                                    param4: dest,
                                    param5: comid,
                                    param6: carrier,
                                    param7: quoteDate,
                                    param8: bookingDate,
                                    param9: fileNo,
                                    param10: hazmat,
                                    request: "true",
                                    dataType: "json"
                                },
                                preloading: true,
                                success: function (data) {
                                    if (data) {
                                        getConverttoBook("oldgetRatesBKG", "");
                                    } else {
                                        var ofrAmount = document.getElementById("ofrRollUpAmount").value;
                                        var oceanFrightForCollapse = document.getElementById("oceanFrightForCollapse").value;
                                        var url = rootPath + "/fclQuotes.do?buttonValue=getRatesBooking&origin=" + document.searchQuotationform.isTerminal.value
                                                + "&pol=" + document.searchQuotationform.placeofReceipt.value + "&pod=" + document.searchQuotationform.finalDestination.value
                                                + "&destn=" + document.searchQuotationform.portofDischarge.value + "&comid=" + document.searchQuotationform.commcode.value
                                                + "&carrier=" + career + "&quoteDate=" + "<%=quoteDate%>"
                                                + "&bookingDate=" + bookingDate + "&OFRRollUpAmount=" + ofrAmount + "&oceanFrightForCollapse=" + oceanFrightForCollapse + "&fileNo=" + "<%=quotdomain.getFileNo()%>" + "&hazmat=" + haz;

                                        GB_show("Rate Change Alert", url, 500, 950);
                                    }
                                }
                            });
                        }
                    } else if (id1 == "convertToArrivalNotice" && id2 == "ok") {
                        proceedConvertToArrivalNotice();
                    } else if (id1 == "deleteArCharges" && id2 == "ok") {
                        document.searchQuotationform.submit();
                    } else if (id1 == "deleteSpecialEquipmentUnit" && id2 == "ok") {
                        document.searchQuotationform.submit();
                    } else if (id1 == "getHazmatForRatesN" && id2 == "ok") {
                        document.searchQuotationform.buttonValue.value = "hazmat";
                        document.searchQuotationform.submit();
                    } else if (id1 == "getHazmatForRates" && id2 == "ok") {
                        saveHazmat();
                    } else if (id1 == "getHazmatForRates" && id2 == "cancel") {
                        retainHazmat();
                    } else if (id1 == "getHazmatForRatesN" && id2 == "cancel") {
                        retainHazmat();
                    } else if (id1 == "hazmatWithInland" && id2 == "ok") {
                        document.searchQuotationform.inland[1].checked = true;
                        document.searchQuotationform.buttonValue.value = "hazmatWithInland";
                        document.searchQuotationform.submit();
                    } else if (id1 == "hazmatWithInland" && id2 == "cancel") {
                        document.searchQuotationform.hazmat[1].checked = true;
                    } else if (id1 == "hazmatWithIntrModel" && id2 == "ok") {
                        document.getElementById("rampCheck").checked = false;
                        document.getElementById("inlandVal").innerHTML = "Inland";
                        document.searchQuotationform.inland[1].checked = true;
                        document.searchQuotationform.buttonValue.value = "hazmatWithIntrModel";
                        document.searchQuotationform.submit();
                    } else if (id1 == "hazmatWithIntrModel" && id2 == "cancel") {
                        document.searchQuotationform.hazmat[1].checked = true;
                    } else if (id1 == "getFFCommission" && id2 == "ok") {
                        document.searchQuotationform.buttonValue.value = "FFCommssion";
                        document.searchQuotationform.submit();
                    } else if (id1 == "deleteFFCommission" && id2 == "ok") {
                        document.searchQuotationform.buttonValue.value = "deleteFFCommssion";
                        document.searchQuotationform.submit();
                    } else if (id1 == "getFFCommission" && id2 == "cancel") {
                        document.getElementById("n5").checked = true;
                    } else if (id1 == "deleteDocCharge" && id2 == "ok") {
                        document.searchQuotationform.buttonValue.value = "deleteDocumentCharge";
                        document.searchQuotationform.submit();
                    } else if (id1 == "deleteDocCharge" && id2 == "cancel") {
                        document.getElementById("docChargeY").checked = true;
                    } else if (id1 == "deletePierPassCharge" && id2 == "ok") {
                        document.searchQuotationform.buttonValue.value = "deletePierPassCharge";
                        document.searchQuotationform.submit();
                    } else if (id1 == "deletePierPassCharge" && id2 == "cancel") {
                        document.getElementById("pierPassY").checked = true;
                    } else if (id1 == "polChangeDeletePierPassCharge" && id2 == "ok") {
                        document.getElementById("pierPassN").checked = true;
                        document.searchQuotationform.buttonValue.value = "deletePierPassCharge";
                        document.searchQuotationform.submit();
                    } else if (id1 == "polChangeDeletePierPassCharge" && id2 == "cancel") {
                        document.getElementById("pierPassY").checked = true;
                        var placeofReceiptForPierPass = jQuery("#placeofReceiptForPierPass").val();
                        jQuery("#placeofReceipt").val(placeofReceiptForPierPass);
                        jQuery("#placeofReceipt_check").val(placeofReceiptForPierPass); 
                    } else if (id1 == "originChangeDeletePierPassCharge" && id2 == "ok") {
                        document.getElementById("pierPassN").checked = true;
                        document.searchQuotationform.buttonValue.value = "deletePierPassCharge";
                        document.searchQuotationform.submit();
                    } else if (id1 == "originChangeDeletePierPassCharge" && id2 == "cancel") {
                        document.getElementById("pierPassY").checked = true;
                        var isTerminalForPierPass = jQuery("#isTerminalForPierPass").val();
                        jQuery("#isTerminal").val(isTerminalForPierPass);
                        jQuery("#isTerminal_check").val(isTerminalForPierPass);
                    } else if (id1 == "deleteFFCommission" && id2 == "cancel") {
                        document.getElementById("y5").checked = true;
                    } else if (id1 == "goBack" && id2 == "yes") {
                        yesFunction();
                        localStorage.removeItem('fclContactId');
                        localStorage.removeItem('fclAccountNo');
                    } else if (id1 == "goBack" && id2 == "no") {
                        noFunction();
                        localStorage.removeItem('fclContactId');
                        localStorage.removeItem('fclAccountNo');
                    } else if (id1 == "goBack" && id2 == "cancel") {
                        cancelFunction();
                    } else if (id1 == "insuranceDelete" && id2 == "ok") {
                        document.searchQuotationform.costofgoods.readOnly = true;
                        document.searchQuotationform.buttonValue.value = "deleteInsuranceToBl";
                        document.searchQuotationform.submit();
                    } else if (id1 == "deleteSpecialEquipment" && id2 == "cancel") {
                        document.searchQuotationform.specialequipment[0].checked = true;
                    } else if (id1 == "deleteSpecialEquipment" && id2 == "ok") {
                        document.searchQuotationform.buttonValue.value = "deleteSpecialEquipment";
                        document.searchQuotationform.submit();
                    } else if (id1 == "insuranceDelete" && id2 == "cancel") {
                        document.getElementById("y8").checked = true;
                    } else if (id1 == "changeOriginDestination" && id2 == "ok") {
                        document.getElementById("portofDischarge").value = "";
                        document.getElementById("isTerminal").value = "";
                        document.getElementById("placeofReceipt").value = "";
                        document.getElementById("finalDestination").value = "";
                        if (importFlag == 'true') {
                            document.searchQuotationform.commcode_check.value = "006205";
                            document.searchQuotationform.commcode.value = "006205";
                            document.searchQuotationform.description.value = "IMPORT FCL GDSM";
                        } else {
                            document.searchQuotationform.commcode_check.value = "006100";
                            document.searchQuotationform.commcode.value = "006100";
                            document.searchQuotationform.description.value = "DEPARTMENT STORE MERCHANDISE";
                        }
                        document.searchQuotationform.buttonValue.value = "changeOriginDestination";
                        document.searchQuotationform.submit();
                    } else if (id1 == "applyDefaultValues" && id2 == "ok") {
                        fillDefaultCustomerData();
                    } else if (id1 == "Econo/Ecu Worldwide" && id2 == "yes") {
        
        document.searchQuotationform.brand.value = "Econo";
        document.searchQuotationform.buttonValue.value = "addBrandValue";
        document.searchQuotationform.submit();
    }else if (id1 == "OTI/Ecu Worldwide" && id2 == "yes") {
       
        document.searchQuotationform.brand.value = "OTI";
        document.searchQuotationform.buttonValue.value = "addBrandValue";
        document.searchQuotationform.submit();
                    }
    else if (id1 === "Ecu Worldwide/Econo" && id2 == "yes") {
        document.searchQuotationform.brand.value = "Ecu Worldwide";
        document.searchQuotationform.buttonValue.value = "addBrandValue";
        document.searchQuotationform.submit();
    }else if (id1 === "Ecu Worldwide/OTI" && id2 === "yes"){
        document.searchQuotationform.brand.value = "Ecu Worldwide";
        document.searchQuotationform.buttonValue.value = "addBrandValue";
        document.searchQuotationform.submit();
    } else if (id1 === "Econo/Ecu Worldwide" && id2 == "no") {
     var data = "Econo/Ecu Worldwide";
      var splitarray = data.split("/");
      var splitarray1 =splitarray[1];
      
      if(splitarray1 === "Ecu Worldwide"){
        document.getElementById('brandEcono').checked = false;
        document.getElementById('brandEcuworldwide').checked = true;
      }
    } else if (id1 === "OTI/Ecu Worldwide" && id2 == "no") {
      var data = "OTI/Ecu Worldwide";
      var splitarray = data.split("/");
      var splitarray1 =splitarray[1];
     
      if(splitarray1 === "Ecu Worldwide"){
        document.getElementById('brandOti').checked = false;
        document.getElementById('brandEcuworldwide').checked = true;
      }
    } else if (id1 === "Ecu Worldwide/Econo" && id2 == "no") {
      var data = "Ecu Worldwide/Econo";
      var splitarray = data.split("/");
      var splitarray1 =splitarray[1];
     
      if(splitarray1 === "Econo"){
       document.getElementById('brandEcono').checked = true;
       document.getElementById('brandEcuworldwide').checked = false;
       }
                }
                else if (id1 === "Ecu Worldwide/OTI" && id2 == "no") {
      var data = "Ecu Worldwide/OTI";
      var splitarray = data.split("/");
      var splitarray1 =splitarray[1];
     
      if(splitarray1 === "OTI"){
       document.getElementById('brandOti').checked = true;
       document.getElementById('brandEcuworldwide').checked = false;
       }
           } else if(id1 === "deleteChassisCharge" && id2 === "yes"){
           document.getElementById('chassisChargeY').checked = false;
         document.getElementById('chassisChargeN').checked = true;
           document.searchQuotationform.buttonValue.value = "deleteChassisCharge";
        document.searchQuotationform.submit();
} else if(id1 === "deleteChassisCharge" && id2 === "no"){
           document.getElementById('chassisChargeN').checked = true;
       
}
           
                    }


                var stat;
                var button;
                function setFocus(ev1, ev2) {                
                    if (document.searchQuotationform.breakBulk != null) {
                        if (document.getElementById("breakBulk2").checked) {
                            document.getElementById("sslcode").readOnly = true;
                        }
                    }
                    if (ev2 == "quotePrint" || ev2 == "quotePrintwithoutsave") {
                        var issuingTerminal = jQuery("#issuingTerminal").val();
                        var cce = "";
                        var path = "${path}/printConfig.do?screenName=Quotation&quotationNo=${QuoteValues.quoteId}&fileNo=<%=fileNumber%>&importFlag=${importFlag}&destination=${QuoteValues.destination_port}&printRemarks=${QuoteValues.remarksFlag}&subject=FCL Quote-+<%=fileNo%>&toName=${QuoteValues.clientname}&toFaxNumber=${QuoteValues.fax}&toAddress=${QuoteValues.email1}&emailMessage=Please find the attached File&ccAddress=" + cce  + "&issuingTerminal=" + issuingTerminal;
                        GB_show("Quotation Report", path, 400, 1000);
                    }
                    stat = ev1;
                    button = ev2;
                    var buttonValue = "${buttonValue}";
                    if (buttonValue == "newgetRates") {
                        ratesFocus();
                    } else {
                        setTimeout("setFocuss()", 800);
                    }
                    window.parent.hideProgressBar();
                }
                function getDeliveryCharge(ev) {
                    var hazmat = "";
                    if (document.searchQuotationform.hazmat[0].checked) {
                        hazmat = "Y";
                    } else {
                        hazmat = "N";
                    }
                    var spcleqpt = "";
                    if (document.searchQuotationform.specialequipment[0].checked) {
                        spcleqpt = "Y";
                    } else {
                        spcleqpt = "N";
                    }
                    var ratedOption = "";
                    if (undefined != document.getElementById("nonRated") && true == document.getElementById("nonRated").checked) {
                        ratedOption = "NonRated";
                    } else {
                        ratedOption = "rated";
                    }
                    GB_show("get Input Rates", "/logisoft/quoteCharge.do?markup=" + "deliveryCharge" + "&hazmat=" + hazmat + "&quoteNo="
                            + ev + "&spcleqpmt=" + spcleqpt + "&button=" + "editquote" + "&fileNo=" + "<%=fileNo%>" + "&ratedOption=" + ratedOption + "&importFlag=" + "${importFlag}", 500, 950);
                }

                function convertToBookings() {
                     var quoteId = jQuery("#quotationNo").val();
                        if (jQuery("#spotRateY").is(":checked") && document.searchQuotationform.ratesNonRates[1].checked && isSpotRate === "No Spot Rate"){
                            alertNew("On Spot Rate Files Spot Costs MUST be entered Manually For All Costs");
                            return;
                        }
                    if (document.searchQuotationform.customerName1 && document.searchQuotationform.customerName1.value == "") {
                        jQuery("#customerName").css("border-color", "red");
                        jQuery("#customerName1").css("border-color", "red");
                        alertNew("PLEASE SELECT CLIENT");
                        return;
                    } else if (document.searchQuotationform.customerName && document.searchQuotationform.customerName.value == "") {
                        jQuery("#customerName").css("border-color", "red");
                        jQuery("#customerName1").css("border-color", "red");
                        alertNew("PLEASE SELECT CLIENT");
                        return;
                    }
                    if (document.searchQuotationform.placeofReceipt.value == "") {
                        alertNew("Please Enter POL");
                        jQuery("#placeofReceipt").css("border-color", "red");
                        return;
                    }
                    if (document.searchQuotationform.finalDestination.value == "") {
                        alertNew("Please Enter POD");
                        jQuery("#finalDestination").css("border-color", "red");
                        return;
                    }
                    if (${importFlag} == false) {
                        if (document.searchQuotationform.typeofMove.selectedIndex == 0) {
                            alertNew("Please Select NVO Move");
                            jQuery("#typeofMoveDiv").css("border", " 1px solid red");
                            return;
                        }
                    }
                    if (document.searchQuotationform.issuingTerminal.value == "") {
                        alertNew("PLEASE SELECT ISSUING TEMINAL");
                        jQuery("#issuingTerminal").css("border", " 1px solid red");
                        return;
                    }
                    if(document.getElementById("directConsignmentN").checked == true){
                    if (((document.searchQuotationform.routedAgentCheck.selectedIndex == 0) && (document.getElementById("nonRated").checked != true)))
                       {
                        alertNew("Please Select ERT");
                        jQuery("#routedAgentCheckDiv").css("border", " 1px solid red");
                        return;
                    }
                    }
                    if (document.searchQuotationform.goodsdesc.value == "" && ${importFlag} != "true") {
                        alertNew("PLEASE ENTER GOODS DESCRIPTION");
                        jQuery("#goodsDesc").css("border", " 1px solid red");
                        if (undefined != action && null != action && action == "inland") {
                            document.searchQuotationform.inland[1].checked = true;
                        }
                        return;
                    }
                    // Delivery remarks OR On-Carriage remarks
                    var navigationType = jQuery("#hiddenImportFlag").val();
                    var doorDest = (navigationType === "true" || navigationType) ? jQuery("#doorOrigin").val() : jQuery("#doorDestination").val();
                    if (doorDest !== "") {
                        if (jQuery("#onCarriage").is(":checked")) {
                            if (jQuery("#hiddenRemarks").val() === "" && checkOnCarriage() === false) {
                                confirmYesOrNo("Oncarriage charge is required. Click Yes to add Oncarriage charge or No to skip this requirement and provide Oncarriage remarks", "onCarriage");
                                return;
                            }
                        } else if (!navigationType && jQuery("#deliveryChargeComments").val() === "" && checkDeliveryRates() === "false") {
                            confirmYesOrNo("Delivery Charge Cost is required when doing door to door move. Choose Yes to continue or choose No to acknowledge and skip past this requirement", "deliveryCharge");
                            return;
                        }
                    }
                    var nvoMove = document.searchQuotationform.typeofMove.value;
                    if (nvoMove == "DOOR TO DOOR" || nvoMove == "DOOR TO PORT" || nvoMove == "DOOR TO RAIL" || nvoMove == "RAMP TO PORT") {
                        var originZip = document.searchQuotationform.zip.value;
                        if (undefined == originZip || originZip == "") {
                            alertNew("Please Enter Origin Zip");
                            jQuery("#zip").css("border-color", "red");
                            return;
                        } else if ((document.searchQuotationform.inland[1].checked &&
                                document.searchQuotationform.intermodelComments.value == "")) {
                            if (document.getElementById("rampCheck").checked) {
                                confirmYesOrNo("Intermodal Ramp Cost is required when doing door move. Choose Yes to continue or choose No to acknowledge and skip past this requirement", "inland");
                            } else {
                                confirmYesOrNo("Inland Cost is required when doing door move. Choose Yes to continue or choose No to acknowledge and skip past this requirement", "inland");
                            }
                            return;
                        }
                    }
                    if (document.searchQuotationform.sslDescription.value == "") {
                        alertNew("Please Enter SSL Name");
                        jQuery("#sslDescription").css("border-color", "red");
                        return;
                    }
                    if (jQuery("#hazmatYes").is(":checked") && checkCharges(HAZARDOUS) === false) {
                        confirmYesOrNo("File is hazardous but does not have hazardous surcharges," +
                                " Do you want to continue to convert to Booking?", "hazCharge");
                        return;
                    }
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                            methodName: "getChargesAvailability",
                            param1: quoteId,
                            dataType: "json"
                        },
                        success: function (data) {
                            if (!data) {
                                alertNew("Please select minimum of One Rate");
                                jQuery("#charge").css("border-color", "red");
                            } else {
                                convertToBkg();
                            }
                        }
                    });
                }

        </script>
        <style type="text/css">
            #QuoteOPtions{
                position:fixed;
                _position:absolute;
                border-style: solid solid solid solid;
                background-color: white;
                z-index:99;
                left:30%;
                top:40%;
                bottom:5%;
                right:5%;
                _height:expression(document.body.offset+"px");
            }
            #chargesOptions{
                position:fixed;
                _position:absolute;
                border-style: solid solid solid solid;
                background-color: white;
                z-index:99;
                left:30%;
                top:40%;
                bottom:5%;
                right:5%;
                _height:expression(document.body.offset+"px");
            }
        </style>
        <%@include file="../includes/resources.jsp" %>
    </head>

    <body class="whitebackgrnd"  topmargin="2">
        <%@include file="../preloader.jsp"%>
         <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <div id="commentDiv"   class="comments">
            <table border="1" id="commentTableInfo">
                <tbody border="0"></tbody>
            </table>
        </div>
        <div id="cover" style="width: 100% ;height: 1000px;"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </form>
        </div>
        <div id="AlertBoxDefaultValues" class="alert">
            <p class="alertHeader"><b>Important Notes</b></p>
            <p id="innerText7" class="containerForAlert"></p>
            <div style="text-align:right;padding-right:10px;padding-bottom:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="onclickAlertOk()">
            </div>
        </div>
        <div id="bubble_tooltip_forComments" style="display: none;">
            <div class="bubble_top_forComments"><span></span></div>
            <div class="bubble_middle_forComments"><span id="bubble_tooltip_content_forComments"></span></div>
            <div class="bubble_bottom_forComments"></div></div>

        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                                   grayOut(false, '');">
            </form>
        </div>
        <div id="AlertBoxForSuspendedCredit" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="alertBoxForSuspendedCreditText" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" onclick="document.getElementById('AlertBoxForSuspendedCredit').style.display = 'none';
                            grayOut(false, '');
                            showCommentsBoxForSuspendedCredit();">
            </form>
        </div>
        <div id="ConfirmBoxForSuspendedCredit" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="confirmBoxForSuspendedCreditText" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" style="width: 50px;" value="Proceed" onclick="proceedConvertToBookings()">
                <input type="button" id="confirmNo"  class="buttonStyleForAlert" value="Cancel" onclick="cancelConvertToBookings()">
            </form>
        </div>
        <div id="CommentsBoxForSuspendedCredit" class="alert">
            <p class="alertHeader"><b>Comments</b></p>
            <textarea id="suspendedCreditComments" class="containerForAlert"
                      style="text-transform: uppercase; width:100%"
                      onkeypress="return checkTextAreaLimit(this, 500)"></textarea>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" style="width: 50px;" value="Proceed" onclick="saveCommentsAndConvertToBookings()">
                <input type="button" id="confirmNo"  class="buttonStyleForAlert" value="Cancel" onclick="cancelCommentsAndConvertToBookings()">
            </form>
        </div>
        <div id="ConfirmBox" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" value="OK"
                       onclick="yes()">
                <input type="button" id="confirmNo"  class="buttonStyleForAlert" value="Cancel"
                       onclick="No()">
            </form>
        </div>
        <div id="reminderBox" class="alert">
            <p class="alertHeader"><b>REMINDER:</b></p>
            <p id="innerTextReminder" class="containerForAlert">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" value="OK"
                       onclick="okForReminderQuote()">
            </form>
        </div>
        <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmYes()">
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="confirmNo()">
            </form>
        </div>
        <div id="ConfirmYesNoCancelDiv" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="confirmMessagePara" class="containerForAlert">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmOptionYes()">
                <input type="button" id="confirmNo"  class="buttonStyleForAlert" value="No"
                       onclick="confirmOptionNo()">
                <input type="button" id="confirmCancel"  class="buttonStyleForAlert" value="Cancel"
                       onclick="confirmOptionCancel()">
            </form>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->

        <font color="#FF4A4A" size="2" ><b style="margin-left:10px;"><%=msg%></b>
        <c:if test="${QuoteValues.localdryage=='Y'}">
            <b style="font-size: 15px;">(Local Drayage Included)</b>
        </c:if>
        <b style="margin-left:150px;color: #000080;font-size: 15px;" ><%=loackMessage%></b></font>

        <html:form action="/editQuotes?accessMode=${param.accessMode}"  styleId="editQuote" scope="request">
            <%@include file="../fclQuotes/fragment/clientSearchOptions.jsp"%>
            <div id="QuoteOPtions" style="display:none;width:400px;height:150px; top: 200px !important" align="center">
                <table class="tableBorderNew" width="100%">
                    <tr class="textlabelsBold">
                        <td>
                            Carrier Print&nbsp;
                        </td>
                        <td>
                            <html:radio property="carrierPrint" value="on" name="transactionbean"  styleId="carrierPrintOn" onclick="carrierOPition(this)"/>Yes
                            <html:radio property="carrierPrint" value="off" name="transactionbean" styleId="carrierPrintOff" onclick="carrierOPition(this)"/>No
                        </td>
                    </tr>
                    <c:if test="${QuoteValues.ratesNonRates=='N'}">
                        <tr class="textlabelsBold">
                            <td>
                                Important Disclosures Print&nbsp;
                            </td>
                            <td>
                                <html:radio property="importantDisclosures" value="on" name="transactionbean"  styleId="importantDisclosuresOn" onclick="disclosureOPition(this)"/>Yes
                                <html:radio property="importantDisclosures" value="off" name="transactionbean" styleId="importantDisclosuresOff" onclick="disclosureOPition(this)"/>No
                            </td>
                        </tr>
                    </c:if>
                    <tr class="textlabelsBold">
                        <td>
                            Use Alternate email address for Docs/Inquiries&nbsp;
                        </td>
                        <td>
                            <html:radio property="docsInquiries" value="on" name="transactionbean"  styleId="docsInquiriesOn" onclick="docsInquiriesOPition(this)"/>Yes
                            <html:radio property="docsInquiries" value="off" name="transactionbean" styleId="docsInquiriesOff" onclick="docsInquiriesOPition(this)"/>No
                        </td>
                    </tr>
                    <c:if test="${QuoteValues.ratesNonRates=='N'}">
                        <tr class="textlabelsBold">
                            <td>
                                Print Port Remarks&nbsp;
                            </td>
                            <td>
                                <html:radio property="printPortRemarks" value="on" name="transactionbean"  styleId="printPortRemarksOn" onclick="printPortRemarksOPition(this)"/>Yes
                                <html:radio property="printPortRemarks" value="off" name="transactionbean" styleId="printPortRemarksOff" onclick="printPortRemarksOPition(this)"/>No
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${importFlag eq false}">
                        <tr class="textlabelsBold" id ="printGRIPortRemarks">
                            <td>
                                Use Terminal Info for change in Issuing Terminal&nbsp;
                            </td>
                            <td>
                                <html:radio property="changeIssuingTerminal" value="Y" name="transactionbean"  styleId="changeIssuingTerminalY" onclick="printPortRemarksOPition(this)"/>Yes
                                <html:radio property="changeIssuingTerminal" value="N" name="transactionbean" styleId="changeIssuingTerminalN" onclick="printPortRemarksOPition(this)"/>No
                            </td>
                        </tr>
                    </c:if>
                    <tr class="textlabelsBold" align="center">
                        <td colspan="2"><input type="button" value="Submit" class="buttonStyleNew" onclick="submitQuoteOPtions()"/>
                            <input type="button" value="Cancel" class="buttonStyleNew" onclick="closeDivs()"/></td>
                    </tr>
                </table>
            </div>
            <div id="chargesOptions" style="display:none;width:400px;height:150px; top: 200px !important" align="center">
                <table class="tableBorderNew" width="100%">
                    <tr class="textlabelsBold">
                        <td>
                            Use True Cost
                        </td>
                        <td>
                            <html:radio property="greenDollarUseTrueCost" value="Y" styleId="greenDollarUseTrueCostY" name="transactionbean" onclick="toggleReduceOFR('Y');"/>Yes
                            <html:radio property="greenDollarUseTrueCost" value="N" styleId="greenDollarUseTrueCostN" name="transactionbean" onclick="toggleReduceOFR('N');"/>No
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>
                            Reduce Ocean Freight by&nbsp;
                        </td>
                        <td>
                            <input Class="textlabelsBoldForTextBox" name="reducedOceanFreight" id="reducedOceanFreight" value=""
                                   size="22" onchange="allowOnlyWholeNumbers(this)"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>
                            Administration Charge&nbsp;
                        </td>
                        <td>
                            <input Class="textlabelsBoldForTextBox" name="adminCharge" id="adminCharge" value=""
                                   size="22" onchange="allowOnlyWholeNumbers(this)"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>
                            Document Charge
                        </td>
                        <td>
                            <html:radio property="greenDollarDocCharge" value="Y" styleId="greenDollarDocChargeY" name="transactionbean" onclick="modifyVisibilityDocumentCharge('Y');"/>Yes
                            <html:radio property="greenDollarDocCharge" value="N" styleId="greenDollarDocChargeN" name="transactionbean" onclick="modifyVisibilityDocumentCharge('N');"/>No
                            <input Class="textlabelsBoldForTextBox" name="documentCharge" id="documentCharge" value=""
                                   size="9" style="visibility: hidden"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold" align="center">
                        <td colspan="2"><input type="button" value="Save" class="buttonStyleNew" onclick="submitChargesOPtions()"/>
                            <input type="button" value="Cancel" class="buttonStyleNew" onclick="closeChargesDivs()"/></td>
                    </tr>
                </table>
            </div>
            <table width="100%" border="0" cellpadding="2" cellspacing="0" >
                <tr>
                    <td id="scrollUp">
                        <table   border="0">
                            <tr>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>File No :<span class="fileNo"><%=fileNo%></span></td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>Quote By :<b class="headerlabel" style="color:blue;">
                                                    <c:out value="${fn:toUpperCase(QuoteValues.quoteBy)}" >
                                                    </c:out></b></td>
                                            <td style="padding-left:5px;">On :
                                                <b  class="headerlabel"  style="color:blue;"><c:out value="${quoteDate}" ></c:out></b></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table class="tableBorderNew">
                                            <tr class="textlabelsBold">
                                                <td>Booked By :<b  class="headerlabel" style="color:blue;"><c:out value="${fn:toUpperCase(QuoteValues.bookedBy)}" ></c:out></b></td>
                                                <td style="padding-left: 5px;">On :
                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="bookedDate" value="${QuoteValues.bookedDate}"/>
                                                <b class="headerlabel" style="color:blue;" >
                                                    <c:out value="${bookedDate}" ></c:out></b></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table class="tableBorderNew">
                                            <tr class="textlabelsBold">
                                                <td>BL By :<b  class="headerlabel" style="color:blue;"><c:out value="${fn:toUpperCase(QuoteValues.blBy)}"></c:out></b></td>
                                                <td style="padding-left:5px;">On :
                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="blDate" value="${QuoteValues.blDate}"/>
                                                <b class="headerlabel" style="color:blue;"><c:out value="${blDate}"></c:out></b></td>
                                            </tr>
                                        </table>
                                    </td>
                                <c:if test="${bulletRates eq true}">
                                    <td>
                                        <table class="tableBorderNew">
                                            <tr class="textlabelsBold">
                                                <td>
                                                    <div class="red bold">**BULLET RATES**</div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </c:if>

                                <td id="creditStatusCol" style="display:none">
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>
                                                <div id="creditStatus" class="red bold"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td id="spotRateMsgDiv" style="display:none">
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>
                                                <div id="spotRateMsgStatus" class="red bold"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        <html:hidden property="buttonValue" styleId="buttonValue"/>
                        <html:hidden property="imsTrucker" styleId="imsTrucker"/>
                        <html:hidden property="imsBuy" styleId="imsBuy"/>
                        <html:hidden property="imsSell" styleId="imsSell"/>
                        <html:hidden property="imsQuoteNo" styleId="imsQuoteNo"/>
                        <html:hidden property="importantNotes" styleId="importantNotes"/>
                        <html:hidden property="imsLocation" styleId="imsLocation"/>
                        <html:hidden property="ertAdjustedValue" styleId="ertAdjustedValue"/>
                        <html:hidden property="selectedCheck" styleId="selectedCheck"/>
                        <html:hidden property="finalized" value="<%=quotesChargesDisable%>"/>
                        <html:hidden property="unitSelect"/>
                        <html:hidden property="number"/>
                        <html:hidden property="copyOrigin"/>
                        <html:hidden property="chargeCode"/>
                        <html:hidden property="docChargeAmount" styleId="docChargeAmount" value="${QuoteValues.documentAmount}"/>
                        <html:hidden property="chargeCodeDesc"/>
                        <html:hidden property="costSelect"/>
                        <html:hidden property="currency"/>
                        <html:hidden property="chargeAmt"/>
                        <html:hidden property="minimumAmt"/>
                        <html:hidden property="selectedOrigin" />
                        <html:hidden property="action" />

                        <html:hidden property="unitselected"/>
                        <html:hidden property="insuranceAmount" value="${QuoteValues.insurancamt}"/>
                        <html:hidden property="insurancamt" value="${QuoteValues.insurancamt}"/>
                        <html:hidden property="selectedDestination"/>
                        <html:hidden property="selectedComCode"/>
                        <html:hidden property="quotationNo" value="${QuoteValues.quoteId}" styleId="quotationNo"/>
                        <html:hidden property="eventCode" styleId="eventCode"/>
                        <html:hidden property="moduleId" value="<%=NotesConstants.FILE%>"/>
                        <html:hidden property="moduleRefId" styleId="moduleRefId" value="${QuoteValues.fileNo}"/>
                        <html:hidden property="homeScreenFileFlag" styleId="homeScreenFileFlag" value="${homeScreenFileFlag}"/>
                        <html:hidden property="vendorName" styleId="vendorName"/>
                        <html:hidden property="accountNo" styleId="accountNo"/>
                        <html:hidden property="amount" styleId="amount"/>
                        <html:hidden property="markUp" styleId="markUp"/>
                        <input type="hidden" id="hiddenRemarks" value="${QuoteValues.onCarriageRemarks}"/>
                        <input type="hidden" id="hiddenImportFlag" value="${importFlag}"/>
                        <input type="hidden" id="quoteBy" value="${QuoteValues.quoteBy}"/>
                        <input type="hidden" id="quoteDate" value="${quoteDate}"/>
                        <input type="hidden" id="isTerminalForPierPass" value="${QuoteValues.origin_terminal}"/>
                        <input type="hidden" id="placeofReceiptForPierPass" value="${QuoteValues.plor}"/>
                        <input type="hidden" id="importFlag" value="${importFlag}"/>
                        <input type="hidden" id="companyCode" value="${companyCode}"/>
                        <input type="button" id="cancel" value="Go Back" onclick="compareWithOldArray1('<%=view%>', '<%=finalized%>', '${QuoteValues.fileNo}')" class="buttonStyleNew"/>
                        <input type="button" id="save" value="Save" onclick="quotupdation('${importFlag}', '')" class="buttonStyleNew"
                               onmouseover="tooltip.show('<strong><%=mandatoryFieldForQuotes%></strong>', null, event);" onmouseout="tooltip.hide();"/>
                        <%if (!fileNo.equals("")) {%>
                        <input type="button" id="conBL" value="ConvertToBooking" onclick="convertToBookings()" class="buttonStyleNew" style="width:110px" />
                        <c:if test="${importFlag && isBlFile eq false}">
                            <input type="button" id="ConvertToArrivalNotice" value="Convert to Arrival Notice" onclick="convertToArrivalNotice('${QuoteValues.quoteId}', '${QuoteValues.quoteBy}', '${quoteDate}')" class="buttonStyleNew" style="width:130px" />
                        </c:if>
                        <input type="button" id="copy" value="Copy" onclick="copyNewQuote()" class="buttonStyleNew"/>
                        <input type="button" id="print" value="Print/Fax/Email" class="buttonStyleNew" style="width:110px"
                               onclick="quotationPrint('${QuoteValues.fileNo}', '<%=finalized%>')"/>
                        <c:set var="manualNotesCount" value="buttonStyleNew"/>
                        <c:if test="${ManualNotes}">
                            <c:set var="manualNotesCount" value="buttonColor"/>
                        </c:if>
                        <input type="button"  class="${manualNotesCount}" id="note"
                               onclick="return GB_show('Notes', '${path}/notes.do?moduleId=' + '<%=NotesConstants.FILE%>&moduleRefId=' + '<%=quotdomain.getFileNo()%>', 300, 730);"
                               id="note" name="search" value="Note"/>
                        <c:choose>
                            <c:when test="${null!=TotalScan && TotalScan!='0'}">
                                <input id="scanButton" class="buttonColor" type="button"
                                       value="Scan/Attach" onClick="scan('<%=quotdomain.getFileNo()%>', '${importFlag}')"/>
                            </c:when>
                            <c:otherwise>
                                <input id="scanButton" class="buttonStyleNew" type="button"
                                       value="Scan/Attach" onClick="scan('<%=quotdomain.getFileNo()%>', '${importFlag}')"/>
                            </c:otherwise>
                        </c:choose>
                        <%}%>
                        <input type="button" id="Options" value="Options" onclick="quoteOptions()" class="buttonStyleNew" />
                        <input type="button" id="HazmatButton"  class="buttonStyleNew" value="Hazmat" onclick="getHazmat('<%=fileNo%>')"/>
                        <c:if test="${! empty QuoteValues.fileNo}">
                            <input type="button" id="arRedInvoice" value="AR Invoice" class="buttonStyleNew"
                                   onclick="return GB_show('AR Invoice', '${path}/arRedInvoice.do?action=listArInvoice&fileNo=${QuoteValues.fileNo}&screenName=QUOTE&fileType=${QuoteValues.fileType}', 550, 1100)"/>
                        </c:if>
                    </td>
                    <td align="right">
                    <td>
                        <c:if test="${roleDuty.allowtoEnterSpotRate and QuoteValues.ratesNonRates eq 'R' and importFlag eq false}">
                            Spot/Bullet Rate
                            <html:radio property="spotRate" value="Y" name="transactionbean" styleId="spotRateY" onclick="confirmVid('Y','${userName}')"/>Y
                            <html:radio property="spotRate" value="N" name="transactionbean" styleId="spotRateN" onclick="confirmVid('N','${userName}')"/>N &nbsp;&nbsp;
                        </c:if>
                        Non-Rated<html:radio property="ratesNonRates" value="N" styleId="nonRated"
                                    name="transactionbean" disabled="true"/>
                        Rated<html:radio property="ratesNonRates" value="R" styleId="rated"
                                    name="transactionbean" disabled="true"/>&nbsp;&nbsp;
                        <c:if test="${QuoteValues.ratesNonRates=='N'}">
                            Break Bulk
                            <html:radio property="breakBulk" value="Y" name="transactionbean" styleId="breakBulk1" onclick=""/>Y
                            <html:radio property="breakBulk" value="N" name="transactionbean" styleId="breakBulk2" onclick=""/>N
                        </c:if>
                          Brand
                          <c:choose>
                              <c:when test="${companyCode == '03'}">
                                  <html:radio property="brand" value="Econo"  name="transactionbean" styleId="brandEcono" onclick="checkBrand('Econo','${QuoteValues.quoteId}','${companyCode}')"/>Econo
                              </c:when>
                              <c:otherwise>
                                  <html:radio property="brand" value="OTI"  name="transactionbean" styleId="brandOti" onclick="checkBrand('OTI','${QuoteValues.quoteId}','${companyCode}')"/>OTI
                              </c:otherwise>
                          </c:choose>
                          <html:radio property="brand" value="${commonConstants.ECU_Worldwide}" name="transactionbean" styleId="brandEcuworldwide" onclick="checkBrand('Ecu Worldwide','${QuoteValues.quoteId}','${companyCode}')"/>Ecu Worldwide

                    </td>
                </tr>
            </table>

            <table  border="0"  width="100%" cellpadding="0"  cellspacing="0" class="tableBorderNew">
                <tr><td>
                        <table width="100%" border="0" cellpadding="0"  cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew"><td colspan="8">Client</td></tr>
                            <tr>
                                <td valign="top">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="right" style="padding-top: 4px;">Client&nbsp;</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right" style="padding-top: 4px;">Contact Name&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table border="0">
                                        <tr class="textlabelsBold">
                                            <td id="existingClient" align="left">
                                                <input type="text" name="customerName" id="customerName"  size="22"
                                                       value="${QuoteValues.clientname}" onchange="tabMoveClient('${importFlag}');" maxlength="50" style="text-transform: uppercase"
                                                       class=" textlabelsBoldForTextBox mandatory" onfocus="checkClientConsignee()"/>
                                                <input id="custname_check" type="hidden" value="${QuoteValues.clientname}"/>
                                                <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                            </td>
                                            <td id="newerClient" align="left">
                                                <input type="text" name="customerName1" id="customerName1"  size="22"
                                                       value="${QuoteValues.clientname}" Class="textlabelsBoldForTextBox mandatory"
                                                       onkeydown="focusSetting(false);" onchange="getEmptyClient()"
                                                       style="text-transform: uppercase"  maxlength="50" />
                                                <input name="dup" type="hidden" Class="textlabelsBoldForTextBox"/>
                                            </td>
                                            <td>
                                                <img src="${path}/images/icons/search_filter.png" id="clientSearchEdit"
                                                     onmouseover="tooltip.show('<strong>Click here to edit Client Search options</strong>', null, event);" onmouseout="tooltip.hide();" onclick="showClientSearchOption()"/>
                                                <c:choose>
                                                    <c:when test="${isClientBlueNotes}">
                                                        <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14"
                                                             id="clientIcon" onclick="openBlueScreenNotesInfo(jQuery('#clientNumber').val(), jQuery('#customerName').val());"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14"
                                                             id="clientIcon" onclick="openBlueScreenNotesInfo(jQuery('#clientNumber').val(), jQuery('#customerName').val());"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <html:checkbox property="clientConsigneeCheck" styleId="clientConsigneeCheck"  name="transactionbean"
                                                               onmouseover="tooltip.show('<strong>Checked=Consignee Listed <p> UnChecked=Consignee Not Listed</strong>',null,event);" onmouseout="tooltip.hide();"
                                                               onclick="clearNewClient()"></html:checkbox>
                                                <html:checkbox property="newClient" styleId="newClient" onclick="newClientEQ();setbrandvalueBasedONDestination('${companyCode}');" name="transactionbean"/>New
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>
                                                <html:text property="contactName" styleId="contactName" style="text-transform: uppercase"
                                                           value="${QuoteValues.contactname}" size="22" maxlength="200" styleClass="textlabelsBoldForTextBox"/>
                                                <div id="contactName_choices" style="display: none" class="autocomplete"></div>
                                                <img src="<%=path%>/img/icons/display.gif" alt="Look Up" align="middle" id="contactNameButton" onclick="getContactInfo()"/>
                                            </td>
                                        </tr>
                                    </table></td>
                                <td width="9%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="right" style="padding-bottom: 4px;">Acct No&nbsp;</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Email&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="16%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td class="textBoxBorder">
                                                <input type="text" name="clientNumber" id="clientNumber" value="${QuoteValues.clientnumber}" tabindex="-1"
                                                       maxlength="15" readonly="true" class="textlabelsBoldForTextBoxDisabledLook" size="22" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <html:text property="email" styleId="email" value="${QuoteValues.email1}" maxlength="500" onblur="emailValidate(this)"  size="22" styleClass="textlabelsBoldForTextBox" tabindex="-1"/>
                                                <div id="email_choices" style="display: none" class="autocomplete"></div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="11%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="right">Phone&nbsp;</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Fax&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="12%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td class="textlabelsBold">
                                                <html:text property="phone" maxlength="30" styleId="phone"  value="${QuoteValues.phone}" onblur="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox" size="22" onkeyup="getIt(this)"/>
                                                <input type="checkbox"  style="visibility: hidden;" />
                                                <font class="textlabelsBold" style="visibility: hidden;">New</font>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>
                                                <html:text property="fax" maxlength="30" styleId="fax" value="${QuoteValues.fax}" onblur="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox" size="22" onkeyup="getIt(this)"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="12%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="right">Client Type:&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="18%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="center">
                                                <html:text property="clienttype" styleId="clienttype" styleClass="whitebackgrnd" value="${QuoteValues.clienttype}"
                                                           style="color:black;font:10;width:100%;font-weight:bold;border:0px;" readonly="true" tabindex="-1"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
                <tr><td >

                        <table width="100%" border="0" cellpadding="0"  cellspacing="0"  class="tableBorderNew">
                            <tr class="tableHeadingNew"><td colspan="7">Trade Route</td></tr>
                            <tr>

                                <td valign="top">
                                    <table border="0" cellpadding="1" >
                                        <%if (importFlag) {%>
                                        <tr class="textlabelsBold">
                                            <td align="right">&nbsp;&nbsp;&nbsp;Origin&nbsp;</td>
                                            <td id="isTerminal1">
                                                <input Class="textlabelsBoldForTextBox mandatory" name="isTerminal" id="isTerminal" value="${QuoteValues.origin_terminal}"
                                                       size="22" onchange="copyValPol();" onkeydown="getTemp()"  />
                                                <input type="hidden" id="isTerminal_check" value="${QuoteValues.origin_terminal}"/>
                                                <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>', null, event);" onmouseout="tooltip.hide();">
                                                    <input type="checkbox" checked="checked" id="isTerminal1City"></span>

                                                <div id="isTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                        initAutocomplete("isTerminal", "isTerminal_choices", "", "isTerminal_check",
                                                                "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6&importFlag=true&radio=city&isDojo=false", "setFocusFromDojo('portofDischarge')");
                                                </script>

                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">&nbsp;&nbsp;&nbsp;Destination&nbsp;</td>
                                            <td><input Class="textlabelsBoldForTextBox mandatory" onchange="copyValPod();" name="portofDischarge" id="portofDischarge" value="${QuoteValues.destination_port}" size="22"
                                                       onkeyup="getDestination()"
                                                       onkeydown="setDojoAction()"  />
                                                <input type="hidden" id="portofDischarge_check" value="${QuoteValues.destination_port}"/>
                                                <div id="destination_port_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>', null, event);" onmouseout="tooltip.hide();">
                                                    <input type="checkbox" id="destinationCity" checked="checked"></span>

                                                <div id="portofDischarge_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                                <script type="text/javascript">
                                                        initAutocomplete("portofDischarge", "portofDischarge_choices", "", "portofDischarge_check",
                                                                "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7&radio=city&isDojo=false", "setFocusFromDojo('getRates')");
                                                </script>
                                                <%-- TO CHANGE DESTINATION & ORIGIN --%>
                                                <img src="${path}/img/icons/astar.gif" id="astarIcon" style="" onclick="changeOriginAndDestination()"
                                                     onmouseover="tooltip.show('<strong>Change Origin & Destination</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                            </td>
                                        </tr>
                                        <%} else {%>
                                        <tr class="textlabelsBold">
                                            <td align="right">&nbsp;&nbsp;&nbsp;Destination&nbsp;</td>
                                            <td><input Class="textlabelsBoldForTextBox mandatory" onchange="tabMoveDestination();" name="portofDischarge" id="portofDischarge" value="${QuoteValues.destination_port}" size="22"
                                                       onkeyup="getDestination()" onkeydown="setDojoAction()"  />
                                                <input type="hidden" id="portofDischarge_check" value="${QuoteValues.destination_port}"/>
                                                <div id="destination_port_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>', null, event);" onmouseout="tooltip.hide();">
                                                    <input type="checkbox" id="destinationCity" checked="checked"></span>
                                                <span onmouseover="tooltip.show('<strong>Show Rates for Entire Country</strong>', null, event);" onmouseout="tooltip.hide();">
                                                    <input type="checkbox" id="showAllCity" tabindex="-1"/></span>

                                                <div id="portofDischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                        initAutocomplete("portofDischarge", "portofDischarge_choices", "", "portofDischarge_check",
                                                                "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7&radio=city&isDojo=false", "setFocusFromDojo('isTerminal');setbrandvalueBasedONDestination('${companyCode}');");
                                                </script>
                                                <%-- TO CHANGE DESTINATION & ORIGIN --%>
                                                <img src="${path}/img/icons/astar.gif" id="astarIcon" style="" onclick="changeOriginAndDestination()"
                                                     onmouseover="tooltip.show('<strong>Change Origin & Destination</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">&nbsp;&nbsp;&nbsp;Origin&nbsp;</td>
                                            <td id="isTerminal1">
                                                <input Class="textlabelsBoldForTextBox mandatory" name="isTerminal" id="isTerminal" value="${QuoteValues.origin_terminal}"
                                                       size="22" onkeydown="getTemp()" onchange="tabOrigin();" />
                                                <input type="hidden" id="isTerminal_check" value="${QuoteValues.origin_terminal}"/>
                                                <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>', null, event);" onmouseout="tooltip.hide();">
                                                    <input type="checkbox" checked="checked" id="isTerminal1City"></span>

                                                <div id="isTerminal_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                        initAutocomplete("isTerminal", "isTerminal_choices", "", "isTerminal_check",
                                                                "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6&radio=city&isDojo=false", "setFocusFromDojo('getRates'),originChange()");
                                                </script>

                                            </td>
                                        </tr>
                                        <%}%>
                                        <tr class="textlabelsBold">
                                            <td align="right" valign="middle">&nbsp;&nbsp;&nbsp;CommCode&nbsp;</td>
                                            <td><input name="commcode" class="textlabelsBoldForTextBox mandatory" id="commcode" value="${QuoteValues.commcode.code}"
                                                       maxlength="6" size="22" onkeydown="bulletRatesStauts();"/>
                                                <input id="commcode_check" type="hidden" value="${QuoteValues.commcode.code}"/>
                                                <div id="commcode_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                        initAutocomplete("commcode", "commcode_choices", "", "commcode_check",
                                                                "${path}/actions/getChargeCode.jsp?tabName=QUOTE&isDojo=false", "disableAutoFF()");
                                                </script>
                                                <html:checkbox property="bulletRatesCheck" styleId="bulletRates" name="transactionbean"
                                                               onclick="bulletRatesClick();"  onmouseover="tooltip.show('<strong>Bullet Rates</strong>',null,event);"
                                                               onmouseout="tooltip.hide();" tabindex="-1"/>
                                                <input type="button" id="getRates" onClick="return popupAddRates('windows', '<%=fileNo%>','','${importFlag}')"
                                                       Value="Rates" style="width: 52px;" class="buttonStyleNew"/>
                                            </td></tr>
                                        <tr class="textlabelsBold">
                                            <td align="right" id="descriptionLabel" style="display: none;">Description :&nbsp;</td>
                                            <td style="display: none;"><html:text property="description" size="22" value="${QuoteValues.description}"
                                                       style="color:black;width:100%;border:0;font:10;font-weight:bold;"
                                                       styleId="description" styleClass="textlabelsBoldForTextBox" readonly="true" tabindex="-1"></html:text>
                                                </td>
                                                <td align="right" id="descriptionLabel">&nbsp;&nbsp;&nbsp;Issuing TM&nbsp;</td>
                                            <%if (importFlag) {%>
                                            <td>
                                                <input type="text" Class="textlabelsBoldForTextBox mandatory"  name="issuingTerminal"
                                                       value="${QuoteValues.issuingTerminal}" id="issuingTerminal" size="22"  />
                                                <input type="hidden" value="${QuoteValues.issuingTerminal}" id="issuingTerminal_check" />
                                                <div id="issuingTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>

                                                <script type="text/javascript">
                                                        initAutocomplete("issuingTerminal", "issuingTerminal_choices", "", "issuingTerminal_check",
                                                                "${path}/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false&importFlag=true", "setFocusFromDojo('hazmatYes')");
                                                </script>
                                            </td>
                                            <%} else {%>
                                            <td>
                                                <input type="text" Class="textlabelsBoldForTextBox mandatory"  name="issuingTerminal"
                                                       value="${QuoteValues.issuingTerminal}" id="issuingTerminal" size="22"  />
                                                <input type="hidden" value="${QuoteValues.issuingTerminal}" id="issuingTerminal_check" />
                                                <div id="issuingTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>

                                                <script type="text/javascript">
                                                        initAutocomplete("issuingTerminal", "issuingTerminal_choices", "", "issuingTerminal_check",
                                                                "${path}/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false", "setFocusFromDojo('hazmatYes')");
                                                </script>
                                            </td>
                                            <%}%>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Hazmat&nbsp;</td>
                                            <td>
                                                <html:radio property="hazmat" styleId="hazmatYes" value="Y" onkeydown="test()"
                                                name="transactionbean" disabled="<%=check%>" onclick="getHazmatForRates()"/>Yes
                                                <html:radio property="hazmat" value="N" styleId="hazmatNo" name="transactionbean"
                                                onkeydown="" disabled="<%=check%>" onclick="getHazmatForRatesN()"/>No</td>
                                        </tr>
                                    </table>
                                </td>
                                <td valign="top">
                                    <table border="0" width="100%" cellpadding="1">
                                        <tr class="textlabelsBold">
                                            <td align="right">POL</td>
                                            <td>
                                                <input Class="textlabelsBoldForTextBox" name="placeofReceipt" id="placeofReceipt" value="${QuoteValues.plor}" size="22" onchange="" />
                                                <input id="placeofReceipt_check" type="hidden" value="${QuoteValues.plor}" size="22" />
                                                <div id="placeofReceipt_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                        initAutocomplete("placeofReceipt", "placeofReceipt_choices", "", "placeofReceipt_check",
                                                                "${path}/actions/getUnlocationCode.jsp?tabName=QUOTE&from=3&isDojo=false", "setFocusFromDojo('finalDestination'),polChange()");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">POD</td>
                                            <td>
                                                <input Class="textlabelsBoldForTextBox" name="finalDestination" id="finalDestination" value="${QuoteValues.finaldestination}"
                                                       size="22" onkeyup="getPod()" onkeydown="getAgentforPod(this.value)"  />
                                                <input type="hidden" id="finalDestination_check" value="${QuoteValues.finaldestination}"/>
                                                <div id="finalDestination_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                                <script type="text/javascript">
                                                        initAutocomplete("finalDestination", "finalDestination_choices", "", "finalDestination_check",
                                                                "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=4&isDojo=false", "setFocusFromDojo('noOfDays')");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Transit Days
                                            </td>
                                            <td>
                                                <html:text styleClass="textlabelsBoldForTextBox"  property="noOfDays" value="${QuoteValues.noOfDays}"
                                                           size="3" maxlength="3" onchange="numericTextbox(this)"  styleId="noOfDays"/>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <c:choose>
                                                <c:when test="${importFlag}">
                                                    <td align="right">NVO Move</td>
                                                    <c:choose>
                                                        <c:when test="${importFlag}">
                                                            <td id="typeofMove" colspan="5">
                                                                <%--  <div class="mandatory" style="float:left">--%>
                                                                <html:select property="typeofMove" styleId="typeofMoveSelect" onchange="setNvoMove()" style="width:130px;"
                                                                             styleClass="dropdown_accounting" onkeydown="if(event.keyCode==9){tabNvoMove();}" value="${QuoteValues.typeofMove}" >
                                                                    <html:optionsCollection name="typeOfMoveList" />
                                                                </html:select>
                                                                <img src="${path}/img/icons/help-icon.gif" onmouseover="tooltip.show('If NVO move is from \'DOOR\', then spotting address is required on Booking', '60', event);" onmouseout="tooltip.hide()"/>
                                                                <input value="off" type="checkbox" name="rampCheck"  id="rampCheck" style="display: none"/>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td id="typeofMove" colspan="5">
                                                                <%--   <div class="mandatory" style="float:left">--%>
                                                                <html:select property="typeofMove" styleId="typeofMoveSelect" onchange="setNvoMove()" style="width:130px;"
                                                                             styleClass="dropdown_accounting" onkeydown="if(event.keyCode==9){tabNvoMove();}" value="${QuoteValues.typeofMove}" >
                                                                    <html:optionsCollection name="typeOfMoveList" />
                                                                </html:select>
                                                                <img src="${path}/img/icons/help-icon.gif" onmouseover="tooltip.show('If NVO move is from \'DOOR\', then spotting address is required on Booking', '60', event);" onmouseout="tooltip.hide()"/>
                                                                <c:choose>
                                                                    <c:when test="${!empty QuoteValues.zip}">
                                                                        <html:checkbox property="rampCheck" styleId="rampCheck"  name="transactionbean" style="visibility: visible" tabindex="-1"
                                                                                       onmouseover="tooltip.show('<strong>Check here to change Door Origin to Ramp Origin</strong>',null,event);" onmouseout="tooltip.hide();"
                                                                                       onclick="getTypeofMoveList(document.getElementById('typeofMoveSelect'),'${QuoteValues.quoteId}');" ></html:checkbox>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <html:checkbox property="rampCheck" styleId="rampCheck"  name="transactionbean" style="visibility: hidden" tabindex="-1"
                                                                                       onmouseover="tooltip.show('<strong>Check here to change Door Origin to Ramp Origin</strong>',null,event);" onmouseout="tooltip.hide();"
                                                                                       onclick="getTypeofMoveList(document.getElementById('typeofMoveSelect'),'${QuoteValues.quoteId}');" ></html:checkbox>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <td align="right">NVO Move</td>
                                                    <c:choose>
                                                        <c:when test="${importFlag}">
                                                            <td id="typeofMove" colspan="5">
                                                                <div class="mandatory" id="typeofMoveDiv" style="float:left">
                                                                    <html:select property="typeofMove" styleId="typeofMoveSelect" onchange="setNvoMove()" style="width:130px;"
                                                                                 styleClass="dropdown_accounting" onkeydown="if(event.keyCode==9){tabNvoMove();}" value="${QuoteValues.typeofMove}" >
                                                                        <html:optionsCollection name="typeOfMoveList" />
                                                                    </html:select></div>
                                                                <img src="${path}/img/icons/help-icon.gif" onmouseover="tooltip.show('If NVO move is from \'DOOR\', then spotting address is required on Booking', '60', event);" onmouseout="tooltip.hide()"/>
                                                                <input value="off" type="checkbox" name="rampCheck"  id="rampCheck" style="display: none"/>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td id="typeofMove" colspan="5">
                                                                <div class="mandatory" id="typeofMoveDiv" style="float:left">
                                                                    <html:select property="typeofMove" styleId="typeofMoveSelect" onchange="setNvoMove()" style="width:130px;"
                                                                                 styleClass="dropdown_accounting" onkeydown="if(event.keyCode==9){tabNvoMove();}" value="${QuoteValues.typeofMove}" >
                                                                        <html:optionsCollection name="typeOfMoveList" />
                                                                    </html:select></div>
                                                                <img src="${path}/img/icons/help-icon.gif" onmouseover="tooltip.show('If NVO move is from \'DOOR\', then spotting address is required on Booking', '60', event);" onmouseout="tooltip.hide()"/>
                                                                <c:choose>
                                                                    <c:when test="${!empty QuoteValues.zip}">
                                                                        <html:checkbox property="rampCheck" styleId="rampCheck"  name="transactionbean" style="visibility: visible" tabindex="-1"
                                                                                       onmouseover="tooltip.show('<strong>Check here to change Door Origin to Ramp Origin</strong>',null,event);" onmouseout="tooltip.hide();"
                                                                                       onclick="getTypeofMoveList(document.getElementById('typeofMoveSelect'),'${QuoteValues.quoteId}');" ></html:checkbox>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <html:checkbox property="rampCheck" styleId="rampCheck"  name="transactionbean" style="visibility: hidden" tabindex="-1"
                                                                                       onmouseover="tooltip.show('<strong>Check here to change Door Origin to Ramp Origin</strong>',null,event);" onmouseout="tooltip.hide();"
                                                                                       onclick="getTypeofMoveList(document.getElementById('typeofMoveSelect'),'${QuoteValues.quoteId}');" ></html:checkbox>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>

                                        <tr class="textlabelsBold">
                                            <td align="right">File Type</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${importFlag}">
                                                        <input  value="IMPORT" class="BackgrndColorForTextBox" size="4" readonly="true" tabindex="-1" />
                                                        <input type="hidden" name="fileType" id="fileType" value="I"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <html:radio property="fileType" value="S" name="transactionbean"  styleId="fileTypeS">
                                                            <span onmouseover="tooltip.show('<strong>Standard</strong>', null, event);"
                                                                  onmouseout="tooltip.hide();">S</span>
                                                        </html:radio>
                                                        <html:radio property="fileType" value="C" name="transactionbean"  styleId="fileTypeC">
                                                            <span onmouseover="tooltip.show('<strong>CFCL</strong>', null, event);"
                                                                  onmouseout="tooltip.hide();">C</span>
                                                        </html:radio>

                                                        <html:radio property="fileType" value="P" name="transactionbean"  styleId="fileTypeP">
                                                            <span onmouseover="tooltip.show('<strong>Project</strong>', null, event);"
                                                                  onmouseout="tooltip.hide();">P</span>
                                                        </html:radio>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td valign="top">
                                    <table border="0" width="100%" cellpadding="1">
                                        <c:choose>
                                            <c:when test="${importFlag}">
                                                <tr class="textlabelsBold">
                                                    <td align="right">Door at Origin</td>
                                                    <td>
                                                        <input class="textlabelsBoldForTextBox" name="doorDestination" id="doorDestination" value="${QuoteValues.doorDestination}" size="22" onchange="changeNVOMove();"
                                                               onkeydown="checkTheDojoURL();
                                                                           if (event.keyCode == 9) {
                                                                               setFocusFromDojo('newDestination')
                                                                           }"/>
                                                        <input type="hidden" id="doorDestination_check" value="${QuoteValues.doorDestination}"/>
                                                        <div id="doorDestination_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                                initAutocompleteWithFormClear("doorDestination", "doorDestination_choices", "", "doorDestination_check",
                                                                        "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=10&isDojo=false", "setFocusFromDojo('zip')", "changeNVOMove()");
                                                        </script>
                                                        <div id="deliveryChargeCommentToolTipDiv" style="display: none;">
                                                            <span class="textlabelsBold" onmouseover="showToolTipByChargeId('delivery', 100, event)"  onmouseout="tooltip.hideComments();">
                                                                <img src="${path}/img/icons/view.gif" id="localDrayageCommentImg"/></span>
                                                        </div>
                                                        <html:checkbox property="destinationCheck" styleId="newDestination"
                                                                       name="transactionbean" onclick="newDestinationEQ()"/>New
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Door Dest Zip/City</td>
                                                    <td valign="top">
                                                        <html:text property="zip" styleId="zip"  styleClass="textlabelsBoldForTextBox"
                                                                   onkeydown="selectNVOmove();if(event.keyCode==9){setFocusFromDojo('agentNo')}" value="${QuoteValues.zip}" size="22" /><!---->
                                                        <input type="hidden" id="zip_check" value="${QuoteValues.zip}"/>
                                                        <div id="zip_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                                initAutocompleteWithFormClear("zip", "zip_choices", "doorOrigin", "zip_check",
                                                                        "${path}/actions/getZipCode.jsp?tabName=QUOTE&from=1", "updateDoorOrigin('doorOrigin')", "hideImsQuote()");
                                                        </script>
                                                        <img src="${path}/img/map.png" id="zipLookUp" align="middle" onmouseover="tooltip.show('<strong>Google Map Search</strong>', null, event);" onmouseout="tooltip.hide();"
                                                             onclick="getGoogleMap()" />
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Door Dest</td>
                                                    <td id="isTerminal1">
                                                        <input Class="textlabelsBoldForTextBox" name="doorOrigin" id="doorOrigin" value="${QuoteValues.doorOrigin}"
                                                               size="22" onblur="getTypeOfMove()" onkeydown="getTypeOfMove()"  />
                                                        <input type="hidden" id="doorOrigin_check" value="${QuoteValues.doorOrigin}"/>
                                                        <div id="doorOrigin_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                                initAutocompleteWithFormClear("doorOrigin", "doorOrigin_choices", "", "doorOrigin_check",
                                                                        "${path}/actions/getUnlocationCode.jsp?tabName=QUOTE&from=5&isDojo=false", "setFocusFromDojo('originZip')", "hideOrgZip();");
                                                        </script>
                                                        <c:choose>
                                                            <c:when test="${QuoteValues.onCarriage == 'on'}">
                                                                <input type="checkbox" name="onCarriage" id="onCarriage" title="On-Carriage" checked/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input type="checkbox" name="onCarriage" id="onCarriage" title="On-Carriage" />
                                                            </c:otherwise>
                                                        </c:choose>
                                                            <c:choose>
                                                                <c:when test="${importFlag}">
                                                                    <c:set var="emptyPickUp" value="${QuoteValues.destination_port}"></c:set>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:set var="emptyPickUp" value="${QuoteValues.origin_terminal}"></c:set>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        <c:choose>
                                                            <c:when test="${not empty emptyPickUp &&  enableIms != 'N' &&  enableIms != 'n'}">
                                                                <input type="button" id="imsQuoteImg" style="visibility: visible" onclick="getEmptyPickUp('${QuoteValues.quoteId}', '${QuoteValues.hazmat}', '${QuoteValues.fileNo}', '${emptyPickUp}')" class="buttonStyleNew" value="Inland"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input type="button" id="imsQuoteImg" style="visibility: hidden" class="buttonStyleNew" value="Inland"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${not empty QuoteValues.onCarriageRemarks and QuoteValues.onCarriage == 'on'}">
                                                            <span class="textlabelsBold" onmouseover="showToolTip('${QuoteValues.onCarriageRemarks}', '', 100, event)"  onmouseout="tooltip.hideComments();">
                                                                <img src="${path}/img/icons/view.gif" id="oncarriage-img"/>
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                    <td align="left" style="display:none;">
                                                        <span><html:checkbox property="originCheck" styleId="newOrigin"  name="transactionbean"
                                                                       onclick="newOriginEQ()"/>New</span>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Origin Zip/City</td>
                                                    <td valign="top">
                                                        <html:text property="zip" onmousedown="if(event.keyCode==9){tabMovErt()}" styleId="zip"  styleClass="textlabelsBoldForTextBox"
                                                                   onkeydown="selectNVOmove()" value="${QuoteValues.zip}" size="22" /><!---->
                                                        <input type="hidden" id="zip_check" value="${QuoteValues.zip}"/>
                                                        <div id="zip_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                                initAutocompleteWithFormClear("zip", "zip_choices", "doorOrigin", "zip_check",
                                                                        "${path}/actions/getZipCode.jsp?tabName=QUOTE&from=1", "updateDoorOrigin('doorOrigin')", "hideImsQuotes('${QuoteValues.quoteId}')");
                                                        </script>
                                                        <img src="${path}/img/map.png" id="zipLookUp" align="middle" onmouseover="tooltip.show('<strong>Google Map Search</strong>', null, event);" onmouseout="tooltip.hide();"
                                                             onclick="getGoogleMap()" />
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Door Org</td>
                                                    <td id="isTerminal1">
                                                        <input Class="textlabelsBoldForTextBoxDisabledLook" tabindex="-1" name="doorOrigin" readonly="true" id="doorOrigin" value="${QuoteValues.doorOrigin}"
                                                               size="22" onblur="getTypeOfMove()" onkeydown="getTypeOfMove()"  />
                                                        <input type="hidden" id="doorOrigin_check" value="${QuoteValues.doorOrigin}"/>
                                                        <div id="doorOrigin_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                                initAutocompleteWithFormClear("doorOrigin", "doorOrigin_choices", "", "doorOrigin_check",
                                                                        "${path}/actions/getUnlocationCode.jsp?tabName=QUOTE&from=5&isDojo=false", "setFocusFromDojo('originZip')", "hideOrgZip();");
                                                        </script>
                                                        <c:choose>
                                                            <c:when test="${not empty QuoteValues.doorOrigin &&  enableIms != 'N' &&  enableIms != 'n'}">
                                                                <input type="button" tabindex="-1" id="imsQuoteImg" style="visibility: visible" onclick="getEmptyPickUp('${QuoteValues.quoteId}', '${QuoteValues.hazmat}', '${QuoteValues.fileNo}', '${QuoteValues.origin_terminal}')" class="buttonStyleNew" value="Inland"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input type="button" tabindex="-1" id="imsQuoteImg" style="visibility: hidden" onclick="getEmptyPickUp('${QuoteValues.quoteId}', '${QuoteValues.hazmat}', '${QuoteValues.fileNo}', '${QuoteValues.origin_terminal}')" class="buttonStyleNew" value="Inland"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td align="left" style="display:none;">
                                                        <span><html:checkbox property="originCheck" styleId="newOrigin"  name="transactionbean"
                                                                       onclick="newOriginEQ()" tabindex="-1"/>New</span>
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Door at Dest</td>
                                                    <td>
                                                        <input class="textlabelsBoldForTextBox"  name="doorDestination" id="doorDestination" value="${QuoteValues.doorDestination}" size="22" onchange="changeNVOMove();" onkeydown="checkTheDojoURL();
                                                                    if (event.keyCode == 9) {
                                                                        setFocusFromDojo('agentNo')
                                                                    }"/>
                                                        <input type="hidden" id="doorDestination_check" value="${QuoteValues.doorDestination}"/>
                                                        <div id="doorDestination_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                                initAutocompleteWithFormClear("doorDestination", "doorDestination_choices", "", "doorDestination_check",
                                                                        "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=10&isDojo=false", "setFocusFromDojo('routedAgentCheck')", "changeNVOMove()");
                                                        </script>
                                                        <c:choose>
                                                            <c:when test="${QuoteValues.onCarriage == 'on'}">
                                                                <input type="checkbox" name="onCarriage" id="onCarriage" title="On-Carriage" checked/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input type="checkbox" name="onCarriage" id="onCarriage" title="On-Carriage" />
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <html:checkbox property="destinationCheck" styleId="newDestination"
                                                                       name="transactionbean" onclick="newDestinationEQ()" tabindex="-1"/>New
                                                        <c:if test="${not empty QuoteValues.deliveryChargeComments or not empty QuoteValues.onCarriageRemarks}">
                                                            <div id="deliveryChargeCommentToolTipDiv" style="display: inline-block;">
                                                                <c:choose>
                                                                    <c:when test="${QuoteValues.onCarriage == 'on'}">
                                                                        <span class="textlabelsBold" onmouseover="showToolTip('${QuoteValues.onCarriageRemarks}', '', 100, event)"  onmouseout="tooltip.hideComments();">
                                                                            <img src="${path}/img/icons/view.gif" id="oncarriage-img"/>
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="textlabelsBold" onmouseover="showToolTipByChargeId('delivery', 100, event)"  onmouseout="tooltip.hideComments();">
                                                                            <img src="${path}/img/icons/view.gif" id="localDrayageCommentImg"/>
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:otherwise>

                                        </c:choose>
                                        <tr class="textlabelsBold"> 
                                            <c:choose>
                                                <c:when test="${importFlag}">
                                                    <td align="right"><div id="inlandVal" >Delivery</div></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td align="right"><div id="inlandVal" ></div></td>
                                                    </c:otherwise>
                                                </c:choose>
                                            <td colspan="2">
                                                <span style="float:left">
                                                    <html:radio property="inland"  value="Y" styleId="y7"
                                                    onclick="getInland('${QuoteValues.quoteId}','${QuoteValues.fileNo}','<%=fileNo%>')" name="transactionbean" disabled="<%=check%>"/>Yes
                                                    <html:radio property="inland"  value="N" styleId="n7" onclick="deleteInland()"
                                                    name="transactionbean" disabled="<%=check%>"/>No
                                                </span><span style="float:left"><html:hidden  property="intermodelComments"/></span>
                                                <span id="override" style="display: none; float:left;line-height: 25px; margin: 0 0 0 10px">Override</span>
                                                <div id="localDrayageCommentToolTipDiv" style="display: none;">
                                                    <span class="textlabelsBold" onmouseover="showToolTipByChargeId('intermodel', 100, event)"  onmouseout="tooltip.hideComments();" style="color:black;margin: 6px 0 0 10px;float:left" >
                                                        <img src="${path}/img/icons/view.gif" id="localDrayageCommentImg"/></span>
                                                </div>
                                            </td>
                                            <html:hidden  property="deliveryChargeComments" styleId="deliveryChargeComments" value="${QuoteValues.deliveryChargeComments}"/>

                                        </tr>
                                    </table>
                                </td>

                                <td valign="top">
                                    <table width="100%"  class="tableBorderNew" cellpadding="1"
                                           style="border-left-width: 1px;border-left-color:grey;border-top-width: 0px;
                                           border-bottom-width: 0px;border-right-width: 0px;">
                                        <tr class="textlabelsBold">
                                            <td align="right">Default Agent&nbsp;</td>
                                            <td>
                                                <html:radio property="defaultAgent" styleId="defaultAgentY" onclick="fillDefaultAgent();" value="Y"
                                                            name="transactionbean"/>Yes
                                                <html:radio property="defaultAgent" styleId="defaultAgentN" onclick="clearValues();"
                                                            value="N" name="transactionbean"/>No
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                            <td align="right">Direct Consignment&nbsp;</td>
                              <td>
                                <html:radio property="directConsignmntCheck" styleId="directConsignmentY" onclick="directConsignmnt();"
                                            value="on" name="transactionbean" tabindex="-1"/>Yes
                                <html:radio property="directConsignmntCheck" styleId="directConsignmentN" onclick="directConsignmntNo();"
                                            value="off"  name="transactionbean" tabindex="-1"/>No
                               </td>
                                         </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Agent Name&nbsp;</td>
                                            <c:choose>
                                                <c:when test="${transactionbean.directConsignmntCheck=='on'}">
                                                    <td>
                                                        <html:text property="agent" styleId="agent" value="${QuoteValues.agent}" styleClass="textlabelsBoldForTextBox"
                                                                   size="22" />

                                                        <input name="agent_check" id="agent_check" type="hidden" value="${QuoteValues.agent}"/>
                                                        <div id="agent_choices" style="display: none;"  class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                                initAutocomplete("agent", "agent_choices", "agentNo", "agent_check",
                                                                        "${path}/actions/tradingPartner.jsp?tabName=QUOTE&from=3&importFlag=${importFlag}", "checkDisableForAgent('routedbymsg')");
                                                        </script>
                                                      </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>
                                                        <html:text property="agent" styleId="agent" value="${QuoteValues.agent}" styleClass="textlabelsBoldForTextBox"
                                                                   size="22"  onkeydown="setDojoPathForAgent()"  />
                                                        <input name="agent_check" id="agent_check" type="hidden" value="${QuoteValues.agent}"/>
                                                        <div id="agent_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                                initAutocomplete("agent", "agent_choices", "agentNo", "agent_check",
                                                                        "${path}/actions/tradingPartner.jsp?tabName=QUOTE&from=3", "checkDisableForAgent('routedbymsg')");
                                                        </script>
                                                   </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Agent Number&nbsp;</td>
                                            <td>
                                                <html:text property="agentNo" styleId="agentNo" value="${QuoteValues.agentNo}" tabindex="-1"
                                                           size="22" styleClass="textlabelsBoldForTextBoxDisabledLook"  readonly="true"  />
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">ERT&nbsp;</td>
                                            <td>
                                                <div id="routedAgentCheckDiv"
                                                     <c:choose>
                                                         <c:when test="${importFlag}">
                                                             class="textlabelsBold"
                                                         </c:when>
                                                         <c:otherwise>
                                                             class= "mandatory"
                                                         </c:otherwise>
                                                     </c:choose>
                                                     style="float: left">
                                                    <c:choose>
                                                        <c:when test="${transactionbean.directConsignmntCheck=='on'}">
                                                            <html:select property="routedAgentCheck" disabled="true" name="transactionbean" style="width:130px;"
                                                                         styleClass="dropdown_accounting" onkeydown="if(event.keyCode==9){tabgoodsDesc();}" onchange="setDefaultRouteAgent('${importFlag}')"
                                                                         styleId="routedAgentCheck">
                                                                <html:option value="">Select</html:option>
                                                                <html:option value="yes">Yes</html:option>
                                                                <html:option value="no">No</html:option>
                                                            </html:select>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:select property="routedAgentCheck" onkeydown="if(event.keyCode==9){tabgoodsDesc();}" disabled="false" name="transactionbean" style="width:130px;" styleClass="dropdown_accounting"
                                                                         onchange="setDefaultRouteAgent('${importFlag}')" styleId="routedAgentCheck">
                                                                <html:option value="">Select</html:option>
                                                                <html:option value="yes">Yes</html:option>
                                                                <html:option value="no">No</html:option>
                                                            </html:select>
                                                        </c:otherwise>

                                                    </c:choose>
                                                </div>
                                                <div id ="shortCutCharge">
                                                    <img src="${path}/img/icons/quickCharges.gif"
                                                         <c:choose>
                                                             <c:when test="${(QuoteValues.greendollarSignClickCount ne undefined) && (QuoteValues.greendollarSignClickCount eq 0 || QuoteValues.greendollarSignClickCount eq null || QuoteValues.greendollarSignClickCount eq '')}">
                                                                 onclick="shortCutCharges();"
                                                             </c:when>
                                                             <c:when test="${QuoteValues.greendollarSignClickCount==0 || QuoteValues.greendollarSignClickCount==null || QuoteValues.greendollarSignClickCount==''}">onclick="shortCutCharges();"</c:when>
                                                             <c:otherwise>onmouseover="tooltip.show(document.searchQuotationform.ertAdjustedValue.value)" onmouseout="tooltip.hide()"</c:otherwise>
                                                         </c:choose>
                                                         />

                                                </div>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Routed By Agent&nbsp;</td>
                                            <td  valign="top">
                                                <c:choose>
                                                    <c:when test="${transactionbean.directConsignmntCheck=='on'}">
                                                        <input type="text" Class="textlabelsBoldForTextBoxDisabledLook" name="routedbymsg" id="routedbymsg"  size="22"
                                                               value="${QuoteValues.routedbymsg}"  maxlength="15"  disabled="true"  />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="text" Class="textlabelsBoldForTextBox" name="routedbymsg" id="routedbymsg"  size="22"
                                                               value="${QuoteValues.routedbymsg}"  maxlength="15"  />
                                                    </c:otherwise>
                                                </c:choose>

                                                <input Class="textlabelsBoldForTextBox"  name="routedbymsg_check"
                                                       id="routedbymsg_check" type="hidden" value="${QuoteValues.routedbymsg}"/>
                                                <div id="Routed_Choices"  style="display: none;width: 10px;" class="autocomplete"></div>
                                                <c:choose>
                                                    <c:when test="${importFlag}">
                                                        <script type="text/javascript">
                                                                initAutocomplete("routedbymsg", "Routed_Choices", "routedNo", "",
                                                                        "${path}/actions/tradingPartner.jsp?tabName=QUOTE&from=2", "checkDisable()");
                                                        </script>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <script type="text/javascript">
                                                                initAutocomplete("routedbymsg", "Routed_Choices", "routedNo", "routedbymsg_check",
                                                                        "${path}/actions/tradingPartner.jsp?tabName=QUOTE&from=2", "checkDisable()");
                                                        </script>
                                                    </c:otherwise>
                                                </c:choose>
                                                <input name="routedNo" id="routedNo" type="text" style="display: none;"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="8">
                                    <table width="100%" height="100%" class="tableBorderNew"
                                           style="border-top-width:1px;border-top-color:grey;border-left-width: 0px;
                                           border-bottom-width:0px;border-right-width: 0px;">

                                        <tr class="textlabelsBold">
                                            <td>Region Remarks :</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td width="100%" valign="baseline">
                                                <c:if test="${not empty regionRemarks}">
                                                    <ul style='margin-left: 0px;'>
                                                        <li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'> ${str:splitter(regionRemarks,150,'<br>')} </li>
                                                    </ul>
                                                </c:if>
                                            </td>
                                        <tr>&nbsp;</tr>
                                            <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td width="100%" valign="baseline">
                                                <c:if test="${not empty orginRemarks}">
                                                    <ul style='margin-left: 0px;'>
                                                         <li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'> ${str:splitter(orginRemarks,150,'<br>')} </li>
                                                    </ul>
                                                </c:if>  
                                            </td>
                                            </tr>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="8">
                                    <table id="remarksTable" width="100%" height="100%" class="tableBorderNew"
                                           style="border-top-width: 1px;border-top-color:grey;border-left-width: 0px;
                                           border-bottom-width: 0px;border-right-width: 0px;">

                                        <tr class="textlabelsBold">
                                            <td valign="top">Remarks for <c:out value="${QuoteValues.destination_port}"/>:</td>
                                            <td width="100%">
                                                <%
                                                    if (request.getAttribute("QuoteValues") != null) {
                                                        Quotation quotation = (Quotation) request.getAttribute("QuoteValues");
                                                        String remarks = quotation.getRemarks() != null ? quotation.getRemarks() : "";
                                                        String[] remarksDup = remarks.split("\\n");
                                                        out.println("<ul style='margin-left: 0px;'>");
                                                        for (int j = 0; j < remarksDup.length; j++) {
                                                            out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + remarksDup[j] + "</li>");
                                                        }
                                                        out.println("</ul>");
                                                    }
                                                %>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold"><td>&nbsp;</td>
                                            <td colspan="2" valign="top">
                                                <%
                                                    if (request.getAttribute("QuoteValues") != null) {
                                                        Quotation quotation = (Quotation) request.getAttribute("QuoteValues");
                                                        String rateRemarks = quotation.getRatesRemarks() != null ? quotation.getRatesRemarks() : "";
                                                        String[] rateRemarksDup = rateRemarks.split("\\n");
                                                        out.println("<ul style='margin-left: 0px;'>");
                                                        for (int k = 0; k < rateRemarksDup.length; k++) {
                                                            out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + rateRemarksDup[k] + "</li>");
                                                        }
                                                        out.println("</ul>");
                                                    }
                                                %>
                                            </td></tr>
                                        <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td width="100%" valign="baseline">
                                                <%
                                                    if (request.getAttribute("QuoteValues") != null) {
                                                        Quotation quotation = (Quotation) request.getAttribute("QuoteValues");
                                                        String remarks = quotation.getFclTempRemarks() != null ? quotation.getFclTempRemarks() : "";
                                                        String[] remarksDup = remarks.split("\\n");
                                                        out.println("<ul style='margin-left: 0px;'>");
                                                        for (int j = 0; j < remarksDup.length; j++) {
                                                            out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + remarksDup[j] + "</li>");
                                                        }
                                                        out.println("</ul>");
                                                    }
                                                %>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td width="100%" valign="baseline">
                                                <%
                                                    if (request.getAttribute("QuoteValues") != null) {
                                                        Quotation quotation = (Quotation) request.getAttribute("QuoteValues");
                                                        String remarksGRI = quotation.getFclGRIRemarks() != null ? quotation.getFclGRIRemarks() : "";
                                                        String[] remarksGRIDup = remarksGRI.split("\\n");
                                                        out.println("<ul style='margin-left: 0px;'>");
                                                        for (int j = 0; j < remarksGRIDup.length; j++) {
                                                            out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + remarksGRIDup[j] + "</li>");
                                                        }
                                                        out.println("</ul>");
                                                    }
                                                %>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td width="100%" valign="baseline" style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>
                                                ${remarks}
                                            </td>
                                        </tr>
                                    </table></td>
                            </tr>
                        </table>
                    </td></tr>
                <tr><td>
                        <table border="0" width="100%" cellpadding="0"  cellspacing="0"  class="tableBorderNew">
                            <tr>
                                <td class="tableHeadingNew" width="50%" style="border-right:1px solid #dcdcdc">Carrier</td>
                                <td class="tableHeadingNew" width="50%">Other Charges</td>
                            </tr>
                            <tr>
                                <td width="50%">
                                    <table border="0" width="100%" cellpadding="0"  cellspacing="0" style="border-right:1px solid #dcdcdc">
                                        <tr><td colspan="10">
                                                <table><tr>
                                                        <td id="message"><font color="#006400" size="2" id="message"><b>
                                                                <c:if test="${QuoteValues.ratesNonRates=='R' && not empty fclRates}">
                                                                    ${ratesbasicsmessage}
                                                                </c:if>
                                                            </b></font></td>
                                                    </tr></table>
                                            </td></tr>
                                        <tr>
                                            <td><table cellpadding="3" width="100%">
                                                    <tr class="textlabelsBold"><td align="right">SSL Name&nbsp;</td></tr>
                                                    <tr class="textlabelsBold"><td align="right">Phone&nbsp;</td></tr>
                                                </table></td>
                                            <td>
                                                <table width="100%">
                                                    <tr class="textlabelsBold">
                                                    <tr class="textlabelsBold">
                                                        <td>
                                                            <input type="text" Class="textlabelsBoldForTextBox" name="sslDescription" id="sslDescription"  size="22" value="<%=sslName%>"  maxlength="50"  />
                                                            <input Class="textlabelsBoldForTextBox"  name="sslname_check" id="sslname_check"   type="hidden" style="display: none;" value="<%=sslName%>" />
                                                            <div id="sslname_choices" style="display: none" class="autocomplete"></div>
                                                            <script type="text/javascript">
                                                                    initAutocompleteWithFormClear("sslDescription", "sslname_choices", "sslcode", "sslname_check",
                                                                            "${path}/actions/tradingPartner.jsp?tabName=QUOTE&from=1", "focusSettingForSSl();", "onBlurForSSL();");
                                                            </script>
                                                        </td>
                                                    </tr>
                                        </tr>
                                        <tr class="textlabelsBold"><td> <html:text property="carrierPhone"  styleClass="textlabelsBoldForTextBox" styleId="carrierPhone" onblur="checkForNumberAndDecimal(this)"
                                                   value="${QuoteValues.carrierPhone}" size="22" maxlength="20" onkeyup="getIt(this)" ></html:text></td></tr>
                                        </table>
                                    </td>
                                    <td><table cellpadding="3" width="100%">
                                            <tr class="textlabelsBold"><td align="right">SSL Acct#&nbsp;
                                                </td></tr>
                                            <tr class="textlabelsBold"><td align="right">Fax&nbsp;</td></tr>
                                        </table></td>
                                    <td><table width="100%">
                                            <tr class="textlabelsBold"><td><input readonly="readonly" class="textlabelsBoldForTextBoxDisabledLook" name="sslcode" onclick="setBlur(this)" onmouseover="setBlur(this)"
                                                                                  id="sslcode" size="22" value="${QuoteValues.ssline}"  />
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold"><td><html:text property="carrierFax" styleId="carrierFax" onblur="checkForNumberAndDecimal(this)" maxlength="50"
                                                   value="${QuoteValues.carrierFax}" styleClass="textlabelsBoldForTextBox"
                                                   size="22" onkeyup="getIt(this)"></html:text></td>
                                            </tr>
                                        </table></td>
                                    <td><table cellpadding="3" width="100%">
                                            <tr class="textlabelsBold" valign="top"><td align="right">Cont Name&nbsp;</td></tr>
                                            <tr class="textlabelsBold" valign="top"><td align="right">Email&nbsp;</td></tr>

                                        </table></td>
                                    <td><table width="100%">
                                            <tr class="textlabelsBold" valign="top"><td class="textlabelsBold">
                                                <html:text property="carrierContact" styleId="carrierContact" maxlength="50"
                                                           value="${QuoteValues.carrierContact}" styleClass="textlabelsBoldForTextBox" size="22" ></html:text>
                                                <img src="${path}/img/icons/display.gif"  id="contactNameButtonSecond" onclick="getCarrierContactInfo()" />
                                                <!-- only for alignment purpose --><input type="checkbox" style="visibility: hidden;"/><font style="visibility: hidden;">&nbsp;New</font>
                                            </td></tr>
                                        <tr class="textlabelsBold" valign="top"><td>
                                                <html:text property="carrierEmail" maxlength="500" styleId="carrierEmail" onblur="emailValidate(this)"
                                                           value="${QuoteValues.carrierEmail}" styleClass="textlabelsBoldForTextBox" size="22" ></html:text></td></tr>
                                        </table></td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%">
                            <table  width="100%" style="margin-left: 0px;">
                                <tr ><td><table  width="100%"  border ="0" cellpadding="0" cellspacing="0"><tr>
                                            <c:if test="${importFlag eq false}">
                                                <td class="textlabelsBold"> Auto Deduct FF Commission
                                                    <html:radio property="deductFFcomm" value="Y" styleId="y5"
                                                    name="transactionbean" disabled="<%=check%>" onclick="getFFCommission()"/>Yes
                                                    <html:radio property="deductFFcomm" value="N" styleId="n5"
                                                    name="transactionbean" disabled="<%=check%>" onclick="deleteFFCommission()"/>No
                                                </td>
                                            </c:if>
                                            <td class="textlabelsBold"> Document Charge
                                                <html:radio property="docCharge" value="Y" styleId="docChargeY" name="transactionbean" onclick="getDocCharge()"/>Yes
                                                <html:radio property="docCharge" value="N" styleId="docChargeN" name="transactionbean" onclick="deleteDocCharge()"/>No
                                            </td>
                                            
                                            <c:if test="${importFlag eq false}">
                                            <td class="textlabelsBold"> Chassis
                                                <html:radio property="chassisCharge" value="Y" styleId="chassisChargeY" name="transactionbean"  disabled="<%=check%>" onclick="getChasisCharge('${QuoteValues.quoteId}')"/>Yes
                                                <html:radio property="chassisCharge" value="N" styleId="chassisChargeN" name="transactionbean"  disabled="<%=check%>" onclick="deleteChassis()"/>No
                                            </td>
                                            </c:if>
                                           
                                        </tr></table></td></tr>
                            <tr><td><table style="border-top:1px solid #dcdcdc;" width="100%"><tr class="textlabelsBold">
                                            <td>Insurance:</td>
                                            <td>
                                                <html:radio property="insurance" value="Y" styleId="y8"
                                                            onkeydown="setFR()" onclick="getInsurance('${QuoteValues.quoteId}')"
                                                name="transactionbean" disabled="<%=check%>">Yes</html:radio>
                                                <html:radio property="insurance"  value="N" styleId="n8" onclick="getinsurance1()"
                                                name="transactionbean" disabled="<%=check%>"/>No</td>
                                            <td valign="bottom" colspan="2" style="padding-left:20px;">Cost of Goods
                                                <fmt:formatNumber type="number"  var="costofgoods"   value="${QuoteValues.costofgoods}" pattern="########0" />
                                                <html:text property="costofgoods" styleClass="BackgrndColorForTextBox"
                                                           onkeydown="return false;" styleId="costofgoERT" readonly="true" onkeypress="return false;"
                                                           value="${costofgoods}" size="8"   tabindex="-1"/></td>
                                            <td class="textlabelsBold">Pier Pass:
                                                <html:radio property="pierPass"  styleId="pierPassY" value="Y"  name="transactionbean"  disabled="<%=check%>"  onclick="calculatePierPassCharge()"/>Yes
                                                <html:radio property="pierPass" styleId="pierPassN" value="N"  name="transactionbean"  disabled="<%=check%>" onclick="deletePierPassCharge()"/>No
                                            </td> 
                                                <td valign="bottom" style="visibility:hidden">Insurance Amt:
                                                <fmt:formatNumber type="number"  var="insuranceCharge"  value="${QuoteValues.insuranceCharge}" pattern="########0.00" />
                                                <html:text property="insuranceCharge"
                                                           styleId="insuranceCharge" value="${insuranceCharge}"
                                                           size="8" readonly="true" styleClass="textlabelsBoldForTextBox"
                                                               onchange="addInsuranceToBl()" tabindex="-1"/></td>
                                        </tr></table></td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td></tr>
<tr><td>
        <table border="0" width="100%"  cellpadding="0"  cellspacing="0"  class="tableBorderNew">
            <tr>
                <td>
                    <table border="0" width="100%"  cellpadding="0"  cellspacing="0"  class="tableBorderNew">
                        <tr>
                            <td valign="top">
                                <table class="tableBorderNew" height="100%"  border="0"
                                       style="border-left-width: 0px;border-right-width: 0px;border-bottom-width: 0px;"
                                       cellpadding="0" cellspacing="0" width="100%">
                                    <tr class="tableHeadingNew">
                                        <td width="50%"> Goods Description<font class="mandatoryStarColor"><%=star%></font></td>
                                        <td width="50%" style="border-left:#dcdcdc 1px solid;padding-left: 3px">
                                            <div style="float:left">Remarks  </div>
                                            <div style="float:right" align="right">
                                                Predefined Remarks<img src="${path}/img/icons/display.gif" value="Look Up" onclick="goToRemarksLookUp('${importFlag}')"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="50%">
                                            <html:textarea styleId="goodsDesc"  styleClass="textlabelsBoldForTextBox"
                                                           property="goodsdesc" value="${QuoteValues.goodsdesc}" cols="100"  rows="6"
                                                           style="text-transform: uppercase;width:100%"
                                                           onkeypress="return checkTextAreaLimit(this, 500)" >
                                            </html:textarea>
                                        </td>
                                        <td width="50%">
                                            <html:textarea styleId="commentRemark" onkeydown="if(event.keyCode==9){tabRemark1();}"  property="comment" cols="100"  rows="6"
                                                           styleClass="textlabelsBoldForTextBox"  value="${QuoteValues.comment1}"
                                                           style="text-transform: uppercase; width:100%"
                                                           onkeypress="return checkTextAreaLimit(this, 500)" >
                                            </html:textarea>
                                        </td>
                                    </tr>
                                </table>
                            </td>

                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td></tr>

<tr><td>
        <table width="100%" border="0"  cellpadding="0"  cellspacing="0"  class="tableBorderNew">
            <tr class="tableHeadingNew"><td colspan="1">Special Provision</td></tr>
            <tr>
                <td height="100%"><table width="100%" height="100%" class="tableBorderNew"
                                         style="border-right-width: 0px;border-right-color:grey;border-left-width: 0px;
                                         border-bottom-width: 0px;border-top-width: 0px;">
                        <tr>
                            <td class="textlabelsBold" align="top">Special Equipment :</td>
                            <td class="textlabelsBold"><html:radio property="specialequipment" value="Y" name="transactionbean" disabled="<%=check%>" onclick="addSpecialEquipment('${QuoteValues.quoteId}')"/>Yes
                                <html:radio property="specialequipment" value="N" name="transactionbean" disabled="<%=check%>" onclick="deleteSpecialEquipment()"/> No<br></td>
                            <td class="textlabelsBold"  style="" colspan="2" align="left">
                                <html:select  styleClass="dropdown_accounting"  property="specialEqpmt" styleId="specialEqpmt" value="">
                                    <html:optionsCollection name="spclList"/> </html:select>
                                    &nbsp;&nbsp;&nbsp;
                                <html:select  styleClass="dropdown_accounting"  property="specialEqpmtUnit" styleId="specialEqpmtUnit" value="">
                                    <html:option value="">Select Unit</html:option>
                                    <c:if test="${! empty specialEquipmentUnitList}">
                                        <html:optionsCollection name="specialEquipmentUnitList"/>
                                    </c:if>
                                </html:select>
                                &nbsp;&nbsp;&nbsp;
                                <input type="button" value="Add to Quote" id="addEquipment" onclick="addOrUpdateSpecialEquipment('${QuoteValues.quoteId}', 'add')" class="buttonStyleNew" />
                                &nbsp;&nbsp;&nbsp;
                                <input type="button" value="Update Standard" id="updateEquipment" onclick="addOrUpdateSpecialEquipment('${QuoteValues.quoteId}', 'update')" class="buttonStyleNew" />
                            </td>
                            <td>&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold">Is ${companyMnemonicCode} Filing AES :&nbsp;</td>
                            <td class="textlabelsBold"><html:radio property="aesFilling" name="QuoteValues" value="true">
                                    Yes
                                </html:radio>
                                <html:radio property="aesFilling" name="QuoteValues" value="false">
                                    No
                                </html:radio></td>
                            <td>&nbsp;</td>
                        </tr>
                    </table></td>
            </tr>
        </table>
    </td></tr>
<tr>
    <td align="center">
        <input type="button"  class="buttonStyleNew" id="charge" value="Input Rates Manually"
               style="width: 115px;"
               onClick="return popupQuoteCharge('windows', '${QuoteValues.quoteId}', '<%=fileNo%>', '${importFlag}')"/>
    </td>
</tr>
</table>
<br style="margin-top:5px"/>
<!-- main Table for rates -->
<html:hidden property="imsOrigin"/>
<html:hidden property="localdryage" value="${QuoteValues.localdryage}"/>
<html:hidden property="selectedUnits" value="${QuoteValues.selectedUnits}" styleId="selectedUnits"/>
<%@include  file="fragment/tableQuoteRates.jspf"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" >

    <tr>
        <td colspan="10">
            <table>
                <tr class="textlabelsBold">
                    <td>File No :<span class="fileNo"><%=fileNo%></span></td>
                    <td id="spotRateMsgDiv2" style="display:none">
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>
                                                <div id="spotRateMsgStatus2" class="red bold"/>
                                            </td>
                                        </tr>
                                    </table>
            </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
        <td colspan="10">
            <input type="button" id="cancel1" value="Go Back" onclick="compareWithOldArray('<%=view%>', '<%=finalized%>')" class="buttonStyleNew"/>
            <input type="button" id="save1" value="Save" onclick="quotupdation('${importFlag}', '')" class="buttonStyleNew"
                   onmouseover="tooltip.showTopText('<strong><%=mandatoryFieldForQuotes%></strong>', null, event);" onmouseout="tooltip.hide();"/>
            <c:if test="${! empty QuoteValues.fileNo}">
                <input type="button" id="conBL" value="ConvertToBooking" onclick="convertToBookings()" class="buttonStyleNew" style="width:110px" />
                <c:if test="${importFlag && isBlFile eq false}">
                    <input type="button" id="ConvertToArrivalNotice" value="Convert to Arrival Notice" onclick="convertToArrivalNotice('${QuoteValues.quoteId}', '${QuoteValues.quoteBy}', '${quoteDate}')" class="buttonStyleNew" style="width:130px" />
                </c:if>
                <input type="button" id="copy" value="Copy" onclick="copyNewQuote()" class="buttonStyleNew"/>
                <input type="button" id="print" value="Print/Fax/Email" onclick="quotationPrint('${QuoteValues.fileNo}', '<%=finalized%>')" class="buttonStyleNew" style="width:110px"/>
                <input type="button"  id="noteButtonDown" name="search" value="Note" class="${manualNotesCount}"
                      onclick="return GB_show('Notes', '${path}/notes.do?moduleId=' + '<%=NotesConstants.FILE%>&moduleRefId=' + '<%=quotdomain.getFileNo()%>', 300, 730);" />
                <c:choose>
                    <c:when test="${null!=TotalScan && TotalScan!='0'}">
                        <input id="scanButtonDown" class="buttonColor" type="button"
                               value="Scan/Attach" onClick="scan('<%=quotdomain.getFileNo()%>', '${importFlag}')"/>
                    </c:when>
                    <c:otherwise>
                        <input id="scanButtonDown" class="buttonStyleNew" type="button"
                               value="Scan/Attach" onClick="scan('<%=quotdomain.getFileNo()%>', '${importFlag}')"/>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <input type="button" id="Options" value="Options" onclick="quoteOptions()" class="buttonStyleNew" />
            <input type="button" id="HazmatButtonDown"  class="buttonStyleNew" value="Hazmat" onclick="getHazmat('<%=fileNo%>')" />
            <c:if test="${! empty QuoteValues.fileNo}">
                <input type="button" id="arRedInvoiceDown" value="AR Invoice" class="buttonStyleNew"
                       onclick="return GB_show('AR Invoice', '${path}/arRedInvoice.do?action=listArInvoice&fileNo=${QuoteValues.fileNo}&screenName=QUOTE&fileType=${QuoteValues.fileType}', 550, 1100)"/>
            </c:if>
        </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
</table>
</body>
<script>setDefaultAgent("${QuoteValues.quoteId}");</script>
<script>load('${buttonValue}', '${QuoteValues.quoteNo}', '<%=fileNo%>', '<%=userEmail%>', '<%=rateListSize%>', '${rateFlag}');</script>
<%if ((view != null && view.equals("3"))) {%>
<script>
        window.parent.disableFieldsWhileLocking(document.getElementById("editQuote"));
        disableImages();
</script>
<%}%>
<c:if test="${param.accessMode == '0'}">
    <script>
            window.parent.disableFieldsWhileLocking(document.getElementById("editQuote"));
            disableImages();
    </script>
</c:if>
<%if (finalized != null && finalized.equals("on")) {%>
<script>
        window.parent.makeFormBorderless(document.getElementById("editQuote"));
        makeFormBorderless1(document.getElementById("editQuote"));
</script>
<%} else {%>
<script>disableAutoFF();</script>
<%}%>
<script>setFocus('${focusValue}', '${buttonValue}');</script>
<script>tabMoveAfterDeleteRates('${importFlag}');</script>

</html:form>

<script type="text/javascript">
        function printDocument() {
            GB_show('Quotation Report', '${path}/printConfig.do?screenName=Quotation&quotationNo=${QuoteValues.quoteId}&printRemarks=${QuoteValues.remarksFlag}&destination=${QuoteValues.destination_port}&subject=FCL Quote ${QuoteValues.quoteNo}&fileNo=' + '<%=fileNo%>', 400, 600);
        }
        function populateAgentDojo1(data) {
            var defaultAgentCheck = "<%=defaultAgent%>";

            if (null !== data && data.accountName != undefined && data.accountName != "" && data.accountName != null && defaultAgentCheck == 'ECI') {
                document.getElementById("agent").value = data.accountName;
            } else {
                document.getElementById("agent").value = "";
            }
            if (null !== data && data.accountno != undefined && data.accountno != "" && data.accountno != null && defaultAgentCheck == 'ECI') {
                document.getElementById("agentNo").value = data.accountno;
            } else {
                document.getElementById("agentNo").value = "";
            }
            if (document.searchQuotationform.routedAgentCheck.value == "yes") {
                document.searchQuotationform.routedbymsg.value = "";
            }
            var city = document.searchQuotationform.portofDischarge.value;
            var index = city.indexOf('/');
            var cityName = city.substring(0, index);
            cityNameForRemarks1 = cityName;
            unlocationCode = city.substring(index + 1, city.length);
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                    methodName: "getSpecialRemarks",
                    param1: cityName
                },
                success: function (data) {
                    showSpecialRemarks(data);
                }
            });
        }

</script>
<c:if test="${null!=result}">
    <c:choose>
        <c:when test="${null!=result && result=='resultFalse'}">
            <script>cancel1('<%=view%>', '<%=finalized%>');</script>
        </c:when>
        <c:otherwise>
            <script>goBackCall('<%=view%>', '<%=finalized%>', '${buttonValue}');</script>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${empty quoteOldNotSet}">
    <script>
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "updateSessionForComparison",
                    param1: "<%=fileNo%>",
                    request: "true"
                }
            });
    </script>
</c:if>
<c:if test="${!empty inputRatesManually && inputRatesManually == 'inland'}">
    <script>addInland('${QuoteValues.quoteId}');</script>
</c:if>
<c:if test="${!empty inputRatesManually && inputRatesManually == 'manualRates'}">
    <script>openInputRatesManually('${QuoteValues.quoteId}', '<%=fileNo%>', '${importFlag}');</script>
</c:if>
<style>
    #localDrayageCommentDiv{
        position: fixed;
        _position: absolute;
        z-index: 99;
        border-style:solid solid solid solid;
        background-color: white;
        _height: expression(document.body.offsetHeight + "px");
    }
    #deliveryChargeCommentDiv {
        position: fixed;
        _position: absolute;
        z-index: 99;
        border-style:solid solid solid solid;
        background-color: white;
        _height: expression(document.body.offsetHeight + "px");
    }
    #outOfGaugeCommentDiv {
        position: fixed;
        z-index: 99;
        border-style:solid solid solid solid;
        width: 310px !important;
        background-color: white;
        height: 300px;
    }
    #originAndDestinationDiv {
        position: fixed;
        _position: absolute;
        z-index: 99;
        border-style:solid;
        border-width:2px;
        border-color:#808080;
        padding:0px 0px 0px 0px;
        background-color: #FFFFFF;
        left:10px;
        right:5px;
        top:0;
        margin:0 auto;
    }
</style>
<script type="text/javascript">
        changeSelectBoxOnViewMode();
        document.getElementById('save').focus();
</script>
<script type="text/javascript" src="<%=path%>/js/fcl/clientSearch.js"></script>
</html>
