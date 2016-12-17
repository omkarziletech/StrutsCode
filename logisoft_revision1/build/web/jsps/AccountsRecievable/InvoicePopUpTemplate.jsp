<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">List of Charges</td>
            <td>
		<a id="lightBoxClose" href="javascript: closePopUpDiv()">
		    <img alt="close" src="${path}/js/greybox/w_close.gif" title="Close" border="0">Close
		</a>
            </td>
        </tr>
    </tbody>
</table>
<div class="scrolldisplaytable" style="width:100%;height:270px;overflow: auto">
    <display:table name="${charges}" class="displaytagstyle" style="width:100%;" id="charge" sort="list">
        <display:setProperty name="paging.banner.some_items_found">
            <span class="pagebanner"><font color="blue">{0}</font>
		Charges displayed,for more charges click on page numbers.
            </span>
        </display:setProperty>
        <display:setProperty name="paging.banner.one_item_found">
            <span class="pagebanner">One {0} displayed. page number</span>
        </display:setProperty>
        <display:setProperty name="paging.banner.all_items_found">
            <span class="pagebanner">{0} {1} displayed, page number</span>
        </display:setProperty>
        <display:setProperty name="basic.msg.empty_list">
            <span class="pagebanner">No Charges Found.</span>
        </display:setProperty>
        <display:setProperty name="paging.banner.placement" value="top" />
        <display:setProperty name="paging.banner.item_name" value="Charge" />
        <display:setProperty name="paging.banner.items_name" value="Charges" />
        <display:column property="customerNumber" title=" Customer<br/>Number" style="text-transform:uppercase;"/>
        <display:column property="customerName" title="Customer<br/>Name" maxLength="30" style="text-transform:uppercase;"/>
        <display:column  title="Invoice/Bl" style="text-transform:uppercase;">
            <c:choose>
                <c:when test="${(fn:indexOf(charge.invoiceOrBl,'04-')>-1) && charge.transactionType=='AR'}">
                            <c:out  value="${fn:replace(fn:substring(charge.invoiceOrBl,fn:indexOf(charge.invoiceOrBl,'04-'),fn:length(charge.invoiceOrBl)),'-','')}"/>
                </c:when>
                <c:otherwise>${charge.invoiceOrBl}</c:otherwise>
            </c:choose>
        </display:column>
        <display:column property="formattedDate" title="Invoice<br/>Date"/>
        <display:column property="formattedPostedDate" title="Posted<br/>Date"/>
        <display:column property="chargeCode" title="Charge<br/>Code" style="text-transform:uppercase;"/>
        <display:column property="glAccountNumber" title="Gl<br/>Account" style="text-transform:uppercase;"/>
        <display:column property="subledgerCode" title="Subledger" style="text-transform:uppercase;"/>
        <c:choose>
	    <c:when test="${fn:startsWith(charge.formattedAmount, '-')}">
		<display:column title="Invoice<br/>Amount" style="color:red;text-align:right;">
		    (${fn:replace(charge.formattedAmount,'-','')})
		</display:column>
	    </c:when>
	    <c:otherwise>
		<display:column title="Invoice<br/>Amount" style="color:black;text-align:right;">${charge.formattedAmount}</display:column>
	    </c:otherwise>
        </c:choose>
                <display:column property="transactionType" title="Transaction<br/>Type" style="text-transform:uppercase;"/>
    </display:table>
</div>