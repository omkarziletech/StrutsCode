<%-- 
    Document   : lclExportUserDefaults
    Created on : Sep 2, 2015, 12:07:41 PM
    Author     : aravindhan.v
--%>
<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <%@include file="init.jsp" %>
        <%@include file="colorBox.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <%@include file="../fragment/lclFormSerialize.jspf"  %>
        <cong:javascript src="${path}/jsps/LCL/js/common.js"/>
        <%@include file="/taglib.jsp" %>
        <script type="text/javascript">
            $(document).ready(function () {
                if ($(".defaultClass").length === 0) {
                    $("#applyDefaultName").hide();
                }
                if ($("#applyDefaultName").val() !== '') {
                    $("#save").hide();
                    $(".textlabelsLclBoldForMainScreenTextBox").attr("readOnly", true);
                    $(".textlabelsLclBoldForMainScreenTextBox").addClass("readOnly");
                }
                $("#loadForm").val($("#lclUserDefaultsForm").serialize());
            });

            function save1(path) {
                var flag = true;
                $("#applyDefaultName option").each(function () {
                    if ($("#lcldefaultName").val() !== '' && $(this).text().trim() !== '') {
                        if ($(this).text().trim().toUpperCase() === $("#lcldefaultName").val().toUpperCase()) {
                            flag = false;
                        }
                    }
                });
                if (!flag && $("#applyDefaultId").val() === '') {
                    $.prompt("LCL Default Name Already Exists");
                    return false;
                } else if ($("#lcldefaultName").val() === '') {
                    $.prompt("Please Enter LCL Default Name");
                    return false;
                }
                $("#applyDefaultId").val($("#applyDefaultName").val());
                $("#methodName").val('save');
                var params = $("#lclUserDefaultsForm").serialize();
                $.post($("#lclUserDefaultsForm").attr("action"), params, function (data) {
                    clear1(path);
                });
            }
            function clear1(path) {
                var href = path + "/lclExportUserDefaults.do?methodName=clear";
                document.location.href = href;
            }
            function edit(path) {
                if ($("#applyDefaultName").val() === '') {
                    $.prompt("Please Select Atleast One Lcl Default Name");
                    return false;
                } else {
                    $("#applyDefaultId").val($("#applyDefaultName").val());
                    $("#save").show();
                    $(".textlabelsLclBoldForMainScreenTextBox").removeClass("readOnly");
                    $(".textlabelsLclBoldForMainScreenTextBox").attr("readOnly", false);
                }

            }
            function deleteDefaultName(path) {
                if ($("#applyDefaultName").val() === '') {
                    $.prompt("Please Select Atleast One Lcl Default Name");
                    return false;
                } else {
                    // $("#applyDefaultId").val($("#applyDefaultName").val());
                    $.prompt("Do you want to proceed deleting this LCL Default?", {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v === 1) {
                                $("#methodName").val('delete');
                                var params = $("#lclUserDefaultsForm").serialize();
                                $.post($("#lclUserDefaultsForm").attr("action"), params, function (data) {
                                    clear1(path);
                                });
                            } else {
                                $.prompt.close();
                            }
                        }
                    });
                }
            }
            function showValue(path) {
                if ($("#applyDefaultName").val() !== '') {
                    $("#applyDefaultId").val($("#applyDefaultName").val());
                    $("#methodName").val('showValue');
                    $("#lclUserDefaultsForm").submit();
                } else {
                    $("#save").show();
                    clear1(path);
                    $(".textlabelsLclBoldForMainScreenTextBox").removeClass("readOnly");
                    $(".textlabelsLclBoldForMainScreenTextBox").attr("readOnly", false);
                }
            }
            function ClosePopUp() {
                parent.$.colorbox.close();
            }
        </script>
    </head>
    <body class="whitebackgrnd">
        <div class="floatLeft tableHeadingNew" style="width:100%">
            Lcl User Defaults
        </div><br><br>
        <cong:form name="lclUserDefaultsForm" id="lclUserDefaultsForm" action="/lclExportUserDefaults" >
            <input type="hidden"  id="methodName" name="methodName"/>
            <input type="hidden"  id="loadForm"/>
            <table width="100%">
                <tr>
                    <td><html:select name="lclUserDefaultsForm" property="applyDefaultName" styleId="applyDefaultName" 
                                 styleClass="smallDropDown textlabelsBoldforlcl" onchange="showValue('${path}');">
                            <html:option value="">Select default to edit</html:option>
                            <html:optionsCollection name="applyDefaultNameList" styleClass="defaultClass"/>
                        </html:select>
                        <cong:hidden id="applyDefaultId" name="applyDefaultId"/>
                    </td>
                    <td style="text-align:right;">
                        <input type="button" class="buttonStyleNew" value="Save" id="save"  onclick="save1('${path}')">
                        <input type="button" class="buttonStyleNew" value="Edit" id="Edit" onclick="edit('${path}')">
                        <input type="button" class="buttonStyleNew" value="Delete" id="Delete" onclick="deleteDefaultName('${path}')">
                        <input type="button" class="buttonStyleNew" value="Clear" id="clear"  onclick="clear1('${path}')">
                        <input type="button" class="buttonStyleNew" value="Go Back" id="cancel" onclick="ClosePopUp();"/>
                    </td>
                </tr>     
            </table>      
            <cong:table  caption="&nbsp; " cellspacing="2" border="0">
                <cong:tr><cong:td colspan="2">&nbsp;</cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">New LCL Default Name</cong:td>
                    <cong:td>
                        <cong:text name="lcldefaultName" id ="lcldefaultName" styleClass="textlabelsLclBoldForMainScreenTextBox mandatory" 
                                   value="${lclUserDefaultsForm.lcldefaultName}"    style="width:44%;"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left"> Current Location </cong:td>
                    <cong:td>
                        <cong:autocompletor id="currentLocationR" name="currentLocation" template="one" fields="unlocationName,NULL,unlocationCode,currentLocationId"
                                            query="PORT" value="${lclUserDefaultsForm.currentLocation}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"         width="200" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="currentLocationId" id="currentLocationId" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">Origin </cong:td>
                    <cong:td>
                        <cong:autocompletor id="portOfOriginR" name="portOfOrigin" template="one" fields="unlocationName,NULL,unlocationCode,portOfOriginId"
                                            query="PORT" value="${lclUserDefaultsForm.portOfOrigin}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  width="200" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="portOfOriginId" id="portOfOriginId" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">POL </cong:td>
                    <cong:td>
                        <cong:autocompletor id="portOfLoadingR" name="portOfLoading" template="one" fields="unlocationName,NULL,unlocationCode,portOfLoadingId"
                                            query="PORT" value="${lclUserDefaultsForm.portOfLoading}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  width="200" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="portOfLoadingId" id="portOfLoadingId" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">POD</cong:td>
                    <cong:td>
                        <cong:autocompletor id="portOfDestinationR" name="portOfDestination" template="one" fields="unlocationName,NULL,unlocationCode,portOfDestinationId"
                                            query="CONCAT_RELAY_NAME_FD" value="${lclUserDefaultsForm.portOfDestination}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  width="200" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="portOfDestinationId" id="portOfDestinationId" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">Destination</cong:td>
                    <cong:td>
                        <cong:autocompletor id="finalDestinationR" name="finalDestination" template="one" fields="unlocationName,NULL,unlocationCode,finalDestinationId"
                                            query="CONCAT_RELAY_NAME_FD" value="${lclUserDefaultsForm.finalDestination}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  width="200" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="finalDestinationId" id="finalDestinationId" value=""/>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:form>
    </body>
</html>

