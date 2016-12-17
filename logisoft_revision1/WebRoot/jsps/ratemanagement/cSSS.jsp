<%@ page language="java" import= "java.text.DecimalFormat,com.gp.cong.logisoft.domain.User,
com.gp.cong.logisoft.util.DBUtil,java.text.DateFormat,
java.text.SimpleDateFormat,java.util.*,com.gp.cong.logisoft.domain.AirCommodityCharges,com.gp.cong.logisoft.domain.AirStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

List csssList = new ArrayList();
List applyGeneralStandardList=new ArrayList();
String chargecode="";
String codeDesc="";
DBUtil dbUtil=new DBUtil();
String chargeType="";
String rateType="";
String minAmt="";
String msg="";
User user=null;
DecimalFormat df2 = new DecimalFormat("########0.00");
DecimalFormat per = new DecimalFormat("0.000");
if(session.getAttribute("loginuser")!=null)
{
	user=(User)session.getAttribute("loginuser");
}
AirRatesBean airRatesBean=new AirRatesBean();
AirCommodityCharges airComm=new AirCommodityCharges();
airComm.setStandard("off");
airComm.setAsFrfgted("off");
airComm.setExclude("off");

if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airComm.setStandard(airRatesBean.getStandard());
airComm.setAsFrfgted(airRatesBean.getAsFrfgted());
airComm.setExclude(airRatesBean.getExclude());
}
request.setAttribute("airComm",airComm);
List typeList=new ArrayList();
AirCommodityCharges airCommodityCharges=new AirCommodityCharges();
airCommodityCharges.setStandard("off");
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

effectiveDate=dateFormat.format(new Date());
DecimalFormat df = new DecimalFormat("0.00");
if(session.getAttribute("airCommodityCharges")!=null )
{
    airCommodityCharges=(AirCommodityCharges)session.getAttribute("airCommodityCharges");
	if(airCommodityCharges.getChargeCode()!=null)
	{
		chargecode=airCommodityCharges.getChargeCode().getCode();
		codeDesc=airCommodityCharges.getChargeCode().getCodedesc();
	}
	
	if(airCommodityCharges.getChargeType()!=null)
	{
		chargeType=airCommodityCharges.getChargeType().getId().toString();
		
	}
	if(airCommodityCharges.getMinAmt()!=null)
	{
	minAmt=df2.format(airCommodityCharges.getMinAmt());
	}
	if(airCommodityCharges.getAmount()!=null)
	{
	amount=df2.format(airCommodityCharges.getAmount());
	}
	if(airCommodityCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df2.format(airCommodityCharges.getInsuranceRate());
	}
	if(airCommodityCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df2.format(airCommodityCharges.getInsuranceAmt());
	}
	if(airCommodityCharges.getPercentage()!=null)
	{
	percentage=per.format(airCommodityCharges.getPercentage());
	
	}
	if(airCommodityCharges.getAmtPerCft()!=null)
	{
	cft=df2.format(airCommodityCharges.getAmtPerCft());
	}
	if(airCommodityCharges.getAmtPerCbm()!=null)
	{
	cbm=df2.format(airCommodityCharges.getAmtPerCbm());
	}
	if(airCommodityCharges.getAmtPer100lbs()!=null)
	{
	lbs=df2.format(airCommodityCharges.getAmtPer100lbs());
	}
	if(airCommodityCharges.getAmtPer1000kg()!=null)
	{
	amtkg=df2.format(airCommodityCharges.getAmtPer1000kg());
	}
	if(airCommodityCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(airCommodityCharges.getEffectiveDate());
	}
}


if(session.getAttribute("cssList") != null)
{
	csssList = (List) session.getAttribute("cssList");
}

if(typeList != null)
{
	typeList=dbUtil.getTypeListForCsss(new Integer(34),"yes","Select Rate type");
	
	request.setAttribute("typeList",typeList);
}

request.setAttribute("typeList",typeList);
if(session.getAttribute("applyGeneralStandardList")!=null)
{
	applyGeneralStandardList=(List)session.getAttribute("applyGeneralStandardList");
}	

String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}


String editPath=path+"/cSSS.do";
if(request.getAttribute("exist") != null)
  {
     msg = request.getAttribute("exist").toString();
     chargecode = "";
     codeDesc = "";
   
  }
%>
<font color="red" size="3"><%= msg %></font>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Commodity Specific Charges</title>
    
		<%@include file="../includes/baseResources.jsp" %>

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
		   		for(i=0; i<input.length; i++){
			  			if(input[i].name!="exclude"&& input[i].name!="charge"&& input[i].name!="desc")
		   			{	
		   		    	input[i].readOnly=false;
		   		    	input[i].style.backgroundColor="white";
		   		    }
			  	}
		  	 	var select = document.getElementsByTagName("select");
		   		for(i=0; i<select.length; i++){
			 		select[i].disabled=false;
					select[i].style.backgroundColor="white";
			  		
		  	 	}
		   		}
   		}
	function searchform()
	{
			document.cSSSForm.buttonValue.value="search";
			document.cSSSForm.submit();
	}
    function addAGSS(){
			if(document.cSSSForm.exclude.checked==false && document.cSSSForm.chargeType.value=="0")
			{
				alert("Please select Exclude  or Charge Type");
				return;
			}
			if(document.cSSSForm.charge.value=="")
			{
				alert("Please select Charge code");
				return;
			}
			document.cSSSForm.buttonValue.value="add";
	  		document.cSSSForm.submit();
	}
    function confirmdelete(obj)
	 {
			var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('agsstable').rows[rowindex].cells;	
	   		document.cSSSForm.index.value=rowindex-1;
			document.cSSSForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this value");
			if(result)
			{
   				document.cSSSForm.submit();
   			}	
   	}
    function submit()
	 {
		   document.cSSSForm.buttonValue.value="";
		   document.cSSSForm.submit();
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
	   		document.getElementById("add").style.visibility = 'hidden';
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
   //---------------DOJO CODES-------------------------
    function getCodeDesc(ev)
     {
		if(event.keyCode==9)
		{
		   document.cSSSForm.desc.value="";
		   document.cSSSForm.buttonValue.value="chargeCode";
		   document.cSSSForm.submit();
		}
     }
   function getCodeDescPress(ev)
    {
		if(event.keyCode==13)
		{
			 document.cSSSForm.desc.value="";
			 document.cSSSForm.buttonValue.value="chargeCode";
			 document.cSSSForm.submit();
		}
    }
   function getChgCodeDesc()
    {
      if(event.keyCode==9)
  	   {
	  	 document.cSSSForm.charge.value="";
	  	 document.cSSSForm.buttonValue.value="chargeCode";
	  	 document.cSSSForm.submit();
       }	
    }
   function getChgCodeDescPress()
    {
      if(event.keyCode==13)
  	  {
	   	 document.cSSSForm.charge.value="";
	  	 document.cSSSForm.buttonValue.value="chargeCode";
	   	 document.cSSSForm.submit();
      }	
    }
   function addfield()
   {
	  document.cSSSForm.buttonValue.value="addItem";
  	  document.cSSSForm.submit();
   }
    function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.cSSSForm.charge.value;
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
			params['codeDesc'] = document.cSSSForm.desc.value;
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
	
  
 <html:form action="/cSSS" name="cSSSForm" type="com.gp.cong.logisoft.struts.ratemangement.form.CSSSForm" scope="request">

  <table width="100%" border="0" >
  <tr class="textlabels">
    <%if(session.getAttribute("airaddcsslist")==null ){ %>
    	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
         <tr class="tableHeadingNew"><td>Commodity Specfic Std Charges</td>
		  <td align="right">
			  <input type="button" value="Add" id="add" class="buttonStyleNew" onclick="addfield()" />
		  </td>
		</tr>
		
    	<%}else{ %>
	  <table width="100%"  border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew" >
	    <tr class="tableHeadingNew">Commodity Specfic Std Charges</tr>
		<tr class="textlabels">
				<td>Chrg Code</td>
				<td>Charge Desc </td>
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
         	if(chargeType.equals("11291")){
         	%>	
         		<td>$/100lb</td>
				<td>MinAmt</td>
			<%}
         	if(chargeType.equals("11288"))
         	{
         	%>
	         	<td>Percent</td>
	         	<td>MinAmt</td>
         	<%
         	}
         	
         	if(chargeType.equals("11290"))
		 	{
         	%>
	         	<td>As Freighted</td>
	         	<td>Insurance Rate</td>
	         	<td>Insurance Amount</td>
	         	<td>MinAmt</td>
         	<%
         	}
         	%>
				<td>Eff.Date</td>
		 </tr>
		 <tr class="textlabels">
 		 <td >
<%-- 		 <html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3"/>--%>
 		     <input name="charge" id="charge" value="<%=chargecode%>"  onkeydown="getdestPort(this.value)"  size="4"/>
   		     <dojo:autoComplete  formId="cSSSForm" textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=CSSS&from=0"/>
 		 </td>
   		 <td>
<%--   		  <html:text property="desc" value="<%=codeDesc%>" readonly="true" styleClass="varysizeareahighlightgrey" size="12"/>--%>
   		  	  <input name="desc" id="desc" size="12" value="<%=codeDesc%>" onkeydown="getdestPort1(this.value)"/>
          	  <dojo:autoComplete formId="cSSSForm" textboxId="desc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=CSSS"/>
   		 </td>
   		 <td><html:checkbox property="exclude" name="airComm"
							onclick="change1(exclude)"></html:checkbox>
					</td>
  		 <td><html:select property="chargeType" styleClass="verysmalldropdownStyle" value="<%=chargeType%>" onchange="submit()">
      								   <html:optionsCollection name="typeList"/>          
                </html:select></td>
         <td><html:checkbox property="standard" name="airComm"></html:checkbox></td>
         <% 
         if(chargeType.equals("11287"))
         {
         %>
         <td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
         <% 
         }
         if(chargeType.equals("11291"))
         {%>
        			 <td><html:text property="amtPer100lbs" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer100lbs" value="<%=lbs%>"/></td>
  		         <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
        	 <%
         	}
         if(chargeType.equals("11288"))
         {
         %>
         <td><html:text property="percentage"  maxlength="3" size="3" onblur="checkdec(this)" value="<%=percentage%>"/>%</td>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 
		 <%
		 }
		
		 if(chargeType.equals("11290"))
		 {
		 %>
		  <td><html:checkbox property="asFrfgted" name="airComm"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  			<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" size="7" readonly="true"/></td>
  			<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
  			<td align="center">
  		    	<input type="button" value="Add To List" class="buttonStyleNew" onclick="addAGSS()" style="width:90px"/>
  			</td>
        </tr> 
        <%}%>
        
		<tr>
		 <td>
		 <div id="divtablesty1" class="scrolldisplaytable">
         <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <% int k=0; %>
        
        <display:table name="<%=csssList%>" pagesize="<%=pageSize%>"   class="displaytagstyle"  sort="list" id="agsstable" defaultorder="ascending" defaultsort="2"> 
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
  			<%Date effective_Date=null;
						String effDate=null;		
				String type=null;
				String chargeCode=null;
				String tempPath="";
				String perc="";
			    if(csssList != null && csssList.size()>0)
			    {
					AirCommodityCharges airCommodityChargesObj2=(AirCommodityCharges)csssList.get(k);
					if(airCommodityChargesObj2.getChargeType()!=null)
					type=airCommodityChargesObj2.getChargeType().getCodedesc();
					if(airCommodityChargesObj2.getChargeCode()!=null)
					chargeCode=airCommodityChargesObj2.getChargeCode().getCodedesc();
				
			
  					if(airCommodityChargesObj2.getEffectiveDate()!=null){
  					effective_Date=airCommodityChargesObj2.getEffectiveDate();
  					
  					
					effDate=dateFormat.format(effective_Date);
					}
					if(airCommodityChargesObj2.getPercentage()!=null)
					{
					perc=per.format(airCommodityChargesObj2.getPercentage()) + "%";
					}
					
				if(airCommodityChargesObj2.getAmtPer1000kg()!=null)
				{
					amtkg=df.format(airCommodityChargesObj2.getAmtPer1000kg());
				}
				if(airCommodityChargesObj2.getInsuranceRate()!=null)
				{
				   insuranceRate=df.format(airCommodityChargesObj2.getInsuranceRate());
				}	
				
				if(airCommodityChargesObj2.getInsuranceAmt()!=null)
				{
					insuranceAmt=df.format(airCommodityChargesObj2.getInsuranceAmt());
				}	
				
				if(airCommodityChargesObj2.getAmount()!=null)
				{
					amount=df.format(airCommodityChargesObj2.getAmount());
				}	
				
				if(airCommodityChargesObj2.getMinAmt()!=null)
				{
					minAmt=df.format(airCommodityChargesObj2.getMinAmt());
				}	
				if(airCommodityChargesObj2.getAmtPerCft()!=null)
				{
					cft=df.format(airCommodityChargesObj2.getAmtPerCft());
				}	
				if(airCommodityChargesObj2.getAmtPer100lbs()!=null)
				{
					lbs=df.format(airCommodityChargesObj2.getAmtPer100lbs());
				}	
				if(airCommodityChargesObj2.getAmtPerCbm()!=null)
				{
					cbm=df.format(airCommodityChargesObj2.getAmtPerCbm());
				}	
					
				}	
					String iStr=String.valueOf(k);
  					tempPath=editPath+"?ind="+iStr;	
			 %>
  			<display:column  title="Chrg Description "><a href="<%=tempPath%>"><%=chargeCode %></a> </display:column>
			<display:column  title="Chrg Type"  ><%=type %></display:column>
			<display:column property="standard" title="Std"></display:column>
			<display:column property="asFrfgted" title="As Freighted"></display:column>
			<display:column  title="Insure Rate"><%=insuranceRate%></display:column>
			<display:column  title="Insure Amt"><%=insuranceAmt%></display:column>
			<display:column  title="Amt/100lb"><%=lbs%></display:column>
			<display:column title="Perc" ><%=perc%></display:column>
			<display:column  title="MinAmt" ><%=minAmt%></display:column>
			<display:column  title="Amt"><%=amount%></display:column>
			<display:column title="Eff Date" ><%=effDate%></display:column>
  			<% k++;%>
		</display:table>
        </table></div>  
    	</td> 
        </tr>  
	</table>
    </table>
    </tr>
    </table>
    
    
  		<html:hidden property="buttonValue" styleId="buttonValue"/>
  		<html:hidden property="index" />
	</html:form>
  </body>
  <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
