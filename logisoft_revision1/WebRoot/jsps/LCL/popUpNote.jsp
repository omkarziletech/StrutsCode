<%-- 
    Document   : lclDocumCharge
    Created on : Nov 2, 2012, 5:09:44 PM
    Author     : mohana
--%>

<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<body style="background:#ffffff">
    <cong:table>
        <cong:tr>
            <cong:td style="font-size:15px">
                <span style="color:green;font-weight: bolder"><c:out value="Booking is saved successFully"/></span>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td style="font-size:15px">Booking#-
                <span style="color:red;font-weight: bolder" class="link" onclick="goToBooking('${path}','${lclBooking.fileNumberId}')" >${fileNo}</span>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td colspan="2">
                <input type="button" class="button-style1" value="Ok" onclick="closePopUp()"/>
            </cong:td>
        </cong:tr>
    </cong:table>
    <script type="text/javascript">
        function closePopUp(){
            parent.parent.$.colorbox.close();
        }
        function goToBooking(path,fileID){
           window.parent.parent.parent.changeLclUnitsSchedule(path, fileID, 'B', "Exports", "");
        }
    </script>
</body>

