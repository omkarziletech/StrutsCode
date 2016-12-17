<%-- 
    Document   : htmlHeader
    Created on : Nov 7, 2011, 4:05:07 PM
    Author     : Thamizh
--%>

<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/cong/" prefix="cong"%>
<%@taglib prefix="fmt" uri="/WEB-INF/fmt-1_0-rt.tld" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<head>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="keywords" content="" />
<meta name="description" content=" " />
<meta name="author" content="LogiwareInc." />

<link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/layout.css" />
<link type="text/css" rel="stylesheet" media="screen" href="${path}/css/common.css" />
<link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"/>
<link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/datepicker/cal.css" />
<link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lightbox.css"/>
<link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/validation/validate.css" />
<link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/js/colorbox/colorbox.css" />

<script type="text/javascript" src="${path}/jsps/LCL/js/calendar/datetimepicker_css.js"></script>
<%--<script type="text/javascript" src="${path}/jsps/LCL/js/commonjs.js"></script>--%>
<script type="text/javascript" src="${path}/js/common.js"></script>
<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
<script type="text/javascript" src="${path}/js/jquery/jquery.util.js"></script>
<script type="text/javascript" src="${path}/js/jquery/jquery.table-filter.js"></script>
<script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
<script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
<%--<script src="${path}/jsps/LCL/js/dropdownjs/jquery.min.js"></script>
<script src="${path}/jsps/LCL/js/dropdownjs/jquery-ui.min.js"></script>
<script src="${path}/jsps/LCL/js/dropdownjs/ui.dropdownchecklist-1.4-min.js"></script>--%>

<script type="text/javascript" src="${path}/jsps/LCL/js/alert/jquery-impromptu.3.2.js"></script>
<link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/alert/congAlert.css" />

<script type="text/javascript" src="${path}/jsps/LCL/js/colorbox/jquery.colorbox.js"></script>

<script type="text/javascript" src="${path}/jsps/LCL/js/validation/jquery.validate.js"></script>
<script type="text/javascript" src="${path}/jsps/LCL/js/validation/jquery.maskedinput.js"></script>

<script type='text/javascript' src='${path}/jsps/LCL/js/autocomplete/jquery.autocomplete.js'></script>
<script type='text/javascript' src='${path}/jsps/LCL/js/autocomplete/localdata.js'></script>
<link rel="stylesheet" type="text/css" href="${path}/jsps/LCL/js/autocomplete/main.css" />
<link rel="stylesheet" type="text/css" href="${path}/jsps/LCL/js/autocomplete/jquery.autocomplete.css" />

   <!-- <script src="${path}/jsps/LCL/js/scriptjs.js" type="text/javascript" ></script>
    <script type="text/javascript" src="${path}/js/tooltip/tooltip.js" ></script>-->
<!--new format tooltip-->
<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
<!--new format tooltip without bubble-->
<script type="text/javascript" src="${path}/jsps/LCL/js/tooltip/jquery.qtip-1.0.0-rc3.min.js" ></script>

<script type="text/javascript">

    $(document).ready(function() {
        function preventBackspace(e) {
            var doPrevent;
            if (e.keyCode === 8) {
                var d = e.srcElement || e.target;
                if (d.tagName.toUpperCase() === 'INPUT' || d.tagName.toUpperCase() === 'TEXTAREA') {
                    doPrevent = d.readOnly || d.disabled;
                }
                else {
                    doPrevent = true;
                }

            }
            else {
                doPrevent = false;
            }

            if (doPrevent) {
                e.preventDefault();
            }
        }
        jQuery(document).keydown(preventBackspace);
        $("[title != '']").not("link").tooltip();
        $(".tabLink").each(function () {
            $(this).click(function () {
                tabeId = $(this).attr('id');
                $(".tabLink").removeClass("activeLink");
                $(this).addClass("activeLink");
                $(".tabcontent").addClass("hide");
                $("#" + tabeId + "-1").removeClass("hide")
                return false;
            });

        });

        /*date picker */
        //            $('input.cal-text').simpleDatepicker({ startdate: 1990, enddate: 2016 });
        /*$('input.two').simpleDatepicker({ startdate: 1980, enddate: 2012 });
         $('input.three').simpleDatepicker({ chosendate: '9/9/2010', startdate: '6/10/2008', enddate: '7/20/2011' });
         $('input.four').simpleDatepicker({ x: 45, y: 3 });*/

    });
    function grayOut(vis, options) {
        var options = options || {};
        var zindex = options.zindex || 50;
        var opacity = options.opacity || 70;
        var opaque = (opacity / 100);
        var bgcolor = options.bgcolor || '#000000';
        var dark = document.getElementById('darkenScreenObject');
        if (!dark) {
            var tbody = document.getElementsByTagName("body")[0];
            var tnode = document.createElement('div');
            tnode.style.position = 'absolute';
            tnode.style.top = '0px';
            tnode.style.left = '0px';
            tnode.style.overflow = 'hidden';
            tnode.style.display = 'none';
            tnode.id = 'darkenScreenObject';
            tbody.appendChild(tnode);
            dark = document.getElementById('darkenScreenObject');
        }
        if (vis) {
            if (document.body && (document.body.scrollWidth || document.body.scrollHeight)) {
                var pageWidth = document.body.scrollWidth + 'px';
                var pageHeight = document.body.scrollHeight + 'px';
            } else if (document.body.offsetWidth) {
                var pageWidth = document.body.offsetWidth + 'px';
                var pageHeight = document.body.offsetHeight + 'px';
            } else {
                var pageWidth = '100%';
                var pageHeight = '100%';
            }
            dark.style.opacity = opaque;
            dark.style.MozOpacity = opaque;
            dark.style.filter = 'alpha(opacity=' + opacity + ')';
            dark.style.zIndex = zindex;
            dark.style.backgroundColor = bgcolor;
            dark.style.width = pageWidth;
            dark.style.height = pageHeight;
            dark.style.display = 'block';
        } else {
            dark.style.display = 'none';
        }
    }
    function showProgressBar() {
        grayOut(true, '');
        document.getElementById('newProgressBar').style.display = 'block';
        jQuery('#newProgressBar').alignMiddle();
    }
    function hideProgressBar() {
        document.getElementById('newProgressBar').style.display = 'none';
        grayOut(false, '');
    }

    function JToolTip(selector, message, width) {
        $(selector).qtip({
            content: message,
            style: {
                width: width,
                tip: {
                    corner: 'topLeft',
                    color: '#8DB7D6'
                },
                border: {
                    width: 1,
                    radius: 3,
                    color: '#8DB7D6'
                }
            },
            position: {
                corner: {
                    target: 'bottomRight',
                    tooltip: 'topLeft'
                }
            },
            show: 'mouseover',
            hide: 'mouseout'
        });
    }

    function JToolTipForTop(selector, message, width) {
        $(selector).qtip({
            content: message,
            style: {
                width: width,
                tip: {
                    corner: 'topLeft',
                    color: '#8DB7D6'
                },
                border: {
                    width: 1,
                    radius: 3,
                    color: '#8DB7D6'
                }
            },
            position: {
                corner: {
                    target: 'topLeft',
                    tooltip: 'bottomLeft'
                }
            },
            show: 'mouseover',
            hide: 'mouseout'
        });
    }

    function JToolTipForLeft(selector, message, width) {
        $(selector).qtip({
            content: message,
            style: {
                width: width,
                tip: {
                    corner: 'topRight',
                    color: '#8DB7D6'
                },
                border: {
                    width: 1,
                    radius: 3,
                    color: '#8DB7D6'
                }
            },
            position: {
                corner: {
                    target: 'bottomLeft',
                    tooltip: 'topRight'
                }
            },
            show: 'mouseover',
            hide: 'mouseout'
        });
    }

    function ImportsJToolTip(selector, message, width, currentPosition) {
        $(selector).qtip({
            content: message,
            style: {
                width: width,
                tip: {
                    corner: currentPosition,
                    color: '#8DB7D6'
                },
                border: {
                    width: 1,
                    radius: 3,
                    color: '#8DB7D6'
                }
            },
            position: {
                corner: {
                    target: 'bottomMiddle',
                    tooltip: currentPosition
                }
            },
            show: 'mouseover',
            hide: 'mouseout'
        });
    }

    jQuery.fn.alignMiddle = function () {
        this.css("position", "absolute");
        this.css("top", Math.max(0, ((jQuery(window).height() - jQuery(this).outerHeight()) / 2) + jQuery(window).scrollTop()) + "px");
        this.css("left", Math.max(0, ((jQuery(window).width() - jQuery(this).outerWidth()) / 2) + jQuery(window).scrollLeft()) + "px");
        return this;
    };
    function alertMsgByFocus(txt,ele){
        $.prompt(txt,{
            callback: function () {
                $(ele).focus();
                $(ele).css("border-color", "red");
            }
        });
    }
</script>
<body>
    <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
        <p class="progressBarHeader" style="width: 100%;padding-left:45px;"><b>Processing......Please Wait</b></p>

        <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
            <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
        </form>
    </div>
    <div id="bubble_tooltip" style="display: none;">
        <div class="bubble_top"><span></span></div>
        <div class="bubble_middle"><span id="bubble_tooltip_content"></span></div>
        <div class="bubble_bottom"></div>
    </div>
    <div id="bubble_tooltip_small" style="display: none;">
        <div class="bubble_small_top"><span></span></div>
        <div class="bubble_small_middle"><span id="bubble_tooltip_content_small"></span></div>
        <div class="bubble_small_bottom"></div>
    </div>
    <div id="bubble_tooltip_ForTop" style="display: none;">
        <div class="bubble_top_ForTop"><span></span></div>
        <div class="bubble_middle_ForTop"><span id="bubble_tooltip_content_ForTop"></span></div>
        <div class="bubble_bottom_ForTop"></div>
    </div>
</body>
<style type="text/css">
</style>
</head>
