<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    <tr class="tableHeadingNew"><td>AP Specialist</td></tr>
    <tr>
        <td>
            <div id="displayTableDiv" class="scrolldisplaytable"
		style="border: thin; overflow: auto;width: 100%;height: 300px;">
                <display:table id="apSpecialist" list="${apSpecialistList}" class="displaytagstyleNew" style="width:100%">
                    <display:column title="Login Name" property="loginName"/>
                    <display:column title="First Name" property="firstName"/>
                    <display:column title="Last Name" property="lastName"/>
                    <display:column title="Email" property="email"/>
                    <display:column title="Telephone" property="telephone"/>
                    <display:column title="User Created On" property="userCreatedDate" format="{0,date,MM/dd/yyyy}"/>
                    <display:column title="TradingPartner subTypes">
                        <c:set var="subTypes" value=""/>
                        <c:forEach var="apSpecialistTradingPartner" items="${apSpecialist.apSpecialistTradingPartners}">
                            <c:set var="subTypes" value="${subTypes},${apSpecialistTradingPartner.subType}"/>
                        </c:forEach>
                        <c:out value="${fn:substring(subTypes,1,fn:length(subTypes))}"/>
                    </display:column>
                    <display:column title="Action">
                        <img id="action${apSpecialist.userId}" src="${path}/img/icons/pubserv.gif" border="0" alt="Assign TradingPartner" onmouseout="tooltip.hide();"
                             onmouseover="tooltip.show('<strong>Assign TradingPartner</strong>',null,event);" onclick="showAssignWindow('${apSpecialist.userId}')" />
                        <c:if test="${!empty apSpecialist.apSpecialistTradingPartners}">
                            <img id="action${collector.userId}" src="${path}/img/icons/remove.gif" border="0" alt="Remove Assign" onmouseout="tooltip.hide();"
                                onmouseover="tooltip.show('<strong>Remove Assign</strong>',null,event);" onclick="removeApSpecialist('${apSpecialist.userId}')"/>
                        </c:if>
                    </display:column>
                </display:table>
            </div>
        </td>
    </tr>
</table>
