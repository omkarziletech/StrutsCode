<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.FTFCommodityCharges,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.LCLColoadStandardCharges,org.apache.struts.util.LabelValueBean"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List applyGeneralStandardList=new ArrayList();
List typeList=new ArrayList();
List csssList=null;
AirRatesBean airRatesBean=new AirRatesBean();
FTFCommodityCharges ftfCommodityCharges=new FTFCommodityCharges();
airRatesBean.setStandard("off");
airRatesBean.setAsFrfgted("off");
airRatesBean.setExclude("off");
request.setAttribute("airRatesBean",airRatesBean);
DBUtil dbUtil=new DBUtil();
String chargeType="0";
String chargecode="";
String codeDesc="";
String amount="";
String cft="";
String lbs="";
String cbm="";
String minAmt="";
String amtkg="";
String percentage="";
String insuranceRate="";
String insuranceAmt="";
String effectiveDate="";
String modify="";
String msg="";
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("0.000");
effectiveDate=dateFormat.format(new Date());
if(session.getAttribute("addftfcommodity")!=null)
{
ftfCommodityCharges=(FTFCommodityCharges)session.getAttribute("addftfcommodity");
if(ftfCommodityCharges.getChargeCode()!=null)
{
chargecode=ftfCommodityCharges.getChargeCode().getCode();
codeDesc=ftfCommodityCharges.getChargeCode().getCodedesc();
}
if(ftfCommodityCharges.getChargeType()!=null&&ftfCommodityCharges.getChargeType().getId()!=null)
{
chargeType=ftfCommodityCharges.getChargeType().getId().toString();

}
if(ftfCommodityCharges.getAmount()!=null)
{
amount=df.format(ftfCommodityCharges.getAmount());
}
if(ftfCommodityCharges.getAmtPerCft()!=null)
{
cft=df.format(ftfCommodityCharges.getAmtPerCft());
}
if(ftfCommodityCharges.getAmtPer100lbs()!=null)
{
lbs=df.format(ftfCommodityCharges.getAmtPer100lbs());
}
if(ftfCommodityCharges.getAmtPerCbm()!=null)
{
cbm=df.format(ftfCommodityCharges.getAmtPerCbm());
}
if(ftfCommodityCharges.getMinAmt()!=null)
{
minAmt=df.format(ftfCommodityCharges.getMinAmt());
}
if(ftfCommodityCharges.getAmtPer1000kg()!=null)
{
amtkg=df.format(ftfCommodityCharges.getAmtPer1000kg());
}
if(ftfCommodityCharges.getPercentage()!=null)
{
percentage=per.format(ftfCommodityCharges.getPercentage()) ;
}
if(ftfCommodityCharges.getInsuranceAmt()!=null)
{
insuranceAmt=df.format(ftfCommodityCharges.getInsuranceAmt());
}
if(ftfCommodityCharges.getInsuranceRate()!=null)
{
insuranceRate=df.format(ftfCommodityCharges.getInsuranceRate());
}
if(ftfCommodityCharges.getEffectiveDate()!=null)
{
effectiveDate=dateFormat.format(ftfCommodityCharges.getEffectiveDate());
}
}

if(session.getAttribute("applyftfcharges")!=null)
{
	applyGeneralStandardList=(List)session.getAttribute("applyftfcharges");
	
}	
List typeList1=new ArrayList();
FTFCommodityCharges ftfComm1=new FTFCommodityCharges();
if(session.getAttribute("ftfcssList") != null)
{
	csssList = (List) session.getAttribute("ftfcssList");
}
if(session.getAttribute("ftfcssList") != null)
{
	csssList = (List) session.getAttribute("ftfcssList");
	for(int i=0;i<csssList.size();i++)
	{
	FTFCommodityCharges ftfComm=(FTFCommodityCharges)csssList.get(i);
	if(ftfComm.getChargeCode()!=null && ftfComm.getChargeCode().getCode().equals(chargecode))
	{
	typeList1.add(ftfComm);
	}
	}
}



if(typeList != null)
{
	typeList=dbUtil.getTypeListForftfCsss(new Integer(34),"yes","Select Rate type",typeList1);
	request.setAttribute("typeList",typeList);
}

//-----------------------------------
	request.setAttribute("typeList",typeList);

modify = (String) session.getAttribute("modifyforftfRates");
if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}

request.setAttribute("typeList",typeList);
String editPath=path+"/addFTFCommodity.do";

String defaultRate="";
if(session.getAttribute("ftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("ftfdefaultRate");
}

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
		<title>JSP for AddFTFCommodityForm form</title>
		<meta http-equiv="pragma" content="no-cache">
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp"%>

		
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>	
			
		<script language="javascript" type="text/javascript">
				function change1(val)
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
			document.addFTFCommodityForm.buttonValue.value="";
			document.addFTFCommodityForm.submit();
			return false;
		}
		function submit()
		{
		document.addFTFCommodityForm.buttonValue.value="chargeType";
		document.addFTFCommodityForm.submit();
		}
		function addlcco()
		{
		if(document.addFTFCommodityForm.exclude.checked==false && document.addFTFCommodityForm.chargeType.value=="0")
		{
		alert("Please select Exclude  or Charge Type");
		return;
		}
		
		if(document.addFTFCommodityForm.charge.value=="")
		{
		alert("Please select Charge code");
		return;
		}
			document.addFTFCommodityForm.buttonValue.value="add";
  			document.addFTFCommodityForm.submit();
		}
			function disabled(val1)
   {
	if(val1!=""&&(val1 == 0 || val1== 3))
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
  	 	 document.getElementById("add").style.visibility = 'hidden';
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
   function addlItem()
   {
        	document.addFTFCommodityForm.buttonValue.value="addItem";
  			document.addFTFCommodityForm.submit();
   }
    function getChgCode()
  {
  	if(event.keyCode==9)
  	{
  	 document.addFTFCommodityForm.buttonValue.value="popupsearch";
  	 document.addFTFCommodityForm.submit();
    }		
  }
  function getChgCodePress()
  {
   if(event.keyCode==13)
  	{
  	 document.addFTFCommodityForm.buttonValue.value="popupsearch";
  	 document.addFTFCommodityForm.submit();
    }	
  }
  
  function getChgCodeDesc()
  {
     if(event.keyCode==9)
  	{
  	 document.addFTFCommodityForm.buttonValue.value="popupsearch";
  	 document.addFTFCommodityForm.submit();
    }	
  }
  
  function getChgCodeDescPress()
  {
    if(event.keyCode==13)
  	{
  	 document.addFTFCommodityForm.buttonValue.value="popupsearch";
  	 document.addFTFCommodityForm.submit();
    }	
  }
  function deleteCOMM(val1){
  	  document.addFTFCommodityForm.commodityId.value=val1;
  	  document.addFTFCommodityForm.buttonValue.value="delete";
  	  document.addFTFCommodityForm.submit();
  }
  
  		
				    function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.addFTFCommodityForm.charge.value;
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
			params['codeDesc'] = document.addFTFCommodityForm.desc.value;
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
	
		<html:form action="/addFTFCommodity" name="addFTFCommodityForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AddFTFCommodityForm" scope="request">
			<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
				<tr class="tableHeadingNew"> <td>Commodity Specfic Std Charges</td>
				<td align="right"><input type="button" value="Add" id="add" class="buttonStyleNew" onclick="addlItem()" id="add"/></td>
				 </tr>
			 <tr>
			  <td colspan="2">
	
	 <table width="100%" border="0" cellpadding="0" cellspacing="4">
			<tr class="textlabels">
<%
if(session.getAttribute("listCoFtfitem")==null)
{ 

%> 
			
	<%}else{ %>			
					<td>
						Chrg Code
					</td>
					<td>
						Chrg Desc
					</td>
					<td>
						Exclude
					</td>
					<td>
						Type
					</td>
					<td>
						Std
					</td>
					<%
					if (chargeType.equals("11287")) {
					%>
					<td>
						Amt
					</td>

					<%
						}
						if (chargeType.equals("11289")) {
					%>
					<%if(defaultRate.equals("E"))
					{ %>
					<td>
						$/cft
					</td>
					<td>
						$/100lbs
					</td>
					<%
					}
					else if(defaultRate.equals("M"))
					{ %>
					<td>
						$/cbm
					</td>
					<td>
						$/1000kg
					</td>
					<%} %>
					<td>
						MinAmt
					</td>
					<%
						}
						if (chargeType.equals("11288")) {
					%>
					<td>
						<td>
						Percentage
						</td>
				    
					<td>
						MinAmt
					</td>
					<%
						}
						if (chargeType.equals("11291")) {
					%>
					<td>
						MinAmt
					</td>
					<%
						}
						if (chargeType.equals("11290")) {
					%>
					<td>
						As Freighted
					</td>
					<td>
						Insurance Rate
					</td>
					<td>
						Insurance Amt
					</td>
					<td>
						MinAmt
					</td>
					<%
					}
					%>

					<td>
						Eff.Date
					</td>

				</tr>
				<tr class="textlabels">
					<td>
<%--						<html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3" />--%>
				    <input name="charge" id="charge" size="4" value="<%=chargecode%>" onkeydown="getdestPort(this.value)"/>
                    <dojo:autoComplete formId="addFTFCommodityForm" textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=FTF&from=0"/>
					</td>
					<td>
<%--						<html:text property="desc" value="<%=codeDesc%>" size="12" />--%>
                  <input name="desc" id="desc" size="12" value="<%=codeDesc%>"  onkeydown="getdestPort1(this.value)"/>
                  <dojo:autoComplete formId="addFTFCommodityForm" textboxId="desc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=FTF_COMMODITY"/>
					</td>
					<td>
						<html:checkbox property="exclude" name="airRatesBean"
							onclick="change1(exclude)"></html:checkbox>
					</td>
					<td>
						<html:select property="chargeType"
							styleClass="verysmalldropdownStyle" value="<%=chargeType%>"
							onchange="submit()">
							<html:optionsCollection name="typeList" />
						</html:select>
					</td>
					<td>
						<html:checkbox property="standard" name="airRatesBean"></html:checkbox>
					</td>
					<%
					if (chargeType.equals("11287")) {
					%>
					<td>
						<html:text property="amount" maxlength="8" size="8"
							 onkeypress="check(this,5)" onblur="checkdec(this)" styleId="amount"
							value="<%=amount%>" />
					</td>
					<%
						}
						if (chargeType.equals("11289")) {
					%>
					<%if(defaultRate.equals("E"))
					{ %>
					<td>
						<html:text property="amtPerCft" maxlength="8" size="8"
							 onkeypress="check(this,5)" onblur="checkdec(this)" styleId="amtPerCft"
							value="<%=cft%>" />
					</td>
					<td>
						<html:text property="amtPer100lbs" maxlength="8" size="8"
							onkeypress="check(this,5)" onblur="checkdec(this)" styleId="amtPer100lbs"
							value="<%=lbs%>" />
					</td>
					<%}
					else if(defaultRate.equals("M"))
					{ %>
					<td>
						<html:text property="amtPerCbm" maxlength="8" size="8"
							 onkeypress="check(this,5)" onblur="checkdec(this)" styleId="amtPerCbm"
							value="<%=cbm%>" />
					</td>
					<td>
						<html:text property="amtPer1000kg" maxlength="8" size="8"
							onkeypress="check(this,5)" onblur="checkdec(this)" styleId="amtPer1000kg"
							value="<%=amtkg%>" />
					</td>
					<%} %>
					<td>
						<html:text property="minAmt" maxlength="8" size="8"
							onkeypress="check(this,5)" onblur="checkdec(this)" styleId="minAmt"
							value="<%=minAmt%>" />
					</td>
					<%
						}
						if (chargeType.equals("11288")) {
					%>
					<td>
						<td>
							<html:text property="percentage"  maxlength="3" size="3" onblur="checkdec(this)"
							 value="<%=percentage%>" />%</td>
					
					
					<td>
						<html:text property="minAmt" maxlength="8" size="8"
							 onkeypress="check(this,5)" onblur="checkdec(this)" styleId="minAmt"
							value="<%=minAmt%>" />
					</td>

					<%
						}
						if (chargeType.equals("11291")) {
					%>
					<td>
						<html:text property="minAmt" maxlength="8" size="8"
							onkeypress="check(this,5)" onblur="checkdec(this)" styleId="minAmt"
							value="<%=minAmt%>" />
					</td>
					<%
						}
						if (chargeType.equals("11290")) {
					%>
					<td>
						<html:checkbox property="asFrfgted" name="airRatesBean"></html:checkbox>
					</td>
					<td>
						<html:text property="insuranceRate" maxlength="6" size="6"
							 onkeypress="check(this,3)" onblur="checkdec(this)" styleId="minAmt"
							value="<%=insuranceRate%>" />
					</td>
					<td>
						<html:text property="insuranceAmt" maxlength="8" size="8"
							 onkeypress="check(this,5)" onblur="checkdec(this)" styleId="minAmt"
							value="<%=insuranceAmt%>" />
					</td>
					<td>
						<html:text property="minAmt" maxlength="8" size="8"
							 onkeypress="check(this,5)" onblur="checkdec(this)" styleId="minAmt"
							value="<%=minAmt%>" />
					</td>
					<%
					}
					%>
					<td>
						<html:text property="txtItemcreatedon" styleId="txtitemcreatedon"
							value="<%=effectiveDate%>" size="7" readonly="true" />
					</td>
					<td>
						<div>
							<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
								id="itemcreatedon"
								onmousedown="insertDateFromCalendar(this.id,0);" />
						</div>
					</td>
					<td align="center">
						<input type="button" value="Add To List" class="buttonStyleNew" onclick="addlcco()" id="add"/>
					</td>
				</tr>
				<%} %>
			</table>
			<table width=100% border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<div id="divtablesty1" class="scrolldisplaytable">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" >
								<%
								int k = 0;
								%>
								<display:table name="<%=csssList%>" pagesize="<%=pageSize%>"
									class="displaytagstyle" sort="list" id="agsstable"
									>

									<display:setProperty name="paging.banner.some_items_found">
										<span class="pagebanner"> <font color="blue">{0}</font>
											Item Details Displayed,For more Items click on Page Numbers.
											<br> </span>
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
										value="Item" />
									<display:setProperty name="paging.banner.items_name"
										value="Items" />
				<%
							String type = "";
							String chargeCode = "";
							String tempPath = "";
							String cheDate = "",comCode="";
						if (csssList != null && csssList.size() > 0) 
						 {
							FTFCommodityCharges ftfComm = (FTFCommodityCharges) csssList.get(k);
							   if (ftfComm.getChargeCode() != null) 
								 { 
								chargeCode = ftfComm.getChargeCode().getCodedesc();
								comCode = ftfComm.getChargeCode().getCode();
							     }
							   if (ftfComm.getChargeType() != null&&ftfComm.getChargeType().getCodedesc()!=null)
							    {
								type = ftfComm.getChargeType().getCodedesc();
							    }
						 	 if (ftfComm.getEffectiveDate() != null) 
								  {
									cheDate = dateFormat.format(ftfComm.getEffectiveDate());
								  }
						  
						  	if(ftfComm.getAmtPer1000kg()!=null)
								{
									amtkg=df.format(ftfComm.getAmtPer1000kg());
								}
						  	if(ftfComm.getInsuranceRate()!=null)
								{
								   insuranceRate=df.format(ftfComm.getInsuranceRate());
								}	
				
							if(ftfComm.getInsuranceAmt()!=null)
								{
									insuranceAmt=df.format(ftfComm.getInsuranceAmt());
								}	
				
							if(ftfComm.getAmount()!=null)
								{
									amount=df.format(ftfComm.getAmount());
								}	
				
							if(ftfComm.getMinAmt()!=null)
								{
									minAmt=df.format(ftfComm.getMinAmt());
								}	
							if(ftfComm.getAmtPerCft()!=null)
								{
									cft=df.format(ftfComm.getAmtPerCft());
								}	
							if(ftfComm.getAmtPer100lbs()!=null)
								{
									lbs=df.format(ftfComm.getAmtPer100lbs());
								}	
							if(ftfComm.getAmtPerCbm()!=null)
								{
									cbm=df.format(ftfComm.getAmtPerCbm());
								}	
							if(ftfComm.getPercentage() != null)
								{
								   percentage = per.format(ftfComm.getPercentage()) + "%";
								}
					
						 }
							String iStr = String.valueOf(k);
							tempPath = editPath + "?ind=" + iStr;
				%>
									<display:column title="Charge Description">
										<a href="<%=tempPath%>"><%=chargeCode%>
										</a>
									</display:column>
									<display:column title="Charge Type">
										<%=type%>
									</display:column>
									<display:column property="standard" title="Standard"></display:column>

									<display:column property="asFrfgted" title="As Freighted"></display:column>
									<display:column title="Insurance Rate"><%=insuranceRate %></display:column>
									<display:column title="Insurance Amount"><%=insuranceAmt%></display:column>
									<%if(defaultRate.equals("E"))
									{ %>
									<display:column title="AmtPerCft" ><%=cft%></display:column>
									<display:column title="AmtPer100lbs" ><%=lbs%></display:column>
									<%}
									else if(defaultRate.equals("M"))
									{ %>
									<display:column title="AmtPerCbm"><%=cbm%></display:column>
									<display:column  title="Amt/1000kg" ><%=amtkg%></display:column>
									<%} %>
									<display:column  title="Percentage" > <%=percentage %> </display:column>

									<display:column  title="MinAmt" ><%=minAmt%></display:column>

									<display:column title="Amt"><%=amount%></display:column>

									<display:column title="Effective Date">
										<%=cheDate%>
									</display:column>
<display:column>
			<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
			<img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteCOMM('<%=comCode%>')" />
			</span>
			</display:column>
									<%
									k++;
									%>

								</display:table>

							</table>
						</div>
					</td>
				</tr>
			</table>

			<html:hidden property="buttonValue" styleId="buttonValue" />
			<html:hidden property="index" />
			<html:hidden property="commodityId" />
		 </td>
		</tr>
	</table>	
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

