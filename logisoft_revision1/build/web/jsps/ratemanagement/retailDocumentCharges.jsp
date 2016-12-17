<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.text.DateFormat,java.text.SimpleDateFormat,java.util.*,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.RetailFreightDocumentCharges,com.gp.cong.logisoft.domain.RetailStandardCharges"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


String chargecode="";
String codeDesc="";
String msg="";
String maxDocCharge="";
String ffCommission="";
String blBottomLine="";
String costCBM="";
DecimalFormat df = new DecimalFormat("0.00");

List docList=new ArrayList();
String amount="";
String modify="";
RetailFreightDocumentCharges documentCharges=null;

if(session.getAttribute("retaildocumentCharges")!=null )
{
    documentCharges=(RetailFreightDocumentCharges)session.getAttribute("retaildocumentCharges");
	if(documentCharges!=null)
	{
      
			chargecode=documentCharges.getChargeCode().getCode();
		
		codeDesc=documentCharges.getChargeCode().getCodedesc();
	}
	if(documentCharges.getAmount()!=null)
	{
	amount=documentCharges.getAmount().toString();
	}
}
RetailStandardCharges standardChrg=new RetailStandardCharges();

if(session.getAttribute("retailstandardCharges")!=null)
{
	standardChrg=(RetailStandardCharges)session.getAttribute("retailstandardCharges");
	if(standardChrg.getMaxDocCharge()!=null)
	{
	maxDocCharge=df.format(standardChrg.getMaxDocCharge());
	}
	if(standardChrg.getFfCommission()!=null)
	{
	ffCommission=df.format(standardChrg.getFfCommission());
	}
	if(standardChrg.getBlBottomLine()!=null)
	{
	blBottomLine=df.format(standardChrg.getBlBottomLine());
	}
	if(standardChrg.getCostCbm()!=null){
	costCBM=df.format(standardChrg.getCostCbm());
	}	
}
if(session.getAttribute("retaildocChargesAdd") != null)
{
	docList = (List) session.getAttribute("retaildocChargesAdd");
	
}
modify = (String) session.getAttribute("modifyforretailRates");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/retailDocumentCharges.do";

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
		<title>JSP for RetailDocumentChargesForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		   dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		<script language="javascript" type="text/javascript">
	   
	function addDocumentCharges(){
		if(document.retailDocumentChargesForm.charge.value==""){
			alert("Please select Charge code");
			return;
		}
		document.retailDocumentChargesForm.buttonValue.value="add";
  		document.retailDocumentChargesForm.submit();
	 }
   function confirmdelete(obj){
			var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('agsstable').rows[rowindex].cells;	
	   		document.retailDocumentChargesForm.index.value=rowindex-1;
			document.retailDocumentChargesForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this value");
			if(result){
   				document.retailDocumentChargesForm.submit();
   			}	
   }
  function disabled(val1){
	if(val1 == 0 || val1== 3){
        var imgs = document.getElementsByTagName('img');
   		for(var k=0; k<imgs.length; k++){
   		    imgs[k].style.visibility = 'hidden';
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++){
	  			input[i].className="areahighlightgreysmall";
	  			input[i].readOnly=true;
  	 	}
  	 	var textarea = document.getElementsByTagName("textarea");
  	 	for(i=0; i<textarea.length; i++){
	 			textarea[i].readOnly=true;
	  	}
   		var select = document.getElementsByTagName("select");
   		for(i=0; i<select.length; i++){
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
  	 	}
  	 }
  	 if(val1 == 1){
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }	
   }
  function getCodeDesc(ev){
		if(event.keyCode==9){
		 document.retailDocumentChargesForm.buttonValue.value="chargeCode";
		 document.retailDocumentChargesForm.submit();
		}
   }
  function getCodeDescPress(ev){
		if(event.keyCode==13){
		  document.retailDocumentChargesForm.buttonValue.value="chargeCode";
		  document.retailDocumentChargesForm.submit();
		}
   }
   
   		    function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.retailDocumentChargesForm.charge.value;
			params['codeType'] ='35' ;
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
			params['codeDesc'] = document.retailDocumentChargesForm.desc.value;
			params['codeType'] ='35' ;
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
		<body class="whitebackgrnd" >
	<%}
	else{ %>
		<body class="whitebackgrnd">
	<%} %>
		
		
	<html:form action="/retailDocumentCharges" name="retailDocumentChargesForm" type="com.gp.cong.logisoft.struts.ratemangement.form.RetailDocumentChargesForm" scope="request">
			
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px">
	<tr class="tableHeadingNew">Document Charges</tr>
     <td>
		<table width=100% border="0" cellpadding="0" cellspacing="2">
			<tr class="textlabels">
	      		<td width="20%">Max Doc Charge</td>
	      		<td width="20%" >FF Commision</td>
	      		<td width="20%" >BL Bottom Line </td>
	      		<td width="20%" >Cost/CBM</td>
	      		<td width="20%"></td>
	      	</tr>
	      	<tr>
	      		<td><html:text property="maxDocCharge" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="8"   styleId="maxDocCharge" value="<%=maxDocCharge%>"/></td>
	   		 	<td><html:text property="ffCommision" onkeypress="check(this,2)" onblur="checkdec(this)" maxlength="5" size="8"   styleId="ffCommision" value="<%=ffCommission%>"/></td>
	   		 	<td><html:text property="blBottomLine" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="blBottomLine" value="<%=blBottomLine%>"/></td>
	      	    <td><html:text property="cocbm" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="CostCbm" value="<%=costCBM%>"/></td>
	      	</tr>
	   </table>
	   <table width="100%"  border="0"  cellpadding="0" cellspacing="0">
			<tr class="textlabels">
				<td width="20%">Chrg Code</td>
				<td width="20%">Charge Desc </td>
				<td width="20%">&nbsp;Amt</td>
				<td width="40%"></td>
			</tr>
			<tr class="textlabels">
 		 		 <td><input name="charge"  id="charge" value="<%=chargecode%>" onkeydown="getdestPort(this.value)"  size="3"/>
	           		 <dojo:autoComplete  formId=retailDocumentChargesForm textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=RETAIL_DOCUMENT_CHARGES&from=0"/></td>
   		 		 <td>
<%--   		 		 <html:text property="desc" value="<%=codeDesc%>"  size="12"/>--%>
   		 	  		 <input name="desc"  id="desc" value="<%=codeDesc%>" onkeydown="getdestPort1(this.value)"  size="12"/>
	          		 <dojo:autoComplete  formId="retailDocumentChargesForm" textboxId="desc" action="<%=path%>/actions/getChargeCode.jsp?tabName=RETAIL_DOCUMENT_CHARGES&from=1"/>
   		 	    </td>
   		  		<td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
   		  		<td align="left">
   		  		 	 <input type="button" value="Add" class="buttonStyleNew" id="add" onclick="addDocumentCharges()"/>
   		  		</td>
		   </tr>
	  </table>
	 <table width="100%">
	 <tr>
	  <td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        %>
        <display:table name="<%=docList%>" pagesize="20" class="displaytagstyle"  sort="list" id="agsstable"> 
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
			<%
				
				String chargeCode="";
				String chargeDesc="";
				String tempPath="";
			    if(docList != null && docList.size()>0)
			    {
					RetailFreightDocumentCharges docCharges=(RetailFreightDocumentCharges)docList.get(i);
					if(docCharges.getChargeCode()!=null)
					{
					chargeCode=docCharges.getChargeCode().getCode();
					}
					if(docCharges.getChargeCode()!=null)
					{
					chargeDesc=docCharges.getChargeCode().getCodedesc();
					}
					if(docCharges.getAmount()!=null)
					{
						amount=df.format(docCharges.getAmount());
					}
					docCharges.setIndex(i);
					String iStr=String.valueOf(i);
  					tempPath=editPath+"?index="+iStr;
				}
			 %>
			
  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Charge Code">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=tempPath%>"><%=chargeCode %></a> </display:column>
  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Charge Description"  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=chargeDesc %></display:column>
			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=amount %></display:column>
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
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

