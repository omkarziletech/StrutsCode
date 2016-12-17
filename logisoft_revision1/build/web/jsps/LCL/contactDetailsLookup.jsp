
<%@include file="init.jsp" %>
<body>
    <cong:form name="lclContactDetailsForm" id="lclContactDetailsForm" action="/lclContactDetails.do">
        <cong:hidden name="methodName" id="methodName"/>
        <table width="100%" cellpadding="0" cellspacing="0" class="tableHeadingNew">
            <tr><td>List of Codes</td>
        </table>
        <display:table name="${genericCodeList}" id="code" pagesize="50"  class="dataTable" sort="list" requestURI="/lclContactDetails.do" >
            <display:setProperty name="paging.banner.some_items_found">
                <span class="pagebanner">
                    <font color="blue">{0}</font> Receivables displayed,For more Records click on page numbers.
                </span>
            </display:setProperty>
            <display:setProperty name="paging.banner.one_item_found">
                <span class="pagebanner">One {0} displayed. Page Number</span>
            </display:setProperty>
            <display:setProperty name="paging.banner.all_items_found"><span class="pagebanner">{0} {1} Displayed, Page Number</span>
            </display:setProperty>
            <display:setProperty name="basic.msg.empty_list">
            </display:setProperty>
            <display:setProperty name="paging.banner.placement" value="bottom" />
            <display:setProperty name="paging.banner.item_name" value="code"/>
            <display:setProperty name="paging.banner.items_name" value="codes"/>
            <display:column title="code" property="code"></display:column>
            <display:column title="Description"><a href="" id="codedesc" onclick="selectcontactDetail('${code.code}','${code.field1}','${code.id}');">${code.codedesc}</a></display:column>
        </display:table>
    </cong:form>
    <script type="text/javascript">
        function selectcontactDetail(codea,field,id){
            if(field=='A'){
                window.opener.document.lclContactDetailsForm.codea.value=codea;
               // window.opener.document.lclContactDetailsForm.codeAId.value=id;
            }else if(field=='B'){
                window.opener.document.lclContactDetailsForm.codeb.value=codea;
               // window.opener.document.lclContactDetailsForm.codeBId.value=id;
            }else if(field=='C'){
                window.opener.document.lclContactDetailsForm.codec.value=codea;
               // window.opener.document.lclContactDetailsForm.codeCId.value=id;
            }else if(field=='D'){
                window.opener.document.lclContactDetailsForm.coded.value=codea;
                // window.opener.document.lclContactDetailsForm.codeDId.value=id;
            }else if(field=='E'){
                window.opener.document.lclContactDetailsForm.codee.value=codea;
                 //window.opener.document.lclContactDetailsForm.codeEId.value=id;
            }else if(field=='F'){
                window.opener.document.lclContactDetailsForm.codef.value=codea;
                 //window.opener.document.lclContactDetailsForm.codeFId.value=id;
            }else if(field=='G'){
                window.opener.document.lclContactDetailsForm.codeg.value=codea;
                 //window.opener.document.lclContactDetailsForm.codeGId.value=id;
            }else if(field=='H'){
                window.opener.document.lclContactDetailsForm.codeh.value=codea;
                 //window.opener.document.lclContactDetailsForm.codeHId.value=id;
            }else if(field=='I'){
                window.opener.document.lclContactDetailsForm.codei.value=codea;
                 //window.opener.document.lclContactDetailsForm.codeIId.value=id;
            }else if(field=='J'){
                window.opener.document.lclContactDetailsForm.codej.value=codea;
            }else if(field=='K'){
                window.opener.document.lclContactDetailsForm.codek.value=codea;
            }
            window.close();
        }
        
    </script>
</body>