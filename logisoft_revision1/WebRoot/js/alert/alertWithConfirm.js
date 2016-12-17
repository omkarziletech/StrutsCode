var confirmValue;
var bootstrap = {
	init: function(){
		// define our loader
		bootstrap.loader = new YAHOO.util.YUILoader({
			// which components we need
			require: ["container", "button", "fonts", "selector"],
			// load optional components
			loadOptional: true,
			onSuccess: function() {
				// we add the yui-skin-sam class to our body for skinning purposes
				var bodyTag = YAHOO.util.Selector.query('body');
				YAHOO.util.Dom.addClass(bodyTag, 'yui-skin-sam');
				// initialize our object
				ui.init();
				// replace the alert function by ours
				window.confirm = function(text,value){
				alert(text);
				confirmValue=value;
				
					ui.dialogInfo.setBody(text);
					ui.dialogInfo.show();
				};
			}
		});
		// finally we insert the loader
		bootstrap.loader.insert();
	}
}

var ui = {	
	init: function(){
		// our dialog for info, to show messages to the users
		ui.dialogInfo = new YAHOO.widget.SimpleDialog("simpledialog1", 
														{ 	width: "300px",
															fixedcenter: true,
															visible: false,
															draggable: false,
															zIndex: 9999,
															close: false,
															modal: true,
															effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.25},
															constraintoviewport: true,
														 buttons: [ { text:"Yes", handler:function(){confirmValue;this.hide();}, isDefault:true },   
                           											{ text:"No",  handler:function(){this.hide();} } ]   
             											} );  
 

		ui.dialogInfo.setHeader("Confirmation");
		// Render the Dialog
		ui.dialogInfo.render(document.body);
	},
	
	/**
	*	Show the info dialog
	*	@param text String the text we want to show
	**/
	showDialogInfo: function(text){
		ui.dialogInfo.setBody(text);
		ui.dialogInfo.show();
	}
};
 

// get the whole thing going
bootstrap.init();
