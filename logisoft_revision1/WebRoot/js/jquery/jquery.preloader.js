/* 
 *  Document   : jquery.preloader
 *  Created on : Jan 18, 2015, 4:15:00 PM
 *  Author     : Lakshmi Narayanan
 */
(function ($) {
    var path = "/" + window.location.pathname.split('/')[1];
    var mask = $("<div></div>");
    mask.css({
        background: "lightgray",
        opacity: "0.75",
        filter: "alpha(opacity = 75)",
        position: "absolute",
        top: "0px",
        left: "0px",
        width: "100%",
        overflow: "auto",
        "z-index": "40000"
    });
    var preloader = $("<div id='preloader'></div>");
    preloader.css({
        width: "250px",
        height: "48px",
        left: "45%",
        position: "absolute",
        "z-index": "40000"
    });
    preloader.append("<img alt='Loading, Please wait.....' src='" + path + "/images/preloader.gif'></img>");
    preloader.append("<label class='seconds' style='font-weight: bolder;'></label>");
    var timeout;
    $.updatePreloader = function (seconds) {
        $(".seconds").html("Loading in " + seconds + "s...");
        if (preloader.length > 0) {
            timeout = setTimeout(function () {
                seconds++;
                $.updatePreloader(seconds);
            }, 1000);
        }
    };

    $.startPreloader = function () {
        $("body").append(mask);
        $("body").css("overflow", "none");
        mask.css({
            width: $(document).width(),
            height: $(document).height()
        });
        $("body").append(preloader);
        preloader.css({
            position: "absolute",
            top: (Math.max(0, (($(window).height() - $(preloader).outerHeight()) / 2) + $(window).scrollTop()) + "px"),
            left: (Math.max(0, (($(window).width() - $(preloader).outerWidth()) / 2) + $(window).scrollLeft()) + "px")
        });
        $.updatePreloader(1);
    };

    $.stopPreloader = function () {
        mask.remove();
        $("body").css("overflow", "auto");
        preloader.remove();
        clearTimeout(timeout);
    };
}(jQuery));
