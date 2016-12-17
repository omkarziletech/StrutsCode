<%@ page language="java"  import="java.text.DateFormat,java.text.DecimalFormat, java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,java.util.List,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,com.gp.cong.logisoft.domain.FclBuyCost"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<jsp:useBean id="fclrecordform" class="com.gp.cong.logisoft.struts.ratemangement.form.AddFCLRecordsForm" scope="request"/>   
<%
	String path = request.getContextPath();
	String costcode=""; 
	FclBuy fclBuy=new FclBuy();
	FclBuyCost fclBuycost=new FclBuyCost();
	String terminalNumber="";
	String terminalName="";
	String destSheduleNumber="";
	String destAirportname="";
	String comCode="";
	String comDesc="";
	String message="";
	String msg="";
	String enddate="";
	String startDate="";
	String modify="";
	String sslineno="";
	String contact="";
	String costdesc="";
	String costtype="";
	DecimalFormat df=new DecimalFormat("0.00");
	String process=null;
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	String sslinename="";
	String orgRegion="";
	String destRegion="";
	List costcodelist=new ArrayList();;
	List costtypelist=new ArrayList();;
	List li=new ArrayList();
	List unittypelist=new ArrayList();
	//List unittypelist=new ArrayList();;
	List recordsList=new ArrayList();
	DBUtil dbUtil=new DBUtil();
	String valid="";
	List airFreightList=new ArrayList();
	List l2=new ArrayList();
	List airTypelist=new ArrayList();
	String effectiveDate= dateFormat.format(new Date());
%>
<%

	if(session.getAttribute("costCodeList")==null){
		valid="Please Select At least One Cost Code! Else select previous";
	}
	if(request.getAttribute("start")!=null){
	startDate=(String)request.getAttribute("start");
	}
	if(request.getAttribute("end")!=null){
	enddate=(String)request.getAttribute("end");
	}
	if(session.getAttribute("editrecords")!=null){
	process=(String)session.getAttribute("editrecords");
	}
	fclrecordform.setStandard("off");
	if(session.getAttribute("addfclrecords")!=null){
	session.removeAttribute("fclmessage");
	fclBuy=(FclBuy)session.getAttribute("addfclrecords");
	if(fclBuy.getOriginTerminal()!=null){
	terminalNumber=fclBuy.getOriginTerminal().getUnLocationCode();
	terminalName=fclBuy.getOriginTerminal().getUnLocationName();
	}
	if(fclBuy.getDestinationPort()!=null){
	destSheduleNumber=fclBuy.getDestinationPort().getUnLocationCode();
	destAirportname=fclBuy.getDestinationPort().getUnLocationName();
	}
	if(fclBuy.getComNum()!=null){
	comCode=fclBuy.getComNum().getCode();
	comDesc=fclBuy.getComNum().getCodedesc();
	}
	if(fclBuy.getSslineNo()!=null){
	sslineno=fclBuy.getSslineNo().getAccountno();
	sslinename=fclBuy.getSslineNo().getAccountName();
	//contact=fclBuy.getSslineNo().getFclContactNumber();
	}
	if(fclBuy.getContract()!=null){
	contact=fclBuy.getContract().toString();
	}
	if(fclBuy.getStartDate()!=null){
	startDate=dateFormat.format(fclBuy.getStartDate());
	}
	if(fclBuy.getEndDate()!=null){
	enddate=dateFormat.format(fclBuy.getEndDate());
	}
	if(fclBuy.getOriginalRegion()!=null)
	{
	orgRegion=fclBuy.getOriginalRegion();
	}
	if(fclBuy.getDestinationRegion()!=null)
	{
	destRegion=fclBuy.getDestinationRegion();
	}
	//confirm="records";
	}
	if(session.getAttribute("con")!=null){
	fclBuy=(FclBuy)session.getAttribute("con");
	contact=fclBuy.getContract();
	if(fclBuy.getStartDate()!=null){
	startDate=dateFormat.format(fclBuy.getStartDate());
	}
	if(fclBuy.getEndDate()!=null){
	enddate=dateFormat.format(fclBuy.getEndDate());
	}
	}
	if(session.getAttribute("fclfrightrecords")!=null){
	airFreightList=(List)session.getAttribute("fclfrightrecords");
	}
	List unitTypeList=new ArrayList();
	if(session.getAttribute("fclrecords")!=null){
	unitTypeList=(List)session.getAttribute("fclrecords");
	}
	if(costcodelist != null){
		costcodelist=dbUtil.getGenericCodeCostList(new Integer(36),"no","Select Cost Code");
		request.setAttribute("costcodelist",costcodelist);
		 
	}
	if(costtypelist != null){
		costtypelist=dbUtil.getGenericCodetypeList(new Integer(37),"yes","Select Cost type");
		request.setAttribute("costtypelist",costtypelist);
		 
	}
	String codesc="";
	if(session.getAttribute("costcode")!=null){
		fclBuycost=(FclBuyCost)session.getAttribute("costcode");
		if(fclBuycost!=null && fclBuycost.getCostId()!=null){
		costdesc=fclBuycost.getCostId().getCodedesc();
		}
	if(fclBuycost.getContType()!=null){
		costtype=fclBuycost.getContType().getId().toString();
		codesc=fclBuycost.getContType().getCodedesc();
		}
	 }
	request.setAttribute("defaultcurrency",dbUtil.getGenericFCL(new Integer(32),"yes"));
	if(session.getAttribute("fclrecords")!=null){
		recordsList=(List)session.getAttribute("fclrecords");
		for(int i=0;i<recordsList.size();i++){
   				FclBuyCostTypeRates	frt=(FclBuyCostTypeRates)recordsList.get(i);
   				if(costdesc!=null && frt!=null && costdesc.equals(frt.getCostCode())){
   				li.add(frt);
   				}
   				}
	 	   }
	if(unittypelist != null){
		unittypelist=dbUtil.getUnitFCLUnitypeTest(new Integer(38),"yes","Select Unit code",li,null);
		request.setAttribute("unittypelist",unittypelist);
		}
	if(session.getAttribute("fclfrightrecords")!=null){
		airTypelist=(List)session.getAttribute("fclfrightrecords");
		for(int i=0;i<airTypelist.size();i++){
   				FclBuyAirFreightCharges	frt=(FclBuyAirFreightCharges)airTypelist.get(i);
   				if(costdesc!=null && frt!=null && costdesc.equals(frt.getCostCode())){
				l2.add(frt);
   				}
   				}
		    }
	if(airTypelist != null){
		airTypelist=dbUtil.getWeightFCLList(new Integer(31),"yes","Select Unit code",l2);
		request.setAttribute("wightlist",airTypelist);
	}
	if(session.getAttribute("usermessage")!=null){
			message=(String)session.getAttribute("usermessage");
		}
	if(request.getAttribute("message")!=null){
		msg=(String)request.getAttribute("message");
		}	
	if(session.getAttribute("view")!=null){
		modify=(String)session.getAttribute("view");
		}
	if(request.getAttribute("editflaterate")!=null && !request.getAttribute("editflaterate").equals("")){
			if(request.getAttribute("buy")!=null){
			   // buy=(String)request.getAttribute("buy");
			}
			// ii = Integer.parseInt((String)request.getAttribute("editflaterate"));
		}	
%>
<html>
	<head>
	<title>JSP for AddLclColoadCommodityForm form</title>
	<%@include file="../includes/baseResources.jsp" %>
	<script language="javascript" type="text/javascript">
				
       	function submit1()
		{
			document.addFCLRecordsForm.buttonValue.value="costcode";
			document.addFCLRecordsForm.costtype.value="0";
			document.addFCLRecordsForm.submit();
		}
		function costtypechange()
		{
		
			if(document.addFCLRecordsForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else{
				document.addFCLRecordsForm.buttonValue.value="costType";
				document.addFCLRecordsForm.submit();
			}
		}
		
		function addForm(val1)
		{
			//alert(val1);
			if(document.addFCLRecordsForm.costcode.value!=null&&document.addFCLRecordsForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else if(document.addFCLRecordsForm.costtype.value!=null&&document.addFCLRecordsForm.costtype.value=="0")
			{
				alert("Please select Cost Type !");
			}
			else if(val1!=null&&val1==11300)
			{
				 if(document.addFCLRecordsForm.unittype.value!=null&&document.addFCLRecordsForm.unittype.value=="0")
					{
					alert("Please select Unit Type !");
					}
					else{
					document.addFCLRecordsForm.buttonValue.value="add";
					document.addFCLRecordsForm.submit();
					}
			}
			else if(val1!=null&&val1==11306)
			{
					if(document.addFCLRecordsForm.range.value!=null&&document.addFCLRecordsForm.range.value=="0")
					{
						alert("Please select Wight Range !");
					}else{
					document.addFCLRecordsForm.buttonValue.value="add";
					document.addFCLRecordsForm.submit();
					}
			}
			else{
			
				document.addFCLRecordsForm.buttonValue.value="add";
				document.addFCLRecordsForm.submit();
			}
		}
		function delete1()
		{
			document.addFCLRecordsForm.buttonValue.value="delete";
			document.addFCLRecordsForm.submit();
		}
		function saveForm1(val,val2)
		{
		
			
			if(val!=null && val!="save")
			{
			document.addFCLRecordsForm.buttonValue.value="edit";
			}
			else{
				document.addFCLRecordsForm.buttonValue.value="save";
			}
			if(val2!=""){
			alert(val2);
			
			}
			else{
			
			document.addFCLRecordsForm.submit();
			}
		}
		function cancelForm(val1,val2)
		{
			
			if(val1!="edit"){
				var1=confirm("Do u want to save this record");
				if(var1){
				saveForm1(val1,val2);
				}
				else{
				document.addFCLRecordsForm.buttonValue.value="cancel";
				document.addFCLRecordsForm.submit();
				}
			}else{
			document.addFCLRecordsForm.buttonValue.value="cancel";
			document.addFCLRecordsForm.submit();
			}
		}
		function noteForm()
		{
			document.addFCLRecordsForm.buttonValue.value="note";
			document.addFCLRecordsForm.submit();
		}
		function nonTest(val1){
			
			document.addFCLRecordsForm.buttonValue.value="index";
			document.addFCLRecordsForm.index.value=val1;
			document.addFCLRecordsForm.buy.value="buy";
			document.addFCLRecordsForm.submit();
			mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+val1+"&buy=buy","","width=800,height=300");
          mywindow.moveTo(100,180);		
		}
		function comTest(val1){
			
			document.addFCLRecordsForm.buttonValue.value="index";
			document.addFCLRecordsForm.index.value=val1;
			document.addFCLRecordsForm.submit();
			mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+val1,"","width=800,height=200");
       		 mywindow.moveTo(200,180);		
		}
		function fcopy(){
			
			document.addFCLRecordsForm.buttonValue.value="copy";
			document.addFCLRecordsForm.submit();
		}
		
		</script>
		<style type="text/css">
		.disableInput {
           border:0;
           readonly:true;
           disabled:disabled;
           class:bodybackgrnd;
        }
		</style>
</head>
<body class="whitebackgrnd"  onLoad="disabled('<%=modify%>','<%=message%>','<%=msg%>','<%=process%>')">
<html:form action="/addFCLRecords" scope="request">
<table>
<tr>
<td>
<font color="blue" size="4"><%=msg%></font>
<table  cellpadding="1" cellspacing="0" width="100%" border="0" class="tableBorderNew" id="addFCLCostCodes">
	<tr class="tableHeadingNew"><td colspan="2"><b>Add FCL CostCodes  Rates</b></td>
	<td colspan="4" align="right"><input type="button" onclick="saveForm1('<%=process%>','<%=valid%>')" id="save" value="Save" class="buttonStyleNew"/>
	 	   <input type="button" onclick="delete1()"  value="Delete " class="buttonStyleNew" id="delete"/>
	 	   <input type="button" onclick="cancelForm('<%=process%>','<%=valid%>')"  value="Go Back" class="buttonStyleNew" id="cancel"/>
	 	   <input type="button" onclick="noteForm()"  value="Note" class="buttonStyleNew" id="note"/></td>
	 </tr>
	
	<tr class="textlabels">
  <td>OriginTerminal</td>
  <td><html:text property="orgTerminal" value="<%= terminalNumber%>" readonly="true" styleClass="disableInput bodybackgrnd"></html:text>
    </td>
  <td > OriginTerminal Name </td>
  <td ><html:text property="orgName" value="<%=terminalName %>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
  <td >Dest Port</td>
  <td ><html:text property="destnum" value="<%= destSheduleNumber%>" readonly="true" styleClass="disableInput bodybackgrnd" />
  </td>
  </tr >
  
   <tr class="textlabels">
   <td class="textlabels">Dest Port Name </td>
   <td><html:text property="destname" value="<%= destAirportname%>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
   <td class="textlabels">Com Code </td>
   <td><html:text property="comcode" value="<%= comCode%>" readonly="true" styleClass="disableInput bodybackgrnd"/>
    </td>
  <td class="textlabels">Com Description</td>
  <td><html:text property="comdesc" value="<%=comDesc %>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
 </tr>
 <tr>
  <td class="textlabels">SS Line Code</td>
  <td ><html:text property="sslineno" value="<%= sslineno%>" readonly="true" styleClass="disableInput bodybackgrnd"/>
   </td>
   <td class="textlabels">SS Line Name</td>
  <td ><html:text property="sslinename" value="<%=sslinename %>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
 <td class="style2">Origin Region</td>
 <td><html:text property="orgRegion" value="<%=orgRegion%>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
 </tr>
	<tr class="textlabels">
	<td class="style2">Destination Region</td>
  <td><html:text property="destRegion" value="<%=destRegion%>" readonly="true" styleClass="disableInput bodybackgrnd" /></td>
	
		<td>StartDate</td>
	<td><html:text property="startDate" size="12" styleId="txtitemcreatedon" value="<%=startDate%>" styleClass="disableInput bodybackgrnd"/>
	<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" />
	</td>
		 <td  >EndDate</td>
		  <td><html:text property="endDate" styleId="txtEitemcreatedon" size="12" value="<%=enddate%>" styleClass="disableInput bodybackgrnd"/>
			<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top" id="Eitemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
	</tr>
	<tr class="textlabels">
	<td>Contract Name</td>
    <td><html:text property="contact"  maxlength="40" size="12"  value="<%=contact%>" readonly="true" styleClass="disableInput bodybackgrnd"/> </td>
		<td>Cost Code</td>
		 <td ><html:select property="costcode"  onchange="submit1()" style="width:120px">
		   <html:optionsCollection name="costcodelist"/>    
		  </html:select></td>
		  <td>Cost description</td>
		    <td ><html:text property="costdesc" value="<%=costdesc %>" size="12" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
	</tr>
    <tr>
        <td class="textlabels">Cost Type </td>
        <td colspan="5" ><html:select property="costtype"  onchange="costtypechange()">
		<html:optionsCollection name="costtypelist"/>  </html:select></td>
    </tr>
</table>
</td>
</tr>
<br>
<tr>
<td>
<%if(codesc!=null && !codesc.equals("")){ %>
<table class="tableBorderNew" width="100%">
	<tr class="tableHeadingNew" height="90%"><b>Add Rates for <%=codesc%> </b> </tr>
<tr  class="textlabels">
<%

if(codesc!=null && codesc.trim().equalsIgnoreCase("Per Container Size"))
{
%>
	<td >Unit Type</td>
	<td  ><html:select property="unittype" styleClass="shortselectstyle1">
  <html:optionsCollection name="unittypelist"/>  </html:select> </td>
	<td >Amount</td>
	 <td ><html:text property="amount"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   value=""/></td>
	<td >Markup</td>
	  <td ><html:text property="markup"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"     value=""/></td>
	<td >Standard</td>
	  <td ><html:checkbox property="standard" name="fclrecordform" ></html:checkbox></td>
<%}
else if(codesc!=null && codesc.trim().equalsIgnoreCase("Per CBM")||codesc.trim().equalsIgnoreCase("per LBS")||codesc.trim().equalsIgnoreCase("Per 1000KG"))
{
%>
  <td >Retail</td>
  <td><html:text property="pcretail"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>
  <td >CTC</td>
  <td><html:text property="pcctc"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   value="" /></td>
  <td >FTF</td>
  <td><html:text property="pcftf" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>
  <td >Minimun</td>
  <td><html:text property="pcminimun" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"      value=""/></td>
  <%}
   else if(codesc!=null && codesc.trim().equalsIgnoreCase("PERCENT OFR")|| codesc.trim().equalsIgnoreCase("Per 2000 lbs")||codesc.trim().equalsIgnoreCase("Per Dock Receipts")||codesc.trim().equalsIgnoreCase("Per Cubic Foot")||codesc.trim().equalsIgnoreCase("PER BL CHARGES") ||codesc.trim().equalsIgnoreCase("Flat Rate Per Container")||codesc.trim().equalsIgnoreCase("Per Doc Receipts"))
 {%>
  <td width="70">Retail</td>
    <td><html:text property="pcretail"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   styleClass="verysmalldropdownStyle"  value=""/></td>
 <%}
 else if(codesc!=null && codesc.trim().equalsIgnoreCase("Air Freight Costs"))
{%>
 <td width="68"><span class="textlabels">Range </span></td>
 <td><html:select property="range" styleClass="verysmalldropdownStyle"  onchange="costtypechange()">
	<html:optionsCollection name="wightlist"/>    
  	</html:select></td>
 <td width="121"><span class="textlabels">Air Freight Amount </span></td>
   <td width="105"><html:text property="afamount"   onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>
<%} %>
 <td>EffectiveDate</td>
      <td>
			<html:text property="effectiveDate" styleId="txtEFDitemcreatedon" styleClass="verysmalldropdownStyle"
				size="7" readonly="true" value="<%=effectiveDate%>" />
			<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
			id="EFDitemcreatedon"	onmousedown="insertDateFromCalendar(this.id,0);" /></td>
	Currency
</td>
      <td colspan="4">&nbsp;<html:select property="currency" value="USD"> 
      <html:optionsCollection name="defaultcurrency" />  </html:select></td>
       <td><input type="button" value="Add To List" class="buttonStyleNew" onclick="addForm('<%=costtype%>')" id="add"/></td>
</tr>
<%}%>
</table>
<br>
<%if(airFreightList!=null && !airFreightList.isEmpty()){ %>
<table class="tableBorderNew" width="100%">
		<tr class="tableHeadingNew" >FCL Air Freight Rates </tr>
		<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        %>
        <display:table name="<%=airFreightList%>" pagesize="<%=pageSize%>"  class="displaytagstyle"  sort="list" id="includedtable" style="width:100%"> 
			<% String  tempPath1="";
				
				String costType="";
				String wightRange="";
				String currencyAir="";
				//String costType="";
				String rateAmount="";
			String iStr=String.valueOf(i);
			tempPath1=path+"/jsps/ratemanagement/AddFCLRecords.jsp";
			if(airFreightList!=null && airFreightList.size()>0)
			{
			FclBuyAirFreightCharges fclBuyAirFreight=(FclBuyAirFreightCharges)airFreightList.get(i);
			 if(fclBuyAirFreight.getWieghtRange()!=null)
					 {
					 wightRange=fclBuyAirFreight.getWieghtRange().getCodedesc();
					 }
					 if(fclBuyAirFreight.getRatAmount()!=null)
					 {
					     rateAmount	=df.format(fclBuyAirFreight.getRatAmount());			
					 }
					if(fclBuyAirFreight.getCurrency()!=null){
					currencyAir=fclBuyAirFreight.getCurrency().getCodedesc();
					}
					 if(fclBuyAirFreight.getCostCode()!=null)
					 {
					 costcode=fclBuyAirFreight.getCostCode();
					 }
					 if(fclBuyAirFreight.getCostType()!=null)
					 {
					 costType=fclBuyAirFreight.getCostType();
					 }
					 if(fclBuyAirFreight.getCostId()!=null)
					 {
					
					 }
			}
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
  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost Code" >
  			<a href="<%=tempPath1%>" onclick="comTest('<%=iStr%>')">
  			<%=costcode%></a></display:column>
  			<display:column  title="Cost type" ><%=costType%></display:column>
  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wieght Range ">
  			<%=wightRange%>
  			</display:column>
  			<display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Air Fright Amount" ><%=rateAmount%></display:column>
  			<display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Currency" ><%=currencyAir%></display:column>
  			<%
  			 i++;
  			%> 
		</display:table>
        </table></div>  
    	</td> 
  		 </tr>  
		</table>
		<br>
		<%}if(unitTypeList!=null && unitTypeList.size()>0){ %>
		<table class="tableBorderNew" width="100%">
		<tr class="tableHeadingNew">FCL Cost Code Rates</tr>
		<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable" style="width:780px">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int j=0;
        %>
        <display:table name="<%=unitTypeList%>" pagesize="20" class="displaytagstyle"  sort="list" id="includedcomtable" style="width:100%"> 
<% String  tempPath1="";
	String unitycode="";
	String costType="";
	String ratAmount="";
	String ctcAmt="";
	String ftfAmt="";
	String minimumAmt="";
	String activeAmt="";
	String markUp="";
	String standard="";
	String curreny="";
	String effDate="";
	//String costType="";
	String iStr=String.valueOf(j);
				tempPath1=path+"/jsps/ratemanagement/AddFCLRecords.jsp";
			if(unitTypeList!=null && unitTypeList.size()>0)
			{
			FclBuyCostTypeRates fclBuyCostType=(FclBuyCostTypeRates)unitTypeList.get(j);
		if(fclBuyCostType.getEffectiveDate()!=null){
		effDate=dateFormat.format(fclBuyCostType.getEffectiveDate());
			}
			
			if(fclBuyCostType.getUnitType()!=null)
					 {
					 unitycode=fclBuyCostType.getUnitType().getCodedesc();
					 }
					 if(fclBuyCostType.getRatAmount()!=null)
					 {
					 if(fclBuyCostType.getRatAmount()==0.0){
					 }else{
					 ratAmount=df.format(fclBuyCostType.getRatAmount());
					 }
					}
					 if(fclBuyCostType.getFtfAmt()!=null)
					 {
						 if(fclBuyCostType.getFtfAmt()==0.0){
						 }
						 else{
						  ftfAmt=df.format(fclBuyCostType.getFtfAmt());
						 }
						}
					 if(fclBuyCostType.getCtcAmt()!=null)
					 {
							 if(fclBuyCostType.getCtcAmt()==0.0){}
							 else{
							 ctcAmt=df.format(fclBuyCostType.getCtcAmt());
							 }
					 }
					 if(fclBuyCostType.getMinimumAmt()!=null)
					 {
						 if(fclBuyCostType.getMinimumAmt()==0.0){}
							 else{
							minimumAmt=df.format(fclBuyCostType.getMinimumAmt());
					
							 }
					 
					 }
					
					 if(fclBuyCostType.getActiveAmt()!=null)
					 {
					 if(fclBuyCostType.getActiveAmt()==0.0){}
					 else{
					 activeAmt=df.format(fclBuyCostType.getActiveAmt());
					 }
					 }
					 if(fclBuyCostType.getMarkup()!=null)
					 {
					 if(fclBuyCostType.getMarkup()==0.0){}
					 else{
					  markUp=df.format(fclBuyCostType.getMarkup());
					 }
					 }
					 if(fclBuyCostType.getStandard()!=null)
					 {
					 standard=fclBuyCostType.getStandard();
					 }
					 if(fclBuyCostType.getCostCode()!=null)
					 {
					 costcode=fclBuyCostType.getCostCode();
					 }
					 if(fclBuyCostType.getCostType()!=null)
					 {
					 costType=fclBuyCostType.getCostType();
					 }
					 if(fclBuyCostType.getCurrency()!=null )
					 {
					 String cur=fclBuyCostType.getCurrency().getCodedesc();
					 curreny=cur.length()>3?cur.substring(0,3):cur;
					 }
			}
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
	  			<display:column  title="&nbsp;&nbsp;Cost Code"><a href="<%=tempPath1%>" onclick="nonTest('<%=iStr%>')"><%=costcode%></a>	</display:column>
  				<display:column  title="Cost type" ><%=costType%></display:column>
  				<display:column title="EffDate"><%=effDate%></display:column>
  				<display:column title="Retail"><%=ratAmount%></display:column>
  				<display:column  title="Unit_Type"><%=unitycode%></display:column>  			
  				<display:column title="Amount"><%=activeAmt%></display:column>
				<display:column title="Markup" ><%=markUp%></display:column>
				<display:column title="FTFAmt" ><%=ftfAmt%></display:column>
				<display:column title="CTCAmt"><%=ctcAmt%></display:column>
				<display:column title="MinimumAmt" ><%=minimumAmt%></display:column>
				<display:column title="Standard" ><%=standard%></display:column>
				<display:column title="Curreny" ><%=curreny%></display:column>
 <%
  			 j++;
  			
%> 
		</display:table>
        </table>
        </div>  
    	</td> 
  		 </tr>  
		</table>
		<%} %>
</td>
</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<html:hidden property="buy" />
<html:hidden property="index" />	
<script>parent.parent.makeFormBorderless(document.getElementById("addFCLCostCodes"))</script>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
