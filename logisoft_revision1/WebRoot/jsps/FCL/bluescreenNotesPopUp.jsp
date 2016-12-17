<%-- 
    Document   : bluescreenNotesPopUp
    Created on : Jan 27, 2014, 11:46:01 PM
    Author     : Meiyazhakan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BlueScreen Notes</title>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css" />
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/layout.css" />
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/alert/jquery-impromptu.3.2.js"></script>
        <%@ taglib prefix="cong" tagdir="/WEB-INF/tags/cong"%>
        <cong:javascript src="${path}/jsps/LCL/js/common.js"/>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
    </head>
    <body>
        <html:form  action="/bluescreenCustomerNotes" styleId="blueScreenNotesForm" name="BlueScreenNotesForm" type="com.gp.cvst.logisoft.struts.form.BlueScreenNotesForm" scope="request">
            <%@include file="/jsps/FCL/blueScreenNoteTp.jsp"%>
            </br>
            <table width="100%">
                <thead>
                    <tr class="tableHeadingNew">
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeA" styleClass="notes" value="$" disabled="${empty notesStatusMap.get('Account')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('Account') ? '' :'greenBold15px'}" id="acctNotes">Accounting($)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeAi" styleClass="notes" value="A" disabled="${empty notesStatusMap.get('Air')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('Air') ? '' :'greenBold15px'}" id="airNotes">Air(A)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeAu" styleClass="notes" value="*" disabled="${empty notesStatusMap.get('auto')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('auto') ? '' :'greenBold15px'}" id="autoNotes">Auto(*)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeD" styleClass="notes" value="D" disabled="${empty notesStatusMap.get('Doc')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('Doc') ? '' :'greenBold15px'}" id="docNotes">Doc(D)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeF" styleClass="notes" value="F" disabled="${empty notesStatusMap.get('Fcl')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('Fcl') ? '' :'greenBold15px'}" id="fclNotes">FCL(F)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeG" value="G" styleClass="notes" 
                                           disabled="${empty notesStatusMap.get('General')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('General') ? '' :'greenBold15px'}" id="generalNotes">General(G)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeL" styleClass="notes" value="L" disabled="${empty notesStatusMap.get('Lcl')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('Lcl') ? '' :'greenBold15px'}" id="lclNotes">LCL(L)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeI" styleClass="notes" value="I" disabled="${empty notesStatusMap.get('Imports')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('Imports') ? '' :'greenBold15px'}" id="importsNotes">Imports(I)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeS" value="S" styleClass="notes" disabled="${empty notesStatusMap.get('Sales')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('Sales') ? '' :'greenBold15px'}" id="salesNotes">Sales(S)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeW" value="W" styleClass="notes" disabled="${empty notesStatusMap.get('whse')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('whse') ? '' :'greenBold15px'}" id="whseNotes">Whse(W)</span>
                        </td>
                        <td>
                            <html:checkbox property="noteType" styleId="noteTypeV" value="V" styleClass="notes" disabled="${empty notesStatusMap.get('void')}" onclick="searchNotes(this)"/>
                            <span class="${empty notesStatusMap.get('void') ? '' :'greenBold15px'}" id="voidNotes">void(V)</span>
                        </td>
                    </tr>
                </thead>
            </table>
            <table class="dataTable">
                <thead>
                    <tr>
                        <th></th>
                        <th>Notes</th>
                        <th> <a href="javascript:doSortAscDesc('itemName')"><span style="color: black !important;">Created Date</span></a></th>
                        <th>User</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="notes" items="${notesList}">
                        <c:choose>
                            <c:when test="${rowStyle eq 'oddStyle'}">
                                <c:set var="rowStyle" value="evenStyle"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="rowStyle" value="oddStyle"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${rowStyle}">
                            <td>${notes.noteType}</td><td>
                                <div style="width:500px; white-space: normal">
                                 ${fn:replace(notes.noteDesc,'\\N','<br/>')}   
                                </div>
                               </td><td>
                                ${notes.itemName}</td><td>
                                ${notes.updatedBy}</td><td>
                                <c:if test="${notes.tpId ne '' && noteTypeSymbol ne 'V'}">
                                 <img src="${path}/img/icons/edit.gif" alt="edit" height="12px" width="12px" 
                         onclick="tpEditNote('${notes.tpId}', '${notes.noteDesc}', '${notes.noteType}')" style="cursor:pointer"/>
                                 </c:if>
                                 <c:if test="${roleDuty.deleteManualNotes && notes.tpId ne '' && noteTypeSymbol ne 'V'}">
                         <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px" onclick="deleteTpNote('Are you sure you want to delete?', '${notes.tpId}','${notes.noteDesc}')" style ="cursor: pointer"/>        
                                  </c:if>
                                 </td>  
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <html:hidden property="methodName" styleId="methodName"/>
            <html:hidden property="tpNoteType" styleId="tpNoteType"/>
            <html:hidden property="tpNoteDesc" styleId="tpNoteDesc"/>
            <html:hidden property="tpId" styleId="tpId"/>
            <input type="hidden" id="customerName" name="customerName"  value="${blueScreenNotesForm.customerName}"/>
            <input type="hidden" id="customerNo" name="customerNo" value="${blueScreenNotesForm.customerNo}"/>
            <input type="hidden" id="noteTypeSymbol" name="noteTypeSymbol" value="${noteTypeSymbol}"/>
            <input type="hidden" id="noteSymbol" name="noteSymbol" value="${noteTypeSymbol}"/>
            <input type="hidden" id="sortBy" name="sortBy"  value="${blueScreenNotesForm.sortBy eq "ASC" ? "DESC":"ASC"}"/>
            <input type="hidden" id="orderBy" name="orderBy"/>
            <input type="hidden" id="noteSymbolList" value="${noteSymbolList}"/>
        </html:form>
    </body>
    <script type="text/javascript">
        jQuery(document).ready(function () {
            var noteSymbol = $('#noteTypeSymbol').val();
            var noteSymbolList =$("#noteSymbolList").val();
            if (noteSymbol !== '') {
                setNoteSymbol(noteSymbol,noteSymbolList);
                $('#noteTypeSymbol').val('');
            } else{
                disableChecking(noteSymbolList);
            }
        });
        function searchNotes(id) {
            $('#noteSymbol').val($(id).val());
            $('#methodName').val('displayNotes');
            $('#blueScreenNotesForm').submit();
        }
        function setNoteSymbol(noteSymbol,noteSymbolList) {
            if (noteSymbol === '$' && noteSymbolList.toString().indexOf("$") !== -1) {
                $('#noteTypeA').attr("checked", true);
                setClass('acctNotes');
            } else {
                $('#noteTypeA').attr("checked", false);
                document.getElementById("noteTypeA").className = "black"; 
            }
            if (noteSymbol === 'A' && noteSymbolList.toString().indexOf("A") !== -1) {
                $('#noteTypeAi').attr("checked", true);
                setClass('airNotes');
            } else {
                $('#noteTypeAi').attr("checked", false);
                document.getElementById("noteTypeAi").className = "black"; 
            }
            if (noteSymbol === 'D' && noteSymbolList.toString().indexOf("D") !== -1) {
                $('#noteTypeD').attr("checked", true);
                setClass('docNotes');
            } else {
               $('#noteTypeD').attr("checked", false);
                document.getElementById("noteTypeD").className = "black"; 
            }
            if (noteSymbol === '*' && noteSymbolList.toString().indexOf("*") !== -1) {
                $('#noteTypeAu').attr("checked", true);
                setClass('autoNotes');
            } else {
                 $('#noteTypeAu').attr("checked", false);
                 document.getElementById("noteTypeAu").className = "black";
            }
            if (noteSymbol === 'G' && noteSymbolList.toString().indexOf("G") !== -1) {
                $('#noteTypeG').attr("checked", true);
                setClass('generalNotes');
            } else {
                $('#noteTypeG').attr("checked", false);
                document.getElementById("noteTypeG").className = "black";
            }
            if (noteSymbol === 'F' && noteSymbolList.toString().indexOf("F") !== -1) {
                $('#noteTypeF').attr("checked", true);
                setClass('fclNotes');
            } else {
                $('#noteTypeF').attr("checked", false);
                document.getElementById("noteTypeF").className = "black";
            }
            if (noteSymbol === 'I' && noteSymbolList.toString().indexOf("I") !== -1) {
                $('#noteTypeI').attr("checked", true);
                setClass('importsNotes');
            } else {
                $('#noteTypeI').attr("checked", false);
                document.getElementById("noteTypeI").className = "black";
            }
            if (noteSymbol === 'L' && noteSymbolList.toString().indexOf("L") !== -1) {
                $('#noteTypeL').attr("checked", true);
                setClass('lclNotes');
            } else {
                $('#noteTypeL').attr("checked", false);
                document.getElementById("noteTypeL").className = "black";
            }
            if (noteSymbol === 'S' && noteSymbolList.toString().indexOf("S") !== -1) {
                $('#noteTypeS').attr("checked", true);
                setClass('salesNotes');
            } else {
                $('#noteTypeS').attr("checked", false);
                document.getElementById("noteTypeS").className = "black";
            }
            if (noteSymbol === 'W' && noteSymbolList.toString().indexOf("W") !== -1) {
                $('#noteTypeW').attr("checked", true);
                setClass('whseNotes');
            } else {
                $('#noteTypeW').attr("checked", false);
                document.getElementById("noteTypeW").className = "black";
            }
            if (noteSymbol === 'V' && noteSymbolList.toString().indexOf("V") !== -1) {
                $('#noteTypeV').attr("checked", true);
                setClass('voidNotes');
            } else {
                 $('#noteTypeV').attr("checked", false);
                document.getElementById("noteTypeV").className = "black";
            }
        }
        
        function disableChecking(noteSymbol){
           if (noteSymbol.toString().indexOf("$") === -1) {
                $('#noteTypeA').attr("checked", false);
                document.getElementById("noteTypeA").className = "black";
            }
            if (noteSymbol.toString().indexOf("A") === -1) {
                $('#noteTypeAi').attr("checked", false);
                document.getElementById("noteTypeAi").className = "black";
            }
            if (noteSymbol.toString().indexOf("D") === -1) {
                $('#noteTypeD').attr("checked", false);
                document.getElementById("noteTypeD").className = "black";
            }
            if (noteSymbol.toString().indexOf("*") === -1) {
                $('#noteTypeAu').attr("checked", false);
                document.getElementById("noteTypeAu").className = "black";
            }
            if (noteSymbol.toString().indexOf("G") === -1) {
                $('#noteTypeG').attr("checked", false);
                document.getElementById("noteTypeG").className = "black";
            }
            if (noteSymbol.toString().indexOf("F") === -1) {
                $('#noteTypeF').attr("checked", false);
                document.getElementById("noteTypeF").className = "black";
            }
            if (noteSymbol.toString().indexOf("I") === -1) {
                $('#noteTypeI').attr("checked", false);
                document.getElementById("noteTypeI").className = "black";
            }
            if (noteSymbol.toString().indexOf("L") === -1) {
                $('#noteTypeL').attr("checked", false);
                document.getElementById("noteTypeL").className = "black";
            }
            if (noteSymbol.toString().indexOf("S") === -1) {
                $('#noteTypeS').attr("checked", false);
                document.getElementById("noteTypeS").className = "black";
            }
            if (noteSymbol.toString().indexOf("W") === -1) {
                $('#noteTypeW').attr("checked", false);
                document.getElementById("noteTypeW").className = "black";
            }
            if (noteSymbol.toString().indexOf("V") === -1) {
                $('#noteTypeV').attr("checked", false);
                document.getElementById("noteTypeV").className = "black";
            }  
        }
        function setClass(id) {
            $('#' + id).removeClass("greenBold15px");
            $('#' + id).addClass("blueBold15px");
        }

        function doSortAscDesc(ele) {
            var noteTypeSymbol = $("#noteSymbol").val();
            $("#orderBy").val(ele);
            $("#sortBy").val();
            $("#noteSymbol").val(noteTypeSymbol);
            $('#methodName').val('displayNotes');
            $('#blueScreenNotesForm').submit();
        }
        
        function showBlock(tar) {
        $(tar).show(300);
        }
         function tpEditNote(id, noteDescSub, noteTypeSub) {
            showBlock('#notesTable');
            $("#tpSave").hide();
            $("#tpUpdate").show();
            $("#tpId").val(id);
            $("#noteTypeSub").val(noteTypeSub);
            $("#noteDescSub").val(noteDescSub);
            document.getElementById("noteTypeSub").disabled=true;
        }
        
        function deleteTpNote(txt, id,noteDesc) {
            var path = "/" + window.location.pathname.split('/')[1];
            $("#tpId").val(id);
            $("#noteDescSub").val(noteDesc);
            $('#methodName').val('deleteTpNote');
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        var params = $("#blueScreenNotesForm").serialize();
                        $.ajaxx({
                            url: path + "/bluescreenCustomerNotes.do",
                            data: params,
                            preloading: true,
                            success: function (data) {
                                $("#notesTable").hide();
                                $("#sortBy").val('');
                                $("#methodName").val("displayNotes");
                                $("#blueScreenNotesForm").submit();
                            }
                        });
                    }
                    else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        }
    </script>
</html>
