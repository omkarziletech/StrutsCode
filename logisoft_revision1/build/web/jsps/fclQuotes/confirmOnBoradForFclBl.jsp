
<table class="tableBorderNew" cellpadding="2" cellspacing="0"  width="100%" >
                            <tr class="tableHeadingNew"> Confirm On Board</tr>
	<tr><td>Confirm on Borad </td><td>Y<input type="redio"/>X<input type="redio"/></td>
	<td>ETD</td>	
	<td>
	<fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${fclBl.sailDate}" />
		<c:out value="${sailDate}"/>
	</td>
	<td>ETA</td>	
	<td>
	<fmt:formatDate pattern="dd-MMM-yyyy" var="eta" value="${fclBl.eta}" />
		<c:out value="${eta}"/>
	</td>
	<td>Varify ETA</td>	
	<td>
		<td style="padding-bottom:10px;">
                                    <fmt:formatDate pattern="dd-MMM-yyyy" var="eta" value="${fclBl.eta}" />
                                    <html:text styleClass="textlabelsBoldForTextBox" property="eta"
                                               value="" styleId="txtcal4" size="10" readonly="true"/>
                                    <img src="<%=path%>/img/CalendarIco.gif" alt="cal" id="cal4"
                                         onmousedown="insertDateFromCalendar(this.id,4);"  />
                                </td>
	</td>
	</tr>
	<tr>
		<td class="textlabelsBold" align="left">Vessel Name</td>
                                <td><%=vesselName%></td>
		<td class="textlabelsBold"  align="right">Vessel</td>
                                <td><%=vessel%> </td>
                                <td></td><td></td><td></td><td></td>
	</tr>
	<tr>
	<td colspan="2" align="center">
	<input class="buttonStyleNew" type="button" value="cancel" onclick="closeChargesPopup()">
	 <input type="button" class="buttonStyleNew" value="Save"  onclick="SaveCorrections()" />
	</td>
	</tr>
</table>