<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
    jQuery(document).ready(function () {
        var expressRelease = $("#expressRelease").val();
        var moduleName = $('#moduleName').val();
        if (expressRelease === 'Y') {
            if ($('#originalblbutton').val() === "Reset") {
                var currentUserId = $("#currentuserId").val();
                var fileId = $("#fileNumberId").val();
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "deleteImportRelease",
                        param1: fileId,
                        param2: $('#originalblbutton').val(),
                        param3: currentUserId,
                        param4: moduleName
                    },
                    success: function (data) {
                        if (data === null || data === "") {
                            $('#originalblbutton').attr('disabled', true);
                            $('#originalblbutton').val("No");
                            $('#originalblbutton').removeClass('green-background');
                            $('#originalblbutton').addClass('button-style1');
                            $("#originalDate").text('');
                            $("#originalYes").text('');

                        }
                    }
                });
            } else {
                $('#originalblbutton').attr('disabled', true);
            }
        }
        parent.$('#entryNo').val($('#entryNo1').val());
    });
    function saveRelease() {
        $("#methodName").val('saveRelease');
        $("#importReleaseForm").submit();
        parent.$('#entryNo').val($('#entryNo1').val());
        var originalDate = jQuery("#originalDate").text();
        var expressRelease = $("#expressRelease").val();
        if ((originalDate === null || originalDate === "") && expressRelease === "Y") {
            parent.$('#expressReleaseY').attr('checked', true);
            parent.$('#expressReleaseY').attr('disabled', false);
        } else if ((originalDate === null || originalDate === "") && expressRelease === "N") {
            parent.$('#expressReleaseN').attr('checked', true);
            parent.$('#expressReleaseN').attr('disabled', false);
            parent.$('#expressReleaseY').attr('checked', false);
            parent.$('#expressReleaseY').attr('disabled', false);
        } else {
            parent.$('#expressReleaseY').attr('checked', false);
            parent.$('#expressReleaseY').attr('disabled', true);
        }
        if (jQuery("#cargoOnHoldYes").text() === 'YES') {
            parent.$('#freightRelease').removeClass("button-style1");
            parent.$('#freightRelease').removeClass("purple-background");
            parent.$('#freightRelease').removeClass("green-background");
            parent.$('#freightRelease').removeClass("orange-background");
            parent.$('#freightRelease').addClass("red-background");
        } else if (jQuery("#cargoGeneralOrderYes").text() === 'YES') {
            parent.$('#freightRelease').removeClass("button-style1");
            parent.$('#freightRelease').removeClass("green-background");
            parent.$('#freightRelease').removeClass("orange-background");
            parent.$('#freightRelease').removeClass("red-background");
            parent.$('#freightRelease').addClass("purple-background");
        } else if ((jQuery("#originalYes").text() === 'YES' || expressRelease === 'Y') && jQuery("#freightYes").text() === 'YES' && jQuery("#paymentYes").text() === 'YES') {
            parent.$('#freightRelease').removeClass("orange-background");
            parent.$('#freightRelease').removeClass("button-style1");
            parent.$('#freightRelease').removeClass("purple-background");
            parent.$('#freightRelease').removeClass("red-background");
            parent.$('#freightRelease').addClass("green-background");
        } else if (jQuery("#originalYes").text() === 'YES' || jQuery("#freightYes").text() === 'YES' || jQuery("#paymentYes").text() === 'YES') {
            parent.$('#freightRelease').removeClass("button-style1");
            parent.$('#freightRelease').removeClass("purple-background");
            parent.$('#freightRelease').removeClass("green-background");
            parent.$('#freightRelease').removeClass("red-background");
            parent.$('#freightRelease').addClass("orange-background");
        } else {
            parent.$('#freightRelease').removeClass("purple-background");
            parent.$('#freightRelease').removeClass("red-background");
            parent.$('#freightRelease').removeClass("green-background");
            parent.$('#freightRelease').removeClass("orange-background");
            parent.$('#freightRelease').addClass("button-style1");
        }
        parent.$.fn.colorbox.close();
    }
    function releaseCurrentDate(idName, obj, objYes) {
        var moduleName = $('#moduleName').val();
        var currentUserId = $("#currentuserId").val();
        var loginame = $("#currentLoginName").val();
        var fileId = $("#fileNumberId").val();
        var buttonValues = $("#" + obj).val();
        if ($("#" + obj).val() == "No") {
            $.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "saveImportRelease",
                    param1: fileId,
                    param2: obj,
                    param3: currentUserId,
                    param4: loginame,
                    param5: buttonValues,
                    param6: moduleName
                },
                success: function (data) {
                    if (data != null && data != "") {
                        $('#' + obj).val("Reset");
                        $("#" + obj).addClass('green-background');
                        $("#" + idName).text(data);
                        $("#" + objYes).text('YES');
                    }
                }
            });
        } else {
            var txt = "Are you sure";
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        $.ajaxx({
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "saveImportRelease",
                                param1: fileId,
                                param2: obj,
                                param3: currentUserId,
                                param4: loginame,
                                param5: buttonValues,
                                param6: moduleName
                            },
                            success: function (data) {
                                if (data == null || data == "") {
                                    $('#' + obj).val("No");
                                    $("#" + objYes).text('');
                                    $("#" + obj).removeClass('green-background');
                                    $("#" + obj).addClass('button-style1');
                                    $("#" + idName).text('');
                                }
                            }
                        });
                    }
                    else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        }
    }

</script>
<body style="background:#ffffff">
    <cong:form id="importReleaseForm" name="importReleaseForm" action="/importRelease">
        <cong:table style="width:97%;border: 1px solid #dcdcdc;" border="0" >
            <input type="hidden" id="fileNumberId" name="fileNumberId" value="${importReleaseForm.fileNumberId}"/>
            <input type="hidden" id="methodName" name="methodName"/>
            <input type="hidden" id="currentuserId" name="currentuserId" value="${importReleaseForm.currentuserId}"/>
            <input type="hidden" id="currentLoginName" name="currentLoginName" value="${importReleaseForm.currentLoginName}"/>
            <input type="hidden" id="moduleName" name="moduleName" value="${importReleaseForm.moduleName}"/>
            <input type="hidden" id="expressRelease" name="expressRelease" value="${importReleaseForm.expressRelease}"/>
            <cong:tr styleClass="tableHeadingNew">
                <cong:td width="70%" colspan="6">   Import Release:</cong:td>
                <cong:td style="float:right;" colspan="2" align="right"><div class="button-style1" align="right" onclick="saveRelease()">Save</div>
                </cong:td>
            </cong:tr>
            <cong:tr> <cong:td colspan="8"></cong:td></cong:tr>

            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="5%">Original B/L Received</cong:td>
                <cong:td width="7%" colspan="1">
                    <c:choose><c:when test="${not empty lclBookingImport.originalBlReceived}">
                            <input type="button" id="originalblbutton" class="green-background" onclick="releaseCurrentDate('originalDate', 'originalblbutton', 'originalYes')" value="Reset">
                            <label id="originalYes"  class="greenFontBold">YES</label>
                        </c:when><c:otherwise>
                            <input type="button" id="originalblbutton" class="button-style1" onclick="releaseCurrentDate('originalDate', 'originalblbutton', 'originalYes')" value="No">
                            <label id="originalYes"  class="greenFontBold"></label>
                        </c:otherwise></c:choose>
                </cong:td>
                <cong:td valign="middle" width="12%">
                    <label id="originalDate" class="importReleaseDesign">${originalBl}</label>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl" width="5%">Customs Clearance Received</cong:td>
                <cong:td colspan="1" width="7%">
                    <c:choose><c:when test="${not empty lclBookingImport.customsClearanceReceived}">
                            <input type="button" id="customButton" class="green-background" onclick="releaseCurrentDate('customDate', 'customButton', 'customYes')" value="Reset">
                            <label id="customYes"  class="greenFontBold">YES</label>
                        </c:when><c:otherwise>
                            <input type="button" id="customButton" class="button-style1" onclick="releaseCurrentDate('customDate', 'customButton', 'customYes')" value="No">
                            <label id="customYes"  class="greenFontBold"></label>
                        </c:otherwise></c:choose>
                </cong:td>
                <cong:td valign="middle" width="12%">
                    <label id="customDate" class="importReleaseDesign" >${customsClear}</label>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Entry#</cong:td>
                <cong:td styleClass="">
                    <cong:text name="entryNo" id="entryNo1" value="${importReleaseForm.entryNo}" styleClass="textLCLuppercase" container="NULL"/>
                </cong:td>

                <%--
                --%>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Cargo On Hold</cong:td>
                <cong:td>
                    <c:choose><c:when test="${not empty lclBookingImport.cargoOnHold}">
                            <input type="button" id="cargoOnHoldButton" class="green-background" onclick="releaseCurrentDate('cargoOnHoldDate', 'cargoOnHoldButton', 'cargoOnHoldYes')" value="Reset">
                            <label id="cargoOnHoldYes"  class="greenFontBold">YES</label>
                        </c:when><c:otherwise>
                            <input type="button" id="cargoOnHoldButton" class="button-style1" onclick="releaseCurrentDate('cargoOnHoldDate', 'cargoOnHoldButton', 'cargoOnHoldYes')" value="No">
                            <label id="cargoOnHoldYes"  class="greenFontBold"></label>
                        </c:otherwise></c:choose>
                </cong:td>
                <cong:td valign="middle">
                    <label id="cargoOnHoldDate" class="importReleaseDesign" style="padding-top: 2px">${cargoOnHold}</label>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Delivery Order Received</cong:td>
                <cong:td>
                    <c:choose><c:when test="${not empty lclBookingImport.deliveryOrderReceived}">
                            <input type="button" id="deliveryButton" class="green-background" onclick="releaseCurrentDate('deliveryDate', 'deliveryButton', 'deliveryYes')" value="Reset">
                            <label id="deliveryYes" class="greenFontBold">YES</label>
                        </c:when><c:otherwise>
                            <input type="button" id="deliveryButton" class="button-style1" onclick="releaseCurrentDate('deliveryDate', 'deliveryButton', 'deliveryYes')" value="No">
                            <label id="deliveryYes" class="greenFontBold"></label>
                        </c:otherwise></c:choose>
                </cong:td>
                <cong:td valign="middle">
                    <label id="deliveryDate" class="importReleaseDesign">${deliveryDate}</label>
                </cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Cargo In General Order</cong:td>
                <cong:td>
                    <c:choose><c:when test="${not empty lclBookingImport.cargoGeneralOrder}">
                            <input type="button" id="cargoGeneralOrderButton" class="green-background" onclick="releaseCurrentDate('cargoGeneralOrderDate', 'cargoGeneralOrderButton', 'cargoGeneralOrderYes')" value="Reset">
                            <label id="cargoGeneralOrderYes" class="greenFontBold">YES</label>
                        </c:when><c:otherwise>
                            <input type="button" id="cargoGeneralOrderButton" class="button-style1" onclick="releaseCurrentDate('cargoGeneralOrderDate', 'cargoGeneralOrderButton', 'cargoGeneralOrderYes')" value="No">
                            <label id="cargoGeneralOrderYes" class="greenFontBold"></label>
                        </c:otherwise></c:choose>
                </cong:td>
                <cong:td valign="middle">
                    <label id="cargoGeneralOrderDate" class="importReleaseDesign">${cargoGeneralOrder}</label>
                </cong:td>

                <cong:td styleClass="textlabelsBoldforlcl">Release Order Received</cong:td>
                <cong:td>
                    <c:choose><c:when test="${not empty lclBookingImport.releaseOrderReceived}">
                            <input type="button" id="releasebButton" class="green-background" onclick="releaseCurrentDate('releaseDate', 'releasebButton', 'releaseYes')" value="Reset">
                            <label id="releaseYes" class="greenFontBold">YES</label>
                        </c:when><c:otherwise>
                            <input type="button" id="releasebButton" class="button-style1" onclick="releaseCurrentDate('releaseDate', 'releasebButton', 'releaseYes')" value="No">
                            <label id="releaseYes" class="greenFontBold"></label>
                        </c:otherwise></c:choose>
                </cong:td>
                <cong:td valign="middle">
                    <label id="releaseDate" class="importReleaseDesign">${releaseOrder}</label>
                </cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
            </cong:tr>
            <cong:tr>

                <cong:td></cong:td>
            </cong:tr>
            <cong:tr styleClass="tableHeadingNew">
                <cong:td>   Freight Release:  </cong:td>
                <cong:td colspan="8" align="center"></cong:td>
            </cong:tr>
            <cong:tr> <cong:td colspan="8"></cong:td></cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Freight Release</cong:td>
                <cong:td align="left" valign="middle">
                    <c:choose><c:when test="${not empty lclBookingImport.freightReleaseDateTime}">
                            <input type="button" id="freightButton" class="green-background" onclick="releaseCurrentDate('freightDate', 'freightButton', 'freightYes')" value="Reset">
                            <label id="freightYes" class="greenFontBold" >YES</label>
                        </c:when><c:otherwise>
                            <input type="button" id="freightButton" class="button-style1" onclick="releaseCurrentDate('freightDate', 'freightButton', 'freightYes')" value="No">
                            <label id="freightYes" class="greenFontBold"></label>
                        </c:otherwise></c:choose>
                </cong:td>
                <cong:td valign="middle">
                    <label id="freightDate" class="importReleaseDesign">${freightOrder}</label>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl" rowspan="4">Release B/L Note</cong:td>
                <cong:td rowspan="4" colspan="4">
                    <cong:textarea name="freightReleaseNote" styleClass="textLCLuppercase" cols="50" rows="3" container="NULL" value="${lclBookingImport.freightReleaseBlNote}"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl"  valign="top">Payment Release</cong:td>
                <cong:td align="left" valign="middle">
                    <c:choose><c:when test="${not empty lclBookingImport.paymentReleaseReceived}">
                            <input type="button" id="paymentButton" class="green-background" onclick="releaseCurrentDate('paymentDate', 'paymentButton', 'paymentYes')" value="Reset">
                            <label id="paymentYes" class="greenFontBold" >YES</label>
                        </c:when><c:otherwise>
                            <input type="button" id="paymentButton" class="button-style1" onclick="releaseCurrentDate('paymentDate', 'paymentButton', 'paymentYes')" value="No">
                            <label id="paymentYes" class="greenFontBold"></label>
                        </c:otherwise></c:choose>
                </cong:td>
                <cong:td  valign="middle">
                    <label id="paymentDate" class="importReleaseDesign">${paymentOrder}</label>
                </cong:td>
            </cong:tr>

            <cong:tr> <cong:td colspan="8"></cong:td></cong:tr>
        </cong:table>
    </cong:form>

</body>
