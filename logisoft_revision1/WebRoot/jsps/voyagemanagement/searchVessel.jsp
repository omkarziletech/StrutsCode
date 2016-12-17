<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
List search=new ArrayList();
GenericCode genericCode=new GenericCode();
String searchVessel="";
String path1="";
String vesselNo="";
String vesselName="";
if(request.getParameter("vesselNo")!=null){
vesselNo=request.getParameter("ssline");
}
if(request.getParameter("vesselno")!=null)
{

vesselNo=request.getParameter("vesselno");
}
if(request.getParameter("sslinename")!=null){
vesselName=request.getParameter("vesselName");
}
if(request.getParameter("vesselname")!=null){
vesselName=request.getParameter("vesselname");
}




if(request.getParameter("button")!=null)
{
searchVessel=(String)request.getParameter("button");
session.setAttribute("searchVessel",searchVessel);
if(session.getAttribute("vesselpopList")!=null)
{
session.removeAttribute("vesselpopList");
}
}
if(session.getAttribute("vesselpopList")!=null)
{
search=(List)session.getAttribute("vesselpopList");
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");

}
String nameno="";
if(search!=null && search.size()>0 && search.size()==1){
	genericCode=(GenericCode)search.get(0);
  		if(genericCode.getCode()!=null)
  			{
  			nameno=genericCode.getCode();
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
String editPath=path+"/searchVessel.do";

%>
 
<html> 
	<head>
		<title>JSP for SearchVesselForm form</title>
		<base href="<%=basePath%>">
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript"> 
	    function search1()
        { 
        //alert(document.searchVesselForm.vesselNumber.value);
        if(document.searchVesselForm.vesselNumber.value=="" && document.searchVesselForm.vesselName.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
        	document.searchVesselForm.buttonValue.value="search"
  	    	document.searchVesselForm.submit();
  	    	document.exportVoyageForm.submit();
  	    	
  	    }
  	     function Vessel(val1,val2,val3)
		{  
		if(val1!=""){
		 if(document.searchVesselForm.vesselNumber.value=="<%=vesselNo%>")
		  {
		   
		 
        	document.searchVesselForm.buttonValue.value="search"
  	    	document.searchVesselForm.submit();
  	      }
		  } 
		  if(val3!=""){
		 if(document.searchVesselForm.vesselName.value=="<%=vesselName%>")
		  {
		   
		 
        	document.searchVesselForm.buttonValue.value="search"
  	    	document.searchVesselForm.submit();
  	      }
		  } 
		  if(val2!=""){
		 
		  document.searchVesselForm.index.value="0";
  	     document.searchVesselForm.submit();
  	      
		  }
		 
		}
  	    </script>
	</head>
	<body class="whitebackgrnd" onload="Vessel('<%=vesselNo%>','<%=nameno%>','<%=vesselName%>')">
		<html:form action="/searchVessel" name="searchVesselForm" type="com.gp.cong.logisoft.struts.voyagemanagement.form.SearchVesselForm" scope="request">
			<html:hidden property="buttonValue"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">Vessel Number </td>
	  		<td><html:text property="vesselNumber"  value="<%=vesselNo%>" size="3"/></td>
	  		<td class="textlabels">Vessel Name</td>
	  		<td><html:text property="vesselName" value="<%=vesselName%>"/></td>
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
    					<font color="blue">{0}</font> Vessel  Details displayed,For more Vessel click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Vessel"/>
  			<display:setProperty name="paging.banner.items_name" value="Vessels"/>
    				
  			<%
  			String vessel="";
  			if(search!=null && search.size()>0)
  			{
  			genericCode=(GenericCode)search.get(i);
  			if(genericCode.getCode()!=null)
  			{
  			vessel=genericCode.getCode();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="VesselNumber"><a href="<%=tempPath%>"><%=vessel%></a></display:column>
		 <display:column property="codedesc" title="VesselName"></display:column>
		 
		 <%i++; %>
		</display:table>  
  		</div>
  	<%}%>
<html:hidden property="index"/>

		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

