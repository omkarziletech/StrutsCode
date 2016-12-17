<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <table width="100%">
            <tr class="tableHeadingNew">
                <td colspan="4">Unit# :<span class="red">${costChargeForm.unitNo}</span></td>
            <tr>
                <td>Ret OFT :<span class="red">${0.0 ne model.blTotalRetailOFT ? model.blTotalRetailOFT :""}</span></td>
                <td>Ret COL :<span class="red">${0.0 ne model.blTotalRetailCollect ? model.blTotalRetailCollect :""}</span></td>
                <td>Ret CBM :<span class="red">${0.0 ne model.blTotalRetailCBM ? model.blTotalRetailCBM :""}</span></td>
                <td>Ret CFT :<span class="red">${0.0 ne model.blTotalRetailCFT ? model.blTotalRetailCFT :""}</span></td>
            </tr>
            <tr>
                <td>CTC OFT :<span class="red">${0.0 ne model.blTotalCTCOFT ? model.blTotalCTCOFT : ""}</span></td>
                <td>CTC COL :<span class="red">${0.0 ne model.blTotalCTCCollect ? model.blTotalCTCCollect :""}</span></td>
                <td>CTC CBM :<span class="red">${0.0 ne model.blTotalCTCCBM ? model.blTotalCTCCBM :""}</span></td>
                <td>CTC CFT :<span class="red">${0.0 ne model.blTotalCTCCFT ? model.blTotalCTCCFT :""}</span></td>
            </tr>
            <tr>
                <td>FTF OFT :<span class="red">${0.0 ne model.blTotalFTFOFT ? model.blTotalFTFOFT :""}</span></td>
                <td>FTF COL :<span class="red">${0.0 ne model.blTotalFTFCollect ? model.blTotalFTFCollect : ""}</span></td>
                <td>FTF CBM :<span class="red">${0.0 ne model.blTotalFTFCBM ? model.blTotalFTFCBM :""}</span></td>
                <td>FTF CFT :<span class="red">${0.0 ne model.blTotalFTFCFT ? model.blTotalFTFCFT : ""}</span></td>
            </tr>
        </table>
        <table class="dataTable">
            <thead>
            <th></th>
            <th width="12%">Charge Type</th>
            <th>Charge Code</th>
            <th>Blue Code</th>
            <th>FAE Cost</th>
        </thead>
        <tbody>
            <c:forEach items="${faeCostList}" var="faeCost">
                <c:set var="rowStyle" value="${rowStyle eq 'oddStyle' ? 'evenStyle' : 'oddStyle'}"/>
                <tr class="${rowStyle}">
            <input type="hidden" class="costAmount${faeCost.value.costACId}" 
                   value="${faeCost.value.costAmount}"/>
            <td><input type="checkbox" name="${faeCost.value.costACId}" 
                       id="${faeCost.value.costACId}" 
                       checked="checked" class="chargeCode"/></td>
            <td>${faeCost.value.chargeType}</td>
            <td>${faeCost.value.chargeCode}</td>
            <td>${faeCost.value.bluescreenCharge}</td>
            <td>${faeCost.value.costAmount}</td>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="5">&nbsp;</td>
    </tr>
</tbody>
</table>
<table>
    <tr>
        <td colspan="5">
            <input type="button" class="button-style1" value="Save Cost" id="saveFAE" 
                   onclick="saveFAE('${path}', '${model.headerId}', '${model.unitSs.id}', '${model.vendorNo}');"/>
            <input type="button" class="button-style1" value="Cancel" id="cancelFAE" onclick="closePopup();"/>
        </td>
    </tr>
</table>
</body>
<script type="text/javascript">
    var code = [];
    function saveFAE(path, headerId, unitSsId, vendor) {
        getCheckedChargeAndCost();
        if (vendor === '') {
            $.prompt("Please Enter Agent In Voyage");
            return false;
        } else if (code.length === 0) {
            $.prompt("Select atleast one cost.");
            return false;
        } else {
            showProgressBar();
            parent.$("#methodName").val("saveFAECost");
            parent.$("#unitSsId").val(unitSsId);
            parent.$("#headerId").val(headerId);
            parent.$("#thirdpartyaccountNo").val(vendor);
            var params = parent.$("#lclUnitCostChargeForm").serialize();
            params += "&chargeAndCost=" + code;
            $.post(parent.$("#lclUnitCostChargeForm").attr("action"), params, function (data) {
                code = "";
                parent.showGroupByDR('false');
                parent.$.colorbox().close();
                hideProgressBar();
            });
        }
    }

    function getCheckedChargeAndCost() {
        $(".chargeCode").each(function () {
            var id = $(this).attr("id");
            if ($("#" + id).is(":checked")) {
                code.push(id + "_" + $(".costAmount" + id).val());
            }
        });
    }
    function closePopup() {
        parent.$.colorbox.close();
    }
</script>
</html>
