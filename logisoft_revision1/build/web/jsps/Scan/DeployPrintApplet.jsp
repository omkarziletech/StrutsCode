<html>
<body>
 <%@ page language="java" %>

 <%!

 /**
 * This JSP can be run from any location.
 * The only requirement for this JSP is that
 * it will be expecting 2 session variables
 * that we will pass in as parameters to the
 * applet.
 */







//session variables we pass to the applet as parameters
String fileName = "";
String filePath = "";
String path = System.getProperty("java.library.path");
%>



<%
//session variables for filename and filepath
//fileName = session.getAttribute( "FileName" ) != null ? session.getAttribute( "FileName" ).toString() : null;
//filePath = session.getAttribute( "FileLocation" ) != null ? session.getAttribute( "FileLocation" ).toString() : null;
filePath = request.getParameter("fileName");
session.removeAttribute("fileList");
if((fileName == null) || (filePath == null)){
	//show error
	%> file name and file path parameters are null <%
	
	}else { //now we load the applet with the parameters

	%>
	 <jsp:plugin code="PrintApplet.class" codebase="/logisoft/jsps/Scan/" type="applet" width="300" height="100" archive="print.jar,PDFRenderer.jar">
	 	<jsp:params>
	 		<jsp:param name="printerName" value=""/>
	 		<jsp:param name="fileLocation" value="<%=filePath%>"/>
	 	</jsp:params>
	 </jsp:plugin>
 	<% 
 }//end else 

%>
 </html>
