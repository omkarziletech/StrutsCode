<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
<un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;" height="15px">
    <tbody>
        <tr>
            <td class="lightBoxHeader">Vendor Invoice Details</td>
            <td>
                <div>
                    <a id="lightBoxClose"  href="javascript:closeDetails();">
                        <img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
        </tr>
    </tbody>
</table>
<table cellpadding="0" cellspacing="0" style="width: 100%;height: 200px;" class="tableBorderNew" align="center">
    <tr>
        <td>
            <div id="divtablesty1" class="scrolldisplaytable" style="width: 100%;height: 200px;overflow: auto">
                <display:table name="${customersDetailList}" class="displaytagstyleNew"
                pagesize="<%=pageSize%>" style="width:100%" id="vendorDetails"
                               sort="list">
                    <display:setProperty name="paging.banner.some_items_found">
                        <span class="pagebanner"><font color="blue">{0}</font>
							Payables displayed,For more Records click on page numbers.</span>
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
                    <display:setProperty name="paging.banner.item_name" value="Payables" />
                    <display:setProperty name="paging.banner.items_name" value="Payables" />
                    <display:column property="invoiceOrBl" title="Vendor<br/>Invoice" sortable="true" headerClass="sortable" 
                                    style="text-transform:uppercase;"/>
                    <c:choose>
                        <c:when test="${fn:contains(vendorDetails.balance, '-')&& vendorDetails.recordType=='AP'}">
                            <display:column paramId="paramid" title="Invoice<br/>Amount" sortable="true" headerClass="sortable" style="color:red;text-align:right;" >
                                <c:out  value="(${fn:substring(vendorDetails.balance,1,fn:length(vendorDetails.balance))})">
                                </c:out>
                            </display:column>
                        </c:when>
                        <c:when test="${fn:contains(vendorDetails.balance, '-') && vendorDetails.recordType=='AR'}">
                            <display:column  title="Invoice<br/>Amount"
                                             sortable="true" headerClass="sortable" style="color:black;text-align:right;">
                                <c:out  value="${fn:substring(vendorDetails.balance,1,fn:length(vendorDetails.balance))}">
                                </c:out>
                            </display:column>
                        </c:when>
                        <c:when test="${!fn:contains(vendorDetails.balance, '-') && vendorDetails.recordType=='AR'}">
                            <display:column  title="Invoice<br/>Amount"
                                             sortable="true" headerClass="sortable" style="color:red;text-align:right;">
                                <c:out  value="(${vendorDetails.amount})">
                                </c:out>
                            </display:column>
                        </c:when>
                        <c:otherwise>
                            <display:column paramId="paramid" title="Invoice<br/>Amount" sortable="true" headerClass="sortable" style="color:black;text-align:right;" >
                                <c:out value="${vendorDetails.balance}"/>
                            </display:column>
                        </c:otherwise>
                    </c:choose>
                    <display:column property="invoiceDate" title="Invoice<br/>Date" sortable="true" headerClass="sortable"/>
                    <display:column property="duedate" title="Due<br/>Date" sortable="true" headerClass="sortable"/>
                    <display:column property="age" title="<br/>Age(Days)" sortable="true" headerClass="sortable"/>
                    <display:column property="creditTerms" title="<br/>Terms" sortable="true" headerClass="sortable" style="text-transform:uppercase;"/>
		    <c:if test="${canEdit}">
			<c:choose>
			    <c:when test="${vendorDetails.status==commonConstants.STATUS_WAITING_FOR_APPROVAL && !loginuser.achApprover}">
				<display:column title="<br/>Remove" style="text-align:center"/>
			    </c:when>
			    <c:otherwise>
				<display:column title="<br/>Remove" style="text-align:center">
				    <img alt="Remove" src="${path}/img/icons/remove.gif" onclick="removePayment('${vendorDetails.transactionId}','${transactionIds}')"/>
				    <img alt="Notes" src="${path}/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.AP_INVOICE}&moduleRefId=${vendorDetails.customerNo}-${vendorDetails.invoiceOrBl}',300,900);"/>
				</display:column>
			    </c:otherwise>
			</c:choose>
		    </c:if>
                </display:table>
            </div>
        </td>
    </tr>
</table>