<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<LabelValueBean> subTypeList = (List) request
			.getAttribute("subTypes");
	List<LabelValueBean> assignedList = (List) request
			.getAttribute("assignedSubTypes");
	String status = "No";
	String userId = (String) request.getAttribute("userId");
%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table width="100%"
	style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;" height="15px">
	<tbody>
		<tr>
			<td class="lightBoxHeader">
				Sub Types
			</td>
			<td>
				<div style="vertical-align: top;">
					<a id="lightBoxClose" href="javascript:closeRangeWindow();">
						<img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
					</a>
				</div>
			</td>
		</tr>
	</tbody>
</table>
<table align="center">
	<%
		for (LabelValueBean label : subTypeList) {
			if (null != label.getLabel()
					&& !label.getLabel().trim().equals("Select One") && !label.getLabel().trim().equals("")) {
					if(null!=assignedList && !assignedList.isEmpty()){
						for (Object assingLabel : assignedList) {
							if (null != assingLabel
									&& assingLabel.toString().trim().equals(
											label.getLabel().trim())) {
								status = "Yes";
								break;
							} else {
								status = "No";
							}
						}
					}
	%>
	<tr class="textlabelsBold">
		<td>
			<%
				if (status.trim().equals("Yes")) {
			%>
			<input type="checkbox" checked="checked" id="subType" name="subType"
				value="<%=label.getValue()%>" /><%=label.getLabel()%>
			<%
				} else {
			%>
			<input type="checkbox" id="subType" name="subType"
				value="<%=label.getValue()%>" /><%=label.getLabel()%>
			<%
				}
			%>
		</td>
	</tr>
	<%
		}
		}
	%>
	<tr>
		<td colspan="2" align="center">
			<input type="hidden" name="userId" id="userId" />
			<input type="button" class="buttonStyleNew" value="Assign" onClick="assignApSpecialist('<%=userId%>');" />
		</td>
	</tr>
</table>
