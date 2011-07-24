work-dir is a container for all files outside code
 - system.properties
 - config ( json configuration files )
 - repository
 - resources
 - plugins (monitored plugins dir if org.chon.dev.tools.monitor deplyed)
 
 
config and repository dirs will be autocreated if not exits.
 
 
 
 
There are two scenarios for for configuring this dir

	1. Production Scenario
	RUN your server with -Dsystem-properties-dir=/my/path/to/app-work-dir
	witch should contain:
	  system.properties
	  ./config (json and all other configuration files)
	  ./repository (JCR repository)
	  ./resources (static resources and templates)
	  ./upload (temp upload dir)
	
	
	2. Developement Scenario
	Additional from "Production Scenario" is
	  ./plugins (dir that will be monitored for osgi plugins, using org.chon.dev.tools.monitor plugin)
  	