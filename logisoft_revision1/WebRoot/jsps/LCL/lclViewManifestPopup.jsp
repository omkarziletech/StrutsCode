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
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="30%">
                View Manifest
            </td>
            <td width="5%"><span class="blackBold"> UNIT# </span></td>
            <td width="30%"><span class="greenBold14px">${lclAddVoyageForm.unitNo}</span></td>
            <td width="5%"><span class="blackBold"> VOYAGE# </span></td>
            <td width="30%"><span class="greenBold14px">${lclAddVoyageForm.scheduleNo}</span></td>
        </tr>
    </table>
    <br/>
     <div style="overflow: auto;height: 250px;width:1100px;" align="center">
        <table border="1" id="manifestDr" style="border-collapse: collapse; border: 1px solid #dcdcdc;width:100%">
            <tr class="tableHeading2">
                <td>File#</td>
                <td>BL#</td>
                <td>Forwarder</td>
                <td>Shipper</td>
                <td>Consignee</td>
                <td width="5%">Pieces</td>
                <td width="5%">Cuft</td>
                <td width="5%">Pounds</td>
                <td>Order#</td>
                <td>Receipt#</td>
                <td>Total Billed</td>
                <td>P/C/B</td>
            </tr>
            <c:forEach items="${drList}" var="manifestBean">
                <c:choose>
                    <c:when test="${zebra=='odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td>${manifestBean.fileNo}</td>
                    <td>${manifestBean.blNo}</td>
                    <td>${manifestBean.forwarderName}</td>
                    <td>${manifestBean.shipperName}</td>
                    <td>${manifestBean.consigneeName}</td>
                    <td>${manifestBean.totalPieceCount}</td>
                    <td>${manifestBean.totalVolumeImperial}</td>
                    <td>${manifestBean.totalWeightImperial}</td>
                    <td>${manifestBean.orderNo}</td>
                    <td>${manifestBean.receiptNo}</td>
                    <td>${manifestBean.totalBilledAmount}</td>
                    <td>${manifestBean.billingType}</td>
                </tr>
            </c:forEach>
        </table>
     </div>
</cong:form>