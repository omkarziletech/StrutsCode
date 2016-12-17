<%--
    Document   : mainScreen
    Created on : Nov 7, 2011, 4:51:27 PM
    Author     : Thamizh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dcong:td">
<html xmlns="http://www.w3.org/1999/xhtml">


    <%@page import="com.gp.cong.logisoft.domain.User"%>
    <%@include file="init.jsp" %>
    <%@include file="/jsps/preloader.jsp" %>
    <%@include file="../includes/jspVariables.jsp"%>
    <%@include file="/jsps/includes/baseResources.jsp" %>
    <%@include file="../includes/resources.jsp" %>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <%@include file="colorBox.jsp"%>
    <%@include file="/taglib.jsp"%>
    <%        User user = (User) request.getSession().getAttribute("loginuser");
        request.setAttribute("user", user);
    %>
    <cong:body style="height:500px">
        <cong:div id="pane">
            <cong:form id="lclSearchForm" name="lclSearchForm" method="post" action="lclSearch.do">
                <jsp:useBean id="portsDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.PortsDAO"/>
                <jsp:useBean id="lclTemplateDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO"/>
                <c:set var="regionList" value="${portsDAO.allRegions}" scope="request"/>
                <c:set var="templateList" value="${lclTemplateDAO.allTemplate}" scope="request"/>
                <span id="warning" style="color:red"> </span>
                <cong:hidden name="methodName" id="methodName"/>
                <%-- file number label --%>
                <c:choose>
                    <c:when test="${lclSession.selectedMenu eq 'Imports'}">
                        <c:set var="fileNoLabel" value="DR No"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="fileNoLabel" value="File No"/>
                    </c:otherwise>
                </c:choose>
                <cong:table styleClass="widthFull" cellspacing="2" border="0">
                    <tr class="tableHeadingNew"><td colspan="12">File Search</td></tr>
                    <tr><td colspan="12"></td></tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">${fileNoLabel}</cong:td>
                            <cong:td>
                                <cong:text name="fileNumber" id="fileNumber" styleClass="textlabelsLclBoldForMainScreenTextBox"
                                           onkeyup="searchEmptyDateValues()"/>
                            </cong:td>
                            <c:choose>
                                <c:when test="${lclSession.selectedMenu!='Imports'}">
                                    <cong:td styleClass="textlabelsBoldforlcl" width="13%">Current Location</cong:td>
                                <cong:td width="10%">
                                    <cong:autocompletor name="currentLocation" id="currentLocation" template="one" width="250" container="NULL" query="PORT"
                                                        styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300px"
                                                        fields="currentLocName,NULL,NULL,currentLocCode"/>
                                    <cong:hidden name="currentLocName" id="currentLocName"/>
                                    <cong:hidden name="currentLocCode" id="currentLocCode"/>
                                </cong:td>
                                <cong:td width="1%" align="left">
                                    <span id="checkBoxMouse" title="Include INTR">
                                        <cong:checkbox name="includeIntr" id="includeIntr" container="null"/>
                                    </span></cong:td>
                            </c:when>
                            <c:otherwise>
                                <cong:td styleClass="textlabelsBoldforlcl" width="13%">Origin</cong:td>
                                <cong:td width="10%">
                                    <cong:autocompletor name="origin" id="origin" template="one" width="250" container="NULL" query="IMPORT_MAIN_SCREEN_ORIGIN"
                                                        styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300"
                                                        fields="portName,NULL,NULL,countrycode"/>
                                    <cong:hidden name="portName" id="portName"/>
                                    <cong:hidden name="countryCode" id="countrycode"/>
                                </cong:td>

                                <cong:td>

                                </cong:td>

                            </c:otherwise>
                        </c:choose>
                        <cong:td></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" width="13%">From Date</cong:td>
                        <cong:td><cong:calendarNew  id="from-date" name="startDate"  styleClass="textlabelsBoldForTextBox"/></cong:td>
                        <cong:td width="8%">&nbsp;</cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">To Date</cong:td>
                        <cong:td><cong:calendarNew  id="endDate"  name="endDate" styleClass="textlabelsBoldForTextBox"/></cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl"> Client </cong:td>
                        <cong:td>
                            <cong:autocompletor name="client" id="client" template="tradingPartner"  fields="clientNo" query="MAIN_SCREEN_CLIENT" scrollHeight="300"  width="600" container="NULL" shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox" />
                            <cong:hidden name="clientNo" id="clientNo" />
                        </cong:td>
                        <c:choose>
                            <c:when test="${lclSession.selectedMenu!='Imports'}">
                                <cong:td styleClass="textlabelsBoldforlcl" width="13%">Origin</cong:td>
                                <cong:td width="10%">
                                    <cong:autocompletor name="origin" id="origin" template="one" width="250" container="NULL" query="PORT"
                                                        styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300" 
                                                        fields="portName,NULL,NULL,countrycode"/>
                                    <cong:hidden name="portName" id="portName"/>
                                    <cong:hidden name="countryCode" id="countrycode"/>
                                </cong:td>
                                <cong:td>
                                </cong:td>
                            </c:when>
                            <c:otherwise>
                                <cong:td styleClass="textlabelsBoldforlcl">POL</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclSession.selectedMenu=='Imports'}">
                                            <c:set var="polQuery" value="IMPORT_MAIN_SCREEN_ORIGIN"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="polQuery" value="PORT"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <cong:autocompletor name="pol" id="pol" template="one" width="250" container="NULL" query="${polQuery}"
                                                        styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300" value="${portOfLoading}"
                                                        fields="polName,NULL,NULL,polCountryCode"/>
                                    <cong:hidden name="polName" id="polName"/>
                                    <cong:hidden name="polCountryCode" id="polCountryCode"/>
                                </cong:td>
                                <cong:td>
                                </cong:td>
                            </c:otherwise>
                        </c:choose>
                        <cong:td></cong:td>

                        <cong:td styleClass="textlabelsBoldforlcl">Issuing Terminal</cong:td>
                        <cong:td>
                            <cong:autocompletor name="issuingTerminal" id="issuingTerminal" template="terminal" query="MAIN_SCREEN_TERMINAL" width="200"
                                                scrollHeight="200" container="NULL" styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true"/>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Container No</cong:td>
                        <cong:td><cong:text name="containerNo"  id="containerNo"   styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/></cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl"> Shipper </cong:td>
                        <cong:td>
                            <cong:autocompletor name="shipperName" id="shipperName" template="tradingPartner" query="MAIN_SCREEN_SHIPPER"
                                                fields="shipperNo" width="600" scrollHeight="300" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode" container="NULL" shouldMatch="true"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
                            <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
                            <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
                            <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
                            <input type="hidden" name="shipperCountryUnLocCode" id="shipperCountryUnLocCode"/>
                            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                                 title="Click here to edit House Shipper Search options" onclick="showClientSearchOption('${path}', 'Shipper')"/>
                            <cong:hidden name="shipperNo" id="shipperNo" />
                        </cong:td>
                        <c:choose>
                            <c:when test="${lclSession.selectedMenu!='Imports'}">
                                <cong:td styleClass="textlabelsBoldforlcl">POL</cong:td>
                                <c:choose>
                                    <c:when test="${lclSession.selectedMenu=='Imports'}">
                                        <c:set var="polUnLocQuery" value="ORIGIN_UNLOC"/>
                                    </c:when>
                                    <c:otherwise><c:set var="polUnLocQuery" value="PORT"/></c:otherwise>
                                </c:choose>
                                <cong:td>
                                    <cong:autocompletor name="pol" id="pol" template="one" width="250" container="NULL" query="${polUnLocQuery}"
                                                        styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300" 
                                                        fields="polName,NULL,NULL,polCountryCode"/>
                                    <cong:hidden name="polName" id="polName"/>
                                    <cong:hidden name="polCountryCode" id="polCountryCode"/>
                                </cong:td>
                            </c:when>
                            <c:otherwise>
                                <cong:td styleClass="textlabelsBoldforlcl">POD</cong:td>
                                <c:set var="destUnLocQuery" value="CONCAT_RELAY_NAME_FD"/>
                                <cong:td> <cong:autocompletor name="pod" id="pod" query="IMPORT_MAIN_SCREEN_DESTINATION" template="one" width="500" fields="podName,NULL,NULL,podCountryCode"
                                                              styleClass="textlabelsLclBoldForMainScreenTextBox" container="NULL" shouldMatch="true" scrollHeight="200"/></cong:td>
                                <cong:hidden name="podName" id="podName"/>
                                <cong:hidden name="podCountryCode" id="podCountryCode"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:td></cong:td>
                        <cong:td></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Origin Region</cong:td>
                        <cong:td>
                            <html:select property="originRegion" styleId="originRegion" style="width:134px" styleClass="smallDropDown textlabelsBoldforlcl" >
                                <html:option value="">Select One</html:option>
                                <html:optionsCollection name="regionList"/>
                            </html:select>

                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">SSL</cong:td>
                        <cong:td>
                            <cong:autocompletor name="ssl" id="ssl" template="tradingPartner" position="left" query="MAIN_SCREEN_SSLINE" 
                                                scrollHeight="300" styleClass="textlabelsLclBoldForMainScreenTextBox"  width="600"  fields="sslineNo"
                                                container="NULL" shouldMatch="true"/>
                            <cong:hidden name="sslineNo" id="sslineNo" />
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl"> Forwarder  </cong:td>
                        <cong:td>
                            <cong:autocompletor name="forwarder" template="tradingPartner" query="MAIN_SCREEN_FORWARDER" fields="forwarderNo"
                                                scrollHeight="300" width="600" container="NULL" shouldMatch="true"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <cong:hidden name="forwarderNo" id="forwarderNo" />
                        </cong:td>
                        <c:choose>
                            <c:when test="${lclSession.selectedMenu!='Imports'}">
                                <cong:td styleClass="textlabelsBoldforlcl">POD</cong:td>
                                <c:set var="destUnLocQuery" value="CONCAT_RELAY_NAME_FD"/>
                                <cong:td> <cong:autocompletor name="pod" id="pod" query="${destUnLocQuery}" template="one" width="500" fields="podName,NULL,NULL,podCountryCode" 
                                                              styleClass="textlabelsLclBoldForMainScreenTextBox" container="NULL" shouldMatch="true" scrollHeight="200"/></cong:td>
                                <cong:hidden name="podName" id="podName"/>
                                <cong:hidden name="podCountryCode" id="podCountryCode"/>
                            </c:when>
                            <c:otherwise>
                                <cong:td styleClass="textlabelsBoldforlcl">Destination</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="destination" id="destination" query="IMPORT_MAIN_SCREEN_DESTINATION" template="one" width="500"
                                                        fields="destinationName,NULL,NULL,destCountryCode" styleClass="textlabelsLclBoldForMainScreenTextBox" container="NULL" shouldMatch="true" scrollHeight="200"/>
                                    <cong:hidden name="destinationName" id="destinationName"/>
                                    <cong:hidden name="destCountryCode" id="destCountryCode"/>
                                </cong:td>
                            </c:otherwise>
                        </c:choose>
                        <cong:td></cong:td>
                        <cong:td></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Destination Region</cong:td>
                        <cong:td>
                            <html:select property="destinationRegion" styleId="destinationRegion" style="width:134px" styleClass="smallDropDown textlabelsBoldforlcl" >
                                <html:option value="">Select One</html:option>
                                <html:optionsCollection name="regionList"/>
                            </html:select>

                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Quote By</cong:td>
                        <cong:td width="10px">
                            <cong:autocompletor name="createdBy" id="createdBy" template="one" query="SALES_PERSON"  width="300" container="NULL"
                                                styleClass="textlabelsLclBoldForMainScreenCheckBox" scrollHeight="200"/>
                            <cong:checkbox name="blcreatedBy" id="blcreatedBy" title="Me" onclick="defaultLoginNameCreatedBy()"/>
                            <input type="hidden" id="login" name="login" value="${user.loginName}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl"> Consignee  </cong:td>
                        <cong:td>
                            <cong:autocompletor name="consignee" template="tradingPartner" query="MAIN_SCREEN_CONSIGNEE" fields="consigneeNo"
                                                width="600" scrollHeight="300" container="NULL" paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeCountryUnLocCode" shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <cong:hidden name="consigneeNo" id="consigneeNo" />
                            <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
                            <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
                            <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
                            <input type="hidden" name="consigneeSearchCountry" id="consigneeSearchCountry"/>
                            <input type="hidden" name="consigneeCountryUnLocCode" id="consigneeCountryUnLocCode"/>
                            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                                 title="Click here to edit House Consignee Search options" onclick="showClientSearchOption('${path}', 'Consignee');"/>
                        </cong:td>
                        <c:choose>
                            <c:when test="${lclSession.selectedMenu!='Imports'}">
                                <cong:td styleClass="textlabelsBoldforlcl">Destination</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="destination" id="destination" query="${destUnLocQuery}" template="one" width="500"
                                                        fields="destinationName,NULL,NULL,destCountryCode" styleClass="textlabelsLclBoldForMainScreenTextBox" container="NULL" shouldMatch="true" scrollHeight="200"/>
                                    <cong:hidden name="destinationName" id="destinationName"/>
                                    <cong:hidden name="destCountryCode" id="destCountryCode"/>
                                </cong:td>
                                <cong:td></cong:td>
                            </c:when>
                            <c:otherwise>
                                <cong:td styleClass="textlabelsBoldforlcl" style="text-align :right">Filter By</cong:td>
                                <cong:td>
                                    <html:select property="filterBy" styleClass="smallDropDown textlabelsBoldforlcl" styleId="filterBy" style="width:134px" onchange="enableInventoryCheckbox()">
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
                                    <cong:td width="1%" align="left">
                                        <span id="checkBoxMouse" title="Include Bookings">
                                            <cong:checkbox name="filterByInventory" id="filterByInventory" onclick="includeBookings()" container="null"/>
                                        </span>
                                        <span id="icon" style="vertical-align: bottom;height: 10px">
                                            <cong:img src="${path}/img/icons/iicon.png" width="12" height="14" id="filterOptions" alt="filter" onclick="showFilterOptions('${path}')"/>
                                        </span>
                                    </cong:td>
                                </cong:td>
                            </c:otherwise>
                        </c:choose>
                        <cong:td></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">SSL Booking #</cong:td>
                        <cong:td><cong:text  id="sslBookingNo" name="sslBookingNo" styleClass="textlabelsLclBoldForMainScreenCheckBox" onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Booked By</cong:td>
                        <cong:td width="10px">
                            <cong:autocompletor name="bookedBy" id="bookedBy" template="one" query="SALES_PERSON"  width="300" scrollHeight="200"
                                                container="NULL" styleClass="textlabelsLclBoldForTextBox"/>
                            <cong:checkbox name="blbookedBy" id="blbookedBy" title="Me" onclick="defaultLoginNameBookedBy()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Foreign Agent</cong:td>
                        <cong:td>
                            <c:choose>
                                <c:when test="${lclSession.selectedMenu=='Imports'}">
                                    <c:set var="foreignAgentType" value="I"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="foreignAgentType" value="E"/>
                                </c:otherwise>
                            </c:choose>
                            <cong:autocompletor name="foreignAgent" id="foreignAgent" template="tradingPartner"  fields="foreignAgentAccount"
                                                query="MAIN_SCREEN_AGENT" params="${foreignAgentType}" scrollHeight="300"  width="600" container="NULL"
                                                shouldMatch="true" styleClass="textlabelsLclBoldForMainScreenTextBox" />
                        </cong:td>
                        <cong:hidden name="foreignAgentAccount" id="foreignAgentAccount"/>
                        <c:choose>
                            <c:when test="${lclSession.selectedMenu!='Imports'}">
                                <cong:td styleClass="textlabelsBoldforlcl" style="text-align :right">Filter By</cong:td>
                                <cong:td>
                                    <html:select property="filterBy" styleClass="smallDropDown textlabelsBoldforlcl" styleId="filterBy" style="width:134px" onchange="enableInventoryCheckbox()">
                                        <html:option value="IWB">Inventory All</html:option>
                                        <html:option value="IPO">Inventory Pickups Only</html:option>
                                        <html:option value="All">All</html:option>
                                        <html:option value="Q">Quotation</html:option>
                                        <html:option value="B" >Booking</html:option>
                                        <html:option value="RF" >Refused</html:option>
                                        <html:option value="X">Terminated</html:option>
                                        <html:option value="BL">Loaded with no B/L</html:option>
                                        <html:option value="BP">BL Pool</html:option>
                                        <html:option value="ONBK">Online Booking</html:option>
                                    </html:select>
                                    <cong:td width="1%" align="left">
                                        <span id="checkBoxMouse" title="Include Bookings">
                                            <cong:checkbox name="filterByInventory" id="filterByInventory" onclick="includeBookings()" container="null"/>
                                        </span>
                                        <span id="icon" style="vertical-align: bottom;height: 10px">
                                            <cong:img src="${path}/img/icons/iicon.png" width="12" height="14" id="filterOptions" alt="filter" onclick="showFilterOptions('${path}');"/>
                                        </span>
                                    </cong:td>
                                </cong:td>
                            </c:when>
                            <c:otherwise>
                                <cong:td styleClass="textlabelsBoldforlcl">Master BL</cong:td>
                                <cong:td>
                                    <cong:text id="masterBl" name="masterBl" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                                </cong:td>
                                <cong:td></cong:td>
                            </c:otherwise>
                        </c:choose>
                        <cong:td></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Inbond Number</cong:td>
                        <cong:td><cong:text id="inbondNo" name="inbondNo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/></cong:td>
                        <cong:td>&nbsp;</cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Choose Template</cong:td>
                        <cong:td>
                            <cong:div id="templateBox">
                                <c:import url="/jsps/LCL/ajaxload/templateSelectBox.jsp"/>
                            </cong:div>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <c:if test="${lclSession.selectedMenu!='Imports'}">
                            <cong:td styleClass="textlabelsBoldforlcl">CFCL</cong:td>
                            <cong:td>
                                <html:select property="cfcl" styleId="cfcl" styleClass="smallDropDown textlabelsBoldforlcl">
                                    <html:option value="0">Exclude</html:option>
                                    <html:option value="1">Only</html:option>
                                    <html:option value="">Include</html:option>
                                </html:select>
                            </cong:td>
                            <c:choose><c:when test="${lclSession.selectedMenu!='Imports'}">
                                    <cong:td styleClass="textlabelsBoldforlcl">Master BL</cong:td>
                                    <cong:td>
                                        <cong:text id="masterBl" name="masterBl" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                                    </cong:td>
                                    <cong:td></cong:td>
                                    <cong:td></cong:td>
                                </c:when>
                                <c:otherwise>
                                    <cong:td styleClass="textlabelsBoldforlcl">Customer Po #</cong:td>
                                    <cong:td>
                                        <cong:text id="customerPo" name="customerPo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                                    </cong:td>
                                    <cong:td></cong:td>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <c:if test="${lclSession.selectedMenu=='Imports'}">
                            <cong:td styleClass="textlabelsBoldforlcl">Warehouse Doc #</cong:td>
                            <cong:td>
                                <cong:text id="warehouseDocNo" name="warehouseDocNo" 
                                           styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                            </cong:td>
                        </c:if>
                        <c:choose><c:when test="${lclSession.selectedMenu!='Imports'}">
                                <cong:td styleClass="textlabelsBoldforlcl">Customer Po #</cong:td>
                                <cong:td>
                                    <cong:text id="customerPo" name="customerPo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                                </cong:td>
                            </c:when>
                            <c:otherwise>
                                <cong:td styleClass="textlabelsBoldforlcl">Customer Po #</cong:td>
                                <cong:td>
                                    <cong:text id="customerPo" name="customerPo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                                </cong:td>
                            </c:otherwise>
                        </c:choose>
                        <cong:td>&nbsp;</cong:td>
                        <c:if test="${lclSession.selectedMenu=='Imports'}">
                            <cong:td>&nbsp;</cong:td>
                        </c:if>
                        <cong:td styleClass="textlabelsBoldforlcl">Tracking No #</cong:td>
                        <cong:td>
                            <cong:text id="trackingNo" name="trackingNo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                        </cong:td>
                        <cong:td>&nbsp;</cong:td>
                        <c:if test="${lclSession.selectedMenu eq 'Imports'}">
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Sub House</cong:td>
                                <cong:td>
                                    <cong:text id="subHouse" name="subHouse" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">AMS House BL</cong:td>
                                <cong:td>
                                    <cong:text id="amsHBL" name="amsHBL" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                                </cong:td>
                                <cong:td></cong:td>
                                <cong:td></cong:td>
                                <cong:td></cong:td>
                                <cong:td></cong:td>
                                <cong:td></cong:td>
                                <cong:td>&nbsp;</cong:td>
                            </cong:tr>
                        </c:if>
                    </cong:tr>
                    <c:if test="${lclSession.selectedMenu!='Imports'}">
                        <cong:tr>
                            <c:choose><c:when test="${lclSession.selectedMenu!='Imports'}">
                                    <cong:td styleClass="textlabelsBoldforlcl">CFCL Account #</cong:td>
                                    <cong:td>
                                        <cong:autocompletor name="cfclAccount"  id="cfclAccount" template="tradingPartner"
                                                            query="CFCL_ACCOUNTNO"  width="600" scrollHeight="300" container="NULL" shouldMatch="true"
                                                            styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                                    </cong:td>
                                </c:when>
                                <c:otherwise>
                                    <cong:td styleClass="textlabelsBoldforlcl">Master BL</cong:td>
                                    <cong:td>
                                        <cong:text id="masterBl" name="masterBl" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                                    </cong:td>
                                </c:otherwise>
                            </c:choose>
                            <cong:td styleClass="textlabelsBoldforlcl" width="13%">Booked For Voyage</cong:td>
                            <cong:td>
                                <cong:autocompletor id="bookedForVoyage" name="bookedForVoyage" width="500" container="NULL" query="BOOKED_VOYAGE" paramFields="polCountryCode,podCountryCode"
                                                    template="two" styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="200" />
                            </cong:td>
                            <cong:td>
                                <span style="vertical-align: bottom;height: 10px" title="Press Spacebar to see entire Voyage List">
                                    <cong:img src="${path}/img/icons/iicon.png" width="12" height="14" id="bookedForVoyageIcon" alt="filter"/>
                                </span>
                            </cong:td>
                            <cong:td></cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl">New Quote Copy From</cong:td>
                            <cong:td>
                                <cong:text id="copyQuote" name="copyQuote" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                                <font style="float:none" class="button-style1" onclick="copyQuote('Y')">Go
                                </font>
                            </cong:td>
                            <cong:td>
                                &nbsp;
                            </cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl">New Booking Copy From</cong:td>
                            <cong:td>
                                <cong:text id="copyBooking" name="copyBooking" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            </cong:td>
                            <cong:td>
                                <div style="float:left" class="button-style1" onclick="copyBooking('Y')">Go
                                </div>
                            </cong:td>
                        </cong:tr>
                    </c:if>
                    <c:if test="${lclSession.selectedMenu!='Imports'}">
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Warehouse Doc #</cong:td>
                            <cong:td>
                                <cong:text id="warehouseDocNo" name="warehouseDocNo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="searchEmptyDateValues()"/>
                            </cong:td>
                            <cong:td></cong:td>
                            <cong:td></cong:td>
                            <cong:td></cong:td>
                            <cong:td></cong:td>
                            <cong:td></cong:td>
                            <cong:td></cong:td>
                            <cong:td></cong:td>
                            <c:if test="${lclSession.selectedMenu ne 'Imports'}">
                                <cong:td styleClass="textlabelsBoldforlcl"> BL Pool Owner  </cong:td>
                                <cong:td width="10px">
                                    <cong:autocompletor name="blPoolOwner" id="blPoolOwner" template="one" query="SALES_PERSON"  width="300" scrollHeight="200"
                                                        container="NULL" styleClass="textlabelsLclBoldForTextBox" shouldMatch="true"/>
                                    <cong:checkbox name="blOwner" id="blOwner" title="Me" onclick="defaultLoginNameBlOwner()"/>
                                </cong:td>
                            </c:if>
                        </cong:tr>
                    </c:if>
                    <cong:tr>
                        <cong:td colspan="2">
                            <input type="button"  value="Apply Defaults" id="LclUserDefaults" align="middle" class="button-style1" onclick="openLclUserDefaults('${path}');"/>
                            <input type="button" class="button-style1" value="LCL Defaults" align="middle"  id="lclDefaults" onclick="return GB_show('Lcl User Defaults', '${path}/lclUserDefaults.do?methodName=display', 250, 550);"/>
                        </cong:td>
                        <cong:td></cong:td>
                        <cong:td>
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
                        </cong:td>
                        <cong:td colspan="5">
                            <input type="button" value="New Quote" align="middle" class="button-style1" onclick="checkFileNumber('${path}', '', 'Quotes');"/>
                            <c:choose>
                                <c:when test="${lclSession.selectedMenu eq 'Imports'}">
                                    <c:set value="New DR" var="buttonValue"/>
                                </c:when><c:otherwise>
                                    <c:set value="New Bkg" var="buttonValue"/>
                                </c:otherwise>
                            </c:choose>
                            <input type="button" value="${buttonValue}" align="middle" class="button-style1" onclick="checkFileNumber('${path}', '', 'Bookings');"/>
                            <c:if test="${lclSession.selectedMenu ne 'Imports'}">
                                <input type="button" id="quickdr"  value="Quick Bkg" align="middle" class="button-style1" onclick="quickDR('${path}');"/>
                            </c:if>
                            <input type="button"  value="Search" align="middle" class="button-style1" onclick="submitForm('search');"/>
                            <input type="button"  value="Reset" align="middle" class="button-style1" onclick="showProgressBar();
                                    submitResetForm();"/>
                            <c:if test="${user.role.roleDesc eq 'Admin'}">
                                <input type="button" value="Add Template" class="button-style1 template" onclick="openTemplate('${path}');"/>
                            </c:if>
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
                    <cong:hidden id="moduleName" name="moduleName" value="${lclSession.selectedMenu}"/>
                </cong:table>
            </cong:form><span  class="red bold"  style="font-size: xx-small">  * All Search Fields Should Be Minimum 3 Characters</span>
        </cong:div>
        <cong:div>
        </cong:div>

        <script type="text/javascript">
            $(document).keypress(function (e) {
                if (e.which == 13) {
                    submitForm("search");
                    return false;
                }
            });
            jQuery(document).ready(function () {
                $("[title != '']").not("link").tooltip();
                var moduleName = $('#moduleName').val();
                if (moduleName == 'Imports') {
                    $('#metric').attr('checked', true);
                }
                if ((document.getElementById("from-date").value == null || document.getElementById("from-date").value == "") &&
                        (document.getElementById("endDate").value == null || document.getElementById("endDate").value == ""))
                {
                    searchEmptyDateValues();
                }
                var cfclValue = $("#cfcl").val();
                if (moduleName != 'Imports') {
                    if (cfclValue == "1" || cfclValue == "") {
                        allFilter();
                    }
                }
            });
            function inventoryAllFilter() {
                document.getElementById("filterBy").value = "IWB";
                $('#filterByInventory').attr('disabled', false);
                $('#filterByInventory').attr('checked', true);
            }
            function allFilter() {
                document.getElementById("filterBy").value = "All";
                $('#filterByInventory').attr('disabled', true);
                $('#filterByInventory').attr('checked', false);
            }
            $('#origin').keyup(function () {
                var origin = $('#origin').val();
                if (origin == "") {
                    $('#origin').val('');
                    $('#portName').val('');
                    $('#countrycode').val('');
                }
            });
            $('#currentLocation').keyup(function () {
                var currentLocation = $('#currentLocation').val();
                if (currentLocation == "") {
                    $('#currentLocation').val('');
                    $('#currentLocName').val('');
                    $('#currentLocCode').val('');
                }
            });

            $('#pol').keyup(function () {
                var pol = $('#pol').val();
                if (pol == "") {
                    $('#pol').val('');
                    $('#polName').val('');
                    $('#polCountryCode').val('');
                }
            });
            $('#pod').keyup(function () {
                var pod = $('#pod').val();
                if (pod == "") {
                    $('#pod').val('');
                    $('#podName').val('');
                    $('#podCountryCode').val('');
                }
            });
            $('#destination').keyup(function () {
                var destination = $('#destination').val();
                if (destination == "") {
                    $('#destination').val('');
                    $('#destinationName').val('');
                    $('#destCountryCode').val('');
                }
            });
            $('#client').keyup(function () {
                var client = $('#client').val();
                if (client == "") {
                    $('#client').val('');
                    $('#clientNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#shipperName').keyup(function () {
                var shipperName = $('#shipperName').val();
                if (shipperName == "") {
                    $('#shipperName').val('');
                    $('#shipperNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#forwarder').keyup(function () {
                var forwarder = $('#forwarder').val();
                if (forwarder == "") {
                    $('#forwarder').val('');
                    $('#forwarderNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#consignee').keyup(function () {
                var consignee = $('#consignee').val();
                if (consignee == "") {
                    $('#consignee').val('');
                    $('#consigneeNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();

                    }
                } else {
                    allFilter();
                }
            });
            $('#fileNumber').keyup(function () {
                var fileNumber = $('#fileNumber').val();
                if (fileNumber == "") {
                    $('#fileNumber').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#inbondNo').keyup(function () {
                var inbondNo = $('#inbondNo').val();
                if (inbondNo == "") {
                    $('#inbondNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#customerPo').keyup(function () {
                var customerPo = $('#customerPo').val();
                if (customerPo == "") {
                    $('#customerPo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#trackingNo').keyup(function () {
                var trackingNo = $('#trackingNo').val();
                if (trackingNo == "") {
                    $('#trackingNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#warehouseDocNo').keyup(function () {
                var warehouseDocNo = $('#warehouseDocNo').val();
                if (warehouseDocNo == "") {
                    $('#warehouseDocNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#ssl').keyup(function () {
                var ssl = $('#ssl').val();
                if (ssl == "") {
                    $('#ssl').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#sslBookingNo').keyup(function () {
                var sslBookingNo = $('#sslBookingNo').val();
                if (sslBookingNo == "") {
                    $('#sslBookingNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#createdBy').keyup(function () {
                var createdBy = $('#createdBy').val();
                if (createdBy == "") {
                    $('#createdBy').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#bookedBy').keyup(function () {
                var bookedBy = $('#bookedBy').val();
                if (bookedBy == "") {
                    $('#bookedBy').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#blPoolOwner').keyup(function () {
                var blPoolOwner = $('#blPoolOwner').val();
                if (blPoolOwner == "") {
                    $('#blPoolOwner').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#masterBl').keyup(function () {
                var masterBl = $('#masterBl').val();
                if (masterBl == "") {
                    $('#masterBl').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#containerNo').keyup(function () {
                var containerNo = $('#containerNo').val();
                if (containerNo == "") {
                    $('#containerNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#issuingTerminal').keyup(function () {
                var issuingTerminal = $('#issuingTerminal').val();
                if (issuingTerminal == "") {
                    $('#issuingTerminal').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#subHouse').keyup(function () {
                var subHouse = $('#subHouse').val();
                if (subHouse == "") {
                    $('#subHouse').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#amsHBL').keyup(function () {
                var subHouse = $('#amsHBL').val();
                if (subHouse == "") {
                    $('#amsHBL').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#cfclAccount').keyup(function () {
                var cfclAccount = $('#cfclAccount').val();
                if (cfclAccount == "") {
                    $('#cfclAccount').val('');
                    $('#cfclAccountNo').val('');
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#bookedForVoyage').keyup(function () {
                var bookedForVoyage = $('#bookedForVoyage').val();
                if (bookedForVoyage == "") {
                    $('#bookedForVoyage').val('');
                }
            });
            $('#foreignAgent').keyup(function () {
                var foreignAgent = $('#foreignAgent').val();
                var moduleName = $('#moduleName').val();
                if (moduleName == 'Exports') {
                    if (foreignAgent == "") {
                        $('#foreignAgent').val('');
                        inventoryAllFilter();
                    }
                } else {
                    if (foreignAgent == "") {
                        $('#foreignAgent').val('');
                        inventoryAllFilter();
                    } else {
                        allFilter();
                    }
                }
            });
            if (document.getElementById('filterBy').value == "IWB") {
                $('#filterByInventory').attr('disabled', false);
                $('#filterByInventory').attr('checked', true);
            } else {
                $('#filterByInventory').attr('checked', false);
                $('#filterByInventory').attr('disabled', true);
            }
            $('#cfcl').change(function () {
                var cfcl = $('#cfcl').val();
                if (cfcl == "0") {
                    inventoryAllFilter();
                } else {
                    allFilter();
                }
            });
            $('#originRegion').change(function () {
                var originRegion = $('#originRegion').val();
                if (originRegion == "") {
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            $('#destinationRegion').change(function () {
                var destinationRegion = $('#destinationRegion').val();
                if (destinationRegion == "") {
                    var moduleName = $('#moduleName').val();
                    if (moduleName !== 'Imports') {
                        inventoryAllFilter();
                    }
                } else {
                    allFilter();
                }
            });
            function submitForm(methodName) {
                if ((methodName === 'search')
                        && (($("#fileNumber").val().length !== 0 && $("#fileNumber").val().length < 3)
                                || ($("#cfclAccount").val().length !== 0 && $("#cfclAccount").val().length < 3)
                                || ($("#warehouseDocNo").val().length !== 0 && $("#warehouseDocNo").val().length < 3)
                                || ($("#masterBl").val().length !== 0 && $("#masterBl").val().length < 3)
                                || ($("#sslBookingNo").val().length !== 0 && $("#sslBookingNo").val().length < 3)
                                || ($("#inbondNo").val().length !== 0 && $("#inbondNo").val().length < 3)
                                || ($("#customerPo").val().length !== 0 && $("#customerPo").val().length < 3)
                                || ($("#containerNo").val().length !== 0 && $("#containerNo").val().length < 3)
                                || ($("#createdBy").val().length !== 0 && $("#createdBy").val().length < 3)
                                || ($("#bookedBy").val().length !== 0 && $("#bookedBy").val().length < 3)
                                || ($("#trackingNo").val().length !== 0 && $("#trackingNo").val().length < 3))) {
                    return;
                }
                var voyNo = $('#bookedForVoyage').val();
                var moduleName = $('#moduleName').val();
                if (voyNo != undefined && voyNo != null && voyNo != "") {
                    var bkVoyNo = voyNo.split('/');
                    $('#bookedForVoyage').val(bkVoyNo[0]);
                }
                if (moduleName === "Exports" && (document.getElementById('filterBy').value === "IWB" || document.getElementById('filterBy').value === "IPO")) {
                    $("#methodName").val("getCompanyName");
                    var params = $("#lclSearchForm").serialize();
                    var foreignAgent = $("#foreignAgent").val();
                    $.post($("#lclSearchForm").attr("action"), params, function (data) {
                        if (data === 'OTI' && foreignAgent === "") {
                            congAlert("Foreign Agent is required ");
                            $("#foreignAgent").css("border-color", "red");
                            $("#warning").show();
                        } else if (data === 'OTI' && foreignAgent != "") {
                            searchValidationForm(methodName);
                        } else if ($('#includeIntr').is(':checked') && $("#currentLocation").val() == '') {
                            congAlert("Please Enter Current Location");
                            $("#currentLocation").css("border-color", "red");
                            $("#warning").show();
                        } else if (document.getElementById('fileNumber').value == "" && document.getElementById('portName').value == "" &&
                                document.getElementById('client').value == "" && document.getElementById('polName').value == "" &&
                                document.getElementById('issuingTerminal').value == "" && document.getElementById('containerNo').value == "" &&
                                document.getElementById('shipperNo').value == "" && document.getElementById('podName').value == "" &&
                                document.lclSearchForm.originRegion.value == "" && document.getElementById('ssl').value == "" &&
                                document.lclSearchForm.destinationRegion.value == "" && document.getElementById('forwarderNo').value == "" &&
                                document.getElementById('destinationName').value == "" && document.getElementById('createdBy').value == "" &&
                                document.getElementById('consigneeNo').value == "" && document.getElementById('sslBookingNo').value == "" &&
                                document.getElementById('bookedBy').value == "" && document.getElementById('masterBl').value == "" &&
                                document.getElementById('inbondNo').value == "" && document.getElementById('foreignAgent').value == "" &&
                                document.getElementById('currentLocation').value == "") {
                            congAlert("Atleast One Search Criteria must be selected for this filter");
                        } else {

                            searchValidationForm(methodName);
                        }
                    });
                } else {
                    searchValidationForm(methodName);
                }
            }
            function searchValidationForm(methodName) {

                if ($('#fileNumber').val() === "" && $('containerNo').val() === "") {
                    var fromdate = $('#from-date').val();
                    var todate = $('#endDate').val();
                    if (fromdate === "" && todate === "") {
                        hideProgressBar();
                        congAlert("Please Enter From and End Date");
                        $("#from-date").css("border-color", "red");
                        $("#endDate").css("border-color", "red");
                        $("#warning").parent.show();
                        return false;
                    }
                    if (fromdate === "") {
                        hideProgressBar();
                        congAlert("Please Enter From date");
                        $("#from-date").css("border-color", "red");
                        $("#warning").parent.show();
                        return false;
                    }
                    else if (todate === "") {
                        hideProgressBar();
                        congAlert("Please fill the toDate");
                        $("#endDate").css("border-color", "red");
                        $("#warning").parent.show();
                        return false;
                    }
                } else {
                    window.parent.showLoading();
                    $("#methodName").val(methodName);
                    $("#lclSearchForm").submit();
                }
            }

            function submitResetForm() {
                var moduleName = $('#moduleName').val();
                $('#fileNumber').val('');
                $('#client').val('');
                $('#sailDate').val('');
                $('#issuingTerminal').val('');
                $('#shipperName').val('');
                $('#destination').val('');
                $('#consignee').val('');
                $('#bookedBy').val('');
                $('#inbondNo').val('');
                $('#origin').val('');
                $('#pol').val('');
                $('#containerNo').val('');
                $('#pod').val('');
                $('#ssl').val('');
                $('#forwarder').val('');
                $('#createdBy').val('');
                $('#sslBookingNo').val('');
                $('#masterBl').val('');
                $('#limit').val('100');
                $('#sortBy').val('');
                $('#destinationRegion').val('');
                $('#originRegion').val('');
                $('#cfcl').val('0');
                $('#cfclAccountNo').val('');
                $('#foreignAgent').val('');
                $('#foreignAgentAccount').val('');
                $('#cfclAccount').val('');
                $('#portName').val('');
                $('#forwarderNo').val('');
                $('#destinationName').val('');
                $('#consigneeNo').val('');
                $('#clientNo').val('');
                $('#polName').val('');
                $('#shipperNo').val('');
                $('#podName').val('');
                $('#customerPo').val('');
                $('#trackingNo').val('');
                $('#warehouseDocNo').val('');
                $('#countrycode').val('');
                $('#polCountryCode').val('');
                $('#podCountryCode').val('');
                $('#destCountryCode').val('');
                $('#copyQuote').val('');
                $('#copyBooking').val('');
                $('#currentLocation').val('');
                $('#blPoolOwner').val('');
                $('#currentLocName').val('');
                $('#currentLocCode').val('');
                $('#subHouse').val('');
                $('#amsHBL').val('');
                $('#blcreatedBy').attr('checked', false);
                $('#blOwner').attr('checked', false);
                $('#blbookedBy').attr('checked', false);
                $('#includeIntr').attr('checked', false);
                $('#metric').attr('checked', true);
                if (moduleName === 'Imports') {
                    $('#filterBy').val('All');
                    $('#metric').attr('checked', true);
                } else {
                    $('#filterBy').val('IWB');
                    $('#imperial').attr('checked', true);
                }
                searchEmptyDateValues();
                hideProgressBar();
            }

            function congAlert(txt) {
                $.prompt(txt);
            }

            function checkFileNumber(path, fileNumberId, moduleId) {
                var moduleName = $('#moduleName').val();
                window.parent.changeLclChilds(path, fileNumberId, moduleId, moduleName);
            }

            function openTemplate(path) {
                var href = path + "/lclTemplate.do?methodName=display";
                $(".template").attr("href", href);
                $(".template").colorbox({
                    iframe: true,
                    width: "80%",
                    height: "80%",
                    title: "Template"
                });
            }
            function searchEmptyDateValues() {
                if (document.getElementById("fileNumber").value !== "" || $('#containerNo').val() !== "" || $('#inbondNo').val() !== ""
                        || $('#masterBl').val() !== "" || $('#trackingNo').val() !== "" || $('#warehouseDocNo').val() !== ""
                        || $('#customerPo').val() !== "" || $('#sslBookingNo').val() !== "" || $('#amsHBL').val() !== ""
                        || $('#subHouse').val() !== "") {
                    document.getElementById("from-date").value = "";
                    document.getElementById("endDate").value = "";
                    document.getElementById("filterBy").value = "IWB";
                    $('#filterByInventory').attr('checked', true);
                    $('#filterByInventory').attr('disabled', false);
                }
                else {
                    var m_names = new Array("Jan", "Feb", "Mar",
                            "Apr", "May", "Jun", "Jul", "Aug", "Sep",
                            "Oct", "Nov", "Dec");
                    var d = new Date();
                    var curr_date = d.getDate();
                    var curr_month = d.getMonth();
                    var curr_year = d.getFullYear();
                    document.getElementById("endDate").value = curr_date + "-" + m_names[curr_month] + "-" + curr_year;
                    d.setDate(d.getDate() - 30);
                    curr_date = d.getDate();
                    curr_month = d.getMonth();
                    curr_year = d.getFullYear();
                    document.getElementById("from-date").value = curr_date + "-" + m_names[curr_month] + "-" + curr_year;
                }

            }

            function showFilterOptions(path) {
                jQuery.ajaxx({//printReport method changed due to change in FileName
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclPrintUtil",
                        methodName: "lclFilterOptionReport",
                        request: "true"
                    },
                    success: function (data) {
                        window.parent.showLightBox('Filter Details', '${path}/servlet/FileViewerServlet?fileName=' + data, 700, 800);
                    }
                });
            }

            function defaultLoginNameCreatedBy() {
                var login = $('#login').val();
                if ($('#blcreatedBy').is(":checked")) {
                    $('#createdBy').val(login);
                    allFilter();
                } else {
                    $('#createdBy').val('');
                    if ($('#blbookedBy').is(":checked")) {
                        $('#bookedBy').val(login);
                        allFilter();
                    } else {
                        $('#bookedBy').val('');
                        inventoryAllFilter();
                    }
                }
            }
            function defaultLoginNameBookedBy() {
                var login = $('#login').val();
                if ($('#blbookedBy').is(":checked")) {
                    $('#bookedBy').val(login);
                    allFilter();
                } else {
                    $('#bookedBy').val('');
                    if ($('#blcreatedBy').is(":checked")) {
                        $('#createdBy').val(login);
                        allFilter();
                    } else {
                        $('#bookedBy').val('');
                        inventoryAllFilter();
                    }
                }
            }
            function defaultLoginNameBlOwner() {
                var login = $('#login').val();
                if ($('#blOwner').is(":checked")) {
                    $('#blPoolOwner').val(login);
                    allFilter();
                } else {
                    $('#blPoolOwner').val('');
                    inventoryAllFilter();
                }
            }
            function enableInventoryCheckbox() {
                if (document.getElementById('filterBy').value == "IWB" || document.getElementById('filterBy').value == "BL" || document.getElementById('filterBy').value == "IPO") {
                    $('#filterByInventory').attr('disabled', false);
                    $('#filterByInventory').attr('checked', true);
                } else {
                    $('#filterByInventory').attr('disabled', true);
                    $('#filterByInventory').attr('checked', false);
                }
            }
            function copyQuote(copyFnVal) {
                if (($("#copyQuote").val().length !== 0 && $("#copyQuote").val().length < 3)) {
                    return;
                }
                var moduleName = $('#moduleName').val();
                var fileNumber = $('#copyQuote').val();
                if (fileNumber != null && fileNumber != "") {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "getStatus",
                            param1: fileNumber
                        },
                        success: function (data) {
                            if (data == 'Q') {
                                window.parent.showLoading();
                                var href = "${path}/lclQuote.do?methodName=copyQuote&fileNumber=" + fileNumber + "&copyFnVal=" + copyFnVal + "&moduleName=" + moduleName;
                                document.location.href = href;
                            } else {
                                if (data == "") {
                                    congAlert("Please Select existing Quote FileNumber");
                                } else {
                                    congAlert("Please Select Quote FileNumber");
                                }
                            }
                        }
                    });
                }
            }
            function copyBooking(copyFnVal) {
                if (($("#copyBooking").val().length !== 0 && $("#copyBooking").val().length < 3)) {
                    return;
                }
                var moduleName = $('#moduleName').val();
                var fileNumber = $('#copyBooking').val();
                if (fileNumber != null && fileNumber != "") {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "getStatus",
                            param1: fileNumber
                        },
                        success: function (data) {
                            if (data == 'B') {
                                window.parent.showLoading();
                                var href = "${path}/lclBooking.do?methodName=copyDr&fileNumber=" + fileNumber + "&copyFnVal=" + copyFnVal + "&moduleName=" + moduleName;
                                document.location.href = href;
                            } else {
                                if (data == "") {
                                    congAlert("Please Select existing Booking FileNumber");
                                } else {
                                    congAlert("Please Select Booking FileNumber");
                                }
                            }
                        }
                    });
                }
            }

            function shipper_AccttypeCheck() {
                target = jQuery("#accttype").val();
                subtype = jQuery("#subtype").val();
                var type;
                var subTypes;
                var array1 = new Array();
                var array2 = new Array();
                if (target != null) {
                    type = target;
                    array1 = type.split(",");
                }
                if (subtype != null) {
                    subTypes = (subtype).toLowerCase();
                    array2 = subTypes.split(",");
                }
                if (target != "") {
                    if (((!array1.contains('S') && target != 'E' && target != 'I' && array1.contains('V')) || (target == 'C')) && !array2.contains('forwarder')) {
                        congAlert("Please select the customers with account type S,E,I and V with subtype forwarder");
                        jQuery("#accttype").val('');
                        jQuery("#subtype").val('');
                        jQuery("#shipperNo").val('');
                        jQuery("#shipperName").val('');
                    }
                }
            }

            function openQuickQuote(path) {
                var href = path + "/lclSearch.do?methodName=openQuickQuote";
                $('#quickQuote').attr("href", href);
                $('#quickQuote').colorbox({
                    iframe: true,
                    width: "63%",
                    height: "80%",
                    title: "Quick Quote"
                });
            }
            function quickDR(path) {
                var href = path + "/jsps/LCL/quickDR.jsp";
                $('#quickdr').attr("href", href);
                $('#quickdr').colorbox({
                    iframe: true,
                    width: "63%",
                    height: "70%",
                    title: "Quick Booking"
                });
            }

            function openLclUserDefaults(path) {
                var href = path + "/lclSearch.do?methodName=applyUserDefaultValues";
                document.location.href = href;
            }
            function showClientSearchOption(path, searchByValue) {
                var href = path + "/lclBooking.do?methodName=clientSearch&searchByValue=" + searchByValue;
                $(".clientSearchEdit").attr("href", href);
                $(".clientSearchEdit").colorbox({
                    iframe: true,
                    width: "35%",
                    height: "40%",
                    title: searchByValue + " Search"
                });
            }
        </script>
    </cong:body>
</html>
