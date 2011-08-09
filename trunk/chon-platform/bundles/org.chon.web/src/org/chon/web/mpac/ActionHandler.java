package org.chon.web.mpac;

import java.util.Map;

public interface ActionHandler {
	public Action getDefaulAction();
	public Map<String, Action> getActions();
}
