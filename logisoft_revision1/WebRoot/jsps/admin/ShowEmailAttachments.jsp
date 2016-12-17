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

		<title>My JSP 'ShowEmailAttachments.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function showAttachment(file){
            window.open('<%=path%>/servlet/PdfServlet?fileName='+file,'','toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=450');
            parent.parent.GB_hide();
 		}
		
	</script>
	</head>

	<body>
		<form name="emailAttachements">
			<table align="center" width="100%" border="0" cellpadding="0"
				cellspacing="0" class="tableBorderNew" style="width:100%;height:100%;overflow:auto">
				<tr class="tableHeadingNew" height="10%">
					<td>

						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr class="tableHeadingNew">
								<td>
									Attachments
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<div id="divtablesty1">
							<table border="0" cellpading="10" cellspacing="10">
								<c:forEach var="file" items="${attachments}">
								<tr class="textlabels">
									<td align="left">
										<a href="#" onclick="showAttachment('${file}')"><c:out value="${file}"></c:out></a>
									</td>
								</tr>
								</c:forEach>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
