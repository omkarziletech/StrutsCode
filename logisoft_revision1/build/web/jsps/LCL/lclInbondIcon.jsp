<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:div style="width:100%; float:left;">
    <cong:table>
        <cong:tr>
            <cong:td valign="middle">
                <c:if test="${inbondListSize > 1}">
                    <div id="inbondNumers">
                        <img src="${path}/jsps/LCL/images/add2.gif" id="inbondIcon" width="16"
                             height="16" alt="display" title="${inbondNumber}">
                    </div>
                </c:if>
            </cong:td>
        </cong:tr>
    </cong:table>
    <script type="text/javascript">
        jQuery(document).ready(function(){
            $("[title != '']").not("link").tooltip();
        });
    </script>
</cong:div>
