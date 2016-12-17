<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../includes/jspVariables.jsp"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <base href="${basePath}">
        <title>AR Reports</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>
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
        <div id="container">
            <ul class="htabs">
                <li><a href="javascript: void(0)" tabindex="0">Statement</a></li>
                <li><a href="javascript: void(0)" tabindex="1">Aging</a></li>
                <li><a href="javascript: void(0)" tabindex="2">DSO</a></li>
                <li><a href="javascript: void(0)" tabindex="3">Account Notes</a></li>
            </ul>
            <div class='pane'>
                <iframe frameborder="0" id="tab0" name="arStatement" src='' width='100%' height='0' onload="hideProgressBar()"></iframe>
                <input type="hidden" id="src0" value="${path}/customerstatement.do"/>
            </div>
            <div class='pane'>
                <iframe frameborder="0" id="tab1" name="agingReport" src='' width='100%' height='0' onload="hideProgressBar()"></iframe>
                <input type="hidden" id="src1" value="${path}/agingReport.do"/>
            </div>
            <div class='pane'>
                <iframe frameborder="0" id="tab2" name="dsoReport" src='' width='100%' height='0' onload="hideProgressBar()"></iframe>
                <input type="hidden" id="src2" value="${path}/jsps/AccountsRecievable/dsoReport.jsp"/>
            </div>
            <div class="pane">
                <iframe frameborder="0" id="tab3" name="arAccountNotesReport" src='' width="100%" height="0" onload="hideProgressBar()"></iframe>
                <input type="hidden" id="src3" value="${path}/jsps/AccountsRecievable/arAccountNotesReport.jsp"/>
            </div>
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
                    }
                    $("#tab"+index).height($(document).height()-45);
                }
            });

        });
        function viewReport(caption,fileName){
            window.parent.showGreyBox(caption,"${path}/servlet/FileViewerServlet?fileName="+fileName);
        }
    </script>
</html>