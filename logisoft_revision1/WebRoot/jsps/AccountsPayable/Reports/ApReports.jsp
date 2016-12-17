<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@include file="../../includes/jspVariables.jsp"%>
<html:html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <base href="${basePath}">
        <title>AP Reports</title>
        <%@include file="../../includes/baseResources.jsp"%>
        <%@include file="../../includes/resources.jsp"%>
        <link href="${path}/css/layout/second-tabs.css" type="text/css" rel="stylesheet"/>
        <script src="${path}/js/jquery/jquery-1.3.1.js" type="text/javascript" ></script>
        <script src="${path}/js/tab/tab.js" type="text/javascript" ></script>
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
    <body class="whitebackgrnd">
        <div id="mask"></div>
        <div id="progressBar" class="progressBar" style="position: absolute;left:40% ;top: 40%;right: 40%;bottom: 60%;display: none;">
            <p class="progressBarHeader"><b style="width: 100%;padding-left: 40px;">Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="${path}/img/icons/newprogress_bar.gif"/>
            </div>
        </div>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <div id="container">
            <ul class="htabs">
                <c:forEach items="${apReportsForm.tabs}" var="tab">
                    <li><a href="javascript: void(0)" tabindex="${tab.key}"><c:out value="${tab.value.label}"/></a></li>
                </c:forEach>
            </ul>
            <c:forEach items="${apReportsForm.tabs}" var="tab">
                <div class='pane'>
                    <iframe frameborder="0" id="tab${tab.key}" name="${tab.key}" src='' width='100%' height='0' onload="hideProgressBar()"></iframe>
                    <input type="hidden" id="src${tab.key}" value="${path}/apReports.do?action=${tab.value.value}"/>
                </div>
            </c:forEach>
        </div>
    </body>
    <script type="text/javascript">
        function showMask() {
            var mask = document.getElementById("mask");
            mask.style.display = "block";
            mask.style.opacity = 0.5;
            mask.style.filter = "alpha(opacity="+50+")";
        }
        function hideMask() {
            var mask = document.getElementById("mask");
            mask.style.display = "none";
        }
        function showProgressBar(){
            showMask();
            document.getElementById('progressBar').style.display="block";
        }
        function hideProgressBar(){
            hideMask();
            document.getElementById('progressBar').style.display="none";
        }
        $(document).ready(function(){
            $("ul.htabs").tabs("> .pane",{effect: 'fade',current:'selected',initialIndex:0,onClick:function(){
                    var index = $("ul.htabs li.selected").find("a").attr("tabindex");
                    var src = $("#src"+index).val();
                    if($("#tab"+index).attr("src")==''){
                        $("#tab"+index).attr("src", src);
                        showProgressBar();
                    }
                    $("#tab"+index).height($(document).height()-45);
                }
            });

        });
        function viewReport(caption,fileName){
            window.parent.showGreyBox(caption,"${path}/servlet/FileViewerServlet?fileName="+fileName);
        }
    </script>
</html:html>