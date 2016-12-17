<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.CarriersOrLineTemp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
List search=new ArrayList();
CarriersOrLineTemp carriersOrLineTemp=new CarriersOrLineTemp();
String searchssline="";
String path1="";
String ssline="";
String sslinename="";
if(request.getParameter("ssline")!=null){
ssline=request.getParameter("ssline");
}
if(request.getParameter("sslinename")!=null){
sslinename=request.getParameter("sslinename");
}
if(request.getParameter("carrierName") != null){
   sslinename = request.getParameter("carrierName");
}
if(request.getParameter("button")!=null)
{
searchssline=(String)request.getParameter("button");
session.setAttribute("searchssline",searchssline);
if(session.getAttribute("fclpopList")!=null)
{
session.removeAttribute("fclpopList");
}
}
if(session.getAttribute("fclpopList")!=null)
{
search=(List)session.getAttribute("fclpopList");
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");

}
String nameno="";
if(search!=null && search.size()>0 && search.size()==1){
	carriersOrLineTemp=(CarriersOrLineTemp)search.get(0);
  		if(carriersOrLineTemp.getCarriercode()!=null)
  			{
  			nameno=carriersOrLineTemp.getCarriercode();
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
String editPath=path+"/searchSSLine.do";

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
        if(document.SearchSSLineForm.ssLineNumber.value=="" && document.SearchSSLineForm.ssLineName.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
        	document.SearchSSLineForm.buttonValue.value="search"
  	    	document.SearchSSLineForm.submit();
  	    }
  	     function SSLine(val1,val2,val3)
		{  
		if(val1!=""){
		 if(document.SearchSSLineForm.ssLineNumber.value=="<%=ssline%>")
		  {
		   
		 
        	document.SearchSSLineForm.buttonValue.value="search"
  	    	document.SearchSSLineForm.submit();
  	      }
		  } 
		  if(val3!=""){
		 if(document.SearchSSLineForm.ssLineName.value=="<%=sslinename%>")
		  {
		    document.SearchSSLineForm.ssLineName.value = val3;
        	document.SearchSSLineForm.buttonValue.value="search"
  	    	document.SearchSSLineForm.submit();
  	      }
		  } 
		  if(val2!=""){
		 
		  document.SearchSSLineForm.index.value="0";
  	     document.SearchSSLineForm.submit();
  	      
		  }
		 
		}
  	    </script>
	</head>
	<body class="whitebackgrnd" onload="SSLine('<%=ssline%>','<%=nameno%>','<%=sslinename%>')">
		<html:form action="/searchSSLine" name="SearchSSLineForm" type="com.gp.cong.logisoft.struts.ratemangement.form.SearchSSLineForm" scope="request">
		<html:hidden property="buttonValue"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">SS Line Number </td>
	  		<td><html:text property="ssLineNumber"  value="<%=ssline%>" size="3"/></td>
	  		<td class="textlabels">SS Line Name</td>
	  		<td><html:text property="ssLineName" value="<%=sslinename%>"/></td>
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
		<div id="divtablesty1" class="scrolldisplaytable">
		<display:table name="<%=search%>" pagesize="10" class="displaytagstyle">
		<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    					<font color="blue">{0}</font> SS line  Details displayed,For more SSline click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="SSline"/>
  			<display:setProperty name="paging.banner.items_name" value="SSline"/>
    				
  			<%
  			String SSline="";
  			if(search!=null && search.size()>0)
  			{
  			carriersOrLineTemp=(CarriersOrLineTemp)search.get(i);
  			if(carriersOrLineTemp.getCarriercode()!=null)
  			{
  			SSline=carriersOrLineTemp.getCarriercode();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="SSlineNumber"><a href="<%=tempPath%>"><%=SSline%></a></display:column>
		 <display:column property="carriername" title="SSlineName"></display:column>
		  <display:column property="fclContactNumber" title="ContactNumber"></display:column>
		 <%i++; %>
		</display:table>  
  		</div>
  	<%}%>
<html:hidden property="index"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

