<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="/taglib.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclQuoteCommodity.js"/>
<cong:javascript src="${path}/js/jquery/jquery.util.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<body>
    <cong:div style="width:99%;float:left;">
        <cong:form  id="lclQuoteCommodityForm"  name="lclQuoteCommodityForm" action="/lclQuoteCommodity.do" >
            <cong:hidden name="moduleName" id="moduleName" value="${lclQuoteCommodityForm.moduleName}"/>
            <cong:hidden name="methodName" id="methodName" value=""/>
            <cong:hidden  name="dimsToolTip" id="dimsToolTip" value="${lclQuoteCommodityForm.dimsToolTip}"/>
            <input type="hidden" name="billingType" id="billingType" value="${lclQuoteCommodityForm.billingType}"/>
            <input type="hidden" name="billToParty" id="billToParty" value="${lclQuoteCommodityForm.billToParty}"/>
            <input type="hidden" name="transhipment" id="transhipment" value="${lclQuoteCommodityForm.transhipment}"/>
            <input type="hidden" name="originName" id="originName" value=""/><%-- originName --%>
            <input type="hidden" name="destinationName" id="destinationName" value=""/><%-- destinationName --%>
            <cong:hidden name="originUnlocCode" id="originUnlocCodeId" value="${lclQuoteCommodityForm.originUnlocCode}"/><%-- originId --%>
            <cong:hidden name="fdUnlocCode" id="fdUnlocCodeId" value="${lclQuoteCommodityForm.fdUnlocCode}"/><%-- destinationId --%>
            <cong:hidden name="rateType" id="rateType" value="${lclQuoteCommodityForm.rateType}"/>
            <cong:hidden name="polUnlocCode" id="polUnlocCodeId" value="${lclQuoteCommodityForm.polUnlocCode}"/><%-- polId --%>
            <cong:hidden name="podUnlocCode" id="podUnlocCodeId" value="${lclQuoteCommodityForm.podUnlocCode}"/><%-- podId --%>

            <cong:hidden name="ratesRecalFlag" id="ratesRecalFlag"/>
            <cong:hidden name="calcHeavy" id="calcHeavy"/>
            <cong:hidden name="deliveryMetro" id="deliveryMetro"/>
            <cong:hidden name="pcBoth" id="pcBoth"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclQuote.lclFileNumber.id}"/>
            <cong:hidden name="fileNumber" id="fileNumber" value="${lclQuoteCommodityForm.fileNumber}"/>

            <cong:hidden name="editDimFlag" id="editDimFlag" value="${editDimFlag}"/>
            <input type="hidden" name="countVal" id="countVal" value="${countVal}"/>




            <input type="hidden" name="bookVolumeImp" id="bookVolumeImp" value="${lclQuotePiece.bookedVolumeImperial}"/>
            <input type="hidden" name="bookWeigtImp" id="bookWeigtImp" value="${lclQuotePiece.bookedWeightImperial}"/>
            <input type="hidden" name="oldBkgWeightMetric" id="oldBkgWeightMetric" value="${lclQuotePiece.bookedWeightMetric}"/>
            <input type="hidden" name="oldBkgVolumeMetric" id="oldBkgVolumeMetric" value="${lclQuotePiece.bookedVolumeMetric}"/>
            <input type="hidden" name="oldBarrel" id="oldBarrel" value="${lclQuotePiece.isBarrel}"/>
            <input type="hidden" name="oldHazmat" id="oldHazmat" value="${lclQuotePiece.hazmat}"/>
            <input type="hidden" name="oldDestFee" id="oldDestFee" value="${bookingExport.includeDestfees}"/>
            <input type="hidden" name="oldCommNo" id="oldCommNo" value="${lclQuotePiece.commodityType.code}"/>
            <input type="hidden" name="oldPieceCount" id="oldPieceCount" value="${lclQuotePiece.bookedPieceCount}"/>
            <cong:hidden name="originNo" id="originNo" value="${lclQuoteCommodityForm.originNo}"/>
            <cong:hidden name="destinationNo" id="destinationNo" value="${lclQuoteCommodityForm.destinationNo}"/>
            <input type="hidden" name="agentNo" id="agentNo" value="${lclQuoteCommodityForm.agentNo}"/>
            <input type="hidden" name="dojoCount" id="dojoCount" value="${dojoCount}">
            <input type="hidden" name="cfcl" id="cfcl" value="${cfcl}">
            <cong:hidden name="quotePieceId" id="quotePieceId" value="${lclQuoteCommodityForm.quotePieceId}"/><%--id--%>
            <input type="hidden" name="QtdetailListSize" id="QtdetailListSize" value="${not empty lclQuotePiece.lclQuotePieceDetailList ? true : false}"/>
            <input type="hidden" id="loadForm"/>
            <cong:hidden name="smallParcelFlag" id="smallParcelFlag" value="false"/>
            <cong:hidden name="smalParcelMeasureImp" id="smalParcelMeasureImp"  />
            <cong:hidden name="smallParcelRemarks" id="smallParcelRemarks"  />

            <cong:table width="98.8%" style="margin:5px 0; float:left" border="0">
                <cong:tr>
                    <cong:td styleClass="tableHeadingNew" colspan="4">
                        <cong:div styleClass="floatLeft">Commodity for File No: <span class="fileNo">${lclQuoteCommodityForm.fileNumber}</span>
                        </cong:div>
                        <cong:div styleClass="floatRight" style="width:13%">
                            Auto Convert <cong:checkbox name="autoConvert" id="autoConvert" container="NULL" />
                        </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  valign="top">
                        <cong:table>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Tariff</cong:td>
                                <c:choose>
                                    <c:when test="${dojoCount eq 'N' && cfcl eq 'Y'}">
                                        <c:set var="COMMODITY_QUERY" value="CFCLCOMMODITY"/>
                                        <c:set var="COMMODITY_CODE_QUERY" value="CFCLCOMMODITY"/>
                                    </c:when>
                                    <c:when test="${lclQuoteCommodityForm.moduleName ne 'Imports' && not empty lclQuoteCommodityForm.originNo  && not empty lclQuoteCommodityForm.destinationNo}">
                                        <c:set var="COMMODITY_QUERY" value="COMMODITYTYPEFORRATES"/>
                                        <c:set var="COMMODITY_CODE_QUERY" value="COMMODITYCODEFORRATES"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="COMMODITY_QUERY" value="COMMODITY_TYPE_NAME"/>
                                        <c:set var="COMMODITY_CODE_QUERY" value="COMMODITY_TYPE_CODE"/>
                                    </c:otherwise>
                                </c:choose>
                                <cong:td>
                                    <cong:autocompletor name="commodityType" id="commodityType" template="two" fields="commodityNo,commodityHazmat,commodityTypeId" query="${COMMODITY_QUERY}"
                                                        width="500" styleClass="textlabelsBoldForTextBoxWidthhigh mandatory"
                                                        value="${lclQuotePiece.commodityType.descEn}" container="NULL" paramFields="originNo,destinationNo"
                                                        callback="checkCommodity();checkSameCommodity();commodityDesc();" shouldMatch="true"
                                                        focusOnNext="true" scrollHeight="200px"/>
                                    <cong:hidden name="commodityTypeId" id="commodityTypeId"  value="${lclQuotePiece.commodityType.id}"/>
                                    <cong:hidden name="oldcommodityTypeId" id="oldcommodityTypeId"  value="${lclQuotePiece.commodityType.id}"/>
                                    <cong:hidden name="oldcommodity" id="oldcommodity"  value="${lclQuotePiece.commodityType.descEn}"/>
                                    <img src="${path}/img/icons/iicon.png" width="16" height="16" style="vertical-align: middle"
                                         alt="search" title="Search Tariff" id="tarif" onclick="openTariffPopUp('${path}')"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Tariff #</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="commodityNo" id="commodityNo" template="two" fields="commodityType,NULL,commodityHazmat,commodityTypeId"
                                                        query="${COMMODITY_CODE_QUERY}" width="500" styleClass="textlabelsBoldForTextBoxWidthShort weight"
                                                        value="${lclQuotePiece.commodityType.code}" container="NULL"
                                                        callback="checkCommodity();checkSameCommodity();" shouldMatch="true"
                                                        readOnly="true" scrollHeight="200px"/>
                                    <cong:hidden name="commodityTypeId" id="commodityTypeId"  value="${lclQuotePiece.commodityType.id}"/>
                                    <cong:hidden name="oldTariffNo" id="oldTariffNo"  value="${lclQuotePiece.commodityType.code}"/>
                                    <input type="hidden" name="commodityHazmat" id="commodityHazmat" />
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Harmonized Code </cong:td>
                                <cong:td>
                                    <cong:text id="harmonizedCode" name="harmonizedCode" 
                                               styleClass="textlabelsBoldForTextBoxWidthShort"
                                               value="${lclQuotePiece.harmonizedCode}" container="NULL" maxlength="50"/>
                                </cong:td>
                            </cong:tr>
                            <c:if test="${lclQuoteCommodityForm.moduleName  ne 'Imports'}">
                                <cong:tr>
                                    <cong:td valign="middle" styleClass="textlabelsBoldforlcl">Small Parcel</cong:td>
                                    <cong:td>
                                        <input type="radio" id ="upsY" name="ups" value="true"> Yes
                                        <input type="radio" id ="upsN" name="ups" value="false"> No
                                    </cong:td>
                                </cong:tr>
                            </c:if>
                        </cong:table>
                    </cong:td>
                    <c:choose>
                        <c:when test="${lclQuoteCommodityForm.moduleName  eq 'Imports'}">
                            <c:set var="padding" value="padding-left:1cm"/>
                            <c:set var="labelName" value="Actual"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="padding" value="padding-left:.90cm;"/>
                            <c:set var="labelName" value="Booked"/>
                        </c:otherwise>
                    </c:choose>
                    <cong:td colspan="2">
                        <cong:table border="0">
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" id="piecetd" style="${padding}">${labelName} Piece Count</cong:td>
                                <cong:td id="piecetd1">
                                    <cong:text name="bookedPieceCount" id="bookedPieceCount" styleClass="text1 floatLeft booked textlabelsBoldForTextBox" value="${lclQuotePiece.bookedPieceCount}"
                                               container="NULL" onkeyup="checkForNumber(this)"
                                               onchange="setPackageTypeQuery(this);calculateBarrelBookedCuft();" maxlength="9"/>
                                    <cong:div styleClass="button-style1 details " id="bookedDim" 
                                              onclick="openDetails('${path}','${editDimFlag}','booked')">DIMS
                                    </cong:div>
                                    <div>
                                        <cong:img src="${path}/img/icons/iicon.png" width="16" height="16" alt="Client" id="dimsDetails"/>
                                    </div>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Pkg Type</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="packageType" template="one" styleClass="text1 mandatory textlabelsBoldForTextBox textLCLuppercase"
                                                        fields="NULL,NULL,packageTypeId" width="250"  query="COMMODITY_PACKAGE_TYPE" scrollHeight="200px"
                                                        value="${lclQuoteCommodityForm.packageType}" id="packageType" container="NULL" shouldMatch="true"
                                                        focusOnNext="true"/>
                                    <cong:hidden name="packageTypeId" id="packageTypeId" value="${lclQuoteCommodityForm.packageTypeId}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td colspan="2">
                                    <div id="bookedField">
                                        <c:import url="/jsps/LCL/ajaxload/bookedCommodity.jsp">
                                            <c:param name="editDimFlag" value="${editDimFlag}"/>
                                            <c:param name="quotePieceId" value="${lclQuoteCommodityForm.quotePieceId}"/>
                                            <c:param name="labelName" value="${labelName}"/>
                                        </c:import>
                                    </div>
                                </cong:td></cong:tr>
                        </cong:table>
                    </cong:td>
                    <%-- <cong:td id="actualTable" style="display:none">
            <cong:div id="actualField">
                <c:import url="/jsps/LCL/ajaxload/actualCommodity.jsp"/>
            </cong:div>
        </cong:td> --%>
                    <cong:td >
                        <cong:table>
                            <cong:tr styleClass="textlabelsBoldforlcl">
                                <cong:td valign="middle">HaZmat</cong:td>
                                <cong:td style="float:left">
                                    <c:choose>
                                        <c:when test="${lclQuotePiece.hazmat==true}">
                                            <input type="radio" name="hazmat" id="hazmatY" class="hazYes" value="Y" checked="yes" onclick="calculateHazRates()"/> Yes
                                            <input type="radio" name="hazmat" id="hazmatN" class="hazNo" value="N" onclick="calculateHazRates()"/> No
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="hazmat" id="hazmatY" class="hazYes" value="Y" onclick="calculateHazRates()" /> Yes
                                            <input type="radio" name="hazmat" id="hazmatN" class="hazNo" value="N" checked="yes" onclick="calculateHazRates()"/> No
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                            <cong:tr styleClass="textlabelsBoldforlcl">
                                <cong:td valign="middle">Personal Effects </cong:td>
                                <cong:td style="float:left">
                                    <cong:radio value="Y" name="personalEffects" container="NULL"/> Yes
                                    <cong:radio value="N" name="personalEffects" container="NULL"/> No
                                    <cong:radio value="L" name="personalEffects" container="NULL"/> L
                                </cong:td>
                            </cong:tr>
                            <cong:tr styleClass="textlabelsBoldforlcl">
                                <cong:td  valign="middle">Refrigeration </cong:td>
                                <cong:td style="float:left">
                                    <c:choose>
                                        <c:when test="${lclQuotePiece.refrigerationRequired==true}">
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
                            <cong:tr  styleClass="textlabelsBoldforlcl">
                                <cong:td valign="middle">Weight Verified </cong:td>
                                <cong:td style="float:left">
                                    <c:choose>
                                        <c:when test="${lclQuotePiece.weightVerified==true}">
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
                            <c:if test="${lclQuoteCommodityForm.moduleName!='Imports' && lclQuoteCommodityForm.transhipment!='N'}">
                                <cong:tr>
                                    <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">
                                        Include Dest Fees
                                    </cong:td>
                                    <cong:td styleClass="textBoldforlcl">
                                        <c:choose><c:when test="${bookingExport.includeDestfees}">
                                                <input type="radio" name="includeDestfees" id="includeDestY" value="Y"  checked>Yes
                                                <input type="radio" name="includeDestfees" id="includeDestN" value="N"  >No
                                            </c:when><c:otherwise>
                                                <input type="radio" name="includeDestfees" id="includeDestY" value="Y"  onclick="">Yes
                                                <input type="radio" name="includeDestfees" id="includeDestN" value="N" checked >No       
                                            </c:otherwise>
                                        </c:choose> 
                                    </cong:td>
                                </cong:tr>
                            </c:if>
                            <cong:tr styleClass="textlabelsBoldforlcl">
                                <cong:td  valign="middle">Barrel Business</cong:td>
                                <cong:td style="float:left">
                                    <c:choose>
                                        <c:when test="${lclQuotePiece.isBarrel==true}">
                                            <input type="radio" name="isBarrel" id="barrelY" value="Y" checked="yes" onclick="setBarrelPkgDetails();"/> Yes
                                            <input type="radio" name="isBarrel" id="barrelN" value="N" onclick="clearPkgType()"/> No
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="isBarrel" id="barrelY" value="Y" onclick="setBarrelPkgDetails();"/> Yes
                                            <input type="radio" name="isBarrel" id="barrelN" value="N" checked="yes" onclick="clearPkgType()"/>No
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:table border="0">
                <cong:tr>
                    <cong:td  valign="top" width="25%">
                        <c:if test="${lclQuoteCommodityForm.moduleName eq 'Exports' && not empty lclQuoteCommodityForm.fileNumber}">
                            <cong:table border="0">
                                <cong:tr styleClass="${hideShow} textBoldforlcl">
                                    <cong:td>&nbsp;&nbsp;&nbsp;Over/Short/Damaged 
                                        <cong:radio value="Y" name="overShortdamaged" id="overShortdamagedY" container="NULL" 
                                                    onclick="displayOsdBox();"/> Yes
                                        <cong:radio value="N" name="overShortdamaged" id="overShortdamagedN" container="NULL"
                                                    onclick="deleteOsdRemarks('Are you sure you want to delete OSD Remarks?', '${lclQuotePiece.lclFileNumber.id}');"/> No
                                    </cong:td>
                                </cong:tr>
                                <cong:tr styleClass="${hideShow}">
                                    <cong:td>
                                        <div id="osdRemarksTextAreaId">
                                            <cong:textarea rows="4" cols="30"  id="osdRemarks" name="osdRemarks" value="${lclQuoteCommodityForm.osdRemarks}"
                                                           style="text-transform: uppercase;resize:none;" onchange="osdRefresh()"></cong:textarea>
                                            </div>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </c:if>
                    </cong:td>
                    <cong:td  width="20%">
                        <cong:table width="10%">
                        <tr class="caption"><td>MARKS AND NUMBERS</td></tr>
                        <tr>
                            <cong:td>
                                <%-- <cong:textarea rows="8" cols="67" name="markNoDesc" id="markNoDesc" value="${lclQuotePiece.markNoDesc}" styleClass="width:99%" container="NULL" style="text-transform: uppercase"/>--%>
                                <html:textarea styleClass="textlabelsBoldForTextBox " property="markNoDesc" styleId="markNoDesc" value="${lclQuotePiece.markNoDesc}"
                                               style="text-transform: uppercase; height: 140px; width: 178px"  rows="12" cols="37"  />
                            </cong:td>
                        </tr>
                    </cong:table>
                </cong:td>
                <cong:td width="34%" align="left">
                    <table  width="30%">
                        <tr class="caption"><td>COMMODITY DESC</td></tr>
                        <tr>
                            <td>
                                <%-- <cong:textarea rows="8" cols="67" name="pieceDesc" id="pieceDesc" value="${lclQuotePiece.pieceDesc}" styleClass="width:99%" container="NULL" style="text-transform: uppercase"/>--%>
                                <html:textarea styleClass="textlabelsBoldForTextBox" property="pieceDesc" styleId="pieceDesc" value="${lclQuotePiece.pieceDesc}"
                                               style="text-transform: uppercase;height: 140px;width: 345px" rows="12" cols="73"  />
                            </td>
                        </tr>
                    </table>
                </cong:td>
                <cong:td width="40%" valign="top">
                    <c:if test="${lclQuoteCommodityForm.moduleName eq 'Exports' && not empty lclQuoteCommodityForm.fileNumber}">
                        <cong:table border="0">
                            <cong:tr>
                                <cong:td width="5%" valign="top" styleClass="${hideShow} textBoldforlcl">HOT Codes
                                    <span class="button-style2" id="Qaddhot" onclick="showBlock('#hotCodesBox')">Add</span>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td>
                                    <div id="hotCodesBox" class="smallInputPopupBox">
                                        <cong:autocompletor name="hotCodes" id="hotCodes" fields="NULL,NULL,NULL,NULL,genCodefield1" query="CONCAT_HOTCODE_TYPE" width="300"
                                                            shouldMatch="true" styleClass="text" container="NULL" template="concatHTC" scrollHeight="200px" value="" callback="checkValidHotCode('hotCodes','hotCode');"/>
                                        <input type="hidden" id="genCodefield1" name="genCodefield1"/>
                                        <input type="button" class="button-style3" value="Add" onclick="addHotCode3pRef('${lclQuotePiece.lclFileNumber.id}')"/>
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
                <cong:td align="center" colspan="3">
                    <input type="button"  value="Save" class="button-style1" id="saveCommodity"/>
                    <input type="button" value="Cancel" class="cancelBut button-style1" onClick="cancelCommodity()"/>
                </cong:td>
                <cong:td>
                    <c:if test="${lclQuoteCommodityForm.moduleName eq 'Exports' && not empty lclQuoteCommodityForm.fileNumber}">
                        <cong:table border="0">
                            <cong:tr>
                                <cong:td width="5%" valign="top" styleClass="${hideShow} textBoldforlcl">
                                    &nbsp;&nbsp;&nbsp;&nbsp;Tracking#
                                    &nbsp;&nbsp;<span class="button-style2" id="Qaddtrack" onclick="showBlock('#trackingBox')">Add</span>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td>
                                    <div id="trackingBox" class="smallInputPopupBox">
                                        <cong:text name="tracking" id="tracking" styleClass="text" style="text-transform: uppercase" maxlength="25" value=""/>
                                        <input type="button" class="button-style3" value="Add" onclick="addTracking3pRef('${lclQuotePiece.lclFileNumber.id}')"/>
                                    </div>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td id="trackingList">
                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/trackingList.jsp"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </c:if>
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:form>
</cong:div>
</body>
