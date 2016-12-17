<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<body style="background:#ffffff">
    <cong:div>
        <table style="width:100%">
            <tr class="tableHeadingNew">
                <td>WareHouse Details For File No:<span class="fileNo">${lclCommodityForm.fileNumber} </span></td>
            </tr>
        </table>
        <cong:form name="lclCommodityForm" id="lclCommodityForm" action="/lclCommodity.do">
            <cong:hidden name="methodName" id="methodName"/>
            <input type="hidden" name="pieceId" id="bookingPieceId" value="${lclCommodityForm.pieceId}"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden name="id" value="${id}"/>
            <cong:hidden name="fileNumber" id="fileNumber" value="${lclCommodityForm.fileNumber}"/>
            <cong:hidden name="moduleName" id="moduleName" value="${lclCommodityForm.moduleName}"/>
            <c:if test="${not empty detailList}">
                <table width="100%" border="0" class="dataTable" style="border:1px solid #dcdcdc" id="dimsTable">
                    <thead>
                        <tr>
                            <th>Length(IN)</th>
                            <th>Length(CM)</th>
                            <th>Width(IN)</th>
                            <th>Width(CM)</th>
                            <th>Height(IN)</th>
                            <th>Height(CM)</th>
                            <th>Weight Per Piece(LBS)</th>
                            <th>Weight Per Piece(KGS)</th>
                            <th>Piece</th>
                            <th>WarehouseLine</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${detailList}" var="details">
                            <c:choose>
                                <c:when test="${rowStyle eq 'oddStyle'}">
                                    <c:set var="rowStyle" value="evenStyle"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="rowStyle" value="oddStyle"/>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${details.actualUom eq 'I'}">
                                    <tr class="${rowStyle}">
                                        <td>${details.actualLength}</td>
                                        <td>
                                            <c:if test="${not empty details.actualLength && details.actualLength ne '0.000'}">
                                                <fmt:formatNumber minFractionDigits="2">${details.actualLength * 2.54}</fmt:formatNumber>
                                            </c:if>
                                        </td>
                                        <td>${details.actualWidth}</td>
                                        <td>
                                            <c:if test="${not empty details.actualWidth && details.actualWidth ne '0.000'}">
                                                <fmt:formatNumber minFractionDigits="2">${details.actualWidth * 2.54}</fmt:formatNumber>
                                            </c:if>
                                        </td>
                                        <td>${details.actualHeight}</td>
                                        <td>
                                            <c:if test="${not empty details.actualHeight && details.actualHeight ne '0.000'}">
                                                <fmt:formatNumber minFractionDigits="2">${details.actualHeight * 2.54}</fmt:formatNumber>
                                            </c:if>
                                        </td>
                                        <td>${details.actualWeight}</td>
                                        <td>
                                            <c:if test="${not empty details.actualWeight && details.actualWeight ne '0.000'}">
                                                <fmt:formatNumber minFractionDigits="2">${details.actualWeight * 2.54}</fmt:formatNumber>
                                            </c:if>
                                        </td>
                                        <td>${details.pieceCount}</td>
                                        <td style="text-transform: uppercase;" class="stowedLocation">${details.stowedLocation}</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td>
                                            <c:if test="${not empty details.actualLength && details.actualLength ne '0.000'}">
                                                <fmt:formatNumber minFractionDigits="2">${details.actualLength * 0.39}</fmt:formatNumber>
                                            </c:if>
                                        </td>
                                        <td>${details.actualLength}</td>
                                        <td>
                                            <c:if test="${not empty details.actualWidth && details.actualWidth ne '0.000'}">
                                                <fmt:formatNumber minFractionDigits="2">${details.actualWidth * 0.39}</fmt:formatNumber>
                                            </c:if>
                                        </td>
                                        <td>${details.actualWidth}</td>
                                        <td>
                                            <c:if test="${not empty details.actualHeight && details.actualHeight ne '0.000'}">
                                                <fmt:formatNumber minFractionDigits="2">${details.actualHeight * 0.39}</fmt:formatNumber>
                                            </c:if>
                                        </td>
                                        <td>${details.actualHeight}</td>
                                        <td>
                                            <c:if test="${not empty details.actualWeight && details.actualWeight ne '0.000'}">
                                                <fmt:formatNumber minFractionDigits="2">${details.actualWeight * 0.39}</fmt:formatNumber>
                                            </c:if>
                                        </td>
                                        <td>${details.actualWeight}</td>
                                        <td>${details.pieceCount}</td>
                                        <td style="text-transform: uppercase;" class="stowedLocation">${details.stowedLocation}</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <cong:table>
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                </cong:tr>
            </cong:table>
            <cong:table width="100%" border="0" cellpadding="0" cellspacing="0">
                <cong:hidden name="bookingPieceWhseId" id="bookingPieceWhseId"/>
                <cong:tr>
                    <cong:td width ="1%" styleClass="textlabelsBoldforlcl" >Warehouse Location</cong:td>
                    <cong:td width="25%">
                        <cong:autocompletor name="whseLocation" id="whseLocation" width="400" query="DETAILWHSE" fields="warehouseNo,warehouseId"
                                            template="two" shouldMatch="true" scrollHeight="200" styleClass="mandatory text textLCLuppercase" container="NULL"/>
                        <input type="text" id="warehouseNo" name="warehouseNo" style="width:30px" readOnly="true" class="textlabelsBoldForTextBoxDisabledLook" value="${warehouseNo}"/>
                        <cong:hidden name="warehouseId" id="warehouseId"/>
                    </cong:td>
                    <cong:td width="5%" styleClass="textlabelsBoldforlcl">Line/Location</cong:td>
                    <cong:td  width="20%"><cong:text styleClass="text textLCLuppercase" name="cityLocation" id="cityLocation" value=""/>
                    </cong:td>
                    <c:if test="${lclCommodityForm.moduleName eq 'Exports'}">
                        <cong:td width="5%" styleClass="textlabelsBoldforlcl">Stowed By</cong:td>
                        <cong:td width="15%">
                            <cong:autocompletor name="stowedByUser" id="stowedByUser" width="400" template="two" query="WAREHOUSE_ACTIVATE_LOGINNAME"
                                                fields="NULL,stowedByUserId"  styleClass="text textLCLuppercase" position="left"
                                                container="NULL" scrollHeight="200"  shouldMatch="true" value="${lclCommodityForm.stowedByUser}"/>
                            <cong:hidden id="stowedByUserId" name="stowedByUserId"/>
                        </cong:td>
                    </c:if>
                    <cong:td>&nbsp;&nbsp;</cong:td>
                    <cong:td>
                        <input type="button" class="button-style1" value="Save" onclick="submitwhseDetail('addWhseDetails');"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:hidden id="status" name="status" value="${lclCommodityForm.status}"/><%--file Status--%>
        </cong:form>
    </cong:div>
    <c:if test="${not empty lclBookingPieceWhseList}">
        <cong:div style="width:99%; float:left; height:190px; overflow-y:scroll; border:1px solid #dcdcdc">
            <table class="dataTable" style="border:1px solid #dcdcdc">
                <thead>
                <th>Warehouse Location</th>
                <th>Warehouse Location Code</th>
                <th>Line Location</th>
                <c:if test="${lclCommodityForm.moduleName eq 'Exports'}">
                    <th>StowedBy</th>
                </c:if>
                <th>Entered Date & Time</th>
                <th>Action</th>
            </thead>
            <tbody style="text-transform: uppercase;">
                <c:forEach items="${lclBookingPieceWhseList}" var="whse" varStatus="whseCount">
                    <c:choose>
                        <c:when test="${rowStyle eq 'oddStyle'}">
                            <c:set var="rowStyle" value="evenStyle"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" value="oddStyle"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowStyle}">
                        <td>${whse.warehouse.warehouseName}</td>
                        <td>${whse.warehouse.warehouseNo}</td>
                        <td><span>${whse.location}</span></td>
                        <c:if test="${lclCommodityForm.moduleName eq 'Exports'}">
                            <td>
                                ${whse.stowedByUser.firstName} ${whse.stowedByUser.lastName}
                            </td>
                        </c:if>
                        <td>
                            <fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="entereddatetime"  value="${whse.enteredDatetime}"/>
                            ${entereddatetime}
                        </td>
                        <td>
                            <img src="${path}/images/edit.png"  style="cursor:pointer" class="cost" width="13" height="13" alt="edit"
                                 onclick="editWhse('${whse.id}', '${whse.warehouse.id}', '${whse.warehouse.warehouseName}',
                                     '${whse.warehouse.warehouseNo}', '${whse.location}','${whse.stowedByUser.userId}',
                                     '${whse.stowedByUser.firstName} ${whse.stowedByUser.lastName}');" title="Edit Whse"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </cong:div>
</c:if>
<script type="text/javascript">
    function submitwhseDetail(methodName) {
        var warehouseId = $('#warehouseId').val();
        if (warehouseId === null || warehouseId === "" || warehouseId === "0") {
            $.prompt("Please Enter Warehouse Location");
            $("#whseLocation").css("border-color", "red");
        } else {
            showLoading();
            $('#methodName').val(methodName);
            $('#lclCommodityForm').submit();
        }
    }
    function editWhse(id, whseId, whseName, whseNo, whseLoc, stowedByUserId,stowedByUser) {
        $('#bookingPieceWhseId').val(id);
        $('#warehouseId').val(whseId);
        $('#whseLocation').val(whseName);
        $('#warehouseNo').val(whseNo);
        $('#cityLocation').val(whseLoc);
        $('#stowedByUserId').val(stowedByUserId);
        $('#stowedByUser').val(stowedByUser);
    }
    $(document).ready(function () {
        $('#whseLocation').keyup(function () {
            if ($('#whseLocation').val() === "") {
                $('#whseLocation').val('');
                $('#warehouseNo').val('');
                $('#warehouseId').val('');
            }
        });
        $('#stowedByUser').keyup(function () {
            if ($('#stowedByUser').val() == "") {
                $('#stowedByUserId').val('');
                $('#stowedByUser').val('');
            }
        });
        if ($('#moduleName').val() === 'Exports') {
            var status = parent.$('#fileStatus').val();
            var dispo = parent.$("#dispoId").text();
            if ((status === 'W' && dispo === 'RCVD') ||
                status == 'WV' || status == 'R' || status == 'PR' || status == 'L' || status == 'M') {
            } else {
                $('#whseLocation').val('');
                $('#warehouseNo').val('');
                $('#warehouseId').val('');
            }
        }
    });
</script>
</body>

