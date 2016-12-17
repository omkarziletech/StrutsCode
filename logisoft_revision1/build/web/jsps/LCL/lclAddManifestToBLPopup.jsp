<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclUnitsSchedule.js"></cong:javascript>
<cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="unitId" name="unitId"  />
    <cong:hidden id="unitssId" name="unitssId"  />
    <cong:hidden id="unitNo" name="unitNo"  />
    <cong:hidden id="filterByChanges" name="filterByChanges" />
    <cong:hidden id="scheduleNo" name="scheduleNo" />
    <cong:hidden id="polId" name="polId" />
    <cong:hidden id="podId" name="podId" />
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="20%">
                Add B/L To Manifest
            </td>
        </tr>
    </table>
    <br/>
    <cong:table width="100%" align="center">
        <cong:tr>
            <cong:td width="30%"></cong:td>
            <cong:td styleClass="style2" align="right">D/R#</cong:td>
            <cong:td>
                <cong:text name="fileNumber" id="fileNumber" styleClass="mandatory textuppercaseLetter" />
            </cong:td>
        </cong:tr>
    </cong:table>
    <br/>
    <cong:table width="100%" border="0" align="center">
        <cong:tr>
            <cong:td width="45%"></cong:td>
            <cong:td colspan="6" align="center">
                <input type="button" value="Submit" align="center" class="button-style1" onclick="showBLStatus();"/>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
    </cong:table>
</cong:form>
