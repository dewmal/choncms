jcr = {
	createNode: function(arg, node, editFnk, successFnk, scope) {
		if(arg=='save') {
			if(!node.type) {
				node.type = $('select[name="type"]').val();
			}
			if(!node.name) {
				node.name = $('input[name="name"]').val();
			}
			if(!node.pid) {
				node.pid = $('select[name="parent"]').val();
			}
			props = [];
			$('input', '#props').each(function() {
				if(this.name.indexOf('name_')==0) {
					var vid =  this.name.substring(5);
					props.push({
						name:  $(this).val(),
						value: $('input[name="value_'+vid+'"]', '#props').val()
					});
				}
			});
			node.props = props;
			if(typeof editFnk == 'function') {
				editFnk.call(scope||this, node);
			}
			//console.log(node);
			//return;
			$.post(siteUrl + '/admin/createNode.ajax', {node: JSON.encode(node)}, function(data) {
				var r = JSON.decode(data);
				if(r.msg=='OK') {
					if(typeof successFnk == 'function') {
						successFnk.call(scope||this, r);
					} else {
						document.location.href='index.do';
					}
				} else {
					alert(r.msg + '\n\n' + r.error);
				}
			});
		} else {
			var pid = $.jstree._reference('#jcr').get_selected().attr('id')
			document.location.href=siteUrl + '/admin/createNode.do'+(pid?'?pid='+pid:'');
		}
	},
	
	cancelCreateNode: function() {
		document.location.href = siteUrl + '/admin/browse.do';
	},
	
	PROP_IDPP: 10,
	
	addProp: function(difId) {
		this.PROP_IDPP++;
		var p = $('<div />').html([
		                   '<div id="ct_',this.PROP_IDPP,'">',
		                       'Name: <input type="text" name="name_',this.PROP_IDPP,'" /> &nbsp;&nbsp;',
		                       'Value: <input type="text" name="value_',this.PROP_IDPP,'" />',
		                       '<input type="button" name="del_',this.PROP_IDPP,'" value="X" onclick="jcr.remProp(',this.PROP_IDPP,')"/>',
		                    '</div>'].join(''))
		$('#'+difId).append(p)
	},
	remProp: function(id) {
		$('#ct_'+id).remove();
	},
	
	onTypeChange: function(a) {
		if($(a).val()=="nt:file") {
			$('#upload-file').show();
		} else {
			$('#upload-file').hide();
		}
	},
	
	selectDlg: function(cb, scope) {
		if(this._dlg) {
			this._dlg.dialog('open');
			return;
		}
		
		this._dlg = $('#dialog')
		
		this._dlg.dialog({
			width: 840,
			modal: true,
			resizable: false
		});
		jcr_tree.create('#jcr');
		var _this = this;
		if(typeof cb=='function') {
			jcr_tree.onNode('dblclick', function(node) {
				jQuery.getJSON(siteUrl + '/admin/getNode.ajax', {id: node.attr.id}, function(r) {
					cb.call(scope||jcr_tree.tree, r);
					_this._dlg.dialog('close');
				});
			});
		}
	}
}