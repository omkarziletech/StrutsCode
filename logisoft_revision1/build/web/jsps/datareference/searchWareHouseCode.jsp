<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.WarehouseTemp"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%

String path=request.getContextPath();
List search= new ArrayList();
String wareHouseCode = "";
String wareHouseName = "";
String search1="";

if(request.getParameter("button")!=null)
{


search1=(String)request.getParameter("button");
session.setAttribute("search",search1);
}





if(request.getParameter("wareHouseCode") != null)
{ 
wareHouseCode = request.getParameter("wareHouseCode");

}

if(request.getParameter("wareHouseName") != null)
{ 
wareHouseName = request.getParameter("wareHouseName");
}




if(session.getAttribute("wareHouse") != null)
 {
   search = (List)session.getAttribute("wareHouse");
  
 }

 String editPath = path +"/searchWareHouseCode.do";
 
 String nameno = "";
if(request.getAttribute("checked1")!=null)
{
 %>
 <script type="text/javascript">
    self.close();
	opener.location.href="<%=path%>/jsps/Containermanagement/CreateUnit.jsp";
 </script>
 <%
 
 }
if(request.getAttribute("checked1")!=null)
{
 %>
 <script type="text/javascript">
    self.close();
	opener.location.href="<%=path%>/jsps/Containermanagement/CreateUnit.jsp";
 </script>
 <%
 
 }
  

 
else if(request.getAttribute("checked") != null)
{

 %>
 <script type="text/javascript">
    self.close();
	opener.location.href="<%=path%>/jsps/datareference/SearchWarehouse.jsp";
 </script>
 <%
 
 }
  %>

<html> 
	<head>
		<title>JSP for SearchCarrierCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
		<script type="text/javascript">
	   
	     function search1()
        { 
        if( document.searchWareHouseCodeForm.wareHouseCode.value=="" &&  document.searchWareHouseCodeForm.wareHouseName.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
          
        	document.searchWareHouseCodeForm.buttonValue.value="search";
  	    	document.searchWareHouseCodeForm.submit();
  	    }
  	    
  	    function wareHouseload(val1,val2,val3)
  	    {
  	     
  	      if(val1 != ""  || val2 != "" )
  	       {
  	      
  	       document.searchWareHouseCodeForm.wareHouseCode.value = val1;
  	       document.searchWareHouseCodeForm.wareHouseName.value = val2;
  	       
		   document.searchWareHouseCodeForm.submit();
  	       } 
  	       
  	     
  	       
  	       if(val3 != "")
  	       {
  	       
  	          document.searchWareHouseCodeForm.index.value = "0";
		      document.searchWareHouseCodeForm.submit();
  	       }
	 
  	    }
  	    </script>
	</head>
	<body  class="whitebackgrnd" onload="wareHouseload('<%=wareHouseCode %>','<%=wareHouseName %>','<%=nameno %>')")>
		<html:form action="/searchWareHouseCode" scope="request">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">WareHouse Code</td>
	  		<td><html:text property="wareHouseCode"  size="3"/></td>
	  		<td class="textlabels">WareHouse Name</td>
	  		<td><html:text property="wareHouseName"/></td>
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
		 if( request.getParameter("wareHouseCode") != "" && request.getParameter("wareHouseName") != "")
		 {
		
		%>
		 <div id="divtablesty1" >
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
  		    <%
  		      String whcode = "";
  			  String whname = "";
  			
  			if(search!=null && search.size()>0)
  			{
  			WarehouseTemp w=(WarehouseTemp)search.get(i);
  			if(w.getId() != null)
  			{
  			whcode = w.getId();
  			}
  			if(w.getWarehouseName() != null)
  			{
  			  whname = w.getWarehouseName();
  			}
  			}
  		    String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  		     %>
  			<display:column title="WHCode" ><a href="<%=tempPath%>"> <%=whcode %> </a></display:column>
  			<display:column title="WHName" > <%=whname %> </display:column>
  			 <%i++; %>
		</display:table>  
  	     </div>
  	     
  	     <%}
else
{ 
%>
<display:table>
  	<display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					No Records Found.
				</span>	
				</display:setProperty>
				</display:table>
  	     <% } }%>
  	      <html:hidden property="buttonValue"/>
  	      <html:hidden property="index"/>
    	</html:form>	
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



