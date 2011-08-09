package org.chon.web.mpac;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public interface InitStatusErrorHandler {
	public void handleStatusError(InitStatusInfo status, Application app, Request req, Response resp);
}
