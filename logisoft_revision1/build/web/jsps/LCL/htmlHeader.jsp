<%-- 
    Document   : htmlHeader
    Created on : Nov 7, 2011, 4:05:07 PM
    Author     : Thamizh
--%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<head>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>

    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <meta name="keywords" content="" />
    <meta name="description" content=" " />
    <meta name="author" content="LogiwareInc." />

    <title> Logiware - OT </title>
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

    <script type="text/javascript">
        $(document).ready(function() {
               
            $("#origin").autocomplete('origin.jsp?request', {
                matchContains: true,
                minChars: 0,
                width:200
            });
                
            $("#shipper").autocomplete(emails, {
                minChars: 0,
                width: 200,
                matchContains: "word",
                autoFill: false,
                formatItem: function(row, i, max) {
                    return "<span class='st1'>" + row.name + "</span> <span class='st2'>"  + row.to + "</span>";
                },
                formatMatch: function(row, i, max) {
                    return row.name + " " + row.to;
                },
                formatResult: function(row) {
                    return row.to;
                }
            });
                
            $(".tabLink").each(function(){
                $(this).click(function(){
                    tabeId = $(this).attr('id');
                    $(".tabLink").removeClass("activeLink");
                    $(this).addClass("activeLink");
                    $(".tabcontent").addClass("hide");
                    $("#"+tabeId+"-1").removeClass("hide")
                    return false;
                });
            });
            
            /* color Box */
            $(".addCommodity").colorbox({iframe:true, width:"90%", height:"90%", title:"Commodity"});
            
            $(".callbacks").colorbox({
                onOpen:function(){ alert('onOpen: colorbox is about to open'); },
                onLoad:function(){ alert('onLoad: colorbox has started to load the targeted content'); },
                onComplete:function(){ alert('onComplete: colorbox has displayed the loaded content'); },
                onCleanup:function(){ alert('onCleanup: colorbox has begun the close process'); },
                onClosed:function(){ alert('onClosed: colorbox has completely closed'); }
            });
                
                
            /*date picker */
            $('input.cal-text').simpleDatepicker({ startdate: 1990, enddate: 2016 });
            /*$('input.two').simpleDatepicker({ startdate: 1980, enddate: 2012 });
            $('input.three').simpleDatepicker({ chosendate: '9/9/2010', startdate: '6/10/2008', enddate: '7/20/2011' });
            $('input.four').simpleDatepicker({ x: 45, y: 3 });*/
                
        });
    </script>
</head>