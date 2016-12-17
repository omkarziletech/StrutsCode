<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.domain.BookingPopUp"%>
<%@ page import="java.util.*,java.text.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ page import="com.gp.cvst.logisoft.util.*"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<% 
 String sellRate="";
    String buyrate="";
    String comments="";
    String charge="";
    String charge1="";
 
BookingPopUp b2=new BookingPopUp();
String path = request.getContextPath();
List book1=new ArrayList();

//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(session.getAttribute("book1")!=null)
{
book1=(List)session.getAttribute("book1");
}
if(session.getAttribute("book2")!=null)
{
b2=(BookingPopUp)session.getAttribute("book2");
}
if(b2.getBuyRate()!=null)
{
buyrate=b2.getBuyRate().toString();
}
if(b2.getSellrate()!=null)
{
sellRate=b2.getSellrate().toString();
}
if(b2.getComments()!=null)
{
comments=b2.getComments();
}
//if(b2.getChargeCode()!=null)
//{
//charge=b2.getChargeCode();
//}*/
    
    
		if(session.getAttribute("chargecodef")!=null)
        {
      		BookingPopUp b=(BookingPopUp)session.getAttribute("chargecodef");
     		//charge=b.getChargeCode();
     		charge1=b.getChargeCode();
     	
     		session.removeAttribute("chargecodef");
      		
    
        } 
    

 
%>
 

		<title>JSP for BookingPopUpForm form</title>
<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function add() 
		{
		  document.bookingPopUpForm.buttonValue.value="add";
		  
		  document.bookingPopUpForm.submit();
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
        
           	document.BookingPopUpForm.submit();
			return false;
		}
		 
		</script>
	</head>
	<body class="whitebackgrnd">
	
		<html:form action="/bookingPopUp" scope="request">
		<font color="blue" size="2"></font>
		<td><img src="<%=path%>/img/addnew.gif" id="addnew" onclick="add()" border="0"/></td>
		<tr>

     

    <div style="width:80%;height:90px;overflow:auto;">
    <table width="100%"  border="0" cellpadding="0" cellspacing="0">
    
    <%int i=0;
    if(book1!=null && book1.size()>0)
     { %>
     <display:table name="<%=book1%>"  class="displaytagstyle" id="lclcoloadratestable" sort="list" style="width:100%" pagesize="<%=pageSize%>"> 
     
     <display:setProperty name="paging.banner.some_items_found">
       <span class="pagebanner">
        <font color="blue">{0}</font> 
         
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
      No Records Found.
     </span>
     
    </display:setProperty>
    <display:setProperty name="paging.banner.placement" value="bottom" />
    <display:setProperty name="paging.banner.item_name" value="FCL BUY Rates"/>
    <display:setProperty name="paging.banner.items_name" value="FCL BUY Rates"/>
    <%
   

    if(book1!=null && book1.size()>0)
    {
    BookingPopUp b1=(BookingPopUp)book1.get(i);
    if(b1.getSellrate()!=null)
    {
    sellRate=b1.getSellrate().toString();
    }
    if(b1.getBuyRate()!=null)
    {
    buyrate=b1.getBuyRate().toString();
    }
    if(b1.getComments()!=null)
    {
    comments=b1.getComments();
    }
    if(b1.getChargeCode()!=null)
    {
      charge=b1.getChargeCode();
 
    }
    if(charge=="")
    {
       if(charge1!="")
       {
       
       charge=charge1;     
       }
    }

  
    } %>
    <display:column title="CodeDescription"> <html:text property="chargecode" value="<%=charge%>"/><img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/fclQuotes/searchCostCode.jsp?button='+'','windows')"/> </display:column>
    <display:column title="Sell Rate"> <html:text property="sellrate" value="<%=sellRate %>"/> </display:column>
     <display:column title="Buy Rate"><html:text property="buyRate" value="<%=buyrate %>"/>   </display:column> 
     <display:column title="Comments"> <html:text property="comments" value="<%=comments %>"/> </display:column>
     <%charge=""; %>
    <%i++; %>
     </display:table>
     <%} %>
    
      
</table></div>

         <html:hidden property="buttonValue" styleId="buttonValue" />
		
		</html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

