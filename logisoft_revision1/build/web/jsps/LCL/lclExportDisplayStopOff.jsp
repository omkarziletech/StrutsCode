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
        <cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm">
            <input type="hidden" name="methodName" id="methodName"/>
            <input type="hidden" name="unLocationId" id="unLocationId"/>
            <input type="hidden" name="delLocId" id="delLocId"/>
            <input type="hidden" name="detailId" id="detailId"/>
            <input type="hidden" name="buttonValue" id="buttonValue"/>
            <input type="hidden" name="headerId" id="headerId" value="${lclAddVoyageForm.headerId}" />
            <input type="hidden" name="stopOffIndex" id="stopOffIndex"/>
            <input type="hidden" name="filterByChanges" id="filterByChanges" value="${lclAddVoyageForm.filterByChanges}">
            <input type="hidden" name="warehouseName" id="warehouseName"/>
            <input type="hidden" name="warehouseNo" id="warehouseNo"/>
            <input type="hidden" name="warehouseId" id="warehouseId"/>
            <input type="hidden" name="stopOffRemarks" id="stopOffRemarks"/>
            <input type="hidden" name="stopOffETD" id="stopOffETD"/>
            <input type="hidden" name="stopOffETA" id="stopOffETA"/>
            <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <div class="tableHeadingNew"  onclick="viewmainEntry()">
                    <span>Voyage Stop-Offs</span>
                </div>
            </table>
            <table class="dataTable" border="0"  id="stopAddList" style="width:30%;">
                <thead>
                    <tr>
                        <th>Stop-Off City</th>
                        <th>Warehouse</th>
                        <th>ETD</th>
                        <th>ETA</th>
                        <th>Remarks</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <c:if test="${not empty stopAddList || stopAddList.addOrRemove eq true}">

                    <c:forEach items="${stopAddList}" var='stopAdd' varStatus="stopOff">
                        <c:choose>
                            <c:when test="${zebra=='odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${stopAdd.addOrRemove eq true}">
                            <tr class="${zebra}">
                                <fmt:formatDate pattern="dd-MMM-yyyy" var="departureDate" value="${stopAdd.stdDate}"></fmt:formatDate>
                                <fmt:formatDate pattern="dd-MMM-yyyy" var="arrivalDate" value="${stopAdd.staDate}"></fmt:formatDate>
                                <td style="display:none; width:20%"><p class="unLocId">${stopAdd.unlocationId}</p></td>
                                <td>${stopAdd.countryValue}</td>
                                <td>${stopAdd.wareHouse}</td>
                                <td>${departureDate}</td>
                                <td>${arrivalDate}</td>
                                <td>${stopAdd.remarks}</td>
                                <td>
                                    &nbsp;&nbsp;
                                    <img src="${path}/jsps/LCL/images/close1.png"
                                         alt="delete" height="16" onclick="deleteStopOff('${stopAdd.unlocationId}','${stopAdd.detailId}');"/>

                                    &nbsp;&nbsp;&nbsp;<img src="${path}/jsps/LCL/images/add2.gif"
                                                           alt="add" height="16" onclick="addinternalstopOff('${path}','${stopOff.index}');"/>

                                </td>
                                <td style="display:none;"><p class="detailId">${stopAdd.detailId}</p></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:if>
            </table>
            <table style ="margin-top:5%;"align="center">
                <tr><td></td>
                    <c:choose>
                        <c:when test="${not empty lclAddVoyageForm.headerId && lclAddVoyageForm.headerId!=0}">
                            <td><input type="button" class="button-style1" id="addStopOff" value="Add" onclick="addMoreStopOff('${path}');"></td>
                            <td><input type="button" class="button-style1" id="save" value="Save" onclick="updateStopOff();"></td>
                            <td><input type="button" class="button-style1" id="clear" value="Clear" onclick="deleteTempData();"></td>
                            </c:when>
                            <c:otherwise>
                            <td><input type="button" class="button-style1" id="Ok" value="Ok" onclick="closePopUp();"></td>
                            </c:otherwise>
                        </c:choose>
                </tr>
            </table>
            <input type="hidden" name="index" id="index"/>
            <input type="hidden" name="buttonValue" id="buttonValue"/>
            <input type="hidden" name="detailIdCount" id="detailIdCount"/>
        </cong:form>
    </body>
</html>
<script type="text/javascript">
    var mouse_is_inside = false;
    $(document).ready(function ()
    {
        parent.parent.$("#originId").val();
        $("#landExitCity").keyup(function(){
            if($(this).val().trim()===""){
                $("#warehouseName").val("");
                $("#warehouseNo").val("");
                $("#warehouseId").val("");
            }
        });
    });

    function ETDwithDetailETD(){
        var PrentETD=parent.$('#std').val().split("-");
        var ETD= $('#std').val().split("-");
        var days=getDaysBetween(PrentETD,ETD);
        if(days >= 0){
            $.prompt("ETD should be greater than Detail ETD");
            $("#std").val("");
        }
    }
    function ETAwithDetailETA(){
        if($('#std').val()==='' || $('#std').val()===null){
            $.prompt("Please Enter ETD");
            $("#etaPod").val("");
        }else{
            var ParentETA= parent.$('#etaPod').val().split("-");
            var ETA= $('#etaPod').val().split("-");
            var days=getDaysBetween(ParentETA,ETA);
            if(days < 0){
                $.prompt("ETA should be Less  than Detail ETA");
                $("#etaPod").val("");
            }
        }
    }
    function ETDwithDetailETA(){
        var ETD=$('#std').val().split("-");
        var ParentETA= parent.$('#etaPod').val().split("-");
        var days=getDaysBetween(ETD,ParentETA);
        if(days > 0){
            $.prompt("ETD should be Less than Detail ETA");
            $("#std").val("");
        }
    }

    function getDaysBetween(etdArray,etaArray){
        var date1 =new Date(etdArray[2],getMonthNumber(etdArray[1])-1,etdArray[0]);
        var date2 =new Date(etaArray[2],getMonthNumber(etaArray[1])-1,etaArray[0]);
        var distance = date1.getTime() - date2.getTime();
        distance = Math.ceil(distance / 1000 / 60 / 60 / 24);
        return distance;
    }

    //    function addStopOff(unLocId){
    //        var unLocArray =new Array();
    //        var flag=true;
    //        $(".unLocId").each(function(){
    //            unLocArray.push($(this).text().trim());
    //        });
    //        for(var i=0;i<unLocArray.length;i++){
    //            if(unLocArray[i] === unLocId){
    //                $.prompt("Stop-Offs Already Exists");
    //                flag=false;
    //                break;
    //            }
    //        }
    //
    //        if(flag){
    //            $('#buttonValue').val('stopOff');
    //            $("#stopOffIndex").val($("#index").val());
    //            $('#unLocationId').val($("#landExitCityId").val());
    //            $("#methodName").val('addStopOff');
    //            var params=$("#lclAddVoyageForm").serialize();
    //            $.post($("#lclAddVoyageForm").attr("action"), params, function (data){
    //                if(data!=null){
    //                    parent.$.colorbox.close();
    //                    parent.$("#stopOffs").html(data);
    //                }
    //            });
    //        }
    //    }

    function addMoreStopOff(path){
       
        var href=path+"/stopOff.do?methodName=displayStopOff&headerId="+$("#headerId").val();
        $.colorbox({
            iframe:true,
            href:href,
            width:"90%",
            height:"80%",
            title:"Add Stop_Off"
        });
    }

    function deleteStopOff(id,detailId){
        $('#delLocId').val(id);
        $('#detailId').val(detailId);
        $('#buttonValue').val('removeVoyageStopOff');
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

    function addinternalstopOff(path,index){
        var href=path+"/stopOff.do?methodName=displayStopOff&headerId="+$("#headerId").val()+"&index="+index;
        $.colorbox({
            iframe:true,
            href:href,
            width:"90%",
            height:"80%",
            title:"Add Stop_Off"
        });
    }
</script>
