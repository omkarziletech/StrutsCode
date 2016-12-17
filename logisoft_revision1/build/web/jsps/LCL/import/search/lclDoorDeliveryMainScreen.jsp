<%-- 
    Document   : lclDoorDeliveryMainScreen
    Created on : Jun 9, 2016, 12:35:01 PM
    Author     : PALRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="../../init.jsp" %>
<%@include file="../../../includes/jspVariables.jsp"%>
<%@include file="../../../../jsps/includes/baseResources.jsp" %>
<%@include file="../../../includes/resources.jsp" %>
<%@include file="/taglib.jsp"%>
<%@include file="/jsps/LCL/colorBox.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<script type="text/javascript" src="${path}/jsps/LCL/js/doorDeliverySearch.js"></script>
<cong:body style="height:250px">
    <cong:div>
        <cong:form id="lclDoorDeliverySearchForm" styleClass="demo-1" name="lclDoorDeliverySearchForm" method="post" action="LclDoorDeliverySearch.do">
            <cong:table styleClass="widthFull" cellspacing="2" border="0">
                <cong:tr styleClass="tableHeadingNew"><cong:td colspan="10">Search Criteria</cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" >Voyage #
                        <cong:text name="voyageNo" id="voyageNo" styleClass="textuppercaseLetter textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyValues()" onkeypress="checkForNumberOnly(this)"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">POL
                        <cong:autocompletor name="pol" id="pol" template="one" width="250" container="NULL" scrollHeight="300px" 
                                            query="ORIGIN_UNLOC" styleClass="textlabelsLclBoldForMainScreenTextBox" 
                                            shouldMatch="true" fields="polName,NULL,NULL,polCountryCode"/>
                        <cong:hidden name="polCountryCode" id="polCountryCode"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" >Hazmat
                        <html:select property="hazmat" styleId="hazmat" styleClass="textlabelsLclBoldForMainScreenTextBox">
                            <html:option value="">Select</html:option>
                            <html:option value="Y">Yes</html:option>
                            <html:option value="N">No</html:option>
                        </html:select>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">ETA From Date</cong:td>
                    <cong:td>
                        <cong:calendarNew name="etaDate" id="etaDate" styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" width="10%">ETA To Date</cong:td>
                    <cong:td>
                        <cong:calendarNew name="etaToDate" id="etaToDate" styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>                           
                </cong:tr>

                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" >DR #
                        <cong:text name="fileNumber" id="fileNumber" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyValues()"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">POD
                        <cong:autocompletor name="pod" id="pod" query="IMPORT_MAIN_SCREEN_DESTINATION" 
                                            template="one" width="500" fields="podName,NULL,NULL,podCountryCode"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox" container="NULL" 
                                            shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="podCountryCode" id="podCountryCode"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" >Need POD
                        <html:select property="deliveryProof" styleId="deliveryProof" styleClass="textlabelsLclBoldForMainScreenTextBox">
                            <html:option value="">Select</html:option>
                            <html:option value="Y">Yes</html:option>
                            <html:option value="N">No</html:option>
                        </html:select>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" width="10%">APD </cong:td>
                    <cong:td> 
                        <cong:calendarNew name="actualPickupDate"  id="actualPickupDate" styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" width="10%">Last FD </cong:td>
                    <cong:td> 
                        <cong:calendarNew name="lastFreeDate"  id="lastFreeDate" styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>   
                </cong:tr>

                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" >HBL #
                        <cong:text name="hblNo" id="hblNo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyValues()"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Disposition
                        <cong:autocompletor name="dispositionCode" id="dispositionCode" width="400" scrollHeight="300px" 
                                            container="NULL" position="left" styleClass="textuppercaseLetter textlabelsLclBoldForMainScreenTextBox"
                                            query="DISPOSITION" fields="NULL,dispositionId" template="two"/>
                        <cong:hidden name="dispositionId" id="dispositionId"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" >Lift Gate
                        <html:select property="liftGate" styleId="liftGate" styleClass="textlabelsLclBoldForMainScreenTextBox">
                            <html:option value="">Select</html:option>
                            <html:option value="Y">Yes</html:option>
                            <html:option value="N">No</html:option>
                        </html:select>
                    </cong:td>

                    <cong:td styleClass="textlabelsBoldforlcl">EDD </cong:td>
                    <cong:td> 
                        <cong:calendarNew  name="estimatedDeliveryDate"  id="estimatedDeliveryDate" styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" width="10%">Delivery Order </cong:td>
                    <cong:td> 
                        <cong:calendarNew name="deliveryOrder" id="deliveryOrder" styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>
                </cong:tr>

                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" >PRO #
                        <cong:text name="proNo" id="proNo" styleClass="textlabelsLclBoldForMainScreenTextBox" onkeyup="setEmptyValues()"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Carrier
                        <cong:text name="scacCode" id="scacCode" styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" >Commercial Delivery
                        <html:select property="commercialDelivery" styleId="commercialDelivery" styleClass="textlabelsLclBoldForMainScreenTextBox">
                            <html:option value="">Select</html:option>
                            <html:option value="Y">Yes</html:option>
                            <html:option value="N">No</html:option>
                        </html:select>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" width="10%">ADD </cong:td>
                    <cong:td> 
                        <cong:calendarNew name="actualDeliveryDate" id="actualDeliveryDate" styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Customs Clearance Received </cong:td>
                    <cong:td> 
                        <cong:calendarNew name="customsClearanceReceived" id="customsClearanceReceived"  styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>
                </cong:tr>

                <cong:tr>
                    <cong:td></cong:td>
                    <cong:td ></cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" >Door Delivery Status
                        <html:select property="doorDeliveryStatus" styleId="doorDeliveryStatus" styleClass="textlabelsLclBoldForMainScreenTextBox">
                            <html:option value="">Select</html:option>
                            <html:option value="P">Pending(Cargo at CFS )</html:option>
                            <html:option value="O">Out For Delivery</html:option>
                            <html:option value="D">Delivered</html:option>
                            <html:option value="F" >Final/Closed</html:option>
                            <html:option value="PC">Pending contact</html:option>
                            <html:option value="DR">Docs requested</html:option>
                            <html:option value="PD">Pending docs</html:option>
                            <html:option value="PA">Pending Appt</html:option>
                            <html:option value="PB">Pending balance</html:option>
                            <html:option value="OH">Cargo on HOLD</html:option>
                            <html:option value="DP">Dispatched</html:option>
                        </html:select>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">EPD
                    </cong:td>
                    <cong:td>
                        <cong:calendarNew name="estimatePickupDate" id="estimatePickupDate" styleClass="textlabelsLclBoldForMainScreenTextBox picker" />
                    </cong:td>
                    <cong:td></cong:td>
                </cong:tr> 
                <cong:tr>
                    <cong:td></cong:td>
                    <cong:td></cong:td>
                    <cong:td></cong:td>
                    
                </cong:tr>
            </cong:table>
            <cong:div >
                <cong:table id="search-criteria" styleClass="tableHeadingNew" cellspacing="2">
                    <cong:tr>
                        <cong:td>Search Result</cong:td>
                        <cong:td width="25px" styleClass="textlabelsBoldforlcl filedSpace" >Limit
                            <html:select property="limit" styleId="limit" styleClass="textlabelsBoldforlcl" style="width:75px">
                                <html:option value="100">100</html:option>
                                <html:option value="150">150</html:option>
                                <html:option value="250">250</html:option>
                                <html:option value="500">500</html:option>
                                <html:option value="1000">1000</html:option>
                            </html:select>   
                        </cong:td>
                        <cong:td styleClass="filedSpace"><input type="button"  value="Search"  class="button-style1" onclick="submitDoorDeliveryForm('search');"/>
                            <input type="button" value="Reset"  class="button-style1 filedSpace" onclick="clearValues()"/>
                            <input type="button" value="Export Excel" class="button-style1 filedSpace" onclick="exportToExcel()"</cong:td>
                            <cong:td></cong:td>
                        </cong:tr>
                    </cong:table>       
                </cong:div>
                <div id="pen">
                <%@include file="/jsps/LCL/import/search/LclDoorDeliverySearchResult.jsp" %>
            </div>                             
            <cong:hidden name="methodName" id="methodName"></cong:hidden>
            <input type="hidden" id="sortBy" name="sortBy" value="${lclDoorDeliverySearchForm.sortBy}"/>
            <input type="hidden" id="orderBy" name="orderBy" />
        </cong:form>
    </cong:div>
</cong:body>