<%-- 
    Document   : batch
    Created on : 26 Jun, 2015, 4:11:41 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<table class="table">
    <tr>
        <td class="label">Batch No</td>
        <td>
            <input type="text" name="batchId" id="batchId" value="${apPaymentForm.batchId}" class="textbox readonly" readonly/>
        </td>
    </tr>
    <tr>
        <td class="label">Batch Desc</td>
        <td>
            <textarea name="batchDesc" id="batchDesc" rows="2" cols="30" class="textarea"></textarea>
        </td>
    </tr>
    <tr>
        <td colspan="2" class="align-center">
            <input type="button" class="button" value="Save" onclick="savePayment();"/>
            <input type="button" class="button" value="Cancel" onclick="cancelBatch();"/>
        </td>
    </tr>
</table>