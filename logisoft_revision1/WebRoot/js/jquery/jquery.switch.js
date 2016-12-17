/*
 Document   : jquery.switch
 Created on : Sep 27, 2013, 5:46:10 PM
 Author     : Lakshmi Narayanan
 Description: To create css/jquery switch for checkboxes
 */
function createGuid(){
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c === 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}

(function() {
    $.fn.jswitch = function(options) {
	options = options || {};
	options.afterContent = options.afterContent || "ON";
	options.beforeContent = options.beforeContent || "OFF";
	options.css = options.css || {
	    width: "40px",
	    margin: "0 0 0 15px"
	};
	options.styleClass = options.styleClass || "";
	return this.each(function() {
	    var $input = $(this);
	    $input.change(function() {
		if (typeof options.callback === "function") {
		    options.callback(this);
		}
	    });
	    $input.after("<div class='" + options.styleClass + " bool-slider true'><div class='inset'><div class='control'></div></div></div>");
	    $input.parent().find(".bool-slider").css(options.css);
	    var id = $input.attr("id");
            if($.trim(id) === ""){
                id = createGuid();
                $input.attr("id", id);
            }
	    var $switch = $input.parent().find(".bool-slider .inset .control");
	    $switch.click(function() {
		if (!$(this).parent().parent().hasClass('disabled')) {
		    if ($(this).parent().parent().hasClass('true')) {
			$(this).parent().parent().addClass('false').removeClass('true');
			$("#" + id).removeAttr("checked").change();
		    } else {
			$(this).parent().parent().addClass('true').removeClass('false');
			$("#" + id).attr("checked", true).change();
		    }
		}
	    });
	    $switch.attr({'after-content': options.afterContent, 'before-content': options.beforeContent});
	    if ($input.is(":checked")) {
		$switch.parent().parent().addClass("true").removeClass('false');
	    } else {
		$switch.parent().parent().addClass('false').removeClass('true');
	    }
	    if ($input.is(":disabled")) {
		$switch.parent().parent().addClass("disabled");
	    }
	    $input.hide();
	});
    };
})(jQuery);