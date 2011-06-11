chon.Explorer.Tree = function(config) {
	config = config || {};
	Ext.apply(config, {
		useArrows: true,
        autoScroll: true,
        animate: true,
        enableDD: true,
        containerScroll: true,
        border: false,
        // auto create TreeLoader
        dataUrl: 'explorer.treeItem.ajax' + (config.filter ? ('?filter='+config.filter) : ''),

        rootVisible: false,
        
        root: {
            nodeType: 'async',
            text: 'JCR Content Repository',
            draggable: false,
            id: 'root'
        }
	});
	chon.Explorer.Tree.superclass.constructor.call(this, config);
	this._initEvents();
}

Ext.extend(chon.Explorer.Tree, Ext.tree.TreePanel, {
	_initEvents: function() {
		if(this.explorer) {
			// on load event expand www node
			this.getLoader().on('load', function(loader, node) {
				if(node.attributes.cls=="type-root") {
					//select www node, load in grid
					this.getSelectionModel().select(node);
					this.explorer.historyIndex=0;
				}
			}, this);
			
			// context menus
			this.on('contextmenu', function(node, e) {
				var n = node.attributes;
				var m = this.explorer.getMenu(n);
				if(m && m.menu) {
					m.menu.showAt(e.xy);
				}
			}, this);
			
			// update grid when selection changes
			this.getSelectionModel().on('selectionchange', function(selMode, node) {
		    	var n = node.attributes;
		    	this.explorer._loadNodeForGrid(n);
		    }, this);
		}
	}
})