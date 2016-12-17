<%-- 
    Document   : predefinedRemarks
    Created on : May 17, 2013, 4:59:42 PM
    Author     : Rajesh
--%>
<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>

<c:set var="path" value="${pageContext.request.contextPath}" />

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${path}/jsps/LCL/js/predefinedRemarks.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>

<div class="display-table width-100pc">
    <display:table name="${genericCodeList}" id="gc" class="width-100pc">
        <display:column title="">
            <c:choose>
                <c:when test="${not empty multipleSelect}">
                    <input type="checkbox" value="${gc}" class="predefinedRemarks"/>
                </c:when>
                <c:otherwise>
                    <input type="checkbox" value="${gc}" class="predefinedRemarks" onclick="uncheckOther(this);"/>
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column title="Remarks">${gc}</display:column>
    </display:table>
    <c:choose>
        <c:when test="${not empty multipleSelect && empty from}">
            <input type="button" id="submit-remarks" value="Submit" class="button" onclick="submitRemarks('#externalComment');" />
        </c:when>
        <c:when test="${not empty multipleSelect && from eq 'commodityPopup'}">
            <input type="button" id="submit-remarks" value="Submit" class="button" onclick="submitRemarks('#pieceDesc');" />
        </c:when>
        <c:otherwise>
            <input type="button" id="submit-remarks" value="Submit" class="button" onclick="submitRemarks('#remarksForLoading');" />
        </c:otherwise>
    </c:choose>
</div>