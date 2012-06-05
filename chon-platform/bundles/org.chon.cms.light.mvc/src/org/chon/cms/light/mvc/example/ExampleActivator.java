package org.chon.cms.light.mvc.example;

import org.chon.cms.light.mvc.LightMVCService;
import org.osgi.framework.ServiceReference;

public class ExampleActivator {
	/*
	private void trackMVCService() {
		ServiceTracker t = new ServiceTracker(getBundleContext(),
				LightMVCService.class.getName(), null) {
			@Override
			public Object addingService(ServiceReference reference) {
				LightMVCService mvc = (LightMVCService) super.addingService(reference);
				registerMVCModel(mvc);
				return mvc;
			}

			@Override
			public void removedService(ServiceReference reference,
					Object service) {
			}
		};
		t.open();
	}

	protected void registerMVCModel(LightMVCService mvc) {
		//ControllerInitialiser.init(mvc, getConfig());
	}
	*/
}
