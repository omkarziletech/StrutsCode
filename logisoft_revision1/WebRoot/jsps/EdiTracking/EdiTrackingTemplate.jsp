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
			<td class="lightBoxHeader" width="80%" >
				Electronic Data Interchange Tracking
					<a id="lightBoxClose" href="javascript:closeEdiInfoListDiv();">
						<img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
					</a>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="textLabels" align="center" cellpadding="0" cellspacing="0" border="0">
					
					<tr class="textLabels">
						<td>DR/File #:</td>
						<td><c:out value="${ediBean.drNumber}"></c:out></td>
					</tr>
					<tr class="textLabels">
						<td>Message Type:</td>
						<td><c:out value="${ediBean.messageType}"></c:out></td>
					</tr>
					<tr class="textLabels">	
						<td>EDI Company:</td>
						<td><c:out value="${ediBean.ediCompany}"></c:out></td>
					</tr>
					<tr class="textLabels">
						<td>File Name:</td>
						<td><c:out value="${ediBean.fileName}"></c:out></td>
					</tr>
						<c:if test="${ediBean.messageType =='997'}">
							<tr class="textLabels">	
								<td>Booking Number:</td>
								<td><c:out value="${ediBean.bookingNumber}"></c:out></td>
							</tr>
							<tr class="textLabels">	
								<td>Scac Code:</td>
								<td><c:out value="${ediBean.scacCode}"></c:out></td>
							</tr>
						</c:if>
						<c:choose>
							<c:when test="${ediBean.status =='success' or ediBean.status =='change' or ediBean.status =='cancel'
                                                                        or ediBean.status =='pending' or ediBean.status =='conditionally accepted' 
                                                                         or ediBean.status =='declined' or ediBean.status =='replaced'}">
								<tr class="textLabels">
									<td align="center" colspan="3"><input type="button" class="buttonStyleNew" id="xmlFile" value="OpenXml" 
											style="width:100px" onclick="openFile()"/></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr class="textLabels">
									<td align="center" colspan="3"><input type="button" class="buttonStyleNew" id="logFile" value="OpenLogFile" 
									style="width:100px" onclick="openFile()"/></td>
								</tr>
							</c:otherwise>
						</c:choose>
				</table>
						<input type="hidden" id="filePath" value="${filePath}">
			</td>
		</tr>
	</tbody>
</table>
