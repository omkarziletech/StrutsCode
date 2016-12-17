/*
Copyright 2005, 4word systems
All rights reserved.

This software may not be reproduced or distributed in any form without the express 
written consent of 4word systems or it's designee.

Revision 1.1:  20050729 Added underscore to list of valid characters
*/


function isValidEmail(email) {
    email=email.trim();
    if (email=="") {
        return false;
    }
    if (email.length==0) {  
        return false;
    }
    if (! allValidChars(email)) {  // check to make sure all characters are valid
        return false;
    }
    if (email.indexOf("@") < 1) { //  must contain @, and it must not be the first character
        return false;
    }
    if (email.lastIndexOf(".") <= email.indexOf("@")) {  // last dot must be after the @
        return false;
    } 
    if (email.lastIndexOf("@") == email.length-1) {  // @ must not be the last character
        return false;
    }
    if (email.indexOf("..") >=0) { // two periods in a row is not valid
	return false;
    }
    if (email.indexOf(".@") >=0) { // two periods in a row is not valid
	return false;
    }
    if (email.lastIndexOf(".") == email.length-1) {  // . must not be the last character
	return false;
    }
    if (email.lastIndexOf(",") == email.length-1) {  // , must not be the last character
	return false;
    }
    return true;
}

function allValidChars(email) {
  var parsed = true;
  var validchars = "abcdefghijklmnopqrstuvwxyz0123456789@.-_,";
  for (var i=0; i < email.length; i++) {
    var letter = email.charAt(i).toLowerCase();
    if (validchars.indexOf(letter) != -1)
      continue;
    parsed = false;
    break;
  }
  return parsed;
}
