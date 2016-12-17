<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;" height="15px">
    <tbody>
        <tr style="">
            <td class="lightBoxHeader">
				Check Register Details
            </td>
            <td>
                <div style="vertical-align: top;">
                    <a id="lightBoxClose" href="javascript:closeDetails();">
                        <img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
        </tr>
    </tbody>
</table>
<div class="scrolldisplaytable" style="width:99%; overflow: auto;height: 200px">
    <c:set var="i" value="0"></c:set>
    <display:table name="${checkRegisterDetailsList}" class="displaytagstyleNew" pagesize="100" id="checkRegisterDetails" style="width:100%;">
        <display:setProperty name="paging.banner.some_items_found">
            <span class="pagebanner"><font color="blue">{0}</font> Transaction displayed,For more Records click on page numbers.</span>
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
        <display:setProperty name="paging.banner.placement" value="bottom"/>
        <display:setProperty name="paging.banner.item_name" value="Transaction"/>
        <display:setProperty name="paging.banner.items_name" value="Transactions"/>
        <display:column property="chequenumber" title="Check<br/>Number" sortable="true" headerClass="sortable"/>
        <display:column property="customer" title="Vendor" sortable="true" headerClass="sortable" style="text-transform:uppercase;"/>
        <display:column property="customerNo" title="Vendor<br/>Number" sortable="true" headerClass="sortable" style="text-transform:uppercase;"/>
        <display:column property="invoiceOrBl" title="Invoice<br/>Number" sortable="true" headerClass="sortable" class="uppercase"/>
        <display:column property="paymentMethod" title="Payment<br/>Method" sortable="true" headerClass="sortable"/>
        <c:choose>
            <c:when test="${fn:contains(checkRegisterDetails.amount, '-')}">
                <display:column  title="Payment<br/>Amount" sortable="true" headerClass="sortable"  style="color:red">
                    <c:out  value="(${fn:substring(checkRegisterDetails.amount,1,fn:length(checkRegisterDetails.amount))})"/>
                </display:column>
            </c:when>
            <c:otherwise>
                <display:column  title="Payment<br/>Amount" sortable="true" headerClass="sortable" style="color:black">
                    <c:out value="${checkRegisterDetails.amount}"/>
                </display:column>
            </c:otherwise>
        </c:choose>
        <display:column property="transDate" title="Payment<br/>Date" sortable="true" headerClass="sortable"/>
        <display:column property="glAcctNo" title="GL<br/>Account" sortable="true" headerClass="sortable"/>
        <display:column property="bankAccountNumber" title="Bank<br/>Account" sortable="true" headerClass="sortable"/>
        <display:column title="Cleared">
            <c:choose>
                <c:when test="${checkRegisterDetails.cleared=='Y'}">
                    <input type="checkbox" id="cleared${i}"  checked='checked' disabled="disabled"/>
                </c:when>
                <c:otherwise>
                    <input type="checkbox" id="cleared${i}" disabled="disabled"/>
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column property="clearedDate" title="Cleared<br/>Date" sortable="true" headerClass="sortable"/>
        <display:column property="confirmationNumber" title="Confirmation<br/>Number" sortable="true" 
                        headerClass="sortable" style="text-transform:uppercase;"/>
	<c:if test="${canEdit}">
	    <display:column title="Action">
		<img title="Notes" alt="Notes" src="${path}/img/icons/info1.gif" border="0" onclick="window.parent.GB_showCenter('Notes', '${path}/notes.do?moduleId=${notesConstants.AP_INVOICE}&moduleRefId=${checkRegisterDetails.customerNo}-${checkRegisterDetails.invoiceOrBl}',300,900);"/>
	    </display:column>
	</c:if>
        <c:set var="i" value="${i+1}"></c:set>
    </display:table>
</div>
