<%--
    Document   : Autocompleter tag
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : Lakshmi Naryanan
--%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@attribute name="name" required="true"%>
<%@attribute name="value"%>
<%@attribute name="label"%>
<%@attribute name="labeltitle"%>
<%@attribute name="title"%>
<%@attribute name="styleClass"%>
<%@attribute name="readOnly"%>
<%@attribute name="id"%>
<%@attribute name="validate"%>
<%@attribute name="table"%>
<%@attribute name="column"%>
<%@attribute name="columns"%>
<%@attribute name="container"%>
<%@attribute name="error_message"%>
<%@attribute name="disabled"%>
<%@attribute name="query"%>
<%@attribute name="URL"%>
<%@attribute name="maxlength"%>
<%@attribute name="fields"%>
<%@attribute name="paramFields"%>
<%@attribute name="params"%>
<%@attribute name="paramFunction"%>
<%@attribute name="callback"%>
<%@attribute name="template"%>
<%@attribute name="multiSelect"%>
<%@attribute name="shouldMatch" type="java.lang.Boolean"%>
<%@attribute name="focusOnNext" type="java.lang.Boolean"%>
<%@attribute name="position" type="java.lang.String"%>
<%@attribute name="width" type="java.lang.Integer"%>
<%@attribute name="scrollHeight" type="java.lang.String"%>
<c:catch var="exception">
    <c:if test="${id == null}" var="_var">
        <c:set var="id" value="${name}"/>
    </c:if>
    <c:if test="${container == null}">
        <c:set var="container" value="td"/>
    </c:if>
    <c:if test="${container != 'NULL'}">
        <${container}>
    </c:if>

    <c:if test="${empty shouldMatch}">
        <c:set var="shouldMatch" value="false"/>
    </c:if>
    <c:if test="${empty focusOnNext}">
        <c:set var="focusOnNext" value="false"/>
    </c:if>
    <c:if test="${empty position}">
        <c:set var="position" value="right"/>
    </c:if>
    <c:if test="${label != null}">
        <label title="${labeltitle}">
            ${label}
        </label>
    </c:if>

    <c:choose>
        <c:when test="${form != null}">
            <c:choose>
                <c:when test="${value != null}">
                    <html:text property="${name}" alt="${query}" name="${form}" styleId="${id}" styleClass="text ${styleClass}" readonly="${readOnly}" value="${value}"  disabled="${disabled}"
                               maxlength="${maxlength}" title="${title}"/>
                </c:when>
                <c:otherwise>
                    <html:text property="${name}"  alt="${query}" name="${form}" styleId="${id}" styleClass="text ${styleClass}" readonly="${readOnly}"  disabled="${disabled}"
                               maxlength="${maxlength}" title="${title}"/>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <html:text property="${name}" styleId="${id}"  alt="${query}"  styleClass="text ${styleClass}"  readonly="${readOnly}"  value="${value}"  disabled="${disabled}"
                       maxlength="${maxlength}" title="${title}"/>
        </c:otherwise>
    </c:choose>
    <c:if test="${shouldMatch}">
        <input type="hidden" name="${name}Check_DeFAULT" id="${id}Check_DeFAULT" value="${value}" title="${title}"/>
    </c:if>
    <c:choose>
        <c:when test="${empty query && (empty table || empty column)}">
            <cong:prompt text="Either query or table and column required : '${name}'" type="warning"/>
        </c:when>
        <c:otherwise>
            <c:if test="${not empty table && not empty column}">
                <c:set var="requiredColumns" value="${fn:split(columns,',')}"/>
                <c:set var="requiredFields" value="${fn:split(fields,',')}"/>
                <c:if test="${not empty requiredColumns && not empty requiredFields && fn:length(requiredColumns)!=fn:length(requiredFields)}">
                    <cong:prompt text="Both columns and fields should be same numbers" type="warning"/>
                </c:if>
            </c:if>
        </c:otherwise>
    </c:choose>

    <!-- if the attribute table and columns is not null then contruct URL -->
     
    <c:choose>
        <c:when test="${not empty multiSelect && multiSelect}">
            <c:set value="/jsps/LCL/export/search/getMultieAutoCompleter.jsp"  var="jspPage"/>
        </c:when>
        <c:otherwise>
             <c:set value="/actions/getAutocompleterResults.jsp"  var="jspPage"/>
        </c:otherwise>
    </c:choose>
        <c:url var="URL" value="${jspPage}" scope="page"> 
        <c:choose>
            <c:when  test="${not empty query}">
                <%--<c:param name="query" value="${query}"/>--%>
                <c:set var="param_query" value="query=${query}"/>
            </c:when>
            <c:when  test="${not empty table && not empty column}">
                <c:param name="table" value="${table}"/>
                <c:param name="column" value="${column}"/>
                <c:param name="columns" value="${columns}"/>
            </c:when>
        </c:choose>
        <c:param name="template" value="${template}"/>
    </c:url>


    <!-- If URL is not null -->
    <c:if test="${URL != null}">
        <script>
            $().ready(function() {
                $("#${id}").autocomplete("${URL}&", {
                    cellSeparator: "^",
                    width: "${width}",
                    shouldMatch:${shouldMatch},
                    focusOnNext:${focusOnNext},
                    noAuto: false,
                    position: "${position}",
            <c:if test="${not empty scrollHeight}">
                        scroll: true,
                        scrollHeight: "${scrollHeight}",
            </c:if>
                        onItemSelect: function(li) {
            <c:if test="${!focusOnNext}">
                            $(document).keydown(function(e) {
                                if (e.keyCode === 13) {
                                    $("#${id}").focus().val();
                                    return false;
                                }
                            });
            </c:if>
                            var values = li.id.split("^");
                            $("#${id}").val(values[0]);
                            if (${shouldMatch}) {
                                $("#${id}Check_DeFAULT").val(values[0]);
                                //  $(document).keydown(function(e) {
                                // if (e.keyCode === 13) {
                                // return false;
                                //  }
                                // });
                            }
            <c:set var="index" value="1"/>
            <c:forTokens items="${fields}" delims="," var="field">
                <c:if test="${field!='null'}">
                    <c:choose>
                        <c:when test="${fn:containsIgnoreCase(field,':Check')}">
                                        $("#${fn:replace(field,':Check','')}").val(values["${index}"]);
                                        $("#${fn:replace(field,':','')}").val(values["${index}"]);
                        </c:when>
                        <c:otherwise>
                                        $("#${field}").val(values["${index}"]);
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:set var="index" value="${index+1}"/>
            </c:forTokens>
            <c:if test="${not empty callback}">
                            eval("${callback}");
            </c:if> <c:if test="${!focusOnNext}">
                                $("#${id}").focus().val();
            </c:if>
                        },
                        callBefore: function() {
                            param = "${URL}";
            <c:forTokens items="${paramFields}" delims="," var="field" varStatus="status">
                <c:if test="${not empty field}">
                                param += "&param${status.count}=" + $("#${field}").val();
                </c:if>
            </c:forTokens>
            <c:forTokens items="${params}" delims="," var="paramV" varStatus="status">
                <c:if test="${not empty paramV}">
                                param += "&param${status.count}=${paramV}"
                </c:if>
            </c:forTokens>
                            param += "&";
            <c:if test="${not empty query}">
                            param += "query=" + $("#${id}").attr("alt") + "&";
            </c:if>
            <c:if test="${not empty paramFunction}">
                            param +=${paramFunction} + "&";
            </c:if>
                            var val = $("#${id}").val();
                            val=val.replace('&',':amp:');
                            param += "value=" + val;
                            $("#${id}").setOptions({
                                url: param
                            });
                        }
                    });
                });
                $("#${id}").change(function() {
                    if (${shouldMatch}) {
                        if ($("#${id}").val() != $("#${id}Check_DeFAULT").val()) {
                            $("#${id}").val("");
                            $("#${id}Check_DeFAULT").val("");
            <c:forTokens items="${fields}" delims="," var="field">
                <c:if test="${not empty field}">
                    <c:choose>
                        <c:when test="${fn:containsIgnoreCase(field,':Check')}">
                                        $("#${fn:replace(field,':Check','')}").val("");
                                        $("#${fn:replace(field,':','')}").val("");
                        </c:when>
                        <c:otherwise>
                                        $("#${field}").val("");
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </c:forTokens>
                        }
                    }
                });
        </script>
    </c:if>
    <jsp:doBody/>
    <c:if test="${container != 'NULL'}">
        </${container}>
    </c:if>
</c:catch>
<cong:prompt type="exception" text="${exception}"/>
