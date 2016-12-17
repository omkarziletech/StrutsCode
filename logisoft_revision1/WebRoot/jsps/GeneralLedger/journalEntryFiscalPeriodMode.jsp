<%-- 
    Document   : journalEntryFullMode
    Created on : 8 Sep, 2011, 8:04:11 PM
    Author     : lakshh
--%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html:hidden property="from" styleId="from" value="FiscalPeriod"/>
<html:hidden property="year" styleId="year"/>
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
            <html:text property="glBatch.id" styleId="glBatchId"
                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
        </td>
        <td>Description</td>
        <td>
            <html:text property="glBatch.description" styleId="glBatchDescription" styleClass="textlabelsBoldForTextBox" maxlength="40"/>
        </td>
        <td>Status</td>
        <td>
            <html:text property="glBatch.status" styleId="glBatchStatus"
                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
        </td>
    </tr>
    <tr class="textlabelsBold">
        <td>Debit</td>
        <td>
            <html:text property="glBatch.debit" styleId="glBatchDebit"
                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
            <input type="hidden" id="batchDebit" value="${glBatchForm.batchDebit}"/>
        </td>
        <td>Credit</td>
        <td>
            <html:text property="glBatch.credit" styleId="glBatchCredit"
                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
            <input type="hidden" id="batchCredit" value="${glBatchForm.batchCredit}"/>
        </td>
        <td>Difference</td>
        <td>
            <input type="text" id="batchDifference" value="0.00"
                   class="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
        </td>
    </tr>
    <tr>
        <td colspan="8" align="center">
            <input type="button" value="Post & Close Year" class="buttonStyleNew" onclick="postAndCloseYear()" tabindex="-1"/>
            <input type="button" value="Delete" class="buttonStyleNew" onclick="deleteAndGoBackToFiscalPeriod()" tabindex="-1"/>
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
        <td>
            <html:text property="journalEntry.description" styleId="journalEntryDescription"
                       styleClass="textlabelsBoldForTextBox" maxlength="100" style="text-transform:uppercase;"/>
        </td>
        <td>Subledger Type</td>
        <td>
            <html:select property="journalEntry.subledgerType" tabindex="-1"
                         styleId="journalEntrySubledgerType" styleClass="textlabelsBoldForTextBox" style="width:150px">
                <html:optionsCollection property="subledgerTypes"/>
            </html:select>
        </td>
    </tr>
    <tr class="textlabelsBold">
        <td>Period</td>
        <td>
            <c:choose>
                <c:when test="${roleDuty.journalEntryClosedPeriod}">
                    <html:text property="journalEntry.period" styleId="journalEntryPeriod"
                               styleClass="textlabelsBoldForTextBox"  maxlength="8"/>
                    <input type="hidden" id="journalEntryPeriodValid" name="jperValid"  value="${glBatchForm.journalEntry.period}"/>
                    <div id="journalEntryPeriodChoices" style="display: none" class="newAutoComplete"></div>
                </c:when>
                <c:otherwise>
                    <html:select property="journalEntry.period"
                                 styleId="journalEntryPeriod" styleClass="textlabelsBoldForTextBox" style="width:125px">
                        <html:optionsCollection property="journalEntry.periods"/>
                    </html:select>
                </c:otherwise>
            </c:choose>
        </td>
        <td>Memo</td>
        <td>
            <html:text property="journalEntry.memo" styleId="journalEntryMemo" styleClass="textlabelsBoldForTextBox" maxlength="200" 
                       style="text-transform:uppercase;"/>
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
            <html:text property="journalEntry.credit" styleId="journalEntryCredit" 
                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
        </td>
        <td>Difference</td>
        <td>
            <input type="text" id="journalEntryDifference" value="0.00"
                   class="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
        </td>
    </tr>
    <tr class="textlabelsBold">
        <td colspan="8" align="center">
            <input type="button" value="Save" class="buttonStyleNew" id="save" tabindex="-1"/>
            <c:choose>
                <c:when test="${not empty glBatchForm.journalEntry.flag && glBatchForm.journalEntry.flag=='A'}">
                    <input type="button" value="Auto Reverse"
                           class="buttonStyleNew" disabled 
                           style="background-color: green;color: white;cursor: auto" tabindex="-1"/>
                </c:when>
                <c:otherwise>
                    <input type="button" value="Auto Reverse"
                           class="buttonStyleNew" onclick="autoReverse()" tabindex="-1"/>
                </c:otherwise>
            </c:choose>
            <input type="button" value="Copy" class="buttonStyleNew" onclick="copyJournalEntry()" tabindex="-1"/>
            <input type="button" value="Add" class="buttonStyleNew" onclick="addJournalEntry()" tabindex="-1"/>
            <input type="button" value="Delete" class="buttonStyleNew" onclick="deleteJournalEntry()" tabindex="-1"/>
            <input type="button" value="Print" class="buttonStyleNew" onclick="printJournalEntry()" tabindex="-1"/>
            <input type="button" value="Export" class="buttonStyleNew" onclick="exportJournalEntry()" tabindex="-1"/>
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
        <td>
            Actions
            <img src="${path}/img/icons/add2.gif" border="0" alt="" class="addLineItem"/>
        </td>
    </tr>
    <tr class="textlabelsBold defaultLineItem" style="display:none;vertical-align: top">
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
                <input type="text" class="account textlabelsBoldForTextBox" style="width: 125px;"/>
                <input type="hidden" class="accountValid"/>
                <input type="hidden" class="accountDescription"/>
                <div class="newAutoComplete accountChoices"></div>
            </div>
            <div class="drillDownDiv"></div>
        </td>
        <td><input type="text" class="debit textlabelsBoldForTextBox" maxlength="11" value="0.00"/></td>
        <td><input type="text" class="credit textlabelsBoldForTextBox" maxlength="11" value="0.00"/></td>
        <td><input type="text" class="lineItemId textlabelsBoldForTextBoxDisabledLook" readonly tabindex="-1"/></td>
        <td><input type="text" class="currency textlabelsBoldForTextBoxDisabledLook" value="USD" readonly tabindex="-1"/></td>
        <td align="center">
            <img src="${path}/img/icons/remove.gif"  alt="" border="0" class="removeLineItem"/>
        </td>
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
                    <input type="text" name="account${varStatus.count}" id="account${varStatus.count}"
                           class="account textlabelsBoldForTextBox" value="${lineItem.account}" style="width: 125px;"/>
                    <input type="hidden" name="accountValid${varStatus.count}" value="${lineItem.account}"
                           id="accountValid${varStatus.count}" class="accountValid"/>
                    <input type="hidden" name="accountDescription${varStatus.count}" id="accountDescription${varStatus.count}"
                           value="${lineItem.accountDescription}" class="accountDescription"/>
                    <div class="newAutoComplete accountChoices" id="accountChoices${varStatus.count}"></div>
                    <script type="text/javascript">
                        var rowIndex = "${varStatus.count}";
                        var autocompleterUrl = rootPath + "/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=account" + rowIndex;
                        autocompleterUrl += "&tabName=JournalEntry"
                        var fn = "moveToDebit('#account" + rowIndex + "')";
                        AjaxAutocompleter("account" + rowIndex, "accountChoices" + rowIndex,
                                "accountDescription" + rowIndex, "accountValid" + rowIndex, autocompleterUrl, fn, "");
                    </script>
                </div>
                <div class="drillDownDiv"></div>
            </td>
            <td><input type="text" class="debit textlabelsBoldForTextBox" maxlength="11" value="${lineItem.debit}"/></td>
            <td><input type="text" class="credit textlabelsBoldForTextBox" maxlength="11" value="${lineItem.credit}"/></td>
            <td>
                <input type="text"
                       class="lineItemId textlabelsBoldForTextBoxDisabledLook" value="${lineItem.id}" readonly tabindex="-1"/>
            </td>
            <td><input type="text" class="currency textlabelsBoldForTextBoxDisabledLook" value="USD" readonly tabindex="-1"/></td>
            <td align="center">
                <img src="${path}/img/icons/remove.gif"  alt="" border="0" class="removeLineItem"/>
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
