<%@ page language="java" pageEncoding="ISO-8859-1" 
import= "com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerAccounting,
java.util.*,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.Customer" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Jsp for Address Information</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	
	function displayprime(val1){
		if(val1=="0"){
		document.addressPopUpForm.buttonValue.value="checkboxValue";
		 document.addressPopUpForm.submit();
		}
		if(val1=="1"){
		document.addressPopUpForm.buttonValue.value="editcheckBoxValue";
		 document.addressPopUpForm.submit();
		}
	 var datatableobj = document.getElementById('addresstable');
				if(datatableobj!=null)
				{
				  for(i=0; i<datatableobj.rows.length; i++)
				  {
						var tablerowobj = datatableobj.rows[i];
		                if(tablerowobj.cells[12].innerHTML=='on')
						{
		                   tablerowobj.cells[0].style.visibility="visible";
						   tablerowobj.cells[0].innerHTML="*";
		                }
		                else
		                {
		                   tablerowobj.cells[0].style.visibility="hidden";
		                }
	              }
	            }
			}
		function addAddess(val1){
			document.tradingPartnerForm.buttonValue.value="addAddressToApConfiguration";
			document.tradingPartnerForm.submit();
			 //parent.parent.GB_hide();
		}
	</script>
	<%@include file="../includes/resources.jsp" %>
  </head>
  
  <body class="whitebackgrnd">
<html:form action="/tradingPartner" name="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">
	<font color="blue" size="2"></font>

<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
 <tr class="tableHeadingNew" colspan="2">Address Details</tr>
  <tr>
     <td>
        <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:300px;">
        <table width="100%"	>	
		   <%
		      int i=0;
		     
		   %>
		
		<display:table name="${addressList}" pagesize="5" class="displaytagstyle" id="addresstable">
		<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Address Details displayed,For more code click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="User"/>
  			<display:setProperty name="paging.banner.items_name" value="Users"/> 
        <display:column title="" style="width:2%;visibility=hidden"></display:column>
		<display:column title="Address" property="primeAddress"/>
		<display:column title="&nbsp;&nbsp;City" property="city1"/>
		<display:column title="&nbsp;&nbsp;State" property="state" />
		<display:column title="&nbsp;&nbsp;Country" property="cuntry"/>
		<display:column title="&nbsp;&nbsp;Zip" property="zip"/>
		<display:column title="" property="primeAddress" style="width:2%;visibility:hidden"></display:column>
		<display:column sortable="true">
			<span class="hotspot" onmouseover="tooltip.show('<strong>Insert</strong>',null,event);" onmouseout="tooltip.hide();">
			<img src="<%=path%>/img/icons/wiz-resources-small.gif" onclick="addAddess('${addresstable.acctNo}')"/></span>
		</display:column>	
       </display:table>

    
    </table></div>
    </td></tr>
 <html:hidden property="buttonValue"/>
 </table>
</html:form>
  </body>
  <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
