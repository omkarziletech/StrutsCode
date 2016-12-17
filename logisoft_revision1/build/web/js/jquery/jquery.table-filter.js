/* 
 *  Document   : jquery.table-filter
 *  Created on : Jul 31, 2015, 11:46:00 AM
 *  Author     : Lakshmi Narayanan
 */
var path = "/" + window.location.pathname.split('/')[1];

(function ($) {
    $.fn.extend({
        tablefilter: function (options) {
            //set default options
            options = $.extend({
                container: "#result-table",
                class: "table-filter"
            }, options);
            //set table rows as visible
            $(options.container + " tbody tr").show();
            //add mask for the filter
            var mask = $("<div/>").css({
                background: "black",
                opacity: "0.25",
                filter: "alpha(opacity = 25)",
                position: "absolute",
                top: "0px",
                left: "0px",
                width: "100%",
                overflow: "auto",
                "z-index": "39999",
                display: "none"
            }).click(function () {
                //on click mask hide the filter and the mask itself
                $("." + options.class + ":visible").slideToggle(function () {
                    mask.hide();
                });
            });
            $("body").append(mask);
            var filtered = [];
            var triggers = $(this);
            triggers.each(function () {
                var trigger = $(this);
                trigger.data("current", false);
                var values = [];
                var index = trigger.parent().index();
                //get values from the columns
                $(options.container).find(" tbody tr").each(function () {
                    $(this).find("td:eq(" + index + ")").each(function () {
                        var value = $(this).text().replace(/\s/g, "");
                        if ($.inArray($.trim(value), values) < 0) {
                            values.push($.trim(value));
                        }
                    });
                });
                //add filter only if there are values in the column
                if (values.length > 0) {
                    //sort the values
                    values.sort();
                    //create filter div
                    var filter = $("<div/>").attr("id", "table-filter" + index)
                            .addClass(options.class)
                            .css("display", "none");
                    filter.data("field", trigger.attr("data-field"));
                    filter.data("index", index);
                    filter.data("current", false);
                    //create sub-filter div and textbox to fiter the filter values
                    var subfilterdiv = $("<div/>");
                    subfilterdiv.append("<label class='label float-left'>Filter:&nbsp;</label>");
                    var subfilter = $("<input/>").attr({
                        type: "text",
                        value: "",
                        class: "textbox float-left filter-input"
                    }).bind("input propertychange", function () {
                        //show checkboxes which matches the value entered
                        var filterlist = $("#table-filter" + index + " ul li");
                        if ($(this).val() !== "") {
                            var filtered = filterlist.filter(":icontains('" + $(this).val() + "')").show();
                            filtered.find(".checkbox").attr("checked", true);
                            filterlist.not(filtered).hide();
                            filterlist.not(filtered).find(".checkbox").removeAttr("checked");
                            filterlist.find("#selectall").parent().show();
                            if (filterlist.find(".checkbox:checked:not('#selectall')").length === 0) {
                                filterlist.find("#selectall").prop("indeterminate", false).removeAttr("checked");
                            } else if (filterlist.find(".checkbox:checked:not('#selectall')").length
                                    !== filterlist.find(".checkbox:not('#selectall')").length) {
                                filterlist.find("#selectall").prop("indeterminate", true).removeAttr("checked");
                            } else {
                                filterlist.find("#selectall").prop("indeterminate", false).attr("checked", true);
                            }
                        } else {
                            filterlist.show().find(".checkbox").attr("checked", true);
                            filterlist.find("#selectall").prop("indeterminate", false).attr("checked", true);
                        }
                    });
                    subfilterdiv.append(subfilter);
                    filter.append(subfilterdiv).append("<br/>");
                    //create filter list
                    var li = [];
                    li.push("<li class='selectall'><input type='checkbox' class='checkbox' checked value='' id='selectall'/><label class='bold'>Select All</label></li>");
                    $(values).each(function () {
                        li.push("<li><input type='checkbox' class='checkbox' checked value='" + this + "'/><label>" + (this.length !== 0 ? this : "[Blanks]") + "</label></li>");
                    });
                    var list = $("<div/>").html("<ul>" + li.join("") + "</ul>");
                    filter.append(list);
                    var filterlist = filter.find("ul li");
                    filterlist.find("#selectall").click(function () {
                        $(this).prop("indeterminate", false);
                        if ($(this).is(":checked")) {
                            filterlist.find(".checkbox").attr("checked", true);
                        } else {
                            filterlist.find(".checkbox").removeAttr("checked");
                        }
                    });
                    filterlist.find(".checkbox:not('#selectall')").click(function () {
                        if (filterlist.find(".checkbox:checked:not('#selectall')").length === 0) {
                            filterlist.find("#selectall").prop("indeterminate", false).removeAttr("checked");
                        } else if (filterlist.find(".checkbox:checked:not('#selectall')").length
                                !== filterlist.find(".checkbox:not('#selectall')").length) {
                            filterlist.find("#selectall").prop("indeterminate", true).removeAttr("checked");
                        } else {
                            filterlist.find("#selectall").prop("indeterminate", false).attr("checked", true);
                        }
                    });
                    //create buttons - filter, clear and cancel
                    var buttons = $("<div/>").addClass("align-center");
                    //create filter button
                    var submit = $("<input/>").attr({
                        type: "button",
                        value: "Filter",
                        class: "button"
                    }).click(function () {
                        if ($("#table-filter" + index).find(".checkbox:not('#selectall')").length > 0) {
                            var current = null;
                            if (filtered.length > 0) {
                                var findex = null;
                                $.each(filtered, function (i) {
                                    if (this.current) {
                                        current = this;
                                        findex = i;
                                        this.current = false;
                                    }
                                });
                                if (findex !== null && current.index === index) {
                                    delete filtered[findex];
                                }
                            }
                            $("." + options.class).data("current", false);
                            var curindex = index;
                            if (filtered.length === 0 || (current !== null && current.index !== index)) {
                                if ($("#table-filter" + index).find(".checkbox:checked:not('#selectall')").length !== $("#table-filter" + index).find(".checkbox:not('#selectall')").length) {
                                    $("#table-filter" + index).data("current", true);
                                    var cvalues = [];
                                    $("#table-filter" + index).find(".checkbox:not('#selectall')").each(function () {
                                        cvalues.push({checked: $(this).is(":checked"), value: $.trim($(this).val())});
                                    });
                                    filtered.push({index: index, values: cvalues, current: true});
                                } else {
                                    var previous = filtered[filtered.length - 1];
                                    if (previous !== null && $.type(previous) !== "undefined") {
                                        curindex = previous.index;
                                    }
                                }
                            } else if (current !== null && current.index === index
                                    && $("#table-filter" + index).find(".checkbox:checked:not('#selectall')").length !== current.values.length) {
                                filtered.pop();
                                $("#table-filter" + index).data("current", true);
                                var cvalues = [];
                                $("#table-filter" + index).find(".checkbox:not('#selectall')").each(function () {
                                    cvalues.push({checked: $(this).is(":checked"), value: $.trim($(this).val())});
                                });
                                filtered.push({index: index, values: cvalues, current: true});
                            } else if (current !== null && current.index === index
                                    && $("#table-filter" + index).find(".checkbox:checked:not('#selectall')").length === current.values.length) {
                                filtered.pop();
                                var previous = filtered[filtered.length - 1];
                                if (previous !== null && $.type(previous) !== "undefined") {
                                    $("#table-filter" + previous.index).data("current", true);
                                    curindex = previous.index;
                                    var li = [];
                                    li.push("<li class='selectall'><input type='checkbox' class='checkbox' checked value='' id='selectall'/><label class='bold'>Select All</label></li>");
                                    var cvalues = [];
                                    $(previous.values).each(function () {
                                        li.push("<li><input type='checkbox' class='checkbox' " + (this.checked ? "checked" : "") + " value='" + this.value + "'/><label>" + (this.value.length !== 0 ? this.value : "[Blanks]") + "</label></li>");
                                        cvalues.push({checked: this.checked, value: this.value});
                                    });
                                    filtered.pop();
                                    filtered.push({index: previous.index, values: cvalues, current: true});
                                    $("#table-filter" + previous.index).find("ul").html(li.join(""));
                                    var filterlist = $("#table-filter" + previous.index).find("ul li");
                                    filterlist.find("#selectall").click(function () {
                                        $(this).prop("indeterminate", false);
                                        if ($(this).is(":checked")) {
                                            filterlist.find(".checkbox").attr("checked", true);
                                        } else {
                                            filterlist.find(".checkbox").removeAttr("checked");
                                        }
                                    });
                                    filterlist.find(".checkbox:not('#selectall')").click(function () {
                                        if (filterlist.find(".checkbox:checked:not('#selectall')").length === 0) {
                                            filterlist.find("#selectall").prop("indeterminate", false).removeAttr("checked");
                                        } else if (filterlist.find(".checkbox:checked:not('#selectall')").length
                                                !== filterlist.find(".checkbox:not('#selectall')").length) {
                                            filterlist.find("#selectall").prop("indeterminate", true).removeAttr("checked");
                                        } else {
                                            filterlist.find("#selectall").prop("indeterminate", false).attr("checked", true);
                                        }
                                    });
                                    if (filterlist.find(".checkbox:checked:not('#selectall')").length === 0) {
                                        filterlist.find("#selectall").prop("indeterminate", false).removeAttr("checked");
                                    } else if (filterlist.find(".checkbox:checked:not('#selectall')").length
                                            !== filterlist.find(".checkbox:not('#selectall')").length) {
                                        filterlist.find("#selectall").prop("indeterminate", true).removeAttr("checked");
                                    } else {
                                        filterlist.find("#selectall").prop("indeterminate", false).attr("checked", true);
                                    }
                                } else {
                                    filtered.length = 0;
                                }
                            }
                            if (filtered.length > 0) {
                                $("." + options.class + ":visible").slideToggle(function () {
                                    mask.hide();
                                });
                                $("#table-filter" + curindex).data("current", true);
                                var fvalues = [];
                                $.each(filtered, function () {
                                    var filter = $("#table-filter" + this.index);
                                    var cvalues = [];
                                    filter.find(".checkbox:checked:not('#selectall')").each(function () {
                                        cvalues.push($(this).val());
                                    });
                                    fvalues.push({
                                        field: filter.data("field"),
                                        index: filter.data("index"),
                                        cvalues: cvalues
                                    });
                                });
                                var rows = $(options.container + " tbody tr");
                                rows.removeClass("filtered");
                                rows.show();
                                $.each(fvalues, function () {
                                    var field = this.field;
                                    var index = this.index;
                                    var cvalues = this.cvalues;
                                    rows = $(options.container + " tbody tr:visible");
                                    rows.each(function () {
                                        $(this).find("td:eq(" + index + ")").each(function () {
                                            var value = $(this).text().replace(/\s/g, "");
                                            if ($.inArray($.trim(value), cvalues) >= 0) {
                                                $(this).parent().addClass("filtered");
                                            } else {
                                                $(this).parent().removeClass("filtered");
                                            }
                                        });
                                    });
                                    var filteredrows = rows.filter(".filtered").show();
                                    rows.not(filteredrows).hide();
                                });
                            } else {
                                $("#table-filter" + curindex).find(".clear").click();
                            }
                            trigger.attr("src", path + "/images/icons/filter-yellow.png");
                        } else {
                            $("." + options.class + ":visible").slideToggle(function () {
                                mask.hide();
                            });
                        }
                    });
                    buttons.append(submit);
                    //create clear button
                    var clear = $("<input/>").attr({
                        type: "button",
                        value: "Clear",
                        class: "button clear"
                    }).click(function () {
                        $("." + options.class).data("current", false);
                        $(options.container + " tbody tr").removeClass("filtered").show();
                        $("." + options.class + ":visible").slideToggle(function () {
                            $("." + options.class + " ul li").show().find(".checkbox").attr("checked", true);
                            mask.hide();
                        });
                        filtered.length = 0;
                        triggers.attr("src", path + "/images/icons/filter.png");
                    });
                    buttons.append(clear);
                    //create cancel button
                    var cancel = $("<input/>").attr({
                        type: "button",
                        value: "Cancel",
                        class: "button"
                    }).click(function () {
                        mask.hide();
                        $("." + options.class + ":visible").slideToggle();
                    });
                    buttons.append(cancel);
                    filter.append(buttons);
                    $(document.body).append(filter);

                    trigger.click(function () {
                        mask.css({
                            width: $(document).width(),
                            height: $(document).height()
                        }).show();
                        if ($("#table-filter" + index).data("current") === false) {
                            var values = [];
                            $(options.container).find(" tbody tr:visible").each(function () {
                                $(this).find("td:eq(" + index + ")").each(function () {
                                    var value = $(this).text().replace(/\s/g, "");
                                    if ($.inArray($.trim(value), values) < 0) {
                                        values.push($.trim(value));
                                    }
                                });
                            });
                            values.sort();
                            var li = [];
                            li.push("<li class='selectall'><input type='checkbox' class='checkbox' checked value='' id='selectall'/><label class='bold'>Select All</label></li>");
                            $(values).each(function () {
                                li.push("<li><input type='checkbox' class='checkbox' checked value='" + this + "'/><label>" + (this.length !== 0 ? this : "[Blanks]") + "</label></li>");
                            });
                            $("#table-filter" + index).find("ul").html(li.join(""));
                            var filterlist = $("#table-filter" + index).find("ul li");
                            filterlist.find("#selectall").click(function () {
                                $(this).prop("indeterminate", false);
                                if ($(this).is(":checked")) {
                                    filterlist.find(".checkbox").attr("checked", true);
                                } else {
                                    filterlist.find(".checkbox").removeAttr("checked");
                                }
                            });
                            filterlist.find(".checkbox:not('#selectall')").click(function () {
                                if (filterlist.find(".checkbox:checked:not('#selectall')").length === 0) {
                                    filterlist.find("#selectall").prop("indeterminate", false).removeAttr("checked");
                                } else if (filterlist.find(".checkbox:checked:not('#selectall')").length
                                        !== filterlist.find(".checkbox:not('#selectall')").length) {
                                    filterlist.find("#selectall").prop("indeterminate", true).removeAttr("checked");
                                } else {
                                    filterlist.find("#selectall").prop("indeterminate", false).attr("checked", true);
                                }
                            });
                        }
                        $("." + options.class + ":visible").slideToggle();
                        $("#table-filter" + index).find(".filter-input").val("");
                        $("#table-filter" + index + " ul li").show();
                        $("#table-filter" + index).css({
                            "min-width": $(this).parent().width() + "px",
                            top: ($(this).offset().top + 15) + "px",
                            left: ($(this).parent().offset().left) + "px"
                        }).slideToggle(function () {
                            $("#table-filter" + index).find(".filter-input").focus();
                        });
                    });
                }
            });
        }
    });
})(jQuery);