<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.LCLColoadStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:directive.page import = "java.text.DecimalFormat"/>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
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
LCLColoadStandardCharges coStandardCharges=new LCLColoadStandardCharges();

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

LCLColoadStandardCharges coStd=new LCLColoadStandardCharges();
coStd.setStandard("off");
coStd.setAsFrfgted("off");

effectiveDate=dateFormat.format(new Date());
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
coStd.setStandard(airRatesBean.getStandard());
coStd.setAsFrfgted(airRatesBean.getAsFrfgted());

}
request.setAttribute("airStd",coStd);
if(session.getAttribute("coStandardCharges")!=null )
{
	
    coStandardCharges=(LCLColoadStandardCharges)session.getAttribute("coStandardCharges");
	if(coStandardCharges.getChargeCode()!=null)
	{
		chargecode=coStandardCharges.getChargeCode().getCode();
		codeDesc=coStandardCharges.getChargeCode().getCodedesc();
	}
	if(coStandardCharges.getChargeType()!=null)
	{
	chargeType=coStandardCharges.getChargeType().getId().toString();
	chartypedesc=coStandardCharges.getChargeType().getCodedesc();
	}
	if(coStandardCharges.getMinAmt()!=null)
	{
		minAmt=df.format(coStandardCharges.getMinAmt());
	}
	if(coStandardCharges.getEffectiveDate()!=null)
	{
		effectiveDate=coStandardCharges.getEffectiveDate().toString();
	}
	if(coStandardCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(coStandardCharges.getInsuranceRate());
	}
	if(coStandardCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(coStandardCharges.getInsuranceAmt());
	}
	if(coStandardCharges.getPercentage()!=null)
	{
	percentage=per.format(coStandardCharges.getPercentage());
	}
	if(coStandardCharges.getAmount()!=null)
	{
	amount=df.format(coStandardCharges.getAmount());
	}
	if(coStandardCharges.getAmtPerCft()!=null)
	{
	cft=df.format(coStandardCharges.getAmtPerCft());
	}
	if(coStandardCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(coStandardCharges.getAmtPer100lbs());
	}
	if(coStandardCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(coStandardCharges.getAmtPerCbm());
	}
	if(coStandardCharges.getAmtPer1000Kg()!=null)
	{
	amtkg=df.format(coStandardCharges.getAmtPer1000Kg());
	}
	if(coStandardCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(coStandardCharges.getEffectiveDate());
		}
}
List typeList1=new ArrayList();
if(session.getAttribute("coagssAdd") != null)
{
	agssList = (List) session.getAttribute("coagssAdd");
	
}
if(session.getAttribute("coagssAdd") != null)
{
	agssList = (List) session.getAttribute("coagssAdd");
	for(int i=0;i<agssList.size();i++)
	{
	LCLColoadStandardCharges ftfComm=(LCLColoadStandardCharges)agssList.get(i);
	if(ftfComm.getChargeCode()!=null && ftfComm.getChargeCode().getCode().equals(chargecode))
	{
	typeList1.add(ftfComm);
	}
	}
}

if(typeList != null)
{
	typeList=dbUtil.getTypeSSList(new Integer(34),"yes","Select Rate type",null);
	request.setAttribute("typeList",typeList);
	
}

	
	
request.setAttribute("typeList",typeList);
String msg="";
if(request.getAttribute("message")!=null)
{
	modify=(String)request.getAttribute("message");
}
modify = (String) session.getAttribute("modifyforlclcoloadRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/addLclColoadStandard.do";
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
		<title>JSP for AGSSForm form</title>
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
			document.aGSSForm.submit();
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
		 
		   document.aGSSForm.buttonValue.value="addItem";
			document.aGSSForm.submit();
		}
		function getCodeDesc(ev)
		{
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
  	function deleteCOMM(value1){
  			
  			document.aGSSForm.standardId.value=value1;
		  	document.aGSSForm.buttonValue.value="delete";
		  	document.aGSSForm.submit();
  	}
	</script>
	</head>
	<body class="whitebackgrnd" >
		
	<html:form action="/addLclColoadStandard" name="aGSSForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AddLclColoadStandardForm" scope="request">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
     <td>
		 <%if(session.getAttribute("listLclAgssitem")==null){ %>
		   <table width=100% border="0" cellpadding="0" cellspacing="0" >
		    <tr class="tableHeadingNew"><td>Accessorial & Gen Std Chrgs</td>
				<td align="right">
			   	 <input type="button" onclick="addfield()" id="save" value="Add" class="buttonStyleNew"/>
				</td>
			</tr>
		<%} else{%>
			<table width=100% border="0" cellpadding="0" cellspacing="0">
			<tr class="tableHeadingNew">Accessorial & Gen Std Chrgs</tr>
			<tr class="textlabels">
				<td >Chrg Code</td>
				<td >Charge Desc </td>
				<td >Type </td>
				<td >Std</td>
				
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
         		<td>Percent</td>
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
	         	<td>Insurance Amount</td>
	         	<td>MinAmt</td>
         	<%
         	}
         	%>
           
				<td>Eff.Date</td>
		 </tr>
		 <tr class="textlabels">
 		 <td >
<%-- 		 <html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3" />--%>
 		 <input name="charge"  id="charge" value="<%=chargecode%>" onkeydown="getCodeDesc(this.value)"  onkeypress="getCodeDescPress(this.value)"  size="4"/>
	     <dojo:autoComplete  formId="aGSSForm" textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=CO_AGSS&from=0"/>
 		 </td>
   		 <td >
<%--   		 <html:text property="desc" value="<%=codeDesc%>"  size="12"/>--%>
   		  <input name="desc" id="desc" size="12" value="<%=codeDesc%>" onkeydown="getChgCodeDesc()" onkeypress="getChgCodeDescPress()"/>
          <dojo:autoComplete formId="aGSSForm" textboxId="desc" action="<%=path%>/actions/getChargeCodeDesc.jsp"/>
   		 </td>
  		      
  		 <td ><html:select property="chargeType" styleClass="verysmalldropdownStyle" value="<%=chargeType%>" onchange="submit()">
      								   <html:optionsCollection name="typeList"/>          
                </html:select></td>
         <td><html:checkbox property="standard" name="airStd"></html:checkbox></td>
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
         <%if(defaultRate.equals("E"))
         { %>
         <td><html:text property="amtPerCft" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPerCft" value="<%=cft%>"/></td>
  		 <td><html:text property="amtPer100lbs" onkeypress="check(this,5)" onblur="checkdec(this)"maxlength="8" size="8"  styleId="amtPer100lbs" value="<%=lbs%>"/></td>
  		 <%}
  		 else if(defaultRate.equals("M"))
  		 { %>
  		 <td><html:text property="amtPerCbm" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPerCbm" value="<%=cbm%>"/></td>
  		 <td><html:text property="amtPer1000kg" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer1000kg" value="<%=amtkg%>"/></td>	
  		<%} %>
  		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
         <%
         }
         if(chargeType.equals("11288"))
         {
         %>
         <td><html:text property="percentage"  maxlength="3" size="3" onblur="checkdec(this)" styleId="percentage" value="<%=percentage%>"/>%</td>
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
		  <td><html:checkbox property="asFrfgted" name="airStd"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  		 <td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" size="7" readonly="true" value="<%=effectiveDate%>"/></td>
  		 <td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
  		 <td align="right">
  		     <input type="button" onclick="addAGSS()" id="save" value="Add To List" class="buttonStyleNew"/>
		 </td>
        </tr> 
     </table>  
		<%} %>	
			
	  <tr>
		<td colspan="2">
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        %>
        <display:table name="<%=agssList%>" pagesize="<%=pageSize%>"  class="displaytagstyle"  sort="list" id="agsstable" > 
			<%
			    Date effective_Date=null;
				String type=null;
				String chargeCode=null;
				String tempPath="",chrCode="";
					String effDate=null;				
			    if(agssList != null && agssList.size()>0)
			    {
					LCLColoadStandardCharges lCLColoadStandardCharges2=(LCLColoadStandardCharges)agssList.get(i);
					if(lCLColoadStandardCharges2.getChargeType()!=null)
					{
					type=lCLColoadStandardCharges2.getChargeType().getCodedesc();
					}
					if(lCLColoadStandardCharges2.getChargeCode()!=null)
					{
					chrCode = lCLColoadStandardCharges2.getChargeCode().getCode();
					chargeCode=lCLColoadStandardCharges2.getChargeCode().getCodedesc();
					}
					 if(lCLColoadStandardCharges2.getEffectiveDate()!=null){
					 effDate=dateFormat.format(lCLColoadStandardCharges2.getEffectiveDate());
					}
					
					if(lCLColoadStandardCharges2.getAmtPer1000Kg()!=null)
						{
							amtkg=df.format(lCLColoadStandardCharges2.getAmtPer1000Kg());
						}
					if(lCLColoadStandardCharges2.getInsuranceRate()!=null)
						{
						   insuranceRate=df.format(lCLColoadStandardCharges2.getInsuranceRate());
						}	
				
					if(lCLColoadStandardCharges2.getInsuranceAmt()!=null)
						{
							insuranceAmt=df.format(lCLColoadStandardCharges2.getInsuranceAmt());
						}	
				
					if(lCLColoadStandardCharges2.getAmount()!=null)
						{
							amount=df.format(lCLColoadStandardCharges2.getAmount());
						}	
				
					if(lCLColoadStandardCharges2.getMinAmt()!=null)
						{
							minAmt=df.format(lCLColoadStandardCharges2.getMinAmt());
						}	
					if(lCLColoadStandardCharges2.getAmtPerCft()!=null)
						{
							cft=df.format(lCLColoadStandardCharges2.getAmtPerCft());
						}	
					if(lCLColoadStandardCharges2.getAmtPer100lbs()!=null)
						{
							lbs=df.format(lCLColoadStandardCharges2.getAmtPer100lbs());
						}	
					if(lCLColoadStandardCharges2.getAmtPerCbm()!=null)
						{
							cbm=df.format(lCLColoadStandardCharges2.getAmtPerCbm());
						}
							
						if(lCLColoadStandardCharges2.getPercentage() !=null)
						{
						
							percentage=per.format(lCLColoadStandardCharges2.getPercentage()) + "%";
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
  			
  			<display:column  title="Charge Code"><a href="<%=tempPath%>"><%=chargeCode %></a> </display:column>
			<display:column  title="Charge Type"  ><%=type %></display:column>
			<display:column property="standard" title="Standard"></display:column>
			<display:column property="asFrfgted" title="As Freighted"></display:column>
			<display:column title="Insurance Rate"><%=insuranceRate%></display:column>
			<display:column title="Insurance Amount"><%=insuranceAmt%></display:column>
			<%if(defaultRate.equals("E"))
			{ %>
			<display:column  title="AmtPerCft" ><%=cft%></display:column>
			<display:column  title="AmtPer100lbs" ><%=lbs%></display:column>
			<%}else if(defaultRate.equals("M"))
			{ %>
			<display:column  title="AmtPer100lbs"><%=amtkg%></display:column>
			<display:column  title="AmtPerCbm" ><%=cbm%></display:column>
			<%} %>
			<display:column  title="Percentage" ><%=percentage%></display:column>
			<display:column  title="MinAmt" ><%=minAmt%></display:column>
			<display:column  title="Amt"><%=amount%></display:column>
			<display:column title="Effective Date" ><%=effDate%></display:column>
			<display:column>
			<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
			<img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteCOMM('<%=chrCode%>')" />
			</span>
			</display:column>
  			<% i++;
  			%>
		  </display:table>
        </table></div>  
    	</td> 
     </tr>  
  </table>
  </td>
  </table>
  
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<html:hidden property="index" />
<html:hidden property="standardId" />
</html:form>
</body>
 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

