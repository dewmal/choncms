set MAVEN_OPTS="-Xmx1024m"
cd choncms-parent
call mvn clean package
cd ..
cd bundles
call mvn clean package
cd ..
cd chon-cms-web-container
call mvn clean package
call mvn jetty:run


