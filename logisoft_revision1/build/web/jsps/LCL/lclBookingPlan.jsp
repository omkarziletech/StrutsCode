<%@include file="init.jsp" %>
<script>
    $(document).ready(function(){
        $('#savePlan').click(function(){
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
            }); if(error){
                submitForm('add');
            }
        });
    });
</script>
<body style="background:#ffffff">
  <cong:div style="width:99%; float:left;">
    <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
     Booking Plan List:
    </cong:div>
    <cong:form name="lclBookingPlanForm" id="lclBookingPlanForm" action="/lclBookingPlan.do">
      <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
      <cong:hidden name="fromId" value="${fromId}"/>
      <cong:hidden name="toId" value="${toId}"/>
      <cong:hidden name="fileNumber" value="${fileNumber}"/>
      <cong:table style="width:100%">
        <cong:tr>
          <cong:td>
            <cong:table width="100%" style="margin:5px 0; float:left">
              <cong:tr>
                <cong:td width="33%">
                  <cong:table>
                    <cong:tr>
                      <cong:td>From ETD</cong:td>
                      <cong:td>
                        <cong:calendarNew name="fromEtd" id="fromEtd" styleClass="text mandatory required"/>
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>From ATD</cong:td>
                      <cong:td>
                        <cong:calendarNew name="fromAtd" id="fromAtd" styleClass="text mandatory required"/>
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>To ETA</cong:td>
                      <cong:td>
                        <cong:calendarNew name="toEta" id="toEta" styleClass="text mandatory required"/>
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>To ATA</cong:td>
                      <cong:td>
                        <cong:calendarNew name="toAta" id="toAta" styleClass="text mandatory required"/>
                      </cong:td>
                    </cong:tr>
                  </cong:table>
                </cong:td>

                <cong:td width="33%">
                  <cong:table>
                    <cong:tr>
                      <cong:td>SP Account</cong:td>
                      <cong:td>
                        <cong:text name="spAcct" id="spAcct" styleClass="text" container="NULL"/>
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>SP Contact</cong:td>
                      <cong:td>
                        <cong:text name="spContact" id="spContact" styleClass="text" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/>
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>SP Vessel</cong:td>
                      <cong:td>
                        <cong:text name="spVessel" id="spVessel" styleClass="text" container="NULL"/>
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>SP Ref</cong:td>
                      <cong:td>
                        <cong:text name="spRef" id="spRef" styleClass="text" container="NULL"/>
                      </cong:td>
                    </cong:tr>
                  </cong:table>
                </cong:td>
                <cong:td width="25%">
                  <cong:table>
                    <cong:tr>
                      <cong:td>Seg No</cong:td>
                      <cong:td>
                        <cong:text name="segNo" id="segNo" styleClass="text" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/>
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>Schedule </cong:td>
                      <cong:td>
                       <cong:text name="schedule" id="schedule" styleClass="text" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/>
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>Trans Mode</cong:td>
                      <cong:td>
                        <cong:radio value="A" name="transMode" container="NULL"/>A
                        <cong:radio value="R" name="transMode" container="NULL"/>R
                        <cong:radio value="T" name="transMode" container="NULL"/>T
                      </cong:td>
                    </cong:tr>
                    <cong:tr>
                      <cong:td>Relay Override </cong:td>
                      <cong:td>
                        <cong:radio value="Y" name="relayOverride" container="NULL"/> Yes
                        <cong:radio value="N" name="relayOverride" container="NULL"/> No
                      </cong:td>
                    </cong:tr>
                  </cong:table>
                </cong:td>
              </cong:tr>
              <cong:tr>
                <cong:td>
                  <input type="button"  value="ADD" class="button-style1" id="savePlan"/>  
                </cong:td>
              </cong:tr>
            </cong:table>
          </cong:td>
        </cong:tr>

        <cong:tr>
          <cong:td>
            <cong:table width="100%" border="1" style="border-collapse: collapse; border: 1px solid #dcdcdc">

              <cong:tr styleClass="tableHeading2">
                <cong:td>Seg No</cong:td>
                <cong:td>From</cong:td>
                <cong:td>To</cong:td>
                <cong:td>Schedule</cong:td>
                <cong:td>Relay</cong:td>
              </cong:tr>
              <c:forEach items="${bookingPlanList}" var="bookingPlan">
                <c:choose>
                  <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                  </c:when>
                  <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                  </c:otherwise>
                </c:choose>
                <cong:tr styleClass="${zebra}">
                  <cong:td>${bookingPlan.segNo}</cong:td>
                  <cong:td>${bookingPlan.fromId.unLocationName}</cong:td>
                  <cong:td>${bookingPlan.toId.unLocationName}</cong:td>
                  <cong:td></cong:td>
                  <cong:td>
                      <c:choose>
                          <c:when test=" ${bookingPlan.relayOverride==true}">
                              <c:out value="Y"/>
                          </c:when>
                          <c:otherwise>
                              <c:out value="N"/>
                          </c:otherwise>
                      </c:choose>

                  </cong:td>
                </cong:tr>
              </c:forEach>
            </cong:table>
          </cong:td>
        </cong:tr>
      </cong:table>
      <cong:hidden name="methodName" id="methodName"/>
    </cong:form>
  </cong:div>
  <script type="text/javascript">
    function submitForm(methodName){
      $("#methodName").val(methodName) ;
      $("#lclInbondForm").submit();
    }
    function sampleAlert(txt){
      $.prompt(txt);
    } 
    function checkForNumberAndDecimal(obj){ 
            var result;  
            if(!/^\d*(\.\d{0,6})?$/.test(obj.value)){ 
                obj.value=""; 
                sampleAlert("This field should be Numeric");
                             
            }
    }
  </script>
</body>
