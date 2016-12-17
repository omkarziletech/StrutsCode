<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<script type="text/javascript">

</script>
<body style="background:#ffffff">
    <cong:form action="/lclImportAddUnits" name="lclAddUnitsForm" id="lclAddUnitsForm">
        <input type="hidden" id="methodName" name="methodName"/>
        <cong:hidden id="unitssId" name="unitssId"/>
        <cong:hidden id="unitId" name="unitId"/>
         <cong:table align="center" width="100%" border="0">
            <cong:tr>
                <cong:td width="30%" > </cong:td>
                <cong:td width="90%" >
                   Do you want to reverse accounting?
                </cong:td>
            </cong:tr>
        </cong:table>
                   <br>
        <cong:table align="center" width="100%" border="0">
            <cong:tr>
                <cong:td width="18%" > </cong:td>
                <cong:td width="50%" >
                    <input type="button" class="button-style1" value="Proceed and Reverse Accounting" id="proceedReverse" onclick="unmanifest('unmanifest');" />
                    <input type="button" class="button-style1" value="Cancel" id="abort" onclick="abortCurrentPopup();" />
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:form></body>
