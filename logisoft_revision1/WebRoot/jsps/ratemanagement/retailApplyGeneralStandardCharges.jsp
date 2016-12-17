<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.RetailStandardCharges1,com.gp.cong.logisoft.domain.RetailStandardCharges,com.gp.cong.logisoft.domain.RefTerminal"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List search=new ArrayList();
String buttonValue="";
RetailStandardCharges retailRatesObj1= new RetailStandardCharges();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
if(request.getParameter("button")!=null)
{
buttonValue=(String)request.getParameter("button");
session.setAttribute("option",buttonValue);
}
List includedList = new ArrayList();

DBUtil dbUtil=new DBUtil();
List retailapplyGeneralStandardList=new ArrayList();
if(session.getAttribute("retailstandardCharges")!=null )
{
    retailRatesObj1=(RetailStandardCharges)session.getAttribute("retailstandardCharges");
    
   }
   
if(session.getAttribute("retailapplyGeneralStandardList")!=null)
{

retailapplyGeneralStandardList=(List)session.getAttribute("retailapplyGeneralStandardList");
}
else
{

if(retailRatesObj1!=null){
retailapplyGeneralStandardList=dbUtil.getRetailStandardChargesList(retailRatesObj1.getOrgTerminal(),retailRatesObj1.getDestPort());

}
}

session.setAttribute("retailapplyGeneralStandardList",retailapplyGeneralStandardList);
if(session.getAttribute("retailincludedList")!=null)
{
includedList=(List)session.getAttribute("retailincludedList");

}
String cancel="";

if(request.getAttribute("cancel")!=null)
{
cancel=(String)request.getAttribute("cancel");
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
		opener.location.href="<%=path%>/jsps/ratemanagement/retailCSSC.jsp";
	</script>
<%
}

String editPath=path+"/retailApplyGeneralStandardCharges.do";

%>

 
<html> 
	<head>
		<title>JSP for RetailApplyGeneralStandardChargesForm form</title>
		<base href="<%=basePath%>">
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript">
	    function searchform()
		{
			document.retailApplyGeneralStandardChargesForm.buttonValue.value="search";
			document.retailApplyGeneralStandardChargesForm.submit();
		}
		function confirmdelete(obj)
		{
		//alert(document.retailApplyGeneralStandardChargesForm.index);
		//alert(x);
		
		var rowindex=obj.parentNode.parentNode.rowIndex;
	
	 		var x=document.getElementById('retailincludedtable').rows[rowindex].cells;	
	 		
			document.retailApplyGeneralStandardChargesForm.index.value=obj.name;
		
    		document.retailApplyGeneralStandardChargesForm.buttonValue.value="delete";
    		
   		    var result = confirm("Are you sure you want to delete this Standard Charge ");
			if(result)
			{
   				document.retailApplyGeneralStandardChargesForm.submit();
   			}	
		
		}
		function cancel()
		{
		document.retailApplyGeneralStandardChargesForm.buttonValue.value="cancel";
			
			document.retailApplyGeneralStandardChargesForm.submit();
		}
		
		function go_there(val)
		{
		
		 if(val!="")
		 {
		 var result = confirm("Do you really want to override charge type ??");
		 document.retailApplyGeneralStandardChargesForm.buttonValue.value="accept";
		 if(result)
			{
   				document.retailApplyGeneralStandardChargesForm.submit();
   			}	
		 else
		 {
		  
		  }}
		}
		
  	    </script>
		
	</head>
	<body class="whitebackgrnd" onload="go_there('<%=msg%>')">
		<html:form action="/retailApplyGeneralStandardCharges" scope="request">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr class="textlabels">
  			<td align="left" class="headerbluelarge">Retail Apply General Standard Charges </td>
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
        		<display:table name="<%=retailapplyGeneralStandardList%>" pagesize="20" class="displaytagstyle"  sort="list" id="agsstable" > 
				
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
			    	if(retailapplyGeneralStandardList != null && retailapplyGeneralStandardList.size()>0)
			    	{
						RetailStandardCharges1 retailStandardChargesObj2=(RetailStandardCharges1)retailapplyGeneralStandardList.get(i);
						if(retailStandardChargesObj2.getChargeType().getCodedesc() != null)
						{
							type=retailStandardChargesObj2.getChargeType().getCodedesc();
						}
						if(retailStandardChargesObj2.getChargeCode().getCode() != null)
						{
							chargeCode=retailStandardChargesObj2.getChargeCode().getCode();
						}
						if(retailStandardChargesObj2.getEffectiveDate()!=null)
						{
						effectDate=dateFormat.format(retailStandardChargesObj2.getEffectiveDate());
						}
						if(retailStandardChargesObj2.getChangedDate()!=null)
						{
						chngDate=dateFormat.format(retailStandardChargesObj2.getChangedDate());
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
				<display:column property="amtPer1000kg" title="Amt/1000kg" />
				<display:column property="percentage" title="Per" />
				<display:column property="minAmt" title="MinAmt" />
				<display:column title="Effec Date"><%=effectDate%></display:column>
				<display:column property="whoChanged" title="Who Chngd" />
				<display:column title="Chnged Date"><%=chngDate%></display:column>
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
				<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
        		<table width="100%" border="0" cellpadding="0" cellspacing="0">
        		<% 
        			int j=0;
        		 %>
        		<display:table name="<%=includedList%>" pagesize="20" class="displaytagstyle"  sort="list" id="retailincludedtable" > 
				
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
					String effectDate1="";
					String chngDate1="";
				
			    	if(includedList != null && includedList.size()>0)
			    	{
						RetailStandardCharges1 retailStandardChargesObj2=(RetailStandardCharges1)includedList.get(j);
						
						if(retailStandardChargesObj2.getChargeType().getCodedesc() != null)
						{
							type=retailStandardChargesObj2.getChargeType().getCodedesc();
						}
						if(retailStandardChargesObj2.getChargeCode().getCode() != null)
						{
							chargeCode=retailStandardChargesObj2.getChargeCode().getCode();
						}
						if(retailStandardChargesObj2.getEffectiveDate()!=null)
						{
						effectDate1=dateFormat.format(retailStandardChargesObj2.getEffectiveDate());
						}
						if(retailStandardChargesObj2.getChangedDate()!=null)
						{
						chngDate1=dateFormat.format(retailStandardChargesObj2.getChangedDate());
						}
					}
					
			 	%>
  				<display:column  title="Chrg Code"  ><%=chargeCode%></display:column>
				<display:column  title="Chrg Type"  ><%=type %></display:column>
				<display:column property="standard" title="Std" />
				<display:column property="amtPerCft" title="Amt/Cft" />
				<display:column property="amtPer100lbs" title="Amt/100lbs" />
				<display:column property="amtPerCbm" title="Amt/Cbm" />
				<display:column property="amtPer1000kg" title="Amt/1000kg" />
				<display:column property="percentage" title="Per" />
				<display:column property="minAmt" title="MinAmt" />
				<display:column title="Effec Date"><%=effectDate1%></display:column>
				<display:column property="whoChanged" title="Who Chngd" />
				<display:column title="Chnged Date"><%=chngDate1%></display:column>
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

