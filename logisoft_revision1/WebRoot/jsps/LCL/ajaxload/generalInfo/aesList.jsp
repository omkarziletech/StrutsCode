<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:div style="width:120px">
<cong:table>
    <c:forEach items="${lcl3PList}" var="aes">
        <c:if test="${aes.type=='AES_ITNNUMBER' || aes.type=='AES_EXCEPTION'}">
            <c:choose>
                <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <cong:tr styleClass="${zebra}" style="text-transform: uppercase">
                <cong:td style="width:5%"> <a style=" cursor: pointer" title="${fn:toUpperCase(aes.reference)}">
                ${fn:substring(aes.reference,0,15)}</a></cong:td>
                <cong:td styleClass="floatRight">
                    <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                         onclick="deleteAes('Are you sure you want to delete ?');deleteLcl3pReference('${aes.id}')"/>
                </cong:td>
            </cong:tr>
        </c:if>
    </c:forEach>
    <cong:hidden name="custId" id="custId" />
    <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
</cong:table>
</cong:div>
