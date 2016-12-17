<%-- 
    Document   : newLclTpNote
    Created on : Jul 6, 2015, 10:32:27 AM
    Author     : PALRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script type="text/javascript">
    function addNewNotes() {
        showBlock('#notesTable');
        $("#tpUpdate").hide();
        $("#tpSave").show();
    }
    function cancelNotes() {
        $("#notesTable").hide();
        $("#noteTypeSub").val('');
        $("#noteDescSub").val('');
    }
    function saveTpNote(save) {
        var path = "/" + window.location.pathname.split('/')[1];
        var noteType = $("#noteTypeSub").val();
        var noteDesc = $("#noteDescSub").val();
        if (noteType === "") {
            $.prompt("Please Select Note Type");
            $("#noteTypeSub").css("border-color", "red");
            return false;
        }
        if(save ==='save'){
            $("#tpId").val('');
        }
        $("#tpNoteType").val(noteType);
        $("#tpNoteDesc").val(noteDesc);
        $("#methodName").val('addTpNote');
        var params = $("#blueScreenNotesForm").serialize();
        $.ajaxx({
            url: path + "/bluescreenCustomerNotes.do",
            data: params,
            preloading: true,
            success: function (data) {
                $("#notesTable").hide();
                $("#noteSymbol").val(trim(data));
                $("#sortBy").val('');
                $("#methodName").val("displayNotes");
                $("#blueScreenNotesForm").submit();
            }
        });

    }
</script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    </head>
    <body>
    <cong:table id="noteTable" cellpadding="0" cellspacing="0" width="100%" border="0">
        <cong:tr styleClass="tableHeadingNew">
            <cong:td>
                <cong:div styleClass="button-style1" style="float:left" onclick="addNewNotes()">Add</cong:div>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td>
                <cong:table id="notesTable" width="40%" style="margin:5px 0; float:left; display:none;" border="0">
                    <cong:tr>
                        <cong:td styleClass="td textlabelsBoldforlcl">Note Type</cong:td>
                        <cong:td>
                            <cong:select name="tpNoteType" container="NULL" id="noteTypeSub">
                                <cong:option  value="">select</cong:option>
                                <cong:option  value="$">Accounting($)</cong:option>
                                <cong:option  value="A">Air(A)</cong:option>
                                <cong:option  value="D">Doc(D)</cong:option>
                                <cong:option  value="F">FCL(F)</cong:option>
                                <cong:option  value="G">General/Sales(G)</cong:option>
                                <cong:option  value="L">LCL(L)</cong:option>
                                <cong:option  value="I">Import(I)</cong:option>
                                <cong:option  value="S">Sales(S)</cong:option>
                                <cong:option  value="W">Whse(W)</cong:option>
                            </cong:select>
                        </cong:td>
                    </cong:tr>
                    <tr>
                        <td></td>
                        <td></td>
                    </tr>
                    <cong:tr>
                        <cong:td styleClass="td textlabelsBoldforlcl">Note</cong:td>
                        <cong:td>
                            <cong:textarea name="tpNoteDesc" cols="40" rows="3" id="noteDescSub" style="text-transform:uppercase;color:black;font-size:12px"></cong:textarea>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td></cong:td>
                        <cong:td colspan="3">
                            <input type="button"  id="tpSave" value="Save" class="button-style1" onclick="saveTpNote('save');"/>
                            <input type="button"  id="tpUpdate" value="update" class="button-style1" onclick="saveTpNote();"/>
                            <input type="button"  value="Cancel" class="button-style1" onclick="cancelNotes()"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
        </cong:tr>
    </cong:table>
</body>
</html>
