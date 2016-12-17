<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Unit</title>
    </head>
    <body>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="left">
            <tr class="tableHeadingNew">
                <td width="20%">What do you want to do with the unit</td>
            </tr>
        </table>
        <cong:hidden id="unitId" name="unitId" value="${unitId}"/>
        <input type="hidden" id="loginUserId" name="loginUserId" value="${loginuser.userId}"/>
        <input type="hidden" name="ssHeader" id="ssHeaderId"/>
         <cong:hidden id="sealNo" name="sealNo" value="${sealNo}"/>
         <cong:hidden id="chassisNo" name="chassisNo" value="${chassisNo}"/>
         <cong:hidden id="loadedByUserId" name="loadedByUserId" value="${loadedByUserId}"/>  
       
        <table width="100%" align="left">
            <tr><td class="textBoldforlcl">
                    <input type="radio" name="dltMoveAction" value="delete" checked onclick="DisableWareHouse();" />
                    Delete unit</td>
            </tr>
            <tr>
                <td class="textBoldforlcl">
                    <input type="radio" name="dltMoveAction" value="move" onclick="EnableWareHouse();" />
                    Move Unit to Yard &nbsp;
                    <label for="Warehouse">Warehouse Name</label>
                    <cong:autocompletor name="warehouseNo" id="warehouseNo" width="300" scrollHeight="150px" styleClass="mandatory smallTextlabelsBoldForTextBox "
                                        query="WAREHOUSE_WITH_WHSENAME" fields="warehouseName,warehouseId"
                                        container="NULL" template="three" shouldMatch="true" callback="searchUnAssingedUnit('searchByUnAssignUnit');"/>
                    <cong:text name="warehouseName" id="warehouseName" styleClass="textLCLuppercase"/>
                    <cong:hidden name="warehouseId" id="warehouseId"/>
                </td>
            </tr>
            <tr>
                <td><br/><br/>
                    <input type="button" id="continueDltMove"  class="button-style3" Value="Continue" onclick="DltMoveProceed();"/>
                    <input type="button" id="cancelDltMove"  class="button-style3" value="Cancel" onclick="closepopup1();"/></td>
            </tr>
        </table>
    </body>
</html>

<script type="text/javascript">
    $(document).ready(function () {
        var dltRadio = $('input:radio[name=dltMoveAction]:checked').val();
        if (dltRadio == 'delete') {
            $('label[for="Warehouse"]').hide();
            $('#warehouseNo').hide();
            $('#warehouseName').hide();
        }        
    });

    function EnableWareHouse(){
        $('label[for="Warehouse"]').show();
        $('#warehouseNo').show();
        $('#warehouseName').show();
    }
    function searchUnAssingedUnit(methodName) {
        if ($("#warehouseName").val() === '' || $("#warehouseName").val() === null
            || $("#dispositionCode").val() === '' || $("#dispositionCode").val() === null) {
            return false;
        }
        // window.parent.showLoading();
        $("#warehouseId").val();
        $("#methodName").val(methodName);
        $("#lclUnitsScheduleForm").submit();
    }
    function DisableWareHouse(){
        $('label[for="Warehouse"]').hide();
        $('#warehouseNo').hide();
        $('#warehouseName').hide();
    }

    function closepopup1(){
        parent.$.colorbox.close();
        
    }
    function DltMoveProceed(){
        var selAction=$("[name='dltMoveAction']:checked").val();
        var unitId=document.getElementById("unitId").value;
        var warehouseId=document.getElementById("warehouseId").value;
        //var warehouseNo=document.getElementById("warehouseNo").value;
        var loguserId = $('#loginUserId').val();
        if(selAction == 'delete') {
            showProgressBar();
            lcldeleteUnits();
            hideProgressBar();
        } else {
            if(warehouseId=="" || warehouseId == undefined){
                document.getElementById("warehouseNo").focus();
                sampleAlert("Please select ware house No ");
                return false;
            }else {
                showProgressBar(); 
                lcldeleteUnits();
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO",
                        methodName: "insert",
                        param1:unitId,
                        param2:warehouseId,
                        param3:loguserId,
                        param4:$('#sealNo').val() ,
                        param5:$('#chassisNo').val(), 
                        param6:$('#loadedByUserId').val()                         
                    },
                    preloading: true,
                    success: function (data) {
                         
                    }
                });
                hideProgressBar();
            }
        }
    }
    function lcldeleteUnits(){
        var unitId=document.getElementById("unitId").value;
        var dltMoveAction=$("[name='dltMoveAction']:checked").val();
        parent.$("#methodName").val('deleteUnits');
        parent.document.getElementById("unitId").value = unitId;
        parent.$("#deleteMoveAction").val(dltMoveAction);
        var warehouseId=document.getElementById("warehouseId").value;
        if(warehouseId != undefined && warehouseId !="" ){
        var wareHouseNo=document.getElementById("warehouseNo").value;
        parent.$("#wareHouseNo").val(wareHouseNo);
        }
        parent.$("#lclAddVoyageForm").submit();
    }
</script>