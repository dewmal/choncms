Ext.ns("display.lists");

display.lists.helper = {
	getRadios: function() {
		var radios = Ext.query('input[name=selectListType]');
		return radios; 
	},
	
	getValues: function() {
		var radios = display.lists.helper.getRadios();
		var rv = [];
		for(var i=0; i<radios.length; i++) {
			var r = radios[i];
			rv.push(r.value);
		}
		return rv;
	},
	
	getSelectedRadio: function() {
		var radios = display.lists.helper.getRadios();
		var rv = [];
		for(var i=0; i<radios.length; i++) {
			var r = radios[i];
			if(r.checked) {
				return r;
			}
		}
	},
	
	getSelectedRadioValue: function() {
		var r = display.lists.helper.getSelectedRadio();
		if(r) {
			return r.value;
		}
	},
	
	hideAll: function() {
		var radio_values = display.lists.helper.getValues();
		for(var i=0; i<radio_values.length; i++) {
			var v = radio_values[i];
			Ext.get(v+'ListContent').dom.style.display='none';
		}
	},
	
	expand: function(v) {
		Ext.get(v+'ListContent').dom.style.display='block';
	},
	
	makeSortable: function() {
		$(".displayList").sortable({
			axis: 'y', 
			containment: 'parent',
			delay: 5,
			helper: 'original' ,
			items: 'li',
			tolerance: 'pointer',
			update: function() {
				
			}
		});
	}
};

display.lists._init = function() {
	display.lists.helper.hideAll();
	var radios = display.lists.helper.getRadios();
	for(var i=0; i<radios.length; i++) {
		var r = radios[i];
		r.onclick = (function(v) {
			return function() {
				display.lists.helper.hideAll();
				display.lists.helper.expand(v);
			}
		})(r.value);
	}
}

Ext.onReady(function() {
	display.lists._init();
	display.lists.helper.makeSortable();
});


function selectWin() {
	if(window._win) {
		_win.show();
	} else {
			var cfg = {
				//filter: 'sys.public,telegraf.category,category.ct.year,category.ct.month,telegraf.vest'
			}
			var chonExplTree = new chon.Explorer.Tree(cfg);
		_win = new Ext.Window({
			title: 'Add Dispplay List Node',
			width: 400,
			height: 500,
			layout: 'fit',
			items: chonExplTree,
			buttons: [{
				text: 'Cancel',
				handler: function() {
					_win.hide();
				}
			}, {
				text: 'Select',
				handler: function() {
					//TODO
					var sm = chonExplTree.getSelectionModel();
					var node = sm.getSelectedNode();
					if(node) {
						var path = node.attributes.path;
						var name = node.attributes.text;
						var type = node.attributes.type;
						var title = node.attributes.title || '';
						var selNodeId = node.attributes.id;
						
						var txt = $('#li_item_tpl').html();
						txt = txt.replace(/\$it.name/g, name);
						txt = txt.replace(/\$it.absPath/g, path);
						txt = txt.replace(/\$it.type/g, type);
						txt = txt.replace(/\$it.title/g, title);
						
						$('<li />').html(txt).appendTo($('.displayList'));
						_win.hide();
					} else {
						Ext.Msg.alert("No Item Selected", "Please select category where you want to pubish current node");
					}
				}
			}]
		});
		_win.nodeId = id;
		_win.show();
	}
}

preselectNodeType = function(type) {
	if(type) {
		var rds = display.lists.helper.getRadios();
		for(var i=0; i<rds.length;i++) {
			if(rds[i].value==type) {
				rds[i].checked = true;
			}
		}
		display.lists.helper.expand(type);
	}
}

saveDisplayList = function(name) {
	var listType = display.lists.helper.getSelectedRadioValue();
	if(!listType) {
		alert('Please select list type (click on radio button)');
	}
	
	var ls = [];
	$('input[name=node_path]', '.displayList').each(function() {
		ls.push($(this).val());
	});
	var title = $('input[name=displayListTitle]').val();
	var req = JSON.encode({
		name: name, 
		type: listType, 
		list: ls,
		title: title
	});
	
	jQuery.post('display.lists.savelist.ajax', { req: req } , function(resp) {
		if(resp == 'SUCCESS') {
			Ext.Msg.alert('Saved', 'Save operation successful!');
		} else {
			Ext.Msg.alert('Oooppps, an error occured', resp);
		}
	});
}

deleteListItem = function(btn) {
	Ext.Msg.confirm('Please confirm', 'Are you sure you want to delete this item?', function(b) {
		if(b=='yes') {
			$(btn).parents('li').filter(':first').remove()
		}
	});
}