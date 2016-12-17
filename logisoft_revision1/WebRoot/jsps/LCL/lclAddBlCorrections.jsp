<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/tooltip/tooltip.js" ></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclCorrection.js"></cong:javascript>
<cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="lclCorrection.do">
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="fileId" name="fileId" />
    <cong:hidden id="blNo" name="blNo"/>
    <cong:hidden id="fileNo" name="fileNo"/>
    <cong:hidden id="correctionId" name="correctionId"/>
    <cong:hidden id="chargeId" name="chargeId"/>
    <cong:hidden id="billToParty" name="billToParty" value="${lclCorrectionNoticeBean.billToParty}"/>
    <cong:hidden id="billToCode" name="billToCode" value="${lclCorrectionNoticeBean.billToCode}"/>
    <cong:hidden id="billToPartyChanged" name="billToParty" value="${lclCorrectionForm.billToParty}"/>
    <cong:hidden id="oldAmount" name="oldAmount"/>
    <cong:hidden id="newAmount" name="newAmount"/>
    <cong:hidden id="costAmount" name="costAmount" />
    <cong:hidden id="currentProfit" name="currentProfit" />
    <cong:hidden id="profitAfterCN" name="profitAfterCN" />
    <cong:hidden id="correctionTypeIdA" name="correctionTypeIdA"/>
    <cong:hidden id="correctionTypeIdY" name="correctionTypeIdY"/>
    <cong:hidden id="noticeNo" name="noticeNo" />
    <cong:hidden id="shipperNo" name="shipperNo" />
    <cong:hidden id="forwarderNo" name="forwarderNo" />
    <cong:hidden id="thirdPartyAcctNo" name="thirdPartyAcctNo" />
    <cong:hidden id="agentNo" name="agentNo" />
    <cong:hidden id="consigneeNo" name="consigneeNo" />
    <cong:hidden id="notifyNo" name="notifyNo" />
    <cong:hidden id="customerAcctNo" name="customerAcctNo" />
    <cong:hidden id="creditMemoEmail" name="creditMemoEmail"  />
    <cong:hidden id="debitMemoEmail" name="debitMemoEmail"  />
    <cong:hidden id="viewMode" name="viewMode"  />
    <cong:hidden id="correctionChargeId" name="correctionChargeId"/>
    <cong:hidden id="lclBookingAcId" name="lclBookingAcId"/>
    <cong:hidden id="selectedMenu" name="selectedMenu" value="${lclCorrectionForm.selectedMenu}"/>
    <cong:hidden id="buttonValue"  name="buttonValue"/>
    <cong:hidden id="screenName"  name="screenName"/>
    <cong:hidden id="shipperName"  name="shipperName"/>
    <cong:hidden name="forwarder" id="forwarder" />
    <cong:hidden name="dateFilter" id="dateFilter" />
    <cong:hidden name="origin" id="origin" />
    <cong:hidden name="portOfOriginId" id="portOfOriginId"/>
    <cong:hidden name="pol" id="pol"  />
    <cong:hidden name="polId" id="polId" />
    <cong:hidden name="pod" id="pod" />
    <cong:hidden name="podId"  id="podId"/>
    <cong:hidden name="destination" id="destination"/>
    <cong:hidden name="finalDestinationId" id="finalDestinationId"/>
    <cong:hidden name="createdBy" id="createdBy"/>
    <cong:hidden name="createdByUserId" id="createdByUserId"/>
    <cong:hidden name="approvedBy" id="approvedBy"/>
    <cong:hidden name="approvedByUserId" id="approvedByUserId"/>
    <cong:hidden name="filterBy" id="filterBy"/>
    <cong:hidden name="searchCorrectionCode" id="searchCorrectionCode"/>
    <cong:hidden name="searchShipperNo" id="searchShipperNo"/>
    <cong:hidden name="searchForwarderNo" id="searchForwarderNo"/>
    <cong:hidden name="searchThirdPartyAcctNo" id="searchThirdPartyAcctNo"/>
    <cong:hidden id="concatenatedBlNo" name="concatenatedBlNo" value="${lclCorrectionNoticeBean.blNo}"/>
    <cong:hidden id="notesBlNo" name="notesBlNo"/>
    <cong:hidden id="formChangedVal" name="formChangedVal"/>
    <cong:hidden id="headerId" name="headerId" />
    <cong:hidden id="cfsDevAcctNo" name="cfsDevAcctNo"/>
    <cong:hidden id="cfsDevAcctName" name="cfsDevAcctName"/>
    <cong:hidden id="currentExitBillToCode" name="currentExitBillToCode"/>
    <cong:hidden id="billingType" name="billingType" value="${lclCorrectionNoticeBean.billingType}"/>
    <input type="hidden" id="partyNo" name="partyNo" value="${lclCorrectionForm.partyNo}"/>
    <input type="hidden" id="Originally" value=" ${lclCorrectionNoticeBean.customer}"/>
    <input type="hidden" id="hiddenCorrectionCode" name="hiddenCorrectionCode"/>
    <table>
        <tr>
            <td>
                <table class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <td>
                            <c:choose>
                                <c:when test="${lclCorrectionForm.selectedMenu!='Imports'}">
                                    BL
                                </c:when>
                                <c:otherwise>
                                    File
                                </c:otherwise>
                            </c:choose>
                            NO:</td><td><b class="headerlabel" style="color:blue;">
                            <c:choose>
                                <c:when test="${lclCorrectionForm.selectedMenu!='Imports'}">
                                    ${lclCorrectionNoticeBean.blNo}
                                </c:when>
                                <c:otherwise>
                                    ${lclCorrectionNoticeBean.fileNo}
                                </c:otherwise>
                            </c:choose>
                            </b></td>
                    </tr></table>
            </td><td>
                <table class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <c:if test="${not  empty lclCorrectionNoticeBean.noticeNo }">
                            <td>CN #</td>
                            <td><b class="headerlabel" style="color:blue;">  <c:out value="${lclCorrectionNoticeBean.noticeNo}"/></b></td>
                        </c:if>
                    </tr></table></td>
            <td>
                <table class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <c:if test="${not  empty lclCorrectionNoticeBean.enteredBy }">
                            <td>Created BY:</td>
                            <td><b class="headerlabel" style="color:blue;">  <c:out value="${lclCorrectionNoticeBean.enteredBy}"/></b></td>
                        </c:if>
                    </tr></table></td><td>
                <table class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <c:if test="${not  empty lclCorrectionNoticeBean.noticeDate }">
                            <td>On:</td>
                            <td>
                                <b class="headerlabel" style="color:blue;">  <c:out value="${lclCorrectionNoticeBean.noticeDate}"/></b></td>
                            </c:if>
                    </tr>
                </table>
            </td><td>
                <b> <FONT size="2" color="red">
                        <c:if test="${lclCorrectionNoticeBean.correctionStatus=='A'}">
                            Approved
                        </c:if>
                        <c:if test="${not empty lclCorrectionNoticeBean.postedDate}">
                            ,Posted
                        </c:if>
                    </FONT></b>
            </td>
        </tr>
    </table>
    <br>
    <cong:table align="center" cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc;">
        <cong:tr styleClass="tableHeadingNew">
            <cong:td width="88%" colspan="8">Add/Edit Corrections</cong:td>
            <cong:td width="20%" colspan="7" style="float:right">
                <c:if test="${lclCorrectionForm.viewMode!='view'}">
                    <c:choose>
                        <c:when test="${lclCorrectionForm.selectedMenu=='Imports' && lclCorrectionForm.buttonValue=='quickcn'}">
                            <input type="button" value="Post Quick CN" align="center" class="button-style1" 
                                   onclick="postQuickCN('${lclCorrectionNoticeBean.blNo}', '${lclCorrectionNoticeBean.noticeNo}');"/>
                        </c:when>
                        <c:otherwise>
                            <input type="button" value="Save" align="center" class="button-style1" onclick="saveLclCorrection();"/>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <input type="button" value="Go Back" align="center" class="button-style1" onclick="goBackCorrections($('#headerId').val());"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td colspan="9">
                <div id="divtablesty1" style="width: 100%;">
                    <table width="100%" border="0">
                        <tr class="textlabelsBold">
                            <td>Origin</td>
                            <td>
                                <span onmouseover="tooltip.show('<strong> ${lclCorrectionNoticeBean.origin}</strong>', null, event);" onmouseout="tooltip.hide();">
                                    <cong:text name="origin" id="origin" value="${lclCorrectionNoticeBean.origin}"  readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook textWidth260" />
                                </span></td>
                            <td>Destination</td>
                            <td>
                                <span onmouseover="tooltip.show('<strong>${lclCorrectionNoticeBean.destination}</strong>', null, event);" onmouseout="tooltip.hide();">
                                    <input class="BackgrndColorForTextBox"  value="${lclCorrectionNoticeBean.destination}" readonly="readonly"/>
                                </span></td>
                            <td>Sail Date</td>
                            <td><fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${lclCorrectionNoticeBean.sailDate}"/>
                                <input class="BackgrndColorForTextBox" value="${sailDate}" readonly="readonly"/></td>
                            <td valign="top">Voyage</td>
                            <td valign="top">
                                <input class="BackgrndColorForTextBox" value="${lclCorrectionNoticeBean.voyageNumber}" readonly="readonly"
                                       <c:if test="${not empty lclCorrectionNoticeBean.voyageNumber}">
                                           onmouseover="tooltip.show('<strong>${lclCorrectionNoticeBean.voyageNumber}</strong>', null, event);" onmouseout="tooltip.hide();"
                                       </c:if>
                                       /></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>Consignee</td>
                            <td>
                                <cong:autocompletor  name="constAcctName" styleClass="text-readonly" id="constAcctName" container="null" 
                                                     fields="constAcctNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdPartyDisabled" readOnly="true"
                                                     shouldMatch="true" width="600" query="IMPORTS_TP" template="tradingPartner" scrollHeight="300"
                                                     value="${lclCorrection.consAcct.accountName}" callback="setPartiesEnable();"/>
                                <input type="text" id="constAcctNo" style="width:70px" readOnly="true" 
                                       class="textlabelsBoldForTextBoxDisabledLook" value="${lclCorrection.consAcct.accountno}"/>
                            </td>
                            <td>Agent</td>
                            <td>
                                <input class="BackgrndColorForTextBox"  value="${lclCorrectionNoticeBean.agentName}" readonly="readonly"
                                       <c:if test="${not empty lclCorrectionNoticeBean.agentName}">
                                           onmouseover="tooltip.show('<strong>${lclCorrectionNoticeBean.agentName}</strong>', null, event);" onmouseout="tooltip.hide();"
                                       </c:if>/>
                            </td>
                            <td>Shipper</td>
                            <td>
                                <input class="BackgrndColorForTextBox" value="${lclCorrectionNoticeBean.shipperName}" readonly="readonly"
                                       <c:if test="${not empty lclCorrectionNoticeBean.shipperName}">
                                           onmouseover="tooltip.show('<strong>${lclCorrectionNoticeBean.shipperName}</strong>', null, event);" onmouseout="tooltip.hide();"
                                       </c:if>/>
                            </td>
                            <td>F/F #</td>
                            <td><input type="text" class="BackgrndColorForTextBox" value="${lclCorrectionNoticeBean.forwarderName}"
                                       readonly="readonly"/></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>Notify Party</td>
                            <td>
                                <cong:autocompletor  name="notyAcctName" styleClass="text-readonly" id="notyAcctName" container="null" 
                                                     fields="notyAcctNo,NULL,NULL,NULL,NULL,NULL,NULL,NULL" readOnly="true"
                                                     shouldMatch="true" width="600" query="TRADING_PARTNER_IMPORTS" template="tradingPartner" scrollHeight="300"
                                                     value="${lclCorrection.notyAcct.accountName}" callback="setPartiesEnable();"/>
                                <input type="text" id="notyAcctNo" style="width:70px" readOnly="true" 
                                       class="textlabelsBoldForTextBoxDisabledLook" value="${lclCorrection.notyAcct.accountno}"/>
                            </td>
                            <td colspan="4"></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>Third Party</td>
                            <td>
                                <cong:autocompletor  name="thirdPartyName" styleClass="text-readonly" id="thirdPartyname" container="null"
                                                     fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdPartyDisabled" readOnly="true"
                                                     shouldMatch="true" width="600" query="THIRD_PARTY" template="tradingPartner" scrollHeight="300"
                                                     value="${lclCorrection.thirdPartyAcct.accountName}" callback="setPartiesEnable();"/>
                                <input type="hidden" id="thirdPartyDisabled" value="thirdPartyDisabled"/>
                                <input type="text" id="thirdpartyaccountNo" style="width:70px" readOnly="true" 
                                       class="textlabelsBoldForTextBoxDisabledLook" value="${lclCorrection.thirdPartyAcct.accountno}"/>
                            </td>
                            <td colspan="4"></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td colspan="1">Originally --></td>
                            <td valign="baseline" colspan="5">${lclCorrectionNoticeBean.billingType}
                                By
                                <font color="Blue">${lclCorrectionNoticeBean.customerLabel}</font>
                                <font color="red">
                                    ${lclCorrectionNoticeBean.customer}</font></td>
                            <td>File No</td>
                            <td><html:text property="fileNo"  styleClass="BackgrndColorForTextBox"  value="${lclCorrectionNoticeBean.fileNo}"
                                       readonly="true"/></td>
                        </tr>
                        <tr>
                            <td colspan="8">
                                <div id="memoDivSection"></div>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>Comments<b  class="mandatoryStarColor">*</b></td>
                            <td colspan="3">
                                <c:choose>
                                    <c:when test="${lclCorrectionForm.viewMode!='view'}">
                                        <html:textarea styleClass="textlabelsBoldForTextBox" property="comments" value="${lclCorrectionNoticeBean.comments}"
                                                       rows="4" cols="65" style="text-transform: uppercase"/>
                                    </c:when>
                                    <c:otherwise>
                                        <html:textarea styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" property="comments" value="${lclCorrectionNoticeBean.comments}"
                                                       rows="4" cols="65" style="text-transform: uppercase"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                             <td>Terms</td>
                             <td>
                                 <input type="radio" name="billingType" id="billTypeP" ${lclCorrectionNoticeBean.billingType eq 'Prepaid' ? 'checked': ''} onclick="updateBillToCode();"/>P  
                                 <input type="radio" name="billingType" id="billTypeC" ${lclCorrectionNoticeBean.billingType eq 'Collect' ? 'checked': ''} onclick="updateBillToCode();"/>C 
                             </td>
                            <td>Bill to Code</td>
                            <td>
                                <input type="radio" name="billToCode" id="billToCodeA" ${lclCorrectionNoticeBean.billToCode eq 'A' ? 'checked': ''} onclick="updateBillToCode();"/>A
                                <input type="radio" name="billToCode" id="billToCodeT" ${lclCorrectionNoticeBean.billToCode eq 'T' ? 'checked': ''} onclick="updateBillToCode();"/>T
                                <input type="radio" name="billToCode" id="billToCodeC" ${lclCorrectionNoticeBean.billToCode eq 'C' ? 'checked': ''} onclick="updateBillToCode();"/>C
                                <input type="radio" name="billToCode" id="billToCodeN" ${lclCorrectionNoticeBean.billToCode eq 'N' ? 'checked': ''} onclick="updateBillToCode();"/>N
                                <input type="radio" name="billToCode" id="billToCodeW" ${lclCorrectionNoticeBean.billToCode eq 'W' ? 'checked': ''} onclick="updateBillToCode();"/>W
                                <input type="hidden" id="exitBillToCode" value="${lclCorrectionNoticeBean.billToCode}"/>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>C/N Type<b class="mandatoryStarColor">*</b></td>
                            <td colspan="5">
                                <c:choose>
                                    <c:when test="${lclCorrectionForm.viewMode!='view' && lclCorrectionForm.buttonValue!='quickcn'}">
                                        <html:select property="correctionType" value="${lclCorrectionNoticeBean.correctionType}"
                                                     onchange="displaySubGrids()"  styleId="correctionTypeId"
                                                     styleClass="dropdown_accounting"
                                                     style="width:481px;">
                                            <html:optionsCollection name="correctionTypeList"/></html:select>
                                    </c:when>
                                    <c:otherwise>
                                        <html:select property="correctionType" value="${lclCorrectionNoticeBean.correctionType}"
                                                     styleId="correctionTypeId" styleClass="dropdown_accounting textlabelsBoldForTextBoxDisabledLook" disabled="true" style="width:481px;">
                                            <html:optionsCollection name="correctionTypeList"/></html:select>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${lclCorrectionForm.viewMode!='view'}">
                                    <input type="button" class="buttonStyleNew" id="addCharges" value="Add Charges"
                                           onclick="addCorrectionCharge('${path}', '${correctionId}', '${lclCorrectionForm.selectedMenu}');"
                                           style="width:80px;visibility: hidden;position:absolute;" />
                                </c:if>
                            </td>
                            <td >
                                <c:if test="${lclCorrectionForm.selectedMenu!='Imports'}">
                                    <font class="green"> Current Profit     :
                                    </font>
                                    <font class="red-90">
                                        <label id="lblCurrentProfit">${lclCorrectionNoticeBean.currentProfit}</label>
                                    </font>
                                </c:if>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>C/N Code<b class="mandatoryStarColor">*</b></td>
                            <td colspan="5">
                                <c:choose>
                                    <c:when test="${lclCorrectionForm.viewMode!='view'}">
                                        <html:select property="correctionCode" styleClass="dropdown_accounting" value="${lclCorrectionNoticeBean.correctionCode}"
                                                     styleId="correctionCodeId" style="width:481px;" onchange="setEnableCode();">
                                            <html:optionsCollection name="correctionCodeList"/>
                                        </html:select>
                                    </c:when>
                                    <c:otherwise>
                                        <html:select property="correctionCode" styleClass="dropdown_accounting" value="${lclCorrectionNoticeBean.correctionCode}"
                                                     styleId="correctionCodeId" disabled="true"
                                                     style="width:481px;">
                                            <html:optionsCollection name="correctionCodeList"/>
                                        </html:select>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td >
                                <c:if test="${lclCorrectionForm.selectedMenu!='Imports'}">
                                    <font class="green"> Profit after C/N : </font><font class="red-90">
                                        <label id="lblProfitAfterCn">${lclCorrectionNoticeBean.profitAfterCN}</label>
                                    </font>
                                </c:if>
                            </td>
                            <td align="center"></td>
                        </tr>
                    </table>
                </div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <c:if test="${not empty lclCorrectionChargesList}">
        <display:table class="dataTable" name="${lclCorrectionChargesList}" id="chargesTable" style="visibility:hidden;position:absolute;">
            <display:column title="Charge Code" style="width:100Px;">${chargesTable.chargeCode}</display:column>
            <display:column title="Charge Desc" style="width:200Px;">${chargesTable.chargeDescriptions}</display:column>
            <display:column title="Bill To Party" style="width:200Px;">${chargesTable.billToPartyLabel}</display:column>
            <display:column title="Amount" style="width:100Px;">${chargesTable.oldAmount}</display:column>
            <display:column title="New Amount" style="width:100Px;">${chargesTable.newAmount}</display:column>
            <display:column title="Difference" style="width:100Px;">${chargesTable.differenceAmount}</display:column>
            <display:column title="Action" style="width:80Px;">
                <c:if test="${lclCorrectionForm.viewMode!='view'}">
                    <img src="${path}/images/edit.png"  style="cursor:pointer" class="correctionCharge" width="13" height="13" alt="edit"
                         onclick="editCorrectionCharge('${path}', '${chargesTable.chargeId}', '${chargesTable.chargeCode}', '${chargesTable.chargeDescriptions}',
                             '${chargesTable.oldAmount}', '${chargesTable.newAmount}', '${chargesTable.differenceAmount}', '${correctionId}',
                             '${chargesTable.billToPartyLabel}', '${lclCorrectionForm.selectedMenu}', '${chargesTable.correctionChargeId}','${chargesTable.lclBookingAcId}');"
                         />
                </c:if>
                <c:if test="${chargesTable.delete && lclCorrectionForm.viewMode!='view'}">
                    <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                         onclick="deleteCorrectionCharge('${chargesTable.correctionChargeId}');" />
                </c:if>
            </display:column>
        </display:table>
    </c:if>
</cong:form>
<script type="text/javascript">
    jQuery(document).ready(function() {
        displaySubGrids();
    });
</script>

