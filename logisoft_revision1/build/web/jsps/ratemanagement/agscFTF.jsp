<%@ page language="java"
	import="com.gp.cong.logisoft.util.DBUtil,java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.FTFStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat" />
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();

java.util.Date date = new java.util.Date();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("0.000");

String chargecode="";
String codeDesc="";
List typeList=new ArrayList();
List agssList=new ArrayList();
String chargeType="0";
FTFStandardCharges ftfStandardCharges=new FTFStandardCharges();

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
String amtkg="",result ="";
String chartypedesc="";
String modify="";

FTFStandardCharges ftfStd=new FTFStandardCharges();
ftfStd.setStandard("off");
ftfStd.setAsFrfgted("off");

java.util.Date date1 = new java.util.Date();
effectiveDate=dateFormat.format(date1);
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
ftfStd.setStandard(airRatesBean.getStandard());
ftfStd.setAsFrfgted(airRatesBean.getAsFrfgted());

}
request.setAttribute("ftfStd",ftfStd);
if(session.getAttribute("visited") != null)
{
  session.removeAttribute("ftfStandardCharges");
  session.removeAttribute("visited");
}
if(session.getAttribute("ftfStandardCharges")!=null )
{
	
   ftfStandardCharges=(FTFStandardCharges)session.getAttribute("ftfStandardCharges");
	if(ftfStandardCharges.getChargeCode()!=null)
	{
	  
		chargecode=ftfStandardCharges.getChargeCode().getCode();
		codeDesc=ftfStandardCharges.getChargeCode().getCodedesc();
	}
	if(ftfStandardCharges.getChargeType()!=null)
	{
	chargeType=ftfStandardCharges.getChargeType().getId().toString();
	chartypedesc=ftfStandardCharges.getChargeType().getCodedesc();
	}
	if(ftfStandardCharges.getMinAmt()!=null)
	{
		minAmt=df.format(ftfStandardCharges.getMinAmt());
	}
	if(ftfStandardCharges.getEffectiveDate()!=null)
	{
		effectiveDate=ftfStandardCharges.getEffectiveDate().toString();
	}
	if(ftfStandardCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(ftfStandardCharges.getInsuranceRate());
	}
	if(ftfStandardCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(ftfStandardCharges.getInsuranceAmt());
	}
	if(ftfStandardCharges.getPercentage()!=null)
	{
	percentage=per.format(ftfStandardCharges.getPercentage());
//	Systm.ou.print
	}
	
	
	if(ftfStandardCharges.getAmount()!=null)
	{
	amount=df.format(ftfStandardCharges.getAmount());
	}
	if(ftfStandardCharges.getAmtPerCft()!=null)
	{
	cft=df.format(ftfStandardCharges.getAmtPerCft());
	}
	if(ftfStandardCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(ftfStandardCharges.getAmtPer100lbs());
	}
	if(ftfStandardCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(ftfStandardCharges.getAmtPerCbm());
	}
	if(ftfStandardCharges.getAmtPer1000Kg()!=null)
	{
	amtkg=df.format(ftfStandardCharges.getAmtPer1000Kg());
	}
	if(ftfStandardCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(ftfStandardCharges.getEffectiveDate());
		}
}
List typeList1=new ArrayList();
if(session.getAttribute("ftfagssAdd") != null)
{
	agssList = (List) session.getAttribute("ftfagssAdd");
	
}

if(session.getAttribute("ftfagssAdd") != null)
{
	agssList = (List) session.getAttribute("ftfagssAdd");
	for(int i=0;i<agssList.size();i++)
	{
	FTFStandardCharges ftfComm=(FTFStandardCharges)agssList.get(i);
	if(ftfComm.getChargeCode()!=null && ftfComm.getChargeCode().getCode().equals(chargecode))
	{
	typeList1.add(ftfComm);
	}
	}
}


if(typeList != null)
{
	typeList=dbUtil.getTypeFTFList(new Integer(34),"yes","Select Rate type",typeList1);
	request.setAttribute("typeList",typeList);
	
}

	
	
request.setAttribute("typeList",typeList);
String msg="";
if(request.getAttribute("message")!=null)
{
	modify=(String)request.getAttribute("message");
}
modify = (String) session.getAttribute("modifyforftfRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/agscFTF.do";

String defaultRate="";
if(session.getAttribute("ftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("ftfdefaultRate");
}

if(session.getAttribute("serachftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("serachftfdefaultRate");
}

 if(request.getAttribute("exist") != null)
  {
     msg = request.getAttribute("exist").toString();
     chargecode = "";
     codeDesc = "";
   
  }
%>
<font color="red" size="3"><%=msg%>
</font>
<html>
	<head>
		<title>JSP for AgscFTFForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
		<%@include file="../includes/resources.jsp"%>

		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		   dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		<script language="javascript" type="text/javascript">
		function addAGSS()
		{
		if(document.agscFTFForm.chargeType.value=="0")
		{
		alert("Please select Charge Type");
		return;
		}
		if(document.agscFTFForm.charge.value=="")
		{
		alert("Please select Charge code");
		return;
		}
			document.agscFTFForm.buttonValue.value="add";
			document.agscFTFForm.submit();
		}
		
   		function submit()
		{
		document.agscFTFForm.buttonValue.value="";
			document.agscFTFForm.submit();
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
			document.agscFTFForm.submit();
			return false;
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
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
		function addfield()
		{
		 	
		 	document.agscFTFForm.buttonValue.value="addItem";
			document.agscFTFForm.submit();		  
		}
			  
		function getCodeDesc(ev)
		{
		if(event.keyCode==9)
		{
		 document.agscFTFForm.buttonValue.value="chargeCode";
		 document.agscFTFForm.submit();
		}
		}
	  function getCodeDescPress(ev)
		{
		if(event.keyCode==13)
		{
		 document.agscFTFForm.buttonValue.value="chargeCode";
		 document.agscFTFForm.submit();
		}
		}
		
		 function getChgCodeDesc()
  {
     if(event.keyCode==9)
  	{
  	 document.agscFTFForm.buttonValue.value="chargeCode";
  	 document.agscFTFForm.submit();
    }	
  }
  
  function getChgCodeDescPress()
  {
    if(event.keyCode==13)
  	{
  	 document.agscFTFForm.buttonValue.value="chargeCode";
  	 document.agscFTFForm.submit();
    }	
  }
  function deleteCOMM(val1){
  		alert(val1);
  		 document.agscFTFForm.standardId.value=val1;
  		document.agscFTFForm.buttonValue.value="delete";
  	    document.agscFTFForm.submit();
  }
  
  
		    function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.agscFTFForm.charge.value;
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
			params['codeDesc'] = document.agscFTFForm.desc.value;
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
	<body class="whitebackgrnd">
		<html:form action="/agscFTF" name="agscFTFForm"
			type="com.gp.cong.logisoft.struts.ratemangement.form.AgscFTFForm" scope="request">
			<%
			if (session.getAttribute("listAgsCoitem") == null) {
			%>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew">
				<tr class="tableHeadingNew">
					<td>
						Accessorial &amp; Gen Std Chrgs
					</td>
					<td align="right">
						<input type="button" onclick="addfield()" value="Add" id="save"
							class="buttonStyleNew" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table width=100% border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td>
									<%
									} else {
									%>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="tableBorderNew">
										<tr class="tableHeadingNew">
											<td>
												Accessorial &amp; Gen Std Chrgs
											</td>
											<td align="right">
												<input type="button" onclick="addfield()" value="Add"
													id="save" class="buttonStyleNew" />
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<table width="100%" border="0" cellpadding="0"
													cellspacing="0">
													<tr>
														<td></td>
													</tr>


<tr>
	<td>
		<div id="divtablesty1" class="scrolldisplaytable"
			style="width:660px;height: 100px">
			<table width="100%" border="0" cellpadding="3"
				cellspacing="0">

				<tr class="textlabels">
					<td>
						Chrg Code
					</td>
					<td>
						Charge Desc
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
					<td align="center">
						Amt
					</td>

					<%
							}
							if (chargeType.equals("11289")) {
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
					<td align="center">
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
						<input name="charge" id="charge"
							value="<%=chargecode%>"
							onkeydown="getdestPort(this.value)" size="4" />
						<dojo:autoComplete formId="agscFTFForm"
							textboxId="charge"
							action="<%=path%>/actions/getChargeCode.jsp?tabName=AGSC_FTF&from=0" />
						<%-- 		 <html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3" />--%>
					</td>
					<td>
						<%--   		 <html:text property="desc" value="<%=codeDesc%>"  size="12"/>--%>
						<input name="desc" id="desc" size="12"
							value="<%=codeDesc%>" onkeydown="getdestPort1(this.value)" />
						<dojo:autoComplete formId="agscFTFForm"
							textboxId="desc"
							action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=AGSC_FTF" />
					</td>

					<td>
						<html:select property="chargeType"
							styleClass="verysmalldropdownStyle"
							value="<%=chargeType%>" onchange="submit()">
							<html:optionsCollection name="typeList" />
						</html:select>
					</td>
					<td>
						<html:checkbox property="standard" name="ftfStd"></html:checkbox>
					</td>
					<%
					if (chargeType.equals("11287")) {
					%>
					<td>
						<html:text property="amount"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="amount"
							value="<%=amount%>" />
					</td>
					<%
							}
							if (chargeType.equals("11289")) {
								if (defaultRate.equals("E")) {
					%>

					<td>
						<html:text property="amtPerCft"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="amtPerCft"
							value="<%=cft%>" />
					</td>
					<td>
						<html:text property="amtPer100lbs"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="amtPer100lbs"
							value="<%=lbs%>" />
					</td>
					<%
								}
								if (defaultRate.equals("M")) {
					%>
					<td>
						<html:text property="amtPerCbm"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="amtPerCbm"
							value="<%=cbm%>" />
					</td>
					<td>
						<html:text property="amtPer1000kg"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="amtPer1000kg"
							value="<%=amtkg%>" />
					</td>
					<%
					}
					%>
					<td>
						<html:text property="minAmt"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="minAmt"
							value="<%=minAmt%>" />
					</td>
					<%
							}
							if (chargeType.equals("11288")) {
					%>
					<td>
						<html:text property="percentage" maxlength="3"
							size="3" onblur="checkdec(this)"
							styleId="percentage" value="<%=percentage%>" />
						%
					</td>
					<td>
						<html:text property="minAmt"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="minAmt"
							value="<%=minAmt%>" />
					</td>

					<%
							}
							if (chargeType.equals("11291")) {
					%>
					<td>
						<html:text property="minAmt"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="minAmt"
							value="<%=minAmt%>" />
					</td>
					<%
							}
							if (chargeType.equals("11290")) {
					%>
					<td>
						<html:checkbox property="asFrfgted" name="ftfStd"></html:checkbox>
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
						<html:text property="minAmt"
							onkeypress="check(this,5)" onblur="checkdec(this)"
							maxlength="8" size="8" styleId="minAmt"
							value="<%=minAmt%>" />
					</td>
					<%
					}
					%>
					<td>
						<html:text property="txtItemcreatedon"
							styleId="txtitemcreatedon" size="7" readonly="true"
							value="<%=effectiveDate%>" />
					</td>
					<td>
						<div>
							<img src="<%=path%>/img/CalendarIco.gif" alt="cal"
									align="top" id="itemcreatedon"
									onmousedown="insertDateFromCalendar(this.id,0);" />
							</div>
						</td>
						<td align="right">
							<input type="button" onclick="addAGSS()" id="save"
								value="Add To List" class="buttonStyleNew" />
						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>

<%
}
%>
	
<tr>
	<td>
		<table width="100%" border="0" cellpadding="0"
			cellspacing="0">
			<tr>
		</table>
	</td>
</tr>

<tr>
	<td>
		<table width="100%">
			<tr height="5">
			</tr>
			<tr>
				<td>
					<div id="divtablesty1" class="scrolldisplaytable">
						<table width="100%" border="0" cellpadding="0"
							cellspacing="0">
							<%
				int i = 0;
				%>
				<display:table name="<%=agssList%>"
					pagesize="<%=pageSize%>" class="displaytagstyle"
					sort="list" id="agsstable">
					<%
								Date effective_Date = null;
								String type = null;
								String chargeCode = null;
								String tempPath = "";
								String effDate = null;
								if (agssList != null && agssList.size() > 0) {
							FTFStandardCharges ftfStandardCharges2 = (FTFStandardCharges) agssList
									.get(i);
							if (ftfStandardCharges2.getChargeType() != null) {
								type = ftfStandardCharges2.getChargeType()
								.getCodedesc();
							}
							if (ftfStandardCharges2.getChargeCode() != null) {
								chargeCode = ftfStandardCharges2.getChargeCode()
								.getCode();
								
							}
							if (ftfStandardCharges2.getEffectiveDate() != null) {
								effDate = dateFormat.format(ftfStandardCharges2
								.getEffectiveDate());
							}

							if (ftfStandardCharges2.getPercentage() != null) {
								percentage = per.format(ftfStandardCharges2
								.getPercentage())
								+ "%";
							}

							if (ftfStandardCharges2.getAmtPer1000Kg() != null) {
								amtkg = df.format(ftfStandardCharges2
								.getAmtPer1000Kg());
							}
							if (ftfStandardCharges2.getInsuranceRate() != null) {
								insuranceRate = df.format(ftfStandardCharges2
								.getInsuranceRate());
							}

							if (ftfStandardCharges2.getInsuranceAmt() != null) {
								insuranceAmt = df.format(ftfStandardCharges2
								.getInsuranceAmt());
							}

							if (ftfStandardCharges2.getAmount() != null) {
								amount = df.format(ftfStandardCharges2.getAmount());
							}

							if (ftfStandardCharges2.getMinAmt() != null) {
								minAmt = df.format(ftfStandardCharges2.getMinAmt());
							}
							if (ftfStandardCharges2.getAmtPerCft() != null) {
								cft = df.format(ftfStandardCharges2.getAmtPerCft());
							}
							if (ftfStandardCharges2.getAmtPer100lbs() != null) {
								lbs = df.format(ftfStandardCharges2
								.getAmtPer100lbs());
							}
							if (ftfStandardCharges2.getAmtPerCbm() != null) {
								cbm = df.format(ftfStandardCharges2.getAmtPerCbm());
							}

								}
								String iStr = String.valueOf(i);
								tempPath = editPath + "?ind=" + iStr;
					%>
					<display:setProperty
						name="paging.banner.some_items_found">
						<span class="pagebanner"> <font color="blue">{0}</font>
							Item Details Displayed,For more Items click on Page
							Numbers. <br> </span>
					</display:setProperty>
					<display:setProperty
						name="paging.banner.one_item_found">
						<span class="pagebanner"> One {0} displayed.
							Page Number </span>
					</display:setProperty>
					<display:setProperty
						name="paging.banner.all_items_found">
						<span class="pagebanner"> {0} {1} Displayed,
							Page Number </span>
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

					<display:column title="Charge Code">
						<a href="<%=tempPath%>"><%=chargeCode%>
						</a>
					</display:column>
					<display:column title="Charge Type">
						<%=type%>
					</display:column>
					<display:column property="standard" title="Standard"></display:column>
					<display:column property="asFrfgted"
						title="As Freighted"></display:column>
					<display:column title="Insurance Rate">	<%=insuranceRate%></display:column>
					<display:column title="Insurance Amount"><%=insuranceAmt%></display:column>
					<display:column title="AmtPerCft"><%=cft%></display:column>
					<display:column title="AmtPer100lbs"><%=lbs%></display:column>
					<display:column title="AmtPer1000kg"><%=amtkg%></display:column>
					<display:column title="AmtPerCbm"><%=cbm%></display:column>
					<display:column title="Percentage"><%=percentage%></display:column>
					<display:column title="MinAmt"><%=minAmt%></display:column>
					<display:column title="Amt"><%=amount%></display:column>
					<display:column title="Effective Date"><%=effDate%></display:column>
					<display:column>
						<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
						<img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteCOMM('<%=chargeCode%>')" />
						</span>
				</display:column>

					<%
					i++;
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

									<html:hidden property="buttonValue" styleId="buttonValue" />
									<html:hidden property="index" />
									<html:hidden property="standardId" />
								</td>
							</tr>
						</table>
						</div>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

