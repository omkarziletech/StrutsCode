<%@ page language="java"
	import="com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.beans.SearchUserBean,com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String buttonValue="";
List itemList=null;
String link="";
SearchUserBean sBean=new SearchUserBean();
User user=null;
if(session.getAttribute("loginuser")!=null)
{
 user=(User)session.getAttribute("loginuser");
}
user.setMatch("starts");
String modify=null;
if(request.getAttribute("buttonValue")!=null)
{
    buttonValue=(String)request.getAttribute("buttonValue");
}
if(request.getAttribute("suBean")!=null)
{
 sBean=(SearchUserBean)request.getAttribute("suBean");
 user.setMatch(sBean.getMatch());
}
if(session.getAttribute("itemList")!=null)
{
 itemList=(List)session.getAttribute("itemList");
 //loginName=userList.getLoginName();
} 


String msg="";
if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
	
}	
String message="";
if(request.getAttribute("msg")!=null)
{
message=(String)request.getAttribute("msg");
}

if(request.getParameter("modify")!= null)
{
 	session.setAttribute("modifyformenu",request.getParameter("modify"));
 	modify=(String)session.getAttribute("modifyformenu");
}
else
{
 modify=(String)session.getAttribute("modifyformenu");

}

if(request.getParameter("programid")!= null && session.getAttribute("processinfoformenu")==null)
{
	buttonValue="searchall";
 	
}
if(buttonValue.equals("searchall"))
{

  itemList=dbUtil.getAllItems();
  session.setAttribute("itemList",itemList);
  if(request.getParameter("programid")!=null)
{
session.setAttribute("processinfoformenu",request.getParameter("programid"));
}
  session.setAttribute("itemListCaption","itemList {All Records}");
}
request.setAttribute("user",user);
// Name:Yogesh Date:12/01/2007(mm/dd/yy) setting the path to userManagement action
String editPath=path+"/searchMenuandAction.do";



%>

<html>
	<head>
		<title>SearchMenu/Action</title>
<%@include file="../includes/baseResources.jsp" %>


		<script language="javascript" type="text/javascript">
		function searchform()
		{
		if(document.searchMenuandAction.txtItemcreatedon.value!="")
			{
				if(isValidDate(document.searchMenuandAction.txtItemcreatedon.value)==false)
				{

					document.searchMenuandAction.txtItemcreatedon.value="";
					document.searchMenuandAction.txtItemcreatedon.focus();
					return;
				}
			}
		
		
			if(document.searchMenuandAction.itemname.value=="")
			{
				
				if(document.searchMenuandAction.programname.value=="")
				{
					
					if(document.searchMenuandAction.txtItemcreatedon.value=="")
						{
							alert("Please enter Item name or program name or Item created date");
							return;
						}
			         
			        }
			     
			  }
						
			if(document.searchMenuandAction.programname.value.match(" "))
           {
            	alert("Space is not allowed for Program Name");
            	return;
           }
					
  			document.searchMenuandAction.buttonValue.value="search";
  			document.searchMenuandAction.submit();
  		
  		}
		function confirmdelete(obj)
		{	
    		var rowindex=obj.parentNode.parentNode.rowIndex;
			var x=document.getElementById('itemtable').rows[rowindex].cells; 
		    document.searchMenuandAction.index.value=obj.name;
    		document.searchMenuandAction.buttonValue.value="delete";
    	    var result = confirm("Are you sure you want to delete "+x[2].innerHTML);
			if(result)
			{
   				document.searchMenuandAction.submit();
   			} 
   			
		}
		function editForm(obj)
		{
  			document.searchMenuandAction.index.value=obj.name;
  			document.searchMenuandAction.buttonValue.value="edit";
  			document.searchMenuandAction.submit();
		}
		
		function viewForm(obj)
		{
  			document.searchMenuandAction.index.value=obj.name;
  			document.searchMenuandAction.buttonValue.value="view";
  			document.searchMenuandAction.submit();
		}
		
		var newwindow = '';
           function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/admin/itemCode.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/admin/itemCode.jsp","","width=400,height=100");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           } 

		function searchallform()
         {
         document.searchMenuandAction.buttonValue.value="searchall";
  			document.searchMenuandAction.submit();
  		 }
		
function disabled(val)
{
	if(val == 0)
	{		
     var imgs = document.getElementsByTagName('img');
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id != "itemcreatedon" && imgs[k].id != "showall" && imgs[k].id!="search")
   		 {
   		 	imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		
   		
  	 }
  	 			setSearchAllStyle(); 
  				//displaytagcolor();
   				//initRowHighlighting();
 }
 function setSearchAllStyle()
		  {
			
		  	if(document.searchMenuandAction.buttonValue.value=="searchall")
		  	{
		  
		  	 var x=document.getElementById('itemtable').rows[0].cells;	
		
		     x[0].className="sortable sorted order1";
		  	}
		  	if(document.searchMenuandAction.buttonValue.value=="search")
		  	{
		  	var input = document.getElementsByTagName("input");
		  	var select = document.getElementsByTagName("select");
	  		 			if(!input[0].value=="")
	  					{
	  					
	  		 				 var x=document.getElementById('itemtable').rows[0].cells;	
	  		   				 x[0].className="sortable sorted order1";s
	  		   			}
	  		   			else if(!input[1].value=="")
	  					{
	  					
	  		 				 var x=document.getElementById('itemtable').rows[0].cells;	
	  		   				 x[5].className="sortable sorted order1";
	  		   			}
	  		   			
	  		   		
	  		   		else if(!input[4].value=="0")
	  					{
	  		 				 var x=document.getElementById('itemtable').rows[0].cells;	
	  		   				 x[1].className="sortable sorted order1";
	  		   			}
	  		   			else if(!input[5].value=="0")
	  					{
	  		 				 var x=document.getElementById('itemtable').rows[0].cells;	
	  		   				 x[2].className="sortable sorted order1";
	  		   			}
	  				}
		 }  
		  function displaytagcolor()
		  {
		  
				var datatableobj = document.getElementById('itemtable');
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
		 }
		
		function initRowHighlighting()
  		{
			if (!document.getElementById('itemtable')){ return; }
			var tables = document.getElementById('itemtable');
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
		
		 function toUppercase(obj) 
	    {
			obj.value = obj.value.toUpperCase();
		}
		function getItemPage(){
		window.location.href="<%=path%>/jsps/admin/addMenuaction.jsp";
		}
</script>
<%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
<html:form action="/searchMenuandAction" name="searchMenuandAction"
			type="com.gp.cong.logisoft.struts.form.SearchMenuandActionForm" scope="request">
<font color="blue"><h4>
					<%=msg%>
				</h4>
			</font>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Search Criteria</tr>				
<tr>
   <td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
							<td width="100%">
									<table width="100%" border="0" cellspacing="0">
										<tr>
											<td class="style2">&nbsp;
												Item Name
											 </td>

											<td class="style2">
												Program Name
											</td>

											<td class="style2">
												Item Created On
											</td>
											<td></td>
											<td></td>
											<td>
												<table>
													<tr>
														<td class="style2">
															Match Only
														</td>
														<td>
															<html:radio property="match" value="match" name="user"></html:radio>
														</td>
													</tr>
												</table>
											</td>
											<td>
												<table>
													<tr>
														<td class="style2">
															Start list at
														</td>
														<td>
															<html:radio property="match" value="starts" name="user"></html:radio>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>

											<td>&nbsp;
												<html:text property="itemname"
													value="<%=sBean.getItemname()%>"
													onkeyup="toUppercase(this)"></html:text>
											</td>

											<td>
												<html:text property="programname"
													value="<%=sBean.getProgramname()%>"
													onkeyup="toUppercase(this)" />
											</td>

											<td>
												<html:text property="txtItemcreatedon"
													styleId="txtitemcreatedon"
													value="<%=sBean.getTxtItemcreatedon()%>" />
											
												<img src="<%=path%>/img/CalendarIco.gif" alt="cal"
													align="top" id="itemcreatedon"
													onmousedown="insertDateFromCalendar(this.id,0);" />
											</td>
									 </tr>
							</table>
						</td>
					</tr>
	   </table>
	 </td>
</tr>

<tr>
    <td colspan="6" align="center">
        <table width="60%">
        <tr>
<%--            <td>--%>
<%--                 <img align="right" src="<%=path%>/img/showall.gif" border="0"--%>
<%--													onclick="searchallform()" id="showall" />--%>
<%--		        <img align="right" src="<%=path%>/img/addnew.gif" onclick="addform()"--%>
<%--													border="0" />--%>
<%--		        <img  align="right" src="<%=path%>/img/search.gif" id="search" border="0" onclick="searchform()" style="cursor: pointer; cursor: hand;" />--%>
<%--               --%>
<%--           </td>--%>

               <td align="center">
               
              <input type="button" class="buttonStyleNew" onclick="searchform()" name="Search" value="Search"/>
	 	      <input type="button" class="buttonStyleNew" onclick="searchallform()" name="Search" value="ShowAll"/>
	          <input type="button" class="buttonStyleNew" value="Add New"
	          onclick="return GB_show('Menu', '<%=path%>/jsps/admin/itemCode.jsp?relay='+'add',150,600)"
	         /> 
               
               </td>
        </tr>
        </table>
    </td>
</tr>
</table>

<table>
    <tr><td>&nbsp;</td></tr>
</table>

<table width="100%" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Search Result</tr>
<%--					<td class="headerbluesmall">--%>
<%--						&nbsp;&nbsp;--%>
<%--						<%=session.getAttribute("itemListCaption")%>--%>
<%--					</td>--%>
				

				<%
				if (!buttonValue.equals("load") || itemList != null) {
				%>
				<tr>
					<td>
						<div id="divtablesty1"
							class="scrolldisplaytable">
							<table border="0" cellpadding="0" cellspacing="0">
								<display:table name="<%=itemList%>" pagesize="<%=pageSize%>"
									class="displaytagstyle" id="itemtable" sort="list">

									<display:setProperty name="paging.banner.some_items_found">
										<span class="pagebanner"> <font color="blue">{0}</font>
											Item Details Displayed,For more Items click on Page Numbers.
											<br> </span>
									</display:setProperty>
									<display:setProperty name="paging.banner.one_item_found">
										<span class="pagebanner"> One {0} displayed. Page
											Number </span>
									</display:setProperty>
									<display:setProperty name="paging.banner.all_items_found">
										<span class="pagebanner"> {0} {1} Displayed, Page
											Number </span>
									</display:setProperty>
									<display:setProperty name="basic.msg.empty_list">
										<span class="pagebanner"> No Records Found. </span>
									</display:setProperty>
									<display:setProperty name="paging.banner.placement"
										value="bottom" />
									<display:setProperty name="paging.banner.item_name"
										value="Item" />
									<display:setProperty name="paging.banner.items_name"
										value="Items" />
									<display:column property="itemDesc" href="<%=editPath%>"
										paramId="paramid" paramProperty="itemId"
										title="NAME" class="align"
										sortable="true" sortProperty="itemDesc" />

									<display:column property="itemType"
										title="ITEM TYPE"
										sortable="true" sortProperty="itemType" />

									<display:column property="programName"
										title="&nbsp;PROGRAM NAME"
										class="align" sortable="true" sortProperty="programName" />

									<display:column property="itemcreateddate"
										title="ITEM CREATED ON"
										sortable="true" sortProperty="itemcreateddate" />

									<display:column property="predecessor"
										title="PREDECESSOR"
										sortable="true" sortProperty="predecessor" />

									<display:column href="<%=editPath%>" paramId="param"
										paramProperty="itemId" title="Actions">
									<span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);" onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" border="0" onclick="window.location.href('<%=link%>')" /></span>
									</display:column>

								</display:table>
							</table>
						</div>
					</td>
				</tr>
				<%
				}
				%>

</table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
			<html:hidden property="index" styleId="index" />
			<html:hidden property="msg" />
		</html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

