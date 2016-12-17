<%--
    Document   : invoice Mapping
    Created on : 23 July, 2014, 3:01:25 PM
    Author     : Vijay
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Invoice Mapping Details</title>
        <%@include file="../init.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/tabs.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/eculineInvoice.js"></script>
    </head>
    <body>
        <br/>
        <div id="search-criteria" class="head-tag font-14px">
            Search Criteria
        </div>
        <br/>
        <html:form action="/lclEculineInvoice.do" name="eculineInvoiceForm"
                   styleId="eculineInvoiceForm" type="com.gp.cvst.logisoft.struts.form.lcl.EculineInvoiceForm" scope="request" method="post">
            <table class="width-50pc">
                <tr>
                    <td class="label">Eculine Desc</td>
                    <td>
                        <cong:autocompletor name="srcChargeDesc" template="one" id="srcChargeDesc" query="ECULINE_CHARGE_DESC"
                                            fields="" styleClass="textlabelsBoldForTextBox textCap" value="${ecuChargeDesc}"
                                            width="250" container="NULL" shouldMatch="false" scrollHeight="200px" callback="search('byUnitNo')"/>
                    </td>
                    <td>
                        <input type="hidden" id="methodName" name="methodName"/>
                    </td>
                    <td colspan="3"></td>
                    <td>
                        <input type="button" value="Search" class="button" onclick="search('byDesc');"/>
                        <input type="button" value="Search All" class="button" onclick="search('all');"/>
                        <input type="button" value="Reset" class="button" onclick="resetAll();"/>
                    </td>
                </tr>
            </table>
            <br/>
            <div id="search-results" class="head-tag font-14px">
                Search Results &nbsp;&nbsp;&nbsp;&nbsp; <input type="button" value="Save All" class="button" onclick="updateChargeCodeMappingAll( 'updateChargeMapping');"/>
                <div class="float-right">
                    <div class="float-left">
                        <c:choose>
                            <c:when test="${eculineInvoiceForm.totalRows gt eculineInvoiceForm.selectedRows}">
                                <%--${eculineInvoiceForm.selectedRows} records displayed.--%> ${eculineInvoiceForm.totalRows} records found.
                            </c:when>
                            <c:when test="${eculineInvoiceForm.selectedRows gt 1}">${eculineInvoiceForm.selectedRows} records found.</c:when>
                            <c:otherwise>1 record found.</c:otherwise>
                        </c:choose>
                    </div>
                    <c:if test="${eculineInvoiceForm.totalPages gt 1 and eculineInvoiceForm.selectedPage gt 1}">
                        <a title="First page" href="javascript: gotoPage('1')">
                            <img alt="First page" title="First page" src="${path}/images/first.png"/>
                        </a>
                        <a title="Previous page" href="javascript: gotoPage('${eculineInvoiceForm.selectedPage-1}')">
                            <img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
                        </a>
                    </c:if>
                    <c:if test="${eculineInvoiceForm.totalPages gt 1}">
                        <select id="selectedPageNo" class="dropdown float-left">
                            <c:forEach begin="1" end="${eculineInvoiceForm.totalPages}" var="selectedPage">
                                <c:choose>
                                    <c:when test="${eculineInvoiceForm.selectedPage eq selectedPage}">
                                        <option value="${selectedPage}" selected>${selectedPage}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${selectedPage}">${selectedPage}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                        <a href="javascript: gotoSelectedPage()">
                            <img alt="Goto Page" title="Goto Page" src="${path}/images/go.jpg"/>
                        </a>
                    </c:if>
                    <c:if test="${eculineInvoiceForm.totalPages gt eculineInvoiceForm.selectedPage}">
                        <a title="Next page" href="javascript: gotoPage('${eculineInvoiceForm.selectedPage + 1}')">
                            <img alt="Next Page" title="Next page" src="${path}/images/next.png"/>
                        </a>
                        <a title="Last page" href="javascript: gotoPage('${eculineInvoiceForm.totalPages}')">
                            <img alt="Last Page" title="Last page" src="${path}/images/last.png"/>
                        </a>
                    </c:if>
                    <html:hidden styleId="pageNo" property="selectedPage"/>
                </div>
            </div>
            <table class="display-table">
                <thead>
                    <tr>
                        <th>Eculine Desc</th>
                        <th>Charge Code</th>
                        <th>Cost Code</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="i" value="0"/>
                    <c:forEach items="${chargesList}" var="charge" varStatus="st">
                        <c:choose>
                            <c:when test="${rowStyle eq 'odd'}">
                                <c:set var="rowStyle" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="rowStyle" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${rowStyle}">
                            <%-- Eculine Desc Starts--%>
                            <td>${charge.eculineDesc}
                                <input type="hidden" name="ecuChargedesc" id="charge-desc${i}" value="${charge.eculineDesc}"/>
                            </td>
                            <%-- Charge Code Starts--%>
                            <td align="center">
                                <cong:autocompletor name="ecuChargeCode" id="chargeCode${i}" template="two" styleClass="textbox "
                                                    value="${charge.chargeCode}" params="LCLI" scrollHeight="150"
                                                    fields="NULL,NULL,chargeBlueCode${i}" width="350" query="CHARGE_CODE"
                                                    container="NULL" shouldMatch="true" />
                                <input id="chargeBlueCode${i}" name="ecuBlueChargeCode" type="hidden" value="${charge.bluChargeCode}" />
                            </td>
                            <%-- Cost Code Starts--%>
                            <td align="center">
                                <cong:autocompletor name="ecuCostCode" id="costCode${i}" template="two" styleClass="textbox "
                                                    value="${charge.costCode}" params="LCLI" scrollHeight="150"
                                                    fields="null,NULL,costBlueCode${i}" width="350" query="COST_CODE"
                                                    container="NULL" shouldMatch="true"/>
                                <input id="costBlueCode${i}" name="ecuBlueCostCode" type="hidden" value="${charge.bluCostCode}"/>
                            </td>
                            <%-- Charge Actions Starts--%>
                            <td>
                                <img alt="Update" title="Update" src="${path}/img/icons/save.gif"
                                     onclick="updateChargeCodeMapping( '${i}', 'updateChargeMapping');"/>
                            </td>
                        </tr>
                        <html:hidden styleId="eculineChargedesc" property="eculineChargedesc"/>
                        <html:hidden styleId="blueChargeCode" property="blueChargeCode"/>
                        <html:hidden styleId="blueCostCode" property="blueCostCode"/>
                        <html:hidden styleId="costCode" property="costCode"/>
                        <html:hidden styleId="chargeCode" property="chargeCode"/>
                        <c:set var="i" value="${i+1}"/>
                    </c:forEach>
                </tbody>
            </table>
                    <html:hidden styleId="methodName" property="methodName"/>
                    <html:hidden styleId="mappingSaveAllFlag" property="mappingSaveAllFlag"/>
        </html:form>
    </body>
</html>
