<%--
    Document   : mainScreen
    Created on : Feb 5, 2015, 6:21:33 PM
    Author     : Mohana
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dcong:td">
<html xmlns="http://www.w3.org/1999/xhtml">
    <%@include file="../../init.jsp" %>
    <%@include file="../../../includes/jspVariables.jsp"%>
    <%@include file="../../../../jsps/includes/baseResources.jsp" %>
    <%@include file="../../../includes/resources.jsp" %>
    <%@page import="com.gp.cong.logisoft.domain.User"%>
    <%@include file="../../colorBox.jsp"%>
    <%@include file="/taglib.jsp"%>
    <script type="text/javascript" src="${path}/jsps/LCL/js/importSearchResult.js"></script>
    <%        User user = (User) request.getSession().getAttribute("loginuser");
        request.setAttribute("user", user);
    %>
    <cong:body style="height:500px">
        <cong:div id="pane">
            <cong:form id="lclSearchForm" name="lclSearchForm" method="post" action="lclImportSearch.do">
                <jsp:useBean id="portsDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.PortsDAO"/>
                <c:set var="regionList" value="${portsDAO.allRegions}" scope="request"/>
                <cong:table styleClass="widthFull" cellspacing="2" border="0">
                    <tr class="tableHeadingNew"><td colspan="9">File Search</td></tr>
                    <tr><td colspan="9"></td></tr>
                        <%--1st row><--%>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl" width="23%">DR #
                            <cong:text name="fileNumber" id="fileNumber" styleClass="textlabelsLclBoldForMainScreenTextBox"
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" width="23%">Origin
                            <cong:autocompletor name="origin" id="origin" template="one" width="250" container="NULL" query="IMPORT_MAIN_SCREEN_ORIGIN"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300px"
                                                fields="portName,NULL,NULL,countrycode"/>
                            <cong:hidden name="portName" id="portName"/>
                            <cong:hidden name="countryCode" id="countrycode"/>
                        </cong:td>              
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" width="15%">From Date</cong:td>
                        <cong:td>
                            <cong:calendarNew  id="from-date" name="startDate"  styleClass="textlabelsBoldForTextBox"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" width="15%">To Date</cong:td>
                        <cong:td>
                            <cong:calendarNew  id="endDate"  name="endDate" styleClass="textlabelsBoldForTextBox"/>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <%-->2nd row<--%>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Client
                            <cong:autocompletor name="client" id="client" template="tradingPartner" width="600" 
                                                fields="clientNo" query="MAIN_SCREEN_CLIENT" scrollHeight="300px"  
                                                container="NULL" shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox" />
                            <cong:hidden name="clientNo" id="clientNo" />
                        </cong:td>
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">POL
                            <cong:autocompletor name="pol" id="pol" template="one" width="250" container="NULL" scrollHeight="300px" 
                                                query="ORIGIN_UNLOC" styleClass="textlabelsLclBoldForMainScreenTextBox" 
                                                shouldMatch="true" fields="polName,NULL,NULL,polCountryCode"/>
                            <cong:hidden name="polName" id="polName"/>
                            <cong:hidden name="polCountryCode" id="polCountryCode"/>
                        </cong:td>     
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Issuing Terminal</cong:td>
                        <cong:td>
                            <cong:autocompletor name="issuingTerminal" id="issuingTerminal" template="terminal" 
                                                query="SEARCH_IMPORTTERMINAL" width="200" scrollHeight="200px" container="NULL"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Container No</cong:td>
                        <cong:td>
                            <cong:text name="containerNo"  id="containerNo"   styleClass="textlabelsLclBoldForMainScreenTextBox" 
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Shipper
                            <cong:autocompletor name="shipperName" id="shipperName" template="tradingPartner" query="MAIN_SCREEN_SHIPPER"
                                                fields="shipperNo" width="600" scrollHeight="300px" container="NULL"
                                                paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode" 
                                                shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
                            <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
                            <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
                            <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
                            <input type="hidden" name="shipperCountryUnLocCode" id="shipperCountryUnLocCode"/>
                            <cong:hidden name="shipperNo" id="shipperNo" />
                            <cong:td width="5%">
                                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                                     title="Click here to edit House Shipper Search options" style="vertical-align: middle"
                                     onclick="showClientSearchOption('${path}', 'Shipper')"/>
                            </cong:td>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">POD
                            <cong:autocompletor name="pod" id="pod" query="IMPORT_MAIN_SCREEN_DESTINATION" 
                                                template="one" width="500" fields="podName,NULL,NULL,podCountryCode"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" container="NULL" 
                                                shouldMatch="true" scrollHeight="200px"/>
                            <cong:hidden name="podName" id="podName"/>
                            <cong:hidden name="podCountryCode" id="podCountryCode"/>
                        </cong:td>     
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Origin Region</cong:td>
                        <cong:td>
                            <html:select property="originRegion" styleId="originRegion" style="width:134px" 
                                         styleClass="smallDropDown textlabelsBoldforlcl">
                                <html:option value="">Select One</html:option>
                                <html:optionsCollection name="regionList"/>
                            </html:select>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">SSL</cong:td>
                        <cong:td>
                            <cong:autocompletor name="ssl" id="ssl" template="tradingPartner" position="left" query="MAIN_SCREEN_SSLINE" 
                                                scrollHeight="300px" styleClass="textlabelsLclBoldForMainScreenTextBox" fields="sslineNo"
                                                width="600" container="NULL" shouldMatch="true"/>
                            <cong:hidden name="sslineNo" id="sslineNo" />
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <%--4th row><--%>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Consignee
                            <cong:autocompletor name="consignee" template="tradingPartner" query="MAIN_SCREEN_CONSIGNEE" fields="consigneeNo"
                                                width="600" scrollHeight="300px" container="NULL" id="consignee"
                                                paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeCountryUnLocCode" 
                                                shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <cong:hidden name="consigneeNo" id="consigneeNo" />
                            <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
                            <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
                            <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
                            <input type="hidden" name="consigneeSearchCountry" id="consigneeSearchCountry"/>
                            <input type="hidden" name="consigneeCountryUnLocCode" id="consigneeCountryUnLocCode"/>
                        </cong:td>
                        <cong:td width="5%">
                            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle"
                                 title="Click here to edit House Consignee Search options" onclick="showClientSearchOption('${path}', 'Consignee');"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Destination
                            <cong:autocompletor name="destination" id="destination" query="IMPORT_MAIN_SCREEN_DESTINATION"
                                                template="one" width="500" fields="destinationName,NULL,NULL,destCountryCode" 
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" container="NULL" 
                                                shouldMatch="true" scrollHeight="200px"/>
                            <cong:hidden name="destinationName" id="destinationName"/>
                            <cong:hidden name="destCountryCode" id="destCountryCode"/>
                           </cong:td>
                            <cong:td width="5%">
                            <span id="checkBoxMouse" title="Include All Cities">
                                <cong:checkbox name="destinationAllcities" id="destinationAllcities" onclick="showAllcities();"  container="null"/>
                            </span>
                            </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Destination Region</cong:td>
                        <cong:td>
                            <html:select property="originRegion" styleId="originRegion" style="width:134px" 
                                         styleClass="smallDropDown textlabelsBoldforlcl" >
                                <html:option value="">Select One</html:option>
                                <html:optionsCollection name="regionList"/>
                            </html:select>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Quote By</cong:td>
                        <cong:td>
                            <cong:autocompletor name="createdBy" id="createdBy" template="one" query="SALES_PERSON"  width="300" container="NULL"
                                                styleClass="textlabelsLclBoldForMainScreenCheckBox" scrollHeight="200px"/>
                            <cong:checkbox name="blcreatedBy" id="blcreatedBy" title="Me" onclick="defaultLoginNameCreatedBy()"/>
                            <input type="hidden" id="login" name="login" value="${user.loginName}"/>
                        </cong:td>
                    </cong:tr>
                    <%--5th row><--%>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Foreign Agent
                            <cong:autocompletor name="foreignAgent" id="foreignAgent" template="tradingPartner"  fields="foreignAgentAccount"
                                                query="MAIN_SCREEN_AGENT" params="I" width="600" scrollHeight="300px" 
                                                container="NULL" shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox" />
                            <cong:hidden name="foreignAgentAccount" id="foreignAgentAccount"/>
                        </cong:td>
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Filter By
                            <html:select property="filterBy" styleClass="smallDropDown textlabelsBoldforlcl" styleId="filterBy" 
                                         style="width:134px" onchange="enableInventoryCheckbox()">
                                <html:option value="All">All</html:option>
                                <html:option value="IWB">Inventory All</html:option>
                                <html:option value="Q">Quotation</html:option>
                                <html:option value="B" >Booking</html:option>
                                <html:option value="X">Terminated</html:option>
                                <html:option value="INAVAL">Inventory AVAL</html:option>
                                <html:option value="COH">Cargo on Hold</html:option>
                                <html:option value="CGO">Cargo in General Order</html:option>
                                <html:option value="CNR">Cargo not Released</html:option>
                            </html:select>
                        </cong:td>
                        <cong:td width="5%" align="left">
                            <span id="checkBoxMouse" title="Include Bookings">
                                <cong:checkbox name="filterByInventory" id="filterByInventory" onclick="includeBookings()" container="null"/>
                            </span>
                            <span id="icon" style="vertical-align: bottom;height: 10px">
                                <cong:img src="${path}/img/icons/iicon.png" width="12" height="14" id="filterOptions" 
                                          alt="filter" onclick="showFilterOptions('${path}')"/>
                            </span>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Master BL</cong:td>
                        <cong:td>
                            <cong:text id="masterBl" name="masterBl" styleClass="textlabelsLclBoldForMainScreenTextBox" 
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Booked By</cong:td>
                        <cong:td>
                            <cong:autocompletor name="bookedBy" id="bookedBy" template="one" query="SALES_PERSON"  width="300" 
                                                scrollHeight="200px" container="NULL" styleClass="textlabelsLclBoldForTextBox"/>
                            <cong:checkbox name="blbookedBy" id="blbookedBy" title="Me" onclick="defaultLoginNameBookedBy()"/>
                        </cong:td>
                    </cong:tr>
                    <%--5th row><--%>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Warehouse Doc#
                            <cong:text id="warehouseDocNo" name="warehouseDocNo" styleClass="textlabelsLclBoldForMainScreenTextBox" 
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Customer Po#
                            <cong:text id="customerPo" name="customerPo" styleClass="textlabelsLclBoldForMainScreenTextBox" 
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Tracking #</cong:td>
                        <cong:td>
                            <cong:text id="trackingNo" name="trackingNo" styleClass="textlabelsLclBoldForMainScreenTextBox" 
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Inbond #</cong:td>
                        <cong:td>
                            <cong:text id="inbondNo" name="inbondNo" styleClass="textlabelsLclBoldForMainScreenTextBox" 
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <%--6th row><--%>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Sub House
                            <cong:text id="subHouse" name="subHouse" styleClass="textlabelsLclBoldForMainScreenTextBox"
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">AMS House BL
                            <cong:text id="amsHBL" name="amsHBL" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                        </cong:td>
                        <cong:td width="1%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Choose Template</cong:td>
                        <cong:td>
                            <cong:div id="templateBox">
                                <c:import url="/jsps/LCL/ajaxload/templateSelectBox.jsp"/>
                            </cong:div>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Limit</cong:td>
                        <cong:td>
                            <html:select property="limit" styleId="limit" styleClass="smallDropDown textlabelsBoldforlcl" style="width:134px">
                                <html:option value="100">100</html:option>
                                <html:option value="150">150</html:option>
                                <html:option value="250">250</html:option>
                                <html:option value="500">500</html:option>
                                <html:option value="1000">1000</html:option>
                            </html:select>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <%--7th row><--%>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Sales Code
                            <cong:text id="salesCode" name="salesCode" styleClass="textlabelsLclBoldForMainScreenTextBox" onchange="validateSalescode(this)"/>
                        </cong:td>
                        <cong:td width="5%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">IPI Load Number
                            <cong:text id="ipiLoadNo" name="ipiLoadNo" styleClass="textlabelsLclBoldForMainScreenTextBox"
                                       onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td colspan="6">&nbsp;</cong:td>
                    </cong:tr>
                    <%-- button row--%>
                    <cong:tr >
                        <cong:td>
                            <input type="button"  value="Apply Defaults" id="LclUserDefaults" align="middle" 
                                   class="button-style1" onclick="openLclUserDefaults('${path}');"/>
                            <input type="button" class="button-style1" value="LCL Defaults" align="middle"  id="lclDefaults" 
                                   onclick="return GB_show('Lcl User Defaults', '${path}/lclUserDefaults.do?methodName=display', 250, 550);"/>
                        </cong:td>
                        <cong:td></cong:td>
                        <cong:td>
                            <c:choose>
                                <c:when test="${lclSearchForm.commodity eq 'I'}">
                                    <input type="radio" name="commodity" id="imperial" value="I" checked/>Imperial
                                    <input type="radio" name="commodity" id="metric" value="M" />Metric
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" name="commodity" id="imperial" value="I" />Imperial
                                    <input type="radio" name="commodity" id="metric" value="M" checked/>Metric
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                        <cong:td colspan="5">
                            <input type="button" value="New Quote" align="middle" class="button-style1" 
                                   onclick="checkFileNumber('${path}', '', 'Quotes');"/>
                            <input type="button" value="New DR" align="middle" class="button-style1" 
                                   onclick="checkFileNumber('${path}', '', 'Bookings');"/>
                            <input type="button"  value="Search" align="middle" class="button-style1" onclick="submitForm('search');"/>
                            <input type="button"  value="Reset" align="middle" class="button-style1" onclick="showProgressBar();
                                    submitResetForm();"/>
                            <c:if test="${user.role.roleDesc eq 'Admin'}">
                                <input type="button" value="Add Template" class="button-style1 template" onclick="openTemplate('${path}');"/>
                            </c:if>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                </cong:table>
                <span class="red bold" style="font-size: xx-small">  * All Search Fields Should Be Minimum 3 Characters</span>
                <cong:hidden name="methodName" id="methodName"/>
                <cong:hidden id="moduleName" name="moduleName" value="${lclSession.selectedMenu}"/>
            </cong:form>
        </cong:div>
    </cong:body>
</html>