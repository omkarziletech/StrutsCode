<%--
    Document   : tradingPartner
    Created on : Oct 28, 2014, 1:02:14 PM
    Author     : Mohanapriya
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="bold">
    <span class="blue-70">
        ${values[0]}<%--account name--%>
        <c:if test="${values[30] != 0 && not empty values[30]}">
            <img src="${path}/img/icons/e_contents_view.gif" width="16" height="16"/> <%--bluescreen notes--%>
        </c:if>
        <c:choose>
            <c:when test="${values[31] != 0 && values[32] == 0 && not empty values[31]}">
                <img src="${path}/img/icons/contact_person.png" width="16" height="16"/><%--Tp contact details without code F--%>
                ${values[31]}
            </c:when>
            <c:when test="${values[32] != 0 && not empty values[32]}">
                <img src="${path}/img/icons/contact_person_green.png" width="16" height="16"/><%--Tp contact details with code F--%>
                ${values[31]}
            </c:when>
        </c:choose><-->
    </span>
    <span class="red-90">
        ${values[1]} <%--account number--%>
    </span>
    <span class="red">
        ${values[2]} <%--account type--%>
    </span>
    <c:if test="${fn:containsIgnoreCase(values[2], 'V') && not empty values[3]}">
        <span class="red">
	   - (${values[3]}) <%--sub type--%>
        </span>
    </c:if>
    <c:choose>
        <c:when test="${fn:containsIgnoreCase(values[2], 'V') && not empty values[6] && (values[3] == 'Steamship Line' || values[3] == 'Air Line')}">
	    ,<span class="green">
                ${values[6]} <%--bluescreen ssline number--%>
            </span>
        </c:when>
        <c:when test="${fn:containsIgnoreCase(values[2], 'V') && not empty values[4] && values[3] == 'Forwarder'}">
	    ,<span class="blue">
                ${values[4]} <%--bluescreen forwarder number--%>
            </span>
        </c:when>
        <c:when test="${fn:containsIgnoreCase(values[2], 'S') && not empty values[4]}">
	    ,<span class="blue">
                ${values[4]} <%--bluescreen shipper number--%>
            </span>
        </c:when>
        <c:when test="${fn:containsIgnoreCase(values[2], 'C') && not empty values[5]}">
	    ,<span class="blue">
                ${values[5]} <%--bluescreen consignee number--%>
            </span>
        </c:when>
        <c:when test="${not empty values[4]}">
	    ,<span class="blue">
                ${values[4]} <%--bluescreen account number--%>
            </span>
        </c:when>
    </c:choose>
    <c:if test="${not empty values[7]}">
	,<span class="green">
	    SP=${values[7]} <%--sales person--%>
        </span>
    </c:if>
    <c:if test="${values[8] == 'Y'}">
	,<span class="red">
	    DISABLED <%--disabled status--%>
        </span>
    </c:if>
</div>
<div>
    <span class="grey font-8px">
        ${values[9]} <%--address--%>
        <c:if test="${not empty values[10]}">
	    ,${values[10]} <%--city--%>
        </c:if>
        <c:if test="${not empty values[11]}">
	    ,${values[11]} <%--state--%>
        </c:if>
        <c:if test="${not empty values[12]}">
	    ,${values[12]} <%--country--%>
        </c:if>
        <c:if test="${not empty values[13]}">
	    ,${values[13]} <%--zip--%>
        </c:if>
    </span>
</div>