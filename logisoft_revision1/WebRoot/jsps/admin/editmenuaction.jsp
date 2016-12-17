<%@ page language="java"  import="com.gp.cong.logisoft.bc.notes.NotesConstants,com.gp.cong.logisoft.util.DBUtil,java.util.List"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
com.gp.cong.logisoft.domain.Item item = new com.gp.cong.logisoft.domain.Item();
String modify = null;
modify=(String)session.getAttribute("modifyformenu");
if(session.getAttribute("item")!=null)
{
   item=(com.gp.cong.logisoft.domain.Item)session.getAttribute("item");
}
String msg="";

if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
	
}

// Name:Yogesh Date:12/01/2007(mm/dd/yy)  ----> Is view only when page is locked
if(session.getAttribute("view")!= null)
{
	modify=(String)session.getAttribute("view");
}


request.setAttribute("predecessorList",dbUtil.getPredecessorList(item.getItemDesc()));	

%>
<html> 
<head>
	<title>Edit Menu/Action</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script language="javascript" type="text/javascript">
	function saveValues()
	{
		 
		 if(document.editmenuaction.itemname.value=="")
		 {
				alert("Please enter Item name");
				document.editmenuaction.itemname.focus();
				return false;
		 }
		var val=document.editmenuaction.itemname.value;
			
			for(var i=0;i< val.length;i++)
			{
				if(val.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}	
		
	
		document.editmenuaction.buttonValue.value="save";
		document.editmenuaction.submit();
		return true;
	}
		
	function cancelbtn(val)
	{
	
	if(val==0 || val==3)
	{
	document.editmenuaction.buttonValue.value="cancelview";
	}
	else
	{
	   	document.editmenuaction.buttonValue.value="cancel";
	 }
     	document.editmenuaction.submit();
   	}
   	
   	function disabled(val1,val2)
   {
  
	if(val1 == 0 || val1== 3)
	{		
        var imgs = document.getElementsByTagName('img');
   	 	document.getElementById("save").style.visibility = 'hidden';
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id != "cancel" )
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		   
   		    document.getElementById("save").style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue")
	 		{
	  			input[i].readOnly=true;
				input[i].style.color="blue";
			}
  	 	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
  	 	
  	 }
  	 if(val1 == 1)
  	 {
  	 	document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 if(val1==3 && val2!="")
		{
			alert(val2);
		}		
  	 
   }
   
   	function confirmdelete()
	{
		
		document.editmenuaction.buttonValue.value="delete";
    	var result = confirm("Are you sure you want to delete this user");
		if(result)
		{
   		document.editmenuaction.submit();
   		}	
   	}
   	 function confirmnote()
	{
		document.editmenuaction.buttonValue.value="note";
    	document.editmenuaction.submit();
   	}
   	
   	
	</script>
	 <%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=msg%>')">
<html:form action="/editmenuaction" name="editmenuaction" type="com.gp.cong.logisoft.struts.form.EditmenuactionForm" scope="request">
<font color="blue"><h4><%=msg%></h4></font>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Menu/Action Items </tr>
 <tr> 	
   <td>
	<table width="100%" height="28" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="right">
        <input type="button" class="buttonStyleNew" id="save" name="save" onclick="saveValues()"  value="Save"/>
        <input type="button" class="buttonStyleNew"  onclick="cancelbtn(<%=modify%>)" name="search" value="Go Back"/>
        <input type="button" class="buttonStyleNew" onclick="confirmnote()" id="note" name="search" value="Note"/>
          <input type="button" class="buttonStyleNew" id="note" onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId='+'<%=NotesConstants.EDITMENUACTION%>',300,700);" id="note" name="search" value="Note"/>      
       </td>
        
    </tr>
    </table>
  </td>
</tr> 
 
<tr>
   <td>
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
       <tr>
          <td width="119" height="34"><label class="style2">&nbsp;Item Name</label></td>
          <td width="245">&nbsp;<html:text property="itemname" value="<%=item.getItemDesc()%>" styleClass="areahighlightyellow" maxlength="25"/></td>
          <%
          if(item.getProgramName()!=null && item.getProgramName()!= "" )
          {
          %>
          <td width="98"><label class="style2">Program Name</label> </td>
          <td width="333"><html:text property="programname" value="<%=item.getProgramName()%>" styleClass="areahighlightyellow" /></td>
          <% } %>
      </tr>
    
       <tr>
          <td height="31"><label class="style2">&nbsp;Predecessor</label></td>
          <td>&nbsp;<html:select property="predecessor" styleClass="selectboxstyle" value="<%=item.getParentId()%>" disabled="true">
                 <html:optionsCollection name="predecessorList"/>
              </html:select></td>
          <td><label class="style2">&nbsp;Unique Code</label></td>
          <td><html:text property="itemCode" value="<%=item.getUniqueCode() %>" readonly="true"></html:text></td>
        </tr>
        
      </table></td>
  	</tr>
</table>
		<html:hidden property="buttonValue" styleId="buttonValue" />
		<html:hidden property="editView"/>
		</html:form>
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

