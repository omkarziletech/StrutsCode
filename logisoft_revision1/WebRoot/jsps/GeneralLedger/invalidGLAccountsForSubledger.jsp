<%-- 
    Document   : invalidGLAccountsForSubledger
    Created on : Mar 31, 2011, 11:04:56 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
	<tr>
	    <td class="lightBoxHeader">Invalid Gl Accounts</td>
	    <td>
		<div style="vertical-align: top">
		    <a id="lightBoxClose" href="javascript: closeInvalid();">
			<img alt="" src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
		    </a>
		</div>
	    </td>
	</tr>
    </tbody>
</table>
<span class="pagebanner">
    ${fn:length(transactions)} Subledger Transactions with invalid Gl Account displayed
    <input type="button" class="buttonStyleNew" value="Post"  onclick="postGeneralLedger()"  style="width:65px"/>
</span>
<div class="scrolldisplaytable" style="width:99%;height:75%;overflow: auto">
    <c:set var="index" value="0"/>
    <display:table name="${transactions}" class="displaytagstyle" id="transaction" style="width:100%;">
	<display:column title="Subledger" property="subledgerCode"/>
	<display:column title="GlAccount">
	    <input type="text" name="glAccount" id="glAccount${index}" maxlength="10" class="textlabelsBoldForTextBox" size="10"/>
	    <input type="hidden" name="glAccountValid" id="glAccountValid${index}"/>
	    <input type="hidden" name="transactionId" value="${transaction.transactionId}"/>
	    <div class="newAutoComplete" id="glAccountChoices${index}"></div>
	    <img alt="" src="${path}/img/icons/ok.gif" onclick="saveGlAccount('glAccount${index}','${transaction.transactionId}')"
		 onmouseover="tooltip.show('<strong>Save GLAccount</strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer;"/>
	</display:column>
	<display:column title="BL" property="billOfLadding"/>
	<display:column title="Invoice" property="invoiceNumber"/>
	<display:column title="Charge Code" property="chargeCode"/>
	<display:column title="Voyage" property="voyage"/>
	<display:column title="Transaction Date" property="formattedDate"/>
	<display:column title="Reporting Date" property="formattedReportingDate"/>
	<display:column title="Posted Date" property="formattedPostedDate"/>
	<display:column title="Transaction Amount" property="formattedAmount"/>
	<display:column title="Debit" property="formattedDebit"/>
	<display:column title="Credit" property="formattedCredit"/>
	<display:column title="Record Type" property="transactionType"/>
        <c:set var="index" value="${index+1}"/>
    </display:table>
</div>
