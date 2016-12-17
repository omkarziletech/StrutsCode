<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:form  action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="headerId" name="headerId" />
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="4">
                <cong:div styleClass="floatLeft">Edit Header</cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
            <cong:table width="90%">
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl"  width="10%">Voyage#</cong:td>
            <cong:td valign="top" width="5%">
                <cong:text name="scheduleNo" id="scheduleNo" readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook"/>
            </cong:td>
            <cong:td rowspan="2"></cong:td>
            <cong:td rowspan="2"></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl"  width="10%" colspan="2">Remarks</cong:td>
            <cong:td width="10%" rowspan="2">
                <cong:textarea cols="5" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox " id="remarks"
                               name="remarks" value="${lclAddVoyageForm.remarks}" >
                </cong:textarea>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Billing Terminal</cong:td>
            <cong:td><cong:autocompletor name="billTerminal" id="billTerminal" width="400" scrollHeight="150px" container="NULL"
                      query="IMPORTTERMINAL" fields="NULL,billTerminalNo" template="three" shouldMatch="true"/>
                   <cong:hidden name="billTerminalNo" id="billTerminalNo" />
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
            </cong:table>
             <cong:table width="80%">
        <cong:tr>
            <cong:td styleClass="style2" align="right" width="10%"></cong:td>
            <cong:td width="5%">

            </cong:td>
            <cong:td styleClass="style2" align="right" width="10%"></cong:td>

            <cong:td width="10%">
                <div class="button-style1" onclick="saveVoyageHeader();">Save Voyage Header</div>
            </cong:td>
            <cong:td styleClass="style2" align="right" width="5%"></cong:td>
            <cong:td width="10%">
            </cong:td>
        </cong:tr>
    </cong:table>
</cong:form>
<script type="text/javascript">
    function saveVoyageHeader(){
        parent.document.getElementById("remarks").value = document.getElementById("remarks").value;
        parent.document.getElementById("billTerminalNo").value = document.getElementById("billTerminalNo").value;
        parent.$("#methodName").val('saveVoyageHeader');
        parent.$("#lclAddVoyageForm").submit();
    }
</script>