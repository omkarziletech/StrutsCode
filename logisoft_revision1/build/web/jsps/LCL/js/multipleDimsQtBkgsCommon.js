/* 
 *  Document   : multipleDimsQtBkgsCommon
 *  Created on : Oct 20, 2016, 2:46:48 PM
 *  Author     : NambuRajasekar
 */
// This page is Common for Quote/Booking Dims
$(document).ready(function () {
    addInbox();
    var uom = $('#uom').val();
    if (uom == 'M') {
        $('#actualUomM').attr('checked', true);
        $('#actualM').show();
        $('#actualI').hide();
    }
    if (uom == 'I' || uom === "") {
        $("#actualUomI").attr('checked', true);
        $('#actualM').hide();
        $('#actualI').show();
    }
    if ($("#actualUomI").is(":checked")) {
        changeUom();
    }
    if ($("#actualUomM").is(":checked")) {
        changeUom();
    }
});

function saveMultiple() {
    var error = false;
    var post = false;
    var index1stFlag = 0;// it's initalize a 1st row Index(always empty value)

    parent.$("#uom").val($('input:radio[name=actualUom]:checked').val());
    $(".mandatory").each(function () {
        if (index1stFlag > 3 && $(this).val().length === 0) {
            error = false;
            $(this).css("border-color", "red");
            $(this).focus();
            $.prompt('This field is required');
            return false;
        } else {
            index1stFlag++;
            error = true;
        }
    });

    if (error) {
        var indexstFlag = 0;
        var lengthDims = [];
        var acWidthDims = [];
        var heightDims = [];
        var piecesDims = [];
        var weightDims = [];
        var warehouseTabDims = [];

        $(".pieces").each(function () {
            if (indexstFlag > 0 && $(this).val().length !== 0) {
                var row = $(this).parent().parent();
                post = true;
                lengthDims.push(row.find("td .acLength").val());
                acWidthDims.push(row.find("td .acWidth").val());
                heightDims.push(row.find("td .acHeight").val());
                piecesDims.push($(this).val());
                weightDims.push(row.find("td .acWeight").val());
                warehouseTabDims.push(row.find("td .warehouseTab").val());

            } else {
                indexstFlag++;
            }
        });
    }
    if (post) {
        parent.$("#lengthDims").val(lengthDims);
        parent.$("#acWidthDims").val(acWidthDims);
        parent.$("#heightDims").val(heightDims);
        parent.$("#piecesDims").val(piecesDims);
        parent.$("#weightDims").val(weightDims);
        parent.$("#warehouseTabDims").val(warehouseTabDims);
        parent.$("#uom").val($('input:radio[name=actualUom]:checked').val());

    }
    if (post) {
        var uomAc = $('input:radio[name=actualUom]:checked').val();
        parent.selectedMenultiDims(uomAc); //common for Quote/Booking
        parent.$.fn.colorbox.close();
    }
}
function moveToNextInbox(event, note) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        if ($(note).parent().parent().prevAll().length === $("#inboxTable tr").length - 1) {
            addInbox();
        }
        event.preventDefault();
        $(note).parent().parent().next().find(".acLength").select().fadeIn().fadeOut().fadeIn();
    }
}

function addInbox() {
    var rowIndex = $("#inboxTable tr").length;
    var row = $(".defaultPP").clone().removeClass("defaultPP").show();
    row.find(".removePP").click(function () {
        $(this).parent().parent().remove();
    });
    row.find(".warehouseTab").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToNextInbox(event, this);
        }
    });
    row.appendTo("#inboxTable");
    row.find("#actualLength" + rowIndex).focus();
    if (rowIndex === 1) {
        row.find("td .acLength").focus();
    }
}
$(".removePP").click(function () {
    $(this).parent().parent().remove();
});



function changeUom() {
    var uom = $('#uom').val();
    var parentRowLength = $("#parentRowLen").val();
    if (uom == 'M' && parentRowLength > 1) {
        $('#actualUomI').attr('disabled', true);
    }
    if (uom == 'I' && parentRowLength > 1) {
        $('#actualUomM').attr('disabled', true);
    }
}
function showValue() {
    if ($("#actualUomM").is(":checked")) {
        $('#actualM').show();
        $('#actualI').hide();
    }
    if ($("#actualUomI").is(":checked")) {
        $('#actualI').show();
        $('#actualM').hide();
    }
}
function showHideMeasureLabel(obj) {
    var val = obj.value;
    if (val == 'M') {
        $('#uom').val(val);
    }
    if (val == 'I') {
        $('#uom').val(val);
    }
}
function checkForNumberAndDecimal(obj) {
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");

    }
}
function checkForNumber(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode
    if ((charCode < 48 || charCode > 57) && ((charCode < 95 || charCode > 105))) {
        if (charCode != 9 && charCode != 8) {
            evt.value = "";
            $.prompt("This field should be Numeric");
        }
    }
}
