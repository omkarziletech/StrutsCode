<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:table>
    <c:forEach items="${lcl3PList}" var="inbnd">
        <c:if test="${inbnd.type=='IB'}">
            <c:choose>
                <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <cong:tr styleClass="${zebra}">
                <cong:td>${inbnd.reference}</cong:td>
                <cong:td styleClass="floatRight">
                    <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                         onclick="deleteInbond('Are you sure you want to delete?');deleteLcl3pReference('${inbnd.id}')"/>
                </cong:td>
            </cong:tr>
        </c:if>
    </c:forEach>
    <cong:hidden name="custId" id="custId" />
    <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
</cong:table>

