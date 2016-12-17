<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<jsp:useBean id="ssdetaildao" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO"/>
<jsp:useBean id="lclAddVoyageForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm"/>
<jsp:useBean id="unitTypeDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO"/>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%    request.setAttribute("transModeList", ssdetaildao.getAllTransModesForDisplay());
    request.setAttribute("unitTypeList", unitTypeDAO.getAllUnittypesForDisplay("1", "0"));
%>
<cong:form  action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="headerId" name="headerId" />
    <cong:hidden id="detailId" name="detailId" />
    <cong:hidden id="unitId" name="unitId" />
    <cong:hidden id="polUnlocationCode" name="polUnlocationCode" />
    <cong:hidden id="unitsReopened" name="unitsReopened" />
    <%-- <cong:hidden id="limit" name ="limit" value="${lclAddVoyageForm.limit}" />--%>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
        <tr class="tableHeadingNew">
            <td width="20%">  Add Detail
            </td>
            <td width="2%" style="font-weight:bold;font-size: 12px;text-transform: uppercase;">POL:</td><td width="30%" id="polName" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase; " >${lclUnitsScheduleForm.polName}</td>
            <td width="2%" style="font-weight:bold;font-size: 12px;text-transform: uppercase;">POD:</td><td id="podName" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase;" >${lclUnitsScheduleForm.podName}</td>
        </tr>
    </table>
    <cong:table width="98%" border="0">
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Carrier</cong:td>
            <cong:td>
                <cong:autocompletor  name="accountName" styleClass="mandatory textWidth"  position="right" id="accountName" fields="accountNumber,NULL,NULL,NULL,NULL,blueScreenCarrier" callback="checkBlueScreenAcct();"
                                     shouldMatch="true" width="600"  query="IMPORT_SSLINE" template="tradingPartner" container="null" scrollHeight="300"/>
                <cong:text id="accountNumber" name="accountNumber" style="width:70px" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                <input type="hidden" name="blueScreenCarrier" id="blueScreenCarrier"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Vessel</cong:td>
            <cong:td>
                <cong:autocompletor name="spReferenceName" id="spReferenceName" template="one" params="14" position="right" scrollHeight="150"
                                    query="GENERICCODE_BY_CODETYPEID"  width="250" container="NULL" styleClass="mandatory textWidth textlabelsLclBoldForTextBox"/>
            </cong:td>

            <cong:td styleClass="textlabelsBoldforlcl">SS Voyage#</cong:td>
            <cong:td> <cong:text name="spReferenceNo" id="spReferenceNo" styleClass="mandatory textlabelsBoldForTextBoxWidth"/> </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">ETD (Sailing Date)</cong:td>

            <cong:td>
                <cong:calendarNew styleClass="mandatory textWidth" id="std" name="std" value="" onchange="calculateDaysinTT();etdValidate()"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">ETA at POD</cong:td>
            <cong:td>
                <cong:calendarNew id="etaPod" name="etaPod" styleClass="mandatory textWidth"
                                  value="" onchange="calculateDaysinTT();etaValidate();unitNumberExists();"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Transit Mode</cong:td>
            <cong:td>
                <html:select property="transMode"  disabled="true" styleClass="smallDropDown textlabelsBoldForTextBoxWidth">
                    <html:optionsCollection name="transModeList"/>
                </html:select>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Departure Pier</cong:td>
            <cong:td>
                <cong:autocompletor id="departurePier" name="departurePier" template="one" fields="NULL,NULL,NULL,departureId" position="right"
                                    query="CONCAT_PORT_NAME"  styleClass="mandatory textWidth"  width="350" container="NULL"  shouldMatch="true" scrollHeight="200"/>
                <cong:hidden id="departureId" name="departureId"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Arrival Pier/POD</cong:td>
            <cong:td>
                <cong:autocompletor id="arrivalPier" name="arrivalPier" template="one" fields="NULL,NULL,NULL,arrivalId" position="left"
                                    query="CONCAT_PORT_NAME"  styleClass="mandatory textlabelsBoldForTextBox textWidth"  width="350" container="NULL"  shouldMatch="true" scrollHeight="200"/>
                <cong:hidden id="arrivalId" name="arrivalId"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Transit Days</cong:td>
            <cong:td>
                <input type="label" id="ttOverrideDays" readOnly="true" class="textlabelsBoldForTextBoxDisabledLook" tabindex="-1"/>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Unit#</cong:td>
            <cong:td>
                <cong:text name="unitNo" id="unitNo" onchange="unitNumberValidate(this);unitNumberExists();" styleClass="mandatory textlabelsBoldForTextBox textWidth"/>
                <input type="checkbox"  name="allowfreetext" id="allowfreetext" style="vertical-align: middle"
                       title="Checked=Allow any Unit# Format<br> UnChecked=Allow only AAAA-NNNNNN-N Unit# Format"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Size</cong:td>
            <cong:td>
                <html:select property="unitType" styleId="unitType" styleClass="smallDropDown textlabelsBoldforlcl mandatory"  style="width:205px" onchange="changeUnitSize();">
                    <html:option value="">Select One</html:option>
                    <html:optionsCollection name="unitTypeList"/>
                </html:select>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Hazardous</cong:td>
            <cong:td styleClass="textBoldforlcl">
                <cong:radio value="Y" name="hazmatPermitted" container="NULL"/> Yes
                <cong:radio value="N" name="hazmatPermitted" container="NULL" /> No
            </cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">IT Date</cong:td>
            <cong:td>
                <cong:calendarNew styleClass="textWidth" id="itDatetime" name="itDatetime" onchange="dateCheck(this)" value=""/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">IT Port</cong:td>
            <cong:td>
                <cong:autocompletor id="itPort" name="itPort" template="one" fields="NULL,NULL,NULL,itPortId" position="left" styleClass="textlabelsBoldForTextBox textWidth"
                                    query="CONCAT_PORT_NAME"  width="350" container="NULL"  shouldMatch="true" scrollHeight="150"/>
                <cong:hidden name="itPortId" id="itPortId"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">IT Number</cong:td>
            <cong:td>
                <cong:text name="itNo" id="itNo" styleClass="textlabelsBoldForTextBoxWidth" onkeyup="checkForNumberOnly(this)" maxlength="9"/>
                <input type="checkbox" name="allowfreetextIT" id="allowfreetextIT"  style="vertical-align: middle"
                       onclick="checkBoxMaxLengthForIt()" title="Checked=Allow any Format<br> UnChecked=Allow only Number Format"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Last Free Date</cong:td>
            <cong:td>
                <cong:calendarNew styleClass="textWidth" id="lastFreeDate" name="lastFreeDate" onchange="dateCheck(this)"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Billing Terminal</cong:td>
            <cong:td><cong:autocompletor name="billTerminal" id="billTerminal" width="400" scrollHeight="80" container="NULL" position="left"
                                         styleClass="mandatory textlabelsBoldForTextBox textWidth" query="IMPORTTERMINAL" fields="NULL,billTerminalNo" template="three" shouldMatch="true"/>
                <cong:hidden name="billTerminalNo" id="billTerminalNo" />
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl"> Approx Due Date</cong:td>
            <cong:td>
                <cong:calendarNew id="approxDueDate" name="approxDueDate" onchange="dateCheck(this)" styleClass="textlabelsBoldForTextBoxWidth"/>
            </cong:td>
        </cong:tr>
        <tr>
            <cong:td styleClass="textlabelsBoldforlcl">Origin Agent</cong:td>
            <cong:td>
                <cong:autocompletor name="originAcct" id="originAcct" width="400" scrollHeight="100px" container="null" params="${lclAddVoyageForm.polUnlocationCode}"
                                    styleClass="mandatory textWidth"  query="IMPORTORIGINAGENT" fields="originAcctNo" template="two" shouldMatch="true"/>
                <cong:text name="originAcctNo" id="originAcctNo" readOnly="true"  styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" style="width:70px"/>
            </cong:td>
            <td class="textlabelsBoldforlcl">Master BL</td>
            <td>
                <cong:text name="masterBL" id="masterBL" styleClass="mandatory textlabelsBoldForTextBox textWidth"/>
            </td>
            <cong:td styleClass="textlabelsBoldforlcl">Stripped Date</cong:td>
            <cong:td>
                <cong:calendarNew id="strippedDate" name="strippedDate" onchange="dateCheck(this)" styleClass="textlabelsBoldForTextBoxWidth"/>
            </cong:td>
        </tr>
        <cong:tr>
            <td class="textlabelsBoldforlcl">Coloader</td>
            <td>
                <cong:autocompletor name="coloaderAcct" id="coloaderAcct" width="600" scrollHeight="100" container="null" styleClass="textlabelsBoldForTextBoxWidth"
                                    query="TRADING_PARTNER_IMPORTS"  fields="coloaderAcctNo,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fmcNumber,otiNumber,NULL,NULL,retailCommodity"
                                    template="tradingPartner" shouldMatch="true"  paramFields="coloaderSearchState,coloaderSearchZip,coloaderSearchCity"/>
                <cong:text name="coloaderAcctNo" id="coloaderAcctNo" readOnly="true" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" style="width:70px"/>
                <span style="cursor:pointer">
                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" width="16" height="16"  style="vertical-align: middle"
                         onclick="searchByFilter('${path}', 'Coloader');" title="Click here to see Coloader Search Filter Options"/>
                </span>
                <input type="hidden" name="coloaderSearchState" id="coloaderSearchState"/>
                <input type="hidden" name="coloaderSearchZip" id="coloaderSearchZip"/>
                <input type="hidden" name="coloaderSearchCity" id="coloaderSearchCity"/>
            </td>
            <cong:td styleClass="textlabelsBoldforlcl">Unit Terminal</cong:td>
            <cong:td>
                <cong:autocompletor name="unitWarehouse" id="unitWarehouse" width="500" styleClass="textlabelsBoldForTextBoxWidth" scrollHeight="100" container="null" position="left"
                                    query="IMPORTWAREHOUSE" fields="NULL,NULL,unitwarehsAddress,NULL,NULL,NULL,NULL,NULL,unitsWarehouseId" template="delwhse" shouldMatch="true"/>
                <cong:hidden name="unitsWarehouseId" id="unitsWarehouseId"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">CFS (Devanning)</cong:td>
            <cong:td>
                <cong:autocompletor name="cfsWarehouse" id="cfsWarehouse" width="500" scrollHeight="100" container="null" position="left"
                                    styleClass="textlabelsBoldForTextBoxWidth mandatory" query="CFS_WAREHOUSE" fields="NULL,NULL,cfswarehsAddress,NULL,NULL,NULL,NULL,NULL,cfsWarehouseId" template="delwhse" shouldMatch="true"/>
                <cong:hidden name="cfsWarehouseId" id="cfsWarehouseId"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Trucking Remarks</cong:td>
            <cong:td colspan="2">
                <html:textarea cols="10" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox textAreaLimit30"
                               onkeyup="limitTextarea(this,15,30)" property="remarks" />
            </cong:td>
            <cong:td styleClass="style2">
                <cong:textarea rows="4" cols="33" readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook" name="unitwarehsAddress" id="unitwarehsAddress" value=""/>
            </cong:td>
            <cong:td></cong:td>
            <cong:td styleClass="style2">
                <cong:textarea rows="4" cols="33" readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook" name="cfswarehsAddress" id="cfswarehsAddress" value=""/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Seal In</cong:td>
            <cong:td>
                <cong:text id="sealNoIn" name="sealNoIn" styleClass="textlabelsBoldForTextBoxWidth" />
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Seal Out</cong:td>
            <cong:td>
                <cong:text id="sealNoOut" name="sealNoOut" styleClass="textlabelsBoldForTextBoxWidth"/>
            </cong:td>
            <td class="textlabelsBoldforlcl">Coloader Devanning Warehouse</td>
            <td colspan="2">
                <cong:autocompletor name="coloaderDevngAcct" id="coloaderDevngAcct" width="600" scrollHeight="100" container="null" styleClass="textlabelsBoldForTextBoxWidth" position="left"
                                    query="TRADING_PARTNER_IMPORTS"  fields="coloaderDevngAcctNo,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fmcNumber,otiNumber,NULL,NULL,retailCommodity"
                                    template="tradingPartner" shouldMatch="true"  paramFields="coloaderDevngSearchState,coloaderDevngSearchZip,coloaderDevngSearchCity"/>
                <cong:text name="coloaderDevngAcctNo" id="coloaderDevngAcctNo" readOnly="true"
                           styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" style="width:70px"/>
                <span style="cursor:pointer">
                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" width="16" height="16"  style="vertical-align: middle"
                         onclick="searchByFilter('${path}', 'Coloader Devanning Warehouse');" title="Click here to see <br/> Coloader Devanning Warehouse Filter Options"/>
                </span>
                <input type="hidden" name="coloaderDevngSearchState" id="coloaderDevngSearchState"/>
                <input type="hidden" name="coloaderDevngSearchZip" id="coloaderDevngSearchZip"/>
                <input type="hidden" name="coloaderDevngSearchCity" id="coloaderDevngSearchCity"/>
            </td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Prepaid/Collect</cong:td>
            <cong:td align="left">
                <html:select styleId="prepaidCollect" property="prepaidCollect" style="width:134px" styleClass="smallDropDown textlabelsBoldforlcl">
                    <html:option value="P">Prepaid</html:option>
                    <html:option value="C">Collect</html:option>
                </html:select>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Doc Received</cong:td>
            <cong:td>
                <cong:calendarNew id="docReceived" name="docReceived" styleClass="mandatory textWidth"/>
            </cong:td>
            <cong:td colspan="2"></cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="6">&nbsp;</cong:td></cong:tr>
    </cong:table>

    <cong:table width="100%" border="0" align="center">
        <cong:tr>
            <cong:td width="45%"></cong:td>
            <cong:td colspan="6" align="center">
                <input type="button" value="Save Voyage Detail" align="center" class="button-style1" onclick="saveVoyageDetail();"/>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
    </cong:table>

</cong:form>
<script type="text/javascript">
    jQuery(document).ready(function () {
        document.getElementById("polName").innerHTML = parent.document.getElementById("pooOrigin").value;
        document.getElementById("podName").innerHTML = parent.document.getElementById("podDestination").value;
        document.lclAddVoyageForm.transMode.value = "V";
        var unitNumberObj = document.getElementById("unitNo");
        setCheckBoxOnload(unitNumberObj);
        checkBoxMaxLengthForIt();
        if (parent.document.getElementById("openPopup").value != null
                && parent.document.getElementById("openPopup").value == "openPopup") {
            $('#departurePier').val(parent.$('#departurePier').val());
            $('#departureId').val(parent.$('#departureId').val());
            $('#arrivalPier').val(parent.$('#arrivalPier').val());
            $('#arrivalId').val(parent.$('#arrivalId').val());
            $('#accountName').val(parent.$('#accountName').val());
            $('#accountNumber').val(parent.$('#accountNumber').val());
            $('#billTerminalNo').val(parent.$('#billTerminalNo').val());
            $('#billTerminal').val(parent.$('#billTerminal').val());
            $("#originAcctNo").val(parent.$("#originAcctNo").val());
            $("#originAcct").val(parent.$("#originAcct").val());
            $("#unitsWarehouseId").val(parent.$("#unitsWarehouseId").val());
            $("#unitWarehouse").val(parent.$("#unitWarehouse").val());
            $("#coloaderAcctNo").val(parent.$("#coloaderAcctNo").val());
            $("#coloaderAcct").val(parent.$("#coloaderAcct").val());
            $("#cfsWarehouseId").val(parent.$("#cfsWarehouseId").val());
            $("#cfsWarehouse").val(parent.$("#cfsWarehouse").val());
            document.lclAddVoyageForm.transMode.value = parent.document.getElementById("transMode").value;
            unitwarhsAddress();
            cfswarhsAddress();
        } else {
            $("#departurePier").val(parent.$("#originalOriginName").val());
            $("#departureId").val(parent.$("#originalOriginId").val());
        }
    });
    // function change() {
    var focusables = $("#unitType");
    // focusables.eq(0).focus().select();
    focusables.each(function () {
        $(this).keydown(function (e) {
            if (e.which == '13') { // Enter
                e.preventDefault();
                var current = focusables.index(this),
                        next = focusables.eq(current - 1).length ? focusables.eq(current - 1) : focusables.eq(0)
                this.blur();
                setTimeout(function () {
                    next.focus().select();
                }, 50);
            }
        });
    });
    // }
    function unitwarhsAddress() {
        var unitwarhsId = parent.$("#unitsWarehouseId").val();
        if (unitwarhsId != null && unitwarhsId != "" && unitwarhsId != undefined) {
            $.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "impwarehsDetails",
                    param1: unitwarhsId
                },
                async: false,
                success: function (data) {
                    $("#unitwarehsAddress").val(data);
                }
            });
        }
    }
    function cfswarhsAddress() {
        var cfswarhsId = parent.$("#cfsWarehouseId").val();
        if (cfswarhsId != null && cfswarhsId != "" && cfswarhsId != undefined) {
            $.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "impwarehsDetails",
                    param1: cfswarhsId
                },
                async: false,
                success: function (data) {
                    $("#cfswarehsAddress").val(data);
                }
            });
        }
    }
    function unitNumberValidate(obj) {
        $('#unitType').removeClass('textlabelsBoldForTextBoxDisabledLook');
        $('#unitType').removeAttr('readOnly');
        $('#unitType').val('');
        $('#unitType').removeAttr('disabled');
        $('#unitType').addClass('smallDropDown textlabelsBoldforlcl mandatory');
        var unitchecked = document.getElementById("allowfreetext").checked;
        if (!unitchecked) {
            var unitNo = obj.value;
            unitNo = unitNo.replace(/-/g, '');
            if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(4, 12))) {
                sampleAlert('Unit number must be "AAAA-NNNNNN-N" in format');
                return false;
            } else {
                if (unitNo.lastIndexOf("-") != 11 || unitNo.indexOf("-") != 4) {
                    obj.value = unitNo.substring(0, 4) + "-" + unitNo.substring(4, 10) + "-" + unitNo.substring(10);
                }
            }
        }
        return true;
    }
    function setCheckBoxOnload(obj) {
        var unitNo = obj.value;
        unitNo = unitNo.replace(/-/g, '');
        if (unitNo != "" && unitNo != null) {
            if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(4, 12))) {
                document.getElementById("allowfreetext").checked = true;
            }
        }
    }

    function unitNumberExists() {
        $("#unitId").val('');
        $("#unitsReopened").val('');
        var etaDate = $("#etaPod").val();
        var unitNo = $("#unitNo").val();
        if(etaDate!=null && etaDate!="" && unitNo!=""){
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "isUnitNumberExists",
                    param1: etaDate,
                    param2: unitNo,
                    dataType: "json"
                },
                async: false,
                preloading:true,
                success: function (data) {
                    unitCheckCurrentVoyage(data,unitNo);
                }
            });
        }
    }
    function submitUnit(txt,data){
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    $("#unitId").val(data[0]);
                    if (data[3] != null && data[3] != "") {
                        $('#unitType').val(data[3]);
                        $('#unitType').removeClass('smallDropDown textlabelsBoldforlcl mandatory');
                        $('#unitType').attr('readOnly', true);
                        $('#unitType').attr('disabled', true);
                        $('#unitType').addClass('textlabelsBoldForTextBoxDisabledLook');
                    }
                    $("#unitsReopened").val('Y');
                    saveVoyageDetail();
                    hideProgressBar();
                    $.prompt.close();
                }
                else if (v == 2) {
                    $('#unitType').removeClass('textlabelsBoldForTextBoxDisabledLook');
                    $('#unitType').removeAttr('readOnly');
                    $('#unitType').removeAttr('disabled');
                    $('#unitType').addClass('smallDropDown textlabelsBoldforlcl mandatory');
                    $("#unitId").val('');
                    $("#unitNo").val('');
                    $('#unitType').val('');
                    $("#unitsReopened").val('');
                    $.prompt.close();
                }
            }
        });
    }
    function unitCheckCurrentVoyage(data,unitNo) {
        if(data[1]!=null && data[1]!=""){
            if(data[1]=='UnitNo' && data[0]!=null && data[0]!=""){
                var txt="Unit# <span style=color:red>" + unitNo + "</span> Already Exists.Do you want to open the same unit?"
                submitUnit(txt,data);
            }else if (data[2]==='true' && data[1]!=null && data[1]!="") {
                var txt="Unit# <span style=color:red>" + unitNo + "</span> Already Exists in Voyage#"+
                    "<span style=color:red>" + data[1]+"</span>";
                $("#unitId").val('');
                $("#unitNo").val('');
                $("#unitsReopened").val('');
                $.prompt(txt);
                return false;
            }else if(data[2]==='false' && data[1]!=null && data[1]!=""){
                var txt="Unit# <span style=color:red>" + unitNo + "</span> Already Exists in Voyage#"+
                    "<span style=color:red>" + data[1]+"</span>.Do you want to open the same unit?";
                submitUnit(txt,data);
            }
        }else{
            $("#unitId").val('');
        }
    }
    function saveVoyageDetail() {
        var size = $("#unitType option:selected").text().toLowerCase();
        if (document.getElementById("accountNumber") === null || document.getElementById("accountNumber").value === "") {
            $.prompt("Carrier is required");
            $("#accountName").css("border-color", "red");
            $("#warning").show();
        }
        else if (document.getElementById("spReferenceName") === null || document.getElementById("spReferenceName").value === "") {
            $.prompt("Vessel is required");
            $("#spReferenceName").css("border-color", "red");
            $("#warning").show();
        }
        else if (document.getElementById("spReferenceNo") === null || document.getElementById("spReferenceNo").value === "") {
            $.prompt("SS Voyage# is required");
            $("#spReferenceNo").css("border-color", "red");
            $("#warning").show();
        }
        else if (document.getElementById("etaPod") === null || document.getElementById("etaPod").value === "") {
            $.prompt("ETA is required");
            $("#etaPod").css("border-color", "red");
            $("#warning").show();
        }
        else if (document.getElementById("std") === null || document.getElementById("std").value === "") {
            $.prompt("ETD Sailing Date is required");
            $("#std").css("border-color", "red");
            $("#warning").show();
        } else if (document.getElementById("departureId") === null || document.getElementById("departureId").value === "" ||
                document.getElementById("departureId").value === 0) {
            $.prompt("Departure Pier is required");
            $("#departurePier").css("border-color", "red");
            $("#warning").show();
        } else if (document.getElementById("arrivalId") === null || document.getElementById("arrivalId").value === "" ||
                document.getElementById("arrivalId").value === 0) {
            $.prompt("Arrival Pier is required");
            $("#arrivalPier").css("border-color", "red");
            $("#warning").show();
        } else if (document.getElementById("unitNo").value === null || document.getElementById("unitNo").value === "") {
            $.prompt("Unit Number is required");
            $("#unitNo").css("border-color", "red");
            $("#warning").show();
        } else if ($("#unitType").val() === "" || $("#unitType").val() === null) {
            $.prompt("Unit Type Size is required");
            $("#unitType").css("border-color", "red");
            $("#warning").show();
        } else if ($("#billTerminal").val() === null || $("#billTerminal").val() === "") {
            $.prompt("Billing Terminal is required");
            $("#billTerminal").css("border-color", "red");
            $("#warning").show();
        } else if ($("#originAcct").val() === null || $("#originAcct").val() === "") {
            $.prompt("Origin Agent is required");
            $("#originAcct").css("border-color", "red");
            $("#warning").show();
        } else if ($("#masterBL").val() === null || $("#masterBL").val() === "") {
            $.prompt("MasterBL is required");
            $("#masterBL").css("border-color", "red");
            $("#warning").show();
        } else if (($("#cfsWarehouse").val() === null || $("#cfsWarehouse").val() === "") && size !== "coload") {
            $.prompt("CFS(Devanning) is required");
            $("#cfsWarehouse").css("border-color", "red");
            $("#cfsWarehouse").show();
        } else if (($("#coloaderDevngAcctNo").val() === null || $("#coloaderDevngAcctNo").val() === "") && size === "coload") {
            $.prompt("Coloader Devanning Warehouse is required");
            $("#coloaderDevngAcct").css("border-color", "red");
            $("#coloaderDevngAcct").show();
        } else if ($("#ttOverrideDays").val() === '' || $("#ttOverrideDays").val() <= 0) {
            $.prompt("ETA should be greater than ETD");
            $("#departurePier").css("border-color", "red");
            $("#arrivalPier").css("border-color", "red");
            $("#warning").show();
        }else if (document.getElementById("docReceived") == null || document.getElementById("docReceived").value == "" ||
                document.getElementById("docReceived").value == 0) {
            $.prompt("Doc Received is required");
            $("#docReceived").css("border-color", "red");
            $("#warning").show();
        }
        else {
            showAlternateMask();
            var transMode = document.lclAddVoyageForm.transMode.value;
            var detailId = document.getElementById("detailId").value;
            unitAutoCostFlag(detailId, transMode);
        }
    }
    function unitAutoCostFlag(detailId, transMode) {
        if (parent.$('#originalOriginId').val() != "" && parent.$('#originalDestinationId').val() != "" && $('#cfsWarehouseId').val() != "" && $('#unitType').val() != "") {
            $.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkUnitAutoCostByImp",
                    forward: "/jsps/LCL/lclUnitAutoCostTemplate.jsp",
                    param1: parent.$('#originalOriginId').val(),
                    param2: parent.$('#originalDestinationId').val(),
                    param3: $('#cfsWarehouseId').val(),
                    param4: $('#unitType').val(),
                    param5: "",
                    request: true
                },
                async: false,
                success: function (data) {
                    if ($.trim(data) !== "") {
                        jQuery("<div style='top:50px; margin-left : 450px; width:500px;height:150px'></div>").html(data).addClass("popup").appendTo("body");
                        closePreloading();
                    } else {
                        showLoading();
                        saveDetailValues(detailId, transMode, "AUTOCOST_NO");
                    }
                }
            });
        } else {
            showLoading();
            saveDetailValues(detailId, transMode, "AUTOCOST_NO");
        }
    }

    function saveDetailValues(detailId, transMode, autoCostFlag) {
        showLoading();
        parent.document.getElementById("accountNumber").value = document.getElementById("accountNumber").value;
        parent.document.getElementById("spReferenceName").value = document.getElementById("spReferenceName").value;
        parent.document.getElementById("spReferenceNo").value = document.getElementById("spReferenceNo").value;
        parent.document.getElementById("std").value = document.getElementById("std").value;
        parent.document.getElementById("etaPod").value = document.getElementById("etaPod").value;
        parent.document.getElementById("departureId").value = document.getElementById("departureId").value;
        parent.document.getElementById("arrivalId").value = document.getElementById("arrivalId").value;
        parent.document.getElementById("transMode").value = transMode;
        parent.document.getElementById("detailId").value = detailId;
        parent.document.getElementById("remarks").value = document.lclAddVoyageForm.remarks.value;
        parent.document.getElementById("unitNo").value = document.getElementById("unitNo").value;
        parent.document.getElementById("hazmatPermittedUnit").value = $('input:radio[name=hazmatPermitted]:checked').val();
        //  parent.document.getElementById("goDate").value =  document.getElementById("goDate").value;
        parent.document.getElementById("itDatetime").value = document.getElementById("itDatetime").value;
        parent.document.getElementById("originAcctNo").value = document.getElementById("originAcctNo").value;
        parent.document.getElementById("unitsWarehouseId").value = document.getElementById("unitsWarehouseId").value;
        parent.document.getElementById("lastFreeDate").value = document.getElementById("lastFreeDate").value;
        parent.document.getElementById("coloaderAcctNo").value = document.getElementById("coloaderAcctNo").value;
        parent.document.getElementById("cfsWarehouseId").value = document.getElementById("cfsWarehouseId").value;
        parent.document.getElementById("itNo").value = document.getElementById("itNo").value;
        parent.document.getElementById("itPortId").value = document.getElementById("itPortId").value;
        parent.document.getElementById("unitType").value = document.lclAddVoyageForm.unitType.value;
        parent.document.getElementById("billTerminalNo").value = document.getElementById("billTerminalNo").value;
        parent.document.getElementById("unitId").value = document.getElementById("unitId").value;
        parent.document.getElementById("unitsReopened").value = document.getElementById("unitsReopened").value;
        parent.document.getElementById("docReceived").value = document.getElementById("docReceived").value;
        parent.$("#approxDueDate").val($("#approxDueDate").val());
        parent.$("#prepaidCollect").val($("#prepaidCollect").val());
        parent.$("#masterBL").val($("#masterBL").val());
        parent.$("#coloaderDevngAcctNo").val($("#coloaderDevngAcctNo").val());
        parent.$("#sealNoIn").val($("#sealNoIn").val());
        parent.$("#sealNoOut").val($("#sealNoOut").val());
        parent.$("#strippedDate").val($("#strippedDate").val());
        parent.$("#unitAutoCostFlag").val(autoCostFlag);
        parent.$("#methodName").val('saveVoyageDetail');
        parent.$("#lclAddVoyageForm").submit();
    }
    function calculateDaysinTT() {
        if ($("#std").val() !== "" && $("#etaPod").val() !== "") {
            var one_day = 1000 * 60 * 60 * 24;
            var stdDate = $("#std").val();
            var etdDay = stdDate.substring(0, 2);
            var etdMonth = getMonthNumber(stdDate.substring(3, 6));
            var etdYear = stdDate.substring(7, stdDate.length);
            var etdSailingDate = new Date(etdYear, etdMonth, etdDay);
            var etaPod = $("#etaPod").val();
            var etaDay = etaPod.substring(0, 2);
            var etaMonth = getMonthNumber(etaPod.substring(3, 6));
            var etaYear = etaPod.substring(7, etaPod.length);
            var etaDate = new Date(etaYear, etaMonth, etaDay);
            var totaldays = Math.ceil((etaDate.getTime() - etdSailingDate.getTime()) / (one_day));
            $("#ttOverrideDays").val(totaldays);
        }
    }
    function searchByFilter(path, searchByValue) {
        var href = path + "/lclBooking.do?methodName=clientSearch&searchByValue=" + searchByValue;
        $(".clientSearchEdit").attr("href", href);
        $(".clientSearchEdit").colorbox({
            iframe: true,
            width: "35%",
            height: "45%",
            title: searchByValue + " Search"
        });
    }
    function checkBlueScreenAcct() {
        var blueScreenAcct = $('#blueScreenCarrier').val();
        if (blueScreenAcct === null || blueScreenAcct === "" || blueScreenAcct === "00000") {
            $.prompt('Please Select Steam ship line Account');
            $("#accountName").css("border-color", "red");
            $("#accountName").show();
            $("#accountName").val('');
            $("#accountNumber").val('');
        }
    }
    function changeUnitSize() {
        var unitSize = $("#unitType option:selected").text().toLowerCase();
        if (unitSize === "coload") {
            $('#cfsWarehouse').removeClass("mandatory");
            setEnableTextBox("coloaderDevngAcct");
        } else {
            $('#cfsWarehouse').addClass("mandatory");
            setDisableTextBox("coloaderDevngAcct");
            setClearValues("coloaderDevngAcct", "coloaderDevngAcctNo");
        }
    }
    function setDisableTextBox(id) {
        $('#' + id).addClass('textlabelsBoldForTextBoxDisabledLook');
        $('#' + id).attr('readonly', true);
    }
    function setEnableTextBox(id) {
        $('#' + id).removeClass('textlabelsBoldForTextBoxDisabledLook');
        $('#' + id).removeAttr('readonly');
        $('#' + id).addClass('textlabelsBoldForTextBoxWidth');
    }
    function setClearValues(id1, id2) {
        $('#' + id1).val('');
        $('#' + id2).val('');
    }
</script>
