<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<cong:div id="lineItemList" style="width:100%;height:130px;overflow-y:scroll">
    <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%"> List of Line Items
        <cong:div styleClass="floatRight button-style1">Add New</cong:div></cong:div>
    <cong:table width="100%" border="1" style="border-collapse: collapse; border: 1px solid #dcdcdc">
        <cong:tr styleClass="tableHeading2">
            <cong:td>Customer Name</cong:td>
            <cong:td>Account #</cong:td>
            <cong:td>Customer Type</cong:td>
            <cong:td>Invoice #</cong:td>
            <cong:td>Invoice Amount</cong:td>
            <cong:td>Invoice Date</cong:td>
            <cong:td>Status</cong:td>
            <cong:td>Posted Date</cong:td>
            <cong:td>Action</cong:td>
        </cong:tr>
        <c:forEach items="${inbondList}" var="lclInbond">
            <c:choose>
                <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <cong:tr styleClass="${zebra}">
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td>
                    <img src="${path}/images/edit.png" alt="edit" style="cursor: pointer" onclick="editInbond('${lclInbond.inbondId}','${lclInbond.inbondNo}','${lclInbond.inbondType}','${inbondDate}','${lclInbond.inbondPort.unLocationName}')"/>
                    <img src="${path}/jsps/LCL/images/search_over.gif" alt="preview" width="20" height="20"
                                             onmouseover="tooltip.show('<strong>Preview</strong>');"onmouseout="tooltip.hide();"
                                             onclick="previewArInvoice('${lclArInvoiceForm.fileNumberId}','${lclArInvoiceForm.fileNumber}');"/>
                    <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px" onclick="deleteInbond('Are you sure you want to delete?');closeInbond('${lclInbond.inbondId}','${lclInbond.inbondNo}')" style="cursor: pointer"/>
                </cong:td>
            </cong:tr>
        </c:forEach>
    </cong:table>
</cong:div>