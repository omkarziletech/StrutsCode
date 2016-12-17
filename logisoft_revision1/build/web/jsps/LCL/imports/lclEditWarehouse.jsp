<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsps/LCL/init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
    <body style="background:#ffffff">
        <form name="lclEditWarehouseForm" id="lclEditWarehouseForm" action="/lclEditWarehouse.do">
            <table width="98%" style="border:1px solid #dcdcdc" border="0">
                <tr class="tableHeadingNew">
                    <td colspan="2">
                        Warehouse# <span class="fileNo" style="color:#0000FF">${wareHsNo}</span>
                </td> 
            </tr>
            <tr>
                <td class="textlabelsBoldforlcl">Warehouse Name</td>
                <td class="textlabelsBoldleftforlcl">
                    <cong:autocompletor name="warehouseName" id="warehouseName" width="250" scrollHeight="120" styleClass="mandatory textLCLuppercase" 
                                        query="IMPORTWAREHOUSE" fields="NULL,warehouseNo,NULL,NULL,NULL,NULL,NULL,NULL,warehouseId" template="delwhse"
                                        container="NULL" shouldMatch="true"/>
                    <input type="text" id="warehouseNo" name="warehouseNo" style="width:70px" readOnly="true" class="textlabelsBoldForTextBoxDisabledLook"/>
                    <cong:hidden name="warehouseId" id="warehouseId" />
                </td>
            </tr>
             <tr><td colspan="2"></td></tr>
             <tr><td colspan="2"></td></tr>
             <tr><td colspan="2"></td></tr>
            <tr>
                <td></td>
                <td>
                    <input type="button" class="button-style1" value="Save" id="saveCode" onclick="saveWarehouse();"/>
                </td>
            </tr>
        </table>
        <cong:hidden name="methodName" id="methodName"/>
        <cong:hidden name="unitWarehouseId" id="unitWarehouseId" value="${lclEditWarehouseForm.unitWarehouseId}"/>
    </form>
    <script type="text/javascript">
                        function saveWarehouse() {
                            if ($("#warehouseName").val() === "" || $("#warehouseName").val() === null) {
                                $.prompt("Warehouse Name is required");
                                $("#warehouseName").css("border-color", "red");
                                $("#warning").show();
                            } else {
                                showLoading();
                                parent.$("#unitWarehouseId").val($("#unitWarehouseId").val());
                                parent.$("#warehouseId").val($("#warehouseId").val());
                                parent.$("#methodName").val('saveWarehouse');
                                parent.$("#lclAddUnitsForm").submit();
                            }
                        }
    </script>
</body>
