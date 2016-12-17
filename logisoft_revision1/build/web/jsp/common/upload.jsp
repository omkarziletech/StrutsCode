<%-- 
    Document   : upload
    Created on : Mar 03, 2014, 6:35:26 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload</title>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/fileUpload/enhanced.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
	<script type="text/javascript" src="${path}/js/common/upload.js"></script>
    </head>
    <body>
	<%@include file="../../jsps/preloader.jsp"%>
        <c:choose>
            <c:when test="${not empty message}">
                <span class="message width-100pc align-center" style="margin: 20px 0 0 0;">${message}</span>
            </c:when>
            <c:otherwise>
                <html:form action="/upload?action=${param.action}" name="uploadForm" styleId="uploadForm"
                           type="com.logiware.common.form.UploadForm" scope="request" method="post" enctype="multipart/form-data">
                    <div class="error italic font-12px">&nbsp;</div>
                    <div class="float-left label" style="margin: 10px 0 0 50px;">
                        <span class="float-left">
                            <html:file property="file" styleId="file" tabindex="-1"/>
                        </span>
                        <span class="float-left" style="margin: 2px 0 0 5px;">
                            <input type="button" value="Upload" class="button" style="height: 17px" onclick="$('#uploadForm').submit();"/>
                        </span>
                    </div>
                </html:form>
            </c:otherwise>
        </c:choose>
    </body>
</html>
