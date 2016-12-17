<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<cong:form  id="lclOutsourceForm"  name="lclOutsourceForm" action="lclOutsource.do" >
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:hidden name="methodName" id="methodName"/>
        <cong:hidden name="fileNumber" id="fileNumber"/>
        <cong:hidden name="fileId" id="fileId"/>
        <cong:hidden name="emailId" id="emailId"/>
        <cong:hidden name="unitId" id="unitId"/>
        <cong:hidden name="ssHeaderId" id="ssHeaderId"/>
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="4">
                <cong:div styleClass="floatLeft">
                    Outsource for
                    <c:if test="${unitSourceEmail eq true}">
                        <%-- outsource is unit level */--%>
                        Unit #
                    </c:if>
                    : <span style="color: red">${fileNumber}</span>
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table width="100%" style="margin:5px 0; float:left" border="0">
        <cong:tr> <cong:td  colspan="2"></cong:td>  </cong:tr>
        <cong:tr> <cong:td  colspan="2"></cong:td>  </cong:tr>
        <cong:tr> <cong:td  colspan="2"></cong:td>  </cong:tr>
        <cong:tr>
            <cong:td styleClass="td bold">To</cong:td>
            <cong:td><cong:text name="mailTo" id="mailTo" value="${lclOutsourceForm.mailTo}" readOnly="true"
                                styleClass="text textlabelsBoldForTextBoxDisabledLook" container="NULL" style="width: 200px"/></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="td bold" width="10%">Message</cong:td>
            <c:choose>
                <c:when test="${flag}">
                    <cong:td>
                        <cong:textarea id="message"  name="message" readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:84%;text-transform:uppercase"  cols="10" rows="10" container="NULL" value="${lclOutsourceForm.message}"/>
                    </cong:td>
                </c:when>
                <c:otherwise>
                    <cong:td>
                        <cong:textarea id="message"  name="message"  styleClass="text" style="width:84%;text-transform:uppercase"  cols="10" rows="10" container="NULL" value="${lclOutsourceForm.message}"/>
                    </cong:td>
                </c:otherwise>
            </c:choose>
        </cong:tr>
    </cong:table>
    <cong:table border="0">
        <cong:tr>
            <c:choose>
                <c:when test="${flag}">
                </c:when>
                <c:otherwise>
                    <cong:td><input type="button" value="Submit" class="button-style1" onclick="submitFormEmail('sendEmailDetails')"/></cong:td>
                    <cong:td><input type="button" value="Cancel" class="button-style1" onclick="closeOutsourcepopUp()"/></cong:td>
                </c:otherwise>
            </c:choose>
            <cong:td width="85%"></cong:td>
        </cong:tr></cong:table>

</cong:form>
<script type="text/javascript">
    $(document).ready(function(){
        changeOutsource('${remarks}');
    });
    function submitFormEmail(methodName){
        $("#methodName").val(methodName);
        $("#lclOutsourceForm").submit();
        var unit_id = $("#unitId").val();
        if(unit_id === "") {
            parent.$("#lcloutsourceButton").addClass('green-background');
        } else {
            parent.$("#outsource"+unit_id).addClass('green-background');
        }
        parent.$.fn.colorbox.close();
    }
    function closeOutsourcepopUp(){
        parent.$.colorbox.close();
    }
</script>
