<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.Warehouse"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    DBUtil dbUtil = new DBUtil();
    String buttonValue = "load";

    Warehouse warehouse = new Warehouse();
    String managerName = "";
    String cityId = "";
    String countryId = "";
    String state = "";
    String accityId = "";
    String accountryId = "";
    String acstate = "";
    String phone = "";
    String extension = "";
    String fax = "";
    String acphone = "";
    String acExtension = "";
    String acfax = "";


    if (session.getAttribute("warehouse") != null) {
        warehouse = (Warehouse) session.getAttribute("warehouse");

        if (warehouse.getPhone() != null) {
            if (warehouse.getPhone().indexOf('(') == -1) {
                phone = dbUtil.appendstring(warehouse.getPhone());
            } else {
                phone = warehouse.getPhone();
            }
        }
        if (warehouse.getFax() != null) {
            if (warehouse.getFax().indexOf('(') == -1) {
                fax = dbUtil.appendstring(warehouse.getFax());
            } else {
                fax = warehouse.getFax();
            }
        }
        if (warehouse.getAcPhone() != null) {
            if (warehouse.getAcPhone().indexOf('(') == -1) {
                acphone = dbUtil.appendstring(warehouse.getAcPhone());
            } else {
                acphone = warehouse.getAcPhone();
            }
        }
        if (warehouse.getAcFax() != null) {
            if (warehouse.getAcFax().indexOf('(') == -1) {
                acfax = dbUtil.appendstring(warehouse.getAcFax());
            } else {
                acfax = warehouse.getAcFax();
            }
        }
        if (warehouse != null && warehouse.getCityCode() != null && warehouse.getCityCode().getCountryId() != null && warehouse.getCityCode().getCountryId().getCodedesc() != null) {
            countryId = warehouse.getCityCode().getCountryId().getCodedesc();
        }

        if (warehouse != null && warehouse.getCityCode() != null && warehouse.getCityCode().getStateId() != null && warehouse.getCityCode().getStateId().getCode() != null) {
            state = warehouse.getCityCode().getStateId().getCode();
        }
        if (warehouse.getCity() != null && warehouse.getCity() != "") {
            cityId = warehouse.getCity();
        }

        if (warehouse != null && warehouse.getAcCity() != null && warehouse.getAcCity().getCountryId() != null && warehouse.getAcCity().getCountryId().getCodedesc() != null) {
            accountryId = warehouse.getAcCity().getCountryId().getCodedesc();
        }

        if (warehouse != null && warehouse.getAcCity() != null && warehouse.getAcCity().getStateId() != null && warehouse.getAcCity().getStateId().getCode() != null) {
            acstate = warehouse.getAcCity().getStateId().getCode();
        }
        if (warehouse.getAirCity() != null && warehouse.getAirCity() != "") {
            accityId = warehouse.getAirCity();
        }
    }
    if (session.getAttribute("warehouse2") != null) {
        warehouse = (Warehouse) session.getAttribute("warehouse2");

        if (warehouse.getPhone() != null) {
            if (warehouse.getPhone().indexOf('(') == -1) {
                phone = dbUtil.appendstring(warehouse.getPhone());
            } else {
                phone = warehouse.getPhone();
            }
        }
        if (warehouse.getFax() != null) {
            if (warehouse.getFax().indexOf('(') == -1) {
                fax = dbUtil.appendstring(warehouse.getFax());
            } else {
                fax = warehouse.getFax();
            }
        }
        if (warehouse.getAcPhone() != null) {
            if (warehouse.getAcPhone().indexOf('(') == -1) {
                acphone = dbUtil.appendstring(warehouse.getAcPhone());
            } else {
                acphone = warehouse.getAcPhone();
            }
        }
        if (warehouse.getAcFax() != null) {
            if (warehouse.getAcFax().indexOf('(') == -1) {
                acfax = dbUtil.appendstring(warehouse.getAcFax());
            } else {
                acfax = warehouse.getAcFax();
            }
        }
        if (warehouse != null && warehouse.getManager() != null) {
            managerName = warehouse.getManager();
        }
        if (warehouse != null && warehouse.getCityCode() != null && warehouse.getCityCode().getCountryId() != null && warehouse.getCityCode().getCountryId().getCodedesc() != null) {
            countryId = warehouse.getCityCode().getCountryId().getCodedesc();
        }
        if (warehouse != null && warehouse.getCityCode() != null && warehouse.getCityCode().getUnLocationName() != null) {
            cityId = warehouse.getCityCode().getUnLocationName();
        }
        if (warehouse != null && warehouse.getCityCode() != null && warehouse.getCityCode().getStateId() != null && warehouse.getCityCode().getStateId().getCode() != null) {
            state = warehouse.getCityCode().getStateId().getCode();
        }
        if (warehouse.getCity() != null && warehouse.getCity() != "") {
            cityId = warehouse.getCity();
        }

        if (warehouse != null && warehouse.getAcCity() != null && warehouse.getAcCity().getCountryId() != null && warehouse.getAcCity().getCountryId().getCodedesc() != null) {
            accountryId = warehouse.getAcCity().getCountryId().getCodedesc();
        }
        if (warehouse != null && warehouse.getAcCity() != null && warehouse.getAcCity().getUnLocationName() != null) {
            accityId = warehouse.getAcCity().getUnLocationName();
        }
        if (warehouse != null && warehouse.getAcCity() != null && warehouse.getAcCity().getStateId() != null && warehouse.getAcCity().getStateId().getCode() != null) {
            acstate = warehouse.getAcCity().getStateId().getCode();
        }
        if (warehouse.getAirCity() != null && warehouse.getAirCity() != "") {
            accityId = warehouse.getAirCity();
        }
        session.setAttribute("warehouse", warehouse);
    }

    String msg = "";
    if (request.getAttribute("message") != null) {
        msg = (String) request.getAttribute("message");

    }
    String modify = null;
    if (session.getAttribute("modifyforwarehouse") != null) {
        modify = (String) session.getAttribute("modifyforwarehouse");

    }
    if (request.getParameter("message") != null) {
        msg = (String) request.getParameter("message");
    }
// Name:Rohith Date:12/10/2007 (mm/dd/yy)  ----> Is view only when page is locked

    if (session.getAttribute("view") != null) {
        modify = (String) session.getAttribute("view");
    }

%>

<html>
    <head>
        <title>SearchMenu/Action</title>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
<script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/script-aculo-us.tabs.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/isValidEmail.js"></script>
        <script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
        <script language="javascript">
            start = function() {
                serializeForm();
            }
            window.onload = start;
        </script>
        <script language="javascript" type="text/javascript">

            function save1()
            {
                var wrhsname = document.editWarehouse.warehouseName;
                if (wrhsname.value == "")
                {
                    alert("Please enter the Warehouse Name ");
                    wrhsname.focus();
                    return;
                }

                if (document.editWarehouse.country.value == "0")
                {
                    alert("Please enter the country");
                    document.editWarehouse.country.value = "";
                    document.editWarehouse.country.focus();
                    return;
                }
                if (document.editWarehouse.city.value == "0")
                {
                    alert("Please enter the city");
                    document.editWarehouse.city.value = "";
                    document.editWarehouse.city.focus();
                    return;
                }
                if (document.editWarehouse.address.value != "" && document.editWarehouse.address.value.length > 25)
                {
                    alert("Address number should be 25 letters");
                    return;
                }

                if (document.editWarehouse.acAddress.value != "" && document.editWarehouse.acAddress.value.length > 25)
                {
                    alert("ACAddress number should be 25 letters");
                    return;
                }
                if (document.editWarehouse.phone.value != "")
                {
                    if (document.editWarehouse.phone.value < 13)
                    {
                        alert("phone number should be 13 digits");
                        return;
                    }
                }
                if (document.editWarehouse.fax.value != "")
                {
                    if (document.editWarehouse.fax.value < 13)
                    {
                        alert("fax number should be 13 digits");
                        return;
                    }
                }
                if (document.editWarehouse.acPhone.value != "")
                {
                    if (document.editWarehouse.acPhone.value < 13)
                    {
                        alert("acPhone number should be 13 digits");
                        return;
                    }
                }
                if (document.editWarehouse.acFax.value != "")
                {
                    if (document.editWarehouse.acFax.value < 13)
                    {
                        alert("acFax number should be 13 digits");
                        return;
                    }
                }
                if (IsNumeric(document.editWarehouse.zip.value.replace(/ /g, '')) == false)
                {
                    alert("Zip Code should be Numeric.");
                    document.editWarehouse.zip.value = "";
                    document.editWarehouse.zip.focus();
                    return;
                }
                if (document.editWarehouse.zip.value != "")
                {
                    if (document.editWarehouse.zip.value.length < 5)
                    {
                        if (document.editWarehouse.zip.value.length != 5)
                        {
                            alert("zip must be 5 digits");

                            document.editWarehouse.zip.focus();
                            return;
                        }
                    }
                    else if (document.editWarehouse.zip.value.length > 5)
                    {
                        if (document.editWarehouse.zip.value.length > 10)
                        {
                            alert("zip must be 9 digits");

                            document.editWarehouse.zip.focus();
                            return;
                        }
                    }
                }
                if (IsNumeric(document.editWarehouse.phone.value.replace(/ /g, '')) == false)
                {
                    alert("Phone Number should be Numeric.");
                    document.editWarehouse.phone.value = "";
                    document.editWarehouse.phone.focus();
                    return;
                }

                if (IsNumeric(document.editWarehouse.fax.value.replace(/ /g, '')) == false)
                {
                    alert("Fax Number should be Numeric.");
                    document.editWarehouse.fax.value = "";
                    document.editWarehouse.fax.focus();
                    return;
                }
                if (IsNumeric(document.editWarehouse.acZip.value.replace(/ /g, '')) == false)
                {
                    alert("Air Cargo Zip Code should be Numeric.");
                    document.editWarehouse.acZip.value = "";
                    document.editWarehouse.acZip.focus();
                    return;
                }
                if (document.editWarehouse.acZip.value != "")
                {
                    if (document.editWarehouse.acZip.value.length < 5)
                    {
                        if (document.editWarehouse.acZip.value.length != 5)
                        {
                            alert("zip must be 5 digits");

                            document.editWarehouse.acZip.focus();
                            return;
                        }
                    }
                    else if (document.editWarehouse.acZip.value.length > 5)
                    {
                        if (document.editWarehouse.acZip.value.length > 10)
                        {
                            alert("zip must be 9 digits");

                            document.editWarehouse.acZip.focus();
                            return;
                        }
                    }
                }
                if (IsNumeric(document.editWarehouse.acPhone.value.replace(/ /g, '')) == false)
                {
                    alert("Air Cargo Phone Number should be Numeric.");
                    document.editWarehouse.acPhone.value = "";
                    document.editWarehouse.acPhone.focus();
                    return;
                }
                if (IsNumeric(document.editWarehouse.acFax.value.replace(/ /g, '')) == false)
                {
                    alert("Air Cargo Fax Number should be Numeric.");
                    document.editWarehouse.acFax.value = "";
                    document.editWarehouse.acFax.focus();
                    return;
                }
                document.editWarehouse.buttonValue.value = "save";
                document.editWarehouse.submit();
            }
            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function confirmdelete()
            {
                document.editWarehouse.buttonValue.value = "delete";
                var result = confirm("Are you sure you want to delete this Warehouse");
                if (result)
                {
                    document.editWarehouse.submit();
                }
            }
            function confirmnote()
            {
                document.editWarehouse.buttonValue.value = "note";
                document.editWarehouse.submit();
            }
            function cancelbtn(val) {
                if (val == 0 || val == 3) {
                    document.editWarehouse.buttonValue.value = "cancelview";
                    document.editWarehouse.submit();
                } else {
                    document.editWarehouse.buttonValue.value = "cancel";
                    var result = confirm("Would you like to save the changes?");
                    if (result) {
                        save1();
                    } else {
                        document.editWarehouse.submit();
                    }
                }
            }
            function disabled(val1, val2)
            {

                if (val1 == 0 || val1 == 3)
                {
                    var imgs = document.getElementsByTagName('img');
                    for (var k = 0; k < imgs.length; k++)
                    {
                        if (imgs[k].id != "cancel" && imgs[k].id != "note")
                        {
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                    var input = document.getElementsByTagName("input");
                    for (i = 0; i < input.length; i++)
                    {
                        if (input[i].id != "buttonValue")
                        {
                            input[i].readOnly = true;
                            input[i].style.color = "blue";

                        }

                    }
                    var textarea = document.getElementsByTagName("textarea");
                    for (i = 0; i < textarea.length; i++)
                    {
                        textarea[i].readOnly = true;

                        textarea[i].style.color = "blue";

                    }
                    var select = document.getElementsByTagName("select");

                    for (i = 0; i < select.length; i++)
                    {
                        select[i].disabled = true;
                        select[i].style.backgroundColor = "blue";
                        //select[i].disabled = true;
                    }
                    document.getElementById("save").style.visibility = 'hidden';
                    document.getElementById("delete").style.visibility = 'hidden';
                }
                if (val1 == 1)
                {
                    document.getElementById("delete").style.visibility = 'hidden';
                }
                if (val1 == 3 && val2 != "")
                {
                    alert(val2);
                }
                //document.editWarehouse.warehouseCode.color="Blue";

                if (document.editWarehouse.city.value == "<%=cityId%>")
                {
                    if (document.editWarehouse.city.value == "")
                    {
                        document.editWarehouse.state.value = "";
                        document.editWarehouse.country.value = "";
                    }
                }
                if (document.editWarehouse.acCity.value == "<%=accityId%>")
                {
                    if (document.editWarehouse.acCity.value == "")
                    {
                        document.editWarehouse.acState.value = "";
                        document.editWarehouse.acCountry.value = "";
                    }
                }
            }

            //to clear the city fields
            function clearDefault(el) {
//alert("from the function");
                if (el.value == "")
                {
                    document.editWarehouse.state.value = "";
                    document.editWarehouse.country.value = "";
                }
            }
            function defaultclear(el)
            {
                if (el.value == "")
                {
                    document.editWarehouse.acState.value = "";
                    document.editWarehouse.acCountry.value = "";
                }
            }

            function popup1(mylink, windowname)
            {
                if (!window.focus)
                    return true;
                var href;
                if (typeof(mylink) == 'string')
                    href = mylink;
                else
                    href = mylink.href;
                var w = 500;
                var h = 400;
                LeftPosition = (screen.width) ? (screen.width - w) / 2 : 0;
                TopPosition = (screen.height) ? (screen.height - h) / 2 : 0;
                settings =
                        'height=' + h + ',width=' + w + ',top=' + TopPosition + ',left=' + LeftPosition + ',scrollbars=yes,resizable';
                mywindow = window.open(href, windowname, settings);
                mywindow.moveTo(200, 180);

                document.editWarehouse.submit();
                return false;
            }
            function limitText(limitField, limitCount, limitNum) {
                limitField.value = limitField.value.toUpperCase();
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }

            }
            function searchcity() {
                document.editWarehouse.buttonValue.value = "searchcity";
                document.editWarehouse.submit();
            }
            function searchaccity() {


                document.editWarehouse.buttonValue.value = "searchaccity";
                document.editWarehouse.submit();
            }
            function zipcode(obj)
            {
                if (document.editWarehouse.country.value == "" && document.editWarehouse.country.value != "UNITED STATES")
                {


                    if ((document.editWarehouse.zip.value.length > 5) || IsNumeric(document.editWarehouse.zip.value.replace(/ /g, '')) == false)
                    {
                        alert("please enter the only 6 digits and numerics only");
                        document.editWarehouse.zip.value = "";
                        document.editWarehouse.zip.focus();
                    }//document.editWarehouse.phone.value.length=6;
                    // document.editWarehouse.zip.value.length=6;
                }
                else {
                    getzip(obj);

                }
            }
// to check zipcode of accountry
            function zipcode1(obj)
            {
                if (document.editWarehouse.acCountry.value == "" && document.editWarehouse.acCountry.value != "UNITED STATES")
                {

                    if ((document.editWarehouse.acZip.value.length > 5) || IsNumeric(document.editWarehouse.acZip.value.replace(/ /g, '')) == false)
                    {
                        alert("please enter the only 6 digits and numerics only");
                        document.editWarehouse.acZip.value = "";
                        document.editWarehouse.acZip.focus();
                    }//document.editWarehouse.phone.value.length=6;
                }
                else {
                    getzip(obj);

                }
            }
            function fillCountryAndState() {
                var array = new Array();
                var city = document.editWarehouse.city.value;
                array = city.split('/');
                document.editWarehouse.city.value = array[0];
                document.editWarehouse.country.value = array[1];
                if (undefined != array[2] && null != array[2]) {
                    document.editWarehouse.state.value = array[2];
                }
            }
            function fillAcCountryAndState() {
                var array = new Array();
                var city = document.editWarehouse.acCity.value;
                array = city.split('/');
                document.editWarehouse.acCity.value = array[0];
                document.editWarehouse.acCountry.value = array[1];
                if (undefined != array[2] && null != array[2]) {
                    document.editWarehouse.acState.value = array[2];
                }
            }
// AJAX Code
            function stateChanged()
            {
                if (xmlHttp.readyState == 4)
                {
                    document.getElementById("txtHint").innerHTML = xmlHttp.responseText;
                }
            }
    function checkDisable() {
        var ipiVendor = document.getElementById('ipiVendor').value;
        var ipiVendorNo=ipiVendor.substring(0, ipiVendor.indexOf(":-"));
        jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: ipiVendorNo
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alert(data);
                document.getElementById('ipiVendor').value = "";
            } else {
                document.getElementById('ipiVendor').value = document.getElementById('ipiVendor').value;
            }
        }
    });
}
            
        </script>
    </head>
    <body class="whitebackgrnd" onkeydown="preventBack()">

        <html:form action="/editWarehouse" name="editWarehouse" type="com.gp.cong.logisoft.struts.form.EditWarehouseForm" scope="request">
            <div align="right">

                <input type="button" class="buttonStyleNew" value="Save" name="save" onclick="save1()" /> 
                <input type="button" class="buttonStyleNew" value="Go Back"  name="save" onclick="cancelbtn(<%=modify%>)" /> 
                <input type="button" class="buttonStyleNew" value="Delete" name="delete" onclick="confirmdelete()" /> 
                <input type="button" class="buttonStyleNew" value="Note"  name="note" onclick="confirmnote()" disabled="true"/> 


            </div>
            <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    Warehouse Details 
                </tr>


                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">

                            <tr>
                                <td>
                                    <table width="100%" border="0" cellspacing="0">
                                        <tr >
                                            <td  class="style2" >Warehouse Code</td>
                                            <td><html:text property="warehouseCode" value="<%=warehouse.getWarehouseNo()%>" size="3" readonly="true" styleClass="varysizeareahighlightgrey" maxlength="3" onkeyup="toUppercase(this)" ></html:text></td>
                                            </tr>
                                            <tr>
                                                <td  class="style2" >Warehouse Type</td>
                                                <td>
                                                <html:select property="warehouseType" styleClass="selectboxstyle"  style="width:118px"  value="<%=warehouse.getWarehouseType()%>">
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
                                            <td ><html:text property="warehouseName" value="<%=warehouse.getWarehouseName()%>" maxlength="30" styleClass="areahighlightyellow1" onkeyup="toUppercase(this)"/></td>
                                            <td class="style2" width="18%">Phone</td>
                                            <td >
                                                <html:text property="phone" value="<%=phone%>" maxlength="10"  size="11"  styleClass="warehousewarehouseareahighlightwhite"/>
                                                <html:text property="extension" value="<%=warehouse.getExtension()%>" size="4"/>
                                            </td>


                                        </tr>
                                        <tr>
                                          <td class="style2" width="21%">CFS Devanning Email</td>
                                          <td><html:text property="cfsDevanningEmail" value="<%=warehouse.getCfsDevanningEmail()%>" maxlength="50" styleClass="warehousewarehouseareahighlightwhite"/></td>  
                                        </tr>
                                        <tr>
                                            <td><table width="100%" border="0">
                                                    <tr class="style2"><td>Manager Name*&nbsp;</td></tr></table></td>
                                            <td>
                                                <html:text property="managerName" styleId="managerName" maxlength="25" value="<%=managerName%>"/>
                                            </td>

                                            <td class="style2" >Fax</td>
                                            <td ><html:text property="fax" value="<%=fax%>" maxlength="10"  styleClass="warehousewarehouseareahighlightwhite"/></td>
                                        </tr>
                                        <tr>
                                            <td class="style2" valign="top">Address*</td>
                                            <td ><html:textarea property="address" value="<%=warehouse.getAddress()%>" cols="25" styleClass="selectboxstyle" onkeyup="limitText(this.form.address,this.form.countdown,25)"></html:textarea></td>
                                                <td class="style2">IPI Vendor # &nbsp; </td>
                                                <td>
                                                    <input  name="ipiVendor" id="ipiVendor" value='<%=null != warehouse.getVendorNo() ? warehouse.getVendorNo() : ""%>'/>
                                                <div id="ipiVendor_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("ipiVendor", "ipiVendor_choices", "", "",
                                                            "${path}/actions/getCustomer.jsp?tabName=WAREHOUSE&from=0&isDojo=false", "checkDisable()");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="style2">City &nbsp; </td>
                                            <td >

                                                <input name="city" id="city" value="<%=cityId%>" />
                                                <div id="city_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("city", "city_choices", "", "",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=WAREHOUSE&isDojo=false", "fillCountryAndState()", "");
                                                </script>
                                            </td>
                                            <td class="style2">IPI Commodity # &nbsp;</td>
                                            <td>
                                                <input name="ipiCommodity" id="ipiCommodity" value='<%=null != warehouse.getCommodityNo() ? warehouse.getCommodityNo() : ""%>'  />
                                                <div id="commcode_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("ipiCommodity", "commcode_choices", "", "",
                                                            "${path}/actions/getChargeCode.jsp?tabName=WAREHOUSE&isDojo=false", "");
                                                </script>
                                            </td>

                                        </tr>
                                        <tr>
                                            <td class="style2">State</td>
                                            <td><html:text property="state" value="<%=state%>" readonly="true" styleClass="varysizeareahighlightgrey"/></td>
                                            <td class="style2">Zip</td>
                                            <td ><html:text property="zip" value="<%=warehouse.getZipCode()%>" maxlength="10" styleClass="warehousewarehouseareahighlightwhite" onkeypress="zipcode(this)"/></td>

                                        </tr>
                                        <tr>
                                            <td class="style2">Country</td>
                                            <td><html:text property="country" value="<%=countryId%>"  readonly="true" styleClass="varysizeareahighlightgrey"/></td>
                                            <td class="style2">Imports CFS Devanning</td>
                                            <td class="style2">
                                                <input type="radio" id="warehouseY" name="importCfsDevanning" value="Y" <c:if test="${warehouse.importsCFSDevanning eq 'Y'}">checked</c:if>/>Yes
                                                <input type="radio" id="warehouseN" name="importCfsDevanning" value="N" <c:if test="${warehouse.importsCFSDevanning eq 'N'}">checked</c:if>/>No
                                                </td>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>		



                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing="0" height="100%">
                                <tr class="tableHeadingNew">
                                    Air Cargo Warehouse
                                </tr>

                                <tr>
                                    <td><table width="100%" border="0" cellspacing="0" height="100%">
                                            <tr>	
                                                <td class="style2"  valign="top" width="21%">General Air Cargo Wrhse</td>
                                                <td ><html:text property="generalAirCargo"  value="<%=warehouse.getAcWarehouseName()%>"  maxlength ="25" styleClass="warehousewarehouseareahighlightwhite" onkeyup="toUppercase(this)"/></td>
                                            <td class="style2" width="18%">Phone</td>
                                            <td><html:text property="acPhone" value="<%=acphone%>" maxlength="13" size="11" onkeypress="phonevalid1(this)" styleClass="warehousewarehouseareahighlightwhite"/>
                                                <html:text property="acExtension" value="<%=warehouse.getAcExtension()%>" maxlength="4" size="4"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="style2"  valign="top">Address*</td>
                                            <td  valign="top"><html:textarea property="acAddress" value="<%=warehouse.getAcAddress()%>" cols="25" styleClass="selectboxstyle" onkeyup="limitText(this.form.address,this.form.countdown,25)"/></td>
                                            <td class="style2" >Fax</td>
                                            <td ><html:text property="acFax" value="<%=acfax%>" maxlength="13" onkeypress="faxvalid1(this)" styleClass="warehousewarehouseareahighlightwhite"/></td>
                                        </tr>
                                        <tr>
                                            <td class="style2">City &nbsp;  </td>
                                            <td >

                                                <input name="acCity" id="acCity" value="<%=accityId%>" />
                                                <div id="acCity_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("acCity", "acCity_choices", "", "",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=WAREHOUSE&isDojo=false", "fillAcCountryAndState()", "");
                                                </script>
                                            </td>
                                            <td class="style2" >Zip</td>
                                            <td ><html:text property="acZip" value="<%=warehouse.getAcZipCode()%>" maxlength="10" onkeypress="zipcode1(this)"/></td>


                                        </tr>
                                        <tr>
                                            <td class="style2">State</td>
                                            <td><html:text property="acState" value="<%=acstate%>" readonly="true" styleClass="varysizeareahighlightgrey"/></td>
                                            <td class="style2">Country</td>
                                            <td><html:text property="acCountry" value="<%=accountryId%>" readonly="true" styleClass="varysizeareahighlightgrey"/>
                                            <td></td>

                                        </tr>
                                        <tr>
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
            <script>disabled('<%=modify%>', '<%=msg%>')</script>
        </html:form>

    </body>
</html>

