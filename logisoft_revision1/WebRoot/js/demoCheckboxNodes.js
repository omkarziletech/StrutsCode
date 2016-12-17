//
// Copyright (c) 2006 by Conor O'Mahony.
// For enquiries, please email GubuSoft@GubuSoft.com.
// Please keep all copyright notices below.
// Original author of TreeView script is Marcelino Martins.
//
// This document includes the TreeView script.
// The TreeView script can be found at http://www.TreeView.net.
// The script is Copyright (c) 2006 by Conor O'Mahony.
//
// This configuration file is used to demonstrate how to add checkboxes to your tree.
// If your site will not display checkboxes, pick a different configuration file as 
// the example to follow and adapt.  You can find general instructions for this file 
// at www.treeview.net.  Intructions on how to add checkboxes to a tree are provided 
// in this file.
//

USETEXTLINKS = 1  
STARTALLOPEN = 0
HIGHLIGHT = 0
PRESERVESTATE = 1

// NOTE:  If you are going to set USEICONS = 1, then you will want to edit the gif 
// files and remove the white space on the right
USEICONS = 0

// In this case we want the whole tree to be built, even those branches that are 
// closed. The reason is that otherwise some form elements might not be built at 
// all before the user presses the "Get Values" button.
BUILDALL = 1


// Some auxiliary functions for the contruction of the tree follow.  You will 
// certainly want to change these functions for your own purposes.
//
// These functions are directly related with the additional JavaScript in the 
// page holding the tree (demoCheckboxLeftFrame.html), where the form handling 
// code resides.

function CA(f) 
{
	var obj = document.getElementById('ft1')
	for (i=0;i<obj.elements.length;i++) 
	{
		if ((obj.elements[i].type == "checkbox") && (obj.elements[i].id == f.id)) 
		{			
			if (f.checked == true) {
				obj.elements[i].checked = true
			}else {
				obj.elements[i].checked = false
			}
       		}
   	}
}

function EA(f) {
	alert(f.checked)
	var obj = document.getElementById('ft1')
	for (i=0;i<obj.elements.length;i++) 
	{
		if ((obj.elements[i].type == "radio") && (obj.elements[i].id == f.id)) 
		{			
			if (f.checked == true) {
				obj.elements[i].checked = true
			}else {
				obj.elements[i].checked = false
			}
       		}
   	}

}


// If you want to add checkboxes to the folder, you will have to create a function 
// similar to this one and then call it in the tree construction section below.
function generateCheckBox(parentfolderObject, itemLabel, checkBoxDOMId) {
  var newObj;

  // For an explanation of insDoc and gLnk, read the online instructions.
  // They are the basis upon which TreeView is based.
  newObj = insDoc(parentfolderObject, gLnk("R", "", "javascript:parent.op()"))

  // The trick to show checkboxes in a tree that was made to display links is to 
  // use the prependHTML. There are general instructions about this member
  // in the online documentation.
  newObj.prependHTML = "<td valign=middle><input type=checkbox id="+checkBoxDOMId+"></td>" +
  "<td>" + itemLabel + "</td>" +
  "<td><input type=radio name='" + itemLabel + "' id=" + checkBoxDOMId + " checked></td><td><font color=lightgrey><I>Executable</I></td>" +
  "<td><input type=radio name='" + itemLabel + "' id=" + checkBoxDOMId + "></td><td><font color=lightgrey><I>View Only</I></td>"
  
   
}





// This function os similar to the one above, but instead of creating checkboxes, 
// it creates radio buttons.
function generateRadioB(parentfolderObject, itemLabel, checkBoxDOMId) {
  var newObj;

  // For an explanation of insDoc and gLnk, read the online instructions.
  // They are the basis upon which TreeView is based.
  newObj = insDoc(parentfolderObject, gLnk("R", itemLabel, "javascript:parent.op()"))

  // The trick to show radio buttons in a tree that was made to display links  
  // is to use the prependHTML. There are general instructions about this member
  // in the online documentation.
  newObj.prependHTML = "<td valign=middle><input type=radio name=hourPick id="+checkBoxDOMId+"></td>"
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
	"<td>" + itemLabel + "</td>" +
  	"<td><input type=radio name='" + itemLabel + "' id=" + checkid + " checked '></td><td><font color=lightgrey><I>Execute All</I></td>" +
  	"<td><input type=radio name='" + itemLabel + "' id=" + checkid + " '></td><td><font color=lightgrey><I>View All</I></td>"

}




// The following code constructs the tree.
foldersTree = gFld("Econocaribe Menu Creator", "")
foldersTree.treeID = "checkboxTree"
aux00 = insFld(foldersTree, gFld("Logisoft", "javascript:parent.op()"))
aux0 = insFld(aux00, gFld("LCL", "javascript:parent.op()"))
aux1 = insFld(aux0, gFld("Receivings", "javascript:parent.op()"))
aux2 = insFld(aux1, gFld("Dock Receipt", "javascript:parent.op()"))
generateTopCheckBox(aux2, "checkAll", "BOX1", 1);
generateCheckBox(aux2, "New/Existing DR/Booking", "BOX1")
generateCheckBox(aux2, "Trucking Request", "BOX1")
generateCheckBox(aux2, "Hazardous Material", "BOX1")
generateCheckBox(aux2, "Barrel Details", "BOX1")
generateCheckBox(aux2, "Measure Cargo", "BOX1")
generateCheckBox(aux2, "Validate Documents", "BOX1")
generateCheckBox(aux2, "Notes", "BOX1")
aux3 = insFld(aux1, gFld("Inventory/ Release", "javascript:parent.op()"))
generateCheckBox(aux3, "View and Release DR/Booking", "BOX1")

aux4 = insFld(aux0, gFld("Container Management", "javascript:parent.op()"))
generateTopCheckBox(aux4, "checkAll", "BOX2", 1);
generateCheckBox(aux4, "New/Existing DR/Booking", "BOX2")
generateCheckBox(aux4, "View unit", "BOX2")
generateCheckBox(aux4, "Create/ Update unit", "BOX2")
generateCheckBox(aux4, "Stuffing", "BOX2")
generateCheckBox(aux4, "Exception List", "BOX2")
generateCheckBox(aux4, "Trucking Pickup/ Request", "BOX2")

aux4 = insFld(aux0, gFld("House BL", "javascript:parent.op()"))
generateTopCheckBox(aux4, "checkAll", "BOX3", 1);
generateCheckBox(aux4, "Create House BL", "BOX3")
generateCheckBox(aux4, "HazMat", "BOX3")
generateCheckBox(aux4, "AES Management(SEDs)", "BOX3")
generateCheckBox(aux4, "AES Response", "BOX3")
generateCheckBox(aux4, "Charges", "BOX3")

aux5 = insFld(aux0, gFld("Voyage Management", "javascript:parent.op()"))
generateTopCheckBox(aux5, "checkAll", "BOX4", 1);
generateCheckBox(aux5, "View Export Voyages", "BOX4")
generateCheckBox(aux5, "Create or Edit Export Voyages", "BOX4")
generateCheckBox(aux5, "View Inland Voyages", "BOX4")
generateCheckBox(aux5, "Create or Edit Inland Voyages", "BOX4")

aux6 = insFld(aux0, gFld("Rate Management", "javascript:parent.op()"))
generateTopCheckBox(aux6, "checkAll", "BOX4", 1);
generateCheckBox(aux6, "Air Rates", "BOX4")
generateCheckBox(aux6, "FCL Buy Rates", "BOX4")
generateCheckBox(aux6, "FCL Sell Rates", "BOX4")
generateCheckBox(aux6, "Ocean Rates", "BOX4")

aux7 = insFld(aux0, gFld("Trading partner maintenance", "javascript:parent.op()"))
generateTopCheckBox(aux7, "checkAll", "BOX5", 1);
generateCheckBox(aux7, "View Shipper/Forwader/Consignee", "BOX5")
generateCheckBox(aux7, "Create/Update Shipper/Forwarder/Others", "BOX5")
generateCheckBox(aux7, "Consignee", "BOX5")

aux8 = insFld(aux0, gFld("Reference Data Management", "javascript:parent.op()"))
generateTopCheckBox(aux8, "checkAll", "BOX6", 1);
generateCheckBox(aux8, "Carrier", "BOX6")
generateCheckBox(aux8, "System Rules", "BOX6")
generateCheckBox(aux8, "User Profile", "BOX6")
generateCheckBox(aux8, "Adding Ports", "BOX6")

aux9 = insFld(aux0, gFld("Status Tracking", "javascript:parent.op()"))
generateTopCheckBox(aux9, "checkAll", "BOX7", 1);
generateCheckBox(aux9, "DR/Bookings/Shipments", "BOX7")
generateCheckBox(aux9, "Container Status", "BOX7")
generateCheckBox(aux9, "Inland Status", "BOX7")

aux10 = insFld(aux0, gFld("Quotation", "javascript:parent.op()"))
generateTopCheckBox(aux10, "checkAll", "BOX8", 1);
generateCheckBox(aux10, "Quotation", "BOX8")
generateCheckBox(aux10, "View Quotation", "BOX8")

aux11 = insFld(aux0, gFld("Pre-defined Reports", "javascript:parent.op()"))
generateTopCheckBox(aux11, "checkAll", "BOX9", 1);
generateCheckBox(aux11, "Under Construction", "BOX9")
generateCheckBox(aux11, "Under Construction", "BOX9")

aux12 = insFld(aux0, gFld("Ad-hoc Reports", "javascript:parent.op()"))
generateTopCheckBox(aux12, "checkAll", "BOX10", 1);
generateCheckBox(aux12, "Under Construction", "BOX10")
generateCheckBox(aux12, "Under Construction", "BOX10")


aux01 = insFld(aux00, gFld("FCL", "javascript:parent.op()"))


aux02 = insFld(aux00, gFld("ADMIN", "javascript:parent.op()"))
aux021 = insFld(aux02, gFld("Menu Management", "javascript:parent.op()"))
generateTopCheckBox(aux021, "checkAll", "BOX11", 1);
generateCheckBox(aux021, "Role Management", "BOX11")
generateCheckBox(aux021, "User Management", "BOX11")
