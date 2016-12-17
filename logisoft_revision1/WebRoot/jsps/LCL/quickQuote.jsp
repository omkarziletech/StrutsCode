<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%--
    Document   : quickDR
    Created on : Jan 18, 2013, 7:02:14 PM
    Author     : Mahalakshmi
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="init.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<cong:body>
    <cong:form name="lclQuoteForm" id="lclQuoteForm" action="lclQuote.do">
        <cong:table border="0">
            <cong:tr>
                <cong:td colspan="8" styleClass="tableHeadingNew" width="100%">
                    Quick Quote
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Customer Name</cong:td>
                <cong:td><cong:autocompletor name="clientCompanyForDr" template="tradingPartner" id="clientCompanyForDr"
                                             fields="clientAcctForDr,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,NULL,dupClientPhone,dupClientFax,NULL,commodityNumber,fmcNo,otiNumber,clientPoaClient,creditForClient,retailCommodity"
                                             query="CLIENT_WITH_CONSIGNEE" value="${clientCompanyForDr}"  width="600" container="NULL"
                                             shouldMatch="true" styleClass="textlabelsBoldForTextBox" scrollHeight="250px" callback="fillTariffDetails('')"/>
                    Cust#
                    <cong:hidden name="commodityNumber" id="commodityNumber" value="${commodityNumber}"/>
                    <input type="hidden" name="retailCommodity" id="retailCommodity" value="${retailCommodity}"/>
                    <input type="hidden" name="rateAmount" id="rateAmount" value="${rateAmount}"/>
                    <cong:text name="clientAcctForDr" id="clientAcctForDr" value="${clientAcctForDr}" style="width:85px" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td valign="middle" styleClass="textlabelsBoldforlcl">CTC/Retail/FTF<span style="color:red;font-size: 19px;vertical-align: middle">*</span></cong:td><cong:td>
                    <input type="radio" value="C" name="rateTypeForDr" id="rateCtc" onclick="checkOriginTerminalQuickQuote();
                            fillTariffDetails('c');
                            getRegionsForOrgDest();"/> C
                    <input type="radio" value="R" name="rateTypeForDr" id="rateR" onclick="checkOriginTerminalQuickQuote();
                            fillTariffDetails('r');
                            getRegionsForOrgDest();"/> R
                    <input type="radio" value="F" name="rateTypeForDr" id="rateFtf" onclick="checkOriginTerminalQuickQuote();
                            fillTariffDetails('f');
                            getRegionsForOrgDest();"/> FTF
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="13%">
                    Origin CFS
                </cong:td>
                <cong:td colspan="2">
                    <cong:autocompletor id="portOfOriginForDr" name="portOfOriginForDr"
                                        template="one" fields="NULL,NULL,originUnlocationCodeForDr,portOfOriginIdForDr" query="RELAYNAME" styleClass="mandatory textlabelsBoldForTextBox"
                                        width="250" container="NULL" shouldMatch="true" scrollHeight="200px" callback="clearRates('');getRegionsForOrgDest();checkOriginTerminalQuickQuote();"  value="${portOfOriginForDr}"/>
                </cong:td>
                <cong:hidden name="portOfOriginIdForDr" id="portOfOriginIdForDr"  value="${portOfOriginIdForDr}"/>
                <input type="hidden" name="originUnlocationCodeForDr" id="originUnlocationCodeForDr" value="${originUnlocationCodeForDr}"/>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Destination</cong:td>
                <cong:td colspan="2">
                    <cong:autocompletor id="finalDestinationForDr" name="finalDestinationForDr" template="one" fields="unlocationName,NULL,unlocationCodeForDr,finalDestinationIdForDr" callback="clearRates('');getRegionsForOrgDest();"
                                        query="CONCAT_RELAY_NAME_FD" styleClass="mandatory textlabelsBoldForTextBox"  width="250" container="NULL" shouldMatch="true" scrollHeight="200px" value="${finalDestinationForDr}"/>
                </cong:td>
                <cong:hidden name="finalDestinationIdForDr" id="finalDestinationIdForDr" value="${finalDestinationIdForDr}"/>
                <input type="hidden" name="unlocationCodeForDr" id="unlocationCodeForDr" value="${unlocationCodeForDr}"/>
                <cong:td>&nbsp;</cong:td>
                <cong:td>&nbsp;</cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Tariff</cong:td>
                <cong:td>
                    <cong:autocompletor name="commodityTypeForDr" id="commodityTypeForDr" template="two" fields="commodityNoForDr,hazmatDr,commodityTypeId" query="COMMODITYTYPEFORRATES" width="250" styleClass="textlabelsBoldForTextBox  mandatory"
                                        value="${commodityTypeForDr}" container="NULL" scrollHeight="250px" paramFields="originDr,destinationDr"
                                        callback="clearRates('');" shouldMatch="true"/>
                    Tariff #
                    <cong:autocompletor name="commodityNoForDr" id="commodityNoForDr" template="two" fields="commodityTypeForDr,hazmatDr,commodityTypeId" query="COMMODITY_TYPE_CODE" width="250" styleClass="text weight textlabelsBoldForTextBox"
                                        value="${commodityNoForDr}" container="NULL"
                                        scrollHeight="200px" shouldMatch="true" />
                </cong:td>
                <input type="hidden" name="packageDr" id="packageDr" value="Packages" />
                <cong:hidden name="commodityTypeId" id="commodityTypeId" value="${commodityTypeIdForDr}"/>
                <input type="hidden" name="packageIdDr" id="packageIdDr" value="175"/>
                <input type="hidden" name="rateType" id="rateType" value="${rateTypeDr}"/>
                <input type="hidden" name="originDr" id="originDr" value="${originDr}"/>
                <input type="hidden" name="hazmatDr" id="hazmatDr" value="${hazmatDr}"/>
                <input type="hidden" name="destinationDr" id="destinationDr" value="${destinationDr}"/>
                <input type="hidden" name="stdRateBasis" id="stdRateBasis" value="${stdRateBasis}"/>
            </cong:tr>
        </cong:table>
        <cong:table> <cong:tr>
                <cong:td>&nbsp;</cong:td>
                <cong:td>&nbsp;</cong:td>
                <cong:td>&nbsp;</cong:td>
                <cong:td>&nbsp;</cong:td>
            </cong:tr></cong:table>
        <div class="button-style1 saveDr" onclick="submitQuickQuote('${path}');" >
            Submit
        </div>
        <div class="button-style1" onclick="convertToQuote('quickQuote', '${path}')">
            Convert to Quote
        </div>
        <div>
            <c:if test="${not empty chargeList}">
                <display:table class="dataTable" name="${chargeList}" id="chargesTable">
                    <display:column title="" style="width:150Px;">${chargesTable.label1}</display:column>
                    <display:column title="Charge Code" style="width:100Px;">${chargesTable.arglMapping.chargeCode}</display:column>
                    <display:column title="Charge Desc" style="width:200Px;">${chargesTable.arglMapping.chargeDescriptions}</display:column>
                    <display:column title="Rate" style="width:250Px;">${chargesTable.label2}
                        <c:if test="${not empty rateAmount && chargesTable.arglMapping.chargeCode=='OCNFRT'}">
                            <span class="cons">
                                <cong:label text="More Info" style="font-weight:bolder;color:#0000FF" id="rate" />
                            </span>
                        </c:if>
                    </display:column>
                </display:table>
            </c:if>
        </div>
    </cong:form>
    <script>
        function congAlert(txt) {
            $.prompt(txt);
        }
        function validateform() {
            var rateType = $('input:radio[name=rateTypeForDr]:checked').val();
            if (document.getElementById("portOfOriginForDr") == null || document.getElementById("portOfOriginForDr").value == "") {
                congAlert("Origin CFS is required");
                $("#portOfOriginForDr").css("border-color", "red");
                $("#warning").parent.show();
                return false;
            }
            else if (document.getElementById("finalDestinationForDr").value == null || document.getElementById("finalDestinationForDr").value == "") {
                congAlert("Destination is required");
                $("#finalDestinationForDr").css("border-color", "red");
                $("#warning").parent.show();
                return false;
            }
            else if (document.getElementById("commodityTypeForDr") == null || document.getElementById("commodityTypeForDr").value == "") {
                congAlert("Tariff# is Required")
                $("#commodityTypeForDr").css("border-color", "red");
                $("#warning").parent.show();
                return false;
            } else if (rateType == null || rateType == "" || rateType == undefined) {
                congAlert("RateType is required");
                return false;
            }
            return true;
        }

        function fillTariffDetails(index) {
            getRegionsForOrgDest();
            var rateType = $('input:radio[name=rateTypeForDr]:checked').val();
            var commodityNumber = "";
            if (rateType != "" && rateType != null && rateType != "undefined") {
                if (rateType == 'C') {
                    commodityNumber = $('#commodityNumber').val();
                } else if (rateType == 'R') {
                    commodityNumber = $('#retailCommodity').val();
                }
                if (rateType == 'C' && (trim(commodityNumber) == "" || trim(commodityNumber) == "000000 COMMON COMMODITY")) {
                    commodityNumber = "032500 ";
                }
                if (index == 'r' && ($('#retailCommodity').val() == "" || $('#retailCommodity').val() == null) || index == 'f') {
                    clearTariff();
                }
                if (trim(commodityNumber) != "" && trim(commodityNumber) != null) {
                    var commCode = commodityNumber.substr(0, 6);
                    $.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "getCommodityCode",
                            param1: commCode,
                            dataType: "json"
                        },
                        success: function (data) {
                            if (data != null) {
                                $('#commodityTypeId').val(data[0]);
                                $('#commodityNoForDr').val(data[1]);
                                $('#commodityTypeForDr').val(data[2]);
                            }
                        }
                    });
                }
            } else {
                if ($('#clientCompanyForDr').val() != "" && $('#clientCompanyForDr').val() != null &&
                        $('#commodityNumber').val() != "" && $('#commodityNumber').val() != null && ($('#retailCommodity').val() == "" || $('#retailCommodity').val() == null)) {
                    commodityNumber = $('#commodityNumber').val();
                    if (trim(commodityNumber) == "" || trim(commodityNumber) == "000000 COMMON COMMODITY") {
                        commodityNumber = "";
                    }
                    if (trim(commodityNumber) != "" && trim(commodityNumber) != null) {
                        var commCode = commodityNumber.substr(0, 6);
                        $.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "getCommodityCode",
                                param1: commCode,
                                dataType: "json"
                            },
                            async: false,
                            success: function (data) {
                                if (data != null) {
                                    $('#commodityTypeId').val(data[0]);
                                    $('#commodityNoForDr').val(data[1]);
                                    $('#commodityTypeForDr').val(data[2]);
                                    $('#rateCtc').attr('checked', true);
                                }
                            }
                        });
                    }
                }
            }
            clearRates('');
        }

        function clearRates(val) {
            var table = document.getElementById("chargesTable");
            if (table != null) {
                var rowCount = table.rows.length;
                if (rowCount > 0) {
                    for (var i = 0; i < rowCount; i++) {
                        table.deleteRow(i);
                        i--;
                    }
                }
            }
        }

        function convertToQuote(methodName, path) {
            if (validateform()) {
                var rateTypeForDr = $('input:radio[name=rateTypeForDr]:checked').val();
                var commodityNoForDr = $("#commodityNoForDr").val();
                var commodityTypeForDr = $("#commodityTypeForDr").val();
                var packageIdDr = $("#packageIdDr").val();
                var packageDr = $("#packageDr").val();
                var commodityTypeId = $("#commodityTypeId").val();
                var portOfOriginForDr = $("#portOfOriginForDr").val();
                var portOfOriginIdForDr = $("#portOfOriginIdForDr").val();
                var finalDestinationForDr = $("#finalDestinationForDr").val();
                var finalDestinationIdForDr = $("#finalDestinationIdForDr").val();
                var clientCompanyForDr = $("#clientCompanyForDr").val();
                var clientAcctForDr = $("#clientAcctForDr").val();
                var stdRateBasis = $("#stdRateBasis").val();
                var hazmatDr = $("#hazmatDr").val();
                var originDr = $("#originDr").val();
                var destinationDr = $("#destinationDr").val();
                var href = path + "/lclQuote.do?methodName=quickDr&portOfOriginForDr=" + portOfOriginForDr + "&portOfOriginIdForDr=" + portOfOriginIdForDr + "&finalDestinationForDr=" + finalDestinationForDr + "&finalDestinationIdForDr=" + finalDestinationIdForDr + "&clientCompanyForDr=" + clientCompanyForDr + "&clientAcctForDr=" + clientAcctForDr + "&commodityTypeId=" + commodityTypeId + "&packageDr=" + packageDr + "&packageIdDr=" + packageIdDr + "&commodityTypeForDr=" + commodityTypeForDr + "&commodityNoForDr=" + commodityNoForDr + "&rateTypeForDr=" + rateTypeForDr + "&stdRateBasis=" + stdRateBasis + "&hazmatDr=" + hazmatDr + "&destinationDr=" + destinationDr + "&originDr=" + originDr;
                parent.window.location.href = href;
            }
        }
        function clearTariff() {
            $('#commodityNoForDr').val('')
            $('#commodityTypeForDr').val('')
            $('#commodityTypeId').val('')
        }


        function submitQuickQuote(path) {
            if (validateform()) {
                var rateTypeForDr = $('input:radio[name=rateTypeForDr]:checked').val();
                var commodityNoForDr = $("#commodityNoForDr").val();
                var commodityTypeForDr = $("#commodityTypeForDr").val();
                var packageIdDr = $("#packageIdDr").val();
                var packageDr = $("#packageDr").val();
                var commodityTypeId = $("#commodityTypeId").val();
                var portOfOriginForDr = $("#portOfOriginForDr").val();
                var portOfOriginIdForDr = $("#portOfOriginIdForDr").val();
                var finalDestinationForDr = $("#finalDestinationForDr").val();
                var finalDestinationIdForDr = $("#finalDestinationIdForDr").val();
                var clientCompanyForDr = $("#clientCompanyForDr").val();
                var clientAcctForDr = $("#clientAcctForDr").val();
                var originUnlocationCodeForDr = $("#originUnlocationCodeForDr").val();
                var unlocationCodeForDr = $("#unlocationCodeForDr").val();
                var commodityNo = $("#commodityNumber").val();
                var retailCommodity = $("#retailCommodity").val();
                var hazmatDr = $("#hazmatDr").val();
                var originDr = $("#originDr").val();
                var destinationDr = $("#destinationDr").val();
                var href = path + "/lclQuote.do?methodName=submitQuote&portOfOriginForDr=" + portOfOriginForDr + "&portOfOriginIdForDr=" + portOfOriginIdForDr + "&finalDestinationForDr=" + finalDestinationForDr + "&finalDestinationIdForDr=" + finalDestinationIdForDr + "&clientCompanyForDr=" + clientCompanyForDr + "&clientAcctForDr=" + clientAcctForDr + "&commodityTypeId=" + commodityTypeId + "&packageDr=" + packageDr + "&packageIdDr=" + packageIdDr + "&commodityTypeForDr=" + commodityTypeForDr + "&commodityNoForDr=" + commodityNoForDr + "&rateTypeForDr=" + rateTypeForDr + "&originUnlocationCodeForDr=" + originUnlocationCodeForDr + "&unlocationCodeForDr=" + unlocationCodeForDr + "&retailCommodity=" + retailCommodity + "&commodityNo=" + commodityNo + "&hazmatDr=" + hazmatDr + "&destinationDr=" + destinationDr + "&originDr=" + originDr;
                window.location = href;
            }
        }

        $(document).ready(function () {
            $(document).keydown(function (e) {
                if ($(e.target).attr("readonly")) {
                    if (e.keyCode === 8) {
                        return false;
                    }
                }
            });
            $("#clientCompanyForDr").keyup(function (e) {
                if ($('#clientCompanyForDr').val() == "") {
                    clearTariff();
                    $('#clientAcctForDr').val('');
                    $('#rateCtc').attr('checked', false);
                    $('#rateR').attr('checked', false);
                    $('#rateFtf').attr('checked', false);
                    clearRates('');
                }
            });

            $("#commodityTypeForDr").keyup(function (e) {
                if ($('#commodityTypeForDr').val() == "") {
                    $('#commodityTypeForDr').val('');
                    $('#commodityTypeId').val('');
                    $('#commodityNoForDr').val('');
                }
            });
            checkRateType();
            showRates();
        });

        function checkRateType() {
            var rate = $('#rateType').val();
            if (rate == 'R') {
                $('#rateR').attr('checked', true);
            } else if (rate == 'C') {
                $('#rateCtc').attr('checked', true);
            } else if (rate == 'F') {
                $('#rateFtf').attr('checked', true);
            }
        }
        function showRates() {
            if ($('#rateAmount').val() != '') {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "displayRates",
                        param1: $('#rateAmount').val()
                    },
                    async: false,
                    success: function (data) {
                        JToolTipForLeft('#rate', data, 350);
                        return true;
                    }
                });
            }
        }


        function getRegionsForOrgDest() {
            var origin;
            var destination;
            var originObjR = document.getElementById('portOfOriginForDr');
            if (originObjR != undefined && originObjR != null && originObjR.value != "") {
                originCode = document.getElementById('portOfOriginForDr').value;
                if (originCode.lastIndexOf("(") > -1 && originCode.lastIndexOf(")") > -1) {
                    origin = originCode.substring(originCode.lastIndexOf("(") + 1, originCode.lastIndexOf(")"));
                }
            }
            var destinationObjR = document.getElementById('finalDestinationForDr');
            if (destinationObjR != undefined && destinationObjR != null && destinationObjR.value != "") {
                destinationCode = document.getElementById('finalDestinationForDr').value;
                if (destinationCode.lastIndexOf("(") > -1 && destinationCode.lastIndexOf(")") > -1) {
                    destination = destinationCode.substring(destinationCode.lastIndexOf("(") + 1, destinationCode.lastIndexOf(")"));
                }
            }
            var rateType = $('input:radio[name=rateTypeForDr]:checked').val();
            if (origin != undefined && origin != "" && destination != "" && destination != undefined && rateType != "" && rateType != null && rateType != undefined) {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "getRegions",
                        param1: origin,
                        param2: destination,
                        param3: rateType
                    },
                    async: false,
                    success: function (data) {
                        if (data != null && data != "" && data != "undefined") {
                            var org = data.substring(0, data.indexOf('-'));
                            var dest = data.substring(data.indexOf('-') + 1);
                            $('#originDr').val(org);
                            $('#destinationDr').val(dest);
                        }
                    }
                });
            }
        }

        function checkOriginTerminalQuickQuote() {
            var origin;
            var originObjR = document.getElementById('portOfOriginForDr');
            if (originObjR != undefined && originObjR != null && originObjR.value != "") {
                originCode = document.getElementById('portOfOriginForDr').value;
                if (originCode.lastIndexOf("(") > -1 && originCode.lastIndexOf(")") > -1) {
                    origin = originCode.substring(originCode.lastIndexOf("(") + 1, originCode.lastIndexOf(")"));
                }
            }
            var ratetype = $('input:radio[name=rateTypeForDr]:checked').val();
            var rateTypes;
            if (ratetype == "R") {
                rateTypes = "Retail";
            } else if (ratetype == "C") {
                rateTypes = "Coload";
            } else if (ratetype == "F") {
                rateTypes = "Foreign to Foreign";
            }
            if ((origin != undefined && origin != "") && (ratetype != undefined && ratetype != "")) {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "checkTerminal",
                        param1: origin,
                        param2: ratetype,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data) {
                            congAlert(rateTypes + " Terminal is not available.Please select different origin");
                            $('#portOfOriginForDr').val('');
                            $('#portOfOriginForDr').css("border-color", "red");
                        }
                    }
                });
            }
        }
    </script>
</cong:body>
