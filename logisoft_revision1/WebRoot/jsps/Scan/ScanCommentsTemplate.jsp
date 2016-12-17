<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<table width="100%"
       style="background-image: url(/logisoft/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;" height="15px">
    <tbody>
        <tr><c:if test="${action=='Scan' || action=='Attach'}">
                <td class="lightBoxHeader"><c:out value="${fileNumber}  -  ${documentName}"/></td>
            </c:if>
            <c:if test="${action=='uploadCSV' || action=='uploadFile' || action=='uploadGLMapping'}">
                <td class="lightBoxHeader">Upload File</td>
            </c:if>
            <td class="lightBoxHeader">
                <div style="vertical-align: top">
                    <a id="lightBoxClose" href="javascript:closeAttachList();">
                        <img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
        </tr>
    </tbody>
</table>
<table width="100%" class="textLabels" align="center">
    <c:if test="${action=='Scan' || action=='Attach'}">
        <tr>
            <td valign="middle" align="right">Comment
                <c:if test="${screenName!='INVOICE' && screenName!='AR BATCH' && screenName!='JOURNAL ENTRY'}">
                    <font color="red" size="+2">*</font>
                    </c:if>
            </td>
            <td><textarea rows="2" cols="30" id="comment" style="text-transform: uppercase" onblur="validateMaxLength(this, 250)"></textarea></td>
        </tr>
    </c:if>
    <c:if test="${action=='Scan'}">
        <c:if test="${documentName =='SS LINE MASTER BL'}">
            <tr align="center">
                <td>
                    <input align="right" type="radio" value="Approved" name="ssMasterStatus" id="ssApproved"/>Approved
                </td>
                <td>
                    <input align="right" type="radio" value="Disputed" name="ssMasterStatus" id="ssDisputed" />Disputed
                </td>
            </tr>
        </c:if>
        <tr>
            <td align="center" colspan="2">
                <input type="button" name='Scan' class="buttonStyleNew" value="Scan"
                       style='width: 60px' onclick="saveScan('${documentName}');"/>
                <input type="button" name='Cancel' class="buttonStyleNew" value='Cancel'
                       style='width: 50px' onclick=" closeAttachList();" />
            </td>
        </tr>
    </c:if>
    <c:if test="${action=='Attach'}">
        <tr>
            <td align="right">File Name</td>
            <td align="left">
                <input type="file" id="theFile" size="25" />
            </td>
        </tr>
        <c:if test="${documentName =='SS LINE MASTER BL'}">
            <tr>
                <td>
                    <input type="radio" value="Approved" name="ssMasterStatus" id="ssApproved"/>Approved
                </td>
                <td>
                    <input type="radio" value="Disputed" name="ssMasterStatus" id="ssDisputed" />Disputed
                </td>
            </tr>
            <tr>
            </c:if>
        <tr>
            <td align="center" colspan="2">
                <input type="button" name='Attach' value="Attach" class="buttonStyleNew"
                       style='width: 60px' onclick="copyFile('${documentName}')"/>
                <input type="button" name="dragAndDrop" value="Drag & Drop" class="buttonStyleNew" onclick="saveScan('${index}', 'Attach')"/>
                <input type="button" name='Cancel' class="buttonStyleNew" value='Cancel'
                       style='width: 50px' onclick=" closeAttachList();" />
            </td>
        </tr>
    </c:if>
    <c:if test="${action=='uploadCSV' || action=='uploadFile' || action=='uploadGLMapping'}">
        <tr>
            <td align="right">File Name</td>
            <td align="left">
                <input type="file" id="theFile" size="25" style="cursor: pointer;"/>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="2">
                <input type="button" name='Upload' value="Upload" class="buttonStyleNew"
                       style='width: 60px' onclick="copyFile()"/>
                <input type="button" name='Cancel' class="buttonStyleNew" value='Cancel'
                       style='width: 50px' onclick=" closeAttachList();" />
            </td>
        </tr>
    </c:if>
</table>
