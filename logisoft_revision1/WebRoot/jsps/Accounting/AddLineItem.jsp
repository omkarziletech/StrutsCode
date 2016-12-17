<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.LineItem,java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%
DBUtil dbUtil=new DBUtil();
String path = request.getContextPath();
List lineItemList=new ArrayList();
String itemNo="";
String journalId="0";
if(session.getAttribute("lineItemList")!=null)
{
lineItemList=(List)session.getAttribute("lineItemList");

}
LineItem line=new LineItem();
String reference="";
String refDesc="";
String account="";
String accountDesc="";
String lineDebit="";
String lineCredit="";
String currency="USD";
String buttonValue="";
if(request.getAttribute("buttonValue")!=null)
{
buttonValue=(String)request.getAttribute("buttonValue");
}
if(buttonValue.equals("cancel"))
{
%>    
<script>
		self.close();
		opener.location.href="<%=path%>/jsps/Accounting/JournalEntry.jsp";
</script>
	<%}

/*if(session.getAttribute("line")!=null)
{
line=(LineItem)session.getAttribute("line");
if(line.getLineItemId()!=null)
{
itemNo=line.getLineItemId();
}
if(line.getReference()!=null)
{
reference=line.getReference();
}
if(line.getReferenceDesc()!=null)
{
refDesc=line.getReferenceDesc();
}
if(line.getAccount()!=null)
{
account=line.getAccount();

}
if(line.getAccountDesc()!=null)
{
accountDesc=line.getAccountDesc();
}
if(line.getDebit()!=null && !line.getDebit().equals(""))
{
lineDebit=line.getDebit().toString();
}
if(line.getCredit()!=null && !line.getCredit().equals(""))
{
lineCredit=line.getCredit().toString();
}

if(line.getCurrency()!=null)
{
currency=line.getCurrency();
}
}*/
%>
<html> 
	<head>
		<title>JSP for AddLineItemForm form</title>
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<%@include file="../includes/baseResources.jsp" %>
		<script language="javascript" type="text/javascript">
		
  		function addaccount()
  		{
  			document.addLineItemForm.buttonValue.value="acctonchange";
  			document.addLineItemForm.submit();
  		}
  		function deleteline()
  		{
  			document.addLineItemForm.buttonValue.value="deleteline";
  			document.addLineItemForm.submit();
  		}
  	
  		function popup(mylink, windowname)
		{
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
			mywindow.moveTo(200,180);
			document.addLineItemForm.buttonValue.value="";
			document.addLineItemForm.submit();
			return false;
		}
	function saveline()
  	{	
  	document.addLineItemForm.buttonValue.value="addline";
  	document.addLineItemForm.submit();
  	}
  	
  	
  	
  	
  	
  	function cancel()
  	{
  		var x=document.getElementsByName("dispdebit");
  		var y=0;
  		var z=document.getElementsByName("dispcredit");
  		var setFlag="";
		for(y=0; y<x.length; y++)
		{
			if(x[y].value=="0.0" && z[y].value=="0.0" )
			{
				 setFlag ="bothEmpty";
			}
			if(x[y].value!="0.0" && z[y].value!="0.0" )
			{
				 setFlag ="bothFull";
				 
			}
			
		}
		
  		if(setFlag=="bothEmpty")
  		{
  			alert("Please enter either Debit or Credit");
  			
  			return;
  		}
  		if(setFlag=="bothFull")
  		{
  			alert("Only enter either Debit or Credit");
  			
  			return;
  		}
  		document.addLineItemForm.buttonValue.value="cancel";
  		document.addLineItemForm.submit();
  	}

  function function2() {
     
     x = event.keyCode;
   
    if(x==115)
    {
   
       document.addLineItemForm.buttonValue.value="addline";
  	   document.addLineItemForm.submit();
  	}
    }

		</script>
	</head>
	<body class="whitebackgrnd" onkeydown="function2()">

		<html:form action="/addLineItem" scope="request">
			<table width=100% border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td width="85%"></td>
   <td align="right"><img src="<%=path%>/img/toolBar_add_hover.gif" onclick="saveline()"/></td>
   <td align="right"><img src="<%=path%>/img/cancel.gif" border="0" onclick="cancel()"/></td>
  </tr>
    
  
</table>

  
<table width="100%">
<tr>
    <td  class="headerbluesmall" colspan="10">&nbsp;&nbsp;List of Line Items </td> 
  </tr>
</table>
<table width="100%" >
 <tr>
  <td>
           <div id="divtablesty1" class="scrolldisplaytable">
          <table border="0" cellpadding="0" cellspacing="0">
        <% int i=0;
        
         %>
		<display:table  name="<%=lineItemList%>" pagesize="<%=pageSize%>"  id="lineitemtable" class="displaytagstyle"> 
		
		<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> LineItem details displayed,For more LineItems click on page numbers.
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
				<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="LineItem"/>
  			<display:setProperty name="paging.banner.items_name" value="LineItems"/>
		<%
		String currency1="";
		String lineItemId="";
		String dispreference="";
		String disprefdesc="";
		String dispaccount="";
		String dispaccdisc="";
		String dispcredit="0.0";
		String dispdebit="0.0";
	
		if(lineItemList!=null && lineItemList.size()>0)
		{
			LineItem line1=(LineItem)lineItemList.get(i);
			if(line1.getLineItemId()!=null)
			{
				lineItemId=line1.getLineItemId();
			}
			if(line1.getReference()!=null)
			{
			dispreference=line1.getReference();
			}
			if(line1.getReferenceDesc()!=null)
			{
			disprefdesc=line1.getReferenceDesc();
			}
			if(line1.getAccount()!=null)
			{
			dispaccount=line1.getAccount();
			}
			if(line1.getAccountDesc()!=null)
			{
			dispaccdisc=line1.getAccountDesc();
			}
			if(line1.getDebit()!=null )
			{
				dispdebit=line1.getDebit().toString();
			}
			if(line1.getCredit()!=null)
			{
				dispcredit=line1.getCredit().toString();
			}
			if(line1.getCurrency()!=null)
			{
				currency1=line1.getCurrency();
			}
		}
		
      
	 %>
      <display:column title="Account">
      <html:text property="dispaccount" value="<%=dispaccount%>" styleClass="verysmalldropdownStyle" />
      <img border="0" src="<%=path%>/img/search1.gif" onclick="return popup('<%=path%>/jsps/Accounting/AccountPopUp.jsp?linebutton='+'lineItem&itemNo='+'<%=lineItemId%>','windows')"/>
      </display:column>
      <display:column title="Debit"><html:text property="dispdebit" name="debitsVal" value="<%=dispdebit%>" styleClass="smalltextstyle" onkeypress=" getDecimal(this,10,event)" maxlength="13" styleId="debit"/></display:column>
	  <display:column title="Credit"><html:text property="dispcredit" value="<%=dispcredit%>" styleClass="smalltextstyle" onkeypress=" getDecimal(this,10,event)" maxlength="13" styleId="credit"/></display:column>
	  <display:column title="Account Desc"><html:text property="dispaccountDesc" readonly="true" value="<%=dispaccdisc%>" styleClass="largetextstyle"/></display:column>
      <display:column title="Item No"><html:text property="displineItemId" value="<%=lineItemId%>" readonly="true" styleClass="areahighlightgrey"/></display:column>
	  <display:column title="Reference"><html:text property="dispreference" value="<%=dispreference%>" styleClass="smalltextstyle"/></display:column>
	  <display:column title="Description"><html:text  property="dispreferenceDesc" value="<%=disprefdesc%>" styleClass="largetextstyle"/></display:column>
	  <display:column title="Currency"><html:text property="dispcurrency" readonly="true" value="<%=currency1%>" styleClass="verysmalldropdownStyle" /></display:column>
	<%i++; %>
    </display:table>
    </table></div></td>
    </tr>
    </table>   

<html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

