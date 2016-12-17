<%-- 
    Document   : moreInfo
    Created on : Oct 14, 2013, 4:44:26 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>More Info</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript">
            var path = "/" + window.location.pathname.split('/')[1];

            function showMessage(message) {
                $(".message").html(message);
            }

            function showReference() {
                if($("#manual").is(":checked")) {
                    $("#referenceText").val($("#referenceSelect").val()).show();
                    $("#referenceSelect").hide();
                }else{
                    $("#referenceText").hide();
                    $("#referenceSelect").show();
                }
            }
            function updateCustomerReference() {
                $.prompt("Do you want to the update the customer reference?", {
                    buttons: {
                        Yes: true,
                        No: false
                    },
                    callback: function (v) {
                        if (v) {
                            var url = path + "/arInquiry.do";
                            ajaxCall(url, {
                                data: {
                                    action: "updateInvoice",
                                    id: $("#id").val(),
                                    customerReference: $($("#manual").is(":checked") ? "#referenceText" : "#referenceSelect").val()
                                },
                                preloading: true,
                                success: "showMessage",
                                async: false
                            });
                        }
                    }
                });
            }
        </script>
        <style type="text/css">
            .display-table tr:nth-child(even) {
                background: #D1E6EE;
            }
        </style>
    </head>
    <body>
        <%@include file="../../../../jsps/preloader.jsp"%>
        <div class="message align-left italic">${message}</div>
        <div class="result-container">
            <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
                <tbody class="label">
                    <tr>
                        <td class="align-right">BL #&nbsp;&nbsp;</td>
                        <td>${ar.blNumber}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Invoice #&nbsp;&nbsp;</td>
                        <td>${ar.invoiceNumber}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Vessel #&nbsp;&nbsp;</td>
                        <td>${ar.vesselNumber}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Voyage #&nbsp;&nbsp;</td>
                        <td>${ar.voyageNumber}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Master BL&nbsp;&nbsp;</td>
                        <td>${ar.masterBl}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Sub House BL&nbsp;&nbsp;</td>
                        <td>${ar.subHouseBl}</td>
                    </tr>
                    <c:if test="${fn:startsWith(ar.blNumber, 'IMP-')}">
                        <tr>
                            <td class="align-right">AMS House BL&nbsp;&nbsp;</td>
                            <td>${ar.amsHouseBl}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td class="align-right">BL Terms&nbsp;&nbsp;</td>
                        <td>${ar.blTerms}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Container #&nbsp;&nbsp;</td>
                        <td>${ar.containerNumber}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Customer Reference&nbsp;&nbsp;</td>
                        <td>
                            <select id="referenceSelect" class="dropdown">
                                <option id="referenceDefault" value="${ar.reference}" selected>${ar.reference}</option>
                                <c:if test="${not empty ar.masterBl}">
                                    <option value="${ar.masterBl}">${ar.masterBl}</option>
                                </c:if>
                                <c:if test="${not empty ar.subHouseBl}">
                                    <option value="${ar.subHouseBl}">${ar.subHouseBl}</option>
                                </c:if>
                                <c:if test="${fn:startsWith(ar.blNumber, 'IMP-') and not empty ar.amsHouseBl}">
                                    <option value="${ar.amsHouseBl}">${ar.amsHouseBl}</option>
                                </c:if>
                                <c:if test="${not empty ar.containerNumber}">
                                    <option value="${ar.containerNumber}">${ar.containerNumber}</option>
                                </c:if>
                            </select>
                            <input type="text" id="referenceText" class="textbox" style="display: none;"/>
                            <input type="checkbox" title="Check to enter Manually" id="manual" onclick="showReference();"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="align-center">
                            <input type="hidden" id="id" value="${ar.id}"/>
                            <input type="button" value="Update" class="button" onclick="updateCustomerReference()"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </body>
</html>