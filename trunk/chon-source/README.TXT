Build choncms with maven build:

1. Build initial target-platfrom

	cd choncms-parent
	mvn package


2. Build bundles and include to target platrofm

	cd bundles
	mvn package


3. Modify properties from chon-cms-web-container/src/main/webapp/WEB-INF/system.properties
to point to your targrt platform and bundles (for static resources and templates) directories.

	commons.configson.resources=/path/to/bundles
	targetPlatform=/path/to/target-platform/target
	
Optional: upload, reposotory and config will be autocreated relative to system.properties file
modify those to point to location at your choise.
 

4. Run jetty
	cd  chon-cms-web-container
	mvn jetty:run
	
