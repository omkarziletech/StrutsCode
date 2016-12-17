/* 
 *  Document   : subledger
 *  Created on : Dec 20, 2012, 3:28:08 PM
 *  Author     : Lakshmi Narayanan
 */

function fromDateChange() {
    if ($.trim($("#fromDate").val()) != "") {
        if (!isDate($("#fromDate"))) {
            $.prompt("Please enter valid from date", {
                callback: function() {
                    $("#fromDate").val("").callFocus();
                }
            });
        } else if (isGreaterThan($("#fromDate"), $("#toDate"))) {
            $.prompt("From date cannot be greater than to date", {
                callback: function() {
                    $("#fromDate").val("").callFocus();
                }
            });
        }
    }
}

function toDateChange() {
    if ($.trim($("#toDate").val()) != "") {
        if (!isDate($("#toDate"))) {
            $.prompt("Please enter valid to date", {
                callback: function() {
                    $("#toDate").val("").callFocus();
                }
            });
        } else if (isGreaterThan($("#fromDate"), $("#toDate"))) {
            $.prompt("To date cannot be lesser than from date", {
                callback: function() {
                    $("#toDate").val("").callFocus();
                }
            });
        }
    }
}

function initDate() {
    $("#fromDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='From Date'/>",
        onClose: function() {
            fromDateChange();
        }
    }).change(function() {
        fromDateChange();
    });
    $("#toDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='To Date'/>",
        onClose: function() {
            toDateChange();
        }
    }).change(function() {
        toDateChange();
    });
}

function initGlPeriod() {
    $("#glPeriod").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_PERIOD&template=glPeriod&fieldIndex=1,3,4&",
        width: "350px",
        otherFields: "fromDate^toDate",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 250
    });
}

function initAmounts() {
    $("#fromAmount,#toAmount").numeric();
}

function showMessage(result) {
    if (result == "success") {
        $.prompt("GL Account is updated successfully");
    }
}

function updateGlAccount(id, glAccount) {
    if ($.trim($("#" + glAccount).val()) == "") {
        $.prompt("Please enter gl account", {
            callback: function() {
                $("#" + glAccount).callFocus();
            }
        })
    } else {
        var url = $("#subledgerForm").attr("action");
        var params = "action=updateGlAccount";
        params += "&subledgerId=" + id;
        params += "&glAccount=" + $("#" + glAccount).val();
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "showMessage",
            async: false
        });
    }
}

function initGlAccount() {
    $("#glAccount").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
        width: "350px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
    if ($(".glAccount").length > 0) {
        $(".glAccount").each(function() {
            $(this).initautocomplete({
                url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
                row: true,
                width: "350px",
                resultsClass: "ac_results z-index",
                resultPosition: "fixed",
                scroll: true,
                scrollHeight: "125px"
            });
        });
        $("#glAccount0").callFocus();
    }
}

function searchByChange() {
    var searchBy = $.trim($("#searchBy").val());
    if (searchBy == "transaction_amt") {
        $("#searchByAmount").show();
        $("#fromAmount").removeAttr("readonly").removeClass("readonly").val("0.00").callFocus();
        $("#toAmount").removeAttr("readonly").removeClass("readonly").val("0.00");
        $("#searchByValue").hide();
        $("#searchValue").attr("readonly", true).addClass("readonly").val("");
        $("#searchByGlAccount").hide();
        $("#glAccount").attr("readonly", true).addClass("readonly").val("");
    } else if (searchBy == "gl_account_number") {
        $("#searchByGlAccount").show();
        $("#glAccount").removeAttr("readonly").removeClass("readonly").val("").callFocus();
        $("#searchByValue").hide();
        $("#searchValue").attr("readonly", true).addClass("readonly").val("");
        $("#searchByAmount").hide();
        $("#fromAmount").attr("readonly", true).addClass("readonly").val("0.00");
        $("#toAmount").attr("readonly", true).addClass("readonly").val("0.00");
    } else if (searchBy != "" && searchBy != "blank_gl" && searchBy != "gl_not_in_coa") {
        $("#searchByValue").show();
        $("#searchValue").removeAttr("readonly").removeClass("readonly").val("").callFocus();
        $("#searchByGlAccount").hide();
        $("#glAccount").attr("readonly", true).addClass("readonly").val("");
        $("#searchByAmount").hide();
        $("#fromAmount").attr("readonly", true).addClass("readonly").val("0.00");
        $("#toAmount").attr("readonly", true).addClass("readonly").val("0.00");
    } else {
        $("#searchValue").attr("readonly", true).addClass("readonly").val("");
        $("#searchByGlAccount").hide();
        $("#glAccount").attr("readonly", true).addClass("readonly").val("");
        $("#searchByAmount").hide();
        $("#fromAmount").attr("readonly", true).addClass("readonly").val("0.00");
        $("#toAmount").attr("readonly", true).addClass("readonly").val("0.00");
    }
}

function validate() {
    if ($.trim($("#glPeriod").val()) == "") {
        $.prompt("Please enter gl period", {
            callback: function() {
                $("#glPeriod").callFocus();
            }
        })
        return false;
    } else if ($.trim($("#fromDate").val()) == "") {
        $.prompt("Please enter from date", {
            callback: function() {
                $("#fromDate").callFocus();
            }
        })
        return false;
    } else if ($.trim($("#toDate").val()) == "") {
        $.prompt("Please enter to date", {
            callback: function() {
                $("#toDate").callFocus();
            }
        })
        return false;
    } else if ($.trim($("#searchBy").val()) == "gl_account_number"
            && $.trim($("#glAccount").val()) == "") {
        $.prompt("Please enter gl account", {
            callback: function() {
                $("#glAccount").callFocus();
            }
        })
        return false;
    } else if ($.trim($("#fromAmount").val()) == ""
            && $.trim($("#toAmount").val()) == ""
            && ($.trim($("#searchBy").val()) == "transaction_amt")) {
        $.prompt("Please enter From or To Amount", {
            callbak: function() {
                $("#fromAmount").val("").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#searchBy").val()) != ""
            && $.trim($("#searchBy").val()) != "blank_gl"
            && $.trim($("#searchBy").val()) != "gl_not_in_coa"
            && $.trim($("#searchBy").val()) != "gl_account_number"
            && $.trim($("#searchBy").val()) != "transaction_amt"
            && $.trim($("#searchValue").val()) == "") {
        $.prompt("Please enter search value", {
            callbak: function() {
                $("#searchValue").val("").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function search() {
    if (validate()) {
        $("#action").val("search");
        $("#limit").val("200");
        $("#selectedPage").val("1");
        $("#sortBy").val(($.trim($("#subledger").val()) == "ACC" || $.trim($("#subledger").val()) == "ALL") ? "sailing_date" : "posted_date");
        $("#orderBy").val("desc");
        $("#subledgerForm").submit();
    }
}

function paging(pageNo) {
    if (pageNo !== "") {
        $("#selectedPage").val(pageNo);
    } else {
        $("#selectedPage").val($("#selectedPageNo").val());
    }
    $("#action").val("sortingAndPaging");
    $("#subledgerForm").submit();
}

function sorting(sortBy) {
    $("#sortBy").val(sortBy);
    if ($("#orderBy").val() === "desc") {
        $("#orderBy").val("asc");
    } else {
        $("#orderBy").val("desc");
    }
    $("#action").val("sortingAndPaging");
    $("#subledgerForm").submit();
}

function clearAll() {
    $("#action").val("clearAll");
    $("#subledgerForm").submit();
}

function initGlAccounts() {
    $(".glAccounts").each(function() {
        $(this).initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
            row: true,
            width: "350px",
            resultsClass: "ac_results z-index",
            resultPosition: "fixed",
            scroll: true,
            scrollHeight: "125px"
        });
    });
    $("#glAccounts0").callFocus();
}

function post(data) {
    if (data != "") {
        $.prompt("Some records are missing GL Account - they will not be posted to the GL.", {
            buttons: {
                "Yes, Continue": "Yes",
                "No, Correct the GL Account": "No",
                "Cancel": "Cancel"
            },
            callback: function(v, m, f) {
                if (v == "Yes") {
                    $("#action").val("post");
                    $("#subledgerForm").submit();
                } else if (v == "No") {
                    $.prompt(data, {
                        top: "20%",
                        left: "20%",
                        width: "90%",
                        height: "50%",
                        overflowy: "auto",
                        buttons: {},
                        loaded: function() {
                            initGlAccounts();
                        }
                    });
                }
            }
        });
    } else {
        $("#action").val("post");
        $("#subledgerForm").submit();
    }
}

function validateGlAccounts(data) {
    if (data) {
        var url = $("#subledgerForm").attr("action");
        var params = "action=validateGlAccounts";
        params += "&" + $("#subledgerForm").serialize();
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "post",
            async: false
        });
    } else {
        $.prompt("Cannot Post Subledger - one or more AR Batches are open for this period");
    }
}

function validateArBatches() {
    var url = $("#subledgerForm").attr("action");
    var params = "action=validateArBatches";
    params += "&" + $("#subledgerForm").serialize();
    ajaxCall(url, {
        data: params,
        preloading: true,
        success: "validateGlAccounts",
        async: false
    });

}

function validatePost() {
    if ($.trim($("#subledger").val()) == "") {
        $.prompt("You cannot post ALL subledgers. Please select one.", {
            callback: function() {
                $("#subledger").callFocus();
            }
        })
    } else if ($.trim($("#glPeriod").val()) == "") {
        $.prompt("Please enter gl period", {
            callback: function() {
                $("#glPeriod").callFocus();
            }
        })
    } else if ($.trim($("#fromDate").val()) == "") {
        $.prompt("Please enter from date", {
            callback: function() {
                $("#fromDate").callFocus();
            }
        })
    } else if ($.trim($("#toDate").val()) == "") {
        $.prompt("Please enter to date", {
            callback: function() {
                $("#toDate").callFocus();
            }
        })
    } else {
        validateArBatches();
    }
}

function exportExcel(fileName) {
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
}

function createExcel() {
    if (validate()) {
        var url = $("#subledgerForm").attr("action");
        var params = "action=createExcel";
        params += "&" + $("#subledgerForm").serialize();
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "exportExcel",
            async: false
        });
    }
}

function setResultHeight() {
    if ($(".result-container").length > 0) {
        var windowHeight = window.parent.getFrameHeight();
        var height = windowHeight;
        height -= $(".filter-container").height();
        height -= $(".table-banner").height();
        height -= 100;
        $(".result-container").height(height);
        $("body").css("overflow", "hidden");
    }
}

$(document).ready(function() {
    $("#subledgerForm").submit(function() {
        showPreloading();
    });
    setResultHeight();
    initDate();
    initGlPeriod();
    initAmounts();
    initGlAccount();
    $("#searchBy").change(function() {
        searchByChange();
    });
    $("[title != '']").not("link").tooltip();
    $(document).keypress(function(event) {
        if ($("#" + event.target.id).length <= 0
                || (event.target.id != "jqi_state0_buttonOk" && event.target.id != "jqi_state0_buttonYes" && event.target.id != "jqi_state0_buttonNo")) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == 13) {
                event.preventDefault();
                search();
            }
        }
    });
});