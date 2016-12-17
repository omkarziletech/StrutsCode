<div id="confirmOnBoard" style="display:none;width:650px;height:200px;">
 	   <table class="tableBorderNew" cellpadding="2" cellspacing="0"  width="100%">
         <tr class="tableHeadingNew"><font style="font-weight: bold">Confirm On Board</font></tr>
		 <tr class="textlabelsBold">
                     <td>
                         <table>
                             <tr>
                                 <td class="textlabelsBold" style=" border-left: red 2px solid;">Confirm on Board </td>
                             </tr>
                         </table>
                     </td>

            <td>Y<html:radio property="confirmOnBoard" name="transactionBean" value="Y"
                    styleId="confirmOnBoardNo" onclick="displayHeading()"/>&nbsp;&nbsp;
	    		N<html:radio property="confirmOnBoard" name="transactionBean" value="N"
                    styleId="confirmOnBoardYes" onclick="displayHeading()"/></td>
			<td>ETD</td>
			<td>
	    		<fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${fclBl.sailDate}"/>
                        <input size="10" class="BackgrndColorForTextBox" value="${sailDate}" id="etdValue" readonly="readonly"  tabindex="-1"/>
			</td>
			<td>ETA</td>
			<td>
				<fmt:formatDate pattern="dd-MMM-yyyy" var="eta" value="${fclBl.eta}"/>
				<input size="10" class="BackgrndColorForTextBox" value="${eta}" id="etaValue" readonly="readonly"  tabindex="-1"/>
			</td>
			<td>Verified ETA</td>
			<td style="padding-bottom:10px;">
                <fmt:formatDate pattern="dd-MMM-yyyy" var="verEta" value="${fclBl.verifyETA}" />
                <html:text styleClass="textlabelsBoldForTextBox"  property="verifyEta" value="${verEta}"
                  styleId="txtcal4" size="18" readonly="true" onchange="validateETA()" tabindex="-1"/>
                <img src="${path}/img/CalendarIco.gif" alt="cal" id="cal4" name="VerfiedETADate"
                  onmousedown="insertDateFromCalendar(this.id,3);"  />
                <input type="checkbox" name="verifiedEtaCheck" id="verifiedEtaCheck" onclick="copyEtaDate()" onmouseover="tooltip.show('<strong>Same as ETA</strong>',null,event);" onmouseout="tooltip.hide();"/>

            </td>
	     </tr>
	     <tr>
		    <td class="textlabelsBold" align="left" >Vessel Name</td>
            <td colspan="3">
                <input size="30" class="BackgrndColorForTextBox" value="<%=vesselName%>" readonly="readonly"  tabindex="-1"/></td>
		    <td class="textlabelsBold"  align="right">Voyage</td>
            <td colspan="3">
                <input size="25" class="BackgrndColorForTextBox" value="${fclBl.voyages}" readonly="readonly"  tabindex="-1"/>
                </td>
            <td>
            </td><td></td><td></td><td></td>
	    </tr>
        <tr><td colspan="9" align="left" >
        	<table><tr class="textlabelsBold">
        	         <td valign="top">Comment</td>
        		     <td style="padding-left: 40px;">
        		        <html:textarea property="confOnBoardComments"  styleClass="textlabelsBoldForTextBox" styleId="confOnBoardComments"
        		          value="${fclBl.confOnBoardComments}" rows="3" cols="31" onkeydown="maxLength(this,200)"
        		          style="text-transform: uppercase">
                	</html:textarea></td></tr>
             </table>
       </td></tr>
	    <tr>
		   <td colspan="9" align="center">
	 	      <input type="button" class="buttonStyleNew" value="Save"
	 			onclick="updateFCLBL('${fclBl.bol}','','<%=userName%>','BlConfirm','${importFlag}')" />

	 		  <input class="buttonStyleNew" type="button" id="manifestCancel" value="Cancel"
	 		    onclick="closeChargesPopup()">
		   </td>
	    </tr>
     </table>
   </div>

   <div id="importReleasePopUp" style="display:none;width:500px;height:200px;">
       <table class="tableBorderNew" cellpadding="2" cellspacing="0"  border="0"  width="100%">
         <tr class="tableHeadingNew">Import Release</tr>
		 <tr class="textlabelsBold">
                     <td>
                         <table>
                             <tr class="textlabelsBold">
                                 <td>Doc Release</td>
                                  <td> Y<html:radio property="importRelease" name="transactionBean" value="Y"
                    styleId="importReleaseYes" onclick="checkImportRelease()"/>&nbsp;&nbsp;
	    		N<html:radio property="importRelease" name="transactionBean" value="N"
                    styleId="importReleaseNo" onclick="checkImportRelease()"/>
                                  </td>
                                   <td>
                                       Released On
                                   </td>
                                   <td>
                                       <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="verEta" value="${fclBl.importVerifiedEta}" />
                    <html:text styleClass="textlabelsBoldForTextBox"  property="importVerifiedEta" value="${verEta}"
                      styleId="txtcal99" size="18" onfocus="this.blur()" readonly="true" style="background-color:CCEBFF;" tabindex="-1"/>

                                   </td>
                                    <td>
                                        Comment
                                    </td>
                                    <td>
                                          <html:textarea property="importReleaseComments"  styleClass="textlabelsBoldForTextBox"
                                                  styleId="importCommentsId" value="${fclBl.importReleaseComments}" rows="2" cols="31"
                                                  onkeydown="maxLength(this,200)" style="text-transform: uppercase">
                                          </html:textarea>
                                    </td>
                             </tr>
                             <tr>
                                 <td colspan="6">
                                     <hr/>
                                 </td>
                             </tr>
                             <tr class="textlabelsBold">
                                    <td >Payment Release</td>
                                     <td >Y
                                         <html:radio property="paymentRelease" value="Y" name="transactionBean" styleId="paymentReleaseYes" onclick="checkPaymentRelease()"/>
                                          N<html:radio property="paymentRelease" value="N" name="transactionBean" styleId="paymentReleaseNo" onclick="checkPaymentRelease()"/>

                                     </td>
                                   <td >Released On</td>
                                                    <td style="padding-bottom:2px;">
                                                         <fmt:formatDate pattern="dd-MMM-yyyy" var="payReleasedOn" value="${fclBl.paymentReleasedOn}" />
                                                        <html:text property="paymentReleasedOn" value="${payReleasedOn}"
                                                                   styleClass="textlabelsBoldForTextBox" styleId="txtpayRelCal" size="16"/>
                                                         <img src="${path}/img/CalendarIco.gif" alt="cal" id="payRelCal"
                                                                 onmousedown="insertDateFromCalendar(this.id,3);"  />

                                   </td>

                                     <td >Comment</td>
                                         <td style="padding-left:1px;">
                                              <html:textarea property="paymentReleaseComments"  styleClass="textlabelsBoldForTextBox"
                                              styleId="paymentReleaseComments" value="${fclBl.paymentReleaseComments}" rows="2" cols="31"
                                              onkeydown="maxLength(this,200)" style="text-transform: uppercase">
                                              </html:textarea>
                                          </td>
                                      </tr>
                                <tr  class="textlabelsBold">
                                    <td>Check Number</td>
                                    <td><html:text  property="checkNumber" styleId="checkNumber" value="${fclBl.checkNumber}" styleClass="textlabelsBoldForTextBox" maxlength="20"/></td>
                                    <td>Amount</td>
                                    <td>
                                        <fmt:formatNumber  var="pmtAmount" value="${fclBl.paymentAmount}" pattern="0.00"/>
                                        <html:text property="paymentAmount" styleId="paymentAmount" value="${pmtAmount}" styleClass="textlabelsBoldForTextBox" maxlength="12" onchange="formatNumber(this)"/>
                                    </td>
                                    <td>Paid By</td>
                                    <td><html:text property="amountAndPaidBy" styleId="amountAndPaidBy" value="${fclBl.amountAndPaidBy}" styleClass="textlabelsBoldForTextBox" size="18" maxlength="100" style="text-transform: uppercase"/></td>
                                </tr>
                         </table>
                     </td>
                 </tr>
                 <tr style="display: none;">


			<td style="padding-left:10px;display:none;" >ETD</td>
                        <td style="display:none;">
	    		<fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${fclBl.sailDate}"/>
				<input size="10" class="BackgrndColorForTextBox" value="${sailDate}" readonly="readonly" tabindex="-1"/>
			</td>
			<td style="padding-left:10px;display:none;">ETA</td>
			<td style="display:none;">
				<fmt:formatDate pattern="dd-MMM-yyyy" var="eta" value="${fclBl.eta}"/>
				<input size="10" class="BackgrndColorForTextBox" value="${eta}" id="importEtaValue" readonly="readonly" tabindex="-1"/>
			</td>

	     </tr>
	    <tr>
		   <td colspan="9" align="center">
	 	      <input type="button" class="buttonStyleNew" value="Save"
	 			onclick="updateFCLBL('${fclBl.bol}','','<%=userName%>','BlImportRelease')" />
	 		  <input class="buttonStyleNew" type="button" id="importCancel" value="Cancel"
	 		    onclick="closeImportReleasePopUp()">
		   </td>
	    </tr>
     </table>
   </div>
                          <div id="containerCommentBox" style="display:none;width:300px;height:150px;" align="center">
         <table class="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
             <tr class="tableHeadingNew"> Comments</tr>
           	 <tr>
           		<td align="center">
        		   <html:textarea property="containerComments" style="text-transform: uppercase"
        		    styleClass="textlabelsBoldForTextBox"  cols="30" rows="3"/></td>
           	 </tr>
           	 <tr><td>&nbsp;</td></tr>
           	 <tr>
           	    <td align="center">
           	       <!-- ***** below functions in fclcontainer.jsp ****** -->
           			<input class="buttonStyleNew" type="button" value="Save" onclick="disableThisContainer()">
           			<input class="buttonStyleNew" type="button" value="Cancel" onclick="closeCommentPopUp()">
           		</td>
           	</tr>
        </table>
    </div>
    <div id="editContainerCommentBox" style="display:none;width:300px;height:150px;" align="center">
         <table class="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
             <tr class="tableHeadingNew">Comments</tr>
           	 <tr>
           		<td align="center">
        		   <html:textarea property="tempContainerComments" style="text-transform: uppercase"
        		    styleClass="textlabelsBoldForTextBox" cols="30" rows="3"/></td>
           	 </tr>
           	 <tr><td>&nbsp;</td></tr>
           	 <tr>
           	    <td align="center">
           	       <!-- ***** below functions in fclcontainer.jsp ****** -->
           			<input class="buttonStyleNew" type="button" value="Save" onclick="enableThisContainer()">
           			<input class="buttonStyleNew" type="button" value="Cancel" onclick="closeCommentPopUpForEnable()">
           		</td>
           	</tr>
        </table>
    </div>
    <div id="importCargoLocationDiv" style="display:none;width:300px;height:150px;" align="center">
         <table class="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
             <tr class="tableHeadingNew">Cargo Location</tr>
           	 <tr class="textlabelsBold">
           		<td>Location/Name</td>
                        <td>
                            <input name="importWareHouseName" Class="textlabelsBoldForTextBox" id="exportPositioning"
                                   size="23" maxlength="50"  style="text-transform: uppercase"
                                   value="${fclBl.importWareHouseName}"/>
                            <input type="hidden" Class="textlabelsBoldForTextBox" name="wareHouse_check"
                                   id="wareHouse_check"    value="${fclBl.importWareHouseName}"/>
                            <div id="wareHouse_choices" style="display: none" class="autocomplete"></div>
                            <script type="text/javascript">
                                initAutocompleteWithFormClear("exportPositioning","wareHouse_choices","importWareHouseId","wareHouse_check",
                                "${path}/actions/getWareHouseDetails.jsp?tabName=FCLBL","getWareHouseAdd()","");
                            </script>
                            <input type="hidden" Class="textlabelsBoldForTextBox" name="importWareHouseId" id="importWareHouseId"/>
                            <input type="text" Class="BackgrndColorForTextBox" name="importWareHouseCode"
                                   id="wareHouseCode" value="${fclBl.importWareHouseCode}" size="4" readonly="true"  tabindex="-1"/>
                            <img src="${path}/img/icons/add2.gif" id ="addWareHouse" onclick="return GB_show('Add WareHouse','${path}/jsps/fclQuotes/AddFclBlWareHouse.jsp?wareHouseType=FCLI&screenName=BL',325,730);"/>
                        </td>
           	 </tr>
                 <tr class="textlabelsBold">
                     <td>Name/Address</td>
                    <td>
                        <html:textarea property="importWareHouseAddress" styleClass="textlabelsBoldForTextBox"
                               styleId="importWareHouseAddress" cols="28" rows="3" value="${fclBl.importWareHouseAddress}" style="text-transform: uppercase"
                               onkeypress="return checkTextAreaLimit(this, 200)" >
                        </html:textarea>
                    </td>
                 </tr>
                 <tr class="textlabelsBold">
                        <td>Remarks</td>
                        <td>
                             <html:textarea property="importPickUpRemarks" styleClass="textlabelsBoldForTextBox"
                                   styleId="pickUpRemarks" cols="28" rows="3" style="text-transform: uppercase"
                                   onchange="return limitTextchar(this,200)"
                                   value="${fclBl.importPickUpRemarks}"
                                   onkeypress=" return limitTextchar(this,200)" ></html:textarea>
                        </td>
                    </tr>
           	 <tr align="center">
                     <td colspan="2">
                    <input class="buttonStyleNew" type="button" value="Submit" onclick="fillOnwardInlandRouting()">
                    <input class="buttonStyleNew" type="button" value="Cancel" onclick="closeImportWareHousePopUp()">
                </td>
           	</tr>
        </table>
    </div>

    <div id="contactConfigDetails" style="display:none;">
        <table class="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
            <tr><td style="color: red">${invoiceMessage}</td></tr>
            <c:if test="${not empty ContactConfigE1andE2}">
           <tr class="tableHeadingNew"><font style="font-weight: bold">Freight Invoice Will be sent to</font></tr>
          <tr><td>
             <%int t=0; %>

             <display:table name="${ContactConfigE1andE2}" id="configTableId" class="displaytagstyle" pagesize="<%=pageSize%>">
             <display:setProperty name="basic.msg.empty_list">
				<span style="display:none;" class="pagebanner" />
			 </display:setProperty>
		     <display:setProperty name="paging.banner.placement" >
		       <span style="display:none;"></span>
		     </display:setProperty>

              <c:choose>
                  <c:when test="${not empty configTableId.accountNo}">
                       <display:column title="AccountName" property="accountName"></display:column>
                       <display:column title="AccountNo" property="accountNo"></display:column>
	               <display:column title="AccountType">${configTableId.accountType}
                           <c:if test="${not empty configTableId.subType}">
                               (${configTableId.subType})
                           </c:if>
                       </display:column>
	               <display:column title="FirstName" property="firstName"></display:column>
	               <display:column title="LastName" property="lastName"></display:column>
	               <display:column title="Email" property="email"></display:column>
	               <display:column title="Fax" property="fax"></display:column>
	               <display:column title="CodeK" property="codek.code"></display:column>
                       <display:column title="Action">
                           <c:choose>
                               <c:when test="${configTableId.accountingSelected}">
                                   <input type="checkbox" name="freightInvoiceContactIds" value="${configTableId.id}" checked disabled/>
                               </c:when>
                               <c:otherwise>
                                   <input type="checkbox" name="freightInvoiceContactIds" value="${configTableId.id}" checked/>
                               </c:otherwise>
                           </c:choose>
                       </display:column>
                  </c:when>
                  <c:otherwise>
                       <display:column title="AccountName" style="font-weight:bolder;background:pink" property="accountName"></display:column>
	               <display:column title="AccountNo" style="font-weight:bolder;background:pink" property="accountNo"></display:column>
	               <display:column title="AccountType" style="font-weight:bolder;background:pink">
                           ${configTableId.accountType}${configTableId.subType}
                           <c:if test="${not empty configTableId.subType}">
                               (${configTableId.subType})
                           </c:if>
                       </display:column>
	               <display:column title="FirstName" style="font-weight:bolder;background:pink" property="firstName"></display:column>
	               <display:column title="LastName" style="font-weight:bolder;background:pink" property="lastName"></display:column>
	               <display:column title="Email" style="font-weight:bolder;background:pink" property="email"></display:column>
	               <display:column title="Fax" style="font-weight:bolder;background:pink" property="fax"></display:column>
	               <display:column title="CodeK" style="font-weight:bolder;background:pink">BookingEmail</display:column>
                  </c:otherwise>
              </c:choose>
             </display:table>
          </td></tr>
          <tr><td>&nbsp;</td>
          </tr>
              <tr>`
                     <td align="center">
                      <input class="buttonStyleNew" type="button" value="Ok" onclick="sendFrieghtInvoice()">
<!--                    <input class="buttonStyleNew" type="button" value="Do Not Send" onclick="doNotSendFrieghtInvoice()">-->
                     </td>
              </tr>
          </c:if>
          <c:if test="${empty ContactConfigE1andE2}">
              <tr>
                     <td align="center">
                       <input class="buttonStyleNew" type="button" value="OK" onclick="frieghtInvoiceWillNotSent()">
                     </td>
              </tr>
          </c:if>
        </table>
    </div>