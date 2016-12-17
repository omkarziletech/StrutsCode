<%-- 
    Document   : ControlReport
    Created on : Nov 9, 2010, 4:19:43 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ include file="../includes/baseResources.jsp"%>
<%@ include file="../includes/resources.jsp"%>
<%@ include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"/>
        <base href="${basePath}">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Control</title>
		<script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
    </head>
    <body>
		<html:form action="/controlReport" name="controlReportForm" type="com.logiware.form.ControlReportForm" scope="request" method="post">
			<html:hidden property="action"/>
			<table width="100%" cellspacing="5" cellpadding="3">
                <tr class="tableHeadingNew">
                    <td colspan="4">Control Report</td>
                </tr>
				<tr class="textlabelsBold">
					<td>Created Date
						<html:text property="createdDate" styleId="txtcreatedDate" styleClass="textlabelsBoldForTextBox"/>
						<img src="${path}/img/CalendarIco.gif" alt="Created Date" id="createdDate" onmousedown="insertDateFromCalendar(this.id,0);" align="top"/>
					</td>
					<td>Report Type
						<html:radio property="reportType" value="AC"/>AC
						<html:radio property="reportType" value="AR"/>AR
					</td>
					<td colspan="2">
						<input type="button" value="Generate" class="buttonStyleNew" style="width:100px" onclick="generateReport()"/>
						<input type="button" value="Print" class="buttonStyleNew" onclick="printReport()"/>
						<input type="button" value="Export To Excel" class="buttonStyleNew" style="width:100px" onclick="exportToExcel()"/>
					</td>
				</tr>
				<c:if test="${not empty controlReportForm.numberOfBluScreenRecords || not empty controlReportForm.numberOfLogiwareRecords}">
					<tr class="tableHeadingNew">
						<td colspan="4">Summary</td>
					</tr>
					<tr class="textlabelsBold">
						<td colspan="2">
							<table width="100%" cellspacing="3" cellpadding="3">
								<tr>
									<td></td>
									<td class="tableHeadingNew" align="right">Blue Screen</td>
									<td class="tableHeadingNew" align="right">Logiware</td>
									<td class="tableHeadingNew" align="right">Difference</td>
								</tr>
								<tr class="textlabelsBold">
									<td align="right"><b>No. Of Records :</b></td>
									<td align="right">${controlReportForm.numberOfBluScreenRecords}</td>
									<td align="right">${controlReportForm.numberOfLogiwareRecords}</td>
									<td align="right">${controlReportForm.numberOfBluScreenRecords - controlReportForm.numberOfLogiwareRecords}</td>
								</tr>
								<tr class="textlabelsBold">
									<td align="right"><b>Total Amount :</b></td>
									<td align="right">
										<fmt:formatNumber value="${controlReportForm.totalAmountInBlueScreen}" pattern="###,###,##0.00"/>
									</td>
									<td align="right">
										<fmt:formatNumber value="${controlReportForm.totalAmountInLogiware}" pattern="###,###,##0.00"/>
									</td>
									<td align="right">
										<fmt:formatNumber value="${controlReportForm.totalAmountInBlueScreen-controlReportForm.totalAmountInLogiware}" pattern="###,###,##0.00"/>
									</td>
								</tr>
							</table>
						</td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<c:set var="blueScreenTotal" value="0"/>
					<c:set var="logiwareTotal" value="0"/>
					<c:set var="blueScreenItemsSize" value="0"/>
					<c:set var="logiwareItemsSize" value="0"/>
					<c:choose>
						<c:when test="${controlReportForm.reportType=='AC'}">
							<tr class="tableHeadingNew">
								<td colspan="4">Blue Screen Detail</td>
							</tr>
							<tr class="textlabelsBold">
								<td colspan="4">
									<c:choose>
										<c:when test="${not empty controlReportForm.blueScreenAccruals}">
											<div>
												<c:set var="blueScreenItemsSize" value="${fn:length(controlReportForm.blueScreenAccruals)}"/>
												${blueScreenItemsSize} Blue Screen Accruals missed/conflict in Logiware
											</div>
											<div class="scrolldisplaytable" style="height:100%">
												<display:table name="${controlReportForm.blueScreenAccruals}" class="displaytagstyleNew" id="blueScreenAccrual">
													<display:setProperty name="paging.banner.placement" value="none"/>
													<display:column title="Vendor" property="vendorName"/>
													<display:column title="Vendor#" property="vendorNumber"/>
													<display:column title="Invoice/Bl" property="invoiceOrBl"/>
													<display:column title="Amount" property="amount" format="{0,number,###,###,##0.00}" style="text-align:right"/>
													<display:column title="Blue Screen Key" property="apCostKey" style="white-space: pre"/>
													<c:set var="blueScreenTotal" value="${blueScreenTotal+blueScreenAccrual.amount}"/>
													<display:footer>
														<tr class="textlabelsBold">
															<td>Total :&nbsp;&nbsp;&nbsp;<fmt:formatNumber value="${blueScreenTotal}" pattern="###,###,##0.00"/></td>
														</tr>
													</display:footer>
												</display:table>
											</div>
										</c:when>
										<c:otherwise>
											<div>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr>
														<td>No Blue Screen Accruals missed/conflict in Logiware</td>
													</tr>
													<tr>
														<td >Total :&nbsp;&nbsp;&nbsp;<fmt:formatNumber value="${blueScreenTotal}" pattern="###,###,##0.00"/></td>
													</tr>
												</table>
											</div>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr class="tableHeadingNew">
								<td colspan="4">Logiware Detail</td>
							</tr>
							<tr class="textlabelsBold">
								<td colspan="4">
									<c:choose>
										<c:when test="${not empty controlReportForm.logiwareAccruals}">
											<div>
												<c:set var="logiwareItemsSize" value="${fn:length(controlReportForm.logiwareAccruals)}"/>
												${logiwareItemsSize} Logiware Accruals missed/conflict in Blue Screen
											</div>
											<div class="scrolldisplaytable" style="height:100%">
												<display:table name="${controlReportForm.logiwareAccruals}" class="displaytagstyleNew" id="logiwareAccrual">
													<display:setProperty name="paging.banner.placement" value="none"/>
													<display:column title="Vendor" property="vendorName"/>
													<display:column title="Vendor#" property="vendorNumber"/>
													<display:column title="Invoice/Bl" property="invoiceOrBl"/>
													<display:column title="Amount" property="amount" format="{0,number,###,###,##0.00}" style="text-align:right"/>
													<display:column title="Blue Screen Key" property="apCostKey" style="white-space: pre"/>
													<c:set var="logiwareTotal" value="${logiwareTotal+logiwareAccrual.amount}"/>
													<display:footer>
														<tr class="textlabelsBold">
															<td>Total :&nbsp;&nbsp;&nbsp;<fmt:formatNumber value="${logiwareTotal}" pattern="###,###,##0.00"/></td>
														</tr>
													</display:footer>
												</display:table>
											</div>
										</c:when>
										<c:otherwise>
											<div>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr>
														<td>No Logiware Accruals missed/conflict in Blue Screen</td>
													</tr>
													<tr>
														<td>Total :&nbsp;&nbsp;&nbsp;<fmt:formatNumber value="${logiwareTotal}" pattern="###,###,##0.00"/></td>
													</tr>
												</table>
											</div>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr class="tableHeadingNew">
								<td colspan="4">Blue Screen Detail</td>
							</tr>
							<tr class="textlabelsBold">
								<td colspan="4">
									<c:choose>
										<c:when test="${not empty controlReportForm.blueScreenAccountReceivables}">
											<div>
												<c:set var="blueScreenItemsSize" value="${fn:length(controlReportForm.blueScreenAccountReceivables)}"/>
												${blueScreenItemsSize} Blue Screen Account Receivables missed/conflict in Logiware
											</div>
											<div class="scrolldisplaytable" style="height:100%">
												<display:table name="${controlReportForm.blueScreenAccountReceivables}" class="displaytagstyleNew" id="blueScreenAccountReceivable">
													<display:setProperty name="paging.banner.placement" value="none"/>
													<display:column title="Vendor" property="vendorName"/>
													<display:column title="Vendor#" property="vendorNumber"/>
													<display:column title="Invoice/Bl" property="invoiceOrBl"/>
													<display:column title="Amount" property="amount" format="{0,number,###,###,##0.00}" style="text-align:right"/>
													<c:set var="blueScreenTotal" value="${blueScreenTotal+blueScreenAccountReceivable.amount}"/>
													<display:footer>
														<tr class="textlabelsBold">
															<td>Total :&nbsp;&nbsp;&nbsp;<fmt:formatNumber value="${blueScreenTotal}" pattern="###,###,##0.00"/></td>
														</tr>
													</display:footer>
												</display:table>
											</div>
										</c:when>
										<c:otherwise>
											<div>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr>
														<td>No Blue Screen Account Receivables missed/conflict in Logiware</td>
													</tr>
													<tr>
														<td>Total :&nbsp;&nbsp;&nbsp;<fmt:formatNumber value="${blueScreenTotal}" pattern="###,###,##0.00"/></td>
													</tr>
												</table>
											</div>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr class="tableHeadingNew">
								<td colspan="4">Logiware Detail</td>
							</tr>
							<tr class="textlabelsBold">
								<td colspan="4">
									<c:choose>
										<c:when test="${not empty controlReportForm.logiwareAccountReceivables}">
											<div>
												<c:set var="logiwareItemsSize" value="${fn:length(controlReportForm.blueScreenAccountReceivables)}"/>
												${logiwareItemsSize} Logiware Account Receivables missed/conflict in Blue Screen
											</div>
											<div class="scrolldisplaytable" style="height:100%">
												<display:table name="${controlReportForm.logiwareAccountReceivables}" class="displaytagstyleNew" id="logiwareAccountReceivable">
													<display:setProperty name="paging.banner.placement" value="none"/>
													<display:column title="Vendor" property="vendorName"/>
													<display:column title="Vendor#" property="vendorNumber"/>
													<display:column title="Invoice/Bl" property="invoiceOrBl"/>
													<display:column title="Amount" property="amount" format="{0,number,###,###,##0.00}" style="text-align:right"/>
													<c:set var="logiwareTotal" value="${logiwareTotal+logiwareAccrual.amount}"/>
													<display:footer>
														<tr class="textlabelsBold">
															<td>Total :&nbsp;&nbsp;&nbsp;<fmt:formatNumber value="${logiwareTotal}" pattern="###,###,##0.00"/></td>
														</tr>
													</display:footer>
												</display:table>
											</div>
										</c:when>
										<c:otherwise>
											<div>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr>
														<td>No Logiware Account Receivables missed/conflict in Blue Screen</td>
													</tr>
													<tr>
														<td>Total :&nbsp;&nbsp;&nbsp;<fmt:formatNumber value="${logiwareTotal}" pattern="###,###,##0.00"/></td>
													</tr>
												</table>
											</div>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
					<tr class="tableHeadingNew">
						<td colspan="4">Difference in Detail</td>
					</tr>
					<tr class="textlabelsBold">
						<td colspan="4">
							<table border="0" cellspacing="3">
								<tr>
									<td width="35px"></td>
									<td width="35px">No. Of Records</td>
									<td width="35px">Total Amount</td>
								</tr>
								<tr>
									<td width="35px">Blue Screen</td>
									<td width="35px">${blueScreenItemsSize}</td>
									<td width="35px"><fmt:formatNumber value="${blueScreenTotal}" pattern="###,###,##0.00"/></td>
								</tr>
								<tr>
									<td width="35px">Logiware</td>
									<td width="35px">${logiwareItemsSize}</td>
									<td width="35px"><fmt:formatNumber value="${logiwareTotal}" pattern="###,###,##0.00"/></td>
								</tr>
								<tr>
									<td width="35px">Difference</td>
									<td width="35px">${blueScreenItemsSize - logiwareItemsSize}</td>
									<td width="35px"><fmt:formatNumber value="${blueScreenTotal-logiwareTotal}" pattern="###,###,##0.00"/></td>
								</tr>
							</table>
						</td>
					</tr>
				</c:if>
			</table>
		</html:form>
    </body>
	<c:if test="${not empty controlReportForm.fileName}">
		<script type="text/javascript">
			window.parent.showGreyBox('${controlReportForm.reportType} Control','${path}/servlet/FileViewerServlet?fileName=${controlReportForm.fileName}');
		</script>
	</c:if>
	<script type="text/javascript">
		function generateReport(){
			if(validate()){
				document.controlReportForm.action.value="generateReport";
				document.controlReportForm.submit();
			}
		}
		function printReport(){
			if(validate()){
				document.controlReportForm.action.value="print";
				document.controlReportForm.submit();
			}
		}
		function exportToExcel(){
			if(validate()){
				document.controlReportForm.action.value="exportToExcel";
				document.controlReportForm.submit();
			}
		}
		function validate(){
			if(trim(document.controlReportForm.createdDate.value)==""){
				alert("Please enter Created Date");
				return false;
			}else{
				return true;
			}
		}
	</script>
</html>
