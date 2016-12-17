<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="/taglib.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclBlCommodity.js"/>
<cong:javascript src="${path}/js/jquery/jquery.util.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>	
<body>
    <cong:div style="width:99%;float:left;">
        <cong:form  id="lclBlCommodityForm"  name="lclBlCommodityForm" action="lclBlCommodity.do" >
            <cong:hidden name="insurance" id="insurance"/>
            <cong:hidden name="valueOfGoods" id="valueOfGoods"/>
            <cong:hidden name="pcBoth" id="pcBoth"/>
            <cong:hidden name="moduleName" id="moduleName" value="${lclCommodityForm.moduleName}"/>
            <cong:hidden name="calcHeavy" id="calcHeavy"/>
            <cong:hidden name="methodName" id="methodName" value=""/>
            <cong:hidden name="blPieceId" id="blPieceId" value="${lclBlCommodityForm.blPieceId}"/><%--blpieceId--%>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBlCommodityForm.fileNumberId}"/><%--fileId--%>
            <cong:hidden name="fileNumber" id="fileNumber" value="${lclBlCommodityForm.fileNumber}"/><%--fileNumber--%>
            <cong:hidden name="pooUnlocCode" id="pooUnlocCode" value="${lclBlCommodityForm.pooUnlocCode}"/><%--pooUnloccode--%>
            <cong:hidden name="fdUnlocCode" id="fdUnlocCode" value="${lclBlCommodityForm.fdUnlocCode}"/><%--fdUnloccode--%>
            <cong:hidden name="polUnlocCode" id="polUnlocCode" value="${lclBlCommodityForm.polUnlocCode}"/><%--polUnLocCode--%>
            <cong:hidden name="podUnlocCode" id="podUnlocCode" value="${lclBlCommodityForm.podUnlocCode}"/><%--podUnlocode--%>
            <cong:hidden name="rateType" id="rateType" value="${lclBlCommodityForm.rateType}"/><%--ratetype--%>
            <cong:hidden name="ratesRecalFlag" id="ratesRecalFlag"/><%--Rates recalculate Flag--%>
            <cong:hidden name="dimsToolTip" id="dimsToolTip"/><%--Dims ToolTip--%>

            <%--old values for Rates recalculate Flag--%>
            <input type="hidden" name="actualVolumeImp" id="oldActualVolumeImp" value="${lclBlPiece.actualVolumeImperial}"/>
            <input type="hidden" name="actualWeigtImp" id="oldActualWeigtImp" value="${lclBlPiece.actualWeightImperial}"/>
            <input type="hidden" name="actualWeightMet" id="oldActualWeightMetric" value="${lclBlPiece.actualWeightMetric}"/>
            <input type="hidden" name="actualVolumeMet" id="oldActualVolumeMetric" value="${lclBlPiece.actualVolumeMetric}"/>
            <input type="hidden" name="oldBarrel" id="oldBarrel" value="${lclBlPiece.isBarrel}"/>
            <input type="hidden" name="oldHazmat" id="oldHazmat" value="${lclBlPiece.hazmat}"/>
            <input type="hidden" name="oldCommNo" id="oldCommNo" value="${lclBlPiece.commodityType.code}"/>
            <input type="hidden" name="pieceDetailAvailable" id="pieceDetailAvailable" value="${empty lclBlPiece.lclBlPieceDetailList ? false : true}"/>
            <input type="hidden" name="roleDutyChangeBLCommodityAfterCOB" id="roleDutyChangeBLCommodityAfterCOB" value="${roleDuty.changeBLCommodityAfterCOB}"/>
            
            <cong:hidden name="autoConvertMetrics" id="autoConvertMetrics" value="${lclBlCommodityForm.autoConvertMetric}"/>
            

            <cong:hidden name="editDimFlag" id="editDimFlag" value="${editDimFlag}"/>
            <cong:hidden name="commCode" id="commCode" value="${commCode}"/>
            <cong:table width="100%" style="margin:5px 0; float:left">
                <cong:tr>
                    <cong:td styleClass="tableHeadingNew" colspan="4">
                        <cong:div styleClass="floatLeft">Commodity for File No: <span class="fileNo">${lclBlCommodityForm.fileNumber}</span>
                        </cong:div>
                        <cong:div style="width:13%" styleClass="floatRight">
                            Auto Convert <cong:checkbox name="autoConvert" id="autoConvert" container="NULL"/>
                        </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                        <cong:table>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Commodity</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="commodityType" id="commodityType" template="two" fields="commodityNo,commHazmat,commodityTypeId" query="COMMODITY_TYPE_NAME" width="500" styleClass="mandatory textlabelsBoldForTextBox textLCLuppercase"
                                                        value="${lclBlPiece.commodityType.descEn}" container="NULL"
                                                        callback="checkSameCommodity();" shouldMatch="true" scrollHeight="200px"/>
                                    <input type="hidden" name="commHazmat" id="commHazmat"/><%-- commodity hazmat--%>
                                    <input type="hidden" name="oldcommodityId" id="oldcommodityId" value="${lclBlPiece.commodityType.id}"/>
                                    <input type="hidden" name="oldcommodity" id="oldcommodity"  value="${lclBlPiece.commodityType.descEn}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Commodity#</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="commodityNo" id="commodityNo" template="two" fields="commodityType,commHazmat,commodityTypeId" query="COMMODITY_TYPE_CODE" width="500" styleClass="textlabelsBoldForTextBox textLCLuppercase weight"
                                                        value="${lclBlPiece.commodityType.code}" container="NULL" callback="checkSameCommodity();"
                                                        scrollHeight="200px" shouldMatch="true"/>
                                    <cong:hidden name="commodityTypeId" id="commodityTypeId"  value="${lclBlPiece.commodityType.id}"/>
                                    <input type="hidden" name="oldTariffNo" id="oldTariffNo"  value="${lclBlPiece.commodityType.code}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Harmonized Code </cong:td>
                                <cong:td>
                                    <cong:text id="harmonizedCode" name="harmonizedCode" styleClass="text" value="${lclBlPiece.harmonizedCode}" container="NULL" maxlength="50"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                    <cong:td>
                        <cong:table>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" id="piecetd" style="padding-left:53px">Actual Piece Count</cong:td>
                                <cong:td id="piecetd1">
                                    <cong:text name="actualPieceCount" id="actualPieceCount" styleClass="text1 floatLeft actual textlabelsBoldForTextBox commonClass"
                                               value="${actualPieceCount}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"
                                               onchange="calculateActualCuft();setPackageTypeQuery()" maxlength="9"/>
                                <cong:div styleClass="button-style1 details " id="actualDim" onclick="openDetails('${path}','${lclBlCommodityForm.blPieceId}','${editDimFlag}','actual')">DIMS
                                </cong:div>
                                <cong:img src="${path}/img/icons/iicon.png" width="16" height="16" alt="Client" id="dimsDetails"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle" style="padding-left:35px">Pkg Type</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="packageType" template="one" 
                                                        styleClass="text1 mandatory textlabelsBoldForTextBox textLCLuppercase commonClass"
                                                        fields="NULL,NULL,packageTypeId" width="300"  query="COMMODITY_PACKAGE_TYPE"
                                                        value="${lclBlCommodityForm.packageType}" id="packageType" container="NULL" shouldMatch="true"
                                                        callback="" scrollHeight="200px"/>
                                    <cong:hidden name="packageTypeId" id="packageTypeId" value="${lclBlPiece.packageType.id}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td colspan="2" id="actualTable">
                                    <div id="actualField">
                                        <c:import url="/jsps/LCL/ajaxload/actualCommodity.jsp">
                                            <c:param name="CommodityScreen" value="blcommodity"/>
                                        </c:import>
                                    </div>
                                </cong:td></cong:tr>
                        </cong:table>
                    </cong:td>
                    <cong:td >
                        <cong:table styleClass="textBoldforlcl">
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">HaZmat</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclBlPiece.hazmat==true}">
                                            <input type="radio" name="hazmat" id="hazmat" class="hazYes" value="Y" checked="yes"/>Yes
                                            <input type="radio" name="hazmat" id="hazmat" class="hazNo" value="N"/> No
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="hazmat" id="hazmat" class="hazYes" value="Y"/>Yes
                                            <input type="radio" name="hazmat" id="hazmat" class="hazNo" value="N" checked="yes"/> No
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Personal Effects/HHG </cong:td>
                                <cong:td>
                                    <cong:radio value="Y" name="personalEffects" container="NULL"/> Yes
                                    <cong:radio value="N" name="personalEffects" container="NULL"/> No
                                    <cong:radio value="L" name="personalEffects" container="NULL"/> L
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Refrigeration </cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclBlPiece.refrigerationRequired==true}">
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
                            <%--   <cong:tr>
                                   <cong:td styleClass="td" valign="middle">Weight Verified </cong:td>
                                   <cong:td>
                                       <c:choose>
                                           <c:when test="${lclBlPiece.weightVerified==true}">
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
                             <cong:tr>
                                        <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">
                                            Include Dest Fees
                                        </cong:td>
                                        <cong:td styleClass="textBoldforlcl">
                                             <c:choose><c:when test="${bookingExport.includeDestfees}">
                                            <input type="radio" name="includeDestfees" id="includeDestY" value="Y" disabled checked>Yes
                                            <input type="radio" name="includeDestfees" id="includeDestN" value="N"  disabled>No
                                            </c:when><c:otherwise>
                                            <input type="radio" name="includeDestfees" id="includeDestY" value="Y" disabled onclick="">Yes
                                            <input type="radio" name="includeDestfees" id="includeDestN" value="N" checked disabled>No       
                                                </c:otherwise>
                                            </c:choose> 
                                        </cong:td>
                                    </cong:tr> 
                     <input type="hidden" name="oldDestFee" id="oldDestFee" value="${bookingExport.includeDestfees}"/> --%>
                            <cong:tr>
                                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Barrel Business</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclBlPiece.isBarrel==true}">
                                            <input type="radio" name="isBarrel" id="barrelY" value="Y" checked="yes" onclick="setBarrelBlPkgDetails();"/> Yes
                                            <input type="radio" name="isBarrel" id="barrelN" value="N" onclick="clearBlPkgType();"/> No
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="isBarrel" id="barrelY" value="Y" onclick="setBarrelBlPkgDetails();"/> Yes
                                            <input type="radio" name="isBarrel" id="barrelN" value="N" checked="yes" onclick="clearBlPkgType();"/>No
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <table align="center" border='0' width="100%">
                <tr>
                    <td  width="15%">
                        <table width="29%" style="margin-left:270px">
                            <tr class="caption"><td>MARKS AND NUMBERS</td></tr>
                            <tr>
                                <cong:td>
                                    <html:textarea styleClass="textlabelsBoldForTextBox commonClass" property="markNoDesc" styleId="markNoDesc" value="${lclBlPiece.markNoDesc}"
                                                   style="text-transform: uppercase; height: 140px; width: 178px"  rows="12" cols="37" />
                                </cong:td>
                            </tr>
                        </table>
                    </td>
                    <td width="40%">
                        <table width="30%">
                            <tr class="caption">
                                <td>COMMODITY DESC
                                    <img alt="remarks" id="predefinedRemarks-img" style="vertical-align: middle" src="${path}/img/icons/display.gif" title="Predefined Remarks LCLI" onclick="getPredefinedRemarks('${path}', 'true', '#predefinedRemarks-img');"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    
                                    <html:textarea styleClass="textlabelsBoldForTextBox commonClass" property="pieceDesc" styleId="pieceDesc" value="${lclBlPiece.pieceDesc}"
                                                   style="text-transform: uppercase;height: 140px;width: 345px" rows="12"  cols="73" />
                                    
                                </td>
                                <td>
                                </td>
                                <td>
                                    <cong:button label="Place P/Os into Desc" onclick="copyPO('${lclBlCommodityForm.fileNumberId}')" styleClass="button-style1"/>
                                    <img alt="remarks" id="podesc-img" style="vertical-align: middle" src="${path}/img/icons/Help-icon.png" title="This will place any P/O numbers found on all D/Rs for this BL, into the Commodity Description box. Place the cursor exactly where you want the P/O numbers to appear." />
                                </td>
                            </tr>
                            
                        </table>
                    </td>
                </tr>
            </table>
            <table align="center">
                <cong:tr><cong:td></cong:td></cong:tr>
                <cong:tr><cong:td></cong:td></cong:tr>
                <cong:tr>
                    <cong:td>
                        <input type="button"  value="Save" class="button-style1" id="saveBlCommodity"/>
                    </cong:td>
                </cong:tr>
            </table>
        </cong:form>
    </cong:div>
</body>
