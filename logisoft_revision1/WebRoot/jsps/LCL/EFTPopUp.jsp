<%-- 
    Document   : EFTPopUp
    Created on : Sep 21, 2014, 1:39:55 PM
    Author     : aravindhan.v
--%>

<%@include file="init.jsp" %>
<link type="text/css" rel="stylesheet" media="screen" href="${path}/css/common.css" />
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
        <table class="dataTable" border="0">
            <thead>
                <tr>
                    <th></th>
                    <th>Account No</th>
                    <th>Name</th>
                    <th>Email</th>
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
                        <input type="checkbox" name="r1" id="mailCheckbox${contact.id}" class="emailList" onclick="checkETFMail('${contact.id}','${contact.email}')">
                    </td>
                    <td>${contact.accountNo}</td>
                    <td>${contact.firstName}</td>
                    <td>${contact.email}</td>
                </tr>
                 </c:forEach>
            </tbody>
    </table>
    <cong:div style="width:100%;">
        <input type="button" class="button-style1" id="sendEmail" value="Send" onclick="sendETFMail();"/>
        <input type="button" class="button-style1" id="cancelEmail" value="Cancel" onclick="closepopup();"/>
    </cong:div>
</form>
</body>