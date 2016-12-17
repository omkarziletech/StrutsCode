<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty purchaseOrderList}">
	<div class="result-container">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
			<th>P.O Number</th>
			<th>Package Type</th>
			<th>Package Quantity</th>
			<th>Handling Unit</th>
			<th>Quantity</th>
			<th>Class</th>
			<th>Weight</th>
                        <th>Length</th>
                        <th>Width</th>
                        <th>Height</th>
			<th>Hazmat</th>
			<th>NMFC</th>
                        <th>Action</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="po" items="${purchaseOrderList}">
			<tr class="${zebra}">
			    <td>${po.purchaseOrderNo}</td>
			    <td>${po.packageType}</td>
			    <td>${po.packageQuantity}</td>
			    <td>${po.handlingUnitType}</td>
			    <td>${po.handlingUnitQuantity}</td>
			    <td>${po.classes}</td>
			    <td>${po.weight}</td>
			    <td>${po.length}</td>
			    <td>${po.width}</td>
			    <td>${po.height}</td>
                            <c:choose>
                                <c:when test="${po.hazmat}">
                                    <td>Y</td>
                                </c:when>
                                <c:otherwise>
                                    <td>N</td>
                                </c:otherwise>
                            </c:choose>
			    <td>${po.nmfc}</td>
                            <td class="align-center" onclick="editPurchaseOrder('${po.id}')">
				<img src="${path}/images/icons/edit.gif" title="Edit"/>
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
	<div class="table-banner green" style="background-color: #D1E6EE;">No Purchase Order found</div>
    </c:otherwise>
</c:choose>
