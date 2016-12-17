<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*,com.gp.cvst.logisoft.struts.form.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  
<title> Add Values</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
SegmentValues segvaluedomain= new SegmentValues();
String id="";
String message="";
String code="";
String desc="";
String code1="";
String seglenth="";

List segstructreDomain =null;
if((List)session.getAttribute("segList")!=null)
 {
 segstructreDomain =(List)session.getAttribute("segList");
 if(!segstructreDomain.isEmpty())
 {
 segvaluedomain = (SegmentValues)segstructreDomain.get(0);
 code=segvaluedomain.getSegmentValue();
desc=segvaluedomain.getSegmentValueDesc();
 }
 }

if(request.getAttribute("message")!=null)
 {
  message=(String)request.getAttribute("message");
 }
 if(request.getParameter("acctStructName")!=null)
 {
 id=(String)request.getParameter("acctStructName");
 }
if(request.getAttribute("ActStructId1")!=null)
 {
  id=(String)request.getAttribute("ActStructId1");
 } 


if(request.getParameter("segcode")!=null)
{
code1 = request.getParameter("segcode");

}
if(request.getParameter("value")!=null)
{

  String indexval=request.getParameter("value");
  if(session.getAttribute("AcctSturct")!=null)
  {
   List segmentList=(List)session.getAttribute("AcctSturct");
   if(!segmentList.isEmpty())
   {
    AccountStructureBean asBean =(AccountStructureBean)segmentList.get(Integer.valueOf(indexval)-1);
    code1=asBean.getSeg_code();
   }
   
  }

}
 if(request.getAttribute("segcode")!=null)
 {
  code1=(String)request.getAttribute("segcode");
 
 }
 if(request.getParameter("seglenth")!=null)
 {
 seglenth=request.getParameter("seglenth");
 }
if(request.getAttribute("seglength")!=null)
 {
  seglenth= (String)request.getAttribute("seglength");
 }
 List addvaluelist=null;
if(session.getAttribute("addvaluelist")!=null)
{
  addvaluelist=(List)session.getAttribute("addvaluelist");
  session.removeAttribute("addvaluelist");
}
%>
<%@include file="../includes/baseResources.jsp" %>

<script type="text/javascript">


  
  function segmentvalues()
 {
   
   document.AddValueForm.seglength.value="<%=seglenth%>";
   var str=document.AddValueForm.addcode.value;
   
    
     document.AddValueForm.id.value="<%=id%>";
     
     document.AddValueForm.segcode.value="<%=code1%>";
     
     var segmCode=document.AddValueForm.addcode.value;
    
     segmCode=segmCode.replace(/^\s*|\s*$/,"");
     document.AddValueForm.addcode.value=segmCode;
   
     if(document.AddValueForm.seglength.value==segmCode.length)
     {
    
     document.AddValueForm.buttonValue.value="segmentValues";
     document.AddValueForm.submit();     
   	 }
   	 else
   	 {
   	   alert("Please Check the Segment Length");
   	 }
   	
  }  
  function goback(){
        parent.parent.GB_hide();
        parent.parent.refreshPage();
  }
</script>
</head>
<body class="whitebackgrnd"   > 
<html:errors/>
<html:form action="/addvalue" name="AddValueForm" type="com.gp.cvst.logisoft.struts.form.AddValueForm" scope="request"  >
<html:hidden property="id" />
<html:hidden property="segcode"/>
<html:hidden property="seglength" />
<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" >Add Segment Values</tr>
<td>
<table width="100%" height="15" border="0" cellpadding="0" cellspacing="0">

    <tr>
    <td  colspan="3"><b> </b></td>
    <td colspan="4" class="headerbluelarge"><a href="#"> </a></td>
    </tr>
   <tr>
    <td   colspan="8">&nbsp; </td>
  </tr>
  <tr>
  <td><font color=blue><%=message%></font>
  </td>
  </tr>
</table>
<table  width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-right:0px;border-left:0px;">
<tr class="tableHeadingNew">Add Segment values for SegmentCode "<%=code1%>"</tr>
  <tr>
    <td class="textlabels">&nbsp;</td>
    <td class="textlabels">&nbsp;</td>
    <td colspan="2" class="textlabels">&nbsp;</td>
    <td class="textlabels">&nbsp;</td>
  </tr>
  <tr>
    <td class="textlabels"><b>Segment Value</b></td>
    
    <td class="textlabels"><b>Description</b></td>
    
    <td class="textlabels">&nbsp;</td>
  </tr>
  <tr>
    <td width="127" ><html:text property="addcode"  styleClass="textfieldstyle"   /></td>
    <td width="127"><html:text property="adddesc"    styleClass="textfieldstyle"   /></td>
    
     <td>
     	 <input type="button" name="search" onclick="segmentvalues()" value="Save" class="buttonStyleNew"/>
     	 <input type="button" name="Go Back" onclick="goback()" value="Go Back" class="buttonStyleNew"/>
     </td>
   
  </tr>
 </table> 
 

 
 <br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-left:0px;border-right:0px;border-bottom:0px;">
<tr class="tableHeadingNew" height="90%">List of SegmentValues</tr> 
 <td>
<%if((addvaluelist!=null && addvaluelist.size()>0)) 
 { 
 int i =0; 
%>
 
 <div id="divtablesty1" class="scrolldisplaytable">
<display:table name="<%=addvaluelist%>" pagesize="<%=pageSize%>" class="displaytagstyle" sort="list"    >
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Add SegmentValue details displayed,For more code click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Add SegValue"/>
  			<display:setProperty name="paging.banner.items_name" value="Add SegValues"/>
		
    <display:column      title="Segment value" property="segmentvalue"   headerClass="sortable"></display:column>
	<display:column    title="Segment Desc"  property="segmentdesc" headerClass="sortable"></display:column>
	 
<%i++; %>
</display:table>  
  </div>
  <%} %>
  </td>
  </table>
  
  
  </table>
  
  
<html:hidden property="buttonValue"/>
<html:hidden property="segcodeId" />
</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
