<%-- 
    Document   : addOrEditWarehouse
    Created on : Feb 12, 2016, 10:12:07 PM
    Author     : Mei
--%>

<div id="lclWarehouseDiv" style="visibility: hidden;position: absolute;vertical-align: top;">
    <cong:table align="center" id="noteTable" cellpadding="0" cellspacing="0" width="99%" border="1" style="border:1px solid #dcdcdc">
        <cong:tr styleClass="tableHeadingNew">
            <cong:td width="100%">WAREHOUSE</cong:td>
            <cong:td styleClass="textlabelsBoldforlcl" >
                <div class="button-style1" onclick="addWarehouse('${path}')">Add</div>
            </cong:td>
        </cong:tr>
        <cong:tr >
            <cong:td colspan="2">
                <c:if test="${not empty lclWarehouseList}">
                    <div id="divtablesty1" style="width: 100%;">
                        <table class="dataTable">
                            <thead>
                                <tr>
                                    <th>Warehouse Name</th>
                                    <th>Location</th>
                                    <th>Arrived Date Time</th>
                                    <th>Departed Date Time</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="warehouse" items="${lclWarehouseList}">
                                <c:choose>
                                    <c:when test="${rowStyle eq 'oddStyle'}">
                                        <c:set var="rowStyle" value="evenStyle"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="rowStyle" value="oddStyle"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr class="${rowStyle}">
                                    <td>
                                        ${warehouse.warehouse.warehouseName}
                                        <span  style="color:#0000FF;font-weight: bold">(${warehouse.warehouse.warehouseNo})</span>
                                    </td>
                                    <td>${warehouse.location}</td>
                                    <td>
                                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="arrivedDatetime" value="${warehouse.arrivedDatetime}"/>
                                ${warehouse.arrivedDatetime}
                                </td>
                                <td>
                                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="departedDatetime" value="${warehouse.departedDatetime}"/>
                                ${warehouse.departedDatetime}
                                </td>
                                <td>
                                    <a href="${path}/lclEditWarehouse.do?methodName=editWarehouse&unitWarehouseId=${warehouse.id}" class="editWarehouse">
                                        <img src="${path}/images/edit.png" alt="edit" width="16" height="16" title="Edit Warehouse" />
                                    </a>
                                    <img src="${path}/images/error.png" alt="delete" width="16" height="16"
                                         title="Delete Warehouse" onclick="deleteWarehouse('${warehouse.id}')"/>
                                </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </cong:td>
        </cong:tr>
    </cong:table>
</div>
