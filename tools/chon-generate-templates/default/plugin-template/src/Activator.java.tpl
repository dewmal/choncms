package ${project-package};

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
#if($isSimpleTemplate)	
		// register breadcrumb extension
		app.regExtension("breadcrumb", new BreadcrumbExtenstion());
		
		// TODO register other extensions
#else
		// TODO register extensions
#end
	}

	@Override
	protected String getName() {
		return "${project-package}";
	}
	
}
