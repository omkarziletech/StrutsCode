<%@page import="com.gp.cong.common.Application"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@taglib prefix="fmt" uri="/WEB-INF/fmt-1_0-rt.tld"%> 
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/layout.css" />
    <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/datepicker/cal.css" />
    <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lightbox.css"/>
    <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"  />
    <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/validation/validate.css" />
    <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/js/colorbox/colorbox.css" />


    <script type="text/javascript" src="${path}/jsps/LCL/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="${path}/jsps/LCL/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${path}/jsps/LCL/js/datepicker/datepicker.js"></script>

    <script type="text/javascript" src="${path}/jsps/LCL/js/colorbox/jquery.colorbox.js"></script>

    <script type="text/javascript" src="${path}/jsps/LCL/js/validation/jquery.validate.js"></script>
    <script type="text/javascript" src="${path}/jsps/LCL/js/validation/jquery.maskedinput.js"></script>
    <!-- <script type="text/javascript" src="js/validation/mktSignup.js"></script> -->

    <script type='text/javascript' src='${path}/jsps/LCL/js/autocomplete/jquery.autocomplete.js'></script>
    <script type='text/javascript' src='${path}/jsps/LCL/js/autocomplete/localdata.js'></script>
    <link rel="stylesheet" type="text/css" href="${path}/jsps/LCL/js/autocomplete/main.css" />
    <link rel="stylesheet" type="text/css" href="${path}/jsps/LCL/js/autocomplete/jquery.autocomplete.css" />

    <script src="${path}/jsps/LCL/js/scriptjs.js" type="text/javascript" ></script>
    <script type="text/javascript" src="${path}/js/tooltip/tooltip.js" ></script>
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