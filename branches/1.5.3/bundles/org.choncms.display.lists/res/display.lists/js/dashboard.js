Ext.ns("display.lists");

display.lists.createNew = function() {

	var txtNameId = Ext.id();
	var txtTitleId = Ext.id();
	var panel = new Ext.Panel({
		border: false,
		items: [{
			border: false,
			style: 'margin: 10px',
			html: '<table><tr><td> Name: </td><td> <input type="text" id="'+txtNameId+'" /> <td></tr>' +
			'<tr><td> Title: </td><td> <input type="text" id="'+txtTitleId+'" /> <td></tr></table>'
		}]
	});

			var _this = this;
			_this.win = new Ext.Window({
				title: "Creare New Display List",
				layout: 'fit',
				width:240, 
				height:150,
				modal: true,
				items: panel,
				buttons: [{
					text: 'Cancel',
					handler: function() {
						_this.win.hide();
						_this.win.destroy();
					}
				}, {
					text: 'Save',
					handler: function() {
						var name = Ext.get(txtNameId).dom.value;
						if(/^[a-z_]*$/.test(name)) {
							createDisplayList(name, Ext.get(txtTitleId).dom.value);
						} else {
							Ext.Msg.alert("Invalid name", "Only lower latin letters are allowed in name");
						}
					}
				}]
			}).show();
}