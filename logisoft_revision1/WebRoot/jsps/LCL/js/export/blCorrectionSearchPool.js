function searchPoolResult(){
    showLoading();
    $("#methodName").val('searchCorrectionByPool');
    $("#lclCorrectionForm").submit();
}
function goBackByPoolSearch(){
    showLoading();
    $("#methodName").val('displayByPool');
    $("#lclCorrectionForm").submit();
}
function editPoolCorrection(correctionId, fileId,blNo) {
    showLoading();
    $("#blNo").val(blNo);
    $("#methodName").val('editCorrection');
    $('#fileId').val(fileId);
    $('#correctionId').val(correctionId);
    $('#screenName').val("searchPool");
    $("#lclCorrectionForm").submit();
}
function approvePendingCorrection(path, blNo, correctionId, fileId, noticeNo) {
    var href = path + "/blCorrection.do?methodName=viewAuthenticationScreen&blNo=" + blNo + "&correctionId=" + correctionId;
    href = href + "&fileId=" + fileId + "&noticeNo=" + noticeNo;
    $.colorbox({
        iframe: true,
        width: "50%",
        height: "50%",
        href: href,
        title: "Approval"
    });
}
function unApproveCorrection(correctionId, blNo, fileId, noticeNo) {
    $("#methodName").val('unApproveCorrections');
    document.getElementById('correctionId').value = correctionId;
    document.getElementById('concatenatedBlNo').value = blNo + "-" + noticeNo;
    document.getElementById('notesBlNo').value = "(" + blNo + "-C-" + noticeNo + ")";
    document.getElementById('fileId').value = fileId;
    document.getElementById('buttonValue').value = "UA";
    $("#lclCorrectionForm").submit();
}
function viewBlSearchCorrection(correctionId, fileId,blNo){
    $("#blNo").val(blNo);
    $("#methodName").val('viewCorrections');
    document.getElementById('correctionId').value = correctionId;
    document.getElementById('fileId').value = fileId;
    document.getElementById('screenName').value = "searchPool";
    $("#lclCorrectionForm").submit();
}
function defaultLoginName(thisVal,id1,id2){
    var loginName=$('#loginName').val();
    var loginUserId=$('#loginUserId').val();
    var checkBox_Obj=$(thisVal).is(":checked");
    if(checkBox_Obj){
        $('#'+id1).val(loginName);
        $('#'+id2).val(loginUserId);
        setdisableByField(id1);
        setdisableByField(id2);
    }else{
        $('#'+id1).val("");
        $('#'+id2).val("");
        setenableByField(id1);
        setenableByField(id2);
    }
}
function setdisableByField(id) {
    $('#' + id).css("background", "#CCEBFF");
    $('#' + id).attr("readonly", true);
}
function setenableByField(id) {
    $('#' + id).css("background", "#FFFFFF");
    $('#' + id).attr("readonly", false);
}