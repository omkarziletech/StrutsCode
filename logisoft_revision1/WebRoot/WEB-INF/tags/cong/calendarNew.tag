<%@taglib tagdir="/WEB-INF/tags/cong/" prefix="cong"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@taglib uri="/WEB-INF/tlds/date.tld" prefix="date"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@attribute name="name" required="true"%>
<%@attribute name="value"%>
<%@attribute name="label"%>
<%@attribute name="id"%>
<%@attribute name="styleClass"%>
<%@attribute name="disabled"%>
<%@attribute name="tooltip"%>
<%@attribute name="is12HrFormat" type="java.lang.Boolean"%>
<%@attribute name="before" type="java.lang.Boolean"%>
<%@attribute name="today" type="java.lang.Boolean"%>
<%@attribute name="showTime" type="java.lang.Boolean"%>
<%@attribute name="calType"%>
<%@attribute name="onchange"%>
<c:choose>
    <c:when test="${today && showTime}">
        <c:set var="value" value="${date:formatDate(date:currentDate(),'dd-MMM-yyyy hh:mm:ss a')}"/>
    </c:when>
    <c:when test="${today}">
        <c:set var="value" value="${date:formatDate(date:currentDate(),'dd-MMM-yyyy')}"/>
    </c:when>
</c:choose>

<c:if test="${before}">
    <c:set var="value" value="${date:formatDate(date:subtractDays(date:currentDate(),30),'dd-MMM-yyyy')}"/>
</c:if>

<c:if test="${empty showTime}">
    <c:set var="showTime" value=""/>
</c:if>
<c:choose>
    <c:when test="${not empty is12HrFormat && is12HrFormat}">
        <c:set var="timeFormat" value="12"/>
    </c:when>
    <c:otherwise>
        <c:set var="timeFormat" value="24"/>
    </c:otherwise>
</c:choose>

<c:if test="${not empty callType}">
    <c:choose>
        <c:when test="${calType=='past'}">
            <c:set var="calenderType" value="past"/>
        </c:when>
        <c:otherwise>
            <c:set var="calenderType" value="future"/>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${readOnly}">
    <c:set var="readOnlyClass" value="readOnly"/>
</c:if>

<c:choose>
    <c:when test="${ empty calType}">
        <html:text property="${name}" styleId="${id}" title="${label}" readonly="${readOnly}"  onclick="NewCssCal('${id}','ddMMMyyyy','BOTH','${showTime}','${timeFormat}','')"  styleClass="cal-text  text ${styleClass} ${readOnlyClass}"  disabled="${disabled}" onchange="${onchange};this.focus()"/>
    </c:when>
    <c:otherwise>
        <html:text  property="${name}"  styleId="${id}" title="${label}" readonly="${readOnly}"  onclick="NewCssCal('${id}','ddMMMyyyy','BOTH','${showTime}','${timeFormat}','','${calType}')" styleClass="cal-text text  ${styleClass} ${readOnlyClass}" value="${value}"  disabled="${disabled}" onchange="${onchange};this.focus()"/>
    </c:otherwise>
</c:choose>

<!--change 'dropdown' to 'arrow' for calader change-->