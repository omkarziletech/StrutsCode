<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ include file="../includes/jspVariables.jsp" %>
<%@ include file="../includes/resources.jsp" %>
<%@ include file="../includes/baseResources.jsp"%>
<html>
<head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <base href="${basePath}">
        <title>My JSP 'SedFiling.jsp' starting page</title>
        <link rel="stylesheet" href="<c:url value="/css/script-aculo-us.tabs.css"/>" title="script-aculo-us.tabs">
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/script-aculo-us.tabs.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/fcl/sedFiling.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script language="javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
</head>
<c:if test="${not empty hideGbShow}">
    <script>
        parent.parent.refreshPage();
    </script>
</c:if>
<body class="whitebackgrnd">
    <div id="cover" style="width: expression(document.body.offsetWidth+'px');height: expression(document.body.offsetHeight+120+'px');display: none"></div>
         <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                  onclick="document.getElementById('AlertBox').style.display='none';
       					grayOut(false,'');">
            </form>
        </div>
         <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmYes()">
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="confirmNo()">
            </form>
        </div>
         <div id="ConfirmBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert" style="width: 100%;padding-left: 3px;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" id="confirmYes"
                       onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel" id="confirmNo"
                       onclick="No()">
            </form>
        </div>
    <html:form action="/sedFiling" type="com.gp.cong.logisoft.struts.form.SedFilingForm" name="sedFilingForm" scope="request">
<table>
    <tr valign="top">
            <td width="30%">
                <table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3" >
                    <tr class="tableHeadingNew">
                        <td>USPPI</td>
                    </tr>
                    <tr>
                        <td>
                            <table  border="0" width="100%" cellspacing="0" cellpadding="2">
                                <tr class="textlabelsBold">
                                    <td>Name</td>
                                    <td>
                                        <input name="expnam" value="${sedFilingForm.expnam}" size="35" onfocus="disableDojo(this)"
                                               id="expnam" class="textlabelsBoldForTextBox mandatory"  style="text-transform: uppercase" maxlength="60"/>
                                        <input id="sedhouseName_check"  type="hidden" value="${sedFilingForm.expnam}"/>
                                        <div id="sedshipper_choices"  style="display:none;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("expnam","sedshipper_choices","expnum","sedhouseName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=3","getSedShipper()","clearShipper()");
                                        </script>
                                        <html:checkbox property="shipperCheck" styleId="shipperCheck" name="sedFilingForm"
                                             onclick="disableAutoComplete(this)" onmouseover="tooltip.show('<strong>Use TP Dojo</strong>',null,event);"
                                             onmouseout="tooltip.hide();"/>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Number</td>
                                    <td><html:text property="expnum" size="35" styleClass="BackgrndColorForTextBox"
                                               styleId="expnum" name="sedFilingForm" maxlength="10" style="text-transform: uppercase" readonly="true" tabindex="-1"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Cargo Origin <br>Address</td>
                                    <td><html:textarea property="expadd" styleClass="textlabelsBoldForTextBox mandatory" name="sedFilingForm" styleId="expadd" cols="36" rows="3" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>City</td>
                                    <td><html:text property="expcty" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expcty" name="sedFilingForm" maxlength="30" style="text-transform: uppercase"/></td>
                                </tr>
                                 <tr class="textlabelsBold">
                                     <td>State</td>
                                    <td><html:text property="expsta"  styleClass="textlabelsBoldForTextBox mandatory"  styleId="expsta" name="sedFilingForm" maxlength="2" style="text-transform: uppercase"/></td>
                                 </tr>
                                <tr class="textlabelsBold">
                                    <td>Zip</td>
                                    <td><html:text property="expzip" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expzip" name="sedFilingForm" maxlength="5" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>IRS #</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty sedFilingForm.expirs && sedFilingForm.expirsTp == 'Y'}">
                                                <html:text property="expirs" styleClass="BackgrndColorForTextBox mandatory"  styleId="expirs" name="sedFilingForm"
                                                            style="text-transform: uppercase" readonly="true" tabindex="-1"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text property="expirs" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expirs" name="sedFilingForm"
                                                            style="text-transform: uppercase"  onchange="validateIrs(this)"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <html:hidden property="expirsTp" name="sedFilingForm" styleId="expirsTp"/>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                     <td>Type</td>
                                    <td>
                                        <html:select property="expicd" styleId="expicd" styleClass="dropdown_accounting mandatory" name="sedFilingForm">
                                            <html:option value="">Select</html:option>
                                            <html:option value="E">EIN</html:option>
                                            <html:option value="D">DUNS</html:option>
                                            <html:option value="T">PASSPORT NUMBER</html:option>
                                        </html:select>
                                    </td>
                                 </tr>
                                <tr class="textlabelsBold">
                                     <td>POA</td>
                                    <td>
                                        <html:radio property="exppoa" styleId="exppoa1" name="sedFilingForm" value="Y"/>Y
                                        <html:radio property="exppoa" styleId="exppoa2" name="sedFilingForm" value="N"/>N
                                    </td>
                                 </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3" >
                    <tr class="tableHeadingNew">
                        <td>ULTIMATE CONSIGNEE</td>
                    </tr>
                    <tr>
                        <td>
                            <table  border="0" width="100%" cellspacing="0" cellpadding="2">
                                <tr class="textlabelsBold">
                                    <td>Name</td>
                                    <td>
                                        <input  class="textlabelsBoldForTextBox mandatory" name="connam" id="connam" size="35" maxlength="60"
                                                value="${sedFilingForm.connam}"  style="text-transform: uppercase" onfocus="disableDojo(this)"/>
                                        <input id="sedConsigneeName_check" type="hidden" value="${sedFilingForm.connam}" />
                                        <div id="sedConsignee_choices"  style="display:none;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("connam","sedConsignee_choices","connum","sedConsigneeName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=4&acctTyp=C","getSedConsignee()","clearConsignee()");
                                        </script>
                                        <html:checkbox property="consigneeCheck" styleId="consigneeCheck" name="sedFilingForm"
                                             onclick="disableAutoComplete(this)" onmouseover="tooltip.show('<strong>Use TP Dojo</strong>',null,event);"
                                             onmouseout="tooltip.hide();"/>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Number</td>
                                    <td><html:text property="connum" size="35" styleClass="BackgrndColorForTextBox"
                                               styleId="connum" name="sedFilingForm" maxlength="10" style="text-transform: uppercase" readonly="true" tabindex="-1"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Address</td>
                                    <td><html:textarea property="conadd" styleClass="textlabelsBoldForTextBox mandatory"  styleId="conadd" name="sedFilingForm" cols="36" rows="3" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>City</td>
                                    <td><html:text property="concty" styleClass="textlabelsBoldForTextBox mandatory"  styleId="concty" name="sedFilingForm" maxlength="30" style="text-transform: uppercase"/></td>
                                </tr>
                                <c:set var="displayConsta" value="none"/>
                                <c:if test="${sedFilingForm.cntdes eq 'MX' || sedFilingForm.cntdes eq 'CA' || sedFilingForm.cntdes eq 'US'}">
                                    <c:set var="displayConsta" value="block"/>
                                </c:if>
                                <tr class="textlabelsBold" style="display: ${displayConsta}" id="consigneeStateId">
                                    <td>State</td>
                                    <td>
                                        <html:text property="consta" styleClass="textlabelsBoldForTextBox"  styleId="consta" name="sedFilingForm" maxlength="2" style="text-transform: uppercase"/>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Country</td>
                                    <td>
                                        <html:text property="conctry" styleClass="textlabelsBoldForTextBox"  styleId="conctry" name="sedFilingForm" maxlength="10" style="text-transform: uppercase"/>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Zip</td>
                                    <td><html:text property="conpst" styleClass="textlabelsBoldForTextBox"  styleId="conpst" name="sedFilingForm" maxlength="5" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>POA</td>
                                    <td>
                                        <html:radio property="conpoa" styleId="conpoa1" name="sedFilingForm" value="Y"/>Y
                                        <html:radio property="conpoa" styleId="conpoa2" name="sedFilingForm" value="N"/>N
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                     <td>CONTYP</td>
                                    <td>
                                        <html:select property="CONTYP" styleId="CONTYP" styleClass="dropdown_accounting mandatory" name="sedFilingForm">
                                            <html:option value="">Select</html:option>
                                            <html:option value="O">O=Other/Unknown</html:option>
                                            <html:option value="D">D=DirectConsumer</html:option>
                                            <html:option value="G">G=GovernmentEntity</html:option>
                                            <html:option value="R">R=ResaleDealer</html:option>
                                        </html:select>
                                    </td>
                                 </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <%--<table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3" >
                    <tr class="tableHeadingNew">
                        <td>FORWARDER</td>
                    </tr>
                    <tr>
                        <td>
                            <table  border="0" width="100%" cellspacing="0" cellpadding="2">
                                <tr class="textlabelsBold">
                                    <td>Name</td>
                                    <td colspan="3">
                                        <input name="frtnam" id="frtnam" value="${sedFilingForm.frtnam}" size="35" maxlength="60"
                                               class="textlabelsBoldForTextBox" style="text-transform: uppercase" onfocus="disableDojo(this)"/>
                                        <input id="sedForwarder_check" type="hidden" value="${sedFilingForm.frtnam}" />
                                        <div id="sedForwarder_choices"  style="display:none;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("frtnam","sedForwarder_choices","frtnum","sedForwarder_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=10","getSedForwarder()","clearForwarder()");
                                        </script>
                                        <html:checkbox property="forwarderCheck" styleId="forwarderCheck" name="sedFilingForm"
                                             onclick="disableAutoComplete(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>');"
                                             onmouseout="tooltip.hide();"/>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Number</td>
                                    <td colspan="3"><html:text property="frtnum"  size="35" styleClass="BackgrndColorForTextBox"  styleId="frtnum"
                                               name="sedFilingForm" maxlength="10" style="text-transform: uppercase" readonly="true" tabindex="-1"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Address</td>
                                    <td colspan="3"><html:textarea property="frtadd" styleClass="textlabelsBoldForTextBox"  styleId="frtadd" name="sedFilingForm" cols="36" rows="3" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>City</td>
                                    <td><html:text property="frtcty" styleClass="textlabelsBoldForTextBox"  styleId="frtcty" name="sedFilingForm" maxlength="30" style="text-transform: uppercase"/></td>
                                    <td>State</td>
                                    <td><html:text property="frtsta" styleClass="textlabelsBoldForTextBox"  styleId="frtsta" name="sedFilingForm" maxlength="2" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Zip</td>
                                    <td><html:text property="frtzip" styleClass="textlabelsBoldForTextBox"  styleId="frtzip" name="sedFilingForm" maxlength="5" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>IRS #</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty sedFilingForm.frtirs && sedFilingForm.frtirsTp == 'Y'}">
                                                <html:text property="frtirs" styleClass="BackgrndColorForTextBox"  styleId="frtirs" name="sedFilingForm"
                                                            style="text-transform: uppercase" readonly="true" tabindex="-1"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text property="frtirs" styleClass="textlabelsBoldForTextBox"  styleId="frtirs" name="sedFilingForm"
                                                            style="text-transform: uppercase"  onchange="validateIrs(this)"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <html:hidden property="frtirsTp" name="sedFilingForm" styleId="frtirsTp"/>
                                    </td>
                                    <td>Type</td>
                                    <td>
                                        <html:select property="frticd" styleId="frticd" styleClass="textlabelsBoldForTextBox" name="sedFilingForm">
                                            <html:option value="">Select</html:option>
                                            <html:option value="E">EIN</html:option>
                                            <html:option value="S">SSN</html:option>
                                            <html:option value="D">DUNS</html:option>
                                        </html:select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>--%>
            </td>
            <td width="70%" valign="top">
                <table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3">
                    <tr class="tableHeadingNew">
                        <td>General Information</td>
                        <td align="right">
                            <c:choose>
                                <c:when test="${action == 'updateAes'}">
                                    <input type="button" value="Update" id="updateAes" class="buttonStyleNew" onclick="updateAesDetails()" />
                                </c:when>
                                <c:otherwise>
                                    <input type="button" value="Save" id="saveAes" class="buttonStyleNew" onclick="saveAesDetails()" />
                                </c:otherwise>
                            </c:choose>
                            <%--<input type="button" value="Schedule B" id="addScheduleB" class="buttonStyleNew" onclick="addSchedBDetails('${fclBl.fileNo}')" />--%>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table  border="0" width="100%" cellspacing="0" cellpadding="2">
                                <tr class="textlabelsBold">
                                    <td>ITN#</td>
                                    <td><html:text property="itn" styleClass="BackgrndColorForTextBox" styleId="itn" name="sedFilingForm" maxlength="40" readonly="true" tabindex="-1"/></td>
                                    <td>Shipment/DR#</td>
                                    <td><html:text property="shpdr" styleClass="BackgrndColorForTextBox" styleId="shpdr" name="sedFilingForm" maxlength="8" readonly="true" tabindex="-1"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Transaction Ref</td>
                                    <td><html:text property="trnref" styleClass="BackgrndColorForTextBox" styleId="trnref" name="sedFilingForm" maxlength="17" readonly="true" tabindex="-1"/></td>
                                    <td>BL NUM#</td>
                                    <td><html:text property="blnum" styleClass="BackgrndColorForTextBox" styleId="blnum" name="sedFilingForm"  maxlength="17" readonly="true" tabindex="-1"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>SSL BKG#</td>
                                    <td><html:text property="bkgnum" styleClass="BackgrndColorForTextBox" styleId="bkgnum" name="sedFilingForm" maxlength="30" readonly="true" tabindex="-1" value="${bkgNu}"/></td>
                                    <td>Email</td>
                                    <td><html:text property="email" styleClass="textlabelsBoldForTextBox mandatory" styleId="email" name="sedFilingForm" maxlength="38"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Status Of SED</td>
                                    <td>
                                        <html:select property="status" styleId="status" styleClass="textlabelsBoldForTextBox" name="sedFilingForm" disabled="true">
                                            <html:option value="N">New</html:option>
                                            <html:option value="S">Sent</html:option>
                                            <html:option value="C">Complete</html:option>
                                            <html:option value="E">Error</html:option>
                                        </html:select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3" >
                    <tr class="tableHeadingNew">
                        <td>Trade Route</td>
                    </tr>
                    <tr>
                        <td>
                            <table  border="0" width="100%" cellspacing="0" cellpadding="2">
                                <tr class="textlabelsBold">
                                    <td>Origin State</td>
                                    <td>
                                        <input class="textlabelsBoldForTextBox mandatory" name="origin" value="${sedFilingForm.origin}"  id="origin" style="text-transform: uppercase" size="25"/>
                                        <div id="sedterminalName_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                        <input id="origin_check" type="hidden" value="${sedFilingForm.origin}" />
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("origin","sedterminalName_choices","orgsta","origin_check",
                                            "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false","","");
                                        </script>
                                         &nbsp;&nbsp;&nbsp;&nbsp;<input class="BackgrndColorForTextBox" name="orgsta" value="${sedFilingForm.orgsta}"  id="orgsta" style="text-transform: uppercase" size="7" readonly="true" tabindex="-1"/>
                                    </td>
                                    <td><span class="mandatory">Routed Transaction</span></td>
                                    <td>
                                        <html:radio property="routed" value="Y" name="sedFilingForm" styleId="routed"/>Yes
                                        <html:radio property="routed" value="N" name="sedFilingForm" styleId="routed"/>No
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Destination Country</td>
                                    <td>
                                        <input class="textlabelsBoldForTextBox mandatory" name="destn"  value="${sedFilingForm.destn}"  id="destn"  style="text-transform: uppercase" size="25"/>
                                        <div id="sedDestination_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                        <input id="destn_check" type="hidden" value="${sedFilingForm.destn}" />
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("destn","sedDestination_choices","cntdes","destn_check",
                                            "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=2&isDojo=false","consigneeState()","");
                                        </script>
                                         &nbsp;&nbsp;&nbsp;&nbsp;<input class="BackgrndColorForTextBox" name="cntdes"  value="${sedFilingForm.cntdes}"  id="cntdes"  style="text-transform: uppercase" size="7" readonly="true" tabindex="-1"/>
                                    </td>
                                    <td><span class="nonMandatory">Related Companies</span></td>
                                    <td>
                                        <html:radio property="relate" value="Y" name="sedFilingForm" styleId="relate"/>Yes
                                        <html:radio property="relate" value="N" name="sedFilingForm" styleId="relate"/>No
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>POL</td>
                                    <td>
                                        <input  class="textlabelsBoldForTextBox mandatory" name="pol" value="${sedFilingForm.pol}" id="pol"  style="text-transform: uppercase" size="25"/>
                                        <div id="sedportofladding_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                        <input id="pol_check" type="hidden" value="${sedFilingForm.pol}" />
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("pol","sedportofladding_choices","exppnm","pol_check",
                                            "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=3&isDojo=false","","");
                                        </script>
                                        &nbsp;&nbsp;&nbsp;&nbsp;<input  class="BackgrndColorForTextBox" name="exppnm" value="${sedFilingForm.exppnm}" id="exppnm"  style="text-transform: uppercase" size="7" readonly="true" tabindex="-1"/>

                                    </td>
                                    <td><span class="nonMandatory">Waiver Notice</span></td>
                                    <td>
                                        <html:radio property="waiver" value="Y" name="sedFilingForm" styleId="waiver"/>Yes
                                        <html:radio property="waiver" value="N" name="sedFilingForm" styleId="waiver"/>No
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>POD</td>
                                    <td>
                                        <input  class="textlabelsBoldForTextBox mandatory" name="pod" value="${sedFilingForm.pod}" id="pod"  style="text-transform: uppercase" size="25"/>
                                        <div id="sedportofdischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                        <input id="pod_check" type="hidden" value="${sedFilingForm.pod}" />
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("pod","sedportofdischarge_choices","upptna","pod_check",
                                            "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=4&isDojo=false","","");
                                        </script>
                                            &nbsp;&nbsp;&nbsp;&nbsp;<input  class="BackgrndColorForTextBox" name="upptna" value="${sedFilingForm.upptna}" id="upptna"  style="text-transform: uppercase" size="7" readonly="true" tabindex="-1"/>
                                    </td>
                                    <td><span class="mandatory">Hazardous</span></td>
                                    <td>
                                        <html:radio property="hazard" value="Y"  name="sedFilingForm" styleId="hazard"/>Yes
                                        <html:radio property="hazard" value="N"  name="sedFilingForm" styleId="hazard"/>No
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Departure Date</td>
                                    <td>
                                        <html:text property="depDate" styleClass="textlabelsBoldForTextBox mandatory" styleId="txtcal3"
                                                   size="18" name="sedFilingForm" onchange="validateDate(this);" value="${sedFilingForm.depDate}"/>
                                        <c:if test="${mode != 'edit'}">
                                            <img src="${path}/img/CalendarIco.gif" alt="cal" id="cal3"
                                                 onmousedown="insertDateFromCalendar(this.id,0);" />
                                        </c:if>
                                    </td>
                                    <td><span class="nonMandatory">Mode Of Transportation</span></td>
                                    <td>
                                        <html:select property="modtrn" styleId="modtrn" styleClass="dropdown_accounting " name="sedFilingForm">
                                            <html:option value="10">Vessel</html:option>
                                            <html:option value="40">Air</html:option>
                                            <html:option value="20">Rail</html:option>
                                        </html:select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3">
                    <tr class="tableHeadingNew">
                        <td >Carrier/Vessel/Voyage &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Inbond</td>
                    </tr>
                    <tr>
                        <td>
                            <table  border="0" width="100%" cellspacing="0" cellpadding="2">
                                <tr class="textlabelsBold">
                                    <td>Conveyance &nbsp;&nbsp;
                                        <img src="${path}/img/icons/help-icon.gif" onmouseover="tooltip.show(showModtrn(),null,event);"
                                  onmouseout="tooltip.hide()"/>
                                    </td>
                                    <td>
                                        <input  class="textlabelsBoldForTextBox mandatory" name="vesnam" id="vesnam" value="${sedFilingForm.vesnam}" style="text-transform: uppercase"/>
                                        <input type="hidden" name="vesselname_check" id="vesselname_check" value="${sedFilingForm.vesnam}"/>
                                        <div id="vesselname_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("vesnam","vesselname_choices","vesnum","vesselname_check",
                                            "${path}/actions/getVesselName.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false","");
                                        </script>
                                    </td>
                                    <td>Inbond Number</td>
                                    <td><html:text property="inbnd" styleClass="textlabelsBoldForTextBox" name="sedFilingForm"  styleId="inbnd" onchange="enableFtZone()" maxlength="15" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Vessel Number</td>
                                    <td><html:text property="vesnum" styleClass="BackgrndColorForTextBox" styleId="vesnum" name="sedFilingForm" maxlength="6" readonly="true" tabindex="-1"/></td>
                                    <td>Entry#</td>
                                    <td><html:text property="inbent" styleClass="textlabelsBoldForTextBox" name="sedFilingForm" styleId="inbent" onkeyup="checkInbondNo(this)" maxlength="12" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Voyage Number</td>
                                    <td><html:text property="voyvoy" styleClass="textlabelsBoldForTextBox" styleId="voyvoy" name="sedFilingForm" maxlength="30" style="text-transform: uppercase"/></td>
                                    <td>Foreign Trade Zone</td>
                                    <td><html:text property="ftzone" styleClass="textlabelsBoldForTextBox" name="sedFilingForm" styleId="ftzone" onkeyup="checkInbondNo(this)" maxlength="7" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Carrier SCAC</td>
                                    <td><html:text property="scac" styleClass="textlabelsBoldForTextBox mandatory"  styleId="scac" name="sedFilingForm" maxlength="4" style="text-transform: uppercase"/></td>
                                    <td>Inbond Type</td>
                                    <td><html:select property="inbtyp" styleId="inbtyp" styleClass="dropdown_accounting" name="sedFilingForm">
                                            <html:option value="">Please Select</html:option>
                                            <html:option value="3">FTZ-TE</html:option>
                                            <html:option value="4">FTZ-IE</html:option>
                                            <html:option value="5">WW-TE</html:option>
                                            <html:option value="6">WW-IE</html:option>
                                        </html:select>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Unit Number</td>
                                    <td><html:text property="unitno" styleClass="textlabelsBoldForTextBox" styleId="unitno" name="sedFilingForm" 
                                               maxlength="13" style="text-transform: uppercase" onkeyup="formatUnitNo(this)"/></td>
                                    <td>Inbond Indicator</td>
                                    <td>
                                        <html:radio property="inbind" value="Y"  name="sedFilingForm" styleId="inbind"/>Yes
                                        <html:radio property="inbind" value="N"  name="sedFilingForm" styleId="inbind"/>No
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3" >
                    <tr class="tableHeadingNew">
                        <td>Contact Information</td>
                    </tr>
                    <tr>
                        <td>
                            <table  border="0" width="100%" cellspacing="0" cellpadding="2">
                                <tr class="textlabelsBold">
                                    <td  class="lab-sty">Shipper Contact</td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>First Name</td>
                                    <td><html:text property="expcfn" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expcfn" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                    <td>Last Name</td>
                                    <td><html:text property="expcln" styleClass="textlabelsBoldForTextBox"  styleId="expcln" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                    <td>Phone</td>
                                    <td><html:text property="expcpn" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expcpn" name="sedFilingForm" maxlength="10" style="text-transform: uppercase" onkeyup="cleanString(this)"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td  class="lab-sty">Consignee Contact</td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td >First Name</td>
                                    <td><html:text property="concfn" styleClass="textlabelsBoldForTextBox"  styleId="concfn" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                    <td>Last Name</td>
                                    <td><html:text property="concln" styleClass="textlabelsBoldForTextBox"  styleId="concln" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                    <td>Phone</td>
                                    <td><html:text property="concpn" styleClass="textlabelsBoldForTextBox"  styleId="concpn" name="sedFilingForm" maxlength="10" style="text-transform: uppercase" onkeyup="cleanString(this)"/></td>
                                </tr>
                                <%--<tr class="textlabelsBold">
                                    <td  class="lab-sty">Forwarder Contact</td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>First Name</td>
                                    <td><html:text property="forcfn" styleClass="textlabelsBoldForTextBox "  styleId="forcfn" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                    <td>Last Name</td>
                                    <td><html:text property="forcln" styleClass="textlabelsBoldForTextBox"  styleId="forcln" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                    <td>Phone</td>
                                    <td><html:text property="forcpn" styleClass="textlabelsBoldForTextBox "  styleId="forcpn" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                </tr>--%>
                            </table>
                        </td>
                    </tr>
                 </table>
            </td>
        </tr>
</table>
<html:hidden property="buttonValue"/>
<html:hidden property="expctry" name="sedFilingForm" styleId="expctry"/>
<%--<html:hidden property="consta" name="sedFilingForm" styleId="consta"/>--%>
<html:hidden property="forctry" name="sedFilingForm" styleId="forctry"/>
</html:form>
         <script language="javascript" type="text/javascript">
             function confirmMessageFunction(id1,id2){
                 if(id1=="clearForwarder" && id2=="yes"){
                    clearForwarder();
                 }else if(id1=="clearConsignee" && id2=="yes"){
                    clearConsignee();
                 }else if(id1=="clearShipper" && id2=="yes"){
                    clearShipper();
                 }else if(id1=="clearForwarder" && id2=="no"){
                    document.getElementById("forwarderCheck").checked=false;
                 }else if(id1=="clearConsignee" && id2=="no"){
                    document.getElementById("consigneeCheck").checked=false;
                 }else if(id1=="clearShipper" && id2=="no"){
                    document.getElementById("shipperCheck").checked=false;
                 }else if(id1=="uncheckClearForwarder" && id2=="yes"){
                    clearForwarder();
                 }else if(id1=="uncheckClearConsignee" && id2=="yes"){
                    clearConsignee();
                 }else if(id1=="uncheckClearShipper" && id2=="yes"){
                    clearShipper();
                 }else if(id1=="uncheckClearForwarder" && id2=="no"){
                    document.getElementById("forwarderCheck").checked=true;
                 }else if(id1=="uncheckClearConsignee" && id2=="no"){
                    document.getElementById("consigneeCheck").checked=true;
                 }else if(id1=="uncheckClearShipper" && id2=="no"){
                    document.getElementById("shipperCheck").checked=true;
                 }
             }
     function validateDate(data) {
          if(data.value!=""){
              data.value = data.value.getValidDateTime("/","",false);
              if(data.value==""||data.value.length>10){
                  alertNew("Please enter valid date");
                  data.value="";
                  document.getElementById(data.id).focus();
              }
              yearValidation(data);
          }
      }
             function clearConsignee(){
                document.getElementById("connam").value="";
                document.getElementById("connum").value="";
                document.getElementById("conadd").value="";
                document.getElementById("concty").value="";
                document.getElementById("conctry").value="";
                document.getElementById("consta").value="";
                document.getElementById("conpst").value="";
             }
             function clearForwarder(){
                document.getElementById("frtnam").value="";
                document.getElementById("frtnum").value="";
                document.getElementById("frtadd").value="";
                document.getElementById("frtcty").value="";
                document.getElementById("frtsta").value="";
                document.getElementById("frtzip").value="";
                document.getElementById("frtirs").value="";
                document.getElementById("frtirsTp").value="";
                document.getElementById("frticd").value="";
                document.getElementById("frtirs").className = "textlabelsBoldForTextBox";
                document.getElementById("frtirs").readOnly = false;
                document.getElementById("frtirs").tabIndex=0;
             }
             function clearShipper(){
                document.getElementById("expnam").value="";
                document.getElementById("expnum").value="";
                document.getElementById("expadd").value="";
                document.getElementById("expcty").value="";
                document.getElementById("expsta").value="";
                document.getElementById("expzip").value="";
                document.getElementById("expirs").value="";
                document.getElementById("expirsTp").value="";
                document.getElementById("expicd").value="";
                document.getElementById("expirs").className = "textlabelsBoldForTextBox mandatory";
                document.getElementById("expirs").readOnly = false;
                document.getElementById("expirs").tabIndex=0;
             }
             function showModtrn(){
                 if(document.getElementById('modtrn').value == '10'){
                     return "Vessel";
                 }else if(document.getElementById('modtrn').value == '20'){
                     return "Rail";
                 }else {
                     return "Air";
                 }
             }
             function cleanString (obj) {
                 var val;
                 val=obj.value.replace(/[\(\)\.\-\s,]+/g, "");
                 obj.value = val;   
             }
             function updateAesDetails(){    
                if (document.getElementById('concpn').value != "" && document.getElementById('concpn').value.length <10) {
                           alertNew("Phone Number should be 10 Digits");
                           document.getElementById("concpn").value = "";
                           document.getElementById("concpn").focus();
                            return false;
                    }
                    if (document.getElementById('expcpn').value != "" && document.getElementById('expcpn').value.length < 10) {
                            alertNew("Phone Number should be 10 Digits");
                            document.getElementById("expcpn").value = "";
                           document.getElementById("expcpn").focus();
                            return false;
                    }
                    if(document.getElementById('CONTYP').value === ""){
                           alertNew("Select CONTYP in ULTIMATE CONSIGNEE"); 
                           return false;
                    }
                    document.sedFilingForm.buttonValue.value = "updateAes";
                    document.sedFilingForm.submit();
            }
            function saveAesDetails() {
                    if (document.getElementById('expcpn').value != "" && document.getElementById('expcpn').value.length < 10) {
                            alertNew("Phone Number should be 10 Digits");
                            document.getElementById("expcpn").value = "";
                           document.getElementById("expcpn").focus();
                            return false;
                    }
                     if (document.getElementById('concpn').value != "" && document.getElementById('concpn').value.length <10) {
                           alertNew("Phone Number should be 10 Digits");
                           document.getElementById("concpn").value = "";
                           document.getElementById("concpn").focus();
                            return false;
                    }
                    if(document.getElementById('CONTYP').value === ""){
                           alertNew("Select CONTYP in ULTIMATE CONSIGNEE"); 
                           return false;
                    }
                    document.sedFilingForm.buttonValue.value = "saveAes";
                    document.sedFilingForm.submit();
            }
            function setBlSslBoookingNoToAes(){
                document.getElementById("bkgnum").value =parent.parent.document.getElementById("booking").value.toUpperCase().replace(" ", "");
            }

         </script>
</body>
<script>
changeSelectBoxOnViewMode();
setBlSslBoookingNoToAes();
</script>
</html>
