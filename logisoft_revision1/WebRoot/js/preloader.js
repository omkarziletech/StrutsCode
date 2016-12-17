/* 
 *  Document   : preloader
 *  Created on : Mar 27, 2014, 3:46:00 PM
 *  Author     : Lakshmi Narayanan
 */
var seconds = 1;
var showPreloader = true;

function showPreloading() {
    showMask();
    jQuery(".preloader").show().alignMiddle();
    showPreloader = true;
    updatePreloading();
}
function updatePreloading() {
    jQuery(".seconds").html("Loading in " + seconds + "s...");
    seconds++;
    if (showPreloader) {
        setTimeout("updatePreloading()", 1000);
    }
}

function closePreloading() {
    hideMask();
    jQuery(".preloader").hide();
    jQuery(".seconds").html("");
    showPreloader = false;
    seconds = 1;
}

function showDwrPreloading() {
    dwr.engine.setPreHook(function() {
        showPreloading();
    });
    dwr.engine.setPostHook(function() {
        closePreloading();
    });
}

function showMask() {
    jQuery(".mask").show();
    jQuery("body").css("overflow", "none");
    jQuery(".mask").css("width", jQuery(document).width());
    jQuery(".mask").css("height", jQuery(document).height());
}

function hideMask() {
    jQuery("body").css("overflow", "auto");
    jQuery(".mask").hide();
}

function showAlternateMask() {
    jQuery(".alternate-mask").show();
    jQuery(".alternate-mask").css("width", jQuery(document).width());
    jQuery(".alternate-mask").css("height", jQuery(document).height());
}

function hideAlternateMask() {
    jQuery(".alternate-mask").hide();
}

function closePopUpDiv() {
    jQuery('.popup').remove();
    hideMask();
    hideAlternateMask();
}

function hidePopUpDiv() {
    jQuery('.popup').hide();
    hideMask();
    hideAlternateMask();
}
function showLoading() {
    showMask();
    jQuery(".preloader").show();
    jQuery(".preloader").alignMiddle();
    showPreloader = true;
    updatePreloading();
}

function showDwrPreloading() {
    dwr.engine.setPreHook(function() {
        showPreloading();
    });
    dwr.engine.setPostHook(function() {
        closePreloading();
    });
}

jQuery.fn.alignMiddle = function() {
    this.css("position", "absolute");
    this.css("top", Math.max(0, ((jQuery(window).height() - jQuery(this).outerHeight()) / 2) + jQuery(window).scrollTop()) + "px");
    this.css("left", Math.max(0, ((jQuery(window).width() - jQuery(this).outerWidth()) / 2) + jQuery(window).scrollLeft()) + "px");
    return this;
};