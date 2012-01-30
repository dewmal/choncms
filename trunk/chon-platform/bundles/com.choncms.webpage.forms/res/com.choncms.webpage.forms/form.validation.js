if(!this.chon) chon={};

chon.forms = {
	validate: function(form) {
		var rv = true;
		jQuery('.required', form).each(function() {
			var v = jQuery(this).val();
			if(!v) {
				chon.forms.markInvalid(this); 
				rv = false;
			}
		});
		jQuery('.email', form).each(function() {
			var v = jQuery(this).val();
			var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			if (!filter.test(v)) {
				chon.forms.markInvalid(this); 
				rv = false;
			}
		});
		return rv;
	},
	
	ajaxsubmit: function(btn) {
		var form = $(btn).closest("form");
		if(chon.forms.validate(form)) {
			jQuery.post(ctx.siteUrl + '/' + chon.forms.AJAX_POST_NODE, form.serialize(), function(hm, msg, r) {
				//console.log(r.responseText);
				form.html(r.responseText);
			});
		}
	},
	
	initForm: function(formId) {
		var form = jQuery('#'+formId);
		form.submit(function() {
			return chon.forms.validate(form); 
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