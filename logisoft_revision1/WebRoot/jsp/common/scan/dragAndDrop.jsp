<%-- 
    Document   : dragAndDrop
    Created on : 21 Sep, 2015, 12:14:11 PM
    Author     : lakshmi
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Drag and Drop</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/common/dragAndDrop.js"></script>
        <style type="text/css">
            #drop {
                width: 95%;
                min-height: 275px;
                border: 5px dashed #4679bd;
                font-family: Verdana;
            }

            #drop legend{
                text-align:center;
                font-size: 20px;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <form action="${path}/scan.do" name="scanForm" id="scanForm" method="post">
            <input type="button" class="button" value="Go Back" onclick="goBack();"/>
            <fieldset id="drop">
                <legend>Drop inside</legend>
                <div id="files"></div>  
            </fieldset>
            
            <input type="hidden" name="documentId" id="documentId" value="${documentStoreLog.documentID}"/>
            <input type="hidden" name="documentName" id="documentName" value="${documentStoreLog.documentName}"/>
            <input type="hidden" name="documentType" id="documentType" value="${documentStoreLog.documentType}"/>
            <input type="hidden" name="comment" id="comment" value="${documentStoreLog.comments}"/>
            <input type="hidden" name="hiddenScreenName" id="hiddenScreenName" value="${scanForm.hiddenScreenName}"/>
            <input type="hidden" name="bookingType" id="bookingType" value="${scanForm.bookingType}"/>
            <input type="hidden" name="ssMasterBl" id="ssMasterBl" value="${scanForm.ssMasterBl}"/>
            <c:if test="${not empty documentStoreLog.status}">
                <input type="hidden" name="status" id="status" value="${documentStoreLog.status}"/>
            </c:if>
            <c:choose>
                <c:when test="${not empty scanForm.hiddenScreenName && scanForm.hiddenScreenName eq 'LCL IMPORTS DR' 
                  &&scanForm.bookingType eq 'T' && scanForm.screenName eq 'LCL EXPORTS DR'}">
                    <input type="hidden" name="screenName" id="screenName" value="${scanForm.screenName}"/> 
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="screenName" id="screenName" value="${documentStoreLog.screenName}"/>
                </c:otherwise>
            </c:choose>   
              
            <input type="hidden" name="action" id="action"/>
        </form>
        <div id="download" style="display: none;">
            <div style="font-family: Verdana; font-size: 13px;margin-left: 20px;">
                DDAddin: Drag and Drop to HTML5 Addin for Microsoft Outlook is not installed.<br><br> 
                <a href="${path}/Resources/ddaddin64.msi">DDAddin for 64bit Outlook</a><br> 
                <a href="${path}/Resources/ddaddin32.msi">DDAddin for 32bit Outlook</a><br><br> 
                <i style="font-size: 12px;">
                    Hint: <br>
                    i) On Windows 64bit, a 32bit Outlook is displayed as OUTLOOK.EXE *32 in the Task Manager.<br>
                    ii) After installation, please RESTART your Outlook.
                </i>
            </div>
        </div>
    </body>
</html>
