<%@ page language="java" pageEncoding="ISO-8859-1" import=" com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%> 

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List listOfExceptions=null;
String button="";

if(session.getAttribute("remarksList")!=null){
  listOfExceptions=(List)session.getAttribute("remarksList");
}
if(session.getAttribute("buttonValue")!=null){
  button=(String)session.getAttribute("buttonValue");
}
%>

<html>
  <head>
  </head>
   <%@include file="../includes/baseResources.jsp"%>
	<script type="text/javascript">
	
function searchException(){
  var val=document.remarksLookUpForm.remarks.value;
  document.remarksLookUpForm.remarkscode.value=val;
  document.remarksLookUpForm.buttonValue.value="Search";
  document.remarksLookUpForm.submit();
}
function submitAll(){
  document.remarksLookUpForm.buttonValue.value="Go";
  document.remarksLookUpForm.submit();
}	
	
	</script>
  <body class="whitebackgrnd">
    <html:form action="/remarksLookUp" scope="request">
   
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew">Search</tr>
		<tr class="textlabelsBold">
			<td style="padding-left:10px;">Exceptions
				<html:text property="remarks" value="" size="40"></html:text>
				<input type="button" class="buttonStyleNew" value="Search" onclick="searchException()">
		    </td>				
		</tr>	
		<tr style="padding-top:10px;">
		    <td>
			   <table width="100%" border="0" cellpadding="0" cellspacing="0">
			    <tr class="tableHeadingNew">
			    	<td>
			    	   <input type="button" class="buttonStyleNew" value="Submit" onclick="submitAll()" style="width:40px;">
			    		List of Exceptions
			    	</td>
			    </tr>
				<tr><td>
				   <%
					 if(listOfExceptions!=null && listOfExceptions.size()>0){
						 int i=0; 
					%>
		 <div id="divtablesty1" style="height:80%;">
		 
		 <display:table name="<%=listOfExceptions%>"  class="displaytagstyle" 
 			id="lclcoloadratestable" sort="list" style="width:100%" pagesize="<%=pageSize%>"> 

				<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Exceptions displayed,For more Records click on page numbers.
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
			    <display:setProperty name="paging.banner.item_name" value="Remarks"/>
  			    <display:setProperty name="paging.banner.items_name" value="Remarks"/>
  			        <%
  			            String exceptionValue="";
  						GenericCode genericCode=(GenericCode)listOfExceptions.get(i);
  						if(genericCode.getCodedesc()!=null){
  						  exceptionValue=genericCode.getCodedesc().toString();
  						}
  						String index=Integer.toString(i);
  			        %>
  			           
  			   <display:column title="" style="width:20px;">
  			   		<html:radio property="rcheck" value="<%=index%>"></html:radio>
  			   </display:column>   
  	           <display:column title="Exceptions"><%=exceptionValue%></display:column>
  
				<%i++;%>
		</display:table>  
       		<%}%>
		  </div>
		</td></tr>
		</table>
	  </td></tr>
   </table>
   
        <html:hidden property="buttonValue"/>
		<html:hidden property="remarkscode"/>
		<html:hidden property="buttonParameter" value="<%=button%>"/>
		<input type="hidden" name="requestFrom" value="${param.buttonValue}"/>
		</html:form>
  </body>
</html>
