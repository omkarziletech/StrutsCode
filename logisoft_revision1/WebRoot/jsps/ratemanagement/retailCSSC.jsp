
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.text.DateFormat,java.text.SimpleDateFormat,java.util.*,com.gp.cong.logisoft.domain.RetailCommodityCharges,com.gp.cong.logisoft.domain.RetailWeightRangesRates,com.gp.cong.logisoft.domain.RetailStandardCharges1,com.gp.cong.logisoft.beans.AirRatesBean"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

List csssList = null;
List applyGeneralStandardList=new ArrayList();
String chargecode="";
String msg="";
String codeDesc="";
DBUtil dbUtil=new DBUtil();
String chargeType="0";
String rateType="";
String minAmt="";
String typ="";


AirRatesBean retailRatesBean=new AirRatesBean();
RetailCommodityCharges retailComm=new RetailCommodityCharges();
retailComm.setStandard("N");
retailComm.setExclude("N");
retailComm.setAsFreightedCheckBox("off");
if(request.getAttribute("airRatesBean")!=null)
{
retailRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
retailComm.setStandard(retailRatesBean.getStandard());
retailComm.setExclude(retailRatesBean.getExclude());
retailComm.setAsFreightedCheckBox(retailRatesBean.getAsFrfgted());
}
request.setAttribute("retailComm",retailComm);
List typeList=new ArrayList();
RetailCommodityCharges retailCommodityCharges=new RetailCommodityCharges();
retailCommodityCharges.setStandard("N");
retailCommodityCharges.setExclude("N");
String amount="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String cft="";
String lbs="";
String cbm="";
String amtkg="";
String effectiveDate="";
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("0.000");
String defaultRate="";
java.util.Date date = new java.util.Date();
effectiveDate=dateFormat.format(date);
if(session.getAttribute("retaildefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("retaildefaultRate");
}
if(session.getAttribute("retailCommodityCharges")!=null )
{
    retailCommodityCharges=(RetailCommodityCharges)session.getAttribute("retailCommodityCharges");
	if(retailCommodityCharges.getChargeCode()!=null)
	{
		
		chargecode=retailCommodityCharges.getChargeCode().getCode();
		codeDesc=retailCommodityCharges.getChargeCode().getCodedesc();
	}
	
	if(retailCommodityCharges.getChargeType()!=null && retailCommodityCharges.getChargeType().getId()!=null)
	{
		chargeType=retailCommodityCharges.getChargeType().getId().toString();

	}
	if(retailCommodityCharges.getMinAmt()!=null)
	{
	minAmt=df.format(retailCommodityCharges.getMinAmt()).toString();

	}
	if(retailCommodityCharges.getAmount()!=null)
	{
	amount=df.format(retailCommodityCharges.getAmount()).toString();

	}
	if(retailCommodityCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(retailCommodityCharges.getInsuranceRate());

	}
	if(retailCommodityCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(retailCommodityCharges.getInsuranceAmt());

	}
	if(retailCommodityCharges.getPercentage()!=null)
	{
	percentage=per.format(retailCommodityCharges.getPercentage());

	}
	if(retailCommodityCharges.getAmtPerCft()!=null)
	{
	cft=retailCommodityCharges.getAmtPerCft().toString();

	}
	if(retailCommodityCharges.getAmtPerCbm()!=null)
	{
	cbm=retailCommodityCharges.getAmtPerCbm().toString();

	}
	if(retailCommodityCharges.getAmtPer100lbs()!=null)
	{
	lbs=retailCommodityCharges.getAmtPer100lbs().toString();

	}
	if(retailCommodityCharges.getAmtPer1000kg()!=null)
	{
	amtkg=retailCommodityCharges.getAmtPer1000kg().toString();

	}
	if(retailCommodityCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(retailCommodityCharges.getEffectiveDate());

	}
}
if(session.getAttribute("retailcssList") != null)
{
	csssList = (List) session.getAttribute("retailcssList");
}


if(typeList != null)
{
	typeList=dbUtil.getTypeListForCssc(new Integer(34),"yes","Select Rate type");
	request.setAttribute("typeList",typeList);
}

request.setAttribute("typeList",typeList);
if(session.getAttribute("retailapplyGeneralStandardList")!=null)
{
	applyGeneralStandardList=(List)session.getAttribute("retailapplyGeneralStandardList");
}	

String modify="";
modify = (String) session.getAttribute("modifyforretailRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
RetailWeightRangesRates retailWeightRangeRatesObj=new RetailWeightRangesRates();

if(session.getAttribute("retailweightrangesrate")!=null)
		{
			
			retailWeightRangeRatesObj=(RetailWeightRangesRates)session.getAttribute("retailweightrangesrate");
		}
		
		typ=retailWeightRangeRatesObj.getType();

		 	

String editPath=path+"/retailCSSC.do";

if(request.getAttribute("exist") != null)
  {
     msg = request.getAttribute("exist").toString();
     chargecode = "";
     codeDesc = "";
   
  }
 
%>
<font color="red" size="3"><%= msg %></font>
 
<html> 
	<head>
	<base href="<%=basePath%>">
		<title>JSP for RetailCSSCForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp"%>
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		   dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
		<script language="javascript" type="text/javascript">
	function searchform(){
			document.retailCSSCForm.buttonValue.value="search";
			document.retailCSSCForm.submit();
	}
	function deleteCOMM(val1){
	
		document.retailCSSCForm.buttonValue.value="delete";
		document.retailCSSCForm.retailCSSId.value=val1;
		document.retailCSSCForm.submit();
	}
	function addAGSS(val){
			if(document.retailCSSCForm.charge.value==""){
				alert("Please select Charge Code");
				return;
			}
			if(document.retailCSSCForm.desc.value==""){
				alert("Please select Charge Descritpion");
				return;
			}
			if(document.retailCSSCForm.exclude.checked==false && document.retailCSSCForm.chargeType.value=="0")
			{
				alert("Please select Exclude  or Charge Type");
				return;
			}
			document.retailCSSCForm.buttonValue.value="add";
  			document.retailCSSCForm.submit();
	}
   function confirmdelete(obj){
			var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('agsstable').rows[rowindex].cells;	
	   		document.retailCSSCForm.index.value=rowindex-1;
			document.retailCSSCForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this value");
			if(result)
			{
   				document.retailCSSCForm.submit();
   			}	
   	}
   function popup1(mylink, windowname){
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
			mywindow.moveTo(200,180);
			document.retailCSSCForm.submit();
			return false;
	}	
  function submit(){
		document.retailCSSCForm.buttonValue.value="";
			document.retailCSSCForm.submit();
   }
  function disable(val){
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
   function disabled(val1){
		if(val1!="" && (val1 == 0 || val1== 3))
		{
       document.getElementById("add").style.visibility = 'hidden';
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
  	 	var radio = document.getElementById("radio");
  	 	alert(radio.length);
  	 	 
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }	
  }
  function addfield(){
		    document.retailCSSCForm.buttonValue.value="coloumnadd";
			document.retailCSSCForm.submit();
   }
  function getCodeDesc(){
		if(event.keyCode==9){
		    document.retailCSSCForm.desc.value="";
		    document.retailCSSCForm.buttonValue.value="chargeCode";
			document.retailCSSCForm.submit();
		}
    }
   function getCodeDescPress(){
		if(event.keyCode==13)
		{
			document.retailCSSCForm.desc.value="";
		 document.retailCSSCForm.buttonValue.value="chargeCode";
		 document.retailCSSCForm.submit();
		}
    }
  function getChgCodeDesc(){
     if(event.keyCode==9)
  	   {
  		document.retailCSSCForm.charge.value="";
  	    document.retailCSSCForm.buttonValue.value="chargeCode";
  	    document.retailCSSCForm.submit();
       }	
    }
  
  function getChgCodeDescPress() {
    if(event.keyCode==13)
  	{
       document.retailCSSCForm.charge.value="";
  	   document.retailCSSCForm.buttonValue.value="chargeCode";
  	   document.retailCSSCForm.submit();
    }	
  }
  
  function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.retailCSSCForm.charge.value;
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
			params['codeDesc'] = document.retailCSSCForm.desc.value;
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
	
		<body class="whitebackgrnd" onLoad="">
	<%}
	
	else{ %>
		<body class="whitebackgrnd">
	<%} %>
	
<html:form action="/retailCSSC" name="retailCSSCForm" type="com.gp.cong.logisoft.struts.ratemangement.form.RetailCSSCForm" scope="request">
			
 <table width="100%"  border="0"  cellpadding="0" cellspacing="0">	 	 
  <%if(session.getAttribute("listcss")==null){%> 		 
 	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
   	 <tr valign="top" class="tableHeadingNew"><td>Commodity Specfic Std Charges</td>
		<td align="right">
		   <input type="button" value="Add" class="buttonStyleNew" id="add" onclick="addfield()" />
		</td>
	</tr>	
 <%}else{%>      
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
   	 <tr valign="top" class="tableHeadingNew">Commodity Specfic Std Charges</tr>
		<tr class="textlabels">
			<td>Chrg Code</td>
			<td>Chrg Desc </td>
			<td>Exclude </td>
			<td>Type </td>
			<td>Std</td>
			<% 
            if(chargeType.equals("11287"))
        	{
        	%>
        		<td>Amt</td>
            <%
         	}
         	if(chargeType.equals("11289"))
         	{
         	%>
           <td><table><tr class="textlabels">
           <%if(defaultRate.equals("E"))
           { %>
         		<td><table><tr id="eng" class="textlabels">
        		<td>$/cft</td>
				<td>$/100lbs</td>
			</tr></table></td>
			<%}
			else if(defaultRate.equals("M"))
			{ %>
			<td><table><tr id="met" class="textlabels">
				<td> $/cbm</td>
				<td> $/1000kg</td>
			</tr></table></td>
			<%} %>
        		<td> MinAmt</td>
        	</tr></table></td>
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
         		<td>MinAmt</td>
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
 		    <td><input name="charge" id="charge" value="<%=chargecode%>" onkeydown="getdestPort(this.value)"  styleClass="varysizeareahighlightgrey" size=4/>
   		         <dojo:autoComplete  formId="retailCSSCForm" textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=RETAIL_CSSC&from=0"/></td>
   		    <td>
<%--   		 <html:text property="desc" value="<%=codeDesc%>"  size="12"/>--%>
        	 	<input name="desc" id="desc" size="12" value="<%=codeDesc%>" onkeydown="getdestPort1(this.value)"/>
        	 	<dojo:autoComplete formId="retailCSSCForm" textboxId="desc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=RETAIL_CSSC"/>
   		 	</td>
   		 	<td><html:checkbox property="exclude" name="retailComm" onclick="disable(this)"></html:checkbox></td>	
  		 	<td ><html:select property="chargeType" styleClass="verysmalldropdownStyle" value="<%=chargeType%>" onchange="submit()">
      								   <html:optionsCollection name="typeList"/>          
                </html:select></td>
         	<td><html:checkbox property="standard" name="retailComm"></html:checkbox></td>
         <% 
         if(chargeType.equals("11287"))
         {
         %>
         	<td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
         <% 
         }
         if(chargeType.equals("11289"))
         {
         %>
        	 <td><table><tr>
         <%if(defaultRate.equals("E"))
         { %>
          
        	 <td><table><tr id="eng1">
         	 <td><html:text property="amtPerCft" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPerCft" value="<%=cft%>"/></td>
  		 	 <td><html:text property="amtPer100lbs" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer100lbs" value="<%=lbs%>"/></td>
  		   </tr></table></td>
  		 <%}
  		 else if(defaultRate.equals("M"))
  		 { %>
  			 <td><table><tr id="met1">
  			 <td><html:text property="amtPerCbm" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPerCbm" value="<%=cbm%>"/></td>
  			 <td><html:text property="amtPer1000kg" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer1000kg" value="<%=amtkg%>"/></td>	
  			 </tr></table></td>
  		<%}%>
  		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
         </tr></table></td>
         <%
         }
         if(chargeType.equals("11288"))
         {
         %>
         <td><html:text property="percentage" maxlength="3" size="3" onblur="checkdec(this)" value="<%=percentage%>"/>%</td>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 if(chargeType.equals("11291"))
		 {
		 %>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 if(chargeType.equals("11290"))
		 {
		 %>
		  <td><html:checkbox property="asFreightedCheckBox" name="retailComm"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		<% } %>
  			<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" size="7" readonly="true"/></td>
  			<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
  			<td align="center">
  		       <input type="button" value="Add To List" class="buttonStyleNew" id="add" onclick="addAGSS('<%=retailComm.getExclude()%>')" style="width:80px"/>
        	</td>
       </tr> 
     <%}%>
     <tr><td colspan="2"></td></tr></table></table></table>
     <br>
	 <table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	  <tr class="tableHeadingNew">List of Commodity Specfic Std Charges</tr>
	  <tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int k=0;
        %>
        <display:table name="<%=csssList%>" pagesize="<%=pageSize%>" class="displaytagstyle"  id="agsstable" > 
			
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
				Date effective_Date=null;
				String effDate="";
				String type="";
				String chargeCode="";
				String tempPath="";
				String perc="",comCode="";
				
				
			    if(csssList != null && csssList.size()>0)
			    {
					RetailCommodityCharges retailCommodityChargesObj2=(RetailCommodityCharges)csssList.get(k);
					if(retailCommodityChargesObj2.getChargeType()!=null)
					{
					type=retailCommodityChargesObj2.getChargeType().getCodedesc();					
					}
					if(retailCommodityChargesObj2.getChargeCode()!=null)
					{
					chargeCode=retailCommodityChargesObj2.getChargeCode().getCodedesc();
					comCode =retailCommodityChargesObj2.getChargeCode().getCode();
					}
					
  					if(retailCommodityChargesObj2.getEffectiveDate()!=null){
  					effective_Date=retailCommodityChargesObj2.getEffectiveDate();
  					effDate=dateFormat.format(effective_Date);
					}
					if(retailCommodityChargesObj2.getPercentage()!=null)
					{
					perc=per.format(retailCommodityChargesObj2.getPercentage()) + "%";
					}
				if(retailCommodityChargesObj2.getAmtPer1000kg()!=null){
				
				amtkg=df.format(retailCommodityChargesObj2.getAmtPer1000kg());
				}
				if(retailCommodityChargesObj2.getInsuranceRate()!=null)
				{
				insuranceRate=df.format(retailCommodityChargesObj2.getInsuranceRate());
				
				}	
				
				if(retailCommodityChargesObj2.getInsuranceAmt()!=null)
				{
				insuranceAmt=df.format(retailCommodityChargesObj2.getInsuranceAmt());
				
				}	
				
				if(retailCommodityChargesObj2.getAmount()!=null)
				{
				amount=df.format(retailCommodityChargesObj2.getAmount());
				
				}	
				
				if(retailCommodityChargesObj2.getMinAmt()!=null)
				{
				minAmt=df.format(retailCommodityChargesObj2.getMinAmt());
				
				}	
				if(retailCommodityChargesObj2.getAmtPerCft()!=null)
				{
				cft=df.format(retailCommodityChargesObj2.getAmtPerCft());
				
				}	
				if(retailCommodityChargesObj2.getAmtPer100lbs()!=null)
				{
				lbs=df.format(retailCommodityChargesObj2.getAmtPer100lbs());
				
				}	
				if(retailCommodityChargesObj2.getAmtPerCbm()!=null)
				{
				cbm=df.format(retailCommodityChargesObj2.getAmtPerCbm());
				
				}	
				}
				String iStr=String.valueOf(k);
  					tempPath=editPath+"?ind="+iStr;
				
			 %>
  			<display:column  title="Chrg Description"><a href="<%=tempPath%>"><%=chargeCode %></a> </display:column>
			<display:column  title="Chrg Type"  ><%=type%></display:column>
			<display:column property="standard" title="STD"></display:column>
		    <display:column property="exclude" title="EXC"></display:column>
			<display:column property="asFreightedCheckBox" title="As Freighted"></display:column>
			<display:column title="Insurance Rate"><%=insuranceRate%></display:column>
			<display:column  title="Insurance Amt"><%=insuranceAmt%></display:column>
			<%if(defaultRate.equals("E"))
			{ %>
			<display:column  title="AmtPerCft" ><%=cft%></display:column>
			<display:column  title="AmtPer100lbs" ><%=lbs%></display:column>
			<%}
			else if(defaultRate.equals("M"))
			{ %>
			<display:column  title="AmtPerCbm" ><%=cbm%></display:column>
			<display:column  title="AmtPer1000kg" ><%=amtkg%></display:column>
			<% } %>
			<display:column title="Perc" ><%=perc%></display:column>
			
			<display:column  title="MinAmt" ><%=minAmt%></display:column>
			
			<display:column  title="Amt"><%=amount%></display:column>
			
			<display:column title="Effec Date" ><%=effDate%></display:column>
			<display:column>
			<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
			<img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteCOMM('<%=comCode%>')" />
			</span>
			</display:column>
  			<% k++;
  		   	   
  			%>
		     	 
		</display:table>
        </table></div>  
    	</td> 
  		</tr>  
		</table>
		

  		<html:hidden property="buttonValue" styleId="buttonValue"/>
  		<html:hidden property="index" />
  		<html:hidden property="retailCSSId" />
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

