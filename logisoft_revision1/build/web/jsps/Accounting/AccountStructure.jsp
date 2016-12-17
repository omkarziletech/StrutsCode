<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  
<title> Account Structure</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}
 
String editPath=path+"/accountStructure.do";
DBUtil dbUtil=new DBUtil();
session.setAttribute("AcctStructureList",dbUtil.getAcctStructureList());
List AcctStructureList1=null;
AcctStructureList1=(List)request.getAttribute("AcctStructureList1");

Segment segdomain= new Segment();
AccountStructureBean segstructreDomain =null;
SegmentvalueBean segvalueBean=null;
 
String acctdesc="";
String segCode="";
String segDesc="";
String segLen="";
String acctstruct="0";
String svalue="";
String sdesc="";
int id=0;
int acctId=0;
int segvalid=0;
ArrayList Acctstrcut=null;
if((String) request.getAttribute("acctStructDesc")!=null)
{
acctdesc=(String)request.getAttribute("acctStructDesc");
}
if(session.getAttribute("AcctSturct")!=null)
{
 if(!acctdesc.equals(""))
 Acctstrcut = (ArrayList)session.getAttribute("AcctSturct");
}
SegmentvalueBean valuelist = null;

if(request.getAttribute("AcctStruct")!=null)
{
acctstruct=(String)request.getAttribute("AcctStruct");
}

List valueList = null;
if(session.getAttribute("valuelist")!=null)
{
if(!acctdesc.equals(""))
valueList = (List)session.getAttribute("valuelist");


}
 if(session.getAttribute("segvalueacctId")!=null)
 {
 segvalid=(Integer)session.getAttribute("segvalueacctId");
 }

if((AccountStructureBean)request.getAttribute("aclist")!=null)
 { 
 segstructreDomain =(AccountStructureBean)request.getAttribute("aclist");
 id = (Integer)segstructreDomain.getId();                      
 segCode = (String)segstructreDomain.getSeg_code();
 segDesc=(String)segstructreDomain.getSeg_desc();
 segLen=String.valueOf(segstructreDomain.getSeg_leng());
 }

 String message2="";
 if(request.getAttribute("message2")!=null)
 {
 message2=(String)request.getAttribute("message2");
 }
 String vsc="";
 if(request.getAttribute("vsegcode")!=null)
 {
  
  vsc=(String)request.getAttribute("vsegcode");
  
 }
%>
<%@include file="../includes/baseResources.jsp" %>
<script type="text/javascript">
function initRowHighlighting()
 {
 if (!document.getElementById('acctst')){ return; }
 var tables = document.getElementById('acctst');
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

function popup1(mylink, windowname)
 {
 
  if (!window.focus)return true;
  var href;
  if (typeof(mylink) == 'string')
  href=mylink;
  else
   href=mylink.href;
   window.open(href, windowname,'width=500,height=250,scrollbars=yes');
   return false;
 }

function popup(mylink, windowname)
 {
 
  if (!window.focus)return true;
  var href;
  if (typeof(mylink) == 'string')
  href=mylink;
 else
  href=mylink.href;
  window.open(href,windowname,'width=550,height=250,scrollbars=yes');
  return false;
 } 
 function refreshPage(){
  document.accountStructureForm.buttonValue.value="AcctStructureSelected";
   document.accountStructureForm.submit();
 }
 
  function showall()
  {
  
  if(document.accountStructureForm.acctstructure.value=="")
  {
  alert("please Select Account Structure");
  document.accountStructureForm.acctstructure.focus();
  return false;
  } 
   document.accountStructureForm.buttonValue.value="showall";
   document.accountStructureForm.submit();
  }
 
 function editshow(obj,windowname)
 {
  document.accountStructureForm.index.value=obj;
  document.accountStructureForm.buttonValue.value="editshow1";
  document.accountStructureForm.scode.value="<%=vsc%>";
  document.accountStructureForm.submit();
  if (!window.focus)return true;
  var href;
  var mylink="<%=path%>/jsps/Accounting/EditPopUp.jsp?accountStructure="+document.accountStructureForm.acctstructure.value;
  
  if (typeof(mylink) == 'string')
  href=mylink;
  else
     href=mylink.href;
     window.open(href, windowname, 'width=600,height=200,scrollbars=yes');
      return false;
      
      document.accountStructureForm.submit();
  } 
 
 function deleteacct(obj)
 {
 
 var rowindex=obj.parentNode.parentNode.rowIndex;

   document.accountStructureForm.buttonValue.value="deleteacct";
   var result = confirm("Are you sure you want to delete this Account Structure");
   if(result)
   {
    document.accountStructureForm.submit();
   } 
  }
 
 function deletesegment(obj,val)
 {
 
  var rowindex=obj.parentNode.parentNode.rowIndex;
  
  var x=document.getElementById('acctst').rows[val+1].cells; 
  document.accountStructureForm.index.value=val+1;
  document.accountStructureForm.buttonValue.value="deleteSegment";
  var result = confirm("Are you sure you want to delete this Segment");
  if(result)
    {
    document.accountStructureForm.submit();
     } 
  }
  function deletesegmentValues(obj,val)
  {
   
    var result = confirm("Are you sure you want to delete this Segment Value");
    if(result)
    {
     document.accountStructureForm.index.value=val;
    document.accountStructureForm.buttonValue.value="deleteSegmentValue";
     document.accountStructureForm.scode.value="<%=vsc%>";
    document.accountStructureForm.submit();
    } 
  
  
  }
 function showvalue(obj,val)
{
 
  var x=document.getElementById('acctst').rows[++val].cells;
  document.accountStructureForm.index.value=val;
  document.accountStructureForm.buttonValue.value="showvalues";
  document.accountStructureForm.submit();
  initRowHighlighting();
 }
 
 function addValues(obj,val,windowname)
  {
  var value=val;
  
  var x=document.getElementById('acctst').rows[++value].cells;
   var segcode=x[0].innerHTML;
   var seglenth = x[2].innerHTML
   
   if (!window.focus)return true;
   var href;
     if(seglenth==0)
      {
         alert("we cant add it for 'Zero' segmength");
      }
     else
     {
       var asid=document.accountStructureForm.acctstructure.value;
       if(asid=="")
       {
         alert("PLease Select Account Structure");
         document.accountStructureForm.acctstructure.focus;
         return false;
       }
       var mylink="<%=path%>/jsps/Accounting/AddValuePopup.jsp?acctStructName="+document.accountStructureForm.acctstructure.value+"&seglenth="+seglenth+"&value="+value;
       if (typeof(mylink) == 'string')
       href=mylink;
       else
       href=mylink.href;
       GB_show('ValuePopup','<%=path%>/jsps/Accounting/AddValuePopup.jsp?acctStructName='+document.accountStructureForm.acctstructure.value+'&seglenth='+seglenth+'&value='+value,180,650);
      return false;
     }
 }
  function submit2()
{

  if(document.accountStructureForm.acctstructure.value=="")
   {
   document.accountStructureForm.groupdesc.value="";
   alert("select a value from the list");
   document.accountStructureForm.acctstructure.focus();
     return false;
    }
     document.accountStructureForm.buttonValue.value="AcctStructureSelected";
     document.accountStructureForm.submit();
   }
  
  
  function load()
 {
 initRowHighlighting();
 if( document.accountStructureForm.acctstructure.value=="0")
 {
  document.accountStructureForm.groupdesc.value="";
 }
 }
  function print()
  {

  document.accountStructureForm.buttonValue.value="print";
     document.accountStructureForm.submit();
  }
  function updateAccountStructure()
  {
   document.accountStructureForm.buttonValue.value="updateAS";
   document.accountStructureForm.submit();
  }
</script>
<%@include file="../includes/resources.jsp" %>
</head>

<body class="whitebackgrnd" onload="load()">
<html:errors/>
<html:form action="/accountStructure" name="accountStructureForm" type="com.gp.cvst.logisoft.struts.form.AccountStructureForm" scope="request">
<html:hidden property="buttonValue"/>
<html:hidden property="abc"/>
<html:hidden property="index"/>
<html:hidden property="segmentcode"/>
<html:hidden property="segmentdesc"/>
<html:hidden property="segmentlength"/>
<html:hidden property="id123"/>


<c:if test="${msg!=null}">
<table>
<tr>
<td class="error">${msg}</td>
<tr>
</c:if>
<table width="100%" border="0" cellpadding="5" cellspacing="0" class="tableBorderNew" style="padding-left:10px">
<tr class="tableHeadingNew" > Account Structure </tr>

    <tr>   
        <td  width="15%"  class="textlabels"><b>Structure</b></td>
        <td width="15%" ><html:select property="acctstructure" styleClass="dropdown_accounting" onchange="submit2()" value="<%=acctstruct%>" >
        <html:optionsCollection name="AcctStructureList" styleClass="unfixedtextfiledstyle" /></html:select></td>
        <td  width="15%"   class="textlabels"><b>Description </b></td>
        <td width="15%"   ><html:text property="groupdesc" value="<%=acctdesc%>" styleClass="largetextfieldstyle"></html:text></td>
   <td width="40%" align="right">
   <input type="button" name="search" value="Print" onclick="print()" class="buttonStyleNew"/></td>
    </tr>
    <tr>
       
        
      <td colspan="4">
           <table width="100%">
           <tr>
     <td valign="top" colspan="7" align="center" style="padding-top:10px;">
  <input type="button" class="buttonStyleNew" value="Add Structure" style="width:80px;" onclick="return GB_show('Structures', '<%=path%>/jsps/Accounting/accstructurepopup.jsp',500,650)"/>
   <input type="button" class="buttonStyleNew" value="Add Segment" style="width:80px;" onclick="return GB_show('Segments', '<%=path%>/jsps/Accounting/AddSegmentPopUp.jsp?button='+'searchwarehousecity&acctStructName='+document.accountStructureForm.acctstructure.value,500,650)"/>
     <input type="button" name="search" value="Delete" onclick="deleteacct(this)" class="buttonStyleNew"/>
     <input type="button" value="Save" onclick="updateAccountStructure()" class="buttonStyleNew"/>
      </td>  
     	</tr>
     	</table>
     </td>
     </tr>
     </table>
<br/><br/>
  
 <table width="100%" border="0" cellpadding="5" cellspacing="0" class="tableBorderNew" style="padding-left:10px">
<tr class="tableHeadingNew" >List Of Segments</tr> 
 <td>
 <%! String a="hai"; %> 
 <%

if((Acctstrcut!=null && Acctstrcut.size()>0)) 
{
int test=0; 
%>

<div id="divtablesty1" style="border: thin; overflow: scroll; width: 100%; height: 320px;">
<display:table name="<%=Acctstrcut%>" pagesize="<%=pageSize%>" class="displaytagstyleNew" sort="list"  id="acctst"  >
<%
AccountStructureBean asBean = new AccountStructureBean();
asBean=(AccountStructureBean)Acctstrcut.get(test);
String vl=asBean.getValidateList();
String SegmentCode=asBean.getSeg_code();

 %>
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Account details displayed,For more code click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="User"/>
  			<display:setProperty name="paging.banner.items_name" value="Segments "/>
		
	 
    <display:column   paramId="paramid" title="SegmentCode"  headerClass="sortable"><a href="#" onclick="showvalue(this,<%=test%>)"><%=SegmentCode%></a></display:column><%--
    <a href="#" onclick="showvalue(this,<%=test%>)"><display:column  property="seg_code" paramId="paramid" title="SegmentCode" sortable="true" headerClass="sortable"><%=SegmentCode%></display:column></a>
--%><display:column  property="seg_desc" title="Segment Description"  headerClass="sortable"></display:column>
	<display:column  property="seg_leng" title="Segment Length"  paramId="paramid" maxLength="5" headerClass="sortable"></display:column>
    <display:column  property="validateList" title="Validate List" headerClass="sortable"></display:column>
  <%if(vl.equals("Y")){ %> 
   <display:column title="Actions">
         <span class="hotspot" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/edit.gif" border="0" alt="" onclick=" editshow('<%=test%>','windows')" /> </span>
   
         <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/delete.gif" border="0" alt="" onclick="deletesegment(this,<%=test%>)"/> </span>
   
         <span class="hotspot" onmouseover="tooltip.show('<strong>Add Values</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/add.gif" border="0" alt="" onclick="addValues(this,'<%=test%>','windows')"/> </span>
   

         <span class="hotspot" onmouseover="tooltip.show('<strong>Show Values</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/showall.gif" border="0" alt="" onclick="showvalue(this,<%=test%>)"/> </span>
    </display:column>	
	
	<%}else{ %>
	
	<display:column title="Actions">
         <span class="hotspot" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/edit.gif" border="0" alt="" onclick=" editshow('<%=test%>','windows')" /> </span>
    
         <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/delete.gif" border="0" alt="" onclick="deletesegment(this,<%=test%>)"/> </span>
    
         <span class="hotspot" onmouseover="tooltip.show('<strong>Add Values</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/add.gif" border="0" alt="" onclick=""/> </span>
   
         <span class="hotspot" onmouseover="tooltip.show('<strong>Show Values</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/showall.gif" border="0" alt="" onclick=""/> </span>
    </display:column>
	
	<%}test++;%>
	</display:table>  
	
   </div>
  <table width=100% border="0" cellpadding="0" cellspacing="0">
  <br/>
  <tr class="tableHeadingNew">"<%=vsc%>" Segment Code Values<br />
  
  </tr></table>
 <%}%>
 <table>
 <tr>
 <td>
  <input type="hidden" value='<%=a%>' name="test" id='test1' />
  </td>
 
  </tr></table>
 
  
 <%
 if((valueList !=null && valueList .size()>0)) 
{int i=0;
%>

 <div id="divtablesty1" class="scrolldisplaytable" style="border: thin; overflow: scroll; width: 100%; height: 320px;">
<display:table name="<%=valueList%>" pagesize="<%=pageSize%>" class="displaytagstyle" sort="list"  id="acctst"   >
<display:setProperty name="paging.banner.some_items_found">
	 <span class="pagebanner">
	 <font color="blue">{0}</font> AddSegmentValue details displayed,For more code click on page numbers.
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
	<display:setProperty name="paging.banner.item_name" value="AddSegment Value"/>
	<display:setProperty name="paging.banner.items_name" value="AddSegment Values"/>
	<display:column property="segmentvalue" title="Segment Value" headerClass="sortable" ></display:column>
	<display:column property="segmentdesc" title="Segment Value Desc" headerClass="sortable" ></display:column>
	<display:column title="Action"> <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();" >
         <img src="<%=path%>/img/icons/delete.gif" border="0" alt="" onclick="deletesegmentValues(this,<%=i%>)"/> </span>
    </display:column>
<%i++; %>
</display:table>  
  </div>
 <%}%>
  
  <table width="739" id="hiddentablesty4" height="88">
 <tr><td colspan="15">  <table width="100%" height="15" border="0" cellpadding="0" cellspacing="0">
   <tr>
    <td height="15" width=100%   class="headerbluesmall">&nbsp;&nbsp;Edit Segments <br /></td>
  </tr>
  </table></td></tr>
 <tr>
    <td class="textlabels"><b>Segment Code</b></td>
    <td class="textlabels"><b>Description</b></td>
    <td class="textlabels"><b>Length</b></td>
    
  </tr>
  
  <tr>
    <td><html:text property ="scode" styleClass="textfieldstyle" value="<%=segCode%>"  readonly="true"/></td>
    <td><html:text property ="editdesc"  styleClass="textfieldstyle" value="<%=segDesc%>" /></td>
    <td><html:text property="editlen"   styleClass="textfieldstyle" value="<%=segLen%>"  readonly="true" /></td>
    <td><img src="<%=path%>/img/add.gif" border="0" onclick="updations()"  /></td>
  </tr>
    
</table>
</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
