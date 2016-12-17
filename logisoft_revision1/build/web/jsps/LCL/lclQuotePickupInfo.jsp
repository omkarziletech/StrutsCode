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
        <cong:hidden name="pickUpInfo" id="pickUpInfo" value="false"/>
        <input type="hidden" name="arGlMappingFlag" id="arGlMappingFlagId" value="${arGlMappingFlag}"/>
        <input type="hidden" name="apGlMappingFlag" id="apGlMappingFlagId" value="${apGlMappingFlag}"/>
        <cong:table style="width:100%" border="0">
            <cong:tr>
                <cong:td styleClass="tableHeadingNew" colspan="3">
                    <cong:div styleClass="floatLeft">Pickup Info for File No:
                        <span class="fileNo">${lclQuotePickupInfoForm.fileNumber}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                        DoorOriginCity:<span class="fileNo OrginZip">${lclQuotePickupInfoForm.cityStateZip}</span>
                    </cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="25%">
                    <cong:table>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Issued By:</cong:td>
                            <cong:td>
                                <%--<cong:autocompletor name="issuingTerminal" id="issuingTerminal" template="commTempFormat" styleClass="text mandatory textlabelsBoldForTextBox textLCLuppercase" query="ISSUING_TERMINAL" fields="NULL,trmnum" width="250" value="${lclQuotePickupInfoForm.issuingTerminal}" container="NULL" shouldMatch="true"/>--%>
                                <cong:text name="issuingTerminal" id="issuingTerminal" styleClass="textlabelsBoldForTextBoxDisabledLook textLCLuppercase" readOnly="true" value="${lclQuotePickupInfoForm.issuingTerminal}"/>
                                <cong:hidden name="trmnum"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">To Trucker</cong:td>
                            <cong:td>
                                <span id="toFieldDojo">
                                    <cong:autocompletor name="toVendorName" template="tradingPartner" id="toDojo" fields="toVendorNo" scrollHeight="300px"
                                                        styleClass="textlabelsBoldForTextBox textLCLuppercase"  query="VENDOR" width="600" container="NULL" shouldMatch="true" value="${lclQuotePickupInfoForm.toVendorName}"/>
                                    <cong:hidden name="toVendorNo" id="toVendorNo" />
                                </span>
                                <span id="toFieldText">
                                    <cong:text name="manualCompanyName" id="manualCompanyName"  styleClass="text textLCLuppercase" container="NULL" value="${lclQuotePickupInfoForm.manualCompanyName}"/>
                                </span> <cong:checkbox name="manualShipper" id="manualShipper" onclick="newManualShipper();" container="NULL"/>New
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Ship/Supplier</cong:td>
                            <cong:td>
                                <span id="dojoPickUpShipper">
                                    <cong:autocompletor name="companyName" template="tradingPartner" id="shipSupplier" styleClass="textlabelsBoldForTextBox"
                                                        fields="shipperAccountNo,shipper_acct_type,NULL,NULL,NULL,NULL,NULL,shipSupDisabled,address,shipperCity,shipperState,NULL,zip,NULL,phone1,fax1,NULL,NULL,NULL,NULL,NULL,NULL,shipSupDisableAcct"
                                                        query="SHIPPER" scrollHeight="300px"  width="500" paramFields="shipSupSearchState,shipSupSearchZip,shipSupSearchSalesCode,shipCountryUnLocCode" container="NULL" shouldMatch="true"
                                                        value="${lclQuotePickupInfoForm.companyName}" callback="shipSupplierAcctType();"/>
                                    <cong:text name="shipperAccountNo" id="shipperAccountNo" style="width:70px" readOnly="true"
                                               styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                                </span>
                                <input type="hidden" name="shipSupSearchState" id="shipSupSearchState"/>
                                <input type="hidden" name="shipSupSearchZip" id="shipSupSearchZip"/>
                                <input type="hidden" name="shipSupSearchSalesCode" id="shipSupSearchSalesCode"/>
                                <input type="hidden" name="shipSupSearchCountry" id="shipSupSearchCountry"/>
                                <input type="hidden" name="shipCountryUnLocCode" id="shipCountryUnLocCode"/>
                                <input type="hidden" name="shipper_acct_type" id="shipper_acct_type"/>
                                <input type="hidden" name="shipSupDisabled" id="shipSupDisabled"/>
                                <input type="hidden" name="shipSupDisableAcct" id="shipSupDisableAcct"/>
                                <span id="manualShipp">
                                    <cong:text name="companyNameDup" id="dupShipName" styleClass="textlabelsBoldForTextBox"
                                               style="text-transform: uppercase" value="${lclQuotePickupInfoForm.companyNameDup}"/>
                                </span><cong:checkbox id="newCompanyName" name="newCompanyName" title="New" onclick="newcompanyName();" container="NULL"/>
                                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle"
                                     title="Click here to edit Ship/Supplier Search options" onclick="showShipSupplierSearchOption('${path}', 'Ship/Supplier')"/>


                                <cong:img src="${path}/jsps/LCL/images/add2.gif" id="greenPlusIcon" width="16" height="16" alt="display" styleClass="trading" onclick="openWarehouse()"/>
                                <span title="Checked=Copy from Shipper">
                                    <cong:checkbox name="dupShipper" id="dupShipper" onclick="copyShipper();"
                                                   container="NULL"/>Copy
                                </span>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea   cols="40" rows="3"  name="address" id="address" styleClass="textlabelsBoldForTextBox"
                                                 value="${lclQuotePickupInfoForm.address}" container="NULL"/>
                            </cong:td>
                        </cong:tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
                            <td>
                            <cong:text name="shipperCity" id="shipperCity" styleClass="textlabelsBoldForTextBox" style="width:100px"
                                       value="${lclQuotePickupInfoForm.shipperCity}" container="NULL"/>
                            <span class="textlabelsBoldforlcl">&nbsp;&nbsp;State</span>
                            <cong:text name="shipperState" id="shipperState" styleClass="textlabelsBoldForTextBox" style="width:100px"
                                       value="${lclQuotePickupInfoForm.shipperState}" container="NULL"/>
                        </td>
                    </tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text  name="zip" id="zip" styleClass="textlabelsBoldForTextBox"
                                        value="${lclQuotePickupInfoForm.zip}" container="NULL"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Ready Date</cong:td>
                        <cong:td>
                            <cong:calendarNew name="pickupReadyDate" id="pickupReadyDate" value="${lclQuotePickupInfoForm.pickupReadyDate}" styleClass="textlabelsBoldForTextBox"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">CutOff Date</cong:td>
                        <cong:td>
                            <cong:calendarNew name="pickupCutoffDate" id="pickupCutoffDate" styleClass="textlabelsBoldForTextBox" value="${lclQuotePickupInfoForm.pickupCutoffDate}"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
            <cong:td width="40%" align="left" valign="top">
                <cong:table>
                    <cong:tr>
                        <cong:td width="40%" id="pickupCharge" colspan="2">
                            <c:import url="/jsps/LCL/ajaxload/pickupQuotecharge.jsp"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
            <cong:td width="30%" align="left"></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table width="99%" border="0">
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl">Ref#/PRO#</cong:td>
            <cong:td width="92%"><cong:textarea rows="1" name="pickupReferenceNo" id="pickupReferenceNo" styleClass="textlabelsBoldForTextBox textLCLuppercase" style="width:47%" cols="50" value="${lclQuotePickupInfoForm.pickupReferenceNo}" onkeyup="limitText(this,50)"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table>
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl"></cong:td>
            <cong:td styleClass="tableHeadingNew" colspan="3">
                <cong:div styleClass="floatLeft">Deliver To Whse</cong:div>
            </cong:td>
            <cong:td width="15%" styleClass="td"></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table border="0" width="85%">
        <cong:tr>
            <cong:td width="10%"></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl" width="3%">WhseName</cong:td>
            <cong:td><cong:autocompletor name="whsecompanyName" id="whsecompanyName" width="500" query="DELWHSE" fields="NULL,NULL,whseState,whseZip,whseAddress,whsePhone,whsecity" styleClass="textLclWidth textLCLuppercase" template="delwhse" value="${lclQuotePickupInfoForm.whsecompanyName}" scrollHeight="200px" shouldMatch="true"/></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
            <cong:td><cong:text name="whseAddress" id="whseAddress" styleClass="textlabelsBoldForTextBox textLCLuppercase" style="width:200px" value="${lclQuotePickupInfoForm.whseAddress}"/></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
            <cong:td>
                <cong:text name="whseCity" id="whsecity" styleClass="textlabelsBoldForTextBox textLCLuppercase" value="${lclQuotePickupInfoForm.whseCity}" container="NULL" style="width:66px" maxlength="50"/>
                <cong:text name="whseState" id="whseState" styleClass="textlabelsBoldForTextBox textLCLuppercase" value="${lclQuotePickupInfoForm.whseState}" container="NULL" style="width:19px" maxlength="50"/>
                <cong:text name="whseZip" id="whseZip" styleClass="textlabelsBoldForTextBox textLCLuppercase" value="${lclQuotePickupInfoForm.whseZip}" container="NULL" style="width:40px" maxlength="50"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">phone</cong:td>
            <cong:td><cong:text  name="whsePhone" id="whsePhone" styleClass="textlabelsBoldForTextBox textLCLuppercase" value="${lclQuotePickupInfoForm.whsePhone}" maxlength="50"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table border="0" width="100%">
        <cong:tr>
            <cong:td  width="8%" styleClass="textlabelsBoldforlcl">Instructions
            </cong:td>
            <cong:td  width="92%"> <cong:textarea  name="pickupInstructions" id="pickupInstructions" styleClass="textWide textlabelsBoldForTextBox" style="width:84%" value="${lclQuotePickupInfoForm.pickupInstructions}" container="NULL"></cong:textarea></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Commodity
            </cong:td>
            <cong:td><cong:text  name="commodityDesc"  id="commodityDesc" styleClass="textLCLuppercase textlabelsBoldForTextBox" style="width:84%" value="${lclQuotePickupInfoForm.commodityDesc}" container="NULL" onkeyup="limitText(this,255)"/></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">TOS</cong:td>
            <cong:td><cong:textarea  name="termsOfService" id="termsOfService" styleClass="textLCLuppercase textlabelsBoldForTextBox" style="width:84%" value="${lclQuotePickupInfoForm.termsOfService}" container="NULL"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:tr>
        <cong:td>
            <input type="button" value="Save" class="button-style1"  id="savePickup" onclick="submitPickUpinfoQuote('save', '${lclQuotePickupInfoForm.moduleName}');"/>
            <input type="button" value="Carrier Rates" id="carrierRates" class="button-style1 carrier" onclick="submitPickUpinfoQuote('callCts', '${lclQuotePickupInfoForm.moduleName}');"/>
        </cong:td>
    </cong:tr>
    <cong:hidden name="methodName" id="methodName"/>
</cong:form>
<script type="text/javascript">
    jQuery(document).ready(function () {
        shipManualClient();
        var originUnlocationCode = parent.$('#originUnlocationCode').val();
        var fileId = parent.$('#fileNumberId').val();
        if ($('#dupShipper').is(":checked")) {
            copyShipper();
        }
        updateDeliverToWarehouse();
        copyPickupDetailsFromExistingQuote();
        showShipperToDetails();
    });


//    function whseDetails(originUnlocationCode) {
//        $.ajaxx({
//            dataType:"json",
//            data: {
//                className: "com.gp.cong.lcl.dwr.LclDwr",
//                methodName: "getdeliverCargoDetailsForWhse",
//                param1: originUnlocationCode,
//                dataType:"json"
//            },
//            success: function (data) {
//                if (data != undefined && data != "" && data != null) {
//                    $('#whsecompanyName').val(data[0]);
//                    $('#whseAddress').val(data[1]);
//                    $('#whsecity').val(data[2]);
//                    $('#whseState').val(data[3]);
//                    $('#whseZip').val(data[4]);
//                    $('#whsePhone').val(data[5])
//                }
//            }
//        });
//    }

    function editCharge(txt) {
        var pickup = $('#chargeAmount').val();
        var pickupCost = $('#pickupCost').val();
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    $('#clearCharge').hide();
                    $('#chargeAmount').removeClass('textlabelsBoldForTextBoxDisabledLook');
                    $('#chargeAmount').removeAttr("readOnly");
                    $('#chargeAmount').addClass("textlabelsBoldForTextBox");
                    $.prompt.close();
                    hideProgressBar();
                }
                else if (v == 2) {
                    $('#chargeAmount').val(pickup);
                    $('#chargeAmount').val(pickupCost);
                    $.prompt.close();
                }
            }
        });
    }
    //                    function doorCity() {
    //                        var doorOrigin = parent.document.getElementById("doorOriginCityZip").value;
    //                        if (doorOrigin == "") {
    //                            var cityState = $('#cityStateZip').val();
    //                            parent.$('#doorOriginCityZip').val(cityState);
    //                        }
    //                        else {
    //                            var cityState = $('#OriginCityZip').val();
    //                            parent.$('#doorOriginCityZip').val(cityState);
    //                        }
    //                    }
    function congAlert(txt) {
        $.prompt(txt);
    }

    function copyShipper() {
        var shipName = parent.$('#shipperNameClient').val();
        var dupShipperName = parent.$('#dupShipName').val();
        var shipAccount = parent.$('#shipperCode').val();
        var shipaddress = parent.$('#shipperAddress').val();
        var shipCity = parent.$('#shipperCity').val();
        var shipState = parent.$('#shipperState').val();
        var shipZip = parent.$('#shipperZip').val();
        var shipPhone = parent.$('#shipperPhone').val();
        var shipFax = parent.$('#shipperFax').val();
        var shipContactName = parent.$('#shipperContactClient').val();
        var shipEmail = parent.$('#shipperEmail').val();
        if ($('#dupShipper').is(":checked")) {
            if (shipName != "") {
                $("#dojoPickUpShipper").show();
                $("#manualShipp").hide();
                $("#dupShipName").val('');
                $("#shipperAccountNo").val(shipAccount);
                $('#shipSupplier').val(shipName);
                $('#newCompanyName').attr('checked', false);
            } else if (dupShipperName != "") {
                $("#manualShipp").show();
                $("#dojoPickUpShipper").hide();
                $('#shipSupplier').val('');
                $("#shipperAccountNo").val('');
                $("#dupShipName").val(dupShipperName);
                $('#newCompanyName').attr('checked', true);
            }
            $('#toAccountNo').val(shipAccount);
            $('#address').val(shipaddress);
            $('#phone1').val(shipPhone);
            $('#contactName').val(shipContactName);
            $('#shipperCity').val(shipCity);
            $('#shipperState').val(shipState);
            $('#fax1').val(shipFax);
            $('#email1').val(shipEmail);
            $('#zip').val(shipZip);
            $('#manualShipper').attr("checked", false);
            $('#tempShipper').val("");
            $('#dojoTPShipper').show();
            $('#shipperManual').hide();
        } else {
            $('#shipSupplier').val("");
            $('#shipperAccountNo').val("");
            $("#dupShipName").val("");
            $('#toAccountNo').val("");
            $('#shipperCity').val('');
            $('#shipperState').val('');
            $('#address').val("");
            $('#phone1').val("");
            $('#contactName').val("");
            $('#zip').val("");
            $('#fax1').val("");
            $('#email1').val("");
            $('#tempShipper').val("");
            $('#dojoTPShipper').show();
            $('#shipperManual').hide();
            $("#dojoPickUpShipper").show();
            $("#manualShipp").hide();
            $('#newCompanyName').attr('checked', false);
        }
    }

    function showShipperToDetails() {
        var manualCompanyName = $('#manualCompanyName').val();
        if (manualCompanyName !== "") {
            $('#manualShipper').attr("checked", true);
            $('#toFieldText').show();
            $('#toFieldDojo').hide();
        } else {
            $('#manualShipper').attr("checked", false);
            $('#toFieldText').hide();
            $('#toFieldDojo').show();
        }
    }
    function newManualShipper() {
        if ($('#manualShipper').is(":checked")) {
            $('#toFieldDojo').hide();
            $('#toFieldText').show();
            $('#toDojo').val('');
            $('#toVendorNo').val('');
        } else {
            $('#toFieldDojo').show();
            $('#toFieldText').hide();
            $('#manualCompanyName').val('');
            $('#toDojo').val('');
        }
    }

    $('#shipSupplier').keyup(function () {
        var shipSupplier = $('#shipSupplier').val();
        if (shipSupplier == "") {
            $('#shipSupplier').val('');
            $('#shipperAccountNo').val('');
            $('#address').val('');
            $('#fax1').val('');
            $('#phone1').val('');
        }
    });
    function shipManualClient() {
        var dojoShipper = $('#shipSupplier').val();
        var Shipper = $('#manualCompanyName').val();
        var spAcct = $('#spAcct').val();
        if (dojoShipper != "" && Shipper != "" & spAcct == "") {
            $('#manualShipper').attr("checked", true);
        }
    }

    function copyPickupDetailsFromExistingQuote() {
        var fileNumberId = $('#fileNumberId').val();
        if (fileNumberId == null || fileNumberId == "" || fileNumberId == '0') {
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
    function updateDeliverToWarehouse() {
        if ($('#whsecompanyName').val() === null || $('#whsecompanyName').val() === '') {
            $('#whsecompanyName').val(parent.$('#deliverCargoToName').val() + "-" + parent.$('#deliveryCargoToCode').val());
            $('#whseAddress').val(parent.$('#deliverCargoToAddress').val());
            $('#whsecity').val(parent.$('#deliverCargoToCity').val());
            $('#whseState').val(parent.$('#deliverCargoToState').val());
            $('#whseZip').val(parent.$('#deliverCargoToZip').val());
            $('#whsePhone').val(parent.$('#deliverCargoToPhone').val());
        }
    }

</script>
</body>
