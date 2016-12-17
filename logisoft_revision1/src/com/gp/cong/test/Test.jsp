<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>New Document</TITLE>
		<META NAME="Generator" CONTENT="EditPlus">
		<META NAME="Author" CONTENT="">
		<META NAME="Keywords" CONTENT="">
		<META NAME="Description" CONTENT="">
		<script>
  var globrefnum=0;
var globsysnum=0;
var globlocnum=0;
var globextnum=0;
var MAX_REF_NUMBER=5;
  function createRefRow(divtype){
	globrefnum = globsysnum;
   
    if(globrefnum < MAX_REF_NUMBER){
		globrefnum++;
		var date_var="t_date"+divtype+""+globrefnum;
    	var refrowHTML =  
        	'<table name="'+divtype+'" cellpadding="3" cellspacing="0" border="0" width="95%" align="center">' + 
			'<tr valign="top">' +
			'<td width="200"><input type="text" name="t_refby'+divtype+''+globrefnum+'" id="t_refby'+divtype+''+globrefnum+'" value="{referredBy}" size="25"/></td>';

		if(divtype=='sys') refrowHTML = refrowHTML + '<td width="200"><input type="text" id="t_secby'+divtype+''+globrefnum+'" name="t_secby'+divtype+''+globrefnum+'" value="{status}" size="22"/></td>';
        else refrowHTML = refrowHTML + '<td width="200"><textarea id="t_contact'+divtype+''+globrefnum+'" name="t_contact'+divtype+''+globrefnum+'" rows="2" colw="15" OnKeyDown="limitTextArea(this,500);" onKeyUp="limitTextArea(this,500);" ></textarea></td>';

		refrowHTML = refrowHTML + '<td width="200"><input type="text" id="t_date'+divtype+''+globrefnum+'" name="t_date'+divtype+''+globrefnum+'" readonly="true" value="{referralDate}" size="12"/><a href="javascript:void(0)" onclick="if(self.gfPop)gfPop.fPopCalendar(document.userform.'+date_var+');return false;" ><img class="PopcalTrigger" align="absmiddle" src="calbtn.gif" width="25" height="25" border="0" alt=""/></a></td>'+
	  		'<td width="200"><input type="text" id="t_status'+divtype+''+globrefnum+'" name="t_status'+divtype+''+globrefnum+'" value="{status}" size="25"/></td>' +
           	'<td width="10"><a href="javascript:removeRefRow('+globrefnum+',\''+divtype+'\');">Delete</a></td>' +
			'</tr>' +
			'</table>' +
            '<div id="'+divtype+'_div'+parseInt(globrefnum+1)+'"></div>';

		document.getElementById(divtype+"_div"+globrefnum).innerHTML = refrowHTML;      

    	globsysnum = globrefnum;
         onload();
	}
    else{
    	alert('Only ' + MAX_REF_NUMBER +' number of referrence rows are allowed at one time.');
    }
}
function removeRefRow(thisrefrow,divtype){

	globrefnum = globsysnum;

	if(globrefnum > thisrefrow) {alert('Please remove the last row first');}
    else{
		document.getElementById(divtype+"_div"+thisrefrow).innerHTML = "";
   		globrefnum = globrefnum - 1;
	    //needed because for some reason its not setting to 0, so set it explicitly
    	if(globrefnum==0) globrefnum = 0;
   	}
globsysnum = globrefnum;
}
  </script>
	</HEAD>

	<BODY>
		
<a href="#" onclick="createRefRow('test');">Add Row</a>
<div id="test_div1"></div>
	</BODY>

</HTML>
