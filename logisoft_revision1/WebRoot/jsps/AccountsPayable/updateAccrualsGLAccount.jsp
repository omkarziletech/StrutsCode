<%-- 
    Document   : validGLAccount
    Created on : Jan 6, 2012, 6:06:28 PM
    Author     : logiware
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <title>Valid GL Account</title>
        <%@include file="../includes/jspVariables.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/AccrualsBC.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
        <script type="text/javascript" src="${path}/js/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/accruals" name="accrualsForm" type="com.gp.cvst.logisoft.struts.form.AccrualsForm" scope="request">
            <div class="scrolldisplaytable">
                <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="accrualTable" style="border: 1px solid white">
                    <thead>
                        <tr>
                            <th>Vendor Name</th>
                            <th>Vendor Number</th>
                            <th>Invoice Number</th>
                            <th>B/L</th>
                            <th>Accrued Amount</th>
                            <th>Container</th>
                            <th>Dock Receipt</th>
                            <th>Voyage Number</th>
                            <th>Cost Code</th>
                            <th>BlueScreen ChargeCode</th>
                            <th>Shipment Type</th>
                            <th>Terminal</th>
                            <th>GL Account</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="index" value="0"></c:set>
                        <c:forEach var="accrual" items="${accruals}">
                            <c:choose>
                                <c:when test="${zebra=='odd'}">
                                    <c:set var="zebra" value="even"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="odd"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${zebra}">
                                <td>
                                    <label title="${accrual.custName}">
                                        <c:choose>
                                            <c:when test="${fn:length(accrual.custName)>20}">
                                                ${fn:substring(accrual.custName,0,20)}...
                                            </c:when>
                                            <c:otherwise>${accrual.custName}</c:otherwise>
                                        </c:choose>
                                    </label>
                                </td>
                                <td>${accrual.custNo}</td>
                                <td>${accrual.invoiceNumber}</td>
                                <td>${accrual.billLaddingNo}</td>
                                <td>${accrual.balance}</td>
                                <td>
                                    <label title="${accrual.containerNo}">
                                        <c:choose>
                                            <c:when test="${fn:length(accrual.containerNo)>20}">
                                                ${fn:substring(accrual.containerNo,0,20)}...
                                            </c:when>
                                            <c:otherwise>${accrual.containerNo}</c:otherwise>
                                        </c:choose>
                                    </label>
                                </td>
                                <td>${accrual.docReceipt}</td>
                                <td>${accrual.voyageNo}</td>
                                <td>
                                    <input type="text" name="costCode" id="costCode${index}" class="textlabelsBoldForTextBox" value="${accrual.chargeCode}"/>
                                    <input type="hidden" name="costCodeValid" id="costCodeValid${index}" value="${accrual.chargeCode}"/>
                                    <div id="costCodeDiv${index}" style="display: none" class="newAutoComplete"/>
                                </td>
                                <td>
                                    <input type="text" name="blueScreenChargeCode" id="blueScreenChargeCode${index}" value="${accrual.blueScreenChargeCode}"
                                           class="textlabelsBoldForTextBoxDisabledLook" readonly/>
                                </td>
                                <td>
                                    <input type="text" name="shipmentType" id="shipmentType${index}" value="${accrual.shipmentType}"
                                           class="textlabelsBoldForTextBoxDisabledLook" readonly/>
                                </td>
                                <td>
                                    <input type="text" name="terminal" id="terminal${index}" class="textlabelsBoldForTextBox" 
                                           style="display:none;" onchange="deriveGlAccount(${index})"/>
                                </td>
                                <td>
                                    <input type="text" name="glAccount" id="glAccount${index}" value="${accrual.glAccountNumber}"
                                           class="textlabelsBoldForTextBoxDisabledLook" readonly/>
                                </td>
                                <td align="center">
                                    <input type="button" id="updateBtn${index}" class="buttonStyleNew" value="Update" 
                                           onclick="updateAccruals('${accrual.transactionId}','${index}')"/>
                                    <input type="hidden" name="deriveYn" id="deriveYn${index}" value=""/>
                                    <input type="hidden" name="accrualId" id="accrualId${index}" value="${accrual.transactionId}"/>
                                </td>
                            </tr>
                            <c:set var="index" value="${index+1}"/>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </html:form>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/updateAccrualsGLAccount.js"></script>
    </body>
</html>
