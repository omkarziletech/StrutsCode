<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"
	import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.RelayOrigin,com.gp.cong.logisoft.domain.RelayDestination"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RelayInquiry" />
<jsp:directive.page import="com.gp.cong.logisoft.domain.PortsTemp" />
<jsp:directive.page import="com.gp.cong.logisoft.domain.PortException"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<jsp:useBean id="print"class="com.gp.cong.logisoft.struts.form.EditRelayForm"></jsp:useBean>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	DBUtil dbUtil = new DBUtil();
	String buttonValue = "load";
	String poldesc = "";
	String polid = "";
	String poddesc = "";
	String podid = "";
	List itemList = null;
	List originList = null;
	List destinationList = null;
	
	String status = "";
	String ttFromPolToPod = "";
	String cutOffDays = "";
	String hourOfDay = "";
	String printOnSs = "";
	RelayInquiry check=new RelayInquiry();
	check.setDestCheck("checked");
	request.setAttribute("myCheck",check);
	List portRelayList=null;
	
	RelayInquiry relay = new RelayInquiry();
	
	if(request.getAttribute("editorigin")!=null && request.getAttribute("editorigin").equals("editorigin"))
	{
	%>
	<script type="text/javascript">
mywindow=window.open("<%=path%>/jsps/datareference/editOrigin.jsp?editOrigin="+"list","","width=650,height=150");
      mywindow.moveTo(200,180);
</script>
	<% 
	}
	if(request.getAttribute("editdestination")!=null && request.getAttribute("editdestination").equals("editdestination"))
	{
	%>
	<script type="text/javascript">
mywindow=window.open("<%=path%>/jsps/datareference/editDestination.jsp?editdestination="+"list","","width=650,height=150");
      mywindow.moveTo(200,180);
</script>
	<% 
	}
	if (session.getAttribute("relay") != null) {
		relay = (RelayInquiry) session.getAttribute("relay");
		
		if (relay.getStatus() != null) {
			status = relay.getStatus();
			
		}
		if (relay.getTtFromPolToPod() != null) {
			ttFromPolToPod = relay.getTtFromPolToPod().toString();
		}
		if (relay.getCutOffDays() != null) {
			cutOffDays = relay.getCutOffDays().toString();
		}
		if (relay.getHourOfDay() != null) {
			hourOfDay = relay.getHourOfDay().toString();
		}
		printOnSs= relay.getPrintOnSs();
					if(printOnSs!=null && printOnSs.equals("Y"))
					  {
					  print.setPrintOnSs("on");
					  }
					   else
					   {
					    print.setPrintOnSs("off");
					   }
	
		}
		if (relay != null && relay.getPolCode() != null) {
			poldesc = relay.getPolCode().getPortname();
		if(relay.getPolCode()!=null)
		{
			polid = relay.getPolCode().getShedulenumber();
		}	
		}
		if (relay != null && relay.getPodCode() != null) {
			poddesc = relay.getPodCode().getPortname();
				if(relay.getPodCode()!=null)
		{
			 podid= relay.getPodCode().getShedulenumber();
			}	
		}
		
	if(relay!=null)
{
List orgnList=new ArrayList();
if(session.getAttribute("originList")==null)
{
	if(relay.getOriginSet()!=null)
	{
		Iterator iter=relay.getOriginSet().iterator();
		while(iter.hasNext())
		{
			RelayOrigin rlyorgn=(RelayOrigin)iter.next();
			orgnList.add(rlyorgn);
		}
	}	
	session.setAttribute("originList",orgnList);
	
}
 
}
if(relay!=null)
{
List dstList=new ArrayList();
if(session.getAttribute("destinationList")==null)
{
if(relay.getDestinationSet()!=null)
{
Iterator iter=relay.getDestinationSet().iterator();
while(iter.hasNext())
{
RelayDestination rlydest=(RelayDestination)iter.next();
dstList.add(rlydest);
}
}	
 session.setAttribute("destinationList",dstList);
}
}
if(relay!=null)
{
List excList=new ArrayList();
if(session.getAttribute("portRelayList")==null)
{
if(relay.getPortExceptionSet()!=null)
{
Iterator iter=relay.getPortExceptionSet().iterator();
while(iter.hasNext())
{
PortException rlyexc=(PortException)iter.next();
excList.add(rlyexc);
}
}	
 session.setAttribute("portRelayList",excList);
}
}
	
	request.setAttribute("statuslist", dbUtil.getStatusListForRelay());
	String msg = "";

	
	if (request.getAttribute("message") != null)
	{
		msg = (String) request.getAttribute("message");
	}
	String modify = null;
	if (session.getAttribute("modifyforrelay") != null) {
		modify = (String) session.getAttribute("modifyforrelay");

	}

	// Name:Rohith Date:12/10/2007 (mm/dd/yy)  ----> Is view only when page is locked

	if (session.getAttribute("view") != null)
	{
		modify = (String) session.getAttribute("view");
	}

	if (session.getAttribute("originList") != null) {
		originList = (List) session.getAttribute("originList");
	}
	if (session.getAttribute("destinationList") != null) {
		destinationList = (List) session.getAttribute("destinationList");
	}
//	RelayInquiry relay1 = new RelayInquiry();
//relay.setPrintOnSs("Y");
request.setAttribute("print",relay);
String editPath=path+"/editRelay.do";
if(session.getAttribute("portRelayList") != null)
{
	portRelayList=(List)session.getAttribute("portRelayList");
}
%>

<html>
	<head>
	<%@include file="../includes/resources.jsp" %>
		<title>SearchMenu/Action</title>
		<%@include file="../includes/baseResources.jsp" %>
                <%@include file="../fragment/formSerialize.jspf"  %>


		<script language="javascript" type="text/javascript">
		
		function save()
		{
			if(document.editRelay.pol.value=="0")
			{
				alert("Please select POL Code");
				document.editRelay.pol.value="";
				document.editRelay.pol.focus();
				return;
			}
			if(document.editRelay.pod.value=="0")
			{
				alert("Please select POD Code");
				document.editRelay.pod.value="";
				document.editRelay.pod.focus();
				return;
			}
			if(IsNumeric(document.editRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.editRelay.ttFromPolToPod.value="";
    				document.editRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.editRelay.cutOffDays.value="";
    				document.editRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.editRelay.hourOfDay.value="";
    				document.editRelay.hourOfDay.focus();
   					return;
  				} 
			 if(document.editRelay.comment.value!="" && document.editRelay.comment.value.length>80)
			 {
			 		alert("Comment should be within 80 characters.");
  					document.editRelay.comment.value="";
    				document.editRelay.comment.focus();
   					return;
  				} 
			 document.editRelay.buttonValue.value="save";
			 
			 document.editRelay.submit();
   		
 	}
 		function openPortException()
	{
		document.editRelay.buttonValue.value="addport";
   		document.editRelay.submit();
 	}	
	
	function submit1()
    {
   		if(IsNumeric(document.editRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.editRelay.ttFromPolToPod.value="";
    				document.editRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.editRelay.cutOffDays.value="";
    				document.editRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.editRelay.hourOfDay.value="";
    				document.editRelay.hourOfDay.focus();
   					return;
  				} 
   	  	document.editRelay.buttonValue.value="polselected";
  	 	document.editRelay.submit();
    }	
	function submit2()
    {
    if(IsNumeric(document.editRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.editRelay.ttFromPolToPod.value="";
    				document.editRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.editRelay.cutOffDays.value="";
    				document.editRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.editRelay.hourOfDay.value="";
    				document.editRelay.hourOfDay.focus();
   					return;
  				} 
   		 
   	  	document.editRelay.buttonValue.value="podselected";
  	 	document.editRelay.submit();
    }	
	 function openOrigin() 
      {
      		if(IsNumeric(document.editRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.editRelay.ttFromPolToPod.value="";
    				document.editRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.editRelay.cutOffDays.value="";
    				document.editRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.editRelay.hourOfDay.value="";
    				document.editRelay.hourOfDay.focus();
   					return;
  				} 
  		window.open('<%=path%>/jsps/datareference/editOrigin.jsp?relay='+'add','openOrigin', 'width=600,height=150,scrollbars=yes');
  		document.editRelay.submit();
      }    
	 function openDestination() 
      {
      if(IsNumeric(document.editRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.editRelay.ttFromPolToPod.value="";
    				document.editRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.editRelay.cutOffDays.value="";
    				document.editRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.editRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.editRelay.hourOfDay.value="";
    				document.editRelay.hourOfDay.focus();
   					return;
  				} 
  				window.open('<%=path%>/jsps/datareference/editDestination.jsp?relay='+'add','openDestination', 'width=650,height=150,scrollbars=yes')
  				document.editRelay.submit();
      }  
      
 function confirmorgdelete(obj)
	{
	
	var rowindex=obj.parentNode.parentNode.rowIndex;
	
	 var x=document.getElementById('origintable').rows[rowindex].cells;	
	
  document.editRelay.ind.value=rowindex-1;
		document.editRelay.buttonValue.value="orgdelete";
    	var result = confirm("Are you sure you want to delete this origin ");
		if(result)
		{
   			document.editRelay.submit();
   		}	
   	}	
   	function confirmdestdelete(obj)
	{
	var rowindex=obj.parentNode.parentNode.rowIndex;
	 var x=document.getElementById('destinationtable').rows[rowindex].cells;	
	
  document.editRelay.inx.value=rowindex-1;
		document.editRelay.buttonValue.value="destdelete";
    var result = confirm("Are you sure you want to delete this Destination ");
		if(result)
		{
   			document.editRelay.submit();
   		}	
   	}	
   	
   	function defaultValue(status,ttFromPolToPod,cutOffDays,hourOfDay,printOnSs,modify,msg)
   	{
   		disabled(modify,msg);
			document.editRelay.ttFromPolToPod.value=ttFromPolToPod;
			document.editRelay.status.value=status;
			document.editRelay.cutOffDays.value=cutOffDays;
			document.editRelay.hourOfDay.value=hourOfDay;
			document.editRelay.printOnSs.value=printOnSs;
   		
   	}
   		function confirmdelete()
	{
		document.editRelay.buttonValue.value="delete";
    	var result = confirm("Are you sure you want to delete this Relay");
		if(result)
		{
   			document.editRelay.submit();
   		}	
   	}
   	 function confirmnote()
	{
		document.editRelay.buttonValue.value="note";
		document.editRelay.submit();
   	}	
   	function cancelbtn(val)
  	{
   	 if(val==0 || val==3 )
		{
			document.editRelay.buttonValue.value="cancelview";
		}
	 else
		{
		  var result = confirm("Would you like to save the changes?");
		  if(result)
		  {
		   save();
		  }
   			document.editRelay.buttonValue.value="cancel";
  		}
  		 document.editRelay.submit();
    }
   
    
  function disabled(val1,val2)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id != "cancel" &&  imgs[k].id!="note")
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue" && input[i].name!="polText" && input[i].name!="podText")
	 		{
	  			input[i].readOnly=true;
                input[i].style.color="blue";

	  		}
  	 	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
  	 	document.getElementById("save").style.visibility = 'hidden';
  	 	document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 	if(val1==3  && val2!="")
		{
			alert(val2);
			
		}		
    }
    	function checkbox()
{
if(document.getElementById("myCheck").checked==true)
{
document.editRelay.buttonValue.value="check";
document.editRelay.submit();
}
}
function limitText(limitField, limitCount, limitNum) {
	limitField.value = limitField.value.toUpperCase();
if (limitField.value.length > limitNum) {
limitField.value = limitField.value.substring(0, limitNum);
} else {
limitCount.value = limitNum - limitField.value.length;
 }
 
}
function printon(){
    if(document.editRelay.printOnSs.checked)
    {
     
    document.editRelay.printOnSs.value="Y";
     
     document.editRelay.printOnSs.focus();
    
    return false;
    }
        }
</script>
<script language="javascript">
            start = function(){
                serializeForm();
              }
            window.onload = start;
</script>
</head>
<body class="whitebackgrnd" onload="defaultValue('<%=status%>','<%=ttFromPolToPod%>','<%=cutOffDays%>','<%=hourOfDay%>','<%=printOnSs%>','<%=modify%>','<%=msg%>')" onkeydown="preventBack()">
<html:form action="/editRelay" name="editRelay" type="com.gp.cong.logisoft.struts.form.EditRelayForm" scope="request">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew"><td> Retay Details</td>
		<td align="right">	<input type="button" class="buttonStyleNew" value="Save" id="save" onclick="save()"/>
		<input type="button" class="buttonStyleNew" value="Delete" id="delete" onclick="confirmdelete()"/>
		<input type="button" class="buttonStyleNew" value="Go Back" id="cancel" onclick="cancelbtn(<%=modify%>)"/>
		<%if(portRelayList!=null && portRelayList.size()>0)
		{ %>
			<input type="button" class="buttonStyleNew" value="Port Exception"  style="width:90px;background:red" onclick="openPortException()"/>
		<%}
		else
		{ %>
		<input type="button" class="buttonStyleNew" value="Port Exception"  style="width:90px;" onclick="openPortException()"/>
		<%} %>
		<input type="button" class="buttonStyleNew" value="Note" onclick="confirmnote()" disabled="true"/></td>
	</tr>
	<tr>
	<td colspan="2">
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
						<tr>
								<td class="style2">
									POL<font color="red">*</font>
								</td>
								<td><html:text property="pol" 
										value="<%=polid%>" readonly="true" styleClass="varysizeareahighlightgrey" style="width:215px">
										
									</html:text>
								<td class="style2">
									Description
								</td>
								<td>
									<html:text property="polText" value="<%=poldesc%>" style="width:215px"
										 readonly="true" styleClass="varysizeareahighlightgrey" />
								</td>
								</tr><tr>
								<td class="style2">
									POD<font color="red">*</font>
								</td>
								<td><html:text property="pod" 
										value="<%=podid%>" readonly="true" styleClass="varysizeareahighlightgrey" style="width:215px">
										
									</html:text>
								<td class="style2">
									Description
								</td>
								<td>
									<html:text property="podText" value="<%=poddesc%>" style="width:215px"
										 readonly="true" styleClass="varysizeareahighlightgrey" />
								</td>
							</tr>
						
							<tr>
								<td class="style2" valign="bottom">
									Status
											</td>
											<td>
												<html:select property="status" styleClass="dropdownboxStyle" value="<%=status%>" style="width:215px">
													<html:optionsCollection name="statuslist" />
												</html:select>
											</td>
											<td class="style2" valign="bottom">
												TT From POL To POD
											</td>
											<td>
												<html:text property="ttFromPolToPod" value="<%=ttFromPolToPod%>"  maxlength="3" onkeypress="return checkIts(event)" style="width:215px"/>
											</td>
											</tr>
											<tr>
											<td class="style2" valign="bottom">
												Cut Off Days
											</td>
											<td>
												<html:text property="cutOffDays"  value="<%=cutOffDays %>"  maxlength="2" onkeypress="return checkIts(event)" style="width:215px"/>
											</td>
											<td class="style2" valign="bottom">
												Hour of the day
											</td>
											<td>
												<html:text property="hourOfDay"  value="<%=hourOfDay %>"  maxlength="2" style="width:215px"/>
											</td>
											</tr><tr>
											
											<td class="style2">
												<html:checkbox property="printOnSs"    name="print"></html:checkbox>Print on ss
											</td>
											<td></td>
											<td class="style2">Comment</td>
											<td><html:textarea property="comment" cols="16" value="<%=relay.getComments()%>" onkeyup="limitText(this.form.comment,this.form.countdown,80)" style="width:215px"></html:textarea>					
										</tr></table>
									
											</td>
											<td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
								</td>
							</tr>

							<tr>
								<td>
									<table width="100%">
										<tr align="center">
											<td>
												<input type="button" class="buttonStyleNew" value="Add Origin" onclick="return openOrigin() "/>
												
											</td>
											<td>
											<table><tr><td>
											<input type="button" class="buttonStyleNew" value="Add Destination" style="width:90px" onclick="return openDestination() "/>
												
											</td>
											<td class="style2">Add all on Carriage Dest to Assoc Final Dest</td>
											<td><input type="checkbox" name="myCheck" id="myCheck" onclick="checkbox()"/></td>
											</tr></table></td>
										</tr>
										<tr>
											
											
										</tr>
										<tr>
											<td valign="top" width="50%">
												<table width="100%"class="tableBorderNew">
							
												<tr class="tableHeadingNew">List of Origin</tr>
													<tr>

													</tr>
													<tr>
														<td>
															<%
															int r = 0;
															int i=0;
															%>
															<div style=width:100%;height:100;overflow:auto> 
															<display:table name="<%=originList%>" pagesize="<%=pageSize%>"
																class="displaytagstyle" id="origintable" defaultorder="ascending"  defaultsort="2" sort="list">
																<display:setProperty
																	name="paging.banner.some_items_found">
																	<span class="pagebanner"> <font color="blue">{0}</font>
																		Origin Displayed,For more Data click on Page Numbers.
																		<br> </span>
																</display:setProperty>
																<display:setProperty name="paging.banner.one_item_found">
																	<span class="pagebanner"> One {0} displayed.
																		Page Number </span>
																</display:setProperty>
																<display:setProperty
																	name="paging.banner.all_items_found">
																	<span class="pagebanner"> {0} {1} Displayed,
																		Page Number </span>
																</display:setProperty>
																<display:setProperty name="basic.msg.empty_list">
																	<span class="pagebanner"> No Records Found. </span>
																</display:setProperty>
																<display:setProperty name="paging.banner.placement"
																	value="bottom" />
																<display:setProperty name="paging.banner.item_name"
																	value="Origin" />
																<display:setProperty name="paging.banner.items_name"
																	value="Origin" />
																<%
																			PortsTemp port = null;
																			String portCode = "";
																			String portName = "";
																			 String tttime="";
                                     										String week="0";
                                    										 String time="";
																			if (originList != null && originList.size() > 0) {
																		RelayOrigin relayOrigin = (RelayOrigin) originList
																				.get(r);
																		if (relayOrigin != null) {
																			port = relayOrigin.getOriginId();
																			if (port != null) {
																				portCode = port.getShedulenumber();
																				portName = port.getPortname();
																			}
																			if(relayOrigin.getTtToPol()!=null)
																			{
																			 tttime=relayOrigin.getTtToPol().toString();
																			 }
                                     	  if(relayOrigin.getCutOffDayOfWeek()!=null)
                                     	  {
                                     	  week=relayOrigin.getCutOffDayOfWeek().getCodedesc();
                                     	  }
                                     	  if(relayOrigin.getCutOffTime()!=null)
                                     	  {
                                     	  time=relayOrigin.getCutOffTime().toString();
                                     	  }
																		}
																			}
														 String iStr=String.valueOf(i);
  							String editPath1=editPath+"?paramid="+iStr;
																%>
																<display:column title="Origin Code"><a href="<%=editPath1%>">
																	<%=null!=portCode?portCode:""%></a>
																</display:column>
																
																<display:column title="Origin Name">
																	<%=portName%>
																</display:column>
													<display:column title="TT to POL" ><%=tttime%></display:column>
									<display:column title="Week" ><%=week%></display:column>
									<display:column title="Cutoff Time"><%=time%></display:column>		
																

																<%
																r++;
																i++;
																%>
															</display:table>
														</td>
													</tr>
												</table>
											</td>
											<td valign="top">
												<table width="100%" class="tableBorderNew">
							<tr class="tableHeadingNew">List of Destination</tr>
													<tr>
														<td valign="top">
															<%
															int j = 0;
															int k=0;
															%>
															<div style=width:100%;height:100;overflow:auto> 
															<display:table name="<%=destinationList%>" pagesize="<%=pageSize%>" sort="list"
																class="displaytagstyle" id="destinationtable" defaultorder="ascending" defaultsort="2">
																<display:setProperty
																	name="paging.banner.some_items_found">
																	<span class="pagebanner"> <font color="blue">{0}</font>
																		Destination Displayed,For more Data click on Page
																		Numbers. <br> </span>
																</display:setProperty>
																<display:setProperty name="paging.banner.one_item_found">
																	<span class="pagebanner"> One {0} displayed.
																		Page Number </span>
																</display:setProperty>
																<display:setProperty
																	name="paging.banner.all_items_found">
																	<span class="pagebanner"> {0} {1} Displayed,
																		Page Number </span>
																</display:setProperty>
																<display:setProperty name="basic.msg.empty_list">
																	<span class="pagebanner"> No Records Found. </span>
																</display:setProperty>
																<display:setProperty name="paging.banner.placement"
																	value="bottom" />
																<display:setProperty name="paging.banner.item_name"
																	value="Destination" />
																<display:setProperty name="paging.banner.items_name"
																	value="Destination" />
																<%
																			PortsTemp port1 = null;
																			String destCode = "";
																			String destName = "";
																			 String ttPol="";
																			if (destinationList != null && destinationList.size() > 0) {
																		RelayDestination relayDestination = (RelayDestination) destinationList
																				.get(j);
																		if (relayDestination != null) {
																			port1 = relayDestination.getDestinationId();
																			if (port1 != null) {
																				destCode = port1.getShedulenumber();
																				destName = port1.getPortname();
																			}
																			if(relayDestination.getTtFromPodToFd()!=null)
																			{
																			 ttPol=relayDestination.getTtFromPodToFd().toString();
																			 }
																		}
																			}
											 String iStr1=String.valueOf(k);
  							String editPath2=editPath+"?param="+iStr1;
																%>
																<display:column title="Dest Code"><a href="<%=editPath2%>">
																	<%=null!=destCode?destCode:""%></a>
																</display:column>
																
																<display:column title="Dest Name">
																	<%=destName%>
																</display:column>
																<display:column title="TT Pol"><%=ttPol%></display:column>
																

																<%
																j++;
																k++;
																%>
															</display:table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>

							</tr>
						</table>
						<html:hidden property="buttonValue" styleId="buttonValue" />
						<html:hidden property="ind" />
						<html:hidden property="inx" />


						</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

