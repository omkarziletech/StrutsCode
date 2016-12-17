<%-- 
    Document   : correctionChargeDesc
    Created on : 22 Jan, 2016, 8:29:15 AM
    Author     : PALRAJ
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/tooltip/tooltip.js" ></cong:javascript>
<c:if test="${not empty lclCorrectionChargesList}">
    <display:table class="dataTable" name="${lclCorrectionChargesList}" id="chargesTable">
        <display:column title="Charge Code" style="width:100Px;">${chargesTable.chargeCode}</display:column>
        <display:column title="Charge Desc" style="width:200Px;">${chargesTable.chargeDescriptions}</display:column>
        <display:column title="Bill To Party" style="width:200Px;">${chargesTable.billToPartyLabel}</display:column>
        <display:column title="Amount" style="width:100Px;">${chargesTable.oldAmount}</display:column>
        <display:column title="New Amount" style="width:100Px;">${chargesTable.newAmount}</display:column>
        <display:column title="Difference" style="width:100Px;">${chargesTable.differenceAmount}</display:column>
        <display:column title="Action" style="width:80Px;">
            <c:if test="${lclCorrectionForm.viewMode!='view'}">
                <img src="${path}/images/edit.png"  style="cursor:pointer" class="correctionCharge" width="13" height="13" alt="edit"
                     onclick="editCorrectionCharge('${path}', '${chargesTable.chargeId}', '${chargesTable.chargeCode}', '${chargesTable.chargeDescriptions}',
                                         '${chargesTable.oldAmount}', '${chargesTable.newAmount}', '${chargesTable.differenceAmount}', '${correctionId}',
                                         '${chargesTable.billToPartyLabel}', '${lclCorrectionForm.selectedMenu}', '${chargesTable.correctionChargeId}', '${chargesTable.lclBookingAcId}');"
                     />
            </c:if>
            <c:if test="${chargesTable.delete && lclCorrectionForm.viewMode!='view'}">
                <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                     onclick="deleteCorrectionCharge('${chargesTable.correctionChargeId}');" />
            </c:if>
        </display:column>
    </display:table>
</c:if>