<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*"%>

<jsp:directive.page import="com.gp.cong.logisoft.domain.PortsTemp"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%

String path=request.getContextPath();
List search= new ArrayList();
String scheduleCode = "";
String city ="";


	if(request.getParameter("scheduleCode") != null)
	{ 
		scheduleCode = request.getParameter("scheduleCode");
	}

  if(request.getParameter("city") != null)
	{ 
		city = request.getParameter("city");
	}

 if(session.getAttribute("portslist") != null)
	{
	    search = (List)session.getAttribute("portslist"); 
	}


String editPath = path +"/searchScheduleCode.do";

String nameno = "";
 if(search!=null && search.size()>0 && search.size()==1)
 {
          PortsTemp p=(PortsTemp)search.get(0);
  			if(p.getShedulenumber()!=null)
  			{
  			nameno=p.getShedulenumber();
  			}
 }
  

if(request.getAttribute("checked") != null)
{
 %>
 <script type="text/javascript">
    self.close();
	opener.location.href="<%=path%>/jsps/datareference/managePorts.jsp";
 </script>
 <%
 
 }
  %>
<html> 
	<head>
		<title>JSP for SearchScheduleCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
		<script type="text/javascript">
	    function search1()
        { 
        if(document.searchScheduleCodeForm.scheduleCode.value=="" && document.searchScheduleCodeForm.city.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
        document.searchScheduleCodeForm.buttonValue.value = "search";
        document.searchScheduleCodeForm.submit();
  	    	    	
  	    }
  	    
  	    function scheduleload(val1,val2,val3)
  	    {
  	   
  	      if(val1 != "" || val2 != "")
  	       {
  	       
  	       document.searchScheduleCodeForm.scheduleCode.value = val1;
  	       document.searchScheduleCodeForm.city.value = val2;
		   document.searchScheduleCodeForm.submit();
  	       } 
  	       
  	       if(val3 != "")
  	       {
  	          document.searchScheduleCodeForm.index.value = "0";
		      document.searchScheduleCodeForm.submit();
  	       }
	    
  	    }
  	    </script>
	</head>
	<body  class="whitebackgrnd" onload="scheduleload('<%=scheduleCode %>','<%=city %>','<%=nameno %>')">
		<html:form action="/searchScheduleCode" scope="request">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">Schedule Code</td>
	  		<td><html:text property="scheduleCode" /></td>
	  		<td class="textlabels">City Name</td>
	  		<td><html:text property="city"/></td>
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
		 if(request.getParameter("scheduleCode") != "")
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
  			String scode = "";
  			String cname = "";
  			
  			if(search!=null && search.size()>0)
  			{
  			PortsTemp p=(PortsTemp)search.get(i);
  			if(p.getShedulenumber() != null)
  			{
  			scode = p.getShedulenumber();
  			}
  			if(p.getPortname() != null)
  			{
  			  cname = p.getPortname();
  			}
  			}
  		    String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
  			<display:column title="Schdule Code"><a href="<%=tempPath%>"><%=scode%></a></display:column>
		    <display:column title="City Name"><%=cname%></display:column> 
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

