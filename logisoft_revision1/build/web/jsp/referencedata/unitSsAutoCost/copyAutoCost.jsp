<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../../jsps/LCL/init.jsp" %>
<%@include file="../../../jsps/preloader.jsp"%>
<html>
    <body>
        <input type="hidden">
        <div class="tableHeadingNew"><label>Copy Cost</label></div>
        <table style="margin-top: 20px; overflow: hidden" width="100%" border="0">
            <tr>
                <td class="label" style="float: right">Origin</td>
                <td>
                    <cong:autocompletor id="origin" name="copyOrigin" template="one" fields="NULL,NULL,NULL,copyOriginId" query="RELAYNAME"
                                        styleClass="textuppercaseLetter pol" width="250" container="NULL"  shouldMatch="true"
                                        callback="" value="" />
                    <cong:hidden id="copyOriginId" name="copyOriginId" value=""/>
                </td>
                <td class="label" style="float: right">Destination</td>
                <td>
                    <cong:autocompletor id="destination" name="copyDestination" template="one"  fields="NULL,NULL,unlocationCode,copyDestinationId"
                                        query="CONCAT_RELAY_NAME_FD" styleClass="textuppercaseLetter pod"
                                        width="250" container="NULL" shouldMatch="true"
                                        value=""/>
                    <cong:hidden id="copyDestinationId" name="copyDestinationId" value=""/>
                </td>
                <td width="25%"></td>
            </tr>
            <tr align="center">
                <td colspan="2" style="padding-top: 5em;"></td>
                <td colspan="3">
                    <input type="button" class="button-style1" value="Copy" onclick="copyUnitAutoCost();"/>
                    <input type="button" class="button-style1" value="Cancel" onclick="parent.$.colorbox.close();"/>
                </td>
            </tr>
        </table>
    </body>
</html>

<script type="text/javascript">

    function copyUnitAutoCost() {
        var parentOrigin = parent.$("#originId").val();
        var parentDestination = parent.$("#fdId").val();
        var currentOrigin = $("#copyOriginId").val();
        var currentDestination = $("#copyDestinationId").val();

        if (currentOrigin === "") {
            $.prompt("Please enter origin");
            return false;
        } else if (currentDestination === "") {
            $.prompt("Please enter destination");
            return false;
        } else if (parentOrigin === currentOrigin && parentDestination === currentDestination) {
            $.prompt("Please enter different origin or destination");
            return false;
        } else {
            var costCode = new Array();
            parent.$(".checkSingle").each(function () {
                if ($(this).is(":checked")) {
                    costCode.push(parent.$(".costCode" + $(this).attr("id")).text().trim().trim());
                }
            });

            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.UnitSsAutoCostingDAO",
                    methodName: "validateExistCost",
                    param1: currentOrigin,
                    param2: currentDestination,
                    param3: costCode.toString(),
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data !== "") {
                        copyValidCost(data);
                    } else {
                        copyCostForSelectesCost("");
                    }
                    return false;
                }
            });
        }
        return true;
    }

    function copyValidCost(data) {
        var value = data.split("#");
        $.prompt("<span class='red'>" + value[0] + "</span> charge code already exists.Do you want to overwrite?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    copyCostForSelectesCost(value[0]);
                } else if (v === 2) {
                    removeDuplicate(data);
                }
            }
        });
    }

    function removeDuplicate(data) {
        var code = data.split(",");
        for (var i = 0; i < data.split(",").length; i++) {
            parent.$("." + code[i]).attr("checked", false);
        }
        copyCostForSelectesCost("");
    }

    function copyCostForSelectesCost(code) {
        var costId = new Array();
        parent.$(".checkSingle").each(function () {
            if ($(this).is(":checked")) {
                costId.push($(this).attr("id"));
            }
        });
        submitCopyCost(costId, code);
    }

    function submitCopyCost(costId, code) {
        if (costId.length > 0) {
            showLoading();
            parent.$("#action").val("copyCost");
            parent.$("#copyOriginId").val($("#copyOriginId").val());
            parent.$("#copyDestinationId").val($("#copyDestinationId").val());
            parent.$("#copyCostId").val(costId.toString());
            parent.$("#duplicateCostCode").val(code!=="" ? "true" : "false");
            var params = parent.$("#unitSsAutoCostingForm").serialize();
            $.post(parent.$("#unitSsAutoCostingForm").attr("action"), params, function (data) {
                parent.$("#originId").val($("#copyOriginId").val());
                parent.$("#originName").val($("#origin").val());
                parent.$("#fdId").val($("#copyDestinationId").val());
                parent.$("#fdName").val($("#destination").val());
                parent.searchCost();
            });
        } else {
            parent.$.colorbox.close();
        }
    }
</script>
