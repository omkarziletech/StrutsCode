<%-- 
    Document   : AES
    Created on : Jun 6, 2013, 6:59:50 PM
    Author     : Shanmugam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@include file="/jsps/LCL/init.jsp" %>
<%@include file="/jsps/LCL/colorBox.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <cong:form name="lclBlForm" id="lclBlForm" action="lclBl.do">
            <cong:hidden name="fileNumberId" value="${lclBlForm.fileNumberId}"/>
            <cong:hidden name="fileNumber" value="${lclBlForm.fileNumber}"/>
              <input type="hidden" name="methodName" id="methodName"/>
            <table style="width:100%">
                <tr class="tableHeadingNew" >
                    <td width="90%">File No: <span class="fileNo">${lclBlForm.fileNumber}</span></td>
                </tr>
                <tr><td></td></tr>
                <tr class="tableHeadingNew" >
                    <td width="90%">AES/ITN View Details</td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" class="dataTable" style="border-collapse: collapse; border: 1px solid #dcdcdc" id="aestable">
                            <tr>
                                <th  width="10%">DR#</th>
                                <th width="40%">ITN Number</th>
                                <th  width="50%">Exception</th>
                            </tr>
                            <c:forEach items="${lcl3PList}" var="aes">
                                <c:choose>
                                    <c:when test="${zebra eq 'odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr class="${zebra}" style="text-transform: uppercase">
                                    <td>
                                        ${aes.lclFileNumber.fileNumber}
                                    </td>
                                    <td style="width:5%" class="AES_ITNNUMBER">
                                        <c:if test="${aes.type eq 'AES_ITNNUMBER'}">
                                            ${aes.reference}</c:if></td>
                                        <td style="width:5%" class="AES_EXCEPTION">
                                        <c:if test="${aes.type eq 'AES_EXCEPTION'}">
                                            ${aes.reference}
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </td>
                </tr>
            </table>
        </cong:form>
    </body>
</html>
