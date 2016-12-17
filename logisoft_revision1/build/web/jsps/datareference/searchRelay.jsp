<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="org.apache.commons.lang3.StringUtils
,com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.beans.SearchRelayBean"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RelayInquiry,com.gp.cong.logisoft.domain.RelayOrigin,com.gp.cong.logisoft.beans.SearchRelayBean,com.gp.cong.logisoft.domain.RelayInquiryTemp"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();

String buttonValue="load";
String msg="";
String message="";
String modify=null;
List relayList=null;
String poldesc = "";
String polid = "";
String poddesc = "";
String podid = "";
RelayInquiry portobj1= null;

if(session.getAttribute("pierList")!=null)
{
	session.removeAttribute("pierList");
}
SearchRelayBean srBean=new SearchRelayBean();
RelayInquiry relay=new RelayInquiry();
relay.setMatch("starts");
session.getAttribute("RelayCaption");

if(session.getAttribute("searchrelaycode")!=null )
{
    
	portobj1=(RelayInquiry)session.getAttribute("searchrelaycode");
	
	if(portobj1.getPolCode()!=null)
	{
		polid=portobj1.getPolCode().getShedulenumber();
		poldesc=portobj1.getPolCode().getPortname();
	}
	if(portobj1.getPodCode()!=null)
	{
	podid=portobj1.getPodCode().getShedulenumber();
	poddesc=portobj1.getPodCode().getPortname();
	}
}
if(request.getAttribute("srBean")!=null)
	{
 		srBean=(SearchRelayBean)request.getAttribute("srBean");
 		relay.setMatch(srBean.getMatch());
 		
	} 
request.setAttribute("relay",relay);

if(request.getAttribute("msg")!=null)
{
	message=(String)request.getAttribute("msg");
}
if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
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
if(session.getAttribute("relayList")!=null)
{
	relayList=(List)session.getAttribute("relayList");
} 
if(request.getAttribute("buttonValue")!=null)
{
			buttonValue=(String)request.getAttribute("buttonValue");
} 
if(request.getParameter("programid")!= null && session.getAttribute("processinfoforrelay")==null)
{
  	 buttonValue="searchall";
  	
}

if(buttonValue.equals("searchall"))
{

 	relayList=dbUtil.getAllRelay();
 	session.setAttribute("relayList",relayList);
 	if(request.getParameter("programid")!=null)
{
session.setAttribute("processinfoforrelay",request.getParameter("programid"));
}
 	session.setAttribute("RelayCaption","List of Relay");
}
// Name:Rohith Date:12/04/2007  setting the path to Search Relay action
String editPath=path+"/searchRelay.do";
%>
 
<html>
  <head>
	<base href="<%=basePath%>">
	<title>JSP for Search Relay form</title>

	<%@include file="../includes/baseResources.jsp" %>
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    dojo.require("dojo.io.*");
			dojo.require("dojo.event.*");
			dojo.require("dojo.html.*");
		</script>
	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>
		<script type="text/javascript">
		
		var commonChargesVerticalSlide;
		window.addEvent('domready', function() {
				commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
				commonChargesVerticalSlide.toggle();
				$('commonChargesToggle').addEvent('click', function(e){
					commonChargesVerticalSlide.toggle();
				});				
			});
	
	 var newwindow = '';
           function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/datareference/relayCode.jsp?relay="+"add";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/datareference/relayCode.jsp?relay="+"add","","width=450,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           } 
	function disabled(val)
    {
		if(val == 0)
		{		
        	var imgs = document.getElementsByTagName('img');
   			for(var k=0; k<imgs.length; k++)
   			{
   				 if(imgs[k].id != "showall" && imgs[k].id!="search")
   				 {
   		 			imgs[k].style.visibility = 'hidden';
   				 }
   			}
  	 	}
  	 	var tables = document.getElementById('relaytable');
  	 	   if(tables!=null)
  	 	   {
    			//displaytagcolor();
   				//initRowHighlighting();
   				setShowAllStyle();
   		   }		
  			 }
  			function setShowAllStyle()
		  {
		
		  	if(document.searchRelay.buttonValue.value=="searchall")
		  	{
		  	 var x=document.getElementById('relaytable').rows[0].cells;	
		  
		     x[0].className="sortable sorted order1";
		  	}
		  	if(document.searchRelay.buttonValue.value=="search")
		  	{
		  	var input = document.getElementsByTagName("input");
		  	var select = document.getElementsByTagName("select");
	  		 			if(!input[0].value=="")
	  					{
	  					
	  		 				 var x=document.getElementById('relaytable').rows[0].cells;	
	  		   				 x[0].className="sortable sorted order1";
	  		   			}
	  		   			else if(!input[1].value=="")
	  					{
	  					
	  		 				 var x=document.getElementById('relaytable').rows[0].cells;	
	  		   				 x[1].className="sortable sorted order1";
	  		   			}
	  		   			
	  		   		
	  		   		else if(!select[0].value=="0")
	  					{
	  		 				 var x=document.getElementById('relaytable').rows[0].cells;	
	  		   				 x[2].className="sortable sorted order1";
	  		   			}
	  				}
		 }  
		  function displaytagcolor()
		  {
				var datatableobj = document.getElementById('relaytable');
				for(i=0; i<datatableobj.rows.length; i++)
				{
					var tablerowobj = datatableobj.rows[i];
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
				}
		 }
		 function initRowHighlighting()
  		{
			if (!document.getElementById('relaytable')){ return; }
			var tables = document.getElementById('relaytable');
			attachRowMouseEvents(tables.rows);
		}

		function attachRowMouseEvents(rows)
		{
			for(var i =1; i < rows.length; i++)
			{
				var row = rows[i];
				row.onmouseover =	function() 
								{ 
									this.className = 'rowin';
								}
				row.onmouseout =	function() 
								{ 
									this.className = '';
								}
            	row.onclick= function() 
								{ 
							 	}
			}
		
		}	
		 
	function searchform()
	{
		if(document.searchRelay.pol.value=="0" && document.searchRelay.pod.value=="0")
		{
			alert("Please select any Search Criteria ");
			document.searchRelay.pol.value="";
			document.searchRelay.pol.focus();
			return;
		}
		document.searchRelay.buttonValue.value="search";
		document.searchRelay.submit();
	}
	function searchallform()
	{	
		document.searchRelay.buttonValue.value="searchall";
		document.searchRelay.submit();
	}
	function submit1()
    { 
    	document.searchRelay.buttonValue.value="polselected";
  	 	document.searchRelay.submit();
    }	
	function submit2()
    {
        document.searchRelay.buttonValue.value="podselected";
  	 	document.searchRelay.submit();
    }	
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
		function test(){
			
			window.location.href="<%=path%>/jsps/datareference/addRelay.jsp";
			
			}
	function getPolName(ev){ 
	document.getElementById("polText").value="";
			if(event.keyCode==9 || event.keyCode==13){
			   
				 var params = new Array();
				 params['requestFor'] = "ScheduleCode";
				 params['scheduleCode'] = document.searchRelay.pol.value;
			     var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 
				 var req = dojo.io.bind(bindArgs);
			 
				 dojo.event.connect(req, "load", this, "populatePolName");
				
			    }
			 }
			 
		   function populatePolName(type, data, evt) {
		   	if(data){
		   		document.getElementById("polText").value=data.portname;
		   	
		   	}
		   }
		function getPortofDischarge(ev){ 
		    document.getElementById("podText").value="";
			if(event.keyCode==9 || event.keyCode==13){
			     var params = new Array();
				 params['requestFor'] = "ScheduleCode";
				 params['scheduleCode'] = document.searchRelay.pod.value;
			     var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			  
				 dojo.event.connect(req, "load", this, "populatePodName");
				
			    }
			 }
			 
		   function populatePodName(type, data, evt) {
		   	if(data){
		   		document.getElementById("podText").value=data.portname;
		   	
		   	}
		   }
		   function getPolValue(ev){ 
		   document.getElementById("pol").value="";
			if(event.keyCode==9 || event.keyCode==13){
			     var params = new Array();
				 params['requestFor'] = "DestinationPortNumberInCodes";
				 params['destinationPortName'] = document.searchRelay.polText.value;
			     var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			  
				 dojo.event.connect(req, "load", this, "populatePolValue");
				
			    }
			 }
			 
		   function populatePolValue(type, data, evt) {
		   	if(data){
		   		document.getElementById("pol").value=data.DestinationNumberInCodes;
		   	
		   	}
		   }
		   	   function getPodValue(ev){ 
			if(event.keyCode==9 || event.keyCode==13){
			     document.getElementById("pod").value="";
			     var params = new Array();
				 params['requestFor'] = "DestinationPortNumberInCodes";
				 params['destinationPortName'] = document.searchRelay.podText.value;
			     var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			  
				 dojo.event.connect(req, "load", this, "populatePodValue");
				
			    }
			 }
			 
		   function populatePodValue(type, data, evt) {
		   	if(data){
		   		document.getElementById("pod").value=data.DestinationNumberInCodes;
		   	
		   	}
		   }
</script>
<%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd" >
<html:form action="/searchRelay" name="searchRelay" type="com.gp.cong.logisoft.struts.form.SearchRelayForm" scope="request">
<font color="blue"><h4><%=msg%></h4></font>	
    		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorderNew">
   			<tr class="tableHeadingNew"><td >Search Criteria</td>
   			<td align="right">	<input type="button" class="buttonStyleNew" id="addnew" value="Add New"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/relayCode.jsp?relay='+'add',150,600)"/></td>
   			</tr>
   			<tr>
   			<td colspan="2">
   			<table width="100%" border="0" cellspacing="3" cellpadding="0">
				<tr>
				<td class="textlabels" >POL</td>
				<td>
		         <input  name="pol" id="pol" value="<%=polid%>" class="smalldropdownStyle" onkeydown="getPolName(this.value)" style="width:215px"/>   
  		           <dojo:autoComplete formId="searchRelay" textboxId="pol" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_CODE&from=0"/>
               </td>
     			<td class="textlabels" > POL Name</td>
				<td>
				         <input  name="polText" id="polText" class="smalldropdownStyle" value="<%=poldesc%>" onkeydown="getPolValue(this.value)" style="width:215px"/>   
	     		          <dojo:autoComplete formId="searchRelay" textboxId="polText" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_CODE&from=1"/>
		            </td>
                </tr>
				<tr>
					<td  class="textlabels">POD</td>
						<td>
				         <input  name="pod" id="pod" value="<%=podid%>" class="smalldropdownStyle" onkeydown="getPortofDischarge(this.value)" style="width:215px"/>   
	     		           <dojo:autoComplete formId="searchRelay" textboxId="pod" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_CODE&from=2"/>
		               </td>
				<td class="textlabels"> POD Name</td>
					
					<td>
				         <input  name="podText" id="podText" class="smalldropdownStyle" value="<%=poddesc%>" onkeydown="getPodValue(this.value)" style="width:215px"/>   
	     		          <dojo:autoComplete formId="searchRelay" textboxId="podText" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_CODE&from=3"/>
		            </td>

			
	</tr>
	<tr>
	<td colspan="2" class="textlabels"> <html:radio property="match" value="match" name="relay" />Match Only
		<html:radio property="match" value="starts" name="relay" />Start list at</td>
  <tr>	
					<td colspan="4" align="center">
				    <input type="button" class="buttonStyleNew" id="search" value="Search" onclick="searchform()"/>
					<input type="button" class="buttonStyleNew" id="showall" value="Show All" onclick="searchallform()"/>
				</tr>
			
    	 	</table>
    	 	</td>
    	 	</tr>
    	 	</table>
     	<br> 
 	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
 	<tr class="tableHeadingNew"><td><%=session.getAttribute("RelayCaption") %> </td>
 	<td align="right">
 	
<%-- 	<a id="commonChargesToggle" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a>--%>
 	
 	 </td>
 	</tr>
  	<tr>
        <td align="left" scope="row" colspan="2">
<%--        <div id="common_Charges_vertical_slide">--%>
        <div id="divtablesty1" class="scrolldisplaytable">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" >
          	<%
         		 int i=0;
          	%>
          	
          	 <display:table name="<%=relayList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="relaytable" sort="list" style="width:100%"> 
			 	<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Relay Displayed,For more Data click on Page Numbers.
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
    			<display:setProperty name="basic.msg.empty_list">
    			    <span class="pagebanner">
					No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="Relay"/>
  				<display:setProperty name="paging.banner.items_name" value="Relays"/>
  				<%
  					String polName = "";
  					String podName = "";
  					String poName="";
  					String link ="";
  					String name =null;
  					if(relayList!=null && relayList.size()>0)
  					{
  						RelayInquiryTemp relayInquiry=(RelayInquiryTemp)relayList.get(i); 			
  						if(relayInquiry!=null )
  						{
  							polName=relayInquiry.getPolName().toString();
  							podName=relayInquiry.getPodName().toString();
  							
  							poName=polName+"-"+podName;
  							name = StringUtils.abbreviate(poName,16) ;
  						   
  						}
  						link = editPath+"?param="+relayInquiry.getId();
  					}
  					
  		 		%>
  		 		
				<display:column sortable="true" property="status" title="Sts"/>
				<display:column sortable="true" property="cutOffDays" title="Days"/>
				<display:column sortable="true" property="hourOfDay" title="Hour"/>
				<display:column sortable="true" property="printOnSs" title="SS"/>
				<display:column sortable="true"  property="ttFromPolToPod" title="TT"/>
				<display:column sortable="true" property="noOfOrigins" title=" No. Of Org"></display:column>
			    <display:column sortable="true" property="noOfDestns"  title="No. Of FDs"></display:column>
			    <display:column sortable="true" property="noOfPortExcptns"  title="No. Of Exc"></display:column>
				<display:column title="POL-POD" href="<%=editPath%>"  paramId="paramid" sortable="true" paramProperty="relayId">
				<span onmouseover="tooltip.show('<strong>POL</strong> - <%=polName %><br/> <strong>POD</strong> - <%=podName %> ',null,event);" onmouseout="tooltip.hide();"><%=name%></span>  </display:column>
	  		    
			    <display:column title="Actions">
			     <span onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);" onmouseout="tooltip.hide();"> <img src="<%=path%>/img/icons/pubserv.gif" onclick="window.location.href='<%=link %>' "/> </span>
			    </display:column>
			 <%i++; %>
			 
		 </display:table>
   	 </table>
   	 </div>
<%--   	 </div>--%>
  </td>
 </tr>
</table>	
 
<html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
 <script>disabled('<%=modify%>')</script>
</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

