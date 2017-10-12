

~~~~~~~~
mvn clean compile package
~~~~~~~~

Prior to deploying through maven central. Install it to the local .m2 repository

~~~~~~~~
mvn install:install-file -Dfile=target/restmud-engine-1.2-SNAPSHOT-jar-with-dependencies.jar -DpomFile=pom.xml
~~~~~~~~
