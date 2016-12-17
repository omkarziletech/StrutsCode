<%@ page import="com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.domain.VoyageMaster,com.gp.cong.logisoft.domain.VoyageExport,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FclBuy,com.gp.cong.logisoft.domain.FclBuyCost,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,java.text.DecimalFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%




	String trmNum="",trmNam="",destAirPortNo="",destAirPortName="",editpath="",message="",modify="",sslineno="",sslinename="";
	List searchvoyageList=null;
	List records=new ArrayList();
	AirRatesBean airRatesBean=new AirRatesBean();
	airRatesBean.setMatch("match");
	DecimalFormat df = new DecimalFormat("0.00");
	String path = request.getContextPath();
	VoyageExport voyageExport=new VoyageExport();
	
	if(session.getAttribute("trade")!=null){
		session.removeAttribute("trade");
		}
	if(request.getAttribute("airRatesBean")!=null){
		airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
		}
		request.setAttribute("airRatesBean",airRatesBean);
	if(request.getAttribute("warning")!=null){
		message=(String)request.getAttribute("warning");
		}
	if(session.getAttribute("searchvoyagerecords")!=null){
		voyageExport=(VoyageExport)session.getAttribute("searchvoyagerecords");
		if(voyageExport.getOriginTerminal()!=null){
			trmNum=voyageExport.getOriginTerminal().getTrmnum();
			trmNam=voyageExport.getOriginTerminal().getTerminalLocation();
			}
		if(voyageExport.getDestinationPort()!=null){
			destAirPortNo=voyageExport.getDestinationPort().getShedulenumber();
			destAirPortName=voyageExport.getDestinationPort().getPortname();
			}
		if(voyageExport.getLineNo()!=null){
			sslineno=voyageExport.getLineNo().getCarriercode();
			sslinename=voyageExport.getLineNo().getCarriername();
			}
	}
	if(session.getAttribute("fclmessage")!=null){
			message=(String)session.getAttribute("fclmessage");
		}
	if(request.getParameter("programid")!= null){
			 modify=(String)request.getParameter("programid");
			 session.setAttribute("processinfoforExport",modify);
		}
	if(session.getAttribute("searchvoyageList")!=null){
			searchvoyageList=(List)session.getAttribute("searchvoyageList");
			session.setAttribute("searchvoyageList",searchvoyageList);
		} 
	if(request.getAttribute("voyagemessage")!=null){
		message=(String)request.getAttribute("voyagemessage");
		}
	if(request.getParameter("modify")!= null){
		 modify=(String)request.getParameter("modify");
		 request.setAttribute("modifyforvoyageRates",modify);
		}else
		{
	 	modify=(String)request.getAttribute("modifyforvoyageRates");
		}
	editpath=path+"/searchVoyage.do";	
	session.setAttribute("voyagerate", "edit");
	airRatesBean.setMatch("match");
  %>
<html> 
	<head>
		<title>JSP for SearchVoyageForm form</title>
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
		
		<script language="javascript" type="text/javascript">
		 var newwindow = '';  
		function searchform(){
			if(document.searchVoyageForm.terminalNumber.value=="" || document.searchVoyageForm.destSheduleNumber.value==""){
					alert("Please select OriginTerminal  and Dest Airport  ");
					return;
				}
			document.searchVoyageForm.buttonValue.value="search";
			document.searchVoyageForm.index.value="get";
			document.searchVoyageForm.submit();
			}
		function searchallform(){
			document.searchVoyageForm.buttonValue.value="showall";
			document.searchVoyageForm.submit();
			}
  		function addform() {
			 if (!newwindow.closed && newwindow.location) {
             	newwindow.location.href = "<%=path%>/jsps/voyagemanagement/voyagePopUp.jsp";
           	  }else {
         		newwindow=window.open("<%=path%>/jsps/voyagemanagement/voyagePopUp.jsp?fclcorates="+"add","","width=600,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
              }
	          if (window.focus) {newwindow.focus()}
    	      return false;
    	  }    
	function getVoyageExport(){
	  window.location.href="<%=path%>/jsps/voyagemanagement/exportVoyage.jsp";
	 }
    function getOriginalTerminal(ev){  
    document.getElementById("terminalName").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "OrgTerminalName";
				 params['terminalNumber'] = document.searchVoyageForm.terminalNumber.value;
				  var bindArgs = {
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateTerminalNameDojo");
			    }
			 }
	   function populateTerminalNameDojo(type, data, evt) {
		   	if(data){
		   		document.getElementById("terminalName").value=data.terminalName;
		   	}
		   }
       function getOriginalTerminalName(ev){
       		document.getElementById("terminalNumber").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "OrgTerminalNumber";
				 params['terminalName'] = document.searchVoyageForm.terminalName.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateTerminalNumberDojo");
			    }
			 }
	   function populateTerminalNumberDojo(type, data, evt) {
		   	if(data){
		   		document.getElementById("terminalNumber").value=data.terminalNumber;
		   	}
		   }
      function getPortName(ev){
            document.getElementById("destAirportname").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "DestAirportname";
				 params['scheduleNumber'] = document.searchVoyageForm.destSheduleNumber.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateDestAirportname");
			    }
			 }
	   function populateDestAirportname(type, data, evt) {
		   	if(data){
		   		document.getElementById("destAirportname").value=data.destAirportname;
		   	}
		   }
        function getScheduleNumber(ev){
        document.getElementById("destSheduleNumber").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "ScheduleNumber";
				 params['destAirportName'] = document.searchVoyageForm.destAirportname.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateScheduleNumber");
			    }
			 }
	   function populateScheduleNumber(type, data, evt) {
		   	if(data){
		   		document.getElementById("destSheduleNumber").value=data.destSheduleNumber;
		   	}
		   }
      function getSslineName(ev){
      document.getElementById("sslinename").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "SslineName";
				 params['sslinenumber'] = document.searchVoyageForm.sslinenumber.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateSslineName");
			    }
			 }
	   function populateSslineName(type, data, evt) {
		   	if(data){
		   		document.getElementById("sslinename").value=data.sslinename;
		   	}
		   }
       function getSslineNumber(ev){
       document.getElementById("sslinenumber").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "SslineNumber";
				 params['sslinename'] = document.searchVoyageForm.sslinename.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateSslineNumber");
			    }
			 }
	   function populateSslineNumber(type, data, evt) {
		   	if(data){
		   		document.getElementById("sslinenumber").value=data.sslinenumber;
		   	}
		   }
	
		</script>
		<%@include file="../includes/resources.jsp" %>
	</head>
	<body class="whitebackgrnd" >
		<html:form action="/searchVoyage" scope="request">
		<font color="blue" size="4"><%=message%></font>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" >
				
	  			 <tr><td>&nbsp; </td></tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
			<td>
		    <table width="100%"  border="0"  >
		        <tr class="tableHeadingNew">Search Criteria</tr>
  				<tr><td></td></tr>
  		
  				<tr valign="top">
    			<td>
    			<table  border="0" ><tr class="style2"><td>Org Trm&nbsp;
	    			
	    			</td></tr></table></td>
	    			<td >
<%--	    			<html:text property="terminalNumber" size="2" onkeydown="titleLetter(this.value)" maxlength="2" styleId="terminalNumber" value="<%=trmNum%>"/>--%>
               <input name="terminalNumber" id="terminalNumber" size="15" onkeydown="getOriginalTerminal(this.value)"   value="<%=trmNum %>" maxlength="2"/>    
    		   <dojo:autoComplete formId="searchVoyageForm" textboxId="terminalNumber" action="<%=path%>/actions/getTerminal.jsp?tabName=EXPORT_VOYAGE&from=0"/>                
					</td>
					<td>&nbsp;</td>
	    			<td><span class="textlabels">Org Trm Name</span></td>
	    			<td >
<%--	    			<html:text property="terminalName"  onkeydown="titleLetterName(this.value)" value="<%=trmNam%>"/>--%>
	    			<input name="terminalName" id="terminalName" onkeydown=" getOriginalTerminalName(this.value)"   value="<%=trmNam %>" />    
    			   <dojo:autoComplete formId="searchVoyageForm" textboxId="terminalName" action="<%=path%>/actions/getTerminalName.jsp?tabName=EXPORT_VOYAGE&from=0"/>
	    			</td>
	    			
					</tr>
		    		<tr valign="top">	
    				<td class="textlabels">
    					<table border="0" ><tr class="style2"><td>POD No&nbsp;</td></tr></table> </td>
		    			<td >
<%--		    			<html:text property="destSheduleNumber"  size="5" onkeydown="getDestination(this.value)" maxlength="5" value="<%=destAirPortNo%>"></html:text><br>--%>
		    	<input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getPortName(this.value)"  value="<%=destAirPortNo %>" size="15" maxlength="5"/>    
    			<dojo:autoComplete formId="searchVoyageForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getPorts.jsp?tabName=EXPORT_VOYAGE&from=0"/>
		    			</td>
		    			<td></td>
		    			<td><span class="textlabels">POD Name</span></td>
		    			<td >
<%--		    			<html:text property="destAirportname" onkeydown="getDestinationName(this.value)" value="<%=destAirPortName%>"/><br>--%>
		    	 <input name="destAirportname" id="destAirportname"  onkeydown="getScheduleNumber(this.value)"  value="<%=destAirPortName %>" />    
    		    <dojo:autoComplete formId="searchVoyageForm" textboxId="destAirportname" action="<%=path%>/actions/getPorts.jsp?tabName=EXPORT_VOYAGE&from=1"/>
		    			</td>
		    			<td></td>
		    			<td></td>
		    			<td></td>
		    			<td></td>
		    		</tr>
	    			<tr valign="top">	
    				<td>
    					<table  border="0"><tr class="style2"><td>SS Line number&nbsp;</td></tr></table> </td>
			    		<td >
<%--			    		<html:text property="sslinenumber"  size="6" onkeydown="getSSLine(this.value)" maxlength="5" value="<%=sslineno%>"/>--%>
			    	<input name="sslinenumber" id="sslinenumber" size="15" maxlength="5" value="<%=sslineno%>" onkeydown="getSslineName(this.value)"  />
    			    <dojo:autoComplete formId="searchVoyageForm" textboxId="sslinenumber" action="<%=path%>/actions/getSsLineNo.jsp?tabName=EXPORT_VOYAGE&from=0"/>
			    		</td>
    			<td>&nbsp;</td>
    			<td class="textlabels">SS Line Name</td>
    			<td >
<%--    			<html:text property="sslinename"  onkeydown="getSSLineName(this.value)" value="<%=sslinename%>"/>--%>
    			<input name="sslinename" id="sslinename"  value="<%=sslinename%>" onkeydown="getSslineNumber(this.value)" />
    			<dojo:autoComplete formId="searchVoyageForm" textboxId="sslinename" action="<%=path%>/actions/getSsLineName.jsp?tabName=EXPORT_VOYAGE&from=1"/>
    			</td>
    			
			    			
			    			
			    			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td></td>
							<td>&nbsp;</td>
			    			<td>&nbsp;</td>
			    		</tr>	
			    <tr>		
				<table>	
			    <tr>
			      <td>
			       <table border="0">
			       
			        <td class="style2">Match Only
					<html:radio property="match" value="match" name="airRatesBean"></html:radio>
					Start list at
					<html:radio property="match" value="starts" name="airRatesBean"></html:radio></td>
					</table>
					</td>
			    
			    </tr>
			    </table>
			
			   </tr>		
			    		
			    <tr>
			    
			            <td valign="top" colspan="6" align="center" style="padding-top:10px;">
			             <input type="button" value="Search"  onclick="searchform()" class="buttonStyleNew"/>
			             <input type="button" value="Add New"  onclick="return GB_show('Export Voyage','<%=path%>/jsps/voyagemanagement/voyagePopUp.jsp?fclcorates='+'add',150,600)"  class="buttonStyleNew"/>
			            
			            </td>
			   
			    </tr>
    		
    	
  			
  			</table>
  			</td>
  			</table>
  			<br/><br/>
	<table width="100%" class="tableBorderNew">
	<tr class="tableHeadingNew">Export Voyages {Match Only}</tr>

		<tr><td>
		<table width="100%">
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
          		<table width="80%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;

          		%>
          	 	<display:table name="<%=searchvoyageList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="lclcoloadratestable" sort="list" style="width:100%"> 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Voyages Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="Voyage"/>
				<display:setProperty name="paging.banner.items_name" value="Voyages"/>
  				
<%
		  String vesselName="", vessel="",Sslinename="",Sslineno="",portName="",terminal="",saildate="",
		  port="", eciVoyage="",flightSsVoyage="",transitDaysOverride="",transShipments="",agentForVoyage="",
		  truckingInfo="",link ="";
		  String ratAmount[]=new String[10];
		  List unittypelist=new ArrayList();
		  Set set=new HashSet();
		  int k=0;
		 VoyageExport voyageExports=new VoyageExport();
  				 
  		    if (request.getAttribute("voyageExport") != null) {
					request.removeAttribute("voyageExport");
					}
			if (session.getAttribute("addvoyagerecords") != null) {
				session.removeAttribute("addvoyagerecords");
			}
			if (request.getAttribute("modifyforvoyageRates") != null) {
				request.removeAttribute("modifyforvoyageRates");
			}
			if (session.getAttribute("modify") != null) {
				session.removeAttribute("modify");
			}
			if (session.getAttribute("programid") != null) {
				session.removeAttribute("programid");
			}
			if(session.getAttribute("voyageExport1")!=null){
				session.removeAttribute("voyageExport1");
			}
			if(request.getAttribute("voyageExprt")!=null){
				request.removeAttribute("voyageExprt");
			}
			if (session.getAttribute("view") != null) {
				session.removeAttribute("view");
			}
  		if (searchvoyageList != null && searchvoyageList.size() > 0){
  						 voyageExports=(VoyageExport)searchvoyageList.get(i);
  						 session.setAttribute("searchvoyageList",searchvoyageList);
						if(voyageExports.getId()!=null){
							if(voyageExports.getOriginTerminal()!=null){
								terminal=voyageExports.getOriginTerminal().getTrmnum().toString();
							}
		if(voyageExports.getDestinationPort()!=null){
			port=voyageExports.getDestinationPort().getShedulenumber().toString();
			portName=voyageExports.getDestinationPort().getPortname().toString();
		}
  						if(voyageExports.getInternalVoyage()!=null){
			eciVoyage=voyageExports.getInternalVoyage();
		}
		if(voyageExports.getFligtSsVoyage()!=null){
			flightSsVoyage=voyageExports.getFligtSsVoyage();
		}
		
		if(voyageExports.getTransShipments()!=null){
			transShipments=voyageExports.getTransShipments();
		}
		
		if(voyageExports.getTransitDaysOverride()!=null){
			transitDaysOverride=voyageExports.getTransitDaysOverride();
		}
		if(voyageExports.getAgentForVoyage()!=null){
			agentForVoyage=voyageExports.getAgentForVoyage();
		}
		if(voyageExports.getTruckingInfo()!=null){
			truckingInfo=voyageExports.getTruckingInfo();
		}
		if(voyageExports.getSailDate()!=null){
			DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
			saildate=df1.format(voyageExports.getSailDate());
		}
		if(voyageExports.getLineNo()!=null){
			Sslineno=voyageExports.getLineNo().getCarriercode();
			Sslinename=voyageExports.getLineNo().getCarriername();
		}
		if(voyageExports.getVesselNo()!=null){
			vessel=voyageExports.getVesselNo().getCode().toString();
			vesselName=voyageExports.getVesselNo().getCodedesc().toString();
			}
  						
  		}
		link=editpath+"?param="+voyageExports.getId();	
	}
  				  String iStr=String.valueOf(i);
                  String tempPath=editpath+"?ind="+iStr;	
%>
 		 	    <display:column title="" property="voyageChange" style="width:2%;visibility:hidden;color:red"><%=""%></display:column>
  			   <display:column   title="Term." paramId="paramid" sortable="true"  paramProperty="id">&nbsp;&nbsp;
				<a  href="<%=tempPath%>"><%=terminal%></a>
				</display:column>
				<display:column  sortable="true" title="ECI Voyage"><%=eciVoyage%></display:column>
				<display:column sortable="true" title="Schedule Code"><%=port%></display:column>
				<display:column sortable="true" title="Port Name"><%=portName%></display:column>
				<display:column sortable="true" title="Sail Dt."><%=saildate%></display:column>
				<display:column sortable="true" title="SSL#"><%=Sslineno%></display:column>
				<display:column sortable="true" title="SSL Name"><%=Sslinename%></display:column>
				<display:column sortable="true" title="Vessel"><%=vesselName%></display:column>
				<%
				if(unittypelist!=null){
				for(int j=0;j<unittypelist.size();j++)
				{
				%>
				<display:column sortable="true" title="<%=(String)unittypelist.get(j)%>"><%=ratAmount[j]%></display:column>
				
				<%}
				}i++; %>
				<display:column title="Actions">
				    <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong',null,event);" onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" border="0" onclick="window.location.href('<%=link%>')" /></span>
				</display:column>
				
				
		 		</display:table>
		 
   	 </table>
   	 <!-- This is for common commdity -->

   	

<html:hidden property="buttonValue" styleId="buttonValue" />
<html:hidden property="index"  />
<script> load()</script>
	</html:form>
	</body>
</html>

