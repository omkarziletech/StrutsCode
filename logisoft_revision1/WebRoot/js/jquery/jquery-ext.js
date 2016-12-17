jQuery.fn.initautocomplete = function(options) {
    var cellSeparator = "^";
    $ele = jQuery(this);
    options.mustMatch = options.mustMatch || false;
    options.minChars = options.minChars || 3;
    if (options.row) {
	$ele.autocomplete(options.url, {
	    cellSeparator: cellSeparator,
	    noAuto: false,
            minChars: options.minChars,
	    selectFirst: true,
            extraParams: options.extraParams,
	    width: options.width,
	    resultsClass: options.resultsClass,
	    resultPosition: options.resultPosition,
	    scroll: options.scroll,
	    scrollHeight: options.scrollHeight,
	    showLeft: options.showLeft,
	    callBefore: function() {
		if (jQuery.trim(options.callBefore) !== "") {
		    eval(options.callBefore);
		}
	    },
	    onItemSelect: function($input, li) {
		var values = li.id.split(cellSeparator);
		$input.val(values[0]);
		var row = $input.parent().parent();
		if (row.find("." + options.checkClass).length > 0) {
		    row.find("." + options.checkClass).val(values[0]);
		}
		if (jQuery.trim(options.otherFields) !== "") {
		    var otherFields = options.otherFields.split(cellSeparator);
		    jQuery.each(otherFields, function(index, field) {
			row.find("." + field).val(values[index + 1]);
		    });
		}
		if (jQuery.trim(options.callback) !== "") {
		    eval(options.callback);
		}
		if (jQuery.trim(options.focusField) !== "") {
		    row.find("." + options.focusField).focus().fadeIn().fadeOut().fadeIn();
		}
	    },
	    onInputBlur: function($input) {
		var row = $input.parent().parent();
		if (row.find("." + options.checkClass).length > 0 && !$input.hasClass("readonly")
			&& jQuery.trim($input.val()) !== jQuery.trim(row.find("." + options.checkClass).val())) {
		    $input.val("");
		    row.find("." + options.checkClass).val("");
		    if (jQuery.trim(options.otherFields) !== "") {
			var otherFields = options.otherFields.split(cellSeparator);
			if (options.row) {
			    jQuery.each(otherFields, function(index, field) {
				row.find("." + field).val("");
			    });
			}
		    }
		    if (jQuery.trim(options.focusField) !== "") {
			row.find("." + options.focusField).focus().fadeIn().fadeOut().fadeIn();
		    }
		}
		if (jQuery.trim(options.onblur) !== "") {
		    eval(options.onblur);
		}
	    }
	});
    } else {
	$ele.autocomplete(options.url, {
	    cellSeparator: cellSeparator,
	    noAuto: false,
            minChars: options.minChars,
	    selectFirst: true,
	    width: options.width,
	    resultsClass: options.resultsClass,
	    resultPosition: options.resultPosition,
	    scroll: options.scroll,
	    scrollHeight: options.scrollHeight,
	    showLeft: options.showLeft,
	    callBefore: function() {
		if (jQuery.trim(options.callBefore) !== "") {
		    eval(options.callBefore);
		}
	    },
	    onItemSelect: function($input, li) {
		var values = li.id.split(cellSeparator);
		if (!options.mustMatch || (options.mustMatch && values[0].toUpperCase().indexOf(jQuery.trim($input.val().toUpperCase())) > -1)) {
		    $input.val(values[0]);
		    var eleId = $input.attr("id");
		    if (jQuery("#" + eleId + "Check").length > 0) {
			jQuery("#" + eleId + "Check").val(values[0]);
		    }
		    if (jQuery.trim(options.otherFields) !== "") {
			var otherFields = options.otherFields.split(cellSeparator);
			jQuery.each(otherFields, function(index, field) {
			    if (options.append) {
				jQuery("#" + field).val(jQuery("#" + field).val() + (jQuery.trim(jQuery("#" + field).val()) !== "" ? ";" : "") + values[index + 1]);
			    } else {
				jQuery("#" + field).val(values[index + 1]);
			    }
			});
		    }
		    if (jQuery.trim(options.callback) !== "") {
			eval(options.callback);
		    }
		    if (jQuery.trim(options.focusField) !== "") {
			if (jQuery(options.focusField).length > 0) {
			    jQuery(options.focusField).focus().fadeIn().fadeOut().fadeIn();
			} else {
			    jQuery("#" + options.focusField).focus().fadeIn().fadeOut().fadeIn();
			}
		    }
		}
	    },
	    onInputBlur: function($input) {
		var eleId = $input.attr("id");
		if (jQuery("#" + eleId + "Check").length > 0 && !$input.hasClass("readonly")
			&& jQuery.trim($input.val()) !== jQuery.trim(jQuery("#" + eleId + "Check").val())) {
		    $input.val("");
		    jQuery("#" + eleId + "Check").val("");
		    if (jQuery.trim(options.otherFields) !== "") {
			var otherFields = options.otherFields.split(cellSeparator);
			jQuery.each(otherFields, function(index, field) {
			    jQuery("#" + field).val("");
			});
		    }
		    if (jQuery.trim(options.focusField) !== "") {
			jQuery("#" + options.focusField).focus().fadeIn().fadeOut().fadeIn();
		    }
		}
		if (jQuery.trim(options.onblur) !== "") {
		    eval(options.onblur);
		}
	    }
	});
    }
};

jQuery.fn.callFocus = function() {
    jQuery(this).focus().fadeIn().fadeOut().fadeIn();
};

jQuery.fn.focusNextInputField = function() {
    return this.each(function() {
	var fields = jQuery(this).parents("form:eq(0),body").find("button,input[type!='hidden'],textarea,select,:input:not([readonly])").not(".readonly,.focusless,.hidden");
	var index = fields.index(this);
	jQuery(fields.eq(index + 1)).focus().fadeIn().fadeOut().fadeIn();
	return false;
    });
};

var freezeHeader = function(options) {
    options = options || {};
    options.container = options.container || ".search-results-container";
    options.body = options.body || ".search-results-body";
    options.header = options.header || ".search-results-header";
    jQuerytableBody = jQuery(options.body);
    jQuerytableHeader = jQuery(options.header);
    jQuerytableBody.find("tbody tr td").each(function(index) {
	var width = jQuery(this).width();
	jQuerytableHeader.find("thead th").eq(index).width(width);
    });
    var maxWidth = jQuery(options.container).width();
    var blankSpace = maxWidth - jQuerytableBody.width();
    jQuerytableHeader.find("thead th:last").width(jQuerytableHeader.find("thead th:last").width() + blankSpace);
    jQuerytableHeader.find("thead th:last").width(jQuerytableHeader.find("thead th:last").width() - 2);
}

function findPosition(obj) {
    var curleft = obj.offsetLeft || 0;
    var curtop = obj.offsetTop || 0;
    while ((obj = obj.offsetParent)) {
	curleft += obj.offsetLeft
	curtop += obj.offsetTop
    }
    return {
	x: curleft,
	y: curtop
    };
}

jQuery.fn.focusNextInput = function() {
    var $input = jQuery(this).find("input[type!='hidden'],textarea,select").not(".readonly").not(".hidden").not(":[tabindex=-1]");
    $input.keydown(function(event) {
	var totalIndex = $input.length;
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if (!event.shiftKey && (keycode === 9 || keycode === 13)) {
	    event.stopPropagation();
	    event.preventDefault();
	    var nextIndex = $input.index(this) + 1;
	    if (jQuery(this).is(":focus") && nextIndex < totalIndex && event.target === this) {
		jQuery($input[nextIndex]).focus().fadeIn().fadeOut().fadeIn().select();
	    }
	}
    });
}

function dynamicFunction(func) {
    window[func].apply(null, Array.prototype.slice.call(arguments, 1));
}

var ajaxCall = function(url, options) {
    options = options || {};
    options.top = options.top || "20%";
    options.left = options.left || "35%";
    options.width = options.width || "800px";
    options.height = options.height || "300px";
    options.overflowy = options.overflowy || "auto";
    options.async = options.async || true;
    jQuery.ajax({
	type: "POST",
	url: url + (url.indexOf("?") > -1 ? "&" : "?") + "date=" + new Date(),
	data: options.data,
	cache: false,
	async: options.async,
	beforeSend: function() {
	    if (options.preloading) {
		showPreloading();
	    }
	    if (options.callBefore) {
		dynamicFunction(options.callBefore);
	    }
	},
	success: function(data) {
	    if (options.preloading) {
		closePreloading();
	    }
	    if (options.success) {
		dynamicFunction(options.success, data);
	    }
	},
	error: function(jqXHR) {
	    if (options.preloading) {
		dynamicFunction("closePreloading");
	    }
	    if (jQuery.trim(jqXHR.responseText) !== "") {
		jQuery.prompt(jqXHR.responseText, {
		    top: options.top,
		    left: options.left,
		    width: options.width,
		    height: options.height,
		    overflowy: options.overflowy,
		    callback: function() {
			if (options.error) {
			    dynamicFunction(options.error);
			}
		    }
		});
	    }
	}
    });
};

jQuery.fn.validate = function() {
    jQuery(".invalid").remove();
    var $input = jQuery(this).find(":[validate]");
    var count = 0;
    var email = "^[A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}(?:[;][A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}?)*";
    $input.each(function() {
	var value = jQuery(this).val();
	var validate = jQuery(this).attr("validate").split(":");
	if (((validate.length === 2)) || (validate.length === 3 && validate[2] === "optional" && jQuery.trim(value) !== "")) {
	    if (validate[0] === "email" && String(value).search(email) === -1) {
		count++;
		jQuery(this).after("<div class='invalid float-left'>" + validate[1] + "</div>");
	    } else if (jQuery.trim(value) === "") {
		jQuery(this).after("<div class='invalid float-left'>" + validate[1] + "</div>");
		count++;
	    }
	}
    });
    return count > 0 ? false : true;
}