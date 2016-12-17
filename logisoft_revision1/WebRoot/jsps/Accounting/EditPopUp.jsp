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
      String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
      if (path == null) {
        path = "../..";
      }
      String asId = (String) session.getAttribute("getEditsegaccstructid");
      String editPath = path + "/accountStructure.do";
      DBUtil dbUtil = new DBUtil();
      request.setAttribute("AcctStructureList", dbUtil.getAcctStructureList());
      List AcctStructureList1 = null;
      AcctStructureList1 = (List) request.getAttribute("AcctStructureList1");

      String segid = (String) request.getAttribute("acctid1");
      String segCode = "";
      String segDesc = "";
      String segLen = "";
      String acctdesc = "";
      acctdesc = (String) request.getAttribute("acctStructDesc");
      List Acctstrcut = (List) session.getAttribute("AcctSturct");
      SegmentvalueBean valuelist = null;
      Segment segdomain = new Segment();
      String accountStructure = "";
      if (request.getParameter("accountStructure") != null) {
        accountStructure = (String) request.getParameter("accountStructure");
      }

      AccountStructureBean segstructreDomain = null;
      SegmentvalueBean segvalueBean = null;
      String editacctdescId = (String) request.getParameter("acctStructName");
      String segId = (String) session.getAttribute("acctid1");
      String ab = (String) request.getAttribute("buttonValue");
      if (ab != null && ab.equals("completed")) {
    %>
    <script>
      window.opener.document.accountStructureForm.submit();
      self.close();
    </script>
    <% }
    %>

    <%@include file="../includes/baseResources.jsp" %>

    <script type="text/javascript">

      function editpopup()
      {
    alert("Editing Description for the segments")    ;
        document.EditPopupForm.editId.value=<%=segId%>;
        document.EditPopupForm.buttonValue.value="editpopup";
        document.EditPopupForm.submit(); 
      }
    </script>
  </head>
  <body class="whitebackgrnd"  >
    <html:errors/>
    <html:form action="/editpopup" name="EditPopupForm" type="com.gp.cvst.logisoft.struts.form.EditPopupForm" scope="request">
      <html:hidden property="buttonValue"/>
      <html:hidden property="editId" />
      <table class="tableBorderNew" width="100%">
        <tr class="tableHeadingNew">Edit Segments</tr>
        <tr ><td colspan="15">  <table width="100%" height="15" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="15" width="100%">&nbsp;&nbsp;<br /></td>
              </tr>
            </table></td></tr>
        <tr>
          <td class="textlabels"><b>Segment Code</b></td>
          <td class="textlabels"><b>Description</b></td>
          <td class="textlabels"><b>Length</b></td>

        </tr>

        <tr>
          <td><html:text   property ="editsegcode" styleClass="textfieldstyle"  readonly="true" value="${segmentcode}"  /></td>
          <td><html:text  property ="editsegdescription"   styleClass="textfieldstyle"  value="${segmentDesc}"  /></td>
          <td><html:text  property="editseglen"   styleClass="textfieldstyle"  readonly="true" value="${segmentLength}"  /></td>
          <td valign="top" colspan="4" align="center" style="padding-top:10px;">
            <input type="button" name="search" onclick ="editpopup()" value="Save" class="buttonStyleNew"/>
          </td>
        </tr>

      </table>
      <html:hidden property="accountStructure" value="<%=accountStructure%>"/>
    </html:form>
  </body>
  <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>