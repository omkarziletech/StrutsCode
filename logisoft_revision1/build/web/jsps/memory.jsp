<%java.lang.Runtime rt = java.lang.Runtime.getRuntime();
int mbSize = 1024*1024;
%>
<style>

th, td{
    padding:1px;
}

table{
    font:12px Arial,Tahoma,Helvetica,FreeSans,sans-serif;
    margin: 0;
    padding: 0;
}

table caption{
    text-align:left;
    padding:4px 10px;
}
/*== style3 *****/
.table-sty3{
    width:98%;
    margin: 0px 1%;
}

.table-sty3 caption{
    background:#d1d1d1 url("../../images/blueTemplate/bg_header1.jpg") repeat-x;
    border-bottom: 1px solid #dddddd;
    font-weight:bold;
    color:#053f5e;
}

.table-sty3 a {
    color: #c75f3e;
}

.table-sty3 td {
    background: #fff;
    color:#154A5F;
}

.border{
    border: solid 1px blue;
}

/****************/
.input-sty{
    float:left;
}

.text, select,textarea{
    color: #353535;
    font: 11px Arial,Tahoma,Helvetica,FreeSans,sans-serif;
    border:1px solid #bcbcbc;
}
.text, .select{
    width:150px;
    float: left;
}

.display-table .text{
    width:90%;;
}
select.small-sel{
    width:50px;
    font-size:9px;
    line-height:10px;
}

.text-small{
    width:63px !important;
}
.text-medium{
    width:80px !important;
}

.select-small{
    width:63px !important;
}

input .submit{
    width:100px;
}

.datepicker .date {
    padding: 0 0 0 20px;
}
input.date {
    width:132px !important;
}
table label{
    width:100px;
    float:left;
}

table, td{
    /*vertical-align:top;*/
}

.label110 label{
    width: 110px;
}


.borderL{
    border-left: solid 2px #d1d1d1;
}

label.label130,.label130 label{
    width: 130px;
}
label.label160,.label160 label{
    width: 160px;
}
/************* DISPLAY TABLE ***************/
.display-table{
    width:100%;
    font-size: 11px;
    border-collapse:separate;
}

.display-table th a{
    color: #383838;
}
.table{
    width: 99%;
    margin: 0.5%;
}

.table th, .display-table th{
    color: #383838;
    text-align: left;
    height: 25px;
    text-align: center;
    background: transparent url(../../images/blueTemplate/table_header_bg.png) repeat-x scroll 0 0;
}

.display-table td{
    padding:3px;
    margin: 0;
}
table .odd td, li.odd{
    background-color:#D1E6EE;
}

table .even td,li.even{
    background-color:#fff ;
}

.display-table td{
    width:auto;
    vertical-align: middle;
}

.display-table td input{
    font-size:10px;
    float: left;
}

span.pagebanner{
    float: left;
    width: auto;
    margin: 0 0 0 10px;
}

span.pagelinks{
    float: right;
    width: auto;
    margin: 0 10px 0 0;
}

/************* Container ***************/

table.container{
    margin:3px 0 0 0;
    width:100%;
    border-collapse: collapse;
    float: left;
}
.container-top{
    width: 99%;
    margin:0 0 0 0.5%;
    background: #fff url(../../images/blueTemplate/white-top-left.gif) no-repeat top left;
    height: 2px;
    line-height: 2px;
}

.container-top-right{
    width: 100%;
    background:#FFF url(../../images/blueTemplate/white-top-right.gif) no-repeat top right;
    height: 2px;
    line-height: 2px;
}

.container-mid{
    width: 99%;
    background: #fff;
    margin: 0 0 0 0.5%;
}

.container-bottom{
    width: 99%;
    background: #fff url(../../images/blueTemplate/white-bottom-left.gif) no-repeat bottom left;
    height: 2px;
    margin: 0 0 4px 0.5%;
    line-height: 2px;
}

.container-bottom-right{
    width: 100%;
    background:#FFF url(../../images/blueTemplate/white-bottom-right.gif) no-repeat bottom right;
    height: 2px;
    line-height:2px;
}

.two-table{
    margin-left:0;
    width: 100%;
}

table.three-column label{
    width:150px;
}
table.three-column td{
    width:33%;
}

.no-hover td{
    background-color: transparent !important;
    color: #000 !important;
}

table.rates td{
    height:10px;
    margin:0;
    padding: 2px 4px 0;

}
table.rates td input{
    vertical-align: middle;
}

/************* LAYOUT *********/
.ten{
    width: 10%;
}
.twenty{
    width: 20%;
}
.thirty{
    width: 30%;
}
.fourty{
    width: 40%;
}
.fifty{
    width: 50%;
}
.sixty{
    width: 60%;
}
.seventy{
    width: 70%;
}
.eighty{
    width: 80%;
}
.ninty{
    width: 90%;
}
.hundred{
    width: 100%;
}
</style>
<div class="table-con font11 bold">
    <table style="width:100%">
        <thead>
            <tr><th colspan="2">Heap utilization statistics</th></tr>
            <tr class="odd"><td>Total Memory</td><td><%=rt.totalMemory()/1048576%> mb</td></tr>
            <tr><td>Max Memory</td><td><%=rt.maxMemory()/1048576%> mb</td></tr>
            <tr class="odd"><td>Used Memory</td><td><%=(rt.totalMemory() - rt.freeMemory())/mbSize%> mb</td></tr>
            <tr><td>Free Memory</td><td><%=rt.freeMemory()/1048576%> mb</td></tr>
        </thead>
    </table>
    <%
      long free = (rt.freeMemory() * 100) / rt.totalMemory();
      request.setAttribute("free", free);
    %>
    <table width="200px" cellspacing="0" cellpadding="0" style="border:solid 1px black" title="Free Memory ${free}%">
        <tbody>
            <tr>
                <td style="background-color: red; line-height: 4px">&nbsp;</td>
                <td style="width:${free}%;background-color: chartreuse;line-height: 4px">&nbsp;</td>
            </tr>
        </tbody>
    </table>
</div>