<%-- 
    Document   : adjustmentChargeBkgBlDiv
    Created on : Nov 3, 2016, 4:52:48 PM
    Author     : NambuRajasekar
    This is common for Booking/Bl adjustment Charges
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <table class="table" style="margin: 2px;width: 598px;">
        <tr>
            <th>
        <div class="float-left">
            <label id="headingAdjustmentComments"></label>
        </div>
    </th>
</tr>
<tr>
    <td></td>
</tr>
<tr>
<input type="hidden" name="chargeIdAdj" id="chargeIdAdj" /><%-- chargeId --%>
<input type="hidden" name="oldAdjusmentVal" id="oldAdjusmentVal" /> <%-- oldAdjustmentValue --%>
<td class="label">
    <textarea id="adjustmentcomments" name="adjustmentcomments" cols="85" rows="5" class="textBoldforlcl"
              style="resize:none;text-transform: uppercase"></textarea>
</td>
</tr>
<tr>
    <td align="center">
        <input type="button"  value="Save" id="saveComments"
               align="center" class="button" onclick="addAdjustmentComments();"/>
        <input type="button"  value="Cancel" id="cancelHotCode"
               align="center" class="button" onclick="cancelAdjustmentComments();"/>
    </td>
</tr>
</table>
<script type="text/javascript">
    function calculateAdjustmentAmount(chargeId, oldAdjusmentVal) {
        showAlternateMask();
        $("#add-Comments-container").center().show(500, function () {
            $('#headingAdjustmentComments').text('Adjustment Comments:');
            $('#adjustmentcomments').val('');
            $('#chargeIdAdj').val(chargeId);
            $('#oldAdjusmentVal').val(oldAdjusmentVal);
        });
    }
    function addAdjustmentComments() {
        var comments = $('#adjustmentcomments').val();
        if (comments.trim() === "") {
            $.prompt('Please Enter Adjustment Comments');
            return false;
        }
        modifyAdjustmentAmount(comments);
        $("#add-Comments-container").center().hide(500, function () {
            hideAlternateMask();
        });
    }
    function cancelAdjustmentComments() {
        $("#add-Comments-container").center().hide(500, function () {
            var chargeIdAdj = $('#chargeIdAdj').val();
            $('#adjustmentAmount' + chargeIdAdj).val($('#oldAdjusmentVal').val());
            $('#chargeIdAdj').val('');
            $('#newAdjusmentVal').val('');
            $('#oldAdjusmentVal').val('');
            hideAlternateMask();
        });
    }
</script>

</html>
