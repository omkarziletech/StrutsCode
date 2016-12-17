<%-- 
    Document   : lclMultiUnitResult
    Created on : Dec 3, 2015, 8:14:11 PM
    Author     : Mei
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../../../init.jsp" %>
        <%@include file="../../../../includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/taglib.jsp" %>
        <title>MultiUnit</title>
    </head>
    <body  class="whitebackgrnd" id="pane">
        <table width="100%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
            <tr class="tableHeadingNew"><td>Unit No:<span class="red">${lclUnitsScheduleForm.unitNo}</span></td></tr>
        </table>
        <table class="dataTable">
            <thead>
                <tr>
                    <th>Voyage#</th>
                    <th>Origin</th>
                    <th>Destination</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="voyage" items="${unitList}">
                    <c:choose>
                        <c:when test="${rowStyle eq 'oddStyle'}">
                            <c:set var="rowStyle" value="evenStyle"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" value="oddStyle"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowStyle}">
                        <td>
                            <u>
                                <span style="height: 10px;cursor: pointer;"
                                      class="link"
                                      onclick="search('${voyage.arrivalPier}','${voyage.pooId}','${voyage.fdId}','${voyage.departPier}','${voyage.scheduleNo}')">
                                    ${voyage.scheduleNo}
                                </span>
                            </u>
                        </td>
                        <td><span title="${voyage.arrivalPier}">${voyage.arrivalPierUnloc}</span></td>
                        <td><span title="${voyage.departPier}">${voyage.departPierUnloc}</span></td>
                    </tr>

                </c:forEach>
            </tbody>
        </table>
        <input type="hidden" name="filterByChanges" id="filterByChanges" value="${lclUnitsScheduleForm.filterByChanges}"/>
    </body>
</html>
<script type="text/javascript">
    function search(pooName,pooId,fdId,fdName,voyageNo){
        var filterByValue = $('#filterByChanges').val();
        var serviceType="";
        parent.$('#portOfOriginId').val(pooId);
        parent.$('#finalDestinationId').val(fdId);
        parent.$('#portOfOriginId').val(pooId);
        parent.$('#origin').val(pooName);
        parent.$('#finalDestinationId').val(fdId);
        parent.$('#destination').val(fdName);
        parent.$('#voyageNo').val(voyageNo);
        parent.$('#voyageNo').val(voyageNo);
//        if(filterByValue==="lclCfcl"){
//            serviceType='C';
//        }else if(filterByValue==="lclExport"){
//            serviceType='E';
//
//        }else if(filterByValue==="lclDomestic"){
//            serviceType='N';
//        }
     parent.$('#serviceType').val('');
        parent.search('search');
    }
</script>