(function() {
    jQuery.fn.fileInput = function(){
	return this.each(function(){
	    var fileInput = jQuery(this);
	    fileInput.addClass("customfile-input");
	    //create custom control container
	    var upload = jQuery('<div class="customfile"></div>');
	    //create custom control button
	    var uploadButton = jQuery('<span class="customfile-button" aria-hidden="true">Browse</span>').appendTo(upload);
	    //create custom control feedback
	    var uploadFeedback = jQuery('<span class="customfile-feedback" aria-hidden="true">No file selected...</span>').appendTo(upload);
	    upload.insertAfter(fileInput);
	    upload.mousemove(function(e){
		fileInput.css({
		    'left': e.pageX - upload.offset().left - fileInput.outerWidth() + 20, //position right side 20px right of cursor X)
		    'top': e.pageY - upload.offset().top - jQuery(window).scrollTop() - 3
		});	
	    });
	    fileInput.appendTo(upload);
	    fileInput.mouseover(function(){ 
		upload.addClass("customfile-hover"); 
	    });
	    fileInput.mouseout(function(){ 
		upload.removeClass("customfile-hover"); 
	    });
	    fileInput.bind("disable",function(){
		fileInput.attr("disabled",true);
		upload.addClass("customfile-disabled");
	    });
	    fileInput.bind("enable",function(){
		fileInput.removeAttr("disabled");
		upload.removeClass("customfile-disabled");
	    });
	    fileInput.bind("checkChange", function(){
		if(fileInput.val() && fileInput.val() !== fileInput.data('val')){
		    fileInput.trigger("change");
		}
	    });
	    fileInput.bind('change',function(){
		//get file name
		var fileName = jQuery(this).val().split(/\\/).pop();
		if(jQuery.trim(fileName) !== ""){
		    //get file extension
		    var fileExt = 'customfile-ext-' + fileName.split('.').pop().toLowerCase();
		    //update the feedback
		    uploadFeedback.text(fileName); //set feedback text to filename
		    uploadFeedback.removeClass(uploadFeedback.data('fileExt') || ''); //remove any existing file extension class
		    uploadFeedback.addClass(fileExt); //add file extension class
		    uploadFeedback.data('fileExt', fileExt); //store file extension for class removal on next change
		    uploadFeedback.addClass('customfile-feedback-populated'); //add class to show populated state
		    //change text of button	
		    uploadButton.text('Change');	
		}else{
		    uploadFeedback.text("No file selected...");
		    uploadFeedback.removeClass(uploadFeedback.data('fileExt') || ''); //remove any existing file extension class
		    uploadFeedback.removeClass("customfile-feedback-populated"); //remove any existing file extension class
		    uploadFeedback.data('fileExt', ""); //store empty file extension
		    //change text of button	
		    uploadButton.text('Browse');	
		}
	    });
	    fileInput.click(function(){ //for IE and Opera, make sure change fires after choosing a file, using an async callback
		fileInput.data('val', fileInput.val());
		setTimeout(function(){
		    fileInput.trigger('checkChange');
		},100);
	    });
	});
    };
})(jQuery);