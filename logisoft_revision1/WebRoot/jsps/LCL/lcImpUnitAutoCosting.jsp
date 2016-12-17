<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<body style="background:#ffffff">
    <cong:form id="lclAddVoyageForm" name="lclAddVoyageForm" action="/lclImportAddVoyage">
        <cong:hidden id="methodName" name="methodName"/>
        <cong:hidden id="headerId" name="headerId" value="${lclAddVoyageForm.headerId}" />
        <cong:hidden id="voyageNo" name="voyageNo" value="${lclAddVoyageForm.voyageNo}" />
        <cong:hidden id="unitId" name="unitId"  />
        <cong:hidden id="unitssId" name="unitssId"  />
        <cong:hidden id="unitNo" name="unitNo"  />
        <cong:table width="100%">
            <cong:tr styleClass="tableHeadingNew">
                <cong:td >Auto Cost</cong:td>
                <cong:td>Unit No:<span style="color:red">${lclAddVoyageForm.unitNo}</span></cong:td>
                <cong:td>
                    <c:choose>
                        <c:when test='${greenFlag eq true}'>
                            <input type="button"  id="addCost" class="green-background" value="Actual Costs/Accruals"/>
                        </c:when>
                        <c:otherwise>
                            <input type="button"  id="addCost" class="button-style1" value="Actual Costs/Accruals" onclick="createAccruals();"/>
                        </c:otherwise>
                    </c:choose>
                </cong:td> </cong:tr>
            <cong:tr>
                <cong:td colspan="3">
                    <c:if test="${not empty autoCostingList}">
                        <display:table class="dataTable" name="${autoCostingList}" id="costValue">
                            <display:column title="Charge Code" style="width:100Px;">${costValue.arGlMappingId.chargeCode}</display:column>
                            <display:column title="Charge Desc" style="width:200Px;">${costValue.arGlMappingId.chargeDescriptions}</display:column>
                            <display:column title="Charge Amount" style="width:100Px;">${costValue.apAmount}</display:column>
                            <display:column title="Vendor" style="width:100Px;">${costValue.apAcctNo.accountno}/${costValue.apAcctNo.accountName}</display:column>
                        </display:table>
                    </c:if>
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:form>
</body>
<script type="text/javascript">
    function createAccruals(){
        showProgressBar();
        $("#methodName").val('createAccrualsByAutoCost');
        $("#lclAddVoyageForm").submit();
    }
</script>
