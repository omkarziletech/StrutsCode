<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"
	import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.PortsTemp,com.gp.cong.logisoft.domain.PortException,com.gp.cong.logisoft.domain.RelayOrigin"%>

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
	List originList=null; 
	List destinationList=null;
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
	//request.setAttribute("originList",orgList);
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
	if(document.relayPortException.originCode.value=="0")
			{
				alert("Please select Origin Code");
				document.relayPortException.originCode.value="0";
				document.relayPortException.originCode.focus();
				return;
			}
			if(document.relayPortException.destinationCode.value=="0")
			{
				alert("Please select Destination Code");
				document.relayPortException.destinationCode.value="0";
				document.relayPortException.destinationCode.focus();
				return;
			}
		document.relayPortException.buttonValue.value="add";
   		document.relayPortException.submit();
 	}		
		
	function cancel()
	{
		document.relayPortException.buttonValue.value="cancel";
   		document.relayPortException.submit();
 	}	
	function submit1()
    {
    
   	  	document.relayPortException.buttonValue.value="originselected";
  	 	document.relayPortException.submit();
    }	
	function submit2()
    {
   
   	  	document.relayPortException.buttonValue.value="destinationselected";
  	 	document.relayPortException.submit();
    }	
	 function openOrigin() 
      {
        	mywindow=window.open("<%=path%>/jsps/datareference/addOrigin.jsp","","width=650,height=450");
           	mywindow.moveTo(200,180);
      }    
	 function openDestination() 
      {
        	mywindow=window.open("<%=path%>/jsps/datareference/addDestination.jsp","","width=650,height=450");
           	mywindow.moveTo(200,180);
      } 
	function confirmdelete(obj)
	{
	var rowindex=obj.parentNode.parentNode.rowIndex;
	 var x=document.getElementById('portexceptiontable').rows[rowindex].cells;	
	
  document.relayPortException.index.value=rowindex-1;
		document.relayPortException.buttonValue.value="delete";
    	var result = confirm("Are you sure you want to delete this Port Exception");
		if(result)
		{
   			document.relayPortException.submit();
   		}	
   	}
</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/relayPortException" name="relayPortException" type="com.gp.cong.logisoft.struts.form.RelayPortExceptionForm" scope="request">
			<font color="blue" size="4"><%=msg%></font>
			<table width="560"  border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="560">
						<table>
							<tr>
								<td width="460" height="30" class="headerbluelarge">
									Port Combination Exceptions
								</td>
								<td>
								</td>
							
								<td width="80" align="right">
									<input type="button" class="buttonStyleNew" value="Previous" onclick="cancel()"/>
								</td>
								
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td height="15" class="headerbluesmall">
						&nbsp;&nbsp;Port Details
					</td>
				</tr>
				
				<tr>
					<td>
						<table width="560" border="0" cellspacing="0" cellpadding="0">
							
							<tr>
								<td width="560">
									<table width="560" border="0" cellspacing="0">
										<tr>
											<td width="100" class="style2">
												Origin*
											</td>
											
											<td width="20">
											</td>
											<td width="100" class="style2">
												Destination*
											</td>
											

										</tr>
										<tr>
											<td width="100">
												<html:select property="originCode"  value="<%=originCode%>"
													 styleClass="selectboxstyle">
													<html:optionsCollection name="originList" />
												</html:select>
											</td>
										
											<td width="20">
											</td>
											<td width="100">
												<html:select property="destinationCode"  value="<%=destinationCode%>"
													 styleClass="selectboxstyle">
													<html:optionsCollection name="destinationList" />
												</html:select>
											</td>
											
										<td width="50" align="right">
									<img src="<%=path%>/img/add.gif" border="0"
										onclick="add()" />
								</td>
										</tr>
							</table>
						</td>
					</tr>
					<tr>
								<td height="15"></td>
					</tr>
						
				<tr>
					<td height="15" width="560" class="headerbluesmall">
																						&nbsp;&nbsp;List of Port Exceptions
																						</td>
				</tr>	
				<%if(portRelayList!=null && portRelayList.size()>0)
				{ %>	
				<tr>
			<td width="560" valign="top">
						<table width="560">
							<tr>
								
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
									<display:column></display:column>
									<display:column></display:column>
									<display:column title="Origin Name" ><%=orgportName%></display:column>
									<display:column></display:column>
									<display:column></display:column>
									<display:column title="Destination Code"><%=destportCode%></display:column>
									<display:column></display:column>
									<display:column></display:column>
									<display:column title="Destination Name" ><%=destportName%></display:column>
									<display:column></display:column>
									<display:column></display:column>
									 <display:column><img name="<%=i%>" src="<%=path%>/img/toolBar_delete_hover.gif" border="0" id="delete" onclick="confirmdelete(this)"/></display:column>
									

									
									<%
									i++;
									%>
								</display:table>
								</td>
							</tr>
							<%} %>
						</table>
					</td>
				</tr>
			</table>
			</td>
			</tr></table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
			<html:hidden property="index" />
		</html:form>
	</body>
	
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

