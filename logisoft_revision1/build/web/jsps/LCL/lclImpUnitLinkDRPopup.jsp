<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<script type="text/javascript">
    function linkDrStatus(){
        if(document.getElementById("fileNumber").value==""){
            sampleAlert("File Number is required");
            $("#fileNumber").css("border-color","red");
            $("#warning").show();
        }else{
            if(isSpecial(document.getElementById("fileNumber").value)){
                $("#methodName").val('savelinkDR');
                $("#lclAddVoyageForm").submit();
                $("#submithide").hide();
            }else{
                sampleAlert("Special Characters Not Allowed in D/R#");
                $("#fileNumber").css("border-color","red");
                $("#warning").show();
            }
        }
    }
    function linkDrOk(){
        parent.$("#methodName").val('refreshUnitsPage');
        parent.$("#lclAddVoyageForm").submit();
    }
</script>
<cong:form  action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="unitId" name="unitId"  />
    <cong:hidden id="unitssId" name="unitssId"  />
    <cong:hidden id="unitNo" name="unitNo"  />
    <cong:hidden id="filterByChanges" name="filterByChanges" />
    <cong:hidden id="scheduleNo" name="scheduleNo" />
    <cong:hidden id="polId" name="polId" />
    <cong:hidden id="podId" name="podId" />
    <cong:hidden id="headerId" name="headerId" />
    <cong:hidden id="polUnlocationCode" name="polUnlocationCode" value="${lclAddVoyageForm.polUnlocationCode}"/>
    <cong:hidden id="fdUnlocationCode" name="fdUnlocationCode" value="${lclAddVoyageForm.fdUnlocationCode}"/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="20%">
                Link DR: <span style="color: red;">${lclAddVoyageForm.unitNo}</span>
            </td>
        </tr>
    </table>
    <br/>
    <cong:table width="100%" border="0">
        <c:if test="${not empty lclAddVoyageForm.message}">
            <cong:tr>
                <cong:td colspan="3" styleClass="greenFontBold" align="center" width="50%">${lclAddVoyageForm.message}</cong:td>
            </cong:tr>
        </c:if>
        <cong:tr>
            <cong:td width="10%"></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Enter D/R#</cong:td>
            <cong:td>
                <c:choose>
                    <c:when test="${lclAddVoyageForm.message eq 'D/R# Successfully Linked !'}">
                        <c:set var="cssStyle" value="textlabelsBoldForTextBoxDisabledLook"/>
                    </c:when>
                    <c:otherwise><c:set var="cssStyle" value="mandatory textuppercaseLetter"/></c:otherwise></c:choose>
                <cong:autocompletor id="fileNumber" name="fileNumber" template="one" query="DOCK_RECEIPT" styleClass="${cssStyle}"
                                    shouldMatch="true" width="130" container="NULL" params="${lclAddVoyageForm.polUnlocationCode},${lclAddVoyageForm.fdUnlocationCode}" scrollHeight="150"/>
            </cong:td>
        </cong:tr>
    </cong:table>
    <br/>
    <cong:table width="100%" border="0" align="center">
        <cong:tr>
            <cong:td width="45%"></cong:td>
            <cong:td align="center">
                <c:choose>
                    <c:when test="${lclAddVoyageForm.message eq 'D/R# Successfully Linked !'}">
                        <input type="button"  value="ok" align="center" class="button-style1" onclick="linkDrOk();"/>
                    </c:when>
                    <c:otherwise>
                        <input type="button" id="submithide" value="Submit" align="center" class="button-style1" onclick="linkDrStatus();"/>
                    </c:otherwise>
                </c:choose>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="2"></cong:td></cong:tr>
    </cong:table>
</cong:form>
