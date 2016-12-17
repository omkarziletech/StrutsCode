<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <div style="width:100%;height:150px; overflow-y:auto;overflow-x:hidden;border-collapse: collapse; border: 1px solid #F8F8FF;">
    <c:set var="listSize" value="${fn:length(amsHBLList)}"/>
    <c:if test="${not empty amsHBLList &&  listSize gt 1}">
        <table class="dataTable" width="100%" id="amsHbl">
            <thead>
                <tr>
                    <th width="25%">SCAC</th>
                    <th width="48%">AMS No</th>
                    <th width="22%">Pcs</th>
                    <th width="5%">Seg DR#</th>
                    <th width="5%">Action</th>
                </tr>
            </thead>
            <c:forEach items="${amsHBLList}" var="amsHbl" varStatus="st">
                <c:if test="${st.count ne 1}">
                    <c:choose>
                        <c:when test="${zebra eq 'odd'}">
                            <c:set var="zebra" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="zebra" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${zebra}" style="text-transform: uppercase">
                        <td class="scacNo">${amsHbl.scac}</td >
                        <td>
                            <span class="amsHblNo" title="${fn:toUpperCase(amsHbl.amsNo)}">
                                ${fn:toUpperCase(fn:substring(amsHbl.amsNo,0,20))}
                            </span>

                        </td >
                        <td class="amsHblPcs">${amsHbl.pieces}</td>
                        <td><c:choose>
                                <c:when test="${not empty lclssheader and empty lclssheader.closedBy and empty amsHbl.segregationLclFileNumber}">
                                    <input type="button" value="Seg" class="button-style1" 
                                           onclick="openSegDrPopup('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', '${amsHbl.id}', '${amsHbl.amsNo}', '${amsHbl.pieces}', '${lclBookingForm.finalDestination}')"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="link" onclick="openSegBooking('${path}', '${amsHbl.segregationLclFileNumber.id}', '${amsHbl.segregationLclFileNumber.fileNumber}')">
                                        ${amsHbl.segregationLclFileNumber.fileNumber}
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${empty amsHbl.segregationLclFileNumber.fileNumber }">
                                <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                                     onclick="deleteAmsHbl('Are you sure you want to delete?', '${amsHbl.id}');"/>
                            </c:if>
                            <input type="hidden" class="amsHbl1" value="${amsHbl.id}"/>
                            <span class="hblAmsNoHblScac" style="display: none">${amsHbl.amsNo}${amsHbl.scac}</span>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
        </table>
    </c:if>
</div>
