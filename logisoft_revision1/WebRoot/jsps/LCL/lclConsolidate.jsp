<%@include file="init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@include file="/jsps/preloader.jsp" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left;">
        <cong:form name="lclConsolidateForm" id="lclConsolidateForm" action="/lclConsolidate">
            <table width="99%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td>File No:<span class="fileNo">${fileNumber}</span></td>
                    <td>POL:<span class="fileNo" id="polNameId"></span></td>
                    <td>POD:<span class="fileNo" id="podNameId"></span></td>
                    <td>FD:<span class="fileNo" id="fdNameId"></span></td>
                    <td>
                        <span class="button-style1" onclick="openManualContainer('${path}')">Manual Add</span>
                        <span class="button-style1" onclick="addConsolidate('saveConsolidate', '#lclConsolidateForm', '#consolidateDesc')">Save</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" class="dataTable" style="border-collapse: collapse; border: 1px solid #dcdcdc">
                <thead>
                    <tr>
                        <th width="1%">&nbsp;</th>
                        <th class="${lclConsolidateForm.sortByValue eq 'file_number' ? lclConsolidateForm.searchType : ''}">
                            <a href="javascript:doSortAscDec('file_number');">
                                <span style="color: black;">File No</span>
                            </a>
                        </th>
                        <th>Origin</th>
                        <th class="${lclConsolidateForm.sortByValue eq 'client_name' ? lclConsolidateForm.searchType : ''}">
                            <a href="javascript:doSortAscDec('client_name');">
                                <span style="color: black;">Client</span>
                            </a>
                        </th>
                        <th class="${lclConsolidateForm.sortByValue eq 'shipper_name' ? lclConsolidateForm.searchType : ''}">
                            <a href="javascript:doSortAscDec('shipper_name');">
                                <span style="color: black;">Shipper</span></a></th>
                        <th class="${lclConsolidateForm.sortByValue eq 'forwarder_name' ? lclConsolidateForm.searchType : ''}">
                            <a href="javascript:doSortAscDec('forwarder_name');">
                                <span style="color: black;">Forwarder</span></a></th>
                        <th class="${lclConsolidateForm.sortByValue eq 'consignee_name' ? lclConsolidateForm.searchType : ''}">
                            <a href="javascript:doSortAscDec('consignee_name');"><span style="color: black;">Consignee</span></a></th>
                        <th>Status</th>
                        <th class="${lclConsolidateForm.sortByValue eq 'pol_etd' ? lclConsolidateForm.searchType : ''}">
                            <a href="javascript:doSortAscDec('pol_etd');"><span style="color: black;">Sail Date</span></a></th>
                        <th>Piece</th>
                        <th>Weight</th>
                        <th>Measure</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="count" value="0"/>
                    <c:forEach items="${lclConsolidateForm.consolidateFileList}" var="consolidatebean">
                        <c:set var="zebra" value="${zebra eq 'odd' ?  'even' : 'odd'}"/> 
                        <tr class="${zebra}">
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${consolidatebean.etdSailingDate}"/>
                            <td width="1%">
                                <input type="checkbox"  id="${consolidatebean.fileId}" class="chkConsolidate" value="${consolidatebean.fileId}"/>
                                <c:if test="${not empty consolidatebean.consolidateId}">
                                    <img src="${path}/jsps/LCL/images/consolidate.png" alt="cons" height="10" width="10" />
                                </c:if>

                            </td>
                            <td><span class="link ${consolidatebean.fileId eq consolidatedId ? 'highlight-saffron' : ''}" 
                                      style="color:blue;" onclick ="goToBooking('${path}', '${consolidatebean.fileId}', 'B', 'Exports', 'Booking', 'ConsolidatePopUp');">
                                    ${consolidatebean.fileNo}</span></td>
                            <td>${consolidatebean.originName}</td>
                            <td>
                                <span id="clientId" title="${consolidatebean.clientName}">
                                    ${consolidatebean.clientNameKey}
                                </span>
                            </td>
                            <td>
                                <span id="shipperId" title="${consolidatebean.shipperName}">
                                    ${consolidatebean.shipperNameKey}
                                </span>
                            </td>
                            <td>
                                <span id="forwarderId" title="${consolidatebean.forwarderName}">
                                    ${consolidatebean.forwarderNameKey}
                                </span>
                            </td>
                            <td>
                                <span id="consigneeId" title="${consolidatebean.consigneeName}">
                                    ${consolidatebean.consigneeNameKey}
                                </span>
                            </td>
                            <td>
                                <c:if test="${consolidatebean.status eq 'B'}">
                                    <c:out value="Booking"/>
                                </c:if>
                                <c:if test="${consolidatebean.status eq 'R'}">
                                    <c:out value="WAREHOUSE (Release)"/>
                                </c:if>
                                <c:if test="${consolidatebean.status eq 'W'}">
                                    <c:out value="WAREHOUSE (Verified)"/>
                                </c:if>
                                <c:if test="${consolidatebean.status=='WU'}">
                                    <c:out value="WAREHOUSE (Un-verified)"/>
                                </c:if>
                                <c:if test="${consolidatebean.status eq 'RF'}">
                                    <c:out value="WAREHOUSE (Refused)"/>
                                </c:if>
                            </td>
                            <td>${polEtd}</td>
                            <td>${consolidatebean.totalPieceCount}</td>
                            <td>${consolidatebean.totalWeightImperial}</td>
                            <td>${consolidatebean.totalVolumeImperial}</td>
                        </tr>
                        <c:set var="count" value="${count+1}"/>
                    </c:forEach>
                </tbody>
            </table>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclConsolidateForm.fileNumberId}"/>
            <cong:hidden name="podId" id="podId" value="${lclConsolidateForm.podId}"/>
            <cong:hidden name="fdId" id="fdId" value="${lclConsolidateForm.fdId}"/>
            <cong:hidden name="polId" id="polId" value="${lclConsolidateForm.polId}"/>
            <input type="hidden" id="searchType" name="searchType" value="${lclConsolidateForm.searchType}"/>
            <input type="hidden" id="sortByValue" name="sortByValue"  />
            <input type="hidden" id="hiddenSaveDeconsolidate" name="hiddenSaveDeconsolidate" />
            <input type="hidden" id="pickedConsolidate" name="pickedConsolidate" />
            <input type="hidden" id="pickedConsolidateDr" name="pickedConsolidateDr" />
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        jQuery(document).ready(function () {
            $('#polNameId').text(parent.$('#portOfLoading').val());
            $('#podNameId').text(parent.$('#portOfDestination').val());
            $('#fdNameId').text(parent.$('#finalDestinationR').val());
        });
        var chkConsolidate = [];
        function congAlert(txt) {
            $.prompt(txt);
        }

        function submitAjaxForm(methodName, formName, selector, remarks, currentFileId) {
            var count = parent.$('#mytbody tr').length;
            if ($("#pickedConsolidate").val() !== "") {
                chkConsolidate.push($("#pickedConsolidate").val());
            }
            $(".chkConsolidate:checked").each(function () {
                chkConsolidate.push(jQuery(this).val());
            });
            if (chkConsolidate.length <= 0) {
                sampleAlert("Please select atleast one record for consolidation");
                return false;
            } else {
                $("#hiddenSaveDeconsolidate").val(chkConsolidate);
            }
            showLoading();
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            var fileNumberId = $('#fileNumberId').val();
            params += "&fileNumberId=" + fileNumberId + "&consolidatedFileIds=" + chkConsolidate
                    + "&fileState=" + parent.$("#fileType").val() + "&acceptance_remarks=" + remarks + "&currentFileId=" + currentFileId;
            $.post($(formName).attr("action"), params, function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                $("#pickedConsolidate").val("");
                parent.$.fn.colorbox.close();

            });
            if (chkConsolidate.length >= 1 && count === 0) {
                parent.checkConsolidate();
            }
        }
        function goToBooking(path, fileId, fileState, moduleName, toScreen, fromScreen) {
            var fromFileId = parent.$("#fileNumberId").val();
            window.parent.parent.goToConsolidatePage(path, fileId, fileState, moduleName, toScreen, fromScreen, fromFileId);
        }
        function doSortAscDec(sortByValue) {
            var searchType = $("#searchType").val();
            var toggleValue = searchType === "up" ? "down" : "up";
            $("#" + sortByValue).removeClass(searchType).addClass(toggleValue);
            $("#sortByValue").val(sortByValue);
            $("#searchType").val(toggleValue);
            $('#methodName').val('doSortAscDec');
            $('#lclConsolidateForm').submit();
        }

        function addConsolidate(methodName, formName, selector) {
            var currentFileId = $("#fileNumberId").val();
            if (parent.$("#fileType").val() !== "BL") {
                submitAjaxForm(methodName, formName, selector, "", "");
            } else {
                var txt = "This change will not impact the B/L cube/weight/charges since it is already COB";
                var currentFileId = $("#fileNumberId").val();
                var cob = parent.$("#cob").val();
                if (cob === 'true') {
                    cobYes(txt, methodName, formName, selector, currentFileId);
                } else {
                    confirmConsolidation(methodName, formName, selector, currentFileId);
                }
            }
        }

        function confirmConsolidation(methodName, formName, selector, currentFileId) {
            var txt = "Do you want to auto modify the cube/weight/charges on the B/L";
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        submitAjaxForm(methodName, formName, selector, "", currentFileId);
                    } else if (v === 2) {
                        submitAjaxForm(methodName, formName, selector, "", "");
                    }
                }
            });
        }

        function cobYes(txt, methodName, formName, selector, currentFileId) {
            var remarks = '';
            $.prompt(txt, {
                buttons: {Continue: 1, Cancel: 2},
                submit: function (v) {
                    if (v === 1) {
                        remarks = "Accepted rates warning after COB.";
                        submitAjaxForm(methodName, formName, selector, remarks, "");
                    } else if (v === 2) {
                        remarks = "Rates warning after COB not accepted.";
                    }
                    parent.ratesNotAccepted(currentFileId, remarks);
                }
            });
        }
        function openManualContainer(path) {
            $(".chkConsolidate").attr("checked", false);
            var href = path + "/lclConsolidate.do?methodName=openManualConsolidateDR&podId="
                    + $('#podId').val() + "&fdId=" + $('#fdId').val() + "&fileNumberId=" + $("#fileNumberId").val();
            $.colorbox({
                iframe: true,
                href: href,
                height: "70%",
                width: "75%",
                title: "Manual Consolidation"
            });
        }

        function addPickedDrManuallyToConsolidate(fileId) {
            addConsolidate('saveConsolidate', '#lclConsolidateForm', '#consolidateDesc');
        }
    </script>
