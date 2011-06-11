package org.chon.cms.admin.mpac;

import java.util.Map;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.chon.web.mpac.ActionHandler;
import org.chon.web.util.FileInfo;
import org.json.JSONException;
import org.json.JSONObject;


public class AdminUpload implements ActionHandler {

	@Override
	public Map<String, Action> getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action getDefaulAction() {
		return new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				JSONObject fi = new JSONObject();
				try {
					FileInfo fileIno = req.getFiles().get(0);
					fi.put("fileName", fileIno.getFile().getName());
					fi.put("contentType", fileIno.getContentType());
					fi.put("fileSize", fileIno.getSize());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return "<div id=\"uploadInfo\" style=\"display: none\">" + fi.toString() + "</div>";
			}
		};
	}

}
