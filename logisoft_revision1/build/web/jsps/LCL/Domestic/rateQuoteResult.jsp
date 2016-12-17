<%-- 
    Document   : rateQuoteResult
    Created on : Aug 1, 2013, 11:50:28 AM
    Author     : Shanmugam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%@include file="../init.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@include file="../colorBox.jsp" %>
    <%@include file="../../includes/baseResources.jsp" %>
    <%@include file="../../includes/resources.jsp" %>
    <%@include file="../../includes/jspVariables.jsp" %>
    <%@include file="../../../taglib.jsp" %>
    <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <script type="text/javascript">
		var path = "${path}";
    </script>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
        <html:form action="/rateQuote" name="rateQuoteForm"
                   styleId="rateQuoteForm" type="com.logiware.domestic.form.rateQuoteForm" scope="request" method="post">
            <input type="hidden" name="methodName" id="methodName"/>
            <input type="hidden" name="carrierName" id="carrierName"/>
            <input type="hidden" name="quoteId" id="quoteId" value="${quote.id}"/>
            <html:hidden property="bookingNumber" styleId="bookingNumber"/>
            <c:choose>
                <c:when test="${not empty quote}">
                    <cong:table styleClass="floatLeft" width="100%" cellpadding="3" cellspacing="3" border="1">
                        <cong:tr>
                            <cong:td styleClass="tableHeadingNew" colspan="12">
                                <cong:div styleClass="floatLeft">Quote&nbsp; &nbsp;</cong:div>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">Shipment Id</cong:td>
                            <cong:td styleClass="textlabelsBold" align="left"><a style="color: red">${quote.shipmentId}</a></cong:td>
                            <cong:td styleClass="textlabelsBold">Origin:</cong:td>
                            <cong:td styleClass="textlabelsBold" align="left">${quote.originCity},${quote.originState},${quote.originZip}</cong:td>
                            <cong:td styleClass="textlabelsBold">Destination:</cong:td>
                            <cong:td styleClass="textlabelsBold" align="left">${quote.destinationCity},${quote.destinationState},${quote.destinationZip}</cong:td>
                            <cong:td styleClass="textlabelsBold">Distance:</cong:td>
                            <cong:td styleClass="textlabelsBold">${quote.miles}</cong:td>
                            <cong:td styleClass="textlabelsBold">Cubic Feet</cong:td>
                            <cong:td styleClass="textlabelsBold">${quote.cube}</cong:td>
                            <cong:td styleClass="textlabelsBold">Ship Date:</cong:td>
                            <cong:td styleClass="textlabelsBold">${quote.shipDate} </cong:td>
                        </cong:tr>
                    </cong:table>
                    <cong:table width="100%" border="1" styleClass="tableHeading2-small" style="border-collapse: collapse; border: 1px solid #dcdcdc">
                        <cong:tr>
                            <cong:th>Action</cong:th>
                            <cong:th>Carrier Name</cong:th>
                            <cong:th>Direct/InterLine</cong:th>
                            <cong:th>Estimated Days/Delivery Date</cong:th>
                            <cong:th>Type</cong:th>
                            <cong:th>Line Hual</cong:th>
                            <cong:th>Fuel Charge</cong:th>
                            <cong:th>Extra Charges</cong:th>
                            <cong:th>Final Charge</cong:th>
                        </cong:tr>
                        <c:forEach items="${carrier}" var="carrier">
                            <c:choose>
                                <c:when test="${zebra=='odd'}">
                                    <c:set var="zebra" value="even"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="odd"/>
                                </c:otherwise>
                            </c:choose>
                            <cong:tr styleClass="${zebra}">
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${not empty quote.bookedOn}">
                                            <c:choose>
                                                <c:when test="${carrier.rated}">
                                                    <input type="radio" name="carrierRadio" id="carrierRadio" value="${carrier.carrierName}" checked disabled="true">
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="radio" name="carrierRadio" id="carrierRadio" value="${carrier.carrierName}" disabled="true">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${carrier.rated}">
                                               <input type="radio" name="carrierRadio" id="carrierRadio" value="${carrier.carrierName}" checked >
                                         </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="carrierRadio" id="carrierRadio" value="${carrier.carrierName}">
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                                <cong:td>${carrier.carrierName}</cong:td>
                                <cong:td>${carrier.directInterline}</cong:td>
                                <cong:td>${carrier.estimatedDays}</cong:td>
                                <cong:td>${carrier.type}</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${loginuser.role.roleDesc != 'Admin' && loginuser.role.roleDesc != 'SALES'}">
                                            <span onmouseover="tooltip.showSmall('<strong>${carrier.lineHual}</strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.lineHual}
                                            </span>
                                        </c:when>
                                        <c:when test="${carrier.rateType == 'P'}">
                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$${carrier.lineAmount}</a><br><a style=\'color: green\'>MarkUp(${carrier.lineMarkUp}%):</a>  <a style=\'color: blue\'>$${carrier.lineMarkUpCharge}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${carrier.lineHual}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.lineHual}
                                            </span>
                                        </c:when>
                                        <c:when test="${carrier.rateType == 'F'}">
                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$${carrier.lineAmount}</a><br><a style=\'color: green\'>Flat Fee:</a>  <a style=\'color: blue\'>$${carrier.flatFee}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${carrier.lineHual}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.lineHual}
                                            </span>
                                        </c:when>
                                        <c:when test="${carrier.rateType == 'MP' || carrier.rateType == 'MF'}">
                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Minimum Amount :</a> <a style=\'color: blue\'>$${carrier.lineHual}</a></strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.lineHual}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span onmouseover="tooltip.showSmall('<strong>${carrier.lineHual}</strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.lineHual}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>

                                <cong:td>
                                    <c:choose>
                                        <c:when test="${loginuser.role.roleDesc != 'Admin' && loginuser.role.roleDesc != 'SALES'}">
                                            <span onmouseover="tooltip.showSmall('<strong>${carrier.fuelCharge}</strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.fuelCharge}
                                            </span>
                                        </c:when>
                                        <c:when test="${carrier.rateType == 'P'}">
                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base FuelCharge</a> : <a style=\'color: blue\'>$${carrier.fuelAmount}</a><br><a style=\'color: green\'>MarkUp(${carrier.fuelMarkUp}%):</a>  <a style=\'color: blue\'>$${carrier.fuelMarkUpCharge}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${carrier.fuelCharge}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.fuelCharge}
                                            </span>
                                        </c:when>
                                        <c:when test="${carrier.rateType == 'F'}">
                                            <span onmouseover="tooltip.showSmall('<strong>${carrier.fuelCharge}</strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.fuelCharge}
                                            </span>
                                        </c:when>
                                         <c:when test="${carrier.rateType == 'MP' || carrier.rateType == 'MF'}">
                                                ${carrier.fuelCharge}
                                        </c:when>
                                        <c:otherwise>
                                            <span onmouseover="tooltip.showSmall('<strong>${carrier.fuelCharge}</strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.fuelCharge}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                                <cong:td>
                                    ${carrier.extraCharge}
                                </cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${loginuser.role.roleDesc != 'Admin' && loginuser.role.roleDesc != 'SALES'}">
                                            <span onmouseover="tooltip.showSmall('<strong>${carrier.finalCharge}</strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.finalCharge}
                                            </span>
                                        </c:when>
                                        <c:when test="${carrier.rateType == 'P'  || carrier.rateType == 'F'}">
                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Summary</a><br>-----------------------------<br><a style=\'color: green\'>LineHual</a> : <a style=\'color: blue\'>$${carrier.lineHual}</a><br><a style=\'color: green\'>Fuel Charge</a> : <a style=\'color: blue\'>$${carrier.fuelCharge}</a><br>  <a style=\'color: green\'>Extra Charge</a> : <a style=\'color: blue\'>$${carrier.extraCharge}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${carrier.finalCharge}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.finalCharge}
                                            </span>
                                        </c:when>
                                        <c:when test="${carrier.rateType == 'MP'}">
                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Summary</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$${carrier.lineAmount}</a><br><a style=\'color: green\'>MarkUp(${carrier.lineMarkUp}%):</a>  <a style=\'color: blue\'>$${carrier.lineMarkUpCharge}</a><br><a style=\'color: green\'>Base FuelCharge</a> : <a style=\'color: blue\'>$${carrier.fuelAmount}</a><br><a style=\'color: green\'>MarkUp(${carrier.fuelMarkUp}%):</a>  <a style=\'color: blue\'>$${carrier.fuelMarkUpCharge}</a><br><a style=\'color: green\'>Extra Charge</a> : <a style=\'color: blue\'>$${carrier.extraAmount}</a><br>-----------------------------<br>   <a style=\'color: green\'>Min Amount </a> <a style=\'color: blue\'>$${carrier.minAmount} > total</a></strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.finalCharge}
                                            </span>
                                        </c:when>
                                        <c:when test="${carrier.rateType == 'MF'}">
                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Summary</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$${carrier.lineAmount}</a><br><a style=\'color: green\'>Flat Fee:</a>  <a style=\'color: blue\'>$${carrier.flatFee}<br><a style=\'color: green\'>FuelCharge</a> : <a style=\'color: blue\'>$${carrier.fuelAmount}</a><br><a style=\'color: green\'>Extra Charge</a> : <a style=\'color: blue\'>$${carrier.extraCharge}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${carrier.minAmount} > total</a></strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.finalCharge}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span onmouseover="tooltip.showSmall('<strong>${carrier.finalCharge}</strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                ${carrier.finalCharge}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                        </c:forEach>
                    </cong:table>

                    <cong:tr>
                        <cong:td>
                            <cong:button  label="Back" onclick="backToRate()" styleClass="button"/>
                            <c:if test="${empty quote.bookedOn}">
                                <cong:button  label="Save" onclick="saveQuote()" styleClass="button"/>
                                <cong:button  label="Book" onclick="openBookPopUp()" styleClass="button"/>
                            </c:if>
                            <cong:button  label="Preview" onclick="createPreview()" styleClass="button"/>
                        </cong:td>
                    </cong:tr>
                </c:when>
                <c:otherwise>
                    <cong:table styleClass="floatLeft" width="100%" cellpadding="3" cellspacing="3" border="1">
                        <cong:tr>
                            <cong:td styleClass="tableHeadingNew" colspan="8">
                                <cong:div styleClass="floatLeft">Quote&nbsp; &nbsp;</cong:div>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  colspan="8" style="font-size: 30;color:red">
                               ${error}
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                        <cong:td>
                            <cong:button  label="Back" onclick="backToRate()" styleClass="button"/>
                        </cong:td>
                    </cong:tr>
                    </cong:table>
                </c:otherwise>
            </c:choose>

        </html:form>
    </body>
</html>
<script type="text/javascript">
    var bookingNumber;
    function backToRate(){
        $('#methodName').val("searchQuote");
        $('#rateQuoteForm').submit();
    }
    function bookQuote(bookingNo,carrierName){
        bookingNumber = bookingNo;
        var quoteId = $('#quoteId').val();
        var url = $("#rateQuoteForm").attr("action");
        var params = "methodName=bookQuote";
         params += "&carrierName=" + carrierName;
        params += "&bookingNumber=" + bookingNo;
        params += "&quoteId=" + quoteId;
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "showMessage",
            async: false
        });
    }
    function showMessage(result) {
        if(result.indexOf("available========") >- 1){
            var itemId = result.replace("available========","");
            window.parent.gotoDomestBookingScreens(bookingNumber,itemId);
        }
    }
    function editBookPopUp(id){
        alert(id);
        var href=path+"/domesticBooking.do?methodName=editBooking&bookingId="+id;
        $.colorbox({
            iframe:true,
            href:href,
            width:"90%",
            height:"100%",
            title:"Edit Booking"
         });
    }
    function openBookPopUp(){
        if($("input[type=radio][name=carrierRadio]:checked").length > 0){
            var carrier = $('input:radio[name=carrierRadio]:checked').val();
            var quoteId = $('#quoteId').val();
            var href=path+"/rateQuote.do?methodName=bookQuotePopUp&carrierName="+carrier+"&quoteId="+quoteId;
            $.colorbox({
                iframe:true,
                href:href,
                width:"50%",
                height:"50%",
                title:"Add Reference Number"

            });
        }else{
            sampleAlert("Please select carrier.");
        }
    }
    function saveQuote(){
        if($("input[type=radio][name=carrierRadio]:checked").length > 0){
            var carrier = $('input:radio[name=carrierRadio]:checked').val();
             $('#carrierName').val(carrier);
             $('#methodName').val("saveQuote");
             $('#rateQuoteForm').submit();
        }else{
            sampleAlert("Please select carrier.");
        }
    }
    function closeBookPopUp(){
        document.getElementById('bookQuoteDiv').style.display='none';
        closePopUp();
    }
   
    function showPreview(fileName){
        var title = "Quote Rate";
        var url = path+"/servlet/FileViewerServlet?fileName="+fileName;
        window.parent.showLightBox(title,url);
    }
    $(document).ready(function() {
        $("#rateQuoteForm").submit(function() {
            window.parent.showPreloading();
        });
        window.parent.closePreloading();
    });
    function createPreview(){
        var carrier = "";
        if($("input[type=radio][name=carrierRadio]:checked").length > 0){
            carrier = $('input:radio[name=carrierRadio]:checked').val();
        }
        var quoteId = $('#quoteId').val();
        var url = $("#rateQuoteForm").attr("action");
        var params = "methodName=preview";
        params += "&carrierName=" + carrier;
        params += "&quoteId=" + quoteId;
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "showPreview",
            async: false
        });
    }
</script>
