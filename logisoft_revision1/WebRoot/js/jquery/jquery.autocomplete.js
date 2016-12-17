(function($){
    $.fn.extend({
        autocomplete: function(page, options, callback){
            if(typeof(page) == "undefined" ){
                alert("Autocomplete error");
            }
			
            var autoClass = 'autocomplete';
            var selClass = 'sel';
            var splitter = 'rel';
            var ele = $(this);

            $(":not(div." + autoClass + ")").click(function(){
                $("div." + autoClass).remove();
            });
			
            ele.attr("autocomplete","off");
			
            ele.keyup(function(ev){
                var param = ele.attr('name');
                var getOptions = {}
                getOptions[param]=ele.val();
                
                if( typeof(options) == "object" ){
                    autoClass = typeof( options.autoCompleteClassName ) != "undefined" ? options.autoCompleteClassName : autoClass;
                    selClass = typeof( options.selectedClassName ) != "undefined" ? options.selectedClassName : selClass;
                    splitter = typeof( options.attrCallBack ) != "undefined" ? options.attrCallBack : splitter;
                    if( typeof( options.identifier ) == "string" )
                        getOptions.identifier = options.identifier;

                    if( typeof( options.extraParamFromInput ) != "undefined" )
                        getOptions.extraParam = $( options.extraParamFromInput ).val();
                }

                kc = (( typeof( ev.charCode ) == 'undefined' || ev.charCode === 0 ) ? ev.keyCode : ev.charCode);
                key = String.fromCharCode(kc);

                //console.log(kc, key, ev );

                if (kc == 27){
                    $('div.' + autoClass).remove();
                }
                if (kc == 13){
                    $('div.' + autoClass + ' li.' + selClass).trigger('click');
                }
                if (key.match(/[a-zA-Z0-9_\- ]/) || kc == 8 || kc == 46){
                    $.get(page, getOptions, function(data){
                        $('div.' + autoClass).remove();
                        autoCompleteList = $('<div>').addClass(autoClass).html(data);
                        if (data != ''){
                            autoCompleteList.insertAfter(ele);
							
                            var position = ele.position();
                            var height = ele.height();
                            var width = ele.width();

                            $('div.' + autoClass).css({
                                'top': ( height + position.top + 6 ) + 'px',
                                'left': ( position.left )+'px',
                                'margin': '0px'
                            });
							
                            $('div.' + autoClass + ' ul').css({
                                'margin-left': '0px'
                            });
							
                            $('div.' + autoClass + ' li').each(function( n, el ){
                                el = $(el);
                                el.mouseenter(function(){
                                    $('div.' + autoClass + ' li.' + selClass).removeClass(selClass);
                                    $(this).addClass(selClass);
                                });
                                el.click(function(){
                                    ele.attr('value', el.text());

                                    if( typeof( callback ) == "function" )
                                        callback( el.attr(splitter));
									
                                    $('div.' + autoClass).remove();
                                    ele.focus();
                                });
                            });
                        }
                    },"html");
                }
                if (kc == 38 || kc == 40){
                    if ($('div.' + autoClass + ' li.' + selClass).length == 0){
                        if (kc == 38){
                            $($('div.' + autoClass + ' li')[$('div.' + autoClass + ' li').length - 1]).addClass(selClass);
                        } else {
                            $($('div.' + autoClass + ' li')[0]).addClass(selClass);
                        }
                    }else{
                        sel = false;
                        $('div.' + autoClass + ' li').each(function(n, el){
                            el = $(el);
                            if ( !sel && el.hasClass(selClass) ){
                                el.removeClass(selClass);
                                $($('div.' + autoClass + ' li')[(kc == 38 ? (n - 1) : (n + 1))]).addClass(selClass);
                                sel = true;
                            }
                        });
                    }
                    $('div.' + autoClass).scrollTo($('div.' + autoClass + ' li.' + selClass).offset().height);
                }
                if (ele.val() == ''){
                    $('div.' + autoClass).remove();
                }
            });
        }
    });
})(jQuery);