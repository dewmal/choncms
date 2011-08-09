upload = {
	getFrameDoc: function(ifrm) {
	//ifrm = (ifrm.contentWindow) ? ifrm.contentWindow : (ifrm.contentDocument.document) ? ifrm.contentDocument.document : ifrm.contentDocument;
		  var doc = null;
		  if(ifrm.contentDocument)
			  // Firefox, Opera
			  doc = ifrm.contentDocument;
		  else if(ifrm.contentWindow)
			  // Internet Explorer
			  doc = ifrm.contentWindow.document;
		  else if(ifrm.document)
			  // Others?
			  doc = ifrm.document;
		 return doc;
	},
	
	createFrame: function(div, result) {
		var html = [
			'<form id="upload_form" action="'+siteUrl+'/admin/file.upload" method="post" enctype="multipart/form-data">',
				'<input id="file_input_id" type="file" name="file1" />',
				//'<input type="submit" value="upload" />',
			'</form>',
			'<div id="progress" style="display: none">Uploading ... </div>'
		].join('');
		var ifrm = document.createElement('iframe');
		ifrm.style.border=0;
		ifrm.width='250px';
		ifrm.height='25px';
		ifrm.frameBorder=0;
		ifrm.marginHeight=0;
		ifrm.marginWidth=0;
		ifrm.scrolling='no';
		div.appendChild(ifrm);
		ifrm.src = ifrm.src;
		doc = this.getFrameDoc(ifrm);
        doc.open();
        doc.write(html);
        doc.close();
        
        doc.getElementById('file_input_id').onchange = function() {
        	doc.getElementById('upload_form').style.display='none';
        	doc.getElementById('progress').style.display='block';
        	setTimeout(function() {
				upload.uploadProgress(ifrm, doc.getElementById('progress'), result);
			}, 500);
        	doc.getElementById('upload_form').submit();
        }
	},
	
	uploadProgress: function(ifrm, progressDiv, result) {
		$.getJSON(siteUrl + '/admin/getUploadProgress.ajax', function(r) {
			if(r==null) {
				setTimeout(function() {
					upload.uploadProgress(ifrm, progressDiv, result);
				}, 500);
			} else {
				var p = 0;
				if(r.status=='progress') {
					p = Math.round( (r.bytesRead/r.totalSize) * 100);
					upload.uploadProgress(ifrm, progressDiv, result);
					progressDiv.innerHTML = p + "%";
				} else if(r.status == 'done') {
					p = 100;
					var d = upload.getFrameDoc(ifrm).getElementById("uploadInfo");
					var resp = JSON.decode(d.innerHTML);
					//console.log(resp);
					d.innerHTML = "Uploaded: " + resp.fileName;
					d.style.display="block";
					if(typeof(result) == 'function') {
						result(resp);
					} else {
						result.fileName = resp.fileName;
						result.mime = resp.contentType;
						result.fileSize = resp.fileSize;
					}
				} 
			}
		});
	}
}