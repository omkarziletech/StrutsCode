<%-- 
    Document   : layout
    Created on : Jul 31, 2010, 6:34:42 PM
    Author     : Lakshmi Naryanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <%@include file="../includes/resources.jsp"%>
        <base href="${basePath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logiware</title>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
	<link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
    </head>
    <body onbeforeunload="doUnload()" onmousedown="somefunction()">
        <c:choose>
            <c:when test="${not empty loginuser}">
                <iframe frameborder="0" id="homeFrame" name="homeFrame" src='${path}/home.do?action=showHome' width='100%' height='100%' style="margin: -10px  -10px -30px -10px"></iframe>
            </c:when>
            <c:otherwise>
                <c:redirect url="/jsps/login.jsp"/>
            </c:otherwise>
        </c:choose>
	<script type="text/javascript">
	    var isClose = false;
	    document.onkeydown = checkKeycode
	    function checkKeycode(e) {
		var keycode;
		if (window.event)
		    keycode = window.event.keyCode;
		else if (e)
		    keycode = e.which;
		if(keycode == 116){
		    isClose = true;
		}
	    }
	    function somefunction(){
		isClose = true;
	    }
	    function doUnload(){
		if(!isClose){
		    window.location = "${path}/logout.do";
		}
	    }

	    function showGreyBox(caption,path){
		var height = jQuery(document).height()-50;
		var width = jQuery(document).width()-100;
		var version = jQuery.browser.version;
		if(jQuery.browser.msie && parseFloat(version)>=9.0){
		    window.open(path,"mywindow","menubar=1,resizable=1,width="+width+",height="+height);
		}else{
		    GB_show(caption,path,height,width);
		}
	    }

	    function showLightBox(title,url,height,width){
		var height = height ? height : jQuery(document).height()-50;
		var width = width ? width : jQuery(document).width()-100;
		url += (url.indexOf("?")>-1?"&":"?")+"TB_iframe&height="+height+"&width="+width;
		Lightbox.showPopUp(title, url, "sexylightbox", "","");
	    }

	    jQuery(document).ready(function(){
		$("#homeFrame").height($(document).height());
		$("#homeFrame").width($(document).width()-2);
		$(window).resize(function() {
		    $("#homeFrame").height($(window).height()-20);
		    $("#homeFrame").width($(window).width()-2);
		});
		Lightbox.initialize({
		    color:'black',
		    dir : '${path}/js/lightbox/images',
		    moveDuration : 1,
		    resizeDuration : 1
		});
	    });
	</script>
    </body>
</html>
