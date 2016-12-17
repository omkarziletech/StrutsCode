<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.hibernate.dao.SegmentDAO,com.gp.cvst.logisoft.domain.Segment"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="expires" content="0"/>
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
        <meta http-equiv="description" content="This is my page"/>
        <title> Account Maintenace</title>
        <% 
            List AcctStructureList = null;
            DBUtil dbUtil = new DBUtil();
            request.setAttribute("AcctStructureList", dbUtil.getAcctStructureList());
            AcctStructureList = (List) request.getAttribute("AcctStructureList");
            request.setAttribute("SubLedgerList", dbUtil.getUniqueSubLedgerList());
            request.setAttribute("Segment1", dbUtil.getSegment1());
            request.setAttribute("Segment3", dbUtil.getSegment3());
            request.setAttribute("Segment2", dbUtil.getSegment2());
            request.setAttribute("Accttype", dbUtil.getAccttype());

            //To validate the Text Boxes Segment of the Account this 5 variables are used
            int textbox1Length = 0;
            int textbox2Length = 0;
            int textbox3Length = 0;
            int textbox4Length = 0;
            int textbox5Length = 0;
            List accg = null;
            accg = (List) session.getAttribute("AcctGroup");
            request.setAttribute("accg", accg);
            List closetoAcct = null;
            closetoAcct = (List) request.getAttribute("closetoacct");
            request.setAttribute("closetoAcct", closetoAcct);
            String acctdesc = (String) request.getAttribute("acctdesc");
            String datelastmodified = (String) request.getAttribute("datelastmodified");
            String groupdesc = null;
            groupdesc = (String) request.getAttribute("Groupdesc");
            //request.setAttribute("groupdesc",groupdesc);
            List subLedgeraddList = null;
            subLedgeraddList = (List) session.getAttribute("subLedgeraddList");
            if (datelastmodified == null) {
                datelastmodified = "";
            }
            if (acctdesc == null) {
                acctdesc = "";
            }
            String message = "";
            if (request.getAttribute("message") != null) {
                message = (String) request.getAttribute("message");
            }
            List segmentList = null;
            SegmentDAO segDAO = new SegmentDAO();
            if (session.getAttribute("SegList") != null) {
                segmentList = (List) session.getAttribute("SegList");
                String segvalidate1 = "";
                String segm1 = "";
                String segvalidate2 = "";
                String segvalidate3 = "";
                String segvalidate4 = "";
                String segvalidate5 = "";

                if (segmentList.size() == 2) {
                    int s1 = (Integer) segmentList.get(0);
                    Segment segment = null;
                    segvalidate1 = segDAO.getValidateListforSegment(s1);
                    if (segvalidate1.equals("Y") && s1 != 0) {
                        request.setAttribute("sv1", dbUtil.getSegment(s1));
                        segment = segDAO.findById(s1);
                        textbox1Length = segment.getSegment_leng();
                    } else if (segvalidate1.equals("N") && s1 != 0) {
                        segment = segDAO.findById(s1);
                        textbox1Length = segment.getSegment_leng();
                    }

                    int s2 = (Integer) segmentList.get(1);
                    segvalidate2 = segDAO.getValidateListforSegment(s2);
                    if (segvalidate2.equals("Y") && s2 != 0) {
                        request.setAttribute("sv2", dbUtil.getSegment(s2));
                        segment = segDAO.findById(s2);
                        textbox2Length = segment.getSegment_leng();
                    } else if (segvalidate2.equals("N") && s2 != 0) {
                        segment = segDAO.findById(s2);
                        textbox2Length = segment.getSegment_leng();

                    }
                    segment = null;
                }
                if (segmentList.size() == 3) {
                    int s1 = (Integer) segmentList.get(0);
                    Segment segment = null;
                    segvalidate1 = segDAO.getValidateListforSegment(s1);
                    if (segvalidate1.equals("Y") && s1 != 0) {
                        request.setAttribute("sv1", dbUtil.getSegment(s1));
                        segment = segDAO.findById(s1);
                        textbox1Length = segment.getSegment_leng();
                    } else if (segvalidate1.equals("N") && s1 != 0) {
                        segment = segDAO.findById(s1);
                        textbox1Length = segment.getSegment_leng();
                    }

                    int s2 = (Integer) segmentList.get(1);
                    segvalidate2 = segDAO.getValidateListforSegment(s2);
                    if (segvalidate2.equals("Y") && s2 != 0) {
                        request.setAttribute("sv2", dbUtil.getSegment(s2));
                        segment = segDAO.findById(s2);
                        textbox2Length = segment.getSegment_leng();
                    } else if (segvalidate2.equals("N") && s2 != 0) {
                        segment = segDAO.findById(s2);
                        textbox2Length = segment.getSegment_leng();
                    }

                    int s3 = (Integer) segmentList.get(2);
                    segvalidate3 = segDAO.getValidateListforSegment(s3);
                    if (segvalidate3.equals("Y") && s3 != 0) {
                        request.setAttribute("sv3", dbUtil.getSegment(s3));
                        segment = segDAO.findById(s3);
                        textbox3Length = segment.getSegment_leng();
                    } else if (segvalidate3.equals("N") && s3 != 0) {
                        segment = segDAO.findById(s3);
                        textbox3Length = segment.getSegment_leng();
                    }
                    segment = null;
                }
                if (segmentList.size() == 4) {
                    int s1 = (Integer) segmentList.get(0);
                    Segment segment = new Segment();
                    segvalidate1 = segDAO.getValidateListforSegment(s1);
                    if (segvalidate1.equals("Y") && s1 != 0) {
                        request.setAttribute("sv1", dbUtil.getSegment(s1));
                        segment = segDAO.findById(s1);
                        textbox1Length = segment.getSegment_leng();
                    } else if (segvalidate1.equals("N") && s1 != 0) {

                        segment = segDAO.findById(s1);
                        textbox1Length = segment.getSegment_leng();
                    }

                    int s2 = (Integer) segmentList.get(1);
                    segvalidate2 = segDAO.getValidateListforSegment(s2);
                    if (segvalidate2.equals("Y") && s2 != 0) {
                        request.setAttribute("sv2", dbUtil.getSegment(s2));
                        segment = segDAO.findById(s2);
                        textbox2Length = segment.getSegment_leng();
                    } else if (segvalidate2.equals("N") && s2 != 0) {

                        segment = segDAO.findById(s2);
                        textbox2Length = segment.getSegment_leng();
                    }

                    int s3 = (Integer) segmentList.get(2);
                    segvalidate3 = segDAO.getValidateListforSegment(s3);
                    if (segvalidate3.equals("Y") && s3 != 0) {
                        request.setAttribute("sv3", dbUtil.getSegment(s3));
                        segment = segDAO.findById(s3);
                        textbox3Length = segment.getSegment_leng();
                    } else if (segvalidate3.equals("N") && s3 != 0) {
                        segment = segDAO.findById(s3);
                        textbox3Length = segment.getSegment_leng();

                    }
                    int s4 = (Integer) segmentList.get(3);
                    segvalidate4 = segDAO.getValidateListforSegment(s4);
                    if (segvalidate4.equals("Y") && s4 != 0) {
                        request.setAttribute("sv4", dbUtil.getSegment(s4));
                        segment = segDAO.findById(s4);
                        textbox4Length = segment.getSegment_leng();
                    } else if (segvalidate4.equals("N") && s4 != 0) {
                        segment = segDAO.findById(s4);
                        textbox4Length = segment.getSegment_leng();
                    }
                    segment = null;
                }
                if (segmentList.size() == 5) {
                    int s1 = (Integer) segmentList.get(0);
                    segvalidate1 = segDAO.getValidateListforSegment(s1);
                    Segment segment = new Segment();
                    if (segvalidate1.equals("Y") && s1 != 0) {
                        request.setAttribute("sv1", dbUtil.getSegment(s1));
                        segment = segDAO.findById(s1);
                        textbox1Length = segment.getSegment_leng();
                    } else if (segvalidate1.equals("N") && s1 != 0) {
                        segment = segDAO.findById(s1);
                        textbox1Length = segment.getSegment_leng();
                    }

                    int s2 = (Integer) segmentList.get(1);
                    segvalidate2 = segDAO.getValidateListforSegment(s2);
                    if (segvalidate2.equals("Y") && s2 != 0) {
                        request.setAttribute("sv2", dbUtil.getSegment(s2));
                        segment = segDAO.findById(s2);
                        textbox2Length = segment.getSegment_leng();
                    } else if (segvalidate2.equals("N") && s2 != 0) {
                        segment = segDAO.findById(s2);
                        textbox2Length = segment.getSegment_leng();
                    }

                    int s3 = (Integer) segmentList.get(2);
                    segvalidate3 = segDAO.getValidateListforSegment(s3);
                    if (segvalidate3.equals("Y") && s3 != 0) {
                        request.setAttribute("sv3", dbUtil.getSegment(s3));
                        segment = segDAO.findById(s3);
                        textbox3Length = segment.getSegment_leng();
                    } else if (segvalidate3.equals("N") && s3 != 0) {
                        segment = segDAO.findById(s3);
                        textbox3Length = segment.getSegment_leng();
                    }
                    int s4 = (Integer) segmentList.get(3);
                    segvalidate4 = segDAO.getValidateListforSegment(s4);
                    if (segvalidate4.equals("Y") && s4 != 0) {
                        request.setAttribute("sv4", dbUtil.getSegment(s4));
                        segment = segDAO.findById(s4);
                        textbox4Length = segment.getSegment_leng();
                    } else if (segvalidate4.equals("N") && s4 != 0) {
                        segment = segDAO.findById(s4);
                        textbox4Length = segment.getSegment_leng();
                    }
                    int s5 = (Integer) segmentList.get(4);
                    segvalidate5 = segDAO.getValidateListforSegment(s5);
                    if (segvalidate5.equals("Y") && s5 != 0) {
                        request.setAttribute("sv5", dbUtil.getSegment(s5));
                        segment = segDAO.findById(s5);
                        textbox5Length = segment.getSegment_leng();
                    } else if (segvalidate5.equals("N") && s5 != 0) {
                        segment = segDAO.findById(s5);
                        textbox5Length = segment.getSegment_leng();
                    }
                }
            }
            Date dateCreated = new Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            String dateofcreation = sdf.format(dateCreated);
        %>
        <%@include file="../includes/baseResources.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <script type="text/javascript">
            function fun1()
            {
                if (document.accountMaintenance.controlacct.checked) {
                    toggleTable('hiddentablesty1', 1);
                } else {
                    fundouble();
                }
            }
            function fun2()
            {
                toggleTable('hiddentablesty2', 1);
            }
            function fundouble()
            {
                document.getElementById('hiddentablesty1').style.visibility = 'hidden';
            }

            function submit1()
            {
                if (document.forms[0].accttype.value == "Select AcctType")
                {
                    alert("please enter Account Type");
                    return false;
                }

                document.forms[0].buttonValue.value = "AccttypeSelected";
                document.accountMaintenance.submit();
            }
            function submit2()
            {
                if (document.forms[0].acctgroup.value == "Select Acct Group")
                {
                    alert("please enter Account Group");
                    return false;
                }

                document.forms[0].buttonValue.value = "AcctgroupSelected";
                document.accountMaintenance.submit();
            }
            function displaydate()
            {

                document.accountMaintenance.datecreated.value = "<%=dateofcreation%>";
                document.accountMaintenance.datelastmodified.value = "<%=dateofcreation%>";
                if (document.accountMaintenance.controlacct.checked)
                {
                    fun1();
                    if (document.accountMaintenance.subledger.value != "")
                    {
                        toggleTable('hiddentablesty', 1);
                    }
                }
            }

            function save1()
            {
                if (document.accountMaintenance.controlacct.checked) {
                    if (document.accountMaintenance.subledger.value == "") {
                        alert("Cannot save account - you must select a suibledger if the Control Account box is checked");
                        return;
                    }
                }
                if (document.forms[0].acctgroup.value == "") {
                    alert("Account cannot be saved - you must select Account Group");
                    return;
                }
                if (document.forms[0].acctstructure.value == "") {
                    alert("please enter Account Structure");
                    document.accountMaintenance.acctstructure.focus();
                    return false;
                }
                if (document.forms[0].acctdesc.value == "") {
                    alert("please enter Account Description");
                    document.accountMaintenance.acctdesc.focus();
                    return false;
                }
                if (document.forms[0].accttype.value == "") {
                    alert("Please enter Account Type");

                    document.accountMaintenance.accttype.focus();
                    return false;
                }
                if (document.forms[0].acctstatus.value == "Active") {
                    if (document.forms[0].acctgroup.value == "") {
                        alert("please enter Account Group");
                        document.accountMaintenance.acctgroup.focus();
                        return false;
                    }
                }
                if (document.forms[0].closetoacct.value == "") {
                    alert("please enter CloseToAccount ");
                    document.accountMaintenance.closetoacct.focus();
                    return false;
                }

                if (document.accountMaintenance.seg1 != undefined && document.accountMaintenance.seg1.value == "") {
                    alert("Please Enter the Account Segments");
                    return false;

                } else if (document.accountMaintenance.seg2 != undefined && document.accountMaintenance.seg2.value == "") {
                    alert("Please Enter the Account Segments");
                    return false;
                } else if (document.accountMaintenance.seg3 != undefined && document.accountMaintenance.seg3.value == "") {
                    alert("Please Enter the Account Segments");
                    return false;
                } else if (document.accountMaintenance.seg4 != undefined && document.accountMaintenance.seg4.value == "") {
                    alert("Please Enter the Account Segments");
                    return false;
                } else if (document.accountMaintenance.seg5 != undefined && document.accountMaintenance.seg5.value == "") {
                    alert("Please Enter the Account Segments");
                    return false;
                }
                else
                {
                    //alert("NO Remarks");
                }

                // To check the Segment length.

                if (document.accountMaintenance.seg1 != undefined && document.accountMaintenance.seg1.value != "") {
                    var segmentCodeLength = "<%=textbox1Length%>";
                    // alert("------hi-"+document.accountMaintenance.seg1.value.length+"==="+segmentCodeLength);
                    if (document.accountMaintenance.seg1.value.length != segmentCodeLength)
                    {
                        alert("Please Check Segment 1 size");
                        return false;
                    }
                }
                if (document.accountMaintenance.seg2 != undefined && document.accountMaintenance.seg2.value != "") {
                    var segmentCodeLength = "<%=textbox2Length%>";
                    //alert("hi-2->"+document.accountMaintenance.seg2.value.length);
                    if (document.accountMaintenance.seg2.value.length != segmentCodeLength)
                    {
                        alert("Please Check Segment 2 size");
                        return false;
                    }
                }
                if (document.accountMaintenance.seg3 != undefined && document.accountMaintenance.seg3.value != "") {
                    var segmentCodeLength = "<%=textbox3Length%>";
                    if (document.accountMaintenance.seg3.value.length != segmentCodeLength)
                    {
                        alert("Please Check Segment 3 size");
                        return false;
                    }
                }
                if (document.accountMaintenance.seg4 != undefined && document.accountMaintenance.seg3.value != "") {
                    var segmentCodeLength = "<%=textbox4Length%>";
                    if (document.accountMaintenance.seg4.value.length != segmentCodeLength)
                    {
                        alert("Please Check Segment 4 size");
                        return false;
                    }
                }
                if (document.accountMaintenance.seg5 != undefined && document.accountMaintenance.seg2.value != "") {
                    var segmentCodeLength = "<%=textbox5Length%>";
                    if (document.accountMaintenance.seg5.value.length != segmentCodeLength)
                    {
                        alert("Please Check Segment 5 size");
                        return false;
                    }
                } else
                {
                    //alert("Submit");
                    document.accountMaintenance.buttonValue.value = "save";
                    document.accountMaintenance.submit();
                }
            }
            function addSubledger()
            {
                if (document.accountMaintenance.subledger.value == "")
                {
                    alert("please Select the SubLedger type");
                    return false;
                }
                if (document.accountMaintenance.seg1 == undefined || document.accountMaintenance.seg1.value == "")
                {
                    alert("please select the Account Number");
                    return false;
                }

                document.accountMaintenance.buttonValue.value = "addSubledger";
                document.accountMaintenance.submit();
            }


            function confirmdelete(obj)
            {
                var rowindex = obj.parentNode.parentNode.rowIndex;
                //var x=document.getElementById('subLedger').rows[rowindex].cells;
                document.accountMaintenance.index.value = obj.name;
                document.accountMaintenance.buttonValue.value = "delete";

                var result = confirm("Are you sure you want to delete subledger");
                if (result)
                {
                    document.accountMaintenance.submit();
                }
            }
            function submitacctstruct()
            {
                document.accountMaintenance.buttonValue.value = "accountStructure";
                document.accountMaintenance.submit();
            }
        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>

    <body class="whitebackgrnd" onload="displaydate()">
        <html:errors/>
        <html:form action="/acctMaintenance" name="accountMaintenance" type="com.gp.cvst.logisoft.struts.form.AcctMaintenanceForm" scope="request">
            <html:hidden property="buttonValue"/>
            <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew ">
                <tr class="tableHeadingNew">
                    <td>Account Maintenance</td>
                    <td align="right" colspan="5">
                        <input type="button" name="button1" onclick="window.location.href = '${path}/jsps/Accounting/ChartOfAccounts.jsp'" value="Go Back"  class="buttonStyleNew"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Account Structure</td>
                    <td>
                        <html:select property="acctstructure"  onchange="submitacctstruct()" styleClass="dropdown_accounting" style="width:125px;">
                            <html:optionsCollection name="AcctStructureList" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                    <td>Account</td>
                    <td>
                        <% if (segmentList != null && segmentList.size() == 2) {
                                if (request.getAttribute("sv1") != null) {%>
                        <html:select property="seg1" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv1" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg1"  maxlength="6" size="4" onmouseover="tooltip.show('<strong>Add Values</strong>',null,event);" onmouseout="tooltip.hide();" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <%if (request.getAttribute("sv2") != null) {%>
                        <html:select property="seg2" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv2" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text styleClass="textlabelsBoldForTextBox" property="seg2" maxlength="6" style="width:50px"></html:text>
                        <%}%>
                        <%} else if (segmentList != null && segmentList.size() == 3) {
                            if (request.getAttribute("sv1") != null) {%>
                        <html:select property="seg1" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv1" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg1"  maxlength="6"  size="4"></html:text>
                        <%}%>
                        <%if (request.getAttribute("sv2") != null) {%>
                        <html:select property="seg2" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv2" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg2"  styleClass="textlabelsBoldForTextBox" style="width:50px"  maxlength="6" ></html:text>
                        <%}%>
                        <%if (request.getAttribute("sv3") != null) {%>
                        <html:select property="seg3" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv3" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg3" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <%} else if (segmentList != null && segmentList.size() == 4) {
                            if (request.getAttribute("sv1") != null) {%>
                        <html:select property="seg1" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv1" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg1"  maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <%if (request.getAttribute("sv2") != null) {%>
                        <html:select property="seg2" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv2" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg2" maxlength="6" style="width:50px"  styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <%if (request.getAttribute("sv3") != null) {%>
                        <html:select property="seg3" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv3" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg3" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <% if (request.getAttribute("sv4") != null) {%>
                        <html:select property="seg4" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv4" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg4" maxlength="6"  size="4" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <%} else if (segmentList != null && segmentList.size() == 5) {
                            if (request.getAttribute("sv1") != null) {%>
                        <html:select property="seg1" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv1" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg1"  maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <%if (request.getAttribute("sv2") != null) {%>
                        <html:select property="seg2" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv2" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg2" maxlength="6" style="width:50px" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <%if (request.getAttribute("sv3") != null) {%>
                        <html:select property="seg3" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv3" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg3" size="4" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <% if (request.getAttribute("sv4") != null) {%>
                        <html:select property="seg4" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv4" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg4" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <% if (request.getAttribute("sv5") != null) {%>
                        <html:select property="seg5" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sv5" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <html:text property="seg5" maxlength="6" size="4" styleClass="textlabelsBoldForTextBox"></html:text>
                        <%}%>
                        <%}%>
                    </td>
                    <td>Acct Type </td>
                    <td>
                        <html:select  property="accttype" styleId="accttype" onchange="submit1()" styleClass="dropdown_accounting" style="width:125px;">
                            <html:optionsCollection name="Accttype" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Description</td>
                    <td><input name="acctdesc" type="text" value="<%=acctdesc%>" class="textlabelsBoldForTextBox" style="width:125px;"/></td>
                    <td>Acct Group </td>
                    <td>
                        <%if (accg != null) {%>
                        <html:select property="acctgroup" onchange="submit2()" styleClass="dropdown_accounting" style="width:125px;">
                            <html:optionsCollection name="accg" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                        <%} else {%>
                        <select name="acctgroup" class="dropdown_accounting" style="width:125px;">
                            <option value=""class="unfixedtextfiledstyle">Select Acct Group</option>
                        </select>
                        <%}%>
                    </td>
                    <td>Group Desc</td>
                    <%if (groupdesc == null) {
                            groupdesc = "";
                        }
                    %>
                    <td><html:text property="groupdesc" size="25" value="<%=groupdesc%>" styleClass="textlabelsBoldForTextBox" style="width:125px;"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Close To Acct</td>
                    <td>
                        <c:if test="${closetoAcct==null}">
                            <select name="closetoacct" class="dropdown_accounting" id="select9" style="width:125px;">
                                <option value="Unknown" class="unfixedtextfiledstyle">Unknown</option>
                            </select>
                        </c:if>
                        <c:if test="${closetoAcct!=null}">
                            <html:select property="closetoacct" styleClass="dropdown_accounting" style="width:125px;">
                                <html:optionsCollection name="closetoAcct" styleClass="unfixedtextfiledstyle"/>
                            </html:select>
                        </c:if>
                    </td>
                    <td>Normal Balance</td>
                    <td><select name="normalbalance" class="dropdown_accounting" style="width:125px;">
                            <option value="Credit" class="unfixedtextfiledstyle">Credit</option>
                            <option selected="selected" value="Debit">Debit</option>
                        </select>
                    </td>
                    <td>Default Currency</td><td colspan="4">
                        <select name="defaultcurrency" class="dropdown_accounting" id="select9" style="width:125px;">
                            <option value="USD" class="unfixedtextfiledstyle">USD</option>
                        </select>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>ECU Report Category</td>
                    <td>
                        <input type="text" name="reportCategory" id="reportCategory"
                               class="textlabelsBoldForTextBox" style="width:125px;"/>
                        <input type="hidden" id="reportCategoryCheck"/>
                    </td>
                    <td> Date Created</td>
                    <td>
                        <div style="float:left">
                            <input type="text" name="datecreated" id="txtcal5" style="width:125px;float:left" class="textlabelsBoldForTextBox"/>
                            <img src="${path}/img/CalendarIco.gif" alt="cal1" style="margin: 0 0 0 3px; float:left" width="14px" id="cal5" onmousedown="insertDateFromCalendar(this.id, 0);"/>
                        </div>
                    </td>
                    <td>Last Modified</td>
                    <td><input type="text" name="datelastmodified" size="14" class="textlabelsBoldForTextBox"  style="width:125px;" id="dat" value="<%=datelastmodified%>"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Acct Status</td>
                    <td>
                        <html:radio property="acctstatus" value="Active"/>Active
                        <html:radio property="acctstatus" value="Inactive"/>Inactive
                    </td>
                    <td>Control Acct<html:checkbox property="controlacct" value="ControlAcct" onclick="fun1()"/></td>
                    <td>Multi-Currency<html:checkbox property="multicurrency" value="Yes"/></td>
                    <td colspan="2"></td>
                </tr>
                <tr class="textlabelsBold" id="hiddentablesty1">
                    <td>SubLedger</td>
                    <td>
                        <html:select property="subledger" styleClass="dropdown_accounting" style="width:125px;">
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
                        </div>
                    </td>
                </tr>
            </table>
            <html:hidden property="index"/>
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
