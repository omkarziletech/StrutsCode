
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.UniverseAirFreight,
com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,
java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:directive.page import = "java.text.DecimalFormat"/>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String wightrange="";
String amount="";
DecimalFormat df = new DecimalFormat("0.00");
List startrange=new ArrayList();
List endrange=new ArrayList();
session.setAttribute("startrange",startrange);
session.setAttribute("endrange",endrange);
String modify="";
if(request.getAttribute("message")!=null)
{
	modify=(String)request.getAttribute("message");
}
modify = (String) session.getAttribute("modifyforlclcoloadRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/universalAirRate.do";
if(session.getAttribute("edituniarifright")!=null){
UniverseAirFreight universeAirFreight=(UniverseAirFreight)session.getAttribute("edituniarifright");
if(universeAirFreight!=null){
	if(universeAirFreight.getWeightRange()!=null){
	wightrange=universeAirFreight.getWeightRange().getCodedesc();
	}if(universeAirFreight.getAmount()!=null){
	amount=df.format(universeAirFreight.getAmount());
	}

}
}

%>
 
<html> 
	<head>
		<title>JSP for AGSSForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function uniAirFright()
		{
			document.editUniverAirFrightForm.buttonValue.value="add";
			document.editUniverAirFrightForm.submit();
		}
	function delete1(){
			document.editUniverAirFrightForm.buttonValue.value="delete";
			document.editUniverAirFrightForm.submit();
	}
	 function cancelbtn(){
	 document.editUniverAirFrightForm.buttonValue.value="cancel";
			document.editUniverAirFrightForm.submit();
	 }
   		
   		function submit()
		{
		document.editUniverAirFrightForm.buttonValue.value="";
			document.editUniverAirFrightForm.submit();
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
			document.editUniverAirFrightForm.submit();
			return false;
		}
		

   		
		function disabled(val1)
   		{
	if(val1!=""&&(val1 == 0 || val1== 3))
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		
   		    if(imgs[k].id != "cancel")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
   		   
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			//input[i].className="areahighlightgreysmall";
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
  	 	 document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
		
	</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/editUniverAirFright" name="editUniverAirFrightForm" type="com.gp.cong.logisoft.struts.ratemangement.form.EditUniverAirFrightForm" scope="request">
		<div align="left">
		 <input type="button" value="Go Back" onclick="uniAirFright()" id="cancel" class="buttonStyleNew" />
		<input type="button" value="Delete" onclick="delete1()" id="delete" class="buttonStyleNew" />
			</div>
<table width="100%" class="tableBorderNew" border="0">
  	<tr class="tableHeadingNew"> Universal Air Fright Rates </tr>
  <tr>
    <td>
   
		
 <table width=100% border="0" cellpadding="0" cellspacing="0">
  		 <tr>
    		<td></td>
  		</tr>
  	
		</table>
		<table><tr class="textlabels">
		<td>Wight Range</td>
		
		<td>Amount </td>
		</tr><tr>
			<td width="105"><html:text property="startrange" size="7" readonly="true" value="<%=wightrange%>"/></td>
			<td width="105"><html:text property="amount"   onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value="<%=amount %>"/></td>
			   <td align="right">
			
			 </tr>
		
       
	</table>
	 </td>
  </tr>
  
</table>		
	<html:hidden property="buttonValue"/>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>







 	
			
