<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.AirFreightDocumentCharges,com.gp.cong.logisoft.domain.StandardCharges"%>
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
String amount="";
StandardCharges standardChrg=new StandardCharges();
AirFreightDocumentCharges airDocument=new AirFreightDocumentCharges();

List docList=new ArrayList();
if(session.getAttribute("documentCharges")!=null )
{
	airDocument = (AirFreightDocumentCharges)session.getAttribute("documentCharges");
	if(airDocument!=null)
	{
		chargecode=airDocument.getChargeCode().getCode();
		codeDesc=airDocument.getChargeCode().getCodedesc();
	}
	if(airDocument.getAmount()!=null)
	{
	amount=df.format(airDocument.getAmount());
	}
}
if(session.getAttribute("standardCharges")!=null)
{
standardChrg=(StandardCharges)session.getAttribute("standardCharges");
if(standardChrg.getMaxDocCharge()!=null)
{
maxDocCharge=df.format(standardChrg.getMaxDocCharge());
}
if(standardChrg.getBlBottomLine()!=null)
{
blBottomLine=df.format(standardChrg.getBlBottomLine());
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
String editPath=path+"/docChargesEdit.do";
String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}


%>
 
<html> 
	<head>
		<title>JSP for DocChargesEditForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function add()
		{
		document.docChargesEditForm.buttonValue.value="add";
  			document.docChargesEditForm.submit();
		}
		function delete1()
		{
		document.docChargesEditForm.buttonValue.value="delete";
  			document.docChargesEditForm.submit();
		}
		function cancelbtn()
		{
		document.docChargesEditForm.buttonValue.value="cancel";
  		document.docChargesEditForm.submit();
		}
		function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   			if(imgs[k].id!="cancel")
   			{
   		    	imgs[k].style.visibility = 'hidden';
   		    }
   		 
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
		</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/docChargesEdit" scope="request">
         <table width="100%" class="tableBorderNew" border="0" cellpadding="0" cellspacing="0">
         <tr class="tableHeadingNew"><td>Document Charges</td>
         	<td align="right" colspan="2">
         	 <input type="button" value="Add" id="save" onclick="add()" class="buttonStyleNew"/>
         	 <input type="button" value="Delete" id="delete" onclick="delete1()" class="buttonStyleNew"/>
         	 <input type="button" value="Go Back" id="cancel" onclick="cancelbtn()" class="buttonStyleNew"/>
            </td>
        </tr>
		<tr>
      		<td class="textlabels">Max Doc Charge</td>
      		<td class="textlabels">FF Commission</td>
      		<td class="textlabels">BL Bottom Line </td>
      	</tr>
      	<tr>
      		<td><html:text property="maxDocCharge" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="8"   styleId="maxDocCharge" value="<%=maxDocCharge%>"/></td>
   		 	<td><html:text property="ffCommision" onkeypress="check(this,2)" onblur="checkdec(this)" maxlength="5" size="8"   styleId="ffCommision" value="<%=ffCommission%>"/></td>
   		 	<td><html:text property="blBottomLine" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="blBottomLine" value="<%=blBottomLine%>"/></td>
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

