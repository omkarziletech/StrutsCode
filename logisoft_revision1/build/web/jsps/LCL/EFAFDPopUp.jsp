<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%-- 
    Document   : EFTPopUp
    Created on : Sep 21, 2014, 1:39:55 PM
    Author     : aravindhan.v
--%>

<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript  src="${path}/jsps/LCL/js/lclImportBooking.js"/>
<!--Content From here-->
<body>
    <form name="lclBookingForm" id="lclBookingForm" action="lclBooking.do">
        <input type="hidden" id="methodName" name="methodName">
        <cong:div style="width:100%; float:left;">
            <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
                Send Status Report
            </cong:div> </cong:div>
            <table width="100%" class="dataTable" border="0">
                <thead>
                    <tr>
                        <th></th>
                        <th>Account No</th>
                        <th>Name</th>
                        <th>Email/Fax</th>
                        <th>Code F</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="contact" items="${contactList}">
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
                            <c:choose>
                                <c:when test="${contact.chargeCode=='E1' || contact.chargeCode=='E2' || contact.chargeCode=='E3'}">
                                    <input type="checkbox" name="r1" id="mailCheckbox" class="emailList"
                                           value="${contact.email}" onclick="mailValidate('${contact.email}');">
                                </c:when>
                                <c:when test="${contact.chargeCode=='F1' || contact.chargeCode=='F2' || contact.chargeCode=='F3'}">
                                    <input type="checkbox" name="r1" id="mailCheckbox" class="emailList"
                                           value="${contact.consigneeFax}">
                                </c:when>
                            </c:choose>
                        </td>
                        <td>${contact.agentNo}</td>
                        <td>${contact.agentName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${contact.chargeCode=='E1' || contact.chargeCode=='E2' || contact.chargeCode=='E3'}">
                                    ${contact.email}
                                </c:when>
                                <c:when test="${contact.chargeCode=='F1' || contact.chargeCode=='F2' || contact.chargeCode=='F3'}">
                                    ${contact.consigneeFax}
                                </c:when>
                            </c:choose>
                        </td>
                        <td>${contact.chargeCode}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <cong:div style="width:100%;">
            <input type="button" class="button-style1" id="sendEmail" value="Send" onclick="sendETFMail();"/>
            <input type="button" class="button-style1" id="cancelEmail" value="Cancel" onclick="closepopup();"/>
            <input type="hidden" id="headerId" name="headerId" value="${headerId}">
            <input type="hidden" id="etaFDDate" name="etaFDDate" value="${eftDate}">
        </cong:div>
    </form>
</body>
