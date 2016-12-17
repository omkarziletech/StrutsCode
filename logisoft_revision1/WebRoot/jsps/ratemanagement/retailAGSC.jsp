<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.*,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.RetailStandardCharges1,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
//Date date=new Date();
java.util.Date date = new java.util.Date();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("0.000");
String chargecode="";
String codeDesc="";
String msg="";
List typeList=new ArrayList();
List agssList=null;
String chargeType="";
RetailStandardCharges1 retailStandardCharges=new RetailStandardCharges1();
String effdate="";
SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
effdate=sdf.format(date);

AirRatesBean airRatesBean=new AirRatesBean();
String effectiveDate="";
String minAmt="";
String amount="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String cft="";
String lbs="";
String cbm="";
String amtkg="";
String popup="";
RetailStandardCharges1 retailStd=new RetailStandardCharges1();
retailStd.setStandard("off");
retailStd.setAsFrfgted("off");
if(request.getParameter("Std")!=null)
 {
    popup=(String)request.getParameter("Std");
    session.setAttribute("url",popup);//-----for request url object----------
 }

 if(session.getAttribute("url")!=null)
 {
    popup=(String)session.getAttribute("url");

 }

if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
retailStd.setStandard(airRatesBean.getStandard());
retailStd.setAsFrfgted(airRatesBean.getAsFrfgted());
}
request.setAttribute("airStd",retailStd);
if(session.getAttribute("retailStandardCharges")!=null )
{

    retailStandardCharges=(RetailStandardCharges1)session.getAttribute("retailStandardCharges");
	if(retailStandardCharges.getChargeCode()!=null)
	{

		chargecode=retailStandardCharges.getChargeCode().getCode();
		codeDesc=retailStandardCharges.getChargeCode().getCodedesc();
	}
	if(retailStandardCharges.getChargeType()!=null)
	{
	chargeType=retailStandardCharges.getChargeType().getId().toString();
	}
	if(retailStandardCharges.getMinAmt()!=null)
	{
		minAmt=df.format(retailStandardCharges.getMinAmt());
	}
	if(retailStandardCharges.getEffectiveDate()!=null)
	{
		effectiveDate=retailStandardCharges.getEffectiveDate().toString();
	}
	if(retailStandardCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(retailStandardCharges.getInsuranceRate());
	}
	if(retailStandardCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(retailStandardCharges.getInsuranceAmt());
	}
	if(retailStandardCharges.getPercentage()!=null)
	{
	percentage=per.format(retailStandardCharges.getPercentage());
	}
	if(retailStandardCharges.getAmount()!=null)
	{
	amount=df.format(retailStandardCharges.getAmount());
	}
	if(retailStandardCharges.getAmtPerCft()!=null)
	{
	cft=df.format(retailStandardCharges.getAmtPerCft());
	}
	if(retailStandardCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(retailStandardCharges.getAmtPer100lbs());
	}
	if(retailStandardCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(retailStandardCharges.getAmtPerCbm());
	}
	if(retailStandardCharges.getAmtPer1000kg()!=null)
	{
	amtkg=df.format(retailStandardCharges.getAmtPer1000kg());
	}
	if(retailStandardCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(retailStandardCharges.getEffectiveDate());
	}
}
if(session.getAttribute("retailagssAdd") != null)
{
	agssList = (List) session.getAttribute("retailagssAdd");
}

if(typeList != null)

{
	typeList=dbUtil.getTypeListForRetail(new Integer(34),"yes","Select Rate type");
	request.setAttribute("typeList",typeList);
	
}

request.setAttribute("typeList",typeList);

String modify="";
modify = (String) session.getAttribute("modifyforretailRates");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/retailAGSC.do";
String defaultRate="";
if(session.getAttribute("retaildefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("retaildefaultRate");
}
if(request.getAttribute("popupclose")!=null)//-----TO CLOSE THE POPUP WINDOW-----
{
  
%>
<script type="text/javascript">
self.close();

opener.location.href="<%=path%>/jsps/ratemanagement/manageRetailRates.jsp";

</script>
 <%} 
 
 
 
 if(request.getAttribute("exist") != null)
  {
     msg = request.getAttribute("exist").toString();
     chargecode = "";
     codeDesc = "";
   
  }%>

<font color="red" size="3"><%= msg %></font>
<html> 
	<head>
		<title>JSP for RetailAGSCForm form</title>
				<%@include file="../includes/baseResources.jsp" %>
				<%@include file="../includes/resources.jsp"%>

		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		   dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
		<script language="javascript" type="text/javascript">
		function addAGSS(val1)
		{
		if(document.retailAGSCForm.charge.value!=null && document.retailAGSCForm.charge.value=="")
		{
		
		alert("Please select Charge Code");
		return;
		}
		if(document.retailAGSCForm.desc.value=="")
		{
		alert("Please select Charge Description");
		return;
		}
		if(document.retailAGSCForm.chargeType.value!=null && document.retailAGSCForm.chargeType.value=="0")
		{
		alert("Please select Charge Type");
		return;
		}
			if(val1 !=""){
			  
			document.retailAGSCForm.buttonValue.value="add12";
			}
		    else{
			 
			document.retailAGSCForm.buttonValue.value="add";
			}
  			document.retailAGSCForm.submit();
		}
		function addfield()
		{
		    document.retailAGSCForm.buttonValue.value="coloumnadd";
			document.retailAGSCForm.submit();
		}
   		function submit()
		{
		    document.retailAGSCForm.buttonValue.value="";
			document.retailAGSCForm.submit();
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
			document.retailAGSCForm.submit();
			return false;
		}
		
		function disabled(val1)
      {
   
	if(val1 == 0 || val1== 3)
	{
       alert("HIDE");
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		
   		    imgs[k].style.visibility = 'hidden';
   		 
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			input[i].disabled=true;
	  			
	  		
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
   
   
    function disable(val)
   		{
   		if(val.checked == true){
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			if(input[i].name!="exclude"&& input[i].name!="charge"&& input[i].name!="desc")
   			{
   		    	input[i].value="";
   		    	input[i].readOnly=true;
   		    	input[i].style.backgroundColor="blue";
   		    	
   		    }
	  	}
  	 	var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].value="";
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
   		}else {
   		
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			if(input[i].name!="exclude"&& input[i].name!="charge"&& input[i].name!="desc")
   			{	
   		    	input[i].readOnly=false;
   		    	input[i].style.backgroundColor="white";
   		    }
	  	}
  	 	var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=false;
			select[i].style.backgroundColor="white";
	  		
  	 	}
   		}
   		}
		function getCodeDesc(ev)
		{
		if(event.keyCode==9)
		{
		 document.retailAGSCForm.buttonValue.value="chargeCode";
		 document.retailAGSCForm.submit();
		}
		}
	  function getCodeDescPress(ev)
		{
		if(event.keyCode==13)
		{
		 document.retailAGSCForm.buttonValue.value="chargeCode";
		 document.retailAGSCForm.submit();
		}
		}
		function deleteCOMM(value1){
		alert(value1);
  		 document.retailAGSCForm.retailAGSCId.value = value1;
		 document.retailAGSCForm.buttonValue.value="delete";
		 document.retailAGSCForm.submit();
		}
		
		    function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.retailAGSCForm.charge.value;
			params['codeType'] ='2' ;
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
   			document.getElementById("desc").value=data.commodityDesc;
   			}
	}
	
	    function getdestPort1(ev){ 
    document.getElementById("charge").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['codeDesc'] = document.retailAGSCForm.desc.value;
			params['codeType'] ='2' ;
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
   			document.getElementById("charge").value=data.commodityCode;
   			}
	}
	</script>
	</head>
	<%
	
	if(modify!=null && modify.equals("3"))
	{
	
	 %>
	
		<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
	<%}
	
	else{ %>
		<body class="whitebackgrnd">
	<%} %>
	
	<html:form action="/retailAGSC" name="retailAGSCForm" type="com.gp.cong.logisoft.struts.ratemangement.form.RetailAGSCForm" scope="request">
<table width="100%" border="0" >
	<%if(session.getAttribute("listofitem")==null){%>
		<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew"><td>Accessorial & Gen Std Chrgs</td>
			<td align="right">
		      <input type="button" value="Add" onclick="addfield()" class="buttonStyleNew" >
			</td>
		</tr>
		
   <%}else{%>
			
 		<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew">Accessorial & Gen Std Chrgs</tr>
		<tr class="textlabels">
			<td width="12%">Chrg Code</td>
			<td>Chrg Desc </td>
			<td>Exclude </td>
			<td>Type </td>
			<td>Std</td>
			<% 
            if(chargeType.equals("11287"))
        	{
        	%>
        		<td align="center">Amt</td>
            <%
         	}
         	if(chargeType.equals("11289"))
         	{
         	%>
         	<%if(defaultRate.equals("E"))
         	{ %>
	        	<td>$/cft</td>
				<td>$/100lbs</td>
			<%}
			else if(defaultRate.equals("M"))
			{ %>
				<td>$/cbm</td>
				<td>$/1000kg</td>
			<%} %>
        		<td>MinAmt</td>
            <%
         	}
         	if(chargeType.equals("11288"))
         	{
         	%>
         		<td>Perc</td>
         		<td>MinAmt</td>
         	<%
         	}
         	if(chargeType.equals("11291"))
         	{
         	%>
         		<td align="center">MinAmt</td>
         	<%
         	}
         	if(chargeType.equals("11290"))
		 	{
         	%>
         		<td>As Freighted</td>
         		<td>Insurance Rate</td>
         		<td>Insurance Amt</td>
         		<td>MinAmt</td>
         	<%
         	}
         	%>
				<td>Eff.Date</td>
		 </tr>
		 <tr class="textlabels">
 		 <td ><input name="charge" id="charge" value="<%=chargecode%>"  onkeydown="getdestPort(this.value)" styleClass="varysizeareahighlightgrey" size="4"/>
   		 <dojo:autoComplete  formId="retailAGSCForm" textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=RETAIL_AGSC&from=0"/></td>
   		 <td >
<%--   		 <html:text property="desc" value="<%=codeDesc%>"  size="12"/>--%>
   		 <input name="desc" id="desc" value="<%=codeDesc%>"  onkeydown="getdestPort1(this.value)" styleClass="varysizeareahighlightgrey" size="4"/>
   		 <dojo:autoComplete  formId="retailAGSCForm" textboxId="desc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=RETAIL_AGSC"/>
   		 </td>
  		  <td><html:checkbox property="exclude" name="airStd" onclick="disable(this)"></html:checkbox></td>	 
  		 <td ><html:select property="chargeType" styleClass="verysmalldropdownStyle" value="<%=chargeType%>" onchange="submit()">
      								   <html:optionsCollection name="typeList"/>          
                </html:select></td>
         <td><html:checkbox property="standard" name="airStd"></html:checkbox></td>
 <%  if(chargeType.equals("11287")) { %>
         <td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="amount" value="<%=amount%>"/></td>
         <% 
         }
         if(chargeType.equals("11289"))
         {
         %>
         <%if(defaultRate.equals("E"))
         { %>
         <td><html:text property="amtPerCft" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="amtPerCft" value="<%=cft%>"/></td>
  		 <td><html:text property="amtPer100lbs" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="amtPer100lbs" value="<%=lbs%>"/></td>
  		<%}
  		else if(defaultRate.equals("M"))
  		{ %>
  		 <td><html:text property="amtPerCbm" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="amtPerCbm" value="<%=cbm%>"/></td>
  		 <td><html:text property="amtPer1000kg" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="amtPer1000kg" value="<%=amtkg%>"/></td>	
  		 <%} %>
  		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="minAmt" value="<%=minAmt%>"/></td>
         <%
         }
         if(chargeType.equals("11288"))
         {
         %>
         <td><html:text property="percentage" maxlength="3" size="3" onblur="checkdec(this)" styleId="percentage"  />%</td>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="minAmt" value="<%=minAmt%>"/></td>
		 
		 <%
		 }
		 if(chargeType.equals("11291"))
		 {
		 %>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="minAmt" value="<%=minAmt%>"/>%</td>
		 <%
		 }
		 if(chargeType.equals("11290"))
		 {
		 %>
		  <td><html:checkbox property="asFrfgted" name="airStd"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6" styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  		<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" size="7" readonly="false" value="<%=effdate%>"/>
  		<img src="<%=path%>/img/CalendarIco.gif" alt="cal"  id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
  		<td align="right">
			  <input type="button" value="Add To List" onclick="addAGSS('<%=popup%>')" class="buttonStyleNew" style="width:90px" >
			
			</td>
      </tr> 
    
	<%} %>
	</table>
	
	<tr><td colspan="2">
	<%
	if(session.getAttribute("url")==null)
	{ %>
	<table width="100%" border="0">
	<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        %>
        <display:table name="<%=agssList%>" pagesize="<%=pageSize%>"   class="displaytagstyle"  sort="list" id="agsstable" > 
			<%
				String type="";
				String chargeCode="";
				String tempPath="";
				String effectDate="";
				String perc="";
			    if(agssList != null && agssList.size()>0)
			    {
					RetailStandardCharges1 retailStandardChargesObj2=(RetailStandardCharges1)agssList.get(i);
					if(retailStandardChargesObj2.getChargeType()!=null)
					{
						type=retailStandardChargesObj2.getChargeType().getCodedesc();
					}
					if(retailStandardChargesObj2.getChargeCode()!=null)
					{
						chargeCode=retailStandardChargesObj2.getChargeCode().getCode();
					}
					if(retailStandardChargesObj2.getEffectiveDate()!=null)
					{
					effectDate=dateFormat.format(retailStandardChargesObj2.getEffectiveDate());
					}
					if(retailStandardChargesObj2.getPercentage()!=null)
					{
					perc=per.format(retailStandardChargesObj2.getPercentage());
					}
					
						if(retailStandardChargesObj2.getAmtPer1000kg()!=null)
						{
					  			amtkg=df.format(retailStandardChargesObj2.getAmtPer1000kg());
						}
				if(retailStandardChargesObj2.getInsuranceRate()!=null)
				{
				insuranceRate=df.format(retailStandardChargesObj2.getInsuranceRate());
				
				}	
				
				if(retailStandardChargesObj2.getInsuranceAmt()!=null)
				{
				insuranceAmt=df.format(retailStandardChargesObj2.getInsuranceAmt());
				
				}	
				
				if(retailStandardChargesObj2.getAmount()!=null)
				{
				amount=df.format(retailStandardChargesObj2.getAmount());
				
				}	
				
				if(retailStandardChargesObj2.getMinAmt()!=null)
				{
				minAmt=df.format(retailStandardChargesObj2.getMinAmt());
				
				}	
				if(retailStandardChargesObj2.getAmtPerCft()!=null)
				{
				cft=df.format(retailStandardChargesObj2.getAmtPerCft());
				
				}	
				if(retailStandardChargesObj2.getAmtPer100lbs()!=null)
				{
				lbs=df.format(retailStandardChargesObj2.getAmtPer100lbs());
				
				}	
				if(retailStandardChargesObj2.getAmtPerCbm()!=null)
				{
				cbm=df.format(retailStandardChargesObj2.getAmtPerCbm());
				
				}	
				}
				String iStr=String.valueOf(i);
  					tempPath=editPath+"?ind="+iStr;
			 %>
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
  			
  			<display:column  title="Chrg_Code"><a href="<%=tempPath%>"><%=chargeCode%></a> </display:column>
			<display:column  title="&nbsp;Chrg Type" ><%=type %></display:column>
			<display:column property="standard" title="STD"></display:column>
			<display:column property="exclude" title="EXC"></display:column>
			<display:column property="asFrfgted" title="&nbsp;&nbsp;As_Fr" ></display:column>
			
			<display:column  title="&nbsp;&nbsp;Insu_Rate">&nbsp;&nbsp;<%=insuranceRate %></display:column>
			<display:column  title="&nbsp;&nbsp;Insu_Amt">&nbsp;&nbsp;<%=insuranceAmt%></display:column>
			<%if(defaultRate.equals("E"))
			{ %>
			<display:column  title="&nbsp;&nbsp;Amt/Cft" >&nbsp;&nbsp;<%=cft%></display:column>
			<display:column  title="&nbsp;&nbsp;Amt/100lbs" >&nbsp;&nbsp;<%=lbs%></display:column>
			<%}else if(defaultRate.equals("M"))
			{ %>
			<display:column  title="&nbsp;&nbsp;Amt/Cbm" >&nbsp;&nbsp;<%=cbm%></display:column>
			<display:column  title="&nbsp;&nbsp;Amt/1000kg" >&nbsp;&nbsp;<%=amtkg %></display:column>
		<%} %>
			<display:column title="&nbsp;&nbsp;Perc" >&nbsp;&nbsp;<%=perc%></display:column>
		
			<display:column title="&nbsp;&nbsp;MinAmt" >&nbsp;&nbsp;<%=minAmt %></display:column>
			
			<display:column title="&nbsp;&nbsp;Amt">&nbsp;&nbsp;<%=amount %></display:column>
			
			<display:column title="&nbsp;&nbsp;Effe Date" >&nbsp;&nbsp;<%=effectDate%></display:column>
			<display:column>
			<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
			<img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteCOMM('<%=chargeCode%>')" />
			</span>
			</display:column>
  			<% i++;%>
		</display:table>
        </table></div>  
    	</td> 
   </tr>  
</table>
<%} %>
</td>
</tr>
</table>
</table>	
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<html:hidden property="index" />
<html:hidden property="retailAGSCId" />

		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

