<%-- 
    Document   : addressTooltip
    Created on : Dec 18, 2013, 12:25:42 PM
    Author     : Rajesh
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table style='border: none'>
    <c:if test="${not empty param.whName}">
        <tr>
            <td style='color:#008000; border: none'>Name</td>
            <td style='border: none'>: ${param.whName}/${param.whNo}</td>
        </tr>
    </c:if>
    <c:if test="${not empty param.whCoName}">
        <tr>
            <td style='color:#008000; border: none'>C/O Name</td>
            <td style='border: none'>: ${param.whCoName}</td>
        </tr>
    </c:if>
    <c:if test="${not empty param.whAddress}">
        <tr>
            <td style='color:#008000; border: none'>Address</td>
            <td style='border: none'>: ${param.whAddress}</td>
        </tr>
    </c:if>
    <c:if test="${not empty param.whCity}">
        <tr>
            <td style='color:#008000; border: none'>City</td>
            <td style='border: none'>: ${param.whCity}</td>
        </tr>
    </c:if>
    <c:if test="${not empty param.whState}">
        <tr>
            <td style='color:#008000; border: none'>State</td>
            <td style='border: none'>: ${param.whState}</td>
        </tr>
    </c:if>
    <c:if test="${not empty param.whZip}">
        <tr>
            <td style='color:#008000; border: none'>Zip</td>
            <td style='border: none'>: ${param.whZip}</td>
        </tr>
    </c:if>
    <c:if test="${not empty param.whPhone}">
        <tr>
            <td style='color:#008000; border: none'>Phone</td>
            <td style='border: none'>: ${param.whPhone}</td>
        </tr>
    </c:if>
    <c:if test="${not empty param.whFax}">
        <tr>
            <td style='color:#008000; border: none'>Fax</td>
            <td style='border: none'>: ${param.whFax}</td>
        </tr>
    </c:if>
</table>