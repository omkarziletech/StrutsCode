<%-- 
    Document   : wareHouseTooltip
    Created on : Aug 8, 2016, 6:33:56 PM
    Author     : NambuRajasekar
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table>
    <tbody>
        <c:set var="wareLocationCode" value="${fn:split(param.locationCode,'<>')}"/>
        <tr>
            <td>Line Location</td>
        </tr>
        <c:forEach items="${wareLocationCode}" var="location">
            <tr>
                <td style='text-transform: uppercase; color:darkblue'>${location}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
