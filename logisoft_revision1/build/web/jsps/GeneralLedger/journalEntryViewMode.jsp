<%-- 
    Document   : journalEntryViewMode
    Created on : 8 Sep, 2011, 8:07:52 PM
    Author     : lakshh
--%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table width="100%" border="0" cellpadding="3" cellspacing="0" class="tableBorderNew">
    <tr class="tableHeadingNew" style="width:100%">
        <td colspan="6">Batch Details</td>
    </tr>
    <tr class="textlabelsBold">
        <td colspan="2">No. Of Batches :  ${glBatchForm.noOfBatches}</td>
        <td colspan="4">
    <c:if test="${not empty message}">
        <div class="message">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
</td>
</tr>
<tr class="textlabelsBold">
    <td>Batch Number</td>
    <td>
<html:text property="glBatch.id" styleId="glBatchId" readonly="true"
           styleClass="textlabelsBoldForTextBoxDisabledLook" tabindex="-1"/>
</td>
<td>Description</td>
<td colspan="3">
<html:text property="glBatch.description" styleId="glBatchDescription"
           readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" tabindex="-1"/>
</td>
</tr>
<tr class="textlabelsBold">
    <td>Debit</td>
    <td>
<html:text property="glBatch.debit" styleId="glBatchDebit"
           styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
<input type="hidden" id="originalGlBatchDebit" value="${glBatchForm.glBatch.debit}"/>
</td>
<td>Credit</td>
<td>
<html:text property="glBatch.credit" styleId="glBatchCredit"
           styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
<input type="hidden" id="originalGlBatchCredit" value="${glBatchForm.glBatch.credit}"/>
</td>
<td>Status</td>
<td>
<html:text property="glBatch.status" styleId="glBatchStatus"
           styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
</td>
</tr>
</table>
<table width="100%" border="0" cellpadding="3" cellspacing="0" class="tableBorderNew">
    <tr class="tableHeadingNew" style="width:100%">
        <td colspan="6">Journal Entry Details</td>
    </tr>
    <tr class="textlabelsBold">
        <td colspan="6">No. Of Journal Entries :  ${glBatchForm.noOfJournalEntries}</td>
    </tr>
    <tr class="textlabelsBold">
        <td>Journal Entry Number</td>
        <td>
    <html:text property="journalEntry.id" readonly="true" tabindex="-1"
               styleId="journalEntryId" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
</td>
<td>Description</td>
<td colspan="3">
<html:text property="journalEntry.description" styleId="journalEntryDescription"
           readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" tabindex="-1"/>
</td>
</tr>
<tr class="textlabelsBold">
    <td>Period</td>
    <td>
<html:text property="journalEntry.period" styleId="journalEntryPeriod"
           styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
</td>
<td>Subledger Type</td>
<td>
<html:hidden property="journalEntry.subledgerType" styleId="journalEntrySubledgerType"/>
<html:text property="journalEntry.subledgerCode" styleId="journalEntrysubledgerCode"
           styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
</td>
<td>Subledger Description</td>
<td>
<html:text property="journalEntry.subledgerDescription" styleId="journalEntrySubledgerDescription"
           styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
</td>
</tr>
<tr class="textlabelsBold">
    <td>Debit</td>
    <td>
<html:text property="journalEntry.debit"
           styleId="journalEntryDebit" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
</td>
<td>Credit</td>
<td>
<html:text property="journalEntry.credit"
           styleId="journalEntryCredit" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
</td>
<td>Memo</td>
<td>
<html:text property="journalEntry.memo" styleId="journalEntryMemo"
           readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" tabindex="-1"/>
</td>
</tr>
<tr>
    <td colspan="6" align="center">
        <input type="button" value="Close" class="buttonStyleNew" onclick="window.parent.parent.GB_hide()"/>
    </td>
</tr>
</table>
<table width="100%" border="0" cellpadding="3" cellspacing="0" class="tableBorderNew displaytagstyleNew" id="lineItems">
    <tr class="tableHeadingNew" style="width:100%">
        <td colspan="8">Line Item Details</td>
    </tr>
    <tr class="list-heading">
        <td>GL Account</td>
        <td>Debit</td>
        <td>Credit</td>
        <td>Item Number</td>
        <td>Currency</td>
    </tr>
    <c:set var="zebra" value="odd"/>
    <c:forEach var="lineItem" items="${glBatchForm.lineItems}" varStatus="varStatus">
        <tr class="textlabelsBold ${zebra}" style="vertical-align: top">
            <td>
        <c:if test="${not empty glBatchForm.journalEntry.subledgerClose && glBatchForm.journalEntry.subledgerClose=='Y'}">
            <div class="float-left">
                <img alt="" src="${path}/img/icons/toggle.gif"
                     style="display: block;" onclick="drillDown(this)" class="drillDown float-left"/>
                <img alt="" src="${path}/img/icons/toggle_collapse.gif"
                     style="display: none;" onclick="drillUp(this)" class="drillUp float-left"/>
            </div>
        </c:if>
        <div class="float-left">
            <input type="text" readonly value="${lineItem.account}" tabindex="-1"
                   class="account textlabelsBoldForTextBoxDisabledLook" style="width: 125px;"/>
        </div>
        <div class="drillDownDiv"></div>
        </td>
        <td>
            <input type="text" readonly
                   class="debit textlabelsBoldForTextBoxDisabledLook" value="${lineItem.debit}" tabindex="-1"/>
        </td>
        <td>
            <input type="text" readonly
                   class="credit textlabelsBoldForTextBoxDisabledLook" value="${lineItem.credit}" tabindex="-1"/>
        </td>
        <td>
            <input type="text" readonly
                   class="lineItemId textlabelsBoldForTextBoxDisabledLook" value="${lineItem.id}" tabindex="-1"/>
        </td>
        <td>
            <input type="text" readonly
                   class="currency textlabelsBoldForTextBoxDisabledLook" value="${lineItem.currency}" tabindex="-1"/>
        </td>
        </tr>
        <c:choose>
            <c:when test="${zebra=='odd'}">
                <c:set var="zebra" value="even"/>
            </c:when>
            <c:otherwise>
                <c:set var="zebra" value="odd"/>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</table>