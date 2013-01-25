/*!
 * Ext JS Library 3.2.1
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.onReady(function() {
	function createIcon(ct, cls, callback, scope) {
		var d = document.createElement('DIV');
		d.className = cls;
		ct.appendChild(d);
		Ext.get(d).on('click', callback, scope);
	}
	
    Ext.QuickTips.init();

    var tree = new Ext.ux.tree.TreeGrid({
       // title: 'Categories',
        width: 380,
        height: 300,
        renderTo: 'tree-grid',
        enableDD: true,

        columns:[{
            header: 'Category',
            dataIndex: 'name',
            width: 130
        }, {
            header: 'Description',
            width: 180,
            dataIndex: 'description'
        }, {
        	header: 'Tools',
        	width: 45,
        	dataIndex: 'id',
        	align: 'center',
        	renderer: function() {
        		//console.log([this, arguments]);
        		return '<div id="'+this.id+'"></div>';
        	}
        }],
        afterNodeRender: function() {
    		var ct = Ext.get(this.node.id).dom;
    		createIcon(ct, 'edit-icon', function() {
    			console.log(this);
    			alert('edit: ' + this.name);
    		}, this.node.attributes);
    		createIcon(ct, 'delete-icon', function() {
    			console.log(this);
    			alert('delete: ' + this.name);
    		}, this.node.attributes);
    		
//    		Ext.get(this.elNode).on('click', function() {
//    			console.log(this);
//    		}, this.node.attributes);
    	},
        dataUrl: 'getCategoriesTree.ajax'
    });
    tree.getSelectionModel().on('selectionchange', function(selMode, node) {
    	var n = node.attributes;
    	//console.log(n);
    	jQuery.getJSON('getContentNodesForCategory.ajax', {id: n.id}, function(data) {
    		store.loadData(data);
    	});
    });
});