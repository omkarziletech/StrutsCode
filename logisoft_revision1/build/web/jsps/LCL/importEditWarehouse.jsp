<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<body style="background:#ffffff">
    <cong:form name="lclEditWarehouseForm" id="lclEditWarehouseForm" action="/lclImportEditWarehouse.do">
        <cong:table width="97%" style="border:1px solid #dcdcdc">
            <cong:tr styleClass="tableHeadingNew"><cong:td colspan="4">
                    Edit Warehouse#    <span class="fileNo" style="color:#0000FF">${warehouseNumber}</span>
                </cong:td> </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="20%">Warehouse Name</cong:td>
                <cong:td styleClass="textlabelsBoldleftforlcl">
                    <cong:autocompletor name="warehouseName" id="warehouseName" width="400" scrollHeight="200px"  template="three" shouldMatch="true"
                                        styleClass="mandatory" container="NULL" query="WAREHOUSE" fields="NULL,NULL,warehouseId"/>
                    <cong:hidden name="warehouseId" id="warehouseId" />
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Location</cong:td>
                <cong:td>
                    <cong:text id="location" name="location"  styleClass="textuppercaseLetter"/>
                </cong:td></cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Arrived DateTime</cong:td>
                <cong:td styleClass="textlabelsBoldleftforlcl">
                    <cong:calendarNew showTime="" id="arrivedDateTime" name="arrivedDateTime" value=""/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl" width="10%">Departed DateTime</cong:td>
                <cong:td styleClass="textlabelsBoldleftforlcl">
                    <cong:calendarNew showTime="" id="departedDateTime" name="departedDateTime" value=""/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Seal In</cong:td>
                <cong:td>
                    <cong:text id="sealNoIn" name="sealNoIn" styleClass="textuppercaseLetter" />
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Seal Out</cong:td>
                <cong:td>
                    <cong:text id="sealNoOut" name="sealNoOut" styleClass="textuppercaseLetter"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Stripped Date</cong:td>
                <cong:td>
                    <cong:calendarNew id="strippedDate" name="strippedDate"/>
                </cong:td>
                <cong:td colspan="2"></cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  colspan="2"></cong:td>
                <cong:td colspan="2">
                    <input type="button" class="button-style1" value="Save" id="saveCode" onclick="saveWarehouse()"/>
                </cong:td>
            </cong:tr>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="unitWarehouseId" id="unitWarehouseId" />
        </cong:table>
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
                parent.document.getElementById("sealNoIn").value = document.getElementById("sealNoIn").value;
                parent.document.getElementById("sealNoOut").value = document.getElementById("sealNoOut").value;
                parent.document.getElementById("strippedDate").value = document.getElementById("strippedDate").value;
                parent.$("#methodName").val('addWareHouse');
                parent.$("#lclAddUnitsForm").submit();
            }
        }
    </script>
</body>
