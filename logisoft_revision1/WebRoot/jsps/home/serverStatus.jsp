<%-- 
    Document   : serverStatus
    Created on : Mar 27, 2012, 6:48:50 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div style="width:98%" class="table-con font11 bold">
    <table class="display-table">
        <thead>
            <tr><th colspan="2">Server Status</th></tr>
	</thead>
	<tbody>
            <tr class="odd"><td>Max Memory</td><td>${maxMemory} mb</td></tr>
            <tr><td>Total Memory</td><td>${totalMemory} mb</td></tr>
	    <tr class="odd"><td>Used Memory</td><td>${usedMemory} mb</td></tr>
            <tr><td>Free Memory</td><td>${freeMemory} mb</td></tr>
        </tbody>
    </table>
    <table class="display-table" cellspacing="0" cellpadding="0" style="border:solid 1px black" title="Free Memory ${freePercentage}%">
        <tbody>
            <tr>
                <td style="background-color: red; line-height: 3px">&nbsp;</td>
                <td style="width:${freePercentage}%;background-color: chartreuse;line-height: 3px">&nbsp;</td>
            </tr>
        </tbody>
    </table>
</div>