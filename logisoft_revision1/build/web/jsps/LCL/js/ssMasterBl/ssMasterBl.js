
var documentMasterId = "";
function resetAll() {
    var form = document.getElementById('ssMasterBlForm');
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "text" || element.type === "hidden") {
            element.value = "";
        }
    }
    $("#masterDisputeResult").closest('tr').remove();
}

function search() {
    $("#methodName").val("search");
    $('#ssMasterBlForm').submit();
}

function showDocuments(url) {
    window.parent.showLightBox('Report', url, 300, 900);
}

function acknowledge(masterId, path) {
    $.prompt("Are you sure you want to take ownership of this disputed file?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $("#methodName").val("acknowledge");
                var params = $('#ssMasterBlForm').serialize();
                params += "&documentMasterId=" + masterId;
                $.ajax({
                    type: "POST",
                    url: path + "/lcl/ssMasterDisputedBl.do",
                    data: params,
                    async: false,
                    success: function (data) {
                        updateAcknowledge(path, data, masterId);
                    }
                });
            }
        }
    });
}

function updateAcknowledge(path, data, masterId) {
    $("#acknowledge" + masterId).data('tipText', data);
    $("#acknowledge" + masterId).removeAttr("onclick");
    $("#acknowledge" + masterId).attr("src", path + "/images/icons/dots/dark-green.gif");
}
function openNotes(path, headerId, voyageNo) {
    var href = path + "/lclSSHeaderRemarks.do?methodName=displayNotes&headerId=" + headerId + "&actions=show All&voyageNumber=" + voyageNo;
    $.colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "370px",
        title: "Remarks"
    });
}

function  goToVoyageFromDispute(path,headerId,filter){
   var toScreenName= "EXP VOYAGE";
   var fromScreen = "DISPUTED_MASTER_BL";
   var filterByChange = filter === "I" ? 'lclDomestic' : filter === "E" ? "lclExport" : filter === "C"  ?  "lclCfcl" : "";
   window.parent.goToVoyageFromDisputed(path,headerId,filterByChange,fromScreen,toScreenName);
}