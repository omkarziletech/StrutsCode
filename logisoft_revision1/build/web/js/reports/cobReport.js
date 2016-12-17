/* 
 * Document   : cobReport
 * Created on : Mar 04, 2015, 04:56:13 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];


function fromDateChange() {
    if ($.trim($("#fromDate").val()) !== "" && !validateDate($("#fromDate"))) {
        $.prompt("Please enter valid from date", {
            callback: function () {
                $("#fromDate").val("").focus().fadeIn().fadeOut().fadeIn();
            }
        });
    }
}

function toDateChange() {
    if ($.trim($("#toDate").val()) !== "" && !validateDate($("#toDate"))) {
        $.prompt("Please enter valid to date", {
            callback: function () {
                $("#toDate").val("").focus().fadeIn().fadeOut().fadeIn();
            }
        });
    }
}

function initDate() {
    $("#fromDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='From Date'/>",
        onClose: function () {
            fromDateChange();
        }
    }).change(function () {
        fromDateChange();
    });
    $("#toDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='To Date'/>",
        onClose: function () {
            toDateChange();
        }
    }).change(function () {
        toDateChange();
    });
}

function selectAllTerminals(ele){
    if($(ele).val() === "Select All Terminals"){
        $("input[name='billingTerminal']").each(function(){
            $(this).attr("checked", true);
        });
        $(ele).val("Unselect All Terminals");
    }else{
        $("input[name='billingTerminal']").each(function(){
            $(this).removeAttr("checked");
        });
        $(ele).val("Select All Terminals");
    }
}

function selectAllRegions(ele){
    if($(ele).val() === "Select All Regions"){
        $("input[name='destinationRegions']").each(function(){
            $(this).attr("checked", true);
        });
        $(ele).val("Unselect All Regions");
    }else{
        $("input[name='destinationRegions']").each(function(){
            $(this).removeAttr("checked");
        });
        $(ele).val("Select All Regions");
    }
}

function exportToExcel() {
    if ($.trim($("#fromDate").val()) === "") {
        $.prompt("Please enter from date", {
            callback: function () {
                $("#fromDate").val("").focus().fadeIn().fadeOut().fadeIn();
            }
        });
    } else if ($.trim($("#toDate").val()) === "") {
        $.prompt("Please enter to date", {
            callback: function () {
                $("#toDate").val("").focus().fadeIn().fadeOut().fadeIn();
            }
        });
    } else if ($("input[name='billingTerminal']:checked").length === 0) {
        $.prompt("Please select atleast one billing terminal");
    } else if ($("input[name='destinationRegions']:checked").length === 0) {
        $.prompt("Please select atleast one destination region");
    } else {
        var billingTerminal = "";
        $("input[name='billingTerminal']:checked").each(function () {
            billingTerminal += (billingTerminal.length > 0 ? "<-->" : "") + $(this).val();
        });
        var destinationRegions = "";
        $("input[name='destinationRegions']:checked").each(function () {
            destinationRegions += (destinationRegions.length > 0 ? "," : "") + $(this).val();
        });
        var dateRange = "";
       $("input:radio[name=dateRange]:checked").each(function (){
           dateRange += (dateRange.length > 0 ? "," : "") + $(this).val();
        });
        var includeBookings = "";
        $("input[name='includeBookings']:checked").each(function (){
           includeBookings+= (includeBookings.length > 0 ? "," : "") + $(this).val(); 
        });
        
       $.ajaxx({
            url: path + "/report.do",
            data: {
                action: "exportCobReport",
                billingTerminal: billingTerminal,
                destinationRegions: destinationRegions,
                fromDate: $("#fromDate").val(),
                toDate: $("#toDate").val(),
                dateRange: dateRange,
                includeBookings: includeBookings
            },
            beforeSend: function () {
                $.startPreloader();
            },
            success: function (fileName) {
                $.stopPreloader();
                var src = path + "/servlet/FileViewerServlet?fileName=" + fileName;
                if ($("#iframe").length > 0) {
                    $("#iframe").attr("src", src);
                } else {
                    $("<iframe/>", {
                        name: "iframe",
                        id: "iframe",
                        src: src
                    }).appendTo("body").hide();
                }
            },
            error: function () {
                $.stopPreloader();
            }
        });
    }
}

$(document).ready(function () {
    initDate();
    $("[title != '']").not("link").tooltip();
});
