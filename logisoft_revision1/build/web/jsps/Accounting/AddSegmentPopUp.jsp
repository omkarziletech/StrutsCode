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
  
<title> Add Segment</title>
<%

String path = request.getContextPath();

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}

 AccountStructureBean acctbean=new AccountStructureBean();
Segment segdomain= new Segment();
List segstructreDomain =null;
String segCode="";
String segDesc="";
String segLen="";
String asId = (String)request.getParameter("acctStructName");
if(request.getAttribute("asId2")!=null)
{
asId=(String)request.getAttribute("asId2");

}

if((List)request.getAttribute("segmentdomainlist")!=null)
 {
  segstructreDomain =(List)request.getAttribute("segmentdomainlist");
 segdomain = (Segment)segstructreDomain.get(0);
 }
 segCode= segdomain.getSegmentCode();
 segDesc= segdomain.getSegmentDesc();
 segLen= String.valueOf(segdomain.getSegment_leng());
 List abclist=null;
 String msg="";
  if(request.getAttribute("msg")!=null)
   {
    msg=(String)request.getAttribute("msg");
   }
   if(session.getAttribute("AcctSturct")!=null)
   {
   abclist=(List)session.getAttribute("AcctSturct");
    if(!abclist.isEmpty())
    {
    acctbean=(AccountStructureBean)abclist.get(0);
    }
   }
   List segList=null;
   if(session.getAttribute("addseglist")!=null)
   {
   segList=(List)session.getAttribute("addseglist");
   }
   if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed")){
%>
<script>
		parent.parent.GB_hide();
		parent.parent.refreshPage();
</script>
<%} %>

<%@include file="../includes/baseResources.jsp" %>

<script type="text/javascript">

function AddSegStructure()
 {  
    if(document.addSegmentForm.addSegmentcCode.value=="")
    {
      alert("Please Enter Segment Code");
      document.addSegmentForm.addSegmentcCode.focus();
      return false; 
    }else if(document.addSegmentForm.addSegmentLength.value=="0"){
       alert("Please Enter Segment Lenght");
       document.addSegmentForm.addSegmentLength.focus();
       return false;
    }else if(document.addSegmentForm.addSegmentDesc.value==""){
       alert("Please Enter Segment Description");
       document.addSegmentForm.addSegmentDesc.focus();
       return false;
    }
    else
    {
    document.addSegmentForm.buttonValue.value="AddSegStructure";
    document.addSegmentForm.asId.value="<%=asId%>";
    document.addSegmentForm.submit();
    }
  }   
function CloseSegment(){
 document.addSegmentForm.buttonValue.value="close";
  document.addSegmentForm.submit();
}
</script>
</head>

<body class="whitebackgrnd"   > 
<html:errors/>
<html:form action="/addSegment" name="addSegmentForm" type="com.gp.cvst.logisoft.struts.form.addSegmentForm" scope="request">
<html:hidden property="buttonValue"/>
<html:hidden property="index"/>
 <html:hidden property="asId"/>
 
 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
<tr class="tableHeadingNew" >Add Segment </tr>
<td>

<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-left:0px;border-right:0px;padding-left:4px;padding-top:4px;" > 
<%--<tr class="tableHeadingNew" height="90%">Add Segment</tr> --%>
<tr>
  <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" >
         <tr>
                 <td class="textlabels"><b>Segment Code</b></td>
                 <td  ><html:text property="addSegmentcCode"  styleClass="textfieldstyle"   /></td>
                 <td class="textlabels"><b>Description</b></td>
                  <td width="127"><html:text property="addSegmentDesc"    styleClass="textfieldstyle"   /></td>
                
         </tr>
          <tr>
              <td class="textlabels"><b>Length</b></td> 
              <td><html:text property="addSegmentLength"  styleClass="textfieldstyle"  /></td>   
              <td class="textlabels"><b>Validate List</b></td>
              <td  align="left"><html:checkbox   property="validateList" value="Y" onclick="" ></html:checkbox></td>
              
          </tr>
            <tr>  
            <td valign="top" colspan="8" align="center" style="padding-top:10px;">
            <input type="button" name="search" onclick="AddSegStructure()" value="Save" class="buttonStyleNew"/>
<input type="button" name="Close" onclick="CloseSegment()" value="Close" class="buttonStyleNew"/></td>
              </tr>
      </table>

 </tr>
</table>
<br/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-left:0px;border-right:0px;border-bottom:0px;">
<tr class="tableHeadingNew" height="90%">List of Add Segments</tr> 
 <td>
  <%if((segList!=null && segList.size()>0)) 
  { 
  int i =0;
  %>
 
<div id="divtablesty1" class="scrolldisplaytable">
<display:table name="<%=segList%>" pagesize="<%=pageSize%>" class="displaytagstyle" sort="list"     >
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    				<font color="blue">{0}</font> Add Segment details displayed,For more code click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Add Segment"/>
  			<display:setProperty name="paging.banner.items_name" value="Add Segments"/>
   <display:column   property="seg_code" title="SegCode" sortable="true" headerClass="sortable"></display:column>
   <display:column   property="seg_desc" title="SegDesc" sortable="true" headerClass="sortable"></display:column>
   <display:column   property="seg_leng" title="SegLength" sortable="true" headerClass="sortable"></display:column>
  
<%i++;%>
  </display:table>  
  </div>
  <%}%>
 </html:form>
 </body>
 	<%@include file="../includes/baseResourcesForJS.jsp" %>
 </html>
