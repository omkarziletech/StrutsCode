<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.AuditLogRecord,com.gp.cong.logisoft.domain.AuditLogRecordUser,com.gp.cong.logisoft.domain.User,java.text.SimpleDateFormat,com.gp.cong.logisoft.hibernate.dao.AuditLogRecordDAO,com.gp.cong.logisoft.beans.NoteBean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String noteId=null; 

String buttonValue="";
NoteBean nBean=new NoteBean();
String customernotes="";
String exportnotes="";
String inlandnotes="";

if(request.getAttribute("buttonValue")!=null)
{
buttonValue=(String)request.getAttribute("buttonValue");
}
if(request.getAttribute("customernotes")!=null)
{
customernotes=(String)request.getAttribute("customernotes");
 session.setAttribute("customernotes", customernotes);
}
if(session.getAttribute("customernotes")!=null)
{
customernotes=(String)session.getAttribute("customernotes");
}
if(request.getAttribute("exportnotes")!=null)
{
exportnotes=(String)request.getAttribute("exportnotes");
 session.setAttribute("exportnotes", exportnotes);
}
if(session.getAttribute("exportnotes")!=null)
{
exportnotes=(String)session.getAttribute("exportnotes");
}

if(request.getAttribute("inlandnotes")!=null)
{
inlandnotes=(String)request.getAttribute("inlandnotes");
 session.setAttribute("inlandnotes", inlandnotes);
}
if(session.getAttribute("inlandnotes")!=null)
{
inlandnotes=(String)session.getAttribute("inlandnotes");
}
String voided="";
String pageName="";
List auditList=null;
if(request.getParameter("notes")!=null && request.getParameter("notes").equals("accountsrec"))
{
if(session.getAttribute("noteBean")!=null)
{
nBean=(NoteBean)session.getAttribute("noteBean");

if(nBean.getAuditList()!=null)
{
auditList=nBean.getAuditList();

session.setAttribute("auditList",auditList);
}
if(nBean.getVoidednote()!=null)
{
voided=nBean.getVoidednote();
session.setAttribute("voided",voided);
}
if(nBean.getPageName()!=null)
{
pageName=nBean.getPageName();
session.setAttribute("pagename",pageName);
}
}
}
if(request.getAttribute("noteBean")!=null)
{ 
nBean=(NoteBean)request.getAttribute("noteBean");
if(nBean.getAuditList()!=null)
{
auditList=nBean.getAuditList();

session.setAttribute("auditList",auditList);
}
if(nBean.getVoidednote()!=null)
{
voided=nBean.getVoidednote();
session.setAttribute("voided",voided);
}
if(nBean.getPageName()!=null)
{
pageName=nBean.getPageName();
session.setAttribute("pagename",pageName);
}
}

if(session.getAttribute("voided")!=null)
{
voided=(String)session.getAttribute("voided");
}
if(session.getAttribute("pagename")!=null)
{
pageName=(String)session.getAttribute("pagename");
}
if(session.getAttribute("auditList")!=null)
{
auditList=(List)session.getAttribute("auditList");
}
noteId=nBean.getNoteId();
User user1=null;
user1=nBean.getUser();
//String loginName=user1.getLoginName();


String message="";
Date date1=new Date(System.currentTimeMillis()); 
SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
String userDate=String.valueOf(sdf.format(date1));
String notePath=path+"/jsps/admin/NotePage.jsp";
String modify="";
if(session.getAttribute("notesmodify")!=null)
{
modify=(String)session.getAttribute("notesmodify");
}
request.setAttribute("notesTypelist", dbUtil.getNotesList(28,"no", "Select Note type"));
%>
 
 
<html> 
	<head>
		<%@include file="../includes/resources.jsp" %>
		<title>JSP for NotesForm form</title>
		<link rel="stylesheet" type="text/css" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<script type="text/javascript" src="<%=path%>/js/caljs/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
<script language="Javascript" type="text/javascript" src="<%=path%>/js/common.js"></script>
<script>
function save1(id)
{

if(document.notesForm.notes.value=="")
{
alert("Please enter the Note");
return;
}
if(document.notesForm.notes.value.length>200)
{
alert("Please enter the note below 200 characters");
document.notesForm.notes.value="";
document.notesForm.notes.focus();
return;
}

document.notesForm.noteId.value=id;
document.notesForm.buttonValue.value="add";
document.notesForm.submit();
}
function cancel(obj)
{
document.notesForm.buttonValue.value="cancel";
document.notesForm.submit();
}
function void1(obj,val)
{

document.notesForm.number.value=val;
document.notesForm.buttonValue.value="void";
document.notesForm.submit();
}
function voidnotes()
{
document.notesForm.voidnote.value="voided";
document.notesForm.buttonValue.value="voidnote";
document.notesForm.submit();
}
function displaytagcolor(obj)
		  {
		        var datatableobj = document.getElementById('notetable');
				if(datatableobj!=null)
				{
				for(i=0; i<datatableobj.rows.length; i++)
				{
					var tablerowobj = datatableobj.rows[i];
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
				}
				initRowHighlighting();
				setWarehouseStyle(obj);
				}
		 }
		 function setWarehouseStyle(obj)
		  {
		
		  	if(obj=="note")
		  	{
		  	 var x=document.getElementById('notetable').rows[0].cells;	
		  
		     x[4].className="sortable sorted order2";
		  	}
		  	}
		 function initRowHighlighting()
  		{
			if (!document.getElementById('notetable')){ return; }
			var tables = document.getElementById('notetable');
			attachRowMouseEvents(tables.rows);
		}

		function attachRowMouseEvents(rows)
		{
			for(var i =1; i < rows.length; i++)
			{
				var row = rows[i];
				row.onmouseover =	function() 
								{ 
									this.className = 'rowin';
								}
				row.onmouseout =	function() 
								{ 
									this.className = '';
								}
            	row.onclick= function() 
								{ 
							 	}
			}
		
		}	
		 var newwindow = '';
		function notes11(obj)
		{
		if (!newwindow.closed && newwindow.location)
	  {
	  GB_show('Inland Voyage','<%=path%>/jsps/admin/NotePage.jsp?notevalue='+obj,150,600);
         // newwindow.location.href = "<%=path%>/jsps/admin/NotePage.jsp?notevalue="+obj;
      }
      else 
      {
     	 GB_show('Inland Voyage','<%=path%>/jsps/admin/NotePage.jsp?notevalue='+obj,150,600);
          //newwindow=window.open("<%=path%>/jsps/admin/NotePage.jsp?notevalue="+obj,"","width=450,height=150");
         // if (!newwindow.opener) newwindow.opener = self;
      }
     // if (window.focus) {newwindow.focus()}
      return false;
		}
</script>
<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd" onload="displaytagcolor('<%=buttonValue%>')">

		<html:form action="/notes" scope="request">
			
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
	<tr class="tableHeadingNew" >Notes</tr>
    <td>		
		
  		<table width="100%"  border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td align="left" width="80%">&nbsp;</td>
    		<td  align="right"> <input type="button" value="Go Back" class="buttonStyleNew" onclick="cancel('<%=modify%>')"/> </td>
    		
    		<td align="right">  <input type="button" value="VoidedNotes" class="buttonStyleNew" onclick="voidnotes()"/> </td>
    			
    	</tr>
    	</table>
    	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-left:0px;border-right:0px;">
  		<tr class="tableHeadingNew">Notes Details </tr>
    	<tr height="8"></tr>
  		<tr>
    		<td height="15"  >&nbsp;&nbsp;</td>
  		</tr>
  	<tr height="8"></tr>
  		<tr>
    		<td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      
         <tr>
        <td><table width="100%" border="0" cellspacing="0">
          <tr>
          	 <td class="style2">Reference#</td>
             <td ><html:text property="reference" value="<%=nBean.getReferenceId()%>" readonly="true"/></td>
          	 <td  class="style2">Document Name</td>
          	 <td > <html:text property="documentName" value="<%=nBean.getItemName()%>" readonly="true"/></td>
        		 
            
     </table>
     <table width="100%" border="0" cellspacing="0">
            <tr> 
          		<td class="style2">Notes</td>
           		<td><html:textarea property="notes" value=""  rows="5" cols="72" styleClass="textareastyleNotes"/></td>
            <% 
             if((customernotes!=null && customernotes!="") || (exportnotes!=null && exportnotes!="") ||(inlandnotes!=null && inlandnotes!=""))
             {
             %>
             <td class="style2">Notes Type</td>
         <td ><html:select property="notesType" styleClass="selectboxstyle">
            		<html:optionsCollection name="notesTypelist" />
                	</html:select></td>
                	<%} %>
              <td> <input type="button" value="AddNew"  class="buttonStyleNew" onclick="save1('<%=noteId%>')" /></td>
            </tr>
           
         
        </table>
     
      
    </table></td>
  </tr>
  </table>
  <br/>
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-left:0px;border-right:0px;border-bottom:0px;">  	
      <%
      if(!voided.equals("voided"))
      {
       %>
      <tr class="tableHeadingNew">List of Notes</tr>
<%--    	<td height="15"  class="headerbluesmall">&nbsp;&nbsp;List of Notes </td>--%>
<%--  		</tr>--%>
  		<%
  		}
  		 %>
  		 <%
      if(voided.equals("voided"))
      {
       %>
      <tr class="tableHeadingNew">List of Void Notes</tr>
<%--    	<td height="15" class="headerbluesmall">&nbsp;&nbsp;List of Void Notes </td>--%>
<%--  		</tr>--%>
  		<%
  		}
  		 %>
    
  		<tr>
    	<td>
    	<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
        <table  border="0" cellpadding="0" cellspacing="0">
        <% 
         int i=0;
         int j=0;
         int k=0;
    	%>
		<display:table name="<%=auditList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="notetable" style="width:100%" sort="list"> 
			
			<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Notes Details Displayed,For more Notes click on Page Numbers.
    				<br>
    				</span>
  			  </display:setProperty>
  			  <display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
						One {0} displayed. Page Number
					</span>
  			  </display:setProperty>
  			   <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
						{0} {1} Displayed, Page Number
					</span>
  			  </display:setProperty>
    			<display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					  No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="Note"/>
  			<display:setProperty name="paging.banner.items_name" value="Notes"/>
  			<%
  			String note="";
  			String noteType="";
  			String date=null;
  			String whoDid="";
  			int number=0;
  			String note1="";
  			String voidValue=null;
  			String entityAttribute="";
  			String id="";
  			if(auditList!=null && auditList.size()>0)
  			{
  				AuditLogRecord audit=(AuditLogRecord)auditList.get(i);
  				id=audit.getId().toString();
  				noteType=audit.getNoteType();
  				entityAttribute=audit.getEntityAttribute();
  				if(noteType.equalsIgnoreCase("auto"))
  				{
  				if(entityAttribute!=null && !entityAttribute.equals("Inserted Successfully"))
  				{
  			 	note=audit.getEntityAttribute()+"  value was  "+audit.getOldValue();
  			 	audit.setNote(note);
  			 	}
  			 	else if(entityAttribute!=null)
  			 	{
  			 	note=audit.getEntityAttribute();
  			 	audit.setNote(note);
  			 	}
  			 	}
  			 	else
  			 	{
  			 	note=audit.getOldValue();
  			 	audit.setNote(note);
  			 	}
  			 	note1=note;
  			 if(note.length()>40)
  			 {
  			    note1=note.substring(0,40);
  			 }
  			 	date=audit.getDate();
  			 	whoDid=audit.getUpdatedBy();
  			 	number=audit.getId();
  			 	voidValue=audit.getVoided();
  			 	
  			}
  			 %>
  		<display:column  title="Note" ><a href="#" onclick="notes11('<%=note%>')" ><%=note1%></a></display:column>
		
			<display:column  title="Note Type" ><%=noteType%></display:column>
			
			<display:column  title="Date" ><%=date%></display:column>
			
			<display:column  title="Who Did" ><%=whoDid%></display:column>
		
	       <display:column  title="Number" ><%=number%></display:column>
			
			
			
			<%
			if(!noteType.equals("Auto"))
			{
			if(voidValue==null )
			{
			 %>
       		<display:column >
       		 <input type="button" value="Void" class="buttonStyleNew" name="<%=j%>" onclick="void1(this,'<%=id %>')"/></display:column>
       		<%
       		}
       		else
       		{
       		%>
       		 <display:column></display:column>
       		 <%
       		 }
       		}
       		else
       		{
       		 %>
       		 <display:column></display:column>
       		 <%
       		 }
       		  %>
       		<%j++; %>
       		<% i++;%>
		</display:table>
        </table>
        </div>
        </td>
  		</tr>
  		</table>
  		
  		
		<html:hidden property="number"/>
		<html:hidden property="buttonValue"  value="<%=nBean.getButtonValue()%>"/>
		<html:hidden property="noteId" value="<%=noteId%>"/>
		<html:hidden property="voidnote" value="<%=voided%>"/>
		
		<html:hidden property="itemName" value="<%=nBean.getItemName()%>"/>
		<html:hidden property="pageName" value="<%=pageName%>"/>
		
		
		</html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

