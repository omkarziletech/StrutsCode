<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dcong:td">
<html xmlns="http://www.w3.org/1999/xhtml">
    <%@include file="init.jsp"%>
    <%@include file="/jsps/preloader.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@include file="../includes/jspVariables.jsp"%>
    <%@include file="../includes/baseResources.jsp"%>
    <%@include file="/taglib.jsp"%>
    <cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
    <script type="text/javascript" src="${path}/jsps/LCL/js/lclVoyageArInvoice.js"></script>
    <body>
        <style>
            .show{
                display: block;
            }
            .hide{
                display: none;
            }
        </style>
        <cong:form name="lclVoyageArInvoiceForm" id="lclVoyageArInvoiceForm" action="lclVoyageArInvoice.do">
            <jsp:useBean id="glMappingDAO" scope="request" class="com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO"/>
            <jsp:useBean id="dbUtil" scope="request" class="com.gp.cong.logisoft.util.DBUtil"/>
            <c:set var="chargeCodeList" value="${glMappingDAO.lclChargeCodeArInvoice}" scope="request"/>
            <c:set var="terminalCodeList" value="${dbUtil.terminalCodeList}" scope="request"/>
            <cong:hidden id="fileNumber" name="fileNumber" value="${fileNumber}"/>
            <cong:hidden id="fileNumberId" name="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden id="arRedInvoiceId" name="arRedInvoiceId" value="${arRedInvoice.id}"/>
            <cong:hidden id="status" name="status" value="${arRedInvoice.status}"/>
            <cong:hidden id="pageName" name="pageName" value="${terminate}"/>
            <cong:hidden id="unitNo" name="unitNo" value="${lclVoyageArInvoiceForm.unitNo}"/>
            <cong:hidden id="selectedMenu" name="selectedMenu" />
            <cong:hidden id="voyageId" name="voyageId" value="${lclVoyageArInvoiceForm.voyageId}"/>
            <cong:hidden id="agentFlag" name="agentFlag" value="${lclVoyageArInvoiceForm.agentFlag}"/>
            <cong:hidden id="printOnDrFlag" name="printOnDrFlag"/>
            <cong:hidden  id="voyageTerminal" name="voyageTerminal" value="${lclVoyageArInvoiceForm.voyageTerminal}"/>
            <input type="hidden" id="newDate" name="newDate" value="${lclVoyageArInvoiceForm.date}"/>
            <cong:hidden  id="invoiceId" name="invoiceId"/>
            <c:set var="moduleName" value='${lclVoyageArInvoiceForm.selectedMenu}' />
            <c:choose><c:when test="${moduleName eq 'Imports'}">
                    <c:set var="shipmentType" value="LCLI"/>
                </c:when>
                <c:otherwise> <c:set var="shipmentType" value="LCLE"/></c:otherwise>
            </c:choose>
            <table style="width:100%">
                <tr class="tableHeadingNew">
                    <cong:td>AR Invoice: <span class="fileNo">
                            <c:choose>
                                <c:when test="${moduleName eq 'Imports'}">
                                    ${lclVoyageArInvoiceForm.unitNo}
                                </c:when>
                                <c:otherwise>
                                    <b class="printVoyageNoForExport"></b>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </cong:td>
                    <td>
                        <span id="blueBold" style="font-size: 14px">${displayMessage}
                            <c:if test="${arRedInvoice.status eq 'AR'}">
                                This invoice <c:out value="${arRedInvoice.invoiceNumber}"/> is posted to AR SuccessFully
                            </c:if>
                        </span>
                    </td>
                    <td align="right">
                        <c:choose><c:when test="${empty lclssheader.auditedBy && empty lclssheader.closedBy}">
                                <c:if test="${arRedInvoice.status eq 'AR'}">
                                    <cong:div styleClass="button-style1" onclick="searchAR('${path}','${lclVoyageArInvoiceForm.agentFlag}')" id="searchButton">Go Back</cong:div>
                                </c:if>
                                <c:if test="${!newItemFlag==true}"><cong:div styleClass="button-style1" onclick="searchAR('${path}','${lclVoyageArInvoiceForm.agentFlag}')" id="searchButton">Search</cong:div>
                                    <cong:div styleClass="button-style1" onclick="showLineItem()" id="addnewButton">Add New</cong:div>
                                    <c:if test="${lclVoyageArInvoiceForm.selectedMenu eq 'Imports'}">
                                        <cong:div styleClass="button-style1" onclick="openAgentInvoicePopup('${path}','${lclVoyageArInvoiceForm.fileNumberId}','${lclVoyageArInvoiceForm.voyageId}');"
                                                  id="agentInvoice">Agent Invoice    </cong:div>
                                    </c:if>
                                </c:if>
                            </c:when><c:otherwise> <c:if test="${!newItemFlag==true}">
                                    <cong:div styleClass="gray-background">Search</cong:div>
                                    <cong:div styleClass="gray-background">Add New</cong:div>
                                    <c:if test="${lclVoyageArInvoiceForm.selectedMenu eq 'Imports'}">
                                        <cong:div styleClass="gray-background" id="agentInvoice">Agent Invoice</cong:div>
                                    </c:if>
                                </c:if>
                            </c:otherwise></c:choose>
                        </td>
                    </tr>
                </table>
            <c:choose>
                <c:when test="${newItemFlag==true}">
                    <c:set var="showHide" value="show"/>
                </c:when>
                <c:otherwise>
                    <c:set var="showHide" value="hide"/>
                </c:otherwise>
            </c:choose>
            <cong:div id="addNew" style="width:100%; float:left" styleClass="${showHide}">
                <cong:table width="80%"  border="0" cellpadding="0" cellspacing="0">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Customer Name</cong:td>
                        <cong:td>
                            <cong:autocompletor name="customerName" template="tradingPartner" id="customerName"
                                                fields="customerNumber,customerType,NULL,NULL,NULL,NULL,NULL,customerDisabled,address,NULL,NULL,NULL,NULL,contactName,phoneNumber,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,customerDisableAcct"
                                                query="CLIENT_NO_CONSIGNEE" value="${arRedInvoice.customerName}"  width="400" scrollHeight="300" container="NULL" shouldMatch="true" styleClass="mandatory text" callback="verifyAcctType()"/>
                            <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openArContact()"/>
                            <input type="hidden" name="customerDisabled" id="customerDisabled"/>
                            <input type="hidden" name="customerDisableAcct" id="customerDisableAcct"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Customer Number</cong:td>
                        <cong:td><cong:text name="customerNumber" id="customerNumber" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${arRedInvoice.customerNumber}"/></cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="dueDate" value="${arRedInvoice.dueDate}"/>
                        <cong:td styleClass="textlabelsBoldforlcl">Due Date
                            <cong:calendar  name="dueDate" id="dueDate" styleClass="textlabelsBoldForTextBox" value="${dueDate}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Customer Type</cong:td>
                        <cong:td><cong:text  name="customerType" id="customerType"  styleClass="text" value="${arRedInvoice.customerType}"/> </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Contact Name</cong:td>
                        <cong:td><cong:text name="contactName" id="contactName"  styleClass="text" value="${arRedInvoice.contactName}"/> </cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="date1" value="${arRedInvoice.date}"/>
                        <cong:td styleClass="textlabelsBoldforlcl">Date
                            <c:choose> <c:when test="${arRedInvoice.date eq ''}">
                                    <cong:calendar name="date" id="date" styleClass="textlabelsBoldForTextBox" value=""/>
                                </c:when><c:otherwise>
                                    <cong:calendar name="date" id="date" styleClass="textlabelsBoldForTextBox" value="${date1}"/>
                                </c:otherwise></c:choose>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
                        <cong:td>
                            <cong:textarea name="address" id="address" styleClass="smallTextarea" style="width: 82%; height: 30px" value="${arRedInvoice.address}"></cong:textarea>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Phone</cong:td>
                        <cong:td><cong:text name="phoneNumber" id="phoneNumber"  styleClass="text" value="${arRedInvoice.phoneNumber}"/> </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Terms</cong:td>
                        <cong:td><cong:text name="termDesc" id="termDesc" style="width:150px"  styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" value="Due Upon Receipt" readOnly="true"/> </cong:td>
                        <input type="hidden" id="term" name="term" value="11344"/>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">For</cong:td>
                        <cong:td colspan="3">
                            <cong:textarea  name="notes" id="notes" styleClass="smallTextarea" value="${arRedInvoice.notes}" style="width: 87%; height: 40px;text-transform: uppercase">
                            </cong:textarea>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Invoice #</cong:td>
                        <cong:td><cong:text name="invoiceNumber" id="invoiceNumber" style="width:150px" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${arRedInvoice.invoiceNumber}"/></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td></cong:td><cong:td></cong:td>
                        <cong:td>
                            <c:if test="${arRedInvoice.status ne 'AR'}">
                                <input type="button" value="Save" id="save" class="button-style1" onclick="createInvoice('save');"/>
                                <c:if test="${not empty arRedInvoice.id}">
                                    <input type="button" value="Delete" class="button-style1" onclick="deleteArInvoice('${path}', '${lclVoyageArInvoiceForm.fileNumberId}', $('#voyageId').val(), $('#arRedInvoiceId').val(), false, true);"/>
                                </c:if>
                                <c:if test="${not empty invoiceChargeList && not empty arRedInvoice.id}">
                                    <input type="button" value="Post" id="post" class="button-style1 post" onclick="postInvoice('${path}', '${lclVoyageArInvoiceForm.fileNumberId}', '${lclVoyageArInvoiceForm.voyageId}', '${arRedInvoice.id}');"/>
                                </c:if>
                                <input type="button" class="button-style1" id="cancel" value="Cancel" onclick="backToInvoiceList('${path}', '${lclVoyageArInvoiceForm.fileNumberId}', $('#voyageId').val(), '${lclVoyageArInvoiceForm.agentFlag}');"/>
                            </c:if>
                        </cong:td>
                        <cong:td></cong:td><cong:td></cong:td>
                    </cong:tr>
                </cong:table>
            </cong:div>
            <c:if test="${not empty invoiceList}">
                <div id="arInvoiceList" style="height: 230px;" class="result-container">
                    <table width="100%" border="0" class="display-table">
                        <thead>
                            <tr>
                                <th>Customer No#</th>
                                <th>Customer Type</th>
                                <th>Invoice#</th>
                                <th>Invoice Amount</th>
                                <th>Invoice Date</th>
                                <th>Status</th>
                                <th>Posted Date</th>
                                <th>Created By</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="index" value="1"/>
                            <c:forEach items="${invoiceList}" var="invoice">
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr class="${zebra}">
                                    <td title="${invoice.customerName}<br>${invoice.customerNumber}">${invoice.customerNumber}</td><td>
                                        ${invoice.customerType}</td><td>
                                        ${invoice.invoiceNumber}</td><td>
                                        ${invoice.invoiceAmount}</td><td>
                                        ${invoice.dateAsString}</td><td>
                                        <c:choose>
                                            <c:when test="${invoice.status eq 'AR'}">
                                                <span style="color: green;font-weight: bold;">Posted</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: red;font-weight: bold;">In Progress</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <fmt:formatDate pattern="dd-MMM-yyyy" var="invoicePostedDate" value="${invoice.postedDate}"/><td>
                                        ${invoicePostedDate}</td><td>
                                        <span title="${fn:toUpperCase(invoice.invoiceBy)}">${fn:toUpperCase(fn:substring(invoice.invoiceBy,0,15))}</span></td><td>
                                        <img src="${path}/jsps/LCL/images/search_over.gif" alt="preview" title="Print Preview"
                                             style="cursor: pointer" onclick="previewArInvoice('', '${invoice.id}', '${path}');"/>
                                        <img src="${path}/img/icons/send.gif" style="cursor: pointer" title="Email/Fax"
                                             onclick="viewReports('${path}', '${invoice.invoiceNumber}','${invoice.id}');" />
                                        <c:choose><c:when test="${empty lclssheader.auditedBy && empty lclssheader.closedBy}">
                                                <img src="${path}/images/edit.png" alt="edit" style="cursor: pointer" title="Edit Invoice"
                                                     onclick="editArInvoice('${path}', '${lclVoyageArInvoiceForm.fileNumberId}', '${lclVoyageArInvoiceForm.voyageId}', '${invoice.id}', false, true, '${lclVoyageArInvoiceForm.agentFlag}');"/>
                                                <c:if test="${invoice.status eq 'AR' && roleDuty.reversePostedInvoices}">
                                                    <img src="${path}/img/icons/unpost1.png" alt="unpost" title="Reverse Post" style="cursor: pointer"
                                                         onclick="unPostInvoice('${path}', '${lclVoyageArInvoiceForm.fileNumberId}', $('#voyageId').val(), '${invoice.id}', '${lclVoyageArInvoiceForm.agentFlag}');"/>
                                                </c:if>
                                                <c:if test="${invoice.status ne 'AR'}">
                                                    <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                                         onclick="deleteInvoice('${invoice.id}');" title="Delete Invoice"/>
                                                </c:if>
                                            </c:when></c:choose>
                                        </td>
                                    </tr>
                                <c:set var="index" value="${index+1}"/>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <c:if test="${not empty arRedInvoice.id}">
                <div id="lineItemList" style="width:100%">
                    <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
                        <span style="margin-right: 35%"> List of Line Items</span>
                        <c:if test="${arRedInvoice.status ne 'AR'}">
                            <cong:div styleClass="floatRight button-style1" id="addList" onclick="showAddItem()">Add</cong:div>
                            <c:if test="${moduleName eq 'Imports'}">
                                <cong:div styleClass="floatRight button-style1"  onclick="addDrLevelAgentInvoicePopUp('${path}','${lclVoyageArInvoiceForm.fileNumberId}');">
                                    Add Released Charges</cong:div>
                            </c:if>
                        </c:if>
                    </cong:div>
                    <div id="redInvoiceCharge">
                        <c:import url="/jsps/LCL/ajaxload/lclVoyageArInvoiceCharge.jsp">
                            <c:param name="status" value="${arRedInvoice.status}"/>
                            <c:param name="unitNo" value="${lclVoyageArInvoiceForm.unitNo}"/>
                            <c:param name="selectedMenu" value="${lclVoyageArInvoiceForm.selectedMenu}"/>
                        </c:import>
                    </div>
                </div>
            </c:if>
            <cong:div id="addLineItem" style="width:100%;display:none">
                <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">Add Line Items </cong:div>
                <cong:table width="100%"  border="0" cellpadding="0" cellspacing="0">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">
                            <c:choose>
                                <c:when test="${moduleName eq 'Imports'}">
                                    Unit#
                                </c:when>
                                <c:otherwise>
                                    File#
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                        <cong:td>
                            <c:choose>
                                <c:when test="${moduleName eq 'Imports'}">
                                    <input type="text" name="unitNumber" id="unitNumber" class="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${lclVoyageArInvoiceForm.unitNo}"/>
                                </c:when>
                                <c:otherwise>
                                    <cong:text name="fileNumber" id="fileNumber" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Shipment Type</cong:td>
                        <cong:td><cong:text name="shipmentType" id="shipmentType" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${shipmentType}"/> </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Charge Code</cong:td>
                        <cong:td>
                            <cong:autocompletor name="chargeCode" id="chargeCode" template="two" query="CHARGE_CODE" fields="NULL,chargeId" shouldMatch="true" scrollHeight="150"
                                                params="${shipmentType}" position="left" container="NULL" styleClass="text mandatory require" width="350" />
                            <cong:hidden id="chargeId" name="chargeId"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Terminal Code</cong:td>
                        <cong:td>
                            <html:select property="terminal" style="width:150px;" styleClass="dropdown_accounting textlabelsBoldforlcl" styleId="terminal">
                                <html:option value="${lclVoyageArInvoiceForm.voyageTerminal}">${lclVoyageArInvoiceForm.voyageTerminal}</html:option>
                                <html:optionsCollection name="terminalCodeList"/>
                            </html:select>
                            <cong:hidden name="terminalNumber" id="terminalNumber"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Amount</cong:td>
                        <cong:td><cong:text name="amount" id="amount" styleClass="twoDigitDecFormat mandatory" onkeyup="allowNegativeNumbers(this)"/> </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Description</cong:td>
                        <cong:td><cong:textarea name="chargeDescription" id="chargeDescription" onkeypress="return checkTextAreaLimit(this, 40)"  style="width: 72%; height: 40px;text-transform: uppercase"/> </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td></cong:td><cong:td></cong:td>
                        <cong:td>
                            <input type="button" value="Save" class="button-style1" onclick="submitFormForCharge('saveCharge')"/>
                            <input type="button" class="button-style1" value="Cancel" onclick="hideAddItem()"/>
                        </cong:td>
                        <cong:td></cong:td><cong:td></cong:td>
                    </cong:tr>
                </cong:table>
            </cong:div>
            <cong:hidden id="methodName" name="methodName"/>
        </cong:form>
    </body>
</html>
