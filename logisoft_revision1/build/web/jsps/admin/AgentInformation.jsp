<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty agentInformation}">
     <table width="60%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
        <tr>
            <td class="tableHeadingNew">Agent Destination<td>
        </tr>
        <tr>
            <td>
                <table class="dataTable" id="printertable">
                    <thead>
                        <tr>
                            <th>Country</th>
                            <th>City</th>
                            <th>UNLOC</th>
                            <th>ECI Port Code</th>
                            <th>Sched K</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${agentInformation}" var="agentInfo">
                            <c:choose>
                                <c:when test="${rowStyle eq 'oddStyle'}">
                                    <c:set var="rowStyle" value="evenStyle"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="rowStyle" value="oddStyle"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${rowStyle}">
                                <td>${agentInfo.portId.countryName}</td>
                                <td>${agentInfo.portId.portname}</td>
                                <td>${agentInfo.portId.unLocationCode}</td>
                                <td>${agentInfo.portId.eciportcode}</td>
                                <td>${agentInfo.portId.shedulenumber}</td>
                                <td>
                                    <input type="button" class="buttonStyleNew" value="Delete"
                                           id="addDestination" onclick="deleteAgent('${user.userId}','${agentInfo.id}')"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </td>
        </tr>
    </table>
</c:if>
</body>