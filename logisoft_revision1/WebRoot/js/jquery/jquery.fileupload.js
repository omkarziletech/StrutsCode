/* 
 *  Document   : jquery.fileupload
 *  Created on : Feb 05, 2015, 3:41:00 PM
 *  Author     : Lakshmi Narayanan
 */
(function ($) {
    var path = "/" + window.location.pathname.split('/')[1];

    $.fn.extend({
        fileupload: function (options) {
            // Default options
            options = $.extend({
                url: path + "/fileUpload.do",
                type: "POST",
                dataType: "text",
                preloading: false,
                top: "20%",
                left: "35%",
                width: "800px",
                height: "300px",
                overflowy: "auto"
            }, options);
            if (typeof File !== "undefined" &&
                    typeof FormData !== "undefined" &&
                    typeof (new XMLHttpRequest()).upload !== "undefined") {
                var data = new FormData(document.getElementById($(this).attr("id")));
                $.ajax({
                    url: options.url + (options.url.indexOf("?") > -1 ? "&" : "?") + "date=" + new Date(),
                    type: options.type,
                    dataType: options.dataType,
                    data: data,
                    async: true,
                    cache: false,
                    contentType: false,
                    processData: false,
                    beforeSend: function (xhr) {
                        if (options.preloading) {
                            $.startPreloader();
                        }
                    },
                    success: function (data) {
                        if (options.preloading) {
                            $.stopPreloader();
                        }
                        if (options.success) {
                            options.success(data);
                        }
                    },
                    error: function (jqr) {
                        if (options.preloading) {
                            $.stopPreloader();
                        }
                        if ($.trim(jqr.responseText) !== "") {
                            $.prompt(jqr.responseText, {
                                top: options.top,
                                left: options.left,
                                width: options.width,
                                height: options.height,
                                overflowy: options.overflowy,
                                callback: function () {
                                    if (options.error) {
                                        options.error(jqr);
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                var iframe = $("<iframe name='uploadFrame' id='uploadFrame' src='javascript:false;' style='display: none'></iframe>");
                $("body").append(iframe);
                var form = $(this);
                form.attr("action", options.url + (options.url.indexOf("?") > -1 ? "&" : "?") + "date=" + new Date());
                form.attr("method", "POST");
                form.attr("encoding", "multipart/form-data");
                form.attr("enctype", "multipart/form-data");
                form.attr("target", "uploadFrame");
                form.submit(function () {
                    if (options.preloading) {
                        $.startPreloader();
                    }
                });
                form.submit();
                iframe.load(function () {
                    if (options.preloading) {
                        $.stopPreloader();
                    }
                    if (options.success) {
                        options.success($(this).contents().find("body").html());
                    }
                    iframe.remove();
                    form.removeAttr("target");
                });
            }
        }
    });
}(jQuery));
