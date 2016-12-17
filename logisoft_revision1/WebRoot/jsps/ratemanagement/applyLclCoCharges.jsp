<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,
com.gp.cong.logisoft.domain.LCLColoadStandardCharges,com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.domain.LCLColoadMaster,java.text.DateFormat,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List search=new ArrayList();
String buttonValue="";
LCLColoadStandardCharges lCLColoadStandardCharges=new LCLColoadStandardCharges();
LCLColoadMaster lCLColoadMaster= new LCLColoadMaster();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
if(request.getParameter("button")!=null)
{
buttonValue=(String)request.getParameter("button");
session.setAttribute("option",buttonValue);
}
List includedList = new ArrayList();

DBUtil dbUtil=new DBUtil();
List applylclcoList=new ArrayList();
if(session.getAttribute("addlclColoadMaster")!=null)
{
	lCLColoadMaster=(LCLColoadMaster)session.getAttribute("addlclColoadMaster");
}
   



if(session.getAttribute("lclcolaodincludedList")!=null)
{
includedList=(List)session.getAttribute("lclcolaodincludedList");

}
String cancel="";

if(request.getAttribute("cancel")!=null)
{
cancel=(String)request.getAttribute("cancel");
}
else
{
if(session.getAttribute("applylclcoloadcharges")!=null)
{

applylclcoList=(List)session.getAttribute("applylclcoloadcharges");

}
else
{

if(lCLColoadMaster!=null)
{
applylclcoList=dbUtil.getCoStandardChargesList(lCLColoadMaster.getOriginTerminal(),lCLColoadMaster.getDestinationPort());

}

}
session.setAttribute("applylclcoloadcharges",applylclcoList);
}
String msg="";
if(request.getAttribute("message")!=null)
{
msg=(String)request.getAttribute("message");

}


if(cancel.equals("cancel"))
{
%>

	<script>
	
		self.close();
		opener.location.href="<%=path%>/jsps/ratemanagement/AddLclColoadCommodity.jsp";
	</script>
<%
}

String editPath=path+"/applyLclCoCharges.do";

%>
<html> 
	<head>
		<title>Apply General Standard Charges</title>
		<base href="<%=basePath%>">
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript">
	    function searchform()
		{
			document.applyLclCoChargesForm.buttonValue.value="search";
			document.applyLclCoChargesForm.submit();
		}
		function confirmdelete(obj)
		{
		
		var rowindex=obj.parentNode.parentNode.rowIndex;
	
	 		var x=document.getElementById('includedtable').rows[rowindex].cells;	
	 		
			document.applyLclCoChargesForm.index.value=obj.name;
		
    		document.applyLclCoChargesForm.buttonValue.value="delete";
    		
   		    var result = confirm("Are you sure you want to delete this Standard Charge ");
			if(result)
			{
   				document.applyLclCoChargesForm.submit();
   			}	
		
		}
		function cancel()
		{
		document.applyLclCoChargesForm.buttonValue.value="cancel";
			
			document.applyLclCoChargesForm.submit();
		}
		function go_there(val)
		{
		
		 if(val!=""){
		 var result = confirm("Do you really want to override charge type ??");
		 document.applyLclCoChargesForm.buttonValue.value="accept";
		 if(result)
			{
   				document.applyLclCoChargesForm.submit();
   			}	
		 else
		 {
		  
		  }}
		}
  	    </script>
	</head>
	<body class="whitebackgrnd" onload="go_there('<%=msg%>')">
		<html:form action="/applyLclCoCharges" scope="request">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr class="textlabels">
  			<td align="left" class="headerbluelarge">Apply General Standard Charges </td>
  			<td align="left" class="headerbluelarge">&nbsp;</td>
  			<td><a>
<img src="<%=path%>/img/cancel.gif" onclick="cancel()"/></a></td>
		</tr>
		
  		
  			</table>
		<table width="100%">
     	<tr>
    		<td height="12" class="headerbluesmall">&nbsp;General Standard Charges </td> 
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
        		<display:table name="<%=applylclcoList%>" pagesize="20" class="displaytagstyle"  sort="list" id="agsstable" > 
				
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
					String type=null;
					String chargeCode=null;
					String effectDate="";
					String chngDate="";
			    	if(applylclcoList != null && applylclcoList.size()>0)
			    	{
						LCLColoadStandardCharges coStandardChargesObj2=(LCLColoadStandardCharges)applylclcoList.get(i);
						if(coStandardChargesObj2.getChargeType().getCodedesc() != null)
						{
							type=coStandardChargesObj2.getChargeType().getCodedesc();
						}
						if(coStandardChargesObj2.getChargeCode().getCode() != null)
						{
							chargeCode=coStandardChargesObj2.getChargeCode().getCode();
						}
						if(coStandardChargesObj2.getEffectiveDate()!=null)
						{
						effectDate=dateFormat.format(coStandardChargesObj2.getEffectiveDate());
						}
						if(coStandardChargesObj2.getChangedDate()!=null)
						{
						chngDate=dateFormat.format(coStandardChargesObj2.getChangedDate());
						}
					}
					String iStr=String.valueOf(i);
  			String tempPath=editPath+"?ind="+iStr;
			 	%>
  				<display:column  title="Chrg Code"  ><a href="<%=tempPath%>"><%=chargeCode%></a></display:column>
				<display:column  title="Chrg Type"  ><%=type %></display:column>
				<display:column property="standard" title="Std" />
				<display:column property="amtPerCft" title="Amt/Cft" />
				<display:column property="amtPer100lbs" title="Amt/100lbs" />
				<display:column property="amtPerCbm" title="Amt/Cbm" />
				<display:column property="amtPer1000Kg" title="Amt/1000kg" />
				<display:column property="percentage" title="Per" />
				<display:column property="minAmt" title="MinAmt" />
				<display:column title="Effec Date" ><%=effectDate%></display:column>
				<display:column property="whoChanged" title="Who Chngd" />
				<display:column title="Chnged Date" ><%=chngDate%></display:column>
  				<% i++;
  		   	   
  				%>
		    </display:table>
            </table></div>  
    		</td> 
   			</tr>  
  		 </table>
  		 <table width="100%">
     	<tr>
    		<td height="12" class="headerbluesmall">&nbsp;Included Standard Charges </td> 
  		</tr>
  		
		</table>
		<table width="100%">
			<tr>
				<td>
				<div id="divtablesty1" class="scrolldisplaytable">
        		<table width="100%" border="0" cellpadding="0" cellspacing="0">
        		<% 
        			int j=0;
        		 %>
        		<display:table name="<%=includedList%>" pagesize="20" class="displaytagstyle"  sort="list" id="includedtable" style="width:100%"> 
				
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
					String type=null;
					String chargeCode=null;
					
			    	if(includedList != null && includedList.size()>0)
			    	{
						LCLColoadStandardCharges coStandardChargesObj2=(LCLColoadStandardCharges)includedList.get(j);
					
						if(coStandardChargesObj2.getChargeType().getCodedesc() != null)
						{
							type=coStandardChargesObj2.getChargeType().getCodedesc();
						}
						if(coStandardChargesObj2.getChargeCode().getCode() != null)
						{
							chargeCode=coStandardChargesObj2.getChargeCode().getCode();
						}
					}
					
			 	%>
  				<display:column  title="Chrg Code"  ><%=chargeCode%></display:column>
				<display:column  title="Chrg Type"  ><%=type %></display:column>
				<display:column property="standard" title="Std" />
				<display:column property="amtPerCft" title="Amt/Cft" />
				<display:column property="amtPer100lbs" title="Amt/100lbs" />
				<display:column property="amtPerCbm" title="Amt/Cbm" />
				<display:column property="amtPer1000Kg" title="Amt/1000kg" />
				<display:column property="percentage" title="Per" />
				<display:column property="minAmt" title="MinAmt" />
				<display:column property="effectiveDate" title="Effec Date" />
				<display:column property="whoChanged" title="Who Chngd" />
				<display:column property="changedDate" title="Chnged Date" />
<display:column><img name="<%=j%>" src="<%=path%>/img/toolBar_delete_hover.gif" border="0" id="delete" onclick="confirmdelete(this)"/></display:column>
	
  				<% j++;
  		   	   
  				%>
		    </display:table>
            </table></div>  
    		</td> 
   			</tr>  
  		 </table>
	<html:hidden property="buttonValue" styleId="buttonValue"/>
	 <html:hidden property="index"/>
	</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>