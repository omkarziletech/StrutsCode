<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
List search=new ArrayList();
String searchCode="";
String path1="";
String searchChargeCode="";
String apindex="";
//**************************************************
// request.getParameter("button")identifies the tabs
// For agss the key is "searchchargecode " 
// For csss the key is "searchchargecodecss"
//**************************************************
if(request.getParameter("button")!=null)
{
if(session.getAttribute("chargeCodeCodeList")!=null)
{
session.removeAttribute("chargeCodeCodeList");
}
searchCode=(String)request.getParameter("button");
session.setAttribute("searchCode",searchCode);
}
if(request.getParameter("index")!=null)
{
apindex=(String)request.getParameter("index");
session.setAttribute("apayindex",apindex);

}
String msg="";
if(request.getAttribute("message")!=null){
msg=(String)request.getAttribute("message");
}
if(request.getAttribute("searchchargecode")!=null)
{
	searchChargeCode=(String)request.getAttribute("searchchargecode");
}

if(session.getAttribute("chargeCodeCodeList")!=null)
{
search=(List)session.getAttribute("chargeCodeCodeList");
}


if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");

}
String res="";
if(request.getParameter("button")!=null){
 
	res=request.getParameter("button");

	if(res!=null&& (res.equals("documentCharges")||res.equals("lcldocumentCharges")))
	session.setAttribute("ratebutton","lcldocumentCharges");
 
}

 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(searchChargeCode.equals("searchchargecode"))
{
%>
	<script>
	
		self.close();
		opener.location.href="<%=path%>/jsps/ratemanagement/airRatesFrame.jsp";
	</script>
<%
}	
if(msg.equals(""))
{
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
%>    
<script>
		self.close();
		opener.location.href="<%=path%>/<%=path1%>";
</script>
	<%}
}	

	
DBUtil dbUtil=new DBUtil();
String editPath=path+"/searchChargeCode.do";

%>
<html> 
	<head>
		<title>JSP for SearchChargeCodeForm form</title>
		<base href="<%=basePath%>">
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript">
	    function search1()
        { 
        if(document.searchChargeCodeForm.chargeCode.value=="" && document.searchChargeCodeForm.codeDescription.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
           // document.searchChargeCodeForm.appIndex.value="";
        	document.searchChargeCodeForm.buttonValue.value="search";
  	    	document.searchChargeCodeForm.submit();
  	    }
  	    </script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/searchChargeCode" name="searchChargeCodeForm" type="com.gp.cong.logisoft.struts.ratemangement.form.SearchChargeCodeForm" scope="request">
		<font color="blue" size="4"><%=msg%></font>
		<html:hidden property="buttonValue"/>
		
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    	<tr class="tableHeadingNew">
    		Search Criteria
  		</tr>
  		<tr >
      		<td class="textlabels">Charge Code</td>
	  		<td><html:text property="chargeCode"  value="" size="3"/></td>
	  		<td class="textlabels"> Code Description</td>
	  		<td><html:text property="codeDescription" value=""/></td>
	  	    <td><img src="<%=path%>/img/search1.gif" onclick="search1()" /></td>
	  	</tr>
	    </table>
	  	<br>
		<%
  			int i=0;
			if((search!=null)) 
			{
		%>
		<div id="divtablesty1" class="scrolldisplaytable">
		<table width="100%" class="tableBorderNew"> 
		<tr class="tableHeadingNew">
    	List of Charge Code
  		</tr>
  		<tr><td>
		<display:table name="<%=search%>" pagesize="10" class="displaytagstyle">
		<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    					<font color="blue">{0}</font> Code Details displayed,For more ChargeCode click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="ChargeCode"/>
  			<display:setProperty name="paging.banner.items_name" value="ChargeCode"/>
    				
  			<%
  			String code="";
  			if(search!=null && search.size()>0)
  			{
  			GenericCode g=(GenericCode)search.get(i);
  			if(g.getCode()!=null)
  			{
  			code=g.getCode();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="Charge Code"><a href="<%=tempPath%>"><%=code%></a></display:column>
		
		 <display:column property="codedesc" title="Code Description"></display:column>
		 <%i++; %>
		</display:table>  
</td></tr></table>
  		</div>
  	<%}%>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

