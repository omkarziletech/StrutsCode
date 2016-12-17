<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.GenericCode"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
 List search=new ArrayList();
 String path1="";
  String path = request.getContextPath();
 if(session.getAttribute("codeList")!=null)
			{   
				search=(List)session.getAttribute("codeList");
				
			}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");

}
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
%>
<script>
alert("CLOSING");
		self.close();
		opener.location.href="<%=path%>/<%=path1%>";
</script>
	<%}
	
String editPath=path+"/searchCostCode.do";
%>
<html> 
	<head>
		<title>JSP for SearchCostCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript">
		function search1()
		{
		 if(document.searchCostCodeForm.costCode.value=="" && document.searchCostCodeForm.codeDescription.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
            
        	document.searchCostCodeForm.buttonvalue.value="search"
  	    	document.searchCostCodeForm.submit();
		}
		</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/searchCostCode" scope="request">
		<html:hidden property="buttonvalue"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">Cost Code</td>
	  		<td><html:text property="costCode"  value="" size="3"/></td>
	  		<td class="textlabels"> Code Description</td>
	  		<td><html:text property="codeDescription" value=""/></td>
	  	    <td><img src="<%=path%>/img/search1.gif" onclick="search1()" /></td>
	  	</tr>
	    </table>
	  	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
        <tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
		</table>
		
		<%
  			int i=0;
			if((search!=null)) 
			{
		%>
		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:260%;">
		<display:table name="<%=search%>" pagesize="<%=pageSize%>" class="displaytagstyle">
		<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    					<font color="blue">{0}</font> Code Details displayed,For more ChargeCode click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="ChargeCode"/>
  			<display:setProperty name="paging.banner.items_name" value="ChargeCode"/>
    				
  			<%
  			
  			
  			String code="";
  			if(search!=null && search.size()>0)
  			{
  			GenericCode g=(GenericCode)search.get(i);
  			if(g.getCode()!=null)
  			{
  			code=g.getCode();
  			
  			}
  			}
  			String iStr=String.valueOf(i);
  			
  			String tempPath=editPath+"?index="+iStr;
  			
  			%>
		 <display:column title="Cost Code"><a href="<%=tempPath%>"><%=code%></a></display:column>
		
		 <display:column property="codedesc" title="Code Description"></display:column>
		 <%i++; %>
		</display:table>  
  		</div>
  	<%}%>
		</html:form>
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

