<%@include file="../includes/jspVariables.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
  <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
    <head>
      <base href="${basePath}"/>
    <title>Charge/Cost Codes</title>
    <%@include file="../includes/baseResources.jsp" %>
    <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="${path}/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    <c:set var="accessMode" value="2"/>
    <c:if test="${param.accessMode==0}">
      <c:set var="accessMode" value="0"/>
      <style type="text/css">
        .display-none{
          display:none;
        }
      </style>
    </c:if>
  </head>
  <body class="whitebackgrnd">
    <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
    <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
    <c:if test="${not empty glMappingForm.message}">
      <c:choose>
        <c:when test="${fn:contains(glMappingForm.message,'failed')}">
          <div style="color: red;font-weight: bolder;">
            <c:out value="${glMappingForm.message}"/>
          </div>
        </c:when>
        <c:otherwise>
          <div style="color: blue;font-weight: bolder;">
            <c:out value="${glMappingForm.message}"/>
          </div>
        </c:otherwise>
      </c:choose>
    </c:if>
    <html:form action="/glMapping?accessMode=${accessMode}" method="post" enctype="multipart/form-data" name="glMappingForm" type="com.logiware.form.GlMappingForm" scope="request">
      <c:choose>
        <c:when test="${glMappingForm.action=='editGlMapping' || glMappingForm.action=='saveGlMapping' || glMappingForm.action=='addGlMapping'}">
          <div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
              <html:hidden property="glMapping.id"/>
              <tr class="tableHeadingNew">
                <td>Charge/Cost Code Details</td>
                <td colspan="5" align="right">
                  <input type="button" class="buttonStyleNew" value="Go Back" onclick="goBack()"/>
                </td>
              </tr>
              <tr class="textlabelsBold">
                <td>Blue Screen Charge Code</td>
                <td><html:text property="glMapping.blueScreenChargeCode" styleId="glMappingBlueScreenChargeCode" 
                           styleClass="textlabelsBoldForTextBox uppercase" maxlength="5"/></td>
                <td>Charge Code</td>
                <td><html:text property="glMapping.chargeCode" styleId="glMappingChargeCode" styleClass="textlabelsBoldForTextBox uppercase"/></td>
                <td>Charge Descriptions</td>
                <td><html:text property="glMapping.chargeDescriptions" styleClass="textlabelsBoldForTextBox uppercase"/></td>
              </tr>
              <tr class="textlabelsBold">
                <td>GL Account</td>
                <td><html:text property="glMapping.glAcct" styleId="glMappingGlAcct" styleClass="textlabelsBoldForTextBox"/></td>
                <td>Transaction Type</td>
                <td><html:text property="glMapping.transactionType" styleId="glMappingTransactionType" 
                           styleClass="textlabelsBoldForTextBox uppercase"/></td>
                <td>Shipment Type</td>
                <td><html:text property="glMapping.shipmentType" styleId="glMappingShipmentType" styleClass="textlabelsBoldForTextBox uppercase"/></td>
              </tr>
              <tr class="textlabelsBold">
                <td>Suffix Value</td>
                <td><html:text property="glMapping.suffixValue" styleId="glMappingSuffixValue" styleClass="textlabelsBoldForTextBox uppercase"/></td>
                <td>
                  Suffix Alternate
                  <html:checkbox property="glMapping.special" styleId="glMapping_special" onclick="enableSuffixAlternate()"/>
                </td>
                <td>
                  <html:text property="glMapping.suffixAlternate" styleId="glMapping_suffixAlternate" styleClass="textlabelsBoldForTextBox uppercase"/>
                </td>
                <td>Derive_YN</td>
                <td><html:text property="glMapping.deriveYn" styleId="glMappingDeriveYN" styleClass="textlabelsBoldForTextBox uppercase"/></td>
              </tr>
              <tr class="textlabelsBold">
                <td>Sub Ledger</td>
                <td><html:text property="glMapping.subLedgerCode" styleId="glMappingSubLedgerCode" styleClass="textlabelsBoldForTextBox uppercase"/></td>
                <td>Rev Exp</td>
                <td><html:text property="glMapping.revExp" styleId="glMappingRevExp" styleClass="textlabelsBoldForTextBox uppercase"/></td>
                <td>BlueScreen Feedback</td>
                <td><html:checkbox property="glMapping.bluescreenFeedback" styleId="glMapping_bluescreenFeedback"/></td>
              </tr>
              <tr class="textlabelsBold">
                  <td>BL Level Cost</td>
                <td><html:checkbox property="glMapping.blLevelCost" styleId="glMapping_blLevelCost"/></td>
              </tr>
              <tr>
                <td align="center" colspan="6">
                  <input type="button" class="buttonStyleNew" style="width: 140px" value="Save Charge/Cost Code" onclick="saveGlMapping()" />
                </td>
              </tr>
            </table>
          </div>
        </c:when>
        <c:when test="${glMappingForm.action=='viewGlMapping'}">
          <div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
              <html:hidden property="glMapping.id"/>
              <html:hidden property="glMapping.blueScreenChargeCode"/>
              <tr class="tableHeadingNew">
                <td>Charge/Cost Code Details</td>
                <td colspan="5" align="right">
                  <input type="button" class="buttonStyleNew" value="Go Back" onclick="goBack()"/>
                </td>
              </tr>
              <tr class="textlabelsBold">
                <td>Blue Screen Charge Code</td>
                <td><html:text property="glMapping.blueScreenChargeCode" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
                <td>Charge Code</td>
                <td><html:text property="glMapping.chargeCode" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
                <td>Charge Descriptions</td>
                <td><html:text property="glMapping.chargeDescriptions" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
              </tr>
              <tr class="textlabelsBold">
                <td>GL Account</td>
                <td><html:text property="glMapping.glAcct" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
                <td>Transaction Type</td>
                <td><html:text property="glMapping.transactionType" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
                <td>Shipment Type</td>
                <td><html:text property="glMapping.shipmentType" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
              </tr>
              <tr class="textlabelsBold">
                <td>Suffix Value</td>
                <td><html:text property="glMapping.suffixValue" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
                <td>Suffix Alternate</td>
                <td><html:text property="glMapping.suffixAlternate" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
                <td>Derive_YN</td>
                <td><html:text property="glMapping.deriveYn" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
              </tr>
              <tr class="textlabelsBold">
                <td>Sub Ledger</td>
                <td><html:text property="glMapping.subLedgerCode" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
                <td>Rev Exp</td>
                <td><html:text property="glMapping.revExp" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/></td>
              </tr>
            </table>
          </div>
        </c:when>
        <c:otherwise>
          <div>
            <table width="100%" border="0" cellpadding="0" cellspacing="3" class="tableBorderNew">
              <tr class="tableHeadingNew"><td colspan="6">Search Charge/Cost Codes</td></tr>
              <tr class="textlabelsBold">
                <td>Charge/Cost Code</td>
                <td>
                    <html:text property="searchBychargeCode" styleId="searchBychargeCode" styleClass="textlabelsBoldForTextBox" 
                               style="text-transform:uppercase;"/>
                  <input type="hidden" name="chargeCodeValid" id="chargeCodeValid" value="${glMappingForm.searchBychargeCode}"/>
                  <div class="newAutoComplete" id="chargeCodeDiv"></div>
                </td>
                <td>Starting Account</td>
                <td>
                  <html:text property="startAccount" styleId="startAccount" styleClass="textlabelsBoldForTextBox"/>
                  <input type="hidden" name="startAccountValid" id="startAccountValid" value="${glMappingForm.startAccount}"/>
                  <div class="newAutoComplete" id="startAccountDiv"></div>
                </td>
                <td>Ending Account </td>
                <td>
                  <html:text property="endAccount" styleId="endAccount" styleClass="textlabelsBoldForTextBox"/>
                  <input type="hidden" name="endAccountValid" id="endAccountValid" value="${glMappingForm.endAccount}"/>
                  <div class="newAutoComplete" id="endAccountDiv"></div>
                </td>
              </tr>
              <tr class="textlabelsBold">
                <td colspan="4" align="right">
                  <input type="button" value="Search" class="buttonStyleNew" onclick="searchGlMapping()"/>
                  <input type="button" value="Add" class="buttonStyleNew display-none" onclick="addGlMapping()"/>
                  <input type="button" value="Export To Excel" class="buttonStyleNew display-none" style="width: 120px" onclick="exportToExcel()"/>
                </td>
                <td>Charge/Cost Code Sheet</td>
                <td>
                  <html:file property="glMappingSheet" styleClass="textlabelsBoldForTextBox"/>
                  <input type="button" value="Upload" class="buttonStyleNew display-none" onclick="upload()"/>
                </td>
              </tr>
              <tr class="tableHeadingNew">
                <td colspan="6">List of Charge/Cost Codes</td>
              </tr>
              <tr>
                <td colspan="6">
                  <div class="scrolldisplaytable">
                    <display:table name="${glMappings}" pagesize="100" class="displaytagstyleNew" id="glMapping" sort="list" 
                                   style="width:100%">
                      <display:setProperty name="paging.banner.some_items_found">
                        <span class="pagebanner">
                          <font color="blue">{0}</font> Charge/Cost Codes displayed,For more Data click on Page Numbers.
                        </span>
                      </display:setProperty>
                      <display:setProperty name="paging.banner.one_item_found">
                        <span class="pagebanner">One {0} displayed. Page Number</span>
                      </display:setProperty>
                      <display:setProperty name="paging.banner.all_items_found">
                        <span class="pagebanner">{0} {1} displayed, Page Number</span>
                      </display:setProperty>
                      <display:setProperty name="basic.msg.empty_list">
                        <span class="pagebanner">No Charge/Cost Codes found</span>
                      </display:setProperty>
                      <display:setProperty name="paging.banner.placement" value="bottom" />
                      <display:setProperty name="paging.banner.item_name" value="Charge/Cost Code"/>
                      <display:setProperty name="paging.banner.items_name" value="Charge/Cost Codes"/>
                      <display:column title="Blue Screen<br>Charge Code" property="blueScreenChargeCode" sortable="true" style="text-align:center"/>
                      <display:column title="Charge Code" property="chargeCode" sortable="true" class="uppercase"/>
                      <display:column title="Descriptions" property="chargeDescriptions" sortable="true" class="uppercase"/>
                      <display:column title="GL Account" property="glAcct" sortable="true"/>
                      <display:column title="Shipment Type" property="shipmentType" sortable="true" class="uppercase"/>
                      <display:column title="Transaction Type" property="transactionType" sortable="true" class="uppercase"/>
                      <display:column title="Suffix Value" property="suffixValue" sortable="true"/>
                      <display:column title="Suffix Alternate" property="suffixAlternate" sortable="true" class="uppercase"/>
                      <display:column title="Derive Y/N" property="deriveYn" sortable="true" class="uppercase"/>
                      <display:column title="Subledger Code" property="subLedgerCode" sortable="true" class="uppercase"/>
                      <display:column title="Rev/Exp" property="revExp" sortable="true" class="uppercase"/>
                      <display:column title="Blue Screen<br>Feedback">
                          <c:if test="${glMapping.bluescreenFeedback}">
                              <img alt="" src="${path}/img/icons/ok.gif"/>
                          </c:if>
                      </display:column>
                      <display:column title="Actions">
                        <c:if test="${accessMode!=0}">
                            <img alt="" title="Edit" src="${path}/img/icons/edit.gif" onclick="editGlMapping('${glMapping.id}');"/>
                            <img alt="" title="Delete" src="${path}/img/icons/delete.gif" onclick="deleteGlMapping('${glMapping.id}');"/>
                        </c:if>
                            <img alt="" title="View" src="${path}/img/icons/pubserv.gif" border="0" onclick="viewGlMapping('${glMapping.id}');"/>
                            <img alt="" title="Notes" src="${path}/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '${path}/notes.do?moduleId='+'${notesConstants.CHARGE_CODE}&moduleRefId=${glMapping.id}',300,900);"/>
                      </display:column>
                    </display:table>
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </c:otherwise>
      </c:choose>
      <html:hidden property="action"/>
      <html:hidden property="glId"/>
    </html:form>
  </body>
  <script type="text/javascript">
    function searchGlMapping(){
      if((document.glMappingForm.startAccount.value!="" && document.glMappingForm.endAccount.value=="") ||
        (document.glMappingForm.startAccount.value=="" && document.glMappingForm.endAccount.value!="")) {
        alert("Please select Starting and Ending Account..");
        return;
      }
      document.glMappingForm.action.value="searchGlMapping";
      document.glMappingForm.submit();
    }
    function enableSuffixAlternate(){
      if(document.getElementById("glMapping_special").checked){
        document.getElementById("glMapping_suffixAlternate").disabled=false;
        document.getElementById("glMapping_suffixAlternate").focus();
      }else{
        document.getElementById("glMapping_suffixAlternate").disabled=true;
      }
    }
    function exportToExcel(){
      if((document.glMappingForm.startAccount.value!="" && document.glMappingForm.endAccount.value=="") ||
        (document.glMappingForm.startAccount.value=="" && document.glMappingForm.endAccount.value!="")) {
        alert("Please select Starting and Ending Account..");
        return;
      }
      document.glMappingForm.action.value="exportGlMapping";
      document.glMappingForm.submit();
    }
    function editGlMapping(glId){
      document.glMappingForm.action.value="editGlMapping";
      document.glMappingForm.glId.value=glId;
      document.glMappingForm.submit();
    }

    function deleteGlMapping(glId){
      document.glMappingForm.action.value="deleteGlMapping";
      document.glMappingForm.glId.value=glId;
      document.glMappingForm.submit();
    }
    function viewGlMapping(glId){
      document.glMappingForm.action.value="viewGlMapping";
      document.glMappingForm.glId.value=glId;
      document.glMappingForm.submit();

    }
    function addGlMapping(){
      document.glMappingForm.action.value="addGlMapping";
      document.glMappingForm.submit();

    }
    function upload(){
      if(trim(document.glMappingForm.glMappingSheet.value)==""){
        alert("Please Include Charge/Cost Code Sheet");
        return;
      }
      document.glMappingForm.action.value="uploadGlMapping";
      document.glMappingForm.submit();
    }
    function saveGlMapping(){
      if(trim(document.getElementById("glMappingBlueScreenChargeCode").value)==""){
          alert("Please enter blue screen charge code");
          document.getElementById("glMappingBlueScreenChargeCode").focus();
          return;
      }else if(trim(document.getElementById("glMappingChargeCode").value)==""){
          alert("Please enter charge code");
          document.getElementById("glMappingChargeCode").focus();
          return;
      }else if(trim(document.getElementById("glMappingGlAcct").value)==""){
          alert("Please enter gl account");
          document.getElementById("glMappingGlAcct").focus();
          return;
      }else if(trim(document.getElementById("glMappingTransactionType").value)==""){
          alert("Please enter transaction type");
          document.getElementById("glMappingTransactionType").focus();
          return;
      }else if(trim(document.getElementById("glMappingShipmentType").value)==""){
          alert("Please enter shipment type");
          document.getElementById("glMappingShipmentType").focus();
          return;
      }else if(trim(document.getElementById("glMappingSuffixValue").value)==""){
          alert("Please enter suffix value");
          document.getElementById("glMappingSuffixValue").focus();
          return;
      }else if(trim(document.getElementById("glMappingDeriveYN").value)==""){
          alert("Please enter derive YN");
          document.getElementById("glMappingDeriveYN").focus();
          return;
      }else if(trim(document.getElementById("glMappingSubLedgerCode").value)==""){
          document.getElementById("glMappingSubLedgerCode").focus();
          alert("Please enter sub ledger");
          return;
      }else if(trim(document.getElementById("glMappingRevExp").value)==""){
          alert("Please enter rev exp");
          document.getElementById("glMappingRevExp").focus();
          return;
      }
      document.glMappingForm.action.value="saveGlMapping";
      document.glMappingForm.submit();
    }

    function goBack(){
      document.glMappingForm.action.value="searchGlMapping";
      document.glMappingForm.submit();
    }
    function addMoreParams(element, entry) {
      return entry;
    }
      if(document.getElementById("searchBychargeCode")){
      AjaxAutocompleter("searchBychargeCode", "chargeCodeDiv","searchBychargeCode", "chargeCodeValid", "${path}/servlet/AutoCompleterServlet?action=ChargeCode&from=glMapping&textFieldId=searchBychargeCode","","");
    }
      if(document.getElementById("startAccount")){
      AjaxAutocompleter("startAccount", "startAccountDiv","startAccount", "startAccountValid", "${path}/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=startAccount&tabName=GLMAPPING","","");
    }
      if(document.getElementById("startAccount")){
      AjaxAutocompleter("endAccount", "endAccountDiv","endAccount", "endAccountValid","${path}/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=endAccount&tabName=GLMAPPING","","");
    }
    if(document.getElementById("glMapping_special")){
      enableSuffixAlternate();
    }
    jQuery(document).ready(function(){
        jQuery(document).keypress(function(event) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if(keycode == 13) {
                searchGlMapping();
            }
        });
    });
  </script>
  <%@include file="../includes/resources.jsp" %>
  <%@include file="../includes/baseResourcesForJS.jsp"%>
  <script type="text/javascript">
    jQuery.noConflict();
    jQuery(document).ready(function(){
      jQuery("#glMapping").tablesorter({widgets: ['zebra']});
    });
  </script>
</html>
