<%-- 
    Document   : JcodeContact
    Created on : Oct 15, 2015, 1:22:30 PM
    Author     : Stefy
--%>
<html>
    <%@include file="/jsps/LCL/init.jsp" %>
    <%@include file="/jsps/preloader.jsp" %>
    <%@include file="/taglib.jsp" %>
    <%@include file="/jsps/includes/baseResources.jsp" %>
    <%@include file="/jsps/includes/resources.jsp" %>
    <%@include file="/jsps/includes/jspVariables.jsp" %>
    <head>
        <%@page contentType="text/html" pageEncoding="UTF-8"%>
        <title>JSP Page</title>
    </head>
    <script type="text/javascript">
        function saveKcode(methodName) {
            $("#methodName").val(methodName);
            $("#tradingPartnerForm").submit();
            parent.$.colorbox.close();
        }
           function toggleAppShipment(){
            $('#onlyWhenBookingContactCodeK').attr("checked",false)
            $('#applicableToAllShipmentsCodeK').attr("checked",true)
        }
        function toggleBkgShipment(){
            $('#onlyWhenBookingContactCodeK').attr("checked",true);
            $('#applicableToAllShipmentsCodeK').attr("checked",false);
        }
    </script>

    <body style="background:#ffffff">
        <html:form action="/KcodeContactDetailsAction" name="tradingPartnerForm" styleId="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm">
            <html:hidden styleId="methodName" property="methodName"/>
            <html:hidden styleId="index" property="index" value="${custContactId}"/>
            <cong:table style="width:100%; height:35%;">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td colspan="4">  Code K Contact Details</cong:td>  
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="4"> &nbsp;</cong:td>  
                </cong:tr>
               <cong:tr>
                    <cong:td  align="left" styleClass="textBoldforlcl">Applicable To All Shipments
                        <cong:checkbox name="applicableToAllShipmentsCodeK" id="applicableToAllShipmentsCodeK" onclick="toggleAppShipment()"
                                       value="${tradingPartnerForm.applicableToAllShipmentsCodeK}" container="NULL"/>
                    </cong:td>
                    <cong:td  align="left" styleClass="textBoldforlcl">Only When Booking Contact
                        <cong:checkbox name="onlyWhenBookingContactCodeK" id="onlyWhenBookingContactCodeK" onclick="toggleBkgShipment()"
                                       value="${tradingPartnerForm.onlyWhenBookingContactCodeK}" container="NULL"/>
                    </cong:td>
                    <cong:td width="25%"/>
                    <cong:td width="25%"/>
                </cong:tr>
                     <cong:tr>
                    <cong:td colspan="4"> &nbsp;</cong:td>  
                </cong:tr>
                <cong:tr>
                    <cong:td align="left" styleClass="textBoldforlcl">LCL Exports
                        <cong:checkbox name="lclExports" id="lclExports" value="${tradingPartnerForm.lclExports}" container="NULL"/>
                    </cong:td>
                    <cong:td align="left" styleClass="textBoldforlcl">LCL Imports
                        <cong:checkbox name="lclImports" id="lclImports" value="${tradingPartnerForm.lclImports}" container="NULL"/>
                    </cong:td>
                    <cong:td align="left" styleClass="textBoldforlcl">FCL Exports
                        <cong:checkbox name="fclExports" id="fclExports" value="${tradingPartnerForm.fclExports}" container="NULL"/>
                    </cong:td>
                    <cong:td align="left" styleClass="textBoldforlcl">FCL Imports
                        <cong:checkbox name="fclImports" id="fclImports" value="${tradingPartnerForm.fclImports}" container="NULL"/>
                    </cong:td>
                </cong:tr>
                 <cong:tr>
                    <cong:td colspan="4"> &nbsp;</cong:td>  
                </cong:tr>
                     <cong:tr>
                    <cong:td colspan="4"> &nbsp;</cong:td>  
                </cong:tr>
                 <cong:tr>
                    <cong:td colspan="4" align="center">
                        <html:button property="save" value="Save" styleClass="buttonStyleNew" onclick="saveKcode('saveKcodeContact');"/>   
                    </cong:td> 
                </cong:tr>   
            </cong:table>
        </html:form>
    </body>
</html>