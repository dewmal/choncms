Ext.ns('chon.Menu');
chon.Menu.Tree = function(config) {
	config = config || {};
	Ext.apply(config, {
		useArrows: true,
		autoScroll: true,
		animate: true,
		enableDD: true,
		containerScroll: true,
		border: false,
		rootVisible: false,
        // auto create TreeLoader
        dataUrl: 'menu.getItems.ajax?name='+config.menuName,
        
        root: {
            nodeType: 'async',
            text: 'Chon Menu Root',
            draggable: false,
            id: 'root'
        }
	});
	chon.Menu.Tree.superclass.constructor.call(this, config);
	this._initEvents();
}

Ext.extend(chon.Menu.Tree, Ext.tree.TreePanel, {
	_initEvents: function() {
		// on load event expand www node
		this.getLoader().on('load', function(loader, node) {
		
		}, this);
		
		// context menus
		this.on('contextmenu', function(node, e) {
//			var n = node.attributes;
			
//			if(m && m.menu) {
//				m.menu.showAt(e.xy);
//			}
		}, this);
		
		// update grid when selection changes
		this.getSelectionModel().on('selectionchange', function(selMode, node) {
//	    	var n = node.attributes;
//	    	this.explorer._loadNodeForGrid(n);
	    }, this);
		
	}
})