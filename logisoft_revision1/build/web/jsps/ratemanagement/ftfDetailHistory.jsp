<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FTFDetailsHistory,com.gp.cong.logisoft.domain.FTFMaster,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%@include file="../includes/jspVariables.jsp" %>
 
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List ftfDetailHistoryList=new ArrayList();
FTFMaster ftfMaster=new FTFMaster();
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
 if(session.getAttribute("addftfMaster")!=null){
					ftfMaster=(FTFMaster)session.getAttribute("addftfMaster");
				}
ftfDetailHistoryList=dbUtil.getftfHistory(ftfMaster.getId());

String defaultRate="";
if(session.getAttribute("ftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("ftfdefaultRate");
}
%>
<html> 
	<head>
		<title>JSP for AFRHForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
		<html:form action="/aFRH" scope="request">
			<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew"> Lcl Co Load Rate History </tr>
			<tr>
			  <td>
			  
			<table width="100%">
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable" >
          		<table width="720" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        
        %>
        <display:table name="<%=ftfDetailHistoryList%>" pagesize="<%=pageSize%>"  
         class="displaytagstyle" defaultorder="descending" defaultsort="1" sort="list" id="airweightratestable"> 
			<%
				String cheDate="",effDate="";
				
				//String weightRange=null;
				if(ftfDetailHistoryList!=null && ftfDetailHistoryList.size()>0)
  					{
  						FTFDetailsHistory ftfDetailsHistory=(FTFDetailsHistory)ftfDetailHistoryList.get(i); 			
  						if(ftfDetailsHistory.getWhoChanged()!=null)
  						{
  							ftfDetailsHistory.getWhoChanged();
  						}
  						if(ftfDetailsHistory.getChangedDate()!=null){
							cheDate=dateFormat.format(ftfDetailsHistory.getChangedDate());
  						}
  						if(ftfDetailsHistory.getEffectiveDate()!=null){
							effDate=dateFormat.format(ftfDetailsHistory.getEffectiveDate());
  						}
  						if(ftfDetailsHistory.getSizeAOf() != null)
  						{
  						 sizeAOf = df.format(ftfDetailsHistory.getSizeAOf());
  						}
  						if(ftfDetailsHistory.getSizeATt() != null)
  						{
  						 sizeATt = df.format(ftfDetailsHistory.getSizeATt());
  						}
  						if(ftfDetailsHistory.getSizeBOf() != null)
  						{
  						 sizeBOf = df.format(ftfDetailsHistory.getSizeBOf());
  						}
  						if(ftfDetailsHistory.getSizeBTt() != null)
  						{
  						 sizeBTt = df.format(ftfDetailsHistory.getSizeBTt());
  						}
  						if(ftfDetailsHistory.getSizeCOf() != null)
  						{
  						 sizeCOf = df.format(ftfDetailsHistory.getSizeCOf());
  						}
  						if(ftfDetailsHistory.getSizeCTt() != null)
  						{
  						 sizeCTt = df.format(ftfDetailsHistory.getSizeCTt());
  						}
  						if(ftfDetailsHistory.getSizeDOf() != null)
  						{
  						 sizeDOf = df.format(ftfDetailsHistory.getSizeDOf());
  						}
  						if(ftfDetailsHistory.getSizeDTt() != null)
  						{
  						 sizeDTt = df.format(ftfDetailsHistory.getSizeDTt());
  						}
  						if(ftfDetailsHistory.getMetric1000kg() != null)
  						{
  						 metric1000kg = df.format(ftfDetailsHistory.getMetric1000kg() );
  						}
  						if(ftfDetailsHistory.getMetricCbm() != null)
  						{
  						 metricCbm = df.format(ftfDetailsHistory.getMetricCbm());
  						}
  						if(ftfDetailsHistory.getMetricOfMinamt() != null)
  						{
  						  metricOfMinamt = df.format(ftfDetailsHistory.getMetricOfMinamt());
  						}
  						if(ftfDetailsHistory.getEnglish100lb() != null)
  						{
  						 english100lb = df.format(ftfDetailsHistory.getEnglish100lb());
  						}
  						if(ftfDetailsHistory.getEnglishCft() != null)
  						{
  						 englishCft = df.format(ftfDetailsHistory.getEnglishCft());
  						}
  						if(ftfDetailsHistory.getEnglishOfMinamt() != null)
  						{
  						 englishOfMinamt = df.format(ftfDetailsHistory.getEnglishOfMinamt());
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
			
			<display:column property="measureType" title="Express Min Amt"></display:column>
			<display:column  title="Effective Date" ><%=effDate%></display:column>
			<display:column  title="Changed Date" ><%=cheDate%></display:column>

  			<% i++;
  		   	   
  			%>
		     	    		
       		
		</display:table>
        
        </table></div>  
    	</td> 
     </tr>  
   </table>
	            </td>
			  </tr> 
			</table>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

