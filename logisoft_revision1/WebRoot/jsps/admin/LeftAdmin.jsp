<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.util.test"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
test test=new test();
String path1=path+"/newRole.do";
test.getTreeStucture(request);


%>

<HTML>

<!--------------------------------------------------------------->
<!-- Copyright (c) 2006 by Conor O'Mahony.                     -->
<!-- For enquiries, please email GubuSoft@GubuSoft.com.        -->
<!-- Please keep all copyright notices below.                  -->
<!-- Original author of TreeView script is Marcelino Martins.  -->
<!--------------------------------------------------------------->
<!-- This document includes the TreeView script.  The TreeView -->
<!-- script can be found at http://www.TreeView.net.  The      -->
<!-- script is Copyright (c) 2006 by Conor O'Mahony.           -->
<!--------------------------------------------------------------->

 <HEAD>

  <STYLE>
   .headerbluelarge
 {
 font-size:16px;
 color:#3399CC;
 font-family:Arial;
 font-weight:bold;
 
  }
    BODY {
      background-color: #E6F2FF;}
    TD {
      font-size:12px;
      font-family:Arial, Helvetica, sans-serif;
      text-decoration: none;
      white-space:nowrap;}
    A {
      text-decoration: none;
      color: black;}
  </STYLE>
  
  <BODY topmargin="16" marginheight="16" class="whitebackgrnd">
  <FORM id="newRole" name="newRole" action="<%=path1%>" method="post">

  
  <!-- Code for browser detection. DO NOT REMOVE.             -->
  <SCRIPT src="<%=Application.JS_PATH%>/common/ua.js"></SCRIPT>

  <!-- Infrastructure code for the TreeView. DO NOT REMOVE.   -->
  <SCRIPT src="<%=Application.JS_PATH%>/common/ftiens4.js"></SCRIPT>

  <!-- Scripts that define the tree. DO NOT REMOVE.           -->
  <SCRIPT SRC="<%=Application.JS_PATH%>/common/demoCheckboxNodes1.js"></SCRIPT>


  <SCRIPT>

    // NOTE: This function depends on the actual prependHTML strings used in the
    // configuration file (dmeoCheckboxNodes.js). If you change these strings,
    // you will need to change this function.
    function generateQueryString() {
      var retStr
      // The getElById function is defined in ftiens4.js and helps with cross-
      // browser compatibility
      retStr = "BOX1=" + getElById('BOX1').checked
      retStr = retStr + "&BOX2=" + getElById('BOX2').checked
      retStr = retStr + "&BOX3=" + getElById('BOX3').checked
      retStr = retStr + "&RD1=" + getElById('RD1').checked
      retStr = retStr + "&RD2=" + getElById('RD2').checked
      retStr = retStr + "&RD3=" + getElById('RD3').checked
      return retStr;
    }

    // NOTE: If you are using a frameless layout, you need to change this function
    // so it reloads "self" rather than reloading the right frame.
    function submitTreeForm() {
      window.open("demoCheckboxRightFrame.html?" + generateQueryString(), "basefrm")
    }

	
  </SCRIPT>
<script language="javascript" type="text/javascript">
	function redirect(val){
		
		if(val==3){
		window.location.href="searchrole.html"
		}
		}
		

		</script>

  <TITLE>TreeView Demo: Checkboxes and Radio Buttons for a Form</TITLE>

	<%@include file="../includes/baseResources.jsp" %>



 
 <table width="688" border="0" cellspacing="0" cellpadding="0">
  <tr>
    
  </tr>
</table>

<table width="389" border="0" cellspacing="0" cellpadding="0">
      
     
    </table>

<!------------------------------------------------------------->
  <!-- IMPORTANT NOTICE:                                       -->
  <!-- Removing the following link will prevent this script    -->
  <!-- from working.  Unless you purchase the registered       -->
  <!-- version of TreeView, you must include this link.        -->
  <!-- If you make any unauthorized changes to the following   -->
  <!-- code, you will violate the user agreement.  If you want -->
  <!-- to remove the link, see the online FAQ for instructions -->
  <!-- on how to obtain a version without the link.            -->
  <!------------------------------------------------------------->
  <DIV style="position:absolute; top:0; left:0;"><TABLE border=0><TR><TD><FONT size=-2><A style="font-size:7pt;text-decoration:none;color:silver" href="http://www.treemenu.net/" target=_blank>Javascript Tree Menu</A></FONT></TD></TR></TABLE></DIV>

  <!-- The form-related tags in this file are needed only to  -->
  <!-- demonstrate the checkbox functionality.                -->



   
      <!-- Build the browser's objects and display default view  -->
      <!-- of the tree.                                          -->
      <SCRIPT>initializeDocument()</SCRIPT>
   
   
    
    <p>&nbsp;  </p>
 <table>
  
 </table>
 

 </FORM> 
  </BODY>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</HTML>