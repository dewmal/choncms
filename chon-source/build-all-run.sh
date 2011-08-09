#!/bin/bash
export MAVEN_OPTS="-Xmx1024m"

cd choncms-parent
mvn clean package

cd ..
cd bundles
mvn clean package

cd ..
cd chon-cms-web-container

mvn clean package
mvn jetty:run

cd ..
