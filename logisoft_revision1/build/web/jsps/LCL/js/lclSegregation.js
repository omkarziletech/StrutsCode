function correctBookedWeightMetric(obj) {
    if (obj.value > 20000) {
        $.prompt('Are you sure you have the correct amount?', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    $.prompt.close();
                    if (!isNaN(parseFloat($("#kgs").val()) * 2.2046)) {
                        $('#lbs').val((parseFloat($("#kgs").val()) * 2.2046).toFixed(3));
                    }
                }
                else if (v == 2) {
                    $("#kgs").val("");
                    $(this).focus();
                    $.prompt.close();
                }
            }
        });
    } else {
        $.prompt.close();
        if (!isNaN(parseFloat($("#kgs").val()) * 2.2046)) {
            $('#lbs').val((parseFloat($("#kgs").val()) * 2.2046).toFixed(3));
        }
    }
}

function correctBookedVolumeMetric(obj) {
    if (obj.value > 50) {
        $.prompt('Are you sure you have the correct amount?', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    if (!isNaN(parseFloat($("#cbm").val()) * 35.3146)) {
                        $('#cft').val((parseFloat($("#cbm").val()) * 35.314).toFixed(3));
                    }
                    $.prompt.close();
                }
                else if (v == 2) {
                    $("#cbm").val("");
                    $("#cbm").css("border-color", "red");
                    $(this).focus();
                    $.prompt.close();
                }
            }
        });
    } else {
        if (!isNaN(parseFloat($("#cbm").val()) * 35.3146)) {
            $('#cft').val((parseFloat($("#cbm").val()) * 35.314).toFixed(3));
        }
        $.prompt.close();
    }
}

function abortSegregation() {
    parent.$.fn.colorbox.close();
}

function createSegregationDr() {
    var destination = $('#segDest').val();
    var destUnLocCode = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
    var destCode = destUnLocCode.substring(0, 2);
    var cbm = $('#cbm').val();
    var kgs = $('#kgs').val();
    var transshipment = $('input:radio[name="transshipment"]:checked').val();
    if (destination === null || destination === '') {
        sampleAlert("Please select Final Destination");
    } else if (cbm === null || cbm === '') {
        sampleAlert("Please enter the CBM value");
    } else if (kgs === null || kgs === '') {
        sampleAlert("Please enter the KGS value");
    } else if (transshipment === null || transshipment === '') {
        sampleAlert("Please select TransShipment");
    } else if (transshipment === 'Y' && destCode === 'US') {
        sampleAlert("Final destination should not be in USA, Please change the Final destination");
        $("#segDest").css("border-color", "red");
    } else {
        $("#fileId").val(parent.$("#fileNumberId").val()).val();
        $("#methodName").val("createSegregationDr");
        var params = $("#lclSegregationForm").serialize();
        var url = $("#lclSegregationForm").attr("action");
        showProgressBar();
        $.post(url, params, function (data) {
            showProgressBar();
            parent.$("#methodName").val("editBooking");
            parent.$("#lclBookingForm").submit();
            parent.$.fn.colorbox.close();
            hideProgressBar();
        });
    }
}

function changeFdDojo() {
    var transshipment = $('input:radio[name="transshipment"]:checked').val();
    if (transshipment === 'Y') {
        $('#segDest').attr('alt', 'DEST_WITHOUT_US_COUNTRY');
    } else {
        $('#segDest').attr('alt', 'DEST_UNLOC');
    }
}