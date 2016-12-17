/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */	
 
var Search=function(){
     this.searchByName=function() {
     var byName=document.getElementById("byName").value;
     console.log(byName);
     
     
    $("#action").val(searchName);
    var params = $("#personForm").serialize();
    params+="byName="+byName;
    
    
    $.post($("#personForm").attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
     
     
     
     
     
        };
   
};
var search=new Search();
   