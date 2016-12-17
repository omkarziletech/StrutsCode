<%-- 
    Document   : Ports
    Created on : Mar 03, 2016, 6:56:32 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Ports</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/layout/second-tabs.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/tab/tab.js"></script>
    </head>
    <body>
        <html:form action="/editPorts" name="editPortsForm"
                   styleId="editPortsForm" type="com.gp.cong.logisoft.struts.form.EditPortsForm" scope="request" method="post">
            <table class="table" style="margin: 0; border: none;">
                <tr>
                    <td class="label width-100px">Schedule Code</td>
                    <td><html:text property="portCode" styleId="portCode" value="${sessionScope.ports.shedulenumber}" styleClass="textbox readonly" readonly="true"/></td>
                    <td class="label width-100px">Control Number</td>
                    <td><html:text property="controlNo" styleId="controlNo" value="${sessionScope.ports.controlNo}" styleClass="textbox readonly" readonly="true"/></td>
                    <td>
                        <input type="button" class="button" value="Save" onclick="savePort()"/>
                        <input type="button" class="button" value="Delete" onclick="confirmDelete()"/>
                        <input type="button" class="button" value="Go Back" onclick="goBack()"/>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue" />
        </html:form>
        <div id="tabs">
            <ul class="htabs">
                <li><a rel="General" href="javascript: void(0)" title="editPortsFrame">General</a></li>
                <li><a rel="LCL" href="javascript: void(0)" title="editlclPortsFrame">LCL</a></li>
                <li><a rel="FCL" href="javascript: void(0)" title="editfclPortsFrame">FCL</a></li>
                <li><a rel="Air" href="javascript: void(0)" title="editairPortsFrame">Air</a></li>
                <li><a rel="Imports" href="javascript: void(0)" title="editimportPortsFrame">Imports</a></li>
            </ul>
            <div class="pane">
                <iframe frameborder="0" name="editPortsFrame" id="editPortsFrame" src="${path}/jsps/datareference/editPortDetails.jsp" width="100%" height="0"></iframe>
            </div>
            <div class="pane">
                <iframe frameborder="0" name="editlclPortsFrame" id="editlclPortsFrame" src="${path}/jsps/datareference/editLclPortsConfig.jsp" width="100%" height="0"></iframe>
            </div>
            <div class="pane">
                <iframe frameborder="0" name="editfclPortsFrame" id="editfclPortsFrame" src="${path}/jsps/datareference/editFclPortsConfig.jsp" width="100%" height="0"></iframe>
            </div>
            <div class="pane">
                <iframe frameborder="0" name="editairPortsFrame" id="editairPortsFrame" src="${path}/jsps/datareference/editAirportConfig.jsp" width="100%" height="0"></iframe>
            </div>
            <div class="pane">
                <iframe frameborder="0" name="editimportPortsFrame" id="editimportPortsFrame" src="${path}/jsps/datareference/editImportConfig.jsp" width="100%" height="0"></iframe>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        var accessMode = "${not empty sessionScope.view ? sessionScope.view : sessionScope.modifyforports}";
        var message = "${sessionScope.message}";
        $(document).ready(function () {
            if (accessMode === "0" || accessMode === "3") {
                $("input[type='button'][value='Save']").hide();
                $("input[type='button'][value='Delete']").hide();
            }
            if (accessMode === "1") {
                $("input[type='button'][value='Delete']").hide();
            }
            if (accessMode === "3" && message !== "") {
                $.prompt(message);
            }
            var initial = 0;
            $("ul.htabs").tabs("> .pane", {effect: "fade", current: "selected", initialIndex: initial, onClick: function () {
                    var frameid = $("ul.htabs li.selected").find("a").attr("title");
                    $("#" + frameid).height($(document).height() - 60);
                    if (frameid === "editlclPortsFrame") {
                        $("#" + frameid).contents().find("#domesticRouting").focus();
                        $("#" + frameid).contents().find("#internalMarks").focus();
                        $("#" + frameid).contents().find("#griRemarks").focus();
                        $("#" + frameid).contents().find("#splRemarksInEnglish").focus();
                        $("#" + frameid).contents().find("#splRemarksInSpanish").focus();
                        $("#" + frameid).contents().find("#terminalNumber").focus();
                    } else if (frameid === "editairPortsFrame") {
                        $("#" + frameid).contents().find("#airSplRemarksinEnglish").focus();
                        $("#" + frameid).contents().find("#airSplRemarksinSpanish").focus();
                        $("#" + frameid).contents().find("#terminalNumber").focus();
                    } else if (frameid === "editfclPortsFrame") {
                        $("#" + frameid).contents().find("#clauseDesc").focus();
                        $("#" + frameid).contents().find("#tempRemark").focus();
                        $("#" + frameid).contents().find("#quoteRemarks").focus();
                        $("#" + frameid).contents().find("#griRemarks").focus();
                        $("#" + frameid).contents().find("#terminalNumber").focus();
                    }
                }
            });
        });

        function savePort() {
            $("input[type='button']").attr("disabled", true);
            var editPortDetails = $("#editPortsFrame").contents().find("#editPortDetails");
            var editLclPortsConfig = $("#editlclPortsFrame").contents().find("#editLclPortsConfig");
            var editFclPortsConfig = $("#editfclPortsFrame").contents().find("#editFclPortsConfig");
            var editAirPortConfigForm = $("#editairPortsFrame").contents().find("#editAirPortConfigForm");
            var editImportConfigForm = $("#editimportPortsFrame").contents().find("#editImportConfigForm");

            if ($.trim(editPortDetails.find("input[name='portName']").val()) === "") {
                $.prompt("Please enter the Port Name");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editLclPortsConfig.find("input[name='ftfFee']").val()) !== ""
                    && !editLclPortsConfig.find("input[name='ftfFee']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Ftp Fee should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if (editLclPortsConfig.find("textarea[name='defaultDomesticRoutingInstructions']").val().length > 200) {
                $.prompt("Default Domestic Routing Instructions should be only 200 characters");
                $("input[type='button']").removeAttr("disabled");
            } else if (editLclPortsConfig.find("textarea[name='lclSplRemarksinEnglish']").val().length > 400) {
                $.prompt("Lcl Spl Remarks in English should be only 400 characters");
                $("input[type='button']").removeAttr("disabled");
            } else if (editLclPortsConfig.find("textarea[name='lclSplRemarksinSpanish']").val().length > 400) {
                $.prompt("Lcl Spl Remarks in Spanish should be only 400 characters");
                $("input[type='button']").removeAttr("disabled");
            } else if (editFclPortsConfig.find("textarea[name='clauseDescription']").val().length > 200) {
                $.prompt("Clause Description should be only 200 characters");
                $("input[type='button']").removeAttr("disabled");
            } else if (editFclPortsConfig.find("textarea[name='specialRemarksforQuotation']").val().length > 320) {
                $.prompt("Special Remarks for Quotation should be only 320 characters");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editFclPortsConfig.find("input[name='amountRouteByAgentAdmin']").val()) !== ""
                    && !editFclPortsConfig.find("input[name='amountRouteByAgentAdmin']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Amount1 should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editFclPortsConfig.find("input[name='tierAmountRouteByAgentAdmin']").val()) !== ""
                    && !editFclPortsConfig.find("input[name='tierAmountRouteByAgentAdmin']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Tier Amount1 should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editFclPortsConfig.find("input[name='amountRouteByAgentCommn']").val()) !== ""
                    && !editFclPortsConfig.find("input[name='amountRouteByAgentCommn']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Amount2 should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editFclPortsConfig.find("input[name='tierAmountRouteByAgentCommn']").val()) !== ""
                    && !editFclPortsConfig.find("input[name='tierAmountRouteByAgentCommn']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Tier Amount2 should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editFclPortsConfig.find("input[name='amountRouteNotAgentAdmin']").val()) !== ""
                    && !editFclPortsConfig.find("input[name='amountRouteNotAgentAdmin']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Amount3 should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editFclPortsConfig.find("input[name='tierAmountRouteNotAgentAdmin']").val()) !== ""
                    && !editFclPortsConfig.find("input[name='tierAmountRouteNotAgentAdmin']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Tier Amount3 should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editFclPortsConfig.find("input[name='amountRouteNotAgentCommn']").val()) !== ""
                    && !editFclPortsConfig.find("input[name='amountRouteNotAgentCommn']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Amount4 should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if ($.trim(editFclPortsConfig.find("input[name='tierAmountRouteNotAgentCommn']").val()) !== ""
                    && !editFclPortsConfig.find("input[name='tierAmountRouteNotAgentCommn']").val().match(/^\d+(\.\d{1,2})?$/i)) {
                $.prompt("Tier Amount4 should be Numeric");
                $("input[type='button']").removeAttr("disabled");
            } else if (editAirPortConfigForm.find("textarea[name='airPortSplRemarksinEnglish']").val().length > 300) {
                $.prompt("Air Spl Remarks in English should be only 300 characters");
                $("input[type='button']").removeAttr("disabled");
            } else if (editAirPortConfigForm.find("textarea[name='airPortSplRemarksinSpanish']").val().length > 300) {
                $.prompt("Lcl Spl Remarks in Spanish should be only 300 characters");
                $("input[type='button']").removeAttr("disabled");
            } else {
                $.startPreloader();
                $.ajaxx({
                    url: editPortDetails.attr("action"),
                    data: editPortDetails.serialize(),
                    preloading: false,
                    success: function () {
                        $.ajaxx({
                            url: editLclPortsConfig.attr("action"),
                            data: editLclPortsConfig.serialize(),
                            preloading: false,
                            success: function () {
                                $.ajaxx({
                                    url: editFclPortsConfig.attr("action"),
                                    data: editFclPortsConfig.serialize(),
                                    preloading: false,
                                    success: function () {
                                        $.ajaxx({
                                            url: editAirPortConfigForm.attr("action"),
                                            data: editAirPortConfigForm.serialize(),
                                            preloading: false,
                                            success: function () {
                                                $.ajaxx({
                                                    url: editImportConfigForm.attr("action"),
                                                    data: editImportConfigForm.serialize(),
                                                    preloading: false,
                                                    success: function () {
                                                        $("#buttonValue").val("save");
                                                        $("#editPortsForm").submit();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }

        function goBack() {
            if (accessMode === "0" || accessMode === "3") {
                $("#buttonValue").val("cancelview");
                $("#editPortsForm").submit();
            } else {
                $.prompt("Would you like to save the changes?", {
                    buttons: {
                        Yes: true,
                        No: false
                    },
                    callback: function (v) {
                        if (v) {
                            savePort();
                        } else {
                            $("#buttonValue").val("cancel");
                            $("#editPortsForm").submit();
                        }
                    }
                });
            }
        }

        function confirmDelete() {
            $.prompt("Do you want to delete this Port?", {
                buttons: {
                    Yes: true,
                    No: false
                },
                callback: function (v) {
                    if (v) {
                        $("#buttonValue").val("delete");
                        $("#editPortsForm").submit();
                    }
                }
            });
        }
    </script>
</html>