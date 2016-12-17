<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.domain.Usecases,com.gp.cong.logisoft.domain.DataExchangeTransaction"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List useCaseList=null;
DBUtil dbUtil=new DBUtil();
String buttonValue="load";

if(request.getAttribute("buttonValue")!=null)
{
    buttonValue=(String)request.getAttribute("buttonValue");
}
if(session.getAttribute("useCaseList")!=null)
{
useCaseList=(List)session.getAttribute("useCaseList");
}
if(buttonValue.equals("searchall"))
{
 useCaseList=dbUtil.getAllUseCases();
 session.setAttribute("useCaseList",useCaseList);
}
request.setAttribute("usecaseidlist",dbUtil.getAllUseCaseId());
request.setAttribute("flowlist",dbUtil.getFlowList());
request.setAttribute("statuslist",dbUtil.getStatasList());
request.setAttribute("hourslist",dbUtil.getHours());
request.setAttribute("minuteslist",dbUtil.getMinutes());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
<html> 
	<head>
	<base href="<%=basePath%>">
		<title>JSP for SearchUseCaseForm form</title>
		<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
<link rel="stylesheet" type="text/css" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<script type="text/javascript" src="<%=path%>/js/caljs/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
<script language="Javascript" type="text/javascript" src="<%=path%>/js/common.js"></script>
<script>
function search1form()
{

document.searchUseCaseForm.buttonValue.value="searchall";
document.searchUseCaseForm.submit();
}
function searchform()
{
if(document.searchUseCaseForm.useCaseId.value=="0")
{
if(document.searchUseCaseForm.txtCal.value=="")
{

if(document.searchUseCaseForm.docSetKeyValue.value=="")
{
if(document.searchUseCaseForm.flowFrom.value=="0")
{
if(document.searchUseCaseForm.status.value=="0")
{
alert("please select any search criteria");
return;
}
}
}
}}

if(document.searchUseCaseForm.txtCal.value!="")
{
if(document.searchUseCaseForm.hours.value=="0")
{
alert("please select the date");
return;
}
}
if(document.searchUseCaseForm.txtCal.value!="")
{
if(isValidDate(document.searchUseCaseForm.txtCal.value)==false)
{
	document.searchUseCaseForm.txtCal.value="";
	document.searchUseCaseForm.txtCal.focus();
	return;
}
}
document.searchUseCaseForm.buttonValue.value="search";
document.searchUseCaseForm.submit();
}

</script>
<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
		<html:form action="/searchUseCase" scope="request">
		<table width="840" border="0" cellpadding="0" cellspacing="0">
  
  <tr>
    <td height="15"  class="headerbluesmall">&nbsp;&nbsp;Search Criteria </td>
  </tr>
  <br />
  <tr>
    <td><table width="722" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td width="675"><table width="826" border="0" cellspacing="0">
          <tr>
            <td width="112" class="style2">Use Case Id </td>
            <td width="128"><html:select property="useCaseId" styleClass="selectboxstyle">
                 <html:optionsCollection name="usecaseidlist" />
                </html:select></td>
                <td></td>
            <td width="59" class="style2">Date</td>
            <td width="85"><html:text property="txtCal" styleId="txtcal" /></td>
            <td></td>
          <td width="25"><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
           
            <td width="36" class="style2" >Time </td>
            <td width="54" ><html:select property="hours">
                 <html:optionsCollection name="hourslist"/>
                </html:select></td>
            <td width="26" class="style2" >Hrs</td>
            <td width="54" ><html:select property="minutes">
                 <html:optionsCollection name="minuteslist"/>
                </html:select></td>
            <td width="102" class="style2">Mins</td>
            <td width="20" class="style2" >&nbsp;</td>
            </tr>
          <tr>
            <td class="style2">&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td colspan="2">&nbsp;</td>
            <td >&nbsp;</td>
            <td colspan="4" >&nbsp;</td>
            
          </tr>
          <tr>
            <td class="style2">Doc Set Key Value</td>
            <td><html:text property="docSetKeyValue" /></td>
            <td></td>
            <td class="style2">Flow From</td>
            <td colspan="2"><html:select property="flowFrom" styleClass="selectboxstyle">
                 <html:optionsCollection name="flowlist"/>
                </html:select></td>
                <td></td>
            <td class="style2">Status</td>
            <td colspan="4" ><html:select property="status" styleClass="selectboxstyle">
                 <html:optionsCollection name="statuslist"/>
                </html:select></td>
            <td >&nbsp;</td>
            <td><img src="<%=path%>/img/search1.gif" id="search" border="0" onclick="searchform()" 
              	   style="cursor: pointer; cursor: hand;"/>
            </td>      
              <td >&nbsp;</td>
           <td><img src="<%=path%>/img/showall.gif"  border="0" onclick="search1form()" style="cursor: pointer; cursor: hand;"/>
            </td>
          </tr>
          <tr>
            <td class="style2">&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td colspan="2">&nbsp;</td>
            <td >&nbsp;</td>
            <td colspan="4" >&nbsp;</td>
            <td >&nbsp;</td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
		<tr>
        <td height="15"  class="headerbluesmall">&nbsp;&nbsp;List of Data Exchange </td>
  </tr>
		<tr>
                <td>
           <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:270px;">
          <table  border="0" cellpadding="0" cellspacing="0">
       
    
		<display:table name="<%=useCaseList%>" pagesize="<%=pageSize %>" class="displaytagstyle"> 
		
		<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Usecases details displayed,For more Usecases click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="DataExchangeTransaction"/>
  			<display:setProperty name="paging.banner.items_name" value="DataExchangeTransactions"/>
		
		<display:column property="usecaseCode" title="Use Case ID" />
		<display:column></display:column>
		<display:column></display:column>
		<display:column property="usecaseName" title="Name" />
		<display:column></display:column>
		<display:column></display:column>
		<display:column property="docSetKeyValue" title="Doc Set Key Value" /> 
		<display:column></display:column> 
		<display:column></display:column>
		<display:column property="flowFrom" title="Flow From" />
		<display:column></display:column>
		<display:column></display:column>
		<display:column property="status" title="Status" />
		<display:column></display:column>
		<display:column></display:column>
		<display:column property="useCaseDate" title="Date" />
		<display:column></display:column>
		<display:column></display:column>
       </display:table>
              </table></div></td>
            </tr>
           
  <tr>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
     <td align="center"><table width="200" border="0" cellspacing="0" cellpadding="0">
       <tr>

         <td>&nbsp;</td>
         <td>&nbsp;</td>
       </tr>
     </table></td>
  </tr>
  <html:hidden property="buttonValue"/>
		</html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

