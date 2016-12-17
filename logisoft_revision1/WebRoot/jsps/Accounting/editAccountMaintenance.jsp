<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,java.util.*,java.text.SimpleDateFormat,com.gp.cvst.logisoft.beans.AccountMaintenenceBean"%>
<%@ page import="com.gp.cvst.logisoft.hibernate.dao.*" %>
<%@ page import="com.gp.cvst.logisoft.domain.*" %>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="com.gp.cong.logisoft.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="acctForm" class="com.gp.cvst.logisoft.struts.form.AcctMaintenanceForm" scope="request"></jsp:useBean>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
            <title> Edit Account Maintenace</title>
        <%@include file="../includes/baseResources.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <%
            DBUtil dbUtil = new DBUtil();
            request.setAttribute("Segment1", dbUtil.getSegment1());
            request.setAttribute("Segment2", dbUtil.getSegment2());
            request.setAttribute("Segment3", dbUtil.getSegment3());
            request.setAttribute("Accttype", dbUtil.getAccttype());
            request.setAttribute("SubLedgerList", dbUtil.getUniqueSubLedgerList());
            List accg = null;
            List closetoacct = null;
            if (request.getAttribute("accountgroup") != null) {
                accg = (List) request.getAttribute("accountgroup");
            } else {
                accg = (List) request.getAttribute("AcctGroup");
            }
            request.setAttribute("accg", accg);
            String groupdesc = null;
            groupdesc = (String) request.getAttribute("Groupdesc");
            request.setAttribute("groupdesc", groupdesc);
            request.setAttribute("normalbalance", dbUtil.getnormalbalance());
            request.setAttribute("defaultcurrency", dbUtil.getdefaultcurrency());
            closetoacct = (List) request.getAttribute("closetoacct");
            request.setAttribute("closetoacct", dbUtil.getClosetoAcctList());
            List editAccountdetailslist = (List) session.getAttribute("editAccountdetailslist");
            List subLedgeraddList = null;

            String actype = "";
            String acgroup = "";
            String defCurrency = "";
            String actbal = "";
            String clacct = "";
            String segm1 = "", segm2 = "", segm3 = "", segm4 = "", segm5 = "";
            String actdesc = "";

            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {

                if (request.getAttribute("acctype") != null) {
                    actype = (String) request.getAttribute("acctype");
                } else {
                    actype = (String) editAccountdetailslist.get(4);
                    accg = dbUtil.getAcctgroupByType(actype);
                    request.setAttribute("accg", accg);
                }

            } else {
                actype = "";
            }
            String mc = "";
            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {
                mc = (String) editAccountdetailslist.get(11);

                if (mc.equals("Yes")) {
                    acctForm.setMulticurrency("on");
                }
            }
            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {
                String abc = (String) editAccountdetailslist.get(9);
                if (abc == null) {
                }
                if ((String) editAccountdetailslist.get(9) != null) {
                    acctForm.setControlacct("on");

                    String acct = (String) editAccountdetailslist.get(0);

                    subLedgeraddList = dbUtil.getSubLedgeraddList(acct);
                    session.setAttribute("subLedgeraddList", subLedgeraddList);
                } else {
                    acctForm.setControlacct("off");

                }
            }
            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {
                if (editAccountdetailslist.get(7).equals("Active")) {
                    acctForm.setAcctstatus("Active");
                }
                if (editAccountdetailslist.get(7).equals("Inactive")) {
                    acctForm.setAcctstatus("Inactive");
                }
            }
            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {

                if (request.getAttribute("acctgroup") != null) {
                    acgroup = (String) request.getAttribute("acctgroup");

                } else {
                    acgroup = (String) editAccountdetailslist.get(5);

                }

                if (request.getAttribute("Groupdesc") != null) {
                    groupdesc = (String) request.getAttribute("Groupdesc");
                } else {
                    groupdesc = (String) editAccountdetailslist.get(6);
                }
            } else {
                acgroup = "";
            }

            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {
                actbal = (String) editAccountdetailslist.get(8);

            } else {
                actbal = "";
            }

            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {
                defCurrency = (String) editAccountdetailslist.get(12);

            } else {
                defCurrency = "";
            }
            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {
                clacct = (String) editAccountdetailslist.get(10);

            } else {
                clacct = "";
            }
            if (editAccountdetailslist != null && editAccountdetailslist.size() > 0) {
                actdesc = (String) editAccountdetailslist.get(2);

            } else {
                actdesc = "";
            }
            List actList = (List) session.getAttribute("actList");

            if (actList != null && actList.size() > 0) {
                if (actList.size() == 2) {
                    segm1 = (String) actList.get(0);
                    segm2 = (String) actList.get(1);
                }
                if (actList.size() == 3) {
                    segm1 = (String) actList.get(0);
                    segm2 = (String) actList.get(1);
                    segm3 = (String) actList.get(2);
                }
                if (actList.size() == 4) {
                    segm1 = (String) actList.get(0);
                    segm2 = (String) actList.get(1);
                    segm3 = (String) actList.get(2);
                    segm4 = (String) actList.get(3);
                }
                if (actList.size() == 5) {
                    segm1 = (String) actList.get(0);
                    segm2 = (String) actList.get(1);
                    segm3 = (String) actList.get(2);
                    segm4 = (String) actList.get(3);
                    segm5 = (String) actList.get(4);
                }

            }

            // to get segment list
            List segmentList = null;
            SegmentDAO segDAO = new SegmentDAO();
            if (session.getAttribute("eSegList") != null) {
                segmentList = (List) session.getAttribute("eSegList");
                String segvalidate1 = "";

                String segvalidate2 = "";
                String segvalidate3 = "";
                String segvalidate4 = "";
                String segvalidate5 = "";
                if (segmentList.size() == 2) {
                    int s1 = (Integer) segmentList.get(0);
                    segvalidate1 = segDAO.getValidateListforSegment(s1);
                    if (segvalidate1.equals("Y") && s1 != 0) {
                        request.setAttribute("sv1", dbUtil.getSegment(s1));
                    }

                    int s2 = (Integer) segmentList.get(1);
                    segvalidate2 = segDAO.getValidateListforSegment(s2);
                    if (segvalidate2.equals("Y") && s2 != 0) {
                        request.setAttribute("sv2", dbUtil.getSegment(s2));
                    }

                }
                if (segmentList.size() == 3) {
                    int s1 = (Integer) segmentList.get(0);
                    segvalidate1 = segDAO.getValidateListforSegment(s1);
                    if (segvalidate1.equals("Y") && s1 != 0) {
                        request.setAttribute("sv1", dbUtil.getSegment(s1));
                    }

                    int s2 = (Integer) segmentList.get(1);
                    segvalidate2 = segDAO.getValidateListforSegment(s2);
                    if (segvalidate2.equals("Y") && s2 != 0) {
                        request.setAttribute("sv2", dbUtil.getSegment(s2));
                    }

                    int s3 = (Integer) segmentList.get(2);
                    segvalidate3 = segDAO.getValidateListforSegment(s3);
                    if (segvalidate3.equals("Y") && s3 != 0) {
                        request.setAttribute("sv3", dbUtil.getSegment(s3));
                    }

                }
                if (segmentList.size() == 4) {
                    int s1 = (Integer) segmentList.get(0);
                    segvalidate1 = segDAO.getValidateListforSegment(s1);
                    if (segvalidate1.equals("Y") && s1 != 0) {
                        request.setAttribute("sv1", dbUtil.getSegment(s1));
                    }

                    int s2 = (Integer) segmentList.get(1);
                    segvalidate2 = segDAO.getValidateListforSegment(s2);
                    if (segvalidate2.equals("Y") && s2 != 0) {
                        request.setAttribute("sv2", dbUtil.getSegment(s2));
                    }

                    int s3 = (Integer) segmentList.get(2);
                    segvalidate3 = segDAO.getValidateListforSegment(s3);
                    if (segvalidate3.equals("Y") && s3 != 0) {
                        request.setAttribute("sv3", dbUtil.getSegment(s3));
                    }
                    int s4 = (Integer) segmentList.get(3);
                    segvalidate4 = segDAO.getValidateListforSegment(s4);
                    if (segvalidate4.equals("Y") && s4 != 0) {
                        request.setAttribute("sv4", dbUtil.getSegment(s4));
                    }

                }
                if (segmentList.size() == 5) {
                    int s1 = (Integer) segmentList.get(0);
                    segvalidate1 = segDAO.getValidateListforSegment(s1);
                    if (segvalidate1.equals("Y") && s1 != 0) {
                        request.setAttribute("sv1", dbUtil.getSegment(s1));
                    }

                    int s2 = (Integer) segmentList.get(1);
                    segvalidate2 = segDAO.getValidateListforSegment(s2);
                    if (segvalidate2.equals("Y") && s2 != 0) {
                        request.setAttribute("sv2", dbUtil.getSegment(s2));
                    }

                    int s3 = (Integer) segmentList.get(2);
                    segvalidate3 = segDAO.getValidateListforSegment(s3);
                    if (segvalidate3.equals("Y") && s3 != 0) {
                        request.setAttribute("sv3", dbUtil.getSegment(s3));
                    }
                    int s4 = (Integer) segmentList.get(3);
                    segvalidate4 = segDAO.getValidateListforSegment(s4);
                    if (segvalidate4.equals("Y") && s4 != 0) {
                        request.setAttribute("sv4", dbUtil.getSegment(s4));
                    }
                    int s5 = (Integer) segmentList.get(4);
                    segvalidate5 = segDAO.getValidateListforSegment(s5);
                    if (segvalidate5.equals("Y") && s5 != 0) {
                        request.setAttribute("sv5", dbUtil.getSegment(s5));
                    }
                }
            }

            Date dateLastModified = new Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            String dateofModification = sdf.format(dateLastModified);

        %>


        <script language="javascript" type="text/javascript">

            function fun1()
            {
                if (document.accountMaintenance.controlacct.checked) {
                    toggleTable('hiddentablesty1', 1);
                } else {
                    fundouble();
                }

            }

            function fundouble()
            {
                document.getElementById('hiddentablesty1').style.visibility = 'hidden';
            }

            function submit1()
            {
                document.forms[0].buttonValue.value = "editAccttypeSelected";
                document.accountMaintenance.submit();
            }
            function submit2()
            {
                if (document.forms[0].acctgroup.value == "")
                {
                    alert("please select Account Group");
                    return false;
                }
                document.forms[0].buttonValue.value = "editAcctgroupSelected";
                document.accountMaintenance.submit();
            }
            function addSubledger() {
                //toggleTable('hiddentablesty',1);

                if (document.accountMaintenance.subledger.value == "")
                {
                    alert("please Select the SubLedger type");
                    return;
                }

                document.accountMaintenance.buttonValue.value = "addSubledger";
                document.accountMaintenance.submit();
            }

            function confirmdelete(obj) {
                var rowindex = obj.parentNode.parentNode.rowIndex;
                //var x=document.getElementById('subLedger').rows[rowindex].cells;


                document.accountMaintenance.index.value = obj.name;
                document.accountMaintenance.buttonValue.value = "delete";

                var result = confirm("Are you sure you want to delete this subledger");
                if (result) {
                    document.accountMaintenance.submit();
                }
            }


            function save1()
            {
                if (document.accountMaintenance.controlacct.checked) {
                    var rowCount = document.getElementById('subledgerTableCount').value;
                    if (rowCount == 0) {
                        alert("Cannot save account - you must select a subledger if the Control Account box is checked");
                        return;
                    }
                }
                if (document.forms[0].acctgroup.value == "") {
                    alert("Account cannot be saved - you must select Account Group");
                    return;
                }
                if (document.forms[0].acctstructure.value == "")
                {
                    alert("please enter Account Structure");
                    document.accountMaintenance.acctstructure.focus();
                    return false;
                }
                if (document.forms[0].acctdesc.value == "")
                {
                    alert("please enter Account Description");
                    document.accountMaintenance.acctdesc.focus();
                    return false;
                }


                if (document.forms[0].accttype.value == "")
                {
                    alert("Please enter Account Type");

                    document.accountMaintenance.accttype.focus();
                    return false;
                }


                if (document.forms[0].accttype.value == 'Income Statement')
                {
                    if (document.accountMaintenance.closetoacct.value == '') {
                        alert("Please select an Account Type");
                        document.accountMaintenance.closetoacct.focus();
                        return false;
                    }
                }

                if (document.forms[0].acctstatus.value == "Active")
                {
                    if (document.forms[0].acctgroup.value == "")
                    {
                        alert("please enter Account Group");
                        document.accountMaintenance.acctgroup.focus();
                        return false;
                    }
                }

                if (document.forms[0].seg1.value == "")
                {
                    alert("please enter seg1 value");
                    document.accountMaintenance.seg1.focus();
                    return false;
                }
                if (document.forms[0].seg2.value == "")
                {
                    alert("please enter seg2 value ");
                    document.accountMaintenance.seg1.focus();
                    return false;
                }
                if (document.forms[0].seg3.value == "")
                {
                    alert("please enter seg3 value ");
                    document.accountMaintenance.seg1.focus();
                    return false;
                }
                var check = CheckforAccount();
                if (check == false)
                {
                    return false;
                }
                else
                {

                    document.forms[0].buttonValue.value = "update";
                    document.accountMaintenance.submit();
                }

            }
            function CheckforAccount()
            {
                if (confirm("Do you Want to save?") == true)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            function displaydate()
            {

                document.accountMaintenance.datelastmodified.value = "<%=dateofModification%>";
                if (document.accountMaintenance.controlacct.checked)
                {
                    fun1();
                    if (document.accountMaintenance.controlacct.value != null)
                    {
                        toggleTable('hiddentablesty', 1);
                    }
                }
            }
            function segChange()
            {

                alert("Do you want to change Account Number,which causes Either creation of New Account or Updation of an account");
            }

        </script>
    </head>

    <body class="whitebackgrnd" onload="displaydate()">
        <html:form  action="/editacctMaintenance" name="accountMaintenance" type="com.gp.cvst.logisoft.struts.form.AcctMaintenanceForm" scope="request">
            <html:hidden property="buttonValue"/>
            <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>Edit Account Maintenance</td>
                    <td valign="top" align="right" colspan="5">
                        <input type="button" name="button1" onclick="window.location.href = '${path}/jsps/Accounting/ChartOfAccounts.jsp'" value="Go Back"  class="buttonStyleNew"></input>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Account Structure</td>
                    <td>
                        <input name="acctstructure" type="text" class="textlabelsBoldForTextBox"  value="<%=editAccountdetailslist.get(1)%>" readonly="true"/>
                    </td>
                    <td>Account</td>
                    <td>
                        <%if (segmentList != null && segmentList.size() == 2) {
                                if (request.getAttribute("sv1") != null) {%>
                        <html:select property="seg1" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv1" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg1"  maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%if (request.getAttribute("sv2") != null) {%>
                        <html:select property="seg2" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv2" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg2" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%} else if (segmentList != null && segmentList.size() == 3) {
                            if (request.getAttribute("sv1") != null) {%>
                        <html:select property="seg1" value="<%=segm1%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv1" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg1" value="<%=segm1%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%if (request.getAttribute("sv2") != null) {%>
                        <html:select property="seg2" value="<%=segm2%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv2" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg2"  value="<%=segm2%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%if (request.getAttribute("sv3") != null) {%>
                        <html:select property="seg3" value="<%=segm3%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv3" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg3" value="<%=segm3%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%} else if (segmentList != null && segmentList.size() == 4) {
                            if (request.getAttribute("sv1") != null) {%>
                        <html:select property="seg1" value="<%=segm1%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv1" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg1"  value="<%=segm1%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%if (request.getAttribute("sv2") != null) {%>
                        <html:select property="seg2" value="<%=segm2%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv2" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg2" value="<%=segm2%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%if (request.getAttribute("sv3") != null) {%>
                        <html:select property="seg3" value="<%=segm3%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv3" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg3" value="<%=segm3%>" maxlength="10" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <% if (request.getAttribute("sv4") != null) {%>
                        <html:select property="seg4" value="<%=segm4%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv4" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg4" value="<%=segm4%>" maxlength="10" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%} else if (segmentList != null && segmentList.size() == 5) {
                            if (request.getAttribute("sv1") != null) {%>
                        <html:select property="seg1" value="<%=segm1%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv1" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg1"  value="<%=segm1%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%if (request.getAttribute("sv2") != null) {%>
                        <html:select property="seg2" value="<%=segm2%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv2" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg2" value="<%=segm2%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%if (request.getAttribute("sv3") != null) {%>
                        <html:select property="seg3" value="<%=segm3%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv3" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg3" value="<%=segm3%>"  maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <% if (request.getAttribute("sv4") != null) {%>
                        <html:select property="seg4" value="<%=segm4%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv4" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg4" value="<%=segm4%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <% if (request.getAttribute("sv5") != null) {%>
                        <html:select property="seg5" value="<%=segm5%>" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv5" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                        <%} else {%>
                        <html:text property="seg5" value="<%=segm5%>" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"/>
                        <%}%>
                        <%}%>
                    </td>
                    <td>Acct Type</td>
                    <td>
                        <html:select property="accttype" styleId="accttype" value="<%=actype%>" onchange="submit1()" styleClass="dropdown_accounting" style="width:125px">
                            <html:optionsCollection name="Accttype" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Description</td>
                    <td><input type="text" name="acctdesc" value="<%=actdesc%>" class="textlabelsBoldForTextBox" style="width:125px"/></td>
                    <td>Acct Group</td>
                    <td>
                        <%if (accg != null) {%>
                        <html:select property="acctgroup"  value="<%=acgroup%>" onchange="submit2()" styleClass="dropdown_accounting" style="width:125px">
                            <html:optionsCollection name="accg" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <select name="acctgroup" class="textlabelsBoldForTextBox" style="width:125px">
                            <option value="" class="unfixedtextfiledstyle">Select Acct Group</option>
                        </select>
                        <%}%>
                    </td>
                    <td>Group Desc</td>
                    <td><%if (groupdesc == null) {
                            groupdesc = "";
                        }
                        %>
                        <input type="text" name="groupdesc" value="<%=groupdesc%>" class="textlabelsBoldForTextBox" style="width:125px"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Close To Acct</td>
                    <td>
                        <%if (closetoacct != null || actype.equals("Income Statement")) {%>
                        <html:select property="closetoacct" value="<%=clacct%>" styleClass="dropdown_accounting" style="width:125px">
                            <html:optionsCollection name="closetoacct" styleClass="unfixedtextfiledstyle"/>
                        </html:select><br />
                        <% }
                            if (actype.equals("Balance Sheet") || actype.equals("Retained Earnings")) {%>
                        <select name="closetoacct" class="dropdown_accounting" style="width:125px">
                            <option class="unfixedtextfiledstyle">Unknown</option>
                        </select>
                        <%}%>
                    </td>
                    <td>Normal Balance</td>
                    <td>
                        <html:select property="normalbalance" value="<%=actbal%>" styleClass="dropdown_accounting" style="width:125px">
                            <html:optionsCollection name="normalbalance" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                    <td>Default Currency</td>
                    <td>
                        <html:select property="defaultcurrency" value="<%=defCurrency%>" styleClass="dropdown_accounting" style="width:125px">
                            <html:optionsCollection name="defaultcurrency" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>ECU Report Category</td>
                    <td>
                        <input type="text" name="reportCategory" id="reportCategory"
                               class="textlabelsBoldForTextBox" style="width:125px;" value="${reportCategory}"/>
                        <input type="hidden" id="reportCategoryCheck" value="${reportCategory}"/>
                    </td>
                    <td>Date Created</td>
                    <td>
                        <input type="text" name="datecreated" value="<%=(String) editAccountdetailslist.get(3)%>" readonly="true" class="textlabelsBoldForTextBox" style="width:125px"/>
                    </td>
                    <td>Last Modified</td>
                    <td>
                        <input type="text" name="datelastmodified" id="date" readonly="true" class="textlabelsBoldForTextBox" style="width:125px"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Acct Status</td>
                    <td>
                        <html:radio property="acctstatus" value="Active" name="acctForm"/>Active
                        <html:radio property="acctstatus" value="Inactive" name="acctForm"/>Inactive
                    </td>
                    <td>Control Acct<html:checkbox property="controlacct" name="acctForm" value="ControlAcct" onclick="fun1()"/></td>
                    <td>Multi-Currency<html:checkbox property="multicurrency" value="Yes" name="acctForm"/></td>
                    <td colspan="2"></td>
                </tr>
                <tr class="textlabelsBold" id="hiddentablesty1">
                    <td>SubLedger</td>
                    <td>
                        <html:select property="subledger" styleClass="dropdown_accounting">
                            <html:optionsCollection name="SubLedgerList" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <img src="${path}/img/toolBar_add_hover.gif" align="middle" onclick="addSubledger()"/>
                    </td>
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <td align="center" colspan="6"><input type="button" name="save" onClick="save1()" value="Save"  class="buttonStyleNew"/></td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0"  class="tableBorderNew">
                <tr class="tableHeadingNew"><td>List of Subledgers</td></tr>
                <tr>
                    <td>
                        <div id="divtablesty1" class="scrolldisplaytable">
                            <c:set var="rowCount" value="0"/>
                            <%	if (null != subLedgeraddList && !subLedgeraddList.isEmpty()) {
                            %>
                            <display:table name="<%=subLedgeraddList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="subLedger">
                                <display:setProperty name="basic.msg.empty_list"><span class="pagebanner"/></display:setProperty>
                                <display:setProperty name="paging.banner.placement" value="bottom" />
                                <display:setProperty name="paging.banner.item_name" value="Subledger"/>
                                <display:setProperty name="paging.banner.items_name" value="Subledgers"/>
                                <display:column property="subledger" title="SubLedger List" />
                                <display:column/>
                                <display:column><img name="${rowCount}" src="${path}/img/toolBar_delete_hover.gif" border="0" onclick="confirmdelete(this)" /></display:column>
                                <c:set var="rowCount" value="${rowCount+1}"/>
                            </display:table>
                            <%}
                            %>
                            <input type="hidden" id="subledgerTableCount" value="${rowCount}"/>
                        </div>
                    </td>
                </tr>
            </table>
            <html:hidden property="index" />
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
    <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
    <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/accountMaintenance.js"></script>
</html>
