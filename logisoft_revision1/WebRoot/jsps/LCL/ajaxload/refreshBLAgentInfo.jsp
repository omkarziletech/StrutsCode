<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <cong:td styleClass="td">  Agent Name</cong:td>
    <cong:td>
        <cong:autocompletor id="agentName" name="accountName" template="two" fields="agentNumber" callback=""
                            width="200" shouldMatch="true" query="TRADING_AGENT" styleClass="text-readonly" readOnly="true"
                            container="NULL" params="${lclBlForm.unlocationCode}" value="${lclBl.agentAcct.accountName}" />
        <%-- <cong:text title="" styleClass="text-readonly" id="agentName" name="agentName" value="${lclBookingForm.agentName}" readOnly="true"/>--%>
    </cong:td>
