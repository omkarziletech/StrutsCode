<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html> 
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>JobScheduler</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
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
    <body class="whitebackgrnd">
        <div id="cover"></div>
        <html:form action="/jobScheduler" scope="request">
            <table class="tableBorderNew" border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr class="tableHeadingNew"><td>List of Schedulers</td></tr>
                <tr>
                    <td>
                        <display:table id="schedulerJob" name="${jobSchedulers}" class="displaytagstyleNew">
                            <display:setProperty name="paging.banner.placement" value="none"/>
                            <display:column title="Scheduler Name">
                                <c:out value="${schedulerJob.type}"/>
                            </display:column>
                            <display:column title="Start Date" property="startDate"></display:column>
                            <display:column title="End Date" property="endDate"></display:column>
                            <display:column title="Status" property="status"></display:column>
                            <display:column title="Action">
                                <c:choose>
                                    <c:when test="${schedulerJob.type == 'ACH Process'}">
                                        <c:if test="${schedulerJob.status!='Completed'}">
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>Execute</strong>',null,event);" onmouseout="tooltip.hide();">
                                                <img src="${path}/img/icons/lrun_obj1.gif" border="0" onclick="doAchProcess()"/>
                                            </span>
                                        </c:if>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>View All Ach Jobs</strong>',null,event);" onmouseout="tooltip.hide();">
                                            <img src="${path}/img/icons/preview.gif" border="0" onclick="viewAllACHProcess()"/>
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Execute</strong>',null,event);" onmouseout="tooltip.hide();">
                                            <img src="${path}/img/icons/lrun_obj1.gif" border="0" onclick="excecute('${schedulerJob.id}')"/>
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                        </display:table>
                    </td>
                </tr>
            </table>
            <html:hidden property="index"/>
            <html:hidden property="action"/>
        </html:form>
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
        function excecute(val){
            document.jobSchedulerForm.index.value=val;
            document.jobSchedulerForm.action.value="execute";
            document.jobSchedulerForm.submit();
        }

        function doAchProcess(){
            AchScheduler.processAchPayments("new",false,function(data){});
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


        function viewAllACHProcess(){
            var height = document.body.offsetHeight-75;
            var width = document.body.offsetWidth-75;
            GB_show("ACH Process","${path}/jsps/AccountsPayable/achScheduler.jsp",height,width);
        }
    </script>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

