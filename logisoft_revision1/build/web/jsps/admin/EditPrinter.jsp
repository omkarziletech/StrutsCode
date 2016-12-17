<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.domain.Printer"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	DBUtil dbUtil=new DBUtil();
	
	List printerTypeList = new ArrayList();
	List printerNameList = new ArrayList();
	com.gp.cong.logisoft.domain.Printer printer = new com.gp.cong.logisoft.domain.Printer();
	
	List printList=(List)session.getAttribute("printList");
	if(session.getAttribute("printer")!=null)
	{
	   printer=(com.gp.cong.logisoft.domain.Printer)session.getAttribute("printer");
	   
	}
	List printerAddList=null;
	if(session.getAttribute("printerAddList")!=null)
	{
	 printerAddList=(List)session.getAttribute("printerAddList");
	}
	if(printer.getPrinterType()!=null )
	{
		printerNameList = dbUtil.getPrintList(printer.getPrinterType(),printerAddList);
	}
	request.setAttribute("printerTypeList",dbUtil.getPrinterList());
	request.setAttribute("printerNameList",printerNameList);
	
%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Printer Page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	function submit2()
   {
    if(document.editPrinterForm.printerType.value=="0")
   {
     alert("Please select the printer type");
     
     return;
   } 
    
  document.editPrinterForm.buttonValue.value="printerselected";
  document.editPrinterForm.submit();
   }
    function save()
   {
   
    document.editPrinterForm.buttonValue.value="save";
  document.editPrinterForm.submit();
  }
	</script>
</head>  
  
  <body class="whitebackgrnd">
		<html:form action="/editPrinter" scope="request">
			<table width="600" height="213" border="0" cellpadding="0" cellspacing="0" >
  <tr>
    <td width="600" height="213" align="left" valign="top" bgcolor="#E6F2FF"  >
    <table width="600" height="89"  cellpadding="0" cellspacing="0">
      <tr>
        <td height="52" align="left" valign="middle">
        <table width="600" border="0" cellspacing="0" cellpadding="0">
          <tr>
            
            <td width="15" class="headerbluelarge">Enter Printer Information </td>
              
            <td width="50"><img src="<%=path%>/img/add.gif" border="0" onclick="save()"/></td>
          
          </tr>

        </table></td>
        <table width="450" height="8" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="6" align="left" valign="middle"></td>
                <td></td>
                <td align="right"></td>
                <td height="6" align="left"></td>

                <td></td>
              </tr>
              <tr>
               <td width="117" height="21"><label class="style2">&nbsp;Printer Type</label></td>
                <td width="144"><html:select property="printerType" styleClass="selectboxstyle" onchange="submit2()">
                 <html:optionsCollection name="printerTypeList"/>
                </html:select></td>
                 <td width="5">&nbsp;</td>
                <td width="117" height="21"><label class="style2">&nbsp;Printer Name</label></td>
                <td width="144"><html:select property="printerName" styleClass="selectboxstyle">
                 <html:optionsCollection name="printerNameList"/>
                </html:select>
                </td>
              </tr>
          </table>  
          <div id="divtablesty1" style="border:thin;overflow:scroll;width:200%;height:370px;">
          <table width="600" border="0" cellpadding="0" cellspacing="0">
          <% 
         int i=0;
        
    %>
           <tr>
        <td height="5"  class="headerbluesmall">&nbsp;&nbsp;List of Printers</td>
     </tr>
         <display:table name="<%=printerAddList%>" class="displaytagstyle" id="printertable" pagesize="<%=pageSize%>"  > 
	     <display:column property="printerType" title="PRINTER TYPE" />
		 <display:column property="printerName" title="PRINTER NAME" />
	     <display:column><img name="<%=i%>" src="<%=path%>/img/toolBar_delete_hover.gif" border="0" onclick="confirmdelete(this)"/></display:column>
  <% i++;%>
		</display:table>
        </table></div>   
         </tr>           
      </table>
      <html:hidden property="buttonValue"/>
		</html:form>
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

