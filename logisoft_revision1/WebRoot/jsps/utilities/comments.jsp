<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
  <head>
    <base href="<%=basePath%>">
     <%@include file="../includes/baseResources.jsp"%>
		<script language="javascript" src="<%=path%>/js/common.js"></script>
		<%@include file="../includes/resources.jsp" %>
    <title>Comments</title>
    <script type="text/javascript">
    function search(){
    	document.comments.buttonValue.value="Search"
    	document.comments.submit();
    }
    function deleteComments(module,chargeId){
   		 document.comments.module.value=module;
    	document.comments.chargeId.value=chargeId;
    	document.comments.buttonValue.value="deletCharges"
    	document.comments.submit();
    }
    </script>
  </head>
  
  <body class="whitebackgrnd">
   <html:form action="/comments" scope="request">
   <html:hidden property="buttonValue"/>
    <html:hidden property="chargeId"/>
        <html:hidden property="module"/>
    <table border="0" class="tableBorderNew" width="100%">
        <tr class="tableHeadingNew"><font style="font-weight: bold">Search Comments</font></tr>
    	<tr class="textlabelsBold">
    	<td>CommentsFrom</td>
    	 <td><html:select property="action" styleClass="verysmalldropdownStyleForText" value="${commentsForm.action}">
                    	<html:option value="All">All</html:option>
                    	<html:option value="Quote">Quote</html:option>
                    	<html:option value="Booking">Booking</html:option>
                    	<html:option value="FclBlCharges">FclBlCharges</html:option>
                    	<html:option value="FclBlCostCode">FclBlCostCode</html:option>
                    </html:select>
                    </td>
    	<td>FileNumber</td>
    	<td><html:text property="fileNo"  value="${commentsForm.fileNo}" styleClass="textlabelsBoldForTextBox" ></html:text> 
    	<input type="button" class="buttonStyleNew" value="Search"  
                               onclick="search()" /> </td>
    	<td></td>
    	</tr>
    </table>
    <br>
    <table width="100%" class="tableBorderNew">
        <tr class="tableHeadingNew"><font style="font-weight: bold">CommentsList</font></tr>
    	<tr>
    	<td>
    	<display:table  name="${commentsList}"  id="commentsTable" class="displaytagstyle" pagesize="<%=pageSize%>" > 
			<display:setProperty name="basic.msg.empty_list">
			<span style="display:none;" class="pagebanner" /> 
			</display:setProperty>
			<display:setProperty name="paging.banner.placement" value="false">
		<span style="display:none;"></span>
		</display:setProperty>
		<display:column title="ChargCode" property="chargesCode">
		</display:column>
		<display:column title="Comments" maxWords="15">
					<c:out value="${commentsTable.comments}"/>
		</display:column>
		<display:column title="Module" property="module" maxWords="15"/>
		<display:column title="Action">
		<img src="<%=path%>/img/icons/delete.gif" onclick="deleteComments('${commentsTable.module}','${commentsTable.chargesId}')" id="deleteimg" />
		</display:column>
		</display:table>
    	</td>
    	</tr>
    </table>
   </html:form>
  </body>
</html>
