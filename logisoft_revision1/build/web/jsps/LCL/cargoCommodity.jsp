<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html>
    <head>
       
    </head>
    <body>
        <div id="container">
            <ul class="htabs">
                <c:forEach items="${homeForm.tabs}" var="tabItem">
                    <li><a href="javascript: void(0)" tabindex="${tabItem.key}">${tabItem.value.itemDesc}</a></li>
                </c:forEach>
            </ul>
            <c:forEach items="${homeForm.tabs}" var="tabItem">
                <div class='pane'>
                    <iframe frameborder="0" id="tab${tabItem.key}" title="${tabItem.value.itemDesc}" name="${tabItem.key}" src='' width='100%' height='0' onload="hideProgressBar()"></iframe>
                    <input type="hidden" id="src${tabItem.key}" value="${tabItem.value.src}"/>
                </div>
            </c:forEach>
            <c:if test="${empty selectedTab}">
                <c:set var="selectedTab" value="0"/>
            </c:if>
        </div>
    </body>
</html>
