<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.RetailFreightDocumentCharges,com.gp.cong.logisoft.domain.RetailStandardCharges"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
DecimalFormat df = new DecimalFormat("0.00");

String chargecode=null;
String codeDesc=null;
String maxDocCharge="";
String ffCommission="";
String blBottomLine="";
String costCBM="";
String amount="";
RetailStandardCharges standardChrg=new RetailStandardCharges();
RetailFreightDocumentCharges retailDocument=new RetailFreightDocumentCharges();

List docList=new ArrayList();
if(session.getAttribute("retaildocumentCharges")!=null )
{
	retailDocument = (RetailFreightDocumentCharges)session.getAttribute("retaildocumentCharges");
	if(retailDocument!=null)
	{
		chargecode=retailDocument.getChargeCode().getCode();
		codeDesc=retailDocument.getChargeCode().getCodedesc();
	}
	if(retailDocument.getAmount()!=null)
	{
	amount=df.format(retailDocument.getAmount());
	}
}
if(session.getAttribute("retailstandardCharges")!=null)
{
standardChrg=(RetailStandardCharges)session.getAttribute("retailstandardCharges");
if(standardChrg.getMaxDocCharge()!=null)
{
maxDocCharge=df.format(standardChrg.getMaxDocCharge());
}
if(standardChrg.getBlBottomLine()!=null)
{
blBottomLine=df.format(standardChrg.getBlBottomLine());
}
if(standardChrg.getCostCbm()!=null)
{
costCBM=df.format(standardChrg.getCostCbm());
}
if(standardChrg.getFfCommission()!=null)
{
ffCommission=df.format(standardChrg.getFfCommission());
}
}
if(session.getAttribute("docChargesAddFoeEdit") != null)
{
	docList = (List) session.getAttribute("docChargesAddFoeEdit");
	session.setAttribute("docchargesedit",docList);
	request.setAttribute("docList",docList);
}


String modify="";
modify = (String) session.getAttribute("modifyforretailRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}

String editPath=path+"/retailEditDocCharges.do";



%>

 
<html> 
	<head>
		<title>JSP for RetailEditDocChargesForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function add()
		{
		document.retailEditDocChargesForm.buttonValue.value="add";
  			document.retailEditDocChargesForm.submit();
		}
		function delete1()
		{
		document.retailEditDocChargesForm.buttonValue.value="delete";
  			document.retailEditDocChargesForm.submit();
		}
		function cancelbtn()
		{
		document.retailEditDocChargesForm.buttonValue.value="cancel";
  		document.retailEditDocChargesForm.submit();
		}
		
		function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		if(imgs[k].id != "cancel")
   		{
   		
   		    imgs[k].style.visibility = 'hidden';
   		 
   		}
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			input[i].className="areahighlightgreysmall";
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
	
		</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/retailEditDocCharges" scope="request">
			
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px">
	  <tr class="tableHeadingNew"><td>Document Charges</td>
	  		<td style="paddingleft:200px;"></td>
	  		<td style="paddingleft:200px;"></td>
			<td  align="right">
				<input type="button" value="Update" onclick="add()" class="buttonStyleNew">
				<input type="button" value="Delete" onclick="delete1()" () class="buttonStyleNew">
				<input type="button" value="Go Back" id="cancel" onclick="cancelbtn()" class="buttonStyleNew" >
			</td>
	  </tr>
		<tr>
      		<td class="textlabels">Max Doc Charge</td>
      		<td class="textlabels">FF Commission</td>
      		<td class="textlabels">BL Bottom Line </td>
      		<td class="textlabels">Cost/CBM </td>
      	</tr>
      	<tr>
      		<td><html:text property="maxDocCharge" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="8"   styleId="maxDocCharge" value="<%=maxDocCharge%>"/></td>
   		 	<td><html:text property="ffCommision" onkeypress="check(this,2)"  onblur="checkdec(this)" maxlength="5" size="8"   styleId="ffCommision" value="<%=ffCommission%>"/></td>
   		 	<td><html:text property="blBottomLine" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="blBottomLine" value="<%=blBottomLine%>"/></td>
   		 	<td><html:text property="cocbm" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="CostCbm" value="<%=costCBM%>"/></td>
      	</tr>
      	<tr class="textlabels">
				<td>Chrg Code</td>
				<td>Charge Desc </td>
				<td>Amt</td>
			</tr>
		<tr  class="textlabels">
 		 		<td ><html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3"/></td>
   		 		<td ><html:text property="desc" value="<%=codeDesc%>" readonly="true" styleClass="varysizeareahighlightgrey" size="12"/></td>
   		  		<td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
   		  		
			</tr>
		</table>
		<html:hidden property="buttonValue" styleId="buttonValue"/>	
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

