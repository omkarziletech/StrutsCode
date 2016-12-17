<%@include file="/taglib.jsp" %>
<%@attribute name="value"%>
<%@attribute name="type"%>
<%@attribute name="styleClass"%>
<font  class="${type} ${styleClass} transparent">
    <fmt:formatNumber pattern="0.00" value="${value}"/>
</font>
