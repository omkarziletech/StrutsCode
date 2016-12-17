<%-- 
    Document   : searchScreen
    Created on : May 27, 2015, 4:21:18 PM
    Author     : Mei
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Screen</title>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <%@ taglib prefix="cong" tagdir="/WEB-INF/tags/cong"%>
        <%@include file="../../init.jsp" %>
        <%@include file="../../../includes/jspVariables.jsp"%>
        <%@include file="../../../../jsps/includes/baseResources.jsp" %>
        <%@include file="../../../includes/resources.jsp" %>
        <link type="text/css" rel="stylesheet" href='<c:url value="/css/common.css"></c:url>'/>
        <script type="text/javascript" src='<c:url value="/jsps/LCL/js/export/searchScreen.js"></c:url>'></script>
    </head>
    <body>
        <cong:form id="lclSearchForm" name="lclSearchForm" method="post" action="lclSearch.do">
            <html:hidden property="moduleName" styleId="moduleNameId" value="${lclSearchForm.moduleName}"/>
            <html:hidden property="methodName" styleId="methodNameId" value="${lclSearchForm.methodName}"/>
            <input type="hidden" id="loginNameId" name="loginName" value="${user.loginName}"/>
            <input type="hidden" id="loginUserId" name="loginUserId" value="${user.userId}"/>
            <cong:hidden id="searchAndApply" name="searchAndApply"/>
            <div id="pane" style="overflow: auto;">
                <table class="widthFull" style="margin: 0;overflow: hidden" border="0">
                    <tr class="tableHeadingNew"><td colspan="10">File Search</td></tr>
                    <tr><td colspan="10"></td></tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">File No</td>
                        <td width="8%">
                            <cong:text name="fileNumber" id="fileNumber" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyDateValuesByField()"/>
                        </td>
                        <td class="textlabelsBoldforlcl">Current Location</td>
                        <td width="8%">
                            <cong:autocompletor name="currentLocation" id="currentLocation" template="one" width="250" container="NULL" query="PORT"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300px"
                                                fields="currentLocName,null,curUncode,currentLocCode"/>
                            <html:hidden property="currentLocName" styleId="currentLocName"/>
                            <html:hidden property="curUncode" styleId="curUncode"/>
                            <html:hidden property="currentLocCode" styleId="currentLocCode"/>
                        </td>
                        <td width="1%" align="left">
                            <span id="checkBoxMouse" title="Include INTR" style="float: left">
                                <cong:checkbox name="includeIntr" id="includeIntr" container="null"/>
                            </span>
                            <span id="checkBoxMouse" title="Include Bookings" >
                                <cong:checkbox name="includeBkg" id="includeBkg" container="null"/>
                            </span>  
                        </td>
                        <td class="textlabelsBoldforlcl">From Date</td>
                        <td width="8%">
                            <cong:calendarNew  id="from-date" name="startDate"  styleClass="textlabelsBoldForTextBox"/>
                        </td>
                        <td class="textlabelsBoldforlcl">To Date</td>
                        <td width="8%">
                            <cong:calendarNew  id="endDate"  name="endDate" styleClass="textlabelsBoldForTextBox"/>
                        </td>
                        <td></td>
                    </tr>


                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>



                    <tr>
                        <td class="textlabelsBoldforlcl">Client</td>
                        <td>
                            <cong:autocompletor name="client" id="client" template="tradingPartner"
                                                fields="clientNo" query="MAIN_SCREEN_CLIENT"
                                                scrollHeight="300"  width="600" container="NULL"
                                                shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox" />
                            <html:hidden property="clientNo" styleId="clientNo" />
                        </td>
                        <td class="textlabelsBoldforlcl">Origin</td>
                        <td>
                            <cong:autocompletor name="origin" id="origin" template="one" width="250" container="NULL" query="PORT"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300"
                                                fields="null,null,portName,countrycode" callback="combineValue('portName')"/>
                            <cong:hidden name="portName" id="portName"/>
                            <cong:hidden name="countryCode" id="countrycode"/>
                        </td>
                        <td>
                            <span style="vertical-align: bottom;height: 10px" title="Choose up to 3 cities">
                                <cong:img src="${path}/img/icons/iicon.png" width="12" height="14" id="orginIcon" alt="filter" onclick="addMorecites('${path}','origin');"/>
                            </span>
                        </td>
                        <td class="textlabelsBoldforlcl">Issuing Terminal</td>
                        <td>
                            <cong:autocompletor name="issuingTerminal" id="issuingTerminal" template="terminal" query="MAIN_SCREEN_TERMINAL" width="200" fields="terminalNo"
                                                scrollHeight="200" container="NULL" styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true"/>
                            <cong:hidden name="terminalNo" id="terminalNo"/>
                            <cong:checkbox name="associatedTerminal" id="associatedTerminal" title="Select Associated CTC/FTF"  container="null"/>
                        </td>
                        <td class="textlabelsBoldforlcl">Container No</td>
                        <td>
                            <cong:text name="containerNo"  id="containerNo"   styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyDateValuesByField()"/>
                        </td>
                        <td></td>
                    </tr>

                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>

                    <tr>
                        <td class="textlabelsBoldforlcl">Shipper</td>
                        <td>
                            <cong:autocompletor name="shipperName" id="shipperName" template="tradingPartner" query="MAIN_SCREEN_SHIPPER"
                                                fields="shipperNo" width="600" scrollHeight="300"
                                                paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode"
                                                container="NULL" shouldMatch="true"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
                            <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
                            <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
                            <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
                            <input type="hidden" name="shipperCountryUnLocCode" id="shipperCountryUnLocCode"/>
                            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" alt="Shipper" style="vertical-align: middle"
                                 title="Click here to edit House Shipper Search options" onclick="searchFilter('${path}', 'Shipper')"/>
                            <cong:hidden name="shipperNo" id="shipperNo" />
                        </td>
                        <td class="textlabelsBoldforlcl">POL</td>
                        <td>
                            <cong:autocompletor name="pol" id="pol" template="one" width="250" container="NULL" query="PORT"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300"
                                                fields="null,NULL,polName,polCountryCode" callback="combineValue('polName')"/>
                            <cong:hidden name="polName" id="polName"/>
                            <cong:hidden name="polCountryCode" id="polCountryCode"/>
                        </td>
                        <td>
                            <span style="vertical-align: bottom;height: 10px" title="Choose up to 3 cities">
                                <cong:img src="${path}/img/icons/iicon.png" width="12" height="14" id="polIcon" alt="filter" onclick="addMorecites('${path}','pol');"/>
                            </span>
                        </td>
                        <td class="textlabelsBoldforlcl">Origin Region</td>
                        <td>
                            <html:select property="originRegion" styleId="originRegion" styleClass="smallDropDown textlabelsBoldforlcl" style="width:134px">
                                <html:optionsCollection property="regions" label="label" value="value"/>
                            </html:select>
                        </td>
                        <td class="textlabelsBoldforlcl">SSL</td>
                        <td>
                            <cong:autocompletor name="ssl" id="ssl" template="tradingPartner" position="left"
                                                query="MAIN_SCREEN_SSLINE" scrollHeight="300" styleClass="textlabelsLclBoldForMainScreenTextBox"
                                                width="600" container="NULL" shouldMatch="true"/>
                        </td>
                        <td></td>
                    </tr>

                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>


                    <tr>
                        <td class="textlabelsBoldforlcl">Forwarder</td>
                        <td>
                            <cong:autocompletor name="forwarder" template="tradingPartner" query="MAIN_SCREEN_FORWARDER" fields="forwarderNo"
                                                scrollHeight="300" width="600" container="NULL" shouldMatch="true"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <cong:hidden name="forwarderNo" id="forwarderNo" />
                        </td>
                        <td class="textlabelsBoldforlcl">POD</td>
                        <td>
                            <cong:autocompletor name="pod" id="pod" query="CONCAT_RELAY_NAME_FD" template="one" width="250" fields="podName,NULL,NULL,podCountryCode"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" container="NULL" shouldMatch="true" scrollHeight="200"/>
                            <cong:hidden name="podName" id="podName"/>
                            <cong:hidden name="podCountryCode" id="podCountryCode"/>
                        </td>
                        <td></td>
                        <td class="textlabelsBoldforlcl">Destination Region</td>
                        <td>
                            <html:select property="destinationRegion" styleId="destinationRegion" styleClass="smallDropDown textlabelsBoldforlcl" style="width:134px">
                                <html:optionsCollection property="regions" label="label" value="value"/>
                            </html:select>
                        </td>
                        <td class="textlabelsBoldforlcl">Quote By</td>
                        <td>
                            <cong:autocompletor name="createdBy" id="createdBy" template="one" query="SALES_PERSON"  width="300" container="NULL" position="left"
                                                styleClass="textlabelsLclBoldForMainScreenCheckBox" scrollHeight="200" shouldMatch="true"/>
                        </td>
                        <td>
                            <cong:checkbox name="blcreatedBy" id="blcreatedBy" title="Quote Created By Me" onclick="defaultLoginNameCreatedBy()" container="null"/>
                        </td>

                    </tr>

                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>



                    <tr>
                        <td class="textlabelsBoldforlcl">Consignee</td>
                        <td>
                            <cong:autocompletor name="consignee" template="tradingPartner" query="MAIN_SCREEN_CONSIGNEE" fields="consigneeNo"
                                                width="600" scrollHeight="300" container="NULL"
                                                paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeCountryUnLocCode"
                                                shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <cong:hidden name="consigneeNo" id="consigneeNo" />
                            <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
                            <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
                            <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
                            <input type="hidden" name="consigneeSearchCountry" id="consigneeSearchCountry"/>
                            <input type="hidden" name="consigneeCountryUnLocCode" id="consigneeCountryUnLocCode"/>
                            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle" alt="Consignee"
                                 title="Click here to edit House Consignee Search options" onclick="searchFilter('${path}', 'Consignee');"/>
                        </td>
                        <td class="textlabelsBoldforlcl">Destination</td>
                        <td>
                            <cong:autocompletor name="destination" id="destination" query="CONCAT_RELAY_NAME_FD" template="one" width="250"
                                                fields="destinationName,NULL,NULL,destCountryCode" styleClass="textlabelsLclBoldForMainScreenTextBox"
                                                container="NULL" shouldMatch="true" scrollHeight="200"/>
                            <cong:hidden name="destinationName" id="destinationName"/>
                            <cong:hidden name="destCountryCode" id="destCountryCode"/>
                        </td>
                        <td></td>
                        <td class="textlabelsBoldforlcl">SSL Booking #</td>
                        <td>
                            <cong:text  id="sslBookingNo" name="sslBookingNo" styleClass="textlabelsLclBoldForMainScreenCheckBox" onkeyup="setEmptyDateValuesByField()"/>
                        </td>
                        <td class="textlabelsBoldforlcl">Booked By</td>
                        <td>
                            <cong:autocompletor name="bookedBy" id="bookedBy" template="one" query="SALES_PERSON"  width="300" scrollHeight="200" position="left" 
                                                container="NULL" styleClass="textlabelsLclBoldForTextBox" shouldMatch="true" />
                        </td>
                        <td>
                            <cong:checkbox name="blbookedBy" id="blbookedBy" title="Booking Created By Me" onclick="defaultLoginNameBookedBy()" container="NULL"/>
                        </td>
                    </tr>


                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>


                    <tr>
                        <td class="textlabelsBoldforlcl">Foreign Agent</td>
                        <td>
                            <cong:autocompletor name="foreignAgent" id="foreignAgent" template="tradingPartner"  fields="foreignAgentAccount"
                                                query="MAIN_SCREEN_AGENT" params="E" scrollHeight="300"  width="600" container="NULL"
                                                shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox" />
                        </td>
                        <cong:hidden name="foreignAgentAccount" id="foreignAgentAccount"/>
                        <td class="textlabelsBoldforlcl">Filter By</td>
                        <td>
                            <html:select property="filterBy" styleClass="smallDropDown textlabelsBoldforlcl" styleId="filterBy" style="width:134px" onchange="filterOptionEnable()">
                                <html:option value="IWB">Inventory All</html:option>
                                <html:option value="IPO">Inventory Pickups Only</html:option>
                                <html:option value="All">All</html:option>
                                <html:option value="Q">Quotation</html:option>
                                <html:option value="RF" >Refused</html:option>
                                <html:option value="X">Terminated</html:option>
                                <html:option value="BL">Loaded with no B/L or In Pool</html:option>
                                <html:option value="BP">BL Pool</html:option>
                                <html:option value="ONBK">Online Booking</html:option>
                                <html:option value="UND">Unknown Destination</html:option>
                                <html:option value="BNR">Bookings NOT Received</html:option>

                            </html:select>
                        </td>
                        <td>
                            <span id="checkBoxMouse" title="Include Bookings">
                                <cong:checkbox name="filterByInventory" id="filterByInventory"  container="null"/>
                            </span>
                            <span id="icon" style="vertical-align: bottom;height: 10px">
                                <cong:img src="${path}/img/icons/iicon.png" width="12" height="14" id="filterOptions" alt="filter" onclick="filterOptionReport('${path}');"/>
                            </span>
                        </td>
                        <td class="textlabelsBoldforlcl">Inbond Number</td>
                        <td>
                            <cong:text id="inbondNo" name="inbondNo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyDateValuesByField()"/>
                        </td>
                        <td class="textlabelsBoldforlcl">Choose Template</td>
                        <td>
                            <cong:div id="templateBox">
                                <c:import url="/jsps/LCL/ajaxload/templateSelectBox.jsp"/>
                            </cong:div>
                        </td>
                        <td></td>
                    </tr>


                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>


                    <tr>
                        <td class="textlabelsBoldforlcl">CFCL</td>
                        <td>
                            <html:select property="cfcl" styleId="cfcl" styleClass="smallDropDown textlabelsBoldforlcl">
                                <html:option value="0">Exclude</html:option>
                                <html:option value="1">Only</html:option>
                                <html:option value="">Include</html:option>
                            </html:select>
                        </td>
                        <td class="textlabelsBoldforlcl">Master BL</td>
                        <td>
                            <cong:text id="masterBl" name="masterBl" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyDateValuesByField()"/>
                        </td>
                        <td></td>
                        <td class="textlabelsBoldforlcl">Customer Po #</td>
                        <td>
                            <cong:text id="customerPo" name="customerPo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyDateValuesByField()"/>
                        </td>
                        <td class="textlabelsBoldforlcl">Tracking No #</td>
                        <td>
                            <cong:text id="trackingNo" name="trackingNo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyDateValuesByField()"/>
                        </td>
                        <td></td>
                    </tr>






                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>

                    <tr>
                        <td class="textlabelsBoldforlcl">CFCL Account #</td>
                        <td>
                            <cong:autocompletor name="cfclAccount"  id="cfclAccount" template="tradingPartner" fields="NULL,NULL,NULL,NULL,NULL,NULL,NULL,disabledAccount,forwardAccount"
                                                query="CFCL_ACCOUNTNO"  width="600" scrollHeight="300" container="NULL" shouldMatch="true"
                                                callback="cfcl_AccttypeCheck();" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                        </td>
                    <input type="hidden" name="disabledAccount" id="disabledAccount"/>
                    <input type="hidden" name="forwardAccount" id="forwardAccount"/>
                    <td class="textlabelsBoldforlcl">Booked For Voyage</td>
                    <td>
                        <cong:autocompletor id="bookedForVoyage" name="bookedForVoyage" width="500" container="NULL" query="BOOKED_VOYAGE"
                                            paramFields="polCountryCode,podCountryCode" template="two" shouldMatch="true" scrollHeight="200"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  />
                    </td>
                    <td>
                        <span style="vertical-align: bottom;height: 10px" title="Press Spacebar to see entire Voyage List">
                            <cong:img src="${path}/img/icons/iicon.png" width="12" height="14" id="bookedForVoyageIcon" alt="filter"/>
                        </span>
                    </td>
                    <td class="textlabelsBoldforlcl">New Quote Copy From</td>
                    <td>
                        <cong:text id="copyQuote" name="copyQuote" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                        <font style="float:none" class="button-style1" onclick="newcopyQuoteForm('${path}')">Go
                        </font>
                    </td>
                    <td class="textlabelsBoldforlcl">New Booking Copy From</td>
                    <td>
                        <cong:text id="copyBooking" name="copyBooking" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                    </td>
                    <td>
                        <div style="float:left" class="button-style1" onclick="newCopyBkgForm('${path}')">Go
                        </div>
                    </td>
                    </tr>

                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>

                    <tr>
                        <td class="textlabelsBoldforlcl">Warehouse Doc #</td>
                        <td>
                            <cong:text id="warehouseDocNo" name="warehouseDocNo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyDateValuesByField()"/>
                        </td>
                        <td class="textlabelsBoldforlcl">Order By</td>
                        <td>
                            <html:select property="orderBy"  styleId="orderBy" styleClass="smallDropDown textlabelsBoldforlcl" style="width:134px">
                                <html:option value="BDesc" >Bkg# Descending</html:option>
                                <html:option value="BAsc">Bkg# Ascending</html:option>
                                <html:option value="B">Receiving Terminal</html:option>
                                <html:option value="Des">Final Destination</html:option>
                                <html:option value="Cons">Consignee Name</html:option>
                                <html:option value="Fwd">Forwarder Name</html:option>
                                <html:option value="Ship">Shipper Name</html:option>
                            </html:select>
                        </td>
                        <td></td>
                        <td class="textlabelsBoldforlcl"></td>
                        <td></td>
                        <td class="textlabelsBoldforlcl">BL Pool Owner</td>
                        <td>
                            <cong:autocompletor name="blPoolOwner" id="blPoolOwner" template="one" query="SALES_PERSON"  width="200" scrollHeight="200"
                                                container="NULL" styleClass="textlabelsLclBoldForTextBox" shouldMatch="true" position="left"/>

                        </td>
                        <td>
                            <cong:checkbox name="blOwner" id="blOwner" title="BL Pool Owner By Me" onclick="defaultLoginNameBlOwner()" container="NULL"/>
                        </td>
                    </tr>






                    <tr><td colspan="10"></td></tr>
                    <tr><td colspan="10"></td></tr>

                    <tr>
                        <td  colspan="2">
                            <input type="button" class="button-style1" value="Edit LCL Defaults"  id="lclDefaults" 
                                   onclick="openApplyDefaults('${path}', 'display', '${user.userId}', 'Exports');" />
                            <html:select property="lclDefaultName" styleId="lclDefaultName"  styleClass="smallDropDown textlabelsBoldforlcl"
                                         onchange="applyUserDefaultValue('${path}');" value="${lclSearchForm.lclDefaultId}">
                                <html:option value="">Select LCL Default</html:option>
                                <html:optionsCollection name="applyDefaultNameList"/> 
                            </html:select>   
                            <cong:hidden id="lclDefaultId" name="lclDefaultId"/>
                        </td>
                        <%--<input type="button"  value="Apply Defaults" id="LclUserDefaults"
                            align="middle" class="button-style1" onclick="applyUserDefaultValue('${path}');"/>--%>
                        <%--<input type="checkbox" align="right" name="applyandsearch" id="applyandsearch"
                             class="button-style1" title="Search after applying"
                              onclick="searchAfterApply();"/>--%>
                        <td class="textlabelsBoldforlcl"></td>
                        <td class="textlabelsBoldforlcl">
                            <c:choose>
                                <c:when test="${lclSearchForm.commodity eq 'M'}">
                                    <input type="radio" name="commodity" id="imperial" value="I" />Imperial
                                    <input type="radio" name="commodity" id="metric" value="M" checked="yes"/>Metric
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" name="commodity" id="imperial" value="I" checked="yes"/>Imperial
                                    <input type="radio" name="commodity" id="metric" value="M" />Metric
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                        </td>
                        <td colspan="3" class="textlabelsBoldforlcl">
                            <input type="button" value="New Quote" align="middle" class="button-style1" onclick="createNewFile('${path}', 'Quotes');"/>
                            <input type="button" value="New Bkg" align="middle" class="button-style1" onclick="createNewFile('${path}', 'Bookings');"/>
                            <c:if test="${roleQuickBkg}">
                                <input type="button" id="quickdr"  value="Quick Bkg" align="middle" class="button-style1" onclick="createQuickBkg('${path}');"/>
                            </c:if>
                            <input type="button"  value="Search" align="middle" class="button-style1" onclick="searchValidation('search');"/>
                            <input type="button"  value="Reset" align="middle" class="button-style1" onclick="submitResetForm();"/>
                            <c:if test="${roleDuty.addTemplates}">
                                <input type="button" value="Add/Edit Templates" class="button-style1 template" onclick="createTemplate('${path}');"/>
                            </c:if>
                                <input type="button"  value="Batch Terminate" align="middle" class="button-style1" onclick="openTerminatePopUp('${path}');"/>
                            Limit
                        </td>

                        <td>
                            <html:select property="limit" styleId="limit" styleClass="smallDropDown textlabelsBoldforlcl" style="width:134px">
                                <html:option value="100">100</html:option>
                                <html:option value="150">150</html:option>
                                <html:option value="250">250</html:option>
                                <html:option value="500">500</html:option>
                                <html:option value="1000">1000</html:option>
                            </html:select>
                        </td>
                        <td></td>
                    </tr>

                    <tr><td colspan="2">
                            <%--<html:select property="lclDefaultName" styleId="lclDefaultName" style="margin-left:90px;" styleClass="smallDropDown textlabelsBoldforlcl">
                                <html:option value="">SELECT</html:option>
                                <html:optionsCollection name="applyDefaultNameList"/> 
                            </html:select>   
                            <cong:hidden id="lclDefaultId" name="lclDefaultId"/>--%>
                        </td><td colspan="8"></td></tr>
                    <tr><td colspan="10"></td></tr>

                </table>
                <span  class="red bold"  style="font-size: xx-small">  * All Search Fields Should Be Minimum 3 Characters</span>
            </div>
        </cong:form>
    </body>
</html>
