<%-- 
    Document   : header
    Created on : Feb 27, 2014, 6:44:53 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="table no-margin-border">
    <tr>
        <th>Fiscal Year</th>
    </tr>
    <tr>
        <td>
            <span class="label">Choose Year</span>
            <span>
                <html:select property="fiscalYear.year" styleId="year" styleClass="dropdown" onchange="searchYear();">
                    <html:optionsCollection property="fiscalYears" label="year" value="year"/>
                </html:select>
            </span>
            <span class="label margin-0-0-0-10">Starting Period</span>
            <span>
                <html:text property="fiscalYear.startPeriod" styleId="startPeriod" readonly="true" styleClass="textbox width-40px readonly"/>
            </span>
            <span class="label margin-0-0-0-10">Ending Period</span>
            <span>
                <html:text property="fiscalYear.endPeriod" styleId="endPeriod" readonly="true" styleClass="textbox width-40px readonly"/>
            </span>
            <span class="label margin-0-0-0-10">Budget Set</span>
            <span>
                <html:select property="budgetSet" styleId="budgetSet" styleClass="dropdown">
                    <html:option value="1"/>
                    <html:option value="2"/>
                    <html:option value="3"/>
                    <html:option value="4"/>
                </html:select> 
            </span>
            <span class="margin-0-0-0-10">
                <input type="button" class="button" value="Search Year" onclick="searchYear();"/>
                <input type="button" class="button" value="Create Year" onclick="createYear();"/>
                <input type="button" class="button" value="Close Year" onclick="closeYear();"/>
                <input type="button" class="button" value="Create Budget" onclick="createBudget();"/>
                <input type="button" class="button" value="Import Budget" onclick="importBudget();"/>
                <input type="button" class="button" value="Trial Balance" onclick="createTrialBalance();"/>
                <input type="button" class="button" value="Income Statement" onclick="createIncomeStatement();"/>
            </span>
        </td>
    </tr>
</table>