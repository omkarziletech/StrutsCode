<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:table width="100%">
    <tr class="caption"  onclick="toggleImportDetails();
        return false;" style="cursor: pointer">
        <td colspan="2" width="48%"></td>
        <td>Imports Details</td>
        <td  align="right" width="45%" id="hide">Click to Hide</td>
        <td>
            <a href="javascript: void()">
                <img alt="" src="${path}/img/icons/up.gif" border="0" style="margin: 0 0 0 0;float: right;"/>
            </a>
        </td>
    </tr>
</cong:table>
<div id="importDetails">
    <cong:table width="100%">
        <cong:tr>
            <cong:td width="30%">
                <cong:table border="0">
                    <cong:tr style="height:20.5px">
                        <cong:td colspan="11">
                            <div id="importDetails">
                                <span id="update-msg" style="margin-left: 350px; color:green; z-index: -1" class="font-trebuchet green font-12px bold"></span>
                                <span id="update-val" style="color:red; z-index: -1" class="font-trebuchet red font-12px bold"></span>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl" width="5%">SCAC</cong:td>
                            <cong:td>
                                <cong:text name="scac" id="scac" styleClass="capitalTextForSmallTextBox" maxlength="4" value="${lclQuoteImport.scac}"/>
                            </cong:td>
                            <cong:td width="3%"></cong:td>
                            <cong:td width="4%" styleClass="textBoldforlcl">AMS/HBL #</cong:td>
                            <cong:td width="5%">
                                <c:set var="defaultAmsNo" value="${amsHBLList[0].amsNo}"/>
                                <cong:text name="defaultAms" id="defaultAms" styleClass="textbox"
                                           style="curor:text; text-transform:uppercase"
                                           maxlength="50" value="${defaultAmsNo}"
                                           onchange="updateAms('${lclQuote.lclFileNumber.id}','updateAms');" />
                            </cong:td>
                            <cong:td width="6%">
                                <c:if test="${empty defaultAmsNo}">
                                    <c:set var="hideClass" value="display-hide"/>
                                </c:if>
                                <input type="button" class="button ${hideClass}" id="more-ams" onclick="showBlock('#amsHblBox')" value="&nbsp;&nbsp;More AMS/HBL&nbsp;&nbsp;"/>
                            </cong:td>
                            <cong:td width="2%"></cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl" width="5%">Freight Release</cong:td>
                            <cong:td>
                                <input type="button" class="button-style1" id="freightRelease" onclick="impFreightRelease('${path}', '${lclQuote.lclFileNumber.id}')" value="Release"/>
                            </cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl" width="10%">Entry#</cong:td>
                            <cong:td>
                                <cong:text name="entryNo" id="entryNo" value="${lclQuoteImport.entryNo}" styleClass="textLCLuppercase" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl" width="5%">Sub-House BL</cong:td>
                            <cong:td>
                                <cong:text name="subHouseBl" id="subHouseBl" value="${lclQuoteImport.subHouseBl}" styleClass="textLCLuppercase"  maxlength="50" onchange="validateSubHouseBl('${lclQuote.lclFileNumber.id}');"/>
                            </cong:td>
                            <cong:td width="3%"></cong:td>
                            <cong:td  rowspan="7" colspan="3">
                                <cong:table>
                                    <cong:tr>
                                        <cong:td>
                                            <div id="amsHblBox" class="smallInputPopupBox">
                                                <cong:table border="1" style="border:1px solid #dcdcdc">
                                                    <cong:tr>
                                                        <cong:td>AMS No</cong:td>
                                                        <cong:td>Piece</cong:td>
                                                        <cong:td></cong:td>
                                                    </cong:tr>
                                                    <cong:tr>
                                                        <cong:td><cong:text name="amsHblNo" id="amsHblNo" styleClass="text mandatory" style="text-transform:uppercase" maxlength="50" value=""  /></cong:td>
                                                        <cong:td><cong:text name="amsHblPiece" id="amsHblPiece" styleClass="smallTextlabelsBoldForTextBox" onkeyup="checkForNumberAndDecimal(this)" maxlength="4" value=""/> </cong:td>
                                                        <cong:td>
                                                            <input type="button" class="button-style3" style="width:40px" value="Add" onclick="submitAmsHblPopupBox('addImpAmsHBL', '${lclQuote.lclFileNumber.id}', '#amsHblList')"/></cong:td>
                                                    </cong:tr></cong:table>
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
                            <cong:td styleClass="textBoldforlcl">
                                <c:choose>
                                    <c:when test="${not empty lclQuoteImport.originalBlReceived}">
                                        <input id="expressReleaseY" type="radio" value="Y" name="expressReleaseClasuse" disabled="true"/> Yes
                                        <input id="expressReleaseN" type="radio" value="N" name="expressReleaseClasuse" checked="yes"/> No
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${lclQuoteImport.expressReleaseClause}">
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
                            <cong:td rowspan="3" ><cong:textarea rows="4" cols="30" styleClass="textLCLuppercase" style="width:259px;resize:none;" name="externalComment" id="externalComment" value="${lclQuoteForm.externalComment}"></cong:textarea></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td width="5%">
                                <input type="button" class="${inbondStyle} inbondButton" id="inbondimp" style="float:right"
                                       onclick="openInbond('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}')" value="Inbonds"/>
                            </cong:td>
                            <cong:td valign="middle" width="16%">
                                <table>
                                    <tr><td>
                                            <div style="float: left;text-transform:uppercase;">
                                                <label id="inbondNumber">
                                                    ${lclInbond.inbondType} ${lclInbond.inbondNo} ${lclInbond.inbondPort.unLocationName} ${inbondDatetime}
                                                </label>
                                        </td>
                                        <td>
                                            <div id="inbondNumersList">
                                                <c:import url="/jsps/LCL/lclInbondIcon.jsp">
                                                    <c:param name="fileNumberId" value="${lclQuote.lclFileNumber.id}"/>
                                                    <c:param name="fileNumber" value="${lclQuote.lclFileNumber.fileNumber}"/>
                                                </c:import>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </cong:td>
                        <cong:td  width="3%">
                        </cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl"  width="5%"></cong:td>
                        <cong:td>
                        </cong:td>
                        <cong:td></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Terms</cong:td>
                        <cong:td styleClass="textBoldforlcl">
                            <cong:radio value="P" name="pcBothImports" id="radioP" container="NULL" onclick="setBillingType();updateBillToCode()"/>P
                            <cong:radio value="C" name="pcBothImports" id="radioC" container="NULL" onclick="setBillingType('defaultC');updateBillToCode();"/>C
                            <input type="hidden" id="existBillingType" value="${lclQuote.billingType}"/>
                        </cong:td>
                        <cong:td width="3%"></cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td colspan="4"></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle"> Bill to Code</cong:td>
                        <cong:td styleClass="textBoldforlcl">
                            <cong:radio value="A" name="billtoCodeImports" id="billA" container="NULL" onclick="setBillingType();updateBillToCode()"/> A
                            <cong:radio value="T" name="billtoCodeImports" id="billT" container="NULL" onclick="setBillingType();updateBillToCode()"/> T
                            <cong:radio value="C" name="billtoCodeImports" id="billC" container="NULL" onclick="setBillingType('Default');updateBillToCode()"/> C
                            <cong:radio value="N" name="billtoCodeImports" id="billN" container="NULL" onclick="setBillingType();updateBillToCode()"/> N
                            <input type="hidden" id="hiddenBillToCode" value="${lclQuote.billToParty}"/>
                        </cong:td>
                        <cong:td width="3%"></cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td colspan="3"></cong:td>
                        <cong:td rowspan="2">
                            <textarea  cols="30" class="textarea-readonlytext" style="width:259px;resize:none;" name="fdgriRemarks" id="externalGRIRemarks" readOnly="true">${fdGriRemarks}</textarea> </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Third Party Account Name
                            <cong:autocompletor  name="thirdPartyname" styleClass="mandatory textlabelsBoldForTextBox" id="thirdPartyname" fields="thirdpartyaccountNo"
                                                 shouldMatch="true" width="600" query="THIRD_PARTY" template="tradingPartner" scrollHeight="300px"  value="${lclQuote.thirdPartyAcct.accountName}"/>
                        </cong:td>
                        <cong:td width="3%"></cong:td>
                        <cong:td width="2%"></cong:td>
                        <cong:td colspan="4"></cong:td></cong:tr>
                    <cong:tr style="height:25px">
                        <cong:td styleClass="textlabelsBoldforlcl">Account#</cong:td>
                        <cong:td><cong:text styleClass="textlabelsBoldForTextBoxDisabledLook"  name="thirdpartyaccountNo" id="thirdpartyaccountNo" readOnly="true" value="${lclQuote.thirdPartyAcct.accountno}"/></cong:td>
                        <cong:td width="3%"></cong:td> <cong:td width="2%"></cong:td>
                        <cong:td colspan="4"></cong:td>
                    </cong:tr>

                </cong:table>
            </cong:td>
        </cong:tr>
    </cong:table>
</div>
