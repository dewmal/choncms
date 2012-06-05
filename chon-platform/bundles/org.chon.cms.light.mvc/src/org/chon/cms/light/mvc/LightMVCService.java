package org.chon.cms.light.mvc;

import java.util.Map;


public interface LightMVCService {
	public void setupController(String root_path, Map<String, Class<? extends AbstractAction>> actionzz) throws Exception;
}
