Ext.ns("display.lists");

display.lists.createNew = function() {

	var txtId = Ext.id(); 
	var panel = new Ext.Panel({
		border: false,
		items: [{
			border: false,
			html: '<div style="margin: 10px"> Name: <input type="text" id="'+txtId+'" /> </div>' 
		}]
	});

			var _this = this;
			_this.win = new Ext.Window({
				title: "Creare New Display List",
				layout: 'fit',
				width:240, 
				height:110,
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
						var val = Ext.get(txtId).dom.value;
						if(/^[a-z_]*$/.test(val)) {
							createDisplayList(val);
						} else {
							Ext.Msg.alert("Invalid name", "Only lower latin letters are allowed in name");
						}
					}
				}]
			}).show();
}