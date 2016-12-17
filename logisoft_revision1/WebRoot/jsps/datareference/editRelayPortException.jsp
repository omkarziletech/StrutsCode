<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"
	import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.PortsTemp,com.gp.cong.logisoft.domain.PortException"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	DBUtil dbUtil = new DBUtil();
	String buttonValue = "load";
	String originName = "";
	String originCode = "0";
	String destinationName = "";
	String destinationCode = "0";
	List exceptionList = null;
	List portRelayList = null;
	List originList=new ArrayList();
	List destinationList=new ArrayList();
	
	PortException exception = new PortException();
	

	if (session.getAttribute("exception") != null) {
		exception = (PortException) session.getAttribute("exception");
}
		if (exception != null && exception.getOriginId() != null) {
			originName = exception.getOriginId().getPortname();
			originCode = exception.getOriginId().getId().toString();
		}
		if (exception != null && exception.getDestinationId() != null) {
			destinationName = exception.getDestinationId().getPortname();
			destinationCode = exception.getDestinationId().getId().toString();
		}
	
	if (session.getAttribute("originList") != null) {
		originList = (List) session.getAttribute("originList");
		
	}
	if (session.getAttribute("destinationList") != null) {
		destinationList = (List) session.getAttribute("destinationList");
		
	}
	request.setAttribute("originList", dbUtil.getPortList1(originList));
	request.setAttribute("destinationList", dbUtil.getPortList2(destinationList));
	
	
	String msg = "";

	if (request.getAttribute("message") != null) {
		msg = (String) request.getAttribute("message");
	}

	if (session.getAttribute("portRelayList") != null) {
		portRelayList = (List) session.getAttribute("portRelayList");
		
	}
	
	
%>

<html>
	<head>
		<title>Relay Port Exception</title>
		<%@include file="../includes/baseResources.jsp" %>


		<script language="javascript" type="text/javascript">
	function add()
	{
	if(document.editRelayPortException.originCode.value=="0")
			{
				alert("Please select Origin Code");
				document.editRelayPortException.originCode.value="0";
				document.editRelayPortException.originCode.focus();
				return;
			}
			if(document.editRelayPortException.destinationCode.value=="0")
			{
				alert("Please select Destination Code");
				document.editRelayPortException.destinationCode.value="0";
				document.editRelayPortException.destinationCode.focus();
				return;
			}
		document.editRelayPortException.buttonValue.value="add";
   		document.editRelayPortException.submit();
 	}		
		
	function cancel()
	{
		document.editRelayPortException.buttonValue.value="cancel";
   		document.editRelayPortException.submit();
 	}	
	function submit1()
    {
    
   	  	document.editRelayPortException.buttonValue.value="originselected";
  	 	document.editRelayPortException.submit();
    }	
	function submit2()
    {
   
   	  	document.editRelayPortException.buttonValue.value="destinationselected";
  	 	document.editRelayPortException.submit();
    }	
	 
			function confirmdelete(obj)
	{
	var rowindex=obj.parentNode.parentNode.rowIndex;
	 var x=document.getElementById('portexceptiontable').rows[rowindex].cells;	
	
  document.editRelayPortException.index.value=rowindex-1;
		document.editRelayPortException.buttonValue.value="delete";
    	var result = confirm("Are you sure you want to delete this Port Exception");
		if(result)
		{
   			document.editRelayPortException.submit();
   		}	
   	}
</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/editRelayPortException" name="editRelayPortException" type="com.gp.cong.logisoft.struts.form.EditRelayPortExceptionForm" scope="request">
			<font color="blue"><h4>
					<%=msg%>
				</h4>
			</font>
		<div align="right" > <input type="button" class="buttonStyleNew" value="Previous" onclick="cancel()"/></div>
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
				<tr class="tableHeadingNew"><td colspan="2">Port Details</td>
				  <td colspan="2" align="right"><input type="button"  class="buttonStyleNew" value="Add" onclick="add()"/> </td>
					</tr>
						<tr>
							<td width="100" class="style2">
								Origin*
							</td>
							<td><html:select property="originCode"  value="<%=originCode%>" styleClass="selectboxstyle">
									<html:optionsCollection name="originList" /></html:select></td>
							<td class="style2">Destination*</td>
							<td><html:select property="destinationCode"  value="<%=destinationCode%>" styleClass="selectboxstyle" >
									<html:optionsCollection name="destinationList" /></html:select></td >
						</tr>
							</table>
						<br>
						<table width="100%" class="tableBorderNew">
							<tr class="tableHeadingNew">
								List of Port Exceptions
							</tr>
							<tr >
								<td>
								<%
								int i = 0;
								%>
								<display:table name="<%=portRelayList%>" pagesize="<%=pageSize%>"
									class="displaytagstyle" id="portexceptiontable">
									<display:setProperty name="paging.banner.some_items_found">
										<span class="pagebanner"> <font color="blue">{0}</font>
											Port Exceptions Displayed,For more Data click on Page Numbers. <br>
										</span>
									</display:setProperty>
									<display:setProperty name="paging.banner.one_item_found">
										<span class="pagebanner"> One {0} displayed. Page
											Number </span>
									</display:setProperty>
									<display:setProperty name="paging.banner.all_items_found">
										<span class="pagebanner"> {0} {1} Displayed, Page
											Number </span>
									</display:setProperty>
									<display:setProperty name="basic.msg.empty_list">
										<span class="pagebanner"> No Records Found. </span>
									</display:setProperty>
									<display:setProperty name="paging.banner.placement"
										value="bottom" />
									<display:setProperty name="paging.banner.item_name"
										value="Port Exception" />
									<display:setProperty name="paging.banner.items_name"
										value="Port Exception" />
                              <%
                                     PortsTemp orgport = null;
                                     PortsTemp destport=null;
                                     String orgportCode="";
                                     String orgportName="";
                                     String destportCode="";
                                     String destportName="";
                                     if(portRelayList!=null && portRelayList.size()>0)
                                     {
                                     	PortException portException=(PortException)portRelayList.get(i);
                                     	if(portException!=null)
                                     	{
                                      	  orgport= portException.getOriginId();
                                      	  destport=portException.getDestinationId();
                                      	  if(orgport!=null)
                                      	  {
                                     	  		 orgportCode = orgport.getShedulenumber();
                                     	  		 orgportName = orgport.getPortname();
                                     	 		}
                                     		  if(destport!=null)
                                      	  {
                                     	   		destportCode = destport.getShedulenumber();
                                     	   		destportName = destport.getPortname();
                                     	  	}
                                     		}
                                     }
                               %>
									<display:column title="Origin Code"><%=orgportCode%></display:column>
									
									<display:column title="Origin Name" ><%=orgportName%></display:column>
								
									<display:column title="Destination Code"><%=destportCode%></display:column>
								
									<display:column title="Destination Name" ><%=destportName%></display:column>
									
									 <display:column><img name="<%=i%>" src="<%=path%>/img/toolBar_delete_hover.gif" border="0" id="delete" onclick="confirmdelete(this)"/></display:column>
									<%
									i++;
									%>
								</display:table>
								</td>
							</tr>
						</table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
			<html:hidden property="index" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

