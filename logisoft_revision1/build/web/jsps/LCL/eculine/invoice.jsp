<%-- 
    Document   : invoice
    Created on : Aug 6, 2013, 3:01:25 PM
    Author     : Rajesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Voyage Details</title>
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
        <script type="text/javascript">
            $(document).ready(function () {
                showTabs();
            });
        </script>
    </head>
    <body>
        <input type="button" value="Go Back" class="button" style="margin-left: 10px;"onclick="getVoyageDetails('${path}', '${id}', '${bol}');"/>
        <br/>
        <%-- Header Part --%>
        <div id="voy-detail" class="head-tag font-14px" style="margin-left: 10px;">
            <span>
                Invoice details of HBL #
                <span class="red" style="cursor: text;">${bol}</span>
                &nbsp; &nbsp; File Number #
                <span class="red" style="cursor: text;">${fileNo}</span>
                <c:if test="${roleDuty.lclEcuInvoiceMapp }">
                    <span class="red" style="cursor: text;margin-left:850px;"  >
                        <input type="button" value="Code Mapping" class="button post-invoice" id ="codeMapping" onclick="openCodeMapping('${path}');"/>
                    </span></c:if>
            </div>
            <br/>
        <%-- Tabs --%>
        <ul class="tabs" style="width: 98%">
            <c:forEach items="${invoices}" var="invoice" varStatus="status">
                <fmt:formatDate var="createdDate" value="${invoice.value[0].createdDate}" pattern="dd-MMM-yyyy"/>
                <c:set var="isPosted" value="Posted"/>
                <c:forEach items="${invoice.value}" var="charge" varStatus="st1">
                    <c:if test="${isPosted eq 'Posted' && charge.invoiceStatus ne 1}">
                        <c:set var="isPosted" value="Open"/>
                    </c:if>
                </c:forEach>
                <li>
                    <c:choose>
                        <c:when test="${ isPosted eq 'Posted'}">
                            <a href="#tab${status.count}" class="bg-reviewed">
                                <span>${createdDate} <-></span>
                                <span class="green">${invoice.key}</span>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="#tab${status.count}">
                                <span>${createdDate} <-></span>
                                <span class="red">${invoice.key}</span>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </li>

            </c:forEach>
        </ul>
        <%-- Tab Contents --%>
        <html:form action="/lclEculineInvoice.do" name="eculineInvoiceForm"
                   styleId="invoiceForm" type="com.gp.cvst.logisoft.struts.form.lcl.EculineInvoiceForm" scope="request" method="post">
            <jsp:useBean id="billToParty" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO"/>
            <c:set var="billToPartyList" value="${billToParty.billToPartyForEculine}"/>
            <div class="tab-container"  style="overflow: auto;width:98%;">
                <c:forEach items="${invoices}" var="invoice" varStatus="status">
                    <c:set var="chargeErr" value="false"/>
                    <c:set var="isPosted" value="Posted"/>
                    <c:set var="isPostedClass" value="green" />
                    <div id="tab${status.count}" class="tab-content">
                        <table id="invoice-tbl${status.count}" class="display-table">
                            <thead>
                                <tr>
                                    <th>Vendor Code</th>
                                    <th>Eculine Desc</th>
                                    <th>Charge Code</th>
                                    <th>Charge Amount</th>
                                    <th>Bill To Party</th>
                                    <th>Cost Code</th>
                                    <th>Cost Amount</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Currency</th>
                                    <th>exchange rate</th>
                                    <th>Dispute</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not  invoice.value[0].blApproved}">
                                    <img alt="Process" title="" width="17px" height="17px" src="${path}/jsps/LCL/images/warning.png" class="edit" />
                                    <span class="blueBold"  style="margin-left: 10px; font-size: 14px; font-weight: bold ;" >House Bl is not linked to DR ,You cant post or dispute .</span>
                                </c:when>
                                <c:when test="${ invoice.value[0].blApproved && not invoice.value[0].containerApproved }">
                                    <img alt="Process" title="" width="17px" height="17px" src="${path}/jsps/LCL/images/warning.png" class="edit" />
                                    <span class="warningStyle"  style="margin-left: 10px; font-size: 14px; font-weight: bold ;" >Container is not approved ,You cant submit charges And costs .</span>
                                </c:when>
                            </c:choose>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach items="${invoice.value}" var="charge" varStatus="st">
                                <c:set var="i" value="${status.count}${st.count}"/>
                                <tr class="${zebra}">
                                    <td> <c:if test="${charge.apAmount ne '0.00' && charge.apAmount ne '0'}"><span title="${charge.vendorName}">${charge.vendorNo}</span></td></c:if>
                                        <td>
                                            <input type="text" id="charge-desc${i}" readonly value="${charge.eculineDesc}"  title="${charge.eculineDesc}" class="textbox"
                                               style="border: 0px none; background: transparent"/>
                                    </td>
                                    <%-- Charge Code Starts--%>
                                    <td>
                                        <c:choose>
                                            <c:when test="${charge.invoiceStatus eq '1' }">
                                                <cong:autocompletor name="chargeCode" id="chargeCode${i}" template="two" styleClass="textbox text-readonly" disabled="true"
                                                                    value="${charge.chargeCode}" params="LCLI" scrollHeight="150"
                                                                    fields="NULL,chargeArGlId${i},chargeBlueCode${i}" width="350" query="CHARGE_CODE"
                                                                    container="NULL" shouldMatch="true"/>
                                            </c:when>
                                            <c:otherwise>
                                                <cong:autocompletor name="chargeCode" id="chargeCode${i}" template="two" styleClass="textbox text-readonly" disabled="true"
                                                                    value="${charge.chargeCode}" params="LCLI" scrollHeight="150"
                                                                    fields="NULL,chargeArGlId${i},chargeBlueCode${i}" width="350" query="CHARGE_CODE"
                                                                    container="NULL" shouldMatch="true"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <input id="chargeArGlId${i}" type="hidden" value="${charge.chargeId}"/>
                                        <input id="chargeBlueCode${i}" type="hidden" value="${charge.bluChargeCode}"/>
                                    </td>
                                    <%-- Charge Code Ends--%>
                                    <%-- Charge Amount Starts--%>
                                    <td>
                                        <c:choose>
                                            <c:when test="${charge.invoiceStatus eq '1' }">
                                                <input type="text" id="arAmount${i}" value="${charge.arAmount}" readOnly="true" disabled="true" class="textbox amount text-readonly" onchange="validateTotalAmount('${i}')"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" id="arAmount${i}" value="${charge.arAmount}" readOnly="true" class="textbox amount text-readonly" onchange="validateTotalAmount('${i}')"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${charge.chargeStatus ne 'P' && charge.chargeStatus ne 'D'}">
                                            <img alt="changeCode" title="Change Charge Amount" src="${path}/images/icons/edit.gif"  onclick="changeChargeAmt('${i}');" />
                                        </c:if>
                                        <c:if test="${charge.chargeFlag eq 'C' && charge.arAmount eq '0.00'}">
                                            <img src="${path}/img/icons/iicon.png" width="14" height="14" alt="arAmount" id="arAmnt" title="This cost code is set to COST Only"/>
                                        </c:if></td>
                                        <%-- Charge Amount Ends--%>
                                    <td>
                                        <html:select property="hiddenBillToParty" styleId="hiddenBillToParty${i}"  styleClass="smallDropDown mandatory textlabelsBoldforlcl"
                                                     onchange="changeBillToParty('${i}');"  value="${charge.arBillToParty}">
                                            <html:option  value="">Select</html:option>
                                            <html:optionsCollection name="billToPartyList"/>
                                        </html:select>
                                        <input type="hidden" name="billToParty"  id="billToParty${i}" value="${charge.arBillToParty}"/>
                                    </td>
                                    <%-- Cost Code Starts--%>
                                    <td>
                                        <c:choose>
                                            <c:when test="${charge.invoiceStatus eq '1' }">
                                                <cong:autocompletor name="costCode" id="costCode${i}" template="two" styleClass="textbox text-readonly" disabled="true"
                                                                    value="${charge.costCode}" params="LCLI" scrollHeight="150"
                                                                    fields="null,chargeApGlId${i},costBlueCode${i}" width="350" query="COST_CODE"
                                                                    container="NULL" shouldMatch="true"/>
                                            </c:when>
                                            <c:otherwise>
                                                <cong:autocompletor name="costCode" id="costCode${i}" template="two" styleClass="textbox text-readonly" disabled="true"
                                                                    value="${charge.costCode}" params="LCLI" scrollHeight="150"
                                                                    fields="null,chargeApGlId${i},costBlueCode${i}" width="350" query="COST_CODE"
                                                                    container="NULL" shouldMatch="true"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <input id="chargeApGlId${i}" type="hidden" value="${charge.costId}"/>
                                        <input id="costBlueCode${i}" type="hidden" value="${charge.bluCostCode}"/>
                                    </td>
                                    <%-- Cost Code End--%>
                                    <%-- Cost Amount Starts--%>
                                    <td>
                                        <c:choose>
                                            <c:when test="${charge.invoiceStatus eq '1' }">
                                                <input type="text" id="apAmount${i}" value="${charge.apAmount}" readOnly="true" disabled="true" class="textbox amount text-readonly" onchange="validateTotalAmount('${i}')"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" id="apAmount${i}" value="${charge.apAmount}" readOnly="true" class="textbox amount text-readonly" onchange="validateTotalAmount('${i}')"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${charge.chargeStatus ne 'P' && charge.chargeStatus ne 'D'}">
                                            <img alt="changeCode" title="Change Cost Amount" src="${path}/images/icons/edit.gif" onclick="changeCostAmt('${i}');"/>
                                        </c:if>
                                        <c:if test="${charge.chargeFlag eq 'S' && charge.apAmount eq '0.00'}">
                                            <img src="${path}/img/icons/iicon.png" width="14" height="14" alt="apAmount" id="apAmnt" title="This cost code is set to CHARGE Only"/>
                                        </c:if></td>
                                        <%-- Cost Amount Ends--%>
                                    <td>${charge.quantity}</td>
                                    <td>
                                        <fmt:formatNumber value="${charge.price}" pattern="0.00"/>
                                        <input type="hidden" id="pricel${i}" value="${charge.price}"  />
                                    </td>
                                    <td>${charge.currency}</td>
                                    <td>${charge.rate}</td>
                                    <%-- Dispute Starts--%>
                                    <td>
                                        <c:choose>
                                            <c:when test="${charge.chargeStatus eq 'P' || charge.chargeStatus eq 'D' }">
                                                <input type="checkbox" id="dispute${i}" class="dispute text-readonly" title="Dispute" disabled="true"
                                                       ${charge.chargeStatus eq 'D' ? "Checked" :""}
                                                       onchange="updateCharge('${charge.invoiceId}', '${i}', 'doDispute');"/>
                                            </c:when><c:when test="${invoice.value[0].blApproved}">
                                                <input type="checkbox" id="dispute${i}" class="dispute " title="Dispute"
                                                       ${charge.chargeStatus eq 'D' ? "Checked" :""}
                                                       onchange="updateCharge('${charge.invoiceId}', '${i}', 'doDispute');"/>
                                            </c:when>
                                            <c:otherwise>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <%-- Dispute Ends--%>
                                    <td>
                                        <%-- Error checks Starts--%>
                                        <c:if test="${chargeErr eq 'false' && empty charge.chargeStatus}">
                                            <c:set var="chargeErr" value="true"/>
                                        </c:if>
                                        <c:if test="${isPosted eq 'Posted' && charge.invoiceStatus ne 1}">
                                            <c:set var="isPosted" value="Open"/>
                                            <c:set var="isPostedClass" value="red" />
                                        </c:if>
                                        <img alt="notes" title="Notes" src="${path}/images/icons/notes.png"
                                             class="notes" onclick="openNotes('${path}', '${charge.invoiceNo}');"/>
                                        <%-- Error checks Ends--%>

                                        <%-- Charge Actions Starts--%>
                                        <c:if test="${charge.invoiceStatus eq '0' }">
                                            <img alt="Update" title="Update" src="${path}/img/icons/save.gif"
                                                 onclick="updateCharge('${charge.invoiceId}', '${i}', 'updateCharge','${invoice.key}');"/></c:if>
                                        <c:choose>
                                            <c:when test="${not empty charge.vendorNo && not empty charge.chargeCode && empty charge.chargeStatus && invoice.value[0].blApproved}">
                                                <img alt="Ready to Post" title="Ready to Post" src="${path}/images/icons/check-yellow.png"
                                                     title="Approve" align="top" class="approveInvoice" onclick="submitCharge('${charge.invoiceId}', '${i}', 'submitInvoice' ,'${charge.fileContainChargesFlag}','#containerId');"/>
                                            </c:when>
                                            <c:when test="${charge.chargeStatus eq 'P'}">
                                                <img alt="Approved" title="Posted" src="${path}/jsps/LCL/images/approve.png"/>
                                            </c:when>
                                            <c:when test="${charge.chargeStatus eq 'D'}">
                                                <img alt="Approved" title="Disputed" src="${path}/jsps/LCL/images/D_Alphabet.gif"/>
                                            </c:when>
                                        </c:choose>
                                        <%-- Charge Actions ends--%>
                                    </td>
                                </tr>
                                <input type="hidden" id="isPosted${i}" value="${charge.chargeStatus}" />
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            </tbody>
                        </table>
                        <%--Submit ALL--%>
                        <%--
                        <c:if test="${invoice.value[0].containerApproved && invoice.value[0].blApproved && chargeErr ne 'true'
                                      && isPosted ne 'Posted'}">
                              <input type="button" value="Submit" class="button post-invoice" onclick="postInvoice('${invoice.key}', '${status.count}');"/>
                        </c:if>--%>
                        <%--Submit ALL ENds--%>
                        <br/>
                        <div id="invoice-detail" class="head-tag font-14px" style="cursor: text;">
                            Invoice # :
                            <span class="red">${invoice.key}</span>
                            <span style="position: absolute; left: 315px; text-align:right;">
                                Invoice Status:
                                <span class="${isPostedClass}">${isPosted}</span>
                            </span>
                        </div>
                        <%-- hidden fields --%>
                        <html:hidden styleId="invoiceNo" property="invoiceNo"/>
                        <html:hidden styleId="chargeDesc" property="chargeDesc"/>
                        <html:hidden styleId="chargeCodes" property="chargeCodes"/>
                        <html:hidden styleId="chargeId" property="chargeId"/>
                        <html:hidden styleId="apGlId" property="apGlId"/>
                        <html:hidden styleId="arGlId" property="arGlId"/>
                        <html:hidden styleId="blueChargeCode" property="blueChargeCode"/>
                        <html:hidden styleId="blueCostCode" property="blueCostCode"/>
                        <html:hidden styleId="arAmount" property="arAmount"/>
                        <html:hidden styleId="apAmount" property="apAmount"/>
                        <html:hidden styleId="price" property="price"/>
                        <html:hidden styleId="chargeAmount" property="chargeAmount"/>
                        <html:hidden styleId="costAmount" property="costAmount"/>
                        <html:hidden styleId="chargeBillToParty" property="chargeBillToParty"/>
                        <html:hidden styleId="eculineChargedesc" property="eculineChargedesc"/>
                        <html:hidden styleId="methodName" property="methodName"/>
                        <html:hidden styleId="dispute" property="dispute"/>
                        <html:hidden styleId="isPosted" property="isPosted" value="${isPosted}"/>
                        <html:hidden styleId="blNo" property="blNo" value="${bol}"/>
                        <html:hidden styleId="containerId" property="containerId" value="${id}"/>
                        <html:hidden styleId="fileNumberId" property="fileNumberId" value="${fileNumberId}"/>
                        <html:hidden styleId="rowIndex" property="rowIndex" value="${invoiceForm.rowIndex}"/>
                        <input type="hidden" id="fileNumberId" value="${fileNumberId}"/>
                    </div>
                </c:forEach>
            </div>
        </html:form>
        <%-- Tab Contents Ends--%>
    </body>
</html>
