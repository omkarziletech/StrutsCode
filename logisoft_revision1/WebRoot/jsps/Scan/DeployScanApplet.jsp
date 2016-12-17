<%-- 
    Document   : DeployScanApplet
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
            function alertWindow(message) {
                if (message === "CONFIG_ERROR") {
                    document.getElementById("error").innerHTML =  "Please make sure the Scanner is swithed ON and Configured Properly";
                } else if (message === "UPLOAD_ERROR") {
                    document.getElementById("error").innerHTML =  "Error while uploding the file, Please try Again";
                } else if (message === "SCAN_ERROR") {
                    document.getElementById("error").innerHTML =  "Error occured while scan, Please try Again";
                } else {
                    document.getElementById("message").innerHTML = "Your Document has been Scanned Successfully";
                }
                document.getElementById("applet-div").style.display = "none";
                document.getElementById("message-div").style.display = "block";
            }
        </script>
    </head>
    <body>
        <div id="applet-div">
            <applet code="com.scan.GuruScan.class"
                    codebase="${pageContext.request.contextPath}/jsps/Scan/" archive="guruscan.jar, JTwain.jar, iText2.1.4.jar" width="250" height="300">
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
        </div>
        <div style="text-align: center; padding-top: 15%;display: none;" id="message-div">
            <span id="error" style="color: red;font-weight: bolder;font-family: Arial,Helvetica,sans-serif;font-size: 22px;"></span>
            <span id="message" style="color: green;font-weight: bolder;font-family: Arial,Helvetica,sans-serif;font-size: 22px;"></span>
        </div>
    </body>
</html>