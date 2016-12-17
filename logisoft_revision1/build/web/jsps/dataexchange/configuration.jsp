<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.beans.SearchUserBean"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.Usecases"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List userList=new ArrayList();
com.gp.cong.logisoft.domain.Usecases usecases=new com.gp.cong.logisoft.domain.Usecases();
request.setAttribute("usecaseslist",dbUtil.getUsecasesList());
String buttonValue="load";
SearchUserBean sBean=new SearchUserBean();
List flowFromList=new ArrayList();
if(request.getAttribute("buttonValue")!=null)
{
   buttonValue=(String)request.getAttribute("buttonValue");
}
String usecaseName="";
com.gp.cong.logisoft.domain.Usecases usecase=new com.gp.cong.logisoft.domain.Usecases();
if(buttonValue.equals("usecaseselected") && request.getAttribute("usecase")!=null)
{
    usecase=dbUtil.getName((String)request.getAttribute("usecase"));
    flowFromList=dbUtil.getFlowFromList((String)request.getAttribute("usecase"));
    usecaseName=usecase.getUsecaseName();
}

request.setAttribute("flowFromList",flowFromList);
String msg="";
String usecaseValue="0";


if(request.getAttribute("usecase")!=null)
{
   
   usecaseValue=(String)request.getAttribute("usecase");
}

if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
	
}
String modify="";
List dataExchangeList=dbUtil.getDataExchangeList();
if(request.getParameter("modify")!= null)
{
 session.setAttribute("modifyforrole",request.getParameter("modify"));
 modify=(String)session.getAttribute("modifyforrole");
}
else
{
 modify=(String)session.getAttribute("modifyforrole");
} 

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
   <script type="text/javascript">
    function submit1()
   {
   
    if(document.configurationForm.usecase.value=="0")
   {
     alert("Please select the Usecase");
     
     return;
   }  
       document.configurationForm.buttonValue.value="usecaseselected";
  
     document.configurationForm.submit();
   }
   
    function save()
   {
    if(document.configurationForm.usecase.value=="0")
    {
    alert("please enter the Usecase");
    return;
   } 
  
   document.configurationForm.buttonValue.value="save";
  document.configurationForm.submit();
  }
  
  function confirmdelete(obj)
{
  // alert("delete");
     var rowindex=obj.parentNode.parentNode.rowIndex;
     var x=document.getElementById('configurationtable').rows[rowindex].cells;	
	 document.configurationForm.index.value=x[8].innerHTML;
     document.configurationForm.buttonValue.value="delete";
    
     var result = confirm("Are you sure you want to delete this usecase "+x[0].innerHTML);
	 if(result)
	{
   		document.configurationForm.submit();
   	}	
}

function checkbox()
{
 box = eval("document.configurationForm.dataExchange"); 
 box.checked = true;
}
function disabled(val)
{

  	 if(val==0)
  	 {
  	 	var del = document.getElementsByTagName('img');
  	 	for(var k=0; k<del.length; k++)
   		{
   		
   		 if(del[k].id == "delete" ||del[k].id=="add")
   		 {
   		 	del[k].style.visibility = 'hidden';
   		 }
   		} 
   		var select = document.getElementsByTagName("select");
   		for(i=0; i<select.length; i++)
	 	{
	 		
	  			select[i].disabled = true;
	  		
  	 	}
   	 }
   	 else if(val==1)
   	 {
   	 var del = document.getElementsByTagName('img');
  	 	for(var k=0; k<del.length; k++)
   		{
   		
   		 if(del[k].id == "delete")
   		 {
   		 	del[k].style.visibility = 'hidden';
   		 }
   		} 
   		
   	 }
  }
   </script>
  </head>
  
  
<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')" >
	<html:form action="/configuration" scope="request">	
		<font color="blue"><h4><%=msg%></h4></font>


  <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="headerbluelarge" width="100%">Configuration</td>
  </tr>
 
  <tr>
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td class="headerbluesmall" width="100%">&nbsp;&nbsp;Configure </td>
            </tr>
            <tr height="8"></tr>
            <tr>
              <td><table width="100%" border="0" cellpadding="0" cellspacing="0" >
       <tr>
                     <td><label class="style2" id="text">UseCase</label></td>
                  
                   <td><html:select property="usecase" value="<%=usecaseValue%>" onchange="submit1()" styleClass="selectboxstyle" >
                  <html:optionsCollection name="usecaseslist"/>
                   </html:select>
                     </td>
                     <td></td>
                   <td><label class="style2">Name</label></td>
                    <td><html:text property="name" value="<%=usecaseName%>" readonly="true"/></td>
                    <td></td>
                    
                    <td><label class="style2" id="text">Flow From</label></td>
                  
                   <td><html:select property="testFlowFrom" styleClass="selectboxstyle" styleId="testFlowFrom">
                 <html:optionsCollection name="flowFromList"/>
                </html:select>
                     <td></td>
                     <td align="right"><label class="style2">Data Exchange</label></td>
                    <td><input type="checkbox"  checked="checked" disabled="true" /></td>
                  
                   <td><img src="<%=path%>/img/toolBar_add_hover.gif" border="0" id="add" onclick="save()"/></td>  
                  
       </tr>
              </table></td>
            </tr>
          </table>
            <p></p></td>
      </tr>
      <tr height="8"></tr>
      <tr>
         <td class="headerbluesmall" width="100%">&nbsp;&nbsp;List of Data Set Configuration </td>
         <td>&nbsp;</td>
       </tr>
      <tr>
        <td align="left" scope="row"><div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
           <display:table name="<%=dataExchangeList%>" pagesize="<%=pageSize %>" class="displaytagstyle" id="configurationtable" style="width:100%"> 
			
			<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Data Exchange Congiguration Displayed,For more Data Set click on Page Numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Data Sets"/>
  			<display:setProperty name="paging.banner.items_name" value="Data Sets"/>
  			
		<display:column property="usecaseCode" title="USECASE"/>
		<display:column></display:column>
		<display:column></display:column>
		<display:column property="usecaseName" title="NAME"/>
		<display:column></display:column>
		<display:column></display:column>
		<display:column property="flowFrom" title="FLOW FROM"/>
		<display:column></display:column>
		
	   <display:column style="visibility: hidden" property="id" title="" ></display:column>
		
		
	  
       <display:column><img  src="<%=path%>/img/toolBar_delete_hover.gif" id="delete" border="0"  onclick="confirmdelete(this)"/></display:column>
       
       
      
	
	
		</display:table>
          </table>
        </div>
     
      </td>
      </tr>
      
    </table></td>
  </tr>
</table>


<html:hidden property="buttonValue"/>
<html:hidden property="index"/>


  
	</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
