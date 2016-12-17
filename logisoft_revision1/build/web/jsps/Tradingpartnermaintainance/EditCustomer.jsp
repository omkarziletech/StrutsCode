<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.domain.GenericCode,java.util.*,com.gp.cong.logisoft.beans.customerBean,com.gp.cong.logisoft.domain.CustomerAssociation"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
Customer customer =new Customer();
String countryId="";
String cityId="";
String state="";
String message="";
String modify="";
String phone="";
String fax="";

String extension="";
String ScheduleNumber="";
String type="0";
String edit="";
if(request.getAttribute("edit")!=null)
{
edit=(String)request.getAttribute("edit");
}
customerBean customerbean=new customerBean();
if(session.getAttribute("customerbean")!=null)
{
customerbean=(customerBean)session.getAttribute("customerbean");
}
List customerList=new ArrayList();

if(session.getAttribute("addressList")!=null)
{
customerList=(List)session.getAttribute("addressList");
for(int i=0;i<customerList.size();i++)
{
Customer cust=(Customer)customerList.get(i);
cust.setIndex(i);
}
}
if(session.getAttribute("message")!=null)
{
 message=(String)session.getAttribute("message");
} 

if(session.getAttribute("customer")!=null)
{
customer=(Customer)session.getAttribute("customer");

if(customer!=null && customer.getCuntry()!=null && customer.getCuntry().getCodedesc()!=null)
   {
   	 countryId=customer.getCuntry().getCodedesc();
   }
 if(customer!=null && customer.getCity1()!=null && customer.getCity1().getUnLocationName()!=null)
   {
   	 cityId=customer.getCity1().getUnLocationName();
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
  	   extension=customer.getExtension();
   }
   
   if(customer.getSchNum()!=null && customer.getSchNum()!=null)
   {
   ScheduleNumber=customer.getSchNum();
   }
  
   if(customer.getCity2()!=null && customer.getCity2()!="")
   {
   cityId=customer.getCity2();
   }
}



modify = (String) session.getAttribute("modifyforcustomer");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
	}
String editPath=path+"/editCustomer.do";

if(edit!=null && edit.equals("edit"))
{
%>
<script type="text/javascript">
mywindow=window.open("<%=path%>/jsps/Tradingpartnermaintainance/EditAddress.jsp?editAddress="+"edit","","width=650,height=400");
      mywindow.moveTo(200,180);
      </script>
<%
}
%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for CustomerForm form</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
    <script type="text/javascript">
    
    function selectcity()
    {
    if(document.editCustomerForm.country.value=="0")
    {
    	alert("Please enter the country");
   	    return;
    }
    document.editCustomerForm.buttonValue.value="selectcity";
    document.editCustomerForm.submit();
    }
    function selectstate()
    {
    if(document.editCustomerForm.city.value=="0")
    {
    	alert("Please enter the city");
    	return;
    }
     document.editCustomerForm.buttonValue.value="selectstate";
    document.editCustomerForm.submit();
    }
    
    function add1()
	{
	   if(document.editCustomerForm.coName.value== "" )
        {
			alert("please enter the Coname");
			document.editCustomerForm.coName.value="";
			document.editCustomerForm.coName.focus();
       }
      else{
		   document.editCustomerForm.buttonValue.value="add";
    	   document.editCustomerForm.submit();
    	}
   	}
    
     function confirmnote()
	{
		document.editCustomerForm.buttonValue.value="note";
    	document.editCustomerForm.submit();
   	}
    function disabled(val1,val2,val3)
   {
    
	if(val1 == 0 || val1== 3)
	{
	
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id!="note")
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue" && input[i].id != "add")
	 		{
	  			input[i].disabled = true;
	  		}
  	 	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled = true;
	  		
  	 	}
  	 	
  	 	var textarea = document.getElementsByTagName("textarea");
   		
   		for(i=0; i<textarea.length; i++)
	 	{
	 		textarea[i].disabled = true;
	  		
  	 	
  	 	}
  	 	 document.getElementById("add").style.visibility = 'hidden';
     }  
  	 
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
	            custports(val3);
  	 }	
    
     function popup1(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
mywindow=window.open(href, windowname, 'width=575,height=250,scrollbars=yes');
mywindow.moveTo(200,180);
document.editCustomerForm.submit();
return false;
}
 function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
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
document.editCustomerForm.submit();
}	



function custports(val)
{

if(val=='null')
{
document.getElementById('custport').style.visibility = 'hidden';
}
else if(val=='on')
{
document.getElementById('custport').style.visibility = 'visible';
}
}

function phonevalid(obj)
{ 
if(document.editCustomerForm.country.value=="" && document.editCustomerForm.country.value!="UNITED STATES")
{
    
  		if((document.editCustomerForm.phone.value.length>10) || IsNumeric(document.editCustomerForm.phone.value.replace(/ /g,''))==false)
 		{  
  		 alert("please enter the only 10 digits and numerics only");
		  document.editCustomerForm.phone.value="";
  			document.editCustomerForm.phone.focus(); 
 		 }
}
 else{
 getIt(obj);
  
}
}

function faxvalid(obj)
{ 
if(document.editCustomerForm.country.value=="" && document.editCustomerForm.country.value!="UNITED STATES")
{
  if((document.editCustomerForm.fax.value.length>10) || IsNumeric(document.editCustomerForm.fax.value.replace(/ /g,''))==false)
 {  
   alert("please enter the only 10 digits and numerics only");
  document.editCustomerForm.fax.value="";
  document.editCustomerForm.fax.focus(); 
  }
	}
 
 else{
 getIt(obj);
  
}
}
function zipcode(obj)
{
if(document.editCustomerForm.country.value=="" && document.editCustomerForm.country.value!="UNITED STATES")
{
  
  		if((document.editCustomerForm.zip.value.length>5) || IsNumeric(document.editCustomerForm.zip.value.replace(/ /g,''))==false)
 		{  
   alert("please enter the only 5 digits and numerics only");
  document.editCustomerForm.zip.value="";
  document.editCustomerForm.zip.focus(); 
  			}
			}
 else{
 getzip(obj);
  
}
}

function popup()
{
  window.open("<%=path%>/jsps/Tradingpartnermaintainance/addCustomer.jsp?addCustomer="+"add","","width=650,height=380");
}
    
</script>
<%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>','<%=customerbean.getAccountType4()%>')">
<html:form action="/editCustomer" name="editCustomerForm" type="com.gp.cong.logisoft.struts.form.EditCustomerForm" scope="request">
			 
<table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="7">List of Customer Addressess</tr>
 <tr>
  <td align="right">
    <input type="button" class="buttonStyleNew" value="Add" id="add" 
    onclick="return GB_show('TradingPartner', '<%=basePath%>/jsps/Tradingpartnermaintainance/addCustomer.jsp?addCustomer='+'add',300,600)"/>
  
 </td>
 </tr>

<tr>
   <td>
           <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
           <table  border="0" cellpadding="0" cellspacing="0">
             <% int i=0; %>
		  <display:table name="<%=customerList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="customertable" sort="list"> 
		
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
		if(customerList!=null && customerList.size()>0)
		{
			Customer customer1=(Customer)customerList.get(i);
			customer.setIndex(i);
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
		<display:column title="" style="width:2%;visibility:hidden"></display:column>
        <display:column title="C/O Name"><%=coName%></display:column>
		<display:column  title="Address"><a href="<%=tempPath%>"><%=address%></a></display:column>
		<display:column title="City" property="city2"></display:column>
		<display:column title="State" property="state"></display:column>
		<display:column title="Country"><%=country%></display:column>
		<display:column title="" property="primary" style="width:2%;visibility:hidden"></display:column>
		
	<%i++; %> 
       </display:table>
   </table></div>
</td></tr>
 <html:hidden property="buttonValue" styleId="buttonValue"/>
 </table>
 </html:form>
 </body>
 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
		

