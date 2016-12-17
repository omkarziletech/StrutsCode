/**
 * @author Lakshminarayanan
 * @description This AutoCompleter is build by extending Ajax AutoCompleter<br>for fixing Scroll View Issue
 */
function trim(str) {
    return str.replace(/^\s+|\s+$/g, "");
}

Ajax.ScrollAutocompleter = Class.create();
Object.extend(Object.extend(Ajax.ScrollAutocompleter.prototype, Autocompleter.Base.prototype), {
    initialize: function(element, update, url, options) {
        this.baseInitialize(element, update, options);
        this.options.asynchronous = true;
        this.options.onComplete = this.onComplete.bind(this);
        this.options.defaultParams = this.options.parameters || null;
        this.url = url;
        this.cache = {};
    },

    getUpdatedChoices: function() {
        var t = this.getToken();
        entry = encodeURIComponent(this.options.paramName) + '=' +
        encodeURIComponent(t);

        this.options.parameters = this.options.callback ?
        this.options.callback(this.element, entry) : entry;

        if(this.options.defaultParams)
            this.options.parameters += '&' + this.options.defaultParams;

        new Ajax.Request(this.url, this.options);
    },

    onComplete: function(request) {
        this.updateChoices(this.cache[this.getToken()] = request.responseText);
    },
    render: function() {
        if(this.entryCount > 0) {
            for (var i = 0; i < this.entryCount; i++) {
                this.index==i ?
                Element.addClassName(this.getEntry(i),"selected") :
                Element.removeClassName(this.getEntry(i),"selected");
                if (this.index == i) {
                    var element = this.getEntry(i);
                    element.scrollIntoView(false);
                }
            }
            if(this.hasFocus) {
                this.show();
                this.active = true;
            }
        } else {
            this.active = false;
            this.hide();
        }
    },
    renderForMouseOver: function() {
        if(this.entryCount > 0) {
            for (var i = 0; i < this.entryCount; i++) {
                this.index==i ?
                Element.addClassName(this.getEntry(i),"selected") :
                Element.removeClassName(this.getEntry(i),"selected");
                if (this.index == i) {
                    var element = this.getEntry(i);
                //element.scrollIntoView(false);
                }
            }
            if(this.hasFocus) {
                this.show();
                this.active = true;
            }
        } else {
            this.active = false;
            this.hide();
        }
    },
    onHover: function(event) {
        var element = Event.findElement(event, 'LI');
        if(this.index != element.autocompleteIndex)
        {
            this.index = element.autocompleteIndex;
            this.renderForMouseOver();
        }
        Event.stop(event);
    },
    markPrevious: function() {
        if(this.index > 0) {
            this.index--;
        }
        else {
            this.index = this.entryCount-1;
            this.update.scrollTop = this.update.scrollHeight;
        }
        selection = this.getEntry(this.index);
        selection_top = selection.offsetTop;
        if(selection_top < this.update.scrollTop){
            this.update.scrollTop = this.update.scrollTop-selection.offsetHeight;
        }
    },

    markNext: function() {
        if(this.index < this.entryCount-1) {
            this.index++;
        }
        else {
            this.index = 0;
            this.update.scrollTop = 0;
        }
        selection = this.getEntry(this.index);
        selection_bottom = selection.offsetTop+selection.offsetHeight;
        if(selection_bottom > this.update.scrollTop+this.update.offsetHeight){
            this.update.scrollTop = this.update.scrollTop+selection.offsetHeight;
        }
    },

    updateChoices: function(choices) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = choices;
            Element.cleanWhitespace(this.update);
            Element.cleanWhitespace(this.update.down());

            if (this.update.firstChild && this.update.down().childNodes) {
                this.entryCount =
                this.update.down().childNodes.length;
                for (var i = 0; i < this.entryCount; i++) {
                    var entry = this.getEntry(i);
                    entry.autocompleteIndex = i;
                    this.addObservers(entry);
                }
            } else {
                this.entryCount = 0;
            }

            this.stopIndicator();
            this.update.scrollTop = 0;
            this.index = 0;

            if (this.entryCount==1 && this.options.autoSelect) {
                this.selectEntry();
                this.hide();
            } else {
                this.render();
            }
        }
    }
});
/**
 * @author Lakshminarayanan
 * @description This AutoCompleter is build based on Ajax ScrollAutocompleter
 * @param textField - Input field name
 * @param divToPopulate - Div where the result will be populated.
 * @param idField - Id to be stored(optional)
 * @param validateField - Validate Field used to validate the input
 * @param url - request path
 * @param afterUpdate - It is callback function can be used after getting response from server.
 * @param addMoreParams - used to add more request params. Should be included
 * @param afterBlur - It is callback function can be used after the element got blurred.
 */
function AjaxAutocompleter(textField,divToPopulate,idField,validateField,url,afterUpdate,addMoreParams,afterBlur) {
    new Ajax.ScrollAutocompleter(textField, divToPopulate, url, {
        callback: function(element, entry){
            if(addMoreParams != null && trim(addMoreParams) != "") {
                params = eval(addMoreParams);
                if(null!=params && trim(params)!=""){
                    entry = entry + params;
                }
            }
            return entry;
        },
        paramName: textField,
        tokens:"<-->",
        afterUpdateElement : function (text, li) {
            if(li.id!="No Record"){
                if($(idField)!= undefined){
                    $(idField).value = li.id;
                }
                if($(validateField)!= undefined){
                    $(validateField).value = text.value;
                }
                $(textField).blur();
                if(afterUpdate != null && trim(afterUpdate) != "") {
                    eval(afterUpdate);
                }
            }else{
                $(textField).value = "";
                $(validateField).value = "";
                $(textField).focus();
            }
        }
    });

    Event.observe(textField, "blur", function (event){
        var element = Event.element(event);
        if($(validateField)!=undefined && element.value!=$(validateField).value){
            element.value = '';
            if($(idField)!= undefined){
                $(idField).value = '';
            }
            if(afterBlur != null && trim(afterBlur) != "") {
                eval(afterBlur);
            }
            if($(validateField)!= undefined){
                $(validateField).value = '';
            }
        }
    });
}
