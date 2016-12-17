<%@ page import="com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO,com.gp.cong.logisoft.hibernate.dao.FclBuyDAO,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FclBuy,com.gp.cong.logisoft.domain.FclBuyCost,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,java.text.DecimalFormat,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<jsp:useBean id="fclSellRatesDojo" class="com.gp.cong.logisoft.domain.FclBuy" scope="request"/>   

<%
String path = request.getContextPath();
DecimalFormat df2 = new DecimalFormat("########0.00");
FclBuy fclBuy=new FclBuy();

 List displayList=(List)request.getAttribute("displayList");
String orgTerminalNo="",orgTerminalName="",destinationPort="",destinationPortName="",commodityCode="610000",commodityCodeDesc="";
String ssLineNo="",ssLineName="",orgCap="",destCap="",comCap="",ssLineCap=""; 
if(request.getAttribute("fclSellRatesDojo")!=null)
  {
		fclBuy=(FclBuy)request.getAttribute("fclSellRatesDojo");
		if(fclBuy.getOriginTerminal()!=null){
			orgTerminalNo=fclBuy.getOriginTerminal().getUnLocationCode();
			orgTerminalName=fclBuy.getOriginTerminal().getUnLocationName();
			orgCap="Origin";
		}
		if(fclBuy.getDestinationPort()!=null){
			destinationPort=fclBuy.getDestinationPort().getUnLocationCode();
			destinationPortName=fclBuy.getDestinationPort().getUnLocationName();
			destCap="Destination";
		}
		if(fclBuy.getComNum()!=null){
			commodityCode=fclBuy.getComNum().getCode();
			commodityCodeDesc=fclBuy.getComNum().getCodedesc();
			comCap="Commodity";
		}
		if(fclBuy.getSslineNo()!=null){
			ssLineNo=fclBuy.getSslineNo().getAccountno();
			ssLineName=fclBuy.getSslineNo().getAccountName();
			ssLineCap="SSLine Number";
		}
  }		
if(request.getAttribute("closeRemarksPage")!=null){
%>
<script>
parent.parent.GB_hide();
//self.close();
</script>
<%} %>
<html> 
	<head>
		<title>FclBuyRates JSP</title>
		<%@include file="../includes/baseResources.jsp" %>
	<script language="javascript" src="<%=path%>/js/tooltip/tooltip.js"></script>
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
		  
		   function getOriginalTerminal(ev){
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "OrgTerminalName";
				 params['terminalNumber'] = document.searchFCLForm.terminalNumber.value;
				  var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateTerminalNameDojo");
				
				    //document.searchFCLForm.terminalName.value="";
				   //document.searchFCLForm.buttonValue.value="dojosearch";
				  //document.searchFCLForm.submit();
			     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
			    }
			 }
			 
		   function populateTerminalNameDojo(type, data, evt) {
		   	if(data){
		   		document.getElementById("terminalName").value=data.terminalName
		   	}
		   }		 
		   
	var termName = '';
	function getOriginalTerminalName(ev){ 
		document.getElementById("terminalNumber").value="";
    	if(event.keyCode==9 || event.keyCode==13){
    		if(ev.indexOf("-")!=-1){
    	    	var string  = ev;
    	   		var index = ev.indexOf("-");
    	   		var orgTrm = string.substring(index+1,string.length);
    	    	var orgTrmName=string.substring(0,index);
    	  		document.searchFCLForm.terminalName.value=orgTrmName;
    	    	document.getElementById("terminalNumber").value=orgTrm;
    	    }else{
    	    	var params = new Array();
			  	params['requestFor'] = "OrgTerminalNumber";
			  	params['terminalName'] = document.searchFCLForm.terminalName.value;
			  	termName = document.searchFCLForm.terminalName.value;
			  	var bindArgs = {
	  		  		url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  		  		error: function(type, data, evt){alert("error");},
	 		  		mimetype: "text/json",
	  				content: params
				};
				
				document.searchFCLForm.terminalName.value=document.searchFCLForm.terminalName.value;
				var req = dojo.io.bind(bindArgs);
				dojo.event.connect(req, "load", this, "populateTerminalNumber");
			}
			
		}
	}
	function populateTerminalNumber(type, data, evt) {
  		if(data){
   			document.getElementById("terminalNumber").value=data.terminalNumber;
   			document.searchFCLForm.terminalName.value=termName;
   		}
	}
		 function getDestinationPort(ev){
		    if(event.keyCode==9 || event.keyCode==13){
		    
			    var params = new Array();
					 params['requestFor'] = "DestinationPortName";
					 params['destSheduleNumber'] = document.searchFCLForm.destSheduleNumber.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateDestinationPortNameDojo");
		    
				//document.searchFCLForm.destAirportname.value="";
				//document.searchFCLForm.buttonValue.value="dojosearch";
				//document.searchFCLForm.submit();
			//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
			   }
		    }
		    function populateDestinationPortNameDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("destAirportname").value=data.destAirportname
		   	    }
		    }
		   var destPortName='';
	function getDestinationPortName(ev){ 
		document.getElementById("destSheduleNumber").value="";
    	if(event.keyCode==9 || event.keyCode==13){
    		if( ev.indexOf("-")!= -1){
				var string  = ev;
    	   		var index = ev.indexOf("-");
    	   		var destTrm = string.substring(index+1,string.length);
    	    	var destTrmName=string.substring(0,index);
				document.searchFCLForm.destAirportname.value = destTrmName;
				document.getElementById("destSheduleNumber").value= destTrm;
			}else{
				var params = new Array();
				destPortName = document.searchFCLForm.destAirportname.value;
				params['requestFor'] = "OrgTerminalNumber";
				params['terminalName'] = document.searchFCLForm.destAirportname.value;
				var bindArgs = {
	  				url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  				error: function(type, data, evt){alert("error");},
	 				mimetype: "text/json",
	  				content: params
				};
				var req = dojo.io.bind(bindArgs);
				dojo.event.connect(req, "load", this, "populateDestPortName");
			}
		}
	}
	function populateDestPortName(type, data, evt) {
  		if(data){
   			document.getElementById("destSheduleNumber").value=data.terminalNumber;
   			document.searchFCLForm.destAirportname.value = destPortName;
   		}
	}	 
	      function getComCode(ev){
			if(event.keyCode==9 || event.keyCode==13){
				var params = new Array();
					 params['requestFor'] = "CommodityCodeDescription";
					 params['commodityCode'] = document.searchFCLForm.comCode.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateCommodityDescriptionDojo");
			
				//document.searchFCLForm.comDescription.value="";
				//document.searchFCLForm.buttonValue.value="dojosearch";
				//document.searchFCLForm.submit();
				//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
			   }
		     }
		    function populateCommodityDescriptionDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("comDescription").value=data.commodityDescription
		   	     }
		     } 
		   
		   function getComCodeName(ev){
			if(event.keyCode==9 || event.keyCode==13){	
			       var params = new Array();
					 params['requestFor'] = "CommodityCode";
					 params['commodityCodeDescription'] = document.searchFCLForm.comDescription.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateCommodityCodeDojo");
			
				//document.searchFCLForm.comCode.value="";
				//document.searchFCLForm.buttonValue.value="dojosearch";
				//document.searchFCLForm.submit();
			//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
					}
		    } 
		  function populateCommodityCodeDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("comCode").value=data.commodityCode
		   	     }
		     }   
		     
		 function getSSlineCode(ev){
		  if(event.keyCode==9 || event.keyCode==13){
		  	var params = new Array();
					 params['requestFor'] = "SSLineName";
					 params['custNo'] = document.searchFCLForm.sslinenumber.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getCustDetails.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateSSLineNameDojo");
		  
			//document.searchFCLForm.sslinename.value="";
			//document.searchFCLForm.buttonValue.value="dojosearch";
			//document.searchFCLForm.submit();
			}
		  }
		  function populateSSLineNameDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("sslinename").value=data.custName;
		   	     }
		     }
		     
		     
	     function getSSlineName(ev) {
		  if(event.keyCode==9 || event.keyCode==13){
		  
		     var params = new Array();
					 params['requestFor'] = "SSLineNo";
					 params['custName'] = document.searchFCLForm.sslinename.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getCustDetails.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateSSLineNoDojo");
		  
		//	document.searchFCLForm.sslinenumber.value="";
		//	document.searchFCLForm.buttonValue.value="dojosearch";
		//	document.searchFCLForm.submit();
			}
	 	}
	 	 function populateSSLineNoDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("sslinenumber").value=data.custNumber;
		   	     }
		     }
		     
	  function search(){
	  if(document.searchFCLForm.terminalNumber.value=="" && document.searchFCLForm.destSheduleNumber.value=="")
	     {
	      alert("please select OriginTerminal and DestinationPort");
	      return;
	     }
	      document.searchFCLForm.buttonValue.value="search";
		  document.searchFCLForm.submit();
	  }	
	  
	  function clearScreen() {
	    document.searchFCLForm.buttonValue.value="clear";
		document.searchFCLForm.submit();
	  }
	  
	  function  getdisabled(){
	          
	  		 var tables1 = document.getElementById('displayTable');
	  		 if(tables1!=null) {
		  	 	 //displaytagcolor();
		   		 //initRowHighlighting();
	   		 }
	 	    
	   	}
	   function displaytagcolor(){
			 var datatableobj = document.getElementById('displayTable');
			 for(i=0; i<datatableobj.rows.length; i++){
						var tablerowobj = datatableobj.rows[i];
						if(i%2==0){
							tablerowobj.bgColor='#FFFFFF';
						}else{
							tablerowobj.bgColor='#E6F2FF';
						}
					}
			}
			
	  function initRowHighlighting(){
				if (!document.getElementById('displayTable')){ return; }
				 var tables = document.getElementById('displayTable');
				attachRowMouseEvents(tables.rows);
			}
		 
	
	 </script>
	   
	    <%@include file="../includes/resources.jsp" %>
	    
	    </head>
	    <%
			if(session.getAttribute("pageCollapse")!=null){ 
		%>
		<body class="whitebackgrnd" onLoad="getdisabled()">
		<% 
			}else{  
		%>
		<body class="whitebackgrnd">
		<%
		}
		%>
		<html:form action="/fclSellRates" scope="request">
	    <table width="100%"  border="0" cellpadding="2"  cellspacing="1" class="tableBorderNew">
	   		 <%
 		 	if(session.getAttribute("pageCollapse")==null){
			%>
			<tr class="tableHeadingNew" height="90%"><b>Search Current FCL Sell Rates</b></tr>
  	 	    <tr valign="top" class="textlabels">
  	 	    <td><table width="100%" border="0">
  	 	    <tr>
 			 <td class="style2">Org Region</td>
 			 <td><input name="orgRegion" id="orgRegion" size="30">
 			<dojo:autoComplete formId="searchFCLForm" textboxId="orgRegion" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=FCL_SELL_RATES&from=0"/>
 			 </td>
 			 </tr>
	    	    <tr class="style2">
	    	        <td>Org Trm</td>
	    		    <td>
	                    <input name="terminalNumber" id="terminalNumber" size="30" value="<%=orgTerminalNo%>" onkeydown="getOriginalTerminal(this.value)"  />
			            <dojo:autoComplete formId="searchFCLForm" textboxId="terminalNumber" action="<%=path%>/actions/?tabName=FCL_SELL_RATES&from=0"/>
             	    </td>
             	 </tr>
             	 <tr valign="top" class="style2">
    		  	  	 <td>Dest Port</td>
		    	 	 <td>
		    				<input  name="destSheduleNumber" id="destSheduleNumber" size="30" value="<%=destinationPort%>" maxlength="5"  onkeydown="getDestinationPort(this.value)" />
	                		<dojo:autoComplete  formId="searchFCLForm" textboxId="destSheduleNumber"  action="<%=path%>/actions/?tabName=FCL_SELL_RATES&from=1"/>
		    	  	 </td>
		    	</tr>
             	<tr valign="top" class="style2">	
    			    <td>Com Code</td>
			        <td>
			            <input name="comCode"  id="comCode" maxlength="6" size="30" value="<%=commodityCode%>" onkeydown="getComCode(this.value)" />
    			        <dojo:autoComplete formId="searchFCLForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=FCL_SELL_RATES&from=1"/>
<%--			       		<div dojoAttachEvent="onclick, "></div>--%>
			       </td>
			   </tr> 
			   <tr class="textlabels">
  			       <td>SS Line Code</td>
    			   <td>
    				 <input name="sslinenumber" id="sslinenumber" size="30"  maxlength="5" value="<%=ssLineNo%>" onkeydown="getSSlineCode(this.value)" />
    			     <dojo:autoComplete formId="searchFCLForm" textboxId="sslinenumber" action="<%=path%>/actions/getCustomer.jsp?tabName=FCL_SELL_RATE&from=0"/>
    			 </td>
    		  </tr>
    		  <tr valign="top" class="textlabels">
    			 <td>Unit Type</td>
    			 <td><html:select property="unitType" multiple="true">
    			 <html:optionsCollection name="unittypelist"/>
    			 </html:select></td>
    			 <td style="padding-left:2px;"></td>
    			 <td style="padding-left:2px;"></td>
    		 </tr>
    	</table><td>
    	<td><table  width="100%" border="0">
    			 <tr>
 					 <td class="textlabels" align="right">Dest Region</td>
 				 	<td ><input name="destRegion" id="destRegion" size="30" >
 					 	<dojo:autoComplete formId="searchFCLForm" textboxId="destRegion" action="<%=path%>/actions/getChargeCode.jsp?tabName=FCL_SELL_RATES&from=0"/>
 			     	</td>
 			 	</tr>	 
             	 <tr>
				 	<td class="textlabels" align="right">Org Trm Name</td>
	    		 	<td>
	    		  	 	<input  name="terminalName" size="30"  id="terminalName" value="<%=orgTerminalName%>" onkeydown="getOriginalTerminalName(this.value)" />
                 	 	<dojo:autoComplete formId="searchFCLForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_SELL_RATES&from=0"/>
	    			</td>
 				</tr>
		    	<tr  class="style2">
		    	    <td align="right">Dest Port Name</td>
		    	    <td >
		    			<input name="destAirportname" size="30"  id="destAirportname" value="<%=destinationPortName%>" onkeydown="getDestinationPortName(this.value)" />
                    	<dojo:autoComplete formId="searchFCLForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_SELL_RATES&from=1"/>
		    	   </td>
		       </tr>
			   <tr  class="style2">
			       <td align="right">Com Description </td>
			       <td >
			    	 <input name="comDescription" size="30"  id="comDescription" value="<%=commodityCodeDesc%>" onkeydown="getComCodeName(this.value)"/>
    			  	 <dojo:autoComplete formId="searchFCLForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=FCL_SELL_RATES&from=2"/>
			      </td>
			  </tr>
    		 <tr  class="style2">
    			  <td align="right">SS Line Name</td>
    			  <td >
    				 <input name="sslinename" size="30" id="sslinename" value="<%=ssLineName%>" onkeydown="getSSlineName(this.value)" />
    			     <dojo:autoComplete formId="searchFCLForm" textboxId="sslinename" action="<%=path%>/actions/getCustomerName.jsp?tabName=FCL_SELL_RATES&from=0"/>
    			 </td>
    		</tr>
    	</table></td>
    	
  		<tr>
	  		<td colspan="5" align="center">
	  		        <input type="button" value="Search" class="buttonStyleNew" onclick="search()"  />
	  			    <input type="reset" value="Clear" class="buttonStyleNew" id="cancel" onclick=""/>
	  			    <input type="button" value="Add Remarks" class="buttonStyleNew"
	  			    onclick="openPage(GB_show('FCL Current','<%=path%>/fclSellRates.do?button='+'remarkspage&originalTerminal='+document.searchFCLForm.terminalNumber.value+'&destinationPort='+document.searchFCLForm.destSheduleNumber.value+'&ssline='+document.searchFCLForm.sslinenumber.value,200,600))"/>
			        <input type="button" value="Fcl Consolidator" class="buttonStyleNew" style="width:90px;"
	  			    onclick="openPage(GB_show('FCL Current','<%=path%>/fclConsolidator.do?originalTerminal='+document.searchFCLForm.terminalNumber.value+'&destinationPort='+document.searchFCLForm.destSheduleNumber.value,350,400))"/>
		    </td>
  		</tr>
  	</table>
  	
	<%}else{ %>
	
	<table width="100%">
		<tr>
		    <td colspan="5" align="right">
		          <input type="button" value="Search" class="buttonStyleNew" id="cancel" onclick="clearScreen()" />
		     </td>
		</tr>
		<tr>
		    <table width="100%" class="tableBorderNew">
			    <td colspan="5">
				<table width="100%" class="displaytagstyle">
				   <thead>
				   <tr>
						<td><b>
							<%=orgCap%>
						</td>
						<td><b>
							<%=destCap%>
						</td>
						<td><b>
							<%=comCap%>		
						</td>
						<td><b>
							<%=ssLineCap%>			
						</td>
				   </tr>
				   </thead>
				   <tr class="even">
						<td class="textlabels"><%=orgTerminalName%></td>
		  				<td class="textlabels"><%=destinationPortName%></td>
		   		   		<td class="textlabels"><%=commodityCodeDesc%></td>
		   		   		<td class="textlabels"><%=ssLineName%></td>
	   			   </tr>
   			   </table>
			</td>
			</table>
		</tr>
	</table><br>
	
	<br style="padding-top:10px;">
	<%if(displayList!=null && displayList.size()>0 )
	{%>
	<table width="100%" class="tableBorderNew">
  		<tr class="tableHeadingNew" height="90%"><b>Current FCLSell Rates</b></tr>	
		<tr><td>
		<table width="100%">
  			<tr>
        		<td align="left" >
          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;
          		%>
          	 	<display:table name="<%=displayList%>"  pagesize="<%=pageSize%>"  class="displaytagstyle" id="displayTable"  sort="list"> 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> FCL Sell Rates Displayed,For more Data click on Page Numbers.
    			</span>
  			  	</display:setProperty>
  				<display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
						1 {0} displayed. Page Number
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
				<display:setProperty name="paging.banner.item_name" value="FCL BUY Rates"/>
				<display:setProperty name="paging.banner.items_name" value="FCL BUY Rates"/>
  	<%
  	String org="";
  	String orgDesc="";
  	String dest="";
  	String destDesc="";
  	String comCode="";
  	String comCodeDesc="";
  	String ssline="";
  	String sslineName="";
  	String costCode="";
  	String costCodeDesc="";
  	String costType="";
  	String retail="";
  	FclBuyCost fclBuyCost=(FclBuyCost)displayList.get(i);
  	org=fclBuyCost.getOrgTerminalName().getUnLocationCode();
  	orgDesc=fclBuyCost.getOrgTerminalName().getUnLocationName();
  	dest=fclBuyCost.getDestinationPortName().getUnLocationCode();
  	destDesc=fclBuyCost.getDestinationPortName().getUnLocationName();
  	comCode=fclBuyCost.getCommodityCode().getCode();
  	comCodeDesc=fclBuyCost.getCommodityCodeDesc();
  	ssline=fclBuyCost.getSsLineName().getAccountno();
  	sslineName=fclBuyCost.getSsLineName().getAccountName();
  	costCode=fclBuyCost.getCostId().getCode();
  	costCodeDesc=fclBuyCost.getCostId().getCodedesc();
  	costType=fclBuyCost.getContType().getCode();
  	if(fclBuyCost.getRetail()!=null){
  	retail=df2.format(fclBuyCost.getRetail());
  	}
  	 %>		
	
		<display:column  title="Origin" >
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=orgDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=org%></span> 
		</display:column>
		<display:column title="Destination">
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=destDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=dest%></span> 
		</display:column>
		<display:column title="CommodityCode" >
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=comCodeDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=comCode%></span> 
		</display:column>
		<display:column title="SSLineName">
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=sslineName%></strong>',null,event);" onmouseout="tooltip.hide();"><%=ssline%></span> 
		</display:column>
		<display:column title="Charge Code">
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=costCodeDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=costCode+"/"+costType%></span> 
		</display:column>
		<display:column title="Retail"><%=retail%></display:column>
		<display:column title="Currency" property="currency"> </display:column>
		
		<%if(displayList.get(i)!=null)
		{
		FclBuyCost tempFclBuyCost=(FclBuyCost)displayList.get(i);
		for(int j=0;j<tempFclBuyCost.getUnitTypeList().size();j++)
		{
		FclBuyCostTypeRates fclBuyCostTypeRates=(FclBuyCostTypeRates)tempFclBuyCost.getUnitTypeList().get(j);
		 %>
		 <display:column  title="<%=fclBuyCostTypeRates.getUnitname()%>"><%=df2.format(fclBuyCostTypeRates.getUnitAmount())%></display:column>
		 <display:column  title="Currency"><%=fclBuyCostTypeRates.getCurrency().getCode()%></display:column>
		 
		 <%}
		 } %>
		<%i++; %>
 		</display:table>
	   	
	   	 </table>
   		</td>
  	 </tr>
	</table>
	
<%} %>
<table width="100%">
<tr class="style2">
  <td>A->Flat Rate Per Container Size</td>
  <td>B->Per Cubic Foot</td>
  <td>C->Per CBM</td>
  <td>D->per LBS</td>
  <td>E->Per 1000KG</td>
</tr>
<tr class="style2">
  <td>F->Per Dock Receipts</td>
  <td>G->Air Freight Costs</td>
  <td>H->Per BL Charges</td>
  <td>I->Flat Rate Per Container</td>
  <td>K->PER 2000 LBS</td>
</tr>
<tr class="style2">
<td>Per->Percent Of OFR</td>
</tr>
</table>
</td>
</tr>
</table>
<%} %>

<html:hidden property="buttonValue" styleId="buttonValue" />
<html:hidden property="index"/>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


