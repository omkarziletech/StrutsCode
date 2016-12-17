<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<html> 
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <head>
            <base href="${basePath}">
        <title>JSP for APPaymentForm form</title>
        <style>
            #payListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                left: 20%;
                top: 20%;
                border-style:solid solid solid solid;
                background-color: white;
            }
            #batchDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                left: 30%;
                top: 20%;
                border-style:solid solid solid solid;
                background-color: white;
            }
        </style>
        <c:set var="accessMode" value="1"/>
        <c:set var="canEdit" value="true"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="canEdit" value="false"/>
        </c:if>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/GeneralLedgerDwr.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/APPaymentBC.js"></script>
	<script type="text/javascript" src="${path}/dwr/interface/ArDwr.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    </head>
    <body  class="whitebackgrnd">
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </form>
        </div>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form  action="/aPPayment?accessMode=${accessMode}" scope="request">
        <span id="printerMessage" class="error" style="font-size: medium">
            <c:if test="${!empty checkPrinterMessage}">
                <c:out value="${checkPrinterMessage}"/>
            </c:if>
            <c:if test="${!empty overflowPrinterMessage}">
                <br><br><c:out value="${overflowPrinterMessage}"/>
            </c:if>
        </span>
        <span id="errorMessage" class="error" style="font-size: medium">
            <c:if test="${!empty errorMessage}">
                <c:out value="${errorMessage}"/>
            </c:if>
        </span>
        <table cellpadding="3" cellspacing="0" width="100%">
            <tr class="textlabelsBold">
            <td colspan="5">
                Vendor Name
                <input  name="vendor" id="vendor" value="${paymentForm.vendor}"  class="textlabelsBoldForTextBox" style="text-transform: uppercase;"/>
                <input type="button" id="searchButton" class="buttonStyleNew" value='Go' style='width: 30px' onclick="showAll()"/>
                <input type="button" id="clearButton" class="buttonStyleNew" value='Clear' style='width: 40px' onclick="clearAll()"/>
                <c:if test="${not empty listOfcustomers && canEdit}">
                    <input type="button" id="makePaymentButton" class="buttonStyleNew" value='Make Payment' style='width: 100px' onclick="makePayments()" />
                </c:if>
                <input name="vendorNumber" id="vendorNumber"  type="hidden" value="${paymentForm.vendorNumber}" style="text-transform: uppercase;"/>
                <input name="custname_check" id="custname_check"  type="hidden"/>
                <div id="custname_choices" style="display: none" class="autocomplete"></div>
            </td>
        </tr>
        <c:if test="${not empty bankNames}">
            <tr class="textlabelsBold">
            <td>Bank Name</td>
            <td>
                <select name="bankName" id="bankName"
                        style="width:125px" class="dropdown_accounting" onchange="getBankAccounts(this)">
                    <c:if test="${fn:length(bankNames)>1}">
                        <option value="">Select Bank</option>
                    </c:if>
                    <c:forEach var="bankName" items="${bankNames}">
                        <option value="${bankName}">${bankName}</option>
                    </c:forEach>
                </select>
            </td>
            <td>Bank Account</td>
            <td>
                <select name="bankAccountNumber" id="bankAccountNumber" 
                        style="width:120px" class="dropdown_accounting" onchange="setStartingNumber();">
                    <c:choose>
                        <c:when test="${fn:length(bankAccounts)>1 || empty bankAccounts}">
                            <option value="">Select Bank Account</option>
                            <c:forEach var="bankAccount" items="${bankAccounts}">
                                <option value="${bankAccount.bankAcctNo}">${bankAccount.acctName}</option>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <option value="${bankAccounts[0].bankAcctNo}">
                                <c:choose>
                                    <c:when test="${not empty bankAccounts[0].acctName}">${bankAccounts[0].acctName}</c:when>
                                    <c:otherwise>${bankAccounts[0].bankAcctNo}</c:otherwise>
                                </c:choose>
                            </option>
                            <c:set var="startingNumber" value="${bankAccounts[0].startingSerialNo}"/>
                        </c:otherwise>
                    </c:choose>
                </select>
                <input type="hidden" name="startingNumber" id="startingNumber" value="${startingNumber}"/>
            </td>
            <td>Starting Number</td>
            <td>
                <h3 id="startingNumberLabel">${startingNumber}</h3>
            </td>
        </tr>
    </c:if>
</table>
<br>
<c:if test="${!empty listOfcustomers &&  fn:length(listOfcustomers)>0}">
    <table cellpadding="0" cellspacing="0" class="tableBorderNew" width="100%" >
        <tr class="tableHeadingNew">
        <td >List of Vendor
        </td>
        <td style="padding-left: 400px;font: 200">
            <c:if test="${canEdit}">
                Payment Date
                <input type="text" name="txtCommonPaymentDate" id="txtCommonPaymentDate"
                       value="" size="7" readonly="readonly" onchange="changeAllDateFields(this)" class="textlabelsBoldForTextBox"/>
                <img src="${path}/img/CalendarIco.gif" alt="cal" style="height: 13px"
                     align="bottom" id="CommonPaymentDate" onmousedown="insertDateFromCalendar(this.id,0);" />
                <html:checkbox property="checkAll" value="pay" onclick="selectAllCheckBoxes()">Pay All</html:checkbox>
                <c:if test="${loginuser.achApprover}">
                    <html:checkbox property="approveAll" value="approve" onclick="approvedAllPay()">Approve All</html:checkbox>
                </c:if>
            </c:if>
        </td>
    </tr>
    <tr>
    <td colspan="3">
        <div id="divtablesty1" class="scrolldisplaytable" style="height:420px;">
            <c:set var="i" value="0"></c:set>
            <display:table name="${listOfcustomers}" class="displaytagstyleNew" pagesize="${pageSize}"  style="width:100%" id="aPPayment" sort="list" >
                <display:setProperty name="paging.banner.some_items_found">
                    <span class="pagebanner"><font color="blue">{0}</font> Payables displayed,For more Records click on page numbers.</span>
                </display:setProperty>
                <display:setProperty name="paging.banner.one_item_found">
                    <span class="pagebanner">One {0} displayed. Page Number</span>
                </display:setProperty>
                <display:setProperty name="paging.banner.all_items_found">
                    <span class="pagebanner">{0} {1} Displayed, Page Number</span>
                </display:setProperty>
                <display:setProperty name="basic.msg.empty_list">
                    <span class="pagebanner">No Records Found.</span>
                </display:setProperty>
                <display:setProperty name="paging.banner.placement" value="bottom" />
                <display:setProperty name="paging.banner.item_name" value="Payable"/>
                <display:setProperty name="paging.banner.items_name" value="Payables"/>
                <c:choose>
                    <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL && !loginuser.achApprover}">
                        <display:column title="<br/>Vendor" sortable="true" property="customer" headerClass="sortable" 
                                        style="background-color: gray;text-transform:uppercase;"/>
                        <display:column title="Vendor<br/>Number" sortable="true" property="customerNo" headerClass="sortable" 
                                        style="background-color: gray;text-transform:uppercase;"/>
                        <display:column title="Credit<br/>Hold" sortable="true" property="hold" headerClass="sortable" 
                                        style="background-color: gray;text-transform:uppercase;"/>
                        <display:column title="Payment<br/>Amount" sortable="true" headerClass="sortable" style="background-color: gray;">
                            <c:choose>
                                <c:when test="${fn:contains(aPPayment.balance, '-')}">
                                    <span style="color: red">
                                        <c:out  value="(${fn:substring(aPPayment.balance,1,fn:length(aPPayment.balance))})"/>
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${aPPayment.balance}"/>
                                </c:otherwise>
                            </c:choose>
                        </display:column>
                        <display:column title="Payment<br/>Method" style="background-color: gray;">
                            <select name="paymethod" id="paymethod" style="width: 95px"  class="textlabelsBoldForTextBox">
                                <c:forEach var="labelValueBean" items="${aPPayment.paymentMethods}">
                                    <option value="${labelValueBean.value}">${labelValueBean.label}</option>
                                </c:forEach>
                            </select>
                            <input type="hidden" name="paymentMethodArray" id="paymentMethodArray${i}"/>
                        </display:column>
                        <display:column title="Payment<br/>Date" style="background-color: gray;">
                            <input type="text" name="paymentDate" id="txtPaymentDate${i}" style="text-align: center;background-color: gray;"
                                   value="${aPPayment.transDate}" size="10" readonly="readonly"/>
                        </display:column>
                    </c:when>
                    <c:otherwise>
                        <display:column title="<br/>Vendor" sortable="true" property="customer" headerClass="sortable"
					style="background-color: inherit;text-transform:uppercase;"/>
                        <display:column title="Vendor<br/>Number" sortable="true" property="customerNo" headerClass="sortable" 
                                        style="background-color: inherit;text-transform:uppercase;"/>
                        <display:column title="Credit<br/>Hold" sortable="true" property="hold" headerClass="sortable"
					style="background-color: inherit;text-transform:uppercase;"/>
                        <display:column title="Payment<br/>Amount" sortable="true" headerClass="sortable"
					style="background-color: inherit;">
                            <c:choose>
                                <c:when test="${fn:contains(aPPayment.balance, '-')}">
                                    <span style="color: red">
                                        <c:out  value="(${fn:substring(aPPayment.balance,1,fn:length(aPPayment.balance))})"/>
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${aPPayment.balance}"/>
                                </c:otherwise>
                            </c:choose>
                        </display:column>
                        <display:column title="Payment<br/>Method" style="background-color: inherit;">
                            <select name="paymethod" id="paymethod" style="width: 95px"  class="textlabelsBoldForTextBox">
                                <c:forEach var="labelValueBean" items="${aPPayment.paymentMethods}">
                                    <option value="${labelValueBean.value}">${labelValueBean.label}</option>
                                </c:forEach>
                            </select>
                            <input type="hidden" name="paymentMethodArray" id="paymentMethodArray${i}"/>
                        </display:column>
                        <display:column title="Payment<br/>Date" style="background-color: inherit;">
                            <input type="text" name="paymentDate" id="txtPaymentDate${i}"
                                   style="text-align: center" value="${aPPayment.transDate}" size="10" readonly="readonly"  class="textlabelsBoldForTextBox"/>
                        </display:column>
                    </c:otherwise>
                </c:choose>
                <c:if test="${canEdit}">
                    <c:choose>
                        <c:when test="${!loginuser.achApprover}">
                            <c:choose>
                                <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL}">
                                    <display:column title="Check<br/>Pay" style="background-color: gray;">
                                        <input type="checkbox" name="payCheckBox" checked disabled  value="${aPPayment.transactionId}"/>
                                    </display:column>
                                </c:when>
                                <c:otherwise>
                                    <display:column title="Check<br/>Pay" style="background-color: inherit;">
                                        <input type="checkbox" name="payCheckBox" value="${aPPayment.transactionId}"/>
                                    </display:column>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <display:column title="Check<br/>Pay" style="background-color: inherit;">
                                <c:choose>
                                    <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL}">
                                        <input type="checkbox" name="payCheckBox" checked value="${aPPayment.transactionId}" onclick="payOrUnPay(this)"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" name="payCheckBox" value="${aPPayment.transactionId}" onclick="payOrUnPay(this)"/>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${!loginuser.achApprover}">
                            <c:choose>
                                <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL}">
                                    <display:column title="Approve<br/>Pay" style="background-color: gray;">
                                        <input type="checkbox" name="approveCheckBox" disabled/>
                                    </display:column>
                                </c:when>
                                <c:otherwise>
                                    <display:column title="Approve<br/>Pay" style="background-color: inherit;">
                                    </display:column>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <display:column title="Approve<br/>Pay" style="background-color: inherit;">
                                <c:choose>
                                    <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL}">
                                        <div id="approveCheckBoxdiv${i}">
                                            <input type="checkbox" name="approveCheckBox" value="${aPPayment.transactionId}" onclick="approveOrUnApprove(this)"/>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div id="approveCheckBoxdiv${i}" style="display: none">
                                            <input type="checkbox" name="approveCheckBox" value="${aPPayment.transactionId}" onclick="approveOrUnApprove(this)"/>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:choose>
                    <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL && !loginuser.achApprover}">
                        <display:column title="<br/>Action" style="background-color: gray;">
                            <span class="hotspot" onmouseover="tooltip.show('<strong>ShowAll</strong>',null,event);"
                                  onmouseout="tooltip.hide();">
                                <img src="${path}/img/icons/showall.gif" border="0" onclick="showDetails('${aPPayment.transactionId}')" />
                            </span>
                            <input type="hidden" name="customersName" id="customersName" value="${aPPayment.customer}"/>
                        </display:column>
                    </c:when>
                    <c:otherwise>
                        <display:column title="<br/>Action" style="background-color: inherit;">
                            <span class="hotspot" onmouseover="tooltip.show('<strong>ShowAll</strong>',null,event);" onmouseout="tooltip.hide();" >
                                <img src="${path}/img/icons/showall.gif" border="0" onclick="showDetails('${aPPayment.transactionId}')" />
                            </span>
                            <input type="hidden" name="customersName" id="customersName" value="${aPPayment.customer}"/>
                        </display:column>
                    </c:otherwise>
                </c:choose>
                <c:set var="i" value="${i+1}" />
            </display:table>
        </div>
    </td>
</tr>
</table>
</c:if>
<html:hidden property="idsForPayment"/>
<html:hidden property="idsForApproval"/>
<html:hidden property="idsForUnCheckPay"/>
<html:hidden property="paymentMethods"/>
<html:hidden property="button"/>
<html:hidden property="totalAmount"/>
<html:hidden property="invoiceNumber"/>
<html:hidden property="batchId"/>
<html:hidden property="batchDescription"/>
<input type="hidden" id="messageLoginError" value="${commonConstants.MESSAGE_LOGIN_ERROR}">
<input type="hidden" id="messageSuccess" value="${commonConstants.MESSAGE_SUCCESS}">
<input type="hidden" id="canEdit" value="${canEdit}">
</html:form>
</body>

<script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApPayment.js"></script>
<%@include file="../includes/baseResourcesForJS.jsp" %>
<script type="text/javascript">
    jQuery.noConflict();
    jQuery(document).ready(function(){
        jQuery("#aPPayment").tablesorter({widgets: ['zebra'],textExtraction: ['complex']});
    });
</script>
</html>

