Ext.ns('chon');

chon.Explorer = function(cfg) {
	this._init(cfg)
}
chon.Explorer.ACTIONS = {
	'COPY': function(node) {
		this._node = node;
		this._op='copy';
	},
	'CUT': function(node) {
		this._node = node;
		this._op='cut';
	},
	'PASTE': function(node) {
		if(this._op=='copy' || this._op=='cut') {
			console.log('TODO: ' + this._op + ' node ', this._node.path, ' into ', node.path);
			//this._node = node;
			//TODO copy \
			this._op=='paste'
		}
	},
	
	'EDIT': function(node) {
		document.location.href = siteUrl + '/admin/content.edit.do?path='+node.path;
	},
	'PREVIEW': function(node) {
		//var p=node.path.substring('/www'.length);
		document.location.href = siteUrl + node.path;
	},
	'DELETE': function(node) {
		Ext.Msg.confirm("Are you sure you want to delete \""+node.name+"\"?",
				"Node on path " + node.path + " will be permanenly removed and all its childs will be deleted. Are you sure you want continue?", 
		function(btn) {
			if(btn == 'yes') {
				jQuery.getJSON('deleteNode.ajax', {id: node.id}, function(r) {
					if(r.msg == 'OK') {
						document.location.href='index.do';
					} else {
						alert(r.msg + '\n\n' + r.error);
					}
				});
			}
		});
	},
	'CREATE-HTML': function(node) {
		document.location.href = siteUrl + '/admin/content.do?pid='+node.id;
	},
	'CREATE-CATEGORY': function(node) {
		document.location.href = siteUrl + '/admin/category.do?pid='+node.id;
	}
}

chon.Explorer.prototype = {
	history: [],
	historyIndex: -1,
	
	//private methods
	_init: function(config) {
		this.config = config;
		this._initServer();
	},
	
	_initServer: function() {
		var _this = this;
		//prepare session, read server configuration
		jQuery.post('explorer.init.ajax', {config: JSON.encode(this.config)}, function(data) {
    		_this._initClient(JSON.decode(data));
    	});
	},
	
	//create UI
	_initClient: function(srvData) {
		this.srvConfig = srvData.config;
		
		//init context menus used in tree and grid
		this._createContextMenus();
		
		//create left navigation tree
		this.tree = new chon.Explorer.Tree({explorer: this });
		
	    // create the Grid
	    this.grid = new chon.Explorer.Grid({explorer: this });
	    
	    // layout into main container
		this._createMainPanel();
	},
	
	_loadNodeForGrid: function(node, doNotUpdateCmb) {
		if(!node) {
			node = this.history[this.history.length-1];
		}
		this.pathCmb.setValue(node.path);
		if(!doNotUpdateCmb) {
			var index = this.pathCmb.store.indexOfId(node.id);
			if(index!=-1) {
				this.pathCmb.store.removeAt(index)
			}
			this.pathCmb.store.insert(0, new this.pathCmb.store.recordType(node, node.id));
			if(this.history.length>0) {
				this.historyIndex = this.history.length-1;
				this.backButton.setDisabled(false);
			}
			this.history.push(node);
			this.forwButton.setDisabled(true);
		}
		
		var id = node.id;
		var _this = this;
		jQuery.getJSON('explorer.grid.ajax', {id: id}, function(data) {
    		_this.grid.store.loadData(data);
    		//TODO: fire events
    	});
	},
	
	_createMainPanel: function() {
		var _this = this;
		var config = this.config;
		return new Ext.Panel({
		    renderTo: config.renderTo,
		    width: '100%',
		    height: config.height,
		    //title: 'Border Layout',
		    layout: 'border',
		    items: [{
		        //title: 'North Region',
		        region: 'north',     // position for region
		        height: 40,
		        //split: true,         // enable resizing
		        //minSize: 75,         // defaults to 50
		        //maxSize: 150,
		        margins: '2 2 0 2',
		        items: new Ext.Panel({
		        	layout:'column',
		        	border: false,
		        	style: 'padding: 8px 4px 4px 4px',
		        	items: [
		        	        this.backButton = new Ext.Button({text: '<b>&lt;</b>', width: 24, disabled: true,
		        	        					handler: function() {
		        	        						var n = _this.history[_this.historyIndex--];
		        	        						if(_this.historyIndex<0) {
		        	        							this.setDisabled(true);
		        	        						}
		        	        						_this._loadNodeForGrid(n, true);
		        	        						_this.forwButton.setDisabled(false);
		        	        					}
		        	        				}),
		        	        this.forwButton = new Ext.Button({text: ' <b>&gt;</b> ', width: 24, disabled: true, style: 'margin-left: 3px;',
		        	        					handler: function() {
		        	        						_this.historyIndex++;
		        	        						var n = _this.history[_this.historyIndex+1];
		        	        						if(_this.historyIndex>=_this.history.length-2) {
		        	        							this.setDisabled(true);
		        	        						}
		        	        						_this._loadNodeForGrid(n, true);
		        	        						_this.backButton.setDisabled(false);
		        	        					}
		        	        				}),
		        	      //  new Ext.Panel({html: 'Path:', border: false, style: 'margin-left: 10px; margin-right: 0px; margin-top: 2px',}),
		        	        new Ext.Panel({
		        	        	layout: 'fit',
		        	        	border: false,
		        	        	columnWidth: .75, 
		        	        	style: 'margin-left: 5px; margin-right: 5px',
		        	        	items: this.pathCmb = new Ext.form.ComboBox({
		        	        		mode: 'local',
		        	        		store: new Ext.data.JsonStore({
		        	        			id: 'id',
		        	        			fields: ['name', 'path', 'id']
		        	        		}),
		        	        		triggerAction: 'all',
		        	        		displayField: 'path',
		        	        		valueField: 'path',
		        	        		listeners: {
		        	        			select: function(cmb, r, index) {
		        	        				_this._loadNodeForGrid(r.data, true);
		        	        			}
		        	        		}
		        	        	})
		        	        }), new Ext.app.SearchField({columnWidth: .25, action: 'explorer.search.ajax', paramName: 'q', explorer:  _this })]
		        })
		    }, {
		        // xtype: 'panel' implied by default
		        title: config.title,
		        region:'west',
		        margins: '2 0 2 2',
		        width: 300,
		        collapsible: true,   // make collapsible
		        cmargins: '2 2 2 2', // adjust top margin when collapsed
		        id: 'west-region-container',
		        split: true,         // enable resizing
		        layout: 'fit',
		        items: this.tree
		        //unstyled: true
		    },{
		        //title: 'Center Region',
		        region: 'center',     // center region is required, no width/height specified
		        xtype: 'container',
		        layout: 'fit',
		        margins: '2 2 2 0',
		        items: this.grid
		        //html: 'JOCO'
		    }]
		});
	},
	
	_createContextMenus: function() {
		var menusCfg = this.srvConfig.contextMenu;
		this.menus = {};
		for(var k in menusCfg) {
			var items = [];
			var cfgArr = menusCfg[k];
			//console.log('typeof cfgArr = ' + (typeof cfgArr)); 
			if(typeof cfgArr == 'string') {
				cfgArr = menusCfg[cfgArr];
			}
			
			for(var i=0; i<cfgArr.length; i++) {
				var mi = cfgArr[i];
				if(typeof mi == 'string') {
					items.push(mi);
				} else {
					items.push({text: mi.name, cfg: mi});					
				}
			}
			
			this.menus[k] = {
				cfg: cfgArr,
				menu: new Ext.menu.Menu({
	    			id: 'menu-'+k,
	    			items: items
	    		})
			}
		}
	},
	
	getMenu: function(node) {
		var _this = this;
		var m = this.menus[node.type];
		if(m && m.menu && m.menu.items) {
			var items = m.menu.items.items;
			for(var i=0; i<items.length; i++) {
				items[i].setHandler(function() {
					_this._processContextMenuAction(this);
				}, {menu: m, node: node, cfg: items[i].cfg});
			}
		}
		return m;
	},
	
	getDblClickAction: function(node) {
		var actions = this.srvConfig.dblClick;
		//console.log(actions);
		if(actions && actions[node.type]) {
			var a = actions[node.type]
			if(a && a.action) {
				return chon.Explorer.ACTIONS[a.action];
			}
		}
		return null;
	},
	
	_processContextMenuAction: function(o) {
		var cfg = o.cfg;
		var node = o.node;
		if(typeof chon.Explorer.ACTIONS[cfg.action] == 'function') {
			chon.Explorer.ACTIONS[cfg.action].apply(this, [node, cfg, o]);
		} else {
			console.log("ERROR: no action " + cfg.action + " on node " + node.type);
		}
	}
}
