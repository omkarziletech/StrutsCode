<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm">
    <cong:table width="100%">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="4">
                <cong:div styleClass="floatLeft">Change Voyage:
                    <span style="color:red">${lclAddVoyageForm.scheduleNo}</span>
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table align="center" style="font-size: 14px" width="80%">
        <cong:hidden id="methodName" name="methodName"/>
        <cong:hidden id="scheduleNo" name="scheduleNo"/>
        <cong:hidden id="unitssId" name="unitssId"/>
        <cong:hidden id="headerId" name="headerId"/>
        <cong:hidden id="unitId" name="unitId"/>
        <cong:hidden id="unitNo" name="unitNo"/>
        <cong:hidden id="originalOriginId" name="originalOriginId"/>
        <cong:hidden id="originalDestinationId" name="originalDestinationId"/>
        <cong:hidden id="schServiceType" name="schServiceType" value="${lclAddVoyageForm.schServiceType}"/>
        <cong:tr><cong:td>
                <cong:table  border="0" width="100%">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Voyage No#</cong:td>
                        <cong:td>
                            <cong:autocompletor name="schedule" id="schedule" template="one" paramFields="originalOriginId,originalDestinationId,scheduleNo,schServiceType"
                                                position="right" scrollHeight="100px" query="LCL_SS_VOYAGE" shouldMatch="true"
                                                fields="changeVoyageNo,changeVoyHeaderId"
                                                width="128" container="NULL" styleClass="mandatory textlabelsLclBoldForTextBox"/>
                            <cong:hidden name="changeVoyageNo" id="changeVoyageNo"/>
                            <cong:hidden name="changeVoyHeaderId" id="changeVoyHeaderId"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td></cong:td>
                        <cong:td>
                            <input type="button" class="button-style3" value="Change" onclick="changeVoyageForUnit()"/>
                            <input type="button" class="button-style3" value="Cancel" onclick="closePopupBox()"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
        </cong:tr>
    </cong:table>
</cong:form>
<script type="text/javascript">
    function closePopupBox(){
        parent.$.fn.colorbox.close();
    }
    function changeVoyageForUnit(){
        var scheduleNo=$('#changeVoyageNo').val()
        if(unitNumberExist()){
        if(scheduleNo===null || scheduleNo===''){
            $.prompt("VoyageNo is Required");
            return false;
        }
        parent.$("#changeVoyHeaderId").val($("#changeVoyHeaderId").val());
        parent.$("#changeVoyageNo").val(scheduleNo);

        parent.document.getElementById("headerId").value = document.getElementById("headerId").value;
        parent.document.getElementById("unitssId").value = document.getElementById("unitssId").value;
        parent.document.getElementById("unitId").value = document.getElementById("unitId").value;
        showProgressBar();
        parent.$("#methodName").val('changeVoyage');
        parent.$("#lclAddVoyageForm").submit();
        }
    }
    function unitNumberExist() {
       var scheduleNo=$('#changeVoyageNo').val()
        var unitNumber = $("#unitNo").val();
        var unitId= parent.$("#unitId").val();
        var flag =true;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkUnitNumberExists",
                param1: $('#changeVoyHeaderId').val(),
                param2: unitNumber,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if(data[0] == "1") {
                $.prompt("Unit Number  <span style=color:red>" + unitNumber + " </span> Already Exists in the Voyage#" + "<span style=color:red>" + scheduleNo);
                $("#schedule").val('');
                flag =false;
                }
               
            }
        });
        return flag;
    }

</script>
