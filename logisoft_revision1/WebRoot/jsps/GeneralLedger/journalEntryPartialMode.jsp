<%-- 
    Document   : journalEntryPartialMode
    Created on : 8 Sep, 2011, 8:08:46 PM
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
            <div style="float:left">
                <c:choose>
                    <c:when test="${glBatchForm.batchIndex-1>=0
                                    && glBatchForm.noOfBatches-1>glBatchForm.batchIndex}">
                        <c:set var="width" value="88"/>
                    </c:when>
                    <c:when test="${glBatchForm.batchIndex-1>=0
                                    || glBatchForm.noOfBatches-1>glBatchForm.batchIndex}">
                        <c:set var="width" value="120"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="width" value="150"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${glBatchForm.batchIndex-1>=0}">
                    <a title="First" href="javascript:gotoBatch('0')">
                        <img alt="" src="${path}/images/first.png" border="0" class="padding-top"/>
                    </a>
                    <a title="Previous" href="javascript:gotoBatch('${glBatchForm.batchIndex-1}')">
                        <img alt="" src="${path}/images/prev.png" border="0" class="padding-top"/>
                    </a>
                </c:if>
                <html:text property="glBatch.id" styleId="glBatchId" readonly="true" tabindex="-1"
                           styleClass="textlabelsBoldForTextBoxDisabledLook" style="float:left;width:${width}px;"/>
                <c:if test="${glBatchForm.noOfBatches-1>glBatchForm.batchIndex}">
                    <a title="Next" href="javascript:gotoBatch('${glBatchForm.batchIndex+1}')">
                        <img alt="" src="${path}/images/next.png" border="0" class="padding-top"/>
                    </a>
                    <a title="Last" href="javascript:gotoBatch('${glBatchForm.noOfBatches-1}')">
                        <img alt="" src="${path}/images/last.png" border="0" class="padding-top"/>
                    </a>
                </c:if>
            </div>
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
<tr>
    <td colspan="6" align="center">
        <input type="button" value="Go Back" class="buttonStyleNew" onclick="goBack()" tabindex="-1"/>
        <input type="button" value="Print" class="buttonStyleNew" onclick="printBatch()" tabindex="-1"/>
        <input type="button" value="Export" class="buttonStyleNew" onclick="exportBatch()" tabindex="-1"/>
        <c:set var="scanAttachForBatch" value="buttonStyleNew"/>
        <c:if test="${glBatchForm.glBatch.uploaded}">
            <c:set var="scanAttachForBatch" value="buttonColor"/>
        </c:if>
            <input type="button" value='Scan/Attach' tabindex="-1" class="${scanAttachForBatch}" 
                   onclick="showScanOrAttach('${glBatchForm.glBatch.id}')"/>
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
            <div style="float:left;">
                <c:choose>
                    <c:when test="${glBatchForm.journalEntryIndex-1>=0
                                    && glBatchForm.noOfJournalEntries-1>glBatchForm.journalEntryIndex}">
                        <c:set var="width" value="88"/>
                    </c:when>
                    <c:when test="${glBatchForm.journalEntryIndex-1>=0
                                    || glBatchForm.noOfJournalEntries-1>glBatchForm.journalEntryIndex}">
                        <c:set var="width" value="120"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="width" value="150"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${glBatchForm.journalEntryIndex-1>=0}">
                    <a title="First" href="javascript:gotoJournalEntry('0')">
                        <img alt="" src="${path}/images/first.png" border="0" class="padding-top"/>
                    </a>
                    <a title="Previous" href="javascript:gotoJournalEntry('${glBatchForm.journalEntryIndex-1}')">
                        <img alt="" src="${path}/images/prev.png" border="0" class="padding-top"/>
                    </a>
                </c:if>
                <html:text property="journalEntry.id" styleId="journalEntryId" readonly="true" tabindex="-1"
                           styleClass="textlabelsBoldForTextBoxDisabledLook" style="float:left;width:${width}px;"/>
                <c:if test="${glBatchForm.noOfJournalEntries-1>glBatchForm.journalEntryIndex}">
                    <a title="Next" href="javascript:gotoJournalEntry('${glBatchForm.journalEntryIndex+1}')">
                        <img alt="" src="${path}/images/next.png" border="0" class="padding-top"/>
                    </a>
                    <a title="Last" href="javascript:gotoJournalEntry('${glBatchForm.noOfJournalEntries-1}')">
                        <img alt="" src="${path}/images/last.png" border="0" class="padding-top"/>
                    </a>
                </c:if>
            </div>
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
        <c:choose>
            <c:when test="${not empty glBatchForm.journalEntry.flag && glBatchForm.journalEntry.flag=='R'}">
                <input type="button" value="Reverse"
                       class="buttonStyleNew" disabled style="background-color: green;color: white;cursor: auto" tabindex="-1"/>
            </c:when>
            <c:when test="${canEdit}">
                <input type="button" value="Reverse" class="buttonStyleNew" onclick="reverse()" tabindex="-1"/>
            </c:when>
        </c:choose>
        <c:if test="${canEdit}">
            <input type="button" value="Copy" class="buttonStyleNew" onclick="copyJournalEntry()" tabindex="-1"/>
        </c:if>
        <input type="button" value="Print" class="buttonStyleNew" onclick="printJournalEntry()" tabindex="-1"/>
        <input type="button" value="Export" class="buttonStyleNew" onclick="exportJournalEntry()" tabindex="-1"/>
        <c:if test="${not empty glBatchForm.journalEntry.subledgerClose && glBatchForm.journalEntry.subledgerClose=='Y'}">
            <input type="button" value="Print History" class="buttonStyleNew" onclick="printHistory()" tabindex="-1"/>
            <input type="button" value="Export History" class="buttonStyleNew" onclick="exportHistory()" tabindex="-1"/>
        </c:if>
        <c:set var="scanAttachForJE" value="buttonStyleNew"/>
        <c:if test="${glBatchForm.journalEntry.uploaded}">
            <c:set var="scanAttachForJE" value="buttonColor"/>
        </c:if>
        <input type="button" value='Scan/Attach' tabindex="-1" class="${scanAttachForJE}" 
               onclick="showScanOrAttach('${glBatchForm.journalEntry.id}')"/>    
    </td>
</tr>
</table>
<table width="100%" border="0" cellpadding="3" cellspacing="0" class="tableBorderNew displaytagstyleNew" id="lineItems">
    <tr class="tableHeadingNew capitalize" style="width:100%">
        <td colspan="8">Line Item Details</td>
    </tr>
    <tr class="list-heading capitalize">
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