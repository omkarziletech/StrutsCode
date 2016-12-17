<%@ page language="java" import="java.util.*,com.gp.cvst.logisoft.hibernate.dao.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
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
PaymentsDAO paymentsDAO = new PaymentsDAO();
TransactionDAO transDAO = new TransactionDAO();
String index = request.getParameter("value");
List showList=null;

List adjustList = null;
TransactionBean tbean=new TransactionBean();
if(session.getAttribute("transList")!=null)
{
 showList=(List)session.getAttribute("transList");
 tbean=(TransactionBean)showList.get(Integer.parseInt(index));
 String transid=tbean.getTransactionId();
adjustList = (List)transDAO.findforsearch(transid);
}

  

 
 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Adjust</title>

<%@include file="../includes/baseResources.jsp" %>


<script language="javascript" type="text/javascript">
        function confirmdelete()
	{
		var result = confirm('Are you sure you want to delete Port?');
		return result
	}
</script>
	
</head>

<body class="whitebackgrnd">
 <table width="419" border="0" cellpadding="0" cellspacing="0" >
    <tr class="textlabels">
      <td>&nbsp;</td>
      <td height="18">&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>

      <td>&nbsp;</td>
    </tr>
    <tr class="textlabels">
      <td class="headerbluelarge">More</td>
      <td height="18">&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>

    </tr>
    <tr class="textlabels">
      <td width="158">&nbsp;</td>
      <td width="3" height="15">&nbsp;</td>
      <td width="73">&nbsp;</td>
      <td width="152">&nbsp;</td>
      <td width="33">&nbsp;</td>
    </tr>

     <tr>
    <td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp;More</td> 
  </tr>
</table>
	  <%
    
 int i=0;
   if((adjustList !=null  && adjustList.size()>0))
 {

  %>
<div id="ARinquiryListDiv" >
<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:360px;">
<display:table name="<%=adjustList%>" pagesize="<%=pageSize%>" class="displaytagstyle" style="width:60%" id="accountrecievable" sort="list" >
 
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
			<display:setProperty name="paging.banner.item_name" value="User"/>
  			<display:setProperty name="paging.banner.items_name" value="Users"/>
		
   <display:column   property="subhouseBl" title="ShowHouseBL" sortable="true" headerClass="sortable"></display:column>
      <display:column   property="cotainernumber" title="ContainerNumber" sortable="true" headerClass="sortable"></display:column>
   <display:column  property="voyagenumber" title="VoyageNumber"  sortable="true" headerClass="sortable"></display:column>
    <display:column  property="vesselnumber" title="Vessel Number "  sortable="true" headerClass="sortable"></display:column>
    <display:column  property="masterbl" title=" MasterBL "  sortable="true" headerClass="sortable"></display:column>
        <display:column  property="customerReference" title=" Customer Reference No "  sortable="true" headerClass="sortable"></display:column>
    masterbl
   <%i++; %>
</display:table>  
  </div>
</div>
<%} %>
 
<!--</div>-->
</body>

	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

