<%--
    Document   : addressTooltip
    Created on : Feb 14, 2014, 12:25:42 PM
    Author     : Mohana
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<table>
    <tbody>
        <c:set var="amsDetail" value="${fn:split(param.amsDetails,'<br/>')}"/>
        <tr>
            <td>SCAC</td>
            <td>AMS#</td>
            <td>PIECES</td>
        </tr>
        <c:forEach var="amsBl" items="${amsDetail}" varStatus="st">
            <tr>
                <c:set var="ams" value="${fn:split(amsBl,',')}"/>
                <td style='color:darkblue'>${ams[0]}</td>
                <td style='color:darkblue'>${ams[1]}</td>
                <td style='color:darkblue'>${ams[2]}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>