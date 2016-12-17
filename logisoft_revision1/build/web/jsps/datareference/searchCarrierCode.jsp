<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.CarriersOrLineTemp;"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%

String path=request.getContextPath();
List search= new ArrayList();
String carrierCode = "";
String carrierName = "";


if(request.getParameter("carriercode") != null)
{ 
	carrierCode = request.getParameter("carriercode");
}

if(request.getParameter("carriername") != null)
{ 
	carrierName = request.getParameter("carriername");
}


if(session.getAttribute("carrierinfo") != null)
{
    search = (List)session.getAttribute("carrierinfo"); 
}



String nameno="";
if(search!=null && search.size()>0&& search.size()==1)
  			{
  			CarriersOrLineTemp p=(CarriersOrLineTemp)search.get(0);
  			if(p.getCarriercode()!=null)
  			{
  			nameno=p.getCarriercode();
  			}
  			
  			
}
String editPath = path +"/searchCarrierCode.do";

if( request.getAttribute("checked") != null)
{
 %>
 <script type="text/javascript">
    self.close();
	opener.location.href="<%=path%>/jsps/datareference/managingCarriersOAT.jsp";
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
        if(document.searchCarrierCodeForm.carrierCode.value=="" && document.searchCarrierCodeForm.carrierName.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
      
        document.searchCarrierCodeForm.buttonValue.value = "search";
        
        document.searchCarrierCodeForm.submit();
  	    	    	
  	    }
  	    
  	    function carrierload(val1,val2,val3)
  	    {
  	      
  	      if(val1 != "" || val2 != "")
  	       {
  	       
  	       document.searchCarrierCodeForm.carrierCode.value = val1;
  	       document.searchCarrierCodeForm.carrierName.value = val2;
		   document.searchCarrierCodeForm.submit();
  	       } 
  	       if(val3 != "")
  	       {
  	         document.searchCarrierCodeForm.index.value = "0";
        
        document.searchCarrierCodeForm.submit();
  	    	    	
  	       }
	 
  	    }
  	   
  	    </script>
	</head>
	<body  class="whitebackgrnd"  onunload="test()" onload="carrierload('<%=carrierCode %>','<%=carrierName %>','<%=nameno %>')">
		<html:form action="/searchCarrierCode" scope="request">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
        <tr class="tableHeadingNew">
    		 Search Criteria
  		</tr>
  		<tr >
      		<td class="textlabels">Carrier Code</td>
	  		<td><html:text property="carrierCode" size="5"/></td>
	  		<td class="textlabels">Carrier Name</td>
	  		<td><html:text property="carrierName"/></td>
	  	    <td><img src="<%=path%>/img/search1.gif" onclick="search1()" /></td>
	  	</tr>
	    </table>
	   <br>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
        <tr class="tableHeadingNew">
    		 List Of Carries Code
  		</tr>
		<tr><td>
		<%
		int i=0;
		if(search != null )
		{
		
		 if(request.getParameter("carriercode") != "" && request.getParameter("carriername") != "")
		  {
		
		 %>
		 <div id="divtablesty1">
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
  			String ccode = "";
  			String cname = "";
  			
  			if(search!=null && search.size()>0)
  			{
  			CarriersOrLineTemp c=(CarriersOrLineTemp)search.get(i);
  			if(c.getCarriercode() != null)
  			{
  			ccode = c.getCarriercode();
  			}
  			if(c.getCarriername() != null)
  			{
  			  cname = c.getCarriername();
  			}
  			}
  		    String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
  			<display:column title="Carrier Code"><a href="<%=tempPath%>"><%=ccode%></a></display:column>
		    <display:column title="Carrier Name"><%=cname%></display:column> 
		 <%i++; %>
		</display:table>  
  	     </div></td></tr></table>
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
		 <%
		   }
		   }
		  %>
		  <html:hidden property="buttonValue"/>
		  <html:hidden property="index"/>
		</html:form>	
		
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

