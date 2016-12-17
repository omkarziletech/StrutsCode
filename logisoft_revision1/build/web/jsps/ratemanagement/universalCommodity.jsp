<%@ page language="java"
	import="java.text.DecimalFormat,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.util.DBUtil,java.text.DateFormat,java.text.SimpleDateFormat,java.util.*,com.gp.cong.logisoft.domain.UniverseCommodityChrg,com.gp.cong.logisoft.domain.AirStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	List csssList = new ArrayList();
	List applyGeneralStandardList = new ArrayList();
	String chargecode = "";
	String codeDesc = "";
	String msg = "";
	DBUtil dbUtil = new DBUtil();
	String chargeType = "";
	String rateType = "";
	String minAmt = "";
	User user = null;
	String amount = "";
	String insuranceRate = "";
	String insuranceAmt = "";
	String percentage = "";
	String cft = "";
	String lbs = "";
	String cbm = "";
	String amtkg = "";
	String effectiveDate = "";
	Date d = new Date();

	DecimalFormat df2 = new DecimalFormat("########0.00");
	DecimalFormat per = new DecimalFormat("########0.000");
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DecimalFormat df = new DecimalFormat("0.00");
	effectiveDate = dateFormat.format(d);
	if (session.getAttribute("loginuser") != null) {
		user = (User) session.getAttribute("loginuser");
	}
	AirRatesBean airRatesBean = new AirRatesBean();
	UniverseCommodityChrg airComm = new UniverseCommodityChrg();
	airComm.setStandard("off");
	airComm.setAsFrfgted("off");
	airComm.setExclude("off");
	if (request.getAttribute("airRatesBean") != null) {
		airRatesBean = (AirRatesBean) request
				.getAttribute("airRatesBean");
		airComm.setStandard(airRatesBean.getStandard());
		airComm.setAsFrfgted(airRatesBean.getAsFrfgted());
		airComm.setExclude(airRatesBean.getExclude());
	}
	request.setAttribute("airComm", airComm);
	List typeList = new ArrayList();
	UniverseCommodityChrg airCommodityCharges = new UniverseCommodityChrg();
	airCommodityCharges.setStandard("off");

	if (session.getAttribute("uniCommodityCharges") != null) {
		airCommodityCharges = (UniverseCommodityChrg) session
				.getAttribute("uniCommodityCharges");
		if (airCommodityCharges != null) {
			if (airCommodityCharges.getChargeCode() != null) {
				chargecode = airCommodityCharges.getChargeCode()
						.getCode();
				codeDesc = airCommodityCharges.getChargeCode()
						.getCodedesc();
			}
			
			if (airCommodityCharges.getChargeType() != null) {
				chargeType = airCommodityCharges.getChargeType()
						.getId().toString();
			
			}
			if (airCommodityCharges.getMinAmt() != null) {
				minAmt = df2.format(airCommodityCharges.getMinAmt());
			}
			if (airCommodityCharges.getAmount() != null) {
				amount = df2.format(airCommodityCharges.getAmount());
			}
			if (airCommodityCharges.getInsuranceRate() != null) {
				insuranceRate = df2.format(airCommodityCharges
						.getInsuranceRate());
			}
			if (airCommodityCharges.getInsuranceAmt() != null) {
				insuranceAmt = df2.format(airCommodityCharges
						.getInsuranceAmt());
			}
			if (airCommodityCharges.getPercentage() != null) {
				percentage = per.format(airCommodityCharges
						.getPercentage());
			}
			if (airCommodityCharges.getAmtPerCft() != null) {
				cft = df2.format(airCommodityCharges.getAmtPerCft());
			}
			if (airCommodityCharges.getAmtPerCbm() != null) {
				cbm = df2.format(airCommodityCharges.getAmtPerCbm());
			}
			if (airCommodityCharges.getAmtPer100lbs() != null) {
				lbs = df2.format(airCommodityCharges.getAmtPer100lbs());
			}
			if (airCommodityCharges.getAmtPer1000kg() != null) {
				amtkg = df2.format(airCommodityCharges
						.getAmtPer1000kg());
			}
			if (airCommodityCharges.getEffectiveDate() != null) {
				effectiveDate = dateFormat.format(airCommodityCharges
						.getEffectiveDate());
			}
		}

	}
	
	if (session.getAttribute("unicssList") != null) {
		csssList = (List) session.getAttribute("unicssList");
	}

	if (typeList != null) {
		typeList = dbUtil.getTypeListForUniverse(new Integer(34),
				"yes", "Select Rate type");

		request.setAttribute("typeList", typeList);
	}

	request.setAttribute("typeList", typeList);
	if (session.getAttribute("applyGeneralStandardList") != null) {
		applyGeneralStandardList = (List) session
				.getAttribute("applyGeneralStandardList");
	}

	String modify = "";
	String defaultRate = "";
	modify = (String) session.getAttribute("modifyforairRates");

	if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

	}

	String editPath = path + "/universalCommodity.do";

	if (session.getAttribute("unibverdefaultRate") != null) {
		defaultRate = (String) session
				.getAttribute("unibverdefaultRate");
	}

	if (request.getAttribute("exist") != null) {
		msg = request.getAttribute("exist").toString();
		chargecode = "";
		codeDesc = "";

	}
%>
<font color="red" size="3"><%=msg%></font>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>Commodity Specific Charges</title>

		<%@include file="../includes/baseResources.jsp"%>


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
	    function searchform()
		{
			document.universalCommodityForm.buttonValue.value="search";
			document.universalCommodityForm.submit();
		}
		function addAGSS()
		{
		/*if(document.universalCommodityForm.exclude.checked==false && document.universalCommodityForm.chargeType.value=="0")
		{
		alert("Please select Exclude  or Charge Type");
		return;
		}*/
		if(document.universalCommodityForm.charge.value=="")
		{
			alert("Please select Charge code");
			return;
		}
			
			document.universalCommodityForm.buttonValue.value="add";
  			document.universalCommodityForm.submit();
		}
		function confirmdelete(obj)
		{
			var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('agsstable').rows[rowindex].cells;	
	   		document.universalCommodityForm.index.value=rowindex-1;
			document.universalCommodityForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this value");
			if(result)
			{
   				document.universalCommodityForm.submit();
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
			document.universalCommodityForm.submit();
			return false;
		}
		
		function submit()
		{
		document.universalCommodityForm.buttonValue.value="";
			document.universalCommodityForm.submit();
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
  	 	document.getElementById("add").style.visibility = 'hidden';
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 var tables1 = document.getElementById('Unicomm');
		
  		 if(tables1!=null)
  		 {
  	 	 //displaytagcolor();
   		 //initRowHighlighting();
   		// setWarehouseStyle();
   		 }			
    }
    function displaytagcolor()
		{
		 	 	var datatableobj1 = document.getElementById('Unicomm');
		
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
					
		 	 	}
		}
	function initRowHighlighting()
  	{
			if (!document.getElementById('Unicomm')){ return; }
			 var tables = document.getElementById('Unicomm');
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
		
		 function getChgCode()
  {
  	if(event.keyCode==9)
  	{
  	 document.universalCommodityForm.buttonValue.value="popupsearch";
  	 document.universalCommodityForm.submit();
    }		
  }
  function getChgCodePress()
  {
   if(event.keyCode==13)
  	{
  	 document.universalCommodityForm.buttonValue.value="popupsearch";
  	 document.universalCommodityForm.submit();
    }	
  }
  
  function getChgCodeDesc()
  {
     if(event.keyCode==9)
  	{
  	 document.universalCommodityForm.buttonValue.value="popupsearch";
  	 document.universalCommodityForm.submit();
    }	
  }
  
  function getChgCodeDescPress()
  {
    if(event.keyCode==13)
  	{
  	 document.universalCommodityForm.buttonValue.value="popupsearch";
  	 document.universalCommodityForm.submit();
    }	
  }
  	 function addfield()
		{
		     document.universalCommodityForm.buttonValue.value="addItem";
  	 document.universalCommodityForm.submit();
		}
		
<%--typr 2 dojo implementation		--%>
    function getdestPort(ev){ 
    	document.getElementById("desc").value="";
		if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.universalCommodityForm.charge.value;
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
			params['codeDesc'] = document.universalCommodityForm.desc.value;
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
		if (modify != null && modify.equals("3")) {
	%>

	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<%
			}

			else {
		%>
	
	<body class="whitebackgrnd">
		<%
			}
		%>
		<html:form action="/universalCommodity" name="universalCommodityForm"
			type="com.gp.cong.logisoft.struts.ratemangement.form.UniversalCommodity" scope="request">

			<table class="tableBorderNew" border="0" width="100%" cellpadding="0"
				cellspacing="0">
				<tr class="tableHeadingNew">
					<td>
						Commodity Specfic Std Charges
					</td>
					<td align="right">
						<input type="button" value="Add" onclick="addfield()" id="add"
							class="buttonStyleNew" />
					</td>

				</tr>
				<tr>
					<td colspan="2">
						<table width=100% border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td>
									<%
										if (session.getAttribute("uniCssAdd") == null) {
									%>

									<%
										} else {
									%>
									<table width="100%" border="0" cellpadding="0" cellspacing="0">

										<tr class="textlabels">
											<td>
												Chrg Code
											</td>
											<td>
												Charge Desc
											</td>
											<%--			<td>Exclude </td>--%>
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
											<%
												if (defaultRate.equals("E")) {
											%>
											<td>
												$/cft
											</td>
											<td>
												$/100lbs
											</td>
											<%
												} else if (defaultRate.equals("M")) {
											%>
											<td>
												$/cbm
											</td>
											<td>
												$/1000kg
											</td>
											<%
												}
											%>
											<td>
												MinAmt
											</td>
											<%
												}
														if (chargeType.equals("11288")) {
											%>
											<td>
												Percent
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
												Insurance Amount
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
												<%-- 		 <html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3"/>--%>
												<input name="charge" id="charge" size="4"
													value="<%=chargecode%>" onkeydown="getdestPort(this.value)" />
												<dojo:autoComplete formId=universalCommodityForm
													textboxId="charge"
													action="<%=path%>/actions/getChargeCode.jsp?tabName=UNIVERSAL_COMMODITY&from=0" />
											</td>
											<td>
												<%--   		 <html:text property="desc" value="<%=codeDesc%>" readonly="true" styleClass="varysizeareahighlightgrey" size="12"/>--%>
												<input name="desc" id="desc" size="12" value="<%=codeDesc%>"
													onkeydown="getdestPort1(this.value)" />
												<dojo:autoComplete formId="universalCommodityForm"
													textboxId="desc"
													action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=UNIVERSAL_COMMODITY" />
											</td>
											<%--   	<td>	<html:checkbox property="exclude" name="airComm"onclick="change1(exclude)"></html:checkbox>	</td>--%>
											<td>
												<html:select property="chargeType"
													styleClass="verysmalldropdownStyle" value="<%=chargeType%>"
													onchange="submit()">
													<html:optionsCollection name="typeList" />
												</html:select>
											</td>
											<td>
												<html:checkbox property="standard" name="airComm"></html:checkbox>
											</td>
											<%
												if (chargeType.equals("11287")) {
											%>
											<td>
												<html:text property="amount" onkeypress="check(this,5)"
													onblur="checkdec(this)" maxlength="8" size="8"
													styleId="amount" value="<%=amount%>" />
											</td>
											<%
												}
														if (chargeType.equals("11289")) {
											%>
											<%
												if (defaultRate.equals("E")) {
											%>
											<td>
												<html:text property="amtPerCft" onkeypress="check(this,5)"
													onblur="checkdec(this)" maxlength="8" size="8"
													styleId="amtPerCft" value="<%=cft%>" />
											</td>
											<td>
												<html:text property="amtPer100lbs" maxlength="8" size="8"
													onkeypress="check(this,5)" onblur="checkdec(this)"
													styleId="amtPer100lbs" value="<%=lbs%>" />
											</td>
											<%
												} else if (defaultRate.equals("M")) {
											%>
											<td>
												<html:text property="amtPerCbm" maxlength="8" size="8"
													onkeypress="check(this,5)" onblur="checkdec(this)"
													styleId="amtPerCbm" value="<%=cbm%>" />
											</td>
											<td>
												<html:text property="amtPer1000kg" maxlength="8" size="8"
													onkeypress="check(this,5)" onblur="checkdec(this)"
													styleId="amtPer1000kg" value="<%=amtkg%>" />
											</td>
											<%
												}
											%>
											<td>
												<html:text property="minAmt" maxlength="8" size="8"
													onkeypress="check(this,5)" onblur="checkdec(this)"
													styleId="minAmt" value="<%=minAmt%>" />
											</td>
											<%
												}
														if (chargeType.equals("11288")) {
											%>
											<td>
												<html:text property="percentage" onblur="checkdec(this)"
													maxlength="3" size="3" value="<%=percentage%>" />
												%
											</td>
											<td>
												<html:text property="minAmt" onkeypress="check(this,5)"
													onblur="checkdec(this)" maxlength="8" size="8"
													styleId="minAmt" value="<%=minAmt%>" />
											</td>

											<%
												}
														if (chargeType.equals("11291")) {
											%>
											<td>
												<html:text property="minAmt" onkeypress="check(this,5)"
													onblur="checkdec(this)" maxlength="8" size="8"
													styleId="minAmt" value="<%=minAmt%>" />
											</td>
											<%
												}
														if (chargeType.equals("11290")) {
											%>
											<td>
												<html:checkbox property="asFrfgted" name="airComm"></html:checkbox>
											</td>
											<td>
												<html:text property="insuranceRate"
													onkeypress="check(this,3)" onblur="checkdec(this)"
													maxlength="6" size="6" styleId="minAmt"
													value="<%=insuranceRate%>" />
											</td>
											<td>
												<html:text property="insuranceAmt"
													onkeypress="check(this,5)" onblur="checkdec(this)"
													maxlength="8" size="8" styleId="minAmt"
													value="<%=insuranceAmt%>" />
											</td>
											<td>
												<html:text property="minAmt" onkeypress="check(this,5)"
													onblur="checkdec(this)" maxlength="8" size="8"
													styleId="minAmt" value="<%=minAmt%>" />
											</td>
											<%
												}
											%>
											<td>
												<html:text property="txtItemcreatedon"
													styleId="txtitemcreatedon" value="<%=effectiveDate%>"
													size="7" readonly="true" />
											</td>
											<td>
												<div>
													<img src="<%=path%>/img/CalendarIco.gif" alt="cal"
														align="top" id="itemcreatedon"
														onmousedown="insertDateFromCalendar(this.id,0);" />
												</div>
											</td>
											<td align="center">
												<input type="button" value="Add To List" onclick="addAGSS()"
													id="add" class="buttonStyleNew" />
											</td>
										</tr>
									</table>
									<%
										}
									%>
									<table width=100% border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td>
												<div id="divtablesty1" class="scrolldisplaytable"
													style="height: 120px">
													<table width="100%" border="0" cellpadding="0"
														cellspacing="0">
														<%
															int k = 0;
														%>
														<display:table name="<%=csssList%>"
															pagesize="<%=pageSize%>" class="displaytagstyle"
															sort="list" id="Unicomm" defaultorder="ascending"
															defaultsort="2">

															<display:setProperty
																name="paging.banner.some_items_found">
																<span class="pagebanner"> <font color="blue">{0}</font>
																	Item Details Displayed,For more Items click on Page
																	Numbers. <br> </span>
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
																Date effective_Date = null;
																				String effDate = null;
																				String type = null;
																				String chargeCode = null;
																				String tempPath = "";
																				String perc = "";
																				if (csssList != null && csssList.size() > 0) {
																					UniverseCommodityChrg airCommodityChargesObj2 = (UniverseCommodityChrg) csssList
																							.get(k);
																					if (airCommodityChargesObj2.getChargeType() != null)
																						type = airCommodityChargesObj2
																								.getChargeType().getCodedesc();
																					if (airCommodityChargesObj2.getChargeCode() != null)
																						chargeCode = airCommodityChargesObj2
																								.getChargeCode().getCode();

																					if (airCommodityChargesObj2.getEffectiveDate() != null) {
																						effective_Date = airCommodityChargesObj2
																								.getEffectiveDate();
																						effDate = dateFormat.format(effective_Date);
																					}
																					if (airCommodityChargesObj2.getPercentage() != null) {
																						perc = per.format(airCommodityChargesObj2
																								.getPercentage())
																								+ "%";
																					}

																					if (airCommodityChargesObj2.getAmtPer1000kg() != null) {
																						amtkg = df.format(airCommodityChargesObj2
																								.getAmtPer1000kg());
																					}
																					if (airCommodityChargesObj2.getInsuranceRate() != null) {
																						insuranceRate = df
																								.format(airCommodityChargesObj2
																										.getInsuranceRate());
																					}

																					if (airCommodityChargesObj2.getInsuranceAmt() != null) {
																						insuranceAmt = df
																								.format(airCommodityChargesObj2
																										.getInsuranceAmt());
																					}

																					if (airCommodityChargesObj2.getAmount() != null) {
																						amount = df.format(airCommodityChargesObj2
																								.getAmount());
																					}

																					if (airCommodityChargesObj2.getMinAmt() != null) {
																						minAmt = df.format(airCommodityChargesObj2
																								.getMinAmt());
																					}
																					if (airCommodityChargesObj2.getAmtPerCft() != null) {
																						cft = df.format(airCommodityChargesObj2
																								.getAmtPerCft());
																					}
																					if (airCommodityChargesObj2.getAmtPer100lbs() != null) {
																						lbs = df.format(airCommodityChargesObj2
																								.getAmtPer100lbs());
																					}
																					if (airCommodityChargesObj2.getAmtPerCbm() != null) {
																						cbm = df.format(airCommodityChargesObj2
																								.getAmtPerCbm());
																					}

																				}
																				String iStr = String.valueOf(k);
																				tempPath = editPath + "?ind=" + iStr;
															%>
															<display:column title="Chrg Code">
																<a href="<%=tempPath%>"><%=chargeCode%></a>
															</display:column>
															<display:column title="Chrg Type"><%=type%></display:column>
															<display:column property="standard" title="Std"></display:column>

															<display:column property="asFrfgted" title="As Freighted"></display:column>
															<display:column title="Insure Rate"><%=insuranceRate%></display:column>
															<display:column title="Insure Amt"><%=insuranceAmt%></display:column>
															<%
																if (defaultRate.equals("E")) {
															%>
															<display:column title="Amt/Cft"><%=cft%></display:column>
															<display:column title="Amt/100lbs"><%=lbs%></display:column>

															<%
																} else if (defaultRate.equals("M")) {
															%>
															<display:column title="Amt/Cbm"><%=cbm%></display:column>
															<display:column title="Amt/1000kg"><%=amtkg%></display:column>
															<%
																}
															%>
															<display:column title="Perc"><%=perc%></display:column>

															<display:column title="MinAmt"><%=minAmt%></display:column>

															<display:column title="Amt"><%=amount%></display:column>


															<display:column title="Eff Date"><%=effDate%></display:column>
															<%
																k++;
															%>




														</display:table>

													</table>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
			<html:hidden property="index" />
		</html:form>
	</body>
</html>
