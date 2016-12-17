<%-- 
    Document   : adjustmentTotalChgBkgBlDiv
    Created on : Nov 4, 2016, 12:10:40 PM
    Author     : NambuRajasekar
    This is common for Booking/Bl TotalAdjustment Charges
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <table class="table" style="margin: 2px;width: 545px;">
        <tr>
            <th colspan="4">
        <div class="float-left">
            <label id="headingAdjustmensTotal"></label>
        </div>
    </th>
</tr>
<tr>
    <td colspan="4">&nbsp;</td>
</tr>
<tr>
<input type="hidden" name="chargeIdTotal" id="chargeIdTotal" /><%-- chargeId --%>
<input type="hidden" name="oldOriginalChargeVal" id="oldOriginalChargeVal" /> <%-- oldAdjustmentValue --%>
<td class="textlabelsBoldforlcl">Total Amount desired&nbsp;</td>

<td> <input type="text" id="adjustmentAmountBl" name="adjustmentAmountBl"  class="twoDigitDecFormat" style="width:76px;" onchange="allowOnlyWholeNumbers(this);"></td>
<td class="textlabelsBoldforlcl" >Comments &nbsp; </td>
<td class="label">
    <textarea id="adjustmentcommentsBl" name="adjustmentcommentsBl" cols="30" rows="3" class="textBoldforlcl" style="resize:none;text-transform: uppercase"></textarea>
</td>
</tr>
<tr>
    <td colspan="4">&nbsp;</td>
</tr>
<tr>
    <td colspan="4" align="center">
        <input type="button"  value="Save"  align="center" class="button" onclick="addAdjustmentTotal();"/>
        <input type="button"  value="Cancel"  align="center" class="button" onclick="cancelAdjustmentTotal();"/>
    </td>
</tr>
</table> 
<script type="text/javascript">
    function calculateAdjustmentTotal(chargeId, oldCharge) {
        showAlternateMask();
        $("#add-Comments-containerTotal").center().show(500, function () {
            $('#headingAdjustmensTotal').text('Adjustment:');
            $(".twoDigitDecFormat").val('').focus();
            $('#adjustmentcommentsBl').val('');
            $('#chargeIdTotal').val(chargeId);
            $('#oldOriginalChargeVal').val(oldCharge);
        });
    }
    function cancelAdjustmentTotal() {
        $("#add-Comments-containerTotal").center().hide(500, function () {
            $('#chargeIdTotal').val('');
            $('#oldOriginalChargeVal').val('');
            hideAlternateMask();
        });
    }
    function addAdjustmentTotal() {
        var amount = $('#adjustmentAmountBl').val();
        var comments = $('#adjustmentcommentsBl').val();
        if (amount === "") {
            $.prompt('Please Enter Adjustment Amount');
            $('#adjustmentAmountBl').css("border-color", "red");
            return false;
        } else if (comments === "") {
            $.prompt('Please Enter Adjustment Comments');
            $('#adjustmentcommentsBl').css("border-color", "red");
            return false;
        }
        submitAjaxFormForCharges(amount, comments);
        $("#add-Comments-containerTotal").center().hide(500, function () {
            hideAlternateMask();
        });
    }
</script>
</html>
