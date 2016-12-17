<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"
	import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.RelayOrigin,com.gp.cong.logisoft.domain.RelayDestination"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RelayInquiry" />
<jsp:directive.page import="com.gp.cong.logisoft.domain.PortsTemp" />
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<jsp:useBean id="print"class="com.gp.cong.logisoft.struts.form.AddRelayForm"></jsp:useBean>
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
	String status="";
	String ttFromPolToPod="";
	String cutOffDays="";
	String hourOfDay="";
	// from hyd
	String printOnSs="";
	
	List portRelayList=null;
	if(session.getAttribute("portRelayList") != null)
{
	portRelayList=(List)session.getAttribute("portRelayList");
}
	 
	RelayInquiry check=new RelayInquiry();
	check.setDestCheck("checked");
	request.setAttribute("myCheck",check);
	if(request.getAttribute("addorigin")!=null && request.getAttribute("addorigin").equals("addorigin"))
	{
	%>
	<script type="text/javascript">
mywindow=window.open("<%=path%>/jsps/datareference/addOrigin.jsp?addorigin="+"list","","width=650,height=450");
      mywindow.moveTo(200,180);
</script>
	<% 
	}
	if(request.getAttribute("adddestination")!=null && request.getAttribute("adddestination").equals("adddestination"))
	{
	%>
	<script type="text/javascript">
	  mywindow=window.open("<%=path%>/jsps/datareference/addDestination.jsp?addDestination="+"list","","width=650,height=450");
      mywindow.moveTo(200,180);
</script>
	<% 
	}
	RelayInquiry relay = new RelayInquiry();
	if (session.getAttribute("relay") != null) {
		relay = (RelayInquiry)session.getAttribute("relay");
			if(relay.getStatus()!=null)
					{
							status=relay.getStatus();
					}
			if(relay.getTtFromPolToPod()!=null)
					{
				 			ttFromPolToPod=relay.getTtFromPolToPod().toString();
					}
			if(relay.getCutOffDays()!=null)
					{
							cutOffDays=relay.getCutOffDays().toString();
					}
			if(relay.getHourOfDay()!=null)
					{
							hourOfDay=relay.getHourOfDay().toString();
					}
			//source changed for checkbox
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
	
	
	request.setAttribute("statuslist", dbUtil.getStatusListForRelay());
	String msg = "";

	if (request.getAttribute("message") != null) {
		msg = (String) request.getAttribute("message");
	}

	if (session.getAttribute("originList") != null) {
		originList = (List) session.getAttribute("originList");
		
		//request.setAttribute("originList",originList);
	}
	if (session.getAttribute("destinationList") != null) {
		destinationList = (List) session.getAttribute("destinationList");
		
	}
	relay.setPrintOnSs("Y");
	session.setAttribute("print",relay);
	String editPath=path+"/addRelay.do";
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
			if(document.addRelay.pol.value=="0")
			{
				alert("Please select POL Code");
				document.addRelay.pol.value="";
				document.addRelay.pol.focus();
				return;
			}
			if(document.addRelay.pod.value=="0")
			{
				alert("Please select POD Code");
				document.addRelay.pod.value="";
				document.addRelay.pod.focus();
				return;
			}
			if(IsNumeric(document.addRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.addRelay.ttFromPolToPod.value="";
    				document.addRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.addRelay.cutOffDays.value="";
    				document.addRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.addRelay.hourOfDay.value="";
    				document.addRelay.hourOfDay.focus();
   					return;
  				} 
			 if(document.addRelay.comment.value!="" && document.addRelay.comment.value.length>80)
			 {
					 alert("Comment should be within 80 characters");
  					document.addRelay.comment.value="";
    				document.addRelay.comment.focus();
   					return;
  				} 
			 document.addRelay.buttonValue.value="save";
   			 document.addRelay.submit();
 	}
 		function openPortException()
	{
	
		document.addRelay.buttonValue.value="addport";
   		document.addRelay.submit();
 	}	
	function cancel()
	{
		document.addRelay.buttonValue.value="cancel";
   		document.addRelay.submit();
 	}	
	function submit1()
    {
   		if(IsNumeric(document.addRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.addRelay.ttFromPolToPod.value="";
    				document.addRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.addRelay.cutOffDays.value="";
    				document.addRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.addRelay.hourOfDay.value="";
    				document.addRelay.hourOfDay.focus();
   					return;
  				} 
   	  	document.addRelay.buttonValue.value="polselected";
  	 	document.addRelay.submit();
    }	
	function submit2()
    {
    if(IsNumeric(document.addRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.addRelay.ttFromPolToPod.value="";
    				document.addRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.addRelay.cutOffDays.value="";
    				document.addRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.addRelay.hourOfDay.value="";
    				document.addRelay.hourOfDay.focus();
   					return;
  				} 
   		 
   	  	document.addRelay.buttonValue.value="podselected";
  	 	document.addRelay.submit();
    }	
	 function openOrigin(){
      if(IsNumeric(document.addRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.addRelay.ttFromPolToPod.value="";
    				document.addRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.addRelay.cutOffDays.value="";
    				document.addRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.addRelay.hourOfDay.value="";
    				document.addRelay.hourOfDay.focus();
   					return;
  				} 
      		window.open('<%=path%>/jsps/datareference/addOrigin.jsp?relay='+'add','openOrigin','width=600,height=150,scrollbars=yes');
      		document.addRelay.submit();
      }    
      
      
	 function openDestination() 
      {
      if(IsNumeric(document.addRelay.ttFromPolToPod.value)==false)
  				{
   					alert("Transit Time From POL To POD should be Numeric.");
  					document.addRelay.ttFromPolToPod.value="";
    				document.addRelay.ttFromPolToPod.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.cutOffDays.value)==false)
  				{
   					alert("Cut off days should be Numeric.");
  					document.addRelay.cutOffDays.value="";
    				document.addRelay.cutOffDays.focus();
   					return;
  				} 
  				if(IsNumeric(document.addRelay.hourOfDay.value)==false)
  				{
   					alert("Hour Of The Day should be Numeric.");
  					document.addRelay.hourOfDay.value="";
    				document.addRelay.hourOfDay.focus();
   					return;
  				} 
  				window.open( '<%=path%>/jsps/datareference/addDestination.jsp?relay='+'add','openDestination','width=600,height=150,scrollbars=yes')
  					document.addRelay.submit();
      }  
      
 function confirmdelete(obj)
	{
	var rowindex=obj.parentNode.parentNode.rowIndex;
	 var x=document.getElementById('origintable').rows[rowindex].cells;	
	
  document.addRelay.ind.value=rowindex-1;
		document.addRelay.buttonValue.value="delete";
    	var result = confirm("Are you sure you want to delete this origin ");
		if(result)
		{
   			document.addRelay.submit();
   		}	
   	}	
   	function confirmdestdelete(obj)
	{
	var rowindex=obj.parentNode.parentNode.rowIndex;
	 var x=document.getElementById('destinationtable').rows[rowindex].cells;	
	
  document.addRelay.inx.value=rowindex-1;
		document.addRelay.buttonValue.value="destdelete";
    var result = confirm("Are you sure you want to delete this Destination ");
		if(result)
		{
   			document.addRelay.submit();
   		}	
   	}	
   	function checkbox()
{

if(document.getElementById("myCheck").checked==true)
{

document.addRelay.buttonValue.value="check";
document.addRelay.submit();
}
 
}
   	function defaultValue(status,ttFromPolToPod,cutOffDays,hourOfDay,printOnSs)
   	{
   		
			document.addRelay.ttFromPolToPod.value=ttFromPolToPod;
			document.addRelay.status.value=status;
			document.addRelay.cutOffDays.value=cutOffDays;
			document.addRelay.hourOfDay.value=hourOfDay;
			document.addRelay.printOnSs.value=printOnSs;
   		
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
    if(document.addRelay.printOnSs.checked)
    {
     
    document.addRelay.printOnSs.value="Y";
     
     document.addRelay.printOnSs.focus();
    
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
<body class="whitebackgrnd" onload="defaultValue('<%=status%>','<%=ttFromPolToPod%>','<%=cutOffDays%>','<%=hourOfDay%>','<%=printOnSs%>')">
<html:form action="/addRelay" name="addRelay" type="com.gp.cong.logisoft.struts.form.AddRelayForm" scope="request">
<table width="100%"  border="0" cellpadding="0" cellspacing="0">
<tr>
   <td><table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorderNew">
		<tr class="tableHeadingNew"> <td >Relay Details</td>
		<td align="right"><input type="button" class="buttonStyleNew" value="Save" onclick="save()"/>
	    <input type="button" class="buttonStyleNew" value="Cancel" onclick="cancel()"/>
       <% if(portRelayList!=null && portRelayList.size()>0) { %>
       <input type="button" class="buttonStyleNew" id="portexception" value="Port Exception" style="width:90px" onclick="openPortException()"/>
       <% }else{ %>
	   <input type="button" class="buttonStyleNew" id="portexception" value="Port Exception" style="width:90px" onclick="openPortException()"/>
	   <% } %></td>
	   </tr>
	   <tr>
	   <td colspan="2"><table width="100%" border="0" cellspacing="3" cellpadding="0">
	   <tr>
	   <td  class="style2">POL*</td>
	   <td width="20%"><html:text property="pol" readonly="true"value="<%=polid%>" styleClass="varysizeareahighlightgrey" style="width:200px"/>&nbsp;
       </td>
	   <td  class="style2" width="20%">Description</td>
	   <td><html:text property="polText" value="<%=poldesc%>"  readonly="true" styleClass="varysizeareahighlightgrey" style="width:200px"/></td>
	   </tr>
	   <tr><td class="style2">POD*</td>
  	   <td><html:text property="pod" readonly="true" value="<%=podid%>" styleClass="varysizeareahighlightgrey" style="width:200px"/></td>
 	   <td  class="style2">Description</td>
	   <td><html:text property="podText" value="<%=poddesc%>"  readonly="true" styleClass="varysizeareahighlightgrey" style="width:200px"/></td>
	   </tr>
 	  <tr>
	  <td class="style2" valign="bottom">Status </td>
	  <td > <html:select property="status" styleClass="dropdownboxStyle" style="width:200px">
		 <html:optionsCollection name="statuslist" /> </html:select> </td>
	  <td class="style2" valign="bottom">TT From POL To POD </td>
	  <td> <html:text property="ttFromPolToPod"   maxlength="3" onkeypress="return checkIts(event)" style="width:200px"/> </td>
	  </tr>
	  <tr> <td class="style2" valign="bottom">Cut Off Days</td>
      <td> <html:text property="cutOffDays" maxlength="2" onkeypress="return checkIts(event)" style="width:200px"/> </td>
	  <td class="style2" valign="bottom">Hour of the day </td>
	  <td> <html:text property="hourOfDay"  maxlength="2" style="width:200px"/> </td>
	  </tr>
	  <tr>
	  <td class="style2" valign="top">Print on ss<html:checkbox property="printOnSs"   name="print" onclick="printon()" /> </td>
	  <td> </td>
	  <td class="style2">Comment</td>
	  <td><html:textarea property="comment" cols="16" value="<%=relay.getComments()%>" onkeyup="limitText(this.form.comment,this.form.countdown,80)" style="width:200px"></html:textarea></td>
	  </tr>
	</table>
	</td>
	</tr>
	</table>
</td>
</tr>
<tr>
	<td>
	<table width="100%">
	 <tr align="center">
	 <td> <input type="button" class="buttonStyleNew"  value="Add Origin"	onclick="openOrigin()"/> </td>
     <td>
     <table>
     <tr>
	 <td> <input type="button" class="buttonStyleNew"  value="Add Destination" style="width:90px"  onclick="openDestination()"/> </td>
	 <td > <input type="checkbox" name="myCheck" id="myCheck" onclick="checkbox()"/> </td>
	 <td class="style2">Add all on Carriage Dest to Assoc Final Dest</td>			
	 </tr>
	 </table>
	</td>
  </tr>
  <tr>
	<td valign="top" width="50%">
	<table width="100%" class="tableBorderNew">
	<tr class="tableHeadingNew">List of Origin</tr>
	<tr>
    </tr>
	<tr >
	<td>
	<%int i = 0;
	  int k=0;%>
	<div style=width:100%;height:100;overflow:auto> 
	<display:table name="<%=originList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="origintable" defaultorder="ascending" defaultsort="1">
	<display:setProperty name="paging.banner.some_items_found">
	<span class="pagebanner"> <font color="blue">{0}</font>
	Origin Displayed,For more Data click on Page Numbers. <br>
	</span></display:setProperty>
	<display:setProperty name="paging.banner.one_item_found">
	<span class="pagebanner"> One {0} displayed. Page Number </span></display:setProperty>
	<display:setProperty name="paging.banner.all_items_found">
	<span class="pagebanner"> {0} {1} Displayed, Page Number </span></display:setProperty>
	<display:setProperty name="basic.msg.empty_list">
    <span class="pagebanner"> No Records Found. </span>
    </display:setProperty>
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="paging.banner.item_name" value="Origin" />
	<display:setProperty name="paging.banner.items_name" value="Origin" />
    <%
                                     PortsTemp port = null;
                                     String portCode="";
                                     String portName="";
                                     String tttime="";
                                     String week="0";
                                     String time="";
                                     if(originList!=null && originList.size()>0)
                                     {
                                     	RelayOrigin relayOrigin=(RelayOrigin)originList.get(i);
                                     	if(relayOrigin!=null)
                                     	{
                                      	  port= relayOrigin.getOriginId();
                                      	  if(port!=null)
                                      	  {
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
  			
  			String iStr=String.valueOf(k);
  			String editPath1=editPath+"?paramid="+iStr;
  			
  			 %>
  			 
									<display:column title="Origin Code"><a href="<%=editPath1%>"><%=portCode%></a></display:column>
									
									<display:column title="Origin Name" ><%=portName%></display:column>
									<display:column title="TT to POL" ><%=tttime%></display:column>
									<display:column title="Week" ><%=week%></display:column>
									<display:column title="Cutoff Time"><%=time%></display:column>
 									
									
									<%
									i++;
									k++;
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
								
							</tr>
							<tr>
								<td valign="top">
								<%
								int j = 0;
								int a=0;
								
								%>
								<div style=width:100%;height:100;overflow:auto> 
								<display:table name="<%=destinationList%>" pagesize="<%=pageSize%>"
									class="displaytagstyle" id="destinationtable" defaultorder="ascending" defaultsort="1">
									<display:setProperty name="paging.banner.some_items_found">
										<span class="pagebanner"> <font color="blue">{0}</font>
											Destination Displayed,For more Data click on Page Numbers. <br>
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
										value="Destination" />
									<display:setProperty name="paging.banner.items_name"
										value="Destination" />
                                    <%
                                     PortsTemp port = null;
                                     String destCode="";
                                     String destName="";
                                     String ttPol="";
                                     if(destinationList!=null && destinationList.size()>0)
                                     {
                                     
								
                                     	RelayDestination relayDestination=(RelayDestination)destinationList.get(j);
                                     	if(relayDestination!=null)
                                     	{
                                      	  port= relayDestination.getDestinationId();
                                      	  if(port!=null)
                                      	  {
                                     	   destCode = port.getShedulenumber();
                                     	   destName = port.getPortname();
                                     	  }
                                     	  if(relayDestination.getTtFromPodToFd()!=null)
                                     	  {
                                     	  ttPol=relayDestination.getTtFromPodToFd().toString();
                                     	  }
                                     	}
                                     }
                                     String iStr=String.valueOf(a);
  							String editPath1=editPath+"?param="+iStr;
                                      %>
									<display:column title="Dest Code"><a href="<%=editPath1%>" ><%=destCode%></a></display:column>
									
									<display:column title="Dest Name" ><%=destName%></display:column>
									<display:column title="TT Pol" ><%=ttPol%></display:column>
									
									
									<%
									j++;
									a++;
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
			<html:hidden property="ind"/>
			<html:hidden property="inx"/>
			
		
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

