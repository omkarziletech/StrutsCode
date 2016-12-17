<%-- 
    Document   : addOrEditDisposition
    Created on : Feb 12, 2016, 10:13:43 PM
    Author     : Mei
--%>

<div id="lclDispositionDiv" style="visibility: hidden;position: absolute">
    <c:if test="${lclAddUnitsForm.filterByChanges ne 'unassignedContainers'}">
        <table align="center" id="noteTable" cellpadding="0" cellspacing="0"
               width="99%" border="0" style="border:1px solid #dcdcdc">
            <tr class="tableHeadingNew">
                <td>
                    <div onclick="toggle('#disp-table');" style="cursor: pointer;">
                        DISPOSITION
                    </div>
                </td>
                <td class="textlabelsBoldforlcl">
                    <div class="red" style="cursor: pointer;text-align:right;"
                         onclick="toggle('#disp-table');">
                        Click this bar to Add Disposition &nbsp;
                    </div>
                </td>
            </tr>
            <td colspan="2">
            <cong:table id="disp-table" width="97%" border="0" style="display:none;">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" width="15%">
                        Disposition
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl">
                        <cong:autocompletor name="disposition" id="disposition" width="350" scrollHeight="300px" styleClass="mandatory"
                                            query="DISPOSITION" fields="NULL,dispositionId" template="two" shouldMatch="true"/>
                        <cong:hidden name="dispositionId" id="dispositionId" />
                    </cong:td>
                    <cong:td width="5%" styleClass="textlabelsBoldforlcl">Disposition Date/Time</cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl">
                        <cong:calendarNew styleClass="textlabelsBoldForTextBoxsizeWidth152" id="dispositionDateTime"
                                          is12HrFormat="true" showTime="true"  name="dispositionDateTime"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Select City</cong:td>
                    <cong:td>
                        <html:select property="arrivallocation" styleId="arrivallocation" value="" style="width:120px;"
                                     styleClass="textlabelsBoldForTextBox verysmalldropdownStyleForText">
                            <html:option value="">Select One</html:option>
                            <html:optionsCollection name="arrivalCityList"/>
                        </html:select>
                        <input type="hidden" name="unLocId" id="unLocId"/>
                    </cong:td>
                    <cong:td>
                        <input type="button" class="button-style1" value="Save" id="saveCode"
                               onclick="saveDisposition();"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            </td>
            <tr>
                <td colspan="2">
            <c:if test="${not empty lclDispositionList}">
                <div id="divtablesty1" style="width: 100%;">

                    <table class="dataTable">
                        <thead>
                            <tr>
                                <th>Disposition</th>
                                <th>Disposition Description</th>
                                <th>Disposition Date Time</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="disposition" items="${lclDispositionList}">
                            <c:choose>
                                <c:when test="${rowStyle eq 'oddStyle'}">
                                    <c:set var="rowStyle" value="evenStyle"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="rowStyle" value="oddStyle"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${rowStyle}">
                            <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a"
                                            var="dispositionDatetime" value="${disposition.dispositionDatetime}"/>
                            <td>${disposition.disposition.eliteCode}</td>
                            <td>${disposition.disposition.description}</td>
                            <td>${dispositionDatetime}</td>
                            <td>
                                <img src="${path}/images/error.png" width="16" height="16" style="cursor:pointer" title="Delete Disposition"
                                     alt="Delete"  onclick="deleteDisposition('${disposition.id}')"/>
                            </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            </td>
            </tr>
        </table>
    </c:if>
</div>