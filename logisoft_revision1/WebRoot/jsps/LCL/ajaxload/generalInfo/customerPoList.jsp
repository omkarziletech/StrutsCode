<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>  
<div style="width:94%;height:155px; overflow-y:auto;overflow-x:hidden;border-collapse: collapse; border: 1px solid #F8F8FF">
    <table class="dataTable">
        <c:forEach items="${lcl3PList}" var="customerPo">
            <c:if test="${customerPo.type eq 'CP'}">
                <c:choose>
                    <c:when test="${zebra eq 'odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <tr class="${zebra}" style="text-transform: uppercase">
                    <td>
                        <span style="cursor: pointer" title="${fn:toUpperCase(customerPo.reference)}">
                            ${fn:toUpperCase(fn:substring(customerPo.reference,0,23))}
                        </span>
                    </td>
                    <td style="width:10% !important;">
                        <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                             border="0" onclick="deleteCustPo('Are you sure you want to delete?', '${customerPo.id}');"/>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
    </table>
</div>
<script type="text/javascript">
                                 $(document).ready(function() {
                                     $("[title != '']").not("link").tooltip();
                                 });
</script>

