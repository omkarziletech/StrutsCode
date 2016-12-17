<%-- 
    Document   : add
    Created on : Jun 26, 2014, 4:14:02 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://cong.logiwareinc.com/dao" prefix="dao"%>
<%@taglib uri="http://cong.logiwareinc.com/gson" prefix="gson"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.switch.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.util.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/referencedata/genericCode.js"></script>
        <title>Generic Code</title>
    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
        <html:form action="/genericCode" type="com.logiware.referencedata.form.GenericCodeForm"
                   name="genericCodeForm" styleId="genericCodeForm" scope="request" method="post">
            <html:hidden property="action" styleId="action"/>
            <table class="table" style="margin: 0">
                <tr class="label">
                    <td class="width-200px">Code Type</td>
                    <td>
                        <input type="text" value="${genericCodeForm.codeType.description}" readonly class="textbox readonly width-220px"/>
                        <input type="hidden" name="genericCode.codetypeid" id="codetypeid" value="${genericCodeForm.codeType.codetypeid}"/>
                        <input type="button" class="button" value="Save" onclick="save();"/>
                        <input type="button" class="button" value="Reset" onclick="refresh('add');"/>
                        <input type="button" class="button" value="Go Back" onclick="goback();"/>
                    </td>
                </tr>
                <c:forEach var="gcl" items="${genericCodeForm.genericCodeLabels}" varStatus="status">
                    <tr>
                        <td class="label">${gcl.label}</td>
                        <td>
                            <c:choose>
                                <c:when test="${status.count eq 1 or gcl.fieldType eq 'text'}">
                                    <input type="text" name="genericCode.${gcl.fieldName}" id="${gcl.fieldId}"
                                           value="" class="${gcl.styleClass}" style="${gcl.style}"/>
                                </c:when>
                                <c:when test="${status.count eq 2 or gcl.fieldType eq 'textarea'}">
                                    <textarea name="genericCode.${gcl.fieldName}" id="${gcl.fieldId}"
                                              class="${gcl.styleClass} text-case-none" style="${gcl.style}"></textarea>
                                </c:when>
                                <c:when test="${gcl.fieldType eq 'select'}">
                                    <select name="genericCode.${gcl.fieldName}" id="${gcl.fieldId}"
                                            value="${genericCodeForm.genericCode[gcl.fieldName]}" class="${gcl.styleClass}" style="${gcl.style}">
                                        <c:choose>
                                            <c:when test="${fn:startsWith(gcl.options, 'QUERY=')}">
                                                <option value="">Select One</option>
                                                <c:forEach var="option" items="${dao:getList(fn:replace(gcl.options, 'QUERY=', ''))}" varStatus="options">
                                                    <option value="${option.value}">${option.label}</option>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="option" items="${gson:fromJson(gcl.options, 'java.util.List')}" varStatus="options">
                                                    <option value="${option.value}" 
                                                            <c:if test="${option.selected}">selected</c:if>>
                                                        ${option.label}
                                                    </option>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                </c:when>
                                <c:when test="${gcl.fieldType eq 'radio'}">
                                    <c:forEach var="option" items="${gson:fromJson(gcl.options, 'java.util.List')}" varStatus="options">
                                        <input type="radio" name="genericCode.${gcl.fieldName}" id="${gcl.fieldId}${options.count}" 
                                               <c:if test="${option.selected}">checked</c:if>  value="${option.value}"/>
                                        <label class="label" for="${gcl.fieldId}${options.count}">${option.label}</label>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </html:form>
    </body>
    <script type="text/javascript">
        $(document).ready(function() {
            <c:forEach var="gcl" items="${genericCodeForm.genericCodeLabels}" varStatus="status">
                <c:if test="${not empty gcl.javascript}">${gcl.javascript}</c:if>
            </c:forEach>
        });
    </script>
</html>
