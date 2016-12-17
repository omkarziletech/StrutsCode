<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.UniversalMaster,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
UniversalMaster universalMaster=new UniversalMaster();
String terminalNumber="";
String terminalName="";
String destSheduleNumber="";
String destAirportname="";
String comCode="";
String comDesc="";
String message="";
String msg="";
String modify="";
AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setCommon("");
UniversalMaster lclNew=new UniversalMaster();
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
}
request.setAttribute("airRatesBean",airRatesBean);
	if(request.getParameter("universal")!=null)
	{
	if(session.getAttribute("searchuniMaster")!=null)
	{
		
		UniversalMaster sc1=(UniversalMaster)session.getAttribute("searchuniMaster");
		lclNew. setOriginTerminal(sc1.getOriginTerminal());
	    lclNew.setDestinationPort(sc1.getDestinationPort());
	    lclNew.setCommodityCode(sc1.getCommodityCode());
		
		session.setAttribute("addUniversalMaster",lclNew);
	}
	}
if(session.getAttribute("addUniversalMaster")!=null)
{
universalMaster=(UniversalMaster)session.getAttribute("addUniversalMaster");
if(universalMaster.getOriginTerminal()!=null)
{
terminalNumber=universalMaster.getOriginTerminal().getTrmnum();
terminalName=universalMaster.getOriginTerminal().getTerminalLocation();
}
if(universalMaster.getDestinationPort()!=null)
{
destSheduleNumber=universalMaster.getDestinationPort().getShedulenumber();
destAirportname=universalMaster.getDestinationPort().getPortname();
}
if(universalMaster.getCommodityCode()!=null)
{
comCode=universalMaster.getCommodityCode().getCode();
comDesc=universalMaster.getCommodityCode().getCodedesc();
}
}
String messge="";
List list=new ArrayList();
DBUtil dbutil=new DBUtil();
if(universalMaster!=null){
	list=dbutil.getUniversalRecords(universalMaster.getOriginTerminal(),universalMaster.getDestinationPort(),universalMaster.getCommodityCode());
		
		if(list!=null&&list.size()>0){
			if(request.getParameter("universal")!=null)
	{}
	else{
				messge="PLease Select another Commodity or else change Origin number or Destination port ";
		}		
				
			comCode="";
			comDesc="";
			}
	}
if(session.getAttribute("exist")!=null){
session.removeAttribute("exist");
}	
String univeradd="";
if(request.getAttribute("adduniversal")!=null)
{
univeradd=(String)request.getAttribute("adduniversal");
}
if(request.getAttribute("msg")!=null)
{
	msg=(String)request.getAttribute("msg");
}
if(request.getAttribute("update")!=null)
{
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

if(message.equals("")){

if(univeradd.equals("adduniversal"))
{
%>
	<script>
	
	   parent.parent.getUniversal();
	   parent.parent.GB_hide();
	
		//self.close();
		//opener.location.href="<%=path%>/jsps/ratemanagement/universal_frame.jsp";
	</script>
<%
}}
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
				document.universalAddPopForm.buttonValue.value="search1";
			document.universalAddPopForm.submit();
			return false;
		}
		
		function searchform()
		{
		if(document.universalAddPopForm.terminalNumber.value=="")
		{
		alert("Please select Terminal Number");
		return;
		}
		if(document.universalAddPopForm.destSheduleNumber.value=="")
		{
		alert("Please select Destination Airport Code");
		return;
		}
		if(document.universalAddPopForm.comCode.value=="")
		{
			alert("Please select select  common commodity");
			return;
		}
		
			document.universalAddPopForm.buttonValue.value="search";
			document.universalAddPopForm.submit();
		}
		
	 function titleLetter(ev)
	{
		if(event.keyCode==9){
		document.universalAddPopForm.buttonValue.value="popupsearch";
	    document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.universalAddPopForm.terminalName.value="";
		document.universalAddPopForm.buttonValue.value="popupsearch";
		document.universalAddPopForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	function getDestination(ev)
	{
		if(event.keyCode==9){
		document.universalAddPopForm.buttonValue.value="popupsearch";
	    document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.universalAddPopForm.destAirportname.value="";
		document.universalAddPopForm.buttonValue.value="popupsearch";
		document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCode(ev)
	{
		if(event.keyCode==9){
		document.universalAddPopForm.buttonValue.value="popupsearch";
	    document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=searchlclcoload&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.universalAddPopForm.comDescription.value="";
		document.universalAddPopForm.buttonValue.value="popupsearch";
		document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterName(ev)
	{
		if(event.keyCode==9){
		document.universalAddPopForm.buttonValue.value="popupsearch";
	    document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.universalAddPopForm.terminalNumber.value="";
		document.universalAddPopForm.buttonValue.value="popupsearch";
		document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationName(ev)
	{
		if(event.keyCode==9){
		document.universalAddPopForm.buttonValue.value="popupsearch";
	    document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodeName(ev)
	{
		if(event.keyCode==9){
		document.universalAddPopForm.buttonValue.value="popupsearch";
	    document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=searchlclcoload&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.universalAddPopForm.destSheduleNumber.value="";
		document.universalAddPopForm.buttonValue.value="popupsearch";
		document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.universalAddPopForm.comCode.value="";
		document.universalAddPopForm.buttonValue.value="popupsearch";
		document.universalAddPopForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	</script>
	<%@include file="../includes/resources.jsp" %>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/universalAddPop" scope="request">
		<font color="blue"><b><%=messge %></b></font>
		<font color="blue"><b><%=message%></b></font>
			<table width=100%  border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew"> Add New</tr>
			<tr>
    			<td>
    			<table width="100%" border="0" cellpadding="0" cellspacing="2">
    			<tr class="style2"><td>Org Trm</td></tr></table></td>
    			<td>
    			<input name="terminalNumber" id="terminalNumber" size="15"  onkeydown="titleLetter(this.value)"  onkeypress="titleLetterPress(this.value)" value="<%=terminalNumber %>" maxlength="2"/>    
    			<dojo:autoComplete formId="universalAddPopForm" textboxId="terminalNumber" action="<%=path%>/actions/getTerminal.jsp?tabName=UNIVERSAL_ADD_POPUP&from=0"/>                
    			</td>
    			<td>&nbsp;</td>
    			<td><span class="textlabels">&nbsp;OriginTerminal Name</span></td>
    			<td>
    			 <input name="terminalName" id="terminalName" size="15" onkeydown="titleLetterName(this.value)"  onkeypress="titleLetterNamePress(this.value)" value="<%=terminalName %>" />    
    			   <dojo:autoComplete formId="universalAddPopForm" textboxId="terminalName" action="<%=path%>/actions/getTerminalName.jsp?tabName=UNIVERSAL_ADD_POPUP&from=0"/>
    			</td>
    		</tr>
    		<tr>	
    			<td class="textlabels"><table width="100%" border="0"><tr class="style2"><td>Dest Port</td></tr></table> </td>
    			<td>
    			<input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getDestination(this.value)" onkeypress="getDestinationPress(this.value)" value="<%=destSheduleNumber %>" size="15" maxlength="5"/>    
    			<dojo:autoComplete formId="universalAddPopForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getPorts.jsp?tabName=UNIVERSAL_ADD_POPUP&from=0"/>
    			</td>
    			<td>&nbsp;</td>
    			<td><span class="textlabels">Dest Port Name</span></td>
    			<td>
    			<input name="destAirportname" id="destAirportname" size="15" onkeydown="getDestinationName(this.value)" onkeypress="getDestinationNamePress(this.value)" value="<%=destAirportname %>" />    
    		    <dojo:autoComplete formId="universalAddPopForm" textboxId="destAirportname" action="<%=path%>/actions/getPorts.jsp?tabName=UNIVERSAL_ADD_POPUP&from=1"/>
    			</td>
    			
    		</tr>
    		<tr>	
    			<td><table width="100%" border="0"><tr class="style2"><td>Com Code</td></tr></table> </td>
    			<td>
    			<input name="comCode" id="comCode"  onkeydown="getComCode(this.value)"  onkeypress="getComCodePress(this.value)" value="<%=comCode %>" maxlength="6" size="15"/>    
    		    <dojo:autoComplete formId="universalAddPopForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=UNIVERSAL_ADD_POPUP"/>
    			</td>
    			<td>&nbsp;</td>
    			<td class="textlabels">Com Description </td>
    			<td>
    			<input name="comDescription" id="comDescription" size="15" onkeydown="getComCodeName(this.value)" onkeypress="getComCodeNamePress(this.value)" value="<%=comDesc %>" />    
    		    <dojo:autoComplete formId="universalAddPopForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=UNIVERSAL_ADD_POPUP"/>
    			</td>
    		</tr>
    		<tr>
    		<td><table><tr>

    			
    			</tr></table></td>
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
    		<script>parent.parent.parent.parent.makeFormBorderless(document.getElementById("universalAdd"));</script>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


