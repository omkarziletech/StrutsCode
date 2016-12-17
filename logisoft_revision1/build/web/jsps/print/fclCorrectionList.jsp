<%-- 
    Document   : /jsps/print/fclCorrectionList
    Created on : Apr 19, 2016, 10:50:26 AM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <c:if test="${!empty noticeNumberList && fn:length(noticeNumberList)>0}">
        <div id="divtablesty1" style="border:thin;overflow:auto;height:80%;">
            <display:table  name="${noticeNumberList}" class="displaytagstyle" id="fclBlCorrections">
                <display:setProperty name="paging.banner.placement" ><span style="display:none;"></span></display:setProperty>
                <display:column title="Bol ID" >${fclBlCorrections.blNumber}</display:column>
                <display:column title="CN#">${fclBlCorrections.noticeNo}</display:column>
                <fmt:formatDate pattern="MM/dd/yyyy" var="date" value="${fclBlCorrections.date}"/>
                <display:column title="DATE">${date}</display:column>
                <display:column title="CorrectionType">${fclBlCorrections.correctionType.code}</display:column>
                <c:choose>
                    <c:when test="${RadioButtonCheck == fclBlCorrections.newBolIdForApprovedBl}">
                        <display:column title="Select"><span style="padding-left: 7px;"><input type="radio" name="printCheck" checked="checked" onclick="resetSession('${fclBlCorrections.newBolIdForApprovedBl}')"/> </span></display:column>
                    </c:when>
                    <c:otherwise>
                        <display:column title="Select"><span style="padding-left: 7px;"><input type="radio" name="printCheck"  onclick="resetSession('${fclBlCorrections.newBolIdForApprovedBl}')"/> </span></display:column>
                    </c:otherwise>
                </c:choose>
            </display:table>
            <table width="100%" border="0">
                <tr >
                    <td  class="textlabelsBold" align="right">Non Corrected FclBL</td>
                    <td align="center" style="padding-right: 10px;">
                <c:choose>
                    <c:when test="${fn:indexOf(RadioButtonCheck,'=')==-1}">
                        <input type="radio" name="printCheck"  checked="checked" onclick="resetSessionForOriginalBl('${fclBlCorrections.newBolIdForApprovedBl}')"/>
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="printCheck"  onclick="resetSessionForOriginalBl('${fclBlCorrections.newBolIdForApprovedBl}')"/>
                    </c:otherwise>
                </c:choose>
                </td>
                </tr>
            </table>
        </div>
    </c:if>
</body>
</html>
