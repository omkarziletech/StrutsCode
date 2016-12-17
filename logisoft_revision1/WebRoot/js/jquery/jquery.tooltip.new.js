/**
 *
 *  Document   : jquery.tooltip.js
 *  Created on : Oct 22, 2013, 11:25:43 PM
 *  Author     : Vijay Gupta
 */
(function() {
    $.fn.tooltip = function(options) {
	options = options || {};
	options.attr = options.attr || "title";

	var changePosition = function(e) {
	    var mousex = e.pageX + 10; //Get X coordinates
	    var mousey = e.pageY + 5; //Get Y coordinates
	    var tooltip = jQuery('.tooltip');
	    var width = tooltip.width();
	    var height = tooltip.height();
	    if(mousex + width + 10 > jQuery(window).width()){
		mousex = jQuery(window).width() - mousex + 10;
		if(mousey + height + 70 > jQuery(window).height()){
		    mousey = mousey - height - 30;
		}
		tooltip.css({
		    top: mousey,
		    right: mousex
		});
	    }else {
		if(mousey + height + 70 > jQuery(window).height()){
		    mousey = mousey - height - 30;
		    mousex = mousex - 10;
		}
		tooltip.css({
		    top: mousey,
		    left: mousex
		});
	    }
	};
	return this.each(function() {
	    var title = jQuery(this).attr(options.attr);
	    jQuery(this).data('tipText', title).removeAttr(options.attr);
	    if (jQuery.trim(title) !== "") {
		jQuery(this).hover(function(e){
		    // Hover over code
		    jQuery(this).addClass("cursor");
		    var tooltip = jQuery('<p class="tooltip"></p>')
		    .html(jQuery(this).data('tipText'))
		    .appendTo('body')
		    .fadeIn('slow');
		    if (jQuery(this).attr("tooltipType") !== "comments") {
			tooltip.addClass("tooltip-no-table");
		    }
		    var width = tooltip.width();
		    if (width > 400) {
			tooltip.width(400);
		    } else if (width < 50) {
			tooltip.width(50);
		    }
		    changePosition(e);
		}, function() {
		    // Hover out code
		    jQuery(this).removeClass("cursor");
		    jQuery('p.tooltip').remove();
		}).mousemove(function(e) {
		    //On mouse move code
		    changePosition(e);
		});
	    }
	});
    };
})(jQuery);


