<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/script-aculo-us.tabs.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/isValidEmail.js"></script>
        <script language="javascript" type="text/javascript">
            function cancel(){
                parent.parent.GB_hide();
            }
            function clearAcCity(){
                document.addWarehouseForm.acCity.value = "";
                document.getElementById("acCity_check").value = "";
                document.addWarehouseForm.acCountry.value = "";
                document.addWarehouseForm.acState.value = "";
            }
            function clearCity(){
                document.addWarehouseForm.city.value = "";
                document.getElementById("city_check").value = "";
                document.addWarehouseForm.country.value = "";
                document.addWarehouseForm.state.value = "";
            }
            function fillCountryAndState(){
                var array = new Array();
                var city = document.addWarehouseForm.city.value;
                array = city.split('/');
                document.addWarehouseForm.city.value = array[0];
                document.getElementById("city_check").value = array[0];
                document.addWarehouseForm.country.value = array[1];
                if(undefined != array[2] && null != array[2]){
                    document.addWarehouseForm.state.value = array[2];
                }
            }
            function fillAcCountryAndState(){
                var array = new Array();
                var city = document.addWarehouseForm.acCity.value;
                array = city.split('/');
                document.addWarehouseForm.acCity.value = array[0];
                document.getElementById("acCity_check").value = array[0];
                document.addWarehouseForm.acCountry.value = array[1];
                if(undefined != array[2] && null != array[2]){
                    document.addWarehouseForm.acState.value = array[2];
                }
            }
            function save(type,screen,field){
            <%--validateCode();--%>
                       if(document.addWarehouseForm.warehouseCode.value != '' && document.addWarehouseForm.warehouseCode.value.length < 4){
                           alertNew("WareHouse Code Should be 4 Characters");
                       }else if(trim(document.addWarehouseForm.warehouseName.value) == ''){
                           alertNew("Please Enter WareHouse Name");
                       }else if(trim(document.addWarehouseForm.address.value) == ''){
                           alertNew("Please Enter WareHouse Address");
                       }else if(document.addWarehouseForm.city.value == ''){
                           alertNew("Please Enter City");
                       }else if(document.addWarehouseForm.address.value.length!="" && document.addWarehouseForm.address.value.length>200){
                           alertNew("Address cannot exceed 200 characters");
                       }else if(document.addWarehouseForm.acAddress.value.length!="" && document.addWarehouseForm.acAddress.value.length>25){
                           alertNew("AcAddress must be 25 letters");
                           document.addWarehouse.acAddress.focus();
                       }else{
                           var address = document.addWarehouseForm.warehouseName.value+"\n"+document.addWarehouseForm.address.value;
                           if(document.addWarehouseForm.city.value != ''){
                               address = address+"\n"+document.addWarehouseForm.city.value;
                           }
                           if(document.addWarehouseForm.state.value != ''){
                               address = address+","+document.addWarehouseForm.state.value;
                           }
                           if(null != type && type == 'FCLI' && screen == 'BL'){
                               parent.parent.document.fclBillLaddingform.importWareHouseAddress.value =address;
                               parent.parent.document.fclBillLaddingform.importWareHouseName.value =document.addWarehouseForm.warehouseName.value;
                               parent.parent.document.fclBillLaddingform.importWareHouseCode.value =(document.addWarehouseForm.warehouseCode.value).toUpperCase();
                               parent.parent.document.getElementById('wareHouse_check').value =document.addWarehouseForm.warehouseName.value;
                           }else{
                               if(type=='LCLE'){
                                   var namelength=document.addWarehouseForm.warehouseName.value.length;
                                    address=address.substr(namelength+1,address.length);
                                   parent.parent.document.getElementById('address').value=address;
                                   parent.parent.document.getElementById('shipSupplier').value=document.addWarehouseForm.warehouseName.value;
                                   parent.parent.$('#greenPlusIcon').attr("src","${path}/jsps/LCL/images/greyplus.jpg");
                                   parent.parent.$('#greenPlusIcon').attr("width",22);
                                   parent.parent.$('#greenPlusIcon').attr("height",22);
                                   parent.parent.$('#greenPlusIcon').attr("onclick","");
                               }else{
                                   if(field == 'return'){
                                       parent.parent.document.EditBookingsForm.addressofDelivery.value =address;
                                       parent.parent.document.EditBookingsForm.equipmentReturnName.value =document.addWarehouseForm.warehouseName.value;
                                       parent.parent.document.EditBookingsForm.equipmentReturnTemp.value =(document.addWarehouseForm.warehouseCode.value).toUpperCase();
                                       parent.parent.document.getElementById('equipmentReturn_check').value =document.addWarehouseForm.warehouseName.value;
                                   }else{
                                       parent.parent.document.EditBookingsForm.emptypickupaddress.value =address;
                                       parent.parent.document.EditBookingsForm.exportPositioning.value =document.addWarehouseForm.warehouseName.value;
                                       parent.parent.document.EditBookingsForm.wareHouseTemp.value =(document.addWarehouseForm.warehouseCode.value).toUpperCase();
                                       parent.parent.document.getElementById('wareHouse_check').value =document.addWarehouseForm.warehouseName.value;
                                       if(parent.parent.document.EditBookingsForm.autoAddressFill.checked){
                                           parent.parent.document.EditBookingsForm.addressofDelivery.value =address;
                                       }else{
                                           parent.parent.document.EditBookingsForm.addressofDelivery.value ='';
                                       }
                                   }
                               }
                           }
                           document.addWarehouseForm.buttonValue.value ="saveWareHouse";
                           document.addWarehouseForm.submit();
                       }
                   }
                   function validateCode(){
                       var code = document.addWarehouseForm.warehouseCode.value;
                       jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName: "validateWareHouseCode",
                                param1: code,
                                dataType: "json"
                            },
                            async:false,
                            success: function (data) {
                               if(data){
                                   alertNew("WareHouse Code Already Exist");
                                   document.addWarehouseForm.warehouseCode.value = "";
                                   return;
                               }
                            }
                        });
                   }
        </script>
        <c:if test="${not empty message}">
            <script>
                parent.parent.GB_hide();
            </script>
        </c:if>
    </head>    
    <body class="whitebackgrnd" />
    <div id="cover" style="width: expression(document.body.offsetWidth+'px');height: expression(document.body.offsetHeight+120+'px');display: none"></div>
    <div id="AlertBox" class="alert">
        <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
        <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button"  class="buttonStyleForAlert" value="OK"
                   onclick="document.getElementById('AlertBox').style.display='none';grayOut(false,'');">
        </form>
    </div>
    <html:form action="/addWarehouse" name="addWarehouseForm" type="com.gp.cong.logisoft.struts.form.AddWarehouseForm" scope="request">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
            <tr>
                <td>
                    <table border="0" width="100%">
                        <tr class="tableHeadingNew">
                            <td>
                                Warehouse Details
                            </td>
                            <td align="right">
                                <input type="button" class="buttonStyleNew" value="Save" id="saveWhse" onclick="save('${param.wareHouseType}','${param.screenName}','${param.field}')" />
                                <input type="button" class="buttonStyleNew" value="Cancel" onclick="cancel()" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="textlabelsBold">
                <td>
                    <table width="100%" border="0" cellspacing="0">
                        <tr class="textlabelsBold">
                            <td>Warehouse Code</td>
                            <td><html:text property="warehouseCode"  size="4"  styleClass="textlabelsBoldForTextBox " maxlength="4"   style="text-transform: uppercase"></html:text></td>
                            <td  width="18%">Phone</td>
                            <td><html:text property="phone" size="11" maxlength="50" onkeyup="phonevalid(this)"  styleClass="textlabelsBoldForTextBox"/>
                                <html:text property="extension" maxlength="4" size="4" styleClass="textlabelsBoldForTextBox"/></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td  width="21%">Warehouse Name</td>
                            <td width="31%"><html:text property="warehouseName" maxlength="30" styleClass="textlabelsBoldForTextBox mandatory" style="text-transform: uppercase"/></td>
                            <td >Fax</td>
                            <td><html:text property="fax" value="" maxlength="50"  styleClass="textlabelsBoldForTextBox"/></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td >Address</td>
                            <td ><html:textarea property="address"  cols="25" styleClass="textlabelsBoldForTextBox mandatory" 
                                           style="text-transform: uppercase" onchange="limitTextchar(this,200)"
                                           onkeypress="limitTextchar(this,200)"></html:textarea></td>
                            <td >City &nbsp; </td>
                            <td>
                                <input name="city" id="city"  size="20" class="textlabelsBoldForTextBox mandatory"/>
                                <input type="hidden" id="city_check" name="city_check" size="20" class="textlabelsBoldForTextBox"/>
                                <div id="city_choices"  style="display: none;width: 5px;"  align="left"  class="autocomplete"></div>
                                <script type="text/javascript">
                                    initAutocompleteWithFormClear("city","city_choices","","city_check",
                                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=WAREHOUSE&isDojo=false","fillCountryAndState()","clearCity()");
                                </script>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>Manager Name&nbsp;</td>
                            <td>
                                <input  name="managerName" id="managerName"class="textlabelsBoldForTextBox" style="text-transform: uppercase"/>
                                <div id="managerName_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                <script type="text/javascript">
                                    initOPSAutocomplete("managerName","managerName_choices","","",
                                    "${path}/actions/getUserDetails.jsp?tabName=WAREHOUSE&from=0&isDojo=false","");
                                </script>
                            </td>
                            <td >Country</td>
                            <td><html:text property="country"  readonly="true" styleClass="textlabelsBoldForTextBox"/>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td >IPI Vendor # &nbsp; </td>
                            <td>
                                <input  name="ipiVendor" id="ipiVendor" value="" class="textlabelsBoldForTextBox"/>
                                <div id="ipiVendor_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                <script type="text/javascript">
                                    initOPSAutocomplete("ipiVendor","ipiVendor_choices","","",
                                    "${path}/actions/getCustomer.jsp?tabName=WAREHOUSE&from=0&isDojo=false","");
                                </script>
                            </td>
                            <td >State</td>
                            <td><html:text property="state"  readonly="true" styleClass="textlabelsBoldForTextBox" maxlength="2"/></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td >IPI Commodity # &nbsp;</td>
                            <td>
                                <input name="ipiCommodity" id="ipiCommodity" value=""  class="textlabelsBoldForTextBox"/>
                                <div id="commcode_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                <script type="text/javascript">
                                    initAutocomplete("ipiCommodity","commcode_choices","","",
                                    "${path}/actions/getChargeCode.jsp?tabName=QUOTE&isDojo=false","");
                                </script>
                            </td>
                            <td >Zip</td>
                            <td><html:text property="zip" styleId="zip" value="" maxlength="10" styleClass="textlabelsBoldForTextBox"/></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="tableHeadingNew">
                            <td>
                                Air Cargo Warehouse
                            </td>
                        </tr>
                        <tr>
                            <td><table width="100%" border="0" cellspacing="0">
                                    <tr class="textlabelsBold">
                                        <td  valign="top" width="21%">General Air Cargo Wrhse</td>
                                        <td valign="top" width="31%"><html:text property="generalAirCargo" maxlength="25" styleClass="textlabelsBoldForTextBox" /></td>
                                        <td width="18%">City &nbsp;  </td>
                                        <td>
                                            <input name="acCity" id="acCity" class="textlabelsBoldForTextBox"/>
                                            <input type="hidden" name="acCity_check" id="acCity_check" class="textlabelsBoldForTextBox"/>
                                            <div id="acCity_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                            <script type="text/javascript">
                                                initAutocompleteWithFormClear("acCity","acCity_choices","","acCity_check",
                                                "${path}/actions/getUnlocationCodeDesc.jsp?tabName=WAREHOUSE&isDojo=false","fillAcCountryAndState()","clearAcCity()");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td valign="top">Address</td>
                                        <td valign="top"><html:textarea property="acAddress" cols="25" styleClass="textlabelsBoldForTextBox" 
                                                       style="text-transform: uppercase" onchange="limitTextchar(this,200)"
                                                       onkeypress="limitTextchar(this,200)"/></td>
                                        <td>Country</td>
                                        <td><html:text property="acCountry" readonly="true" styleClass="textlabelsBoldForTextBox"/></td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Phone</td>
                                        <td><html:text property="acPhone" maxlength="50"  size="11" styleClass="textlabelsBoldForTextBox"/>
                                            <html:text property="acExtension" value="" maxlength="4" size="4" styleClass="textlabelsBoldForTextBox"/></td>
                                        <td>State</td>
                                        <td><html:text property="acState"  readonly="true" styleClass="textlabelsBoldForTextBox"/></td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Fax </td>
                                        <td><html:text property="acFax" maxlength="50"  styleClass="textlabelsBoldForTextBox"/></td>
                                        <td>Zip</td>
                                        <td><html:text property="acZip"  maxlength="10" styleClass="textlabelsBoldForTextBox" /></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <html:hidden property="buttonValue" styleId="buttonValue" />
        <html:hidden property="warehouseType" styleId="warehouseType" value="${param.wareHouseType}"/>
    </html:form>
</body>
</html>
