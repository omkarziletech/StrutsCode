/*
 *  Document   : voyage
 *  Author     : Rajesh
 */
$(document).ready(function(){
    $("[title != '']").not("link").tooltip();
});

function setUnitId(unitId, dispoCode) {
    $("#unitId").val(unitId);
    $("#dispoCode").val(dispoCode);
}

function doLink() {
    var $selectedVoy = $('input:radio[name=selectVoy]:checked').val();
    var $form = $("#voyageForm");
    var fileNumber = $("#fileNumber").val();
    var dispoCode = $("#dispoCode").val();
    $("#navigation").val("doLink");
    if (checkUnitLinkChargeAndCostMappingWithGL(fileNumber, dispoCode)) {
        if ($selectedVoy != undefined && $selectedVoy != "") {
            $("#voyageId").val($selectedVoy);
            var params = $form.serialize();
            var url = $form.attr("action");
            showProgressBar();
            $.post(url, params, function (data) {
                window.parent.showLoading();
                parent.$("#methodName").val("editBooking");
                parent.$("#lclBookingForm").submit();
                parent.$("#link-voyage").hide();
                parent.$.fn.colorbox.close();
                hideProgressBar();
            });
        } else {
            $.prompt("Please select a voyage before link!");
        }
    }

    function checkUnitLinkChargeAndCostMappingWithGL(fileNumber, dispoCode) {
        var flag = true;
        if (dispoCode === 'PORT') {
            $.ajaxx({
                dataType: "json",
                data: {
                     className: "com.gp.cong.lcl.dwr.LclDwr",
                     methodName: "checkUnitLinkChargeAndCostMappingWithGL",
                     param1: fileNumber,
                     dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data !== "") {
                        $.prompt("No gl account is mapped with these charge code.Please contact accounting - <span style=color:red>" + data + "</span>.");
                        flag = false;
                    }
                }
            });
        }
        return flag;
    }
}