<%-- 
    Document   : tradingPartner
    Created on : Mar 18, 2014, 7:44:15 PM
    Author     : Lakshmi Narayanan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="bold">
    <span class="blue-70">${values[0]}</span><%--account name--%>
    <span class="red-90"><-->${values[1]}</span><%--account number--%>
    <span class="red">, ${values[2]}</span><%--account type--%>
    <c:if test="${fn:containsIgnoreCase(values[2], 'V') and not empty values[3]}">
	<span class="red">- ${values[3]}</span><%--sub type--%>
    </c:if>
    <c:choose>
	<c:when test="${fn:containsIgnoreCase(values[2], 'V') and (values[3] eq 'Steamship Line' or values[3] eq 'Air Line') and not empty values[6]}">
	    <span class="green">, ${values[6]}</span><%--bluescreen ssline number--%>
	</c:when>
	<c:when test="${fn:containsIgnoreCase(values[2], 'V') and values[3] eq 'Forwarder' and not empty values[4]}">
	    <span class="green">, ${values[4]}</span><%--bluescreen forwarder number--%>
	</c:when>
	<c:when test="${fn:containsIgnoreCase(values[2], 'S') and not empty values[4]}">
	    <span class="green">, ${values[4]}</span><%--bluescreen shipper/forwarder number--%>
	</c:when>
	<c:when test="${fn:containsIgnoreCase(values[2], 'C') and not empty values[5]}">
	    <span class="green">, ${values[5]}</span><%--bluescreen consignee number--%>
	</c:when>
	<c:when test="${not empty values[4]}">
	    <span class="green">, ${values[4]}</span><%--bluescreen shipper/forwarder number--%>
	</c:when>
    </c:choose>
    <c:if test="${not empty values[7]}">
	<span class="olive">, SP=${values[7]}</span><%--sales person--%>
    </c:if>
    <c:if test="${values[8] eq 'Master'}">
	<span class="green">, MASTER</span><%--Master--%>
    </c:if>
    <c:if test="${values[9] eq 'Y'}">
	<span class="red">, DISABLED</span><%--disabled status--%>
    </c:if>
        <c:if test="${not empty sessionScope.importNavigation and not empty values[15] && values[15] eq 'Y'}">
            <span class="green" style="font-size: 130%;">, $</span>
        </c:if> <%--import credit--%>
</div>
<div>
    <span class="grey font-8px">
	${values[10]}<%--street address--%>
	<c:if test="${not empty values[11]}">
	    , ${values[11]}<%--city--%>
	</c:if>
	<c:if test="${not empty values[12]}">
	    , ${values[12]}<%--state--%>
	</c:if>
	<c:if test="${not empty values[13]}">
	    , ${values[13]}<%--country--%>
	</c:if>
	<c:if test="${not empty values[14]}">
	    , ${values[14]}<%--zip--%>
	</c:if>
    </span>
</div>