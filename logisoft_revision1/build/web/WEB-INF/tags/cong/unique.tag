<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
    Note : cong body tag is required to make it worked.
--%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@attribute name="name" required="true"%>
<%@attribute name="value"%>
<%@attribute name="table" required="true"%>
<%@attribute name="column" required="true"%>
<%@attribute name="tableId" required="true"%>
<%@attribute name="label"%>
<%@attribute name="labeltitle"%>
<%@attribute name="styleClass"%>
<%@attribute name="readOnly" type="java.lang.Boolean"%>
<%@attribute name="id"%>
<%@attribute name="validate"%>
<%@attribute name="tooltip"%>
<%@attribute name="title"%>
<%@attribute name="container"%>
<%@attribute name="disabled"%>
<%@attribute name="query"%>
<%@attribute name="URL"%>
<%@attribute name="maxlength"%>
<%@attribute name="onclick"%>
<%@attribute name="onkeyup"%>

<c:catch var="exception">
    <c:if test="${empty id}" var="_var">
        <c:set var="id" value="${name}"/>
    </c:if>
    <c:if test="${empty tableId}">
        <c:set var="tableId" value="id"/>
    </c:if>
    <c:if test="${container == null}">
        <c:set var="container" value="td"/>
    </c:if>
    <c:if test="${readOnly}">
        <c:set var="readOnlyClass" value="readOnly"/>
    </c:if>
    <c:if test="${container != 'NULL'}">
        <${container}>
    </c:if>

    <c:if test="${empty label}">
        <c:set var="label" value="${name}"/>
    </c:if>

    <c:choose>
        <c:when test="${form != null}">
            <c:choose>
                <c:when test="${value != null}">
                    <html:text title="${title}" property="${name}" name="${form}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}" readonly="${readOnly}" value="${value}"  disabled="${disabled}"
                               maxlength="${maxlength}"
                               onkeyup="${onkeyup}"
                               onclick="${onclick}"/>
                </c:when>
                <c:otherwise>
                    <html:text title="${title}" property="${name}" name="${form}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}" readonly="${readOnly}"  disabled="${disabled}"
                               maxlength="${maxlength}"
                               onkeyup="${onkeyup}"
                               onclick="${onclick}"/>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <html:text title="${title}" property="${name}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}"  readonly="${readOnly}"  value="${value}"  disabled="${disabled}"
                       maxlength="${maxlength}"
                       onkeyup="${onkeyup}"
                       onclick="${onclick}"/>
        </c:otherwise>
    </c:choose>


    <!-- if the attribute table is not null then contruct URL -->
    <c:if test="${not empty table && not empty column}">
        <c:url var="URL" value="/action/ajax/unque.jsp" scope="page">
            <c:choose>
                <c:when  test="${not empty query}">
                    <c:param name="query" value="${query}"/>
                </c:when>
                <c:when  test="${not empty table && not empty column}">
                    <c:param name="id" value="${tableId}"/>
                    <c:param name="table" value="${table}"/>
                    <c:param name="column" value="${column}"/>                    
                </c:when>
            </c:choose>
            <c:param name="template" value="${template}"/>
        </c:url>
    </c:if>


    <!-- If URL is not null -->
    <c:choose>
        <c:when test="${not empty unique_rules}">
            <c:set var="separator" value="," scope="page"/>
        </c:when>
        <c:otherwise>
            <c:set var="separator" value="" scope="page"/>
            <c:set var="unique_rules" value="" scope="page"/>
            <c:set var="unique_messages" value="" scope="page"/>
        </c:otherwise>
    </c:choose>
    <c:set var="hash" value="#"/>
    <c:if test="${URL != null}">
        <c:set scope="request" var="unique_rules" value="${unique_rules} ${separator} ${name}: {
                        remote: {
                            url: '${URL}',
                            type: 'get',
                            data: {
                                value: function() {
                                    return $('${hash}${id}').val();
                                },
                                test:'test'
                            }
                        }
                    }"
         />
        <c:set scope="request" var="unique_messages" value="${unique_messages} ${separator} ${name}: {remote: '${label} already exists.'}"/>
    </c:if>
    <jsp:doBody/>
    <c:if test="${container != 'NULL'}">
        </${container}>
    </c:if>
</c:catch>
<cong:prompt type="exception" text="${exception}"/>
