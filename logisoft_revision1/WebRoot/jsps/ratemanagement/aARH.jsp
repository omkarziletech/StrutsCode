<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.AirRatesHistory,com.gp.cong.logisoft.domain.StandardCharges,java.text.DateFormat,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
 <%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List airRatesHistoryList=new ArrayList();
StandardCharges airRatesObj1= new StandardCharges();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
if(session.getAttribute("standardCharges")!=null )
{
    airRatesObj1=(StandardCharges)session.getAttribute("standardCharges");
}
airRatesHistoryList=dbUtil.getAirRatesHistory(airRatesObj1.getId());

%>
<html> 
	<head>
		<title>JSP for AFRHForm form</title>
<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
		<html:form action="/aFRH" scope="request">
<%--			<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">--%>
<%--			<tr class="tableHeader">--%>
<%--  				 <td height="12" colspan="4"  >&nbsp;&nbsp;Air Frieght Rate History </td> --%>
<%--			</tr>--%>
<%--			</table>--%>
			<table width="100%" class="tableBorderNew">
			<tr class="tableHeadingNew">Air Frieght Rate History </tr>
  			<tr>
        		<td align="left" ><div id="divtablesty1"  class="scrolldisplaytable">
          		<table width="720" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        
        %>
        <display:table name="<%=airRatesHistoryList%>" pagesize="5" class="displaytagstyle"  sort="list" id="airweightratestable"> 
			<%
				String cheDate="";
				
				String weightRange=null;
				String generalRate = "";
				String generalMinAmt = "";
				String expressRate = "";
				String expressMinAmt = "";
				String deferredRate = "";
				String deferredMinAmt = "";
				if(airRatesHistoryList!=null && airRatesHistoryList.size()>0)
  					{
  						AirRatesHistory airRatesHistory=(AirRatesHistory)airRatesHistoryList.get(i); 			
  						if(airRatesHistory.getId()!=null)
  						{
  						weightRange=airRatesHistory.getWeightRange().getCodedesc();
  							airRatesHistory.getWhoChanged();
							airRatesHistory.getWeightRange();
							if(airRatesHistory.getChangedDate()!=null){
							cheDate=dateFormat.format(airRatesHistory.getChangedDate());
  						}
  						}
  						if(airRatesHistory.getGeneralRate() != null)
  						{
  							generalRate = df.format(airRatesHistory.getGeneralRate());;
  						}
  						if(airRatesHistory.getGeneralMinAmt() != null)
  						{
  						generalMinAmt = df.format(airRatesHistory.getGeneralMinAmt());
  						}
  						
  						if(airRatesHistory.getExpressRate() != null)
  						{
  						expressRate = df.format(airRatesHistory.getExpressRate());
  						}
  						if(airRatesHistory.getExpressMinAmt() != null)
  						{
  						expressMinAmt = df.format(airRatesHistory.getExpressMinAmt());
  						}
  						if(airRatesHistory.getDeferredRate() != null)
  						{
  						deferredRate = df.format(airRatesHistory.getDeferredRate());
  						}
  						if(airRatesHistory.getDeferredMinAmt() != null)
  						{
  						deferredMinAmt = df.format(airRatesHistory.getDeferredMinAmt());
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
			<display:column  title="Weight Range"  ><%=weightRange %></display:column>
			<display:column  title="General Rate" ><%=generalRate%></display:column>
			<display:column  title="General Min Amt" ><%=generalMinAmt%></display:column>  
			<display:column  title="Express Rate" ><%=expressRate%></display:column>
			<display:column  title="Express Min Amt"><%=expressMinAmt%></display:column>
			<display:column  title="Deferred Rate"><%=deferredRate%></display:column>
			<display:column  title="Deferred Min Amt" ><%=deferredMinAmt%></display:column>
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

			