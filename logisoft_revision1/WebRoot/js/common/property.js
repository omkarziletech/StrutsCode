/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var path = "/" + window.location.pathname.split('/')[1];

function refresh() {
    showPreloading();
    window.location = path + "/property.do";
}

function showMessage(message){
    $(".message").html(message);
}

function save(id) {
    var url = $("#propertyForm").attr("action");
    ajaxCall(url,{
        data: {
            action: "save",
            id: id,
            value : $("#propertyTextValue" + id).val()
        },
        preloading: true,
        success: "showMessage",
        async:false
    });
}

$(document).ready(function() {
    $("#propertiesForm").submit(function() {
        showPreloading();
    });
    $("[title != '']").not("link").tooltip();
    $("ul.htabs").tabs("> .pane", {effect: 'fade', current: 'selected', initialIndex: 0, onClick: function() {
            var index = $("ul.htabs li.selected").find("a").attr("tabindex");
            var src = $("#src" + index).val();
            if ($("#tab" + index).attr("src") === '') {
                $("#tab" + index).attr("src", src);
            }
            $("#tab" + index).height($(document).height() - 45);
        }
    });
});