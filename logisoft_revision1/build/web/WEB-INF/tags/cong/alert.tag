<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@attribute name="type"%>
<%@attribute name="onclick"%>
<c:if test="${empty type}">
    <c:set var="type" value="success"/>
</c:if>

<c:if test="${not empty alert}">
    <div class="prompt" onclick="${onclick}" id="alert">
        <label class="${type}">${alert}</label>
    </div>
        <script>
            $("#alert").fadeOut(5000);
        </script>
</c:if>
