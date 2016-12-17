<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.domain.BlClauses,java.util.List,java.util.ArrayList"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%String path = request.getContextPath(); 
 List addclauseslist=new ArrayList();
 if(request.getAttribute("addclauseslist")!=null)
 {
 addclauseslist=(List)request.getAttribute("addclauseslist");
 }
 String bol="";
 if(request.getAttribute("bol")!=null){
 bol=(String)request.getAttribute("bol");
 }
 String view="";
if(session.getAttribute("view")!=null)
{
view=(String)session.getAttribute("view");
}
 if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
%>    
<script>
	parent.parent.GB_hide();
		self.close();
		
</script>
	<%} 
 %>
<html> 
	<head>
		<title>JSP for BlClauseForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
	<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    		    dojo.require("dojo.io.*");
			dojo.require("dojo.event.*");
			dojo.require("dojo.html.*");
		</script>
	<script language="javascript" type="text/javascript">
	function submitAdd1()
	{
	document.blClauseForm.buttonValue.value="add";
	document.blClauseForm.submit();
	}
	function submit1()
	{
	document.blClauseForm.buttonValue.value="previous";
	document.blClauseForm.submit();
	}
	function getVesselName(ev)
	  {
	    if(event.keyCode==9 || event.keyCode==13)
	    {
document.blClauseForm.buttonValue.value="popup";
  document.blClauseForm.submit();
	    }
	 }
	  function disabled(val1)
   { 
   
  if(val1== 3)
 {
        var imgs = document.getElementsByTagName('img');
      
     for(var k=0; k<imgs.length; k++)
     {
     
      if(imgs[k].id != "previous")
      {
     
         imgs[k].style.visibility = 'hidden';
      }
     }
     
     var input = document.getElementsByTagName("input");
     for(i=0; i<input.length; i++)
   {
   
      input[i].readOnly=true;
      input[i].style.color="blue";
    
     }
     var textarea = document.getElementsByTagName("textarea");
     for(i=0; i<textarea.length; i++)
   {
     textarea[i].readOnly=true;
      textarea[i].style.color="blue";
       
    }
     var select = document.getElementsByTagName("select");
     
     for(i=0; i<select.length; i++)
   {
    select[i].disabled=true;
   select[i].style.backgroundColor="blue";
     
     }
      document.getElementById("save").style.visibility = 'hidden';
     document.getElementById("add").style.visibility = 'hidden';
    }
    
    }
    
    function makeFormBorderless(form) {
			var element;
			for (var i = 0; i < form.elements.length; i++) {
			element = form.elements[i];
			if(element.type == "button"){
			if(element.value=="Add"){
				element.style.visibility="hidden";
				}
			}
		}
		return false;
	}
	function getChrageCodeDesc(ev){ 
   	if(event.keyCode==9 || event.keyCode==13){
   	 		var params = new Array();
	 		params['requestFor'] = "CommodityDetails";
	 		params['code'] = document.getElementById("clause"+ev).value;
	 		params['codeType'] = 21 ;
	 		params['ev'] = ev ;
  			var bindArgs = {
  			url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
  			error: function(type, data, evt){alert("error");},
			mimetype: "text/json",
  			content: params
 		};
		var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "populateTruckerInfo1");
		}
	}
	function populateTruckerInfo1(type, data, evt) {
 		
 		if(data.commodityDesc){
 		document.getElementById("description"+data.ev).value=data.commodityDesc;
    	}
	}
	</script>
	</head>
	
	<body class="whitebackgrnd" />
	
	
<html:form action="/blClause" styleId="fclclauses" scope="request">
<table width="100%" border="0" class="tableBorderNew">
<tr class="tableHeadingNew colspan="2">Clauses Details</tr>
  <tr><td>   
            <table width="100%">
			   <tr class="textlabels">
                   <td width="90%" align="right">
                   <input type="button" value="Save" id="save" class="buttonStyleNew" onclick="submit1()"/>
                   </td>
                   <td width="90%">
                   <input type="button" value="Add" id="add" class="buttonStyleNew" onclick="submitAdd1()"/>
                   </td>
               </tr>
            </table> 
            
            <%--<table width="815" border="0" cellpadding="0" cellspacing="0" id="records">
            
              <tr>
                 <td height="12" colspan="2"  class="headerbluesmall">&nbsp;&nbsp;Clauses Details </td> 
              </tr>
              
           </table>--%> 
         
      <table width="100%">           
           <%
			if(addclauseslist!=null && addclauseslist.size()>0)
           {%> 
      <table width="100%">
       <tr>
           <td>
<%--           <div id="divtablesty1" style="border:thin;width:100%;height:100%">--%>
          <table border="0" cellpadding="0" cellspacing="0">
              <% int i=0;
               %>

		<display:table  name="<%=addclauseslist%>"   id="lineitemtable" class="displaytagstyle" pagesize="<%=pageSize%>"> 
		
		<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> LineItem details displayed,For more LineItems click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="LineItem"/>
  			<display:setProperty name="paging.banner.items_name" value="LineItems"/>
	<%
	String code="";
	
	String description="";
	String text="";
	
	if(addclauseslist!=null && addclauseslist.size()>0)
	{
	BlClauses fclblCharges=(BlClauses)addclauseslist.get(i);
	if(fclblCharges.getCode()!=null)
	{
	code=fclblCharges.getCode();
	}
	if(fclblCharges.getDesciption()!=null)
	{
	description=fclblCharges.getDesciption();
	}
	if(fclblCharges.getText()!=null)
	{
	text=fclblCharges.getText();
	}
	
	} %>
      <display:column title="Code">
	 <input type="text" name="clause<%=i%>" id="clause<%=i%>"  value="<%=code%>" onkeydown="getChrageCodeDesc('<%=i %>')" size="4"/>
      <dojo:autoComplete formId="blClauseForm"
		   textboxId="clause<%=i%>"
		   action="<%=path%>/actions/getVesselNo.jsp?tabName=BL_CLAUSE&index=<%=i%>"/></display:column>
      <display:column title="Description" >
      
	 <input  name="description" id="description<%=i %>" value="<%=description%>"></display:column>
	  <display:column title="Text"><html:text property="text" value="<%=text%>"></html:text></display:column>
	  
	<%i++; %>
    </display:table>
    </table></td>
    </tr>
    </table></table>   

	<%} %>		
			
			<html:hidden property="buttonValue" styleId="buttonValue" />
			</td></tr></table>	
			<%if(view.equals("3"))
{ %>

<%} %>
<html:hidden property="bol" value="<%=bol%>"/>
		</html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

