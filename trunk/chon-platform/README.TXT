Build choncms with maven build:

1. Bundle initial third party libraries

	cd bnd-libs
	mvn clean install


2. Build Core bundles

	cd bundles
	mvn clean install


	Optional: Build wiki-budnles
		cd wiki-feature-bundles/wiki-chon-libs
		mvn clean install
	
		cd wiki-feature-bundles
		mvn clean install
	

3. Build chon-cms-web-container

	cd  chon-cms-web-container
	mvn clean package
	 
	Optional: Modify properties from chon-cms-web-container/src/main/webapp/WEB-INF/system.properties
				to point to your targrt platform and bundles (for static resources and templates) directories.

		# to edit tempplates on fly
		commons.configson.resources=/path/to/source/bundles
		
		# to reload plugins for developement
		chon.dropins.dir=/path/to/eclipse/export/plugins
		
	
	Optional: upload, reposotory and config will be autocreated relative to system.properties file
		modify those to point to location at your choise.
 

4. Run jetty
	cd chon-cms-web-container
	mvn jetty:run
	
