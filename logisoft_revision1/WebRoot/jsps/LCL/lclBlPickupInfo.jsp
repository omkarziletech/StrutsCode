<%@include file="init.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<body style="background:#ffffff">
    <cong:form name="lclBlPickupInfoForm" id="lclBlPickupInfoForm" action="/lclBlPickupInfo.do">
        <cong:hidden name="fileNumberId" value="${lclBlPickupInfoForm.fileNumberId}"/>
        <cong:hidden name="fileNumber" value="${lclBlPickupInfoForm.fileNumber}"/>
        <cong:hidden name="sailDate" id="sailDate" value="${lclBlPickupInfoForm.sailDate}"/>
        <cong:hidden name="origin" id="origin" value="${lclBlPickupInfoForm.origin}"/>
        <cong:hidden name="rateType" id="rateType" value="${lclBlPickupInfoForm.rateType}"/>
        <cong:hidden name="doorOriginCityZip" value="${doorOriginCityZip}"/>
        <cong:hidden name="lclBookingPadId" value="${lclBookingPad.id}"/>
        <cong:table style="width:100%" border="0">
            <cong:tr>
                <cong:td styleClass="tableHeadingNew" colspan="3">
                    <cong:div styleClass="floatLeft">Pickup Info for File No:
                        <span class="fileNo">${lclBlPickupInfoForm.fileNumber}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                        DoorOriginCity:<span class="fileNo">${doorOriginCityZip}</span>
                    </cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="25%">
                    <cong:table>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Issuing Terminal</cong:td>
                            <cong:td>
                                <cong:autocompletor name="issuingTerminal" id="issuingTerminal" template="commTempFormat" styleClass="text mandatory textlabelsBoldForTextBox textuppercaseLetter" query="CONCAT_TERM_BL" fields="NULL,trmnum" width="250" value="${lclBlPickupInfoForm.issuingTerminal}" container="NULL" shouldMatch="true"/>
                                <cong:hidden name="trmnum"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">To</cong:td>
                            <cong:td>
                                <cong:text name="to" id="to"  styleClass="textlabelsBoldForTextBox textuppercaseLetter" container="NULL" />
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Ship/Supplier</cong:td>
                            <cong:td>
                                <cong:autocompletor name="companyName" template="pickup" id="shipSupplier" fields="spAcct,NULL,contactName,phone1,fax1,email1,address,cityStateZip,delCity,delState,delZip"
                                                    styleClass="textlabelsBoldForTextBox textuppercaseLetter" query="PICKUP"  width="350" container="NULL" shouldMatch="true" value="${lclBlPickupInfoForm.companyName}"/>
                                <cong:checkbox name="dupShipper" id="dupShipper" onclick="copyShipper();" onmouseover="tooltip.showSmall('Checked=Copy from Shipper')" onmouseout="tooltip.hide();" container="NULL"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Account#</cong:td>
                            <cong:td><cong:text name="spAcct" id="spAcct" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.spAcct}"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="4" rows="3" name="address" id="address" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.address}" container="NULL"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">City/State/Zip</cong:td>
                            <cong:td>
                                <div id="loadCityValues">
                                    <cong:text name="OriginCityZip" id="OriginCityZip" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${doorOriginCityZip}" container="NULL" maxlength="50"/>
                                </div>
                                <div id="emptyCityValues">
                                    <cong:text name="cityStateZip" id="cityStateZip" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${doorOriginCityZip}" container="NULL" maxlength="50"/>
                                </div>

                            </cong:td>
                            <cong:hidden name="cityStateZip" id="cityStateZip" value="${lclBlPickupInfoForm.cityStateZip}"/>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Phone</cong:td>
                            <cong:td>
                                <cong:text name="phone1" id="phone1" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.phone1}" container="NULL" maxlength="50"/>
                            </cong:td>
                        </cong:tr>

                    </cong:table>
                </cong:td>
                <cong:td width="40%" align="left">
                    <cong:table>
                        <cong:tr>
                            <cong:td width="40%" id="pickupCharge" colspan="2">
                                <c:import url="/jsps/LCL/ajaxload/pickupcharge.jsp"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Fax</cong:td>
                            <cong:td>
                                <cong:text name="fax1" id="fax1" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.fax1}" container="NULL" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Contact Email</cong:td>
                            <cong:td>
                                <cong:text name="email1" id="email1" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.email1}" container="NULL" maxlength="100"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Contact Name</cong:td>
                            <cong:td> 
                                <cong:text name="contactName" id="contactName" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.contactName}" container="NULL" maxlength="150"/>
                            </cong:td>
                        </cong:tr>

                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Shipper Hours</cong:td>
                            <cong:td>
                                <cong:text name="pickupHours" id="pickupHours" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.pickupHours}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Ready Date</cong:td>
                            <cong:td>
                                <cong:calendarNew name="pickupReadyDate" styleClass="textlabelsBoldForTextBox textuppercaseLetter" id="pickupReadyDate" value="${lclBlPickupInfoForm.pickupReadyDate}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">CutOff Date</cong:td>
                            <cong:td>
                                <cong:calendarNew name="pickupCutoffDate" styleClass="textlabelsBoldForTextBox textuppercaseLetter" id="pickupCutoffDate" value="${lclBlPickupInfoForm.pickupCutoffDate}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">CutOff Note</cong:td>
                            <cong:td>
                                <cong:text name="pickupReadyNote" id="pickupReadyNote" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.pickupReadyNote}" maxlength="150"/>
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </cong:td>
                <cong:td width="30%" align="left"></cong:td>
            </cong:tr>
        </cong:table>
        <cong:table width="100%" border="0">
            <cong:tr>
                <cong:td width="8%" styleClass="textlabelsBoldforlcl">Special/Ref#</cong:td>
                <cong:td width="92%"><cong:textarea rows="2" name="pickupReferenceNo" id="pickupReferenceNo" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:85%" value="${lclBlPickupInfoForm.pickupReferenceNo}"/></cong:td>
            </cong:tr>
        </cong:table>
        <cong:table>
            <cong:tr>
                <cong:td width="8%" styleClass="textlabelsBoldforlcl"></cong:td>
                <cong:td styleClass="tableHeadingNew" colspan="3">
                    <cong:div styleClass="textlabelsBoldforlcl floatLeft">DEL TO WHSE</cong:div>
                </cong:td>
                <cong:td width="14%" styleClass="td"></cong:td>
            </cong:tr>
        </cong:table>
        <cong:table border="0" width="100%">
            <cong:tr>
                <cong:td width="1%"></cong:td>
                <cong:td>&nbsp;</cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">WhseName</cong:td>
                <cong:td><cong:autocompletor name="whsecompanyName" id="whsecompanyName" width="250" query="DELWHSE" fields="whseState,whseZip,whseAddress,whsePhone,whsecity" template="client" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.whsecompanyName}" shouldMatch="true"/></cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;City</cong:td>
                <cong:td>
                    <cong:text name="whseCity" id="whsecity" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.whseCity}" container="NULL" style="width:66px" maxlength="50"/>
                    <cong:text name="whseState" id="whseState" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.whseState}" container="NULL" style="width:19px" maxlength="50"/>
                    <cong:text name="whseZip" id="whseZip" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.whseZip}" container="NULL" style="width:40px" maxlength="50"/>
                </cong:td>
            </cong:tr>
        </cong:table>
        <cong:table border="0" width="100%">
            <cong:tr>
                <cong:td width="6%" styleClass="td"></cong:td>
                <cong:td>&nbsp;</cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
                <cong:td><cong:textarea cols="14" name="whseAddress" id="whseAddress" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.whseCity}"/></cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">phone</cong:td>
                <cong:td><cong:text  name="whsePhone" id="whsePhone" styleClass="textlabelsBoldForTextBox textuppercaseLetter" value="${lclBlPickupInfoForm.whsePhone}" maxlength="50"/></cong:td>
            </cong:tr>
        </cong:table>
        <cong:table border="0" width="100%">
            <cong:tr>
                <cong:td  width="8%" styleClass="textlabelsBoldforlcl">Instructions
                </cong:td>
                <cong:td  width="92%"> <cong:textarea  name="pickupInstructions" id="pickupInstructions" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:84%" value="${lclBlPickupInfoForm.pickupInstructions}" container="NULL"></cong:textarea></cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Commodity
                </cong:td>
                <cong:td><cong:text  name="commodityDesc"  id="commodityDesc" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:84%" value="${lclBlPickupInfoForm.commodityDesc}" container="NULL"/></cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">TOS</cong:td>
                <cong:td><cong:textarea  name="termsOfService" id="termsOfService" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:84%" value="${lclBlPickupInfoForm.termsOfService}" container="NULL"/></cong:td>
            </cong:tr>
        </cong:table>
        <cong:tr>
            <cong:td>
                <input type="button" value="Save" class="button-style1" id="savePickup" onclick="submitForm('save');
                        doorCity();"/>
                <input type="button" value="Carrier Rates" class="button-style1 carrier" onclick="callCTS('#origin', '#sailDate', '#fileNumberId', '#rateType')"/>
                <input type="button" value="Send EDI" class="button-style1" id="SendEDI"/>
            </cong:td>
        </cong:tr>
        <cong:hidden name="methodName" id="methodName"/>
    </cong:form>
    <script type="text/javascript">
        jQuery(document).ready(function () {
            var doorOrigin = parent.document.getElementById("doorOriginCityZip").value;
            if (doorOrigin == "") {
                $("#loadCityValues").hide();
                $("#emptyCityValues").show();
            } else {
                $("#loadCityValues").show();
                $("#emptyCityValues").hide();
            }

        });
        function submitForm(methodName) {
            $("#methodName").val(methodName);
            $("#lclBlPickupInfoForm").submit();
            parent.$.fn.colorbox.close();
        }
        function doorCity() {
            var doorOrigin = parent.document.getElementById("doorOriginCityZip").value;
            if (doorOrigin == "") {
                var cityState = $('#cityStateZip').val();
                parent.$('#doorOriginCityZip').val(cityState);
            }
            else {
                var cityState = $('#OriginCityZip').val();
                parent.$('#doorOriginCityZip').val(cityState);
            }
        }
        function congAlert(txt) {
            $.prompt(txt);
        }
        function validatePickup()
        {
            var terminal = $('#issuingTerminal').val();
            if (terminal == "") {
                congAlert("IssuingTerminal is Required");
            }
        }

        function copyShipper()
        {
            var shipName = parent.$('#shipperName').val();
            var shipAccount = parent.$('#shipperCode').val();
            var shipaddress = parent.$('#shipperAddress').val();
            var shipCity = parent.$('#shipperCity').val();
            var shipState = parent.$('#shipperState').val();
            var shipZip = parent.$('#shipperZip').val();
            var shipPhone = parent.$('#shipperPhone').val();
            var shipFax = parent.$('#shipperFax').val();
            var shipContactName = parent.$('#shipperContactName').val();
            var shipEmail = parent.$('#shipperEmail').val();
            if ($('#dupShipper').is(":checked")) {
                $('#shipSupplier').val(shipName);
                $('#spAcct').val(shipAccount);
                $('#address').val(shipaddress);
                if (shipCity == "" && shipState == "") {
                    $('#cityStateZip').val(shipZip);
                } else if (shipCity == "" && shipZip == "") {
                    $('#cityStateZip').val(shipState);
                } else if (shipState == "" && shipZip == "") {
                    $('#cityStateZip').val(shipCity);
                } else if (shipState == "") {
                    $('#cityStateZip').val(shipZip + "-" + shipCity);
                } else if (shipZip == "") {
                    $('#cityStateZip').val(shipCity + "-" + shipState);
                } else if (shipCity == "") {
                    $('#cityStateZip').val(shipZip + "-" + shipState);
                } else if (shipState == "" && shipCity == "null" && shipZip == "") {
                    $('#cityStateZip').val('');
                } else {
                    $('#cityStateZip').val(shipZip + "-" + shipCity + "/" + shipState);
                }
                $('#phone1').val(shipPhone);
                $('#contactName').val(shipContactName);
                $('#fax1').val(shipFax);
                $('#Email1').val(shipEmail);
            } else {
                $('#shipSupplier').val("");
                $('#spAcct').val("");
                $('#address').val("");
                $('#cityStateZip').val("");
                $('#phone1').val("");
                $('#contactName').val("");
                $('#fax1').val("");
                $('#Email1').val("");
            }

        }

        function callCTS(originUnLocation, sailDate, fileNumberId, rateType) {
            var fileId = $(fileNumberId).val();
            if ($("#sailDate").val() != null && $("#sailDate").val() == "") {
                congAlert("Please select Sail Date from Upcoming Sailings");
                return false;
            }
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkcommodityinCarrierRatesForBl",
                    param1: fileId,
                    dataType: "json",
                    request: "true"
                },
                async: false,
                success: function (data) {
                    if (data == false) {
                        congAlert("Please add Weight and Measure from Commodity");
                        return false;
                    }
                    else {
                        var fromZip = getFromZip();
                        var href = "lclBlPickupInfo.do?methodName=carrier&originUnLocation=" + $(originUnLocation).val() + "&sailDate=" + $(sailDate).val() + "&fileNumberId=" + $(fileNumberId).val() + "&fromZip=" + fromZip + "&rateType=" + $(rateType).val();
                        $(".carrier").attr("href", href);
                        $(".carrier").colorbox({
                            iframe: true,
                            width: "98%",
                            height: "98%",
                            title: "Carrier"
                        });
                    }
                }
            });
        }

        function getFromZip() {
            var fromZip = '';
            var originCityZip = $("#cityStateZip").val();
            if (originCityZip.indexOf("-") > -1) {
                fromZip = originCityZip.substr(0, originCityZip.indexOf("-"));
            }
            return fromZip;
        }
    </script>
</body>
