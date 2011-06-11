package org.chon.web.api.internal;

import org.chon.web.api.Resource;
import org.chon.web.api.ServerInfo;

public class ResourceProcessorRunner {

	public static void process(Resource r, ServerInfo si) {
		try {
			si.getApplication().prepareRequest(r, si);
			// MAIN RESOURCE PROCESSING ////////////// 
			r.process(si);
			//////////////////////////////////////////
		} catch (Exception e) {
			si.getApplication().handleException(e, r, si);
		} finally {
			si.getApplication().finalizeRequest(r, si);
		}
	}

}