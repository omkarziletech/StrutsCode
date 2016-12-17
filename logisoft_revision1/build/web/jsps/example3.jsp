<html>
<body onLoad="onLoad();">
<head>
 <title>Example 1</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/dojo/dojo.js"></script>
 <script language="javascript">
  dojo.require("dojo.io.*");
  dojo.require("dojo.event.*");

  function onLoad() {
   var buttonObj = document.getElementById("myButton");
   dojo.event.connect(buttonObj, "onclick",
          this, "onclick_myButton");
  }

  function onclick_myButton() {
   var bindArgs = {
    url: "example3.jsp",
    error: function(type, data, evt){
     alert("An error occurred.");
    },
    load: function(type, data, evt){
     alert(data);
    },
    mimetype: "text/plain",
    formNode: document.getElementById("myForm")
   };
   dojo.io.bind(bindArgs);
  }
 </script>
</head>
<body>
<form id="myForm">
 <input type="text" name="name"/>
 <input type="button" id="myButton" value="Submit"/>
</form>
</body>
</html>