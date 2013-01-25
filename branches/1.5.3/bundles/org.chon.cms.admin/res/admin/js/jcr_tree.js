jcr_tree = {
		
	onNode: function(evtName, callback) {
		this.tree.get_container().delegate("a", evtName+".jstree", jQuery.proxy(function(e) {
			callback(this.tree.get_json(e.currentTarget)[0]);
		}, this));
	},
	
	create: function(container) {
		$(container).jstree({ 
			// the list of plugins to include
			"plugins" : [ "themes", "json_data", "ui", "hotkeys", "types", "contextmenu"],
			// Plugin configuration
	
			// I usually configure the plugin that handles the data first - in this case JSON as it is most common
			"json_data" : { 
				// I chose an ajax enabled tree - again - as this is most common, and maybe a bit more complex
				// All the options are the same as jQuery's except for `data` which CAN (not should) be a function
				"ajax" : {
					// the URL to fetch the data
					"url" : "./getNode.ajax",
					// this function is executed in the instance's scope (this refers to the tree instance)
					// the parameter is the node being loaded (may be -1, 0, or undefined when loading the root nodes)
					"data" : function (n) { 
						// the result is fed to the AJAX request `data` option
						return { 
							"node" : n.attr ? n.attr("id") : "" 
						}; 
					}
				},
				"progressive_render": true
			},
			"ui": {
			},
			// Using types - most of the time this is an overkill
			// Still meny people use them - here is how
			"types" : {
				// I set both options to -2, as I do not need depth and children count checking
				// Those two checks may slow jstree a lot, so use only when needed
				"max_depth" : -2,
				"max_children" : -2,
				// I want only `drive` nodes to be root nodes 
				// This will prevent moving or creating any other type as a root node
				//"valid_children" : [ "drive" ],
				"types" : {
					// The default type
					"default" : {
						// I want this type to have no children (so only leaf nodes)
						// In my case - those are files
						//"valid_children" : "none",
						// If we specify an icon for the default type it WILL OVERRIDE the theme icons
						//"icon" : {
						//	"image" : "./images/16x16/file.png"
						//}
					},
					"file": {
						"icon" : {
							"image" : "./images/16x16/file.png"
						}
					},
					// The `folder` type
					"folder" : {
						"icon" : {
							"image" : "./images/16x16/folder.png"
						}
					},
					// The `drive` nodes 
					"property" : {
						// can have files and folders inside, but NOT other `drive` nodes
						"valid_children" : "none",
						"icon" : {
							"image" : "./images/16x16/card-small.png"
							//"image" : "./images/16x16/asterisk-small-yellow.png"
						}
					},
					"system" : {
						"icon" : {
							"image" : "./images/16x16/root.png"
						}
					}, 
					"category" : {
						"icon" : {
							"image" : "./images/16x16/blue-folder-open-document-text.png"
						}
					},
					"html" : {
						"icon" : {
							"image" : "./images/16x16/document-text-image.png"
						}
					},
					"vest" : {
						"icon" : {
							"image" : "./images/16x16/document-text-image.png"
						}
					}
				}
			},
			/*
			"themes": {
				"theme": "apple"
			},*/
			"contextmenu": {
				select_node: true,
				items: {
					"create": {
						"label": "Create Node",
						"action": function(n) { console.log(n); }
					},
					"rename": {
						"label": "Go To ...",
						"action": function(n) {
						var tree_ref = jQuery.jstree._reference('#jcr');
							var s = ""+document.location;
							var base = s.substring(0, s.indexOf('admin/'));
							var arr = [];
							while(n!=-1) {								
								arr.push(tree_ref.get_text(n))
								n = tree_ref._get_parent(n);
							}
							for(var i=arr.length-2; i>=0; i--) {
								base += arr[i];
								if(i>0) base += "/";
							}
							document.location.href = base;
						}
					},
					"remove": {
						"label": "Brisi",
						"action": function(n) { 
							//jcr_tree.remove(n[0]);
							//console.log(n);
							//joco = n;
							if(confirm("Дали сте сигурни?")) {
								var id = n.attr('id');
								$.getJSON('deleteNode.ajax', {id: id}, function(r) {
									if(r.msg == 'OK') {
										document.location.href='index.do';
									} else {
										alert(r.msg + '\n\n' + r.error);
									}
								});
							}
						}
					},
					
					ccp: false
				}
			},
			// the core plugin - not many options here
			"core" : { 
				// just open those two nodes up
				// as this is an AJAX enabled tree, both will be downloaded from the server
				// "initially_open" : [ "node_2" , "node_3" ] 
				html_titles	: true,
				animation: false
			}
		});
		this.tree = jQuery.jstree._reference(container);
	}
}