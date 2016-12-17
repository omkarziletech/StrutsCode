<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>BatchList</title>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript">
            function save(){
                var result = true;
                if(document.newBatchForm.totalCredit.value!=document.newBatchForm.totalDebit.value){
                    result=confirm("Batch debits and credits are out of balance.  Do you want to Save anyway?");
                }
                if(result){
                    document.newBatchForm.buttonValue.value="save";
                    document.newBatchForm.submit();
                }
            }
            function goBack(){
                document.newBatchForm.buttonValue.value="cancel";
                document.newBatchForm.submit();
            }
            function checkPost(obj){
                if(obj.checked){
                    document.newBatchForm.status.value="ready to post";
                }else{
                    document.newBatchForm.status.value="open";
                }
            }
        </script>
    </head>

    <body class="whitebackgrnd">
        <html:form action="/newBatch" name="newBatchForm" type="com.gp.cvst.logisoft.struts.form.NewBatchForm" scope="request">
            <html:hidden property="buttonValue"/>
            <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="7">Enter Batch Details</td>
                    <td align="right">
                        <input type="button" onclick="goBack()"  value="Go Back" class="buttonStyleNew"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Batch Number</td>
                    <td><html:text property="batchno" readonly="true" styleClass="textlabelsBoldForTextBox"></html:text></td>
                    <td>Description</td>
                    <td><html:text property="desc" styleClass="textlabelsBoldForTextBox"></html:text></td>
                    <td>Source Ledger</td>
                    <td>
                        <html:select property="sourceLedger" styleClass="dropdown_accounting">
                            <html:optionsCollection name="sourceLedgers" styleClass="unfixedtextfiledstyle" />
                        </html:select>
                    </td>
                    <td>Type</td>
                    <td>
                        <html:select property="type" styleClass="dropdown_accountingF">
                            <html:optionsCollection name="typeList" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Total Credit </td>
                    <td><html:text property="totalCredit" styleClass="textlabelsBoldSForTextBox"/></td>
                    <td>Total Debit</td>
                    <td><html:text property="totalDebit" styleClass="textlabelsBoldForTextBox"/></td>
                    <td>Ready to Posted</td>
                    <td><html:checkbox property="post" onclick="checkPost(this)"/></td>
                    <td>Status</td>
                    <td><html:text property="status" readonly="true" styleClass="textlabelsBoldForTextBox"/></td>
                </tr>
                <tr>
                    <td colspan="8" align="center" style="padding-top:10px;">
                        <input type="button" onclick="save()" value="Save" class="buttonStyleNew"/>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>

