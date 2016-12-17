<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<table width="100%">
    <tr class="caption"  onclick="toggleImportDetails();
        return false;" style="cursor: pointer">
        <td colspan="2" width="48%"></td>
        <td>Imports Details</td>
        <td  align="right" width="45%" id="hide">Click to Hide</td>
        <td>  <a href="javascript: void()">
                <img alt="" src="${path}/img/icons/up.gif" border="0" style="margin: 0 0 0 0; float: right;"/>
            </a>
        </td>
    </tr>
</table>
<div id="importDetails">
    <cong:table width="100%" border="0">
        <cong:tr>
            <cong:td width="30%">
                <cong:table border="0">
                    <cong:tr style="height:20.5px">
                        <cong:td colspan="11">
                            <span id="msg-scac" style="margin-left: 400px; color:green; z-index: -1" class="font-trebuchet green font-12px bold"></span>
                            <span id="val-scac" style="color:red; z-index: -1" class="font-trebuchet red font-12px bold"></span>
                            <span id="update-msg" style="margin-left: 2px; color:green; z-index: -1" class="font-trebuchet green font-12px bold"></span>
                            <span id="update-val" style="color:red; z-index: -1" class="font-trebuchet red font-12px bold"></span>
                            <span id="msg-pcs" style="margin-left: 2px; color:green; z-index: -1" class="font-trebuchet green font-12px bold"></span>
                            <span id="val-pcs" style="color:red; z-index: -1" class="font-trebuchet red font-12px bold"></span>
                        </cong:td>
                    </cong:tr>

                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" width="5%">Sub-House BL</cong:td>
                        <cong:td>
                            <cong:text name="subHouseBl" id="subHouseBl" value="${lclBookingImport.subHouseBl}" styleClass="textLCLuppercase"  maxlength="50" onchange="validateSubHouseBl('${lclBooking.lclFileNumber.id}');"/>
                             <input type="checkbox" name="subHouseBlVal" id="subHouseBlVal" title="Copy AMS HBL# value" style="vertical-align: middle;" onclick="insertSubHouseBl();validateSubHouseBl('${lclBooking.lclFileNumber.id}');"/>
                        </cong:td>
                        <cong:td width="4%"></cong:td>
                        <c:choose>
                            <c:when test="${empty lclBookingSegregation}">
                                <cong:td width="5%" styleClass="textBoldforlcl">
                                    <c:set var="defaultAmsNo" value="${amsHBLList[0].amsNo}"/>
                                    <c:set var="defaultPieces" value="${amsHBLList[0].pieces}"/>
                                    &nbsp;SCAC<cong:text name="scac" id="scac" styleClass="capitalTextForSmallTextBox" maxlength="4" value="${amsHBLList[0].scac}"/>
                                    &nbsp;AMS/HBL #<cong:text name="defaultAms" id="defaultAms" styleClass="textbox"
                                               style="curor:text; text-transform:uppercase"
                                               maxlength="50" value="${defaultAmsNo}"
                                               onchange="amsNoDelete();"/>
                                    <input type="hidden" name="defaultAms" id="tempdata" value="${defaultAmsNo}"/>
                                    &nbsp;Pcs<cong:text name="defaultPieces" id="defaultPieces" styleClass="smallTextlabelsBoldForTextBox"
                                               style="curor:text; text-transform:uppercase"
                                               maxlength="5" value="${defaultPieces}"
                                               onchange="updateAms('${lclBooking.lclFileNumber.id}','updateAms');"
                                               onkeyup="checkForNumber(this)"/>
                                    <input type="hidden" id="prevPcs" value="${defaultPieces}" />
                                    <input type="hidden" id="prevAms" value="${defaultAmsNo}"/>
                                    <input type="hidden" id="amsHblId" value="${amsHBLList[0].id}"/>
                                </cong:td></c:when>
                            <c:otherwise>
                                <cong:td width="5%">
                                    <c:set var="segAms" value="${fn:split(lclBookingSegregation.referenceNo,',')}"/>
                                    &nbsp;SCAC<cong:text name="scac" id="scac" styleClass="text-readonly-small capitalTextForSmallTextBox" 
                                               style="curor:text; text-transform:uppercase" readOnly="true" value="${segAms[0]}"/>
                                    &nbsp;AMS/HBL #<cong:text name="defaultAms" id="defaultAms" styleClass="textlabelsBoldForTextBoxDisabledLook"
                                               style="curor:text; text-transform:uppercase" value="${segAms[1]}" readOnly="true"/>
                                    &nbsp;Pcs<cong:text name="defaultPieces" id="defaultPieces" styleClass="text-readonly-small capitalTextForSmallTextBox"
                                               style="curor:text; text-transform:uppercase" value="${segAms[2]}" readOnly="true"/>
                                </cong:td>
                            </c:otherwise>
                        </c:choose>
                        <cong:td width="6%">
                            <c:if test="${empty lclBookingSegregation}">
                                <input type="button" class="button disabledButton" id="more-ams" onclick="showBlock('#amsHblBox')"
                                       value="&nbsp;&nbsp;More AMS/HBL&nbsp;&nbsp;"/>
                            </c:if>
                        </cong:td>
                        <cong:td></cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" width="5%">Freight Release</cong:td>
                        <cong:td>
                            <c:choose>
                                <c:when test="${not empty lclBookingImport.cargoOnHold}">
                                    <c:set var="releaseButton" value="red-background"/>
                                </c:when>
                                <c:when test="${not empty lclBookingImport.cargoGeneralOrder}">
                                    <c:set var="releaseButton" value="purple-background"/>
                                </c:when>
                                <c:when test="${(not empty lclBookingImport.originalBlReceived || lclBookingImport.expressReleaseClause) && not empty lclBookingImport.freightReleaseDateTime && not empty lclBookingImport.paymentReleaseReceived}">
                                    <c:set var="releaseButton" value="green-background"/>
                                </c:when>
                                <c:when test="${not empty lclBookingImport.originalBlReceived || not empty lclBookingImport.freightReleaseDateTime || not empty lclBookingImport.paymentReleaseReceived}">
                                    <c:set var="releaseButton" value="orange-background"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="releaseButton" value="button-style1"/>
                                </c:otherwise>
                            </c:choose>
                            <input type="button" class="${releaseButton}"
                                   id="freightRelease" onclick="impFreightRelease('${path}', '${lclBooking.lclFileNumber.id}')" value="Release"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" width="10%">Entry#</cong:td>
                        <cong:td>
                            <cong:text name="entryNo" id="entryNo" value="${lclBookingImport.entryNo}" styleClass="textLCLuppercase" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td></cong:td>
                        <cong:td></cong:td>
                        <cong:td width="3%"></cong:td>
                        <cong:td  rowspan="7" colspan="3">
                            <cong:table>
                                <cong:tr>
                                    <cong:td>
                                        <div id="amsHblBox" class="smallInputPopupBox">
                                            <cong:table border="1" style="border:1px solid #dcdcdc">
                                                <cong:tr>
                                                    <cong:td>SCAC</cong:td>
                                                    <cong:td>AMS No</cong:td>
                                                    <cong:td>Piece</cong:td>
                                                    <cong:td></cong:td>
                                                </cong:tr>
                                                <cong:tr>
                                                    <cong:td>
                                                        <cong:text name="amsHblScac" id="amsHblScac" style="text-transform:uppercase" styleClass="smallTextlabelsBoldForTextBox"
                                                                   maxlength="4" value=""/>
                                                        <span id="scacSpan"><input type="checkbox" name="setSacacValue" id="setSacacValue"  title="Check here to Set Scac "
                                                                                   onclick="setScacVal('${amsHBLList[0].scac}')"/>
                                                        </cong:td>
                                                        <cong:td>
                                                            <cong:text name="amsHblNo" id="amsHblNo" styleClass="text mandatory" style="text-transform:uppercase"
                                                                       maxlength="50" value=""/>
                                                        </cong:td>
                                                        <cong:td>
                                                            <cong:text name="amsHblPiece" id="amsHblPiece" styleClass="smallTextlabelsBoldForTextBox"
                                                                       onkeypress="checkForNumber(this);" value="" maxlength="5" />
                                                        </cong:td>
                                                        <cong:td>
                                                            <input type="button" class="button-style3" style="width:75px;height:20px;" value="Add"
                                                                   onclick="submitAmsHblPopupBox('addImpAmsHBL', '${lclBooking.lclFileNumber.id}', '#amsHblList')"/>
                                                        </span>
                                                    </cong:td>
                                                </cong:tr>
                                            </cong:table>
                                        </div>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td id="amsHblList">
                                        <jsp:include page="/jsps/LCL/ajaxload/amsHblList.jsp"/>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" width="5%">Express Release</cong:td>
                        <cong:td>
                            <c:choose>
                                <c:when test="${not empty lclBookingImport.originalBlReceived}">
                                    <input id="expressReleaseY" type="radio" value="Y" name="expressReleaseClasuse" disabled="true"/> Yes
                                    <input id="expressReleaseN" type="radio" value="N" name="expressReleaseClasuse" checked="yes"/> No
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${lclBookingImport.expressReleaseClause}">
                                            <input id="expressReleaseY" type="radio" value="Y" name="expressReleaseClasuse" checked="yes"/> Yes
                                            <input id="expressReleaseN" type="radio" value="N" name="expressReleaseClasuse"/> No
                                        </c:when>
                                        <c:otherwise>
                                            <input id="expressReleaseY" type="radio" value="Y" name="expressReleaseClasuse"/> Yes
                                            <input id="expressReleaseN" type="radio" value="N" name="expressReleaseClasuse" checked="yes"/> No
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl"  width="5%">External Comment</cong:td>
                        <cong:td rowspan="3" >
                            <cong:textarea rows="4" cols="30" styleClass="textLCLuppercase"
                                           style="width:259px" name="externalComment" id="externalComment"
                                           value="${lclBookingForm.externalComment}">
                            </cong:textarea>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td width="5%">
                            <input type="button" class="${not empty lclInbond ? "green-background":"button-style1"} inbondButton disabledButton" id="inbondimp" style="float:right"
                                   onclick="openInbond('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}')" value="Inbonds"/>
                        </cong:td>
                        <cong:td valign="middle" width="16%" colspan="2">
                            <table>
                                <tr>
                                    <td>
                                        <div style="float: left;text-transform:uppercase;">
                                            <label id="inbondNumber">
                                                ${lclInbond.inbondType} ${lclInbond.inbondNo} ${lclInbond.inbondPort.unLocationName} ${inbondDatetime}
                                            </label>
                                        </div>
                                    </td>
                                    <td>
                                        <div id="inbondNumersList">
                                            <c:import url="/jsps/LCL/lclInbondIcon.jsp">
                                                <c:param name="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
                                                <c:param name="fileNumber" value="${lclBooking.lclFileNumber.fileNumber}"/>
                                            </c:import>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl"  width="5%">Master BL</cong:td>
                        <cong:td>
                            <cong:text name="masterBl" id="masterBl" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"  value="${lclBookingForm.masterBl}"/>
                        </cong:td>
                        <cong:td><img alt="remarks"  align="right" id="predefinedRemarks-img"  src="/logisoft/img/icons/display.gif" title="Predefined Remarks LCLI" onclick="getRemarks('${path}', 'true', '#predefinedRemarks-img');"/></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle" width="5%">Terms</cong:td>
                        <cong:td styleClass="textBoldforlcl" width="16%">
                            <cong:radio value="P" name="pcBothImports" id="radioP" container="NULL" onclick="setBillingType();updateBillToCode()"/>P
                            <cong:radio value="C" name="pcBothImports" id="radioC" container="NULL" onclick="setBillingType('Default');updateBillToCode();"/>C
                            <input type="hidden" id="existBillingType" value="${lclBooking.billingType}"/>
                        </cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" align="right"  width="5%">Door Delivery Comment</cong:td>
                        <cong:td>
                              <c:choose>
                                        <c:when test="${empty lclBookingImport.doorDeliveryComment || lclBookingImport.doorDeliveryComment}">
                                            <input id="doorDeliveryCommentY" type="radio" value="Y" name="doorDeliveryComment" checked="yes"/> Yes
                                            <input id="doorDeliveryCommentN" type="radio" value="N" name="doorDeliveryComment"/> No
                                        </c:when>
                                        <c:otherwise>
                                            <input id="doorDeliveryCommentY" type="radio" value="Y" name="doorDeliveryComment"/> Yes
                                            <input id="doorDeliveryCommentN" type="radio" value="N" name="doorDeliveryComment" checked="yes"/> No
                                        </c:otherwise>
                                    </c:choose>
                        </cong:td>
                       
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle"> Bill to Code</cong:td>
                        <cong:td styleClass="textBoldforlcl"  valign="middle">
                            <cong:radio value="A" name="billtoCodeImports" id="billA" container="NULL" onclick="setBillingType();updateBillToCode()"/> A
                            <cong:radio value="T" name="billtoCodeImports" id="billT" container="NULL" onclick="setBillingType();updateBillToCode()"/> T
                            <cong:radio value="C" name="billtoCodeImports" id="billC" container="NULL" onclick="setBillingType('Default');updateBillToCode()"/> C
                            <cong:radio value="N" name="billtoCodeImports" id="billN" container="NULL" onclick="setBillingType();updateBillToCode()"/> N
                            <cong:radio value="W" name="billtoCodeImports" id="billW" container="NULL" onclick="setBillingType();updateBillToCode()"/> W
                            <input type="hidden" id="hiddenBillToCode" value="${lclBooking.billToParty}"/>
                        </cong:td>
                        <cong:td width="3%"></cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td colspan="3" align="right"  styleClass="textlabelsBoldforlcl">SU Heading Note</cong:td>
                        <cong:td rowspan="1">
                            <cong:text styleClass="textLCLuppercase" style="width:200px"
                                       maxlength="30" name="suHeadingNote" id="suHeadingNote" value="${lclBookingForm.suHeadingNote}">
                            </cong:text>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Third Party Account Name </cong:td>
                        <cong:td>
                            <cong:autocompletor  name="thirdPartyname" styleClass="mandatory textlabelsBoldForTextBox" id="thirdPartyname"  fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdPartyDisabled,NULL,NULL,NULL,NULL,NULL"
                                                 shouldMatch="true" width="600" container="NULL" query="THIRD_PARTY" template="tradingPartner"  paramFields="thirdSearchState,thirdSearchZip" scrollHeight="300"  value="${lclBooking.thirdPartyAcct.accountName}" callback="thirdPartyAcct();"/>
                            <input type="hidden" id="thirdPartyDisabled" value="thirdPartyDisabled"/>
                            <input type="hidden" name="thirdSearchState" id="thirdSearchState"/>
                            <input type="hidden" name="thirdSearchZip" id="thirdSearchZip"/>
                            <img alt="" src="${path}/images/icons/search_filter.png" style="vertical-align: middle;"
                              class="clientSearchEdit" title="Click here to Third Party Search options" onclick="showClientSearchOption('${path}', 'ThirdParty')"/>
                        </cong:td>
                        <cong:td width="3%"></cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td colspan="3" align="right" styleClass="textlabelsBoldforlcl">Picked Up By</cong:td>
                        <cong:td>
                            <cong:text styleClass="textLCLuppercase" style="width:200px;"
                                       maxlength="40" name="pickedUpBy" id="pickedUpBy" value="${lclBookingForm.pickedUpBy}">
                            </cong:text>
                        </cong:td>
                    </cong:tr>
                    <cong:tr style="height:25px">
                        <cong:td styleClass="textlabelsBoldforlcl">Account#</cong:td>
                        <cong:td><cong:text styleClass="textlabelsBoldForTextBoxDisabledLook"  name="thirdpartyaccountNo" id="thirdpartyaccountNo" readOnly="true" value="${lclBooking.thirdPartyAcct.accountno}"/></cong:td>
                        <cong:td width="3%"></cong:td> <cong:td width="2%"></cong:td>
                        <cong:td colspan="4"></cong:td>
                    </cong:tr>

                </cong:table>
            </cong:td>
        </cong:tr>
    </cong:table>
</div>
