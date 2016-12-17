<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.text.DateFormat,java.text.SimpleDateFormat,java.util.*,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FTFDocumentCharges,com.gp.cong.logisoft.domain.AirFreightDocumentCharges,com.gp.cong.logisoft.domain.FTFMaster"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


String chargecode="";
String codeDesc="";
String maxDocCharge="";
String ffCommission="";
String blBottomLine="";
DecimalFormat df = new DecimalFormat("0.00");
List docList=new ArrayList();
String amount="";
String msg="";
FTFDocumentCharges documentCharges=null;

if(session.getAttribute("ftfdocumentCharges")!=null )
{
    documentCharges=(FTFDocumentCharges)session.getAttribute("ftfdocumentCharges");
	if(documentCharges!=null)
	{
		chargecode=documentCharges.getChargeCode().getCode();
		codeDesc=documentCharges.getChargeCode().getCodedesc();
	}
	if(documentCharges.getAmount()!=null)
	{
	amount=df.format(documentCharges.getAmount());
	}
}
FTFMaster ftfMaster=new FTFMaster();

if(session.getAttribute("addftfMaster")!=null)
{
	ftfMaster=(FTFMaster)session.getAttribute("addftfMaster");
	if(ftfMaster.getMaxDocCharge()!=null)
	{
	maxDocCharge=df.format(ftfMaster.getMaxDocCharge());
	}
	if(ftfMaster.getFfCommission()!=null)
	{
	ffCommission=df.format(ftfMaster.getFfCommission());
	}
	if(ftfMaster.getBlBottomLine()!=null)
	{
	blBottomLine=df.format(ftfMaster.getBlBottomLine());
	}
}
if(session.getAttribute("ftfdocChargesAdd") != null)
{
	docList = (List) session.getAttribute("ftfdocChargesAdd");
}
String editPath=path+"/documentChargesFTF.do";
String modify="";
modify = (String) session.getAttribute("modifyforftfRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

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
		<title>JSP for DocumentChargesFTFForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		   dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
		<script language="javascript" type="text/javascript">
	   
		function addDocumentCharges()
		{
			
				if(document.documentChargesFTFForm.charge.value=="")
		{
			alert("Please select Charge code");
			return;
		}
		
			document.documentChargesFTFForm.buttonValue.value="add";
  			document.documentChargesFTFForm.submit();
		}
		
		function confirmdelete(obj)
		{
			var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('agsstable').rows[rowindex].cells;	
	   		document.documentChargesFTFForm.index.value=rowindex-1;
			document.documentChargesFTFForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this value");
			if(result)
			{
   				document.documentChargesFTFForm.submit();
   			}	
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
			document.documentChargesFTFForm.submit();
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
		
		function getCodeDesc(ev)
		{
		if(event.keyCode==9)
		{
		 document.documentChargesFTFForm.buttonValue.value="chargeCode";
		 document.documentChargesFTFForm.submit();
		}
		}
	  function getCodeDescPress(ev)
		{
		if(event.keyCode==13)
		{
		 document.documentChargesFTFForm.buttonValue.value="chargeCode";
		 document.documentChargesFTFForm.submit();
		}
		}
		
				    function getdestPort(ev){ 
    document.getElementById("desc").value="";
	
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.documentChargesFTFForm.charge.value;
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
			params['codeDesc'] = document.documentChargesFTFForm.desc.value;
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
	<body class="whitebackgrnd" >
		<html:form action="/documentChargesFTF" name="documentChargesFTFForm" type="com.gp.cong.logisoft.struts.ratemangement.form.DocumentChargesFTFForm" scope="request">
			<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
  			<tr>
    			<td></td>
  			</tr>
  			<tr class="tableHeadingNew"> Document Charges </tr>
  			<tr>
  			  <td>
  			 
		<table>
		<tr>
      		<td class="textlabels">Max Doc Charge</td>
      		<td class="textlabels">FF Commission</td>
      		<td class="textlabels">BL Bottom Line </td>
      	</tr>
      	<tr>
      		<td><html:text property="maxDocCharge" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="15"   styleId="maxDocCharge" value="<%=maxDocCharge%>"/></td>
   		 	<td><html:text property="ffCommision" onkeypress="check(this,2)" onblur="checkdec(this)" maxlength="5" size="15"   styleId="ffCommision" value="<%=ffCommission%>"/></td>
   		 	<td><html:text property="blBottomLine" onkeypress="check(this,5)" onblur="checkdec(this)"maxlength="8" size="15"  styleId="blBottomLine" value="<%=blBottomLine%>"/></td>
      	</tr>
		</table>
		<table width="100%"  border="0"  cellpadding="0" cellspacing="0">
			
		</table>	
		<table>	
			<tr class="textlabels">
				<td>Chrg Code</td>
				<td>Charge Desc </td>
				<td>Amt</td>
			</tr>
			<tr class="textlabels">
 		 		<td >
 		 	 <input name="charge"  id="charge" value="<%=chargecode%>" onkeydown="getdestPort(this.value)"  size="15"/>
	          <dojo:autoComplete  formId="documentChargesFTFForm" textboxId="charge" action="<%=path%>/actions/getChargeCode.jsp?tabName=DOCUMENT_CHARGES_FTF&from=0"/>
	          </td>
 		 		  		 		
   		 		<td >
              <input name="desc"  id="desc" value="<%=codeDesc%>" onkeydown="getdestPort1(this.value)"  size="15"/>
	          <dojo:autoComplete  formId="documentChargesFTFForm" textboxId="desc" action="<%=path%>/actions/getChargeCode.jsp?tabName=DOCUMENT_CHARGES_FTF&from=1"/>
   		 		</td>
   		  		<td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="15"   styleId="amount" value="<%=amount%>"/></td>
   		  		<td align="center">
   		  		    <input type="button" value="Add" onclick="addDocumentCharges()" id="add" class="buttonStyleNew"/>
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
        <display:table name="<%=docList%>" pagesize="20" class="displaytagstyle"  sort="list" id="agsstable" style="width:100%"> 
			<%
				
				String chargeCode=null;
				String chargeDesc=null;
				String tempPath="";
			    if(docList != null && docList.size()>0)
			    {
					FTFDocumentCharges docCharges=(FTFDocumentCharges)docList.get(i);
					
					chargeCode=docCharges.getChargeCode().getCode();
					chargeDesc=docCharges.getChargeCode().getCodedesc();
					docCharges.setIndex(i);
					
					if(docCharges.getAmount()!=null)
						{
						amount=df.format(docCharges.getAmount());
						}
				}
				String iStr=String.valueOf(i);
  					tempPath=editPath+"?index="+iStr;
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
  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Charge Code">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=tempPath%>"><%=chargeCode %></a> </display:column>
  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Charge Description"  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=chargeDesc %></display:column>
			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=amount%></display:column>
			
  			<% i++;
  		   	   
  			%>
		     	    		
       		
		</display:table>
        
        </table></div>  
    	</td> 
  		 </tr>  
		</table>
		
		<html:hidden property="buttonValue" styleId="buttonValue"/>	
        <html:hidden property="index" />
              </td>
  			</tr>
		</table>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

