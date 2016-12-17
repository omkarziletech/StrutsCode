function isEmail (s)
{  
var count = 0;
if (isEmpty(s)) 
       if (isEmail.arguments.length == 1) return defaultEmptyOK;
       else return (isEmail.arguments[1] == true);
       
    // there must be >= 1 character before @, so we
    // start looking at character position 1 
    // (i.e. second character)
    var i = 1;
    var sLength = s.length;

    // look for @
    while ((i < sLength) && (s.charAt(i) != "@"))
    { i++
    }

    if ((i >= sLength) || (s.charAt(i) != "@")) return false;
    else i += 2;

    // look for .
    while ((i < sLength) && (s.charAt(i) != "."))
    { i++
    }

    // there must be at least one character after the .
    if ((i >= sLength - 1) || (s.charAt(i) != ".")) return false;
  	for(var i=0;i<s.length;i++)
  	{
  		 if(s[i]=='@')
  		 {
  		 count++;
  		 }
  	}
  	if(count>1)
  	{
  		return false;
  	}
    return true;
  
  	
}


// Check whether string s is empty.

function isEmpty(s)
{   return ((s == null) || (s.length == 0))
}

function isEmailSpecial(parm) 
{
var iChars = "!#$%^&*()+=-[]\\\';,/{}|\":<>?";
for (var i = 0; i < parm.length; i++) 
{
  if (iChars.indexOf(parm.charAt(i)) != -1)  
  return false;
  }
  return true;
} 