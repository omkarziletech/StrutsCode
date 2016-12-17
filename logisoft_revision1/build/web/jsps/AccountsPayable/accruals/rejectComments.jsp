<%-- 
    Document   : rejectComments
    Created on : Sep 7, 2012, 2:57:26 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 0;border: none;border-spacing: 0;">
    <thead>
	<tr>
	    <th>
		<div class="float-left">Reject Comments</div>
		<div class="float-right">
		    <a href="javascript: closeRejectComments()">
			<img alt="Close Comments" src="${path}/images/icons/close.png"/>
		    </a>
		</div>
	    </th>
	</tr>
    </thead>
    <tbody>
	<tr class="align-center">
	    <td>
		<textarea name="comments" id="comments" styleClass="textbox" style="width: 245px;height: 80px;"></textarea>
	    </td>
	</tr>
	<tr class="align-center">
	    <td>
		<input type="button" value="Reject" class="button" onclick="rejectInvoice()"/>
		<input type="button" value="Cancel" class="button" onclick="closeRejectComments()"/>
	    </td>
	</tr>
    </tbody>
</table>