<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib prefix="fmt" uri="/WEB-INF/fmt-1_0-rt.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="status" value="${param.status}"></c:set>
<c:set var="totalCharge" value="0.000"></c:set>

    <div style="height: 170px;" class="result-container">
        <table width="100%" border="0" class="display-table">
            <thead>
                <tr>
                    <th>File#</th>
                    <th>Charge Code</th>
                    <th>Description</th>
                    <th>Amount</th>
                    <th>GL Account</th>
                    <th>Shipment Type</th>
                    <th>Action</th>
                </tr>
            </thead>
        <c:forEach items="${invoiceChargeList}" var="arInvoiceCharge">
            <c:choose>
                <c:when test="${zebra eq 'odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <tr class="${zebra}">
                <c:set var="totalCharge" value="${totalCharge+arInvoiceCharge.amount}"></c:set>
                <td>
                   ${arInvoiceCharge.blDrNumber}
                </td>
                <td>${arInvoiceCharge.chargeCode}</td>
                <td><div style="width:300px;white-space: normal">${fn:toUpperCase(arInvoiceCharge.description)}</div></td>
                <td><fmt:formatNumber minFractionDigits="2"> ${arInvoiceCharge.amount}</fmt:formatNumber></td>
                <td>${arInvoiceCharge.glAccount}</td>
                <td>${arInvoiceCharge.shipmentType}</td>
                <td>
                    <c:if test="${status ne 'AR' && empty arInvoiceCharge.lclDrNumber}">
                        <img src="${path}/images/error.png" alt="delete" height="12px" title="Delete Charge" width="12px"  style="cursor: pointer"
                             onclick="deleteInvoiceCharge('deleteInvoiceCharge', '#lclArInvoiceForm', '${arInvoiceCharge.id}', '#redInvoiceCharge');"/>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <tfoot>
            <tr>
                <td colspan="3" class="align-right">Total :&nbsp;</td>
                <td colspan="4" class="align-left"><b id="greenBold"><fmt:formatNumber minFractionDigits="2">${totalCharge}</fmt:formatNumber></b></td>
            </tr>
        </tfoot>
    </table>
</div>
<script type="text/javascript">
                                 function deleteInvoiceCharge(methodName, formName, invoiceChargeId, selector) {
                                     $.prompt('Are you sure you want to delete?', {
                                         buttons: {
                                             Yes: 1,
                                             No: 2
                                         },
                                         submit: function(v) {
                                             if (v == 1) {
                                                 showProgressBar();
                                                 $("#methodName").val(methodName);
                                                 var params = $(formName).serialize();
                                                 params += "&invoiceChargeId=" + invoiceChargeId;
                                                 $.post($(formName).attr("action"), params,
                                                         function(data) {
                                                             $(selector).html(data);
                                                             $(selector, window.parent.document).html(data);
                                                             $("#addLineItem").hide();
                                                             hideProgressBar();
                                                         });
                                                 $.prompt.close();
                                             }
                                             else if (v == 2) {
                                                 $.prompt.close();
                                             }
                                         }
                                     });
                                 }
</script>