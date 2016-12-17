<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">Sub Types</td>
            <td>
                <a id="lightBoxClose"  href="javascript:closeAssignWindow();">
                    <img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<div style="width:100%;height:70%;">
    <table width="100%" class="displaytagstyleNew">
         <tbody>
             <c:set var="classApSpecialist" value="odd"/>
             <c:if test="${!empty assignedsubTypes}">
                <c:forEach var="assignedsubType" items="${assignedsubTypes}">
                    <tr class="${classApSpecialist}">
                        <td><input type="checkbox" name="subType" value="${assignedsubType.subType}" checked="checked"><c:out value="${assignedsubType.subType}"/></td>
                    </tr>
                    <c:choose>
                        <c:when test="${classApSpecialist=='odd'}">
                            <c:set var="classApSpecialist" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="classApSpecialist" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
             <c:if test="${!empty availableSubTypes}">
                <c:forEach var="availableSubType" items="${availableSubTypes}">
                    <tr class="${classApSpecialist}">
                        <td><input type="checkbox" name="subType" value="${availableSubType}"><c:out value="${availableSubType}"/></td>
                    </tr>
                    <c:choose>
                        <c:when test="${classApSpecialist=='odd'}">
                            <c:set var="classApSpecialist" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="classApSpecialist" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
            <c:if test="${!empty assignedsubTypes || !empty availableSubTypes}">
                <tr class="textlabelsBold">
                    <td><input type="button" class="buttonStyleNew" value="Assign" onclick="assignApSpecialist('${userId}')"/></td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>