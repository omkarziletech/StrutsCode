<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<cong:javascript src="${path}/js/jquery/jquery.util.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclCommodity.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<script type="text/javascript">
    function showCommodity(bookingPieceId) {
        fillCommodity(bookingPieceId);
    }

    //---------removed
    $(document).ready(function () {
        var status = $("#status").val();
    <c:choose>
        <c:when test="${not empty cargoReceivedList}">
        var cargoReceived = true;
            <c:if test="${fn:length(cargoReceivedList) == 1}">
        cargoReceived = false;
            </c:if>
        cargoCommodity('${path}', '${lclCommodityForm.fileNumberId}', '${lclCommodityForm.fileNumber}', 'true', '${lclCommodityForm.originId}', '${lclCommodityForm.destinationId}', '${lclCommodityForm.rateType}', 'V', 'WV', cargoReceived, '${lclCommodityForm.verifiedIds}');
        </c:when>
        <c:otherwise>

        if ($("#id").val() === "" && parent.$("#terminal").val() === "QUEENS, NY/59")
        {
            var $radios = $('input:radio[name=personalEffects]');
            $radios.filter('[value=Y]').attr('checked', true);
        }
        if (status === 'WV' && parent.$('#moduleName').val() === 'Exports') {
            $("#commodityCancel").hide();   // hide the cancel button only when cargo received;
        }
        actualCommodityFlag($("#fileNumberId").val());
            <c:if test="${fn:length(lclCommodityList) > 1}">
        $("#cargoReceived").val(true);
            </c:if>
            <c:if test="${not empty lclCommodityList}">
//        if (status === 'WV' && parent.$('#moduleName').val() === 'Exports' && $('#barrel').val() === 'true') {
//                        $('#container').hide();
//                    }else{
        $("#actulDims").click();
//        }
            </c:if>
        barrelBusiness();
        showDimsDetails();
        </c:otherwise>
    </c:choose>
    });
</script>

<body onload="">
    <c:if test="${not empty lclCommodityList}">
        <div id="container" class="tab-box" style="display: none">
            <ul class="">
                <c:set var="count" value="0"/>
                <c:forEach items="${lclCommodityList}" var="tabItem">
                    <c:set var="replace" value="\\'" />
                    <li><a href="${path}/jsps/LCL/commodityTab.jsp" id="${count}" class="tabLink"
                           onclick="showCommodity('${tabItem.id}');">${tabItem.commodityType.descEn}</a></li>
                        <c:set var="count" value="{count+1}"/>
                    </c:forEach>
            </ul>
        </div>
    </c:if>
    <cong:div style="width:99%;float:left;">
        <cong:form  id="lclCommodityForm"  name="lclCommodityForm" action="/lclCommodity.do" >
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="cargoReceived" id="cargoReceived" value="${lclCommodityForm.cargoReceived}"/>
            <cong:hidden name="verifiedIds" id="verifiedIds" value="${lclCommodityForm.verifiedIds}"/>
            <cong:hidden name="fileStatus" id="fileStatus"  value="${lclCommodityForm.fileStatus}"/>
            <cong:hidden name="calcHeavy" id="calcHeavy"/>
            <cong:hidden name="deliveryMetro" id="deliveryMetro"/>
            <cong:hidden name="insurance" id="insurance"/>
            <cong:hidden name="pcBoth" id="pcBoth"/>
            <cong:hidden name="shortShipFileNo" id="shortShipFileNo"  value="${lclCommodityForm.shortShipFileNo}"/><%-- Original ShortShip FileNo --%>
            <cong:hidden name="valueOfGoods" id="valueOfGoods"/>
            <cong:hidden name="originId" id="originId" value="${lclCommodityForm.origin}"/>
            <cong:hidden name="destinationId" id="destinationId" value="${lclCommodityForm.destination}"/>
            <cong:hidden name="polId" id="polId" value=""/>
            <cong:hidden name="podId" id="podId" value=""/>
            <cong:hidden name="rateType" id="rateType" value="${lclCommodityForm.rateType}"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
            <cong:hidden name="pol" id="pol" value="${lclCommodityForm.pol}"/>
            <cong:hidden name="pod" id="pod" value="${lclCommodityForm.pod}"/>
            <cong:hidden name="billingType" id="billingType" value="${lclCommodityForm.billingType}"/>
            <cong:hidden name="billToParty" id="billToParty" value="${lclCommodityForm.billToParty}"/>
            <cong:hidden name="impCfsWarehsId" id="impCfsWarehsId" value="${lclCommodityForm.impCfsWarehsId}"/>
            <cong:hidden name="agentNo" id="agentNo" value="${lclCommodityForm.agentNo}"/>
            <cong:hidden name="fileNumber" value="${fileNumber}"/>
            <cong:hidden name="editDimFlag" id="editDimFlag" value="${editDimFlag}"/>
            <cong:hidden name="commCode" id="commCode" value="${commCode}"/>
            <cong:hidden name="bookPieceCount" id="bookPieceCount" value="${bookingPieceCount}"/>
            <cong:hidden name="bookVolumeImp" id="bookVolumeImp" value="${bookedVolumeImperial}"/>
            <cong:hidden name="bookWeigtImp" id="bookWeigtImp" value="${bookedWeightImperial}"/>
            <cong:hidden name="actPieceCount" id="actPieceCount" value="${actualPieceCount}"/>
            <cong:hidden name="actVolumeImp" id="actVolumeImp" value="${actualMeasureImp}"/>
            <cong:hidden name="actWeigtImp" id="actWeigtImp" value="${actualWeightImp}"/>
            <input type="hidden" name="actHazmat" id="actHazmat" value="${hazmat}"/>
            <input type="hidden" name="spotRateCommNo" id="spotRateCommNo" value="${spotRateCode}"/>
            <cong:hidden name="originNo" id="originNo" value=""/>
            <cong:hidden name="destinationNo" id="destinationNo" value=""/>
            <input type="hidden" name="originName" id="originName" value=""/>
            <input type="hidden" name="destinationName" id="destinationName" value=""/>
            <cong:hidden name="barrel" id="barrel" value="${barrel}"/>
            <cong:hidden name="id" id="id" value="${id}"/>
            <cong:hidden name="count" id="count" value="${count}"/>
            <cong:hidden name="ok" id="ok" value=""/>
            <cong:hidden name="okHaz" id="okHaz" value=""/>
            <cong:hidden name="status" id="status" value="${lclCommodityForm.status}"/>
            <c:set var="status" value="${lclCommodityForm.status}"/>
            <cong:hidden name="commName" id="commName" value="${lclBookingPiece.commodityType.descEn}"/>
            <cong:hidden name="unitSsId" id="unitSsId" value="${lclCommodityForm.unitSsId}"/>
            <input type="hidden" name="verifyCargo" id="verifyCargo" value="${verifyCargo}"/>
            <input type="hidden" name="transhipment" id="transhipment" value="${lclCommodityForm.transhipment}"/>
            <input type="hidden" name="shortShipOriginalState" id="shortShipOriginalState" value="${shortShipOriginalState}"/>
            <input type="hidden" name="oldDestFee" id="oldDestFee" value="${bookingExport.includeDestfees}"/>
            <cong:hidden name="destinationNo" id="destinationNo" value=""/>
            <cong:hidden name="moduleName" id="moduleName" value="${lclCommodityForm.moduleName}"/>
            <cong:hidden name="totalMeasureImp" id="totalMeasureImp"/>
            <cong:hidden name="totalWeightImp" id="totalWeightImp"/>
            <cong:hidden name="totalMeasureMet"  id="totalMeasureMet" />
            <cong:hidden name="totalWeightMet"   id="totalWeightMet" />
            <cong:hidden name="totalPieces"   id="totalPieces" />
            <cong:hidden name="actualUom"   id="actualUom" />
            <cong:hidden name="ratesValidationFlag"   id="ratesValidationFlag" />
            <input type="hidden" name="dojoCount" id="dojoCount" value="${dojoCount}">
            <input type="hidden" name="cfcl" id="cfcl" value="${cfcl}">
            <input type="hidden" name="detailListSize" id="detailListSize" value="${not empty lclBookingPiece.lclBookingPieceDetailList ? true : false}"/>
            <cong:hidden name="smallParcelFlag"   id="smallParcelFlag" value=""/>
            <cong:hidden name="methodFlag"   id="methodFlag" />
            <input type="hidden" name="loginUserId" id="loginUserId" value="${loginuser.userId}"/>
            <input type="hidden" id="loadForm"/>
            <cong:table width="98.8%" style="margin:5px 0; float:left" border="0">
                <cong:tr>
                    <cong:td styleClass="tableHeadingNew" colspan="10">
                        <cong:div styleClass="floatLeft">Commodity for File No: <span class="fileNo">${lclCommodityForm.fileNumber}</span>
                        </cong:div>
                        <cong:div style="width:13%" styleClass="floatRight">
                            Auto Convert <cong:checkbox name="autoConvert" id="autoConvert" container="NULL"/>
                        </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td valign="top">
                        <cong:table>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Tariff</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclCommodityForm.moduleName!='Imports'}">
                                            <c:choose>
                                                <c:when test="${dojoCount eq 'N' && cfcl eq 'Y'}">
                                                    <c:set var="COMMODITY_QUERY" value="CFCLCOMMODITY"/>
                                                    <c:set var="COMMODITY_CODE_QUERY" value="CFCLCOMMODITY"/>
                                                </c:when>
                                                <c:when test="${not empty lclCommodityForm.origin}">
                                                    <c:set var="COMMODITY_QUERY" value="COMMODITYTYPEFORRATES"/>
                                                    <c:set var="COMMODITY_CODE_QUERY" value="COMMODITYCODEFORRATES"/>
                                                </c:when><c:otherwise>
                                                    <c:set var="COMMODITY_QUERY" value="COMMODITY_TYPE_NAME"/>
                                                    <c:set var="COMMODITY_CODE_QUERY" value="COMMODITY_TYPE_CODE"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="COMMODITY_QUERY" value="COMMODITY_TYPE_NAME"/>
                                            <c:set var="COMMODITY_CODE_QUERY" value="COMMODITY_TYPE_CODE"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <cong:autocompletor name="commodityType" id="commodityType" template="two" fields="commodityNo,hazmat,commodityTypeId" query="${COMMODITY_QUERY}" width="500" styleClass="mandatory textlabelsBoldForTextBoxWidthhigh textLCLuppercase"
                                                        value="${lclBookingPiece.commodityType.descEn}" container="NULL" scrollHeight="200"
                                                        callback="checkCommodity();checkSameCommodity();" paramFields="originNo,destinationNo" focusOnNext="true" shouldMatch="true"/>
                                    <cong:hidden name="commodityTypeId" id="commodityTypeId"  value="${lclBookingPiece.commodityType.id}"/>
                                    <cong:hidden name="oldcommodityId" id="oldcommodityId" value="${lclBookingPiece.commodityType.id}"/>
                                    <cong:hidden name="oldcommodity" id="oldcommodity"  value="${lclBookingPiece.commodityType.descEn}"/>
                                    <img src="${path}/img/icons/iicon.png" width="16" height="16" alt="search" id="tarif" title="Search Tariff" onclick="openTariffList('${path}')"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Tariff #</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="commodityNo" id="commodityNo" template="two" fields="commodityType,hazmat,commodityTypeId" query="${COMMODITY_CODE_QUERY}" width="500" styleClass="text weight textlabelsBoldForTextBoxWidthShort"
                                                        value="${lclBookingPiece.commodityType.code}" container="NULL" callback="checkCommodity();checkSameCommodity();"
                                                        scrollHeight="200" focusOnNext="true" shouldMatch="true" />
                                    <cong:hidden name="oldTariffNo" id="oldTariffNo"  value="${lclBookingPiece.commodityType.code}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Harmonized Code</cong:td>
                                <cong:td>
                                    <cong:text id="harmonizedCode" name="harmonizedCode" styleClass="text textlabelsBoldForTextBoxWidthShort" value="${lclBookingPiece.harmonizedCode}" container="NULL" maxlength="50"/>
                                </cong:td>
                            </cong:tr>
                            <c:if test="${lclCommodityForm.moduleName eq 'Exports'}">
                                <cong:tr>
                                    <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Weighed/Measured By</cong:td>
                                    <cong:td>
                                        <cong:autocompletor name="weightVerifiedBy" id="weightVerifiedBy" template="two" fields="NULL,weightverifiedUserId" query="WAREHOUSE_ACTIVATE_LOGINNAME" width="500" styleClass="text weight textlabelsBoldForTextBoxWidthShort"
                                                            value="${lclBookingPiece.weightVerifiedBy.firstName} ${lclBookingPiece.weightVerifiedBy.lastName}" container="NULL" scrollHeight="200"  focusOnNext="true" shouldMatch="true" />
                                        <cong:hidden id="weightverifiedUserId" name="weightverifiedUserId"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">
                                        Print Labels - How Many ?
                                    </cong:td>
                                    <cong:td>
                                        <cong:text name="labelField" styleClass="text" style="width:30px"
                                                   id="labelField" maxlength="4" onkeyup="checkForNumber(this)"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">
                                        Small Parcel
                                    </cong:td>
                                    <cong:td>
                                        <input type="radio" id ="upsY" name="ups" value="true" > Yes
                                        <input type="radio" id ="upsN" name="ups" value="false"> No
                                    </cong:td>
                                </cong:tr>
                            </c:if>
                        </cong:table>
                    </cong:td>
                    <c:choose>
                        <c:when test="${lclCommodityForm.moduleName eq 'Imports'}">
                            <c:set var="padding" value="padding-left:.80cm"/>
                            <c:set var="packClas" value="mandatory"/>
                            <c:set var="labelName" value="Actual"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="labelName" value="Booked"/>
                            <c:choose>
                                <c:when test="${status=='W' || status=='WV' || status=='R' || status=='PR' || status=='L' || status=='M' && lclCommodityForm.moduleName ne 'Imports'}">
                                    <c:set var="padding" value="padding-left:0.100cm;"/>
                                    <c:set var="width" value="58%"/>
                                    <c:set var="width1" value=""/>
                                    <c:set var="width2" value="52%"/>
                                    <c:set var="packClass" value="mandatory"/>
                                    <c:set var="packClas" value=""/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="padding" value="padding-left:0.90cm;"/>
                                    <c:set var="width" value=""/>
                                    <c:set var="width1" value=""/>
                                    <c:set var="packClas" value="mandatory"/>
                                    <c:set var="packClass" value=""/>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    <cong:td colspan="2" style="width:25%">
                        <cong:table border="0">
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" id="piecetd"  style="${padding}" width="${width}">${labelName} Piece Count</cong:td>
                                <cong:td id="piecetd1"  width="${width1}">
                                    <cong:text name="bookedPieceCount" id="bookedPieceCount" styleClass="text1  floatLeft booked textlabelsBoldForTextBox" value="${bookedPieceCount}"
                                               container="NULL" onkeyup="checkForNumber(this)" onchange="calculateBookedCuft();setPackageTypeQuery(this)" maxlength="9"/>
                                    <cong:div styleClass="button-style1 details " id="bookedDim" onclick="openDetails('${path}','${id}','${editDimFlag}','booked')">DIMS
                                    </cong:div>
                                    <c:if test="${(status!='W' || lclCommodityForm.disposition != 'RCVD') && status!='WV' && status!='R' && status!='PR' && status!='L' && status!='M'}">
                                        <div>
                                            <cong:img src="${path}/img/icons/iicon.png" width="16" height="16" alt="Client" id="dimsDetails"/>
                                        </div>
                                    </c:if>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Pkg Type</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="packageType" template="one" styleClass="text1 ${packClas}  booked textlabelsBoldForTextBox textLCLuppercase"
                                                        fields="NULL,NULL,packageTypeId" width="250" query="COMMODITY_PACKAGE_TYPE" scrollHeight="200" value="${lclCommodityForm.packageType}" id="packageType"
                                                        focusOnNext="true" container="NULL" shouldMatch="true" callback="copySinPack()"/>
                                    <cong:hidden name="packageTypeId" id="packageTypeId" value="${lclBookingPiece.packageType.id}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td colspan="2">
                                    <div id="bookedField">
                                        <c:import url="/jsps/LCL/ajaxload/bookedCommodity.jsp">
                                            <c:param name="editDimFlag" value="${editDimFlag}"/>
                                            <c:param name="commObjId" value="${id}"/>
                                            <c:param name="labelName" value="${labelName}"/>
                                        </c:import>
                                    </div>
                                </cong:td></cong:tr>
                        </cong:table>
                    </cong:td>
                    <c:if test="${lclCommodityForm.moduleName ne 'Imports'}">
                        <cong:td colspan="2" width="25%">
                            <cong:table border="0" id="actualRow">
                                <cong:tr>
                                    <cong:td styleClass="td textlabelsBoldforlcl"  style="${padding}" width="${width2}">Actual Piece Count</cong:td>
                                    <cong:td  width="${width}">
                                        <cong:text name="actualPieceCount" id="actualPieceCount" styleClass="text1 floatLeft textlabelsBoldForTextBox" value="${actualPieceCount}" container="NULL" onkeyup="checkForNumberAndDecimal(this)" onchange="calculateActualCuft();setPackageTypeQuery(this);" maxlength="9"/>
                                        <cong:div styleClass="button-style1 details " id="actulDims" onclick="openDetails('${path}','${id}','','actual')">DIMS
                                        </cong:div>
                                        <div>
                                            <c:if test="${status=='WV' || status=='W' || status=='R' || status=='PR' || status=='L' || status=='M'}">
                                                <cong:img src="${path}/img/icons/iicon.png" width="16" height="16" alt="Client" id="dimsDetails"/>
                                            </c:if>
                                        </div>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <c:choose>
                                        <c:when test="${lclCommodityForm.moduleName!='Imports'}">
                                            <c:set value="packageType" var="packageType"/>
                                            <c:set value="packageTypeId" var="packageTypeId"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set value="actualPackageType" var="packageType"/>
                                            <c:set value="actualPackageTypeId" var="packageTypeId"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Pkg Type</cong:td>
                                    <cong:td>
                                        <cong:autocompletor name="actualPackageName" template="one" styleClass="text1 ${packClass} textlabelsBoldForTextBox textLCLuppercase"
                                                            fields="NULL,NULL,actualPackageTypeId" width="250"  query="COMMODITY_PACKAGE_TYPE"  scrollHeight="200px"
                                                            value="${lclCommodityForm.actualPackageName}" id="actualPackageType" container="NULL" shouldMatch="true"
                                                            callback="copySinPack()"/>
                                        <cong:hidden name="actualPackageTypeId" id="actualPackageTypeId" value="${lclCommodityForm.actualPackageTypeId}"/>

                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td id="actualField" colspan="2">
                                        <c:import url="/jsps/LCL/ajaxload/actualCommodity.jsp"/>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                    </c:if>
                    <cong:td>
                        <cong:table>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">HaZmat</cong:td>
                                <cong:td  styleClass="textBoldforlcl">
                                    <input type="hidden" name="hazmat1" id="hazmat" />
                                    <%-- <input type="hidden" name="duphazmat" id="duphazmat" value="${lclBookingPiece.hazmat}"/> --%>
                                    <input type="hidden" name="existHazmat" id="existHazmat" value="${lclBookingPiece.hazmat}"/>
                                    <input type="radio" name="hazmat" id="hazmatY" class="hazYes" value="Y"
                                           ${lclBookingPiece.hazmat ? 'checked' :''} onclick="calculateHazmatRates()"/>Yes
                                    <input type="radio" name="hazmat" id="hazmatN" class="hazNo"
                                           value="N" ${!lclBookingPiece.hazmat ? 'checked' :""} onclick="calculateHazmatRates()"/> No
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Personal Effects/HHG </cong:td>
                                <cong:td  styleClass="textBoldforlcl">
                                    <cong:radio value="Y" name="personalEffects" container="NULL"/> Yes
                                    <cong:radio value="N" name="personalEffects" container="NULL"/> No
                                    <cong:radio value="L" name="personalEffects" container="NULL"/> L
                                </cong:td>
                            </cong:tr>
                            <c:choose>
                                <c:when test="${lclCommodityForm.moduleName=='Imports' && lclCommodityForm.transhipment=='N'}">

                                </c:when>
                                <c:otherwise>
                                    <cong:tr>
                                        <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Refrigeration </cong:td>
                                        <cong:td  styleClass="textBoldforlcl">
                                            <c:choose>
                                                <c:when test="${lclBookingPiece.refrigerationRequired==true}">
                                                    <input type="radio" name="refrigerationRequired" id="refrigerationRequired" value="Y" checked="yes"/> Yes
                                                    <input type="radio" name="refrigerationRequired" id="refrigerationRequired" value="N"/> No
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="radio" name="refrigerationRequired" id="refrigerationRequired" value="Y"/> Yes
                                                    <input type="radio" name="refrigerationRequired" id="refrigerationRequired" value="N" checked="yes"/> No
                                                </c:otherwise>
                                            </c:choose>
                                        </cong:td>
                                    </cong:tr>
                                </c:otherwise></c:choose>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Weight Verified </cong:td>
                                <cong:td styleClass="textBoldforlcl">
                                    <c:choose>
                                        <c:when test="${lclBookingPiece.weightVerified==true}">
                                            <input type="radio" name="weightVerified" id="weightVerified" value="Y" checked="yes"/> Yes
                                            <input type="radio" name="weightVerified" id="weightVerified" value="N"/> No
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="weightVerified" id="weightVerified" value="Y"/> Yes
                                            <input type="radio" name="weightVerified" id="weightVerified" value="N" checked="yes"/> No
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                            <c:if test="${lclCommodityForm.moduleName!='Imports' && lclCommodityForm.transhipment!='N'}">
                                <cong:tr>
                                    <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">
                                        Include Dest Fees
                                    </cong:td>
                                    <cong:td styleClass="textBoldforlcl">
                                        <c:choose><c:when test="${bookingExport.includeDestfees}">
                                                <input type="radio" name="includeDestfees" id="includeDestY" value="Y" checked>Yes
                                                <input type="radio" name="includeDestfees" id="includeDestN" value="N" >No
                                            </c:when><c:otherwise>
                                                <input type="radio" name="includeDestfees" id="includeDestY" value="Y"  onclick="">Yes
                                                <input type="radio" name="includeDestfees" id="includeDestN" value="N" checked >No       
                                            </c:otherwise>
                                        </c:choose> 
                                    </cong:td>
                                </cong:tr>
                            </c:if>
                            <c:choose>
                                <c:when test="${lclCommodityForm.moduleName=='Imports' && lclCommodityForm.transhipment=='N'}">

                                </c:when>
                                <c:otherwise>
                                    <cong:tr>
                                        <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Barrel Business</cong:td>
                                        <cong:td styleClass="textBoldforlcl">
                                            <c:choose>
                                                <c:when test="${lclBookingPiece.isBarrel==true}">
                                                    <input type="radio" name="isBarrel" id="barrelY" value="Y"
                                                           checked="yes" onclick="setBarrelsByPackage();"/> Yes
                                                    <input type="radio" name="isBarrel" id="barrelN" value="N" onclick="setBarrelsByPackage();"/> No
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="radio" name="isBarrel" id="barrelY" value="Y" onclick="setBarrelsByPackage();"/> Yes
                                                    <input type="radio" name="isBarrel" id="barrelN" value="N" checked="yes"
                                                           onclick="setBarrelsByPackage();"/>No
                                                </c:otherwise>
                                            </c:choose>
                                        </cong:td>
                                    </cong:tr>
                                </c:otherwise></c:choose>
                        </cong:table>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:table>
                <cong:tr>
                    <cong:td  valign="top" width="25%">
                        <c:if test="${lclCommodityForm.moduleName eq 'Exports' && not empty lclCommodityForm.fileNumber}">
                            <cong:table>
                                <cong:td styleClass="textBoldforlcl">
                                    &nbsp;&nbsp;&nbsp;Over/Short/Damage
                                    <cong:radio value="Y" name="overShortdamaged" id="overShortdamagedY" container="NULL" onclick="displayOsdBox();"/> Yes
                                    <cong:radio value="N" name="overShortdamaged" id="overShortdamagedN" container="NULL"
                                                onclick="deleteOsdRemarks('Are you sure you want to delete OSD Remarks?','${lclCommodityForm.fileNumberId}');"/> No
                                </cong:td>
                                <cong:tr styleClass="${hideShow}">
                                    <cong:td>
                                        <div id='osdRemarksId'>
                                            <cong:textarea rows="4" cols="30"  id="osdRemarks" name="osdRemarks"
                                                           value="${lclCommodityForm.osdRemarks}" style="resize:none;text-transform: uppercase; border: 1px solid #C4C5C4;" >
                                            </cong:textarea>
                                        </div>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </c:if>
                    </cong:td>
                    <cong:td width="20%"  >
                        <cong:table width="10%">
                        <tr class="caption"><td>MARKS AND NUMBERS</td></tr>
                        <tr>
                            <cong:td >
                                <%--   <cong:textarea rows="8" cols="67" name="markNoDesc" id="markNoDesc" value="${lclBookingPiece.markNoDesc}" styleClass="width:99%" container="NULL" style="text-transform: uppercase"/>--%>

                                <html:textarea styleClass="textlabelsBoldForTextBox " property="markNoDesc" styleId="markNoDesc" value="${lclBookingPiece.markNoDesc}"
                                               style="text-transform: uppercase; height: 140px; width: 178px"  rows="12" cols="37"  />
                            </cong:td>
                        </tr>
                    </cong:table>
                </cong:td>

                <cong:td width="34%">
                    <table width="30%">
                        <tr class="caption"><td>COMMODITY DESC
                                <img alt="remarks"  align="middle" id="predefinedRemarks-img"  src="${path}/img/icons/display.gif" title="Predefined Remarks LCLI" onclick="getPredefinedRemarks('${path}', 'true', '#predefinedRemarks-img');"/>
                            </td></tr>
                        <tr>
                            <td>
                                <%-- <cong:textarea rows="8" cols="67" name="pieceDesc" id="pieceDesc" value="${lclBookingPiece.pieceDesc}" styleClass="width:99%" container="NULL" style="text-transform: uppercase"/>--%>

                                <html:textarea styleClass="textlabelsBoldForTextBox" property="pieceDesc" styleId="pieceDesc" value="${lclBookingPiece.pieceDesc}"
                                               style="text-transform: uppercase;height: 140px;width: 345px" rows="12"  cols="73"/>
                            </td>
                        </tr>
                    </table>
                </cong:td>
                <cong:td width="40%" valign="top">
                    <c:if test="${lclCommodityForm.moduleName eq 'Exports' && not empty lclCommodityForm.fileNumber}">
                        <cong:table border="0">
                            <cong:tr>
                                <cong:td width="5%" style="float:left" valign="top" styleClass="${hideShow} textlabelsBoldforlcl">HOT Codes
                                    <span class="button-style2" id="addhot" onclick="showBlock('#hotCodesBox')">Add</span>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td>
                                    <div id="hotCodesBox" class="smallInputPopupBox">
                                        <cong:autocompletor name="hotCodes" id="hotCodes" fields="NULL,NULL,NULL,NULL,genCodefield1" query="CONCAT_HOTCODE_TYPE"  width="300"
                                                            shouldMatch="true" styleClass="text" container="NULL" template="concatHTC" scrollHeight="200px" value="" callback="checkValidHotCode('hotCodes','hotCode');"/>
                                        <input type="hidden" id="genCodefield1" name="genCodefield1"/>
                                        <input type="button" class="button-style3" style="max-width:70%" value="Add"
                                               onclick="addHotCode3pRef('${lclBookingPiece.lclFileNumber.id}')"/>
                                    </div>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td id="hotCodesList">
                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/hotcodeList.jsp"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </c:if>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  valign="top">
                    <input type="button"  value="Save" class="button-style1" id="saveCommodity"/>
                    <input type="button" value="Cancel" id="commodityCancel" class="cancelBut button-style1"
                           onClick="cancelCommodity()"/>
                </cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td  valign="top">
                    <c:if test="${lclCommodityForm.moduleName eq 'Exports'  && not empty lclCommodityForm.fileNumber}">
                        <cong:table border="0">
                            <cong:tr>
                                <cong:td width="5%" style="float:left" valign="top" styleClass="${hideShow} textlabelsBoldforlcl">
                                    &nbsp;&nbsp;&nbsp;&nbsp;Tracking#&nbsp;&nbsp;
                                    <span class="button-style2" id="addtrack" onclick="showBlock('#trackingBox')">Add</span>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td>
                                    <div id="trackingBox" class="smallInputPopupBox">
                                        <cong:text name="tracking" id="tracking" maxlength="25" styleClass="text" style="text-transform:uppercase" value=""/>
                                        <input type="button" class="button-style3" value="Add" onclick="submitTracking('#trackingBox', 'addHotCodeTrackingComm', '${lclBookingPiece.lclFileNumber.id}', '#trackingList', 'tracking')"/>
                                    </div>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td id="trackingList">
                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/trackingList.jsp" />
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </c:if>
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:form>
</cong:div>

<div id="add-hotCodeComments-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
    <table class="table" style="margin: 2px;width: 598px;">
        <tr>
            <th>
                <div class="float-left">
                    <label id="headingComments"></label>
                </div>
            </th>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
        <input type="hidden" name="hiddenDeleteHotCodeFlag" id="hiddenDeleteHotCodeFlag"/>
        <input type="hidden" name="3pRefId" id="3pRefId"/>
        <td class="label">
            <textarea id="hotCodeComments" name="hotCodeComments" cols="85" rows="5" class="textBoldforlcl"
                      style="resize:none;text-transform: uppercase"></textarea>
        </td>
        </tr>
        <tr>
            <td align="center">
                <input type="button"  value="Save" id="saveHotCode"
                       align="center" class="button" onclick="addHotCodeXXX3pRef();"/>
                <input type="button"  value="Cancel" id="cancelHotCode"
                       align="center" class="button" onclick="cancelHotCodeXXXComments();"/>
            </td>
        </tr>
    </table>
</div>
<style type="text/css">
    .pane{
        width: 100%;
        float: left;
        background: #fff;
    }
    a{
        outline : none;
    }

    .htabs{
        background: url(${path}/images/second-menu.gif) repeat-x;
        float: left;
        width: 100%;
        height: 27px;
        margin: 0;
    }

    .htabs li{
        float: left;
        margin: 0;
        height: 25px;
        line-height: 27px;
        list-style: none;
        padding: 0;
        font-size: 13px;
        color: #000;
        text-align: center;
        border-right:1px solid #568ea1;
    }
    .htabs li a{
        color:#000;
        text-decoration: none;
        padding:0 10px;
    }
    .htabs li:hover{
        background:url(${path}/images/second-menu-selected.gif) repeat-x;
    }

    .htabs li.selected{
        height: 25px;
        background:url(${path}/images/second-menu-selected.gif) repeat-x;
    }

    .htabs li.selected a{
        color:#000;
    }
    #container{
        border:1px solid #137587;
        float:left;
        margin:0 0 0 0%;
        width:100%;
    }
</style>
<script type="text/javascript">
    function barrelBusiness() {//removed
        //  alert("inside barrelBusiness ()");
        var fileStatus = $('#fileStatus').val() !== "" ? $('#fileStatus').val() : 'B';
        var isBarrel = $("input[name='isBarrel']:checked").val();
        if (isBarrel === 'Y') {
            // alert("isBarrel==='Y' 2nd condition");
            if (fileStatus != 'B' && fileStatus != 'WU') {
                //     alert("fileStatus!='B' && fileStatus!='WU' actual dim hide ");
                $("#actulDims").show();
                $(".actualBarrel").removeClass("text-readonly");
                $(".actualBarrel").removeAttr("readonly");
                disableBox("packageType")
                disableBox("actualPackageType")
            }
            if (fileStatus == 'B' || fileStatus == 'WU') {

                //  alert("fileStatus=='B' && fileStatus=='WU' booked dim hide ");
                disableBox("packageType")
                $("#bookedDim").show();
                $('#piecetd').attr("width", "");
                $('#piecetd1').attr("width", "");
                $(".bookedBarrel").removeClass("text-readonly");
                $(".bookedBarrel").removeAttr("readonly");
                //             $("#toggle-measure").hide();
                //                $(".bookedBarrel").addClass("text-readonly");
                //                $(".bookedBarrel").attr("readonly", true);
                //                $('#piecetd').attr("width", "53%");
                //                $('#piecetd1').attr("width", "57%");
            }
        } else {
            bookPieceCount();
        }
    }
    function setBarrelsByPackage() {
        var fileStatus = $('#fileStatus').val() === '' ? 'B' : $('#fileStatus').val();
        var isBarrel = $("input[name='isBarrel']:checked").val();
        if (isBarrel == "Y") {
            var actualPiece = $("#actualPieceCount").val();
            var bookedPiece = $("#bookedPieceCount").val();
            $.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "displayPackageType",
                    param1: "BARREL"
                },
                success: function (data) {
                    $('#packageType').val(data.substring(0, data.indexOf('==')));
                    $('#packageTypeId').val(data.substring(data.indexOf('==') + 2));
                    if ("" != fileStatus && fileStatus !== 'B' && fileStatus != 'WU') {
                        $('#actualPackageTypeId').val(data.substring(data.indexOf('==') + 2));
                        $('#actualPackageType').val(data.substring(0, data.indexOf('==')));
                        $("#actualVolumeImperial").val(parseFloat(actualPiece * 14).toFixed(3));
                        $("#actualVolumeMetric").val(parseFloat((actualPiece * 14) / 35.3146).toFixed(3));
                        disableBox("actualPackageType")
                    }
                    disableBox("packageType")
                    $("#bookedVolumeImperial").val(parseFloat(bookedPiece * 14).toFixed(3));
                    $("#bookedVolumeMetric").val(parseFloat((bookedPiece * 14) / 35.3146).toFixed(3));
                    barrelBusiness();
                }
            });
        } else {
            bookPieceCount();
            $('#piecetd').attr("width", "");
            $('#piecetd1').attr("width", "");
            $(".bookedBarrel").removeClass("text-readonly");
            $(".bookedBarrel").removeAttr("readonly");
            if (fileStatus !== 'B' && fileStatus != 'WU') {
                enableBox("actualPackageType");
                $("#actulDims").show();
                $(".actualBarrel").removeClass("text-readonly");
                $(".actualBarrel").removeAttr("readonly");
                $("#actualPackageType").val('');
                $("#actualPackageTypeId").val('');
            } else {
                $("#bookedDim").show();
                enableBox("packageType");
                $("#toggle-measure").show();
                clearPkgType();
            }
        }
    }
    jQuery(document).ready(function () {
        $("[title != '']").not("link").tooltip();
        if ($('#moduleName').val() === 'Exports') {
            parent.$('input:radio[name=ups]:checked').val(parent.$("#ups").val());
            var dispo = parent.$("#exportDisposition").val();
            var overShortDamage = parent.$('input:radio[name=overShortdamaged]:checked').val();
            var osdRemark = parent.$("#osdRemarks").val();
            if (overShortDamage === 'Y') {
                $("#overShortdamagedY").attr('checked', true);
                $("#osdRemarks").val(osdRemark);
                $("#osdRemarksId").show();
            } else {
                $("#overShortdamagedN").attr('checked', true);
                $("#osdRemarks").val(osdRemark);
                $("#osdRemarksId").hide();
            }

            $("#labelField").keyup(function (e) {
                if (parseInt($('#labelField').val()) === 0) {
                    $.prompt("Label Copy cannot be 0");
                    $('#labelField').val('');
                    return false
                }
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.logiware.common.dao.PropertyDAO",
                        methodName: "getProperty",
                        param1: "LabelPrintCount",
                        dataType: "json"
                    },
                    success: function (data) {
                        if (parseInt($('#labelField').val()) > data) {
                            $.prompt("Labels Copy should be less than <span style=color:red>" + data + "</span>");
                            $('#labelField').val('');
                        }
                    }
                });
            });

            var pcBoth = parent.$('input:radio[name=pcBoth]:checked').val();
            $("#pcBoth").val(pcBoth);
            var calcHeavy = parent.$('input:radio[name=calcHeavy]:checked').val();
            $("#calcHeavy").val(calcHeavy);
            var insurance = parent.$('input:radio[name=insurance]:checked').val();
            $("#insurance").val(insurance);
            var valueOfGoods = parent.$('#valueOfGoods').val();
            $("#valueOfGoods").val(valueOfGoods);
            $("#billToParty").val(parent.$('input:radio[name=billForm]:checked').val());
        } else {
            $("#billToParty").val(parent.$('input:radio[name=billtoCodeImports]:checked').val());
            $("#billingType").val(parent.$('input:radio[name=pcBothImports]:checked').val());
            $("#agentNo").val(parent.$('#supplierCode').val());
            $("#impCfsWarehsId").val(parent.$('#impCfsWarehsId').val());
        }
        var org = parent.$("#portOfOriginR").val();
        var orgName = org.substring(0, org.indexOf('/'));
        $("#originName").val(orgName);
        var dest = parent.$("#finalDestinationR").val();
        var destName = dest.substring(0, dest.indexOf('/'));
        $("#destinationName").val(destName);
        var pol = parent.$("#portOfLoading").val();
        var polUnLoc = pol.substring(pol.lastIndexOf('(') + 1, pol.lastIndexOf(')'));
        $("#polId").val(polUnLoc);
        var pod = parent.$("#portOfDestination").val();
        var podUnLoc = pod.substring(pod.lastIndexOf('(') + 1, pod.lastIndexOf(')'));
        $("#podId").val(podUnLoc);
        var delMetro = parent.$('input:radio[name=deliveryMetro]:checked').val();
        $("#deliveryMetro").val(delMetro);
        disableTextFeilds();
        $('#loadForm').val($('#lclCommodityForm').serialize());
        $("#actualPieceCount").focus();
        var bookingStatus = $('#status').val();
        var moduleName = parent.$('#moduleName').val();
        if (((bookingStatus == 'W' && dispo !== 'RUNV') || bookingStatus == 'WV' || bookingStatus == 'R' || bookingStatus == 'L' || bookingStatus == 'M') && moduleName !== 'Imports') {
            $("#packageType").attr("readonly", true);
            // $("#packageType").attr("alt",'');

        }
        var bookedVolumeImperial = $("#bookedVolumeImperial").val();
        var actualVolumeImperial = $("#actualVolumeImperial").val();
        var totalMeasureImp = $("#totalMeasureImp").val();
        var smalparcelFlag = $("#smallParcelFlag").val();
        var previousImpMeasure = parent.$("#isMeasureImpChanged").val();
        var saveRemarks = parent.$("#saveRemarks").val();
        if (smalparcelFlag === 'true' && previousImpMeasure === 'true' && saveRemarks === 'true') {
            var volumeImperial = actualVolumeImperial !== '' ? actualVolumeImperial : bookedVolumeImperial;
            var remarks = "Cuft was changed from " + totalMeasureImp + " to " + volumeImperial + ", due to small parcel.";
            parent.$("#smallParcelRemarks").val(remarks);
        }
    });
    function disableTextFeilds() {//removed
        var moduleName = parent.$('#moduleName').val();
        var fileStatus = parent.$('#fileStatus').val();
        if (moduleName === 'Imports' && fileStatus !== '' && fileStatus !== 'B') {
            $('#bookedDim').attr('disabled', true);
            disableTextBox('commodityNo');
            disableTextBox('commodityType');
        }
    }
    function disableFieldByName(selector, type) {
        $('input:' + type + '[name=' + selector + ']').each(function () {
            $(this).attr("disabled", true);
        });
    }
    function disableTextBox(selector) {
        $('#' + selector).attr("readonly", true).addClass("readonly").unbind();
    }
    function clearPkgType() {
        $("#packageType").val('');
        $("#packageTypeId").val('');
    }
    function calculateBookedCuft() {
        var val = $("input[name='isBarrel']:checked").val();
        var bookedPiece = $("#bookedPieceCount").val();
        if (val == "Y") {
            $("#bookedVolumeImperial").val(parseFloat(bookedPiece * 14).toFixed(3));
            $("#bookedVolumeMetric").val(parseFloat((bookedPiece * 14) / 35.3146).toFixed(3));
        }
    }
    function calculateActualCuft() {
        var val = $("input[name='isBarrel']:checked").val();
        var actualPiece = $("#actualPieceCount").val();
        if (val == "Y") {
            $("#actualVolumeImperial").val(parseFloat(actualPiece * 14).toFixed(3));
            $("#actualVolumeMetric").val(parseFloat((actualPiece * 14) / 35.3146).toFixed(3));
        }
    }

    function showDimsDetails() {//removed
        $.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "displayDimsDetails",
                param1: $('#id').val(),
                param2:  $('#fileNumberId').val(),
                param3: $('#commodityNo').val(),
                request: "true"
            },
            success: function (data) {
                JToolTipForLeft('#dimsDetails', data, 450);
                if (data.length > 245 && parent.$('#moduleName').val() === 'Exports') {
                    $(".details").addClass('green-background');
                } else if (parent.$('#moduleName').val() === 'Exports') {
                    $(".details").addClass('button-style1');
                }
                return true;
            }
        });
    }
    
    function emptyDims() {
        $(".details").removeClass('green-background');
    }
    function cancelCommodity() {
        parent.$.fn.colorbox.close();
    }
    function showBlock(tar) {
        $('#setSacacValue').attr('checked', false);
        $('.smallInputPopupBox').hide();
        $("#amsHblNo").val("");
        $("#amsHblPiece").val("");
        $("#amsHblScac").val("");
        $(tar).show("slow");
    }

    function hideBlock(tar) {
        $(tar).hide("slow");
    }
    function  fillCommodity(pieceId) {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getLclCommodity",
                param1: pieceId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                $('#editDimFlag').val('true');
                $('input:radio[name=hazmat]').val([data.hazmat ? 'Y' : 'N']);
                $('input:radio[name=isBarrel]').val([data.isBarrel ? 'Y' : 'N']);
                $('#existHazmat').val(data.hazmat);
                $('#packageType').val(data.description);
                $('#actualPackageType').val(data.description);
                $('#id').val(data.id);
                $('#commodityTypeId').val(data.commodityId);
                $('#commodityNo').val(data.code);
                $('#commCode').val(data.code);
                $('#barrel').val(data.isBarrel);
                $('#commodityType').val(data.descEn);
                $('#markNoDesc').val(data.markNoDesc);
                $('#pieceDesc').val(data.pieceDesc);
                $('#bookedPieceCount').val(data.bookedPieceCount);
                $('#packageTypeId').val(data.packageTypeId);
                $('#actualPackageTypeId').val(data.packageTypeId);
                $('#bookedWeightImperial').val(data.bookedWeightImperial);
                $('#bookedWeightMetric').val(data.bookedWeightMetric);
                $('#bookedVolumeImperial').val(data.bookedVolumeImperial);
                $('#bookedVolumeMetric').val(data.bookedVolumeMetric);
                $('#actualPieceCount').val(data.actualPieceCount);
                $('#actualWeightImperial').val(data.actualWeightImperial);
                $('#actualWeightMetric').val(data.actualWeightMetric);
                $('#actualVolumeImperial').val(data.actualVolumeImperial);
                $('#actualVolumeMetric').val(data.actualVolumeMetric);
                $('#actPieceCount').val(data.actualPieceCount);
                $('#actVolumeImp').val(data.actualVolumeImperial);
                $('#actWeigtImp').val(data.actualWeightImperial);
                $('.booked').addClass("text-readonly");
                $('.booked').attr("readonly", true);
                $('#bookedDim').hide();
            }
        });
    }
</script>
</body>
