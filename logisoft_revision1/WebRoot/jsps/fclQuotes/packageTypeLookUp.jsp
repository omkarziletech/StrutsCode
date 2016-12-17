<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%
		String path = request.getContextPath();
		String link = "";
		String button = "";
		if (session.getAttribute("buttonValue") != null) {
			button = (String) session.getAttribute("buttonValue");
		}
		String editPath = path + "/wareHouselookUp.do";
		List wareHouseList = null;
		if (session.getAttribute("WareHouseList") != null) {
			wareHouseList = (List) session.getAttribute("WareHouseList");
		}
		if (request.getAttribute("buttonValue") != null
				&& request.getAttribute("buttonValue").equals(
				"QuotationWarehouse")) {
		%>
			<script type="text/javascript">
				parent.parent.GB_hide();
				parent.parent.getWareHouseDetailsFromLookUp('${wareHouseNewList[0]}','${wareHouseNewList[1]}');
			</script>
		<%
		}
%>
 
<html> 
	<head>
		<title>JSP for PackageTypeLookUpForm form</title>
	<%@include file="../includes/baseResources.jsp"%>
	<script type="text/javascript">
	function getClose(){
		parent.parent.GB_hide();
	}
	function getGo(){
		if(document.wareHouselookUpForm.wareHouseName.value=="" ){
			alert("Please enter WareHouseName");
			return;
		}
		document.wareHouselookUpForm.button.value="Go";
		document.wareHouselookUpForm.submit();
	}
	</script>
		
	
	</head>
	<body>
		<html:form action="/packageTypeLookUp" scope="request">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew ">
				<tr class="tableHeadingNew">
					<td width="100%">
						<table>
							<tr>
								<td width="100%" class="style2">WareHouse Name Search</td>
							</tr>
						</table>
					</td>
					<td>
						<table>
							<tr></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr class="textlabels">
								<td>
									Package Type
								</td>
								<td>
									<html:text property="packageCode"></html:text>
								</td>
								<td>
								     Description
								</td>
								<td>
									<html:text property="packageCodeDescription"></html:text>
								</td>
								
								
								<td>
									<input type="button" class="buttonStyleNew" value="Go" onclick="getGo()">
								</td>
							</tr>
						</table>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew ">
				<tr class="tableHeadingNew">
					<td width="100%">
						<table>
							<tr>
								<td width="100%" class="style2">
									List of Package Types
								</td>
							</tr>
						</table>
					</td>
					<td>
						<table>
							<tr>
							</tr>
						</table>
					</td>
				</tr>
<%
	if(wareHouseList!=null && wareHouseList.size()>0){
 	int i=0; 
 
%>
<div id="divtablesty1" style="height:80%;">
<display:table name="<%=wareHouseList%>" class="displaytagstyle" pagesize="10"  style="width:100%" id="arInquiry" sort="list" >

<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Recievables displayed,For more Records click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Customer"/>
  			<display:setProperty name="paging.banner.items_name" value="Customers"/>
  			<%
  			String wareHouseName="";
  			String address="";
  			Warehouse warehouse=(Warehouse)wareHouseList.get(i);
  				if(warehouse.getWarehouseName()!=null){
  					wareHouseName=warehouse.getWarehouseName();
  				}
  				if(warehouse.getAddress()!=null){
  					address=warehouse.getAddress();
  				}
  			link=editPath+"?paramId="+i+"&button="+button+"&wareHouse="+wareHouseName;
  			
  			 %>
  	<display:column title="Warehouse Name" ><a href="<%=link%>" ><%=wareHouseName%></a></display:column>
  	<display:column title="Address" ><%=address%></display:column>
  
<%i++; %>
</display:table>  
<%} %>
  </div>


		</table>
			
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

