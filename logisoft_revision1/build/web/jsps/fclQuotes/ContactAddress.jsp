<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cvst.logisoft.domain.CustAddress"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
List addressList=new ArrayList();
if(session.getAttribute("addressList")!=null){
addressList=(List)session.getAttribute("addressList");
}
String link="";
String editPath=path+"/contactAddress.do";
String button="";
if(session.getAttribute("buttonValue")!=null){
button=(String)session.getAttribute("buttonValue");
}
String clientNo="";
if(request.getAttribute("clientNo")!=null){
	 clientNo=(String)request.getAttribute("clientNo");
}
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("Booking"))
{
%>
<script type="text/javascript">
parent.parent.GB_hide();
parent.parent.getBookingCustomer('${clientList[0]}','${clientList[1]}','${clientList[2]}','${clientList[3]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[7]}','${clientList[8]}','${clientList[9]}','${clientList[10]}','${clientList[11]}');
</script>
<%
}
 %>
<html> 
	<head>
		<title>JSP for ContactAddressForm form</title>
		<script type="text/javascript">
		function getSearchResult(val1){
		document.contactAddressForm.clientNo.value=val1
		document.contactAddressForm.buttonValue.value="SearchCustomer";
		document.contactAddressForm.submit();
	}
		</script>
	</head>
	<%@include file="../includes/baseResources.jsp" %>
	<body class="whitebackgrnd">
		<html:form action="/contactAddress" scope="request">
		<table width="100%" border="0" cellpadding="0" cellspacing="2" class="tableBorderNew">
			<tr class="tableHeadingNew">Customer Search</tr>
			<tr>
			    <td><table>
			        <tr>
				       <td class="textlabelsBold">Address</td>	  
				       <td><html:text property="address" styleClass="textlabelsBoldForTextBox" value=""></html:text></td>		
				       <td>
				           <input type="button" value="Search" onclick="getSearchResult('<%=clientNo %>')" 
				              Class="buttonStyleNew"/>
				       </td>
				    </tr>
				</table></td>
			</tr>
		 </table>	
		 			
		 <br style="padding-top:2px;" />		
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		 <tr class="tableHeadingNew">List of Addresses</tr>
		 <tr><td>
				<%
				   if(addressList!=null && addressList.size()>0){
				       int i=0; 
				%>
<div id="divtablesty1" style="height:80%;">
<display:table name="<%=addressList%>" class="displaytagstyleNew" pagesize="10"  style="width:100%" id="arInquiry" sort="list" >

<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Contacts displayed,For more Records click on page numbers.
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
  			
  			CustAddress custAddress=(CustAddress)addressList.get(i);
  			if(custAddress.getAcctName()!=null){
  			accountName=custAddress.getAcctName();
  			accountNo=custAddress.getAcctNo();
  			}
  			link=editPath+"?paramId="+i+"&button="+button+"&accountNo="+accountNo;
  			 %>
  	<display:column title="CustomerName" ><a href="<%=link%>" ><%=accountName%></a></display:column>
  	<display:column property="acctNo" title="CustomerNo"></display:column>
	<display:column property="address1" title="Address"></display:column>
	<display:column property="phone" title="Phone"></display:column>
	<display:column property="fax" title="Fax"></display:column>
	<display:column property="email1" title="Email"></display:column>

<%i++;%>
</display:table>  
<%}%>
  </div>
  </td></tr>
</table>
			<html:hidden property="clientNo"/>
			<html:hidden property="buttonValue"/>
    </html:form>
  </body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

