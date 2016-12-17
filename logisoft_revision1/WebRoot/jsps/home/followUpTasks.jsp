<%-- 
    Document   : followUpTasks
    Created on : Mar 27, 2012, 6:34:39 PM
    Author     : Lakshmi Naryanan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="scrollable-table-popup followup-container" style="height: 250px;">
    <div>
        <div>
            <table>
                <thead>
                    <tr>
                        <th><div label="Follow Up Date"/></th>
                        <th><div label="Module"/></th>
                        <th><div label="Ref #"/></th>
                        <th><div label="Notes"/></th>
                        <th><div label="Action"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="followUpTasks" value="${homeForm.followUpTasks}"/>
                    <c:choose>
                        <c:when test="${not empty followUpTasks}">
                            <c:set var="modules" value="AR_INQUIRY,AP_INQUIRY,ACCOUNT_PAYABLE,AR_BATCH,TRADING_PARTNER,FCL_FILE,LCL_FILE,LCL_VOYAGE"/>
                            <c:set var="removeModules" value="AR_INQUIRY,AP_INQUIRY,ACCOUNT_PAYABLE,AR_BATCH,TRADING_PARTNER"/>
                            <c:forEach var="task" items="${followUpTasks}">
                                <tr class="${zebra}">
                                    <td>${task.followupDate}</td>
                                    <td>${fn:replace(task.module, '_', ' ')}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${str:in(task.module, modules)}">
                                                <a href="javascript:jump('${fn:replace(task.module, '_', ' ')}', '${task.reference}')">${task.reference}</a>
                                            </c:when>
                                            <c:otherwise>
                                                ${fn:toUpperCase(task.reference)}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="green bold" title="${task.description}">${fn:toUpperCase(str:abbreviate(task.description, 45))}</td>
                                    <td>
                                        <c:if test="${str:in(task.module, removeModules) and not empty task.id}">
                                            <img title="Remove" src="${path}/img/icons/remove.gif" onclick="removeFollowUpTask('${task.id}')"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="odd">
                                <td class="green bold" colspan="4">No Follow Up Tasks found</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>                  
    </div>                  
</div>                  
<c:if test="${not empty lockMessage}">
    <script type="text/javascript">
        var lockMessage = "${lockMessage}";
        if (lockMessage.indexOf("is already opened in another window") > -1) {
            $.prompt(lockMessage);
        } else {
            $.prompt(lockMessage + ". Do you want to view the file?", {
                buttons: {
                    "Yes": true,
                    "No": false
                },
                callback: function (v) {
                    if (v) {
                        jump2view();
                    }
                }
            });
        }
    </script>
</c:if>
<script type="text/javascript">
    var selectedFileNumber;
    function checkHomeFileLock(path, fileNumber, fileId, moduleId, userId, lclFileType, fclFileType, unitSsId) {
        if (lclFileType === "I" || lclFileType === "E") {
            var moduleName = "";
            if (lclFileType === "I") {
                moduleName = "Imports";
            } else if (lclFileType === "E") {
                moduleName = "Exports";
            }
            $("#action").val("checkHomeFileLock");
            $("#fileNumber").val(fileNumber);
            $("#userId").val(userId);
            $("#screenName").val("LCL");
            var params = $("#homeForm").serialize();
            $.ajaxx({
                url: path + "/home.do",
                data: params,
                preloading: true,
                success: function (data) {
                    if (data === "available") {
                        callBackMethod(path, fileId, moduleId, moduleName, lclFileType, fileNumber);
                    } else if (data.indexOf("is already opened in another window") > -1) {
                        $.prompt(data);
                    } else {
                        $.prompt(data + ". Do you want to view the file?", {
                            buttons: {
                                Yes: 1,
                                No: 2
                            },
                            submit: function (v) {
                                if (v === 1) {
                                    callBackMethod(path, fileId, moduleId, moduleName, lclFileType, fileNumber);
                                } else if (v === 2) {
                                    $.prompt.close();
                                }
                            }
                        });
                    }
                }
            });
        } else if (unitSsId !== "") {
            callBackMethod(path, fileId, moduleId, moduleName, lclFileType, fileNumber, unitSsId, 'OPEN IMP VOYAGE');
        } else {
            openFclFile(path, fileNumber, userId, fclFileType);
        }

    }


    function openFclFile(path, fileNumber, userId, fclFileType) {
        selectedFileNumber = fileNumber;
        $("#action").val("checkHomeFileLock");
        $('#fileNumber').val(fileNumber);
        $('#userId').val(userId);
        $("#screenName").val("FCL");
        var params = $("#homeForm").serialize();
        $.ajaxx({
            url: path + "/home.do",
            data: params,
            preloading: true,
            success: function (data) {
                showResult(data, fclFileType);
            }
        });
    }


    function showResult(result, fclFileType) {
        if (result === 'available') {
            gotoFclOpsScreens(selectedFileNumber, '', fclFileType);
        } else if (result.indexOf("is already opened in another window") > -1) {
            $.prompt(result);
        } else {
            $.prompt(result + ". Do you want to view the file?", {
                buttons: {
                    "Yes": true,
                    "No": false
                },
                callback: function (v) {
                    if (v) {
                        gotoFclOpsScreens(selectedFileNumber, '', fclFileType);
                    }
                }
            });
        }
    }
    function callBackMethod(path, fileId, moduleId, moduleName, lclFileType, fileNumber, unitSsId, callBackFlag) {
        changeLclChilds(path, fileId, moduleId, moduleName, callBackFlag, unitSsId, lclFileType, fileNumber);
    }
</script>
