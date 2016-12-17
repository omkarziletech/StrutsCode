<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%
 String path= request.getContextPath();
 String search="";
 String code="";
 if(request.getParameter("code") != null)
 {
   code= request.getParameter("code");
 }
  %>
<html> 
	<head>
		<title>JSP for SearchCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
		<script type="text/javascript">
		function codeload(val){
		    if(val != ""){
  	       document.searchCodeForm.code.value = val;
		   document.searchCodeForm.submit();
  	       } 
	 
		}
		</script>
	</head>
	<body class="whitebackgrnd" onload="codeload('<%=code%>')">
		<html:form action="/searchCode" scope="request">
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">Code</td>
	  		<td><html:text property="code"  size="5"/></td>
	  		<td class="textlabels">Code Description</td>
	  		<td><html:text property="codeDesc"/></td>
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
		if(search != null )
		{
		 
		 %>
		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:260%;">
		 <display:table name="<%=search%>" pagesize="<%=pageSize%>" class="displaytagstyle">
		<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    					<font color="blue">{0}</font> Terminal Details displayed,For more Ports click on page numbers.
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
  			</display:table>
  			</div>
  			<%
  			}
  			 %>
		</html:form>
	</body>
	
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

