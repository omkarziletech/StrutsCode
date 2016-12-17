
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.UniverseInsuranceChrg,
com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,
java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:directive.page import = "java.text.DecimalFormat"/>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
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
getObj=(String)session.getAttribute("rangeObj");

if(session.getAttribute("uniinsurancelist")!=null )
{
ariFrightList=(List)session.getAttribute("uniinsurancelist");
	
 }


	   
String insurance="0";

String modify="";


	
String msg="";
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
		function addAGSS()
		{
		
			document.universalInsuranceForm.buttonValue.value="add";
			document.universalInsuranceForm.submit();
		}
	/*function costtypechange()
		{
		
			if(document.universalInsuranceForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else{
				document.universalInsuranceForm.buttonValue.value="costType";
				document.universalInsuranceForm.submit();
			}
		}*/
   		function submit()
		{
		document.universalInsuranceForm.buttonValue.value="";
			document.universalInsuranceForm.submit();
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
			document.universalInsuranceForm.submit();
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
  	 	 document.getElementById("add").style.visibility = 'hidden';
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 var tables1 = document.getElementById('universalInsurance');
		
  		 if(tables1!=null)
  		 {
  	 	// displaytagcolor();
   		// initRowHighlighting();
   		// setWarehouseStyle();
   		 }		
    }function displaytagcolor()
		{
		 	 	var datatableobj1 = document.getElementById('universalInsurance');
		
				for(i=0; i<datatableobj1.rows.length; i++)
				{
					var tablerowobj = datatableobj1.rows[i];
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
					
		 	 	}
		}
	function initRowHighlighting()
  	{
			if (!document.getElementById('universalInsurance')){ return; }
			 var tables = document.getElementById('universalInsurance');
			attachRowMouseEvents(tables.rows);
		
	}
	function attachRowMouseEvents(rows)
	{
		for(var i =1; i < rows.length; i++)
		{
			var row = rows[i];
			row.onmouseover =	function() 
			{ 
				this.className = 'rowin';
			}
			row.onmouseout =	function() 
			{ 
				this.className = '';
			}
            row.onclick= function() 
			{ 
			}
			}
		
		}
		function addItem()
		{
		    document.universalInsuranceForm.buttonValue.value="addItem";
			document.universalInsuranceForm.submit();
		}
	</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/universalInsurance" name="universalInsuranceForm" type="com.gp.cong.logisoft.struts.ratemangement.form.UniversalInsuranceForm" scope="request">
 
 <table width="100%" border="0" class="tableBorderNew" cellpadding="0" cellspacing="0" >
    <tr class="tableHeadingNew"> <td>Insurance Charges</td>
    	<td align="right">
    		     <input type="button" value="Add" onclick="addItem()" id="add" class="buttonStyleNew" />
  		</td>
     </tr>
    <tr>
     <td colspan="2">
     
 
 <table width=100% border="0" cellpadding="0" cellspacing="0">
  		 <tr>
 <%if(session.getAttribute("uniInsuranceAdd")==null){ %>
    	<%}else{ %>
  		</tr>
  		
		</table>
		<table><tr class="textlabels">
		<td>Insurance Amount</td>
		
		<td>Per Valuation </td>
		</tr><tr>
    <td width="105"><html:text property="amount"   onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>			
			    <td width="105"><html:text property="pervalue"   onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>
			   <td align="right">
			       <input type="button" value="Add To List" onclick="addAGSS()" id="save" class="buttonStyleNew" />
			   </td>
			 </tr>
		
       <%} %>
	</table>
			
			
			<table width="100%">
	<tr height="5">
    </tr>  
	<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable" style="height:120px">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        
        %>
        <display:table name="<%=ariFrightList%>" pagesize="<%=pageSize%>"   class="displaytagstyle"  sort="list" id="universalInsurance"> 
			<%
				String tempPath="";
				String wightrange="";

				String iStr=String.valueOf(i);
				
  					tempPath=editPath+"?ind="+iStr;
  					if(ariFrightList!=null && ariFrightList.size()>0){
  					UniverseInsuranceChrg universeInsuranceChrg=(UniverseInsuranceChrg)ariFrightList.get(i);
  					if(universeInsuranceChrg.getInsuranceAmount()!=null){
  					insurance=df.format(universeInsuranceChrg.getInsuranceAmount());
  					}
  					if(universeInsuranceChrg.getPerValue()!=null){
  					amount=df.format(universeInsuranceChrg.getPerValue());
  					}
  					}
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
  			
  			
			<display:column  paramId="param" paramProperty="id"  title="Insurance Amount &nbsp; &nbsp;" ><a href="<%=tempPath%>"><%=insurance%></a></display:column>
			<display:column  title="Per Valuation"><%=amount%></display:column>
			
  			<% i++;
  		   	   
  			%>
		     	    		
       		
		</display:table>
        
        </table></div>  
    	</td> 
   </tr>  
</table>
	 </td>
    </tr>
    
 </table>		
	
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<html:hidden property="index" />
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>







 	
			
