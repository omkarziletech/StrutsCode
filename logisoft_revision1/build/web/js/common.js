
// common JavaScript Document
var searchcriteria;
var searchresults;
var todayDate = new Date();
function changeButtonImageOnMouseOver(obj,path,name,c)
{
	
    if(c==1)
    {
        obj.src=path+"imgs/buttons/"+name+"_roll.gif";
    }
    else if(c===0)
    {
        obj.src=path+"imgs/buttons/"+name+".ginitAutocompleteWithFormClearif";
    }
} 

function popupListofAddress()
{
    mywindow=window.open ("List of Address.html","ListofAddress","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=410");
    mywindow.moveTo(250,250);
}

function popupDRcosts()
{
    mywindow=window.open ("DRcostspopup.html","DR Costs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=500,height=200");
    mywindow.moveTo(250,250);
}

function popupCopyBLFromBooking()
{
    mywindow=window.open ("vieweditBookings.html","DR Costs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=800,height=500");
    mywindow.moveTo(100,100);
}

function popupCopyBLFromBL()
{
    mywindow=window.open ("viewmbl.html","DR Costs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=800,height=500");
    mywindow.moveTo(100,100);
}

function popupBookingConfirmation()
{
    mywindow=window.open ("booking_conf_ATL.pdf","DR Costs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=800,height=500");
    mywindow.moveTo(100,100);
}

function popupFCLbuycharge()
{
    mywindow=window.open ("FCLbuycharge.html","FCL buy charge","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=670,height=180");
    mywindow.moveTo(200,200);
}

function popupFCLsellcharge()
{
    mywindow=window.open ("FCLFCLsellcharge.html","FCL sell charge","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=670,height=180");
    mywindow.moveTo(200,200);
}

function popupoceanaccessorialcharges()
{
    mywindow=window.open ("oceanaccessorialcharges.html","Ocean accessorial charges","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=770,height=200");
    mywindow.moveTo(200,200);
}

function popupAiraccessorialcharges()
{
    mywindow=window.open ("Airaccessorialcharges.html","Air accessorial charges","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=770,height=200");
    mywindow.moveTo(200,200);
}

function popupTracking()
{
    mywindow=window.open ("trackingpopup.html","Tracking","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=530,height=170");
    mywindow.moveTo(250,250);
}

function popupHistory()
{
    mywindow=window.open ("historypopup.html","history","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=700,height=210");
    mywindow.moveTo(250,250);
}

function copyDR()
{
    alert("The Current DR is being Copied  to new DR. Only the Voyage Details are not carried over");
}

function popupInvoiceDR()
{
    mywindow=window.open ("invoicefromDR.html","Invoice From DR","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=520,height=170");
    mywindow.moveTo(250,250);
}
function popupContainerSummary()
{
    mywindow=window.open ("ContainerSummary.html","ContainerSummary","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=470");
    mywindow.moveTo(250,150);
}

function popupAssociatedvoyages()
{
    mywindow=window.open ("associatedvoyages.html","ContainerSummary","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=450,height=350");
    mywindow.moveTo(250,200);
}

function popupListofAddressBL()
{
    mywindow=window.open ("../List of Address.html","ListofAddress","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=410");
    mywindow.moveTo(250,250);
}

function popupListofAddress1()
{
    mywindow=window.open ("List of Address.html","ListofAddress","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=410");
    mywindow.moveTo(250,250);
}

function ListofAddressDM()
{
    mywindow=window.open ("../List of Address.html","ListofAddress","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=410");
    mywindow.moveTo(250,250);
}

function popupCDrive()
{
    mywindow=window.open ("file:///C:/","CDrive","width=700,height=500,scrollbars=yes");
    mywindow.moveTo(150,150);
}

function popupDrCharges()
{
    mywindow=window.open ("DRchargespopup.html","DRchargespopup","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=420,height=150");
    mywindow.moveTo(250,250);
}

function popupvoyage()
{
    mywindow=window.open ("Voyagedetails.html","Voyagedetails","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=750,height=450");
    mywindow.moveTo(150,150);
}

function showpopupvoyage()
{
    mywindow=window.open ("../Voyagedetails.html","Voyagedetails","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=750,height=450");
    mywindow.moveTo(150,150);
}

function popupAddToVoyage()
{
    mywindow=window.open ("addvoyagepopup.html","addvoyagepopup","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=370,height=200");
    mywindow.moveTo(250,250);
}

function popupScheduleB()
{
    mywindow=window.open ("AES_SCHEDULE_B.html","AES_SCHEDULE_B","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=500,height=400");
    mywindow.moveTo(250,250);
}

function insertDateFromCalendar(idButton,c){
    var idInputField = "txt"+idButton;
    if(c==0){
        Calendar.setup(
        {
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%m/%d/%Y",    // the date format
            button      : idButton       // ID of the button
        }
        );
    } else if(c==1){
        Calendar.setup({
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%m/%d/%Y %I:%M %p",    // the date format
            button      : idButton       // ID of the button
          
        }
        );
    }else if(c==2){
        Calendar.setup({
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%m/%d/%Y %H:%M ",    // the date format
            button      : idButton       // ID of the button
        }
        );
    }else if(c==3){
        Calendar.setup({
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%d-%b-%Y",    // the date format
            button      : idButton       // ID of the button
        }
        );
    }
    else if(c==4){
        Calendar.setup({
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%d-%b-%Y %H:%M %p",    // the date format
            button      : idButton       // ID of the button
        
        }
        );
    }else if(c==5){
        Calendar.setup({
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%d-%b-%Y %H:%M",    // the date format
            button      : idButton       // ID of the button
        }
        );
    }else if(c==6){
        Calendar.setup({
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%d-%b-%Y %H:%M:%S",    // the date format
            button      : idButton       // ID of the button
        }
        );
    }else if(c==7){
        Calendar.setup({
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%Y-%m-%d",    // the date format
            button      : idButton       // ID of the button
        }
        );

    }else if(c==8){
        Calendar.setup({
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%d/%m/%Y",    // the date format
            button      : idButton       // ID of the button
        }
        );
    }else if(c==9){
        Calendar.setup(
        {
            inputField  : idInputField,         // ID of the input field
            ifFormat    : "%m/%d/%Y %H:%M %p",    // the date format
            button      : idButton       // ID of the button
        }
        );
    }
}

function openpopupsearchcriteria()
{
    searchcriteria=window.open("popup/searchcriteria.html", 'Search Criteria','width=400,height=380,left=410, top=0,resize=yes,resizable=yes,scrollbars=yes,menubar=no,status=no');

} 


function openpopupsearchresults()
{
    searchresults=window.open("searchresult.html", 'Search Results','width=1000,height=250,left=610, top=502,resize=yes,resizable=yes,scrollbars=yes,menubar=no,status=no');
} 

function closeallpopus()
{
    self.close();
    opener.close();
}
 
function closepopupListofAddress()
{
    self.close();
}	


function openpopupnotes()
{
    mywindow=window.open("NotesScreen.html", 'Notes','width=670,height=400,left=590, top=102,resize=yes,resizable=yes,scrollbars=yes,menubar=no,status=no');
    mywindow.moveTo(250,150);
} 

function popupnotes()
{
    mywindow=window.open("../NotesScreen.html", 'Notes','width=700,height=555,left=590, top=102,resize=yes,resizable=yes,scrollbars=yes,menubar=no,status=no');
    mywindow.moveTo(100,100);
} 

function closepopupnotes()
{
    self.close();
}	

 

function popupvoyagecontmgmt()
{
    mywindow=window.open ("Voyagedetails.html","Voyagedetails","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=750,height=450");
    mywindow.moveTo(150,150);
}
function closepopupvoyage()
{
    self.close();
}

function closepopupAddToVoyage()
{
    self.close();
}
	
function openpopupvoyagehist()
{
    mywindow = window.open("voyagechnghistpopup.html", 'Voyagehist','width=500,height=230,left=550, top=102,resize=yes,resizable=yes,scrollbars=yes,menubar=no,status=no');
    mywindow.moveTo(250,250);
}

function popupConfirmOSD()
{
    mywindow = window.open("ConfirmOSDpopup.html", 'Voyagehist','width=400,height=230,left=550, top=102,resize=yes,resizable=yes,scrollbars=yes,menubar=no,status=no');
    mywindow.moveTo(250,250);
}

function popupvoyagehist()
{
    mywindow = window.open("../voyagechnghistpopup.html", 'Voyagehist','width=500,height=230,left=550, top=102,resize=yes,resizable=yes,scrollbars=yes,menubar=no,status=no');
    mywindow.moveTo(250,250);
}

function closepopupvoyagehist()
{
    self.close();
}

function toggleDiv(id,flagit) 
{
	
    if (flagit=="1")
    {
        if (document.layers)
            document.layers[''+id+''].visibility = "show"
        else if (document.all)
            document.all[''+id+''].style.visibility = "visible"
        else if (document.getElementById)
            document.getElementById(''+id+'').style.visibility = "visible"
    }
    else if (flagit=="0")
    {
        if (document.layers)
            document.layers[''+id+''].visibility = "hide"
        else if (document.all)
            document.all[''+id+''].style.visibility = "hidden"
        else if (document.getElementById)
            document.getElementById(''+id+'').style.visibility = "hidden"
    }
}

function toggleTable(id,flagit) 
{
	
    if (flagit=="1")
    {
        if (document.layers)
            document.layers[''+id+''].visibility = "show"
        else if (document.all)
            document.all[''+id+''].style.visibility = "visible"
        else if (document.getElementById)
            document.getElementById(''+id+'').style.visibility = "visible"
    }
    else if (flagit=="0")
    {
        if (document.layers)
            document.layers[''+id+''].visibility = "hide"
        else if (document.all)
            document.all[''+id+''].style.visibility = "hidden"
        else if (document.getElementById)
            document.getElementById(''+id+'').style.visibility = "hidden"
    }
}
	
function popupsealdetails()
{
	
    mywindow=window.open ("sealdetailspopup.html","Seal Details","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=300,height=260");
    mywindow.moveTo(250,250);
}

function popupsearchDR()
{
	
    mywindow=window.open ("searchDR/viewandreleaseDR.html","Search For DR","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=740,height=480");
    mywindow.moveTo(100,100);
}

function closepopup()
{
    self.close();
}

function popdeliveryReport()
{
    mywindow=window.open ("../../Reports/deliveryrep.html","Search For DR","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=680,height=480");
    mywindow.moveTo(100,100);
}

function popupassociat()
{
    closepopup();
    mywindow=window.open("popupassociate.html","Associate Unit To Voyage","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=350,height=90");
    mywindow.moveTo(100,100);
}

function associatedelete()
{
    alert("Cannot Delete Since DR loaded");
}

function popupdateunit()
{
    mywindow=window.open("UpdateUnit.html","Update Unit","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=330,height=90");
    mywindow.moveTo(250,250);
}

function popupinsurance()
{
    mywindow=window.open("../../htmls/LCL/housebl/insurance.html","Insurance ?","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=290,height=70");
    mywindow.moveTo(250,250);
}

function viewDRfromHBL()
{
    mywindow=window.open("viewandreleaseDR.html","Search DR's","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=750,height=520");
    mywindow.moveTo(150,150);
}

function displayDR()
{
    self.close();
    mywindow=window.open("../displaynewexistingdrbking.html","Search DR's","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=850,height=450");
    mywindow.moveTo(150,150);
}

function printpopup()
{
    self.close();
    mywindow=window.open("../print/drprintlabel.html","Print Preferences","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=370");
    mywindow.moveTo(250,250);
}

function printDRLabel()
{
    self.close();
    mywindow=window.open("../Reports/printlabel.html","Print Preferences","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=700,height=400");
    mywindow.moveTo(250,250);
}

function alertnotes()
{
    alert("****Notes Exist for this Dock Receipt!****");
}

function popupDrViewoceanUnit()
{
    mywindow=window.open("viewoceanunitdetails.html","View Ocean Unit","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=300,height=200");
    mywindow.moveTo(250,250);
}

function popupinlandViewUnit()
{
    mywindow=window.open("viewinlandunitdetails.html","View Inland Unit","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=300,height=150");
    mywindow.moveTo(250,250);
}

function popupcostsdetailsdips()
{
    mywindow=window.open("chargedetails.html","Costs Details","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=325,height=340");
    mywindow.moveTo(250,250);
}

function pophouseblcharges()
{
    cwindow=window.open("charges.html","Charge Details","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=620,height=280");
    cwindow.moveTo(250,250);
}

function popupmarksandnumbers()
{
    cwindow=window.open("markspcsdetails.html","Marks and Pcs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=940,height=180");
    cwindow.moveTo(50,200);
}

function popupviewmasterbl()
{
    cwindow=window.open("../masterbl/viewmbl.html","Marks and Pcs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=820,height=470");
    cwindow.moveTo(100,100);
}
function popupcreatemasterbl()
{
    cwindow=window.open("../masterbl/masterblFCL.html","Marks and Pcs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=840,height=400");
    cwindow.moveTo(100,100);
}

function popupstrippintally()
{
    cwindow=window.open("../Inlandreceivings/Shiptally.html","Marks and Pcs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=840,height=500");
    cwindow.moveTo(100,100);
}

function popupcreateinlandvoyage()
{
    cwindow=window.open("../Inlandreceivings/createinlandvoyage.html","Marks and Pcs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=740,height=400");
    cwindow.moveTo(100,100);
}

function popupimportsdr()
{
    cwindow=window.open("drimports.html","Marks and Pcs","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=840,height=600");
    cwindow.moveTo(100,100);
}

function printhazmatpopup()
{
    self.close();
    mywindow=window.open("../../print/drprintlabel.html","Print Preferences","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=370");
    mywindow.moveTo(250,250);
}

function openAirFrame()
{
    var yes=confirm("Do you want to switch to the AIR Module");
    if(yes)
        window.top.location="../../AIRFramesPage.html";
    else
        return;
}

function openLCLFrame()
{
    var yes=confirm("Do you want to switch to the LCL Module");
    if(yes)
        window.top.location="../../LCLFramesPage.html";
    else
        return;
}

function openFCLFrame()
{
    var yes=confirm("Do you want to switch to the FCL Module");
    if(yes)
        window.top.location="../../FCLFramesPage.html";
    else
        return;
}

function openImportsFrame()
{
    var yes=confirm("Do you want to switch to the IMPORTS Module");
    if(yes)
        window.top.location="../../IMPORTSFramesPage.html";
    else
        return;
}

function popupquotecharges()
{
    mywindow=window.open ("quotecharges.html","Charges","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=120");
    mywindow.moveTo(200,200);
}

function popupquoteinsurance()
{
    mywindow=window.open ("insurance.html","Charges","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=400,height=120");
    mywindow.moveTo(200,200);
}

function popupFCLBLConsolidations()
{
    cwindow=window.open("FCLblladingpart2.html","FCL COnsolidations","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=600,height=430");
    cwindow.moveTo(100,100);
}

function popupFCLBLmarkscontdetails()
{
    cwindow=window.open("FCLblmarksandnumbers.html","FCL COnsolidations","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=790,height=430");
    cwindow.moveTo(100,100);
}

function popupFCLBLAutoNotification()
{
    cwindow=window.open("FCLblautonotification.html","FCL COnsolidations","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=750,height=430");
    cwindow.moveTo(100,100);
}

function popupMNTNCEAddressdtls()
{
    cwindow=window.open("AddressDetails.html","FCL COnsolidations","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=620,height=480");
    cwindow.moveTo(100,100);
}

function popupMNTNCENotificationconfig()
{
    cwindow=window.open("defaultinstruction.html","FCL COnsolidations","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=720,height=280");
    cwindow.moveTo(100,100);
}

function openAirTopFrame()
{
    var yes=confirm("Do you want to switch to the AIR Module");
    if(yes)
        parent.header.location="../topair.html";
    else
        return;
}

function popupinbondentry()
{
    cwindow=window.open("inbondentrypopup.html","InBond","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=220,height=180");
    cwindow.moveTo(100,100);
}

function FCLBLcosts()
{
    cwindow=window.open("FCLBLcosts.html","InBond","titlebar=0,status=0,toolbar=0,menubar=0,resizable=0,width=600,height=480");
    cwindow.moveTo(100,100);
}

function DateCompare2 (startDate, endDate)
{
		
		   
		   
    var startddindex = startDate.indexOf('/');
    var startdd = startDate.substring(0,startddindex);
		   
    var startmmindex = startDate.lastIndexOf('/');
    var startmm = startDate.substring(startddindex+1,startmmindex);
    var startyear = startDate.substring(startmmindex+1,startDate.length);
		   
    var endddindex = endDate.indexOf('/');
    var enddd = endDate.substring(0,endddindex);
		   
    var endmmindex = endDate.lastIndexOf('/');
		  
    var endmm = endDate.substring(endddindex+1,endmmindex);
    var endyear = endDate.substring(endmmindex+1,endDate.length);
    var date1=new Date(startmm+"/"+startdd+"/"+startyear);
    var date2=new Date(endmm+"/"+enddd+"/"+endyear);
		  
    //date1.setFullYear(startyear,startmm-1,startdd);
    //date2.setFullYear(endyear,endmm-1,enddd);
    //var date1  = new Date(date1Str);
    //var date2  = new Date(date2Str);
    //
		
    if (date1 < date2)
    {
        return true;
    }
    else
    {
        return false;
    }
		
}
function isValid(parm,val) 
{
    for (i=0; i<parm.length; i++)
    {
        if (val.indexOf(parm.charAt(i),0) == -1) return false;
    }
    return true;
}
function isSpecial(parm) 
{
    var iChars = "!@#$%^&*()+=[]\\\';,./{}|\":<>?";

    for (var i = 0; i < parm.length; i++) {
        if (iChars.indexOf(parm.charAt(i)) != -1)  return false;
  	
    }
    return true;
}
function isSpecialExceptPeriod(parm) 
{
    var iChars = "!@#$%^&*()+=-_[]~`\\\';,/{}|\":<>?";

    for (var i = 0; i < parm.length; i++) {
        if (iChars.indexOf(parm.charAt(i)) != -1)  return false;
  	
    }
    return true;
}
function isSpecialExceptAsteric(parm)
{
    var iChars = "!@#$%^&()+=-[]\\\';,./{}|\":<>?";

    for (var i = 0; i < parm.length; i++) {
        if (iChars.indexOf(parm.charAt(i)) != -1)  return false;

    }
    return true;
}
function isAlpha(parm) 
{
    var iChars = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?_";

    for (var i = 0; i < parm.length; i++) {
        if (iChars.indexOf(parm.charAt(i)) != -1)  return false;
  	
    }
    return true;
}

/*function DateCompare2 (date1Str, date2Str)
   {
		var date1  = new Date(date1Str);
		var date2  = new Date(date2Str);
		//
		if (date1 < date2)
		{
   			return true;
		}
		else 
		{
   			return false;
		}
  }
*/



/**function noRightClick() { 
if (event.button==2) {
alert('Right click is disabled!') 
} 
} 

document.onmousedown=noRightClick 
**/ 

function IsNumeric(strString) {
    var strValidChars = "()0123-456789";
    var strChar;
    var blnResult = true;
    if (strString.length == 0) return true;
    for (i = 0; i < strString.length && blnResult == true; i++) {
        strChar = strString.charAt(i);
        if (strValidChars.indexOf(strChar) == -1) {
            blnResult = false;
        }
    }
    return blnResult;
} 

function IsCalender(strString) {
    var strValidChars = "0123456789-/:";
    var strChar;
    var blnResult = true;
    if (strString.length == 0) return true;
    for (i = 0; i < strString.length && blnResult == true; i++) {
        strChar = strString.charAt(i);
        if (strValidChars.indexOf(strChar) == -1) {
            blnResult = false;
        }
    }
    return blnResult;
} 

function IsPassword(parm) 
{
    var iChars = "!@#$%^&*()+=-[]\\\';,/{}|\":<>?";

    for (var i = 0; i < parm.length; i++) {
        if (iChars.indexOf(parm.charAt(i)) != -1)  return false;
  	
    }
    return true;
}
function changeColor(event){
    var newValue = event.getNewValue();
    var src = event.getSource();
    if(newValue != null && IsNumeric(newValue)){

        src.setContentStyle('background-color:yellow;');

        src.setInlineStyle('background-color:yellow;');

    } else {

        src.setContentStyle('background-color:white;');

        src.setInlineStyle('background-color:white;');

    }

}
var n;
var p;
var p1;
function ValidatePhone(m){
    p=p1.value
    if(p.length==3){

        pp=p;
        d4=p.indexOf('(')
        d5=p.indexOf(')')
        if(d4==-1){
            pp="("+pp;
        }
        if(d5==-1){
            pp=pp+")";
        }
	
        m.value="";
        m.value=pp;
    }
    if(p.length>3){
        d1=p.indexOf('(')
        d2=p.indexOf(')')
        if (d2==-1){
            l30=p.length;
            p30=p.substring(0,4);
		
            p30=p30+")"
            p31=p.substring(4,l30);
            pp=p30+p31;
		
            m.value="";
            m.value=pp;
        }
    }
    if(p.length>5){
        p11=p.substring(d1+1,d2);
        if(p11.length>3){
            p12=p11;
            l12=p12.length;
            l15=p.length

            p13=p11.substring(0,3);
            p14=p11.substring(3,l12);
            p15=p.substring(d2+1,l15);
            m.value="";
            pp="("+p13+")"+p14+p15;
            m.value=pp;
	
        }
        l16=p.length;
        p16=p.substring(d2+1,l16);
        l17=p16.length;
        if(l17>3&&p16.indexOf('-')==-1){
            p17=p.substring(d2+1,d2+4);
            p18=p.substring(d2+4,l16);
            p19=p.substring(0,d2+1);
		
            pp=p19+p17+"-"+p18;
            m.value="";
            m.value=pp;
	
        }
    }

}
function getIt(m){
    n=m.name;

    p1=m
    ValidatePhone(m)
}
function testphone(obj1){
    p=obj1.value

    p=p.replace("(","")
    p=p.replace(")","")
    p=p.replace("-","")
    p=p.replace("-","")

    if (isNaN(p)==true){
        alert("Check phone");
        return false;
    }
}
// zip function
function getzip(m){
    n=m.name;
    p1=m
// ValidateZip(m)
}
function ValidateZip(m){
    p=p1.value
    if(p.length==5){
        pp=p;
        d4=p.indexOf("-")
        if(d4==-1){
            pp=pp+"-";
        }
        m.value="";
        m.value=pp;
    }
}
//check
function check(txt,n){
    var val = txt.value;
    if(val.length==n){
        ab=val.indexOf(".")
        if(ab==-1){
            val = val + '.';
        }
        txt.value=val;
    }
}

//checkdec

function checkdec(n){
    var val = n.value;
    var re = /^[0-9]*\.?[0-9]*$/ ;
  
    if(!re.test(val)) {
        alertNew("Enter the proper data");
        n.value="";
        n.select();
    }
}

// titleLetter
function titleLetter(ev)
{
    if(event.keyCode==13)
    {
        window.open("<%=path%>/jsps/datareference/managingCarriersOAT.jsp" + ev ,ev,"toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
    }
}
// get decimal
function getDecimal(m,k,evt)
{
    n=m.name;
    checkIt(evt);
    p1=m;
    ValidateDecimal(m,k);
}
function ValidateDecimal(m,k){
    p=p1.value
    if(p.length==k){

        pp=p;
        d4=p.indexOf(".")
        if(d4==-1){
            pp=pp+".";
        }
 
 
        m.value="";
        m.value=pp;
    }
}



// get decimals
function getDecimals(m,k,evt){
    n=m.name;
    checkIt(event);
    p1=m
    ValidateDecimals(m,k)
}
function ValidateDecimals(m,k){
    p=p1.value

    if(p.length<k){
        pp=p;
        d4=p.indexOf(".")
        if(d4==-1){
            pp=pp+".";
        }
        m.value="";
        m.value=pp;
    }
}
// get decimal1
function getDecimal1(m,evt){
    n=m.name;
    checkIt(evt);
    p1=m
    ValidateDecimal1(m)
 
}
function ValidateDecimal1(m){
    p=p1.value
    if(p.length<1){

        pp=p;
        d4=p.indexOf(".")
        if(d4==-1){
            pp=pp+".";
        }
	
	
        m.value="";
        m.value=pp;
    }
}
// is float
function isFloat(s)
{
    var strValidChars = "0123456789.";
    var strChar;
    var a=0;
    var blnResult = true;
    if (s.length == 0) return true;
    for (i = 0; i < s.length && blnResult == true; i++) {
        strChar = s.charAt(i);
        if (strValidChars.indexOf(strChar) == -1) {
            blnResult = false;
        }
        if(strChar.indexOf(".")==0)
        {
            a++;
        }
    }
    if(a>1)
    {
        blnResult = false;
    }
    return blnResult;
}
 
// FUNCTION TO RESTRICT ALPHABETS AND DECIMALS AS INPUT TO TEXT FIELD
 
function checkIts(evt) {
    evt = (evt) ? evt : window.event
    var charCode = (evt.which) ? evt.which :event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        //status = "This field accepts numbersonly."
        //return false
        alert("This field accepts numbersonly");
        return false;
    }
    status = ""
    return true
}
 
function checkItzero(evt) {
    evt = (evt) ? evt : window.event
    var charCode = (evt.which) ? evt.which :event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
 
        status = "This field accepts numbersonly."
        return false
    }
    status = ""
    return true
}
function checkIt(evt) {
    var charCode = evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        status = "This field accepts numbersonly."
        evt.keyCode="";
        return false
    }
    status= ""
    return true;
}

function commondisable(val1,val2)
{
    if(val1 == 0 || val1== 3)
    {
        var imgs = document.getElementsByTagName('img');
        for(var k=0; k<imgs.length; k++)
        {
            if(imgs[k].id != "cancel" && imgs[k].id!="note")
            {
                imgs[k].style.visibility = 'hidden';
            }
        }
        var input = document.getElementsByTagName("input");
        for(i=0; i<input.length; i++)
        {
            if(input[i].id != "buttonValue")
            {
                //alert("323"+input[i].readOnly);
                input[i].readOnly=true;
	  						
                input[i].className="areahighlightgrey";
            }
	  				
        }
        var textarea = document.getElementsByTagName("textarea");
        for(i=0; i<textarea.length;i++)
        {
            textarea[i].readOnly=true;
	  						
            textarea[i].className="areahighlightgrey";
	 					
        }
        var select = document.getElementsByTagName("select");
        for(i=0; i<select.length; i++)
        {
            select[i].readOnly=true;
	  						
            select[i].className="areahighlightgrey";
        //select[i].disabled = true;
        }
    }
    if(val1 == 1 )
    {
        document.getElementById("delete").style.visibility = 'hidden';
    }
    if(val1==3 && val2!="")
    {
        alert(val2);
    }
//document.editWarehouse.warehouseCode.color="Blue";
}	
//For dwr - ram
function useLogisoftLodingMessage() {
    var loadingImage;
    dwr.engine.setPreHook(function() {
        showPopUp();
        var disabledImageZone = document.getElementById('disabledImageZone');
        if (!disabledImageZone) {
            disabledImageZone = document.createElement('div');
            disabledImageZone.setAttribute('id', 'disabledImageZone');
            disabledImageZone.style.position = "absolute";
            disabledImageZone.style.zIndex = "1000";
            disabledImageZone.style.left = "50%";
            disabledImageZone.style.top = "40%";
            disabledImageZone.style.right = "50%";
            disabledImageZone.style.bottom = "60%";
            //disabledImageZone.style.width = "100%";
            //disabledImageZone.style.height = "100%";
            var imageZone = document.createElement('img');
            imageZone.setAttribute('id','imageZone');
            imageZone.setAttribute('src',"/logisoft/img/icons/ajax-loader.gif");
            imageZone.style.position = "absolute";
            imageZone.style.width="100";
            imageZone.style.height="100";
            disabledImageZone.appendChild(imageZone);
            document.body.appendChild(disabledImageZone);
        }
        else {
            $('imageZone').src = "/logisoft/img/icons/ajax-loader.gif";
            disabledImageZone.style.visibility = 'visible';
        }
    });
    dwr.engine.setPostHook(function() {
        closePopUp();
        document.getElementById('disabledImageZone').style.visibility = 'hidden';
    });
}
	
	
function showPopUp() {
    var cover = document.getElementById("cover");
    cover.style.display = "block";
    cover.style.width = document.body.scrollWidth;
    cover.style.height = document.body.scrollHeight;
    cover.style.opacity = 0.5;
    cover.style.filter = "alpha(opacity="+50+")";
}
function closePopUp() {
    var cover = document.getElementById("cover");
    cover.style.display = "none";
}
	
//new processing message for dwr
	
	
function useLogisoftLodingMessageNew() {
    var loadingImage;
    dwr.engine.setPreHook(function() {
        showPopUp();
        if(document.getElementById('newProgressBar')!=null && document.getElementById('newProgressBar')!=undefined){
            document.getElementById('newProgressBar').style.display='block';
        }
	   
    });
    dwr.engine.setPostHook(function() {
        closePopUp();
        if(document.getElementById('newProgressBar')!=null && document.getElementById('newProgressBar')!=undefined){
            document.getElementById('newProgressBar').style.display='none';
        }
    });
}	
	
//FORMATTING FUNCTION FOR UNIT NUMBER IN FCLBL Container tab.
function formatTrialNo(val){
    if (event.keyCode != 8 && event.keyCode != 46){
        var str;
        var middleValue;
        var outputValue;
        val.value = val.value.replace(/\s/g,'');
        var inputValue=val.value;
        if(inputValue.length<5 && isNotAlphabetic(inputValue)){
            val.value="";
            alertNew("Please enter alphabetic value");
            return;
        }
        if(inputValue.length==4){
            str=inputValue+"-";
        }
        if(inputValue.length>5){
            str=inputValue.substring(0,5);
            middleValue=inputValue.substring(5,11);
            if(!isInteger(middleValue)){
                alertNew("Please enter numeric value");
            }else{
                str=str+middleValue;
            }
            if(inputValue.length==11){
                str=str+"-";
            }
        }
        if(inputValue.length==13){
            str=inputValue.substring(0,5);
            middleValue=inputValue.substring(5,12);
            outputValue=inputValue.charAt(12);
            if(!isInteger(outputValue)){
                str = inputValue.substring(0,12);
                alertNew("Please enter numeric value");
            }else{
                str=str+middleValue+outputValue;
            }
        //        alert("The data length cannot exceed more than 13");
        }
        if(str!=undefined){
            val.value=str;
        }
    }
}

// check to see if input is alphabetic
function isAlphabetic(val){
    if (val.match(/^[a-zA-Z ]+$/)){
        return true;
    }else{
        return false;
    }
}
function isNotAlphabetic(val){
    if (val.match(/^[a-zA-Z ]+$/)){
        return false;
    }else{
        return true;
    }
}

// check to see if input is alphanumeric
function isAlphaNumeric(val){
    if (val.match(/^[a-zA-Z0-9-s ]+$/)){
        return true;
    }else{
        return false;
    }
}
	
//  returns a trimmed value
function trim(str) {
    return str.replace(/^\s+|\s+$/g, "");
}
	
// check to see if input is number
function isNumber(val) {
    if (isNaN(val))
    {
        return false;
    }
    else
    {
        return true;
    }
}
	
function ascii_value(c) {
    // restrict input to a single character
    c = c . charAt (0);
	
    // loop through all possible ASCII values
    var i;
    for (i = 0; i < 256; ++ i)
    {
        // convert i into a 2-digit hex string
        var h = i . toString (16);
        if (h . length == 1)
            h = "0" + h;
	
        // insert a % character into the string
        h = "%" + h;
	
        // determine the character represented by the escape code
        h = unescape (h);
	
        // if the characters match, we've found the ASCII value
        if (h == c)
            break;
    }
    return i;
}
///converting to uppercase
function toUppercase(obj) {
    obj.value = obj.value.toUpperCase();
}

/**
    * txtName - Input field name
    * choiceName - Div where the result will be populated.
    * txtId - Control needs to be filled up automatically according to txtName.
    * txtCheck - store id as temp variable
    * action - request path
    * update - It is callback function can be used after getting response from server.
    */
function initAutocomplete(txtName,choiceName,txtId,txtCheck,action,update) {
    new Ajax.Autocompleter(txtName, choiceName, action, {
        paramName: txtName,
        tokens:"<-->",
        afterUpdateElement : function (text, li,selectedOption) {
            if($(txtId)!= undefined){
                $(txtId).value = li.id;
            }
            if($(txtCheck)!=undefined){
                $(txtCheck).value = selectedOption;
            }
            $(txtName).blur();
            if(update != null && trim(update) != "") {
                eval(update);
            }
        }
    });
        
    Event.observe(txtName, "blur", function (event){
        var element = Event.element(event);
        if($(txtCheck)!=undefined && element.value!=$(txtCheck).value){
            element.value = '';
            if(txtId != ''){
                $(txtId).value = '';
            }
            $(txtCheck).value = '';
        }
    }
    );
}

function initAutocompleteWithFormClear(txtName,choiceName,txtId,txtCheck,action,update,clear) {
    new Ajax.Autocompleter(txtName, choiceName, action, {
        paramName: txtName,
        tokens:"<-->",
        afterUpdateElement : function (text, li,selectedOption) {
            if($(txtId)!= undefined){
                $(txtId).value = li.id;
            }
            if($(txtCheck)!=undefined){
                $(txtCheck).value = selectedOption;
            }
            $(txtName).blur();
            if(update != null && trim(update) != "") {
                eval(update);
            }
        }
    });

    Event.observe(txtName, "blur", function (event){
        var element = Event.element(event);
        if($(txtCheck)!=undefined && trim($(txtName).value)!=trim($(txtCheck).value)){
            if(txtId != ''){
                $(txtId).value = '';
            }
            $(txtCheck).value = '';
            if(clear != null && trim(clear) != "") {
                eval(clear);
            }
            element.value = '';
        }
    }
    );
}

function initOPSAutocomplete(txtName,choiceName,txtId,txtCheck,action,update) {
    new Ajax.Autocompleter(txtName, choiceName, action, {
        paramName: txtName,
        tokens:"<-->",
        afterUpdateElement : function (text, li,selectedOption) {
            if($(txtId)!= undefined){
                $(txtId).value = li.id;
            }
            if($(txtCheck)!=undefined){
                $(txtCheck).value =selectedOption;
            }
            $(txtName).blur();
            if(update != null && trim(update) != "") {
                eval(update);
            }
        }
    });
}

function updateEncodeUrl(controlArray) {
    var _encodeUrl = "";
    for (key in controlArray){
        var control = document.getElementById(controlArray[key]);
        if(control == undefined){
            alert("Check element Id : " + controlArray[key])
            break;
        }
        _encodeUrl = _encodeUrl + "&" + key + "=" + control.value;
    }
    _encodeUrl = _encodeUrl + "&isDojo=false";
    encodeUrl = _encodeUrl;
}

function appendEncodeUrl(_url) {
    encodeUrl = _url + "&isDojo=false";
}

/**
 * @author Lakshmi Narayanan V
 * @description This AutoCompleter is build based on Ajax AutoCompleter with only one input field and no control field<br>
 * @param textFieldId - Input field name
 * @param divToPopulateId - Div where the result will be populated.
 * @param validateFieldId - Validate Field used to validate the input
 * @param url - request path
 * @param update - It is callback function can be used after getting response from server.
 * @function addMoreParams - used to add more request params. Should be included
 */
function initAjaxAutoCompleter(textFieldId, divToPopulateId, validateFieldId, url, update){       
    new Ajax.Autocompleter(textFieldId, divToPopulateId, url,{
        callback: addMoreParams,
        afterUpdateElement : function (text, li) {
            if(li.id!="No Record"){
                $(textFieldId).value = li.id;
                $(validateFieldId).value = li.id;
                $(textFieldId).blur();
                if(update != null && trim(update) != "") {
                    eval(update);
                }
            }else{
                $(textFieldId).value = "";
                $(validateFieldId).value = "";
                $(textFieldId).focus();
            }
        }
    });
    Event.observe(textFieldId, "blur", function (event){
        var element = Event.element(event);
        if(element.value != $(validateFieldId).value){
            element.value = '';
            $(validateFieldId).value = '';
        }
    });
}

function arAutocomplete(txtName,choiceName,txtId,txtCheck,action,update) {
    new Ajax.Autocompleter(txtName, choiceName, action, {
        paramName: txtName,
        tokens:"<-->",
        afterUpdateElement : function (text, li) {
            $(txtId).value = li.id;
            $(txtCheck).value = text.value;
            $(txtName).blur();
            if(update != null && trim(update) != "") {
                eval(update);
            }
        }
    });
    Event.observe(txtName, "blur", function (event){
        var element = Event.element(event);
        $(txtCheck).value=$(element.id).value;
        if(element.value != $(txtCheck).value){
    //            element.value = '';
    //            $(txtId).value = '';
    //            $(txtCheck).value = '';
    }
    }
    );
}

		
function initAutoCompleteForApplyPayments(txtId,divId) {
    new Autocompleter.DWR(txtId, divId, updateList,{
        valueSelector: nameValueSelector,
        partialChars: 3
    });
 		
}	


//To get autocomplete using DWR 
function initAutoCompleterUsingDWR(txtId,divId,callBack) {
    new Autocompleter.DWR(txtId, divId, updateList,{
        afterUpdateElement: function(){
            $(txtId).blur();
            if(callBack != null && trim(callBack) != "") {
                eval(callBack);
            }
        }
    });
}		
 	
//To Show loading Icon for a Jsp --By pradeep
function showLoadingImg(){
    var disabledImageZone ;
    var cvr = document.getElementById("cover");
    cvr.style.display = "block";
    disabledImageZone = document.createElement('div');
    disabledImageZone.style.position = "absolute";
    disabledImageZone.style.zIndex = "1000";
    disabledImageZone.style.left = "50%";
    disabledImageZone.style.top = "40%";
    disabledImageZone.style.right = "50%";
    disabledImageZone.style.bottom = "60%";
    var imageZone = document.createElement('img');
    imageZone.setAttribute('src',"/logisoft/img/icons/ajax-loader.gif");
    imageZone.style.position = "absolute";
    imageZone.style.width="100";
    imageZone.style.height="100";
    disabledImageZone.appendChild(imageZone);
    document.body.appendChild(disabledImageZone);
    disabledImageZone.style.visibility = 'visible';
	      
}
function closeLoadingImg(){
    var disabledImageZone ;
    var cvr = document.getElementById("cover");
    cvr.style.display = "none";
    disabledImageZone = document.createElement('div');
    disabledImageZone.style.position = "absolute";
    disabledImageZone.style.zIndex = "1000";
    disabledImageZone.style.left = "50%";
    disabledImageZone.style.top = "40%";
    disabledImageZone.style.right = "50%";
    disabledImageZone.style.bottom = "60%";
    var imageZone = document.createElement('img');
    imageZone.setAttribute('src',"/logisoft/img/icons/ajax-loader.gif");
    imageZone.style.position = "absolute";
    imageZone.style.width="100";
    imageZone.style.height="100";
    disabledImageZone.appendChild(imageZone);
    document.body.appendChild(disabledImageZone);
    disabledImageZone.style.visibility = 'hidden';
}

/**DATE VALIDATION METHODS
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
 
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
    var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
    var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
    // February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
    for (var i = 1; i <= n; i++) {
        this[i] = 31
        if (i==4 || i==6 || i==9 || i==11) {
            this[i] = 30
        }
        if (i==2) {
            this[i] = 29
        }
    }
    return this
}


function isDate(dtStr,newYear){
    var daysInMonth = DaysArray(12)
    var pos1=dtStr.indexOf(dtCh)
    var pos2=dtStr.indexOf(dtCh,pos1+1)
    var strMonth=dtStr.substring(0,pos1)
    var strDay=dtStr.substring(pos1+1,pos2)
    var strYear=dtStr.substring(pos2+1)
    strYr=strYear
    if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
    if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
    for (var i = 1; i <= 3; i++) {
        if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
    }
    month=parseInt(strMonth)
    day=parseInt(strDay)
    year=parseInt(strYr)
    if (pos1==-1 || pos2==-1){
        alertNew("The date format should be : mm/dd/yyyy");
        return false;
    }
    if (strMonth.length<1 || month<1 || month>12){
        alertNew("Please enter a valid month");
        return false;
    }
    if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
        alertNew("Please enter a valid day");
        return false;
    }
    //--manually restricting year ,where it shouldnt be less than 2006 (function called from EditBooking.jsp)-----
    if(newYear!=""){
        minYear=newYear;
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            alertNew("Please enter a valid 4 digit year and should not be less than 2006");
            return false;
        }
    }
    if(newYear==""){
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            alertNew("Please enter a valid 4 digit year ");
            return false;
        }
    }
    //--year retriction ends---
    if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
        alertNew("Please enter a valid date");
        return false;
    }
    return true
}

function isValidDate(dtStr,newYear){
    var daysInMonth = DaysArray(12);
    var pos1=dtStr.indexOf(dtCh);
    var pos2=dtStr.indexOf(dtCh,pos1+1);
    var strMonth=dtStr.substring(0,pos1);
    var strDay=dtStr.substring(pos1+1,pos2);
    var strYear=dtStr.substring(pos2+1);
    strYr=strYear;
    if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1);
    if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1);
    for (var i = 1; i <= 3; i++) {
        if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1);
    }
    month=parseInt(strMonth);
    day=parseInt(strDay);
    year=parseInt(strYr);
    if (pos1==-1 || pos2==-1){
        alertNew("The date format should be : MM/DD/YYYY");
        return false;
    }
    if (strMonth.length<1 || month<1 || month >12){
        alertNew("Please Enter A Valid Month");
        return false;
    }
    if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
        alertNew("Please Enter A Valid Day");
        return false;
    }
    //--manually restricting year ,where it shouldnt be less than 2006 (function called from EditBooking.jsp)-----
    if(newYear!=""){
        minYear=newYear;
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            alertNew("Please Enter a Valid 4 Digit Year and Should not be less than 2006");
            return false;
        }
    }
    if(newYear==""){
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            alertNew("Please Enter A Valid 4 Digit Year");
            return false;
        }
    }
    //---year restriction ends---
    if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
        alertNew("Please Enter A Valid Date");
        return false;
    }
    return true;
}
/****DATE VALIDATION ENDS************* */


/*---FUNCTION FOR NEW ALERT BOX ----*/

function DisplayAlert(id,left,top,text,point) {
    document.getElementById(id).style.left=left+'px';
    document.getElementById(id).style.top=top+'px';
    document.getElementById('innerText').innerHTML = text;
    document.getElementById(id).style.display='block';
    document.getElementById(id).style.left=point.x -100 + "px";
    document.getElementById(id).style.top=point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    document.getElementById(id).style.zIndex = '1000';
    grayOut(true,'');
}

function alertNew(text) {
    DisplayAlert('AlertBox',100,50,text,window.center({
        width:100,
        height:100
    }));
}

function alertBoxNew(text) {
    var something = false;
    if(something) {
    // do this section
    }else {
        DisplayAlertBox('AlertBoxOk',100,50,text,window.center({
            width:100,
            height:100
        }));
    }
}
function newAlertBox(text) {
    var something = false;
    if(something) {
    // do this section
    }else {
        DisplayAlertNewBox('AlertOk',100,50,text,window.center({
            width:100,
            height:100
        }));
    }
}
function DisplayAlertNewBox(id,left,top,text,point) {
    document.getElementById(id).style.left=left+'px';
    document.getElementById(id).style.top=top+'px';
    document.getElementById('innerText4').innerHTML = text;
    document.getElementById(id).style.display='block';
    document.getElementById(id).style.left=point.x - 100 + "px";
    document.getElementById(id).style.top=point.y + "px";
    document.getElementById(id).style.zIndex = '1000';
    grayOut(true,'');
}
function DisplayAlertBox(id,left,top,text,point) {
    document.getElementById(id).style.left=left+'px';
    document.getElementById(id).style.top=top+'px';
    document.getElementById('innerText3').innerHTML = text;
    document.getElementById(id).style.display='block';
    document.getElementById(id).style.left=point.x - 100 + "px";
    document.getElementById(id).style.top=point.y + "px";
    document.getElementById(id).style.zIndex = '1000';
    grayOut(true,'');
}

function DisplayConfirm(id,left,top,text,point) {
    document.getElementById(id).style.left=left+'px';
    document.getElementById(id).style.top=top+'px';
    document.getElementById('innerText1').innerHTML = text;
    document.getElementById(id).style.display='block';
    document.getElementById(id).style.left=point.x - 100 + "px";
    document.getElementById(id).style.top=point.y + "px";
    document.getElementById(id).style.zIndex = '1000';
    grayOut(true,'');
}
function confirmNew(text,jam) {
    returnValue=jam;
    var something1 = false;
    if(something1) {
    // do this section
    }
    else {
        DisplayConfirm('ConfirmBox',100,50,text,window.center({
            width:100,
            height:100
        }));
    }
}
function returnConfirmNew(text,jam) {
    returnValue=jam;

    var something1 = false;
    if(something1) {
    // do this section
    }
    else {
        DisplayConfirm('ConfirmBox',100,50,text,window.center({
            width:100,
            height:100
        }));
    }
    confirmMessageFunction(returnValue,"ok");
}
function yes(){
    document.getElementById('ConfirmBox').style.display='none';
    grayOut(false,'');
    confirmMessageFunction(returnValue,"ok");
}
function No(){
    document.getElementById('ConfirmBox').style.display='none';
    grayOut(false,'');
    confirmMessageFunction(returnValue,"cancel");
}
function DisplayConfirmYesOrNo(id,left,top,text,point) {
    document.getElementById(id).style.left=left+'px';
    document.getElementById(id).style.top=top+'px';
    document.getElementById('innerText2').innerHTML = text;
    document.getElementById(id).style.display='block';
    document.getElementById(id).style.left=point.x-100 + "px";
    document.getElementById(id).style.top=point.y + "px";
    document.getElementById(id).style.zIndex = '1000';
    grayOut(true,'');
}
function confirmYesNoCancel(confirmMessage, confirmId) {
    returnValue = confirmId;
    DisplayConfirmYesNoCancel("ConfirmYesNoCancelDiv", 100, 50, confirmMessage,window.center({
        width:100,
        height:100
    }));
}
function confirmOptionYes() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "yes");
}
function confirmOptionNo() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "no");
}
function confirmOptionCancel() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "cancel");
}
function DisplayConfirmYesNoCancel(divId, left, top, confirmMessage,point) {
    document.getElementById(divId).style.left = left + "px";
    document.getElementById(divId).style.top = top + "px";
    document.getElementById("confirmMessagePara").innerHTML = confirmMessage;
    document.getElementById(divId).style.display = "block";
    document.getElementById(divId).style.left=point.x -100 + "px";
    document.getElementById(divId).style.top=point.y + "px";
    document.getElementById(divId).style.zIndex = "1000";
    grayOut(true, "");
}
function confirmYesOrNo(text,jam) {
    returnValue=jam;
    var something1 = false;
    if(something1) {
    // do this section
    }
    else {
        DisplayConfirmYesOrNo('ConfirmYesOrNo',100,50,text,window.center({
            width:100,
            height:100
        }));
    }
}
function alertNewForDefaultValue(text) {
    DisplayDefaultValues('AlertBoxDefaultValues',100,50,text,window.center({
        width:100,
        height:100
    }));
}
function DisplayDefaultValues(id,left,top,text,point) {
    document.getElementById(id).style.left=left+'px';
    document.getElementById(id).style.top=top+'px';
    document.getElementById('innerText7').innerHTML = text;
    document.getElementById(id).style.display='block';
    document.getElementById(id).style.left=point.x-100 + "px";
    document.getElementById(id).style.top=point.y + "px";
    document.getElementById(id).style.zIndex = '1000';
    grayOut(true,'');
}
function confirmYes(){
    document.getElementById('ConfirmYesOrNo').style.display='none';
    grayOut(false,'');
    confirmMessageFunction(returnValue,"yes");
}
function confirmNo(){
    document.getElementById('ConfirmYesOrNo').style.display='none';
    grayOut(false,'');
    confirmMessageFunction(returnValue,"no");
}
function ConfirmNavigateOk(){
    document.getElementById('ConfirmYesOrNo').style.display='none';
    grayOut(false,'');
    confirmNavigateFunction(returnValue,"yes");
}
function ConfirmNavigateCancel(){
    document.getElementById('ConfirmYesOrNo').style.display='none';
    grayOut(false,'');
    confirmNavigateFunction(returnValue,"no");
}

function newConfirmYes(){    
    document.getElementById('ConfirmYesOrNo').style.display='none';
    grayOut(false,'');    
    newConfirmMessageFunction(returnValue,"yes");
}
function newConfirmNo(){    
    document.getElementById('ConfirmYesOrNo').style.display='none';
    grayOut(false,'');    
    newConfirmMessageFunction(returnValue,"no");
}
function newConfirmBoxOk(){
    document.getElementById('AlertBoxOk').style.display='none';
    grayOut(false,'');
    newAlertOkFunction("Ok");
}

/*------ALERT FUNCTION ENDS-----*/

/*--FOR GREY - OUT SCREEN------*/
function grayOut(vis, options) {  
    var options = options || {};
    var zindex = options.zindex || 50;
    var opacity = options.opacity || 70;
    var opaque = (opacity / 100);
    var bgcolor = options.bgcolor || '#000000';
    var dark=document.getElementById('darkenScreenObject');
    if (!dark) {
        var tbody = document.getElementsByTagName("body")[0];
        var tnode = document.createElement('div');
        tnode.style.position='absolute';
        tnode.style.top='0px';
        tnode.style.left='0px';
        tnode.style.overflow='hidden';
        tnode.style.display='none';
        tnode.id='darkenScreenObject';
        tbody.appendChild(tnode);
        dark=document.getElementById('darkenScreenObject');
    }
    if (vis) {
        if( document.body && ( document.body.scrollWidth || document.body.scrollHeight ) ) {
            var pageWidth = document.body.scrollWidth+'px';
            var pageHeight = document.body.scrollHeight+'px';
        } else if( document.body.offsetWidth ) {
            var pageWidth = document.body.offsetWidth+'px';
            var pageHeight = document.body.offsetHeight+'px';
        } else {
            var pageWidth='100%';
            var pageHeight='100%';
        }
        dark.style.opacity=opaque;
        dark.style.MozOpacity=opaque;
        dark.style.filter='alpha(opacity='+opacity+')';
        dark.style.zIndex=zindex;
        dark.style.backgroundColor=bgcolor;
        dark.style.width= pageWidth;
        dark.style.height= pageHeight;
        dark.style.display='block';
    } else {
        dark.style.display='none';
    }
}
/**
 *	@description - Validate Date for AP
 *	@param - date
 *	@example - 12/25/1984. date should be in this format('MM/dd/yyyy')
 *   @author - Lakshmi Narayanan
 */
function isValidDateForAP(date){
    var daysInMonth = DaysArray(12);
    var pos1=date.indexOf(dtCh);
    var pos2=date.indexOf(dtCh,pos1+1);
    var strMonth=date.substring(0,pos1);
    var strDay=date.substring(pos1+1,pos2);
    var strYear=date.substring(pos2+1);
    strYr=strYear;
    if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1);
    if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1);
    for (var i = 1; i <= 3; i++) {
        if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1);
    }
    month=parseInt(strMonth);
    day=parseInt(strDay);
    year=parseInt(strYr);
    if (pos1==-1 || pos2==-1){
        alert("The date format should be : mm/dd/yyyy");
        return false;
    }
    if (strMonth.length<1 || month<1 || month>12){
        alert("Please enter a valid month");
        return false;
    }
    if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
        alert("Please enter a valid day");
        return false;
    }
    if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
        if(strYear.length==2){
            return true;
        }
        alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear);
        return false;
    }
    if (date.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(date, dtCh))==false){
        alert("Please enter a valid date");
        return false;
    }
    return true;
}
//---Function to support .contains() function for Array in java script---
//--refer function populateShipperInfo() in EditBooking.jsp -----
Array.prototype.contains = function (element) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == element) {
            return true;
        }
    }
    return false;
}
//     **********************************
//     * Function to make Div floating 	*
//     * author : Lakshmi Narayanan.V	*
//     **********************************
var ns = (navigator.appName.indexOf("Netscape") != -1);
var d = document;
function floatDiv(id, sx, sy)
{
    var el=d.getElementById?d.getElementById(id):d.all?d.all[id]:d.layers[id];
    var px = document.layers ? "" : "px";
    window[id + "_obj"] = el;
    if(d.layers)el.style=el;
    el.cx = el.sx = sx;
    el.cy = el.sy = sy;
    el.sP=function(x,y){
        this.style.left=x+px;
        this.style.top=y+px;
    };

    el.floatIt=function()
    {
        var pX, pY;
        pX = ns ? pageXOffset : document.documentElement && document.documentElement.scrollLeft ?
        document.documentElement.scrollLeft : document.body.scrollLeft;
        if(this.sx<0)
            pX += ns ? innerWidth : document.documentElement && document.documentElement.clientWidth ?
            document.documentElement.clientWidth : document.body.clientWidth;
        pY = ns ? pageYOffset : document.documentElement && document.documentElement.scrollTop ?
        document.documentElement.scrollTop : document.body.scrollTop;
        if(this.sy<0)
            pY += ns ? innerHeight : document.documentElement && document.documentElement.clientHeight ?
            document.documentElement.clientHeight : document.body.clientHeight;
        this.cx += (pX + this.sx - this.cx)/8;
        this.cy += (pY + this.sy - this.cy)/8;
        this.sP(this.cx, this.cy);
        setTimeout(this.id + "_obj.floatIt()", 40);
    }
    return el;
}
function floatDiv2(id, sx, sy)
{
    var el=d.getElementById?d.getElementById(id):d.all?d.all[id]:d.layers[id];
    var px = document.layers ? "" : "px";
    window[id + "_obj"] = el;
    if(d.layers)el.style=el;
    el.cx = el.sx = sx;
    el.cy = el.sy = sy;

    el.floatIt=function()
    {
        var pX, pY;
        pX = ns ? pageXOffset : document.documentElement && document.documentElement.scrollLeft ?
        document.documentElement.scrollLeft : document.body.scrollLeft;
        if(this.sx<0)
            pX += ns ? innerWidth : document.documentElement && document.documentElement.clientWidth ?
            document.documentElement.clientWidth : document.body.clientWidth;
        pY = ns ? pageYOffset : document.documentElement && document.documentElement.scrollTop ?
        document.documentElement.scrollTop : document.body.scrollTop;
        if(this.sy<0)
            pY += ns ? innerHeight : document.documentElement && document.documentElement.clientHeight ?
            document.documentElement.clientHeight : document.body.clientHeight;
        this.cx += (pX + this.sx - this.cx)/8;
        this.cy += (pY + this.sy - this.cy)/8;
        setTimeout(this.id + "_obj.floatIt()", 40);
    }
    return el;
}

//     **********************************
//     * Function to replace in string	*
//     * author : Lakshmi Narayanan.V	*
//     **********************************
function replaceString(str,strToRemove,repStr) {
    regExp = new RegExp("["+strToRemove+"]","g");
    return str.replace(regExp,repStr);
}

//     ***********************************
//     * Function to create HTML Element *
//     * author : Lakshmi Narayanan.V	 *
//     ***********************************
function createHTMLElement(elementName,elementId,width,height,parentElement) {
    var element = document.createElement(elementName);
    element.id = elementId;
    element.style.width = width;
    element.style.height = height;
    parentElement.appendChild(element);
    return element;
}
function checkForNumber(obj){
    if (isNaN(obj.value)==true){
        alertNew('Please Enter Number '+obj.value+' is not a Number');
        obj.value='';
        obj.focus();
        return false;
    }else{
        obj.value = trim(obj.value);
    }
}
function formatNumber(obj){
    obj.value = obj.value.replace(/,/g,'');
    checkForNumber(obj);
    if(trim(obj.value)!=''){
        obj.value=Number(obj.value).toFixed(2);
    }else{
        obj.value="0.00";
        obj.focus();
    }
}
//-----------------------------------------------------------------------------------
// formatDate (date_object, format)
// Returns a date in the output format specified.
// The format string uses the same abbreviations as in getDateFromFormat()
// ----------------------------------------------------------------------------------
function formatDate(date,format) {
    format=format+"";
    var result="";
    var i_format=0;
    var c="";
    var token="";
    var y=date.getYear()+"";
    var M=date.getMonth()+1;
    var d=date.getDate();
    var E=date.getDay();
    var H=date.getHours();
    var m=date.getMinutes();
    var s=date.getSeconds();
    var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
    // Convert real date parts into formatted versions
    var value=new Object();
    if (y.length < 4) {
        y=""+(y-0+1900);
    }
    value["y"]=""+y;
    value["yyyy"]=y;
    value["yy"]=y.substring(2,4);
    value["M"]=M;
    value["MM"]=LZ(M);
    value["MMM"]=MONTH_NAMES[M-1];
    value["NNN"]=MONTH_NAMES[M+11];
    value["d"]=d;
    value["dd"]=LZ(d);
    value["E"]=DAY_NAMES[E+7];
    value["EE"]=DAY_NAMES[E];
    value["H"]=H;
    value["HH"]=LZ(H);
    if (H==0){
        value["h"]=12;
    }
    else if (H>12){
        value["h"]=H-12;
    }
    else {
        value["h"]=H;
    }
    value["hh"]=LZ(value["h"]);
    if (H>11){
        value["K"]=H-12;
    } else {
        value["K"]=H;
    }
    value["k"]=H+1;
    value["KK"]=LZ(value["K"]);
    value["kk"]=LZ(value["k"]);
    if (H > 11) {
        value["a"]="PM";
    }
    else {
        value["a"]="AM";
    }
    value["m"]=m;
    value["mm"]=LZ(m);
    value["s"]=s;
    value["ss"]=LZ(s);
    while (i_format < format.length) {
        c=format.charAt(i_format);
        token="";
        while ((format.charAt(i_format)==c) && (i_format < format.length)) {
            token += format.charAt(i_format++);
        }
        if (value[token] != null) {
            result=result + value[token];
        }
        else {
            result=result + token;
        }
    }
    return result;
}
//---ENDSWITH() METHOD OF STRING-----
String.prototype.endsWith = function(str){
    return (this.match(str+"$")==str);
}
//----TRIM() METHOD OF STRING-----
String.prototype.trim = function(){
    return this.replace(/^\s+|\s+$/, '');
}	
//text area limit checking with normal alert 
function checkTextAreaLimitNormal(ev,length){
    if(ev.value.length > length-1 && event.keyCode != 8 && event.keyCode != 9){
        alert('More than '+ length +' characters are not allowed');
        var value = ev.value.substring(0,length);
        ev.value = value;
        event.returnValue=false;
        return false;
    }
}
//---TO RESTRICT THE LENGTH OF TEXTAREA------
function testCommentsLength(value,obj,length){
    if(parseInt(value.length)+obj.value.length < length){
    }else{
        alert('More than 500 characters are not allowed');
        return (parseInt(value.length)+obj.value.length < length);
    }
}
function checkTextAreaLimit(ev,length){
    return (ev.value.length < length);
}
function limitTextchar(limitField,limitNum) {
    if (limitField.value.length > limitNum) {
        alertNew('More than '+limitNum+' characters are not allowed');
        limitField.value = limitField.value.substring(0, limitNum).trim();
        return limitField.value;
    } else {
        return limitField.value;
    }
}
//the below 4 functions are only for rate comments in quotes,booking and bl
function imposeMaxLength(Object, MaxLen){
    if(Object.value.length > MaxLen-1 && event.keyCode != 8 && event.keyCode != 9 && event.keyCode != 46){
        alertNew('More than 500 characters are not allowed');
        var value='';
        if(Object.value.length > MaxLen-1 &&  Object.value.length < 500){
            value = Object.value;
        }else{
            value = Object.value.substring(0,MaxLen);
        }
        Object.value = value;
        event.returnValue=false;
        return false;
    }
}
function appendUserInfoForComments(object,userName,time){
    if(object!=undefined){
        if(object.value!=''){
            var comment = object.value.trim();
            if(comment.length <= 460 && comment!=''){
                var val=object.value.trim()+'('+userName+'-'+time+').';
                object.value = val;
            }
        }
    }
}
function getUpperCaseValue(ev){
    ev.value= ev.value.toUpperCase();
}
var tempTextValue='';
function editValidate(textValue,object){
    if(object.value.indexOf(textValue)!=-1){
        tempTextValue = object.value;
    }else{
        alertNew("EDITING PREVIOUS COMMENTS IS NOT ALLOWED.");
        object.value = tempTextValue;
    }
}
//THE ABOVE 4 FUNCTIONS ARE ONLY FOR COMMENTS SECTION INSIDE QUOTATION ,BOOKING AND BL.
/**
 * @description Validate Date and Time using prototype
 * @param dateSeparater - used to get date,month,year from the string
 * @param timeSeparater - used to get hour,minute from the string [optional]
 * @param isTime - for checking time included
 * @return if valid returns date, otherwise empty string
 * @author Lakshmi Narayanan V
 */
String.prototype.getValidDateTime = function(dateSeparater,timeSeparater,isTime){
    arrParts = this.split(" ");
    dateParts = arrParts[0].split(dateSeparater);
    /**
     * @Added a check if date value entered like 08082012
     * Begin
    */
    if(dateParts.length==1) {
        if( dateParts[0].length == 8 && !(isNaN(dateParts[0])) ) {
            tempDateParts = dateParts[0] ;
            dateParts[0] = tempDateParts.substring(0,2);
            dateParts[1] = tempDateParts.substring(2,4);
            dateParts[2] = tempDateParts.substring(4);
        }
    }
    //End
    if(dateParts.length!=3 || dateParts[2]==undefined){
        return "";
    }
    timeParts = "";
    if(isTime){
        if(arrParts.length>1 && arrParts[1]!=""){
            timeParts = arrParts[1].split(timeSeparater);
        }else{
            var dt = new Date();
            timeParts = new Array(dt.getHours(),dt.getMinutes());
        }
    }
    if(dateParts[2].length==2){
        dateParts[2] = "20"+dateParts[2];
    }
    var dateStr = dateParts[0] + "/" + dateParts[1] + "/" + dateParts[2];
    if(isTime){
        dateStr+=" " + timeParts[0] + ":" + timeParts[1];
    }
    var date = new Date(dateStr);
    validDate = (date.getMonth()+1 == Number(dateParts[0]) &&
        date.getDate() == Number(dateParts[1]) &&
        date.getFullYear() == Number(dateParts[2]));
    if(validDate && isTime){
        validDate = (date.getHours() == Number(timeParts[0]) &&
            date.getMinutes() == Number(timeParts[1]));
    }
    if(validDate){
        return dateStr;
    }else{
        return "";
    }
}
function scanAttach(documentId){
    GB_show("Scan", "/logisoft/scan.do?screenName=TRADINGPARTNER&documentId=" +documentId, 350, 650);
}
window.size = function(){
    var w = 0;
    var h = 0;
    //IE
    if(!window.innerWidth){
        //strict mode
        if(!(document.documentElement.clientWidth == 0)){
            w = document.documentElement.clientWidth;
            h = document.documentElement.clientHeight;
        }else{
            w = document.body.clientWidth;
            h = document.body.clientHeight;
        }
    }else{
        w = window.innerWidth;
        h = window.innerHeight;
    }
    return {
        width:w,
        height:h
    };
}
window.center = function(){
    var hWnd = (arguments[0] != null) ? arguments[0] : {
        width:0,
        height:0
    };
    var _x = 0;
    var _y = 0;
    var offsetX = 0;
    var offsetY = 0;
    if(!window.pageYOffset){
        if(!(document.documentElement.scrollTop == 0)){
            offsetY = document.documentElement.scrollTop;
            offsetX = document.documentElement.scrollLeft;
        }else{
            offsetY = document.body.scrollTop;
            offsetX = document.body.scrollLeft;
        }
    }else{
        offsetX = window.pageXOffset;
        offsetY = window.pageYOffset;
    }
    _x = ((this.size().width-hWnd.width)/2)+offsetX;
    _y = ((this.size().height-hWnd.height)/2)+offsetY;
    return{
        x:_x,
        y:_y
    };
}
function preventBack(evt)
{
    evt = (evt) ? evt : ((window.event) ? window.event : "")
    if(!evt) return;
    var elem = (evt.target)
    ? (evt.target.nodeType == 3) ? evt.target.parentNode : evt.target
    : elem = evt.srcElement ;
    if(!elem) return;
    if(event.srcElement.readOnly == true){
        if (
            (elem.form)/*elem is a form's element*/ &&
            (evt.keyCode == 8) || (evt.keyCode == 37 && evt.altKey) || (evt.keyCode == 39 && evt.altKey) )
            {
            evt.cancelBubble = true;
            evt.returnValue = false;
        }
    }
}
function disablePage(form){
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if(element.type == "text" || element.type == "textarea" || element.type=="select-one"){
            element.style.border=0;
            if(element.type == "select-one"){
                element.disabled = true;
            }
            element.readOnly = true;
            if(element.type == "text" || element.type == "textarea"){
                element.style.backgroundColor="#CCEBFF";
            }else{
                element.className="textlabelsBoldForTextBox";
            }
        }else if(element.type=="checkbox" || element.type=="radio"){
            element.style.border=0;
            element.disabled = true;
            element.className="textlabelsBoldForTextBox";
        }else if(element.type == "button"){
            if(element.value!="Go Back"){
                element.style.visibility="hidden";
            }
        }
        //--to hide img---
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            imgs[k].style.visibility = "hidden";
        }
    }
    return false;
}
function getMonthName(monthIndex){
    var m_names = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep","Oct", "Nov", "Dec");
    var name=m_names[monthIndex-1];
    return name;
}
function getMonthNumber(monthName){
    var m_names = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep","Oct", "Nov", "Dec");
    var monthIndex = 0;
    for(var i=0;i<m_names.length;i++)
    {
        if(monthName==m_names[i])
        {
            return i+1;
        }
    }
    return monthIndex;
}
function getCurrentTime(){
    var currentTime="";
    var a_p = "";
    var d = new Date();
    var curr_hour = d.getHours();
    if (curr_hour < 12){
        a_p = "AM";
    }else{
        a_p = "PM";
    }
    if(curr_hour == 0){
        curr_hour = 12;
    }
    if(curr_hour > 12){
        curr_hour = curr_hour - 12;
    }
	
    var curr_min = d.getMinutes();
    curr_min = curr_min + "";
	
    if(curr_min.length == 1){
        curr_min = "0" + curr_min;
    }

    currentTime=curr_hour +":"+ curr_min + " " + a_p;
    return currentTime;
}
/**By Pradeep***/
function setValue(elementId,value){
    document.getElementById(elementId).value=value;
}
function getValue(elementId){
    var value = document.getElementById(elementId).value;
    return value;
}
function makeReadOnly(elementId){
    var element = document.getElementById(elementId);
    element.style.backgroundColor = "#CCEBFF";
    element.style.border = 0;
    element.tabIndex = -1;
    element.className = "textlabelsBoldForTextBox";
}
function disableReadOnly(elementId){
    var element = document.getElementById(elementId);
    element.style.backgroundColor = "#FFFFFF";
    element.style.border='1px solid #C4C5C4';
    element.readOnly = false;
    element.tabIndex = 0;
    element.className = "textlabelsBoldForTextBox";
}
function validate_email(field,alerttxt){
    if(trim(field)!=''){
        var apos=field.indexOf("@");
        var dotpos=field.lastIndexOf(".");
        if (apos<1||dotpos-apos<2) {
            alert(alerttxt);
            return false;
        } else {
            return true;
        }
    }
}
function compare(a,b){
    return a-b;
}
function checkForNumberAndDecimal(obj){
    if(!/^\d*(\.\d{0,6})?$/.test(obj.value)){
        obj.value="";
        sampleAlert("This field should be Numeric");
    }
}
function sampleAlert(txt){
    $.prompt(txt);
}
function limitTextarea(textarea,maxLines,maxChar){
    var lines=textarea.value.replace(/\r/g,'').split('\n'),
    lines_removed,
    char_removed,
    i;
    if(maxLines&&lines.length>maxLines){
        alertNew('You can not enter\nmore than '+maxLines+' lines');
        lines=lines.slice(0,maxLines);
        lines_removed=1
    }
    if(maxChar){
        i=lines.length;
        while(i-->0)
            if(lines[i].length>maxChar){
                lines[i]=lines[i].slice(0,maxChar);
                char_removed=1;
            }
        if(char_removed)sampleAlert('You can not enter more\nthan '+maxChar+' characters per line');
    }
    if(char_removed||lines_removed)textarea.value=lines.join('\n');
}
function shipper_AccttypeCheck(){
    target=jQuery("#acct_type").val();
    subtype=jQuery("#sub_type").val();
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if (target!= null) {
        type = target;
        array1 = type.split(",");
    }
    if (subtype != null) {
        subTypes = ( subtype).toLowerCase();
        array2 = subTypes.split(",");
    }
    if(target!=""){
        if((!array1.contains('S') && !array1.contains('E') && !array1.contains('I')  && array1.contains('V') && !array2.contains('forwarder')) || (target==('C'))){
            sampleAlert("Please select the customers with account type S,E,I and V with subtype forwarder");
            jQuery("#shipperEdi").val('');
            jQuery("#shipperAccountNo").val('');
            jQuery("#shipperAccountNumber").val('');
        }
    }
}
function consignee_AccttypeCheck(){
    target=jQuery("#acct_type").val();
    subtype=jQuery("#sub_type").val();
    var type;
    var array1 = new Array();
    if (target!= null) {
        type = target;
        array1 = type.split(",");
    }
    if(target!=""){
        if(!array1.contains('C')){
            sampleAlert("Please select the customers with account type C");
            jQuery("#consigneeEdi").val('');
            jQuery("#consigneeAccountNo").val('');
            jQuery("#consigneeAccountNumber").val('');
        }
    }
}
function notify_AccttypeCheck(){
    target=jQuery("#acct_type").val();
    subtype=jQuery("#sub_type").val();
    var type;
    var array1 = new Array();
    if (target!= null) {
        type = target;
        array1 = type.split(",");
    }
    if(target!=""){
        if(!array1.contains('C')){
            sampleAlert("Please select the customers with account type C");
            jQuery("#notifyEdi").val('');
            jQuery("#notifyAccountNo").val('');
            jQuery("#notifyAccountNumber").val('');
        }
    }
}
function checkForNumberAndDecimal(phoneFaxZip){
    if(IsNumeric(phoneFaxZip.value)==false && phoneFaxZip.value!=null){
        phoneFaxZip.value="";
        alertNew("This field should be Numeric");
    }
}
function emailValidate(emailId){
    var email = "^[A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}(?:[;][A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}?)*";
    if (trim(emailId.value)!="" && (String(emailId.value).search(email)== -1)){
        alertNew("Enter a valid Email address");
        emailId.value="";
    }
}
function yearValidation(date){
    var d = date.value.split("/");      
    var enteredYear=d[2].substring(0,5);
    var validYear=enteredYear==todayDate.getFullYear() || enteredYear==todayDate.getFullYear()+1 || enteredYear==todayDate.getFullYear()-1;
    if(!validYear){
        alertNew("Please Enter Valid Year");
        date.value="";
        document.getElementById(date.id).focus();
        return;
    }
}
function reminderBox(text, jam) {
    returnValue = jam;
    displayReminder("reminderBox", 100, 50, text,window.center({
        width:100,
        height:100
    }));
}
function displayReminder(id, left, top, text,point) {
    document.getElementById(id).style.left = left + "px";
    document.getElementById(id).style.top = top + "px";
    document.getElementById("innerTextReminder").innerHTML = text;
    document.getElementById(id).style.display = "block";
    document.getElementById(id).style.left=point.x -100 + "px";
    document.getElementById(id).style.top=point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    grayOut(true, "");
}
function reminderCopyBox(text) {
    copyBookReminder("confirmCopyBox", 100, 50, text,window.center({
        width:100,
        height:100
    }));
}
function copyBookReminder(id, left, top, text,point) {
    document.getElementById(id).style.left = left + "px";
    document.getElementById(id).style.top = top + "px";
    document.getElementById("copyBkgIntimation").innerHTML = text;
    document.getElementById(id).style.display = "block";
    document.getElementById(id).style.left=point.x -100 + "px";
    document.getElementById(id).style.top=point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    grayOut(true, "");
}
function insuranceAllowedForPort() {
    var portname = document.getElementById('finalDestination').value;
    var unloc = portname.substring(portname.lastIndexOf("(") + 1, portname.lastIndexOf(")"));
    var contains=(insuranceAllowed.indexOf(unloc)!=-1);
    if(document.getElementById('insuranceYes') && document.getElementById('insuranceNo') && portname!==null && portname!=='') {
        if(contains) {
            document.getElementById('insuranceYes').disabled =true;
            document.getElementById('insuranceNo').checked =true;
            document.getElementById('insuranceNo').disabled =true;
        } else {
            document.getElementById('insuranceYes').disabled =false;
            document.getElementById('insuranceNo').disabled =false;
        }
    }
}
/*
 * Used to set Focus by ElementID
 */
function setFocusByElementId(elementId) {
    var element = document.getElementById(elementId);
    if (element) {
        element.focus();
    }
}
