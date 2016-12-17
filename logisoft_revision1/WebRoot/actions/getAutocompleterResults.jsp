<%@include file="/import.jsp" %>
<%@include  file="/taglib.jsp" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@page import="com.logiware.utils.QueryUtil"%>
<%
            BaseHibernateDAO baseHibernateDAO = new BaseHibernateDAO();
            String query = request.getParameter("query");
            String param = request.getParameter("value");
            param = param == null ? "" : param;
            if (param != "") {
                param = param.substring(0, param.indexOf("?q="));
                param = param.replace(":amp:", "&");
            }
            String[] array = {"PICKUP","PICKUPFORQUOTE","WAREHOUSE","DETAILWHSE","IMPORTWAREHOUSE","CFS_WAREHOUSE",
            "MAIN_SCREEN_TERMINAL","ISSUEING_TERMINAL","CONCAT_TERM_BL","TERM_BL","ISSUING_TERMINAL","IMPORTTERMINAL",
            "ORIGINTRM","CHARGE_CODE","CHARGE_COST_CODE","COST_CODE","INVOICE_CHARGE_CODE","PORTCHARGECODE","PACKAGE_TYPE",
            "PACKAGE_TYPE1","PACK_TYPE","PACKAGE_TYPE_PLURAL","SALES_PERSON","BL_PERSON","COMMODITY_TYPE_NAME",
            "COMMODITY_TYPE_NAME_BOOK","COMMODITY_TYPE_CODE","COMMODITYTYPEFORRATES","COMMODITYCODEFORRATES",
            "QUOTECOMMODITYTYPEFORRATES","QUOTECOMMODITYCODEFORRATES","CITY_NAME","CONCAT_CITY","GENERICCODE_BY_CODETYPEID",
            "CONTACT","CONTACTFORACCT","SSCHEDULENO","SSCHEDULENAME","WHSCODE","UNIT_TYPE","COUNTRY","SEARCH_BY_COUNTRY",
            "ECULINE_UNIT","ECULINE_REFNO","ECULINE_INVOICE_NO","CONCAT_HOTCODE_TYPE","HOTCODE_TYPE","DELWHSE","CONCAT_CITY_NAME",
            "CONCAT_COUNTRY_NAME","GENERICCODE_BY_IMONUMBER","EXP_UNIT_SEARCH","UNIT_NO","EXP_BOOKING_NO_SEARCH","COMMODITY_PACKAGE_TYPE"};
            List l = new ArrayList();
            l.addAll(Arrays.asList(array));
            if(!l.contains(query)){
                param = param.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
            }
            String queryString = null;
            if (CommonUtils.isNotEmpty(query)) {
                queryString = QueryUtil.getQuery(query, param, request.getParameterMap());
            } else {
                String column = request.getParameter("column");
                String columns = request.getParameter("columns");
                columns = CommonUtils.isNotEmpty(columns) ? column + "," + columns : column;
                String table = request.getParameter("table");
                queryString = "SELECT DISTINCT " + columns + " FROM " + table + " WHERE " + column + " LIKE '" + param + "%' limit 10 ";
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
                    <jsp:include page="template/${template}.jsp"/>
                </li>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</c:if>