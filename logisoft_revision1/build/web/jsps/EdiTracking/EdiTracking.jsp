<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <%@include file="../includes/baseResources.jsp" %>
        <title>Edi Tracking</title>

        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
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
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type='text/javascript' src="${path}/js/common.js"></script>
    </head>
    <body class="whitebackgrnd" >
        <div id="cover" style="width: expression(document.body.offsetWidth+'px');height: expression(document.body.offsetHeight+120+'px')"></div>
        <html:form action="/ediTracking" type="com.gp.cong.logisoft.struts.form.EdiTrackingForm" scope="request">
            <table width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table align="center" width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">Edi Tracking</tr>
                            <tr><td><table>
                                        <tr class="textlabelsBold">
                                            <td>DR/File #&nbsp;&nbsp;&nbsp;</td>
                                            <td><html:text property="drNumber" styleClass="textlabelsBoldForTextBox" value="${ediBean.drNumberTxt}" maxlength="8" size="8" ></html:text>&nbsp;&nbsp;&nbsp;</td>
                                                <td>Message Type&nbsp;&nbsp;&nbsp;</td>
                                                <td><html:select property="messageType" style="width:150px;" styleClass="BackgrndColorForTextBox dropdown_accounting" value="${ediBean.messageTypeTxt}">
                                                    <html:optionsCollection name="messagetypelist"/></html:select>&nbsp;&nbsp;&nbsp;</td>
                                                <td>EDI Company&nbsp;&nbsp;&nbsp;</td>
                                                <td><html:select property="ediCompany" style="width:150px;" styleClass="BackgrndColorForTextBox dropdown_accounting" value="${ediBean.ediCompanyTxt}">
                                                    <html:optionsCollection name="edicompanylist"/></html:select>&nbsp;&nbsp;&nbsp;</td>
                                                <td>&nbsp;&nbsp;&nbsp;</td>
                                                <td><input type="button" class="buttonStyleNew" value="Search" onclick="searchEDI()"></td>
                                            </tr>
                                        </table></td></tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table cellspacing="0" cellpadding="0"  width="100%" border="0" class="tableBorderNew" >
                                <tr class="tableHeadingNew">
                                    <td>Edi Tracking List<td>
                                </tr>
                                <tr>
                                    <td>
                                    <c:if test="${!empty logFileList}">
                                        <c:set var="i" value="0"></c:set>
                                        <display:table name="${logFileList}" class="displaytagstyleNew" pagesize="15"
                                                       id="ediTracking" sort="list" requestURI="/ediTracking.do?">
                                            <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner">
                                                    <font color="blue">{0}</font>Filenames Displayed,For more Data click on Page Numbers.
                                                    <br>
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.one_items_found">
                                                <span class="pagebanner">
                                                    One {0} displayed. Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.all_items_found">
                                                <span class="pagebanner">
                                                    {0} {1} Displayed, Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="basic.msg.empty_list">
                                                <span class="pagebanner">
                                                    No Records Found.
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.placement" value="bottom" />
                                            <display:setProperty name="paging.banner.item_name" value="Filename"/>
                                            <display:setProperty name="paging.banner.items_name" value="Filenames"/>
                                            <display:column  property="drNumber" sortable="true" title="FILE NO"></display:column>
                                            <display:column  property="messageType" sortable="true" title="MSG TYPE"></display:column>
                                            <display:column  property="ediCompany" sortable="true" title="COMPANY"></display:column>
                                            <display:column sortable="true" title="FILE NAME">
                                                <c:choose>
                                                    <c:when test="${ediTracking.messageType == '315'}">
                                                        <span title="${ediTracking.eventName}">${str:abbreviate(ediTracking.eventName,35)}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span title="${ediTracking.fileName}">${str:abbreviate(ediTracking.fileName,35)}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </display:column>
                                            <display:column  property="bookingNumber" sortable="true" title="BOOKING #"></display:column>
                                            <display:column property="fmtprocessedDate" title="PROCESSED DATE" sortable="true">
                                            </display:column>
                                            <display:column property="status" title="SI STATUS" sortable="true" ></display:column>
                                            <c:choose>
                                                <c:when test="${ediTracking.messageType == '997'}">
                                                    <display:column property="fmtprocessedDate" sortable="true" title="ACK RCVD DATE"></display:column>
                                                </c:when>
                                                <c:otherwise>
                                                    <display:column property="ackRecievedDate" sortable="true" title="ACK RCVD DATE"></display:column>
                                                </c:otherwise>
                                            </c:choose>
                                            <display:column property="severity" title="ACK STATUS" sortable="true"></display:column>
                                            <display:column title="MORE INFO" >
                                                <span style="cursor: pointer;">
                                                    <img alt=""src="<c:url value="/img/icons/pubserv.gif"/>" border="0"
                                                         onclick="showMoreInfo('${ediTracking.drNumber}', '${ediTracking.messageType}', '${ediTracking.ediCompany}', '${ediTracking.fileName}', '${ediTracking.status}', '${ediTracking.processedDate}', '${ediTracking.scacCode}', '${ediTracking.bookingNumber}','${fn:escapeXml(ediTracking.description)}', '${ediBean.docTyp}')"/>
                                                </span>
                                            </display:column>
                                            <c:set var="i" value="${i+1}"></c:set>
                                        </display:table>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" />
        </html:form>

    </body>
    <script type="text/javascript">
        function searchEDI() {
            document.ediTrackingForm.buttonValue.value = "search";
            document.ediTrackingForm.submit();
            return;
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
                    forward: "/jsps/EdiTracking/EdiTrackingTemplate.jsp"
                },
                success: function(data) {
                    if (null != data && data != "") {
                        showPopUp();
                        var ediInfoListDiv = createHTMLElement("div", "ediInfoListDiv", "50%", "40%", document.body);
                        jQuery("#ediInfoListDiv").html(data);
                        floatDiv("ediInfoListDiv", document.body.offsetWidth / 4, document.body.offsetHeight / 4).floatIt();
                    }
                }
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
        changeSelectBoxOnViewMode();
    </script>
</html>
