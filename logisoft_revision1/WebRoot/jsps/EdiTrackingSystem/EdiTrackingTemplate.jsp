<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="basePath"
	value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
<table width="100%">
	<tbody>
		<tr	style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;" height="15px">
			<td class="lightBoxHeader">
				Electronic Data Interchange Tracking
			</td>
			<td class="lightBoxHeader">
				<div style="vertical-align: top">
					<a id="lightBoxClose" href="javascript:closeEdiInfoListDiv();">
						<img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
					</a>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="textLabels" align="center" cellpadding="0" cellspacing="0" border="0">
					
					<tr class="textLabels">
						<td class="textlabelsBold">DR/File #:</td>
						<td><c:out value="${ediBean.drNumber}"></c:out></td>
					</tr>
					<tr class="textLabels">
						<td class="textlabelsBold">Message Type:</td>
						<td><c:out value="${ediBean.messageType}"></c:out></td>
					</tr>
					<tr class="textLabels">	
						<td class="textlabelsBold">EDI Company:</td>
						<td><c:out value="${ediBean.ediCompany}"></c:out></td>
					</tr>
					<tr class="textLabels">
						<td class="textlabelsBold">File Name:</td>
						<td><c:out value="${ediBean.fileName}"></c:out></td>
					</tr>
							<tr class="textLabels">	
								<td class="textlabelsBold">Booking Number:</td>
								<td><c:out value="${ediBean.bookingNumber}"></c:out></td>
							</tr>
							<tr class="textLabels">	
								<td class="textlabelsBold">Scac Code:</td>
								<td><c:out value="${ediBean.scacCode}"></c:out></td>
							</tr>
                            <tr class="textLabels">
								<td class="textlabelsBold">Origin:</td>
								<td><c:out value="${ediBean.placeOfReceipt}"></c:out></td>
							</tr>
                            <tr class="textLabels">
								<td class="textlabelsBold">POL:</td>
								<td><c:out value="${ediBean.portOfLoad}"></c:out></td>
							</tr>
							<tr class="textLabels">
								<td class="textlabelsBold">POD:</td>
								<td><c:out value="${ediBean.portOfDischarge}"></c:out></td>
							</tr>
							<tr class="textLabels">
								<td class="textlabelsBold">Destination:</td>
								<td><c:out value="${ediBean.placeOfDelivery}"></c:out></td>
							</tr>
                            <tr class="textLabels">
								<td class="textlabelsBold">Origin City:</td>
								<td><c:out value="${ediBean.placeOfReceiptCity}"></c:out></td>
							</tr>
                            <tr class="textLabels">
								<td class="textlabelsBold">POL City:</td>
								<td><c:out value="${ediBean.portOfLoadCity}"></c:out></td>
							</tr>
							<tr class="textLabels">
								<td class="textlabelsBold">POD City:</td>
								<td><c:out value="${ediBean.portOfDischargeCity}"></c:out></td>
							</tr>
							<tr class="textLabels">
								<td class="textlabelsBold">Destination City:</td>
								<td><c:out value="${ediBean.placeOfDeliveryCity}"></c:out></td>
							</tr>
					<%--<tr class="textLabels">
						<td>Error Description:</td>
						<td><span><c:out value="${ediBean.description}"></c:out></span></td>
					</tr>--%>
						<c:choose>
							<c:when test="${ediBean.status =='success'}">
								<tr class="textLabels">
									<td align="center" colspan="3"><input type="button" class="buttonStyleNew" id="xmlFile" value="ViewXml"
											style="width:100px" onclick="openFile('${ediBean.id}','${ediBean.messageType}')"/></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr class="textLabels">
									<td align="center" colspan="3"><input type="button" class="buttonStyleNew" id="logFile" value="ViewLogFile"
									style="width:100px" onclick="openFile('${ediBean.id}','${ediBean.messageType}')"/></td>
								</tr>
							</c:otherwise>
						</c:choose>
				</table>
						<input type="hidden" id="filePath" value="${filePath}">
			</td>
		</tr>
	</tbody>
</table>
