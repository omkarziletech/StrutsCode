<table width="100%">
    <tr class="caption" onclick="toggleGeneralInformation()" style="cursor: pointer">
        <td colspan="5" align="right" width="55%">General Information</td>
        <td align="right" width="45%" colspan="2" id="hideG">Click to Hide</td>
        <td>
            <a href="javascript: void()">
               <img alt="" src="${path}/img/icons/up.gif" border="0" style="margin: 0 0 0 0;float: right;"/>
            </a>
        </td>
    </tr>
    <tr>
        <td colspan="7">
            <div id="generalInformation">
                <table>
                    <tr>
                        <td width='60%'>
                            <table>
                                <tr>
                                    <td width='15%'>
                                        <div style="height:200px;">
                                            <table border="0">
                                                <tr>
                                                    <td width="5%" style="float:left" valign="top" class="${hideShow} textlabelsBoldforlcl">HOT Codes
                                                        <span class="button-style2" id="addhot" onclick="showBlock('#hotCodesBox');">Add</span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <div id="hotCodesBox" class="smallInputPopupBox">
                                                            <cong:autocompletor name="hotCodes" id="hotCodes" fields="NULL,NULL,NULL,NULL" query="CONCAT_HOTCODE_TYPE" width="500"
                                                                                styleClass="text" container="NULL" template="concatHTC" scrollHeight="200px" value=""/>
                                                            <input type="button" class="button-style3" style="max-width:70%" value="Add"
                                                                   onclick="submitPopupBox('#hotCodesBox', 'addLcl3pReference', '${lclBooking.lclFileNumber.id}', '#hotCodesList', 'hotCodes');"/>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td id="hotCodesList">
                                                        <jsp:include page="/jsps/LCL/ajaxload/generalInfo/hotcodeList.jsp"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>
                                    <td width='17%' style="vertical-align: top;">
                                        <table border="0">
                                            <tr>
                                                <td width="5%" style="float:left" valign="top" class="${hideShow} textlabelsBoldforlcl">
                                                    Customer PO#
                                                    <span class="button-style2" id="addcust" onclick="showBlock('#customerPoBox');">Add</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div id="customerPoBox" class="smallInputPopupBox">
                                                        <cong:text name="customerPo" id="customerPo" maxlength="250" styleClass="text" style="text-transform: uppercase" value=""/>
                                                        <input type="button" class="button-style3"  value="Add"
                                                               onclick="submitPopupBox('#customerPoBox', 'addLcl3pReference', '${lclBooking.lclFileNumber.id}', '#customerPoList', 'customerPo');"/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td id="customerPoList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/customerPoList.jsp"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width='15%' style="vertical-align: top;">
                                        <table border="0">
                                            <tr>
                                                <td width="5%" style="float:left" valign="top" class="${hideShow} textlabelsBoldforlcl">
                                                    NCM#&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <span class="button-style2 " id="addncm" onclick="showBlock('#ncmNoBox');">Add</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div id="ncmNoBox" class="smallInputPopupBox">
                                                        <cong:text name="ncmNo" id="ncmNo" maxlength="250" styleClass="text" style="text-transform:uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add"
                                                               onclick="submitPopupBox('#ncmNoBox', 'addLcl3pReference', '${lclBooking.lclFileNumber.id}', '#ncmNoList', 'ncmNo');"/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td id="ncmNoList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/ncmList.jsp"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width='15%' style="vertical-align: top;">
                                        <table border="0">
                                            <tr>
                                                <td width="5%" style="float:left" valign="top" class="${hideShow} textlabelsBoldforlcl">
                                                    Whse Doc#
                                                    <span class="button-style2" id="addWhse" onclick="showBlock('#warehouseDocBox');">Add</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div id="warehouseDocBox" class="smallInputPopupBox">
                                                        <cong:text name="wareHouseDoc" id="wareHouseDoc" maxlength="250" styleClass="text" style="text-transform:uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add"
                                                               onclick="submitPopupBox('#warehouseDocBox', 'addLcl3pReference', '${lclBooking.lclFileNumber.id}', '#warehouseList', 'wareHouseDoc');"/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td id="warehouseList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/warehouseDocList.jsp"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td colspan="2" width='30%'>
                                        <table border="0">
                                            <tr>
                                                <td width="5%" style="float:left" valign="top" class="${hideShow} textlabelsBoldforlcl">
                                                    HS Code#&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <span class="button-style2" id="addHs" onclick="showHsCodeBlock('#hsCodeBox');">Add</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div id="hsCodeBox" class="smallInputPopupBox">
                                                        <cong:text name="hsCode" id="hsCode" styleClass="text" maxlength="250" style="text-transform:uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="submitPopupBox('#hsCodeBox', 'addLclBookingHsCode', '${lclBooking.lclFileNumber.id}', '#hsCodeList', 'hsCode1');"/>
                                                    </div>
                                                    <div id="hsCodeBoxForOther" class="smallInputPopupBox" style="display:none">
                                                        <table border="0">
                                                            <tr>
                                                                <th>CodeDesc</th>
                                                                <th>Piece</th>
                                                                <th>Weight(KGS)</th>
                                                                <th>Package Type</th>
                                                                <th></th>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <cong:text name="bookingHsCode" id="bookingHsCode" styleClass="text mandatory" style="text-transform:uppercase" maxlength="250" value=""/>
                                                                    <cong:hidden name="bookingHsCodeId" id="bookingHsCodeId" value=""/>
                                                                </td>
                                                                <td>
                                                                    <cong:text name="hsCodePiece" id="hsCodePiece" styleClass="smallTextlabelsBoldForTextBox mandatory" onkeyup="checkForNumberAndDecimal(this);" value=""/>
                                                                </td>
                                                                <td>
                                                                    <cong:text name="hsCodeWeightMetric" id="hsCodeWeightMetric" styleClass="text1 weight mandatory" onkeyup="checkForNumberAndDecimal(this);" value=""/>
                                                                </td>
                                                                <td>
                                                                    <cong:autocompletor name="packageType" template="one" styleClass="textlabelsBoldForTextBox mandatory" fields="NULL,NULL,packageTypeId" width="500"  query="PACKAGE_TYPE"
                                                                                    value="" id="packageType" container="NULL" shouldMatch="true" />
                                                                    <cong:hidden name="packageTypeId" id="packageTypeId" value=""/>
                                                                </td>
                                                                <td>
                                                                    <input type="button" class="button-style3" value="Add" onclick="submitPopupBox('#hsCodeBoxForOther', 'addLclBookingHsCode', '${lclBooking.lclFileNumber.id}', '#hsCodeList', 'hsCode');"/>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td id="hsCodeList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/hsCodeList.jsp"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width='15%'>
                                        <table border="0">
                                            <tr>
                                                <td width="5%" style="float:left" valign="top" class="${hideShow} textlabelsBoldforlcl">
                                                    Tracking#
                                                    <span class="button-style2" id="addtrack" onclick="showBlock('#trackingBox');">Add</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div id="trackingBox" class="smallInputPopupBox">
                                                        <cong:text name="tracking" id="tracking" maxlength="25" styleClass="text" style="text-transform:uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add"
                                                               onclick="submitPopupBox('#trackingBox', 'addLcl3pReference', '${lclBooking.lclFileNumber.id}', '#trackingList', 'tracking');"/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td id="trackingList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/trackingList.jsp"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td style="vertical-align: top;">
                            <div style="height:100px;width:259px;overflow-y:auto;border-collapse: collapse; border: 2px solid #F8F8FF" id="refreshPriority">
                                <jsp:include page="/jsps/LCL/ajaxload/refreshPriorityNotes.jsp"/>
                            </div>
                            <table>
                                <tr>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                </tr>
                                <tr class="${hideShow}">
                                    <td style="float:left" class="textlabelsBoldforlcl">
                                        Over/Short/Damage
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table class="${hideShow}">
                                            <tr>
                                                <td>
                                                    <cong:radio value="Y" name="overShortdamaged" container="NULL" onclick="displayOsdBox();"/> Yes
                                                    <cong:radio value="N" name="overShortdamaged" container="NULL" onclick="deleteOsdRemarks('Are you sure you want to delete OSD Remarks?', '${lclBooking.lclFileNumber.id}');"/> No
                                                </td>
                                            </tr>
                                            <tr class="${hideShow}">
                                                <td>
                                                    <div id='osdRemarksId'>
                                                        <cong:textarea rows="4" cols="30"  id="osdRemarks" name="osdRemarks" value="${lclBookingForm.osdRemarks}"
                                                                       style="text-transform: uppercase"></cong:textarea>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                     </td>
                                </tr>
                            </table>
                        </td>
                        <td style="vertical-align: top;">
                            <table>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">PWK</cong:td>
                                    <cong:td>
                                        <cong:radio value="Y" name="pwk" container="NULL"/> Yes
                                        <cong:radio value="N" name="pwk" container="NULL"/> No
                                    </cong:td>
                                </cong:tr>
                                <tr>
                                    <td class="textlabelsBoldforlcl">Weighed By</td>
                                    <td>
                                        <input type="text" title="" class="text textlabelsBoldForTextBox" id="weighedBy" name="weighedBy" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textlabelsBoldforlcl">
                                        Stowed By
                                    </td>
                                    <td>
                                        <input type="text" title="" class="text textlabelsBoldForTextBox" id="stowedBy" name="stowedBy" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textlabelsBoldforlcl">Measured By</td>
                                    <td>
                                        <input type="text" class="text textlabelsBoldForTextBox" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textlabelsBoldforlcl">Remarks for Loading</td>
                                    <td>
                                        <input type="text" title="" class="text textlabelsBoldForTextBox textCap" id="remarksForLoading" name="remarksForLoading" value="${lclBookingForm.remarksForLoading}"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
</table>