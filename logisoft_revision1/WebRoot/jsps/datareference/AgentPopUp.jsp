<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.beans.customerBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String search1="";
String path1="";
List search=new ArrayList();
customerBean customerbean=new customerBean(); 
   String Name="";
      if(request.getParameter("name")!=null)
     {
        if(session.getAttribute("tradingpartner")!=null)
        {
            session.removeAttribute("tradingpartner");
        }  
      Name=request.getParameter("name");
      session.setAttribute("CustName",Name);
    }
  
  
 
 
 if(session.getAttribute("customerbean")!=null)
{
  customerbean=(customerBean)session.getAttribute("customerbean");
}

if(request.getParameter("button")!=null)
{
if(session.getAttribute("agentList")!=null)
{
session.removeAttribute("agentList");
}
search1=(String)request.getParameter("button");
session.setAttribute("search1",search1);
}
if(session.getAttribute("agentList")!=null)
{
search=(List)session.getAttribute("agentList");
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
 
 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
%>    
<script>
		self.close();
		opener.location.href="<%=path%>/<%=path1%>";
</script>
	<%}
	

String editPath=path+"/agentPopUp.do";

%>
<html> 
	<head>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	
	<script type="text/javascript">
function search1(){
	if(document.agentPopUpForm.accountNo.value=="" && document.agentPopUpForm.accountName.value==""){
	  alert("Please Enter any search Criteria");
      return;
    }
	document.agentPopUpForm.buttonValue.value="search"
  	document.agentPopUpForm.submit();
}
function loadnames(val){
  if(val != ""){
  	 document.agentPopUpForm.accountName.value = val;
  	 document.agentPopUpForm.buttonValue.value="search";
     document.agentPopUpForm.submit();
  } 
}
	
	</script>
   </head>
<body class="whitebackgrnd" onload="loadnames('<%=Name %>')">
<html:form action="/agentPopUp" name="agentPopUpForm" type="com.gp.cong.logisoft.struts.form.AgentPopUpForm" scope="request">
			
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
  <tr class="tableHeadingNew">Search Criteria </tr>
  <tr class="textlabelsBold">
      <td class="textlabels">AccountNo</td>
	  <td><html:text property="accountNo"  value="" size="3" styleClass="textlabelsBoldForTextBox" /></td>
	  <td class="textlabels">Account Name</td>
	  <td><html:text property="accountName"  value="<%=Name%>" size="3" styleClass="textlabelsBoldForTextBox" /> </td>
	  <td><input type="button" class="buttonStyleNew" value="Search" onclick="search1()" /></td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
  <tr class="tableHeadingNew">
    <td>Results</td> 
  </tr>
  <tr><td>
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr><td>
	    <%
		  int i=0;
		  //if(search!=null ||(nameList!=null && nameList.size()>0)) 
		  if(search != null && search.size()>0 ){
		%>
       <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:260%;">
       <display:table name="<%=search%>" pagesize="<%=pageSize%>" class="displaytagstyle">
              <display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Ports Details displayed,For more Ports click on page numbers.
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
			  <display:setProperty name="paging.banner.item_name" value="Ports"/>
  			  <display:setProperty name="paging.banner.items_name" value="Ports"/>
    				
  			<%
	  			String accountNo="";
	  			String acctName="";
	  			if(search!=null && search.size()>0){
	  			  TradingPartner c1=(TradingPartner)search.get(i);
	  			  accountNo=c1.getAccountno();
	  			}
	  			String iStr=String.valueOf(i);
	  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="Account No"><a href="<%=tempPath%>"><%=accountNo%></a></display:column>
		 <display:column title="Account Name" property="accountName"><a href="<%=tempPath%>"><%=acctName%></a></display:column>
		 <display:column title="Account Type" property="acctType"></display:column>
	   <%i++;%>
     </display:table>  
    </div>
    </td></tr>
   </table>
   </td></tr>
<%}%>
</table>

<html:hidden property="buttonValue" styleId="buttonValue"/>

  </html:form>
 </body>
	
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

