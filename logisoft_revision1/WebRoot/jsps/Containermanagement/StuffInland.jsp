<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cong.logisoft.beans.*,com.gp.cvst.logisoft.domain.*, com.gp.cong.logisoft.beans.*,java.util.*,com.gp.cvst.logisoft.hibernate.dao.*,com.gp.cong.logisoft.hibernate.dao.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<jsp:useBean id="unitport" class="com.gp.cong.logisoft.struts.containermangement.form.StuffExportForm" scope="request" ></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html>
<head>
<%
String unitNumber="123456";
String unitNumber1="34567";
String unitNumber2="56789";
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}
session.setAttribute("unitnumber",unitNumber);
String flagvalue="loaded";
  DockreceiptDAO docDAO = new DockreceiptDAO();
    
String Total= docDAO.getTotal(flagvalue,unitNumber);
String Weight = docDAO.getWeights(flagvalue,unitNumber);
Double gWeight = 0.0;

Double GTotal=0.0;
Double Gtotals=0.0;
Double GUnitSize=0.0;
Double GWeights=0.0;
 UnitDAO unitdao = new UnitDAO();
  String unitSize = unitdao.getUnitNumber(unitNumber);
  String unitWieghts=unitdao.getUnitWeights(unitNumber);
   out.write(unitSize);
   out.write("----"+unitWieghts); 
 out.print(request.getContextPath());
if(Total==null || Total.equals("0"))
{
GTotal=0.0;
}
 else
{
GTotal = Double.parseDouble(Total);
GUnitSize = Double.parseDouble(unitSize);
Gtotals = GTotal/GUnitSize*100;

}

if(Weight==null || Weight.equals("0"))
{
gWeight=0.0;
} 
 else
{
gWeight = Double.parseDouble(Weight);
GUnitSize = Double.parseDouble(unitWieghts);
GWeights = (gWeight/GUnitSize) *100;
}
List docReciept = null;
  docReciept = docDAO.docrecipets();  
  List upDoceReciept=null;
  upDoceReciept = docDAO.docrecipets1(unitNumber); 
  
 String editPath=path+"/stuffInland.do";
 %>
<%@include file="../includes/baseResources.jsp" %>

<script language="javascript" type="text/javascript">

 function save()
 {
 alert("hai");
 document.stuffexportform.buttonValue.value="save";
 
 document.stuffexportform.submit();
 }
 
</script>
</head>

<body class="whitebackgrnd">
<html:form action="/stuffInland" name="stuffInlandtform" type="com.gp.cong.logisoft.struts.containermangement.form.StuffInlandForm"   scope="request" >
   
<table width="734" border="0" cellspacing="0" cellpadding="0">
<tr>
  <td colspan="7" class="headerbluelarge"><br /></td>
</tr>
<tr>
  <td width="261" height="24" class="headerbluelarge">Stuff Inland </td>
  <td width="26" class="textlabels">Unit:</td>
  <td width="100" class="nonlabels"><%=unitNumber%></td>
  <td width="91" class="textlabels">Inland Voyage:</td>
  <td width="84" class="nonlabels"> 01-250-1338 </td>
  <td width="106" class="textlabels">Current Location:</td>
  <td width="66" class="nonlabels">MAIME </td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
             <td  class="headerbluesmall"colspan="10">&nbsp;&nbsp;DRs Already Picked</td>
          </tr> 
              <tr>
                <td>&nbsp;</td>
                <td class="textlabels">&nbsp;</td>
                <td class="textlabels">&nbsp;</td>
                <td class="textlabels">&nbsp;</td>
                <td class="textlabels">&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
				<td class="textlabels"><b>Size</b></td>
				<td class="textlabels"><b>SSL</b></td>
                <td class="textlabels"><b>Type</b></td>
                <td class="textlabels"><b>Destination</b></td>
                <td class="textlabels"><b>Sail Dt.</b> </td>
                <td align="right" class="textlabels">&nbsp;</td>
                <td>&nbsp;</td>
                <td><b>CFT </b> </td>
                 <td>
                 <%if(Gtotals==0)
                 { %>
                 <img src="<%=path%>/img/Progress0.GIF" width="121" height="21" />
                 <%} else if(Gtotals>0 && Gtotals<=10) 
                 {%>
                 <img src="<%=path%>/img/Progress10.GIF" width="121" height="21" />
                 <%}else if(GTotal>10 && GTotal<=20)
                 {%>
                 <img src="<%=path%>/img/Progress20.GIF" width="121" height="21" />
                <%} else if(Gtotals>20 && Gtotals<=30) 
                  { %>
                 <img src="<%=path%>/img/Progress30.GIF" width="121" height="21" />
                 <%} else if(Gtotals>30 && Gtotals<=40) 
                  { %>
                 <img src="<%=path%>/img/Progress40.GIF" width="121" height="21" />
                 <%} else if(Gtotals>40 && Gtotals<=50) 
                  { %>
                 <img src="<%=path%>/img/Progress50.GIF" width="121" height="21" />
                 <%}else if(Gtotals>50 && Gtotals<=60) 
                  { %>
                 <img src="<%=path%>/img/Progress60.GIF" width="121" height="21" />
                 <%} else if(Gtotals>60 && Gtotals<=70) 
                  { %>
                 <img src="<%=path%>/img/Progress70.GIF" width="121" height="21" />
                 <%} else if(Gtotals>70 && Gtotals<=80) 
                  { %>
                 <img src="<%=path%>/img/Progress80.GIF" width="121" height="21" />
                 <%} else if(Gtotals>80 && Gtotals<=90) 
                  { %>
                 <img src="<%=path%>/img/Progress90.GIF" width="121" height="21" />
                 <%} else if(Gtotals>90 && Gtotals<=100) 
                  { %>
                 <img src="<%=path%>/img/progress100.GIF" width="121" height="21" />
                 <%}  %>
                 </td>
              </tr>
              </table>
              <table>
              <tr> 
				 <td>AMERICAN ACTV </td> 
				  <td>&nbsp;</td>
				 <td>xxxx</td>  
				  <td>&nbsp;</td>
                <td>Dry</td> 
                 <td>&nbsp;</td>
                <td>Kingston, Jamaica </td>
                <td>&nbsp;</td>
                 <td>&nbsp;</td>
                  <td>&nbsp;</td>
                   <td>&nbsp;</td>
                <td>7/2/07</td> 
                 <td>&nbsp;</td>
                  <td>&nbsp;</td>
                 <td>&nbsp;</td>  
                  <td>&nbsp;</td>
                   <td>&nbsp;</td>
                    <td>&nbsp;</td>
                     <td>&nbsp;</td>
                      <td>&nbsp;</td>
                       <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                         <td>&nbsp;</td>
                          <td>&nbsp;</td>
                           <td>&nbsp;</td>
                <td class="textlabels"><b>Wieght</b></td>
                  <td>&nbsp;</td> <td width="2%">&nbsp;</td>  
                <td> <%if(GWeights==0)
                 { %>
                 <img src="<%=path%>/img/Progress0.GIF" width="121" height="21" />
                 <% }else if(GWeights>0 && GWeights<=10) 
                 {%>
                 <img src="<%=path%>/img/Progress10.GIF" width="121" height="21" /> 
                 <%}else if(GWeights>10 && GWeights<=20)
                 {%>
                 <img src="<%=path%>/img/Progress20.GIF" width="121" height="21" />
                <%} else if(GWeights>20 && GWeights<=30) 
                  { %>
                 <img src="<%=path%>/img/Progress30.GIF" width="121" height="21" />
                 <%} else if(GWeights>30 && GWeights<=40) 
                  { %>
                 <img src="<%=path%>/img/Progress40.GIF" width="121" height="21" />
                 <%} else if(GWeights>40 && GWeights<=50) 
                  { %>
                 <img src="<%=path%>/img/Progress50.GIF" width="121" height="21" />
                 <%}else if(GWeights>50 && GWeights<=60) 
                  { %>
                 <img src="<%=path%>/img/Progress60.GIF" width="121" height="21" />
                 <%} else if(GWeights>60 && GWeights<=70) 
                  { %>
                 <img src="<%=path%>/img/Progress70.GIF" width="121" height="21" />
                 <%} else if(GWeights>70 && GWeights<=80) 
                  { %>
                 <img src="<%=path%>/img/Progress80.GIF" width="121" height="21" />
                 <%} else if(GWeights>80 && GWeights<=90) 
                  { %>
                 <img src="<%=path%>/img/Progress90.GIF" width="121" height="21" />
                 <%} else if(GWeights>90 && GWeights<=100) 
                  { %>
                 <img src="<%=path%>/img/progress100.GIF" width="121" height="21" />
                 <%}  %>
                 </td>  
              </tr>
            </table> 
			  
  <% 
int k=0;
if(upDoceReciept!=null && upDoceReciept.size()>0)
{
DocRecieptBean DRB = (DocRecieptBean)upDoceReciept.get(k);

%>
<div style="overflow:scroll;width:120%;height:120px;">  
   <display:table name ="<%=upDoceReciept%>"  pagesize="10" class="displaytagstyle" style="width:60%" id="dockreciepts" sort="list" >
 
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> DockReciepts details displayed,For more code click on page numbers.
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
			 
  			 <display:column  property ="dockReceipt"    title="DockReciept"   sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="piece" title="piece" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="cuftWarehouse" title="Warehouse" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="remarks" title="remarks" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="cft" title="CFT" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="hazmat" title="hazmatShipper" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="dateIn" title="dateIn" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="eta" title="eta" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="etd" title="etd" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="status" title="status" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="consignee" title="consignee" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="hazNotes" title="hazNotes" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="genNotes" title="genNotes" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="loadingInstr" title="loadingInstr" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="weight" title="weight" sortable="true" headerClass="sortable">  </display:column>
			<display:column paramId="paramid1" href="<%=editPath %>"   paramProperty="dockReceipt">  <img src ="<%=path%>/img/unstuff.gif" border="0"  /> </display:column>
            <%k++; %>
 
 </display:table>  
              </div>
              <%} %>
         
              <table width="550" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="123">&nbsp;</td>
                  <td width="48">&nbsp;</td>
                  <td width="178">&nbsp;</td>
                  
                 <td><img src="<%=path %>/img/save.gif"  onclick="save()"/></td>
                  <td width="29">&nbsp;</td>
                   <td><img src="<%=path %>/img/loadguide.gif" /></td>
                </tr>
              </table> 
	<table> 
  <tr>
    <td colspan="2" align="center" class="headerbluelarge">&nbsp;</td>
  </tr>
  <tr>
    <td width="754" colspan="2" align="center" class="headerbluelarge">
     Released Dock Receipts
       	</td>
	  </tr>
</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr class="headerbluesmall">
     <td height="14" colspan="6" >&nbsp;&nbsp;Released Dock Receipts </td>
  </tr>
</table>
<html:hidden property="flagvalue"  />
<html:hidden property="buttonValue"  />
 <div style="overflow:scroll;width:120%;height:120px;">   
<% 

int i=0;
if(docReciept!=null && docReciept.size()>0)
{
DocRecieptBean DRB = (DocRecieptBean)docReciept.get(i);

%>
 

<display:table name ="<%=docReciept%>"  pagesize="10" class="displaytagstyle" style="width:60%" id="dockreciepts" sort="list" >
 
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> DockReciepts details displayed,For more code click on page numbers.
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
			 
  			 <display:column  property ="dockReceipt"    title="DockReciept"   sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="piece" title="piece" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="cuftWarehouse" title="Warehouse" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="cft" title="CFT" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="remarks" title="remarks" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="hazmat" title="hazmatShipper" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="dateIn" title="dateIn" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="eta" title="eta" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="etd" title="etd" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="status" title="status" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="consignee" title="consignee" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="hazNotes" title="hazNotes" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="genNotes" title="genNotes" sortable="true" headerClass="sortable">  </display:column>
  			 <display:column  property ="loadingInstr" title="loadingInstr" sortable="true" headerClass="sortable">  </display:column>
			<display:column  property ="weight" title="weight" sortable="true" headerClass="sortable">  </display:column>
			<display:column paramId="paramid" href="<%=editPath %>"   paramProperty="dockReceipt"  >  <img src ="<%=path%>/img/stuff.gif" border="0" /> </display:column>
            
 <%i++; %>
 </display:table>  
  <html:hidden property="docreceipt"/>
 </div>
 <%} %>
</html:form>
</body>

	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>