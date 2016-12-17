<%-- 
    Document   : displayPickedConsolidateDrs
    Created on : 24 Aug, 2016, 11:55:28 PM
    Author     : PALRAJ
--%>
<%@include file="../init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="displayPickedConsolidateDr">
    <c:if test="${not empty pickedConsolidateDrList}">
        <c:forEach var="pickedConsolidateDr" items="${pickedConsolidateDrList}" >
            <span id="labelText">${pickedConsolidateDr}</span>
            <img src="${path}/jsps/LCL/images/close1.png" style="cursor:pointer" width="13" height="10"  onclick="removeConsolidateFile('${pickedConsolidateDr}')"/>
        </c:forEach><br/><br/>
        <table style="margin-left: 15.62em;">
            <tr>
                <td>
                    <input type="button" class="button-style1" value="save" onclick="savePickedDrToConsolidate()"/> 
                    <input id="duplicateFileNo" type="hidden" value="${duplicateFileNo}"/>
                    <input id="pickedConsolidateDrList" type="hidden" value="${pickedConsolidateDrList}"/>
                </td>
            </tr>
        </table>
    </c:if>
</div>
