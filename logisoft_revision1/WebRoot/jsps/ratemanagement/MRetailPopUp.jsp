<%@ page language="java"%>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.*,
com.gp.cong.logisoft.domain.RetailStandardCharges,
com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat,
com.gp.cong.logisoft.domain.RetailWeightRangesRates,com.gp.cong.logisoft.domain.RetailStandardCharges1, com.gp.cong.logisoft.hibernate.dao.StandardChargesDAO"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List airStandardComm=new ArrayList();
List noncommon=new ArrayList();
String com="";
String noncom="";
String procced="";
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

if(session.getAttribute("rerecords")!=null){
procced=(String)session.getAttribute("rerecords");

}
if(session.getAttribute("trade")!=null)
{
session.removeAttribute("trade");
}
if(session.getAttribute("recommonList")!=null){

airStandardComm=(List)session.getAttribute("recommonList");

}
if(session.getAttribute("renoncommonList")!=null){
noncommon=(List)session.getAttribute("renoncommonList");

}
if(request.getAttribute("noncommon")!=null){
noncom=(String)request.getAttribute("noncommon");
}
if(request.getAttribute("common")!=null){
com=(String)request.getAttribute("common");
}
String buttonValue="load";
String msg="";
String message="";
String modify="";
List retailRatesList=null;

String terminalNumber = "";
String terminalName = "";
String destSheduleNumber = "";
String destAirportname = "";
String comCode = "";
String comDesc = "";
RetailStandardCharges retailRatesObj1= null;
DecimalFormat df = new DecimalFormat("0.00");

AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setMatch("match");
airRatesBean.setCommon("");
if(session.getAttribute("retmessage")!=null){
	message=(String)session.getAttribute("retmessage");
}

if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
}

airRatesBean.setCommon("");

if(session.getAttribute("reairRatesBean")!=null)
{
airRatesBean=(AirRatesBean)session.getAttribute("reairRatesBean");
}
airRatesBean.setMatch("match");

request.setAttribute("airRatesBean",airRatesBean);

if(session.getAttribute("retailmanage")!=null )
{
    retailRatesObj1=(RetailStandardCharges)session.getAttribute("retailmanage");
	if(retailRatesObj1.getOrgTerminal()!=null)
	{
		terminalNumber=retailRatesObj1.getOrgTerminal().getTrmnum();
		terminalName=retailRatesObj1.getOrgTerminal().getTerminalLocation();
	}
	if(retailRatesObj1.getDestPort() != null)
	{
		destSheduleNumber=retailRatesObj1.getDestPort().getShedulenumber();
		destAirportname=retailRatesObj1.getDestPort().getPortname();
	}
	if(retailRatesObj1.getComCode() != null)
	{
		comCode=retailRatesObj1.getComCode().getCode();
		comDesc=retailRatesObj1.getComCode().getCodedesc();
	}
}

if(request.getParameter("modify")!= null)
{

 modify=(String)request.getParameter("modify");
 session.setAttribute("modifyforretailRates",modify);
}
else
{
 	modify=(String)session.getAttribute("modifyforretailRates");
}


if(request.getAttribute("buttonValue")!=null)
{
		buttonValue=(String)request.getAttribute("buttonValue");
} 
if(request.getParameter("programid")!=null)
{
String programId=request.getParameter("programid");
  	session.setAttribute("processinfoforretailRates",programId);
  	}




String editPath=path+"/manageRetailRates.do";

//--------For BOTTOMLINE RATES------
if(request.getAttribute("btl")!=null){
%>

<script language="javascript" type="text/javascript">
	
	 mywindow=window.open("<%=path%>/jsps/ratemanagement/RetailBottomLine.jsp?flaterate=koko","","width=400,height=200");
 	 mywindow.moveTo(200,180);		
	
 </script>
<%} %>

<html> 
	<head>
		<title>JSP for ManageRetailRatesForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		 var newwindow = '';
           function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/ratemanagement/retailAddAirRatesPopup.jsp?airrates="+"add";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/ratemanagement/retailAddAirRatesPopup.jsp?retailsrates="+"add","","width=600,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           } 
	
	function searchform()
	{
	   
		if(document.manageRetailRates.terminalNumber.value=="" || document.manageRetailRates.destSheduleNumber.value=="")
		{
			alert("Please select Original Terminal and Destination Airport");
			return;
		}
		
		/*if((document.manageRetailRates.comCode.value=="" ||!document.manageRetailRates.comCode.value=="000000")  && document.manageRetailRates.common.checked==true){
		var result = confirm("Do want to search common commidty !! If not Please uncheck Common check box");
		 	
		 	 if(result)
				{
	   				document.manageRetailRates.buttonValue.value="search";
					document.manageRetailRates.search.value="get";
					document.manageRetailRates.submit();
	   			}	
			 else
			  {
			  
			  }
		}*/
		
		document.manageRetailRates.buttonValue.value="search";
		document.manageRetailRates.search.value="get";
		document.manageRetailRates.submit();
	}
	function searchallform()
	{	
		document.manageRetailRates.buttonValue.value="searchall";
		document.manageRetailRates.submit();
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
		document.manageRetailRates.buttonValue.value="search";
		document.manageRetailRates.submit();
		return false;
	}
	
	function openwindow()
	 {
	  
	   mywindow=window.open("<%=path%>/jsps/ratemanagement/retailAGSC.jsp?Std="+"add1","","width=600,height=200" );
	
	   mywindow.moveTo(200,180);
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
  		  var tables1 = document.getElementById('nonretailtable');
  		 if(tables1!=null)
  		 {
  	 	 //displaytagcolor();
   		 //initRowHighlightingcom();
   		 setWarehouseStylecom();
   		 }
  		 var tables = document.getElementById('retailtable');
  		 if(tables!=null)
  		 {
  	 	 //displaytagcolor();
   		 //initRowHighlighting();
   		 setWarehouseStyle();
   		 }
   	}
    function displaytagcolor()
	{
		 var datatableobj = document.getElementById('retailtable');
		
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
					if(tablerowobj.cells[1].innerHTML=="com")
					{
//					tablerowobj.bgColor='#FFB7DB';
					
					}else if(tablerowobj.cells[1].innerHTML=="acc")
					{
					//alert(tablerowobj.cells[1].innerHTML);
					tablerowobj.cells[1].innerHTML="*";
					tablerowobj.cells[1].style.color='#FF3300';
						tablerowobj.cells[1].style.fontSize=20;
					}
		 	 	}
		 	 	var datatableobj1 = document.getElementById('nonretailtable');
		
			 for(i=0; i<datatableobj1.rows.length; i++)
				{
					var tablerowobj = datatableobj1.rows[i];
					
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
					if(tablerowobj.cells[0].innerHTML=="com")
					{
//					tablerowobj.bgColor='#FFB7DB';
					
					}else if(tablerowobj.cells[0].innerHTML=="acc")
					{
					tablerowobj.bgColor='#FF3300';
					
					}
		 	 	}
		
	}
	function initRowHighlighting()
  	{
			if (!document.getElementById('retailtable')){ return; }
			 var tables = document.getElementById('retailtable');
			attachRowMouseEvents(tables.rows);
		
	}
	function initRowHighlightingcom()
  	{
			if (!document.getElementById('nonretailtable')){ return; }
			 var tables = document.getElementById('nonretailtable');
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
	function setWarehouseStyle()
	{

		if(document.manageRetailRates.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('retailtable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		}
		if(document.manageRetailRates.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('retailtable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('retailtable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('retailtable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
	 function setWarehouseStylecom()
	{

		if(document.manageRetailRates.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('nonretailtable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		}
		if(document.manageRetailRates.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('nonretailtable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('nonretailtable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('nonretailtable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
	 function titleLetter(ev)
	{
		if(event.keyCode==9){
		document.manageRetailRates.buttonValue.value="search";
		document.manageRetailRates.submit();
	window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestination(ev)
	{
		if(event.keyCode==9){
		document.manageRetailRates.buttonValue.value="search";
		document.manageRetailRates.submit();
	window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCode(ev)
	{
		if(event.keyCode==9){
		document.manageRetailRates.buttonValue.value="search";
		document.manageRetailRates.submit();
	window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterName(ev)
	{
		if(event.keyCode==9){
		document.manageRetailRates.buttonValue.value="search";
		document.manageRetailRates.submit();
	window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationName(ev)
	{
		if(event.keyCode==9){
		document.manageRetailRates.buttonValue.value="search";
		document.manageRetailRates.submit();
	window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodeName(ev)
	{
		if(event.keyCode==9){
		document.manageRetailRates.buttonValue.value="search";
		document.manageRetailRates.submit();
	window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
		 function ClearScreen()
	 {
	 document.manageRetailRates.buttonValue.value="clear";
	 document.manageRetailRates.submit();
	 
	 }
		</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/manageRetailRates" name="manageRetailRates" type="com.gp.cong.logisoft.struts.ratemangement.form.ManageRetailRatesForm" scope="request">
			<font color="blue" size="4"><%=message%></font>

			<table width=150% border="0" cellpadding="0" cellspacing="0">
			  	<tr class="textlabels">
  					<td align="left" class="headerbluelarge"></td>
			  	</tr>
  			     <tr>
    				<td height="12"  class="headerbluesmall">&nbsp;&nbsp;LCL Retail Rates </td> 
  				</tr>
			</table>
			
			<table width=150%  border="0" cellpadding="0" cellspacing="0">
  			<tr>&nbsp;</tr>
  			<tr>&nbsp;</tr>
  		     <tr>
  		         
  		         
  		        <td class="style2" align="right">Match Only</td>
				<td><html:radio property="match" value="match" name="airRatesBean"></html:radio></td>       
			    
			    <td class="style2" >Start list at</td>
				<td><html:radio property="match" value="starts" name="airRatesBean"></html:radio></td>
				
  		    </tr>
  		    <tr>&nbsp;</tr>
  			<tr>&nbsp;</tr>
  		    
  			<tr>
    			<td><span class="textlabels">Org Trm</span></td>
    			<td ><html:text property="terminalNumber"   size="2" onkeydown="titleLetter(this.value)"  maxlength="2" value="<%=terminalNumber%>" styleId="terminalNumber"/></td>
    			<td ><span class="textlabels">Org Trm Name</span></td>
    			<td ><html:text property="terminalName" value="<%=terminalName%>" onkeydown="titleLetterName(this.value)" /></td>
    			<td>&nbsp;&nbsp;</td>
    			<td><span class="textlabels">&nbsp;Dest Port&nbsp;</span></td>
	            <td><html:text property="destSheduleNumber" size="5" maxlength="5"  onkeydown="getDestination(this.value)"   value="<%=destSheduleNumber%>"/></td>
    			<td>&nbsp;&nbsp;</td>
    			<td><span class="textlabels">&nbsp;Dest PortName&nbsp;</span></td>
    			<td><html:text property="destAirportname" value="<%=destAirportname%>" onkeydown="getDestinationName(this.value)" /></td>
    			<td>&nbsp;&nbsp;</td>
    			<td><span class="textlabels">&nbsp;Com Code&nbsp;</span></td>
    			<td ><html:text property="comCode"  maxlength="6"  size="6" onkeydown="getComCode(this.value)"  value="<%=comCode%>"/></td>
    			<td>&nbsp;&nbsp;</td>
    			<td><span class="textlabels">&nbsp;Com Description </span></td>
    			<td ><html:text property="comDescription" value="<%=comDesc%>" onkeydown="getComCodeName(this.value)" /></td>
    			
    		</tr>	
    		<tr><td>&nbsp;</td></tr>
    		
  			<tr>
    			<td  colspan ="9" height="15"  class="headerbluesmall">&nbsp;&nbsp;<%=session.getAttribute("RetailRateCaption") %> </td>
  			</tr>
  	  </table>
  			<table width="150%">
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
          		<table width="150%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;
	
          		%>

          		<%if((noncom!=null && noncom.equals("noncommon"))||((airRatesBean.getCommon()==null||airRatesBean.getCommon().equals("")||airRatesBean.getCommon().equals("setcommon")) && !comCode.equals("000000")))
          		{
          		if(noncommon!=null&&noncommon.size()>0){
          		
          			retailRatesList=noncommon;
          			
          		}
          		 %>
          	 	<display:table name="<%=retailRatesList%>" pagesize="6" class="displaytagstyle" id="retailtable" sort="list" > 
			 	
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Retail Rates Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="Retail"/>
  				<display:setProperty name="paging.banner.items_name" value="Retails"/>
  				<%
  					String comCodeDisplay = "";
  					String comDescDisplay = "";
  					
  					String ofMinAmt = "";
  					String ratekgs="";
  					String rateCBM="";
  					String user;
  					String ratelb="";
  					String rateCFT="";
  					String englishMinAmt="";
  					String sizeAOF="";
  					String sizeATT="";
  					String sizeBOF="";
  					String sizeBTT="";
  					String sizeCOF="";
  					String sizeCTT="";
  					String sizeDOF="";
  					String sizeDTT="";
  					String measureType="";
  					
  					String exist="";
  					if(retailRatesList!=null && retailRatesList.size()>0)
  					{
  						RetailStandardCharges manageRetailRates=(RetailStandardCharges)retailRatesList.get(i); 			
  						if(manageRetailRates.getComCode()!=null)
  						{
  							comCodeDisplay=manageRetailRates.getComCode().getCode();
  							comDescDisplay=manageRetailRates.getComCode().getCodedesc();
  						}
  						Set set=new HashSet();
  					
  						if(manageRetailRates.getRetailCommoditySet()!=null){
  						 set=manageRetailRates.getRetailCommoditySet();
  					
  						if(set!=null && set.size()<1){
  					
  						
  						}else{
  						exist="acc";
  						}
  						}
  						
  						
  						if(manageRetailRates.getRetailStandardCharges()!=null){
  						Iterator iter=manageRetailRates.getRetailStandardCharges().iterator();
  						while(iter.hasNext())
  						{
  						//RetailWeightRangesRates retailRate=(RetailWeightRangesRates)iter.next();
  						}
  						  
  						}
  						if(manageRetailRates.getRetailWeightRangeSet()!=null)
  						{
  						
  						Iterator iter=manageRetailRates.getRetailWeightRangeSet().iterator();
  						while(iter.hasNext())
  						{
  						  RetailWeightRangesRates retailRate=(RetailWeightRangesRates)iter.next();
  							if(retailRate.getGeneralRate()!=null)
	  							{
	  							ratekgs=df.format(retailRate.getGeneralRate());
	  							}
	  							else{
	  							ratekgs=("0.00");
	  							}
	  							 
  							if(retailRate.getGeneralMinAmt()!=null)
	  							{
	  							rateCBM=df.format(retailRate.getGeneralMinAmt());
	  							}
	  							else{
	  							rateCBM=("0.00");
	  							}
  							if(retailRate.getExpressRate()!=null)
	  							{
	  							ofMinAmt=df.format(retailRate.getExpressRate());
	  							}
	  							else{
	  							ofMinAmt=("0.00");
	  							}
  							if(retailRate.getExpressMinAmt()!=null)
	  							{
	  							ratelb=df.format(retailRate.getExpressMinAmt());
	  							}
	  							else{
	  							ratelb=("0.00");
	  							}
  							if(retailRate.getDeferredRate()!=null)
	  							{
	  							rateCFT=df.format(retailRate.getDeferredRate());
	  							}
	  							else{
	  							rateCFT=("0.00");
	  							}
  							if(retailRate.getDeferredMinAmt()!=null)
	  							{
	  							englishMinAmt=df.format(retailRate.getDeferredMinAmt());
	  							}
	  							else{
	  							englishMinAmt=("0.00");
	  							}
  							if(retailRate.getFirstOcean()!=null)
	  							{
	  							sizeAOF=df.format(retailRate.getFirstOcean());
	  							}
	  							else{
	  							sizeAOF=("0.00");
	  							}
  							if(retailRate.getFirstTt()!=null)
	  							{
	  							sizeATT=df.format(retailRate.getFirstTt());
	  							}
	  							else{
	  							sizeATT=("0.00");
	  							}
  							if(retailRate.getSecondOcean()!=null)
	  							{
	  							sizeBOF=df.format(retailRate.getSecondOcean());
	  							}
	  							else{
	  							sizeBOF=("0.00");
	  							}
  							if(retailRate.getSecondTt()!=null)
	  							{
	  							sizeBTT=df.format(retailRate.getSecondTt());
	  							}
	  							else{
	  							sizeBTT=("0.00");
	  							}
  							if(retailRate.getThirdOcean()!=null)
	  							{
	  							sizeCOF=df.format(retailRate.getThirdOcean());
	  							}
	  							else{
	  							sizeCTT=("0.00");
	  							}
  							if(retailRate.getThirdTt()!=null)
	  							{
	  							sizeCTT=df.format(retailRate.getThirdTt());
	  							}
	  							else{
	  							sizeCTT=("0.00");
	  							}
  							if(retailRate.getFourthOcean()!=null)
	  							{
	  							sizeDOF=df.format(retailRate.getFourthOcean());
	  							}
	  							else{
	  							sizeDOF=("0.00");
	  							}
  							if(retailRate.getFourthTt()!=null)
	  							{
	  							sizeDTT=df.format(retailRate.getFourthTt());
	  							}
	  							else{
	  							sizeDTT=("0.00");
	  							}
  						
	  							if(retailRate.getMeasureType()!=null)
	  							{
	  							measureType=retailRate.getMeasureType();
	  							}
	  							
  						 }
  						}
  					}
  					
  					
  		 		%>
  		 		
  		 		<display:column title=""  href="<%=editPath%>" paramId="param" paramProperty="id"><%="More Info"%></display:column>
  		 		<display:column ><%=exist%></display:column> 
  		 		<display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Comm#" sortable="true" href="<%=editPath%>"  paramId="paramid" sortable="true" paramProperty="id">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=comCodeDisplay%></display:column>
  		 	<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Code Desc" >&nbsp;&nbsp;&nbsp;&nbsp;<%=comDescDisplay%></display:column>	
  		 		<%if(airRatesBean.getMatch()!=null && airRatesBean.getMatch().equals("match"))
  		 		{ %>
				
  		 		<%/*if(measureType.equals("E"))
  		 		{ */%>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Rate/CFT">&nbsp;&nbsp;&nbsp;&nbsp;<%=rateCFT%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Rate/100LB" >&nbsp;&nbsp;&nbsp;&nbsp;<%=ratelb%></display:column>
				
  		 		
				<%/*}else if(measureType.equals("M"))
				{ */%>
				<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Rate/CBM">&nbsp;&nbsp;&nbsp;&nbsp;<%=rateCBM%></display:column>
				<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Rate/1000kGS">&nbsp;&nbsp;&nbsp;&nbsp;<%=ratekgs%></display:column>
				

				<%//} %>
				<%}%>
				 <display:column href="<%=editPath%>" paramId="rates" paramProperty="id"><%="BLR"%></display:column>
			 	<%i++; %>
		 </display:table>
		 <%}%>
		 </table></div></td></tr></table>
		 
		 <table width="150%">
		 <tr>
    			<td  colspan ="9" height="15"  class="headerbluesmall">&nbsp;&nbsp;<%=session.getAttribute("reatilcommonList") %> </td>
  			</tr>
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
          		<table width="150%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int k=0;
          		%>
		 
		 <%		
		 //if(airRatesBean.getCommon()!=null && comCode!=null && (airRatesBean.getNoncommon()!=null && airRatesBean.getNoncommon().equals("noncommon")) || comCode.equals("000000")||(com!=null && com.equals("common"))||(procced!=null && !procced.equals("")))
		// { 
		
		   %>
		 <display:table name="<%=airStandardComm%>" pagesize="6" class="displaytagstyle" id="nonretailtable" sort="list" > 
			 	
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Retail Rates Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="Retail"/>
  				<display:setProperty name="paging.banner.items_name" value="Retails"/>
  				<%
  					String comCodeDisplay = "";
  					String comDescDisplay = "";
  					
  					String chargeCode = "";
  					String chargeType="";
  					String standard="";
  					String amtPerCft="";
  					String amtlbs="";
  					String amtPerCbm="";
  					String amtPerKg="";
  					String amount="";
  					String percentage="";
  					String minAmt="";
  					String insuranceRate="";
  					String insuranceAmt="";
  					String asFrfgted="";
  					String effdate="";
  					String uname="";
  					if(airStandardComm!=null && airStandardComm.size()>0)
  					{
  					    //.....to retrive only standard records........
  					   
  						RetailStandardCharges1 retailRate=(RetailStandardCharges1)airStandardComm.get(k);//child record		
  						
  						RetailStandardCharges parentObject=new RetailStandardCharges();//parent record
  						StandardChargesDAO StandardCharges=new StandardChargesDAO();
  						parentObject=StandardCharges.findById1(retailRate.getStandardId());
  						
  						if(parentObject.getComCode()!=null)
  						{
  							comCodeDisplay=parentObject.getComCode().getCode();
  							comDescDisplay=parentObject.getComCode().getCodedesc();
  						}
  						
  						
  						   
  						if(retailRate.getStandard()!=null && retailRate.getStandard().equals("Y"))
  						  {
  						    if(retailRate.getChargeCode()!=null)
  							{
  							chargeCode=retailRate.getChargeCode().getCode();
  							}
  							if(retailRate. getEffectiveDate()!=null){
  							effdate=dateFormat.format(retailRate.getEffectiveDate());
  							}
  							if(retailRate. getWhoChanged()!=null){
  							uname=retailRate. getWhoChanged();
  							}
  							if(retailRate.getChargeType()!=null)
  							{
  							chargeType=retailRate.getChargeType().getCodedesc();
  							}
  							if(retailRate.getStandard()!=null)
  							{
  							standard=retailRate.getStandard();
  							}
  							if(retailRate.getAmtPerCft()!=null)
  							{
  							amtPerCft=df.format(retailRate.getAmtPerCft());
  							}
  							if(retailRate.getAmtPer100lbs()!=null)
  							{
  							amtlbs=df.format(retailRate.getAmtPer100lbs());
  							}
  							if(retailRate.getAmtPerCbm()!=null)
  							{
  							amtPerCbm=df.format(retailRate.getAmtPerCbm());
  							}
  							if(retailRate.getAmtPer1000kg()!=null)
  							{
  							amtPerKg=df.format(retailRate.getAmtPer1000kg());
  							}
  							if(retailRate.getAmount()!=null)
  							{
  							amount=df.format(retailRate.getAmount());
  							}
  							if(retailRate.getPercentage()!=null)
  							{
  							percentage=retailRate.getPercentage().toString();
  							}
  							if(retailRate.getMinAmt()!=null)
  							{
  							minAmt=df.format(retailRate.getMinAmt());
  							}
  							if(retailRate.getInsuranceRate()!=null)
  							{
  							insuranceRate=df.format(retailRate.getInsuranceRate());
  							}
  							if(retailRate.getInsuranceAmt()!=null)
  							{
  							insuranceAmt=df.format(retailRate.getInsuranceAmt());
  							}
  							if(retailRate.getAsFrfgted()!=null)
  							{
  							asFrfgted=retailRate.getAsFrfgted();
  							}
  						  }	
  						
  						
  					
  					}
  					
  					
  		 		%>
  		 		<display:column title="Show All"  href="<%=editPath%>" paramId="param" paramProperty="standardId"><%="More Info"%></display:column>
  		 	<display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Comm#" sortable="true" href="<%=editPath%>"  paramId="paramid" sortable="true" paramProperty="standardId">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=comCodeDisplay%></display:column>
					<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Code Desc" >&nbsp;&nbsp;&nbsp;&nbsp;<%=comDescDisplay%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;Effective Date" >&nbsp;&nbsp;&nbsp;&nbsp;<%=effdate%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;User Name" >&nbsp;&nbsp;&nbsp;&nbsp;<%=uname%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Chrg Code" >&nbsp;&nbsp;&nbsp;&nbsp;<%=chargeCode%></display:column>
				<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Chrg Type">&nbsp;&nbsp;&nbsp;&nbsp;<%=chargeType%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Std">&nbsp;&nbsp;&nbsp;&nbsp;<%=standard%></display:column>
				
				<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;As Freighted"><%=asFrfgted%></display:column>
				<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Insurance Rate">&nbsp;&nbsp;&nbsp;&nbsp;<%=insuranceRate%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Insurance Amt">&nbsp;&nbsp;&nbsp;&nbsp;<%=insuranceAmt%></display:column>
				
				<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;Amt/Cft">&nbsp;&nbsp;&nbsp;<%=amtPerCft%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Amt/100lbs">&nbsp;&nbsp;&nbsp;&nbsp;<%=amtlbs%></display:column>
				<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;Amt/Cbm">&nbsp;&nbsp;&nbsp;<%=amtPerCbm%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Amt/1000kg">&nbsp;&nbsp;&nbsp;&nbsp;<%=amtPerKg%></display:column>		 	
			 	<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;Perc">&nbsp;&nbsp;&nbsp;<%=percentage%></display:column>
			 	<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;Amt">&nbsp;&nbsp;&nbsp;<%=amount%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;MinAmt" >&nbsp;&nbsp;&nbsp;&nbsp;<%=minAmt%></display:column>
			 	<display:column paramId="param" ><img src="<%=path%>/img/add.gif" border="0" onclick="openwindow()" paramProperty="standardId"></display:column>
			 	  
			 	<%k++; %>
		 </display:table>
		 <%//} %>
   	 </table>
   </div>
  </td>
 </tr>	
		</table>

<html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
<html:hidden property="search" />			
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

