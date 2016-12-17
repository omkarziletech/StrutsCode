function addNotes() {
    $("#id").val('');
    $("#priorityView").attr('checked', false);
    $("#userMail").attr('checked', false);
    showBlock('#notesTable');
    $("#savNotes").css("display", "block");
    $("#updateNotes").css("display", "none");
    $("#followupDate").val('');
    $("#remarks").val('');
    $("#followupEmail").val('');
}
function deleteNotes(remarkId){
    $.prompt("Are you sure you want to delete?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showLoading();
                $("#id").val(remarkId);
                $('#methodName').val('closeNotes');
                $("#lclRemarksForm").submit();
                $.prompt.close();
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function editNote(id, remarks, followUpdate, followupEmail) {
    $("#id").val(id);
    $("#priorityView").attr('checked', false);
    $("#userMail").attr('checked', false);
    showBlock('#notesTable');
    $("#followupDate").val(followUpdate);
    $("#remarks").val(remarks);
    $("#followupEmail").val(followupEmail);
    followDateOnly();
    $("#savNotes").css("display", "none");
    $("#updateNotes").css("display", "block");
}
function tpEditNote(id, noteDescSub, noteTypeSub) {
    showBlock('#notesTable');
    $("#tpSave").hide();
    $("#tpUpdate").show();
    $("#tpId").val(id);
    $("#noteTypeSub").val(noteTypeSub);
    $("#noteDescSub").val(noteDescSub);
    document.getElementById("noteTypeSub").disabled=true;
}

function deleteTpNote(txt, id,noteDescSub) {
    var path = "/" + window.location.pathname.split('/')[1];
    $("#tpId").val(id);
    $("#noteDesc").val(noteDescSub);
    $('#methodName').val('deleteLclTpNote');
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                var params = $("#lclRemarksForm").serialize();
                $.ajaxx({
                    url: path + "/lclRemarks.do",
                    data: params,
                    preloading: true,
                    success: function (data) {
                        $("#actions").val("specialNotes");
                        $("#notesTable").hide();
                        $("#methodName").val("display");
                        $("#lclRemarksForm").submit();
                    }
                });
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function searchNotesType(methodName) {
    showLoading();
    var clntAcctNo = parent.$('#client_no').val();
    $('#clntAcctNo').val(clntAcctNo);
    var shpAcctNo = parent.$('#shipperCodeClient').val();
    $('#shpAcctNo').val(shpAcctNo);
    var frwdAcctNo = parent.$('#forwarderCodeClient').val();
    $('#frwdAcctNo').val(frwdAcctNo);
    var consAcctNo = parent.$('#consigneeCodeClient').val();
    $('#consAcctNo').val(consAcctNo);
    $("#methodName").val(methodName);
    //$("#actionType").val($("#actions").val());
    $("#lclRemarksForm").submit();
}