<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="unitTypeDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO"/>
<jsp:useBean id="lclSSMasterBlDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO"/>
<%@include file="init.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@taglib uri="/WEB-INF/tlds/date.tld" prefix="date"%>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%@include file="/taglib.jsp" %>
<jsp:useBean id="lclAddUnitsForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclAddUnitsForm"/>
<%    request.setAttribute("unitTypeList", unitTypeDAO.getAllUnittypesForDisplay("1", "0"));
            lclAddUnitsForm.setLocation("");
            lclAddUnitsForm.setDisposition("");
            lclAddUnitsForm.setWarehouseName("");
            lclAddUnitsForm.setDepartedDateTime("");
            lclAddUnitsForm.setArrivedDateTime("");
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add LCL Units</title>
    </head>
    <body>
        <cong:form action="/lclImportAddUnits" name="lclAddUnitsForm" id="lclAddUnitsForm">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="dupCfsId" name="dupCfsId" value="${lclAddUnitsForm.dupCfsId}"/>
            <cong:hidden id="dupUnitTypeId" name="dupUnitTypeId" value="${lclAddUnitsForm.dupUnitTypeId}"/>
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden name="unitId" id="unitId" />
            <cong:hidden id="headerId" name="headerId" />
            <cong:hidden name="unitWarehouseId" id="unitWarehouseId" />
            <cong:hidden name="warehouseId" id="warehouseId" />
            <cong:hidden name="unitssId" id="unitssId"/>
            <cong:hidden name="unitDispoId" id="unitDispoId" />
            <cong:hidden name="location" id="location" />
            <cong:hidden name="oldUnitNo" id="oldUnitNo" value="${lclAddUnitsForm.unitNo}"/>
            <cong:hidden id="unitsReopened" name="unitsReopened" value="${unitsReopened}"/>
            <cong:hidden id="buttonValue" name="buttonValue"  />
            <cong:hidden id="unmanifestLCLUnit" name="unmanifestLCLUnit"  value="${lclAddUnitsForm.unmanifestLCLUnit}"/>
            <cong:hidden id="unitStatus" name="unitStatus" value="${lclAddUnitsForm.unitStatus}" />
            <input type="hidden" id="etaPodDate" name="etaPodDate" value="${lclAddUnitsForm.etaPodDate}" />
            <input type="hidden" id="unitNoFlag" name="unitNoFlag" value="${unitNoFlag}" />
            <input type="hidden" id="closedBy" name="closedBy" value="${closedBy}" />
            <input type="hidden" id="loadForm"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew"><td width="94%">
                        ${not empty lclAddUnitsForm.unitNo ? "Edit" :"Add"} Unit#
                        <span class="fileNo">${lclAddUnitsForm.unitNo}</span>
                    </td>
                    <td width="6%">
                        <div class="button-style1" onclick="saveUnits();">Save</div></td>
                </tr>
            </table>
            <table border="0" width="98.5%"  style="border:1px solid #dcdcdc">
                <tr><td colspan="9"></td></tr>
                <tr><td colspan="9"></td></tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" width="5%">Unit#</cong:td>
                    <cong:td width="2%" colspan="2">
                        <cong:text name="unitNo" id="unitNo" container="NULL"
                                   onchange="unitNumberValidate(this);unitNumberExists(this);"
                                   styleClass="mandatory textuppercaseLetter" />
                        <input type="checkbox" name="allowfreetext" id="allowfreetext"
                               title="Checked=Allow any Unit# Format<br/>UnChecked=Allow only AAAA-NNNNNN-N Unit# Format"/>
                        <input type="hidden" name="existUnitNo" id="existUnitNo" value="${lclAddUnitsForm.unitNo}"/>
                        <input type="hidden" name="existUnitId" id="existUnitId" value="${lclAddUnitsForm.unitId}"/>
                        <input type="hidden" name="existUnitChecked" id="existUnitChecked"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Size</cong:td>
                    <cong:td>
                        <html:select property="unitType" styleId="unitType" onclick="disabledUnitSize('${unitNoFlag}')"
                                     styleClass="${unitNoFlag eq true ? 'textlabelsBoldForTextBoxDisabledLook' : 'smallDropDown textlabelsBoldforlcl mandatory'}"
                                     style="width:150px" onchange="changeUnitSize();">
                            <html:option value="">Select One</html:option>
                            <html:optionsCollection name="unitTypeList"/>
                        </html:select>
                        <input type="hidden" name="existUnitTypeId" id="existUnitTypeId" value="${lclAddUnitsForm.unitType}"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" width="10%">IT Date</cong:td>
                    <cong:td >
                        <cong:calendarNew styleClass="" id="itDatetime" name="itDatetime" onchange="dateCheck(this);"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Master BL</cong:td>
                    <cong:td> <cong:text name="masterBL" id="masterBL" styleClass="textuppercaseLetter mandatory"/>
                    </cong:td>

                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">IT Number</cong:td>
                    <cong:td colspan="2">
                        <cong:text name="itNo" id="itNo" styleClass="textuppercaseLetter" onkeyup="checkForNumberOnly(this);" maxlength="9"/>
                        <input type="checkbox" name="allowfreetextIT" id="allowfreetextIT" onclick="checkBoxMaxLengthForIt();"
                               title="Checked=Allow any Format<br/>UnChecked=Allow only Number Format"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">IT Port</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl">
                        <cong:autocompletor id="itPort" name="itPort" template="one" fields="NULL,NULL,NULL,itPortId" position="right"
                                            query="CONCAT_PORT_NAME"  width="350" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="itPortId" id="itPortId"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Stripped Date</cong:td>
                    <cong:td>
                        <cong:calendarNew id="strippedDate" name="strippedDate" onchange="dateCheck(this);strippedValidate()"/>
                        <input type="hidden" name="date" id="date" value="${date:formatDate(date:currentDate(),'dd-MMM-yyyy')}">
                        <input type="hidden" name="date" id="dateOne" value="${date:formatDate(date:currentDate(),'dd')}">
                        <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="search" onclick="fillLastFreeDate();"
                             title="Click here to fill in Last Free date,<br/> by adding 6 days to Strip Date"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Seal In</cong:td>
                    <cong:td>
                        <cong:text id="sealNoIn" name="sealNoIn" styleClass="textuppercaseLetter" />
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Hazardous</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl" colspan="2">
                        <cong:radio value="Y" name="hazmatPermitted" container="NULL" /> Yes
                        <cong:radio value="N" name="hazmatPermitted" container="NULL" /> No
                    </cong:td>

                    <cong:td styleClass="textlabelsBoldforlcl">Stop Off</cong:td>
                    <cong:td>
                        <cong:radio value="Y" name="stopoff" container="NULL" /> Yes
                        <cong:radio value="N" name="stopoff" container="NULL" /> No
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl"> Approx Due Date</cong:td>
                    <cong:td>
                        <cong:calendarNew  id="approxDueDate" name="approxDueDate" onchange="dateCheck(this);"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Seal Out</cong:td>
                    <cong:td>
                        <cong:text id="sealNoOut" name="sealNoOut" styleClass="textuppercaseLetter"/>
                    </cong:td>
                </cong:tr>

                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Drayage Provided</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl" colspan="2">
                        <cong:radio value="Y" name="drayageProvided" container="NULL" /> Yes
                        <cong:radio value="N" name="drayageProvided" container="NULL" /> No
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">InterModal Provided</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl">
                        <cong:radio value="Y" name="intermodalProvided" container="NULL" /> Yes
                        <cong:radio value="N" name="intermodalProvided" container="NULL" /> No
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Last Free Date</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl">
                        <cong:calendarNew styleClass="" id="lastFreeDate" name="lastFreeDate" onchange="dateCheck(this);"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Stripped By</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl">
                        <cong:autocompletor id="strippedByUser"  container="null"  name="strippedByUser" query="USER_STATUS" fields="strippedByUserId" shouldMatch="true"
                                            width="200" scrollHeight="200"  template="one" position="left" styleClass="textuppercaseLetter"/>
                        <input type="hidden" id="strippedByUserId" name="strippedByUserId" value="${lclAddUnitsForm.strippedByUserId}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Coloader</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl" colspan="2">
                        <cong:autocompletor name="coloaderAcct" id="coloaderAcct" width="600" scrollHeight="300" container="null"
                                            query="TRADING_PARTNER_IMPORTS"  fields="coloaderAcctNo,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fmcNumber,otiNumber,NULL,NULL,retailCommodity" template="tradingPartner"
                                            paramFields="coloaderSearchState,coloaderSearchZip,coloaderSearchCity" shouldMatch="true"/>
                        <cong:text name="coloaderAcctNo" id="coloaderAcctNo" readOnly="true" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" style="width:70px;"/>
                        <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" width="16" height="16" style="cursor:pointer;vertical-align:middle;"
                             onclick="showClientSearchOption('${path}', 'Coloader');" title="Click here to see Coloader Search Filter Options"/>
                        <input type="hidden" name="coloaderSearchCity" id="coloaderSearchCity"/>
                        <input type="hidden" name="coloaderSearchState" id="coloaderSearchState"/>
                        <input type="hidden" name="coloaderSearchZip" id="coloaderSearchZip" value=""/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Origin Agent</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl">
                        <cong:autocompletor name="originAcct" id="originAcct" width="400" scrollHeight="200px" container="null" params="${lclAddUnitsForm.polUnlocationCode}"
                                            query="IMPORTORIGINAGENT" fields="originAcctNo" template="two" shouldMatch="true" styleClass="mandatory"/>
                        <cong:text name="originAcctNo" id="originAcctNo"  readOnly="true"  styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" style="width:70px;"/>
                    </cong:td>

                    <cong:td styleClass="textlabelsBoldforlcl">SU Heading Note
                    </cong:td>
                    <cong:td>
                        <cong:text id="SUHeadingNote"  container="null"  name="SUHeadingNote" styleClass="textlabelsBoldleftforlcl textuppercaseLetter" maxlength="30" style="width :200px"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Loaded By</cong:td>
                    <cong:td>
                        <cong:autocompletor id="loadedByUser"  container="null"  name="loadedByUser" query="USER_STATUS" fields="loadedByUserId" shouldMatch="true"
                                            template="one" width="200" scrollHeight="200" position="left" styleClass="textuppercaseLetter"/>
                        <input type="hidden" id="loadedByUserId" name="loadedByUserId" value="${lclAddUnitsForm.loadedByUserId}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">CFS(Devanning)</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl" colspan="2">
                        <cong:autocompletor name="cfsWarehouse" id="cfsWarehouse" width="500" scrollHeight="200" container="null" styleClass="textlabelsBoldForTextBoxWidth mandatory"
                                            query="CFS_WAREHOUSE" fields="NULL,NULL,cfswarehsAddress,city,state,zipCode,phone,fax,cfsWarehouseId" template="delwhse" shouldMatch="true" callback="cfsAddress()"/>
                        <input type="hidden" name="city" id="city" value=""/>
                        <input type="hidden" name="state" id="state" value=""/>
                        <input type="hidden" name="zipCode" id="zipCode" value=""/>
                        <input type="hidden" name="phone" id="phone" value=""/>
                        <input type="hidden" name="fax" id="fax" value=""/>
                        <cong:hidden name="cfsWarehouseId" id="cfsWarehouseId"/><br/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Unit Terminal</cong:td>
                    <cong:td >
                        <cong:autocompletor name="unitWarehouse" id="unitWarehouse" width="500"  scrollHeight="200" container="null"
                                            styleClass="textlabelsBoldForTextBoxWidth" query="IMPORTWAREHOUSE" fields="NULL,NULL,unitwarehsAddress,NULL,NULL,NULL,NULL,NULL,unitsWarehouseId" template="delwhse" shouldMatch="true"/>
                        <cong:hidden name="unitsWarehouseId" id="unitsWarehouseId"/><br/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" rowspan="2">Trucking Information</cong:td>
                    <cong:td rowspan="2">
                        <html:textarea cols="7" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox" property="remarks"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Door</cong:td>
                    <cong:td>
                        <cong:text id="doorNumber" name="doorNumber" styleClass="textuppercaseLetter" maxlength="100"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl"></cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl" colspan="2">
                        <cong:textarea rows="4" cols="32" readOnly="true" style="resize:none"  styleClass="textlabelsBoldForTextBoxDisabledLook" name="cfswarehsAddress" id="cfswarehsAddress" value=""/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl"></cong:td>
                    <cong:td >
                        <cong:textarea rows="4" cols="32" readOnly="true" style="resize:none"  styleClass="textlabelsBoldForTextBoxDisabledLook" name="unitwarehsAddress" id="unitwarehsAddress" value=""/>
                    </cong:td>
                    <td></td>
                    <td></td>
                </cong:tr>
                <tr>
                    <td class="textlabelsBoldforlcl">Coloader Devanning Warehouse</td>
                    <td>
                        <cong:autocompletor name="coloaderDevngAcctName" id="coloaderDevngAcct" width="600" scrollHeight="100" container="null" styleClass="textlabelsBoldForTextBoxWidth"
                                            query="TRADING_PARTNER_IMPORTS"  fields="coloaderDevngAcctNo,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fmcNumber,otiNumber,NULL,NULL,retailCommodity"
                                            template="tradingPartner" shouldMatch="true"  paramFields="coloaderDevngSearchState,coloaderDevngSearchZip,coloaderDevngSearchCity"/>
                        <cong:text name="coloaderDevngAcctNo" id="coloaderDevngAcctNo" readOnly="true"
                                   styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" style="width:70px"/>
                        <span style="cursor:pointer">
                            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" width="16" height="16"
                                 onclick="showClientSearchOption('${path}', 'Coloader Devanning Warehouse');" title="Click here to see <br/> Coloader Devanning Warehouse Filter Options"/>
                        </span>
                        <input type="hidden" name="coloaderDevngSearchState" id="coloaderDevngSearchState"/>
                        <input type="hidden" name="coloaderDevngSearchZip" id="coloaderDevngSearchZip"/>
                        <input type="hidden" name="coloaderDevngSearchCity" id="coloaderDevngSearchCity"/>
                    </td>
                    <td align="right" colspan="2" class="textlabelsBoldforlcl">Prepaid/Collect</td>
                    <td>
                        <html:select styleId="prepaidCollect" value="${lclAddUnitsForm.prepaidCollect}"
                                     property="prepaidCollect" style="width:134px" styleClass="smallDropDown textlabelsBoldforlcl">
                            <html:option value="P">Prepaid</html:option>
                            <html:option value="C">Collect</html:option>
                        </html:select>
                    </td>
                    <td colspan="4"></td>
                </tr>
            </table>
            <c:if test="${not empty lclAddUnitsForm.unitId && lclAddVoyageForm.unitId!=0}">
                <cong:table border="0" width="100%">
                    <cong:tr>
                        <cong:td width="50%">
                            <cong:table align="center" id="noteTable" cellpadding="0" cellspacing="0" width="99%" border="0" style="border:1px solid #dcdcdc">
                                <cong:tr styleClass="tableHeadingNew">
                                    <cong:td width="100%">WAREHOUSE</cong:td>
                                </cong:tr>
                                <cong:tr><cong:td>
                                        <c:if test="${not empty lclUnitWhse}">
                                            <display:table name="${lclUnitWhse}" class="dataTable" pagesize="20"  id="lclWarehouse" sort="list" requestURI="/lclImportAddUnits.do">
                                                <display:setProperty name="paging.banner.some_items_found">
                                                    <span class="pagebanner"><font color="blue">{0}</font>LCL Schedule displayed,For more Records click on page numbers.</span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.one_item_found">
                                                    <span class="pagebanner">One {0} displayed. Page Number</span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.all_items_found">
                                                    <span class="pagebanner">{0} {1} Displayed, Page Number</span>
                                                </display:setProperty>
                                                <display:setProperty name="basic.msg.empty_list">
                                                    <span class="pagebanner">No Records Found.</span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.placement" value="bottom"/>
                                                <display:setProperty name="paging.banner.item_name" value="Warehouse"/>
                                                <display:setProperty name="paging.banner.items_name" value="Warehouses"/>
                                                <display:column title="Warehouse Name"  headerClass="sortable">
                                                    ${lclWarehouse.warehouse.warehouseName}
                                                    <span  style="color:#0000FF;font-weight: bold">
                                                        <c:if test="${not empty lclWarehouse.warehouse.warehouseNo}">
                                                            (${lclWarehouse.warehouse.warehouseNo})
                                                        </c:if>
                                                    </span>
                                                    <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="modifiedDatetime" value="${lclWarehouse.modifiedDatetime}"></fmt:formatDate>
                                                    <span style="color:green;font-weight: bold">&nbsp;&nbsp;&nbsp;(${modifiedDatetime})</span>
                                                </display:column>
                                                <fmt:formatDate pattern="dd-MMM-yyyy" var="stripDate" value="${lclWarehouse.destuffedDatetime}"></fmt:formatDate>
                                                <display:column title="Strip Date" headerClass="sortable">${stripDate}</display:column>
                                                <display:column title="Seal In" property="sealNoIn" headerClass="sortable" />
                                                <display:column title="Seal Out" property="sealNoOut" headerClass="sortable" />
                                                <display:column title="Action">
                                                    <span style="cursor:pointer">
                                                        <img src="${path}/images/edit.png" width="16" height="16"
                                                             onclick="editWarehouse('${path}', '${lclWarehouse.id}', '${lclWarehouse.warehouse.warehouseNo}', '${lclWarehouse.lclUnit.unitNo}')" title="Edit Warehouse"/>
                                                    </span>
                                                </display:column>
                                            </display:table>
                                        </c:if>
                                    </cong:td></cong:tr>
                            </cong:table>
                        </cong:td>
                        <cong:td width="50%">
                            <cong:table align="center" id="noteTable" cellpadding="0" cellspacing="0" width="99%" border="0" style="border:1px solid #dcdcdc">
                                <cong:tr styleClass="tableHeadingNew">
                                    <cong:td width="100%">
                                        <div onclick="toggle('#disp-table');" style="cursor: pointer;">
                                            DISPOSITION
                                        </div>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">
                                        <span class="red" style="cursor: pointer; right: 10px; width: 900px; text-align:right;" onclick="toggle('#disp-table');">
                                            Click this bar to Add Disposition&nbsp;
                                        </span>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td colspan="2">
                                        <cong:table id="disp-table" width="97%" border="0" style="border:1px solid #dcdcdc; display:none;">
                                            <cong:tr>
                                                <cong:td styleClass="textlabelsBoldforlcl" width="15%">
                                                    Disposition
                                                </cong:td>
                                                <cong:td styleClass="textlabelsBoldleftforlcl">
                                                    <cong:autocompletor name="disposition" id="disposition" width="350" scrollHeight="200px" styleClass="mandatory textuppercaseLetter"
                                                                        query="DISPOSITION" fields="NULL,dispositionId" template="two" shouldMatch="true" container="NULL"
                                                                        callback="validateDispo();"/>
                                                    <cong:hidden name="dispositionId" id="dispositionId" />
                                                </cong:td>
                                                <%--  <cong:td width="5%" styleClass="textlabelsBoldforlcl">Disposition DateTime</cong:td>--%>
                                                <cong:td>
                                                    <input type="button" class="button-style1" value="Save" id="saveCode" onclick="addNewDisposition('${path}');"/>
                                                    <cong:hidden styleClass="mandatory textWidth" id="dispositionDateTime" name="dispositionDateTime"/>
                                                </cong:td>
                                            </cong:tr>
                                            <%--<cong:tr>
                                                <cong:td colspan="2"></cong:td>
                                                <cong:td colspan="2">
                                                    <input type="button" class="button-style1" value="Save" id="saveCode" onclick="addNewDisposition('${path}');"/>
                                                </cong:td>
                                            </cong:tr>--%>
                                        </cong:table>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td colspan="2">
                                        <c:if test="${not empty lclDispositionList}">
                                            <c:set var="reversePostIcon" value="true"/>
                                            <display:table name="${lclDispositionList}" class="dataTable" pagesize="20"  id="lclDisposition" sort="list" requestURI="/lclAddUnits.do">
                                                <display:setProperty name="paging.banner.some_items_found">
                                                    <span class="pagebanner"><font color="blue">{0}</font>LCL Schedule displayed,For more Records click on page numbers.</span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.one_item_found">
                                                    <span class="pagebanner">One {0} displayed. Page Number</span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.all_items_found">
                                                    <span class="pagebanner">{0} {1} Displayed, Page Number</span>
                                                </display:setProperty>
                                                <display:setProperty name="basic.msg.empty_list">
                                                    <span class="pagebanner">No Records Found.</span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.placement" value="bottom"/>
                                                <display:setProperty name="paging.banner.item_name" value="Disposition"/>
                                                <display:setProperty name="paging.banner.items_name" value="Dispositions"/>
                                                <c:set var="count" value="0"/>
                                                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="dispositionDatetime" value="${lclDisposition.dispositionDatetime}"></fmt:formatDate>
                                                <display:column title="Disposition" headerClass="sortable">${lclDisposition.disposition.eliteCode}</display:column>
                                                <display:column title="Disposition Description" headerClass="sortable">${lclDisposition.disposition.description}</display:column>
                                                <display:column title="Disposition Date Time" headerClass="sortable" ><span>${dispositionDatetime}</span></display:column>
                                                <display:column title="Action">
                                                    <span style="cursor:pointer">
                                                        <img src="${path}/images/error.png" width="16" height="16" onclick="deleteDisposition('${lclDisposition.id}')" title="Delete"/>
                                                    </span>
                                                </display:column>
                                            </display:table>
                                        </c:if>
                                    </cong:td></cong:tr>
                            </cong:table>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </c:if>
        </cong:form>
        <script type="text/javascript">
            jQuery(document).ready(function () {
                $("[title != '']").not("link").tooltip();
                if ($("#unitId").val() !== null && $("#unitId").val() !== "0" && $("#unitId").val() !== "") {
                    var unitNumberObj = document.getElementById("unitNo");
                    setCheckBoxOnload(unitNumberObj);
                    setCheckBoxOnLoadForIT();
                    unitwarhsAddress();
                    cfswarhsAddress();
                    changeUnitSize();
                    setDisableCfsWarehouse();
                }
                $("#loadForm").val($("#lclAddUnitsForm").serialize());
            });
            checkBoxMaxLengthForIt();
            $(document).ready(function () {
                $('#cfsWarehouse').change(function () {
                    $('#cfsWarehouseId').val('');
                    $('#cfswarehsAddress').val('');
                });
                $('#unitWarehouse').change(function () {
                    $('#unitsWarehouseId').val('');
                    $('#unitwarehsAddress').val('');
                });
            });
            function backtoSearchScreen() {
                $("#methodName").val('goBack');
                $("#lclAddUnitsForm").submit();
            }
            function unitwarhsAddress() {
                var unitwarhsId = $("#unitsWarehouseId").val();
                if (unitwarhsId !== null && unitwarhsId !== "" && unitwarhsId !== undefined) {
                    $.ajaxx({
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "impwarehsDetails",
                            param1: unitwarhsId
                        },
                        async: false,
                        success: function (data) {
                            $("#unitwarehsAddress").val(data);
                        }
                    });
                }
            }
            function cfswarhsAddress() {
                var cfswarhsId = $("#cfsWarehouseId").val();
                if (cfswarhsId !== null && cfswarhsId !== "" && cfswarhsId !== undefined) {
                    $.ajaxx({
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "impwarehsDetails",
                            param1: cfswarhsId
                        },
                        async: false,
                        success: function (data) {
                            $("#cfswarehsAddress").val(data);
                        }
                    });
                }
            }
            function saveUnits() {
                var size = $("#unitType option:selected").text().toLowerCase();
                var unitNumber = $("#unitNo").val();
                var unitId = $("#unitId").val();
                var existingDisposition = $("#lclDisposition").find('tr:nth-child(1) td:nth-child(1)').html();
                var strippedDate = $("#strippedDate").val();
                if (!unitNumberValidate(document.getElementById("unitNo"))) {
                    return false;
                }
                else if (unitNumber === null || unitNumber === "") {
                    $.prompt("Unit Number is required");
                    $("#unitNo").css("border-color", "red");
                    $("#warning").show();
                } else if ($("#unitType").val() === "" || $("#unitType").val() === null) {
                    $.prompt("Unit Type Size is required");
                    $("#unitType").css("border-color", "red");
                    $("#warning").show();
                } else if ($("#originAcct").value === null || $("#originAcct").value === "") {
                    $.prompt("Origin Agent is required");
                    $("#originAcct").css("border-color", "red");
                    $("#warning").show();
                } else if ($("#masterBL").val() === null || $("#masterBL").val() === "") {
                    $.prompt("MasterBL is required");
                    $("#masterBL").css("border-color", "red");
                    $("#warning").show();
                } else if (($("#cfsWarehouse").val() === null || $("#cfsWarehouse").val() === "") && size !== "coload") {
                    $.prompt("CFS(Devanning) is required");
                    $("#cfsWarehouse").css("border-color", "red");
                    $("#cfsWarehouse").show();
                } else if (($("#coloaderDevngAcctNo").val() === null || $("#coloaderDevngAcctNo").val() === "") && size === "coload") {
                    $.prompt("Coloader Devanning Warehouse is required");
                    $("#coloaderDevngAcct").css("border-color", "red");
                    $("#coloaderDevngAcct").show();
                } else if(existingDisposition == 'AVAL' && (strippedDate == "" || strippedDate == null)){
                   $.prompt("Strip Date Missing");
                   $("#strippedDate").css("border-color", "red");
                    $("#strippedDate").show(); 
                   }
                else {
                    submitForm(unitId,unitNumber)
                }
            }

            function submitForm(unitId,unitNumber) {
                showLoading();
                parent.$("#methodName").val('saveImpUnits');
                parent.document.getElementById("unitId").value = unitId;
                parent.document.getElementById("unitNo").value = unitNumber;
                parent.document.getElementById("SUHeadingNote").value = document.getElementById("SUHeadingNote").value;
                parent.document.getElementById("unitType").value = document.lclAddUnitsForm.unitType.value;
                parent.document.getElementById("hazmatPermittedUnit").value = $('input:radio[name=hazmatPermitted]:checked').val();
                parent.document.getElementById("drayageProvided").value = $('input:radio[name=drayageProvided]:checked').val();
                parent.document.getElementById("intermodalProvided").value = $('input:radio[name=intermodalProvided]:checked').val();
                parent.document.getElementById("stopoff").value = $('input:radio[name=stopoff]:checked').val();
                parent.document.getElementById("remarks").value = document.lclAddUnitsForm.remarks.value;
                parent.document.getElementById("unitsReopened").value = document.lclAddUnitsForm.unitsReopened.value;
                parent.$("#itDatetime").val($("#itDatetime").val());
                parent.$("#originAcctNo").val($("#originAcctNo").val());
                parent.$("#unitsWarehouseId").val($("#unitsWarehouseId").val());
                parent.$("#lastFreeDate").val($("#lastFreeDate").val());
                parent.$("#coloaderAcctNo").val($("#coloaderAcctNo").val());
                parent.$("#cfsWarehouseId").val($("#cfsWarehouseId").val());
                parent.$("#itNo").val($("#itNo").val());
                parent.$("#itPortId").val($("#itPortId").val());
                parent.$("#approxDueDate").val($("#approxDueDate").val());
                parent.$("#masterBL").val($("#masterBL").val());
                parent.$("#sealNoIn").val($("#sealNoIn").val());
                parent.$("#sealNoOut").val($("#sealNoOut").val());
                parent.$("#strippedDate").val($("#strippedDate").val());
                parent.$("#stuffedByUserId").val($("#loadedByUserId").val());
                parent.$("#destuffedByUserId").val($("#strippedByUserId").val());
                parent.$("#doorNumber").val($("#doorNumber").val());
                parent.$("#coloaderDevngAcctNo").val($("#coloaderDevngAcctNo").val());
                parent.$("#prepaidCollect").val($("#prepaidCollect").val());
                parent.$("#headerId").val($("#headerId").val());
                parent.$("#unitssId").val($("#unitssId").val());
                parent.$("#lclAddVoyageForm").submit();
            }
            function unitNumberValidate(obj) {
                $('#unitType').removeClass('textlabelsBoldForTextBoxDisabledLook');
                $('#unitType').removeAttr('readOnly');
                $('#unitType').removeAttr('disabled');
                $('#unitType').addClass('smallDropDown textlabelsBoldforlcl mandatory');
                var unitchecked = document.getElementById("allowfreetext").checked;
                if (!unitchecked) {
                    var unitNo = obj.value;
                    unitNo = unitNo.replace(/-/g, '');
                    if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(4, 12))) {
                        $.prompt('Unit number must be "AAAA-NNNNNN-N" in format');
                        return false;
                    } else {
                        if (unitNo.lastIndexOf("-") !== 11 || unitNo.indexOf("-") !== 4) {
                            obj.value = unitNo.substring(0, 4) + "-" + unitNo.substring(4, 10) + "-" + unitNo.substring(10);
                        }
                    }
                }
                return true;
            }
            function setCheckBoxOnload(obj) {
                var unitNo = obj.value;
//                unitNo = unitNo.replace(/-/g, '');
                if (unitNo.length < 13 || unitNo.length > 13 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(5, 11))
                         || !isInteger(unitNo.charAt(12))) {
                    document.getElementById("allowfreetext").checked = true;
                    $('#existUnitChecked').val(true);
                }
            }
            function unitNumberExists(obj) {
                var etaPodDate = $("#etaPodDate").val();
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "isUnitNumberExists",
                        param1: etaPodDate,
                        param2: obj.value,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        unitCheckCurrentVoyage(data, obj.value);
                    }
                });
            }
            function submitUnit(txt,data){
                $.prompt(txt, {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            $('#unitId').val(data[0]);
                            $('#unitType').removeClass('smallDropDown textlabelsBoldforlcl mandatory');
                            $('#unitType').attr('readOnly', true);
                            $('#unitType').attr('disabled', true);
                            $('#unitType').addClass('textlabelsBoldForTextBoxDisabledLook');
                            saveUnits();
                        } else if (v === 2) {
                            $('#unitType').removeClass('textlabelsBoldForTextBoxDisabledLook');
                            $('#unitType').removeAttr('readOnly');
                            $('#unitType').removeAttr('disabled');
                            $('#unitType').addClass('smallDropDown textlabelsBoldforlcl mandatory');
                            $('#unitId').val($('#existUnitId').val());
                            $('#unitType').val($('#existUnitTypeId').val());
                            $('#unitNo').val($('#existUnitNo').val());
                            document.getElementById("allowfreetext").checked = $('#existUnitChecked').val();
                            $.prompt.close();
                        }
                    }
                });
            }
            function unitCheckCurrentVoyage(data, unitNo) {
                if(data[1]!=null && data[1]!=""){
                    if(data[1]=='UnitNo'){
                        var txt="Unit# <span style=color:red>" + unitNo + "</span> Already Exists.Do you want to open the same unit?"
                        $('#unitType').val($('#existUnitTypeId').val());
                        submitUnit(txt,data);
                    }else if (data[2]==='true' && data[1]!=null && data[1]!="") {
                        var txt="Unit Number <span style=color:red>" + unitNo + "</span> Already Exists in Voyage#"+
                            "<span style=color:red>" + data[1]+"</span>";
                        $('#unitId').val($('#existUnitId').val());
                        $('#unitType').val($('#existUnitTypeId').val());
                        $('#unitNo').val($('#existUnitNo').val());
                        $("#unitsReopened").val('');
                        $.prompt(txt);
                        return false;
                    }else if(data[2]==='false' && data[1]!=null && data[1]!=""){
                        var txt="Unit Number <span style=color:red>" + unitNo + "</span> Already Exists in Voyage#"+
                            "<span style=color:red>" + data[1]+"</span>.Do you want to open the same unit?";
                        $('#unitType').val(data[3]);
                        submitUnit(txt,data);
                    }
                }else{
                    $("#unitId").val('');
                }
            }

            function deleteDisposition(dispositionId) {
                var txt = 'Are you sure You want to delete?';
                $.prompt(txt, {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            showProgressBar();
                            $("#methodName").val('deleteDisposition');
                            document.getElementById("unitDispoId").value = dispositionId;
                            $("#lclAddUnitsForm").submit();
                            hideProgressBar();
                        } else if (v === 2) {
                            $.prompt.close();
                        }
                    }
                });
            }

            function cfsAddress() {
                var phone = $('#phone').val();
                var fax = $('#fax').val();
                var cfsaddress = "";
                cfsaddress += $("#cfswarehsAddress").val() + "\n";
                cfsaddress += $("#city").val() + " ";
                cfsaddress += $("#state").val() + " ";
                cfsaddress += $("#zipCode").val() + "\n";
                if (null !== phone && phone !== "") {
                    cfsaddress += "Phone:" + $("#phone").val() + "\n";
                }
                if (null !== fax && fax !== "") {
                    cfsaddress += "Fax:" + $("#fax").val();
                }
                $("#cfswarehsAddress").val(cfsaddress);
            }
            function showClientSearchOption(path, searchByValue) {
                var href = path + "/lclBooking.do?methodName=clientSearch&searchByValue=" + searchByValue;
                $(".clientSearchEdit").attr("href", href);
                $(".clientSearchEdit").colorbox({
                    iframe: true,
                    width: "35%",
                    height: "40%",
                    title: searchByValue + " Search"
                });
            }
            $(document).ready(function () {
                var closedBy = $('#closedBy').val();
                if (closedBy !== "") {
                    $('#prepaidCollect').addClass('textlabelsBoldForTextBoxDisabledLook');
                    $('#prepaidCollect').attr('disabled', true);
                }
                $('#itPort').keyup(function () {
                    var itPort = $('#itPort').val();
                    if (itPort === "") {
                        $('#itPort').val('');
                        $('#itPortId').val('');
                    }
                });
                $('#coloaderAcct').keyup(function () {
                    var coloaderAcct = $('#coloaderAcct').val();
                    if (coloaderAcct === "") {
                        $('#coloaderAcct').val('');
                        $('#coloaderAcctNo').val('');
                    }
                });
                $('#loadedByUser').keyup(function () {
                    var loadedByUser = $('#loadedByUser').val();
                    if (loadedByUser === "") {
                        $('#loadedByUser').val('');
                        $('#loadedByUserId').val('');
                    }
                });
                $('#strippedByUser').keyup(function () {
                    var strippedByUser = $('#strippedByUser').val();
                    if (strippedByUser === "") {
                        $('#strippedByUser').val('');
                        $('#strippedByUserId').val('');
                    }
                });
                $('#coloaderDevngAcct').keyup(function () {
                    var strippedByUser = $('#coloaderDevngAcct').val();
                    if (strippedByUser === "") {
                        $('#coloaderDevngAcct').val('');
                        $('#coloaderDevngAcctNo').val('');
                    }
                });
            });
            function editWarehouse(path, id, warhsNo, unitNo) {
                var href = path + "/lclEditWarehouse.do?methodName=editImpWarehouse&unitWarehouseId=" + id + "&wareHsNo=" + warhsNo + "&unitNo=" + unitNo;
                $.colorbox({
                    iframe: true,
                    width: "50%",
                    height: "50%",
                    href: href,
                    title: "Warehouse"
                });
            }
            function changeUnitSize() {
                var unitSize = $("#unitType option:selected").text().toLowerCase();
                if (unitSize === "coload") {
                    $('#cfsWarehouse').removeClass("mandatory");
                    setEnableTextBox("coloaderDevngAcct");
                } else {
                    $('#cfsWarehouse').addClass("mandatory");
                    setDisableTextBox("coloaderDevngAcct");
                    setClearValues("coloaderDevngAcct", "coloaderDevngAcctNo");
                }
            }
            function setDisableTextBox(id) {
                $('#' + id).addClass('textlabelsBoldForTextBoxDisabledLook');
                $('#' + id).attr('readonly', true);
            }
            function setEnableTextBox(id) {
                $('#' + id).removeClass('textlabelsBoldForTextBoxDisabledLook');
                $('#' + id).removeAttr('readonly');
                $('#' + id).addClass('textlabelsBoldForTextBoxWidth');
            }
            function setClearValues(id1, id2) {
                $('#' + id1).val('');
                $('#' + id2).val('');
            }
            function disabledUnitSize(unitNoFlag) {
                if (unitNoFlag === "true") {
                    sampleAlert("The Unit# is already associated with other voyages so you cannot change the container size.");
                    return false;
                }
            }
            function setDisableCfsWarehouse(){
                var stripStr = $('#strippedDate').val();
                if (stripStr != "") {
                    var dateArray = stripStr.split("-");
                    var day = dateArray[0];
                    var month = getMonthNumber(dateArray[1]);
                    var year = dateArray[2];
                    var strippedDate = new Date(month + "/" + day + "/" + year);
                    strippedDate.setDate(strippedDate.getDate() + parseInt(5));
                    var pDate = new Date();
                    if(strippedDate.getDate()===pDate.getDate()){
                        $('#cfsWarehouse').addClass('textlabelsBoldForTextBoxDisabledLook');
                        $('#cfsWarehouse').attr('readonly', true);
                    }
                }
            }
        </script>
    </body>
</html>
