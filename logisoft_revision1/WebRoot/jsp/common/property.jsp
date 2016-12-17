<%-- 
    Document   : property
    Created on : 1 Oct, 2013, 4:26:22 PM
    Author     : Balaji.E
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/constants" prefix="constant"%>  
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
    <head>
        <title>Property</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script src="${path}/js/tab/tab.js" type="text/javascript" ></script>
        <link href="${path}/css/layout/second-tabs.css" type="text/css" rel="stylesheet"/>
        <script type="text/javascript" src="${path}/js/common/property.js"></script>
        <style type="text/css">
            #mask {
                display: none;
                position: absolute;
                overflow: visible;
                vertical-align: super;
                left: 0px;
                top: 0px;
                width: 100%;
                height: 100%;
                background: #000;
                opacity: 0.5;
                -moz-opacity: 0.5;
                -khtml-opacity: 0.5;
            }
        </style>
    </head>
    <body  class="whitebackgrnd">
        <%@include file="../../jsps/preloader.jsp"%>
        <div class="message">${message}</div>
        <html:form action="/property" type="com.logiware.common.form.PropertyForm" name="propertyForm" styleId="propertyForm" scope="request" method="post">
            <html:hidden property="action" styleId="action"/>
            <table class="table">
                <tr>
                <div id="mask"></div>
                <div id="progressBar" class="progressBar" style="position: absolute;left:40% ;top: 40%;right: 40%;bottom: 60%;display: none;">
                    <p class="progressBarHeader"><b style="width: 100%;padding-left: 40px;">Processing......Please Wait</b></p>
                    <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                        <input type="image" src="${path}/img/icons/newprogress_bar.gif"/>
                    </div>
                </div>
            <th>
                <div class="float-left">Property Management</div>
                <div class="float-right">
                    <a title="Refresh" href="javascript: void(0)" onclick="refresh();">
                        <img alt="Refresh" title="Refresh" src="${path}/images/icons/refresh_16.png"/>
                    </a>
                </div>
            </th>
        </tr>
        <tr>
            <td>

                <div id="type" class="result-container">
                    <ul class="htabs">
                        <li><a href="javascript: void(0)" tabindex="0">COMMON</a></li>
                        <li><a href="javascript: void(0)" tabindex="1">ACCOUNTING</a></li>
                        <li><a href="javascript: void(0)" tabindex="2">DOMESTIC</a></li>
                        <li><a href="javascript: void(0)" tabindex="3">FCL</a></li>
                        <li><a href="javascript: void(0)" tabindex="4">LCL</a></li>
                    </ul>
                    <div class="pane" id="common">
                        <c:set var="properties" value="${propertyForm.commonProperties}" scope="session"></c:set>
                        <c:import url="/jsp/common/propertyResult.jsp"/>

                    </div>
                    <div class="pane" id="accounting">
                        <c:set var="properties" value="${propertyForm.accountingProperties}" scope="session"></c:set>
                        <c:import url="/jsp/common/propertyResult.jsp"/>

                    </div>
                    <div class="pane" id="domestic">
                        <c:set var="properties" value="${propertyForm.domesticProperties}" scope="session"></c:set>
                        <c:import url="/jsp/common/propertyResult.jsp"/>

                    </div>
                    <div class="pane" id="fcl">
                        <c:set var="properties" value="${propertyForm.fclProperties}" scope="session"></c:set>
                        <c:import url="/jsp/common/propertyResult.jsp"/>
                    </div>
                    <div class="pane" id="lcl">
                        <c:set var="properties" value="${propertyForm.lclProperties}" scope="session"></c:set>
                        <c:import url="/jsp/common/propertyResult.jsp"/>
                    </div>
                </div>
            </td>
        </tr>
    </table>
</html:form>
</body>
</html>
