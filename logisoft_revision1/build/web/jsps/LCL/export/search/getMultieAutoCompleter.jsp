<%-- 
    Document   : getMultieAutoCompleter
    Created on : Jul 8, 2015, 12:23:22 PM
    Author     : aravindhan.v
--%>
<%@include file="/import.jsp" %>
<%@include  file="/taglib.jsp" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@page import="com.logiware.utils.QueryUtil"%>
<%@ taglib uri="http://cong.logiwareinc.com/string" prefix="string"%>

<%
    BaseHibernateDAO baseHibernateDAO = new BaseHibernateDAO();
    String query = request.getParameter("query");
    String param = request.getParameter("value");
    param = param == null ? "" : param;
    if (param != "") {
        param = param.substring(0, param.indexOf("?q="));
        param = param.replace(":amp:", "&");
    }
    if (param.lastIndexOf(",") != -1 && param.lastIndexOf(",") <= 11) {
        param = param.substring(param.lastIndexOf(",") + 1);
    }
    String queryString = null;
    if (CommonUtils.isNotEmpty(query)) {
        queryString = QueryUtil.getQuery(query, param, request.getParameterMap());
    }
    String template = request.getParameter("template");
    List list = baseHibernateDAO.executeSQLQuery(queryString);
    if (CommonUtils.isNotEmpty(list)) {
        request.setAttribute("list", list);
        Object row = list.get(0);
        request.setAttribute("isString", row instanceof String);
        if (template == null || template.trim().isEmpty()) {
            template = "default";
        }
        request.setAttribute("template", template);
    }

%>
<c:if test="${not empty list}">
    <c:choose>
        <c:when test="${isString}">
            <c:forEach var="fieldValue" items="${list}">
                <li id="${fieldValue}">
                    <font color="#093ba1"> ${fieldValue} </font>
                </li>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach var="row" items="${list}">
                <c:set var="fieldValues" value=""/>
                <c:forEach var="col" items="${row}">
                    <c:set var="fieldValues" value="${fieldValues}${col}^"/>
                </c:forEach>
                <li id="${fieldValues}">
                    <c:set var="values" value="${row}" scope="session"/>
                    <jsp:include page="/actions/template/${template}.jsp"/>
                </li>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</c:if>

