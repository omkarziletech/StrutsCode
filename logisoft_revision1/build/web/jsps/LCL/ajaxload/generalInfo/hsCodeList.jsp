<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<cong:div style="width:94%;height:155px; overflow-y:auto;overflow-x:hidden;border-collapse: collapse; border: 1px solid #F8F8FF">
    <c:if test="${not empty lclBookingHsCodeList}">
        <c:set var="count" value="0"/>
        <c:forEach items="${lclBookingHsCodeList}" var="hs">
            <c:if test="${not empty hs.noPieces}">
                <c:set var="count" value="1"></c:set>
            </c:if>
        </c:forEach>
        <c:choose>
            <c:when test="${count eq 1}">
                <display:table  name="${lclBookingHsCodeList}" id="hsCode" class="dataTable" sort="list" requestURI="/lclBooking.do" >
                    <display:column title="Code">
                        <span style=" cursor: pointer" title="${fn:toUpperCase(hsCode.codes)}">${fn:toUpperCase(fn:substring(hsCode.codes,0,25))}</span>
                    </display:column>
                    <display:column title="Pcs" >
                        <span style="font-size: 10px">${hsCode.noPieces}</span>
                    </display:column>
                    <display:column title="Wgt(KGS)">
                        <span style="font-size: 10px">${hsCode.weightMetric}</span>
                    </display:column>
                    <display:column title="PkgType">
                        <span style="font-size: 10px"> ${hsCode.packageType.description}</span>
                    </display:column>
                    <display:column title="Action">
                        <span style="font-size: 10px">
                            <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                                 onclick="deleteHsCode('Are you sure you want to delete?', '${hsCode.id}');"/></span>
                        </display:column>
                    </display:table>
                </c:when>
                <c:otherwise>
                <table class="dataTable">
                    <c:forEach items="${lclBookingHsCodeList}" var="hsCode">
                        <c:choose>
                            <c:when test="${zebra eq 'odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <tr style="text-transform: uppercase;font-size: 11px;" class="${zebra}">
                            <td style="width:5%">
                                <span style=" cursor: pointer" title="${fn:toUpperCase(hsCode.codes)}">
                                    ${fn:toUpperCase(fn:substring(hsCode.codes,0,25))}
                                </span>
                            </td>
                            <td style="width:10% !important;">
                                <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12" 
                                     onclick="deleteHsCode('Are you sure you want to delete?', '${hsCode.id}');"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>
    <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
</cong:div>
