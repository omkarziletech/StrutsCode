var viewportwidth;
var viewportheight;


function popup(url) {
    day = new Date();
    id = day.getTime();
    eval("page" + id + " = window.open(url, '" + id + "', 'toolbar=0,scrollbars=1,location=0,statusbar=0,menubar=0,resizable=0,width=800,height=300,left = 0,top = 0');");
}
/***** Lightbox ****/
function lightbox(target){
    document.getElementById(target).style.display='block';
    document.getElementById('fade').style.display='block';
}
function lightboxClose(target){
    document.getElementById(target).style.display='none';
    document.getElementById('fade').style.display='none';
}

function getHeight(ele){
    var eleHeight=document.getElementById(ele).style.height;
    return(eleHeight);
}
function setViewport(){
    var other=80+27+42;
    getViewport();
    vHeight=viewportheight-other;
    document.getElementById('pane').style.height=(viewportheight-other-20)+ 'px';

}

function getViewport(){


    // the more standards compliant browsers (mozilla/netscape/opera/IE7) use window.innerWidth and window.innerHeight

    if (typeof window.innerWidth != 'undefined')
    {
        viewportwidth = window.innerWidth,
        viewportheight = window.innerHeight
    }

    // IE6 in standards compliant mode (i.e. with a valid doctype as the first line in the document)

    else if (typeof document.documentElement != 'undefined'
        && typeof document.documentElement.clientWidth !=
        'undefined' && document.documentElement.clientWidth != 0)
        {
        viewportwidth = document.documentElement.clientWidth,
        viewportheight = document.documentElement.clientHeight
    }

    // older versions of IE

    else
    {
        viewportwidth = document.getElementsByTagName('body')[0].clientWidth,
        viewportheight = document.getElementsByTagName('body')[0].clientHeight
    }

}

function stuffing(){
    
}

function checkthis(tar, weight, cub){
    alert(tar);
    var target;
    container_weight=1000;
    container_cube=1500;
    current_weight=456;
    current_cube=800;
    target=document.getElementById(tar);
    if (target.checked==true)
    {
        target.parentNode.parentNode.style.backgroundColor="yellow";
        current_weight+=weight;
        current_cube+=cub;
    }
    else {
        target.parentNode.parentNode.style.backgroundColor="white";
        current_weight-=weight;
        current_cube-=cub;
    }
    
    weight_per=current_weight/container_weight*100;
    cub_per=current_cube/container_weight*100;
    alert(weight_per);
    documet.getElementById("weight-indi").style.width=weight_per;
    documet.getElementById("cft-indi").style.width=weight_per;
}