How to deploy and run the application on tomcat:

Unzipped dir will contain:
	<EXTRACT_LOCATION>/chon-home
	<EXTRACT_LOCATION>/chon-web
	
Note to replace <EXTRACT_LOCATION> and <TOMCAT_HOME> with your local absolute paths to unzipped dir and your tomcat home directory
	
1. Move (deploy) chon-web dir (application) in <TOMCAT_HOME>/webapps (it should work with any J2EE container, tested with tomcat 6.0)
2. Set startup variable for java vm on server startup
Navigate console to <TOMCAT_HOME>/bin
 - Windows: 
	SET JAVA_OPTS=-Dsystem-properties-dir="<EXTRACT_LOCATION>/chon-home"
	startup.bat
 - Linux
 	export JAVA_OPTS=-Dsystem-properties-dir="<EXTRACT_LOCATION>/chon-home"
	./startup.sh
3. Test on browser at: http://localhost:8080/chon-web
4. Login on admin part: http://localhost:8080/chon-web/admin/index.do
	user: admin
	pass: admin

5. Add content on admin part, use "Create/Edit Content" menu to create category, html...
	Initially nodes will be created in user dir, you need to publish them
	
For designers: 
Adjust visual aspect of the node, control how they will be shown
 - Modify default template
	<EXTRACT_LOCATION>/chon-home/resources/org.chon.cms.template/templates/default
 - Switch to new template
	<EXTRACT_LOCATION>/chon-home/config/org.chon.cms.template.json
	(see <EXTRACT_LOCATION>/chon-home/resources/org.chon.cms.template/templates for a list of available templates)