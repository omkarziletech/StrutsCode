<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.text.DateFormat,java.text.SimpleDateFormat,java.util.*,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.StandardCharges,com.gp.cong.logisoft.domain.AirFreightFlightShedules"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();

List flightSheduleList=new ArrayList();
String terminalNumber = "";
String terminalName = "";
StandardCharges standardCharges= new StandardCharges();

List minutes=dbUtil.getMinutes();
List hours=dbUtil.getHours();
request.setAttribute("minutes",minutes);
request.setAttribute("hours",hours);
request.setAttribute("daysList",dbUtil.getWeekListForFlightShedule(new Integer(25),"yes","")); 

if(session.getAttribute("standardCharges")!=null )
{
    standardCharges=(StandardCharges)session.getAttribute("standardCharges");
	if(standardCharges.getScheduleTerminal()!=null)
	{
		terminalNumber=standardCharges.getScheduleTerminal().getCode();
		terminalName=standardCharges.getScheduleTerminal().getCodedesc();
		//terminalNumber= standardCharges.getOrgTerminal().getTrmnum();
		//terminalName = standardCharges.getOrgTerminal().getTrmnam();
	}
}
if(session.getAttribute("flightShedulesAdd") != null)
{
	flightSheduleList = (List) session.getAttribute("flightShedulesAdd");
	session.setAttribute("flightShedulesEdit",flightSheduleList);
	
	
}			
String editPath=path+"/flightShedule.do";
String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
%>
 
<html> 
	<head>
		<title>Fight Shedule</title>
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
			mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
			mywindow.moveTo(200,180);
			return false;
		}
		
		function addFlightShedules()
		{
			document.flightSheduleForm.buttonValue.value="add";
  			document.flightSheduleForm.submit();
		}
		
		function confirmdelete(obj)
		{
			var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('agsstable').rows[rowindex].cells;	
	   		document.flightSheduleForm.index.value=rowindex-1;
			document.flightSheduleForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this value");
			if(result)
			{
   				document.flightSheduleForm.submit();
   			}	
   		}
	function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		
   		    imgs[k].style.visibility = 'hidden';
   		 
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			input[i].readOnly=true;
	  			
	  		
  	 	}
  	 	var textarea = document.getElementsByTagName("textarea");
  	 	for(i=0; i<textarea.length; i++)
	 	{
	 			textarea[i].readOnly=true;
	  			
	 					
	  	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
    function titleLetter(ev)
	{
		if(event.keyCode==9)
		{
		document.flightSheduleForm.terminalName.value="";
		document.flightSheduleForm.buttonValue.value="popupsearch";
		document.flightSheduleForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	
	function titleLetterName(ev)
	{
		if(event.keyCode==9)
		{
		document.flightSheduleForm.terminalNumber.value="";
		document.flightSheduleForm.buttonValue.value="popupsearch";
		document.flightSheduleForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.flightSheduleForm.terminalName.value="";
		document.flightSheduleForm.buttonValue.value="popupsearch";
		document.flightSheduleForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	
	function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.flightSheduleForm.terminalNumber.value="";
		document.flightSheduleForm.buttonValue.value="popupsearch";
		document.flightSheduleForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function getdestPort(ev){ 
    document.getElementById("terminalName").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.flightSheduleForm.terminalNumber.value;
			params['codeType'] ='1' ;
			var bindArgs = {
	  			url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  			error: function(type, data, evt){alert("error");},
	  			mimetype: "text/json",
	  			content: params
			};
			var req = dojo.io.bind(bindArgs);
			dojo.event.connect(req, "load", this, "populateDestPort");
		}
		}
	function populateDestPort(type, data, evt) {
  		if(data){
   			document.getElementById("terminalName").value=data.commodityDesc;
   			}
	}
	
	    function getdestPort1(ev){ 
    document.getElementById("terminalNumber").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['codeDesc'] = document.flightSheduleForm.terminalName.value;
			params['codeType'] ='1' ;
			var bindArgs = {
	  			url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  			error: function(type, data, evt){alert("error");},
	  			mimetype: "text/json",
	  			content: params
			};
			var req = dojo.io.bind(bindArgs);
			dojo.event.connect(req, "load", this, "populateDestPort1");
		}
		}
	function populateDestPort1(type, data, evt) {
  		if(data){
   			document.getElementById("terminalNumber").value=data.commodityCode;
   			}
	}
  	    
  	    </script>
	</head>
	<body class="whitebackgrnd" >
		<html:form action="/flightShedule" name="flightSheduleForm" type="com.gp.cong.logisoft.struts.ratemangement.form.FlightSheduleForm" scope="request">
		
	
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
   <td>	
		
		
		<table width=100%  border="0" cellpadding="0" cellspacing="0">
			<tr>
    			<td><table width="100%" border="0"><tr class="style2"><td>Shedule Terminal No</td></tr></table></td>
    			<td>
<%--    			<html:text property="terminalNumber" value="<%=terminalNumber%>" readonly="true" styleClass="areahighlightgrey"/>--%>
    			
    			<input name="terminalNumber" id="terminalNumber" size="2" onkeydown="getdestPort(this.value)" maxlength="2" value="<%=terminalNumber%>"/>
	            <dojo:autoComplete formId="flightSheduleForm" textboxId="terminalNumber" action="<%=path%>/actions/getChargeCode.jsp?tabName=FLIGHT_SHEDULE&from=0"/>
    			</td>
    			<td>&nbsp;</td>
    			<td><span class="textlabels">&nbsp;Shedule Terminal Name</span></td>
    			<td>
<%--    			<html:text property="terminalName" value="<%=terminalName%>" readonly="true" styleClass="areahighlightgrey"/>--%>
    			
    			<input  name="terminalName" id="terminalName" value="<%=terminalName%>" onkeydown="getdestPort1(this.value)"/>
                <dojo:autoComplete formId="flightSheduleForm" textboxId="terminalName" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=FLIGHT_SCHEDULE&from=0"/>
    			</td>
    		<td align="right">
					<input type="button" class="buttonStyleNew" value="Add" onclick="addFlightShedules()"/>
			</td>
    		</tr>
    	</table>	
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
 	    	<td colspan="10"  >&nbsp;&nbsp;</td> 
  		</tr>
		</table>
		<table width="100%" class="tableBorderNew" style="border-left:0px;border-right:0px;"> 
		<tr class="tableHeadingNew" >Flight Schedule</tr>
		
		<tr class="textlabels">
  			<td >Day</td>
  			<td>Flight</td>
  			<td>Stops </td>
  			
  			<td>Departure</td>
  			
  			<td>CutOff Time</td>
  			
  			<td>Aircraft Type </td>
  			
  			
		</tr>
		<tr class="textlabels" align="top">
			<td><html:select   property="day"  styleClass="verysmalldropdownStyle" multiple="true" >
           		<html:optionsCollection name="daysList" />	
           	</html:select></td>
			<td><html:text property="flight" maxlength="20" size="5"/></td>
			<td><html:text property="stops" maxlength="20"  size="5"/></td>
			<td><table><tr>
			<td>
			 <html:select property="flightHours">
                 <html:optionsCollection name="hours"/> 
             </html:select>
            </td>
            <td class="style2">Hrs</td>
            <td>
              <html:select property="flightMinutes">
                <html:optionsCollection name="minutes"/> 
              </html:select>
            </td> 
            <td class="style2">Mins</td>
            </tr></table></td>
            <td><table><tr>
            <td>
			 <html:select property="cutOffFlightHours">
                 <html:optionsCollection name="hours"/> 
             </html:select>
            </td>
            <td class="style2">Hrs</td>
            <td>
              <html:select property="cutOffFlightMinutes">
                <html:optionsCollection name="minutes"/> 
              </html:select>
            </td> 
            <td class="style2">Mins</td>
			</tr></table></td>
			<td><html:text property="airCraftType" maxlength="15"  /></td>
			
			
			
		 </tr>
		
		</table>
		<br/>
<%--		<table width="100%">	--%>
<%--			<tr>--%>
<%--			  <td colspan="14"  class="headerbluesmall">&nbsp;&nbsp;List Of Flight Schedule  </td> --%>
<%--			</tr>--%>
<%--		</table>--%>
		<table width="100%" class="tableBorderNew" style="border-left:0px;border-right:0px;border-bottom:0px;">
		<tr class="tableHeadingNew" >List Of Flight Schedule</tr>
		<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        
        %>
        <display:table name="<%=flightSheduleList%>" pagesize="5" class="displaytagstyle"  sort="list" id="agsstable" > 
			
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
  			<%
  			String sheduleTerminal=null;
			 String day="";
			    if(flightSheduleList != null && flightSheduleList.size()>0)
			    {
					AirFreightFlightShedules flightShedules=(AirFreightFlightShedules)flightSheduleList.get(i);
					//sheduleTerminal=flightShedules.getOriginTerminal().getTerminalLocation();
					day=flightShedules.getDay();
					flightShedules.setIndex(i);
				}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			 %>
  			<display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Days">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=tempPath%>"><%=day%></a></display:column>
  			<display:column property="flight" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Flight" />
  			<display:column property="stops" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Stops" />
  			<display:column property="departure" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Departure" />
			<display:column property="cutOffTime" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CutOff Time" />
			<display:column property="aircraftType" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Aircraft Type" />
			
			
  			<% i++;
  		   	   
  			%>
		     	    		
       		
		</display:table>
        
        </table></div>  
    	</td> 
  		</tr>  
		</table>
	</td>
	</table>	
		<html:hidden property="buttonValue" styleId="buttonValue"/>	
        <html:hidden property="index" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

