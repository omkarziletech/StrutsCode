<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<body style="background:#ffffff">
    <cong:form name="lclEditWarehouseForm" id="lclEditWarehouseForm" action="/lclEditWarehouse.do">
        <cong:table width="98%" style="border:1px solid #dcdcdc" border="0">
            <cong:tr styleClass="tableHeadingNew"><cong:td colspan="8">
                    Warehouse# <span class="fileNo" style="color:#0000FF">${warehouseNumber}</span>
                </cong:td> </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Warehouse Name</cong:td>
                <cong:td styleClass="textlabelsBoldleftforlcl">
                    <cong:autocompletor name="warehouseName" id="warehouseName" width="400" scrollHeight="200px" styleClass="mandatory textLCLuppercase"  query="WAREHOUSE" fields="NULL,NULL,warehouseId"
                                        container="NULL" template="three" shouldMatch="true"/>
                    <cong:hidden name="warehouseId" id="warehouseId" />
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Arrived DateTime</cong:td>
                <cong:td styleClass="textlabelsBoldleftforlcl">
                    <cong:calendarNew  id="arrivedDateTime" name="arrivedDateTime" value=""/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Departed DateTime</cong:td>
                <cong:td styleClass="textlabelsBoldleftforlcl">
                    <cong:calendarNew  id="departedDateTime" name="departedDateTime" value=""/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Location</cong:td>
                <cong:td styleClass="textlabelsBoldleftforlcl">
                    <cong:text id="location" name="location" maxlength="10" style="text-transform: uppercase" />
                </cong:td>
            </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Seal Number</cong:td>
                        <cong:td  styleClass="textlabelsBoldleftforlcl">
                            <cong:text id="sealNoOut" name="sealNoOut" style="text-transform: uppercase" container="null"/>
                        </cong:td>
                        <cong:td colspan="6">
                            </cong:td>
                    </cong:tr>
            <cong:tr>
                <cong:td colspan="2"></cong:td><cong:td colspan="6">
                    <input type="button" class="button-style1" value="Save" id="saveCode" onclick="saveWarehouse()"/>
                </cong:td>
            </cong:tr>
            <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
        </cong:table>
        <cong:hidden name="methodName" id="methodName"/>
        <cong:hidden name="unitWarehouseId" id="unitWarehouseId" />
    </cong:form>
    <script type="text/javascript">
        function saveWarehouse(){
            if(document.getElementById("warehouseName").value==""||document.getElementById("warehouseName").value==""){
                sampleAlert("Warehouse name is required");
                $("#warehouseName").css("border-color","red");
                $("#warning").show();
            }else{
                parent.document.getElementById("unitWarehouseId").value = document.getElementById("unitWarehouseId").value;
                parent.document.getElementById("warehouseId").value = document.getElementById("warehouseId").value;
                parent.document.getElementById("location").value = document.getElementById("location").value;
                parent.document.getElementById("arrivedDateTime").value = document.getElementById("arrivedDateTime").value;
                parent.document.getElementById("departedDateTime").value = document.getElementById("departedDateTime").value;
                parent.document.getElementById("sealNoOut").value = document.getElementById("sealNoOut").value;
                parent.$("#methodName").val('addWareHouse');
                parent.$("#lclAddUnitsForm").submit();
            }
        }
    </script>
</body>
