/* 
 *  Document   : jquery.util
 *  Created on : Jun 04, 2014, 11:33:00 PM
 *  Author     : Lakshmi Narayanan
 */
(function ($) {
    $.fn.extend({
        textarea: function (options) {
            options = $.extend({
                height: "200px",
                width: "590px",
                cols: 25,
                rows: 12,
                limit: 250,
                target: "form",
                targetaction: "submit"
            }, options);
            options.id = $(this).attr("id");
            options.wrapid = "textarea-" + $(this).attr("id");
            $(this).attr({
                cols: options.cols,
                rows: options.rows
            }).css({
                "outline": "none",
                "border": "none",
                "margin": "0",
                "padding": "0",
                "overflow": "hidden",
                "white-space": "pre-wrap"
            });
            $(this).wrap("<div class='textarea' id='" + options.wrapid + "'></div>")
                    .focus(function () {
                        $("#" + options.wrapid).css({
                            "outline": "2px solid lightblue",
                            "border": "none"
                        });
                    })
                    .blur(function () {
                        $("#" + options.wrapid).css({
                            "border": "1px solid #C4C5C4",
                            "outline": "none"
                        });
                    })
                    .bind("input propertychange", function (e) {
                        if ($(this).val().length > options.limit) {
                            $(this).val($(this).val().substr(0, options.limit));
                            e.preventDefault();
                        }
                    });
            $("#" + options.wrapid).css({
                "height": options.height,
                "width": options.width
            });
            $(options.target).bind(options.targetaction, function () {
                var text = $("#" + options.id).val();
                var lines = text.split("\n");
                var newtext = "";
                var currenttext = "";
                for (var l = 0; l < lines.length; l++) {
                    var textarray = lines[l].split(" ");
                    for (var i = 0; i < textarray.length; i++) {
                        var str = textarray[i];
                        if (currenttext.length + str.length > options.cols) {
                            if ($.trim(newtext) === "") {
                                newtext = currenttext;
                            } else {
                                newtext += "\n" + currenttext;
                            }
                            if (textarray.length - i === 1) {
                                newtext += "\n" + str;
                            } else {
                                currenttext = str;
                            }
                        } else {
                            if (textarray.length - i === 1) {
                                if ($.trim(newtext) !== "") {
                                    newtext += "\n";
                                }
                                if (currenttext.length + str.length > options.cols) {
                                    newtext += currenttext + "\n" + str;
                                } else {
                                    newtext += currenttext + " " + str;
                                }
                            } else {
                                if ($.trim(currenttext) === "") {
                                    currenttext = str;
                                } else {
                                    currenttext += " " + str;
                                }
                            }
                        }
                    }
                    currenttext = "";
                }
                $("#" + options.id).val(newtext);
            });
        },
        focuz: function () {
            $(this).focus().fadeIn().fadeOut().fadeIn();
        },
        limit: function (charLimit) {
            return this.bind("input propertychange", function (e) {
                if ($(this).val().length > charLimit) {
                    $(this).val($(this).val().substr(0, charLimit));
                    e.preventDefault();
                    e.stopPropagation();
                }
            });
        },
        numberOnly: function () {
            return this.bind("input propertychange", function (e) {
                var start = this.selectionStart;
                var end = this.selectionEnd;
                var substr = $(this).val().substr(0, start);
                var newstart = substr.replace(/[^\d]/gi, '').length;
                var newend = end - (start - newstart);
                $(this).val($(this).val().replace(/[^\d]/gi, ''));
                this.setSelectionRange(newstart, newend);
            });

        },
        alignCenter: function () {
            this.css("position", "absolute");
            this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + $(window).scrollTop()) + "px");
            this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft()) + "px");
            return this;
        }
    });
    $.validateXML = function (value) {
        var errors = new Array();
        var characterEntities = value.match(new RegExp("&#\\d+;", "g"));
        if (null !== characterEntities && characterEntities.length > 0) {
            errors.concat(characterEntities);
        }
        var supported = new Array();
        var AtoF = ["A", "B", "C", "D", "E", "F"];
        for (var i = 0; i <= 9; i++) {
            for (var j = 0; j <= 9; j++) {
                supported.push(i + "" + j);
            }
            $(AtoF).each(function (k, atof) {
                supported.push(i + "" + atof);
            });
        }
        $(AtoF).each(function (k, i) {
            for (var j = 0; j <= 9; j++) {
                supported.push(i + "" + j);
            }
            $(AtoF).each(function (k, atof) {
                supported.push(i + "" + atof);
            });
        });
        var unsupported = ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0B", "0C", "0E", "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "3F", "21", "3C", "3E"];
        supported = $.grep(supported, function (n, i) {
            return $.inArray(n, unsupported) === -1;
        });
        var chars = value.split("");
        for (var i = 0; i < chars.length; i++) {
            if ($.inArray(chars[i].charCodeAt(0).toString(16).toUpperCase(), supported) === -1 && $.inArray(chars[i].toString(16).toUpperCase(), errors) === -1) {
                errors.push(chars[i]);
            }
        }
        return errors;
    };
    $.extend($.expr[":"], {
        "icontains": function (elem, i, match, array) {
            return (elem.textContent || elem.innerText || "").toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
        }
    });
})(jQuery);