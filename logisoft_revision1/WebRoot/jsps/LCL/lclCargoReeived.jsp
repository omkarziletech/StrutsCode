<%-- 
    Document   : lclCargoReeived
    Created on : Feb 28, 2012, 6:38:19 PM
    Author     : lakshh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclCargoReceived.js"/>
<style type="text/css">
    .td-prompt{
        margin-left: 10px;
    }
</style>
<body>
    <script type="text/javascript">
    </script>
    <cong:div>
        <cong:table>
            <cong:tr>
                <cong:td align="center">
                    <b style="color: red;" class="headerlabel">Would you like to receive this cargo?</b>
                </cong:td>
            </cong:tr>
        </cong:table>
        <br><br><br><br>
        <cong:table align="center">
            <cong:tr>
                <cong:td>
                    <div class="button-style1 td-prompt" style="margin-left: 25%" onclick="clickNo();">
                        No
                    </div>
                    <div class="button-style1 td-prompt" id="yUn" onclick="yesWithUnverified();">
                        Yes-Unverified
                    </div>
                    <div class="button-style1 td-prompt" onclick="yesWithVerified();">
                        Yes-Verified
                    </div>
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:div>
</body>