<%@ page language="java" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.RefTerminalTemp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
List search=new ArrayList();
String searchTerminal="";
String path1="";
String trname="";
String tername="";
if(request.getParameter("trname")!=null){
trname=request.getParameter("trname");
}
if(request.getParameter("tername")!=null){
tername=request.getParameter("tername");
}

if(request.getParameter("button")!=null)
{
if(session.getAttribute("terminalpopList")!=null)
{
session.removeAttribute("terminalpopList");
}
searchTerminal=(String)request.getParameter("button");
session.setAttribute("searchTerminal",searchTerminal);
}
if(session.getAttribute("terminalpopList")!=null)
{
search=(List)session.getAttribute("terminalpopList");
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
String nameno="";
if(search!=null && search.size()>0 && search.size()==1){
	RefTerminalTemp t=(RefTerminalTemp)search.get(0);
  			if(t.getTrmnum()!=null)
  			{
  			nameno=t.getTrmnum();
  			}
  			
}

 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
%>    
<script>
		self.close();
		opener.location.href="<%=path%>/<%=path1%>";
</script>
	<%}
	
DBUtil dbUtil=new DBUtil();
String editPath=path+"/searchTerminal.do";

%>
 
<html> 
	<head>
		<title>JSP for SearchTerminalForm form</title>
		<base href="<%=basePath%>">
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript">
	    function search1()
        { 
        if(document.searchTerminalForm.terminalNumber.value=="" && document.searchTerminalForm.terminalName.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
        	document.searchTerminalForm.buttonValue.value="search"
  	    	document.searchTerminalForm.submit();
  	    }
  	    function terminalLoad(val1,val2,val3)
		{  
		 if(val1!=""){
		 if(document.searchTerminalForm.terminalNumber.value=="<%=trname%>")
		  {
		   
		  document.searchTerminalForm.buttonValue.value="search"
  	    	document.searchTerminalForm.submit();
  	      
		  } 
		  }
		  if(val3!=""){
		 if(document.searchTerminalForm.terminalName.value=="<%=tername%>")
		  {
		   
		  document.searchTerminalForm.buttonValue.value="search"
  	    	document.searchTerminalForm.submit();
  	      
		  } 
		  }
		   if(val2!=""){
		 
		  document.searchTerminalForm.index.value="0";
  	     document.searchTerminalForm.submit();
  	      
		  }
		 
		 
		}
  	    </script>
	</head>
	<body class="whitebackgrnd" onload="terminalLoad('<%=trname%>','<%=nameno%>','<%=tername %>')">
		<html:form action="/searchTerminal" name="searchTerminalForm" type="com.gp.cong.logisoft.struts.ratemangement.form.SearchTerminalForm" scope="request">
		<html:hidden property="buttonValue"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew">Search Criteria</tr>
		<td>
		<br>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
<%--    	<tr>--%>
<%--    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> --%>
<%--  		</tr>--%>
        
  		<tr >
      		<td class="textlabelsBold">Terminal Number</td>
	  		<td><html:text property="terminalNumber" styleClass="textlabelsBoldForTextBox"  value="<%=trname%>" size="3"/></td>
	  		<td class="textlabelsBold">Terminal Location</td>
	  		<td><html:text property="terminalName" styleClass="textlabelsBoldForTextBox" value="<%=tername%>"/></td>
	  	    <td>
	  	    <input type="button" class="buttonStyleNew" value="Go" onclick="search1()"/>
	  	    </td>
	  	</tr>
	    </table>
	    <br>
	  	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
        <tr class="tableHeadingNew"> Search Results</td>
    		
  		</tr><tr><td valign="top"><br></td><td valign="top"><br></td></tr>
		</table>
		<%
  			int i=0;
			if((search!=null)) 
			{
		%>
		<div id="divtablesty1" class="scrolldisplaytable">
		<display:table name="<%=search%>" pagesize="10" class="displaytagstyle">
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
  			String terminalNum="";
  			if(search!=null && search.size()>0)
  			{
  			RefTerminalTemp t=(RefTerminalTemp)search.get(i);
  			if(t.getTrmnum()!=null)
  			{
  			terminalNum=t.getTrmnum();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="Terminal"><a href="<%=tempPath%>"><%=terminalNum%></a></display:column>
		 <display:column property="terminalLocation" title="Terminal Location"></display:column>
		 <%i++; %>
		</display:table>  
  		</div>
  	<%}%>
  	</td>
  	</table>
  	<html:hidden property="index" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

