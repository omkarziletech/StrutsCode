<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="java.util.List,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.LCLPortConfiguration,com.gp.cong.logisoft.domain.AgencyInfo,com.gp.cong.logisoft.beans.PortsBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:useBean id="lclPortRate" class="com.gp.cong.logisoft.struts.form.LclPortsConfigForm" scope="request" ></jsp:useBean>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%    String path = request.getContextPath();

    String terminalName = "";
    String terminalNo = "";
    String userId = "";

    String transhipment = "";
    String defaultDischarge = "";
    String fTFFee = "";
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
    String blNumbering = "";
    String collectChargeOnLclBls = "";
    String asetup = "";
    String asetupAcct = "";
    String asetupAcctName = "";
    String acAccountPickup = "";
    String acAccountPickupName = "";
    String domestic = "";
    String hazAllowed = "";
    String ftfWeight = "";
    String ftfMeasure = "";
    String ftfMinimum = "";
    PortsBean portBean = new PortsBean();
    List agencyInfoListForLCL = null;
    request.setAttribute("lclServiceList", new DBUtil().getSelectBoxList(new Integer(73), "Select Port Service"));
    request.setAttribute("lclDefaultRateList", new DBUtil().getSelectBoxList(new Integer(75), "Select"));
    if (session.getAttribute("agencyInfoListForLCL") != null) {
        agencyInfoListForLCL = (List) session.getAttribute("agencyInfoListForLCL");
        session.setAttribute("agencyInfoList", agencyInfoListForLCL);
    }

    // Default values for radio buttons
    LCLPortConfiguration lclPortConfigurationDefaultRate = new LCLPortConfiguration();

    if (session.getAttribute("lclPortobj") != null) {
        lclPortConfigurationDefaultRate = (LCLPortConfiguration) session.getAttribute("lclPortobj");

        /*if(lclPortConfigurationDefaultRate.getLineManager()!=null)
         {
         userId=lclPortConfigurationDefaultRate.getLineManager().getFirstName()+" "+lclPortConfigurationDefaultRate.getLineManager().getLastName();
         }*/
        if (lclPortConfigurationDefaultRate.getTrmnum() != null) {
            terminalNo = lclPortConfigurationDefaultRate.getTrmnum().getTrmnum();
            terminalName = lclPortConfigurationDefaultRate.getTrmnum().getTerminalLocation();
        }
        if (lclPortConfigurationDefaultRate.getLineManager() != null) {
            userId = lclPortConfigurationDefaultRate.getLineManager();
        }
        if (lclPortConfigurationDefaultRate.getTranshipment() != null) {
            transhipment = lclPortConfigurationDefaultRate.getTranshipment();
        }
        if (lclPortConfigurationDefaultRate.getDefaultPortOfDischarge() != null) {
            defaultDischarge = lclPortConfigurationDefaultRate.getDefaultPortOfDischarge();
        }

        /*if(lclPortConfigurationDefaultRate.getTranshipment()!=null )
         {
         transhipment=lclPortConfigurationDefaultRate.getTranshipment().getShedulenumber()+"-"+lclPortConfigurationDefaultRate.getTranshipment().getPortname();
         }
         if(lclPortConfigurationDefaultRate.getDefaultPortOfDischarge()!=null )
         {
         defaultDischarge=lclPortConfigurationDefaultRate.getDefaultPortOfDischarge().getShedulenumber()+"-"+lclPortConfigurationDefaultRate.getDefaultPortOfDischarge().getPortname();
         }*/
        if (lclPortConfigurationDefaultRate.getFtfFee() != null) {
            fTFFee = lclPortConfigurationDefaultRate.getFtfFee().toString();
        }
        if (lclPortConfigurationDefaultRate.getAsetup() != null) {
            asetup = lclPortConfigurationDefaultRate.getAsetup().toString();
        }
        if (lclPortConfigurationDefaultRate.getAsetupAcct() != null) {
            asetupAcct = lclPortConfigurationDefaultRate.getAsetupAcct().toString();
        }
        if (lclPortConfigurationDefaultRate.getAcAccountPickup() != null) {
            acAccountPickup = lclPortConfigurationDefaultRate.getAcAccountPickup().toString();
        }
        if (lclPortConfigurationDefaultRate.getDomestic() != null) {
            domestic = lclPortConfigurationDefaultRate.getDomestic().toString();
        }
        if (lclPortConfigurationDefaultRate.getHazAllowed() != null) {
            hazAllowed = lclPortConfigurationDefaultRate.getHazAllowed().toString();
        }
        if (lclPortConfigurationDefaultRate.getFtfWeight() != null) {
            ftfWeight = lclPortConfigurationDefaultRate.getFtfWeight().toString();
        }
        if (lclPortConfigurationDefaultRate.getFtfMeasure() != null) {
            ftfMeasure = lclPortConfigurationDefaultRate.getFtfMeasure().toString();
        }
        if (lclPortConfigurationDefaultRate.getFtfMinimum() != null) {
            ftfMinimum = lclPortConfigurationDefaultRate.getFtfMinimum().toString();
        }
        // code for check box
        defaultRate = lclPortConfigurationDefaultRate.getDefaultRate();
        if (defaultRate != null && defaultRate.equals("M")) {
            lclPortRate.setDefaultRate("on");
        } else {
            lclPortRate.setDefaultRate("off");
        }
        autoCalLclLoad = lclPortConfigurationDefaultRate.getAutoCalLclLoad();
        if (autoCalLclLoad != null && autoCalLclLoad.equals("Y")) {
            lclPortRate.setAutoCalLclLoad("on");
        } else {
            lclPortRate.setAutoCalLclLoad("off");
        }

        lclOceanbl = lclPortConfigurationDefaultRate.getLclOceanbl();
        if (lclOceanbl != null && lclOceanbl.equals("Y")) {
            lclPortRate.setLclOceanbl("on");
        } else {
            lclPortRate.setLclOceanbl("off");
        }
        calLclFaeUnitVoyage = lclPortConfigurationDefaultRate.getCalLclFaeUnitVoyage();
        if (calLclFaeUnitVoyage != null && calLclFaeUnitVoyage.equals("V")) {
            lclPortRate.setCalLclFaeUnitVoyage("on");
        } else {
            lclPortRate.setCalLclFaeUnitVoyage("off");
        }
        spanishDescOnBl = lclPortConfigurationDefaultRate.getSpanishDescOnBl();
        if (spanishDescOnBl != null && spanishDescOnBl.equals("Y")) {
            lclPortRate.setSpanishDescOnBl("on");
        } else {
            lclPortRate.setSpanishDescOnBl("off");
        }
        printOnSailingSch = lclPortConfigurationDefaultRate.getPrintOnSailingSch();

        if (printOnSailingSch != null && printOnSailingSch.equals("Y")) {
            lclPortRate.setPrintOnSailingSch("on");
        } else {
            lclPortRate.setPrintOnSailingSch("off");
        }
        includeLclDocChargesBl = lclPortConfigurationDefaultRate.getIncludeLclDocChargesBl();
        if (includeLclDocChargesBl != null && includeLclDocChargesBl.equals("Y")) {
            lclPortRate.setIncludeLclDocChargesBl("on");
        } else {
            lclPortRate.setIncludeLclDocChargesBl("off");
        }

        persEffectBl = lclPortConfigurationDefaultRate.getPersEffectBl();
        if (persEffectBl != null && persEffectBl.equals("Y")) {
            lclPortRate.setPersEffectBl("on");
        } else {
            lclPortRate.setPersEffectBl("off");
        }
        onCarriage = lclPortConfigurationDefaultRate.getOnCarriage();
        if (onCarriage != null && onCarriage.equals("O")) {
            lclPortRate.setOnCarriage("on");
        } else {
            lclPortRate.setOnCarriage("off");
        }

        insChargesLclBl = lclPortConfigurationDefaultRate.getInsChargesLclBl();
        if (insChargesLclBl != null && insChargesLclBl.equals("Y")) {
            lclPortRate.setInsChargesLclBl("on");
        } else {
            lclPortRate.setInsChargesLclBl("off");
        }
        blNumbering = lclPortConfigurationDefaultRate.getBlNumbering();
        if (blNumbering != null && blNumbering.equals("Y")) {
            lclPortRate.setBlNumbering("on");
        } else {
            lclPortRate.setBlNumbering("off");
        }
        protectDefaultRoute = lclPortConfigurationDefaultRate.getProtectDefaultRoute();

        if (protectDefaultRoute != null && protectDefaultRoute.equals("Y")) {
            lclPortRate.setProtectDefaultRoute("on");
        } else {
            lclPortRate.setProtectDefaultRoute("off");
        }

        collectChargeOnLclBls = lclPortConfigurationDefaultRate.getCollectChargeOnLclBls();
        if (collectChargeOnLclBls != null && collectChargeOnLclBls.equals("Y")) {
            lclPortRate.setCollectChargeOnLclBls("on");
        } else {
            lclPortRate.setCollectChargeOnLclBls("off");
        }

    }
    lclPortConfigurationDefaultRate.setSrvcOcean("Y");
    lclPortRate.setOriginalsRequired(lclPortConfigurationDefaultRate.isOriginalsRequired());
    lclPortRate.setExpressRelease(lclPortConfigurationDefaultRate.isExpressRelease());
    lclPortRate.setDoNotExpressRelease(lclPortConfigurationDefaultRate.isDoNotExpressRelease());
    lclPortRate.setMemoHouseBillofLading(lclPortConfigurationDefaultRate.isMemoHouseBillofLading());
    lclPortRate.setBillCollectsFdAgent(lclPortConfigurationDefaultRate.isBillCollectsFdAgent());
    lclPortRate.setOriginalsReleasedAtDestination(lclPortConfigurationDefaultRate.isOriginalsReleasedAtDestination());
    lclPortRate.setForceAgentReleasedDrLoading(lclPortConfigurationDefaultRate.isForceAgentReleasedDrLoading());
    lclPortRate.setPrintImpOnMetric(lclPortConfigurationDefaultRate.getPrintImpOnMetric() != null ? lclPortConfigurationDefaultRate.getPrintImpOnMetric() : false);
    if (request.getAttribute("portBean") != null) {
        // set the  values of checkboxes
        portBean = (PortsBean) request.getAttribute("portBean");
        lclPortConfigurationDefaultRate.setDefaultRate(defaultRate);
        lclPortConfigurationDefaultRate.setAutoCalLclLoad(autoCalLclLoad);
        lclPortConfigurationDefaultRate.setLclOceanbl(lclOceanbl);

        lclPortConfigurationDefaultRate.setSrvcOcean(portBean.getServiceOcean());
        lclPortConfigurationDefaultRate.setCalLclFaeUnitVoyage(calLclFaeUnitVoyage);
        lclPortConfigurationDefaultRate.setSpanishDescOnBl(spanishDescOnBl);
        lclPortConfigurationDefaultRate.setPrintOnSailingSch(printOnSailingSch);
        lclPortConfigurationDefaultRate.setIncludeLclDocChargesBl(includeLclDocChargesBl);
        lclPortConfigurationDefaultRate.setPersEffectBl(persEffectBl);
        lclPortConfigurationDefaultRate.setOnCarriage(onCarriage);
        lclPortConfigurationDefaultRate.setInsChargesLclBl(insChargesLclBl);
        lclPortConfigurationDefaultRate.setProtectDefaultRoute(protectDefaultRoute);
        lclPortConfigurationDefaultRate.setCollectChargeOnLclBls(collectChargeOnLclBls);
        lclPortConfigurationDefaultRate.setBlNumbering(blNumbering);
    }
    request.setAttribute("lclPortConfigurationDefaultRate", lclPortConfigurationDefaultRate);
    AgencyInfo agencyDefaultObj = new AgencyInfo();
    agencyDefaultObj.setDefaultValue("Y");
    request.setAttribute("agencyDefaultObj", agencyDefaultObj);
    session.setAttribute("agency", "add");

%>
<html> 
    <head>
        <title>LCL Ports Configuration</title>
        <%@include file="../includes/baseResources.jsp" %>

        <script language="javascript" type="text/javascript">
            function submit1()
            {
                document.LclPortsConfigForm.buttonValue.value = "terminalSelected";
                document.LclPortsConfigForm.submit();
            }
            function cleardefaultDischarge() {
                var acct = document.getElementById("defaultPortOfDischarge").value;
                if (acct == '') {
                    document.getElementById("defaultPortOfDischarge").value = "";
                }
            }
            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function save()
            {
                document.LclPortsConfigForm.submit();
            }

            var newwindow = '';
            function openAgencyInfo() {
                if (!newwindow.closed && newwindow.location)
                {
                    newwindow.location.href = "<%=path%>/jsps/datareference/agencyInfo.jsp?";
                }
                else
                {
                    newwindow = window.open("<%=path%>/jsps/datareference/agencyInfo.jsp?", "", "width=650,height=450");
                    if (!newwindow.opener)
                        newwindow.opener = self;
                }
                if (window.focus) {
                    newwindow.focus()
                }
                document.LclPortsConfigForm.submit();
                return false;
            }
            var mywindow = '';
            function openassociatedport() {
                if (!mywindow.closed && mywindow.location)
                {
                    mywindow.location.href = "<%=path%>/jsps/datareference/AssociatedPort.jsp";
                }
                else
                {
                    mywindow = window.open("<%=path%>/jsps/datareference/AssociatedPort.jsp", "", "width=650,height=450");
                    if (!mywindow.opener)
                        mywindow.opener = self;
                }
                if (window.focus) {
                    mywindow.focus()
                }
                document.LclPortsConfigForm.submit();
                return false;
            }
            function limitText(limitField, limitCount, limitNum) {
                limitField.value = limitField.value.toUpperCase();
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                } else {
                    limitCount.value = limitNum - limitField.value.length;
                }

            }

            //functions for the codecheks
            function drchkall() {
                if (document.LclPortsConfigForm.defaultRate.checked)
                {

                    document.LclPortsConfigForm.defaultRate.value = "M";

                    document.LclPortsConfigForm.defaultRate.focus();

                    return false;
                }
            }

            function autochkall() {
                if (document.LclPortsConfigForm.autoCalLclLoad.checked)
                {

                    document.LclPortsConfigForm.autoCalLclLoad.value = "Y";

                    document.LclPortsConfigForm.autoCalLclLoad.focus();

                    return false;
                }
            }
            function oceanchkall() {
                if (document.LclPortsConfigForm.lclOceanbl.checked)
                {

                    document.LclPortsConfigForm.lclOceanbl.value = "V";

                    document.LclPortsConfigForm.lclOceanbl.focus();

                    return false;
                }
            }
            function spanchkall() {
                if (document.LclPortsConfigForm.spanishDescOnBl.checked)
                {

                    document.LclPortsConfigForm.spanishDescOnBl.value = "Y";

                    document.LclPortsConfigForm.spanishDescOnBl.focus();

                    return false;
                }
            }
            function printchkall() {
                if (document.LclPortsConfigForm.printOnSailingSch.checked)
                {

                    document.LclPortsConfigForm.printOnSailingSch.value = "Y";

                    document.LclPortsConfigForm.printOnSailingSch.focus();

                    return false;
                }
            }

            function lclchkall() {
                if (document.LclPortsConfigForm.includeLclDocChargesBl.checked)
                {

                    document.LclPortsConfigForm.includeLclDocChargesBl.value = "Y";

                    document.LclPortsConfigForm.includeLclDocChargesBl.focus();

                    return false;
                }
            }

            function perschkall() {
                if (document.LclPortsConfigForm.persEffectBl.checked)
                {

                    document.LclPortsConfigForm.persEffectBl.value = "Y";

                    document.LclPortsConfigForm.persEffectBl.focus();

                    return false;
                }
            }
            function onchkall() {
                if (document.LclPortsConfigForm.onCarriage.checked)
                {

                    document.LclPortsConfigForm.onCarriage.value = "O";

                    document.LclPortsConfigForm.onCarriage.focus();

                    return false;
                }
            }


            function inschkall() {
                if (document.LclPortsConfigForm.insChargesLclBl.checked)
                {
                    document.LclPortsConfigForm.insChargesLclBl.value = "Y";
                    document.LclPortsConfigForm.insChargesLclBl.focus();
                    return false;
                }
            }

            function blnumberall() {
                if (document.LclPortsConfigForm.blNumbering.checked) {
                    document.LclPortsConfigForm.blNumbering.value = "Y";
                    document.LclPortsConfigForm.blNumbering.focus();
                    return false;
                }

                function prochkall() {
                    if (document.LclPortsConfigForm.protectDefaultRoute.checked)
                    {

                        document.LclPortsConfigForm.protectDefaultRoute.value = "Y";

                        document.LclPortsConfigForm.protectDefaultRoute.focus();

                        return false;
                    }
                }
                function calchkall() {
                    if (document.LclPortsConfigForm.calLclFaeUnitVoyage.checked)
                    {

                        document.LclPortsConfigForm.calLclFaeUnitVoyage.value = "V";

                        document.LclPortsConfigForm.calLclFaeUnitVoyage.focus();

                        return false;
                    }
                }
                function collchkall() {
                    if (document.LclPortsConfigForm.collectChargeOnLclBls.checked)
                    {

                        document.LclPortsConfigForm.collectChargeOnLclBls.value = "Y";

                        document.LclPortsConfigForm.collectChargeOnLclBls.focus();

                        return false;
                    }
                }

                function popup1(mylink, windowname)
                {
                    if (!window.focus)
                        return true;
                    var href;
                    if (typeof (mylink) == 'string')
                        href = mylink;
                    else
                        href = mylink.href;
                    mywindow = window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
                    mywindow.moveTo(200, 180);

                    document.LclPortsConfigForm.submit();
                    return false;
                }

                function getLclPortsConfig() {
                    window.location.href = "<%=path%>/jsps/datareference/lclPortsConfig.jsp";
                }
        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body  class="whitebackgrnd">
        <html:form action="/lclPortsConfig" name="LclPortsConfigForm" type="com.gp.cong.logisoft.struts.form.LclPortsConfigForm" scope="request">

            <table width="100%">
                <tr>
                    <td align="right">

                        <input type="button" class="buttonStyleNew" id="search" value="Assoc Ports"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/AssociatedPort.jsp?relay=' + 'add', 350, 650)"/>

                </tr>
            </table>

            <table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew" colspan="2">Lcl Ports Configuration</tr>
                <tr>
                    <td>
                        <table width="100%">
                            <tr class="style2">
                                <td>Terminal No&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=' + 'lcl', 'windows')"></td>
                                <td class="style2">Terminal Location</td>
                                <td class="style2">Line Manager&nbsp;
                                </td>
                                <td class="style2">Lane</td>
                            </tr>
                            <tr>
                                <td><html:text property="terminalNo" value="<%=terminalNo%>" readonly="true" style="width:120px">
                                    </html:text></td>
                                <td><html:text property="terminalName" value="<%=terminalName%>" styleClass="areahighlightgrey" readonly="true" style="width:120px"/></td>
                                <td><html:text property="lineManager"  value="<%=userId%>"   style="width:120px"/></td>
                                <td><html:text property="laneField" maxlength="6"  value="<%=lclPortConfigurationDefaultRate.getLaneField()%>" onkeyup="toUppercase(this)" style="width:120px"/></td>
                            </tr>
                        </table>
                    </td></tr>
                <tr>
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" >
                            <tr>
                                <td width="15%">
                                    <input type="button" class="buttonStyleNew" id="search" value="Agency Info" onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/agencyInfo.jsp?relay=' + 'add', 400, 900)"/>
                                <td>
                                    <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
                                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                            <%
                                                int i = 0;
                                                int k = 0;
                                            %>
                                            <display:table name="<%=agencyInfoListForLCL%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portexceptiontable" style="width:100%">
                                                <display:setProperty name="paging.banner.some_items_found">
                                                    <span class="pagebanner"> <font color="blue">{0}</font>
                                                        AgencyInfo Displayed,For more AgencyInfo click on Page Numbers. <br>
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
                                                <display:column title="Default" property="defaultValue"/>
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



                                                <display:column></display:column>
                                                <display:column></display:column>
                                                <% i++;
                                                    k++;
                                                %>
                                            </display:table>
                                        </table></div>
                                </td>
                            </tr>
                        </table>
                    </td></tr>

                <tr>
                    <td>
                        <table width="100%" border="0">
                            <tr class="style2">
                                <td>SSMBL Alternate PortName</td>
                                <td>Transhipment&nbsp;
                                    <%--<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'trashipment','windows')">--%>
                                </td>
                                <td><bean:message key="form.LCLPortsConfiguration.FTFfee" /> </td>
                                <td>Default Port Of Discharge&nbsp;
                                    <%--<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'defaultport','windows')">--%>
                                </td>
                            </tr>
                            <tr>
                                <td><html:text property="alternatePortName" maxlength="25" value="<%=lclPortConfigurationDefaultRate.getAltPortName()%>" onkeyup="toUppercase(this)" style="width:120px"/></td>
                                <td><html:text property="transhipment" value="<%=transhipment%>"  style="width:120px" /></td>
                                <td><html:text property="ftfFee" maxlength="8"  value="<%=fTFFee%>" onkeypress=" getDecimal(this,5,event)" style="width:120px"/></td>
                                <%--<td><html:text property="defaultPortOfDischarge" value="<%=defaultDischarge%>"  style="width:120px" /></td>--%>
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
                                    <input type="radio" name="hazAllowed" value="Y">Y &nbsp;
                                    <input type="radio" name="hazAllowed" value="N">N &nbsp;
                                    <input type="radio" name="hazAllowed" value="R">R
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
                                <td></td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold">
                                    <input type="radio" name="asetup" value="I">I &nbsp;
                                    <input type="radio" name="asetup" value="U">U &nbsp;
                                    <input type="radio" name="asetup" value="B">B
                                </td>
                                <td class="textlabelsBold">
                                    <input type="radio" name="domestic" value="Y" >Y &nbsp;
                                    <input type="radio" name="domestic" value="N" >N
                                </td>
                                <td class="textlabelsBold">
                                    <input type="radio" name="printOFdollars" value="Y" >Y &nbsp;
                                    <input type="radio" name="printOFdollars" value="N" >N

                                </td> <td></td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td>
                        <table border="0" width="100%">
                            <tr class="style2">
                                <td width="30%"><bean:message key="form.LCLPortsConfiguration.DefaultDomesticRoutingInstructions" /> </td>
                                <td width="25%"></td>

                            </tr>
                        </table>
                        <table  border="0" width="100%">
                            <tr class="style2">
                                <td width="30%"><html:textarea property="defaultDomesticRoutingInstructions" cols="66" rows="3" value="<%=lclPortConfigurationDefaultRate.getRoutingInstr()%>" onkeyup="limitText(this.form.defaultDomesticRoutingInstructions,this.form.countdown,120)" styleClass="textareastyle"/></td>
                                <td align="right"   width="15%"><bean:message key="form.LCLPortsConfiguration.ServiceOcean" /> </td>
                                <td align="left">
                                    <html:select property="srvcOcean" style="width:120px;" styleClass="verysmalldropdownStyleForText" value="${lclPortobj.srvcOcean}" onchange="getService()">
                                        <html:optionsCollection name="lclServiceList"/>
                                    </html:select>
                                </td>

                            </tr>
                        </table>
                    </td>
                </tr>


                <tr>
                    <td>
                        <table width="100%" class="tableBorderNew">
                            <tr class="style2">
                                <td  class="smallLabel"><bean:message key="form.LCLPortsConfiguration.DefaultRateStd" /></td>
                                <%--<td><div align="left"><html:checkbox property="defaultRate"  name="lclPortRate" onclick="drchkall()"></html:checkbox></div></td>--%>
                                <td align="left" style="width: 150px" >
                                    <html:select property="defaultRate" style="width:120px;" styleClass="verysmalldropdownStyleForText" value="${lclPortConfigurationObj.defaultRate}" >
                                        <html:optionsCollection name="lclDefaultRateList"/>
                                    </html:select>
                                </td>
                                <td   class="smallLabel"><bean:message key="form.LCLPortsConfiguration.AutoCalculateLCLLoadingCost" />(Y/N)</td>
                                <td ><div align="left"><html:checkbox property="autoCalLclLoad"   name="lclPortRate" onclick="autochkall()"  onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                    <td  class="smallLabel"><bean:message key="form.LCLPortsConfiguration.LCLB/LGoCollect" />(Y/N)</td>
                                <td ><div align="left"><html:checkbox property="lclOceanbl"  name="lclPortRate" onclick="oceanchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td  class="smallLabel">Express Release(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="expressRelease"  name="lclPortRate" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td  class="smallLabel">Do Not Express Release(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="doNotExpressRelease"  name="lclPortRate" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td  class="smallLabel">MEMO HOUSE BILL OF LADING(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="memoHouseBillofLading"  name="lclPortRate" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                </tr>
                                <tr class="style2">
                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.CalculateLCLFAEbyUnit/Voyage" />(U/V)</td>
                                <td><div align="left"><html:checkbox property="calLclFaeUnitVoyage"  name="lclPortRate" onclick="calchkall()" onmouseover="tooltip.show('<strong>Check For Unit and UnCheck For Voyage</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.ShowSpanishDesconBL" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="spanishDescOnBl"  name="lclPortRate" onclick="spanchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.PrintonSailingSch" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="printOnSailingSch"  name="lclPortRate" onclick="printchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td  class="smallLabel">Originals Required(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="originalsRequired"  name="lclPortRate"  onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                </tr>
                                <tr class="style2">
                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.IncludeLCLDocChargesonBL" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="includeLclDocChargesBl"   name="lclPortRate" onclick="lclchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.PersonalEffectsonBL" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="persEffectBl"  name="lclPortRate" onclick="perschkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.OnCarriage" />(O/N)</td>
                                <td><div align="left"><html:checkbox property="onCarriage"   name="lclPortRate" onclick="onchkall"  ></html:checkbox></div></td>
                                    <td  class="smallLabel">Originals Released At Destination(Y/N)</td>
                                    <td ><div align="left"><html:checkbox property="originalsReleasedAtDestination"  name="lclPortRate"  onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                </tr>
                                <tr class="style2">
                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.InsChargesAllowedonLCLBLs" />(Y/N)</td>
                                <td><div align="left"><html:checkbox property="insChargesLclBl"   name="lclPortRate" onclick="inschkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                    <td class="smallLabel"> Protect Default Routing Instrn(Y/N) </td>
                                    <td><div align="left"><html:checkbox property="protectDefaultRoute"  name="lclPortRate" onclick="prochkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>

                                    <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.CollectChargeonLCLBLs"/>(Y/N) </td>
                                <td><div align="left"><html:checkbox property="collectChargeOnLclBls"   name="lclPortRate" onclick="collchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox></div></td>
                                <td class="smallLabel"><bean:message key="form.LCLPortsConfiguration.blNumbering"/>(Y/N)</td>
                                <td><div align="left">
                                        <html:checkbox property="blNumbering" name="lclPortRate" onclick="blnumberall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"> </html:checkbox>
                                        </div>
                                    </td>
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
                                        <html:checkbox property="printImpOnMetric"   name="lclPortRate"
                                                       onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();">
                                        </html:checkbox>
                                    </div>
                                </td>
                                <td class="smallLabel">Bill Collects to FD agent when oncarriage</td>
                                <td>
                                    <div align="left">
                                        <html:checkbox property="billCollectsFdAgent"   name="lclPortRate"
                                                       onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();">
                                        </html:checkbox>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table>
                            <tr align="left" class="style2">
                                <td colspan="7">LCL Internal Remarks</td>
                                <td colspan="7">LCL GRI Remarks</td>
                            </tr>
                            <tr align="left" class="style2">
                                <td colspan="7" rowspan="3"><html:textarea property="intrmRemarks" cols="58" rows="3" value="<%=lclPortConfigurationDefaultRate.getIntrmRemarks()%>" onkeyup="limitText(this.form.lclInternalRemarks,this.form.countdown,400)"   styleClass="textareastyle"/></td>
                                <td colspan="7" rowspan="3"><html:textarea property="frmRemarks" cols="58" rows="3" value="<%=lclPortConfigurationDefaultRate.getFrmRemarks()%>" onkeyup="limitText(this.form.lclGRIRemarks,this.form.countdown,400)"   styleClass="textareastyle"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table>
                            <tr align="left" class="style2">
                                <td colspan="7"><bean:message key="form.LCLPortsConfiguration.LCLSplRemarksinEnglish"/></td>
                                <td colspan="7"><bean:message key="form.LCLPortsConfiguration.LCLSplRemarksinSpanish"/> </td>

                            </tr>
                            <tr align="left" class="style2">
                                <td colspan="7" rowspan="2"><html:textarea property="lclSplRemarksinEnglish" cols="58" rows="3" value="<%=lclPortConfigurationDefaultRate.getLclSplRemarksInEnglish()%>" onkeyup="limitText(this.form.lclSplRemarksinEnglish,this.form.countdown,400)"   styleClass="textareastyle"/></td>
                                <td colspan="7" rowspan="3"><html:textarea property="lclSplRemarksinSpanish" cols="58" rows="3" value="<%=lclPortConfigurationDefaultRate.getLclSplRemarksInSpanish()%>" onkeyup="limitText(this.form.lclSplRemarksinSpanish,this.form.countdown,400)" styleClass="textareastyle"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue"/>
        </html:form>
    </body>

    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

