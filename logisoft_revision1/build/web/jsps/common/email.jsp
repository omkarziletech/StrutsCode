<%-- 
    Document   : email
    Created on : Jul 2, 2012, 9:41:01 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Email</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
            <script type="text/javascript">
                var path = "${path}";
        </script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-te.min.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery-te.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
    </head>
    <body>
        <c:if test="${not empty param.toAddress}">
            <c:set var="toAddress" value="${param.toAddress}"/>
        </c:if>
        <c:if test="${not empty param.ccAddress}">
            <c:set var="ccAddress" value="${param.ccAddress}"/>
        </c:if>
        <c:if test="${not empty param.bccAddress}">
            <c:set var="bccAddress" value="${param.bccAddress}"/>
        </c:if>
        <c:if test="${not empty param.subject}">
            <c:set var="subject" value="${param.subject}"/>
        </c:if>
        <c:if test="${not empty param.body}">
            <c:set var="body" value="${param.body}"/>
        </c:if>
        <c:if test="${not empty param.action}">
            <c:set var="action" value="${param.action}"/>
        </c:if>
        <div id="body-container">
            <%@include file="../preloader.jsp"%>
            <form name="emailForm" id="emailForm" action="${path}/${action}" method="post">
                <table class="table">
                    <thead>
                        <tr>
                            <th colspan="2">Compose Email</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="label width-60px">User</td>
                            <td>
                                <input type="text" name="loginName" id="loginName" style="width: 400px" class="float-left jqte"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label width-60px">To</td>
                            <td>
                                <input type="text" name="toAddress" id="toAddress" style="width: 400px"
                                       class="float-left jqte required" value="${toAddress}" validate="email:Please enter valid to address"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label width-60px">Cc</td>
                            <td>
                                <input type="text" name="ccAddress" id="ccAddress" style="width: 400px"
                                       class="float-left jqte" value="${ccAddress}" validate="email:Please enter valid cc address:optional"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label width-60px">Bcc</td>
                            <td>
                                <input type="text" name="bccAddress" id="bccAddress" style="width: 400px"
                                       class="float-left jqte" value="${bccAddress}" validate="email:Please enter valid bcc address:optional"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label width-60px">Subject</td>
                            <td>
                                <input type="text" name="subject" id="subject" style="width: 400px"
                                       class="float-left jqte required" value="${subject}" validate="subject:Please enter subject"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label width-60px">Body</td>
                            <td>
                                <textarea name="body" id="body">${body}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                <input type="button" value="Send" class="button" id="send"/>
                                <input type="button" value="Cancel" class="button" id="cancel"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <script type="text/javascript">
                function closeEmail() {
                    if (window.parent.Lightbox) {
                        window.parent.Lightbox.close();
                    } else if (window.parent.GB_CURRENT) {
                        window.parent.GB_hide();
                    } else {
                        window.close();
                    }
                }

                function onAjaxSuccess(data) {
                    if ($.trim(data) === "disputed") {
                        window.parent.changeDisputeIconColor("disputed", "${param.id}", "${param.invoice}");
                        window.parent.Lightbox.close();
                    } else if ($.trim(data) === "Email is sent successfully.") {
                        window.parent.displayMessage(data);
                        window.parent.Lightbox.close();
                    } else if (data.indexOf("Cannot send email.") === 0) {
                        $.prompt(data);
                    } else {
                        closeEmail();
                    }
                }

                $(document).ready(function () {
                    $("#body").jqte();
                    $("#send").click(function () {
                        if ($("#emailForm").validate()) {
                            var url = $("#emailForm").attr("action");
                            var params = $("#emailForm").serialize();
                            ajaxCall(url, {
                                data: params,
                                preloading: true,
                                success: "onAjaxSuccess",
                                top: "10%",
                                left: "30%",
                                width: "700px",
                                height: "300px",
                                overflowy: "auto"
                            });
                        }
                    });
                    $("#cancel").click(function () {
                        closeEmail();
                    });
                    $("#loginName").initautocomplete({
                        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=EMAIL&template=email&fieldIndex=1,2&",
                        width: "480px",
                        otherFields: "toAddress",
                        resultsClass: "ac_results z-index",
                        resultPosition: "absolute",
                        scroll: true,
                        scrollHeight: 250,
                        append: true
                    });
                });
                </script>
            </form>
        </div>
    </body>
</html>
