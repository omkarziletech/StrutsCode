<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerAccounting,com.gp.cong.logisoft.domain.PaymentPopUp,com.gp.cong.logisoft.domain.Vendor,com.gp.cong.logisoft.beans.customerBean" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
Vendor vendorDomainObj=new Vendor();
customerBean customerbean=new customerBean();
CustomerAccounting accounting=new CustomerAccounting ();
PaymentPopUp payObj=new PaymentPopUp();
List list = new ArrayList();//--------------for display column-----------
List PayList = new ArrayList();//----------for paymentinfo display--------

if(session.getAttribute("paymentList")!=null)
{
PayList=(List)session.getAttribute("paymentList");//----------------LIST DISPLAYING PAYMENT INFORMATION--------
}

 vendorDomainObj.setWfile("off");
 vendorDomainObj.setCredit("off");
 vendorDomainObj.setDeactivate("off");
 String E_phno="";
 String E_fax="";
 String E_crLimit="";
 String E_crTerms="0";
 String arcode="0";
 String paycountryId="";
 

if(session.getAttribute("VaddressList")!=null)
 {
     vendorDomainObj=(Vendor)session.getAttribute("VaddressList");
     if(vendorDomainObj.getAddress1() != null)
     {
        list.add(vendorDomainObj);//-------LIST DISPLAYING ADDRESS INFORMATION----
     }
     if(vendorDomainObj!=null && vendorDomainObj.getCuntry()!=null && vendorDomainObj.getCuntry().getCodedesc()!=null)
     {
		paycountryId=vendorDomainObj.getCuntry().getCodedesc();
	 }
}

if(session.getAttribute("VendorInfoList")!=null)
{
  vendorDomainObj=(Vendor)session.getAttribute("VendorInfoList");
  if(vendorDomainObj.getPhone()!=null)
   	{
		E_phno=dbUtil.appendstring(vendorDomainObj.getPhone());
	}
  if(vendorDomainObj.getFax()!=null)
	{
		E_fax=dbUtil.appendstring(vendorDomainObj.getFax());
	}
	
	
	
  if(vendorDomainObj.getClimit()!=null)
	{
		E_crLimit=String.valueOf(vendorDomainObj.getClimit());
	}
  if(vendorDomainObj!=null && vendorDomainObj.getCterms()!=null && vendorDomainObj.getCterms().getId()!=null)
	{
		E_crTerms=vendorDomainObj.getCterms().getId().toString();
	}
  if(vendorDomainObj!=null && vendorDomainObj.getEamanager()!=null && vendorDomainObj.getEamanager().getUserId()!=null)
    {
        arcode=vendorDomainObj.getEamanager().getUserId().toString();
    }
}
request.setAttribute("creditTermList",dbUtil.getGenericCodeList(29,"yes","Select Credit Terms"));
request.setAttribute("arcodelist",dbUtil.getUserList(24,"no","Select AR Contact Code"));

if(session.getAttribute("customerbean")!=null)
{
  customerbean=(customerBean)session.getAttribute("customerbean");
  vendorDomainObj.setCredit(customerbean.getCredit());
  vendorDomainObj.setWfile(customerbean.getWfile());
  vendorDomainObj.setDeactivate(customerbean.getDeactivate());
}
request.setAttribute("customerbean",vendorDomainObj);


String editPath=path+"/editvendor.do";
String modify="";
modify = (String) session.getAttribute("modifyforcustomer");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
    
	}
if(request.getAttribute("openwindow") != null)
 {
 %>
 <script type="text/javascript">
  window.open("<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button=checked","", "width=700,height=400");
  window.moveTo(200,180);

</script>
 <%
 }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Vendor.jsp' starting page</title>
    
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
    <script type="text/javascript">
     
    function toUppercase(obj) 
	 {
		obj.value = obj.value.toUpperCase();
	 }
	
   function limitText(limitField, limitCount, limitNum) 
	{
	   limitField.value = limitField.value.toUpperCase();
       if (limitField.value.length > limitNum)
        {
           limitField.value = limitField.value.substring(0, limitNum);
        } 
       else
        {
			limitCount.value = limitNum - limitField.value.length;
 	    }
 
    }
	
  function setcreditterms()
   {
    (document.editvendorForm.climit.value == 0)?document.editvendorForm.cterms.selectedIndex=1:document.editvendorForm.climit.value > 0?document.editvendorForm.cterms.selectedIndex=3:document.editvendorForm.cterms.selectedIndex=0;
   }
  
  function disable()
   {
   
      var state= document.editvendorForm.paymethod;
      var opt=document.editvendorForm.swift;
        (state.value==1)?opt.disabled=true:opt.disabled=false;
   }
   
   function popup()
    { 
      
      document.editvendorForm.submit();
      
      window.open("<%=path%>/jsps/Tradingpartnermaintainance/paymentPopup.jsp?button="+"editvend","","width=650,height=348");
 
    }
   
    function popup2()
    { 
      document.editvendorForm.submit();  
      window.open("<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button="+"editpayaddress","","width=700,height=400");
        
    }
   function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id != "note")
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue" && input[i].id != "add" && input[i].id != "add1" && input[i].id != "note")
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
  	 	document.getElementById("add1").style.visibility = 'hidden';
  	 }
   }
    function confirmnote()
	{
	
		document.editvendorForm.buttonValue.value="note";
    	document.editvendorForm.submit();
   	}
    </script>
    <%@include file="../includes/resources.jsp" %>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/editvendor" scope="request">
		
		
<table>		
<tr>
    <td width="100%">&nbsp;&nbsp;</td>
    <td align="right">
      <input type="button" class="buttonStyleNew" value="Notes" id="note" onclick="confirmnote()" />
   </td>
										
</tr>
</table>
		
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Vendor Information </tr>
 <tr>
    <td width="50%">    
       <table width="100%" border="0">
           <tr>
              <td class="style2" width="10%">TIN </td>
              <td align="left"><html:text property="tin" size="40"  value="<%=vendorDomainObj.getTin()%>" onkeyup="toUppercase(this)"/></td>        
           </tr>
        </table>
     </td>
     <td width="50%">
       <table width="80%" border="0">
            <tr>       
               <td width="1%" ><span class="style2">W-9 Field </span></td>
               <td width="30%"><html:checkbox property="wfile" name="customerbean"></html:checkbox></td>
               <td class="style2" width="30%">Vendor de-Activate</td>
               <td ><html:checkbox property="deactivate" name="customerbean" onclick="block()"></html:checkbox></td>    
           </tr>
       </table>
     </td>  
 </tr>
 <tr> 
     <td width="50%">
         <table width="10%" border="0">       
            <tr>
                <td class="style2" height="21">Legal Name</td>
           </tr>
           <tr>
               <td>
                  <html:text property="legalname" size="57"  value="<%=vendorDomainObj.getLegalname()%>" onkeyup="toUppercase(this)"/>
               </td>
          </tr>
        </table></td>
       <td width="50%">
          <table width="100%" border="0">
           <tr>
                <td class="style2">DBA</td>
           </tr>
           <tr>
                <td><html:text property="dba" size="57" value="<%=vendorDomainObj.getDba()%>" onkeyup="toUppercase(this)"/></td>            
           </tr>
       </table>
      </td>
 </tr>
</table>         
   
<table>
   <tr>
     <td>&nbsp;&nbsp;</td>
   </tr>  
</table> 

<table width="100%" border="0" class="tableBorderNew">
 <tr class="tableHeadingNew" colspan="2">A/P Contact</tr>
 <tr>
	 <td>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
		       <td class="style2">Econo Acct Manager</td>
		       <td class="style2" >Credit</td>
		       <td class="style2">Credit Limit</td>
		       <td class="style2">Credit Terms</td>
		       <td>&nbsp;&nbsp;</td>
           </tr>
           <tr>
               <td><html:select property="eamanager" styleClass="verysmalldropdownStyle" value="<%=arcode%>">
                    <html:optionsCollection name="arcodelist"/>
                    </html:select></td>
               <td ><html:checkbox property="credit" name="customerbean" ></html:checkbox></td>
               <td><html:text property="climit" value="<%=E_crLimit%>" onkeyup="toUppercase(this)" onblur="setcreditterms()"  maxlength="5" onkeypress="return checkIts(event)"  /></td>
               <td><html:select property="cterms" styleClass="selectboxstyle" value="<%=E_crTerms %>">
                    <html:optionsCollection name="creditTermList"/>
                    </html:select></td>
               <td>&nbsp;&nbsp;</td>
                <td align="right">
                 <input type="button" class="buttonStyleNew" value="Add" id="add"
                 onclick="return GB_show('Contact', '<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'editpayaddress',200,600)" />
                 
                </td> 
          </tr>
          
      </table></td> 
 </tr>
 
 <tr>
    <td>
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
            
             <% int i=0; %>
	 	<display:table  name="<%=list%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="customertable" sort="list" > 
		<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font>For more Customers click on page numbers.
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
        <display:column title="C/O Name" property="company" ></display:column>
		<display:column title="Address" property="address1" />
		<display:column title="City" property="city2"></display:column>
		<display:column title="State" property="state"></display:column>
	    <display:column title="Zip" property="zip"></display:column>
		<display:column title="Country"><%=paycountryId%></display:column>
		<%i++; %> 
		</display:table>
    </table></td>
  </tr>
</table> 
  
<table>
   <tr>
     <td>&nbsp;&nbsp;</td>
   </tr>  
</table>  
 
<table width="100%" border="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Payment Information</tr>
 <tr class="style2">
       <td width="95%" align="right">
         <input type="button" class="buttonStyleNew" value="Add" id="add1" 
          onclick="return GB_show('Contact', '<%=path%>/jsps/Tradingpartnermaintainance/paymentPopup.jsp?button='+'editvend',300,600)" />
       
      </td> 
 </tr>       
       
<tr> 
     <td>
         <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
			 <% int j=0; %>
	 	<display:table  name="<%=PayList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="customertable" sort="list" > 
		<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> For more Customers click on page numbers.
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
				
				<%
				String iStr=String.valueOf(i);
  			    String tempPath=editPath+"?ind="+iStr;
				 %>	
        <display:column title="Payment Method" property="paymethod" ></display:column>
		<display:column title="Bank Name"     property="bankname" />
		<display:column title="Bank Address"  property="baddr" />
		<display:column title="Account Name" property="vacctname"></display:column>
		<display:column title="Account No" property="vacctno"></display:column>
		<display:column title="Aba" property="aba"></display:column>
		<display:column title="Swift" property="swift"></display:column>
		<display:column title="Remit Email" property="remail"></display:column>
		<display:column title="Remit Fax" property="rfax"></display:column>
		<display:column title="Email" property="payemail"></display:column>
		<%j++; %> 
		</display:table>
     </table></td>
  </tr> 
<html:hidden property="buttonValue" styleId="buttonValue" />
</table>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
   