<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<body>
    <cong:form  id="lclBlHazmatForm"  name="lclBlHazmatForm" action="/lclBlHazmat.do" >
        <cong:div style="width:99%;">
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="fileId" id="fileId" value="${lclBlHazmatForm.fileId}"/>
            <cong:hidden name="fileNo" id="fileNo" value="${lclBlHazmatForm.fileNo}"/>
            <cong:hidden name="blPieceId" id="blPieceId"  value="${lclBlHazmatForm.blPieceId}"/>
            <cong:table  style="width:100%">
                <cong:tr styleClass="tableHeadingNew" >
                    <cong:td  width="10%">Edit Hazmat for File No: <span style="color: red">${lclBlHazmatForm.fileNo}</span></cong:td>
                    <cong:td  width="10%">Tariff No:  <span style="color: red" >${lclBlHazmatForm.commodityNo}</span></cong:td>
                    <cong:td  width="10%">Tariff Name: <span style="color: red" >${lclBlHazmatForm.commodityName}</span></cong:td>
                </cong:tr>
            </cong:table>

            <c:if test="${not empty bkgHazmatList}">
                <cong:div id="hazmatList">
                    <table class="dataTable">
                        <thead>
                            <tr>
                                <th>FileNo#</th>
                                <th>UN Number</th>
                                <th>Proper Shipping Name</th>
                                <th>Technical Name</th>
                                <th>IMO Class Code</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach  var="hazmat" items="${bkgHazmatList}">
                                <c:choose>
                                    <c:when test="${rowStyle eq 'oddStyle'}">
                                        <c:set var="rowStyle" value="evenStyle"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="rowStyle" value="oddStyle"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr class="${rowStyle}">
                                    <td>${hazmat.fileNo}</td>
                                    <td>${hazmat.unHazmatNo}</td>
                                    <td>${hazmat.shippingName}</td>
                                    <td>${hazmat.technicalName}</td>
                                    <td>${hazmat.priclassCode}</td>
                                    <td>
                                        <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge" width="13" height="13" alt="edit"
                                             onclick="editHazmat('${path}','${hazmat.bkgHazmatId}','${hazmat.fileNo}');"
                                             title="Edit Hazmat"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </cong:div>
            </c:if>
        </cong:div>
    </cong:form>
    <script type="text/javascript">
        function editHazmat(path,bkgHazmatId,fileNo){
            var fileId=$('#fileId').val();
            var blPieceId=$('#blPieceId').val();
            var href = path + "/lclBlHazmat.do?methodName=editHazmat&bkgHazmatId="+bkgHazmatId+"&hazmatFileNo="+fileNo+"&fileId="+fileId+"&blPieceId="+blPieceId;
            $.colorbox({
                iframe: true,
                width: "90%",
                height: "90%",
                href: href,
                title: "Edit Hazmat"
            });
        }
    </script>
</body>
