<%-- 
    Document   : addAccrualsTemplate
    Created on : Jun 14, 2011, 9:02:34 PM
    Author     : lakshh
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
<td>
    ${transaction.customerNumber}
    <input type="hidden" class="customerNumber"
           id="customerNumber${transaction.transactionId}" value="${transaction.customerNumber}"/>
</td>
<td>
    <input type="hidden" class="customerName" value="${transaction.customerName}"/>
    <c:choose>
        <c:when test="${fn:length(transaction.customerName)>20}">
            <label title="${transaction.customerName}">
                ${fn:substring(transaction.customerName,0,20)}..
            </label>
        </c:when>
        <c:otherwise>${transaction.customerName}</c:otherwise>
    </c:choose>
</td>
<td>
    <c:choose>
        <c:when test="${fn:length(transaction.customerReference)>15}">
            <label title="${transaction.customerReference}">
                ${fn:substring(transaction.customerReference,0,15)}..
            </label>
        </c:when>
        <c:otherwise>${transaction.customerReference}</c:otherwise>
    </c:choose>
</td>
<td>${transaction.chargeCode}</td>
<td>
    <input type="text" id="invoiceOrBl${transaction.transactionId}" value="${transaction.invoiceOrBl}"
           style="width: 100px" class="textlabelsBoldForTextBox invoiceOrBl" maxlength="100"/>
    <input type="hidden" id="invoiceOrBl${transaction.transactionId}" value="${transaction.invoiceOrBl}"/>
    <input type="hidden" id="correctionNotice${transaction.transactionId}" value="${transaction.correctionNotice}"/>
    <input type="hidden" id="manifestFlag${transaction.transactionId}" value="${transaction.manifestFlag}"/>
</td>
<td>${transaction.voyage}</td>
<td>${transaction.dockReceipt}</td>
<c:choose>
    <c:when test="${fn:startsWith(transaction.formattedAmount,'-')}">
        <c:set var="invoiceAmount" value="(${fn:replace(transaction.formattedAmount,'-','')})"/>
        <c:set var="amountColor" value="red"/>
    </c:when>
    <c:otherwise>
        <c:set var="invoiceAmount" value="${transaction.formattedAmount}"/>
        <c:set var="amountColor" value="black"/>
    </c:otherwise>
</c:choose>
<td class="${amountColor}" style="text-align:right;">
    ${invoiceAmount}
    <input type="hidden" class="transactionAmount" value="${invoiceAmount}"/>
</td>
<c:choose>
    <c:when test="${fn:startsWith(transaction.formattedBalanceInProcess,'-')}">
        <c:set var="balanceInProcess" value="(${fn:replace(transaction.formattedBalanceInProcess,'-','')})"/>
        <c:set var="balanceInProcessColor" value="red"/>
    </c:when>
    <c:otherwise>
        <c:set var="balanceInProcess" value="${transaction.formattedBalanceInProcess}"/>
        <c:set var="balanceInProcessColor" value="black"/>
    </c:otherwise>
</c:choose>
<td class="${balanceInProcessColor}" style="text-align:right;">
    <label class="balanceInProcessLbl">${balanceInProcess}</label>
    <input type="hidden" class="balanceInProcess" id="balanceInProcess${transaction.transactionId}" value="${transaction.balanceInProcess}"/>
</td>
<td>${transaction.formattedDate}</td>
<td>
    ${transaction.transactionType}
    <input type="hidden" class="transactionType" value="${transaction.transactionType}"/>
</td>
<td>
    <input type="checkbox" value="${transaction.transactionId}" class="selectedIds"/>
    <input type="hidden" value="${transaction.transactionId}" class="transactionId"/>
</td>
<td>
    <input type="text" style="width: 70px"
           value="${transaction.paidAmount}" class="textlabelsBoldForTextBoxDisabledLook paidAmount" readonly tabindex="-1"/>
    <input type="hidden" class="originalPaidAmount" value="${transaction.paidAmount}"/>
</td>
<td>
    <input type="text" style="width: 70px"
           value="${transaction.adjustAmount}" class="textlabelsBoldForTextBoxDisabledLook adjustAmount" readonly tabindex="-1"/>
    <input type="hidden" class="originalAdjustAmount" value="${transaction.adjustAmount}"/>
<td>
    <input type="text" name="glAccount${varStatus.count}" id="glAccount${varStatus.count}" style="width: 70px"
           value="${transaction.glAccount}" class="textlabelsBoldForTextBoxDisabledLook glAccount" readonly tabindex="-1"/>
</td>
<td class="image-td">
    <div style="width: 90px;float: left">
        <c:choose>
            <c:when test="${fn:toLowerCase(transaction.status)==fn:toLowerCase(commonConstants.STATUS_INACTIVE)}">
                <img alt="" title="Inactive Accruals" src="${path}/images/locked.png" border="0"
                     onclick="activateAccruals('${transaction.transactionId}')" style="display: block;" class="lock"/>
                <img alt="" title="Active Accruals" src="${path}/images/unlocked.png" border="0"
                     onclick="inActivateAccruals('${transaction.transactionId}')" style="display: none;" class="unlock"/>
            </c:when>
            <c:when test="${fn:toLowerCase(transaction.status)!=fn:toLowerCase(commonConstants.STATUS_PENDING)}">
                <img alt="" title="Inactive Accruals" src="${path}/images/locked.png" border="0"
                     onclick="activateAccruals('${transaction.transactionId}')" style="display: none;" class="lock"/>
                <img alt="" title="Active Accruals" src="${path}/images/unlocked.png" border="0"
                     onclick="inactivateAccruals('${transaction.transactionId}')" style="display: block;" class="unlock"/>
            </c:when>
        </c:choose>
        <img alt="" src="${path}/img/icons/edit.gif" border="0" onclick="showUpdateAccrual('${transaction.transactionId}')"/>
        <c:if test="${not empty transaction.invoiceOrBl && fn:trim(transaction.invoiceOrBl)!='' 
                      && (!roleDuty.viewAccountingScanAttach || (roleDuty.viewAccountingScanAttach && transaction.subType != 'Y'))}">
	    <c:choose>
		<c:when test="${transaction.hasDocuments}">
		    <img alt="Scan/Attach" src="${path}/images/upload-green.gif" border="0"
			 title="Scan/Attach" onclick="showScanOrAttach('${transaction.transactionId}')"/>
		</c:when>
		<c:otherwise>
		    <img alt="Scan/Attach" src="${path}/img/icons/upload.gif" border="0"
			 title="Scan/Attach" onclick="showScanOrAttach('${transaction.transactionId}')"/>
		</c:otherwise>
	    </c:choose>
        </c:if>
	<c:choose>
	    <c:when test="${transaction.manualNotes}">
		<img alt="Notes" src="${path}/images/notepad_green.png" border="0" width="16px" height="16px"
		     title="Notes" onclick="showInvoiceNotes('${transaction.noteModuleId}','${transaction.noteRefId}')"/>
	    </c:when>
	    <c:otherwise>
		<img alt="Notes" src="${path}/images/notepad_yellow.png" border="0" width="16px" height="16px"
		     title="Notes" onclick="showInvoiceNotes('${transaction.noteModuleId}','${transaction.noteRefId}')"/>
	    </c:otherwise>
	</c:choose>		      
    </div>
</td>