jQuery.autocomplete = function(input, options) {
    // Create a link to self
    var me = this;

    // Create jQuery object for input element
    var $input = $(input).attr("autocomplete", "off");

    // Apply inputClass if necessary
    if (options.inputClass) $input.addClass(options.inputClass);

    // Create results
    var results = document.createElement("div");
    // Create jQuery object for results
    var $results = $(results);
    $results.hide().addClass(options.resultsClass).css("position", "absolute");
    if( options.width > 0 ) $results.css("width", options.width);

    // Add to body element
    $("body").append(results);

    input.autocompleter = me;
    var timeout = null;
    var prev = "";
    var active = -1;
    var hasFocus = false;
    var lastKeyPressCode = null;

    // if there is a data array supplied
    if( options.data != null ){
        var sFirstChar = "", stMatchSets = {}, row = [];

        // loop through the array and create a lookup structure
        for( var i=0; i < options.data.length; i++ ){
            // if row is a string, make an array otherwise just reference the array
            row = ((typeof options.data[i] == "string") ? [options.data[i]] : options.data[i]);

            // if the length is zero, don't add to list
            if( row[0].length > 0 ){
                // get the first character
                sFirstChar = row[0].substring(0, 1).toLowerCase();
                // if no lookup array for this character exists, look it up now
                if( !stMatchSets[sFirstChar] ) stMatchSets[sFirstChar] = [];
                // if the match is a string
                stMatchSets[sFirstChar].push(row);
            }
        }
    }

    $input.keydown(function(e) {
        // track last key pressed
        lastKeyPressCode = e.keyCode;
        switch(e.keyCode) {
            case 38: // up
                e.preventDefault();
                moveSelect(-1);
                break;
            case 40: // down
                e.preventDefault();
                moveSelect(1);
                break;
            case 9:  // tab
            case 13: // return
                if( selectCurrent() ){
                    // make sure to blur off the current field
                    $input.get(0).blur();
                    e.preventDefault();
                }
                break;
            default:
                active = -1;
                if (timeout) clearTimeout(timeout);
                timeout = setTimeout(function(){
                    onChange();
                }, options.delay);
                break;
        }
    }).focus(function(){
        // track whether the field has focus, we shouldn't process any results if the field no longer has focus
        hasFocus = true;
    }).blur(function() {
        // track whether the field has focus
        hasFocus = false;
        hideResults();
    }).bind("setOptions", function() {
        $.extend(options, arguments[1]);
    }).bind("paste", function () {
        // mouse right click paste
        active = -1;
        if (timeout)
            clearTimeout(timeout);
        timeout = setTimeout(function () {
            onChange();
        }, options.delay);
    });

    hideResultsNow();

    function onChange() {
        if(options.noAuto){
            return;
        }else{
            // ignore if the following keys are pressed: [del] [shift] [capslock]
            if(lastKeyPressCode == 46 || (lastKeyPressCode > 8 && lastKeyPressCode < 32))
                return $results.hide();
            var v = jQuery.trim($input.val());
            if (v == prev)
                return;
            prev = v;
            if (v==="%" ||v==="%%" || v.length >= options.minChars) {
                $input.addClass(options.loadingClass);
                return requestData(v);
            } else {
                $input.removeClass(options.loadingClass);
                return $results.hide();
            }
        }
    };

    function moveSelect(step) {
        var lis = $("li", results);
        if (!lis) return;
        active += step;
        if (active < 0) {
            active = 0;
        } else if (active >= lis.size()) {
            active = lis.size() - 1;
        }
        lis.removeClass("ac_over");
        $(lis[active]).addClass("ac_over");
    };

    function selectCurrent() {
        var li = $("li.ac_over", results)[0];
        if (!li) {
            var $li = $("li", results);
            if (options.selectOnly) {
                if ($li.length == 1) li = $li[0];
            } else if (options.selectFirst) {
                li = $li[0];
            }
        }
        if (li) {
            selectItem(li);
            return true;
        } else {
            return false;
        }
    };

    function selectItem(li) {
        if (!li) {
            li = document.createElement("li");
            li.selectValue = "";
        }
        if (options.onItemSelect) setTimeout(function() {
            options.onItemSelect(li)
        }, 1);
        $results.html("");
        hideResultsNow();
    };

    // selects a portion of the input string
    function createSelection(start, end){
        // get a reference to the input element
        var field = $input.get(0);
        if( field.createTextRange ){
            var selRange = field.createTextRange();
            selRange.collapse(true);
            selRange.moveStart("character", start);
            selRange.moveEnd("character", end);
            selRange.select();
        } else if( field.setSelectionRange ){
            field.setSelectionRange(start, end);
        } else {
            if( field.selectionStart ){
                field.selectionStart = start;
                field.selectionEnd = end;
            }
        }
        field.focus();
    };

    // fills in the input box w/the first match (assumed to be the best match)
    function autoFill(sValue){
        // if the last user key pressed was backspace, don't autofill
        if( lastKeyPressCode != 8 ){
            // fill in the value (keep the case the user has typed)
            $input.val($input.val() + sValue.substring(prev.length));
            // select the portion of the value not typed by the user (so the next character will erase)
            createSelection(prev.length, sValue.length);
        }
    };

    function showResults() {
        // get the position of the input field right now (in case the DOM is shifted)
        var pos = findPos(input);
        // either use the specified width, or autocalculate based on form element
        var iWidth = (options.width > 0) ? options.width : $input.width();
        // reposition
        $results.css({
            width: parseInt(iWidth) + "px",
            top: (pos.y + input.offsetHeight) + "px",
            left: pos.x + "px"
        }).show();
    };

    function hideResults() {
        if (timeout) clearTimeout(timeout);
        timeout = setTimeout(hideResultsNow, 200);
    };

    function hideResultsNow() {
        if (timeout) clearTimeout(timeout);
        $input.removeClass(options.loadingClass);
        if ($results.is(":visible")) {
            $results.hide();
        }
        if (options.mustMatch) {
            var v = $input.val();
            if (v != input.lastSelected) {
                selectItem(null);
            }
        }
    };

    function receiveData(data) {
        if (data) {
            $input.removeClass(options.loadingClass);
            results.innerHTML = "";
            // if the field no longer has focus or if there are no matches, do not display the drop down
            //@modified if(!hasFocus || data.length == 0){
            if(data.length == 0){
                return hideResultsNow();
            }
            dataToDom(data);
            return showResults();
        } else {
            return hideResultsNow();
        }
    };

    function dataToDom(data) {
        var ul = document.createElement("ul");
        $ul = $(ul);
        $ul.html(data);
        if($ul.children("li").length==1){
            $li = $($ul.children("li"));
            $li.addClass("ac_over");
            $li.click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                selectItem(this);
            });
        }else{
            $.each($ul.children("li"), function(){
                $li = $(this);
                $li.hover(
                    function() {
                        $("li", ul).removeClass("ac_over");
                        $(this).addClass("ac_over");
                        active = $("li", ul).indexOf($(this).get(0));
                    },
                    function() {
                        $(this).removeClass("ac_over");
                    }).click(function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    selectItem(this);
                });
            });
        }
        $(results).append($ul);
    };

    function requestData(q) {
        if (!options.matchCase) q = q.toLowerCase();
        // if an AJAX url has been supplied, try loading the data now
        if( (typeof options.url == "string") && (options.url.length > 0) ){
            if(options.callBefore){
                options.callBefore();
            }
            $.get(makeUrl(q), function(data) {
                if(null!=data && $.trim(data)!=""){
                    receiveData(data);
                }else{
                    if(options.shouldMatch){
                        $input.val("");
                    }
                    $results.hide();
                }
            });
        // if there's been no data found, remove the loading class
        } else {
            $input.removeClass(options.loadingClass);
        }
    };

    function makeUrl(q) {      
        var url = options.url + "?q=" + encodeURI(q);
        for (var i in options.extraParams) {
            url += "&" + i + "=" + encodeURI(options.extraParams[i]);
        }
        return url;
    };


    function matchSubset(s, sub) {
        if (!options.matchCase) s = s.toLowerCase();
        var i = s.indexOf(sub);
        if (i == -1) return false;
        return i == 0 || options.matchContains;
    };

    this.setExtraParams = function(p) {
        options.extraParams = p;
    };

    this.findValue = function(){
        var q = $input.val();
        if (!options.matchCase) q = q.toLowerCase();
        if( (typeof options.url == "string") && (options.url.length > 0) ){
            $.get(makeUrl(q), function(data) {
                data = parseData(data)
                findValueCallback(q, data);
            });
        } else {
            // no matches
            findValueCallback(q, null);
        }
    }

    function findPos(obj) {
        var curleft = obj.offsetLeft || 0;
        var curtop = obj.offsetTop || 0;
        while (obj = obj.offsetParent) {
            curleft += obj.offsetLeft
            curtop += obj.offsetTop
        }
        return {
            x:curleft,
            y:curtop
        };
    }
}

jQuery.fn.autocomplete = function(url, options, data) {
    // Make sure options exists
    options = options || {};
    // Set url as option
    options.url = url;
    // set some bulk local data
    options.data = ((typeof data == "object") && (data.constructor == Array)) ? data : null;

    // Set default values for required options
    options.inputClass = options.inputClass || "ac_input";
    options.resultsClass = options.resultsClass || "ac_results";
    options.lineSeparator = options.lineSeparator || "\n";
    options.cellSeparator = options.cellSeparator || "|";
    options.minChars = options.minChars || 3;
    options.delay = options.delay || 400;
    options.matchCase = options.matchCase || 0;
    options.matchSubset = options.matchSubset || 1;
    options.matchContains = options.matchContains || 0;
    options.mustMatch = options.mustMatch || 0;
    options.extraParams = options.extraParams || {};
    options.loadingClass = options.loadingClass || "ac_loading";
    options.selectFirst = options.selectFirst || false;
    options.selectOnly = options.selectOnly || false;
    options.maxItemsToShow = options.maxItemsToShow || -1;
    options.autoFill = options.autoFill || false;
    options.width = parseInt(options.width, 10) || 0;
    options.noAuto = false;
    this.each(function() {
        var input = this;
        new jQuery.autocomplete(input, options);
    });

    // Don't break the chain
    return this;
}
jQuery.fn.setOptions= function(options) {
    return this.trigger("setOptions",[options]);
}
jQuery.fn.autocompleteArray = function(data, options) {
    return this.autocomplete(null, options, data);
}

jQuery.fn.indexOf = function(e){
    for( var i=0; i<this.length; i++ ){
        if( this[i] == e ) return i;
    }
    return -1;
};
