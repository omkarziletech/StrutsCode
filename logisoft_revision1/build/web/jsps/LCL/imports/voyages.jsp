<%-- 
    Document   : voyages
    Created on : Sep 10, 2013, 5:18:47 PM
    Author     : Rajesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Imports voyages</title>
        <%@include file="/jsps/LCL/init.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/voyage.js"></script>
    </head>
    <body>
        <html:form action="linkVoyage.do" name="linkVoyageForm"
                   styleId="voyageForm" type="com.gp.cvst.logisoft.struts.form.lcl.LinkVoyageForm" scope="request" method="post">
            <div class="head-tag" style="position: fixed; width: 100%">
                Port of loading :
                <span class="green">${load}</span>
                <span style="margin-left: 150px;">Port of destination :</span>
                <span class="green">${dest}</span>
                <c:if test="${not empty voyages}">
                    <input type="button" class="button" id="linkVoy" name="linkVoy"
                           value="Link" style="margin-left: 300px"
                           onclick="doLink();"/>
                </c:if>
            </div>
            <br/>
            <%-- List of voyage --%>
            <br/>
            <div style="width:99.7%; float:left; height:380px; overflow-y:scroll; border:1px solid #dcdcdc">
                <table class="dataTable" width="100%">
                    <thead>
                        <tr>
                            <th width="1%"></th>
                            <th>Voyage #</th>
                            <th>Unit #</th>
                            <th>Terminal</th>
                            <th>Sail Date</th>
                            <th>Arrival Date</th>
                            <th>Transit days</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="zebra" value="odd"/>
                        <c:forEach items="${voyages}" var="voyage" varStatus="status">
                            <tr class="${zebra} row">
                                <td width="1%">
                                    <c:if test="${(voyage.dispoCode eq 'DATA' || voyage.dispoCode eq 'WATR' 
                                                  || (voyage.dispoCode eq 'PORT' && roleDuty.linkDrAfterDispositionPort)) && voyage.closedBy eq ''}">
                                          <input type="radio" class="radio" id="selectVoy${status.count}" name="selectVoy" value="${voyage.ssHeaderId}"
                                                 onclick="setUnitId('${voyage.unitSsId}','${voyage.dispoCode}');"/>
                                     </c:if>
                                </td>
                                <td>${voyage.scheduleNo}</td>
                                <td>${voyage.unitNo}</td>
                                <td>${voyage.terminal}</td>
                                <td>${voyage.etaSailDate}</td>
                                <td>${voyage.etaPodDate}</td>
                                <td>${voyage.totaltransPod}</td>
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
            <%-- hidden fields --%>
            <html:hidden property="fileId" styleId="fileId" value="${fileId}"/>
            <html:hidden property="voyageId" styleId="voyageId"/>
            <html:hidden property="unitId" styleId="unitId"/>
            <html:hidden property="unitId" styleId="dispoCode"/>
            <html:hidden property="fileNumber" styleId="fileNumber" value="${fileNumber}"/>
            <html:hidden property="navigation" styleId="navigation"/>
        </html:form>
    </body>
</html>
