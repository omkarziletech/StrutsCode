<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="com.gp.cong.logisoft.util.DBUtil"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<jsp:directive.page import="com.gp.cvst.logisoft.domain.ArInvoice"/>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	DBUtil dbUtil = new DBUtil();
	request.setAttribute("shipmentTypeList",dbUtil.getShipmentType());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <base href="<%=basePath%>">
    <title>ARInvoiceGenerator</title>
	<%@include file="../includes/baseResources.jsp" %>
	<%@include file="../includes/resources.jsp" %>  

   <script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
   		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    start = function(){
				viewInvoice(${view});
			}
		    dojo.addOnLoad(start);
		</script>
</head>
<body class="bodybackground" >
<html:form action="/arInvoice"  name="arInvoiceform" type="com.gp.cvst.logisoft.struts.form.ARInvoiceForm" scope="request">
<html:hidden property="buttonValue"/>
<html:hidden property="arInvoiceChargesId"/>
<html:hidden property="arInvoiceId" value="${arInvoice.id}" />
<html:hidden property="manifest" value="${arInvoice.manifest}" />
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr>
	<td colspan="2">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
			<tr class="tableHeadingNew"><td>AR Invoice</td>
			<td align="right">
			  <c:if test="${goBackToInquiry!=null}">
			  <input type="button" class="buttonStyleNew" style="width:90px" id="previous" value="Go Back"  onclick="goToARInquiryPage()"/>
			  </c:if>
			   <c:if test="${goBackToInquiry==null}">
			<input type="button" class="buttonStyleNew" style="width:90px" id="previous" value="Go Back"  onclick="getSearchPage()"/> 
			</c:if></td>
			</tr>
		</table>
	</td>		
</tr> 


<tr>
  <td  valign="top" width="50%" >
   <table width="100%" border="0" cellpadding="0" cellspacing="0" >
   <tr>
      <td><table width="50%" >
      <c:if test="${arInvoice.manifest!=null}">
		<font color="red" size="4"> This Invoice has Manifested</font>
      </c:if>
      <c:if test="${message!=null}">
       	<font color="red" size="4"><c:out value="${message}"></c:out> </font>
      </c:if>
	        <tr class="textlabels">
				<td>Customer</td>
<%--				<td><html:text property="cusName" value="${arInvoice.customerName}"></html:text></td>--%>
<c:choose>
	<c:when test="${arInvoice.id!=null}">
	<td>
				    <input name="cusName" id="cusName" readonly="true" value="${arInvoice.customerName}" onkeyup="toUppercase(this)" onkeydown="setCustomerDetails(this.value)"  />
				    <dojo:autoComplete formId="arInvoiceform" textboxId="cusName" action="<%=path%>/actions/getAccountName.jsp?tabName=AR_INVOICE&from=0" />
				</td>
	</c:when>
	<c:otherwise>
	<td>
				    <input name="cusName" id="cusName" value="${arInvoice.customerName}" onkeyup="toUppercase(this)" onkeydown="setCustomerDetails(this.value)"  />
				    <dojo:autoComplete formId="arInvoiceform" textboxId="cusName" action="<%=path%>/actions/getAccountName.jsp?tabName=AR_INVOICE&from=0" />
				</td>
	</c:otherwise>

</c:choose>
				
				<td align="left">Account Number</td>
				<td><html:text property="accountNumber" value="${arInvoice.accountNumber}"></html:text></td>
			</tr>
			<tr class="textlabels">
				<td>Customer Type</td>
				<td><html:text property="arCustomertype" value="${arInvoice.customerType}"></html:text></td>
				<td align="left">Contact Name</td>
				<td><html:text property="contactName" value="${arInvoice.contactName}"></html:text></td>
			</tr>
			<tr class="textlabels">
				<td valign="top">Address</td>
				<td><html:textarea property="address" rows="2" cols="16" value="${arInvoice.address}"></html:textarea></td>
		    <td align="left">Phone</td>
				<td><html:text property="phoneNumber" value="${arInvoice.phoneNumber}" maxlength="13"></html:text></td>
		    </tr>
       <tr class="textlabels">
		  <td  valign="top">For</td>
		  <td  valign="top" align="left" colspan="3"><html:textarea property="notes" rows="3" cols="48" value="${arInvoice.notes}"></html:textarea></td>

  	   </tr>
  </table></td>	
  </tr>
</table>
</td>
 <td width="30%" valign="top">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
   		<td align="top"><table width="50%">
	        <tr class="textlabels">
				<td align="left">Invoice Number:</td>
				<td><html:text property="invoiceNumber" value="${arInvoice.invoiceNumber}" style="border:0px;" readonly="true" ></html:text></td>
				<td>&nbsp;</td>

			</tr>
			<tr class="textlabels">
				 <td align="left">BL/DR Number</td>
<%--				 <td><html:text property="bl_drNumber" value="${arInvoice.blDrNumber}"></html:text></td>--%>
				 <td>
				 <div id="BLNumber" style="height:0px;width:0px;">
					<input  name="bl_drNumber" id="bl_drNumber" value="${arInvoice.blDrNumber}"  />   
		 	        <dojo:autoComplete formId="arInvoiceform" textboxId="bl_drNumber" action="<%=path%>/actions/getBlNo.jsp?tabName=ARINVOICEGENERATOR&from=0"/>       
		 	    </div>
		 	     <div id="DRNumber" style="margin-top:-24px;">
		 	       <input name="ord_bl_drNumber" id="ord_bl_drNumber" value="${arInvoice.blDrNumber}" />
		 	     </div>
		 	    </td>
				<td align="left">
				<html:radio property="bl_drRadio" value="BL" onclick="displayBL()"></html:radio>BL<br>
				<html:radio property="bl_drRadio" value="DR" onclick="displayDR()"></html:radio>DR
				</td>
			</tr>
			<tr class="textlabels">
				<td align="left">Date</td>
				<fmt:formatDate pattern="MM/dd/yyyy" var="date" value="${arInvoice.date}"/> 
				<td><html:text property="date" value="${date}" styleId="txtdateDitemcreatedon"></html:text>
				<img src="<%=path%>/img/CalendarIco.gif" id="dateDitemcreatedon" alt="cal" name="cal1" 
				 onmousedown="insertDateFromCalendar(this.id,0);"/></td>
		    </tr>
		    <tr class="textlabels">
				<td align="left">Terms</td><td>
				<html:select property="term" onchange="dueDateCalculation(this)" value="${arInvoice.term}">
				<html:optionsCollection name="creditTermList"/>
				</html:select>
				
         </td>
				
		    </tr>
		    
		    <tr class="textlabels">
				<td align="left">Due Date</td>
				<td>
				<fmt:formatDate pattern="MM/dd/yyyy" var="dueDate" value="${arInvoice.dueDate}"/> 
				<html:text property="dueDate" value="${dueDate}"></html:text>
				</td>
		   </tr>
	
		</table>
		</td>
	   </tr>
   	</table></td>
	</tr>
	<tr>
		   <td colspan="2" align="center">
		    <input type="button" class="buttonStyleNew" name="update" style="width:90px"  value="Save Invoice"  onclick="updateArInvoice()"/>
			<input type="button" class="buttonStyleNew" name="delete" style="width:90px"  value="Delete Invoice"  onclick="deleteArInvoice()"/>
		 <c:if test="${arInvoice.id!=null && arInvoice.manifest==null}">
		<input type="button" class="buttonStyleNew" name="manifest" style="width:90px" value="Manifest" onclick="manifest1()">
      </c:if>
		   
		   </td>
	</tr>
</table>
<br style="padding-top:5px;"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">List of Line Items</tr>	
<tr>
   <td>	
  <c:set var="totalamount" value="${0}"/> 
	<div id="divtablesty1" class="scrolldisplaytable">
    <display:table name="${arInvoice.arinvoiceCharges}" pagesize="<%=pageSize%>" defaultorder="descending"  
    defaultsort="6"  class="displaytagstyle" id="arinvoicecharges" sort="list">
	<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Line Items displayed,For more Records click on page numbers.
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
			   <display:footer>
			   <td colspan="6" align="right">
			   <b>Total Invoice=</b>
			   </td>
			   	<td>
			   	  	<c:set var="totalamount" value="${totalamount+arinvoicecharges.amount}"/>
			   	<fmt:formatNumber pattern="###,###,##0.00" value="${totalamount}" var="totalAmt"> </fmt:formatNumber>
				<font size="2"><c:out value="${totalAmt}"></c:out></font>
				</td>
			   </display:footer>
	<display:column property="chargesCode"  title="Charge Code" sortable="true"  ></display:column>
	<display:column property="chargesCodeDesc"  title="Charge CodeDesc" sortable="true"  ></display:column>
  	<display:column property="glAccount"  title="GL Account" sortable="true" ></display:column>
	<display:column property="description"  title="Description" sortable="true" ></display:column>
	<fmt:formatNumber pattern="###,###,##0.00" var="rate" value="${arinvoicecharges.rate}"/>
	<display:column   title="Rate" sortable="true" >${rate}
	</display:column>
	<display:column property="quantity"  title="Quantity" sortable="true" ></display:column>
	<fmt:formatNumber pattern="###,###,##0.00" var="amount" value="${arinvoicecharges.amount}"/>
	<display:column title="Amount" sortable="true" >${amount}</display:column>
	
	<display:column paramId="id" paramName="getId" sortable="true">
	<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
	<img src="<%=path%>/img/icons/delete.gif" onclick="deleteArCharges(${arinvoicecharges.id})"/></span>
	</display:column>	
	<display:column>
	     <c:set var="totalamount" value="${totalamount+arinvoicecharges.amount}"/>
	</display:column>
	
	</display:table>	   
    </div>
  
   </td>
</tr>
</table>

<br style="padding-top:5px;"/>

<table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Add Line Items</tr>
 <tr class="textlabels">
  <td valign="baseline"><table width="50%" border="0" >
 	  <tr class="textlabels">
		  <td>Charge Code</td>
<%--		  <td ><html:text property="chargeCode" value=""></html:text></td>--%>
		   <td valign="top">
			<input name="chargeCode" id="chargeCode"  onkeydown="getCodeDesc(this.value)" />
			<dojo:autoComplete formId="arInvoiceform" textboxId="chargeCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=AR_INVOICE&from=0" />
		</td>	
		<td>Charge Description</td>
		<td>
			<input name="chargeCodeDesc" id="chargeCodedesc"  onkeydown="getCode(this.value)" />
			<dojo:autoComplete formId="arInvoiceform" textboxId="chargeCodeDesc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=AR_INVOICE&from=0" />
		</td>
		<td>(OR) GL Account</td>
<%--		  <td><html:text property="glAccount"></html:text></td>--%>
		<td valign="top">
			 <input name="glAccount" id="glAccount" onkeydown="setGlAccountDescription(this.value)" />
			 <dojo:autoComplete formId="arInvoiceform" textboxId="glAccount" action="<%=path%>/actions/accountNo.jsp?tabName=AR_INVOICE_GENERATOR&from=0" />
		</td><%--
		<td>
				Shipment Type
		</td>
		<td><html:select property="shipmentType"><html:optionsCollection name="shipmentTypeList"/>
				</html:select>
		</td>
	--%></tr>
	<tr class="style2"> 
			<td>Rate</td>	 	
			<td><input name="rate" id="rate"  /></td>
			<td  style="padding-left:10px;">Quantity</td>
			<td ><html:text property="quantity"   onkeypress="check(this,10)" onblur="checkdec(this)"  value="" maxlength="13"></html:text></td>
			
	 	<td >  Description</td>
			   <td  rowspan="3"><html:textarea property="description" rows="2" cols="20" value=""></html:textarea></td> 
				</tr>
	 
	  <tr class="textlabels">
	 	<td colspan="8" align="center" >
	 	<input type="button" class="buttonStyleNew" value="Add"  name="save" onclick="submitForm()"/> 
	 	</td> 	  
			 </tr>
	  <tr class="textlabels">
      </tr>
  </table></td>	
 </tr>
 <tr><td><br style="padding-bottom:2px; "/></td></tr>	
</table>
</html:form>
</body>
<script language="javascript" type="text/javascript">
	function submitForm(){
		if(document.arInvoiceform.cusName.value==""){
			alert("Please select Customer ");
			return;
		}
<%--		if(document.arInvoiceform.invoiceNumber.value==""){--%>
<%--			alert("Please select InvoiceNumber ");--%>
<%--			return;--%>
<%--		}--%>
		document.arInvoiceform.buttonValue.value="save";
		document.arInvoiceform.submit();
	}
	function deleteArCharges(val1){
		document.arInvoiceform.arInvoiceChargesId.value = val1;
		document.arInvoiceform.buttonValue.value="deleteCharges";
	 	document.arInvoiceform.submit();
	}
	function deleteArInvoice(){
		var1=confirm("Do u want to Delete this Ar Invoice");
		if(var1){
			document.arInvoiceform.buttonValue.value="deleteInvoice";
			document.arInvoiceform.submit();
		}
	}
	function updateArInvoice(){
		document.arInvoiceform.buttonValue.value="updateArInvoice";
		document.arInvoiceform.submit();
	}
	function toUppercase(obj){ //converting to uppercase
		obj.value = obj.value.toUpperCase();
	}
	function setCustomerDetails(ev){
	   		if(event.keyCode == 9||event.keyCode == 13){
			     document.arInvoiceform.buttonValue.value="customerInfo";
			     document.arInvoiceform.submit();
	   		}
	 }
	function getSearchPage(){// redirect To  Search Page 
		window.location.href="<%=path%>/jsps/AccountsRecievable/SearchArInvoice.jsp";
	}
	function goToARInquiryPage(){ // redirect to AR Inquiry page.
	    window.location.href="<%=path%>/AccountRecievable.do?";
	}
	function viewInvoice(val1){
		if(val1!="" && val1!=undefined){
			var input = document.getElementsByTagName("input");
			for(i=0; i<input.length; i++){
			  			input[i].readOnly=true;
					 	}
			var input = document.getElementsByTagName("input");
			for(i=0; i<input.length; i++){
						if(input[i].id=="previous"){
			  			}else{
			  				input[i].className="areahighlightgreydefult";
				  			input[i].readOnly=true;
			  			}
					 	}
		  	var textarea = document.getElementsByTagName("textarea");
			for(i=0; i<textarea.length; i++){
			 			textarea[i].readOnly=true;		 					
					  	}
	   		var select = document.getElementsByTagName("select");
	   		for(i=0; i<select.length; i++)	{
				 		select[i].disabled=true;
						select[i].style.backgroundColor="blue";
		  		  	 	}
		  	document.getElementById("delete").style.visibility = 'hidden';
		  	document.getElementById("save").style.visibility = 'hidden';
		  	document.getElementById("update").style.visibility = 'hidden';
		  	document.getElementById("manifest").style.visibility = 'hidden';
			}
			// when you Add new invoice these buttons will be hidden,Once you  save invoice with invoice charges it will display
			if(document.arInvoiceform.arInvoiceId.value==""){
				document.getElementById("delete").style.visibility = 'hidden';	
				document.getElementById("update").style.visibility = 'hidden';
				document.getElementById("manifest").style.visibility = 'hidden';
			}
			
			if(document.arInvoiceform.manifest.value!=undefined && document.arInvoiceform.manifest.value=="M"){
				var input = document.getElementsByTagName("input");
				for(i=0; i<input.length; i++){
					//alert("Name is "+input[i].name);
						if(input[i].name!="chargeCode" && input[i].name!="chargeCodeDesc" && 
						input[i].name!="glAccount" && input[i].name!="rate" && input[i].name!="quantity"){
							input[i].readOnly=true;
						}
					 	}
				var textarea = document.getElementsByTagName("textarea");
				for(i=0; i<textarea.length; i++){
							if(textarea[i].name!="description"){
							//	alert(textarea[i].name);
								textarea[i].readOnly=true;	
							}
					  	}
				var select = document.getElementsByTagName("select");
	   			for(i=0; i<select.length; i++)	{
				 		select[i].disabled=true;
						select[i].style.backgroundColor="blue";
		  		  	 	}
			}
	}
	function manifest1(){
	//alert("save");
	 document.arInvoiceform.buttonValue.value="manifest";
	 document.arInvoiceform.submit();
	}
		function dueDateCalculation(ev){  
				var params = new Array();
				params['requestFor'] = "dueDate";
				params['date'] = document.getElementById("date").value;
				params['term'] = document.getElementById("term").options[document.arInvoiceform.term.selectedIndex].text;
				var bindArgs = {
					url: "<%=path%>/actions/dueDateCalculation.jsp",
					error: function(type, data, evt){alert("Please Enter the Date");},
					mimetype: "text/json",
					content: params
				};
				var req = dojo.io.bind(bindArgs);
				dojo.event.connect(req, "load", this, "populateDueDate");
	}
	function populateDueDate(type, data, evt) {
				if(data){
					document.getElementById("dueDate").value=data.newDate;
					}
	}
	
		        function getCodeDesc(ev){
       document.getElementById("chargeCodeDesc").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "CommodityDetails";
				 params['code'] = document.arInvoiceform.chargeCode.value;
				 params['codeType'] = '2';
				  var bindArgs = { 
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateCodeDesc");
			    }
			 }
	   function populateCodeDesc(type, data, evt) {
		   	if(data){
		   		document.getElementById("chargeCodeDesc").value=data.commodityDesc;
		   	}
		   }
		   
		   		        function getCode(ev){
       document.getElementById("chargeCode").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "CommodityDetails";
				 params['codeDesc'] = document.arInvoiceform.chargeCodeDesc.value;
				 params['codeType'] = '2';
				  var bindArgs = { 
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateCode");
			    }
			 }
	   function populateCode(type, data, evt) {
		   	if(data){
		   		document.getElementById("chargeCode").value=data.commodityCode;
		   	}
		   }
		   
      function displayBL(){
		   if(document.arInvoiceform.bl_drRadio[0].value="BL"){
				document.getElementById("BLNumber").style.visibility="visible";
				document.getElementById("DRNumber").style.visibility="hidden";
			}
		}	
      function displayDR(){
      	alert("DR Number is not present, plz enter the number");
      	if(document.arInvoiceform.bl_drRadio[1].value="DR"){
				document.getElementById("BLNumber").style.visibility="hidden";
				document.getElementById("DRNumber").style.visibility="visible";
			}
      }
	  
  </script>

	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
