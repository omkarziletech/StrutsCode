<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    <tr class="tableHeadingNew"><td>Collector</td></tr>
    <tr>
        <td>
            <div id="displayTableDiv" class="scrolldisplaytable"
		 style="border: thin; overflow: auto;width: 100%;height: 300px;">
                <display:table id="collector" list="${collectorList}" class="displaytagstyleNew" style="width:100%">
                    <display:column title="Login Name" property="loginName"/>
                    <display:column title="First Name" property="firstName"/>
                    <display:column title="Last Name" property="lastName"/>
                    <display:column title="Email" property="email"/>
                    <display:column title="Telephone" property="telephone"/>
                    <display:column title="User Created On" property="userCreatedDate" format="{0,date,MM/dd/yyyy}"/>
                    <display:column title="TradingPartner Range">
                        <c:set var="range" value=""/>
			<c:set var="applyToConsigneeOnlyAccounts" value="false"/>
                        <c:forEach var="collectorTradingPartner" items="${collector.collectorTradingPartners}">
			    <c:choose>
				<c:when test="${collectorTradingPartner.applyToConsigneeOnlyAccounts}">
				    Applied to Consignee Only Accounts
				    <c:set var="applyToConsigneeOnlyAccounts" value="true"/>
				</c:when>
				<c:otherwise> 
				    <c:set var="range" value="${range},${collectorTradingPartner.startRange}-${collectorTradingPartner.endRange}"/>
				</c:otherwise>
			    </c:choose>
                        </c:forEach>
			<c:if test="${applyToConsigneeOnlyAccounts && not empty range}">
			    <br/>
			</c:if>
                        <c:out value="${fn:substring(range,1,fn:length(range))}"/>
                    </display:column>
                    <display:column title="Action">
                        <img id="action${collector.userId}" src="${path}/img/icons/pubserv.gif" border="0" alt="Assign TradingPartner" onmouseout="tooltip.hide();"
                             onmouseover="tooltip.show('<strong>Assign TradingPartner</strong>',null,event);" onclick="showAssignWindow('${collector.userId}')"/>
                        <c:if test="${!empty collector.collectorTradingPartners}">
                            <img id="action${collector.userId}" src="${path}/img/icons/remove.gif" border="0" alt="Remove Assign" onmouseout="tooltip.hide();"
				 onmouseover="tooltip.show('<strong>Remove Assign</strong>',null,event);" onclick="removeCollector('${collector.userId}')"/>
                        </c:if>
                    </display:column>
                </display:table>
            </div>
        </td>
    </tr>
</table>
