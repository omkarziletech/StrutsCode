<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="com.gp.cvst.logisoft.struts.form.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>BatchList</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
path="../..";
}
String batchId="";
if(request.getAttribute("batchId")!=null)
{
batchId=request.getAttribute("batchId").toString();
}
%>
<%@include file="../includes/baseResources.jsp" %>

<script language="javascript" type="text/javascript">
	function redirect(val){
		if(val==1){
			window.location.href="managerole.html";
		}
		if(val==0){
			window.location.href="editmanagerole.html";
		}
		if(val==2){
			testwindow= window.open ("roledetails.pdf", "mywindow",
       "location=1,status=1,scrollbars=1,width=600,height=400");
        testwindow.moveTo(350,200);
		}
		if(val==3){
			testwindow= window.open ("roledetails.pdf", "mywindow",
       "location=1,status=1,scrollbars=1,width=600,height=400");
        testwindow.moveTo(350,200);
		}
		}
		
			
        function confirmdelete()
	{
		var result = confirm('Are you sure you want to delete Generic Code Maintanance?');
		return result
	}
	 function confirmdelete1()
	{
		var result = confirm('This Terminal is associated to user. So cannot delete this Terminal');
		return result
	}
	function fun()
	{
	
	document.form1.totaldebit.disabled=false;
	document.form1.totalcredit.disabled=false;
	
	}
	function fun1()
	{ document.form1.select2.options[1].selected=true;
	}
	
	function save()
 { 
   if(document.JournalEntryForm.jecredit.value=="")
     {
     document.JournalEntryForm.jecredit.value="0";
     return true;
     }
     if(document.JournalEntryForm.jedebit.value=="")
     {
     document.JournalEntryForm.jedebit.value="0";
     return true;
     }
  document.JournalEntryForm.buttonValue.value="save";
  document.JournalEntryForm.submit();
  
  }
		</script>
</head>

<body class="whitebackgrnd">
<html:errors/>
<html:form action="/JournalEntryaddnew" name="JournalEntryForm" type="com.gp.cvst.logisoft.struts.form.JournalEntryForm" scope="request">
<html:hidden property="buttonValue"/>
<table width="815" border="0" cellpadding="0" cellspacing="0">
<tr class="textlabels">
  <td width="586" align="left" class="headerbluelarge">Enter Journal Entry Details </td>
  <td width="104" align="left" class="headerbluelarge"><img src="<%=path%>/img/save.gif" onclick="save()" border="0" style="cursor: pointer; cursor: hand;"/></td>
  <td width="125" align="left" class="headerbluelarge"><html:link page="/jsps/Accounting/JournalEntry.jsp"><img src="<%=path%>/img/previous.gif" width="72" height="23" border="0"/></html:link></td>
</tr>
<tr class="textlabels">
    <td colspan="3"align="left" class="headerbluelarge">&nbsp;</td>
  </tr>
  <tr>
  <td colspan="3"></td></tr>
</table>
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <tr>
    <td height="12"  class="headerbluesmall">&nbsp;&nbsp;Batch Details </td> 
  </tr>
</table>
    <table width="815" border="0" >
    <tr class="textlabels">
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td><div align="right">Period</div></td>
      <td><div align="right">
        <select name="year" class="unfixedtextfiledstyle" property="year">
          <option>2002</option>
          <option>2003</option>
          <option>2004</option>
          <option>2005</option>
          <option>2006</option>
          <option>2007</option>
        </select>
      </div></td>
      <td><select name="month" class="unfixedtextfiledstyle" property="month">
        <option>Jan</option>
        <option>Feb</option>
        <option>Mar</option>
        <option>Apr</option>
        <option>May</option>
        <option>Jun</option>
        <option>Jul</option>
        <option>Aug</option>
        <option>Sep</option>
        <option>Oct</option>
        <option>Nov</option>
        <option>Dec</option>
      </select></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr class="textlabels">
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td width="47">&nbsp;</td>
    </tr>
    <tr class="textlabels">
      <td width="95">Batch Number </td>
      <td width="96"><input type="text" class="textdatestyle"  name="batch" property="batch" value="<%=batchId %>"/></td>
      <td width="93">Journal Entry ID </td>
      <td width="84"><input type="text" class="textdatestyle"  name="journalid" property="journalid" value="12000" /></td>
      <td width="102"><p>Account Number </p>      </td>
      <td width="71"><input type="text" class="textdatestyle"  name="acctno" property="acctno"></td>
      <td width="94">Account Name </td>
      <td width="95"><input type="text" class="textdatestyle"  name="acctname" property="acctname"></td>
      <td>&nbsp;</td>
    </tr>
    <tr >
      <td height="19" class="textlabels">Debit</td>
      <td><input name="jedebit" type="text" class="textdatestyle" property="jedebit"  /></td>
      <td class="textlabels">Credit </td>
      <td><input name="jecredit" type="text" class="textdatestyle" value=""  property="jecredit"/></td>
      <td class="textlabels">Currency</td>
      <td><select name="jecurrency" size="1" property="jecurrency">
        <option value="USD">USD</option>
        <option value="INR">INR</option>
      </select></td>
      <td class="textlabels">Memo</td>
      <td><input name="memo" type="text" class="smalltextstyle" value="" property="memo" /></td>
      <td>&nbsp;</td>
    </tr>
  </table>

</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
