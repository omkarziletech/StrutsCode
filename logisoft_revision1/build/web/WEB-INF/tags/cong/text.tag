<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@attribute name="name" required="true"%>
<%@attribute name="value"%>
<%@attribute name="labeltitle"%>
<%@attribute name="styleClass"%>
<%@attribute name="readOnly" type="java.lang.Boolean"%>
<%@attribute name="id"%>
<%@attribute name="validate"%>
<%@attribute name="table"%>
<%@attribute name="column"%>
<%@attribute name="tooltip"%>
<%@attribute name="title"%>
<%@attribute name="container"%>
<%@attribute name="error_message"%>
<%@attribute name="disabled"%>
<%@attribute name="style"%>
<%@attribute name="query"%>
<%@attribute name="URL"%>
<%@attribute name="maxlength"%>
<%@attribute name="onclick"%>
<%@attribute name="onchange"%>
<%@attribute name="onkeyup"%>
<%@attribute name="callback"%>
<%@attribute name="tabindex"%>
<%@attribute name="onkeypress"%>
<c:catch var="exception">
    <c:if test="${id == null}" var="_var">
        <c:set var="id" value="${name}"/>
    </c:if>
    <c:set var="tabIndex" value="0"/>
    <c:if test="${readOnly}">
        <c:set var="readOnlyClass" value="readOnly"/>
        <c:set var="tabIndex" value="-1"/>
    </c:if>

    <c:choose>
        <c:when test="${form != null}">
            <c:choose>
                <c:when test="${value != null}">
                    <html:text title="${title}" style="${style}" property="${name}" name="${form}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}" readonly="${readOnly}" value="${value}"  disabled="${disabled}"
                               maxlength="${maxlength}"
                               onkeyup="${onkeyup}"
                               onkeypress="${onkeypress}"
                               onchange="${onchange}"
                               onclick="${onclick}" tabindex="${tabIndex}" />
                </c:when>
                <c:otherwise>
                    <html:text title="${title}" style="${style}" property="${name}" name="${form}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}" readonly="${readOnly}"  disabled="${disabled}"
                               maxlength="${maxlength}"
                               onkeyup="${onkeyup}"
                               onkeypress="${onkeypress}"
                               onchange="${onchange}"
                               onclick="${onclick}" tabindex="${tabIndex}"/>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <html:text title="${title}" property="${name}" style="${style}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}"  readonly="${readOnly}"  value="${value}"  disabled="${disabled}"
                       maxlength="${maxlength}"
                       onkeyup="${onkeyup}"
                       onkeypress="${onkeypress}"
                       onchange="${onchange}"
                       onclick="${onclick}" tabindex="${tabIndex}"/>
        </c:otherwise>
    </c:choose>


    <!-- if the attribute table and columns is not null then contruct URL
    <c:if test="${not empty table && not empty column}">
        <c:url var="URL" value="/action/getAutocompleterResults.jsp" scope="page">
            <c:choose>
                <c:when  test="${not empty query}">
                    <c:param name="query" value="${query}"/>
                </c:when>
                <c:when  test="${not empty table && not empty column}">
                    <c:param name="table" value="${table}"/>
                    <c:param name="column" value="${column}"/>
                    <c:param name="columns" value="${columns}"/>
                </c:when>
            </c:choose>
            <c:param name="template" value="${template}"/>
        </c:url>
    </c:if> -->


    <!-- If URL is not null -->
    <c:if test="${not empty URL}">
        <script>
            $(document).ready(function(){
                $("#${id}").autocomplete("${URL}&",{
                    onItemSelect:function(li){
                        var values = li.id.split("|");
                        $("#${id}").val(values[0]);
                <c:if test="${not empty callback}">
                            eval("${callback}");
                </c:if>
                        }
                    });                
            })
        </script>
    </c:if>
    <jsp:doBody/>
    <c:if test="${container != 'NULL'}">
        </${container}>
    </c:if>
</c:catch>
<cong:prompt type="exception" text="${exception}"/>
