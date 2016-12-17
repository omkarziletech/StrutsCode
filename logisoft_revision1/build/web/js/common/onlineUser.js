/* 
 * Document   : onlineUser
 * Created on : May 06, 2014, 05:20:00 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function search() {
    $("#sortBy").val("loggedOn");
    $("#orderBy").val("desc");
    $("#selectedPage").val("1");
    $("#action").val("search");
    $("#onlineUserForm").submit();
}

function exportToExcel() {
    $.ajaxx({
        url: path + "/onlineUser.do",
        data: {
            action: "exportToExcel"
        },
        preloading: true,
        success: function(data) {
            var src = path + "/servlet/FileViewerServlet?fileName=" + data;
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
    });
}

function sorting(sortBy) {
    $("#sortBy").val(sortBy);
    if ($("#orderBy").val() === "desc") {
        $("#orderBy").val("asc");
    } else {
        $("#orderBy").val("desc");
    }
    $("#action").val("search");
    $("#onlineUserForm").submit();
}

function paging(pageNo) {
    if (pageNo === "") {
        $("#selectedPage").val($("#selectedPageNo").val());
    } else {
        $("#selectedPage").val(pageNo);
    }
    $("#action").val("search");
    $("#onlineUserForm").submit();
}

function killUser(loginName, userId) {
    $.prompt("Do you want to kill the user - " + loginName + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function(v) {
            if (v) {
                $("#id").val(userId);
                $("#action").val("killUser");
                $("#onlineUserForm").submit();
            }
        }
    });
}

$(document).ready(function() {
    $("#onlineUserForm").submit(function() {
        showPreloading();
    });
    $("[title != '']").not("link").tooltip();
});