<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String remarks = request.getParameter("remarks");
String remarksNew = remarks.trim().replaceAll("'", "\\\\'").replaceAll("\"", "&quot;");


%>

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'PortSearch.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/ajax.js"></script>
	<script type="text/javascript">
		function fillRemarks(ev){
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                            methodName: "getSpecialRemarks",
                            param1: ev
                        },
                        success: function (data) {
                            showSpecialRemarks(data);
                        }
                    });
	  	}
	  	function showSpecialRemarks(data) {
	  		document.getElementById('remark').value=data;
	  	}
	  	function assignTextValue(){
	  	   if(document.getElementById('accecptRemarks').checked == true){
		  	    parent.parent.GB_hide();
			 	parent.parent.assignRemarksValue(document.getElementById('remark').value);
	  	   }else{
	  	     	alert("Please accept special remarks");
	 	    	return;
	  	   }
	  	   if(parent.parent.document.getElementById("zip")!="undefined"){
	  			parent.parent.document.getElementById("zip").focus();
	  	   }
	  	}
	  	function setFocus(){
	  		setTimeout("set()",500);
	  	}
	  	function set(){
	  	
	  		document.getElementById('accecptRemarks').focus();
	  		
	  	}
	</script>

  </head>
  <%@include file="../includes/resources.jsp" %>
  <body class="whitebackgrnd">
    <form name="remarksFromDojo">
    		<table class="tableBorderNew" width="100%" cellpadding="0" cellspacing="0" border="0">
    			<tr class="tableHeadingNew">Port Remarks For <c:out value="${param.remarks}"></c:out></tr>
    			<tr>
    			   	<td align="center" style="padding-top: 20px;width: 100%;">
    			       	<textarea  class="textlabelsBoldForTextBox" rows="4" cols="100" id="remark"
    			       	 name="remark"  value=<%=remarks%> readonly="true"></textarea>
				  	</td>
    			 </tr>
    			 <tr>
    			   	<td align="center" class="textlabels">
    			   	I have read and understood the remarks
    			         <input type="checkbox" id="accecptRemarks" name="accecptRemarks">
    			   	</td>
    			</tr>
    			<tr>
    			   <td align="center">
    			    
						<input type="button" class="buttonStyleNew" value="OK" onClick="assignTextValue()"/></td>
				</tr>
    			
    		</table>
    	
								
	</form>
  </body>
  <script>setFocus();fillRemarks("<%=remarksNew%>");</script>
  	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
