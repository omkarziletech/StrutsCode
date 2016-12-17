
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.UniverseInsuranceChrg,
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

java.util.Date date = new java.util.Date();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
String chargecode=null;
String codeDesc=null;
List ariFrightList=new ArrayList();
List startrange=new ArrayList();
List airList=new ArrayList();
String getObj=null;
String amount="";
String insurance="";
getObj=(String)session.getAttribute("rangeObj");


if(session.getAttribute("edituniinsurance")!=null){
UniverseInsuranceChrg universeInsuranceChrg=(UniverseInsuranceChrg)session.getAttribute("edituniinsurance");
  					if(universeInsuranceChrg.getInsuranceAmount()!=null){
  					insurance=df.format(universeInsuranceChrg.getInsuranceAmount());
  					}
  					if(universeInsuranceChrg.getPerValue()!=null){
  					amount=df.format(universeInsuranceChrg.getPerValue());
  					}

}
	   


String modify="";

if(request.getAttribute("message")!=null)
{
	modify=(String)request.getAttribute("message");
}
modify = (String) session.getAttribute("modifyforlclcoloadRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/universalInsurance.do";


%>
 
<html> 
	<head>
		<title>JSP for AGSSForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function uniInsurance()
		{
			document.editUniversalInsuranceForm.buttonValue.value="add";
			document.editUniversalInsuranceForm.submit();
		}
		function delete1(){
				document.editUniversalInsuranceForm.buttonValue.value="delete";
				document.editUniversalInsuranceForm.submit();
		}
		 function cancelbtn(){
		 document.editUniversalInsuranceForm.buttonValue.value="cancel";
				document.editUniversalInsuranceForm.submit();
		 }
		
   		function submit()
		{
		document.editUniversalInsuranceForm.buttonValue.value="";
			document.editUniversalInsuranceForm.submit();
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
			document.editUniversalInsuranceForm.submit();
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
		<html:form action="/editUniversalInsurance" name="editUniversalInsuranceForm" type="com.gp.cong.logisoft.struts.ratemangement.form.EditUniversalInsuranceForm" scope="request">
	
<div align="left">	 
	<input type="button" value="Go Back" onclick="uniInsurance()" id="cancel" class="buttonStyleNew" />
	<input type="button" value="Delete" onclick="delete1()" id="delete" class="buttonStyleNew" />
	</div>		
<table width="100%" class="tableBorderNew" border="0">
  <tr class="tableHeadingNew"> Insurance Charges </tr>
 		
		<tr class="textlabels">
		<td>Insurance Amount</td>
		
		<td>Per Valuation </td>
		</tr><tr>
    <td width="105"><html:text property="amount"   onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value="<%=insurance %>"/></td>			
			    <td width="105"><html:text property="pervalue"   onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value="<%=amount %>"/></td>
			   <td align="right">
			
			 </tr>
		
       
	</table>
			
			
	
			
	
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<html:hidden property="index" />
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>







 	
			
