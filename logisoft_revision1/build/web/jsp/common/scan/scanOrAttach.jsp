<%-- 
    Document   : scanOrAttach
    Created on : 13 Jan, 2015, 12:53:40 AM
    Author     : Lucky
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Scan/Attach</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/fileUpload/enhanced.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.util.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/common/scanOrAttach.js"></script>
    </head>
    <body>
        <span class="message">${message}</span>
        <span class="error">${error}</span>
        <html:form action="/scan" name="scanForm" styleId="scanForm" 
                   type="com.logiware.common.form.ScanForm" scope="request" method="post" enctype="multipart/form-data">
            <div>
            <table class="table">
                <tr>
                    <th>
                        <span>Document List for ${scanForm.screenName}</span>
                        <c:choose>
                            <c:when test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                <input type="button" value="Add" class="button float-right" onclick="addDocument()"/>
                                <html:select property="screenName" styleId="screenName" 
                                             styleClass="dropdown width-220px float-right" onchange="search()">
                                    <html:option value="">ALL</html:option>
                                    <html:optionsCollection property="screenNames" label="codedesc" value="codedesc"/>
                                </html:select>
                                <input type="hidden" name="scanConfig.id"/>
                                <input type="hidden" name="scanConfig.screenName"/>
                                <input type="hidden" name="scanConfig.documentName"/>
                            </c:when>
                            <c:otherwise>
                                <html:hidden property="screenName" styleId="screenName"/>
                                <html:hidden property="documentName" styleId="documentName"/>
                                <html:hidden property="documentId" styleId="documentId"/>
                                <html:hidden property="ssMasterBl" styleId="ssMasterBl"/>
                                <c:choose>
                                    <c:when test="${scanForm.screenName eq 'INVOICE' or scanForm.screenName eq 'AR BATCH'}">
                                        <span class="red">&nbsp;${scanForm.documentId}</span>
                                    </c:when>
                                    <c:when test="${scanForm.screenName eq 'LCL UNITS'}">
                                        <span class="red">&nbsp;${scanForm.unitNo}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>File No</span><span class="red">&nbsp;${scanForm.documentId}</span>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${not empty scanForm.results1 || not empty scanForm.results2}">
                                    <input type="button" value="View All" class="button float-right" onclick="showDocuments('')"/>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </th>
                </tr>
                <tr>
                    <td><c:import url="/jsp/common/scan/results.jsp"/></td>
                    </tr>
            </table>
            </div>
                <br/>
                <br/>
                <div>
                <c:if test="${not empty scanForm.importTransResults1 || not empty scanForm.importTransResults2}">
                    <table class="table">
                        <tr>
                            <th>
                                <span>Document List for LCL IMPORTS DR Transhipment</span>
                                 
                                <c:choose>
                                    <c:when test="${empty scanForm.documentId && loginuser.role.roleDesc eq 'Admin'}">
                                        <input type="button" value="Add" class="button float-right" onclick="addDocument()"/>
                                        <html:select property="screenName" styleId="screenName" 
                                                     styleClass="dropdown width-220px float-right" onchange="search()">
                                            <html:option value="">ALL</html:option>
                                            <html:optionsCollection property="screenNames" label="codedesc" value="codedesc"/>
                                        </html:select>
                                        <input type="hidden" name="scanConfig.id"/>
                                        <input type="hidden" name="screenName" styleId="screenName"/>
                                        <input type="hidden" name="scanConfig.documentName"/>
                                        <html:hidden property="bookingType" styleId="bookingType"/>
                                          </c:when>
                                    <c:otherwise>
                                        <html:hidden property="screenName" styleId="screenName"/>
                                        <html:hidden property="documentName" styleId="documentName"/>
                                        <html:hidden property="documentId" styleId="documentId"/>
                                        <html:hidden property="ssMasterBl" styleId="ssMasterBl"/>
                                        <html:hidden property="bookingType" styleId="bookingType"/>
                                        <c:choose>
                                            <c:when test="${scanForm.screenName eq 'INVOICE' or scanForm.screenName eq 'AR BATCH'}">
                                                <span class="red">&nbsp;${scanForm.documentId}</span>
                                            </c:when>
                                            <c:when test="${scanForm.screenName eq 'LCL UNITS'}">
                                                <span class="red">&nbsp;${scanForm.unitNo}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span>File No</span><span class="red">&nbsp;${scanForm.documentId}</span>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${not empty scanForm.importTransResults1 || not empty scanForm.importTransResults2}">
                                            <input type="button" value="View All" class="button float-right" onclick="showDocumentForTrans('','${scanForm.screenName}','${scanForm.hiddenScreenName}')"/>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </th>
                        </tr>
                        <tr>
                        <div id="results">
                               <%@include file="/jsp/common/scan/importTranshipmentResults.jsp" %>
                               
                        </div>
                        </tr>
                    </table>
                    </div>    
                </c:if>       
                    <html:hidden property="action" styleId="action"/>
                    <html:hidden property="hiddenScreenName" styleId="hiddenScreenName"/>

        </html:form>
    </body>
</html>
