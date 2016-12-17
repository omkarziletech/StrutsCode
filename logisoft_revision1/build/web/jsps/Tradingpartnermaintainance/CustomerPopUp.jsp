<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerTemp,java.util.*,com.gp.cong.logisoft.domain.CustomerAccounting,com.gp.cong.logisoft.beans.customerBean,com.gp.cong.logisoft.domain.CustomerAssociation,com.gp.cong.logisoft.hibernate.dao.CustomerDAO"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String buttonValue="";
String cancel="";
String checkbox="";
String accoutnNo="";
List assocList=new ArrayList();
CustomerTemp custtemp=new CustomerTemp();
CustomerDAO customerDAO=new CustomerDAO();
List customerList=new ArrayList();
List associateList=new ArrayList();
List relatedList=new ArrayList();
String acctno="";
String aname="";

/*if(request.getAttribute("namelist")!=null)
{
//relatedList=(List)session.getAttribute("namelist");
  custtemp=(CustomerTemp)request.getAttribute("namelist");
  if(custtemp!=null && custtemp.getAccountNo()!=null)
  {
   acctno=custtemp.getAccountNo();
   aname=custtemp.getAccountName();
  }

}*/

if(session.getAttribute("assocList")!=null)
{
customerList=(List)session.getAttribute("assocList");
}

if(request.getParameter("button")!=null)
{
buttonValue=(String)request.getParameter("button");
session.setAttribute("option",buttonValue);
}
if(request.getAttribute("cancel")!=null)
{
cancel=(String)request.getAttribute("cancel");
}
if(cancel.equals("cancel"))
	{
	%>
		<script>
		parent.parent.GB_CURRENT.hide();
<%--			self.close();--%>
<%--			opener.location.href="<%=path%>/jsps/Tradingpartnermaintainance/MasterCustomer.jsp";--%>
		</script>
	<%
	}
else if(cancel.equals("editcancel"))
	{
	%>
		<script>
		parent.parent.GB_CURRENT.hide();
<%--			self.close();--%>
<%--			opener.location.href="<%=path%>/jsps/Tradingpartnermaintainance/masterEditCustomer.jsp";--%>
		</script>
	<%
	}
	String modify="";
modify = (String) session.getAttribute("modifyformastercustomer");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
    
	}
String editPath=path+"/customerPopUp.do";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for CustomerForm form</title>
    
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
	<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
	<script type="text/javascript">
	
   	function cancel1()
   		{
   			document.customerPopUpForm.buttonValue.value="cancel";
  			document.customerPopUpForm.submit();
   		}
   		function associate(obj)
   		{
   		
   		document.customerPopUpForm.index.value=obj;
   		document.customerPopUpForm.buttonValue.value="associate";
  		document.customerPopUpForm.submit();
   		}
   		function save()
   		{
   		
   		var a1="";
   		for(var i=0;i<document.customerPopUpForm.checkbox.length;i++)
   		{
   		if(document.customerPopUpForm.checkbox[i].checked)
		{
		a1=a1+i+",";
		}
		}
		
		document.customerPopUpForm.index.value=a1;
   		document.customerPopUpForm.buttonValue.value="save";
  		document.customerPopUpForm.submit();
   		}
   		
   		
		function confirmdelete(obj)
		{
		
		var rowindex=obj.parentNode.parentNode.rowIndex;
	
	 		var x=document.getElementById('associatetable').rows[rowindex].cells;	
	 		
			document.customerPopUpForm.index.value=obj.name;
		
    		document.customerPopUpForm.buttonValue.value="delete";
    			
    		
   		    var result = confirm("Are you sure you want to delete this port "+x[1].innerHTML);
			if(result)
			{
   				document.customerPopUpForm.submit();
   			}	
		
		}
		function search1()
		{
		document.customerPopUpForm.buttonValue.value="search";
  		document.customerPopUpForm.submit();
		}
 function disabled1(val1)
   {
     
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id!="cancel")
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue" && input[i].id != "cancel")
	 		{
	  			input[i].disabled = true;
	  		}
  	 	}
   		document.getElementById("cancel").style.visibility = 'hidden';
   		document.getElementById("delete").style.visibility = 'hidden';
  	 }	
  	 
    }
    function titleLetter()
		   {
		       if(event.keyCode==9)
			    {
			       document.customerPopUpForm.accountName.value = "";
			      document.customerPopUpForm.buttonValue.value ="custpopup" ;
		          document.customerPopUpForm.submit();
			    }
		   }
		  

			function titleLetterPress()
			{
			   if(event.keyCode==13)
				  {
					  document.customerPopUpForm.accountName.value = "";
					  document.customerPopUpForm.buttonValue.value ="custpopup" ;
					  document.customerPopUpForm.submit();
				  }
			  
			}
			function titleLetterName()
		   {
		       if(event.keyCode==9)
			    {
			       document.customerPopUpForm.accountNo.value = "";
			      document.customerPopUpForm.buttonValue.value ="custpopup" ;
		          document.customerPopUpForm.submit();
			    }
		   }
		  

			function titleLetterPressName()
			{
			   if(event.keyCode==13)
				  {
					  document.customerPopUpForm.accountNo.value = "";
					  document.customerPopUpForm.buttonValue.value ="custpopup" ;
					  document.customerPopUpForm.submit();
				  }
			  
			}
			
	</script>
	</head>
	<%
	
	if(modify!=null && modify.equals("3"))
	{
	
	 %>
	
		<body class="whitebackgrnd" onLoad="disabled1('<%=modify%>')">
	<%}
	
	else{ %>
		<body class="whitebackgrnd">
	<%} %>
<%--	<body class="whitebackgrnd" onLoad="disabled1('<%=modify%>')">--%>
		
		
<html:form action="/customerPopUp" scope="request">
<table width="100%" border="0" >
<tr>
	<td>
		
 <input type="button" class="buttonStyleNew" value="Go Back"  id="cancel" onclick="cancel1()">


	</tr>
</table>

<table width="100%" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Search Criteria</tr>
<tr>
    <td>
		<table width="100%">
		   <tr>
	            <td class="textlabels">AccountNo</td>
		        <td>
	      		<%--<html:text property="accountNo"  value="" size="10"/>--%>  
	      			 <input name="accountNo" id="accountNo" value="" onkeydown="titleLetter()" onkeypress="titleLetterPress()" />
	       			 <dojo:autoComplete formId="customerPopUpForm" textboxId="accountNo" action="<%=path%>/actions/getCustomer.jsp"/>            
	      
		  		</td>
		  		<td class="textlabels">Account Name</td>
		  		<td>
		    		 <%--<html:text property="accountName" value=""  size="10" />--%> 
	     			<input name="accountName" id="accountName" value="" onkeydown="titleLetterName()" onkeypress="titleLetterPressName()" />
	       			<dojo:autoComplete formId="customerPopUpForm" textboxId="accountName" action="<%=path%>/actions/getCustomerName.jsp"/>
			   </td>
					<%--	  <td><img src="<%=path%>/img/search1.gif" onclick="search1()" id="search"/></td>--%>
	       </tr>
	  </table>
	</td>
 </tr>		
			<%--<table width="100%">

         <tr>
    <td class="headerbluesmall">&nbsp;&nbsp;List of Related Accounts</td>
   
  </tr>  
<tr>
  <td>
           <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
          <table  border="0" cellpadding="0" cellspacing="0">
       <% 
       int i=0; %>
		<display:table name="<%=relatedList%>" pagesize="5" id="customertable" class="displaytagstyle" style="width:100%"> 
		
		<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Customer details displayed,For more Customers click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Customer"/>
  			<display:setProperty name="paging.banner.items_name" value="Customers"/>
		
		
		<%
		String accountName="";
		String accountNo="";
		if(relatedList!=null && relatedList.size()>0)
		{
		CustomerTemp customer1=(CustomerTemp)relatedList.get(i);
		accountName=customer1.getAccountName();
		
		accountNo=customer1.getAccountNo();
		}
		String iStr=String.valueOf(i);
  			String tempPath=editPath+"?ind="+iStr;
		
		 %>
      <display:column title="NAME"><a href="<%=tempPath%>"><%=accountName%></a></display:column>
     
		<display:column title="ACCOUNT#"><%=accountNo%></display:column>
	
	
		
		
	 <%
	 i++; %>
       </display:table>
              </table></div></td>
            </tr>
      
    
    </table>--%>
<tr>
 <td>
  <table width="100%" border="0" >
   <tr class="tableHeadingNew" colspan="2">List of Associated Accounts</tr>
   <tr>
      <td>
           <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:150px;">
          <table  border="0" cellpadding="0" cellspacing="0">
       <% 
       int j=0; %>
		<display:table name="<%=customerList%>" pagesize="<%=pageSize%>" id="associatetable" class="displaytagstyle" style="width:100%"> 
		
		<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Customer details displayed,For more Customers click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Customer"/>
  			<display:setProperty name="paging.banner.items_name" value="Customers"/>
		
		
		<%
		String accountName="";
		String accountNo="";
		if(customerList!=null && customerList.size()>0)
		{
		CustomerTemp customer1=(CustomerTemp)customerList.get(j);
		accountName=customer1.getAccountName();
		
		accountNo=customer1.getAccountNo();
		}
		
		 %>
      <display:column title="ACCOUNT#"><%=accountNo%></display:column>
      <display:column title="NAME"><%=accountName%></display:column>
     
	 <display:column>
	  <input type="button" class="buttonStyleNew" name="<%=j%>" value="Delete"  id="delete" onclick="confirmdelete(this)">
	</display:column>
		
	 <%
	 j++; %>
	 
   </display:table>
   </table></div>
   </td>
 </tr>
</table>
    <html:hidden property="buttonValue" styleId="buttonValue"/>
    <html:hidden property="index"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

