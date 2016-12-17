<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.domain.CustAddress,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.bc.fcl.QuotationConstants"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String editPath=path+"/commodityDescriptionLookUp.do";
String link="";
String button="";
String field="",fromPage=null;
List commDescriptionList=null;

if(session.getAttribute("fieldValue")!=null){
	field=(String)session.getAttribute("fieldValue");
}

if(session.getAttribute("buttonValue")!=null){
	button=(String)session.getAttribute("buttonValue");
}

if(session.getAttribute("codeDescriptionList")!=null){
    commDescriptionList=(List)session.getAttribute("codeDescriptionList");
}
if(request.getAttribute("comingFrom")!=null){
    fromPage=(String)request.getAttribute("comingFrom");
}

if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("GeneralInfo")){
%>
<script type="text/javascript">
if(window.opener != null) {
    window.opener.setCommodityDesc('${DescriptionList[0]}','${DescriptionList[1]}');
    window.close();
}else {
    parent.parent.GB_hide();
    parent.parent.setCommodityDesc('${DescriptionList[0]}','${DescriptionList[1]}');
}
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("GeneralImportInfo")){ %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setImportCommodityDesc('${DescriptionList[0]}','${DescriptionList[1]}');
        window.close();
    }else {
	parent.parent.GB_hide();
	parent.parent.setImportCommodityDesc('${DescriptionList[0]}','${DescriptionList[1]}');
    }
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("retailCommodity")){ %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setRetailCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
        window.close();
    }else {
	parent.parent.GB_hide();
	parent.parent.setRetailCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
    }
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("fclCommodity")){ %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setFclCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
        window.close();
    }else {
	parent.parent.GB_hide();
	parent.parent.setFclCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
    }
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("consColoadCommodity")){ %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setConsColoadCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
        window.close();
    }else {
	parent.parent.GB_hide();
	parent.parent.setConsColoadCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
    }
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("consRetailCommodity")){ %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setConsRetailCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
        window.close();
    }else {
	parent.parent.GB_hide();
	parent.parent.setConsRetailCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
    }
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("consFclCommodity")){ %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setConsFclCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
        window.close();
    }else {
	parent.parent.GB_hide();
	parent.parent.setConsFclCommodity('${DescriptionList[0]}','${DescriptionList[1]}');
    }
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("SalesCode")){ %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setSalesCode('${DescriptionList[0]}','${DescriptionList[1]}');
        window.close();
    }else {
	parent.parent.GB_hide();
	parent.parent.setSalesCode('${DescriptionList[0]}','${DescriptionList[1]}');
    }
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("ConsSalesCode")){ %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setConsSalesCode('${DescriptionList[0]}','${DescriptionList[1]}');
        window.close();
    }else {
	parent.parent.GB_hide();
	parent.parent.setConsSalesCode('${DescriptionList[0]}','${DescriptionList[1]}');
    }
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("ContactConfigCode")){
 		if(fromPage==null){
 	  %>
<script type="text/javascript">
    if(window.opener != null) {
        window.opener.setContactCode('${DescriptionList[0]}','${DescriptionList[1]}','${DescriptionList[2]}');
        window.close();
    }else {
        parent.parent.GB_hide();
        parent.parent.setContactCode('${DescriptionList[0]}','${DescriptionList[1]}','${DescriptionList[2]}');
    }
</script>
	  <%}else{%>
	     <script type="text/javascript">
                 if(window.opener != null) {
                     window.opener.setContactCode('${DescriptionList[0]}','${DescriptionList[1]}','${DescriptionList[2]}');
                     window.close();
                 }else {
                    parent.parent.GB_hide();
                    parent.parent.setContactCode('${DescriptionList[0]}','${DescriptionList[1]}','${DescriptionList[2]}');
                 }
     	 </script>
	  <%}%>
<%}%>
<html> 
	<head>
	</head>
	<%@include file="../includes/baseResources.jsp" %>
	<script type="text/javascript">
	
function getClose(){
    if(window.opener != null) {
        window.close();
    }else {
        parent.parent.GB_hide();
    }
}
function getGo(){
		if(document.commodityDescriptionLookUpForm.commodityNumber.value=="" && document.commodityDescriptionLookUpForm.commodityDescription.value==""){
		alert("Please enter either CommodityNumber or Commodity Description");
		return;
		}
	document.commodityDescriptionLookUpForm.button.value="Go";
	document.commodityDescriptionLookUpForm.submit();
 }
	</script>
	
<body class="whitebackgrnd"  >
 <html:form action="/commodityDescriptionLookUp" scope="request">
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
 <%if(session.getAttribute("ContactConfiguration")==null){ %>
 <tr class="tableHeadingNew"><td width="100%">
 <table><tr><td width="100%" class="style2">Search Criteria</td></tr>
</table></td><td><table><tr>
</tr>
</table></td></tr>
<tr><td><table>
	<tr class="textlabels">
	<td>Code</td>
	<td><html:text property="commodityNumber"></html:text></td>
	<td>Description</td>
	<td><html:text property="commodityDescription"></html:text></td>
	<td><input type="button" class="buttonStyleNew" value="Go" onclick="getGo()"></td>
	</tr>
</table>
</table>
<%}%>
<table width="100%" cellpadding="0" cellspacing="0" class="tableHeadingNew">
            <tr><td>List of Codes</td>
</table>
<%
if(commDescriptionList!=null && commDescriptionList.size()>0)
 {
 	int i=0; 
%>
<div id="divtablesty1" style="height:80%;">
<display:table name="<%=commDescriptionList%>" class="displaytagstyle" pagesize="25"  style="width:100%" id="arInquiry" sort="list" >
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
  			String commNo="";
  			String commDesc="";
  			GenericCode genericCode=(GenericCode)commDescriptionList.get(i);
  			if(genericCode.getCode()!=null){
  			   commNo=genericCode.getCode();
  			   commDesc=genericCode.getCodedesc();
  			}
  			link=editPath+"?paramId="+i+"&button="+button+"&accountNo="+commNo+"&fieldParam="+field+"&From="+fromPage;
  			%>
  	<display:column title="Code"  property="code"></display:column>
  	<display:column title="Description" ><a href="<%=link%>" ><%=commDesc%></a></display:column>
  	
<%i++;%>
</display:table>  
<%}%>
  </div>
</table>
	<html:hidden property="button"/>
	<input type="hidden" name="action" value="<%=button%>"/>
		</html:form>
	</body>
</html>

