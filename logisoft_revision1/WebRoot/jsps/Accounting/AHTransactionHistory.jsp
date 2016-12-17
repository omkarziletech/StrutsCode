<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,com.gp.cvst.logisoft.struts.form.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Tranasaction History</title>
        <%

        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        if (path == null) {
            path = "../..";
        }
        %>
        <%
        DBUtil dbUtil = new DBUtil();
        String buttonValue = "";
        session.setAttribute("SourcecodeList", dbUtil.getSourcecodeList());
        List itemdetails = new ArrayList();
        if (session.getAttribute("AHitemdetails") != null) {
            itemdetails = (List) session.getAttribute("AHitemdetails");

        }
        String Sdate = "";
        String Edate = "";

        if (request.getAttribute("startDate") != null) {
            Sdate = (String) request.getAttribute("startDate");

        }
        if (request.getAttribute("startDate") != null) {
            Edate = (String) request.getAttribute("endDate");
        }

        if (!(request.getAttribute("buttonValue") == null)) {
            buttonValue = (String) request.getAttribute("buttonValue");

        }

        List aclist = (List) session.getAttribute("aclist");
        String acct = "";
        String desc = "";
        if (aclist != null && aclist.size() > 0) {
            acct = (String) aclist.get(0);

        } else {
            acct = "";
        }
        if (aclist != null && aclist.size() > 0) {
            desc = (String) aclist.get(1);

        } else {
            desc = "";
        }
        %>
        <%@include file="../includes/baseResources.jsp" %>


        <script language="javascript" type="text/javascript">
            function sub()
            {
                if(document.forms[0].datefrom.value=="")
                {
                    alert("please Enter AccountStarting date ");
                    document.TransactionHistoryForm.datefrom.focus();
                    return false;
                }
                if(document.forms[0].dateto.value=="")
                {
                    alert("please Enter AccountEnding date ");
                    document.TransactionHistoryForm.dateto.focus();
                    return false;
                }
                if(document.forms[0].sourcecode.value=="")
                {
                    alert("please select Sourcecode ");
                    document.TransactionHistoryForm.sourcecode.focus();
                    return false;
                }
                var DATEFROM=document.forms[0].datefrom.value;
                var DATETO=document.forms[0].dateto.value;
                if(DATEFROM>DATETO)
                {
                    alert("Please Check the date");
                    document.TransactionHistoryForm.dateto.focus();
                    return false;
                }
                document.forms[0].buttonValue.value="AHgo";
                document.TransactionHistoryForm.submit();
            }
            function submit()
            {
                document.forms[0].buttonValue.value="showall";
                document.TransactionHistoryForm.submit();
            }
        </script>
    </head>

    <body class="whitebackgrnd">
        <html:form action="/TransHistory" name="TransactionHistoryForm" type="com.gp.cvst.logisoft.struts.form.TransactionHistoryForm" scope="request">
            <html:hidden property="buttonValue"/>
            <table width=815 border="0" cellpadding="0" cellspacing="0">

                <tr class="textlabels">
                    <td width="375" align="left" class="headerbluelarge">Transaction Histroy </td>
                    <td width="47" align="left" class="headerbluelarge">&nbsp;</td>
                    <td width="90" align="left" class="headerbluelarge">&nbsp;</td>
                    <td width="109" align="left" class="headerbluelarge"><html:link page="/jsps/Accounting/AccountHistory.jsp"><img src="<%=path%>/img/previous.gif" alt="go" width="72" height="23" border="0" /></html:link></td>
                    <td width="54" align="left" class="headerbluelarge">&nbsp;</td>
                    <td width="61" align="left" class="headerbluelarge">&nbsp;</td>
                    <td width="79" align="left" class="headerbluelarge">&nbsp;</td>
                </tr>
                <tr class="textlabels">
                    <td colspan="7"align="left" class="headerbluelarge">&nbsp;</td>
                </tr>
            </table>
            <table width=100%  border="0" cellpadding="0" cellspacing="0">
                <tr>
                <td colspan="7"></td></tr>
                <tr>
                    <td height="12" colspan="7"  class="headerbluesmall">&nbsp;Transaction History Details </td>
                </tr>
            </table>
            <table width=815  border="0" cellpadding="0" cellspacing="0">

                <tr >
                    <td width="73" height="15" class="textlabels">&nbsp;</td>
                    <td colspan="5">&nbsp;</td>
                    <td colspan="2" class="textlabels">&nbsp;</td>
                    <td>&nbsp;</td>
                    <td colspan="2">&nbsp;</td>
                    <td width="55">&nbsp;</td>
                </tr >
                <tr >
                    <td height="29" class="textlabels">  Account </td>
                    <td colspan="3"><html:text property="account"  value="<%=acct%>" readonly="true"></html:text></td>
                    <td colspan="2" class="textlabels">Description</td>
                    <td colspan="2"><html:text property="desc"  value="<%=desc%>" readonly="true"></html:text></td>
                    <td>&nbsp;</td>
                    <td colspan="2">&nbsp;</td>
                </tr >
                <tr >
                    <td height="27" class="textlabels">Date From </td>
                    <td width="66"><html:text property="datefrom" styleId="txtcal" value="<%=Sdate%>" /></td>
                    <td width="36" align="center"><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
                    <td width="51"><span class="textlabels">Date To</span></td>
                    <td width="64"><html:text property="dateto" styleId="txtcal1" value="<%=Edate%>" /></td>
                    <td width="36" align="center"><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal1" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
                    <td width="112" class="textlabels">Source Code</td>
                    <td width="170"><html:select property="sourcecode"  >
                            <html:optionsCollection name="SourcecodeList" />
                    </html:select></td>
                    <td ><img src="<%=path%>/img/go1.gif" border="0"  onclick="sub()" /></td>
                    <td colspan="2">&nbsp;</td>
                    <td><img src="<%=path%>/img/showall.gif" border="0"  onclick="submit()" /></td>
                </tr >

                <tr class="textlabels">
                    <td >&nbsp;</td>
                    <td colspan="5">&nbsp;</td>
                    <td colspan="2">&nbsp;</td>
                    <td>&nbsp;</td>
                    <td colspan="2">&nbsp;</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td  colspan ="13" height="15"  class="headerbluesmall">&nbsp;&nbsp;List Of Transaction History </td>
                </tr>
            </table>
            <%
        if ((itemdetails != null && itemdetails.size() > 0)) {
            int i = 0;
            String beg = "Beg";
            %>
            <div id="divtablesty1" class="scrolldisplaytable" style="overflow:scroll; width:100%; height:350px;">
                <display:table name="<%=itemdetails%>" pagesize="<%=pageSize%>" class="displaytagstyle" sort="list"  id="accounthistory">
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
                    <display:setProperty name="paging.banner.item_name" value="Account"/>
                    <display:setProperty name="paging.banner.items_name" value="Accounts"/>
                    <%-- 	 <%if(i==0){ %>

        <!--	<display:column title="Period" ><c:out value="<%=beg%>"/></display:column>
    <display:column title="Date" ></display:column>
    <display:column title="SourceCode"   ></display:column>
    <display:column title="Reference" ></display:column>
    <display:column title="Description"></display:column>
    <display:column title="Debit" ></display:column>
    <display:column title="Credit"></display:column>
    <display:column title="Balance"><c:out value="<%=bl%>"/>
    <display:column title="Currency"></display:column>
    <display:column title="SourceAmount"></display:column>

            <%} %>-->--%>
                    <%if (i >= 0) {


                    <display:column  property="period" paramId="paramid" title="Period" ></display:column>
                    <display:column property="date" title="Date" ></display:column>
                    <display:column property="sourcecode" title="SourceCode"   ></display:column>
                    <display:column property="reference" title="Reference"></display:column>
                    <display:column property="description" title="Description"></display:column>
                    <display:column property="debit" title="Debit"></display:column>
                    <display:column property="credit" title="Credit"></display:column>
                    <display:column property="balance" title="Balance"></display:column>
                    <display:column property="currency" title="Currency"></display:column>
                    <display:column property="sourceamount" title="SourceAmount"></display:column>
                    <%}
                i++;%>
                </display:table>
            </div>
            <%}%>
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
