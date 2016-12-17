<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="init.jsp"%>
<%@include file="colorBox.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/taglib.jsp"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html>
    <body>

        <div class="tableHeadingNew">Add/Edit Templates</div>
        <input type="hidden" id="loadtemplateForm"/>
        <cong:form name="lclTemplateForm" id="lclTemplateForm" action="/lclTemplate.do">
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="moduleName" id="moduleName" value="${lclTemplateForm.moduleName}"/>
            <cong:hidden name="orderedData" id="orderedData"/>
            <cong:table border="0" width="100%"  id="headingRow">
                <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">
                        Template Name:
                        <cong:text name="templateName" id="templateName" value="${lclSearchTemplate.templateName}"  
                                   container="NULL" style="text-transform:uppercase"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </cong:td>
                    <cong:td>
                        <div class="button-style1" id="save" 
                             onclick="submitAjaxForm('${path}', 'saveTemplate')" >Save</div>
                        <div class="button-style1" onclick="editTemplate('${path}')" >Edit</div>
                        <div class="button-style1" onclick="clearTemplate('${path}')" >Clear</div>
                        <div class="button-style1" onclick="deleteTemplate('${path}')" >Delete</div>
                        <div class="button-style1" onclick="ClosePopUp('${path}')" >GoBack</div>
                    </cong:td>
                    <cong:td width="50%"></cong:td>
                </cong:tr>
            </cong:table>
            <cong:table border="0" width="20%">
                <cong:tr><cong:td><br></cong:td></cong:tr>
                <cong:tr>
                    <cong:td>
                        <html:select name="lclTemplateForm" property="templateId" styleId="templateId" 
                                     value="${lclSearchTemplate.id}" styleClass="smallDropDown textlabelsBoldforlcl" 
                                     style="text-transform:uppercase" onchange="showTemplate('${path}');">
                            <html:option value="">SELECT</html:option>
                            <html:optionsCollection name="lclTemplateForm" property="templateList" value="id" label="templateName"/>
                        </html:select>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <c:choose>
                <c:when test="${lclTemplateForm.moduleName eq 'Exports'}">
                    <%@include file="/jsps/LCL/ExportTemplate.jsp" %> 
                </c:when>                 
                <c:otherwise>
                    <cong:table width="auto" border="0" id="listofTemplate" style="margin-left:50px;">
                        <cong:tr><cong:td colspan="8"><br></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td>QUOTE:</cong:td>
                            <cong:td><input type="checkbox" name="qu" id="QUOTE" class="checkbox" ${lclSearchTemplate.qu ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>BKG:</cong:td>
                            <cong:td><input type="checkbox" name="bk" id="BOOKING" class="checkbox" ${lclSearchTemplate.bk ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>BL:</cong:td>
                            <cong:td><input type="checkbox" name="bl" id="BL" class="checkbox" ${lclSearchTemplate.bl ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>HZ:</cong:td>
                            <cong:td><input type="checkbox" name="hz" id="HAZ" class="checkbox" ${lclSearchTemplate.hz ? 'checked':''} container="NULL"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>EDI:</cong:td>
                            <cong:td><input type="checkbox" name="edi" id="EDI" class="checkbox" ${lclSearchTemplate.edi ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>FILE NO:</cong:td>
                            <cong:td><input type="checkbox" name="fileNo" id="FILE_NUMBER" class="checkbox" ${lclSearchTemplate.fileNo ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>TR:</cong:td>
                            <cong:td><input type="checkbox" name="tr" id="TR" class="checkbox" ${lclSearchTemplate.tr ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>STATUS:</cong:td>
                            <cong:td><input type="checkbox" name="status" id="STATUS" class="checkbox" ${lclSearchTemplate.status ? 'checked':''} container="NULL"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>DOC:</cong:td>
                            <cong:td><input type="checkbox" name="doc" id="DOC" class="checkbox" ${lclSearchTemplate.doc ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>DATE RECEIVED:</cong:td>
                            <cong:td><input type="checkbox" name="dateReceived" id="DATE_RECEIVED" ${lclSearchTemplate.dateReceived ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>PCS:</cong:td>
                            <cong:td><input type="checkbox" name="pcs" id="PCS" class="checkbox" ${lclSearchTemplate.pcs ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>CUBE:</cong:td>
                            <cong:td><input type="checkbox" name="cube" id="CUBE" class="checkbox" ${lclSearchTemplate.cube ? 'checked':''} container="NULL"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>WEIGHT:</cong:td>
                            <cong:td><input type="checkbox" name="weight" id="WEIGHT" class="checkbox" ${lclSearchTemplate.weight ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>ORIGIN:</cong:td>
                            <cong:td><input type="checkbox" name="origin" id="ORIGIN" class="checkbox" ${lclSearchTemplate.origin ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>POL:</cong:td>
                            <cong:td><input type="checkbox" name="pol" id="POL" class="checkbox" ${lclSearchTemplate.pol ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>POD:</cong:td>
                            <cong:td><input type="checkbox" name="pod" id="POD" class="checkbox" ${lclSearchTemplate.pod ? 'checked':''} container="NULL"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>DESTINATION:</cong:td>
                            <cong:td><input type="checkbox" name="destination" id="DESTINATION" ${lclSearchTemplate.destination ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>SHIPPER:</cong:td>
                            <cong:td><input type="checkbox" name="shipper" id="SHIPPER" class="checkbox" ${lclSearchTemplate.shipper ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>FWD:</cong:td>
                            <cong:td><input type="checkbox" name="fwd" id="FORWARDER" class="checkbox" ${lclSearchTemplate.fwd ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>CONSIGNEE:</cong:td>
                            <cong:td><input type="checkbox" name="consignee" id="CONSIGNEE" ${lclSearchTemplate.consignee ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>BILL TM:</cong:td>
                            <cong:td><input type="checkbox" name="billTm" id="BILL_TERMINAL" ${lclSearchTemplate.billTm ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>AES BY:</cong:td>
                            <cong:td><input type="checkbox" name="aesBy" id="AES_BY" ${lclSearchTemplate.aesBy ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>QUOTE BY:</cong:td>
                            <cong:td><input type="checkbox" name="quoteBy" id="QUOTE_BY" ${lclSearchTemplate.quoteBy ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>BOOKED BY:</cong:td>
                            <cong:td><input type="checkbox" name="bookedBy" id="BOOKED_BY" ${lclSearchTemplate.bookedBy ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>CONS:</cong:td>
                            <cong:td><input type="checkbox" name="cons" id="CONSOLIDATE" class="checkbox" ${lclSearchTemplate.cons ? 'checked':''} container="NULL"/></cong:td>
                            <cong:td>BOOKED FOR SAIL DATE:</cong:td>
                            <cong:td><input type="checkbox" name="bookedSaildate" id="BOOKED_SAILDATE" ${lclSearchTemplate.bookedSaildate ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>RELAY OVERRIDE:</cong:td>
                            <cong:td><input type="checkbox" name="relayOverride" id="RELAY_OVERRIDE" ${lclSearchTemplate.relayOverride ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>HOT CODES:</cong:td>
                            <cong:td><input type="checkbox" name="hotCodes" id="HOTCODE" ${lclSearchTemplate.hotCodes ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>LOADING RMKS:</cong:td>
                            <cong:td><input type="checkbox" name="loadingRemarks" id="LOAD_REMARKS" ${lclSearchTemplate.loadingRemarks ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>ORIGIN LRD:</cong:td>
                            <cong:td><input type="checkbox" name="originLrd" id="ORIGIN_LRD" ${lclSearchTemplate.originLrd ? 'checked':''} class="checkbox" container="NULL"/></cong:td>
                            <cong:td>CURRENT LOCATION:</cong:td>
                            <cong:td><input type="checkbox" name="currentLocation" id="CURRENT_LOCATION" class="checkbox" ${lclSearchTemplate.currentLocation ? 'checked':''}  container="NULL"/></cong:td>
                            <cong:td>LINE LOCATION:</cong:td>
                            <cong:td><input type="checkbox" name="lineLocation" id="LINE_LOCATION" class="checkbox" ${lclSearchTemplate.lineLocation ? 'checked':''}  container="NULL"/></cong:td>
                        </cong:tr>  
                        <cong:tr>
                            <cong:td>PN:</cong:td>
                            <cong:td><input type="checkbox" name="pn" id="PN" class="checkbox" ${lclSearchTemplate.pn ? 'checked':''} container="NULL"/></cong:td>
                            
                        </cong:tr>
                    </cong:table>
                </c:otherwise>
            </c:choose>
        </cong:form>
    </body>
    <script type="text/javascript">
        var array = new Array();
        $(document).ready(function () {
            $(".arrowAction").hide();
            $('#listofTemplate tr td:nth-child(2n+1)').css({
                'width': '15%',
                'text-align': 'right'
            });
            if ($("#methodName").val() === "showTemplate") {
                $(".checkbox").attr("disabled", true);
                $("#templateName").attr("readOnly", true);
                $("#templateName").addClass("readOnly");
                $("#save").hide();
            } else if ($("#methodName").val() === "saveTemplate") {
                clearTemplate('${path}');
            }
            addSelectedItemOnDropDown();
            $("#loadtemplateForm").val($("#lclTemplateForm").serialize());
        });

        function editTemplate(path) {
            if ($("#templateId").val() === '') {
                $.prompt("Please Select Atleast One Lcl Template Name");
                return false;
            } else {
                $("#save").show();
                $(".checkbox").attr("disabled", false);
                $("#templateName").attr("readOnly", false);
            }
            $(".orderKey").each(function () {
                array.push($(this).text().trim().trim());
            });
            setDataOnDropDown();
        }
        function clearTemplate(path) {
            var href = path + "/lclTemplate.do?methodName=clear&moduleName=" + $("#moduleName").val();
            document.location.href = href;
        }
        function showTemplate(path) {
            var templateId = $("#templateId").val();
            if (templateId !== '') {
                showLoading();
                $("#templateId").val();
                $("#methodName").val('showTemplate');
                $("#lclTemplateForm").submit();
                closePreloading();
            } else {
                clearTemplate(path);
            }
        }
        function deleteTemplate(path) {
            var templateId = $("#templateId").val();
            if (templateId === '') {
                $.prompt("Please Select Atleast One Template Name");
                return false;
            } else {
                $.prompt("Do you want to proceed deleting this Template?", {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            $.ajax({
                                url: path + "/lclTemplate.do?methodName=delete&id=" + templateId,
                                success: function (data) {
                                    $("#tempListValues").html(data);
                                    clearTemplate(path);
                                }
                            });
                        } else {
                            $.prompt.close();
                        }
                    }
                });
            }
        }
        function ClosePopUp() {
            parent.$.colorbox.close();
        }
        function validateTemplate(templateName) {
            var flag = true;
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkTemplateFlag",
                    param1: templateName,
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    flag = data;
                }
            });
            return flag;
        }

        function submitAjaxForm(path, methodName) {
            var templateName = $('#templateName').val();
            var tempObj = document.getElementById("templateName");
            if (tempObj === null || tempObj.value === "") {
                $.prompt("Template name is required");
                $("#templateName").css("border-color", "red");
                $("#warning").parent.show();
                return false;
            } else if (!$("#FILE_NUMBER").is(":checked")) {
                $.prompt("File No is required");
                $("#FILE_NUMBER").css("background-color", "red");
                $("#warning").parent.show();
                return false;
            } else if (validateTemplate(templateName) && templateName === '') {
                $.prompt('Template name is already available.please enter another template');
                $('#templateName').val("");
                return false;
            } else {
                showLoading();
                var orderedArray = getOrderArrayList();
                $("#orderedData").val(orderedArray);
                $("#templateId").val();
                $("#methodName").val(methodName);
                $("#lclTemplateForm").submit();
                closePreloading();
            }
        }
        function getOrderArrayList() {
            var orderedArray = new Array();
            if ($("#moduleName").val() === 'Exports') {
                $('#orderedItem option').each(function () {
                    orderedArray.push($(this).text());
                });
            } else {
                $('.checkbox').each(function () {
                    if ($(this).is(":checked")) {
                        orderedArray.push($(this).attr("id"));
                    }
                });
            }
            return orderedArray.toString();
        }
        function moveUpDown(action) {
            var option = $('#orderedItem option:selected');
            if (option.length) {
                action === "up" ? option.first().prev().before(option) :
                        option.last().next().after(option);
            }
        }


        function addSelectedItemOnDropDown() {
            $(".checkbox").click(function () {
                if ($(this).is(":checked") && $.inArray($(this).attr("id"), array) === -1) {
                    array.push($(this).attr("id"));
                } else if (!$(this).is(":checked") && $.inArray($(this).attr("id"), array) !== -1) {
                    array.splice($.inArray($(this).attr("id"), array), 1);
                }
                setDataOnDropDown();
            });
        }

        function setDataOnDropDown() {
            var content = "<select name='orderedItemList' id='orderedItem' multiple style='height:150px;width:160px;'>";
            for (var i = 0; i < array.length; i++) {
                content = content + "<option value=" + i + ">" + array[i] + "</option>";
            }
            $("#templateOrderList").html(content + "</select>");
            content = "";

            if (array.length === 0) {
                $(".arrowAction").hide();
            } else {
                $(".arrowAction").show();
            }
        }
    </script>
</html>