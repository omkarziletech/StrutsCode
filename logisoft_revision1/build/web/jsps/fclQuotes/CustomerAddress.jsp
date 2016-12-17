<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC,
         com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.CustomerContact"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
List addressList=null;
if(session.getAttribute("addressList")!=null){
	addressList=(List)session.getAttribute("addressList");
}
TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
TradingPartner tradingPartner = new TradingPartner();
String custNo="",custName="";
if(session.getAttribute("custNo")!=null){
	custNo=(String)session.getAttribute("custNo");
}

if(session.getAttribute("customerName")!=null){
	custName=(String)session.getAttribute("customerName");
}

String button="";
if(session.getAttribute("buttonValue")!=null){
	button=(String)session.getAttribute("buttonValue");
}
//----request fetched when records are updated after Edit & Delete---
if(session.getAttribute("UpdatedContactList")!=null){
 addressList=(List)session.getAttribute("UpdatedContactList");
}
String link="";
String editPath=path+"/customerAddress.do";
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("Booking"))
{
%>
<script type="text/javascript">
parent.parent.GB_hide();
parent.parent.getBookingCustomer('${clientList[0]}','${clientList[1]}','${clientList[7]}','${clientList[3]}','${clientList[10]}','${clientList[11]}','${clientList[12]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[8]}');
</script>
<%
}
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("Quotation"))
{
%>
<script type="text/javascript">
            parent.parent.GB_hide();
            parent.parent.getContactNamefromPopup('${clientList[0]}','${clientList[1]}','${clientList[2]}','${clientList[3]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[7]}','${clientList[8]}','${clientList[9]}','${clientList[10]}');
</script>
<%
}
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("CarrierQuotation"))
{
%>
<script type="text/javascript">
parent.parent.GB_hide();
parent.parent.getCarrierContactNamefromPopup('${clientList[0]}','${clientList[1]}','${clientList[2]}','${clientList[3]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[7]}','${clientList[8]}','${clientList[9]}','${clientList[10]}');
</script>
<%
}
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("MultipleContactsForQuotationClient")){
%>
<script type="text/javascript">
parent.parent.GB_hide();
parent.parent.getContactNameAndEmailfromPopup('${clientList[0]}','${clientList[1]}','${clientList[2]}','${clientList[3]}','${fclContactId}','${fclAccountNo}');
</script>
<%}
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("MultipleContactsForQuotationCarrier")){
%>
<script type="text/javascript">
parent.parent.GB_hide();
parent.parent.getMultipleCarrierContactNameAndEmailfromPopup('${clientList[0]}','${clientList[1]}','${clientList[2]}','${clientList[3]}');
</script>
<%}%>
<html>
	<head>
		<title>JSP for CustomerAddressForm form</title>
	<%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>

                <script language="javascript" src="<%=path%>/js/common.js"></script>

	<script type="text/javascript">
	function getSearchResult(){
            if(document.customerAddressForm.custName.value==''){
                alertNew('Please Select Customer Name to add contact');
                return false;
            }else{
                document.customerAddressForm.buttonValue.value="SearchCustomer";
		document.customerAddressForm.submit();
            }
		//document.customerAddressForm.custNameTemp.value=val1;
		//document.customerAddressForm.custName.value=val2;

	}
	function setFocus(){
		setTimeout("set()",150);
	}
	function set(){
		document.customerAddressForm.contactName.focus();
	}
	function gotoTradingPartnerContactConfig(val1,val2){
            if(document.customerAddressForm.custName.value==''){
                alertNew('Please Select Customer Name to add contact');
                return false;
            }
		document.customerAddressForm.buttonValue.value="contactConfig";
		document.customerAddressForm.submit();
	}
	function selectedContacts(val1,val2){
		var flag=false;
                if(document.customerAddressForm.selectedCheckBox!=undefined && document.customerAddressForm.selectedCheckBox.length==undefined){
                    if(document.customerAddressForm.selectedCheckBox.checked){
				flag=true;
                    }
                }else{
                    for(var i=0;i<document.customerAddressForm.selectedCheckBox.length;i++){
                            if(document.customerAddressForm.selectedCheckBox[i].checked){
                                    flag=true;
                                    break;
                            }
                    }
                }
		if(!flag){
			alertNew("Please select Contact");
			return;
		}
                if(undefined != window.close()){
            window.close();
        }else{
	   document.customerAddressForm.buttonValue.value="checkedContacts";
	   document.customerAddressForm.submit();
        }
        }
	function editContactInfo(val1,val2,val3){
	   document.customerAddressForm.recordId.value=val1;
	   document.customerAddressForm.buttonValue.value="editContact";
	   document.customerAddressForm.submit();
	}
	function deleteContactInfo(val1,val2,val3,index){
		document.customerAddressForm.recordId.value=val1;
		document.customerAddressForm.custName.value=val3;
		document.customerAddressForm.buttonValue.value="deleteContact";
		var result = confirm("Are you sure you want to delete this Contact ");
			if(result){
				var contactName =document.getElementById('currentContactName'+index).value;
				var contactEmail = document.getElementById('currentContactEmail'+index).value;

				//--simultaneously updating the contactname and email fields in parent page---
				if(null != parent.parent.updateContactInfoAfterDeletion
				  && undefined != parent.parent.updateContactInfoAfterDeletion){
					parent.parent.updateContactInfoAfterDeletion(contactName,contactEmail);
		   		}
		   		//--- updating the CARRIER contactname and CARRIER email fields in parent page---
		   		if(null != parent.parent.updateCarrierContactAfterDeletion
				  && undefined != parent.parent.updateCarrierContactAfterDeletion){
					parent.parent.updateCarrierContactAfterDeletion(contactName,contactEmail);
		   		}
				document.customerAddressForm.submit();
			}
	}
	</script>
	<%@include file="../includes/resources.jsp" %>
	</head>

	<body class="whitebackgrnd" onload="setFocus()">
	<div id="cover" style="width: 100% ;height: 1000px;"></div>
 <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
		<p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
		<form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
			<input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
		</form>
</div>
<!--DESIGN FOR NEW ALERT BOX ---->
<div id="AlertBox" class="alert">
	<p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
	<p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

	</p>
	<form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
	<input type="button"  class="buttonStyleForAlert" value="OK"
	onclick="document.getElementById('AlertBox').style.display='none';
	grayOut(false,'');">
	</form>
</div>

<div id="ConfirmBox" class="alert">
	<p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
	<p id="innerText1" class="containerForAlert" style="width: 100%;padding-left: 3px;">

	</p>
	<form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
	<input type="button"  class="buttonStyleForAlert" value="OK"
	onclick="yes()">
	<input type="button"  class="buttonStyleForAlert" value="Cancel"
	onclick="No()">
	</form>
</div>
<html:form action="/customerAddress" scope="request">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	 <tr><td>
			<table width="100%" border="0" cellpadding="1" cellspacing="0">
			    <tr class="tableHeadingNew">
                                <td colspan="4" align="right">Contact Search
                                        <input type="button"  value="Add Contact" class="buttonStyleNew" onclick="gotoTradingPartnerContactConfig('<%=custNo%>','<%=custName%>')"  style="width:85px;">
						</td>
				</tr>
				<tr class="textlabelsBold">

							<td>Customer Name                                                     
							 <input type="text" name="custName"  id="custName" value="${customerAcctName}" class="textlabelsBoldForTextBox">
                                                        <input id="custname_check"   type="hidden"   value="" />
                                                        <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("custName","custname_choices","custNo","custname_check",
                                                            "<%=path%>/actions/tradingPartner.jsp?tabName=CONTACTPOPUP&from=0","","");
                                                        </script></td><td>
                                                         Customer Number
                                                         <input type="text" name="custNo"  id="custNo" value="${customerAcctNumber}"  readonly="true" class="BackgrndColorForTextBox"/>
                                                                </td>
                                                        <td>Contact Name
                                                            <SPAN style="padding-left:30px;"><html:text property="contactName"  value="${customerAddressForm.contactName}" styleClass="textlabelsBoldForTextBox"></html:text></SPAN>
							</td>
<%--						<td>Contact Number</td>--%>
<%--						<td> <html:text property="custNo"></html:text></td>--%>
				</tr>
                                <tr><td colspan="4" align="center"><input type="button" value="Search" class="buttonStyleNew"
							     onclick="getSearchResult()" style="width:50px;" ></td></tr>
			</table>
	 </td></tr>
	 <tr style="padding-top:10px;">
		<td>
			<table width="100%" border="0" cellpadding="1" cellspacing="0">
			   <tr class="tableHeadingNew">
			   <td>
			   		<%if(addressList!=null && addressList.size()>0){ %>
			   		  <input type="button" value="Submit" class="buttonStyleNew" onclick="selectedContacts('<%=custNo%>','<%=custName%>')" style="width:45px;"/>
			   		<%}%>
			   		List of Contacts
			   </td>
			   </tr>
			   <tr><td>
<%
if(addressList!=null && addressList.size()>0)
 {
 int i=0;

%>
<div id="divtablesty1" style="height:80%;">
    <display:table name="<%=addressList%>" class="displaytagstyleNew" pagesize="20"  style="width:100%" id="contactTable" sort="list" >

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
			<display:setProperty name="paging.banner.item_name" value="Contacts"/>
  			<display:setProperty name="paging.banner.items_name" value="Contacts"/>
  			<%
	  			 String accountName="";
	  			String contactName="";
                                String firstName="";
                                String lastName="";
	  			String accountNo="";
	  			String email="";
	  			CustomerContact customerContact=(CustomerContact)addressList.get(i);
                                firstName = customerContact.getFirstName();
                                lastName = customerContact.getLastName();
	  			contactName=firstName+" "+lastName;

	  			accountNo=customerContact.getAccountNo();
                                if(null!=accountNo){
                                    tradingPartner=tradingPartnerBC.findTradingPartnerById(accountNo);
                                accountName=tradingPartner.getAccountName();
                                }
	  			//accountName=customerContact.getAccountName();
	  			email=customerContact.getEmail();
	  			String index=Integer.toString(i);
                                accountName=(accountName!=null)?accountName.replace("&","amp"):accountName;
	  		    link=editPath+"?paramId="+i+"&button="+button+"&accountNo="+accountNo+"&acctName="+accountName;
  			%>
  	<%
  	   // Check box can be seen only when more than 1 contact is available
  	if(addressList!=null && addressList.size()>0){%>
	    <display:column title="">
	    	<html:checkbox property="selectedCheckBox" value="<%=index%>"></html:checkbox>
	    </display:column>
    <%}%>
  	<%--<display:column title="CustomerName" property="acctName"></display:column>--%>
  	<display:column title="CustomerNo">
  		<span class="hotspot" onmouseover="tooltip.show('<strong><%=accountName%></strong>','20',event);"
		 onmouseout="tooltip.hide();" style="color:black;"><%=accountNo%></span>
	</display:column>
        <display:column title="ContactName" style="text-transform: uppercase"><%=contactName%></display:column>
  	<%--<%
  	  //a href link disabled when more than 1 contacts is available

  	 /*if(addressList!=null && addressList.size()>1){%>
	   <display:column title="ContactName"><%=contactName%></display:column>
	<%}else{%>
	  <display:column title="ContactName"><a href="<%=link%>"><%=contactName%></a></display:column>
	<%}*/%> --%>
	<display:column title="Position" property="position" style="text-transform: uppercase"></display:column>
	<display:column title="Email" property="email" style="text-transform: uppercase"></display:column>
	<display:column title="Phone" property="phone" ></display:column>
	<display:column title="Fax"  property="fax"></display:column>
         <display:column title="A" property="codea.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="B" property="codeb.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="C" property="codec.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="D" property="coded.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="E" property="codee.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="F" property="codef.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="G" property="codeg.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="H" property="codeh.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="I" property="codei.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="J" property="codej.code" style="width:30px;text-transform: uppercase"/>
       <display:column title="K" property="codek.code" style="width:30px;text-transform: uppercase"/>
	<display:column title="Actions">
		<span class="hotspot" onmouseover="tooltip.show('Edit',null,event);" onmouseout="tooltip.hide();">
		   <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editContactInfo('${contactTable.id}','<%=custNo%>','<%=custName%>')" /></span>
		<span class="hotspot" onmouseover="tooltip.show('Delete',null,event);" onmouseout="tooltip.hide();">
		   <img src="<%=path%>/img/icons/delete.gif" border="0"  onclick="deleteContactInfo('${contactTable.id}','<%=custNo%>','<%=custName%>','<%=i %>')" /></span>
	</display:column>
	<display:column  style="visibility:hidden;width:5px;">
	     <input type="text" value="<%=contactName%>" id="currentContactName<%=i%>" style="width:5px;"></display:column>
	<display:column  style="visibility:hidden;width:5px;">
	     <input type="text" value="<%=email%>" id="currentContactEmail<%=i%>" style="width:5px;"></display:column>
<%i++;%>
</display:table>
<%}%>
  </div>
  </td></tr>
</table>
</td>
</tr>
    <c:out value="${contactTable.id}"></c:out>
</table>
	<html:hidden property="buttonValue"/>
	<html:hidden property="custNameTemp"/>

	<html:hidden property="recordId"/>
	<html:hidden property="buttonParameter" value="<%=button%>"/>
	</html:form>
  </body>

</html>

