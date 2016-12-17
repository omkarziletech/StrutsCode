<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <table class="dataTable">
        <thead>
            <tr>
                <th>
                    <b style="color: #FF0000;font-weight:bold">Priority Notes</b>
                </th>
            </tr>
        </thead>
    <c:forEach var="remarks" items="${lclRemarksPriority}">
        <c:choose>
            <c:when test="${zebra eq 'odd'}">
                <c:set var="zebra" value="even"/>
            </c:when>
            <c:otherwise>
                <c:set var="zebra" value="odd"/>
            </c:otherwise>
        </c:choose>
        <tbody>
            <tr class="${zebra}">
                <td style="text-transform: uppercase;white-space: normal;">${remarks}</td>
            </tr>
        </tbody>
    </c:forEach>
</table>
