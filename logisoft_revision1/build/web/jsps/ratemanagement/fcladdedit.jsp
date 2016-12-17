<%@ page language="java"  import="com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,java.text.DecimalFormat,
com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,java.util.List,com.gp.cong.logisoft.util.DBUtil,
com.gp.cong.logisoft.domain.FclBuyCostTypeRates,com.gp.cong.logisoft.domain.FclBuyCost"%>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:directive.page import="java.text.DateFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<jsp:useBean id="fclrecordform" class="com.gp.cong.logisoft.struts.ratemangement.form.AddFCLRecordsForm" scope="request"/>   
<%
String path = request.getContextPath();
String costtype="";
List list=new ArrayList();
String param="";
FclBuyCostTypeRates fclBuyCostType=null;
FclBuyAirFreightCharges fclBuyAirFreightCharges=null;
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DBUtil dbUtil=new DBUtil();
String  tempPath1="";
String unitycode="";
String ComCode="";
String costcode="";
String CodeDesc="";
String costType="";
String ratAmount="";
String ctcAmt="";
String ftfAmt="";
String effectiveDate="";
String minimumAmt="";
String activeAmt="";
String markup="";
String standard="";
String wightRange="";
String codesc="";
//String costType="";
String rateAmount="";
Object obj=null;
String message="";
String modify="";
String currency="";
request.setAttribute("defaultcurrency",dbUtil.getGenericFCL(new Integer(32),"yes"));
if(request.getAttribute("usermessage")!=null)
{
	message=(String)request.getAttribute("usermessage");
	

}
if(session.getAttribute("view")!=null){
	
	modify=(String)session.getAttribute("view");
}
FclBuyCost fclBuycost=new FclBuyCost();
List recordsList=new ArrayList();
 %>
<%
 param=request.getParameter("flaterate");
 String buy = "";

 if(request.getParameter("buy")!=null && !request.getParameter("buy").trim().equals(""))
 {
 	buy=request.getParameter("buy");
 }


if(param!=null&&list!=null&&!param.equals("")){

	int pra=Integer.parseInt(param);
	if(buy!=null&&buy.equals("buy")){
		
		list=(List)session.getAttribute("fclrecords");
		if(pra<list.size()){
			fclBuyCostType=(FclBuyCostTypeRates)list.get(Integer.parseInt(param));
			if(fclBuyCostType.getUnitType()!=null)
					 {
					 unitycode=fclBuyCostType.getUnitType().getCodedesc();
					 }
							 if(fclBuyCostType.getRatAmount()!=null)
					 {
					 ratAmount=df.format(fclBuyCostType.getRatAmount());
					
					 }
					 if(fclBuyCostType.getFtfAmt()!=null)
					 {
					 ftfAmt=df.format(fclBuyCostType.getFtfAmt());
					
					 }
					
					 if(fclBuyCostType.getCtcAmt()!=null)
					 {
					 ctcAmt=df.format(fclBuyCostType.getCtcAmt());
					
					 }
					 
					 if(fclBuyCostType.getMinimumAmt()!=null)
					 {
					 minimumAmt=df.format(fclBuyCostType.getMinimumAmt());
					
					 }
					
					 if(fclBuyCostType.getActiveAmt()!=null)
					 {
					 activeAmt=df.format(fclBuyCostType.getActiveAmt());
				
					 }
					
					 if(fclBuyCostType.getMarkup()!=null)
					 {
					 markup=df.format(fclBuyCostType.getMarkup());
					
					 }
					if(fclBuyCostType.getStandard()!=null)
					 {
					 standard=fclBuyCostType.getStandard();
					 if(standard.equals("Y")){
 						 fclrecordform.setStandard("on");
					 	}
					 	else{
					 	 fclrecordform.setStandard("off");
					 	}

					 }
					 if(fclBuyCostType.getCostId()!=null)
					 {
					 costtype=fclBuyCostType.getCostId();
					
					 }
					 if(fclBuyCostType.getCurrency()!=null)
					 {
					 currency=fclBuyCostType.getCurrency().getId().toString();
					 }
					  if(fclBuyCostType.getCostType()!=null)
					 {
					 costType=fclBuyCostType.getCostType();
					
					 }
					 if(fclBuyCostType.getEffectiveDate()!=null){
							effectiveDate= dateFormat.format(fclBuyCostType.getEffectiveDate());
					 }
				
			}
		}

	else {
	
	list=(List)session.getAttribute("fclfrightrecords");
	fclBuyAirFreightCharges=(FclBuyAirFreightCharges)list.get(Integer.parseInt(param));
	
	 if(fclBuyAirFreightCharges.getWieghtRange()!=null)
					 {
					 wightRange=fclBuyAirFreightCharges.getWieghtRange().getCodedesc();
					 }
					 if(fclBuyAirFreightCharges.getRatAmount()!=null)
					 {
					 rateAmount=fclBuyAirFreightCharges.getRatAmount().toString();
					 }
					 if(fclBuyAirFreightCharges.getCostCode()!=null)
					 {
					 costcode=fclBuyAirFreightCharges.getCostCode();
					 }
					 if(fclBuyAirFreightCharges.getCostType()!=null)
					 {
					 costType=fclBuyAirFreightCharges.getCostType();
					 }
				
					if(fclBuyAirFreightCharges.getCostId()!=null)
					 {
					 costtype=fclBuyAirFreightCharges.getCostId();
					 
					 }
					if(fclBuyAirFreightCharges.getCurrency()!=null){
					
					 currency=fclBuyAirFreightCharges.getCurrency().getId().toString() ;
					
					 }
					
		}
	
  }

if(request.getAttribute("addfclrecord")!=null){%>
<script type="text/javascript">
	    self.close();
		opener.location.href="<%=path%>/jsps/ratemanagement/AddFCLRecords.jsp";

     
</script>
<%}%>


<html>
	<head>
		<title>JSP for AddLclColoadCommodityForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function addForm()
		{
		
		document.fCLAddEditForm.buttonValue.value="add";
		document.fCLAddEditForm.submit();
		}
		function confirmdelete()
		{
		
		document.fCLAddEditForm.buttonValue.value="delete";
		document.fCLAddEditForm.submit();
		}cancelForm()
		function cancelForm()
		{
		
		document.fCLAddEditForm.buttonValue.value="cancel";
		document.fCLAddEditForm.submit();
		}
		function disabled(val1,val2) {
		
		
			if(val1!=""&&(val1 == 0 || val1== 3))
			{
		  	  	 var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id != "cancel")	 {
	   		 			     //imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
		   		//var input = document.getElementsByTagName("input");
			   		//for(i=0; i<input.length; i++)	{
			  			//input[i].readOnly=true;
					 	//}
		  	 	//var textarea = document.getElementsByTagName("textarea");
			  	 	//for(i=0; i<textarea.length; i++){
			 			//textarea[i].readOnly=true;		 					
					  	//}
	   			//var select = document.getElementsByTagName("select");
	   		   		//for(i=0; i<select.length; i++)	{
				 		//select[i].disabled=true;
						//select[i].style.backgroundColor="blue";
		  		  	 	//}
		  		  	 	//document.getElementById("save").style.visibility = 'hidden';
		  		  	 	//document.getElementById("delete").style.visibility = 'hidden';
		  		  	 	
	  	 		}
	  	 		
		  	 if(val1 == 1)	 {
		  		 document.fCLAddEditForm.standard.disabled=true
	  	 		 	 document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 	if(val1==3 && val2!=""){
				
				alert(val2);
				
				}		
    		}
		</script>
<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
<html:form  action="/fclAddEdit"   name="fCLAddEditForm" type="com.gp.cong.logisoft.struts.ratemangement.form.FCLAddEditForm" scope="request">
<table>
<tr>
	 <td  align="left" >
 	   <input type="button" value="Save" id="save" class="buttonStyleNew" onclick="addForm()"/>
 	
 	  </td>
	  <td  align="left" class="headerbluelarge">
	   
	     <input type="button" value="Go Back" class="buttonStyleNew" onclick="cancelForm()" id="cancel" />
      </td>
	  <td>
	       <input type="button" value="Delete" class="buttonStyleNew" onclick="confirmdelete()" id="delete"/>
	     
	  </td>
</tr>
</table>
  <table border="0" class="tableBorderNew" width="100%">

  		<tr class="tableHeadingNew" height="90%">Edit FCL Rate </tr>	
  		<tr class="textlabels"  class="style2">

 
	</tr>  
	<tr class="textlabels">
<%
if(costType!=null && costType.trim().equalsIgnoreCase("Per Container Size"))
{%>
	 
	 <td >Unit Type</td>
	  <td ><html:text property="unittype" readonly="true"  maxlength="7" size="7"   value="<%=unitycode%>"/></td>
	<td >Amount</td>
	 <td ><html:text property="amount" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  value="<%=activeAmt%>"/></td>
	 <td >Markup</td>
	 <td ><html:text property="markup"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"     value="<%=markup %>"/></td>
	 <td >Standard</td>
	 <td ><html:checkbox property="standard" name="fclrecordform" ></html:checkbox></td>
<%
}
else if(costType!=null &&  costType.trim().equalsIgnoreCase("Per CBM")||costType.trim().equalsIgnoreCase("per LBS")||costType.trim().equalsIgnoreCase("Per 1000KG"))
{
%>
	  <td >Retail</td>
	  <td><html:text property="pcretail"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value="<%=ratAmount %>"/></td>
	   <td >CTC</td>
	  <td><html:text property="pcctc" onkeypress="check(this,4)"  onblur="checkdec(this)" maxlength="7" size="7"    value="<%=ctcAmt %>" /></td>
	  <td >FTF</td>
	  <td><html:text property="pcftf" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"     value="<%=ftfAmt%>"/></td>
	 <td >Minimun</td>
	  <td><html:text property="pcminimun" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value="<%=minimumAmt%>"/></td>
	 
  
<%
}
 else if(costType!=null && costType.trim().equalsIgnoreCase("PER 2000 LBS") || costType.trim().equalsIgnoreCase("PERCENT OFR") ||  costType.trim().equalsIgnoreCase("Per Dock Receipts")||costType.trim().equalsIgnoreCase("Per Cubic Foot")||costType.trim().equalsIgnoreCase("PER BL CHARGES") ||costType.trim().equalsIgnoreCase("Flat Rate Per Container"))
 {
%>
	 
	 <td width="94">Retail</td>
	 <td><html:text property="pcretail" onkeypress="check(this,4)"  onblur="checkdec(this)" maxlength="7" size="7"   styleClass="verysmalldropdownStyle"  value="<%=ratAmount%>"/></td>
 
<%
}
else if(costType!=null && costType.trim().equalsIgnoreCase("Air Freight Costs"))
{%>
	 <td width="121"><span class="textlabels">Air Wight Range </span></td>
	 <td width="105"><html:text property="wightrange"  readonly="true" styleClass="verysmalldropdownStyle"  value="<%=wightRange%>"/></td>
     <td width="121"><span class="textlabels">Air Freight Amount </span></td>
    <td width="105"><html:text property="amount"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  styleClass="verysmalldropdownStyle"  value="<%=rateAmount%>"/></td>
 
<%
	}
 %>
  <td>EffectiveDate</td>
      <td>
			<html:text property="effectiveDate" styleId="txtEFDitemcreatedon" styleClass="verysmalldropdownStyle"
				size="7" readonly="true" value="<%=effectiveDate%>" />
			<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
			id="EFDitemcreatedon"	onmousedown="insertDateFromCalendar(this.id,0);" />
			</td>
	<td >Currency</td>
	<td colspan="4">&nbsp;<html:select property="currency" value="<%=currency%>"> 
      <html:optionsCollection name="defaultcurrency" /> 
      </html:select>  
      
            </td>
 </tr>
 <tr><td>
 	<table>
 		<tr>
 		<td>&nbsp;</td>
 		</tr>
 	</table>
 </td>
 
 </tr>
 </table>
<html:hidden property="index" styleId="index" value="<%=param%>"/>	
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<html:hidden property="costcode" styleId="costtype" value="<%=costtype%>"/>
<html:hidden property="buy" styleId="costtype" value="<%=buy%>"/>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

