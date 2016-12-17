var tooltip=function(){
    return{
        show:function(v,w,e){
            e = e || window.event;
            var obj = document.getElementById('bubble_tooltip');
            var obj2 = document.getElementById('bubble_tooltip_content');
            obj2.innerHTML = v;
            obj.style.display = 'block';
            obj.style.zIndex = "99999";
            var posx=0,posy=0;
            if(e==null) e=window.event;
            if(e.pageX || e.pageY){
                posx=e.pageX;
                posy=e.pageY;
            }else{
                if(document.documentElement.scrollTop){
                    posx=e.clientX+document.documentElement.scrollLeft;
                    posy=e.clientY+document.documentElement.scrollTop;
                }else{
                    posx=e.clientX+document.body.scrollLeft;
                    posy=e.clientY+document.body.scrollTop;
                }
                obj.style.top=(posy+10)+"px";
                obj.style.left=(posx-20)+"px";
            }
            obj.style.top=(posy+10)+"px";
            obj.style.left=(posx-20)+"px";
            if(undefined != w && null != w){
                obj.style.left=(posx-w)+"px";
            }
        },
        showSmall:function(v,w,e){
            e = e || window.event;
            var obj = document.getElementById('bubble_tooltip_small');
            var obj2 = document.getElementById('bubble_tooltip_content_small');
            obj2.innerHTML = v;
            obj.style.display = 'block';
            obj.style.zIndex = "99999";
            var posx=0,posy=0;
            if(e==null) e=window.event;
            if(e.pageX || e.pageY){
                posx=e.pageX;
                posy=e.pageY;
            }else{
                if(document.documentElement.scrollTop){
                    posx=e.clientX+document.documentElement.scrollLeft;
                    posy=e.clientY+document.documentElement.scrollTop;
                }else{
                    posx=e.clientX+document.body.scrollLeft;
                    posy=e.clientY+document.body.scrollTop;
                }
                obj.style.top=(posy+10)+"px";
                obj.style.left=(posx-20)+"px";
            }
            obj.style.top=(posy+10)+"px";
            obj.style.left=(posx-20)+"px";
            if(undefined != w && null != w){
                obj.style.left=(posx-w)+"px";
            }
        },
        showTop:function(v,w,e){
            e = e || window.event;
            var obj = document.getElementById('bubble_tooltip_ForTop');
            var obj2 = document.getElementById('bubble_tooltip_content_ForTop');
            obj2.innerHTML = v;
            obj.style.display = 'block';
            obj.style.zIndex = "99999";
            var posx=0,posy=0;
            if(e==null) e=window.event;
            if(e.pageX || e.pageY){
                posx=e.pageX;
                posy=e.pageY;
            }else{
                if(document.documentElement.scrollTop){
                    posx=e.clientX+document.documentElement.scrollLeft;
                    posy=e.clientY+document.documentElement.scrollTop;
                }else{
                    posx=e.clientX+document.body.scrollLeft;
                    posy=e.clientY+document.body.scrollTop;
                }
                obj.style.left=(posx-37)+"px";
            }

            obj.style.left=(posx-37)+"px";
            var st = Math.max(document.body.scrollTop,document.documentElement.scrollTop);
            obj.style.top = e.clientY - obj.offsetHeight -1 + st + 'px';
            if(undefined != w && null != w){
                obj.style.left=(posx-w)+"px";
            }
        },
        showComments:function(v,w,e){
            e = e || window.event;
            var obj = document.getElementById('commentDiv');
            var commentTable = document.getElementById('commentTableInfo');
            var splitArry = new Array();
            splitArry = v.split(").");
            var info = '';
            var userInfo = '';
            if(splitArry.length > 1){
                for(var i=splitArry.length-2;i>=0;i--){
                    userInfo = splitArry[i].substring(splitArry[i].indexOf('(')+1,splitArry[i].length);
                    info = splitArry[i].substring(0,splitArry[i].indexOf("("));
                    appendMessage(info,userInfo,i,commentTable);
                }
            }else{
                appendMessage(v,'',i,commentTable);
            }
            obj.style.display = 'block';
            var st = Math.max(document.body.scrollTop,document.documentElement.scrollTop);
            var leftPos = e.clientX-w;
            if(leftPos<0)leftPos = 0;
            obj.style.left = leftPos + 'px';
            obj.style.top = e.clientY - obj.offsetHeight -1 + st + 'px';
            obj.style.zIndex = "2";
            obj.style.width = "200px";
				
        },
        showTopText:function(v,w,e){
            e = e || window.event;
            var obj = document.getElementById('bubble_tooltip_ForTop');
            var obj2 = document.getElementById('bubble_tooltip_content_ForTop');
            obj2.innerHTML = v;
            obj.style.display = 'block';
            obj.style.zIndex = "99999";
            var posx=0,posy=0;
            if(e==null) e=window.event;
            if(e.pageX || e.pageY){
                posx=e.pageX;
                posy=e.pageY;
            }else{
                if(document.documentElement.scrollTop){
                    posx=e.clientX+document.documentElement.scrollLeft;
                    posy=e.clientY+document.documentElement.scrollTop;
                }else{
                    posx=e.clientX+document.body.scrollLeft;
                    posy=e.clientY+document.body.scrollTop;
                }
            }
            var st = Math.max(document.body.scrollTop,document.documentElement.scrollTop);
            var leftPos = e.clientX - 20;
            obj.style.left = leftPos + 'px';
            obj.style.top = e.clientY - obj.offsetHeight -1 + st + 'px';
            if(undefined != w && null != w){
                obj.style.left=(posx-w)+"px";
            }
				
        },
			
        hide:function(){
            if(null!=document.getElementById('bubble_tooltip')){
                document.getElementById('bubble_tooltip').style.display = 'none';
            }
            if(null!=document.getElementById('bubble_tooltip_ForTop')){
                document.getElementById('bubble_tooltip_ForTop').style.display = 'none';
            }
            if(null!=document.getElementById('bubble_tooltip_small')){
                document.getElementById('bubble_tooltip_small').style.display = 'none';
            }
			
			
        },
        hideComments:function(){
            removeMessage("commentTableInfo");
            document.getElementById('commentDiv').style.display = 'none';
        }
    };
}();
function appendMessage(info,userInfo,index,table) {
    var tbody = document.getElementById('commentTableInfo').getElementsByTagName("TBODY")[0];
    var paragraph1 = document.createElement("p");
    paragraph1.innerHTML = wordwrap(info);
    var row = document.createElement("TR");
    var td1 = document.createElement("TD");
    td1.className = 'forComments';
    td1.appendChild(paragraph1);
    row.appendChild(td1);
    tbody.appendChild(row);
    var paragraph2 = document.createElement("p");
    paragraph2.innerHTML = userInfo.chunk(30).join('<br>');
    var td2 = document.createElement("TD");
    td2.className = 'forUserNameTime';
    td2.appendChild (paragraph2);
    row.appendChild(td2);
    tbody.appendChild(row);
}
function wordwrap( str, width, brk, cut ) {
    brk = brk || '<br/>\n';
    width = width || 40;
    cut = cut || false;
    if (!str) {
        return str;
    }
    var regex = '.{1,' +width+ '}(\\s|$)' + (cut ? '|.{' +width+ '}|.+$' : '|\\S+?(\\s|$)');
    return str.match( RegExp(regex, 'g') ).join( brk );
}
function removeMessage(text) {
    var holder = document.getElementById(text).getElementsByTagName("TBODY")[0];//the holder div
    while(holder.hasChildNodes()){
        holder.removeChild(holder.lastChild);
    }
}
String.prototype.chunk = function(n) {
    var ret = [];
    for(var i=0, len=this.length; i < len; i += n) {
        ret.push(this.substr(i, n))
    }
    return ret
};

/*
var tooltip=function(){
	var id = 'tt';
	var top = 3;
	var left = 3;
	var maxw = 600;
	var speed = 10;
	var timer = 20;
	var endalpha = 95;
	var alpha = 0;
	var tt,t,c,b,h;
	var ie = document.all ? true : false;
	return{
		show:function(v,w){
				if(tt == null){
					tt = document.createElement('div');
					tt.setAttribute('id',id);
					t = document.createElement('div');
					t.setAttribute('id',id + 'top');
					c = document.createElement('div');
					c.setAttribute('id',id + 'cont');
					b = document.createElement('div');
					b.setAttribute('id',id + 'bot');
					tt.appendChild(t);
					tt.appendChild(c);
					tt.appendChild(b);
					document.body.appendChild(tt);
					tt.style.opacity = 0;
					tt.style.filter = 'alpha(opacity=0)';
					document.onmousemove = this.pos;
				}
				tt.style.display = 'block';
				c.innerHTML = v;
				tt.style.width = w ? w + 'px' : 'auto';
				if(!w && ie){
					t.style.display = 'none';
					b.style.display = 'none';
					tt.style.width = tt.offsetWidth;
					t.style.display = 'block';
					b.style.display = 'block';
				}
				if(tt.offsetWidth > maxw){tt.style.width = maxw + 'px'}
				h = parseInt(tt.offsetHeight) + top;
				clearInterval(tt.timer);
				tt.timer = setInterval(function(){tooltip.fade(1)},timer);
			}
			,
			showToolTip:function(v,l,w){
				//w = 405;
				if(v != "" && v.length > l) {
					v = '<strong>'+v+'</strong>';
					if(tt == null){
						tt = document.createElement('div');
						tt.setAttribute('id',id);
						t = document.createElement('div');
						t.setAttribute('id',id + 'top');
						c = document.createElement('div');
						c.setAttribute('id',id + 'cont');
						b = document.createElement('div');
						b.setAttribute('id',id + 'bot');
						tt.appendChild(t);
						tt.appendChild(c);
						tt.appendChild(b);
						document.body.appendChild(tt);
						tt.style.opacity = 0;
						tt.style.filter = 'alpha(opacity=0)';
						document.onmousemove = this.pos;
					}
					tt.style.display = 'block';
					c.innerHTML = v;
					tt.style.width = w ? w + 'px' : 'auto';
					if(!w && ie){
						t.style.display = 'none';
						b.style.display = 'none';
						tt.style.width = tt.offsetWidth;
						t.style.display = 'block';
						b.style.display = 'block';
					}
					if(tt.offsetWidth > maxw){tt.style.width = maxw + 'px'}
					h = parseInt(tt.offsetHeight) + top;
					clearInterval(tt.timer);
					tt.timer = setInterval(function(){tooltip.fade(1)},timer);
				}
		},
		pos:function(e){
			var u = ie ? event.clientY + document.documentElement.scrollTop : e.pageY;
			var l = ie ? event.clientX + document.documentElement.scrollLeft : e.pageX;
			tt.style.top = (u - h) + 'px';
			tt.style.left = (l + left) + 'px';
		},
		fade:function(d){
			var a = alpha;
			if((a != endalpha && d == 1) || (a != 0 && d == -1)){
				var i = speed;
				if(endalpha - a < speed && d == 1){
					i = endalpha - a;
				}else if(alpha < speed && d == -1){
					i = a;
				}
				alpha = a + (i * d);
				tt.style.opacity = alpha * .01;
				tt.style.filter = 'alpha(opacity=' + alpha + ')';
			}else{
				clearInterval(tt.timer);
				if(d == -1){tt.style.display = 'none'}
			}
		},
		hide:function(){
			if(tt) {
				clearInterval(tt.timer);
				tt.timer = setInterval(function(){tooltip.fade(-1)},timer);
			}
			
		}
	};
}();*/