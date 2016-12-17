<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@page import="javax.swing.JFileChooser"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	int fileCount = 0;
%>
 		<%@include file="../includes/baseResourcesForJS.jsp" %>
<div style=" border-bottom: 1px solid #aaa;
    border-top: 1px solid #999;
    border-left: 3px solid #ccc;
    border-right: 3px solid #ccc;">
<table width="100%"
	style="background-image: url(/logisoft/js/greybox/header_bg.gif);">
	<tbody>
<tr>
<td class="header"><font size="2.5" style="font-weight: bolder">Attachment List</font></td>
<td class="close" >
				<div style="vertical-align: top">
					<a id="lightBoxClose" href="#"	onclick=" closeAttachList();">
						<img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
					</a>
				</div>
</td>
</tr>
</tbody>
</table>
</div>
<br/>
<table>
	<tr>
		<td>
		<div id="divtablesty2" style=" width: 300px;height: 150px;overflow: auto" class="scrolldisplaytable">
	<display:table name="${attachmentList}"
		defaultorder="descending" class="displaytagstyle"
		id="attachListItem" style="width: 100%;">
		<display:setProperty name="paging.banner.some_items_found">
			<span class="pagebanner"> <font color="blue">{0}</font> Scan
				Details Displayed,For more Scan Details click on Page Numbers. <br>
			</span>
		</display:setProperty>
		<display:setProperty name="paging.banner.one_item_found">
			<span class="pagebanner"> One {0} displayed. Page Number </span>
		</display:setProperty>
		<display:setProperty name="paging.banner.all_items_found">
			<span class="pagebanner"> {0} {1} Displayed, Page Number </span>
		</display:setProperty>
		<display:setProperty name="basic.msg.empty_list">
			<span class="pagebanner"> No Records Found. </span>
		</display:setProperty>
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<display:setProperty name="paging.banner.item_name" value="Attach" />
		<display:setProperty name="paging.banner.items_name" value="Attaches" />
		<display:column title="Action">
			<input type="radio" name="fileCheckBox" id="checkbox" onclick="checkBoxIndex(this);"/>
			<input type="hidden" name="attachFileName"  id="attachFileName" value="${attachListItem.name}"/>
			<input type="hidden" name ="attachFileLocation" id="attachFileLocation" value="${attachListItem.absolutePath}"/>
		</display:column>
		<display:column title="File Name" property="name" sortable="true" headerClass="sortable">
		</display:column>
	</display:table>
		
			</div>
		</td>
		</tr>	
		</table>
		<div align="center"><input type="hidden" value="" id="checkBoxIndex">
 			<input type="button" id="Attach" value="Attach" onclick="copyFiles();">
 		</div>
 		
 		
