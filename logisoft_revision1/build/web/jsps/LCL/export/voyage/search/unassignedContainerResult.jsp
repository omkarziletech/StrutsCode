<%-- 
    Document   : unassignedContainerResult
    Created on : Jun 19, 2015, 10:20:54 AM
    Author     : Mei
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<div id="result-header" class="table-banner green">
    <div class="float-left">
        <c:choose>
            <c:when test="${fn:length(unassignedContainerList)>1}">
                ${fn:length(unassignedContainerList)} files found.
            </c:when>
            <c:otherwise>1 file found.</c:otherwise>
        </c:choose>
    </div>
</div>
<table class="display-table">
    <thead>
        <tr>
            <th>Unit#</th>
            <th>Size</th>
            <th>Comments</th>
            <th>CFT</th>
            <th>CBM</th>
            <th>KGS</th>
            <th>LBS</th>
            <th>Haz</th>
            <th>Trucking Info</th>
            <th>Created By</th>
            <th>Created Date</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach var="voyage" items="${unassignedContainerList}">
        <c:choose>
            <c:when test="${rowStyle eq 'oddStyle'}">
                <c:set var="rowStyle" value="evenStyle"/>
            </c:when>
            <c:otherwise>
                <c:set var="rowStyle" value="oddStyle"/>
            </c:otherwise>
        </c:choose>
        <tr class="${rowStyle}">
            <td style="text-transform: uppercase">${voyage.unitNo}</td>
            <td>${voyage.unitSize}</td>
            <td>${voyage.comments}</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>${voyage.isHazmat eq '1' ? 'YES' : 'NO'}</td>
            <td>${voyage.unitTrackingNotes}</td>
            <td style="text-transform: uppercase">${voyage.createdBy}</td>
            <td>${voyage.createdDate}</td>
            <td>
                <img src="${path}/images/edit.png" id="edit" alt="editUnit" style="vertical-align: middle" title="Edit Unit"
                     width="16" height="16" onclick="editUnits('${path}','${voyage.unitId}');"/>
                <img src="${path}/images/error.png" width="16" height="16" alt="delete" title="Delete Unit"
                     style="vertical-align: middle" onclick="deleteUnit('${voyage.unitId}')"/>
            </td>
        </tr>
    </c:forEach>
</tbody>
</table>


