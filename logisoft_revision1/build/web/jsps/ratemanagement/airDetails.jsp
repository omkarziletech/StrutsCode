<%@ page language="java"  import="java.text.DecimalFormat,java.util.Date,com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.domain.AirWeightRangesRates,com.gp.cong.logisoft.domain.GenericCode,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.io.ObjectInputStream.GetField"/>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String tempPath=null;
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
DecimalFormat df = new DecimalFormat("0.00");
java.util.Date date = new java.util.Date();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
String dateStr="";
String userdisplay="";

List weightRangeList=new ArrayList();
List airDetailsList = null;
dateStr = dateFormat.format(date);
User user=null;
String editPath=path+"/airDetails.do";
if(session.getAttribute("loginuser")!=null)
{
	user=(User)session.getAttribute("loginuser");
}
if(userdisplay != null)
{
	userdisplay=user.getFirstName();
}
if (session.getAttribute("airdetailsAdd") != null)
{

	airDetailsList = (List) session.getAttribute("airdetailsAdd");
		
	
}
if(weightRangeList != null)
{
	weightRangeList=dbUtil.getWeightRangeList(new Integer(31),"yes","Select Weight Range",airDetailsList);
	request.setAttribute("weightRangeList",weightRangeList);
}
String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}


%>
 
<html> 
	<head>
		<title>JSP for AirDetailsForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function addAirDetails()
		{
		if(document.airDetailsForm.weightRange.value=="0")
		{
			alert("Please select Weight Range");
			return;
		}
		
			document.airDetailsForm.buttonValue.value="add";
  			document.airDetailsForm.submit();
		}
		function confirmdelete(obj)
		{
		 	var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('airweightratestable').rows[rowindex].cells;	
	   		document.airDetailsForm.index.value=rowindex-1;
			document.airDetailsForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this Weight Range");
			if(result)
			{
   				document.airDetailsForm.submit();
   			}	
   		}
   		function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
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
  	 		
    }
    function confirmnote()
	{
		document.airDetailsForm.buttonValue.value="note";
    	document.airDetailsForm.submit();
   	}
   function addItem()
	{
		document.airDetailsForm.buttonValue.value="addItem";
    	document.airDetailsForm.submit();
   	}
   
	</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
	
<html:form action="/airDetails" name="airDetailsForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AirDetailsForm" scope="request">

<table width="100%" border="0" ellpadding="0" cellspacing="0">
  <tr class="textlabels">
  <td>
  <%if(session.getAttribute("addDetailItem")==null)
	     { %>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
      <tr class="tableHeadingNew" ><td>List of Air Details</td>
        
         <td align="right">
             <input type="button" value="Add" class="buttonStyleNew" id="add" onclick="addItem()"/>
         </td>
      </tr> 
      		<%}else{ %>
      <table width="100%" border="0" cellpadding="0" cellspacing="2" class="tableBorderNew">
         <tr class="tableHeadingNew" >List of Air Details</tr>
      	 <tr class="textlabels">
      	    <td><div align="center"></div></td>
      		<td><div align="center"><b>GENERAL</div></td>
      		<td><div align="center"><b>EXPRESS</div></td>
      		<td><div align="center"><b>DEFERRED</div></td>
    	 </tr>
    	<tr class="textlabels">
      		<td ><div align="center">Weight Range</div></td>
      		<td><table border="0"><tr  class="textlabels">
      		     <td>&nbsp;&nbsp;</td>
      			<td >Rate/LB</td>
      			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      			<td >Min Amt</td>
      		</tr> </table> </td>
      		<td><table border="0"><tr class="textlabels">
      		    <td>&nbsp;&nbsp;</td>
      			<td >Rate/LB</td>
      			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      			<td >Min Amt</td>
      		</tr> </table> </td>
      		<td><table><tr class="textlabels">
      		     <td>&nbsp;&nbsp;</td>
      			<td >Rate/LB</td>
      			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      			<td >Min Amt</td>
      		</tr> </table> </td>
      		<td></td>
    	</tr>
    	<tr class="textlabels" >
      		<td align="center" ><html:select property="weightRange" styleClass="verysmalldropdownStyle" value="">
      			<html:optionsCollection name="weightRangeList"/>          
                </html:select></td>
      		<td align="center"><table ><tr>
      			<td ><html:text property="generalRate" onkeypress="check(this,4)"  onblur="checkdec(this)"  maxlength="7" size="7"  /></td>      			
      			<td ><html:text property="generalAmt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  /></td>
      		</tr> </table> </td>
      		<td align="center"><table><tr>
      			<td ><html:text property="expressRate" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7" /></td>
      			<td ><html:text property="expressAmt"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7" /></td>
      		</tr> </table> </td>
      		<td align="center"><table><tr>
      			<td ><html:text property="deferredRate" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  /></td>
      			<td><html:text property="deferredAmt"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7" /></td>
      		</tr> </table> </td>
      	<td align="center">
      	   <input type="button" value="Add To List" id="add" onclick="addAirDetails()" class="buttonStyleNew" style="width:75px" />

      	 </td>	
      <%} %>    	
      </tr>
    	
		
	<tr>
		<td colspan="5">
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <%  int i=0; %>
        <display:table name="<%=airDetailsList%>" pagesize="<%=pageSize%>"  class="displaytagstyle"  sort="list" id="airweightratestable"> 
			<%
				String weightRange=null;
				String change_date=null;
				String cheDate = "";
				String generalRate = "";
				String generalMinAmt = "";
				String expressRate = "";
				String expressMinAmt = "";
				String deferredRate = "";
				String deferredMinAmt = "";
				
			    if(airDetailsList != null && airDetailsList.size()>0)
			    {
					AirWeightRangesRates aiirweightObj=(AirWeightRangesRates)airDetailsList.get(i);
					GenericCode gen=aiirweightObj.getWeightRange();
					weightRange=gen.getCodedesc();
					if(aiirweightObj.getChangedDate()!=null){
  					change_date=dateFormat.format(aiirweightObj.getChangedDate());
  				}
				 
				if(aiirweightObj.getGeneralRate() != null)
				{
					generalRate = df.format(aiirweightObj.getGeneralRate());
				}
					
				
				if(aiirweightObj.getGeneralMinAmt() != null){
				   generalMinAmt = df.format(aiirweightObj.getGeneralMinAmt());
				}
				
				
				if(aiirweightObj.getExpressRate() != null){
				   expressRate = df.format(aiirweightObj.getExpressRate());
				}
				
				
				if(aiirweightObj.getExpressMinAmt() != null){
				   expressMinAmt = df.format(aiirweightObj.getExpressMinAmt());
				}
				
				
				if(aiirweightObj.getDeferredRate() != null){
				   deferredRate = df.format(aiirweightObj.getDeferredRate());
				}
				
				
				if(aiirweightObj.getDeferredMinAmt() != null)
				{
				  deferredMinAmt = df.format(aiirweightObj.getDeferredMinAmt());
				}
				}
				String iStr=String.valueOf(i);
  					tempPath=editPath+"?indno="+iStr;
  					
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
			<display:column  title="Weight Range"  ><a href="<%=tempPath%>"><%=weightRange %></a></display:column>
			<display:column  title="General Rate"><%=generalRate%></display:column>
			<display:column title="General Min Amt" ><%=generalMinAmt%></display:column>  
			<display:column title="Express Rate" ><%=expressRate%></display:column>
			<display:column title="Express Min Amt" ><%=expressMinAmt%></display:column>
			<display:column title="Deferred Rate" ><%=deferredRate%></display:column>
			<display:column title="Deferred Min Amt" ><%=deferredMinAmt%></display:column>
			<% i++;%>
		</display:table>
        </table></div>  
    	</td> 
   </tr> 
   </table> 
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

