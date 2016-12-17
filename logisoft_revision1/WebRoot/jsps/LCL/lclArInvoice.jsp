<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dcong:td">
<html xmlns="http://www.w3.org/1999/xhtml">
    <%@include file="init.jsp"%>
    <%@include file="/jsps/preloader.jsp" %>
    <%@include file="../includes/jspVariables.jsp"%>
    <%@include file="../includes/baseResources.jsp"%>
    <%@include file="/taglib.jsp"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
    <body>
        <style>
            .show{
                display: block;
            }
            .hide{
                display: none;
            }
        </style>
        <cong:form name="lclArInvoiceForm" id="lclArInvoiceForm" action="lclArInvoice.do">
            <jsp:useBean id="glMappingDAO" scope="request" class="com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO"/>
            <jsp:useBean id="dbUtil" scope="request" class="com.gp.cong.logisoft.util.DBUtil"/>
            <c:set var="chargeCodeList" value="${glMappingDAO.lclChargeCodeArInvoice}" scope="request"/>
            <c:set var="terminalCodeList" value="${dbUtil.terminalCodeList}" scope="request"/>
            <cong:hidden id="fileNumber" name="fileNumber" value="${lclArInvoiceForm.fileNumber}"/>
            <cong:hidden id="fileNumberId" name="fileNumberId" value="${lclArInvoiceForm.fileNumberId}"/>
            <cong:hidden id="moduleName" name="moduleName" value="${lclArInvoiceForm.moduleName}"/>
            <cong:hidden id="arRedInvoiceId" name="arRedInvoiceId" value="${arRedInvoice.id}"/>
            <cong:hidden id="status" name="status" value="${arRedInvoice.status}"/>
            <cong:hidden id="voyTerminalNo" name="voyTerminalNo" value="${lclArInvoiceForm.voyTerminalNo}"/>
            <cong:hidden id="headerId" name="headerId" value="${lclArInvoiceForm.headerId}"/>
            <cong:hidden id="pageName" name="pageName" value="${terminate}"/>
            <input type="hidden" id="closePopup" value="${closePopup}"/>
            <cong:table styleClass="floatLeft tableHeadingNew" style="width:100%">
                <cong:tr styleClass="tableHeadingNew">
                    <c:choose>
                        <c:when test="${terminate eq 'Terminate'}">
                            <cong:td width="70%">AR Invoice for File No:<span class="fileNo">${lclArInvoiceForm.fileNumber}</span></cong:td>
                        </c:when>
                        <c:otherwise>
                            <cong:td width="80%">AR Invoice for File No:<span class="fileNo">${lclArInvoiceForm.fileNumber}</span></cong:td>
                        </c:otherwise>
                    </c:choose>
                    <cong:td>
                        <c:choose><c:when test="${empty auditedBy && empty closedBy}">
                                <cong:div styleClass="button-style1" style="float:left"
                                          onclick="searchAR('${path}','${lclArInvoiceForm.fileNumber}','${lclArInvoiceForm.fileNumberId}','${lclArInvoiceForm.moduleName}','${lclArInvoiceForm.headerId}')">Search</cong:div>
                                <cong:div styleClass="button-style1" style="float:left" onclick="showLineItem()">Add New</cong:div>
                            </c:when><c:otherwise>
                                <cong:div styleClass="gray-background" style="float:left">Search</cong:div>
                                <cong:div styleClass="gray-background" style="float:left">Add New</cong:div>
                            </c:otherwise></c:choose>
                        <cong:div styleClass="button-style1" style="float:left" onclick="closeInvoice()">Close</cong:div>
                        <c:if test="${terminate eq 'Terminate'}">
                            <cong:div styleClass="button-style1" style="float:left" onclick="skipInvoice()">Skip - No Invoice</cong:div>
                        </c:if>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <c:choose>
                <c:when test="${newItemFlag==true}">
                    <c:set var="showHide" value="show"/>
                </c:when>
                <c:otherwise>
                    <c:set var="showHide" value="hide"/>
                </c:otherwise>
            </c:choose>
            <cong:div id="success">
                <span id="blueBold" style="font-size: 14px">
                    ${displayMessage}
                    <c:if test="${arRedInvoice.status eq 'AR'}">
                        This invoice <c:out value="${arRedInvoice.invoiceNumber}"/> is posted to AR SuccessFully</c:if>
                    </span>
            </cong:div>
            <cong:div id="addNew" style="width:100%; float:left" styleClass="${showHide}">
                <cong:table width="80%"  border="0" cellpadding="0" cellspacing="0">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Customer Name</cong:td>
                        <cong:td id="cust">
                            <cong:autocompletor name="customerName" template="tradingPartner" id="customerName"
                                                fields="customerNumber,customerType,NULL,NULL,NULL,NULL,NULL,clientDisabled,address,NULL,NULL,NULL,NULL,contactName,phoneNumber,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,clientDisableAcct" scrollHeight="300px"
                                                query="CLIENT_NO_CONSIGNEE" value="${arRedInvoice.customerName}"  width="600" container="NULL" shouldMatch="true" styleClass="mandatory text" callback="verifyAcctType()"/>
                            <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openArContact()"/>
                        </cong:td>
                        <input type="hidden" name="clientDisabled" id="clientDisabled"/>
                        <input type="hidden" name="clientDisableAcct" id="clientDisableAcct"/>
                        <cong:td styleClass="textlabelsBoldforlcl">Customer Number</cong:td>
                        <cong:td><cong:text name="customerNumber" id="customerNumber" styleClass="text" value="${arRedInvoice.customerNumber}"/></cong:td>
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
                            <cong:calendar  name="date" id="date" styleClass="textlabelsBoldForTextBox" value="${date1}"/> </cong:td>
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
                            <c:if test="${arRedInvoice.status!='AR'}">
                                <input type="button" value="Save" id="save" class="button-style1" onclick="submitForm('save')"/>
                                <input type="button" value="Delete" class="button-style1" onclick="deleteArInvoice('${path}', '${lclArInvoiceForm.fileNumberId}', '${lclArInvoiceForm.fileNumber}', $('#arRedInvoiceId').val(), false, true)"/>
                                <c:if test="${not empty invoiceChargeList && not empty arRedInvoice.id}">
                                    <input type="button" value="Post" id="post" class="button-style1 post" onclick="postInvoice('${path}', '${lclArInvoiceForm.fileNumberId}', '${lclArInvoiceForm.fileNumber}', '${arRedInvoice.id}', '${lclArInvoiceForm.moduleName}')"/>
                                </c:if>
                                <input type="button" class="button-style1" id="cancel" value="Cancel" onclick="backToInvoiceList('${path}', '${lclArInvoiceForm.fileNumberId}', '${lclArInvoiceForm.fileNumber}', true)"/>
                            </c:if>
                            <cong:div id="addN" style="display:none">
                                <input type="button" value="Save" id="save" class="button-style1" onclick="submitForm('save')"/>
                                <input type="button" value="Delete" class="button-style1" onclick="deleteArInvoice('${path}', '${lclArInvoiceForm.fileNumberId}', '${lclArInvoiceForm.fileNumber}', $('#arRedInvoiceId').val(), false, true)"/>
                                <input type="button" class="button-style1" id="cancel" value="Cancel" onclick="backToInvoiceList('${path}', '${lclArInvoiceForm.fileNumberId}', '${lclArInvoiceForm.fileNumber}', true)"/>
                            </cong:div>
                        </cong:td>
                        <cong:td></cong:td><cong:td></cong:td>
                    </cong:tr>
                </cong:table>
            </cong:div>
            <c:if test="${not empty invoiceList}">
                <div id="arInvoiceList" style="height: 250px;" class="result-container">
                    <table width="100%" border="0" class="display-table">
                        <thead>
                            <tr>
                                <th>Customer Name</th>
                                <th>Account#</th>
                                <th>Customer Type</th>
                                <th>Invoice#</th>
                                <th>Invoice Amount</th>
                                <th>Invoice Date</th>
                                <th>Status</th>
                                <th>Posted Date</th>
                                <th>User Name</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
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
                                    <td>${invoice.customerName}</td>
                                    <td>${invoice.customerNumber}</td>
                                    <td>${invoice.customerType}</td>
                                    <td>${invoice.invoiceNumber}</td>
                                    <td>${invoice.invoiceAmount}</td>
                                    <td>${invoice.dateAsString}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${invoice.status eq 'AR'}">
                                                <c:out value="Posted"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="In Progress"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <fmt:formatDate pattern="dd-MMM-yyyy" var="invoicePostedDate" value="${invoice.postedDate}"/>
                                    <td>${invoicePostedDate}</td>
                                    <td>
                                        <span title="${fn:toUpperCase(invoice.invoiceBy)}">${fn:toUpperCase(fn:substring(invoice.invoiceBy,0,15))}</span></td>
                                    <td>
                                        <img src="${path}/jsps/LCL/images/search_over.gif" alt="preview" style="cursor: pointer" onclick="previewArInvoice('', '${invoice.id}')"/>
                                        <img src="${path}/img/icons/send.gif" style="cursor: pointer" onclick="viewReports('${path}', '${invoice.invoiceNumber}', '${invoice.id}')" title="Print/Fax/Email"/>
                                        <c:if test="${empty auditedBy && empty closedBy}">
                                            <img src="${path}/images/edit.png" title="edit" alt="edit" style="cursor: pointer" onclick="editArInvoice('${path}', '${lclArInvoiceForm.fileNumberId}', '${lclArInvoiceForm.fileNumber}', '${invoice.id}', '${lclArInvoiceForm.moduleName}', true, '${lclArInvoiceForm.headerId}')"/>
                                            <c:if test="${invoice.status=='AR' && roleDuty.reversePostedInvoices}">
                                                <img src="${path}/img/icons/unpost1.png" alt="unpost" title="Reverse Post" style="cursor: pointer" onclick="unPostInvoice('${path}', '${lclArInvoiceForm.fileNumberId}', '${lclArInvoiceForm.fileNumber}', '${invoice.id}', true, '${lclArInvoiceForm.moduleName}')"/>
                                            </c:if>
                                            <c:if test="${invoice.status ne 'AR'}">
                                                <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                                     onclick="deleteArInvoice('${path}', '${invoice.id}', '${invoice.fileNo}', '${invoice.blNumber}', '${lclArInvoiceForm.moduleName}');" title="Delete Invoice"/>
                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>
                            </tbody>
                        </c:forEach>
                    </table>
                </div>
            </c:if>
            <c:if test="${not empty arRedInvoice.id}">
                <cong:div id="lineItemList" style="width:100%">
                    <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
                        <span style="margin-right: 35%"> List of Line Items</span>
                        <c:if test="${arRedInvoice.status ne 'AR'}">
                            <cong:div styleClass="floatRight button-style1" id="addList" onclick="showAddItem()">Add</cong:div>
                        </c:if>
                    </cong:div>
                    <cong:div id="redInvoiceCharge">
                        <c:import url="/jsps/LCL/ajaxload/lclArInvoiceCharge.jsp">
                            <c:param name="status" value="${arRedInvoice.status}"/>
                        </c:import>
                    </cong:div>
                </cong:div>
            </c:if>
            <cong:div id="addLineItem" style="width:100%;display:none">
                <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">Add Line Items </cong:div>
                <cong:table width="100%"  border="0" cellpadding="0" cellspacing="0">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">File Number</cong:td>
                        <cong:td><cong:text name="fileNumber" id="fileNumber" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true"/> </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Shipment Type</cong:td>
                        <c:choose>
                            <c:when test="${lclArInvoiceForm.moduleName eq 'Imports'}">
                                <c:set var="shipmentType" value="LCLI"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="shipmentType" value="LCLE"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:td><cong:text name="shipmentType" id="shipmentType" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${shipmentType}"/> </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Charge Code</cong:td>
                        <cong:td>
                            <cong:autocompletor name="chargeCode" id="chargeCode" template="two" query="CHARGE_CODE" fields="NULL,chargeId" shouldMatch="true" scrollHeight="150"
                                                params="${shipmentType}" position="left" value="" container="NULL" styleClass="text mandatory require" width="400" />
                            <cong:hidden id="chargeId" name="chargeId" value=""/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Terminal Code</cong:td>
                        <cong:td>
                            <c:choose>
                                <c:when test="${lclArInvoiceForm.voyTerminalNo ne ''}">
                                    <html:select property="terminal" style="width:150px;" styleClass="smallDropDown textlabelsBoldforlcl mandatory" styleId="terminal">
                                        <html:option value="${lclArInvoiceForm.voyTerminalNo}">${lclArInvoiceForm.voyTerminalNo}</html:option>
                                        <html:optionsCollection name="terminalCodeList"/>
                                    </html:select>
                                </c:when>
                                <c:otherwise>
                                    <html:select property="terminal" value="" style="width:150px;" styleClass="smallDropDown textlabelsBoldforlcl mandatory" styleId="terminal">
                                        <html:optionsCollection name="terminalCodeList"/>
                                    </html:select>
                                </c:otherwise>
                            </c:choose>
                            <cong:hidden name="terminalNumber" id="terminalNumber"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Amount</cong:td>
                        <cong:td><cong:text name="amount" id="amount" styleClass="twoDigitDecFormat mandatory" onkeyup="allowNegativeNumbers(this)"/> </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Description</cong:td>
                        <cong:td><cong:textarea name="chargeDescription" id="chargeDescription" style="width: 72%; height: 40px;text-transform: uppercase"/> </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td></cong:td><cong:td></cong:td>
                        <cong:td>
                            <input type="button" value="Save" class="button-style1" onclick="submitFormForCharge('saveCharge')"/>
                            <input type="button" class="button-style1" value="Cancel" onclick="hideAddItem()"  onkeydown="tabFocus(event);"/>
                        </cong:td>
                        <cong:td></cong:td><cong:td></cong:td>
                    </cong:tr>
                </cong:table>
            </cong:div>
            <div id="add-invoiceComments-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
                <table class="table" style="margin: 2px;width: 598px;">
                    <tr>
                        <th>
                            <div class="float-left">
                                <label id="headingComments"></label>
                            </div>
                        </th>
                    </tr>
                    <tr>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="label">
                            <textarea id="invoiceComments" name="invoiceComments" cols="85" rows="5" class="textBoldforlcl"
                                      style="resize:none;text-transform: uppercase"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <input type="button"  value="Save" id="saveInvoice"
                                   align="center" class="button" onclick="saveInvoiceComments();"/>
                            <input type="button"  value="Cancel" id="cancelInvoice"
                                   align="center" class="button" onclick="cancelInvoiceComments();"/>
                        </td>
                    </tr>
                </table>
            </div>
            <cong:hidden id="comments" name="comments"  value="${lclArInvoiceForm.comments}"/>
            <cong:hidden id="hiddenInvoiceComments" name="hiddenInvoiceComments"/>
            <cong:hidden id="consoTerminate" name="consoTerminate" value="${lclArInvoiceForm.consoTerminate}"/>
            <cong:hidden id="methodName" name="methodName"/>
        </cong:form>
        <script type="text/javascript">
            $(document).ready(function () {
                $("#customerName").focus();
                if ($('#closePopup').val() === 'close' && $('#pageName').val() === 'Terminate')
                {
                    window.parent.showLoading();
                    parent.jQuery("#fileStatus").val('T');
                    parent.jQuery("#statuslabel").text("Terminated");
                    parent.$("#lclDomTermination").hide();
                    parent.$("#lclDomTermination1").hide();
                    parent.$("#lclUnTermination").show();
                    parent.$("#lclUnTermination1").show();
                    parent.$("#methodName").val('saveBooking');
                    parent.$("#lclBookingForm").submit();
                    parent.$.colorbox.close();
                }
            });
            function allowNegativeNumbers(obj) {
                if (!/^-?\d*(\.\d{0,6})?$/.test(obj.value)) {
                    obj.value = "";
                    $.prompt("This field should be Numeric");
                }
            }
            function backToInvoiceList(path, fileId, fileNumber, listFlag) {
                var url = path + "/lclArInvoice.do?methodName=display&fileNumberId=" + fileId + "&fileNumber=" + fileNumber + "&listFlag=" + listFlag;
                window.location = url;
            }
            function unPostInvoice(path, fileId, fileNumber, invoiceId, listFlag, moduleName) {
                var url = path + "/lclArInvoice.do?methodName=unPost&fileNumberId=" + fileId + "&fileNumber=" + fileNumber + "&invoiceId=" + invoiceId + "&listFlag=" + listFlag + "&moduleName=" + moduleName;
                window.location = url;
            }
            function searchAR(path, fileNumber, fileId, moduleName, headerId) {
                var url = path + "/lclArInvoice.do?methodName=searchAR&fileNumber=" + fileNumber + "&fileNumberId=" + fileId + "&moduleName=" + moduleName + "&headerId=" + headerId;
                ;
                window.location = url;
            }

            function editArInvoice(path, fileId, fileNumber, invoiceId, moduleName, newItemFlag, headerId) {
                var url = path + "/lclArInvoice.do?methodName=edit&fileNumberId=" + fileId + "&fileNumber=" + fileNumber + "&invoiceId=" + invoiceId + "&moduleName=" + moduleName + "&newItemFlag=" + newItemFlag + "&headerId=" + headerId;
                window.location = url;
                ;
                window.location = url;
            }
            function previewArInvoice(buttonValue, invoiceId) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclPrintUtil",
                        methodName: "lclArInvoiceReport",
                        param1: "",
                        param2: buttonValue,
                        param3: invoiceId,
                        param4: "",
                        param5: "No",
                        request: "true"
                    },
                    preloading: true,
                    success: function (data) {
                        viewFile(data);
                    }
                });
            }
            function viewFile(file) {
                var win = window.open('${path}/servlet/PdfServlet?fileName=' + file, '_new', 'width=1000,height=650,toolbar=no,directories=no,status=no,linemenubar=no,scrollbars=no,resizable=no,modal=yes');
                window.onblur = function () {
                    win.focus();
                }
            }
            function deleteArInvoice(path, invoiceId, fileId, fileNo, moduleName) {
                $.prompt('Are you sure you want to delete?', {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v == 1) {
                            showProgressBar();
                            var url = path + "/lclArInvoice.do?methodName=deleteInvoice&invoiceId=" + invoiceId + "&fileNumber=" + fileNo + "&fileNumberId=" + fileId + "&moduleName=" + moduleName;
                            window.location = url;
                            hideProgressBar();
                            $.prompt.close();
                        }
                        else if (v == 2) {
                            $.prompt.close();
                        }
                    }
                });
            }
            function showLineItem() {
                var m_names = new Array("JAN", "FEB", "MAR",
                        "APR", "MAY", "JUN", "JUL", "AUG", "SEP",
                        "OCT", "NOV", "DEC");
                var d = new Date();
                var curr_date = d.getDate();
                var curr_month = d.getMonth();
                var curr_year = d.getFullYear();
                $("#date").val(curr_date + "-" + m_names[curr_month]
                        + "-" + curr_year);
                var status = $('#status').val();
                $("#addNew").show();
                clearValues();
                $("#lineItemList").hide();
                $("#arInvoiceList").hide();
                $("#success").hide();
                if (status == 'AR') {
                    $("#addN").show();
                }
            }
            function submitForm(methodName) {
                var custName = $('#customerName').val();
                var date = $('#date').val();
                if (custName == "" || custName == null) {
                    congAlert("CustomerName is required");
                    $("#customerName").css("border-color", "red");
                } else if ("" == date || date != "") {
                    if (date == "") {
                        congAlert("Please Enter Date");
                        $("#date").css("border-color", "red");
                        $('#date').val('');
                    } else {
                        if (null != date && date != undefined) {
                            $.ajaxx({
                                dataType: "json",
                                data: {
                                    className: "com.gp.cong.lcl.dwr.LclDwr",
                                    methodName: "validDate",
                                    param1: date,
                                    dataType: "json"
                                },
                                success: function (data) {
                                    if (!data) {
                                        congAlert("Please Enter valid Date");
                                        $('#date').val('');
                                    } else {
                                        calculateDueDate();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    saveArinvoice(methodName);
                }
                //                if (terminate == 'Terminate') {
                //                    parent.document.getElementById('statuslabel').innerHTML = 'Terminated';
                //                    parent.document.getElementById('fileStatus').value = 'X';
                //                    showTerminate();
                //                }

                parent.$("#arInvoice").addClass('green-background');
                parent.$("#arinvoice").addClass('green-background');
                parent.$("#invoiceR").addClass('green-background');
            }

            function calculateDueDate() {
                var day, month, year;
                var date = $('#date').val().split("-");
                day = date[0];
                month = getMonthNumber(date[1]) - 1;
                year = date[2];
                var invoiceDate = new Date(year, month, day);
                if (invoiceDate > new Date()) {
                    $('#date').val("");
                    $.prompt("Invoice Date should not be greater than Today's Date");
                    $("#date").css("border-color", "red");
                    return;
                } else {
                    saveArinvoice('save');
                }
            }

            function saveArinvoice(methodName) {
                $("#methodName").val(methodName);
                $("#lclArInvoiceForm").submit();
                var terminate = $('#pageName').val();
                if (terminate === 'Terminate') {
                    parent.jQuery("#fileStatus").val('T');
                    parent.jQuery("#statuslabel").text("Terminated");
                    parent.$("#lclDomTermination").hide();
                    parent.$("#lclDomTermination1").hide();
                    parent.$("#lclUnTermination").show();
                    parent.$("#lclUnTermination1").show();
                }
                parent.$("#arInvoice").addClass('green-background');
                parent.$("#arinvoice").addClass('green-background');
                parent.$("#invoiceR").addClass('green-background');
            }

            function skipInvoice() {
                showAlternateMask();
                $("#add-invoiceComments-container").center().show(500, function () {
                    $('#headingComments').text('Please enter comments regarding why an AR Invoice is not needed');
                    $('#invoiceComments').val('');
                    //                    $('#hiddenDeleteHotCodeFlag').val('');
                    //                    $('#3pRefId').val('');
                });
            }

            function cancelInvoiceComments() {
                $("#add-invoiceComments-container").center().hide(500, function () {
                    // $('#hotCodes').val('');
                    hideAlternateMask();
                });
            }

            function saveInvoiceComments() {
                var consoTerminate = $('#consoTerminate').val();
                consoTerminate = (consoTerminate !== undefined) ? consoTerminate : "";
                $("#add-invoiceComments-container").center().hide(500, function () {
                    var invoiceComment = $('#invoiceComments').val();
                    if (invoiceComment === null || invoiceComment === "" || invoiceComment === undefined) {
                        $.prompt("Comment  is required");
                        skipInvoice();
                        return false;
                    } else {
                        var fileId = $('#fileNumberId').val();
                        var comment = $('#comments').val();
                        var userId = parent.$("#loginUserId").val();

                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "lclSaveInvoiceComments",
                                param1: fileId,
                                param2: "X",
                                param3: comment,
                                param4: invoiceComment,
                                param5: userId
                            },
                            async: false,
                            success: function (data) {
                                if (data === "true") {
                                    jQuery.ajaxx({
                                        data: {
                                            className: "com.gp.cong.lcl.dwr.LclDwr",
                                            methodName: "lclDomTermination",
                                            param1: fileId,
                                            param2: "X",
                                            param3: "Cancelled By Customer",
                                            param4: comment,
                                            param5: userId,
                                            param6: consoTerminate
                                        },
                                        async: false,
                                        success: function (data) {
                                            if (data === "true") {
                                                window.parent.showLoading();
                                                parent.$("#methodName").val('saveBooking');
                                                parent.$("#lclBookingForm").submit();
                                                parent.$.colorbox.close();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                })
            }

            function showAddItem() {
                $("#addLineItem").show();
                $("#chargeCode").focus();
                if ($("#voyTerminalNo").val() !== "") {
                    $("#terminal").val($("#voyTerminalNo").val());
                } else {
                    $("#terminal").val('Select');
                }
            }
            function hideAddItem() {
                $("#addLineItem").hide();
            }
            function submitAjaxForm(methodName, formName, fileNumber, selector) {
                $("#methodName").val(methodName);
                var params = $(formName).serialize();
                params += "&fileNumber=" + fileNumber;
                $.post($(formName).attr("action"), params,
                        function (data) {
                            $(selector).html(data);
                            $(selector, window.parent.document).html(data);
                            $("#addLineItem").hide();
                            $("#chargeCode").val("");
                            $("#terminal").val("");
                            $("#amount").val("");
                            $("#chargeDescription").val("");
                        });
            }

            function postInvoice(path, fileId, fileNumber, invoiceId, moduleName) {
                var acctNo = $('#customerNumber').val();
                var acctName = $('#customerName').val();
                var postdate = $('#date').val();
                var invoiceNumber = $("#invoiceNumber").val();
                var arRedInvoiceId = $('#arRedInvoiceId').val();
                if (acctNo === '' || acctNo === null || acctNo === undefined) {
                    congAlert("Please select Customer");
                    $("#customerName").css("border-color", "red");
                } else if (postdate === '' || postdate === null || postdate === undefined) {
                    congAlert("Please Enter Date");
                    $("#date").css("border-color", "red");
                } else if (invoiceNumber === '' || invoiceNumber === null || invoiceNumber === undefined) {
                    congAlert("Please Save Invoice");
                } else if (moduleName === 'Imports' && !checkGlAccountMapping(arRedInvoiceId)) {
                    return false;
                } else {
                    $("#methodName").val("checkCodeFCountByAcctNo");
                    var params = $("#lclArInvoiceForm").serialize();
                    params += "&acctNo=" + acctNo + "&acctName=" + acctName;
                    $.post($("#lclArInvoiceForm").attr("action"), params,
                            function (data) {
                                if (data === 'true') {
                                    var href = path + "/lclArInvoice.do?methodName=post&fileNumberId=" + fileId + "&fileNumber=" + fileNumber + "&invoiceId=" + invoiceId + "&moduleName=" + moduleName;
                                    $.colorbox({
                                        iframe: true,
                                        href: href,
                                        width: "90%",
                                        height: "90%",
                                        title: "AR Invoice"
                                    });
                                } else {
                                    $.prompt("<font color='red'>" + data + "</font>");
                                }
                            });
                }
            }
            function congAlert(txt) {
                $.prompt(txt);
            }

            function submitFormForCharge(methodName) {
                var amount = $("#amount").val();
                var chargeId = $("#chargeId").val();
                var terminal = $("#terminal").val();
                if (chargeId === null || chargeId === "" || chargeId === "0") {
                    congAlert('Charge Code is required');
                    $("#chargeCode").css("border-color", "red");
                    return false;
                } else if (terminal === null || terminal === "") {
                    congAlert('Terminal Code is required');
                    $("#terminal").css("border-color", "red");
                    return false;
                } else if (amount === null || amount === "" || amount === "0.0") {
                    congAlert("Amount is required");
                    $("#amount").css("border-color", "red");
                    return false;
                } else if ($("#moduleName").val() === 'Exports' && !validateGlMapping($("#chargeCode").val())) {
                    return false;
                } else {
                    showProgressBar();
                    $("#methodName").val(methodName);
                    $("#lclArInvoiceForm").submit();
                }
            }

            function clearValues() {
                $('#customerName').val('');
                $('#customerNumber').val('');
                $('#dueDate').val('');
                $('#notes').val('');
                $('#invoiceNumber').val('');
                $('#customerType').val('');
                $('#contactName').val('');
                $('#address').val('');
                $('#contactName').val('');
                $('#phoneNumber').val('');
                $('#arRedInvoiceId').val('');
                //$('#date').val('');
                $('#invoiceDate').val('');
                $('#status').val('');
            }

            function verifyAcctType() {
                var target = $('#customerType').val();
                if ($('#clientDisabled').val() === 'Y') {
                    $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#clientDisableAcct').val() + "</span>");
                    clearValues();
                }
                if (target != "") {
                    if (target == 'C') {
                        congAlert("Consignee Accounts are not allowed to be billed");
                        jQuery("#customerName").val('');
                        jQuery("#customerNumber").val('');
                        jQuery("#contactName").val('');
                        jQuery("#phoneNumber").val('');
                        jQuery("#fax").val('');
                        jQuery("#address").val('');
                    }
                }
            }

            function closeInvoice() {
                parent.$.colorbox.close();
            }

            function openArContact() {
                var customer = $('#customerNumber').val();
                var customerName = $('#customerName').val();
                var contactName = $('#contactName').val();
                var subtype = "AR";
                customerName = customerName.replace("&", "amp;");
                var href = "${path}/lclContactDetails.do?methodName=display&accountName=" + customerName + "&accountNo=" + customer + "&contactName=" + contactName
                        + "&subtype=" + subtype + "&vendorName=" + customerName + "&vendorNo=" + customer;
                mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
                mywindow.moveTo(200, 180);
            }
            function viewReports(path, invoiceNo, invoiceId) {
                var url = path + "/printConfig.do?screenName=BL&arInvoice=" + invoiceNo + "&arInvoiceId=" + invoiceId;
                mywindow = window.open(url, '', 'width=800,height=500,scrollbars=yes');
                mywindow.moveTo(200, 180);
            }
            function tabFocus(e) {
                if (e.which === 9) {
                    $("#cust").focus();
                }
            }
            function checkGlAccountMapping(arRedInvoiceId) {
                var flag = true;
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "checkInvoiceChargeAndCostMappingWithGLAccount",
                        param1: arRedInvoiceId,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data !== '') {
                            $.prompt("No gl account is mapped with these charge code" +
                                    ".Please contact accounting - <span style=color:red>" + data + "</span>.");
                            flag = false;
                        }
                    }
                });
                return flag;
            }

            function validateGlMapping(chargeCode) { //this method for both BL and Booking.
                var flag = true;
                var originId = parent.$("#portOfOriginId").val();
                var bookedHeader = parent.$("#eciVoyage").val();
                var pickedHeader = parent.$("#pickedOnVoyageNo").val();
                var voyScheduleNo = pickedHeader === '' ? bookedHeader : pickedHeader;
                var voyOriginId = "";
                if (voyScheduleNo !== '') {
                    voyOriginId = parent.getVoyageOriginId(voyScheduleNo);
                } else {
                    voyOriginId = parent.$("#portOfLoadingId").val();
                }
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO",
                        methodName: "validateLclExportGlAccount",
                        param1: chargeCode,
                        param2: originId,
                        param3: $("#terminal").val(),
                        param4: voyOriginId,
                        param5: "AR",
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data !== '') {
                            flag = false;
                            $.prompt(data);
                        }
                    }
                });
                return flag;
            }
        </script>
    </body>
</html>
