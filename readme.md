

~~~~~~~~
mvn clean compile package
~~~~~~~~

Prior to deploying through maven central. Install it to the local .m2 repository

https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html

If you have the jar
~~~~~~~~
mvn install:install-file -Dfile=target/restmud-engine-1.3-jar-with-dependencies.jar -DpomFile=pom.xml
~~~~~~~~

Or, if just the jar

~~~~~~~~
mvn install:install-file -Dfile=target/restmud-engine-1.3-jar-with-dependencies.jar -DpomFile=pom.xml -DgroupId=uk.co.compendiumdev -DartifactId=restmud-engine -Dversion=1.3 -Dpackaging=jar
~~~~~~~~

For local development use to add jar and sources:

~~~~~~~~
mvn install:install-file -Dfile=target/restmud-engine-1.4-SNAPSHOT-jar-with-dependencies.jar -DpomFile=pom.xml -Dsources=target/restmud-engine-1.4-SNAPSHOT-sources.jar
~~~~~~~~