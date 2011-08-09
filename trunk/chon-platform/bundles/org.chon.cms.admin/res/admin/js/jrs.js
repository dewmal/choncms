jrs = {
	ajax : function(svcName, req, callback, scope) {
		if (!req)
			req = {};
		if (!req.sessionId)
			req.sessionId = 0;
		$.post(siteUrl + '/admin/jrs.ajax', {
			name : svcName,
			req : JSON.encode(req)
		}, function(data) {
			var resp = JSON.decode(data);
			callback.call(scope || this, resp);
		});
	}
}