<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:form  id="lclCargoPopupForm"  name="lclCargoPopupForm" action="lclCargoPopup.do" >
    <input type="hidden" name="currentLocId" id="currentLocId" value="${currentLocId}"/>
    <cong:hidden name="origin" id="origin" value="${lclCargoPopupForm.origin}"/>
    <cong:hidden name="destination" id="destination" value="${lclCargoPopupForm.destination}"/>
    <cong:hidden name="pol" id="pol" value="${lclCargoPopupForm.pol}"/>
    <cong:hidden name="rateType" id="rateType" value="${lclCargoPopupForm.rateType}"/>
    <cong:hidden name="originCityName" id="originCityName" value="${lclCargoPopupForm.originCityName}"/>
    <cong:hidden name="disposition" id="disposition" value="${lclCargoPopupForm.disposition}"/>
    <cong:hidden name="yesVerifiedClicked" id="yesVerifiedClicked"/>
    <cong:hidden  name="pod" id="pod" value="${booking.portOfDestination.unLocationName}"/>
    <cong:hidden  name="usaPortOfExit" id="usaPortOfExit" value="${bookingImport.usaPortOfExit.unLocationName}"/>
    <input type="hidden" id="loginUserId" name="loginUserId" value="${loginuser.userId}"><%-- Login User Id--%>
    <cong:table width="100%" style="margin:2px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew">
                <cong:div styleClass="floatLeft">Cargo is received in <span style="color: red;">${lclCargoPopupForm.originCityName}</span>
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <table>
        <tr>
            <td>
                <span id="refusedValidate" style="color:blue;font-size: 14px;font-weight: bold;"/>
            </td>
        </tr>
    </table>
    <cong:table align="center" style="font-size: 14px" width="80%">
        <cong:hidden id="fileNumber" name="fileNumber" value="${lclCargoPopupForm.fileNumber}"/>
        <cong:hidden id="fileId" name="fileId"/>
        <cong:hidden id="methodName" name="methodName"/>
        <cong:tr>
            <cong:td  width="100%" align="center">
                <cong:div id="refusedBox" style="font-size:12px;font-weight: bold">Would you like to receive this cargo?
                </cong:div>               
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td>
                <cong:div id="osdCodeBox" styleClass="smallInputPopupBox">
                    <cong:table  border="0" width="100%">
                        <cong:tr>
                            <cong:td style="font-size:12px;font-weight: bold">Why is this cargo being refused?</cong:td>
                            <cong:td align="right">
                                <input type="button" class="button-style1" value="Ok" onclick="if (!validateFormRefuse())
                                            return false;
                                        clickRefuse('${lclFileNumber.id}', '${lclCargoPopupForm.originCityName}');"/>
                                <input type="button" class="button-style1" value="Cancel" onclick="closePopupBox()"/>
                            </cong:td>
                        </cong:tr>                    
                        <cong:tr><cong:td colspan="3">
                                <cong:textarea styleClass="refusedTextarea" id="refusedRemarks" name="refusedRemarks"></cong:textarea></cong:td></cong:tr>
                    </cong:table>
                </cong:div>
            </cong:td></cong:tr>
        <cong:tr><cong:td>
                <cong:div id="unverifyBox" styleClass="smallInputPopupBox">
                    <cong:table  border="0" width="">
                        <cong:tr>
                            <td colspan="2" style="font-size:12px;font-weight: bold">How many Labels would you like to print?&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>
                                <cong:text id="labelsCount" name="labelsCount" styleClass="text number" maxlength="4" style="width:30px"
                                           onkeyup="checkForNumberAndDecimal(this)">

                                </cong:text>
                            </cong:td>
                            <cong:td  align="right">
                                <input type="button" class="button-style1" value="Ok" onclick="if (!validateunVerifyForm())
                                            return false;
                                        clickYesUnverified('${lclFileNumber.id}', '${lclCargoPopupForm.originCityName}');"/>
                                <input type="button" class="button-style1" value="Cancel" onclick="closeUnverifyPopupBox()"/>
                                <input type="button" class="button-style1" value="Skip" onclick="clickYesUnverified('${lclFileNumber.id}', '${lclCargoPopupForm.originCityName}');"/>
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </cong:div>
                <cong:div id="locationPopup" styleClass="smallInputPopupBox" >
                    <cong:table  border="0" width="">
                        <cong:tr>
                            <td colspan="2" style="font-size:12px;font-weight: bold">Please select the Current Location of the cargo:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>
                                <select id="location" name="location" style="width:120px" >
                                    <option>Select</option>
                                    <option  value="${booking.portOfDestination.id}">${booking.portOfDestination.unLocationName}(${booking.portOfDestination.unLocationCode})</option>
                                    <option  value="${bookingImport.usaPortOfExit.id}">${bookingImport.usaPortOfExit.unLocationName}(${bookingImport.usaPortOfExit.unLocationCode})</option>
                                </select>
                            </cong:td>
                            <cong:td  align="right">
                                <input type="button" class="button-style1" value="Ok" onclick="getCurrentLocation('${lclFileNumber.id}', '${lclCargoPopupForm.originCityName}',
                                                '${path}', '${lclFileNumber.fileNumber}',
                                                'true', '${lclCargoPopupForm.origin}',
                                                '${lclCargoPopupForm.destination}', '${lclCargoPopupForm.rateType}',
                                                'V', '${lclCargoPopupForm.pol}');">
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </cong:div>                
            </cong:td></cong:tr>
    </cong:table>
    <br>
    <cong:table align="center" style="font-size: 14px" width="100%" border="0">
        <cong:tr>
            <cong:td width="6%"></cong:td>
            <cong:td width="40%">
                <cong:div id="refusedBox" style="display:block">
                    <span id="spanRefused" class="button-style1" onclick="showBlockPop('#osdCodeBox');"
                          <c:if test="${not empty lclFileNumber &&  lclFileNumber.status =='RF'}">
                              style="visibility: hidden;position: absolute"
                          </c:if>
                          >Refused</span>
                    <span id="spanUnVerified" class="button-style1" onclick="showUnverifyBlock('#unverifyBox');"
                          <c:if test="${not empty lclFileNumber &&  lclFileNumber.status eq 'W' && lclCargoPopupForm.disposition eq 'RUNV'}">
                              style="visibility: hidden;position: absolute"
                          </c:if>
                          >Yes-Unverified</span>
                    <span class="button-style1 addCommodity"
                          onclick="clickYesVerified('${lclFileNumber.id}', '${lclCargoPopupForm.originCityName}',
                                          '${path}', '${lclFileNumber.fileNumber}',
                                          'true', '${lclCargoPopupForm.origin}',
                                          '${lclCargoPopupForm.destination}', '${lclCargoPopupForm.rateType}',
                                          'V', '${lclCargoPopupForm.pol}');">Yes-Verified                    </span>
                    <span class="button-style1" id="reversetoObkg" onclick="reverseToOBKG();"
                          <c:if test="${not empty lclFileNumber &&  lclFileNumber.status ne 'W' && lclCargoPopupForm.disposition ne 'RUNV'}">
                              style="visibility: hidden;position: absolute"
                          </c:if>     
                          >ReverseToOBKG</span>
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
</cong:form>
<script type="text/javascript">
    var transhipMentFlag = false;
    var unverifiedFlag = false;
    var cargoLocation;
    jQuery(document).ready(function () {
        $("#labelsCount").keyup(function (e) {
            $('#refusedValidate').html("");
            if (parseInt($('#labelsCount').val()) === 0) {
                $('#refusedValidate').html('Label Copy cannot be 0')
                $('#labelsCount').val("");
                return false
            }

            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.logiware.common.dao.PropertyDAO",
                    methodName: "getProperty",
                    param1: "LabelPrintCount",
                    dataType: "json"
                },
                success: function (data) {
                    if (parseInt($('#labelsCount').val()) > data) {
                        $('#refusedValidate').html('Labels Copy should be less than <span style=color:red>' + data + '</span>')
                        $('#labelsCount').val('');
                    }
                }
            });
        });
    });
    function closePopup() {
        parent.$.fn.colorbox.close();
    }

    function closeClient(id) {
        $("#id").val(id);
    }
    function cargoAlert(txt) {
        $.prompt(txt);
    }

    function clickYesVerified(fileId, cityName, path, fileNumber, tabFlag, origin, destination, rateType, verifyCargo, pol) {
        var userId = $("#loginUserId").val();
        var pod = $('#pod').val();
        var usaPortOfExit = $('#usaPortOfExit').val();
        var TranShipment = parent.$("#bookingType").val();
        var topButtonClass = parent.$("#lclCargoButton").attr("class") === 'red-background' ? true : false;
        var currentLocId = $("#currentLocId").val();
        if (TranShipment === "T" && (usaPortOfExit !== pod) && !transhipMentFlag && topButtonClass === false) {
            $("#locationPopup").show("slow");
        } else {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "updateStatus",
                    param1: fileId,
                    param2: "WV",
                    param3: cityName,
                    param4: userId,
                    param5: currentLocId,
                    param6: $('#disposition').val(),
                    param7: origin,
                    param8: pol
                },
                async: false,
                success: function (data) {
                    if (data === "true") {
                        parent.cargoVerifyBtnGreen();
                        parent.showReleaseBtn();
                        parent.showPreReleaseBtn();
                        parent.jQuery("#dispoId").text("RCVD");
                        parent.jQuery("#statuslabel").text("WAREHOUSE(Verified)");
                        parent.jQuery("#currentLocation").text(cityName);
                        parent.jQuery("#fileStatus").val('WV');
                        cargoCommodity(path, fileId, fileNumber, tabFlag, origin, destination, rateType, verifyCargo, 'WV');
                    }
                }
            });
        }
    }

    function cargoCommodity(path, fileNumberId, fileNumber, tabFlag, origin, destination, rateType, verifyCargo, status) {
        var moduleName = parent.$('#moduleName').val();
        parent.$("#isFormChanged").val(parent.isFormChanged());
        var editDimFlag = 'false';
        var href = path + "/lclCommodity.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber +
                "&tabFlag=" + tabFlag + "&editDimFlag=" + editDimFlag + "&origin=" + origin + "&destination=" + destination +
                "&rateType=" + rateType + "&verifyCargo=" + verifyCargo + "&moduleName=" + moduleName + "&status=" + status;
        parent.$('#cboxClose').hide();
        parent.$.colorbox({
            iframe: true,
            href: href,
            width: "92%",
            height: "98%",
            title: "Commodity"
        });
    }

    function clickRefuse(fileId, cityName) {
        var comment = $("#refusedRemarks").val();
        var userId = $("#loginUserId").val();
        var currentLocId = $("#currentLocId").val();
        var unlocationCode = parent.$('#originUnlocationCode').val();
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "lclRefusedDetails",
                param1: fileId,
                param2: "RF",
                param3: cityName,
                param4: comment,
                param5: userId,
                param6: unlocationCode
            },
            async: false,
            success: function (data) {
                if (data === "true") {
                    parent.$("#lclCargoButton").removeClass("red-background");
                    parent.$("#lclCargoButton").addClass("button-style1");
                    parent.$("#lclCargoButtonBottom").removeClass("red-background");
                    parent.$("#lclCargoButtonBottom").addClass("button-style1");
                    parent.jQuery("#statuslabel").text("Refused");
                    parent.jQuery("#currentLocation").text('');
                    parent.jQuery("#fileStatus").val('RF');
                    parent.$.fn.colorbox.close();
                    parent.showLoading();
                    parent.$("#methodName").val("editBooking");
                    parent.$("#lclBookingForm").submit();
                }
            }
        });
    }

    function clickYesUnverified(fileId, cityName) {
        var count = $('#labelsCount').val();
        var fileNo = $("#fileNumber").val();

        var currentLocId = $("#currentLocId").val();
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "lclUnverifiedDetails",
                param1: fileId,
                param2: fileNo,
                param3: "WU",
                param4: cityName,
                param5: count,
                param6: currentLocId,
                request: "true"
            },
            async: false,
            success: function (data) {
                if (data) {
                    parent.cargoUnVerifyBtnRed();
                    parent.jQuery("#dispoId").text("RUNV");
                    parent.jQuery("#statuslabel").text("WAREHOUSE(Un-Verified)");
                    parent.jQuery("#fileStatus").val('WU');
                    parent.$.fn.colorbox.close();
                    parent.showLoading();
                    var unVerified = parent.$("#isFormChanged").val(parent.isFormChanged()) ? "saveBooking" : "editBooking";
                    parent.$("#methodName").val(unVerified);
                    parent.$("#lclBookingForm").submit();
                }
            }
        });
    }
    function showDetails(methodName) {
        $("#methodName").val(methodName);
        $("#lclCargoPopupForm").submit();
    }
    function showBlockPop(tar) {
        $('.smallInputPopupBox').hide();
        $('#refusedBox').hide();
        $(tar).show("slow");
    }
    function showUnverifyBlock(tar) {
        var transhipMent = parent.$("#bookingType").val();
        var pod = $('#pod').val();
        var usaPortOfExit = $('#usaPortOfExit').val();
        if (transhipMent === "T" && usaPortOfExit !== pod) {
            $("#locationPopup").show("slow");
            unverifiedFlag = true;
        }
        else {
            showUnverifiedBox(tar);
        }
    }

    function showUnverifiedBox(tar) {
        $('.smallInputPopupBox').hide();
        $('#refusedBox').hide();
        $(tar).show("slow");
    }
    function hideBlock(tar) {
        $(tar).hide("slow");
    }
    function closePopupBox() {
        $("#refusedValidate").hide();
        $("#refusedBox").show();
        $("#osdCodeBox").hide();
        $("#refusedRemarks").val('');
    }
    function closeUnverifyPopupBox() {
        $("#refusedValidate").hide();
        $("#refusedBox").show();
        $("#unverifyBox").hide();
        $("#labelsCount").val('');
        $("#location").val('');
    }

    function validateFormRefuse() {
        $('#refusedValidate').html("");
        var result = true;
        var refuse = $('#refusedRemarks').val();
        if (refuse == null || refuse == "") {
            $('#refusedValidate').html('Refused Note is required')
            $("#refusedRemarks").css("border-color", "red");
            return false;
        }
        return result;
    }
    function validateunVerifyForm() {
        $('#refusedValidate').html("");
        var result = true;
        var labels = $('#labelsCount').val();
        if (labels == null || labels == "") {
            $('#refusedValidate').html('Label is required')
            $("#labelsCount").css("border-color", "red");
            $("#warning").parent.show();
            return false;
        }
        return result;
    }

    function congAlert(txt) {
        $.prompt(txt);
    }
    function checkForNumberAndDecimal(obj) {
        //$('#refusedValidate').html("");
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            $('#refusedValidate').html('This should be Numeric');
            $("#labelsCount").css("border-color", "red");
            obj.value = "";
            return false;
        }
    }
    function getCurrentLocation(fileId, cityName, path, fileNumber, tabFlag, origin, destination, rateType, verifyCargo, pol) {
        cargoLocation = $("#location").val();
        if (cargoLocation === "Select") {
            $.prompt("Please select the Current Location of the cargo ");
        } else if (unverifiedFlag) {
            $("#currentLocId").val(cargoLocation);
            showUnverifiedBox('#unverifyBox');
        }
        else {
            $("#currentLocId").val(cargoLocation);
            $("#locationPopup").hide();
            transhipMentFlag = true;
            clickYesVerified(fileId, cityName, path, fileNumber, tabFlag, origin, destination, rateType, verifyCargo, pol);
        }
    }
    function reverseToOBKG() {
        var releaseClass = parent.$("#lclReleaseButton1").attr('class') === 'green-background' ? true : false;
        var preReleaseClass = parent.$("#lclPreReleaseButton1").attr('class') === 'green-background' ? true : false;
        if (releaseClass) {
            $.prompt("DR is already Released");
        }else if(preReleaseClass){
            $.prompt("DR is already Pre-Released");
        }else{
        $.prompt("Are you sure?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    window.parent.showLoading();
                    parent.$("#methodName").val("reverseToOBKG");
                    parent.$("#lclBookingForm").submit();
                } else {
                    $.prompt.close();
                }
            }
        });
    }
    }
</script>
