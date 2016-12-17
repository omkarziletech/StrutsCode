<%-- 
    Document   : achScheduler
    Created on : Feb 23, 2010, 11:00:12 PM
    Author     : Lakshminarayanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ include file="../includes/baseResources.jsp"%>
<%@ include file="../includes/resources.jsp"%>
<%@ include file="../includes/jspVariables.jsp"%>
<%@ include file="../includes/baseResourcesForJS.jsp"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/AchScheduler.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <style type="text/css">
            #processInfoDiv {
                position: fixed;
                _position: absolute;
                z-index: 999;
                left: 15%;
                top: 15%;
                width: 600px;
                height: 300px;
                border-style:solid solid solid solid;
                background-color: white;
            }
            .progress-container {
                border: 1px solid #ccc;
                width: 250px;
                margin: 2px 5px 2px 180px;
                padding: 1px;
                background: white;
                position:relative;
                height: 15px;
            }

            .progress_bar {
                background-color: #66CC00;
                height: 15px;
                text-align:right;
                font-size:11px;
            }
        </style>
    </head>
    <body class="whitebackgrnd" onload="searchAchProcessHistory()">
        <div id="cover"></div>
        <table class="tableBorderNew" cellpadding="3" cellspacing="3" width="100%">
            <tr class="tableHeadingNew"><td>Ach Filter</td></tr>
            <tr>
                <td>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr class="textlabelsBold">
                            <td>Start Date</td>
                            <td><input type="text" name="startDate" id="txtCal1" class="textlabelsBoldForTextBox">
                                <img src="${path}/img/CalendarIco.gif" alt="cal1" width="16" height="16" align="top" id="Cal1"  onmousedown="insertDateFromCalendar(this.id,6);" />
                            </td>
                            <td>End Date</td>
                            <td><input type="text" name="endDate" id="txtCal2" class="textlabelsBoldForTextBox">
                                <img src="${path}/img/CalendarIco.gif" alt="cal2" width="16" height="16" align="top" id="Cal2" onmousedown="insertDateFromCalendar(this.id,6);" />
                            </td>
                            <td>Status</td>
                            <td>
                                <select name="status" id="status" class="textlabelsBoldForTextBox">
                                    <option label="Completed" value="Completed"/>
                                    <option label="Ready to send" value="Ready to send"/>
                                    <option label="Failed" value="Failed"/>
                                </select>
                            </td>
                        </tr>

                    </table>
                </td>
            </tr>
            <tr>
                <td align="center"><input type="button" value="Search" class="buttonStyleNew" onclick="searchAchProcessHistory()"></td>
            </tr>
            <tr class="tableHeadingNew"><td>List of ACH Files</td></tr>
            <tr>
                <td>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td colspan="6">
                                <table class="displaytagstyleNew" cellpadding="0" cellspacing="0" width="100%">
                                    <thead>
                                        <tr>
                                            <th align="left">Bank Name</th>
                                            <th align="left">Bank Account#</th>
                                            <th align="left">Bank Routing#</th>
                                            <th align="left">Start Time</th>
                                            <th align="left">End Time</th>
                                            <th align="left">Status</th>
                                            <th align="left">Ach File</th>
                                            <th align="left">Encrypted File</th>
                                            <th align="left">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody id="listBody">
                                        <tr id="pattern" style="display:none;">
                                            <td id="bankName"></td>
                                            <td id="bankAcctNo"></td>
                                            <td id="bankRoutingNo"></td>
                                            <td id="startTime"></td>
                                            <td id="endTime"></td>
                                            <td id="status"></td>
                                            <td id="achFileName"></td>
                                            <td id="encryptedFileName"></td>
                                            <td id="action">
                                                <span id="jobSpan"></span>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <div id="processInfoDiv" style="display: none;">
            <table width="100%"
                   style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
                <tbody>
                    <tr>
                        <td class="lightBoxHeader">
                            Ach Process Info Details
                        </td>
                        <td>
                            <div style="vertical-align: top">
                                <a id="lightBoxClose" href="javascript: closeProcessInfoDiv();">
                                    <img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                                </a>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="progress-container">
                <div id="progress_bar" class="progress_bar" style="width:0"></div>
            </div>
            <div id="ProcessInfoContents" style="width: 590px;height: 280px;overflow: auto;"></div>
        </div>
    </body>
    <script type="text/javascript">
        dwr.engine.setTextHtmlHandler(dwrSessionError);
        function searchAchProcessHistory(){
            var startTime = dwr.util.getValue("txtCal1");
            var endTime = dwr.util.getValue("txtCal2");
            var status = dwr.util.getValue("status");
            AchScheduler.getAchProcessHistory(startTime,endTime,status,function(data){
                if(null!=data){
                    dwr.util.removeAllRows("listBody", { filter:function(tr) {
                            return (tr.id != "pattern");
                        }});
                    var achSchedulerBean, id;
                    var styleClass = "odd";
                    for (var i = 0; i < data.length; i++) {
                        achSchedulerBean = data[i];
                        id = achSchedulerBean.processId;
                        dwr.util.cloneNode("pattern", { idSuffix:id });
                        dwr.util.setValue("bankName" + id, achSchedulerBean.bankName);
                        dwr.util.setValue("bankAcctNo" + id, achSchedulerBean.bankAcctNo);
                        dwr.util.setValue("bankRoutingNo" + id, achSchedulerBean.bankRoutingNo);
                        dwr.util.setValue("startTime" + id, achSchedulerBean.startTime);
                        dwr.util.setValue("endTime" + id, achSchedulerBean.endTime);
                        dwr.util.setValue("status" + id, achSchedulerBean.status);
                        var achFileName = "<a href=\"javascript:viewFile('"+achSchedulerBean.achFileName+"','"+achSchedulerBean.processId+"')\">"+achSchedulerBean.achFileName+"</a>"
                        dwr.util.setValue("achFileName" + id, achFileName, { escapeHtml:false } );
                        var encryptedFileName = "<a href=\"javascript:viewFile('"+achSchedulerBean.encryptedFileName+"','"+achSchedulerBean.processId+"')\">"+achSchedulerBean.encryptedFileName+"</a>"
                        dwr.util.setValue("encryptedFileName" + id, encryptedFileName, { escapeHtml:false } );
                        if(achSchedulerBean.status=="Completed"){
                            //document.getElementById("jobSpan" + id).style.display = "none";
                            var jobSpan = document.getElementById("jobSpan" + id);
                            jobSpan.innerHTML="<img src='${path}/img/icons/lrun_obj1.gif' alt='Resend' onmouseover='tooltip.show(\"Resend\",null,event)' onmouseout='tooltip.hide()' onclick='doAchProcess(\""+id+"\")'/>";
                        }else{
                            var jobSpan = document.getElementById("jobSpan" + id);
                            jobSpan.innerHTML="<img src='${path}/img/icons/lrun_obj1.gif' alt='Execute' onmouseover='tooltip.show(\"Execute\",null,event)' onmouseout='tooltip.hide()' onclick='doAchProcess(\""+id+"\")'/>";
                        }
                        document.getElementById("pattern" + id).style.display = "block";
                        document.getElementById("pattern" + id).className = styleClass;
                        if(styleClass=="even"){
                            styleClass = "odd";
                        }else{
                            styleClass = "even";
                        }
                    }
                }
            });
        }

        function viewFile(fileName,id){
            window.parent.parent.parent.showGreyBox(fileName,"${path}/servlet/FileViewerServlet?domain=AchProcessHistory&id="+id+"&fileName="+fileName);
        }
        function doAchProcess(id){
            AchScheduler.processAchPayments(id,true,function(data){});
            setTimeout("updateAchProcessInfo()", 50);
        }
        function updateAchProcessInfo(){
            AchScheduler.getAchProcessInfo(function (achProcessInfo){
                if(null!=achProcessInfo){
                    showPopUp();
                    document.getElementById("processInfoDiv").style.display="block";
                    var pbar = document.getElementById('progress_bar');
                    pbar.innerHTML = achProcessInfo.progressCount + '%';
                    pbar.style.width = achProcessInfo.progressCount + '%';
                    var divContent = achProcessInfo.message;
                    dwr.util.setValue("ProcessInfoContents", divContent, { escapeHtml:false });
                    if(achProcessInfo.status!="ended"
                        && achProcessInfo.status!="failed"){
                        setTimeout("updateAchProcessInfo()", 50);
                    }
                }
            });
        }
        function closeProcessInfoDiv(){
            document.getElementById("processInfoDiv").style.display="none";
            dwr.util.setValue("ProcessInfoContents", "", { escapeHtml:false });
            closePopUp();
        }
    </script>
</html>
