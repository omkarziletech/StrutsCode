<%@include file="/jsps/includes/baseResources.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="com.gp.cong.common.Application" %>
<meta http-equiv="refresh" content="4;URL=<%=Application.JPS_PATH%>/login.jsp"/>
<html>
    <head>
        <title>Page Not Found</title>
        <link rel="stylesheet" type="text/css" href='<%=Application.CSS_PATH%>/main.css' title="Aqua" />
        <link rel="stylesheet" type="text/css" href='<%=Application.CSS_PATH%>/default/style.css' title="Aqua" />
    </head>
    <body>
        <html:form   action="/loginPage"  scope="request">
            <table  width="100%" border="0" >
                <tr>
                    <td align="left"><img src='<%=request.getContextPath()%><%=com.gp.cong.struts.LoadLogisoftProperties.getProperty("application.image.logo")%>' alt="Home Page" border="0"/></td>
                </tr>
                <tr>
                    <td align="center" >
                        <table width="350" border="0" align="center" cellpadding="5" cellspacing="0" class="tableBorderNew" style="margin-top:100px">
                            <tr class="tableHeadingNew" height="90%">
                                <td>
                                    <p>
                                        <font color=orange size=4>
                                            The resource you are trying to access can not be found.</font><br/>

                                        <br/>This ofen happens when you trying to find  invalid resources on this site.<br/>
                                        Any further help can be found in the help page on this site. <br/>
                                        You will be redirected to the main page in 4 seconds.
                                        <br/><br/>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>


