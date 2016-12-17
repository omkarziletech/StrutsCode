<%@ page language="java"  import="java.text.DateFormat,java.text.DecimalFormat,java.text.ParseException,
java.util.*, java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,java.text.DecimalFormat,
com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,java.util.List,com.gp.cong.logisoft.util.DBUtil,
com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates,com.gp.cong.logisoft.domain.FclBuyCost"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>

<jsp:useBean id="fclrecordform" class="com.gp.cong.logisoft.struts.ratemangement.form.AddFCLRecordsForm" scope="request"/>   
<%
String path = request.getContextPath();
String costtype="";
List list=new ArrayList();
String param="";
FclBuyCostTypeFutureRates fclBuyCostType=null;
FclBuyAirFreightCharges fclBuyAirFreightCharges=null;
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DBUtil dbUtil=new DBUtil();
String  tempPath1="";
String unitycode="";
String ComCode="";                                                                                             
String costcode="";
String costType="";
String ratAmount="";
String ctcAmt="";
String ftfAmt="";
String minimumAmt="";
String activeAmt="";
String markup="";
String efd="";
String standard="";
String wightRange="";
String costCode="";
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
//


if(param!=null && list!=null && !param.equals(""))
 {
	int pra=Integer.parseInt(param);
	if(buy!=null&&buy.equals("buy"))
	{
		
	    list=(List)session.getAttribute("fclrecordsfuture");
		if(pra<list.size())
		{
		   
			fclBuyCostType=(FclBuyCostTypeFutureRates)list.get(Integer.parseInt(param));
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
			 if(fclBuyCostType.getEffectiveDate()!=null)
			 {
			 		efd=dateFormat.format(fclBuyCostType.getEffectiveDate());
			 }
			 if(fclBuyCostType.getCostType()!=null)
					 {
					 costType=fclBuyCostType.getCostType();
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
		opener.location.href="<%=path%>/jsps/ratemanagement/addFutureFcl.jsp";

     
</script>
<%}%>


<html>
	<head>
		<title>JSP for AddLclColoadCommodityForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function addForm()
		{
		
		document.futureFclEditForm.buttonValue.value="add";
		document.futureFclEditForm.submit();
		}
		function confirmdelete()
		{
		
		document.futureFclEditForm.buttonValue.value="delete";
		document.futureFclEditForm.submit();
		}cancelForm()
		function cancelForm()
		{
		
		document.futureFclEditForm.buttonValue.value="cancel";
		document.futureFclEditForm.submit();
		}
		function disabled(val1,val2) {
		
		
			if(val1!=""&&(val1 == 0 || val1== 3))
			{
		  	  	 var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id != "cancel")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
		   		var input = document.getElementsByTagName("input");
			   		for(i=0; i<input.length; i++)	{
			  			input[i].readOnly=true;
					 	}
		  	 	var textarea = document.getElementsByTagName("textarea");
			  	 	for(i=0; i<textarea.length; i++){
			 			textarea[i].readOnly=true;		 					
					  	}
	   			var select = document.getElementsByTagName("select");
	   		   		for(i=0; i<select.length; i++)	{
				 		select[i].disabled=true;
						select[i].style.backgroundColor="blue";
		  		  	 	}
		  		  	 	document.getElementById("delete").style.visibility = 'hidden';
		  		  	 	document.getElementById("save").style.visibility = 'hidden';
	  	 		}
	  	 		
		  	 if(val1 == 1)	 {
		  		 document.futureFclEditForm.standard.disabled=true
	  	 		 	 document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 	if(val1==3 && val2!=""){
				
				alert(val2);
				
				}		
    		}
		</script> 
<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
<html:form  action="/futureFclEdit"   name="futureFclEditForm" type="com.gp.cong.logisoft.struts.ratemangement.form.FutureFclEditForm" scope="request">
<%--<div align="right">--%>
<%--	<img src="<%=path%>/img/save.gif" onclick="addForm()"/>--%>
<%-- 	<img src="<%=path%>/img/cancel.gif"" border="0" id="cancel" onclick="cancelForm()"/>--%>
<%--	<img src="<%=path%>/img/delete.gif" border="0" onclick="confirmdelete()" id="delete"/>--%>
<%--</div>--%>
  <table border="0" class="tableBorderNew" width="100%">

  		<tr class="tableHeadingNew" height="90%">Edit FCL Rate </tr>
<tr class="textlabels">


<%
	if(costType!=null && costType.trim().equalsIgnoreCase("Flat Rate Per Container Size"))
{
%>
	
	 <td >Unit Type</td>
	  <td ><html:text property="unittype" readonly="true"  size="7" value="<%=unitycode%>"/></td>
	 <td >Amount</td>
	 <td ><html:text property="amount" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  value="<%=activeAmt%>"/></td>
	<td >Markup</td>
	 <td ><html:text property="markup"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"     value="<%=markup %>"/></td>
	   <td >Standard</td>
	 <td ><html:checkbox property="standard" name="fclrecordform" ></html:checkbox></td>
<% 
}
	else if(costType!=null && costType.trim().equalsIgnoreCase("Per CBM")||costType.trim().equalsIgnoreCase("per LBS")||costType.trim().equalsIgnoreCase("Per 1000KG"))
	{
%>  <td >Retail</td>
	  <td><html:text property="pcretail"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   styleClass="verysmalldropdownStyle" value="<%=ratAmount %>"/></td>
	 <td >CTC</td>
	  <td><html:text property="pcctc" onkeypress="check(this,4)"  onblur="checkdec(this)" maxlength="7" size="7"   styleClass="verysmalldropdownStyle" value="<%=ctcAmt %>" /></td>
	<td >FTF</td>
	  <td><html:text property="pcftf" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   styleClass="verysmalldropdownStyle"  value="<%=ftfAmt%>"/></td>
	<td >Minimun</td>
	  <td><html:text property="pcminimun" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  styleClass="verysmalldropdownStyle"  value="<%=minimumAmt%>"/></td>
	  <td colspan="3">&nbsp;</td>
  
<%
	}
	else if(costType!=null && costType.trim().equalsIgnoreCase("PER 2000 LBS") || costType.trim().equalsIgnoreCase("PERCENT OFR") || costType.trim().equalsIgnoreCase("Per Dock Receipts")||costType.trim().equalsIgnoreCase("Per Cubic Foot")||costType.trim().equalsIgnoreCase("Per BL Charges") ||costType.trim().equalsIgnoreCase("Flat Rate Per Container"))
 {
%>		<td >Retail</td>
	 <td><html:text property="pcretail" onkeypress="check(this,4)"  onblur="checkdec(this)" maxlength="7" size="7"   styleClass="verysmalldropdownStyle"  value="<%=ratAmount%>"/></td>
 
<%
}
else if(costType!=null && costType.trim().equalsIgnoreCase("Air Freight Costs"))
{
%> <td ><span class="textlabels">Air Wight Range </span></td>
	 <td ><html:text property="wightrange"  readonly="true" styleClass="verysmalldropdownStyle"  value="<%=wightRange%>"/></td>
    <td><span class="textlabels">Air Freight Amount </span></td>
    <td ><html:text property="amount"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  styleClass="verysmalldropdownStyle"  value="<%=rateAmount%>"/></td>
 
<%
	}
 %>
 </tr><tr class="textlabels">
  <td >Effective Date</td>
	<td>
						<html:text property="efdate" styleId="txtEFDAitemcreatedon" styleClass="verysmalldropdownStyle"
							size="7" readonly="true" value="<%=efd %>" />
							
							<img src="<%=path%>/img/CalendarIco.gif" alt="cal" 
								id="EFDAitemcreatedon"
								onmousedown="insertDateFromCalendar(this.id,0);" />
						
					</td>
					<td width="94">Currency</td>
	<td colspan="4" align="left" width="50%">&nbsp;<html:select property="cuurency" value="<%=currency%>"> 
      <html:optionsCollection name="defaultcurrency" /> 
      </html:select>  
      
            </td>
 	  <td align="left" class="headerbluelarge">
 	 		 <input type="button" value="Save" id="save" onclick="addForm()"  class="buttonStyleNew" />
 	  </td>
	  <td  align="left" class="headerbluelarge">
	  		<input type="button" value="Go Back" id="cancel" class="buttonStyleNew" onclick="cancelForm()"/>
	  </td>
	  <td>
	  		<input type="button" value="Delete" onclick="confirmdelete()" id="delete" class="buttonStyleNew" />
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

