jQuery(document).ready(function(){
  jQuery(".scrolldisplaytable").height(jQuery("body").height()-jQuery("#searchFilters").height()-100)
  jQuery(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == 13) {
            search();
        }
    });
});

function onChangeDate(obj){
  if(jQuery.trim(jQuery(obj).val())!="" && !isDate(jQuery(obj))){
    jQuery(obj).val("").select().fadeIn().fadeOut().fadeIn();
    jQuery.prompt("Please enter date in mm/dd/yyyy format");
  }
}

function search(){
  jQuery("#action").val("search");
  jQuery("#currentPageNo").val("1");
  jQuery("#sortBy").val("reportedDate");
  jQuery("#orderBy").val("desc");
  jQuery("#arMigrationLogForm").submit();
}

function gotoPage(pageNo){
  jQuery("#currentPageNo").val(pageNo);
  jQuery("#action").val("search");
  jQuery("#arMigrationLogForm").submit();
}
function gotoSelectedPage(){
  jQuery("#currentPageNo").val(jQuery("#selectedPageNo").val());
  jQuery("#action").val("search");
  jQuery("#arMigrationLogForm").submit();
}
function doSort(sortBy,orderBy){
  jQuery("#sortBy").val(sortBy);
  jQuery("#orderBy").val(orderBy);
  jQuery("#action").val("search");
  jQuery("#arMigrationLogForm").submit();
}

function showCsvFile(id){
  showPreloading();
  jQuery.post(jQuery("#arMigrationLogForm").attr("action"), {
    id: id, 
    action: "showCsvFile"
  },
  function(data) {
    closePreloading();
    showMask();
    jQuery("<div style='width:900px;height:220px'></div>").html(data).addClass("popup").appendTo("#arMigrationLogForm").center();
  });
}

function showReprocessLogs(id){
  showPreloading();
  jQuery.post(jQuery("#arMigrationLogForm").attr("action"), {
    id: id, 
    action: "showReprocessLogs"
  },
  function(data) {
    closePreloading();
    showMask();
    jQuery("<div style='width:900px;height:220px'></div>").html(data).addClass("popup").appendTo("#arMigrationLogForm").center();
  });
}

function saveCsvFile(id){
  jQuery.prompt("Do you want to save the changes?",{
    buttons:{
      Yes:true,
      No:false
    },
    callback: function(v,m,f){
      if(v){
        showPreloading();
        var params = $(".input-text").serialize();
        params+="&id="+id+"&action=saveCsvFile";
        jQuery.post(jQuery("#arMigrationLogForm").attr("action"), params,
          function(data) {
            closePreloading();
            if(data=="success"){
              closePopUpDiv();
              jQuery.prompt("The csv file is updated and ready to reprocess..");
            }else{
              jQuery.prompt(data);
            }
          }
          );
      }
    }
  });
}

function reprocessAllErrors(){
  jQuery.prompt("Do you want to reprocess all the errors?",{
    buttons:{
      Yes:true,
      No:false
    },
    callback: function(v,m,f){
      if(v){
        jQuery("#log tbody").empty();
        showPreloading();
        jQuery.post(jQuery("#arMigrationLogForm").attr("action"), {
          action: "reprocessAllErrors"
        }, function(data) {
          closePreloading();
          if(data=="success"){
            jQuery.prompt("All the error files got reprocessed");
          }else{
            showMask();
            jQuery("<div style='width:900px;height:220px'></div>").html(data).addClass("popup").appendTo("#arMigrationLogForm").center();
          }
        });
      }
    }
  });
}

function reprocessSingleError(id,obj){
  jQuery.prompt("Do you want to reprocess this error?",{
    buttons:{
      Yes:true,
      No:false
    },
    callback: function(v,m,f){
      if(v){
        showPreloading();
        jQuery.post(jQuery("#arMigrationLogForm").attr("action"), {
          id: id, 
          action: "reprocessSingleError"
        }, function(data) {
          closePreloading();
          if(data=="success"){
            jQuery(obj).parent().parent().remove();
            jQuery.prompt("The file got reprocessed");
          }else if(data=="warning"){
            jQuery(obj).parent().parent().remove();
            jQuery.prompt("The file got reprocessed but with some warnings");
          }else{
            showMask();
            jQuery("<div style='width:600px;height:220px'></div>").html(data).addClass("popup").appendTo("#arMigrationLogForm").center();
          }
        });
      }
    }
  });
}

function deleteLog(id,obj){
  jQuery.prompt("Do you want to delete this error log?",{
    buttons:{
      Yes:true,
      No:false
    },
    callback: function(v,m,f){
      if(v){
        showPreloading();
        jQuery.post(jQuery("#arMigrationLogForm").attr("action"), {
          id: id, 
          action: "delete"
        }, function(data) {
          closePreloading();
          if(data=="success"){
            jQuery(obj).parent().parent().remove();
            jQuery.prompt("The log is removed");
          }else{
            jQuery.prompt(data);
          }
        });
      }
    }
  });
}

function printControlReport(){
  if(jQuery.trim(jQuery("#txtcal1").val())=="" || !isDate(jQuery("#txtcal1"))){
    jQuery("#txtcal1").val("").select().fadeIn().fadeOut().fadeIn();
    jQuery.prompt("Please enter from date in mm/dd/yyyy format");
  }else if(jQuery.trim(jQuery("#txtcal2").val())=="" || !isDate(jQuery("#txtcal2"))){
    jQuery("#txtcal2").val("").select().fadeIn().fadeOut().fadeIn();
    jQuery.prompt("Please enter to date in mm/dd/yyyy format");
  }else{
    jQuery("#action").val("printControlReport");
    jQuery("#arMigrationLogForm").submit();
  }
}

function exportControlReport(){
  if(jQuery.trim(jQuery("#txtcal1").val())=="" || !isDate(jQuery("#txtcal1"))){
    jQuery("#txtcal1").val("").select().fadeIn().fadeOut().fadeIn();
    jQuery.prompt("Please enter from date in mm/dd/yyyy format");
  }else if(jQuery.trim(jQuery("#txtcal2").val())=="" || !isDate(jQuery("#txtcal2"))){
    jQuery("#txtcal2").val("").select().fadeIn().fadeOut().fadeIn();
    jQuery.prompt("Please enter to date in mm/dd/yyyy format");
  }else{
    jQuery("#action").val("exportControlReport");
    jQuery("#arMigrationLogForm").submit();
    closePreloading();
  }
}
