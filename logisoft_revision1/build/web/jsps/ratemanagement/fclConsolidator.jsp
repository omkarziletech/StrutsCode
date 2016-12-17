<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cong.logisoft.domain.FclConsolidator"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/baseResources.jsp" %>
 <% 
 String path = request.getContextPath();
 List fclconsolidatorList=new ArrayList();
 String originTerminal=null;
 String destinationPort=null;
 if(request.getAttribute("originalTerminal")!=null)
 {
 originTerminal=(String)request.getAttribute("originalTerminal");
 }
if(request.getAttribute("destinationPort")!=null)
 {
 destinationPort=(String)request.getAttribute("destinationPort");
 }
 if(session.getAttribute("fclconsolidator")!=null)
 {
 fclconsolidatorList=(List)session.getAttribute("fclconsolidator");
 }
 int i=0;
 if(request.getAttribute("completed")!=null && request.getAttribute("completed").equals("completed"))
 {
 %>
 <script type="text/javascript">
parent.parent.GB_hide();
</script>
 <%
 }
 %>
<html> 
	<head>
		<title>JSP for FclConsolidatorForm form</title>
		<script type="text/javascript">
		function getsave(val,val1,val2)
		{
		var varcharge=new Array();
		var varrollToCharge=new Array(); 
		var vardisplay=new Array(); 
		var varexcludeFromTotal=new Array(); 
		for(var i=0;i<val;i++){
			
		varcharge[i]=document.getElementById("charge"+i).value;
		varrollToCharge[i]=document.getElementById("rollToCharge"+i).value;
		vardisplay[i]=document.getElementById("display"+i).value;
		varexcludeFromTotal[i]=document.getElementById("excludeFromTotal"+i).value;
	
		}
		document.searchFCLForm.hiddencharge.value=varcharge;
		document.searchFCLForm.hiddenrollToCharge.value=varrollToCharge;
		document.searchFCLForm.hiddendisplay.value=vardisplay;
		document.searchFCLForm.hiddenexcludeFromTotal.value=varexcludeFromTotal;
		document.searchFCLForm.hiddenTerminalNumber.value=val1;
		document.searchFCLForm.hiddendestSheduleNumber.value=val2;
		document.searchFCLForm.buttonValue.value="save";
		document.searchFCLForm.submit();
		}
		function add(val,val1,val2){
		var varcharge=new Array();
		var varrollToCharge=new Array(); 
		var vardisplay=new Array(); 
		var varexcludeFromTotal=new Array(); 
		for(var i=0;i<val;i++){
			
		varcharge[i]=document.getElementById("charge"+i).value;
		varrollToCharge[i]=document.getElementById("rollToCharge"+i).value;
		vardisplay[i]=document.getElementById("display"+i).value;
		varexcludeFromTotal[i]=document.getElementById("excludeFromTotal"+i).value;
	
		}
		document.searchFCLForm.hiddencharge.value=varcharge;
		document.searchFCLForm.hiddenrollToCharge.value=varrollToCharge;
		document.searchFCLForm.hiddendisplay.value=vardisplay;
		document.searchFCLForm.hiddenexcludeFromTotal.value=varexcludeFromTotal;
		document.searchFCLForm.hiddenTerminalNumber.value=val1;
		document.searchFCLForm.hiddendestSheduleNumber.value=val2;
		document.searchFCLForm.buttonValue.value="add";
		document.searchFCLForm.submit();
		}
		
		function deleteConsolidator(obj){
		document.searchFCLForm.index.value=obj.name;
		document.searchFCLForm.buttonValue.value="delete";
		document.searchFCLForm.submit();
		}
		
		function getReset(){
		document.searchFCLForm.buttonValue.value="reset";
		document.searchFCLForm.submit();
		}
		</script>
		<%@include file="../includes/resources.jsp" %>
	</head>
	<body><br>
		<html:form action="/fclConsolidator" scope="request">
		
		<table width="100%" class="tableBorderNew">
		<tr><td>
		<table width="100%" cellpadding="0" cellspacing="0">
		<tr class="tableHeadingNew"><td>Consolidator Screen</td>
		<td align="right"><input type="button" value="Add" class="buttonStyleNew" onclick="add('<%=fclconsolidatorList.size()%>','<%=originTerminal%>','<%=destinationPort%>')"/></td>
		</tr>
		</table>
		<table width="100%" >
		<tr class="style2">
		<td >Charge<br></td>
		<td>Roll To Charge<br></td>
		<td>Display<br></td>
		<td>Exclude From Total<br></td>
		</tr>
		<%
		while(fclconsolidatorList!=null && fclconsolidatorList.size()>i)
		{
		FclConsolidator fclConsolidator=(FclConsolidator)fclconsolidatorList.get(i);
		 %>
		<tr>
		<td><input name="charge<%=i%>" value="<%=fclConsolidator.getCharge()%>" id="charge<%=i%>" size="5"/></td>
		<td><input name="rollToCharge<%=i%>" value="<%=fclConsolidator.getRollToCharge()%>" size="5"></input></td>
		<td><input name="display<%=i%>" value="<%=fclConsolidator.getDisplay()%>" size="5"></input></td>
		<td><input name="excludeFromTotal<%=i%>" value="<%=fclConsolidator.getExcludeFromTotal()%>" size="5"/>
		     <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" 
		           onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/delete.gif" border="0"
		           onclick="deleteConsolidator(this)" name="<%=i%>" /></span></td>
		</tr>
		<%--<td><input type="button" class="buttonStyleNew" value="Delete" onclick="deleteConsolidator(this)" name="<%=i%>"></td>--%>
		<%
		i++;
		} %>
		</table>
		</td></tr></table>
		<table>
		<tr>
			<td style="padding-left:110px;"><input type="button" value="Save" class="buttonStyleNew" onclick="getsave('<%=fclconsolidatorList.size()%>','<%=originTerminal%>','<%=destinationPort%>')"/>
			<input type="button" value="Reset" class="buttonStyleNew" onclick="getReset()"></td>
		</tr>
		
		</table>
		<html:hidden property="buttonValue"/>
		<html:hidden property="hiddencharge"/>
		<html:hidden property="hiddenrollToCharge"/>
		<html:hidden property="hiddendisplay"/>
		<html:hidden property="hiddenexcludeFromTotal"/>
		<html:hidden property="hiddenTerminalNumber" />
		<html:hidden property="hiddendestSheduleNumber"/>
		<html:hidden property="index"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

