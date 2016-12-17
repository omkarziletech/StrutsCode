<%-- 
    Document   : hsCode
    Created on : Jan 3, 2012, 2:58:19 PM
    Author     : Thamizh
--%>
<%@include file="init.jsp" %>
<script>
   $(document).ready(function(){
        $('#saveHs').click(function(){
            var error = true;
            $(".required").each(function(){
                if($(this).val().length == 0)
                {
                    sampleAlert('This field is required');
                    error = false;
                    $(this).css("border-color","red");
                    $(this).focus(); 
                    return false;
                }
            });
            if(error){
                    submitForm('addHsCode');
                }
        });
    });
</script>
<body>
    <cong:div style="width:99%; float:left;">
        <cong:form  id="hsCodeForm"  name="hsCodeForm" action="/hsCode.do" >
            <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
            <cong:table cellspacing="0" width="100%">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td>HsCode for File No: <span style="color: red;">${hsCodeForm.fileNumber}</span></cong:td>
                    <cong:td>
                        <cong:div styleClass="button-style1" style="float:right" onclick="addHsCode();"> Add </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2">
                        <cong:table id="HsCodeTable" width="35%" style="margin:5px 0; float:left; display:none;">
                            <cong:tr>
                                <cong:td>Enter HS Code</cong:td>
                                <cong:td>
                                    <cong:text name="hsCode" id="hsCode" styleClass="required"/>
                                    <cong:hidden name="refType" value="HsCode"/>
                                    <cong:hidden name="hsCodeFileNo"/>
                                    <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
                                    <cong:hidden name="id" id="id"/>
                                    <cong:hidden name="fileNumber" value="${fileNumber}"/>
                                    <cong:hidden name="methodName" id="methodName"/>
                                </cong:td>
                                <cong:tr>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>
                                        <input type="button" id="saveHs" value="Save" class="button-style1"/> 
                                        <input type="button"  value="cancel" class="button-style1" onclick="cancelNotes()"/>
                                    </cong:td>
                                </cong:tr>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:form>
    </cong:div>
    <br/>
    <table width="100%" cellpadding="0" cellspacing="0" class="tableHeadingNew">
        <tr><td>List of HsCode</td></tr>
    </table><cong:div style="width:99%; float:left; height:200px; overflow-y:scroll; border:1px solid #dcdcdc">
        <cong:table border="1" width="100%" style="margin:5px 0; float:left">
            <cong:tr styleClass="tableHeading2">
                <cong:td width="95%">Hs Code</cong:td>
                <cong:td width="15%">Action</cong:td>
            </cong:tr>
            <c:forEach var="lcl3pRefNo" items="${allLclHsCodeList}">
                <c:choose>
                    <c:when test="${zebra=='odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <cong:tr styleClass="${zebra}">
                    <cong:td>
                        ${lcl3pRefNo.reference}
                    </cong:td>
                    <cong:td>
                        <img src="${path}/images/edit.png" alt="edit" style="cursor: pointer" onclick="editHS('${lcl3pRefNo.id}','${lcl3pRefNo.reference}')"/> 
                        <img src="${path}/img/icons/trash.jpg"  alt="delete" height="12px" width="12px" onclick="closeNotes('${lcl3pRefNo.id}','closeNotes')" style="cursor:pointer"/>
                    </cong:td>
                </cong:tr>
            </c:forEach>
        </cong:table>
    </cong:div>
    <script type="text/javascript">
        function submitForm(methodName){
            $("#methodName").val(methodName);
            $("#hsCodeForm").submit();
        }
        function closeNotes(id,methodName){
            $("#methodName").val(methodName);
            $("#id").val(id);
            $("#hsCodeForm").submit();
        }
        /* script for alert */
        
        function sampleAlert(txt){
            $.prompt(txt);
        }
        function cancelNotes()
        {
            $("#hsCode").val('');  
            $("#hsCode").css("border-color","");
            document.getElementById("HsCodeTable").style.display="none";
        }
        function addHsCode(){
            $("#id").val('');
            showBlock('#HsCodeTable');
        }
        function showBlock(tar){
            $(tar).show(300);
        }
        function editHS(id,reference){
            $("#id").val(id);
            $("#hsCode").val(reference);
            showBlock('#HsCodeTable');
        }
    </script>
</body>
