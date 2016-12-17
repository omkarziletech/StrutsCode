jQuery(document).ready(function(){
    jQuery("#file").fileInput();
    jQuery(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == 13) {
            search();
        }
    });
    jQuery("#search").click(function(){
        search();
    });

    jQuery("#add").click(function(){
        jQuery("#action").val("addBatch");
        jQuery("#glBatchForm").submit();
    });

    jQuery("#reset").click(function(){
        jQuery("#action").val("reset");
        jQuery("#glBatchForm").submit();
    });

});
function search(){
    if(isNaN(jQuery("#batchId").val())) {
        alert('Please enter valid batch number');
        jQuery("#batchId").focus();
    }else {
        jQuery("#pageNo").val("1");
        jQuery("#sortBy").val("batchId");
        jQuery("#orderBy").val("desc");
        jQuery("#action").val("searchBatch")
        jQuery("#glBatchForm").submit();
    }
}
function gotoPage(pageNo){
    jQuery("#pageNo").val(pageNo);
    jQuery("#action").val("searchBatch");
    jQuery("#glBatchForm").submit();
}
function gotoSelectedPage(){
    jQuery("#pageNo").val(jQuery("#selectedPageNo").val());
    jQuery("#action").val("searchBatch");
    jQuery("#glBatchForm").submit();
}
function doSort(sortBy,orderBy){
    jQuery("#sortBy").val(sortBy);
    jQuery("#orderBy").val(orderBy);
    jQuery("#action").val("searchBatch");
    jQuery("#glBatchForm").submit();
}

function editBatch(batchId){
    jQuery("#batchId").val(batchId);
    jQuery("#action").val("editBatch");
    jQuery("#glBatchForm").submit();
}

function viewBatch(batchId){
    jQuery("#batchId").val(batchId);
    jQuery("#action").val("editBatch");
    jQuery("#glBatchForm").submit();
}

function postBatch(batchId){
    jQuery.prompt("Do you want to post this batch - "+batchId+"?",{
        buttons:{
            Yes:true,
            No:false
        },
        callback: function(v,m,f){
            if(v){
                jQuery("#batchId").val(batchId);
                jQuery("#action").val("postBatch");
                jQuery("#glBatchForm").submit();
            }
        }
    });
}

function showScanOrAttach(documentId){
    GB_show('Upload', rootPath+'/scan.do?screenName=JOURNAL ENTRY&documentId='+documentId, 350, 650);
}

function voidBatch(batchId){
    jQuery.prompt("Do you want to void this batch - "+batchId+"?",{
        buttons:{
            Yes:true,
            No:false
        },
        callback: function(v,m,f){
            if(v){
                jQuery("#batchId").val(batchId);
                jQuery("#action").val("voidBatch");
                jQuery("#glBatchForm").submit();
            }
        }
    });
}

function printBatch(batchId){
    var url = rootPath+"/glBatch.do?action=printBatch&glBatch.id="+batchId;
    window.parent.showGreyBox("GL Batch Report - "+batchId,url);
}

function exportBatch(batchId){
    jQuery("#glBatchId").val(batchId);
    jQuery("#action").val("exportBatch");
    jQuery("#glBatchForm").submit();
    closePreloading();
}

function importBatch(){
    if (jQuery.trim(jQuery("#file").val()) === "") {
        jQuery.prompt("Please attach batch excel spreadsheet to upload");
    } else {
        jQuery("#action").val("upload");
        jQuery("#className").val("com.logiware.dwr.GeneralLedgerDwr");
        jQuery("#methodName").val("importBatchFile");
        jQuery("#glBatchForm").fileupload({
            preloading: true,
            success: function (data) {
                if (data) {
                    jQuery("#batchFileName").val(data);
                    jQuery("#action").val("importBatch");
                    jQuery("#glBatchForm").submit();
                }
            }
        });
    }
}

function showBatchNotes(batchId){
    var moduleId = jQuery.trim(jQuery("#notesConstantGlBatch").val());
    GB_show("Notes", rootPath+"/notes.do?moduleId="+moduleId+"&moduleRefId="+batchId,375,700);
}