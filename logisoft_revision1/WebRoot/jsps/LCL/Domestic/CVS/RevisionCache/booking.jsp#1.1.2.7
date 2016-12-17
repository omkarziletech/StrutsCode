<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="../init.jsp" %>
        <%@include file="../colorBox.jsp" %>
        <%@include file="../../preloader.jsp" %>
        <%@include file="../../includes/baseResources.jsp"%>
        <%@include file="../../includes/resources.jsp" %>
        <%@include file="../../includes/jspVariables.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Misc Invoice Pool</title>
	<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
	<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
		var path = "${path}";
	</script>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>

    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
	<html:form action="/domesticBooking" name="bookingForm"
		   styleId="bookingForm" type="com.logiware.domestic.form.BookingForm" scope="request" method="post">
	    <html:hidden property="methodName" styleId="methodName"/>
	    <html:hidden property="bookingId" styleId="bookingId"/>
	    <table class="table" style="margin: 0">
		<tr>
		    <th colspan="10">Search Criteria</th>
		</tr>
		<tr>
		    <td class="label">From Zip</td>
		    <td>
			<cong:autocompletor styleClass="textlabelsBoldForTextBox "  id="fromZip" name="fromZip" scrollHeight="200px"
                                query="CONCAT_CITY" fields="NULL,NULL,NULL" template="concatOrigin" container="NULL" width="500" shouldMatch="true" />
		    </td>
		    <td class="label">To Zip</td>
		    <td>
			<cong:autocompletor styleClass="textlabelsBoldForTextBox "  id="toZip" name="toZip" scrollHeight="200px"
                                query="CONCAT_CITY" fields="NULL,NULL,NULL" template="concatOrigin" container="NULL" width="500" shouldMatch="true" />
		    </td>
		    <td class="label">Booking Number</td>
		    <td>
			<html:text property="bookingNumber" styleId="bookingNumber" styleClass="textbox"/>
		    </td>
		    <td class="label">Carrier name</td>
		    <td>
			<html:text property="carrierName" styleId="carrierName" styleClass="textbox"/>
		    </td>
                    <td styleClass="label">User</td>
                    <td>
                        <c:choose>
                            <c:when test="${loginuser.role.roleDesc == 'Admin' || loginuser.role.roleDesc == 'SALES'}">
                                <html:select property="userId" styleId="userId"
                                                         style="width:125px;" styleClass="textlabelsBoldForTextBox dropdown_accounting" >
                                                         <html:optionsCollection  name="userNameList" />
                                            </html:select>
                            </c:when>
                            <c:otherwise>
                                <html:text property="userName" styleId="userName" styleClass="textbox" value="${loginuser.firstName}"/>
                                <html:hidden property="userId" styleId="userId" styleClass="textbox" value="${loginuser.userId}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
		</tr>
		<tr>
		    <td colspan="10" class="align-center">
			<input type="button" value="Search" class="button" onclick="search();"/>
			<input type="button" value="Reset" class="button" onclick="resetAll();"/>
		    </td>
		</tr>
		<tr>
		    <th colspan="10">Domestic Booking List</th>
		</tr>
		<tr>
		    <td colspan="10">
			<div id="results">
			    <c:import url="/jsps/LCL/Domestic/domesticBookingResults.jsp"/>
			</div>
		    </td>
		</tr>
	    </table>
	</html:form>
         <c:if test="${not empty booking.id}">
            <script type="text/javascript">
                var href="${path}/domesticBooking.do?methodName=editBooking&bookingId=${booking.id}";
                $.colorbox({
                    iframe:true,
                    href:href,
                    width:"90%",
                    height:"100%",
                    title:"Edit Booking"
                 });
            </script>
        </c:if>
    </body>
</html>
<script type="text/javascript">
    function search(){
        $('#methodName').val("search");
        $('#bookingForm').submit();
    }
    function resetAll(){

    }
    $(document).ready(function() {
        $("#bookingForm").submit(function() {
            window.parent.showPreloading();
        });
        window.parent.closePreloading();
    });
function showPreview(fileName){
    var title = "Quote Rate";
    var url = path+"/servlet/FileViewerServlet?fileName="+fileName;
    window.parent.showLightBox(title,url);
}

function createPreview(id){
    var url = $("#bookingForm").attr("action");
    var params = "methodName=preview";
    params += "&bookingId=" + id;
    ajaxCall(url, {
	data: params,
	preloading: true,
	success: "showPreview",
	async: false
    });
}
function showNotes(url){
     $.colorbox({
            iframe:true,
            href:url,
            width:"70%",
            height:"80%",
            title:"Notes"

     });
}
function trackBol(url){
    window.open(url);
}
function editBooking(id){
    var href=path+"/domesticBooking.do?methodName=editBooking&bookingId="+id;
        $.colorbox({
            iframe:true,
            href:href,
            width:"90%",
            height:"100%",
            title:"Edit Booking"

     });
}
</script>
