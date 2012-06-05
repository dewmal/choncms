Bundle for providing simple MVC model for chon cms nodes.

How it works:
	1. It exposes "org.chon.cms.light.mvc.LightMVCService" service in osgi
	2. Implementation automatically creates nodes (on setupController call) and responds (calls) registered actions
	
	
How to use:
	1. Create Action (s) :
		public class MyAction extends AbstractAction {
			@Override
			public Result exec() {
				return R.html("Simple HTML output");
			}
		}
	
	2. Register controller:
	   - Track "org.chon.cms.light.mvc.LightMVCService" service and call setup its setupController method:
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
			Map<String, Class<? extends AbstractAction>> actions = new HashMap<String, Class<? extends AbstractAction>>();
			actions.put("my_action", MyAction.class);
			try {
				mvc.setupController("actions_root", actions);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	3. Url that corresponds to above example (MyAction) is:
		$ctx.siteUrl/actions_root/my_action