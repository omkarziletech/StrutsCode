<table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr class="tableHeadingNew"><td colspan="8">Trade Route</td></tr>
    <tr class="textlabelsBold">
        <%-- if booking called from import fields are editabled with proper dojo, when booking called from export its read only and disabled --%>
        <%@include file="../fragment/fclBlDesOrgForImxport.jsp"  %>
    </tr>
<tr class="textlabelsBold">
    <td  valign="top" align="left" colspan="8">
        <table width="100%" height="100%" border="0" class="tableBorderNew"
               style="border-top-width: 1px;border-top-color:grey;border-left-width: 0px;
               border-bottom-width: 0px;border-right-width: 0px;">
            <tr class="textlabelsBold">
                <td valign="top" width="8%">Port Remarks:</td>
                <td>

                    <%
                                if (request.getAttribute("fclBl") != null) {
                                    FclBl fclBl = (FclBl) request.getAttribute("fclBl");
                                    String remarks = fclBl.getDestRemarks() != null ? fclBl.getDestRemarks() : "";
                                    String[] remarksDup = remarks.split("\\n");
                                    out.println("<ul style='margin-left: 0px;'>");
                                    for (int i = 0; i < remarksDup.length; i++) {
                                        out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + remarksDup[i] + "</li>");
                                    }
                                    out.println("</ul>");
                                }
                    %>
                </td>
            </tr>
            <tr class="textlabelsBold">
                <td>&nbsp;</td>
                <td valign="top" align="left"><%
                            if (request.getAttribute("fclBl") != null) {
                                FclBl fclBl = (FclBl) request.getAttribute("fclBl");
                                String rateRemarks = fclBl.getRatesRemarks() != null ? fclBl.getRatesRemarks() : "";
                                String[] remarksDup = rateRemarks.split("\\n");
                                out.println("<ul style='margin-left: 0px;'>");
                                for (int i = 0; i < remarksDup.length; i++) {
                                    out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + remarksDup[i] + "</li>");
                                }
                                out.println("</ul>");
                            }
                    %>
                </td>
        </table>
    </td>
</tr>
</table>