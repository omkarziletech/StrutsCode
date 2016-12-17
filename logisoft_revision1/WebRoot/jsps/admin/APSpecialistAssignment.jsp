<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <head>
            <base href="${basePath}"/>
        <title>ApSpecialist Assignment</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <style type="text/css">
            #AssignDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: white;
            }
        </style>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <%@include file="../includes/resources.jsp"%>
    </head>
    <body class="whitebackgrnd" onload="searchUser()">
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </div>
        </div>
        <div>
            <table width="100%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="6">Search APSpecialist</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>First Name</td>
                    <td><input type="text" name="firstName" id="firstName" style="text-transform:uppercase" class="textlabelsBoldForTextBox"/></td>
                    <td>Last Name</td>
                    <td><input type="text" name="lastName" id="lastName" value="" style="text-transform:uppercase" class="textlabelsBoldForTextBox"/></td>
                    <td>Email</td>
                    <td><input type="text" name="email" id="email" class="textlabelsBoldForTextBox"/></td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                        <input type="button" class="buttonStyleNew" onclick="searchUser()" value="Search" />
                        <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues();" />
                    </td>

                </tr>
                <tr>
                    <td colspan="6"><div id="apSpecialistDiv" style="width: 100%"></div></td>
                </tr>
            </table>
        </div>
    </body>
    <script type="text/javascript">
        function searchUser() {
            var firstName = jQuery("#firstName").val();
            var lastName = jQuery("#lastName").val();
            var email = jQuery("#email").val();
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "findUser",
                    param1: firstName,
                    param2: lastName,
                    param3: email,
                    param4: "APSpecialist",
                    forward: "/jsps/admin/ApSpecialistListTemplate.jsp"
                },
                success: function(data) {
                    jQuery("#apSpecialistDiv").html(data);
                }
            });
        }
        function showAssignWindow(userId) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "showAssignWindow",
                    param1: userId,
                    param2: "apSpecialist",
                    forward: "/jsps/admin/ApSpecialistAssignWindow.jsp"
                },
                success: function(data) {
                    if (data) {
                        showPopUp();
                        var AssignDiv = createHTMLElement("div", "AssignDiv", "15%", "40%", document.body);
                        jQuery("#AssignDiv").html(data);
                        floatDiv("AssignDiv", document.body.offsetWidth / 2, document.body.offsetHeight / 3).floatIt();
                    }
                }
            });
        }
        function closeAssignWindow() {
            document.body.removeChild(document.getElementById('AssignDiv'));
            closePopUp();
        }

        function assignApSpecialist(userId) {
            var subType = document.getElementsByName('subType');
            var subTypes = "";
            for (var i = 0; i < subType.length; i++) {
                if (subType[i].checked) {
                    subTypes = subTypes + subType[i].value + ",";
                }
            }
            if (trim(subTypes) != "") {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.dwr.DwrUtil",
                        methodName: "assignAPSpecialistToTradingPartner",
                        param1: trim(userId),
                        param2: trim(subTypes),
                        dataType: "json"
                    },
                    success: function(data) {
                        if (data) {
                            closeAssignWindow();
                            searchUser();
                        } else {
                            showPopUp();
                            alert("SubTypes already assigned");
                        }
                    }
                });
            } else {
                alert("Please select atleast one sub type");
            }
        }

        function removeApSpecialist(userId) {
            if (confirm("Do you want to deassign?")) {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.dwr.DwrUtil",
                        methodName: "unAssignAPSpecialistFromTradingPartner",
                        param1: userId,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (data) {
                            searchUser();
                        } else {
                            alert("Not deassigned");
                        }
                    }
                });
            }
        }

        function clearValues() {
            jQuery("#firstName").val("");
            jQuery("#lastName").val("");
            jQuery("#email").val("");
            jQuery("#apSpecialistDiv").empty();
        }
    </script>
</html>