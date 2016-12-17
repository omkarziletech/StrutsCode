<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.beans.SearchUserBean,com.gp.cong.logisoft.domain.Warehouse"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.GenericCode"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
    response.addHeader("Pragma", "No-cache");
    response.addHeader("Cache-Control", "no-cache");
%>
<%
    String path = request.getContextPath();
    Warehouse warehouse = new Warehouse();
    if (session.getAttribute("warehouse") != null) {
        warehouse = (Warehouse) session.getAttribute("warehouse");
    }
    String msg = "";
    if (request.getAttribute("message") != null) {
        msg = (String) request.getAttribute("message");
    }
%>
<html>
    <head>
        <title>SearchMenu/Action</title>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/script-aculo-us.tabs.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/isValidEmail.js"></script>
        <script language="javascript" type="text/javascript">
            start = function() {
                serializeForm();
            }
            window.onload = start;
        </script>
        <script language="javascript" type="text/javascript">

            function save()
            {
                var wrhscode = document.addWarehouse.warehouseCode;
                if (wrhscode.value == "")
                {
                    alert("Please enter the Warehouse Code ");
                    wrhscode.focus();
                    return;
                }
                if (wrhscode.value.match(" "))
                {
                    alert("Space is not allowed for Warehouse Code");
                    wrhscode.focus();
                    return;
                }

                if (document.addWarehouse.city.value == "")
                {
                    alert("Please enter the city");
                    document.addWarehouse.city.value = "";
                    document.addWarehouse.city.focus();
                    return;
                }
                if (isSpecial(wrhscode.value) == false)
                {
                    alert("Special Characters not allowed for Warehouse Code.");
                    wrhscode.value = "";
                    wrhscode.focus();
                    return;
                }
                var wrhsname = document.addWarehouse.warehouseName;
                if (trim(wrhsname.value) == "")
                {
                    alert("Please enter the Warehouse Name ");
                    wrhsname.focus();
                    return;
                }
                if (document.addWarehouse.zip.value != "")
                {
                    if (document.addWarehouse.zip.value.length < 5)
                    {
                        if (document.addWarehouse.zip.value.length != 5)
                        {
                            alert("zip must be 5 digits");

                            document.addWarehouse.zip.focus();
                            return;
                        }
                    }
                    else if (document.addWarehouse.zip.value.length > 5)
                    {
                        if (document.addWarehouse.zip.value.length > 10)
                        {
                            alert("zip must be 9 digits");

                            document.addWarehouse.zip.focus();
                            return;
                        }
                    }
                }
                if (document.addWarehouse.address.value.length != "" && document.addWarehouse.address.value.length > 25)
                {
                    alert("Address must be 25 leters");
                    document.addWarehouse.address.focus();
                    return;
                }
                if (document.addWarehouse.acAddress.value.length != "" && document.addWarehouse.acAddress.value.length > 25)
                {
                    alert("AcAddress must be 25 letters");
                    document.addWarehouse.acAddress.focus();
                    return;
                }
                if (IsNumeric(document.addWarehouse.zip.value.replace(/ /g, '')) == false)
                {
                    alert("Zip Code should be Numeric.");
                    document.addWarehouse.zip.value = "";
                    document.addWarehouse.zip.focus();
                    return;
                }
                if (IsNumeric(document.addWarehouse.phone.value.replace(/ /g, '')) == false)
                {
                    alert("Phone Number should be Numeric.");
                    document.addWarehouse.phone.value = "";
                    document.addWarehouse.phone.focus();
                    return;
                }

                if (IsNumeric(document.addWarehouse.fax.value.replace(/ /g, '')) == false)
                {
                    alert("Fax Number should be Numeric.");
                    document.addWarehouse.fax.value = "";
                    document.addWarehouse.fax.focus();
                    return;
                }
                if (document.addWarehouse.acZip.value != "")
                {
                    if (document.addWarehouse.acZip.value.length < 5)
                    {
                        if (document.addWarehouse.acZip.value.length != "5")
                        {
                            alert("zip must be 5 digits");

                            document.addWarehouse.acZip.focus();
                            return;
                        }
                    }
                    else if (document.addWarehouse.acZip.value.length > 5)
                    {
                        if (document.addWarehouse.acZip.value.length != "10")
                        {
                            alert("zip must be 9 digits");

                            document.addWarehouse.acZip.focus();
                            return;
                        }
                    }
                }
                if (IsNumeric(document.addWarehouse.acZip.value.replace(/ /g, '')) == false)
                {
                    alert("Air Cargo Zip Code should be Numeric.");
                    document.addWarehouse.acZip.value = "";
                    document.addWarehouse.acZip.focus();
                    return;
                }
                if (IsNumeric(document.addWarehouse.acPhone.value.replace(/ /g, '')) == false)
                {
                    alert("Air Cargo Phone Number should be Numeric.");
                    document.addWarehouse.acPhone.value = "";
                    document.addWarehouse.acPhone.focus();
                    return;
                }

                if (IsNumeric(document.addWarehouse.acFax.value.replace(/ /g, '')) == false)
                {
                    alert("Air Cargo Fax Number should be Numeric.");
                    document.addWarehouse.acFax.value = "";
                    document.addWarehouse.acFax.focus();
                    return;
                }
                document.addWarehouse.buttonValue.value = "add";
                document.addWarehouse.submit();
            }
            function cancel() {
                document.addWarehouse.buttonValue.value = "cancel";
                var result = confirm("Do you want to save the changes");
                if (result) {
                    save();
                }
                document.addWarehouse.submit();
            }
            function fillCountryAndState() {
                var array = new Array();
                var city = document.addWarehouse.city.value;
                array = city.split('/');
                document.addWarehouse.city.value = array[0];
                document.addWarehouse.country.value = array[1];
                if (undefined != array[2] && null != array[2]) {
                    document.addWarehouse.state.value = array[2];
                }
            }
            function fillAcCountryAndState() {
                var array = new Array();
                var city = document.addWarehouse.acCity.value;
                array = city.split('/');
                document.addWarehouse.acCity.value = array[0];
                document.addWarehouse.acCountry.value = array[1];
                if (undefined != array[2] && null != array[2]) {
                    document.addWarehouse.acState.value = array[2];
                }
            }
            function toUppercase(obj) {
                obj.value = obj.value.toUpperCase();
            }
            function clearDefault(el) {
                if (el.value == "") {
                    document.addWarehouse.state.value = "";
                    document.addWarehouse.country.value = "";
                }
            }
            function defaultclear(el) {
                if (el.value == "") {
                    document.addWarehouse.acState.value = "";
                    document.addWarehouse.acCountry.value = "";
                }
            }
            function limitText(limitField, limitCount, limitNum) {
                limitField.value = limitField.value.toUpperCase();
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }
            function searchcity() {
                document.addWarehouse.buttonValue.value = "searchcity";
                document.addWarehouse.submit();
            }
            function searchaccity() {
                document.addWarehouse.buttonValue.value = "searchaccity";
                document.addWarehouse.submit();
            }
            function zipcode(obj) {
                if (document.addWarehouse.country.value == "" && document.addWarehouse.country.value != "UNITED STATES") {
                    if ((document.addWarehouse.zip.value.length > 5) || IsNumeric(document.addWarehouse.zip.value.replace(/ /g, '')) == false) {
                        alert("please enter the only 5 digits and numerics only");
                        document.addWarehouse.zip.value = "";
                        document.addWarehouse.zip.focus();
                    }
                } else {
                    getzip(obj);
                }
            }
// to check zipcode of accountry
            function zipcode1(obj) {
                if (document.addWarehouse.acCountry.value == "" && document.addWarehouse.acCountry.value != "UNITED STATES") {
                    if ((document.addWarehouse.acZip.value.length > 5) || IsNumeric(document.addWarehouse.acZip.value.replace(/ /g, '')) == false) {
                        alert("please enter the only 5 digits and numerics only");
                        document.addWarehouse.acZip.value = "";
                        document.addWarehouse.acZip.focus();
                    }
                } else {
                    getzip(obj);
                }
            }
            function phonevalid(obj) {
                if (document.addWarehouse.country.value == "" && document.addWarehouse.country.value != "UNITED STATES") {
                    if ((document.addWarehouse.phone.value.length > 10) || IsNumeric(document.addWarehouse.phone.value.replace(/ /g, '')) == false) {
                        alert("please enter the only 10 digits and numerics only");
                        document.addWarehouse.phone.value = "";
                        document.addWarehouse.phone.focus();
                    }
                } else {
                    getIt(obj);
                }
            }
            function phonevalid1(obj) {
                if (document.addWarehouse.acCountry.value == "" && document.addWarehouse.acCountry.value != "UNITED STATES") {
                    if ((document.addWarehouse.acPhone.value.length > 10) || IsNumeric(document.addWarehouse.acPhone.value.replace(/ /g, '')) == false) {
                        alert("please enter the only 10 digits and numerics only");
                        document.addWarehouse.acPhone.value = "";
                        document.addWarehouse.acPhone.focus();
                    }
                } else {
                    getIt(obj);
                }
            }
            function faxvalid(obj) {
                if (document.addWarehouse.country.value == "" && document.addWarehouse.country.value != "UNITED STATES") {
                    if ((document.addWarehouse.fax.value.length > 10) || IsNumeric(document.addWarehouse.fax.value.replace(/ /g, '')) == false) {
                        alert("please enter the only 10 digits and numerics only");
                        document.addWarehouse.fax.value = "";
                        document.addWarehouse.fax.focus();
                    }
                } else {
                    getIt(obj);
                }
            }

        </script>
    </head>
    <body class="whitebackgrnd"    >
        <html:form action="/addWarehouse" name="addWarehouse" type="com.gp.cong.logisoft.struts.form.AddWarehouseForm" scope="request">
            <div align="right">
                <input type="button" class="buttonStyleNew" value="Save" onclick="save()" /> 
                <input type="button" class="buttonStyleNew" value="Go Back" onclick="cancel()" /> 
            </div>
            <table width="100%" height="100%" border="0" cellpadding="0" 
                   cellspacing="0" class="tableBorderNew">
                <tr valign="top">
                    <td valign="top">
                        <table border="0" width="100%">
                            <tr class="tableHeadingNew">
                                Warehouse Details</tr>

                        </table>
                    </td>
                </tr>


                <tr>
                    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">

                            <tr>
                                <td>
                                    <table width="100%" border="0" cellspacing="0">
                                        <tr valign="top" >
                                            <td  class="style2" >Warehouse Code</td>
                                            <td><html:text property="warehouseCode" value="<%=warehouse.getWarehouseNo()%>" size="3" readonly="true" styleClass="varysizeareahighlightgrey" maxlength="3" onkeyup="toUppercase(this)" ></html:text></td>
                                            </tr>
                                            <tr>
                                                <td  class="style2" >Warehouse Type</td>
                                                <td>
                                                <html:select property="warehouseType" styleClass="selectboxstyle"  style="width:118px" >
                                                    <html:option value="">Select</html:option>
                                                    <html:option value="FCLE">FCLE</html:option>
                                                    <html:option value="LCLE">LCLE</html:option>
                                                    <html:option value="FCLI">FCLI</html:option>
                                                    <html:option value="LCLI">LCLI</html:option>
                                                    <html:option value="AIRE">AIRE</html:option>
                                                    <html:option value="AIRI">AIRI</html:option>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr>

                                            <td class="style2" width="21%">Warehouse Name*</td>
                                            <td width="31%"><html:text property="warehouseName"  maxlength="30" styleClass="areahighlightyellow1" onkeyup="toUppercase(this)"/></td>
                                            <td class="style2" width="18%">Phone</td>
                                            <td><html:text property="phone" size="11"  maxlength="40"   />
                                                <html:text property="extension"  maxlength="4" size="4"/></td>
                                        </tr>
                                        <tr>
                                             <td class="style2" width="21%">CFS Devanning Email</td>
                                            <td ><html:text property="cfsDevanningEmail"  maxlength="50" styleClass="warehousewarehouseareahighlightwhite"/></td>
                                            
                                        </tr>
                                        <tr>
                                            <td><table width="100%" border="0"><tr class="style2"><td>Manager Name*&nbsp;</td></tr></table></td>
                                            <td>
                                                <input  name="managerName" id="managerName" />
                                                <div id="managerName_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                    initOPSAutocomplete("managerName", "managerName_choices", "", "",
                                            "${path}/actions/getUserDetails.jsp?tabName=WAREHOUSE&from=0&isDojo=false", "");
                                                </script>
                                            </td>
                                            <td class="style2">Fax</td>
                                            <td><html:text property="fax"  maxlength="40"  styleClass="varysizeareahighlightwhite"/></td>
                                        </tr>
                                        <tr>
                                            <td class="style2">Address*</td>
                                            <td ><html:textarea property="address"  cols="25" styleClass="selectboxstyle" onkeyup="limitText(this.form.address,this.form.countdown,25)" ></html:textarea></td>
                                                <td class="style2">IPI Vendor # &nbsp; </td>
                                                <td>
                                                    <input  name="ipiVendor" id="ipiVendor"/>
                                                    <div id="ipiVendor_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initOPSAutocomplete("ipiVendor", "ipiVendor_choices", "", "",
                                                                "${path}/actions/getCustomer.jsp?tabName=WAREHOUSE&from=0&isDojo=false", "");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr>

                                            <td class="style2">City &nbsp;  </td>
                                            <td>
                                                <input name="city" id="city" size="20" />
                                                <div id="city_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("city", "city_choices", "", "",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=WAREHOUSE&isDojo=false", "fillCountryAndState()", "");
                                                </script>
                                            </td>
                                            <td class="style2">IPI Commodity # &nbsp;</td>
                                            <td>
                                                <input name="ipiCommodity" id="ipiCommodity"/>
                                                <div id="commcode_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("ipiCommodity", "commcode_choices", "", "",
                                                            "${path}/actions/getChargeCode.jsp?tabName=WAREHOUSE&isDojo=false", "");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="style2">State</td>
                                            <td><html:text property="state"  readonly="true" styleClass="varysizeareahighlightgrey"/></td>	

                                            <td class="style2">Zip</td>
                                            <td><html:text property="zip"  maxlength="10" styleClass="varysizeareahighlightwhite" onkeyup="zipcode(this)" /></td>

                                        </tr>
                                        <tr>
                                            <td class="style2">Country</td>
                                            <td><html:text property="country"  readonly="true" styleClass="varysizeareahighlightgrey"/>
                                            </td>
                                            <td class="style2">Imports CFS Devanning</td>
                                            <td class="style2">
                                                <input type="radio" id="warehouseY" name="importCfsDevanning" value="Y"/>Yes
                                                <input type="radio" id="warehouseN" name="importCfsDevanning" value="N" checked/>No
                                                </td>
                                                
                                        </table>	
                                    </td>
                                </tr>

                            </table></td>
                    </tr>		

                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr class="tableHeadingNew">
                                    Air Cargo Warehouse
                                </tr>
                                <tr>
                                    <td><table width="100%" border="0" cellspacing="0">
                                            <tr>	
                                                <td class="style2" valign="top" width="21%">General Air Cargo Wrhse</td>
                                                <td valign="top" width="31%"><html:text property="generalAirCargo"  maxlength="25" styleClass="varysizeareahighlightwhite" onkeyup="toUppercase(this)"/></td>
                                            <td class="style2" width="18%">Phone</td>
                                            <td><html:text property="acPhone"  maxlength="40"  size="11" />
                                                <html:text property="acExtension"  maxlength="4" size="4"/></td>
                                        </tr>
                                        <tr>
                                            <td class="style2"  valign="top">Address*</td>
                                            <td  valign="top"><html:textarea property="acAddress"  cols="25" styleClass="selectboxstyle" onkeyup="limitText(this.form.acAddress,this.form.countdown,25)"/></td>
                                            <td class="style2">Fax</td>
                                            <td><html:text property="acFax"  maxlength="40"  styleClass="varysizeareahighlightwhite"/></td>
                                        </tr>
                                        <tr>
                                            <td class="style2">City &nbsp;  </td>
                                            <td>
                                                <input name="acCity" id="acCity"/>
                                                <div id="acCity_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("acCity", "acCity_choices", "", "",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=WAREHOUSE&isDojo=false", "fillAcCountryAndState()", "");
                                                </script>
                                            </td>



                                            <td class="style2">Zip</td>
                                            <td><html:text property="acZip"  maxlength="10" styleClass="varysizeareahighlightwhite" onkeyup="zipcode1(this)" /></td>
                                        </tr>
                                        <tr>
                                            <td class="style2" >State</td>
                                            <td><html:text property="acState"  readonly="true" styleClass="varysizeareahighlightgrey"/></td>
                                            <td class="style2" >Country</td>
                                            <td ><html:text property="acCountry"  readonly="true" styleClass="varysizeareahighlightgrey"/></td>

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
        </html:form>
    </body>
</html>

