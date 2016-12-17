<%-- 
    Document   : truckerRatesResults
    Created on : 27 Sep, 2012, 4:41:48 PM
    Author     : Lakshmi Narayanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:choose>
    <c:when test="${not empty truckerRatesForm.truckerRatesList}">
	<div align="right" class="table-banner" style="padding-right: 15px;">
	    <div style="float:right">
		<div style="float:left">
		    <c:choose>
			<c:when test="${truckerRatesForm.totalRows>truckerRatesForm.selectedRows}">
			    ${truckerRatesForm.selectedRows} out of ${truckerRatesForm.totalRows} Trucker Rates displayed.
			</c:when>
			<c:when test="${truckerRatesForm.selectedRows>1}">${truckerRatesForm.selectedRows} Trucker Rates displayed.</c:when>
			<c:otherwise>1 Trucker Rate displayed.</c:otherwise>
		    </c:choose>
		</div>
		<c:if test="${truckerRatesForm.totalPages>1 && truckerRatesForm.selectedPage>1}">
		    <a title="First page" href="javascript: gotoPage('1')">
			<img alt="First page" title="First page" src="${path}/images/first.png"/>
		    </a>
		    <a title="Previous page" href="javascript: gotoPage('${truckerRatesForm.selectedPage-1}')">
			<img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
		    </a>
		</c:if>
		<c:if test="${truckerRatesForm.totalPages>1}">
		    <select id="selectedPageNo" class="dropdown" style="float:left;">
			<c:forEach begin="1" end="${truckerRatesForm.totalPages}" var="selectedPage">
			    <c:choose>
				<c:when test="${truckerRatesForm.selectedPage!=selectedPage}">
				    <option value="${selectedPage}">${selectedPage}</option>
				</c:when>
				<c:otherwise>
				    <option value="${selectedPage}" selected>${selectedPage}</option>
				</c:otherwise>
			    </c:choose>
			</c:forEach>
		    </select>
		    <a href="javascript: gotoSelectedPage()">
			<img alt="Goto Page" title="Goto Page" src="${path}/images/go.jpg"/>
		    </a>
		</c:if>
		<c:if test="${truckerRatesForm.totalPages>truckerRatesForm.selectedPage}">
		    <a title="Next page" href="javascript: gotoPage('${truckerRatesForm.selectedPage+1}')">
			<img alt="Next page" title="Next page" src="${path}/images/next.png"/>
		    </a>
		    <a title="Last page" href="javascript: gotoPage('${truckerRatesForm.totalPages}')">
			<img alt="Last page" title="Last page" src="${path}/images/last.png"/>
		    </a>
		</c:if>
	    </div>
	</div>
	<div class="search-results">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
			<c:choose>
			    <c:when test="${truckerRatesForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
			    <c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
			</c:choose>
			<th class="width-175px"><a href="javascript:doSort('tp.acct_name','${orderBy}')">Trucker</a></th>
			<th><a href="javascript:doSort('tp.acct_no','${orderBy}')">Vendor Account</a></th>
			<th><a href="javascript:doSort('tr.from_zip','${orderBy}')">From Zip Code</a></th>
			<th><a href="javascript:doSort('tr.from_city','${orderBy}')">From City</a></th>
			<th><a href="javascript:doSort('tr.from_state','${orderBy}')">From State</a></th>
			<th><a href="javascript:doSort('un.un_loc_code','${orderBy}')">To Port</a></th>
			<th><a href="javascript:doSort('tr.rate','${orderBy}')">Rate</a></th>
			<th><a href="javascript:doSort('tr.fuel','${orderBy}')">Fuel</a></th>
			<th><a href="javascript:doSort('tr.buy','${orderBy}')">Buy</a></th>
			<th><a href="javascript:doSort('tr.haz','${orderBy}')">Haz</a></th>
			<th><a href="javascript:doSort('tr.markup','${orderBy}')">Markup %</a></th>
			<th><a href="javascript:doSort('tr.sell','${orderBy}')">Sell</a></th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="rate" items="${truckerRatesForm.truckerRatesList}">
			<tr class="${zebra}">
			    <c:choose>
				<c:when test="${not empty rate.truckerName}">
				    <td>
					<span class="width-175px" title="${rate.truckerName}">
					    ${str:abbreviate(rate.truckerName, 25)}
					</span>
				    </td>
				    <td>
					${rate.truckerNumber}
				    </td>
				</c:when>
				<c:otherwise>
				    <td>
					<input type="text" id="truckerName${rate.id}" class="truckerName textbox width-175px"/>
					<input type="hidden" class="truckerNameCheck"/>
				    </td>
				    <td>
					<input type="text" id="truckerNumber${rate.id}" class="truckerNumber textbox readonly" readonly/>
				    </td>
				</c:otherwise>
			    </c:choose>
			    <c:choose>
				<c:when test="${not empty rate.fromZip and fn:length(rate.fromState) eq 2}">
				    <td>
					${rate.fromZip}
				    </td>
				    <td>
					${rate.fromCity}
				    </td>
				    <td>
					${rate.fromState}
				    </td>
				</c:when>
				<c:otherwise>
				    <td>
					<input type="text" id="fromZip${rate.id}" class="fromZip textbox width-75px"/>
					<input type="hidden" class="fromZipCheck"/>
				    </td>
				    <td>
					<input type="text" id="fromCity${rate.id}" class="fromCity textbox readonly" readonly/>
				    </td>
				    <td>
					<input type="text" id="fromState${rate.id}" class="fromState textbox readonly width-30px" readonly/>
				    </td>
				</c:otherwise>
			    </c:choose>
			    <c:choose>
				<c:when test="${not empty rate.toPortCode}">
				    <td>
					${rate.toPortCode}
				    </td>
				</c:when>
				<c:otherwise>
				    <td>
					<input type="text" id="toPortCode${rate.id}" class="toPortCode textbox width-50px"/>
					<input type="hidden" class="toPortCodeCheck"/>
					<input type="hidden" id="toPort${rate.id}" class="toPort"/>
				    </td>
				</c:otherwise>
			    </c:choose>
			    <td class="align-right">${rate.rateValue}</td>
			    <td class="align-right">${rate.fuelValue}</td>
			    <td class="align-right">${rate.buyValue}</td>
			    <td class="align-right">${rate.hazValue}</td>
			    <td class="align-center">${rate.markup}</td>
			    <td class="align-right">
				${rate.sellValue}
				<c:if test="${empty rate.truckerName || empty rate.fromZip || fn:length(rate.fromState) ne 2 || empty rate.toPortCode}">
				    <input type="hidden" class="id" value="${rate.id}"/>
				</c:if>
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
		</tbody>
	    </table>
	</div>
    </c:when>
    <c:otherwise>
	<div class="table-banner green" style="background-color: #D1E6EE;">No Trucker Rates Found</div>
    </c:otherwise>
</c:choose>