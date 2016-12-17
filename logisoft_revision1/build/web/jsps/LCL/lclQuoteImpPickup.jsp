<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript  src="${path}/jsps/LCL/js/lclQuotePickup.js"/>
<body style="background:#ffffff">
    <cong:form name="lclQuotePickupInfoForm" id="lclQuotePickupInfoForm" action="/lclQuotePickupInfo.do">
        <cong:hidden name="moduleName" id="moduleName" value="${lclQuotePickupInfoForm.moduleName}"/>
        <cong:hidden name="cityStateZip" id="cityStateZip" value="${lclQuotePickupInfoForm.cityStateZip}"/>
        <cong:hidden name="fileNumberId" value="${lclQuotePickupInfoForm.fileNumberId}"/>
        <cong:hidden name="fileNumber" value="${lclQuotePickupInfoForm.fileNumber}"/>
        <cong:hidden name="lclQuotePadId" value="${lclQuotePad.id}"/>
        <input type="hidden" name="arGlMappingFlag" id="arGlMappingFlagId" value="${arGlMappingFlag}"/>
        <input type="hidden" name="apGlMappingFlag" id="apGlMappingFlagId" value="${apGlMappingFlag}"/>
        <cong:table style="width:100%" border="0">
            <cong:tr>
                <cong:td styleClass="tableHeadingNew" colspan="4">
                    <cong:div styleClass="floatLeft">Door Delivery Info for File No:
                        <span class="fileNo">${lclQuotePickupInfoForm.fileNumber}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                        DoorOriginCity:<span class="fileNo">${lclQuotePickupInfoForm.cityStateZip}</span>
                    </cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="25%">
                    <cong:table>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Issued By:</cong:td>
                            <cong:td>
                                <cong:text name="issuingTerminal" id="issuingTerminal" styleClass="textlabelsBoldForTextBoxDisabledLook textLCLuppercase" readOnly="true" value="${lclQuotePickupInfoForm.issuingTerminal}"/>
                                <cong:hidden name="trmnum"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Ship To</cong:td>
                            <cong:td>
                                <span id="dojoVendor">
                                    <cong:autocompletor name="companyName" template="tradingPartner" id="shipSupplier"  query="SHIP_TO"
                                                        fields="accountNo,shipperaccttype,NULL,NULL,NULL,NULL,NULL,shipToDissable,address,shipperCity,shipperState,country,zip,contactName,phone1,fax1,email1,shipToUnlocationCode,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL"
                                                        styleClass="textlabelsBoldForTextBox textLCLuppercase" width="500" scrollHeight="200px" container="NULL" value="${lclQuotePickupInfoForm.companyName}"/>
                                    <input type="hidden" name="country" id="country" value=""/>
                                    <input type="hidden" name="shipToUnlocationCode" id="shipToUnlocationCode" />
                                </span>
                                <span id="manualVendor">
                                    <cong:text name="manualCompanyName" id="manualCompanyName" styleClass="textlabelsBoldForTextBox"
                                               style="text-transform: uppercase"  value="${lclQuotePickupInfoForm.manualCompanyName}"/>
                                </span>
                                <input type="text" id="accountNo" name="accountNo" style="width:80px;"  readOnly="true" 
                                       class="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" value="${lclQuotePickupInfoForm.accountNo}"/>
                                <cong:checkbox name="dupShipper" id="dupShipper" title="New"  container="NULL"/>
                                <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display" styleClass="trading" onclick="openTradingPartner('${path}','shipperName')"/>
                                <span>
                                    <img src="${path}/img/map.png" id="lclOriginMap" align="middle" width="12" height="12" title="Google Map Search"
                                         style="vertical-align: middle"  onclick="getShipToGoogleMap('${path}', 'displayShipToMap')" /></span>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="40" rows="3" name="address" id="address" styleClass="textlabelsBoldForTextBox textLCLuppercase" value="${lclQuotePickupInfoForm.address}" container="NULL"/>
                            </cong:td>
                        </cong:tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
                        <td>
                            <cong:text name="shipperCity" id="shipperCity" styleClass="textlabelsBoldForTextBox"  style="width:110px"
                                       value="${lclQuotePickupInfoForm.shipperCity}" container="NULL"/>

                            <span class="textlabelsBoldforlcl">&nbsp;&nbsp;State</span>
                            <cong:text name="shipperState" id="shipperState" styleClass="textlabelsBoldForTextBox"  style="width:100px"
                                       value="${lclQuotePickupInfoForm.shipperState}" container="NULL"/>
                        </td>
                    </tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text  name="zip" id="zip" styleClass="textlabelsBoldForTextBox textLCLuppercase" value="${lclQuotePickupInfoForm.zip}" container="NULL"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Ready Date</cong:td>
                        <cong:td>
                            <cong:calendarNew name="deliveryReadyDate" id="pickupReadyDate" value="${lclQuotePickupInfoForm.deliveryReadyDate}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Last Free Day</cong:td>
                        <cong:td>
                            <cong:calendarNew name="pickupCutoffDate" id="pickupCutoffDate" styleClass="textlabelsBoldForTextBox" value="${lclQuotePickupInfoForm.pickupCutoffDate}"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
            <cong:td width="20%" align="left" valign="top">
                <cong:table>
                    <cong:tr>
                        <cong:td width="40%" id="pickupCharge" colspan="2">
                            <c:import url="/jsps/LCL/ajaxload/pickupQuotecharge.jsp"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
            <cong:td width="20%" align="left" valign="top">
                <cong:table align="left">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Door Delivery Status</cong:td>
                        <cong:td>
                            <html:select property="doorDeliveryStatus" styleClass="smallDropDown textlabelsBoldforlcl" styleId="doorDeliveryStatus" style="width:150px" onchange="enableDoorDeliveryETA();">
                                <html:option value="P">Pending(Cargo at CFS )</html:option>
                                <html:option value="O">Out For Delivery</html:option>
                                <html:option value="D">Delivered</html:option>
                                <html:option value="F" >Final/Closed</html:option>
                            </html:select>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">POD Signed</cong:td>
                        <cong:td>
                            <cong:text name="podSigned" styleClass="textlabelsBoldForTextBox" id="podSigned" style="width: 143px;text-transform:uppercase;" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">POD Date</cong:td>
                        <cong:td>
                            <cong:calendarNew name="podDate" id="podDate" showTime="true" value="${lclQuotePickupInfoForm.pickupCutoffDate}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Vendor</cong:td>
                        <cong:td>
                            <cong:autocompletor name="costVendorAcct" template="tradingPartner" id="costVendorAcct" fields="costVendorNo,acctType,NULL,NULL,NULL,NULL,NULL,costDisabled,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,forwarderAcct"
                                                styleClass="textlabelsBoldForTextBox textLCLuppercase" position="left" query="TRADING_PARTNER_IMPORTS" width="500" scrollHeight="200" container="NULL" callback="acctTypeCheck()"/>
                            <input type="text" name="costVendorNo"  id="costVendorNo" style="width:80px;" value="${lclQuotePickupInfoForm.costVendorNo}"class="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true"/>
                            <input type="hidden" name="acctType" id="acctType"/>
                            <input type="hidden" name="costDisabled" id="costDisabled"/>
                            <input type="hidden" name="forwarderAcct" id="forwarderAcct"/>
                        </cong:td>
                    </cong:tr>
                    <c:choose>
                        <c:when test="${lclQuotePickupInfoForm.moduleName ne 'Exports'}">
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl" id="doorDelivery">Door Delivery ETA</cong:td>
                                <cong:td>
                                    <cong:calendarNew name="doorDeliveryEta" id="doorDeliveryEta" styleClass="mandatory" value="${lclQuotePickupInfoForm.doorDeliveryEta}"/>
                                </cong:td>
                            </cong:tr>
                        </c:when>
                    </c:choose>
                </cong:table>
            </cong:td>
            <cong:td width="1%"></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table width="99%" border="0">
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl">Special Instructions</cong:td>
            <cong:td width="92%">
                <cong:textarea rows="2" name="pickupReferenceNo" id="pickupReferenceNo" styleClass="textlabelsBoldForTextBox textLCLuppercase" style="width:85%" value="${lclQuotePickupInfoForm.pickupReferenceNo}"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table>
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl"></cong:td>
            <cong:td styleClass="tableHeadingNew" colspan="3">
                <cong:div styleClass="floatLeft">DEL FROM WHSE</cong:div>
            </cong:td>
            <cong:td width="15%" styleClass="td"></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table border="0" width="85%">
        <cong:tr>
            <cong:td width="10%"></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">WhseName
                <cong:autocompletor name="whsecompanyName" id="whsecompanyName" width="500" query="IMPORT_CFS_STATE"
                                    paramFields="defaultWhseState" fields="whseAccountNo,NULL,NULL,NULL,NULL,NULL,NULL,NULL,whseAddress,whsecity,whsePhone,whseState,NULL,whseZip,NULL"
                                    styleClass="textLclWidth textuppercaseLetter" template="tradingPartner"  scrollHeight="200px"/>
                <input type="hidden" name="defaultWhseState" id="defaultWhseState"/>
                <input type="hidden" name="whseAccountNo" id="whseAccountNo" value="${lclQuotePickupInfoForm.whseAccountNo}"/></cong:td>
            <cong:td>
                <span id="manualWhse" style="display:none">
                    <cong:text name="manualWhseName" id="manualWhseName" styleClass="textLclWidth textuppercaseLetter" value="${lclQuotePickupInfoForm.manualWhseName}"/></span>
                </cong:td>
                <cong:td>
                    <cong:checkbox name="newWhse" id="newWhse"  title="New" onclick="newDelWhse()" container="NULL" value="${lclQuotePickupInfoForm.newWhse}"/>
                <img src="${path}/images/icons/search_filter.png" id="ipiSearchEdit" class="clientSearchEdit"
                     title="Click here to edit Whse Search options" onclick="showClientSearchOption('${path}', 'IMPORT_WHSE')"/>
            </cong:td><cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
            <cong:td><cong:text name="whseAddress" id="whseAddress" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:200px" value="${lclQuotePickupInfoForm.whseAddress}"/></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
            <cong:td>
                <cong:text name="whseCity" id="whsecity" styleClass="textlabelsBoldForTextBox" value="${lclQuotePickupInfoForm.whseCity}" container="NULL" style="width:66px" maxlength="50"/>
                <cong:text name="whseState" id="whseState" styleClass="textlabelsBoldForTextBox" value="${lclQuotePickupInfoForm.whseState}" container="NULL" style="width:19px" maxlength="50"/>
                <cong:text name="whseZip" id="whseZip" styleClass="textlabelsBoldForTextBox" value="${lclQuotePickupInfoForm.whseZip}" container="NULL" style="width:40px" maxlength="50"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">phone</cong:td>
            <cong:td><cong:text  name="whsePhone" id="whsePhone" styleClass="textlabelsBoldForTextBox" value="${lclQuotePickupInfoForm.whsePhone}" maxlength="50"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table border="0" width="100%">
        <cong:tr>
            <cong:td  width="8%" styleClass="textlabelsBoldforlcl">Bill To</cong:td>
            <cong:td  width="92%"> <cong:textarea  name="pickupInstructions" id="pickupInstructions" styleClass="textWide textlabelsBoldForTextBox" style="width:84%" value="${lclQuotePickupInfoForm.pickupInstructions}" container="NULL"></cong:textarea></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Commodity</cong:td>
            <cong:td><cong:text  name="commodityDesc"  id="commodityDesc" styleClass="textLCLuppercase textlabelsBoldForTextBox" style="width:84%" value="${lclQuotePickupInfoForm.commodityDesc}" container="NULL"/></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">TOS</cong:td>
            <cong:td><cong:textarea  name="termsOfService" id="termsOfService" styleClass="textLCLuppercase textlabelsBoldForTextBox" style="width:84%" value="${lclQuotePickupInfoForm.termsOfService}" container="NULL"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:tr>
        <cong:td>
            <input type="button" value="Save" class="button-style1" id="savePickup" onclick="submitForm('save');
                   doorCity();"/>
            <input type="button" value="Carrier Rates" id="carrierRates"class="button-style1 carrier" onclick="callCarrierRates($('#whseZip').val(), $('#pickupReadyDate').val(), $('#fileNumberId').val(), '${lclQuotePickupInfoForm.moduleName}');"/>
        </cong:td>
    </cong:tr>
    <cong:hidden name="methodName" id="methodName"/>
    <input type="hidden" name="filterButtonValue" id="filterButtonValue"/>
    <input type="hidden" name="whseFilterState" id="whseFilterState"/>
</cong:form>
<script type="text/javascript">
    function showClientSearchOption(path, searchByValue) {
        var href = path + "/lclQuote.do?methodName=clientSearch&searchByValue=" + searchByValue;
        $(".clientSearchEdit").attr("href", href);
        $(".clientSearchEdit").colorbox({
            iframe: true,
            width: "35%",
            height: "51%",
            title: searchByValue + " Search"
        });
    }
    jQuery(document).ready(function() {
        if ($('#moduleName').val() === 'Imports') {
            enableDoorDeliveryETA();
        }
        showShipperToDetails();
        setWhseDetails();
        copyPickupDetailsFromExistingQuote();
    });
    function setWhseDetails() {
        if ($("#whseAccountNo").val() == "" && $("#manualWhseName").val() != "") {
            $("#newWhse").attr('checked', true);
            $("#whsecompanyName").hide();
            $("#manualWhse").show();
        } else {
            $("#newWhse").attr('checked', false);
            $("#whsecompanyName").show();
            $("#manualWhse").hide();
        }
        var cityStateZip = $('#cityStateZip').val().split('/');
        var state = cityStateZip[1];
        state = "'" + state + "'";
        $("#defaultWhseState").val(state);
        $("#whsecompanyName").show();
        var FD = parent.$('#unlocationCode').val();
        var POD = parent.$('#podUnlocationcode').val();
        if (FD !== POD && $("#whsecompanyName").val().trim() == "") {
            var whseName = parent.document.getElementById("stGeorgeAccount").value;
            var whseAddress = parent.document.getElementById("ipiCfsAddress").value;
            var whseState = parent.document.getElementById("ipiCfsState").value;
            var whsePhone = parent.document.getElementById("ipiCfsPhone").value;
            var whseCity = parent.document.getElementById("ipiCfsCity").value;
            var whseZip = parent.document.getElementById("ipiCfsZip").value;
            $('#whsecompanyName').val(whseName);
            $('#whseAddress').val(whseAddress);
            $('#whsecity').val(whseCity);
            $('#whseState').val(whseState);
            $('#whseZip').val(whseZip);
            $('#whsePhone').val(whsePhone);
            $("#newWhse").attr('checked', false);
        } else {
            //                            $('#whsecompanyName').removeClass("textlabelsBoldForTextBoxDisabledLook  text-readonly");
            //                            $('#whsecompanyName').removeAttr("readonly");
            //                            $("#whsecompanyName").addClass("textlabelsBoldForTextBox");
            //                            $('#whseAddress').removeClass("text readOnly");
            //                            $('#whseAddress').removeClass("textlabelsBoldForTextBoxDisabledLook  text-readonly");
            //                            $('#whseAddress').removeAttr("readonly");
            //                            $("#whseAddress").addClass("textlabelsBoldForTextBox");
            //                            $('#whseAddress').removeClass("text readOnly");
            //                            $('#whsecity').removeClass("text readOnly");
            //                            $('#whsecity').removeClass("textlabelsBoldForTextBoxDisabledLook  text-readonly");
            //                            $('#whsecity').removeAttr("readonly");
            //                            $("#whsecity").addClass("textlabelsBoldForTextBox");
            //                            $('#whseState').removeClass("text readOnly");
            //                            $('#whseState').removeClass("textlabelsBoldForTextBoxDisabledLook  text-readonly");
            //                            $('#whseState').removeAttr("readonly");
            //                            $("#whseState").addClass("textlabelsBoldForTextBox");
            //                            $('#whseZip').removeClass("text readOnly");
            //                            $('#whseZip').removeClass("textlabelsBoldForTextBoxDisabledLook  text-readonly");
            //                            $('#whseZip').removeAttr("readonly");
            //                            $("#whseZip").addClass("textlabelsBoldForTextBox");
            //                            $('#whsePhone').removeClass("text readOnly");
            //                            $('#whsePhone').removeClass("textlabelsBoldForTextBoxDisabledLook  text-readonly");
            //                            $('#whsePhone').removeAttr("readonly");
            //                            $("#whsePhone").addClass("textlabelsBoldForTextBox");
        }
    }
    function submitForm(methodName) {
        var moduleName = $('#moduleName').val();
        if($('#apGlMappingFlagId').val()==="N" &&($('#pickupCost').val() !== "" && $('#pickupCost').val() !== '0.00') ){
            $.prompt('No Gl Mapping found for CostCode ---> DOORDEL');
          $('#pickupCost').val('');
          return false;
        }else if($('#arGlMappingFlagId').val()==="N" &&($('#chargeAmount').val() !== "" && $('#chargeAmount').val() !== '0.00') ){
           $.prompt('No Gl Mapping found for ChargeCode ---> DOORDEL');
          $('#chargeAmount').val('');
          return false;
        }
        if (moduleName === 'Imports' && ($('#pickupCost').val() !== "" && $('#pickupCost').val() !== null) && ($('#costVendorAcct').val() === "" || $('#costVendorAcct').val() === null)) {
            $.prompt("Vendor Name is required");
            $("#costVendorAcct").css("border-color", "red");
            $("#costVendorAcct").show();
        } else {
            var doorDeliveryEta = $("#doorDeliveryEta").val();
            if(moduleName === 'Imports' && doorDeliveryEta == '' && $("#doorDeliveryStatus").val() != 'P'){
                $.prompt("Door Delivery ETA is required");
            }else{ 
                showLoading();
                $('#methodName').val(methodName);
                submitAjaxFormForPickup(methodName, '#lclQuotePickupInfoForm', '#chargeDesc');
            }
        }
    }
    function submitAjaxFormForPickup(methodName, formName, selector) {
        $("#methodName").val(methodName);
        var params = $(formName).serialize();
        params += "&moduleName=Imports";
        $.post($(formName).attr("action"), params,
        function(data) {
            $(selector).html(data);
            $(selector, window.parent.document).html(data);
            parent.$.fn.colorbox.close();
        });
    }
    function editCharge(txt) {
        var pickup = $('#chargeAmount').val();
        var pickupCost = $('#pickupCost').val();
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function(v) {
                if (v === 1) {
                    showProgressBar();
                    $('#chargeAmount').val('');
                    $('#pickupCost').val('');
                    $('#clearCharge').hide();
                    $('#costVendorAcct').val('');
                    $('#costVendorNo').val('');
                    $('#costVendorAcct').addClass('textlabelsBoldForTextBoxDisabledLook');
                    $('#costVendorAcct').attr("readonly", true);
                    $('#chargeAmount').removeClass('textlabelsBoldForTextBoxDisabledLook');
                    $('#chargeAmount').removeAttr("readOnly");
                    $('#chargeAmount').addClass("textlabelsBoldForTextBox");
                    $.prompt.close();
                    hideProgressBar();
                }
                else if (v === 2) {
                    $('#chargeAmount').val(pickup);
                    $('#chargeAmount').val(pickupCost);
                    $.prompt.close();
                }
            }
        });
    }
    function doorCity() {
        parent.$('#doorOriginCityZip').val($('#cityStateZip').val());
    }
    function congAlert(txt) {
        $.prompt(txt);
    }


    $('#shipSupplier').keyup(function() {
        var shipSupplier = $('#shipSupplier').val();
        if (shipSupplier === "") {
            $('#shipSupplier').val('');
            $('#accountNo').val('');
            $("#address").val('');
            $("#zip").val('');
            $('#fax1').val('');
            $('#phone1').val('');
            $("#shipperCity").val('');
            $("#shipperState").val('');
        }
    });
    function showShipperToDetails() {
        var manualCompanyName = $('#manualCompanyName').val();
        if (manualCompanyName !== "") {
            $('#dupShipper').attr("checked", true);
            $('#manualVendor').show();
            $('#dojoVendor').hide();
        }
        else {
            $('#dupShipper').attr("checked", false);
            $('#manualVendor').hide();
            $('#dojoVendor').show();
        }
    }
    function newmanualVendor() {
        if ($('#dupShipper').is(":checked")) {
            $("#dojoVendor").hide();
            $("#manualVendor").show();
            $('#accountNo').val('')
            $('#companyName').val('')
            $('#address').val('')
        } else {
            $("#dojoVendor").show();
            $("#manualVendor").hide();
            $('#manualCompanyName').val('')
            $('#address').val('')
        }
    }

    function newDelWhse() {
        if ($('#newWhse').is(":checked")) {
            $('#whsecompanyName').val('');
            $("#whseAccountNo").val('');
            $('#whseAddress').val('');
            $('#whsecity').val('');
            $('#whseState').val('');
            $('#whseZip').val('');
            $('#whsePhone').val('');
            $("#manualWhse").show();
            $('#whsecompanyName').hide();
        } else {
            $('#whsecompanyName').val('');
            $('#manualWhseName').val('');
            $("#whseAccountNo").val('');
            $('#whseAddress').val('');
            $('#whsecity').val('');
            $('#whseState').val('');
            $('#whseZip').val('');
            $('#whsePhone').val('');
            var finalDestn = parent.$("#finalDestinationR").val();
            var state = finalDestn.substring(finalDestn.lastIndexOf("/") + 1, finalDestn.lastIndexOf("("));
            state = "'" + state + "'";
            $("#defaultWhseState").val(state);
            $("#whsecompanyName").show();
            $("#manualWhse").hide();
        }
    }
    function copyPickupDetailsFromExistingQuote() {
        var fileNumberId = $('#fileNumberId').val();
        if (fileNumberId === null || fileNumberId === "" || fileNumberId === '0') {
            $('#whsePhone').val(parent.$('#whsePhone').val());
            $('#whsecompanyName').val(parent.$('#whsecompanyName').val());
            $('#whsecity').val(parent.$('#whseCity').val());
            $('#whseState').val(parent.$('#whseState').val());
            $('#whseZip').val(parent.$('#whseZip').val());
            $('#whseAddress').val(parent.$('#whseAddress').val());
            $('#pickupInstructions').val(parent.$('#pickupInstructions').val());
            $('#shipSupplier').val(parent.$('#shipSupplier').val());
            $('#tempShipper').val(parent.$('#shipSupplier').val());
            $('#spAcct').val(parent.$('#spAcct').val());
            $('#address').val(parent.$('#paddress').val());
            $('#cityStateZip').val(parent.$('#cityStateZip').val());
            $('#phone1').val(parent.$('#pphone1').val());
            $('#contactName').val(parent.$('#pcontactName').val());
            $('#fax1').val(parent.$('#pfax1').val());
            $('#email1').val(parent.$('#pemail1').val());
            $('#termsOfService').val(parent.$('#termsOfService').val());
            $('#commodityDesc').val(parent.$('#pcommodityDesc').val());
            $('#pickupReferenceNo').val(parent.$('#pickupReferenceNo').val());
            $('#pickupCutoffDate').val(parent.$('#pickupCutoffDate').val());
            $('#pickupReadyDate').val(parent.$('#pickupReadyDate').val());
            $('#cityStateZip').val(parent.$('#cityStateZip').val());
            $('#issuingTerminal').val(parent.$('#issuingTerminal').val());
            $('#to').val(parent.$('#to').val());
            $('#trmnum').val(parent.$('#ptrmnum').val());
            $('#pickupCost').val(parent.$('#pickupCost').val());
            $('#chargeAmount').val(parent.$('#chargeAmount').val());
            $('#duplicateChargeAmount').val(parent.$('#chargeAmount').val());
            $('#scacCode').val(parent.$('#scacCode').val());
            $('#pickupHours').val(parent.$('#pickupHours').val());
            $('#pickupReadyNote').val(parent.$('#pickupReadyNote').val());
        }
    }
    //    function concatShipTo() {
    //        var shipToAddress = "";
    //        shipToAddress += $("#address").val() + "\n";
    //        shipToAddress += $("#city").val() + " ";
    //        shipToAddress += $("#state").val() + " ";
    //        shipToAddress += $("#zipCode").val() + "\n";
    //        $("#address").val(shipToAddress);
    //    }
</script>
</body>
