<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <table width="100%" >
            <tr  class="tableHeadingNew">
                <td>
                    Change D/R Number For : <span class="fileNnumber red"></span>      
                </td>
            </tr>
        </table>
        <br/>
        <table border="0">
            <tr>
                <td class="textlabelsBoldforlcl" style="padding-left: 8em;">Enter D/R Number :</td>
                <td> <input type="text" id="newFileNumber" name="newFileNumber" maxlength="6" size="10"
                            class="textlabelsBoldForTextBox" style="text-transform: uppercase" onkeyup="checkValidFileNumber(this);" /></td>
            </tr>
            <tr>
                <td style="padding-top:4em;"></td>
                <td>
                    <input type="button" class="button-style1" value="save" 
                           id="saveNewNumber" onclick="saveDrNumber('${path}')"/>
                    <input type="button" class="button-style1" value="Abort" 
                           id="abortDrNumber" onclick="closepopup();"/>
                </td>
            </tr>
        </table>

    </body>
    <script type="text/javascript">
        var fileNumber = "";
        $(document).ready(function () {
            fileNumber = parent.$("#fileNumberBooking").text().trim().trim();
            $(".fileNnumber").text(" " + fileNumber.substring(4, fileNumber.length));
            fileNumber = fileNumber.substring(0, 3);
        });

        function saveDrNumber(path) {
            if ($("#newFileNumber").val().length !== 6) {
                $.prompt("File Number should be 6 digit");
                return false;
            } else if (isfileNumberExists()) {
                $.prompt("<span class='red'>" + $("#newFileNumber").val().toUpperCase() + "</span> Already Exists");
                return false;
            } else {
                showProgressBar();
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.lcl.bc.ExportBookingUtils",
                        methodName: "saveNewDRNumber",
                        param1: $("#newFileNumber").val().toUpperCase(),
                        param2: parent.$("#fileNumber").val(),
                        request: true
                    },
                    success: function (data) {
                        hideProgressBar();
                        parent.$("#fileNumber").val($("#newFileNumber").val().toUpperCase());
                        parent.$.colorbox.close();
                        parent.$(".fileNo").text(fileNumber + "-" + $("#newFileNumber").val().toUpperCase());
                        parent.submitForm("editBooking");
                    }
                });
            }
        }

        function closepopup() {
            parent.$.colorbox.close();
        }

        function isfileNumberExists() {
            var flag = false;
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO",
                    methodName: "getFileIdByFileNumber",
                    param1: $("#newFileNumber").val()
                },
                async: false,
                success: function (data) {
                    if (data !== 0) {
                        flag = true;
                    }
                }
            });
            return flag;
        }

        function checkValidFileNumber(obj) {
            if (!/^[a-zA-Z0-9]*$/.test(obj.value)) {
                obj.value = "";
                sampleAlert("This field should be Alphanumeric");
            }
        }
    </script>
</html>
