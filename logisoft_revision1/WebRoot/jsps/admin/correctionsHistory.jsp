<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%@page import="com.gp.cong.logisoft.bc.notes.NotesConstants"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	List actionList = new ArrayList();
	actionList.add(new LabelValueBean("Select One","0"));
	actionList.add(new LabelValueBean("Void Corrections",NotesConstants.SHOW_DELETED_CORRECTION_EVENT_NOTES));
	actionList.add(new LabelValueBean("All Corrections",NotesConstants.SHOW_CORRECTION_EVENT_NOTES));
	request.setAttribute("actionList",actionList);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <%@include file="../includes/resources.jsp" %>
    <title>My JSP 'correctionsHistory.jsp' starting page</title>
    <%@include file="../includes/baseResources.jsp" %>
      <script language="javascript" src="<%=path%>/js/common.js"> </script>
       <script language="javascript">
      function showVoid(){
		    	document.notesForm.submit();
		    }
      </script>
  </head>
  
  <body class="whitebackgrnd">
  <html:form name="notesForm" type="com.gp.cong.logisoft.struts.form.NotesForm" action="/notes" scope="request">
 
  <table class="tableBorderNew" width="100%"  >
  <tr class="tableHeadingNew"><td> List of Corrections</td>
  <td align="right">Action&nbsp;<html:select property="actions" value="${notesForm.actions}" onchange="showVoid()" styleClass="textlabelsBoldForTextBox">
							<html:optionsCollection name="actionList"/>
						</html:select>&nbsp;</td>
  </tr><tr><td colspan="2">
   <display:table name="${notesList}"   defaultsort="1"  requestURI= "" defaultorder="descending" class="displaytagstyleNew" id="notetable" style="width:100%" sort="list"> 
	  	<display:column  style="width:1px;visibility:hidden"   property="id" title=""></display:column>
		<display:column title="Action" property="noteDesc"></display:column>
  		<fmt:formatDate pattern="dd-MMM-yyyy HH:mm a" var="date" value="${notetable.updateDate}"/> 
  		<display:column title="Date"><c:out value="${date}"></c:out></display:column>
  		<fmt:formatDate pattern="dd-MMM-yyyy HH:mm a" var="followupDate" value="${notetable.followupDate}"/> 
		<display:column title="user" property="updatedBy"></display:column>
		</display:table>
		</td>
        </tr></table>
         <html:hidden property="buttonValue"/>	
     <html:hidden property="documentName" value="${param.moduleId}"/>
     <html:hidden name="moduleId" property="moduleId" value="${param.moduleId}"/>
     <html:hidden name="moduleRefId" property="moduleRefId" value="${param.moduleRefId}"/>
     <html:hidden name="itemName" property="itemName" value="${itemName}"/>
     <html:hidden property="noteId"/>		
        </html:form>
  </body>
</html>
