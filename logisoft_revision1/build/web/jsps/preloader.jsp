<%-- 
    Document   : preloader
    Created on : May 19, 2011, 12:33:53 PM
    Author     : Lakshmi Naryanan
--%>
<%--
    Jquery must be added in the jsp file where the preloader will be called
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${path}/js/preloader.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/css/preloader.css"/>
<div class="mask"></div>
<div class="alternate-mask"></div>
<div class="preloader">
    <img alt="Loading, Please wait....." src="${path}/images/preloader.gif"/>
    <label class="seconds" style="position: absolute; top: 50px; left: 115px;"></label>
</div>