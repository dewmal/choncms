set MAVEN_OPTS="-Xmx1024m"
cd choncms-parent
call mvn clean package
cd ..
cd bundles
call mvn clean install
cd ..
cd chon-cms-web-container
call mvn clean package
rem call mvn jetty:run


