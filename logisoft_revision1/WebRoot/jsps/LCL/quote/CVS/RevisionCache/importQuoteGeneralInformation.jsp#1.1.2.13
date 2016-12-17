<%-- 
    Document   : generalInformation
    Created on : Oct 24, 2012, 1:59:56 AM
    Author     : Ram
--%>
<cong:table width="100%">
    <tr class="caption" onclick="toggleGeneralInformation()" style="cursor: pointer">
        <td colspan="5" align="right" width="55%">General Information</td>
        <td align="right" width="45%">
    <cong:label id="collapse">Click to Expand</cong:label>
    <cong:label id="expand">Click to Hide</cong:label>
    <a href="javascript: void()">
        <img alt="" src="${path}/img/icons/up.gif" border="0" style="margin: 0 0 0 0;float: right;"/>
    </a>
</td>
</tr>
</cong:table>
<cong:table>
    <tr>
        <td colspan="6">
            <div id="generalInformation">
                <cong:table>
                    <cong:tr>
                        <cong:td width="80%" valign="top">
                            <cong:table>
                                <cong:tr>
                                    <cong:td width="29%">
                                        <cong:div style="height:200px;">
                                            <cong:table border="0">
                                                <cong:tr>
                                                    <cong:td width="5%" valign="top" styleClass="${hideShow} textBoldforlcl">HOT Codes
                                                        <span class="button-style2" id="Qaddhot" onclick="showBlock('#hotCodesBox')">Add</span>
                                                    </cong:td>
                                                </cong:tr>
                                                <cong:tr>
                                                    <cong:td>
                                                        <div id="hotCodesBox" class="smallInputPopupBox">
                                                            <cong:autocompletor name="hotCodes" id="hotCodes" fields="NULL,NULL,NULL,NULL" query="CONCAT_HOTCODE_TYPE" width="250"
                                                                                styleClass="text" container="NULL" template="concatHTC" scrollHeight="200px" value=""/>
                                                            <input type="button" class="button-style3" value="Add" onclick="addHotCode3pRef('${lclQuote.lclFileNumber.id}')"/>
                                                        </div>
                                                    </cong:td>
                                                </cong:tr>
                                                <cong:tr>
                                                    <cong:td id="hotCodesList">
                                                        <jsp:include page="/jsps/LCL/ajaxload/generalInfo/hotcodeList.jsp"/>
                                                    </cong:td>
                                                </cong:tr>
                                            </cong:table>
                                        </cong:div>
                                    </cong:td>
                                    <%-- <cong:td></cong:td>--%>
                                    <cong:td width="26%" valign="top">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" valign="top" styleClass="${hideShow} textBoldforlcl">
                                                    Customer PO#
                                                    <span class="button-style2" id="Qaddcust" onclick="showBlock('#customerPoBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="customerPoBox" class="smallInputPopupBox">
                                                        <cong:text name="customerPo" id="customerPo" styleClass="text" maxlength="250" style="text-transform: uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="addCustomerPo3pRef('${lclQuote.lclFileNumber.id}')"/>
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
                                    <cong:td width="26%" valign="top">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" valign="top" styleClass="${hideShow} textBoldforlcl">
                                                    NCM#&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <span class="button-style2 " id="Qaddncm" onclick="showBlock('#ncmNoBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="ncmNoBox" class="smallInputPopupBox">
                                                        <cong:text name="ncmNo" id="ncmNo" styleClass="text" style="text-transform: uppercase" maxlength="250" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="addNcmNo3pRef('${lclQuote.lclFileNumber.id}')"/>
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
                                    <cong:td width="24%" valign="top">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" valign="top" styleClass="${hideShow} textBoldforlcl">
                                                    Whse Doc#
                                                    <span class="button-style2" id="QaddWhse" onclick="showBlock('#warehouseDocBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="warehouseDocBox" class="smallInputPopupBox">
                                                        <cong:text name="wareHouseDoc" id="wareHouseDoc" styleClass="text" maxlength="250" style="text-transform: uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="addWareHouseDoc3pRef('${lclQuote.lclFileNumber.id}')"/>
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
                            <cong:table>
                                <cong:tr>
                                    <cong:td width="30%">
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

                                    <cong:td width="44%">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" valign="top" styleClass="${hideShow} textBoldforlcl">
                                                    &nbsp;HS Code#&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <span class="button-style2" id="QaddHs" onclick="showHsCodeBlock('#hsCodeBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="hsCodeBox" class="smallInputPopupBox">
                                                        <cong:text name="hsCode" id="hsCode" styleClass="text" maxlength="250" style="text-transform: uppercase" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="addHsCode('#hsCodeBox','addLclQuoteHsCode','${lclQuote.lclFileNumber.id}','#hsCodeList','hsCode1')"/>
                                                    </div>
                                                    <div id="hsCodeBoxForOther" class="smallInputPopupBox" style="display:none">
                                                        <cong:table border="1">
                                                            <cong:tr>
                                                                <cong:td>CodeDesc</cong:td>
                                                                <cong:td>Piece</cong:td>
                                                                <cong:td>Weight(KGS)</cong:td>
                                                                <cong:td>Package Type</cong:td>
                                                                <cong:td></cong:td>
                                                            </cong:tr>
                                                            <cong:tr>
                                                                <cong:td><cong:text name="bookingHsCode" id="bookingHsCode" maxlength="250" styleClass="text mandatory" style="text-transform:uppercase" value=""/>
                                                                    <cong:hidden name="bookingHsCodeId" id="bookingHsCodeId" value=""/></cong:td>
                                                                <cong:td><cong:text name="hsCodepiece" id="hsCodePiece" styleClass="smallTextlabelsBoldForTextBox mandatory" onkeyup="checkForNumberAndDecimal(this)" value=""/> </cong:td>
                                                                <cong:td><cong:text name="hsCodeWeightMetric" id="hsCodeWeightMetric" styleClass="text1 weigh mandatory" onkeyup="checkForNumberAndDecimal(this)" value=""/></cong:td>
                                                                <cong:td><cong:autocompletor name="packageType" template="one" styleClass="textlabelsBoldForTextBox mandatory" fields="NULL,NULL,packageTypeId" width="500"  query="PACKAGE_TYPE"
                                                                                             value="" id="packageType" container="NULL" shouldMatch="true" />
                                                                    <cong:hidden name="packageTypeId" id="packageTypeId" value=""/>
                                                                </cong:td><cong:td>
                                                                    <input type="button" class="button-style3" value="Add" onclick="addHsCode('#hsCodeBoxForOther','addLclQuoteHsCode','${lclQuote.lclFileNumber.id}','#hsCodeList','hsCode')"/>
                                                                </cong:td>
                                                            </cong:tr>
                                                        </cong:table>
                                                    </div>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td id="hsCodeList">
                                                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/quoteHsCodeList.jsp"/>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                    <cong:td width="29%">
                                        <cong:table border="0">
                                            <cong:tr>
                                                <cong:td width="5%" valign="top" styleClass="${hideShow} textBoldforlcl">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tracking#
                                                    <span class="button-style2" id="Qaddtrack" onclick="showBlock('#trackingBox')">Add</span>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td>
                                                    <div id="trackingBox" class="smallInputPopupBox">
                                                        <cong:text name="tracking" id="tracking" styleClass="text" style="text-transform: uppercase" maxlength="25" value=""/>
                                                        <input type="button" class="button-style3" value="Add" onclick="addTracking3pRef('${lclQuote.lclFileNumber.id}')"/>
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
                        <cong:td valign="top">
                            <cong:div style="height:100px;width:259px;overflow-y:auto;border-collapse: collapse; border: 2px solid #F8F8FF" id="refreshPriority">
                                <jsp:include page="/jsps/LCL/ajaxload/refreshPriorityNotes.jsp"/>
                            </cong:div>
                            <cong:table>
                                <cong:tr styleClass="${hideShow} textBoldforlcl">
                                    <cong:td>Over/Short/Damage</cong:td> </cong:tr>
                                <cong:tr>
                                    <cong:td>
                                        <cong:table styleClass="${hideShow}">
                                            <cong:tr>
                                                <cong:td>
                                                    <cong:radio value="Y" name="overShortdamaged" container="NULL" onclick="displayOsdBox();"/> Yes
                                                    <cong:radio value="N" name="overShortdamaged" container="NULL" onclick="deleteOsdRemarks('Are you sure you want to delete OSD Remarks?','${lclQuote.lclFileNumber.id}');"/> No</cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr styleClass="${hideShow}">
                                    <cong:td>
                                        <div id="osdRemarksTextAreaId">
                                            <cong:textarea rows="4" cols="30"  id="osdRemarks" name="osdRemarks" value="${lclQuoteForm.osdRemarks}"
                                                           style="text-transform: uppercase;resize:none;"></cong:textarea>
                                        </div>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                        <cong:td valign="top">
                            <cong:table>
                                <cong:tr>
                                    <cong:td valign="middle" styleClass="textlabelsBoldforlcl">PWK</cong:td>
                                    <cong:td>
                                        <cong:radio value="Y" name="pwk" container="NULL"/> Yes
                                        <cong:radio value="N" name="pwk" container="NULL"/> No
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Weighed By</cong:td>
                                    <cong:td><input type="text" title="" class="text textlabelsBoldForTextBox"  name="weighedBy" value=""/></cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Stowed By</cong:td>
                                    <cong:td><input type="text" title="" class="text textlabelsBoldForTextBox" id="stowedBy" name="stowedBy" value=""/></cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Measured By</cong:td>
                                    <cong:td><input type="text" class="text textlabelsBoldForTextBox" value=""/></cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Remarks for Loading</cong:td>
                                    <cong:td><input type="text" title="" class="text textlabelsBoldForTextBox textCap" id="remarksForLoading" name="remarksForLoading" value="${lclQuoteForm.remarksForLoading}"/></cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </div>
        </td>
    </tr>
</cong:table>
