<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
List search=new ArrayList();
GenericCode genericCode=new GenericCode();
String searchReason="";
String path1="";
String code="";
String codeDesc="";
if(request.getParameter("codeid")!=null){
code=request.getParameter("codeid");
}
if(request.getParameter("codedesc")!=null)
{
codeDesc=request.getParameter("codedesc");
}


if(request.getParameter("button")!=null)
{
	searchReason=(String)request.getParameter("button");
	session.setAttribute("searchReason",searchReason);
	if(session.getAttribute("reasonPopList")!=null)
	{
		session.removeAttribute("reasonPopList");
	}
}
if(session.getAttribute("reasonPopList")!=null)
{
	search=(List)session.getAttribute("reasonPopList");
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
	<%
}
	
DBUtil dbUtil=new DBUtil();
String editPath=path+"/voyageCodePopUp.do";

%>
 
<html> 
	<head>
		<title>JSP for VoyageCodePopUpForm form</title>
		<base href="<%=basePath%>">
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript"> 
		function search1()
        { 
        
        	if(document.voyageCodePopUpForm.code.value=="" && document.voyageCodePopUpForm.codeDescription.value=="")
        	{
        		alert("Please Enter any search Criteria");
        		return;
        	}
        	document.voyageCodePopUpForm.buttonValue.value="search"
  	    	document.voyageCodePopUpForm.submit();
  	    	document.exportVoyageForm.submit();
  	    	
  	    }
		
		function Vessel(val1,val2,val3)
		{  
			if(val1!="")
			{
		 	 if(document.voyageCodePopUpForm.code.value=="<%=code%>")
		  	 {
		          document.voyageCodePopUpForm.buttonValue.value="search"
  	    		  document.voyageCodePopUpForm.submit();
		     }
		    } 
		    if(val3!="")
		    {
		      if(document.voyageCodePopUpForm.codeDescription.value=="<%=codeDesc%>")
		      {
		           	document.voyageCodePopUpForm.buttonValue.value="search"
  	    			document.voyageCodePopUpForm.submit();
               }
		    } 
		    if(val2!="")
		    {
		 
		                   document.voyageCodePopUpForm.index.value="0";
  	                        document.voyageCodePopUpForm.submit();
  	        }
		 
		}
	   
  	    </script>
	</head>
	<body class="whitebackgrnd" onload="Vessel('<%=code%>','<%=nameno%>','<%=codeDesc%>')">
		<html:form action="/voyageCodePopUp" name="voyageCodePopUpForm" type="com.gp.cong.logisoft.struts.voyagemanagement.form.VoyageCodePopUpForm" scope="request">
			<html:hidden property="buttonValue"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr>
    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  		</tr>
  		<tr >
      		<td class="textlabels">Code </td>
	  		<td><html:text property="code"  value="<%=code%>" size="3"/></td>
	  		<td class="textlabels">Code Description</td>
	  		<td><html:text property="codeDescription" value="<%=codeDesc%>"/></td>
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
    					<font color="blue">{0}</font> Voyage Reason Code Details displayed,For more Vessel click on page numbers.
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
  			String Code="";
  			String codeDEsc="";
  			
  			if(search!=null && search.size()>0)
  			{
  			genericCode=(GenericCode)search.get(i);
  			if(genericCode.getCode()!=null)
  			{
  			Code=genericCode.getCode();
  			}
  			if(genericCode.getCodedesc()!=null)
  			{
  			codeDEsc=genericCode.getCodedesc();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="Code "><a href="<%=tempPath%>"><%=Code%></a></display:column>
		 <display:column property="codedesc"  title="Code Description" ></display:column>
		 
		 <%i++; %>
		</display:table>  
  		</div>
  	<%}%>
<html:hidden property="index"/>

		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



