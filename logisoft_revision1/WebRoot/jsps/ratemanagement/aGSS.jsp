<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.AirStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
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
effectiveDate=dateFormat.format(date);
List typeList=new ArrayList();
List agssList=null;
String chargeType="0";
AirStandardCharges airStandardCharges=new AirStandardCharges();



AirStandardCharges airStd=new AirStandardCharges();
airStd.setStandard("off");
airStd.setAsFrfgted("off");

if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airStd.setStandard(airRatesBean.getStandard());
airStd.setAsFrfgted(airRatesBean.getAsFrfgted());
}
request.setAttribute("airStd",airStd);
if(session.getAttribute("airStandardCharges")!=null )
{
	
    airStandardCharges=(AirStandardCharges)session.getAttribute("airStandardCharges");
	if(airStandardCharges.getChargeCode()!=null)
	{
		chargecode=airStandardCharges.getChargeCode().getCode();
		codeDesc=airStandardCharges.getChargeCode().getCodedesc();
	}
	if(airStandardCharges.getChargeType()!=null)
	{
	chargeType=airStandardCharges.getChargeType().getId().toString();
	chartypedesc=airStandardCharges.getChargeType().getCodedesc();
	}
	if(airStandardCharges.getMinAmt()!=null)
	{
		minAmt=df.format(airStandardCharges.getMinAmt());
	}
	if(airStandardCharges.getEffectiveDate()!=null)
	{
		effectiveDate=airStandardCharges.getEffectiveDate().toString();
	}
	if(airStandardCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(airStandardCharges.getInsuranceRate());
	}
	if(airStandardCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(airStandardCharges.getInsuranceAmt());
	}
	if(airStandardCharges.getPercentage()!=null)
	{
	percentage=per.format(airStandardCharges.getPercentage());
	}
	if(airStandardCharges.getAmount()!=null)
	{
	amount=df.format(airStandardCharges.getAmount());
	}
	if(airStandardCharges.getAmtPerCft()!=null)
	{
	cft=df.format(airStandardCharges.getAmtPerCft());
	}
	if(airStandardCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(airStandardCharges.getAmtPer100lbs());
	}
	if(airStandardCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(airStandardCharges.getAmtPerCbm());
	}
	if(airStandardCharges.getAmtPer1000kg()!=null)
	{
	amtkg=df.format(airStandardCharges.getAmtPer1000kg());
	}
	if(airStandardCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(airStandardCharges.getEffectiveDate());
	}
}
List typeList1=new ArrayList();
if(session.getAttribute("agssAdd") != null)
{
	agssList = (List) session.getAttribute("agssAdd");
}

if(typeList != null)
{
	typeList=dbUtil.getTypeList1(new Integer(34),"yes","Select Rate type");
	request.setAttribute("typeList",typeList);
	
}


request.setAttribute("typeList",typeList);
String msg="";
if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
}
String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/aGSS.do";

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
		<title>JSP for AGSSForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		   dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		<script language="javascript" type="text/javascript">
	function addAGSS()
	{
			if(document.aGSSForm.chargeType.value=="0")
			{
			alert("Please select Charge Type");
			return;
			}
			if(document.aGSSForm.charge.value=="")
			{
			alert("Please select Charge code");
			return;
			}
			document.aGSSForm.buttonValue.value="add";
  			document.aGSSForm.submit();
	}
   function submit()
   {
		document.aGSSForm.buttonValue.value="";
		document.aGSSForm.submit();
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
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
	//---DOJO CODES-----	
  function getCodeDesc(ev){
		if(event.keyCode==9)
		{
		 document.aGSSForm.buttonValue.value="chargeCode";
		 document.aGSSForm.submit();
		}
	}
   function getCodeDescPress(ev)
	{
		if(event.keyCode==13)
		{
		 document.aGSSForm.buttonValue.value="chargeCode";
		 document.aGSSForm.submit();
		}
   }
  function getChgCodeDesc()
   {
	     if(event.keyCode==9)
	  	{
	  	 document.aGSSForm.buttonValue.value="chargeCode";
	  	 document.aGSSForm.submit();
	    }	
   }
  function getChgCodeDescPress()
  {
	    if(event.keyCode==13)
	  	{
	  	 document.aGSSForm.buttonValue.value="chargeCode";
	  	 document.aGSSForm.submit();
	    }	
  }	
  function addfield()
  {
		 document.aGSSForm.buttonValue.value="addItem";
  	 	 document.aGSSForm.submit();
  }
  
      function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.aGSSForm.charge.value;
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
			params['codeDesc'] = document.aGSSForm.desc.value;
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
	
  <html:form action="/aGSS" name="aGSSForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AGSSForm" scope="request">
	
 <table width="100%" border="0" >
  <tr class="textlabels">
	<%
	if(session.getAttribute("listOfAgssItem")==null){
    %>	
      <table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
      <tr class="tableHeadingNew"><td>Accessorial Gen Std Chrgs</td>
			<td align="right">
			   <input type="button" value="Add" onclick="addfield()" class="buttonStyleNew"/>
			</td>
	  </tr>	
  <%}
 	else{
  %>
	<table width="100%"  border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew">Accessorial Gen Std Chrgs</tr>
	<tr class="textlabels">
				<td>Chrg Code</td>
				<td>Charge Desc </td>
				<td>Type </td>
				<td>Std</td>
			<% 
            if(chargeType.equals("11287"))
        	{
        	%>
        	<td align="center">Amt</td>
        	
            <%
         	}
         	if(chargeType.equals("11291"))
         	{%>
         	<td>
						$/100lbs
					</td>
					
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
 		   <td>
<%-- 		  <html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3"/>--%>
 		      <input name="charge" id="charge" value="<%=chargecode%>"  onkeydown="getdestPort(this.value)"  size="4"/>
   		      <dojo:autoComplete  formId="aGSSForm" textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=AGSS&from=0"/>
 		   </td>
   		   <td>
<%--   		  <html:text property="desc" value="<%=codeDesc%>" readonly="true" styleClass="varysizeareahighlightgrey" size="12"/>--%>
   		      <input name="desc" id="desc" size="12" value="<%=codeDesc%>" onkeydown="getdestPort1(this.value)"/>
              <dojo:autoComplete formId="aGSSForm" textboxId="desc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=AGSS"/>
   		 </td>
  		 <td><html:select property="chargeType" styleClass="verysmalldropdownStyle" value="<%=chargeType%>" onchange="submit()">
      			<html:optionsCollection name="typeList"/>          
                </html:select></td>
         <td><html:checkbox property="standard" name="airStd"></html:checkbox></td>
         <% 
         if(chargeType.equals("11287"))
         {
         %>
         <td><html:text property="amount" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
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
         	<td><html:text property="percentage" onblur="checkdec(this)"  maxlength="3" size="3"  styleId="percentage" value="<%=percentage%>"/></td>
		 	<td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 
		 <%
		 }
		 
		 if(chargeType.equals("11290"))
		 {
		 %>
		  <td><html:checkbox property="asFrfgted" name="airStd"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)"  maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  			<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" size="7" readonly="true" value="<%=effectiveDate%>"/></td>
  			<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
  			<td align="right">
				  <input type="button" value="Add To List" onclick="addAGSS()" class="buttonStyleNew" id="save"/>
			</td>
        </tr> 
			
			<%}%>
			
	    <tr>
		<td colspan="2">
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        %>
        <display:table name="<%=agssList%>" pagesize="<%=pageSize%>"  class="displaytagstyle"  sort="list" id="agsstable"  defaultorder="ascending" defaultsort="2"> 
			<%
				String type=null;
				String chargeCode=null;
				String tempPath="";
					String effDate=null;	
					String perc="";			
			    if(agssList != null && agssList.size()>0)
			    {
					AirStandardCharges airStandardChargesObj2=(AirStandardCharges)agssList.get(i);
					if(airStandardChargesObj2.getChargeType()!=null)
					{
					type=airStandardChargesObj2.getChargeType().getCodedesc();
					}
					if(airStandardChargesObj2.getChargeCode()!=null)
					{
					chargeCode=airStandardChargesObj2.getChargeCode().getCodedesc();
					}
					 if(airStandardChargesObj2.getEffectiveDate()!=null){
					 effDate=dateFormat.format(airStandardChargesObj2.getEffectiveDate());
					}
					if(airStandardChargesObj2.getPercentage()!=null)
					{
					perc=per.format(airStandardChargesObj2.getPercentage()) + "%";
					}
					
					if(airStandardChargesObj2.getAmtPer1000kg()!=null)
				{
					amtkg=df.format(airStandardChargesObj2.getAmtPer1000kg());
				}
				if(airStandardChargesObj2.getInsuranceRate()!=null)
				{
				   insuranceRate=df.format(airStandardChargesObj2.getInsuranceRate());
				}	
				
				if(airStandardChargesObj2.getInsuranceAmt()!=null)
				{
					insuranceAmt=df.format(airStandardChargesObj2.getInsuranceAmt());
				}	
				
				if(airStandardChargesObj2.getAmount()!=null)
				{
					amount=df.format(airStandardChargesObj2.getAmount());
				}	
				
				if(airStandardChargesObj2.getMinAmt()!=null)
				{
					minAmt=df.format(airStandardChargesObj2.getMinAmt());
				}	
				if(airStandardChargesObj2.getAmtPerCft()!=null)
				{
					cft=df.format(airStandardChargesObj2.getAmtPerCft());
				}	
				if(airStandardChargesObj2.getAmtPer100lbs()!=null)
				{
					lbs=df.format(airStandardChargesObj2.getAmtPer100lbs());
				}	
				if(airStandardChargesObj2.getAmtPerCbm()!=null)
				{
					cbm=df.format(airStandardChargesObj2.getAmtPerCbm());
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
  			
  			<display:column  title="Chrg Description"><a href="<%=tempPath%>"><%=chargeCode %></a> </display:column>
			<display:column  title="Chrg Type"  ><%=type %></display:column>
			<display:column property="standard" title="Std"></display:column>
			
			<display:column property="asFrfgted" title="As Freighted"></display:column>
			<display:column  title="Insure Rate"><%=insuranceRate%></display:column>
			<display:column  title="Insuree Amount"><%=insuranceAmt%></display:column>
			
		
			<display:column  title="Amt/100lbs" ><%=lbs%></display:column>
			
			<display:column title="Perc" ><%=perc%></display:column>
			
			
			<display:column  title="MinAmt" ><%=minAmt%></display:column>
			
			<display:column  title="Amt"><%=amount%></display:column>
			
			<display:column title="Effective Date" ><%=effDate%></display:column>
			
  			<% i++;
  			%>
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

