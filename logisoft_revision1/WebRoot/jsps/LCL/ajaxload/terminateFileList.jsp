<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<cong:div style="width:94%;height:155px; overflow-y:auto;overflow-x:hidden;border-collapse: collapse; border: 1px solid #F8F8FF">
    <c:if test="${not empty batchTerminateFileList}">
        <table class="dataTable">
            <input type="hidden" id="fileSize" name="fileSize" value="${fn:length(batchTerminateFileList)}"/>
            <c:forEach items="${batchTerminateFileList}" var="terminateList">
                <c:choose>
                    <c:when test="${zebra eq 'odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <input type="hidden"  id="fileNoRef${terminateList.key}" class="fileNo" value='${terminateList.key}'/>
                <tr style="text-transform: uppercase;font-size: 11px;" class="${zebra}">
                    <td style="width:5%">
                            ${terminateList.value}
                    </td>
                    <td style="width:10% !important;">
                        <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12" 
                             onclick="removeTermiateFile('Are you sure you want to delete?', '${terminateList.key}');"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</cong:div>
