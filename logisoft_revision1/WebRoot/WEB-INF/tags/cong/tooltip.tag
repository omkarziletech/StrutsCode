<%-- 
    Document   : tooltip
    Created on : Jul 13, 2010, 2:36:43 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@attribute name="tooltip" required="true"%>
<%@attribute name="id"%>
<%@attribute name="styleClass"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>

<c:if test="${empty id && empty styleClass }">
    <cong:prompt type="warning" text="Either id or styleClass is required for tag tooltip"/>
</c:if>

<c:if test="${inittooltip!='tooltip'}">
    <script type="text/javascript" src="${path}/js/tooltip/tooltip.jquery.js"></script>
</c:if>

<c:choose>
    <c:when test="${not empty id}">
        <script type="text/javascript">
            $(document).ready(function(){
                $('#${id}').tooltip({tooltip_text:"${tooltip}"});
            });
        </script>
    </c:when>
    <c:otherwise>
        <script type="text/javascript">
            $(document).ready(function(){
                $('.${styleClass}').tooltip({tooltip_text:"${tooltip}"});
            });
        </script>
    </c:otherwise>
</c:choose>
<c:set var="inittooltip" value="tooltip" scope="request"/>
