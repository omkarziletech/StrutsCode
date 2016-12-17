<%@include file="../init.jsp" %>
<%@include file="/taglib.jsp"%>
<%@include file="../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:form  action="/rateQuote" name="rateQuoteForm" id="rateQuoteForm">
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="4">
                <cong:div styleClass="floatLeft">Add Reference Number&nbsp; &nbsp;
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table align="center" style="font-size: 14px" width="80%">
        <cong:tr><cong:td>
                <cong:table  border="0" width="100%">
                    <cong:tr>
                        <cong:td>
                            <cong:text name="bookingNumber" id="bookingNumber"/>
                            <cong:hidden name="carrierName" id="carrierName"/>
                            <cong:hidden name="quoteId" id="quoteId"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td align="center">
                            <input type="button" class="button-style3" value="Submit" onclick="changeBooking()"/>
                            <input type="button" class="button-style3" value="Cancel" onclick="closePopupBox()"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td></cong:tr>

    </cong:table>
</cong:form>
<script>
    function closePopupBox(){
        parent.$.fn.colorbox.close();
    }
    function changeBooking(){
        var bookingNo = document.getElementById("bookingNumber").value;
        var carrierName = document.getElementById("carrierName").value;
        parent.bookQuote(bookingNo,carrierName);
    }
    $(document).ready(function() {
        $("#rateQuoteForm").submit(function() {
            window.parent.showPreloading();
        });
        window.parent.closePreloading();
    });
</script>
