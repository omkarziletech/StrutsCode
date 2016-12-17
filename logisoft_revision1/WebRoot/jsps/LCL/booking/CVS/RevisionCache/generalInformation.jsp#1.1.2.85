<cong:table>
    <tr class="caption">
        <td colspan="5" align="center">General Information</td>
    </tr>
    <tr>
        <td colspan="6">
            <div id="generalInformation">
                <cong:table border="0">
                    <cong:tr>
                        <cong:td width="60%" valign="top">
                            <cong:table border="0">
                                <cong:tr>
                                    <cong:td width="26%" valign="top">
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
                                                               onclick="addHotCode3pRef('${lclBooking.lclFileNumber.id}')"/>
                                                    </div>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td id="hotCodesList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/hotcodeList.jsp"/>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                    <%--   <cong:td></cong:td> --%>
                                    <cong:td width="26%">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" style="float:left" valign="top" styleClass="${hideShow} textlabelsBoldforlcl">
                                                    Customer PO#
                                                    <span class="button-style2" id="addcust" onclick="showBlock('#customerPoBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="customerPoBox" class="smallInputPopupBox">
                                                        <cong:text name="customerPo" id="customerPo" maxlength="250" styleClass="text" style="text-transform: uppercase" value=""/>
                                                        <input type="button" class="button-style3"  value="Add" onclick="submitPopupBox('#customerPoBox','addLcl3pReference','${lclBooking.lclFileNumber.id}','#customerPoList','customerPo')"/>
                                                    </div>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td id="customerPoList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/customerPoList.jsp"/>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                    <cong:td width="21%">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" style="float:left" valign="top" styleClass="${hideShow} textlabelsBoldforlcl">
                                                    NCM#&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <span class="button-style2 " id="addncm" onclick="showBlock('#ncmNoBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="ncmNoBox" class="smallInputPopupBox">
                                                        <cong:text name="ncmNo" id="ncmNo" maxlength="250" styleClass="text" style="text-transform:uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="submitPopupBox('#ncmNoBox','addLcl3pReference','${lclBooking.lclFileNumber.id}','#ncmNoList','ncmNo')"/>
                                                    </div>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td id="ncmNoList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/ncmList.jsp"/>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                    <cong:td width="28%">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" style="float:left" valign="top" styleClass="${hideShow} textlabelsBoldforlcl">
                                                    Whse Doc#
                                                    <span class="button-style2" id="addWhse" onclick="showBlock('#warehouseDocBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="warehouseDocBox" class="smallInputPopupBox">
                                                        <cong:text name="wareHouseDoc" id="wareHouseDoc" maxlength="250" styleClass="text" style="text-transform:uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="submitPopupBox('#warehouseDocBox','addLcl3pReference','${lclBooking.lclFileNumber.id}','#warehouseList','wareHouseDoc')"/>
                                                    </div>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td id="warehouseList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/warehouseDocList.jsp"/>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                            <cong:table border="0">
                                <cong:tr>
                                    <cong:td width="26%">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td>
                                                    &nbsp;
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    &nbsp;
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    &nbsp;
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                    <cong:td width="26%">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" style="float:left" valign="top" styleClass="${hideShow} textlabelsBoldforlcl">
                                                    &nbsp;HS Code#&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <span class="button-style2" id="addHs" onclick="showHsCodeBlock('#hsCodeBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="hsCodeBox" class="smallInputPopupBox">
                                                        <cong:text name="hsCode" id="hsCode" styleClass="text" maxlength="250" style="text-transform:uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="submitPopupBox('#hsCodeBox','addLclBookingHsCode','${lclBooking.lclFileNumber.id}','#hsCodeList','hsCode1')"/>
                                                    </div>
                                                    <div id="hsCodeBoxForOther" class="smallInputPopupBox" style="display:none">
                                                        <span style="font-size: 10px;">
                                                            <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12" align="right" onclick="closeHsCodeBox('hsCodeBoxForOther');"/>
                                                        </span>
                                                        <table style="border: 1px solid #dcdcdc; border-top:0 ">
                                                            <cong:tr>
                                                                <cong:td>CodeDesc</cong:td>
                                                                <cong:td>Piece</cong:td>
                                                                <cong:td>Weight(KGS)</cong:td>
                                                                <cong:td>Package Type</cong:td>
                                                                <cong:td></cong:td>
                                                            </cong:tr>
                                                            <cong:tr>
                                                                <cong:td><cong:text name="bookingHsCode" id="bookingHsCode" styleClass="text mandatory" style="text-transform:uppercase" maxlength="250" value=""/>
                                                                    <cong:hidden name="bookingHsCodeId" id="bookingHsCodeId" value=""/></cong:td>
                                                                <cong:td><cong:text name="hsCodePiece" id="hsCodePiece" styleClass="smallTextlabelsBoldForTextBox mandatory" onkeyup="checkForNumberAndDecimal(this)" value=""/> </cong:td>
                                                                <cong:td><cong:text name="hsCodeWeightMetric" id="hsCodeWeightMetric" styleClass="text1 weight mandatory" onkeyup="checkForNumberAndDecimal(this)" value=""/></cong:td>
                                                                <cong:td><cong:autocompletor name="packageType" template="one" styleClass="textlabelsBoldForTextBox mandatory" fields="NULL,NULL,packageTypeId" width="500"  query="PACKAGE_TYPE"
                                                                                             value="" id="packageType" container="NULL" shouldMatch="true" />
                                                                    <cong:hidden name="packageTypeId" id="packageTypeId" value=""/>
                                                                </cong:td><cong:td>
                                                                    <input type="button" class="button-style3" value="Add" onclick="submitPopupBox('#hsCodeBoxForOther','addLclBookingHsCode','${lclBooking.lclFileNumber.id}','#hsCodeList','hsCode')"/>
                                                                </cong:td>
                                                            </cong:tr>
                                                        </table>
                                                    </div>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td id="hsCodeList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/hsCodeList.jsp"/>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                    <cong:td width="21%">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td>
                                                    &nbsp;
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                    <cong:td width="32%">
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
                                                        <input type="button" class="button-style3" value="Add" onclick="submitPopupBox('#trackingBox','addLcl3pReference','${lclBooking.lclFileNumber.id}','#trackingList','tracking')"/>
                                                    </div>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td id="trackingList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/trackingList.jsp"/>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                        <cong:td valign="top" width="40px">
                            <cong:table>
                                <cong:tr>
                                    <cong:td styleClass="textBoldforlcl">External Comment
                                        <img id="externalremarks-img" alt="remarks" src="${path}/jsps/LCL/images/folder_open.png" width="16" height="16"
                                             style="vertical-align: middle" onclick="getRemarks('${path}', 'true', '#externalremarks-img');"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td >
                                        <cong:textarea rows="4" cols="30" name="externalComment"
                                                       id="externalComment" styleClass="textLCLuppercase" style="width:259px;resize:none;"
                                                       value="${lclBookingForm.externalComment}"></cong:textarea></cong:td>
                                </cong:tr>
                            </cong:table>
                            <cong:div style="height:100px;width:259px;overflow-y:auto;border-collapse: collapse; border: 2px solid #F8F8FF" id="refreshPriority">
                                <jsp:include page="/jsps/LCL/ajaxload/refreshPriorityNotes.jsp"/>
                            </cong:div>
                            <cong:table>
                                <cong:tr>
                                    <cong:td></cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td> </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td></cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td></cong:td>
                                </cong:tr>
                                <cong:tr styleClass="${hideShow}">
                                    <cong:td style="float:left" styleClass="textlabelsBoldforlcl">Over/Short/Damage</cong:td> </cong:tr>
                                <cong:tr>
                                    <cong:td>
                                        <cong:table styleClass="${hideShow}">
                                            <cong:tr>
                                                <cong:td styleClass="textBoldforlcl">
                                                    <cong:radio value="Y" name="overShortdamaged" id="overShortdamaged" container="NULL" onclick="displayOsdBox();"/> Yes
                                                    <cong:radio value="N" name="overShortdamaged" id="overShortdamaged" container="NULL" onclick="deleteOsdRemarks('Are you sure you want to delete OSD Remarks?','${lclBooking.lclFileNumber.id}');"/> No</cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr styleClass="${hideShow}">
                                    <cong:td>
                                        <div id='osdRemarksId'>
                                            <cong:textarea rows="4" cols="30"  id="osdRemarks" name="osdRemarks" value="${lclBookingForm.osdRemarks}" style="resize:none;text-transform: uppercase"></cong:textarea>
                                        </div>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                        <cong:td valign="top">
                            <cong:table>
                                <cong:tr>
                                    <cong:td  styleClass="textlabelsBoldforlcl" valign="middle">PWK /Docs Received</cong:td>
                                    <cong:td styleClass="textBoldforlcl"><div id="pwkBorder">
                                            <cong:radio value="Y" name="pwk" container="NULL"/> Yes
                                            <cong:radio value="N" name="pwk" container="NULL"/> No</div>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle"> Bill to Code</cong:td>
                                    <input type ="hidden" name="previousbillToParty" id="previousbillToParty" value="${lclBooking.billToParty}">
                                    <cong:td styleClass="textBoldforlcl"><div id="billToCodeBorder">
                                            <cong:radio value="F" name="billForm" id="billF" container="NULL" onclick="checkBilltoCode();validateBillToCode('F',this);"/> F
                                            <cong:radio value="S" name="billForm" id="billS" container="NULL" onclick="checkBilltoCode();validateBillToCode('S',this);"/> S
                                            <cong:radio value="T" name="billForm" id="billT" container="NULL" onclick="checkBilltoCode();validateBillToCode('T',this);"/> T
                                            <cong:radio value="A" name="billForm" id="billA" container="NULL" onclick="checkBilltoCode();validateBillToCode('A',this);"/> A</div>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Third Party Account Name
                                        <cong:autocompletor  name="thirdPartyname" styleClass="mandatory textlabelsBoldForTextBox" position="left"
                                                             id="thirdPartyname" fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdPartyDisabled,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL ,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,thirdpartyDisableAcct"
                                                             shouldMatch="true" width="600" query="THIRD_PARTY_BL" template="tradingPartner"
                                                             callback="thirdPartyAcct();" scrollHeight="300px" value="${lclBooking.thirdPartyAcct.accountName}"/>
                                        <input type="hidden" id="thirdPartyDisabled" value="thirdPartyDisabled"/>
                                        <input type="hidden" id="thirdpartyDisableAcct" value="thirdpartyDisableAcct"/>
                                    </cong:td></cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Account#</cong:td>
                                    <cong:td><cong:text styleClass="textlabelsBoldForTextBox"  name="thirdpartyaccountNo" id="thirdpartyaccountNo"
                                                        readOnly="true" value="${lclBooking.thirdPartyAcct.accountno}"/></cong:td>
                                </cong:tr>
                                <!--                                <cong:tr>
                                                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Small Parcel</cong:td>
                                                                    <cong:td styleClass="textBoldforlcl">
                                                                        <c:set var="upsFlag" value="${lclBookingExport.ups}"/>
                                                                        <input type="radio" id ="upsYes" name="ups" value="Y" ${upsFlag ? 'checked' :''}/> Yes
                                                                        <input type="radio" id ="upsNo" name="ups" value="N" ${!upsFlag ? 'checked' :''} /> No
                                
                                                                    </cong:td>
                                                                </cong:tr>-->
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Calc Heavy/Dense/ExLen</cong:td>
                                    <cong:td styleClass="textBoldforlcl">
                                        <c:set var="calcHeavyFlag" value="${lclBookingExport.calcHeavy}"/>
                                        <input type="radio" id ="calcYes" name="calcHeavy" value="Y" ${calcHeavyFlag ? 'checked' :''} onclick="addCalcHeavyCharges()"/>Yes
                                        <input type="radio" id ="calcNo" name="calcHeavy" value="N"  ${!calcHeavyFlag ? 'checked' :''} onclick="removeCalcHeavyCharge();"/> No
                                    </cong:td>
                                    <cong:td></cong:td>
                                    <cong:td></cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Storage Date</cong:td>
                                    <cong:td>
                                        <cong:calendarNew styleClass="textlabelsBoldForTextBox" is12HrFormat="true"
                                                          showTime="true" id="storageDatetime" name="storageDatetime"/></cong:td>
                                </cong:tr>
                                <!--                                <cong:tr>
                                                                    <cong:td styleClass="textlabelsBoldforlcl">Weighed By</cong:td>
                                                                    <cong:td><input type="text" title="" class="text textlabelsBoldForTextBox" id="weighedBy" name="weighedBy" value=""/></cong:td>
                                                                </cong:tr>
                                                                <cong:tr>
                                                                    <cong:td styleClass="textlabelsBoldforlcl">Stowed By</cong:td>
                                                                    <cong:td><input type="text" title="" class="text textlabelsBoldForTextBox" id="stowedBy" name="stowedBy" value=""/></cong:td>
                                                                </cong:tr>
                                                                <cong:tr>
                                                                    <cong:td styleClass="textlabelsBoldforlcl">Measured By</cong:td>
                                                                    <cong:td><input type="text" class="text textlabelsBoldForTextBox" value=""/></cong:td>
                                                                </cong:tr>-->
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Remarks for Loading
                                        <img id="remarks-img" alt="remarks"  src="${path}/jsps/LCL/images/folder_open.png" width="16" height="16"
                                             style="vertical-align: middle" onclick="getRemarks('${path}', '', '#remarks-img');"/>
                                    </cong:td>
                                    <cong:td>
                                        <input type="text" class="text textlabelsBoldForTextBox textCap" id="remarksForLoading" name="remarksForLoading"
                                               value="${lclBookingForm.remarksForLoading}" title="${lclBookingForm.remarksForLoading}"
                                               onfocus="clearRemarksSession()"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl" valign="middle"> AES By ECI </cong:td>
                                <cong:td colspan="2" styleClass="textBoldforlcl">
                                    <c:set var="aesFlag" value="${lclBookingExport.aes}"/>
                                    <input type="radio" id="aesByY" value="Y" name="aesBy" ${aesFlag ? 'checked' :''} onclick="coloadOrTerminal()"/> Yes
                                    <input type="radio" id="aesByN" value="N" name="aesBy" ${!aesFlag ? 'checked' :''}/> No

                                </cong:td>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">No B/L Required</cong:td>
                                    <cong:td>
                                        <c:set var="noBlFlag" value="${lclBookingExport.noBlRequired}"/>
                                        <input type="checkbox" id="noBLRequired" name="noBLRequired" value="${lclBookingExport.noBlRequired}" ${noBlFlag ? 'checked' :'unchecked'} onclick="noBLRequiredforBooking();"/></cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td colspan="1" styleClass="textlabelsBoldforlcl" valign="middle">
                                        <input type="button" name="noEeiAes" id="noEeiAes" value="NOEEI-LV"  class="button-style2" onclick="noEeiAestoAdd('${lclBooking.lclFileNumber.id}');"/>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </div>
        </td>
    </tr>
</cong:table>
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