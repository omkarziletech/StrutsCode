<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerAccounting,com.gp.cong.logisoft.beans.customerBean,com.gp.cong.logisoft.domain.Vendor" pageEncoding="ISO-8859-1"%>
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
List list = new ArrayList();//--------------for display column-----------
List PayList = new ArrayList();//----------for paymentinfo display--------
if(session.getAttribute("paymentList")!=null)
{
PayList=(List)session.getAttribute("paymentList");//-------LIST DISPLAYING PAYMENT INFORMATION--------
}

 vendorDomainObj.setWfile("off");
 vendorDomainObj.setCredit("off");
 vendorDomainObj.setDeactivate("off");
 String ph_no="";
 String fax="";
 String crLimit="";
 String crTerms="0";
 String arcode="0";
  String paycountryId="";
 
 
 if(session.getAttribute("VMasteraddress")!=null)
 {
     vendorDomainObj=(Vendor)session.getAttribute("VMasteraddress");
     if(vendorDomainObj.getAddress1() != null)
     {
        list.add(vendorDomainObj);//-------LIST DISPLAYING ADDRESS INFORMATION----
     }
     if(vendorDomainObj!=null && vendorDomainObj.getCuntry()!=null && vendorDomainObj.getCuntry().getCodedesc()!=null)
     {
		paycountryId=vendorDomainObj.getCuntry().getCodedesc();
	 }
}
 
if(session.getAttribute("MasterVendorInfoList")!=null)
{
  vendorDomainObj=(Vendor)session.getAttribute("MasterVendorInfoList");
  if(vendorDomainObj.getPhone()!=null)
   	{
		ph_no=dbUtil.appendstring(vendorDomainObj.getPhone());
	}
  if(vendorDomainObj.getFax()!=null)
	{
		fax=dbUtil.appendstring(vendorDomainObj.getFax());
	}
  if(vendorDomainObj.getClimit()!=null)
	{
		crLimit=String.valueOf(vendorDomainObj.getClimit());
	}
  if(vendorDomainObj!=null && vendorDomainObj.getCterms()!=null && vendorDomainObj.getCterms().getId()!=null)
	{
		crTerms=vendorDomainObj.getCterms().getId().toString();
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
  customerbean.setCredit(customerbean.getCredit());
  customerbean.setWfile(customerbean.getWfile());
  customerbean.setDeactivate(customerbean.getDeactivate());
}
request.setAttribute("customerbean",customerbean);//--------for checkboxes name------
String editPath=path+"/mastervendor.do";

if(request.getAttribute("openwindow") != null)
 {
 %>
 <script type="text/javascript">
  window.open("<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button=checked","", "width=700,height=380");
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
    (document.mastervendorForm.mclimit.value == 0)?document.mastervendorForm.mcterms.selectedIndex=1:document.mastervendorForm.mclimit.value > 0?document.mastervendorForm.mcterms.selectedIndex=3:document.mastervendorForm.mcterms.selectedIndex=0;
   }
  
  
  function disable()
   {
   
  // document.vendorForm.swift.disabled=!document.vendorForm.paymethod.selectedIndex=1;
     var state= document.mastervendorForm.mpaymethod;
     var opt=document.mastervendorForm.mswift;
        (state.value==1)?opt.disabled=true:opt.disabled=false;
   }
   function popup()
    { 
      document.mastervendorForm.submit();
      window.open("<%=path%>/jsps/Tradingpartnermaintainance/paymentPopup.jsp?button="+"mvend","","width=650,height=348");
 
    }
   
    function popup2()
    { 
       document.mastervendorForm.submit(); 
      window.open("<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button="+"mpay","","width=700,height=370");
        
    }
  
</script>
 <%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd">
<html:form action="/mastervendor" scope="request">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Vendor Details</tr>
<tr>
   <td width="50%"> 
      <table width="100%" border="0">  
          <tr>
             <td class="style2" width="10%" >TIN</td>
             <td><html:text property="mtin" size="40"  value="<%=vendorDomainObj.getTin()%>" onkeyup="toUppercase(this)"/></td>
          </tr>   
       </table>
   </td>
   <td>
      <table width="100%" border="0"> 
        <tr> 
          <td class="style2" width="8%">W-9 Field </td>
          <td width="25%"><html:checkbox property="wfile" name="customerbean" ></html:checkbox></td>
          <td class="style2" width="30%">Vendor de-Activate</td>
          <td ><html:checkbox property="deactivate" name="customerbean" onclick="block()" ></html:checkbox></td>
        </tr>  
      </table>
   </td>        
</tr>
<tr> 
    <td width="50%">
        <table border="0" width="100%">
          <tr>   
              <td class="style2" width="50%">Legal Name </td>
          </tr>
          <tr>
             <td><html:text property="mlegalname" size="57" value="<%=vendorDomainObj.getLegalname()%>" onkeyup="toUppercase(this)" /></td>
          </tr>     
       </table>
   </td>  
   <td width="50%">
        <table border="0" width="100%">
          <tr>
             <td class="style2"> DBA</td>
           </tr>
           <tr>
              <td><html:text property="mdba"  size="57" value="<%=vendorDomainObj.getDba()%>" onkeyup="toUppercase(this)"/></td>
        
           </tr>  
         </table>
   </td>      
 </tr>
 </table>
 
 <table>
    <tr>
       <td>&nbsp;</td>
    </tr>
 </table>
 

<table width="100%" border="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2"> A/P Contact</tr> 
 <tr>
	 <td>
	    <table width="100%" border="0" cellspacing="3" cellpadding="0">
           <tr>
		       <td class="style2">Econo Acct Manager</td>
		       <td class="style2">Credit</td>
		       <td class="style2">Credit Limit</td>
		       <td class="style2">Credit Terms</td>
		       <td>&nbsp;&nbsp;</td>
           </tr>
           <tr>
               <td><html:select property="meamanager" styleClass="verysmalldropdownStyle" value="<%=arcode%>">
                    <html:optionsCollection name="arcodelist"/>
                    </html:select></td>
               <td><html:checkbox property="credit" name="customerbean" ></html:checkbox></td>
               <td><html:text property="mclimit" value="<%=crLimit%>" onkeyup="toUppercase(this)" onblur="setcreditterms()"  maxlength="5" onkeypress="return checkIts(event)"  /></td>
               <td><html:select property="mcterms" styleClass="selectboxstyle" value="<%=crTerms %>">
                    <html:optionsCollection name="creditTermList"/>
                    </html:select></td>
               <td>&nbsp;&nbsp;</td>
               <td align="right">
                 <input type="button" class="buttonStyleNew" value="Add" 
                  onclick="return GB_show('Contact', '<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'mpay',200,600)" />
                 
               </td> 
          </tr>
       </table>
   </td>  
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
		<display:column title="Country" ><%=paycountryId%></display:column>
		<%i++; %> 
		</display:table>
    </table>
</td>
</tr>      
</table>

<table>
    <tr>
       <td>&nbsp;</td>
    </tr>
</table>
  
 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2" > Payment Information</tr> 
<tr class="style2"> 
    <td width="95%" align="right">
      <input type="button" class="buttonStyleNew" value="Add" id="add" 
       onclick="return GB_show('Contact', '<%=path%>/jsps/Tradingpartnermaintainance/paymentPopup.jsp?button='+'mvend',300,600)" />
     
    </td> 
</tr>       
      
<tr>
   <td>
      <table border="0" >
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
		<display:column>&nbsp;&nbsp;</display:column>
		<display:column title="Remit Email" property="remail"></display:column>
		<display:column title="Remit Fax" property="rfax"></display:column>
		<display:column title="Email" property="payemail"></display:column>
		<%j++; %> 
		</display:table>       

 <%--<tr class="style2"><!-- 7r -->
          <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
               <tr class="style2">
                   <td class="style2">Bank Account </td>
                   <td class="style2" align="left" div="left">Payment Method </td>
                   <td class="style2" align="left">Bank Name</td>
                   <td class="style2" align="left">Bank Address</td>
                   
                   
               </tr>
               <tr class="style2">
               
                    <td><html:text property="mbaccount"  maxlength="20" value="<%=vendorDomainObj.getBaccount()%>" onkeyup="toUppercase(this)"/></td>
                    <td><html:select property="mpaymethod" styleClass="selectboxstyle" onchange="disable()">
                         <html:option value="0">CHECK</html:option>
                         <html:option value="1">ACH</html:option>
                         <html:option value="2">WIRE</html:option>
                         </html:select></td>
                    <td><html:text property="mbankname"  maxlength="30" value="<%=vendorDomainObj.getBankname()%>"onkeyup="toUppercase(this)"/></td>
                    <td><html:textarea property="mbaddr" value="<%=vendorDomainObj.getBaddr()%>" onkeyup="limitText(this.form.address1,this.form.countdown,30)" cols="25" rows="4"/></td>
                    <td>&nbsp;</td>
                
               </tr>
         </table></td> 
 </tr><!-- 7rend -->
 
 <tr>&nbsp;&nbsp;</tr>         
 <tr class="style2"><!-- 8r -->
          <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
               <tr class="style2">
                   <td class="style2">Account Name </td>
                   <td class="style2">Account No </td>
                   <td class="style2">ABA Routing</td>
                   <td class="style2">Swift</td>
                   
               </tr>
               <tr class="style2">
                    <td><html:text property="mvacctname"  maxlength="30"  value="<%=vendorDomainObj.getVacctname()%>" onkeyup="toUppercase(this)"/></td>
                    <td><html:text property="mvacctno" maxlength="10"  value="<%=vendorDomainObj.getVacctno()%>"onkeyup="toUppercase(this)"/></td>
                    <td><html:text property="maba"    maxlength="20" value="<%=vendorDomainObj.getAba()%>"    onkeyup="toUppercase(this)"/></td>    
                    <td><html:text property="mswift" maxlength="20"  value="<%=vendorDomainObj.getSwift()%>"  onkeyup="toUppercase(this)"/></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
               </tr>
               <tr class="style2">
                    <td>&nbsp;</td>
               </tr>
               <tr class="style2">  											
                    <td class="style2">Remit Email</td>							
                    <td class="style2">Remit Fax</td>							
                    <td class="style2">Email</td>
                    <td class="style2">Vendor de-Activate</td>
               </tr>    
               <tr class="style2">
                    <td><html:text property="mremail" maxlength="20" value="<%=vendorDomainObj.getRemail()%>"/></td>
                    <td><html:text property="mrfax" maxlength="13"  value="<%=vendorDomainObj.getRfax()%>"   onkeypress="getIt(this)"/></td>
                    <td><html:text property="mpayemail" maxlength="20"  value="<%=vendorDomainObj.getPayemail() %>"/></td>
                    <td><html:checkbox property="deactivate" name="customerbean"></html:checkbox></td>
               </tr>
         </table></td> 
 </tr><!-- 8rend --> --%>   

</table>
</td></tr>
</table>

<html:hidden property="buttonValue"/>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
  