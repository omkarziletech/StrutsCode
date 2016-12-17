<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ page import="com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.domain.*,com.gp.cvst.logisoft.beans.*" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
	    String path = request.getContextPath();
	    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	    if (path == null) {
		path = "../..";
	    }
	    GenericCode genericCodeObj = new GenericCode();

	    CustAddress custAddress = new CustAddress();

	    String CustomerName = "";
	    String CustomerNo = "";

	    if (session.getAttribute("batch") != null) {
		custAddress = (CustAddress) session.getAttribute("batch");
		if (custAddress.getAcctName() != null) {
		    CustomerName = custAddress.getAcctName();
		}
		if (custAddress.getAcctNo() != null) {
		    CustomerNo = custAddress.getAcctNo();
		}
	    }
	    String Ccode = "";
	    String CodeList = "";
	    if (session.getAttribute("chargecode1") != null) {
		genericCodeObj = (GenericCode) session.getAttribute("chargecode1");
		if (genericCodeObj.getCodedesc() != null) {
		    Ccode = genericCodeObj.getCode();
		}
	    }

	    if (session.getAttribute("genericobjs") != null) {
		genericCodeObj = (GenericCode) session.getAttribute("genericobjs");
		if (genericCodeObj.getCodedesc() != null) {
		    CodeList = genericCodeObj.getCode();
		}

	    }

//  session.setAttribute("invoiceList",invoiceList);

	    List invoiceList = null;
	    if (session.getAttribute("invoiceList") != null) {
		invoiceList = (List) session.getAttribute("invoiceList");

	    }

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
	<title>AR Invoice</title>
	<%@include file="../includes/baseResources.jsp" %>
	<c:set var="accessMode" value="1"/>
	<c:set var="canEdit" value="true"/>
	<c:if test="${param.accessMode==0}">
	    <c:set var="accessMode" value="0"/>
	    <c:set var="canEdit" value="false"/>
	</c:if>
    </head>
    <body class="whitebackgrnd" onload="load()">
	<html:form action="/arInvoice?accessMode=${accessMode}"  name="arInvoiceform" type="com.gp.cvst.logisoft.struts.form.ARInvoiceForm" scope="request">
	    <html:hidden property="buttonValue"/>
	    <table width="805" height="53" border="0" cellpadding="0" cellspacing="0">
		<tr class="textlabels">
		    <td width="805" align="left" class="headerbluelarge">&nbsp;</td>
		</tr>
		<tr class="textlabels">
		    <td align="left" class="headerbluelarge">AR Invoice </td>
		</tr>
		<tr class="textlabels">
		    <td>&nbsp;</td>
		</tr>
		<tr>
		    <td></td></tr>
	    </table>
	    <table width="100%"  border="0" cellpadding="0" cellspacing="0">
		<tr>
		    <td height="12"  class="headerbluesmall">&nbsp;&nbsp;</td>
		</tr>
	    </table>
	    <table width="831" height="139"  border="0" cellpadding="0" cellspacing="0">
		<tr>
		    <td width="93">&nbsp;</td>
		</tr>
		<tr class="textlabels">
		    <td width="93">Customer</td>
		    <td width="129"><html:text property="cusName" value="<%=CustomerName%>"/><img src="<%=path%>/img/search1.gif" border="0" onclick="return popup('<%=path%>/jsps/AccountsRecievable/customerSearch.jsp?button='+'searchArInvoice','windows')"/>    </td>
		    <td width="87">Customer Type </td>
		    <td width="129">&nbsp; &nbsp; Forward<html:radio property="arCustomertype" value="F"/> &nbsp;&nbsp; Shipper<html:radio property="arCustomertype" value="S"/>
		    </td>
		    <td width="59">Due Date </td>
		    <td width="96"> <html:text property="dueDate" styleId="txtcal"/></td>
		    <td width="212">
			<c:if test="${canEdit}">
			    <img src="<%=path%>/img/CalendarIco.gif" border="0" id="cal" alt="cal" onmousedown="insertDateFromCalendar(this.id,0);"/>
			</c:if>
		    </td>
		</tr>
		<tr class="textlabels">
		    <td height="27">Customer Number </td>
		    <td><html:text property="cusNumber" value="<%=CustomerNo%>"/></td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		</tr>
		<tr class="textlabels">
		    <td height="30">Invoice Number </td>
		    <td><html:text property="invoiceNumber"/></td>
		    <td>BL/DR Number </td>
		    <td> <html:text property="bl_drNumber"/></td>
		    <td>&nbsp;</td>
		    <td>
			<c:if test="${canEdit}">
			    <img src="<%=path%>/img/AddInvoice.gif" border="0" onclick="addinvoice()"/>
			</c:if>
		    </td>
		    <td><a href="#" onclick="toggleTable('hiddentablesty5',1)"/></td>
		</tr>

		<tr>
		    <td>&nbsp;</td>
		</tr>
	    </table>
	    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
		    <td  colspan ="8" height="15"  class="headerbluesmall">&nbsp;</td>
		</tr>
	    </table>

	    <table width="815">
		<tr class="textlabels">
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td colspan="2">&nbsp;</td>
		</tr>
		<tr class="textlabels">
		    <td width="88">Charge Code </td>
		    <td width="133"><html:text property="chargeCode" value="<%=Ccode%>"/><img src="<%=path%>/img/search1.gif" border="0" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchChargeCode.jsp?button='+'searchInvoice','windows') "/> </td>
		    <td>Amount</td>
		    <td><html:text property="amount"/></td>
		    <td>
			<c:if test="${canEdit}">
			    <img src="<%=path%>/img/AddCharge.gif" border="0" onclick="addcharge()"/>
			</c:if>
		    </td>
		</tr>
		<tr class="textlabels">
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td width="180">&nbsp;</td>
		    <td width="316">&nbsp;</td>
		</tr>
		<tr>
		    <td class="textlabels">&nbsp;</td>
		    <td>&nbsp;</td>
		    <td class="textlabels">&nbsp;</td>
		    <td colspan="2"><img src="<%=path%>/img/go1.gif" border="0" onclick="listsearch()"/></td>
		</tr>
	    </table>

	    <table width="100%"  border="0" cellpadding="0" cellspacing="0">
		<tr>
		    <td height="12"  class="headerbluesmall">&nbsp;&nbsp;</td>
		</tr>
	    </table>
	    <%

			int i = 0;
			if (invoiceList != null && invoiceList.size()> 0) {
	    %>
	    <div id="ARinquiryListDiv">
		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:360px;">
		    <display:table name="<%=invoiceList%>" pagesize="200" class="displaytagstyle" style="width:60%" id="accountrecievable" sort="list">


			<display:setProperty name="paging.banner.some_items_found">
			    <span class="pagebanner">
				<font color="blue">{0}</font> Account details displayed,For more code click on page numbers.
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
			<display:setProperty name="paging.banner.placement" value="bottom"/>
			<display:setProperty name="paging.banner.item_name" value="Transaction"/>
			<display:setProperty name="paging.banner.items_name" value="Transactions"/>

			<display:column   property="custName" title="Customer Name" sortable="true" headerClass="sortable"></display:column>
			<display:column  property="custNumber" title="Customer number" sortable="true" headerClass="sortable"></display:column>
			<display:column property = "customerType"title="Customer Type" sortable="true" headerClass="sortable"></display:column>
			<display:column property="invoiceNumber"  title="Invoice Number" sortable="true" headerClass="sortable"></display:column>
			<display:column  property="blNumber" title="Bill of Ladding"  sortable="true" headerClass="sortable"></display:column>
			<display:column  property="dueDate" title="Due Date"  sortable="true" headerClass="sortable"></display:column>
			<display:column  property="chargecode" title="Charge Code"  sortable="true" headerClass="sortable" style="color:green;"> </display:column>
			<display:column  property="amount" title="Amount"  sortable="true" headerClass="sortable" style="color:red;"> </display:column>



			<%i++;%>
		    </display:table>
		</div>
	    </div>
	    <%}%>

	</html:form>
    </body>
    <script language="javascript" type="text/javascript">
	function popup(mylink, windowname)
	{
  
	    if (!window.focus)return true;
	    var href;
	    if (typeof(mylink) == 'string')
		href=mylink;
  
	    else
		href=mylink.href;
	    window.open(href, windowname, 'width=500,height=200,scrollbars=yes');
 
	    return false;
	}
	function popup1(mylink, windowname)
	{
	    if (!window.focus)return true;
	    var href;
	    if (typeof(mylink) == 'string')
		href=mylink;
    
	    else
		href=mylink.href;
	    mywindow = window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
 
	    mywindow.moveTo(200,180);
          
	    return false;
	}

	//for second popup

	function chargecode1(mylink, windowname)
	{
	    if (!window.focus)return true;
	    var href;
	    if (typeof(mylink) == 'string')
		href=mylink;
 
	    else
		href=mylink.href;
	    window.open(href, windowname, 'width=500,height=310,scrollbars=yes');
	    return false;
	}
	function popup2(mylink, windowname)
	{
	    if (!window.focus)return true;
	    var href;
	    if (typeof(mylink) == 'string')
		href=mylink;
	    else
		href=mylink.href;
	    window.open(href, windowname, 'width=500,height=350,scrollbars=yes');
	    return false;
	}
	function confirmdelete()
	{
	    var result = confirm('Are you sure you want to delete Warehouse Details?');
	    return result
	}
	function confirmdelete1()
	{
	    var result = confirm('This Terminal is associated to user. So cannot delete this Terminal');
	    return result
	}
		
	function load()
	{
	    if(document.arInvoiceform.cusName.value=="")
	    {
		document.arInvoiceform.chargeCode.value="";
		document.arInvoiceform.chargecode1.value="";
	    }
		 
	}
	function addinvoice()
	{
	    document.arInvoiceform.buttonValue.value="invoice";
	    alert(document.arInvoiceform.buttonValue.value);
	    document.arInvoiceform.submit();
	}
	function addcharge()
	{
	    document.arInvoiceform.buttonValue.value="charge";
	    alert(document.arInvoiceform.buttonValue.value);
	    document.arInvoiceform.submit();
	}
	function listsearch(){
	    document.arInvoiceform.buttonValue.value="go";
	    alert(document.arInvoiceform.buttonValue.value);
	    document.arInvoiceform.submit();
	}
		
    </script>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

