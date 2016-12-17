<%@ page language="java" import="java.util.*,com.gp.cvst.logisoft.struts.form.EmailForm" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%
            String path = request.getContextPath();
            String toAddress = request.getParameter("mail");
            if (toAddress == null) {
                toAddress = "";
            }
            EmailForm emailForm = new EmailForm();
            String fileNo = "";
            if (request.getAttribute("emailForm") != null) {
                emailForm = (EmailForm) request.getAttribute("emailForm");
                fileNo = emailForm.getFileNo();
            }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>


        <link href="<%=path%>/css/style.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
        <link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" />
        <link href="<%=path%>/css/style.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
        <script language="JavaScript" type="text/javascript" src="<%=path%>/js/cbrte/html2xhtml.js"></script>
        <script language="JavaScript" type="text/javascript" src="<%=path%>/js/cbrte/richtext_compressed.js"></script>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
    </head>

    <body class="whitebackgrnd">

        <form name="mailForm" method="post" enctype="multipart/form-data" action="<%=path%>/sendEmail.do" type="com.gp.cvst.logisoft.struts.form.EmailForm">

            <script type="text/javascript">
 
                function cancelDetails(){
                    parent.parent.GB_hide();
                }
                function sendEmail() {
                    updateRTEs();
                    document.mailForm.body.value = htmlDecode(document.mailForm.rte1.value);
                    if(document.getElementById('toAddress').value == ''){
                        alert('Please Enter To Address');
                        return false;
                    }else if(document.getElementById('subject').value == ''){
                        alert('Please Enter subject');
                        return false;
                    }else if(document.mailForm.body.value == ''){
                        alert('Please Enter Message');
                        return false;
                    }
                    document.mailForm.submit();
                    parent.parent.GB_hide();
                }
                initRTE("<%=path%>/js/cbrte/images/", "<%=path%>/js/cbrte/", "", true);
            </script>
            <table width="100%" class="tableBorderNew" border="0">
                <font color="red" size="4" ><b><%=fileNo%></b></font>
                <tr class="tableHeadingNew">Compose Mail<br></tr>
                <tr>
                    <td class="textlabels" width="10%"> TO </td>
                    <td><input type="text" name="toAddress" id="toAddress" size="79"/></td>
                </tr>
                <tr>
                    <td class="textlabels"> CC </td>
                    <td><input type="text" name="ccAddress" id="ccAddress" size="79"/></td>
                </tr>
                <tr>
                    <td class="textlabels"> BCC </td>
                    <td><input type="text" name="bccAddress" id="bccAddress" size="79"/></td>
                </tr>
                <tr>
                    <td class="textlabels">Subject</td>
                    <td><input type="text" name="subject" id="subject" size="79"/></td>
                </tr>
                <tr>
                    <td class="textlabels">Attach</td>
                    <td><input name="file" type="file" id="file" size="66"></td>
                </tr>
                <tr>
                    <td class="textlabels" valign="top">Message</td>
                    <td>
                        <script language="JavaScript" type="text/javascript">
                            <!--

                            var rte1 = new richTextEditor('rte1');
                            rte1.toggleSrc = false;
                            rte1.build();
                            //-->
                        </script>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center"><input type="button" value="Send" onClick="sendEmail()"/>
                        <input type="button" value="Cancel" onclick="cancelDetails()"/></td>
                </tr>
            </table>
            <input type="hidden" name="buttonValue" value="newMail"/>
            <input type="hidden" name="index" />
            <input type="hidden" name="action" value="quoteEmail" />
            <input type="hidden" name="body" id="body"/>
        </form>
        <%!
            public static String rteSafe(String strText) {
                //returns safe code for preloading in the RTE
                String tmpString = strText;

                //convert all types of single quotes
                tmpString = tmpString.replace((char) 145, (char) 39);
                tmpString = tmpString.replace((char) 146, (char) 39);
                tmpString = tmpString.replace("'", "&#39;");

                //convert all types of double quotes
                tmpString = tmpString.replace((char) 147, (char) 34);
                tmpString = tmpString.replace((char) 148, (char) 34);
                //tmpString = tmpString.replace("\"", "\"");

                //replace carriage returns & line feeds
                tmpString = tmpString.replace((char) 10, (char) 32);
                tmpString = tmpString.replace((char) 13, (char) 32);

                return tmpString;
            }
        %>
        <script language="javascript" >
            document.getElementById("toAddress").value='<%=toAddress%>';
        </script>

    </body>

    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>