<%@ page language="java" import="java.util.*,java.text.*,com.gp.cvst.logisoft.hibernate.dao.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cvst.logisoft.AccountingConstants"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
path="../..";
}
 
String index = request.getParameter("value");
String parentPage = request.getParameter("page");
List showList=null;
PaymentsDAO paymentdao= new PaymentsDAO();
List paymentList = null;
TransactionBean tbean=new TransactionBean();
String invOrBLNumber = "";
if(request.getParameter("value")!=null)
{
 String transid=(String)request.getParameter("value");
 TransactionDAO transactionDAO=new TransactionDAO();
  invOrBLNumber=transactionDAO.getInvOrBLNumberForTransaction(transid);
 
  if(null != parentPage && parentPage.trim().equals(AccountingConstants.AR_INQUIRY_PAGE)) {
 	paymentList =  paymentdao.getPaymentsByTransactionIdAndStatus(transid,AccountingConstants.PAYMENT_STATUS_CLOSED);
 	
 }else {
 	paymentList =  paymentdao.showforinvoice(transid);
 	
 }
}
else
{
//paymentList=(List)session.getAttribute("payementList");
}

Payments payment = new Payments();
Date batchDate = payment.getBatchDate();

 
%>
   
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
  
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Batchitems</title>

<%@include file="../includes/baseResources.jsp" %>
</head>

<body class="whitebackgrnd">
  <table width="100%" border="0" cellpadding="0" cellspacing="0" >
    
    <tr class="textlabels">
      <td height="18" colspan="2"class="tableHeadingNew"> &nbsp;Show Transactions For Invoice/BL # <c:out value="<%=invOrBLNumber%>" /></td>
      
    </tr></table>
  
    <%
    List a = new ArrayList();
    NumberFormat number=new DecimalFormat("0.00");
    
 int i=0;
   if((paymentList !=null  && paymentList.size()>0))
 {

  %>

<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:250px;">
<display:table name="<%=paymentList%>" pagesize="<%=pageSize%>" class="displaytagstyle" style="width:100%" id="accountrecievable" sort="list" defaultsort="1" defaultorder="descending">
<%

showpaymentsBean showbean = (showpaymentsBean) paymentList.get(i);

String dates = showbean.getBatchdate();
if(dates==null)
{
dates="";
}
a.add(dates);

String adjAmount = number.format(showbean.getAdjamount().doubleValue());
String amount = number.format(showbean.getAmount().doubleValue());


if(amount==null || amount.equals("0.00")){amount="";}
if(adjAmount==null || adjAmount.equals("0.00")){adjAmount="";}
 %>
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
				<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="Payment"/>
  			<display:setProperty name="paging.banner.items_name" value="Payments"/>
		     <display:footer>
		        
		      	 <td colspan="3" align="right"> <b>Total :</b></td>
		      	 <td align="right">
		      	   <c:set var="totalAmount" value="${totalAmount+accountrecievable.amount}"/>
		      	   <font size="2">
		      	    <fmt:formatNumber  pattern="###,###,##0.00" value="${totalAmount}" var="amt"> 
		      	    </fmt:formatNumber>
		      	   <c:out value="${amt}"></c:out>
		      	   
		      	   </font>
		      	 </td>
		      	 <td colspan="3" align="right"><b>Adj Total:</b></td>
		      	 <td  align="right">
		      	  <fmt:formatNumber pattern="0.00" value="${totalAdjAmt+accountrecievable.adjamount}" var="totalAdjmentAmt"> </fmt:formatNumber>
		      	     <c:set var="totalAdjAmt" value="${totalAdjmentAmt}"></c:set>
		      	     <font size="2"><c:out value="${totalAdjAmt}"></c:out></font>
                 </td>
                 <tr>
                 	<td colspan="3" align="right"> <b>Grand Total :</b></td>
		        	<td align="right">
			      	    <c:set var="grandTotal" value="${totalAmount+totalAdjAmt}"/>
			      	    <font size="2">
				      	    <fmt:formatNumber  pattern="###,###,##0.00" value="${grandTotal}" var="grandAmt"> </fmt:formatNumber>
				      	    <c:out value="${grandAmt}"></c:out>
			      	    </font>
		      	 </td>
                 </tr>
		      </display:footer>  
		     
   <display:column  property="batchId" title="Batch#"  sortable="true" headerClass="sortable"></display:column>
  <!-- Title Check# to Transaction No -->
   <display:column  property="chequenumber" title="Transaction No"  sortable="true" headerClass="sortable"></display:column>
<%--    <display:column  property="batchdate" title="Check Date"  sortable="true" headerClass="sortable"></display:column>--%>
    <display:column  property="batchDepositDate" title="Deposit Date"  sortable="true" headerClass="sortable"></display:column>
    <display:column   title="Amount" sortable="true" headerClass="sortable" style="text-align:right">
      <c:choose>
        <c:when test="${accountrecievable.status=='Open'}">
          <c:out value="<%=amount%>"/> <font style="font: bold;font-size:small;color: red; "> * </font>
        </c:when>
        <c:otherwise>
          <c:out value="<%=amount%>"/>
        </c:otherwise>
      </c:choose>
    </display:column>
    <!-- Charge Code changed to GL Acct No -->
    <display:column   property="chargeCode" title="Gl Account#" sortable="true" headerClass="sortable"></display:column>
    <display:column   title="Adj Date" property="adjustmentDate" style="text-align:right"></display:column>
    <display:column title="User Name" property="userName"></display:column>
    <display:column   title="Adj Amount" sortable="true" headerClass="sortable" style="text-align:right"><c:out value="<%=adjAmount%>"/></display:column>
    <display:column>
	  <c:set var="totalAmount" value="${totalAmount+accountrecievable.amount}"/>
	</display:column>
	<display:column>
	  <c:set var="totalAdjAmt" value="${totalAdjAmt+accountrecievable.adjamount}"/>
	</display:column>
   <%i++; %>
</display:table>  
  </div>

<%} %>
<%if(a!=null)
session.setAttribute("lista",a);
 %>

</body>


<script language="javascript" type="text/javascript">
        function confirmdelete()
	{
		var result = confirm('Are you sure you want to delete Port?');
		return result
	}
	
	function AddPayment()
	{
		document.showpayments.buttonValue.value = "AddPayment";
		document.showpayments.submit();
	}
	
	
	function check(){
	 self.close();
	}
	
</script>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

