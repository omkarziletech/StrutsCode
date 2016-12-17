(function(){
    jQuery.fn.center = function(){
	return this.each(function(){
	    var obj = jQuery(this);
	    centerElement(obj);
	    if (jQuery.browser.msie) {
		jQuery(window).scroll(function(){
		    centerElement(obj);
		});
	    }

	    function centerElement(obj){
		var element = jQuery(obj);
		var win = jQuery(window);
		if (jQuery.browser.msie) {
		    var viewportwidth;
		    var viewportheight;
		    if (typeof document.documentElement !== "undefined" 
			&& typeof document.documentElement.clientWidth !== "undefined" && document.documentElement.clientWidth !== 0){
			viewportwidth = document.documentElement.clientWidth,
			viewportheight = document.documentElement.clientHeight;
		    }else{
			viewportwidth = document.getElementsByTagName("body")[0].clientWidth,
			viewportheight = document.getElementsByTagName("body")[0].clientHeight;
		    }
		    element.css("position","absolute");
		    element.css("top",((viewportheight-element.height())/2+win.scrollTop()) + "px");
		    element.css("left", ((viewportwidth - element.width())/2+win.scrollLeft()) + "px");
		}else{
		    element.css("position","fixed");
		    element.css("top", (win.height() - element.height())/2 + "px");
		    element.css("left", (win.width() - element.width())/2 + "px");
		}
	    }
	});
    };
})();