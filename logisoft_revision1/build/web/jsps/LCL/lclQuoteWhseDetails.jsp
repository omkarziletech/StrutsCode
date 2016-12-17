<%--
    Document   : WareHouse Details
    Created on : jan 2, 2013, 5:09:44 PM
    Author     : Maha
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left; ">
        <cong:form name="lclQuoteCommodityForm" id="lclQuoteCommodityForm" action="/lclQuoteCommodity.do">
            <cong:table>
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td width="90%">Whse Details <span class="fileNo">${lclQuoteCommodityForm.fileNumber}</span></cong:td>
                </cong:tr>
            </cong:table>
            <cong:hidden name="fileNumber" value="${lclQuoteCommodityForm.fileNumber}"/>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclQuoteCommodityForm.fileNumberId}"/>
            <cong:hidden name="quotePieceId" id="quotePieceId" value="${lclQuoteCommodityForm.quotePieceId}"/><%--bookingPieceId --%>
            <c:if test="${not empty detailList}">
                <table width="100%" class="dataTable" id="dimsTable">
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
                            <th>Warehouse Line</th>
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
                                        <td style="text-transform: uppercase;">${details.stowedLocation}</td>
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
                                        <td style="text-transform: uppercase;">${details.stowedLocation}</td>
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
                <cong:tr>
                    <cong:hidden name="quotewhseId" id="quotewhseId" value=""/>
                    <cong:td styleClass="textlabelsBoldforlcl" >Warehouse Location</cong:td>
                    <cong:td><cong:autocompletor name="whseLocation" id="whseLocation" width="400" query="DETAILWHSE" fields="NULL,warehouseId"  value=""
                                                 template="two" shouldMatch="true" scrollHeight="200px" styleClass="mandatory text textLCLuppercase"/>
                        <cong:hidden name="warehouseId" id="warehouseId" value=""/>
                    </cong:td>
                    <cong:td width="20%" styleClass="textlabelsBoldforlcl">Line/Location</cong:td>
                    <cong:td  width="20%"><cong:text styleClass="text textLCLuppercase" name="cityLocation" id="cityLocation" value=""/>
                    </cong:td>
                    <cong:td>&nbsp;</cong:td>
                    <cong:td><input type="button" class="button-style1" value="Save" onclick="submitwhseDetail('addWhseDetails')"/>
                    </cong:td>
                    <cong:td width="30%">&nbsp;</cong:td>
                </cong:tr>
            </cong:table>
            <cong:table>&nbsp;</cong:table>
        </cong:form>
    </cong:div>
    <c:if test="${not empty whseList}">
        <cong:div style="width:99%; float:left; height:190px; overflow-y:scroll; border:1px solid #dcdcdc">
            <display:table  name="${whseList}" id="whse" class="dataTable" sort="list" requestURI="/lclQuoteCommodity.do" >
                <display:column title="Warehouse Location" style="text-transform: uppercase">
                    ${whse.warehouse.warehouseName}
                </display:column>
                <display:column title="Warehouse Location Code" style="text-transform: uppercase">
                    ${whse.warehouse.warehouseNo}
                </display:column>
                <display:column title="Line Location" style="text-transform: uppercase">
                    ${whse.location}
                </display:column>
                <display:column title="Entered Date & Time" >
                    <fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="entereddatetime"  value="${whse.enteredDatetime}"/>
                    ${entereddatetime}
                </display:column>
                <display:column title="Action" >
                    <img src="${path}/images/edit.png"  style="cursor:pointer" class="cost" width="13" height="13" alt="edit"
                         onclick="editWhse('${whse.id}', '${whse.warehouse.id}', '${whse.warehouse.warehouseName}', '${whse.warehouse.warehouseNo}', '${whse.location}');" title="Edit Whse"/>
                </display:column>
            </display:table>
        </cong:div>
    </c:if>
    <script type="text/javascript">
        function submitwhseDetail(methodName) {
            var warehouseId = $('#warehouseId').val();
            var whseLocation = $('#whseLocation').val();
            if (warehouseId == null || warehouseId == undefined || warehouseId == "" || whseLocation == null || whseLocation == undefined || whseLocation == "") {
                $.prompt("Please Enter Warehouse Location");
                $("#whseLocation").css("border-color", "red");
                return false;
            } else {
                showLoading();
                $('#methodName').val(methodName);
                $('#lclQuoteCommodityForm').submit();
            }
        }


        function checkwhselocation() {//remove
            var whseLocation = $('#whseLocation').val();
            var bookingPieceId = $('#bookingPieceId').val();
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkWhseLocationForQuote",
                    param1: whseLocation,
                    param2: bookingPieceId,
                    dataType: "json"
                },
                success: function (data) {
                    if (data) {
                        $.prompt("Warehouse Location is already added.Please Select different Warehouse Location");
                        $("#whseLocation").val("");
                        $("#whseLocation").css("border-color", "red");
                        return false;
                    }
                }
            });
        }

        function editWhse(id, whseId, whseName, whseNo, whseLoc) {
            $('#quotewhseId').val(id);
            $('#warehouseId').val(whseId);
            $('#whseLocation').val(whseName);
            $('#cityLocation').val(whseLoc);
        }

    </script>
</body>

