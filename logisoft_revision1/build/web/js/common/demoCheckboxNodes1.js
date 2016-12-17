USETEXTLINKS = 1
STARTALLOPEN = 0
HIGHLIGHT = 0
PRESERVESTATE = 1
USEICONS = 0
BUILDALL = 1
 var checkflag = "false";
/*function CA(f) {
  if (checkflag == "false") {
  for (i = 0; i < f.length; i++) {
  f[i].checked = true;}
  checkflag = "true";
  return "Uncheck All"; }
  else {
  for (i = 0; i < f.length; i++) {
  f[i].checked = false; }
  checkflag = "false";
  return "Check All"; }
  }*/

function CA(f)
{
  var obj = document.getElementById('newRole')
  //alert(obj.elements.length);
  for (i=0;i<obj.elements.length;i++)
  {
   if ((obj.elements[i].type == 'checkbox') && (obj.elements[i].id == f.id))
    {
      if (f.checked == true) {obj.elements[i].checked = true}
      else 
      {obj.elements[i].checked = false}
    }
  }
}

function overlay() {el = document.getElementById('overlay');
el.style.visibility = (el.style.visibility == 'visible') ? 'hidden' : 'visible';
}
function generateCheckBox(parentfolderObject, itemLabel, checkBoxDOMId) {
var newObj;
 // For an explanation of insDoc and gLnk, read the online instructions.
// They are the basis upon which TreeView is based.
newObj = insDoc(parentfolderObject, gLnk("R", "", "javascript:parent.op()"))
// The trick to show checkboxes in a tree that was made to display links is to
// use the prependHTML. There are general instructions about this member
// in the online documentation.
newObj.prependHTML = "<td width=600px valign=middle><input type=checkbox id="+checkBoxDOMId+" checked></td>" +
"<td >" + itemLabel + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>" +
"<td><input type=checkbox name='" + itemLabel + "' id=M" + checkBoxDOMId + " checked></td><td><font color=black>Modify&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>" +
"<td><img src='Lock4.gif' name='addRest' value='Add Restriction' img src='addrestriction.gif' border=0 onClick='overlay()' ></td>"
}
function generateTopCheckBox(parentfolderObject, itemLabel, groupname, checkid) {
var newObj;
// Read the online documentation for an explanation of insDoc and gLnk,
// they are the base of the simplest Treeview trees
newObj = insDoc(parentfolderObject, gLnk("R", "", ""))
 // The trick to show checkboxes in a tree that was made to display links is to
// use the prependHTML. There are general instructions about this member
// in the online documentation
var lyrstr = 1;
newObj.prependHTML = "<td valign=middle><input type=checkbox name='" + lyrstr + "' id='"+ groupname + "' onclick='CA(this)'></td>" +
"<td>" + itemLabel + "</td>"}
foldersTree = gFld("Econocaribe Menu Creator", "")
foldersTree.treeID = "checkboxTree"
aux1 = insFld(foldersTree, gFld("Logisoft", "javascript:parent.op()"))
aux2 = insFld(aux1, gFld('LCL', 'javascript:parent.op()'))
aux54 = insFld(aux1, gFld('FCL', 'javascript:parent.op()'))
aux87 = insFld(aux1, gFld('Admin', 'javascript:parent.op()'))
aux92 = insFld(aux1, gFld('yogi1', 'javascript:parent.op()'))


aux3 = insFld(aux2, gFld('Receivings', 'javascript:parent.op()'))
aux14 = insFld(aux2, gFld('Container Management', 'javascript:parent.op()'))
aux20 = insFld(aux2, gFld('House BL', 'javascript:parent.op()'))
aux26 = insFld(aux2, gFld('Voyage Management', 'javascript:parent.op()'))
aux31 = insFld(aux2, gFld('Rate Management', 'javascript:parent.op()'))
aux36 = insFld(aux2, gFld('Trading Partner Maintenance', 'javascript:parent.op()'))
aux40 = insFld(aux2, gFld('Reference Data Management', 'javascript:parent.op()'))
aux45 = insFld(aux2, gFld('Status Tracking', 'javascript:parent.op()'))
aux49 = insFld(aux2, gFld('Quotation', 'javascript:parent.op()'))
aux52 = insFld(aux2, gFld('Pre-defined Reports', 'javascript:parent.op()'))
aux53 = insFld(aux2, gFld('Ad-hoc Reports', 'javascript:parent.op()'))


aux4 = insFld(aux3, gFld('Dock Receipt', 'javascript:parent.op()'))
aux12 = insFld(aux3, gFld('Inventory/Release', 'javascript:parent.op()'))
aux93 = insFld(aux3, gFld('yogi2', 'javascript:parent.op()'))


generateTopCheckBox(aux4, 'checkAll', '4', 1)
generateCheckBox(aux4, 'New Existing DR/Booking', '5')
generateCheckBox(aux4, 'Trucking Request', '6')
generateCheckBox(aux4, 'Hazardous Material', '7')
generateCheckBox(aux4, 'Barrel Details', '8')
generateCheckBox(aux4, 'Measure Cargo', '9')
generateCheckBox(aux4, 'Validate Documents', '10')
generateCheckBox(aux4, 'Notes', '11')


generateTopCheckBox(aux12, 'checkAll', '12', 1)
generateCheckBox(aux12, 'View and Release DR/Booking', '13')


generateTopCheckBox(aux14, 'checkAll', '14', 1)
generateCheckBox(aux14, 'View Unit', '15')
generateCheckBox(aux14, 'Create/Update Unit', '16')
generateCheckBox(aux14, 'Stuffing', '17')
generateCheckBox(aux14, 'Exception List', '18')
generateCheckBox(aux14, 'Trucking Pickup/Request', '19')


generateTopCheckBox(aux20, 'checkAll', '20', 1)
generateCheckBox(aux20, 'Create House BL', '21')
generateCheckBox(aux20, 'Hazmat', '22')
generateCheckBox(aux20, 'AES Management(SEDs)', '23')
generateCheckBox(aux20, 'AES Response', '24')
generateCheckBox(aux20, 'charges', '25')


generateTopCheckBox(aux26, 'checkAll', '26', 1)
generateCheckBox(aux26, 'View Export Voyages', '27')
generateCheckBox(aux26, 'Create or Edit Export Voyages', '28')
generateCheckBox(aux26, 'View Inland Voyages', '29')
generateCheckBox(aux26, 'Create or Edit Inland Voyages', '30')
aux99 = insFld(aux26, gFld('yogi9new', 'javascript:parent.op()'))




generateTopCheckBox(aux31, 'checkAll', '31', 1)
generateCheckBox(aux31, 'Air Rates', '32')
generateCheckBox(aux31, 'FCL Buy Rates', '33')
generateCheckBox(aux31, 'FCL Sell Rates', '34')
generateCheckBox(aux31, 'Ocean Rates', '35')


generateTopCheckBox(aux36, 'checkAll', '36', 1)
generateCheckBox(aux36, 'View Shipper/Forwarder/Consignee', '37')


generateTopCheckBox(aux40, 'checkAll', '40', 1)
generateCheckBox(aux40, 'Carrier', '41')
generateCheckBox(aux40, 'System Rules', '42')
generateCheckBox(aux40, 'User Profiles', '43')
generateCheckBox(aux40, 'Adding Ports', '44')


generateTopCheckBox(aux45, 'checkAll', '45', 1)
generateCheckBox(aux45, 'DR/Bookings/Shipments', '46')
generateCheckBox(aux45, 'Container Status', '47')
generateCheckBox(aux45, 'Inland Status', '48')
aux94 = insFld(aux45, gFld('yogi3', 'javascript:parent.op()'))




generateTopCheckBox(aux49, 'checkAll', '49', 1)
generateCheckBox(aux49, 'Quotaion', '50')
generateCheckBox(aux49, 'View Quotation', '51')






aux55 = insFld(aux54, gFld('Bookings, Quotes and BL', 'javascript:parent.op()'))
aux60 = insFld(aux54, gFld('Container Management', 'javascript:parent.op()'))
aux65 = insFld(aux54, gFld('Rate Management', 'javascript:parent.op()'))
aux72 = insFld(aux54, gFld('Trading Partner Maintenance', 'javascript:parent.op()'))
aux76 = insFld(aux54, gFld('Reference Data Management', 'javascript:parent.op()'))
aux81 = insFld(aux54, gFld('Status Tracking', 'javascript:parent.op()'))
aux85 = insFld(aux54, gFld('Pre-defined Reports', 'javascript:parent.op()'))
aux86 = insFld(aux54, gFld('Ad-hoc Reports', 'javascript:parent.op()'))
aux97 = insFld(aux54, gFld('yogi6', 'javascript:parent.op()'))
generateTopCheckBox(aux54, 'checkAll', '54', 1)
generateCheckBox(aux54, 'yogi7', '98')


generateTopCheckBox(aux55, 'checkAll', '55', 1)
generateCheckBox(aux55, 'Booking Request', '56')
generateCheckBox(aux55, 'FCl BL', '57')
generateCheckBox(aux55, 'Quotes', '58')
generateCheckBox(aux55, 'Change Mode', '59')


generateTopCheckBox(aux60, 'checkAll', '60', 1)
generateCheckBox(aux60, 'View Unit', '61')
generateCheckBox(aux60, 'Create/Update Unit', '62')
generateCheckBox(aux60, 'Trucking Request to Pick up empty', '63')
generateCheckBox(aux60, 'Exception List', '64')


generateTopCheckBox(aux65, 'checkAll', '65', 1)
generateCheckBox(aux65, 'Service Contract Rate', '66')
generateCheckBox(aux65, 'Port Groups', '67')
generateCheckBox(aux65, 'LCL Rate estimator', '68')
generateCheckBox(aux65, 'Inland Rates', '69')
generateCheckBox(aux65, 'Ocean Rates', '70')
generateCheckBox(aux65, 'Standard Charges', '71')


generateTopCheckBox(aux72, 'checkAll', '72', 1)
generateCheckBox(aux72, 'View Shipper/Forwrder/Consignee', '73')
generateCheckBox(aux72, 'Create/Update Shipper/Forwarder/Others', '74')
generateCheckBox(aux72, 'Consignee', '75')
aux95 = insFld(aux72, gFld('yogi4', 'javascript:parent.op()'))




generateTopCheckBox(aux76, 'checkAll', '76', 1)
generateCheckBox(aux76, 'Carrier', '77')
generateCheckBox(aux76, 'System Rules', '78')
generateCheckBox(aux76, 'User Profile', '79')
generateCheckBox(aux76, 'Adding Ports', '80')


generateTopCheckBox(aux81, 'checkAll', '81', 1)
generateCheckBox(aux81, 'DR/Bookings/shipments', '82')
generateCheckBox(aux81, 'Container Status', '83')
generateCheckBox(aux81, 'Inland Rates', '84')








aux88 = insFld(aux87, gFld('Menu Management', 'javascript:parent.op()'))


generateTopCheckBox(aux88, 'checkAll', '88', 1)
generateCheckBox(aux88, 'Role Management', '89')
generateCheckBox(aux88, 'User Management', '90')
generateCheckBox(aux88, 'Menu/Action Management', '91')
generateCheckBox(aux88, 'yogi5', '96')
generateCheckBox(aux88, 'yogi10', '100')
aux101 = insFld(aux88, gFld('yogi11', 'javascript:parent.op()'))


generateTopCheckBox(aux101, 'checkAll', '101', 1)
generateCheckBox(aux101, 'yogi12', '102')




