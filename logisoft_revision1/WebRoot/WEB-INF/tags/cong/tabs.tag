<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@attribute name="tabs"%>
<c:if test="${empty tabInit}">
    <c:set var="tabInit" value="tabs" scope="request"/>
    <link href="${path}/js/tab/tab.css" type="text/css" rel="stylesheet" />
    <script src="${path}/js/tab/tab.js" type="text/javascript" ></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("ul.htabs").tabs("> .pane",{effect: 'fade',current:'selected'});
        });
    </script>
</c:if>

<c:set var="tabIndex" value="0" scope="request"/>

<div class="tab-container">
    <ul class="htabs">
        <c:forTokens var="tab" items="${tabs}" delims=",">
            <li index="${tabIndex}"><span><a href="#">${tab}</a></span></li>
            <c:set var="tabIndex" value="${tabIndex + 1}" scope="request"/>
        </c:forTokens>
    </ul>
    <c:set var="tabIndex" value="0" scope="request"/>
    <jsp:doBody/>
</div>
<%--reset index --%>    
<c:set var="tabIndex" value="0" scope="request"/>
