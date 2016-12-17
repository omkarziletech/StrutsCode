<%--
    Document   : getAutocompleterSuggestions
    Created on : Mar 28, 2012, 5:26:58 PM
    Author     : Lakshmi Naryanan
--%>
<%@page import="java.util.Arrays"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://cong.logiwareinc.com/string" prefix="string"%>
<%@page import="java.util.List"%>
<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="com.logiware.accounting.utils.QueryUtils"%>
<%
    String query = request.getParameter("query");
    String param = request.getParameter("?q");
    String fieldIndex = request.getParameter("fieldIndex");
    if ((query.contains("OPEN_CUSTOMER") || query.contains("OPEN_AR_CUSTOMER")) && CommonUtils.isEmpty(param)) {
        query = "ALL_" + query;
    } else if (query.equalsIgnoreCase("AP_INVOICE") || query.equalsIgnoreCase("EDI_INVOICE")) {
        String vendorNumber = request.getParameter("param1");
        if (CommonUtils.isNotEmpty(vendorNumber)) {
            param = param.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
        }
    } else {
        if (query.equalsIgnoreCase("RECONCILE") || query.equalsIgnoreCase("GL_ACCOUNT")) {
            param = param.replace("-", "").replaceFirst("(.{2})(?!$)", "$1-").replaceFirst("(.{7})(?!$)", "$1-");
        }
    }
    List list = QueryUtils.getResults(query, param, request.getParameterMap());
    if (CommonUtils.isNotEmpty(list)) {
        request.setAttribute("list", list);
        Object row = list.get(0);
        request.setAttribute("isString", row instanceof String);
        String template = request.getParameter("template");
        if (template == null || template.trim().isEmpty()) {
            template = "default";
        }
        request.setAttribute("template", template);
        request.setAttribute("fieldIndex", fieldIndex);
        String tabName = request.getParameter("tabName");
        if (query.equalsIgnoreCase("GL_ACCOUNT") && "GL_REPORT".equalsIgnoreCase(tabName) && param.length() >= 7) {
            String allSuffix = StringUtils.substring(param, 0, 7) + "-ALL";
            Object[] row1 = new Object[]{allSuffix, "All Suffixes"};
            list.add(0, row1);
        }
    }
%>
<c:if test="${not empty list}">
    <c:choose>
        <c:when test="${isString}">
            <c:forEach var="col" items="${list}">
                <li id="${col}">
                    <font color="#093ba1"> ${col} </font>
                </li>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach var="row" items="${list}">
                <c:set var="fieldValues" value=""/>
                <c:forEach var="col" items="${row}" varStatus="index">
                    <c:if test="${string:in(index.count,fieldIndex)}">
                        <c:set var="fieldValues" value="${fieldValues}${col}^"/>
                    </c:if>
                </c:forEach>
                <li id="${fieldValues}">
                    <c:set var="values" value="${row}" scope="session"/>
                    <c:import url="/autocompleter/templates/${template}.jsp"/>
                </li>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</c:if>