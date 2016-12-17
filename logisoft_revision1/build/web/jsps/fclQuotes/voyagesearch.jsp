<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.VoyageExport"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
List search=new ArrayList();
VoyageExport voyageExport=new VoyageExport();
String searchVoyage="";
String path1="";
String voyageNo="";

if(request.getParameter("vesselNo")!=null){
voyageNo=request.getParameter("ssline");
}

if(request.getParameter("button")!=null)
{
searchVoyage=(String)request.getParameter("button");
session.setAttribute("searchVoyage",searchVoyage);

if(session.getAttribute("voyagepopList")!=null)
{
session.removeAttribute("voyagepopList");
}
}

if(session.getAttribute("voyagepopList")!=null)
{
search=(List)session.getAttribute("voyagepopList");
}

if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");

}

String nameno="";
if(search!=null && search.size()>0 && search.size()==1){
	voyageExport=(VoyageExport)search.get(0);
  		if(voyageExport.getId()!=null)
  			{
  			nameno=voyageExport.getId().toString();
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
String editPath=path+"/voyagesearch.do";

%>
 
<html> 
	<head>
		<title>JSP for VoyagesearchForm form</title>
		<base href="<%=basePath%>">
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript"> 
		
	    function search1()
        { 
        alert(document.voyagesearchForm.voyageNumber.value);
        if(document.voyagesearchForm.voyageNumber.value=="" )
        {
        alert("Please Enter any search Criteria");
        return;
        }
        	document.voyagesearchForm.buttonValue.value="search"
  	    	document.voyagesearchForm.submit();
  	    }
  	     function Voyage(val1)
		{  
		if(val1!=""){
		 if(document.voyagesearchForm.voyageNumber.value=="<%=voyageNo%>")
		  {
		   
		 
        	document.voyagesearchForm.buttonValue.value="search"
  	    	document.voyagesearchForm.submit();
  	      }
		  } 
		}
  	    </script>
	</head>
	<body class="whitebackgrnd" onload="Voyage('<%=voyageNo%>')">
		<html:form action="/voyagesearch" scope="request">
			<html:hidden property="buttonValue"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">Voyage Number </td>
	  		<td><html:text property="voyageNumber"  value="<%=voyageNo%>" size="3"/></td>
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
		<display:table name="<%=search%>" pagesize="<%=pageSize%>"  class="displaytagstyle">
		<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    					<font color="blue">{0}</font> Voyage  Details displayed,For more Voyage click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Voyage"/>
  			<display:setProperty name="paging.banner.items_name" value="Voyages"/>
    				
  			<%
  			String voyage="";
  			if(search!=null && search.size()>0)
  			{
  			voyageExport=(VoyageExport)search.get(i);
  			if(voyageExport.getId()!=null)
  			{
  			voyage=voyageExport.getId().toString();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="VoyageNumber"><a href="<%=tempPath%>"><%=voyage%></a></display:column>
		 
		 
		 <%i++; %>
		</display:table>  
  		</div>
  	<%}%>
<html:hidden property="index"/>

		</html:form>
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

