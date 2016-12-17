/**
 *
 *  Document   : jquery.tooltip.js
 *  Created on : May 24, 2012, 11:25:43 PM
 *  Author     : Lakshmi Narayanan
 */
(function ($) {
    var changePosition = function (e) {
        var mousex = e.pageX + 10; //Get X coordinates
        var mousey = e.pageY + 5; //Get Y coordinates
        var tooltip = $('.tooltip');
        var width = tooltip.width();
        var height = tooltip.height();
        if (mousex + width + 10 > $(window).width()) {
            mousex = $(window).width() - mousex + 10;
            if (mousey + height + 70 > $(window).height()) {
                mousey = mousey - height - 30;
            }
            tooltip.css({
                top: mousey,
                right: mousex
            });
        } else {
            if (mousey + height + 70 > $(window).height()) {
                mousey = mousey - height - 30;
                mousex = mousex - 10;
            }
            tooltip.css({
                top: mousey,
                left: mousex
            });
        }
    };
    $.fn.extend({
        tooltip: function (options) {
            options = $.extend({
                attr: "title"
            }, options);
            return this.each(function () {
                var title = $(this).attr(options.attr);
                $(this).data('tipText', title).removeAttr(options.attr);
                if ($.trim(title) !== "") {
                    $(this).hover(function (e) {
                        // Hover over code
                        $(this).addClass("cursor");
                        var tooltip = $('<p class="tooltip"></p>')
                                .html($(this).data('tipText'))
                                .appendTo('body')
                                .fadeIn('slow');
                        if ($(this).attr("tooltipType") !== "comments") {
                            tooltip.addClass("tooltip-no-table");
                        }
                        var width = tooltip.width();
                        if (width > 400) {
                            tooltip.width(400);
                        } else if (width < 50) {
                            tooltip.width(50);
                        }
                        changePosition(e);
                    }, function () {
                        // Hover out code
                        $(this).removeClass("cursor");
                        $('p.tooltip').remove();
                    }).mousemove(function (e) {
                        //On mouse move code
                        changePosition(e);
                    });
                }
            });
        }
    });
})(jQuery);