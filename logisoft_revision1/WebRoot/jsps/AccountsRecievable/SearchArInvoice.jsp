<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %><%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
	<title>Search AR Invoice</title>
	<%
	       String path = request.getContextPath();
	       String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
	<%@include file="../includes/baseResources.jsp" %>
	<%@include file="../includes/resources.jsp" %>


	<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
	<script type="text/javascript">
	    dojo.hostenv.setModulePrefix('utils', 'utils');
	    dojo.widget.manager.registerWidgetPackage('utils');
	    dojo.require("utils.AutoComplete");
	</script>
	<c:set var="accessMode" value="1"/>
	<c:set var="canEdit" value="true"/>
	<c:if test="${param.accessMode==0}">
	    <c:set var="accessMode" value="0"/>
	    <c:set var="canEdit" value="false"/>
	</c:if>
    </head>

    <body class="whitebackgrnd">
	<html:form action="/arInvoice?accessMode=${accessMode}"  name="arInvoiceform" type="com.gp.cvst.logisoft.struts.form.ARInvoiceForm" scope="request">
	    <table width="100%" border="0" cellpadding="5" cellspacing="5" class="tableBorderNew" style="padding-left:10px">
		<html:hidden property="buttonValue" value="" />
		<html:hidden property="arInvoiceChargesId"/>
		<tr class="tableHeadingNew"> Search AR Invoice</tr>
		<tr class="textlabels">
		    <td width="60">Customer</td>
		    <td>
			<input name="cusName" id="cusName" value="${arInvoiceform.cusName}"   />
			<dojo:autoComplete formId="arInvoiceform" textboxId="cusName" action="<%=path%>/actions/getAccountName.jsp?tabName=AR_INVOICE&from=0" />
		    </td>
		    <td align="left" width="100">Account Number</td>
		    <td><html:text property="accountNumber" value="${arInvoiceform.accountNumber}"></html:text></td>
		</tr>
		<tr class="textlabels">
		    <td>Invoice Number</td>
		    <td><html:text property="invoiceNumber" value="${arInvoiceform.invoiceNumber}" ></html:text></td>
		    <td>Invoice Amount</td>
		    <td><html:text property="invoiceAmount" value="${arInvoiceform.invoiceAmount}" ></html:text></td>
		</tr>
		<tr class="textlabels">
		    <td>From Date</td>
		    <td><html:text property="fromDate" value="${arInvoiceform.fromDate}" ></html:text></td>
		    <td>To Date</td>
		    <td><html:text property="toDate" value="${arInvoiceform.toDate}" ></html:text></td>
		</tr>
		<tr>
		    <td colspan="4" align="center">
			<input type="button" value="Search" name="search" class="buttonStyleNew" onclick="SearchArInvoice()"/>
			<c:if test="${canEdit}">
			    <input type="button" value="Add New"  name="addNew" class="buttonStyleNew" onclick="addNewArInvoice()"/>
			</c:if>
		    </td>
		</tr>
	    </table ><br/>

	    <table width="100%" class="tableBorderNew">
		<tr class="tableHeadingNew">List of AR Invoice </tr><tr>
		    <td align="left">
			<div  style="height: 250px; overflow: auto" >
			    <!--  defaultorder="descending" defaultsort="1" -->
			    <display:table name="${arInvoiceList}" pagesize="<%=pageSize%>"   class="displaytagstyle" id="arInvoice" sort="list" >

				<display:setProperty name="paging.banner.some_items_found">
				    <span class="pagebanner">
					<font color="blue">{0}</font> Invoices Displayed,For more Data click on Page Numbers.

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
				<display:setProperty name="basic.msg.empty_list">
				    <span class="pagebanner">

				    </span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="Invoice"/>
				<display:setProperty name="paging.banner.items_name" value="Invoice "/>
				<display:column property="invoiceNumber"  title="Invoice #" sortable="true" ></display:column>
				<display:column property="customerName"  title="CustomerName" sortable="true"  ></display:column>
				<display:column property="accountNumber"  title="Account #" sortable="true"  ></display:column>
				<display:column property="customerType"  title="Customer Type"  ></display:column>
				<display:column title="Action">
				    <c:if test="${canEdit}">
					<span class="hotspot" onmouseover="tooltip.show('<strong>Edit Invoice</strong>',null,event);" onmouseout="tooltip.hide();">
					    <img src="<%=path%>/img/icons/edit.gif" onclick="editArInvoice(${arInvoice.id})"/>
					</span>
				    </c:if>
				    <span class="hotspot" onmouseover="tooltip.show('<strong>View Invoice</strong>',null,event)" onmouseout="tooltip.hide()" >
					<img src="<%=path%>/img/icons/pubserv.gif" border="0" onclick="viewArInvoice(${arInvoice.id})" />
				    </span>
				    <c:if test="${canEdit}">
					<span class="hotspot" onmouseover="tooltip.show('<strong>Print</strong>',null,event);" onmouseout="tooltip.hide();" >
					    <img src="<%=path%>/img/icons/print.gif" border="0" alt="" onclick="printArInvoice(${arInvoice.id})" />
					</span>
					<span class="hotspot" onmouseover="tooltip.show('<strong>Email</strong>',null,event);" onmouseout="tooltip.hide();" >
					    <img src="<%=path%>/img/icons/send.gif" border="0" alt="" onclick="openMailPopup(${arInvoice.id})" />
					</span>
				    </c:if>
				</display:column>
			    </display:table>

			</div></td>

		</tr></table>
	    <!--  This is for the mailer -->
	    <input type="hidden" name="mailToAddress" />
	    <input type="hidden" name="mailCcAddress" />
	    <input type="hidden" name="mailBccAddress" />
	    <input type="hidden" name="mailSubject" />
	    <input type="hidden" name="mailBody" />
	    <input type="hidden" name="arInvoiceId" value="${arInvoice.id}"/>
	</html:form>
	<c:if test="${fileName!=null}">
	    <script type="text/javascript">
		GB_show('AR Invoice Report','<%=path%>/servlet/PdfServlet?fileName=${fileName}',400,650);
	    </script>
	</c:if>
    </body>
    <script type="text/javascript">
	function SearchArInvoice(){
	    document.arInvoiceform.submit();
	}
	function editArInvoice(val1){
	    document.arInvoiceform.buttonValue.value="editInvoice";
	    document.arInvoiceform.arInvoiceId.value = val1;
	    document.arInvoiceform.submit();
	}
	function viewArInvoice(val1){
	    document.arInvoiceform.buttonValue.value="viewInvoice";
	    document.arInvoiceform.arInvoiceId.value = val1;
	    document.arInvoiceform.submit();
	
	}
	function addNewArInvoice(){
	    document.arInvoiceform.buttonValue.value="addArInvoice";
	    document.arInvoiceform.submit();
		
	}
	function printArInvoice(arInvoiceId){
	    document.arInvoiceform.buttonValue.value="printArInvoice";
	    document.arInvoiceform.arInvoiceId.value = arInvoiceId;
	    document.arInvoiceform.submit();
	}
	function openMailPopup(arInvoiceId){
	    document.arInvoiceform.arInvoiceId.value = arInvoiceId;
	    GB_show('Email','<%=path%>/sendEmail.do?id='+arInvoiceId+'&moduleName=ArInvoice',455,650);
	    return true;
	}
	function sendReportByEmail(){
	    document.arInvoiceform.buttonValue.value="emailArInvoice";
	    document.arInvoiceform.submit();
	}
	

    </script>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>