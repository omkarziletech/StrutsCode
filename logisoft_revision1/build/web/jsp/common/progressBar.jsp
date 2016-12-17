<%-- 
    Document   : progressBar
    Created on : Nov 1, 2013, 9:47:14 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Progress Bar</title>
	<style type='text/css'>
	    body { 
		font-size: 13px; 
		font-family: Tahoma; 
	    }

	    #wrap { 
		width: 100%;
		height: 200px;
	    }

	    .progress {
		border-radius: 6px;    
		background: #efefef;
		border: 1px solid #db7a15;
	    }

	    .progress .bar {
		width: 1%;
		height: 2%;
		color: #fff; 
		font-size: 11px; 
		font-weight: bold; 
		line-height: 20px;
		background: green;
		text-align: center;
		border-radius: 6px;
		text-shadow: 0px 0px 3px #000;
	    }
	    .status{
		height: 98%;
		overflow: auto;
	    }
	    .status ul{
		margin: 0;
		padding: 0;
		list-style-type: none;
	    }
	    .status ul li{
		padding: 0 0 0 20px;
	    }
	    .status ul li.yes{
		background: url(${path}/images/icons/yes.png) left no-repeat;
	    }
	    .status ul li.no{
		background: url(${path}/images/icons/no.png) left no-repeat;
	    }
	</style>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/common/progressBar.js"></script>
    </head>
    <body>
	<div id="wrap">
	    <div class="progress">
		<div class="bar">${progressBar.percentage}%</div>    
	    </div>
	    <div class="status">
		<ul>${progressBar.message}</ul>
	    </div>
	</div>
    </body>
</html>
