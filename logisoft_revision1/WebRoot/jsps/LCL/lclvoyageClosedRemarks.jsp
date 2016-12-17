<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:form action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm">
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="4">
                <cong:div styleClass="floatLeft">${status} Remarks &nbsp;&nbsp;
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table align="center" style="font-size: 14px" width="80%">
        <cong:hidden id="methodName" name="methodName"/>
        <cong:hidden id="headerId" name="headerId" value="${headerId}"/>
        <cong:hidden id="unitId" name="unitId" value="${unitId}"/>
        <cong:tr><cong:td>
                <cong:table  border="0" width="100%">

                    <cong:tr>
                        <cong:td>
                            <cong:textarea styleClass="refusedTextarea" id="remarks" name="remarks"></cong:textarea>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td align="center">
                            <input type="button" class="button-style3" value="Ok" onclick="updateVoyageRemarks('${status}','${headerId}','${unitId}')"/>
                            <input type="button" class="button-style3" value="Cancel" onclick="closePopupBox()"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td></cong:tr>

    </cong:table>
</cong:form>
<script>
    function closePopupBox()
    {
        parent.$.fn.colorbox.close();
    }
    function updateVoyageRemarks(status,headerId,unitId){
        showProgressBar();
        var remarks = document.getElementById("remarks").value;
        var headerId = parent.document.getElementById("headerId").value;
        parent.document.getElementById("buttonValue").value = status;
        parent.document.getElementById("headerId").value = headerId;
        parent.document.getElementById("unitId").value = unitId;
        parent.document.getElementById("closedRemarks").value = remarks;
        parent.document.getElementById('reopenedRemarks').value = remarks;
        parent.document.getElementById("methodName").value = "updateClosedAuditedRemarks";
        parent.$("#lclAddVoyageForm").submit();
    }
    function congAlert(txt){
        $.prompt(txt);
    }

</script>
