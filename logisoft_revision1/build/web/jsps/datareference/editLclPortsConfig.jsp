<%@page import="com.gp.cong.common.CommonUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
         java.util.List,java.util.HashSet,java.util.ArrayList,com.gp.cong.logisoft.domain.RefTerminalTemp,com.gp.cong.logisoft.domain.User,java.util.Set,java.util.Iterator,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.Consignee,com.gp.cong.logisoft.domain.LCLPortConfiguration,com.gp.cong.logisoft.domain.AgencyInfo,com.gp.cong.logisoft.domain.Ports"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib prefix="cong" tagdir="/WEB-INF/tags/cong"%>
<%@include file="/taglib.jsp" %>
<jsp:useBean id="lclPortRate" class="com.gp.cong.logisoft.struts.form.EditLclPortsConfigForm" scope="request"></jsp:useBean>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%    String path = request.getContextPath();
    RefTerminalTemp terminalobj = null;
    String terminalName = "";
    String terminalNo = "";
    String linemanager = "";
    String drAbbr = "";
    String transhipment = "";
    String ftfFee = "";
    List agencyInfoListForLCL = null;
    String modify = "";
    String defaultDischarge = "";
    LCLPortConfiguration lclPortConfigurationObj = null;
    LCLPortConfiguration lclPortConfigurationDefaultRate = new LCLPortConfiguration();
    AgencyInfo agencyDefaultObj = new AgencyInfo();
    User userObj = null;
    String lane = "";
    String portName = "";
    String dafaultRoute = "";
    String lclEnglish = "";
    String lclSpanish = "";
    String lclIntrmRemarks = "";
    String lclFrmRemarks = "";
//changed hyd.
    String defaultRate = "";
    String autoCalLclLoad = "";
    String lclOceanbl = "";
    String calLclFaeUnitVoyage = "";
    String spanishDescOnBl = "";
    String printOnSailingSch = "";
    String includeLclDocChargesBl = "";
    String persEffectBl = "";
    String onCarriage = "";
    String insChargesLclBl = "";
    String protectDefaultRoute = "";
    String collectChargeOnLclBls = "";
    String blNumbering = "";
    String asetup = "";
    String asetupAcct = "";
    String asetupAcctName = "";
    String acAccountPickup = "";
    String acAccountPickupName = "";
    String domestic = "";
    String printOFdollars = "";
    boolean printInvoice = false;
    boolean lockPort = false;
    String lclBrand = "";
    String hazAllowed = "";
    String ftfWeight = "";
    String ftfMeasure = "";
    String ftfMinimum = "";
    lclPortConfigurationDefaultRate.setSrvcOcean("Y");
    request.setAttribute("lclServiceList", new DBUtil().getSelectBoxList(new Integer(73), "Select Port Service"));
    //request.setAttribute("lclDefaultRateList", new DBUtil().getSelectBoxList(new Integer(75), "Select"));
    if (session.getAttribute("agencyInfoListForLCL") != null) {
        agencyInfoListForLCL = (List) session.getAttribute("agencyInfoListForLCL");
        for (int i = 0; i < agencyInfoListForLCL.size(); i++) {
            AgencyInfo agencyInfo = (AgencyInfo) agencyInfoListForLCL.get(i);
            agencyDefaultObj.setDefaultValue(agencyInfo.getDefaultValue());
        }
        session.setAttribute("agencyInfoList", agencyInfoListForLCL);
    } else {
        session.removeAttribute("agencyInfoList");
    }
    if (session.getAttribute("lclPortConfigurationObj") != null) {
        lclPortConfigurationObj = (LCLPortConfiguration) session.getAttribute("lclPortConfigurationObj");
        if (lclPortConfigurationObj.getLaneField() != null) {
            lane = lclPortConfigurationObj.getLaneField();
        }
        if (lclPortConfigurationObj.getAltPortName() != null) {
            portName = lclPortConfigurationObj.getAltPortName();
        }
        if (lclPortConfigurationObj.getRoutingInstr() != null) {
            dafaultRoute = lclPortConfigurationObj.getRoutingInstr();
        }
        if (lclPortConfigurationObj.getLclSplRemarksInEnglish() != null) {
            lclEnglish = lclPortConfigurationObj.getLclSplRemarksInEnglish();
        }
        if (lclPortConfigurationObj.getLclSplRemarksInSpanish() != null) {
            lclSpanish = lclPortConfigurationObj.getLclSplRemarksInSpanish();
        }
        if (lclPortConfigurationObj.getIntrmRemarks() != null) {
            lclIntrmRemarks = lclPortConfigurationObj.getIntrmRemarks();
        }
        if (lclPortConfigurationObj.getFrmRemarks() != null) {
            lclFrmRemarks = lclPortConfigurationObj.getFrmRemarks();
        }
        if (lclPortConfigurationObj.getLineManager() != null) {
            linemanager = lclPortConfigurationObj.getLineManager();
        }
        if (lclPortConfigurationObj.getTranshipment() != null) {
            transhipment = lclPortConfigurationObj.getTranshipment();
        }
        if (lclPortConfigurationObj.getDefaultPortOfDischarge() != null) {
            defaultDischarge = lclPortConfigurationObj.getDefaultPortOfDischarge();
        }
        terminalobj = lclPortConfigurationObj.getTrmnum();
        if (terminalobj != null) {
            terminalNo = terminalobj.getTrmnum();
            terminalName = terminalobj.getTerminalLocation();
        }
        /*userObj=lclPortConfigurationObj.getLineManager();
         if(userObj != null) {
         linemanager=userObj.getFirstName()+" "+userObj.getLastName();
         }*/
        if (lclPortConfigurationObj.getDrAbbr() != null) {
            drAbbr = lclPortConfigurationObj.getDrAbbr().toString();
        }
        /*if(lclPortConfigurationObj.getTranshipment()!=null ) {
         transhipment=lclPortConfigurationObj.getTranshipment().getShedulenumber()+"-"+lclPortConfigurationObj.getTranshipment().getPortname();
         }
         if(lclPortConfigurationObj.getDefaultPortOfDischarge()!=null ) {
         defaultDischarge=lclPortConfigurationObj.getDefaultPortOfDischarge().getShedulenumber()+"-"+lclPortConfigurationObj.getDefaultPortOfDischarge().getPortname();
         }*/
        if (lclPortConfigurationObj.getFtfFee() != null) {
            ftfFee = lclPortConfigurationObj.getFtfFee().toString();
        }
        if (lclPortConfigurationObj.getAsetup() != null) {
            asetup = lclPortConfigurationObj.getAsetup().toString();
        }
        if (lclPortConfigurationObj.getAsetupAcct() != null) {
            asetupAcct = lclPortConfigurationObj.getAsetupAcct().toString();
        }
        if (lclPortConfigurationObj.getAsetupAcct() != "") {
            String acctName = new TradingPartnerDAO().getAccountName(lclPortConfigurationObj.getAsetupAcct());
            lclPortConfigurationObj.setAsetupAcctName(acctName);
        }
        if (lclPortConfigurationObj.getAsetupAcctName() != null) {
            asetupAcctName = lclPortConfigurationObj.getAsetupAcctName().toString();
        }
        if (lclPortConfigurationObj.getAcAccountPickup() != null) {
            acAccountPickup = lclPortConfigurationObj.getAcAccountPickup().toString();
        }
        if (lclPortConfigurationObj.getAcAccountPickup() != "") {
            String acctName = new TradingPartnerDAO().getAccountName(lclPortConfigurationObj.getAcAccountPickup());
            lclPortConfigurationObj.setAcAccountPickupName(acctName);
        }
        if (lclPortConfigurationObj.getAcAccountPickupName() != null) {
            acAccountPickupName = lclPortConfigurationObj.getAcAccountPickupName().toString();
        }
        if (lclPortConfigurationObj.getDomestic() != null) {
            domestic = lclPortConfigurationObj.getDomestic().toString();
        }
        if (lclPortConfigurationObj.getPrintOFdollars() != null) {
            printOFdollars = lclPortConfigurationObj.getPrintOFdollars().toString();
        }
        if (lclPortConfigurationObj.isPrintInvoiceValue()) {
            printInvoice = lclPortConfigurationObj.isPrintInvoiceValue();
        }
        if (lclPortConfigurationObj.isLockport()) {
            lockPort = lclPortConfigurationObj.isLockport();

        }
        if (lclPortConfigurationObj.getHazAllowed() != null) {
            hazAllowed = lclPortConfigurationObj.getHazAllowed().toString();
        }
        if (lclPortConfigurationObj.getFtfWeight() != null) {
            ftfWeight = lclPortConfigurationObj.getFtfWeight().toString();
        }
        if (lclPortConfigurationObj.getFtfMeasure() != null) {
            ftfMeasure = lclPortConfigurationObj.getFtfMeasure().toString();
        }
        if (lclPortConfigurationObj.getFtfMinimum() != null) {
            ftfMinimum = lclPortConfigurationObj.getFtfMinimum().toString();
        }
// code for check box
        defaultRate = lclPortConfigurationObj.getDefaultRate();
        if (defaultRate != null && defaultRate.equals("M")) {
            lclPortRate.setDefaultRate("on");
        } else {
            lclPortRate.setDefaultRate("off");
        }
        autoCalLclLoad = lclPortConfigurationObj.getAutoCalLclLoad();
        if (autoCalLclLoad != null && autoCalLclLoad.equals("Y")) {
            lclPortRate.setAutoCalLclLoad("on");
        } else {
            lclPortRate.setAutoCalLclLoad("off");
        }
        lclOceanbl = lclPortConfigurationObj.getLclOceanbl();
        if (lclOceanbl != null && lclOceanbl.equals("Y")) {
            lclPortRate.setLclOceanbl("on");
        } else {
            lclPortRate.setLclOceanbl("off");
        }
        calLclFaeUnitVoyage = lclPortConfigurationObj.getCalLclFaeUnitVoyage();
        if (calLclFaeUnitVoyage != null && calLclFaeUnitVoyage.equals("V")) {
            lclPortRate.setCalLclFaeUnitVoyage("on");
        } else {
            lclPortRate.setCalLclFaeUnitVoyage("off");
        }
        spanishDescOnBl = lclPortConfigurationObj.getSpanishDescOnBl();
        if (spanishDescOnBl != null && spanishDescOnBl.equals("Y")) {
            lclPortRate.setSpanishDescOnBl("on");
        } else {
            lclPortRate.setSpanishDescOnBl("off");
        }
        printOnSailingSch = lclPortConfigurationObj.getPrintOnSailingSch();
        if (printOnSailingSch != null && printOnSailingSch.equals("Y")) {
            lclPortRate.setPrintOnSailingSch("on");
        } else {
            lclPortRate.setPrintOnSailingSch("off");
        }
        includeLclDocChargesBl = lclPortConfigurationObj.getIncludeLclDocChargesBl();
        if (includeLclDocChargesBl != null && includeLclDocChargesBl.equals("Y")) {
            lclPortRate.setIncludeLclDocChargesBl("on");
        } else {
            lclPortRate.setIncludeLclDocChargesBl("off");
        }
        persEffectBl = lclPortConfigurationObj.getPersEffectBl();
        if (persEffectBl != null && persEffectBl.equals("Y")) {
            lclPortRate.setPersEffectBl("on");
        } else {
            lclPortRate.setPersEffectBl("off");
        }
        onCarriage = lclPortConfigurationObj.getOnCarriage();
        if (onCarriage != null && onCarriage.equals("O")) {
            lclPortRate.setOnCarriage("on");
        } else {
            lclPortRate.setOnCarriage("off");
        }
        insChargesLclBl = lclPortConfigurationObj.getInsChargesLclBl();
        if (insChargesLclBl != null && insChargesLclBl.equals("Y")) {
            lclPortRate.setInsChargesLclBl("on");
        } else {
            lclPortRate.setInsChargesLclBl("off");
        }
        protectDefaultRoute = lclPortConfigurationObj.getProtectDefaultRoute();
        if (protectDefaultRoute != null && protectDefaultRoute.equals("Y")) {
            lclPortRate.setProtectDefaultRoute("on");
        } else {
            lclPortRate.setProtectDefaultRoute("off");
        }
        blNumbering = lclPortConfigurationObj.getBlNumbering();
        if (blNumbering != null && blNumbering.equals("Y")) {
            lclPortRate.setBlNumbering("on");
        } else {
            lclPortRate.setBlNumbering("off");
        }
        lclPortRate.setOriginalsRequired(lclPortConfigurationObj.isOriginalsRequired());
        lclPortRate.setExpressRelease(lclPortConfigurationObj.isExpressRelease());
        lclPortRate.setDoNotExpressRelease(lclPortConfigurationObj.isDoNotExpressRelease());
        lclPortRate.setMemoHouseBillofLading(lclPortConfigurationObj.isMemoHouseBillofLading());
        lclPortRate.setBillCollectsFdAgent(lclPortConfigurationObj.isBillCollectsFdAgent());
        lclPortRate.setOriginalsReleasedAtDestination(lclPortConfigurationObj.isOriginalsReleasedAtDestination());
        lclPortRate.setForceAgentReleasedDrLoading(lclPortConfigurationObj.isForceAgentReleasedDrLoading());
        lclPortRate.setPrintImpOnMetric(lclPortConfigurationObj.getPrintImpOnMetric());
        collectChargeOnLclBls = lclPortConfigurationObj.getCollectChargeOnLclBls();
        if (collectChargeOnLclBls != null && collectChargeOnLclBls.equals("Y")) {
            lclPortRate.setCollectChargeOnLclBls("on");
        } else {
            lclPortRate.setCollectChargeOnLclBls("off");
        }
        lclPortConfigurationDefaultRate.setSrvcOcean(lclPortConfigurationObj.getSrvcOcean());
    }
    request.setAttribute("agencyDefaultObj", agencyDefaultObj);
    request.setAttribute("lclPortConfigurationDefaultRate", lclPortConfigurationDefaultRate);
    modify = (String) session.getAttribute("modifyforports");
    if (session.getAttribute("view") != null) {
        modify = (String) session.getAttribute("view");
    }
    session.setAttribute("agency", "edit");
    session.setAttribute("assoc", "edit");
%>
<head>
    <title>LCL Ports Configuration</title>
    <%@include file="../includes/baseResources.jsp" %>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
    <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js" ></script>
    <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
    <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/autocomplete.js"></script>
    <script type="text/javascript" src="<%=path%>/js/common.js"></script>
    <script language="javascript" type="text/javascript">
        function editLclPortsConfiguration() {
            window.location.href = "<%=path%>/jsps/datareference/editLclPortsConfig.jsp";
        }
        function submit1() {
            document.editLclPortsConfig.buttonValue.value = "terminalSelected";
            document.editLclPortsConfig.submit();
        }
        function save() {
            document.editLclPortsConfig.submit();
        }
        function toUppercase(obj) {
            obj.value = obj.value.toUpperCase();
        }
        var newwindow = '';
        function openAgencyInfo() {
            if (!newwindow.closed && newwindow.location) {
                newwindow.location.href = "<%=path%>/jsps/datareference/agencyInfo.jsp";
            } else {
                newwindow = window.open("<%=path%>/jsps/datareference/agencyInfo.jsp", "", "width=1000,height=450");
                if (!newwindow.opener) {
                    newwindow.opener = self;
                }
            }
            if (window.focus) {
                newwindow.focus()
            }
            return false;
        }
        var mywindow = '';
        function openassociatedport() {
            if (!mywindow.closed && mywindow.location) {
                mywindow.location.href = "<%=path%>/jsps/datareference/AssociatedPort.jsp";
            } else {
                mywindow = window.open("<%=path%>/jsps/datareference/AssociatedPort.jsp", "", "width=650,height=450");
                if (!mywindow.opener)
                    mywindow.opener = self;
            }
            if (window.focus) {
                mywindow.focus()
            }
            return false;
        }
        function disabled(val1) {
            if (val1 == 0 || val1 == 3) {
                var imgs = document.getElementsByTagName('img');
                for (var k = 0; k < imgs.length; k++) {
                    if (imgs[k].id != "assocport" && imgs[k].id != "note") {
                        imgs[k].style.visibility = 'hidden';
                    }
                }
                var input = document.getElementsByTagName("input");
                for (i = 0; i < input.length; i++) {
                    if (input[i].id != "buttonValue" && input[i].name != "terminalName") {
                        input[i].readOnly = true;
                        input[i].style.color = "blue";
                    }
                }
                var textarea = document.getElementsByTagName("textarea");
                for (i = 0; i < textarea.length; i++) {
                    textarea[i].readOnly = true;
                    textarea[i].style.color = "blue";
                }
                var select = document.getElementsByTagName("select");
                for (i = 0; i < select.length; i++) {
                    select[i].disabled = true;
                    select[i].style.backgroundColor = "blue";
                }
            }
        }
        function confirmnote() {
            document.editLclPortsConfig.buttonValue.value = "note";
            document.editLclPortsConfig.submit();
        }
        function limitText(limitField, limitCount, limitNum) {
            limitField.value = limitField.value.toUpperCase();
            if (limitField.value.length > limitNum) {
                limitField.value = limitField.value.substring(0, limitNum);
            } else {
                limitCount.value = limitNum - limitField.value.length;
            }
        }
        function drchkall() {
            if (document.editLclPortsConfig.defaultRate.checked) {
                document.editLclPortsConfig.defaultRate.value = "M";
                document.editLclPortsConfig.defaultRate.focus();
                return false;
            }
        }
        function autochkall() {
            if (document.editLclPortsConfig.autoCalLclLoad.checked) {
                document.editLclPortsConfig.autoCalLclLoad.value = "Y";
                document.editLclPortsConfig.autoCalLclLoad.focus();
                return false;
            }
        }
        function oceanchkall() {
            if (document.editLclPortsConfig.lclOceanbl.checked) {
                document.editLclPortsConfig.lclOceanbl.value = "V";
                document.editLclPortsConfig.lclOceanbl.focus();
                return false;
            }
        }
        function spanchkall() {
            if (document.editLclPortsConfig.spanishDescOnBl.checked) {
                document.editLclPortsConfig.spanishDescOnBl.value = "Y";
                document.editLclPortsConfig.spanishDescOnBl.focus();
                return false;
            }
        }
        function printchkall() {
            if (document.editLclPortsConfig.printOnSailingSch.checked) {
                document.editLclPortsConfig.printOnSailingSch.value = "Y";
                document.editLclPortsConfig.printOnSailingSch.focus();
                return false;
            }
        }
        function lclchkall() {
            if (document.editLclPortsConfig.includeLclDocChargesBl.checked) {
                document.editLclPortsConfig.includeLclDocChargesBl.value = "Y";
                document.editLclPortsConfig.includeLclDocChargesBl.focus();
                return false;
            }
        }
        function perschkall() {
            if (document.editLclPortsConfig.persEffectBl.checked) {
                document.editLclPortsConfig.persEffectBl.value = "Y";
                document.editLclPortsConfig.persEffectBl.focus();
                return false;
            }
        }
        function onchkall() {
            if (document.editLclPortsConfig.onCarriage.checked) {
                document.editLclPortsConfig.onCarriage.value = "O";
                document.editLclPortsConfig.onCarriage.focus();
                return false;
            }
        }
        function inschkall() {
            if (document.editLclPortsConfig.insChargesLclBl.checked) {
                document.editLclPortsConfig.insChargesLclBl.value = "Y";
                document.editLclPortsConfig.insChargesLclBl.focus();
                return false;
            }
        }
        function calchkall() {
            if (document.editLclPortsConfig.calLclFaeUnitVoyage.checked) {
                document.editLclPortsConfig.calLclFaeUnitVoyage.value = "V";
                document.editLclPortsConfig.calLclFaeUnitVoyage.focus();
                return false;
            }
        }
        function prochkall() {
            if (document.editLclPortsConfig.protectDefaultRoute.checked) {
                document.editLclPortsConfig.protectDefaultRoute.value = "Y";
                document.editLclPortsConfig.protectDefaultRoute.focus();
                return false;
            }
        }
        function collchkall() {
            if (document.editLclPortsConfig.collectChargeOnLclBls.checked) {
                document.editLclPortsConfig.collectChargeOnLclBls.value = "Y";
                document.editLclPortsConfig.collectChargeOnLclBls.focus();
                return false;
            }
        }
        function blnumberformat() {
            if (document.editLclPortsConfig.blNumbering.checked) {
                document.editLclPortsConfig.blNumbering.value = "Y";
                document.editLclPortsConfig.blNumbering.focus();
                return false;
            }
        }
        function popup1(mylink, windowname) {
            if (!window.focus)
                return true;
            var href;
            if (typeof (mylink) == 'string')
                href = mylink;
            else
                href = mylink.href;
            mywindow = window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
            mywindow.moveTo(200, 180);

            document.editLclPortsConfig.submit();
            return false;
        }

        function fillAccountNo1() {
            var val = document.getElementById('asetupAcct').value;
            var accountNo = val.substring(0, val.indexOf(":-"));
            var accountName = val.substring(val.indexOf("-") + 1, val.lastIndexOf("-"));
            document.getElementById('asetupAcct').value = accountNo;
            document.getElementById('asetupAcctName').value = accountName;
            if (val.endsWith("DISABLED")) {
                var data = val.substring(0, val.indexOf(":-"));
                disableAgent(data);
            }
        }

        function fillAccountNo2() {
            var val = document.getElementById('acAccountPickup').value;
            var accountNo = val.substring(0, val.indexOf(":-"));
            var accountName = val.substring(val.indexOf("-") + 1, val.lastIndexOf("-"));
            document.getElementById('acAccountPickup').value = accountNo;
            document.getElementById('acAccountPickupName').value = accountName;
            if (val.endsWith("DISABLED")) {
                var data = val.substring(0, val.indexOf(":-"));
                disableAgent(data);
            }
        }

        function disableAgent(data1) {
            if (data1 !== null && data1 !== "") {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "checkForDisable",
                        param1: data1
                    },
                    success: function (data) {
                        if (jQuery.trim(data) !== "") {
                            alertNew(data);
                            document.getElementById("agentAcountNo").value = "";
                            document.getElementById("agentName").value = "";
                            document.getElementById('consigneeAcctNo').value = "";
                            document.getElementById('consigneeName').value = "";
                        } else {
                            document.getElementById("agentAcountNo").value = data1;
                        }
                    }
                });
            }
        }

        function clearAcct() {
            var acct = document.getElementById("asetupAcct").value;
            if (acct == '') {
                document.getElementById("asetupAcctName").value = "";
            }
        }
        function cleardefaultDischarge() {
            var acct = document.getElementById("defaultPortOfDischarge").value;
            if (acct == '') {
                document.getElementById("defaultPortOfDischarge").value = "";
            }
        }

        function clearPickupAcct() {
            var acct = document.getElementById("acAccountPickup").value;
            if (acct == '') {
                document.getElementById("acAccountPickupName").value = "";
            }
        }

    </script>
    <%@include file="../includes/resources.jsp" %>

    <style>
        div.autocomplete ul{
            width: 400px;
        }
    </style>
</head>
<html>
    <body  class="whitebackgrnd" onload="disabled('<%=modify%>')" onkeydown="preventBack()">
        <html:form action="/editLclPortsConfig" name="editLclPortsConfig" styleId="editLclPortsConfig" type="com.gp.cong.logisoft.struts.form.EditLclPortsConfigForm" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td><bean:message key="form.LCLPortsConfiguration.LCLPortsConfiguration1" /></td>
                    <td class="textlabelsBold" align="right">
                        <input type="button" class="buttonStyleNew" id="asscoc" value="Assoc Port"  onclick="return GB_show('Assoc Port', '<%=path%>/jsps/datareference/AssociatedPort.jsp?relay=' + 'add', 350, 650)"/>
                        <input type="button" class="buttonStyleNew" id="note" value="Note"  onclick="confirmnote()" disabled="true"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" width="100%">
                        <table width="100%" border="0" cellpadding="2" cellspacing="0">
                            <tr>
                                <td class="textlabelsBold">Terminal No<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=' + 'editlcl', 'windows')"></td>
                                <td class="textlabelsBold">Terminal Location</td>
                                <td class="textlabelsBold">Line Manager
                                    <%--<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/UserPopUp.jsp?button='+'editlcl','windows')">--%>
                                </td>
                                <td class="textlabelsBold">Lane</td>
                            </tr>
                            <tr>
                                <td><html:text property="terminalNo" styleId="terminalNumber" styleClass="textlabelsBoldForTextBox" value="<%=terminalNo%>" readonly="true" style="width:120px"/></td>
                                <td><html:text property="terminalName"  value="<%=terminalName%>" maxlength="100" readonly="true" styleClass="areahighlightgrey" style="width:120px"/></td>
                                <td><html:text property="lineManager" styleClass="textlabelsBoldForTextBox"  value="<%=linemanager%>"    style="width:120px" /></td>
                                <td><html:text property="laneField" styleClass="textlabelsBoldForTextBox"  value="<%=lane%>" maxlength="6"  onkeyup="toUppercase(this)" style="width:120px"/></td>
                            </tr>
                        </table>
                        <table width="100%" border="0" cellpadding="2" cellspacing="0">
                            <tr>
                                <td width="20%">
                                    <input type="button" class="buttonStyleNew" id="agencyInfo" value="Agency Info"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/agencyInfo.jsp?relay=' + 'add', 310, 1000)"/>
                                </td>
                                <td width="80%">
                                    <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;vertical-align: bottom;">
                                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                            <%
                                                int i = 0;
                                                int k = 0;
                                            %>
                                            <display:table name="<%=agencyInfoListForLCL%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portexceptiontable" style="width:100%">
                                                <display:setProperty name="paging.banner.some_items_found">
                                                    <span class="pagebanner"> <font color="blue">{0}</font>
                                                        Port Exceptions Displayed,For more Data click on Page Numbers. <br>
                                                    </span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.one_item_found">
                                                    <span class="pagebanner">
                                                        One {0} displayed. Page Number
                                                    </span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.all_items_found">
                                                    <span class="pagebanner">
                                                        {0} {1} Displayed, Page	Number
                                                    </span>
                                                </display:setProperty>
                                                <display:setProperty name="basic.msg.empty_list">
                                                    <span class="pagebanner"> No Records Found. </span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.placement"
                                                                     value="bottom" />
                                                <display:setProperty name="paging.banner.item_name"
                                                                     value="Agency Info" />
                                                <display:setProperty name="paging.banner.items_name"
                                                                     value="Agency Info" />
                                                <%
                                                    TradingPartner customerObj = null;
                                                    TradingPartner consigneeObj = null;
                                                    String agentAcountNo = "";
                                                    String agntName = "";
                                                    String conAcctNo = "";
                                                    String conName = "";
                                                    String lclAgentLevelBrand = "";
                                                    StringBuilder pod = new StringBuilder();
                                                    String fd = "";
                                                    if (agencyInfoListForLCL != null && agencyInfoListForLCL.size() > 0) {
                                                        AgencyInfo agencyInfoObj = (AgencyInfo) agencyInfoListForLCL.get(i);
                                                        lclAgentLevelBrand = agencyInfoObj.getLclAgentLevelBrand();
                                                        if (agencyInfoObj != null) {
                                                            customerObj = agencyInfoObj.getAgentId();
                                                            consigneeObj = agencyInfoObj.getConsigneeId();
                                                            if (customerObj != null) {
                                                                agentAcountNo = customerObj.getAccountno();
                                                                agntName = customerObj.getAccountName();
                                                            }
                                                            if (consigneeObj != null) {
                                                                conAcctNo = consigneeObj.getAccountno();
                                                                conName = consigneeObj.getAccountName();
                                                            }
                                                            if (agencyInfoObj != null && agencyInfoObj.getPortOfDischarge() != null) {
                                                                if (CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationName()) && null != agencyInfoObj.getPortOfDischarge().getStateId()
                                                                        && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getStateId().getCode()) && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationCode())) {
                                                                    pod.append(agencyInfoObj.getPortOfDischarge().getUnLocationName()).append("/").append(agencyInfoObj.getPortOfDischarge().getStateId().getCode()).append('(').append(agencyInfoObj.getPortOfDischarge().getUnLocationCode()).append(')');
                                                                } else if (CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationName()) && agencyInfoObj.getPortOfDischarge().getCountryId() != null
                                                                        && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationCode())) {
                                                                    pod.append(agencyInfoObj.getPortOfDischarge().getUnLocationName()).append("/").append(agencyInfoObj.getPortOfDischarge().getCountryId().getCodedesc()).append('(').append(agencyInfoObj.getPortOfDischarge().getUnLocationCode()).append(')');
                                                                } else if (CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationCode()) && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationCode())) {
                                                                    pod.append(agencyInfoObj.getPortOfDischarge().getUnLocationName()).append('(').append(agencyInfoObj.getPortOfDischarge().getUnLocationCode()).append(')');
                                                                }
                                                                pod.toString();
                                                            }
                                                            fd = agencyInfoObj.getFinalDeliveryTo() != null ? agencyInfoObj.getFinalDeliveryTo() : "";
                                                        }
                                                    }
                                                %>
                                                <display:column title="Agent Acct No"><%=agentAcountNo%></display:column>
                                                <display:column></display:column>
                                                <display:column></display:column>
                                                <display:column title="Agent Name" ><%=agntName%></display:column>
                                                <display:column></display:column>
                                                <display:column></display:column>
                                                <display:column title="To Pickup Freight Acct"><%=conAcctNo%></display:column>
                                                <display:column></display:column>
                                                <display:column></display:column>
                                                <display:column title="To Pickup Freight name" ><%=conName%></display:column>
                                                <display:column></display:column>
                                                <display:column></display:column>
                                                <display:column title="Default" property="defaultValue">
                                                    <bean:message key="form.LCLPortsConfiguration.RadioDisplayTagY" />
                                                </display:column>
                                                <display:column title="Brand" >
                                                    <c:set var="lclAgentBrand" value="<%=lclAgentLevelBrand%>"/>
                                                    <c:choose>
                                                        <c:when test="${lclAgentBrand eq 'Econo'}">
                                                            Econo
                                                        </c:when>
                                                        <c:when test="${lclAgentBrand eq 'OTI'}">
                                                            OTI
                                                        </c:when>
                                                        <c:when test="${lclAgentBrand eq 'ECU WW'}">
                                                            ECU WW
                                                        </c:when>
                                                        <c:otherwise>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </display:column>
                                                <display:column title="PortOfDischarge">
                                                    <%=pod%>
                                                </display:column >
                                                <display:column title="FD">
                                                    <%=fd%>
                                                </display:column>
                                                <% i++;
                                                    k++;
                                                %>
                                            </display:table>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <table width="100%" border="0" cellpadding="2" cellspacing="0">
                                        <tr>
                                            <td class="textlabelsBold">SSMBL Alternate PortName</td>
                                            <td class="textlabelsBold">Transhipment To
                                                <%--<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'edittranshipment','windows')">--%>
                                            </td>
                                            <td><span class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.FTFfee" /></span></td>
                                            <td class="textlabelsBold">Default Port Of Discharge
                                                <%--<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'editdefaultport','windows')">--%>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td><html:text styleClass="textlabelsBoldForTextBox" property="alternatePortName" value="<%=portName%>" maxlength="25"  onkeyup="toUppercase(this)" style="width:120px"/></td>
                                            <td><html:text styleClass="textlabelsBoldForTextBox" property="transhipment" styleId="transhipment" value="<%=transhipment%>" onkeyup="toUppercase(this)"  style="width:120px"/></td>
                                        <div id="transhipmentchoices"  class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initAutocomplete("transhipment", "transhipmentchoices", "", "",
                                                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=PORTS&from=1&isDojo=false", "");
                                        </script>
                                        <td><html:text styleClass="textlabelsBoldForTextBox" property="ftfFee" maxlength="8" value="<%=ftfFee%>" size="22" onkeypress="getDecimal(this,5,event)" style="width:120px"/></td>
                                        <%--                                        <td><html:text styleClass="textlabelsBoldForTextBox" property="defaultPortOfDischarge" value="<%=defaultDischarge%>" onkeyup="toUppercase(this)" style="width:120px"/></td>--%>
                                        <td>
                                            <html:text styleClass="textlabelsBoldForTextBox" styleId="defaultPortOfDischarge" property="defaultPortOfDischarge" value="<%=defaultDischarge%>" onkeyup="toUppercase(this)" style="width:160px" onchange="cleardefaultDischarge()"/>
                                            <div id="defaultPortOfDischargechoices"  class="autocomplete"></div>
                                            <script type="text/javascript">
                                                initAutocomplete("defaultPortOfDischarge", "defaultPortOfDischargechoices", "", "",
                                                        "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=PORTS&from=1&isDojo=false", "");

                                            </script>
                                        </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.asetupAcct" /></td>
                                <td class="textlabelsBold">Account Name</td>
                                <td><span class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.acAccountPickup" /></span></td>
                                <td class="textlabelsBold">Account Name</td>

                            </tr>
                            <tr class="textlabelsBold">
                                <td>
                                    <%-- <input type="text" name="asetupAcct" class="textlabelsBoldForTextBox"  value="<%=asetupAcct%>" maxlength="10" id="asetupAcct"/> --%>
                                    <html:text styleClass="textlabelsBoldForTextBox" styleId="asetupAcct" property="asetupAcct" value="<%=asetupAcct%>" maxlength="10" style="width:80px" onkeyup="toUppercase(this)" onchange="clearAcct()"/>
                                    <div id="asetUpNamechoices"  class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("asetupAcct", "asetUpNamechoices", "", "",
                                                "<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=1&isDojo=false", "fillAccountNo1()");
                                    </script>
                                </td>
                                <td>
                                    <html:text styleClass="areahighlightgrey" styleId="asetupAcctName" property="asetupAcctName" style="width:120px" readonly="true" value="<%=asetupAcctName%>" />
                                </td>
                                <td>
                                    <html:text styleClass="textlabelsBoldForTextBox" styleId="acAccountPickup" property="acAccountPickup" maxlength="10" value="<%=acAccountPickup%>" onkeyup="toUppercase(this)" style="width:120px" onchange="clearPickupAcct()"/>
                                    <%--  <input type="text" name="acAccountPickup" class="textlabelsBoldForTextBox"  value="<%=acAccountPickup%>" id="acAccountPickup" maxlength="10"/> --%>
                                    <div id="acAccountPickupchoices" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("acAccountPickup", "acAccountPickupchoices", "", "",
                                                "<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=1&isDojo=false", "fillAccountNo2()");
                                    </script>
                                </td>
                                <td>
                                    <html:text styleClass="areahighlightgrey" styleId="acAccountPickupName" property="acAccountPickupName"  style="width:120px" readonly="true" value="<%=acAccountPickupName%>"/>
                                </td>

                            </tr>
                            <tr>
                                <td class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.hazAllowed" /></td>
                                <td class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.ftfWeight" /></td>
                                <td><span class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.ftfMeasure" /></span></td>
                                <td class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.ftfMinimum" /></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>
                                    <c:set var="hazallow" value="<%=hazAllowed%>"/>
                                    <c:choose><c:when test="${hazallow=='Y'}">
                                            <input type="radio" name="hazAllowed" value="Y" checked>Y &nbsp;
                                            <input type="radio" name="hazAllowed" value="N">N &nbsp;
                                            <input type="radio" name="hazAllowed" value="R">R
                                        </c:when>
                                        <c:when test="${hazallow=='N'}">
                                            <input type="radio" name="hazAllowed" value="Y">Y &nbsp;
                                            <input type="radio" name="hazAllowed" value="N" checked>N &nbsp;
                                            <input type="radio" name="hazAllowed" value="R" >R
                                        </c:when>
                                        <c:when test="${hazallow=='R'}">
                                            <input type="radio" name="hazAllowed" value="Y">Y &nbsp;
                                            <input type="radio" name="hazAllowed" value="N">N &nbsp;
                                            <input type="radio" name="hazAllowed" value="R" checked>R
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="hazAllowed" value="Y">Y &nbsp;
                                            <input type="radio" name="hazAllowed" value="N">N &nbsp;
                                            <input type="radio" name="hazAllowed" value="R">R
                                        </c:otherwise> </c:choose>
                                    <%-- <html:text styleClass="textlabelsBoldForTextBox" property="hazAllowed" value="<%=hazAllowed%>" maxlength="1"  onkeyup="toUppercase(this)" style="width:120px"/> --%>
                                </td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" property="ftfWeight" value="<%=ftfWeight%>" maxlength="6"  onkeyup="checkForNumberAndDecimal(this)" style="width:120px"/></td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" property="ftfMeasure" maxlength="6" value="<%=ftfMeasure%>" onkeyup="checkForNumberAndDecimal(this)" style="width:120px"/></td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" property="ftfMinimum" maxlength="6" value="<%=ftfMinimum%>" onkeyup="checkForNumberAndDecimal(this)" style="width:120px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.asetup" /></td>
                                <td class="textlabelsBold"><bean:message key="form.LCLPortsConfiguration.domestic" /></td>
                                <td class="textlabelsBold">Print O/F dollars on Ocean Manifest</td>
                                <td class="textlabelsBold">Print Invoice Value on LCL BL</td>
                                <td class="textlabelsBold">Lock port for LCL Ex Quotes/Bkgs</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold">
                                    <c:set var="asetup" value="<%=asetup%>"/>
                                    <c:choose>
                                        <c:when test ="${asetup=='I'}">
                                            <input type="radio" name="asetup" value="I" checked>I &nbsp;
                                            <input type="radio" name="asetup" value="U">U &nbsp;
                                            <input type="radio" name="asetup" value="B">B
                                        </c:when>
                                        <c:when test ="${asetup=='U'}">
                                            <input type="radio" name="asetup" value="I">I &nbsp;
                                            <input type="radio" name="asetup" value="U" checked>U &nbsp;
                                            <input type="radio" name="asetup" value="B">B
                                        </c:when>
                                        <c:when test ="${asetup=='B'}">
                                            <input type="radio" name="asetup" value="I">I &nbsp;
                                            <input type="radio" name="asetup" value="U">U &nbsp;
                                            <input type="radio" name="asetup" value="B" checked>B
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="asetup" value="I">I &nbsp;
                                            <input type="radio" name="asetup" value="U">U &nbsp;
                                            <input type="radio" name="asetup" value="B">B
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                                <td class="textlabelsBold">
                                    <c:set var="Domest" value="<%=domestic%>"/>
                                    <c:choose>
                                        <c:when test="${Domest=='Y'}">
                                            <input type="radio" name="domestic" value="Y" checked>Y &nbsp;
                                            <input type="radio" name="domestic" value="N">N
                                        </c:when>
                                        <c:when test="${Domest=='N'}">
                                            <input type="radio" name="domestic" value="Y" >Y &nbsp;
                                            <input type="radio" name="domestic" value="N" checked>N
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="domestic" value="Y" >Y &nbsp;
                                            <input type="radio" name="domestic" value="N" >N
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                                <td class="textlabelsBold">
                                    <c:set var="printOFdoll" value="<%=printOFdollars%>"/>
                                    <c:choose>
                                        <c:when test="${printOFdoll=='Y'}">
                                            <input type="radio" name="printOFdollars" value="Y" checked>Y &nbsp;
                                            <input type="radio" name="printOFdollars" value="N">N
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="printOFdollars" value="Y" >Y &nbsp;
                                            <input type="radio" name="printOFdollars" value="N" checked>N
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="textlabelsBold">
                                    <c:set var="printInvoiceValue" value="<%=printInvoice%>"/>
                                    <c:choose>
                                        <c:when test="${printInvoiceValue}">
                                            <input type="radio" name="printInvoiceValue" value="Y" checked>Y
                                            <input type="radio" name="printInvoiceValue" value="N">N
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="printInvoiceValue" value="Y">Y
                                            <input type="radio" name="printInvoiceValue" value="N" checked>N
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="textlabelsBold">
                                    <c:set var="lockPortForLcl" value="<%=lockPort%>"/>
                                    <c:choose>
                                        <c:when test="${lockPortForLcl}">
                                            <input type="radio" name="lockport" value="Y" checked>Y
                                            <input type="radio" name="lockport" value="N">N
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="lockport" value="Y">Y
                                            <input type="radio" name="lockport" value="N" checked>N
                                        </c:otherwise>
                                    </c:choose>


                                </td>
                            </tr>
                        </table>
                        <table width="100%" border="0" cellpadding="2" cellspacing="0">
                            <tr class="textlabelsBold">
                                <td><bean:message key="form.LCLPortsConfiguration.DefaultDomesticRoutingInstructions" /> </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td ><html:textarea  property="defaultDomesticRoutingInstructions" styleId="domesticRouting" value="<%=dafaultRoute%>"  cols="63" rows="3" onkeyup="limitText(this.form.defaultDomesticRoutingInstructions,this.form.countdown,120)" styleClass="textareastyle"/></td>
                                <td align="left" ><bean:message key="form.LCLPortsConfiguration.ServiceOcean" /></td>
                                <%-- <td><div align="center"><html:radio property="srvcOcean" value="Y" name="lclPortConfigurationDefaultRate" >Y</html:radio></div></td>
                                 <td><div align="center"><html:radio property="srvcOcean" value="N" name="lclPortConfigurationDefaultRate" >N</html:radio></div></td>
                                 <td><div  align="center"><html:radio property="srvcOcean" value="I" name="lclPortConfigurationDefaultRate" >I</html:radio></div></td>--%>

                                <td align="left">
                                    <html:select property="srvcOcean" style="width:120px;" styleClass="verysmalldropdownStyleForText" value="${lclPortConfigurationObj.srvcOcean}" onchange="getService()">
                                        <html:optionsCollection name="lclServiceList"/>
                                    </html:select>
                                </td>

                            </tr>
                        </table>
                        <table width="100%" border="0"  class="tableBorderNew" cellpadding="2" cellspacing="0">
                            <tr class="textlabelsBold">
                                <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.DefaultRateStd" /></td>
                                <%--<td><div align="left"><html:checkbox property="defaultRate"  name="lclPortRate" onclick="drchkall()"></html:checkbox></div></td>--%>
                                <td align="left" style="width: 150px" >
                                    <html:select property="defaultRate" style="width:120px;" styleClass="verysmalldropdownStyleForText"
                                                 value="${lclPortConfigurationObj.defaultRate}" >
                                        <html:option value="">Select</html:option>
                                        <html:option value="E">E</html:option>
                                        <html:option value="M">M</html:option>
                                    </html:select>
                                </td>

                                <td class="smallLabel" ><bean:message key="form.LCLPortsConfiguration.AutoCalculateLCLLoadingCost" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="autoCalLclLoad"   name="lclPortRate" onclick="autochkall()"  onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                    <td  class="smallLabel"><bean:message key="form.LCLPortsConfiguration.LCLB/LGoCollect" />(Y/N)</td>
                                <td ><div align="left"><html:checkbox property="lclOceanbl"  name="lclPortRate" onclick="oceanchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td  class="smallLabel">Express Release(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="expressRelease"  name="lclPortRate" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                </tr>
                                <tr class="textlabelsBold">
                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.CalculateLCLFAEbyUnit/Voyage" />(U/V)</td>
                                <td><div align="left"><html:checkbox property="calLclFaeUnitVoyage" name="lclPortRate" onclick="calchkall()" onmouseover="tooltip.show('<strong>Check For Unit and UnCheck For Voyage</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.ShowSpanishDesconBL" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="spanishDescOnBl"  name="lclPortRate" onclick="spanchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.PrintonSailingSch" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="printOnSailingSch"  name="lclPortRate" onclick="printchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td  class="smallLabel">Do Not Express Release(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="doNotExpressRelease"  name="lclPortRate" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                </tr>

                                <tr class="textlabelsBold">
                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.IncludeLCLDocChargesonBL" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="includeLclDocChargesBl"   name="lclPortRate" onclick="lclchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.PersonalEffectsonBL" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="persEffectBl"  name="lclPortRate" onclick="perschkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.OnCarriage" />(O/N)</td>
                                <td><div align="left"><html:checkbox property="onCarriage"   name="lclPortRate" onclick="onchkall"></html:checkbox></div></td>

                                    <td  class="smallLabel">Originals Required(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="originalsRequired"  name="lclPortRate"  onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                </tr>
                                <tr class="textlabelsBold">
                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.InsChargesAllowedonLCLBLs" />(Y/N)</td>

                                <td><div align="left"><html:checkbox property="insChargesLclBl"   name="lclPortRate" onclick="inschkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td class="smallLabel"> Protect Default Routing Instrn(Y/N) </td>
                                    <td><div align="left"><html:checkbox property="protectDefaultRoute"  name="lclPortRate" onclick="prochkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.CollectChargeonLCLBLs"/>(Y/N)</td>
                                <td><div align="left"><html:checkbox property="collectChargeOnLclBls"   name="lclPortRate" onclick="collchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td  class="smallLabel">Originals Released At Destination(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="originalsReleasedAtDestination"  name="lclPortRate"  onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td class="smallLabel">Force agent matching on Released D/Rs when loading</td>
                                    <td>
                                        <div align="left">
                                        <html:checkbox property="forceAgentReleasedDrLoading"   name="lclPortRate"
                                                       onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();">
                                        </html:checkbox>
                                    </div>
                                </td>
                                <td class="smallLabel">Default Print Imperial values on Metric Ports</td>
                                <td>
                                    <div align="left">
                                        <html:checkbox property="printImpOnMetric" name="lclPortRate"
                                                       onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();">
                                        </html:checkbox>
                                    </div>
                                </td>

                                <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.blNumbering"/>(Y/N)</td>
                                <td>
                                    <div align="left">
                                        <html:checkbox property="blNumbering" name="lclPortRate" onclick="blnumberformat()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"> </html:checkbox>
                                        </div>
                                    </td>
                                    <td  class="smallLabel">MEMO HOUSE BILL OF LADING(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="memoHouseBillofLading"  name="lclPortRate" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td  class="smallLabel">Bill Collects to FD agent when oncarriage</td>
                                    <td ><div align="left">
                                        <html:checkbox property="billCollectsFdAgent"  name="lclPortRate" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>   
                                </tr>    
                            </table>
                            <table border="0" cellpadding="2" cellspacing="0" width="100%">
                                <tr align="left" class="textlabelsBold">
                                    <td>LCL Internal Remarks</td>
                                    <td>LCL GRI Remarks</td>
                                </tr>
                                <tr align="left" class="textlabelsBold">
                                    <td rowspan="2"><html:textarea property="intrmRemarks" value="<%=lclIntrmRemarks%>" styleId="internalMarks" cols="58" rows="3"  onkeyup="limitText(this.form.intrmRemarks,this.form.countdown,400)" styleClass="textareastyle"/></td>
                                <td rowspan="3"><html:textarea property="frmRemarks" value="<%=lclFrmRemarks%>" styleId="griRemarks" cols="58" rows="3" onkeyup="limitText(this.form.frmRemarks,this.form.countdown,400)" styleClass="textareastyle"/></td>
                            </tr>

                        </table>
                        <table border="0" cellpadding="2" cellspacing="0" width="100%">
                            <tr align="left" class="textlabelsBold">
                                <td><bean:message key="form.LCLPortsConfiguration.LCLSplRemarksinEnglish"/></td>
                                <td><bean:message key="form.LCLPortsConfiguration.LCLSplRemarksinSpanish"/> </td>
                            </tr>
                            <tr align="left" class="textlabelsBold">
                                <td rowspan="2"><html:textarea property="lclSplRemarksinEnglish" value="<%=lclEnglish%>" styleId="splRemarksInEnglish" cols="58" rows="3"  onkeyup="limitText(this.form.lclSplRemarksinEnglish,this.form.countdown,400)" styleClass="textareastyle"/></td>
                                <td rowspan="3"><html:textarea property="lclSplRemarksinSpanish" value="<%=lclSpanish%>" styleId="splRemarksInSpanish" cols="58" rows="3" onkeyup="limitText(this.form.lclSplRemarksinSpanish,this.form.countdown,400)" styleClass="textareastyle"/></td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
</html:form>
</body>

</html>

