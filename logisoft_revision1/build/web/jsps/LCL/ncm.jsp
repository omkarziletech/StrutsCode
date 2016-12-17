<%-- 
    Document   : hsCode
    Created on : Jan 3, 2012, 2:58:19 PM
    Author     : Thamizh
--%>
<%@include file="init.jsp" %>
<script>
    $(document).ready(function(){
       $('#savSubmit').click(function(){
        $(".required").each(function(){
            if($(this).val().length == 0)
            {
               sampleAlert('This field is required');
               $(this).css("border-color","red");
               $(this).focus(); 
               return false;
            }else{
                 submitForm('addNcmNumber');
            }
        });
    });
    });
</script>
<body><cong:div style="width:99%; float:left;">
    <cong:form  id="ncmNumberForm"  name="ncmNumberForm" action="/ncmNumber.do" >
      <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
      <cong:table cellpadding="0" cellspacing="0" width="100%">
        <cong:tr styleClass="tableHeadingNew">
          <cong:td>NcmNumber for File No: <span style="color: red;">${ncmNumberForm.fileNumber}</span></cong:td>
          <cong:td>
            <cong:div styleClass="button-style1" style="float:right" onclick="addNcmNumber();"> Add </cong:div>
          </cong:td>
        </cong:tr>
        <cong:tr>
          <cong:td colspan="2">
            <cong:table id="NcmCodeTable" width="35%" style="margin:5px 0; float:left; display:none;">
              <cong:td>NCM Number</cong:td>
              <cong:td>
                <cong:text  name="ncmNumaber" id="ncmNumaber" styleClass="required" />
                <cong:hidden name="refType" value="NCM Number"/>
                <cong:hidden name="ncmCodeFileNo"/>
                <cong:hidden name="id" id="id"/>
                <cong:hidden name="fileNumber" value="${fileNumber}"/>
              </cong:td>
              <cong:tr>
                <cong:td>&nbsp;</cong:td>
                <cong:td>
                  <input type="button" id="savSubmit"  value="Save" class="button-style1"/>  
                  <input type="button"  value="Cancel" class="button-style1" onclick="cancelNotes()"/>  
                </cong:td>
              </cong:tr>
              <cong:hidden name="methodName" id="methodName"/>
            </cong:table>
          </cong:td>
        </cong:tr>
      </cong:table>
    </cong:form> 
  </cong:div>
  <br/>
  <table width="100%" cellpadding="0" cellspacing="0" class="tableHeadingNew">
    <tr><td>List of Notes</td></tr>
  </table><cong:div style="width:99%; float:left; height:200px; overflow-y:scroll; border:1px solid #dcdcdc">
    <cong:table border="1" width="100%" style="margin:5px 0; float:left">
      <cong:tr styleClass="tableHeading2">
        <cong:td width="95%">Ncm Number</cong:td>
        <cong:td width="15%">Action</cong:td>
      </cong:tr>
      <c:forEach var="lcl3pRefNo" items="${allLclNcmNumberList}">
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
              <img src="${path}/images/edit.png" alt="edit" style="cursor: pointer" onclick="editNcmNumber('${lcl3pRefNo.id}','${lcl3pRefNo.reference}')"/> 
            <img src="${path}/img/icons/trash.jpg" height="12px" width="12px" onclick="closeNotes('${lcl3pRefNo.id}','closeNotes')" style="cursor:pointer"/>
          </cong:td>
        </cong:tr>
      </c:forEach>
    </cong:table>
  </cong:div>
  <script type="text/javascript">
    function submitForm(methodName){
      $("#methodName").val(methodName);
      $("#ncmNumberForm").submit();
    }
    function cancelNotes()
    {
       $("#ncmNumaber").val('');  
       $("#ncmNumaber").css("border-color","");  
      document.getElementById("NcmCodeTable").style.display="none";
    }
    function showBlock(tar){
      $(tar).show(300);
    }
    function closeNotes(id,methodName){
      $("#methodName").val(methodName);
      $("#id").val(id);
      $("#ncmNumberForm").submit();
    }
    function validateNcmForm(){
      var result=true;
      var ncmNumber=$('#ncmNumaber').val();
      if(ncmNumber==""){
        sampleAlert("NCM number is required");
        $("#ncmNumaber").css("border-color","red");
        $("#warning").parent.show();
        result=false;
      }
      return result;
    }
    function sampleAlert(txt){
      $.prompt(txt);
    }
    function editNcmNumber(id,reference){
        $("#id").val(id);
        $("#ncmNumaber").val(reference);
        showBlock('#NcmCodeTable');
    }
     function addNcmNumber(){
        $("#id").val('');
        showBlock('#NcmCodeTable');
    }
  </script>
</body>
