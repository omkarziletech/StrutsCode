<%@ page language="java" import="com.gp.cong.logisoft.bc.notes.NotesConstants,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.Item"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
com.gp.cong.logisoft.domain.User user=new com.gp.cong.logisoft.domain.User();
if(session.getAttribute("user")!=null)
{
   user=(com.gp.cong.logisoft.domain.User)session.getAttribute("user");
}
Item item=new Item();
if(session.getAttribute("item")!=null)
{
item=(Item)session.getAttribute("item");
}
String msg="";

if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
	
}	
request.setAttribute("predecessorList",dbUtil.getPredecessorList(null));


%>
<html> 
	<head>
		<title>Add Menu/Action Items</title>
		<%@include file="../includes/baseResources.jsp" %>

        <script language="javascript" src="<%=path%>/js/common.js" ></script>
        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
  	<script type="text/javascript">
  	function checkUniqueCode(){
            document.getElementById('a').style.visibility="hidden";
            if(event.keyCode==9 || event.keyCode==13){
                var itemCode = document.getElementById('itemCode').value;
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.ItemDAO",
                        methodName: "getItemCodeNotEqual"
                        param1: itemCode
                    },
                    success: function (data) {
                        if(data){
                            document.getElementById('itemCode').value="";
                            document.getElementById('a').style.visibility="visible";
                            document.getElementById('a').className="bodybackgrnd";
                        }
                    }
                });
            }
  	}
  	</script>
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
	<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    dojo.require("dojo.io.*");
			dojo.require("dojo.event.*");
			dojo.require("dojo.html.*");
	</script>
		<script language="javascript" type="text/javascript">
		
		function defaultValues()
		{
			document.addMenuactionForm.checkbox.checked=true;
			
			if(document.addMenuactionForm.checkbox.checked==true)
			{
				document.addMenuactionForm.programname.disabled=true;
			}
		}
		function enabledisableprogramname()
		{
			if(document.addMenuactionForm.checkbox.checked==false)
			{
				document.addMenuactionForm.programname.disabled=false;
			}
			else
			{
				document.addMenuactionForm.programname.disabled=true;
			}
		}
		function save()
		{
			
			if(document.addMenuactionForm.itemname.value=="")
			{
				alert("Please enter Item name");
				document.addMenuactionForm.itemname.focus();
				return false;
			}
			var val=document.addMenuactionForm.itemname.value;
			
			for(var i=0;i< val.length;i++)
			{
				if(val.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
			
			if(document.addMenuactionForm.checkbox.checked==false && document.addMenuactionForm.programname.value=="")
			{
				alert("Please enter Program name");
				document.addMenuactionForm.programname.focus();
				return false;
			}
			if(document.addMenuactionForm.predecessor.value=="0")
			{
				alert("Please select a Predecessor");
				return false;
			}
			
            
             if(document.addMenuactionForm.programname.value.match(" "))
           {
            	alert("Space is not allowed for Program Name");
            	return;
           }
			document.addMenuactionForm.buttonValue.value="save";
   			document.addMenuactionForm.submit();
			return true;
		}
		
		function cancel()
   		{
   			
         	document.addMenuactionForm.buttonValue.value="cancel";
       	 	document.addMenuactionForm.submit();
   		}
		
		
		</script>
		 <%@include file="../includes/resources.jsp" %>
	</head>
<body class="whitebackgrnd">
<html:form action="/addMenuaction" scope="request">
<font color="blue"><h4><%=msg%></h4></font>

<table width="100%" border="0" cellspacing="5" cellpadding="10" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Add Menu/Action Items</tr>
<tr>        	
        	<td align="right">
        	<input type="button" class="buttonStyleNew" onClick="if(!save())return false" name="Save" value="Save"/>
        	<input type="button" class="buttonStyleNew" onclick="cancel()" name="search" value="Go Back"/>
        	        <input type="button" class="buttonStyleNew" id="note" onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId='+'<%=NotesConstants.MENUACTION%>',300,700);" id="note" name="search" value="Note"/>
        	
</tr>

<tr>
  <td>
     <table width="100%" border="0" cellspacing="0" cellpadding="2">
        <tr>
          	<td  align="right"><label><input type="checkbox" name="checkbox" property="menuactionchkbox" value="checkbox" onClick="enabledisableprogramname()"  /></label></td>
          	<td width=""><label class="style2">Item Type-Menu/Action</label></td>
        </tr>
        <tr>  
          	<td><label class="style2">Item Name</label></td>
          	<td ><html:text property="itemname" value="<%=item.getItemDesc()%>" styleClass="varysizeareahighlightgrey" readonly="true" /></td>
          	<td ><label class="style2">Program Name</label></td>
          	<td ><html:text property="programname" styleClass="areahighlightyellow1"  /></td>
          	</tr>
      <tr>  
          	
          	<td><label class="style2">Predecessor</label></td>
          	<td><html:select  property="predecessor"  styleClass="dropdownboxStyle">
                 <html:optionsCollection name="predecessorList" styleClass="areahighlightyellow"/>
                </html:select> </td>
          	<td><label class="style2">Unique Code</label></td>
          	<td ><input name="itemCode" styleClass="areahighlightyellow1"  id="itemCode" onkeydown="checkUniqueCode()"/>
          	 <dojo:autoComplete formId="addMenuactionForm" textboxId="itemCode" action="<%=path%>/actions/itemCode.jsp?tabName=ITEM_MASTER&from=0"/> </td>
<td class="style2"> <div id="a" style="visibility:hidden;border:0;">Please it should be Unique Code
</div></td>
        </tr>
 </table>
 </td>
</tr>
</table>
<script>defaultValues()</script>
    <html:hidden property="buttonValue"/> 
	</html:form>
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

