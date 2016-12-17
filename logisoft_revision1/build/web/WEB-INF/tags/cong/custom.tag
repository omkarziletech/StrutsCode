<%@include file="/taglib.jsp" %>
<%@attribute name="code" required="true"%>
<jsp:setProperty name="customUtil" property="code" value="${code}"/>
<c:choose>
    <c:when test="${user.debug}">
        <cong:hasRole role="DEV">
            <div style="border: dashed red 1px; float: left;width: 100%">
                <div id="${customUtil.custom.id}_tool">
                    <font class="selected_${customUtil.custom.enabled} cursor" onclick="loadPage('#${customUtil.custom.id}_tool','${path}/custom.do?action=action&customId=${customUtil.custom.id}&enable=${customUtil.custom.enabled}')">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </font>
                </div>
                <jsp:doBody/>
            </div>
        </cong:hasRole>
    </c:when>
    <c:otherwise>
        <c:if test="${customUtil.enabled}">
            <jsp:doBody/>
        </c:if>
    </c:otherwise>
</c:choose>
