BrowseCmp = {
	create: function(ct, actionCreator, attr) {
		ct.innerHTML = ['<table width="100%">',
		        	'<tr>',
		        		'<td> <div> Categories (Folders)</div> <div id="tree-grid"></div></td>',
		        		'<td> <div> Items for selected category (Content)</div> <div id="content-grid"></div></td>',
		        	'</tr>',
		        	'<tr>',
	        			'<td colspan="2" id="BrowseCmp-selection"></td>',
	        		'</tr>',
		        '</table>'].join('');
		if(!attr) attr={};
		this.createTree('tree-grid', attr);
		this.createGrid('content-grid', attr);
		this.actionCreator = actionCreator;
		this.rendered = true;
	},
	
	createIcon: function(ct, cls, callback, scope) {
		var d = document.createElement('DIV');
		d.className = cls;
		ct.appendChild(d);
		Ext.get(d).on('click', callback, scope);
	},
	
	createTree: function(ct, attr) {
		var _this = this;
		 var tree = new Ext.ux.tree.TreeGrid({
		       // title: 'Categories',
		        width: 420,
		        height: attr.height || 300,
		        renderTo: ct,
		        enableDD: true,

		        columns:[{
		            header: 'Category',
		            dataIndex: 'name',
		            width: 230
		        }, {
		            header: 'Description',
		            width: 170,
		            dataIndex: 'description'
		        }/*, {
		        	header: 'Tools',
		        	width: 45,
		        	dataIndex: 'id',
		        	align: 'center',
		        	renderer: function() {
		        		//console.log([this, arguments]);
		        		return '<div id="'+this.id+'"></div>';
		        	}
		        }*/],
		        /*
		        afterNodeRender: function() {
		    		var ct = Ext.get(this.node.id).dom;
		    		_this.createIcon(ct, 'edit-icon', function() {
		    			console.log(this);
		    			alert('edit: ' + this.name);
		    		}, this.node.attributes);
		    		_this.createIcon(ct, 'delete-icon', function() {
		    			console.log(this);
		    			alert('delete: ' + this.name);
		    		}, this.node.attributes);
		    		
//		    		Ext.get(this.elNode).on('click', function() {
//		    			console.log(this);
//		    		}, this.node.attributes);
		    	},*/
		        
		        dataUrl: 'getCategoriesTree.ajax'
		    });
		    tree.getSelectionModel().on('selectionchange', function(selMode, node) {
		    	var n = node.attributes;
		    	_this.grid.getSelectionModel().clearSelections(true)
		    	_this.displaySelection(n.type, n.name, n.description, n);
		    	//console.log(n);
		    	var action;
		    	if(n.type=='movie_year') {
		    		action = 'getMoviesForYearNode'
		    	} else if(n.type='category'){
		    		action = 'getContentNodesForCategory'
		    	} else {
		    		throw 'Invalid node type'
		    	}
		    	
		    	jQuery.getJSON(action+'.ajax', {id: n.id}, function(data) {
		    		_this.gridStore.loadData(data);
		    	});
		    });
		this.tree = tree;
	},
	
	createGrid: function(ct, attr) {
		// create the data store
		var _this = this;
	    store = new Ext.data.JsonStore({
	        fields: [
	           {name: 'id'},
	           {name: 'name'},
	           {name: 'introText'},
	           {name: 'path'},
	           {name: 'type'}
	        ]
	    });
	    
	    // create the Grid
	    var grid = new Ext.grid.GridPanel({
	        store: store,
	        columns: [
	            {header: 'Name', width: 160, dataIndex: 'name', renderer: function(v) { return '<b>'+v+'</b>'}},
	            {header: 'Tools', width: 45, dataIndex: 'id', renderer: function(v) { return '<div id="'+v+'"></div>'}}
	        ],
	        stripeRows: true,
	        height: attr.height || 300,
	        width: 550,
	     // customize view config
	        viewConfig: {
	            forceFit:true,
	            enableRowBody:true,
	            showPreview:true,
	            getRowClass : function(record, rowIndex, p, store){
	                if(this.showPreview){
	                    p.body = '<p>'+record.data.introText+'</p>';
	                    return 'x-grid3-row-expanded';
	                }
	                return 'x-grid3-row-collapsed';
	            }
	        },
	        sm: new Ext.grid.RowSelectionModel({singleSelect:true})

	        //title: 'Array Grid',
	        // config options for stateful behavior
	        //stateful: true,
	        //stateId: 'grid'        
	    });
	    // render the grid to the specified div in the page
	    grid.render(ct);
	    grid.getSelectionModel().on('selectionchange', function(e) {
	    	if(e.selections.items.length) {
	    		var n = e.selections.items[0].data;
	    		//console.log(n);
	    		_this.tree.getSelectionModel().clearSelections(true)
	    		_this.displaySelection(n.type, n.name, n.introText, n);
	    	}
	    });
	    this.gridStore = store;
	    this.grid = grid;
	},
	
	displaySelection: function(type, name, txt, n) {
		var buf = ['<div class="x-grid3-header" style="border: solid #99bbe8 1px; border-bottom:none">',
		           '<div style="padding: 5px;">',
		           '<div style="padding-left: 10px; float: left;"> Selected: ',n.path,'</div>',
		           '<div id="BrowseCmp-selection-actions" style="text-align: right; height: 16px;"></div>',
		           '</div>',
		           '</div>',
		           
		       '<div style="border: solid #99bbe8 1px; min-height: 40px"><table width="100%" cellpadding="0" cellspacing="0">',
		   		'<tr>',
				'<td style="width: 74px">',
					'<div class="',type,'-icon-64"></div>',
				'</td>',
				'<td>',
					'<div class="sel-title">',name,'</div>',
					'<p> ',txt,' </p>',
				'</td>',
			'</tr>',
		'</table></div>'].join('');
		jQuery('#BrowseCmp-selection').html(buf);
		if(this.actionCreator) {
			this.actionCreator.apply(this, [Ext.get('BrowseCmp-selection-actions').dom, type, n]);
		}
	}
}