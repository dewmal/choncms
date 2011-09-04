if(!this.chon) chon={};

chon.forms = {		
	initForm: function(formId) {
		jQuery('#'+formId).submit(function() {
			var rv = true;
			jQuery('.required', this).each(function() {
				var v = jQuery(this).val();
				if(!v) {
					chon.forms.markInvalid(this); 
					rv = false;
				}
			});
			jQuery('.email', this).each(function() {
				var v = jQuery(this).val();
				var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				if (!filter.test(v)) {
					chon.forms.markInvalid(this); 
					rv = false;
				}
			});
			return rv;
		});
	},
	
	markInvalid: function(field) {
		var border = jQuery(field).css('border');
		jQuery(field).css('border', "solid red 2px");
		jQuery(field).focus((function(b) {
			return function() { jQuery(this).css('border', b) };
		})(border));
	}
};