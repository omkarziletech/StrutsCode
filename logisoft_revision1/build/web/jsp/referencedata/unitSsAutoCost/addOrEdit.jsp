<%-- 
    Document   : addOrEdit
    Created on : Mar 28, 2016, 10:56:30 AM
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
        <title>Add Or Edit Unit Auto Cost</title>
    </head>
    <body>
        <div id="pane">
            <%@include file="../../../jsps/preloader.jsp"%>
            <html:form action="/unitSsAutoCosting" type="com.logiware.referencedata.action.LclUnitSsAutoCostingAction"
                       name="unitSsAutoCostingForm" styleId="unitSsAutoCostingForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="unitSsCostId" styleId="unitSsCostId"/>
                <html:hidden property="originId" styleId="originId"/>
                <html:hidden property="fdId" styleId="fdId"/>
                <table class="table" style="margin: 0;overflow: hidden">
                    <tr class="tableHeadingNew">
                        <td colspan="2">${unitSsAutoCostingForm.unitSsCostId eq 0 ? "Add" :"Edit"} Unit Auto Cost</td>
                        <td align="right">Origin:</td>
                        <td><span style="color: green">${unitSsAutoCostingForm.originName}</span></td>
                        <td align="right">Destination:</td>
                        <td><span style="color: green">${unitSsAutoCostingForm.fdName}</span></td>
                    </tr>
                    <tr>
                        <td colspan="6"></td>
                    </tr>
                    <tr>
                        <td  class="label" style="float: right">Cost Code</td>
                        <td>
                            <cong:autocompletor name="addGlMapCode" id="addGlMapCode" template="two" query="COST_CODE" fields="null,addGlMappingId"
                                                shouldMatch="true" scrollHeight="150" params="LCLE" value="${unitSsAutoCostingForm.addGlMapCode}"
                                                container="NULL" styleClass="textlabelsBoldForTextBoxWidth" width="400"/>
                            <cong:hidden name="addGlMappingId" id="addGlMappingId" value="${unitSsAutoCostingForm.addGlMappingId}"/>
                        </td>
                        <td class="label" style="float: right">
                            Size

                        </td>
                        <td>
                            <html:select property="addUnitTypeId" styleId="addUnitTypeId" styleClass="smallDropDown" >
                                <html:option value="">Select</html:option>
                                <html:optionsCollection property="unitTypeList" label="label" value="value"/>
                            </html:select>
                        </td>
                        <td class="label" style="float: right">Vendor Name</td>
                        <td>
                            <cong:autocompletor  name="addVendorName"  id="addVendorName" fields="addVendorNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdpartyDisabled,NULL,NULL,NULL,NULL,NULL,thirdparyDisableAcct"
                                                 container="NULL"  shouldMatch="true" width="530" query="VENDOR" value="${unitSsAutoCostingForm.addVendorName}"
                                                 template="tradingPartner" scrollHeight="150" styleClass="text mandatory require" />
                            <cong:text  name="addVendorNo" id="addVendorNo" style="width:80px" value="${unitSsAutoCostingForm.addVendorNo}"
                                        styleClass="text-readonly" readOnly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6"></td>
                    </tr>
                    <tr>
                        <td class="label" style="float: right">Type</td>
                        <td>
                            <input type="radio" id="costTypeId" name="costType" value="BASE" onclick="changeType();"
                                   ${unitSsAutoCostingForm.costType eq 'BASE' ? 'Checked' :''}/>BASE
                            <input type="radio" id="costTypeId" name="costType" onclick="changeType();"
                                   ${unitSsAutoCostingForm.costType eq 'ADD' ? 'Checked' :''}
                                   value="ADD"/>ADD
                        </td>
                        <td class="label" style="float: right">Rate Uom</td>
                        <td>
                            <html:select property="rateUom" styleId="rateUom"  value="${unitSsAutoCostingForm.rateUom}"
                                         styleClass="smallDropDown" onchange="changeType();">
                                <html:option value="">Select</html:option>
                                <html:option value="FLAT">FLAT</html:option>
                                <html:option value="MEASURE">MEASURE</html:option>
                                <html:option value="WEIGHT">WEIGHT</html:option>
                            </html:select>
                        </td>
                        <td class="label" style="float: right">Rate Per Uom</td>
                        <td>
                            <cong:text  name="ratePerUomAmt" id="ratePerUomAmt" onkeyup="checkForNumberAndDecimal(this);"
                                        value="${unitSsAutoCostingForm.ratePerUomAmt}" />
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6"></td>
                    </tr>
                    <tr id="rateByAdd">
                        <td class="label" style="float: right">Rate Action</td>
                        <td>
                            <html:select property="rateAction" styleId="rateAction" value="${unitSsAutoCostingForm.rateAction}"
                                         styleClass="smallDropDown">
                                <html:option value="">Select</html:option>
                                <html:option value="+">+</html:option>
                                <html:option value="-">-</html:option>
                                <html:option value="*">*</html:option>
                                <html:option value="/">/</html:option>
                            </html:select>
                        </td>
                        <td class="label" style="float: right">Rate Uom</td>
                        <td>
                            <html:select property="rateCondition" styleId="rateCondition" value="${unitSsAutoCostingForm.rateCondition}"
                                         styleClass="smallDropDown">
                                <html:option value="">Select</html:option>
                                <html:option value="EQ">EQ</html:option>
                                <html:option value="EQGT">EQGT</html:option>
                                <html:option value="EQLT">EQLT</html:option>
                                <html:option value="GT">GT</html:option>
                                <html:option value="LT">LT</html:option>
                            </html:select>
                        </td>
                        <td class="label" style="float: right">Rate Condition Qty</td>
                        <td>
                            <cong:text  name="rateConditionQty" id="rateConditionQty"  onkeyup="checkForNumberAndDecimal(this);"
                                        value="${unitSsAutoCostingForm.rateConditionQty}"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6"></td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button"  value="Save" id="save"
                                   align="middle" class="button" onclick="saveCost();"/>
                            <input type="button"  value="Close" id="Close"
                                   align="middle" class="button" onclick="closePopUp();"/>
                        </td>
                    </tr>
                </table>
            </html:form>
        </div>
    </body>
    <script type="text/javascript">
        jQuery(document).ready(function () {
            changeType();
        });
        function changeType(){
            var rateUom=$('#rateUom').val();
            var costType=$('input:radio[name=costType]:checked').val();
            if(rateUom!='' && costType==='ADD' &&  rateUom!='FLAT'){
                $('#rateByAdd').show();
            }else{
                $('#rateConditionQty').val("");
                $('#rateCondition').val("");
                $('#rateAction').val("");
                $('#rateByAdd').hide();
            }
        }

        function saveCost(){
            var costCode=$('#addGlMapCode').val();
            if(costCode==='' || costCode===null){
                $.prompt("Cost Code is Required");
                $("#addGlMapCode").css("border-color", "red");
                return false;
            }
            var unitTypeId=$('#addUnitTypeId').val();
            if(unitTypeId==='' || unitTypeId===null){
                $.prompt("UnitSize is Required");
                $("#addUnitTypeId").css("border-color", "red");
                return false;
            }
            var vendorNo=$('#addVendorNo').val();
            if(vendorNo==='' || vendorNo===null){
                $.prompt("Vendor Account is Required");
                $("#addVendorName").css("border-color", "red");
                return false;
            }
            var costType=$('input:radio[name=costType]:checked').val();
            if(costType==='' || costType==undefined){
                $.prompt("Type is Required");
                return false;
            }
            var rateUom=$('#rateUom').val();
            if(rateUom===''){
                $.prompt("Rate UOM is Required");
                $("#addGlMapCode").css("border-color", "red");
                return false;
            }
            var ratePerUomAmt=$('#ratePerUomAmt').val();
            if(ratePerUomAmt===''){
                $.prompt("Rate Per UOM is Required");
                $("#ratePerUomAmt").css("border-color", "red");
                return false;
            }
            var rateConditionFlag=(costType==='ADD' && rateUom!='FLAT') ? true :false;
            if(rateConditionFlag){
                var rateAction=$('#rateAction').val();
                if(rateAction===''){
                    $.prompt("Rate Action is Required");
                    $("#rateAction").css("border-color", "red");
                    return false;
                }
                var rateCondition=$('#rateCondition').val();
                if(rateCondition===''){
                    $.prompt("Rate Condition is Required");
                    $("#rateCondition").css("border-color", "red");
                    return false;
                }
                var rateConditionQty=$('#rateConditionQty').val();
                if(rateConditionQty===''){
                    $.prompt("Rate Condition Qty is Required");
                    $("#rateConditionQty").css("border-color", "red");
                    return false;
                }
            }
            showProgressBar();
            $("#action").val("saveCost");
            var params = $("#unitSsAutoCostingForm").serialize();
            params += "&unitSsCostId=" + $('#unitSsCostId').val();
            $.post($("#unitSsAutoCostingForm").attr("action"), params,
            function (data) {
                parent.searchCost();
            });
        }
        function closePopUp(){
            parent.$.fn.colorbox.close();
        }
        function checkForNumberAndDecimal(obj) {
            if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
                obj.value = "";
                $.prompt("This field should be Numeric");
            }
        }
    </script>
</html>
