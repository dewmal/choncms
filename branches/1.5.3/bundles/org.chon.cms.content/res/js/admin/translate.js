translations = {
	Menu: function() {
		this.init.apply(this, arguments);
	}
}

$(function() {
	$("textarea.menu.original").each(function() {
		var name = $(this).attr('name');
		translations.Menu._map[name] = new translations.Menu(this); 
	});
});

translations.Menu._map = {};
translations.Menu.get = function(name) {
	return translations.Menu._map[name];
};

translations.Menu.toggleOriginal = function(inc_orig) {
	for(var k in translations.Menu._map) {
		var m = translations.Menu._map[k];
		m.setReadOnly(null, !inc_orig);
	}
};

translations.Menu.prototype = {
	data: null,
	
	setReadOnly: function(data, flag) {
		if(!data) data=this.data;
		for(var i=0; i<data.length; i++) {
			data[i]._tb.parent().parent().css('background', flag?'none':'white');
			if(data[i].childs) {
				this.setReadOnly(data[i].childs, flag);
			}
		}
	},
	
	init: function(ta) {
		
		var ta_t_id = $(ta).attr('id').substring("original_".length);
		
		var ta_t = $('#translation_'+ta_t_id)[0];
		
		
		this.data = JSON.decode(ta.value);
		this.findTranslations(ta_t.value ? JSON.decode(ta_t.value) : null);
		
		
		//console.log(this.data);
		
		this.ct_o = this.createCt(ta);
		this.ct_t = this.createCt(ta_t);
		
		this.createtf(this.data, 0);
	},
	
	findTranslations: function(tr_data) {
		this.attachTransl(this.data, tr_data||[]);
	},
	
	attachTransl: function(childs, tr_data) {
		for(var i=0; i<childs.length; i++) {
			childs[i].translation = this.findTranslationByLink(childs[i].link, tr_data);
			if(!childs[i].translation) {
				childs[i].translation={
					name: '',
					link: childs[i].link + "?lang=mk"
				};
			}
			if(childs[i].childs) {
				this.attachTransl(childs[i].childs, tr_data);
			}
		}
	},
	
	findTranslationByLink: function(link, tr_data) {
		if(!tr_data) return false;
		for(var i=0; i<tr_data.length; i++) {
			var n = tr_data[i].link.indexOf('?lang=mk');
			var t_link = tr_data[i].link.substring(0, n);  
			if(t_link==link) {
				return tr_data[i];
			}
			var rv = this.findTranslationByLink(link, tr_data[i].childs);
			if(rv) return rv;
		}
		return false;
	},
	
	createCt: function(ta) {
		var ct = $('<div />');
		ct.css('min-width', '300px');
		ct.css('text-align', 'left');
		$(ta).hide();
		$(ta).parent().append(ct);
		return ct;
	},
	
	createtf: function(childs, level) {
		if(childs) {
			for(var i=0; i<childs.length; i++) {
				var d = this.createLineDiv(childs[i], level);
				this.ct_o.append(d);
				
				var d_t = this.createLineDiv(childs[i].translation, level, true);
				this.ct_t.append(d_t);
				
				//recur..
				this.createtf(childs[i].childs, level+1);
			}
		}
	},
	
	createLineDiv: function(menu_node, level, isTransl) {
		var d = $('<div />');
		d.css('border-bottom', 'dotted green 1px');
		d.css('margin', '3px');
		if(isTransl) {
			d.css('background', 'white');
		}
		var tb = $('<input type="text" value="'+menu_node.name+'" style="border:0px;background:none"/>');
		var c =  $('<div style="margin-left:'+(30*level)+'px; padding:2px;" />');
		c.append(tb);
		d.append(c);
		menu_node._tb=tb;
		return d;
	},
	//c == original/translation
	getContent: function(c) {
		var d = this.collectData(this.data, c);
		return JSON.encode(d);
	},
	
	collectData: function(data, c) {
		var rv = [];
		for(var i=0; i<data.length; i++) {
			var u_mi = c=='original'?data[i]:data[i].translation;
			
			var mi = {
				link: u_mi.link,
				name: u_mi._tb.val()
			};
			if(data[i].childs) {
				mi.childs = this.collectData(data[i].childs, c);
			}
			rv.push(mi);
		}
		return rv;
	},
	
	copy: function(arr) {
		if(!arr) arr = this.data;
		for(var i=0; i<arr.length; i++) {
			if(arr[i].translation._tb.val()=='') {
				arr[i].translation._tb.val(arr[i]._tb.val());
			}
			if(arr[i].childs) {
				this.copy(arr[i].childs);
			}
		}
	}
}