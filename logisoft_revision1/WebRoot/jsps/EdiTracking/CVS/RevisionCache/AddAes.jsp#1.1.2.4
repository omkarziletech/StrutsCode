<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ include file="../includes/jspVariables.jsp" %>
<%@ include file="../includes/resources.jsp" %>
<%@ include file="../includes/baseResources.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <base href="${basePath}">
        <title>My JSP 'SedFiling.jsp' starting page</title>
        <link rel="stylesheet" href="<c:url value="/css/script-aculo-us.tabs.css"/>" title="script-aculo-us.tabs">
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
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    </head>
    <body class="whitebackgrnd" onload="gotoSelectedTab('${mode}')">
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
        <html:form action="/sedFiling" type="com.gp.cong.logisoft.struts.form.SedFilingForm" name="sedFilingForm" styleId="sedFilingPage" scope="request">
            <table class="tableBorderNew">
                <tr class="textlabels">
                    <td class="textlabelsBold">Entered By :
                        <b class="headerlabel">
                            <c:if test="${!empty sessionScope.loginuser}">
                                <c:out value="${sessionScope.loginuser.loginName}"/>
                            </c:if>
                        </b>
                    </td>
                    <td class="textlabelsBold">On :
                        <b class="headerlabel">
                            <jsp:useBean id="date" class="java.util.Date" />
                            <fmt:formatDate value="${date}" pattern="dd-MMM-yyyy HH:mm"/>
                        </b>
                    </td>
                    <td class="textlabelsBold">Status :
                        <b class="headerlabel">
                            <c:choose>
                                <c:when test="${status == 'S'}">
                                    <c:out value="Sent"/>
                                </c:when>
                                <c:when test="${status == 'C'}">
                                    <c:out value="Complete"/>
                                </c:when>
                                <c:when test="${status == 'E'}">
                                    <c:out value="Error"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="New"/>
                                </c:otherwise>
                            </c:choose>
                        </b>
                    </td>
                </tr>
            </table>
                <table class="tableBorderNew" border="0" width="100%" cellspacing="3" cellpadding="0">
                    <tr><td>
                            <table border="0" width="100%" cellspacing="3" cellpadding="0">
                                <tr>
                                    <td>
                                        <table border="0" width="100%" cellspacing="3" cellpadding="0">
                                            <tr>
                                                <td>
                                                    <table cellpadding="0" cellspacing="0" border="0" class="tableBorderNew" width="100%">
                                                        <tr class="tableHeadingNew"><td colspan="2">SHIPPER<font style="color: red">*</font></td></tr>
                                                        <tr>
                                                            <td>
                                                                <table>
                                                                    <tr class="textlabelsBold">
                                                                        <td>Name</td>
                                                                        <td>
                                                                            <input name="expnam" value="${sedFilingForm.expnam}" size="30"
                                                                                   id="expnam" class="textlabelsBoldForTextBox mandatory"  style="text-transform: uppercase"/>
                                                                            <input id="houseName_check"  type="hidden" value="${sedFilingForm.expnam}"/>
                                                                            <div id="shipperMaster_choices"  style="display:none;" class="autocomplete"></div>
                                                                            <script type="text/javascript">
                                                                                initAutocompleteWithFormClear("expnam","shipperMaster_choices","expnum","houseName_check",
                                                                                "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=3","getMasterShipperInfo()","");
                                                                            </script>
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="textlabelsBold">
                                                                        <td>Number</td>
                                                                        <td><html:text property="expnum" size="30" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expnum" name="sedFilingForm" maxlength="10" style="text-transform: uppercase"/></td>
                                                                    </tr>
                                                                    <tr class="textlabelsBold">
                                                                        <td>Address</td>
                                                                        <td><html:textarea property="expadd" styleClass="textlabelsBoldForTextBox mandatory" name="sedFilingForm" styleId="expadd" cols="30" rows="3" style="text-transform: uppercase"/></td>
                                                                    </tr>
                                                                    <tr class="textlabelsBold">
                                                                        <td>City</td>
                                                                        <td><html:text property="expcty" size="30" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expcty" name="sedFilingForm" maxlength="30" style="text-transform: uppercase"/></td>
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
                                                                        <td>First Name</td>
                                                                        <td><html:text property="expcfn" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expcfn" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                                                    </tr>
                                                                    <tr class="textlabelsBold">
                                                                        <td>Last Name</td>
                                                                        <td><html:text property="expcln" styleClass="textlabelsBoldForTextBox"  styleId="expcln" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                                                    </tr>
                                                                    <tr class="textlabelsBold">
                                                                        <td>Phone</td>
                                                                        <td><html:text property="expcpn" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expcpn" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                                                    </tr>
                                                                    <tr class="textlabelsBold">
                                                                        <td>IRS Number</td>
                                                                        <td><html:text property="expirs" styleClass="textlabelsBoldForTextBox mandatory"  styleId="expirs" name="sedFilingForm" maxlength="13" onchange="validateIrs(this)" style="text-transform: uppercase"/></td>
                                                                    </tr>
                                                                    <tr class="textlabelsBold">
                                                                        <td>IRS Code</td>
                                                                        <td>
                                                                            <html:select property="expicd" styleId="expicd" styleClass="textlabelsBoldForTextBox" name="sedFilingForm">
                                                                                <html:option value="E">EIN</html:option>
                                                                                <html:option value="S">SSN</html:option>
                                                                                <html:option value="N">NONE</html:option>
                                                                            </html:select>
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="textlabelsBold">
                                                                        <td>Power Of Attorney</td>
                                                                        <td>
                                                                            <html:radio property="exppoa" styleId="exppoa" name="sedFilingForm" value="Y"/>Y
                                                                            <html:radio property="exppoa" styleId="exppoa" name="sedFilingForm" value="N"/>N
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>

                                                    </table>
                                                </td>
                                            </tr>

                                            <tr>
                                                <td>
                                                    <table border="0" width="100%" cellspacing="0" cellpadding="0" class="tableBorderNew" style="">
                                                        <tr class="tableHeadingNew"><td colspan="2">CONSIGNEE<font style="color: red">*</font></td></tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Name</td>
                                                            <td>
                                                                <%--<html:text property="connam" styleClass="textlabelsBoldForTextBox"  styleId="connam" name="sedFilingForm" maxlength="40"/>--%>
                                                                <input  class="textlabelsBoldForTextBox mandatory" name="connam" id="connam"
                                                                        value="${sedFilingForm.connam}"  style="text-transform: uppercase"/>
                                                                <input id="houseConsigneeName_check" type="hidden" value="${sedFilingForm.connam}" />
                                                                <div id="shipperMaster_choices"  style="display:none;" class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    initAutocompleteWithFormClear("connam","shipperMaster_choices","connum","houseConsigneeName_check",
                                                                    "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=4","getMasterConsigneeInfo()","");
                                                                </script>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Number</td>
                                                            <td><html:text property="connum" styleClass="textlabelsBoldForTextBox mandatory"  styleId="connum" name="sedFilingForm" maxlength="10" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Address</td>
                                                            <td><html:textarea property="conadd" styleClass="textlabelsBoldForTextBox mandatory"  styleId="conadd" name="sedFilingForm" cols="21" rows="3" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>City</td>
                                                            <td><html:text property="concty" styleClass="textlabelsBoldForTextBox mandatory"  styleId="concty" name="sedFilingForm" maxlength="30" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>State</td>
                                                            <td><html:text property="consta" styleClass="textlabelsBoldForTextBox"  styleId="consta" name="sedFilingForm" maxlength="2" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Zip</td>
                                                            <td><html:text property="conpst" styleClass="textlabelsBoldForTextBox"  styleId="conpst" name="sedFilingForm" maxlength="5" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>First Name</td>
                                                            <td><html:text property="concfn" styleClass="textlabelsBoldForTextBox"  styleId="concfn" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Last Name</td>
                                                            <td><html:text property="concln" styleClass="textlabelsBoldForTextBox"  styleId="concln" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Phone</td>
                                                            <td><html:text property="concpn" styleClass="textlabelsBoldForTextBox"  styleId="concpn" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Power Of Attorney</td>
                                                            <td>
                                                                <html:radio property="conpoa" styleId="" name="sedFilingForm" value="Y"/>Yes
                                                                <html:radio property="conpoa" styleId="" name="sedFilingForm" value="N"/>No
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table border="0" width="100%" cellspacing="0" cellpadding="0" class="tableBorderNew">
                                                        <tr class="tableHeadingNew"><td colspan="2">FORWARDER</td></tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Name</td>
                                                            <td>
                                                                <%--<html:text property="frtnam" styleClass="textlabelsBoldForTextBox"  styleId="frtnam" name="sedFilingForm" maxlength="40"/>--%>
                                                                <input name="frtnam" id="frtnam" value="${sedFilingForm.frtnam}"
                                                                       class="textlabelsBoldForTextBox" style="text-transform: uppercase" />
                                                                <input id="forwardingAgentName_check" type="hidden" value="${sedFilingForm.frtnam}" />
                                                                <div id="shipperMaster_choices"  style="display:none;" class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    initAutocompleteWithFormClear("frtnam","shipperMaster_choices","frtnum","forwardingAgentName_check",
                                                                    "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=10","getForwardingInfo()","");
                                                                </script>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Number</td>
                                                            <td><html:text property="frtnum" styleClass="textlabelsBoldForTextBox"  styleId="frtnum" name="sedFilingForm" maxlength="10" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Address</td>
                                                            <td><html:textarea property="frtadd" styleClass="textlabelsBoldForTextBox"  styleId="frtadd" name="sedFilingForm" cols="21" rows="3" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>City</td>
                                                            <td><html:text property="frtcty" styleClass="textlabelsBoldForTextBox"  styleId="frtcty" name="sedFilingForm" maxlength="30" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>State</td>
                                                            <td><html:text property="frtsta" styleClass="textlabelsBoldForTextBox"  styleId="frtsta" name="sedFilingForm" maxlength="2" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Zip</td>
                                                            <td><html:text property="frtzip" styleClass="textlabelsBoldForTextBox"  styleId="frtzip" name="sedFilingForm" maxlength="20" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>IRS Number</td>
                                                            <td><html:text property="frtirs" styleClass="textlabelsBoldForTextBox"  styleId="frtirs" name="sedFilingForm" maxlength="13" onchange="validateIrs(this)" style="text-transform: uppercase"/></td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>IRS Code</td>
                                                            <td>
                                                                <html:select property="frticd" styleId="frticd" styleClass="textlabelsBoldForTextBox" name="sedFilingForm">
                                                                    <html:option value="E">EIN</html:option>
                                                                    <html:option value="S">SSN</html:option>
                                                                    <html:option value="N">DUNS</html:option>
                                                                </html:select>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td valign="top">
                                        <table border="0" width="100%" cellspacing="3" cellpadding="0">
                                            <tr>
                                                <td>
                                                    <table border="0" width="100%" cellspacing="0" cellpadding="0" class="tableBorderNew">
                                                        <tr class="tableHeadingNew">
                                                            <td colspan="4">Trade Route</td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Destination</td>
                                                            <td>
                                                                <%--<html:text property="cntdes" styleClass="textlabelsBoldForTextBox mandatory"  styleId="cntdes" name="sedFilingForm"  maxlength="2"/>--%>
                                                                <input class="BackgrndColorForTextBox mandatory" name="cntdes"  value="${sedFilingForm.cntdes}"  id="cntdes"  style="text-transform: uppercase" readonly/>
                                                                <div id="finalDestination_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    initOPSAutocomplete("cntdes","finalDestination_choices","","",
                                                                    "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=2&isDojo=false","");
                                                                </script>
                                                            </td>
                                                            <td>Routed Transaction</td>
                                                            <td>
                                                                <html:radio property="routed" value="Y" name="sedFilingForm" styleId="routed"/>Yes
                                                                <html:radio property="routed" value="N" name="sedFilingForm" styleId="routed"/>No
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Origin</td>
                                                            <td>
                                                                <%--<html:text property="orgsta" styleClass="textlabelsBoldForTextBox mandatory"  styleId="orgsta" name="sedFilingForm" maxlength="2"/>--%>
                                                                <input class="BackgrndColorForTextBox mandatory" name="orgsta" value="${sedFilingForm.orgsta}"  id="orgsta" style="text-transform: uppercase" readonly/>
                                                                <div id="terminalName_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    initOPSAutocomplete("orgsta","terminalName_choices","","",
                                                                    "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false","");
                                                                </script>
                                                            </td>
                                                            <td>Related Companies</td>
                                                            <td>
                                                                <html:radio property="relate" value="Y" name="sedFilingForm" styleId="relate"/>Yes
                                                                <html:radio property="relate" value="N" name="sedFilingForm" styleId="relate"/>No
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>POL</td>
                                                            <td>
                                                                <%--<html:text property="exppnm" styleClass="textlabelsBoldForTextBox mandatory"  styleId="exppnm" name="sedFilingForm" maxlength="5"/>--%>
                                                                <input  class="BackgrndColorForTextBox mandatory" name="exppnm" value="${sedFilingForm.exppnm}" id="exppnm"  style="text-transform: uppercase" readonly/>
                                                                <div id="portofladding_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    initOPSAutocomplete("exppnm","portofladding_choices","","",
                                                                    "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=3&isDojo=false","");
                                                                </script>
                                                            </td>
                                                            <td>Waiver Notice</td>
                                                            <td>
                                                                <html:radio property="waiver" value="Y" name="sedFilingForm" styleId="waiver"/>Yes
                                                                <html:radio property="waiver" value="N" name="sedFilingForm" styleId="waiver"/>No
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>POD</td>
                                                            <td>
                                                                <%--<html:text property="upptna" styleClass="textlabelsBoldForTextBox mandatory"  styleId="upptna" name="sedFilingForm" maxlength="5"/>--%>
                                                                <input  class="BackgrndColorForTextBox mandatory" name="upptna" value="${sedFilingForm.upptna}" id="upptna"  style="text-transform: uppercase" readonly/>
                                                                <div id="portofdischarge_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    initOPSAutocomplete("upptna","portofdischarge_choices","","",
                                                                    "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=4&isDojo=false","");
                                                                </script>
                                                            </td>
                                                            <td>Hazardous</td>
                                                            <td>
                                                                <html:radio property="hazard" value="Y"  name="sedFilingForm" styleId="hazard"/>Yes
                                                                <html:radio property="hazard" value="N"  name="sedFilingForm" styleId="hazard"/>No
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Departure Date</td>
                                                            <td>
                                                                <html:text property="depDate" styleClass="textlabelsBoldForTextBox mandatory" onchange="validateDate(this);" styleId="txtcal3"
                                                                           size="18" value="${sedFilingForm.depDate}"/>
                                                                <c:if test="${mode != 'edit'}">
                                                                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="cal3"
                                                                         onmousedown="insertDateFromCalendar(this.id,0);" />
                                                                </c:if>
                                                            </td>
                                                            <td>Mode Of Transportation</td>
                                                            <td>
                                                                <html:select property="modtrn" styleId="modtrn" styleClass="textlabelsBoldForTextBox " name="sedFilingForm">
                                                                    <html:option value="10">Vessel</html:option>
                                                                    <html:option value="40">Air</html:option>
                                                                </html:select>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table border="0" width="100%" cellspacing="0" cellpadding="0" class="tableBorderNew">
                                                        <tr class="tableHeadingNew">
                                                            <td colspan="2">Carrier/Vessel/Voyage</td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Vessel Name</td>
                                                            <td>
                                                                <input  class="textlabelsBoldForTextBox mandatory" name="vesnam" id="vesnam" value="${sedFilingForm.vesnam}" style="text-transform: uppercase"/>
                                                                <input type="hidden" name="vesselname_check" id="vesselname_check" />
                                                                <div id="vesselname_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    initAutocompleteWithFormClear("vesnam","vesselname_choices","vesnum","vesselname_check",
                                                                    "${path}/actions/getVesselName.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false","");
                                                                </script>
                                                                <%--<html:text property="vesnam" styleClass="textlabelsBoldForTextBox" styleId="vesnam" name="sedFilingForm" maxlength="30"/>--%>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Vessel Number</td>
                                                            <td>
                                                                <html:text property="vesnum" styleClass="BackgrndColorForTextBox" styleId="vesnum" name="sedFilingForm" maxlength="6" readonly="true"/>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Voyage Number</td>
                                                            <td>
                                                                <html:text property="voyvoy" styleClass="textlabelsBoldForTextBox mandatory" styleId="voyvoy" name="sedFilingForm" maxlength="4" style="text-transform: uppercase"/>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Voyage Suffix</td>
                                                            <td>
                                                                <html:text property="voysuf" styleClass="textlabelsBoldForTextBox" styleId="voysuf" name="sedFilingForm" maxlength="1" style="text-transform: uppercase"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textlabelsBold">Carrier SCAC</td>
                                                            <td>
                                                                <html:text property="scac" styleClass="textlabelsBoldForTextBox mandatory"  styleId="scac" name="sedFilingForm" maxlength="4" style="text-transform: uppercase"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                 <td>
                                                    <table border="0" width="100%" cellspacing="0" cellpadding="0" class="tableBorderNew">
                                                        <tr class="tableHeadingNew">
                                                            <td colspan="4">General Information</td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>ITN#</td>
                                                            <td>
                                                                <html:text property="itn" styleClass="BackgrndColorForTextBox" styleId="itn" name="sedFilingForm" maxlength="40" readonly="true"/>
                                                            </td>
                                                            <td>Shipment/DR#</td>
                                                            <td>
                                                                <html:text property="shpdr" styleClass="BackgrndColorForTextBox" styleId="shpdr" name="sedFilingForm" maxlength="8" readonly="true"/>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Transaction Ref</td>
                                                            <td>
                                                                <html:text property="trnref" styleClass="textlabelsBoldForTextBox" styleId="trnref" name="sedFilingForm" maxlength="17"/>
                                                            </td>
                                                            <td>BL NUM#</td>
                                                            <td>
                                                                <html:text property="blnum" styleClass="BackgrndColorForTextBox" styleId="blnum" name="sedFilingForm"  maxlength="17" readonly="true"/>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>SSL BKG#</td>
                                                            <td>
                                                                <html:text property="bkgnum" styleClass="BackgrndColorForTextBox" styleId="bkgnum" name="sedFilingForm" maxlength="30" readonly="true"/>
                                                            </td>
                                                            <td>Email</td>
                                                            <td>
                                                                <html:text property="email" styleClass="textlabelsBoldForTextBox mandatory" styleId="email" name="sedFilingForm" maxlength="38"/>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Inbond Number</td>
                                                            <td>
                                                                <html:text property="inbnd" styleClass="textlabelsBoldForTextBox" name="sedFilingForm"  styleId="inbnd" onchange="enableFtZone()" maxlength="15"/>
                                                            </td>
                                                            <td>Inbond Indicator</td>
                                                            <td>
                                                                <html:text property="inbind" styleClass="textlabelsBoldForTextBox" name="sedFilingForm" styleId="inbind" maxlength="1"/>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Foreign Trade</td>
                                                            <td>
                                                                <html:text property="ftzone" styleClass="textlabelsBoldForTextBox" name="sedFilingForm" styleId="ftzone" onkeydown="checkInbondNo()" maxlength="5"/>
                                                            </td>
                                                            <td>Inbond Type</td>
                                                            <td>
                                                                <html:text property="inbtyp" styleClass="textlabelsBoldForTextBox" styleId="inbtyp" name="sedFilingForm" maxlength="1"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table border="0" width="100%" cellspacing="0" cellpadding="0" class="tableBorderNew">
                                                        <tr class="tableHeadingNew">
                                                            <td colspan="2">Status</td>
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
                                                        <tr class="textlabelsBold">
                                                            <td>User Changed</td>
                                                            <td>
                                                                <html:text property="stsusr" styleClass="textlabelsBoldForTextBox" styleId="stsusr" size="18" name="sedFilingForm" maxlength="8"/>
                                                            </td>
                                                        </tr>
                                                        <tr class="textlabelsBold">
                                                            <td>Date Changed</td>
                                                            <td>
                                                                <html:text property="stsDate" styleClass="textlabelsBoldForTextBox" styleId="txtcal4"
                                                                           size="18" readonly="true" name="sedFilingForm" value="${sedFilingForm.stsDate}"/>
                                                                <c:if test="${mode != 'edit'}">
                                                                    <img src="${path}/img/CalendarIco.gif" alt="cal4" id="cal4"
                                                                         onmousedown="insertDateFromCalendar(this.id,3);" />
                                                                </c:if>
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
                </table>
            <html:hidden property="buttonValue" value="${sedFilingForm.buttonValue}"/>
            <html:hidden property="schedId" value="${sedFilingForm.schedId}"/>
        </html:form>
    </body>
</html>
