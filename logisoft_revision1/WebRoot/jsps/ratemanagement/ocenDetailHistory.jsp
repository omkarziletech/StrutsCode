<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.LCLColoadDetailsHistory,com.gp.cong.logisoft.domain.LCLColoadMaster,java.text.DateFormat,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List CoDetailHistoryList=new ArrayList();
LCLColoadMaster lCLColoadMaster=new LCLColoadMaster();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
String sizeAOf= "";
String sizeATt= "";
String sizeBOf= "";
String sizeBTt= "";
String sizeCOf= "";
String sizeCTt= "";
String sizeDOf= "";
String sizeDTt= "";
String metric1000kg= "";
String metricCbm= "";
String metricOfMinamt= "";
String english100lb= "";
String englishCft= "";
String englishOfMinamt= "";
 if(session.getAttribute("addlclColoadMaster")!=null){
					lCLColoadMaster=(LCLColoadMaster)session.getAttribute("addlclColoadMaster");
				}
CoDetailHistoryList=dbUtil.getCoLoadHistory(lCLColoadMaster.getId());
String defaultRate="";
if(session.getAttribute("lcldefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("lcldefaultRate");
}
%>
<html> 
	<head>
		<title>JSP for AFRHForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
		<html:form action="/aFRH" scope="request">
<%--			<table width=100% border="0" cellpadding="0" cellspacing="0">--%>
<%--			<tr class="textlabels">--%>
<%--  				 <td height="12" colspan="4"  class="headerbluesmall">&nbsp;&nbsp;Lcl Co Load Rate History </td> --%>
<%--			</tr>--%>
<%--			</table>--%>
<%--			<table width="100%">--%>

 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" height="90%">Lcl Co Load Rate History</tr> 
 <td>
 
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
          		<table width="720" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        
        %>
        <display:table name="<%=CoDetailHistoryList%>" pagesize="<%=pageSize%>" defaultorder="descending" defaultsort="1" class="displaytagstyle" id="itemtable" sort="list"> 
			<%
				String cheDate="",effDate="";
				
				String weightRange=null;
				if(CoDetailHistoryList!=null && CoDetailHistoryList.size()>0)
  					{
  						LCLColoadDetailsHistory lCLColoadDetailsHistory=(LCLColoadDetailsHistory)CoDetailHistoryList.get(i); 			
  						if(lCLColoadDetailsHistory.getId()!=null)
  						{
  						//weightRange=airRatesHistory.getWeightRange().getCodedesc();
  							lCLColoadDetailsHistory.getWhoChanged();
							//airRatesHistory.getWeightRange();
							if(lCLColoadDetailsHistory.getChangedDate()!=null){
							cheDate=dateFormat.format(lCLColoadDetailsHistory.getChangedDate());
  						}
  						}
  						if(lCLColoadDetailsHistory.getSizeAOf() != null)
  						{
  						 sizeAOf = df.format(lCLColoadDetailsHistory.getSizeAOf());
  						}
  						if(lCLColoadDetailsHistory.getSizeATt() != null)
  						{
  						 sizeATt = df.format(lCLColoadDetailsHistory.getSizeATt());
  						}
  						if(lCLColoadDetailsHistory.getSizeBOf() != null)
  						{
  						 sizeBOf = df.format(lCLColoadDetailsHistory.getSizeBOf());
  						}
  						if(lCLColoadDetailsHistory.getSizeBTt() != null)
  						{
  						 sizeBTt = df.format(lCLColoadDetailsHistory.getSizeBTt());
  						}
  						if(lCLColoadDetailsHistory.getSizeCOf() != null)
  						{
  						 sizeCOf = df.format(lCLColoadDetailsHistory.getSizeCOf());
  						}
  						if(lCLColoadDetailsHistory.getSizeCTt() != null)
  						{
  						 sizeCTt = df.format(lCLColoadDetailsHistory.getSizeCTt());
  						}
  						if(lCLColoadDetailsHistory.getSizeDOf() != null)
  						{
  						 sizeDOf = df.format(lCLColoadDetailsHistory.getSizeDOf());
  						}
  						if(lCLColoadDetailsHistory.getSizeDTt() != null)
  						{
  						 sizeDTt = df.format(lCLColoadDetailsHistory.getSizeDTt());
  						}
  						if(lCLColoadDetailsHistory.getMetric1000kg() != null)
  						{
  						 metric1000kg = df.format(lCLColoadDetailsHistory.getMetric1000kg() );
  						}
  						if(lCLColoadDetailsHistory.getMetricCbm() != null)
  						{
  						 metricCbm = df.format(lCLColoadDetailsHistory.getMetricCbm());
  						}
  						if(lCLColoadDetailsHistory.getMetricOfMinamt() != null)
  						{
  						  metricOfMinamt = df.format(lCLColoadDetailsHistory.getMetricOfMinamt());
  						}
  						if(lCLColoadDetailsHistory.getEnglish100lb() != null)
  						{
  						 english100lb = df.format(lCLColoadDetailsHistory.getEnglish100lb());
  						}
  						if(lCLColoadDetailsHistory.getEnglishCft() != null)
  						{
  						 englishCft = df.format(lCLColoadDetailsHistory.getEnglishCft());
  						}
  						if(lCLColoadDetailsHistory.getEnglishOfMinamt() != null)
  						{
  						 englishOfMinamt = df.format(lCLColoadDetailsHistory.getEnglishOfMinamt());
  						}
  						if(lCLColoadDetailsHistory.getEffectiveDate()!=null){
  						effDate=dateFormat.format(lCLColoadDetailsHistory.getEffectiveDate());
  						}
  						}
			 %>
			<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Item Details Displayed,For more Items click on Page Numbers.
    				<br>
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
			<display:setProperty name="paging.banner.item_name" value="Item"/>
  			<display:setProperty name="paging.banner.items_name" value="Items"/>
  			<display:column property="id" title="" style="width:2%;visibility:hidden;color:red;"/>
 			<%if(defaultRate.equals("M"))
 			{ %>
			<display:column  title="Metric Rate/1000KGS"><%=metric1000kg%></display:column>
			<display:column  title="Metric Rate/CBM" ><%=metricCbm%> </display:column> 
			<display:column  title="Metric OfMinamt"><%=metricOfMinamt%></display:column>
			<%}else if(defaultRate.equals("E"))
			{ %>
			<display:column  title="English Rate/100lb"><%=english100lb%></display:column>
			<display:column  title="English Rate/Cft"><%=englishCft%></display:column>
			<display:column  title="English OfMinamt"><%=englishOfMinamt%></display:column>
			<%} %>
			<display:column  title="Size(A)of Ocean Freight" ><%=sizeAOf%></display:column>
			<display:column  title="Size(A)of T&T" ><%=sizeATt%></display:column>
			<display:column  title="Size(B)of Ocean Freight" ><%=sizeBOf%></display:column>  
			<display:column  title="Size(B)of T&T" ><%=sizeBTt%></display:column>
			<display:column  title="Size(C)of Ocean Freight" ><%=sizeCOf%></display:column>
			<display:column  title="Size(C)of T&T"><%=sizeCTt%></display:column>
			<display:column  title="Size(D)of Ocean Freight"><%=sizeDOf%></display:column>
			<display:column  title="Size(D)of T&T"><%=sizeDTt%></display:column>
			<%--<display:column property="measureType" title="Measure Type"></display:column>
			--%><display:column  title="Effective Date" ><%=effDate%></display:column>
			<display:column  title="Changed Date" ><%=cheDate%></display:column>
			<display:column property="whoChanged" title="Who Changed" />

  			<% i++;
  		   	   
  			%>
		     	    		
       		
		</display:table>
        
        </table></div>  
    	</td> 
   </tr>  
</table>

	   
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

