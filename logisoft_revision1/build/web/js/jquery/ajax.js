/* 
 *  Document   : ajax
 *  Created on : Mar 26, 2014, 5:47:00 PM
 *  Author     : Lakshmi Narayanan
 */

/**
 * 
 * @requires jquery-impromptu.js
 * @requires preloading.js
 */
(function ($) {
    var path = "/" + window.location.pathname.split('/')[1];

    $.ajaxx = function (options) {
        // Default options
        options = $.extend({
            url: path + "/ajax",
            type: "POST",
            async: true,
            cache: false,
            dataType: "text",
            preloading: false,
            top: "20%",
            left: "35%",
            width: "800px",
            height: "300px",
            overflowy: "auto"
        }, options);

        $.ajax({
            url: options.url + (options.url.indexOf("?") > -1 ? "&" : "?") + "date=" + new Date(),
            type: options.type,
            dataType: options.dataType,
            data: options.data,
            cache: options.cache,
            async: options.async,
            beforeSend: function (xhr) {
                if (options.preloading) {
                    if ($(".preloader").length > 0) {
                        showPreloading();
                    } else {
                        $.startPreloader();
                    }
                }
                if (options.beforeSend) {
                    options.beforeSend();
                }
            },
            success: function (data) {
                if (options.preloading) {
                    if ($(".preloader").length > 0) {
                        closePreloading();
                    } else {
                        $.stopPreloader();
                    }
                }
                if (options.success) {
                    options.success(data);
                }
            },
            error: function (jqr) {
                if (options.preloading) {
                    if ($(".preloader").length > 0) {
                        closePreloading();
                    } else {
                        $.stopPreloader();
                    }
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
    };
}(jQuery));
