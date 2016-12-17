<%@ page import="com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.domain.Role"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<%@taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
<%

            String _path = request.getContextPath();
            User user = null;
            if (session.getAttribute("loginuser") != null) {
                user = (User) session.getAttribute("loginuser");
                String userdisplay = user.getFirstName() + " " + user.getLastName();
                userdisplay = userdisplay.toUpperCase();
                RefTerminal terminal = user.getTerminal();
                String terminaldisplay = "";
                if (terminal != null) {
                    terminaldisplay = terminal.getTrmnum() + " " + terminal.getTerminalLocation();
                }
                Role role = user.getRole();
                String roledisplay = "";
                if (role != null) {
                    roledisplay = role.getRoleDesc();
                }
%>
<html> 
    <head>
        <title> TopFrame</title>
        <%@include file="includes/baseResources.jsp" %>
        <style>
            .style4{
                color: white;
            }
            .style3{
               vertical-align:middle;
               color:white;
            }
        </style>
    </head>
    <body class="whitebackgrnd" >
        <html:form action="/topFrame" scope="request">
            <input type='hidden' name='theme'>
            <table  width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#2B2C6C"  >
                <tr>
                    <td>
                        <img src="<%=_path%>/${dao:getProperty('application.image.logo')}" border="0" style="background-color:#E6F2FF"/>
                    </td>
                    <td valign="top" align="center">
                        <div class="style4" style="vertical-align:top" align="left">
                            <b>Hi,</b><%=userdisplay%> (<%=roledisplay%>)
                            <img src="<%=_path%>/img/icons/contextelement.gif" border="0">
                            <b>Version:</b>${dao:getProperty('application.version')}
                        </div>
                        <div class="style4" style="vertical-align:top" align="left">
                            <b>Terminal: </b><%=terminaldisplay%>
                            <img src="<%=_path%>/img/icons/jsconsole.gif" border="0">
                        </div>
                        <div class="style4" style="vertical-align" align="left">
                            <b>Email: </b><c:out value="${loginuser.email}"/>
                        </div>
                    </td>
                    <td valign="top" align="right" >
                        <table border="0" width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td valign="top" align="left">
                                    <img src="<%=_path%>/img/icons/home.gif" border="0">
                                    <a href="<%=_path%>" class="style3">Home</a>
                                </td>
                                <td valign="top">
                                    <img src="<%=_path%>/img/icons/dashboard.png" border="0">
                                    <a href="#" onclick="GB_show('Dashboard','<%=_path%>/dashboard.do',width='700',height='1100')" class="style3">Dashboard</a>
                                </td>
                                <td valign="top">
                                    <img src="<%=_path%>/img/icons/myeclipse_run_active1.gif" border="0">
                                    <a href="<%=_path%>/jsps/logout.jsp" class="style3">Logout</a>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4" valign="bottom" align="right" style="padding-right:5px;">
                                    <span style="text-align: justify;color: white;font-size: medium"><b>Powered By</b></span>
                                    <img src="<%=_path%>/img/icons/poweredBy.gif"  border="0"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue"/>
        </html:form>
    </body>
</html>
<%
            }%>

