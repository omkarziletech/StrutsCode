<%@ page language="java" import="java.util.ArrayList,com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.domain.FclBuy"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
FclBuy fclBuy=new FclBuy();
String terminalNumber="";
String terminalName="";
String destSheduleNumber="";
String destAirportname="";
String comCode="";
String comDesc="";
String message="";
String msg="";
String modify="";
String sslineno="";
String sslinename="";
String contact="";

/*if(session.getAttribute("searchFclcodelist")!=null){

	List li=(List)session.getAttribute("searchFclcodelist");
	
	if(li.size()>0){
	session.removeAttribute("searchFclcodelist");
	
	}

}*/
if(request.getParameter("futurefclcorates")!=null)
{
	session.setAttribute("futurerates","futurerates");

	}
FclBuy lclNew=new FclBuy();
if(request.getParameter("fclcorates")!=null)
{
	if(session.getAttribute("futuresearchfclrecords")!=null)
	{
		FclBuy sc1=(FclBuy)session.getAttribute("futuresearchfclrecords");
		lclNew. setOriginTerminal(sc1.getOriginTerminal());
	    lclNew.setDestinationPort(sc1.getDestinationPort());
	    lclNew.setComNum(sc1.getComNum());
		session.setAttribute("futureaddfclrecords",lclNew);
	}
	//session.setAttribute("futureaddfclrecords",session.getAttribute("searchfclrecords"));
	}
if(request.getAttribute("message")!=null){
	message=(String)request.getAttribute("message");
		
}
if(session.getAttribute("costcode")!=null){session.removeAttribute("costcode");}
if(session.getAttribute("costCodeList")!=null){session.removeAttribute("costCodeList");}

			  
if(session.getAttribute("futureaddfclrecords")!=null)
{

fclBuy=(FclBuy)session.getAttribute("futureaddfclrecords");
if(fclBuy.getOriginTerminal()!=null)
{
terminalNumber=fclBuy.getOriginTerminal().getUnLocationCode();
terminalName=fclBuy.getOriginTerminal().getUnLocationName();
}
if(fclBuy.getDestinationPort()!=null)
{
destSheduleNumber=fclBuy.getDestinationPort().getUnLocationCode();
destAirportname=fclBuy.getDestinationPort().getUnLocationName();
}
if(fclBuy.getComNum()!=null)
{
comCode=fclBuy.getComNum().getCode();
comDesc=fclBuy.getComNum().getCodedesc();
}
if(fclBuy.getSslineNo()!=null){
sslineno=fclBuy.getSslineNo().getCarriercode();
sslinename=fclBuy.getSslineNo().getCarriername();
contact=fclBuy.getSslineNo().getFclContactNumber();
}
}
List list=new ArrayList();
DBUtil dbutil=new DBUtil();
if(fclBuy!=null){
	list=dbutil.getFCLDetails(fclBuy.getOriginTerminal(),fclBuy.getDestinationPort(),fclBuy.getComNum(),fclBuy.getSslineNo(),fclBuy.getOriginalRegion(),fclBuy.getDestinationRegion());

		if(list!=null&&list.size()>0){
		
				}
				else{
				
				}
		}
String address="";

if(request.getAttribute("sendfclcontrol")!=null)
{
address=(String)request.getAttribute("sendfclcontrol");
}
if(request.getAttribute("msg")!=null)
{
	msg=(String)request.getAttribute("msg");
}
/*if(request.getAttribute("update")!=null)
{
	message=(String)request.getAttribute("update");
	}*/
if(request.getAttribute("update")!=null){
	message=(String)request.getAttribute("update");
}
if(request.getParameter("modify")!= null)
{
	session.setAttribute("modifyforrelay",request.getParameter("modify"));
 	modify=(String)session.getAttribute("modifyforrelay");
}
else
{
 	modify=(String)session.getAttribute("modifyforrelay");
}

if((terminalNumber!=null && !terminalNumber.equals("")) && (destSheduleNumber!=null && !destSheduleNumber.equals("")) && 
(comCode!=null && !comCode.equals("")) && (sslineno!=null && !sslineno.equals("")))
{
if(message.equals("")){

	if(address.equals("sendfclcontrol"))
	{
%>
	<script>
	     alert("hello");
	     parent.parent.getFclFuture();
	     parent.parent.GB_hide();
		//self.close();
		///opener.location.href="<%=path%>/jsps/ratemanagement/addFutureFcl.jsp";
	</script>
<%
	}
	else if(address.equals("sendfuturefclcontrol"))
	{
	session.removeAttribute("futurerates");
%>
	<script>
	     alert("hello");
	     parent.parent.getFclFuture();
	     parent.parent.GB_hide();
		//self.close();
		//opener.location.href="<%=path%>/jsps/ratemanagement/addFutureFcl.jsp";
	</script>
<%
	}
	
}
}
%>

<html> 
	<head>
		<title>JSP for AddLclColoadPopupForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
		<script language="javascript" type="text/javascript">
		 function popup1(mylink, windowname)
		{
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=500,height=250,scrollbars=yes');
			mywindow.moveTo(200,180);
			document.addFutureFclPopForm.buttonValue.value="search";
			document.addFutureFclPopForm.submit();
			return false;
		}
		function searchform()
		{
		if(document.addFutureFclPopForm.terminalNumber.value=="")
		{
		alert("Please select Terminal Number");
		return;
		}
		if(document.addFutureFclPopForm.destSheduleNumber.value=="")
		{
		alert("Please select Destination Airport Code");
		return;
		}
		if(document.addFutureFclPopForm.comCode.value=="")
		{
		alert("Please select Commodity Code");
		return;
		}
		if(document.addFutureFclPopForm.sslinenumber.value=="")
		{
		alert("Please select SSLine Number Code");
		return;
		}
			document.addFutureFclPopForm.buttonValue.value="search";
			document.addFutureFclPopForm.submit();
		}
	function getSSLine(ev)
	{
	
		if(event.keyCode==9){
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
			document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button=searchfcl&ssline=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	
	function getSSLineName(ev)
	{
		if(event.keyCode==9){
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
//	window.open("<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button=searchfcl&sslinename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function titleLetter(ev)
	{
		if(event.keyCode==9)
		{
		document.addFutureFclPopForm.terminalName.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
 function getDestination(ev)
	{
		if(event.keyCode==9)
		{
		document.addFutureFclPopForm.destAirportname.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCode(ev)
	{
		if(event.keyCode==9)
		{

		document.addFutureFclPopForm.comDescription.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function titleLetterName(ev)
	{
		if(event.keyCode==9)
		{
		document.addFutureFclPopForm.terminalNumber.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getDestinationName(ev)
	{
		if(event.keyCode==9)
		{
		document.addFutureFclPopForm.destSheduleNumber.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeName(ev)
	{
		if(event.keyCode==9)
		{		
		document.addFutureFclPopForm.comCode.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	 function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFutureFclPopForm.terminalName.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
 function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFutureFclPopForm.destAirportname.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFutureFclPopForm.comDescription.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFutureFclPopForm.terminalNumber.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFutureFclPopForm.destSheduleNumber.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.addFutureFclPopForm.comCode.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}	
	function getsslineno(ev)
	{
	  if(event.keyCode==9){
		document.addFutureFclPopForm.sslinename.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	
	}
	}
	
	function getsslinenopress(ev)
	{
	  if(event.keyCode==13){
		document.addFutureFclPopForm.sslinename.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	
	}
	}
	 
	 function getsslinename(ev)
	 {
	  if(event.keyCode==9){
		document.addFutureFclPopForm.sslinenumber.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	
	}
	 }
	 function getsslinenamepress(ev)
	 {
	  if(event.keyCode==13){
		document.addFutureFclPopForm.sslinenumber.value="";
		document.addFutureFclPopForm.buttonValue.value="popupsearch";
		document.addFutureFclPopForm.submit();
	
	}
	 }
	</script>
		<%@include file="../includes/resources.jsp" %>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/addFutureFclPop" scope="request">
		<font color="blue"><b><%=message%></b></font>
			<table width=100%  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			 <tr class="tableHeadingNew" height="90%">
					<b>Add Future FCL Rates </b>
				</tr>
			<tr>
    			<td><table width="100%" border="0"><tr class="style2"><td>Org Trm</td></tr></table></td>
    			<td>
<%--    			<html:text property="terminalNumber" size="2" onkeydown ="titleLetter(this.value)" maxlength="2" value="<%=terminalNumber%>"  />--%>
    			 <input name="terminalNumber" id="terminalNumber"  onkeydown="titleLetter(this.value)"  onkeypress="titleLetterPress(this.value)" maxlength="2" value="<%=terminalNumber%>"/>
	              <dojo:autoComplete formId="addFutureFclPopForm" textboxId="terminalNumber" action="<%=path%>/actions/getTerminal.jsp?tabName=ADD_FUTURE_FCL_POPUP&from=0"/>
    			</td>
    			
    			<td><span class="textlabels">Org Trm Name</span></td>
    			<td>
<%--    			<html:text property="terminalName" value="<%=terminalName%>" onkeydown="titleLetterName(this.value)"/>--%>
    			 <input  name="terminalName" id="terminalName" value="<%=terminalName%>" onkeydown="titleLetterName(this.value)" onkeypress="titleLetterNamePress(this.value)"/>
                  <dojo:autoComplete formId="addFutureFclPopForm" textboxId="terminalName" action="<%=path%>/actions/getTerminalName.jsp?tabName=ADD_FUTURE_FCL_POPUP&from=0"/>
    			</td>
    		</tr>
    		<tr>	
    			<td class="textlabels"><table width="100%" border="0"><tr class="style2"><td>Dest port</td></tr></table> </td>
    			<td>
<%--    			<html:text property="destSheduleNumber" size="5" onkeydown="getDestination(this.value)" maxlength="5" value="<%=destSheduleNumber%>" />--%>
    			<input  name="destSheduleNumber" id="destSheduleNumber"  maxlength="5"    value="<%=destSheduleNumber%>"  onkeydown="getDestination(this.value)" onkeypress="getDestinationPress(this.value)" />
	                	<dojo:autoComplete  formId="addFutureFclPopForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getPorts.jsp?tabName=ADD_FUTURE_FCL_POPUP&from=0"/>
    			</td>
    			
    			<td><span class="textlabels">Dest Port Name</span></td>
    			<td>
<%--    			<html:text property="destAirportname" value="<%=destAirportname%>"  onkeydown="getDestinationName(this.value)"/>--%>
    			<input name="destAirportname"  id="destAirportname" value="<%=destAirportname%>" onkeydown="getDestinationName(this.value)" onkeypress="getDestinationNamePress(this.value)" />
                    	<dojo:autoComplete formId="addFutureFclPopForm" textboxId="destAirportname" action="<%=path%>/actions/getPorts.jsp?tabName=ADD_FUTURE_FCL_POPUP&from=1"/>
    			</td>
    			
    		</tr>
    		<tr>	    			<td><table width="100%" border="0"><tr class="style2"><td>Com Code&nbsp;</td></tr></table> </td>
    			<td>
<%--    			<html:text property="comCode" maxlength="6"  size="6" onkeydown="getComCode(this.value)" value="<%=comCode%>"  />--%>
    				<input name="comCode"  id="comCode" maxlength="6"   onkeydown="getComCode(this.value)"  onkeypress="getComCodePress(this.value)" value="<%=comCode%>"/>
    			       <dojo:autoComplete formId="addFutureFclPopForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=ADD_FUTURE_FCL_POPUP"/>
    			
    			</td>
    			
    			<td class="textlabels">Com Description </td>
    			<td>
<%--    			<html:text property="comDescription" value="<%=comDesc%>" onkeydown="getComCodeName(this.value)"/>--%>
    			<input name="comDescription" id="comDescription" value="<%=comDesc%>" onkeydown="getComCodeName(this.value)" onkeypress="getComCodeNamePress(this.value)"/>
    			    <dojo:autoComplete formId="addFutureFclPopForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=ADD_FUTURE_FCL_POPUP"/>
    			</td>
    		</tr>
    		<tr>	
    			<td><table width="100%" border="0"><tr class="style2"><td>SS Line Code</td></tr></table> </td>
    			<td>
<%--    			<html:text property="sslinenumber"  size="6" onkeydown="getSSLine(this.value)" maxlength="5" value="<%=sslineno%>"  />--%>
    				<input name="sslinenumber" id="sslinenumber"  maxlength="5" value="<%=sslineno%>" onkeydown="getsslineno(this.value)" onkeypress="getsslinenopress(this.value)"/>
    			    <dojo:autoComplete formId="addFutureFclPopForm" textboxId="sslinenumber" action="<%=path%>/actions/getSsLineNo.jsp?tabName=ADD_FUTURE_FCL_POPUP&from=0"/>
    			</td>
    			
    			<td class="textlabels">SS Line Name </td>
    			<td>
<%--    			<html:text property="sslinename" value="<%=sslinename%>" onkeydown="getSSLineName(this.value)"/>--%>
    			<input name="sslinename" id="sslinename"  value="<%=sslinename%>" onkeydown="getsslinename(this.value)" onkeypress="getsslinenamepress(this.value)"/>
    			    <dojo:autoComplete formId="addFutureFclPopForm" textboxId="sslinename" action="<%=path%>/actions/getSsLineName.jsp?tabName=ADD_FUTURE_FCL_POPUP&from=0"/>
    			</td>
    		</tr>
    		
    		
    		<tr>
    		<td>&nbsp;</td>
    		<td>&nbsp;</td>
    		<td>  
    		      <input type="button" value="Submit" onclick="searchform()" id="search" class="buttonStyleNew" />
    		 </td>
    		<td>&nbsp;</td>
    		</tr>	
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


