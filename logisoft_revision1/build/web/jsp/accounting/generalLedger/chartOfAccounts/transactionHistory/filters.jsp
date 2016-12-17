<%-- 
    Document   : filters
    Created on : Jun 2, 2014, 9:18:06 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<div style="display: ${toggleStyle}" class="filter-container">
    <table class="table" style="margin: 0; border-spacing: 0; border: none;">
        <tr>
            <td class="label width-100px">Starting Account</td>
            <td>
                <html:text property="startAccount" styleId="startAccount" styleClass="textbox"/>
                <input type="hidden" id="startAccountCheck" value="${chartOfAccountsForm.startAccount}"/>
            </td>
            <td class="label width-100px">Ending Account</td>
            <td>
                <html:text property="endAccount" styleId="endAccount" styleClass="textbox"/>
                <input type="hidden" id="endAccountCheck" value="${chartOfAccountsForm.endAccount}"/>
            </td>
        </tr>
        <tr>
            <td class="label width-100px">Starting Period</td>
            <td>
                <html:text property="startPeriod" styleId="startPeriod" styleClass="textbox"/>
                <input type="hidden" id="startPeriodCheck" value="${chartOfPeriodsForm.startPeriod}"/>
            </td>
            <td class="label width-100px">Ending Period</td>
            <td>
                <html:text property="endPeriod" styleId="endPeriod" styleClass="textbox"/>
                <input type="hidden" id="endPeriodCheck" value="${chartOfPeriodsForm.endPeriod}"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="align-center">
                <input type="button" value="Search" class="button" onclick="search();"/>
                <input type="button" value="Clear" class="button" onclick="clear();"/>
                <input type="button" value="Add New" class="button" onclick="add();"/>
                <input type="button" value="Export to Excel" class="button" onclick="exportToExcel();"/>
            </td>
        </tr>
    </table>
</div>