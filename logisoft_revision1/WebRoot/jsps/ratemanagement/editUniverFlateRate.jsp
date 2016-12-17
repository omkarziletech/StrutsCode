
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.UniverseFlatRate,
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
String unittype="";
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
if(session.getAttribute("edituniflatrate")!=null){
UniverseFlatRate universeFlatRate=(UniverseFlatRate)session.getAttribute("edituniflatrate");
if(universeFlatRate!=null){
	if(universeFlatRate.getUnitType()!=null){
	unittype=universeFlatRate.getUnitType().getCodedesc();
	}if(universeFlatRate.getAmount()!=null){
	amount=df.format(universeFlatRate.getAmount());
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
			document.editUniverFlateRateForm.buttonValue.value="add";
			document.editUniverFlateRateForm.submit();
		}
	function delete1(){
			document.editUniverFlateRateForm.buttonValue.value="delete";
			document.editUniverFlateRateForm.submit();
	}
	 function cancelbtn(){
	 document.editUniverFlateRateForm.buttonValue.value="cancel";
			document.editUniverFlateRateForm.submit();
	 }
   		
   		function submit()
		{
		document.editUniverFlateRateForm.buttonValue.value="";
			document.editUniverFlateRateForm.submit();
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
			document.editUniverFlateRateForm.submit();
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
		<html:form action="/editUniverFlateRate" name="editUniverFlateRateForm" type="com.gp.cong.logisoft.struts.ratemangement.form.EditUniverFlateRateForm" scope="request">
	 <div align="left">
    			         <input type="button" value="Go Back" onclick="uniAirFright()" id="cancel" class="buttonStyleNew" />
			             <input type="button" value="Delete" onclick="delete1()" id="delete" class="buttonStyleNew" />
		</div>		      
 <table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
  		 
  		<tr class="tableHeadingNew"> Flat Rate Per Unit </tr>
  		<tr>
  		 <td>
  		
		<table><tr class="textlabels">
		<td>Unit Size Type </td>
		
		<td>Amount </td>
		</tr><tr>
			<td width="105"><html:text property="startrange" size="7"  readonly="true" value="<%=unittype%>"/></td>
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







 	
			
