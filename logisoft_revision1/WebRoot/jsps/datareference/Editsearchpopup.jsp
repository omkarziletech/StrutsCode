<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.hibernate.dao.UnLocationDAO,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.beans.CitySelectionBean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ page import="com.gp.cong.logisoft.beans.*,com.gp.cong.logisoft.domain.User" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>
<%@ page import="com.gp.cong.logisoft.util.*" %>
<%
String search="";
if(session.getAttribute("search")!=null)
{
search=(String)session.getAttribute("search");
}
 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(request.getAttribute("buttonValue")!=null  && request.getAttribute("buttonValue").equals("completed"))
{
%>    
<script>
		self.close();
		opener.location.href="<%=path%>/jsps/datareference/EditTerminal.jsp";
	</script>
	<%}
DBUtil dbUtil=new DBUtil();
List cityDetails=(List)session.getAttribute("cityDetails");
  
  
String editPath=path+"/EditSearchcity.do";
CitySelectionBean cityBean=null;
%>
<html:html> 
	<head>
	   <base href="<%=basePath%>">
		<title>JSP for City selectionform</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
	<%@include file="../includes/baseResources.jsp" %>
	
	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	 
		
         function search1()
         { 
                document.editTerminalForm.buttonValue.value="search"
  	     
  	     document.editTerminalForm.submit();
  	     }
  	         	    
	</script>
	
</head>

<body class="whitebackgrnd">
<html:form action="/EditSearchcity" name="editTerminalForm" type="com.gp.cong.logisoft.struts.form.EditTerminalForm" scope="request">
<html:hidden property="buttonValue"/>
 <table width="687" border="0" cellpadding="0" cellspacing="0" height="101">
    
    
    <tr class="textlabels">
      <td width="43">&nbsp;</td>
      <td width="118" height="15">&nbsp;</td>
      <td width="73">&nbsp;</td>
      <td width="152">&nbsp;</td>
      <td width="225">&nbsp;</td>
    </tr>

     <tr>
    <td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  </tr>
  
	 	<tr class="textlabels">
	   <td> &nbsp;</td>
	  </tr>
	<tr >
      <td class="textlabels">City Name </td>
	  <td>
	   <html:text property="city" styleClass="areahighlightgrey" value=""/>
	  </td>
	  <td class="textlabels">&nbsp;<img src="<%=path%>/img/search1.gif" onclick="search1()"> </td>
	  <td>&nbsp;</td>
	  <td class="textlabels"></td>
	  <td> &nbsp;
	 </td>
	  <td><br></td>
	  </tr>
	  </table>
	  <table width="689" border="0" cellpadding="0" cellspacing="0" height="1">
    
    
    

     <tr>
    <td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; List of Cities</td> 
  </tr>
</table>

  <%
  int i=0;
if((cityDetails!=null && cityDetails.size()>0)) 
{
%>
<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:260px;">
<display:table name="<%=cityDetails%>" pagesize="<%=pageSize%>" class="displaytagstyle">
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> City Details displayed,For more code click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="User"/>
  			<display:setProperty name="paging.banner.items_name" value="Users"/>
  			<%String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
                  cityBean=(CitySelectionBean)cityDetails.get(i);

  			%>
		 <display:column   title="City"> <a href="<%=tempPath%>" > <%=cityBean.getCity()%>  </a></display:column>

	<display:column property="counrty" href="" title="Country" ></display:column>
	<display:column property="state" title="State"  maxLength="15"></display:column>
	<%i++; %>
</display:table>  
  </div>
  
<%}%>
 
 
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>
