<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:td styleClass="td">Agent Info</cong:td>
<cong:td>
    <cong:autocompletor id="agentInfo" name="rtdAgentAcct" template="two" fields="agentInfo" width="200" shouldMatch="true"
                        query="TRADING_AGENT" styleClass="text" params="${lclBookingForm.unlocationCode}"
                        container="NULL" value="${lclBooking.rtdAgentAcct.accountno}"/>
    <%-- <cong:text styleClass="text"  id="agentInfo" name="agentInfo" />--%>
</cong:td>
