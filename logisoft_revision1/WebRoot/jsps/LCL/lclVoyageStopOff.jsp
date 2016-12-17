<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclUnitsSchedule.js"></cong:javascript>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>InLand_Voyage_Stop_Off</title>
    </head>
    <body>
        <cong:form  action="/stopOff" name="lclExportStopOffForm" id="lclExportStopOffForm">
            <input type="hidden" name="methodName" id="methodName"/>
            <input type="hidden" name="unLocationId" id="unLocationId"/>
            <input type="hidden" name="delLocId" id="delLocId"/>
            <input type="hidden" name="detailId" id="detailId"/>
            <input type="hidden" name="buttonValue" id="buttonValue"/>
            <input type="hidden" name="headerId" value="${lclAddVoyageForm.headerId}" />
            <input type="hidden" name="stopOffIndex" id="stopOffIndex"/>
            <input type="hidden" name="filterByChanges" id="filterByChanges" value="${lclAddVoyageForm.filterByChanges}">
            <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <div class="tableHeadingNew"><span>Voyage Stop-Offs</span> </div>
            </table>
            <table border="0" width="50%">
                <tr>
                    <td class="style2" align="right">City</td>
                    <td>
                        <cong:autocompletor  name="landExitCity" styleClass=""  position="center" id="landExitCity" fields="null,null,unLocationCode,landExitCityId"
                                             shouldMatch="true" width="200" query="RELAYNAME" template="one" container="null" scrollHeight="200px" callback="getWareHouse('mainEntry')" value=""/>
                        <input type="hidden" id="landExitCityId" name="landExitCityId"/>
                        <input type="hidden" id="unLocationCode" name="unLocationCode"/>
                    </td>
                    <td class="style2" align="right">Ware House</td>
                    <td>
                        <cong:text  name="warehouseName" styleClass="textlabelsBoldForTextBox"  id="warehouseName" value=""/>
                        <cong:text  id="warehouseNo" name="warehouseNo" styleClass="text smallTextlabelsBoldForTextBox ac_input" value=""/>
                        <cong:hidden  id="warehouseId" name="warehouseId" styleClass="text smallTextlabelsBoldForTextBox ac_input"/>
                    </td>
                    <td class="style2" align="right">Remarks</td>
                    <td>
                        <cong:textarea cols="30" rows="3" name="stopOffRemarks" id="stopOffRemarks"/>
                    </td>
                </tr>
                <tr>
                    <td class="style2" align="right">ETD Date</td>
                    <td>
                        <cong:calendarNew styleClass="mandatory textWidth" id="stopOffETD" name="stopOffETD" value="" onchange="validateETA();ETDwithDetailETD();ETDwithDetailETA();"/>
                    </td>
                    <td class="style2" align="right">ETA Date</td>
                    <td>
                        <cong:calendarNew styleClass="mandatory textWidth" id="stopOffETA" name="stopOffETA" value="" onchange="validateETD();ETAwithDetailETA();"/>
                    </td>
                    <td></td></tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td></td> <td></td> <td></td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty lclExportStopOffForm.headerId}">
                                <div class="button-style1" id="savestopoffs" onclick="addStopOff($('#landExitCityId').val(),'addVoyageStopOff');">Save</div>
                            </c:when>
                            <c:otherwise>
                                <div class="button-style1" id="stopoffs" onclick="addStopOff($('#landExitCityId').val(),'stopOff');">Add</div>
                            </c:otherwise>
                        </c:choose>
                        <div class="button-style1" id="cancel" onclick="closePopUp();">Cancel</div>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="index" id="index" value="${lclExportStopOffForm.index}"/>
            <input type="hidden" name="buttonValue" id="buttonValue"/>
            <input type="hidden" name="headerId" id="headerId" value="${lclExportStopOffForm.headerId}"/>
            <input type="hidden" name="detailIdCount" id="detailIdCount"/>
        </cong:form>
    </body>
</html>
<script type="text/javascript">
    var mouse_is_inside = false;
    $(document).ready(function ()
    {
        $("#landExitCity").keyup(function(){
            if($(this).val().trim()===""){
                $("#warehouseName").val("");
                $("#warehouseNo").val("");
                $("#warehouseId").val("");
            } 
        });
    });

    function validateETA(){
        var ETA=$('#stopOffETD').val().split("-");
        var ETD= $('#stopOffETA').val().split("-");
        var days=getDaysBetween(ETA,ETD);
        if(days >= 0){
            $.prompt("ETD should be less than ETA");
            $("#stopOffETD").val("");
        }
    }
    function validateETD(){
        var ETA=$('#stopOffETD').val().split("-");
        var ETD= $('#stopOffETA').val().split("-");
        var days=getDaysBetween(ETA,ETD);
        if(days >= 0){
            $.prompt("ETA should be greater than ETD");
            $('#stopOffETA').val("");
        }
    }
    function ETDwithDetailETD(){
        var PrentETD=parent.$('#std').val().split("-");
        var ETD= $('#stopOffETD').val().split("-");
        var days=getDaysBetween(PrentETD,ETD);
        if(days >= 0){
            $.prompt("ETD should be greater than Detail ETD");
            $("#stopOffETD").val("");
        }
    }
    function ETAwithDetailETA(){
        if($('#stopOffETD').val()==='' || $('#stopOffETD').val()===null){
            $.prompt("Please Enter ETD");
            $("#stopOffETA").val("");
        }else{
            var ParentETA= parent.$('#etaPod').val().split("-");
            var ETA= $('#stopOffETA').val().split("-");
            var days=getDaysBetween(ParentETA,ETA);
            if(days < 0){
                $.prompt("ETA should be Less  than Detail ETA");
                $("#stopOffETA").val("");
            }
        }
    }
    function ETDwithDetailETA(){
        var ETD=$('#stopOffETD').val().split("-");
        var ParentETA= parent.$('#etaPod').val().split("-");
        var days=getDaysBetween(ETD,ParentETA);
        if(days > 0){
            $.prompt("ETD should be Less than Detail ETA");
            $("#stopOffETD").val("");
        }
    }
    function getDaysBetween(etdArray,etaArray){
        var date1 =new Date(etdArray[2],getMonthNumber(etdArray[1])-1,etdArray[0]);
        var date2 =new Date(etaArray[2],getMonthNumber(etaArray[1])-1,etaArray[0]);
        var distance = date1.getTime() - date2.getTime();
        distance = Math.ceil(distance / 1000 / 60 / 60 / 24);
        return distance;
    }

    function addStopOff(unLocId,buttonValue){
        var unLocArray =new Array();
        var flag=true;
        parent.$(".unLocId").each(function(){
            unLocArray.push($(this).text().trim());
        });
        for(var i=0;i<unLocArray.length;i++){
            if(unLocArray[i] === unLocId){
                $.prompt("Stop-Offs Already Exists");
                flag=false;
                break;
            }
        }
        if($('#stopOffETD').val()===''){
            $.prompt("Please Enter ETD");
            flag=false;
        }else if($('#stopOffETA').val()===''){
            $.prompt("Please Enter ETA");
            flag=false;
        }
        if(flag){
            parent.$("#filterByChanges").val('lclDomestic');
            parent.$("#stopOffRemarks").val($("#stopOffRemarks").val());
            parent.$("#stopOffETD").val($("#stopOffETD").val());
            parent.$("#stopOffETA").val($("#stopOffETA").val());
            parent.$("#warehouseName").val($("#warehouseName").val());
            parent.$("#warehouseNo").val($("#warehouseNo").val());
            parent.$("#warehouseId").val($("#warehouseId").val());
            parent.$('#buttonValue').val(buttonValue);
            parent.$("#stopOffIndex").val($("#index").val());
            parent.$('#unLocationId').val($("#landExitCityId").val());
            parent.$("#methodName").val('addStopOff');
            parent.$("#lclAddVoyageForm").submit();
        }
    }
    function isStopOffExist(unLocId){
        var unLocArray =new Array();
        var flag=true;
        parent.$(".unLocId").each(function(){
            unLocArray.push($(this).text().trim());
        });
        for(var i=0;i<unLocArray.length;i++){
            if(unLocArray[i] === unLocId){
                flag=false;
                break;
            }
        }
        return flag;
    }

    function addMoreStopOff(unLocId,index){
        var unLocArray =new Array();
        var flag=true;
        parent.$(".unLocId").each(function(){
            unLocArray.push($(this).text().trim());
        });
        for(var i=0;i<unLocArray.length;i++){
            if(unLocArray[i] === unLocId){
                $.prompt("Stop-Offs Already Exists");
                flag=false;
                break;
            }
        }
        if(flag){
            parent.$("#filterByChanges").val('lclDomestic');
            parent.$("#stopOffRemarks").val($("#stopOffRemarks").val());
            parent.$("#stopOffETD").val($("#stopOffETD").val());
            parent.$("#stopOffETA").val($("#stopOffETA").val());
            parent.$("#warehouseName").val($("#warehouseName").val());
            parent.$("#warehouseNo").val($("#warehouseNo").val());
            parent.$("#warehouseId").val($("#warehouseId").val());
            parent.$('#buttonValue').val('stopOff');
            parent.$("#stopOffIndex").val(index);
            parent.$('#unLocationId').val($("#landExitCityId"+index).val());
            parent.$("#methodName").val('addStopOff');
            parent.$("#lclAddVoyageForm").submit();
        }
    }

    function deleteStopOff(id,detailId){
        $('#delLocId').val(id);
        $('#detailId').val(detailId);
        $('#buttonValue').val('removeStopOff');
        $("#methodName").val('addStopOff');
        $("#lclAddVoyageForm").submit();
    }

    function updateStopOff(){
        showLoading();
        var unLocArray =new Array();
        $(".detailId").each(function(){
            if($(this).text().trim()==''){
                unLocArray.push($(this).text().trim());
            }
        });
        $("#detailIdCount").val(unLocArray.length);
        $("#buttonValue").val("updateStopOff");
        $("#methodName").val("addStopOff");
        //        $("#lclAddVoyageForm").submit();
        var params=$("#lclAddVoyageForm").serialize();
        $.post($("#lclAddVoyageForm").attr("action"), params, function (data){
            if(data){
                parent.$.colorbox.close();
                $("#methodName").val('editVoyage');
            }
        });
    }
    function deleteTempData(){
        $("#buttonValue").val("clearStopOff");
        $("#methodName").val("addStopOff");
        $("#lclAddVoyageForm").submit();
    }

    function getWareHouse(flag){
        var unLocId=$("#landExitCityId").val();
        if(parent.parent.$("#originId").val() == unLocId || parent.parent.$("#finalDestinationId").val()==unLocId){
            $.prompt("Stop-Offs Already Exists");
            clearFields();
        }else if(parent.$("#originId").val() == unLocId || parent.$("#finalDestinationId").val()==unLocId){
            $.prompt("Stop-Offs Already Exists");
            clearFields();
        }else if(!isStopOffExist(unLocId)){
            $.prompt("Stop-Offs Already Exists");
            clearFields(); 
        }else{
            var unloc=$("#unLocationCode").val();
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "getWareHouseByTerminal",
                    param1: unloc,
                    dataType: "json"
                },
                async:false,
                success: function(data) {
                    if (data!=null) {
                        if(flag==="mainEntry"){
                            $("#warehouseName").val(data[0]);
                            $("#warehouseNo").val(data[1]);
                            $("#warehouseId").val(data[2]);
                        }
                    }
                }
            });
        }
    }
    function clearFields(){
        $("#landExitCity").val("");
        $("#remarks").val("")
        $("#std").text(" ");
        $("#etaPod").text(" ");
        $("#warehouseName").val("");
        $("#warehouseNo").val("");
        $("#warehouseId").val("");
    }
    function closePopUp(){
        parent.$.colorbox.close();
    }
</script>
