<%-- 
    Document   : results
    Created on : Mar 25, 2016, 4:49:14 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table">
    <tr class="tableHeadingNew"><td>Search Result</td></tr>
    <tr><td  id="autoCostListTable">
            <div class="table-banner green">
                <table style="margin: 0;overflow: hidden" id="corporateAccountList">
                    <tr><td>
                            <c:choose>
                                <c:when test="${fn:length(autoCostList)>=1}">
                                    ${fn:length(autoCostList)}   files found.
                                </c:when>
                                <c:otherwise>No file found.</c:otherwise>
                            </c:choose>
                        </td></tr>
                </table>
            </div>
            <c:if test="${not empty autoCostList}">
                <table class="dataTable">
                    <thead>
                    <th>Origin</th>
                    <th>Destination</th>
                    <th>Size</th>
                    <th>Cost Code</th>
                    <th>Type</th>
                    <th>Rate Uom</th>
                    <th>Rate Per Uom</th>
                    <th>Rate Action</th>
                    <th>Rate Condition</th>
                    <th>Rate Condition Qty</th>
                    <th>VendorNo#</th>
                    <th>Action<input type="checkbox" id='checkAll' onclick='copyAllCost();'></th>
                    </thead>
                    <tbody style="text-transform: uppercase">
                        <c:forEach var="unitAutoCost" items="${autoCostList}">
                            <c:choose>
                                <c:when test="${zebra eq 'odd'}">
                                    <c:set var="zebra" value="even"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="odd"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${zebra}" style="border-color:white;">
                                <td>${unitAutoCost.originName}</td>
                                <td>${unitAutoCost.fdName}</td>
                                <td>${unitAutoCost.unitTypeDesc}</td>
                                <td>${unitAutoCost.costCode}</td>
                                <td>${unitAutoCost.costType}</td>
                                <td>${unitAutoCost.rateUom}</td>
                                <td>${unitAutoCost.ratePerUom}</td>
                                <td>${unitAutoCost.rateAction}</td>
                                <td>${unitAutoCost.rateCondition}</td>
                                <td>${unitAutoCost.rateConQty}</td>
                                <td title="${unitAutoCost.vendorName}">${unitAutoCost.vendorNo}</td>
                                <td>
                                    <img src="${path}/images/edit.png"  onclick="editCost('${path}', '${unitAutoCost.autoCostId}')"
                                         style="cursor:pointer" width="13" height="13" alt="edit" title="Edit"/>
                                    <img src="${path}/images/error.png"  onclick="deleteCost('${unitAutoCost.autoCostId}')"
                                         style="cursor:pointer" width="13" height="13" alt="edit" title="Edit"/>
                                    <input type="checkbox" id='${unitAutoCost.autoCostId}' class='checkSingle ${unitAutoCost.costCode}'>
                                </td>
                                <td class="costCode${unitAutoCost.autoCostId}" 
                                    style="display: none">${unitAutoCost.costCode}#${unitAutoCost.unitTypeId}#${unitAutoCost.rateUom}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </td>
    </tr>
</table>

