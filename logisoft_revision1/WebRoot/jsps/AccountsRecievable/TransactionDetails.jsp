<%@ page language="java"
	import="java.util.*,com.gp.cong.common.CommonConstants"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'TransactionDetails.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
		<form name="transactionDetails">
			<%
				Map transactionDetails = (Map) request.getAttribute(CommonConstants.TRANSACTION_DETAILS_MAP);
				Object[] transaction = null;
				List trailerNumbers = null;
				if (null != transactionDetails) {
					transaction = (Object[]) transactionDetails.get(CommonConstants.TRANSACTION_INFORMATION);
					trailerNumbers = (List) transactionDetails
							.get(CommonConstants.TRAILER_NUMBERS);
				}
				String trailerNoLabel = null;
				if(null == trailerNumbers) {
					trailerNumbers = new ArrayList();
				}
			%>
			<table align="center" width="100%" border="0" cellpadding="0"
				cellspacing="0" class="tableBorderNew">
				<tr class="tableHeadingNew" height="10%">
					<td>

						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr class="tableHeadingNew">
								<td>
									Transaction Details
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<div id="divtablesty1">
							<table border="0" cellpading="10" cellspacing="10">
								<tr class="textlabels">
									<td align="left">
										<b>Invoice Number : </b></td><td width="100" class="error">
										
										<%=null != transaction && null != transaction[5] ? transaction[5]: ""%>
									</td>
									<td>
										<b>BOL Number : </b></td><td class="error"><%=null != transaction && null != transaction[4] ? transaction[4]: ""%></td>
								</tr>
								<tr class="textlabels">
									<td align="left">
										<b>Vessel Number : </b></td><td width="100">
										<%=null != transaction && null != transaction[0] ? transaction[0]: ""%>
									</td>
									<td>
										<b>Voyage Number : </b></td><td><%=null != transaction && null != transaction[1] ? transaction[1]: ""%></td>
								</tr>
								<tr class="textlabels">
									<td>
										<b>MasterBL : </b></td><td><%=null != transaction && null != transaction[2] ? transaction[2]: ""%></td>
									<td>
										<b>Sub HouseBL : </b></td><td><%=null != transaction && null != transaction[3] ? transaction[3]: ""%></td>
								</tr>
								<tr class="textlabels">
								   <td><b>BL Terms: </b></td>
								   <td><%=null != transaction && null != transaction[6] ? transaction[6]: ""%></td>
								</tr>
								<tr class="textlabels">
									<td>
										<b>Trailer Number : </b></td><td colspan="3" align="left">
										<%int count = 0;
											for(Object trailerNo : trailerNumbers){ 
												
												 if(null == trailerNoLabel && null != trailerNo) {
												 	trailerNoLabel = trailerNo.toString();
												 	count++;
												 }else if(null != trailerNo) {
												 	//reset count if it reached 3
													if(count == 3) {
														trailerNoLabel = trailerNoLabel+", <br/>"+trailerNo.toString();
														count = 0;
													}else {
														trailerNoLabel = trailerNoLabel+", "+trailerNo.toString();
													}
												 	
												 	count++;
											 	}
										}%>
										<%=null != trailerNoLabel ? trailerNoLabel : ""%>
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
