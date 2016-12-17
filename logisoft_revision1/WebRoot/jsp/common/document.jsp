<%-- 
    Document   : document
    Created on : Mar 11, 2013, 5:27:25 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Documents</title>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
	<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
	<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
		var path = "${path}";
	</script>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/common/document.js"></script>
	<c:set var="accessMode" value="1"/>
	<c:set var="writeMode" value="true"/>
	<c:if test="${param.accessMode==0}">
	    <c:set var="accessMode" value="0"/>
	    <c:set var="writeMode" value="false"/>
	</c:if>
    </head>
    <body>
	<div id="body-container">
	    <table class="display-table">
		<thead>
		    <tr>
			<th>Document Name</th>
			<th>File Name</th>
			<th>File Size</th>
			<th>Operation</th>
			<th>Operation Date</th>
			<c:if test="${documentForm.documentName =='SS LINE MASTER BL'}">
			    <th>Status</th>
			</c:if>
			<th>Comments</th>
			<th>View</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="document" items="${documentForm.documents}" varStatus="status">
			<tr class="${zebra}">
			    <td>${document.documentName}</td>
			    <td>${document.fileName}</td>
			    <td>${document.fileSize}</td>
			    <td>${document.operation}</td>
			    <td>${document.operationDate}</td>
			    <c:if test="${documentForm.documentName =='SS LINE MASTER BL'}">
				<td>${document.status}</td>
			    </c:if>
			    <td>
				${str:splitter(document.comments,30,'<br>')}
			    </td>
			    <td>
				<img src="${path}/images/icons/preview.png"
				     title="View" onclick="viewDocument('${document.fileName}','${document.fileLocation}/${document.fileName}')"/>
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
    </body>
</html>