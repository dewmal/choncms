mb_hide_to = 0;
$(function() {
	var files = [];
	#foreach($f in $this.files)
		files.push({name: '$f.name', contentType: '$f.mimeType' });
	#end
	upload.createFrame($('#uploadDiv')[0], uploadHandler);
	//if(node.files) {
		for(var i=0; i<files.length; i++) {
			var f = files[i];
			appendMedia(f.name, f.contentType);
		}
	//}
	
	media_buttons = $('<div style="position: absolute; top: 0px; left: 0px; display: none"> <button title="Click to detete" style="font-size:8px; font-weight: bold; border: solid gray 1px; cursor:pointer" onclick="removeMedia()">X</button> </div>');
	media_buttons.appendTo(document.body);
	$(media_buttons).hover(function() {
		if(mb_hide_to) {
			clearTimeout(mb_hide_to)
			mb_hide_to = 0;
		}
	}, function() {
		
	});
	
});


function uploadHandler(uploadedFile){
	jcr.createNode('save', {
		name: uploadedFile.fileName,
		pid: node.id,
		type: 'nt:file',
		fileName: uploadedFile.fileName,
		mime: uploadedFile.contentType
	}, /*edit fnk*/ null, /*success fnk*/function(r) {
		
		appendMedia(r.node.data,uploadedFile.contentType);
		$('#uploadDiv')[0].innerHTML = '';
		upload.createFrame($('#uploadDiv')[0], uploadHandler);
	}/*, optional scope for methods*/);
};

function appendMedia(name, contentType) {
	var ctDiv = $('<div cname="'+name+'" style="float: left; margin: 5px; width: 140px; height: 140px; text-align: center; overflow:hidden; border:solid gray 1px"></div>');
	if(contentType.indexOf('image')==0) {
		ctDiv.html('<table cellpadding="0" cellspacing="0" width="100%"><tr><td style="border-bottom: solid gray 1px; background:#fff;padding:0px"><img src="'+siteUrl+''+node.path+'/'+name+'?w=135&h=100&cut=3" style="margin-top:3px;" />' 
				+ "</td></tr><tr><td>"+'<div style="overflow: hidden;max-width:135px;font-size:9px">'+name+'</div>'+"</td></tr></table>");
	} else {
		ctDiv.html('<table cellpadding="0" cellspacing="0" width="100%"><tr><td style="border-bottom: solid gray 1px; background:#fff;padding:4px">'
				+ '<div style="height: 96px;">'
				+ '<br><a href="'+name+'">'+name+'</a><br><br>'+contentType
				+ '</div>'
				+ "</td></tr><tr><td>"+'<div style="overflow: hidden;max-width:135px;font-size:9px">'+name+'</div>'+"</td></tr></table>");
	}
	$('#imgCt').append(ctDiv);
	ctDiv.hover(function() {
		if(mb_hide_to) {
			clearTimeout(mb_hide_to)
			mb_hide_to = 0;
		}
		media_current = $(this);
		var pos = $(this).offset();
		media_buttons.css('left', pos.left+119).css('top', pos.top+3);
		media_buttons.show();
	}, function() {
		mb_hide_to = setTimeout(function() {
			media_buttons.hide();
		}, 500);
	});
}

function removeMedia() {
	var file = media_current.attr('cname');
	Ext.Msg.confirm('Are you sure?', 'Are you sure you want to permanently delete this resource?', function(btn) {
		if(btn=='yes') {
			$.post(siteUrl + '/admin/removeFile.ajax', {nodeId: node.id, file: file}, function(resp) {
				var r = JSON.decode(resp);
				if(r.msg == 'OK') {
					media_current.hide();
					media_buttons.hide();
				} else {
					alert(r.msg);
				}
			});
		}
	});
}
