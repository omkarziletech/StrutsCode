<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
<table id="lcl3p-container" class="dataTable">
    <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
    <c:if test="${not empty lclHotCodeList}">
        <c:forEach items="${lclHotCodeList}" var="hotCode">
            <c:choose>
                <c:when test="${zebra eq 'odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <tr class="${zebra}" style="text-transform: uppercase">
                <td style="width:5%">
                    <input type="hidden"  class="hotCodesValue" value='${hotCode.id},${hotCode.code}'/>
                    <input type="hidden"  id="hotCodesRef${hotCode.id}" class="hotCode" value='${hotCode.code}'/>
                    <span style="cursor: pointer;text-transform: uppercase" 
                          title="${hotCode.code}&nbsp;${fn:contains(hotCode.code,"XXX") ? isHotCodeRemarks :''}">
                        ${fn:substring(hotCode.code,0,30)}
                    </span>
                </td>
                <td style="width:10% !important;">
                    <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                         onclick="deleteHotCode('Are you sure you want to delete?', '${hotCode.id}');"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <input type="hidden" id="hotCodeFlagId" name="hotCodeFlagId" value="${hotCodeFlag}"/>
</table>
<script type="text/javascript">
    $(document).ready(function () {
        $("[title != '']").not("link").tooltip();
    });
</script>
