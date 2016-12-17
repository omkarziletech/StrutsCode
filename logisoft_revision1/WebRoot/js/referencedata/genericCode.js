/* 
 *  Document   : genericCode
 *  Created on : Apr 23, 2014, 1:10:00 PM
 *  Author     : Lakshmi Narayanan
 */
function search() {
    if ($.trim($("#codetypeid").val()) === "") {
        $.prompt("Please select code type.", {
            callback: function() {
                $("#codetypeid").focuz();
            }
        });
    } else {
        $("#action").val("search");
        $("#sortBy").val("code");
        $("#orderBy").val("asc");
        $("#selectedPage").val(1);
        $("#genericCodeForm").submit();
    }
}

function clearAll() {
    $("#action").val("clearAll");
    $("#genericCodeForm").submit();
}

function paging(pageNo) {
    if (pageNo !== "") {
        $("#selectedPage").val(pageNo);
    } else {
        $("#selectedPage").val($("#selectedPageNo").val());
    }
    $("#action").val("sortingAndPaging");
    $("#genericCodeForm").submit();
}

function sorting(sortBy) {
    $("#sortBy").val(sortBy);
    if ($("#orderBy").val() === "desc") {
        $("#orderBy").val("asc");
    } else {
        $("#orderBy").val("desc");
    }
    $("#action").val("sortingAndPaging");
    $("#genericCodeForm").submit();
}

function add() {
    if ($.trim($("#codetypeid").val()) === "") {
        $.prompt("Please select code type", {
            callback: function() {
                $("#codetypeid").val("").focuz();
            }
        });
    }else if ($.trim($("#codetypeid option:selected").text().toLowerCase()) === "cost code") {
        $.prompt("Cannot add Cost Code here. Please select another code type", {
            callback: function() {
                $("#codetypeid").val("").focuz();
            }
        });
    } else {
        $("#action").val("add");
        $("#genericCodeForm").submit();
    }
}

function edit(id) {
    $("#id").val(id);
    $("#action").val("edit");
    $("#genericCodeForm").submit();
}

function view(id) {
    $("#id").val(id);
    $("#action").val("view");
    $("#genericCodeForm").submit();
}

function deleteCode(id, code) {
    $.prompt("Do you want to delete this code - " + code + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function(v) {
            if (v) {
                $("#id").val(id);
                $("#action").val("delete");
                $("#genericCodeForm").submit();
            }
        }
    });
}

function save() {
    if ($.trim($("#code").val()) === "") {
        $.prompt("Please enter code", {
            callback: function() {
                $("#code").val("").focuz();
            }
        });
    } else {
        $("#action").val("save");
        $("#genericCodeForm").submit();
    }
}

function refresh(action) {
    if (action === "edit") {
        $("#action").val("edit");
        $("#genericCodeForm").submit();
    } else {
        $("#action").val("add");
        $("#genericCodeForm").submit();
    }
}

function goback() {
    $("#action").val("sortingAndPaging");
    $("#genericCodeForm").submit();
}

function setResultHeight() {
    if ($(".result-container").length > 0) {
        var height = window.parent.getFrameHeight() - 100;
        $(".result-container").height(height);
        $("body").css("overflow-y", "hidden");
    }
}

$(document).ready(function() {
    setResultHeight();
    $("#genericCodeForm").submit(function() {
        showPreloading();
    });
    $("[title != '']").not("link").tooltip();
});