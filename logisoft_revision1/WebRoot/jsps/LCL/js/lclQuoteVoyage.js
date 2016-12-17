/*
 *  Document   : lclQuoteVoyage
 *  Author     : Rajesh
 */
function validateConvertToBkg(path, fileNumberId, moduleId) {
    var ssHeaderId = $('#ssHeaderId').val();
    var originLrd = $('#originLrdId').val();
    var fdEtaDate = $('#fdEtaDateId').val();
 //   var unknownDest = $('#unknownDest').val();
//    if ((ssHeaderId === null || ssHeaderId === '' || ssHeaderId === undefined)
//        && unknownDest === 'false') {
//        $.prompt("Please select voyage");
//        return false;
//    } else {
        doConvert(path, fileNumberId, moduleId, ssHeaderId, originLrd, fdEtaDate);
   // }
}

function doConvert(path, fileNumberId, moduleId, ssHeaderId, originLrd, fdEtaDate) {
    showProgressBar();
    var url = path + "/lclQuote.do?methodName=convertToBkg&fileNumberId="
    + fileNumberId + "&bookedSsHeaderId=" + ssHeaderId + "&originLrdDate=" + originLrd + "&fdEtaDate=" + fdEtaDate;
    $.post(url, function () {
        window.parent.changeLclChilds(path, fileNumberId, moduleId, "Exports");
    });
}

//function chooseVoyage(path, fileNumberId) {
//    var voyage = $('input:radio[name=voyageRadio]:checked').val();
//    var url = path + "/lclQuote.do?methodName=convertToBooking&fileNumberId="
//    + fileNumberId + "&masterScheduleNo=" + voyage;
//    window.location = url;
//}

function cancelConvertToBooking(path, fileNumberId) {
    var url = path + "/lclQuote.do?methodName=editQuote&fileNumberId=" + fileNumberId+"&moduleId="+$("#fileNumber").val();
    window.location = url;
}

function showOlder(path, fileNumberId, fileNumber) {
    var showOlderVoyage = "false";
    if ($("#showOlder").prop("checked"))
        {
            showOlderVoyage = true;
        }
      var url = path + "/lclQuote.do?methodName=displayBookingVoyage&fileNumberId=" + fileNumberId
                + "&fileNumber=" + fileNumber + "&voyageAction=true&prevSailing="+showOlderVoyage;
        window.location = url;
}

function setVoyage(ssHeaderId, originLrd, fdEtaDate) {
    $('#ssHeaderId').val(ssHeaderId);
    $('#originLrdId').val(originLrd);
    $('#fdEtaDateId').val(fdEtaDate);
}