preview = function() {
	save(function() {
		document.location.href=siteUrl + '/' + node.path;
	});
};

publish = function() {
	var destPath = jQuery('#dest').html();
	jQuery.post('content.publish.ajax', {srcId: node.id, destPath: destPath }, function(resp) {
		if(resp=='OK') {
			Ext.Msg.alert('Success', 'Node is moved to public folder', function() {
				document.location.href = 'content.edit.do?path='+destPath+'/'+node.name;
			});
		} else {
			Ext.Msg.alert('Error publishing node', resp);
		}
	});
};

selectPublishDest = function() {
	if(window._win) {
		_win.show();
	} else {
			var cfg = {
				filter: 'sys.public,category'
			}
			var chonExplTree = new chon.Explorer.Tree(cfg);
		_win = new Ext.Window({
			title: 'Choose Category to publish this node',
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
						//Ext.Msg.alert("OK, TODO", "Node will be published in " + path);
						jQuery('#publishing').show();
						jQuery('#dest').html(path);
						_win.hide();
					} else {
						Ext.Msg.alert("No Item Selected", "Please select category where you want to pubish current node");
					}
				}
			}]
		});
		_win.show();
	}
};
