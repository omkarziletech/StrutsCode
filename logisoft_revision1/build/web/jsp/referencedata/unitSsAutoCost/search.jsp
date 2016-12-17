<%-- 
    Document   : search
    Created on : Mar 25, 2016, 4:48:33 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../../jsps/LCL/init.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search By Unit Auto Costing</title>
    </head>
    <body>
        <div id="pane">
            <%@include file="../../../jsps/preloader.jsp"%>
            <html:form action="/unitSsAutoCosting" type="com.logiware.referencedata.action.LclUnitSsAutoCostingAction"
                       name="unitSsAutoCostingForm" styleId="unitSsAutoCostingForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="copyCostId" styleId="copyCostId"/>
                <html:hidden property="duplicateCostCode" styleId="duplicateCostCode"/>
                <html:hidden property="copyOriginId" styleId="copyOriginId"/>
                <html:hidden property="copyDestinationId" styleId="copyDestinationId"/>
                <table class="table" style="margin: 0;overflow: hidden">
                    <tr class="tableHeadingNew"><td colspan="4">Search</td></tr>
                    <tr>
                        <td colspan="4"></td>
                    </tr>
                    <tr>
                        <td class="label" style="float: right">Origin</td>
                        <td>
                            <cong:autocompletor id="originName" name="originName" template="one" fields="NULL,NULL,NULL,originId" query="RELAYNAME"
                                                styleClass="textuppercaseLetter pol" width="250" container="NULL"  shouldMatch="true"
                                                callback="" value="${unitSsAutoCostingForm.originName}" />
                            <cong:hidden id="originId" name="originId" value="${unitSsAutoCostingForm.originId}"/>
                        </td>
                        <td class="label" style="float: right">Destination</td>
                        <td>
                            <cong:autocompletor id="fdName" name="fdName" template="one"  fields="NULL,NULL,unlocationCode,fdId"
                                                query="CONCAT_RELAY_NAME_FD" styleClass="textuppercaseLetter pod"
                                                width="350" container="NULL" shouldMatch="true"
                                                value="${unitSsAutoCostingForm.fdName}"/>
                            <cong:hidden id="fdId" name="fdId" value="${unitSsAutoCostingForm.fdId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" style="float: right">
                            Size

                        </td>
                        <td>
                            <html:select property="unitTypeId" styleId="unitTypeId" styleClass="smallDropDown" >
                                <html:option value="">Select</html:option>
                                <html:optionsCollection property="unitTypeList" label="label" value="value"/>
                            </html:select>
                        </td>
                        <td  class="label" style="float: right">Cost Code</td>
                        <td>
                            <cong:autocompletor name="costCode" id="costCode" template="two" query="COST_CODE" fields="null,costCodeId"
                                                shouldMatch="true" scrollHeight="150" params="LCLE" value="${unitSsAutoCostingForm.costCode}"
                                                container="NULL" styleClass="textlabelsBoldForTextBoxWidth" width="400"/>
                            <cong:hidden name="costCodeId" id="costCodeId" value="${unitSsAutoCostingForm.costCodeId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="center">
                            <input type="button"  value="Search" id="search"
                                   align="middle" class="button" onclick="searchCost();"/>
                            <input type="button"  value="Add New" id="addNew"
                                   align="middle" class="button" onclick="addCost('${path}', 0);"/>
                            <input type="button"  value="Reset" id="reset"
                                   align="middle" class="button" onclick="resetCost();"/>
                            <input type="button"  value="Copy" id="copy"
                                   align="middle" class="button" onclick="copyCost('${path}');"/>
                        </td>
                    </tr>
                </table>
                <br/>
                <div id="results">
                    <c:import url="/jsp/referencedata/unitSsAutoCost/results.jsp"/>
                </div>

            </html:form>
        </div>
    </body>
    <script type="text/javascript">
        function searchCost() {
            var originId = $('#originId').val();
            var fdId = $('#fdId').val();
            var unitTypeId = $('#unitTypeId').val();
            var costCodeId = $('#costCodeId').val();
            if (costCodeId === '' && unitTypeId === '' && originId === '' && fdId === '') {
                $.prompt("Please Enter Any Search Criteria")
                return false;
            }
            showLoading();
            $("#action").val("search");
            $('#unitSsAutoCostingForm').submit();
        }
        function addCost(path, costId) {
            var originName = $('#originName').val();
            var fdName = $('#fdName').val();
            var originId = $('#originId').val();
            var fdId = $('#fdId').val();
            if (originName === '' || originName === null) {
                $.prompt("Origin is Required")
                $("#originName").css("border-color", "red");
                return false;
            }
            if (fdName === '' || fdName === null) {
                $.prompt("Destination is Required")
                $("#fdName").css("border-color", "red");
                return false;
            }
            var url = path + "/unitSsAutoCosting.do?action=addOrEditCost&unitSsCostId=" + costId +
                    "&originName=" + originName + "&fdName=" + fdName + "&originId=" + originId + "&fdId=" + fdId;
            $.colorbox({
                iframe: true,
                href: url,
                width: "70%",
                height: "60%",
                title: "<span style=color:blue>Add Unit Auto Cost</span>"
            });
        }
        function editCost(path, costId) {
            var url = path + "/unitSsAutoCosting.do?action=addOrEditCost&unitSsCostId=" + costId;
            $.colorbox({
                iframe: true,
                href: url,
                width: "70%",
                height: "60%",
                title: "<span style=color:blue>Edit Unit Auto Cost</span>"
            });
        }
        function resetCost() {
            $('#originName').val("");
            $('#fdName').val("");
            $('#originId').val("");
            $('#fdId').val("");
            $('#unitTypeId').val("");
            $('#costCodeId').val("");
            $('#costCode').val("");
            $('#autoCostListTable').hide();
        }

        function copyAllCost() {
            $(".checkSingle").attr("checked", $("#checkAll").is(":checked"));
        }

        function copyCost(path) {
            var flag = false;
            $(".checkSingle").each(function () {
                if ($(this).is(":checked")) {
                    flag = true;
                }
            });
            if (!flag) {
                $.prompt("Please select atleast one cost");
            } else {
                var url = path + "/jsp/referencedata/unitSsAutoCost/copyAutoCost.jsp";
                $.colorbox({
                    iframe: true,
                    href: url,
                    width: "40%",
                    height: "50%",
                    title: "<span style=color:blue>Copy Cost</span>"
                });
            }
        }

        function deleteCost(costId) {
            $.prompt("Are you sure want to delete?", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        $.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.hibernate.dao.lcl.UnitSsAutoCostingDAO",
                                methodName: "deleteUnitSsCost",
                                param1: costId,
                                dataType: "json"
                            },
                            async: false,
                            success: function (data) {
                                searchCost();
                            }
                        });
                    } else {
                        $.prompt.close();
                    }
                }
            });

        }
    </script>
</html>
