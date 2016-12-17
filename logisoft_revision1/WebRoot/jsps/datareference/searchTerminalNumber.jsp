<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*"  %>
<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RefTerminalTemp"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%
  String path = request.getContextPath();
  String search = "";
  List terminal = new ArrayList();
  String termNo = "";
  String termLoc = "";
  if(request.getParameter("termno") != null)
  {
      termNo = request.getParameter("termno");
  }
  
   if(request.getParameter("termloc") != null)
  {
      termLoc = request.getParameter("termloc");
  }
  
  if(session.getAttribute("terminal") != null)
  {
     terminal = (List)session.getAttribute("terminal");
  }
  
  String editPath = path +"/searchTerminalNumber.do";
  
  String nameno = "";
 if(terminal!=null && terminal.size()>0 && terminal.size()==1)
 {
          RefTerminalTemp p=(RefTerminalTemp)terminal.get(0);
  			if(p.getTrmnum()!=null)
  			{
  			nameno=p.getTrmnum();
  			}
 }
  
  if(request.getAttribute("checked") != null)
   {
  
   %>
   <script>
     self.close();
     opener.location.href="<%=path%>/jsps/datareference/TerminalManagement.jsp" ;
  
   </script>
  <%
  }
   %>
<html> 
	<head>
		<title>JSP for SearchTerminalNumberForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
		
		<script type="text/javascript">
	    function search1()
        { 
        if(document.searchTerminalNumberForm.termNo.value=="" && document.searchTerminalNumberForm.termName.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
        document.searchTerminalNumberForm.buttonValue.value = "search";
        document.searchTerminalNumberForm.submit();
  	   	
  	    }
  	    
  	    function terminalload(val1,val2,val3)
  	    {
  	     
  	      if(val1 != "" || val2 != "")
  	       {
  	    
  	       document.searchTerminalNumberForm.termNo.value = val1;
  	       document.searchTerminalNumberForm.termName.value = val2;
  	       
		   document.searchTerminalNumberForm.submit();
  	       } 
  	       
  	       if(val3 != "")
  	       {
  	          document.searchTerminalNumberForm.index.value = "0";
		      document.searchTerminalNumberForm.submit();
  	       }
	 
  	    }
  	    </script>
		
	</head>
	<body class="whitebackgrnd" onload="terminalload('<%=termNo %>','<%=termLoc %>','<%=nameno %>')">
		<html:form action="/searchTerminalNumber" scope="request">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">Terminal Number</td>
	  		<td><html:text property="termNo"   size="5"/>  </td>
	  		<td class="textlabels">Terminal Location</td>
	  		<td><html:text property="termName"/></td>
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
		if(terminal != null )
		{
		 if(request.getParameter("termno") != "" && request.getParameter("termloc") != "")
		  {
		%>
		 <div id="divtablesty1" >
		 <display:table name="<%=terminal%>" pagesize="<%=pageSize%>" class="displaytagstyle">
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
  			 String tnum = "";
  			 String tloc = "";
  			
  			if(terminal!=null && terminal.size()>0)
  			{
  			RefTerminalTemp r=(RefTerminalTemp)terminal.get(i);
  			if(r.getTrmnum() != null)
  			{
  			tnum = r.getTrmnum();
  			}
  			if(r.getTrmnam() != null)
  			{
  			  tloc = r.getTerminalLocation();
  			}
  			}
  		    String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			 %>
  			<display:column title="Terminal Num"><a href="<%=tempPath%>"><%=tnum%></a></display:column>
		    <display:column title="Terminal Loc"><%=tloc%></display:column> 
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

