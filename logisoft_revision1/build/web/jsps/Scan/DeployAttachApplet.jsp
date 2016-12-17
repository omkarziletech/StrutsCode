<%-- 
    Document   : DeployAttachApplet
    Modified on : Aug 05, 2014, 4:37:02 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery.js"></script>
        <title>Drag and Drop Applet</title>
        <script type="text/javascript">
            function closeAppletWindow(message) {
                document.getElementById("error").innerHTML = "";
                document.getElementById("applet-div").style.display = "none";
                document.getElementById("message-div").style.display = "block";
                document.getElementById("message").innerHTML = message;
            }

            function verifyDropedFile() {
                document.getElementById("error").innerHTML = "Only one email allowed";
            }
        </script>
    </head>
    <body>
        <div>
            <div id="applet-div">
                <applet code="com.scan.Upload.class"
                        codebase="${pageContext.request.contextPath}/jsps/Scan/" archive="guruscan.jar,jacob.jar" width="350" height="300">
                    <param name="FileName" value="${sessionScope.FileName}">
                    <param name="FileLocation" value="${sessionScope.FileLocation}">
                    <param name="documnetId" value="${sessionScope.documentStoreLog.documentID}">
                    <param name="documentName" value="${sessionScope.documentStoreLog.documentName}">
                    <param name="documentType" value="${sessionScope.documentStoreLog.documentType}">
                    <param name="screenName" value="${sessionScope.documentStoreLog.screenName}">
                    <param name="comments" value="${sessionScope.documentStoreLog.comments}">
                    <param name="status" value="${sessionScope.documentStoreLog.status}">
                    Your browser does not support Java applet.
                </applet>
                <span id="error" style="color: red;font-weight: bolder;font-family: Arial,Helvetica,sans-serif;font-size: 22px;"></span>
            </div>
            <div style="text-align: center; padding-top: 20%;display: none;" id="message-div">
                <span id="message" style="color: green;font-weight: bolder;font-family: Arial,Helvetica,sans-serif;font-size: 22px;"></span>
            </div>
        </div>
    </body>
</html>
