<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <%@include file="../includes/baseResources.jsp" %>
        <title>Edi Tracking</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <%@include file="init.jsp" %>
        <%@include file="../includes/jspVariables.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>

    <style type="text/css">
            #ediInfoListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: white;
                background-position: 100%;
                left: 25%;
                top: 15%;
            }
        </style>
    </head>
    <body class="whitebackgrnd" >
        <div id="cover" style="width: expression(document.body.offsetWidth+'px');height: expression(document.body.offsetHeight+120+'px')"></div>
        <table cellspacing="0" cellpadding="0"  width="100%" border="0" class="tableBorderNew">
            <input type="hidden" value="${_304IntraPath}" id="_304IntraPath"/>
            <input type="hidden" value="${_304GtnexusPath}" id="_304GtnexusPath"/>
            <tr class="tableHeadingNew">
                <td>Edi Tracking <td>
            </tr>
            <tr>
                <td>
                    <table class="dataTable" border="0" id="ediTrackingTable">
                        <thead>
                            <tr>
                                <th>Booking#</th>
                                <th>Msg Type</th>
                                <th>Company</th>
                                <th>File Name</th>
                                <th>Processed Date</th>
                                <th>Status</th>
                                <th>Ack Rcvd Date</th>
                                <th>Ack Status</th>
                                <th>More Info</th>
                            </tr>
                        </thead>
                        <c:if test="${not empty editList}">
                            <tbody>
                                <c:forEach var="ediTracking" items="${editList}">
                                    <c:choose>
                                        <c:when test="${rowStyle eq 'oddStyle'}">
                                            <c:set var="rowStyle" value="evenStyle"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="rowStyle" value="oddStyle"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <tr class="${rowStyle}">
                                        <c:set var='companyName' value='${ediTracking.ediCompany eq "I" ? "INTTRA" :"GTNEXUS"}'/>
                                        <td>${ediTracking.bookingNumber}</td>
                                        <td>${ediTracking.messageType}</td>
                                        <td>${companyName}</td>
                                        <td>${ediTracking.filename}</td>
                                        <td>${ediTracking.processedDate}</td>
                                        <td>${ediTracking.status}</td>
                                        <td>${ediTracking.ackReceivedDate}</td>
                                        <td>${ediTracking.ackStatus}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${ediTracking.messageType ==  '304'}">
                                                <img alt=""src="${path}/img/icons/pubserv.gif" border="0"
                                                     onclick="openEdi('${ediTracking.bookingNumber}', '${ediTracking.messageType}',
                                                         '${companyName}', '${ediTracking.filename}')"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img alt=""src="${path}/img/icons/pubserv.gif" border="0"
                                                         onclick="showMoreInfo('${ediTracking.drnumber}', '${ediTracking.messageType}', '${companyName}', '${ediTracking.filename}',
                                                                     '${ediTracking.status}', '${ediTracking.processedDate}', 
                                                                     '', '${ediTracking.bookingNumber}','', '${ediBean.docType}')"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </c:if>
                    </table>
                </td>
            </tr>
        </table>
    </body>
    <script type="text/javascript">
        function openEdi(bkgNo,msgType,ediCompany,fileName){
            showAlternateMask();
            $("#add-Comments-container").center().show(500, function () {
                $('#bkgNoId').text(bkgNo);
                $('#msgTypeId').text(msgType);
                $('#ediCompanyId').text(ediCompany);
                $('#fileNameId').text(fileName);
            });
        }
        function showMoreInfo(drNumber, messageType, ediCompany, fileName, status, processedDate, scacCode, bookingNumber, description, docTyp) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.EdiDwr",
                    methodName: "showMoreInfoForEdi",
                    formName: "com.gp.cong.logisoft.beans.EdiBean",
                    drNumber: drNumber,
                    messageType: messageType,
                    ediCompany: ediCompany,
                    status: status,
                    processedDate: processedDate,
                    fileName: fileName,
                    scacCode: scacCode,
                    bookingNumber: bookingNumber,
                    description: description,
                    docTyp: docTyp,
                    forward: "/jsps/LCL/EdiTrackingTemplate.jsp"
                },
                success: function(data) {
                    if (null !== data && data !== "") {
                        showPopUp();
                        var ediInfoListDiv = createHTMLElement("div", "ediInfoListDiv", "55%", "45%", document.body);
                        jQuery("#ediInfoListDiv").html(data);
                        floatDiv("ediInfoListDiv", document.body.offsetWidth / 4, document.body.offsetHeight / 4).floatIt();
                    }
                }
            });
        }
        function closeEdi(){
            $("#add-Comments-container").center().hide(500, function () {
                hideAlternateMask();
            });
        }
        function closeEdiInfoListDiv() {
            closePopUp();
            document.body.removeChild(document.getElementById("ediInfoListDiv"));
        }
        function openFile() {
            var filePath = document.getElementById("filePath").value;
            window.open("${path}/servlet/FileViewerServlet?fileName=" + filePath, "mywindow", "resizable=yes,scrollbars=yes,width=800,height=600,left=80,top=100");
            closeEdiInfoListDiv();
        }
        function openXmlFiles(){
            var companyName=$('#ediCompanyId').text();
            var filePath="";
            if(companyName==="INTTRA"){
                filePath=$('#_304IntraPath').val()+"/";
            }else{
                filePath=$('#_304GtnexusPath').val()+"/";
            }
            var path=filePath+$('#fileNameId').text();
            window.open("${path}/servlet/FileViewerServlet?fileName=" + path, "mywindow", "resizable=yes,scrollbars=yes,width=800,height=600,left=80,top=100");
            closeEdi();
        }
    </script>
</html>

<div id="add-Comments-container" class="static-popup" style="display: none;width: 600px;height: 200px;">
    <table class="table" style="margin: 2px;width: 598px;" border="0">
        <tr class="tableHeadingNew">
            <td>
                <div class="float-left">
                    <label id="headingAdjustmentComments">Electronic Data Interchange Tracking</label>
                </div>
            </td>
            <td>
                <span style="float: right;">
                    <a id="lightBoxClose" href="javascript:closeEdi();">
                        <img src="${path}/js/greybox/w_close.gif" alt="close" title="Close" style="border: none;">Close
                    </a>
                </span>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="textBoldforlcl">Booking Number:&nbsp;&nbsp;&nbsp;<span id="bkgNoId"></span></td>
        </tr>
        <tr>
            <td colspan="2" class="textBoldforlcl">Message Type:&nbsp;&nbsp;&nbsp;<span id="msgTypeId"></span></td>
        </tr>
        <tr>
            <td colspan="2" class="textBoldforlcl">EDI Company:&nbsp;&nbsp;&nbsp;<span id="ediCompanyId"></span></td>
        </tr>
        <tr>
            <td colspan="2" class="textBoldforlcl">File Name:&nbsp;&nbsp;&nbsp;<span id="fileNameId"></span></td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td align="center">
                <input type="button" value="OpenXml" class="button-style2" onclick="openXmlFiles()"/>
            </td>
            <td></td>
        </tr>
    </table>
</div>
