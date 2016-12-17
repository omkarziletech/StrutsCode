<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String lclPortsConfigPath = path + "/jsps/datareference/lclPortsConfig.jsp";
            String fclPortsConfigPath = path + "/jsps/datareference/fclPortsConfiguration.jsp";
            String airportConfigPath = path + "/jsps/datareference/airPortConfiguration.jsp";
            String importConfigPath = path + "/jsps/datareference/imports.jsp";
            String portpath = path + "/jsps/datareference/portDetails.jsp";

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="includes/baseResources.jsp"%>
        <%@include file="includes/jspVariables.jsp"%>
        <script src="${path}/js/jquery/jquery.js" type="text/javascript"></script>
        <link href="${path}/css/layout/second-tabs.css" type="text/css" rel="stylesheet"/>
        <script src="${path}/js/tab/tab.js" type="text/javascript" ></script>
        <script type="text/javascript">
            $(document).ready(function(){
                var initial = 0;
                var index=0;
                $("ul.htabs").tabs("> .pane",{effect: 'fade',current:'selected',initialIndex:initial,onClick:function(){
                        var frameid = $("ul.htabs li.selected").find("a").attr("title");
                        $("#"+frameid).height($(document).height()-60);
                    }
                });
            });
        </script>
    </head>
    <body>
        <div id="mainTabContainer">
            <ul class="htabs">
                <li><a rel="General" href="javascript: void(0)" title="portsFrame">General</a></li>
                <li><a rel="LCL" href="javascript: void(0)" title="lclPortsFrame">LCL-Ocean</a></li>
                <li><a rel="FCL" href="javascript: void(0)" title="fclPortsFrame">FCL</a></li>
                <li><a rel="Air" href="javascript: void(0)" title="airPortsFrame">Air</a></li>
                <li><a rel="Imports" href="javascript: void(0)" title="importPortsFrame">Imports</a></li>
            </ul>
            <div class='pane'>
                <iframe frameborder="0"  name="portsFrame" id="portsFrame" src='<%=portpath%>' width='100%' height='0'></iframe>
            </div>
            <div class='pane'>
                <iframe frameborder="0"  name="lclPortsFrame" id="lclPortsFrame" src='<%=lclPortsConfigPath%>' width='100%' height='0'></iframe>
            </div>
            <div class='pane'>
                <iframe frameborder="0"  name="fclPortsFrame" id="fclPortsFrame" src='<%=fclPortsConfigPath%>' width='100%' height='0'></iframe>
            </div>
            <div class='pane'>
                <iframe frameborder="0"  name="airPortsFrame" id="airPortsFrame" src='<%=airportConfigPath%>' width='100%' height='0'></iframe>
            </div>
            <div class='pane'>
                <iframe frameborder="0"  name="importPortsFrame" id="importPortsFrame" src='<%=importConfigPath%>' width='100%' height='0'></iframe>
            </div>
        </div>
    </body>
</html>
