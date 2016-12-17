/*
Sweet Titles (c) Creative Commons 2005
http://creativecommons.org/licenses/by-sa/2.5/
Author: Dustin Diaz | http://www.dustindiaz.com
*/
function getIEVersionNo() {
    var ua = navigator.userAgent;
    var MSIEOffset = ua.indexOf("MSIE ");

    if (MSIEOffset == -1) {
        return 0;
    } else {
        return parseFloat(ua.substring(MSIEOffset + 5, ua.indexOf(";", MSIEOffset)));
    }
}
var isIE5Min = getIEVersionNo() <= 8;

var sweetTitles = { 
	xCord : 0,								// @Number: x pixel value of current cursor position
	yCord : 0,								// @Number: y pixel value of current cursor position
	tipElements : ['a','abbr','acronym'],	// @Array: Allowable elements that can have the toolTip
	obj : Object,							// @Element: That of which you're hovering over
	tip : Object,							// @Element: The actual toolTip itself
	active : 0,								// @Number: 0: Not Active || 1: Active
	init : function() {
		if ( !document.getElementById ||
			!document.createElement ||
			!document.getElementsByTagName ) {
			return;
		}
		var i,j;
		this.tip = document.createElement('div');
		this.tip.id = 'toolTip';
		document.getElementsByTagName('body')[0].appendChild(this.tip);
		this.tip.style.top = '0';
		this.tip.style.visibility = 'hidden';
		var tipLen = this.tipElements.length;
		for ( i=0; i<tipLen; i++ ) {
			var current = document.getElementsByTagName(this.tipElements[i]);
			var curLen = current.length;
			for ( j=0; j<curLen; j++ ) {
				addEvent(current[j],'mouseover',this.tipOver);
				addEvent(current[j],'mouseout',this.tipOut);
				current[j].setAttribute('tip',current[j].title);
				current[j].removeAttribute('title');
			}
		}
	},
	updateXY : function(e) {
		if ( document.captureEvents ) {
			sweetTitles.xCord = e.pageX;
			sweetTitles.yCord = e.pageY;
		} else if ( window.event.clientX ) {
			sweetTitles.xCord = window.event.clientX+document.documentElement.scrollLeft;
			sweetTitles.yCord = window.event.clientY+document.documentElement.scrollTop;
		}
	},
	tipOut: function() {
		if ( window.tID ) {
			clearTimeout(tID);
		}
		if ( window.opacityID ) {
			clearTimeout(opacityID);
		}
		sweetTitles.tip.style.visibility = 'hidden';
	},
	checkNode : function() {
		var trueObj = this.obj;
		if ( this.tipElements.inArray(trueObj.nodeName.toLowerCase()) ) {
			return trueObj;
		} else {
			return trueObj.parentNode;
		}
	},
	tipOver : function(e) {
		sweetTitles.obj = this;
		tID = window.setTimeout("sweetTitles.tipShow()",100);
		sweetTitles.updateXY(e);
	},
	tipShow : function() {		
		var scrX = Number(this.xCord);
		var scrY = Number(this.yCord);
		var tp = parseInt(scrY+100);
		var lt = parseInt(scrX+500);
		var anch = this.checkNode();
		var addy = '';
		var access = '';
		if ( anch.nodeName.toLowerCase() == 'a' ) {
			addy = (anch.href.length > 25 ? anch.href.toString().substring(0,25)+"..." : anch.href);
			var access = ( anch.accessKey ? ' <span>['+anch.accessKey+']</span> ' : '' );
		} else {
			addy = anch.firstChild.nodeValue;
		}
		this.tip.innerHTML = "<p>"+anch.getAttribute('tip')+"<em>"+access+addy+"</em></p>";
                if(isIE5Min){//for IE8
                    if ( parseInt(document.documentElement.clientWidth+document.documentElement.scrollLeft) < parseInt(this.tip.offsetWidth+lt) ) {
			this.tip.style.left = parseInt(lt-(this.tip.offsetWidth+10))+292+'px';
//                            this.tip.style.left = parseInt(lt-(this.tip.offsetWidth+10))+'px';
                    } else {
                        this.tip.style.left = lt+292+'px';
//                            this.tip.style.left = lt+'px';
                    }
                    if ( parseInt(document.documentElement.clientHeight+document.documentElement.scrollTop) < parseInt(this.tip.offsetHeight+tp) ) {
    			this.tip.style.top = parseInt(tp-(this.tip.offsetHeight+10))-130+'px';
//                            this.tip.style.top = parseInt(tp-(this.tip.offsetHeight+10))+'px';
                    } else {
    			this.tip.style.top = tp-130+'px';
//                            this.tip.style.top = tp+'px';
                    }
                }else{
                    if ( parseInt(document.documentElement.clientWidth+document.documentElement.scrollLeft) < parseInt(this.tip.offsetWidth+lt) ) {
                            this.tip.style.left = parseInt(lt-(this.tip.offsetWidth+10))-210+'px';
                    } else {
                            this.tip.style.left = lt-210+'px';
                    }
                    if ( parseInt(document.documentElement.clientHeight+document.documentElement.scrollTop) < parseInt(this.tip.offsetHeight+tp) ) {
                            this.tip.style.top = parseInt(tp-(this.tip.offsetHeight+10))-110+'px';
                    } else {
                            this.tip.style.top = tp-110+'px';
                    }
                }
		this.tip.style.visibility = 'visible';
		//this.tip.style.opacity = '.1';  /*commend by suriya*/
		//this.tipFade(10);
	},
	tipFade: function(opac) {
		var passed = parseInt(opac);
		var newOpac = parseInt(passed+10);
		if ( newOpac < 80 ) {
			this.tip.style.opacity = '.'+newOpac;
			this.tip.style.filter = "alpha(opacity:"+newOpac+")";
			opacityID = window.setTimeout("sweetTitles.tipFade('"+newOpac+"')",20);
		}
		else { 
			this.tip.style.opacity = '.80';
			this.tip.style.filter = "alpha(opacity:80)";
		}
	}
};
function pageLoader() {
	sweetTitles.init();
}
addEvent(window,'load',pageLoader);