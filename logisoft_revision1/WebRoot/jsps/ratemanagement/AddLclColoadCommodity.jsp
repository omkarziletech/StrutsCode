<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.LCLColoadCommodityCharges,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.LCLColoadStandardCharges,org.apache.struts.util.LabelValueBean"%>
<jsp:directive.page import="java.text.DecimalFormat"/>	
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List applyGeneralStandardList=new ArrayList();
List typeList=new ArrayList();
List csssList=null;
AirRatesBean airRatesBean=new AirRatesBean();
LCLColoadCommodityCharges lCLColoadCommodityCharges=new LCLColoadCommodityCharges();
airRatesBean.setStandard("off");
airRatesBean.setAsFrfgted("off");
airRatesBean.setExclude("off");
request.setAttribute("airRatesBean",airRatesBean);
DBUtil dbUtil=new DBUtil();
String msg="";
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
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("0.000");
effectiveDate=dateFormat.format(new Date());
if(session.getAttribute("addlclcoloadcommodity")!=null)
{
lCLColoadCommodityCharges=(LCLColoadCommodityCharges)session.getAttribute("addlclcoloadcommodity");
if(lCLColoadCommodityCharges.getChargeCode()!=null)
{
chargecode=lCLColoadCommodityCharges.getChargeCode().getCode();
codeDesc=lCLColoadCommodityCharges.getChargeCode().getCodedesc();
}
if(lCLColoadCommodityCharges.getChargeType()!=null&&lCLColoadCommodityCharges.getChargeType().getId()!=null)
{
chargeType=lCLColoadCommodityCharges.getChargeType().getId().toString();

}
if(lCLColoadCommodityCharges.getAmount()!=null)
{
amount=df.format(lCLColoadCommodityCharges.getAmount());
}
if(lCLColoadCommodityCharges.getAmtPerCft()!=null)
{
cft=df.format(lCLColoadCommodityCharges.getAmtPerCft());
}
if(lCLColoadCommodityCharges.getAmtPer100lbs()!=null)
{
lbs=df.format(lCLColoadCommodityCharges.getAmtPer100lbs());
}
if(lCLColoadCommodityCharges.getAmtPerCbm()!=null)
{
cbm=df.format(lCLColoadCommodityCharges.getAmtPerCbm());
}
if(lCLColoadCommodityCharges.getMinAmt()!=null)
{
minAmt=df.format(lCLColoadCommodityCharges.getMinAmt());
}
if(lCLColoadCommodityCharges.getAmtPer1000kg()!=null)
{
amtkg=df.format(lCLColoadCommodityCharges.getAmtPer1000kg());
}
if(lCLColoadCommodityCharges.getPercentage()!=null)
{
percentage=per.format(lCLColoadCommodityCharges.getPercentage());
}
if(lCLColoadCommodityCharges.getInsuranceAmt()!=null)
{
insuranceAmt=df.format(lCLColoadCommodityCharges.getInsuranceAmt());
}
if(lCLColoadCommodityCharges.getInsuranceRate()!=null)
{
insuranceRate=df.format(lCLColoadCommodityCharges.getInsuranceRate());
}
if(lCLColoadCommodityCharges.getEffectiveDate()!=null)
{
effectiveDate=dateFormat.format(lCLColoadCommodityCharges.getEffectiveDate());
}
}

if(session.getAttribute("applylclcoloadcharges")!=null)
{
	applyGeneralStandardList=(List)session.getAttribute("applylclcoloadcharges");
	
}

if(session.getAttribute("lclcoloadcssList") != null)
{
	csssList = (List) session.getAttribute("lclcoloadcssList");
}

if(typeList != null)
{
	typeList=dbUtil.getTypeListForcoloadCsss(new Integer(34),"yes","Select Rate type");
	
	request.setAttribute("typeList",typeList);
}

//-----------------------------------


modify = (String) session.getAttribute("modifyforlclcoloadRates");
if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}

request.setAttribute("typeList",typeList);
String editPath=path+"/addLclColoadCommodity.do";

String defaultRate="";
if(session.getAttribute("lcldefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("lcldefaultRate");
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
		<title>JSP for AddLclColoadCommodityForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
				<%@include file="../includes/resources.jsp"%>

			<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		<script language="javascript" type="text/javascript">
			
	function change1(val){
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
   function submit(){
		document.addLclColoadCommodityForm.buttonValue.value="chargeType";
		document.addLclColoadCommodityForm.submit();
		}
   function addlcco(){
			if(document.addLclColoadCommodityForm.exclude.checked==false && document.addLclColoadCommodityForm.chargeType.value=="0")
			{
				alert("Please select Exclude  or Charge Type");
				return;
			}
		
			if(document.addLclColoadCommodityForm.charge.value=="")
			{
				alert("Please select Charge code");
				return;
			}
	      document.addLclColoadCommodityForm.buttonValue.value="add";
	      document.addLclColoadCommodityForm.submit();
   }
  function addfield() {
		document.addLclColoadCommodityForm.buttonValue.value="addItem";
  		document.addLclColoadCommodityForm.submit();
	}
		
  function getChgCode() {
  	if(event.keyCode==9)
  	{
  	 document.addLclColoadCommodityForm.desc.value="";
  	 document.addLclColoadCommodityForm.buttonValue.value="popupsearch";
  	 document.addLclColoadCommodityForm.submit();
    }		
  }
  function getChgCodePress(){
   if(event.keyCode==13)
  	{
  	 document.addLclColoadCommodityForm.desc.value="";
  	 document.addLclColoadCommodityForm.buttonValue.value="popupsearch";
  	 document.addLclColoadCommodityForm.submit();
    }	
  }
  function getChgCodeDesc() {
     if(event.keyCode==9)
  	{
  	 document.addLclColoadCommodityForm.charge.value="";
  	 document.addLclColoadCommodityForm.buttonValue.value="popupsearch";
  	 document.addLclColoadCommodityForm.submit();
    }	
  }
  
  function getChgCodeDescPress(){
    if(event.keyCode==13)
  	{
  	  document.addLclColoadCommodityForm.charge.value="";
  	 document.addLclColoadCommodityForm.buttonValue.value="popupsearch";
  	 document.addLclColoadCommodityForm.submit();
    }	
  }
  function deleteCOMM(val1){
  	 document.addLclColoadCommodityForm.commodityId.value=val1;
  	 document.addLclColoadCommodityForm.buttonValue.value="delete";
  	 document.addLclColoadCommodityForm.submit();
  }
  
  		    function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.addLclColoadCommodityForm.charge.value;
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
			params['codeDesc'] = document.addLclColoadCommodityForm.desc.value;
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

	if(modify!=null && modify.equals("3")){

	 %>
	
		<body class="whitebackgrnd" onLoad="">
	<%}
	
	else{ %>
		<body class="whitebackgrnd">
	<%} %>
		
	<html:form action="/addLclColoadCommodity" name="addLclColoadCommodityForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AddLclColoadCommodityForm" scope="request">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
        
        <%if(session.getAttribute("listLclCoitem")==null){ %>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	        <tr class="tableHeadingNew"><td>Commodity Specfic Std Charges</td>
	        	<td align="right">
				   <input type="button" value="Add" onclick="addfield()" id="add" class="buttonStyleNew" />
				</td>
			</tr>
		<%} else{ %>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			 <tr class="tableHeadingNew">Commodity Specfic Std Charges</tr>
				<tr class="textlabels">
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
						Perc
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
<%--					<html:text property="charge" value="<%=chargecode%>" styleClass="varysizeareahighlightgrey" size="3" />--%>
                    	<input name="charge" id="charge" size="4" value="<%=chargecode%>" onkeydown="getdestPort(this.value)"/>
                    	<dojo:autoComplete formId="addLclColoadCommodityForm" textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=FCL_AIR_BUY&from=0"/>
					</td>
					<td>
<%--						<html:text property="desc" value="<%=codeDesc%>"  size="12" />--%>
                   		 <input name="desc" id="desc" size="12" value="<%=codeDesc%>" onkeydown="getdestPort1(this.value)"/>
                    	<dojo:autoComplete formId="addLclColoadCommodityForm" textboxId="desc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=LCL_COMMODITY"/>
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
						<html:text property="amount" maxlength="8" size="8" onkeypress="check(this,5)" onblur="checkdec(this)" styleId="amount" value="<%=amount%>" />
					</td>
					<%
						}
						if (chargeType.equals("11289")) {
					%>
					<%if(defaultRate.equals("E"))
					{ %>
					<td>
						<html:text property="amtPerCft" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"	 styleId="amtPerCft" value="<%=cft%>" />
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
						<html:text property="percentage" maxlength="3" size="3" onblur="checkdec(this)"
							 value="<%=percentage%>" />%
					</td>
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
					    <input type="button" value="Add To List" onclick="addlcco()" id="add" class="buttonStyleNew" style="80px"/>
						
					</td>
				</tr>
				
			</table>
			<%} %>
			
			<tr><td colspan="2">
			<table width=100% border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<div id="divtablesty1"
							class="scrolldisplaytable">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
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
							String cheDate = "";
							String perc = "",chrCode="";
				if (csssList != null && csssList.size() > 0)
				 {
						
						LCLColoadCommodityCharges lclComm = (LCLColoadCommodityCharges) csssList.get(k);
						if (lclComm.getChargeCode() != null) {
							chargeCode = lclComm.getChargeCode().getCodedesc();
							chrCode = lclComm.getChargeCode().getCode();
						}
						if (lclComm.getChargeType() != null&&lclComm.getChargeType().getCodedesc()!=null) {
													type = lclComm.getChargeType().getCodedesc();
						}
						
						if (lclComm.getEffectiveDate() != null) {
							cheDate = dateFormat.format(lclComm.getEffectiveDate());
						}
						if(lclComm.getPercentage()!=null)
							{
							perc=per.format(lclComm.getPercentage()) + "%";
							}
						if(lclComm.getAmtPer1000kg()!=null)
							{
								amtkg=df.format(lclComm.getAmtPer1000kg());
							}
						if(lclComm.getInsuranceRate()!=null)
							{
							   insuranceRate=df.format(lclComm.getInsuranceRate());
							}	
				
						if(lclComm.getInsuranceAmt()!=null)
							{
								insuranceAmt=df.format(lclComm.getInsuranceAmt());
							}	
				
						if(lclComm.getAmount()!=null)
							{
								amount=df.format(lclComm.getAmount());
							}	
				
						if(lclComm.getMinAmt()!=null)
							{
								minAmt=df.format(lclComm.getMinAmt());
							}	
						if(lclComm.getAmtPerCft()!=null)
							{
								cft=df.format(lclComm.getAmtPerCft());
							}	
						if(lclComm.getAmtPer100lbs()!=null)
							{
								lbs=df.format(lclComm.getAmtPer100lbs());
							}	
						if(lclComm.getAmtPerCbm()!=null)
							{
								cbm=df.format(lclComm.getAmtPerCbm());
							}	
							String ex="";
							if(lclComm.getExclude()!=null){
							ex=lclComm.getExclude();
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
									<display:column property="exclude" title="EX"></display:column>
									<display:column property="asFrfgted" title="As Freighted"></display:column>
									<display:column title="Insurance Rate"><%=insuranceRate%></display:column>
									<display:column title="Insurance Amount"><%=insuranceAmt%></display:column>
									<%if(defaultRate.equals("E"))
									{ %>
									<display:column title="AmtPerCft" ><%=cft%></display:column>
									<display:column title="AmtPer100lbs" ><%=lbs%></display:column>
									<%}
									else if(defaultRate.equals("M"))
									{ %>
									<display:column  title="AmtPerCbm"><%=cbm%></display:column>
									<display:column  title="Amt/1000kg" ><%=amtkg%></display:column>
									<%} %>
									<display:column  title="Percentage" ><%=perc%></display:column>

									<display:column title="MinAmt"><%=minAmt%></display:column>

									<display:column title="Amt"><%=amount%></display:column>

									<display:column title="Effective Date">
										<%=cheDate%>
									</display:column>
									<display:column title="Action">
										<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
										<img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteCOMM('<%=chrCode%>')" />
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
		</td></tr>
</table>
</table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
			<html:hidden property="index" />
			<html:hidden property="commodityId" />
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


