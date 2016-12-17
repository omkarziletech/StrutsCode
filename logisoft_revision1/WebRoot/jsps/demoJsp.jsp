<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,java.util.*,java.text.SimpleDateFormat,com.gp.cvst.logisoft.domain.*"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.FclBlConstants"/>
<jsp:directive.page import="java.text.DateFormat"/>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>

<% 
String path = request.getContextPath();
List crList=(List)request.getAttribute(FclBlConstants.BLCORRECTIONLIST);
%>
<html> 
	<head>
		<title>JSP for FclBlCorrectionsForm form</title>
		<%@include file="../includes/baseResources.jsp"%>
		<script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
		<script type='text/javascript' src='/logisoft/dwr/interface/FclBlCorrectionsBC.js'></script>
		<script language="javascript" src="<%=path%>/js/common.js"/></script>
		<script type="text/javascript">
    function update() {alert("getData.....");
        FclBlCorrectionsBC.getData(function(data) {
            dwr.util.setValue("correctionDisplyTable", data, { escapeHtml:false });
        });
    }
    update("");
</script>


		<%@include file="../includes/resources.jsp" %>
	</head>
	<body onload="update()">
		<%int i=0; %>	    
<div  style="height:80%;">
 <display:table name="<%=crList%>" id="correctionDisplyTable" pagesize="10" 
            class="displaytagstyle" sort="list"  style="width:100%" defaultorder="ascending" defaultsort="1">
	      <display:setProperty name="paging.banner.some_items_found">
	        <span class="pagebanner">
	         <font color="blue">{0}</font> Search Quotation details displayed,For more code click on page numbers.
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
	      <display:setProperty name="basic.msg.empty_list">
	        <span class="pagebanner">
	              No Records Found.
	       </span> 
	      </display:setProperty>
	      <display:setProperty name="paging.banner.placement" value="bottom" />
		  <display:setProperty name="paging.banner.item_name" value="Corrections"/>
  		  <display:setProperty name="paging.banner.items_name" value="Corrections"/>
	      
			      <% 
			         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			      	 String correctionType="",date="",sailDate="",blNumber="";
			      	 String correctionCode="";
			      	 String approved="";
			      	 String noticeNo="";
			      	 String fileNo="";
			      	 if(crList!=null && !crList.isEmpty()){
			      	 FclBlCorrections fclBlCorrections=(FclBlCorrections)crList.get(i);
				      	 if(fclBlCorrections.getCorrectionType()!=null){
				      	 	correctionType =fclBlCorrections.getCorrectionType().getCode();
				      	 }
			      		 if(fclBlCorrections.getDate()!=null && !fclBlCorrections.getDate().equals("")){
			      		 	date=dateFormat.format(fclBlCorrections.getDate());
			      		 }	
			      		 if(fclBlCorrections.getSailDate()!=null && !fclBlCorrections.getSailDate().equals("")){
			      		   sailDate=dateFormat.format(fclBlCorrections.getSailDate());
			      		 }
			      		 if(fclBlCorrections.getCorrectionCode()!=null){
			      		   correctionCode=fclBlCorrections.getCorrectionCode().getCode();
			      		 }
			      		 if(fclBlCorrections.getBlNumber()!=null){
			      		   blNumber=fclBlCorrections.getBlNumber();
			      		
			      		 }
			      		
			      		 if(fclBlCorrections.getFileNo()!=null){
			      		 	fileNo=String.valueOf(fclBlCorrections.getFileNo());
			      		 	}
			      		 if(fclBlCorrections.getStatus()!=null){
			      		 approved=fclBlCorrections.getStatus();
			      		 }
			      		 if(fclBlCorrections.getNoticeNo()!=null){
			      		 noticeNo=fclBlCorrections.getNoticeNo();
			      		 }
			      }
				%>
	   <display:column title="FileNo"  sortable="true"><%=fileNo%></display:column>
	    <display:column title="BL No" property="blNumber" sortable="true"></display:column>
	    <display:column title="Notice_#" property="noticeNo"  sortable="true"></display:column>	  
	    <display:column title="User"    property="userName"  sortable="true"></display:column>	
		<display:column title="Date"    sortable="true"><%=date%></display:column>
		<display:column title="P" property="prepaidCollect"  sortable="true"></display:column>	  
		<display:column title="Sail Date" sortable="true"><%=sailDate%></display:column>
		<display:column title="C/N Code"  sortable="true"><%=correctionCode%></display:column>
		<display:column title="Approval"   sortable="true" property="approval"></display:column>
		<display:column title="F"   sortable="true" property="isFax"></display:column>
		<display:column title="P"   sortable="true" property="isPost"></display:column>
		<display:column title="C-Type"   sortable="true"><%=correctionType%></display:column>
		<display:column title="Actions">
		<c:choose>
			<c:when test="${not empty FclBlCorrectionForm.temp}">
<%
		if(!approved.equals("Approved")){ 
%>
				<span class="hotspot" onmouseover="tooltip.show('Edit',null,event);" onmouseout="tooltip.hide();">
			 	  <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editCorrectionRecord('<%=noticeNo%>','<%=blNumber%>','')" />
			 </span>
<%
		}else{
%>
				 <span class="hotspot" onmouseover="tooltip.show('view',null,event);" onmouseout="tooltip.hide();">
				 	  <img src="<%=path%>/img/icons/container_obj.gif" border="0" onclick="viewCorrectionRecord('<%=noticeNo%>','<%=blNumber%>')" />
				 </span>
<%
		 } 
%>
			</c:when>
			<c:otherwise>
			<span class="hotspot" onmouseover="tooltip.show('view',null,event);" onmouseout="tooltip.hide();">
			 	  <img src="<%=path%>/img/icons/container_obj.gif" border="0" onclick="viewCorrectionRecord('<%=noticeNo%>','<%=blNumber%>')" />
			 </span>
			 <%if(approved.equals("")){
			 %>
			  <span class="hotspot" onmouseover="tooltip.show('Approve',null,event);" onmouseout="tooltip.hide();">
		 	  <img src="<%=path%>/img/icons/approve.gif" border="0"  onclick="approveUse('<%=noticeNo%>','<%=blNumber%>')"/>
		 	</span>
			 <%
			 } %>
		 <span class="hotspot" onmouseover="tooltip.show('Email',null,event);" onmouseout="tooltip.hide();">
		 	  <img src="<%=path%>/img/icons/send.gif" border="0" />
		 </span>
		 <span class="hotspot" onmouseover="tooltip.show('Fax',null,event);" onmouseout="tooltip.hide();">
		 	  <img src="<%=path%>/img/icons/print.gif" border="0" />
		 </span>
			</c:otherwise>
			</c:choose>
		 
		 
		</display:column>
		<%i++; %>
	  </display:table>    
	</div>
  
  </body>
  <%@include file="../includes/baseResourcesForJS.jsp" %>

</html>

