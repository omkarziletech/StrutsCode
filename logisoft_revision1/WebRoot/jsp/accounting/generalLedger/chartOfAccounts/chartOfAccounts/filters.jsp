<%-- 
    Document   : filters
    Created on : Jun 2, 2014, 6:10:19 PM
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
            <td class="label">Account Type</td>
            <td>
                <html:select property="accountType" styleId="accountType" styleClass="dropdown">
                    <html:option value="">Select</html:option>
                    <html:options property="accountTypes"/>
                </html:select>
            </td>
            <td class="label">Account Group</td>
            <td>
                <html:select property="accountGroup" styleId="accountGroup" styleClass="dropdown">
                    <html:option value="">Select</html:option>
                    <html:options property="accountGroups"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label">Account Status</td>
            <td>
                <html:radio property="accountStatus" styleId="accountStatus1" value="Active"/>
                <label for="accountStatus1" class="label">Active</label>
                <html:radio property="accountStatus" styleId="accountStatus2" value="Inactive"/>
                <label for="accountStatus2" class="label">Inactive</label>
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