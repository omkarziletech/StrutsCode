<%@ page language="java" pageEncoding="ISO-8859-1"
	import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerContact,com.gp.cong.logisoft.domain.GeneralInformation,com.gp.cong.logisoft.domain.CustomerAccounting,com.gp.cong.logisoft.domain.GenericCode,java.util.*,com.gp.cong.logisoft.beans.customerBean,com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	DBUtil dbUtil = new DBUtil();
	CustomerAccounting accounting = new CustomerAccounting();
	GeneralInformation generalInformation = new GeneralInformation();
	CustomerContact customerCont = new CustomerContact();

	String arcode = "0";
	List list = new ArrayList();//----------LISTS USED FOR DISPLAY COLUMNS------
	List list1 = new ArrayList();
	List cList = new ArrayList();

	accounting.setHoldList("on");
	accounting.setSuspendCredit("on");
	accounting.setLegal("on");
	accounting.setExtendCredit("on");
	generalInformation.setInsure("on");
	accounting.setIncludeagent("on");
	accounting.setCreditbalance("on");
	accounting.setCreditinvoice("on");

	String creditRate = "0";
	String creditStatus = "0";
	String statements = "0";
	customerBean customerbean = null;

	if (session.getAttribute("customerbean") != null) {
		customerbean = (customerBean) session
		.getAttribute("customerbean");
		accounting.setExtendCredit(customerbean.getExtendCredit());
		accounting.setHoldList(customerbean.getHoldList());
		accounting.setSuspendCredit(customerbean.getSuspendCredit());
		accounting.setLegal(customerbean.getLegal());
		accounting.setIncludeagent(customerbean.getIncludeagent());
		accounting.setCreditbalance(customerbean.getCreditbalance());
		accounting.setCreditinvoice(customerbean.getCreditinvoice());
		generalInformation.setInsure(customerbean.getInsure());
	}	
	request.setAttribute("accounting", accounting);
	//request.setAttribute("countrylist",dbUtil.getGenericCodeList(11,"yes","Select Country"));
	String countryId = "";
	String cityId = "";
	String state = "";
	String paycountryId = "";
	String paycityId = "";
	String paystate = "";

	String creditLimit = "";
	String modify = "";
	String phone = "";
	String fax = "";
	String schedule = "";

	/*if(session.getAttribute("CPlist")!=null)//---------LIST TO DISPLAY CONTACT  CONFIG INFORMATION-------
	 {
	 accounting=(CustomerAccounting)session.getAttribute("CPlist");
	 cList.add(accounting);
	 }*/

	if (session.getAttribute("accounting") != null) {
		accounting = (CustomerAccounting) session
		.getAttribute("accounting");
		cList.add(accounting);
		if (accounting.getAddress1() != null) {
			list.add(accounting);//-------------LIST TO DISPLAY ADDRESS--------
		}

		if (accounting.getPayAddress1() != null) {
			list1.add(accounting);//----------LIST TO DISPLAY ADDRESS FROM MASTER---------
		}

		if (accounting.getArPhone() != null) {
			phone = dbUtil.appendstring(accounting.getArPhone());
		}
		if (accounting.getArFax() != null) {
			fax = dbUtil.appendstring(accounting.getArFax());
		}
		if (accounting.getCreditLimit() != null) {
			creditLimit = String.valueOf(accounting.getCreditLimit());
		}
		if (accounting != null && accounting.getArcode() != null
		&& accounting.getArcode().getId() != null) {
			arcode = accounting.getArcode().getId().toString();
		}
		if (accounting.getCreditRate() != null
		&& accounting.getCreditRate().getId() != null) {
			creditRate = accounting.getCreditRate().getId().toString();
		}
		if (accounting.getCreditStatus() != null
		&& accounting.getCreditStatus().getId() != null) {
			creditStatus = accounting.getCreditStatus().getId()
			.toString();

		}
		if (accounting != null && accounting.getStatements() != null
		&& accounting.getStatements().getId() != null) {
			statements = accounting.getStatements().getId().toString();
		}
	}
	if (accounting != null && accounting.getCuntry() != null
			&& accounting.getCuntry().getCodedesc() != null) {
		countryId = accounting.getCuntry().getCodedesc();
	}
	if (accounting != null && accounting.getCity1() != null
			&& accounting.getCity1().getId() != null) {
		cityId = accounting.getCity1().getId().toString();
	}
	if (accounting != null && accounting.getState() != null) {
		state = accounting.getState();
	}
	if (accounting != null && accounting.getCity2() != null) {
		cityId = accounting.getCity2();
	}
	if (accounting != null && accounting.getPayCuntry() != null
			&& accounting.getPayCuntry().getCodedesc() != null) {
		paycountryId = accounting.getPayCuntry().getCodedesc();
	}
	if (accounting != null && accounting.getPayCity1() != null
			&& accounting.getPayCity1().getId() != null) {
		paycityId = accounting.getPayCity1().getId().toString();
	}
	if (accounting != null && accounting.getPayState() != null) {
		paystate = accounting.getPayState();
	}
	if (accounting != null && accounting.getPaycity2() != null) {
		paycityId = accounting.getPaycity2();
	}
	if (accounting != null && accounting.getSchedulestmt() != null) {
		schedule = accounting.getSchedulestmt();
	}

	modify = (String) session.getAttribute("modifyforcustomer");
	if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

	}
	request.setAttribute("creditRateList", dbUtil.getGenericCodeList(
			29, "yes", "Select Credit Terms"));
	request.setAttribute("statements", dbUtil.getGenericCodeList(30,
			"yes", "Select Statements"));
	//request.setAttribute("arcodelist",dbUtil.getUserList(24,"no","Select AR Contact Code"));
	request.setAttribute("arcodelist", dbUtil.getGenericCodeList(44,
			"Yes", "Select Collector Code"));
	request.setAttribute("creditStatusList", dbUtil.getGenericCodeList(
			43, "yes", "Select Credit Status"));
	request.setAttribute("Schedulelist", dbUtil.getScheduleList());

	User user = new User();
	if (session.getAttribute("loginuser") != null) {
		user = (User) session.getAttribute("loginuser");
	}
	boolean hold = true;
	if (user.getLoginName().equals("Mitch")
			|| user.getLoginName().equals("Rick")) {
		hold = false;
	}
	if (request.getAttribute("openwindow") != null) {
%>
<script type="text/javascript">
<!--
  //window.open("<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button=editchecked","", "width=700,height=400,scrollbars=yes");
  //window.moveTo(200,180);
//-->
</script>
<%
	}
	if (request.getParameter("close") != null) {
%>
<script type="text/javascript">
  //window.open("<%=path%>/jsps/Tradingpartnermaintainance/contactPopup.jsp?","width=700,height=400");
  //window.moveTo(200,180);
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

		<script language="javascript" src="<%=path%>/js/isValidEmail.js"></script>
		<script type="text/javascript">
 function selectcity()
    {
    if(document.editAccountingForm.country.value=="0")
    {
    	alert("Please enter the country");
   	    return;
    }
    document.editAccountingForm.buttonValue.value="selectcity";
    document.editAccountingForm.submit();
    }
    function selectstate()
    {
    if(document.editAccountingForm.city.value=="0")
    {
    	alert("Please enter the city");
    	return;
    }
     document.editAccountingForm.buttonValue.value="selectstate";
    document.editAccountingForm.submit();
    }
    function selectpaycity()
    {
    if(document.editAccountingForm.ffcountry.value=="0")
    {
    	alert("Please enter the country");
   	    return;
    }
    document.editAccountingForm.buttonValue.value="selectcity";
    document.editAccountingForm.submit();
    }
    function selectpaystate()
    {
    if(document.editAccountingForm.ffcity.value=="0")
    {
    	alert("Please enter the city");
    	return;
    }
     document.editAccountingForm.buttonValue.value="selectstate";
    document.editAccountingForm.submit();
    }
    
     function confirmnote()
	{
		document.editAccountingForm.buttonValue.value="note";
    	document.editAccountingForm.submit();
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
	 		if(input[i].id != "buttonValue" && input[i].id != "add" && input[i].id != "note")
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
  	 	document.getElementById("add2").style.visibility = 'hidden';
  	 	document.getElementById("add1").style.visibility = 'hidden';
  	 }
   }
     function popup1(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
mywindow.moveTo(200,180);

          document.editAccountingForm.submit();
return false;
}
 function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
	
	function zipcode(obj)
{
if(document.editAccountingForm.country.value=="" && document.editAccountingForm.country.value!="UNITED STATES")
{
  
  		if((document.editAccountingForm.zipCode.value.length>5) || IsNumeric(document.editAccountingForm.zipCode.value.replace(/ /g,''))==false)
 		{  
   alert("please enter the only 5 digits and numerics only");
  document.editAccountingForm.zipCode.value="";
  document.editAccountingForm.zipCode.focus(); 
  			}
			}
 else{
 getzip(obj);	
 }
 }
 
 function ffzipCode1(obj)
{
if(document.editAccountingForm.ffcountry.value=="" && document.editAccountingForm.ffcountry.value!="UNITED STATES")
{
  
  		if((document.editAccountingForm.ffzipCode.value.length>5) || IsNumeric(document.editAccountingForm.ffzipCode.value.replace(/ /g,''))==false)
 		{  
   alert("please enter the only 5 digits and numerics only");
  document.editAccountingForm.ffzipCode.value="";
  document.editAccountingForm.ffzipCode.focus(); 
  			}
			}
 else{
 getzip(obj);	
 }
 }
 
 function setcreditterms()
  {
  (document.editAccountingForm.creditLimit.value == 0)?document.editAccountingForm.creditRate.selectedIndex=1:document.editAccountingForm.creditLimit.value > 0?document.editAccountingForm.creditRate.selectedIndex=3:document.editAccountingForm.creditRate.selectedIndex=0;
  }
  
   function setCreditStatus1()
  {
  document.editAccountingForm.suspendCredit.checked? document.editAccountingForm.creditStatus.selectedIndex=4: document.editAccountingForm.legal.checked?document.editAccountingForm.creditStatus.selectedIndex=5:document.editAccountingForm.creditStatus.selectedIndex=0;
  }
  
  function setCreditStatus2()
  {
  document.editAccountingForm.legal.checked? document.editAccountingForm.creditStatus.selectedIndex=5: document.editAccountingForm.suspendCredit.checked?document.editAccountingForm.creditStatus.selectedIndex=4:document.editAccountingForm.creditStatus.selectedIndex=0;
  }
  
  function addressMaster1()
   {
  
  if(document.editAccountingForm.addressMaster.checked)
	   {
	   alert();
	   
	   GB_show('Address', '<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'editcheckBoxValue',250,600);
	     //document.accountingForm.buttonValue.value = "checked";
	     //document.accountingForm.submit();
	   }
          
   }
  
  function popup()
    { 
        document.editAccountingForm.submit();
        window.open('<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'editeconoaddress',"","width=700,height=400");
    }
  
  function popup2()
    { 
        document.editAccountingForm.submit();
        window.open('<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'editffaddress',"","width=700,height=400");
    }
    
  function popup1()
    { 
      document.editAccountingForm.submit();
      window.open('<%=path%>/jsps/Tradingpartnermaintainance/contactPopup.jsp?button='+'editcont',"","width=700,height=400");
 
    }  
  </script>
		<%@include file="../includes/resources.jsp"%>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/editAccounting" name="editAccountingForm"
			type="com.gp.cong.logisoft.struts.form.EditAccountingForm" scope="request">
			<table>
				<tr>
					<td width="100%">
						&nbsp;
					</td>
					<td align="right">
						<input type="button" class="buttonStyleNew" value="Notes" id="note"
							onclick="confirmnote()" />
					</td>
				</tr>
			</table>

			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew">
				<tr class="tableHeadingNew" colspan="2">
					Account Receivable
				</tr>
				<tr>
					<td valign="top" width="50%">
						<table width="100%" border="0" cellspacing="0" cellpadding="2">
							<tr>
								<td class="style2">
									&nbsp;
									<bean:message key="form.customerForm.statements" />
								</td>
								<td class="style2">
									Schedule For Statements
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
									<html:select property="statements" styleClass="selectboxstyle"
										value="<%=statements%>">
										<html:optionsCollection name="statements" />
									</html:select>
								</td>
								<td>
									<html:select property="schedulestmt"
										styleClass="selectboxstyle" value="<%=schedule%>">
										<html:optionsCollection name="Schedulelist" />
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="style2">
									&nbsp;Credit Status
								</td>
								<td class="style2">
									Collector Code
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
									<html:select property="creditStatus"
										styleClass="selectboxstyle" value="<%=creditStatus%>">
										<html:optionsCollection name="creditStatusList" />
									</html:select>
								</td>
								<td>
									<html:select property="arCode" styleClass="dropdownboxStyle"
										value="<%=arcode%>">
										<html:optionsCollection name="arcodelist" />
									</html:select>
								</td>
							</tr>
							<tr>
								<td>
									<span class="style2">&nbsp;Account Receivable<br />&nbsp;&nbsp;&nbsp;Comment
										Line</span>
								</td>
							</tr>
							<tr>
								<td class="style2">
									&nbsp;
									<html:textarea property="acctReceive"
										value="<%=accounting.getComment()%>" cols="15" rows="2"
										onkeyup="toUppercase(this)" />
								</td>
							</tr>
						</table>
					</td>

					<td>
						&nbsp;
					</td>

					<td valign="top" width="50%">
						<table border="0" cellspacing="1">
							<tr>
								<td class="style2">
									<bean:message key="form.customerForm.creditlimit" />
								</td>
								<td class="style2">
									&nbsp;Credit Terms
								</td>
								<td class="style2">
									&nbsp;Include Agents
								</td>
							</tr>
							<tr>
								<td class="style2">
									<html:text property="creditLimit" value="<%=creditLimit%>"
										maxlength="5" onblur="setcreditterms()"
										onkeyup="toUppercase(this)"
										onkeypress="return checkIts(event)" />
								</td>
								<td>
									&nbsp;
									<html:select property="creditRate" styleClass="selectboxstyle"
										value="<%=creditRate%>">
										<html:optionsCollection name="creditRateList" />
									</html:select>
								</td>
								<td align="center">
									&nbsp;
									<html:checkbox property="includeagent" name="accounting"></html:checkbox>
								</td>
							</tr>
						</table>
						<table width="20%" border="0" style="padding:right:20px;">
							<tr class="tableHeadingNew" colspan="2" align="center">
								&nbsp;Send Statements With
							</tr>
							<tr>
								<td class="style2" align="left">
									Credit balance
								</td>
								<td>
									<html:checkbox property="creditbalance" name="accounting"></html:checkbox>
								</td>
								<td class="style2" align="right">
									&nbsp;&nbsp;Credit Invoice
								</td>
								<td>
									<html:checkbox property="creditinvoice" name="accounting"></html:checkbox>
								</td>
							</tr>
						</table>


					</td>
				</tr>
			</table>

			<table>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				class="tableBorderNew">
				<tr class="tableHeadingNew" colspan="2">
					Contact Details
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="right">
									<input type="button" class="buttonStyleNew" value="Add" id="add"
										onclick="return GB_show('Contact', '<%=path%>/jsps/Tradingpartnermaintainance/contactPopup.jsp?button='+'editcont',150,600)" />
								</td>
							</tr>

							<%
							int j = 0;
							%>
							<display:table name="<%=cList%>" pagesize="<%=pageSize%>"
								class="displaytagstyle" id="customertable" sort="list">
								<display:setProperty name="paging.banner.some_items_found">
									<span class="pagebanner"> <font color="blue">{0}</font>
										Customer details displayed,For more Customers click on page
										numbers. <br> </span>
								</display:setProperty>
								<display:setProperty name="paging.banner.one_item_found">
									<span class="pagebanner"> One {0} displayed. Page Number
									</span>
								</display:setProperty>
								<display:setProperty name="paging.banner.all_items_found">
									<span class="pagebanner"> {0} {1} Displayed, Page Number

									</span>
								</display:setProperty>

								<display:setProperty name="basic.msg.empty_list">
									<span class="pagebanner"> No Records Found. </span>
								</display:setProperty>
								<display:column title="Contact Name" property="contact"></display:column>
								<display:column title="Phone" property="arPhone"></display:column>
								<display:column title="Fax" property="arFax"></display:column>
								<display:column title="Email" property="acctRecEmail"></display:column>
								<%
								j++;
								%>
							</display:table>
						</table>
					</td>
				</tr>
			</table>

			<table>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>

			<table width="100%" class="tableBorderNew">
				<tr class="tableHeadingNew" colspan="2">
					Customer Invoice Address
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">

							<tr>
								<td align="right">
									<input type="button" class="buttonStyleNew" value="Add" id="add1"
										onclick="return GB_show('Address', '<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'editeconoaddress',250,600)" />
								</td>
							</tr>

							<%
							int i = 0;
							%>
							<display:table name="<%=list%>" pagesize="<%=pageSize%>"
								class="displaytagstyle" id="customertable" sort="list">
								<display:setProperty name="paging.banner.some_items_found">
									<span class="pagebanner"> <font color="blue">{0}</font>
										Customer details displayed,For more Customers click on page
										numbers. <br> </span>
								</display:setProperty>
								<display:setProperty name="paging.banner.one_item_found">
									<span class="pagebanner"> One {0} displayed. Page Number
									</span>
								</display:setProperty>
								<display:setProperty name="paging.banner.all_items_found">
									<span class="pagebanner"> {0} {1} Displayed, Page Number

									</span>
								</display:setProperty>

								<display:setProperty name="basic.msg.empty_list">
									<span class="pagebanner"> No Records Found. </span>
								</display:setProperty>
								<display:column title="C/O Name" property="company"></display:column>
								<display:column property="address1" title="Address1" />
								<display:column title="City" property="city2"></display:column>
								<display:column title="State" property="state"></display:column>
								<display:column title="Country">
									<%=paycountryId%>
								</display:column>
								<display:column title="Zip" property="zip"></display:column>
								<%
								i++;
								%>
							</display:table>
						</table>
					</td>
				</tr>
			</table>

			<table>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>

			<table width="100%" class="tableBorderNew">
				<tr class="tableHeadingNew" colspan="2">
					F.F Payment Address
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							class="style2">
							<tr class="style2">
								<td width="90%" align="right">
									Use From Master
									<html:checkbox property="addressMaster"
										onclick="addressMaster1()" name="accounting"></html:checkbox>
								</td>
								<td align="right">
									<input type="button" class="buttonStyleNew" value="Add" id="add2"
										onclick="return GB_show('Address', '<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'editffaddress',250,600)" />

								</td>
							</tr>


							<%
							int k = 0;
							%>
							<display:table name="<%=list1%>" pagesize="<%=pageSize%>"
								class="displaytagstyle" id="customertable" sort="list">
								<display:setProperty name="paging.banner.some_items_found">
									<span class="pagebanner"> <font color="blue">{0}</font>
										Customer details displayed,For more Customers click on page
										numbers. <br> </span>
								</display:setProperty>
								<display:setProperty name="paging.banner.one_item_found">
									<span class="pagebanner"> One {0} displayed. Page Number
									</span>
								</display:setProperty>
								<display:setProperty name="paging.banner.all_items_found">
									<span class="pagebanner"> {0} {1} Displayed, Page Number

									</span>
								</display:setProperty>

								<display:setProperty name="basic.msg.empty_list">
									<span class="pagebanner"> No Records Found. </span>
								</display:setProperty>

								<display:column title="C/O Name" property="payCompany"></display:column>
								<display:column title="Address1" property="payAddress1" />
								<display:column title="ffcity" property="paycity2"></display:column>
								<display:column title="ffstate" property="payState"></display:column>
								<display:column title="ffcountry">
									<%=paycountryId%>
								</display:column>
								<display:column title="Zip" property="payZip"></display:column>
								<%
								k++;
								%>
							</display:table>
						</table>
					</td>
				</tr>
			</table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

