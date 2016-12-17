<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@include file="../includes/jspVariables.jsp" %>

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
        <script type='text/javascript' src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
    </head>
    <body class="whitebackgrnd" >
        <div id="cover" style="width: expression(document.body.offsetWidth+'px');height: expression(document.body.offsetHeight+120+'px')"></div>
        <html:form action="/ediTrackingSystem" scope="request">
            <table width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table align="center" width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">Edi Tracking</tr>
                            <tr><td><table>
                                        <tr class="textlabelsBold">
                                            <td>File No #&nbsp;&nbsp;&nbsp;</td>
                                            <td><html:text property="drNumber" styleClass="textlabelsBoldForTextBox" value="${ediBean.drNumberTxt}" maxlength="8" size="25" ></html:text>&nbsp;&nbsp;&nbsp;</td>
                                            <td>Message Type&nbsp;&nbsp;&nbsp;</td>
                                            <td><html:select property="messageType" style="width:150px;" styleClass="BackgrndColorForTextBox dropdown_accounting" value="${ediBean.messageTypeTxt}">
                                                    <html:optionsCollection name="messagetypelist"/></html:select>&nbsp;&nbsp;&nbsp;</td>
                                                <td>EDI Company&nbsp;&nbsp;&nbsp;</td>
                                                <td><html:select property="ediCompany" style="width:150px;" styleClass="BackgrndColorForTextBox dropdown_accounting" value="${ediBean.ediCompanyTxt}">
                                                    <html:optionsCollection name="edicompanylist"/></html:select>&nbsp;&nbsp;&nbsp;</td>
                                                <td>Booking No&nbsp;&nbsp;&nbsp;</td>
                                                <td><html:text property="bookingNo" styleClass="textlabelsBoldForTextBox" value="${ediBean.bookingNumber}" maxlength="35" size="25" ></html:text></td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                            <td>Origin &nbsp;&nbsp;&nbsp;</td>
                                            <td><html:text property="placeOfReceipt" styleClass="textlabelsBoldForTextBox" value="${ediBean.placeOfReceipt}" maxlength="35" size="25" ></html:text>&nbsp;&nbsp;&nbsp;</td>
                                            <td>POL&nbsp;&nbsp;&nbsp;</td>
                                                <td><html:text property="portOfLoad" styleClass="textlabelsBoldForTextBox" value="${ediBean.portOfLoad}" maxlength="35" size="25" ></html:text></td>
                                                <td>POD&nbsp;&nbsp;&nbsp;</td>
                                                <td><html:text property="portOfDischarge" styleClass="textlabelsBoldForTextBox" value="${ediBean.portOfDischarge}" maxlength="35" size="25" ></html:text></td>
                                            <td>Destination&nbsp;&nbsp;&nbsp;</td>
                                            <td><html:text property="placeOfDelivery" styleClass="textlabelsBoldForTextBox" value="${ediBean.placeOfDelivery}" maxlength="35" size="25" ></html:text></td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                 <td>From Date &nbsp;&nbsp;&nbsp;</td>
                                                 <td>
                                                    <c:choose>
                                                        <c:when test="${empty ediTrackingSystemForm.fromDate}">
                                                            <html:text property="fromDate" value="${fromDate}" readonly="true" size="11" styleClass="textlabelsBoldForTextBox" styleId="txtcalbb" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:text property="fromDate" value="${ediTrackingSystemForm.fromDate}" size="11" maxlength="11" styleClass="textlabelsBoldForTextBox" styleId="txtcalbb" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <img src="${path}/img/CalendarIco.gif" alt="cal" width="16" height="16" align="middle" id="calbb"
                                                         onmousedown="insertDateFromCalendar(this.id,3);" style="padding-top: 2px;"/>
                                                </td>
                                                <td>To Date &nbsp;&nbsp;&nbsp;</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${empty ediTrackingSystemForm.toDate}">
                                                            <html:text property="toDate" value="${toDate}" readonly="true" size="11" styleClass="textlabelsBoldForTextBox" styleId="txtcalToyy" />
                                                        </c:when>
                                                    <c:otherwise>
                                                        <html:text property="toDate" value="${ediTrackingSystemForm.toDate}" size="11" maxlength="11" styleClass="textlabelsBoldForTextBox" styleId="txtcalToyy" />
                                                    </c:otherwise>
                                                     </c:choose>
                                                        <img src="${path}/img/CalendarIco.gif" alt="cal" width="16" height="16" align="middle" id="calToyy"
                                                             onmousedown="insertDateFromCalendar(this.id,3);" style="padding-top: 2px;"/>
                                                </td>
                                            </tr>

                                            <tr>
                                                <td colspan="4" align="right"><input type="button" class="buttonStyleNew" value="Search" onclick="searchEDI()"></td>
                                                <td><input type="button" class="buttonStyleNew" value="Export to Excel" style="width: 100px" onclick="exportToExcel()"/></td>
                                                <td colspan="3" align="left"><input type="button" class="buttonStyleNew" value="Clear" onclick="clearFormValues()"/> </td>
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
                                                       id="ediTrackingSystem" sort="list" requestURI="/ediTrackingSystem.do?">
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
                                            <display:column  property="bookingNumber" sortable="true" title="BookingNo"></display:column>
                                            <display:column property="requestor" sortable="true" title="Requestor"></display:column>
                                            <display:column  property="messageType" sortable="true" title="MSG <br/> TYPE"></display:column>
                                            <display:column  property="ediCompany" sortable="true" title="COMPANY"></display:column>
                                            <display:column sortable="true" title="Origin" style="width:10px;">
                                                <c:choose>
                                                    <c:when test="${not empty ediTrackingSystem.placeOfReceiptCity}">
                                                        <span class="hotspot" onmouseover="tooltip.showSmall('<strong>${ediTrackingSystem.placeOfReceiptCity}</strong>','20',event);"
                                                              onmouseout="tooltip.hide();" style="color:black;font-weight: bold;">
                                                        ${ediTrackingSystem.placeOfReceipt}
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color:red;font-weight: bold;">${ediTrackingSystem.placeOfReceipt}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </display:column>
                                            <display:column  sortable="true" title="POL" style="width:10px;">
                                                <c:choose>
                                                    <c:when test="${not empty ediTrackingSystem.portOfLoadCity}">
                                                        <span class="hotspot" onmouseover="tooltip.showSmall('<strong>${ediTrackingSystem.portOfLoadCity}</strong>','20',event);"
                                                              onmouseout="tooltip.hide();" style="color:black;font-weight: bold;">
                                                        ${ediTrackingSystem.portOfLoad}
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color:red;font-weight: bold;">${ediTrackingSystem.portOfLoad}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </display:column>
                                            <display:column  sortable="true" title="POD" style="width:10px;">
                                                <c:choose>
                                                    <c:when test="${not empty ediTrackingSystem.portOfDischargeCity}">
                                                        <span class="hotspot" onmouseover="tooltip.showSmall('<strong>${ediTrackingSystem.portOfDischargeCity}</strong>','20',event);"
                                                              onmouseout="tooltip.hide();" style="color:black;font-weight: bold;">
                                                        ${ediTrackingSystem.portOfDischarge}
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color:red;font-weight: bold;">${ediTrackingSystem.portOfDischarge}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </display:column>
                                            <display:column sortable="true" title="Destination" style="width:10px;">
                                                <c:choose>
                                                    <c:when test="${not empty ediTrackingSystem.placeOfDeliveryCity}">
                                                        <span class="hotspot" onmouseover="tooltip.showSmall('<strong>${ediTrackingSystem.placeOfDeliveryCity}</strong>','20',event);"
                                                              onmouseout="tooltip.hide();" style="color:black;font-weight: bold;">
                                                        ${ediTrackingSystem.placeOfDelivery}
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color:red;font-weight: bold;">${ediTrackingSystem.placeOfDelivery}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </display:column>
                                            <display:column  title="PROCESSED DATE" sortable="true" property="processedDate"/>
                                            <display:column  title="ACK Created DATE" sortable="true" property="ackRecievedDate"/>
                                            <display:column property="ackStatus" title="ACK STATUS" sortable="true"></display:column>
                                            <display:column property="ediStatus" title="STATUS" sortable="true" ></display:column>
                                            <display:column property="transportService" title="TRANSPORT </br> SERVICE" sortable="true"> </display:column>
                                            <display:column property="transactionStatus" title="TRANSACTION</br> STATUS" sortable="true"></display:column>
                                            <display:column title="PDF" >
                                                <span style="cursor: pointer;">
                                                    <img alt=""src="<c:url value="/img/icons/pdf.gif"/>" border="0"
                                                         onclick="openPDF('${ediTrackingSystem.id}','${ediTrackingSystem.messageType}')"/>
                                                </span>
                                                <input type="hidden" name="id" value=""/>
                                            </display:column>
                                            <display:column title="XML" >
                                                <span style="cursor: pointer;">
                                                    <img alt=""src="<c:url value="/images/xml.gif"/>" border="0"
                                                         onclick="openFile('${ediTrackingSystem.id}','${ediTrackingSystem.messageType}')"/>
                                                </span>
                                                <input type="hidden" name="id" value=""/>
                                            </display:column>
                                            <display:column title="ACK XML" >
                                                <span style="cursor: pointer;">
                                                    <img alt=""src="<c:url value="/images/xml.gif"/>" border="0"
                                                         onclick="openFile('${ediTrackingSystem.id}','997')"/>
                                                </span>
                                                <input type="hidden" name="id" value=""/>
                                            </display:column>
                                            <display:column  property="fileName" sortable="true" title="FILE NAME" maxLength="18">
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
        function searchEDI(){
            document.ediTrackingSystemForm.buttonValue.value="search";
            document.ediTrackingSystemForm.submit();
            return;
        }
	function clearFormValues(){
            document.ediTrackingSystemForm.drNumber.value = "";
            document.ediTrackingSystemForm.messageType.value = "";
            document.ediTrackingSystemForm.ediCompany.value = "";
            document.ediTrackingSystemForm.bookingNo.value = "";
            document.ediTrackingSystemForm.placeOfReceipt.value = "";
            document.ediTrackingSystemForm.portOfLoad.value = "";
            document.ediTrackingSystemForm.portOfDischarge.value = "";
            document.ediTrackingSystemForm.placeOfDelivery.value = "";
            document.ediTrackingSystemForm.fromDate.value = "";
            document.ediTrackingSystemForm.toDate.value = "";
            document.ediTrackingSystemForm.buttonValue.value="";
            document.ediTrackingSystemForm.submit();
        }
        function exportToExcel(){
            document.ediTrackingSystemForm.buttonValue.value="exportToExcel";
            document.ediTrackingSystemForm.submit();
        }
        function closeEdiInfoListDiv(){
            closePopUp();
            document.body.removeChild(document.getElementById("ediInfoListDiv"));
        }
        function openFile(id,messageType){
            window.open ("${path}/servlet/EdiXmlServlet?id="+id+"&messageType="+messageType,"mywindow","resizable=yes,scrollbars=yes,width=800,height=600,left=80,top=100");
            closeEdiInfoListDiv();
        }
        function openPDF(id){
            window.open ("${path}/servlet/EdiReportServlet?id="+id,"mywindow","resizable=yes,scrollbars=yes,width=800,height=600,left=80,top=100");
            closeEdiInfoListDiv();
        }
        changeSelectBoxOnViewMode();
    </script>
</html>
