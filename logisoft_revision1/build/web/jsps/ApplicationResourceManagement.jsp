<%-- 
    Document   : ApplicationResourceManagement
    Created on : Jun 3, 2010, 5:35:00 PM
    Author     : Vinay
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/logisoft/css/default/style.css" title="default" >
        <style type="text/css">
            input.buttonStyleNew {
                background-color:#8DB7D6;
                color: black;
                border-color: black;
                border-width: 1px; cursor : pointer;
                cursor: hand;;
                width: 65px;;
                font-size: 12px;
                cursor: pointer;
            }
        </style>
        <script language="text/javascript" src="${path}/js/common.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Application Resource Management</title>
    </head>
    <body class="whitebackgrnd">
        <%@ page language="java" import="java.util.Map,java.util.Set, java.util.Iterator;"%>
        <%
            Map m = (Map) request.getAttribute("properties");
            String path = "applicationResourceEdit.do";
        %>
        <font face="Arial" size="-1">
            <form action="<%=path%>" class="ApplicationResourceForm" method="post">
                <%                    if

                    (



                       "update".equals(request.getParameter("action"))) {
                %>
                <span>
                    <font color="GREEN"> Updated Successfully </font>
                </span>
                <%                                        }
                %>
                <table width="100%" border="0" bordercolor="#C4C5C4" cellspacing="1" cellpadding="0" class="tableBorderNew">
                    <tr>
                        <td colspan="4" bgcolor="C4C5C4"><h4>&nbsp;Application Resource Management</h4></td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <table cellspacing=1 cellpadding=1 border="0" align="center" width="100%">
                                <tr bgcolor='#8DB7D6'><h5>
                                    <td style="width: 30%">&nbsp;Field</td>
                                    <td style="width: 20%">&nbsp;Input</td>
                                    <td style="width: 30%">&nbsp;Field</td>
                                    <td style="width: 20%">&nbsp;Input</td></h5>
                    </tr>

                    <%
                                Set keys = m.keySet();
                                Iterator e = keys.iterator();
                                int j = 0;
                        for




                              (Object  key : keys) {
                                    j = (j + 1) % 2;
                            String color;
                            if (j == 1) {
                                color = "#FFFFFF";
                            } else {
                                color = "#D1DBE9";
                            }
                    %>
                    <tr bgcolor="<%=color%>">
                        <%
                                    int i;
                                    for (i = 0; i < 2; i++) {
                                        if (e.hasNext()) {
                        %>
                        <td style="width: 20%">&nbsp;<%=key%></td>
                        <td style="width: 30%"><% String s = (String) m.get(key);%>
                            <textarea name="<%=key%>" rows="3" cols="50" style="text-transform:uppercase" class="textlabelsBoldForTextBox" ><%=s%></textarea></td>
                            <%
                                                                                            } else {
                            %>
                        <td style="width: 30%">&nbsp;</td>
                        <td style="width: 20%">&nbsp;</td>
                        <%                                                                    }
                                    }
                        %>
                    </tr>
                    <%
                        }
                    %>
                    <tr>
                        <td colspan="4" align="center" >
                            <input class="buttonStyleNew" TYPE=SUBMIT VALUE="Submit" >
                            <input class="buttonStyleNew" TYPE=RESET VALUE="Reset">
                        </td>
                    </tr>
                </table>
                </td>
                </tr>
                </table>
                <input type="hidden" name="action" value="update"/>
            </form>
        </font>
    </body>
</html>
