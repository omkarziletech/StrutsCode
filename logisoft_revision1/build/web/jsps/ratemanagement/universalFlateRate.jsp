
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.UniverseFlatRate,
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
getObj=(String)session.getAttribute("rangeObj");

if(session.getAttribute("unifaltratelist")!=null )
{
ariFrightList=(List)session.getAttribute("unifaltratelist");
	
 }

if(startrange.size()<1 && getObj==null)
		{
			startrange=dbUtil.getUnitFreightListUniTest(new Integer(38),"yes","Select Unit Type ",ariFrightList);
			session.setAttribute("startrange",startrange);//FOR OCEN FRIGHT RIGHT COST CODE FLATE RATE PER CUBIC
			
		}
	   
	   
String chargeType="0";
String effectiveDate="";
String minAmt="";
String amount="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String cft="";
String lbs="";
String cbm="";
String amtkg="",result ="";
String chartypedesc="";
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
String editPath=path+"/universalFlateRate.do";


%>
 
<html> 
	<head>
		<title>JSP for AGSSForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function addAGSS()
		{
		if(document.universalFlateRateForm.startrange.value=="0")
		{
		alert("Please select Unit Type ");
		return;
		}
		
			document.universalFlateRateForm.buttonValue.value="add";
			document.universalFlateRateForm.submit();
		}
	
   		function submit()
		{
		document.universalFlateRateForm.buttonValue.value="";
			document.universalFlateRateForm.submit();
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
			document.universalFlateRateForm.submit();
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
  	 var tables1 = document.getElementById('univarsalAirable');
		
  		 if(tables1!=null)
  		 {
  	 	// displaytagcolor();
   		// initRowHighlighting();
   		// setWarehouseStyle();
   		 }
	  	 				
    }
    function displaytagcolor()
		{
		 	 	var datatableobj1 = document.getElementById('univarsalAirable');
		
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
			if (!document.getElementById('univarsalAirable')){ return; }
			 var tables = document.getElementById('univarsalAirable');
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
		    document.universalFlateRateForm.buttonValue.value="addItem";
			document.universalFlateRateForm.submit();
		}
		
	</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">

		<html:form action="/universalFlateRate" name="universalFlateRateForm" type="com.gp.cong.logisoft.struts.ratemangement.form.UniversalFlateRateForm" scope="request">

 <table class="tableBorderNew" border="0" width="100%" cellpadding="0" cellspacing="0" >
   <tr class="tableHeadingNew"><td>Flat Rate Per Unit</td>
   <td align="right">
			<input type="button" value="Add" onclick="addItem()" id="add" class="buttonStyleNew" />
     </td>
   </tr>
   <tr>
      <td colspan="2">
     
 <table width=100% border="0" cellpadding="0" cellspacing="0">
  <tr>
	<%if(session.getAttribute("universalFalteAdd")==null)
	{ %>
	
	<%}
	else{ %>
  		
    		<td></td>
  		</tr>
  		
		</table>
		<table><tr class="textlabels">
		<td>Unit Size Type</td>
		
		<td>Amount </td>
		</tr><tr>
			<td><html:select property="startrange" styleClass="verysmalldropdownStyle" >
			<html:optionsCollection name="startrange"/>    
			 </html:select></td>
			 	
			    <td width="105"><html:text property="amount"   onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>
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
		<div id="divtablesty1" class="scrolldisplaytable" style="height:125px">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        
        %>
        <display:table name="<%=ariFrightList%>" pagesize="<%=pageSize%>"  class="displaytagstyle"  sort="list" id="univarsalAirable" > 
			<%
				String tempPath="";
				String wightrange="";

				String iStr=String.valueOf(i);
				
  					tempPath=editPath+"?ind="+iStr;  				
  					if(ariFrightList!=null && ariFrightList.size()>0){
  					UniverseFlatRate universeAirFreight=(UniverseFlatRate)ariFrightList.get(i);
  					if(universeAirFreight.getUnitType()!=null){
  					wightrange=universeAirFreight.getUnitType().getCodedesc();
  					}
  					if(universeAirFreight.getAmount()!=null){
  					amount=df.format(universeAirFreight.getAmount());
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
  			
  			
			<display:column  paramId="param" paramProperty="id"  title="Unit Size Type &nbsp;&nbsp;"><a href="<%=tempPath%>"><%=wightrange%></a></display:column>
			<display:column  title="Amount"><%=amount%></display:column>
			
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







 	
			














