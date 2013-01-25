set MAVEN_OPTS="-Xmx1024m"

cd bnd-libs
echo Building bnd-libs
call mvn clean install
cd ..

cd bundles
echo Building Core Bundles
call mvn clean install
cd ..

cd wiki-feature-bundles/wiki-chon-libs
echo Building wiki-libs
call mvn clean install
cd ..
echo Building wiki bundles
call mvn clean install
cd ..


cd chon-cms-web-container
echo Package war
call mvn clean package
rem call mvn jetty:run

echo Going to build p2 site
cd ../../tools/chon-p2
call mvn clean package


