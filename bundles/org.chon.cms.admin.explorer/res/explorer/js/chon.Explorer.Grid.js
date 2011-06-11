chon.Explorer.Grid = function(config) {
	config = config || {};
	var store = new Ext.data.JsonStore({
        fields: [
           {name: 'id'},
           {name: 'name'},
           {name: 'dateModified', type: 'date'},
           {name: 'type'},
           {name: 'cls'},
           {name: 'details'}
        ]
    });
	var columns = [
	            {header: 'Name', dataIndex: 'name', renderer: function(v, a, r) {
	            		var cls = r.data.cls;
	            		var icon = '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-tree-node-icon" unselectable="on" />';
	            		var link = '<span unselectable="on" style="padding:4px;">'+v+'</span>';
	            		return  '<div class="x-tree-node-collapsed '+cls+'">' + icon + link + '</div>';
	            	}
	            , sortable: true},
	            {header: 'Date Modified', width: 40, dataIndex: 'dateModified', type: 'date', sortable: true, align:'center', renderer: function(date) { return date && date.format("d/m/Y H:i"); }},
	            {header: 'Type', width: 30, dataIndex: 'type', align:'center', sortable: true}
	        ];
	
	Ext.apply(config, {
		store: store,
        columns: columns,
        stripeRows: true,
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true,
            getRowClass : function(record, rowIndex, p, store){
                if(this.showPreview){
                    p.body = '<p>'+record.data.details+'</p>';
                    return 'x-grid3-row-expanded';
                }
                return 'x-grid3-row-collapsed';
            }
        }
	});
	chon.Explorer.Grid.superclass.constructor.call(this, config);
	this._initEvents();
}

Ext.extend(chon.Explorer.Grid, Ext.grid.GridPanel, {
	_initEvents: function() {
		// enter into node on double click
		this.on('dblclick', function() {
			var item = this.getSelectedItem();
			var action = this.explorer.getDblClickAction(item);
			if(typeof action == 'function') {
				action.apply(this, [item]);
			} else {
				this.explorer._loadNodeForGrid(item);
			}
        }, this);
		
		// context menus
		this.on('contextmenu', function(e) {
			var item = this.getSelectedItem();
			if(!item) {
				console.log("TODO: auto select item under mouse ... ");
				return;
			}
        	var m = this.explorer.getMenu(item);
        	if(m && m.menu) {
        		m.menu.showAt(e.xy);
        		e.preventDefault();
        	}
        }, this);
	},
	
	getSelectedItem: function() {
		//TODO: multiple selection handling
		var sel = this.getSelectionModel().getSelections();
    	var o = sel[0].json;
		return o;
	}
})