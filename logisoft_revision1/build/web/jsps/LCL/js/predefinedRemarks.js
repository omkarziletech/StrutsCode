/*
 *  Document   : predefinedRemarks
 */
function uncheckOther(element) {
    $('.predefinedRemarks').prop('checked', false);
    $(element).prop('checked', true);
}
function submitRemarks(idSelector) {
    var remarks = [];
    $('.predefinedRemarks').each(function() {
        if ($(this).is(':checked')) {
            remarks.push($(this).val());
        }
    });
    parent.$(idSelector).val(parent.$(idSelector).val()+remarks);
    parent.$.fn.colorbox.close();
}

