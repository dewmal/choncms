package org.chon.cms.admin.explorer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.admin.explorer.ext.ExplorerExtension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Activator extends ResTplConfiguredActivator {
	private static final Log log = LogFactory.getLog(Activator.class);

	@Override
	protected String getName() {
		return "org.chon.cms.admin.explorer";
	}

	@Override
	protected void registerExtensions(JCRApplication app) {
		try {
			JSONObject config = getConfig();
			JSONArray jsIncArr = config.optJSONArray("jsinclude");
			List<String> jsIncList = new ArrayList<String>();
			if (jsIncArr != null && jsIncArr.length() > 0) {
				for (int i = 0; i < jsIncArr.length(); i++) {
					jsIncList.add(jsIncArr.getString(i));
				}
			}
			app.regExtension(
					"explorer",
					new ExplorerExtension(config.getJSONObject("config"), jsIncList));
		} catch (JSONException e) {
			log.error("Error while reading configuration", e);
		}

	}

}
