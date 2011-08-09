package org.chon.core.velocity;

import org.apache.velocity.app.event.InvalidReferenceEventHandler;
import org.apache.velocity.context.Context;
import org.apache.velocity.util.introspection.Info;

public class InvalidRefEHJ implements InvalidReferenceEventHandler {

	@Override
	public Object invalidGetMethod(Context arg0, String arg1, Object arg2,
			String arg3, Info arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invalidMethod(Context ctx, String ref, Object obj,
			String method, Info info) {
		
		System.out.println("InvalidRefEHJ.invalidMethod() .. " + obj + " - " + method);
		return "The JOCO";
	}

	@Override
	public boolean invalidSetMethod(Context arg0, String arg1, String arg2,
			Info arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
