<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
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

		<title>My JSP 'ShowSubsidiaries.jsp' starting page</title>

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
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew">
				<tr class="tableHeadingNew" height="10%">
					<td>

						<table width="100%" cellpadding="0" cellspacing="0">
							<tr class="tableHeadingNew">
								<td>
									List Of Subsidiaries
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<div id="divtablesty1"
							style="height: 100%; overflow: auto">
							<table border="0" cellpadding="0" cellspacing="0">
								<display:table name="${subsidiariesList}" pagesize="<%=pageSize%>" class="displaytagstyle" id="customertable" sort="list" > 
		
								<display:setProperty name="paging.banner.some_items_found">
						    				<span class="pagebanner">
						    					<font color="blue">{0}</font> Customer details displayed,For more Customers click on page numbers.
						    				<br>
						    				</span>
						  			  </display:setProperty>
						  			  <display:setProperty name="paging.banner.one_item_found">
						    				<span class="pagebanner">
												One {0} displayed. Page Number
											</span>
						  			  </display:setProperty>
						  			   <display:setProperty name="paging.banner.all_items_found">
						    				<span class="pagebanner">
												{0} {1} Displayed, Page Number
											</span>
						  			  </display:setProperty>
						    			<display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
											No Records Found.
										</span>	
										</display:setProperty>
										<display:setProperty name="paging.banner.placement" value="bottom" />
									<display:setProperty name="paging.banner.item_name" value="Customer"/>
						  			<display:setProperty name="paging.banner.items_name" value="Customers"/>
						
								<display:column property="accountName" title="Customer Name"/>		
								<display:column property="accountNumber" title="Acct#"/>
								<display:column title="Acct Type" property="accountType"></display:column>
								<display:column title="Master Acct#" property="masterAccountNumber"></display:column>
								<display:column title="Addr" property="address"></display:column>
								<display:column title="City" property="city"></display:column>
								<display:column title="State" property="state"></display:column>
								 </display:table>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
