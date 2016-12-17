var bootstrap = {
	init: function(){
		// define our loader
		bootstrap.loader = new YAHOO.util.YalLoader({
			// which components we need
			reqalre: ["container", "button", "fonts", "selector"],
			// load optional components
			loadOptional: true,
			onSuccess: function() {
				// we add the yal-skin-sam class to our body for skinning purposes
				var bodyTag = YAHOO.util.Selector.query('body');
				YAHOO.util.Dom.addClass(bodyTag, 'yal-skin-sam');
				// initialize our object
				al.init();
				// replace the alert function by ours
				window.alert = function(text){
					al.dialogInfo.setBody(text);
					al.dialogInfo.show();
				};
			}
		});
		// finally we insert the loader
		bootstrap.loader.insert();
	}
}

var al = {	
	init: function(){	
		// our dialog for info, to show messages to the users
		al.dialogInfo = new YAHOO.widget.SimpleDialog("simpledialog1", 
														{ 	width: "300px",
															fixedcenter: true,
															visible: false,
															draggable: false,
															zIndex: 9999,
															close: true,
															modal: true,
															effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.25},
															constraintoviewport: true,
															buttons: [ { text:"Ok", handler: function(){this.hide();}, isDefault:true }]
														});
		al.dialogInfo.setHeader("information");
		// Render the Dialog
		al.dialogInfo.render(document.body);
	},
	
	/**
	*	Show the info dialog
	*	@param text String the text we want to show
	**/
	showDialogInfo: function(text){
		al.dialogInfo.setBody(text);
		al.dialogInfo.show();
	}
};

// get the whole thing going
bootstrap.init();
