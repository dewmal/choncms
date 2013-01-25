package org.chon.web.mpac;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public interface Initializer {
	public InitStatusInfo process(Application app, Request req, Response resp);
}
