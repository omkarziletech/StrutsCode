<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.FTFDocumentCharges,com.gp.cong.logisoft.domain.FTFMaster"%>
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
FTFMaster ftfMaster=new FTFMaster();
FTFDocumentCharges ftfDocument=new FTFDocumentCharges();

List docList=new ArrayList();
if(session.getAttribute("ftfdocumentCharges")!=null )
{
	ftfDocument = (FTFDocumentCharges)session.getAttribute("ftfdocumentCharges");
	if(ftfDocument!=null)
	{
		chargecode=ftfDocument.getChargeCode().getCode();
		codeDesc=ftfDocument.getChargeCode().getCodedesc();
	}
	if(ftfDocument.getAmount()!=null)
	{
	amount=df.format(ftfDocument.getAmount());
	}
}
if(session.getAttribute("addftfMaster")!=null)
{
	ftfMaster=(FTFMaster)session.getAttribute("addftfMaster");
	if(ftfMaster.getMaxDocCharge()!=null)
	{
	maxDocCharge=df.format(ftfMaster.getMaxDocCharge());
	
	}
	if(ftfMaster.getBlBottomLine()!=null)
	{
	blBottomLine=df.format(ftfMaster.getBlBottomLine());
	
	}
	if(ftfMaster.getFfCommission()!=null)
	{
	ffCommission=df.format(ftfMaster.getFfCommission());
	
	}
}
if(session.getAttribute("ftfdocChargesAddFoeEdit") != null)
{
	docList = (List) session.getAttribute("ftfdocChargesAddFoeEdit");
	session.setAttribute("ftfdocchargesedit",docList);
	request.setAttribute("docList",docList);
}
String editPath=path+"/editDocumentChargesFTF.do";
String modify="";
modify = (String) session.getAttribute("modifyforftfRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}


%>
 
<html> 
	<head>
		<title>JSP for EditDocumentChargesFTFForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function add()
		{
		document.editDocumentChargesFTFForm.buttonValue.value="add";
  			document.editDocumentChargesFTFForm.submit();
		}
		function delete1()
		{
		document.editDocumentChargesFTFForm.buttonValue.value="delete";
  			document.editDocumentChargesFTFForm.submit();
		}
		function cancelbtn()
		{
		document.editDocumentChargesFTFForm.buttonValue.value="cancel";
  		document.editDocumentChargesFTFForm.submit();
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
	<body class="whitebackgrnd"  onLoad="disabled('<%=modify%>')">
		<html:form action="/editDocumentChargesFTF" name="editDocumentChargesFTFForm" type="com.gp.cong.logisoft.struts.ratemangement.form.EditDocumentChargesFTFForm" scope="request">
	
<table width="100%" class="tableBorderNew" border="0">
   <tr class="tableHeadingNew"> Document Charges </tr>
   <tr>
     <td>
        
    	
	<table>
		<tr>
		<td width="80%">
		</td>
			<td align="right">
					<input type="button" value="Add" class="buttonStyleNew" onclick="add()"/>
					
		</td>
			<td align="right">
				<input type="button" value="Delete" class="buttonStyleNew" onclick="delete1()" id="delete"/>
		   </td>
			<td align="right">
				<input type="button" value="Go Back" class="buttonStyleNew" onclick="cancelbtn()" id="cancel"/>
	      </td>
		</tr>
		</table>
	
		<table>
		<tr>
      		<td class="textlabels">Max Doc Charge</td>
      		<td class="textlabels">FF Commission</td>
      		<td class="textlabels">BL Bottom Line </td>
      	</tr>
      	<tr>
      		<td><html:text property="maxDocCharge" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="8"  styleId="maxDocCharge" value="<%=maxDocCharge%>"/></td>
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
		
    </td>
   </tr>
</table>		
		<html:hidden property="buttonValue" styleId="buttonValue"/>	

		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

