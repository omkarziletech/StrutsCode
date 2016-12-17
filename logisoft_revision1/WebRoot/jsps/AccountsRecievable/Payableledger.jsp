<%@ page language="java" import="java.util.*,com.gp.cvst.logisoft.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}
 DBUtil dbUtil = new DBUtil();
  request.setAttribute("periodList",dbUtil.getperiodList());

request.setAttribute("sourceCodeList",dbUtil.getSourcecodeList(33,"no", "Select Source Code"));
//  request.setAttribute("datelist", datelist);			 

String enddate="";
String startdate="";
List datelist =null;
if(request.getAttribute("datelist")!=null)
{
datelist = (List)request.getAttribute("datelist");
startdate = (String)datelist.get(0);
enddate = (String) datelist.get(1);
 
}

List LedgerList=null;
if(session.getAttribute("LedgerList")!=null)
{
   
 LedgerList = (List)session.getAttribute("LedgerList");

}




%> 
 <html:html>
  <head>
    
    
    <title> Reciepts Ledger</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
 
<%@include file="../includes/baseResources.jsp" %>
  </head>
  
  <body class="whitebackgrnd">
 <html:form  action="/receiptsLedger"   name="recieptsLedgerForm" type="com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm" scope="request" > 
  <html:hidden property="buttonValue"/>
<html:hidden property="index"/> 

<table width="805" height="53" border="0" cellpadding="0" cellspacing="0">
<tr class="textlabels">
  <td width="805" align="left" class="headerbluelarge">&nbsp;</td>
</tr>
<tr class="textlabels">
  <td align="left" class="headerbluelarge">Payable Ledger  </td>
</tr>
<tr class="textlabels">
    <td>&nbsp;</td>
  </tr>
  <tr>
  <td></td></tr>
</table>
<table width="100%"  border="0" cellpadding="0" cellspacing="0">
     <tr>
    <td height="12"  class="headerbluesmall">&nbsp; Payable Ledger<br>  </td> 
  </tr>
</table>
<table width="805"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="86">&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td width="64">Sub Ledger<br></td>
    <td colspan="2"> 
    <html:select property="subLedgerList"  >
      <html:optionsCollection name="sourceCodeList" styleClass="unfixedtextfiledstyle" />
      </html:select>
    </td>
    <td> GL Period</td>
    <td width="43">&nbsp;<html:select property="glPeroidList" onchange ="getsource()"> 
    <html:optionsCollection name="periodList" styleClass="unfixedtextfiledstyle" />
      </html:select></td>
 
    <td width="74">
    
    <img src="<%=path%>/img/post.gif" border="0" onclick="postGeneralLedger()"   />
    
    </td>
    <td width="269"> <img src="<%=path%>/img/go1.gif" border="0" onclick="go()"/></td>
  </tr >
  <tr class="textlabels">
    <td>&nbsp;</td>
    <td colspan="5">&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr class="textlabels">
    <td>Start Date<br> </td>
    <td width="84">
    <html:text property="startDate"  value="<%=startdate %>" styleClass="unfixedtextfiledstyle"></html:text>
  
    <td width="40" ><br></td>
    <td width="51"><span class="textlabels">End Date</span><br></td>
    <td width="64"><html:text property="endDate" value="<%=enddate%>"  styleClass="unfixedtextfiledstyle"></html:text>
    
    <td width="52"><img src="<%=path%>/img/details.gif" border="0" onclick="detials()"/></td>
    
    <td>&nbsp;</td>
    <td></td>
  </tr>
  <tr class="textlabels">
    <td>&nbsp;</td>
    <td colspan="5">&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td  colspan ="8" height="15"  class="headerbluesmall">&nbsp;&nbsp;List of&nbsp; Payables </td>
  </tr>
</table>

<%
  
 int i=0;
 if(LedgerList !=null  && LedgerList.size()>0)
 {
  %>
<div id="ARinquiryListDiv" >
<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:360px;"> 

<display:table name="<%=LedgerList%>" pagesize="<%=pageSize%>" class="displaytagstyle" style="width:60%" id="accountrecievable" sort="list" >

 
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
			<display:setProperty name="paging.banner.item_name" value="Transaction"/>
  			<display:setProperty name="paging.banner.items_name" value="Transactions"/>
		
   <display:column   property="glAcctNo" title="GLAccountNumber" sortable="true" headerClass="sortable"></display:column>
      <display:column property="transDate"  title="Transaction Date" sortable="true" headerClass="sortable"></display:column>
   <display:column  property="amount" title="Transaction Amount"  sortable="true" headerClass="sortable"></display:column>
     <display:column  property="recordType" title="Record Type"  sortable="true" headerClass="sortable"></display:column> 
   <%i++; %>
</display:table>  
</div>
</div>
<%}%>
   </html:form>
  </body>
  

<script language="javascript" type="text/javascript">

	 function getsource(){
	 
	 if(document.recieptsLedgerForm.glPeroidList.value==0){
	  alert("select any value");
	  document.recieptsLedgerForm.glPeroidList.focus();
	  return;
	 }
	 document.recieptsLedgerForm.buttonValue.value="addnew";
	 document.recieptsLedgerForm.submit();
	 }
	 function detials(){
	 document.recieptsLedgerForm.buttonValue.value="details";
	 document.recieptsLedgerForm.submit();
	 
	 }
	 function go(){
	 document.recieptsLedgerForm.buttonValue.value="govalues";
	 document.recieptsLedgerForm.submit();
	 
	 }
	 function postGeneralLedger()
	 {
	   document.recieptsLedgerForm.buttonValue.value="postGL";
	   document.recieptsLedgerForm.submit();
	 }
	 	 
</script>
  	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>
