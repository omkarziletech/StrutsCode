<%-- 
    Document   : getAutocompleterResults
    Created on : Feb 12, 2013, 7:19:58 PM
    Author     : Lakshmi Naryanan
--%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.hibernate.SQLQuery"%>
<%@page import="java.util.List"%>
<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="com.logiware.fcl.utils.QueryUtils"%>
<%
    String query = request.getParameter("query");
    String param = request.getParameter("?q");
    List list = QueryUtils.getResults(query, param, request.getParameterMap());
    request.setAttribute("list", list);
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://cong.logiwareinc.com/string" prefix="string"%>
<c:if test="${not empty list}">
    <c:set var="isArray" value="<%= list.get(0).getClass().isArray()%>" />
    <c:forEach var="row" items="${list}">
        <c:set var="fieldValues" value=""/>
        <c:choose>
            <c:when test="${isArray}">
                <c:forEach var="col" items="${row}" varStatus="index">
                    <c:if test="${string:in(index.count, param.fieldIndex)}">
                        <c:set var="fieldValues" value="${fieldValues}${col}^"/>
                    </c:if>
                </c:forEach>            
                <li id="${fieldValues}"> 
                    <c:set var="values" value="${row}" scope="request"/>
                    <c:import url="/autocompleter/fcl/templates/${param.template}.jsp"/>
                </li>
            </c:when>
            <c:otherwise>
                <c:set var="fieldValues" value="${row}^"/>
                <li id="${row}"> 
                    <div class="bold uppercase">
                        <span class="blue-70">${row}</span>
                    </div>
                </li>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</c:if>