Ext.ns('Ext.app');
/*
 * Ext JS Library 2.0
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.app.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        Ext.app.SearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
        this.store = this.explorer.grid.store 
    },

    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width:180,
    hasSearch: false,
    paramName: 'query',
    action: 'search.ajax',

    onTrigger1Click : function(){
        if(this.hasSearch){
            this.el.dom.value = '';
            var o = {start: 0};
            this.store.baseParams = this.store.baseParams || {};
            this.store.baseParams[this.paramName] = '';
            //this.store.reload({params:o});
            //var _this = this;
            //o[this.paramName]='';
            if('Search Results' == this.explorer.pathCmb.getValue()) {
            	this.explorer._loadNodeForGrid(null, true);
            }
            this.triggers[0].hide();
            this.hasSearch = false;
        }
    },

    onTrigger2Click : function(){
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
        var o = {start: 0};
        this.store.baseParams = this.store.baseParams || {};
        this.store.baseParams[this.paramName] = v;
        //this.store.reload({params:o});
        o[this.paramName]=v;
        var _this = this;
        jQuery.getJSON(this.action, o, function(data) {
    		_this.store.loadData(data);
    		_this.explorer.pathCmb.setValue("Search Results");
    	});
        this.hasSearch = true;
        this.triggers[0].show();
    }
});