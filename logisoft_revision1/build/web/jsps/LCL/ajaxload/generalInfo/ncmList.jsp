<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>  
<div style="width:100%;height:155px; overflow-y:auto;overflow-x:hidden;border-collapse: collapse; border: 1px solid #F8F8FF">
    <table class="dataTable">
        <c:forEach items="${lcl3PList}" var="ncm">
            <c:if test="${ncm.type eq 'NCM'}">
                <c:choose>
                    <c:when test="${zebra eq 'odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <tr class="${zebra}" style="text-transform: uppercase;font-size: 11px;">
                    <td>
                        <span style="cursor: pointer" title="${fn:toUpperCase(ncm.reference)}">
                            ${fn:toUpperCase(fn:substring(ncm.reference,0,22))}</span>
                    </td >
                     <td style="width:10% !important;">
                        <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                             onclick="deleteNcm('Are you sure you want to delete?', '${ncm.id}');"/>
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