<%@include file="../includes/jspVariables.jsp" %>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*"%>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title> Add Structure</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}
AcctStructure acctstructdomain = new AcctStructure();
String structurecode="";
String structuredesc="";
List struclist1 = null;
String msg="";
if(request.getAttribute("msg")!=null)
 {
   msg=(String)request.getAttribute("msg");
 }
if(request.getAttribute("struclist")!=null)
 {
 struclist1 =  (List)request.getAttribute("struclist");
 acctstructdomain = (AcctStructure)struclist1.get(0);
 }
 structurecode= acctstructdomain.getAcctStructureName();
 structuredesc = acctstructdomain.getAcctStructureDesc();
%>
<%@include file="../includes/baseResources.jsp" %>
<script type="text/javascript">

function addnew()
 {
  if( document.AddStructurePopUpForm.structure.value=="" &&  document.Addstructurepopup.structuredesc.value=="")
  { alert("Please enter the values in the fields");
    document.AddStructurePopUpForm.structure.value="";
    document.AddStructurePopUpForm.structure.focus();
    return;
  }
   document.AddStructurePopUpForm.buttonValue.value="add";
   document.AddStructurePopUpForm.submit();
  }
function load1()
 {
  document.AddStructurePopUpForm.structure.value="";
  document.AddStructurePopUpForm.structuredesc.value="";
  }
  function previous()
  {
        parent.parent.location.reload(); 
       
  }
</script>
<%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd" onload="load1()"   >
<html:form action="/AddStructure" name="AddStructurePopUpForm" type="com.gp.cvst.logisoft.struts.form.AddStructurePopUpForm" scope="request">
<html:hidden property="buttonValue"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" >Add Structure</tr>
<td>
<table width="100%"  border="0" cellpadding="0" cellspacing="0" >
<tr valign="top">
<%-- <td valign="top" colspan="8" align="right" style="padding-top:10px;padding-bottom:10px;"><img src= "<%=path%>/img/previous.gif" onclick="previous()" /></td>--%>
 <td class="error"><%=msg%></td>
 <td  valign="top" colspan="5" align="right" style="padding-top:3px;">
 <input type="button" name="button1" onclick="previous()" value="Go Back"  class="buttonStyleNew"/>
</td>
</tr>
</table>
<%--<table width="100%" height="15" border="0" cellpadding="0" cellspacing="0">--%>
<%--  <tr>--%>
<%--    <tr>--%>
<%--    <td class="headerbluelarge" colspan="3"></td>--%>
<%--    <td colspan="4" class="headerbluelarge"><a href="#"> </a></td>--%>
<%--    </tr>--%>
<%--  <tr>--%>
<%--    --%>
<%--  </tr>--%>
<%--</table>--%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px;padding-top:10px;border-left:0px;border-right:0px;">
<%--<tr class="tableHeadingNew" >Add Structure</tr>--%>
 <tr>
    <td>
       <table width="100%" border="0" cellpadding="0" cellspacing="0" >
       <tr>
               <td class="textlabels"><b>Structure Code</b></td>
                <td  ><html:text property="structure"  styleClass="textfieldstyle"/></td>
                <td class="textlabels"><b>Structure Desc</b></td>
                 <td ><html:text property="structuredesc"    styleClass="textfieldstyle"   /></td>
       
       <tr>
       
       <table>
     </td>
   
   </tr>
   <tr>
      <table width="100%" border="0" cellpadding="0" cellspacing="0" >
       <tr>
     
<%--         <td valign="top" colspan="8" align="center" style="padding-top:10px;"><img src= "<%=path%>/img/save.gif" onclick="addnew()" />--%>
        
       <td  valign="top" colspan="8" align="center" style="padding-top:10px;">
<input type="button" name="button2" onclick="addnew()" value="Save"  class="buttonStyleNew">
</td>
<%--         <img src= "<%=path%>/img/previous.gif" onclick="previous()" />--%>
         </tr>
        </table>
   </tr>
 </table>








<%--  <tr>--%>
<%--    <td class="textlabels">&nbsp;</td>--%>
<%--    <td class="textlabels">&nbsp;</td>--%>
<%--    <td colspan="2" class="textlabels">&nbsp;</td>--%>
<%--    <td class="textlabels">&nbsp;</td>--%>
<%--  </tr>--%>
<%--  <tr>--%>
<%--    <td class="textlabels"><b>Structure Code</b></td>--%>
<%--    <td class="textlabels"><b>Structure Desc</b></td>--%>
<%--    <td class="textlabels">&nbsp;</td>--%>
<%--  </tr>--%>
<%--  <tr>--%>
<%--    <td  ><html:text property="structure"  styleClass="textfieldstyle"   /></td>--%>
<%--    <td ><html:text property="structuredesc"    styleClass="textfieldstyle"   /></td>--%>
<%--    <td>&nbsp; &nbsp;<img src= "<%=path%>/img/save.gif" onclick="addnew()" /></td>--%>
<%--    <td>&nbsp; &nbsp;<img src= "<%=path%>/img/previous.gif" onclick="previous()" /></td>--%>
<%--  </tr>--%>
<%--</table> --%>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-left:0px;border-right:0px;border-bottom:0px;">
<tr class="tableHeadingNew" height="90%">List Of Add Structure</tr> 
 <td>
<%if((struclist1!=null && struclist1.size()>0)) 
 { 
 int i=0;
%>
<div id="divtablesty1" class="scrolldisplaytable">
<display:table name="<%=acctstructdomain%>" pagesize="<%=pageSize%>" class="displaytagstyleNew" sort="list"    >
  <display:setProperty name="paging.banner.some_items_found">
   <span class="pagebanner">
     <font color="blue">{0}</font> Add Structure details displayed,For more code click on page numbers.
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
  <display:setProperty name="basic.msg.empty_list">
    <span class="pagebanner">
      No Records Found.
     </span> 
   </display:setProperty>
   <display:setProperty name="paging.banner.placement" value="bottom" />
   <display:setProperty name="paging.banner.item_name" value="Add Account Structure"/>
   <display:setProperty name="paging.banner.items_name" value="Add Account Structures"/>
   <display:column  title="Structure Code"  headerClass="sortable"><%=structurecode%></display:column>
   <display:column title="Structure Description"   headerClass="sortable" ><%=structuredesc%></display:column>
 </display:table>  
</div>
<%} %>
</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
