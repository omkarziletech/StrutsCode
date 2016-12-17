<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.domain.GenericCode,java.util.*,com.gp.cong.logisoft.beans.customerBean,com.gp.cong.logisoft.domain.CustomerAssociation,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
DBUtil dbUtil=new DBUtil();
Customer customer =new Customer();
String countryId="";
String cityId="";
String state="";
String phone="";
String fax="";
String edit="";
if(request.getAttribute("edit")!=null)
{
edit=(String)request.getAttribute("edit");
}
String ScheduleNumber="";
String extension="";
List masteraddressList=new ArrayList();
if(session.getAttribute("masteraddressList")!=null)
{
masteraddressList=(List)session.getAttribute("masteraddressList");
}
List customerList1=null;
if(session.getAttribute("assocList")!=null)
{
customerList1=(List)session.getAttribute("assocList");
}
customerBean customerBean=new customerBean();
List customerList=new ArrayList();
if(session.getAttribute("cust")!=null)
{
customerList=(List)session.getAttribute("cust");
}
List cities=new ArrayList();
String type="0";
if(session.getAttribute("customer")!=null)
{
	customer=(Customer)session.getAttribute("customer");
	if(customer!=null && customer.getCuntry()!=null && customer.getCuntry().getCodedesc()!=null)
    {
   	 	countryId=customer.getCuntry().getCodedesc();
    }
 	if(customer!=null && customer.getCity2()!=null )
    {
   	 	cityId=customer.getCity2();
    }
   if(customer!=null && customer.getState()!=null )
   {
   	   state=customer.getState();
   }	
   if(customer.getPhone()!=null)
   {
   	   phone=dbUtil.appendstring(customer.getPhone());
   }
   if(customer.getFax()!=null)
   {
  	   fax=dbUtil.appendstring(customer.getFax());
   }
   
   
   if(customer.getExtension()!=null)
   {
      extension=dbUtil.appendstring(customer.getExtension());
   }
   //if(customer.getSchNum()!=null && customer.getSchNum().getShedulenumber()!=null)
   //{
  // ScheduleNumber=customer.getSchNum().getShedulenumber();
   //}
   
}

if(edit!=null && edit.equals("edit"))
{
%>
<script type="text/javascript">
mywindow=window.open("<%=path%>/jsps/Tradingpartnermaintainance/EditAddress.jsp?editAddress="+"masteradd","","width=650,height=355");
      mywindow.moveTo(200,180);
      </script>
<%
}
String editPath=path+"/masterCustomer.do";
%> 

<html>
  <head>
   
    <title>JSP for CustomerForm form</title>
	
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
    <script type="text/javascript">
    function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
 function popup1(mylink, windowname)
{

if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
mywindow=window.open(href, windowname, 'width=600,height=300,scrollbars=yes');
mywindow.moveTo(200,180);
document.masterCustomerForm.submit();
return false;
}

function limitText(limitField, limitCount, limitNum) {
	limitField.value = limitField.value.toUpperCase();
if (limitField.value.length > limitNum) {
limitField.value = limitField.value.substring(0, limitNum);
} else {


limitCount.value = limitNum - limitField.value.length;
 }
}	
function addports(obj)
{
document.getElementById('custport').style.visibility = 'visible';
document.masterCustomerForm.submit();
}	
function custports(val)
{

	/*if(val=='null')
	{
	document.getElementById('custport').style.visibility = 'hidden';
	}
	else if(val=='on')
	{
	document.getElementById('custport').style.visibility = 'visible';
	}*/

var datatableobj = document.getElementById('customertable');

				if(datatableobj!=null)
				{
				  for(i=0; i<datatableobj.rows.length; i++)
				  {
						var tablerowobj = datatableobj.rows[i];
		                if(tablerowobj.cells[6].innerHTML=='on')
						{
		                   tablerowobj.cells[0].style.visibility="visible";
						   tablerowobj.cells[0].innerHTML="*";
		                }
		                else
		                {
		                   tablerowobj.cells[0].style.visibility="hidden";
		                }
	              }
	            }
}

function phonevalid(obj)
{ 
if(document.masterCustomerForm.country.value=="" && document.masterCustomerForm.country.value!="United States")
{
    
  		if((document.masterCustomerForm.phone.value.length>10) || IsNumeric(document.masterCustomerForm.phone.value.replace(/ /g,''))==false)
 		{  
  		 alert("please enter the only 10 digits and numerics only");
		  document.masterCustomerForm.phone.value="";
  			document.masterCustomerForm.phone.focus(); 
 		 }
}
 else{
 getIt(obj);
  
}
}

function faxvalid(obj)
{ 
if(document.masterCustomerForm.country.value=="" && document.masterCustomerForm.country.value!="United States")
{
  if((document.masterCustomerForm.fax.value.length>10) || IsNumeric(document.masterCustomerForm.fax.value.replace(/ /g,''))==false)
 {  
   alert("please enter the only 10 digits and numerics only");
  document.masterCustomerForm.fax.value="";
  document.masterCustomerForm.fax.focus(); 
  }
	}
 
 else{
 getIt(obj);
  
}
}

function add1()
	{
	      if(document.masterCustomerForm.coName.value== "" )
        {
			alert("please enter the Coname");
			document.masterCustomerForm.coName.value="";
			document.masterCustomerForm.coName.focus();
       }
       else{
		
		document.masterCustomerForm.buttonValue.value="add";
	    document.masterCustomerForm.submit();
   	  }
   }

function zipcode(obj)
{
 if(document.masterCustomerForm.country.value=="" && document.masterCustomerForm.country.value!="United States")
 {
  
  		 if((document.masterCustomerForm.zip.value.length>5) || IsNumeric(document.masterCustomerForm.zip.value.replace(/ /g,''))==false)
 		{  
   alert("please enter the only 5 digits and numerics only");
  document.masterCustomerForm.zip.value="";
  document.masterCustomerForm.zip.focus(); 
  	    }
}
 else{
 getzip(obj);
  
    }
}

function popup()
{
  
 window.open("<%=path%>/jsps/Tradingpartnermaintainance/addCustomer.jsp?addCustomer="+"addMaster","","width=650,height=355");
 
}
  
    </script>
       <%@include file="../includes/resources.jsp" %>
	</head>
<body class="whitebackgrnd" onload="custports('<%=customerBean.getAccountType4()%>')">
<html:form action="/masterCustomer" name="masterCustomerForm" type="com.gp.cong.logisoft.struts.form.MasterCustomerForm" scope="request">

<table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew" >
<tr class="tableHeadingNew" colspan="2">List of Customer Addressess</tr>
<tr>
	<td align="right">
	  <input type="button" class="buttonStyleNew" value="Add" id="add" 
	  onclick="return GB_show('Customer', '<%=path%>/jsps/Tradingpartnermaintainance/addCustomer.jsp?addCustomer='+'addMaster',350,600)"  />
	</td>
</tr>
<tr>
   <td>
          <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
          <table  border="0" cellpadding="0" cellspacing="0">
           <% int i=0; %>
		<display:table name="<%=masteraddressList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="customertable" sort="list" > 
		
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
	
		String country="";
	    String coName="";
	    String address="";
	    
		if(masteraddressList!=null && masteraddressList.size()>0)
		{
			Customer customer1=(Customer)masteraddressList.get(i);
			if(customer1.getCuntry()!=null && customer1.getCuntry().getCodedesc()!=null)
			{
			country=customer1.getCuntry().getCodedesc();
			}
			if(customer1.getCoName()!=null)
			{
			coName=customer1.getCoName();
			}
	        if(customer1.getAddress1()!=null)
	        {
	        address=customer1.getAddress1();
	        }
		}
		String iStr=String.valueOf(i);
  			String tempPath=editPath+"?ind="+iStr;
		 %>
		<display:column title="" style="width:2%;visibility:hidden" ></display:column>
        <display:column title="C/O Name" ><%=coName%></display:column>
		<display:column title="Address1"><a href="<%=tempPath%>"><%=address%></a></display:column>
		<display:column title="City" property="city2"></display:column>
		<display:column title="State" property="state"></display:column>
		<display:column title="Country"><%=country%></display:column>
		<display:column title="" property="primary" style="width:2%;visibility:hidden"></display:column>
		
	<%i++; %> 
       </display:table>
    </table></div>
   </td></tr>
</table>

<table>
  <tr>
   <td class="style2">&nbsp;
      <input type="button" class="buttonStyleNew" value="Members" 
       onclick="return GB_show('Customer', '<%=path%>/jsps/Tradingpartnermaintainance/CustomerPopUp.jsp?button='+'add',300,600)"/>
  
<%--    <img border="0" src="<%=path%>/img/members.gif" onclick="return popup1('<%=path%>/jsps/Tradingpartnermaintainance/CustomerPopUp.jsp?button='+'add','windows')"> --%>
    </td>
  </tr>
</table>
  
<table width="100%" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">List of Related Accounts</tr>
<tr>
  <td>
          <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
          <table  border="0" cellpadding="0" cellspacing="0">
       <% int j=0; %>
		<display:table name="<%=customerList1%>" pagesize="<%=pageSize%>" id="customertable" class="displaytagstyle" > 
		
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
		String checkBox="";
	
		if(customerList1!=null && customerList1.size()>0)
		{
		CustomerTemp customer1=(CustomerTemp)customerList1.get(j);
		accountName=customer1.getAccountName();
		accountNo=customer1.getAccountNo();
		
		}
		 %>
      <display:column title="NAME"><%=accountName%></display:column>
		<display:column title="ACCOUNT#" ><%=accountNo%></display:column>
		
		
	 <%j++; %>
       </display:table>
    
 </table></div>
 </td></tr>
 </table>      
 
<html:hidden property="buttonValue"/>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
		

