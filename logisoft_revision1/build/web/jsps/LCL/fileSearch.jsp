<%-- 
    Document   : fileSearch
    Created on : Nov 7, 2011, 3:46:35 PM
    Author     : Thamizh
--%>

<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,java.text.SimpleDateFormat,com.gp.cong.logisoft.bc.notes.NotesConstants"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.domain.User,java.util.*,com.gp.cvst.logisoft.hibernate.dao.QuotationDAO,com.gp.cong.logisoft.bc.ratemanagement.PortsBC,org.apache.commons.lang3.StringUtils"%>
<%@ page import="com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO"%>
<jsp:directive.page import="com.gp.cong.common.CommonConstants"/>
<jsp:directive.page import="java.text.DateFormat"/>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.QuotationConstants"/>
<jsp:directive.page import="com.gp.cong.logisoft.util.StringFormatter"/>
<jsp:directive.page import="com.logiware.constants.ItemConstants"/>
<%@ page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<bean:define id="fileNumberPrefix" type="String">
    <bean:message key="fileNumberPrefix"/>
</bean:define>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html:html locale="true">
    <head>
        
        <title> Search Quotation</title>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/ProcessInfoBC.js'></script>
        <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
        <script type='text/javascript' src='${path}/dwr/interface/QuoteDwrBC.js'></script>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
        <script type="text/javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/autocomplete.js"></script>
        <script type="text/javascript" src="<%=path%>/js/fcl/fileNumber.js"></script>
        <script type="text/javascript" src="<%=path%>/js/tooltip/tooltip.js" ></script>
        <script language="javascript" src="${path}/js/rates.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/calendar.js" ></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
        

    <body class="whitebackgrnd">

        
        <html:form action="/searchquotation?accessMode=${param.accessMode}" name="searchLCLFileForm"
                   type="com.gp.cvst.logisoft.struts.form.SearchQuotationForm" scope="request">
            <html:hidden property="buttonValue"/>
            <html:hidden property="index"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <%if(displayId==null){ %>
                        <table width="100%"  border="0" cellpadding="0" cellspacing="0"  class="tableBorderNew">
                           
                            <tr>
                                <td colspan="2">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="2" >
                                        <tr class="textlabelsBold">
                                            <td width="7%">File No</td>
                                            <td valign="top">
                                                <html:text property="fileNumber" size="15" styleClass="textlabelsBoldForTextBox"/>
                                            </td>
                                            <td  width="7%"> Origin</td>
                                            <td>
                                                <input name="pol" id="pol" size="15"  maxlength="12" class="textlabelsBoldForTextBox"/>
                                                <div id="pol_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="te xt/javascript">
                                                    initOPSAutocomplete("pol","pol_choices","","",
                                                    "<%=path%>/actions/getUnlocationCode.jsp?tabName=QUOTE&from=6&isDojo=false&countryflag=false","");
                                                </script>
                                            </td>
                                            <td  width="7%">From Date<br></td>
                                            <td>
                                                <div class="foat-left">
                                                    <c:choose>
                                                        <c:when test="${empty searchQuotationForm.quotestartdate}">
                                                            <html:text property="quotestartdate" readonly="true" value="<%=fromDate%>"  styleId="txtcalbb" size="14" styleClass="textlabelsBoldForTextBox float-left"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:text property="quotestartdate" value="${searchQuotationForm.quotestartdate}"
                                                                       styleId="txtcalbb" size="15" styleClass="textlabelsBoldForTextBox float-left"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="middle" id="calbb" class="calendar-img"
                                                         onmousedown="insertDateFromCalendar(this.id,3);" />
                                                </div>
                                            </td>
                                            <td>To Date<br></td>
                                            <td>
                                                <div class="float-left">
                                                    <c:choose>
                                                        <c:when test="${empty searchQuotationForm.toDate}">
                                                            <html:text property="toDate" value="<%=toDate%>" readonly="true" styleId="txtcalToyy" size="13" styleClass="textlabelsBoldForTextBox float-left"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:text property="toDate" value="${searchQuotationForm.toDate}" styleId="txtcalToyy" size="13" styleClass="textlabelsBoldForTextBox float-left"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="middle" id="calToyy" class="calendar-img"
                                                         onmousedown="insertDateFromCalendar(this.id,3);"  />
                                                </div>
                                            </td>
                                        </tr>

                                        <tr class="textlabelsBold">
                                            <td>Client
                                                <input type="checkbox" name="clientCheckbox" id="clientCheckbox" onclick="getClient()" onmouseover="tooltip.show('<strong>Disable dojo</strong>');" onmouseout="tooltip.hide();"><br></td>
                                            <td>
                                                <input name="client" id="client"  size="15"  class="textlabelsBoldForTextBox"/>
                                                <input name="client_check" id="client_check"   type="hidden"
                                                       value="${searchQuotationForm.client}" Class="textlabelsBoldForTextBox"/>
                                                <input name="clientNumber" id="clientNumber"   type="hidden" Class="textlabelsBoldForTextBox"/>
                                                <div id="client_choices" style="display: none" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("client","client_choices","clientNumber","",
                                                    "<%=path%>/actions/getAccountDetails.jsp?tabName=QUOTE&from=5","");
                                                </script>
                                                <br></td>
                                            <td width="7%">POL<br></td>
                                            <td><input name="plor" id="plor" size="15"      maxlength="12" class="textlabelsBoldForTextBox"/>
                                                <div id="plor_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("plor","plor_choices","","",
                                                    "<%=path%>/actions/getUnlocationCode.jsp?tabName=QUOTE&from=4&isDojo=false","");
                                                </script>
                                                <br></td>
                                            <td>Issuing Terminal<br></td><td>
                                                <input type="text" name="issuingTerminal"  id="issuingTerminal" size="18" class="textlabelsBoldForTextBox"/>
                                                <div id="issuingTerminal_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("issuingTerminal","issuingTerminal_choices","","",
                                                    "<%=path%>/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false","");
                                                </script>

                                                <br></td>
                                            <td> Container No<br></td>
                                            <td ><html:text property="container" styleId="txtcal1" size="17" styleClass="textlabelsBoldForTextBox"/>
                                                <br></td>

                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Shipper<br></td>
                                            <td valign="top">
                                                <input name="shipper" id="shipper" size="15" class="textlabelsBoldForTextBox"/>
                                                <input name="shipper_check" id="shipper_check"   type="hidden"
                                                       value="${searchQuotationForm.shipper}" Class="textlabelsBoldForTextBox"/>
                                                <input name="shipperNumber" id="shipperNumber"   type="hidden" Class="textlabelsBoldForTextBox"/>
                                                <div id="shipper_choices" style="display: none"
                                                     class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("shipper","shipper_choices","shipperNumber","shipper_check",
                                                    "<%=path%>/actions/getAccountDetails.jsp?tabName=QUOTE&from=6",
                                                    "");
                                                </script>
                                                <br></td>
                                            <td width="7%">POD<br></td>
                                            <td><input name="plod" id="plod" size="15"    maxlength="12"
                                                       class="textlabelsBoldForTextBox"/>
                                                <div id="plod_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("plod","plod_choices","","",
                                                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=9&isDojo=false","");
                                                </script>

                                                <br></td>
                                            <td>Origin Region<br></td>
                                            <td>
                                                <html:select property="originRegion" style="width:115px"  styleClass="dropdown_accounting"  onchange="">
                                                    <html:option value="0">Select One</html:option>
                                                    <html:optionsCollection name="regions"/>
                                                </html:select>
                                                <br></td>
                                            <td>SSL<br></td>
                                            <td>
                                                <input Class="textlabelsBoldForTextBox" name="carrier"
                                                       id="carrier"    style="width:110px;"/>
                                                <input Class="textlabelsBoldForTextBox"  name="carrier_check" id="carrier_check"
                                                       type="hidden" style="display: none;" value="${searchQuotationForm.carrier}" />
                                                <div id="carrier_choices" style="display: none" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("carrier","carrier_choices","sslcode","carrier_check",
                                                    "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=4","");
                                                </script>

                                                <br></td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Forwarder
                                                <input type="hidden" style="display: none;" name="sslcode" id="sslcode"/>

                                                <br></td>
                                            <td valign="top">
                                                <input name="forwarder" id="forwarder"  size="15" class="textlabelsBoldForTextBox"/>
                                                <input name="forwarder_check" id="forwarder_check"   type="hidden"
                                                       value="${searchQuotationForm.forwarder}" Class="textlabelsBoldForTextBox"/>
                                                <input name="forwarderNumber" id="forwarderNumber"   type="hidden" Class="textlabelsBoldForTextBox"/>
                                                <div id="forwarder_choices" style="display: none"
                                                     class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("forwarder","forwarder_choices","forwarderNumber","forwarder_check",
                                                    "<%=path%>/actions/getAccountDetails.jsp?tabName=QUOTE&from=7",
                                                    "");
                                                </script>
                                                <br></td>
                                            <td>Destination<br></td>
                                            <td><input name="pod" id="pod" size="15"   maxlength="12" class="textlabelsBoldForTextBox"/>
                                                <input name="pod_check" id="pod_check_id"  type="hidden">
                                                <div id="pod_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>

                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("pod","pod_choices","","pod_check",
                                                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=1&isDojo=false","");
                                                </script>
                                                <br></td>
                                            <td>Destination Region<br></td>
                                            <td>
                                                <html:select property="destinationRegion" style="width:115px"  styleClass="dropdown_accounting">
                                                    <html:option value="0">Select One</html:option>
                                                    <html:optionsCollection name="regions"/>
                                                </html:select>
                                                <br></td>
                                            <td>Created By<br></td>
                                            <td>
                                                <input type="text" name="quoteBy"  id="quoteBy"   style="width:110px;" class="textlabelsBoldForTextBox"/>
                                                <input type="checkbox" id="loginCheck"  onclick="getLoginName('<%=loginName%>')"/>Me
                                                <div id="quoteBy_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("quoteBy","quoteBy_choices","","",
                                                    "<%=path%>/actions/getUserDetails.jsp?tabName=SEARCH_FILE&from=0&isDojo=false","");
                                                </script>
                                                <br></td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Consignee<br></td>
                                            <td><input  name="conginee" id="conginee" class="textlabelsBoldForTextBox"  styleId="txtcal1" size="15" />
                                                <input name="conginee_check" id="conginee_check"   type="hidden"
                                                       value="${searchQuotationForm.conginee}" Class="textlabelsBoldForTextBox"/>
                                                <input name="congineeNumber" id="congineeNumber"   type="hidden" Class="textlabelsBoldForTextBox"/>
                                                <div id="conginee_choices" style="display: none"
                                                     class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("conginee","conginee_choices","congineeNumber","conginee_check",
                                                    "<%=path%>/actions/getAccountDetails.jsp?tabName=QUOTE&from=8",
                                                    "");
                                                </script>
                                                <br></td>
                                            <td>Filter By<br></td>
                                            <td><select name="filerBy"  style="width: 100px;" class="dropdown_accounting">
                                                    <option value="All">All</option>
                                                    <option value="Quotation">Quotation</option>
                                                    <option value="Booking">Booking</option>
                                                    <option value="FclBL">FclBL</option>                                                    
                                                    <option value="DNR">Doc's Not Received</option>
                                                    <option value="UMF">Un Manifested</option>
                                                    <option value="MF">Manifested</option>
                                                    <option value="SSL">Master Not Received</option>
                                                    <c:if test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                        <option value="IR">Imp Release</option>
                                                        <option value="NR">No Release</option>
                                                        <option value="DR">Doc Release</option>
                                                        <option value="PR">Pmt Release</option>
                                                        <option value="OP">Over Paid</option>
                                                    </c:if>

                                                </select>
                                                <br></td>
                                            <td>SSL Booking #<br></td>
                                            <td><html:text property="ssBkgNo" size="18" 
                                                       styleClass="textlabelsBoldForTextBox" ></html:text>
                                                <br></td>
                                            <td>Booked By</td>
                                            <td>
                                                <input type="text" name="bookedBy"  id="bookedBy"    style="width:110px;" class="textlabelsBoldForTextBox"/>
                                                <input type="checkbox" id="loginCheck1"  onclick="getLoginName1('<%=loginName%>')"/>Me
                                                <div id="bookedBy_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("bookedBy","bookedBy_choices","","",
                                                    "<%=path%>/actions/getUserDetails.jsp?tabName=SEARCH_FILE&from=1&isDojo=false","");
                                                </script>

                                            </td>
                                        </tr>
                                        <tr  class="textlabelsBold"><td>Limit</td>
                                            <td><html:select property="limit" styleClass="dropdown_accounting">
                                                    <html:optionsCollection name="limitList"/>
                                                </html:select>
                                            </td>
                                            <!--                                            <td>Show VoidBL</td><td>-->
                                            <!--                                                <html:select property="showVoidBL" styleClass="dropdown_accounting" >-->
                                            <!--                                                    <html:option value="N">No</html:option>-->
                                            <!--                                                    <html:option value="Y">YES </html:option>-->
                                                <!--                                                </html:select>-->
                                                <!--                                            </td>-->

                                                <td>Master BL</td>
                                                <td> <input type="text" name="masterBL"  id="bookedBy"  style="width:100px;"  class="textlabelsBoldForTextBox" /></td>
                                                <td>Inbond Number</td>
                                                <td> <input type="text" name="inbondNumber"  id="inbondNumber"   class="textlabelsBoldForTextBox" size="18"/></td>
                                                <td>Sort By</td><td>
                                                <html:select property="sortByDate" style="width:115px; " styleClass="dropdown_accounting">
                                                    <html:option value="S">SELECT</html:option>
                                                    <html:option value="C">Container Cut off</html:option>
                                                    <html:option value="D">Doc Cut Off</html:option>
                                                    <html:option value="E">ETD</html:option>
                                                </html:select>
                                            </td> </tr>
                                        <tr class="textlabelsBold">
                                            <c:if test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                <td>AMS</td><td><html:text property="ams" styleClass="textlabelsBoldForTextBox"/></td>
                                                <td>Sub-House</td><td><html:text property="subHouse" styleClass="textlabelsBoldForTextBox"/></td>

                                            </c:if>
                                            <td>AES ITN</td>
                                            <td> <input type="text" name="aesItn"  id="aesItn"  style="width:100px;"  class="textlabelsBoldForTextBox" /></td>
                                        </tr>

                                        <tr>
                                            <td valign="top" colspan="8"  align="center" style="padding-top:10px;">
                                                <c:if test="${param.accessMode == '1'}">
                                                    <input type="button" value="Quick Rates" onclick="showRatesPopUp()" class="buttonStyleNew" />
                                                    <c:if test="${sessionScope.quoteAccessMode == '1'}">
                                                        <input type="button" value="New Quote" onclick="addquote('quote')" class="buttonStyleNew"/>
                                                    </c:if>
                                                    <c:if test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                        <c:if test="${sessionScope.bookingAccessMode == '1'}">
                                                            <input type="button" value="New Booking"  onclick="addquote('booking')" class="buttonStyleNew"/>
                                                        </c:if>
                                                        <c:if test="${sessionScope.blAccessMode == '1'}">
                                                            <input type="button" value="New Arrival Notice" onclick="addquote('fclBl')" class="buttonStyleNew"/>
                                                        </c:if>
                                                    </c:if>
                                                    <input type="button" value="Search" onclick="searchquotation()" class="buttonStyleNew"/>
                                                </c:if>
                                                <%--<input type="button" value="Show All" onclick="showall()" class="buttonStyleNew"/> --%>
                                            </td>
                                        </tr>
                                    </table>
                                    <%}%>
                                </td>
                            </tr>
                            <tr style="height:60%"></tr>
                        </table>
                    </td>

                <tr>
                    <td align="top">
                        <%if(displayId!=null){ %>
                        <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew ">
                            <tr class="tableHeadingNew">
                                <td class="fileno-text1">
                                    <c:if test="${not empty searchQuotationForm.fileNumber}">
                                        <b class="fileno-text1">File Number-><c:out value="${searchQuotationForm.fileNumber}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.pol}">
                                        <b class="fileno-text1">Origin-><c:out value="${searchQuotationForm.pol}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.plor}">
                                        <b class="fileno-text1">POL-><c:out value="${searchQuotationForm.plor}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.plod}">
                                        <b class="fileno-text1">POD-><c:out value="${searchQuotationForm.plod}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.pod}">
                                        <b class="fileno-text1">Destination-><c:out value="${searchQuotationForm.pod}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.carrier}">
                                        <b class="fileno-text1">SSL-> <c:out value="${searchQuotationForm.carrier}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.forwarder}">
                                        <b class="fileno-text1">Forwarder-><c:out value="${searchQuotationForm.forwarder}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.conginee}">
                                        <b class="fileno-text1">Consignee-><c:out value="${searchQuotationForm.conginee}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.rampCity}">
                                        <b class="fileno-text1">Ramp City->
                                            <c:out value="${searchQuotationForm.rampCity}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.issuingTerminal}">
                                        <b class="fileno-text1">Issuing Terminal->
                                            <c:out value="${searchQuotationForm.issuingTerminal}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.client}">
                                        <b class="fileno-text1">Client-><c:out value="${searchQuotationForm.client}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.container}">
                                        <b>Container-><c:out value="${searchQuotationForm.container}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.quoteBy}">
                                        <b class="fileno-text1">Created By-><c:out value="${searchQuotationForm.quoteBy}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.bookedBy}">
                                        <b class="fileno-text1">Booked By-><c:out value="${searchQuotationForm.bookedBy}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.quotestartdate}">
                                        <b class="fileno-text1"> Start Date->
                                            <c:out value="${searchQuotationForm.quotestartdate}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.toDate}">
                                        <b class="fileno-text1"> To Date->
                                            <c:out value="${searchQuotationForm.toDate}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.originRegion && searchQuotationForm.originRegion!='0'}">
                                        <b class="fileno-text1">Origin Region->
                                            <c:out value="${searchQuotationForm.originRegion}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.destinationRegion && searchQuotationForm.destinationRegion!='0'}">
                                        <b class="fileno-text1">Destination Region->
                                            <c:out value="${searchQuotationForm.destinationRegion}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.ssBkgNo && searchQuotationForm.ssBkgNo!='0'}">
                                        <b class="fileno-text1">SSL Booking #->
                                            <c:out value="${searchQuotationForm.ssBkgNo}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.limit && searchQuotationForm.limit!=''}">
                                        <b class="fileno-text1">Limit-><c:out value="${searchQuotationForm.limit}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='C'}">
                                        <b class="fileno-text1">Sort By-><c:out value="Container Cut off"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='D'}">
                                        <b class="fileno-text1">Sort By-><c:out value="Doc Cut Off"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='DNR'}">
                                        <b class="fileno-text1">Sort By-><c:out value="Doc's Not Received"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='E'}">
                                        <b class="fileno-text1">Sort By-><c:out value="ETD"></c:out></b>,
                                    </c:if>
                                    </td>
                                    <td>
                                    <c:if test="${param.accessMode == '1'}">
                                        <span>
                                            <input type="button" value="Quick Rates" onclick="showRatesPopUp()" class="buttonStyleNew" />
                                            <input type="button" value="Search" onclick="refereshPage()" class="buttonStyleNew" />
                                            <c:if test="${sessionScope.quoteAccessMode == '1'}">
                                                <input type="button" value="New Quote" onclick="addquote('quote')" class="buttonStyleNew"/>
                                            </c:if>
                                            <c:if test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                <c:if test="${sessionScope.bookingAccessMode == '1'}">
                                                    <input type="button" value="New Booking"  style="width: 80px"  onclick="addquote('booking')" class="buttonStyleNew"/>
                                                </c:if>
                                                <c:if test="${sessionScope.blAccessMode == '1'}">
                                                    <input type="button" value="New Arrival Notice" style="width: 110px" onclick="addquote('fclBl')" class="buttonStyleNew"/>
                                                </c:if>
                                            </c:if>
                                        </span>
                                       <%-- <span style="float: right;">
                                            
                                        </span>--%>
                                    </c:if>
                                </td>
                                <td style="float: right">
                                    <b class="fileno-text" style="float:right;">File Search:&nbsp;&nbsp;&nbsp;<input type="text" id="fileNumberSearch" size="8" class="textlabelsBoldForTextBox"/>
                                        <img src="<%=path%>/img/icons/magnifier.png" border="0" onclick="searchByFileNumber()"/>
                                        <html:hidden property="fileNumber"/>
                                        <html:hidden property="limit"/>
                                        <html:hidden property="filerBy"/>
                                        <html:hidden property="sortByDate"/>
                                    </b>
                                </td>
                            </tr>
                            <%}%>

                            <tr align="top">
                                <td colspan="3" align="top">
                                    <% int i=0;  %>
                                        <display:table name="<%=displayId%>" id="fileNumberList" pagesize="${searchQuotationForm.limit}"
                                                       class="displaytagstyleNew" sort="list"  >
                                            <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner">
                                                    <font color="blue">{0}</font> Search File details displayed,For more code click on page numbers.
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.one_item_found">
                                                <span class="pagebanner">
      		One {0} displayed. Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.all_items_found">
                                                <span class="pagebanner">
     		 {0} {1} Displayed, Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="basic.msg.empty_list">
                                                <span class="pagebanner">

                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.placement" value="bottom" />
                                            <display:setProperty name="paging.banner.item_name" value="Quotation"/>
                                            <display:setProperty name="paging.banner.items_name" value="Files"/>
                                            <%
                                                    String toolTipHeight = "";
                                                    if(i==0 || i==1){
                                                            toolTipHeight = "20";
                                                    }else{
                                                            toolTipHeight = "20";
                                                    }
                                                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                                LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
                                                    String quotid=null,bookingId=null,fclBl=null,searchDate="",
                                                    displayQuoteId="",displayBKId="",displayFCLId="",converted="",color="",corrected="",tooltipValue="";
                                                    // folder ID:---
                                                    //Strign to diaply tol TIP
                                                    String orgTerminal="",orgTer="",destination="",dest="",pol="",polName="",pod="",podName="",status="",status1="";
                                                    String user="",textColor="",doorOrigin="",doorDestination="";
                                                    String streamLine="",strm="",clientName="",client="",rampCity="",rmpCity="",ssBkgNo="",displaycolor=null;
                                                    String issueTerm="",bookingComplete="";
                                                    String issue="",fileNumber="",manifest="",ratesNonRates="",hazmat="",prefixFile="",bookedBy="",blClosed="",blAudited="",
                                                    docReceived="",blVoid="",dockReceipt="",ediStatus="",trackingStatus="",doorOriginForToolTip="",doorDestinationForToolTip="";boolean drNumber = false;
                                                    String importRelease ="", itn="", itnStatus="",masterstatus = "";
                                                    Date bolDate = null;
                                                    long dateDiff;
                                                    Integer aesStatus=0;
                                                  
                                          if(displayId!=null && !displayId.isEmpty()){
                                                            FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking=(FileNumberForQuotaionBLBooking)displayId.get(i);
                                                  
                                                    request.setAttribute("docsNotReceivedFlag",fileNumberForQuotaionBLBooking.getDocsNotReceivedFlag());
                                                    if(fileNumberForQuotaionBLBooking.getDoorOrigin()!=null){
                                                            doorOrigin=fileNumberForQuotaionBLBooking.getDoorOrigin().replace("'","");
                                                    }
                                                    if(null != fileNumberForQuotaionBLBooking.getAesStatus()){
                                                        itnStatus = fileNumberForQuotaionBLBooking.getAesStatus();
                                                    }
                                                    if(null == itnStatus || itnStatus.equals("")){
                                                        aesStatus = fileNumberForQuotaionBLBooking.getAesCount();
                                                    }
                                                    if(null != fileNumberForQuotaionBLBooking.getDocumentStatus()){
                                                        masterstatus = fileNumberForQuotaionBLBooking.getDocumentStatus();
                                                    }

                                                    if(fileNumberForQuotaionBLBooking.getDoorDestination()!=null){
                                                            doorDestination=fileNumberForQuotaionBLBooking.getDoorDestination().replace("'","");
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getDestination_port()!=null){
                                                        destination = fileNumberForQuotaionBLBooking.getDestination_port();
                                                        if(!destination.equals("")){
                                                          dest = stringFormatter.getBreketValue(destination);
                                                        }
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getManifest()!=null){
                                                          manifest=fileNumberForQuotaionBLBooking.getManifest();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getBlAudit()!=null){
                                                          blAudited=fileNumberForQuotaionBLBooking.getBlAudit();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getBlClosed()!=null && !fileNumberForQuotaionBLBooking.getBlClosed().equals("")){
                                                          blClosed=fileNumberForQuotaionBLBooking.getBlClosed();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getOrigin_terminal()!=null){
                                                       orgTerminal = fileNumberForQuotaionBLBooking.getOrigin_terminal();
                                                       if(!orgTerminal.equals("")){
                                                          orgTer = stringFormatter.getBreketValue(orgTerminal);
                                                       }//if
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getBlvoid()!=null){
                                                        blVoid="Y";
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getDoorOrigin()!=null && !fileNumberForQuotaionBLBooking.getDoorOrigin().equals("")){
                                                        StringBuilder doorMove=new StringBuilder();
                                                        int index1 = orgTerminal.indexOf("/");
                                                        String origin="";
                                                        if(-1 != index1){
                                                            origin = orgTerminal.substring(0,index1);
                                                        }else{
                                                            origin=orgTerminal;
                                                        }
                                                        if(-1 != orgTerminal.lastIndexOf("(") && -1 != orgTerminal.lastIndexOf(")")){
                                                            origin = origin + orgTerminal.substring(orgTerminal.lastIndexOf("("),orgTerminal.lastIndexOf(")")+1);
                                                            }
                                                            doorMove.append("Origin=");
                                                            doorMove.append(origin);
                                                            doorMove.append("\n");
                                                            doorMove.append("Door Origin=");
                                                            String doorOrg="";
                                                            int index = doorOrigin.indexOf("/");
                                                            if(index!=-1){
                                                               doorOrg=doorOrigin.substring(0,index);
                                                            }else{
                                                               doorOrg=doorOrigin;
                                                            }
                                                            doorMove.append(doorOrg);
                                                            doorOriginForToolTip=doorMove.toString();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getDoorDestination()!=null && !fileNumberForQuotaionBLBooking.getDoorDestination().equals("")){
                                                        StringBuilder doorMove=new StringBuilder();
                                                        int index1 = destination.indexOf("/");
                                                        String destn="";
                                                        if(-1 != index1){
                                                            destn = destination.substring(0,index1);
                                                        }else{
                                                            destn=destination;
                                                        }
                                                        if(-1 != destination.lastIndexOf("(") && -1 != destination.lastIndexOf(")")){
                                                            destn = destn + destination.substring(destination.lastIndexOf("("),destination.lastIndexOf(")")+1);
                                                            }
                                                            doorMove.append("Destination=");
                                                            doorMove.append(destn);
                                                            doorMove.append("\n");
                                                            doorMove.append("Door Destination=");
                                                            String doorDestn="";
                                                            int index = doorDestination.indexOf("/");
                                                            if(index!=-1){
                                                               doorDestn=doorDestination.substring(0,index);
                                                            }else{
                                                               doorDestn=doorDestination;
                                                            }
                                                            doorMove.append(doorDestn);
                                                            doorDestinationForToolTip=doorMove.toString();
                                                    }

                                                    if(fileNumberForQuotaionBLBooking.getHazmat()!=null){
                                                        hazmat=fileNumberForQuotaionBLBooking.getHazmat();
                                                    }else{
                                                        hazmat="";
                                                    }
                                                    
                                                    if(fileNumberForQuotaionBLBooking.getRatesNonRates()!=null){
                                                       ratesNonRates=fileNumberForQuotaionBLBooking.getRatesNonRates();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getImportRelease()!=null){
                                                       importRelease=fileNumberForQuotaionBLBooking.getImportRelease();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getBookingComplete()!=null && fileNumberForQuotaionBLBooking.getBookingComplete().equalsIgnoreCase("Y")){
                                                    bookingComplete="Y";
                                            }
                                                    if(fileNumberForQuotaionBLBooking.getPod()!=null){
                                                            podName = fileNumberForQuotaionBLBooking.getPod();
                                                            if(!podName.equals("")){
                                                              pod = stringFormatter.getBreketValue(podName);
                                                              //pod=StringUtils.abbreviate(podName,5);
                                                            }
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getPol()!=null){
                                                            polName = fileNumberForQuotaionBLBooking.getPol();
                                                            if(!polName.equals("")){
                                                              pol = stringFormatter.getBreketValue(polName);
                                                              //pol=StringUtils.abbreviate(polName,5);
                                                            }
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getIssueTerminal()!=null){
                                                            issueTerm=fileNumberForQuotaionBLBooking.getIssueTerminal();
                                                            issue=issueTerm.indexOf("-") > -1 ?issueTerm.substring(issueTerm.indexOf("-")+1,issueTerm.length()):issueTerm;
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getUser()!=null){
                                                            user=fileNumberForQuotaionBLBooking.getUser().toUpperCase();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getBookedBy()!=null){
                                                        bookedBy=fileNumberForQuotaionBLBooking.getBookedBy().toUpperCase();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getFclBlStatus()!=null){
                                                            status1=fileNumberForQuotaionBLBooking.getFclBlStatus()!=null?fileNumberForQuotaionBLBooking.getFclBlStatus():"";
                                                            status1 = !status1.equalsIgnoreCase("null")?status1:"";
                                                        String[]temp=fileNumberForQuotaionBLBooking.getFclBlStatus().split(",");
                                                            String value="";
			 
                                                            for(int k=0;k<temp.length;k++){
                                                               if(temp[k].equals("I")){
                                                                  value+="I=Intra"+"<p>";
                                                               }else if(temp[k].equals("E")){
                                                                 value=value+"E=Ready To EDI"+"<p>";
                                                               }else if(temp[k].contains("G")){
                                                                 value=value+"G=GT NEXUS"+"<p>";
                                                               }else if(temp[k].equals("A")){
                                                                 value=value+"A=BL Audited"+"<p>";
                                                               }else if(temp[k].equals("RM")){
                                                                  value=value+"RM=Received Master"+"<p>";
                                                               }else if(temp[k].equals("CL")){
                                                                  value=value+"CL=BL Closed"+"<p>";
                                                               }else if(temp[k].contains("C")){
                                                                  value=value+"C=Container Cut Off"+"<p>";
                                                               }else if(temp[k].contains("D")){
                                                                  value=value+"D=Doc Cut Off"+"<p>";
                                                               }else if(temp[k].contains("S")){
                                                                  value=value+"S=Vessel Sailing Date"+"<p>";
                                                               }else if(temp[k].contains("U")){
                                                                  value=value+"U=Number of Units"+"<p>";
                                                               }else if(temp[k].contains("N")){
                                                                  value=value+"N=Non Rated"+"<p>";                                                                 
                                                               }

			  
                                                               status=value;
                                                            }
                                                            status1 =( null != status1)?status1.replaceAll("null",""):status1;
                                                    }
                                                    if(null !=manifest && !manifest.equals("")){
                                                            status+="M=ManiFest"+"<p>";
                                                            status1+=manifest+",";
                                                            if(fileNumberForQuotaionBLBooking.getCorrectedBL()){
                                                                    status+="CorrectedBL";
                                                                    status1+="C,";
                                                                    //corrected="CC";
                                                            }
                                                            if(fileNumberForQuotaionBLBooking.getCorrectionsPresent()!=null){
                                                                corrected="CC";
                                                            }
                                                            status1 =( null != status1)?status1.replaceAll("null",""):status1;
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getSsBkgNo()!=null){
                                                             ssBkgNo=fileNumberForQuotaionBLBooking.getSsBkgNo();
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getDocReceived()!=null){
                                                            docReceived=fileNumberForQuotaionBLBooking.getDocReceived();
                                                    }
                                                   
                                                    if(docReceived.equals("")){
                                                            docReceived="N";
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getRampCity()!=null){
                                                            rampCity=fileNumberForQuotaionBLBooking.getRampCity();
                                                            rmpCity=StringUtils.abbreviate(rampCity,16);
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getFileNo()!=null){
                                                            fileNumber=fileNumberForQuotaionBLBooking.getFileNo();
                                                            prefixFile=fileNumberPrefix+fileNumber;
                                                            dockReceipt = "04"+fileNumber;
                                                            trackingStatus =fileNumberForQuotaionBLBooking.getTrackingStatus();
                                                    }

                                                    if(fileNumberForQuotaionBLBooking.get304Success()!=null){
                                                        ediStatus =fileNumberForQuotaionBLBooking.get304Success();
                                                    }else{
                                                        ediStatus =fileNumberForQuotaionBLBooking.get304Failure();
                                                    }

                                                    if(fileNumberForQuotaionBLBooking.getCarrier()!=null){
                                                            streamLine = fileNumberForQuotaionBLBooking.getCarrier();
                                                            if(null != streamLine){
                                                                    streamLine=streamLine.replaceAll("'","\\\\'").replaceAll("\"","&quot;");
                                                            }
                                                            strm=StringUtils.abbreviate(streamLine,20);
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getClient()!=null){
                                                            clientName = fileNumberForQuotaionBLBooking.getClient();
                                                            client=StringUtils.abbreviate(clientName,20);
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getDisplayColor()!=null){
                                                            displaycolor=fileNumberForQuotaionBLBooking.getDisplayColor();
                                                            fileNumberForQuotaionBLBooking.setDisplayColor(null);
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getFileDate()!=null && !fileNumberForQuotaionBLBooking.getFileDate().equals("")){
                                                            searchDate=dateFormat.format(fileNumberForQuotaionBLBooking.getFileDate());
			
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getBolDate()!=null && !fileNumberForQuotaionBLBooking.getBolDate().equals("")){
                                                            bolDate=fileNumberForQuotaionBLBooking.getBolDate();

                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getQuotId()!=null &&
                                                    !fileNumberForQuotaionBLBooking.getQuotId().equals("")){
                                                            quotid=fileNumberForQuotaionBLBooking.getQuotId().toString();
                                                            displayQuoteId="quotes";
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getBookingId()!=null &&
                                                    !fileNumberForQuotaionBLBooking.getBookingId().equals("")){
                                                            bookingId=fileNumberForQuotaionBLBooking.getBookingId().toString();
                                                            displayBKId="booking";
                                                    }
                                                    if(fileNumberForQuotaionBLBooking.getFclBlId()!=null &&
                                                    !fileNumberForQuotaionBLBooking.getFclBlId().equals("")){
                                                            fclBl=fileNumberForQuotaionBLBooking.getFclBlId().toString();
                                                            displayFCLId="bl";
                                                    }
                                                            // when Quotes===>Booking==>>FCL BL conversion
                                                            if(!displayQuoteId.equals("") && !displayBKId.equals("") && !displayFCLId.equals("")){
                                                                    converted="FclBl";
                                                            }
                                                            // when Quotes===>Booking  conversion
                                                            else if(!displayQuoteId.equals("") && !displayBKId.equals("")){
                                                                    converted="Booking";
                                                            }
                                                            // when Booking==>>FCL BL conversion// have a boubt need to clear
                                                            else if(displayQuoteId.equals("") && !displayBKId.equals("")&& !displayFCLId.equals("")){
                                                                    converted="BookingConvertTOBL";
                                                            }
                                                            // when No conversion independent FCL BL
                                                            else if(displayQuoteId.equals("") && displayBKId.equals("")&& !displayFCLId.equals("")){
                                                                    converted="NotConvertedBL";
                                                            }
                                                            // when No conversion independent Booking
                                                            else if(displayQuoteId.equals("") && !displayBKId.equals("")&& displayFCLId.equals("")){
                                                                    converted="NotConvertedBooking";
                                                            }
                                                            // when No conversion independent Quotes
                                                            else if(!displayQuoteId.equals("") && displayBKId.equals("") && displayFCLId.equals("")){
                                                                    converted="Quote";
                                                            }
                                                    }
                                                    link=editPath+"?eachRowId="+i+"&fileNo="+fileNumber;
                                            %>

                                            <display:column title="Q">
                                                <%
                                                String load = "load";
                                                if(!converted.equals("") && converted.equals("Quote") && ratesNonRates.equals("R")){
                                                %>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong>Rated</strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/lightBlue2.gif" border="0" style="size:100px; "/>
                                                </span>
                                                <%
                                                }else if(!converted.equals("") && converted.equals("Quote") && ratesNonRates.equals("N")){
                                                %>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong>Non-rated</strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/black.gif" border="0" style="size:100px; "/>
                                                </span>
                                                <%} %>
                                            </display:column>

                                            <display:column title="BK">
                                                <%
                                                if(!converted.equals("") && converted.equals("Booking") || converted.equals("NotConvertedBooking")){

                                                        color ="lightBlue2.gif";//QuoteIcon.JPG
                                                        if(converted.equals("NotConvertedBooking")){
                                                                color="lightBlue2.gif";
                                                                tooltipValue="Rated In Process";
                                                        }
                                                        if(!bookingComplete.equals("") && bookingComplete.equalsIgnoreCase("Y")){
                                                                color="darkGreenDot.gif";
                                                                tooltipValue="Booking Complete";
                                                        }
                                                        if(ratesNonRates.equals("N")){
                                                                color="black.gif";
                                                                tooltipValue="Non Rated";
                                                        }
                                                        if(tooltipValue.equals("")){
                                                                tooltipValue="Rated In Process";
                                                        }
                                                //yellow.jpg
                                                %>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=tooltipValue.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');" onmouseout="tooltip.hide();">
                                                    <img src="<%=path%>/img/icons/<%=color%>" border="0"/>
                                                </span>
                                                <%
}%>
                                            </display:column>

                                            <display:column title="BL" >
                                                <%if(!converted.equals("") && converted.equals("FclBl") ||  converted.equals("BookingConvertTOBL")
                                                 || converted.equals("NotConvertedBL")){
                                                                color ="lightBlue2.gif";//lightBlue22.gif
                                                                if(converted.equals("FclBl")){
                                                                        color="lightBlue2.gif";
                                                                        tooltipValue="BL In Process";
                                                                }
                                                                if(!manifest.equals("")&& manifest.equalsIgnoreCase("M")){
                                                                        color="darkGreenDot.gif";
                                                                        tooltipValue="Manifested";
                                                                        if("Y".equalsIgnoreCase(blClosed)){
                                                                            tooltipValue="Manifested,Closed";
                                                                        }
                                                                        if("Y".equalsIgnoreCase(blAudited)){
                                                                            tooltipValue="Manifested,Closed,Audited";
                                                                        }
                                                                }
                                                                if(!corrected.equals("")&& corrected.equalsIgnoreCase("CC")){
                                                                        color="reddot1.gif";
                                                                        tooltipValue="Corrected";
                                                                }
                                                                if(!blVoid.equals("") && blVoid.equals("Y")){
                                                                        color="cross-circle.png";
                                                                        tooltipValue="Void BL";
                                                                }
                                                                if((status1.contains("S")==false)&& !manifest.equalsIgnoreCase("M")){
                                                                        color="yellow.gif";
                                                                        tooltipValue="Sailing Date Past";
                                                                }
                                                                 if(!"M".equalsIgnoreCase(manifest)&&(status1.contains("S")==true)){
                                                                        Date currentDate=new Date();
                                                                        if(bolDate != null && !bolDate.equals("")){
                                                                            long diff = currentDate.getTime() - bolDate.getTime();
                                                                            dateDiff = (diff/ (1000 * 60 * 60 * 24));
                                                                            if(dateDiff>15){
                                                                                color="orange_dot.png";
                                                                                tooltipValue="BL not manifested";
                                                                            }
                                                                        }
                                                                  }
                                                %>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=tooltipValue.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/<%=color%>" border="0" />
                                                </span>
                                                <%
                                                }
                                                %>
                                            </display:column>
                                            <display:column title="HZ">
                                                <%if(hazmat.equals("H")){ %>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong>Hazmat</strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/danger..png" border="0" style="width: 12px "/>
                                                </span>
                                                <%} %>
                                            </display:column>
                                            <display:column title="EDI">
                                                <%if(null != ediStatus && ediStatus.trim().equals("success")){ %>
                                                <span class="hotspot"  style="cursor: pointer;" onclick="ediTrack('<%=dockReceipt%>')">
                                                    <img src="<%=path%>/img/icons/arrow_green.png" border="0" style="width: 12px "/>
                                                </span>
                                                <%}else if(null != ediStatus && ediStatus.trim().equals("failure")){%>
                                                <span class="hotspot"  style="cursor: pointer;" onclick="ediTrack('<%=dockReceipt%>')">
                                                    <img src="<%=path%>/img/icons/arrow_red.png" border="0" style="width: 12px "/>
                                                </span>
                                                <%}%>
                                            </display:column>
                                            <display:column title="FileNo" sortable="true"  sortProperty="fileNo">
                                                <% if(!converted.equals("") && converted.equals("Quote")){
													if(displaycolor!=null){%>
                                                <span class="linkSpan" style="font-weight: bold;color:red;background:#00CCFF"
                                                      onclick="checkLock('<%=fileNumber%>','quoteId=<%=quotid%>','QUOTE');"><%=prefixFile%></span>
                                                <%}else{%>
                                                <span class="linkSpan" style="color:black;" onclick="checkLock('<%=fileNumber%>','quoteId=<%=quotid%>','QUOTE');"><%=prefixFile%></span>
                                                <%}}%>

                                                <%if(!converted.equals("") && converted.equals("Booking") || converted.equals("NotConvertedBooking")){
													if(displaycolor!=null){	%>
                                                <span class="linkSpan" id="converted" style="font-weight: bold;color:red;background:#00CCFF"
                                                      onclick="checkLock('<%=fileNumber%>','bookingId=<%=bookingId%>&quoteId=<%=quotid%>','BOOKING');"><%=prefixFile%></span>
                                                <%}else{%>
                                                <span class="linkSpan" style="color:black;"
                                                      onclick="checkLock('<%=fileNumber%>','bookingId=<%=bookingId%>&quoteId=<%=quotid%>','BOOKING')"><%=prefixFile%></span>
                                                <%}}%>

                                                <%
                                                if(!converted.equals("") && converted.equals("FclBl") ||
                                                converted.equals("BookingConvertTOBL")|| converted.equals("NotConvertedBL")){
                                                    if(displaycolor!=null){%>
                                                <span class="linkSpan" id="converted" style="font-weight: bold;color:red;background:#00CCFF"
                                                      onclick="checkLock('<%=fileNumber%>','blId=<%=fclBl%>&bookingId=<%=bookingId%>&quoteId=<%=quotid%>&fileNumber=<%=fileNumber%>','FCLBL')"><%=prefixFile%></span>
                                                <%}else{%>
                                                <span class="linkSpan" style="color:black;"
                                                      onclick="checkLock('<%=fileNumber%>','blId=<%=fclBl%>&bookingId=<%=bookingId%>&quoteId=<%=quotid%>&fileNumber=<%=fileNumber%>','FCLBL')"><%=prefixFile%></span>
                                                <%}}%>
                                            </display:column>
                                            <display:column  title="Status"  style="width:80px;" sortProperty="fclBlStatus">
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=status.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:black;"><%=status1%></span>
                                            </display:column>
                                            <c:choose>
                                                <c:when test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                    <display:column  sortable="true"  title="Rel" >
                                                        <%
                                                        if(null != importRelease && importRelease.trim().equals("YY")){ %>
                                                        <img src="<%=path%>/img/icons/green_check.png" border="0" class="cursorAuto"/>
                                                        <%}else if(null != importRelease && importRelease.trim().equals("YN")) {%>
                                                        <font color="red" style="font-weight: bold">  DR</font>
                                                        <%}else if(null != importRelease && importRelease.trim().equals("NY")) {%>
                                                        <font color="red" style="font-weight: bold">  PR</font>
                                                        <% }%>
                                                    </display:column>
                                                </c:when>
                                                <c:otherwise>
                                                    <display:column  sortable="true"  title="Doc" >
                                                        <c:choose>
                                                            <c:when test="${docsNotReceivedFlag}">
                                                                <font color="red" style="font-weight: bold;"><%=docReceived%></font>
                                                            </c:when>
                                                            <c:otherwise> 
                                                                <%=docReceived%>
                                                            </c:otherwise>     
                                                        </c:choose>
                                                         <%if(null != masterstatus && masterstatus.trim().equalsIgnoreCase("Approved")) {%>
                                                        ,<font color="green" style="font-weight: bold"> A</font>
                                                        <%}else if(null != masterstatus && masterstatus.trim().equalsIgnoreCase("Disputed")) {%>
                                                        ,<font color="red" style="font-weight: bold"> D</font>
                                                        <% }%>

                                                    </display:column>
                                                </c:otherwise>
                                            </c:choose>
                                            <display:column  sortable="true"  title="SSL Booking#" ><%=ssBkgNo%></display:column>
                                            <display:column  title="StartDate"  sortable="true" sortProperty="fileDate"><%=searchDate %></display:column>
                                            <display:column  title="Origin" sortable="true">
                                                <%if(null != doorOriginForToolTip && !doorOriginForToolTip.equalsIgnoreCase("")){ %>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=doorOriginForToolTip.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n","<br>")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:red;font-weight: bold;"><%=orgTer%></span>
                                                <%}else{%>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=orgTerminal.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:black;"><%=orgTer%></span>
                                                <%}%>
                                            </display:column>
                                            <display:column  title="POL" style="width:10px;" sortable="true">
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=polName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:black;"><%=pol%></span>
                                            </display:column>
                                            <display:column  title="POD" style="width:10px;" sortable="true">
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=podName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:black;"><%=pod%></span>
                                            </display:column>
                                            <display:column  title="Destination" sortable="true">
                                                <%if(null != doorDestinationForToolTip && !doorDestinationForToolTip.equalsIgnoreCase("")){ %>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=doorDestinationForToolTip.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n","<br>")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:red;font-weight: bold;"><%=dest%></span>
                                                <%}else{%>
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=destination.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:black;"><%=dest%></span>
                                                <%}%>
                                            </display:column>
                                            <display:column  title="Client"  sortable="true">
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=clientName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:black;"><%=client %></span>
                                            </display:column>
                                            <display:column  title="SSL" sortable="true">
                                                <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=streamLine.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                      onmouseout="tooltip.hide();" style="color:black;"><%=strm%></span>
                                            </display:column>
                                            <display:column title="ISS" sortable="true"><span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=issueTerm.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>');"
                                                  onmouseout="tooltip.hide();" style="color:black;"><%=issue%></span></display:column>
                                            <display:column title="TR"  sortable="true">
                                                <%if(null != trackingStatus && !trackingStatus.equalsIgnoreCase("")){ %>
                                                <img src="<%=path%>/img/icons/e_contents_view.gif" border="0" onmouseover="tooltip.showSmall('<strong><%=trackingStatus%></strong>','<%=toolTipHeight%>');"
                                                     onmouseout="tooltip.hide();" onclick="return GB_show('Notes','<%=path%>/notes.do?moduleId=File&itemName=100018&moduleRefId='+'<%=fileNumber%>',380,780);" />
                                                <%}%>
                                            </display:column>
                                            <display:column title="AES"  sortable="true">
                                                <%if(null != itnStatus && !itnStatus.equalsIgnoreCase("")){ %>
                                                    <%if(itnStatus.trim().equalsIgnoreCase("Shipment Added") || itnStatus.trim().equalsIgnoreCase("Shipment Replaced")){ %>
                                                        <span class="linkSpan"  style="font-weight: bold;background:#00FF00" onmouseover="tooltip.showSmall('<strong><%=itnStatus%></strong>','<%=toolTipHeight%>');"
                                                         onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','<%=path%>/aesHistory.do?fileNumber='+'<%=fileNumber%>');">AES</span>
                                                    <%}else if(itnStatus.toLowerCase().contains("verify")){%>
                                                    <span class="linkSpan"  style="font-weight: bold;background:#00FFFF" onmouseover="tooltip.showSmall('<strong><%=itnStatus%></strong>','<%=toolTipHeight%>');"
                                                         onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','<%=path%>/aesHistory.do?fileNumber='+'<%=fileNumber%>');">AES</span>
                                                    <%}else if(itnStatus.toLowerCase().contains("shipment rejected")){%>
                                                    <span class="linkSpan"  style="font-weight: bold;background:#FF0000" onmouseover="tooltip.showSmall('<strong><%=itnStatus%></strong>','<%=toolTipHeight%>');"
                                                         onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','<%=path%>/aesHistory.do?fileNumber='+'<%=fileNumber%>');">AES</span>
                                                    <%}else{%>
                                                    <span class="linkSpan"  style="font-weight: bold;background:yellow" onmouseover="tooltip.showSmall('<strong><%=itnStatus%></strong>','<%=toolTipHeight%>');"
                                                         onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','<%=path%>/aesHistory.do?fileNumber='+'<%=fileNumber%>');">AES</span>
                                                    <%}%>
                                                <%}else if(null != aesStatus && aesStatus != 0){%>
                                                <span style="font-weight: bold;background:yellow;cursor: pointer" onmouseover="tooltip.showSmall('<strong>Aes Sent</strong>','<%=toolTipHeight%>');"
                                                         onmouseout="tooltip.hide();">AES</span>
                                                <%}%>
                                            </display:column>
                                            <display:column title="Created By"  sortable="true"><%=user%></display:column>
                                            <display:column title="booked By"  sortable="true"><%=bookedBy%></display:column>
                                            <%i++;%>
                                        </display:table>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
            </table>
            <br/><br/><br/><br/><br/><br/>
            <script>
                <%-- onLoad="disabled('<%=modify%>')"--%>
            </script>
        </html:form>
        <%
        if(null !=session.getAttribute("autoClick") && session.getAttribute("autoClick").equals("autoClick")){
                session.removeAttribute("autoClick");
        %>
        <script>autoClick();</script>
        <%}%>
<%
                if(null !=session.getAttribute("QuoteCopied") && session.getAttribute("QuoteCopied").equals("QuoteCopied")){
                        session.removeAttribute("QuoteCopied");
        %>
        <script>QuoteCopied();</script>
        <%}%>
    </html:html>
