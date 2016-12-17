<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<div class="pane tab-panel" index="${tabIndex}">
    <jsp:doBody/>
</div>
<c:set var="tabIndex" value="${tabIndex + 1}" scope="request"/>
