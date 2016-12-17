<%@ page language="java" import="java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%
if(request.getParameter("futureratesload")!=null){
session.setAttribute("futurerates","futurerates");
}
String path = request.getContextPath();
boolean flag=false;
List li=new ArrayList();
String  denailListName = new String();
int  size=0;
String  successfullyLaoded="";
	if(request.getAttribute("successfullyLoaded")!=null){
		successfullyLaoded=(String)request.getAttribute("successfullyLoaded");
	}
String  notinserted= new String();

if(request.getAttribute("denailList")!=null){
	Set denilarecords=(Set)request.getAttribute("denailList");
	
	Iterator getDeinalRecords=denilarecords.iterator();
	while(getDeinalRecords.hasNext()){
		denailListName = denailListName+(String)getDeinalRecords.next()+"\n";
	 }
	}
 if(session.getAttribute("notInsertlist")!=null){
 
	li=(List)session.getAttribute("notInsertlist");
	for(int i=0;i<li.size();i++){
	notinserted = notinserted+(String)li.get(i)+",";
	 }

}
if(session.getAttribute("list")!=null){
session.removeAttribute("list");
} 
if(session.getAttribute("notInsertlist")!=null){
 
session.removeAttribute("notInsertlist");
}
%>


<head>
<title>Load FCL popup </title>


<head>
		<title>JSP for SearchLCLColoadForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	<script type="text/javascript">
	    function getBack(val1) { 
	   		 
	      	if(document.loadFCLPopupForm.hiddenorigins.value!=""){
	      	document.loadFCLPopupForm.style.visibility = 'hidden';
	      		alert("Unfortunately we have encountered the following errors while loading.\nKindly Review it and reupload. \n\n"+document.loadFCLPopupForm.hiddenorigins.value);
				
				opener.location.href="<%=path%>/jsps/ratemanagement/SearchFCL.jsp";     	
			}
			if(val1!=""){
				document.loadFCLPopupForm.style.visibility = 'hidden';
				self.close();
				alert(val1);
				self.close();
				opener.location.href="<%=path%>/jsps/ratemanagement/SearchFCL.jsp";     	
				}
			
  	    }
  	    function showProccessBar(){
  	 var newwindow;
  	 
  	    	newwindow=window.open("<%=path%>/jsps/ratemanagement/fileupload.jsp",'name','height=400,width=200');
			if (window.focus) {
				newwindow.focus()
			}

  	    	
  	    }
  	   </script>
</head>

<body class="whitebackgrnd" onload="getBack('<%=successfullyLaoded%>')">

<%--<div style="float:left;"><a href="/ct2/"><img src="<%=path%>/img/bigrotation.gif" width="202" height="73" border="0" /></a></div><br style="clear:both;"/>--%>

<div id="processingImage" style="visibility:hidden" align="center">
	<img src="<%=path%>/img/bigrotation.gif"  border="0"/>
</div>

<html:form action="/loadFCLPopup" method="post" enctype="multipart/form-data" name="loadFCLPopupForm"
          type="com.gp.cong.logisoft.struts.ratemangement.form.LoadFCLPopupForm" scope="request">

<table>
<tr class="textlabels">
<td align="center" colspan="2">  
</td>
</tr>

<tr class="textlabels"> 
<td align="left" colspan="2">
<font color="red"><html:errors/></font>
</tr>


<tr class="textlabels">
<td align="right" id="fileTitle" >
File Name
</td>
<td align="left">
<html:file property="theFile"/> 
</td>
</tr>


<tr>
<td align="center" colspan="2">
<html:submit property="uploadButton" styleClass="buttonStyleNew" onclick=" if(document.loadFCLPopupForm.theFile.value=='')
{ alert('Please choose a file to upload');return false;}
else {document.getElementById('theFile').style.visibility = 'hidden';
document.getElementById('fileTitle').style.visibility = 'hidden';
document.getElementById('uploadButton').style.visibility = 'hidden';
document.getElementById('processingImage').style.padding = '40px';
document.getElementById('processingImage').style.visibility = 'visible'; 
return true;}">Upload File</html:submit>
</td>
</tr>
</table>

<input type="hidden" name="hiddenorigins" value="<%=denailListName %>">
<input type="hidden" name="notinsert" value="<%=notinserted%>">
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>

