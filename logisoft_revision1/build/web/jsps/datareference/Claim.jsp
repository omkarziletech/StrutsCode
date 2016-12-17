<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.Claim,java.util.*,com.gp.cong.logisoft.util.DBUtil"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	List claimdetailslist=null;
	DBUtil dbUtil=new DBUtil();
	claimdetailslist=dbUtil.getAllClaims();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
   <title>JSP for ClaimDetailsForm form</title>
    
	
	
<%@include file="../includes/baseResources.jsp" %>

	</head>
	 <body class="whitebackgrnd">
		<html:form action="/claim" scope="request">
			 <table width="100%" class="tableBorderNew">
          <tr class="tableHeadingNew">
       List of UnClaimed
          </tr>
    
          <tr>
       <td>
             <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" >
         
         
         <display:table name="<%=claimdetailslist%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="claimtable" style="width:100%">
         <display:setProperty name="basic.msg.empty_list"><span class="pagebanner"></display:setProperty> 
         <display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="Claim"/>
  			<display:setProperty name="paging.banner.items_name" value="Claims"/>
  			<display:column property="airlinecode" title="AirLine Code" />
	     <display:column title="Master AWB Number" property="awbno"></display:column>
		</display:table>
        </table></div>   
       </td></tr> 
          </table>
       			
      
     
       
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

