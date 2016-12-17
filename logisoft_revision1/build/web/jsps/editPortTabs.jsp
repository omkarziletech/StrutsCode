<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <link type="text/css" rel="stylesheet" href="${path}/css/layout/second-tabs.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/tab/tab.js"></script>
        <title>Port Details Tabs</title>
        <script type="text/javascript">
            $(document).ready(function () {
                var initial = 0;
                $("ul.htabs").tabs("> .pane", {effect: "fade", current: "selected", initialIndex: initial, onClick: function () {
                        var frameid = $("ul.htabs li.selected").find("a").attr("title");
                        $("#" + frameid).height($(document).height() - 60);
                        if (frameid === "editlclPortsFrame") {
                            $("#" + frameid).contents().find("#domesticRouting").focus();
                            $("#" + frameid).contents().find("#internalMarks").focus();
                            $("#" + frameid).contents().find("#griRemarks").focus();
                            $("#" + frameid).contents().find("#splRemarksInEnglish").focus();
                            $("#" + frameid).contents().find("#splRemarksInSpanish").focus();
                            $("#" + frameid).contents().find("#terminalNumber").focus();
                        } else if (frameid === "editairPortsFrame") {
                            $("#" + frameid).contents().find("#airSplRemarksinEnglish").focus();
                            $("#" + frameid).contents().find("#airSplRemarksinSpanish").focus();
                            $("#" + frameid).contents().find("#terminalNumber").focus();
                        } else if (frameid === "editfclPortsFrame") {
                            $("#" + frameid).contents().find("#clauseDesc").focus();
                            $("#" + frameid).contents().find("#tempRemark").focus();
                            $("#" + frameid).contents().find("#quoteRemarks").focus();
                            $("#" + frameid).contents().find("#griRemarks").focus();
                            $("#" + frameid).contents().find("#terminalNumber").focus();
                        }
                    }
                });
            });
        </script>
    </head>
    <body>
        <ul class="htabs">
            <li><a rel="General" href="javascript: void(0)" title="editPortsFrame">General</a></li>
            <li><a rel="LCL" href="javascript: void(0)" title="editlclPortsFrame">LCL</a></li>
            <li><a rel="FCL" href="javascript: void(0)" title="editfclPortsFrame">FCL</a></li>
            <li><a rel="Air" href="javascript: void(0)" title="editairPortsFrame">Air</a></li>
            <li><a rel="Imports" href="javascript: void(0)" title="editimportPortsFrame">Imports</a></li>
        </ul>
        <div class="pane">
            <iframe frameborder="0" name="editPortsFrame" id="editPortsFrame" src="${path}/jsps/datareference/editPortDetails.jsp" width="100%" height="0"></iframe>
        </div>
        <div class="pane">
            <iframe frameborder="0" name="editlclPortsFrame" id="editlclPortsFrame" src="${path}/jsps/datareference/editLclPortsConfig.jsp" width="100%" height="0"></iframe>
        </div>
        <div class="pane">
            <iframe frameborder="0" name="editfclPortsFrame" id="editfclPortsFrame" src="${path}/jsps/datareference/editFclPortsConfig.jsp" width="100%" height="0"></iframe>
        </div>
        <div class="pane">
            <iframe frameborder="0" name="editairPortsFrame" id="editairPortsFrame" src="${path}/jsps/datareference/editAirportConfig.jsp" width="100%" height="0"></iframe>
        </div>
        <div class="pane">
            <iframe frameborder="0" name="editimportPortsFrame" id="editimportPortsFrame" src="${path}/jsps/datareference/editImportConfig.jsp" width="100%" height="0"></iframe>
        </div>
    </body>
</html>
