<%-- 
    Document   : JcodeContact
    Created on : Oct 15, 2015, 1:22:30 PM
    Author     : PAL RAJ
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
        function saveJcode(methodName) {
            $("#methodName").val(methodName);
            $("#tradingPartnerForm").submit();
            parent.$.colorbox.close();
        }
        function toggleAppShipment(){
            $('#onlyWhenBookingContact').attr("checked",false)
            $('#applicableToAllShipments').attr("checked",true)
        }
        function toggleBkgShipment(){
            $('#onlyWhenBookingContact').attr("checked",true);
            $('#applicableToAllShipments').attr("checked",false);
        }
    </script>

    <body style="background:#ffffff">
        <html:form action="/JcodeContactDetailsAction" name="tradingPartnerForm" styleId="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm">
            <html:hidden styleId="methodName" property="methodName"/>
            <html:hidden styleId="index" property="index" value="${custContactId}"/>
            <cong:table style="width:100%; height:35%;">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td colspan="6">  Code J Contact Details</cong:td>  
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Applicable To All Shipments
                        <cong:checkbox name="applicableToAllShipments" id="applicableToAllShipments" onclick="toggleAppShipment()"
                                       value="${tradingPartnerForm.applicableToAllShipments}" container="NULL"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Only When Booking Contact
                        <cong:checkbox name="onlyWhenBookingContact" id="onlyWhenBookingContact" onclick="toggleBkgShipment()"
                                       value="${tradingPartnerForm.onlyWhenBookingContact}" container="NULL"/>
                    </cong:td>
                    <cong:td width="50%"/>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldTitileforlcl">Dr From Codes: </cong:td>
                    <cong:td align="center" styleClass="textlabelsBoldforlcl">OBKG to RCVD
                        <cong:checkbox name="obkgToAny" id="obkgToAny" value="${tradingPartnerForm.obkgToAny}" container="NULL"/>
                    </cong:td>
                    <cong:td align="center" styleClass="textBoldforlcl">RUNV to RCVD
                        <cong:checkbox name="runvToRcvd" id="runvToRcvd" value="${tradingPartnerForm.runvToRcvd}" container="NULL"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">All disposition changes
                        <cong:checkbox name="any" id="any" value="${tradingPartnerForm.any}" container="NULL"/>
                    </cong:td>
                </cong:tr>
            </cong:table>

            <cong:table style="width:100%;border: 1px solid #dcdcdc; height:10%;" cellpadding="0" cellspacing="0">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td  width="20%" align="right"></cong:td>
                    <cong:td  width="20%" styleClass="textlabelsBoldHeadingforlcl" >Posting</cong:td> 
                    <cong:td  width="20%" styleClass="textlabelsBoldHeadingforlcl" >Manifest</cong:td> 
                    <cong:td  width="20%" styleClass="textlabelsBoldHeadingforlcl">COB</cong:td> 
                    <cong:td  width="20%" styleClass="textlabelsBoldHeadingforlcl" >Changes</cong:td> 
                </cong:tr>
            </cong:table>

            <cong:table style="width:100%; height:50%;" cellpadding="0" cellspacing="0">
                <cong:tr>
                    <cong:td width="20%" styleClass="textlabelsBoldTitileforlcl">Non Neg Rated</cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="nonNegRatedPosting" id="nonNegRatedPosting" value="${tradingPartnerForm.nonNegRatedPosting}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="nonNegRatedManifest" id="nonNegRatedManifest" value="${tradingPartnerForm.nonNegRatedManifest}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="nonNegRatedCob" id="nonNegRatedCob" value="${tradingPartnerForm.nonNegRatedCob}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="nonNegRatedChanges" id="nonNegRatedChanges" value="${tradingPartnerForm.nonNegRatedChanges}" container="NULL"/>    
                    </cong:td>
                </cong:tr>

                <cong:tr>
                    <cong:td width="20%" styleClass="textlabelsBoldTitileforlcl">Non Neg Unrated</cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="nonNegUnratedPosting" id="nonNegUnratedPosting" value="${tradingPartnerForm.nonNegUnratedPosting}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="nonNegUnratedManifest" id="nonNegUnratedManifest" value="${tradingPartnerForm.nonNegUnratedManifest}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="nonNegUnratedCob" id="nonNegUnratedCob" value="${tradingPartnerForm.nonNegUnratedCob}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="nonNegUnratedChanges" id="nonNegUnratedChanges" value="${tradingPartnerForm.nonNegUnratedChanges}" container="NULL"/>    
                    </cong:td>
                </cong:tr>

                <cong:tr>
                    <cong:td width="20%" styleClass="textlabelsBoldTitileforlcl">Freight Invoice</cong:td>
                    <cong:td width="20%" align="center"> </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="freightInvoiceManifest" id="freightInvoiceManifest" value="${tradingPartnerForm.freightInvoiceManifest}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="freightInvoiceCob" id="freightInvoiceCob" value="${tradingPartnerForm.freightInvoiceCob}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center"> </cong:td>
                </cong:tr>

                <cong:tr>
                    <cong:td width="20%" styleClass="textlabelsBoldTitileforlcl">Confirm On Board</cong:td>
                    <cong:td width="20%" align="center"> </cong:td>
                    <cong:td width="20%" align="center"> </cong:td>
                    <cong:td width="20%" align="center">
                        <cong:checkbox name="confirmOnBoardCob" id="confirmOnBoardCob" value="${tradingPartnerForm.confirmOnBoardCob}" container="NULL"/>    
                    </cong:td>
                    <cong:td width="20%" align="center"> </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td width="20%" align="center"></cong:td>
                    <cong:td width="20%" align="center"></cong:td>
                    <cong:td width="20%" align="center">
                        <html:button property="save" value="Save" styleClass="buttonStyleNew" onclick="saveJcode('saveJcodeContact');"/>   
                    </cong:td> 
                </cong:tr>    
            </cong:table>
        </html:form>
    </body>
</html>